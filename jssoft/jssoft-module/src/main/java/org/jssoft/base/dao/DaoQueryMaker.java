package org.jssoft.base.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DaoQueryMaker {

    public static Query makeQuery(EntityManager em, Object o, String... fuzzy) {
        StringBuilder sb = new StringBuilder();
        sb.append("select OBJECT(obj) from " + o.getClass().getName() + " obj");
        Map<String, Object> mappings = FindAvailableFields(o);
        Set<String> keys = mappings.keySet();
        List<String> fuzzys = java.util.Arrays.asList(fuzzy);
        if (!keys.isEmpty()) {
            sb.append(" where");
            int num = 0;
            for (String key : keys) {
                if (num != 0) {
                    sb.append(" and");
                }
                if (fuzzys != null && fuzzys.contains(key)) {
                    sb.append(" obj.").append(key).append(" like :").append(key);
                } else {
                    sb.append(" obj.").append(key).append("=:").append(key);
                }
                num++;
            }
            Query q = em.createQuery(sb.toString());
            for (String key : keys) {
                if (fuzzys != null && fuzzys.contains(key)) {
                    q.setParameter(key, "%" + mappings.get(key) + "%");
                } else {
                    q.setParameter(key, mappings.get(key));
                }
            }
            return q;
        }
        return em.createQuery(sb.toString());
    }

    /**
     * o中不为null的字段才可用
     * @return
     */
    private static Map<String, Object> FindAvailableFields(Object o) {
        Map<String, Object> mappings = new HashMap<String, Object>();
        Field[] fields = o.getClass().getFields();
        for (Field f : fields) {
            Object fieldValue = null;
            Object column = f.getAnnotation(javax.persistence.Column.class);
            if (column != null) {   // Means this is a mapping field
                fieldValue = getFieldValue(f, o);
                if (fieldValue != null) {
                    mappings.put(f.getName(), fieldValue);
                }
            }
        }
        return mappings;
    }

    private static Object getFieldValue(Field field, Object o){
        String fieldName = field.getName();
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1);
        try {
            Method method = o.getClass().getMethod(methodName);
            return method.invoke(o);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
        return null;
    }
}
