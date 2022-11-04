package org.soaplab.api.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.soaplab.assertions.FatAssert;
import org.soaplab.domain.Fat;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.ui.fat.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FatControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@Test
	void getEntity() throws Exception {

		final Fat fat = repoHelper.createFat();

		final ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPortAndId(fat.getId()),
				String.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		final ObjectMapper mapper = new ObjectMapper();
		final String expected = mapper.writeValueAsString(fat);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	void getNotExistentEntity() throws Exception {
		final ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPortAndId(UUID.randomUUID()),
				String.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void createEntity() throws Exception {
		final Fat fat = RandomIngredientsTestData.getFatBuilder().build();

		final ResponseEntity<Fat> response = restTemplate.postForEntity(createURLWithPort(), fat, Fat.class);
		repoHelper.assertThatFatHasSameValuesExceptId(fat);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		FatAssert.assertThat(response.getBody()).isDeepEqualToExceptId(fat);
	}

	@Test
	void createNullEntity() throws Exception {
		final ResponseEntity<Fat> response = restTemplate.postForEntity(createURLWithPort(), null, Fat.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void createDuplicateEntity() throws Exception {
		final Fat fat = RandomIngredientsTestData.getFatBuilder().build();

		final ResponseEntity<Fat> response1 = restTemplate.postForEntity(createURLWithPort(), fat, Fat.class);
		assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		final ResponseEntity<Fat> response2 = restTemplate.postForEntity(createURLWithPort(), fat, Fat.class);
		assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void deleteEntity() throws Exception {
		final Fat fat = repoHelper.createFat();

		final ResponseEntity<Void> response = restTemplate.exchange(createURLWithPortAndId(fat.getId()),
				HttpMethod.DELETE, null, Void.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		repoHelper.assertThatFatNotExists(fat.getName());
	}

	@Test
	void deleteEntityTwice() throws Exception {
		final Fat fat = repoHelper.createFat();

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPortAndId(fat.getId()), HttpMethod.DELETE,
				null, Void.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		repoHelper.assertThatFatNotExists(fat.getId());

		response = restTemplate.exchange(createURLWithPortAndId(fat.getId()), HttpMethod.DELETE, null, Void.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		repoHelper.assertThatFatNotExists(fat.getId());
	}

	@Test
	void deleteNotExistendEntity() throws Exception {
		final UUID notExistentUuid = UUID.randomUUID();
		repoHelper.assertThatFatNotExists(notExistentUuid);
		final ResponseEntity<Void> response = restTemplate.exchange(createURLWithPortAndId(notExistentUuid),
				HttpMethod.DELETE, null, Void.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		repoHelper.assertThatFatNotExists(notExistentUuid);
	}

	@Test
	void updateEntity() throws Exception {
		final Fat fat = repoHelper.createFat();
		final Fat updatedFat = fat.toBuilder().name("newName").build();

		final HttpEntity<Fat> requestEntity = new HttpEntity<Fat>(updatedFat);
		final ResponseEntity<Fat> response = restTemplate.exchange(createURLWithPortAndId(fat.getId()), HttpMethod.PUT,
				requestEntity, Fat.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		FatAssert.assertThat(response.getBody()).isDeepEqualToExceptId(updatedFat);
	}

	private String createURLWithPort() {
		return "http://localhost:" + port + "/soaplab/rest/fats";
	}

	private String createURLWithPortAndId(UUID id) {
		return createURLWithPort() + "/" + id;
	}

}
