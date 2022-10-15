package com.crmstudy.service.settings.qx.user;

import com.crmstudy.domain.User;
import java.util.Map;
import java.util.List;

public interface UserService {

    User SelectUserByActNameAndActPwd(Map<String,Object> map);

    List<User> SelectAllUser();
}
