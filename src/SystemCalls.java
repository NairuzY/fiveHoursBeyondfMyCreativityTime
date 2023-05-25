import java.io.*;

public class SystemCalls {
    public static void readFile(String x) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("src/"+x));
        Scheduler.runningProcess.setTemp(br.readLine());
    }
    public static void writeFile(String x, String y) throws IOException {
        FileWriter w = new FileWriter("src/"+x+".txt", true);
        w.write(y);
        w.close();
    }
    public static void print(String x){
        System.out.println(x);
    }
    public static String input() throws IOException {
        System.out.println("Please enter a value");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
    public static String readFromMemory(String x){

    }
}
