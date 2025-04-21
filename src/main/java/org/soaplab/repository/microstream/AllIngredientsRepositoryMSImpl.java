package org.soaplab.repository.microstream;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.AllIngredientsRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AllIngredientsRepositoryMSImpl extends EntityRepositoryMSImpl<Ingredient>
		implements AllIngredientsRepository {

	private static final long serialVersionUID = 1L;

	private final AcidRepository acidRepository;
	private final AdditiveRepository additiveRepository;
	private final FatRepository fatRepository;
	private final FragranceRepository fragranceRepository;
	private final KOHRepository kohRepository;
	private final LiquidRepository liquidRepository;
	private final NaOHRepository naOHRepository;

	@Override
	public List<Ingredient> findByInci(String inci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getInci().toLowerCase().contains(inci.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Ingredient> findByNameOrInci(String nameOrInci) {
		return getEntitiesInternal().stream()
				.filter(ingredient -> ingredient.getName().toLowerCase().contains(nameOrInci.toLowerCase())
						|| ingredient.getInci().toLowerCase().contains(nameOrInci.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	protected Set<Ingredient> getEntitiesInternal() {
		final Set<Ingredient> ingredients = new HashSet<>();
		ingredients.addAll(getDataRoot().getAllAcids());
		ingredients.addAll(getDataRoot().getAllAdditives());
		ingredients.addAll(getDataRoot().getAllFats());
		ingredients.addAll(getDataRoot().getAllFragrances());
		ingredients.addAll(getDataRoot().getAllKOH());
		ingredients.addAll(getDataRoot().getAllLiquids());
		ingredients.addAll(getDataRoot().getAllNaOH());
		return ingredients;
	}

	@Override
	public Ingredient create(Ingredient entity) {
		throw new NotImplementedException();
	}

	@Override
	public void delete(UUID id) {
		getEntitiesInternal().stream().filter(ingredient -> ingredient.getId().equals(id)).findFirst().ifPresent(t -> {
			if (t instanceof Acid) {
				acidRepository.delete(id);
			} else if (t instanceof Additive) {
				additiveRepository.delete(id);
			} else if (t instanceof Fat) {
				fatRepository.delete(id);
			} else if (t instanceof Fragrance) {
				fragranceRepository.delete(id);
			} else if (t instanceof KOH) {
				kohRepository.delete(id);
			} else if (t instanceof Liquid) {
				liquidRepository.delete(id);
			} else if (t instanceof NaOH) {
				naOHRepository.delete(id);
			} else {
				throw new IllegalArgumentException("Type %s not supported".formatted(t.getClass()));
			}
		});
	}

	@Override
	public Ingredient update(Ingredient entity) {
		if (entity instanceof final Acid acid) {
			return acidRepository.update(acid);
		} else if (entity instanceof final Additive additive) {
			return additiveRepository.update(additive);
		} else if (entity instanceof final Fat fat) {
			return fatRepository.update(fat);
		} else if (entity instanceof final Fragrance fragrance) {
			return fragranceRepository.update(fragrance);
		} else if (entity instanceof final KOH koh) {
			return kohRepository.update(koh);
		} else if (entity instanceof final Liquid liquid) {
			return liquidRepository.update(liquid);
		} else if (entity instanceof final NaOH naOH) {
			return naOHRepository.update(naOH);
		} else {
			throw new IllegalArgumentException("Type %s not supported".formatted(entity.getClass()));
		}
	}
}
