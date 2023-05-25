import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Process {
    private int remainingTime; //should we keep?
//    private boolean inMemory = true;

    private int address; //remember to change whenever swapped out and in
    private String id;


    public Process(String id){
        this.id = id;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
//    public boolean isInMemory() {
//        return inMemory;
//    }
//
//    public void setInMemory(boolean inMemory) {
//        this.inMemory = inMemory;
//    }
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    public String getId() {
        return id;
    }

}

