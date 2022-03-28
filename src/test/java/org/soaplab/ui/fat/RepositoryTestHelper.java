package org.soaplab.ui.fat;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
		assertThat(foundFatsByName.get(0).getName()).isEqualTo(name);
		assertThat(foundFatsByName.get(0).getInci()).isEqualTo(inci);
		assertThat(foundFatsByName.get(0).getIns()).isEqualTo(ins);
		assertThat(foundFatsByName.get(0).getSapNaoh()).isEqualTo(sapNaoh);
		assertThat(foundFatsByName.get(0).getIodine()).isEqualTo(iodine);
		assertThat(foundFatsByName.get(0).getLauric()).isEqualTo(lauric);
		assertThat(foundFatsByName.get(0).getLinoleic()).isEqualTo(linoleic);
		assertThat(foundFatsByName.get(0).getLinolenic()).isEqualTo(linolenic);
		assertThat(foundFatsByName.get(0).getMyristic()).isEqualTo(myristic);
		assertThat(foundFatsByName.get(0).getOleic()).isEqualTo(oleic);
		assertThat(foundFatsByName.get(0).getPalmitic()).isEqualTo(palmitic);
		assertThat(foundFatsByName.get(0).getRicinoleic()).isEqualTo(ricinoleic);
		assertThat(foundFatsByName.get(0).getStearic()).isEqualTo(stearic);
	}

	public void assertThatFatNotExists(String name) {
		List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(0);
	}

	public Fat createFat() {
		UUID uuid = fatRepository.create(IngredientsRandomTestData.getFatBuilder().build());
		return fatRepository.get(uuid);
	}

}
