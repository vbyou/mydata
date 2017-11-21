package cn.itcast.core.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.itcast.core.dictionary.Constants;
import cn.itcast.core.tools.FastDFSTool;

/**
 * 文件上传控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class UploadAction {

	// 上传单个文件
	@RequestMapping(value = "/uploadFile.do")
	@ResponseBody
	public Map<String, String> uploadFile(MultipartFile mpf)
			throws FileNotFoundException, IOException, Exception {
		System.out.println("上传文件");
		System.out.println(mpf.getOriginalFilename());

		String filename = mpf.getOriginalFilename();

		String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(), filename);

		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("path", Constants.FDFS_SERVER + uploadFile);

		return hashMap;
	}

	// 上传多个文件
	@RequestMapping(value = "/uploadFiles.do")
	@ResponseBody
	public List<String> uploadFiles(@RequestParam MultipartFile[] mpfs)
			throws IOException, Exception {

		List<String> al = new ArrayList<String>();

		// System.out.println(mpfs.length);
		for (MultipartFile mpf : mpfs) {

			String filename = mpf.getOriginalFilename();
			String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
					filename);

			al.add(Constants.FDFS_SERVER + uploadFile);

		}

		return al;
	}

	// 富文本编辑器的文件上传
	// 接收富文本编辑器传递的图片(无敌版：不考虑文件的name，强行接收)
	@RequestMapping(value = "/uploadFck.do")
	@ResponseBody
	public Map<String, Object> uploadFck(HttpServletRequest request)
			throws IOException, Exception {
		System.out.println("富文本编辑器的文件上传");

		// 将request强转为spring提供的MultipartRequest
		MultipartRequest mr = (MultipartRequest) request;

		Map<String, MultipartFile> fileMap = mr.getFileMap();

		for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile mpf = entry.getValue();

			// 将mpf上传到分布式文件系统中
			String filename = mpf.getOriginalFilename();

			String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
					filename);

			Map<String, Object> hashMap = new HashMap();

			// error和url名字都是固定死的
			hashMap.put("error", 0);
			hashMap.put("url", Constants.FDFS_SERVER + uploadFile);

			return hashMap;
		}
		return null;
	}
}
