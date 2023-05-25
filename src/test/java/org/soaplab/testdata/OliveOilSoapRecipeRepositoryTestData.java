package org.soaplab.testdata;

import java.util.Collection;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Entity;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.EntityRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Component
public class OliveOilSoapRecipeRepositoryTestData extends OliveOilSoapRecipeTestData {

	private final FatRepository fatRepository;
	private final LiquidRepository liquidRepository;
	private final AcidRepository acidRepository;
	private final FragranceRepository fragranceRepository;
	private final SoapRecipeRepository soapRecipeRepository;
	private final KOHRepository kohRepository;
	private final NaOHRepository naohRepository;
	private final AdditiveRepository additiveRepository;
	private final LyeRecipeRepository lyeRecipeRepository;

	private <T extends NamedEntity> T createEntityInRepo(T entity, EntityRepository<T> repository) {
		final Entity persistedEntity = repository.create(entity);
		return (T) persistedEntity;
	}

	private <T extends Ingredient> void createEntitiesInRepo(Collection<RecipeEntry<T>> recipeEntries,
			EntityRepository<T> repository) {
		for (RecipeEntry<T> recipeEntry : recipeEntries) {
			createEntityInRepo(recipeEntry.getIngredient(), repository);
		}
	}

	@Override
	protected Liquid createAcidAppleVinegar() {
		return createEntityInRepo(super.createAcidAppleVinegar(), liquidRepository);

	}

	@Override
	protected Acid createAcidCitric() {
		return createEntityInRepo(super.createAcidCitric(), acidRepository);
	}

	@Override
	protected Fat createFatCoconutOil() {
		return createEntityInRepo(super.createFatCoconutOil(), fatRepository);
	}

	@Override
	protected Fat createFatOliveOil() {
		return createEntityInRepo(super.createFatOliveOil(), fatRepository);
	}

	@Override
	protected Fragrance createFragranceLavendel() {
		return createEntityInRepo(super.createFragranceLavendel(), fragranceRepository);
	}

	@Override
	protected Liquid createLiquidWater() {
		return createEntityInRepo(super.createLiquidWater(), liquidRepository);
	}

	@Override
	protected LyeRecipe createLyeRecipe() {
		LyeRecipe lyeRecipe = super.createLyeRecipe();
		createEntitiesInRepo(lyeRecipe.getAcids(), acidRepository);
		createEntityInRepo(lyeRecipe.getKOH().getIngredient(),kohRepository);
		createEntityInRepo(lyeRecipe.getNaOH().getIngredient(), naohRepository);
		createEntitiesInRepo(lyeRecipe.getLiquids(), liquidRepository);
		createEntitiesInRepo(lyeRecipe.getAdditives(), additiveRepository);
		createEntityInRepo(lyeRecipe, lyeRecipeRepository);

		return lyeRecipe;
	}

	@Override
	public SoapRecipe createSoapRecipe() {
		return createEntityInRepo(super.createSoapRecipe(), soapRecipeRepository);
	}
}
