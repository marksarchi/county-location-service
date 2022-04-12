package com.marksarchi.countylocationservice.county;


import com.marksarchi.countylocationservice.Domain.CountyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountyRepository extends JpaRepository<CountyEntity, UUID> {
  Optional<CountyEntity> findCountyEntityByCode(String code);
}
