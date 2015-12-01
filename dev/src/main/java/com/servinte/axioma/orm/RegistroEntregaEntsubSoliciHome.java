package com.servinte.axioma.orm;

// Generated Dec 7, 2010 9:11:49 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class RegistroEntregaEntsubSolici.
 * @see com.servinte.axioma.orm.RegistroEntregaEntsubSolici
 * @author Hibernate Tools
 */
public class RegistroEntregaEntsubSoliciHome {

	private static final Log log = LogFactory
			.getLog(RegistroEntregaEntsubSoliciHome.class);

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

	public void persist(RegistroEntregaEntsubSolici transientInstance) {
		log.debug("persisting RegistroEntregaEntsubSolici instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RegistroEntregaEntsubSolici instance) {
		log.debug("attaching dirty RegistroEntregaEntsubSolici instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RegistroEntregaEntsubSolici instance) {
		log.debug("attaching clean RegistroEntregaEntsubSolici instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RegistroEntregaEntsubSolici persistentInstance) {
		log.debug("deleting RegistroEntregaEntsubSolici instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RegistroEntregaEntsubSolici merge(
			RegistroEntregaEntsubSolici detachedInstance) {
		log.debug("merging RegistroEntregaEntsubSolici instance");
		try {
			RegistroEntregaEntsubSolici result = (RegistroEntregaEntsubSolici) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RegistroEntregaEntsubSolici findById(long id) {
		log
				.debug("getting RegistroEntregaEntsubSolici instance with id: "
						+ id);
		try {
			RegistroEntregaEntsubSolici instance = (RegistroEntregaEntsubSolici) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.RegistroEntregaEntsubSolici",
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

	public List findByExample(RegistroEntregaEntsubSolici instance) {
		log.debug("finding RegistroEntregaEntsubSolici instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RegistroEntregaEntsubSolici").add(
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
