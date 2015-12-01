package com.servinte.axioma.orm;

// Generated Apr 9, 2010 3:23:30 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TransportadoraValores.
 * @see com.servinte.axioma.orm.TransportadoraValores
 * @author Hibernate Tools
 */
public class TransportadoraValoresHome {

	private static final Log log = LogFactory
			.getLog(TransportadoraValoresHome.class);

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

	public void persist(TransportadoraValores transientInstance) {
		log.debug("persisting TransportadoraValores instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TransportadoraValores instance) {
		log.debug("attaching dirty TransportadoraValores instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TransportadoraValores instance) {
		log.debug("attaching clean TransportadoraValores instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TransportadoraValores persistentInstance) {
		log.debug("deleting TransportadoraValores instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TransportadoraValores merge(TransportadoraValores detachedInstance) {
		log.debug("merging TransportadoraValores instance");
		try {
			TransportadoraValores result = (TransportadoraValores) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TransportadoraValores findById(int id) {
		log.debug("getting TransportadoraValores instance with id: " + id);
		try {
			TransportadoraValores instance = (TransportadoraValores) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.TransportadoraValores", id);
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

	public List findByExample(TransportadoraValores instance) {
		log.debug("finding TransportadoraValores instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TransportadoraValores").add(
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
