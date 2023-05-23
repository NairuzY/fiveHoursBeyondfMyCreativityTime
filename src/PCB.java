public class PCB {
    
    private int PID=0;
    private ProcessState state;
    private int PC;
    private int min;
    private int max;
    
    public PCB(int id, int min, int max){
        PID=id;
        state=ProcessState.READY;
        PC=0;
        this.min=min;
        this.max=max;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    

}
