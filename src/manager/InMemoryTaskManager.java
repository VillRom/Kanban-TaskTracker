package manager;

import model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private Map<LocalDateTime, Task> sortTaskTime = new TreeMap<>(new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            if (o1 == null){
                return 1;
            }else if(o2 == null){
                return -1;
            }else if(o1 == null & o2 == null){
                return 0;
            }else {
                return o1.compareTo(o2);
            }
        }
    });

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getPrioritizedTasks() {
        return new LinkedList<>(sortTaskTime.values());
    }

    private void intersectionsOfTime(Task task) throws IOException {
        for (Task value : sortTaskTime.values()) {
            if (task.getStartTime().isBefore(value.getEndTime()) & task.getStartTime().isAfter(value.getStartTime())) {
                throw new IOException("Пересечение по времени выполнения c задачей - " + value.getName());
            } else if (task.getEndTime().isBefore(value.getEndTime()) & task.getEndTime().
                    isAfter(value.getStartTime())) {
                throw new IOException("Пересечение по времени выполнения c задачей - " + value.getName());
            } else if (task.getEndTime().isAfter(value.getEndTime()) & task.getStartTime().
                    isBefore(value.getStartTime())) {
                throw new IOException("Пересечение по времени выполнения c задачей - " + value.getName());
            }
        }
    }

    @Override
    public ArrayList getSubtaskFromEpic(Integer id) {
        ArrayList<Subtask> listSubtaskFromEpic = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtaskIds()) {
            listSubtaskFromEpic.add(subtasks.get(idSubtask));
        }
        return listSubtaskFromEpic;
    }

    @Override
    public void addTask(Task task) throws IOException {
        intersectionsOfTime(task);
        tasks.put(task.getTaskId(), task);
        sortTaskTime.put(task.getStartTime(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        intersectionsOfTime(subtask);
        subtasks.put(subtask.getTaskId(), subtask);
        sortTaskTime.put(subtask.getStartTime(), subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getTaskId());
        updateEpicStatus(subtask.getEpicId());
        calculatingTheEpicExecutionTime(subtask.getEpicId());
    }

    @Override
    public ArrayList getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public void deleteAllTheTasksInListTask() {
        for (Integer keys: tasks.keySet()) {
            historyManager.remove(keys);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllTheTasksInListSubtask() {
        for (Integer keys: subtasks.keySet()) {
            historyManager.remove(keys);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
        }
        for (Integer idEpic : epics.keySet()) {
            epics.get(idEpic).setStatus(StatusTasks.NEW);
            epics.get(idEpic).setStartTime(null);
            epics.get(idEpic).setDuration(0);
        }
    }

    @Override
    public void deleteAllTheTasksInListEpic() {
        for (Integer keys: epics.keySet()) {
            historyManager.remove(keys);
        }
        for (Integer keys: subtasks.keySet()) {
            historyManager.remove(keys);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getValueById(Integer id) {
        Task value;
        if (tasks.get(id) == null && epics.get(id) == null && subtasks.get(id) != null) {
            value = subtasks.get(id);
            historyManager.add(value);
        } else if (tasks.get(id) == null && epics.get(id) != null) {
            value = epics.get(id);
            historyManager.add(value);
        } else if (tasks.get(id) != null){
            value = tasks.get(id);
            historyManager.add(value);
        } else {
            value = null;
        }
        return value;
    }

    @Override
    public void updateTask(Integer id, Task task) throws IOException {
        if(!tasks.containsKey(task.getTaskId()) || id != task.getTaskId()) {
            return;
        } else {
            sortTaskTime.remove(tasks.get(id).getStartTime());
            intersectionsOfTime(task);
            tasks.put(id, task);
            sortTaskTime.put(task.getStartTime(), task);
        }
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) throws IOException {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        } else {
            sortTaskTime.remove(subtasks.get(id).getStartTime());
            intersectionsOfTime(subtask);
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getEpicId());
            calculatingTheEpicExecutionTime(subtask.getEpicId());
            sortTaskTime.put(subtask.getStartTime(), subtask);
        }

    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        if (!epics.containsKey(epic.getTaskId()) || id != epic.getTaskId()) {
            return;
        } else {
            ArrayList<Integer> subtaskIds = epics.get(id).getSubtaskIds();
            epic.setSubtaskIds(subtaskIds);
            epics.put(id, epic);
        }
    }

    @Override
    public void deleteValueTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            sortTaskTime.remove(tasks.get(id).getStartTime());
            tasks.remove(id);
        }
    }

    @Override
    public void deleteValueEpicById(Integer id){
        if (epics.containsKey(id)) {
            for (Integer idSubtask : epics.get(id).getSubtaskIds()) {
                historyManager.remove(idSubtask);
                sortTaskTime.remove(subtasks.get(idSubtask).getStartTime());
                subtasks.remove(idSubtask);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteValueSubtaskById(Integer id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            historyManager.remove(id);
            epics.get(epicId).getSubtaskIds().remove(id);
            sortTaskTime.remove(subtasks.get(id).getStartTime());
            subtasks.remove(id);
            updateEpicStatus(epicId);
            calculatingTheEpicExecutionTime(epicId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Integer idEpic) {
        int amountSubtaskNew = 0;
        int amountSubtaskDone = 0;
        for (Integer cheak : epics.get(idEpic).getSubtaskIds()) {
            if (subtasks.get(cheak).getStatus() == StatusTasks.NEW) {
                amountSubtaskNew++;
            } else if (subtasks.get(cheak).getStatus() == StatusTasks.DONE) {
                amountSubtaskDone++;
            }
        }
        if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskNew) {
            epics.get(idEpic).setStatus(StatusTasks.NEW);
        } else if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskDone) {
            epics.get(idEpic).setStatus(StatusTasks.DONE);
        } else {
            epics.get(idEpic).setStatus(StatusTasks.IN_PROGRESS);
        }
    }

    private void calculatingTheEpicExecutionTime(Integer idEpic) {
        if (!epics.get(idEpic).getSubtaskIds().isEmpty()) {
            LocalDateTime startEpicTime = subtasks.get(epics.get(idEpic).getSubtaskIds().get(0)).getStartTime();
            LocalDateTime endEpicTime = subtasks.get(epics.get(idEpic).getSubtaskIds().get(0)).getEndTime();
            long durationEpic = 0;
            for (Integer check : epics.get(idEpic).getSubtaskIds()) {
                if (subtasks.get(check).getStartTime().isBefore(startEpicTime)) {
                    startEpicTime = subtasks.get(check).getStartTime();
                }
                if (subtasks.get(check).getEndTime().isAfter(endEpicTime)) {
                    endEpicTime = subtasks.get(check).getEndTime();
                }
                durationEpic += subtasks.get(check).getDuration();
            }
            epics.get(idEpic).setDuration(durationEpic);
            epics.get(idEpic).setStartTime(startEpicTime);
            epics.get(idEpic).setEndTime(startEpicTime.plusMinutes(durationEpic));
        }
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }
}