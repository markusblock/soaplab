package org.soaplab.service;

import org.soaplab.domain.CalculatedSoapReceiptResult;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.ReceiptEntry;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.domain.Unit;
import org.soaplab.domain.Weight;
import org.springframework.stereotype.Component;

@Component
public class SoapCalculatorService {

	public SoapCalculatorService() {
		// TODO Auto-generated constructor stub
	}

	public CalculatedSoapReceiptResult calculate(SoapReceipt soapReceipt) {

		Weight naohFatTotal = Weight.of(0, Unit.GRAMS);
		for (ReceiptEntry<Fat> fatentry : soapReceipt.getFats().values()) {
			Percentage fatPercentage = fatentry.getPercentage();
			Fat fat = fatentry.getIngredient();
			Double sapNaoh = fat.getSapNaoh();
			Weight oilsTotal = soapReceipt.getOilsTotal();
			Percentage superFat = soapReceipt.getSuperFat();
			Weight fatWeight = oilsTotal.calculatePercentage(fatPercentage);
			Weight naoh100 = fatWeight.multiply(sapNaoh);
			Weight naohReduction = naoh100.calculatePercentage(superFat);
			Weight naohPerFat = naoh100.subtract(naohReduction);
			naohFatTotal.plus(naohPerFat);
		}

		return null;
	}

}
