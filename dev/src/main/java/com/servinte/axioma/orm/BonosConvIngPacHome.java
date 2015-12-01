package com.servinte.axioma.orm;
// Generated Sep 6, 2010 11:18:32 AM by Hibernate Tools 3.2.4.GA


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class BonosConvIngPac.
 * @see com.servinte.axioma.orm.BonosConvIngPac
 * @author Hibernate Tools
 */
public class BonosConvIngPacHome {

    private static final Log log = LogFactory.getLog(BonosConvIngPacHome.class);

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
    
    public void persist(BonosConvIngPac transientInstance) {
        log.debug("persisting BonosConvIngPac instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(BonosConvIngPac instance) {
        log.debug("attaching dirty BonosConvIngPac instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(BonosConvIngPac instance) {
        log.debug("attaching clean BonosConvIngPac instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(BonosConvIngPac persistentInstance) {
        log.debug("deleting BonosConvIngPac instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public BonosConvIngPac merge(BonosConvIngPac detachedInstance) {
        log.debug("merging BonosConvIngPac instance");
        try {
            BonosConvIngPac result = (BonosConvIngPac) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public BonosConvIngPac findById( long id) {
        log.debug("getting BonosConvIngPac instance with id: " + id);
        try {
            BonosConvIngPac instance = (BonosConvIngPac) sessionFactory.getCurrentSession()
                    .get("com.servinte.axioma.orm.BonosConvIngPac", id);
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
    
    public List findByExample(BonosConvIngPac instance) {
        log.debug("finding BonosConvIngPac instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.servinte.axioma.orm.BonosConvIngPac")
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

