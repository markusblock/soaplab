package org.soaplab.domain.utils;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;

import lombok.Getter;

@Getter
public class OliveOilSoapRecipeTestData extends OliveOilSoapBasicRecipeTestData {

	private KOH kOH;
	private Acid citricAcid;
	private Liquid appleVinegar;
	private Fat coconutOil;
	private Fragrance lavendelFragrance;
	private Additive mica;
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
		kOH = createKOH();
		citricAcid = createAcidCitric();
		appleVinegar = createAcidAppleVinegar();
		salt = createSalt();
		sugar = createSugar();
		return super.createLyeRecipeBuilder() //
				.naOH(createRecipeEntry(getNaOH(), 80d)) //
				.kOH(createRecipeEntry(kOH, 20d)) //
				.acids(createRecipeEntries( //
						createRecipeEntry(citricAcid, 4d))) //
				.liquids(createRecipeEntries( //
						createRecipeEntry(appleVinegar, 30d), //
						createRecipeEntry(getWater(), 70d))) //
				.additives(createRecipeEntries( //
						createRecipeEntry(salt, 3d), //
						createRecipeEntry(sugar, 6d)));
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		coconutOil = createFatCoconutOil();
		lavendelFragrance = createFragranceLavendel();
		mica = createMica();

		return super.getSoapRecipeBuilder()//
				.fats(createRecipeEntries( //
						createRecipeEntry(getOliveOil(), 80d), //
						createRecipeEntry(coconutOil, 20d))) //
				.fragrances(createRecipeEntries( //
						createRecipeEntry(lavendelFragrance, 100d))) //
				.additives(createRecipeEntries(createRecipeEntry(mica, 1d))) //
				.lyeRecipe(getLyeRecipe());
	}
}
