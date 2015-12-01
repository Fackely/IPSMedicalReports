package com.servinte.axioma.orm;

// Generated Jan 12, 2011 9:11:30 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ExcluPresuEncabezado.
 * @see com.servinte.axioma.orm.ExcluPresuEncabezado
 * @author Hibernate Tools
 */
public class ExcluPresuEncabezadoHome {

	private static final Log log = LogFactory
			.getLog(ExcluPresuEncabezadoHome.class);

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

	public void persist(ExcluPresuEncabezado transientInstance) {
		log.debug("persisting ExcluPresuEncabezado instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ExcluPresuEncabezado instance) {
		log.debug("attaching dirty ExcluPresuEncabezado instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ExcluPresuEncabezado instance) {
		log.debug("attaching clean ExcluPresuEncabezado instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ExcluPresuEncabezado persistentInstance) {
		log.debug("deleting ExcluPresuEncabezado instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ExcluPresuEncabezado merge(ExcluPresuEncabezado detachedInstance) {
		log.debug("merging ExcluPresuEncabezado instance");
		try {
			ExcluPresuEncabezado result = (ExcluPresuEncabezado) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ExcluPresuEncabezado findById(long id) {
		log.debug("getting ExcluPresuEncabezado instance with id: " + id);
		try {
			ExcluPresuEncabezado instance = (ExcluPresuEncabezado) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ExcluPresuEncabezado", id);
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

	public List findByExample(ExcluPresuEncabezado instance) {
		log.debug("finding ExcluPresuEncabezado instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ExcluPresuEncabezado").add(
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
