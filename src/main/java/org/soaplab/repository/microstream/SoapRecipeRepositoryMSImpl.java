package org.soaplab.repository.microstream;

import java.util.Set;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<SoapRecipe> implements SoapRecipeRepository {

	private final FatRepository fatRepository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;

	public SoapRecipeRepositoryMSImpl(MicrostreamRepository repository, FatRepository fatRepository,
			AcidRepository acidRepository, LiquidRepository liquidRepository, FragranceRepository fragranceRepository) {
		super(repository);
		this.fatRepository = fatRepository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
	}

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(SoapRecipe entity) {
		// reload composite entities to reflect potential changes on them
		entity.getFats().forEach(entry -> {
			final Fat fat = fatRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fat);
		});
		entity.getAcids().forEach(entry -> {
			final Acid acid = acidRepository.get(entry.getIngredient().getId());
			entry.setIngredient(acid);
		});
		entity.getLiquids().forEach(entry -> {
			final Liquid liquid = liquidRepository.get(entry.getIngredient().getId());
			entry.setIngredient(liquid);
		});
		entity.getFragrances().forEach(entry -> {
			final Fragrance fragrance = fragranceRepository.get(entry.getIngredient().getId());
			entry.setIngredient(fragrance);
		});
	}

	@Override
	protected Set<SoapRecipe> getEntitiesInternal() {
		return repository.getRoot().getAllSoapReceipts();
	}

	@Override
	protected void storeCompositeEntitiesInRepository(SoapRecipe soapRecipe) {
		repository.getStorage().storeAll(soapRecipe.getAcids());
		repository.getStorage().storeAll(soapRecipe.getFats());
		repository.getStorage().storeAll(soapRecipe.getFragrances());
		repository.getStorage().storeAll(soapRecipe.getLiquids());
	}
}
