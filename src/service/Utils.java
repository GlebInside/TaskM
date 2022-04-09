package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.handlers.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class Utils {
    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson;
    }
}
