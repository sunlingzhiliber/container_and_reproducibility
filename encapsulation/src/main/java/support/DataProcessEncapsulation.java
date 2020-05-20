package support;

import support.model.ModelEvent;
import support.process.DataProcessService;
import support.process.ProcessBehavior;
import support.utils.DataUtils;
import support.utils.PathUtils;

import java.io.File;
import java.nio.file.Path;

/**
 * @ClassName DataProcessEncapsulation
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/4/29
 * @Version 1.0.0
 */
public class DataProcessEncapsulation implements Instance  {

    public DataProcessService dataProcessService;

    public DataProcessEncapsulation(DataProcessService dataProcessService) {
        this.dataProcessService = dataProcessService;
    }

    @Override
    public void run(String id) {
        ProcessBehavior behavior = dataProcessService.getBehavior();
        Path workSpace= PathUtils.getWorkSpace(id);
        File rasterFile = workSpace.resolve("output.zip").toFile();
        for (ModelEvent input : behavior.getInputs()) {
            String dataServiceId = input.getDataServiceId();
                DataUtils.download(dataServiceId,rasterFile);
        }

        for (ModelEvent output : behavior.getOutputs()) {
            output.setDataServiceId(DataUtils.upload(rasterFile));
        }
    }
}
