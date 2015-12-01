/*
 * @(#)HistoricoAdmisiones.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.TipoNumeroId;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HistoricoAdmisionesDao;
import com.princetonsa.dao.postgresql.PostgresqlHistoricoAdmisionesDao;

/**
 * Esta clase obtiene el listado de datos b�sicos de admisiones hospitalarias y de urgencias que no estan activas.
 * Tambi�n obtiene los datos b�sicos de la admision hospitalaria y de urgencias que esta activa
 *
 * @version 1.0, Ago 22, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>.
 */

public class HistoricoAdmisiones {

	private Collection urgencias = null;
	private Collection hospitalarias = null;
	/**
	 * Identificacion del paciente
	 */
	private TipoNumeroId idPaciente;

	/**
	 * Codigo de la instituci�n
	 */
	private int codigoInstitucion;
	/**
	 * El DAO usado por el objeto <code>HistoricoAdmisiones</code> para acceder a la fuente de datos.
	 */
	private HistoricoAdmisionesDao historicoAdmisionesDao;

	/**
	 * Para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(PostgresqlHistoricoAdmisionesDao.class);

	
	public HistoricoAdmisiones(TipoNumeroId idPaciente, int codigoInstitucion)
	{
		this.idPaciente = idPaciente;
		this.codigoInstitucion = codigoInstitucion;
		init(System.getProperty("TIPOBD"));	
		this.urgencias = this.cargarAdmisionesUrgenciasInactivas();	
		this.hospitalarias = this.cargarAdmisionesHospitalariasInactivas();		
	}
	public int size()
	{
		if(this.hospitalarias != null && this.urgencias != null)
			return this.urgencias.size() + this.hospitalarias.size();
		if(this.urgencias == null && this.hospitalarias != null)
			return this.hospitalarias.size();
		if(this.hospitalarias == null && this.urgencias != null)
			return this.urgencias.size();
		return 0;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) {

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			historicoAdmisionesDao = myFactory.getHistoricoAdmisionesDao();
			wasInited = (historicoAdmisionesDao != null);
		}

		return wasInited;

	}
	
	public Collection cargarAdmisionesHospitalariasInactivas()
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = null;
		Collection col;
		if (myFactory != null) {
			try
			{			
				con = myFactory.getConnection();
				col = historicoAdmisionesDao.cargarAdmisionesHospitalarias(con, this.idPaciente.getNumeroId(), this.idPaciente.getTipoId(), this.codigoInstitucion);
				UtilidadBD.closeConnection(con);
				return col;
			}
			catch (SQLException sql)
			{
				try {				
					if(con != null) UtilidadBD.cerrarConexion(con);
				}
				catch (SQLException ex)
				{
					//Error		
					logger.error("La conexi�n no se pudo cerrar:\nDetalles excepci�n:"+sql);					
				}
				//Error		
				logger.error("Error realizando la consulta:\nDetalles excepci�n:"+sql);
			}
		}
		//Error		
		logger.error("Error: Objeto DaoFactory obtenido para la base de datos "+System.getProperty("TIPOBD")+" es nulo");
 		return null;
	}
	
	public Collection cargarAdmisionesUrgenciasInactivas()
	{	
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = null;
		Collection col;
		if (myFactory != null) {
			try
			{			
				con = myFactory.getConnection();				
				col = historicoAdmisionesDao.cargarAdmisionesUrgencias(con, this.idPaciente.getNumeroId(), this.idPaciente.getTipoId(), this.codigoInstitucion);
				UtilidadBD.closeConnection(con);
				return col;
			}
			catch (SQLException sql)
			{
				try {				
					if(con != null) UtilidadBD.cerrarConexion(con);
				}
				catch (SQLException ex)
				{
					//Error		
					logger.error("La conexi�n no se pudo cerrar:\nDetalles excepci�n:"+sql);					
				}
				//Error		
				logger.error("Error realizando la consulta:\nDetalles excepci�n:"+sql);
			}
		}
		//Error		
		logger.error("Error: Objeto DaoFactory obtenido para la base de datos "+System.getProperty("TIPOBD")+" es nulo");		
		return null;
	}
	/**
	 * @return
	 */
	public Collection getHospitalarias() {
		return hospitalarias;
	}

	/**
	 * @return
	 */
	public Collection getUrgencias() {
		return urgencias;
	}

	/**
	 * @param collection
	 */
	public void setHospitalarias(Collection collection) {
		hospitalarias = collection;
	}

	/**
	 * @param collection
	 */
	public void setUrgencias(Collection collection) {
		urgencias = collection;
	}
	
	public Collection getConcatUrgenciasHospitalarias()
	{
		Vector col = new Vector();
		col.addAll(this.urgencias);
		col.addAll(this.hospitalarias);
		if(col.size() > 0) return col;
		return null;
	}
}