import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;

public class Memory {
    
    static String[] stack=new String[40];
    public void setStack(String word,int index) {
        this.stack[index]=word;
    }
    boolean [] used = new boolean[2];
    public String[] getStack() {
        return stack;
    }

    private static HashSet<String> inMemory = new HashSet<>();

    public int allocateMemory() throws IOException {
        for(int i=0; i<used.length; i++){
            if(!used[i]) {
                used[i] = true;
                return i*20;
            }
        }
        for(int i=0; i<used.length; i++){
            if(stack[i*20 + 1].equals("BLOCKED"))
                return swapOut(i);
        }
        for(int i=0; i<used.length; i++){
            if(!stack[i*20 + 1].equals("RUNNING"))
                return swapOut(i);
        }
        return 0;
    }

    private int swapOut(int i) throws IOException {
        String[] swappedOut = new String[20];
        System.arraycopy(stack, i * 20, swappedOut, 0, swappedOut.length);
        addToDisk(swappedOut);
        deallocateMemory(i*20);
        used[i] = true;
        return i*20;
    }
    public void deallocateMemory(int address) {
        inMemory.remove(stack[address]);
        for(int i=0; i<20; i++)
            stack[address+i] = null;
        used[address/20] = false;
    }
    public static HashSet<String> getInMemory() {
        return inMemory;
    }
    public static void setInMemory(HashSet<String> inMemory) {
        Memory.inMemory = inMemory;
    }
    public void addToDisk(String[] values) throws IOException {
        System.out.println("Swapped in to disk process with id= "+values[0]);
        FileWriter writer = new FileWriter( "src/resources/Disk.txt", true );
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<20; i++){
            if(values[i]!=null)
                sb.append(values[i]+ "\n");
        }
        writer.append(sb);
        writer.flush();
        writer.close();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(stack[0]==null && stack[20]==null) return "The Memory is empty";
        if(stack[0]!=null){
        sb.append("Process_"+stack[0]+" ID="+stack[0]+"\n");
        sb.append("Process_"+stack[0]+" State:"+stack[1]+"\n");
        sb.append("Process_"+stack[0]+" PC: "+stack[2]+"\n");
        sb.append("Process_"+stack[0]+" Min: "+stack[3]+"\n");
        sb.append("Process_"+stack[0]+" Max: "+stack[4]+"\n");

        for(int i=5; i<20; i++){
            if(stack[i]!=null)
                sb.append(stack[i]+ "\n");
        }}
        if(stack[20]!=null) {

            sb.append("Process_"+stack[20]+" ID=" + stack[20] + "\n");
            sb.append("Process_"+stack[20]+" State: " + stack[21] + "\n");
            sb.append("Process_"+stack[20]+" PC: " + stack[22] + "\n");
            sb.append("Process_"+stack[20]+" Min: " + stack[23] + "\n");
            sb.append("Process_"+stack[20]+" Max: " + stack[24] + "\n");

            for(int i=25; i<40; i++){
                if(stack[i]!=null)
                    sb.append(stack[i]+ "\n");
            }
        }
        return sb.toString();
    }
}
