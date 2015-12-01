package com.servinte.axioma.orm;
// Generated 19/05/2011 11:36:24 AM by Hibernate Tools 3.4.0.CR1


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ViasAdministracion.
 * @see com.servinte.axioma.orm.ViasAdministracion
 * @author Hibernate Tools
 */
public class ViasAdministracionHome {

    private static final Log log = LogFactory.getLog(ViasAdministracionHome.class);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        }
        catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }
    
    public void persist(ViasAdministracion transientInstance) {
        log.debug("persisting ViasAdministracion instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(ViasAdministracion instance) {
        log.debug("attaching dirty ViasAdministracion instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ViasAdministracion instance) {
        log.debug("attaching clean ViasAdministracion instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(ViasAdministracion persistentInstance) {
        log.debug("deleting ViasAdministracion instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ViasAdministracion merge(ViasAdministracion detachedInstance) {
        log.debug("merging ViasAdministracion instance");
        try {
            ViasAdministracion result = (ViasAdministracion) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public ViasAdministracion findById( int id) {
        log.debug("getting ViasAdministracion instance with id: " + id);
        try {
            ViasAdministracion instance = (ViasAdministracion) sessionFactory.getCurrentSession()
                    .get("com.servinte.axioma.orm.ViasAdministracion", id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(ViasAdministracion instance) {
        log.debug("finding ViasAdministracion instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.servinte.axioma.orm.ViasAdministracion")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

