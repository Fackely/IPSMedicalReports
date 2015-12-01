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
 * Home object for domain model class HisRequisitosPacSubcuenta.
 * @see com.servinte.axioma.orm.HisRequisitosPacSubcuenta
 * @author Hibernate Tools
 */
public class HisRequisitosPacSubcuentaHome {

	private static final Log log = LogFactory
			.getLog(HisRequisitosPacSubcuentaHome.class);

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

	public void persist(HisRequisitosPacSubcuenta transientInstance) {
		log.debug("persisting HisRequisitosPacSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HisRequisitosPacSubcuenta instance) {
		log.debug("attaching dirty HisRequisitosPacSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HisRequisitosPacSubcuenta instance) {
		log.debug("attaching clean HisRequisitosPacSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HisRequisitosPacSubcuenta persistentInstance) {
		log.debug("deleting HisRequisitosPacSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HisRequisitosPacSubcuenta merge(
			HisRequisitosPacSubcuenta detachedInstance) {
		log.debug("merging HisRequisitosPacSubcuenta instance");
		try {
			HisRequisitosPacSubcuenta result = (HisRequisitosPacSubcuenta) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HisRequisitosPacSubcuenta findById(
			com.servinte.axioma.orm.HisRequisitosPacSubcuentaId id) {
		log.debug("getting HisRequisitosPacSubcuenta instance with id: " + id);
		try {
			HisRequisitosPacSubcuenta instance = (HisRequisitosPacSubcuenta) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HisRequisitosPacSubcuenta",
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

	public List findByExample(HisRequisitosPacSubcuenta instance) {
		log.debug("finding HisRequisitosPacSubcuenta instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HisRequisitosPacSubcuenta").add(
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
