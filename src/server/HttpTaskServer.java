package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.LocalDateTimeAdapter;
import manager.Managers;
import manager.TaskManager;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer implements HttpHandler {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final TaskManager taskManager = Managers.getDefault();

    public void start() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this);
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        String[] path = exchange.getRequestURI().getPath().split("/");
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        int index = path.length - 1;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                System.out.println("Началась обработка " + method + " /tasks запроса");
                if (path[index].equals("tasks")) {
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
                if (path[index].equals("history")) {
                    response = gson.toJson(taskManager.getHistory());
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
                if (path[index].equals("task")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        response = gson.toJson(taskManager.getTasks());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        response = gson.toJson(taskManager.getValueById(id));
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("epic")) {
                    if (exchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        response = gson.toJson(taskManager.getSubtaskFromEpic(id));
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        response = gson.toJson(taskManager.getEpics());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("subtask")) {
                    response = gson.toJson(taskManager.getSubtasks());
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
            case "POST":
                System.out.println("Началась обработка " + method + " /tasks запроса");
                if (path[index].equals("task")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        Task task = gson.fromJson(body, Task.class);
                        try {
                            taskManager.addTask(task);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача добавлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        Task task = gson.fromJson(body, Task.class);
                        try {
                            taskManager.updateTask(id,task);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача обновлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("epic")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.addEpic(epic);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача добавлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.updateEpic(id,epic);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача обновлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("subtask")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        try {
                            taskManager.addSubtask(subtask);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача добавлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        try {
                            taskManager.updateSubtask(id, subtask);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача обновлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
            case "DELETE":
                if (path[index].equals("epic")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        taskManager.deleteAllTheTasksInListSubtask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Epic-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        taskManager.deleteValueEpicById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Epic-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("subtask")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        taskManager.deleteAllTheTasksInListSubtask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Subtask-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        taskManager.deleteValueSubtaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Subtask-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("task")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        taskManager.deleteAllTheTasksInListTask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Task-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        taskManager.deleteValueTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Task-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
        }
    }
}