package org.soaplab.api.rest;

import org.soaplab.domain.Additive;
import org.soaplab.repository.AdditiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/additives")
public class AdditiveController extends EntityController<Additive> {

	@Autowired
	public AdditiveController(AdditiveRepository repository) {
		super(repository);
	}
}
