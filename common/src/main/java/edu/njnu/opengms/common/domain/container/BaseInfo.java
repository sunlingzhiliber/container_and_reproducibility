package edu.njnu.opengms.common.domain.container;

import edu.njnu.opengms.common.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName edu.njnu.opengms.container.base.BaseInfo
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/4/9
 * @Version 1.0.0
 */
@Data
public class BaseInfo extends BaseEntity {
    public String name;
    public String description;
    public List<String> tags;
    public String details;
    public String snapshot;
    public String resourceUrl;
}
