package org.soaplab.api.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.soaplab.domain.Fat;
import org.soaplab.ui.fat.RepositoryTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FatControllerITest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@Autowired
	FatController controller;

	@Test
	void test() throws Exception {

		Fat fat = repoHelper.createFat();

		ResponseEntity<String> response = restTemplate
				.getForEntity(createURLWithPort("/soaplab/rest/fats/" + fat.getId()), String.class);
		System.out.println("RESPONSE\n" + response.getBody());

		ObjectMapper mapper = new ObjectMapper();

		String expected = mapper.writeValueAsString(fat);
		System.out.println("EXPECTED\n" + expected);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
