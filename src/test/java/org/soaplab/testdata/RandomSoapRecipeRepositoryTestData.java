package org.soaplab.testdata;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Entity;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.EntityRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class RandomSoapRecipeRepositoryTestData extends RandomSoapRecipeTestData {

	@Autowired
	private final FatRepository fatRepository;

	@Autowired
	private final LiquidRepository liquidRepository;

	@Autowired
	private final AcidRepository acidRepository;

	@Autowired
	private final FragranceRepository fragranceRepository;

	@Autowired
	private final SoapRecipeRepository soapRecipeRepository;

	public RandomSoapRecipeRepositoryTestData(SoapRecipeRepository soapRecipeRepository, FatRepository fatRepository,
			AcidRepository acidRepository, LiquidRepository liquidRepository, FragranceRepository fragranceRepository) {
		super();
		this.soapRecipeRepository = soapRecipeRepository;
		this.fatRepository = fatRepository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
	}

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
}
