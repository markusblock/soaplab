package org.soaplab.api.rest;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/liquids")
public class LiquidController extends EntityController<Liquid> {

	@Autowired
	public LiquidController(LiquidRepository repository) {
		super(repository);
	}
}
