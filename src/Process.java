import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Process {
    private int remainingTime; //should we keep?
//    private boolean inMemory = true;
    private int address; //remember to change whenever swapped out and in
    private  String id;
    private String temp;
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
    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }


    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                '}';
    }
    public void setId(String id) {
        this.id=id;
    }
}

