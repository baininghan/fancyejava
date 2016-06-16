package com.fancye.rsa;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

// ����SHA-1 With RSA �ļ�ǩ�����Լ����ܽ�ǩ
public class RSA_Demo {
	
	RSA_Demo() {
		prepare();
		doSenderWork();
		doReceiverWork();
	}

	// share by sender and receiver
	Signature sign = null;// ǩ��
	// belong to sender,it visible to sender and receiver
	PublicKey publicKey = null;// ��Կ
	// belong to sender,it is only visible to sender
	PrivateKey privateKey;// ˽Կ

	private void prepare() {
		KeyPairGenerator keyGen = null;
		try {
			// ʵ����һ��RSA�㷨�Ĺ�Կ/˽Կ��������
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		int keysize = 1024;
		// ���ù�Կ/˽Կ�Եĳ���
		keyGen.initialize(keysize);
		// ����һ��RSA�㷨�Ĺ�Կ/˽Կ
		KeyPair keyPair = keyGen.generateKeyPair();
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
		try {
			// ʵ����һ����SHA�㷨����ɢ�У���RSA�㷨���м��ܵ�Signature.
			sign = Signature.getInstance("SHA1WithRSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// ��ǩ����˽Կ����
	void doSenderWork() {
		String words = "This is robin.How are you?";// �����ܵ���Ϣ
		Message msg = new Message(words.getBytes());
		try {
			// ���ü���ɢ�����õ�˽Կ
			sign.initSign(privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			// ����ɢ���㷨������
			sign.update(msg.getBody());
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		byte data[] = null;
		try {
			// ����ɢ�У��Բ�����ɢ������м��ܲ�����
			data = sign.sign();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		// �Ѽ��ܺ�ɢ�У���ǩ�����ӵ���Ϣ��
		msg.setSignature(data);
		// ������Ϣ
		sendMsg(msg);
	}

	Message sendingMsg;

	// ������Ϣ
	void sendMsg(Message sendMsg) {
		sendingMsg = sendMsg;
		System.out.println("sending Message");
	}

	void doReceiverWork() {
		// �յ���Ϣ
		Message msg = getReceivedMsg();
		try {
			// ���ý���ɢ�����õĹ�Կ��
			sign.initVerify(publicKey);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
		try {
			// ����ɢ���㷨������
			sign.update(msg.getBody());
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		try {
			/*
			 * ����ɢ�м��㣬�Ƚϼ�������ɢ�����Ƿ�ͽ��ܵ�ɢ�����Ƿ�һ�¡� һ������֤�ɹ�������ʧ��
			 */
			if (sign.verify(msg.getSignature())) {
				System.out.println("����ǩ����֤�ɹ���");
			} else {
				System.out.println("����ǩ����֤ʧ�ܣ�");
			}
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}

	// �յ���Ϣ
	Message getReceivedMsg() {
		System.out.println("receiving Message");
		return sendingMsg;
	}
	
	public static void main(String[] args) {
		new RSA_Demo();
	}
}

// ��Ϣ����
class Message {
	private byte[] body;// ��Ϣ
	private byte[] signature;// ɢ����

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
