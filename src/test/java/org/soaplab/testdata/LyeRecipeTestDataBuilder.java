package org.soaplab.testdata;

import java.util.UUID;

import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;

import lombok.Getter;

@Getter
public class LyeRecipeTestDataBuilder {

	private final LyeRecipeBuilder<?, ?> lyeRecipeBuilder = LyeRecipe.builder();

	public LyeRecipeTestDataBuilder() {
		lyeRecipeBuilder.id(UUID.randomUUID());
	}

	public LyeRecipeBuilder<?, ?> getLyeRecipeBuilder() {
		return lyeRecipeBuilder;
	}

	public LyeRecipe createLyeRecipe() {
		return getLyeRecipeBuilder().build();
	}
}
