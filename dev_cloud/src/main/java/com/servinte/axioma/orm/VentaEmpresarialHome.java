package com.servinte.axioma.orm;

// Generated Aug 19, 2010 5:10:56 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class VentaEmpresarial.
 * @see com.servinte.axioma.orm.VentaEmpresarial
 * @author Hibernate Tools
 */
public class VentaEmpresarialHome {

	private static final Log log = LogFactory
			.getLog(VentaEmpresarialHome.class);

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

	public void persist(VentaEmpresarial transientInstance) {
		log.debug("persisting VentaEmpresarial instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(VentaEmpresarial instance) {
		log.debug("attaching dirty VentaEmpresarial instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(VentaEmpresarial instance) {
		log.debug("attaching clean VentaEmpresarial instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(VentaEmpresarial persistentInstance) {
		log.debug("deleting VentaEmpresarial instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public VentaEmpresarial merge(VentaEmpresarial detachedInstance) {
		log.debug("merging VentaEmpresarial instance");
		try {
			VentaEmpresarial result = (VentaEmpresarial) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public VentaEmpresarial findById(long id) {
		log.debug("getting VentaEmpresarial instance with id: " + id);
		try {
			VentaEmpresarial instance = (VentaEmpresarial) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.VentaEmpresarial", id);
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

	public List findByExample(VentaEmpresarial instance) {
		log.debug("finding VentaEmpresarial instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.VentaEmpresarial").add(
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
