package com.princetonsa.dao.sqlbase.agendaProcedimiento;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Types;
import com.princetonsa.decorator.ResultSetDecorator;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.agendaProcedimiento.UnidadProcedimiento;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class SqlBaseUnidadProcedimiento
{
	//-- Atributos
	
	/**
	 * Maneja los mensajes de error
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseUnidadProcedimiento.class);
	
	/**
	 * cadena de Consulta de Unidades de Procedimiento
	 * */
	private static final String cadenaConsultarUnidadProcStr = 	"SELECT codigo_undproce AS codigo, " +
																"institucion AS institucion, " +
																"codigo_especiali AS especialidad, " +
																"descrip_undproce AS descripcion, " +
																"activo_undproce AS activo, " +
																"'"+ConstantesBD.acronimoSi+"' AS estabd "+	
																"FROM agendaprocedimiento.unidad_procedimiento ";
	
	/**
	 * cadena de consulta Cuenta si existe elementos de Servicio 
	 * */
	private static final String cadenaConsultarServicioUndProcCount ="SELECT COUNT(codigo_servicio) AS cuenta FROM agendaprocedimiento.servicios_undproce" +
																	 " WHERE codigo_undproce=? ";

	
	/**
	 * cadena de Consulta de Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaConsultarServicioUndProcStr = "SELECT codigo_servi_undproce AS codigo, " +
																	"codigo_undproce AS codigoundproce, " +
																	"codigo_servicio AS codigoservicio, " +
																	"'(' || getcodigoespecialidad(codigo_servicio) || '-' || codigo_servicio || ') ' || getnombreservicio(codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion, " +
																	"tiempo_servi_undproce AS tiempo, " +
																	"'"+ConstantesBD.acronimoSi+"' AS estabd " +																	
																	"FROM agendaprocedimiento.servicios_undproce ";
	
	
	/**
	 * cadena de consulta Cuenta si existe elementos de detalle de servicio 
	 * */
	private static final String cadenaConsultarDetalleCount ="SELECT COUNT(codigo_undproce) AS cuenta FROM agendaprocedimiento.servi_detalle_undproce ";
															 
	
	/**
	 * cadena de consulta de Detalle de Servicios por Unidad de Procedimiento (servicios)	   
	 * */
	private static final String cadenaConsultarServicioDetServ= "SELECT sdu.codigo_undproce AS codigoundproce, " +
																"		sdu.codigo_servi_undproce AS codigoserviundproce, " +
																" 		sdu.codigo_servi_detalle AS codigo, " +													
																"		sdu.codigo_servicio AS codigoservicio, "  +
																"'(' || getcodigoespecialidad(sdu.codigo_servicio) || '-' || sdu.codigo_servicio || ') ' || getnombreservicio(sdu.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion, " +																
																"'"+ConstantesBD.acronimoSi+"' AS estabd " +
																"FROM agendaprocedimiento.servi_detalle_undproce sdu " +
																"INNER JOIN view_servicios va ON(va.servicio=sdu.codigo_servicio AND va.tipo_tarifario=0)  ";
	
	/**
	 * cadena de consulta de Detalle de Servicios por Unidad de Procedimiento (condiciones de toma de examen)	   
	 * */
	private static final String cadenaConsultarServicioDetExamen= 	"SELECT sdu.codigo_undproce AS codigoundproce, " +															
																 	"		sdu.codigo_servi_undproce AS codigoserviundproce, " +
																 	"		sdu.codigo_servi_detalle AS codigo, " +																 	
																 	"		sdu.codigo_examenct AS codigoexamen, " +
																 	"cte.descrip_examenct AS descripcion, " +
																 	"'"+ConstantesBD.acronimoSi+"' AS estabd " +																 	
																 	"FROM agendaprocedimiento.servi_detalle_undproce sdu " +
																 	"INNER JOIN facturacion.examen_conditoma cte ON (cte.codigo_examenct=sdu.codigo_examenct) ";
									 
	/**
	 * cadena de consulta de Detalle de Servicios por Unidad de Procedimiento (articulos)	   
	 * */
	private static final String cadenaConsultarServicioDetArticulo=	"SELECT sdu.codigo_undproce AS codigoundproce, " +															
																 	"		sdu.codigo_servi_undproce AS codigoserviundproce, " +
																	"		sdu.codigo_servi_detalle AS codigo, " +
																 	"		sdu.codigo_articulo AS codigoarticulo, " +
																 	"va.descripcion ||' CONC:'|| va.concentracion ||' F.F:'|| getNomFormaFarmaceutica(va.forma_farmaceutica)  || ' NAT:' || va.descripcionnaturaleza  || " +
																	"CASE WHEN va.es_pos= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN ' - POS' WHEN va.es_pos ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - NOPOS' ELSE '' END " +
																 	" AS descripcion, " +
																 	"'"+ConstantesBD.acronimoSi+"' AS estabd " +																 	
																 	"FROM agendaprocedimiento.servi_detalle_undproce sdu " +
																 	"INNER JOIN view_articulos va ON(va.codigo=sdu.codigo_articulo) ";
	
	
	
	/**
	 * cadena de Consulta de Detalle de Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaConsultarServicioDetUndProcStr =  "SELECT sdu.codigo_servi_detalle	AS codigo, " +
																		"sdu.codigo_servi_undproce	AS codigoserviundproce, " +
																		"CASE WHEN sdu.codigo_servicio IS NOT NULL THEN " +																		
																			"'(' || getcodigoespecialidad(sdu.codigo_servicio) || '-' || sdu.codigo_servicio || ') ' || getnombreservicio(sdu.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") " +
																		"ELSE " +
																			" '' " +
																		"END AS descripcionservicio, " +
																		" sdu.codigo_examenct AS codigoexamenct, " +
																		
																		"CASE WHEN sdu.codigo_examenct IS NOT NULL THEN " +																		
																			" cte.descrip_examenct " +
																		"ELSE " +
																			" '' " +																			
																		"END AS descripcionexamenct, " +																		
																		"sdu.codigo_articulo AS codigoarticulo,  " +																	
																		
																		"CASE WHEN sdu.codigo_articulo IS NOT NULL THEN " +
																			"va.descripcion ||' CONC:'|| va.concentracion ||' F.F:'|| getNomFormaFarmaceutica(va.forma_farmaceutica)  || ' NAT:' || va.descripcionnaturaleza  || " +
																			"CASE WHEN va.es_pos= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN ' - POS' WHEN va.es_pos ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - NOPOS' ELSE '' END " +
																		"ELSE  " +
																			"'' " +
																		"END AS descripcionarticulo, "+ 
																	    
																		"CASE WHEN sdu.codigo_articulo IS NOT NULL THEN " +
																			"getNomUnidadMedida(va.unidad_medida) " +
																		"ELSE " +
																			" '' " +
																		"END AS unidadmedidaarticulo, " +
																	    "'"+ConstantesBD.acronimoSi+"' AS estabd " +
																	    
																		"FROM agendaprocedimiento.servi_detalle_undproce sdu " +
																		"LEFT OUTER JOIN facturacion.examen_conditoma cte ON (cte.codigo_examenct=sdu.codigo_examenct) " +
																		"LEFT OUTER JOIN view_articulos va ON(va.codigo=sdu.codigo_articulo) "; 
																		
	/**
	 * cadena de Inserccion de Registros en Unidades de Procedimiento	    
	 * */
	private static final String cadenaInsertarUnidadProcStr = 	"INSERT INTO agendaprocedimiento.unidad_procedimiento( " +
																"		codigo_undproce," +
																"		institucion," +
																"		codigo_especiali," +
																"		descrip_undproce," +
																"		activo_undproce) " +
																"VALUES ";
	
	/**
	 * cadena de Inserccion de Registros en Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaInsertarServicioUndProcStr =	"INSERT INTO agendaprocedimiento.servicios_undproce(" +
																		"		codigo_servi_undproce, " +
																		"		codigo_undproce, " +
																		"		codigo_servicio, " +
																		"		tiempo_servi_undproce)" +
																		"VALUES ";
	
	
	/**
	 * cadena de Inserccion de Registros en Detalle de Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaInsertarServicioDetUndProcStr= "INSERT INTO agendaprocedimiento.servi_detalle_undproce(" +
																	 "		 codigo_undproce, " +
																	 "		 codigo_servi_undproce, " +
																	 "		 codigo_servi_detalle, " +																	 
																	 "		 codigo_servicio, " +
																	 "		 codigo_examenct, " +
																	 "		 codigo_articulo) " +
																	 "VALUES ";
	
	/**
	 * cadena modificacion de Registros en Unidades de Procedimiento 
	 * */
	private static final String cadenaModificaUnidadProcStr = 	"UPDATE agendaprocedimiento.unidad_procedimiento " +
																"SET codigo_especiali=?," +
																"  	 descrip_undproce=?," +
																"	 activo_undproce=? " +
																"WHERE codigo_undproce=? " +
																"AND   institucion=? ";
	
	/**
	 * cadena modificacion de Registros en Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaModificaServicioUndProcStr =	"UPDATE agendaprocedimiento.servicios_undproce " +
																	"SET codigo_servicio=?," +
																	"	 tiempo_servi_undproce=? " +																	
																	"WHERE codigo_servi_undproce=? " +
																	"AND   codigo_undproce=? "; 
	
	/**
	 * cadena modificacion de Registros en Detalle de Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaModificaServicioDetUndProcStr = 	"UPDATE agendaprocedimiento.servi_detalle_undproce " +
																		"SET codigo_servicio=?," +
																		"	 codigo_examenct=?," +
																		"	 codigo_articulo=? " +
																		"WHERE codigo_servi_detalle=? " +
																		"AND   codigo_servi_undproce=? " ;
																			
	
	/**
	 * cadena eliminacion de Registros en Unidades de Procedimiento
	 * */
	private static final String cadenaEliminarUnidadProcSrt=	"DELETE FROM agendaprocedimiento.unidad_procedimiento " +
																"WHERE codigo_undproce=? " +
																"AND   institucion=? ";
	
	/**
	 * cadena eliminacion de Registros en Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaElimarServiciosUndProcStr = "DELETE FROM agendaprocedimiento.servicios_undproce " +
																  "WHERE codigo_servi_undproce=? " +
																  "AND   codigo_undproce=? ";
	
	
	/**
	 * cadena eliminacion de Todos los Registros en Servicios por Unidad de Procedimiento
	 * */
	private static final String cadenaEliminarTodoServiciosUndProcStr = "DELETE FROM agendaprocedimiento.servicios_undproce " +
	  																 	"WHERE codigo_undproce=? ";
	
	/**
	 * cadena eliminacion de Registros en Detalle de Servicios por Unidad de Procedimiento	  
	 * */
	private static final String cadenaEliminarServiciosDetUndProcStr =  "DELETE FROM agendaprocedimiento.servi_detalle_undproce " +
																		"WHERE codigo_servi_detalle=? " +
																		"AND   codigo_servi_undproce=? ";
	
	/**
	 * cadena eliminacion de  Todos los Registros en Detalle de Servicios por Unidad de Procedimiento	  
	 * */
	private static final String cadenaEliminarTodoServiciosDetUndProcStr =  "DELETE FROM agendaprocedimiento.servi_detalle_undproce "; 
	
	
		
	/**
	 * indice de columnas de la tabla Unidad de Procedimiento 
	 * */
	private static final String [] indicesUndProcMapa = {"codigo_","institucion_","especialidad_","descripcion_","activo_","estabd_"};
	
	/**
	 * indice de columnas de la tabla Servicios por Unidad de Procedimiento
	 * */
	private static final String [] indicesServicioUndProcMapa = {"codigo_","codigoundproce_","codigoservicio_","descripcion_","hora_","minuto_","estabd_"};
	
	/**
	 * indice de columnas de la tabla Detalle de Servicios por Unidad de Procedimiento 
	 * */	
	private static final String [] indicesServicioDetUndProcMapa = {"codigo_","codigoserviundproce_","codigoservicio_","codigoexamenct_","codigoarticulo_","estabd_"};
	
	/**
	 * indices de columnas del Detalle de Servicios (Servicios)
	 * */
	private static final String [] indicesServicioDetServMapa = {"codigoundproce_","codigoserviundproce_","codigo_","codigoservicio_","descripcion_","estabd_"};
	
	/**
	 * indices de columnas del Detalle de Servicios (Condicion de Toma de Examen)
	 * */
	private static final String [] indicesServicioDetExamenMapa = {"codigoundproce_","codigoserviundproce_","codigo_","codigoexamen_","descripcion_","estabd_"};
	
	/**
	 * indices de columnas del Detalle de Servicios (Articulos)
	 * */
	private static final String [] indicesServicioDetArticuloMapa = {"codigoundproce_","codigoserviundproce_","codigo_","codigoarticulo_","descripcion_","estabd_"};
		
	//-- Fin Atributos
	
	//-- Metodos
	
	/**
	 * Consulta basica de Unidad de Procedimiento 
	 * @param Connection con 
	 * @param int codigo unidad de procedimiento 
	 * @param int institucion 
	 * */
	public static HashMap consultarUnidadProcBasica(Connection con, int codigoUnidadProc, int institucion)
	{
		String cadenaConsulta = cadenaConsultarUnidadProcStr;
		HashMap mapa = new HashMap();
		mapa.put("numRegistros",0);		
		
		try
		{
			if(codigoUnidadProc==ConstantesBD.codigoNuncaValido)
				cadenaConsulta+= " WHERE institucion=? ";
			else
				cadenaConsulta+=" WHERE codigo_undproce=? AND institucion=? ";
			
			cadenaConsulta+= " ORDER BY codigo_undproce DESC ";	
			
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(codigoUnidadProc == ConstantesBD.codigoNuncaValido)
				ps.setInt(1,institucion);
			else
			{
				ps.setInt(1,codigoUnidadProc);
				ps.setInt(2,institucion);
			}	
			
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA",indicesUndProcMapa);		
		return mapa;
	}
	
	
	/**
	 * Consulta basica de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiUndProce
	 * @param int codigoUndProce
	 * */
	public static HashMap consultarServicioUnidadProcBasica(Connection con, int codigoServiUndProce, int codigoUndProce)
	{
		String cadenaConsulta = cadenaConsultarServicioUndProcStr;
		HashMap mapa = new HashMap();
		mapa.put("numRegistros",0);
		
		if(codigoServiUndProce==ConstantesBD.codigoNuncaValido)
			cadenaConsulta+= "WHERE codigo_undproce="+codigoUndProce+" ";
		else
			cadenaConsulta+= "WHERE codigo_servi_undproce="+codigoServiUndProce+" AND codigo_undproce="+codigoUndProce+" ";
		
		cadenaConsulta+= " ORDER BY codigo_servi_undproce DESC ";		
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}	
		
		mapa.put("INDICES_MAPA",indicesServicioUndProcMapa);
		return mapa;
	}
	
	
	/**
	 * Consulta basica de Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * */	
	public static HashMap consultarServicioDetUnidadProcBasica(Connection con, int codigoServiDetalle, int codigoServiUndProce)
	{
		String cadenaConsulta = cadenaConsultarServicioDetUndProcStr;
		HashMap mapa = new HashMap();
		
		if(codigoServiDetalle==ConstantesBD.codigoNuncaValido)
			cadenaConsulta+= " AND codigo_servi_undproce="+codigoServiDetalle+" ";		
		else
			cadenaConsulta+= " AND  codigo_servi_detalle="+codigoServiUndProce+" AND codigo_servi_undproce="+codigoServiDetalle+" ";
		
		cadenaConsulta+=" ORDER BY codigo_servi_detalle DESC ";
		
		logger.warn("\n\n Valor de la cadena de Consulta del detalle "+cadenaConsulta+"\n\n");
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesServicioDetUndProcMapa);
		return mapa;			
	}
	
	
	/**
	 * Consulta basica de Detalle de Servicios por -> servicio, condicion de toma de examne, articulo 
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * @param String tipo de consulta
	 * */	
	public static HashMap consultarServicioDetalles(Connection con, int codigoServiDetalle, int codigoServiUndProce, String tipo)
	{
		String cadenaConsulta = "";
		HashMap mapa = new HashMap();
		String [] indices ={""};
		
		if(tipo.equals("servicio"))
		{
			cadenaConsulta = cadenaConsultarServicioDetServ;
			indices = indicesServicioDetServMapa;			
		}	
		else if(tipo.equals("examen"))
		{	
			cadenaConsulta = cadenaConsultarServicioDetExamen;
			indices = indicesServicioDetExamenMapa;
		}	
		else if(tipo.equals("articulo"))
		{
			cadenaConsulta = cadenaConsultarServicioDetArticulo;
			indices = indicesServicioDetArticuloMapa;			
		}	
		
		if(codigoServiDetalle==ConstantesBD.codigoNuncaValido)
			cadenaConsulta+= " AND codigo_servi_undproce="+codigoServiUndProce+" ";		
		else
			cadenaConsulta+= " AND  codigo_servi_detalle="+codigoServiDetalle+" AND codigo_servi_undproce="+codigoServiUndProce+" ";
		
		cadenaConsulta+=" ORDER BY codigo_servi_detalle DESC ";
		
		
		logger.warn("\n\n Valor de la cadena de Consulta del detalle "+cadenaConsulta+"\n\n");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indices);
		return mapa;			
	}
	
	/**
	 * Inserta un registro en Unidad Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public static boolean insertarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		String cadenaInsertar = cadenaInsertarUnidadProcStr;				
		
		try
		{
			cadenaInsertar+="("+unidadProcedimiento.getCadenaSecuenciaUndProceStr().trim()+",?,?,?,?) ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,unidadProcedimiento.getInstitucion());
			ps.setInt(2,unidadProcedimiento.getCodigoEspeciali());
			ps.setInt(3,Utilidades.convertirAEntero(unidadProcedimiento.getDescripUndProce()));
			ps.setString(4,unidadProcedimiento.getActivoUndProce());
			
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
	 * Inserta un registro en Servicios por Unidad Procedimiento 
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public static boolean insertarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		String cadenaInsertar = cadenaInsertarServicioUndProcStr;
		
		try
		{
			cadenaInsertar+="("+unidadProcedimiento.getCadenaSecuenciaServicioUndProceStr().trim()+",?,?,?) ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			
			ps.setInt(1,unidadProcedimiento.getCodigoUndProce());
			ps.setInt(2,unidadProcedimiento.getCodigoServicioUndProce());
			ps.setString(3,unidadProcedimiento.getTiempoServi());
			
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
	 * Insertar un registro en Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public static boolean insertarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		String cadenaInsertar = cadenaInsertarServicioDetUndProcStr;
		
		try
		{
			cadenaInsertar+=" (?,?,"+unidadProcedimiento.getCadenaSecuenciaDetServicioUndProceStr().trim()+",?,?,?) ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,unidadProcedimiento.getCodigoUndProce()); //codigo de la unidad de procedimiento
			ps.setInt(2,unidadProcedimiento.getCodigoConsecutivoServiUndproce()); //codigo del Servicio Padre por Unidad de Procedimiento
			
			if(unidadProcedimiento.getCodigoServicio()!=ConstantesBD.codigoNuncaValido)
				ps.setInt(3,unidadProcedimiento.getCodigoServicio()); //codigo del servicio
			else			
				ps.setNull(3, Types.INTEGER);
			
			if(unidadProcedimiento.getCodigoExamenct()!=ConstantesBD.codigoNuncaValido)
				ps.setInt(4,unidadProcedimiento.getCodigoExamenct()); //codigo del examen
			else
				ps.setNull(4, Types.INTEGER);
			
			if(unidadProcedimiento.getCodigoArticulo()!=ConstantesBD.codigoNuncaValido)
				ps.setInt(5,unidadProcedimiento.getCodigoArticulo()); //codigo del articulo
			else
				ps.setNull(5,Types.INTEGER);
				
			
			logger.warn("\n\n valor unidad procedimiento "+unidadProcedimiento.getCodigoUndProce()+"\n\n");
			logger.warn("\n\n valor consecutivo servio por unidad de procedimiento "+unidadProcedimiento.getCodigoConsecutivoServiUndproce()+"\n\n");			
			
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
	 * Modifica un registro de Unidad de Procedimiento
	 * @param Connecition con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public static boolean modificarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificaUnidadProcStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,unidadProcedimiento.getCodigoEspeciali());
			ps.setString(2,unidadProcedimiento.getDescripUndProce());
			ps.setString(3,unidadProcedimiento.getActivoUndProce());
			
			ps.setInt(4,unidadProcedimiento.getCodigoUndProce());
			ps.setInt(5,unidadProcedimiento.getInstitucion());						
			
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
	 * Modifica un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public static boolean modificarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificaServicioUndProcStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,unidadProcedimiento.getCodigoServicioUndProce());
			ps.setString(2,unidadProcedimiento.getTiempoServi());
			
			ps.setInt(3,unidadProcedimiento.getCodigoConsecutivoServiUndproce());
			ps.setInt(4,unidadProcedimiento.getCodigoUndProce());
			
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
	 * Modifica un registro del Detalle del Servico por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public static boolean modificarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificaUnidadProcStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,unidadProcedimiento.getCodigoServicio());
			ps.setInt(2,unidadProcedimiento.getCodigoExamenct());
			ps.setInt(3,unidadProcedimiento.getCodigoArticulo());
			
			ps.setInt(4,unidadProcedimiento.getCodigoConsecutivoServiDetalle());
			ps.setInt(5, unidadProcedimiento.getCodigoConsecutivoServiUndproce());
			
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
	 * Elimina un registro de Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @param int institucion
	 * */
	public static boolean eliminarUnidadProcedimiento(Connection con, int codigoUndProce, int institucion)	
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarUnidadProcSrt,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoUndProce);
			ps.setInt(2,institucion);
			
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
	 * Elimina un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoConsecutivoServiUndproce
	 * @param int CodigoUndProce
	 * */
	public static boolean eliminarServiciosUnidadProcedimiento(Connection con, int codigoConsecutivoServiUndProce, int codigoUndProce)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaElimarServiciosUndProcStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoConsecutivoServiUndProce);
			ps.setInt(2,codigoUndProce);			
			
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
	 * Eliminar un registro del Detalle de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param codigoConsecutivoServiDetalle
	 * @param codigoConsecutivoServiUndProce 
	 * */
	public static boolean eliminarServiciosDetUnidadProcedimiento(Connection con, int codigoConsecutivoServiDetalle, int codigoConsecutivoServiUndProce)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarServiciosDetUndProcStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoConsecutivoServiDetalle);
			ps.setInt(2,codigoConsecutivoServiUndProce);
			
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
	 * Elimina Todos los registros de un Servicio por Unidad de Procedimiento asociado a un codigo de Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoUndProce 
	 * */
	public static boolean eliminarServiciosTodoUndProc(Connection con, int codigoUndProce)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarTodoServiciosUndProcStr,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ps.setInt(1,codigoUndProce);			
			
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
	 * Elimina Todo el Detalle de un Servicio por Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoConsecutivoServiUndProce
	 * */
	public static boolean eliminarServiciosDetTodoUndProc(Connection con, int codigoUndProce, int codigoServiUndProce, int codigoServiDetalle)
	{
		String condicion = " WHERE codigo_undproce=? ";		
		
		if(codigoServiUndProce!=ConstantesBD.codigoNuncaValido)
			condicion+=" AND codigo_servi_undproce="+codigoServiUndProce+" ";
		
		if(codigoServiDetalle!=ConstantesBD.codigoNuncaValido)
			condicion+=" AND codigo_servi_detalle="+codigoServiDetalle+" ";
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarTodoServiciosDetUndProcStr+" "+condicion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoUndProce);		
			
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
	 * Retorna cuantos servicios se encuentra dentro de una unidad de procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @return int numero de registros 
	 * */
	public static int consultaServicioUndProcCuantos(Connection con, int codigoUndProce)
	{
		try
		{
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultarServicioUndProcCount,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoUndProce);			
			rs =new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getInt("cuenta");			
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		
		return 0;
	}
	
	/**
	 * Retorna cuantos detalles (servicios, condiciones de toma de examen, articulos) existen en un servicio por unidad de procedimiento
	 * @param Connection con
	 * @param int codifgoUndProce
	 * @param int codigoServiUndProce
	 * @return int numero de registros 
	 * */
	public static int consultaDetalleCuantos(Connection con, int codigoUndProce, int codigoServiUndProce)
	{
		String condicion = "WHERE codigo_undproce=? ";
		
		if(codigoServiUndProce!=ConstantesBD.codigoNuncaValido)
			condicion+="AND codigo_servi_undproce="+codigoServiUndProce+" ";
		
		try
		{
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultarDetalleCount+" "+condicion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoUndProce);						
			rs =new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getInt("cuenta");			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return 0;
	}	
}