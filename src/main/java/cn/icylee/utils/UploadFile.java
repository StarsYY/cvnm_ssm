package cn.icylee.utils;

import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadFile {

    public static boolean base64StringToImage(String base64, String dir, String imageName) {
        if (base64 == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            base64 = base64.substring(base64.indexOf(",", 1) + 1, base64.length());
            byte[] b = decoder.decodeBuffer(base64);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            boolean isEmpty = new File(dir).mkdirs();
            OutputStream out = new FileOutputStream(dir + imageName);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
