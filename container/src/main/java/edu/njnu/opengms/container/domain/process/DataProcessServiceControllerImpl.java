package edu.njnu.opengms.container.domain.process;

import com.google.common.collect.Lists;
import edu.njnu.opengms.common.controller.BaseController;
import edu.njnu.opengms.common.domain.container.process.DataProcessService;
import edu.njnu.opengms.common.dto.SplitPageDTO;
import edu.njnu.opengms.common.utils.JsonResult;
import edu.njnu.opengms.common.utils.ResultUtils;
import edu.njnu.opengms.container.domain.process.dto.AddDataProcessServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DataProcessServiceControllerImpl
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/4/29
 * @Version 1.0.0
 */
@RestController
@RequestMapping ("data_process_service")
public class DataProcessServiceControllerImpl implements BaseController<AddDataProcessServiceDTO, SplitPageDTO, DataProcessService> {

    @Autowired
    DataProcessServiceRepository dataProcessServiceRepository;

    @RequestMapping (value = "/listByIds", method = RequestMethod.GET)
    public JsonResult listByIds(@RequestParam ("ids") List<String> ids){
        List<DataProcessService> dataProcessServiceList= Lists.newArrayList( dataProcessServiceRepository.findAllById(ids));
        return ResultUtils.success(dataProcessServiceList);
    }

    @Override
    public JsonResult add(AddDataProcessServiceDTO a) {
        DataProcessService dataProcessService=new DataProcessService();
        a.convertTo(dataProcessService);
        return  ResultUtils.success(dataProcessServiceRepository.insert(dataProcessService));
    }

    @Override
    public JsonResult delete(String id) {
        dataProcessServiceRepository.deleteById(id);
        return ResultUtils.success("删除成功");
    }

    @Override
    public JsonResult list(SplitPageDTO findDTO) {
        if(findDTO.getValue()==null||findDTO.getValue().equals("")){
            return ResultUtils.success(dataProcessServiceRepository.findAll(findDTO.getPageable()));
        }
        return ResultUtils.success(dataProcessServiceRepository.getByNameContainsIgnoreCase(findDTO.getValue(),findDTO.getPageable()));
    }

    @Override
    public JsonResult count() {
        return ResultUtils.success(dataProcessServiceRepository.count());
    }

    @Override
    public JsonResult get(String id) {
        return ResultUtils.success(dataProcessServiceRepository.findById(id).orElseGet(null));
    }

    @Override
    public JsonResult update(String id, DataProcessService updateDTO) {
        return null;
    }
}
