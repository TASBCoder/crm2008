package com.crmstudy.mapper.workbench.activity;

import com.crmstudy.domain.Activity;

import java.util.List;

public interface ActivityMapper {
    int saveCreateActivity(Activity activity);
    List<Activity> selectAllActivity();
}
