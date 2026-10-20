package projekt.wypozyczalnia.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import projekt.wypozyczalnia.data.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
