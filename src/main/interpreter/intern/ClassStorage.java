package main.interpreter.intern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassStorage
{
    private static long nextObjectID = 1;
    private static Map<Long, Object> classStorage = new HashMap<>();

    private static long hasObject(Object object)
    {
        for(Map.Entry<Long, Object> entry : classStorage.entrySet())
            if(entry.getValue().equals(object))
                return entry.getKey();
        return -1;
    }

    public static long storeObject(Object object)
    {
        long existent = hasObject(object);
        if(existent != -1)
            return existent;
        classStorage.put(nextObjectID, object);
        return nextObjectID++;
    }

    public static Object getStoredObject(long id)
    {
        return classStorage.get(id);
    }
}
