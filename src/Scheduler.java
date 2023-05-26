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
    static int processesCount=0;
    int currentTime = 0;
    static int semFile = 1;
    static int semScreen = 1;
    static int semInput = 1;
    private int timeSlice = Integer.parseInt(getVal("slice"));
    public boolean choose() throws IOException {
        if(!readyQueue.isEmpty()) {
            runningProcess = readyQueue.poll();
            timeSlice = Integer.parseInt(getVal("slice"));
            if(!Memory.getInMemory().contains(runningProcess.getId())){
                String[] values = retrieveFromDisk(runningProcess.getId());
                storeInMemory(values,runningProcess);
            }
            Memory.stack[runningProcess.getAddress() + 1] = "RUNNING";
            System.out.println("Scheduler chose process "+runningProcess.getId());
            printQueues();
            return true;
        }
        return false;
    }
    public static void block(Blocking reason){
        Memory.stack[runningProcess.getAddress() + 1] = "BLOCKED";
        Scheduler.blockedQueue.put(Memory.stack[runningProcess.getAddress()],Scheduler.runningProcess);
        if(reason == Blocking.Input)
            Scheduler.blockedOnTakingInput.add(Memory.stack[runningProcess.getAddress()]);
        else if(reason == Blocking.File)
            Scheduler.blockedOnFile.add(Memory.stack[runningProcess.getAddress()]);
        else
            Scheduler.blockedOnScreen.add(Memory.stack[runningProcess.getAddress()]);
        readyQueue.remove(runningProcess);
        System.out.println("Scheduler blocked process with id "+runningProcess.getId());
        int iAddress = runningProcess.getAddress() + 8 + Integer.parseInt(Memory.stack[runningProcess.getAddress() + 2])+1;
        findInstruction(iAddress);
        runningProcess = null;
        printQueues();
    }
    public static void terminate(){
        System.out.println("Scheduler terminated process with id "+runningProcess.getId());
        Memory.stack[runningProcess.getAddress()+1] = "TERMINATED";
        runningProcess = null;
        printQueues();
        processesCount++;
    }
    public void storeInMemory(String[] values, Process process) throws IOException {
        int min = memory.allocateMemory();
        process.setAddress(min);
        int max = min+19;
        System.arraycopy(values, 0, Memory.stack, min, values.length);
        memory.setStack(""+min,min+3);
        memory.setStack(""+max,min+4);
        Memory.getInMemory().add(memory.getStack()[min]+"");

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
            System.out.println();
            System.out.println("Memory in start of cycle "+currentTime + " is "+ "\n"+memory.toString());
            if(programs.get(currentTime)!=null){
                Process p=toMemory("src/resources/Program_"+programs.get(currentTime)+".txt",programs.get(currentTime));
                readyQueue.add(p);
                memory.getStack()[p.getAddress()+1]="READY";
            }
            if(timeSlice == 0){
                if(runningProcess!=null) {
                    memory.getStack()[runningProcess.getAddress() + 1] = "READY";
                    readyQueue.add(runningProcess);
                    runningProcess = null;
                }
            }
            if(runScheduler() && processesCount==3)
                return;
            timeSlice--;
            currentTime++;
        }
    }
   public Boolean runScheduler() throws IOException {
        if(runningProcess == null){
            if(!choose())
                return true;
        }

        execute();
        return false;
    }
    private static void printQueues() {
        System.out.println("Ready Queue: " + readyQueue);
        System.out.println("Blocked Queue: "+blockedQueue);
        System.out.println("Blocked on Taking Input Queue: "+ blockedOnTakingInput);
        System.out.println("Blocked on Accessing Files: "+ blockedOnFile);
        System.out.println("Blocked on Accessing Screen: "+ blockedOnScreen);
    }

    private void execute() throws IOException {
        int iAddress = runningProcess.getAddress() + 8 + Integer.parseInt(Memory.stack[runningProcess.getAddress() + 2]);
        System.out.println("Executing instruction: " + Memory.stack[iAddress] + " for process: " + runningProcess.getId());
        Parser.execute(Memory.stack[iAddress]);
        if(runningProcess!=null && !runningProcess.dontMove) {
            iAddress++;
            findInstruction(iAddress);
        }
    }

    private static void findInstruction(int iAddress) {
        Memory.stack[runningProcess.getAddress() + 2] = iAddress - runningProcess.getAddress() - 8 +"";
        int nextInstruction = runningProcess.getAddress() + 8 + Integer.parseInt(Memory.stack[runningProcess.getAddress() + 2]);
        int max = Integer.parseInt(Memory.stack[runningProcess.getAddress() + 4]);
        if(Memory.stack[nextInstruction] == null || nextInstruction > max) {
            terminate();
//                if(runScheduler())
//                    return true;
        }
    }

    public void updateDisk(String[] values) throws IOException {
        FileReader oldDisk = new FileReader("src/resources/Disk.txt");
        BufferedReader br = new BufferedReader(oldDisk);
        StringBuilder newDisk = new StringBuilder();
        String curLine = br.readLine();
        while(curLine!=null){
            if(curLine.equals(values[0])){
                for(int i=1; i<20; i++){
                    br.readLine();
                }
            }
            else
                newDisk.append(curLine).append("\n");
            curLine = br.readLine();
        }
        File itsANewFile = new File("src/resources/Disk.txt");
        FileWriter Disk = new FileWriter(itsANewFile);
        Disk.write(newDisk.toString());
        Disk.close();
     }
    public String[] retrieveFromDisk(String id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/resources/Disk.txt"));
        String line;
        String[] values= new String[20];
        values[0] = id;
        while((line = br.readLine())!=null){
            if(line.equals(id)){
                for(int i=1; i<20; i++){
                    line = br.readLine();
                    values[i] = line.equals("null")?null:line;
                }
                System.out.println("Swapped out of disk process with id= "+values[0]);
                updateDisk(values);
                values[1] = "READY";

                return values;
            }
        }
        return null;
    }
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
           FileWriter Disk = new FileWriter("src/resources/Disk.txt");
              Disk.write("");
                Disk.close();
        Scheduler scheduler = new Scheduler();
        scheduler.cycle();
        }
    }








