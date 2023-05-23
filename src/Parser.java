import java.io.*;
import java.util.*;

public class Parser {

    public void execute(String instructionLine, int address) throws IOException {
        String[] instruction = instructionLine.split(" ");
        switch(instruction[0]){
            case "print": print(instruction[1]); break;
            case "assign": assign(instruction); break;
            case "writeFile": writeFile(instruction[1], instruction[2], address);
            case "readFile" : readFile(instruction[1]); break;
            case "printFromTo" : printFromTo(instruction[1], instruction[2]); break;
            case "semWait": semWait(instruction[1]); break;
            case "semSignal" : semSignal(instruction[1]); break;
            default:
        }
    }

    public void print(String x){
        System.out.println(x);
    }

    public void assign(String[] arr) throws IOException {
        String var;
        if(arr[2].equals("input")){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            var = br.readLine();
        } else{
            //we'll have to use instruction[3] to find the variable to read from memory
        }
    }

    public void writeFile(String x, String y, int address) throws IOException {
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

    public void readFile(String x){

    }

    public void printFromTo(String x, String y){
        int from = Integer.parseInt(x);
        int to = Integer.parseInt(y);
        for(int i=from+1; i<to; i++){
            System.out.println(i);
        }
    }

    public void semWait(String x){

    }

    public void semSignal(String x){

    }

}
