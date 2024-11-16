import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;

public class Off_Button implements IODeviceEventListener {
    private final Pin LED;
    private final Pin buttonPin;
    private final Pin Sound;
    private final SSD1306 display;
    private final Alarm task;
    Off_Button(SSD1306 display, Pin LED, Pin buttonPin, Pin Sound, Alarm task) {
        this.buttonPin = buttonPin;
        this.LED = LED;
        this.Sound = Sound;
        this.display = display;
        this.task = task;
    }
    @Override
    public void onPinChange(IOEvent event) {
        // Return right away if the even isn't from the Button.
        if (event.getPin().getIndex() != buttonPin.getIndex()) {
            return;
        }
        if (buttonPin.getValue() == 1){
            System.out.println("Alarm system has been deactivated.");
            display.getCanvas().clear();
            display.getCanvas().drawString(0,28, " Alarm has been \n Disarmed.");
            display.display();
            task.cancel();
            try {
                LED.setValue(0);
                Sound.setValue(0);

                task.cancel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Turns off alarm system ^^^

        }
    }
    // These are empty methods (nothing in the curly braces)
    @Override
    public void onStart(IOEvent event) {}
    @Override
    public void onStop(IOEvent event) {}
    @Override
    public void onMessageReceive(IOEvent event, String message) {}
}