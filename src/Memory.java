public class Memory {
    private Object[] stack;

    public Memory(){
        stack=new Object[40];
    }


    public void setStack(Object word,int index) {
        this.stack[index]=word;
    }

    public Object[] getStack() {
        return stack;
    }

    // Allocate memory for a process
    public int allocateMemory(int size) {
        // Implement memory allocation logic
        // Return the starting address of the allocated memory block
        return 0;
    }

    // Deallocate memory for a process
    public void deallocateMemory(int address, int size) {
        // Implement memory deallocation logic
    }

}
