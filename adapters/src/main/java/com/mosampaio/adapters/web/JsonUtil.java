package com.mosampaio.adapters.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }

    public static Map<String, String> fromJson(String json) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> result = new Gson().fromJson(json, type);

        return Optional.ofNullable(result).orElseGet(HashMap::new);
    }

}