package org.soaplab.ui.fat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.soaplab.assertions.FatAssert;
import org.soaplab.domain.Fat;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.exception.EntityNotFoundException;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.testdata.RandomSoapRecipeRepositoryTestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryTestHelper {

	@Autowired
	private NaOHRepository naohRepository;

	@Autowired
	private KOHRepository kohRepository;

	@Autowired
	private FatRepository fatRepository;

	@Autowired
	private AcidRepository acidRepository;

	@Autowired
	private LiquidRepository liquidRepository;

	@Autowired
	private FragranceRepository fragranceRepository;

	@Autowired
	private SoapRecipeRepository soapRecipeRepository;

	public void assertThatFatExists(String name, String inci) {
		final List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(1);
		assertThat(foundFatsByName.get(0).getName()).isEqualTo(name);
		assertThat(foundFatsByName.get(0).getInci()).isEqualTo(inci);
	}

	public void assertThatFatHasValues(String name, String inci, Integer ins, BigDecimal sapNaoh, Integer iodine,
			Integer lauric, Integer linoleic, Integer linolenic, Integer myristic, Integer oleic, Integer palmitic,
			Integer ricinoleic, Integer stearic) {
		final List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(1);
		final Fat existingFat = foundFatsByName.get(0);
		// not interested in ID diff -> use the ID of loaded fat
		final Fat expectedFatValues = Fat.builder().id(existingFat.getId()).name(name).inci(inci).ins(ins)
				.sapNaoh(sapNaoh).iodine(iodine).lauric(lauric).linoleic(linoleic).linolenic(linolenic)
				.myristic(myristic).oleic(oleic).palmitic(palmitic).ricinoleic(ricinoleic).stearic(stearic).build();
		FatAssert.assertThat(existingFat).isDeepEqualTo(expectedFatValues);
	}

	public void assertThatFatHasSameValuesExceptId(Fat fat) {
		assertThatFatHasValues(fat.getName(), fat.getInci(), fat.getIns(), fat.getSapNaoh(), fat.getIodine(),
				fat.getLauric(), fat.getLinoleic(), fat.getLinolenic(), fat.getMyristic(), fat.getOleic(),
				fat.getPalmitic(), fat.getRicinoleic(), fat.getStearic());
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

	public SoapRecipe createSoapRecipeWithRandomData() {
		final RandomSoapRecipeRepositoryTestData testData = new RandomSoapRecipeRepositoryTestData(soapRecipeRepository,
				fatRepository, acidRepository, liquidRepository, fragranceRepository, naohRepository, kohRepository);
		return testData.createSoapRecipe();
	}

}
