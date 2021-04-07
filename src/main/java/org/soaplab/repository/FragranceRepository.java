package org.soaplab.repository;

import org.soaplab.domain.Fragrance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fragrance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FragranceRepository extends JpaRepository<Fragrance, Long> {}
