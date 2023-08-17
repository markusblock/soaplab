package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SoapRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<SoapRecipe> implements SoapRecipeRepository {

	private static final long serialVersionUID = 1L;

	private final FatRepository fatRepository;
	private final FragranceRepository fragranceRepository;
	private final LyeRecipeRepository lyeRecipeRepository;
	private final AdditiveRepository additiveRepository;

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(SoapRecipe entity) {
		// reload composite entities to reflect potential changes on them
		entity.getFats().forEach(entry -> {
			final Fat fat = fatRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fat);
		});
		entity.getFragrances().forEach(entry -> {
			final Fragrance fragrance = fragranceRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fragrance);
		});
		entity.getAdditives().forEach(entry -> {
			final Additive additive = additiveRepository.get(entry.getIngredient().getId());
			entry.setIngredient(additive);
		});
		entity.setLyeRecipe(lyeRecipeRepository.get(entity.getLyeRecipe().getId()));
	}

	@Override
	protected Set<SoapRecipe> getEntitiesInternal() {
		return getDataRoot().getAllSoapRecipes();
	}
}
