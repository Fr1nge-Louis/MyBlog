package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.BlogFile;
import com.fr1nge.myblog.service.BlogFileService;
import com.fr1nge.myblog.util.GetMD5;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/admin")
public class FileController {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${file.requrl}")
    private String hostname;

    @Resource
    private BlogFileService fileService;

    @GetMapping("/manageFile")
    public String manageFilePage(HttpServletRequest request) {
        request.setAttribute("path", "manageFile");
        return "admin/fileManage";
    }

    @GetMapping("/uploadFile")
    public String uploadFilePage(HttpServletRequest request) {
        request.setAttribute("path", "uploadFile");
        return "admin/fileUpload";
    }

    @Transactional
    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {
            //获取文件md5
            String fileMD5 = GetMD5.encryptFile(file.getInputStream());
            LambdaQueryWrapper<BlogFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BlogFile::getFileMd5, fileMD5);
            BlogFile blogFile = fileService.getOne(lambdaQueryWrapper);
            if (blogFile != null) {
                log.info("该文件已存在,url=" + blogFile.getFileUrl());
                Result result = ResultGenerator.genSuccessResult();
                result.setData(hostname+blogFile.getFileRealName());
                return result;
            }

            //生成文件名称通用方法
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(now)).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();

            //文件记录储存DB
            blogFile = new BlogFile();
            blogFile.setFileMd5(fileMD5)
                    .setFileName(fileName)
                    .setFileSize(file.getSize())
                    .setFileRealName(newFileName)
                    .setFileUrl(fileDir + newFileName)
                    .setCreateTime(now);
            boolean flag = fileService.save(blogFile);
            if (!flag) {
                log.error("DB记录文件失败");
                throw new RuntimeException();
            }
            //创建文件
            File fileDirectory = new File(fileDir);
            File destFile = new File(fileDir + newFileName);
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    log.error("文件夹创建失败,路径为：" + fileDirectory);
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(hostname+newFileName);
            return resultSuccess;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("文件上传失败");
            response.setStatus(500);
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }


    @GetMapping("/file/list")
    @ResponseBody
    public Result listFile(@RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer limit,
                           @RequestParam(required = false) String keyword) {

        LambdaQueryWrapper<BlogFile> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(BlogFile::getFileName, keyword).or()
                    .like(BlogFile::getFileUrl, keyword);
        }
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<BlogFile> pageQuery = new Page<>((page - 1) * page, limit);
        IPage<BlogFile> fileIPage = fileService.selectPage(pageQuery, queryWrapper);
        List<BlogFile> blogFileList = fileIPage.getRecords();
        for (int i = 0; i < blogFileList.size(); i++) {
            blogFileList.get(i).setFileReqUrl(hostname+blogFileList.get(i).getFileRealName());
        }

        PageResult pageResult = new PageResult(blogFileList, (int) fileIPage.getTotal(),
                (int) fileIPage.getSize(), (int) fileIPage.getCurrent());
        return ResultGenerator.genSuccessResult(pageResult);
    }


}
