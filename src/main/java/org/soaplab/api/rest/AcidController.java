package org.soaplab.api.rest;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/acids")
public class AcidController extends EntityController<Acid> {

	@Autowired
	public AcidController(AcidRepository repository) {
		super(repository);
	}
}
