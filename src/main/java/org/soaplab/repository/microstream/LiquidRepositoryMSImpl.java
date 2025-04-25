package org.soaplab.repository.microstream;

import java.util.Set;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.LiquidRepository;
import org.springframework.stereotype.Component;

@Component
public class LiquidRepositoryMSImpl extends IngredientRepositoryMSImpl<Liquid> implements LiquidRepository {

	private static final long serialVersionUID = 1L;

	public LiquidRepositoryMSImpl(DataRoot dataRoot, SoaplabProperties properties, EmbeddedStorageManager repository) {
		super(dataRoot, properties, repository);
	}

	@Override
	protected Set<Liquid> getEntitiesInternal() {
		return getDataRoot().getAllLiquids();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Liquid entity) {
		if (getDataRoot().getAllLyeRecipes().stream().anyMatch(lyeRecipe -> {
			return lyeRecipe.getLiquids().stream()
					.anyMatch(recipeEntry -> recipeEntry.getIngredient().getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
