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
 * Home object for domain model class FaltanteSobrante.
 * @see com.servinte.axioma.orm.FaltanteSobrante
 * @author Hibernate Tools
 */
public class FaltanteSobranteHome {

	private static final Log log = LogFactory
			.getLog(FaltanteSobranteHome.class);

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

	public void persist(FaltanteSobrante transientInstance) {
		log.debug("persisting FaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FaltanteSobrante instance) {
		log.debug("attaching dirty FaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FaltanteSobrante instance) {
		log.debug("attaching clean FaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FaltanteSobrante persistentInstance) {
		log.debug("deleting FaltanteSobrante instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FaltanteSobrante merge(FaltanteSobrante detachedInstance) {
		log.debug("merging FaltanteSobrante instance");
		try {
			FaltanteSobrante result = (FaltanteSobrante) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FaltanteSobrante findById(long id) {
		log.debug("getting FaltanteSobrante instance with id: " + id);
		try {
			FaltanteSobrante instance = (FaltanteSobrante) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.FaltanteSobrante", id);
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

	public List findByExample(FaltanteSobrante instance) {
		log.debug("finding FaltanteSobrante instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.FaltanteSobrante").add(
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
