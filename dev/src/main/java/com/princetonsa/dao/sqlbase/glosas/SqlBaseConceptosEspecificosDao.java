package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Anexo 685
 * Fecha: Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class SqlBaseConceptosEspecificosDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseConceptosEspecificosDao.class);


	/*----------------------------------------------------------------------------------------
	 *                         ATRIBUTOS DE CONCEPTOS ESPECIFICOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Cadena utilizada para consultar los datos
	 * de conceptos especificos
	 */
	private static final String strCadenaConsulta=" SELECT" +
															" ce.codigo As codigo, " +
															" ce.consecutivo As consecutivo, " +
															" ce.descripcion As descripcion, " +
															" ce.activo As activo, " +
															" '"+ConstantesBD.acronimoSi +"' As esta_bd "+
													" FROM glosas.conceptos_especificos ce " +
													" WHERE ce.institucion=?";
	
	/**
	 * Cadena para actualizar los datos de conceptos especificos
	 */
	private static final String strCadenaActualizar=" UPDATE glosas.conceptos_especificos SET" +
															  //" consecutivo=?, " +
															  " descripcion=?, " + //1
															  " activo=?," + 	  //2
															  " usuario_modifica=?, " +//3
															  " fecha_modifica=?, " +//4
															  " hora_modifica=? " +//5
													" WHERE institucion=?" +//6
													" AND 	codigo=? ";//7
	
	
	/**
	 * cadena encargada de realizar la insercion de datos
	 * de conceptos especificos.
	 */
	private static final String strCadenaInsertar = "INSERT INTO glosas.conceptos_especificos " +
														"(codigo, consecutivo, descripcion, activo, institucion, " +
														" usuario_modifica, fecha_modifica, hora_modifica) VALUES " +
														" (?,?,?,?,?,?,?,?)"; 

	
	/**
	 * cadena encargada de eliminar datos
	 * de conceptos especificos.
	 */
	private static final String strCadenaEliminar = "DELETE FROM glosas.conceptos_especificos WHERE codigo=? AND institucion=?";
	
	
	/*----------------------------------------------------------------------------------------
	 *                         FIN ATRIBUTOS DE CONCEPTOS ESPECIFICOS
	 -----------------------------------------------------------------------------------------*/
	
	/*---------------------------------------------------------------------
	 *  		METODOS PARA EL MANEJO DE CONCEPTOS ESPECIFICOS
	 ---------------------------------------------------------------------*/

	
	/**
	 * Metodo encargado de insetar
	 * los datos de conceptos especificos.
	 * @author Felipe Perez Granda
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo --> Requerido
	 * -- descripcion --> Requerido
	 * -- activo --> Requerido
	 * -- institucion --> Requerido
	 * @return false/true
	 */
	public static boolean insertarConceptosEspecificos (Connection connection, HashMap datos)
	{
		logger.info("\n entre a insertarConceptosEspecificos -->"+datos);
		String cadena=strCadenaInsertar;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			//codigo - consecutivo - descripcion - activo - Institucion
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("codigo")+""));
			ps.setDouble(2, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(connection, "glosas.seq_conceptos_especificos")+""));
			ps.setString(3, datos.get("descripcion")+"");
			ps.setString(4, datos.get("activo")+"");		
			ps.setInt(5, Utilidades.convertirAEntero(datos.get("institucion")+""));

			//usuario, fecha, hora 76902
	  		ps.setString(6, datos.get("usuarioModifica")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		ps.setString(8, UtilidadFecha.getHoraActual(connection));

			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema insertando datos de conceptos especificos "+e);
		}
		return false;
	}
		
	/**
	 * Metodo encargado de consultar la informacion de
	 * conceptos especificos glosas, en la tabla "conceptos_especificos".
	 * @author Felipe Perez Granda
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion --> Requerido
	 * -- action --> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,consecutivo_,descripcion_,
	 * activo_,estaBd_
	 */
	public static HashMap consultaConceptosEspecificos (Connection connection,HashMap criterios )
	{
		logger.info("\n entre a consultaConceptosEspecificos -->"+criterios);
		
		String cadena=strCadenaConsulta;
		String  where = "";
		String order=" ORDER BY codigo ASC";
		boolean filtroActivos=false;
		if (UtilidadCadena.noEsVacio(criterios.get("activo")+""))
		{
			if(UtilidadTexto.getBoolean(criterios.get("activo")+""))
			{
				where+=" AND ce.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			}
			else
			{
				where+=" AND ce.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			}
		}
		
		cadena+=where+order;
		logger.info("\n cadena --> "+cadena);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));	
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(rs, true, true);
			rs.close();
	        ps.close();
			return mapaRetorno;
		} 
		catch (SQLException e) 
		{
			logger.error("\n problema consultando los datos de conceptos específicos "+e);
		}
		
		
		return null;
	}
	
	
	/**
	 * Método encargado de actualizar la tabla de "conceptos_especificos".
	 * 
	 * @author Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo		--> requerido
	 * -- consecutivo	--> requerido
	 * -- descripcion	--> requerido
	 * -- activo		--> requerido
	 * -- institucion	--> requerido
	 * -- usuarioModifica -->  Requerido
	 * @return false/true
	 */
	public static boolean actualizaConceptosEspecificos (Connection connection,HashMap datos )
	{
		logger.info("\n entre a actualizaConceptosEspecificos -->"+datos);
		
		String cadena=strCadenaActualizar;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/*
			 * Solución Tarea 65401
			 * El consecutivo NO SE DEBE DE MODIFICAR
			 */
			//consecutivo
			//ps.setDouble(1, Utilidades.convertirADouble(datos.get("consecutivo")+""));
			//descripcion
			ps.setString(1, datos.get("descripcion")+"");
			//activo
			ps.setString(2, datos.get("activo")+"");
			//usuario modifica
			ps.setString(3, datos.get("usuarioModifica")+"");
			//fecha modifica
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora modifica
			ps.setString(5, UtilidadFecha.getHoraActual());
			//Institucion
			ps.setInt(6, Utilidades.convertirAEntero(datos.get("institucion")+""));
			//codigo
			ps.setInt(7, Utilidades.convertirAEntero(datos.get("codigo")+""));
		
			if (ps.executeUpdate()>0)
				return true;
		} 
		catch (SQLException e) 
		{
			logger.error("\n problema actualizando los datos de conceptos especificos "+e);
		}
		return false;
	}
	/**
	 * Método encargado de eliminar datos de la tabla
	 * conceptos_especificos
	 * @author Felipe Pérez
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * Keys el mapa de datos
	 * -- codigo 		--> Requerido
	 * -- institucion 	--> Requerido
	 * -----------------------------------
	 * @return true/false
	 */
	public static boolean eliminarConceptosEspecificos(Connection connection, HashMap datos)
	{
		logger.info("\n entre a actualizaConceptosEspecificos -->"+datos);
		String cadena=strCadenaEliminar;
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo
			ps.setInt(1, Utilidades.convertirAEntero(datos.get("codigo")+""));
			//Institucion
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("institucion")+""));
			if (ps.executeUpdate()>0)
				return true;
		} catch (SQLException e) {
			logger.error("\n problema eliminando los datos de conceptos especificos "+e);
		}
		return false;
	}
	
		/*---------------------------------------------------------------------
		 *  		FIN METODOS PARA EL MANEJO DE CONCEPTOS ESPECIFICOS
		 ---------------------------------------------------------------------*/
}
