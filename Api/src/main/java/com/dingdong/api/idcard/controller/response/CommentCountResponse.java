package com.dingdong.api.idcard.controller.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentCountResponse {
    @Schema(description = "댓글 갯수")
    private int count;
}
