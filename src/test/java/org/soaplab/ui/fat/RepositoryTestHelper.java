package org.soaplab.ui.fat;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.soaplab.assertions.FatAssert;
import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.testdata.IngredientsRandomTestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryTestHelper {

	@Autowired
	private FatRepository fatRepository;

	public void assertThatFatExists(String name, String inci) {
		List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(1);
		assertThat(foundFatsByName.get(0).getName()).isEqualTo(name);
		assertThat(foundFatsByName.get(0).getInci()).isEqualTo(inci);
	}

	public void assertThatFatHasValues(String name, String inci, Integer ins, BigDecimal sapNaoh, Integer iodine,
			Integer lauric, Integer linoleic, Integer linolenic, Integer myristic, Integer oleic, Integer palmitic,
			Integer ricinoleic, Integer stearic) {
		List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(1);
		Fat existingFat = foundFatsByName.get(0);
		// not interested in ID diff -> use the ID of loaded fat
		Fat expectedFatValues = Fat.builder().id(existingFat.getId()).name(name).inci(inci).ins(ins).sapNaoh(sapNaoh)
				.iodine(iodine).lauric(lauric).linoleic(linoleic).linolenic(linolenic).myristic(myristic).oleic(oleic)
				.palmitic(palmitic).ricinoleic(ricinoleic).stearic(stearic).build();
		FatAssert.assertThat(existingFat).isDeepEqualTo(expectedFatValues);
	}

	public void assertThatFatNotExists(String name) {
		List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(0);
	}

	public Fat createFat() {
		return fatRepository.create(IngredientsRandomTestData.getFatBuilder().build());
	}

	public Fat createFat(String name, String inci) {
		return fatRepository.create(IngredientsRandomTestData.getFatBuilder().name(name).inci(inci).build());
	}

}
