package com.servinte.axioma.orm;

// Generated Sep 10, 2010 6:18:13 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class GrupoInventario.
 * @see com.servinte.axioma.orm.GrupoInventario
 * @author Hibernate Tools
 */
public class GrupoInventarioHome {

	private static final Log log = LogFactory.getLog(GrupoInventarioHome.class);

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

	public void persist(GrupoInventario transientInstance) {
		log.debug("persisting GrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(GrupoInventario instance) {
		log.debug("attaching dirty GrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GrupoInventario instance) {
		log.debug("attaching clean GrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(GrupoInventario persistentInstance) {
		log.debug("deleting GrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GrupoInventario merge(GrupoInventario detachedInstance) {
		log.debug("merging GrupoInventario instance");
		try {
			GrupoInventario result = (GrupoInventario) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public GrupoInventario findById(com.servinte.axioma.orm.GrupoInventarioId id) {
		log.debug("getting GrupoInventario instance with id: " + id);
		try {
			GrupoInventario instance = (GrupoInventario) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.GrupoInventario", id);
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

	public List findByExample(GrupoInventario instance) {
		log.debug("finding GrupoInventario instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.GrupoInventario").add(
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
