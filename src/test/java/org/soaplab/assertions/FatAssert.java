package org.soaplab.assertions;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Ingredient;

public class FatAssert extends IngredientAssert {

	public FatAssert(Fat actual) {
		super(actual, FatAssert.class);
	}

	public static FatAssert assertThat(Fat actual) {
		return new FatAssert(actual);
	}

	@Override
	public FatAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		Fat expectedFat = (Fat) expected;
		Assertions.assertThat(getActual().getSapNaoh()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expectedFat.getSapNaoh());
		Assertions.assertThat(getActual().getSapKoh()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expectedFat.getSapKoh());
		Assertions.assertThat(getActual().getLauric()).isEqualTo(expectedFat.getLauric());
		Assertions.assertThat(getActual().getMyristic()).isEqualTo(expectedFat.getMyristic());
		Assertions.assertThat(getActual().getPalmitic()).isEqualTo(expectedFat.getPalmitic());
		Assertions.assertThat(getActual().getStearic()).isEqualTo(expectedFat.getStearic());
		Assertions.assertThat(getActual().getRicinoleic()).isEqualTo(expectedFat.getRicinoleic());
		Assertions.assertThat(getActual().getOleic()).isEqualTo(expectedFat.getOleic());
		Assertions.assertThat(getActual().getLinoleic()).isEqualTo(expectedFat.getLinoleic());
		Assertions.assertThat(getActual().getLinolenic()).isEqualTo(expectedFat.getLinolenic());
		Assertions.assertThat(getActual().getIodine()).isEqualTo(expectedFat.getIodine());
		Assertions.assertThat(getActual().getIns()).isEqualTo(expectedFat.getIns());

		return this;
	}

	private Fat getActual() {
		return (Fat) actual;
	}
}
