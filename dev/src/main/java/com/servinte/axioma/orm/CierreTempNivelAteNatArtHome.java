package com.servinte.axioma.orm;

// Generated Feb 7, 2011 11:17:38 AM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;


/**
 * Home object for domain model class CierreTempNivelAteNatArt.
 * @see com.servinte.axioma.orm.CierreTempNivelAteNatArt
 * @author Hibernate Tools
 */
public class CierreTempNivelAteNatArtHome {

	private static final Log log = LogFactory
			.getLog(CierreTempNivelAteNatArtHome.class);

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

	public void persist(CierreTempNivelAteNatArt transientInstance) {
		log.debug("persisting CierreTempNivelAteNatArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempNivelAteNatArt instance) {
		log.debug("attaching dirty CierreTempNivelAteNatArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempNivelAteNatArt instance) {
		log.debug("attaching clean CierreTempNivelAteNatArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempNivelAteNatArt persistentInstance) {
		log.debug("deleting CierreTempNivelAteNatArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempNivelAteNatArt merge(
			CierreTempNivelAteNatArt detachedInstance) {
		log.debug("merging CierreTempNivelAteNatArt instance");
		try {
			CierreTempNivelAteNatArt result = (CierreTempNivelAteNatArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempNivelAteNatArt findById(long id) {
		log.debug("getting CierreTempNivelAteNatArt instance with id: " + id);
		try {
			CierreTempNivelAteNatArt instance = (CierreTempNivelAteNatArt) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CierreTempNivelAteNatArt",
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

	public List findByExample(CierreTempNivelAteNatArt instance) {
		log.debug("finding CierreTempNivelAteNatArt instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreTempNivelAteNatArt").add(
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
