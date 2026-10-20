package projekt.wypozyczalnia.repositories;

import projekt.wypozyczalnia.data.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Collection<Address> findByUserUserId(long l);
    Optional<Address> findByUserUserIdAndIsDefaultTrue(long userId);
}
