package fr.piroxxi.factorygame.storage;

public interface Storage {
    /**
     */
    void saveObject(Object object);

    /**
     */
    Object loadObject(Integer id);
}
