package com.servinte.axioma.orm;

// Generated Dec 13, 2010 8:47:19 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EstanciaViaIngCentroCosto.
 * @see com.servinte.axioma.orm.EstanciaViaIngCentroCosto
 * @author Hibernate Tools
 */
public class EstanciaViaIngCentroCostoHome {

	private static final Log log = LogFactory
			.getLog(EstanciaViaIngCentroCostoHome.class);

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

	public void persist(EstanciaViaIngCentroCosto transientInstance) {
		log.debug("persisting EstanciaViaIngCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EstanciaViaIngCentroCosto instance) {
		log.debug("attaching dirty EstanciaViaIngCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EstanciaViaIngCentroCosto instance) {
		log.debug("attaching clean EstanciaViaIngCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EstanciaViaIngCentroCosto persistentInstance) {
		log.debug("deleting EstanciaViaIngCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EstanciaViaIngCentroCosto merge(
			EstanciaViaIngCentroCosto detachedInstance) {
		log.debug("merging EstanciaViaIngCentroCosto instance");
		try {
			EstanciaViaIngCentroCosto result = (EstanciaViaIngCentroCosto) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EstanciaViaIngCentroCosto findById(long id) {
		log.debug("getting EstanciaViaIngCentroCosto instance with id: " + id);
		try {
			EstanciaViaIngCentroCosto instance = (EstanciaViaIngCentroCosto) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.EstanciaViaIngCentroCosto",
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

	public List findByExample(EstanciaViaIngCentroCosto instance) {
		log.debug("finding EstanciaViaIngCentroCosto instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EstanciaViaIngCentroCosto").add(
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
