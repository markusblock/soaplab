package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.repository.AcidRepository;
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
	}

	@Override
	protected Set<LyeRecipe> getEntitiesInternal() {
		return getDataRoot().getAllLyeRecipes();
	}
}
