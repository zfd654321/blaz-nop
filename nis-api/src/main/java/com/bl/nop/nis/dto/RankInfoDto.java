package com.bl.nop.nis.dto;

import com.bl.nop.entity.wx.WxRank;

public class RankInfoDto extends WxRank {
    private String nickName;
    private String avatarUrl;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
