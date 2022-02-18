package org.wzy.sqltemplate.script;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ognl.*;

public class OgnlCache {

	private static final Map<String, ognl.Node> expressionCache = new ConcurrentHashMap<String, ognl.Node>();

	public static Object getValue(String expression, OgnlContext context) {
		try {
			return parseExpression(expression).getValue(context,context.getValues());
//			return Ognl.getValue(parseExpression(expression).getAccessor(), context,context.getRoot());
		} catch (OgnlException e) {
			if(e.getMessage().startsWith("source is null for getProperty")){
				return null;
			}else{
				throw new RuntimeException("Error evaluating expression '"
						+ expression + "'. Cause: " + e, e);
			}
		}
	}

	private static Node parseExpression(String expression)
			throws OgnlException {
		try {
			Node node = expressionCache.get(expression);
			if (node == null) {
				node = new OgnlParser(new StringReader(expression))
						.topLevelExpression();
				expressionCache.put(expression, node);
			}
			return node;
		} catch (ParseException e) {
			throw new ExpressionSyntaxException(expression, e);
		} catch (TokenMgrError e) {
			throw new ExpressionSyntaxException(expression, e);
		}
	}

}
