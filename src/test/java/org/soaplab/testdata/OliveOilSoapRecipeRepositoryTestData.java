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
public class OliveOilSoapRecipeRepositoryTestData extends OliveOilSoapRecipeTestData {

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

	public OliveOilSoapRecipeRepositoryTestData(SoapRecipeRepository soapRecipeRepository, FatRepository fatRepository,
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
	protected Liquid createAcidAppleVinegar() {
		return createEntityInRepo(super.createAcidAppleVinegar(), liquidRepository);

	}

	@Override
	protected Acid createAcidCitric() {
		return createEntityInRepo(super.createAcidCitric(), acidRepository);
	}

	@Override
	protected Fat createFatCoconutOil() {
		return createEntityInRepo(super.createFatCoconutOil(), fatRepository);
	}

	@Override
	protected Fat createFatOliveOil() {
		return createEntityInRepo(super.createFatOliveOil(), fatRepository);
	}

	@Override
	protected Fragrance createFragranceLavendel() {
		return createEntityInRepo(super.createFragranceLavendel(), fragranceRepository);
	}

	@Override
	protected Liquid createLiquidWater() {
		return createEntityInRepo(super.createLiquidWater(), liquidRepository);
	}

	@Override
	public SoapRecipe createSoapRecipe() {
		return createEntityInRepo(super.createSoapRecipe(), soapRecipeRepository);
	}
}
