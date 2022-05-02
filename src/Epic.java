import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Subtask> getListSubtask() {
        return listSubtask;
    }
}
