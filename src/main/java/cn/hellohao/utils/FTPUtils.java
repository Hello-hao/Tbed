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
 * @author Hellohao
 * @date 2019年11月10日
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

	public boolean open() {
		// 判断是否已连接
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}
		try {
			ftpClient = new FTPClient();
			// 连接FTP服务器
			ftpClient.connect(this.server, this.port);
			ftpClient.login(this.userName, this.userPassword);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				Print.warning("FTP服务器拒绝连接.");
				this.close();
				System.exit(1);
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
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
	 * @author Hellohao
	 * @date 2018年10月11日
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
	 * 
	 * @param ftpPath 目的目录
	 * @return 切换结果
	 */
	public boolean changeDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
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
	 * 
	 * @param //localDirectoryAndFileName 本地文件目录和文件名
	 * @param ftpFileName 上传到服务器的文件名()
	 * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录(目录可以省略)
	 * @return
	 */
	public boolean upload(File srcFile, String ftpFileName, String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
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
		return flag;
	}

	/**
	 * @param ftpDirectoryAndFileName ftp服务器文件路径，以/dir形式开始
	 * @param localDirectoryAndFileName 保存到本地的目录
	 * @return
	 */
	public boolean get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		ftpClient.enterLocalPassiveMode();
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
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
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
	 */
	public void close() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
