package server;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;


    public KVTaskClient(String url) throws CustomException {
        this.url = url;
        this.apiToken = registerOrThrow(url);
    }

    private String registerOrThrow(String url) throws CustomException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .uri(uri) // указываем адрес ресурса
                .GET()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Invalid response status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new CustomException("Error while sending HTTP request", e);
        }
    }

    public String load(String key) throws CustomException {
        try {
            URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
            // создаём объект, описывающий HTTP-запрос
            HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                    .uri(uri) // указываем адрес ресурса
                    .GET()
                    .build(); // заканчиваем настройку и создаём ("строим") http-запрос
            // HTTP-клиент с настройками по умолчанию
            HttpClient client = HttpClient.newHttpClient();
            // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Invalid response status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new CustomException("Error while sending HTTP request", e);
        }
    }
public void put(String key, String value) {
    try {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .uri(uri) // указываем адрес ресурса
                .header("Content-Type", "application/json") // задаем заголовок Content-Type
                .POST(HttpRequest.BodyPublishers.ofString(value)) // задаем тело запроса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();
        // получаем стандартный обработчик тела ответа с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        // проверяем код ответа
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
        }
    } catch (IOException | InterruptedException e) {
        // throw exception
        throw new RuntimeException(e);
    }
}
}