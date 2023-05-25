import java.io.*;

public class Parser {

    static boolean dontMove = false;
    public static void execute(String instructionLine, int address) throws IOException {
        String[] instruction = instructionLine.split(" ");
        switch(instruction[0]){
            case "print": SystemCalls.print(instruction[1]); break;
            case "assign": assign(instruction, address); break;
            case "writeFile": writeFile(instruction[1], instruction[2], address);
            case "readFile" : readFile(instruction[1],address); break;
            case "printFromTo" : printFromTo(instruction[1], instruction[2], address); break;
            case "semWait": semWait(instruction[1],address); break;
            case "semSignal" : semSignal(instruction[1]); break;
            default:
        }
    }



    public static void assign(String[] arr, int address) throws IOException {
        String var;
        if(arr[2].equals("input")) {
            if(Scheduler.runningProcess.getTemp() == null) {
                var = SystemCalls.input();
                Scheduler.runningProcess.setTemp(var);
                dontMove = true;
            }else{
                SystemCalls.writeToMemory(arr[1],Scheduler.runningProcess.getTemp(),address);
                Scheduler.runningProcess.setTemp(null);
                dontMove = false;
            }
        }
        else if(arr[2].equals("readFile")) {
            if (Scheduler.runningProcess.getTemp() == null) {
                String file=readFile(arr[3],address);
                Scheduler.runningProcess.setTemp(file);
                dontMove = true;
            }else {
                SystemCalls.writeToMemory(arr[1],Scheduler.runningProcess.getTemp(),address);
                Scheduler.runningProcess.setTemp(null);
                dontMove = false;
            }
        }
        //we'll have to use instruction[3] to find the variable to read from memory
    }
    private static String readFile(String x, int address) throws IOException {
        String file=SystemCalls.readFromMemory(x,address);
        return SystemCalls.readFile(file);
    }
    public static void writeFile(String x, String y, int address) throws IOException {
        String file=SystemCalls.readFromMemory(x,address);
        String yval=SystemCalls.readFromMemory(y,address);
        SystemCalls.writeFile(file, yval);
    }
    public static void printFromTo(String x, String y, int address){
        int from=Integer.parseInt(SystemCalls.readFromMemory(x,address));//is it okay to be an Integer not int?
        int to = Integer.parseInt(SystemCalls.readFromMemory(y,address));
        for(int i=from+1; i<to; i++){
            SystemCalls.print(i+"");
        }

    }
    public static void semWait(String x,int address){
        if(x.equals("userInput")){
            if(Scheduler.semInput==0) {
                Memory.stack[address + 1] = "BLOCKED";
                Scheduler.blockedQueue.put(Memory.stack[address],Scheduler.runningProcess);
                Scheduler.blockedOnTakingInput.add(Memory.stack[address]);
            }
            else
                Scheduler.semInput=0;
        }else if(x.equals("userOutput")){
            if(Scheduler.semScreen==0) {
                Memory.stack[address + 1] = "BLOCKED";
                Scheduler.blockedQueue.put(Memory.stack[address],Scheduler.runningProcess);
                Scheduler.blockedOnScreen.add(Memory.stack[address]);
            }
            else
                Scheduler.semScreen=0;

        }else{
            if(Scheduler.semFile==0) {
                Memory.stack[address + 1] = "BLOCKED";
                Scheduler.blockedQueue.put(Memory.stack[address],Scheduler.runningProcess);
                Scheduler.blockedOnFile.add(Memory.stack[address]);
            }
            else
                Scheduler.semFile=0;

        }
    }
    public static void semSignal(String x){
        if(x.equals("userInput")){
            Scheduler.semInput=1;
            while(!Scheduler.blockedOnTakingInput.isEmpty()){
                String processID=Scheduler.blockedOnTakingInput.poll();
                Scheduler.readyQueue.add(Scheduler.blockedQueue.get(processID));
                Scheduler.blockedQueue.remove(processID);
            }
        }else if(x.equals("userOutput")) {
            Scheduler.semScreen=1;
            while(!Scheduler.blockedOnScreen.isEmpty()){
                String processID=Scheduler.blockedOnScreen.poll();
                Scheduler.readyQueue.add(Scheduler.blockedQueue.get(processID));
                Scheduler.blockedQueue.remove(processID);
            }
        } else{
            Scheduler.semFile=1;
            while(!Scheduler.blockedOnFile.isEmpty()){
                String processID=Scheduler.blockedOnFile.poll();
                Scheduler.readyQueue.add(Scheduler.blockedQueue.get(processID));
                Scheduler.blockedQueue.remove(processID);
            }
        }
    }

}
