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
    public Object[] getStack() {
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
        FileWriter writer = new FileWriter( "src/resources/Disk.txt", true );
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<20; i++){
            if(values[i]!=null)
                sb.append(values[i]+ "\n");
            else
                sb.append("null"+"\n");
        }
        writer.append(sb);
        writer.flush();
        writer.close();
    }

}
