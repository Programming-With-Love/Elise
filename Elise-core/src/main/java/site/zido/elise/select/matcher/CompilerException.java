package site.zido.elise.select.matcher;

public class CompilerException extends Exception {
    public CompilerException(String msg) {
        super("compile error:" + msg);
    }
}
