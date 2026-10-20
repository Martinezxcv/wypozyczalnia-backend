package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.entities.SkillLevel;
import projekt.wypozyczalnia.repositories.SkillLevelRepository;
import projekt.wypozyczalnia.services.SkillLevelService;

@Service
@RequiredArgsConstructor
public class SkillLevelServiceImpl implements SkillLevelService {

    private final SkillLevelRepository skillLevelRepository;

    @Override
    public SkillLevel getSkillLevel(String name) {
        return skillLevelRepository.findByName(name).orElse(null);
    }
}
