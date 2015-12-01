/*
 * Creado en 11 Dic 2006
 * Juan David Ramírez
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.UtilidadSiEs;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 * 13 Dic 2006
 */
public class SqlBaseCategoriaDao
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCategoriaDao.class);
	
	private static final String eliminarCategoriaStr="DELETE FROM categoria WHERE cat_identificador=?";
	
	private static final String eliminarEnfermeraCategoriaStr="DELETE FROM categoria_enfermera WHERE cat_identificador=?";
	
	private static final String eliminarCategoriaRestriccionStr="DELETE FROM categoria_restriccion WHERE cat_identificador=?";
	
	private static final String consultarEnfermeraStr="SELECT getnombrepersonasies(codigo) AS nombre from personas per where codigo=?";    
	
	private static final String insertarStr="INSERT INTO categoria (cat_identificador, cat_nombre, cat_descripcion, centro_costo, cat_activo, color) VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String consultarModificarStr = "SELECT cat_identificador AS codigo, cat_nombre AS nombre, cat_descripcion AS descripcion, cat_activo AS estado, centro_costo AS centro_costo, color AS color FROM categoria WHERE";
	
	private static final String modificarCategoriaStr="UPDATE categoria set cat_nombre=?, cat_descripcion=?, centro_costo=?, cat_activo=?, color=? where cat_identificador=?";
	
	private static final String consultarCategoriasStr = "SELECT c.cat_identificador AS codigo, c.cat_nombre AS nombre, c.cat_descripcion AS descripcion, cc.nombre AS centro_costo, c.cat_activo AS estado, color AS color FROM categoria c INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) ORDER BY c.cat_nombre";

	private static final String consultarCategoriasCentroCostoStr = "SELECT c.cat_identificador AS codigo, c.cat_nombre AS nombre, c.cat_descripcion AS descripcion, cc.nombre AS centro_costo, c.cat_activo AS estado, color AS color FROM categoria c INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) WHERE c.centro_costo=? ORDER BY c.cat_nombre";

	private static final String consultarCategoriasActivasStr = "SELECT c.cat_identificador AS codigo, c.cat_nombre AS nombre, c.cat_descripcion AS descripcion, cc.nombre AS centro_costo, c.cat_activo AS estado, color AS color FROM categoria c INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) WHERE c.cat_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY c.cat_nombre";

	private static final String consultarCategoriasActivasCentroCostoStr = "SELECT c.cat_identificador AS codigo, c.cat_nombre AS nombre, c.cat_descripcion AS descripcion, cc.nombre AS centro_costo, c.cat_activo AS estado, color AS color FROM categoria c INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) WHERE c.cat_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND c.centro_costo=? ORDER BY c.cat_nombre";

	private static final String consultarCategoriasDestinoStr = "SELECT c.cat_identificador AS codigo, c.cat_nombre AS nombre, c.cat_descripcion AS descripcion, cc.nombre AS centro_costo, c.cat_activo AS estado, color AS color FROM categoria c INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) INNER JOIN cuadro_turnos ct ON(ct.codigocategoria=c.cat_identificador) WHERE ? BETWEEN ct.fechainicio AND fechafin ORDER BY c.cat_nombre";

	private static final String actualizarCategoriaEnfermeraStr="UPDATE categoria_enfermera SET fecha_fin=? WHERE codigo_medico=? AND (fecha_fin IS null OR fecha_fin>CURRENT_DATE) AND cat_identificador=?";

	private static final String eliminarCategoriaEnfermeraStr="DELETE FROM categoria_enfermera WHERE codigo_medico=? AND (fecha_fin IS null OR fecha_fin>CURRENT_DATE) AND cat_identificador=?";
	
	private static final String buscarFechaInicioCategoriaEnfermeraStr="SELECT fecha_inicio AS fecha_inicio FROM categoria_enfermera WHERE codigo_medico=? AND (fecha_fin IS null OR fecha_fin>CURRENT_DATE) AND cat_identificador=?";
	
	private static final String insertarCategoriaEnfermeraStr="INSERT INTO categoria_enfermera (codigo_medico, cat_identificador, fecha_inicio, fecha_fin, hora_graba) VALUES (?,?,?,?, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
	
	private static final String consultarCategoriasEnfermeraStr = "SELECT ce.cat_identificador as codigo, ca.cat_nombre AS nombre from categoria_enfermera ce INNER JOIN categoria ca ON(ce.cat_identificador=ca.cat_identificador) WHERE ce.codigo_medico = ? and (ce.fecha_fin IS null OR ce.fecha_fin>CURRENT_DATE)";

	private static final String existeCuadroDestinoFechaFinStr="SELECT max(ct.fechafin) AS fechafin FROM cuadro_turnos ct WHERE ((? BETWEEN ct.fechainicio AND ct.fechafin) OR (? BETWEEN ct.fechainicio AND ct.fechafin)) AND ct.codigocategoria=?";

	private static final String existeCuadroDestinoNoFechaFinStr="SELECT max(ct.fechafin) AS fechafin FROM cuadro_turnos ct WHERE ((? BETWEEN ct.fechainicio AND ct.fechafin)) AND ct.codigocategoria=?";

	private static final String existenTurnosGeneradosNoFechaFinStr="SELECT count(1) AS numResultados FROM cuadro_turnos ct INNER JOIN ct_turno ctt on(ctt.ctcodigocuadro=ct.ctcodigocuadro) WHERE ct.codigocategoria=? AND ctt.codigomedico=? AND ct_fecha>=?";

	private static final String existenTurnosGeneradosFechaFinStr="SELECT count(1) AS numResultados FROM cuadro_turnos ct INNER JOIN ct_turno ctt on(ctt.ctcodigocuadro=ct.ctcodigocuadro) WHERE ct.codigocategoria=? AND ctt.codigomedico=? AND ct_fecha BETWEEN ? AND ?";

	private static final String eliminarTurnosNovedadPersonaEnCategoriaStr = "DELETE FROM ct_turno_novedad WHERE codigo_observacion IN(SELECT codigo FROM ct_turno_observacion WHERE ct_turno IN(SELECT ctcodigo FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=?";
	private static final String eliminarTurnosObservacionPersonaEnCategoriaStr = "DELETE FROM ct_turno_observacion WHERE ct_turno IN(SELECT ctcodigo FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=?";
	// EL ERROR ESTÁ AQUI// Hay que eliminar de ct_turno, ct_cubrir_turno y ct_turno_general
