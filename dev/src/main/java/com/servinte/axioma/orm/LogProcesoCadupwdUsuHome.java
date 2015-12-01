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
 * Home object for domain model class LogProcesoCadupwdUsu.
 * @see com.servinte.axioma.orm.LogProcesoCadupwdUsu
 * @author Hibernate Tools
 */
public class LogProcesoCadupwdUsuHome {

    private static final Log log = LogFactory.getLog(LogProcesoCadupwdUsuHome.class);

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
    
    public void persist(LogProcesoCadupwdUsu transientInstance) {
        log.debug("persisting LogProcesoCadupwdUsu instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(LogProcesoCadupwdUsu instance) {
        log.debug("attaching dirty LogProcesoCadupwdUsu instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(LogProcesoCadupwdUsu instance) {
        log.debug("attaching clean LogProcesoCadupwdUsu instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(LogProcesoCadupwdUsu persistentInstance) {
        log.debug("deleting LogProcesoCadupwdUsu instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public LogProcesoCadupwdUsu merge(LogProcesoCadupwdUsu detachedInstance) {
        log.debug("merging LogProcesoCadupwdUsu instance");
        try {
            LogProcesoCadupwdUsu result = (LogProcesoCadupwdUsu) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public LogProcesoCadupwdUsu findById( int id) {
        log.debug("getting LogProcesoCadupwdUsu instance with id: " + id);
        try {
            LogProcesoCadupwdUsu instance = (LogProcesoCadupwdUsu) sessionFactory.getCurrentSession()
                    .get("com.servinte.axioma.orm.LogProcesoCadupwdUsu", id);
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
    
    public List findByExample(LogProcesoCadupwdUsu instance) {
        log.debug("finding LogProcesoCadupwdUsu instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.servinte.axioma.orm.LogProcesoCadupwdUsu")
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

