package com.servinte.axioma.orm;

// Generated Dec 13, 2010 11:42:38 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoAutorizacionCapitaSub.
 * @see com.servinte.axioma.orm.HistoAutorizacionCapitaSub
 * @author Hibernate Tools
 */
public class HistoAutorizacionCapitaSubHome {

	private static final Log log = LogFactory
			.getLog(HistoAutorizacionCapitaSubHome.class);

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

	public void persist(HistoAutorizacionCapitaSub transientInstance) {
		log.debug("persisting HistoAutorizacionCapitaSub instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoAutorizacionCapitaSub instance) {
		log.debug("attaching dirty HistoAutorizacionCapitaSub instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoAutorizacionCapitaSub instance) {
		log.debug("attaching clean HistoAutorizacionCapitaSub instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoAutorizacionCapitaSub persistentInstance) {
		log.debug("deleting HistoAutorizacionCapitaSub instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoAutorizacionCapitaSub merge(
			HistoAutorizacionCapitaSub detachedInstance) {
		log.debug("merging HistoAutorizacionCapitaSub instance");
		try {
			HistoAutorizacionCapitaSub result = (HistoAutorizacionCapitaSub) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoAutorizacionCapitaSub findById(long id) {
		log.debug("getting HistoAutorizacionCapitaSub instance with id: " + id);
		try {
			HistoAutorizacionCapitaSub instance = (HistoAutorizacionCapitaSub) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HistoAutorizacionCapitaSub",
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

	public List findByExample(HistoAutorizacionCapitaSub instance) {
		log.debug("finding HistoAutorizacionCapitaSub instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoAutorizacionCapitaSub").add(
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
