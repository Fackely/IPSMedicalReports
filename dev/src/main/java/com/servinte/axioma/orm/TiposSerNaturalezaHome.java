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
 * Home object for domain model class TiposSerNaturaleza.
 * @see com.servinte.axioma.orm.TiposSerNaturaleza
 * @author Hibernate Tools
 */
public class TiposSerNaturalezaHome {

	private static final Log log = LogFactory
			.getLog(TiposSerNaturalezaHome.class);

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

	public void persist(TiposSerNaturaleza transientInstance) {
		log.debug("persisting TiposSerNaturaleza instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposSerNaturaleza instance) {
		log.debug("attaching dirty TiposSerNaturaleza instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposSerNaturaleza instance) {
		log.debug("attaching clean TiposSerNaturaleza instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposSerNaturaleza persistentInstance) {
		log.debug("deleting TiposSerNaturaleza instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposSerNaturaleza merge(TiposSerNaturaleza detachedInstance) {
		log.debug("merging TiposSerNaturaleza instance");
		try {
			TiposSerNaturaleza result = (TiposSerNaturaleza) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposSerNaturaleza findById(
			com.servinte.axioma.orm.TiposSerNaturalezaId id) {
		log.debug("getting TiposSerNaturaleza instance with id: " + id);
		try {
			TiposSerNaturaleza instance = (TiposSerNaturaleza) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposSerNaturaleza", id);
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

	public List findByExample(TiposSerNaturaleza instance) {
		log.debug("finding TiposSerNaturaleza instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposSerNaturaleza").add(
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
