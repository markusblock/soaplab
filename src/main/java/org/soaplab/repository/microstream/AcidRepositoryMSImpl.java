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

	public AcidRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<Acid> getEntitiesInternal() {
		return repository.getRoot().getAllAcids();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Acid entity) {
		if (repository.getRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getAcids().stream()
					.anyMatch(referencedEntity -> referencedEntity.getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
