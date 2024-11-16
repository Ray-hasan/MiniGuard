import	org.firmata4j.Pin;
import	org.firmata4j.ssd1306.SSD1306;
import	java.io.IOException;
import	java.util.TimerTask;
public class Alarm extends TimerTask {
    private	final SSD1306 myOled;
    private	final Pin LED;
    private final Pin Sound;
    private final Pin sensor;
    private final Pin Switch;
    private final Pin button;
    int counter = 0;
    double close = 521.0; // 14 cm
    double open = 507.0; // 11 cm
    double m = (11.0-14.0)/(open-close);
    double b = 14.0-(m*close);
    //	class	constructor.
    public	Alarm(SSD1306	display, Pin sensor, Pin Switch, Pin LED, Pin sound, Pin button)	{
        this.myOled = display;
        this.sensor = sensor;
        this.Switch = Switch;
        this.LED = LED;
        this.Sound = sound;
        this.button = button;
    }
    @Override
    public	void	run() {

        counter++;

        if (Switch.getValue() > 100) {
            System.out.println("Alarm system is now active.");
            myOled.getCanvas().clear();
            myOled.getCanvas().drawString(0, 28, " Alarm has been set.");
            myOled.display();
            double distance = sensor.getValue();
            var d = m*distance + b;

            //ON switch ^^^

            if (d < 14 && d > 11) {
                myOled.getCanvas().clear();
                System.out.println("ALARM HAS BEEN TRIPPED");
                myOled.getCanvas().drawString(0, 28, " Call an ambulance, \n but not for me.");
                myOled.display();

                // Active zone set where the alarm only gets triggered if an object is found between 14 cm and 11 cm ^^^

                while (button.getValue() == 0) {
                    System.out.println("ALARM HAS BEEN TRIPPED");
                    try {
                        LED.setValue(1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        LED.setValue(0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Sound.setValue(1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Code that runs when the alarm is tripped ^^^
                }
            }
        }
    }
}

