/**
 * 
 */
package com.fancye.identicon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * @author Fancye
 *
 */
public class IconMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ident = "";
		if(args.length == 0 || null == args[0]){
			System.out.println("Default icon generator");
			ident = "Fancye";
		}else{
			ident = args[0];
		}
        Identicon identicon = new Identicon();

        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(ident, Charsets.UTF_8);
        String md5 = hasher.hash().toString();

        BufferedImage image = identicon.create(md5, 200);

        try {
			ImageIO.write(image, "PNG", new File(new Date().getTime() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
