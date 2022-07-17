package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
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
    Gson gson = new Gson();
    FileBackedTasksManager fileBackedTasksManager = Managers.getFileManager();

    {
        Task task = new Task("Проверка Task", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 1, 10);
        Epic epic = new Epic("Проверка Epic", TypeTask.EPIC, "Epic для проверки", StatusTasks.NEW, 2,
                5);
        Subtask subtask = new Subtask("Проверка Subtask", TypeTask.SUBTASK, "Subtask для проверки"
                , StatusTasks.IN_PROGRESS, 3, 2, 15);
        task.setStartTime((LocalDateTime.now()));
        epic.setStartTime(LocalDateTime.now().plusMinutes(60));
        subtask.setStartTime(LocalDateTime.now().plusMinutes(120));
        try {
            fileBackedTasksManager.addTask(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTasksManager.addEpic(epic);
        try {
            fileBackedTasksManager.addSubtask(subtask);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTasksManager.getValueById(1);
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServer());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = null;
        String[] path = exchange.getRequestURI().getPath().split("/");
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        int index = path.length - 1;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                System.out.println("Началась обработка " + method + " /tasks запроса");
                if (path[index].equals("tasks")) {
                    response = gson.toJson(fileBackedTasksManager.getPrioritizedTasks());
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
                if (path[index].equals("history")) {
                    response = gson.toJson(fileBackedTasksManager.getHistory());
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
                if (path[index].equals("task")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        response = gson.toJson(fileBackedTasksManager.getTasks());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        response = gson.toJson(fileBackedTasksManager.getValueById(id));
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("epic")) {
                    if (exchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        response = gson.toJson(fileBackedTasksManager.getSubtaskFromEpic(id));
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        response = gson.toJson(fileBackedTasksManager.getEpics());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("subtask")) {
                    response = gson.toJson(fileBackedTasksManager.getSubtasks());
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
                            fileBackedTasksManager.addTask(task);
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
                        task.setStartTime(LocalDateTime.now());
                        try {
                            fileBackedTasksManager.updateTask(id,task);
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
                        fileBackedTasksManager.addEpic(epic);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Задача добавлена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        Epic epic = gson.fromJson(body, Epic.class);
                        fileBackedTasksManager.updateEpic(id,epic);
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
                            fileBackedTasksManager.addSubtask(subtask);
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
                            fileBackedTasksManager.updateSubtask(id, subtask);
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
                        fileBackedTasksManager.deleteAllTheTasksInListSubtask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Epic-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        fileBackedTasksManager.deleteValueEpicById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Epic-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("subtask")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        fileBackedTasksManager.deleteAllTheTasksInListSubtask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Subtask-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        fileBackedTasksManager.deleteValueSubtaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Subtask-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
                if (path[index].equals("task")) {
                    if (exchange.getRequestURI().getQuery() == null) {
                        fileBackedTasksManager.deleteAllTheTasksInListTask();
                        exchange.sendResponseHeaders(200, 0);
                        response = "Все Task-задачи удалены";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    } else {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                        fileBackedTasksManager.deleteValueTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        response = "Task-задача под номером " + id + " удалена";
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                    }
                }
        }
    }

    public FileBackedTasksManager getFileBackedTasksManager() {
        return fileBackedTasksManager;
    }
}