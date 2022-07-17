package test;

import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;


public class HttpTaskServerTest {
    private HttpRequest request;
    Gson gson = new Gson();
    HttpClient client = HttpClient.newHttpClient();
    String url = "http://localhost:8080/";


    @Test
    void GetAllTheTasks() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetHistoryTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/history"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetTaskTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/task"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetTaskIdTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/task?id=1"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetEpicTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/epic"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetSubtaskFromEpicIdTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/epic?id=2"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void GetSubtaskTest() {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/subtask"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostTaskTest() {
        Task task = new Task("Проверка Task1", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 5, 10);
        task.setStartTime(LocalDateTime.now().plusMinutes(240));
        String test = "Задача добавлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(url + "tasks/task"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostUpdateTaskTest() {
        Task task = new Task("Проверка Task", TypeTask.TASK,
                "Task для проверки", StatusTasks.IN_PROGRESS, 1, 10);
        task.setStartTime(LocalDateTime.now().plusMinutes(300));
        String test = "Задача обновлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(url + "tasks/task?id=1"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostEpicTest() {
        Epic epic = new Epic("Проверка Epic1", TypeTask.EPIC, "Epic для проверки", StatusTasks.NEW,
                5, 5);
        epic.setStartTime(LocalDateTime.now().plusMinutes(45));
        String test = "Задача добавлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(url + "tasks/epic"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostUpdateEpicTest() {
        Epic epic = new Epic("Проверка Epic", TypeTask.EPIC, "Epic для проверки update", StatusTasks.NEW,
                2, 5);
        epic.setStartTime(LocalDateTime.now().plusMinutes(45));
        String test = "Задача обновлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(url + "tasks/epic?id=2"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostSubtaskTest() {
        Subtask subtask = new Subtask("Проверка Subtask1", TypeTask.SUBTASK, "Subtask для проверки"
                , StatusTasks.IN_PROGRESS, 6, 2, 15);
        subtask.setStartTime(LocalDateTime.now().plusMinutes(2));
        String test = "Задача добавлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(URI.create(url + "tasks/subtask"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void PostUpdateSubtaskTest() {
        Subtask subtask = new Subtask("Проверка Subtask", TypeTask.SUBTASK, "Subtask для проверки"
                , StatusTasks.NEW, 3, 2, 15);
        subtask.setStartTime(LocalDateTime.now().plusMinutes(2));
        String test = "Задача обновлена";
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(URI.create(url + "tasks/subtask?id=3"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteEpicTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/epic"))
                .build();
        String test = "Все Epic-задачи удалены";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteEpicIdTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/epic?id=2"))
                .build();
        String test = "Epic-задача под номером 2 удалена";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteSubtaskTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/subtask"))
                .build();
        String test = "Все Subtask-задачи удалены";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteSubtaskIdTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/subtask?id=3"))
                .build();
        String test = "Subtask-задача под номером 3 удалена";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteTaskTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/task"))
                .build();
        String test = "Все Task-задачи удалены";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void DeleteTaskIdTest() {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "tasks/task?id=1"))
                .build();
        String test = "Task-задача под номером 1 удалена";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode(), "Код ответа не совпадает");
            Assertions.assertEquals(test, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
