package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:56 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DiagAutorizaciones.
 * @see com.servinte.axioma.orm.DiagAutorizaciones
 * @author Hibernate Tools
 */
public class DiagAutorizacionesHome {

	private static final Log log = LogFactory
			.getLog(DiagAutorizacionesHome.class);

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

	public void persist(DiagAutorizaciones transientInstance) {
		log.debug("persisting DiagAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DiagAutorizaciones instance) {
		log.debug("attaching dirty DiagAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DiagAutorizaciones instance) {
		log.debug("attaching clean DiagAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DiagAutorizaciones persistentInstance) {
		log.debug("deleting DiagAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DiagAutorizaciones merge(DiagAutorizaciones detachedInstance) {
		log.debug("merging DiagAutorizaciones instance");
		try {
			DiagAutorizaciones result = (DiagAutorizaciones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DiagAutorizaciones findById(long id) {
		log.debug("getting DiagAutorizaciones instance with id: " + id);
		try {
			DiagAutorizaciones instance = (DiagAutorizaciones) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DiagAutorizaciones", id);
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

	public List findByExample(DiagAutorizaciones instance) {
		log.debug("finding DiagAutorizaciones instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DiagAutorizaciones").add(
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
