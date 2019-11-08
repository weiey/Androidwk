package com.weiey.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Gson 的JSON处理工具
 */
public class GsonUtils {
    private static final Gson gson = new GsonBuilder()
            //解决Integer被默认解析为double 问题
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(), new GsonTypeAdapter())
//            .registerTypeAdapter(new TypeToken<JSONArray>() {
//            }.getType(), new GsonTypeAdapter())
            //Integer 默认值0
            .registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
                @Override
                public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    try {
                        return json.getAsInt();
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            })
            //String 默认值空字符
            .registerTypeAdapter(String.class, new JsonDeserializer<String>() {
                @Override
                public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    try {
                        return json.getAsString();
                    } catch (Exception e) {
                        return "";
                    }
                }
            })
            //Long 默认值为0L
            .registerTypeAdapter(Long.class, new JsonDeserializer<Long>() {
                @Override
                public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    try {
                        return json.getAsLong();
                    } catch (Exception e) {
                        return 0L;
                    }
                }
            })
            .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue()) {
                        return new JsonPrimitive(src.longValue());
                    }
                    return new JsonPrimitive(src);
                }
            })
//           不用 Gson将一些字符自动转换为Unicode转义字符
            .disableHtmlEscaping()
            .create();

    /***
     * 将对象转为JSON串
     * @param src 将要被转化的对象
     * @return
     */
    public static String toJson(Object src) {
        if (src == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(src);
    }

    /***
     * 用来将JSON串转为对象，但此方法不可用来转带泛型的集合
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T toObj(String json, Class<T> classOfT) {
        return gson.fromJson(json, (Type) classOfT);
    }

    /**
     * 用来将JSON串转为对象，此方法可用来转带泛型的集合，
     * 如：Type为   new TypeToken<List<T>>(){}.getType()  ，其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public static Object toObj(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }


    /***
     * 用来将JSON串转为对象List，
     * classOfT为转换的数组类型，如：GsonUtils.toList(json,  Link[].class);
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> List<T> toList(String json, Class<T[]> classOfT) {
        try {
            T[] arr = new Gson().fromJson(json, classOfT);
            return Arrays.asList(arr);
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> toTypeList(String gsonString, Class<T> cls) {
        //这种方式会因为范型类型擦除，导致com.google.gson.internal.LinkedTreeMap转换报错
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
//        }
//        return list;

        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(gsonString).getAsJsonArray();
        for (JsonElement element : jsonarray) {
            list.add(gson.fromJson(element, cls));
        }
        return list;

    }




    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
