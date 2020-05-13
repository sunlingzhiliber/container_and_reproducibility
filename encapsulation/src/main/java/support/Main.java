package support;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @ClassName Main
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/9
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        File file=new File("D:\\EyeFoo3\\tmall.ico");
        String name = FileUtil.getName(file);
        System.out.println(name);
    }
}
