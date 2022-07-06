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
    public LinkedList<Task> getPrioritizedTasks() {
        return new LinkedList<>(sortTaskTime.values());
    }

    public void intersectionsOfTime(LinkedList<Task> task) {
        LocalDateTime endTime = getEndTime(task.get(0));
        int numberOfIntersections = 0;
        for (int i = 1; i < task.size(); i++) {
            if(endTime.isAfter(getEndTime(task.get(i)))) {
                numberOfIntersections++;
                endTime = getEndTime(task.get(i));
            }
        }
        if (numberOfIntersections > 0) {
            System.out.println("Есть пересечение по времени задач");
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
    public void addTask(Task task){
        tasks.put(task.getTaskId(), task);
        sortTaskTime.put(task.getStartTime(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        sortTaskTime.put(subtask.getStartTime(), subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getTaskId());
        updateEpicStatus(subtask.getEpicId());
        calculationDurationEpic(epics.get(subtask.getEpicId()));
        startDateTimeEpic(epics.get(subtask.getEpicId()));
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
        if (tasks.get(id) == null && epics.get(id) == null) {
            value = subtasks.get(id);
            historyManager.add(value);
        } else if (tasks.get(id) == null) {
            value = epics.get(id);
            historyManager.add(value);
        } else {
            value = tasks.get(id);
            historyManager.add(value);
        }
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
        return value;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        if(!tasks.containsKey(task.getTaskId()) || id != task.getTaskId()) {
            return;
        } else {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        } else {
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getEpicId());
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
            tasks.remove(id);
        }
    }

    @Override
    public void deleteValueEpicById(Integer id){
        if (epics.containsKey(id)) {
            for (Integer idSubtask : epics.get(id).getSubtaskIds()) {
                historyManager.remove(idSubtask);
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
            subtasks.remove(id);
            updateEpicStatus(epicId);
            calculationDurationEpic(epics.get(epicId));
            startDateTimeEpic(epics.get(epicId));
        }
    }

    @Override
    public String toString(Task task) {
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void save() throws IOException {
    }

    public void updateEpicStatus(Integer idEpic) {
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

    public void calculationDurationEpic(Epic epic) {
        long duraionEpic = 0;
        for(Integer idSubtask : epic.getSubtaskIds()) {
            duraionEpic += subtasks.get(idSubtask).getDuration();
        }
        epic.setDuration(duraionEpic);
    }

    public void startDateTimeEpic(Epic epic) {
        if(!epic.getSubtaskIds().isEmpty()) {
            LocalDateTime startEpicTime = (subtasks.get(epic.getSubtaskIds().get(0)).getStartTime());
            for (Integer idSubtask : epic.getSubtaskIds()) {
                if (subtasks.get(idSubtask).getStartTime() == null) {
                    break;
                } else if (subtasks.get(idSubtask).getStartTime().isBefore(startEpicTime)) {
                    startEpicTime = subtasks.get(epic.getSubtaskIds().get(idSubtask)).getStartTime();
                }
            }
            epic.setStartTime(startEpicTime);
        } else {
            return;
        }
    }

    public LocalDateTime getEndTime(Task task) {
        if(task.getType().equals(TypeTask.EPIC)) {
            startDateTimeEpic((Epic) task);
            calculationDurationEpic((Epic) task);
            LocalDateTime startTime = task.getStartTime();
            return startTime.plusMinutes(task.getDuration());
        } else {
            return task.getStartTime().plusMinutes(task.getDuration());
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}