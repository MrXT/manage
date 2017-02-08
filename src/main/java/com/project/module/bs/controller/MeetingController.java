package com.project.module.bs.controller;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import com.project.common.exception.BusinessException;
import com.project.common.util.ValidateUtils;
import com.project.common.constant.SystemConstant;
import com.project.common.annotation.ApiResponseDesc;
import com.project.common.annotation.ApiResponseDescs;
import com.project.common.annotation.ApiResponseDesc.ApiResponseType;
import com.project.common.bean.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.project.common.spring.SessionHolder;
import com.project.common.controller.BaseController;
import com.project.module.bs.controller.vo.MeetingVO;
import com.project.module.bs.service.MeetingService;

/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-2-7
 */
@Controller
@RequestMapping("/bs/meeting")
@Api(value="会议-接口")
public class MeetingController extends BaseController<MeetingVO>{
	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(value="/manage",method=RequestMethod.GET)
	@ApiIgnore
    public String manage(MeetingVO meetingVO,Map<String, Object> map){
    	//把增删改查权限放入map中
    	SessionHolder.setMenuMap(map, "bs/meeting");
       	return "bs/meeting_list";
    }
    /**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ApiOperation(value="会议查询",response=QueryResult.class)//控制swagger api文档 response返回类型 ,response 目前支持QueryResult,List,Object 
    @ApiImplicitParams(value = { //控制swagger api文档 request 请求参数
            @ApiImplicitParam(name = "meetingId", value = "会议id",paramType="query", dataType = "String"),
            @ApiImplicitParam(name="meetingName",paramType="query",dataType="String",value="会议名称"),@ApiImplicitParam(name="infor",paramType="query",dataType="String",value="会议简介"),@ApiImplicitParam(name="userId",paramType="query",dataType="String",value="用户ID")
    })  
    @ApiResponseDescs({//控制swagger api文档 response 返回json格式
        @ApiResponseDesc(value="meetingId",description="会议id"),
        @ApiResponseDesc(value="meetingName",description="会议名称"),@ApiResponseDesc(value="infor",description="会议简介"),@ApiResponseDesc(value="userId",description="用户ID"),@ApiResponseDesc(value="name",description="用户名"),
        @ApiResponseDesc(value="valid",type=ApiResponseType.BOOLEAN,description="有效"),
        @ApiResponseDesc(value="utime",description="操作时间"),
        @ApiResponseDesc(value="ctime",description="创建时间"),
        @ApiResponseDesc(value="uid",description="操作人ID")
    })
    @ResponseBody
    public Object query(@ApiIgnore MeetingVO condition) {//@ApiIgnore在接口文档界面上忽略该参数
        return meetingService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ApiOperation(value="会议修改")  
    @ApiImplicitParams(value = {  
            @ApiImplicitParam(name = "meetingId",required=true, value = "会议id",paramType="query", dataType = "String"),
            @ApiImplicitParam(name="meetingName",paramType="query",dataType="String",value="会议名称"),@ApiImplicitParam(name="infor",paramType="query",dataType="String",value="会议简介"),@ApiImplicitParam(name="userId",paramType="query",dataType="String",value="用户ID")
    })  
    @ResponseBody
    public Object update(@ApiIgnore MeetingVO condition) {
        if (ValidateUtils.isBlank(condition.getMeetingId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return meetingService.doUpdate(condition);
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ApiOperation(value="会议新增")  
    @ApiImplicitParams(value = {  
            @ApiImplicitParam(name="meetingName",paramType="query",dataType="String",value="会议名称"),@ApiImplicitParam(name="infor",paramType="query",dataType="String",value="会议简介"),@ApiImplicitParam(name="userId",paramType="query",dataType="String",value="用户ID")
    })  
    @ResponseBody
    public Object insert(@ApiIgnore MeetingVO condition) {
     	if (ValidateUtils.isBlank(condition.getMeetingName())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return meetingService.doInsert(condition);
    }
}