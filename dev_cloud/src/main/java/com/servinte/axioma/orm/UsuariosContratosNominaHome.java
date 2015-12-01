package com.servinte.axioma.orm;

// Generated Jan 4, 2011 9:13:43 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UsuariosContratosNomina.
 * @see com.servinte.axioma.orm.UsuariosContratosNomina
 * @author Hibernate Tools
 */
public class UsuariosContratosNominaHome {

	private static final Log log = LogFactory
			.getLog(UsuariosContratosNominaHome.class);

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

	public void persist(UsuariosContratosNomina transientInstance) {
		log.debug("persisting UsuariosContratosNomina instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UsuariosContratosNomina instance) {
		log.debug("attaching dirty UsuariosContratosNomina instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UsuariosContratosNomina instance) {
		log.debug("attaching clean UsuariosContratosNomina instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UsuariosContratosNomina persistentInstance) {
		log.debug("deleting UsuariosContratosNomina instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UsuariosContratosNomina merge(
			UsuariosContratosNomina detachedInstance) {
		log.debug("merging UsuariosContratosNomina instance");
		try {
			UsuariosContratosNomina result = (UsuariosContratosNomina) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UsuariosContratosNomina findById(
			com.servinte.axioma.orm.UsuariosContratosNominaId id) {
		log.debug("getting UsuariosContratosNomina instance with id: " + id);
		try {
			UsuariosContratosNomina instance = (UsuariosContratosNomina) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UsuariosContratosNomina",
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

	public List findByExample(UsuariosContratosNomina instance) {
		log.debug("finding UsuariosContratosNomina instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UsuariosContratosNomina").add(
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
