package com.servinte.axioma.orm;

// Generated Dec 29, 2010 6:07:09 PM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UsuarioXConvenio.
 * @see com.servinte.axioma.orm.UsuarioXConvenio
 * @author Hibernate Tools
 */
public class UsuarioXConvenioHome {

	private static final Log log = LogFactory
			.getLog(UsuarioXConvenioHome.class);

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

	public void persist(UsuarioXConvenio transientInstance) {
		log.debug("persisting UsuarioXConvenio instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UsuarioXConvenio instance) {
		log.debug("attaching dirty UsuarioXConvenio instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UsuarioXConvenio instance) {
		log.debug("attaching clean UsuarioXConvenio instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UsuarioXConvenio persistentInstance) {
		log.debug("deleting UsuarioXConvenio instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UsuarioXConvenio merge(UsuarioXConvenio detachedInstance) {
		log.debug("merging UsuarioXConvenio instance");
		try {
			UsuarioXConvenio result = (UsuarioXConvenio) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UsuarioXConvenio findById(long id) {
		log.debug("getting UsuarioXConvenio instance with id: " + id);
		try {
			UsuarioXConvenio instance = (UsuarioXConvenio) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UsuarioXConvenio", id);
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

	public List findByExample(UsuarioXConvenio instance) {
		log.debug("finding UsuarioXConvenio instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UsuarioXConvenio").add(
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
