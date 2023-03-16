package org.soaplab.api.rest;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
import org.soaplab.domain.utils.IngredientsExampleData;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.EntityRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
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
@RequestMapping("soaplab/rest/testdata")
@Slf4j
public class TestDataController {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Olive Soap";
	private final FatRepository fatRepository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;
	private final SoapRecipeRepository soapRecipeRepository;
	private final NaOHRepository naOHRepository;
	private final KOHRepository kOHRepository;
	private Fat oliveOil;
	private Fat coconutOil;
	private SoapRecipe oliveSoap;
	private Fragrance lavendelFragrance;
	private Acid citricAcid;
	private Liquid water;
	private Liquid appleVinegar;
	private NaOH naOH;
	private KOH kOH;

	@Autowired
	public TestDataController(FatRepository fatRepository, AcidRepository acidRepository,
			LiquidRepository liquidRepository, FragranceRepository fragranceRepository,
			SoapRecipeRepository soapRecipeRepository, NaOHRepository naOHRepository, KOHRepository kOHRepository) {
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
		this.soapRecipeRepository = soapRecipeRepository;
		this.fatRepository = fatRepository;
		this.naOHRepository = naOHRepository;
		this.kOHRepository = kOHRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create() {

		log.info("creating test data");

		createFats();
		createFragrances();
		createAcids();
		createLiquids();
		createLyes();
		createSoapRecipe();
	}

	@DeleteMapping
	public void delete() {
		deleteByNameFromRepository(soapRecipeRepository, OLIVE_SOAP_RECIPE_NAME);
		deleteByNameFromRepository(kOHRepository, IngredientsExampleData.KOH_NAME);
		deleteByNameFromRepository(naOHRepository, IngredientsExampleData.NAOH_NAME);
		deleteByNameFromRepository(liquidRepository, IngredientsExampleData.WATER_NAME);
		deleteByNameFromRepository(liquidRepository, IngredientsExampleData.APPLE_VINEGAR_NAME);
		deleteByNameFromRepository(acidRepository, IngredientsExampleData.CIDRIC_ACID_ANHYDRAT_NAME);
		deleteByNameFromRepository(fragranceRepository, IngredientsExampleData.LAVENDEL_NAME);
		deleteByNameFromRepository(fatRepository, IngredientsExampleData.OLIVE_OIL_NAME);
		deleteByNameFromRepository(fatRepository, IngredientsExampleData.COCONUT_OIL_NAME);
	}

	private void deleteByNameFromRepository(EntityRepository<?> repository, String name) {
		repository.findByName(name).forEach(entity -> repository.delete(entity.getId()));
	}

	private void createSoapRecipe() {
		oliveSoap = SoapRecipe.builder().name(OLIVE_SOAP_RECIPE_NAME).manufacturingDate(Date.from(Instant.now()))
				.naOH(createRecipeEntry(naOH, 100d)) //
				.kOH(createRecipeEntry(kOH, 0d)) //
				.fatsTotal(Weight.of(100, WeightUnit.GRAMS)).liquidToFatRatio(Percentage.of(33))
				.superFat(Percentage.of(10)).fragranceToFatRatio(Percentage.of(3)).fats(List.of( //
						createRecipeEntry(oliveOil, 80d), //
						createRecipeEntry(coconutOil, 20d)))
				.acids(List.of(//
						createRecipeEntry(citricAcid, 4d)))
				.liquids(List.of(//
						createRecipeEntry(water, 50d), //
						createRecipeEntry(appleVinegar, 50d)))
				.fragrances(List.of(//
						createRecipeEntry(lavendelFragrance, 100d)))
				.build();
		soapRecipeRepository.create(oliveSoap);
	}

	private void createFats() {
		oliveOil = fatRepository.create(IngredientsExampleData.getOliveOilBuilder().build());
		coconutOil = fatRepository.create(IngredientsExampleData.getCoconutOilBuilder().build());
	}

	private void createAcids() {
		citricAcid = acidRepository.create(IngredientsExampleData.getCitricAcidBuilder().build());
	}

	private void createFragrances() {
		lavendelFragrance = fragranceRepository.create(IngredientsExampleData.getLavendelFragranceBuilder().build());
	}

	private void createLiquids() {
		water = liquidRepository.create(IngredientsExampleData.getWaterBuilder().build());
		appleVinegar = liquidRepository.create(IngredientsExampleData.getAppleVinegarBuilder().build());
	}

	private void createLyes() {
		naOH = naOHRepository.create(IngredientsExampleData.getNaOHBuilder().build());
		kOH = kOHRepository.create(IngredientsExampleData.getKOHBuilder().build());
	}
}
