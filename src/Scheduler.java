import java.io.*;
import java.util.*;

public class Scheduler {

    static Queue<Process> readyQueue = new LinkedList<>();
    static Process runningProcess = null;
    static Hashtable<String,Process> blockedQueue = new Hashtable<>();
    static Queue<String> blockedOnScreen = new LinkedList<>();
    static Queue<String> blockedOnTakingInput = new LinkedList<>();
    static Queue<String> blockedOnFile = new LinkedList<>();
    Hashtable<Integer,Integer> programs= new Hashtable<>();
    Memory memory = new Memory();
    int processesCount=0;
    int currentTime = 0;
    static int semFile = 1;
    static int semScreen = 1;
    static int semInput = 1;
    private int timeSlice = Integer.parseInt(getVal("slice"));
    public void choose(){

    }

    public void block(Process process, Blocking reason){

        Memory.stack[process.getAddress()+1] = "BLOCKED";
        Scheduler.blockedQueue.put(Memory.stack[process.getAddress()],Scheduler.runningProcess);
        System.out.println("Scheduler block process with id "+process.getId());
    }

    public void terminate(){

    }

    public void printQueue(Queue<Process> queue){
        //for()
    }

    // Implement Round Robin scheduling algorithm
//    public void roundRobin(List<Process> processes, int timeQuantum) {
//        Queue<Process> queue = new LinkedList<>();
//
//
//        // Add all processes to the queue
//        for (Process process : processes) {
//            queue.add(process);
//        }
//
//        while (!queue.isEmpty()) {
//            Process currentProcess = queue.poll();
//
//            // Determine the time to execute for the current time quantum or remaining time
//            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
//
//            // Simulate process execution
//            currentTime += executionTime;
//            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
//
//            // Check if the process has finished executing
//            if (currentProcess.getRemainingTime() > 0) {
//                // Add the process back to the queue for further execution
//                queue.add(currentProcess);
//            } else {
//                System.out.println("Process " + currentProcess.getId() + " completed at time " + currentTime);
//            }
//        }
//    }
    public void storeInMemory(String[] values, Process process) throws IOException {
        int min = memory.allocateMemory();
        process.setAddress(min);
        int max = min+19;
        System.arraycopy(values, 0, Memory.stack, min, values.length);
        memory.setStack(""+min,min+3);
        memory.setStack(""+max,min+4);
        Memory.getInMemory().add(process.getId());
    }
    public void toMemory(String fileName,int processId) throws IOException {
        Process process = new Process(processId+"");
        String[] values = new String[20];
        values[0] = ""+processId;
        values[1] = "READY";
        values[2] = "0";
        for(int i=3; i<8; i++){
            values[i] = null;
        }
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        for(int i=8; i<20; i++){
            String line=br.readLine();
            values[i] = line;
        }
        storeInMemory(values, process);
    }

    public void cycle() throws IOException {
        for( ; currentTime<100 ;currentTime++){ //to be handled
            if(programs.get(currentTime)!=null){
                toMemory("Program_"+programs.get(currentTime)+".txt",programs.get(currentTime));

            }
        }
    }
   public void runScheduler() throws IOException {
        if(runningProcess == null)
            if(!readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
                if(!Memory.getInMemory().contains(runningProcess.getId())){
                    String[] values = retrieveFromDisk(runningProcess.getId());
                    storeInMemory(values,runningProcess);
                }



            }
        Memory.stack[runningProcess.getAddress() + 1] = "RUNNING";
        timeSlice --;
        int iAddress = runningProcess.getAddress() + 5 + Integer.parseInt(Memory.stack[runningProcess.getAddress() + 2]);
        Parser.execute(Memory.stack[iAddress], runningProcess.getAddress());
        if(!Parser.dontMove){
            iAddress++;
            int max = Integer.parseInt(Memory.stack[runningProcess.getAddress() + 4]);
            if(Memory.stack[iAddress] == null || iAddress > max) {
                terminate();
                return;
            }
            Memory.stack[runningProcess.getAddress() + 2] = iAddress - runningProcess.getAddress() - 5 +"";
            if(timeSlice == 0){
                readyQueue.add(runningProcess);
                runningProcess= null;
            }
        }

            // Check if the process is blocked or finished
//            if (currentProcess.getInfo().getState() == ProcessState.BLOCKED) {
//                blockedQueue.add(currentProcess);
//            } else if (currentProcess.getInfo().getState() != ProcessState.TERMINATED) {
//                readyQueue.add(currentProcess);
//            }

            // Print queues after scheduling event
            printQueues();
    }


    private void setRunning(Process process){
        timeSlice = Integer.parseInt(getVal("slice"));
        if(Memory.getInMemory().contains(process.getId())){

        }
    }
    private void printQueues() {
        System.out.println("Ready Queue: " + readyQueue);
        System.out.println("Blocked Queue: " + blockedQueue);
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }
    private void executeInstruction(Process process, String instruction) {
        // Execute the instruction based on the process
        // This is just a placeholder and you need to replace it with your own logic
        System.out.println("Executing instruction: " + instruction + " for process: " + process.getId());
    }
    public String[] retrieveFromDisk(String id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/resources/Disk.txt"));
        String line;
        String[] values = new String[20];
        values[0] = id;
        while((line = br.readLine())!=null){
            if(line.equals(id)){
                for(int i=1; i<20; i++){
                    line = br.readLine();
                    values[i] = line.equals("null")?null:line;
                }
                return values;
            }
        }
        return null;
    } //while uploading to disk, make sure to upload 20 values even if it'll be null
    public static String getVal(String key) {
        String keyval = null;
        File file = new File("src/resources/os.config");
        Properties prop = new Properties();
        FileInputStream objInput;
        try{
            objInput = new FileInputStream(file);
            prop.load(objInput);
            keyval = prop.getProperty(key);
            objInput.close();
        }catch(Exception e){System.out.println(e.getMessage());}
        return keyval;
        }

    }

//    public Scheduler(int timeSlice) {
//        this.readyQueue = new LinkedList<>();
//        this.blockedQueue = new LinkedList<>();
//        this.timeSlice = timeSlice;
//    }








