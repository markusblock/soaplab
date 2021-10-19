package org.soaplab.repository;

import java.util.List;

import org.soaplab.domain.Fat;

public interface FatRepository extends EntityRepository<Fat> {

	List<Fat> findByInci(String inci);

}
