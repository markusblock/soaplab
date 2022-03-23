package org.soaplab.ui.fat;

import static org.assertj.core.api.Assertions.assertThat;

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

	public void assertThatFatNotExists(String name) {
		List<Fat> foundFatsByName = fatRepository.findByName(name);
		assertThat(foundFatsByName).hasSize(0);
	}

	public Fat createFat() {
		UUID uuid = fatRepository.create(IngredientsRandomTestData.getFatBuilder().build());
		return fatRepository.get(uuid);
	}

}
