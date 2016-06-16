package com.fancye.ssh.war2tomcat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.fancye.ssh.SchUnitJsch;


/**
 * 将war包部署到linux的tomcat下一些操作方法
 * @author liuxy
 *
 */
public class DetectedTomcatService {
	
	SchUnitJsch sshUnit = new SchUnitJsch();
	
	private final static Log logger=LogFactory.getLog(DetectedTomcatService.class);
    //tomcat所在的linux下的根目录
	public String baseTomcatHome;
	
	public DetectedTomcatService(String username, String pwd, String host) {
//		super(username, pwd, host);
	}
	/**
	 * 检测tomcat是否启动
	 */
	public  boolean isStartingTomcat(String command){
	  if(command==null)command="ps -ef | grep Tomcat | grep -v grep"; 
	  StringBuffer res=sshUnit.executeCommands(command);
	  logger.debug(res);
	  return !StringUtils.isEmpty(res);
	  
	}
	/**
	 * 关闭tomcat
	 */
	public  boolean  shutdownTomcat(String commandShutdownTomcat){
		
		String command="ps -ef | grep Tomcat | grep -v grep";
	    if(isStartingTomcat(command)){
	    	if(StringUtils.isEmpty(commandShutdownTomcat)){
	    	   	StringBuffer res=sshUnit.executeCommands(command);
		        String[] fragments=res.toString().split("\\s+");
		        for(String fragment:fragments){
		        	logger.debug(fragment);
		        	if(fragment.indexOf("catalina.base")>-1||fragment.indexOf("catalina.base")>-1)
		        	{
		        		baseTomcatHome=fragment.split("=")[1];
		        		break;
		        	}
		        	continue;
		        }
		         logger.info("baseTomcatHome:"+baseTomcatHome);
		         if(baseTomcatHome!=null) commandShutdownTomcat="sh  "+baseTomcatHome+"/bin/shutdown.sh";
			}
            //关闭tomcat
      		sshUnit.executeCommands(commandShutdownTomcat);
				//等待几秒钟
			   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				 logger.equals(e.getMessage());
			}
			   boolean  success=isStartingTomcat(command);
			    if(success){
			     	StringBuffer res=sshUnit.executeCommands(command);
			        String[] fragments=res.toString().split("\\s+");
			    	sshUnit.executeCommands("kill -9 "+fragments[1]);
			    }
	    	return true;
	    }
		return true;
	}
	/**
	 * 开启tomcat
	 */
	public void startupTomcat(String commandStartupTomcat){
		String command="ps -ef | grep Tomcat | grep -v grep";
	    if(isStartingTomcat(command)){
	    	shutdownTomcat(null);
	    }
	    //开启tomcat
	    if(StringUtils.isEmpty(commandStartupTomcat))
	    	commandStartupTomcat="sh "+baseTomcatHome+"/bin/startup.sh";
	    sshUnit.executeCommands(commandStartupTomcat);
	}
	/**
	 * 删除war包
	 * @return
	 */
	public boolean deleteWar(){
		String regex=baseTomcatHome+"/webapps/ROOT*";
		String deleteCommand="rm -rf "+ regex;
		String  detectFileCommand="ls "+baseTomcatHome+"/webapps | grep ROOT";
		String commands=deleteCommand+" ; "+detectFileCommand;
		StringBuffer res=sshUnit.executeCommands(commands);
		return StringUtils.isEmpty(res);
	}
	/**
	 * 上传war包
	 * @param baseTomcatHome
	 */
	public boolean uploadWar(String localFile){
		String remoteDir=baseTomcatHome+"/webapps";
		sshUnit.uploadLocalFileToRemote(localFile,remoteDir);
		return sshUnit.uploadLocalFileToRemote(localFile,remoteDir);
	}
	
	public boolean warDealwith(){
	   String commands="cd "+baseTomcatHome+"/webapps;mv ROOT.war ROOT.zip;unzip ROOT.zip -d ROOT;rm -rf ROOT.zip";
	   StringBuffer res=sshUnit.executeCommands(commands);
	   logger.info(res);
	   return !StringUtils.isEmpty(res);
	}
	
	
	public void setBaseTomcatHome(String baseTomcatHome) {
		this.baseTomcatHome = baseTomcatHome;
	}
	

}
