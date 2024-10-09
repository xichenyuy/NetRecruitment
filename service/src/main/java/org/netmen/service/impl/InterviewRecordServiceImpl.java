package org.netmen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.common.result.Result;
import org.netmen.dao.mapper.DepartmentInterviewMapper;
import org.netmen.dao.mapper.InterviewRecordMapper;
import org.netmen.dao.po.DepartmentInterview;
import org.netmen.dao.po.InterviewRecord;
import org.netmen.service.InterviewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewRecordServiceImpl extends ServiceImpl<InterviewRecordMapper, InterviewRecord> implements InterviewRecordService {
    @Autowired
    private InterviewRecordMapper interviewRecordMapper;
    @Autowired
    private DepartmentInterviewMapper departmentInterviewMapper;


    /**
     * 获取部门所有面试记录
     * */
    @Override
    public List<Map<String, Object>> getInterviewRecordList(Integer departmentId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getDepartmentId,departmentId);
        Page<InterviewRecord> page = new Page<>(pageNum,pageSize);
        IPage<InterviewRecord> ipage = interviewRecordMapper.selectPage(page,wrapper);

        if(ipage == null || ipage.getRecords().isEmpty()){
            return null;
        }

        List<Map<String,Object>> list = ipage.getRecords().stream().map(interviewRecord -> {
            try{
                return convertToMap(interviewRecord);
            }catch (Exception e){
                throw new RuntimeException("InterviewRecord转换成Map出错:",e);
            }
        }).collect(Collectors.toList());

        return list;
    }

    /**
     * 获取学生面试记录
     * @param studentId
     * @return
     */
    @Override
    public List<InterviewRecord> getStudentInterviewRecordList(Integer studentId) {
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        return interviewRecordMapper.selectList(wrapper);
    }

    /**
     * 面试记录转换
     * */
    public Map<String,Object> convertToMap(InterviewRecord interviewRecord) {
        Map<String,Object> map = new HashMap<>();
        map.put("studentId",interviewRecord.getStudentId());
        map.put("departmentId",interviewRecord.getDepartmentId());
        map.put("failed",interviewRecord.getFailed());
        map.put("priority",interviewRecord.getPriority());
        return map;
    }

    /**
     *增加面试记录
     * 这一部分我们来一步一步考虑。
     * 首先是一个面试者走完了所有面试并被录取，那么在这里的表中，
     * 他所有记录中的priority应该是包含同一个department的所有priority，同时他的priority应该是自动在检索变化，
     * 而不是由interview_status操作输入priority
     * 然后他被淘汰了，那就是状态回退，直接滑入第二志愿一面，或者调剂队列里，那么他的面试级别应该要回滚，滚到第二志愿部门的优先级中
     * 以上可知，我们在向数据库中加入一个面试者的记录时，我们要先确定他当前处于什么部门的面试，并且是从什么状态来的
     * 是从当前部门的上一级面试来的，还是被上一级志愿部门淘汰下来的？
     * 如果是从当前面试部门的上一级面试而来，优先级只要变化成priority数组里的下一个就好。等他到了部门的最后一个priority，就是
     * 说明他已经被这个部门录取了。
     * 如果是从上一级志愿部门淘汰下来的，那就要调整priority。
     * 如果他最后是被第一志愿、第二志愿、调剂部门（假如服从调剂）淘汰掉了
     */
    //待会来改

    /**
     * 此更新方法已被弃用
     * */
