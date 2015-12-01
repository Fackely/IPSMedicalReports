package com.servinte.axioma.orm;

// Generated Sep 23, 2010 2:39:35 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SubgrupoInventario.
 * @see com.servinte.axioma.orm.SubgrupoInventario
 * @author Hibernate Tools
 */
public class SubgrupoInventarioHome {

	private static final Log log = LogFactory
			.getLog(SubgrupoInventarioHome.class);

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

	public void persist(SubgrupoInventario transientInstance) {
		log.debug("persisting SubgrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SubgrupoInventario instance) {
		log.debug("attaching dirty SubgrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SubgrupoInventario instance) {
		log.debug("attaching clean SubgrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SubgrupoInventario persistentInstance) {
		log.debug("deleting SubgrupoInventario instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SubgrupoInventario merge(SubgrupoInventario detachedInstance) {
		log.debug("merging SubgrupoInventario instance");
		try {
			SubgrupoInventario result = (SubgrupoInventario) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SubgrupoInventario findById(
			com.servinte.axioma.orm.SubgrupoInventarioId id) {
		log.debug("getting SubgrupoInventario instance with id: " + id);
		try {
			SubgrupoInventario instance = (SubgrupoInventario) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SubgrupoInventario", id);
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

	public List findByExample(SubgrupoInventario instance) {
		log.debug("finding SubgrupoInventario instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SubgrupoInventario").add(
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
