package org.soaplab.repository.microstream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.soaplab.assertions.FatAssert.assertThat;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.domain.Fat;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.exception.EntityDeletionFailedException;
import org.soaplab.domain.utils.SoapRecipeUtils;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.soaplab.testdata.RandomSoapRecipeRepositoryTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SoapRecipeRepositoryIT {
	@LocalServerPort
	private int port;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@Autowired
	private FatRepository fatRepository;

	@Autowired
	private SoapRecipeRepository soapRecipeRepository;

	@Test
	/**
	 * When fat is updated this update is visible when retrieving soapRecipe.
	 *
	 * @throws Exception
	 */
	void updatedReferencedEntityIsContainedInGet() throws Exception {
		final RandomSoapRecipeRepositoryTestData testData = repoHelper.createSoapRecipeWithRandomData();
		final SoapRecipe soapRecipe = testData.getSoapRecipe();
		final Fat fat = testData.getFat1();

		fat.setSapNaoh(BigDecimal.valueOf(0.999d));
		fatRepository.update(fat);

		final Fat loadedUpdatedFat = fatRepository.get(fat.getId());
		assertThat(loadedUpdatedFat).isDeepEqualToExceptVersion(fat);

		final SoapRecipe loadedSoapRecipe = soapRecipeRepository.get(soapRecipe.getId());
		assertThat(SoapRecipeUtils.getFat(loadedSoapRecipe, fat.getId()).get()).isDeepEqualToExceptVersion(fat);
	}

	@Test
	/**
	 * When soap is updated including an changed fat this changed fat is not
	 * persisted. An update to fat repository should happen separately.
	 *
	 * @throws Exception
	 */
	void updatingCompositEntityDoesNotUpdateReferencedEntity() throws Exception {
		final RandomSoapRecipeRepositoryTestData testData = repoHelper.createSoapRecipeWithRandomData();
		final SoapRecipe soapRecipe = testData.getSoapRecipe();
		final Fat fat = testData.getFat1();
		final BigDecimal sapNaohOldValue = fat.getSapNaoh();
		fat.setSapNaoh(BigDecimal.valueOf(0.999d));
		soapRecipe.setNotes("TEST");
		soapRecipeRepository.update(soapRecipe);

		final SoapRecipe loadedSoapRecipe = soapRecipeRepository.get(soapRecipe.getId());
		final Fat fatFromLoadedRecipe = SoapRecipeUtils.getFat(loadedSoapRecipe, fat.getId()).get();
		Assertions.assertThat(fatFromLoadedRecipe.getSapNaoh()).isEqualByComparingTo(sapNaohOldValue);
		Assertions.assertThat(loadedSoapRecipe.getNotes()).isEqualTo("TEST");
	}

	@Test
	void addingReferencedEntityToCompositeEntity() throws Exception {
		final RandomSoapRecipeRepositoryTestData testData = repoHelper.createSoapRecipeWithRandomData();
		final SoapRecipe soapRecipe = testData.getSoapRecipe();
		final Fat newFat = repoHelper.createFat();
		final SoapRecipe updatedSoapRecipe = SoapRecipeUtils.addFat(soapRecipe, newFat, 80d);
		soapRecipeRepository.update(updatedSoapRecipe);

		final SoapRecipe loadedSoapRecipe = soapRecipeRepository.get(soapRecipe.getId());
		assertThat(SoapRecipeUtils.getFat(loadedSoapRecipe, newFat.getId()).get()).isDeepEqualTo(newFat);
	}

	@Test
	void removingReferencedEntityFromCompositeEntity() throws Exception {
		final RandomSoapRecipeRepositoryTestData testData = repoHelper.createSoapRecipeWithRandomData();
		final SoapRecipe soapRecipe = testData.getSoapRecipe();
		final Fat fat = testData.getFat1();
		final SoapRecipe updatedSoapRecipe = SoapRecipeUtils.removeFat(soapRecipe, fat);
		soapRecipeRepository.update(updatedSoapRecipe);

		final SoapRecipe loadedSoapRecipe = soapRecipeRepository.get(soapRecipe.getId());
		Assertions.assertThat(SoapRecipeUtils.getFat(loadedSoapRecipe, fat.getId())).isEmpty();
	}

	@Test
	void deletingReferencedEntityIsNotAllowed() throws Exception {
		final RandomSoapRecipeRepositoryTestData testData = repoHelper.createSoapRecipeWithRandomData();
		final SoapRecipe soapRecipe = testData.getSoapRecipe();
		final Fat fat = testData.getFat1();
		final EntityDeletionFailedException exception = assertThrows(EntityDeletionFailedException.class, () -> {
			fatRepository.delete(fat.getId());
		});

		assertTrue(exception.getReason().equals(EntityDeletionFailedException.REASON.ENTITY_STILL_REFERENCED));

		final SoapRecipe soapRecipeUpdated = SoapRecipeUtils.removeFat(soapRecipe, fat);
		soapRecipeRepository.update(soapRecipeUpdated);
		fatRepository.delete(fat.getId());
	}

//	@Test
//	void ensureNoObjectInAcidsAfterRestart() {
//		fail("not implemented");
//		// TODO: REST API test or UI test
//		final SoapRecipe soapRecipe = repoHelper.createSoapRecipeWithRandomData();
//		// get soaprecipe
//		// check acid
//
//		Restarter.getInstance().restart();
//
//		// get soaprecipe
//		// check acid
//	}

}
