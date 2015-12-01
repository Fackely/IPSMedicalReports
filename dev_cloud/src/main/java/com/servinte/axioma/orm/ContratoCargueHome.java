package com.servinte.axioma.orm;

// Generated 17/06/2011 03:38:26 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ContratoCargue.
 * @see com.servinte.axioma.orm.ContratoCargue
 * @author Hibernate Tools
 */
public class ContratoCargueHome {

	private static final Log log = LogFactory.getLog(ContratoCargueHome.class);

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

	public void persist(ContratoCargue transientInstance) {
		log.debug("persisting ContratoCargue instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ContratoCargue instance) {
		log.debug("attaching dirty ContratoCargue instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ContratoCargue instance) {
		log.debug("attaching clean ContratoCargue instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ContratoCargue persistentInstance) {
		log.debug("deleting ContratoCargue instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ContratoCargue merge(ContratoCargue detachedInstance) {
		log.debug("merging ContratoCargue instance");
		try {
			ContratoCargue result = (ContratoCargue) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ContratoCargue findById(long id) {
		log.debug("getting ContratoCargue instance with id: " + id);
		try {
			ContratoCargue instance = (ContratoCargue) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ContratoCargue", id);
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

	public List findByExample(ContratoCargue instance) {
		log.debug("finding ContratoCargue instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.ContratoCargue")
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
