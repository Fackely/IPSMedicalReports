package com.servinte.axioma.orm;

// Generated May 12, 2010 9:01:38 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NotasAdministrativas.
 * @see com.servinte.axioma.orm.NotasAdministrativas
 * @author Hibernate Tools
 */
public class NotasAdministrativasHome {

	private static final Log log = LogFactory
			.getLog(NotasAdministrativasHome.class);

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

	public void persist(NotasAdministrativas transientInstance) {
		log.debug("persisting NotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NotasAdministrativas instance) {
		log.debug("attaching dirty NotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NotasAdministrativas instance) {
		log.debug("attaching clean NotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NotasAdministrativas persistentInstance) {
		log.debug("deleting NotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NotasAdministrativas merge(NotasAdministrativas detachedInstance) {
		log.debug("merging NotasAdministrativas instance");
		try {
			NotasAdministrativas result = (NotasAdministrativas) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NotasAdministrativas findById(int id) {
		log.debug("getting NotasAdministrativas instance with id: " + id);
		try {
			NotasAdministrativas instance = (NotasAdministrativas) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NotasAdministrativas", id);
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

	public List findByExample(NotasAdministrativas instance) {
		log.debug("finding NotasAdministrativas instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NotasAdministrativas").add(
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
