package org.soaplab.ui.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.grid.dataview.GridListDataView;

import lombok.SneakyThrows;

// TODO filter price i18n 0,17 instead of 0.17
//TODO reload on filter change
class EntityFilter<T> {
	private GridListDataView<T> dataView;

	private final Map<String, String> filterPropertyNameSearchTermPairs = new HashMap<>();

	public EntityFilter() {
	}

	public void setDataView(GridListDataView<T> dataView) {
		this.dataView = dataView;
		this.dataView.addFilter(this::test);
	}

	public void setFilterToProperty(String propertyName, String searchTerm) {
		this.filterPropertyNameSearchTermPairs.put(propertyName, searchTerm);
		this.dataView.refreshAll();
	}

	public boolean test(T entity) {
		final Set<Map.Entry<String, String>> entries = filterPropertyNameSearchTermPairs.entrySet();
		for (final Map.Entry<String, String> entry : entries) {
			final String entityPropertyValue = extractPropertyValueFromEntity(entity, entry.getKey());
			if (!matches(entityPropertyValue, entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	@SneakyThrows
	public String extractPropertyValueFromEntity(T entity, String propertyName) {
		return ObjectUtils.nullSafeToString(PropertyUtils.getProperty(entity, propertyName));
	}

	private boolean matches(String entityPropertyValue, String searchTerm) {
		return ObjectUtils.isEmpty(searchTerm) || entityPropertyValue.toLowerCase().contains(searchTerm.toLowerCase());
	}
}