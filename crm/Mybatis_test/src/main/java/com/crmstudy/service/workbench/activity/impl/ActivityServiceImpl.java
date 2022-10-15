package com.crmstudy.service.workbench.activity.impl;

import com.crmstudy.domain.Activity;
import com.crmstudy.mapper.workbench.activity.ActivityMapper;
import com.crmstudy.service.workbench.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Activity> selectAllActivity(){
        return mapper.selectAllActivity();
    };
}
