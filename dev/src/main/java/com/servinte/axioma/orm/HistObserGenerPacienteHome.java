package com.servinte.axioma.orm;

// Generated Sep 22, 2010 11:56:23 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistObserGenerPaciente.
 * @see com.servinte.axioma.orm.HistObserGenerPaciente
 * @author Hibernate Tools
 */
public class HistObserGenerPacienteHome {

	private static final Log log = LogFactory
			.getLog(HistObserGenerPacienteHome.class);

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

	public void persist(HistObserGenerPaciente transientInstance) {
		log.debug("persisting HistObserGenerPaciente instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistObserGenerPaciente instance) {
		log.debug("attaching dirty HistObserGenerPaciente instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistObserGenerPaciente instance) {
		log.debug("attaching clean HistObserGenerPaciente instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistObserGenerPaciente persistentInstance) {
		log.debug("deleting HistObserGenerPaciente instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistObserGenerPaciente merge(HistObserGenerPaciente detachedInstance) {
		log.debug("merging HistObserGenerPaciente instance");
		try {
			HistObserGenerPaciente result = (HistObserGenerPaciente) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistObserGenerPaciente findById(long id) {
		log.debug("getting HistObserGenerPaciente instance with id: " + id);
		try {
			HistObserGenerPaciente instance = (HistObserGenerPaciente) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistObserGenerPaciente",
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

	public List findByExample(HistObserGenerPaciente instance) {
		log.debug("finding HistObserGenerPaciente instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistObserGenerPaciente").add(
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
