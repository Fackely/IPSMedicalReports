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
 * Home object for domain model class ResultadoLaboratorioRegenf.
 * @see com.servinte.axioma.orm.ResultadoLaboratorioRegenf
 * @author Hibernate Tools
 */
public class ResultadoLaboratorioRegenfHome {

	private static final Log log = LogFactory
			.getLog(ResultadoLaboratorioRegenfHome.class);

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

	public void persist(ResultadoLaboratorioRegenf transientInstance) {
		log.debug("persisting ResultadoLaboratorioRegenf instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ResultadoLaboratorioRegenf instance) {
		log.debug("attaching dirty ResultadoLaboratorioRegenf instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ResultadoLaboratorioRegenf instance) {
		log.debug("attaching clean ResultadoLaboratorioRegenf instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ResultadoLaboratorioRegenf persistentInstance) {
		log.debug("deleting ResultadoLaboratorioRegenf instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ResultadoLaboratorioRegenf merge(
			ResultadoLaboratorioRegenf detachedInstance) {
		log.debug("merging ResultadoLaboratorioRegenf instance");
		try {
			ResultadoLaboratorioRegenf result = (ResultadoLaboratorioRegenf) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ResultadoLaboratorioRegenf findById(int id) {
		log.debug("getting ResultadoLaboratorioRegenf instance with id: " + id);
		try {
			ResultadoLaboratorioRegenf instance = (ResultadoLaboratorioRegenf) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ResultadoLaboratorioRegenf",
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

	public List findByExample(ResultadoLaboratorioRegenf instance) {
		log.debug("finding ResultadoLaboratorioRegenf instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ResultadoLaboratorioRegenf").add(
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
