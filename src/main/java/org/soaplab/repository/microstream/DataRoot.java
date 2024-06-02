package org.soaplab.repository.microstream;

import java.util.HashSet;
import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
//No constructor injection supported on @Storage Beans.
public class DataRoot {

	private final Set<Fat> allFats = new HashSet<>();
	private final Set<SoapRecipe> allSoapRecipes = new HashSet<>();
	private final Set<Acid> allAcids = new HashSet<>();
	private final Set<Fragrance> allFragrances = new HashSet<>();
	private final Set<Liquid> allLiquids = new HashSet<>();
	private final Set<KOH> allKOH = new HashSet<>();
	private final Set<NaOH> allNaOH = new HashSet<>();
	private final Set<Additive> allAdditives = new HashSet<>();
	private final Set<LyeRecipe> allLyeRecipes = new HashSet<>();
}
