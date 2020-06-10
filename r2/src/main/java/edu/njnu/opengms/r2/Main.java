package edu.njnu.opengms.r2;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName Main
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/14
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        List<File> fileList = FileUtil.loopFiles(new File("F:\\gitpage_blog\\sunlingzhiliber.github.io-master\\siteForTang\\photoWall\\img\\beauty"));
        int index = 0;
        for (File file : fileList) {
            String extension = FilenameUtils.getExtension(file.getName());
            file.renameTo(new File("F:\\gitpage_blog\\sunlingzhiliber.github.io-master\\siteForTang\\photoWall\\img\\beauty\\" + index + "." + extension));
            index++;
        }
    }
}
