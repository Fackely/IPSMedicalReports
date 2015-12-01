package com.servinte.axioma.orm;

// Generated Sep 23, 2010 2:39:35 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NaturalezaArticulo.
 * @see com.servinte.axioma.orm.NaturalezaArticulo
 * @author Hibernate Tools
 */
public class NaturalezaArticuloHome {

	private static final Log log = LogFactory
			.getLog(NaturalezaArticuloHome.class);

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

	public void persist(NaturalezaArticulo transientInstance) {
		log.debug("persisting NaturalezaArticulo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NaturalezaArticulo instance) {
		log.debug("attaching dirty NaturalezaArticulo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NaturalezaArticulo instance) {
		log.debug("attaching clean NaturalezaArticulo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NaturalezaArticulo persistentInstance) {
		log.debug("deleting NaturalezaArticulo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NaturalezaArticulo merge(NaturalezaArticulo detachedInstance) {
		log.debug("merging NaturalezaArticulo instance");
		try {
			NaturalezaArticulo result = (NaturalezaArticulo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NaturalezaArticulo findById(
			com.servinte.axioma.orm.NaturalezaArticuloId id) {
		log.debug("getting NaturalezaArticulo instance with id: " + id);
		try {
			NaturalezaArticulo instance = (NaturalezaArticulo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NaturalezaArticulo", id);
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

	public List findByExample(NaturalezaArticulo instance) {
		log.debug("finding NaturalezaArticulo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NaturalezaArticulo").add(
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
