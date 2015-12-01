package com.servinte.axioma.orm;

// Generated 7/07/2011 11:30:49 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ConceptoNotaPaciente.
 * @see com.servinte.axioma.orm.ConceptoNotaPaciente
 * @author Hibernate Tools
 */
public class ConceptoNotaPacienteHome {

	private static final Log log = LogFactory
			.getLog(ConceptoNotaPacienteHome.class);

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

	public void persist(ConceptoNotaPaciente transientInstance) {
		log.debug("persisting ConceptoNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ConceptoNotaPaciente instance) {
		log.debug("attaching dirty ConceptoNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ConceptoNotaPaciente instance) {
		log.debug("attaching clean ConceptoNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ConceptoNotaPaciente persistentInstance) {
		log.debug("deleting ConceptoNotaPaciente instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ConceptoNotaPaciente merge(ConceptoNotaPaciente detachedInstance) {
		log.debug("merging ConceptoNotaPaciente instance");
		try {
			ConceptoNotaPaciente result = (ConceptoNotaPaciente) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ConceptoNotaPaciente findById(long id) {
		log.debug("getting ConceptoNotaPaciente instance with id: " + id);
		try {
			ConceptoNotaPaciente instance = (ConceptoNotaPaciente) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ConceptoNotaPaciente", id);
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

	public List findByExample(ConceptoNotaPaciente instance) {
		log.debug("finding ConceptoNotaPaciente instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.ConceptoNotaPaciente")
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
