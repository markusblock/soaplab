package org.soaplab.domain.utils;

import java.util.UUID;

import lombok.Getter;
import org.soaplab.domain.*;
import org.soaplab.domain.Acid.AcidBuilder;
import org.soaplab.domain.Additive.AdditiveBuilder;
import org.soaplab.domain.Fat.FatBuilder;
import org.soaplab.domain.Fragrance.FragranceBuilder;
import org.soaplab.domain.KOH.KOHBuilder;
import org.soaplab.domain.Liquid.LiquidBuilder;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.NaOH.NaOHBuilder;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

@Getter
/**
 * Defines example data for some basic entities to play around with. Can also be
 * used in tests.
 *
 */ public class IngredientsExampleData {

    public static final String APPLE_VINEGAR_NAME = "Apple Vinegar 5,1%";
    public static final String WATER_NAME = "Water";
    public static final String LAVENDEL_NAME = "Lavendel";
    public static final String CIDRIC_ACID_ANHYDRAT_NAME = "Cidric Acid anhydrat";
    public static final String COCONUT_OIL_NAME = "Coconut Oil";
    public static final String OLIVE_OIL_NAME = "Olive Oil";
    public static final String NAOH_NAME = "NaOH";
    public static final String KOH_NAME = "KOH";
    public static final String MICA_NAME = "Mica Dark Night";
    public static final String SALT_NAME = "Salt";
    public static final String SUGAR_NAME = "Sugar";
    public static final String SOAP_RECIPE_NAME = "Olive Soap";
    public static final String LYE_RECIPE_NAME = "Lye";
    public static final String FRAGRANCE_RECIPE_NAME = "Lavendel Fragrance Recipe";

    private IngredientsExampleData() {
    }

    public static FatBuilder<?, ?> getOliveOilBuilder() {
        return Fat.builder()
                .id(UUID.randomUUID())
                .name(OLIVE_OIL_NAME)
                .inci("Olea Europaea Fruit Oil")
                .sapNaoh(0.135d)
                .cost(Price.of(0.61d))
                .lauric(0)
                .myristic(0)
                .palmitic(14)
                .stearic(3)
                .ricinoleic(0)
                .oleic(69)
                .linoleic(12)
                .linolenic(1)
                .iodine(85)
                .ins(105);
    }

    public static FatBuilder<?, ?> getCoconutOilBuilder() {
        return Fat.builder()
                .id(UUID.randomUUID())
                .name(COCONUT_OIL_NAME)
                .inci("Cocos Nucifera Oil")
                .sapNaoh(0.183d)
                .cost(Price.of(1.06d))
                .lauric(48)
                .myristic(19)
                .palmitic(9)
                .stearic(3)
                .ricinoleic(0)
                .oleic(8)
                .linoleic(2)
                .linolenic(0)
                .iodine(10)
                .ins(258);
    }

    public static AcidBuilder<?, ?> getCitricAcidBuilder() {
        return Acid.builder()
                .id(UUID.randomUUID())
                .name(CIDRIC_ACID_ANHYDRAT_NAME)
                .inci("Citric Acid")
                .sapNaoh(0.571d)
                .cost(Price.of(1.45d));
    }

    public static LiquidBuilder<?, ?> getWaterBuilder() {
        return Liquid.builder().id(UUID.randomUUID()).name(WATER_NAME).inci("Aqua").cost(Price.of(0.05d));
    }

    public static LiquidBuilder<?, ?> getAppleVinegarBuilder() {
        return Liquid.builder()
                .id(UUID.randomUUID())
                .name(APPLE_VINEGAR_NAME)
                .inci("")
                .sapNaoh(0.666d * 0.051d)
                .cost(Price.of(0.16d));
    }

    public static FragranceBuilder<?, ?> getLavendelFragranceBuilder() {
        return Fragrance.builder()
                .id(UUID.randomUUID())
                .name(LAVENDEL_NAME)
                .inci("")
                .type(FragranceType.VOLATILE_OIL)
                .cost(Price.of(30d));
    }

    public static NaOHBuilder<?, ?> getNaOHBuilder() {
        return NaOH.builder().id(UUID.randomUUID()).name(NAOH_NAME).inci("Sodium Hydroxide").cost(Price.of(0.75d));
    }

    public static KOHBuilder<?, ?> getKOHBuilder() {
        return KOH.builder()
                .id(UUID.randomUUID())
                .name(KOH_NAME)
                .inci("Potassium Hydroxide")
                .kOHPurity(Percentage.of(89.5d))
                .cost(Price.of(0.74d));
    }

    public static AdditiveBuilder<?, ?> getMicaBuilder() {
        return Additive.builder().id(UUID.randomUUID()).name(MICA_NAME).inci("Mica").cost(Price.of(15.8d));
    }

    public static AdditiveBuilder<?, ?> getSaltBuilder() {
        return Additive.builder().id(UUID.randomUUID()).name(SALT_NAME).inci("Sodium chloride").cost(Price.of(0.07d));
    }

    public static AdditiveBuilder<?, ?> getSugarBuilder() {
        return Additive.builder().id(UUID.randomUUID()).name(SUGAR_NAME).inci("Glukose").cost(Price.of(0.1d));
    }

    public static LyeRecipeBuilder<?, ?> getLyeRecipeBuilder() {
        return LyeRecipe.builder().id(UUID.randomUUID());
    }

    public static LyeRecipeBuilder<?, ?> getLyeRecipeBuilderNaOH() {
        return getLyeRecipeBuilder().name("Simple NaOH Lye")
                .naOH(createRecipeEntry(getNaOHBuilder().build(), 100d))
                .liquids(SoapRecipeUtils.createRecipeEntries(createRecipeEntry(getWaterBuilder().build(), 100d)));
    }

    public static LyeRecipeBuilder<?, ?> getLyeRecipeBuilderMixed() {
        return getLyeRecipeBuilder().name("Mixed Lye") //
                .naOH(createRecipeEntry(getNaOHBuilder().build(), 80d)) //
                .kOH(createRecipeEntry(getKOHBuilder().build(), 20d)) //
                .liquids(createRecipeEntries(createRecipeEntry(getWaterBuilder().build(), 100d)));
    }

    public static LyeRecipeBuilder<?, ?> getLyeRecipeBuilderMixedWithAcids() {
        return getLyeRecipeBuilderMixed().name("Mixed Lye with acids") //
                .acids(createRecipeEntries(createRecipeEntry(getCitricAcidBuilder().build(), 4d))) //
                .liquids(createRecipeEntries( //
                        createRecipeEntry(getWaterBuilder().build(), 70d), //
                        createRecipeEntry(getAppleVinegarBuilder().build(), 30d)));
    }

    public static LyeRecipeBuilder<?, ?> getLyRecipeBuilderMixedWithAcidsAndAdditives() {
        return getLyeRecipeBuilderMixedWithAcids().name("Mixed Lye with acids and additives") //
                .additives(createRecipeEntries(createRecipeEntry(getSaltBuilder().build(), 2d), //
                        createRecipeEntry(getSugarBuilder().build(), 3d)));
    }

    public static FragranceRecipe.FragranceRecipeBuilder<?, ?> getFragranceRecipeBuilder() {
        return FragranceRecipe.builder()
                .id(UUID.randomUUID())
                .name(FRAGRANCE_RECIPE_NAME);
    }
}
