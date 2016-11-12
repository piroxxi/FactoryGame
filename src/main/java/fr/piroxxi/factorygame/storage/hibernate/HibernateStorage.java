package fr.piroxxi.factorygame.storage.hibernate;

import fr.piroxxi.factorygame.log.Log;
import fr.piroxxi.factorygame.storage.Storage;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import java.util.List;

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

    public List<Object> listAllObjects(Class c) {
        String hql = "FROM " + c.toString();
        Session session = this.factory.openSession();
        Query query = session.createQuery(hql);
        return query.list();
    }
}
