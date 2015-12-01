package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:55 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PresuContratoOdoImp.
 * @see com.servinte.axioma.orm.PresuContratoOdoImp
 * @author Hibernate Tools
 */
public class PresuContratoOdoImpHome {

	private static final Log log = LogFactory
			.getLog(PresuContratoOdoImpHome.class);

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

	public void persist(PresuContratoOdoImp transientInstance) {
		log.debug("persisting PresuContratoOdoImp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresuContratoOdoImp instance) {
		log.debug("attaching dirty PresuContratoOdoImp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresuContratoOdoImp instance) {
		log.debug("attaching clean PresuContratoOdoImp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresuContratoOdoImp persistentInstance) {
		log.debug("deleting PresuContratoOdoImp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresuContratoOdoImp merge(PresuContratoOdoImp detachedInstance) {
		log.debug("merging PresuContratoOdoImp instance");
		try {
			PresuContratoOdoImp result = (PresuContratoOdoImp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresuContratoOdoImp findById(long id) {
		log.debug("getting PresuContratoOdoImp instance with id: " + id);
		try {
			PresuContratoOdoImp instance = (PresuContratoOdoImp) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresuContratoOdoImp", id);
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

	public List findByExample(PresuContratoOdoImp instance) {
		log.debug("finding PresuContratoOdoImp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresuContratoOdoImp").add(
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
