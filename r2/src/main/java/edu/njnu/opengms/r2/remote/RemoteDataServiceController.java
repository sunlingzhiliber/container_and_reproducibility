package edu.njnu.opengms.r2.remote;

import edu.njnu.opengms.common.utils.JsonResult;
import edu.njnu.opengms.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName RemoteDataServiceController
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/13
 * @Version 1.0.0
 */
@RestController
@RequestMapping ("/data_service")
public class RemoteDataServiceController {
    @Autowired
    ContainerFeign containerFeign;
    @RequestMapping (value = "/addByFile", method = RequestMethod.POST)
    public JsonResult addDataService(@RequestParam ("file") MultipartFile file) {
        return ResultUtils.success(containerFeign.addDataServiceByFile(file).getData());
    }
}
