package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getByIdAndServiceCenterId(Long userId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long userId, Long serviceCenterId);
    Page<User> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    @Query("select u from User u where u.serviceCenter.id =:serviceCenterId AND (u.name like %:title% OR u.surname like %:title%  or concat(u.id, '') like %:title% or u.phoneNumber like %:title% or u.username like %:title% or u.experienceModel.name like %:title%)")
    Page<User> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndIdNotLike(String username, Long userId);}
