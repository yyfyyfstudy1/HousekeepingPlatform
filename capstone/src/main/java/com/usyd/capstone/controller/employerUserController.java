package com.usyd.capstone.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.component.NotificationServer;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.Notification;
import com.usyd.capstone.entity.Task;
import com.usyd.capstone.entity.TaskOngoing;
import com.usyd.capstone.entity.VO.TaskVO;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.entity.VO.ModifyTaskInfoVO;
import com.usyd.capstone.service.NotificationService;
import com.usyd.capstone.service.TaskOngoingService;
import com.usyd.capstone.service.TasksService;
import com.usyd.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/member/employer")
public class employerUserController {
    @Autowired
    private TasksService tasksService;

    @Autowired
    private TaskOngoingService taskOngoingService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${upload.dir}") // 从配置文件获取上传目录
    private String uploadDir;

    @Value("${image.Url}") // 从配置文件获取上传目录
    private String imageUrl;

    @Autowired
    NotificationService notificationService;
    @PostMapping("/postTask")
    public Result postTask(@RequestBody TaskVO taskVO){
        System.out.println(taskVO);

        Task task = new Task();
        task.setTaskDescribe(taskVO.getDescribe());
        task.setTaskLabel(taskVO.getLabels());
        task.setCategory(taskVO.getCategory());
        task.setTaskTitle(taskVO.getTitle());
        task.setTaskSalary(taskVO.getSalary());
        task.setTaskUserId(taskVO.getUserID());
        task.setTaskBeginTime(taskVO.getTaskTimeStamp());
        task.setTaskEstimatedDuration(taskVO.getDuration());
        task.setTaskImageUrl(taskVO.getImageUrl());
        task.setTaskLocation(taskVO.getLocation());
        task.setTaskIsLocked(0);
        task.setTaskIsFinished(0);
        boolean result =  tasksService.save(task);

        Integer id = task.getTaskId();  // 这里能够获取到数据库中生成的ID

        // save task to taskOngoing
        TaskOngoing taskOngoing = new TaskOngoing();
        taskOngoing.setTaskId(id);
        taskOngoing.setTaskPhase(1);
        taskOngoing.setEmployerId(taskVO.getUserID());

        taskOngoing.setTaskBeginTime(taskVO.getTaskTimeStamp());

        boolean result2 =  taskOngoingService.save(taskOngoing);


       if (result && result2){
           return Result.suc(id);
       }else {
           return Result.fail();
       }

    }


    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("Please select a file to upload.");
        }

        try {
            // 确保上传目录存在，如果不存在则创建
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成新的文件名，将时间戳添加到原始文件名中
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String newFileName = fileNameWithoutExtension + System.currentTimeMillis() + fileExtension;
            String filePath = uploadDir + File.separator + newFileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            String imageUrlUse = imageUrl + newFileName;
            // 返回成功响应
            return Result.suc(imageUrlUse);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/employerConfirmTask")
    public Result employerConfirmTask(@RequestBody UserPhase userPhase){
     return   taskOngoingService.employerConfirmTask(userPhase);

    }

    @GetMapping("/getCurrentTaskPhase")
    public Result getCurrentTaskPhase(@RequestParam Integer taskId){
        TaskOngoing taskOngoing = taskOngoingService.getOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
        );

        if (taskOngoing !=null){
            return Result.suc(taskOngoing.getTaskPhase());
        }else {
            return Result.fail("get the task id faild");
        }
    }

    @GetMapping("/getTaskDetailById")
    public Result getTaskDetailById(@RequestParam Integer taskId){
        Task task = tasksService.getById(taskId);
        if (task !=null){
            return Result.suc(task);
        }else {
            return Result.fail("get task detail fail");
        }
    }

    @GetMapping("/getTaskPhaseFourBeginTime")
    public Result getTaskPhaseFourBeginTime(@RequestParam Integer taskId){
        TaskOngoing task = taskOngoingService.getOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
        );
        if (task !=null){
            return Result.suc(task);
        }else {
            return Result.fail("get task fail");
        }
    }


    @GetMapping("/getLaborWorkDuration")
    public Result getLaborWorkDuration(@RequestParam Integer taskId){
        TaskOngoing taskOngoing = taskOngoingService.getOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
        );

        return  Result.suc(taskOngoing.getLaborWorkTime());

    }

    @GetMapping("/deleteMyTask")
    public Result deleteMyTask(@RequestParam Integer taskId, @RequestParam Integer userRole){

        /**
         * userRole: 1 = employer, 2= labor
         */
        TaskOngoing taskOngoing = taskOngoingService.getOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
        );

        Task task = tasksService.getOne(
                new QueryWrapper<Task>().eq("task_id", taskId)
        );


        if (taskOngoing.getTaskPhase()>3){
            return Result.fail("The task can`t cancel in this phase");
        }

        boolean canCancel;

        if (userRole == 1){
           canCancel =  canUserCancel(taskOngoing.getEmployerId());
        }else {
           canCancel = canUserCancel(taskOngoing.getLaborId());
        }


        // 如果超过操作次数就返回
        if (!canCancel){
            return Result.fail("You have used up three operation opportunities today ");
        }

        if (userRole == 1){

            // 然后是employer就直接删除任务

            boolean isRemoved = taskOngoingService.remove(
                    new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
            );
            boolean isRemoved2 = tasksService.remove(
                    new QueryWrapper<Task>().eq("task_id", taskId)
            );

            if (isRemoved && isRemoved2){

                // 通知tasker
                sendNotification(taskOngoing.getTaskId(), taskOngoing.getLaborId(), "employer has delete the task [ " +task.getTaskTitle() +" ]" );
                return  Result.suc();

            }else {

                return  Result.fail();
            }
        } else {
            // 如果是labor就把任务状态归为1, 重新匹配labor

            taskOngoing.setTaskPhase(1);

            // 清空laborID
            taskOngoing.setLaborId(0);
            taskOngoingService.saveOrUpdate(taskOngoing);

            // 通知Employer
            sendNotification(taskOngoing.getTaskId(), taskOngoing.getEmployerId(), "tasker has cancel the task [ " +task.getTaskTitle() +" ]" );

            return Result.suc();
        }



    }


    @PostMapping("/editMyTask")
    public Result editMyTask(@RequestBody ModifyTaskInfoVO modifyTaskInfoVO){

        Task task = tasksService.getById(modifyTaskInfoVO.getTaskId());

        TaskOngoing taskOngoing = taskOngoingService.getOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", modifyTaskInfoVO.getTaskId())
        );

        if (taskOngoing.getTaskPhase()>3){
            return Result.fail("The task information can`t edit in this phase");
        }


        // 判断用户是否操作超过三次
        if (!canUserCancel(taskOngoing.getEmployerId())){
            return Result.fail("You have used up three operation opportunities today");
        }

            // 更新Task表
        task.setTaskTitle(modifyTaskInfoVO.getTaskTitle());
        task.setTaskDescribe(modifyTaskInfoVO.getTaskDescribe());
        task.setTaskLocation(modifyTaskInfoVO.getTaskLocation());
        task.setTaskBeginTime(modifyTaskInfoVO.getTaskBeginTime());
        boolean taskUpdate = tasksService.saveOrUpdate(task);


        // 更新TaskOngoing表

        taskOngoing.setTaskBeginTime(modifyTaskInfoVO.getTaskBeginTime());
        boolean taskOngoingUpdate = taskOngoingService.saveOrUpdate(taskOngoing);

        // 通知tasker
        sendNotification(taskOngoing.getTaskId(), taskOngoing.getLaborId(), "employer has modified the task information [ " + task.getTaskTitle() +" ]" );


        if (taskUpdate && taskOngoingUpdate){
            return Result.suc();
        }

        return  Result.fail();

    }

    private void sendNotification(Integer taskId, Integer messageReceiverId, String notificationMessage){
        // save notification to database
        com.usyd.capstone.entity.Notification notificationDB = new com.usyd.capstone.entity.Notification();
        notificationDB.setTaskId(taskId);
        notificationDB.setContent(notificationMessage);
        notificationDB.setIsRead(0);
        notificationDB.setReceivedUserId(messageReceiverId);
        notificationDB.setSendTime(System.currentTimeMillis());

        notificationService.save(notificationDB);

        Notification notification = new Notification();


        // 设置notification的ID
        notification.setNotificationId(notificationDB.getId());
        String result = JSONObject.toJSONString(notification);

        // send message
        NotificationServer.sendMessage(result, messageReceiverId);


    }

    public boolean canUserCancel(Integer userId) {
        // 定义键
        String key = "user:" + userId + ":cancel";

        // 检查键是否存在

        // 如果不存在就插入键
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().set(key, "1", 24, TimeUnit.HOURS);
            return true;
        } else {
            int currentValue = Integer.parseInt(redisTemplate.opsForValue().get(key));
            if (currentValue < 3) {
                // 递增并返回新值
                long newValue = redisTemplate.opsForValue().increment(key);
                return newValue <= 3;
            } else {
                return false;
            }
        }
    }

}
