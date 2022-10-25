package com.crmstudy.service.workbench.activity.impl;

import com.crmstudy.domain.Activity;
import com.crmstudy.mapper.workbench.activity.ActivityMapper;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;


@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper mapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return mapper.saveCreateActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map map){
        return mapper.selectActivityByConditionForPage(map);
    };

    @Override
    public int selectActivityByCondition(Map map) {
        return mapper.selectActivityByCondition(map);
    }

    @Override
    public int deleteActivity(String[] id) {
        return mapper.deleteActivity(id);
    }

    @Override
    public Activity selectActivityById(String id) {
        return mapper.selectActivityById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        return mapper.updateActivity(activity);
    }

    @Override
    public List<Activity> findAllActivity() {
        return mapper.findAllActivity();
    }
}
