package com.crmstudy.controller.workbench.activity;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.commons.utills.UUIDUtils;
import com.crmstudy.domain.Activity;
import com.crmstudy.domain.User;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    /**
     * 创建市场活动，并记录创建人和创建时间
     * @param activity
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/saveCreateActivity")
    @ResponseBody
    public Object saveCreateActivity(Activity activity,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        System.out.println("session中存储的user的id值 = " + user.getId());

        //保存创建操作需要记录创建时间create_time和创建人create_by
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
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.......");
        }

        return returnObject;
    }

    /**
     * 根据参数查询市场活动
     * @param name
     * @param owner
     * @param start_date
     * @param end_date
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityByConditionForPage")
    @ResponseBody
    public Object selectAllActivity(String name, String owner, String start_date, String end_date, Integer pageNo, Integer pageSize){
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

    /**
     * 根据id删除市场活动
     * @param id  类型：String[] 用于接受前端传过来的id数组
     * @return  returnObj
     */
    @RequestMapping("/workbench/activity/deleteActivity")
    @ResponseBody
    public Object deleteActivity(String[] id){
        System.out.println("deleteActivity开始运作了");
        System.out.println("要删除数据的id数组值：" + id);
//        System.out.println("删除返回的结果： " + i);
        returnObject returnObject = new returnObject();
        try {
            int i = activityService.deleteActivity(id);
            if(i>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试.....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }

    /**
     * 根据id查询市场活动
     * @param id
     * @return Activity
     */
    @RequestMapping("/workbench/activity/selectActivityById")
    @ResponseBody
    public Object selectActivityById(String id){
        Activity activity = activityService.selectActivityById(id);
        return activity;
    }

    /**
     * 修改市场活动，并记录修改人和修改时间
     * @param activity
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/updateActivity")
    @ResponseBody
    public Object updateActivity(Activity activity,HttpServletRequest request){
        returnObject returnObject = new returnObject();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        //更改操作需要记录更改时间edit_time和更改人edit_by
        activity.setEdit_time(DateUtils.formatDateTime(new Date()));
        activity.setEdit_by(user.getId());

        try {
            int i = activityService.updateActivity(activity);
            System.out.println("更新返回的值i： " + i);
            if(i>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试.....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/toDetail")
    public String toDetail(){
        return "workbench/activity/detail";
    }
}
