package projekt.wypozyczalnia.services;

import projekt.wypozyczalnia.data.dtos.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getClientAddresses();
    void addAddress(AddressDTO addressDTO);
    void setDefaultAddress(int addressId);
}
