package com.servinte.axioma.orm;

// Generated Aug 27, 2010 6:29:22 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UnidadAgendaServCitaOdonto.
 * @see com.servinte.axioma.orm.UnidadAgendaServCitaOdonto
 * @author Hibernate Tools
 */
public class UnidadAgendaServCitaOdontoHome {

	private static final Log log = LogFactory
			.getLog(UnidadAgendaServCitaOdontoHome.class);

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

	public void persist(UnidadAgendaServCitaOdonto transientInstance) {
		log.debug("persisting UnidadAgendaServCitaOdonto instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UnidadAgendaServCitaOdonto instance) {
		log.debug("attaching dirty UnidadAgendaServCitaOdonto instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UnidadAgendaServCitaOdonto instance) {
		log.debug("attaching clean UnidadAgendaServCitaOdonto instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UnidadAgendaServCitaOdonto persistentInstance) {
		log.debug("deleting UnidadAgendaServCitaOdonto instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UnidadAgendaServCitaOdonto merge(
			UnidadAgendaServCitaOdonto detachedInstance) {
		log.debug("merging UnidadAgendaServCitaOdonto instance");
		try {
			UnidadAgendaServCitaOdonto result = (UnidadAgendaServCitaOdonto) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UnidadAgendaServCitaOdonto findById(long id) {
		log.debug("getting UnidadAgendaServCitaOdonto instance with id: " + id);
		try {
			UnidadAgendaServCitaOdonto instance = (UnidadAgendaServCitaOdonto) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.UnidadAgendaServCitaOdonto",
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

	public List findByExample(UnidadAgendaServCitaOdonto instance) {
		log.debug("finding UnidadAgendaServCitaOdonto instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UnidadAgendaServCitaOdonto").add(
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
