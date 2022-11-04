package org.soaplab.api.rest;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/soaprecipes")
public class SoapRecipeController extends EntityController<SoapRecipe> {

	@Autowired
	public SoapRecipeController(SoapRecipeRepository repository) {
		super(repository);
	}
}
