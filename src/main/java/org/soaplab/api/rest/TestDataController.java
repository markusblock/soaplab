package org.soaplab.api.rest;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceType;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.ReceiptEntry;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.domain.Unit;
import org.soaplab.domain.Weight;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.SoapReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/testdata")
@Slf4j
public class TestDataController {

	private FatRepository fatRepository;
	private SoapReceiptRepository soapReceiptRepository;
	private Fat oliveOil;
	private Fat coconutOil;
	private SoapReceipt oliveSoap;
	private Fragrance lavendelFragrance;

	@Autowired
	public TestDataController(FatRepository fatRepository, SoapReceiptRepository soapReceiptRepository) {
		this.soapReceiptRepository = soapReceiptRepository;
		this.fatRepository = fatRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create() {

		log.info("creating test data");
		createFats();
		createFragrances();
		createAcids();
		createSoapReceipts();

	}

	private void createSoapReceipts() {
		ReceiptEntry<Fat> olive = ReceiptEntry.<Fat>builder().percentage(Percentage.of(80)).ingredient(oliveOil)
				.build();
		ReceiptEntry<Fat> coconut = ReceiptEntry.<Fat>builder().percentage(Percentage.of(20)).ingredient(coconutOil)
				.build();
		ReceiptEntry<Fragrance> lavendel = ReceiptEntry.<Fragrance>builder().percentage(Percentage.of(100))
				.ingredient(lavendelFragrance).build();
		oliveSoap = SoapReceipt.builder().name("Olive Soap").manufacturingDate(Date.from(Instant.now()))
				.oilsTotal(Weight.of(100, Unit.GRAMS)).liquidTotal(Percentage.of(33)).superFat(Percentage.of(10))
				.fragranceTotal(Percentage.of(3)).fats(createFatEntriesMap(olive, coconut))
				.fragrances(createFragranceEntriesMap(lavendel)).build();
		soapReceiptRepository.add(oliveSoap);
	}

	private void createAcids() {

	}

	private Map<Long, ReceiptEntry<Acid>> createAcidEntriesMap(ReceiptEntry<Acid>... acidEntries) {
		Map<Long, ReceiptEntry<Acid>> acidEntriesMap = new HashMap<>();
		Set.of(acidEntries).forEach(acidEntry -> acidEntriesMap.put(acidEntry.getIngredient().getId(), acidEntry));
		return acidEntriesMap;
	}

	private Map<Long, ReceiptEntry<Fat>> createFatEntriesMap(ReceiptEntry<Fat>... fatEntries) {
		Map<Long, ReceiptEntry<Fat>> fatEntriesMap = new HashMap<>();
		Set.of(fatEntries).forEach(fatEntry -> fatEntriesMap.put(fatEntry.getIngredient().getId(), fatEntry));
		return fatEntriesMap;
	}

	private Map<Long, ReceiptEntry<Fragrance>> createFragranceEntriesMap(ReceiptEntry<Fragrance>... fragranceEntries) {
		Map<Long, ReceiptEntry<Fragrance>> fragranceEntriesMap = new HashMap<>();
		Set.of(fragranceEntries).forEach(
				fragranceEntry -> fragranceEntriesMap.put(fragranceEntry.getIngredient().getId(), fragranceEntry));
		return fragranceEntriesMap;
	}

	private void createFats() {
		oliveOil = Fat.builder().name("Olive Oil").inci("Olea Europaea Fruit Oil").sapNaoh(0.1345d).build();
		coconutOil = Fat.builder().name("Coconut Oil").inci("Cocos Nucifera Oil").sapNaoh(0.183d).build();

		fatRepository.add(oliveOil, coconutOil);
	}

	private void createFragrances() {
		lavendelFragrance = Fragrance.builder().name("Lavendel").inci("").typ(FragranceType.VOLATILE_OIL).build();
	}
}
