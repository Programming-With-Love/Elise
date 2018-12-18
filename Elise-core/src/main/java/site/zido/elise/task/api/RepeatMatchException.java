package site.zido.elise.task.api;

public class RepeatMatchException extends RuntimeException {
    public RepeatMatchException(){
        super("Cannot repeat matches when the target has been built，you may need call the [and] function");
    }
}
