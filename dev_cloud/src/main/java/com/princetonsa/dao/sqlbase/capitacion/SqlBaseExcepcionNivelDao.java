/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseExcepcionNivelDao {


	/**
	 * Para manejar las excepciones de la clase.
	 */
	public static Logger logger = Logger.getLogger(SqlBaseExcepcionNivelDao.class);
	
	
	/**
	 * Metodo para cargar los niveles de Servicios.
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap cargarInformacion(Connection con, int codigoInstitucion) 
	{
		String consulta="	SELECT c.codigo AS cod_convenio, c.nombre AS nom_convenio																	" +
						"		   FROM convenios c																										" +
						"				WHERE c.codigo IN (	SELECT vcont.convenio 																		" +	
						"										   FROM view_contratos_vigentes vcont 													" +
						"												INNER JOIN convenios conv ON (conv.codigo=vcont.convenio)					 	" +
						"													 WHERE conv.tipo_contrato != "+ConstantesBD.codigoTipoContratoEvento+
						"													   AND conv.institucion = "+codigoInstitucion+
						"													   AND vcont.es_vigente = " + ValoresPorDefecto.getValorTrueParaConsultas() +
						"													   	   GROUP BY vcont.convenio												" +
						"							   	   )																							" +
						"			  ORDER BY c.nombre";
		PreparedStatementDecorator pst= null;
		try
		{
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando Infoirmacion de Niveles de Servicios ["+ e + "]");
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseExcepcionNivelDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * Metodo para traer los servicios de un convenio especifico
	 * @param con
	 * @return
	 */
	public static HashMap cargarServiciosConvenio(Connection con, int nroConsulta,  int codigoConvenio, int institucion, int nroContrato)
	{
		String consulta=""; 
		
		if ( nroConsulta == 0 ) //-- Se cargan los Contratos vigentes de un convenio seleccionado. 
		{
			consulta="	SELECT vcont.codigo as codigo_contrato, vcont.numero_contrato as numero_contrato,		" +
					 "		  to_char(vcont.fecha_inicial, 'DD/MM/YYYY')  as fecha_inicial, 					" +
					 "		  to_char(vcont.fecha_final, 'DD/MM/YYYY')  as fecha_final,		 					" +
					 "		  vcont.fecha_final as fecha_final													" +
					 "		   FROM view_contratos_vigentes vcont 												" +
					 "				INNER JOIN convenios conv ON(conv.codigo=vcont.convenio)					" +	
					 "					 WHERE conv.tipo_contrato!="+ConstantesBD.codigoTipoContratoEvento +
					 "					   AND conv.institucion="+institucion+
					 "					   AND vcont.es_vigente=" + ValoresPorDefecto.getValorTrueParaConsultas() +
					 "					   AND vcont.convenio = " + codigoConvenio;	

		} 
		if ( nroConsulta == 1 ) //-- Se cargan los Servicios de un contrato especifico.
		{
		  consulta = "	SELECT e.consecutivo as consecutivo, e.contrato as cod_contrato, e.servicio as cod_servicio,					" +
					 "		   rs.descripcion as nom_servicio, ns.descripcion as nivel, 												" +
					 "		   to_char(c.fecha_inicial, 'DD/MM/YYYY')  || ' -- ' ||	to_char(c.fecha_final, 'DD/MM/YYYY') as vigencia	" +
					 "	   	   FROM capitacion.excepcion_nivel e 																		" +		
					 "	   			INNER JOIN servicios s ON ( s.codigo = e.servicio )													" +
					 "	   			INNER JOIN referencias_servicio rs ON ( rs.servicio = s.codigo )									" +
					 "	   			INNER JOIN contratos c ON ( c.codigo = e.contrato )													" +
					 "	   			LEFT OUTER JOIN capitacion.nivel_atencion ns ON ( ns.consecutivo = s.nivel )						" +
					 "					 WHERE e.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + 
					 "					   AND rs.tipo_tarifario = "  +  ConstantesBD.codigoTarifarioCups +
		  			 "					   AND e.contrato = " + nroContrato;
		}
		PreparedStatementDecorator pst= null;
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando Informacion de Niveles de Servicios ["+ e + "]");
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseExcepcionNivelDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Metodo para insertar los servicios de un convenio especifico
	 * @param con
	 * @param servicio
	 * @param convenio
	 * @return
	 */

	public static int insertarServiciosConvenio(Connection con, int servicio, int contrato, int institucion)
	{
		String consulta = "INSERT INTO capitacion.excepcion_nivel ( consecutivo, contrato, servicio, activo  )  " +
						  "			   VALUES (" + UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_excepcion_nivel")+",?, ?," + ValoresPorDefecto.getValorTrueParaConsultas() + ") ";
		PreparedStatementDecorator stm= null;
		int resultado = 0;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setInt(1, contrato);
			stm.setInt(2, servicio);
			resultado = stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando insertarServiciosConvenio  : "+e);
			resultado = ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseExcepcionNivelDao "+sqlException.toString() );
			}
			
		}
		return resultado;
	}

	/**
	 * Metodo para eliminar el contrato.
	 * @param con
	 * @param nroServicio
	 * @param nroContrato
	 * @return
	 */
	public static int eliminarServicioContrato(Connection con, int nroServicio, int nroContrato) 
	{
		String consulta = " DELETE FROM capitacion.excepcion_nivel WHERE consecutivo = ? AND contrato = ?  "; 
		PreparedStatementDecorator stm= null;
		int resultado = 0;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setDouble(1, Utilidades.convertirADouble(nroServicio+""));
			stm.setInt(2, nroContrato);
			resultado =  stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error En (eliminarServicioContrato) : "+e);
			resultado = ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseExcepcionNivelDao "+sqlException.toString() );
			}
			
		}
		return resultado;

	}

}