//    public Result updateInterview(Integer id, Integer priority) {
//        InterviewRecord interviewRecord = interviewRecordMapper.selectById(id);
//        if(interviewRecord == null){
//            return Result.error().message("不存在此学生的面试记录");
//        }
//        interviewRecord.setPriority(priority);
//        int res = interviewRecordMapper.updateById(interviewRecord);
//        if(res == 0){
//            return Result.error().message("更新失败!");
//        }
//        return Result.success().message("更新成功!");
//    }

    /**
     * 插入学生的第一条记录
     * */
    //初始化一个填了报名表的学生，这个接口应该在student处调用,用来插入第一条数据
    //@Override
    public boolean initStudentInterviewRecord(Integer studentId, Integer currentDepartmentId){
        //首先要查查这个student的第一条记录有没有重复
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        List<InterviewRecord> list = interviewRecordMapper.selectList(wrapper);
        if(!list.isEmpty()) {
            return false;
        }
        //向数据库中加入这个学生的第一条数据
        List<DepartmentInterview> departmentInterviewList = getInterviewList(currentDepartmentId);
        if(departmentInterviewList.isEmpty()){
            return false;
        }
        //给这条记录加上他当前所处的优先级
        insertNewRecord(studentId,currentDepartmentId,departmentInterviewList.get(0).getPriority());
        return true;
    }

    /**
     * 插入调剂之后的第一条数据
     * */
    //@Override
    public boolean adjustStudentInterviewRecord(Integer studentId, Integer adjustDepartmentId){
        int res = insertNewRecord(studentId,adjustDepartmentId,getInterviewList(adjustDepartmentId).get(0).getPriority());
        if(res==0){
            return false;
        }
        return true;
    }

    /**
     * 学生通过上一轮面试，插入下一条面试数据
     * @param
     * @return
     * 返回值为1说明已经进行到最后一轮面试并且完成通过这个最后一轮了
     * 返回值为0说明还要继续面试
     */
    //@Override
    public Integer interviewPass(Integer studentId, Integer curDepartmentId){
        //上一条保持不变
        //先看最新一条的priority是不是当前部门的最高级别，也就是最后一轮面试
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        List<InterviewRecord> list = interviewRecordMapper.selectList(wrapper);
        if(list.isEmpty()){
            return 2;
        }

        //找出当前部门的所有priority
        List<DepartmentInterview> departmentInterviewList = getInterviewList(curDepartmentId);
        if(departmentInterviewList.isEmpty()){
            System.out.println("departmentInterviewList is Empty");
            return 3;
        }
        InterviewRecord curInterviewRecord = list.get(list.size()-1);
        if(curInterviewRecord.getPriority().equals(departmentInterviewList.get(departmentInterviewList.size()-1).getPriority())
                &&!curInterviewRecord.getFailed()){
            //说明已经完成了所有的面试流程
            return 1;//被录取了
        }

        //如果接下来还有面试，那就插入数据
        //先找出来当前是哪个priority

        if(curInterviewRecord.getPriority()==0){
            insertNewRecord(studentId,curDepartmentId,departmentInterviewList.get(0).getPriority());
            return 0;
        }

        for(int i = 0;i<departmentInterviewList.size()-1;i++){
            if(curInterviewRecord.getPriority().equals(departmentInterviewList.get(i).getPriority())){
                //找到下一个面试的优先级了
                insertNewRecord(studentId,curDepartmentId,
                        departmentInterviewList.get(i+1).getPriority());
                return 0;
            }
        }
        return 2;
    }

    /**
     * 淘汰学生
     * @param
     * @return
     */
    //@Override
    public boolean interviewFailed(Integer studentId, Integer firstDepartmentId, Integer secondDepartmentId) {
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        List<InterviewRecord> list = interviewRecordMapper.selectList(wrapper);
        //把这个学生的所有面试记录找出来
        if(list.isEmpty()){
            return false;
        }
        //找到最新的一条
        InterviewRecord lastInterviewRecord = list.get(list.size()-1);
        //将这次面试记录记为淘汰
        lastInterviewRecord.setFailed(true);
        int res = interviewRecordMapper.updateById(lastInterviewRecord);
        if(res == 0){
            return false;
        }
        //同时应该要考虑是从哪里淘汰掉的，是从第一志愿淘汰的，还是从第二志愿淘汰的，还是被调剂部门淘汰的
        if(lastInterviewRecord.getDepartmentId().equals(firstDepartmentId) &&
                !firstDepartmentId.equals(secondDepartmentId)){
            //假如会有第一志愿和第二志愿相同的情况,这种情况应该是直接跳过第二志愿
            //插入新一轮的开始
            insertNewRecord(studentId,secondDepartmentId,0);
        }
        return true;
    }

    //向数据库插入新的数据
    private Integer insertNewRecord(Integer studentId,Integer departmentId,Integer priority) {
        InterviewRecord newOne = new InterviewRecord();

        newOne.setStudentId(studentId);
        newOne.setDepartmentId(departmentId);
        newOne.setPriority(priority);
        newOne.setFailed(false);
        newOne.setDeleted(false);
        return interviewRecordMapper.insert(newOne);
    }


    /**
     * 删除学生记录
     * @param studentIds
     * @return
     * 如果在数组中间有删除失败的话，会返回是到哪个学生的时候删除失败的
     */
    @Override
    public Integer deleted(List<Integer> studentIds) {
        if(studentIds.isEmpty()){
            return Integer.MIN_VALUE;
        }
        for(int i = 0;i<studentIds.size();i++){
            LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InterviewRecord::getStudentId,studentIds.get(i));
            List<InterviewRecord> list = interviewRecordMapper.selectList(wrapper);
            if(list.isEmpty()){
                return studentIds.get(i);
            }
            if (list.stream().mapToInt(interviewRecord -> interviewRecordMapper.deleteById(interviewRecord)).anyMatch(res -> res == 0)) {
                return studentIds.get(i);
            }
        }
        return 0;
    }



    /**
     *
     * @param studentId
     * @return
     * true表示回滚成功
     * false表示回滚失败
     */
    //@Override
    public boolean falseTouchRejection(Integer studentId,Integer firstDepartmentId,Integer secondDepartmentId){
        //先获取所有面试记录
        LambdaQueryWrapper<InterviewRecord>  wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        List<InterviewRecord> list = interviewRecordMapper.selectList(wrapper);
        //要是有两条以上的数据，就拿倒数两条。只有一条，就给这条的状态改一下。没有数据，大哥那你触发这个干啥
        if(list.isEmpty()){
            return false;
        }
        if(list.size()>=2){
            InterviewRecord cur = list.get(list.size()-1);
//            InterviewRecord newNow = new InterviewRecord();
            InterviewRecord newNow = list.get(list.size()-2);
//            int newPriority = 0;
//            int newDepartmentId = 0;
//            boolean newStatus = false;


//            if(cur.getDepartmentId().equals(firstDepartmentId)){
//                List<DepartmentInterview> firstDepartmentInterviews = getInterviewList(firstDepartmentId);
//                newDepartmentId = firstDepartmentId;
//                if(cur.getPriority().equals(firstDepartmentInterviews.get(0).getPriority())){
//                    newPriority = cur.getPriority();
//                }else{
//                    newPriority = getNextPriority(firstDepartmentInterviews,-1, cur.getPriority());
//                }
//            }
//            else if((!Objects.equals(firstDepartmentId, secondDepartmentId))
//                    &&cur.getDepartmentId().equals(secondDepartmentId)){
//                List<DepartmentInterview> firstDepartmentInterviews = getInterviewList(firstDepartmentId);
//                List<DepartmentInterview> secondDepartmentInterviews = getInterviewList(secondDepartmentId);
//                if(cur.getPriority()==0){
//                    //说明要回到第一志愿里并且第一志愿和第二志愿不是同一个部门
//                    //要回到在第一志愿被淘汰的那一轮
//                    boolean flag = false;
//                    for(int i = list.size()-1;i>=0;i--){
//                        InterviewRecord interviewRecord = list.get(i);
//                        if(interviewRecord.getDepartmentId().equals(firstDepartmentId)&&
//                        interviewRecord.getFailed().equals(true)){
//                            newPriority = interviewRecord.getPriority();
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if(!flag){
//                        newPriority = firstDepartmentInterviews.get(firstDepartmentInterviews.size()-1).getPriority();
//                    }
//                    newDepartmentId = firstDepartmentId;
//                }
//                else if(cur.getPriority()==1){
//                    //回退到没被选择的状态
//                    newPriority = 0;
//                    newDepartmentId = secondDepartmentId;
//                }
//                else{
//                    newDepartmentId = secondDepartmentId;
//                    newPriority = getNextPriority(secondDepartmentInterviews,-1, cur.getPriority());
//                }
//            }
//            else{
//                List<DepartmentInterview> secondDepartmentInterviews = getInterviewList(secondDepartmentId);
//                List<DepartmentInterview> adjustDepartmentInterviews = getInterviewList(cur.getDepartmentId());
//                if(cur.getPriority().equals(adjustDepartmentInterviews.get(0).getPriority())){
//                    //说明调剂部门不要ta
//                    //让ta的状态维持在第二志愿被淘汰的的时候
//                    //要找到ta在第二志愿哪一轮面试被淘汰
//                    newPriority = secondDepartmentInterviews.get(secondDepartmentInterviews.size()-1).getPriority();
//                    newDepartmentId = secondDepartmentId;
//                    newStatus = true;
//                }else{
//                    newDepartmentId = secondDepartmentId;
//                    newPriority = getNextPriority(adjustDepartmentInterviews,-1,cur.getPriority());
//                }
//            }

            //只考虑部内回退的事，在淘汰之后没有权限回退
            //如果只是想从当前面试回退到上一轮面试，那就删除最后一条数据
//            if(cur.getDepartmentId().equals(newNow.getDepartmentId())&&!cur.getFailed()){
//                int deleteRes = interviewRecordMapper.deleteById(cur);
//                if(deleteRes!=0){
//                    return true;
//                }
//            }
//            //如果是想从第二志愿预面试回退到第一志愿,那么就要删掉第二志愿预面试的记录，同时要把第一志愿的淘汰记录修改掉
//            if(cur.getDepartmentId().equals(secondDepartmentId)&&cur.getPriority().equals(0)){
//                newNow.setFailed(false);
//                int deleteRes = interviewRecordMapper.deleteById(cur);
//                int updateRes = interviewRecordMapper.updateById(newNow);
//                if(updateRes!=0&&deleteRes!=0){
//                    return true;
//                }
//            }
//            //如果是调剂部门想要取消选择这个学生,就只删掉这条记录，不用改变第二志愿的淘汰结果
//            if(!cur.getDepartmentId().equals(firstDepartmentId)&&
//                !cur.getDepartmentId().equals(secondDepartmentId)){
//
//                int deleteRes = interviewRecordMapper.deleteById(cur);
//                if(deleteRes!=0){
//                    return true;
//                }
//            }
//            newNow.setStudentId(studentId);
//            newNow.setDepartmentId(newDepartmentId);
//            newNow.setPriority(newPriority);

            //int insertRes = interviewRecordMapper.insert(newNow);


            //cur.setFailed(false);
            //如果是要回退到第一志愿的最后一轮
            if(cur.getDepartmentId().equals(secondDepartmentId)&&cur.getPriority().equals(0)){
                newNow.setFailed(false);
                int updateRes = interviewRecordMapper.updateById(newNow);
                int deleteRes = interviewRecordMapper.deleteById(cur);
                if(updateRes!=0&&deleteRes!=0){
                    return true;
                }
            } else if(cur.getFailed()){
                //只要不是回退到第一志愿的最后一轮，只是要捞回来
                cur.setFailed(false);
                int updateRes = interviewRecordMapper.updateById(cur);
                if(updateRes!=0){
                    return true;
                }
            }else{
                //只是要回到上一轮面试
                int deleteRes = interviewRecordMapper.deleteById(cur);
                if(deleteRes!=0){
                    return true;
                }
            }
        }else{
            InterviewRecord cur = list.get(0);
            cur.setFailed(false);
            int updateRes = interviewRecordMapper.updateById(cur);
            if(updateRes!=0){
                return true;
            }
        }
        return false;
    }

    private int getNextPriority(List<DepartmentInterview> curDepartmentInterviews, int index,int nowPriority) {
        for(int i = 0;i<curDepartmentInterviews.size();i++){
            if(curDepartmentInterviews.get(i).getPriority()==nowPriority){
                return curDepartmentInterviews.get(i+index).getPriority();
            }
        }
        return 0;
    }

    /**
     *
     * @param studentId
     * @return
     */
    //@Override
    public boolean initialize(Integer studentId){
        //初始化
        List<InterviewRecord> list = getStudentInterviewRecordList(studentId);
        if(list.isEmpty()){
            return false;
        }
        //把第一条复制一下，把副本的fail设置成零
        InterviewRecord lastOne = new InterviewRecord();
        lastOne.setDeleted(false);
        lastOne.setStudentId(list.get(0).getStudentId());
        lastOne.setDepartmentId(list.get(0).getDepartmentId());
        lastOne.setPriority(getInterviewList(lastOne.getDepartmentId()).get(0).getPriority());
        lastOne.setFailed(false);
        //把前面所有的这个学生的记录全都逻辑删除
        for(int i = 0;i<list.size();i++){
           int deleteRes = interviewRecordMapper.deleteById(list.get(i));
           if(deleteRes == 0){
               return false;
           }
        }
        //最后再插入一条
        int res = interviewRecordMapper.insert(lastOne);
        if(res == 0){
            return false;
        }
        return true;
    }

    /**
     * 获取部门设立的所有面试
     * @param currentDepartmentId
     * @return
     */
    //获取一个部门的所有priority（但是这里好奇怪啊，我觉得应该是在DepartmentInterview那边写好，我这边调用才对）
    //不对，应该是在controller层里调用才对
    public List<DepartmentInterview> getInterviewList(Integer currentDepartmentId){
        LambdaQueryWrapper<DepartmentInterview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DepartmentInterview::getDepartmentId,currentDepartmentId);
        List<DepartmentInterview> res = departmentInterviewMapper.selectList(wrapper);
        if(res.isEmpty()){
            return null;
        }
        //给这个部门所有的interview按照priority排好序
        res.sort(Comparator.comparing(DepartmentInterview::getPriority));
        return res;
    }

    /**
     *
     * @param studentId
     * @return
     * 传入学生id之后，会获得一个这个学生当前所属的面试轮次
     * 如果没有这个学生的面试记录，会拿到一个最大的数字65535
     */
    @Override
    public Integer getCurPriority(Integer studentId){
        LambdaQueryWrapper<InterviewRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterviewRecord::getStudentId,studentId);
        List<InterviewRecord> interviewRecordList = interviewRecordMapper.selectList(wrapper);
        if(interviewRecordList.isEmpty()){
            return Integer.MAX_VALUE;
        }
        //拿到最新一轮的面试
        return interviewRecordList.get(interviewRecordList.size()-1).getPriority();
    }
}