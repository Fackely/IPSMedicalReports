package com.servinte.axioma.orm;

// Generated 26/03/2012 03:36:48 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AdjuntoNotaAclaratoria.
 * @see com.servinte.axioma.orm.AdjuntoNotaAclaratoria
 * @author Hibernate Tools
 */
public class AdjuntoNotaAclaratoriaHome {

	private static final Log log = LogFactory
			.getLog(AdjuntoNotaAclaratoriaHome.class);

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

	public void persist(AdjuntoNotaAclaratoria transientInstance) {
		log.debug("persisting AdjuntoNotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AdjuntoNotaAclaratoria instance) {
		log.debug("attaching dirty AdjuntoNotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AdjuntoNotaAclaratoria instance) {
		log.debug("attaching clean AdjuntoNotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AdjuntoNotaAclaratoria persistentInstance) {
		log.debug("deleting AdjuntoNotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AdjuntoNotaAclaratoria merge(AdjuntoNotaAclaratoria detachedInstance) {
		log.debug("merging AdjuntoNotaAclaratoria instance");
		try {
			AdjuntoNotaAclaratoria result = (AdjuntoNotaAclaratoria) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AdjuntoNotaAclaratoria findById(long id) {
		log.debug("getting AdjuntoNotaAclaratoria instance with id: " + id);
		try {
			AdjuntoNotaAclaratoria instance = (AdjuntoNotaAclaratoria) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AdjuntoNotaAclaratoria",
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

	public List findByExample(AdjuntoNotaAclaratoria instance) {
		log.debug("finding AdjuntoNotaAclaratoria instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AdjuntoNotaAclaratoria")
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
