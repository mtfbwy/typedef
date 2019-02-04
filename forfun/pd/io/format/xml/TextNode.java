package pd.io.format.xml;

import pd.io.format.FormattingConfig;

public class TextNode extends Node {

    CharSequence content = null; // trimmed

    public boolean isEmpty() {
        return content == null || content.length() == 0;
    }

    @Override
    public StringBuilder toString(StringBuilder o, FormattingConfig c) {
        assert o != null;
        if (!isEmpty()) {
            appendIndent(o, c).append(content).append('\n');
        }
        return o;
    }
}