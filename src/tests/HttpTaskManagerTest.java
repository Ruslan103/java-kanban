package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.*;
import model.*;
import org.junit.jupiter.api.*;
import server.CustomException;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerTest {
    Task task1 = new Task("task1", "descriptionT1", Status.NEW);
    Task task2 = new Task("task2", "descriptionT2", Status.NEW);
    Task task3 = new Task("task3", "descriptionT3", Status.NEW);
    Epic epic1 = new Epic("epic1", "description1", Status.NEW);
    Epic epic2 = new Epic("epic2", "description2", Status.NEW);
    Epic epic3 = new Epic("epic3", "description3", Status.NEW);
    Subtask subtask1 = new Subtask("subtask1", "descriptionS1", Status.NEW, 1);
    Subtask subtask2 = new Subtask("subtask2", "descriptionS2", Status.NEW, 1);
    Subtask subtask3 = new Subtask("subtask3", "descriptionS3", Status.NEW, 2);

    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static HttpTaskManager manager;

    @BeforeEach
    public void server() throws IOException, CustomException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.httpTaskServer();
        manager = new HttpTaskManager("http://localhost:8078");
    }

    protected void createTasksForTestWithHistory() throws IOException, InterruptedException {
        postMethod(epic1);
        postMethod(epic2);
        postMethod(subtask1);
        postMethod(subtask2);
        postMethod(subtask3);
        postMethod(epic3);
        postMethod(task1);
        postMethod(task2);
        postMethod(task3);
    }

    private void postMethod(Task task) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String s = gson.toJson(task);
        URI uri;
        if (task.getType().equals(TypeTask.TASK)) {
            uri = URI.create("http://localhost:8080/tasks/task");
        } else if (task.getType().equals(TypeTask.EPIC)) {
            uri = URI.create("http://localhost:8080/tasks/epic");
        } else if (task.getType().equals(TypeTask.SUBTASK)) {
            uri = URI.create("http://localhost:8080/tasks/subtask");
        } else {
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(s))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response1 = client.send(request, handler1).toString();
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();// с конвертацией содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/task) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        System.out.println("это" + response);
        String expectedResponse = "(GET http://localhost:8080/tasks/task/?id=7) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getEpicsTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();// с конвертацией содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/epic) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getSubtaskTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();// с конвертацией содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/subtask) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    //------------------
    @Test
    public void getEpicById() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/epic/?id=1) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/subtask/?id=4) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String s = gson.toJson(task1);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(s))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/task) 200";
        Assertions.assertEquals(response, expectedResponse);
    }

    @Test
    public void createEpicTest() throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String s = gson.toJson(epic1);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(s))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/epic) 200";
        Assertions.assertEquals(response, expectedResponse);
    }

    @Test
    public void createSubtaskTest() throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String s = gson.toJson(subtask1);
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(s))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/subtask) 200";
        Assertions.assertEquals(response, expectedResponse);
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        Task testTask = new Task("TestTitle", "TestDescription", Status.NEW, task1.getId()); // создаем новую задачу
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String stringTestTask = gson.toJson(testTask);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(stringTestTask))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/task) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void updateEpicTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        Epic testEpic = new Epic("TestTitle", "TestDescription", Status.NEW, task1.getId()); // создаем новую задачу
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String stringTestEpic = gson.toJson(testEpic);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(stringTestEpic))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/epic) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        Subtask testSubtask = new Subtask("TestTitle", "TestDescription", Status.NEW, task1.getId()); // создаем новую задачу
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        String stringTestSubtask = gson.toJson(testSubtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(stringTestSubtask))
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler1).toString();
        String expectedResponse = "(POST http://localhost:8080/tasks/subtask) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void deleteTaskForId() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(DELETE http://localhost:8080/tasks/task/?id=7) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void deleteEpicForId() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(DELETE http://localhost:8080/tasks/epic/?id=1) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void deleteSubtaskForIdTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(DELETE http://localhost:8080/tasks/subtask/?id=4) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getEpicSubtasksTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/subtask/epic/?id=1) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        createTasksForTestWithHistory();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();  // HTTP-запрос
        HttpClient client = HttpClient.newHttpClient();  // HTTP-клиент с настройками по умолчанию
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();//  конвертация содержимого в строку
        String response = client.send(request, handler).toString();
        String expectedResponse = "(GET http://localhost:8080/tasks/history) 200";
        Assertions.assertEquals(response, expectedResponse, "код ответа не 200");
    }
}
