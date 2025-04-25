package org.soaplab.repository.microstream;

import java.util.Set;

import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRecipeRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

@Component
public class SoapRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<SoapRecipe> implements SoapRecipeRepository {

	private static final long serialVersionUID = 1L;

	private final FatRepository fatRepository;
	private final FragranceRecipeRepository fragranceRecipeRepository;
	private final LyeRecipeRepository lyeRecipeRepository;
	private final AdditiveRepository additiveRepository;

	public SoapRecipeRepositoryMSImpl(DataRoot dataRoot, SoaplabProperties properties,
			EmbeddedStorageManager repository, FragranceRecipeRepository fragranceRecipeRepository,
			LyeRecipeRepository lyeRecipeRepository, FatRepository fatRepository,
			AdditiveRepository additiveRepository) {
		super(dataRoot, properties, repository);
		this.fatRepository = fatRepository;
		this.fragranceRecipeRepository = fragranceRecipeRepository;
		this.lyeRecipeRepository = lyeRecipeRepository;
		this.additiveRepository = additiveRepository;

	}

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(SoapRecipe entity) {
		// reload composite entities to reflect potential changes on them
		entity.getFats().forEach(entry -> {
			final Fat fat = fatRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fat);
		});
		entity.getAdditives().forEach(entry -> {
			final Additive additive = additiveRepository.get(entry.getIngredient().getId());
			entry.setIngredient(additive);
		});
		entity.setLyeRecipe(lyeRecipeRepository.get(entity.getLyeRecipe().getId()));
		entity.setFragranceRecipe(fragranceRecipeRepository.get(entity.getFragranceRecipe().getId()));
	}

	@Override
	protected Set<SoapRecipe> getEntitiesInternal() {
		return getDataRoot().getAllSoapRecipes();
	}
}
