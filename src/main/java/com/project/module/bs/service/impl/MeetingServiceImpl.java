package com.project.module.bs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.common.service.impl.BaseServiceImpl;
import com.project.module.bs.controller.vo.MeetingVO;
import com.project.module.bs.dao.MeetingMapper;
import com.project.module.bs.service.MeetingService;
import com.project.common.annotation.BaseAnnotation;
/**
 * @author yelinsen
 * @version:2017-2-7
 */
 @Service
public class MeetingServiceImpl extends BaseServiceImpl<MeetingVO> implements MeetingService{
	@BaseAnnotation
	@Autowired
	private MeetingMapper meetingMapper;
}
