package org.soaplab.ui.views;

import java.util.Optional;

import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import lombok.Getter;

public abstract class EntityView<T extends NamedEntity> extends VerticalLayout
		implements BeforeEnterObserver, EntityViewListControllerCallback<T>, EntityViewDetailsControllerCallback<T> {

	private static final long serialVersionUID = 1L;

	private H1 title;

	private EntityDetails<T> entityDetails;
	private EntityList<T> entityList;

	@Getter
	private EntityRepository<T> repository;

	private Optional<T> selectedEntity = Optional.empty();

	private boolean editNewEntityMode;

	public EntityView(EntityRepository<T> repository) {
		super();
		this.repository = repository;

		setSizeFull();
	}

	protected abstract String getHeader();

	protected abstract EntityList<T> createEntityList(EntityViewListControllerCallback<T> callback);

	protected abstract EntityDetails<T> createEntityDetails(EntityViewDetailsControllerCallback<T> callback);

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		add(title);

		HorizontalLayout masterDetail = new HorizontalLayout();
		masterDetail.setSizeFull();

		entityList = createEntityList(this);
		entityList.setMinWidth(50, Unit.PERCENTAGE);
		masterDetail.add(entityList);
		entityDetails = createEntityDetails(this);
		masterDetail.add(entityDetails);

		masterDetail.setFlexGrow(0.8, entityList);
		add(masterDetail);

		entityList.selectFirstEntity();
	}

	@Override
	public void saveEntity(T entity) {
		if (editNewEntityMode) {
			entity = repository.create(entity);
			entityList.refreshAll();
		} else {
			repository.update(entity);
			entityList.refresh(entity);
		}
		editNewEntityMode = false;
		entityList.listenToSelectionChanges();
		entityList.select(entity);
		entityDetails.showEntity(entity);
	}

	@Override
	public void deleteEntity(T entity) {
		repository.delete(entity.getId());
		entityList.refreshAll();
		entityList.selectFirstEntity();
	}

	@Override
	public void editEntity(T entity) {
		entityList.ignoreSelectionChanges();
		entityDetails.editEntity(entity);
	}

	@Override
	public void cancelEditMode() {
		editNewEntityMode = false;
		entityList.listenToSelectionChanges();
		if (selectedEntity.isPresent()) {
			entityList.select(selectedEntity.get());
			entityDetails.showEntity(selectedEntity.get());
		} else {
			entityList.selectFirstEntity();
		}
	}

	@Override
	public void createNewEntity() {
		editNewEntityMode = true;
		entityList.ignoreSelectionChanges();
		Optional<T> selectedEntity = entityList.getSelectedEntity();
		T newEntity = createNewEmptyEntity();
		entityList.deselectAll();
		entityDetails.editEntity(newEntity);
		this.selectedEntity = selectedEntity;
	}

	@Override
	public void entitySelected(T entity) {
		selectedEntity = Optional.ofNullable(entity);
		entityDetails.showEntity(entity);
	}

	protected abstract T createNewEmptyEntity();
}
