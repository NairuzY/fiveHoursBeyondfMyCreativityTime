public class Process {
    private int address;
    private final String id;
    private String temp;
    public Process(String id){
        this.id = id;
    }
    public boolean dontMove = false;
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
}

