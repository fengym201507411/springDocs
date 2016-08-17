package com.galaxy.fym.test;

import com.galaxy.fym.common.ClassUtils;
import com.galaxy.fym.model.FieldAnnotation;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by fengyiming on 2016/7/28.
 */
public class AnnotationTest {

    public final static String DOMAIN = "http://mobile.360haoyao.com";

    @Test
    public void annotation() throws IOException {
        String packageName = "com.galaxy.fym.action";
        List<Class<?>> classList = ClassUtils.getAnnotationClassList(packageName, Controller.class);
        for (Class<?> clazz : classList) {
            RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
            String pathValue = StringUtils.join(classRequestMapping.value());
            System.out.println("-------类名:   " + clazz.getName() + "---------");
            System.out.println("-------功能介绍:   " + classRequestMapping.name() + "---------");
            //获取该类的所有属性
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    print("-------------------------------------------");
                    //获取属性名称
                    String fieldName = field.getName();
                    //获取属性类型
                    Class<?> fieldType = field.getType();
                    print("fieldName:   " + fieldName);
                    print("fieldType:   " + fieldType.getName());
                    //该属性的修饰符
                    print("修饰符:  " + Modifier.toString(field.getModifiers()));
                    FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
                    if (null != fieldAnnotation) {
                        print("参数的名称:   " + fieldAnnotation.desc());
                    }
                    print("-------------------------------------------");
                }
            }
            //获取该类的所有的成员方法
            Method[] methods = clazz.getMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    //判定是否为该类的方法而不是父类的方法
                    if (method.getDeclaringClass().getTypeName().equals(clazz.getTypeName())) {
                        print("-------------------------------------------");
                        String methodName = method.getName();
                        print("methodName:   " + methodName);
                        print("返回的数据:   " + method.getReturnType());
                        print("修饰符:  " + Modifier.toString(method.getModifiers()));
                        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                        if (requestMapping != null) {
                            String methodPath = StringUtils.join(requestMapping.value());
                            String path = DOMAIN + "/" + pathValue + "/" + methodPath + ".htm";
                            print("请求的路径：  " + path);
                        }
                        ResponseBody responseBody = method.getDeclaredAnnotation(ResponseBody.class);
                        if (responseBody == null) {
                            print("返回的数据类型:  视图");
                        } else {
                            print("返回的数据类型:  JSON");
                        }
                        //获取所有的方法的参数
                        Parameter[] parameters = method.getParameters();
                        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
                        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
                        if (parameters != null && parameters.length > 0) {
                            print("该方法需要传递参数");
                            for (int i = 0; i < parameters.length; i++) {
                                print("第" + i + "个参数的配置--------------------");
                                Parameter parameter = parameters[i];
                                String parameterName = parameterNames[i];
                                boolean isMust = true;
                                String defaultValue = "";
                                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                                if (requestParam != null) {
                                    String requestParameterName = requestParam.value();
                                    if (StringUtils.isNotEmpty(requestParameterName)) {
                                        parameterName = requestParameterName;
                                    }
                                    isMust = requestParam.required();
                                    defaultValue = requestParam.defaultValue();
                                }
                                print("参数名：   " + parameterName);
                                print("参数类型：   " + parameter.getType());
                                print("是否为必传参数：   " + isMust);
                                if (requestParam != null) {
                                    String parameterNameDesc = requestParam.name();
                                    if (StringUtils.isNotEmpty(defaultValue) && !isDefaultValue(defaultValue)) {
                                        print("默认值为：   " + defaultValue);
                                    }
                                    if (StringUtils.isNotEmpty(parameterNameDesc)) {
                                        print("参数描述：   " + parameterNameDesc);
                                    }
                                }
                            }
                        } else {
                            print("该方法不需要参数");
                        }
                        print("-------------------------------------------");
                    }
                }
            }
        }
    }

    private void print(String str) {
        System.out.println("-------------------" + str);
    }

    private boolean isDefaultValue(String defaultValue){
        char[] chars = defaultValue.toCharArray();
        char[] isNull = new char[]{'\n','\t','\t','\n','\t','\t','\n','\uE000','\ue001','\ue002','\n','\t','\t','\t','\t','\n'};
        if(chars.length == isNull.length){
            for(int index = 0;index < isNull.length;index++){
                if(isNull[index] != chars[index]){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }
}
