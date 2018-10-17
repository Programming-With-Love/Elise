package site.zido.elise.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * contains a range of content that may be included in addition to text.
 * <p>
 * such as images, videos, etc.
 *
 * @author zido
 */
public class Fragment implements Serializable {
    private static final long serialVersionUID = -630439618508226636L;
    private List<Paragraph> contents = new ArrayList<>();

    public Fragment() {

    }

    public void add(String raw, RichType type) {
        this.contents.add(new Paragraph(raw, type));
    }

    public List<Paragraph> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        if (contents.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Paragraph content : contents) {
            sb.append(content).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
