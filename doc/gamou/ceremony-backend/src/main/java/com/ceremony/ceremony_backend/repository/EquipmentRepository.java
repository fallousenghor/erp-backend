// repository/EquipmentRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceremony.ceremony_backend.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}