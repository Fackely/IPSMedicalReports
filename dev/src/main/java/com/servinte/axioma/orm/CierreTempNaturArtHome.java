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
 * Home object for domain model class CierreTempNaturArt.
 * @see com.servinte.axioma.orm.CierreTempNaturArt
 * @author Hibernate Tools
 */
public class CierreTempNaturArtHome {

	private static final Log log = LogFactory
			.getLog(CierreTempNaturArtHome.class);

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

	public void persist(CierreTempNaturArt transientInstance) {
		log.debug("persisting CierreTempNaturArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempNaturArt instance) {
		log.debug("attaching dirty CierreTempNaturArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempNaturArt instance) {
		log.debug("attaching clean CierreTempNaturArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempNaturArt persistentInstance) {
		log.debug("deleting CierreTempNaturArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempNaturArt merge(CierreTempNaturArt detachedInstance) {
		log.debug("merging CierreTempNaturArt instance");
		try {
			CierreTempNaturArt result = (CierreTempNaturArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempNaturArt findById(long id) {
		log.debug("getting CierreTempNaturArt instance with id: " + id);
		try {
			CierreTempNaturArt instance = (CierreTempNaturArt) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CierreTempNaturArt", id);
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

	public List findByExample(CierreTempNaturArt instance) {
		log.debug("finding CierreTempNaturArt instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreTempNaturArt").add(
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
