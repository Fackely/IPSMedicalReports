package com.servinte.axioma.orm;

// Generated Jan 28, 2011 9:03:33 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UsoConsecutivosCaducados.
 * @see com.servinte.axioma.orm.UsoConsecutivosCaducados
 * @author Hibernate Tools
 */
public class UsoConsecutivosCaducadosHome {

	private static final Log log = LogFactory
			.getLog(UsoConsecutivosCaducadosHome.class);

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

	public void persist(UsoConsecutivosCaducados transientInstance) {
		log.debug("persisting UsoConsecutivosCaducados instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UsoConsecutivosCaducados instance) {
		log.debug("attaching dirty UsoConsecutivosCaducados instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UsoConsecutivosCaducados instance) {
		log.debug("attaching clean UsoConsecutivosCaducados instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UsoConsecutivosCaducados persistentInstance) {
		log.debug("deleting UsoConsecutivosCaducados instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UsoConsecutivosCaducados merge(
			UsoConsecutivosCaducados detachedInstance) {
		log.debug("merging UsoConsecutivosCaducados instance");
		try {
			UsoConsecutivosCaducados result = (UsoConsecutivosCaducados) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UsoConsecutivosCaducados findById(long id) {
		log.debug("getting UsoConsecutivosCaducados instance with id: " + id);
		try {
			UsoConsecutivosCaducados instance = (UsoConsecutivosCaducados) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UsoConsecutivosCaducados",
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

	public List findByExample(UsoConsecutivosCaducados instance) {
		log.debug("finding UsoConsecutivosCaducados instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UsoConsecutivosCaducados").add(
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
