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
 * Home object for domain model class SecNotaPieFormatoFact.
 * @see com.servinte.axioma.orm.SecNotaPieFormatoFact
 * @author Hibernate Tools
 */
public class SecNotaPieFormatoFactHome {

	private static final Log log = LogFactory
			.getLog(SecNotaPieFormatoFactHome.class);

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

	public void persist(SecNotaPieFormatoFact transientInstance) {
		log.debug("persisting SecNotaPieFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SecNotaPieFormatoFact instance) {
		log.debug("attaching dirty SecNotaPieFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SecNotaPieFormatoFact instance) {
		log.debug("attaching clean SecNotaPieFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SecNotaPieFormatoFact persistentInstance) {
		log.debug("deleting SecNotaPieFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SecNotaPieFormatoFact merge(SecNotaPieFormatoFact detachedInstance) {
		log.debug("merging SecNotaPieFormatoFact instance");
		try {
			SecNotaPieFormatoFact result = (SecNotaPieFormatoFact) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SecNotaPieFormatoFact findById(int id) {
		log.debug("getting SecNotaPieFormatoFact instance with id: " + id);
		try {
			SecNotaPieFormatoFact instance = (SecNotaPieFormatoFact) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.SecNotaPieFormatoFact", id);
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

	public List findByExample(SecNotaPieFormatoFact instance) {
		log.debug("finding SecNotaPieFormatoFact instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SecNotaPieFormatoFact").add(
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
