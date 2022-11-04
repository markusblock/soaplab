package org.soaplab.api.rest;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/fats")
public class FatController extends EntityController<Fat> {

	@Autowired
	public FatController(FatRepository repository) {
		super(repository);
	}
}
