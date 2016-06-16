package com.fancye.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * 上传镜像映射检测
 * 
 * @author Fancye
 * 
 */
public class SftpProgressMonitorImpl implements SftpProgressMonitor {

	private static Log logger = LogFactory.getLog(SftpProgressMonitorImpl.class);

	private long size;
	private long currentSize = 0;
	private boolean endFlag = false;

	@Override
	public void init(int op, String srcFile, String dstDir, long size) {
		logger.debug("文件开始上传：" + srcFile + "【" + dstDir + "】" + "文件大小：" + size + "参数：" + op);
		this.size = size;
	}

	@Override
	public void end() {
		logger.debug("文件上传结束");
		endFlag = true;
	}

	@Override
	public boolean count(long count) {
		currentSize += count;
		logger.debug("上传数量" + currentSize);
		return true;
	}

	public boolean isSuccess() {
		return endFlag && currentSize == size;
	}
}
