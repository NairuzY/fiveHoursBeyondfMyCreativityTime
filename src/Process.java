public class Process {
    private PCB info;
    public static int counter=1;

    public Process(int min,int max){
        info=new PCB(counter,min,max);
        counter++;
    }
}
