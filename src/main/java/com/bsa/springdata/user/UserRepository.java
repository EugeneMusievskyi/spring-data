package com.bsa.springdata.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByLastNameIgnoreCaseStartingWith(String lastName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.office.city = :city ORDER BY u.lastName")
    List<User> findByCity(@Param("city") String city);

    List<User> findByExperienceGreaterThanEqual(int experience, Sort sort);

    @Query("SELECT u FROM User u WHERE u.office.city = :city AND u.team.room = :room ORDER BY u.lastName")
    List<User> findByRoomAndCity(@Param("city") String city, @Param("room") String room);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.experience < :experience")
    int deleteByExperience(@Param("experience") int experience);
}
