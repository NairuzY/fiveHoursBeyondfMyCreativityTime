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
}
