package smarthome;

public class AutomationRule {
    private String ruleId;
    private String trigger;
    private String action;
    private Device device;

    public AutomationRule(String ruleId, String trigger, String action, Device device) {
        this.ruleId = ruleId;
        this.trigger = trigger;
        this.action = action;
        this.device = device;
    }

    public void applyRule() {
        System.out.println("Applying rule: " + ruleId);
        if (action.equalsIgnoreCase("turnOn")) {
            device.turnOn();
        } else if (action.equalsIgnoreCase("turnOff")) {
            device.turnOff();
        }
    }
}
