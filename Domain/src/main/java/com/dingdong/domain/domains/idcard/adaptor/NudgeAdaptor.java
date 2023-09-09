package com.dingdong.domain.domains.idcard.adaptor;


import com.dingdong.core.annotation.Adaptor;
import com.dingdong.domain.domains.idcard.domain.entity.Nudge;
import com.dingdong.domain.domains.idcard.domain.model.NudgeVo;
import com.dingdong.domain.domains.idcard.repository.NudgeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Adaptor
@RequiredArgsConstructor
public class NudgeAdaptor {

    private final NudgeRepository nudgeRepository;

    public void save(Nudge nudge) {
        nudgeRepository.save(nudge);
    }

    public Optional<Nudge> findNudge(Long communityId, Long fromUserId, Long toUserId) {
        return nudgeRepository.findNudgeByCommunityIdAndFromUserIdAndToUserId(
                communityId, fromUserId, toUserId);
    }

    public Slice<NudgeVo> getNudges(Long toUserId, Pageable pageable) {
        return nudgeRepository.getNudges(toUserId, pageable);
    }
}
