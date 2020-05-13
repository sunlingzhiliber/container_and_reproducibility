//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package support;


import support.model.ModelBehavior;
import support.model.ModelEvent;
import support.model.ModelService;
import support.utils.DataUtils;
import support.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ModelEncapsulation implements Instance {

   public   ModelService modelService;

    public ModelEncapsulation(ModelService modelService) {
        this.modelService = modelService;
    }


    // y=ax2+bx+c id:5ea94dcadb146b21a7b6017f
    @Override
    public void run(String instanceId) throws IOException, InterruptedException {
        ModelBehavior behavior = modelService.getBehavior();
        Path assemblySpace=PathUtils.getAssembly();

        List<ModelEvent> outputs = behavior.getOutputs();
        Thread.sleep(4000);

        for (ModelEvent output : outputs) {
            if(output.getName().equals("sampling_cells_count")){
                output.setDataServiceId(DataUtils.upload(assemblySpace.resolve("ANNSampling-SamplingCellsCount.xml").toFile()));
            }else if(output.getName().equals("trainNetwork_error")){
                output.setDataServiceId(DataUtils.upload(assemblySpace.resolve("TrainNetwork-TrainNetwork_Error.xml").toFile()));
            }else if(output.getName().equals("simulation_step_image")){
                output.setDataServiceId(DataUtils.upload(assemblySpace.resolve("ann_iterate_100.tif").toFile()));
            }else if(output.getName().equals("simulation_final_image")){
                output.setDataServiceId(DataUtils.upload(assemblySpace.resolve("simulation_final.tif").toFile()));
            }else if(output.getName().equals("simulation_result")){
                output.setDataServiceId(DataUtils.upload(assemblySpace.resolve("ExecuteSimulation-SimulationResult .xml").toFile()));
            }

        }
    }
}
