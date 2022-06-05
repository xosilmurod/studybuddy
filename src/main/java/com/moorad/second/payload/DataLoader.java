package com.moorad.second.payload;

import com.moorad.second.entity.Interest;
import com.moorad.second.entity.Role;
import com.moorad.second.entity.StudyGroup;
import com.moorad.second.entity.User;
import com.moorad.second.entity.enums.RoleName;
import com.moorad.second.repository.InterestRepository;
import com.moorad.second.repository.RoleRepository;
import com.moorad.second.repository.StudyGroupRepository;
import com.moorad.second.repository.UserRepository;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    StudyGroupRepository studyGroupRepository;

    @Override
    public void run(String... args) throws ConfigurationException {
//        try {
//            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://localhost"});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        if (initialMode.equalsIgnoreCase("always")){
            List<Role> roles = roleRepository.saveAll(Arrays.asList(
                    new Role(RoleName.ROLE_ADMIN)
            ));
            Set<Role> setRoles = new HashSet<>(roles);

            userRepository.saveAll(Arrays.asList(
                    new User(
                            "mooradabduhalikov@gmail.com",
                            "Xosilmurod",
                            "Abduxoliqov",
                            setRoles,
                            passwordEncoder.encode("123")
                    )
            ));

            interestRepository.saveAll(Arrays.asList(
                    new Interest("Philosophy"),
                    new Interest("Books"),
                    new Interest("Python"),
                    new Interest("Physics"),
                    new Interest("College Application"),
                    new Interest("Biology"),
                    new Interest("English"),
                    new Interest("Java"),
                    new Interest("Medicine"),
                    new Interest("Artificial Intelligence")
            ));

            User user = userRepository.findByUsername("mooradabduhalikov@gmail.com").get();
            Interest interest = interestRepository.findByName("Philosophy");
            LocalDate localDate=LocalDate.now();
            List<User> members=new ArrayList<>();
            members.add(user);

            List<StudyGroup> studyGroups = studyGroupRepository.saveAll(Arrays.asList(
                    new StudyGroup(user,
                            "Philosophy Basics",
                            "Here we are going to study ohilosophy from scratch from MIT edx course",
                            "Determination, IELTS 7+, Open Mind",
                            "Telegram: t.me//mooradabduhalikov",
                            localDate,
                            members,
                            interest
                    )
            ));

            for (StudyGroup studyGroup : studyGroups) {
                studyGroup.setCount(studyGroup.getMembers().size());
                studyGroupRepository.save(studyGroup);
            }


        }
    }
}