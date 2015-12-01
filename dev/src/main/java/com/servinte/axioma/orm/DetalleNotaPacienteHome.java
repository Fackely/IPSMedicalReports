package com.servinte.axioma.orm;

// Generated 13/07/2011 02:33:03 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetalleNotaPaciente.
 * @see com.servinte.axioma.orm.DetalleNotaPaciente
 * @author Hibernate Tools
 */
public class DetalleNotaPacienteHome {

	private static final Log log = LogFactory
			.getLog(DetalleNotaPacienteHome.class);

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

	public void persist(DetalleNotaPaciente transientInstance) {
		log.debug("persisting DetalleNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleNotaPaciente instance) {
		log.debug("attaching dirty DetalleNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleNotaPaciente instance) {
		log.debug("attaching clean DetalleNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleNotaPaciente persistentInstance) {
		log.debug("deleting DetalleNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleNotaPaciente merge(DetalleNotaPaciente detachedInstance) {
		log.debug("merging DetalleNotaPaciente instance");
		try {
			DetalleNotaPaciente result = (DetalleNotaPaciente) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleNotaPaciente findById(long id) {
		log.debug("getting DetalleNotaPaciente instance with id: " + id);
		try {
			DetalleNotaPaciente instance = (DetalleNotaPaciente) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetalleNotaPaciente", id);
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

	public List findByExample(DetalleNotaPaciente instance) {
		log.debug("finding DetalleNotaPaciente instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.DetalleNotaPaciente")
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
