package com.servinte.axioma.orm;

// Generated Jan 13, 2011 5:41:25 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ProgrContratoEstadoMigrado.
 * @see com.servinte.axioma.orm.ProgrContratoEstadoMigrado
 * @author Hibernate Tools
 */
public class ProgrContratoEstadoMigradoHome {

	private static final Log log = LogFactory
			.getLog(ProgrContratoEstadoMigradoHome.class);

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

	public void persist(ProgrContratoEstadoMigrado transientInstance) {
		log.debug("persisting ProgrContratoEstadoMigrado instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ProgrContratoEstadoMigrado instance) {
		log.debug("attaching dirty ProgrContratoEstadoMigrado instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProgrContratoEstadoMigrado instance) {
		log.debug("attaching clean ProgrContratoEstadoMigrado instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ProgrContratoEstadoMigrado persistentInstance) {
		log.debug("deleting ProgrContratoEstadoMigrado instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProgrContratoEstadoMigrado merge(
			ProgrContratoEstadoMigrado detachedInstance) {
		log.debug("merging ProgrContratoEstadoMigrado instance");
		try {
			ProgrContratoEstadoMigrado result = (ProgrContratoEstadoMigrado) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ProgrContratoEstadoMigrado findById(long id) {
		log.debug("getting ProgrContratoEstadoMigrado instance with id: " + id);
		try {
			ProgrContratoEstadoMigrado instance = (ProgrContratoEstadoMigrado) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ProgrContratoEstadoMigrado",
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

	public List findByExample(ProgrContratoEstadoMigrado instance) {
		log.debug("finding ProgrContratoEstadoMigrado instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ProgrContratoEstadoMigrado").add(
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
