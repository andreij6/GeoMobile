package com.geospatialcorporation.android.geomobile.models.Login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LoginBodyJsonSerializer implements JsonSerializer<LoginBody> {

    @Override
    public JsonElement serialize(LoginBody foo, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("LoginAttemptId", context.serialize(foo.getLoginAttemptId()));
        object.add("Username", context.serialize(foo.getUsername()));
        object.add("Password", context.serialize(foo.getPassword()));
        return object;
    }
}