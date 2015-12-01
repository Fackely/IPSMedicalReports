package com.servinte.axioma.orm;

// Generated Aug 25, 2010 5:37:53 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UnidadesConsulta.
 * @see com.servinte.axioma.orm.UnidadesConsulta
 * @author Hibernate Tools
 */
public class UnidadesConsultaHome {

	private static final Log log = LogFactory
			.getLog(UnidadesConsultaHome.class);

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

	public void persist(UnidadesConsulta transientInstance) {
		log.debug("persisting UnidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UnidadesConsulta instance) {
		log.debug("attaching dirty UnidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UnidadesConsulta instance) {
		log.debug("attaching clean UnidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UnidadesConsulta persistentInstance) {
		log.debug("deleting UnidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UnidadesConsulta merge(UnidadesConsulta detachedInstance) {
		log.debug("merging UnidadesConsulta instance");
		try {
			UnidadesConsulta result = (UnidadesConsulta) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UnidadesConsulta findById(int id) {
		log.debug("getting UnidadesConsulta instance with id: " + id);
		try {
			UnidadesConsulta instance = (UnidadesConsulta) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UnidadesConsulta", id);
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

	public List findByExample(UnidadesConsulta instance) {
		log.debug("finding UnidadesConsulta instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UnidadesConsulta").add(
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
