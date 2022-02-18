package org.wzy.sqltemplate.token;

import org.wzy.sqltemplate.Context;

public interface ContextTokenHandler {
    String getOpenToken();
    String getCloseToken();
    String handleToken(String content, Context context);
}
