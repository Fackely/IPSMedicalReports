package com.servinte.axioma.orm;

// Generated 15/04/2010 03:20:53 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EntidadesFinancieras.
 * @see com.servinte.axioma.orm.EntidadesFinancieras
 * @author Hibernate Tools
 */
public class EntidadesFinancierasHome {

	private static final Log log = LogFactory
			.getLog(EntidadesFinancierasHome.class);

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

	public void persist(EntidadesFinancieras transientInstance) {
		log.debug("persisting EntidadesFinancieras instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EntidadesFinancieras instance) {
		log.debug("attaching dirty EntidadesFinancieras instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EntidadesFinancieras instance) {
		log.debug("attaching clean EntidadesFinancieras instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EntidadesFinancieras persistentInstance) {
		log.debug("deleting EntidadesFinancieras instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EntidadesFinancieras merge(EntidadesFinancieras detachedInstance) {
		log.debug("merging EntidadesFinancieras instance");
		try {
			EntidadesFinancieras result = (EntidadesFinancieras) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EntidadesFinancieras findById(int id) {
		log.debug("getting EntidadesFinancieras instance with id: " + id);
		try {
			EntidadesFinancieras instance = (EntidadesFinancieras) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.EntidadesFinancieras", id);
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

	public List findByExample(EntidadesFinancieras instance) {
		log.debug("finding EntidadesFinancieras instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EntidadesFinancieras").add(
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
