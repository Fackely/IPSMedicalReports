package com.servinte.axioma.orm;

// Generated 19/05/2011 10:17:50 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TiposFrecuencia.
 * @see com.servinte.axioma.orm.TiposFrecuencia
 * @author Hibernate Tools
 */
public class TiposFrecuenciaHome {

	private static final Log log = LogFactory.getLog(TiposFrecuenciaHome.class);

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

	public void persist(TiposFrecuencia transientInstance) {
		log.debug("persisting TiposFrecuencia instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposFrecuencia instance) {
		log.debug("attaching dirty TiposFrecuencia instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposFrecuencia instance) {
		log.debug("attaching clean TiposFrecuencia instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposFrecuencia persistentInstance) {
		log.debug("deleting TiposFrecuencia instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposFrecuencia merge(TiposFrecuencia detachedInstance) {
		log.debug("merging TiposFrecuencia instance");
		try {
			TiposFrecuencia result = (TiposFrecuencia) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposFrecuencia findById(int id) {
		log.debug("getting TiposFrecuencia instance with id: " + id);
		try {
			TiposFrecuencia instance = (TiposFrecuencia) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposFrecuencia", id);
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

	public List findByExample(TiposFrecuencia instance) {
		log.debug("finding TiposFrecuencia instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.TiposFrecuencia")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
