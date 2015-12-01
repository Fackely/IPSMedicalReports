package com.servinte.axioma.orm;

// Generated Jan 5, 2011 2:40:50 PM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UsuariosCapitados.
 * @see com.servinte.axioma.orm.UsuariosCapitados
 * @author Hibernate Tools
 */
public class UsuariosCapitadosHome {

	private static final Log log = LogFactory
			.getLog(UsuariosCapitadosHome.class);

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

	public void persist(UsuariosCapitados transientInstance) {
		log.debug("persisting UsuariosCapitados instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UsuariosCapitados instance) {
		log.debug("attaching dirty UsuariosCapitados instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UsuariosCapitados instance) {
		log.debug("attaching clean UsuariosCapitados instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UsuariosCapitados persistentInstance) {
		log.debug("deleting UsuariosCapitados instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UsuariosCapitados merge(UsuariosCapitados detachedInstance) {
		log.debug("merging UsuariosCapitados instance");
		try {
			UsuariosCapitados result = (UsuariosCapitados) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UsuariosCapitados findById(long id) {
		log.debug("getting UsuariosCapitados instance with id: " + id);
		try {
			UsuariosCapitados instance = (UsuariosCapitados) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UsuariosCapitados", id);
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

	public List findByExample(UsuariosCapitados instance) {
		log.debug("finding UsuariosCapitados instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UsuariosCapitados").add(
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
