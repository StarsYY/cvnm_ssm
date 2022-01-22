package cn.icylee.utils;

import cn.icylee.bean.Upload;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public static Map<String, Object> uploadImage(Upload upload, HttpServletRequest request, String table) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String serverName = "http://" + request.getServerName() + ":" + request.getServerPort();
        String diskPath = "E:\\IDEA\\ideaWeb";
        String imagePath = "/upload/image/" + table + "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
        String imageName = UUID.randomUUID().toString() + ".png";
        map.put("imagePath", serverName + imagePath + imageName);
        return base64StringToImage(upload.getBase64(), diskPath + imagePath, imageName) ? map : null;
    }

}
