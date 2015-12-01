package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.parametrizacion.ConstantesSeccionesParametrizables;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jose Eduardo Arias Doncel Febrero 2008
 */

public class SqlBaseHojaAnestesiaDao {

	
	public static Logger logger=Logger.getLogger(SqlBaseHojaAnestesiaDao.class);
	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS GENERALES***************************************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// *********************************************************************************************
	// Hoja de Anestesia
	// *********************************************************************************************
	/**
	 * Consulta de la informacion de la hoja de anestesia
	 */
	public static String strConsultaHojaAnestesia = "SELECT "
			+ "ha.numero_solicitud AS numero_solicitud0,"
			+ "ha.anestesiologo_cobrable AS anestesiologo_cobrable1,"
			+ "fecha_inicia_anestesia AS fecha_inicia_anestesia2,"
			+ "hora_inicia_anestesia AS hora_inicia_anestesia3,"
			+ "fecha_finaliza_anestesia AS fecha_finaliza_anestesia4,"
			+ "hora_finaliza_anestesia AS hora_finaliza_anestesia5,"
			+ "getTipoSolicitud ( ha.numero_solicitud ) AS tipo_solicitud15 "
			+ "FROM hoja_anestesia ha " + "WHERE ha.numero_solicitud = ? ";

	/**
	 * Insertar Hoja Anestesia
	 */
	public static String strInsertarHojaAnestesia = "INSERT INTO "
			+ "hoja_anestesia" + "(" + "numero_solicitud," + "institucion,"
			+ "fecha_inicia_anestesia," + "hora_inicia_anestesia,"
			+ "fecha_finaliza_anestesia," + "hora_finaliza_anestesia,"
			+ "preanestesia," + "datos_medico," + "finalizada,"
			+ "fecha_finalizada," + "hora_finalizada," + "fecha_grabacion,"
			+ "hora_grabacion," + "observaciones_signos_vitales,"
			+ "anestesiologo_cobrable," + "descripcion" +")"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	/**
	 * Actualizar informaciï¿½n Hoja de Anestesia en Salida Paciente
	 */
	public static String strActualizarHojaAnestesia = "UPDATE "
			+ "hoja_anestesia " + "SET " + "datos_medico = ?,"
			+ "finalizada =  ?," + "fecha_finalizada = ?,"
			+ "hora_finalizada = ? " + "WHERE numero_solicitud = ? "
			+ "AND institucion = ? ";

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION DE INFORMACION GENERAL**********************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	// *********************************************************************************************
	// Especialidades que Intervienen
	// *********************************************************************************************

	/**
	 * Cadena de insercion de las especialidades que intervienen
	 */
	private static String strInsertarEspecialidadesIntervienen = "INSERT INTO "
			+ "esp_intervienen_sol_cx(numero_solicitud,especialidad,asignada,usuario_modifica,fecha_modifica,hora_modifica) "
			+ "VALUES (?,?,?,?,?,?) ";

	/**
	 * Consulta de la informacion de las especialidades que intervienen
	 */
	public static String strConsultaEspecIntervienen = "SELECT "
			+ "numero_solicitud AS numero_solicitud0, "
			+ "especialidad AS especialidad1,"
			+ "getnombreespecialidad(especialidad) AS nombre_especialidad2,"
			+ "asignada AS asignada3," + "'" + ConstantesBD.acronimoSi
			+ "' AS estabd4 " + "FROM " + "esp_intervienen_sol_cx "
			+ "WHERE numero_solicitud = ? ";

	/**
	 * Actualiza la informacion de las especialidades que intervienen
	 */
	public static String strActualizarEspecIntervienen = "UPDATE "
			+ "esp_intervienen_sol_cx SET " + "asignada = ? "
			+ "WHERE numero_solicitud = ? " + "AND especialidad = ? ";

	/**
	 * Eliminacion de datos de las especialidades que intervienen
	 */
	public static String strEliminarEspecIntervienen = "DELETE FROM "
			+ "esp_intervienen_sol_cx " + "WHERE numero_solicitud = ? ";

	// *********************************************************************************************
	// Cirujanos Especialidades Intervienen
	// *********************************************************************************************

	/**
	 * Consulta de la informacion de los ciruganos por la especialidad
	 */
	public static String strConsultarCirujanosEspecialidad = "SELECT "
			+ "consecutivo AS consecutivo0,"
			+ "numero_solicitud AS numero_solicitud1,"
			+ "especialidad AS especialidad2,"
			+ "getnombreespecialidad(especialidad) AS nombre_especialidad3,"
			+ "profesional AS profesional4,"
			+ "administracion.getnombremedico(profesional) AS nombre_profesional5,"
			+ "asignada AS asignada6," + "'" + ConstantesBD.acronimoSi
			+ "' AS estabd9 " + "FROM cirujanos_esp_int_solcx "
			+ "WHERE numero_solicitud = ? AND especialidad = ? "
			+ "ORDER BY nombre_profesional5 ASC ";

	/**
	 * Consulta de la informacion de los ciruganos por solicitud
	 */
	public static String strconsultarCirujanosServicios = "SELECT "
			+ "consecutivo AS consecutivo0, "
			+ "numero_solicitud AS numero_solicitud1,"
			+ "getnombreespecialidad(especialidad) AS nombre_especialidad3,"
			+ "profesional AS profesional4,"
			+ "administracion.getnombremedico(profesional) AS nombre_profesional5,"
			+ "asignada AS asignada6 " 
			+ "FROM cirujanos_esp_int_solcx "
			+ "WHERE numero_solicitud = ?";
	/**
	 * Actualiza la informacion de los cirujanos de la especialidad que
	 * intervienen
	 */
	public static String strActualizarCirujanosEspecialidad = "UPDATE "
			+ "cirujanos_esp_int_solcx SET " + "asignada = ?,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ?  " + "WHERE " + "consecutivo = ? ";

	/**
	 * Elimina la informacion de los cirujanos de la especialidad que
	 * intervienen
	 */
	public static String strEliminaCirujanosEspecialidad = "DELETE FROM "
			+ "cirujanos_esp_int_solcx " + "WHERE 1=1 ";

	// *********************************************************************************************
	// Anestesiologos
	// *********************************************************************************************
	/**
	 * Inserta la informacion de los anestesiologos
	 */
	public static String strInsertarAnestesiologos = "INSERT INTO "
			+ "anestesiologos_hoja_anes (numero_solicitud,profesional,fecha,hora,definitivo) "
			+ "VALUES(?,?,?,?,?) ";

	/**
	 * Consulta la informacion de los anestesiologos
	 */
	public static String strConsultarAnestesiologos = "SELECT "
			+ "numero_solicitud AS numero_solicitud0,"
			+ "profesional AS profesional1,"
			+ "administracion.getnombremedico(profesional) AS nombre_profesional2,"
			+ "TO_CHAR(fecha,'DD/MM/YYYY') AS fecha3," + "hora AS hora4,"
			+ "definitivo AS definitivo5 "
			+ "FROM anestesiologos_hoja_anes "
			+ "WHERE numero_solicitud = ? ";

	/**
	 * Consulta si existe o no el medico en la tabla de anestesiologos
	 */
	public static String strConsultarExisteAnestesiologo = "SELECT "
			+ "COUNT (profesional) AS cantidad "
			+ "FROM anestesiologos_hoja_anes " + "WHERE numero_solicitud = ? "
			+ "AND profesional = ? ";

	/**
	 * Actualiza la informacion de los anestesiologos
	 */
	public static String strActualizarAnestesiologos = "UPDATE "
			+ "anestesiologos_hoja_anes SET " + "fecha = ? ," + "hora = ? "
			+ "WHERE numero_solicitud  = ? AND " + "profesional = ? ";

	/**
	 * Actualiza el campo anestesiologo cobrable
	 */
	public static String strActualizarCobrable = "UPDATE "
			+ "hoja_anestesia SET " + "anestesiologo_cobrable = ? "
			+ "WHERE numero_solicitud = ? ";

	/**
	 * Actualiza el campo definitivo
	 */
	public static String strActualizarDefinitivo = "UPDATE "
			+ "anestesiologos_hoja_anes SET " + "definitivo = ? "
			+ "WHERE numero_solicitud = ? " + "AND profesional = ? ";

	// *********************************************************************************************
	// Accesos Vasculares
	// *********************************************************************************************
	
	/**
	 * actualiza la informacion del pedido qx
	 * */
	public static String strActualizarCantidadDetMaterialesQxVasculares = "UPDATE " +
			"det_materiales_qx " +
			"SET " +
			"cantidad = (SELECT SUM(d.cantidad) FROM accesos_vasculares_hoja_anes d WHERE d.genero_consumo = '"+ConstantesBD.acronimoSi+"' AND d.codigo_det_mate_qx = ? ) " +
			"WHERE " +
			"codigo = ? ";
	
	/**
	 * Consulta los codigos del detalle de materiales qx
	 * */
	public static String strConsultarCodigosDetMateQxVasculares = 
			"SELECT " +
			"d.codigo_det_mate_qx " +
			"FROM " +
			"accesos_vasculares_hoja_anes d " +			
			"INNER JOIN det_materiales_qx mat ON (mat.codigo = d.codigo_det_mate_qx AND mat.cantidad != d.cantidad) " +
			"WHERE " +
			"d.genero_consumo = '"+ConstantesBD.acronimoSi+"' " +			
			"AND d.codigo_det_mate_qx IS NOT NULL AND d.numero_solicitud = ? " +
			"GROUP BY d.codigo_det_mate_qx ";

	
	// *********************************************************************************************
	// Via Area
	// *********************************************************************************************

	/**
	 * Consulta los totales de las cantidades de la via area
	 */
	public static String strConsultaCantidadesViaArea = "SELECT "
			+ "v.codigo AS via_area0, "
			+ "d.articulo AS articulo1,"
			+ "SUM(d.cantidad) AS cantidad2,"
			+ "d.genera_consumo AS genero_consumo3,"
			+ "getdescarticulo(d.articulo) AS descripcion_articulo4 "
			+ "FROM det_vias_aerea_hoja_anes  d "
			+ "INNER JOIN vias_aerea_hoja_anes v ON (v.codigo = d.via_aerea AND v.numero_solicitud = ? AND institucion = ? ) "
			+ "WHERE d.genera_consumo = '" + ConstantesBD.acronimoNo + "' "
			+ "GROUP BY v.codigo,d.articulo,d.genera_consumo ";

	/**
	 * Actualizar el indicativo genero consumo de la via area
	 */
	public static String strActualizarGenConsuDetViaArea = "UPDATE det_vias_aerea_hoja_anes "
			+ "SET genera_consumo = ? ," 
			+ "codigo_det_mate_qx = ? "
			+ "WHERE via_aerea = ? AND "
			+ "articulo = ? ";
	
	/**
	 * actualiza la informacion del pedido qx
	 * */
	public static String strActualizarCantidadDetMaterialesQxViaA = "UPDATE " +
			"det_materiales_qx " +
			"SET " +
			"cantidad = (SELECT SUM(d.cantidad) FROM det_vias_aerea_hoja_anes d WHERE d.genera_consumo = '"+ConstantesBD.acronimoSi+"' AND d.codigo_det_mate_qx = ? ) " +
			"WHERE " +
			"codigo = ? ";
	
	/**
	 * Consulta los codigos del detalle de materiales qx
	 * */
	public static String strConsultarCodigosDetMateQxVia = 
			"SELECT " +
			"d.codigo_det_mate_qx " +
			"FROM " +
			"det_vias_aerea_hoja_anes d " +
			"INNER JOIN vias_aerea_hoja_anes a ON (a.codigo = d.via_aerea AND a.numero_solicitud = ?) " +
			"INNER JOIN det_materiales_qx mat ON (mat.codigo = d.codigo_det_mate_qx AND mat.cantidad != d.cantidad) " +
			"WHERE " +
			"d.genera_consumo = '"+ConstantesBD.acronimoSi+"' " +			
			"AND d.codigo_det_mate_qx IS NOT NULL " +
			"GROUP BY d.codigo_det_mate_qx ";

	// *********************************************************************************************
	// Fecha y Hora de Ingreso a la Sala
	// *********************************************************************************************

