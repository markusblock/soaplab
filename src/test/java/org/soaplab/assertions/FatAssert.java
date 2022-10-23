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

		final Fat expectedFat = (Fat) expected;
		sapNaohIsEqual(expectedFat);
		lauricIsEqual(expectedFat);
		myristicIsEqual(expectedFat);
		palmiticIsEqual(expectedFat);
		stearicIsEqual(expectedFat);
		ricinoleicIsEqual(expectedFat);
		oleicIsEqual(expectedFat);
		linoleicIsEqual(expectedFat);
		linolenicIsEqual(expectedFat);
		iodineIsEqual(expectedFat);
		insIsEqual(expectedFat);

		return this;
	}

	public FatAssert sapNaohIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getSapNaoh()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expectedFat.getSapNaoh());
		return this;
	}

	public FatAssert lauricIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getLauric()).isEqualTo(expectedFat.getLauric());
		return this;
	}

	public FatAssert myristicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getMyristic()).isEqualTo(expectedFat.getMyristic());
		return this;
	}

	public FatAssert palmiticIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getPalmitic()).isEqualTo(expectedFat.getPalmitic());
		return this;
	}

	public FatAssert stearicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getStearic()).isEqualTo(expectedFat.getStearic());
		return this;
	}

	public FatAssert ricinoleicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getRicinoleic()).isEqualTo(expectedFat.getRicinoleic());
		return this;
	}

	public FatAssert oleicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getOleic()).isEqualTo(expectedFat.getOleic());
		return this;
	}

	public FatAssert linoleicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getLinoleic()).isEqualTo(expectedFat.getLinoleic());
		return this;
	}

	public FatAssert linolenicIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getLinolenic()).isEqualTo(expectedFat.getLinolenic());
		return this;
	}

	public FatAssert iodineIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getIodine()).isEqualTo(expectedFat.getIodine());
		return this;
	}

	public FatAssert insIsEqual(Fat expectedFat) {
		Assertions.assertThat(getActual().getIns()).isEqualTo(expectedFat.getIns());
		return this;
	}

	private Fat getActual() {
		return (Fat) actual;
	}
}
