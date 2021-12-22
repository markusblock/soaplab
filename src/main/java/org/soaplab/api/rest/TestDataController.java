package org.soaplab.api.rest;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceType;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/testdata")
@Slf4j
public class TestDataController {

	private static final String APPLE_VINEGAR_NAME = "Apple Vinegar 5,1%";
	private static final String WATER_NAME = "Water";
	private static final String LAVENDEL_NAME = "Lavendel";
	private static final String CIDRIC_ACID_ANHYDRAT_NAME = "Cidric Acid anhydrat";
	private static final String COCONUT_OIL_NAME = "Coconut Oil";
	private static final String OLIVE_OIL_NAME = "Olive Oil";
	private static final String OLIVE_SOAP_RECIPE_NAME = "Olive Soap";
	private FatRepository fatRepository;
	private AcidRepository acidRepository;
	private LiquidRepository liquidRepository;
	private FragranceRepository fragranceRepository;
	private SoapRecipeRepository soapRecipeRepository;
	private Fat oliveOil;
	private Fat coconutOil;
	private SoapRecipe oliveSoap;
	private Fragrance lavendelFragrance;
	private Acid citricAcid;
	private Liquid water;
	private Liquid appleVinegar;

	@Autowired
	public TestDataController(FatRepository fatRepository, AcidRepository acidRepository,
			LiquidRepository liquidRepository, FragranceRepository fragranceRepository,
			SoapRecipeRepository soapRecipeRepository) {
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
		this.soapRecipeRepository = soapRecipeRepository;
		this.fatRepository = fatRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create() {

		log.info("creating test data");
		createFats();
		createFragrances();
		createAcids();
		createLiquids();
		createSoapRecipe();
	}

	@DeleteMapping
	public void delete() {
		soapRecipeRepository.delete(soapRecipeRepository.findByName(OLIVE_SOAP_RECIPE_NAME).get(0).getId());
		liquidRepository.delete(liquidRepository.findByName(WATER_NAME).get(0).getId());
		liquidRepository.delete(liquidRepository.findByName(APPLE_VINEGAR_NAME).get(0).getId());
		acidRepository.delete(acidRepository.findByName(CIDRIC_ACID_ANHYDRAT_NAME).get(0).getId());
		fatRepository.delete(fatRepository.findByName(OLIVE_OIL_NAME).get(0).getId());
		fatRepository.delete(fatRepository.findByName(COCONUT_OIL_NAME).get(0).getId());
		fragranceRepository.delete(fragranceRepository.findByName(LAVENDEL_NAME).get(0).getId());
	}

	private void createSoapRecipe() {
		oliveSoap = SoapRecipe.builder().name(OLIVE_SOAP_RECIPE_NAME).manufacturingDate(Date.from(Instant.now()))
				.naOHToKOHRatio(Percentage.of(100)).kOHPurity(Percentage.of(89.5))
				.fatsTotal(Weight.of(100, WeightUnit.GRAMS)).liquidToFatRatio(Percentage.of(33))
				.superFat(Percentage.of(10)).fragranceTotal(Percentage.of(3)).fats(//
						createIngredientEntriesMap( //
								createReceiptEntry(oliveOil, 80d), //
								createReceiptEntry(coconutOil, 20d)))
				.acids(createIngredientEntriesMap(createReceiptEntry(citricAcid, 4d)))
				.liquids(createIngredientEntriesMap(//
						createReceiptEntry(water, 50d), //
						createReceiptEntry(appleVinegar, 50d)))
				.fragrances(createIngredientEntriesMap(createReceiptEntry(lavendelFragrance, 100d))).build();
		soapRecipeRepository.create(oliveSoap);
	}

	private <T extends Ingredient> RecipeEntry<T> createReceiptEntry(T ingredient, Double percentage) {
		return RecipeEntry.<T>builder().ingredient(ingredient).percentage(Percentage.of(percentage)).build();
	}

	private <T extends Ingredient> Map<UUID, RecipeEntry<T>> createIngredientEntriesMap(
			RecipeEntry<T>... ingredientEntries) {
		Map<UUID, RecipeEntry<T>> entriesMap = new HashMap<>();
		Set.of(ingredientEntries).forEach(entry -> entriesMap.put(entry.getIngredient().getId(), entry));
		return entriesMap;
	}

	private void createFats() {
		oliveOil = Fat.builder().name(OLIVE_OIL_NAME).inci("Olea Europaea Fruit Oil").sapNaoh(0.1345d).build();
		coconutOil = Fat.builder().name(COCONUT_OIL_NAME).inci("Cocos Nucifera Oil").sapNaoh(0.183d).build();

		fatRepository.create(oliveOil, coconutOil);
	}

	private void createAcids() {
		citricAcid = Acid.builder().name(CIDRIC_ACID_ANHYDRAT_NAME).inci("Citric Acid").sapNaoh(0.625d).build();
		acidRepository.create(citricAcid);
	}

	private void createFragrances() {
		lavendelFragrance = Fragrance.builder().name(LAVENDEL_NAME).inci("").typ(FragranceType.VOLATILE_OIL).build();
		fragranceRepository.create(lavendelFragrance);
	}

	private void createLiquids() {
		water = Liquid.builder().name(WATER_NAME).inci("Aqua").build();
		appleVinegar = Liquid.builder().name(APPLE_VINEGAR_NAME).inci("").sapNaoh(0.666d * 0.051d).build();

		liquidRepository.create(water, appleVinegar);
	}
}
