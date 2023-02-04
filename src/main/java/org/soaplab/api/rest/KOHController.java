package org.soaplab.api.rest;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/koh")
public class KOHController extends EntityController<KOH> {

	@Autowired
	public KOHController(KOHRepository repository) {
		super(repository);
	}
}
