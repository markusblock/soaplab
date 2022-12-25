package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Lye;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.LyeRepository;
import org.springframework.stereotype.Component;

@Component
public class LyeRepositoryMSImpl extends IngredientRepositoryMSImpl<Lye> implements LyeRepository {

	private static final long serialVersionUID = 1L;

	public LyeRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Set<Lye> getEntitiesInternal() {
		return repository.getRoot().getAllLyes();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(Lye entity) {
		if (repository.getRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getNaOH().getId().equals(entity.getId())
					|| soapRecipe.getKOH().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
