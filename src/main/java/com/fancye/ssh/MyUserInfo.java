package com.fancye.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive {

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean promptYesNo(String str) {
		return false;
	}

	@Override
	public String getPassphrase() {
		return null;
	}

	@Override
	public boolean promptPassphrase(String message) {
		return false;
	}

	@Override
	public boolean promptPassword(String message) {
		return false;
	}

	@Override
	public void showMessage(String message) {
	}

	@Override
	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {
		return null;
	}

}
