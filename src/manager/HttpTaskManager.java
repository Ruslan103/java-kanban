package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.CustomException;
import server.HttpTaskServer;
import server.KVTaskClient;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private final Type taskListType = new TypeToken<List<Task>>() {
    }.getType();
    private final Type epicListType = new TypeToken<List<Task>>() {
    }.getType();
    private final Type subtaskListType = new TypeToken<List<Task>>() {
    }.getType();
    private final Type historyListType = new TypeToken<List<Task>>() {
    }.getType();

    public HttpTaskManager(String url) throws CustomException {
        if (client != null) {
            load();
        }
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        List<Task> tasks = new ArrayList<>(super.getTask());
        List<Epic> epics = new ArrayList<>(super.getEpics());
        List<Subtask> subtasks = new ArrayList<>(super.getSubtasks());
        List<Task> history = new ArrayList<>(super.getHistory());
        client.put("task", gson.toJson(tasks));
        client.put("epic", gson.toJson(epics));
        client.put("subtask", gson.toJson(subtasks));
        client.put("history", gson.toJson(history));
    }

    public void load() throws CustomException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        List<Task> tasks = gson.fromJson(client.load("task"), taskListType);
        List<Epic> epics = gson.fromJson(client.load("epic"), epicListType);
        List<Subtask> subtasks = gson.fromJson(client.load("subtask"), subtaskListType);
        List<Task> history = gson.fromJson(client.load("history"), historyListType);
        for (Task task : tasks) {
            super.tasks.put(task.getId(), task);
            super.getPrioritizedTasks().add(task);
        }
        for (Epic epic : epics) {
            super.epics.put(epic.getId(), epic);
            super.getPrioritizedTasks().add(epic);
        }
        for (Subtask subtask : subtasks) {
            super.subtasks.put(subtask.getId(), subtask);
            super.getPrioritizedTasks().add(subtask);
        }
        for (Task task : history) {
            super.historyManager.add(task);
        }
    }
}
