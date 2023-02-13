package com.example.iparking.controller;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/* web.xml中配置
<multipart-config>
<location>D:\\upload</location>
<max-file-size>209715200</max-file-size>
<max-request-size>419430400</max-request-size>
<file-size-threshold>0</file-size-threshold>
</multipart-config>
或
application.properties中配置
spring.http.multipart.maxFileSize=200Mb
spring.http.multipart.maxRequestSize=1024M
*/

@Controller
@RequestMapping("/receive")
public class ReceiveFilesController {

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver getStandardServletMultipartResolver(){
            return new StandardServletMultipartResolver();
    }

	@ResponseBody
	@RequestMapping(value = "/receiveDivsionFiles", produces = "multipart/form-data; charset=utf-8", method = RequestMethod.POST)
	public void receiveDivsionFiles(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//		System.out.println("接收到的参数: [File_Smry_Inf = " + request.getParameter("File_Smry_Inf")
//				+ ", Sign_Inf = " + request.getParameter("Sign_Inf"));
//		//验签原串
//		String oriString = "File_Smry_Inf=" + request.getParameter("File_Smry_Inf");
//		String signInf = request.getParameter("Sign_Inf");
//
//		//SM2算法采用公钥
//		//String pubKey = "04083E2CA7E71E51DB5374A49A3C07066390BD18C53B12A939D54C33E39E6916386F448B81D003BF76155EFEA565CD9818F6B84E846CB57CD4364BC715766D4FEC";
//		//RSA算法加密公钥
//		String pubKey_rsa = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh8rZ2GyPfrunGiprc6mpePhxaUjk8jigkxol/TYzMzhLwKIJZFu+04AIn8m3DRQu4J0offZ8/xCauMcsRL9l6CuuuHj6+Jlp20Zz5zjFvMyZ4j05ZwAgdiVZlM3tHg7YitRO8EZTYFbt7XNdqX+fzjkSuYleuGRr0Q7D5Ifmo3R+UcrQo8nz6X1+2jrCW8jc4TFDG6/V98dfaPyeP7HjIVuRVpe7dW7Bnrdqwq9m5x1bD7dXYMGyoOqyzqLDI5klz20L/vsjJrDNQtbhYDZZrlt4KnshXr2sSf264/3+AnupXvgjyrEjMssszK5hHuQ/sJofZ7XO6CIa9dZaDtz3vwIDAQAB";
//
//		//boolean b = SignUtil.verify(oriString, signInf, pubKey);    //国密验签
//		boolean b = RSASignUtil.verifySign(pubKey_rsa, oriString, signInf);    //RSA验签
//
//		if(!b) {
//			throw new RuntimeException("验签失败");
//		}
		//存放路径
		String dstPath = "D://receiveFiles";
		//设置编码格式
		request.setCharacterEncoding("utf-8");
		//判断是否有文件
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart) {
			StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
			Iterator<String> iterator = req.getFileNames();

			System.out.println(iterator.hasNext());
			while(iterator.hasNext()) {
				MultipartFile file = req.getFile(iterator.next());
				String fileName = file.getOriginalFilename();
				System.out.println(fileName);
				String filePath = dstPath + "/" + fileName;
				File desFile = new File(filePath);
				if(!desFile.getParentFile().exists()) {
					desFile.mkdirs();
				}
				try {
					file.transferTo(desFile);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}
