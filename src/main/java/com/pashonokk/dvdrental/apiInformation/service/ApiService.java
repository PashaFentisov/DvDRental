package com.pashonokk.dvdrental.apiInformation.service;

import com.pashonokk.dvdrental.apiInformation.entity.ControllerRecord;
import com.pashonokk.dvdrental.apiInformation.entity.EndpointRecord;
import com.pashonokk.dvdrental.apiInformation.entity.MyApi;
import com.pashonokk.dvdrental.apiInformation.entity.Param;
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

    private MyApi myApi;

    public MyApi getMyApi() {
        if (myApi != null) {
            return myApi;
        }
        myApi = constructMyApi();
        return myApi;
    }

    private List<Class<?>> getClassesAnnotatedWithRestController() {
        var reflections = new Reflections(PATH);
        return new ArrayList<>(reflections.getTypesAnnotatedWith(RestController.class));
    }

    private MyApi constructMyApi() {
        MyApi api = new MyApi();
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
            controllerRecord.setNumberOfAPIs(countEndpoints(clazz.getDeclaredMethods()));
            controllerRecord.setRecords(getAllEndpointsByClass(clazz));

            records.add(controllerRecord);
            controllerRecord = new ControllerRecord();
        }
        return records;
    }

//    private Method[] getEndpointsOfClass(Class<?> clazz) {
//        return (Method[]) Arrays.stream(clazz.getDeclaredMethods())
//                .filter(method -> method.isAnnotationPresent(RequestMapping.class) ||
//                        method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)
//                        || method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(PatchMapping.class)  //TODO finish
//                        || method.isAnnotationPresent(DeleteMapping.class)).toArray();
//    }

    private int countEndpoints(Method[] method) {
        int count = 0;
        for (Method value : method) {
            if (value.isAnnotationPresent(RequestMapping.class) || value.isAnnotationPresent(GetMapping.class)
                    || value.isAnnotationPresent(PostMapping.class) || value.isAnnotationPresent(PatchMapping.class)
                    || value.isAnnotationPresent(DeleteMapping.class) || value.isAnnotationPresent(PutMapping.class)) {
                count++;
            }
        }
        return count;
    }

    private List<EndpointRecord> getAllEndpointsByClass(Class<?> clazz) {
        EndpointRecord endpointRecord = new EndpointRecord();
        List<EndpointRecord> endpointRecords = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            endpointRecord.setHttpMethod(getHttpMethodOfEndpoint(method));
            if (endpointRecord.getHttpMethod().equals("")) {
                continue;
            }
            endpointRecord.setPath(getPathOfEndpoint(method, clazz));
            endpointRecord.setRoles(getRolesOfEndpoint(method, clazz));
            endpointRecord.setParams(getAllEndpointParams(method));
            endpointRecords.add(endpointRecord);
            endpointRecord = new EndpointRecord();
        }
        return endpointRecords;
    }

    private List<Param> getAllEndpointParams(Method method) {
        Parameter[] parameters = method.getParameters();
        List<Param> params = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                params.add(constructParam(parameter));
            }
        }
        return params;
    }

    private Param constructParam(Parameter parameter) {
        Param param = new Param();
        String name;
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if (!requestParam.name().equals("")) {
            name = requestParam.name();
        } else if (!requestParam.value().equals("")) {
            name = requestParam.value();
        } else {
            name = parameter.getName();
        }
        param.setRequired(requestParam.required());
        param.setName(name);
        return param;
    }

    private String getHttpMethodOfEndpoint(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestMapping ann) {
                return Arrays.stream(ann.method()).findFirst().orElseThrow().toString();
            } else if (annotation instanceof GetMapping || annotation instanceof PostMapping
                    || annotation instanceof PutMapping || annotation instanceof PatchMapping
                    || annotation instanceof DeleteMapping) {
                return annotation.annotationType().getSimpleName().replace("Mapping", "");
            }
        }
        return "";
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
