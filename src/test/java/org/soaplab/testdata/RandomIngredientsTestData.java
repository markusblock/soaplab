package org.soaplab.testdata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Acid.AcidBuilder;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Additive.AdditiveBuilder;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fat.FatBuilder;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Fragrance.FragranceBuilder;
import org.soaplab.domain.FragranceType;
import org.soaplab.domain.KOH;
import org.soaplab.domain.KOH.KOHBuilder;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Liquid.LiquidBuilder;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.NaOH.NaOHBuilder;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;

import lombok.Getter;

@Getter
public class RandomIngredientsTestData {

	private final static int STRING_LENGTH = 5;

	public RandomIngredientsTestData() {
	}

	public static LyeRecipeBuilder<?, ?> getLyeRecipeBuilder() {
		return LyeRecipe.builder().id(getRandomUUID()).name(getRandomString()).notes(getRandomString());
	}

	public static AdditiveBuilder<?, ?> getAdditiveBuilder() {
		return Additive.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString());
	}

	public static NaOHBuilder<?, ?> getNaOHBuilder() {
		return NaOH.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.cost(getRandomPrice());
	}

	public static KOHBuilder<?, ?> getKOHBuilder() {
		return KOH.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.kOHPurity(getRandomPercentage()).cost(getRandomPrice());
	}

	public static FatBuilder<?, ?> getFatBuilder() {
		return Fat.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomBigDecimal()).ins(getRandomInteger()).iodine(getRandomInteger())
				.lauric(getRandomInteger()).linoleic(getRandomInteger()).linolenic(getRandomInteger())
				.myristic(getRandomInteger()).oleic(getRandomInteger()).palmitic(getRandomInteger())
				.ricinoleic(getRandomInteger()).stearic(getRandomInteger()).cost(getRandomPrice());
	}

	public static AcidBuilder<?, ?> getAcidBuilder() {
		return Acid.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomBigDecimal()).cost(getRandomPrice());
	}

	public static LiquidBuilder<?, ?> getLiquidBuilder() {
		return Liquid.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.sapNaoh(getRandomBigDecimal()).cost(getRandomPrice());
	}

	public static FragranceBuilder<?, ?> getFragranceBuilder() {
		return Fragrance.builder().id(getRandomUUID()).name(getRandomString()).inci(getRandomString())
				.type(FragranceType.VOLATILE_OIL).cost(getRandomPrice());
	}

	public static UUID getRandomUUID() {
		return UUID.randomUUID();
	}

	public static String getRandomString() {
		return RandomStringUtils.randomAlphabetic(STRING_LENGTH);
	}

	public static BigDecimal getRandomBigDecimal() {
		return new BigDecimal(RandomUtils.nextDouble(1d, 100d)).setScale(2, RoundingMode.CEILING);
	}

	public static int getRandomInteger() {
		return RandomUtils.nextInt(1, 100);
	}

	public static double getRandomDouble() {
		return RandomUtils.nextDouble(0, 10);
	}

	public static Percentage getRandomPercentage() {
		return Percentage.of(RandomUtils.nextDouble(0, 100));
	}

	public static Price getRandomPrice() {
		return Price.of(RandomUtils.nextDouble(0, 10));
	}
}
