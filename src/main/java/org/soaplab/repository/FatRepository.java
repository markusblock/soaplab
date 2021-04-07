package org.soaplab.repository;

import org.soaplab.domain.Fat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FatRepository extends JpaRepository<Fat, Long> {}
