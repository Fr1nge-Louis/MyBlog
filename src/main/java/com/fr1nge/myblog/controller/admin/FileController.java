package com.fr1nge.myblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin")
public class FileController {

    @GetMapping("/files")
    public String linkPage(HttpServletRequest request) {
        request.setAttribute("path", "files");
        return "admin/file";
    }

//    @PostMapping({"/upload/file"})
//    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
//        String fileName = file.getOriginalFilename();
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        //生成文件名称通用方法
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        Random r = new Random();
//        StringBuilder tempName = new StringBuilder();
//        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
//        String newFileName = tempName.toString();
//        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
//        //创建文件
//        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
//        try {
//            if (!fileDirectory.exists()) {
//                if (!fileDirectory.mkdir()) {
//                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
//                }
//            }
//            file.transferTo(destFile);
//            Result resultSuccess = ResultGenerator.genSuccessResult();
//            resultSuccess.setData(MyBlogUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
//            return resultSuccess;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResultGenerator.genFailResult("文件上传失败");
//        }
//    }

}
