package org.soaplab.api.rest;

import org.soaplab.domain.LyeRecipe;
import org.soaplab.repository.LyeRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/lyerecipes")
public class LyeRecipeController extends EntityController<LyeRecipe> {

	@Autowired
	public LyeRecipeController(LyeRecipeRepository repository) {
		super(repository);
	}
}
