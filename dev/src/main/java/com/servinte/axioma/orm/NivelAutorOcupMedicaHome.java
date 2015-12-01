package com.servinte.axioma.orm;

// Generated Sep 23, 2010 2:39:35 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NivelAutorOcupMedica.
 * @see com.servinte.axioma.orm.NivelAutorOcupMedica
 * @author Hibernate Tools
 */
public class NivelAutorOcupMedicaHome {

	private static final Log log = LogFactory
			.getLog(NivelAutorOcupMedicaHome.class);

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

	public void persist(NivelAutorOcupMedica transientInstance) {
		log.debug("persisting NivelAutorOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NivelAutorOcupMedica instance) {
		log.debug("attaching dirty NivelAutorOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NivelAutorOcupMedica instance) {
		log.debug("attaching clean NivelAutorOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NivelAutorOcupMedica persistentInstance) {
		log.debug("deleting NivelAutorOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NivelAutorOcupMedica merge(NivelAutorOcupMedica detachedInstance) {
		log.debug("merging NivelAutorOcupMedica instance");
		try {
			NivelAutorOcupMedica result = (NivelAutorOcupMedica) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NivelAutorOcupMedica findById(int id) {
		log.debug("getting NivelAutorOcupMedica instance with id: " + id);
		try {
			NivelAutorOcupMedica instance = (NivelAutorOcupMedica) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NivelAutorOcupMedica", id);
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

	public List findByExample(NivelAutorOcupMedica instance) {
		log.debug("finding NivelAutorOcupMedica instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NivelAutorOcupMedica").add(
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
