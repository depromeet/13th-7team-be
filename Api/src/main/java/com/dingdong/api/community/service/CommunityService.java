package com.dingdong.api.community.service;


import com.dingdong.api.community.controller.request.CreateCommunityRequest;
import com.dingdong.api.community.controller.request.UpdateCommunityRequest;
import com.dingdong.api.community.controller.response.CommunityCodeResponse;
import com.dingdong.api.community.dto.CommunityDetailsDto;
import com.dingdong.api.community.dto.CommunityListDto;
import com.dingdong.api.global.helper.UserHelper;
import com.dingdong.domain.domains.community.adaptor.CommunityAdaptor;
import com.dingdong.domain.domains.community.domain.Community;
import com.dingdong.domain.domains.community.domain.CommunityImage;
import com.dingdong.domain.domains.community.validator.CommunityValidator;
import com.dingdong.domain.domains.user.domain.User;
import com.dingdong.domain.domains.user.domain.adaptor.UserAdaptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityAdaptor communityAdaptor;
    private final CommunityValidator communityValidator;
    private final UserAdaptor userAdaptor;
    private final UserHelper userHelper;
    private final int MAX_RETRY = 10;

    public CommunityDetailsDto getCommunityDetails(Long communityId) {
        Community community = communityAdaptor.findById(communityId);

        return CommunityDetailsDto.of(community, community.getIdCards().size());
    }

    public List<CommunityListDto> getUserCommunityList(Long userId) {
        User user = userAdaptor.findById(userId);

        return user.getCommunities().stream()
                .map(community -> CommunityListDto.from(community, community.getIdCards().size()))
                .toList();
    }

    // 행성 만들기
    @Transactional
    public CommunityCodeResponse createCommunity(CreateCommunityRequest request) {
        return CommunityCodeResponse.from(
                communityAdaptor.save(
                        createCommunityEntity(request.getName(), request.getLogoImageUrl()),
                        userHelper.getCurrentUser()));
    }

    // 행성 꾸미기
    @Transactional
    public Long updateCommunity(Long communityId, UpdateCommunityRequest request) {
        Community community = findAndValidateCommunity(communityId);
        updateCommunityEntity(
                community,
                request.getName(),
                request.getLogoImageUrl(),
                request.getCoverImageUrl(),
                request.getDescription());
        return community.getId();
    }

    public Community findAndValidateCommunity(Long communityId) {
        User currentUser = userHelper.getCurrentUser();
        // user 가 admin 인지 체크
        communityValidator.verifyAdminUser(communityId, currentUser.getId());
        return communityAdaptor.findById(communityId);
    }

    private Community createCommunityEntity(String name, String logoImageUrl) {
        return Community.createCommunity(name, logoImageUrl, createCommunityInvitationCode());
    }

    // 초대 코드 랜덤 생성
    private String createCommunityInvitationCode() {
        String code = "";
        for (int i = 0; i < MAX_RETRY; i++) {
            code = generateRandomAlphanumericCode();
            if (!communityAdaptor.isAlreadyExistInvitationCode(code)) {
                break;
            }
        }
        return code;
    }

    private String generateRandomAlphanumericCode() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    private void updateCommunityEntity(
            Community community,
            String name,
            String logoImageUrl,
            String coverImageUrl,
            String description) {
        CommunityImage communityImage =
                CommunityImage.createCommunityImage(logoImageUrl, coverImageUrl);
        community.updateCommunity(name, communityImage, description);
    }
}
