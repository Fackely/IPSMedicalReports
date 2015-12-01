package com.servinte.axioma.orm;

// Generated Feb 21, 2011 3:36:24 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class FinalidadesConsulta.
 * @see com.servinte.axioma.orm.FinalidadesConsulta
 * @author Hibernate Tools
 */
public class FinalidadesConsultaHome {

	private static final Log log = LogFactory
			.getLog(FinalidadesConsultaHome.class);

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

	public void persist(FinalidadesConsulta transientInstance) {
		log.debug("persisting FinalidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FinalidadesConsulta instance) {
		log.debug("attaching dirty FinalidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FinalidadesConsulta instance) {
		log.debug("attaching clean FinalidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FinalidadesConsulta persistentInstance) {
		log.debug("deleting FinalidadesConsulta instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FinalidadesConsulta merge(FinalidadesConsulta detachedInstance) {
		log.debug("merging FinalidadesConsulta instance");
		try {
			FinalidadesConsulta result = (FinalidadesConsulta) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FinalidadesConsulta findById(java.lang.String id) {
		log.debug("getting FinalidadesConsulta instance with id: " + id);
		try {
			FinalidadesConsulta instance = (FinalidadesConsulta) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.FinalidadesConsulta", id);
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

	public List findByExample(FinalidadesConsulta instance) {
		log.debug("finding FinalidadesConsulta instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.FinalidadesConsulta").add(
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
