package com.moorad.second.controller;

import com.moorad.second.entity.Interest;
import com.moorad.second.entity.Role;
import com.moorad.second.entity.StudyGroup;
import com.moorad.second.entity.User;
import com.moorad.second.entity.enums.RoleName;
import com.moorad.second.payload.request.ReqLogin;
import com.moorad.second.payload.request.ReqRegister;
import com.moorad.second.payload.request.ReqStudyGroup;
import com.moorad.second.payload.request.ReqUserEdit;
import com.moorad.second.repository.InterestRepository;
import com.moorad.second.repository.RoleRepository;
import com.moorad.second.repository.StudyGroupRepository;
import com.moorad.second.repository.UserRepository;
import com.moorad.second.security.jwt.JwtTokenProvider;
import com.moorad.second.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    StudyGroupRepository studyGroupRepository;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody ReqRegister reqRegister) {
        try {
            Set<Role> roles = new HashSet<>();
            Optional<Role> optionalRole = roleRepository.findByRoleName(RoleName.ROLE_USER);

            if (optionalRole.isPresent()) {
                roles.add(optionalRole.get());
            } else {
                List<Role> savedRoles = roleRepository.saveAll(Arrays.asList(
                        new Role(RoleName.ROLE_USER)
                ));
                roles = new HashSet<>(savedRoles);
            }

            User user = new User(
                    reqRegister.getEmail(),
                    reqRegister.getFirstName(),
                    reqRegister.getLastName(),
                    roles,
                    passwordEncoder.encode(reqRegister.getPassword())
            );

            userRepository.save(user);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody ReqLogin reqLogin) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(reqLogin.getEmail(), reqLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String generateToken = jwtTokenProvider.generateJwtToken(authenticate);
        return ResponseEntity.ok("StudyBuddy "+generateToken);
    }

    @PostMapping("/add-interest")
    public HttpEntity<?> addInterest(@RequestBody List<UUID> ids){
        try {
            User loggedUser = userDetailsService.getLoggedUser();
            Iterable<UUID> iterable = ids;
            List<Interest> interests = interestRepository.findAllById(iterable);
            loggedUser.setInterests(interests);
            User save = userRepository.save(loggedUser);
            return ResponseEntity.ok(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/create-study-group")
    public HttpEntity<?> createStudyGroup(@RequestBody ReqStudyGroup reqStudyGroup){
        try {
            User loggedUser = userDetailsService.getLoggedUser();
            Interest interest = interestRepository.findById(reqStudyGroup.getInterestId()).get();
            LocalDate localDate=LocalDate.now();
            List<User> members=new ArrayList<>();
            members.add(loggedUser);
            StudyGroup save = studyGroupRepository.save(new StudyGroup(
                    loggedUser,
                    reqStudyGroup.getName(),
                    reqStudyGroup.getDescription(),
                    reqStudyGroup.getRequirement(),
                    reqStudyGroup.getContacts(),
                    localDate,
                    members,
                    interest
            ));
            save.setCount(save.getMembers().size());
            studyGroupRepository.save(save);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/join-study-group/{group-id}")
    public HttpEntity<?> joinStudyGroup(@PathVariable("group-id") UUID groupId){
        try {
            User loggedUser = userDetailsService.getLoggedUser();
            StudyGroup studyGroup = studyGroupRepository.findById(groupId).get();
            List<User> members = studyGroup.getMembers();
            if(members.contains(loggedUser)){
               return ResponseEntity.ok(false);
            }
            members.add(loggedUser);
            studyGroup.setMembers(members);
            StudyGroup save = studyGroupRepository.save(studyGroup);

            save.setCount(save.getMembers().size());
            studyGroupRepository.save(save);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/get-joined-study-groups")
    public HttpEntity<?> getJoinedStudyGroups(){
        User loggedUser = userDetailsService.getLoggedUser();
        List<UUID> studyGroupIds = studyGroupRepository.findStudyGroupByUser(loggedUser.getId());
        Iterable iterable=studyGroupIds;
        List<StudyGroup> studyGroups = studyGroupRepository.findAllById(iterable);

        return ResponseEntity.ok(studyGroups);
    }

    @GetMapping("/get-info")
    public HttpEntity<?> getUserInfo(){
        User loggedUser = userDetailsService.getLoggedUser();
        return ResponseEntity.ok(loggedUser);
    }

    @PutMapping("/edit")
    public HttpEntity<?> editUser(@RequestBody ReqUserEdit reqUserEdit) {
        User loggedUser = userDetailsService.getLoggedUser();
        boolean matches = passwordEncoder.matches(reqUserEdit.getPreviousPassword(), loggedUser.getPassword());
        if (matches){
            loggedUser.setUsername(reqUserEdit.getEmail());
            loggedUser.setFirstName(reqUserEdit.getFirstName());
            loggedUser.setLastName(reqUserEdit.getLastName());
            loggedUser.setPassword(passwordEncoder.encode(reqUserEdit.getNewPassword()));

            userRepository.save(loggedUser);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

}
