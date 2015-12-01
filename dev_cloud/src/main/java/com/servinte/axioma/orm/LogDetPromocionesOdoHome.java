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
 * Home object for domain model class LogDetPromocionesOdo.
 * @see com.servinte.axioma.orm.LogDetPromocionesOdo
 * @author Hibernate Tools
 */
public class LogDetPromocionesOdoHome {

	private static final Log log = LogFactory
			.getLog(LogDetPromocionesOdoHome.class);

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

	public void persist(LogDetPromocionesOdo transientInstance) {
		log.debug("persisting LogDetPromocionesOdo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogDetPromocionesOdo instance) {
		log.debug("attaching dirty LogDetPromocionesOdo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogDetPromocionesOdo instance) {
		log.debug("attaching clean LogDetPromocionesOdo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogDetPromocionesOdo persistentInstance) {
		log.debug("deleting LogDetPromocionesOdo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogDetPromocionesOdo merge(LogDetPromocionesOdo detachedInstance) {
		log.debug("merging LogDetPromocionesOdo instance");
		try {
			LogDetPromocionesOdo result = (LogDetPromocionesOdo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogDetPromocionesOdo findById(int id) {
		log.debug("getting LogDetPromocionesOdo instance with id: " + id);
		try {
			LogDetPromocionesOdo instance = (LogDetPromocionesOdo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogDetPromocionesOdo", id);
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

	public List findByExample(LogDetPromocionesOdo instance) {
		log.debug("finding LogDetPromocionesOdo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogDetPromocionesOdo").add(
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
