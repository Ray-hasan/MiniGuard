import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;

public class MiniGuard {

    /* distance sensor will work by measuring the distance between the sensor and the door. When the distance is
    shorter because the door is opened, the alarm triggers. put all of this in a timer task action class.
    alarm will work by flashing an LED. pressing the button stops everything.
     */

    /* NEW
    Distance sensor will measure the distance between the sensor and the door and will activate if an object is detected
    within the designated active zone in order to prevent unintended alarm triggers. The alarm will wrk by both flashing
    an LED and by having a buzzer play a sound. A button press stops everything. THe rotation of a potentiometer turns
    on the alarm.
     */

    static final int A1	= 15;	//	Sensor
    static final int A0	= 14;	//	Potentiometer
    static final int D5	= 5;	//	Sound
    static final int D6	= 6;	//	Button
    static final int D4	= 4;	//	LED
    static final byte I2C0 = 0x3C;	//	OLED	Display

    public static void main(String[] args)
            throws InterruptedException, IOException {

        IODevice ard = new FirmataDevice("COM3"); // Board object, using the name of a port
        ard.start();
        ard.ensureInitializationIsDone();


        var button = ard.getPin(D6);
        button.setMode(Pin.Mode.INPUT);

        var sensor = ard.getPin(A1);
        sensor.setMode(Pin.Mode.ANALOG);

        var Switch = ard.getPin(A0);
        Switch.setMode(Pin.Mode.ANALOG);

        var sound = ard.getPin(D5);
        sound.setMode(Pin.Mode.OUTPUT);

        var LED = ard.getPin(D4);
        LED.setMode(Pin.Mode.OUTPUT);
        LED.setValue(0);


        I2CDevice i2cObject = ard.getI2CDevice(I2C0);
        SSD1306 myOledDisplay = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        myOledDisplay.init();

        //Initialize pins and OLED display ^^

        System.out.println("Alarm system is inactive.");

        myOledDisplay.getCanvas().clear();

        myOledDisplay.getCanvas().drawString(0,28, " You are currently \n defenseless.");
        myOledDisplay.display();

        //Indicates that the alarm has not been set and that it needs to be armed ^^^

        Alarm task = new Alarm(myOledDisplay, sensor, Switch, LED, sound, button);

        new Timer().schedule(task, 0, 500);

        Off_Button stop = new Off_Button(myOledDisplay, LED, button, sound, task);
        ard.addEventListener(stop);

        myOledDisplay.getCanvas().clear();

    }

}
