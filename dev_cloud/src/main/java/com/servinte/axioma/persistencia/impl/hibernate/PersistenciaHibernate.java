package com.servinte.axioma.persistencia.impl.hibernate;


import java.sql.Connection;
import java.sql.SQLException;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.interfaz.IPersistencia;


/**
 * Encargada de controlar la persistencia con Hibernate
 * @author Juan David Ram&iacute;rez
 * @version 1.0.0
 * @since 23 Julio 2010
 */
public class PersistenciaHibernate implements IPersistencia 
{
	@Override
	public Connection obtenerConexion()
	{
		alterarSession();
		return HibernateUtil.obtenerConexion();
	}
	
	@Override
	public boolean alterarSession() {
		int tipoBD=DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		if(tipoBD==DaoFactory.ORACLE)
		{
			DaoFactory daoFactory=DaoFactory.getDaoFactory(DaoFactory.ORACLE);
			try {
				daoFactory.alterarSesionFechaOracle(HibernateUtil.obtenerConexion());
				return true;
			} catch (SQLException e) {
				Log4JManager.error("Error alterando la sessión de ORACLE para el manejo de las fechas", e);
				return false;
			}
		}
		else
		{
			Log4JManager.warning("No se requiere alterar la sessión, opr favor verifique");
		}
		return true;
	}
}
