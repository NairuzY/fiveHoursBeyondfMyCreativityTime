import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Memory {
    
    static String[] stack=new String[40];
    public void setStack(String word,int index) {
        stack[index]=word;
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
            if(stack[i*20 + 1].equals("TERMINATED"))
                return swapOut(i);
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
    public void addToDisk(String[] values) throws IOException {
        System.out.println("Swapped in to disk process with id= "+values[0]);
        FileWriter writer = new FileWriter( "src/resources/Disk.txt", true );
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<20; i++){
            if(values[i]!=null)
                sb.append(values[i]).append("\n");
            else{
                sb.append("null"+ "\n");
            }
        }
        writer.append(sb);
        writer.flush();
        writer.close();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================================\n");
        if(stack[0]==null && stack[20]==null) return "The Memory is empty";
        if(stack[0]!=null){
        sb.append("Process_").append(stack[0]).append(" ID=").append(stack[0]).append("\n");
        sb.append("Process_").append(stack[0]).append(" State:").append(stack[1]).append("\n");
        sb.append("Process_").append(stack[0]).append(" PC: ").append(stack[2]).append("\n");
        sb.append("Process_").append(stack[0]).append(" Min: ").append(stack[3]).append("\n");
        sb.append("Process_").append(stack[0]).append(" Max: ").append(stack[4]).append("\n");

        for(int i=5; i<20; i++){
            if(stack[i]!=null)
                sb.append(stack[i]).append("\n");
        }}
        if(stack[20]!=null) {

            sb.append("Process_").append(stack[20]).append(" ID=").append(stack[20]).append("\n");
            sb.append("Process_").append(stack[20]).append(" State: ").append(stack[21]).append("\n");
            sb.append("Process_").append(stack[20]).append(" PC: ").append(stack[22]).append("\n");
            sb.append("Process_").append(stack[20]).append(" Min: ").append(stack[23]).append("\n");
            sb.append("Process_").append(stack[20]).append(" Max: ").append(stack[24]).append("\n");

            for(int i=25; i<40; i++){
                if(stack[i]!=null)
                    sb.append(stack[i]).append("\n");
            }
        }
        sb.append("=====================================================\n");
        return sb.toString();
    }
}
