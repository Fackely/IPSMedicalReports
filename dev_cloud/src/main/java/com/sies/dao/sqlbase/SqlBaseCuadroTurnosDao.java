/*
 * Creado en  28/06/2005
 * Creado por Karenth Yuliana Marín Vásquez
 *
 * Juan David Ramírez
 * Andrés Arias López
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.SiEsConstantes;
import com.sies.mundo.SiEsSecuencias;
import com.sies.mundo.UtilidadSiEs;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;


/**
 * @author Karenth Yuliana Marín
 * 
 * Creado en 28/06/2005
 * 
 * Modificado el 13/07/2007
 * Juan David Ramírez L.
 * 
 * SiEs
 * Parquesoft Manizales
 */
public class SqlBaseCuadroTurnosDao
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCuadroTurnosDao.class);
	
	/**
	 * String para consultar la última fecha de programacion de cuadros de turnos
	 * para esa categoria
	 */
	private static String consultarFechaUltimaStr="select max(fechafin)AS fechafin from cuadro_turnos where codigocategoria=?";
	
	
	/**
	 * string que consulta la ultima fecha de programación de turnos para esa enfermera
	 */
	private static String consultarFecUltTurEnferStr="select max(ct_fecha) as fechafin from ct_turno where codigomedico=?";
 
	
	/**
	 * String para manejar los ultimos 6 turnos asignados 
	 */
	private static String consultarUltimosTurnosStr="SELECT t.codigoturno AS codigoturno, t.descripcion AS descripcion, t.horadesde AS horadesde, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, t.activo AS activo, tt.por_defecto AS por_defecto, ctt.ct_fecha AS fecha, cat.color AS color_categoria, cat.cat_identificador AS codigo_categoria, cat.cat_nombre AS nombre_categoria FROM turno t INNER JOIN ct_turno ctt ON(ctt.codigoturno=t.codigoturno AND ctt.codigomedico=?) INNER JOIN cuadro_turnos ct ON(ct.ctcodigocuadro=ctt.ctcodigocuadro) INNER JOIN categoria cat ON(ct.codigocategoria=cat.cat_identificador) INNER JOIN tipos_turno tt ON(t.tipo_turno=tt.acronimo) ORDER BY ctt.ct_fecha DESC LIMIT ";
		
	/**
	 * Para consultar todo los turnos que se pueden asignar 
	 */
	private static String consultarTurnosActivosStr="SELECT t.codigoturno AS codigoturno, t.descripcion AS descripcion, t.horadesde AS horadesde, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, t.activo AS activo, tt.por_defecto AS por_defecto, to_char(t.horadesde, 'HH24:MI') AS hora_desde FROM turno t INNER JOIN tipos_turno tt ON(tt.acronimo=t.tipo_turno) WHERE t.activo=? ORDER BY t.simbolo";

	private static String consultarTurnosStr="SELECT t.codigoturno AS codigoturno, t.descripcion AS descripcion, t.horadesde AS horadesde, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, t.activo AS activo, tt.por_defecto AS por_defecto, to_char(t.horadesde, 'HH24:MI') AS hora_desde FROM turno t INNER JOIN tipos_turno tt ON(tt.acronimo=t.tipo_turno) ORDER BY t.simbolo";

	/**
	 * Para consultar todo los turnos que se pueden asignar 
	 */
	private static String consultarTurnosActivosCentroCostoStr="SELECT t.codigoturno AS codigoturno, t.descripcion AS descripcion, t.horadesde AS horadesde, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, t.activo AS activo, tt.por_defecto AS por_defecto, to_char(t.horadesde, 'HH24:MI') AS hora_desde FROM turno t INNER JOIN tipos_turno tt ON(tt.acronimo=t.tipo_turno) LEFT OUTER JOIN turno_centro_costo tcc ON(tcc.codigo_turno=t.codigoturno) WHERE t.activo=? AND (tcc.centro_costo=? OR tcc.centro_costo IS NULL) ORDER BY t.simbolo";

	private static String consultarTurnosCentroCostoStr="SELECT t.codigoturno AS codigoturno, t.descripcion AS descripcion, t.horadesde AS horadesde, t.numero_horas AS numero_horas, t.simbolo AS simbolo, t.tipo_turno AS tipo_turno, t.color_letra AS color_letra, t.color_fondo AS color_fondo, t.es_festivo AS es_festivo, t.activo AS activo, tt.por_defecto AS por_defecto, to_char(t.horadesde, 'HH24:MI') AS hora_desde FROM turno t INNER JOIN tipos_turno tt ON(tt.acronimo=t.tipo_turno) LEFT OUTER JOIN turno_centro_costo tcc ON(tcc.codigo_turno=t.codigoturno) WHERE tcc.centro_costo=? OR tcc.centro_costo IS NULL ORDER BY t.simbolo";

	/**
	 * String para insertan un nueva turno a una enfermera;
	 */
	private static String insertarTurnoStr="INSERT INTO ct_turno(ctcodigo, ctcodigocuadro, codigomedico, ct_fecha, codigoturno) values (?,?,?,?,?)";

	/**
	 * Consultar el código del turno
	 */
	private static String consultarTurnoStr="SELECT ctcodigo AS codigo FROM ct_turno WHERE ctcodigocuadro=? AND codigomedico=? AND ct_fecha=?";

	/**
	 * String para insertar un nuevo cubrimiento de turno a una persona;
	 */
	private static String insertarCubrirTurnoStr = "INSERT INTO ct_cubrir_turno(ctcodigo, codigomedico, ct_fecha, codigoturno, categoria) values (?,?,?,?,?)";

	/**
	 * String para insertar un nuevo cubrimiento de turno a una persona;
	 */
	private static String actualizarCubrirTurnoStr = "UPDATE ct_cubrir_turno SET categoria=? WHERE ctcodigo=?";

	private static String eliminarCubrirTurnoStr = "DELETE FROM ct_cubrir_turno WHERE ctcodigo=?";
	
	/**
	 * SQL para verificar la existencia de un cubrimiento de turno
	 */
	private static String existeCubrimientoStr = "SELECT count(1) AS numRasultados FROM ct_cubrir_turno WHERE ctcodigo=?";

	/**
	 *  String para actualizar la información de un turno
	 */
	private static String actualizarTurnoStr="UPDATE ct_turno set ctcodigocuadro=?, codigoTurno=? WHERE ctcodigo=? " ;
	
	/**
	 * String para consultar si una enfermera tiene asignado un turno en la fecha dada 
	 */
	private static String consultarTurnoFechaStr="SELECT ctcodigo AS codigo FROM ct_turno WHERE codigomedico=? and ct_fecha=? ";
	
	/**
	 * String para insertar en la base de datos los 
	 */
	private static String insertarCuadroStr="INSERT INTO cuadro_turnos(ctcodigocuadro, fechainicio, fechafin, codigocategoria) VALUES (?,?,?,?)";
	
	/**
	 * (Juan David)
	 * String para conservar las restricciones de numeros de personas para las que fue generado el cuadro
	 */
	private static String insertarRestriccionesCuadroStr="INSERT INTO restricciones_cuadro (cuadro, restriccion, valor) VALUES(?, ?, ?)";
	
	private static String consultarTurnoCuadroStr=
		"SELECT ctcodigo as codigo, ctcodigocuadro as codigoCuadro, codigomedico as codigoMedico, " +
		"ct_fecha as fecha, ct_turno.codigoturno as codigoTurno, simbolo as simboloTurno, ctobservacion as observacion, libres as libres " +
		"from ct_turno, turno where ctcodigo = ? and ct_turno.codigoturno = turno.codigoturno";
	
	/**
	 * String para consultar el codigo del cuadro de turno que esta ejecutando en la fecha actual 
	 */
	private static String consultarCodigoCuadroActualCatStr="" +
			"SELECT " +
				"ctcodigocuadro AS codigocuadro, " +
				"fechainicio AS fechainicio, " +
				"fechafin AS fechafin " +
			"FROM cuadro_turnos " +
				"WHERE codigocategoria=?";
		
	/**
	 * String para hacer la actualizción de uin turno en específico, ya sea por cambio de turno
	 * o por observacion o ambas	 * 
	 */
	private static String actualizarTurnoObservacionStr="";
		
	private static String consultarTurnosReporteObPersonaStr=
		"SELECT " +
			"getnombrepersonasies(per.codigo) AS nombre, " +
			"t.simbolo AS turno, " +
			"to_char(ct_t.ct_fecha, 'DD/MM/YYYY') AS fecha, " +
			"cto.ctobservacion AS observacionper, " +
			"tipo.descripcion AS descripobserva " +
		"FROM personas per " +
		"INNER JOIN " +
			"ct_turno ct_t ON(ct_t.codigomedico=per.codigo) " +
		"INNER JOIN " +
			"turno t ON(t.codigoturno=ct_t.codigoturno) " +
		"INNER JOIN " +
			"ct_turno_observacion cto ON(cto.ct_turno=ct_t.ctcodigo) " +
		"INNER JOIN " +
			"tipo_observacion tipo ON(tipo.codigo=cto.tipo_observacion) " +
		"INNER JOIN " +
			"categoria_enfermera ce " +
				"ON(ce.codigo_medico=per.codigo) " +
		"INNER JOIN " +
			"categoria ca ON (ca.cat_identificador=ce.cat_identificador) " +
		"WHERE " +
			"ca.cat_identificador = ? AND (ct_t.ct_fecha BETWEEN ? AND ?) " +
		"GROUP BY " +
			"per.codigo, " +
			"t.simbolo, " +
			"ct_t.ct_fecha, " +
			"cto.ctobservacion, " +
			"tipo.descripcion " +
		"ORDER BY descripobserva, nombre, observacionper";
			
	
	/**
	 * String para manejo del reporte de Observaciones
	 */
	private static String consultarTurnosReporteObStr=
		//SELECT ca.cat_nombre AS nombre from categoria ca INNER JOIN categoria_enfermera ce 
		//ON(ce.cat_identificador=ca.cat_identificador) WHERE ca.cat_identificador=1 GROUP BY nombre;
		"SELECT " +
			"per.numero_identificacion AS cedula, " +
			"per.codigo AS codigo, " +
			"getnombrepersonasies(per.codigo) AS nombre, " +
			"t.simbolo AS turno, " +
			"to_char(ct_t.ct_fecha, 'DD/MM/YYYY') AS fecha, " +
			"cto.ctobservacion AS observacion, " +
			"tipo.descripcion AS descripobserva, " +
			"ca.cat_nombre AS nomcate "+
		"FROM personas per "+
		"INNER JOIN " +
			"ct_turno ct_t ON(ct_t.codigomedico=per.codigo) " +
		"INNER JOIN " +
			"ct_turno_observacion cto ON(cto.ct_turno=ct_t.ctcodigo) " +
		"INNER JOIN " +
			"turno t ON(t.codigoturno=ct_t.codigoturno) " +
		"INNER JOIN " +
			"tipo_observacion tipo ON(tipo.codigo=cto.tipo_observacion) " +
		"INNER JOIN " +
			"usuarios usu ON (usu.codigo_persona=per.codigo) " +
		"INNER JOIN " +
			"categoria_enfermera ce " +
				"ON(ce.codigo_medico=per.codigo AND ?>=ce.fecha_inicio AND (ce.fecha_fin IS NULL OR ?<=ce.fecha_fin)) " +
		"INNER JOIN " +
		 	"categoria ca ON (ca.cat_identificador=ce.cat_identificador)" +
		"WHERE " +
			"(ct_t.ct_fecha BETWEEN ? AND ?) " +
		"AND " +
			"usu.login NOT IN " +
			"(" +
				"SELECT login " +
				"FROM usuarios_inactivos" +
			") "+
		"GROUP BY " +
			"per.codigo, " +
			"per.numero_identificacion, " +
			"ct_t.ct_fecha, " +
			"cto.ctobservacion, " +
			"cto.tipo_observacion, " +
			"t.simbolo, " +
			"tipo.descripcion, "+
			"ca.cat_nombre " +
		"ORDER BY nombre, descripobserva";		

	/**
	 *	Modificado por Andrés Arias López
	 */
	private static String consultarTurnosCategoriaStr=
		"SELECT " +
			"t.ctcodigo AS ctcodigo, " +
			"t.codigomedico AS codigomedico, " +
			"UPPER(getnombrepersonasies(t.codigomedico)) AS nombre, " +
			"t.ct_fecha AS fecha, " +
			"t.codigoturno AS codigoturno, " +
			"t.ctcodigocuadro AS codigo_cuadro, " +
			"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=t.ctcodigo) AS num_observaciones, " +
			"cat.color AS color, " +
			"cat.cat_nombre AS nombre_categoria, " +
			"cat.cat_identificador AS codigo_categoria " +
		"FROM " +
				"ct_turno t " +
			"INNER JOIN " +
				"cuadro_turnos c " +
				"ON (t.ctcodigocuadro=c.ctcodigocuadro) " +
			"LEFT OUTER JOIN " +
				"ct_cubrir_turno ctct " +
				"ON(ctct.ctcodigo=t.ctcodigo) " +
			"LEFT OUTER JOIN " +
				"categoria cat " +
			"ON(cat.cat_identificador=ctct.categoria) ";
	
	private static String consultarTurnosCubiertosCategoriaStr=
		"SELECT " +
			"t.ctcodigo AS ctcodigo, " +
			"t.codigomedico AS codigomedico, " +
			"UPPER(getnombrepersonasies(t.codigomedico)) AS nombre, " +
			"t.ct_fecha AS fecha, " +
			"t.codigoturno AS codigoturno, " +
			"null AS codigo_cuadro, " +
			"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=t.ctcodigo) AS num_observaciones, " +
			"null AS color, " +
			"null AS nombre_categoria, " +
			"null AS codigo_categoria " +
		"FROM " +
			"ct_cubrir_turno t " +
		"INNER JOIN " +
			"categoria cat " +
			"ON(cat.cat_identificador=t.categoria) ";

	private static String consultarObservacionesTurnoStr=
		"SELECT " +
			"o.codigo AS codigo, " +
			"cto.descripcion AS tipo_observacion, " +
			"o.tipo_observacion AS codigo_tipo_observacion, " +
			"o.ctobservacion AS observacion, " +
			"o.fecha AS fecha, " +
			"to_char(o.hora, 'HH24:MI') AS hora, " +
			"getnombreusuariosies(o.usuario) AS usuario," +
			"ctn.tipo_novedad AS tipo_novedad, " +
			"nov.nombre_novedad AS novedad " +
		"FROM " +
				"ct_turno_observacion o " +
			"INNER JOIN " +
				"tipo_observacion cto " +
				"ON (cto.codigo=o.tipo_observacion) " +
			"LEFT OUTER JOIN " +
				"ct_turno_novedad ctn " +
				"ON(ctn.codigo_observacion=o.codigo) " +
			"LEFT OUTER JOIN " +
				"novedad nov " +
				"ON(nov.codigo_novedad=ctn.tipo_novedad) " +
		"WHERE " +
			"o.ct_turno=? " +
		"ORDER BY o.fecha DESC, o.hora DESC";

	/**
	 * Sentencia para ingresar los tipos de novedad que no han sido programados
	 * antes de la generación de lcuadro de turnos
	 */
	private static String ingresarNovedadStr="INSERT INTO ct_turno_novedad (codigo_observacion, tipo_novedad) VALUES (?,?)";

	/**
	 * Consultar personas que pertenecen a un cuadro de turnos específico discriminado por fechas
	 */
	private static String consultarPersonasCuadroPorFechas=
			"SELECT " +
				"t.codigomedico AS codigo, " +
				"UPPER(per.primer_nombre ||   CASE   WHEN   per.segundo_nombre IS NOT NULL AND per.segundo_nombre!=''   THEN   ' ' || per.segundo_nombre   ELSE   ''   END ||   ' ' || per.primer_apellido ||   CASE   WHEN   per.segundo_apellido IS NOT NULL AND per.segundo_apellido!=''   THEN   ' ' || per.segundo_apellido   ELSE   ''   END) AS nombre," +
				"true AS permitir_modificar, " +
				"per.telefono AS telefono " +
				//"hf.numero_horas AS horas_faltantes, " +
				//"hf.numero_horas_anterior AS numero_horas_anterior " +
			"FROM " +
				"ct_turno t " +
				"INNER JOIN " +
					"cuadro_turnos ct " +
					"ON (t.ctcodigocuadro=ct.ctcodigocuadro) " +
				"INNER JOIN " +
					"personas per " +
					"ON (per.codigo=t.codigomedico) " +
				//"INNER JOIN " +
				//	"horas_faltantes hf " +
				//	"ON(hf.codigo_persona=per.codigo) " +
			"WHERE " +
				"ct.codigocategoria=? " +
			"AND " +
				"t.ct_fecha " +
			"BETWEEN ? AND ? " +
			//"GROUP BY codigo, nombre, t.codigomedico, per.telefono, hf.numero_horas, hf.numero_horas_anterior " +
			"GROUP BY codigo, nombre, t.codigomedico, per.telefono " +
		"UNION " +
			"SELECT " +
				"t.codigomedico AS codigo, " +
				"UPPER(per.primer_nombre ||   CASE   WHEN   per.segundo_nombre IS NOT NULL AND per.segundo_nombre!=''   THEN   ' ' || per.segundo_nombre   ELSE   ''   END ||   ' ' || per.primer_apellido ||   CASE   WHEN   per.segundo_apellido IS NOT NULL AND per.segundo_apellido!=''   THEN   ' ' || per.segundo_apellido   ELSE   ''   END) AS nombre, " +
				"false AS permitir_modificar," +
				"per.telefono AS telefono " +
				//"hf.numero_horas AS numero_horas, " +
				//"hf.numero_horas_anterior AS numero_horas_anterior " +
			"FROM " +
				"ct_cubrir_turno t " +
				"INNER JOIN " +
					"personas per " +
					"ON (per.codigo=t.codigomedico) " +
				//"INNER JOIN " +
				//	"horas_faltantes hf " +
				//	"ON(hf.codigo_persona=per.codigo) " +
			"WHERE " +
				"t.categoria=? " +
			"AND " +
				"t.ct_fecha " +
			"BETWEEN ? AND ? " +
			//"GROUP BY codigo, nombre, t.codigomedico, per.telefono, hf.numero_horas, hf.numero_horas_anterior";
			"GROUP BY codigo, nombre, t.codigomedico, per.telefono";

	/**
	 * Consultar personas que pertenecen a un cuadro de turnos específico discriminado por fechas
	 */
	private static String consultarPersonasCuadroPorCodigoCuadroStr=
			"SELECT " +
				"per.codigo AS codigo, " +
				"per.numero_identificacion AS id, " +
				"UPPER(getnombrepersonasies(t.codigomedico)) AS nombre " +
			"FROM " +
				"ct_turno t " +
			"INNER JOIN " +
				"personas per ON(per.codigo=t.codigomedico) " +
			"WHERE " +
				"t.ctcodigocuadro=? " +
			"AND " +
				"t.ct_fecha " +
			"BETWEEN ? AND ? " +
			"GROUP BY per.codigo, per.numero_identificacion, UPPER(getnombrepersonasies(t.codigomedico)) " +
		"UNION " +
			"SELECT " +
				"per.codigo AS codigo, "+
				"per.numero_identificacion AS codigo, " +
				"UPPER(getnombrepersonasies(t.codigomedico)) AS nombre " +
			"FROM " +
				"ct_cubrir_turno t " +
			"INNER JOIN " +
				"personas per ON(per.codigo=t.codigomedico) " +
			"WHERE " +
				"t.categoria=(SELECT ctcodigocuadro FROM cuadro_turnos WHERE ctcodigocuadro=?) " +
			"AND " +
				"t.ct_fecha " +
			"BETWEEN ? AND ? " +
			"GROUP BY per.codigo, per.numero_identificacion, UPPER(getnombrepersonasies(t.codigomedico)) ";

	private static String consultarTiposObservacionStr="" +
			"SELECT " +
				"codigo AS codigo, " +
				"descripcion AS descripcion, " +
				"por_defecto AS por_defecto " +
			"FROM " +
				"tipo_observacion";
	
	private static String consultarEnfermeraCategoriaStr=
			"SELECT " +
				"per.codigo AS codigo, " +
				"getnombrepersonasies(per.codigo) AS nombre, " +
				"to_char(ce.fecha_inicio, 'DD/MM/YYYY') AS fecha_inicio, " +
				"to_char(ce.fecha_fin, 'DD/MM/YYYY') AS fecha_fin, " +
				"to_char(ce.fecha_inicio, 'YYYY-MM-DD') AS fecha_inicio_bd, " +
				"to_char(ce.fecha_fin, 'YYYY-MM-DD') AS fecha_fin_bd, " +
				"true AS permitir_modificar, " +
				"per.telefono AS telefono " +
				//"CASE WHEN hf.numero_horas IS NULL THEN 0 ELSE hf.numero_horas END AS horas_faltantes, " +
				//"CASE WHEN hf.numero_horas_anterior IS NULL THEN 0 ELSE hf.numero_horas_anterior END AS numero_horas_anterior " +
			"FROM " +
				"categoria_enfermera ce " +
			"INNER JOIN " +
				"personas per " +
				"ON (per.codigo=ce.codigo_medico) " +
			//"LEFT OUTER JOIN " +
			//	"horas_faltantes hf " +
			//	"ON (hf.codigo_persona=per.codigo) " +
			"WHERE ce.cat_identificador=? " +
			"AND(" +
				"(" +
					"ce.fecha_inicio " +
						"BETWEEN ? AND ? " +
						"AND (ce.fecha_fin IS NULL OR ce.fecha_fin BETWEEN ? AND ?)) OR (ce.fecha_inicio <=? AND (ce.fecha_fin IS NULL OR ce.fecha_fin>=?)))";
	
	/**
	 * Sentencia para consultar las restricciones de cada cuadro de turnos
	 */
	private static final String consultarRestriccionesCuadroStr="SELECT cuadro AS cuadro, restriccion AS codigo, valor AS valor, ct.fechainicio AS fechainicio, ct.fechafin AS fechafin FROM restricciones_cuadro rc INNER JOIN cuadro_turnos ct ON(ct.ctcodigocuadro=rc.cuadro) WHERE ct.codigocategoria=? AND ((? BETWEEN ct.fechainicio AND ct.fechafin) OR (? BETWEEN ct.fechainicio AND ct.fechafin))";
	
	/**
	 * Lista cuadros de turnos en los cuales está contenida una fecha
	 */
	private static final String consultarCuadroTurnosDadaUnaFechaStr="SELECT ctcodigocuadro AS codigo, to_char(fechainicio, 'dd/mm/yyyy') AS fecha_inicio, to_char(fechafin, 'dd/mm/yyyy') AS fecha_fin, codigocategoria AS codigo_categoria, cat_nombre AS nombre_categoria FROM cuadro_turnos ct INNER JOIN categoria cat ON(ct.codigocategoria=cat.cat_identificador) WHERE ? BETWEEN fechainicio AND fechafin ORDER BY cat.cat_nombre";
	private static final String consultarCuadroTurnosDadaUnaFechaCentroCostoStr="SELECT ctcodigocuadro AS codigo, to_char(fechainicio, 'dd/mm/yyyy') AS fecha_inicio, to_char(fechafin, 'dd/mm/yyyy') AS fecha_fin, codigocategoria AS codigo_categoria, cat_nombre AS nombre_categoria FROM cuadro_turnos ct INNER JOIN categoria cat ON(ct.codigocategoria=cat.cat_identificador) WHERE ? BETWEEN fechainicio AND fechafin AND cat.centro_costo=? ORDER BY cat.cat_nombre";

	/**
	 * Lista de cuadros de turnos que serán eliminados
	 */
	private static final String consultarCuadroTurnosEliminarCentroCostoStr="SELECT ctcodigocuadro AS codigo, to_char(fechainicio, 'dd/mm/yyyy') AS fecha_inicio, to_char(fechafin, 'dd/mm/yyyy') AS fecha_fin, codigocategoria AS codigo_categoria, cat_nombre AS nombre_categoria FROM cuadro_turnos ct INNER JOIN categoria cat ON(ct.codigocategoria=cat.cat_identificador) WHERE ? BETWEEN fechainicio AND fechafin AND codigocategoria NOT IN (SELECT codigocategoria FROM cuadro_turnos WHERE fechainicio>?) AND cat.centro_costo=? ORDER BY cat.cat_nombre";
	private static final String consultarCuadroTurnosEliminarStr="SELECT ctcodigocuadro AS codigo, to_char(fechainicio, 'dd/mm/yyyy') AS fecha_inicio, to_char(fechafin, 'dd/mm/yyyy') AS fecha_fin, codigocategoria AS codigo_categoria, cat_nombre AS nombre_categoria FROM cuadro_turnos ct INNER JOIN categoria cat ON(ct.codigocategoria=cat.cat_identificador) WHERE ? BETWEEN fechainicio AND fechafin AND codigocategoria NOT IN (SELECT codigocategoria FROM cuadro_turnos WHERE fechainicio>?) ORDER BY cat.cat_nombre";
	
	/**
	 * Eliminar cuadro de turnos
	 */
	private static final String eliminarCuadroStr="DELETE FROM cuadro_turnos WHERE ctcodigocuadro=?";
	private static final String eliminarRestriccionesCuadroStr="DELETE FROM restricciones_cuadro WHERE cuadro=?";
	private static final String eliminarTurnosCuadroTurnosStr="DELETE FROM ct_turno_general WHERE ctcodigo IN(SELECT ctcodigo FROM ct_turno WHERE ctcodigocuadro=?)";
	private static final String eliminarObservacionesCuadroTurnosStr="DELETE FROM ct_turno_observacion WHERE ct_turno IN(SELECT ctcodigo FROM ct_turno WHERE ctcodigocuadro=?)";
	private static final String eliminarNovedadesCuadroTurnosStr="DELETE FROM ct_turno_novedad WHERE codigo_observacion IN(SELECT codigo FROM ct_turno_observacion WHERE ct_turno IN(SELECT ctcodigo FROM ct_turno WHERE ctcodigocuadro=?))";
	private static final String eliminarHorasTurnosStr="DELETE FROM ct_turno_horas WHERE ct_turno_pk IN(SELECT ctcodigo FROM ct_turno WHERE ctcodigocuadro=?)";
	
	/**
	 * No se!! -- eso es de karenth
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ResultSet consultarTurnoCuadro(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator consultarSt;
			consultarSt = new PreparedStatementDecorator(con, consultarTurnoCuadroStr);
			consultarSt.setInt(1, codigo);
			return consultarSt.executeQuery();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando turno de enfermera "+e);
		}
		return null;
	}
	
	/**
	 * Método que permite la consulta de la fecha de finalización de la ltima programación
	 * realizada para esa categoría
	 * @param con
	 * @param codigoCategoria
	 * @return
	 */
	public static Collection consultarFechaUltima(Connection con, int codigoCategoria) 
	{
		try
		{
			PreparedStatementDecorator consultarFechaUltimaSt;
			consultarFechaUltimaSt = new PreparedStatementDecorator(con, consultarFechaUltimaStr);
			//System.out.println("consultarFechaUltimaStr "+consultarFechaUltimaStr);
			//System.out.println("codigoCategoria "+codigoCategoria);
			consultarFechaUltimaSt.setInt(1,codigoCategoria);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarFechaUltimaSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la fecha de la última programación de cuadros "+e);
		}
		return null;
	}
	
	/**
	 * Metodo que consulta la fecha de finalización de la última programacion de 
	 * realizada para esta enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public static Collection consulFechaUltEnf(Connection con, int codigoEnfermera)
	{
		try {
			PreparedStatementDecorator consulFechaUltEnfSt;
			consulFechaUltEnfSt = new PreparedStatementDecorator(con, consultarFecUltTurEnferStr);
			consulFechaUltEnfSt.setInt(1,codigoEnfermera);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulFechaUltEnfSt.executeQuery()));
		} catch (SQLException e) {
			logger.error("Error consultando la fecha de la última programacion de cuadros de la Enfermera "+e);
			
		}
		return null;
	}
	
	
	/**
	 * Metodo que consulta los ultimos dos turnos que tiene asignada una enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @param limiteTurnos Cuantos turnos anteriores
	 * @return
	 */
	public static Collection consultarUltimosTurnos(Connection con, int codigoEnfermera, int limiteTurnos)
	{
		
		try {
			String consulta=consultarUltimosTurnosStr+limiteTurnos;
			PreparedStatementDecorator consultarUltimosTurnosSt;
			consultarUltimosTurnosSt=new PreparedStatementDecorator(con, consulta);
			//System.out.println("consultarUltimosTurnosStr "+consulta);
			//System.out.println("codigoEnfermera "+codigoEnfermera);
			consultarUltimosTurnosSt.setInt(1, codigoEnfermera);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarUltimosTurnosSt.executeQuery()));
		} catch (SQLException e) {
			logger.error("No se pueden consultar los dos turnos anteriores "+e);
		   
		}
		return null;
		
	}
	
	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @return Listado de turnos existentes en BD
	 */
	public static Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos)
	{
		try
		{
			PreparedStatementDecorator consultarTurnosSt;
			if(activos)
			{
				consultarTurnosSt=new PreparedStatementDecorator(con, consultarTurnosActivosStr);
				consultarTurnosSt.setBoolean(1, true);
			}
			else
			{
				consultarTurnosSt=new PreparedStatementDecorator(con, consultarTurnosStr);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los turnos "+e);
		}
		return null;
	} 

	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @param centroCosto Código del centro de costo por el cual se desea filtrar
	 * @return Listado de turnos existentes en BD
	 */
	public static Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos, Integer centroCosto)
	{
		try
		{
			PreparedStatementDecorator consultarTurnosSt;
			if(activos)
			{
				consultarTurnosSt=new PreparedStatementDecorator(con, consultarTurnosActivosCentroCostoStr);
				consultarTurnosSt.setBoolean(1, true);
				consultarTurnosSt.setInt(2, centroCosto);
			}
			else
			{
				consultarTurnosSt=new PreparedStatementDecorator(con, consultarTurnosCentroCostoStr);
				consultarTurnosSt.setInt(1, centroCosto);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los turnos "+e);
		}
		return null;
	} 

	/**
	 * Método que retorna el código de la asociación de persona-fecha de la tabla ct_turno
	 * @param con
	 * @param fecha
	 * @param codigoEnfermera
	 * @return
	 */
	public static Collection consultarFechaEnf(Connection con, String fecha, int codigoEnfermera)
	{ 
		PreparedStatementDecorator consultarFechaEnfSt;
		try {
			consultarFechaEnfSt=new PreparedStatementDecorator(con, consultarTurnoFechaStr);
			consultarFechaEnfSt.setInt(1, codigoEnfermera);
			consultarFechaEnfSt.setString(2,UtilidadFecha.conversionFormatoFechaABD(fecha));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarFechaEnfSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error(" Error consultando el turno de una persona en una fecha "+e);
		}
		return null;
		
	}
	
	/**
	 * Metodo que inserta un turno en la base de datos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @return Codigo secuencial del registro ingresado, -1 en caso de error
	 */
	public static int insertarTurno(Connection con, int codigoCT, String fecha, int codigoEnfermera, int codigoTurno)
	{
		PreparedStatementDecorator insertarTurnoSt;
		
		try
		{
			int codigo=UtilidadSiEs.obtenerSiguienteValorSecuencia(con, SiEsSecuencias.NOMBRE_SECUENCIA_TURNOS_ASIGNADOS_CUADRO);
			insertarTurnoSt=new PreparedStatementDecorator(con, insertarTurnoStr);
			insertarTurnoSt.setInt(1, codigo);
			insertarTurnoSt.setInt(2, codigoCT);
			insertarTurnoSt.setInt(3, codigoEnfermera);
			insertarTurnoSt.setString(4, fecha);
			insertarTurnoSt.setInt(5, codigoTurno);
			if(insertarTurnoSt.executeUpdate()<=0)
			{
				return -1;
			}
			return codigo;
			
			
		} catch (SQLException e) {
			logger.error("Error insertando turno ",e);
			return -1;
		   
		}
	 	   
	}

	/**
	 * Método que inserta un cubrimiento de turno en la base de datos
	 * @param con
	 * @param codigoRegistro
	 * @param codigoCategoria
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @param eliminar
	 * @return Código secuencial del registro ingresado, -1 en caso de error
	 */
	public static int procesarCubrimientoTurno(Connection con, int codigoRegistro, int codigoCategoria, String fecha, int codigoEnfermera, int codigoTurno, boolean eliminar)
	{
		try
		{
			PreparedStatementDecorator procesarCubrimientoTurno;
			if(existeCubrimientoTurno(con, codigoRegistro))
			{
				if(eliminar)
				{
					procesarCubrimientoTurno=new PreparedStatementDecorator(con, eliminarCubrirTurnoStr);
					procesarCubrimientoTurno.setInt(1, codigoRegistro);
					return procesarCubrimientoTurno.executeUpdate();
				}
				else
				{
					procesarCubrimientoTurno=new PreparedStatementDecorator(con, actualizarCubrirTurnoStr);
					procesarCubrimientoTurno.setInt(1, codigoCategoria);
					procesarCubrimientoTurno.setInt(2, codigoRegistro);
					return procesarCubrimientoTurno.executeUpdate();
				}
			}
			else
			{
				procesarCubrimientoTurno=new PreparedStatementDecorator(con, insertarCubrirTurnoStr);
				procesarCubrimientoTurno.setInt(1, codigoRegistro);
				procesarCubrimientoTurno.setInt(2, codigoEnfermera);
				procesarCubrimientoTurno.setString(3, fecha);
				procesarCubrimientoTurno.setInt(4, codigoTurno);
				procesarCubrimientoTurno.setInt(5, codigoCategoria);
				return procesarCubrimientoTurno.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			logger.error("Error insertando cubrimiento turno "+e);
			return -1;
		}
	}

	/**
	 * Método que verifica si existe un cubrimiento de turno para
	 * un código de registro específico
	 * @param codigoRegistro
	 * @return true en caso de existir, false en caso contrario o en caso de error.
	 */
	private static boolean existeCubrimientoTurno(Connection con, int codigoRegistro)
	{
		PreparedStatementDecorator procesarCubrimientoTurno;
		try
		{
			procesarCubrimientoTurno = new PreparedStatementDecorator(con, existeCubrimientoStr);
			procesarCubrimientoTurno.setInt(1, codigoRegistro);
			ResultSet resultado=procesarCubrimientoTurno.executeQuery();
			if(resultado.next())
			{
				if(resultado.getInt("numRasultados")>0)
				{
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de un cubrimiento de turno "+e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método que inserta en la base de datos un cuadro de turnos
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @param restriccionesCategoria
	 * @return
	 */
	public static int insertarCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria, HashMap<String, Object> restriccionesCategoria)
	{
		PreparedStatementDecorator insertarCuadroSt;
		try
		{
			int codigoCuadro=UtilidadSiEs.obtenerSiguienteValorSecuencia(con, SiEsSecuencias.NOMBRE_SECUENCIA_CUADRO_TURNOS);
			insertarCuadroSt=new PreparedStatementDecorator(con, insertarCuadroStr);
			insertarCuadroSt.setInt(1,codigoCuadro);
			insertarCuadroSt.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			insertarCuadroSt.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			insertarCuadroSt.setInt(4,codigoCategoria);
			if(insertarCuadroSt.executeUpdate()<=0)
			{
				return -1;
			}
			else
			{
				insertarCuadroSt=new PreparedStatementDecorator(con, insertarRestriccionesCuadroStr);
				insertarCuadroSt.setInt(1, codigoCuadro);
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionMinM);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionMinM)+""));
				insertarCuadroSt.executeUpdate();
				
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionMinT);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionMinT)+""));
				insertarCuadroSt.executeUpdate();
				
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionesEnfN);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionesEnfN)+""));
				insertarCuadroSt.executeUpdate();
				
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionMinMFestivo);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionMinMFestivo)+""));
				insertarCuadroSt.executeUpdate();
				
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionMinTFestivo);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionMinTFestivo)+""));
				insertarCuadroSt.executeUpdate();
				
				insertarCuadroSt.setInt(2, SiEsConstantes.codigoRestriccionMinNFestivo);
				insertarCuadroSt.setInt(3, Integer.parseInt(restriccionesCategoria.get("valor_"+SiEsConstantes.codigoRestriccionMinNFestivo)+""));
				insertarCuadroSt.executeUpdate();
			}
			return codigoCuadro;
			
		}
		catch (SQLException e)
		{
			logger.error("Error insertando el cuadro de Turnos "+e);
		}
		
		return 0;
		
	}
	
	/**
	 * (Juan David)
	 * Método que actualiza el valor de un turno
	 * @param con
	 * @param codigoCuadro
	 * @param codigoTurno
	 * @param codigoCtTurno
	 * @return Número de registros actualizados, 0 en caso de error.
	 */
	public static int actualizarTurno(Connection con, int codigoCuadro, int codigoTurno, int codigoCtTurno )
	{
		PreparedStatementDecorator actualizarTurnoSt;
		try
		{
			if(existeCubrimientoTurno(con, codigoCtTurno))
			{
				actualizarTurnoSt=new PreparedStatementDecorator(con, eliminarCubrirTurnoStr);
				actualizarTurnoSt.setInt(1, codigoCtTurno);
				int res=actualizarTurnoSt.executeUpdate();
				if(res<1)
				{
					logger.error("Error eliminando el turno cubierto para el codigo "+codigoCtTurno);
					return 0;
				}
			}
			actualizarTurnoSt=new PreparedStatementDecorator(con, actualizarTurnoStr);
			actualizarTurnoSt.setInt(1, codigoCuadro );
			actualizarTurnoSt.setInt(2, codigoTurno);
			actualizarTurnoSt.setInt(3, codigoCtTurno);
			return actualizarTurnoSt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error Actualizando el turno "+e);
			return 0;
		}
	}
	
	/**
	 * (Juan David)
	 * Método que consulta el código de cuadro de turnos para una
	 * categoría y una fecha específica
	 * @param con
	 * @param codigoCategoria
	 * @param fecha
	 * @return
	 */
	public static Collection consultarCodigoCuadro(Connection con, int codigoCategoria, String fecha)
	{
		try
		{
			String consulta=consultarCodigoCuadroActualCatStr;
			if(fecha==null)
			{
				consulta+=" AND CURRENT_DATE";
			}
			else
			{
				consulta+=" AND ?";
			}
			consulta+=" BETWEEN fechainicio AND fechafin";
			PreparedStatementDecorator consultarCodigoCuadroSt=new PreparedStatementDecorator(con, consulta);
			consultarCodigoCuadroSt.setInt(1, codigoCategoria);
			if(fecha!=null)
			{
				consultarCodigoCuadroSt.setString(2, fecha);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarCodigoCuadroSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el código del cuadro de turnos "+e);
			return null;
		}
	}
	
	/**
	 * (Juan David)
	 * Método que consulta toda la inf referente a un cuadro de turnos especifico
	 * @param con
	 * @param codigocuadro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarTurnosEnfermeras(Connection con, int codigocuadro)
	{
		/*String consultaStr=consultarTurnosCategoriaStr+
				"WHERE c.ctcodigocuadro=? " +
				"ORDER BY nombre, fecha";*/
		String consultaStr="SELECT turnos.ctcodigo AS ctcodigo, "+
		"turnos.codigomedico AS codigomedico, "+
		"getNombrePersonaSies(turnos.codigomedico) AS nombre, "+
		"turnos.ct_fecha AS fecha, "+
		"turnos.codigoturno AS codigoturno, "+
		"turnos.codigocuadro AS codigo_cuadro, "+
		"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=turnos.ctcodigo) AS num_observaciones, " +
		"CASE WHEN(turnos.codigocuadro != ?) THEN cat.color ELSE null END AS color, " +
		"CASE WHEN(turnos.codigocuadro != ?) THEN cat.cat_nombre ELSE null END AS nombre_categoria, " +
		"cat.cat_identificador AS codigo_categoria, " +
		"to_char(ctth.numero_horas, '99') AS horas_modificadas " +
		"FROM ("+

		"SELECT per.codigomedico, ctt.ctcodigo, ctt.ct_fecha, ctt.codigoturno, (SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=(SELECT ctcodigocuadro FROM ct_turno ctturnotempo WHERE ctturnotempo.ctcodigo=ctt.ctcodigo)) AS categoria, ctt.ctcodigocuadro AS codigocuadro FROM (SELECT ctt.codigomedico FROM ct_turno ctt WHERE ctt.ctcodigocuadro=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.categoria=(SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=?)) per INNER JOIN ct_turno ctt USING(codigomedico) WHERE ctt.ctcodigo NOT IN(SELECT ctcodigo FROM (SELECT ctt.codigomedico FROM ct_turno ctt WHERE ctt.ctcodigocuadro=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.categoria=(SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=?)) per INNER JOIN ct_cubrir_turno ctct USING(codigomedico)) "+

		"UNION "+

		"SELECT per.codigomedico, ctct.ctcodigo, ctct.ct_fecha, ctct.codigoturno, ctct.categoria, (SELECT ctcodigocuadro FROM cuadro_turnos cttempo WHERE ctct.ct_fecha BETWEEN cttempo.fechainicio AND cttempo.fechafin AND cttempo.codigocategoria=(SELECT categoria FROM ct_cubrir_turno AS cate WHERE cate.ctcodigo=ctct.ctcodigo)) AS codigocuadro FROM (SELECT ctt.codigomedico FROM ct_turno ctt WHERE ctt.ctcodigocuadro=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.categoria=(SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=?)) per INNER JOIN ct_cubrir_turno ctct USING(codigomedico) "+

		") turnos " +
		"INNER JOIN categoria cat ON(turnos.categoria=cat.cat_identificador) " +
		"LEFT OUTER JOIN " +
			"ct_turno_horas ctth " +
			"ON(ctth.ct_turno_pk=turnos.ctcodigo) " +
		"WHERE "+
			"turnos.ct_fecha BETWEEN (SELECT ct.fechainicio FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=?) AND (SELECT ct.fechafin FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=?) " +
		"ORDER BY nombre, fecha";

		PreparedStatementDecorator consultarTurnosEnfermerasSt;
		try
		{
			/* ESTE ES CARGAR CUADRO */
			/*
			 * SELECT ctt.codigomedico FROM ct_turno ctt WHERE ctt.ctcodigocuadro=1 GROUP BY ctt.codigomedico
			 * SELECT ct.fechainicio, ct.fechafin FROM cuadro_turnos ct
			 */
			consultarTurnosEnfermerasSt=new PreparedStatementDecorator(con, consultaStr);
			consultarTurnosEnfermerasSt.setInt(1, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(2, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(3, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(4, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(5, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(6, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(7, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(8, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(9, codigocuadro);
			consultarTurnosEnfermerasSt.setInt(10, codigocuadro);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosEnfermerasSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error(" Error consultando los turnos de las enfermeras "+e);
		}
		return null;
	}
	
	/**
	 * Metodo que consulta la fecha maxima y minima de los turnos de una enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public static Collection consultarFechasMaximaMinimaTurnosEnfermera(Connection con, int codigoEnfermera)
	{
		String consultarTurnosEnfermeraStr=
			"SELECT to_char(max(ct_fecha), 'DD/MM/YYYY') AS fechamaxima, to_char(min(ct_fecha), 'DD/MM/YYYY') AS fechaminima " +
			"FROM ct_turno WHERE codigomedico=?";
		PreparedStatementDecorator consulta;
		try
		{
			consulta=new PreparedStatementDecorator(con, consultarTurnosEnfermeraStr);
			consulta.setInt(1, codigoEnfermera);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error(" Error consultando los turnos de la enfermera:"+codigoEnfermera+"\n"+e);
		}
		return null;
	}
	
	/**
	 * (Juan David)
	 * Metodo que consulta la fecha maxima y minima de los turnos de una categoria
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarFechasMaximaMinimaTurnosCategoria(Connection con, int codigoCategoria)
	{
		String consultarMaximMinimaStr=
			"SELECT to_char(max(fechafin), 'DD/MM/YYYY') AS fechamaxima, to_char(min(fechainicio), 'DD/MM/YYYY') AS fechaminima " +
			"FROM cuadro_turnos WHERE codigocategoria=?";
		try
		{
			PreparedStatementDecorator consulta=new PreparedStatementDecorator(con, consultarMaximMinimaStr);
			consulta.setInt(1, codigoCategoria);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error(" Error consultando los turnos de la categoria:"+codigoCategoria+"\n"+e);
		}
		return null;
	}

	/**
	 * Método que me consulta estrictamente un turno de una persona
	 * @param con
	 * @param codigoCategoria
	 * @param turnos Códigos de los turnos a Consultar
	 * @return Los datos del turno
	 */
	public static Collection<HashMap<String, Object>> consultarTurnoPersona(Connection con, int codigoCategoria, String turnos)
	{
		String consultaStr=
			"SELECT " +
				"turnos.ctcodigo, " +
				"turnos.codigomedico, " +
				"turnos.nombre, " +
				"turnos.fecha, " +
				"turnos.codigoturno, " +
				"turnos.codigocuadro AS codigo_cuadro, " +
				"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=turnos.ctcodigo) AS num_observaciones, " +
				"CASE WHEN(cat.cat_identificador != ?) THEN cat.color ELSE null END AS color, " +
				"CASE WHEN(cat.cat_identificador != ?) THEN cat.cat_nombre ELSE null END AS nombre_categoria, " +
				"turnos.categoria AS codigo_categoria, " +
				"to_char(ctth.numero_horas, '99') AS horas_modificadas " +
			"FROM" +
				"(" +
					"SELECT ctt.ctcodigo AS ctcodigo, ctt.codigomedico AS codigomedico, getNombrePersonaSies(ctt.codigomedico) AS nombre, ctt.ct_fecha AS fecha, ctt.codigoturno AS codigoturno, ctt.ctcodigocuadro AS codigocuadro, (SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=(SELECT ctcodigocuadro FROM ct_turno ctturnotempo WHERE ctturnotempo.ctcodigo=ctt.ctcodigo)) AS categoria FROM ct_turno ctt WHERE ctt.ctcodigo IN("+turnos+") AND ctt.ctcodigo NOT IN(SELECT ctct.ctcodigo FROM ct_cubrir_turno ctct WHERE ctct.ctcodigo IN("+turnos+")) " +
					"UNION " +
					"SELECT ctct.ctcodigo AS ctcodigo, ctct.codigomedico AS codigomedico, getNombrePersonaSies(ctct.codigomedico) AS nombre, ctct.ct_fecha AS fecha, ctct.codigoturno AS codigoturno, (SELECT ctcodigocuadro FROM cuadro_turnos cttempo WHERE ctct.ct_fecha BETWEEN cttempo.fechainicio AND cttempo.fechafin AND cttempo.codigocategoria=(SELECT categoria FROM ct_cubrir_turno AS cate WHERE cate.ctcodigo=ctct.ctcodigo)) AS codigocuadro, ctct.categoria AS categoria FROM ct_cubrir_turno ctct WHERE ctct.ctcodigo IN("+turnos+")" +
				") turnos " +
			"INNER JOIN categoria cat ON(turnos.categoria=cat.cat_identificador) "+
			"LEFT OUTER JOIN " +
				"ct_turno_horas ctth " +
				"ON(ctth.ct_turno_pk=turnos.ctcodigo)";
		try
		{
			//System.out.println("Consulta "+consultaStr);
			PreparedStatementDecorator consulta=new PreparedStatementDecorator(con, consultaStr);
			consulta.setInt(1, codigoCategoria);
			consulta.setInt(2, codigoCategoria);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		} catch (SQLException e)
		{
			logger.error(" Error consultando el turno "+turnos+" de la categoria:"+codigoCategoria+"\n"+e);
		}
		return null;
	}

	
	/**
	 * (Juan David)
	 * Método que consulta los turnos de una categoria o de una enfermera entre dos fechas dadas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoBusqueda
	 * @param esCategoria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> consultarTurnosPersonaOCategoria(Connection con, String fechaInicial, String fechaFinal, int codigoBusqueda, boolean esCategoria)
	{
		try
		{
			if(esCategoria)
			{
				String consultaStr=""+
					"SELECT DISTINCT turnos.ctcodigo AS ctcodigo, " +
					"turnos.codigomedico AS codigomedico, " +
					//"getNombrePersonaSies(turnos.codigomedico) AS nombre, " +
					"per.primer_nombre || CASE WHEN per.segundo_nombre IS NOT NULL AND per.segundo_nombre!='' THEN ' ' || per.segundo_nombre ELSE '' END || ' ' || per.primer_apellido || CASE WHEN per.segundo_apellido IS NOT NULL AND per.segundo_apellido!='' THEN ' ' || per.segundo_apellido ELSE '' END AS nombre, " +
					"turnos.ct_fecha AS fecha, " +
					"turnos.codigoturno AS codigoturno, " +
					"turnos.codigocuadro AS codigo_cuadro, " +
					//"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=turnos.ctcodigo) AS num_observaciones, " +
					"CASE WHEN cto.codigo IS NOT NULL THEN 1 ELSE 0 END AS num_observaciones, "+
					"CASE WHEN(cat.cat_identificador != ?) THEN cat.color ELSE null END AS color, " +
					"CASE WHEN(cat.cat_identificador != ?) THEN cat.cat_nombre ELSE null END AS nombre_categoria, " +
					"cat.cat_identificador AS codigo_categoria, " +
					"to_char(ctth.numero_horas, '99') AS horas_modificadas " +
	
					"FROM ( " +
	
					"SELECT per.codigomedico, ctt.ctcodigo, ctt.ct_fecha, ctt.codigoturno, (SELECT ct.codigocategoria FROM cuadro_turnos ct WHERE ct.ctcodigocuadro=(SELECT ctcodigocuadro FROM ct_turno ctturnotempo WHERE ctturnotempo.ctcodigo=ctt.ctcodigo)) AS categoria, ctt.ctcodigocuadro AS codigocuadro FROM (SELECT ctt.codigomedico FROM ct_turno ctt INNER JOIN cuadro_turnos ct USING(ctcodigocuadro) WHERE ctt.ct_fecha BETWEEN ? AND ? AND ct.codigocategoria=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.ct_fecha BETWEEN ? AND ? AND ctct.categoria=?) per INNER JOIN ct_turno ctt USING(codigomedico) WHERE ctt.ctcodigo NOT IN(SELECT ctcodigo FROM (SELECT ctt.codigomedico FROM ct_turno ctt INNER JOIN cuadro_turnos ct USING(ctcodigocuadro) WHERE ctt.ct_fecha BETWEEN ? AND ? AND ct.codigocategoria=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.ct_fecha BETWEEN ? AND ? AND ctct.categoria=?) per INNER JOIN ct_cubrir_turno ctct USING(codigomedico)) " +
	
					"UNION " +
	
					"SELECT per.codigomedico, ctct.ctcodigo, ctct.ct_fecha, ctct.codigoturno, ctct.categoria, (SELECT ctcodigocuadro FROM cuadro_turnos cttempo WHERE ctct.ct_fecha BETWEEN cttempo.fechainicio AND cttempo.fechafin AND cttempo.codigocategoria=(SELECT categoria FROM ct_cubrir_turno AS cate WHERE cate.ctcodigo=ctct.ctcodigo)) AS codigocuadro FROM (SELECT ctt.codigomedico FROM ct_turno ctt INNER JOIN cuadro_turnos ct USING(ctcodigocuadro) WHERE ctt.ct_fecha BETWEEN ? AND ? AND ct.codigocategoria=? GROUP BY ctt.codigomedico UNION SELECT ctct.codigomedico FROM ct_cubrir_turno ctct WHERE ctct.ct_fecha BETWEEN ? AND ? AND ctct.categoria=?) per INNER JOIN ct_cubrir_turno ctct USING(codigomedico) " +
	
					") turnos  " +
					"INNER JOIN categoria cat ON(turnos.categoria=cat.cat_identificador) " +
					"INNER JOIN "+
						"personas per " +
						"ON(per.codigo=turnos.codigomedico)" +
					"LEFT OUTER JOIN " +
						"ct_turno_horas ctth " +
						"ON(ctth.ct_turno_pk=turnos.ctcodigo) " +
					"LEFT OUTER JOIN "+
						"ct_turno_observacion cto " +
						"ON(cto.ct_turno=turnos.ctcodigo)" +
					"WHERE " +
	
					"turnos.ct_fecha BETWEEN ? AND ?";
				PreparedStatementDecorator consulta;
				/*
				System.out.println("Esta es una impresión de prueba");
				System.out.println("codigoBusqueda "+codigoBusqueda);
				System.out.println("fechaInicial "+fechaInicial);
				System.out.println("fechaFinal "+fechaFinal);
				System.out.println("consulta "+consultaStr);
				 */
				consulta=new PreparedStatementDecorator(con, consultaStr);
				consulta.setInt(1, codigoBusqueda);
				consulta.setInt(2, codigoBusqueda);
				consulta.setString(3, fechaInicial);
				consulta.setString(4, fechaFinal);
				consulta.setInt(5, codigoBusqueda);
				consulta.setString(6, fechaInicial);
				consulta.setString(7, fechaFinal);
				consulta.setInt(8, codigoBusqueda);
				consulta.setString(9, fechaInicial);
				consulta.setString(10, fechaFinal);
				consulta.setInt(11, codigoBusqueda);
				consulta.setString(12, fechaInicial);
				consulta.setString(13, fechaFinal);
				consulta.setInt(14, codigoBusqueda);
				consulta.setString(15, fechaInicial);
				consulta.setString(16, fechaFinal);
				consulta.setInt(17, codigoBusqueda);
				consulta.setString(18, fechaInicial);
				consulta.setString(19, fechaFinal);
				consulta.setInt(20, codigoBusqueda);
				consulta.setString(21, fechaInicial);
				consulta.setString(22, fechaFinal);
				//logger.info(consulta);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
			}
			else
			{
				String consultaStr=""+
					"SELECT " +
						"ctcodigo AS ctcodigo, " +
						"codigomedico AS codigomedico, " +
						"getNombrePersonaSies(codigomedico) AS nombre, " +
						"ct_fecha AS fecha, " +
						"codigoturno AS codigoturno, " +
						"ctcodigocuadro AS codigo_cuadro, " +
						"(SELECT count(1) FROM ct_turno_observacion cto WHERE cto.ct_turno=ctcodigo) AS num_observaciones, " +
						"CASE WHEN categoria IS NOT NULL THEN cat.color ELSE null END AS color, " +
						"CASE WHEN categoria IS NOT NULL THEN cat.cat_nombre ELSE null END AS nombre_categoria, " +
						"categoria AS codigo_categoria, " +
						"to_char(ctth.numero_horas, '99') AS horas_modificadas " +
					"FROM " +
						"ct_turno ctt " +
					"INNER JOIN " +
						"personas per " +
						"ON(per.codigo=codigomedico) " +
					"LEFT OUTER JOIN " +
						"ct_cubrir_turno ctct " +
					"USING (ctcodigo, codigomedico, ct_fecha, codigoturno) " +
					"LEFT OUTER JOIN " +
						"categoria cat " +
						"ON(cat.cat_identificador=ctct.categoria) " +
					"LEFT OUTER JOIN " +
						"ct_turno_horas ctth " +
						"ON(ctth.ct_turno_pk=ctt.ctcodigo) " +
					"WHERE " +
						"codigomedico =? " +
					"AND " +
						"ct_fecha " +
					"BETWEEN ? AND ?";
				/*
				System.out.println("codigoBusqueda "+codigoBusqueda);
				System.out.println("fechaInicial "+fechaInicial);
				System.out.println("fechaFinal "+fechaFinal);
				System.out.println("consulta "+consultaStr);
				*/
				PreparedStatementDecorator consulta;
				consulta=new PreparedStatementDecorator(con, consultaStr);
				consulta.setInt(1, codigoBusqueda);
				consulta.setString(2, fechaInicial);
				consulta.setString(3, fechaFinal);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));				
			}
			
	
			/*
			String consultaStr=consultarTurnosCategoriaStr+
					"WHERE " +
							"t.ct_fecha>=? ";
			
			String consultaTurnosCubiertosStr=consultarTurnosCubiertosCategoriaStr+
					"WHERE " +
							"t.ct_fecha>=? ";
			
			if(fechaFinal!=null && !fechaFinal.trim().equalsIgnoreCase(""))
			{
				consultaStr+=
					"AND t.ct_fecha<=? ";
				consultaTurnosCubiertosStr+=
					"AND t.ct_fecha<=? ";
			}
			
			if(esCategoria)
			{
				consultaStr+=
					"AND c.codigocategoria=? ";
				consultaTurnosCubiertosStr+=
					"AND cat.cat_identificador=? ";
			}
			else
			{
				consultaStr+=
					"AND t.codigomedico=? ";
				consultaTurnosCubiertosStr+=
					"AND t.codigomedico=? ";
			}
			
			consultaStr+=" UNION "+consultaTurnosCubiertosStr;
			
			consultaStr+=
					"ORDER BY nombre, fecha";
			*/

		}
		catch (SQLException e)
		{
			logger.error(" Error consultando los turnos de la categoria:"+codigoBusqueda+"\n",e);
		}
		return null;
	}
	
	/**
	 * Metodo que Actualiza un turno ya sea con una observacion o cambio de turno o ambos
	 * @param con
	 * @param ctcodigo
	 * @param codigoTurno
	 * @param observacion
	 * @return
	 */
	public static int actualizarTurnoObservacion(Connection con, int ctcodigo, int codigoTurno, String observacion)
	{
		//System.out.println("en el sqlbase "+ctcodigo);
		
		if (codigoTurno==0 && !observacion.equals(""))//si solo hay que cambiar la observacion 
			
		{
			actualizarTurnoObservacionStr="UPDATE ct_turno SET ctobservacion=? WHERE ctcodigo=?";
			PreparedStatementDecorator actualizarTurnoObservacionSt;
			
			 try {
			 	actualizarTurnoObservacionSt=new PreparedStatementDecorator(con, actualizarTurnoObservacionStr);
			 	actualizarTurnoObservacionSt.setString(1, observacion );
			 	actualizarTurnoObservacionSt.setInt(2, ctcodigo);
			 					
				return actualizarTurnoObservacionSt.executeUpdate(); 
			} catch (SQLException e) {
				logger.error("Error Actualizando el turno y la observacion "+e);
				
			}
			
		}
		else
		{
			if (codigoTurno!=0 && observacion.equals(""))//si solo hay que cambiar el turno
			{
				actualizarTurnoObservacionStr="UPDATE ct_turno SET codigoturno=? WHERE ctcodigo=?";
				PreparedStatementDecorator actualizarTurnoObservacionSt;
				
				try {
				 	actualizarTurnoObservacionSt=new PreparedStatementDecorator(con, actualizarTurnoObservacionStr);
				 	actualizarTurnoObservacionSt.setInt(1, codigoTurno );
				 	actualizarTurnoObservacionSt.setInt(2, ctcodigo);
				 					
					return actualizarTurnoObservacionSt.executeUpdate(); 
				} catch (SQLException e) {
					logger.error("Error Actualizando el turno y la observacion "+e);
					
				}
				
			}
			else // si hay que cabiarlos ambos
			{
			
				actualizarTurnoObservacionStr="UPDATE ct_turno SET ctobservacion=?, codigoturno=?  WHERE ctcodigo=?";
				PreparedStatementDecorator actualizarTurnoObservacionSt;
				
				try {
				 	actualizarTurnoObservacionSt=new PreparedStatementDecorator(con, actualizarTurnoObservacionStr);
				 	
			 		actualizarTurnoObservacionSt.setString(1, observacion );
				 	actualizarTurnoObservacionSt.setInt(2, codigoTurno);
				 	actualizarTurnoObservacionSt.setInt(3, ctcodigo);
				 	
				 				 
			 		return actualizarTurnoObservacionSt.executeUpdate(); 
				} catch (SQLException e) {
					logger.error("Error Actualizando el turno y la observacion "+e);
					
				}
			}
		
		}  
		return 0;
	}
	
	
	
	/**
	 * Metodo para consultar los turnos correspondientes al reporte solicitado  
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoVinculacion
	 * @return
	 */
	public static Collection <HashMap<String, Object>> consultarTurnosReporte(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int tipoVinculacion)
	{
		String consultarTurnosReporteStr=
			"SELECT " +
				"perso.numero_identificacion AS cedula, " +
				//"turnos.ctcodigo AS ctcodigo, " +
				"turnos.codigomedico AS codigomedico, " +
				"getNombrePersonaSies(turnos.codigomedico) AS nombre, " +
				"turnos.codigoturno AS codigoturno, " +
				"turnos.ct_fecha AS fecha, " +
				"to_char(ctth.numero_horas, '99') AS horas_modificadas " +
			"FROM ( " +
				"SELECT " +
					"per.codigomedico, " +
					"ctt.ctcodigo, " +
					"ctt.ct_fecha, " +
					"ctt.codigoturno, " +
					"(SELECT " +
						"ct.codigocategoria " +
					"FROM " +
						"cuadro_turnos ct " +
					"WHERE " +
						"ct.ctcodigocuadro=" +
						"(SELECT " +
							"ctcodigocuadro " +
						"FROM " +
							"ct_turno ctturnotempo " +
						"WHERE " +
							"ctturnotempo.ctcodigo=ctt.ctcodigo)) AS categoria, " +
					"ctt.ctcodigocuadro AS codigocuadro " +
				"FROM " +
					"(SELECT " +
						"ctt.codigomedico " +
					"FROM " +
						"ct_turno ctt " +
					"INNER JOIN " +
						"cuadro_turnos ct USING(ctcodigocuadro) " +
					"WHERE ctt.ct_fecha BETWEEN ? AND ? AND ct.codigocategoria=? " +
					"GROUP BY " +
						"ctt.codigomedico " +
					"UNION " +
					"SELECT " +
						"ctct.codigomedico " +
					"FROM " +
						"ct_cubrir_turno ctct " +
					"WHERE ctct.ct_fecha BETWEEN ? AND ? AND ctct.categoria=?) per " +
					"INNER JOIN " +
						"ct_turno ctt USING(codigomedico) " +
					"WHERE ctt.ctcodigo NOT IN" +
						"(SELECT " +
							"ctcodigo " +
						"FROM " +
							"(SELECT " +
								"ctt.codigomedico " +
							"FROM " +
								"ct_turno ctt " +
									"INNER JOIN " +
										"cuadro_turnos ct USING(ctcodigocuadro) " +
									"WHERE ctt.ct_fecha BETWEEN ? AND ? AND ct.codigocategoria=? " +
									"GROUP BY ctt.codigomedico " +
									"UNION " +
									"SELECT " +
									"ctct.codigomedico " +
									"FROM ct_cubrir_turno ctct " +
									"WHERE ctct.ct_fecha BETWEEN ? AND ? AND ctct.categoria=?) per " +
									"INNER JOIN ct_cubrir_turno ctct USING(codigomedico)) " +

			") turnos  " +
			"INNER JOIN " +
				"categoria cat ON(turnos.categoria=cat.cat_identificador) " +
			"INNER JOIN " +
				"personas perso ON(perso.codigo=turnos.codigomedico) " +
			"LEFT OUTER JOIN " +
				"medicos med ON(med.codigo_medico=perso.codigo) " +
			"LEFT OUTER JOIN " +
				"ct_turno_horas ctth " +
				"ON(ctth.ct_turno=turnos.ctcodigo) " +
			"WHERE " +
				"turnos.ct_fecha BETWEEN ? AND ? ";
		if(tipoVinculacion>0)
		{
			consultarTurnosReporteStr+="AND med.tipo_vinculacion=? ";
		}
		consultarTurnosReporteStr+=
			"GROUP BY " +
				"nombre, " +
				"perso.numero_identificacion, " +
				"turnos.codigomedico, " +
				"turnos.codigoturno, " +
				"turnos.ct_fecha, " +
				"ctth.numero_horas " +
			"ORDER BY nombre";

		try 
		{
			PreparedStatementDecorator consultarTurnosReporteSt = new PreparedStatementDecorator(con, consultarTurnosReporteStr);
			consultarTurnosReporteSt.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			consultarTurnosReporteSt.setInt(3, codigoCategoria);
			consultarTurnosReporteSt.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(5, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			consultarTurnosReporteSt.setInt(6, codigoCategoria);
			consultarTurnosReporteSt.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(8, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			consultarTurnosReporteSt.setInt(9, codigoCategoria);
			consultarTurnosReporteSt.setString(10, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			consultarTurnosReporteSt.setInt(12, codigoCategoria);
			consultarTurnosReporteSt.setString(13, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(14, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			if(tipoVinculacion>0)
			{
				consultarTurnosReporteSt.setInt(15, tipoVinculacion);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosReporteSt.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.error("Error consultando el reporte de nomina "+e);
			return null;
		}
	}
	
	/**
	 * Andrés Arias López
	 * Metodo para consultar las personas que tiene asociada alguna observacion en su turno y presentarse 
	 * en el reporte de observaciones
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarTurnosReporteObPersona(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		/*String consulta=
			"SELECT " +
				"getnombrepersonasies(per.codigo) AS nombre, " +
				"t.simbolo AS turno, " +
				"to_char(ct_t.ct_fecha, 'DD/MM/YYYY') AS fecha, " +
				"cto.ctobservacion AS observacionper, " +
				"tipo.descripcion AS descripobserva " +
			"FROM personas per " +
			"INNER JOIN " +
				"ct_turno ct_t ON(ct_t.codigomedico=per.codigo) " +
			"INNER JOIN " +
				"turno t ON(t.codigoturno=ct_t.codigoturno) " +
			"INNER JOIN " +
				"ct_turno_observacion cto ON(cto.ct_turno=ct_t.ctcodigo) " +
			"INNER JOIN " +
				"tipo_observacion tipo ON(tipo.codigo=cto.tipo_observacion) " +
			"INNER JOIN " +
				"categoria_enfermera ce " +
					"ON(ce.codigo_medico=per.codigo) " +
			"INNER JOIN " +
				"categoria ca ON (ca.cat_identificador=ce.cat_identificador) " +
			"WHERE " +
				"ca.cat_identificador = ? AND (ct_t.ct_fecha BETWEEN ? AND ?) AND " +
			"GROUP BY " +
				"per.codigo, " +
				"t.simbolo, " +
				"ct_t.ct_fecha, " +
				"cto.ctobservacion, " +
				"tipo.descripcion " +
			"ORDER BY descripobserva, nombre, observacionper";*/
			
		
		try
		{
			PreparedStatementDecorator consultarTurnosReportePersonaSt = new PreparedStatementDecorator(con, consultarTurnosReporteObPersonaStr);
			//System.out.println(consultarTurnosReporteObPersonaStr);
			consultarTurnosReportePersonaSt.setInt(1,codigoCategoria);
			consultarTurnosReportePersonaSt.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReportePersonaSt.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosReportePersonaSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el reporte de observaciones para la categoría "+codigoCategoria+": "+e);
			return null;		
		}
	}
	
	/**
	 * (Juan David & Andrés Arias López) 
	 * Metodo para consultar los turnos correspondientes al reporte de Observaciones 
	 * @param con
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarTurnosReporteObs(Connection con, String fechaInicio, String fechaFin)
	{
		try
		{
			PreparedStatementDecorator consultarTurnosReporteSt = new PreparedStatementDecorator(con, consultarTurnosReporteObStr);
			consultarTurnosReporteSt.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			consultarTurnosReporteSt.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarTurnosReporteSt.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosReporteSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el reporte de observaciones entre las fechas dadas"+e);
			return null;		
		}
	}

	/**
	 * (Juan David)
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarPersonasCuadro(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		try
		{
			/*System.out.println("consultarPersonasCuadroPorFechas "+consultarPersonasCuadroPorFechas);
			System.out.println("codigoCategoria "+codigoCategoria);
			System.out.println("fechaInicio "+fechaInicio);
			System.out.println("fechaFin "+fechaFin);*/
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarPersonasCuadroPorFechas);
			stm.setInt(1, codigoCategoria);
			stm.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			stm.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			stm.setInt(4, codigoCategoria);
			stm.setString(5, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			stm.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las personas pertenecientes a un cuadro de turnos "+e);
			return null;
		}
	}

	/**
	 * (Juan David)
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCuadro
	 * @param fechaInicio
	 * @param fechaFin
	 * @return Listado de personas
	 */
	public static Collection<HashMap<String, Object>> consultarPersonasCuadroPorCodigoCuadro(Connection con, int codigoCuadro, String fechaInicio, String fechaFin)
	{
		try
		{
			/*System.out.println("consultarPersonasCuadroPorCodigoCuadroStr "+consultarPersonasCuadroPorCodigoCuadroStr);
			System.out.println("codigoCuadro "+codigoCuadro);
			System.out.println("fechaInicio "+fechaInicio);
			System.out.println("fechaFin "+fechaFin);*/
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarPersonasCuadroPorCodigoCuadroStr);
			stm.setInt(1, codigoCuadro);
			stm.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			stm.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			stm.setInt(4, codigoCuadro);
			stm.setString(5, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			stm.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las personas pertenecientes a un cuadro de turnos "+e);
			return null;
		}
	}

	/**
	 * (Juan David)
	 * Método que consulta las observaciones de un turno específico
	 * @param con
	 * @param codigoTurno
	 * @return Collection con las observaciones descritas
	 */
	public static Collection<HashMap<String, Object>> consultarObservacionesTurno(Connection con, int codigoTurno)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarObservacionesTurnoStr);
			stm.setInt(1, codigoTurno);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las observaciones del turno "+codigoTurno+" Error: "+e);
			return null;
		}
	}

	/**
	 * (Juan David)
	 * Método que consulta el listado de los tipos de observaciónexistentes en la BD
	 * @param con
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarTiposObservacion(Connection con)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarTiposObservacionStr);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipos de observaciones existentes en BD "+e);
			return null;
		}
	}
	
	/**
	 * (Juan David)
	 * Método que actualiza las observaciones de un turno
	 * @param con
	 * @param tipoObservacion
	 * @param codigoTurnoObservacion
	 * @param novedad
	 * @param usuario
	 * @param observacion
	 * @return true si se ingresó, false de lo contrario
	 */
	public static boolean ingresarActualizarObservacion(Connection con, String observacion, int tipoObservacion, int codigoTurnoObservacion, int novedad, String usuario)
	{
		String ingresarActualizarObservacion=""+
			"INSERT INTO " +
				"ct_turno_observacion " +
				"(codigo, tipo_observacion, ct_turno, ctobservacion, fecha, hora, usuario)" +
			"VALUES " +
				"(?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, ingresarActualizarObservacion);
			int codigo=UtilidadSiEs.obtenerSiguienteValorSecuencia(con, SiEsSecuencias.NOMBRE_SECUENCIA_OBSERVACIONES_TURNO);
			stm.setInt(1, codigo);
			stm.setInt(2, tipoObservacion);
			stm.setInt(3, codigoTurnoObservacion);
			stm.setString(4, observacion);
			stm.setString(5, usuario);
			boolean resultado=stm.executeUpdate()>0;
			if(resultado && novedad!=0)
			{
				resultado=ingresarNovedad(con, codigo, novedad);
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la observación para el turno "+codigoTurnoObservacion+": "+e);
			return false;
		}
	}

	/**
	 * (Juan David)
	 * Método para ingresar los tipos de novedad que no han sido programados
	 * antes de la generación del cuadro de turnos 
	 * @param con
	 * @param codigoObservacion
	 * @param codigoNovedad
	 * @return
	 */
	private static boolean ingresarNovedad(Connection con, int codigoObservacion, int codigoNovedad)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, ingresarNovedadStr);
			stm.setInt(1, codigoObservacion);
			stm.setInt(2, codigoNovedad);
			return stm.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la novedad "+codigoNovedad+" para la observación "+codigoObservacion+": "+e);
			return false;
		}
	}
	
	/**
	 * Método para consultar el listado de personas con el que se debe generar el cuadro de turnos
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		try
		{
			fechaInicio=UtilidadFecha.conversionFormatoFechaABD(fechaInicio);
			fechaFin=UtilidadFecha.conversionFormatoFechaABD(fechaFin);
			/*System.out.println(consultarEnfermeraCategoriaStr);
			System.out.println(codigoCategoria);
			System.out.println(fechaInicio);*/
			PreparedStatementDecorator consultarEnfermeraCategoriaStm=new PreparedStatementDecorator(con, consultarEnfermeraCategoriaStr);
			consultarEnfermeraCategoriaStm.setInt(1, codigoCategoria);
			consultarEnfermeraCategoriaStm.setString(2, fechaInicio);
			consultarEnfermeraCategoriaStm.setString(3, fechaFin);
			consultarEnfermeraCategoriaStm.setString(4, fechaInicio);
			consultarEnfermeraCategoriaStm.setString(5, fechaFin);
			consultarEnfermeraCategoriaStm.setString(6, fechaInicio);
			consultarEnfermeraCategoriaStm.setString(7, fechaInicio);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarEnfermeraCategoriaStm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error listando las personas "+e);
		}
		return null;
	}
	
	/**
	 * (Juan David)
	 * Método para consultar las restricciones de los cuadros de turnos generados
	 * entre las fechas pasadas por parámetros y la categoría seleccionada
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarRestriccionesCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria)
	{
		try
		{
			/*System.out.println("consultarRestriccionesCuadroStr "+consultarRestriccionesCuadroStr);
			System.out.println("fechaInicio "+fechaInicio);
			System.out.println("fechaFin "+fechaFin);*/
			PreparedStatementDecorator consultarRestriccionesCuadroStm=new PreparedStatementDecorator(con, consultarRestriccionesCuadroStr);
			consultarRestriccionesCuadroStm.setInt(1, codigoCategoria);
			consultarRestriccionesCuadroStm.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			consultarRestriccionesCuadroStm.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarRestriccionesCuadroStm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error consultando las restricciones de la categoria para los cuadros generados "+e);
			return null;
		}
	}

	/**
	 * Método para Consultar el codigo del turno relacionado al cuadro de turnos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoPersona
	 */
	public static int consultarCTCodigoTurno(Connection con, int codigoCT, String fecha, int codigoPersona)
	{
		PreparedStatementDecorator insertarTurnoSt;
		try
		{
			insertarTurnoSt = new PreparedStatementDecorator(con, consultarTurnoStr);
			insertarTurnoSt.setInt(1, codigoCT);
			insertarTurnoSt.setInt(2, codigoPersona);
			insertarTurnoSt.setString(3, fecha);
			ResultSet resultado=insertarTurnoSt.executeQuery();
			if(resultado.next())
			{
				int codigo=resultado.getInt("codigo");
				return codigo;
			}
		} catch (SQLException e)
		{
			logger.error("Error consultando el código del turno "+e);
			return -1;
		}
		return 0;
	}

	/**
	 * Método que lista los cuadros de turnos que contengan la
	 * fecha pasada por parámetros
	 * @param con
	 * @param fecha
	 * @param centroCosto Centro de costo del usuario
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarCuadroTurnosDadaUnaFecha(Connection con, String fecha, Integer centroCosto)
	{
		try
		{
			PreparedStatementDecorator stm=null;
			if(centroCosto==null || centroCosto==0)
			{
				stm=new PreparedStatementDecorator(con, consultarCuadroTurnosDadaUnaFechaStr);
				/*
				System.out.println(consultarCuadroTurnosDadaUnaFechaStr);
				System.out.println(fecha);
				System.out.println(centroCosto);*/
			}
			else
			{
				stm=new PreparedStatementDecorator(con, consultarCuadroTurnosDadaUnaFechaCentroCostoStr);
				/*
				System.out.println(consultarCuadroTurnosDadaUnaFechaCentroCostoStr);
				System.out.println(fecha);
				System.out.println(centroCosto);*/
			}
			stm.setString(1, fecha);
			if(centroCosto!=null && centroCosto>0)
			{
				stm.setInt(2, centroCosto);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los cuadros de turnos contenidos en la fecha: "+fecha+": "+e);
			return null;
		}
	}

	/**
	 * Método que lista los cuadros de turnos que pueden ser
	 * eliminados pasada una fecha
	 * @param con
	 * @param fecha
	 * @param centroCosto
	 * @return Listado de cuadro de turnos
	 */
	public static Collection<HashMap<String, Object>> consultarCuadroTurnosEliminar(Connection con, String fecha, Integer centroCosto)
	{
		try
		{
			PreparedStatementDecorator stm;
			if(centroCosto!=null && centroCosto!=0)
			{
				stm=new PreparedStatementDecorator(con, consultarCuadroTurnosEliminarCentroCostoStr);
			}
			else
			{
				stm=new PreparedStatementDecorator(con, consultarCuadroTurnosEliminarStr);
			}
			stm.setString(1, fecha);
			stm.setString(2, fecha);
			if(centroCosto!=null && centroCosto!=0)
			{
				stm.setInt(3, centroCosto);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los cuadros de turnos para eliminar: "+fecha+": "+e);
			return null;
		}
	}

	/**
	 * Método que permite eliminar un cuadro de turnoas dado su código
	 * Elimina todos los turnos y observaciones que estén asociados a él
	 * @param con
	 * @param codigoCuadroTurnos
	 * 
	 */
	public static int eliminar(Connection con, int codigoCuadroTurnos)
	{
		try
		{
			// Elimino las horas modificadas
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, eliminarHorasTurnosStr);
			stm.setInt(1, codigoCuadroTurnos);
			stm.executeUpdate();
			// Elimino las novedades
			stm=new PreparedStatementDecorator(con, eliminarNovedadesCuadroTurnosStr);
			stm.setInt(1, codigoCuadroTurnos);
			stm.executeUpdate();
			// Elimino las observaciones
			stm=new PreparedStatementDecorator(con, eliminarObservacionesCuadroTurnosStr);
			stm.setInt(1, codigoCuadroTurnos);
			stm.executeUpdate();
			// Elimino los turnos
			stm=new PreparedStatementDecorator(con, eliminarTurnosCuadroTurnosStr);
			stm.setInt(1, codigoCuadroTurnos);
			stm.executeUpdate();
			// Elimino las restricciones
			stm=new PreparedStatementDecorator(con, eliminarRestriccionesCuadroStr);
			stm.setInt(1, codigoCuadroTurnos);
			stm.executeUpdate();
			// Elimino el cuadro
			stm=new PreparedStatementDecorator(con, eliminarCuadroStr);
			stm.setInt(1, codigoCuadroTurnos);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando el cuadro de turnos "+e);
			return -1;
		}
	}
	
	/**
	 * Método para eliminar un turno específico para una persona en una fecha
	 * @param con
	 * @param codigoPersonaEliminar
	 * @param fecha
	 * @return Retorna 0 en caso de error, en caso de éxito retorna el número de elementos borrados de la base de datos
	 */
	public static int eliminarTurno(Connection con, int codigoPersonaEliminar, String fecha)
	{
		fecha=UtilidadFecha.conversionFormatoFechaABD(fecha);
		String buscarCodigoTurno="SELECT ctcodigo FROM ct_turno WHERE codigomedico=? AND ct_fecha=?";
		String eliminarTurnoNovedad="DELETE FROM ct_turno_novedad WHERE codigo_observacion IN(SELECT codigo FROM ct_turno_observacion WHERE ct_turno=?)";
		String eliminarTurnoObservacionStr="DELETE FROM ct_turno_observacion WHERE ct_turno=?";
		String eliminarHorasModificadasStr="DELETE FROM ct_turno_horas WHERE ct_turno_pk=?";
		String eliminarCubrirTurnoStr="DELETE FROM ct_cubrir_turno WHERE codigomedico=? AND ct_fecha=?";
		String eliminarTurnoStr="DELETE FROM ct_turno WHERE ctcodigo=?";
		String eliminarTurnoGeneralStr="DELETE FROM ct_turno_general WHERE ctcodigo=?";
		int resultados=0;
		try
		{
			int codigoTurno=0;
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, buscarCodigoTurno);
			stm.setInt(1, codigoPersonaEliminar);
			stm.setString(2, fecha);
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				codigoTurno=resultado.getInt("ctcodigo");
			}
			stm=new PreparedStatementDecorator(con, eliminarTurnoNovedad);
			stm.setInt(1, codigoTurno);
			resultados+=stm.executeUpdate();

			stm=new PreparedStatementDecorator(con, eliminarTurnoObservacionStr);
			stm.setInt(1, codigoTurno);
			resultados+=stm.executeUpdate();

			stm=new PreparedStatementDecorator(con, eliminarHorasModificadasStr);
			stm.setInt(1, codigoTurno);
			resultados+=stm.executeUpdate();

			stm=new PreparedStatementDecorator(con, eliminarCubrirTurnoStr);
			stm.setInt(1, codigoPersonaEliminar);
			stm.setString(2, fecha);
			resultados+=stm.executeUpdate();
			
			stm=new PreparedStatementDecorator(con, eliminarTurnoStr);
			stm.setInt(1, codigoTurno);
			resultados+=stm.executeUpdate();

			stm=new PreparedStatementDecorator(con, eliminarTurnoGeneralStr);
			stm.setInt(1, codigoTurno);
			resultados+=stm.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error eliminando el turno para la persona: "+codigoPersonaEliminar+" fecha: "+fecha+" Error: "+e);
			System.exit(1);
			return 0;
		}
		return resultados;
	}

	/**
	 * Método para ingrementar o elíminar el número de horas trabajadas por una persona
	 * en un turno específico sin necesidad de crear un turno nuevo
	 * @param con
	 * @param ctTurno
	 * @param numeroHoras
	 * @return retorna el número de modificaciones en BD
	 */
	public static int ingresarHorasTurno(Connection con, int ctTurno, double numeroHoras)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "SELECT");
			stm.setInt(1, ctTurno);
			stm.setDouble(2, numeroHoras);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando "+numeroHoras+" horas adicionales al turno "+ctTurno+": "+e);
			return 0;
		}
	}

	/**
	 * Método para almacenar el número de horas faltantes del mes para cada persona
	 * @param con
	 * @param codigoPersona
	 * @param numeroHorasFaltantes
	 * @param numeroHorasFaltantesAnteriores
	 * @return número de registros almacenados en BD
	 */
	public static int guardarHorasFaltantes(Connection con, int codigoPersona, double numeroHorasFaltantes, double numeroHorasFaltantesAnteriores)
	{
		String guardarHorasFaltantesStr="INSERT INTO cuadroturnos.horas_faltantes(codigo_persona, numero_horas, numero_horas_anterior) VALUES(?, ?, ?);";
		String actualizarHorasFaltantesStr="UPDATE cuadroturnos.horas_faltantes SET numero_horas=?, numero_horas_anterior=? WHERE codigo_persona=?;";
		try
		{
			if(consultarHorasFaltantes(con, codigoPersona, true)==-1)
			{
				PreparedStatementDecorator stm=new PreparedStatementDecorator(con, guardarHorasFaltantesStr);
				stm.setInt(1, codigoPersona);
				stm.setDouble(2, numeroHorasFaltantes);
				stm.setDouble(3, numeroHorasFaltantesAnteriores);
				return stm.executeUpdate();
			}
			else
			{
				PreparedStatementDecorator stm=new PreparedStatementDecorator(con, actualizarHorasFaltantesStr);
				stm.setDouble(1, numeroHorasFaltantes);
				stm.setDouble(2, numeroHorasFaltantesAnteriores);
				stm.setInt(3, codigoPersona);
				return stm.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando las horas faltantes para la persona "+codigoPersona+": "+e);
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Método para consultar el número de horas faltantes del mes para cada persona
	 * @param con
	 * @param codigoPersona
	 * @param anteriores
	 * @return Número de horas faltantes o faltantes anteriores
	 */
	public static double consultarHorasFaltantes(Connection con, int codigoPersona, boolean anteriores)
	{
		String consultarHorasFaltantesStr="SELECT codigo_persona AS codigo_persona, numero_horas AS numero_horas, numero_horas_anterior AS numero_horas_anterior FROM cuadroturnos.horas_faltantes WHERE codigo_persona=?;";
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarHorasFaltantesStr);
			stm.setInt(1, codigoPersona);
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				if(anteriores)
				{
					return resultado.getDouble("numero_horas_anterior");
				}
				else
				{
					return resultado.getDouble("numero_horas");
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las horas faltantes para la persona "+codigoPersona+": "+e);
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Método que almacena las horas adicionales al turno en BD
	 * En caso de que ya existan son adicionadas a las horas existentes
	 * @param con
	 * @param codigoTurno
	 * @param numeroHoras
	 * @param usuario
	 * @return Número de registros insertados o modificados en BD, -1 en caso de error
	 */
	public static int modificarHorasTurno(Connection con, int codigoTurno, double numeroHoras, String usuario)
	{
		try
		{
			Double horasAnteriores=consultarHorasTurno(con, codigoTurno);
			if(horasAnteriores==null)
			{
				return -1;
			}
			else if(horasAnteriores==0)
			{
				PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "INSERT INTO ct_turno_horas(ct_turno_pk, numero_horas) VALUES(?, ?)");
				stm.setInt(1, codigoTurno);
				stm.setDouble(2, numeroHoras);
				return stm.executeUpdate();
			}
			else
			{
				PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "UPDATE ct_turno_horas SET numero_horas=? WHERE ct_turno_pk=?");
				stm.setDouble(1, horasAnteriores+numeroHoras);
				stm.setInt(2, codigoTurno);
				return stm.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			logger.error("Error modificando las horas del turno: "+codigoTurno+": "+e);
			return -1;
		}
	}

	/**
	 * Método que consulta las horas adicionales de un turno en BD
	 * @param con Conexión con la BD
	 * @param codigoTurno Código del turno que se desea consultar
	 * @return Número de horas adicionales al turno ó 0 si no hay registros en BD, en caso de error retorna null
	 */
	public static Double consultarHorasTurno(Connection con, int codigoTurno)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "SELECT numero_horas FROM ct_turno_horas WHERE ct_turno_pk=?");
			stm.setInt(1, codigoTurno);
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				return resultado.getDouble("numero_horas");
			}
			else
			{
				return 0d;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las horas del turno: "+codigoTurno+": "+e);
			return null;
		}
	}

	/**
	 * Método que eliminar las horas adicionales de un turno en BD
	 * @param con Conexión con la BD
	 * @param codigoTurno Código del turno que se desea eliminar
	 * @return Número de registros modificados en BD, -1 en caso de error
	 */
	public static int eliminarHorasTurno(Connection con, int codigoTurno)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "DELETE FROM ct_turno_horas WHERE ct_turno_pk=?");
			stm.setInt(1, codigoTurno);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las horas del turno: "+codigoTurno+": "+e);
			return -1;
		}
	}

}
