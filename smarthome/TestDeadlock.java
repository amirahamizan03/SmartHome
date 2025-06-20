package smarthome;

public class TestDeadlock {
    public static void main(String[] args) {
        DeadlockController controller = new DeadlockController();
        controller.startDeadlock();
    }
}
