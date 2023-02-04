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

	public NaOHRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<NaOH> getEntitiesInternal() {
		return repository.getRoot().getAllNaOH();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(NaOH entity) {
		if (repository.getRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getNaOH().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
