package com.servinte.axioma.orm;

// Generated 15/04/2010 03:20:53 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TarjetasFinancieras.
 * @see com.servinte.axioma.orm.TarjetasFinancieras
 * @author Hibernate Tools
 */
public class TarjetasFinancierasHome {

	private static final Log log = LogFactory
			.getLog(TarjetasFinancierasHome.class);

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

	public void persist(TarjetasFinancieras transientInstance) {
		log.debug("persisting TarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TarjetasFinancieras instance) {
		log.debug("attaching dirty TarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TarjetasFinancieras instance) {
		log.debug("attaching clean TarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TarjetasFinancieras persistentInstance) {
		log.debug("deleting TarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TarjetasFinancieras merge(TarjetasFinancieras detachedInstance) {
		log.debug("merging TarjetasFinancieras instance");
		try {
			TarjetasFinancieras result = (TarjetasFinancieras) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TarjetasFinancieras findById(int id) {
		log.debug("getting TarjetasFinancieras instance with id: " + id);
		try {
			TarjetasFinancieras instance = (TarjetasFinancieras) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TarjetasFinancieras", id);
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

	public List findByExample(TarjetasFinancieras instance) {
		log.debug("finding TarjetasFinancieras instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TarjetasFinancieras").add(
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
