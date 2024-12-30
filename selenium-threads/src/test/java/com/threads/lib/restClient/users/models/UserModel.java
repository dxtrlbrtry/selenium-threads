package com.threads.lib.restClient.users.models;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;

public record UserModel(Integer id, String name, String password) {
    public static UserModel fromJson(JsonNode node) {
        for (Field field : UserModel.class.getDeclaredFields()) {
            if (!node.has(field.getName())) {
                throw new RuntimeException("Failed to parse user model from json input: " + node.textValue());
            }
        }
        return new UserModel(
                node.get("id").asInt(),
                node.get("name").asText(),
                node.get("password").asText());
    }
}
