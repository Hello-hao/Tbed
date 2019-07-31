package cn.hellohao.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月10日
 */
public class FTPUtils {

	private final Logger LOGGER = LogManager.getLogger(getClass());

	private FTPClient ftpClient = null;
	private String server;
	private int port;
	private String userName;
	private String userPassword;

	public FTPUtils(String server, int port, String userName, String userPassword) {
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/**
	 * <p>Title: 连接服务器</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月10日
	 * 
	 * @return 连接成功与否 true:成功， false:失败
	 */
	public boolean open() {
		// 判断是否已连接
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}

		try {
			ftpClient = new FTPClient();
			// 连接FTP服务器
			ftpClient.connect(this.server, this.port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftpClient.login(this.userName, this.userPassword);
			// 检测连接是否成功
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("FTP服务器拒绝连接.");
				this.close();
				System.exit(1);
			}
			System.out.println("FTP连接成功:" + this.server + ";port:" + this.port + ";name:" + this.userName + ";pwd:" + this.userPassword);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置上传模式.binally or ascii
			ftpClient.enterLocalPassiveMode();

			return true;
		} catch (Exception e) {
			this.close();
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * <p>Title: 切换到父目录</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @return 切换结果 true：成功， false：失败
	 */
	private boolean changeToParentDir() {
		try {
			return ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 改变当前目录到指定目录</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param dir 目的目录
	 * @return 切换结果 true：成功，false：失败
	 */
	private boolean cd(String dir) {
		try {
			return ftpClient.changeWorkingDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 获取目录下所有的文件名称</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param filePath 指定的目录
	 * @return 文件列表,或者null
	 */
	private FTPFile[] getFileList(String filePath) {
		try {
			return ftpClient.listFiles(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>Title: 层层切换工作目录</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param ftpPath 目的目录
	 * @return 切换结果
	 */
	public boolean changeDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "UTF-8"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				for (int i = 0; i < paths.length; i++) {
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "UTF-8"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param ftpPath 需要创建的目录
	 * @return
	 */
	public boolean mkDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			System.out.println("ftpPath:" + ftpPath);
			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录
				ftpClient.makeDirectory(new String(ftpPath.getBytes(), "UTF-8"));
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "UTF-8"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				for (int i = 0; i < paths.length; i++) {
					ftpClient.makeDirectory(new String(paths[i].getBytes(), "UTF-8"));
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "UTF-8"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 上传文件到FTP服务器</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param //localDirectoryAndFileName 本地文件目录和文件名
	 * @param ftpFileName 上传到服务器的文件名()
	 * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录(目录可以省略)
	 * @return
	 */
	//public boolean upload(String localDirectoryAndFileName, String ftpFileName, String ftpDirectory) {
	public boolean upload(File srcFile, String ftpFileName, String ftpDirectory) {

		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
			//File srcFile = new File(localDirectoryAndFileName);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(srcFile);
				// 创建目录
				this.mkDir(ftpDirectory);
				ftpClient.setBufferSize(100000);
				ftpClient.setControlEncoding("UTF-8");
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

				if (ftpFileName == null || ftpFileName == "") {
					ftpFileName = srcFile.getName();
				}

				// 上传
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), "UTF-8"), fis);
			} catch (Exception e) {
				this.close();
				e.printStackTrace();
				return false;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("上传文件成功，本地文件名： " +  "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
		return flag;
	}

	/**
	 * <p>Title: 从FTP服务器上下载文件</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param ftpDirectoryAndFileName ftp服务器文件路径，以/dir形式开始
	 * @param localDirectoryAndFileName 保存到本地的目录
	 * @return
	 */
	public boolean get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		ftpClient.enterLocalPassiveMode(); // Use passive mode as default
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpDirectoryAndFileName.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpDirectoryAndFileName = sbStr.toString();
			String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
			String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
			this.changeDir(filePath);
			ftpClient.retrieveFile(new String(fileName.getBytes(), "UTF-8"), new FileOutputStream(localDirectoryAndFileName)); // download
			// file
			System.out.println(ftpClient.getReplyString()); // check result
			System.out.println("从ftp服务器上下载文件：" + ftpDirectoryAndFileName + "， 保存到：" + localDirectoryAndFileName);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 返回FTP目录下的文件列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param pathName
	 * @return
	 */
	public String[] getFileNameList(String pathName) {
		try {
			return ftpClient.listNames(pathName);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>Title: 删除FTP上的文件</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param ftpDirAndFileName 路径开头不能加/，比如应该是test/filename1
	 * @return
	 */
	public boolean deleteFile(String ftpDirAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			return ftpClient.deleteFile(ftpDirAndFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 删除FTP目录</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月11日
	 * 
	 * @param ftpDirectory
	 * @return
	 */
	public boolean deleteDirectory(String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			return ftpClient.removeDirectory(ftpDirectory);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>Title: 关闭链接</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月10日
	 * 
	 */
	public void close() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
			System.out.println("成功关闭连接，服务器ip:" + this.server + ", 端口:" + this.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FTPUtils ftp = new FTPUtils("hellohao.cn", 21, "test", "test");
		boolean flag = ftp.open();
		if (flag) {
			// 上传
			//ftp.upload("D:/新建文件夹/1.jpg", "/admin/shihao.jpg", "");

			ftp.close();
		}
	}

}
