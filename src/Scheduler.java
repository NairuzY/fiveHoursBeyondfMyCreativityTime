import java.io.*;
import java.util.*;

public class Scheduler {

    static Hashtable<String,Process> readyQueue = new Hashtable<>();
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

    public void block(){

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

    public void toMemory(String fileName,int processId) throws IOException {
        int min = memory.allocateMemory();
        int max = min+19;
        PCB pcb=new PCB(processId,min,max);
        int index=min;
        memory.setStack(""+processId,index++);
        memory.setStack(""+"READY",index++);
        memory.setStack("",index++);
        memory.setStack(""+min,index++);
        memory.setStack(""+max,index++);
        index+=3;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line="";
        while((line=br.readLine())!=null)
            memory.setStack(line,index++);
    }

    public void cycle() throws IOException {
        for( ; currentTime<100 ;currentTime++){ //to be handled
            if(programs.get(currentTime)!=null){
                toMemory("Program_"+programs.get(currentTime)+".txt",programs.get(currentTime));

            }
        }
    }
    public void interpreter(int pc){
        String instruction=
    }

    public void runScheduler() {
        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            // Execute instructions for the current process within the time slice
            for (int i = 0; i < timeSlice; i++) {
                if (currentProcess.hasMoreInstructions()) {
                    String instruction = currentProcess.getNextInstruction();
                    executeInstruction(currentProcess, instruction);
                } else {
                    break;
                }
            }

            // Check if the process is blocked or finished
            if (currentProcess.getInfo().getState() == ProcessState.BLOCKED) {
                blockedQueue.add(currentProcess);
            } else if (currentProcess.getInfo().getState() != ProcessState.TERMINATED) {
                readyQueue.add(currentProcess);
            }

            // Print queues after scheduling event
            printQueues();
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


    public static String getVal(String key)
    {
        String keyval = null;
        //Let's consider properties file is in project folder itself

        File file = new File("src/Resources/DBApp.config");

        //Creating properties object
        Properties prop = new Properties();
        //Creating InputStream object to read data
        FileInputStream objInput = null;
        try{
            objInput = new FileInputStream(file);
            //Reading properties key/values in file
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








