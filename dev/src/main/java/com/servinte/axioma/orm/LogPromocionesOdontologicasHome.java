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
 * Home object for domain model class LogPromocionesOdontologicas.
 * @see com.servinte.axioma.orm.LogPromocionesOdontologicas
 * @author Hibernate Tools
 */
public class LogPromocionesOdontologicasHome {

	private static final Log log = LogFactory
			.getLog(LogPromocionesOdontologicasHome.class);

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

	public void persist(LogPromocionesOdontologicas transientInstance) {
		log.debug("persisting LogPromocionesOdontologicas instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogPromocionesOdontologicas instance) {
		log.debug("attaching dirty LogPromocionesOdontologicas instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogPromocionesOdontologicas instance) {
		log.debug("attaching clean LogPromocionesOdontologicas instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogPromocionesOdontologicas persistentInstance) {
		log.debug("deleting LogPromocionesOdontologicas instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogPromocionesOdontologicas merge(
			LogPromocionesOdontologicas detachedInstance) {
		log.debug("merging LogPromocionesOdontologicas instance");
		try {
			LogPromocionesOdontologicas result = (LogPromocionesOdontologicas) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogPromocionesOdontologicas findById(int id) {
		log
				.debug("getting LogPromocionesOdontologicas instance with id: "
						+ id);
		try {
			LogPromocionesOdontologicas instance = (LogPromocionesOdontologicas) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogPromocionesOdontologicas",
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

	public List findByExample(LogPromocionesOdontologicas instance) {
		log.debug("finding LogPromocionesOdontologicas instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogPromocionesOdontologicas").add(
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
