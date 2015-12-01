package com.servinte.axioma.orm;

import com.servinte.axioma.hibernate.HibernateUtil;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class DetFaltanteSobrante.
 * @see com.servinte.axioma.orm.DetFaltanteSobrante
 * @author Hibernate Tools
 */
public class DetFaltanteSobranteHome {

	private static final Log log = LogFactory
			.getLog(DetFaltanteSobranteHome.class);

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

	public void persist(DetFaltanteSobrante transientInstance) {
		log.debug("persisting DetFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetFaltanteSobrante instance) {
		log.debug("attaching dirty DetFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetFaltanteSobrante instance) {
		log.debug("attaching clean DetFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetFaltanteSobrante persistentInstance) {
		log.debug("deleting DetFaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetFaltanteSobrante merge(DetFaltanteSobrante detachedInstance) {
		log.debug("merging DetFaltanteSobrante instance");
		try {
			DetFaltanteSobrante result = (DetFaltanteSobrante) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetFaltanteSobrante findById(long id) {
		log.debug("getting DetFaltanteSobrante instance with id: " + id);
		try {
			DetFaltanteSobrante instance = (DetFaltanteSobrante) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetFaltanteSobrante", id);
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

	public List findByExample(DetFaltanteSobrante instance) {
		log.debug("finding DetFaltanteSobrante instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetFaltanteSobrante").add(
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
