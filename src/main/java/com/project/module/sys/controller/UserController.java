package com.project.module.sys.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.constant.SystemConstant;
import com.project.common.controller.BaseController;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.CommonUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.sys.controller.vo.RoleVO;
import com.project.module.sys.controller.vo.UserDTO;
import com.project.module.sys.service.RoleService;
import com.project.module.sys.service.UserService;

/**
 * 用户管理的扩展controller
 * ClassName: UserExtController <br/>
 *
 * @author xt
 * @version 2017年2月7日
 */
@Controller
@RequestMapping("/sys/user")
@ApiIgnore
public class UserController extends BaseController<UserDTO>{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    
    @Value("${defaultPassword}")
    private String password;//后台页面管理员添加用户，与重置密码时使用的默认密码
    
    @RequestMapping(value="/manage",method=RequestMethod.GET)
    public String manage(Map<String,Object> map,UserDTO userVO){
        SessionHolder.setMenuMap(map, "sys/user");
        RoleVO roleVO = new RoleVO();
        roleVO.setValid(true);
        roleVO.setPage(false);
        map.put("roles", roleService.queryMapByCondition(roleVO).getList());
        return "sys/user_list";
    }
    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ResponseBody
    public Object insert(UserDTO condition) {
        condition.setPassword(password);
        if(CommonUtils.hasBlank(condition.getName(),condition.getUsername())){
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        condition.setSalt(CommonUtils.get32UUID());// 新增的时候,随机一个盐值
        condition.setPassword(CommonUtils.getPasswordBySalt(condition.getUsername(), condition.getPassword(), condition.getSalt()));
        return userService.doInsert(condition);
    }
    /**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ResponseBody
    public Object query(UserDTO condition) {
        return userService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public Object update(UserDTO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.doUpdate(condition);
    }
    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ResponseBody
    public Object doInvalidate(@ApiIgnore UserDTO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object doBatchInvalid(UserDTO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object batchRevalid(UserDTO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ResponseBody
    public Object revalid(@ApiIgnore UserDTO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.doRevalidate(condition);
    }

}