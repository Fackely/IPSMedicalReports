package com.servinte.axioma.orm;

// Generated 14/01/2012 12:40:03 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreTempClaseInvArt.
 * @see com.servinte.axioma.orm.CierreTempClaseInvArt
 * @author Hibernate Tools
 */
public class CierreTempClaseInvArtHome {

	private static final Log log = LogFactory
			.getLog(CierreTempClaseInvArtHome.class);

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

	public void persist(CierreTempClaseInvArt transientInstance) {
		log.debug("persisting CierreTempClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempClaseInvArt instance) {
		log.debug("attaching dirty CierreTempClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempClaseInvArt instance) {
		log.debug("attaching clean CierreTempClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempClaseInvArt persistentInstance) {
		log.debug("deleting CierreTempClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempClaseInvArt merge(CierreTempClaseInvArt detachedInstance) {
		log.debug("merging CierreTempClaseInvArt instance");
		try {
			CierreTempClaseInvArt result = (CierreTempClaseInvArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempClaseInvArt findById(long id) {
		log.debug("getting CierreTempClaseInvArt instance with id: " + id);
		try {
			CierreTempClaseInvArt instance = (CierreTempClaseInvArt) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.CierreTempClaseInvArt", id);
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

	public List findByExample(CierreTempClaseInvArt instance) {
		log.debug("finding CierreTempClaseInvArt instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.CierreTempClaseInvArt")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
