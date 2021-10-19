package org.soaplab.repository.microstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.SoapReceipt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataRoot {

	private final Map<Long, Fat> allFats = new HashMap<>();
	private final Map<Long, SoapReceipt> allSoapReceipts = new HashMap<>();

	private final List<Acid> allAcids = new ArrayList<>();
	private final List<Fragrance> allFragrances = new ArrayList<>();

}
