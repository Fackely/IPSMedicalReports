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
 * Home object for domain model class AgruArtIncluexcluCc.
 * @see com.servinte.axioma.orm.AgruArtIncluexcluCc
 * @author Hibernate Tools
 */
public class AgruArtIncluexcluCcHome {

	private static final Log log = LogFactory
			.getLog(AgruArtIncluexcluCcHome.class);

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

	public void persist(AgruArtIncluexcluCc transientInstance) {
		log.debug("persisting AgruArtIncluexcluCc instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AgruArtIncluexcluCc instance) {
		log.debug("attaching dirty AgruArtIncluexcluCc instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AgruArtIncluexcluCc instance) {
		log.debug("attaching clean AgruArtIncluexcluCc instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AgruArtIncluexcluCc persistentInstance) {
		log.debug("deleting AgruArtIncluexcluCc instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AgruArtIncluexcluCc merge(AgruArtIncluexcluCc detachedInstance) {
		log.debug("merging AgruArtIncluexcluCc instance");
		try {
			AgruArtIncluexcluCc result = (AgruArtIncluexcluCc) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AgruArtIncluexcluCc findById(long id) {
		log.debug("getting AgruArtIncluexcluCc instance with id: " + id);
		try {
			AgruArtIncluexcluCc instance = (AgruArtIncluexcluCc) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AgruArtIncluexcluCc", id);
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

	public List findByExample(AgruArtIncluexcluCc instance) {
		log.debug("finding AgruArtIncluexcluCc instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AgruArtIncluexcluCc").add(
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
