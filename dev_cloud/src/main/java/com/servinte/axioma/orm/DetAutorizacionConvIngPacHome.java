package com.servinte.axioma.orm;

// Generated Sep 9, 2010 11:32:16 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetAutorizacionConvIngPac.
 * @see com.servinte.axioma.orm.DetAutorizacionConvIngPac
 * @author Hibernate Tools
 */
public class DetAutorizacionConvIngPacHome {

	private static final Log log = LogFactory
			.getLog(DetAutorizacionConvIngPacHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(DetAutorizacionConvIngPac transientInstance) {
		log.debug("persisting DetAutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetAutorizacionConvIngPac instance) {
		log.debug("attaching dirty DetAutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetAutorizacionConvIngPac instance) {
		log.debug("attaching clean DetAutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetAutorizacionConvIngPac persistentInstance) {
		log.debug("deleting DetAutorizacionConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetAutorizacionConvIngPac merge(
			DetAutorizacionConvIngPac detachedInstance) {
		log.debug("merging DetAutorizacionConvIngPac instance");
		try {
			DetAutorizacionConvIngPac result = (DetAutorizacionConvIngPac) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetAutorizacionConvIngPac findById(long id) {
		log.debug("getting DetAutorizacionConvIngPac instance with id: " + id);
		try {
			DetAutorizacionConvIngPac instance = (DetAutorizacionConvIngPac) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.DetAutorizacionConvIngPac",
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

	public List findByExample(DetAutorizacionConvIngPac instance) {
		log.debug("finding DetAutorizacionConvIngPac instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetAutorizacionConvIngPac").add(
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
