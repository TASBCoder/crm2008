package com.crmstudy.controller.workbench.activity;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.commons.utills.ExportUtils;
import com.crmstudy.commons.utills.UUIDUtils;
import com.crmstudy.domain.Activity;
import com.crmstudy.domain.User;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    /**
     * 查看数据详情
     * @return
     */
    @RequestMapping("/workbench/activity/toDetail")
    public String toDetail(String[] id, HttpServletRequest request){
        System.out.println("toDetail接受的参数 : " + id[0]);
        List<Activity> list = activityService.findAllActivityById(id);
        request.setAttribute("ActivityDetail",list);
        return "workbench/activity/detail";
    }

    /**
     * 批量导出市场活动
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/exportFindAllActivity")
    public void exportFindAllActivity(HttpServletResponse response){
        List<Activity> list = activityService.findAllActivity();
        ExportUtils.ExportActivity(list, new Activity(), response);
    }

    /**
     * 选择导出市场活动
     * @param id
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/exportFindActivityById")
    public void exportFindActivityById(String[] id, HttpServletResponse response) {
        //返回根据id查询到的结果
        List<Activity> list = activityService.findAllActivityById(id);
        System.out.println("根据id查询出来的Activity: " + list);
        ExportUtils.ExportActivity(list, new Activity(), response);
    }

    /**
     * 批量上传数据
     * @param activityFile  前端发送的file文件的一切信息都被springMVC封装进MultipartFile这个类中
     */
    @RequestMapping("/workbench/activity/importActivityForFile")
    @ResponseBody
    public Object importActivityForFile(MultipartFile activityFile, HttpSession session) {
        //从session中获取登录的时候存储到服务器的用户
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        returnObject returnObj = new returnObject();
        try{
            //把Excel文件写到磁盘目录中
            //获取原始文件的名称
            String originalFilename = activityFile.getOriginalFilename();
            File file = new File("E:\\Java\\crm-project\\crmServiceExcel",originalFilename);
            //将前端上传的文件转移至E:\Java\crm-project\crmServiceExcel文件夹中
            activityFile.transferTo(file);

            //解析Excel文件，将文件中的数据读取到并封装到List<Activity>集合中
            //读取服务器端的Excel文件
            InputStream inputStream = new FileInputStream("E:\\Java\\crm-project\\crmServiceExcel\\" + originalFilename);
            //根据读取到的Excel文件生成对应的HSSFWorkbook类对象，封装了Excel文件的所有信息
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            //根据下标索引获取Excel文件的表
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            String value = "";
            List<Activity> list = new ArrayList<>();
            //判断Excel是不是空表，表中最后一行的索引为0时，表中没有数据
            if(sheet.getLastRowNum()==0){
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObj.setMessage("导入失败，可能您的表格中没有数据");
                //如果是空表，就不进行下面操作，直接返回
                return returnObj;
            }
            //遍历表中的每一行，第一行是字段名称不用读取，从第二行开始读取数据
            //sheet.getLastRowNum() 获取表中最后一行的索引
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                //每一行都封装进一个Activity对象中
                Activity activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreate_time(DateUtils.formatDateTime(new Date()));
                activity.setCreate_by(user.getId());
                //遍历行中每一列
                //row.getLastCellNum()  获取行中最后一列的索引+1
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    //获取行中的列
                    cell = row.getCell(j);
                    //获取列中的数据
                    if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
                        value = cell.getStringCellValue();
                    }else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                        value = cell.getNumericCellValue()+"";
                    }else if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
                        value = cell.getBooleanCellValue()+"";
                    }else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
                        value = cell.getDateCellValue()+"";
                    }else {
                        value = "";
                    }
                    switch (j){
                        case 0: activity.setName(value);
                        case 1: activity.setStart_date(value);
                        case 2: activity.setEnd_date(value);
                        case 3: activity.setCost(value);
                        case 4: activity.setDescription(value);
                    }
                }
                //每一列的数据都封装到Activity后，将Activity放到List集合中
                list.add(activity);
            }
            int i = activityService.insertActivityByList(list);
            System.out.println("导入的返回结果是：" + i);
            //当上传的Excel为空表时，影响的条数为0。数据存储失败，影响的条数为0。数据存储成功，影响的条数大于0
            if(i>=1){
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObj.setMessage("导入成功，导入"+ i +"条数据");
            }else{
                returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObj.setMessage("系统忙，请稍后重试.....");
            }

            //关闭资源
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            returnObj.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObj.setMessage("系统忙，请稍后重试.....");
        }

        return returnObj;
    }

}
