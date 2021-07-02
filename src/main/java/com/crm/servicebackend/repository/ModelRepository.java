package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Page<Model> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Model findByIdAndServiceCenterId(Long modelId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long modelId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long modelId, Long serviceCenterId);
    @Query("select m from Model m where m.serviceCenter.id =:serviceCenterId AND (m.name like %:title% or concat(m.id,'') like %:title%)")
    Page<Model> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    List<Model> getAllByServiceCenterId(Long serviceCenterId);
}
