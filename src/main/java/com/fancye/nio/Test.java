/**
 * 
 */
package com.fancye.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Fancye
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("data.txt");
		FileOutputStream fos = new FileOutputStream(file);
		FileChannel channel = fos.getChannel();
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);// ����һ�������������ڴ������
		String str = "write nio";
		buffer.put(str.getBytes());// �򻺳���buffer������
		buffer.flip();// ��һ���Ǳ����
		
		channel.write(buffer);// ��������������д�뵽�ܵ�Channel��
		
		channel.close();
		fos.close();
	}

}
