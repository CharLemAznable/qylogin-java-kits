package com.github.charlemaznable.qylogin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class CookieValue {

    @JSONField(name = "UserID")
    private String userID;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Avatar")
    private String avatar;

    @JSONField(name = "CsrfToken")
    private String csrfToken;

    @JSONField(name = "Expired")
    private DateTime expired;
}
