package projekt.wypozyczalnia.repositories;

import projekt.wypozyczalnia.data.entities.SkillLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillLevelRepository extends JpaRepository<SkillLevel, Integer> {

    Optional<SkillLevel> findByName(String name);
}
