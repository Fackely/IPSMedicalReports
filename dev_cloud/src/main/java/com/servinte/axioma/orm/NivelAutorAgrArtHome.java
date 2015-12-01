package com.servinte.axioma.orm;

// Generated Jan 25, 2011 12:12:11 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NivelAutorAgrArt.
 * @see com.servinte.axioma.orm.NivelAutorAgrArt
 * @author Hibernate Tools
 */
public class NivelAutorAgrArtHome {

	private static final Log log = LogFactory
			.getLog(NivelAutorAgrArtHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(NivelAutorAgrArt transientInstance) {
		log.debug("persisting NivelAutorAgrArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NivelAutorAgrArt instance) {
		log.debug("attaching dirty NivelAutorAgrArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NivelAutorAgrArt instance) {
		log.debug("attaching clean NivelAutorAgrArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NivelAutorAgrArt persistentInstance) {
		log.debug("deleting NivelAutorAgrArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NivelAutorAgrArt merge(NivelAutorAgrArt detachedInstance) {
		log.debug("merging NivelAutorAgrArt instance");
		try {
			NivelAutorAgrArt result = (NivelAutorAgrArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NivelAutorAgrArt findById(int id) {
		log.debug("getting NivelAutorAgrArt instance with id: " + id);
		try {
			NivelAutorAgrArt instance = (NivelAutorAgrArt) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NivelAutorAgrArt", id);
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

	public List findByExample(NivelAutorAgrArt instance) {
		log.debug("finding NivelAutorAgrArt instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NivelAutorAgrArt").add(
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
