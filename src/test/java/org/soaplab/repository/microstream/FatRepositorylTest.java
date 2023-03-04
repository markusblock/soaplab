package org.soaplab.repository.microstream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.soaplab.assertions.FatAssert.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fat.FatBuilder;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.repository.FatRepository;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.ui.fat.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FatRepositorylTest {
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
	void createEntityWithDuplicateNameIsNotAllowed() throws Exception {
		final FatBuilder<?, ?> fatBuilder = RandomIngredientsTestData.getFatBuilder();
		fatRepository.create(fatBuilder.build());

		final Exception exception = assertThrows(DuplicateNameException.class, () -> {
			fatRepository.create(fatBuilder.build());
		});

		final String expectedMessage = "Entity with name '" + fatBuilder.build().getName() + "' already exists";
		final String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void updateEntityWithDuplicateNameIsNotAllowed() throws Exception {
		final Fat fat1 = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
		final Fat fat2 = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
		final Fat updatedFat2 = fat2.toBuilder().name(fat1.getName()).build();

		final Exception exception = assertThrows(DuplicateNameException.class, () -> {
			fatRepository.update(updatedFat2);
		});

		final String expectedMessage = "Entity with name '" + updatedFat2.getName() + "' already exists";
		final String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void updateEntity() throws Exception {
		final Fat fat = repoHelper.createFat();
		final String newStringValue = "test";
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
	void deleteEntity() throws Exception {
		final Fat fat = repoHelper.createFat();
		fatRepository.delete(fat.getId());
		repoHelper.assertThatFatNotExists(fat.getName());
	}

}
