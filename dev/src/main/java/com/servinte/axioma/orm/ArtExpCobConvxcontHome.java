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
 * Home object for domain model class ArtExpCobConvxcont.
 * @see com.servinte.axioma.orm.ArtExpCobConvxcont
 * @author Hibernate Tools
 */
public class ArtExpCobConvxcontHome {

	private static final Log log = LogFactory
			.getLog(ArtExpCobConvxcontHome.class);

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

	public void persist(ArtExpCobConvxcont transientInstance) {
		log.debug("persisting ArtExpCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ArtExpCobConvxcont instance) {
		log.debug("attaching dirty ArtExpCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ArtExpCobConvxcont instance) {
		log.debug("attaching clean ArtExpCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ArtExpCobConvxcont persistentInstance) {
		log.debug("deleting ArtExpCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ArtExpCobConvxcont merge(ArtExpCobConvxcont detachedInstance) {
		log.debug("merging ArtExpCobConvxcont instance");
		try {
			ArtExpCobConvxcont result = (ArtExpCobConvxcont) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ArtExpCobConvxcont findById(long id) {
		log.debug("getting ArtExpCobConvxcont instance with id: " + id);
		try {
			ArtExpCobConvxcont instance = (ArtExpCobConvxcont) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ArtExpCobConvxcont", id);
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

	public List findByExample(ArtExpCobConvxcont instance) {
		log.debug("finding ArtExpCobConvxcont instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ArtExpCobConvxcont").add(
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
