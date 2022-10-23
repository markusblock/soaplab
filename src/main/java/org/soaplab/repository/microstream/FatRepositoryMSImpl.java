package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Fat;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.FatRepository;
import org.springframework.stereotype.Component;

@Component
public class FatRepositoryMSImpl extends IngredientRepositoryMSImpl<Fat> implements FatRepository {

	private static final long serialVersionUID = 1L;

	public FatRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<Fat> getEntitiesInternal() {
		return repository.getRoot().getAllFats();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Fat entity) {
		if (repository.getRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getFats().stream()
					.anyMatch(referencedEntity -> referencedEntity.getId().equals(entity.getId()));
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
