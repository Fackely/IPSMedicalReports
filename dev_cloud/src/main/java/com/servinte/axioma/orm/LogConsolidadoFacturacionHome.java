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
 * Home object for domain model class LogConsolidadoFacturacion.
 * @see com.servinte.axioma.orm.LogConsolidadoFacturacion
 * @author Hibernate Tools
 */
public class LogConsolidadoFacturacionHome {

	private static final Log log = LogFactory
			.getLog(LogConsolidadoFacturacionHome.class);

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

	public void persist(LogConsolidadoFacturacion transientInstance) {
		log.debug("persisting LogConsolidadoFacturacion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogConsolidadoFacturacion instance) {
		log.debug("attaching dirty LogConsolidadoFacturacion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogConsolidadoFacturacion instance) {
		log.debug("attaching clean LogConsolidadoFacturacion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogConsolidadoFacturacion persistentInstance) {
		log.debug("deleting LogConsolidadoFacturacion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogConsolidadoFacturacion merge(
			LogConsolidadoFacturacion detachedInstance) {
		log.debug("merging LogConsolidadoFacturacion instance");
		try {
			LogConsolidadoFacturacion result = (LogConsolidadoFacturacion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogConsolidadoFacturacion findById(int id) {
		log.debug("getting LogConsolidadoFacturacion instance with id: " + id);
		try {
			LogConsolidadoFacturacion instance = (LogConsolidadoFacturacion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogConsolidadoFacturacion",
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

	public List findByExample(LogConsolidadoFacturacion instance) {
		log.debug("finding LogConsolidadoFacturacion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogConsolidadoFacturacion").add(
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
