package com.princetonsa.dao.oracle.tramiteReferencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

import com.princetonsa.dao.tramiteReferencia.TramiteReferenciaDao;
import com.princetonsa.dao.sqlbase.tramiteReferencia.SqlBaseTramiteReferenciaDao;
import com.princetonsa.decorator.PreparedStatementDecorator;

public class OracleTramiteReferenciaDao implements TramiteReferenciaDao
{
	Logger logger=Logger.getLogger(OracleTramiteReferenciaDao.class);
	
	/**
	 * Cadena que inactiva un registro se servicios instituciones referencia
	 */
	private static final String inactivarServiciosInstitucionesReferencia = "UPDATE servic_instit_referencia SET activo = ? WHERE codigo_servicio = ? AND codigo_servicio_sirc = ? AND institucion_referir = ? AND institucion = ? AND numero_referencia_tramite = ? ";

	/**
	 * Cadena de Modificacion de servicios instituciones referencia 
	 * */
	private static final String cadenaActualizacionServiciosInstitucionesReferencia = "UPDATE historiaclinica.servic_instit_referencia " +
																					  "SET codigo_servicio = ?, " +
																					  "institucion_referir = ?, " +
																					  "activo = ?, " +
																					  "estado = ?, " +
																					  "motivo = ?, " +
																					  "funcionario_contactado = ?, " +																					 
																					  "cargo = ?, " +
																					  "fecha_tramite = ?, " +
																					  "hora_tramite = ?, " +
																					  "numero_verificacion = ?, " +
																					  "observaciones = ?, " +
																					  "usuario_modifica = ?, " +
																					  "fecha_modifica = ?, " +
																					  "hora_modifica = ? " +
																					  "WHERE codigo_servicio = ? AND codigo_servicio_sirc = ? AND institucion_referir = ? AND institucion = ? AND numero_referencia_tramite = ? " ;

	/**
	 * Inserccion servicios instituciones referencia 
	 * */
	private static final String cadenaInserccionServiciosInstitucionesReferencia = "INSERT INTO historiaclinica.servic_instit_referencia( " +
																				   "numero_referencia_tramite," +
																				   "codigo_servicio_sirc," +
																				   "codigo_servicio," +
																				   "institucion_referir, " +
																				   "institucion," +																				   
																				   "activo," +
																				   "estado," +
																				   "motivo," +
																				   "funcionario_contactado," +																				   
																				   "cargo," +
																				   "fecha_tramite, " +
																				   "hora_tramite," +
																				   "numero_verificacion," +
																				   "observaciones," +
																				   "usuario_modifica," +
																				   "fecha_modifica," +
																				   "hora_modifica)  " +
																				   "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)	"; 

	
	/**
	 * Consultar Listado de Referencia
	 * @param Connection 
	 * @param HashMap parametrosBusqueda (institucion,referencia,estado1,estado2)
	 * */
	public HashMap consultarListadoReferencia(Connection con, HashMap parametrosBusqueda)
	{
		return SqlBaseTramiteReferenciaDao.consultarListadoReferencia(con, parametrosBusqueda);
	}

	/**
	 * Consultar Tramite Referencia
	 * @param Connection 
	 * @param int numero de referencia tramite  
	 * */
	public HashMap consultarTramiteReferencia(Connection con, int numeroReferenciaTramite)
	{
		return SqlBaseTramiteReferenciaDao.consultarTramiteReferencia(con, numeroReferenciaTramite);
	}
	
