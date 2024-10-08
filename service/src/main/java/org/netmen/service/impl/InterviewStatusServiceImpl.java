package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.netmen.dao.mapper.InterviewStatusMapper;
import org.netmen.dao.po.InterviewStatus;
import org.netmen.service.InterviewRecordService;
import org.netmen.service.InterviewStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InterviewStatusServiceImpl extends ServiceImpl<InterviewStatusMapper, InterviewStatus> implements InterviewStatusService {

    @Autowired
    private InterviewRecordService interviewRecordService;

    /**
     * 面试不通过
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interviewFailed(Integer id) {
        InterviewStatus interviewStatus = getById(id);
        Boolean adjust = interviewStatus.getAdjust();
        // 1.如果当前处于第一志愿部门面试
        if (interviewStatus.getCurDepartmentId().equals(interviewStatus.getFirstDepartmentId())) {
            // 第一志愿部门和第二志愿部门相同时
            if (interviewStatus.getFirstDepartmentId().equals(interviewStatus.getSecondDepartmentId())) {
                // 是否调剂意愿为否
                if (adjust.equals(false)) {
                    // 淘汰
                    lambdaUpdate()
                            .set(InterviewStatus::getStatus, ((short) 2))
                            .eq(InterviewStatus::getId, id)
                            .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                            .update();
                    interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                    return;
                } else {
                    lambdaUpdate()
                            .set(InterviewStatus::getCurDepartmentId, 0)
                            .eq(InterviewStatus::getId, id)
                            .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                            .update();
                    interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                    return;
                }
            } else {
                // 不相同，等待第二志愿部门同意面试，也就是进入第二志愿部门第0面
                lambdaUpdate()
                        .set(InterviewStatus::getCurDepartmentId, interviewStatus.getSecondDepartmentId())
                        .eq(InterviewStatus::getId, id)
                        .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                        .update();
                interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                return;
            }
        }
        // 2.当前处于第二志愿部门面试
        if (interviewStatus.getCurDepartmentId().equals(interviewStatus.getSecondDepartmentId())) {
            // 是否调剂意愿为否
            if (adjust.equals(false)) {
                // 淘汰
                lambdaUpdate()
                        .set(InterviewStatus::getStatus, ((short) 2))
                        .eq(InterviewStatus::getId, id)
                        .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                        .update();
                interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                return;
            } else {
                // 进入待调剂状态
                lambdaUpdate()
                        .set(InterviewStatus::getCurDepartmentId, 0)
                        .eq(InterviewStatus::getId, id)
                        .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                        .update();
                interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                return;
            }

        }

        // 3.当前处于调剂部门面试,被淘汰后继续等待调剂
        if (interviewStatus.getCurDepartmentId() != 0) {
            lambdaUpdate()
                    .set(InterviewStatus::getCurDepartmentId, 0)
                    .eq(InterviewStatus::getId, id)
                    .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                    .update();
            interviewRecordService.interviewFailed(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
            return;
        }

        // 没有部门挑选，彻底淘汰
        lambdaUpdate()
                .set(InterviewStatus::getStatus, ((short) 2))
                .eq(InterviewStatus::getId, id)
                .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                .update();
    }

    /**
     * 面试通过
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interviewPass(Integer id) {
        InterviewStatus interviewStatus = getById(id);

        // 最后一轮通过
        if (interviewRecordService.interviewPass(id, interviewStatus.getCurDepartmentId()) == 1){
            lambdaUpdate()
                    .set(InterviewStatus::getStatus, ((short) 1))
                    .eq(InterviewStatus::getId, id)
                    .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                    .update();
        }

    }

    /**
     * 调剂部门挑选接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustDepartmentSelect(Integer departmentId, Integer id) {
        InterviewStatus interviewStatus = getById(id);
        lambdaUpdate()
                .set(InterviewStatus::getCurDepartmentId, departmentId)
                .eq(InterviewStatus::getId, id)
                .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                .update();
        interviewRecordService.adjustStudentInterviewRecord(id, departmentId);
    }

    /**
     * 防误触接口
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void falseTouchRejection(Integer id) {
        InterviewStatus interviewStatus = getById(id);
        Short status = interviewStatus.getStatus();
        Integer curDepartmentId = interviewStatus.getCurDepartmentId();
        Integer firstDepartmentId = interviewStatus.getFirstDepartmentId();
        Integer secondDepartmentId = interviewStatus.getSecondDepartmentId();
        Integer curPriority = interviewRecordService.getCurPriority(id);
        // 如果当前为待面试状态
        if (status.equals(Short.valueOf("0"))) {
            // 1.如果当前处于第二志愿部门面试且处于待面试状态，回退到第一志愿部门
            if (curDepartmentId.equals(secondDepartmentId) && curPriority == 0) {
                // 回退到第一志愿部门
                lambdaUpdate()
                        .set(InterviewStatus::getCurDepartmentId, interviewStatus.getFirstDepartmentId())
                        .eq(InterviewStatus::getId, id)
                        .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                        .update();
                interviewRecordService.falseTouchRejection(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                return;
            }
            // 处于调剂部门，处于调剂部门第一面，回退到第二志愿部门
            if (curDepartmentId != firstDepartmentId && curDepartmentId != secondDepartmentId && curPriority == 1){
                lambdaUpdate()
                        .set(InterviewStatus::getCurDepartmentId, interviewStatus.getSecondDepartmentId())
                        .eq(InterviewStatus::getId, id)
                        .eq(InterviewStatus::getCurDepartmentId, interviewStatus.getCurDepartmentId())
                        .update();
                interviewRecordService.falseTouchRejection(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
                return;
            }
            interviewRecordService.falseTouchRejection(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
        } else if (status.equals(Short.valueOf("1"))) {
            // 当前为录取状态,则回退为待面试状态
            lambdaUpdate()
                    .set(InterviewStatus::getStatus, Short.valueOf("0"))
                    .eq(InterviewStatus::getId, id)
                    .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                    .update();
                interviewRecordService.falseTouchRejection(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
        } else {
            // 当前为淘汰状态,则回退为待面试状态
            lambdaUpdate()
                    .set(InterviewStatus::getStatus, ((short) 0))
                    .eq(InterviewStatus::getId, id)
                    .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                    .update();
            interviewRecordService.falseTouchRejection(id, interviewStatus.getFirstDepartmentId(), interviewStatus.getSecondDepartmentId());
        }
    }

    /**
     * 初始化面试状态
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initialize(Integer id) {
        InterviewStatus interviewStatus = getById(id);
        lambdaUpdate()
                .set(InterviewStatus::getStatus, ((short) 0))
                .set(InterviewStatus::getCurDepartmentId, interviewStatus.getFirstDepartmentId())
                .eq(InterviewStatus::getId, id)
                .eq(InterviewStatus::getStatus, interviewStatus.getStatus())
                .update();
        interviewRecordService.initialize(id);
    }


}
