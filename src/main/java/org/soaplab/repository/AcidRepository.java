package org.soaplab.repository;

import org.soaplab.domain.Acid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Acid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcidRepository extends JpaRepository<Acid, Long> {}