	/**
	 * Actualiza Tramite Referencia
	 * @param Connection 
	 * @param HashMap tramiteReferencia  
	 * */
	public boolean actualizarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		return SqlBaseTramiteReferenciaDao.actualizarTramiteReferencia(con, tramiteReferencia);
	}
	
	/**
	 * Inserta un registro en la tabla tramite referencia 
	 * Connection con
	 * HashMap tramiteReferencia 
	 * */
	public boolean insertarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		return SqlBaseTramiteReferenciaDao.insertarTramiteReferencia(con, tramiteReferencia);
	}
	
	/**
	 * Consulta instituciones tramite referencia
	 * @param Connection con
	 * @param HashMap parametros	 
	 * */
	public HashMap consultarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.consultarInstitucionesTramiteReferencia(con, parametros);
	}	
	
	
	/**
	 * Actualiza un registro en instituciones tramite referencia 
	 * @param Connection con
	 * @param HashMap Parametros 
	 * */
	public boolean actualizarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.actualizarInstitucionesTramiteReferencia(con, parametros);
	}
	
	/**
	 * Inserta un registro en instituciones tramite referencia
	 * @param Connection 
	 * @param HashMap parametros
	 * */
	public boolean insertarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.insertarInstitucionesTramiteReferencia(con, parametros);
	}
	
	/**
	  * Consultar Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public HashMap consultarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.consultarServiciosInstitucionesReferencia(con, parametros);
	}
	
	/**
	 * Actualizar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		try
		{
			String estado = parametros.get("estado").toString(); 
			
			PreparedStatementDecorator ps = null;
			
			//Mientras que esté activo quiere decir que hay modificacion
			if(UtilidadTexto.getBoolean(parametros.get("activo").toString()))
			{
			
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionServiciosInstitucionesReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Integer.parseInt(parametros.get("codigoservicio").toString()));
				ps.setString(2,parametros.get("institucionreferir").toString());
				ps.setString(3,parametros.get("activo").toString());
				ps.setString(4,estado);
				
				if(!parametros.get("motivo").toString().equals(ConstantesBD.codigoNuncaValido+"")&&
					(estado.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado) ||estado.equals(ConstantesIntegridadDominio.acronimoEstadoNegado)))
					ps.setString(5,parametros.get("motivo").toString());
				else
					ps.setNull(5,Types.NUMERIC);
							
				ps.setString(6,parametros.get("funcionariocontactado").toString());
				ps.setString(7,parametros.get("cargo").toString());
				ps.setString(8,parametros.get("fechatramite").toString());
				ps.setString(9,parametros.get("horatramite").toString());
//				logger.info("parametros.get(numeroverificacion) "+parametros.get("numeroverificacion"));
				if(!parametros.get("numeroverificacion").toString().equals(""))
				{
					ps.setString(10,parametros.get("numeroverificacion").toString());
				}
				else
				{
					ps.setString(10," ");
				}
				ps.setString(11,parametros.get("observaciones").toString());
				ps.setString(12,parametros.get("usuariomodifica").toString());
				ps.setString(13,parametros.get("fechamodifica").toString());
				ps.setString(14,parametros.get("horamodifica").toString());
				
				ps.setInt(15,Integer.parseInt(parametros.get("codigoservicio").toString()));
				ps.setInt(16,Integer.parseInt(parametros.get("codigoserviciosirc").toString()));
				ps.setString(17,parametros.get("institucionreferir").toString());
				ps.setInt(18,Integer.parseInt(parametros.get("institucion").toString()));
				ps.setInt(19,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));			
								
			}
			//De lo contrario solo se inactiva
			else
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarServiciosInstitucionesReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,parametros.get("activo").toString());
				ps.setInt(2,Integer.parseInt(parametros.get("codigoservicio").toString()));
				ps.setInt(3,Integer.parseInt(parametros.get("codigoserviciosirc").toString()));
				ps.setString(4,parametros.get("institucionreferir").toString());
				ps.setInt(5,Integer.parseInt(parametros.get("institucion").toString()));
				ps.setInt(6,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
				
			}
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Insertar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		try
		{			
			String estado = parametros.get("estado").toString(); 
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInserccionServiciosInstitucionesReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			
			ps.setInt(1,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
			ps.setInt(2,Integer.parseInt(parametros.get("codigoserviciosirc").toString()));
			ps.setInt(3,Integer.parseInt(parametros.get("codigoservicio").toString()));
			ps.setString(4,parametros.get("institucionreferir").toString());
			ps.setInt(5,Integer.parseInt(parametros.get("institucion").toString()));
						
			ps.setString(6,parametros.get("activo").toString());
			ps.setString(7,estado);
			
			if(!parametros.get("motivo").toString().equals(ConstantesBD.codigoNuncaValido+"")&&
				(estado.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado) ||estado.equals(ConstantesIntegridadDominio.acronimoEstadoNegado)))
				ps.setString(8,parametros.get("motivo").toString());
			else
				ps.setNull(8,Types.NUMERIC);
			
			
			ps.setString(9,parametros.get("funcionariocontactado").toString());
			ps.setString(10,parametros.get("cargo").toString());
			ps.setString(11,parametros.get("fechatramite").toString());
			ps.setString(12,parametros.get("horatramite").toString());
			if(!parametros.get("numeroverificacion").toString().equals(""))
			{
				ps.setString(13,parametros.get("numeroverificacion").toString());
			}
			else
			{
				ps.setString(13," ");
			}
			ps.setString(14,parametros.get("observaciones").toString());
			ps.setString(15,parametros.get("usuariomodifica").toString());
			ps.setString(16,parametros.get("fechamodifica").toString());
			ps.setString(17,parametros.get("horamodifica").toString());
			
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	  * Consultar Historial Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public HashMap consultarHistorialServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.consultarHistorialServiciosInstitucionesReferencia(con, parametros);			
	}
	
	/**
	  * Consultar Traslado Paciente 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public HashMap consultarTrasladoPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.consultarTrasladoPaciente(con, parametros);		
	}
	
	/**
	 * Actualizar Traslado Paciente 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarTrasladoPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.actualizarTrasladoPaciente(con, parametros);
	}
	
	/**
	 * Insertar Traslado Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean insertarTrasladoPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseTramiteReferenciaDao.insertarTrasladoPaciente(con, parametros);
	}
	
	/**
	  * Consultar Historial Traslado Paciente 
	  * @param Connection con
	  *	@param int numeroreferenciatramite
	  * @param int institucionsirc
	  * @param int institucion
	  * */
	public  HashMap consultarHistorialTrasladoPaciente(Connection con, int numeroreferenciatramite, String institucionsirc, int institucion )
	{
		return SqlBaseTramiteReferenciaDao.consultarHistorialTrasladoPaciente(con,numeroreferenciatramite,institucionsirc,institucion ); 
	}
}