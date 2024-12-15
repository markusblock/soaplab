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

	private Optional<T> selectedEntity = Optional.empty();

	private boolean editNewEntityMode;

	public EntityView(EntityRepository<T> repository, String headerKey) {
		super();
		this.repository = repository;
		this.headerKey = headerKey;

		setSizeFull();
	}

	protected abstract EntityTablePanel<T> createEntityTable(EntityTableListener<T> callback);

	protected abstract EntityDetailsPanel<T> createEntityDetails(EntityDetailsListener<T> callback);

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
//		addButton.addClickListener(event -> {
//			log.trace("add button clicked");
//			if (grid.getEditor().isOpen()) {
//				grid.getEditor().closeEditor();
//			}
//			final T newEntity = createNewEmptyEntity();
//			focusedColumn = Optional.of(grid.getColumns().get(0));
//			grid.getListDataView().addItem(newEntity);
//			editEntity(newEntity);
//		});
//		addButton.setEnabled(createEntityFunction);
		headerPanel.add(addButton);

		removeButton = new Button();
		removeButton.setId("entitylist.remove");
		removeButton.setIcon(VaadinIcon.MINUS.create());
//		removeButton.addClickListener(event -> {
//			grid.getSelectionModel().getFirstSelectedItem().ifPresent(entity -> deleteEntity(entity));
//		});
		removeButton.setEnabled(false);
		headerPanel.add(removeButton);

		entityTablePanel = createEntityTable(this);
		entityDetails = createEntityDetails(this);
		SplitLayout splitLayout = new SplitLayout(entityTablePanel, entityDetails);
		splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(50);
		add(splitLayout);

		final HorizontalLayout masterDetail = new HorizontalLayout();
		masterDetail.setSizeFull();

//		entityList = createEntityTable(this);
//		entityList.setMinWidth(50, Unit.PERCENTAGE);
//		masterDetail.add(entityList);
//		entityDetails = createEntityDetails(this);
//		masterDetail.add(entityDetails);
//
//		masterDetail.setFlexGrow(0.8, entityList);
//		add(masterDetail);

		List<T> allEntities = repository.findAll();
		entityTablePanel.setEntities(allEntities);

		entityTablePanel.selectFirstEntity();
	}

	@Override
	public void entityChanged(T entity) {
		saveEntity(entity);
	}

	@Override
	public void selectionChanged(Optional<T> entity) {
		entityDetails.showEntity(entity.orElse(null));
	}

	@Override
	public void enterEditMode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leaveEditMode() {
		// TODO Auto-generated method stub

	}

//	private void deleteEntity(T entity) {
//		log.trace("delete button pressed");
//		repository.delete(entity.getId());
//		refreshTable();
//	}

	public void saveEntity(T entity) {
		if (editNewEntityMode) {
			entity = repository.create(entity);
			// refresh full table
		} else {
			repository.update(entity);
			// refresh only table row
		}
		entityTablePanel.setEntities(repository.findAll());
		entityDetails.setEntity(entity);

//		editNewEntityMode = false;
//		entityList.listenToSelectionChanges();
//		entityList.select(entity);
//		entityDetails.showEntity(entity);
	}

	private void deleteEntity(T entity) {
		repository.delete(entity.getId());
		entityTablePanel.setEntities(repository.findAll());
//		entityList.refreshAll();
//		entityList.selectFirstEntity();
	}
//
//	@Override
//	public void editEntity(T entity) {
//		entityList.ignoreSelectionChanges();
//		entityDetails.editEntity(entity);
//	}
//
//	@Override
//	public void cancelEditMode() {
//		editNewEntityMode = false;
//		entityList.listenToSelectionChanges();
//		if (selectedEntity.isPresent()) {
//			entityList.select(selectedEntity.get());
//			entityDetails.showEntity(selectedEntity.get());
//		} else {
//			entityList.selectFirstEntity();
//		}
//	}
//
//	@Override
//	public void createNewEntity() {
//		editNewEntityMode = true;
//		entityList.ignoreSelectionChanges();
//		final Optional<T> selectedEntity = entityList.getSelectedEntity();
//		final T newEntity = createNewEmptyEntity();
//		entityList.deselectAll();
//		entityDetails.editEntity(newEntity);
//		this.selectedEntity = selectedEntity;
//	}
//
//	@Override
//	public void entitySelected(T entity) {
//		T refreshedEntity = null;
//		if (entity != null) {
//			// reload entity on selection
//			refreshedEntity = repository.get(entity.getId());
//		}
//		selectedEntity = Optional.ofNullable(refreshedEntity);
//		entityDetails.showEntity(refreshedEntity);
//	}

	protected abstract T createNewEmptyEntity();
}
