package cn.edu.dgut.parking.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class ImageTransfer {
    public static String imgToBase64(String imgFile){
        return null;
    }
    public static String base64ToImg(String imgStr, String path){
        if (null == imgStr){
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] b = decoder.decode(imgStr);
            //处理数据
            for (int i = 0;i<b.length;++i){
                if(b[i]<0){
                    b[i]+=256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
