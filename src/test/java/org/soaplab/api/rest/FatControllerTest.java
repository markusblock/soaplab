package org.soaplab.api.rest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.testdata.RandomIngredientsTestData;

@ExtendWith(MockitoExtension.class)
class FatControllerTest {

	@InjectMocks
	FatController controller;

	@Mock
	FatRepository repository;

	@Test
	void test() {
		Fat fatToBeCreated = RandomIngredientsTestData.getFatBuilder().build();
		controller.create(fatToBeCreated);
		verify(repository, times(1)).create(fatToBeCreated);

	}

}
