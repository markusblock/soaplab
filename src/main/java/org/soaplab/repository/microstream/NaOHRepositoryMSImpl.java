package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.NaOH;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.exception.EntityDeletionFailedException.REASON;
import org.soaplab.repository.NaOHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import one.microstream.storage.types.StorageManager;

@Component
public class NaOHRepositoryMSImpl extends IngredientRepositoryMSImpl<NaOH> implements NaOHRepository {

	private static final long serialVersionUID = 1L;

	@Autowired
	public NaOHRepositoryMSImpl(StorageManager repository) {
		super(repository);
	}

	@Override
	protected Set<NaOH> getEntitiesInternal() {
		return getDataRoot().getAllNaOH();
	}

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(NaOH entity) {
		if (getDataRoot().getAllSoapReceipts().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getNaOH().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, REASON.ENTITY_STILL_REFERENCED);
		}
	}
}
