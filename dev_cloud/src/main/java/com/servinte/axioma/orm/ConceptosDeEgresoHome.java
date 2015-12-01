package com.servinte.axioma.orm;

// Generated Apr 9, 2010 9:43:42 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ConceptosDeEgreso.
 * @see com.servinte.axioma.orm.ConceptosDeEgreso
 * @author Hibernate Tools
 */
public class ConceptosDeEgresoHome {

	private static final Log log = LogFactory
			.getLog(ConceptosDeEgresoHome.class);

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

	public void persist(ConceptosDeEgreso transientInstance) {
		log.debug("persisting ConceptosDeEgreso instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ConceptosDeEgreso instance) {
		log.debug("attaching dirty ConceptosDeEgreso instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ConceptosDeEgreso instance) {
		log.debug("attaching clean ConceptosDeEgreso instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ConceptosDeEgreso persistentInstance) {
		log.debug("deleting ConceptosDeEgreso instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ConceptosDeEgreso merge(ConceptosDeEgreso detachedInstance) {
		log.debug("merging ConceptosDeEgreso instance");
		try {
			ConceptosDeEgreso result = (ConceptosDeEgreso) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ConceptosDeEgreso findById(long id) {
		log.debug("getting ConceptosDeEgreso instance with id: " + id);
		try {
			ConceptosDeEgreso instance = (ConceptosDeEgreso) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ConceptosDeEgreso", id);
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

	public List findByExample(ConceptosDeEgreso instance) {
		log.debug("finding ConceptosDeEgreso instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ConceptosDeEgreso").add(
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
