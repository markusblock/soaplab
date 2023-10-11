package org.soaplab.testdata;

import org.soaplab.domain.*;
import org.soaplab.repository.*;
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
	private final FragranceRecipeRepository fragranceRecipeRepository;

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

	protected FragranceRecipe createFragranceRecipe() {
		return createEntityInRepo(super.createFragranceRecipe(), fragranceRecipeRepository);
	}
}
