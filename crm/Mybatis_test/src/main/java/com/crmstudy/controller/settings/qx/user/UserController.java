package com.crmstudy.controller.settings.qx.user;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.domain.User;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("/settings/qx/user/toLogin")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    /**
     * 用户登录
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/settings/qx/user/toIndex")
    @ResponseBody
    public Object toIndex(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String,Object> map = new HashMap();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        //当前登录的用户
        User user = service.SelectUserByActNameAndActPwd(map);

        returnObject returnObj = new returnObject();

        String nowTime = DateUtils.formatDateTime(new Date());
        if(user == null) {
            returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObj.setMessage("用户名或密码错误");
        }else{
            if(nowTime.compareTo(user.getExpire_time()) > 0){
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObj.setMessage("账号已过期");
            }else if("0".equals(user.getLock_state())){
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObj.setMessage("账号已锁定");
            }else if(!user.getAllow_ips().contains(request.getRemoteAddr())){
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObj.setMessage("账号ip异常");
            }else{
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);

                //写入session
                session.setAttribute(Constant.SESSION_USER, user);

                //需要记住密码，往浏览器写如cookie
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", loginAct);
                    c1.setMaxAge(60*60*24*10);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(60*60*24*10);
                    response.addCookie(c2);
                }else{
                    Cookie c1 = new Cookie("loginAct", "0");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "0");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObj;
    }

    /**
     * 安全退出功能
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/settings/qx/user/logout")
    public String logout(HttpServletResponse response, HttpSession session){
        //清空cookie和session
        Cookie c1 = new Cookie("loginAct", "0");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "0");
        c2.setMaxAge(0);
        response.addCookie(c2);
        session.invalidate();
        return "redirect:/";
    }

}
