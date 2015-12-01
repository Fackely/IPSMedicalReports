package com.servinte.axioma.orm;

// Generated 26/08/2011 02:58:13 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TrasladoCajaPrincipalMayor.
 * @see com.servinte.axioma.orm.TrasladoCajaPrincipalMayor
 * @author Hibernate Tools
 */
public class TrasladoCajaPrincipalMayorHome {

	private static final Log log = LogFactory
			.getLog(TrasladoCajaPrincipalMayorHome.class);

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

	public void persist(TrasladoCajaPrincipalMayor transientInstance) {
		log.debug("persisting TrasladoCajaPrincipalMayor instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TrasladoCajaPrincipalMayor instance) {
		log.debug("attaching dirty TrasladoCajaPrincipalMayor instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TrasladoCajaPrincipalMayor instance) {
		log.debug("attaching clean TrasladoCajaPrincipalMayor instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TrasladoCajaPrincipalMayor persistentInstance) {
		log.debug("deleting TrasladoCajaPrincipalMayor instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TrasladoCajaPrincipalMayor merge(
			TrasladoCajaPrincipalMayor detachedInstance) {
		log.debug("merging TrasladoCajaPrincipalMayor instance");
		try {
			TrasladoCajaPrincipalMayor result = (TrasladoCajaPrincipalMayor) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TrasladoCajaPrincipalMayor findById(long id) {
		log.debug("getting TrasladoCajaPrincipalMayor instance with id: " + id);
		try {
			TrasladoCajaPrincipalMayor instance = (TrasladoCajaPrincipalMayor) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.TrasladoCajaPrincipalMayor",
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

	public List findByExample(TrasladoCajaPrincipalMayor instance) {
		log.debug("finding TrasladoCajaPrincipalMayor instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.TrasladoCajaPrincipalMayor")
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
