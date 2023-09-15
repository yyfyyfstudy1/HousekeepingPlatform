package com.usyd.capstone.controller;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.Task;
import com.usyd.capstone.entity.VO.TaskVO;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/member/employer")
public class employerUserController {
    @Autowired
    private TasksService tasksService;
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

       boolean result =  tasksService.save(task);

       if (result){
           return Result.suc();
       }else {
           return Result.fail();
       }

    }

    @Value("${upload.dir}") // 从配置文件获取上传目录
    private String uploadDir;

    @Value("${image.Url}") // 从配置文件获取上传目录
    private String imageUrl;

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
}
