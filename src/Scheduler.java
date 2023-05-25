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
    public static Memory memory = new Memory();
    int processesCount=0;
    int currentTime = 0;
    static int semFile = 1;
    static int semScreen = 1;
    static int semInput = 1;
    private int timeSlice = Integer.parseInt(getVal("slice"));
    public boolean choose() throws IOException {
        if(!readyQueue.isEmpty()) {
            runningProcess = readyQueue.poll();
            timeSlice = Integer.parseInt(getVal("slice"));
            if(!memory.getInMemory().contains(runningProcess.getId())){//3amalet null pointerexception
                String[] values = retrieveFromDisk(runningProcess.getId());
                storeInMemory(values,runningProcess);
            }
            memory.stack[runningProcess.getAddress() + 1] = "RUNNING";
            printQueues();
            return true;
        }
        return false;
    }
    public static void block(Blocking reason){
        memory.stack[runningProcess.getAddress() + 1] = "BLOCKED";
        Scheduler.blockedQueue.put(memory.stack[runningProcess.getAddress()],Scheduler.runningProcess);
        if(reason == Blocking.Input)
            Scheduler.blockedOnTakingInput.add(memory.stack[runningProcess.getAddress()]);
        else if(reason == Blocking.File)
            Scheduler.blockedOnFile.add(memory.stack[runningProcess.getAddress()]);
        else
            Scheduler.blockedOnScreen.add(memory.stack[runningProcess.getAddress()]);
        readyQueue.remove(runningProcess);
        System.out.println("Scheduler blocked process with id "+runningProcess.getId());
        runningProcess = null;
        printQueues();
    }
    public void terminate(){
        System.out.println("Scheduler terminated process with id "+runningProcess.getId());
        runningProcess = null;
        printQueues();
    }

    public void storeInMemory(String[] values, Process process) throws IOException {
        int min = memory.allocateMemory();
        System.out.println(min+"Toooooooooooooooooz");
        process.setAddress(min);
        int max = min+19;
        System.arraycopy(values, 0, memory.stack, min, values.length);
        memory.setStack(""+min,min+3);
        memory.setStack(""+max,min+4);
        memory.getInMemory().add(memory.getStack()[min]+"");

    }
    public Process toMemory(String fileName,int processId) throws IOException {
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
        return process;

    }

    public void cycle() throws IOException {
        programs.put(Integer.parseInt(getVal("program1")),1);
        programs.put(Integer.parseInt(getVal("program2")),2);
        programs.put(Integer.parseInt(getVal("program3")),3);
        while(true){
            System.out.println("Memory in start of cycle "+currentTime + " is "+ "\n"+memory.toString());
            if(programs.get(currentTime)!=null){
                Process p=toMemory("src/resources/Program_"+programs.get(currentTime)+".txt",programs.get(currentTime));
                readyQueue.add(p);
                memory.getStack()[p.getAddress()+1]="READY";
                processesCount++;
            }
            Boolean finished=runScheduler();

            timeSlice--;
            if(timeSlice == 0){
                //running process b2et null
               if(runningProcess!=null) {
                   memory.getStack()[runningProcess.getAddress() + 1] = "READY";
                   readyQueue.add(runningProcess);
                   runningProcess = null;
               }
            }
            currentTime++;
            if(finished && processesCount==3)
                break;

        }
    }
   public Boolean runScheduler() throws IOException {
        if(runningProcess == null){
            if(!choose())
                return true; //handle whole program termination or change this
        }

        int nextInstruction = runningProcess.getAddress() + 8 + Integer.parseInt(memory.stack[runningProcess.getAddress() + 2]);
        int max = Integer.parseInt(memory.stack[runningProcess.getAddress() + 4]);
            if(memory.stack[nextInstruction] == null || nextInstruction > max) { //keda one cycle will be wasted
                terminate();
                runScheduler();
            }
            execute();

return false;

            // Check if the process is blocked or finished
//            if (currentProcess.getInfo().getState() == ProcessState.BLOCKED) {
//                blockedQueue.add(currentProcess);
//            } else if (currentProcess.getInfo().getState() != ProcessState.TERMINATED) {
//                readyQueue.add(currentProcess);
//            }
    }
    private static void printQueues() {
        System.out.println("Ready Queue: " + readyQueue);
        System.out.println("Blocked Queue: "+blockedQueue);
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }
    private void execute() throws IOException {
        int iAddress = runningProcess.getAddress() + 8 + Integer.parseInt(memory.stack[runningProcess.getAddress() + 2]);
        System.out.println("Executing instruction: " + memory.stack[iAddress] + " for process: " + runningProcess.getId());
        Parser.execute(memory.stack[iAddress]);
        if(!Parser.dontMove) {
            iAddress++;
            memory.stack[runningProcess.getAddress() + 2] = iAddress - runningProcess.getAddress() - 8 +"";
        }
    }
    public void updateDisk(String[] values) throws IOException {
        FileReader oldDisk = new FileReader("src/resources/Disk.txt");
        BufferedReader br = new BufferedReader(oldDisk);
        StringBuilder newDisk = new StringBuilder();
        String curLine = br.readLine();
        boolean flag=false;
        int i=0;
        while (i<values.length){
            while (curLine != null) {
                if(i<values.length){
                if(curLine.equals(values[i])){
                    i++;
                    flag=true;
                }
                else{
                    newDisk.append(curLine).append("\n");
                    }
                }
                curLine = br.readLine();
            }
        }
//        while (curLine != null) {
//             newDisk.append(curLine).append("\n");
//             curLine = br.readLine();
//         }
        FileWriter Disk = new FileWriter("src/resources/Disk.txt");
        Disk.write(newDisk.toString());
        Disk.close();
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
                System.out.println("Swapped out of disk process with id= "+values[0]);
                updateDisk(values);
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
        public static void main(String[] args) throws IOException {
        //pls clear disk.txt file before running
           FileWriter Disk = new FileWriter("src/resources/Disk.txt");
              Disk.write("");
                Disk.close();
        Scheduler scheduler = new Scheduler();
        scheduler.cycle();
        }

    }

//    public Scheduler(int timeSlice) {
//        this.readyQueue = new LinkedList<>();
//        this.blockedQueue = new LinkedList<>();
//        this.timeSlice = timeSlice;
//    }








