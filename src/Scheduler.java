import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Scheduler {

    Queue<Process> readyQueue = new LinkedList<>();
    Queue<Process> blockedQueue = new LinkedList<>();
    Queue<Process> blockedOnScreen = new LinkedList<>();
    Queue<Process> blockedOnTakingInput = new LinkedList<>();
    Queue<Process> blockedOnFile = new LinkedList<>();
    Hashtable<Integer,Integer> programs= new Hashtable<>();
    Memory memory = new Memory();
    int processesCount=0;

    int currentTime = 0;
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

    public void toMemory(String fileName){

//will create a new pcb then a new process


    }

    public void runScheduler() {
        // Print queues before scheduling event
        printQueues();

        while (!readyQueue.isEmpty()) {
          if(programs.get(currentTime)!=null){
              toMemory("programs_"+programs.get(currentTime)+".txt");
          }
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








