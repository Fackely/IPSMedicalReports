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
 * Home object for domain model class ClasOdoSecEspe.
 * @see com.servinte.axioma.orm.ClasOdoSecEspe
 * @author Hibernate Tools
 */
public class ClasOdoSecEspeHome {

	private static final Log log = LogFactory.getLog(ClasOdoSecEspeHome.class);

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

	public void persist(ClasOdoSecEspe transientInstance) {
		log.debug("persisting ClasOdoSecEspe instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ClasOdoSecEspe instance) {
		log.debug("attaching dirty ClasOdoSecEspe instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ClasOdoSecEspe instance) {
		log.debug("attaching clean ClasOdoSecEspe instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ClasOdoSecEspe persistentInstance) {
		log.debug("deleting ClasOdoSecEspe instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ClasOdoSecEspe merge(ClasOdoSecEspe detachedInstance) {
		log.debug("merging ClasOdoSecEspe instance");
		try {
			ClasOdoSecEspe result = (ClasOdoSecEspe) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ClasOdoSecEspe findById(int id) {
		log.debug("getting ClasOdoSecEspe instance with id: " + id);
		try {
			ClasOdoSecEspe instance = (ClasOdoSecEspe) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ClasOdoSecEspe", id);
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

	public List findByExample(ClasOdoSecEspe instance) {
		log.debug("finding ClasOdoSecEspe instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ClasOdoSecEspe").add(
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
