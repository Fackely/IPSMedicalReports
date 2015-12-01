package com.servinte.axioma.orm;

// Generated Jun 21, 2010 11:07:52 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TotalFaltanteSobrante.
 * @see com.servinte.axioma.orm.TotalFaltanteSobrante
 * @author Hibernate Tools
 */
public class TotalFaltanteSobranteHome {

	private static final Log log = LogFactory
			.getLog(TotalFaltanteSobranteHome.class);

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

	public void persist(TotalFaltanteSobrante transientInstance) {
		log.debug("persisting TotalFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TotalFaltanteSobrante instance) {
		log.debug("attaching dirty TotalFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TotalFaltanteSobrante instance) {
		log.debug("attaching clean TotalFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TotalFaltanteSobrante persistentInstance) {
		log.debug("deleting TotalFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TotalFaltanteSobrante merge(TotalFaltanteSobrante detachedInstance) {
		log.debug("merging TotalFaltanteSobrante instance");
		try {
			TotalFaltanteSobrante result = (TotalFaltanteSobrante) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TotalFaltanteSobrante findById(
			com.servinte.axioma.orm.TotalFaltanteSobranteId id) {
		log.debug("getting TotalFaltanteSobrante instance with id: " + id);
		try {
			TotalFaltanteSobrante instance = (TotalFaltanteSobrante) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.TotalFaltanteSobrante", id);
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

	public List findByExample(TotalFaltanteSobrante instance) {
		log.debug("finding TotalFaltanteSobrante instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TotalFaltanteSobrante").add(
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
