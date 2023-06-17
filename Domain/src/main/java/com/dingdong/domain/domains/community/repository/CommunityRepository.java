package com.dingdong.domain.domains.community.repository;


import com.dingdong.domain.domains.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    boolean existsCommunityByInvitationCode(String invitationCode);
}
