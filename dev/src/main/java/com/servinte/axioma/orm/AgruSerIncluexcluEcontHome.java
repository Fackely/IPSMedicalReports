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
 * Home object for domain model class AgruSerIncluexcluEcont.
 * @see com.servinte.axioma.orm.AgruSerIncluexcluEcont
 * @author Hibernate Tools
 */
public class AgruSerIncluexcluEcontHome {

	private static final Log log = LogFactory
			.getLog(AgruSerIncluexcluEcontHome.class);

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

	public void persist(AgruSerIncluexcluEcont transientInstance) {
		log.debug("persisting AgruSerIncluexcluEcont instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AgruSerIncluexcluEcont instance) {
		log.debug("attaching dirty AgruSerIncluexcluEcont instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AgruSerIncluexcluEcont instance) {
		log.debug("attaching clean AgruSerIncluexcluEcont instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AgruSerIncluexcluEcont persistentInstance) {
		log.debug("deleting AgruSerIncluexcluEcont instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AgruSerIncluexcluEcont merge(AgruSerIncluexcluEcont detachedInstance) {
		log.debug("merging AgruSerIncluexcluEcont instance");
		try {
			AgruSerIncluexcluEcont result = (AgruSerIncluexcluEcont) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AgruSerIncluexcluEcont findById(long id) {
		log.debug("getting AgruSerIncluexcluEcont instance with id: " + id);
		try {
			AgruSerIncluexcluEcont instance = (AgruSerIncluexcluEcont) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AgruSerIncluexcluEcont",
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

	public List findByExample(AgruSerIncluexcluEcont instance) {
		log.debug("finding AgruSerIncluexcluEcont instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AgruSerIncluexcluEcont").add(
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
