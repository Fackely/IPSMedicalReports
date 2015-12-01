package com.servinte.axioma.orm;

// Generated Jun 8, 2010 5:26:03 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class RecomSerproSerpro.
 * @see com.servinte.axioma.orm.RecomSerproSerpro
 * @author Hibernate Tools
 */
public class RecomSerproSerproHome {

	private static final Log log = LogFactory
			.getLog(RecomSerproSerproHome.class);

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

	public void persist(RecomSerproSerpro transientInstance) {
		log.debug("persisting RecomSerproSerpro instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RecomSerproSerpro instance) {
		log.debug("attaching dirty RecomSerproSerpro instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RecomSerproSerpro instance) {
		log.debug("attaching clean RecomSerproSerpro instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RecomSerproSerpro persistentInstance) {
		log.debug("deleting RecomSerproSerpro instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RecomSerproSerpro merge(RecomSerproSerpro detachedInstance) {
		log.debug("merging RecomSerproSerpro instance");
		try {
			RecomSerproSerpro result = (RecomSerproSerpro) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RecomSerproSerpro findById(int id) {
		log.debug("getting RecomSerproSerpro instance with id: " + id);
		try {
			RecomSerproSerpro instance = (RecomSerproSerpro) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.RecomSerproSerpro", id);
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

	public List findByExample(RecomSerproSerpro instance) {
		log.debug("finding RecomSerproSerpro instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RecomSerproSerpro").add(
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
