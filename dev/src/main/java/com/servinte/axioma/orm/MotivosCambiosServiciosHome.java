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
 * Home object for domain model class MotivosCambiosServicios.
 * @see com.servinte.axioma.orm.MotivosCambiosServicios
 * @author Hibernate Tools
 */
public class MotivosCambiosServiciosHome {

	private static final Log log = LogFactory
			.getLog(MotivosCambiosServiciosHome.class);

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

	public void persist(MotivosCambiosServicios transientInstance) {
		log.debug("persisting MotivosCambiosServicios instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MotivosCambiosServicios instance) {
		log.debug("attaching dirty MotivosCambiosServicios instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MotivosCambiosServicios instance) {
		log.debug("attaching clean MotivosCambiosServicios instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MotivosCambiosServicios persistentInstance) {
		log.debug("deleting MotivosCambiosServicios instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MotivosCambiosServicios merge(
			MotivosCambiosServicios detachedInstance) {
		log.debug("merging MotivosCambiosServicios instance");
		try {
			MotivosCambiosServicios result = (MotivosCambiosServicios) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MotivosCambiosServicios findById(int id) {
		log.debug("getting MotivosCambiosServicios instance with id: " + id);
		try {
			MotivosCambiosServicios instance = (MotivosCambiosServicios) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MotivosCambiosServicios",
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

	public List findByExample(MotivosCambiosServicios instance) {
		log.debug("finding MotivosCambiosServicios instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.MotivosCambiosServicios").add(
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
