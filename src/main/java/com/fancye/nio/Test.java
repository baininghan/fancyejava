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
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);// 构建一个缓冲区，用于存放数据
		String str = "write nio";
		buffer.put(str.getBytes());// 向缓冲区buffer放数据
		buffer.flip();// 这一步是必须的
		
		channel.write(buffer);// 将缓冲区的数据写入到管道Channel中
		
		channel.close();
		fos.close();
	}

}
