package org.soaplab.service.ingredients;

import java.util.HashSet;
import java.util.Set;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LiquidRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Component
@RequiredArgsConstructor
public class IngredientsService {

	private final FatRepository fatRepository;
	private final FragranceRepository fragranceRepository;
	private final AdditiveRepository additiveRepository;
	private final LiquidRepository liquidRepository;
	private final AcidRepository acidRepository;

	public Set<Ingredient> findAll() {
		final Set<Ingredient> ingredients = new HashSet<>();
		ingredients.addAll(fatRepository.findAll());
		ingredients.addAll(fragranceRepository.findAll());
		ingredients.addAll(additiveRepository.findAll());
		ingredients.addAll(liquidRepository.findAll());
		ingredients.addAll(acidRepository.findAll());
		return ingredients;
	}

	public Set<Ingredient> findByNameOrInci(String searchString) {
		final Set<Ingredient> ingredients = new HashSet<>();
		ingredients.addAll(fatRepository.findByNameOrInci(searchString));
		ingredients.addAll(fragranceRepository.findByNameOrInci(searchString));
		ingredients.addAll(additiveRepository.findByNameOrInci(searchString));
		ingredients.addAll(liquidRepository.findByNameOrInci(searchString));
		ingredients.addAll(acidRepository.findByNameOrInci(searchString));
		return ingredients;
	}

}
