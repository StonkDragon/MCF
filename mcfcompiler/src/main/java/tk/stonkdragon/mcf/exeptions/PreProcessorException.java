package tk.stonkdragon.mcf.exeptions;

public class PreProcessorException extends Exception {
    public PreProcessorException(String e) {
        System.err.println(e);
    }
}
