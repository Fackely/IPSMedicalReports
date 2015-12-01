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
 * Home object for domain model class ValoresPorDefecto.
 * @see com.servinte.axioma.orm.ValoresPorDefecto
 * @author Hibernate Tools
 */
public class ValoresPorDefectoHome {

	private static final Log log = LogFactory
			.getLog(ValoresPorDefectoHome.class);

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

	public void persist(ValoresPorDefecto transientInstance) {
		log.debug("persisting ValoresPorDefecto instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ValoresPorDefecto instance) {
		log.debug("attaching dirty ValoresPorDefecto instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ValoresPorDefecto instance) {
		log.debug("attaching clean ValoresPorDefecto instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ValoresPorDefecto persistentInstance) {
		log.debug("deleting ValoresPorDefecto instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ValoresPorDefecto merge(ValoresPorDefecto detachedInstance) {
		log.debug("merging ValoresPorDefecto instance");
		try {
			ValoresPorDefecto result = (ValoresPorDefecto) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ValoresPorDefecto findById(
			com.servinte.axioma.orm.ValoresPorDefectoId id) {
		log.debug("getting ValoresPorDefecto instance with id: " + id);
		try {
			ValoresPorDefecto instance = (ValoresPorDefecto) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ValoresPorDefecto", id);
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

	public List findByExample(ValoresPorDefecto instance) {
		log.debug("finding ValoresPorDefecto instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ValoresPorDefecto").add(
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
