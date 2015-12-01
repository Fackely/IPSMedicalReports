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
 * Home object for domain model class TiposMotivoAtencion.
 * @see com.servinte.axioma.orm.TiposMotivoAtencion
 * @author Hibernate Tools
 */
public class TiposMotivoAtencionHome {

	private static final Log log = LogFactory
			.getLog(TiposMotivoAtencionHome.class);

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

	public void persist(TiposMotivoAtencion transientInstance) {
		log.debug("persisting TiposMotivoAtencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposMotivoAtencion instance) {
		log.debug("attaching dirty TiposMotivoAtencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposMotivoAtencion instance) {
		log.debug("attaching clean TiposMotivoAtencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposMotivoAtencion persistentInstance) {
		log.debug("deleting TiposMotivoAtencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposMotivoAtencion merge(TiposMotivoAtencion detachedInstance) {
		log.debug("merging TiposMotivoAtencion instance");
		try {
			TiposMotivoAtencion result = (TiposMotivoAtencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposMotivoAtencion findById(int id) {
		log.debug("getting TiposMotivoAtencion instance with id: " + id);
		try {
			TiposMotivoAtencion instance = (TiposMotivoAtencion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposMotivoAtencion", id);
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

	public List findByExample(TiposMotivoAtencion instance) {
		log.debug("finding TiposMotivoAtencion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposMotivoAtencion").add(
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
