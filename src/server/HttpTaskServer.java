package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sun.net.httpserver.HttpExchange;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class HttpTaskServer {
    private final TaskManager taskManager = Managers.getDefaultFileBacked();

    public void httpTaskServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        server.createContext("/tasks/task", this::handleTask);
        server.createContext("/tasks/subtask", this::handleSubtask);
        server.createContext("/tasks/epic", this::handleEpic);
        server.createContext("/tasks", this::handleAllTasks);
        server.createContext("/tasks/history", this::handleHistory);
        server.start();
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                taskManager.getHistory();
                String response = "History get successfully";
                exchange.sendResponseHeaders(200, response.length());  // отправка ответа
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
                break;
            default:
                response = "Invalid request method"; // отправка ошибки в случае неправильного метода запроса
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
        }
    }

    private void handleAllTasks(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String stringUri = exchange.getRequestURI().toString();
        switch (method) {
            case "GET":
                taskManager.getPrioritizedTasks();
                String response = "Tasks get successfully";
                exchange.sendResponseHeaders(200, response.length()); // отправка ответа
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
                break;
            default:// отправка ошибки в случае неправильного метода запроса
                response = "Invalid request method";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
        }
    }

    private void handleTask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String stringUri = exchange.getRequestURI().toString();
        switch (method) {
            case "POST":
                try (InputStream requestBody = exchange.getRequestBody()) { // передать задачу через ури
                    String response;
                    String json = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder() // преобразование JSON-строки в объект Task
                            .setPrettyPrinting()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .registerTypeAdapter(Duration.class, new DurationAdapter())
                            .create();
                    Task task = gson.fromJson(json, Task.class);
                    if (taskManager.getTaskForId(task.getId()) == null) {
                        taskManager.createTask(task);
                        // отправка ответа
                        response = "Task created successfully";
                    } else {
                        taskManager.updateTask(task);
                        response = "Task update successfully";
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            case "GET":
                if (stringUri.contains("id")) { // (2) если в URI есть id то получаем задачу по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    if (taskManager.getTaskForId(id) == null) { // (3) если метод возвращает null то сообщаем что такой задачи не существует
                        String response = "There is no task with this id";
                        exchange.sendResponseHeaders(404, response.length());
                        try (OutputStream responseBody = exchange.getResponseBody()) {
                            responseBody.write(response.getBytes());
                        }
                    } else { // (3) иначе получаем задачу
                        taskManager.getTaskForId(id);
                        String response = "Task by id received";
                        exchange.sendResponseHeaders(200, response.length());
                        try (OutputStream responseBody = exchange.getResponseBody()) {
                            responseBody.write(response.getBytes());
                        }
                    }
                } else { // (2) иначе запускаем метод получения задач
                    taskManager.getTask();
                    String response = "Tasks get successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            case "DELETE":
                if (stringUri.contains("id")) { //(4) если в URI содержится id то удаляем по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    taskManager.removeForIdTask(id);
                    String response = "Task remove successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                } else { //(4) если id не содержится то очищаем все задачи
                    taskManager.clearTasks();
                    String response = "Tasks clear successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            default:// отправка ошибки в случае неправильного метода запроса
                String response = "Invalid request method";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
        }
    }

    public void handleSubtask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String stringUri = exchange.getRequestURI().toString();
        switch (method) {
            case "POST":
                try (InputStream requestBody = exchange.getRequestBody()) {
                    String response;
                    String json = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder()// преобразование JSON-строки в объект Task
                            .setPrettyPrinting()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    Subtask subtask = gson.fromJson(json, Subtask.class);
                    if (taskManager.getSubtaskForId(subtask.getId()) == null) {
                        taskManager.createSubtask(subtask);
                        response = "Subtask created successfully";
                    } else {
                        taskManager.updateTask(subtask);
                        response = "Subtask update successfully";
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            case "GET":
                String response = null;
                if (stringUri.contains("tasks/subtask/?id=")) { // (2) если в URI есть id то получаем задачу по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    if (taskManager.getSubtaskForId(id) == null) { // (3) если метод возвращает null то сообщаем что такой задачи не существует
                        response = "There is no subtask with this id";
                        exchange.sendResponseHeaders(404, response.length());
                        try (OutputStream responseBody = exchange.getResponseBody()) {
                            responseBody.write(response.getBytes());
                        }
                    } else { // (3) иначе получаем задачу
                        taskManager.getSubtaskForId(id);
                        response = "Subtask by id received";
                    }
                } else if (stringUri.contains("tasks/subtask/epic/?id=")) {
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    Epic epic = taskManager.getEpicForId(id);
                    taskManager.getSubtasksList(epic);
                    response = "Subtasks get successfully";
                } else { // (2) иначе запускаем метод получения задач
                    taskManager.getSubtasks();
                    response = "Subtask get successfully";
                }
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
                break;
            case "DELETE":
                if (stringUri.contains("id")) { //(4) если в URI содержится id то удаляем по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    taskManager.removeForIdSubtask(id);
                    response = "Subtask remove successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                } else { //(4) если id не содержится то очищаем все задачи
                    taskManager.clearSubtasks();
                    response = "Subtask clear successfully";
                    exchange.sendResponseHeaders(200, response.length());
                }
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
                break;
            default:// отправка ошибки в случае неправильного метода запроса
                response = "Invalid request method";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
        }
    }

    public void handleEpic(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String stringUri = exchange.getRequestURI().toString();
        switch (method) {
            case "POST":
                try (InputStream requestBody = exchange.getRequestBody()) {
                    String json = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder()   // преобразование JSON-строки в объект Эпик
                            .setPrettyPrinting()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    Epic epic = gson.fromJson(json, Epic.class);
                    if (taskManager.getEpicForId(epic.getId()) == null) {
                        taskManager.createEpic(epic);
                    } else {
                        taskManager.updateEpic(epic);
                    }
                    String response = "Epic created successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            case "GET":
                if (stringUri.contains("id")) { // (2) если в URI есть id то получаем задачу по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    if (taskManager.getEpicForId(id) == null) { // (3) если метод возвращает null то сообщаем что такой задачи не существует
                        String response = "There is no epic with this id";
                        exchange.sendResponseHeaders(404, response.length());
                        try (OutputStream responseBody = exchange.getResponseBody()) {
                            responseBody.write(response.getBytes());
                        }
                    } else { // (3) иначе получаем задачу
                        taskManager.getEpicForId(id);
                        String response = "Epic by id received";
                        exchange.sendResponseHeaders(200, response.length());
                        try (OutputStream responseBody = exchange.getResponseBody()) {
                            responseBody.write(response.getBytes());
                        }
                    }
                } else { // (2) иначе запускаем метод получения задач
                    taskManager.getEpics();
                    String response = "Epic get successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            case "DELETE":
                if (stringUri.contains("id")) { //(4) если в URI содержится id то удаляем по id
                    URI uri = exchange.getRequestURI();
                    int id = Integer.parseInt(uri.getQuery().split("=")[1]);
                    taskManager.removeForIdEpic(id);
                    String response = "Epic remove successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                } else { //(4) если id не содержится то очищаем все задачи
                    taskManager.clearEpics();
                    String response = "Epic clear successfully";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(response.getBytes());
                    }
                }
                break;
            default:// отправка ошибки в случае неправильного метода запроса
                String response = "Invalid request method";
                exchange.sendResponseHeaders(400, response.length());
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(response.getBytes());
                }
        }
    }

    // правила конвертации, описанные в TypeAdapter для класса LocalDate
    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); //DateTimeFormatter.ISO_LOCAL_DATE_TIME
            } else {
                jsonWriter.nullValue();
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            String str = in.nextString();
            return Duration.parse(str);
        }
    }
}
