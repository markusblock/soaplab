package org.soaplab.repository.microstream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.soaplab.assertions.FatAssert.assertThat;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.domain.Fat;
import org.soaplab.domain.exception.DuplicateIdException;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.repository.FatRepository;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FatRepositoryIT {
	@LocalServerPort
	private int port;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@Autowired
	private FatRepository fatRepository;

	@Test
	void createEntity() throws Exception {
		final Fat fat = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
		final Fat loadedFat = fatRepository.get(fat.getId());
		assertThat(loadedFat).isDeepEqualTo(fat);
	}

	@Test
	void createEntityWithDuplicateIdIsNotAllowed() throws Exception {
		final Fat fat = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());

		final Exception exception = assertThrows(DuplicateIdException.class, () -> {
			fatRepository.create(RandomIngredientsTestData.getFatBuilder().id(fat.getId()).build());
		});

		final String expectedMessage = "Entity with id '" + fat.getId() + "' already exists";
		final String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void createEntityWithDuplicateNameIsNotAllowed() throws Exception {
		final Fat fat = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());

		final Exception exception = assertThrows(DuplicateNameException.class, () -> {
			fatRepository.create(RandomIngredientsTestData.getFatBuilder().name(fat.getName()).build());
		});

		final String expectedMessage = "Entity with name '" + fat.getName() + "' already exists";
		final String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void updateEntityWithDuplicateNameIsNotAllowed() throws Exception {
		final Fat fat1 = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
		final Fat fat2 = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
		fat2.setName(fat1.getName());

		final Exception exception = assertThrows(DuplicateNameException.class, () -> {
			fatRepository.update(fat2);
		});

		final String expectedMessage = "Entity with name '" + fat2.getName() + "' already exists";
		final String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void updateEntity() throws Exception {
		final Fat fat = repoHelper.createFat();
		final String newStringValue = RandomStringUtils.random(5);
		final Integer newIntegerValue = 123;
		final BigDecimal newBigDecimalValue = BigDecimal.valueOf(567);
		fat.setInci(newStringValue);
		fat.setIns(newIntegerValue);
		fat.setSapNaoh(newBigDecimalValue);
		fatRepository.update(fat);

		final Fat loadedFat = fatRepository.get(fat.getId());

		assertThat(loadedFat).insIsEqual(fat).sapNaohIsEqual(fat).insIsEqual(fat);
	}

	@Test
	void updateEntityIncreasesVersion() throws Exception {
		final Fat fat = repoHelper.createFat();
		fat.setName(RandomStringUtils.random(5));

		final Fat updateFat = fatRepository.update(fat);
		assertThat(updateFat).isDeepEqualToExceptVersion(fat);
		assertThat(updateFat).versionIsEqualTo(2);

		final Fat loadedFat = fatRepository.get(fat.getId());
		assertThat(loadedFat).isDeepEqualToExceptVersion(fat);
		assertThat(loadedFat).versionIsEqualTo(2);
	}

	@Test
	void deleteEntity() throws Exception {
		final Fat fat = repoHelper.createFat();
		fatRepository.delete(fat.getId());
		repoHelper.assertThatFatNotExists(fat.getName());
	}

}
