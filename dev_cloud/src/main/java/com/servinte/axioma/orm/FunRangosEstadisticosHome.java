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
 * Home object for domain model class FunRangosEstadisticos.
 * @see com.servinte.axioma.orm.FunRangosEstadisticos
 * @author Hibernate Tools
 */
public class FunRangosEstadisticosHome {

	private static final Log log = LogFactory
			.getLog(FunRangosEstadisticosHome.class);

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

	public void persist(FunRangosEstadisticos transientInstance) {
		log.debug("persisting FunRangosEstadisticos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FunRangosEstadisticos instance) {
		log.debug("attaching dirty FunRangosEstadisticos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FunRangosEstadisticos instance) {
		log.debug("attaching clean FunRangosEstadisticos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FunRangosEstadisticos persistentInstance) {
		log.debug("deleting FunRangosEstadisticos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FunRangosEstadisticos merge(FunRangosEstadisticos detachedInstance) {
		log.debug("merging FunRangosEstadisticos instance");
		try {
			FunRangosEstadisticos result = (FunRangosEstadisticos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FunRangosEstadisticos findById(int id) {
		log.debug("getting FunRangosEstadisticos instance with id: " + id);
		try {
			FunRangosEstadisticos instance = (FunRangosEstadisticos) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.FunRangosEstadisticos", id);
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

	public List findByExample(FunRangosEstadisticos instance) {
		log.debug("finding FunRangosEstadisticos instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.FunRangosEstadisticos").add(
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
