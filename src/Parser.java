import java.io.*;

public class Parser {

    static boolean dontMove = false;
    public static void execute(String instructionLine, int address) throws IOException {
        String[] instruction = instructionLine.split(" ");
        switch(instruction[0]){
            case "print": print(instruction[1]); break;
            case "assign": assign(instruction, address); break;
            case "writeFile": writeFile(instruction[1], instruction[2], address);
            case "readFile" : readFile(instruction[1]); break;
            case "printFromTo" : printFromTo(instruction[1], instruction[2], address); break;
            case "semWait": semWait(instruction[1],address); break;
            case "semSignal" : semSignal(instruction[1]); break;
            default:
        }
    }

    public static void print(String x){
        System.out.println(x);
    }

    public static void assign(String[] arr, int address) throws IOException {
        String var;
        if(arr[2].equals("input")){
            System.out.println("Please enter a value");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            var = br.readLine();
        } else if(arr[2].equals("readFile")) {
            if (Scheduler.runningProcess.getTemp() == null) {
                readFile(arr[3]);
                dontMove = true;
                return;
            } else {
                var = Scheduler.runningProcess.getTemp();
                Scheduler.runningProcess.setTemp(null);
            }
        } else
            var = arr[2];
        var = arr[0]+"="+var;
        for(int i=address+5; i<address+8; i++){
            if(Memory.stack[i] == null){
                Memory.stack[i] = var;
                return;
            }
        }
        //we'll have to use instruction[3] to find the variable to read from memory

    }

    public static void writeFile(String x, String y, int address) throws IOException {
        String xVal = null;
        String yVal = null;
        for(int i=address+5; i<address+8; i++){
            if(Memory.stack[i]!=null){
                String[] arr = Memory.stack[i].split("=");
                if(arr[0].equals(x))
                    xVal = arr[1];
                else if(arr[0].equals(y))
                    yVal = arr[1];
            }
        }
        if(xVal==null || yVal==null){
            System.out.println("input is invalid");
            return;
        }
        File f = new File("src/"+xVal);
        FileWriter w = new FileWriter(f);
        w.write(yVal);
        w.close();
    }

    public static void readFile(String x) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/"+x));
        Scheduler.runningProcess.setTemp(br.readLine());
    }

    public static void printFromTo(String x, String y, int address){
        Integer from=null;//is it okay to be an Integer not int?
        Integer to = null;

        for(int i=address+5; i<address+8; i++){
            if(Memory.stack[i]!=null){
                String[] arr = Memory.stack[i].split("=");
                if(arr[0].equals(x))
                    from = Integer.parseInt(arr[1]);
                else if(arr[0].equals(y))
                    to = Integer.parseInt(arr[1]);
            }
        }
        if(from==null || to==null){
            System.out.println("input is invalid");
            return;
        }
        for(int i=from+1; i<to; i++){
            System.out.println(i);
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
