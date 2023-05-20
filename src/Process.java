import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Process {
    private PCB info;
    public static int counter=1;
    private String[] instructions;
    private int remainingTime;

    public Process(int min,int max){
        info=new PCB(counter,min,max);
        counter++;
    }

    public void setInfo(PCB info){
        this.info = info;
    }

    public PCB getInfo(){
        return info;
    }

    public static String getVal(String key)
    {
        String keyval = null;
        //Let's consider properties file is in project folder itself

        File file = new File("src/resources/os.config");

        //Creating properties object
        Properties prop = new Properties();
        //Creating InputStream object to read data
        FileInputStream objInput = null;
        try{
            objInput = new FileInputStream(file);
            //Reading properties key/values in file
            prop.load(objInput);
            keyval = prop.getProperty(key);
            objInput.close();
        }catch(Exception e){System.out.println(e.getMessage());}
        return keyval;
    }

    public boolean hasMoreInstructions() {
        return info.getPC() < instructions.length;
    }

    public int getId() {
        return info.getPID();
    }



    public String getNextInstruction() {
        String instruction = instructions[info.getPC()];
        info.setPC(info.getPC()+1);
        return instruction;
    }


//    public boolean isBlocked() {
//        return blocked;
//    }
//
//    public void setBlocked(boolean blocked) {
//        this.blocked = blocked;
//    }
//
//    public boolean isFinished() {
//        return finished;
//    }
//
//    public void setFinished(boolean finished) {
//        this.finished = finished;
//    }
}

