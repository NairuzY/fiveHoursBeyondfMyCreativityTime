import java.util.Hashtable;

public class Disk {

    private static Hashtable <String, String[]> disk = new Hashtable<>();
    public static Hashtable<String, String[]> getDisk() {
        return disk;
    }
    public static void add(String key, String[] process) {
        disk.put(key, process);
    }

}
