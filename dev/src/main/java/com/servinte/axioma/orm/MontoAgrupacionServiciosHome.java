package com.servinte.axioma.orm;

// Generated Aug 25, 2010 4:29:57 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class MontoAgrupacionServicios.
 * @see com.servinte.axioma.orm.MontoAgrupacionServicios
 * @author Hibernate Tools
 */
public class MontoAgrupacionServiciosHome {

	private static final Log log = LogFactory
			.getLog(MontoAgrupacionServiciosHome.class);

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

	public void persist(MontoAgrupacionServicios transientInstance) {
		log.debug("persisting MontoAgrupacionServicios instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MontoAgrupacionServicios instance) {
		log.debug("attaching dirty MontoAgrupacionServicios instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MontoAgrupacionServicios instance) {
		log.debug("attaching clean MontoAgrupacionServicios instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MontoAgrupacionServicios persistentInstance) {
		log.debug("deleting MontoAgrupacionServicios instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MontoAgrupacionServicios merge(
			MontoAgrupacionServicios detachedInstance) {
		log.debug("merging MontoAgrupacionServicios instance");
		try {
			MontoAgrupacionServicios result = (MontoAgrupacionServicios) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MontoAgrupacionServicios findById(int id) {
		log.debug("getting MontoAgrupacionServicios instance with id: " + id);
		try {
			MontoAgrupacionServicios instance = (MontoAgrupacionServicios) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MontoAgrupacionServicios",
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

	public List findByExample(MontoAgrupacionServicios instance) {
		log.debug("finding MontoAgrupacionServicios instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.MontoAgrupacionServicios").add(
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
