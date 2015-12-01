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
 * Home object for domain model class LogLecturaPlanosEnt.
 * @see com.servinte.axioma.orm.LogLecturaPlanosEnt
 * @author Hibernate Tools
 */
public class LogLecturaPlanosEntHome {

	private static final Log log = LogFactory
			.getLog(LogLecturaPlanosEntHome.class);

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

	public void persist(LogLecturaPlanosEnt transientInstance) {
		log.debug("persisting LogLecturaPlanosEnt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogLecturaPlanosEnt instance) {
		log.debug("attaching dirty LogLecturaPlanosEnt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogLecturaPlanosEnt instance) {
		log.debug("attaching clean LogLecturaPlanosEnt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogLecturaPlanosEnt persistentInstance) {
		log.debug("deleting LogLecturaPlanosEnt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogLecturaPlanosEnt merge(LogLecturaPlanosEnt detachedInstance) {
		log.debug("merging LogLecturaPlanosEnt instance");
		try {
			LogLecturaPlanosEnt result = (LogLecturaPlanosEnt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogLecturaPlanosEnt findById(long id) {
		log.debug("getting LogLecturaPlanosEnt instance with id: " + id);
		try {
			LogLecturaPlanosEnt instance = (LogLecturaPlanosEnt) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogLecturaPlanosEnt", id);
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

	public List findByExample(LogLecturaPlanosEnt instance) {
		log.debug("finding LogLecturaPlanosEnt instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogLecturaPlanosEnt").add(
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
