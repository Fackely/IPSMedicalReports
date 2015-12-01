package com.servinte.axioma.orm;

// Generated Jan 18, 2011 11:29:32 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ConsecutivosInventarios.
 * @see com.servinte.axioma.orm.ConsecutivosInventarios
 * @author Hibernate Tools
 */
public class ConsecutivosInventariosHome {

	private static final Log log = LogFactory
			.getLog(ConsecutivosInventariosHome.class);

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

	public void persist(ConsecutivosInventarios transientInstance) {
		log.debug("persisting ConsecutivosInventarios instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ConsecutivosInventarios instance) {
		log.debug("attaching dirty ConsecutivosInventarios instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ConsecutivosInventarios instance) {
		log.debug("attaching clean ConsecutivosInventarios instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ConsecutivosInventarios persistentInstance) {
		log.debug("deleting ConsecutivosInventarios instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ConsecutivosInventarios merge(
			ConsecutivosInventarios detachedInstance) {
		log.debug("merging ConsecutivosInventarios instance");
		try {
			ConsecutivosInventarios result = (ConsecutivosInventarios) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ConsecutivosInventarios findById(int id) {
		log.debug("getting ConsecutivosInventarios instance with id: " + id);
		try {
			ConsecutivosInventarios instance = (ConsecutivosInventarios) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ConsecutivosInventarios",
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

	public List findByExample(ConsecutivosInventarios instance) {
		log.debug("finding ConsecutivosInventarios instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ConsecutivosInventarios").add(
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
