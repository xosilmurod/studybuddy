package com.moorad.second.controller;

import com.moorad.second.entity.Interest;
import com.moorad.second.entity.StudyGroup;
import com.moorad.second.entity.User;
import com.moorad.second.payload.request.ReqInterestIds;
import com.moorad.second.payload.request.ReqStudyGroup;
import com.moorad.second.repository.InterestRepository;
import com.moorad.second.repository.StudyGroupRepository;
import com.moorad.second.repository.UserRepository;
import com.moorad.second.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/study-group")
public class StudyGroupController {
    @Autowired
    StudyGroupRepository studyGroupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @GetMapping("/get-info/{id}")
    public HttpEntity<?> getGroupInfo(@PathVariable UUID id){
        Optional<StudyGroup> optionalStudyGroup = studyGroupRepository.findById(id);
        if(optionalStudyGroup.isPresent()){
            StudyGroup studyGroup = optionalStudyGroup.get();
            return ResponseEntity.ok(studyGroup);
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/delete_member")
    public HttpEntity<?> deleteMember(@RequestParam UUID groupId, @RequestParam UUID memberId){
        try {
            StudyGroup studyGroup = studyGroupRepository.findById(groupId).get();
            User user = userRepository.findById(memberId).get();
            studyGroup.getMembers().remove(user);
            studyGroupRepository.save(studyGroup);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/edit/{id}")
    public HttpEntity<?> editGroup(@PathVariable UUID id, @RequestBody ReqStudyGroup reqStudyGroup){
        try {
            //is creator check
            User loggedUser = userDetailsService.getLoggedUser();
            StudyGroup studyGroup = studyGroupRepository.findById(id).get();
            Interest interest = interestRepository.findById(reqStudyGroup.getInterestId()).get();

            if(studyGroup.getCreator().getUsername().equals(loggedUser.getUsername())){
                studyGroup.setContacts(reqStudyGroup.getContacts());
                studyGroup.setInterest(interest);
                studyGroup.setDescription(reqStudyGroup.getDescription());
                studyGroup.setName(reqStudyGroup.getName());
                studyGroup.setRequirement(reqStudyGroup.getRequirement());

                studyGroupRepository.save(studyGroup);
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/feed")
    public HttpEntity<?> feed(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search,
            @RequestParam String sort,
            @RequestBody ReqInterestIds reqInterestIds){
        if ("hottest".equals(sort)){sort="count";}else {sort="created";}

        List<Interest> userInterests=new ArrayList<>();
        Iterable<UUID> iterable=reqInterestIds.getData();
        List<Interest> selectedCategories = interestRepository.findAllById(iterable);
        Pageable pageable= PageRequest.of(page-1, size, Sort.by(sort));

        if(selectedCategories.size()>0){
            userInterests=selectedCategories;
        }else {
            User loggedUser = userDetailsService.getLoggedUser();
            userInterests = loggedUser.getInterests();
        }

        Page<StudyGroup> groups = studyGroupRepository.findAllByInterestInAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRequirementContainingIgnoreCase(userInterests, search, search, search, pageable);
        return ResponseEntity.ok(groups);
    }
}
