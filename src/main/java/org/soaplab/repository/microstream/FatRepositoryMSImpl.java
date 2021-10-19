package org.soaplab.repository.microstream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FatRepositoryMSImpl extends EntityRepositoryMSImpl<Fat> implements FatRepository {

	public FatRepositoryMSImpl(MicrostreamRepository repository) {
		super(repository);
	}

	@Override
	protected Map<Long, Fat> getIdToEntityMapping() {
		return repository.getRoot().getAllFats();
	}

	@Override
	public List<Fat> findByInci(String inci) {
		return idToEntity.values().stream().filter(fat -> fat.getInci().equals(inci)).collect(Collectors.toList());
	}

}
