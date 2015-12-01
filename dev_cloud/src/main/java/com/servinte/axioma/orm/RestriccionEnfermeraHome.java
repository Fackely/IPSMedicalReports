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
 * Home object for domain model class RestriccionEnfermera.
 * @see com.servinte.axioma.orm.RestriccionEnfermera
 * @author Hibernate Tools
 */
public class RestriccionEnfermeraHome {

	private static final Log log = LogFactory
			.getLog(RestriccionEnfermeraHome.class);

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

	public void persist(RestriccionEnfermera transientInstance) {
		log.debug("persisting RestriccionEnfermera instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RestriccionEnfermera instance) {
		log.debug("attaching dirty RestriccionEnfermera instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RestriccionEnfermera instance) {
		log.debug("attaching clean RestriccionEnfermera instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RestriccionEnfermera persistentInstance) {
		log.debug("deleting RestriccionEnfermera instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RestriccionEnfermera merge(RestriccionEnfermera detachedInstance) {
		log.debug("merging RestriccionEnfermera instance");
		try {
			RestriccionEnfermera result = (RestriccionEnfermera) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RestriccionEnfermera findById(int id) {
		log.debug("getting RestriccionEnfermera instance with id: " + id);
		try {
			RestriccionEnfermera instance = (RestriccionEnfermera) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.RestriccionEnfermera", id);
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

	public List findByExample(RestriccionEnfermera instance) {
		log.debug("finding RestriccionEnfermera instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RestriccionEnfermera").add(
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
