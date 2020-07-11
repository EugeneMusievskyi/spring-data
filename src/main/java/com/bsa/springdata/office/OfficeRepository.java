package com.bsa.springdata.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
    @Query("SELECT o FROM Office o " +
            "WHERE :technology IN( " +
            "SELECT tech.name FROM o.users u " +
            "INNER JOIN u.team t " +
            "INNER JOIN t.technology tech)")
    List<Office> findByTechnology(@Param("technology") String technology);

    @Transactional
    @Modifying
    @Query("UPDATE Office o SET o.address = :newAddress WHERE o.address = :oldAddress " +
            "AND exists(SELECT u FROM o.users u WHERE u.team.project IS NOT NULL)")
    void updateAddress(@Param("oldAddress") String oldAddress,
                       @Param("newAddress") String newAddress);

    Optional<Office> findByAddress(String address);
}
