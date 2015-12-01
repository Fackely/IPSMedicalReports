/*
 * Creado en 18/08/2004
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoAreaAperturaCuentaAutoPYP;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
public class SqlBaseValoresPorDefectoDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseValoresPorDefectoDao.class);
	
	/**
	 * Modoficar el valor del parametro por defecto
	 */
	private static String modificarValorStr="UPDATE administracion.valores_por_defecto SET nombre =?, valor=? where parametro=? AND institucion=?";

	/**
	 * Sentencia SQL para ingresaras
	 */
	private static String insertarValorStr="INSERT INTO administracion.valores_por_defecto (nombre, valor, parametro, institucion) VALUES (?, ?, ?, ?)";

	/**
	 * Cadena para cargar todos los valores por defecto
	 */
	private static String cargarStr="SELECT parametro AS parametro, valor AS valor, nombre AS nombre, institucion AS institucion FROM administracion.valores_por_defecto WHERE institucion=?";
	
	/**
	 * Cadena para cargar el cï¿½digo del tipo de cie actual
	 */
	private static String cargarCodigoTipoCieActualStr="SELECT codigo from manejopaciente.tipos_cie where vigencia<=CURRENT_DATE order by vigencia desc";
	
	/**
	 * Sentencia SQL para listar los cï¿½digos de la instituciones presentes en el sistema
	 */
	private static String listarInstitucionesStr="SELECT codigo AS codigo FROM administracion.instituciones";
	
	/**
	 * Sentencia SQL para verificar la existencia de un parï¿½metro general
	 */
	private static String consultarExistenciaParametroGeneralStr="SELECT count(1) AS numResultados FROM administracion.valores_por_defecto WHERE parametro=? AND institucion=?";
	/**
	 * query para consultar las etiquetas de los parametros
	 */
	private static String consultaEtiquetasValoresXDefectoStr="SELECT parametro,etiqueta,modulo from administracion.valores_por_defecto_modulos where modulo=? ORDER BY etiqueta";
	/**
	 * query para generar el resumen
	 */
	private static String resumenValoresPorDefectoStr="SELECT vpd.valor as valor," +
															 "vpd.nombre as nombre," +
															 "vpdm.etiqueta as etiqueta," +
															 "vpdm.modulo as modulo, " +
															 "vpdm.parametro as parametro " +
															 "from administracion.valores_por_defecto vpd " +
															 "inner join administracion.valores_por_defecto_modulos vpdm on(vpdm.parametro=vpd.parametro) " +
															 "where vpd.institucion=?  AND vpdm.modulo=? ORDER BY vpdm.etiqueta";
	
	/**
	 */
	private static String consultarCentrosCostoTercerosStr= "SELECT v.centro_costo AS centrocosto, administracion.getnomcentrocosto(v.centro_costo) ||' - '|| administracion.getnomcentroatencion(c.centro_atencion) AS nombrecentrocosto, v.institucion, 'si' as estabd, 'no' as eseliminada FROM manejopaciente.val_defecto_centro_costo_ter v inner join administracion.centros_costo c on (c.codigo=v.centro_costo) order by nombrecentrocosto";
	
	/**
	 */
	private static String consultarHorasReprocesoStr= "SELECT via_ingreso AS codigoviaingreso, manejopaciente.getnombreviaingreso(via_ingreso) AS nombreviaingreso, hora AS hora, institucion AS institucion, 'si' as estabd, 'no' as eseliminadahora FROM administracion.horas_reproceso order by nombreviaingreso";
	
	/**
	 * 
	 */
	private static String insertarCentroCostoTercerosStr = "INSERT INTO manejopaciente.val_defecto_centro_costo_ter (centro_costo, institucion) VALUES(?, ?) ";
	
	/**
	 * 
	 */
	private static String insertarHorasReprocesoStr="INSERT INTO administracion.horas_reproceso (via_ingreso, hora,institucion) VALUES(?, ?, ?) ";
	
	/**
	 * 
	 */
	private static String eliminarCentroCostoTercerosStr = "DELETE FROM manejopaciente.val_defecto_centro_costo_ter WHERE centro_costo= ? and institucion=?";
	
	/**
	 * 
	 */
	private static String eliminarHorasReprocesoStr="DELETE FROM administracion.horas_reproceso WHERE via_ingreso= ? and institucion=?";
	
	/**
	 * Cadena encargada de consultar si hay una factura varia
	 * Soluciï¿½n Tarea 2366
	 * @author Felipe Pï¿½rez Granda
	 * "Se debe tener en cuenta que se permite la modificaciï¿½n del parï¿½metro siempre y cuando 
	 * no se hayan generado Facturas Varias, de lo contrario este campo se debe mostrar inactivo."
	 */
	private static String consultarSiExisteFacturaVaria = "SELECT * FROM facturasvarias.facturas_varias";
	
	/**
	 * Cadena encargada de consultar las N Clases de Inventarios para Paquetes Materiales Quirï¿½rgicos
	 */
	private static String consultarClasesInventariosPaqMatQxStr = "SELECT " +
																		"vdci.codigo_clase AS codigoclase, " +
																		"ci.nombre AS nombreclaseinventario, " +
																		"vdci.institucion AS institucion, " +
																		"'"+ConstantesBD.acronimoSi+"' AS estabd, " +
																		"'"+ConstantesBD.acronimoNo+"' AS eseliminada " +
																  "FROM " +
																  		"salascirugia.val_defecto_clase_inv vdci " +
																  		"INNER JOIN inventarios.clase_inventario ci ON (vdci.codigo_clase = ci.codigo) " +
																  "ORDER BY " +
																  		"nombreclaseinventario ";
	
	/**
	 * Cadena encargada de eliminar las N Clases de Inventarios para Paquetes Materiales Quirï¿½rgicos
	 */
	private static String eliminarClasesInventariosPaqMatQxStr = "DELETE FROM salascirugia.val_defecto_clase_inv WHERE codigo_clase = ? AND institucion = ?";
	
	/**
	 * Cadena encargada de insertar las N Clases de Inventarios para Paquetes Materiales Quirï¿½rgicos
	 */
	private static String insertarClasesInventariosPaqMatQxStr = "INSERT INTO " +
																	"salascirugia.val_defecto_clase_inv " +
																	"(" +
																		"codigo_clase, " +
																		"institucion, " +
																		"fecha_modifica, " +
																		"hora_modifica, " +
																		"usuario_modifica" +
																	") " +
																	"VALUES (?, ?, ?, ?, ?) ";
	
	//Anexo 922
	private static String insertarFirmasValoresPorDefecto	=	"INSERT INTO " +
																	"administracion.firmas_valores_por_defecto " +
																"(" +
																	"codigo_pk," +
																	"valor_por_defecto," +
																	"usuario," +
																	"cargo," +
																	"institucion," +
																	"tipo," +
																	"firma_digital," +
																	"usuario_modifica," +
																	"hora_modifica," +
																	"fecha_modifica" +
																") " +
																"VALUES " +
																"(" +
																	"?,?,?,?,?,?,?,?,?,?" +
																")";
	
	private static String consultarFirmasValoresPorDefecto	=	"SELECT " +
																	"codigo_pk," +
																	"valor_por_defecto," +
																	"usuario," +
																	"cargo," +
																	"institucion," +
																	"tipo," +
																	"firma_digital," +
																	"usuario_modifica," +
																	"hora_modifica," +
																	"fecha_modifica " +
																"FROM " +
																	"administracion.firmas_valores_por_defecto " +
																"WHERE " +
																	"valor_por_defecto=? AND institucion=? " +
																"ORDER BY codigo_pk ASC";
	
	private static String borrarFirmasValoresPorDefecto		=	"DELETE FROM " +
																	"administracion.firmas_valores_por_defecto " +
																"WHERE " +
																	"valor_por_defecto=? and institucion=? and codigo_pk=?";
	
	private static String eliminarAreaAperturaCuentaAutoPYP = "DELETE FROM " +
																	"pyp.area_aper_cuen_auto_pyp " +
																"WHERE " +
																	"institucion=? AND centro_atencion=? AND centro_costo=? ";
	
	private static String consultarAreasAperturaCuentaAutoPYP = "SELECT " +
																	"centro_atencion, " +
																	"centro_costo, " +
																	"institucion," +
																	"getnombrecentroscosto(centro_costo) as nombre_cc, " +
																	"getnomcentroatencion(centro_atencion) as nombre_ca " +
																"FROM " +
																	"pyp.area_aper_cuen_auto_pyp " +
																"WHERE " +
																	"institucion= ? ";
	
	private static String guardarAreaAperturaCuentaAutoPYP = "INSERT INTO pyp.area_aper_cuen_auto_pyp VALUES (?,?,?)";
				
	
	
	
	//Fin anexo 922
	
	/**
	 * Mï¿½todo para modificar un parametro de valores por defecto
	 * @param con
	 * @param parametro
	 * @param nombre
	 * @param valor
	 * @return true si se modifico correctamente, false de lo contrario
	 */
	public static boolean modificar(Connection con, String parametro, String nombre, String valor, int institucion)
	{
		try
		{
			PreparedStatementDecorator modificar= new PreparedStatementDecorator(con , consultarExistenciaParametroGeneralStr);
			modificar.setString(1, parametro);
			modificar.setInt(2, institucion);
			Log4JManager.info("consulta parmetros:"+modificar);
			ResultSetDecorator verificacion=new ResultSetDecorator(modificar.executeQuery());
			if(verificacion.next())
			{
				if(verificacion.getInt("numResultados")==1)
				{
					modificar= new PreparedStatementDecorator(con, modificarValorStr);
					//Log4JManager.info("Modificar->"+modificar);
				}
				else
				{
					modificar= new PreparedStatementDecorator(con,insertarValorStr);
					//Log4JManager.info("Insertar ->"+modificar);
				}
				if(valor.equals(ConstantesBD.codigoNuncaValido+""))//en caso de select no guardar -1@@seleccione
				{
				    nombre="";valor="";
				}
				modificar.setString(1, nombre==null||nombre.equals("")?" ":nombre);
				modificar.setString(2, valor==null||valor.equals("")?" ":valor);
				modificar.setString(3, parametro);
				modificar.setInt(4, institucion);
				
				Log4JManager.info("Modificar->"+modificar);
				
				if(modificar.executeUpdate()>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("error modificando el parametro "+parametro+": "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para cargar todos los valores por defecto
	 * @param con
	 * @param int, cï¿½digo del modulo
	 * @return Collection con todos los valores por defecto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection cargar(Connection con)
	{
		Collection parametros=new ArrayList();
		PreparedStatementDecorator ps1 = null;
		ResultSetDecorator rs1 = null;
		PreparedStatementDecorator ps2 = null;
		ResultSetDecorator rs2 = null;
		try
		{
			ps1 =  new PreparedStatementDecorator(con.prepareStatement(listarInstitucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs1 = new ResultSetDecorator(ps1.executeQuery());
			while(rs1.next())
			{
				int codigoInstitucion = rs1.getInt("codigo");
				ps2 =  new PreparedStatementDecorator(con.prepareStatement(cargarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("CONSULTA-->"+cargarStr);
				ps2.setInt(1, codigoInstitucion);	
				rs2 = new ResultSetDecorator(ps2.executeQuery());
				parametros.addAll(UtilidadBD.resultSet2Collection(rs2));
				
				ps2.close();
				rs2.close();
			}
			ps1.close();
			rs2.close();
			
			return parametros;
		}
		catch (SQLException e)
		{
			logger.error("error cargando los valores por defecto "+e);
			return null;
		}
	}
	/**
	 * Metodo para cargar las etiquetas de
	 * parametros generales
	 * @param con Connection
	 * @param modulo int
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap cargarEtiquetas(Connection con,int modulo)
	{
		HashMap mapa= new HashMap();
	    try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaEtiquetasValoresXDefectoStr);
			ps.setInt(1, modulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error cargarEtiquetas "+e);
			return null;
		}
		return mapa;
	}
	
	/**
	 * Implementaciï¿½n del mï¿½todo que que carga el cï¿½digo 
	 * cie vï¿½lido para una BD Genï¿½rica
	 *
	 * @see com.princetonsa.dao.ValoresPorDefectoDao#cargarCodigoTipoCieActual (Connection )
	 */
	public static int cargarCodigoTipoCieActual (Connection con)
	{
	    try
	    {
		    PreparedStatementDecorator cargarCodigoTipoCieActualStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoTipoCieActualStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ResultSetDecorator rs=new ResultSetDecorator(cargarCodigoTipoCieActualStatement.executeQuery());
		    if (rs.next())
		    {
		        int resp=rs.getInt("codigo");
		        rs.close();
		        cargarCodigoTipoCieActualStatement.close();
		        return resp;
		    }
		    else
		    {
		    	rs.close();
		        cargarCodigoTipoCieActualStatement.close();
				logger.error("error cargando el ï¿½ltimo CIE, no se encontrï¿½ ninguno en el sistema");
				return ConstantesBD.codigoNuncaValido;
		    }
	    }
	    catch (SQLException e)
	    {
	        logger.error("error cargando los valores por defecto "+e);
	        return ConstantesBD.codigoNuncaValido;
	    }
	    
	}
	/**
	 * metodo para generar el resumen de parametros
	 * @param con Connection
	 * @param modulo int
	 * @param institucion int 
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap cargarResumenValores(Connection con,int modulo,int institucion)
	{
		HashMap mapa = new HashMap();
	    try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(resumenValoresPorDefectoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);			
			ps.setInt(2, modulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error cargarEtiquetas "+e);
			return null;
		}	
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap cargarIntegridadDominio(Connection con)
	{
		HashMap mapa=new HashMap();
	    try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement("SELECT acronimo,descripcion from administracion.integridad_dominio",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				mapa.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error cargarIntegridadDominio "+e);
		}	
		return mapa;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap consultarCentrosCostoTerceros(Connection con)
	{
		HashMap mapa= new HashMap();
	    try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoTercerosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error consultarCentrosCostoTerceros "+e);
			return null;
		}	
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarCentrosCostoTercero(Connection con, HashMap mapa)
	{
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{	
			/*primero se elimina y luego se inserta*/
			if(mapa.get("estabd_"+w).toString().equals("si") && mapa.get("eseliminada_"+w).toString().equals("si"))
			{
				try
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarCentroCostoTercerosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, mapa.get("centrocosto_"+w).toString());
					ps.setString(2, mapa.get("institucion_"+w).toString());
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
				}
			}
			if(mapa.get("estabd_"+w).toString().equals("no") && mapa.get("eseliminada_"+w).toString().equals("no"))
			{	
				try
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarCentroCostoTercerosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, mapa.get("centrocosto_"+w).toString());
					ps.setString(2, mapa.get("institucion_"+w).toString());
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
				}
			}
		}	
		return true;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap consultarHorasReproceso(Connection con)
	{
		HashMap mapa= new HashMap();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarHorasReprocesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error consultarHorasReproceso "+e);
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarHorasReproceso(Connection con, HashMap mapa)
	{
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{	
			/*primero se elimina y luego se inserta*/
			if(mapa.get("estabd_"+w).toString().equals("si") && mapa.get("eseliminadahora_"+w).toString().equals("si"))
			{
				try
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarHorasReprocesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, mapa.get("codigoviaingreso_"+w).toString());
					ps.setString(2, mapa.get("institucion_"+w).toString());
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
				}
			}
			if(mapa.get("estabd_"+w).toString().equals("no") && mapa.get("eseliminadahora_"+w).toString().equals("no"))
			{	
				try
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHorasReprocesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, mapa.get("codigoviaingreso_"+w).toString());
					ps.setString(2, mapa.get("hora_"+w).toString());
					ps.setString(3, mapa.get("institucion_"+w).toString());
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
				}
			}
		}	
		return true;
	}
	
	/**
	 * Mï¿½todo encargado de ejecutar al consulta consultarSiExisteFacturaVaria
	 * y retorna el value object si hay registros en la tabla: facturas_varias
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param tipoBD 
	 * @return HashMap Cargar Value Object
	 */
	public static HashMap consultarFacturasVarias(Connection con, int tipoBD)
	{
		HashMap mapa= new HashMap();
		try
		{
			String consulta = consultarSiExisteFacturaVaria;
			
			switch(tipoBD)
			{
				case DaoFactory.ORACLE:
					consulta += " WHERE rownum = 1";
				break;
				case DaoFactory.POSTGRESQL:
					consulta += " "+ValoresPorDefecto.getValorLimit1()+" 1";
				break;
			}
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("error consultarSiExisteFacturaVaria "+e);
			return null;
		}
		return mapa;
	}

	/**
	 * Mï¿½todo que consulta las N Clases de Inventarios
	 * de Paquetes Materiales Quirï¿½rgicos parametrizadas
	 * @param con
	 * @return
	 */
	public static HashMap consultarClasesInventariosPaqMatQx(Connection con)
	{
		HashMap mapa= new HashMap();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarClasesInventariosPaqMatQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>Consulta: "+consultarClasesInventariosPaqMatQxStr);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultarClasesInventariosPaqMatQx: "+e);
			return null;
		}
		return mapa;
	}

	/**
	 * Mï¿½todo que inserta las N Clases de Inventarios
	 * de Paquetes Materiales Quirï¿½rgicos parametrizadas
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarClasesInventariosPaqMatQx(Connection con, HashMap mapa)
	{
		for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); w++)
		{	
			/*Primero se elimina y luego se Inserta*/
			if((mapa.get("estabd_"+w)+"").equals(ConstantesBD.acronimoSi) && (mapa.get("eseliminada_"+w)+"").equals(ConstantesBD.acronimoSi))
			{
				try
				{
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarClasesInventariosPaqMatQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codigoclase_"+w)+""));
					ps.setInt(2, Utilidades.convertirAEntero(mapa.get("institucion_"+w)+""));
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn("Error en el Eliminar Clases de Inventarios para Paquetes Mat. Qx. "+e.toString());
				}
			}
			if((mapa.get("estabd_"+w)+"").equals(ConstantesBD.acronimoNo) && (mapa.get("eseliminada_"+w)+"").equals(ConstantesBD.acronimoNo))
			{	
				try
				{
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarClasesInventariosPaqMatQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codigoclase_"+w)+""));
					ps.setInt(2, Utilidades.convertirAEntero(mapa.get("institucion_"+w)+""));
					ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechamodifica_"+w)+"")));
					ps.setString(4, mapa.get("horamodifica_"+w)+"");
					ps.setString(5, mapa.get("usuariomodifica_"+w)+"");
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) 
				{
					logger.warn("Error en el Insertar Clases de Inventarios para Paquetes Mat. Qx. "+e.toString());
				}
			}
		}	
		return true;
	}
	
	/**
	 * Mï¿½todo encargado de consultar si existe el datos seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param codSolicitudOrden
	 * @param esOrdenAmbulatoria
	 * @return resultado HashMap
	 */
	public static HashMap<String,Object> utilizaMedicamentosTratamientoPaciente(Connection con, String numSolicitudOrden, String codArticulo, boolean esOrdenAmbulatoria)
	{
		logger.info("===> numSolicitudOrden = *"+numSolicitudOrden+"*");
		String consulta = "";
		HashMap resultados = new HashMap();
		
		PreparedStatementDecorator ps = null;
		try
		{
			consulta = "SELECT * FROM justificacion_art_param where justificacion_art_sol = ";
			if(esOrdenAmbulatoria)
				consulta += "(SELECT codigo FROM justificacion_art_sol WHERE orden_ambulatoria = "+numSolicitudOrden+" AND articulo = "+codArticulo+")";
			else
				consulta += "(SELECT codigo FROM justificacion_art_sol WHERE numero_solicitud = "+numSolicitudOrden+" AND articulo = "+codArticulo+")";
			
			logger.info("===> La consulta es: "+consulta);
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		
		}catch (SQLException e){
			logger.error("ERROR SQLException utilizaMedicamentosTratamientoPaciente: ", e);
			
		}catch(Exception ex){
			logger.error("ERROR Exception utilizaMedicamentosTratamientoPaciente: ", ex);
			
		}finally{
			try{
				if(ps != null){
					ps.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
				}
		}
		
		return resultados;
	}
	
	
	
	//Anexo 922
	/**
	 * 
	 */
	public static ResultadoBoolean insertarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		ResultadoBoolean resultado=new ResultadoBoolean(false);
		
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			double secuenciaFirmas=UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_firmasvxd");
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, insertarFirmasValoresPorDefecto);
			
			psd.setDouble(1, secuenciaFirmas);
			psd.setString(2, dto.getValorPorDefecto());
			psd.setString(3, dto.getUsuario());
			psd.setString(4, dto.getCargo());
			psd.setInt(5, dto.getInstitucion());
			psd.setString(6, dto.getTipo());
			psd.setString(7, dto.getFirmaDigital());
			psd.setString(8, dto.getUsuarioModifica());
			psd.setString(9, dto.getHoraModifica());
			psd.setString(10, dto.getFechaModifica());
			
			if(psd.executeUpdate()>0)
			{
				resultado.setResultado(true);
				resultado.setDescripcion("Firma insertada correctamente!");
			}
			else
			{
				logger.info("no fue exitosa la inserción!!");
				resultado.setResultado(false);
				resultado.setDescripcion("");
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarFirmasValoresPorDefecto==> "+e);
		}
		return resultado;
	}
	
	public static ArrayList<DtoFirmasValoresPorDefecto> consultarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas = new ArrayList<DtoFirmasValoresPorDefecto>();
		
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultarFirmasValoresPorDefecto);
			psd.setString(1, dto.getValorPorDefecto());
			psd.setInt(2, dto.getInstitucion());
			
			logger.info("CONSULTA DE LAS FIRMAS------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoFirmasValoresPorDefecto dtoFirma=new DtoFirmasValoresPorDefecto();
				dtoFirma.setCodigoPk(rs.getDouble("codigo_pk"));
				dtoFirma.setValorPorDefecto(rs.getString("valor_por_defecto"));
				dtoFirma.setUsuario(rs.getString("usuario"));
				dtoFirma.setCargo(rs.getString("cargo"));
				dtoFirma.setInstitucion(rs.getInt("institucion"));
				dtoFirma.setTipo(rs.getString("tipo"));
				dtoFirma.setFirmaDigital(rs.getString("firma_digital"));
				dtoFirma.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoFirma.setHoraModifica(rs.getString("hora_modifica"));
				dtoFirma.setFechaModifica(rs.getString("fecha_modifica"));
				
				listadoFirmas.add(dtoFirma);
				
			}
			UtilidadBD.cerrarObjetosPersistencia(psd,rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarFirmasValoresPorDefecto==> "+e);
		}
		return listadoFirmas;
	}
	
	public static ResultadoBoolean eliminarFirma(DtoFirmasValoresPorDefecto dto)
	{
		ResultadoBoolean resultado= new ResultadoBoolean(false);
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, borrarFirmasValoresPorDefecto);
			psd.setString(1, dto.getValorPorDefecto());
			psd.setInt(2, dto.getInstitucion());
			psd.setDouble(3, dto.getCodigoPk());
			
			if(psd.executeUpdate()>0)
			{
				resultado.setResultado(true);
				resultado.setDescripcion("Firma eliminada correctamente!");
			}
			else
			{
				resultado.setResultado(false);
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarFirma==> "+e);
		}
		
		
		return resultado;
	}
	
	//Fin Anexo 922
	
	public static boolean existeAgendaOdon()
	{
		boolean existe=false;
		try
		{
			String consulta="SELECT * FROM odontologia.agenda_odontologica";
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
				existe=true;
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN existeAgendaOdon==> "+e);
		}
		return existe;
	}
	
	public static boolean existePresupesto()
	{
		boolean existe=false;
		try
		{
			String consulta="SELECT *FROM odontologia.presupuesto_odontologico";
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
				existe=true;
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN existePresupesto==> "+e);
		}
		return existe;
		
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean guardarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto) {
		boolean resultado=false;
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(guardarAreaAperturaCuentaAutoPYP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			psd.setInt(1, dto.getCentroAtencion());
			psd.setInt(2, dto.getArea());
			psd.setInt(3, dto.getInstitucion());
			
			if(psd.executeUpdate()>0)
				resultado = true;
				
			psd.close();
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarFirmasValoresPorDefecto==> "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto) {
		boolean resultado=false;
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(eliminarAreaAperturaCuentaAutoPYP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, dto.getInstitucion());
			psd.setInt(2, dto.getCentroAtencion());
			psd.setInt(3, dto.getArea());
			
			if(psd.executeUpdate()>0)
				resultado=true;
			
			psd.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param centroAtencion 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoAreaAperturaCuentaAutoPYP> consultarAreasAperturaCuentaAutoPYP(int institucion, int centroAtencion)
	{
		ArrayList<DtoAreaAperturaCuentaAutoPYP> listadoAreas = new ArrayList<DtoAreaAperturaCuentaAutoPYP>();
		logger.info("Usuario: "+institucion);
		try 
		{
			Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			String sql = consultarAreasAperturaCuentaAutoPYP;
			
			if(centroAtencion != ConstantesBD.codigoNuncaValido)
				sql += " AND centro_atencion="+centroAtencion;
			
			logger.info("SQL / consultarAreasAperturaCuentaAutoPYP / "+sql);
			
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoAreaAperturaCuentaAutoPYP dto=new DtoAreaAperturaCuentaAutoPYP();
				dto.setArea(rs.getInt("centro_costo"));
				dto.setCentroAtencion(rs.getInt("centro_atencion"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setNombreArea(rs.getString("nombre_cc"));
				dto.setNombreCentroAtencion(rs.getString("nombre_ca"));
				listadoAreas.add(dto);
			}
			psd.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
		}
		return listadoAreas;
	}

	/**
	 * 
	 * @param institucion
	 * @param con
	 * @return
	 */
	public static ArrayList<Integer> consultarServiciosManejoTransPrimario(int institucion, Connection con) 
	{
		ArrayList<Integer> servicios = new ArrayList<Integer>();
		try 
		{
			String sql = "select servicio from manejopaciente.servi_manejo_trans_primario where institucion="+institucion;
			
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				servicios.add(rs.getInt(1));
			}
			psd.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
		}
		return servicios;
	}

	/**
	 * 
	 * @param institucion
	 * @param con
	 * @return
	 */
	public static ArrayList<Integer> consultarServiciosManejoTransSecundario(int institucion, Connection con) 
	{
		ArrayList<Integer> servicios = new ArrayList<Integer>();
		try 
		{
			String sql = "select servicio from manejopaciente.servi_manejo_trans_secundario where institucion="+institucion;
			
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				servicios.add(rs.getInt(1));
			}
			psd.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
		}
		return servicios;
	}

	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public static boolean insertarServiciosManejoTransPrimario(Connection con,ArrayList<Integer> servicios,int institucion) 
	{
		try 
		{
			String sql = "delete from manejopaciente.servi_manejo_trans_primario ";
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.executeUpdate();
			
			for(int i=0;i<servicios.size();i++)
			{
				sql = "insert into manejopaciente.servi_manejo_trans_primario(servicio,institucion) values("+servicios.get(i)+","+institucion+") ";
				psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psd.executeUpdate();
			}
			
			psd.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public static boolean insertarServiciosManejoTransSecundario(Connection con, ArrayList<Integer> servicios,int institucion) 
	{
		try 
		{
			String sql = "delete from manejopaciente.servi_manejo_trans_secundario ";
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.executeUpdate();
			
			for(int i=0;i<servicios.size();i++)
			{
				sql = "insert into manejopaciente.servi_manejo_trans_secundario(servicio,institucion) values("+servicios.get(i)+","+institucion+") ";
				psd= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psd.executeUpdate();
			}
			
			psd.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR",e);
			return false;
		}
		return true;
	}
	
}