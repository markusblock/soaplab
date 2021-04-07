package org.soaplab.repository;

import java.util.List;
import java.util.Optional;
import org.soaplab.domain.SoapReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoapReceipt entity.
 */
@Repository
public interface SoapReceiptRepository extends JpaRepository<SoapReceipt, Long> {
    @Query(
        value = "select distinct soapReceipt from SoapReceipt soapReceipt left join fetch soapReceipt.fats",
        countQuery = "select count(distinct soapReceipt) from SoapReceipt soapReceipt"
    )
    Page<SoapReceipt> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct soapReceipt from SoapReceipt soapReceipt left join fetch soapReceipt.fats")
    List<SoapReceipt> findAllWithEagerRelationships();

    @Query("select soapReceipt from SoapReceipt soapReceipt left join fetch soapReceipt.fats where soapReceipt.id =:id")
    Optional<SoapReceipt> findOneWithEagerRelationships(@Param("id") Long id);
}
