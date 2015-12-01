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
 * Home object for domain model class RestriccionesCuadro.
 * @see com.servinte.axioma.orm.RestriccionesCuadro
 * @author Hibernate Tools
 */
public class RestriccionesCuadroHome {

	private static final Log log = LogFactory
			.getLog(RestriccionesCuadroHome.class);

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

	public void persist(RestriccionesCuadro transientInstance) {
		log.debug("persisting RestriccionesCuadro instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RestriccionesCuadro instance) {
		log.debug("attaching dirty RestriccionesCuadro instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RestriccionesCuadro instance) {
		log.debug("attaching clean RestriccionesCuadro instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RestriccionesCuadro persistentInstance) {
		log.debug("deleting RestriccionesCuadro instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RestriccionesCuadro merge(RestriccionesCuadro detachedInstance) {
		log.debug("merging RestriccionesCuadro instance");
		try {
			RestriccionesCuadro result = (RestriccionesCuadro) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RestriccionesCuadro findById(
			com.servinte.axioma.orm.RestriccionesCuadroId id) {
		log.debug("getting RestriccionesCuadro instance with id: " + id);
		try {
			RestriccionesCuadro instance = (RestriccionesCuadro) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.RestriccionesCuadro", id);
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

	public List findByExample(RestriccionesCuadro instance) {
		log.debug("finding RestriccionesCuadro instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RestriccionesCuadro").add(
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
