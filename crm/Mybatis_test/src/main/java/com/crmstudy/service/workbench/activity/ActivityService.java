package com.crmstudy.service.workbench.activity;

import com.crmstudy.domain.Activity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Map;
import java.util.List;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    List<Activity> selectActivityByConditionForPage(Map map);
    int selectActivityByCondition(Map map);
    int deleteActivity(String[] id);
    Activity selectActivityById(String id);
    int updateActivity(Activity activity);
    List<Activity> findAllActivity() throws Exception;
}
