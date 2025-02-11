package org.wzy.sqltemplate;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.*;

/**
 * @author Wen
 */
public class Context {


    static {
        OgnlRuntime.setPropertyAccessor(HashMap.class, new ContextAccessor());
    }

    public static final String BINDING_DATA = "_data";

    private Configuration cfg;

    private OgnlContext binding;

    private StringBuilder sql = new StringBuilder();

    private List<Object> parameter;

    private int uniqueNumber = 0;

    public Context(Configuration cfg, Map<String, Object> data) {
        this.cfg = cfg;
        MemberAccess memberAccess = new AbstractMemberAccess() {
            @Override
            public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
                int modifiers = member.getModifiers();
                return Modifier.isPublic(modifiers);
            }
        };
        binding = new OgnlContext(memberAccess, null, null, data);
//		binding.setRoot(data);
//		binding = new HashMap<String, Object>();
        parameter = new ArrayList<Object>();
//		binding.put(BINDING_DATA, data);
    }

    public void bind(String key, Object value) {
        binding.put(key, value);
    }

    public void appendSql(String sqlFragement) {
        sql.append(sqlFragement).append(" ");
    }

    public OgnlContext getBinding() {
        return this.binding;
    }

    public List<Object> getParameter() {
        return this.parameter;
    }

    public void addParameter(Object parameter) {
        this.parameter.add(parameter);
    }

    public String getSql() {
        return sql.toString();
    }

    public void setSql(String sql) {
        this.sql = new StringBuilder(sql);
    }

    public int getUniqueNumber() {
        return ++uniqueNumber;
    }

    public Configuration getConfiguration() {
        return this.cfg;
    }

    static class ContextAccessor implements PropertyAccessor {

        public Object getProperty(Map context, Object target, Object name)
                throws OgnlException {
            Map map = (Map) target;

            Object result = map.get(name);
            if (result != null) {
                return result;
            }

            Object parameterObject = map.get(BINDING_DATA);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        public void setProperty(Map context, Object target, Object name,
                                Object value) throws OgnlException {
            Map map = (Map) target;
            map.put(name, value);
        }

        public String getSourceAccessor(OgnlContext arg0, Object arg1,
                                        Object arg2) {
            // TODO Auto-generated method stub
            return null;
        }

        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
