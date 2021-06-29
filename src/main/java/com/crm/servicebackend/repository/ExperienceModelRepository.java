package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.ExperienceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceModelRepository extends JpaRepository<ExperienceModel, Long> {
    Page<ExperienceModel> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    ExperienceModel findByIdAndServiceCenterId(Long experienceId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long experienceId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long experienceId, Long serviceCenterId);
    Boolean existsByCoefficientAndServiceCenterId(int coefficient, Long serviceCenterId);
    Boolean existsByCoefficientAndIdNotLikeAndServiceCenterId(int coefficient, Long experienceId, Long serviceCenterId);
    @Query("select e from ExperienceModel e where e.serviceCenter.id =:serviceCenterId AND (e.name like %:title% or concat(e.id, '') like %:title% or concat(e.coefficient, '') like %:title%)")
    Page<ExperienceModel> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
}
