package org.soaplab.repository.microstream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.fat.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MicrostreamRepositoryIT {
	@LocalServerPort
	private int port;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@Autowired
	private FatRepository fatRepository;

	// TODO remove or fix
//	@Test
//	@DirtiesContext
//	void ensureEntityIsPersisted() throws Exception {
//		final Fat fat = fatRepository.create(RandomIngredientsTestData.getFatBuilder().build());
//
//		Restarter.getInstance().restart();
//
//		final Fat loadedFat = fatRepository.get(fat.getId());
//		assertThat(loadedFat).isDeepEqualTo(fat);
//	}
}
