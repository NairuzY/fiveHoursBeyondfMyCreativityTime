import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Process {
    private int remainingTime; //should we keep?
    boolean inMemory = true;
    int address; //remember to change whenever swapped out and in

    public Process(int address){
        this.address = address;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}

