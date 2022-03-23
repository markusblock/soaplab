package org.soaplab.testdata;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Acid.AcidBuilder;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fat.FatBuilder;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Fragrance.FragranceBuilder;
import org.soaplab.domain.FragranceType;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Liquid.LiquidBuilder;

import lombok.Getter;

@Getter
public class IngredientsRandomTestData {

	private final static int STRING_LENGTH = 5;

	public IngredientsRandomTestData() {
	}

	public static FatBuilder<?, ?> getFatBuilder() {
		return Fat.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomDouble());
	}

	public static AcidBuilder<?, ?> getAcidBuilder() {
		return Acid.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomDouble());
	}

	public static LiquidBuilder<?, ?> getLiquidBuilder() {
		return Liquid.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomDouble());
	}

	public static FragranceBuilder<?, ?> getFragranceBuilder() {
		return Fragrance.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.type(FragranceType.VOLATILE_OIL);
	}

	private static UUID getRandomUUID() {
		return UUID.randomUUID();
	}

	private static String getRandomString() {
		return RandomStringUtils.randomAlphabetic(STRING_LENGTH);
	}

	private static double getRandomDouble() {
		return RandomUtils.nextDouble();
	}

}
