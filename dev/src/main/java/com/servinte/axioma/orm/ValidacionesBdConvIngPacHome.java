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
 * Home object for domain model class ValidacionesBdConvIngPac.
 * @see com.servinte.axioma.orm.ValidacionesBdConvIngPac
 * @author Hibernate Tools
 */
public class ValidacionesBdConvIngPacHome {

	private static final Log log = LogFactory
			.getLog(ValidacionesBdConvIngPacHome.class);

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

	public void persist(ValidacionesBdConvIngPac transientInstance) {
		log.debug("persisting ValidacionesBdConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ValidacionesBdConvIngPac instance) {
		log.debug("attaching dirty ValidacionesBdConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ValidacionesBdConvIngPac instance) {
		log.debug("attaching clean ValidacionesBdConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ValidacionesBdConvIngPac persistentInstance) {
		log.debug("deleting ValidacionesBdConvIngPac instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ValidacionesBdConvIngPac merge(
			ValidacionesBdConvIngPac detachedInstance) {
		log.debug("merging ValidacionesBdConvIngPac instance");
		try {
			ValidacionesBdConvIngPac result = (ValidacionesBdConvIngPac) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ValidacionesBdConvIngPac findById(long id) {
		log.debug("getting ValidacionesBdConvIngPac instance with id: " + id);
		try {
			ValidacionesBdConvIngPac instance = (ValidacionesBdConvIngPac) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ValidacionesBdConvIngPac",
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

	public List findByExample(ValidacionesBdConvIngPac instance) {
		log.debug("finding ValidacionesBdConvIngPac instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ValidacionesBdConvIngPac").add(
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
