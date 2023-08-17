package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.NaOH;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.NaOHRepository;
import org.springframework.stereotype.Component;

@Component
public class NaOHRepositoryMSImpl extends IngredientRepositoryMSImpl<NaOH> implements NaOHRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<NaOH> getEntitiesInternal() {
		return getDataRoot().getAllNaOH();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(NaOH entity) {
		if (getDataRoot().getAllLyeRecipes().stream().anyMatch(lyeRecipe -> {
			return lyeRecipe.getNaOH().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
