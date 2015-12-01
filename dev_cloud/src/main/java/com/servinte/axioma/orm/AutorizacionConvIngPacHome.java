package com.servinte.axioma.orm;

// Generated Aug 26, 2010 8:03:05 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionConvIngPac.
 * @see com.servinte.axioma.orm.AutorizacionConvIngPac
 * @author Hibernate Tools
 */
public class AutorizacionConvIngPacHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionConvIngPacHome.class);

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

	public void persist(AutorizacionConvIngPac transientInstance) {
		log.debug("persisting AutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionConvIngPac instance) {
		log.debug("attaching dirty AutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionConvIngPac instance) {
		log.debug("attaching clean AutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionConvIngPac persistentInstance) {
		log.debug("deleting AutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionConvIngPac merge(AutorizacionConvIngPac detachedInstance) {
		log.debug("merging AutorizacionConvIngPac instance");
		try {
			AutorizacionConvIngPac result = (AutorizacionConvIngPac) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionConvIngPac findById(long id) {
		log.debug("getting AutorizacionConvIngPac instance with id: " + id);
		try {
			AutorizacionConvIngPac instance = (AutorizacionConvIngPac) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AutorizacionConvIngPac",
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

	public List findByExample(AutorizacionConvIngPac instance) {
		log.debug("finding AutorizacionConvIngPac instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionConvIngPac").add(
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
