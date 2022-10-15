package com.crmstudy.service.workbench.activity;

import com.crmstudy.domain.Activity;

import java.util.List;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    List<Activity> selectAllActivity();
}
