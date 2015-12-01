package com.servinte.axioma.orm;

// Generated 21/07/2011 03:06:48 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EvolDiagnosticos.
 * @see com.servinte.axioma.orm.EvolDiagnosticos
 * @author Hibernate Tools
 */
public class EvolDiagnosticosHome {

	private static final Log log = LogFactory
			.getLog(EvolDiagnosticosHome.class);

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

	public void persist(EvolDiagnosticos transientInstance) {
		log.debug("persisting EvolDiagnosticos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EvolDiagnosticos instance) {
		log.debug("attaching dirty EvolDiagnosticos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EvolDiagnosticos instance) {
		log.debug("attaching clean EvolDiagnosticos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EvolDiagnosticos persistentInstance) {
		log.debug("deleting EvolDiagnosticos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EvolDiagnosticos merge(EvolDiagnosticos detachedInstance) {
		log.debug("merging EvolDiagnosticos instance");
		try {
			EvolDiagnosticos result = (EvolDiagnosticos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EvolDiagnosticos findById(
			com.servinte.axioma.orm.EvolDiagnosticosId id) {
		log.debug("getting EvolDiagnosticos instance with id: " + id);
		try {
			EvolDiagnosticos instance = (EvolDiagnosticos) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.EvolDiagnosticos", id);
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

	public List findByExample(EvolDiagnosticos instance) {
		log.debug("finding EvolDiagnosticos instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.EvolDiagnosticos")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
