package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LecturaPlanosAh.
 * @see com.servinte.axioma.orm.LecturaPlanosAh
 * @author Hibernate Tools
 */
public class LecturaPlanosAhHome {

	private static final Log log = LogFactory.getLog(LecturaPlanosAhHome.class);

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

	public void persist(LecturaPlanosAh transientInstance) {
		log.debug("persisting LecturaPlanosAh instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LecturaPlanosAh instance) {
		log.debug("attaching dirty LecturaPlanosAh instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LecturaPlanosAh instance) {
		log.debug("attaching clean LecturaPlanosAh instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LecturaPlanosAh persistentInstance) {
		log.debug("deleting LecturaPlanosAh instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LecturaPlanosAh merge(LecturaPlanosAh detachedInstance) {
		log.debug("merging LecturaPlanosAh instance");
		try {
			LecturaPlanosAh result = (LecturaPlanosAh) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LecturaPlanosAh findById(com.servinte.axioma.orm.LecturaPlanosAhId id) {
		log.debug("getting LecturaPlanosAh instance with id: " + id);
		try {
			LecturaPlanosAh instance = (LecturaPlanosAh) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LecturaPlanosAh", id);
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

	public List findByExample(LecturaPlanosAh instance) {
		log.debug("finding LecturaPlanosAh instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LecturaPlanosAh").add(
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
