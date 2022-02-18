package org.wzy.sqltemplate.token;

import org.wzy.sqltemplate.Context;
import org.wzy.sqltemplate.script.OgnlCache;

public class ParameterTokenHandler implements ContextTokenHandler {

    @Override
    public String getOpenToken() {
        return "#{";
    }

    @Override
    public String getCloseToken() {
        return "}";
    }

    @Override
    public String handleToken(String content, Context context) {
        Object value = OgnlCache.getValue(content,
                context.getBinding());
        context.addParameter(value);
        return "?";
    }
}
