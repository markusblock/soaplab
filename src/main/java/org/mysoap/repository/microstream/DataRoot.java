package org.mysoap.repository.microstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mysoap.domain.Acid;
import org.mysoap.domain.Fat;
import org.mysoap.domain.Fragrance;
import org.mysoap.domain.SoapReceipt;

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
