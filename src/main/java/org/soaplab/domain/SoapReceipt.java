
package org.soaplab.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder
public class SoapReceipt extends NamedEntity {
	private Date manufacturingDate;
	/**
	 * NaOH to KOH ratio in Percentage. 80% means 80% NaOH and 20% KOH.
	 */
	private Percentage naOHToKOHRatio;
	/**
	 * The purity of the KOH
	 */
	private Percentage kOHPurity;
	/**
	 * Liquid in regards to the total amount of fats
	 */
	private Percentage liquidToFatRatio;
	/**
	 * Weight of fats in total
	 */
	private Weight fatsTotal;
	/**
	 * Amount of superfat in percentage
	 */
	private Percentage superFat;
	/**
	 * Amount of fragrance in percentage regarding total oil
	 */
	private Percentage fragranceTotal;
	private String notes;
	private Map<UUID, ReceiptEntry<Fat>> fats = new HashMap<>();
	private Map<UUID, ReceiptEntry<Acid>> acids = new HashMap<>();
	private Map<UUID, ReceiptEntry<Fragrance>> fragrances = new HashMap<>();
	private Map<UUID, ReceiptEntry<Liquid>> liquids = new HashMap<>();
}
