package smarthome;

class SecurityCamera implements SmartDevice {
    private boolean isOn = false;

    public void activate() {
        isOn = true;
        System.out.println("Security camera is recording.");
    }

    public void deactivate() {
        isOn = false;
        System.out.println("Security camera stopped.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (isOn) {
                    System.out.println("Monitoring area...");
                }
                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
            System.out.println("Security Camera interrupted.");
        }
    }
}

