package org.mysoap.repository;

import java.util.List;

import org.mysoap.domain.Fat;

public interface FatRepository extends EntityRepository<Fat> {

	List<Fat> findByInci(String inci);

}
