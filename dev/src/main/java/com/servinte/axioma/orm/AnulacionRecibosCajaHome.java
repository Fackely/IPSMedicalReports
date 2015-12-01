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
 * Home object for domain model class AnulacionRecibosCaja.
 * @see com.servinte.axioma.orm.AnulacionRecibosCaja
 * @author Hibernate Tools
 */
public class AnulacionRecibosCajaHome {

	private static final Log log = LogFactory
			.getLog(AnulacionRecibosCajaHome.class);

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

	public void persist(AnulacionRecibosCaja transientInstance) {
		log.debug("persisting AnulacionRecibosCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AnulacionRecibosCaja instance) {
		log.debug("attaching dirty AnulacionRecibosCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AnulacionRecibosCaja instance) {
		log.debug("attaching clean AnulacionRecibosCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AnulacionRecibosCaja persistentInstance) {
		log.debug("deleting AnulacionRecibosCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AnulacionRecibosCaja merge(AnulacionRecibosCaja detachedInstance) {
		log.debug("merging AnulacionRecibosCaja instance");
		try {
			AnulacionRecibosCaja result = (AnulacionRecibosCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AnulacionRecibosCaja findById(
			com.servinte.axioma.orm.AnulacionRecibosCajaId id) {
		log.debug("getting AnulacionRecibosCaja instance with id: " + id);
		try {
			AnulacionRecibosCaja instance = (AnulacionRecibosCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AnulacionRecibosCaja", id);
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

	public List findByExample(AnulacionRecibosCaja instance) {
		log.debug("finding AnulacionRecibosCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AnulacionRecibosCaja").add(
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
