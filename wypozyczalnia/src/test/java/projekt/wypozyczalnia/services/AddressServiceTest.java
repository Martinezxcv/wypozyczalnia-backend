package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projekt.wypozyczalnia.data.dtos.AddressDTO;
import projekt.wypozyczalnia.data.entities.Address;
import projekt.wypozyczalnia.data.entities.User;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.mappers.AddressMapper;
import projekt.wypozyczalnia.repositories.AddressRepository;
import projekt.wypozyczalnia.repositories.UserRepository;
import projekt.wypozyczalnia.servicesImpl.AddressServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void shouldGetClientAddresses() {
        // given
        int mockUserId = 1;

        Address address1 = new Address();
        address1.setAddressId(1);
        Address address2 = new Address();
        address2.setAddressId(2);

        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setAddressId(1);
        AddressDTO addressDTO2 = new AddressDTO();
        addressDTO2.setAddressId(2);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(mockUserId);
        when(addressRepository.findByUserUserId(mockUserId)).thenReturn(List.of(address1, address2));
        when(addressMapper.toDto(address1)).thenReturn(addressDTO1);
        when(addressMapper.toDto(address2)).thenReturn(addressDTO2);

        List<AddressDTO> result = addressService.getClientAddresses();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getAddressId());
        assertEquals(2, result.get(1).getAddressId());

        verify(jwtUtils, times(1)).getCurrentUserId();
        verify(addressRepository, times(1)).findByUserUserId(mockUserId);
        verify(addressMapper, times(1)).toDto(address1);
        verify(addressMapper, times(1)).toDto(address2);
    }

    @Test
    void shouldAddAddress() {
        // given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");

        Address mockAddress = new Address();
        mockAddress.setStreet("Test Street");

        User mockUser = new User();
        mockUser.setUserId(1);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(addressMapper.toEntity(addressDTO)).thenReturn(mockAddress);
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        addressService.addAddress(addressDTO);

        // then
        assertEquals(mockUser, mockAddress.getUser());

        verify(jwtUtils, times(1)).getCurrentUserId();
        verify(addressMapper, times(1)).toEntity(addressDTO);
        verify(userRepository, times(1)).findById(1);
        verify(addressRepository, times(1)).save(mockAddress);
    }

    @Test
    void shouldSetDefaultAddress() {
        // given
        int mockUserId = 1;
        int targetAddressId = 2;

        Address address1 = new Address();
        address1.setAddressId(1);
        address1.setDefault(true);

        Address address2 = new Address();
        address2.setAddressId(2);
        address2.setDefault(false);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(mockUserId);
        when(addressRepository.findByUserUserId(mockUserId)).thenReturn(List.of(address1, address2));

        addressService.setDefaultAddress(targetAddressId);

        // then
        assertFalse(address1.isDefault());
        assertTrue(address2.isDefault());

        verify(jwtUtils, times(1)).getCurrentUserId();
        verify(addressRepository, times(1)).findByUserUserId(mockUserId);
        verify(addressRepository, times(1)).saveAll(List.of(address1, address2));
    }
}
