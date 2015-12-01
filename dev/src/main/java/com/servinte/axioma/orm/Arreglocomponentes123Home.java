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
 * Home object for domain model class Arreglocomponentes123.
 * @see com.servinte.axioma.orm.Arreglocomponentes123
 * @author Hibernate Tools
 */
public class Arreglocomponentes123Home {

	private static final Log log = LogFactory
			.getLog(Arreglocomponentes123Home.class);

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

	public void persist(Arreglocomponentes123 transientInstance) {
		log.debug("persisting Arreglocomponentes123 instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Arreglocomponentes123 instance) {
		log.debug("attaching dirty Arreglocomponentes123 instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Arreglocomponentes123 instance) {
		log.debug("attaching clean Arreglocomponentes123 instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Arreglocomponentes123 persistentInstance) {
		log.debug("deleting Arreglocomponentes123 instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Arreglocomponentes123 merge(Arreglocomponentes123 detachedInstance) {
		log.debug("merging Arreglocomponentes123 instance");
		try {
			Arreglocomponentes123 result = (Arreglocomponentes123) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Arreglocomponentes123 findById(
			com.servinte.axioma.orm.Arreglocomponentes123Id id) {
		log.debug("getting Arreglocomponentes123 instance with id: " + id);
		try {
			Arreglocomponentes123 instance = (Arreglocomponentes123) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.Arreglocomponentes123", id);
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

	public List findByExample(Arreglocomponentes123 instance) {
		log.debug("finding Arreglocomponentes123 instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.Arreglocomponentes123").add(
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
