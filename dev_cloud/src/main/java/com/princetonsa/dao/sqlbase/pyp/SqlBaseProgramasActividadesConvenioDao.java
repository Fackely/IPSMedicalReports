/*
 * Ago 08, 2006 
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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de programas y actividades por convenio
 */
public class SqlBaseProgramasActividadesConvenioDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseProgramasActividadesConvenioDao.class);
	
	/**
	 * Cadena que consulta los programas y actividades por convenio
	 */
	private static final String consultarStr = "SELECT "+ 
		"papc.consecutivo AS consecutivo, "+
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
		"END AS descripcion_actividad,"+ 
		"papc.activo AS activo, "+
		"papc.institucion AS institucion, "+
		"CASE WHEN getEsUsadoProgActConv(papc.consecutivo) > 0 THEN 'true' ELSE 'false' END AS es_usado "+ 
		"FROM prog_act_pyp_convenio papc "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) "+ 
		"INNER JOIN programas_salud_pyp  psp ON(psp.codigo=app.programa AND psp.institucion=app.institucion) "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad)  ";
	
	/**
	 * Cadena que inserta un nuevo programa y actividad por convenio
	 */
	private static final String insertarStr = "INSERT INTO prog_act_pyp_convenio " +
		"(consecutivo,convenio,actividad_programa_pyp,activo,institucion) " ;
	
	/**
	 * Cadena que modifica un programa y actividad por convenio
	 */
	private static final String modificarStr = "UPDATE prog_act_pyp_convenio " +
			"SET activo = ? WHERE consecutivo = ?";
	
	/**
	 * Cadena que elimina un programa y actividad por convenio
	 */
	private static final String eliminarStr = "DELETE FROM prog_act_pyp_convenio WHERE consecutivo = ?";
	
	/**
	 * Cadena que consulta las actividades de un programa
	 */
	private static final String cargarActividadesProgramaStr = "SELECT "+ 
		"app.codigo AS consecutivo, "+ 
		"CASE WHEN ap.articulo IS NULL THEN ap.servicio ELSE ap.articulo END AS codigo_actividad, "+ 
		"CASE WHEN ap.articulo IS NULL THEN " +
			"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || " +
			"getcodigopropservicio2(ap.servicio,"+ConstantesBD.codigoTarifarioCups+")  || ' '  || " +
			"getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' ' || getnivelservicio(ap.servicio) " +
		"ELSE " +
			"getdescarticulo(ap.articulo) " +
		"END AS descripcion_actividad "+ 
		"FROM actividades_programa_pyp app "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"WHERE app.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND app.programa = ? AND app.institucion = ?";
		
	
	/**
	 * Método implementado para consultar los programas y actividades por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultar(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultarStr + " WHERE ";
			boolean hayCondicion = false;
			
			//Se verifica campo convenio
			if(campos.get("convenio")!=null)
			{
				consulta += " papc.convenio = "+campos.get("convenio");
				hayCondicion = true;
			}
			
			//Se verifica el campo programa
			if(campos.get("programa")!=null)
			{
				consulta += (hayCondicion?" AND ":"") +" app.programa = '"+campos.get("programa")+"' ";
				hayCondicion = true;
			}
			
			//Se verifica el campo institucion
			if(campos.get("institucion")!=null)
			{
				consulta += (hayCondicion?" AND ":"") +" app.institucion = "+campos.get("institucion");
				hayCondicion = true;
			}
			
			//Se verifica el campo consecutivo
			if(campos.get("consecutivo")!=null)
			{
				consulta += (hayCondicion?" AND ":"") +" papc.consecutivo = "+campos.get("consecutivo");
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
			logger.error("Error en consultar de SqlBaseProgramasActividadesConvenioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertar(Connection con,HashMap campos)
	{
		try
		{
			String insertar = insertarStr + " VALUES("+campos.get("secuencia")+",?,?,?,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO prog_act_pyp_convenio " +
				"(consecutivo,convenio,actividad_programa_pyp,activo,institucion)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("convenio")+""));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("codigoProgramaActividad")+""));
			pst.setBoolean(3,UtilidadTexto.getBoolean(campos.get("activo").toString()));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			logger.info("\n\n\n INSERTAR-->"+insertar+"  conv->"+Utilidades.convertirAEntero(campos.get("convenio")+"")+" progAct->"+Utilidades.convertirADouble(campos.get("codigoProgramaActividad")+"")+" activo->"+UtilidadTexto.getBoolean(campos.get("activo").toString())+" inst->"+Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp =  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_prog_act_pyp_convenio");
			
			return resp;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			logger.error("Error en insertar de SqlBaseProgramasActividadesConvenioDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para modificar un programa y actividad por convenio
	 * 
	 * @param con
	 * @return
	 */
	public static int modificar(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setBoolean(1,UtilidadTexto.getBoolean(campos.get("activo").toString()));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar del SqlBaseProgramasActividadesConvenioDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para eliminar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminar(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseProgramasActividadesConvenioDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para cargar  las actividades de un programa
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarActividadesPrograma(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarActividadesProgramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("programa")+"");
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarActividadesPrograma de SqlBaseProgramasActividadesConvenioDao: "+e);
			return null;
		}
	}
}
