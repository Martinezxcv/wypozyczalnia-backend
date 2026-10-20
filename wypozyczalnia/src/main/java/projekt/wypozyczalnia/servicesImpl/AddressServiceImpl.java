package projekt.wypozyczalnia.servicesImpl;

import projekt.wypozyczalnia.data.dtos.AddressDTO;
import lombok.RequiredArgsConstructor;
import projekt.wypozyczalnia.data.entities.Address;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.mappers.AddressMapper;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.repositories.AddressRepository;
import projekt.wypozyczalnia.repositories.UserRepository;
import projekt.wypozyczalnia.services.AddressService;
import projekt.wypozyczalnia.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    @Override
    public List<AddressDTO> getClientAddresses() {
        return addressRepository.findByUserUserId(jwtUtils.getCurrentUserId()).stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public void addAddress(AddressDTO addressDTO) {
        Address newAddress = addressMapper.toEntity(addressDTO);
        newAddress.setUser(userRepository.findById(jwtUtils.getCurrentUserId()).get());

        addressRepository.save(newAddress);
    }

    @Override
    public void setDefaultAddress(int addressId) {
        List<Address> addresses = new ArrayList<>(addressRepository.findByUserUserId(jwtUtils.getCurrentUserId()));
        for (Address address : addresses) {
            address.setDefault(address.getAddressId() == addressId);
        }
        addressRepository.saveAll(addresses);
    }
}
