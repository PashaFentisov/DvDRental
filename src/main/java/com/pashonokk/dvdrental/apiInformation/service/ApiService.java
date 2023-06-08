package com.pashonokk.dvdrental.apiInformation.service;

import com.pashonokk.dvdrental.apiInformation.entity.ApiInfo;
import com.pashonokk.dvdrental.apiInformation.entity.ControllerRecord;
import com.pashonokk.dvdrental.apiInformation.entity.EndpointParamRecord;
import com.pashonokk.dvdrental.apiInformation.entity.EndpointRecord;
import org.reflections.Reflections;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ApiService {
    private static final String PATH = "com.pashonokk.dvdrental";

    private ApiInfo apiInfo;

    public ApiInfo getApiInfo() {
        if (apiInfo != null) {
            return apiInfo;
        }
        apiInfo = constructMyApi();
        return apiInfo;
    }

    private List<Class<?>> getClassesAnnotatedWithRestController() {
        var reflections = new Reflections(PATH);
        return new ArrayList<>(reflections.getTypesAnnotatedWith(RestController.class));
    }

    private ApiInfo constructMyApi() {
        ApiInfo api = new ApiInfo();
        List<ControllerRecord> controllerRecords = constructControllerRecords();
        api.setRecords(controllerRecords);
        int countOfAllEndpoints = 0;
        for (ControllerRecord controllerRecord : controllerRecords) {
            countOfAllEndpoints += controllerRecord.getNumberOfAPIs();
        }
        api.setTotalNumberOfAPIs(countOfAllEndpoints);
        return api;
    }

    private List<ControllerRecord> constructControllerRecords() {
        List<ControllerRecord> records = new ArrayList<>();
        var classes = getClassesAnnotatedWithRestController();
        ControllerRecord controllerRecord = new ControllerRecord();
        for (Class<?> clazz : classes) {
            controllerRecord.setName(clazz.getSimpleName().replace("Controller", ""));
            controllerRecord.setNumberOfAPIs(countEndpoints(getEndpointsOfClass(clazz)));
            controllerRecord.setRecords(getAllEndpointsByClass(clazz));
            records.add(controllerRecord);
            controllerRecord = new ControllerRecord();
        }
        return records;
    }

    private Method[] getEndpointsOfClass(Class<?> clazz) {
        return (Method[]) Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class) ||
                        method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)
                        || method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(PatchMapping.class)
                        || method.isAnnotationPresent(DeleteMapping.class)).toArray();
    }

    private int countEndpoints(Method[] method) {
        return method.length;
    }

    private List<EndpointRecord> getAllEndpointsByClass(Class<?> clazz) {
        EndpointRecord endpointRecord = new EndpointRecord();
        List<EndpointRecord> endpointRecords = new ArrayList<>();
        Method[] methods = getEndpointsOfClass(clazz);
        for (Method method : methods) {
            endpointRecord.setHttpMethod(getHttpMethodOfEndpoint(method));
            endpointRecord.setPath(getPathOfEndpoint(method, clazz));
            endpointRecord.setRoles(getRolesOfEndpoint(method, clazz));
            endpointRecord.setEndpointParamRecords(getAllEndpointParams(method));
            endpointRecords.add(endpointRecord);
            endpointRecord = new EndpointRecord();
        }
        return endpointRecords;
    }

    private List<EndpointParamRecord> getAllEndpointParams(Method method) {
        Parameter[] parameters = method.getParameters();
        List<EndpointParamRecord> endpointParamRecords = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                endpointParamRecords.add(constructParam(parameter));
            }
        }
        return endpointParamRecords;
    }

    private EndpointParamRecord constructParam(Parameter parameter) {
        EndpointParamRecord endpointParamRecord = new EndpointParamRecord();
        String name;
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if (!requestParam.name().equals("")) {
            name = requestParam.name();
        } else if (!requestParam.value().equals("")) {
            name = requestParam.value();
        } else {
            name = parameter.getName();
        }
        endpointParamRecord.setRequired(requestParam.required());
        endpointParamRecord.setName(name);
        return endpointParamRecord;
    }

    private String getHttpMethodOfEndpoint(Method method) {
        String httpMethodOfEndpoint = "";
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == RequestMapping.class) {
                httpMethodOfEndpoint = Arrays.stream(((RequestMapping) annotation).method()).findFirst().orElseThrow().toString();
            } else if (annotation instanceof GetMapping || annotation instanceof PostMapping
                    || annotation instanceof PutMapping || annotation instanceof PatchMapping
                    || annotation instanceof DeleteMapping) {
                httpMethodOfEndpoint = annotation.annotationType().getSimpleName().replace("Mapping", "");
            }
        }
        return httpMethodOfEndpoint.toUpperCase();
    }


    private String getPathOfEndpoint(Method method, Class<?> clazz) {
        String path = "";
        String[] value = null;
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            value = clazz.getAnnotation(RequestMapping.class).value();
            path += Arrays.toString(value).replace("[", "").replace("]", "");
        }
        if (method.isAnnotationPresent(GetMapping.class)) {
            value = method.getAnnotation(GetMapping.class).value();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            value = method.getAnnotation(PostMapping.class).value();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            value = method.getAnnotation(PutMapping.class).value();
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            value = method.getAnnotation(PatchMapping.class).value();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            value = method.getAnnotation(DeleteMapping.class).value();
        }
        path += Arrays.toString(value).replace("[", "").replace("]", "");
        return path;
    }

    private List<String> getRolesOfEndpoint(Method method, Class<?> clazz) {
        if (clazz.isAnnotationPresent(Secured.class)) {
            return List.of(clazz.getAnnotation(Secured.class).value());
        }
        if (method.isAnnotationPresent(Secured.class)) {
            return List.of(method.getAnnotation(Secured.class).value());
        }
        return Collections.emptyList();
    }

}
