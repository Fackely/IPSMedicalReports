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
 * Home object for domain model class AdjuntosSubcuenta.
 * @see com.servinte.axioma.orm.AdjuntosSubcuenta
 * @author Hibernate Tools
 */
public class AdjuntosSubcuentaHome {

	private static final Log log = LogFactory
			.getLog(AdjuntosSubcuentaHome.class);

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

	public void persist(AdjuntosSubcuenta transientInstance) {
		log.debug("persisting AdjuntosSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AdjuntosSubcuenta instance) {
		log.debug("attaching dirty AdjuntosSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AdjuntosSubcuenta instance) {
		log.debug("attaching clean AdjuntosSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AdjuntosSubcuenta persistentInstance) {
		log.debug("deleting AdjuntosSubcuenta instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AdjuntosSubcuenta merge(AdjuntosSubcuenta detachedInstance) {
		log.debug("merging AdjuntosSubcuenta instance");
		try {
			AdjuntosSubcuenta result = (AdjuntosSubcuenta) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AdjuntosSubcuenta findById(long id) {
		log.debug("getting AdjuntosSubcuenta instance with id: " + id);
		try {
			AdjuntosSubcuenta instance = (AdjuntosSubcuenta) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AdjuntosSubcuenta", id);
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

	public List findByExample(AdjuntosSubcuenta instance) {
		log.debug("finding AdjuntosSubcuenta instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AdjuntosSubcuenta").add(
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
