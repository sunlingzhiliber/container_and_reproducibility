package edu.njnu.opengms.container.domain.data.dto;


import edu.njnu.opengms.common.domain.container.data.DataService;
import edu.njnu.opengms.common.domain.container.data.support.DataEnums;
import edu.njnu.opengms.common.dto.ToDomainConverter;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName AddDataServiceDTO
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/4/29
 * @Version 1.0.0
 */
@Data
@Builder
public class AddDataServiceDTO implements ToDomainConverter<DataService> {
    String name;
    String description;
    List<String> tags;
    String details;
    String resourceUrl;
    String schemaPath;
    String key;
    DataEnums type;
}
