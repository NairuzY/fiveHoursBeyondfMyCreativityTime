import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {
    
    public Parser(){
    
    }

    public void execute() throws IOException {
        File myObj = new File("instructions.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] instruction = line.split(" ");
            switch(instruction[0]){
                case "print": print(instruction[1]); break;
                case "assign": assign(instruction[1], instruction[2]); break;
                case "writeFile": writeFile(instruction[1], instruction[2]);
                case "readFile" : readFile(instruction[1]); break;
                case "printFromTo" : printFromTo(instruction[1], instruction[2]); break;
                case "semWait": semWait(instruction[1]); break;
                case "semSignal" : semSignal(instruction[1]); break;
                default:
            }
        }
    }

    public void print(String x){
        System.out.println(x);
    }

    public void assign(String x, String y){

    }

    public void writeFile(String x, String y){

    }

    public void readFile(String x){

    }

    public void printFromTo(String x, String y){
    
    }

    public void semWait(String x){

    }

    public void semSignal(String x){

    }

}
