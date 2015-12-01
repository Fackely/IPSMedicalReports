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
 * Home object for domain model class ExcepIncluexcluContr.
 * @see com.servinte.axioma.orm.ExcepIncluexcluContr
 * @author Hibernate Tools
 */
public class ExcepIncluexcluContrHome {

	private static final Log log = LogFactory
			.getLog(ExcepIncluexcluContrHome.class);

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

	public void persist(ExcepIncluexcluContr transientInstance) {
		log.debug("persisting ExcepIncluexcluContr instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ExcepIncluexcluContr instance) {
		log.debug("attaching dirty ExcepIncluexcluContr instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ExcepIncluexcluContr instance) {
		log.debug("attaching clean ExcepIncluexcluContr instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ExcepIncluexcluContr persistentInstance) {
		log.debug("deleting ExcepIncluexcluContr instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ExcepIncluexcluContr merge(ExcepIncluexcluContr detachedInstance) {
		log.debug("merging ExcepIncluexcluContr instance");
		try {
			ExcepIncluexcluContr result = (ExcepIncluexcluContr) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ExcepIncluexcluContr findById(long id) {
		log.debug("getting ExcepIncluexcluContr instance with id: " + id);
		try {
			ExcepIncluexcluContr instance = (ExcepIncluexcluContr) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ExcepIncluexcluContr", id);
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

	public List findByExample(ExcepIncluexcluContr instance) {
		log.debug("finding ExcepIncluexcluContr instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ExcepIncluexcluContr").add(
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
