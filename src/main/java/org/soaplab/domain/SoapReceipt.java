
package org.soaplab.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	 * KOH and NaOH ratio. The NaOH part in Percentage.
	 */
	private Percentage naOH;
	/**
	 * KOH and NaOH ratio. The KOH part in Percentage.
	 */
	private Percentage kOH;
	/**
	 * The purity of the KOH
	 */
	private Percentage kOHPurity;
	/**
	 * The total liquid amount in percentage regarding total oil
	 */
	private Percentage liquidTotal;
	/**
	 * oils total
	 */
	private Weight oilsTotal;
	/**
	 * Amount of superfat in percentage
	 */
	private Percentage superFat;
	/**
	 * Amount of fragrance in percentage regarding total oil
	 */
	private Percentage fragranceTotal;
	private String notes;
	private Map<Long, ReceiptEntry<Fat>> fats = new HashMap<>();
	private Map<Long, ReceiptEntry<Fragrance>> fragrances = new HashMap<>();
	private Map<Long, ReceiptEntry<Acid>> acids = new HashMap<>();

}
