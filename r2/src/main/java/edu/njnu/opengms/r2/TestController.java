package edu.njnu.opengms.r2;

import edu.njnu.opengms.common.utils.JsonResult;
import edu.njnu.opengms.common.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/15
 * @Version 1.0.0
 */
@RequestMapping ("/test")
@RestController
public class TestController {


    @RequestMapping (value = "test1", method = RequestMethod.POST)
    public JsonResult test1() {
        return ResultUtils.success(Math.random());
    }

}