//	private static final String eliminarTurnosPersonaEnCategoriaStr = "DELETE FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=?";
//	private static final String eliminarTurnosCubrirPersonaEnCategoriaStr = "DELETE FROM ct_cubrir_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=?";
	private static final String eliminarTurnosPersonaEnCategoriaStr = "DELETE FROM ct_turno_general WHERE ctcodigo IN(SELECT ctcodigo FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=?";
//	private static final String eliminarTurnosPersonaEnCategoriaFechaActualStr = "DELETE FROM ct_turno_general WHERE ctcodigo IN(SELECT ctcodigo FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>=CURRENT_DATE";
	
	private static final String actualizarTurnosPersonaEnCategoriaStr="UPDATE ct_turno SET ctcodigocuadro=(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct_fecha BETWEEN ct.fechainicio AND ct.fechafin AND ct.codigocategoria=?) WHERE ctcodigocuadro=(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=? AND ct_fecha BETWEEN ct.fechainicio AND ct.fechafin GROUP BY ct.ctcodigocuadro) AND codigomedico=? AND ct_fecha>=?";
	
	private static final String buscarTurnosPersonaEnCategoriaStr="SELECT count(1) AS numResultados FROM ct_turno WHERE codigomedico=? AND ctcodigocuadro IN(SELECT ct.ctcodigocuadro FROM cuadro_turnos ct WHERE ct.codigocategoria=?) AND ct_fecha>?";
	
	private static final String consultarFechaFinAsociacionStr="SELECT fecha_fin AS fecha_fin, (SELECT ct.codigocategoria FROM ct_turno ctt INNER JOIN cuadro_turnos ct on(ct.ctcodigocuadro=ctt.ctcodigocuadro) WHERE ctt.codigomedico=? AND ct_fecha BETWEEN ct.fechainicio AND ct.fechafin GROUP BY ct.codigocategoria ORDER BY max(ctt.ct_fecha) DESC LIMIT 1) AS codigoCategoria FROM categoria_enfermera WHERE codigo_medico=? AND cat_identificador=(SELECT ct.codigocategoria FROM ct_turno ctt INNER JOIN cuadro_turnos ct USING(ctcodigocuadro) WHERE ctt.codigomedico=? AND ct_fecha BETWEEN ct.fechainicio AND ct.fechafin GROUP BY ct.codigocategoria ORDER BY max(ctt.ct_fecha) DESC LIMIT 1) AND (fecha_fin IS NULL OR fecha_fin>CURRENT_DATE)";
	
	/*************************Asignar Restricción a Categoría********************/
	
	
	/**
	 * String que contiene la consulta de las restricciones que esten asociadas a la categoría dada
	 * y que tengan en fecha fin null
	 */
	private static final String consultarRestriccionesCategoriaStr="SELECT cr.codigoAsociacion AS codigoAsociacion, cr.cat_identificador AS codigoCategoria, res.codigoRestriccion AS codigo, res.descrrestriccion AS descripcion, res.text AS texto, cr.valorrestriccion AS valor from restriccion_categoria res INNER JOIN categoria_restriccion cr ON(cr.codigoRestriccion=res.codigorestriccion AND cr.cat_identificador=?) WHERE cr.fecha_fin IS null";
	
	private static final String consultarRestriccionesCategoriaPersonaStr=
		"SELECT " +
			"cr.codigoAsociacion AS codigoAsociacion, " +
			"cr.cat_identificador AS codigoCategoria, " +
			"res.codigoRestriccion AS codigo, " +
			"res.descrrestriccion AS descripcion, " +
			"res.text AS texto, " +
			"cr.valorrestriccion AS valor " +
		"FROM " +
			"restriccion_categoria res " +
		"INNER JOIN categoria_restriccion cr " +
			"ON(" +
				"cr.codigoRestriccion=res.codigorestriccion " +
			"AND " +
				"cr.cat_identificador=(" +
					"SELECT " +
						"cat_identificador " +
					"FROM " +
						"categoria_enfermera " +
					"WHERE " +
						"codigo_medico=? " +
					"AND " +
						"fecha_fin IS NULL)" +
				")" +
		"WHERE cr.fecha_fin IS null";
	
	/**
	 * String que contiene la consulta SQL para asociar una restricción a una categoría
	 */
	private static final String insertarRestriccionCategoriaStr="INSERT INTO categoria_restriccion (codigoAsociacion, codigorestriccion, cat_identificador, fecha_inicio, valorrestriccion) VALUES (?, ?, ?,CURRENT_DATE, ? )";
	
	/**
	 * String que contiene la consulta SQL para eliminar todos los registros de la 
	 * tabla categoria_restriccion
	 */
	private static final String eliminarTablaStr="DELETE FROM categoria_restriccion";
	
	/**
	 * String que contiene la consulta SQL para actualizar a la feca actual, la finalización de la 
	 * asociación de la restriccion a esa categoria
	 */	
	private static final String actualizarRestriccionCategoriaStr="UPDATE categoria_restriccion SET fecha_fin=CURRENT_DATE WHERE (codigoRestriccion= ? AND cat_identificador=? AND fecha_fin IS null)";
	
	/**
	 * String que consulta el nombre de la categoria a cual esta asociado el codigo de la persona que entra.
	 */
	private static final String nombreCategoriaEnfermneraStr="SELECT cat.cat_nombre AS nombre FROM categoria cat, categoria_enfermera catE  WHERE (cat.cat_identificador=catE.cat_identificador AND catE.codigo_medico=? AND (fecha_fin IS null OR fecha_fin>CURRENT_DATE))";
	
	/**
	 * String que consulta el las personas que cumplen con las condiciones de la busqueda avanzada
	 */
	private static final String enfermerasBusAvanzadaStr="SELECT per.codigo AS codigo, getnombrepersonasies(per.codigo) AS nombre, cat.cat_identificador AS codigocategoria, cat.cat_nombre AS nombrecategoria, ce.fecha_inicio AS fecha_inicio_bd, ce.fecha_fin AS fecha_fin_bd, to_char(ce.fecha_inicio, 'dd/mm/yyyy') AS fecha_inicio, CASE WHEN fecha_fin IS NULL THEN '' ELSE to_char(ce.fecha_fin, 'dd/mm/yyyy') END AS fecha_fin FROM personas per INNER JOIN categoria_enfermera ce ON(ce.codigo_medico=per.codigo) INNER JOIN categoria cat ON(cat.cat_identificador=ce.cat_identificador) WHERE (ce.fecha_fin IS null OR ce.fecha_fin>CURRENT_DATE) ";
	
	/**
	 * Muestra todas las categorías que se encuentran en el sistema
	 * @param con
	 * @param buscarActivas
	 * @param centroCosto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarCategorias(Connection con, boolean buscarActivas, Integer centroCosto) 
	{
		try
		{
			PreparedStatementDecorator cargarCategoriasStatement;
			if(buscarActivas)
			{
				if(centroCosto!=null && centroCosto!=0)
				{
					cargarCategoriasStatement = new PreparedStatementDecorator(con, consultarCategoriasActivasCentroCostoStr);
					cargarCategoriasStatement.setInt(1, centroCosto);
				}
				else
				{
					cargarCategoriasStatement = new PreparedStatementDecorator(con, consultarCategoriasActivasStr);
				}
			}
			else
			{
				if(centroCosto!=null && centroCosto!=0)
				{
					cargarCategoriasStatement = new PreparedStatementDecorator(con, consultarCategoriasCentroCostoStr);
					cargarCategoriasStatement.setInt(1, centroCosto);
				}
				else
				{
					cargarCategoriasStatement = new PreparedStatementDecorator(con, consultarCategoriasStr);
				}
			}
			//logger.info(cargarCategoriasStatement);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarCategoriasStatement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las categorías ",e);
			return null;
		}
	}
	
	
	/**
	 * Sentencia para ingrear una categoría
	 * @param con
	 * @param nombre
	 * @param descripcion
	 * @param activo
	 * @param centroCosto
	 * @param color
	 * @return
	 */
	public static int insertarCategoria(Connection con, String nombre, String descripcion, boolean activo, int centroCosto, String color)
	{
		try
		{
			PreparedStatementDecorator insertarCat=new PreparedStatementDecorator(con, insertarStr);
			insertarCat.setInt(1, UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_categoria"));
			insertarCat.setString(2, nombre);
			insertarCat.setString(3, descripcion);
			insertarCat.setInt(4, centroCosto);
			insertarCat.setBoolean(5, activo);
			insertarCat.setString(6, color);
			return insertarCat.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error insertando categoria: "+e);
			return 0;
		}
	}
	
	public static Collection<HashMap<String, Object>> categoriaUtilizada(Connection con, String nombre)
	{
		PreparedStatementDecorator consultarModificarCat;
		ResultSet rs=null;
		String consultaString="";
		consultaString=consultarModificarStr;
		consultaString+=" UPPER(cat_nombre)=UPPER('"+nombre+"')";
					
		try
		{
			consultarModificarCat = new PreparedStatementDecorator(con, consultaString);
			rs=consultarModificarCat.executeQuery();
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
		}
		catch (SQLException e)
		{
			logger.error("Error Al consultar la Categoria: "+e);
			return null;
		}
	}
	
	public static ResultSet consultarModificar(Connection con, int codigo)
	{
		PreparedStatementDecorator consultarModificarCat;
		ResultSet rs=null;
		String consultaString="";
		consultaString=consultarModificarStr;
		consultaString+=" cat_identificador=?";
					
		try
		{
			consultarModificarCat = new PreparedStatementDecorator(con, consultaString);
			consultarModificarCat.setInt(1, codigo);
			rs=consultarModificarCat.executeQuery();
		}
		catch (SQLException e)
		{
			logger.error("Error Al consultar la Categoría: "+e);
		}
			
		return rs;
			
	}
	
	/**
	 * Método para modificar una categoría
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param descripcion
	 * @param activo
	 * @param centroCosto
	 * @param color
	 */
	public static void modificar(Connection con, int codigo, String nombre, String descripcion, boolean activo, int centroCosto, String color)
	{
	    try
	    {
	        PreparedStatementDecorator modificarCategoriaStatement;
	        modificarCategoriaStatement = new PreparedStatementDecorator(con, modificarCategoriaStr);
	        
	        modificarCategoriaStatement.setString(1,nombre);
	        modificarCategoriaStatement.setString(2,descripcion);
	        modificarCategoriaStatement.setInt(3, centroCosto);
	        modificarCategoriaStatement.setBoolean(4,activo);
	        modificarCategoriaStatement.setString(5, color);
	        modificarCategoriaStatement.setInt(6,codigo);
	        modificarCategoriaStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la modificacion: "+e);
	    }
	}
	
	/**
	 * Metodo que desactiva una categoria poniendola false en la B.D
	 */
	public static int eliminarCategoria(Connection con, int codigo)
	{
		try
		{
			
		    PreparedStatementDecorator eliminarCategoria;
		    eliminarCategoria = new PreparedStatementDecorator(con, eliminarCategoriaRestriccionStr);
		    eliminarCategoria.setInt(1,codigo);
		    eliminarCategoria.executeUpdate();
		    eliminarCategoria = new PreparedStatementDecorator(con, eliminarEnfermeraCategoriaStr);
		    eliminarCategoria.setInt(1,codigo);
		    eliminarCategoria.executeUpdate();
			eliminarCategoria = new PreparedStatementDecorator(con, eliminarCategoriaStr);
			eliminarCategoria.setInt(1,codigo);
			return eliminarCategoria.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error Al deshabilitar la categoria: "+e);
			return 0;
		}
	}
	
	/**
	 * Consulta las personas asociadas a una categoría
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigo, boolean ordenar) 
	{
		String consultarEnferCategStr = "SELECT per.codigo AS codigo, getnombrepersonasies(per.codigo) AS nombre, to_char(fecha_inicio, 'DD/MM/YYYY') AS fecha_inicio, to_char(fecha_fin, 'DD/MM/YYYY') AS fecha_fin, to_char((SELECT MIN(fecha_inicio-1) FROM categoria_enfermera ce1 WHERE fecha_inicio > ce.fecha_fin AND per.codigo=ce1.codigo_medico), 'DD/MM/YYYY') AS limite, CASE WHEN (SELECT MIN(fecha_inicio-1) FROM categoria_enfermera ce2 WHERE fecha_inicio > ce.fecha_fin AND per.codigo=ce2.codigo_medico) = fecha_fin OR ce.fecha_fin IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as puedo_eliminar FROM personas per INNER JOIN categoria_enfermera ce ON(ce.codigo_medico=per.codigo AND ce.cat_identificador=?) WHERE (ce.fecha_fin IS null OR ce.fecha_fin>CURRENT_DATE)";
				
		if (ordenar)
		{
			consultarEnferCategStr+= " ORDER BY nombre";			
		}
		else
		{
			consultarEnferCategStr+= " ORDER BY random()";			
		}
		
	    try
	    {
	    	PreparedStatementDecorator cargarEnfermerasStatement = new PreparedStatementDecorator(con, consultarEnferCategStr);
	    	//System.out.println("consultarEnferCategStr "+consultarEnferCategStr);
			cargarEnfermerasStatement.setInt(1,codigo);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarEnfermerasStatement.executeQuery()));
		}
	    catch (SQLException e)
	    {
			logger.error("Error consultando las personas de una categoría "+e);
			return null;
		}
	}

	/**
	 * Consulta las personas asociadas a una categoría
	 * @param con
	 * @param codigoCategoria
	 * @param codigoCentroAtencion
	 * @return Listado de personas
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarPersonas(Connection con, Integer codigoCategoria, int codigoCentroAtencion) 
	{
		String consultarEnferCategStr="SELECT per.codigo_persona AS codigo, getnombrepersonasies(per.codigo_persona) AS nombre FROM usuarios per INNER JOIN (SELECT usuario FROM centros_costo cc INNER JOIN centros_costo_usuario ccu ON(cc.codigo=ccu.centro_costo AND cc.centro_atencion=?) GROUP BY usuario) tabla ON(tabla.usuario=per.login) ";
		if(codigoCategoria!=null)
		{
			consultarEnferCategStr+="WHERE per.codigo_persona NOT IN (SELECT codigo_medico FROM categoria_enfermera ce WHERE ce.cat_identificador=? AND (ce.fecha_fin>CURRENT_DATE OR ce.fecha_fin IS NULL)) " +
										"AND " +
										"tabla.usuario NOT IN(SELECT ui.login FROM usuarios_inactivos ui)";
		}
		consultarEnferCategStr+="ORDER BY nombre";
	    try
	    {
	    	PreparedStatementDecorator cargarEnfermerasStatement = new PreparedStatementDecorator(con, consultarEnferCategStr);
	    	cargarEnfermerasStatement.setInt(1, codigoCentroAtencion);
	    	cargarEnfermerasStatement.setInt(2, codigoCategoria);
	    	//logger.info(cargarEnfermerasStatement);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarEnfermerasStatement.executeQuery()));
		}
	    catch (SQLException e)
	    {
			logger.error("Error listando las personas ",e);
			return null;
		}
	}

	/**
	 * Consulta la categoría a la que esta asociada una persona
	 * @param con
	 * @param codigo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarCategoriasEnfermera(Connection con, int codigoPersona) 
	{
		try
		{
			PreparedStatementDecorator cargarEnfermerasStatement = new PreparedStatementDecorator(con, consultarCategoriasEnfermeraStr);
			cargarEnfermerasStatement.setInt(1,codigoPersona);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarEnfermerasStatement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error consultando la categoría de la persona "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta la información de una persona su nombre
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ResultSet consultarEnfermera(Connection con, int codigo)
	{
		try
		{
		    PreparedStatementDecorator consultarEnfermera;
			consultarEnfermera = new PreparedStatementDecorator(con, consultarEnfermeraStr);
			consultarEnfermera.setInt(1,codigo);
			return consultarEnfermera.executeQuery();
		}
		catch (SQLException e)
		{
			logger.error("Error Al consultar las personas: "+e);
			return null;
		}
	}
	
	
	
	/**
	 * Ingresa una asignación a una persona
	 * @param con
	 * @param codigoPersona
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static int insertarEnfermeraCategoria(Connection con, int codigoPersona, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		try
		{
			PreparedStatementDecorator insertarCatEnf=new PreparedStatementDecorator(con, insertarCategoriaEnfermeraStr);
			insertarCatEnf.setInt(1,codigoPersona);
			insertarCatEnf.setInt(2,codigoCategoria);
			insertarCatEnf.setString(3, fechaInicio);
			if(fechaFin==null || fechaFin.equals(""))
			{
				insertarCatEnf.setNull(4, Types.DATE);
			}
			else
			{
				insertarCatEnf.setString(4, fechaFin);
			}
			return insertarCatEnf.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error insertando la persona en la Categoria: "+e);
			return 0;
		}
	}
	
	/**
	 * (Juan David)
	 * Método que verifica si existen cuadros de turnos para mover los turnos existentes
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static String existeCuadroDestino(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		try
		{
			String consultaStr="";
			if(fechaInicio==null || fechaInicio.equals(""))
			{
				fechaInicio=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			}
			if(fechaFin==null || fechaFin.equals(""))
			{
				consultaStr=existeCuadroDestinoNoFechaFinStr;
			}
			else
			{
				consultaStr=existeCuadroDestinoFechaFinStr;
			}
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultaStr);
			stm.setString(1, fechaInicio);
			if(fechaFin==null || fechaFin.equals(""))
			{
				stm.setInt(2, codigoCategoria);
			}
			else
			{
				stm.setString(2, fechaFin);
				stm.setInt(3, codigoCategoria);
			}
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				return resultado.getString("fechafin");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si existe cuadro destino "+e);
		}
		return null;
	}
	
	
	/**
	 * (Juan David)
	 * Actualiza la información de persona asociada a la categoría, cuando esta es cambiada de
	 * categoría o cuando es cancelada la asociación, poniendo como fecha de finalización enviada
	 * por parámetro, en caso de que esta sea nula pone la fecha actual
	 * @param con
	 * @param codigoPersona
	 * @param fechaInicio
	 * @param fechaFin Fecha de finalización
	 * @param codigoCategoria
	 * @param eliminarTurnos Eliminar los turnos siguientes a la fecha de finalización
	 * @param codigoCategoriaOrigen
	 * @return 0 En caso de que no se actualice nada; > 0 en caso contrario
	 */
	public static int actualizarEnfermeraCategoria(Connection con, int codigoPersona, String fechaInicio, String fechaFin, int codigoCategoria, boolean eliminarTurnos, Integer codigoCategoriaOrigen)
	{
		try
		{
			String consultarEnferCategStr = "SELECT max(fecha_fin) AS fecha_fin FROM personas per INNER JOIN categoria_enfermera ce ON(ce.codigo_medico=per.codigo AND ce.cat_identificador!=?) WHERE ce.fecha_fin>CURRENT_DATE AND ce.codigo_medico=? AND ce.fecha_fin IS NOT NULL";
			//System.out.println("consultarEnferCategStr "+consultarEnferCategStr);
			PreparedStatementDecorator stmValidacionFechaFin=new PreparedStatementDecorator(con, consultarEnferCategStr);
			stmValidacionFechaFin.setInt(1, codigoCategoria);
			stmValidacionFechaFin.setInt(2, codigoPersona);
			//System.out.println("codigoPersona "+codigoPersona);
			//System.out.println("codigoCategoria "+codigoCategoria);
			ResultSet resultadoFechaFin=stmValidacionFechaFin.executeQuery();
			String fechaFinAsociacion="";
			if(resultadoFechaFin.next())
			{
				fechaFinAsociacion=resultadoFechaFin.getString("fecha_fin");
				if(fechaFinAsociacion==null)
				{
					fechaFinAsociacion=UtilidadFecha.incrementarDiasAFecha(fechaInicio, -1, true);				
				}
			}
			else
			{
				fechaFinAsociacion=UtilidadFecha.incrementarDiasAFecha(fechaInicio, -1, true);				
			}
			
			//System.out.println("fechaFinAsociacion "+fechaFinAsociacion);
			PreparedStatementDecorator actualizarEnfCat;
			int numResultados=0;
			if(fechaFin!=null && !fechaFin.equals(""))
			{
				//System.out.println("buscarTurnosPersonaEnCategoriaStr "+buscarTurnosPersonaEnCategoriaStr);
				actualizarEnfCat=new PreparedStatementDecorator(con, buscarTurnosPersonaEnCategoriaStr);
				actualizarEnfCat.setInt(1, codigoPersona);
				actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
				actualizarEnfCat.setString(3, fechaFin);
				ResultSet resultadoRs=actualizarEnfCat.executeQuery();
				if(resultadoRs.next())
				{
					numResultados=resultadoRs.getInt("numResultados");
				}
			}
			//System.out.println("consultarFechaFinAsociacionStr "+consultarFechaFinAsociacionStr);
			actualizarEnfCat=new PreparedStatementDecorator(con, consultarFechaFinAsociacionStr);
			actualizarEnfCat.setInt(1, codigoPersona);
			actualizarEnfCat.setInt(2, codigoPersona);
			actualizarEnfCat.setInt(3, codigoPersona);
			ResultSet resultadoRs=actualizarEnfCat.executeQuery();
			String fechaIngresoNuevaAsociacion=null;
			if(resultadoRs.next())
			{
				fechaIngresoNuevaAsociacion=resultadoRs.getString("fecha_fin");
				//System.out.println("codigoCategoriaNuevaAsociacion "+codigoCategoriaNuevaAsociacion);
			}
			//System.out.println("-------------");
			//System.out.println("buscarFechaInicioCategoriaEnfermeraStr "+buscarFechaInicioCategoriaEnfermeraStr);
			actualizarEnfCat=new PreparedStatementDecorator(con, buscarFechaInicioCategoriaEnfermeraStr);
			actualizarEnfCat.setInt(1, codigoPersona);
			actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
			resultadoRs=actualizarEnfCat.executeQuery();
			String fechaInicioComp="";
			if(resultadoRs.next())
			{
				fechaInicioComp=resultadoRs.getString("fecha_inicio");
			}
			
			if(fechaFinAsociacion==null || fechaFinAsociacion.equals(""))
			{
				fechaFinAsociacion=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			}
			if(!fechaInicioComp.equalsIgnoreCase(fechaInicio))
			{
				//System.out.println("actualizarCategoriaEnfermeraStr "+actualizarCategoriaEnfermeraStr);
				actualizarEnfCat=new PreparedStatementDecorator(con, actualizarCategoriaEnfermeraStr);
				actualizarEnfCat.setString(1, fechaFinAsociacion);
				actualizarEnfCat.setInt(2, codigoPersona);
				actualizarEnfCat.setInt(3, codigoCategoriaOrigen);
			}
			else
			{
				//System.out.println("eliminarCategoriaEnfermeraStr "+eliminarCategoriaEnfermeraStr);
				actualizarEnfCat=new PreparedStatementDecorator(con, eliminarCategoriaEnfermeraStr);
				actualizarEnfCat.setInt(1, codigoPersona);
				actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
			}
			int resultado=actualizarEnfCat.executeUpdate();
			
			if(resultado>0)
			{
				String complemento="";
				String fechaFinDestino=existeCuadroDestino(con, codigoCategoria, fechaInicio, fechaFin);
				if(fechaFin==null || fechaFin.equalsIgnoreCase(""))
				{
					fechaFin=null;
					if(!eliminarTurnos && fechaFinDestino!=null)
					{
						complemento=" AND ct_fecha<=?";
					}
				}
				else
				{
					complemento=" AND ct_fecha<=?";
				}
				if(eliminarTurnos)
				{
					try
					{
						actualizarEnfCat=new PreparedStatementDecorator(con, eliminarTurnosNovedadPersonaEnCategoriaStr+complemento+"))");
						actualizarEnfCat.setInt(1, codigoPersona);
						actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
						actualizarEnfCat.setString(3, fechaInicio);
						if(fechaFin!=null)
						{
							actualizarEnfCat.setString(4, fechaFin);
						}
						actualizarEnfCat.executeUpdate();
					}
					catch (Exception e) {
						logger.error("Error eliminando los turnos de las personas "+e);
					}
					try{
						actualizarEnfCat=new PreparedStatementDecorator(con, eliminarTurnosObservacionPersonaEnCategoriaStr+complemento+")");
						actualizarEnfCat.setInt(1, codigoPersona);
						actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
						actualizarEnfCat.setString(3, fechaInicio);
						if(fechaFin!=null)
						{
							actualizarEnfCat.setString(4, fechaFin);
						}
						actualizarEnfCat.executeUpdate();
					}
					catch (Exception e) {
						logger.error("Error eliminando los turnos de las personas "+e);
					}
					try{
						actualizarEnfCat=new PreparedStatementDecorator(con, eliminarTurnosPersonaEnCategoriaStr+complemento+")");
						actualizarEnfCat.setInt(1, codigoPersona);
						actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
						actualizarEnfCat.setString(3, fechaInicio);
						if(fechaFin!=null)
						{
							actualizarEnfCat.setString(4, fechaFin);
						}
						actualizarEnfCat.executeUpdate();
					}
					catch (Exception e) {
						logger.error("Error eliminando los turnos de las personas "+e);
					}
				}
				else
				{
					if(fechaFinDestino!=null)
					{
						/*System.out.println(actualizarTurnosPersonaEnCategoriaStr+complemento);
						System.out.println("codigoCategoria "+codigoCategoria);
						System.out.println("codigoPersona "+codigoPersona);
						System.out.println("codigoCategoriaOrigen "+codigoCategoriaOrigen);
						System.out.println("codigoPersona "+codigoPersona);
						System.out.println("fechaInicio "+fechaInicio);*/
						actualizarEnfCat=new PreparedStatementDecorator(con, actualizarTurnosPersonaEnCategoriaStr+complemento);
						actualizarEnfCat.setInt(1, codigoCategoria);
						//actualizarEnfCat.setInt(2, codigoPersona);
						actualizarEnfCat.setInt(2, codigoCategoriaOrigen);
						actualizarEnfCat.setInt(3, codigoPersona);
						actualizarEnfCat.setString(4, fechaInicio);
						
						if(fechaFin!=null)
						{
							if(fechaFin.compareTo(fechaFinDestino)>0)
							{
								actualizarEnfCat.setString(5, fechaFinDestino);
								try{
									PreparedStatementDecorator eliminarTurnosSinCuadroStm=new PreparedStatementDecorator(con, eliminarTurnosPersonaEnCategoriaStr);
									eliminarTurnosSinCuadroStm.setInt(1, codigoPersona);
									eliminarTurnosSinCuadroStm.setInt(2, codigoCategoriaOrigen);
									eliminarTurnosSinCuadroStm.setString(3, UtilidadFecha.incrementarDiasAFecha(fechaFinDestino, 1, true));
									eliminarTurnosSinCuadroStm.executeUpdate();
								}
								catch (Exception e)
								{
									logger.error("Error eliminando los turnos de las personas "+e);
								}
								
							}
							else
							{
								actualizarEnfCat.setString(5, fechaFin);
							}
						}
						else
						{
							/*
							 * Tengo que organizar esto
							 */
							try
							{
								String fechaInicioEliminacion=UtilidadFecha.incrementarDiasAFecha(fechaFinDestino, 1, true);
								try
								{
									PreparedStatementDecorator eliminarTurnosSinCuadroStm=new PreparedStatementDecorator(con, eliminarTurnosNovedadPersonaEnCategoriaStr+"))");
									eliminarTurnosSinCuadroStm.setInt(1, codigoPersona);
									eliminarTurnosSinCuadroStm.setInt(2, codigoCategoriaOrigen);
									eliminarTurnosSinCuadroStm.setString(3, fechaInicioEliminacion);
									eliminarTurnosSinCuadroStm.executeUpdate();
								}
								catch (Exception e) {
									logger.error("Error eliminando los turnos de las personas "+e);
								}
								try{
									PreparedStatementDecorator eliminarTurnosSinCuadroStm=new PreparedStatementDecorator(con, eliminarTurnosObservacionPersonaEnCategoriaStr+")");
									eliminarTurnosSinCuadroStm.setInt(1, codigoPersona);
									eliminarTurnosSinCuadroStm.setInt(2, codigoCategoriaOrigen);
									eliminarTurnosSinCuadroStm.setString(3, fechaInicioEliminacion);
									eliminarTurnosSinCuadroStm.executeUpdate();
								}
								catch (Exception e) {
									logger.error("Error eliminando los turnos de las personas "+e);
								}
								actualizarEnfCat.setString(5, fechaFinDestino);
								PreparedStatementDecorator eliminarTurnosSinCuadroStm=new PreparedStatementDecorator(con, eliminarTurnosPersonaEnCategoriaStr+")");
								eliminarTurnosSinCuadroStm.setInt(1, codigoPersona);
								eliminarTurnosSinCuadroStm.setInt(2, codigoCategoriaOrigen);
								eliminarTurnosSinCuadroStm.setString(3, fechaInicioEliminacion);
								//eliminarTurnosSinCuadroStm.setString(3, fechaFinDestino);
								eliminarTurnosSinCuadroStm.executeUpdate();
							}
							catch (Exception e) {
								logger.error("Error eliminando los turnos de las personas "+e);
								e.printStackTrace();
							}
						}
						actualizarEnfCat.executeUpdate();
					}
				}
				if(numResultados>0)
				{
					insertarEnfermeraCategoria(con, codigoPersona, codigoCategoriaOrigen, UtilidadFecha.incrementarDiasAFecha(fechaFin, 1, true), fechaIngresoNuevaAsociacion);
				}
				return resultado;
			}
			else
			{
				return 0;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error Actualizando la Tabla de categoria_enfermera: "+e);
			return 0;
		}
	}
	
	
	
	/* ****************Asignar restricciones a Categoria*****************/

	/**
	 * Método que retorna un Collection con las restricciones que se encuentra asociadas a la
	 * categoría con el código dado y que tienen null en fecha fin
	 * @param con
	 * @param codigo
	 * @return Collection<HashMap<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarRestriccionCategoria(Connection con, int codigo) 
	{
	    try
	    {
			PreparedStatementDecorator consultarRestriccionesCategoriaStatement = new PreparedStatementDecorator(con, consultarRestriccionesCategoriaStr);
			consultarRestriccionesCategoriaStatement.setInt(1,codigo);
			//logger.info(consultarRestriccionesCategoriaStatement);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarRestriccionesCategoriaStatement.executeQuery()));
		}
	    catch (SQLException e)
	    {
	    	logger.error("Error consultando las restricciones de la categoría "+e);
			return null;
		}
	}
	
	/**
	 * Método que elimina todos los registros existentes en la tabla de categoria_restriccion
	 * @param con
	 * @return
	 */
	public static int eliminarTabla(Connection con)
	{
		try
		{
			PreparedStatementDecorator elimarTablaStatement=new PreparedStatementDecorator(con, eliminarTablaStr);
			return elimarTablaStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando los registros: "+e);
			return 0;
		}
	}
	

	/**
	 * Método que inserta las restricciones de la categoría
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoCategoria
	 * @param valor
	 * @return
	 */
	public static int insertarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria, int valor)
	{
	    try
		{
			PreparedStatementDecorator insertarResCatSt=new PreparedStatementDecorator(con, insertarRestriccionCategoriaStr);
			insertarResCatSt.setInt(1,UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_categoria_restriccion"));
			insertarResCatSt.setInt(2,codigoRestriccion);
			insertarResCatSt.setInt(3,codigoCategoria);
			insertarResCatSt.setInt(4,valor);
			return insertarResCatSt.executeUpdate();
		}
	    catch (SQLException e)
		{
			logger.error("Error insertando la Restriccion en la Categoria: "+e);
			return 0;
		}	    
	}

	/**
	 * 	
	 * Método ejecuta la consulta de actualización de la restriccion a la categoria, donde la fecha de 
	 * finalización de la asociación de convierte a la fecha actual del sistema
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoCategoria
	 * @return
	 */
	public static int actualizarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria)
	{
		try
		{
			PreparedStatementDecorator actualizarRestriccionCategoriaSt=new PreparedStatementDecorator(con, actualizarRestriccionCategoriaStr);
			actualizarRestriccionCategoriaSt.setInt(1,codigoRestriccion);
			actualizarRestriccionCategoriaSt.setInt(2,codigoCategoria);
			return actualizarRestriccionCategoriaSt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando la Restriccion en la categoria: "+e);
			return 0;
		}	    
	}
	
	/**
	 * Método que retorna true y el nombre de la categoría si una persona esta o no asociada a una Categoria
	 * en caso de que no, retorna false
	 * @param con
	 * @param codigoProfesional
	 * @return
	 */
	
	
	
	public static ResultadoBoolean estaEnfermeraEnCategoria(Connection con, int codigoProfesional)
	{
		
		/**
		 * 
		 * Para inactivar usuarios que son profesionales de la salud y que adicionalmente su ocupaci&oacute;n es 'Enfermero(a)'
		 * Se estaba realizando la consulta de este metodo, ya que para inactivar un usuario enfermero(a) se debe verificar 
		 * que no se encuentre con un cuadro de turnos. El cuadro de turnos se realiza en el m&oacute;dulo de Cuadro de Tunos
		 * actualmente ya no se maneja este m&oacute;dulo por consiguiente en el BD de oracle no existe las tablas pertenecientes
		 * a este esquema solo existe en la BD de postgres. Este consulta se debe volver a activar en el momento de utilizar
		 * el m&oacute;dulo de CUADRO DE TURNOS.
		 * 	 
		 * Inc. 452
		 * 
		 * @author Diana Ruiz
		 * @since 13/07/2011  
		 * 
		 */
		
		return new ResultadoBoolean(false);
	  
		/* try
		{	    	
	    	PreparedStatementDecorator estaEnfermeraEnCategoriaSt=new PreparedStatementDecorator(con, nombreCategoriaEnfermneraStr);
			estaEnfermeraEnCategoriaSt.setInt(1,codigoProfesional);			
			ResultSet resultado=estaEnfermeraEnCategoriaSt.executeQuery();
			if(resultado.next())
			{
				return new ResultadoBoolean(true, resultado.getString("nombre"));
			}
			else
			{
				return new ResultadoBoolean(false);
			}
		} catch (SQLException e)
		{
			logger.error("Error obteniendo si existe una persona: "+e);
			return null;
		}	*/    
	}
	
	/**
	 * Método que retorna un Collection los datos de las personas que cumplen con las
	 * condificiones de una búsqueda avanzada
	 * @param con
	 * @param codigo
	 * @param nombreProfesional
	 * @param codigoCategoria
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarEnfermerasAvanzada(Connection con, int codigo, String nombreProfesional, int codigoCategoria) 
	{
		String consulta=enfermerasBusAvanzadaStr;
		if (codigo!=0)
		{
			consulta+=" AND per.codigo="+codigo;
		}
		if (!nombreProfesional.equals(""))
		{
			consulta+=" AND UPPER(getnombrepersonasies(per.codigo)) LIKE UPPER('%"+nombreProfesional+"%')";
		}
		if(codigoCategoria>0)
		{
			consulta+=" AND cat.cat_identificador="+codigoCategoria;
		}		
		consulta+=" ORDER BY cat.cat_identificador";	
		try
		{
			PreparedStatementDecorator consultarEnfermerasAvanzadaStatement = new PreparedStatementDecorator(con, consulta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarEnfermerasAvanzadaStatement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error haciendo la búsqueda avanzada de personas "+e);
			return null;
		}
	}

	/**
	 * (Juan David)
	 * Método para consultar si existen turnos generados al momento de cancelar
	 * una asociación
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoPersona
	 * @return
	 */
	public static boolean existenTurnosGenerados(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int codigoPersona)
	{
		try
		{
			String consultaStr="";
			
			/*System.out.println("fechaInicio "+fechaInicio);
			System.out.println("codigoCategoria "+codigoCategoria);
			System.out.println("codigoPersona "+codigoPersona);*/
			if(fechaInicio==null || fechaInicio.equals(""))
			{
				fechaInicio=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			}
			if(fechaFin==null || fechaFin.equals(""))
			{
				consultaStr=existenTurnosGeneradosNoFechaFinStr;
			}
			else
			{
				consultaStr=existenTurnosGeneradosFechaFinStr;
			}
			
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultaStr);
			stm.setInt(1, codigoCategoria);
			stm.setInt(2, codigoPersona);
			stm.setString(3, fechaInicio);
			if(fechaFin!=null && !fechaFin.equals(""))
			{
				stm.setString(4, fechaFin);
			}
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si existen turnos e categoría"+e);
		}
		return false;
	}

	/**
	 * Método para actualizar la fecha de finalización de una asignación de persona
	 * @param con conexión con la BD
	 * @param codigoPersona Código de la persona que se desea actualizar
	 * @param codigoCategoria Código de la categoría
	 * @param fechaFinAsociacion fecha actual de finalización
	 * @param limite Fecha a la cual se desea actualizar la fecha fin
	 * @return número de registros actualizados en la BD
	 */
	public static int actualizarFechaFin(Connection con, int codigoPersona, int codigoCategoria, String fechaFinAsociacion, String limite)
	{
		String actualizarFechaFinStr="UPDATE categoria_enfermera SET fecha_fin=? WHERE codigo_medico=? AND cat_identificador=? AND fecha_fin=?";
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, actualizarFechaFinStr);
			if(limite==null)
			{
				stm.setNull(1, Types.NULL);
			}
			else
			{
				stm.setString(1, UtilidadFecha.conversionFormatoFechaABD(limite));
			}
			stm.setInt(2, codigoPersona);
			stm.setInt(3, codigoCategoria);
			stm.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaFinAsociacion));
			return stm.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error actualizando la fecha de finalización para la persona "+codigoPersona+" en la categoría "+codigoCategoria+": "+e);
			return -1;
		}
	}

	/**
	 * Método para consultar las categorías en las cuales se puede hacer un
	 * cubrimiento de turno
	 * @param con
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarCategoriasDestino(Connection con, String fecha)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarCategoriasDestinoStr);
			stm.setString(1, fecha);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		} catch (SQLException e)
		{
			logger.error("Error listando las categorías destino para cubrir turno en la fecha "+fecha+": "+e);
			return null;
		}
	}


	/**
	 * Método para consultar el nombre de una categoría dado esu código
	 * @param con Conexión con la base de datos
	 * @param codigoCategoria Código de la categoría a consultar
	 */
	public static Collection<HashMap<String, Object>> consultar(Connection con, int codigoCategoria)
	{
		String consultarCategoriaStr="SELECT cat_identificador AS cat_identificador, cat_nombre AS cat_nombre, cat_descripcion AS cat_descripcion, cat_activo AS cat_activo, centro_costo AS centro_costo, color AS color FROM categoria WHERE cat_identificador=?";
		PreparedStatementDecorator statement;
		try
		{
			statement = new PreparedStatementDecorator(con, consultarCategoriaStr);
			statement.setInt(1, codigoCategoria);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(statement.executeQuery()));
		} catch (SQLException e)
		{
			logger.error("Error consultando la categoría "+codigoCategoria+": "+e);
		}
		return null;
	}

}
