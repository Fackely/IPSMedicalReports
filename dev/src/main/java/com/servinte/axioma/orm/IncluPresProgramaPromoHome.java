package com.servinte.axioma.orm;

// Generated Jan 23, 2011 12:03:02 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class IncluPresProgramaPromo.
 * @see com.servinte.axioma.orm.IncluPresProgramaPromo
 * @author Hibernate Tools
 */
public class IncluPresProgramaPromoHome {

	private static final Log log = LogFactory
			.getLog(IncluPresProgramaPromoHome.class);

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

	public void persist(IncluPresProgramaPromo transientInstance) {
		log.debug("persisting IncluPresProgramaPromo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(IncluPresProgramaPromo instance) {
		log.debug("attaching dirty IncluPresProgramaPromo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IncluPresProgramaPromo instance) {
		log.debug("attaching clean IncluPresProgramaPromo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(IncluPresProgramaPromo persistentInstance) {
		log.debug("deleting IncluPresProgramaPromo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IncluPresProgramaPromo merge(IncluPresProgramaPromo detachedInstance) {
		log.debug("merging IncluPresProgramaPromo instance");
		try {
			IncluPresProgramaPromo result = (IncluPresProgramaPromo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public IncluPresProgramaPromo findById(long id) {
		log.debug("getting IncluPresProgramaPromo instance with id: " + id);
		try {
			IncluPresProgramaPromo instance = (IncluPresProgramaPromo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.IncluPresProgramaPromo",
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

	public List findByExample(IncluPresProgramaPromo instance) {
		log.debug("finding IncluPresProgramaPromo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.IncluPresProgramaPromo").add(
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
