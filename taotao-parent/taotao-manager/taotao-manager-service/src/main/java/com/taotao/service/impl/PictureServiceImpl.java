package com.taotao.service.impl;

import java.io.File;
import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;

@Service
public class PictureServiceImpl implements PictureService {

	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	@Override
	public PictureResult uploadFile(MultipartFile multipartFile) {
		PictureResult result = null;

		try {
			String oldName = multipartFile.getOriginalFilename();
			String newName = IDUtils.genImageName();
			newName = newName + oldName.substring(oldName.lastIndexOf("."));

			String imagepath = new DateTime().toString("/yyyy/MM/dd");

			boolean flag = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH,
					imagepath, newName, multipartFile.getInputStream());

			if (!flag) {
				result = new PictureResult(1, null, "文件上传失败");
				return result;
			}
			result = new PictureResult(0, IMAGE_BASE_URL + imagepath + File.separator + newName);
			return result;
		} catch (IOException e) {
			result = new PictureResult(1, null, "文件上传发生异常");
			return result;
		}
	}

}
