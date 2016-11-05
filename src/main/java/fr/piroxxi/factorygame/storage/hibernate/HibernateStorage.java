package fr.piroxxi.factorygame.storage.hibernate;

import fr.piroxxi.factorygame.log.Log;
import fr.piroxxi.factorygame.storage.Storage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

public class HibernateStorage implements Storage {
    @Log
    private static Logger LOG;

    private SessionFactory factory;

    public HibernateStorage(SessionFactory factory) {
        this.factory = factory;
    }

    public void saveObject(Object object) {
        LOG.debug("saveObject()");
        Session session = this.factory.openSession();
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
//        this.factory.close();
    }

    public Object loadObject(Integer id) {
        LOG.debug("loadObject(" + id + ")");
        return null;
    }
}
