package org.soaplab.repository.microstream;

import java.util.HashSet;
import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataRoot {

	private final Set<Fat> allFats = new HashSet<>();
	private final Set<SoapRecipe> allSoapReceipts = new HashSet<>();
	private final Set<Acid> allAcids = new HashSet<>();
	private final Set<Fragrance> allFragrances = new HashSet<>();
	private final Set<Liquid> allLiquids = new HashSet<>();
	private final Set<KOH> allKOH = new HashSet<>();
	private final Set<NaOH> allNaOH = new HashSet<>();
}
