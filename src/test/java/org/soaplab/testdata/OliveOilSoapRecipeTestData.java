package org.soaplab.testdata;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.utils.IngredientsExampleData;

import lombok.Getter;

@Getter
public class OliveOilSoapRecipeTestData extends OliveOilSoapBasicRecipeTestData {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Olive Soap";
	private KOH kOH;
	private Acid citricAcid;
	private Liquid appleVinegar;
	private Liquid water;
	private Fat coconutOil;
	private Fragrance lavendelFragrance;
	private Additive mica;
	private LyeRecipe lyeRecipe;
	private Additive salt;
	private Additive sugar;

	public OliveOilSoapRecipeTestData() {
		super();
	}

	protected Liquid createAcidAppleVinegar() {
		return IngredientsExampleData.getAppleVinegarBuilder().build();
	}

	protected Acid createAcidCitric() {
		return IngredientsExampleData.getCitricAcidBuilder().build();
	}

	protected Fat createFatCoconutOil() {
		return IngredientsExampleData.getCoconutOilBuilder().build();
	}

	protected Fragrance createFragranceLavendel() {
		return IngredientsExampleData.getLavendelFragranceBuilder().build();
	}

	protected KOH createKOH() {
		return IngredientsExampleData.getKOHBuilder().build();
	}

	protected Additive createMica() {
		return IngredientsExampleData.getMicaBuilder().build();
	}

	protected Additive createSalt() {
		return IngredientsExampleData.getSaltBuilder().build();
	}

	protected Additive createSugar() {
		return IngredientsExampleData.getSugarBuilder().build();
	}

	protected LyeRecipeBuilder<?, ?> createLyeRecipeBuilder() {
		LyeRecipeBuilder<?, ?> lyeRecipeBuilder = super.createLyeRecipeBuilder();
		lyeRecipeBuilder.naOH(createRecipeEntry(getNaOH(), 80d)) //
				.naOH(null)
				.kOH(createRecipeEntry(kOH, 20d)) //
				.acids(createRecipeEntries( //
						createRecipeEntry(citricAcid, 4d))) //
				.liquids(createRecipeEntries( //
						createRecipeEntry(appleVinegar, 30d), //
						createRecipeEntry(water, 70d))) //
				.additives(createRecipeEntries( //
						createRecipeEntry(salt, 3d), //
						createRecipeEntry(sugar, 6d))) //
				.build();
		return lyeRecipeBuilder;
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		kOH = createKOH();
		coconutOil = createFatCoconutOil();
		lavendelFragrance = createFragranceLavendel();
		citricAcid = createAcidCitric();
		appleVinegar = createAcidAppleVinegar();
		water = createLiquidWater();
		mica = createMica();
		lyeRecipe = createLyeRecipe();
		salt = createSalt();
		sugar = createSugar();

		return super.getSoapRecipeBuilder()//
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.fats(createRecipeEntries( //
						createRecipeEntry(getOliveOil(), 80d), //
						createRecipeEntry(coconutOil, 20d))) //
				.fragrances(createRecipeEntries( //
						createRecipeEntry(lavendelFragrance, 100d))) //
				.additives(createRecipeEntries(createRecipeEntry(mica, 1d))) //
				.lyeRecipe(lyeRecipe);
	}
}
