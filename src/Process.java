import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Process {
    private PCB info;
    public static int counter=1;
    private int timeSlice = Integer.parseInt(getVal("slice"));

    public Process(int min,int max){
        info=new PCB(counter,min,max);
        counter++;
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
}
