package com.project.module.bs.controller.vo;

import com.project.entity.Meeting;
import com.project.common.annotation.Sort;
import io.swagger.annotations.ApiModelProperty;
/**
 * 接受请求参数
 * @author yelinsen
 * @version:2017-2-7
 */
public class MeetingVO extends Meeting{
	//TODO 可以扩展参数
	@Sort("su.name")
	@ApiModelProperty(value="用户名")
	private String name;//（用户名）

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @ApiModelProperty(hidden=true)
    public String getOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return '%'+name.trim()+'%';
    }
    @ApiModelProperty(hidden=true)
    public String getLeftOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return '%'+name.trim();
    }
    @ApiModelProperty(hidden=true)
    public String getRightOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return name.trim()+'%';
    }
}