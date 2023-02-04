package org.soaplab.assertions;

import static org.junit.jupiter.api.Assertions.fail;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;

public class IngredientAssert extends AbstractAssert<IngredientAssert, Ingredient> {

	public IngredientAssert(Ingredient actual, Class<?> assertClass) {
		super(actual, assertClass);
	}

	public static IngredientAssert assertThat(Ingredient actual) {
		if (actual instanceof Fat) {
			return new FatAssert((Fat) actual);
		} else if (actual instanceof Acid) {
			return new AcidAssert((Acid) actual);
		} else if (actual instanceof Fragrance) {
			return new FragranceAssert((Fragrance) actual);
		} else if (actual instanceof Liquid) {
			return new LiquidAssert((Liquid) actual);
		} else if (actual instanceof KOH) {
			return new KOHAssert((KOH) actual);
		} else if (actual instanceof NaOH) {
			return new NaOHAssert((NaOH) actual);
		}

		fail("unsupported type of Ingredient " + actual.getClass().getName());
		return null;
	}

	public IngredientAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		idIsEqual(expected);
		nameIsEqual(expected);
		inciIsEqual(expected);

		return this;
	}

	public IngredientAssert isDeepEqualToExceptId(Ingredient expected) {

		isNotNull();

		// id is not checked to compare entities before and after it is stored. When
		// stored the id is set.
		nameIsEqual(expected);
		inciIsEqual(expected);

		return this;
	}

	public IngredientAssert idIsEqual(Ingredient expected) {
		Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
		return this;
	}

	public IngredientAssert nameIsEqual(Ingredient expected) {
		Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
		return this;
	}

	public IngredientAssert inciIsEqual(Ingredient expected) {
		Assertions.assertThat(actual.getInci()).isEqualTo(expected.getInci());
		return this;
	}
}
