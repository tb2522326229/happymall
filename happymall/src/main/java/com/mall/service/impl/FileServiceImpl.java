package com.mall.service.impl;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;

@Service("fileService")
public class FileServiceImpl implements IFileService {
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	@Override
	public String upload(MultipartFile file, String path) {
		String fileName = file.getOriginalFilename();// 文件原始名
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);// 文件拓展名(jpg)
		String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;// 该文件上传到ftp服务器后的新名(为了防止上传的图片重名而导致后面的覆盖掉前面的图片)
		logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);// {}是占位符,后面的数量和字段意义必须对应前面三个占位符
		// 判断该路径的文件夹（path的最后一个文件夹）是否存在
		File fileDir = new File(path);
		if(!fileDir.exists()){// 如果该文件夹不存在，则创建
			fileDir.setWritable(true);// 给文件夹赋予写的权限
			fileDir.mkdirs();
		}
		File targetFile = new File(path,uploadFileName);
		try {
            file.transferTo(targetFile);//文件已经上传成功了（已经上传到upload文件夹下了）
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));//已经上传到ftp服务器上
            targetFile.delete();// 因为该文件是存在tomcat文件夹下，时间长了会很大，所以要删除upload下的文件
        } catch (Exception e) {
            logger.error("上传文件异常",e);
            return null;
        }
		return targetFile.getName();
	}

}
