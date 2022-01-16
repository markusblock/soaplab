package org.soaplab.testdata;

import java.util.UUID;

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
public class IngredientsTestData {

	private static final String APPLE_VINEGAR_NAME = "Apple Vinegar 5,1%";
	private static final String WATER_NAME = "Water";
	private static final String LAVENDEL_NAME = "Lavendel";
	private static final String CIDRIC_ACID_ANHYDRAT_NAME = "Cidric Acid anhydrat";
	private static final String COCONUT_OIL_NAME = "Coconut Oil";
	private static final String OLIVE_OIL_NAME = "Olive Oil";

	public IngredientsTestData() {
	}

	public static FatBuilder<?, ?> getOliveOilBuilder() {
		return Fat.builder().id(UUID.randomUUID()).name(OLIVE_OIL_NAME).inci("Olea Europaea Fruit Oil").sapNaoh(0.135d);
	}

	public static FatBuilder<?, ?> getCoconutOilBuilder() {
		return Fat.builder().id(UUID.randomUUID()).name(COCONUT_OIL_NAME).inci("Cocos Nucifera Oil").sapNaoh(0.183d);
	}

	public static AcidBuilder<?, ?> getCitricAcidBuilder() {
		return Acid.builder().id(UUID.randomUUID()).name(CIDRIC_ACID_ANHYDRAT_NAME).inci("Citric Acid").sapNaoh(0.571d);
	}

	public static LiquidBuilder<?, ?> getWaterBuilder() {
		return Liquid.builder().id(UUID.randomUUID()).name(WATER_NAME).inci("Aqua");
	}

	public static LiquidBuilder<?, ?> getAppleVinegarBuilder() {
		return Liquid.builder().id(UUID.randomUUID()).name(APPLE_VINEGAR_NAME).inci("").sapNaoh(0.666d * 0.051d);
	}

	public static FragranceBuilder<?, ?> getLavendelFragranceBuilder() {
		return Fragrance.builder().id(UUID.randomUUID()).name(LAVENDEL_NAME).inci("").typ(FragranceType.VOLATILE_OIL);
	}

}
