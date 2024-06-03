package exceptions;

public class RecursiveCallException extends RuntimeException{
    private final String filename;
    public RecursiveCallException(String filename){
        this.filename=filename;
    }
    public String getFilename(){
        return filename;
    }
}
