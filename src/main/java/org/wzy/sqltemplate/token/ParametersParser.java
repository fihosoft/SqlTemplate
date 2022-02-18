package org.wzy.sqltemplate.token;

import org.wzy.sqltemplate.Context;

import java.util.ArrayList;
import java.util.List;

public class ParametersParser {
    private final List<ContextTokenHandler> handlers = new ArrayList<>();

    public void addHandler(ContextTokenHandler handler) {
        handlers.add(handler);
    }

    public String parse(String text, Context context) {
        StringBuilder builder = new StringBuilder();
        if (text != null && text.length() > 0) {
            char[] src = text.toCharArray();
            int offset = 0;
            while (true) {
                int start = -1;
                ContextTokenHandler handler = null;
                for (int i = 0; i < handlers.size(); i++) {
                    ContextTokenHandler tokenHandler = handlers.get(i);
                    int tokenIndex = text.indexOf(tokenHandler.getOpenToken(), offset);
                    if (tokenIndex != -1 && (start == -1 || tokenIndex < start)) {
                        start = tokenIndex;
                        handler = tokenHandler;
                    }
                }
                if (start == -1) {
                    break;
                }
                String openToken = handler.getOpenToken();
                String closeToken = handler.getCloseToken();
                if (start > 0 && src[start - 1] == '\\') {
                    // the variable is escaped. remove the backslash.
                    builder.append(src, offset, start - 1).append(openToken);
                    offset = start + openToken.length();
                } else {
                    int end = text.indexOf(closeToken, start);
                    if (end == -1) {
                        builder.append(src, offset, src.length - offset);
                        offset = src.length;
                    } else {
                        builder.append(src, offset, start - offset);
                        offset = start + openToken.length();
                        String content = new String(src, offset, end - offset);
                        String s = handler.handleToken(content, context);
                        if (s != null) {
                            builder.append(s);
                        }
                        offset = end + closeToken.length();
                    }
                }
            }
            if (offset < src.length) {
                builder.append(src, offset, src.length - offset);
            }
        }
        return builder.toString();
    }
}
