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
 * Home object for domain model class AgrupArtDescComConvcont.
 * @see com.servinte.axioma.orm.AgrupArtDescComConvcont
 * @author Hibernate Tools
 */
public class AgrupArtDescComConvcontHome {

	private static final Log log = LogFactory
			.getLog(AgrupArtDescComConvcontHome.class);

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

	public void persist(AgrupArtDescComConvcont transientInstance) {
		log.debug("persisting AgrupArtDescComConvcont instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AgrupArtDescComConvcont instance) {
		log.debug("attaching dirty AgrupArtDescComConvcont instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AgrupArtDescComConvcont instance) {
		log.debug("attaching clean AgrupArtDescComConvcont instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AgrupArtDescComConvcont persistentInstance) {
		log.debug("deleting AgrupArtDescComConvcont instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AgrupArtDescComConvcont merge(
			AgrupArtDescComConvcont detachedInstance) {
		log.debug("merging AgrupArtDescComConvcont instance");
		try {
			AgrupArtDescComConvcont result = (AgrupArtDescComConvcont) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AgrupArtDescComConvcont findById(long id) {
		log.debug("getting AgrupArtDescComConvcont instance with id: " + id);
		try {
			AgrupArtDescComConvcont instance = (AgrupArtDescComConvcont) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AgrupArtDescComConvcont",
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

	public List findByExample(AgrupArtDescComConvcont instance) {
		log.debug("finding AgrupArtDescComConvcont instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AgrupArtDescComConvcont").add(
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
