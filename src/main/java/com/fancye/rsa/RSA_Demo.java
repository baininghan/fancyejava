package com.fancye.rsa;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

// 基于SHA-1 With RSA 的加签加密以及解密解签
public class RSA_Demo {
	
	RSA_Demo() {
		prepare();
		doSenderWork();
		doReceiverWork();
	}

	// share by sender and receiver
	Signature sign = null;// 签名
	// belong to sender,it visible to sender and receiver
	PublicKey publicKey = null;// 公钥
	// belong to sender,it is only visible to sender
	PrivateKey privateKey;// 私钥

	private void prepare() {
		KeyPairGenerator keyGen = null;
		try {
			// 实例化一个RSA算法的公钥/私钥对生成器
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		int keysize = 1024;
		// 设置公钥/私钥对的长度
		keyGen.initialize(keysize);
		// 生成一个RSA算法的公钥/私钥
		KeyPair keyPair = keyGen.generateKeyPair();
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
		try {
			// 实例化一个用SHA算法进行散列，用RSA算法进行加密的Signature.
			sign = Signature.getInstance("SHA1WithRSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// 加签，用私钥加密
	void doSenderWork() {
		String words = "This is robin.How are you?";// 待加密的消息
		Message msg = new Message(words.getBytes());
		try {
			// 设置加密散列码用的私钥
			sign.initSign(privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			// 设置散列算法的输入
			sign.update(msg.getBody());
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		byte data[] = null;
		try {
			// 进行散列，对产生的散列码进行加密并返回
			data = sign.sign();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		// 把加密后散列（即签名）加到消息中
		msg.setSignature(data);
		// 发送消息
		sendMsg(msg);
	}

	Message sendingMsg;

	// 发送消息
	void sendMsg(Message sendMsg) {
		sendingMsg = sendMsg;
		System.out.println("sending Message");
	}

	void doReceiverWork() {
		// 收到消息
		Message msg = getReceivedMsg();
		try {
			// 设置解密散列码用的公钥。
			sign.initVerify(publicKey);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
		try {
			// 设置散列算法的输入
			sign.update(msg.getBody());
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		try {
			/*
			 * 进行散列计算，比较计算所得散列码是否和解密的散列码是否一致。 一致则验证成功，否则失败
			 */
			if (sign.verify(msg.getSignature())) {
				System.out.println("数字签名验证成功！");
			} else {
				System.out.println("数字签名验证失败！");
			}
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}

	// 收到消息
	Message getReceivedMsg() {
		System.out.println("receiving Message");
		return sendingMsg;
	}
	
	public static void main(String[] args) {
		new RSA_Demo();
	}
}

// 消息对象
class Message {
	private byte[] body;// 消息
	private byte[] signature;// 散列码

	Message(byte data[]) {
		body = data;
	}

	byte[] getBody() {
		return body;
	}

	byte[] getSignature() {
		return signature;
	}

	void setSignature(byte data[]) {
		signature = data;
	}
	
}
