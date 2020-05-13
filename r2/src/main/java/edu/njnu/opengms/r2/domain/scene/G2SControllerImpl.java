package edu.njnu.opengms.r2.domain.scene;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.opengms.common.controller.BaseController;
import edu.njnu.opengms.common.dto.SplitPageDTO;
import edu.njnu.opengms.common.exception.MyException;
import edu.njnu.opengms.common.utils.CopyUtils;
import edu.njnu.opengms.common.utils.JsonResult;
import edu.njnu.opengms.common.utils.ResultUtils;
import edu.njnu.opengms.r2.domain.scene.dto.AddG2SDTO;
import edu.njnu.opengms.r2.domain.scene.dto.UpdateG2SDTO;
import edu.njnu.opengms.r2.domain.scene.nodes.Computation;
import edu.njnu.opengms.r2.domain.scene.nodes.Evaluation;
import edu.njnu.opengms.r2.domain.scene.vo.G2SListVO;
import edu.njnu.opengms.r2.domain.scene.vo.G2SVO;
import edu.njnu.opengms.r2.remote.ContainerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName G2SControllerImpl
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/4/30
 * @Version 1.0.0
 */
@RestController
@RequestMapping ("/g2s")
public class G2SControllerImpl implements BaseController<AddG2SDTO, SplitPageDTO, UpdateG2SDTO> {

    @Autowired
    G2SRepository g2SRepository;

    @Autowired
    ContainerFeign containerFeign;


