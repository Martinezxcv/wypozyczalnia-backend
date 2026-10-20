package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.entities.EquipmentType;
import projekt.wypozyczalnia.repositories.EquipmentTypeRepository;
import projekt.wypozyczalnia.services.EquipmentTypeService;

@Service
@RequiredArgsConstructor
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    @Override
    public EquipmentType getEquipmentType(String name) {
        return equipmentTypeRepository.findByName(name).orElse(null);
    }
}
