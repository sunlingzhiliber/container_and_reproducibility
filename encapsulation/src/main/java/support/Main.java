package support;

import cn.hutool.core.util.RuntimeUtil;

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

        String cmd = "Rscript.exe E:\\project\\container_and_reproducibility\\encapsulation\\Instance\\5ebe8af7c4c05c3759e3c012\\geodetector.R E:\\project\\container_and_reproducibility\\encapsulation\\Instance\\5ebe8af7c4c05c3759e3c012\\inputVariables.csv incidence soiltype,watershed,elevation";

        String[] strings = new String[1];

        Process process = RuntimeUtil.exec(strings, new File("E:\\project\\container_and_reproducibility\\encapsulation\\Instance\\5ebe8af7c4c05c3759e3c012"), cmd);
        RuntimeUtil.getResult(process);


    }
}
