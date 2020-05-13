package edu.njnu.opengms.common.dto;


import cn.hutool.core.util.ReflectUtil;
import edu.njnu.opengms.common.entity.BaseEntity;
import edu.njnu.opengms.common.exception.MyException;
import edu.njnu.opengms.common.utils.CopyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @InterfaceName ToDomainConverter
 * @Description 继承了该接口的DTO ，可以直接转换为对应Domain。
 * @Author sun_liber
 * @Date 2019/7/9
 * @Version 1.0.0
 */
public interface ToDomainConverter<DOMAIN> {
    String BEFORE_UPDATE="edu.njnu.opengms.common.entity.BeforeUpdate";
    String BEFORE_INSERT="edu.njnu.opengms.common.entity.BeforeInsert";

    /**
     * 调用的情形为：将DTO转化为Domain，再将Domain插入到数据库中
     * @param domain
     */
    default void convertTo(DOMAIN domain) {
        invokeMethodByAnnotation(domain,BEFORE_INSERT);
        CopyUtils.copyProperties(this, domain);
    }

    /**
     * 调用的情形为：将DTO的信息更新到Domain上，再将Domain更新到数据库中
     * @param domain
     */
    default void updateTo(DOMAIN domain)  {
        invokeMethodByAnnotation(domain,BEFORE_UPDATE);
        CopyUtils.copyProperties(this, domain);
    }

    /**
     * 如果Domain对象继承BaseEntity对象的话，就执行Domain的并且被annotationType注解修饰过的方法，
     * 该方法避免了BaseEntity对象在插入数据库和更新数据库的公共代码重复。
     * @param domain
     * @param annotationType
     */
    default void invokeMethodByAnnotation(DOMAIN domain, String annotationType)  {
        Class<?> domainClass = domain.getClass();
        //只有继承BaseEntity的类才会执行
        if(BaseEntity.class.isAssignableFrom(domainClass)){
            try {
                Class<? extends Annotation> annotationCls = (Class<? extends Annotation>) Class.forName(annotationType);
                Method[] methods= ReflectUtil.getMethods(domainClass, el-> el.getAnnotation(annotationCls)!=null);
                for (Method method : methods) {
                    method.invoke(domain);
                }
            } catch (Exception e) {
                throw new MyException("Interface ToDomainConverter has some error");
            }
        }
    }



}
