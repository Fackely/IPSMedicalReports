package com.servinte.axioma.orm;

// Generated Jan 28, 2011 9:03:33 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UsoConsecutivosCentroAten.
 * @see com.servinte.axioma.orm.UsoConsecutivosCentroAten
 * @author Hibernate Tools
 */
public class UsoConsecutivosCentroAtenHome {

	private static final Log log = LogFactory
			.getLog(UsoConsecutivosCentroAtenHome.class);

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

	public void persist(UsoConsecutivosCentroAten transientInstance) {
		log.debug("persisting UsoConsecutivosCentroAten instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UsoConsecutivosCentroAten instance) {
		log.debug("attaching dirty UsoConsecutivosCentroAten instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UsoConsecutivosCentroAten instance) {
		log.debug("attaching clean UsoConsecutivosCentroAten instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UsoConsecutivosCentroAten persistentInstance) {
		log.debug("deleting UsoConsecutivosCentroAten instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UsoConsecutivosCentroAten merge(
			UsoConsecutivosCentroAten detachedInstance) {
		log.debug("merging UsoConsecutivosCentroAten instance");
		try {
			UsoConsecutivosCentroAten result = (UsoConsecutivosCentroAten) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UsoConsecutivosCentroAten findById(long id) {
		log.debug("getting UsoConsecutivosCentroAten instance with id: " + id);
		try {
			UsoConsecutivosCentroAten instance = (UsoConsecutivosCentroAten) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.UsoConsecutivosCentroAten",
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

	public List findByExample(UsoConsecutivosCentroAten instance) {
		log.debug("finding UsoConsecutivosCentroAten instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UsoConsecutivosCentroAten").add(
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
