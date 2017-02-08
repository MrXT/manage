package com.project.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.annotation.BaseAnnotation;
import com.project.common.bean.QueryResult;
import com.project.common.service.impl.BaseServiceImpl;
import com.project.common.util.ListUtils;
import com.project.common.util.MapUtils;
import com.project.module.sys.controller.vo.UserDTO;
import com.project.module.sys.controller.vo.UserVO;
import com.project.module.sys.dao.UserMapper;
import com.project.module.sys.dao.UserRoleMapper;
import com.project.module.sys.service.UserService;

/**
 * @author yelinsen
 * @version:2017-1-2
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDTO> implements UserService {

    @BaseAnnotation
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public QueryResult<Map<String, Object>> queryMapByCondition(UserDTO condition) {
        QueryResult<Map<String, Object>> result = super.queryMapByCondition(condition);
        List<Map<String, Object>> users = result.getList();
        List<String> userIds = new ArrayList<String>();
        for (Map<String, Object> map : users) {
            userIds.add(map.get("userId").toString());
        }
        if (!ListUtils.isEmpty(userIds)) {
            UserVO queryUser = new UserVO();
            queryUser.setIds(userIds);
            queryUser.setValid(true);//查询有效的角色
            List<Map<String, Object>> roles = userRoleMapper.queryRoleByUser(queryUser);
            Map<Object, List<Map<String, Object>>> groupRoles = MapUtils.mapGroupByKey(roles, "userId");
            for (Map<String, Object> map : users) {
                List<Map<String, Object>> roleMaps = groupRoles.get(map.get("userId"));
                map.put("roles", roleMaps == null ? new ArrayList<>() : roleMaps);
            }
        }
        return result;
    }
}
