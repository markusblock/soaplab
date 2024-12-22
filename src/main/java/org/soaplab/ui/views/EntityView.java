package org.soaplab.ui.views;

import java.util.List;
import java.util.Optional;

import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class EntityView<T extends NamedEntity> extends VerticalLayout
		implements BeforeEnterObserver, EntityTableListener<T>, EntityDetailsListener<T> {

	private static final long serialVersionUID = 1L;

	private H1 title;

	private Button addButton;
	private Button removeButton;

	private EntityTablePanel<T> entityTablePanel;
	private EntityDetailsPanel<T> entityDetails;

	@Getter
	private final EntityRepository<T> repository;
	private final String headerKey;

	private boolean editNewEntityMode;

	public EntityView(EntityRepository<T> repository, String headerKey) {
		super();
		this.repository = repository;
		this.headerKey = headerKey;

		setSizeFull();
	}

	protected abstract EntityTablePanel<T> createEntityTable(EntityTableListener<T> callback);

	protected abstract EntityDetailsPanel<T> createEntityDetails(EntityDetailsListener<T> callback);

	protected boolean isCreateNewEntityAllowed() {
		return true;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		removeAll();

		final HorizontalLayout headerPanel = new HorizontalLayout();
		headerPanel.setWidthFull();
		add(headerPanel);

		title = new H1(getTranslation(headerKey));
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		headerPanel.add(title);

		addButton = new Button();
		addButton.setId("entitylist.add");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(clickEvent -> {
			log.trace("add button clicked");
			editNewEntityMode = true;
			removeButton.setEnabled(false);
			addButton.setEnabled(false);
			entityTablePanel.cancelEditMode();
			entityTablePanel.removeSelection();
			entityDetails.leaveEditMode();
			final T newEntity = createNewEmptyEntity();
			entityDetails.showEntity(Optional.of(newEntity));
			entityDetails.enterEditMode();
		});
		addButton.setEnabled(isCreateNewEntityAllowed());
		headerPanel.add(addButton);

		removeButton = new Button();
		removeButton.setId("entitylist.remove");
		removeButton.setIcon(VaadinIcon.MINUS.create());
		removeButton.addClickListener(clickEvent -> {
			log.trace("remove button clicked");
			entityTablePanel.cancelEditMode();
			entityDetails.leaveEditMode();
			final Optional<T> selectedEntity = entityTablePanel.getSelectedEntity();
			selectedEntity.ifPresent(t -> deleteEntity(t));
		});
		removeButton.setEnabled(true);
		headerPanel.add(removeButton);

		entityTablePanel = createEntityTable(this);
		entityDetails = createEntityDetails(this);
		final SplitLayout splitLayout = new SplitLayout(entityTablePanel, entityDetails);
		splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(50);
		add(splitLayout);

		final HorizontalLayout masterDetail = new HorizontalLayout();
		masterDetail.setSizeFull();

		final List<T> allEntities = repository.findAll();
		entityTablePanel.setEntities(allEntities);

		entityTablePanel.selectFirstEntity();
	}

	@Override
	public void entityChangedInEntityDetails(T entity) {
		log.trace("entityChangedInEntityDetails" + entity);
		final T savedEntity = saveEntity(entity);
		if (editNewEntityMode) {
			// refresh full table
			entityTablePanel.setEntities(repository.findAll());
		} else {
			// refresh only table row
			entityTablePanel.refreshEntity(savedEntity);
		}
		entityTablePanel.select(savedEntity);
	}

	@Override
	public void entityDetailsPanelEntersEditMode() {
		entityTablePanel.cancelEditMode();
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
	}

	@Override
	public void entityDetailsPanelLeavesEditMode() {
		editNewEntityMode = false;
		addButton.setEnabled(isCreateNewEntityAllowed());
		removeButton.setEnabled(true);
		entityTablePanel.select(entityTablePanel.getSelectedEntity().orElse(null));
	}

	@Override
	public void entityChangedInEntityTable(T entity) {
		log.trace("entityChangedInEntityTable" + entity);
		saveEntity(entity);
		entityDetails.showEntity(Optional.of(entity));
	}

	@Override
	public void selectionChangedInEntityTable(Optional<T> entity) {
		log.trace("selectionChangedInEntityTable " + entity);
		entityDetails.showEntity(entity);
	}

	@Override
	public void entityTableEntersEditMode() {
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
		entityDetails.cancelEditMode();
	}

	@Override
	public void entityTableLeavesEditMode() {
		addButton.setEnabled(isCreateNewEntityAllowed());
		removeButton.setEnabled(true);
	}

	public T saveEntity(T entity) {
		log.trace("saveEntity " + entity);
		if (editNewEntityMode) {
			entity = repository.create(entity);
			// refresh full table
		} else {
			entity = repository.update(entity);
			// refresh only table row
		}

		return entity;
	}

	private void deleteEntity(T entity) {
		log.trace("deleteEntity " + entity);
		repository.delete(entity.getId());
		entityTablePanel.setEntities(repository.findAll());
		entityTablePanel.selectFirstEntity();
	}

	protected abstract T createNewEmptyEntity();
}
