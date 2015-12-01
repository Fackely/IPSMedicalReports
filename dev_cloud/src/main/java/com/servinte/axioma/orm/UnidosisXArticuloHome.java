package com.servinte.axioma.orm;

// Generated Jan 25, 2011 12:12:11 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UnidosisXArticulo.
 * @see com.servinte.axioma.orm.UnidosisXArticulo
 * @author Hibernate Tools
 */
public class UnidosisXArticuloHome {

	private static final Log log = LogFactory
			.getLog(UnidosisXArticuloHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(UnidosisXArticulo transientInstance) {
		log.debug("persisting UnidosisXArticulo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UnidosisXArticulo instance) {
		log.debug("attaching dirty UnidosisXArticulo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UnidosisXArticulo instance) {
		log.debug("attaching clean UnidosisXArticulo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UnidosisXArticulo persistentInstance) {
		log.debug("deleting UnidosisXArticulo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UnidosisXArticulo merge(UnidosisXArticulo detachedInstance) {
		log.debug("merging UnidosisXArticulo instance");
		try {
			UnidosisXArticulo result = (UnidosisXArticulo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UnidosisXArticulo findById(long id) {
		log.debug("getting UnidosisXArticulo instance with id: " + id);
		try {
			UnidosisXArticulo instance = (UnidosisXArticulo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UnidosisXArticulo", id);
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

	public List findByExample(UnidosisXArticulo instance) {
		log.debug("finding UnidosisXArticulo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UnidosisXArticulo").add(
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
