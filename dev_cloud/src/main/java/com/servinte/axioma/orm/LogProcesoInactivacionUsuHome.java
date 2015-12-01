package com.servinte.axioma.orm;
// Generated Dec 9, 2010 8:32:17 AM by Hibernate Tools 3.2.4.GA


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogProcesoInactivacionUsu.
 * @see com.servinte.axioma.orm.LogProcesoInactivacionUsu
 * @author Hibernate Tools
 */
public class LogProcesoInactivacionUsuHome {

    private static final Log log = LogFactory.getLog(LogProcesoInactivacionUsuHome.class);

    protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    
    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        }
        catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }
    
    public void persist(LogProcesoInactivacionUsu transientInstance) {
        log.debug("persisting LogProcesoInactivacionUsu instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(LogProcesoInactivacionUsu instance) {
        log.debug("attaching dirty LogProcesoInactivacionUsu instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(LogProcesoInactivacionUsu instance) {
        log.debug("attaching clean LogProcesoInactivacionUsu instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(LogProcesoInactivacionUsu persistentInstance) {
        log.debug("deleting LogProcesoInactivacionUsu instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public LogProcesoInactivacionUsu merge(LogProcesoInactivacionUsu detachedInstance) {
        log.debug("merging LogProcesoInactivacionUsu instance");
        try {
            LogProcesoInactivacionUsu result = (LogProcesoInactivacionUsu) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public LogProcesoInactivacionUsu findById( int id) {
        log.debug("getting LogProcesoInactivacionUsu instance with id: " + id);
        try {
            LogProcesoInactivacionUsu instance = (LogProcesoInactivacionUsu) sessionFactory.getCurrentSession()
                    .get("com.servinte.axioma.orm.LogProcesoInactivacionUsu", id);
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
    
    public List findByExample(LogProcesoInactivacionUsu instance) {
        log.debug("finding LogProcesoInactivacionUsu instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.servinte.axioma.orm.LogProcesoInactivacionUsu")
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