	/**
	 * Actualiza la fecha y hora de ingreso a la sala
	 */
	public static String strActualizarFechaHoraIngreso = "UPDATE "
			+ "solicitudes_cirugia " + "SET " + "fecha_ingreso_sala = ?,"
			+ "hora_ingreso_sala = ? " + "WHERE " + "numero_solicitud = ? ";

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION DE OBSERVACIONES
	 * GENERALES******************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Consulta la informacion de las Observaciones Generales
	 */
	public static String strConsultarObservacionesGenerales = "SELECT "
			+ "codigo AS codigo0, " + "numero_solicitud AS numero_solicitud1, "
			+ "descripcion AS descripcion2, "
			+ "TO_CHAR(fecha,'DD/MM/YYYY') AS fecha3, " + "hora AS hora4, "
			+ "usuario AS usuario5 " + "FROM observaciones_hoja_anes "
			+ "WHERE numero_solicitud = ? " + "ORDER BY fecha,hora DESC ";

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION DE ANESTESICOS Y MEDICAMENTOS ADMINISTRADOS************************* 
	 * ATRIBUTOS PARA LA SECCION DE LIQUIDOS*********************************************************
	 * ATRIBUTOS PARA LA SECCION DE HEMODERIVADOS****************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Consulta la informacion de los medicamentos suministrados
	 */
	public static String strConsultarAdminHojaAnest = "SELECT  "
			+ "aha.codigo AS codigo0,"
			+ "aha.numero_solicitud AS numero_solicitud1,"
			+ "aha.articulo AS articulo2,"
			+ "aha.otro_medicamento AS otro_medicamento3,"
			+ ConstantesBD.separadorSplit
			+ "aha.seccion AS seccion5,"
			+ "getdescarticulo(aha.articulo) AS descripcion_articulo6,"
			+ "'"+ConstantesBD.acronimoSi+"' AS estabd7,"
			+ "aha.tipo_liquido AS tipo_liquido9,"
			+ "CASE WHEN aha.tipo_liquido IS NULL THEN '' ELSE getdescripciontipoliquidoanes(aha.tipo_liquido) END AS descripcion_tipo_liquido10, "
			+ "na.es_pos AS espos11, "
			+ "gettienejustificacionnopos(aha.articulo,aha.numero_solicitud) AS tienejus12 "
			+ "FROM admin_hoja_anestesia aha "
			+ "LEFT OUTER JOIN articulo a ON (aha.articulo=a.codigo) "
			+ "LEFT OUTER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) "
			+ "WHERE aha.numero_solicitud = ? AND " + "aha.seccion = ? "
			+ "ORDER BY descripcion_tipo_liquido10 ASC ";

	/**
	 * Consulta del detalle de administraciones de los medicamentos
	 * suministrados
	 */
	public static String strConsultaDetHojaAnest = "SELECT "
			+ "codigo AS codigo0," + "admin_hoja_anes AS admin_hoja_anes1,"
			+ "dosis AS dosis2," + "TO_CHAR(fecha,'DD/MM/YYYY') AS fecha3,"
			+ "hora AS hora4," + "graficar AS graficar5, "
			+ "genero_consumo AS genero_consumo6," 
			+ "sellocalidad AS sellocalidad8 "
			+ "FROM det_admin_hoja_anes " + "WHERE admin_hoja_anes = ? ";

	/**
	 * Actualiza el detalle de administraciones de lso medicamentos
	 * suministrados
	 */
	public static String strActualizarDetHojaAnest = "UPDATE "
			+ "det_admin_hoja_anes " 
			+ "SET dosis = ?," 
			+ "fecha = ?, "
			+ "hora = ?," 
			+ "graficar = ?," 
			+ "usuario_modifica = ?,"
			+ "fecha_modifica = ?," 
			+ "hora_modifica = ?," 
			+ "sellocalidad = ?  "
			+ "WHERE codigo = ? ";

	/**
	 * Actualiza el indicativo del genero consumo
	 */
	public static String strActualizarGenConsumoDetHojaAnest = "UPDATE "
			+ "det_admin_hoja_anes  "
			+ "SET "
			+ "genero_consumo = ?,"
			+ "usuario_modifica = ?,"
			+ "fecha_modifica = ?,"
			+ "hora_modifica = ?," 
			+ "codigo_det_mate_qx = ?  "
			+ "WHERE " 
			+ "genero_consumo != '"+ConstantesBD.acronimoSi+"' AND codigo_det_mate_qx IS NULL AND " 
			+ "admin_hoja_anes = "
			+ "(SELECT aha.codigo FROM admin_hoja_anestesia aha WHERE aha.articulo = ? AND aha.numero_solicitud = ? AND aha.seccion = ?  ) ";

	/**
	 * Consulta la informacion de los articulos de Hoja de Anestesia
	 */
	public static String strConsultarArticulosHojaAnes = "SELECT "
			+ "ahai.codigo, "
			+ "ahai.activo,"
			+ "ahai.articulo,"
			+ "getdescarticulo(ahai.articulo) AS descripcion_articulo,"
			+ "ahai.centro_costo,"
			+ "ahai.seccion,"
			+ "ahai.tipo_liquido,"
			+ "CASE WHEN ahai.tipo_liquido IS NULL THEN '' ELSE getdescripciontipoliquidoanes(lach.tipos_liquidos_hanes) END AS descripcion_tipo_liquido, "
			+ "na.es_pos AS espos "
			+ "FROM articulos_hoja_anes_inst ahai "
			+ "LEFT JOIN liquidos_admin_cc_hanes lach ON (lach.codigo = ahai.tipo_liquido) "
			+ "LEFT OUTER JOIN articulo a ON (ahai.articulo=a.codigo) "
			+ "LEFT OUTER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) "
			+ "WHERE ahai.institucion = ? ";
	
	/**
	 * actualiza la informacion del pedido qx
	 * */
	public static String strActualizarCantidadDetMaterialesQx = "UPDATE " +
			"det_materiales_qx " +
			"SET " +
			"cantidad = (SELECT SUM(d.dosis) FROM det_admin_hoja_anes d WHERE d.genero_consumo = '"+ConstantesBD.acronimoSi+"' AND d.codigo_det_mate_qx = ? ) " +
			"WHERE " +
			"codigo = ? ";
	
	/**
	 * Consulta los codigos del detalle de materiales qx
	 * */
	public static String strConsultarCodigosDetMateQx = 
			"SELECT " +
			"d.codigo_det_mate_qx " +
			"FROM " +
			"det_admin_hoja_anes d " +
			"INNER JOIN admin_hoja_anestesia a ON (a.codigo = d.admin_hoja_anes AND a.numero_solicitud = ? AND a.seccion = ? ) " +
			"INNER JOIN det_materiales_qx mat ON (mat.codigo = d.codigo_det_mate_qx AND mat.cantidad != d.dosis) " +
			"WHERE " +
			"d.genero_consumo = '"+ConstantesBD.acronimoSi+"' " +			
			"AND d.codigo_det_mate_qx IS NOT NULL " +
			"GROUP BY d.codigo_det_mate_qx ";

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION DE INFUSIONES*******************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Consulta la informaci�n de las infusiones de la hoja de anestesia
	 */
	public static String strConsultarInfusionesHojaAnes = "SELECT "
			+ "codigo AS codigo0," + "numero_solicitud AS numero_solicitud1,"
			+ "mezcla_inst_cc AS codigo_mezcla_ins_cc12,"
			+ "getdescripcionmezcla(mezcla_inst_cc) AS descripcion_mezcla3, "
			+ "otra_infusion AS otra_infusion4," + "'"
			+ ConstantesBD.acronimoSi + "' AS estabd5,"
			+ "graficar AS graficar6," + "graficar AS graficarold6,"
			+ "suspendido AS suspendido7," + "suspendido AS suspendidoold7,"
			+ "CASE WHEN mezcla_inst_cc IS NULL THEN '"
			+ ConstantesBD.acronimoSi + "' ELSE '" + ConstantesBD.acronimoNo
			+ "' END AS  es_otra_infusion11 "
			+ "FROM infusiones_hoja_anes iha " + "WHERE numero_solicitud = ? ";

	/**
	 * Consulta la informaci�n de las infusiones Agrupadas por mezcla y
	 * articulo
	 */
	public static String strConsultarInfusionesAgrupadas = "SELECT "
			+ "iha.mezcla_inst_cc AS codigo_mezcla_ins_cc12,"
			+ "getdescripcionmezcla(iha.mezcla_inst_cc) AS descripcion_mezcla3, "
			+ "iha.otra_infusion AS otra_infusion4,"
			+ "CASE WHEN iha.mezcla_inst_cc IS NULL THEN '"
			+ ConstantesBD.acronimoSi
			+ "' ELSE '"
			+ ConstantesBD.acronimoNo
			+ "' END AS es_otra_infusion11, "
			+ "det.articulo AS articulo2,"
			+ "getdescarticulo(det.articulo) AS descripcion_articulo3,"
			+ "SUM(dosis) AS dosis4 "
			+ "FROM infusiones_hoja_anes iha "
			+ "LEFT OUTER JOIN mezclas_inst_cc mcc ON (mcc.consecutivo = iha.mezcla_inst_cc) "
			+ "INNER JOIN adm_infusiones_hoja_anes adm ON (adm.cod_info_hoja_anes = iha.codigo) "
			+ "INNER JOIN det_infusion_hoja_anes det ON (det.cod_adm_info_hoja_anes = adm.codigo AND det.genero_consumo = '"
			+ ConstantesBD.acronimoNo
			+ "') "
			+ "WHERE iha.numero_solicitud = ? "
			+ "GROUP BY iha.mezcla_inst_cc,iha.otra_infusion,det.articulo,mcc.codigo_mezcla "
			+ "ORDER BY es_otra_infusion11,descripcion_articulo3 ";

	/**
	 * Actualiza la informaci�n de las infusiones de la hoja de anestesia
	 */
	public static String strActualizarInfusionesHojaAnes = "UPDATE  "
			+ "infusiones_hoja_anes " + "SET " + "graficar = ?,"
			+ "suspendido = ?," + "usuario_modifica = ?,"
			+ "fecha_modifica = ?," + "hora_modifica = ?  " + "WHERE "
			+ "codigo = ? ";

	/**
	 * Consulta la informaci�n de las administraciones de infusiones de la
	 * hoja de anestesia
	 */
	public static String strConsultarAdmInfusionesHojaAnes = "SELECT "
			+ "codigo AS codigo0,"
			+ "cod_info_hoja_anes AS cod_info_hoja_anes1,"
			+ "TO_CHAR(fecha,'DD/MM/YYYY') AS fecha2," + "hora AS hora3," + "'"
			+ ConstantesBD.acronimoSi + "' AS estabd4,"
			+ "TO_CHAR(fecha,'DD/MM/YYYY') AS fechaold2," + "hora AS horaold3 "
			+ "FROM adm_infusiones_hoja_anes " + "WHERE "
			+ "cod_info_hoja_anes = ? ";

	/**
	 * Actualiza la informaci�n de las administraciones de infusiones de la
	 * hoja de anestesia
	 */
	public static String strActualizarAdmInfusionesHojaAnes = "UPDATE "
			+ "adm_infusiones_hoja_anes " + "SET " + "fecha = ?," + "hora = ?,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ?  " + "WHERE " + "codigo = ? ";

	/**
	 * Consulta la informaci�n del detalle de las administraciones de
	 * infusiones de la hoja de anestesia
	 */
	public static String strConsultarDetInfusionHojaAnes = "SELECT "
			+ "codigo AS codigo0,"
			+ "cod_adm_info_hoja_anes AS cod_adm_info_hoja_anes1,"
			+ "articulo AS articulo2,"
			+ "getdescarticulo(articulo) AS descripcion_articulo3,"
			+ "dosis AS dosis4," + "dosis AS dosisold4," + "'"
			+ ConstantesBD.acronimoSi + "' AS estabd5,"
			+ "getnaturalezaarticulo(articulo) AS naturaleza_articulo6, "
			+ "getnaturalezaarticulo(articulo) AS espos9 "
			+ "FROM " + "det_infusion_hoja_anes " + "WHERE ";

