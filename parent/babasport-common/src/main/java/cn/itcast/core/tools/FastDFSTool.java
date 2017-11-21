package cn.itcast.core.tools;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * FastDFS的工具类
 * 
 * @author Administrator
 *
 */
public class FastDFSTool {

	/**
	 * 上传文件到 FastDFS中
	 * 
	 * @param bs
	 * @param filename
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static String uploadFile(byte[] bs, String filename) throws IOException, Exception {

		// 获得classpath下文件的绝对路径
		ClassPathResource classPathResource = new ClassPathResource(
				"fdfs_client.conf");

		String path = classPathResource.getClassLoader()
				.getResource("fdfs_client.conf").getPath();

		System.out.println("path:" + path);

		// 将mpf的file上传给fastdfs中 需要fastdfs提供的java的api fastdfs——client.jar

		// 初始化信息，参数是一个配置文件，
		ClientGlobal.init(path);

		// 创建老大客户端
		TrackerClient trackerClient = new TrackerClient();

		// 通过老大客户端取得连接获得老大服务器端
		TrackerServer connection = trackerClient.getConnection();

		// 小弟客户端
		StorageClient1 storageClient1 = new StorageClient1(connection, null);

		// 截取文件的扩展名
		String extension = FilenameUtils.getExtension(filename);

		// 通过小弟客户端开始上传文件，并返回存放在fastdfs中的文件位置及文件名
		String upload_file1 = storageClient1.upload_file1(bs,
				extension, null);

		return upload_file1;

	}

}
