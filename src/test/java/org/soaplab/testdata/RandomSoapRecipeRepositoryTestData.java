package org.soaplab.testdata;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Entity;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.EntityRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Component
public class RandomSoapRecipeRepositoryTestData extends RandomSoapRecipeTestData {

	private final SoapRecipeRepository soapRecipeRepository;
	private final FatRepository fatRepository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;
	private final NaOHRepository naohRepository;
	private final KOHRepository kohRepository;
	private final AdditiveRepository additiveRepository;
	private final LyeRecipeRepository lyeRecipeRepository;

	private <T extends NamedEntity> T createEntityInRepo(T entity, EntityRepository<T> repository) {
		final Entity persistedEntity = repository.create(entity);
		return (T) persistedEntity;
	}

	@Override
	protected Liquid createLiquid() {
		return createEntityInRepo(super.createLiquid(), liquidRepository);

	}

	@Override
	protected Acid createAcid() {
		return createEntityInRepo(super.createAcid(), acidRepository);
	}

	@Override
	protected Fat createFat() {
		return createEntityInRepo(super.createFat(), fatRepository);
	}

	@Override
	protected Fragrance createFragrance() {
		return createEntityInRepo(super.createFragrance(), fragranceRepository);
	}

	@Override
	public SoapRecipe createSoapRecipe() {
		return createEntityInRepo(super.createSoapRecipe(), soapRecipeRepository);
	}

	@Override
	public NaOH createNaOH() {
		return createEntityInRepo(super.createNaOH(), naohRepository);
	}

	@Override
	public KOH createKOH() {
		return createEntityInRepo(super.createKOH(), kohRepository);
	}

	@Override
	protected Additive createAdditive() {
		return createEntityInRepo(super.createAdditive(), additiveRepository);
	}

	@Override
	protected LyeRecipe createLyeRecipe() {
		return createEntityInRepo(super.createLyeRecipe(), lyeRecipeRepository);
	}
}
