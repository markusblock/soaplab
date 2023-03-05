package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.KOH;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.KOHRepository;
import org.springframework.stereotype.Component;

@Component
public class KOHRepositoryMSImpl extends IngredientRepositoryMSImpl<KOH> implements KOHRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<KOH> getEntitiesInternal() {
		return getDataRoot().getAllKOH();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(KOH entity) {
		if (getDataRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getKOH().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
