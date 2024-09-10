package com.example.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ImageBase64Converter {

    public static String getImgBase64Str(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }

    //TODO 获取文件
    public static String saveFile(MultipartFile file) {
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String path;
        String fileName;
        try {
            //获取项目路径（project）
             path = System.getProperty("user.dir");
            //获取文件的输入流
            inputStream = file.getInputStream();
            //获取上传时的文件名
            fileName = file.getOriginalFilename();
//            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            //注意是路径+文件名
            File targetFile = new File(path + "/wedding" + "/src" + "/main" + "/resources" + "/static" + "/scene/" + fileName);
            //如果之前的 String path = "d:/upload/" 没有在最后加 / ，那就要在 path 后面 + "/"
            //判断文件父目录是否存在
            if (!targetFile.getParentFile().exists()) {
                //不存在就创建一个
                targetFile.getParentFile().mkdir();
            }
            //获取文件的输出流
            outputStream = new FileOutputStream(targetFile);
            //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (
                IOException e) {
            //出现异常，则告诉页面失败
            return "";
        } finally {
            //无论成功与否，都有关闭输入输出流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path + "/wedding" + "/src" + "/main" + "/resources" + "/static" + "/scene/" + fileName;
    }


    /**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param imgPath
     */
//    public static String convertFileToBase64(String imgPath) {
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            InputStream in = new FileInputStream(imgPath);
//            System.out.println("文件大小（字节）="+in.available());
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 对字节数组进行Base64编码，得到Base64编码的字符串
//        BASE64Encoder encoder = new BASE64Encoder();
//        String base64Str = encoder.encode(data);
//        return base64Str;
//    }
//
//    /**
//     * 将base64字符串，生成文件
//     */
//    public static File convertBase64ToFile(String fileBase64String, String filePath, String fileName) {
//
//        BufferedOutputStream bos = null;
//        FileOutputStream fos = null;
//        File file = null;
//        try {
//            File dir = new File(filePath);
//            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
//                dir.mkdirs();
//            }
//
//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] bfile = decoder.decodeBuffer(fileBase64String);
//
//            file = new File(filePath + File.separator + fileName);
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            bos.write(bfile);
//            return file;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (bos != null) {
//                try {
//                    bos.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//    }

}
