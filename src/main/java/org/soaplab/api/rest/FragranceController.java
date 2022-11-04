package org.soaplab.api.rest;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/fragrances")
public class FragranceController extends EntityController<Fragrance> {

	@Autowired
	public FragranceController(FragranceRepository repository) {
		super(repository);
	}
}
