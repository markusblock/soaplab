package org.soaplab.api.rest;

import org.soaplab.domain.NaOH;
import org.soaplab.repository.NaOHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/naoh")
public class NaOHController extends EntityController<NaOH> {

	@Autowired
	public NaOHController(NaOHRepository repository) {
		super(repository);
	}
}
