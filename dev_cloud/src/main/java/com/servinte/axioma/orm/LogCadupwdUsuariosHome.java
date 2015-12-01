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
 * Home object for domain model class LogCadupwdUsuarios.
 * @see com.servinte.axioma.orm.LogCadupwdUsuarios
 * @author Hibernate Tools
 */
public class LogCadupwdUsuariosHome {

    private static final Log log = LogFactory.getLog(LogCadupwdUsuariosHome.class);

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
    
    public void persist(LogCadupwdUsuarios transientInstance) {
        log.debug("persisting LogCadupwdUsuarios instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(LogCadupwdUsuarios instance) {
        log.debug("attaching dirty LogCadupwdUsuarios instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(LogCadupwdUsuarios instance) {
        log.debug("attaching clean LogCadupwdUsuarios instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(LogCadupwdUsuarios persistentInstance) {
        log.debug("deleting LogCadupwdUsuarios instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public LogCadupwdUsuarios merge(LogCadupwdUsuarios detachedInstance) {
        log.debug("merging LogCadupwdUsuarios instance");
        try {
            LogCadupwdUsuarios result = (LogCadupwdUsuarios) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public LogCadupwdUsuarios findById( int id) {
        log.debug("getting LogCadupwdUsuarios instance with id: " + id);
        try {
            LogCadupwdUsuarios instance = (LogCadupwdUsuarios) sessionFactory.getCurrentSession()
                    .get("com.servinte.axioma.orm.LogCadupwdUsuarios", id);
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
    
    public List findByExample(LogCadupwdUsuarios instance) {
        log.debug("finding LogCadupwdUsuarios instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.servinte.axioma.orm.LogCadupwdUsuarios")
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

