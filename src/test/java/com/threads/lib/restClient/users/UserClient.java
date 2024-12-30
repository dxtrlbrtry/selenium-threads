package com.threads.lib.restClient.users;

import com.threads.lib.restClient.RestClientBase;
import com.threads.lib.restClient.users.models.UserModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class UserClient extends RestClientBase {
    private final String usersEndpoint = "/users";

    public UserClient(String baseUrl) {
        super(baseUrl);
    }

    public List<UserModel> getUsers() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + usersEndpoint))
                .GET()
                .build();
        return sendGetUsersRequest(request);
    }

    public List<UserModel> getUsers(String[] tags) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + usersEndpoint + "?tags=" + String.join(",", tags)))
                .GET()
                .build();
        return sendGetUsersRequest(request);
    }

    private List<UserModel> sendGetUsersRequest(HttpRequest request) {
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Get users request failed with status code: " + response.statusCode()
                        + ". Reason: " + response.body());
            }
            var result = new ArrayList<UserModel>();
            var arrayNode = new ObjectMapper().readTree(response.body());
            if (arrayNode.isArray()) {
                arrayNode.forEach(node -> result.add(UserModel.fromJson(node)));
            }
            return result;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error getting users " + e);
        }
    }

    public UserModel createUser(String name, String password, String[] tags) {
        try {
            var userObject = new HashMap<String, Object>() {
                {
                    put("name", name);
                    put("password", password);
                    put("tags", tags);
                }
            };
            var json = new ObjectMapper().writeValueAsString(userObject);
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + usersEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            try {
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                var node = new ObjectMapper().readTree(response.body());
                return UserModel.fromJson(node);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error processing user model to JSON. " + e.getMessage());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing user model to JSON with given params name:" + name +
                    ", password: " + password + ", tags: [" + String.join(",", tags) + "]. "
                    + e.getMessage());
        }
    }

    public void deleteUser(Integer id) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + usersEndpoint + "/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404) {
                System.err.println("User already deleted");
            } else if (response.statusCode() != 204) {
                throw new RuntimeException(
                        "Delete user request failed with status code: " + response.statusCode() + ". Reason: "
                                + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error sending deleting user request " + e);
        }
    }
}
