package com.crmstudy.controller.workbench.activity;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.commons.utills.UUIDUtils;
import com.crmstudy.domain.Activity;
import com.crmstudy.domain.User;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/toIndex")
    public String index(HttpServletRequest request){
        List<User> users = userService.SelectAllUser();
        request.setAttribute("Users",users);

        List<Activity> activities = activityService.selectAllActivity();
        request.setAttribute("Activities",activities);
        //请求转发到jsp页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity")
    @ResponseBody
    public Object saveCreateActivity(Activity activity,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        System.out.println("session中存储的user的id值 = " + user.getId());

        activity.setId(UUIDUtils.getUUID());
        activity.setCreate_time(DateUtils.formatDateTime(new Date()));
        activity.setCreate_by(user.getId());

        System.out.println(activity);

        returnObject returnObject = new returnObject();

        try{
            int i = activityService.saveCreateActivity(activity);
            if(i>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试.......");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }

//    @RequestMapping("/workbench/activity/selectAllActivity")
//    public Object selectAllActivity(){
//        List<Activity> activities = activityService.selectAllActivity();
//        return activities;
//    }
}
