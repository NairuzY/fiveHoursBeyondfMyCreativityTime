public class Memory {
    
    static String[] stack=new String[40];

    public void setStack(String word,int index) {
        this.stack[index]=word;
    }
    boolean [] used = new boolean[2];
    public Object[] getStack() {
        return stack;
    }

    public int allocateMemory() {
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

    private int swapOut(int i) {
        String[] swappedOut = new String[20];
        System.arraycopy(stack, i * 20, swappedOut, 0, swappedOut.length);
        Disk.add(stack[i*20], swappedOut);
        deallocateMemory(i*20);
        used[i] = true;
        return i*20;
    }
    public void deallocateMemory(int address) {
        for(int i=0; i<20; i++)
            stack[address+i] = null;
        used[address/20] = false;
    }

}
