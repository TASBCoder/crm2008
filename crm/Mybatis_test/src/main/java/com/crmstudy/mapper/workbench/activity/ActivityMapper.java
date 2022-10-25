package com.crmstudy.mapper.workbench.activity;

import com.crmstudy.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    int saveCreateActivity(Activity activity);
    List<Activity> selectActivityByConditionForPage(Map map);
    int selectActivityByCondition(Map map);
    int deleteActivity(String[] id);
    Activity selectActivityById(String id);
    int updateActivity(Activity activity);
    List<Activity> findAllActivity();
}
