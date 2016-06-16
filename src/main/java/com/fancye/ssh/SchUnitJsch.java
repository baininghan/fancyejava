package com.fancye.ssh;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * java ssh登录linux以后的一些操作方式
 * 
 * @author liuxy
 * 
 */
public class SchUnitJsch {
	private final static Log logger = LogFactory.getLog(SchUnitJsch.class);

	public SchUnitJsch() {
		super();
	}

	public SchUnitJsch(String username, String password, String host) {
		// super(username, password, host);
	}

	/**
	 * 开启session
	 * 
	 * @return
	 * @throws JSchException
	 */
	private Session openSession() throws JSchException {
		JSch jsch = new JSch();
		Session session = null;
		session = jsch.getSession("username", "host");
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.setPassword("password");
		session.connect(3000);
		return session;
	}

	/**
	 * 上传本地文件到远程linux上 使用sftp上传
	 */
	public boolean uploadLocalFileToRemote(String localFile, String remoteDir) {
		Session session = null;
		try {
			session = openSession();
		} catch (JSchException e) {
			logger.error(e.getMessage());
			if (session != null) {
				session.disconnect();
				return false;
			}
		}
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			SftpProgressMonitorImpl sftpProgressMonitorImpl = new SftpProgressMonitorImpl();
			channel.put(localFile, remoteDir, sftpProgressMonitorImpl);

			return sftpProgressMonitorImpl.isSuccess();
		} catch (JSchException e) {
			if (channel != null) {
				channel.disconnect();
				session.disconnect();
			}
			return false;
		} catch (SftpException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 执行指令
	 * 
	 * @param commands
	 */
	public StringBuffer executeCommands(String commands) {
		return executeCmd(commands).getOutRes();
	}

	public ResInfo executeCmd(String commands) {
		ResInfo resInfo = new ResInfo();
		Session session = null;
		try {
			session = openSession();
		} catch (JSchException e) {
			logger.debug(e.getMessage());
			if (session != null)
				session.disconnect();
			return null;
		}
		ChannelExec channel = null;
		StringBuffer result = new StringBuffer();
		StringBuffer errResult = new StringBuffer();
		try {
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(commands);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(null);
			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();
			channel.connect();
			byte[] bytes = new byte[1024];
			byte[] bytesErr = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(bytes, 0, 1024);
					if (i > 0) {
						i = err.read(bytesErr, 0, 1024);
						if (i > 0 || err.available() > 0)
							continue;
						logger.debug("exit-status:" + channel.getExitStatus());
						resInfo.setExitStuts(channel.getExitStatus());
						resInfo.setOutRes(result);
						resInfo.setOutRes(errResult);
						break;
					}
					Thread.sleep(1000);
				}
				return resInfo;
			}
		} catch (JSchException e) {
			logger.error(e.getMessage());
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			channel.disconnect();
			session.disconnect();
		}
	}
	
	/**
	 * 执行shell指令并且返回结果对象ResInfo
	 * 
	 * @param commands
	 * @return
	 */
//	public ResInfo executeCmd(String commands){
//     ResInfo resInfo=new ResInfo();  
//     Session session=null;  
// try {  
// session = openSession();  
// } catch (JSchException e) {  
// logger.debug(e.getMessage());  
// if(session!=null) session.disconnect();  
// return null;  
// }  
// ChannelExec channel=null;  
// StringBuffer result=new StringBuffer();  
// StringBuffer errResult=new StringBuffer();  
// try {  
//     channel=(ChannelExec) session.openChannel("exec");  
//     channel.setCommand(commands);  
//     channel.setInputStream(null);  
//       ((ChannelExec)channel).setErrStream(null);  
//       InputStream in=channel.getInputStream();  
//       InputStream err=channel.getErrStream();  
//       channel.connect();  
//       byte[] bytes=new byte[1024];  
//       byte[] bytesErr=new byte[1024];  
//       while(true){  
//       while(in.available()>0){  
//       int i=in.read(bytes, 0, 1024);  
//       if(i > 0){  
//       int i=err.read(bytesErr, 0, 1024);  
//       if(i > 0||err.available() > 0) continue;  
//       logger.debug("exit-status:"+channel.getExitStatus());  
//       resInfo.setExitStuts(channel.getExitStatus());  
//       resInfo.setOutRes(result);  
//       resInfo.setErrRes(errResult);  
//               break;  
//       }  
//       Thread.sleep(1000);  
//       }  
//     return resInfo;  
// } catch (JSchException e) {  
// logger.error(e.getMessage());  
//     return null;  
// } catch (Exception e) {  
// logger.error(e.getMessage());  
// return null;  
// }finally{  
// channel.disconnect();  
// session.disconnect();  
// }

	/**
	 * 删除远程linux下的文件
	 */
	public boolean deleteRemoteFileorDir(String remoteFile) {
		Session session = null;
		try {
			session = openSession();
		} catch (JSchException e) {
			logger.info(e.getMessage());
			if (session != null)
				session.disconnect();
			return false;
		}
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			SftpATTRS sftpATTRS = channel.lstat(remoteFile);
			if (sftpATTRS.isDir()) {
				// 目录
				logger.debug("remote File:dir");
				channel.rmdir(remoteFile);
				return true;
			} else if (sftpATTRS.isReg()) {
				// 文件
				logger.debug("remote File:file");
				channel.rm(remoteFile);
				return true;
			} else {
				logger.debug("remote File:unkown");
				return false;
			}
		} catch (JSchException e) {
			if (channel != null) {
				channel.disconnect();
				session.disconnect();
			}
			return false;
		} catch (SftpException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 判断linux下 某文件是否存在
	 */
	public boolean detectedFileExist(String remoteFile) {
		Session session = null;
		try {
			session = openSession();
		} catch (JSchException e) {
			logger.info(e.getMessage());
			if (session != null)
				session.disconnect();
			return false;
		}
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			SftpATTRS sftpATTRS = channel.lstat(remoteFile);
			if (sftpATTRS.isDir() || sftpATTRS.isReg()) {
				// 目录 和文件
				logger.info("remote File:dir");
				return true;
			} else {
				logger.info("remote File:unkown");
				return false;
			}
		} catch (JSchException e) {
			if (channel != null) {
				channel.disconnect();
				session.disconnect();
			}
			return false;
		} catch (SftpException e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
