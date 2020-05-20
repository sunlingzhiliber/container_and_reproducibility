package edu.njnu.opengms.container;

import org.apache.commons.io.FilenameUtils;

/**
 * @ClassName Main
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/9
 * @Version 1.0.0
 */
public class Main
{
    public static void main(String[] args) {

        String s = "12312.txt";
        String extension = FilenameUtils.getExtension(s);
        String prefix = FilenameUtils.getPrefix(s);
    }
}
