package com.servinte.axioma.orm;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreNivelAteClasein.
 * @see com.servinte.axioma.orm.CierreNivelAteClasein
 * @author Hibernate Tools
 */
public class CierreNivelAteClaseinHome {

	private static final Log log = LogFactory
			.getLog(CierreNivelAteClaseinHome.class);

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

	public void persist(CierreNivelAteClasein transientInstance) {
		log.debug("persisting CierreNivelAteClasein instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreNivelAteClasein instance) {
		log.debug("attaching dirty CierreNivelAteClasein instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreNivelAteClasein instance) {
		log.debug("attaching clean CierreNivelAteClasein instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreNivelAteClasein persistentInstance) {
		log.debug("deleting CierreNivelAteClasein instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreNivelAteClasein merge(CierreNivelAteClasein detachedInstance) {
		log.debug("merging CierreNivelAteClasein instance");
		try {
			CierreNivelAteClasein result = (CierreNivelAteClasein) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreNivelAteClasein findById(long id) {
		log.debug("getting CierreNivelAteClasein instance with id: " + id);
		try {
			CierreNivelAteClasein instance = (CierreNivelAteClasein) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CierreNivelAteClasein",
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

	public List findByExample(CierreNivelAteClasein instance) {
		log.debug("finding CierreNivelAteClasein instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreNivelAteClasein").add(
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
