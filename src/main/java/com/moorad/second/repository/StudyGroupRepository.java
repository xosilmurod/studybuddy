package com.moorad.second.repository;

import com.moorad.second.entity.Interest;
import com.moorad.second.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, UUID> {
    @Query(value = "select cast(s.study_group_id as varchar(50)) from study_group_members s where members_id=:id", nativeQuery = true)
    List<UUID> findStudyGroupByUser(UUID id);

    List<StudyGroup> findAllByInterestId(UUID interestId);

    Page<StudyGroup> findAllByInterestInAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRequirementContainingIgnoreCase(List<Interest> interests, String name, String description, String requirement, Pageable pageable);
//    Page<StudyGroup> findAllByInterestInAndNameContainingIgnoreCase(List<Interest> interests, String name, Pageable pageable);

}
