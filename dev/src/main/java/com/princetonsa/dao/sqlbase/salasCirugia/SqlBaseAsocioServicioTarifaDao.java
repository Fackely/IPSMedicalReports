package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Jose Eduardo Arias Doncel 
 * 
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Excepciones Tarifas Asocios
 */

public class SqlBaseAsocioServicioTarifaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseAsocioServicioTarifaDao.class);
	
	
	//**********************Cadenas del Encabezado
	
	/**
	 * Cadena usada para consultar todos los registros del encabezado de 
	 * Asocios Servicios Tarifa
	 * */
	private static final String cargarEncaAsociosServStr ="SELECT " +
			"e.codigo AS codigo_encab," +
			"e.institucion," +			
			"e.esq_tar_particular," +
			"esq.nombre AS nombre_esq_tar_part," +
			"e.esq_tar_general," +
			"tar.nombre AS nombre_esq_tar_gen,"+
			"e.convenio," +
			"getnombreconvenio(e.convenio) AS nombre_convenio," +
			"to_char(e.fecha_inicial,'DD/MM/YYYY') AS fecha_inicial," +
			"to_char(e.fecha_final,'DD/MM/YYYY') AS fecha_final," +
			"(SELECT COUNT(d.codigo) FROM det_aso_servicios_tarifa d WHERE d.codigo_aso_serv_tarifa = e.codigo) AS cuanto_detalle," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM asocios_servicios_tarifa e " +
			"LEFT OUTER JOIN esq_tar_porcen_cx esq ON (esq.codigo = e.esq_tar_particular) " +
			"LEFT OUTER JOIN esquemas_tarifarios tar ON (tar.codigo = e.esq_tar_general) " +
			"WHERE e.institucion = ? ";
	
	/**
	 * Cadena usada para actualizar un registro del encabezado
	 * */
	private static final String actualizarEncaAsociosSerStr="UPDATE " +
			"asocios_servicios_tarifa " +
			"SET esq_tar_particular=?, " +
			"esq_tar_general = ?," +
			"convenio = ?," +
			"fecha_inicial= ?," +
			"fecha_final = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE codigo = ?  ";
		
	/**
	 * Cadena usada para eliminar un registro del encabezado de asocios_servicios_tarifa
	 * */
	private static final String eliminarEncaAsociosStr = "DELETE FROM asocios_servicios_tarifa WHERE codigo = ? ";
	
	//**************************************************
	
	//**********************Cadenas del Detalle	
	
	/**
	 * Consulta de la informacion del detalle 
	 * */
	private static final String consultaDetalleStr = "SELECT " +
			"d.codigo," +
			"d.codigo_aso_serv_tarifa AS codigo_encab," +
			
			"CASE WHEN d.tipo_servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE d.tipo_servicio || '' END AS tipo_servicio," +
			"getnombretiposervicio(d.tipo_servicio) AS nombre_tipo_servicio," +
			
			"CASE WHEN d.especialidad IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.especialidad END AS especialidad, " +
			"getnombreespecialidad(d.especialidad) AS nombre_especialidad," +
			
			"CASE WHEN d.grupo_servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.grupo_servicio END AS grupo_servicio," +
			"getnombregruposervicio(d.grupo_servicio) AS nombre_grupo_servicio," +
			
			"CASE WHEN d.servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.servicio END AS servicio," +
			"getcodigoespecialidad(d.servicio) || ' - ' || d.servicio || ' ' || getnombreservicio(d.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreServicio," +

			"CASE WHEN d.asocio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.asocio END AS asocio," +
			"getnombretipoasocio(d.asocio) AS nombre_asocio," +
			
			" d.liquidar_por AS liquidarpor, " +			
			
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM " +
			"det_aso_servicios_tarifa d " +			
			"WHERE d.codigo_aso_serv_tarifa = ? ";	
	
	
	/**
	 * Modificacion de la informacion del detalle
	 * */
	private static final String actualizarDetalleStr=" UPDATE " +
			"det_aso_servicios_tarifa " +
			"SET " +
			"tipo_servicio = ?," +
			"especialidad = ?," +
			"grupo_servicio = ?," +
			"servicio = ?," +			
			"asocio = ?," +			
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ?," +
			"liquidar_por=? " +
			"WHERE " +
			"codigo = ? AND " +
			"codigo_aso_serv_tarifa = ? ";
	
	
	/**
	 * Eliminar la informacion del detalle
	 * */
	private static final String eliminarDetalleStr= "DELETE FROM det_aso_servicios_tarifa WHERE codigo = ? AND codigo_aso_serv_tarifa = ? ";	
	
	//***************Indicies***************************
	
	/**
	 * Indice del encabezado asocios_servicios_tarifa
	 * */
	private static final String[] indicesEncabMap = {"codigo_","institucion_","esq_tar_particular_","nombre_esq_tar_part_","esq_tar_general_","nombre_esq_tar_gen_","convenio_","nombre_convenio_","fecha_inicial_","fecha_final_","cuanto_detalle_","estabd_"};
	
	
	/**
	 * Indice del detalle det_aso_servicios_tarifa
	 * */
	private static final String[] indicesDetallebMap = {"codigo_","codigo_encab_","tipo_servicio_","nombre_tipo_servicio_","especialidad_","nombre_especialidad_","grupo_servicio_","nombre_grupo_servicio_","servicio_","nombreservicio_","asocio_","nombre_asocio_","estabd_"};
	
	//**************************************************
	
	
	
	//	****************Metodos*****************************************
	
	/**
	 * Método para cargar el listado de los asocios servicios tarifa
	 * parametrizadas por institución
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap cargarEncabAsociosServ(Connection con,HashMap parametros)
	{		
		
		String cadena = cargarEncaAsociosServStr;
		HashMap respuesta = new HashMap();
		
		if(parametros.containsKey("convenio"))		
			cadena+= " AND e.convenio = "+parametros.get("convenio")+" ";	
		else if(parametros.containsKey("esq_tar_particular"))	
			cadena+= " AND (e.esq_tar_particular = "+parametros.get("esq_tar_particular")+" AND e.esq_tar_general = "+parametros.get("esq_tar_general")+" ) ";				
				
		if(parametros.containsKey("codigo_encab"))
			cadena+= " AND e.codigo =  "+parametros.get("codigo_encab")+" ";	
		
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			respuesta =UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);			
			respuesta.put("INDICES_MAP",indicesDetallebMap);
			
			return respuesta;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar Encabezado Asocios Servicios : "+e);
			return null;
		}
	}
	
	
	
	/**
	 * Método usado para modificar un registros de encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public static boolean actualizarEncaAsociosSer(
			Connection con,
			HashMap parametros)
	{
		try
		{			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarEncaAsociosSerStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			/**
			 * UPDATE " +
			"asocios_servicios_tarifa " +
			"SET esq_tar_particular=?, " +
			"esq_tar_general = ?," +
			"convenio = ?," +
			"fecha_inicial= ?," +
			"fecha_final = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE codigo = ?  
			 */	
			
			
			//Esquema Tarifario			
			if(!parametros.get("esq_tar_particular").toString().equals(""))
				pst.setInt(1,Utilidades.convertirAEntero(parametros.get("esq_tar_particular")+""));
			else
				pst.setNull(1,Types.INTEGER);
			
			if(!parametros.get("esq_tar_general").toString().equals(""))
				pst.setInt(2,Utilidades.convertirAEntero(parametros.get("esq_tar_general")+""));
			else
				pst.setNull(2,Types.INTEGER);
			
			//Convenio
			if(!parametros.get("convenio").toString().equals(""))
				pst.setInt(3,Utilidades.convertirAEntero(parametros.get("convenio")+""));
			else
				pst.setNull(3,Types.INTEGER);
			
			//fecha_inicial 
			if(!parametros.get("fecha_inicialBD").toString().equals(""))
				pst.setDate(4,Date.valueOf(parametros.get("fecha_inicialBD")+""));
			else
				pst.setNull(4,Types.DATE);
			
			//fecha_final 
			if(!parametros.get("fecha_finalBD").toString().equals(""))
				pst.setDate(5,Date.valueOf(parametros.get("fecha_finalBD")+""));
			else
				pst.setNull(5,Types.DATE);			
						
			//Usuario Modifica
			pst.setString(6,parametros.get("usuario_modifica")+"");
			
			//Fecha Modifica
			pst.setDate(7,Date.valueOf(parametros.get("fecha_modifica")+""));
			
			//Hora Modifica
			pst.setString(8,parametros.get("hora_modifica")+"");
						
			//Codigo
			pst.setInt(9,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			if(pst.executeUpdate()>0)
				return true;
			
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizar Asocios Servicios Tarifas: "+e);
			return false;
		}
	}
	
	
	/**
	 * Método para eliminar un porcentaje de Asocios Servicios de Tarifa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarEncaAsociosServ(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarEncaAsociosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			if(pst.executeUpdate()>0)
				return true;
			
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en la eliminaciom de Asocios Servicios de Tarifa: "+e);
			return false;
		}
	}
	
	
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros
	 * @param insertarPorcentajeStr
	 */
	public static int insertarEncaAsociosServ(
			Connection con,
			HashMap parametros,
			String insertarEncaAsociosServStr)
	{		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarEncaAsociosServStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			//Institucion
			pst.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_aso_sertar"));
			pst.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			//Esquema Tarifario			
			if(!parametros.get("esq_tar_particular").toString().equals(""))
				pst.setInt(3,Utilidades.convertirAEntero(parametros.get("esq_tar_particular")+""));
			else
				pst.setNull(3,Types.INTEGER);			
			
			
			if(!parametros.get("esq_tar_general").toString().equals(""))
				pst.setInt(4,Utilidades.convertirAEntero(parametros.get("esq_tar_general")+""));
			else
				pst.setNull(4,Types.INTEGER);
			
			
			//Convenio
			if(!parametros.get("convenio").toString().equals(""))
				pst.setInt(5,Utilidades.convertirAEntero(parametros.get("convenio")+""));
			else
				pst.setNull(5,Types.INTEGER);
				
			
			//fecha_inicial 
			if(!parametros.get("fecha_inicialBD").toString().equals(""))
				pst.setDate(6,Date.valueOf(parametros.get("fecha_inicialBD")+""));
			else
				pst.setNull(6,Types.DATE);
				
			
			//fecha_final 
			if(!parametros.get("fecha_finalBD").toString().equals(""))
				pst.setDate(7,Date.valueOf(parametros.get("fecha_finalBD")+""));
			else
				pst.setNull(7,Types.DATE);				
						
			//Usuario Modifica
			pst.setString(8,parametros.get("usuario_modifica")+"");
			
			//Fecha Modifica
			pst.setDate(9,Date.valueOf(parametros.get("fecha_modifica")+""));
			
			//Hora Modifica
			pst.setString(10,parametros.get("hora_modifica")+"");
			
			
			if(pst.executeUpdate()>0)
			{
				String cadena ="";
				
				if(!parametros.get("convenio").toString().equals(""))
					cadena= "SELECT " +
							"codigo " +
							"FROM asocios_servicios_tarifa " +
							"WHERE " +							
							"convenio = "+parametros.get("convenio")+" AND " +
							"fecha_inicial = '"+parametros.get("fecha_inicialBD")+"' AND " +
							"fecha_final = '"+parametros.get("fecha_finalBD")+"' AND " +
							"institucion = "+parametros.get("institucion")+" "+
							""+ValoresPorDefecto.getValorLimit1()+" 1";
				else					
				{
					if(parametros.get("esq_tar_particular").toString().equals(""))						
							cadena = "SELECT " +
									"codigo " +
									"FROM asocios_servicios_tarifa " +
									"WHERE " +
									"esq_tar_particular  IS NULL AND " +
									"esq_tar_general = "+parametros.get("esq_tar_general")+" AND " +							
									"institucion = "+parametros.get("institucion")+" "+
									""+ValoresPorDefecto.getValorLimit1()+" 1";
					
					if(parametros.get("esq_tar_general").toString().equals(""))						
						cadena = "SELECT " +
								"codigo " +
								"FROM asocios_servicios_tarifa " +
								"WHERE " +
								"esq_tar_general  IS NULL AND " +
								"esq_tar_particular = "+parametros.get("esq_tar_particular")+" AND " +							
								"institucion = "+parametros.get("institucion")+" "+
								""+ValoresPorDefecto.getValorLimit1()+" 1";
				}
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				
				if(rs.next())			
				{			
					return rs.getInt(1);
				} 
			}			
			
			return ConstantesBD.codigoNuncaValido;
		
		}
		catch(SQLException e)
		{
			logger.error("Error en Insertar Asocios Servicios de Tarifa: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean insertarDetAsociosServicios(Connection con, HashMap parametros,String strInsertarDetalle)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_aso_servicios_tarifa(
			 * codigo,
			 * codigo_aso_serv_tarifa,
			 * tipo_servicio,
			 * especialidad,
			 * grupo_servicio,
			 * servicio,
			 * asocio,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * liquidar_por) 
			 */
						
			int valorsec =UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_detaso_ser_tar"); 
			logger.info("valor del secuencia en el detalle >> "+valorsec);
			ps.setInt(1,valorsec);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			if(!parametros.get("tipo_servicio").toString().equals("") &&
					!parametros.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(3,parametros.get("tipo_servicio")+"");
			else
				ps.setNull(3,Types.CHAR);
				
			if(!parametros.get("especialidad").toString().equals("") &&
					!parametros.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("especialidad")+""));
			else
				ps.setNull(4,Types.INTEGER);
			
			if(!parametros.get("grupo_servicio").toString().equals("") &&
					!parametros.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get("grupo_servicio")+""));			
			else
				ps.setNull(5,Types.INTEGER);
			
			if(!parametros.get("servicio").toString().equals("") &&
					!parametros.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(6,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			else				
				ps.setNull(6,Types.INTEGER);
			
			
			if(!parametros.get("asocio").toString().equals("") &&
					!parametros.get("asocio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(7,Utilidades.convertirAEntero(parametros.get("asocio")+""));
			else
				ps.setNull(7,Types.INTEGER);
			
			ps.setString(8,parametros.get("usuario_modifica")+"");
			ps.setDate(9,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(10,parametros.get("hora_modifica")+"");
			//liquidar_por
			ps.setString(11,parametros.get("liquidarpor")+"");			
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Modificar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean modificarDetAsociosServ(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * PDATE " +
			"det_aso_servicios_tarifa " +
			"SET " +
			"tipo_servicio = ?," +
			"especialidad = ?," +
			"grupo_servicio = ?," +
			"servicio = ?," +			
			"asocio = ?," +			
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ?," +
			"liquidar_por=? " +
			"WHERE " +
			"codigo = ? AND " +
			"codigo_aso_serv_tarifa = ? 
			 */
			
			
			if(!parametros.get("tipo_servicio").toString().equals("") &&
					!parametros.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(1,parametros.get("tipo_servicio")+"");
			else
				ps.setNull(1,Types.CHAR);
				
			if(!parametros.get("especialidad").toString().equals("")&&
					!parametros.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("especialidad")+""));
			else
				ps.setNull(2,Types.INTEGER);
			
			if(!parametros.get("grupo_servicio").toString().equals("")&&
					!parametros.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("grupo_servicio")+""));			
			else
				ps.setNull(3,Types.INTEGER);
			
			if(!parametros.get("servicio").toString().equals("")&&
					!parametros.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			else				
				ps.setNull(4,Types.INTEGER);
			
			
			if(!parametros.get("asocio").toString().equals("")&&
					!parametros.get("asocio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get("asocio")+""));
			else
				ps.setNull(5,Types.INTEGER);
									
			
			ps.setString(6,parametros.get("usuario_modifica")+"");
			ps.setDate(7,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(8,parametros.get("hora_modifica")+"");
			
			//lisquidar_por
			ps.setString(9,parametros.get("liquidarpor")+"");
			
			ps.setInt(10,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(11,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Eliminar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean EliminarDetAsociosServ(Connection con, HashMap parametros)
	{
		logger.info("\n entre a EliminarDetAsociosServ -->"+parametros);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));		
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * Consultar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static HashMap consultarDetAsociosServ(Connection con, HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		
		try
		{
			String cadena = consultaDetalleStr;
			
			if(parametros.containsKey("codigo"))
				cadena+=" AND d.codigo = "+parametros.get("codigo")+" ";			
			
			if(parametros.containsKey("tipo_servicioB"))
				cadena+=" AND d.tipo_servicio = '"+parametros.get("tipo_servicioB")+"' ";
			
			if(parametros.containsKey("grupo_servicioB"))
				cadena+=" AND d.grupo_servicio = "+parametros.get("grupo_servicioB")+" ";
			
			if(parametros.containsKey("especialidadB"))
				cadena+=" AND d.especialidad = "+parametros.get("especialidadB")+" ";
			
			if(parametros.containsKey("servicioB"))
				cadena+=" AND d.servicio = "+parametros.get("servicioB")+" ";			
			
			if(parametros.containsKey("asocioB"))
				cadena+=" AND d.asocio = "+parametros.get("asocioB")+" ";
			
			if (UtilidadCadena.noEsVacio(parametros.get("liquidar_porB")+""))
				cadena+=" AND d.liquidar_por ='"+parametros.get("liquidar_porB")+"'";
			
			cadena+=" ORDER BY  nombre_tipo_servicio,nombre_especialidad,nombre_grupo_servicio,nombreservicio, nombre_asocio ";
			
			logger.info("\n cadena -->"+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return respuesta;
	}		
	
	//	****************************************************************	
}