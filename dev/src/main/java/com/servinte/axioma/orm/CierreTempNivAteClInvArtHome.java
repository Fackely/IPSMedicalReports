package com.servinte.axioma.orm;

// Generated 14/01/2012 12:40:03 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreTempNivelAteClaseInvArt.
 * @see com.servinte.axioma.orm.CierreTempNivelAteClaseInvArt
 * @author Hibernate Tools
 */
public class CierreTempNivAteClInvArtHome {

	private static final Log log = LogFactory
			.getLog(CierreTempNivAteClInvArtHome.class);

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

	public void persist(CierreTempNivAteClInvArt transientInstance) {
		log.debug("persisting CierreTempNivelAteClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempNivAteClInvArt instance) {
		log.debug("attaching dirty CierreTempNivelAteClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempNivAteClInvArt instance) {
		log.debug("attaching clean CierreTempNivelAteClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempNivAteClInvArt persistentInstance) {
		log.debug("deleting CierreTempNivelAteClaseInvArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempNivAteClInvArt merge(
			CierreTempNivAteClInvArt detachedInstance) {
		log.debug("merging CierreTempNivelAteClaseInvArt instance");
		try {
			CierreTempNivAteClInvArt result = (CierreTempNivAteClInvArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempNivAteClInvArt findById(long id) {
		log.debug("getting CierreTempNivelAteClaseInvArt instance with id: "
				+ id);
		try {
			CierreTempNivAteClInvArt instance = (CierreTempNivAteClInvArt) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.CierreTempNivelAteClaseInvArt",
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

	@SuppressWarnings("rawtypes")
	public List findByExample(CierreTempNivAteClInvArt instance) {
		log.debug("finding CierreTempNivAteClInvArtHome instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.CierreTempNivelAteClaseInvArt")
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
