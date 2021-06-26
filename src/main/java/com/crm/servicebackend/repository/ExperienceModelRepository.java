package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.ExperienceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceModelRepository extends JpaRepository<ExperienceModel, Long> {
}
