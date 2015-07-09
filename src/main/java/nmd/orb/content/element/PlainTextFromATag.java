package nmd.orb.content.element;

/**
 * @author : igu
 */
public class PlainTextFromATag extends PlainText {

    public PlainTextFromATag(String text) {
        super(text);
    }

    @Override
    public String toString() {
        return "PlainTextFromATag{" +
                "text='" + text + '\'' +
                '}';
    }

}
