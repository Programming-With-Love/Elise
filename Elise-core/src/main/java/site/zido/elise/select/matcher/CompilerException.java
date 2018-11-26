package site.zido.elise.select.matcher;

/**
 * The type Compiler exception.
 *
 * @author zido
 */
public class CompilerException extends Exception {
    /**
     * Instantiates a new Compiler exception.
     *
     * @param msg the msg
     */
    public CompilerException(String msg) {
        super("compile error:" + msg);
    }
}
