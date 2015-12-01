package com.servinte.axioma.orm;

// Generated Nov 30, 2010 11:44:24 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ResultadoLaboratorioOrden.
 * @see com.servinte.axioma.orm.ResultadoLaboratorioOrden
 * @author Hibernate Tools
 */
public class ResultadoLaboratorioOrdenHome {

	private static final Log log = LogFactory
			.getLog(ResultadoLaboratorioOrdenHome.class);

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

	public void persist(ResultadoLaboratorioOrden transientInstance) {
		log.debug("persisting ResultadoLaboratorioOrden instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ResultadoLaboratorioOrden instance) {
		log.debug("attaching dirty ResultadoLaboratorioOrden instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ResultadoLaboratorioOrden instance) {
		log.debug("attaching clean ResultadoLaboratorioOrden instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ResultadoLaboratorioOrden persistentInstance) {
		log.debug("deleting ResultadoLaboratorioOrden instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ResultadoLaboratorioOrden merge(
			ResultadoLaboratorioOrden detachedInstance) {
		log.debug("merging ResultadoLaboratorioOrden instance");
		try {
			ResultadoLaboratorioOrden result = (ResultadoLaboratorioOrden) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ResultadoLaboratorioOrden findById(int id) {
		log.debug("getting ResultadoLaboratorioOrden instance with id: " + id);
		try {
			ResultadoLaboratorioOrden instance = (ResultadoLaboratorioOrden) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ResultadoLaboratorioOrden",
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

	public List findByExample(ResultadoLaboratorioOrden instance) {
		log.debug("finding ResultadoLaboratorioOrden instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ResultadoLaboratorioOrden").add(
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