	/**
	 * Actualizar la informaci�n del detalle de las administraciones de
	 * infusiones de la hoja de anestesia
	 */
	public static String strActualizarDetInfusionHojaAnes = "UPDATE "
			+ "det_infusion_hoja_anes " + "SET " + "dosis = ? ,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ? " + "WHERE " + "codigo = ? ";

	/**
	 * Actualiza el indicador de generacion de consumo del detalle de la
	 * infusion
	 */
	public static String strActualizarGenConsuArtMezcla = "UPDATE "
			+ "det_infusion_hoja_anes  "
			+ "SET "
			+ "genero_consumo = ?, "
			+ "usuario_modifica = ?,"
			+ "fecha_modifica = ?,"
			+ "hora_modifica = ?," 
			+ "codigo_det_mate_qx = ? "
			+ "WHERE "
			+ "articulo = ? AND "
			+ "genero_consumo != '"+ConstantesBD.acronimoSi+"' AND codigo_det_mate_qx IS NULL AND "
			+ "cod_adm_info_hoja_anes IN "
			+ "(SELECT adm.codigo FROM adm_infusiones_hoja_anes adm "
			+ " INNER JOIN infusiones_hoja_anes i ON (i.codigo = adm.cod_info_hoja_anes "
			+ "	AND i.numero_solicitud = ? " + ConstantesBD.separadorSplit
			+ " ))";

	/**
	 * Consulta la informaci�n de las mezclas parametrizadas
	 */
	public static String strConsultaMezclas = "SELECT " + "cc.consecutivo,"
			+ "cc.codigo_mezcla," + "m.nombre AS nombre_mezcla,"
			+ "cc.centro_costo " + "FROM mezclas_inst_cc cc "
			+ "INNER JOIN mezcla m ON (m.consecutivo = cc.codigo_mezcla) "
			+ "WHERE " + "cc.institucion = ? AND " + "cc.activo = '"
			+ ConstantesBD.acronimoSi + "' ";

	/**
	 * Consulta los articulos relacionados con las mezclas
	 */
	public static String strConsultarArticulosMezclas = "SELECT "
			+ "am.articulo AS articulo2,"
			+ "getdescarticulo(am.articulo) AS descripcion_articulo3,"
			+"na.es_pos AS espos9,"
			+ "'"+ ConstantesBD.acronimoSi+"' AS estabd5_ "
			+ "FROM articulos_por_mezcla am "
			+ "INNER JOIN mezclas_inst_cc cc ON(cc.consecutivo = ? AND cc.codigo_mezcla = am.mezcla ) "
			+ "INNER JOIN articulo a ON (am.articulo=a.codigo) "
			+ "INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " ;
	
	/**
	 * Consulta los codigos del detalle de materiales qx
	 * */
	public static String strConsultarCodigosDetMateQxInfusiones = 
			"SELECT " +
			"d.codigo_det_mate_qx " +
			"FROM " +
			"det_infusion_hoja_anes d " +
			"INNER JOIN infusiones_hoja_anes i ON (i.numero_solicitud = ?) " +
			"INNER JOIN adm_infusiones_hoja_anes adm ON (adm.cod_info_hoja_anes = i.codigo ) " +
			"INNER JOIN det_materiales_qx mat ON (mat.codigo = d.codigo_det_mate_qx AND mat.cantidad != d.dosis) " +			
			"WHERE " +
			"d.cod_adm_info_hoja_anes = adm.codigo AND " +
			"d.genero_consumo = '"+ConstantesBD.acronimoSi+"' " +			
			"AND d.codigo_det_mate_qx IS NOT NULL " +
			"GROUP BY d.codigo_det_mate_qx ";
	
	/**
	 * actualiza la informacion del pedido qx
	 * */
	public static String strActualizarCantidadDetMaterialesQxInfusiones = "UPDATE " +
			"det_materiales_qx " +
			"SET " +
			"cantidad = (SELECT SUM(d.dosis) FROM det_infusion_hoja_anes d WHERE d.genero_consumo = '"+ConstantesBD.acronimoSi+"' AND d.codigo_det_mate_qx = ? ) " +
			"WHERE " +
			"codigo = ? ";

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION DE BALANCE DE LIQUIDOS**********************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Inserta la informacion de hoja de anestesia balances liquidos
	 */
	public static String strInsertarHojaAnesBalanLiq = "INSERT INTO "
			+ "hoja_anes_balance_liquidos (numero_solicitud,codigo_liquido,cantidad,usuario_modifica,fecha_modifica,hora_modifica) "
			+ "VALUES(?,?,?,?,?,?) ";

	/**
	 * Consulta la informacion de hoja de anestesia balances liquidos
	 */
	public static String strConsultarBalanceLiq = "SELECT "
			+ "ha.numero_solicitud AS numero_solicitud0,"
			+ "ha.codigo_liquido AS codigo_liquido1,"
			+ "ha.cantidad AS cantidad2,"
			+ "getDescripcionLiquido(bl.liquido) AS descripcion_liquido3,"
			+ "'"+ ConstantesBD.acronimoSi+ "' AS estabd4 "
			+ "FROM hoja_anes_balance_liquidos ha "
			+ "INNER JOIN balance_liquidos_anes_inst bl ON(bl.codigo = ha.codigo_liquido ) "
			+ "WHERE " + "ha.numero_solicitud = ? "
			+ "ORDER BY descripcion_liquido3 ASC ";

	/**
	 * Actualiza la informaci�n de Hoja de Anestesia Balances Liquidos
	 */
	public static String strActualizarBalanceLiq = "UPDATE "
			+ "hoja_anes_balance_liquidos " + "SET cantidad = ? ,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ? " + "WHERE numero_solicitud = ? AND "
			+ "codigo_liquido = ? ";

	/**
	 * Elimina un registro de la Hoja de Anestesia Balances Liquidos
	 */
	public static String strEliminarBalanceLiq = "DELETE "
			+ "FROM hoja_anes_balance_liquidos "
			+ "WHERE numero_solicitud = ? AND " + "codigo_liquido = ? ";

	/**
	 * Consula la informaci�n del listado de otros liquidos
	 */
	public static String strConsultarOtrosLiquidosBalance = "SELECT "
			+ "codigo," + "liquido,"
			+ "getDescripcionLiquido(liquido) AS descripcion_liquido "
			+ "FROM balance_liquidos_anes_inst " + "WHERE "
			+ "institucion  = ? ";

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * ATRIBUTOS PARA LA SECCION SALIDA DEL PACIENTE*************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Consulta la informaci�n de las salidas_pac_inst_cc
	 */
	public static String strConsultarSalidasPacInstCC = "SELECT "
			+ "consecutivo," + "salida_pac,"
			+ "getDescripcionSalidaPac(salida_pac) AS descripcion_pac, "
			+ "fallece " + "FROM " + "salidas_pac_inst_cc "
			+ "WHERE institucion = ? " + "AND activo = '"
			+ ConstantesBD.acronimoSi + "' ";

	/**
	 * Consulta la informacion de la salida del paciente
	 */
	public static String strConsultarSalidasPaciente = "SELECT "
			+ "sp.consecutivo AS consecutivo0,"
			+ "sp.numero_solicitud AS numero_solicitud1,"
			+ "sp.salida_paciente_cc AS salida_paciente_cc2,"
			+ "getDescripcionSalidaPac(spic.salida_pac) AS descripcion_salida_paciente3,"
			+ "spic.fallece AS fallece4 "
			+ "FROM salidas_sala_paciente sp "
			+ "INNER JOIN salidas_pac_inst_cc spic ON (spic.consecutivo = sp.salida_paciente_cc) "
			+ "WHERE numero_solicitud = ? ";

	/**
	 * Inserta valores en la tabla salidas_sala_paciente
	 */
	public static String strInsertarSalidasSalaPaciente = "INSERT INTO "
			+ "salascirugia.salidas_sala_paciente(consecutivo,numero_solicitud,salida_paciente_cc,usuario_modifica) "
			+ "VALUES(?,?,?,?) ";
	
	public static String strConsultarIndicativoRegistroDesde = "SELECT "
			+ "sc.ha_registrada_desde "
			+ "FROM salascirugia.solicitudes_cirugia sc "
			+ "WHERE sc.numero_solicitud = ? ";
									
	public static String strActualizarIndicativoRegistroDesde = "UPDATE "
			+ "salascirugia.solicitudes_cirugia SET ha_registrada_desde = ? "
			+ "WHERE numero_solicitud = ? ";
	
	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE INFORMACION GENERAL************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------

	// *********************************************************************************************
	// Especialidades que Intervienen
	// *********************************************************************************************
	/**
	 * Insertar Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarEspecialidadesIntervienen(Connection con,
			HashMap parametros) {
		// System.out.print("\n entro a insertarEspecialidadesIntervienen -->
		// "+parametros);
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarEspecialidadesIntervienen,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			/**
			 * INSERT INTO "
			+ "esp_intervienen_sol_cx(numero_solicitud,especialidad,asignada,usuario_modifica,fecha_modifica,hora_modifica)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesEspecialidadesInter[0])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesEspecialidadesInter[1])+""));
			ps.setString(3, parametros.get(HojaAnestesia.indicesEspecialidadesInter[3])+"");
			ps.setString(4, parametros.get("usuarioModifica")+"");
			ps.setDate(5, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(6, parametros.get("horaModifica")+"");
			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consultar Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarEspecialidadesIntervienen(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultaEspecIntervienen;

		if (parametros.containsKey("especialidad")
				&& !parametros.get("especialidad").toString().equals(""))
			cadena += " AND especialidad = "
					+ parametros.get("especialidad").toString();

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("solicitud")+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Actualizar Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarEspecialidadesIntervienen(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarEspecIntervienen, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setString(1, parametros.get("asignada")+"");
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("numeroSolicitud")+""));
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get("especialidad")+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Eliminar Especialidades Intervienen 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean eliminarEspecialidadesIntervienen(Connection con,HashMap parametros) {
		String cadena = strEliminarEspecIntervienen;

		if (parametros.containsKey(HojaAnestesia.indicesEspecialidadesInter[1])
				&& !parametros.get(HojaAnestesia.indicesEspecialidadesInter[1]).toString().equals(""))
			cadena += " AND especialidad = "+ parametros.get(HojaAnestesia.indicesEspecialidadesInter[1]);

		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesEspecialidadesInter[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// *********************************************************************************************
	// Cirujanos Especialidades Intervienen
	// *********************************************************************************************

	/**
	 * Insertar Cirujanos Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarCirujanosIntervienen(Connection con,
			String strInsertar, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertar,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO " +
			"cirujanos_esp_int_solcx (consecutivo,numero_solicitud,especialidad,profesional,asignada,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES('seq_cirujanos_esp_int'),?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[2])+""));
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[4])+""));
			ps.setString(4, parametros.get(HojaAnestesia.indicesCirujanosPrincipales[6])+"");
			ps.setString(5, parametros.get("usuarioModifica")+"");
			ps.setDate(6, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(7, parametros.get("horaModifica")+"");

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consultar Cirujanos Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarCirujanosIntervienen(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strConsultarCirujanosEspecialidad,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[2])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static HashMap consultarCirujanosSolicitud(Connection con,
			String solicitud){
		
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strconsultarCirujanosServicios,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(solicitud+""));


			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
			
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	// **********************************************************************************************

	/**
	 * Actualizar Cirujanos Especialidades Intervienen
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarCirujanosIntervienen(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarCirujanosEspecialidad,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "cirujanos_esp_int_solcx SET " + "asignada = ?,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ?  " + "WHERE " + "consecutivo = ? 
			 */
			
			ps.setString(1, parametros.get(HojaAnestesia.indicesCirujanosPrincipales[6])+"");

