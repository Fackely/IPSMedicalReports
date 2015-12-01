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
 * Home object for domain model class ConceptosIngTesoreria.
 * @see com.servinte.axioma.orm.ConceptosIngTesoreria
 * @author Hibernate Tools
 */
public class ConceptosIngTesoreriaHome {

	private static final Log log = LogFactory
			.getLog(ConceptosIngTesoreriaHome.class);

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

	public void persist(ConceptosIngTesoreria transientInstance) {
		log.debug("persisting ConceptosIngTesoreria instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ConceptosIngTesoreria instance) {
		log.debug("attaching dirty ConceptosIngTesoreria instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ConceptosIngTesoreria instance) {
		log.debug("attaching clean ConceptosIngTesoreria instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ConceptosIngTesoreria persistentInstance) {
		log.debug("deleting ConceptosIngTesoreria instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ConceptosIngTesoreria merge(ConceptosIngTesoreria detachedInstance) {
		log.debug("merging ConceptosIngTesoreria instance");
		try {
			ConceptosIngTesoreria result = (ConceptosIngTesoreria) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ConceptosIngTesoreria findById(
			com.servinte.axioma.orm.ConceptosIngTesoreriaId id) {
		log.debug("getting ConceptosIngTesoreria instance with id: " + id);
		try {
			ConceptosIngTesoreria instance = (ConceptosIngTesoreria) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ConceptosIngTesoreria", id);
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

	public List findByExample(ConceptosIngTesoreria instance) {
		log.debug("finding ConceptosIngTesoreria instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ConceptosIngTesoreria").add(
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
