package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TypeTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient client;

    public HttpTaskManager(String url) {
        if (client!=null){
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
        client.put("task",gson.toJson(tasks));
        client.put("epic",gson.toJson(epics));
        client.put("subtask",gson.toJson(subtasks));
        client.put("history",gson.toJson(history));


    }
    public Type taskListType = new TypeToken <List<Task>>() {
    }.getType();
    public Type epicListType = new TypeToken<List<Task>>(){
    }.getType();
    public  Type subtaskListType = new TypeToken <List<Task>>(){
    }.getType();
    public Type historyListType = new TypeToken <List<Task>>(){
    }.getType();

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter())
                .create();
        List <Task> tasks=gson.fromJson(client.load("task"), taskListType);
        List<Epic> epics= gson.fromJson(client.load("epic"),epicListType);
        List<Subtask> subtasks =gson.fromJson(client.load("subtask"),subtaskListType);
        List<Task> history = gson.fromJson(client.load("history"),historyListType);
        for (Task task:tasks){
         super.tasks.put(task.getId(),task);
        }
        for (Epic epic:epics){
            super.epics.put(epic.getId(),epic);
        }
        for (Subtask subtask:subtasks){
            super.subtasks.put(subtask.getId(),subtask);
        }
        for (Task task:history){
            super.historyManager.add(task);
        }
    }
}