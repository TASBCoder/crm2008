package com.crmstudy.controller.workbench.activity;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.commons.utills.UUIDUtils;
import com.crmstudy.domain.Activity;
import com.crmstudy.domain.User;
import com.crmstudy.mapper.workbench.activity.ActivityMapper;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/workbench/activity/selectActivityByConditionForPage")
    @ResponseBody
    public Object selectAllActivity(String name, String owner, String start_date, String end_date, Integer pageNo, Integer pageSize, HttpServletRequest request){
        String name1 = request.getParameter("name");
        String owner1 = request.getParameter("owner");
        System.out.println("使用parameter获取到的前端参数name1 = " + name1);
        System.out.println("使用parameter获取到的前端参数owner1 = " + owner1);
        //将前端发送的参数封装进map中
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("start_date", start_date);
        map.put("end_date", end_date);
        map.put("pageNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        System.out.println("map  = "+ map);

        List<Activity> queryActivityList = activityService.selectActivityByConditionForPage(map);
        int conditionTotalRows = activityService.selectActivityByCondition(map);
        //将查询到的结果封装到map中返回到前端
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("queryActivityList", queryActivityList);
        resultMap.put("conditionTotalRows", conditionTotalRows);
        System.out.println("查询返回的结果：" + queryActivityList);
        return resultMap;
    }
}