			ps.setString(2, parametros.get("usuarioModifica")+"");
			ps.setDate(3, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(4, parametros.get("horaModifica")+"");

			ps.setDouble(5, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesCirujanosPrincipales[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Eliminar Especialidades Intervienen
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean eliminarCirujanosIntervienen(Connection con,HashMap parametros) 
	{
		String cadena = strEliminaCirujanosEspecialidad;

		// Consecutivo
		if (parametros.containsKey(HojaAnestesia.indicesCirujanosPrincipales[0])
				&& !parametros.get(HojaAnestesia.indicesCirujanosPrincipales[0]).equals(""))
			cadena += " AND consecutivo =  "+ parametros.get(HojaAnestesia.indicesCirujanosPrincipales[0]).toString();

		// Numero Solicitud
		if (parametros.containsKey(HojaAnestesia.indicesCirujanosPrincipales[1]) && !parametros
						.get(HojaAnestesia.indicesCirujanosPrincipales[1]).equals(""))
			cadena += " AND numero_solicitud =  "+ parametros.get(HojaAnestesia.indicesCirujanosPrincipales[1]).toString();

		// especialidad
		if (parametros.containsKey(HojaAnestesia.indicesCirujanosPrincipales[2])
				&& !parametros.get(HojaAnestesia.indicesCirujanosPrincipales[2]).equals(""))
			cadena += " AND especialidad =  "+ parametros.get(HojaAnestesia.indicesCirujanosPrincipales[2]).toString();

		// asignado
		if (parametros.containsKey(HojaAnestesia.indicesCirujanosPrincipales[6])
				&& !parametros.get(HojaAnestesia.indicesCirujanosPrincipales[6]).equals(""))
			cadena += " AND asignada =  '"+ parametros.get(HojaAnestesia.indicesCirujanosPrincipales[6]).toString() + "'";

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************
	// *********************************************************************************************
	// Anestesiologos
	// *********************************************************************************************

	/**
	 * Insertar Anestesiologos 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean insertarAnestesiologos(Connection con,HashMap parametros) 
	{
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarAnestesiologos, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO "
			+ "anestesiologos_hoja_anes (numero_solicitud,profesional,fecha,hora,definitivo) "
			+ "VALUES(?,?,?,?,?) 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[0])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[1])+""));
			ps.setDate(3, Date.valueOf(parametros.get(HojaAnestesia.indicesAnestesiologos[3])+""));
			ps.setString(4, parametros.get(HojaAnestesia.indicesAnestesiologos[4])+"");
			ps.setString(5, parametros.get(HojaAnestesia.indicesAnestesiologos[5])+"");

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consultar los Anestesiologos 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarAnestesiologos(Connection con,HashMap parametros) 
	{
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultarAnestesiologos;

		if (parametros.containsKey(HojaAnestesia.indicesAnestesiologos[1])
				&& !parametros.get(HojaAnestesia.indicesAnestesiologos[1]).toString().equals(""))
			cadena += " AND profesional = "+ parametros.get(HojaAnestesia.indicesAnestesiologos[1]).toString();

		if (parametros.containsKey(HojaAnestesia.indicesAnestesiologos[5])
				&& !parametros.get(HojaAnestesia.indicesAnestesiologos[5]).toString().equals(""))
			cadena += " AND definitivo = '"+ parametros.get(HojaAnestesia.indicesAnestesiologos[5]).toString() + "' ";

		cadena += " ORDER BY fecha,hora ASC ";

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[0])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Consulta si existe o no el Anestesiologo 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean consultarExisteAnestesiologo(Connection con,HashMap parametros) 
	{
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarExisteAnestesiologo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[0])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[1])+""));

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				if (rs.getInt(1) > 0)
					return true;
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Actualizar Anestesiologos 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarAnestesiologos(Connection con,HashMap parametros) 
	{
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarAnestesiologos, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(parametros.get(HojaAnestesia.indicesAnestesiologos[3])+""));
			ps.setString(2, parametros.get(HojaAnestesia.indicesAnestesiologos[4])+"");
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[0])+""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Actualizar el campo cobrable de la Hoja de Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarCobrable(Connection con, HashMap parametros) {
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarCobrable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get(HojaAnestesia.indicesHojaAnestesia[1])+"");
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Actualizar el campo definitivo para el cobro de honorarios
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarDefinitivoHonorarios(Connection con,HashMap parametros) 
	{
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarDefinitivo, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get(HojaAnestesia.indicesAnestesiologos[5])+"");
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[0])+""));
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesAnestesiologos[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// *********************************************************************************************
	// Via Aerea
	// *********************************************************************************************

	/**
	 * Consultar cantidades detalle via aerea 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarCantidadesDetalleViaAerea(Connection con,HashMap parametros) 
	{
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaCantidadesViaArea, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesViaAerea[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesViaAerea[2])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return respuesta;
	}

	// *********************************************************************************************

	/**
	 * Actualizar el indicativo del detalle de la via aerea 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarGenConsuDetViaArea(Connection con,HashMap parametros) 
	{		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarGenConsuDetViaArea,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get(HojaAnestesia.indicesDetViaAerea[3])+"");
			
			if(Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetViaAerea[5])+"")>0)
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetViaAerea[5])+""));
			else
				ps.setNull(2,Types.INTEGER);
				
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetViaAerea[0])+""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetViaAerea[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// *********************************************************************************************
	// Fecha y Hora de Ingreso Sala
	// *********************************************************************************************

	/**
	 * Actualizar la fecha y la hora de ingreso a la Sala 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarFechaHoraIngreso(Connection con,HashMap parametros) 
	{
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarFechaHoraIngreso, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(parametros.get(HojaAnestesia.indicesSolicitudesCx[2])+""));
			ps.setString(2, parametros.get(HojaAnestesia.indicesSolicitudesCx[3])+"");
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesSolicitudesCx[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE INFORMACION GENERAL************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------

	/**
	 * Consultar Observaciones Generales 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarObservacionesGenerales(Connection con,HashMap parametros) 
	{
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarObservacionesGenerales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesObservacionesGenerales[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Insertar Observaciones Generales
	 * 
	 * @param Connection
	 *            con
	 * @param String
	 *            insertarObservaciones
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarObservacionesGenerales(Connection con,
			String insertarObservaciones, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarObservaciones,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO " +
			"observaciones_hoja_anes(codigo,numero_solicitud,descripcion,fecha,hora,usuario) VALUES('seq_observa_hoja_anes'),?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesObservacionesGenerales[1])+""));
			ps.setString(2, parametros.get(HojaAnestesia.indicesObservacionesGenerales[2])+"");
			ps.setDate(3, Date.valueOf(parametros.get(HojaAnestesia.indicesObservacionesGenerales[3])+""));
			ps.setString(4, parametros.get(HojaAnestesia.indicesObservacionesGenerales[4])+"");
			ps.setString(5, parametros.get(HojaAnestesia.indicesObservacionesGenerales[5])+"");

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE ANESTESICOS Y MEDICAMENTOS
	 * ADMINISTRADOS*************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Insertar Administraciones Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param String
	 *            insertarAdminHojaAnes
	 * @param HashMap
	 *            parametros
	 * @return int numero del consecutivo con que se genero el registro
	 */
	public static int insertarAdminisHojaAnest(Connection con,
			String insertarAdminHojaAnes, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarAdminHojaAnes,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO admin_hoja_anestesia(codigo,numero_solicitud,articulo,tipo_liquido,otro_medicamento,cantidad,seccion) " +
						"VALUES('seq_admin_hoja_anes'),?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[2])+""));

			// Tipo de liquido
			if (parametros.containsKey(HojaAnestesia.indicesMedAdminHojaAnes[9])
					&& !parametros
							.get(HojaAnestesia.indicesMedAdminHojaAnes[9])
							.toString().equals(""))
				ps.setDouble(3, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[9])+""));
			else
				ps.setNull(3, Types.NUMERIC);

			ps.setString(4, parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[3])+"");

			if (parametros
					.containsKey(HojaAnestesia.indicesMedAdminHojaAnes[4])
					&& !parametros
							.get(HojaAnestesia.indicesMedAdminHojaAnes[4])
							.equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[4])+""));
			else
				ps.setNull(5, Types.INTEGER);

