package site.zido.elise.select;

import org.jsoup.nodes.Node;

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

    public Fragment(String text) {
        this.add(text, RichType.TEXT);
    }

    public void add(Node node) {
        switch (node.nodeName()) {
            case "#text":
                add(node.attr("text"), RichType.TEXT);
                break;
            case "#comment":
                add(node.attr("comment"), RichType.TEXT);
                break;
            case "#data":
                add(node.attr("data"), RichType.TEXT);
                break;
            case "img":
                add(node.attr("src"), RichType.IMAGE);
                break;
            case "audio":
                add(node.attr("src"), RichType.AUDIO);
                break;
            case "video":
                add(node.attr("src"), RichType.VIDEO);
                break;
            case "#doctype":
            case "#declaration":
                break;//can't support
            case "a": //mark like this:<a href="http://www.baidu.com"><p>somethings</p><strong>other things</strong></a>
                //and then,the result like this:START_TAG|href=http://www.baidu.com|something|other things|END_TAG
                add("", RichType.CONTENT_START);
                add(node.attr("href"), RichType.LINK);
                List<Node> linkNodes = node.childNodes();
                for (Node linkNode : linkNodes) {
                    add(linkNode);
                }
                add("", RichType.CONTENT_END);
                break;
            default:
                List<Node> nodes = node.childNodes();
                for (Node child : nodes) {
                    add(child);
                }
        }
    }

    public void add(String raw, RichType type) {
        this.contents.add(new Paragraph(raw, type));
    }

    public List<Paragraph> getContents() {
        return contents;
    }

    public String text() {
        StringBuilder sb = new StringBuilder();
        for (Paragraph content : contents) {
            if (content.getType() == RichType.TEXT) {
                sb.append(content.getRaw());
            }
        }
        return sb.toString();
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