    @RequestMapping (value = "/{id}/evaluationServices", method = RequestMethod.GET)
    public JsonResult evaluationServices(@PathVariable String id) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        if(geographicSimulationScene.getEvaluation()==null){
            return ResultUtils.success(null);
        }
        List<String> evaluationServices = geographicSimulationScene.getEvaluation().getEvaluationServices();
        return ResultUtils.success(containerFeign.listEvaluationServices(evaluationServices).getData());
    }

    @RequestMapping (value = "/{id}/evaluationService/{evaluationId}", method = RequestMethod.DELETE)
    public JsonResult evaluationServices(@PathVariable String id,@PathVariable String evaluationId) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        if(geographicSimulationScene.getEvaluation()==null){
            return ResultUtils.success(null);
        }
        List<String> evaluationServices = geographicSimulationScene.getEvaluation().getEvaluationServices();
        evaluationServices.remove(evaluationId);
        geographicSimulationScene.getEvaluation().setEvaluationServices(evaluationServices);
        return ResultUtils.success(g2SRepository.save(geographicSimulationScene));
    }


    @RequestMapping (value = "/{id}/dataServices", method = RequestMethod.GET)
    public JsonResult dataServices(@PathVariable String id) {
        JSONObject obj = new JSONObject();
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        if(geographicSimulationScene.getResourceCollect()==null){
            return ResultUtils.success(null);
        }
        List<String> dataServices = geographicSimulationScene.getResourceCollect().getDataServices();
        obj.put("originalDataServices", containerFeign.listDataServiceByIds(dataServices).getData());

        Map<String, List<String>> intermediateDataServices = geographicSimulationScene.getResourceCollect().getIntermediateDataServices();

        if (intermediateDataServices != null) {
            dataServices.clear();
            for (Map.Entry<String, List<String>> stringStringEntry : intermediateDataServices.entrySet()) {
                //TODO 后面这个字段要展示到前端，以显示中间数据由哪一个服务实例生产
                String serviceInstanceId = stringStringEntry.getKey();
                List<String> value = stringStringEntry.getValue();
                value.forEach(el-> dataServices.add(el));
            }
            obj.put("intermediateDataServices", containerFeign.listDataServiceByIds(dataServices).getData());
        } else {
            obj.put("intermediateDataServices", null);
        }
        return ResultUtils.success(obj);
    }

    @RequestMapping (value = "/{id}/computeServices", method = RequestMethod.GET)
    public JsonResult computeServices(@PathVariable String id) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        List<String> dataProcessServices = geographicSimulationScene.getResourceCollect().getDataProcessServices();
        List<String> modelServices = geographicSimulationScene.getResourceCollect().getModelServices();
        JSONObject obj = new JSONObject();
        obj.put("dataProcessServices", containerFeign.listDataProcessServiceByIds(dataProcessServices).getData());
        obj.put("modelServices", containerFeign.listModelServiceByIds(modelServices).getData());
        return ResultUtils.success(obj);
    }

    @RequestMapping (value = "/{id}/evaluationService", method = RequestMethod.POST)
    public JsonResult evaluationService(@PathVariable String id,@RequestBody JSONObject obj) {
        String evaluationServiceId = (String) ((LinkedHashMap) containerFeign.createEvaluationService(obj).getData()).get("id");
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        Evaluation evaluation = geographicSimulationScene.getEvaluation();
        if(evaluation==null) {
          evaluation=new Evaluation();
        }
        geographicSimulationScene.setEvaluation(evaluation);
        List<String> evaluationServices = evaluation.getEvaluationServices();
        if(evaluationServices==null){
            evaluationServices=new ArrayList<>();
        }
        evaluationServices.add(evaluationServiceId);
        geographicSimulationScene.getEvaluation().setEvaluationServices(evaluationServices);
        g2SRepository.save(geographicSimulationScene);
        return ResultUtils.success(evaluationServiceId);
    }


    @RequestMapping (value = "/{id}/bind/{instanceId}", method = RequestMethod.POST)
    public JsonResult bind(@PathVariable String id, @PathVariable String instanceId,@RequestParam("instanceEnum")String instanceEnum ) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        if("MODEL".equals(instanceEnum)){
            Set<String> modelExpectedInstances = geographicSimulationScene.getComputation().getModelExpectedInstances();
            if(modelExpectedInstances==null){
                modelExpectedInstances=new HashSet<>();
            }
            if(modelExpectedInstances.contains(instanceId)){
                return ResultUtils.success("该实例已绑定");
            }
            modelExpectedInstances.add(instanceId);
            geographicSimulationScene.getComputation().setModelExpectedInstances(modelExpectedInstances);
        }else {
            Set<String> processExpectedInstances = geographicSimulationScene.getComputation().getProcessExpectedInstances();
            if(processExpectedInstances==null){
                processExpectedInstances=new HashSet<>();
            }
            if(processExpectedInstances.contains(instanceId)){
                return ResultUtils.success("该实例已绑定");
            }
            processExpectedInstances.add(instanceId);
            geographicSimulationScene.getComputation().setProcessExpectedInstances(processExpectedInstances);
        }
        Map<String, List<String>> intermediateDataServices = geographicSimulationScene.getResourceCollect().getIntermediateDataServices();
        if(intermediateDataServices==null){
            intermediateDataServices=new HashMap<>();
        }
        Object data = containerFeign.getInstanceOutputs(instanceId).getData();
        intermediateDataServices.put(instanceId, (List<String>) data);
        geographicSimulationScene.getResourceCollect().setIntermediateDataServices(intermediateDataServices);
        return ResultUtils.success(g2SRepository.save(geographicSimulationScene));
    }




    @RequestMapping (value = "/{id}/invoke", method = RequestMethod.POST)
    public JsonResult invokeService(@PathVariable String id, @RequestBody JSONObject serviceInstance) {
        String serviceId = serviceInstance.getJSONObject("service").getStr("id");
        String instanceId = (String) ((LinkedHashMap) containerFeign.addInstance(serviceInstance).getData()).get("id");
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        Computation computation = geographicSimulationScene.getComputation();
        if(computation==null){
            computation=new Computation();
        }
        if (serviceInstance.get("instanceEnum").equals("MODEL")) {
            Map<String, List<String>> modelInstances = computation.getModelInstances();
            modelInstances = getStringListMap(serviceId, instanceId, modelInstances);
            computation.setModelInstances(modelInstances);
        } else {
            Map<String, List<String>> processInstances = computation.getProcessInstances();
            processInstances = getStringListMap(serviceId, instanceId, processInstances);
            computation.setProcessInstances(processInstances);
        }
        geographicSimulationScene.setComputation(computation);
        geographicSimulationScene.setUpdateTime(new Date());
        g2SRepository.save(geographicSimulationScene);
        return ResultUtils.success(containerFeign.invoke(instanceId).getData());
    }


    @RequestMapping (value = "/{id}/getInstances/{type}/{serviceId}", method = RequestMethod.GET)
    public JsonResult getInstances(@PathVariable String id,  @PathVariable String type,@PathVariable String serviceId) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        Map<String, List<String>> instances=new HashMap<>();
        if(type.equals("MODEL")){
            instances= geographicSimulationScene.getComputation().getModelInstances();
        }else if(type.equals("PROCESS")){
            instances= geographicSimulationScene.getComputation().getProcessInstances();
        }
        if(instances==null){
            return ResultUtils.success(null);
        }

        return ResultUtils.success(containerFeign.listInstanceByIds(instances.get(serviceId)).getData());
    }



    @RequestMapping (value = "/{id}/getExpectedInstances", method = RequestMethod.GET)
    public JsonResult getExpectedInstances(@PathVariable String id) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        Computation computation = geographicSimulationScene.getComputation();
        if(computation==null){
            return ResultUtils.success(null);
        }
        Set<String> modelExpectedInstances = computation.getModelExpectedInstances();
        Set<String> processExpectedInstances = computation.getProcessExpectedInstances();
        List<String> all=new ArrayList<>(modelExpectedInstances);
        all.addAll(processExpectedInstances);
        return ResultUtils.success(containerFeign.listInstanceByIds(all).getData());
    }


    @RequestMapping (value = "/{id}/view", method = RequestMethod.GET)
    public JsonResult getG2SVO(@PathVariable String id) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElse(null);
        G2SVO g2SVO=new G2SVO();
        CopyUtils.copyProperties(geographicSimulationScene,g2SVO);

        JSONObject jsonObject=new JSONObject();

        List<String> ids = geographicSimulationScene.getResourceCollect().getDataProcessServices();
        jsonObject.put("dataProcessServices",containerFeign.listDataProcessServiceByIds(ids).getData());
        ids = geographicSimulationScene.getResourceCollect().getModelServices();
        jsonObject.put("modelServices",containerFeign.listModelServiceByIds(ids).getData());
        ids=geographicSimulationScene.getResourceCollect().getDataServices();
        jsonObject.put("dataServices",containerFeign.listDataServiceByIds(ids).getData());
        g2SVO.setResourceCollect(jsonObject);
        jsonObject=new JSONObject();

        jsonObject.put("xmlGraph",geographicSimulationScene.getSimulationConceptGraph().getXmlGraph());
        g2SVO.setSimulationConceptGraph(jsonObject);
        jsonObject=new JSONObject();

        ids=new ArrayList<>(geographicSimulationScene.getComputation().getProcessExpectedInstances());
        ids.addAll(geographicSimulationScene.getComputation().getModelExpectedInstances());
        jsonObject.put("serviceInstances",containerFeign.listInstanceByIds(ids).getData());
        g2SVO.setComputation(jsonObject);

        ids=geographicSimulationScene.getEvaluation().getEvaluationServices();
        JSONArray jsonArray=new JSONArray();
        jsonArray.addAll((Collection<? extends Object>) containerFeign.listEvaluationServices(ids).getData());
        g2SVO.setEvaluation(jsonArray );

        return ResultUtils.success(g2SVO);
    }


    @Override
    public JsonResult add(AddG2SDTO a) {
        GeographicSimulationScene geographicSimulationScene = new GeographicSimulationScene();
        a.convertTo(geographicSimulationScene);
        return ResultUtils.success(g2SRepository.insert(geographicSimulationScene));
    }

    @Override
    public JsonResult delete(String id) {
        g2SRepository.deleteById(id);
        return ResultUtils.success("删除成功");
    }

    @Override
    public JsonResult list(SplitPageDTO findDTO) {
        Page<GeographicSimulationScene> all = g2SRepository.findAll(findDTO.getPageable());
        return ResultUtils.success( all.map(g2s->{
            G2SListVO vo=new G2SListVO();
            CopyUtils.copyProperties(g2s,vo);
            return vo;
        }));
    }

    @Override
    public JsonResult count() {
        return ResultUtils.success(g2SRepository.count());
    }

    @Override
    public JsonResult get(String id) {
        return ResultUtils.success(g2SRepository.findById(id).orElse(null));
    }

    @Override
    public JsonResult update(String id, UpdateG2SDTO updateDTO) {
        GeographicSimulationScene geographicSimulationScene = g2SRepository.findById(id).orElseThrow(MyException::noObject);
        updateDTO.updateTo(geographicSimulationScene);
        return ResultUtils.success(g2SRepository.save(geographicSimulationScene));
    }


    private Map<String, List<String>> getStringListMap(String serviceId, String instanceId, Map<String, List<String>> instances) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        List<String> list = instances.get(serviceId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(instanceId);
        instances.put(serviceId,list);
        return instances;
    }

}
