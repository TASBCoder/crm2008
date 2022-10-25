package com.crmstudy.controller.workbench.activity;

import com.crmstudy.commons.constans.Constant;
import com.crmstudy.commons.domain.returnObject;
import com.crmstudy.commons.utills.DateUtils;
import com.crmstudy.commons.utills.UUIDUtils;
import com.crmstudy.domain.Activity;
import com.crmstudy.domain.User;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    @RequestMapping("/workbench/activity/toDetail")
    public String toDetail(){
        return "workbench/activity/detail";
    }

    @RequestMapping("/workbench/activity/exportFindAllActivity")
    public void exportFindAllActivity(HttpServletResponse response) throws Exception{
        /**
         * 读取数据库中的数据，新建Excel存储查询的数据。将Excel表存储都服务器中
         */
        List<Activity> list = activityService.findAllActivity();
        //使用apache的poi来操作Excel
        //创建一个Excel文档wb
        HSSFWorkbook wb = new HSSFWorkbook();
        //在文档wb创建一个名为”所有市场活动“的Excel表sheet
        HSSFSheet sheet = wb.createSheet("所有市场活动");
        //活动List集合的迭代器
        Iterator<Activity> iterator = list.iterator();
        int i = 0;
        //获取Activity的class类
        Class<Activity> activityClass = Activity.class;
        //反射获取Activity中所有的属性
        Field[] declaredFields = activityClass.getDeclaredFields();
        //在表sheet中新建第一行row
        HSSFRow row = sheet.createRow(i);
        int indexCellNo = 0;
        //遍历所有的属性数组
        for (Field declaredField : declaredFields) {
            //属性都是私有的，暴力发射
            declaredField.setAccessible(true);
            //获取Activity类的所有属性名称
            String name = declaredField.getName();
            //在行row中创建列
            HSSFCell cell = row.createCell(indexCellNo++);
            //将属性名存到列中
            cell.setCellValue(name);
        }
        while(iterator.hasNext()){
            int j = 0;
            row = sheet.createRow(++i);
            Object next = iterator.next();
            for (Field declaredField : declaredFields) {
                HSSFCell cell = row.createCell(j++);
                PropertyDescriptor p = new PropertyDescriptor(declaredField.getName(), activityClass);
                Method readMethod = p.getReadMethod();
                if (readMethod.invoke(next)==null){
                    cell.setCellValue("null");
                }else{
                    cell.setCellValue(readMethod.invoke(next).toString());
                }
            }
        }
        //根据wb生成名称为activityList的Excel文件
        //写入磁盘效率太低下
//        OutputStream outputStream = new FileOutputStream("E:\\Java\\crm-project\\crmHaveExcel\\activityList.xls");
//        wb.write(outputStream);
        //关闭资源
//        outputStream.close();
//        wb.close();

        /**
         * 从服务器中返回Excel表
         */
        //设置响应类型和编码格式
        //application/octet-stream：表示响应的文件是二进制流，charset=UTF_8：表示编码格式是UTF-8
        response.setContentType("application/octet-stream;charset=UTF-8");
        //输出流
        OutputStream outputStream = response.getOutputStream();
        //读取文件
//        File file = new File("E:\\Java\\crm-project\\crmHaveExcel\\activityList.xls");
//        //判读文件是否存在
//        if (!(file.exists())) {
//            throw new Exception("文件不存在");
//        }
//        InputStream inputStream = new FileInputStream(file);
//        //设置缓冲区
//        byte[] buff = new byte[1024];
//        //默认是一个字节一个字节的读，效率太低，设置缓冲区可以一次读取你设置缓冲区大小的字节
//        int len = 0;
//        //判断什么时候读完文件
//        //当文件读取完毕后会返回-1，只要没返回-1就代表文件还没有读取完毕
//        while((len = inputStream.read(buff)) != -1){
//            //将读取到的字节写入输出流，写入的字节长度是每次读取的字节长度read
//            outputStream.write(buff,0,len);
//        }
//        //关闭资源（谁创建的谁关闭）
//        inputStream.close();

        //刷新输出流
//        outputStream.flush();
        //设置响应头
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        wb.write(outputStream);

        wb.close();
        outputStream.flush();
    }


}
