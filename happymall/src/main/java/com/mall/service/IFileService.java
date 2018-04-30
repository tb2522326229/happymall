package com.mall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	/**
	 * 文件上传
	 * @param file 上传的文件
	 * @param path 上传的路径
	 * @return 上传成功后的文件名
	 */
	String upload(MultipartFile file, String path);
}
