package com.servinte.axioma.orm;

// Generated Jan 12, 2011 9:01:07 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class IncluProgramaConvenio.
 * @see com.servinte.axioma.orm.IncluProgramaConvenio
 * @author Hibernate Tools
 */
public class IncluProgramaConvenioHome {

	private static final Log log = LogFactory
			.getLog(IncluProgramaConvenioHome.class);

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

	public void persist(IncluProgramaConvenio transientInstance) {
		log.debug("persisting IncluProgramaConvenio instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(IncluProgramaConvenio instance) {
		log.debug("attaching dirty IncluProgramaConvenio instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IncluProgramaConvenio instance) {
		log.debug("attaching clean IncluProgramaConvenio instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(IncluProgramaConvenio persistentInstance) {
		log.debug("deleting IncluProgramaConvenio instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IncluProgramaConvenio merge(IncluProgramaConvenio detachedInstance) {
		log.debug("merging IncluProgramaConvenio instance");
		try {
			IncluProgramaConvenio result = (IncluProgramaConvenio) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public IncluProgramaConvenio findById(long id) {
		log.debug("getting IncluProgramaConvenio instance with id: " + id);
		try {
			IncluProgramaConvenio instance = (IncluProgramaConvenio) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.IncluProgramaConvenio", id);
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

	public List findByExample(IncluProgramaConvenio instance) {
		log.debug("finding IncluProgramaConvenio instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.IncluProgramaConvenio").add(
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
