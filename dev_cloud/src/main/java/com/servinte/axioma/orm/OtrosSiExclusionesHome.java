package com.servinte.axioma.orm;

// Generated Dec 2, 2010 7:02:45 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class OtrosSiExclusiones.
 * @see com.servinte.axioma.orm.OtrosSiExclusiones
 * @author Hibernate Tools
 */
public class OtrosSiExclusionesHome {

	private static final Log log = LogFactory
			.getLog(OtrosSiExclusionesHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(OtrosSiExclusiones transientInstance) {
		log.debug("persisting OtrosSiExclusiones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(OtrosSiExclusiones instance) {
		log.debug("attaching dirty OtrosSiExclusiones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OtrosSiExclusiones instance) {
		log.debug("attaching clean OtrosSiExclusiones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(OtrosSiExclusiones persistentInstance) {
		log.debug("deleting OtrosSiExclusiones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OtrosSiExclusiones merge(OtrosSiExclusiones detachedInstance) {
		log.debug("merging OtrosSiExclusiones instance");
		try {
			OtrosSiExclusiones result = (OtrosSiExclusiones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OtrosSiExclusiones findById(long id) {
		log.debug("getting OtrosSiExclusiones instance with id: " + id);
		try {
			OtrosSiExclusiones instance = (OtrosSiExclusiones) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.OtrosSiExclusiones", id);
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

	public List findByExample(OtrosSiExclusiones instance) {
		log.debug("finding OtrosSiExclusiones instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.OtrosSiExclusiones").add(
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
