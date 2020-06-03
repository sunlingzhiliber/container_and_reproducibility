//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package support;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.ZipUtil;
import support.model.ModelBehavior;
import support.model.ModelEvent;
import support.model.ModelService;
import support.model.Parameter;
import support.utils.DataUtils;
import support.utils.PathUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ModelEncapsulation implements Instance {

   public   ModelService modelService;

    public ModelEncapsulation(ModelService modelService) {
        this.modelService = modelService;
    }



    @Override
    public void run(String instanceId) {
        ModelBehavior behavior = modelService.getBehavior();
        Path assemblySpace=PathUtils.getAssembly();
        Path workSpace = PathUtils.getWorkSpace(instanceId);
        FileUtil.copyContent(assemblySpace.toFile(), workSpace.toFile(), true);
        Path fileInputPath = workSpace.resolve("spatialData.zip");
        File fileInput = fileInputPath.toFile();
        List<ModelEvent> inputs = behavior.getInputs();
        for (ModelEvent input : inputs) {
            DataUtils.download(input.getDataServiceId(), fileInput);
        }
        Path shapeFileDirPath = workSpace.resolve("shapefileDir");
        File dirZip = shapeFileDirPath.toFile();
        ZipUtil.unzip(fileInput, dirZip);
        List<File> fileList = FileUtil.loopFiles(dirZip, (file) -> file.getName().endsWith(".shp"));
        File shapfile = fileList.get(0);

        String feild = null, pValue = null, permutations = null, cpu_count = null;
        List<Parameter> parameters = behavior.getParameters();
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            String value = parameter.getValue();
            if (name.equals("feild")) {
                feild = value;
            } else if (name.equals("pValue")) {
                pValue = value;
            } else if (name.equals("permutations")) {
                permutations = value;
            } else {
                cpu_count = value;
            }
        }

        String cmd;
        //注意正常来说，R的环境变量应该动态获取，但是目前在这里写死了
        cmd = "D:\\R-4.0.0\\bin\\Rscript mcAndNeighborMap.R " + shapfile.getAbsolutePath() + " " + feild;
        Process process = RuntimeUtil.exec(null, workSpace.toFile(), cmd);
        RuntimeUtil.getResult(process);

        cmd = "python localMoran.py " + shapfile.getAbsolutePath() + " " + feild + " " + permutations + " " + pValue + " " + cpu_count;
        process = RuntimeUtil.exec(null, workSpace.toFile(), cmd);
        RuntimeUtil.getResult(process);

        List<ModelEvent> outputs = behavior.getOutputs();
        for (ModelEvent output : outputs) {
            if (output.getName().equals("clusterMap")) {
                List<File> localMoran = FileUtil.loopFiles(workSpace.resolve("output").toFile(), (file) -> file.getName().startsWith("localMoran"));
                output.setDataServiceId(DataUtils.upload(localMoran.get(0)));
            } else if (output.getName().equals("moranCoefficient")) {
                List<File> mc = FileUtil.loopFiles(workSpace.resolve("output").toFile(), (file) -> file.getName().startsWith("mc"));
                output.setDataServiceId(DataUtils.upload(mc.get(0)));
            } else {
                List<File> neighborsMap = FileUtil.loopFiles(workSpace.resolve("output").toFile(), (file) -> file.getName().startsWith("neighborsMap"));
                output.setDataServiceId(DataUtils.upload(neighborsMap.get(0)));
            }
        }
    }
}
