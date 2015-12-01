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
 * Home object for domain model class SecTotalesFormatoFact.
 * @see com.servinte.axioma.orm.SecTotalesFormatoFact
 * @author Hibernate Tools
 */
public class SecTotalesFormatoFactHome {

	private static final Log log = LogFactory
			.getLog(SecTotalesFormatoFactHome.class);

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

	public void persist(SecTotalesFormatoFact transientInstance) {
		log.debug("persisting SecTotalesFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SecTotalesFormatoFact instance) {
		log.debug("attaching dirty SecTotalesFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SecTotalesFormatoFact instance) {
		log.debug("attaching clean SecTotalesFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SecTotalesFormatoFact persistentInstance) {
		log.debug("deleting SecTotalesFormatoFact instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SecTotalesFormatoFact merge(SecTotalesFormatoFact detachedInstance) {
		log.debug("merging SecTotalesFormatoFact instance");
		try {
			SecTotalesFormatoFact result = (SecTotalesFormatoFact) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SecTotalesFormatoFact findById(int id) {
		log.debug("getting SecTotalesFormatoFact instance with id: " + id);
		try {
			SecTotalesFormatoFact instance = (SecTotalesFormatoFact) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.SecTotalesFormatoFact", id);
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

	public List findByExample(SecTotalesFormatoFact instance) {
		log.debug("finding SecTotalesFormatoFact instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SecTotalesFormatoFact").add(
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
