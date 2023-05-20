import java.util.*;

public class Scheduler {

    Queue<Process> readyQueue = new LinkedList<>();
    Queue<Process> blockedQueue = new LinkedList<>();
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
    public void roundRobin(List<Process> processes, int timeQuantum) {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;

        // Add all processes to the queue
        for (Process process : processes) {
            queue.add(process);
        }

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            // Determine the time to execute for the current time quantum or remaining time
            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());

            // Simulate process execution
            currentTime += executionTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);

            // Check if the process has finished executing
            if (currentProcess.getRemainingTime() > 0) {
                // Add the process back to the queue for further execution
                queue.add(currentProcess);
            } else {
                System.out.println("Process " + currentProcess.getId() + " completed at time " + currentTime);
            }
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


}

//    public Scheduler(int timeSlice) {
//        this.readyQueue = new LinkedList<>();
//        this.blockedQueue = new LinkedList<>();
//        this.timeSlice = timeSlice;
//    }








