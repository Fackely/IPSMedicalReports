package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetSecTotFormatoFact.
 * @see com.servinte.axioma.orm.DetSecTotFormatoFact
 * @author Hibernate Tools
 */
public class DetSecTotFormatoFactHome {

	private static final Log log = LogFactory
			.getLog(DetSecTotFormatoFactHome.class);

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

	public void persist(DetSecTotFormatoFact transientInstance) {
		log.debug("persisting DetSecTotFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetSecTotFormatoFact instance) {
		log.debug("attaching dirty DetSecTotFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetSecTotFormatoFact instance) {
		log.debug("attaching clean DetSecTotFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetSecTotFormatoFact persistentInstance) {
		log.debug("deleting DetSecTotFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetSecTotFormatoFact merge(DetSecTotFormatoFact detachedInstance) {
		log.debug("merging DetSecTotFormatoFact instance");
		try {
			DetSecTotFormatoFact result = (DetSecTotFormatoFact) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetSecTotFormatoFact findById(
			com.servinte.axioma.orm.DetSecTotFormatoFactId id) {
		log.debug("getting DetSecTotFormatoFact instance with id: " + id);
		try {
			DetSecTotFormatoFact instance = (DetSecTotFormatoFact) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetSecTotFormatoFact", id);
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

	public List findByExample(DetSecTotFormatoFact instance) {
		log.debug("finding DetSecTotFormatoFact instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetSecTotFormatoFact").add(
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
