package projekt.wypozyczalnia.repositories;

import projekt.wypozyczalnia.data.entities.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Integer> {

    Optional<EquipmentType> findByName(String name);
}
