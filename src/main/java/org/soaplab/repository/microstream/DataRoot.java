package org.soaplab.repository.microstream;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataRoot {

	private final Map<UUID, Fat> allFats = new HashMap<>();
	private final Map<UUID, SoapRecipe> allSoapReceipts = new HashMap<>();
	private final Map<UUID, Acid> allAcids = new HashMap<>();
	private final Map<UUID, Fragrance> allFragrances = new HashMap<>();
	private final Map<UUID, Liquid> allLiquids = new HashMap<>();
}
