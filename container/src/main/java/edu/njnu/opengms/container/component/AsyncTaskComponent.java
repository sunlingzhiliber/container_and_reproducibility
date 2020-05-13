package edu.njnu.opengms.container.component;

import cn.hutool.core.util.RuntimeUtil;
import edu.njnu.opengms.common.domain.container.instance.InstanceEnum;
import edu.njnu.opengms.common.domain.container.instance.ServiceInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * @ClassName AsyncTaskComponent
 * @Description 使用了@Async注解，就会生成子线程，实现异步
 * @Author sun_liber
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Component
public class AsyncTaskComponent {

    @Value ("${web.upload-path}")
    String storePath;


    /**
     * 异步任务，在主线程中调用异步任务，需要返回值的话，
     * 需要主线程去一直请求Future.isDone()，判断子线程任务是否完成，然后利用Future.get()获取返回值。
     * 如果不需要返回值的话，主线程会直接结束。
     * @return
     */
    @Async
    public Future<String> executeAsyncTaskPlus() {
        System.out.println("执行异步任务Plus：");
        IntStream.range(0,5).forEach(el->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return new AsyncResult<>("异步方法返回值");
    }

    @Async
    public void executeAsyncInstance(ServiceInstance instance) throws InterruptedException {
        System.out.println("实例运行");
        String service_id = (String) ((LinkedHashMap) instance.getService()).get("id");;
        Path path;
        if(instance.getInstanceEnum().equals(InstanceEnum.MODEL)){
            path= Paths.get(storePath,"service_entity","model",service_id);
        }else {
            path= Paths.get(storePath,"service_entity","process",service_id);
        }
        Process exec = RuntimeUtil.exec(null, path.toFile(), "java -Dfile.encoding=utf-8 -jar main.jar localhost 7999 " + instance.getId());
        RuntimeUtil.getResult(exec);
    }
}
