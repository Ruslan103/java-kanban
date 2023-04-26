package manager;

import model.Status;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = registerOrThrow(url);
    }

    private String registerOrThrow(String url) {
        try {
            URI uri = URI.create(url + "/register");
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
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
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
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put (String key, String value) {
        try {
            URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
            // send request
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
        } catch (IOException | InterruptedException e){
            // throw exception
            throw new RuntimeException(e);
        }
    }
}