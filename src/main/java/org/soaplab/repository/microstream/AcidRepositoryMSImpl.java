package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.AcidRepository;
import org.springframework.stereotype.Component;

@Component
public class AcidRepositoryMSImpl extends IngredientRepositoryMSImpl<Acid> implements AcidRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<Acid> getEntitiesInternal() {
		return getDataRoot().getAllAcids();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Acid entity) {
		if (getDataRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getAcids().stream()
					.anyMatch(referencedEntity -> referencedEntity.getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
