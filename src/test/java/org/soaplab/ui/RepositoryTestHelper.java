package org.soaplab.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.soaplab.assertions.FatAssert;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.exception.EntityNotFoundException;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRecipeRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.testdata.RandomSoapRecipeRepositoryTestData;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RepositoryTestHelper {

	private final SoapRecipeRepository soapRecipeRepository;
	private final FatRepository fatRepository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;
	private final NaOHRepository naohRepository;
	private final KOHRepository kohRepository;
	private final AdditiveRepository additiveRepository;
	private final LyeRecipeRepository lyeRecipeRepository;
	private final FragranceRecipeRepository fragranceRecipeRepository;

	public void assertThatFatExists(String name, String inci) {
		final List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(1);
		assertThat(foundFatsByName.get(0).getName()).isEqualTo(name);
		assertThat(foundFatsByName.get(0).getInci()).isEqualTo(inci);
	}

	public void assertThatFatExists(UUID uuid) {
		final Fat fat = fatRepository.get(uuid);
		assertThat(fat).isNotNull();
	}

	public void assertThatFatHasValues(UUID id, String name, String inci, Integer ins, BigDecimal sapNaoh,
			Integer iodine, Integer lauric, Integer linoleic, Integer linolenic, Integer myristic, Integer oleic,
			Integer palmitic, Integer ricinoleic, Integer stearic) {
		final Fat existingFat = fatRepository.get(id);
		final Fat expectedFatValues = Fat.builder().id(id).name(name).inci(inci).ins(ins).sapNaoh(sapNaoh)
				.iodine(iodine).lauric(lauric).linoleic(linoleic).linolenic(linolenic).myristic(myristic).oleic(oleic)
				.palmitic(palmitic).ricinoleic(ricinoleic).stearic(stearic).build();
		FatAssert.assertThat(existingFat).isDeepEqualToExceptVersion(expectedFatValues);
	}

	public void assertThatFatHasSameValuesExceptVersion(Fat fat) {
		assertThatFatHasValues(fat.getId(), fat.getName(), fat.getInci(), fat.getIns(), fat.getSapNaoh(),
				fat.getIodine(), fat.getLauric(), fat.getLinoleic(), fat.getLinolenic(), fat.getMyristic(),
				fat.getOleic(), fat.getPalmitic(), fat.getRicinoleic(), fat.getStearic());
	}

	public void assertThatFatNotExists(String name) {
		final List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(0);
	}

	public void assertThatFatNotExists(UUID id) {
		assertThrows(EntityNotFoundException.class, () -> {
			fatRepository.get(id);
		});
	}

	public Fat createFat() {
		return fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
	}

	public Fat createFat(String name, String inci) {
		return fatRepository.create(RandomIngredientsTestData.getFatBuilder().name(name).inci(inci).build());
	}

	public RandomSoapRecipeRepositoryTestData createSoapRecipeWithRandomData() {
		final RandomSoapRecipeRepositoryTestData testData = createTestData();
		testData.createSoapRecipe();
		return testData;
	}

	public Fragrance createFragrance() {
		return fragranceRepository.create(RandomIngredientsTestData.getFragranceBuilder().build());
	}

	private RandomSoapRecipeRepositoryTestData createTestData() {
		return new RandomSoapRecipeRepositoryTestData(soapRecipeRepository, fatRepository, acidRepository,
				liquidRepository, fragranceRepository, naohRepository, kohRepository, additiveRepository,
				lyeRecipeRepository, fragranceRecipeRepository);
	}

	public RandomSoapRecipeRepositoryTestData createFragranceRecipe() {
		final RandomSoapRecipeRepositoryTestData testData = createTestData();
		testData.createFragranceRecipe();
		return testData;
	}

}
