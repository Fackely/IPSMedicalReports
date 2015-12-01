package com.servinte.axioma.orm;

// Generated Jun 30, 2010 2:13:59 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ContratoOdontologico.
 * @see com.servinte.axioma.orm.ContratoOdontologico
 * @author Hibernate Tools
 */
public class ContratoOdontologicoHome {

	private static final Log log = LogFactory
			.getLog(ContratoOdontologicoHome.class);

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

	public void persist(ContratoOdontologico transientInstance) {
		log.debug("persisting ContratoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ContratoOdontologico instance) {
		log.debug("attaching dirty ContratoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ContratoOdontologico instance) {
		log.debug("attaching clean ContratoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ContratoOdontologico persistentInstance) {
		log.debug("deleting ContratoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ContratoOdontologico merge(ContratoOdontologico detachedInstance) {
		log.debug("merging ContratoOdontologico instance");
		try {
			ContratoOdontologico result = (ContratoOdontologico) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ContratoOdontologico findById(long id) {
		log.debug("getting ContratoOdontologico instance with id: " + id);
		try {
			ContratoOdontologico instance = (ContratoOdontologico) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ContratoOdontologico", id);
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

	public List findByExample(ContratoOdontologico instance) {
		log.debug("finding ContratoOdontologico instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ContratoOdontologico").add(
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
