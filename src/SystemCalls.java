import java.io.*;

public class SystemCalls {
    public static String readFile(String file) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("src/"+file+".txt"));
        String line;
        String res="";
        while((line=br.readLine())!=null){
            res+= line+"\n";
        }
        return res;
    }
    public static void writeFile(String file, String yval) throws IOException {
        FileWriter w = new FileWriter("src/"+file+".txt", true);
        w.write(yval+"\n");
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
    public static String readFromMemory(String x,int address){
        String xval=null;
        for(int i=address+5; i<address+8; i++){
            if(Memory.stack[i]!=null){
                String[] arr = Memory.stack[i].split("=");
                if(arr[0].equals(x))
                    xval = arr[1];
            }
        }
        return xval;
    }
    public static void writeToMemory(String variable,String value, int address){
        String var = variable+"="+value;
        for(int i=address+5; i<address+8; i++){
            if(Memory.stack[i] == null){
                Memory.stack[i] = var;
                return;
            }
        }
        Memory.stack[address+5] = var;
    }
}
