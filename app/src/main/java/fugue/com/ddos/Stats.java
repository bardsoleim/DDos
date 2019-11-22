package fugue.com.ddos;

public class Stats {


    public static int getPause() {
        return Pause;
    }

    public static void setPause(int pause) {
        Pause = pause;
    }

    private static int Pause;
    private static String selectedtarget, message;

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {

        Stats.message  = new String(new char[230]).replace("\0", message);
    }

    public static int getTimeoutET() {
        return timeoutET;
    }

    public static void setTimeoutET(int timeoutET) {
        Stats.timeoutET = timeoutET;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Stats.port = port;
    }

    public static int getMethod() {
        return method;
    }

    public static void setMethod(int method) {
        Stats.method = method;
    }

    public static int getThreads() {
        return threads;
    }

    public static void setThreads(int threads) {
        Stats.threads = threads;
    }

    private static int timeoutET, port, method, threads;


    public static  String getSelectedtarget() {
        return selectedtarget;
    }

    public static void setSelectedtarget(String Selectedtarget) {
        selectedtarget = Selectedtarget;
    }

}
