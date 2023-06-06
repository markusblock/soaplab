package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Fragrance;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.FragranceRepository;
import org.springframework.stereotype.Component;

@Component
public class FragranceRepositoryMSImpl extends IngredientRepositoryMSImpl<Fragrance> implements FragranceRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<Fragrance> getEntitiesInternal() {
		return getDataRoot().getAllFragrances();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Fragrance entity) {
		if (getDataRoot().getAllSoapRecipes().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getFragrances().stream()
					.anyMatch(referencedEntity -> referencedEntity.getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
