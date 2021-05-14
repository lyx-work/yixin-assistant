package com.lyx.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginEntity
{
    /*----- 在刚进入页面或点击验证码刷新时赋值 -----*/
    /**
     *  登录时要携带的code
     */
    public String code;

    /*----- 在点击认证按钮时赋值 -----*/
    /**
     * 请求时要携带的token
     */
    public String token;

    /**
     * 登录时要携带的验证码
     */
    public String kaptcha;

    /**
     * 登录的账号名
     */
    public String loginName;
}