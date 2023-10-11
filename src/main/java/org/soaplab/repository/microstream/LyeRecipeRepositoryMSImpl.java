package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.*;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.NaOHRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LyeRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<LyeRecipe> implements LyeRecipeRepository {

	private static final long serialVersionUID = 1L;

	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final NaOHRepository naohRepository;
	private final KOHRepository kohRepository;
	private final AdditiveRepository additiveRepository;

	@Override
	protected void assertEntityIsNotReferencedByOtherEntities(LyeRecipe entity) {
		if (getDataRoot().getAllSoapRecipes().stream().anyMatch(soapRecipe -> {
			return soapRecipe.getLyeRecipe().getId().equals(entity.getId());
		})) {
			throw new EntityDeletionFailedException(entity, EntityDeletionFailedException.REASON.ENTITY_STILL_REFERENCED);
		}
	}

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(LyeRecipe entity) {
		// reload composite entities to reflect potential changes on them
		if (entity.getNaOH() != null) {
			entity.getNaOH().setIngredient(naohRepository.get(entity.getNaOH().getIngredient().getId()));
		}
		if (entity.getKOH() != null) {
			entity.getKOH().setIngredient(kohRepository.get(entity.getKOH().getIngredient().getId()));
		}
		entity.getAcids().forEach(entry -> {
			final Acid acid = acidRepository.get(entry.getIngredient().getId());
			entry.setIngredient(acid);
		});
		entity.getLiquids().forEach(entry -> {
			final Liquid liquid = liquidRepository.get(entry.getIngredient().getId());
			entry.setIngredient(liquid);
		});
		entity.getAdditives().forEach(entry -> {
			final Additive additive = additiveRepository.get(entry.getIngredient().getId());
			entry.setIngredient(additive);
		});
	}

	@Override
	protected Set<LyeRecipe> getEntitiesInternal() {
		return getDataRoot().getAllLyeRecipes();
	}
}
