package com.servinte.axioma.orm;

// Generated Feb 15, 2011 5:27:26 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CertAtenMedicaFurips.
 * @see com.servinte.axioma.orm.CertAtenMedicaFurips
 * @author Hibernate Tools
 */
public class CertAtenMedicaFuripsHome {

	private static final Log log = LogFactory
			.getLog(CertAtenMedicaFuripsHome.class);

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

	public void persist(CertAtenMedicaFurips transientInstance) {
		log.debug("persisting CertAtenMedicaFurips instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CertAtenMedicaFurips instance) {
		log.debug("attaching dirty CertAtenMedicaFurips instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CertAtenMedicaFurips instance) {
		log.debug("attaching clean CertAtenMedicaFurips instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CertAtenMedicaFurips persistentInstance) {
		log.debug("deleting CertAtenMedicaFurips instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CertAtenMedicaFurips merge(CertAtenMedicaFurips detachedInstance) {
		log.debug("merging CertAtenMedicaFurips instance");
		try {
			CertAtenMedicaFurips result = (CertAtenMedicaFurips) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CertAtenMedicaFurips findById(long id) {
		log.debug("getting CertAtenMedicaFurips instance with id: " + id);
		try {
			CertAtenMedicaFurips instance = (CertAtenMedicaFurips) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CertAtenMedicaFurips", id);
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

	public List findByExample(CertAtenMedicaFurips instance) {
		log.debug("finding CertAtenMedicaFurips instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CertAtenMedicaFurips").add(
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
