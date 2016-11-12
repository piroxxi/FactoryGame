package fr.piroxxi.factorygame.storage;

import java.util.List;

public interface Storage {
    /**
     */
    void saveObject(Object object);

    /**
     */
    Object loadObject(Integer id);

    /**
     */
    List<Object> listAllObjects(Class c);
}
