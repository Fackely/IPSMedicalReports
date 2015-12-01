/*
 * Ago 09, 2006
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Metas PYP
 */
public class SqlBaseMetasPYPDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseMetasPYPDao.class);
	
	/**
	 * Cadena que consulta las actividades de un programa,convenio y año específico
	 */
	private static final String consultarActividadesStr = "SELECT "+ 
		"mp.consecutivo AS consecutivo, "+
		"mp.anio AS anio, "+
		"mp.prog_act_conv AS codigo_papc, "+
		"mp.numero_actividades AS numero_actividades, "+
		"mp.institucion AS institucion, "+
		"papc.convenio AS codigo_convenio, "+
		"getnombreconvenio(papc.convenio) AS nombre_convenio, "+
		"papc.actividad_programa_pyp AS codigo_app, "+
		"app.programa AS codigo_programa, "+
		"psp.descripcion AS nombre_programa, "+
		"CASE WHEN ap.articulo IS NULL THEN ap.servicio ELSE ap.articulo END AS codigo_actividad, "+ 
		"CASE WHEN ap.articulo IS NULL THEN " +
			"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || " +
			"getcodigopropservicio2(ap.servicio,"+ConstantesBD.codigoTarifarioCups+")  || ' '  || " +
			"getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' ' || getnivelservicio(ap.servicio) " +
		"ELSE " +
			"getdescarticulo(ap.articulo) " +
		"END AS descripcion_actividad "+ 
		"FROM metas_pyp mp "+ 
		"INNER JOIN prog_act_pyp_convenio papc ON(papc.consecutivo=mp.prog_act_conv) "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"INNER JOIN programas_salud_pyp psp ON(psp.codigo = app.programa AND psp.institucion= app.institucion) "; 
	
	/**
	 * Cadena que consulta los centros de atención de una actividad, programa, convenio y año específico 
	 */
	private static final String consultarCentrosAtencionStr = "SELECT "+
		"mcp.consecutivo AS consecutivo, "+
		"mcp.centro_atencion AS codigo_centro_atencion, "+
		"ca.descripcion AS nombre_centro_atencion, "+
		"mcp.numero_actividades As numero_actividades," +
		"CASE WHEN getEsUsadoCAMetaPyp(mcp.consecutivo) > 0 THEN 'true' ELSE 'false' END AS es_usado "+ 
		"FROM metas_ca_pyp mcp "+ 
		"INNER JOIN centro_atencion ca ON(ca.consecutivo=mcp.centro_atencion) "; 
	
	/**
	 * Cadena que consulta las ocupaciones médicas de un centro de atencion, actividad, programa, convenio y año específico
	 */
	private static final String consultarOcupacionesStr = "SELECT "+ 
		"mop.consecutivo AS consecutivo, "+
		"mop.ocupacion_medica AS codigo_ocupacion_medica, "+
		"getocupacion(mop.ocupacion_medica) As nombre_ocupacion_medica, "+
		"mop.numero_actividades AS numero_actividades "+ 
		"FROM metas_ocupacion_pyp mop "; 
	
	/**
	 * Cadena que inserta una actividad, programa, convenio y año específico
	 */
	private static final String insertarActividadesStr = "INSERT INTO metas_pyp " +
		"(consecutivo,anio,prog_act_conv,numero_actividades,institucion) ";
	
	/**
	 * Cadena que inserta un centro de atencion, actividad, programa, convenio y año específico
	 */
	private static final String insertarCentrosAtencionStr = "INSERT INTO metas_ca_pyp " +
		"(consecutivo,meta_pyp,centro_atencion,numero_actividades) " ;
	
	/**
	 * Cadena que inserta una ocupacion de un centro de atencion, actividad, programa, convenio y año específico
	 */
	private static final String insertarOcupacionesStr = "INSERT INTO metas_ocupacion_pyp " +
		"(consecutivo,meta_ca_pyp,ocupacion_medica,numero_actividades) " ;
	
	/**
	 * Cadena que modifica informacion de la actividad de un programa, convenio y año específico
	 */
	private static final String modificarActividadStr = "UPDATE metas_pyp SET numero_actividades = ? , prog_act_conv = ? WHERE consecutivo = ?";
	
	/**
	 * Cadena que modifica informacion del centro de atencion de una actividad, programa, convenio y año específico
	 */
	private static final String modificarCentroAtencionStr = "UPDATE metas_ca_pyp SET numero_actividades = ? , centro_atencion = ? WHERE consecutivo = ?";
	
	/**
	 * Cadena que modifica informacion de las ocupaciones médicas de un centro atencion, actividad, programa, convenio y año específico
	 */
	private static final String modificarOcupacionStr = "UPDATE metas_ocupacion_pyp SET numero_actividades = ? , ocupacion_medica = ? WHERE consecutivo = ?";
		
	/**
	 * Cadena usada para eliminar un centro de atencion de una actividad, programa, convenio y año específicos
	 */
	private static final String eliminarCentroAtencionStr = "DELETE FROM metas_ca_pyp WHERE consecutivo = ?";
	
	/**
	 * Cadena usada para eliminar una ocupacion de un centro de atencion, actividad, programa, convenio y año específico
	 */
	private static final String eliminarOcupacionStr = "DELETE FROM metas_ocupacion_pyp WHERE consecutivo = ?";
	
	/**
	 * Método implementado para cargar las actividades de un programa y convenio específico
	 */
	private static final String cargarActividadesProgramaConvenioStr = "SELECT "+ 
		"papc.consecutivo AS consecutivo, "+
		"CASE WHEN ap.articulo IS NULL THEN ap.servicio ELSE ap.articulo END AS codigo_actividad, "+ 
		"CASE WHEN ap.articulo IS NULL THEN " +
			"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || " +
			"getcodigopropservicio2(ap.servicio,"+ConstantesBD.codigoTarifarioCups+")  || ' '  || " +
			"getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' ' || getnivelservicio(ap.servicio) " +
		"ELSE " +
			"getdescarticulo(ap.articulo) " +
		"END AS descripcion_actividad "+ 
		"FROM prog_act_pyp_convenio papc "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"WHERE papc.convenio = ? AND papc.institucion = ? AND papc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND app.programa = ?";
		
	
	/**
	 * Método implementado para consultar las actividades del programa, convenio, y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarActividades(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultarActividadesStr + " WHERE ";
			boolean hayCondicion = false;
			
			
			
			
			//Se verifica el Año
			if(campos.get("anio")!=null)
			{
				consulta += " mp.anio = "+campos.get("anio");
				hayCondicion = true;
			}
			
			//Se verifica el consecutivo
			if(campos.get("consecutivo")!=null)
			{
				consulta += (hayCondicion?" AND ":"") + " mp.consecutivo = "+campos.get("consecutivo");
				hayCondicion = true;
			}
			
			//se verifica la institucion
			if(campos.get("institucion")!= null)
			{
				consulta+= (hayCondicion?" AND ":"") + " mp.institucion = " + campos.get("institucion");
				hayCondicion = true;
			}
			
			//Se verifica el convenio
			if(campos.get("convenio")!=null)
			{
				consulta += (hayCondicion?" AND ":"") + " papc.convenio = "+campos.get("convenio")+" AND papc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
				hayCondicion = true;
			}
			
			//Se verifica el programa
			if(campos.get("programa")!=null)
			{
				consulta += (hayCondicion?" AND ":"") + " app.programa = '"+campos.get("programa")+"'";
				hayCondicion = true;
			}
			
			
			
			consulta += " ORDER BY descripcion_actividad ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarActividades de SqlBaseMetasPYPDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar los centros de atencion de una actividad, programa, convenio y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarCentrosAtencion(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultarCentrosAtencionStr + " WHERE ";
			boolean hayCondicion = false;
			
			//Se verifica consecutivo que relaciona a la actividad
			if(campos.get("consecutivo")!=null)
			{
				consulta += " mcp.meta_pyp = "+campos.get("consecutivo");
				hayCondicion = true;
			}
			
			// se verifica el consecutivo interno axioma del registro
			if(campos.get("consecutivoAxioma")!=null)
			{
				consulta += (hayCondicion?" AND ":"") + " mcp.consecutivo = "+campos.get("consecutivoAxioma");
				hayCondicion = true;
			}
			
			
			consulta += " ORDER BY nombre_centro_atencion ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCentrosAtencion de SqlBaseMetasPYPDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta las ocupaciones del centro atencion, actividad, programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarOcupaciones(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultarOcupacionesStr + " WHERE ";
			boolean hayCondicion = false;
			
			//Se verifica el consecutivo de la relacion con centros atencion
			if(campos.get("consecutivo")!=null)
			{
				consulta += " mop.meta_ca_pyp = " + campos.get("consecutivo");
				hayCondicion = true;
			}
			
			//Se verifica el consecutivo interno Axioma
			if(campos.get("consecutivoAxioma")!=null)
			{
				consulta += (hayCondicion?" AND ":"") +" mop.consecutivo = " + campos.get("consecutivoAxioma");
				hayCondicion = true;
			}
			
			consulta += " ORDER BY nombre_ocupacion_medica ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarOcupaciones de SqlBaseMetasPYPDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar una actividad de un programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarActividades(Connection con,HashMap campos)
	{
		try
		{
			String insertar = insertarActividadesStr + " VALUES ("+campos.get("secuencia")+",?,?,?,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO metas_pyp " +
					"(consecutivo,anio,prog_act_conv,numero_actividades,institucion)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("anio")+""));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("codigoProgramaActividadConvenio")+""));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("numeroActividades")+""));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_metas_pyp");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarActividades de SqlBaseMetasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que insertar un centro de atencion para una actividad, programa, convenio y anio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarCentrosAtencion(Connection con,HashMap campos)
	{
		try
		{
			String insertar = insertarCentrosAtencionStr + " VALUES ("+campos.get("secuencia")+",?,?,?) ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO metas_ca_pyp " +
				"(consecutivo,meta_pyp,centro_atencion,numero_actividades)
			 */
			
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("codigoMetaPYP")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("centroAtencion")+""));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("numeroActividades")+""));
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_metas_ca_pyp");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarCentrosAtencion de SqlBaseMetasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para insertar un ocupacion de una actividad, programa, convenio, año
	 * específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarOcupaciones(Connection con,HashMap campos)
	{
		try
		{
			String insertar = insertarOcupacionesStr + " VALUES ("+campos.get("secuencia")+",?,?,?) ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO metas_ocupacion_pyp " +
				"(consecutivo,meta_ca_pyp,ocupacion_medica,numero_actividades)
			 */
			
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("codigoMetaCAPYP")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("ocupacionMedica")+""));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("numeroActividades")+""));
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_metas_ocupacion_pyp");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarOcupaciones de SqlBaseMetasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para modificar una actividad, centro atencion o ocupacion médica
	 * pertenecientes a un programa, convenio y año específicos.
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valor pueden ser : actividad, centroAtencion y ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificar(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "";
			String tipo = campos.get("tipo").toString();
			PreparedStatementDecorator pst = null;
			
			if(tipo.equals("actividad"))
				consulta = modificarActividadStr;
			else if(tipo.equals("centroAtencion"))
				consulta = modificarCentroAtencionStr;
			else if(tipo.equals("ocupacionMedica"))
				consulta = modificarOcupacionStr;
				
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroActividades")+""));
			
			if(tipo.equals("actividad"))
				pst.setDouble(2,Utilidades.convertirADouble(campos.get("codigoProgramaActividadConvenio")+""));
			else if(tipo.equals("centroAtencion"))
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("centroAtencion")+""));
			else if(tipo.equals("ocupacionMedica"))
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("ocupacionMedica")+""));
			
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de tipo "+campos.get("tipo")+" de SqlBaseMetasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que elimina un centro de atencion o una ocupacion pertenecientes a
	 * una actividad, programa , convenio y año específicos
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valores pueden ser : centroAtencion o ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminar(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "";
			String tipo = campos.get("tipo").toString();
			PreparedStatementDecorator pst = null;
			
			if(tipo.equals("centroAtencion"))
				consulta = eliminarCentroAtencionStr;
			else if(tipo.equals("ocupacionMedica"))
				consulta = eliminarOcupacionStr;
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar con el tipo "+campos.get("tipo")+" de SQlBaseMetasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que consulta las actividades de un programa por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarActividadesProgramaConvenio(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarActividadesProgramaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("convenio")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setString(3,campos.get("programa")+"");
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarActividadesProgramaConvenio de SqlBaseMetasPYPDao: "+e);
			return null;
		}
	}
}
