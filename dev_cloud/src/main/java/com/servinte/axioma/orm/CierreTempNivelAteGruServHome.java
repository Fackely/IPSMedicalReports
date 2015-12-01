package com.servinte.axioma.orm;

// Generated Feb 5, 2011 1:14:00 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreTempNivelAteGruServ.
 * @see com.servinte.axioma.orm.CierreTempNivelAteGruServ
 * @author Hibernate Tools
 */
public class CierreTempNivelAteGruServHome {

	private static final Log log = LogFactory
			.getLog(CierreTempNivelAteGruServHome.class);

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

	public void persist(CierreTempNivelAteGruServ transientInstance) {
		log.debug("persisting CierreTempNivelAteGruServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreTempNivelAteGruServ instance) {
		log.debug("attaching dirty CierreTempNivelAteGruServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreTempNivelAteGruServ instance) {
		log.debug("attaching clean CierreTempNivelAteGruServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreTempNivelAteGruServ persistentInstance) {
		log.debug("deleting CierreTempNivelAteGruServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreTempNivelAteGruServ merge(
			CierreTempNivelAteGruServ detachedInstance) {
		log.debug("merging CierreTempNivelAteGruServ instance");
		try {
			CierreTempNivelAteGruServ result = (CierreTempNivelAteGruServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreTempNivelAteGruServ findById(long id) {
		log.debug("getting CierreTempNivelAteGruServ instance with id: " + id);
		try {
			CierreTempNivelAteGruServ instance = (CierreTempNivelAteGruServ) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.CierreTempNivelAteGruServ",
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

	public List findByExample(CierreTempNivelAteGruServ instance) {
		log.debug("finding CierreTempNivelAteGruServ instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreTempNivelAteGruServ").add(
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
