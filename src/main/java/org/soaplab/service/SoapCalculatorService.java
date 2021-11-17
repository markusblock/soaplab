package org.soaplab.service;

import org.soaplab.domain.Acid;
import org.soaplab.domain.CalculatedSoapReceiptResult;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.ReceiptEntry;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
import org.springframework.stereotype.Component;

@Component
public class SoapCalculatorService {

	public SoapCalculatorService() {
		// TODO Auto-generated constructor stub
	}

	public CalculatedSoapReceiptResult calculate(SoapReceipt soapReceipt) {

		Weight totalWeight = Weight.of(0, WeightUnit.GRAMS);

		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		for (ReceiptEntry<Fat> fatentry : soapReceipt.getFats().values()) {
			Percentage fatPercentage = fatentry.getPercentage();
			Fat fat = fatentry.getIngredient();
			Double sapNaoh = fat.getSapNaoh();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Weight fatWeight = fatsTotal.calculatePercentage(fatPercentage);
			Weight naoh100 = fatWeight.multiply(sapNaoh);
			Percentage superFat = soapReceipt.getSuperFat();
			Weight naohReduction = naoh100.calculatePercentage(superFat);
			Weight naohPerFat = naoh100.subtract(naohReduction);

			naohForFats = naohForFats.plus(naohPerFat);
			fatentry.setWeight(fatWeight);
			totalWeight = totalWeight.plus(fatWeight);
		}

		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		for (ReceiptEntry<Acid> acidEntry : soapReceipt.getAcids().values()) {
			Percentage acidPercentage = acidEntry.getPercentage();
			Acid acid = acidEntry.getIngredient();
			Double sapNaoh = acid.getSapNaoh();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Weight acidWeight = fatsTotal.calculatePercentage(acidPercentage);
			Weight naoh100 = acidWeight.multiply(sapNaoh);

			naohForAcids = naohForAcids.plus(naoh100);
			acidEntry.setWeight(acidWeight);
			totalWeight = totalWeight.plus(acidWeight);
		}

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		Weight liquidTotal = Weight.of(0, WeightUnit.GRAMS);
		for (ReceiptEntry<Liquid> liquidEntry : soapReceipt.getLiquids().values()) {
			Percentage liquidPercentage = liquidEntry.getPercentage();
			Liquid liquid = liquidEntry.getIngredient();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Percentage liquidToFatRatio = soapReceipt.getLiquidToFatRatio();
			Weight liquidWeight = fatsTotal.calculatePercentage(liquidToFatRatio).calculatePercentage(liquidPercentage);
			liquidEntry.setWeight(liquidWeight);
			liquidTotal = liquidTotal.plus(liquidWeight);
			totalWeight = totalWeight.plus(liquidWeight);

			Double sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				Weight naoh100 = liquidWeight.multiply(sapNaoh);
				naohForLiquids = naohForLiquids.plus(naoh100);
			}
		}

		Percentage naohPercentage = soapReceipt.getNaOHToKOHRatio();
		Weight naohForFatsAndAcidsAndLiquids = naohForFats.plus(naohForAcids).plus(naohForLiquids);
		Weight naohTotal = naohForFatsAndAcidsAndLiquids.calculatePercentage(naohPercentage);
		Percentage kohPercentage = Percentage.of(100).minus(soapReceipt.getNaOHToKOHRatio());

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (kohPercentage.getNumber() > 0) {
			Percentage kohPurity = soapReceipt.getKOHPurity();
			Double naohToKohConversion = Double.valueOf(1.40272);
			kohTotal = naohForFatsAndAcidsAndLiquids.multiply(naohToKohConversion)
					.multiply(kohPercentage.getNumber() / kohPurity.getNumber());
		}

		return CalculatedSoapReceiptResult.builder().naohTotal(naohTotal).kohTotal(kohTotal).liquidTotal(liquidTotal)
				.build();
	}

}
