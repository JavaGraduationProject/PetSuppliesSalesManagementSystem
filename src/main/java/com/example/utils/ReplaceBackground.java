package com.example.utils;

import static com.example.utils.ImageBase64Converter.getImgBase64Str;

public class ReplaceBackground {

    //TODO 更换背景图片
    public static String replaceImg() {
        String path = System.getProperty("user.dir");
        String imgBase64Str = getImgBase64Str(path + "/src/main/resources/static/preImg/background.jpg");
        return "background-repeat:no-repeat ;background-size:100% 100%;background-attachment: fixed;background-image: url(data:image/png;base64," + imgBase64Str + ")";
    }

}
