package com.servinte.axioma.orm;

// Generated Feb 5, 2011 1:14:00 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreTempNivelAtenServ.
 * @see com.servinte.axioma.orm.CierreTempNivelAtenServ
 * @author Hibernate Tools
 */
public class CierreTempNivelAtenServHome {

	private static final Log log = LogFactory
			.getLog(CierreTempNivelAtenServHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(CierreTempNivelAtenServ transientInstance) {
		log.debug("persisting CierreTempNivelAtenServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempNivelAtenServ instance) {
		log.debug("attaching dirty CierreTempNivelAtenServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempNivelAtenServ instance) {
		log.debug("attaching clean CierreTempNivelAtenServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempNivelAtenServ persistentInstance) {
		log.debug("deleting CierreTempNivelAtenServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempNivelAtenServ merge(
			CierreTempNivelAtenServ detachedInstance) {
		log.debug("merging CierreTempNivelAtenServ instance");
		try {
			CierreTempNivelAtenServ result = (CierreTempNivelAtenServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempNivelAtenServ findById(long id) {
		log.debug("getting CierreTempNivelAtenServ instance with id: " + id);
		try {
			CierreTempNivelAtenServ instance = (CierreTempNivelAtenServ) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CierreTempNivelAtenServ",
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CierreTempNivelAtenServ instance) {
		log.debug("finding CierreTempNivelAtenServ instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreTempNivelAtenServ").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
