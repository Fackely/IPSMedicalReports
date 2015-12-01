package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetAnulCargosFarmacia.
 * @see com.servinte.axioma.orm.DetAnulCargosFarmacia
 * @author Hibernate Tools
 */
public class DetAnulCargosFarmaciaHome {

	private static final Log log = LogFactory
			.getLog(DetAnulCargosFarmaciaHome.class);

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

	public void persist(DetAnulCargosFarmacia transientInstance) {
		log.debug("persisting DetAnulCargosFarmacia instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetAnulCargosFarmacia instance) {
		log.debug("attaching dirty DetAnulCargosFarmacia instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetAnulCargosFarmacia instance) {
		log.debug("attaching clean DetAnulCargosFarmacia instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetAnulCargosFarmacia persistentInstance) {
		log.debug("deleting DetAnulCargosFarmacia instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetAnulCargosFarmacia merge(DetAnulCargosFarmacia detachedInstance) {
		log.debug("merging DetAnulCargosFarmacia instance");
		try {
			DetAnulCargosFarmacia result = (DetAnulCargosFarmacia) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetAnulCargosFarmacia findById(
			com.servinte.axioma.orm.DetAnulCargosFarmaciaId id) {
		log.debug("getting DetAnulCargosFarmacia instance with id: " + id);
		try {
			DetAnulCargosFarmacia instance = (DetAnulCargosFarmacia) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.DetAnulCargosFarmacia", id);
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

	public List findByExample(DetAnulCargosFarmacia instance) {
		log.debug("finding DetAnulCargosFarmacia instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetAnulCargosFarmacia").add(
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