			ps.setDouble(6, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[5])+""));

			if (ps.executeUpdate() > 0) {
				String cadena = "SELECT codigo "
						+ "FROM admin_hoja_anestesia "
						+ "WHERE numero_solicitud = "
						+ parametros.get(
								HojaAnestesia.indicesMedAdminHojaAnes[1])
								.toString()
						+ " "
						+ "AND articulo = "
						+ parametros.get(
								HojaAnestesia.indicesMedAdminHojaAnes[2])
								.toString()
						+ " "
						+ "AND seccion = "
						+ parametros
								.get(HojaAnestesia.indicesMedAdminHojaAnes[5])
						+ " ";

				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet ));
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

				if (rs.next())
					return rs.getInt("codigo");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("parametros >> "+parametros);
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************************************

	/**
	 * Consultar Administraciones Hoja Anestesia
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarAdminisHojaAnest(Connection con,HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultarAdminHojaAnest;

		if (parametros.containsKey("calcularCantidad")
				&& parametros.get("calcularCantidad").toString().equals(
						ConstantesBD.acronimoSi)) 
		{
			// Verifica si la cantidad se realiza solo con los articulos en los
			// cuales no se hubiera generado consumo
			if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[6]) 
					&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[6]).toString().equals(""))
				cadena = cadena.replace(ConstantesBD.separadorSplit,
										" (SELECT SUM(det.dosis) "
										+ "FROM det_admin_hoja_anes det "
										+ "WHERE det.admin_hoja_anes = aha.codigo "
										+ "AND det.genero_consumo = '"
										+ parametros
												.get(HojaAnestesia.indicesDetMedAdminHojaAnes[6])
										+ "') AS cantidad4, ");
			else
				cadena = cadena.replace(ConstantesBD.separadorSplit,
								" (SELECT SUM(det.dosis) "
										+ "FROM det_admin_hoja_anes det "
										+ "WHERE det.admin_hoja_anes = aha.codigo) AS cantidad4, ");
		}
		else
			cadena = cadena.replace(ConstantesBD.separadorSplit,
					" aha.cantidad AS cantidad4, ");
		
		logger.info("valor de la cadena de consula >> "+cadena+" >> "+parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[1])+" >> "+parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[5]) );

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[1])+""));
			ps.setDouble(2, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[5])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Insertar detalle de los medicamentos administrados
	 * 
	 * @param Connection
	 *            con
	 * @param String
	 *            strInsertarDetAdminHojaAnestesia
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarDetaAdminisHojaAnest(Connection con,
			String strInsertarDetAdminHojaAnestesia, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarDetAdminHojaAnestesia,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO det_admin_hoja_anes(codigo,admin_hoja_anes,dosis,fecha,hora,graficar,
			 * genero_consumo,fecha_modifica,hora_modifica,usuario_modifica) " +
				"VALUES('seq_admin_hoja_anes'),?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[2])+""));
			ps.setDate(3, Date.valueOf(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[3])+""));
			ps.setString(4, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[4])+"");
			ps.setString(5, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[5])+"");
			ps.setString(6, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[6])+"");

			ps.setDate(7, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(8, parametros.get("horaModifica")+"");
			ps.setString(9, parametros.get("usuarioModifica")+"");
			
			if(parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[8]))
				ps.setString(10,parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[8]).toString());
			else
				ps.setString(10,"");

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consultar el detalle de Administraciones Hoja Anestesia
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarDetAdminisHojaAnest(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultaDetHojaAnest;

		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[0])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[0])
						.equals(""))
			cadena += " AND codigo = "
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[0])
							.toString();

		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[2])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[2])
						.equals(""))
			cadena += " AND dosis = "
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[2])
							.toString();

		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[3])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[3])
						.equals(""))
			cadena += " AND fecha = '"
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[3])
							.toString() + "' ";

		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[4])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[4])
						.equals(""))
			cadena += " AND hora = '"
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[4])
							.toString() + "' ";

		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[5])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[5])
						.equals(""))
			cadena += " AND graficar = '"
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[5])
							.toString() + "' ";
		
		if (parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[8])
				&& !parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[8])
						.equals(""))
			cadena += " AND sellocalidad = '"
					+ parametros.get(
							HojaAnestesia.indicesDetMedAdminHojaAnes[8])
							.toString() + "' ";

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Actualizar detalle de los medicamentos administrados
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarDetaAdminisHojaAnest(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarDetHojaAnest, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "det_admin_hoja_anes " + "SET dosis = ?," + "fecha = ?, "
			+ "hora = ?," + "graficar = ?," + "usuario_modifica = ?,"
			+ "fecha_modifica = ?," + "hora_modifica = ?  "
			+ "WHERE codigo = ? 
			 */
			
			logger.info("valor de parametros al actualizar >> "+parametros);
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[2])+""));
			ps.setDate(2, Date.valueOf(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[3])+""));
			ps.setString(3, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[4])+"");
			ps.setString(4, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[5])+"");
			ps.setString(5, parametros.get("usuarioModifica")+"");
			ps.setDate(6, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(7, parametros.get("horaModifica")+"");

			if(parametros.containsKey(HojaAnestesia.indicesDetMedAdminHojaAnes[8]+""))
				ps.setString(8, parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[8])+"");
			else
				ps.setString(8,"");			
			
			ps.setInt(9,Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[0]).toString()));			

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Actualiza el indicativo de genero consumo del detalle de los medicamentos
	 * administrados
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarGenConsumoDetaAdminis(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarGenConsumoDetHojaAnest,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "det_admin_hoja_anes d "
			+ "SET "
			+ "d.genero_consumo = ?,"
			+ "d.usuario_modifica = ?,"
			+ "d.fecha_modifica = ?,"
			+ "d.hora_modifica = ? "
			+ "WHERE d.admin_hoja_anes = "
			+ "(SELECT aha.codigo FROM admin_hoja_anestesia aha WHERE aha.articulo = ? AND aha.numero_solicitud = ? AND aha.seccion = ?  )
			 */
			
			ps.setString(1,parametros.get(HojaAnestesia.indicesDetMedAdminHojaAnes[6])+"");
			ps.setString(2,parametros.get("usuarioModifica")+"");
			ps.setDate(3,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(4,parametros.get("horaModifica")+"");
			
			if(Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[13]).toString()) > 0)	
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[13]).toString()));
			else
				ps.setNull(5,Types.INTEGER);

			ps.setInt(6, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[2])+""));
			ps.setInt(7, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[1])+""));
			ps.setDouble(8, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesMedAdminHojaAnes[5])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE
	 * INFUSIONES*********************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Consultar Infusiones Hoja Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarInfusionesHojaAnestesia(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strConsultarInfusionesHojaAnes, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return respuesta;
	}

	// **********************************************************************************************

	/**
	 * Consultar Infusiones Agrupadas por mezcla y articulo
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarInfusionesAgrupadas(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strConsultarInfusionesAgrupadas,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			logger.info("valor de la cadena consultarInfusionesAgrupadas >> "+strConsultarInfusionesAgrupadas+" >> "+parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1]));
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Insertar Infusiones Hoja de Anestesia
	 * 
	 * @param Connection con
	 * @param String strInsertarInfusionesHojaAnestesia
	 * @param HashMap parametros
	 */
	public static int insertarInfusionesHojaAnes(Connection con,
			String strInsertarInfusionesHojaAnestesia, HashMap parametros) {

		// Consulta del consecutivo
		String cadena = "SELECT codigo FROM infusiones_hoja_anes "
				+ "WHERE numero_solicitud = "
				+ parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1])
				+ " " + "AND graficar = '"
				+ parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[6])
				+ "' " + "AND suspendido = '"
				+ parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[7])
				+ "' " + "AND usuario_modifica = '"
				+ parametros.get("usuarioModifica") + "' "
				+ "AND fecha_modifica = '" + parametros.get("fechaModifica")
				+ "' " + "AND hora_modifica = '"
				+ parametros.get("horaModifica") + "' ";
		try {

			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarInfusionesHojaAnestesia,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO infusiones_hoja_anes (codigo,numero_solicitud,mezcla_inst_cc,otra_infusion,graficar,suspendido,usuario_modifica,fecha_modifica,hora_modifica) " +
					"VALUES('seq_infusiones_hoja_anes'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1])+""));

			if (parametros
					.containsKey(HojaAnestesia.indicesInfusionesHojaAnes[12])
					&& !parametros.get(
							HojaAnestesia.indicesInfusionesHojaAnes[12])
							.equals("")) {
				ps.setDouble(2, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[12])+""));
				cadena += " AND mezcla_inst_cc = "
						+ parametros
								.get(HojaAnestesia.indicesInfusionesHojaAnes[12]);
			} else {
				ps.setNull(2, Types.NULL);
				cadena += " AND mezcla_inst_cc IS NULL ";
			}

			if (parametros
					.containsKey(HojaAnestesia.indicesInfusionesHojaAnes[4])
					&& !parametros.get(
							HojaAnestesia.indicesInfusionesHojaAnes[4]).equals(
							"")) {
				ps.setString(3, parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[4])+"");
				
				cadena += " AND otra_infusion = '"
						+ parametros
								.get(HojaAnestesia.indicesInfusionesHojaAnes[4])
						+ "' ";
			} else {
				ps.setNull(3, Types.NULL);
				cadena += " AND otra_infusion IS NULL ";
			}

			ps.setString(4, parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[6])+"");
			ps.setString(5, parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[7])+"");
			ps.setString(6, parametros.get("usuarioModifica")+"");
			ps.setDate(7, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(8, parametros.get("horaModifica")+"");

			if (ps.executeUpdate() > 0) {
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet ));
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

				if (rs.next())
					return rs.getInt(1);
				else
					logger.info("\n\n la consulta no dio resultados consecutivo");
			} else {
				logger.info("\n\n no inserto datos infusiones");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************************************

	/**
	 * Actualiza la informacion de infusiones de la Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarInfusionesHojaAnes(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarInfusionesHojaAnes,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE  "
			+ "infusiones_hoja_anes " + "SET " + "graficar = ?,"
			+ "suspendido = ?," + "usuario_modifica = ?,"
			+ "fecha_modifica = ?," + "hora_modifica = ?  " + "WHERE "
			+ "codigo = ? 
			 */
			
			ps.setString(1, parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[6])+"");
			ps.setString(2, parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[7])+"");

			ps.setString(3, parametros.get("usuarioModifica")+"");
			ps.setDate(4, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(5, parametros.get("horaModifica")+"");

			ps.setDouble(6, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consulta la informacion de las administraciones de las infusiones de la
	 * hoja de anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarAdmInfusionesHojaAnes(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultarAdmInfusionesHojaAnes;

		if (parametros
				.containsKey(HojaAnestesia.indicesAdmInfusionesHojaAnes[0])
				&& !parametros.get(
						HojaAnestesia.indicesAdmInfusionesHojaAnes[0]).equals(
						""))
			cadena += " AND codigo = "
					+ parametros
							.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[0]);

		if (parametros
				.containsKey(HojaAnestesia.indicesAdmInfusionesHojaAnes[2])
				&& !parametros.get(
						HojaAnestesia.indicesAdmInfusionesHojaAnes[2]).equals(
						""))
			cadena += " AND fecha = '"
					+ parametros
							.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[2])
					+ "' ";

		if (parametros
				.containsKey(HojaAnestesia.indicesAdmInfusionesHojaAnes[3])
				&& !parametros.get(
						HojaAnestesia.indicesAdmInfusionesHojaAnes[3]).equals(
						""))
			cadena += " AND hora = '"
					+ parametros
							.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[3])
					+ "' ";

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	// **********************************************************************************************

	/**
	 * Actualiza la informacion de las administraciones de la hoja de anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarAdmInfusionesHojaAnes(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarAdmInfusionesHojaAnes,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setDate(1, Date.valueOf(parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[2])+""));
			ps.setString(2, parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[3])+"");

			ps.setString(3, parametros.get("usuarioModifica")+"");
			ps.setDate(4, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(5, parametros.get("horaModifica")+"");

			ps.setDouble(6, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Insertar Admisiones Infusiones Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param String
	 *            strInsertarAdmInfusionesHojaAnestesia
	 * @param HashMap
	 *            parametros
	 */
	public static int insertarAdmInfusionesHojaAnes(Connection con,
			String strInsertarAdmInfusionesHojaAnestesia, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarAdmInfusionesHojaAnestesia,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO adm_infusiones_hoja_anes (codigo,cod_info_hoja_anes,fecha,hora,usuario_modifica,fecha_modifica,hora_modifica) " +
					"VALUES('seq_adm_infusiones_hoja_anes'),?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[1])+""));
			ps.setDate(2, Date.valueOf(parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[2])+""));
			ps.setString(3, parametros.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[3])+"");

			ps.setString(4, parametros.get("usuarioModifica")+"");
			ps.setDate(5, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(6, parametros.get("horaModifica")+"");

			if (ps.executeUpdate() > 0) {
				String cadena = "SELECT codigo FROM adm_infusiones_hoja_anes "
						+ "WHERE "
						+ "cod_info_hoja_anes = "
						+ parametros
								.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[1])
						+ " "
						+ "AND fecha = '"
						+ parametros
								.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[2])
						+ "' "
						+ "AND hora = '"
						+ parametros
								.get(HojaAnestesia.indicesAdmInfusionesHojaAnes[3])
						+ "' " + "AND usuario_modifica = '"
						+ parametros.get("usuarioModifica") + "' "
						+ "AND fecha_modifica = '"
						+ parametros.get("fechaModifica") + "' "
						+ "AND hora_modifica = '"
						+ parametros.get("horaModifica") + "' ";

				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet ));
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

				if (rs.next())
					return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************************************

	/**
	 * Consulta la informacion de los detalle de administraciones de las
	 * infusiones de la hoja de anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarDetInfusionesHojaAnes(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		String cadena = strConsultarDetInfusionHojaAnes;

		if (parametros
				.containsKey(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[1])
				&& !parametros.get(
						HojaAnestesia.indicesDetAdmInfusionesHojaAnes[1])
						.equals(""))
			cadena += "  cod_adm_info_hoja_anes = "
					+ parametros
							.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[1]);
		else if (parametros
				.containsKey(HojaAnestesia.indicesAdmInfusionesHojaAnes[1])
				&& !parametros.get(
						HojaAnestesia.indicesAdmInfusionesHojaAnes[1]).equals(
						""))
			cadena += "  cod_adm_info_hoja_anes = (SELECT MAX(adm.codigo) FROM adm_infusiones_hoja_anes adm WHERE adm.cod_info_hoja_anes = "
					+ parametros.get(
							HojaAnestesia.indicesAdmInfusionesHojaAnes[1])
							.toString() + " ) ";

		if (parametros
				.containsKey(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[0])
				&& !parametros.get(
						HojaAnestesia.indicesDetAdmInfusionesHojaAnes[0])
						.equals(""))
			cadena += " AND codigo = "
					+ parametros
							.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[0]);

		if (parametros
				.containsKey(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2])
				&& !parametros.get(
						HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2])
						.equals(""))
			cadena += " AND articulo = "
					+ parametros
							.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2]);

		if (parametros
				.containsKey(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4])
				&& !parametros.get(
						HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4])
						.equals(""))
			cadena += " AND dosis = "
					+ parametros
							.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4]);

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Insertar Detalle de Admisiones Infusiones Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param String
	 *            strInsertarDetInfusionesHojaAnestesia
	 * @param HashMap
	 *            parametros
	 */
	public static int insertarDetInfusionesHojaAnes(Connection con,
			String strInsertarDetInfusionesHojaAnestesia, HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarDetInfusionesHojaAnestesia,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO det_infusion_hoja_anes 
			 * (codigo,cod_adm_info_hoja_anes,articulo,dosis,genero_consumo,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES('seq_det_infu_hoja_anes'),?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[1])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2])+""));
			ps.setDouble(3, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4])+""));

			if (parametros
					.containsKey(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[8])
					&& !parametros.get(
							HojaAnestesia.indicesDetAdmInfusionesHojaAnes[8])
							.equals(""))
				ps.setString(4, parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[8])+"");
			else
				ps.setString(4, ConstantesBD.acronimoNo);

			ps.setString(5, parametros.get("usuarioModifica")+"");
			ps.setDate(6, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(7, parametros.get("horaModifica")+"");

			if (ps.executeUpdate() > 0) {
				String cadena = "SELECT codigo FROM det_infusion_hoja_anes "
						+ "WHERE "
						+ "cod_adm_info_hoja_anes = "
						+ parametros
								.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[1])
						+ " "
						+ "AND articulo = "
						+ parametros
								.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2])
						+ " "
						+ "AND dosis = "
						+ parametros
								.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4])
						+ " " + "AND usuario_modifica = '"
						+ parametros.get("usuarioModifica") + "' "
						+ "AND fecha_modifica = '"
						+ parametros.get("fechaModifica") + "' "
						+ "AND hora_modifica = '"
						+ parametros.get("horaModifica") + "' ";

				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet ));
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

				if (rs.next())
					return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************************************
	/**
	 * Actualiza la informacion del Detalle de la administracion de infusiones
	 * de la Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarDetInfusionesHojaAnes(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarDetInfusionHojaAnes,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "det_infusion_hoja_anes " + "SET " + "dosis = ? ,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ? " + "WHERE " + "codigo = ?
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[4])+""));

			ps.setString(2, parametros.get("usuarioModifica")+"");
			ps.setDate(3, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(4, parametros.get("horaModifica")+"");

			ps.setDouble(5, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[0])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************
	/**
	 * Actualiza el indicador de genero consumo para los articulos de un mezcla 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarGenConsuArtiMezcla(Connection con,HashMap parametros) 
	{

		String cadena = strActualizarGenConsuArtMezcla;
 
		if (parametros.containsKey(HojaAnestesia.indicesInfusionesHojaAnes[12])
				&& !parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[12]).toString().equals(""))
		{
			cadena = cadena
					.replace(
							ConstantesBD.separadorSplit,
							" AND i.mezcla_inst_cc =  "
									+ parametros
											.get(
													HojaAnestesia.indicesInfusionesHojaAnes[12])
											.toString());
		}
		else if (parametros
				.containsKey(HojaAnestesia.indicesInfusionesHojaAnes[4])
				&& !parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[4])
						.toString().equals(""))
		{
			cadena = cadena.replace(ConstantesBD.separadorSplit,
					" AND i.otra_infusion = UPPER('"
							+ parametros.get(
									HojaAnestesia.indicesInfusionesHojaAnes[4])
									.toString() + "') ");
		}
		else 
		{
			cadena = cadena.replace(ConstantesBD.separadorSplit," ");
		}

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "det_infusion_hoja_anes d "
			+ "SET "
			+ "d.genero_consumo = ?, "
			+ "d.usuario_modifica = ?,"
			+ "d.fecha_modifica = ?,"
			+ "d.hora_modifica = ? "
			+ "WHERE "
			+ "d.articulo = ? AND "
			+ "d.cod_adm_info_hoja_anes IN "
			+ "(SELECT list(adm.codigo||'') FROM adm_infusiones_hoja_anes adm "
			+ " INNER JOIN infusiones_hoja_anes i ON (i.codigo = adm.cod_info_hoja_anes "
			+ "	AND i.numero_solicitud = ? " + ConstantesBD.separadorSplit
			+ " ))
			 */
			
			logger.info("\n\n\nvalor de cadena actualizarGenConsuArtiMezcla >> "+cadena);
			ps.setString(1, parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[8])+"");

			ps.setString(2, parametros.get("usuarioModifica")+"");
			ps.setDate(3, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(4, parametros.get("horaModifica")+"");
			
			if(Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[10])+"") > 0 )
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[10]).toString()));
			else
				ps.setNull(5,Types.INTEGER);

			ps.setInt(6, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesDetAdmInfusionesHojaAnes[2])+""));
			ps.setInt(7, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesInfusionesHojaAnes[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE INFORMACION
	 * GENERAL************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------

	/**
	 * Insertar Hoja Anestesia Balance Liquidos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarBalancesLiquidosHojaAnes(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarHojaAnesBalanLiq, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO "
			+ "hoja_anes_balance_liquidos (numero_solicitud,codigo_liquido,cantidad,usuario_modifica,fecha_modifica,hora_modifica) "
			+ "VALUES(?,?,?,?,?,?) 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1])+""));
			ps.setObject(3, parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[2]));

			ps.setString(4, parametros.get("usuarioModifica")+"");
			ps.setDate(5, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(6, parametros.get("horaModifica")+"");

			if (ps.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consulta la informacion de la Hoja de Anestesia Balance Liquidos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarBalanceLiquidos(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarBalanceLiq,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Actualiza la informacion de los balances de liquidos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarBalancesLiquidos(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarBalanceLiq, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "hoja_anes_balance_liquidos " + "SET cantidad = ? ,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ? " + "WHERE numero_solicitud = ? AND "
			+ "codigo_liquido = ? 
			 */
			
			
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[2])+""));

			ps.setString(2, parametros.get("usuarioModifica")+"");
			ps.setDate(3, Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(4, parametros.get("horaModifica")+"");

			ps.setInt(5, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0])+""));
			ps.setInt(6, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Elimina la informacion de los balances de liquidos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean eliminarBalancesLiquidos(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarBalanceLiq,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * DELETE "
			+ "FROM hoja_anes_balance_liquidos "
			+ "WHERE numero_solicitud = ? AND " + "codigo_liquido = ?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[0])+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesBalancesLiquidosHojaAnes[1])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS PARA LA SECCION DE SALIDA SALA
	 * PACIENTE***********************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	/**
	 * Insertar Salidas de Paciente
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static int insertarSalidaSalaPaciente(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarSalidasSalaPaciente, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO "
			+ "salidas_sala_paciente(numero_solicitud,salida_paciente_cc,usuario_modifica) "
			+ "VALUES(?,?,?) 
			 */
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con, "salascirugia.seq_salidas_sala_paciente");
			ps.setInt(1,secuencia);
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[1])+""));
			ps.setDouble(3, Utilidades.convertirADouble(parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[2])+""));
			ps.setString(4, parametros.get("usuarioModifica")+"");

			if (ps.executeUpdate() > 0) {
				String cadena = "SELECT MAX(consecutivo) FROM salidas_sala_paciente WHERE numero_solicitud = "
						+ parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[1])+
						" AND salida_paciente_cc = "+ parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[2])+ " "
						+ "AND usuario_modifica = '"+ parametros.get("usuarioModifica") + "' ";

				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

				if (rs.next()) {
					if (!rs.getString(1).equals(""))
						return rs.getInt(1);
					else
						return ConstantesBD.codigoNuncaValido;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************************************

	/**
	 * Consultar la informacion de la Hoja de Anestesia
	 * 
	 * @param Connection  con
	 * @param HashMap parametros
	 */
	public static HashMap consultarSalidasPaciente(Connection con,
			HashMap parametros) {
		String cadena = strConsultarSalidasPaciente;
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		if (parametros.containsKey(HojaAnestesia.indicesSalidasSalaPaciente[0])
				&& !parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[0])
						.toString().equals(""))
			cadena += " AND sp.consecutivo  = "
					+ parametros
							.get(HojaAnestesia.indicesSalidasSalaPaciente[0])
					+ " ";

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesSalidasSalaPaciente[1])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	// -----------------------------------------------------------------------------------------------
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/***************************************************************************
	 * METODOS GENERALES***************************************************************************
	 **************************************************************************/
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// -----------------------------------------------------------------------------------------------
	// *********************************************************************************************
	// Hoja de Anestesia
	// *********************************************************************************************
	/**
	 * M�todo para saber si la Hoja de anestesia o Hoja Quir�rgica est� en
	 * estado finalizada y si esta Creada
	 * 
	 * @param con ->
	 *            conexion
	 * @param nroSolicitud
	 * @param consultarHojaQx
	 *            true = hojaQuirurgica, false = hojaAnestesia
	 * @return InfoDatos. acronimo = finalizada (S,N), descripcion = creada
	 *         (S,N)
	 */
	public static InfoDatos esFinalizadaCreadaHoja(Connection con,
			int nroSolicitud, boolean consultarHojaQx) {
		String consultaStr = "SELECT finalizada as finalizada FROM ";
		InfoDatos datos = new InfoDatos();

		if (consultarHojaQx)
			consultaStr += "  hoja_quirurgica  ";
		else
			consultaStr += " hoja_anestesia ";

		consultaStr += "WHERE numero_solicitud = ?";

		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try {
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			
			ps.setInt(1, nroSolicitud);

			rs =new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				// Indica si esta finalizada o No la Hoja
				if (rs.getBoolean("finalizada"))
					datos.setAcronimo(ConstantesBD.acronimoSi);
				else
					datos.setAcronimo(ConstantesBD.acronimoNo);

				// Indica que se encuentra creada la Hoja
				datos.setDescripcion(ConstantesBD.acronimoSi);
			} else {
				datos.setAcronimo(ConstantesBD.acronimoNo);
				datos.setDescripcion(ConstantesBD.acronimoNo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		return datos;
	}

	// **********************************************************************************************

	/**
	 * Consultar la informaci�n de la Hoja de Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static HashMap consultarHojaAnestesia(Connection con,
			HashMap parametros) {
		
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaHojaAnestesia, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[0])+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true);
			return respuesta;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Actualiza la informacion de la Hoja de Anestesia en la salida del
	 * paciente
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean actualizarHojaAnestesiaSalidaPaciemte(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarHojaAnestesia, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * UPDATE "
			+ "hoja_anestesia " + "SET " + "datos_medico = ?,"
			+ "finalizada =  ?," + "fecha_finalizada = ?,"
			+ "hora_finalizada = ? " + "WHERE numero_solicitud = ? 
			 */
			
			// datos medico
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[7])
					.toString().equals(""))
				ps.setString(1, parametros.get(HojaAnestesia.indicesHojaAnestesia[7])+"");
			else
				ps.setNull(1, Types.VARCHAR);

			// finalizada
			ps.setBoolean(2, UtilidadTexto.getBoolean(parametros.get(HojaAnestesia.indicesHojaAnestesia[8])+""));

			// fecha finalizada
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[9])
					.toString().equals(""))
				ps.setDate(3, Date.valueOf(parametros.get(HojaAnestesia.indicesHojaAnestesia[9])+""));
			else
				ps.setNull(3, Types.DATE);

			// hora finalizada
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[10])
					.toString().equals(""))
				ps.setString(4, parametros.get(HojaAnestesia.indicesHojaAnestesia[10])+"");
			else
				ps.setNull(4, Types.CHAR);
			
			// Numero Solicitud
			ps.setInt(5, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[0])+""));

			// instituci�n
			ps.setInt(6, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[14])+""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Insertar Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static boolean insertarHojaAnestesia(Connection con,
			HashMap parametros) {
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strInsertarHojaAnestesia, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			/**
			 * INSERT INTO "
			+ "hoja_anestesia" + "(" + "numero_solicitud," + "institucion,"
			+ "fecha_inicia_anestesia," + "hora_inicia_anestesia,"
			+ "fecha_finaliza_anestesia," + "hora_finaliza_anestesia,"
			+ "preanestesia," + "datos_medico," + "finalizada,"
			+ "fecha_finalizada," + "hora_finalizada," + "fecha_grabacion,"
			+ "hora_grabacion," + "observaciones_signos_vitales,"
			+ "anestesiologo_cobrable" + ") "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			// numero Solicitud
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[0])+""));
			// institucion
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[14])+""));

			// fecha inicia anestesia
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[2]).equals(""))
				ps.setDate(3, Date.valueOf(parametros.get(HojaAnestesia.indicesHojaAnestesia[2])+""));
			else
				ps.setNull(3, Types.DATE);

			// Hora Inicia Anestesia
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[3]).equals(""))
				ps.setString(4, parametros.get(HojaAnestesia.indicesHojaAnestesia[3])+"");
			else
				ps.setNull(4, Types.CHAR);

			// Fecha Finaliza Anestesia
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[4]).equals(""))
				ps.setDate(5, Date.valueOf(parametros.get(HojaAnestesia.indicesHojaAnestesia[4])+""));
			else
				ps.setNull(5, Types.DATE);

			// Hora Finaliza Anestesia
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[5]).equals(""))
				ps.setString(6, parametros.get(HojaAnestesia.indicesHojaAnestesia[5])+"");
			else
				ps.setNull(6, Types.CHAR);

			// Preanestesia
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[6]).equals(""))
				ps.setInt(7, Utilidades.convertirAEntero(parametros.get(HojaAnestesia.indicesHojaAnestesia[6])+""));
			else
				ps.setNull(7, Types.INTEGER);

			// Datos Medico
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[7]).equals(""))
				ps.setString(8, parametros.get(HojaAnestesia.indicesHojaAnestesia[7])+"");
			else
				ps.setNull(8, Types.VARCHAR);

			// Finaliza
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[8]).equals(""))
				ps.setBoolean(9, UtilidadTexto.getBoolean(parametros.get(HojaAnestesia.indicesHojaAnestesia[8])+""));
			else
				ps.setObject(9, null);

			// Fecha Finalizada
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[9]).equals(""))
				ps.setDate(10, Date.valueOf(parametros.get(HojaAnestesia.indicesHojaAnestesia[9])+""));
			else
				ps.setNull(10, Types.DATE);

			// Hora Finalizada
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[10]).equals(""))
				ps.setString(11, parametros.get(HojaAnestesia.indicesHojaAnestesia[10])+"");
			else
				ps.setNull(11, Types.CHAR);

			// Fecha Grabacion
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[11]).equals(""))
				ps.setDate(12, Date.valueOf(parametros.get(HojaAnestesia.indicesHojaAnestesia[11])+""));
			else
				ps.setNull(12, Types.DATE);

			// Hora Grabacion
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[12]).equals(""))
				ps.setString(13, parametros.get(HojaAnestesia.indicesHojaAnestesia[12])+"");
			else
				ps.setNull(13, Types.CHAR);

			// Observaciones Signos Vitales
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[13]).equals(""))
				ps.setString(14, parametros.get(HojaAnestesia.indicesHojaAnestesia[13])+"");
			else
				ps.setNull(14, Types.VARCHAR);

			// Anestesiologo Cobrable
			if (!parametros.get(HojaAnestesia.indicesHojaAnestesia[1]).equals(""))
				ps.setString(15, parametros.get(HojaAnestesia.indicesHojaAnestesia[1])+"");
			else
				ps.setNull(15, Types.CHAR);

			//Validacion Capitacion
			ps.setString(16, parametros.get(HojaAnestesia.indicesHojaAnestesia[17])+"");

			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// **********************************************************************************************

	/**
	 * Consultar los Articulos de la Hoja de Anestesia
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static ArrayList consultarArticulosHojaAnestesia(Connection con,
			HashMap parametros) {
		ArrayList respuesta = new ArrayList();
		String cadena = strConsultarArticulosHojaAnes;

		if (parametros.containsKey("centroCosto")
				&& !parametros.get("centroCosto").toString().equals(""))
			cadena += " AND ahai.centro_costo = "
					+ parametros.get("centroCosto") + " ";

		if (parametros.containsKey("activo")
				&& !parametros.get("activo").toString().equals(""))
		{
			if(UtilidadTexto.getBoolean(parametros.get("activo").toString()))
			{
				cadena += " AND ahai.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + " ";
			}
			else
			{
				cadena += " AND ahai.activo = " + ValoresPorDefecto.getValorFalseParaConsultas() + " ";
			}
		}
			

		if (parametros.containsKey("seccion")
				&& !parametros.get("seccion").toString().equals(""))
			cadena += " AND ahai.seccion = " + parametros.get("seccion");

		if (parametros.containsKey("articulo")
				&& !parametros.get("articulo").toString().equals(""))
			cadena += " AND ahai.articulo = " + parametros.get("articulo");

		if (parametros.containsKey("descripcionArticulo")
				&& !parametros.get("descripcionArticulo").toString().equals(""))
			cadena += " AND UPPER(getdescarticulo(ahai.articulo)) LIKE UPPER('%"
					+ parametros.get("descripcionArticulo") + "%') ";

		if (parametros.containsKey("codigoInsertados")
				&& !parametros.get("codigoInsertados").toString().equals(""))
			cadena += " AND ahai.articulo NOT IN ("
					+ parametros.get("codigoInsertados") + "-1) ";

		cadena += " ORDER BY descripcion_articulo ASC ";

		try {
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));

			rs =new ResultSetDecorator(ps.executeQuery());

			// Rellamado a la funcionalidad para datos parametrizados con centro
			// de costo nulos
			if (((rs.first() + "").equals("false") || (rs.first() + "").equals(ConstantesBD.valorFalseEnString)) 
					&& (parametros.containsKey("centroCosto") && !parametros.get("centroCosto").toString().equals("")) 
					&& !parametros.containsKey("rellamado")) 
			{
				parametros.put("centroCosto", "");
				parametros.put("rellamado", ConstantesBD.acronimoSi);
				return consultarArticulosHojaAnestesia(con, parametros);
			}

			HashMap mapa;
			rs.beforeFirst();
			while (rs.next()) {
				mapa = new HashMap();
				mapa.put("codigo", rs.getObject(1));
				mapa.put("activo", rs.getObject(2));
				mapa.put("articulo", rs.getObject(3));
				mapa.put("descripcionArticulo", rs.getObject(4));
				mapa.put("centroCosto", rs.getObject(5));
				mapa.put("seccion", rs.getObject(6));

				mapa.put("tipoLiquido", rs.getObject(7));
				mapa.put("descripcionLiquido", rs.getObject(8));
				mapa.put("espos", rs.getObject(9));

				respuesta.add(mapa);
			}
			logger.info("valor del sql >> " + cadena);
			return respuesta;
		} catch (SQLException e) {
			logger.info("valor del sql error >> " + cadena);
			e.printStackTrace();
		}

		return respuesta;
	}

	// **********************************************************************************************
	
	/**
	 * Actualiza las cantidades del detalle de materiales qx
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarCantidadesDetMatQx(Connection con, HashMap parametros)
	{
		logger.info("\n\n\n//*********************************************");		
		logger.info("Actualizar Cantidades Det Mat Qx >> "+parametros);
		
		String cadena = "", cadenaActualizar = "";
		
		if(Utilidades.convertirAEntero(parametros.get("seccion").toString()) == ConstantesSeccionesParametrizables.seccionLiquidos 
			|| Utilidades.convertirAEntero(parametros.get("seccion").toString()) == ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis)
		{
				cadena = strConsultarCodigosDetMateQx;
				cadenaActualizar = strActualizarCantidadDetMaterialesQx;
				
				try 
				{
					ResultSetDecorator rs;
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));
					ps.setInt(2,Utilidades.convertirAEntero(parametros.get("seccion").toString()));
					
					rs =new ResultSetDecorator(ps.executeQuery());
					
					while(rs.next())
					{
						logger.info("actualiza el detalle material qx >> "+rs.getInt(1));
						ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1,rs.getInt(1));				
						ps.setInt(2,rs.getInt(1));
					
						if(ps.executeUpdate()<=0)
							return false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
		}
		else if(Utilidades.convertirAEntero(parametros.get("seccion").toString()) == ConstantesSeccionesParametrizables.seccionInfusiones)
		{
				cadena = strConsultarCodigosDetMateQxInfusiones;
				cadenaActualizar = strActualizarCantidadDetMaterialesQxInfusiones;
				
				try 
				{
					ResultSetDecorator rs;
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));					
					
					rs =new ResultSetDecorator(ps.executeQuery());
					
					while(rs.next())
					{
						logger.info("actualiza el detalle material qx >> "+rs.getInt(1));
						ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1,rs.getInt(1));				
						ps.setInt(2,rs.getInt(1));
					
						if(ps.executeUpdate()<=0)
							return false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
		}
		else if(Utilidades.convertirAEntero(parametros.get("seccion").toString()) == ConstantesSeccionesParametrizables.subSeccionViaArea)
		{			
			cadena = strConsultarCodigosDetMateQxVia;
			cadenaActualizar = strActualizarCantidadDetMaterialesQxViaA;
			
			try 
			{
				ResultSetDecorator rs;
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));				
				
				rs =new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					logger.info("actualiza el detalle material qx >> "+rs.getInt(1));
					ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,rs.getInt(1));				
					ps.setInt(2,rs.getInt(1));
				
					if(ps.executeUpdate()<=0)
						return false;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}else if(Utilidades.convertirAEntero(parametros.get("seccion").toString()) == ConstantesSeccionesParametrizables.subSeccionAccesosVasculares)
		{
			cadena = strConsultarCodigosDetMateQxVasculares;
			cadenaActualizar = strActualizarCantidadDetMaterialesQxVasculares;

			try 
			{
				ResultSetDecorator rs;
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));

				rs =new ResultSetDecorator(ps.executeQuery());

				while(rs.next())
				{
					logger.info("actualiza el detalle material qx >> "+rs.getInt(1));
					ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,rs.getInt(1));
					ps.setInt(2,rs.getInt(1));

					if(ps.executeUpdate()<=0)
						return false;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;		
	}	
	
	// **********************************************************************************************

	/**
	 * Consultar las Mezclas parametrizadas en el sistema
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static ArrayList consultarMezclas(Connection con, HashMap parametros) {
		ArrayList respuesta = new ArrayList();
		String cadena = strConsultaMezclas;

		if (parametros.containsKey("consecutivo")
				&& !parametros.get("consecutivo").toString().equals(""))
			cadena += " AND cc.consecutivo = " + parametros.get("consecutivo");

		if (parametros.containsKey("centroCosto")
				&& !parametros.get("centroCosto").toString().equals(""))
			cadena += " AND cc.centro_costo = " + parametros.get("centroCosto");

		if (parametros.containsKey("nombre")
				&& !parametros.get("nombre").toString().equals(""))
			cadena += " AND UPPER(m.nombre) LIKE UPPER('%"
					+ parametros.get("nombre") + "%') ";

		if (parametros.containsKey("codigoInsertados")
				&& !parametros.get("codigoInsertados").toString().equals(""))
			cadena += " AND cc.consecutivo NOT IN ("
					+ parametros.get("codigoInsertados") + "-1) ";

		cadena += " ORDER BY m.nombre ASC ";

		try {
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));

			rs =new ResultSetDecorator(ps.executeQuery());

			// Rellamado a la funcionalidad para datos parametrizados con centro
			// de costo nulos
			if (((rs.first() + "").equals("false") || (rs.first() + "")
					.equals(ConstantesBD.valorFalseEnString))
					&& (parametros.containsKey("centroCosto") && !parametros
							.get("centroCosto").toString().equals(""))
					&& !parametros.containsKey("rellamado")) {
				parametros.put("centroCosto", "");
				parametros.put("rellamado", ConstantesBD.acronimoSi);
				return consultarMezclas(con, parametros);
			}

			HashMap mapa;
			rs.beforeFirst();
			while (rs.next()) {
				mapa = new HashMap();
				mapa.put("consecutivo", rs.getObject(1));
				mapa.put("codigo_mezcla", rs.getObject(2));
				mapa.put("nombre_mezcla", rs.getObject(3));

				respuesta.add(mapa);
			}

			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return respuesta;
	}

	// **********************************************************************************************

	/**
	 * Consultar la informacion de los articulos asociados a una mezcla
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static HashMap consultarArticulosMezcla(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(
					strConsultarArticulosMezclas, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get("mezclaCC")+""));

			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,
					true);
			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// **********************************************************************************************

	/**
	 * Consultar otros Liquidos Balance Liquidos Hoja de Anestesia 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList consultarOtrosLiquidosBalanceLiq(Connection con,
			HashMap parametros) {
		ArrayList respuesta = new ArrayList();
		String cadena = strConsultarOtrosLiquidosBalance;

		if (parametros.containsKey("descripcionLiquido")
				&& !parametros.get("descripcionLiquido").toString().equals(""))
			cadena += " AND UPPER(getDescripcionLiquido(liquido)) LIKE UPPER('%"
					+ parametros.get("descripcionLiquido") + "%') ";

		if (parametros.containsKey("centroCosto") && !parametros.get("centroCosto").toString().equals(""))
			cadena += " AND centro_costo = "+ parametros.get("centroCosto") + " ";

		if (parametros.containsKey("codigosInsertados")
				&& !parametros.get("codigosInsertados").toString().equals(""))
			cadena += " AND codigo NOT IN ("
					+ parametros.get("codigosInsertados").toString() + "-1) ";

		cadena += " ORDER BY descripcion_liquido ASC ";

		try {
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));

			rs =new ResultSetDecorator(ps.executeQuery());

			// Rellamado a la funcionalidad para datos parametrizados con centro
			// de costo nulos
			if (((rs.first() + "").equals("false") || (rs.first() + "").equals(ConstantesBD.valorFalseEnString))
					&& (parametros.containsKey("centroCosto") && !parametros.get("centroCosto").toString().equals(""))
						&& !parametros.containsKey("rellamado")) {
				parametros.put("centroCosto", "");
				parametros.put("rellamado", ConstantesBD.acronimoSi);
				return consultarOtrosLiquidosBalanceLiq(con, parametros);
			}

			HashMap mapa;
			rs.beforeFirst();
			while (rs.next()) {
				mapa = new HashMap();
				mapa.put("codigo", rs.getObject(1));
				mapa.put("liquido", rs.getObject(2));
				mapa.put("descripcionLiquido", rs.getObject(3));

				respuesta.add(mapa);
			}

			return respuesta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return respuesta;
	}

	// **********************************************************************************************

	/**
	 * Consultar Salidas de paciente 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList consultarSalidasPacienteInstCCosto(Connection con,
			HashMap parametros) {
		ArrayList respuesta = new ArrayList();
		String cadena = strConsultarSalidasPacInstCC;
		

		if (parametros.containsKey("centroCosto")
				&& !parametros.get("centroCosto").toString().equals(""))
			cadena += " AND centro_costo = " + parametros.get("centroCosto");

		if (parametros.containsKey("fallece")
				&& !parametros.get("fallece").toString().equals(""))
			cadena += " AND fallece = '" + parametros.get("fallece") + "' ";

		try {
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			

			rs =new ResultSetDecorator(ps.executeQuery());

			// Rellamado a la funcionalidad para datos parametrizados con centro
			// de costo nulos
			if (((rs.first() + "").equals("false") || (rs.first() + "")
					.equals(ConstantesBD.valorFalseEnString))
					&& (parametros.containsKey("centroCosto") && !parametros
							.get("centroCosto").toString().equals(""))
					&& !parametros.containsKey("rellamado")) {
				parametros.put("centroCosto", "");
				parametros.put("rellamado", ConstantesBD.acronimoSi);
				return consultarSalidasPacienteInstCCosto(con, parametros);
			}

			HashMap mapa;
			rs.beforeFirst();
			while (rs.next()) {
				mapa = new HashMap();
				mapa.put("consecutivo", rs.getObject(1));
				mapa.put("salidaPac", rs.getObject(2));
				mapa.put("descripcion", rs.getObject(3));
				mapa.put("fallece", rs.getObject(4));

				respuesta.add(mapa);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return respuesta;
	}

	// **********************************************************************************************

	/**
	 * Verifica si los servicios de la solicitud poseen indicativo requiere
	 * interpretacion en Si
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 */
	public static String esRequerioInterServicioSoli(Connection con,
			HashMap parametros) {

		String cadena = "SELECT COUNT(sol.codigo) "
				+ "FROM sol_cirugia_por_servicio sol "
				+ "INNER JOIN servicios s ON (s.codigo = sol.servicio AND s.requiere_interpretacion = '"
				+ ConstantesBD.acronimoSi + "' ) "
				+ "WHERE sol.numero_solicitud = "
				+ parametros.get("numeroSolicitud").toString();

		try {
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			rs =new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				if (rs.getInt(1) > 0)
					return ConstantesBD.acronimoSi;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ConstantesBD.acronimoNo;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public static HashMap<Object, Object> cargarGraficaHojaAnestesia(Connection con, int numeroSolicitud, String graficar, boolean liquidos)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		String cadena= "SELECT  "+
							"aha.codigo AS codigo, "+
							"aha.articulo AS articulo,"+
						 	"aha.otro_medicamento AS otro_medicamento,"+
						 	"getdescarticulo(aha.articulo) AS descripcion_articulo, "+
						 	"to_char(da.fecha, 'DD/MM/YYYY') as fecha, " +
						 	"da.hora as hora, " +
						 	"da.graficar as graficar "+ 
						 "FROM " +
						 	"admin_hoja_anestesia aha " +
						 	"inner join det_admin_hoja_anes da ON (da.admin_hoja_anes=aha.codigo)"+
						 	"INNER JOIN articulo a ON (aha.articulo=a.codigo) "+
						 "WHERE aha.numero_solicitud = ? ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and da.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and da.graficar='"+ConstantesBD.acronimoNo+"' ";
		
		if(!liquidos)
			cadena+=" and tipo_liquido is null ";
		else
			cadena+=" and tipo_liquido is not null ";
		
		cadena+=	" order by fecha, hora ";
		
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param graficar
	 * @return
	 */
	public static HashMap<Object, Object> cargarGraficaInfusionesHA(Connection con, int numeroSolicitud, String graficar)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		//parte inicial de mezclas	
		String cadena = "(SELECT " +
							"iha.codigo as codigo_infusion_hoja_anes, "+
							"iha.mezcla_inst_cc AS codigo_mezcla_ins_cc, "+
							"getdescripcionmezcla(iha.mezcla_inst_cc) AS descripcion_mezcla, "+
							"iha.otra_infusion AS otra_infusion, "+
							"'"+ConstantesBD.acronimoNo+"' AS es_otra_infusion, "+
							"det.articulo AS articulo,"+
							"getdescarticulo(det.articulo) AS descripcion_articulo, " +
							"to_char(adm.fecha, 'DD/MM/YYYY') as fecha, " +
							"adm.hora as hora "+
						"FROM " +
							"infusiones_hoja_anes iha "+
							"INNER JOIN mezclas_inst_cc mcc ON (mcc.consecutivo = iha.mezcla_inst_cc) "+
							"INNER JOIN adm_infusiones_hoja_anes adm ON (adm.cod_info_hoja_anes = iha.codigo) "+
							"INNER JOIN det_infusion_hoja_anes det ON (det.cod_adm_info_hoja_anes = adm.codigo ) "+
						"WHERE " +
							"iha.numero_solicitud = ? ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and iha.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and iha.graficar='"+ConstantesBD.acronimoNo+"' ";
		cadena+=") ";
		
		cadena+=" UNION " +
				"(	SELECT " +
						"iha.codigo as codigo_infusion_hoja_anes, "+
						ConstantesBD.codigoNuncaValido+" AS codigo_mezcla_ins_cc, "+
						"'' AS descripcion_mezcla, "+
						"iha.otra_infusion AS otra_infusion, "+
						"'"+ConstantesBD.acronimoSi+"' AS es_otra_infusion, "+
						"det.articulo AS articulo,"+
						"getdescarticulo(det.articulo) AS descripcion_articulo, " +
						"to_char(adm.fecha, 'DD/MM/YYYY') as fecha, " +
						"adm.hora as hora "+
					"FROM " +
						"infusiones_hoja_anes iha "+
						"INNER JOIN adm_infusiones_hoja_anes adm ON (adm.cod_info_hoja_anes = iha.codigo) "+
						"INNER JOIN det_infusion_hoja_anes det ON (det.cod_adm_info_hoja_anes = adm.codigo ) "+
					"WHERE " +
						"iha.numero_solicitud = ? and iha.mezcla_inst_cc is null ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and iha.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and iha.graficar='"+ConstantesBD.acronimoNo+"' ";
		cadena+=") ";
		
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * Consultar el Indicativo de Registro
	 * 
	 * @param Connection con
	 * @param int numeroSolicitud
	 */
	public static String consultarIndicativoRegistroDesde(Connection con, int numeroSolicitud){
		
		String resultado = "";
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
			
			ps = new PreparedStatementDecorator(con.prepareStatement(strConsultarIndicativoRegistroDesde,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			rs = new ResultSetDecorator(ps.executeQuery());
			logger.info("Consulta Indicativo Registro Desde -> " + strConsultarIndicativoRegistroDesde + "--" + numeroSolicitud);
			
			if(rs.next())
				return rs.getString(1);
		

		} catch (SQLException e) {
			logger.error("consultarIndicativoRegistroDesde: " + e);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return null;
	}
	
	/**
	 * Actualizar Indicativo registro desde
	 * 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarIndicativoRegistroDesde(Connection con, HashMap parametros){
		
		PreparedStatementDecorator ps = null;
		
		try{
			ps =  new PreparedStatementDecorator(con.prepareStatement(
					strActualizarIndicativoRegistroDesde,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			
			ps.setString(1,parametros.get(HojaAnestesia.indicesHojaAnestesia[17])+"");
			ps.setInt(2, Utilidades.convertirAEntero( parametros.get(HojaAnestesia.indicesHojaAnestesia[0])+""));
			
			logger.info("strActualizarIndicativoRegistroDesde-> "+strActualizarIndicativoRegistroDesde+"--"+parametros.get(HojaAnestesia.indicesHojaAnestesia[17])+"--"+ parametros.get(HojaAnestesia.indicesHojaAnestesia[0]));
			
			if (ps.executeUpdate() > 0)
				return true;
			else
				return false;

			
		} catch (SQLException e) {
			logger.error("actualizarIndicativoRegistroDesde: " + e);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
		
		return false;
	}
	
	/**
	 * Metodo que consulta los datos de una hoja de anestesia relacionada con un numero de solicitud dado
	 * y los almacena an un objeto de tipo HojaAnestesiaDto
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @date 19/07/2013
	 */
	public static HojaAnestesiaDto consultarHojaAnestesia(Connection con,int numeroSolicitud) throws BDException {
		
		HojaAnestesiaDto hojaAnestesiaDto = new HojaAnestesiaDto();
		StringBuffer consulta=new StringBuffer(" SELECT fecha_inicia_anestesia, hora_inicia_anestesia, fecha_finaliza_anestesia, hora_finaliza_anestesia, " )
										.append(" datos_medico, finalizada, fecha_finalizada, hora_finalizada, observaciones_signos_vitales, peso, talla, ")
								   		.append(" anestesiologo_cobrable, descripcion ")
								   		.append(" FROM salascirugia.hoja_anestesia ")
								   		.append(" WHERE numero_solicitud=? ");

			PreparedStatement ps = null;
			ResultSet rs = null;
			try 
			{
				String cadena = consulta.toString();
				ps =  con.prepareStatement(cadena);
				ps.setInt(1,numeroSolicitud);
				rs=ps.executeQuery();
				
				if(rs.next()){
					hojaAnestesiaDto.setFechaIniciaAnestesia(rs.getDate("fecha_inicia_anestesia"));
					hojaAnestesiaDto.setHoraIniciaAnestesia(rs.getString("hora_inicia_anestesia"));
					hojaAnestesiaDto.setFechaFinAnestesia(rs.getDate("fecha_finaliza_anestesia"));
					hojaAnestesiaDto.setHoraFinAnestesia(rs.getString("hora_finaliza_anestesia"));
					hojaAnestesiaDto.setDatosMedico(rs.getString("datos_medico"));
					hojaAnestesiaDto.setFinalizada(rs.getBoolean("finalizada"));
					hojaAnestesiaDto.setFechaFinalizada(rs.getDate("fecha_finalizada"));
					hojaAnestesiaDto.setHoraFinalizada(rs.getString("hora_finalizada"));
					hojaAnestesiaDto.setObservacionesSignosVitales(rs.getString("observaciones_signos_vitales"));
					hojaAnestesiaDto.setPeso(rs.getInt("peso"));
					hojaAnestesiaDto.setTalla(rs.getInt("talla"));
					if(rs.getString("anestesiologo_cobrable").equals("S")){
						hojaAnestesiaDto.setAnestesiologoCobrable(true);
					}else if(rs.getString("anestesiologo_cobrable").equals("N")){
						hojaAnestesiaDto.setAnestesiologoCobrable(false);
					}
					hojaAnestesiaDto.setDescripcion(rs.getString("descripcion"));
				}
			}
			catch (SQLException  e) {
				logger.info("\n problema consultando la informacion de la hoja de anestesia "+e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			}
			return hojaAnestesiaDto;
	}
}
