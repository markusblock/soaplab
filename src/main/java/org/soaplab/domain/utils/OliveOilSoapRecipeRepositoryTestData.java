package org.soaplab.domain.utils;

import org.soaplab.domain.*;
import org.soaplab.repository.*;
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
	private final FragranceRecipeRepository fragranceRecipeRepository;

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
	protected NaOH createNaOH() {
		return createEntityInRepo(super.createNaOH(), naohRepository);
	}

	@Override
	protected KOH createKOH() {
		return createEntityInRepo(super.createKOH(), kohRepository);
	}

	@Override
	protected Additive createMica() {
		return createEntityInRepo(super.createMica(), additiveRepository);
	}

	@Override
	protected Additive createSalt() {
		return createEntityInRepo(super.createSalt(), additiveRepository);
	}

	@Override
	protected Additive createSugar() {
		return createEntityInRepo(super.createSugar(), additiveRepository);
	}

	@Override
	protected LyeRecipe createLyeRecipe() {
		return createEntityInRepo(super.createLyeRecipe(), lyeRecipeRepository);
	}

	@Override
	protected FragranceRecipe createFragranceRecipe() {
		return createEntityInRepo(super.createFragranceRecipe(), fragranceRecipeRepository);
	}

	@Override
	public SoapRecipe createSoapRecipe() {
		return createEntityInRepo(super.createSoapRecipe(), soapRecipeRepository);
	}

	public void deleteSoapRecipe() {
		deleteEntityInRepository(IngredientsExampleData.SOAP_RECIPE_NAME, soapRecipeRepository);

		deleteEntityInRepository(IngredientsExampleData.LYE_RECIPE_NAME, lyeRecipeRepository);
		deleteEntityInRepository(IngredientsExampleData.FRAGRANCE_RECIPE_NAME, fragranceRecipeRepository);

		deleteEntityInRepository(IngredientsExampleData.OLIVE_OIL_NAME, fatRepository);
		deleteEntityInRepository(IngredientsExampleData.COCONUT_OIL_NAME, fatRepository);
		deleteEntityInRepository(IngredientsExampleData.LAVENDEL_NAME, fragranceRepository);
		deleteEntityInRepository(IngredientsExampleData.MICA_NAME, additiveRepository);

		deleteEntityInRepository(IngredientsExampleData.KOH_NAME, kohRepository);
		deleteEntityInRepository(IngredientsExampleData.NAOH_NAME, naohRepository);
		deleteEntityInRepository(IngredientsExampleData.CIDRIC_ACID_ANHYDRAT_NAME, acidRepository);
		deleteEntityInRepository(IngredientsExampleData.WATER_NAME, liquidRepository);
		deleteEntityInRepository(IngredientsExampleData.APPLE_VINEGAR_NAME, liquidRepository);
		deleteEntityInRepository(IngredientsExampleData.SALT_NAME, additiveRepository);
		deleteEntityInRepository(IngredientsExampleData.SUGAR_NAME, additiveRepository);
	}

	private <T extends NamedEntity> T createEntityInRepo(T entity, EntityRepository<T> repository) {
		final Entity persistedEntity = repository.create(entity);
		return (T) persistedEntity;
	}

	private void deleteEntityInRepository(String entityName, EntityRepository<?> repository) {
		repository.findByName(entityName).forEach(e -> repository.delete(e.getId()));
	}
}
