package com.fancye.ssh.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fancye.ssh.war2tomcat.DetectedTomcatService;

/**
 * 将演示主程序
 * 
 * @author liuxy
 * 
 */
public class TomcatPublish {

	private static final Log logger = LogFactory.getLog(TomcatPublish.class);

	private DetectedTomcatService tomcatService;
	private String baseTomcatHome = "/opt/server/Tomcat";// 设置默认tomcat根目录
	private String localFileWarPath;// 本地war包路径

	public TomcatPublish(String username, String pwd, String host) {
		tomcatService = new DetectedTomcatService(username, pwd, host);
		tomcatService.setBaseTomcatHome(baseTomcatHome);
	}

	public void publish() {
		// 关闭tomcat
		boolean success = tomcatService.shutdownTomcat(null);

		if (!success) {
			logger.debug("Tomcat shutdown failed");
			return;
		}
		// 删除原有文件
		success = tomcatService.deleteWar();
		if (!success) {
			logger.debug("ROOT 残留war包已经文件没有删除干净");
			return;
		}
		// 上传文件
		if (tomcatService.uploadWar(localFileWarPath)) {
			// 解压
			logger.debug("=============================================解压缩====================");
			if (tomcatService.warDealwith()) {
				logger.debug("........start Tomcat......");
				tomcatService.startupTomcat(null);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
				System.exit(0);
			}

		} else
			logger.debug("上传失败war");

	}

	public DetectedTomcatService getTomcatService() {
		return tomcatService;
	}

	public void setTomcatService(DetectedTomcatService tomcatService) {
		this.tomcatService = tomcatService;
	}

	public String getBaseTomcatHome() {
		return baseTomcatHome;
	}

	public void setBaseTomcatHome(String baseTomcatHome) {
		this.baseTomcatHome = baseTomcatHome;
	}

	public String getLocalFileWarPath() {
		return localFileWarPath;
	}

	public void setLocalFileWarPath(String localFileWarPath) {
		this.localFileWarPath = localFileWarPath;
	}

	public static void main(String[] args) {
		TomcatPublish tomcatPublish = new TomcatPublish("登录用户名(如root)", "密码", "主机域名（如:192.168.0.123）");
		tomcatPublish.setLocalFileWarPath("E:\\WarFiles\\ROOT.war");
		tomcatPublish.publish();
	}
}
