package main.interpreter.intern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassStorage
{
    private static long nextObjectID = 1;
    private static Map<Long, Object> classStorage = new HashMap<>();

    public static long storeObject(Object object)
    {
        classStorage.put(nextObjectID, object);
        return nextObjectID++;
    }

    public static Object getStoredObject(long id)
    {
        return classStorage.get(id);
    }
}
