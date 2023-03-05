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
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SoapRecipeRepositoryMSImpl extends EntityRepositoryMSImpl<SoapRecipe> implements SoapRecipeRepository {

	private static final long serialVersionUID = 1L;

	private final FatRepository fatRepository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;
	private final NaOHRepository naohRepository;
	private final KOHRepository kohRepository;

	@Autowired
	public SoapRecipeRepositoryMSImpl(FatRepository fatRepository,
			AcidRepository acidRepository, LiquidRepository liquidRepository, FragranceRepository fragranceRepository,
			NaOHRepository naohRepository, KOHRepository kohRepository) {
		this.fatRepository = fatRepository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
		this.naohRepository = naohRepository;
		this.kohRepository = kohRepository;
	}

	@Override
	protected void getAndReplaceCompositeEntitiesFromRepository(SoapRecipe entity) {
		// reload composite entities to reflect potential changes on them
		if (entity.getNaOH() != null) {
			entity.getNaOH().setIngredient(naohRepository.get(entity.getNaOH().getIngredient().getId()));
		}
		if (entity.getKOH() != null) {
			entity.getKOH().setIngredient(kohRepository.get(entity.getKOH().getIngredient().getId()));
		}
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
		return getDataRoot().getAllSoapReceipts();
	}
}
