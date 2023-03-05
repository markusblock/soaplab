package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Liquid;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.LiquidRepository;
import org.springframework.stereotype.Component;

@Component
public class LiquidRepositoryMSImpl extends IngredientRepositoryMSImpl<Liquid> implements LiquidRepository {

	private static final long serialVersionUID = 1L;

	@Override
	protected Set<Liquid> getEntitiesInternal() {
		return getDataRoot().getAllLiquids();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Liquid entity) {
		if (getDataRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getLiquids().stream()
					.anyMatch(referencedEntity -> referencedEntity.getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
