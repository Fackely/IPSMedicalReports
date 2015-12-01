package com.princetonsa.dao.sqlbase.historiaClinica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscalaFactorPrediccion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoValorOpcionCampoParam;
import com.princetonsa.enu.general.CarpetasArchivos;

public class SqlBasePlantillasDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePlantillasDao.class);

	private static final int tipoElementoSeccion = 1;
	private static final int tipoElementoComponente = 2;
	private static final int tipoElementoEscala = 3;
	
	private static int institucion=ConstantesBD.codigoNuncaValido;

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Plantillas ******************************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Inserta informacion de las plantillas
	 * */
	private static String strInsertarPlantillas = "INSERT INTO plantillas("
			+ "codigo_pk," + "fun_param," + "centro_costo," + "sexo,"
			+ "especialidad," + "codigo_plantilla," + "nombre_plantilla,"
			+ "institucion," + "usuario_modifica," + "fecha_modifica,"
			+ "hora_modifica," + "tipo_atencion," + "tipo_funcionalidad) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	/**
	 * Actualizar la informacion de la tabla plantillas
	 * */
	private static String strActualizarPlantillas = "UPDATE plantillas SET "
			+ "centro_costo = ?, " + "sexo = ?, " + "especialidad = ?, "
			+ "codigo_plantilla = ? ," + "nombre_plantilla = ?,"
			+ "usuario_modifica = ?," + "fecha_modifica = ?,"
			+ "hora_modifica = ?," + "mostrar_modificacion = ?,"
			+ "tipo_atencion = ?," + "tipo_funcionalidad = ? "
			+ "WHERE codigo_pk = ? ";

	/**
	 * Consulta la informaciï¿½n de la tabla plantillas
	 * */
	private static String strConsultarPlantillas = "SELECT DISTINCT "
			+ "codigo_pk,"
			+ "fun_param,"
			+ "centro_costo,"
			+ "sexo,"
			+ "especialidad,"
			+ "codigo_plantilla,"
			+ "nombre_plantilla,"
			+ "institucion,"
			+ "'"
			+ ConstantesBD.acronimoSi
			+ "' AS estabd,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS servicios,"
			+ "tipo_atencion,"
			+ "especialidad,"
			+ "coalesce(getnombreespecialidad(especialidad),'TODOS') AS nombreespecialidad,"
			+ "coalesce(getintegridaddominio(tipo_atencion),'Sin tipo de Atenciï¿½n') AS nombretipoatencion "
			+ "FROM plantillas " + "WHERE " + "institucion = ? ";

	/**
	 * Cadena usada para cargar el encabezado de la plantilla
	 */
	private static final String strCargarEncabezadoPlantilla = "SELECT "
			+ "codigo_pk, "
			+ "fun_param as codigo_funcionalidad, "
			+ "coalesce(centro_costo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS codigo_centro_costo, "
			+ "coalesce(getnomcentrocosto(centro_costo),'') AS nombre_centro_costo, "
			+ "coalesce(sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS codigo_sexo, "
			+ "coalesce(getdescripcionsexo(sexo),'') AS nombre_sexo, "
			+ "coalesce(especialidad,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS codigo_especialidad, "
			+ "coalesce(getnombreespecialidad(especialidad),'') AS nombre_especialidad, "
			+ "institucion as codigo_institucion, "
			+ "coalesce(codigo_plantilla," + ConstantesBD.codigoNuncaValido
			+ ") AS codigo," + "coalesce(nombre_plantilla,'') AS nombre,"
			+ "coalesce(tipo_funcionalidad,'') AS tipoFuncionalidad,"
			+ "tipo_atencion " + "FROM plantillas WHERE institucion = ? ";

	/**
	 * Insertar resultados procedimiento requerido
	 * */
	private static final String strInsertarResultadosProcRequerido = "INSERT INTO historiaclinica.resultados_proc_requeridos "
			+ "VALUES(?,?,?,?,?) ";

	/**
	 * Actualizar resultados procedimiento requerido
	 * */
	private static final String strActualizarResultadosProRequerido = "UPDATE historiaclinica.resultados_proc_requeridos SET "
			+ "requerido = ?, usuario_modifica = ?, fecha_modifica = ? , hora_modifica = ?  WHERE plantilla = ? ";

	/**
	 * Consulta resultados procedimiento requerido
	 * */
	private static final String strConsultarResultadosProRequerido = "SELECT requerido FROM historiaclinica.resultados_proc_requeridos WHERE plantilla = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Secciones Fijas *************************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena para cargar las secciones fijas de una plantilla
	 */
	private static final String strCargarSeccionesFijasPlantilla = "SELECT * FROM "
			+ "( "
			+
			// CONSULTA DE LAS SECCIONES FIJAS
			"( "
			+ "SELECT "
			+ "coalesce(ps.codigo_pk||'','') AS codigo_pk, "
			+ "fp.codigo_pk AS codigo_pk_fun_param_sec_fij, "
			+ "sf.codigo AS codigo_seccion, "
			+ "sf.nombre AS nombre_seccion, "
			+ "coalesce(ps.orden,fp.orden) AS orden, "
			+ "coalesce(ps.mostrar,'"
			+ ConstantesBD.acronimoSi
			+ "') AS mostrar, "
			+ "fp.activo AS mostrar_modificacion, "
			+ "'"
			+ ConstantesBD.acronimoSi
			+ "' AS es_fija "
			+ "FROM fun_param_secciones_fijas fp "
			+ "INNER JOIN secciones_fijas sf ON(sf.codigo=fp.seccion_fija) "
			+ "LEFT OUTER JOIN plantillas_sec_fijas ps ON(ps.fun_param_secciones_fijas = fp.codigo_pk AND ps.plantilla = ? ) "
			+ "WHERE fp.fun_param = ? AND fp.institucion = ?  AND  fp.activo = '"
			+ ConstantesBD.acronimoSi
			+ "' "
			+ ") "
			+ "UNION "
			+
			// CONSULTA DE SECCIONES PARAMETRIZABLES (INVISIBLES) CUYOS
			// ELEMENTOS ESTÁN AL MISMO NIVEL DE LAS FIJAS
			"( "
			+ "SELECT "
			+ "ps.codigo_pk||'' AS codigo_pk, "
			+ "ps.seccion_param AS codigo_pk_fun_param_sec_fij, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS codigo_seccion, "
			+ "coalesce(sp.descripcion,'') AS nombre_seccion, "
			+ "coalesce(ps.orden,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS orden, "
			+ "coalesce(ps.mostrar,'"
			+ ConstantesBD.acronimoSi
			+ "') AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS es_fija "
			+ "FROM plantillas_sec_fijas ps "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk = ps.seccion_param) "
			+ "WHERE ps.plantilla = ? " + ") " + ") t ORDER BY orden ";

	/**
	 * Cadena que consulta las secciones fijas de una funcionalidad
	 */
	private static final String strCargarSeccionesFijas = "SELECT "
			+ "'' AS codigo_pk, "
			+ "fp.codigo_pk AS codigo_pk_fun_param_sec_fij, "
			+ "sf.codigo AS codigo_seccion, " + "sf.nombre AS nombre_seccion, "
			
			//+ "fp.orden AS orden, " + "'" + ConstantesBD.acronimoSi + "' AS mostrar, " + // Antes de modificación Anexo 860, Cambio 1.12 - Cristhian Murillo
			+ "fp.orden AS orden," + "psf.mostrar, " 	// Se agrego por modificaciones Anexo 860, Cambio 1.12 - Cristhian Murillo
			
			
			
			+ "fp.activo AS mostrar_modificacion "
			+ "FROM fun_param_secciones_fijas fp "
			+ "INNER JOIN secciones_fijas sf ON(sf.codigo=fp.seccion_fija) "
			
			//+ "INNER JOIN plantillas_sec_fijas psf ON(fp.seccion_fija=psf.fun_param_secciones_fijas) " // Se agrego por modificaciones Anexo 860, Cambio 1.12 - Cristhian Murillo
			+ "INNER JOIN plantillas_sec_fijas psf ON(fp.codigo_pk=psf.fun_param_secciones_fijas) " // Se agrego por modificaciones Anexo 860, Cambio 1.12 - Cristhian Murillo //Se modificó la relacion de:(fp.seccion_fija =psf.fun_param_secciones_fijas) a:(fp.codigo_pk=psf.fun_param_secciones_fijas) por tarea Xplaner 34578.
			
			
			+ "WHERE fp.fun_param = ? AND fp.institucion = ? AND fp.activo = '"
			+ ConstantesBD.acronimoSi + "' AND 1=1 " 
			+ " AND psf.mostrar='S' " //Se agrego por tarea Xplaner 34578
			
			+ "GROUP BY fp.codigo_pk, sf.codigo, sf.nombre, fp.orden, psf.mostrar, fp.activo " 
			+ "ORDER BY orden ASC";
	/**
	 * Cadena para cargar los elementos de la seccion fija de una plantilla
	 */
	private static final String strCargarElementosSeccionFijaPlantillaConFiltro = "SELECT * FROM "
			+ "( " + "( " + "SELECT " + "ps.codigo_pk AS consecutivo, "
			+ tipoElementoSeccion
			+ " AS tipo_elemento, "
			+ // cï¿½digo para identificar una secciï¿½n
			"sp.orden AS orden, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ // se consultan el Nï¿½ de columnas de la seccion
			"sp.tipo, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "'  AS observaciones_requeridas, "
			+ "sp.mostrar AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion,"
			+ "sp.seccion_padre, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "FROM plantillas_secciones ps "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=ps.seccion) "
			+ "WHERE ps.plantilla_sec_fija = ? AND sp.seccion_padre is null "
			+ // se consulta la seccion de mï¿½s alto nivel
			// Se filtra la informaciï¿½n del paciente
			" and (sp.sexo is null or sp.sexo = ?) "
			+ " and ((sp.edad_inicial_dias is null and sp.edad_final_dias is null) or (sp.edad_inicial_dias <= ? and sp.edad_final_dias >= ?)) "
			+ ") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "pc.codigo_pk AS consecutivo, "
			+ tipoElementoComponente
			+ " AS tipo_elemento, "
			+ // cï¿½digo para identificar un componente
			"pc.orden AS orden, "
			+ "c.codigo AS codigo_pk, "
			+ "tc.codigo ||'' AS codigo, "
			+ // el codigo es la constante del tipo de componente
			"tc.nombre AS descripcion, "
			+ // la descripcion es el nombre del tipo de componente
			"0 AS columnas_seccion, "
			+ "' ' AS tipo,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS observaciones_requeridas, "
			+ "pc.mostrar AS mostrar, "
			+
			// Se debe tener en cuenta que si el componente fue inactivado ya no
			// se puede mostrar
			"CASE WHEN c.activo = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN '"
			+ ConstantesBD.acronimoNo
			+ "' ELSE coalesce(pc.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS seccion_padre, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM plantillas_componentes pc "
			+ "INNER JOIN componentes c ON(c.codigo=pc.componente) "
			+ "INNER JOIN tipos_componente tc ON(tc.codigo=c.tipo_componente) "
			+ "WHERE pc.plantilla_sec_fija = ? "
			+ ") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "pe.codigo_pk as consecutivo, "
			+ tipoElementoEscala
			+ " as tipo_elemento, "
			+ // cï¿½digo para identificar una escala
			"pe.orden AS orden, "
			+ "e.codigo_pk AS codigo_pk, "
			+ "e.codigo AS codigo, "
			+ "e.nombre AS descripcion, "
			+ "0 AS columnas_seccion, "
			+ "' ' AS tipo,"
			+ "e.observaciones_requeridas AS observaciones_requeridas, "
			+ "pe.mostrar AS mostrar, "
			+
			// Se debe tener en cuenta que si la escala fue inactivada desde la
			// parametrizacion de escalas ya no se puede mostrar
			"CASE WHEN e.mostrar_modificacion = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN '"
			+ ConstantesBD.acronimoNo
			+ "' ELSE coalesce(pe.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS seccion_padre, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM plantillas_escalas pe "
			+ "INNER JOIN escalas e ON(e.codigo_pk=pe.escala) "
			+ "WHERE pe.plantilla_sec_fija = ? "
			+ ") "
			+ ") t ORDER BY t.orden";

	/**
	 * Cadena para cargar los elementos de la seccion fija de una plantilla
	 */
	private static final String strCargarElementosSeccionFijaPlantilla = "SELECT * FROM "
			+ "( " + "( " + "SELECT " + "ps.codigo_pk AS consecutivo, "
			+ tipoElementoSeccion
			+ " AS tipo_elemento, "
			+ // cï¿½digo para identificar una secciï¿½n
			"sp.orden AS orden, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ // se consultan el Nï¿½ de columnas de la seccion
			"sp.tipo, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "'  AS observaciones_requeridas, "
			+ "sp.mostrar AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion,"
			+ "sp.seccion_padre, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "FROM plantillas_secciones ps "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=ps.seccion) "
			+ "WHERE ps.plantilla_sec_fija = ? AND sp.seccion_padre is null "
			+ // se consulta la seccion de mï¿½s alto nivel
			") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "pc.codigo_pk AS consecutivo, "
			+ tipoElementoComponente
			+ " AS tipo_elemento, "
			+ // cï¿½digo para identificar un componente
			"pc.orden AS orden, "
			+ "c.codigo AS codigo_pk, "
			+ "tc.codigo ||'' AS codigo, "
			+ // el codigo es la constante del tipo de componente
			"tc.nombre AS descripcion, "
			+ // la descripcion es el nombre del tipo de componente
			"0 AS columnas_seccion, "
			+ "' ' AS tipo,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS observaciones_requeridas, "
			+ "pc.mostrar AS mostrar, "
			+
			// Se debe tener en cuenta que si el componente fue inactivado ya no
			// se puede mostrar
			"CASE WHEN c.activo = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN '"
			+ ConstantesBD.acronimoNo
			+ "' ELSE coalesce(pc.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS seccion_padre, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM plantillas_componentes pc "
			+ "INNER JOIN componentes c ON(c.codigo=pc.componente) "
			+ "INNER JOIN tipos_componente tc ON(tc.codigo=c.tipo_componente) "
			+ "WHERE pc.plantilla_sec_fija = ? "
			+ ") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "pe.codigo_pk as consecutivo, "
			+ tipoElementoEscala
			+ " as tipo_elemento, "
			+ // cï¿½digo para identificar una escala
			"pe.orden AS orden, "
			+ "e.codigo_pk AS codigo_pk, "
			+ "e.codigo AS codigo, "
			+ "e.nombre AS descripcion, "
			+ "0 AS columnas_seccion, "
			+ "' ' AS tipo,"
			+ "e.observaciones_requeridas AS observaciones_requeridas, "
			+ "pe.mostrar AS mostrar, "
			+
			// Se debe tener en cuenta que si la escala fue inactivada desde la
			// parametrizacion de escalas ya no se puede mostrar
			"CASE WHEN e.mostrar_modificacion = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN '"
			+ ConstantesBD.acronimoNo
			+ "' ELSE coalesce(pe.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS seccion_padre, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM plantillas_escalas pe "
			+ "INNER JOIN escalas e ON(e.codigo_pk=pe.escala) "
			+ "WHERE pe.plantilla_sec_fija = ? "
			+ ") "
			+ ") t ORDER BY t.orden";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Campos Secciones Fijas *******************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena para cargar los campos de una seccion parametrizable de una
	 * plantilla
	 */
	private static final String strCargarCamposSeccionPlantilla = "SELECT "
			+ "pc.codigo_pk AS consecutivo, " + "cp.codigo_pk as codigo_pk, "
			+ "cp.codigo AS codigo, " + "cp.nombre AS nombre, "
			+ "coalesce(cp.etiqueta,'') AS etiqueta, "
			+ "cp.tipo AS codigo_tipo, " + "tc.nombre AS nombre_tipo, "
			+ "cp.tamanio AS tamanio, " + "coalesce(cp.signo,'') AS signo, "
			+ "coalesce(cp.unidad,-1) AS codigo_unidad, "
			+ "coalesce(uc.acronimo,'') AS nombre_unidad, "
			+ "coalesce(cp.valor_predeterminado,'') AS valor_predeterminado, "
			+ "coalesce(cp.maximo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS maximo, "
			+ "coalesce(cp.minimo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS minimo, "
			+ "coalesce(cp.decimales,0) As decimales, "
			+ "coalesce(cp.columnas_ocupadas,1) AS columnas_ocupadas, "
			+ "coalesce(cp.orden,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS orden, "
			+ "cp.unico_fila AS unico_fila, "
			+ "cp.requerido AS requerido, "
			+ "coalesce(cp.formula,'') As formula, "
			+ "cp.institucion as codigo_institucion, "
			+ "cp.tipo_html AS tipo_html,"
			+ "cp.mostrar AS mostrar, "
			+ "cp.mostrar_modificacion as mostrar_modificacion, "
			+ "coalesce(cp.maneja_imagen,' ') as maneja_img, "
			+ "coalesce(cp.imagen_a_asociar,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as img_asociar, "
			+ "coalesce(ib.codigo_pk,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as cod_img_base, "
			+ "coalesce(ib.nombre_imagen,' ') as nom_img_base, "
			+ "coalesce(ib.imagen,' ') as path_img_base "
			+ "FROM historiaclinica.plantillas_campos_sec pc "
			+ "INNER JOIN historiaclinica.campos_parametrizables cp ON(cp.codigo_pk=pc.campo_parametrizable) "
			+ "INNER JOIN historiaclinica.tipos_campo_param tc ON(tc.codigo=cp.tipo) "
			+ "LEFT OUTER JOIN historiaclinica.imagenes_base ib ON (cp.imagen_a_asociar = ib.codigo_pk ) "
			+ "LEFT OUTER JOIN historiaclinica.unidades_campo_param uc ON(uc.codigo=cp.unidad) "
			+ "WHERE pc.plantilla_seccion = ? ORDER BY orden ASC ";

	/**
	 * Cadena que consulta las opciones parametrizadas del campo
	 */
	private static final String strCargarOpcionesCampo = "SELECT "
			+ "ovp.codigo_pk,"
			+ "ovp.campo_parametrizable,"
			+ "ovp.opcion, "
			+ "ovp.valor, "
			+ "coalesce(co.consecutivo,-1) as consecutivo_con_odon, "
			+ "coalesce(co.nombre, ' ') as nom_con_odon, "
			+ "coalesce(co.archivo_convencion, ' ') as path_con_odo "
			+ "FROM historiaclinica.opciones_val_parametrizadas ovp "
			+ "LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (ovp.convencion = co.consecutivo)"
			+ "WHERE ovp.campo_parametrizable = ? AND ovp.institucion = ?";

	/**
	 * Cadena que consulta los valores asociados a la opcion de un campo
	 */
	private static final String strCargarValoresOpcionCampo = "SELECT "
			+ "codigo_pk, " + "codigo_pk_opciones, " + "valor, "
			+ "mostrar_modificacion " + "FROM det_valores " + "WHERE "
			+ "codigo_pk_opciones = ? and mostrar_modificacion = '"
			+ ConstantesBD.acronimoSi + "'";

	/**
	 * Cadena que consulta los codigos de la secciones asociadas a la opcion de
	 * un campo de una plantilla
	 */
	private static final String strCargarPlantillaSeccionesOpcionCampo = "SELECT "
			+ "ds.codigo_pk AS consecutivo_det_secc,"
			+ "ps.codigo_pk AS consecutivo, "
			+ "sp.orden AS orden, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo, "
			+ "sp.mostrar AS mostrar, "
			+ "ds.mostrar_modificacion AS mostrar_modificacion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "from det_secciones ds "
			+ "INNER JOIN plantillas_secciones ps ON(ps.codigo_pk = ds.codigo_pk_plan_seccion) "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=ps.seccion) "
			+ "where " + "ds.codigo_pk_opciones = ?   ";

	/**
	 * Cadena que consulta los codigos de la secciones asociadas a la opcion de
	 * un campo de un componente
	 */
	private static final String strCargarComponenteSeccionesOpcionCampo = "SELECT "
			+ "ds.codigo_pk AS consecutivo_det_secc,"
			+ "cs.codigo_pk AS consecutivo, "
			+ "sp.orden AS orden, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo, "
			+ "sp.mostrar AS mostrar, "
			+ "ds.mostrar_modificacion AS mostrar_modificacion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "from det_secciones ds "
			+ "INNER JOIN componentes_secciones cs ON(cs.codigo_pk = ds.codigo_pk_comp_seccion) "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=cs.seccion) "
			+ "where " + "ds.codigo_pk_opciones = ?  ";

	/**
	 * Cadena que consulta la informacion registrada del campo en la tabla
	 * plantillas_ingreso
	 */
	private static final String strCargarValorCampoSeccion_INGRESO = "SELECT "
			+ "pic.codigo_pk AS consecutivo, "
			+ "coalesce(pic.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM plantillas_ingresos pi "
			+ "INNER JOIN plantillas_ing_campos pic ON(pic.plantilla_ingreso=pi.codigo_pk) "
			+ "WHERE  ";

	/**
	 * Cadena Sql que consulta la informacion registrada del campo en la tabla
	 * pantillas_paciente
	 */
	private static final String strCargarValorCampoSeccion_PacienteOdont = " SELECT "
			+ "camp.codigo_pk AS consecutivo, "
			+ "coalesce(camp.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM manejopaciente.plantillas_pacientes plpac "
			+ "INNER JOIN manejopaciente.plantillas_pac_campos camp ON (camp.plantilla_paciente = plpac.codigo_pk) "
			+ "WHERE plpac.plantilla = ? AND camp.plantilla_campo_sec = ?  ";

	/**
	 * Cadena que consulta la informacion registrada del campo en la tabla
	 * plantillas_res_proc
	 */
	private static final String strCargarValorCampoSeccion_RESPROC = "SELECT "
			+ "ppc.codigo_pk AS consecutivo, "
			+ "coalesce(ppc.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM plantillas_res_proc prp "
			+ "INNER JOIN plantillas_proc_campos ppc ON(ppc.plantilla_res_proc=prp.codigo_pk) "
			+ "WHERE prp.plantilla = ? AND ppc.plantilla_campo_sec = ? ";

	/**
	 * Cadena que consulta la informacion registrada del campo en la tabla
	 * plantillas_evolucion.
	 */
	private static final String strCargarValorCampoSeccion_EVOLUCION = "SELECT "
			+ "pec.codigo_pk AS consecutivo, "
			+ "coalesce(pec.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM plantillas_evolucion pe "
			+ "INNER JOIN plantillas_evo_campos pec ON(pec.plantilla_evolucion=pe.codigo_pk) "
			+ "WHERE pe.plantilla = ? AND pec.plantilla_campo_sec=? ";

	/**
	 * Campo que consulta la informacion basica para cargar plantillas respuesta
	 * procedimiento
	 * */
	private static final String strCargarBasicaPlantillaResProc = "SELECT "
			+ "codigo_pk," + "plantilla " + "FROM " + "plantillas_res_proc "
			+ "WHERE " + "res_sol_proc = ? ";

	/**
	 * Cadena que consulta los valores del campo
	 */
	private static final String strCargarDetalleValorCampoSeccion_INGRESO = "SELECT "
			+ "vp.codigo_pk as consecutivo, "
			+ "coalesce(vp.valor,'') as valor,"
			+ "coalesce(vp.valores_opcion,'') as valores_opcion,"
			+ "coalesce(vp.imagen_opcion,'') AS imagen_opcion, "
			+ "coalesce(vp.seleccionado,'"
			+ ConstantesBD.acronimoNo
			+ "') as seleccionado, "
			+ "coalesce(vp.convencion,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as convencion,"
			+ "coalesce(co.nombre,'') as nombre_convencion,"
			+ "coalesce(co.archivo_convencion,'') as archivo_convencion "
			+ "FROM historiaclinica.valores_plan_ing_camp vp "
			+ "LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON(co.consecutivo = vp.convencion and vp.plantilla_ing_campo = ? )  "
			+ "WHERE vp.plantilla_ing_campo = ? ";

	/**
	 * Cadena que consulta los valores del campo
	 */
	private static final String strCargarDetalleValorCampoSeccion_RESPROC = "SELECT "
			+ "coalesce(valor,'') as valor, "
			+ "coalesce(valores_opcion,'') as valores_opcion, "
			+ "coalesce(imagen_opcion,'') AS imagen_opcion "
			+ "FROM valores_plan_proc_camp vp "
			+ "WHERE plantilla_proc_campos = ? ";

	/**
	 * Cadena que consulta los valores del campo.
	 */
	private static final String strCargarDetalleValorCampoSeccion_EVOLUCION = "SELECT "
			+ "vp.codigo_pk as consecutivo, "
			+ "coalesce(vp.valor,'') as valor, "
			+ "coalesce(vp.valores_opcion,'') as valores_opcion, "
			+ "coalesce(vp.imagen_opcion,'') AS imagen_opcion, "
			+ "coalesce(vp.seleccionado,'"
			+ ConstantesBD.acronimoNo
			+ "') as seleccionado,"
			+ "coalesce(vp.convencion,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as convencion,"
			+ "coalesce(co.nombre,'') as nombre_convencion,"
			+ "coalesce(co.archivo_convencion,'') as archivo_convencion "
			+ "FROM valores_plan_evo_camp vp "
			+ "LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON(co.consecutivo = vp.convencion) "
			+ "WHERE vp.plantilla_evo_campo = ? ";

	/**
	 * Caden que consulta valores del campo Plantilla Paciente Odontologia
	 */
	private static final String strCargarDetalleValorCampoSeccion_PacienteOdont = "SELECT "
			+ "codigo_pk AS codigopk, "
			+ "coalesce(valor,'') as valor, "
			+ "coalesce(valores_opcion,'') as valores_opcion, "
			+ "coalesce(imagen_opcion,'') AS  imagen_opcion, "
			+ "coalesce(seleccionado,'') AS seleccionado, "
			+ "coalesce(convencion,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS convencion "
			+ "FROM manejoPaciente.valores_plan_pac_campo  "
			+ "WHERE plantilla_pac_campo = ? ";

	/**
	 * Cadena que carga las subsecciones de la secciï¿½n de la plantilla
	 */
	private static final String strCargarSubseccionesSeccionPlantilla = "SELECT "
			+ "ps.codigo_pk AS consecutivo, "
			+ "coalesce(sp.orden,0) AS orden, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo, "
			+ "sp.mostrar AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "FROM plantillas_secciones ps "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=ps.seccion) "
			+ "WHERE ps.plantilla_sec_fija = ? AND sp.seccion_padre = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Escalas *********************************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena que consulta lo que se ha registrado de la escala por ingreso
	 */
	private static final String strCargarHistoricoEscalaXIngreso = "SELECT "
			+ "ei.codigo_pk AS consecutivo, "
			+ "coalesce(ei.escala_factor_prediccion||'','') AS codigo_factor_pred, "
			+ "ef.nombre AS nombre_factor_pred, "
			+ "ei.valor_total AS total_escala, "
			+ "coalesce(ei.observaciones,'')  AS observaciones, "
			+ "to_char(ei.fecha,'"
			+ ConstantesBD.formatoFechaAp
			+ "')  AS fecha, "
			+ "ei.hora AS hora "
			+ "FROM escalas_ingresos ei "
			+ "LEFT OUTER JOIN escalas_factores_pred ef ON(ef.codigo_pk=ei.escala_factor_prediccion) "
			+ "WHERE ei.ingreso = ? AND ei.escala = ? ORDER BY ei.fecha desc, ei.hora desc";

	/**
	 * Cadena para consltar la escala registrada por ingreso en una plantilla
	 * especï¿½fica
	 */
	private static final String strCargarEscalaPlantillaIngreso = "SELECT "
			+ "ei.codigo_pk as escala_ingreso "
			+ "FROM plantillas_ingresos pi "
			+ "INNER JOIN plantillas_escala_ing pei ON(pei.plantilla_ingreso=pi.codigo_pk) "
			+ "INNER JOIN escalas_ingresos ei ON(ei.codigo_pk=pei.escala_ingreso) "
			+ "WHERE pi.plantilla = ? ";

	/**
	 * Cadena para consltar la escala registrada por ingreso en una plantilla
	 * especï¿½fica
	 */
	private static final String strCargarEscalaPlantillaResProc = "SELECT "
			+ "ei.codigo_pk as escala_ingreso "
			+ "FROM  plantillas_res_proc prp "
			+ "INNER JOIN plantillas_escala_res_proc prpi ON(prpi.plantilla_res_proc = prp.codigo_pk) "
			+ "INNER JOIN escalas_ingresos ei ON(ei.codigo_pk=prpi.escalas_ingreso) "
			+ "WHERE prp.plantilla = ? AND prp.res_sol_proc = ? ";

	/**
	 * 
	 */
	private static final String strCargarEscalaPlantillaPaciente = "SELECT  "
			+ "ei.codigo_pk as escala_ingreso "
			+ "FROM manejopaciente.plantillas_pacientes ppac "
			+ "INNER JOIN manejopaciente.plantillas_escala_pac escpac ON(escpac.plantilla_paciente = ppac.codigo_pk) "
			+ "INNER JOIN historiaclinica.escalas_ingresos ei ON(ei.codigo_pk = escpac.escala_ingreso) "
			+ "WHERE ppac.plantilla = ? AND ppac.codigo_paciente = ? ";

	/**
	 * Cadena para consultar la escala registrada por ingreso en una plantilla
	 * especifica.
	 */
	private static final String strCargarEscalaPlantillaEvolucion = "SELECT "
			+ "ei.codigo_pk as escala_ingreso "
			+ "FROM plantillas_evolucion pe "
			+ "INNER JOIN plantillas_escalas_evo pee ON(pee.plantilla_evolucion=pe.codigo_pk) "
			+ "INNER JOIN escalas_ingresos ei ON(ei.codigo_pk=pee.escala_ingreso) "
			+ "WHERE pe.plantilla = ? ";

//	/**
//	 * Cadena Sql para consultar la escala registrada por ingeso de un plantilla
//	 * asociada a una Evolucion de Odontologia
//	 */
//	private static final String strCargarEscalaPlantillaEvolucionOdonto = "SELECT "
//			+ "ei.codigo_pk as escala_ingreso "
//			+ "FROM plantillas_evolucion pe "
//			+ "INNER JOIN plantillas_escalas_evo pee ON(pee.plantilla_evolucion=pe.codigo_pk) "
//			+ "INNER JOIN escalas_ingresos ei ON(ei.codigo_pk=pee.escala_ingreso) "
//			+ "WHERE pe.plantilla = ? AND pe.evolucion_odonto = ? ";

	/**
	 * Cadena que carga las secciones de una escala
	 */
	private static final String strCargarSeccionesEscala = "SELECT "
			+ "ss.codigo_pk AS codigo_pk, " + "ss.nombre as descripcion, "
			+ "ss.tipo_respuesta AS tipo_respuesta, "
			+ "ss.activo AS mostrar, "
			+ "ss.mostrar_modificacion AS mostrar_modificacion "
			+ "FROM escalas_secciones ss " + "WHERE ss.escala = ? ";

	/**
	 * Cadena que carga los factores de perdiccion de la escala
	 */
	private static final String strCargarFactoresPrediccionEscala = "SELECT "
			+ "ef.codigo_pk AS codigo_pk, " + "ef.codigo AS codigo, "
			+ "ef.nombre as nombre, "
			+ "ef.institucion as codigo_institucion, "
			+ "ef.valor_inicial AS valor_inicial, "
			+ "ef.valor_final AS valor_final," + "ef.activo AS mostrar, "
			+ "ef.mostrar_modificacion AS mostrar_modificacion "
			+ "FROM escalas_factores_pred ef "
			+ "WHERE ef.escala = ? AND ef.institucion = ?";

	/**
	 * Cadena para cargar los campos de la seccion escala
	 */
	private static final String strCargarCamposSeccionEscalaIngreso = "SELECT "
			+ "ec.codigo_pk AS consecutivo, "
			+ "ec.observaciones_requeridas AS observaciones_requeridas, "
			+ "cp.codigo_pk as codigo_pk, "
			+ "cp.nombre AS nombre, "
			+ "coalesce(cp.etiqueta,'') AS etiqueta, "
			+ "coalesce(cp.maximo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS maximo, "
			+ "coalesce(cp.minimo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS minimo, "
			+ "coalesce(cp.decimales,0) As decimales, "
			+ "cp.requerido AS requerido, "
			+ "cp.institucion as codigo_institucion, "
			+ "coalesce(eci.valor||'','')  AS valor, "
			+ "coalesce(eci.observaciones,'') AS observaciones, "
			+ "cp.mostrar AS mostrar, "
			+ "cp.mostrar_modificacion AS mostrar_modificacion,"
			+ "coalesce(eci.codigo_pk||'','') as consecutivo_historico "
			+ "FROM escalas_campos_seccion ec "
			+ "INNER JOIN campos_parametrizables cp ON(cp.codigo_pk=ec.campo_parametrizable) "
			+ "LEFT OUTER JOIN escalas_campos_ingresos eci ON(eci.escala_campo_seccion=ec.codigo_pk AND eci.escala_ingreso = ?) "
			+ "WHERE ec.escala_seccion = ? ";

	/**
	 * Cadena para cargar los campos de la seccion escala
	 */
	private static final String strCargarCamposSeccionEscalaPerfilNedNueva = "SELECT "
			+ "ec.codigo_pk AS consecutivo, "
			+ "ec.observaciones_requeridas AS observaciones_requeridas, "
			+ "cp.codigo_pk as codigo_pk, "
			+ "cp.nombre AS nombre, "
			+ "coalesce(cp.etiqueta,'') AS etiqueta, "
			+ "coalesce(cp.maximo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS maximo, "
			+ "coalesce(cp.minimo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS minimo, "
			+ "coalesce(cp.decimales,0) As decimales, "
			+ "cp.requerido AS requerido, "
			+ "cp.institucion as codigo_institucion, "
			+ "''  AS valor, "
			+ "'' AS observaciones, "
			+ "cp.mostrar AS mostrar, "
			+ "cp.mostrar_modificacion AS mostrar_modificacion "
			+ "FROM escalas_campos_seccion ec "
			+ "INNER JOIN campos_parametrizables cp ON(cp.codigo_pk=ec.campo_parametrizable) "
			+ "WHERE ec.escala_seccion = ? ";

	/**
	 * Cadena para cargar los campos de la seccion escala
	 */
	private static final String strCargarCamposSeccionEscalaPerfilNedYaInsertada = "SELECT "
			+ "ec.codigo_pk AS consecutivo, "
			+ "ec.observaciones_requeridas AS observaciones_requeridas, "
			+ "cp.codigo_pk as codigo_pk, "
			+ "cp.nombre AS nombre, "
			+ "coalesce(cp.etiqueta,'') AS etiqueta, "
			+ "coalesce(cp.maximo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS maximo, "
			+ "coalesce(cp.minimo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS minimo, "
			+ "coalesce(cp.decimales,0) As decimales, "
			+ "cp.requerido AS requerido, "
			+ "cp.institucion as codigo_institucion, "
			+ "coalesce(cpn.valor||'','')  AS valor, "
			+ "coalesce(cpn.observaciones||'','') AS observaciones, "
			+ "cp.mostrar AS mostrar, "
			+ "cp.mostrar_modificacion AS mostrar_modificacion "
			+ "FROM escalas_campos_seccion ec "
			+ "INNER JOIN campos_parametrizables cp ON(cp.codigo_pk=ec.campo_parametrizable) "
			+ "INNER JOIN manejopaciente.campos_perfil_ned cpn ON(cpn.escala_campo_seccion=ec.codigo_pk AND cpn.perfil_ned = ?) "
			+ "WHERE ec.escala_seccion = ? ";

	/**
	 * Cadena para cargar los adjuntos de una escala
	 */
	private static final String strCargarAdjuntosEscala = "SELECT "
			+ "ea.codigo_pk AS codigo_pk, "
			+ "ea.nombre_archivo AS nombre_archivo, "
			+ "ea.nombre_original AS nombre_original "
			+ "FROM escalas_adjuntos_ingreso ea "
			+ "WHERE ea.escala_ingreso = ?";

	/**
	 * Cadena para consultar los datos generales del registro de la escala
	 */
	private static final String strCargarDatosGeneralesEscalaIngreso = "SELECT "
			+ "coalesce(ef.codigo_pk,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as codigo_factor,"
			+ "coalesce(ef.nombre,'') AS nombre_factor,"
			+ "ei.valor_total,"
			+ "coalesce(ei.observaciones,'') as observaciones "
			+ "FROM escalas_ingresos ei "
			+ "LEFT OUTER JOIN escalas_factores_pred ef ON(ef.codigo_pk = ei.escala_factor_prediccion) "
			+ "WHERE ei.codigo_pk = ?";

	/**
	 * Cadena para cargar los elementos de un componente
	 */
	private static final String strCargarElementosComponente = "SELECT * FROM "
			+ "( " + "( " + "SELECT " + "cs.codigo_pk||'' as consecutivo, "
			+ tipoElementoSeccion
			+ " AS tipo_elemento, "
			+ // se identifica el tipo elemento seccion
			"sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.orden,0) AS orden, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo AS tipo, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS observaciones_requeridas, "
			+ "sp.mostrar AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion,"
			+ "sp.institucion AS codigo_institucion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "FROM componentes_secciones cs "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=cs.seccion) "
			+ "WHERE cs.componente = ? AND sp.seccion_padre is null "
			+ ") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "ce.componente || '"
			+ ConstantesBD.separadorSplit
			+ "' || ce.escala as consecutivo, "
			+ tipoElementoEscala
			+ " as tipo_elemento, "
			+ "e.codigo_pk AS codigo_pk, "
			+ "e.codigo AS codigo, "
			+ "e.nombre AS descripcion, "
			+ "ce.orden AS orden, "
			+ "0 AS columnas_seccion, "
			+ "'' AS tipo, "
			+ "e.observaciones_requeridas AS observaciones_requeridas, "
			+ "ce.mostrar AS mostrar, "
			+ "CASE WHEN e.mostrar_modificacion = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN e.mostrar_modificacion ELSE coalesce(ce.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ "e.institucion AS codigo_institucion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM componentes_escalas ce "
			+ "INNER JOIN escalas e ON(e.codigo_pk=ce.escala) "
			+ "WHERE ce.componente = ? " + ") " + ") t ORDER BY t.orden";

	/**
	 * Cadena para cargar los elementos de un componente con filtro de datos del
	 * paciente
	 */
	private static final String strCargarElementosComponenteConFiltro = "SELECT * FROM "
			+ "( " + "( " + "SELECT " + "cs.codigo_pk||'' as consecutivo, "
			+ tipoElementoSeccion
			+ " AS tipo_elemento, "
			+ // se identifica el tipo elemento seccion
			"sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.orden,0) AS orden, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo AS tipo, "
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS observaciones_requeridas, "
			+ "sp.mostrar AS mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion,"
			+ "sp.institucion AS codigo_institucion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo  "
			+ "FROM componentes_secciones cs "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=cs.seccion) "
			+ "WHERE cs.componente = ? AND sp.seccion_padre is null "
			+ " and (sp.sexo is null or sp.sexo = ?) "
			+ " and ((sp.edad_inicial_dias is null and sp.edad_final_dias is null) or (sp.edad_inicial_dias <= ? and sp.edad_final_dias >= ?))"
			+ ") "
			+ "UNION "
			+ "( "
			+ "SELECT "
			+ "ce.componente || '"
			+ ConstantesBD.separadorSplit
			+ "' || ce.escala as consecutivo, "
			+ tipoElementoEscala
			+ " as tipo_elemento, "
			+ "e.codigo_pk AS codigo_pk, "
			+ "e.codigo AS codigo, "
			+ "e.nombre AS descripcion, "
			+ "ce.orden AS orden, "
			+ "0 AS columnas_seccion, "
			+ "'' AS tipo, "
			+ "e.observaciones_requeridas AS observaciones_requeridas, "
			+ "ce.mostrar AS mostrar, "
			+ "CASE WHEN e.mostrar_modificacion = '"
			+ ConstantesBD.acronimoNo
			+ "' THEN e.mostrar_modificacion ELSE coalesce(ce.mostrar_modificacion,'') END AS mostrar_modificacion, "
			+ "e.institucion AS codigo_institucion, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS sexo, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_inicial_dias, "
			+ ConstantesBD.codigoNuncaValido
			+ " AS edad_final_dias,"
			+ "'"
			+ ConstantesBD.acronimoNo
			+ "' AS restriccion_val_campo "
			+ "FROM componentes_escalas ce "
			+ "INNER JOIN escalas e ON(e.codigo_pk=ce.escala) "
			+ "WHERE ce.componente = ? " + ") " + ") t ORDER BY t.orden";

	/**
	 * Cadena usada para consultar la fecha/hora registro de la plantilla
	 * ingreso
	 */
	private static final String strCargarFechaHoraRegistroPlantilla_INGRESO = "SELECT fecha_modifica,hora_modifica FROM plantillas_ingresos WHERE plantilla = ?";

	/**
	 * Cadena usada para consultar la fecha/hora registro de la plantilla de la
	 * Respuesta de Procedimientos
	 */
	private static final String strCargarFechaHoraRegistroPlantilla_RESPPROC = "SELECT fecha_modifica,hora_modifica FROM plantillas_res_proc WHERE plantilla = ? AND res_sol_proc= ? ";

	/**
	 * Cadena usada para consultar la fecha/hora registro de la plantilla de la
	 * Evolucion.
	 */
	private static final String strCargarFechaHoraRegistroPlantilla_EVOLUCION = "SELECT "
			+ "fecha_modifica, "
			+ "hora_modifica "
			+ "FROM plantillas_evolucion "
			+ "WHERE plantilla = ? AND evolucion = ? ";

	/**
	 * Cadena Sql para consultar la fecha/hora registro de la plantilla
	 * Evolucion Odontologia
	 */
	private static final String strCargarFechaHoraRegistroPlantilla_EVOLUCION_ODONT = "SELECT "
			+ "fecha_modifica, "
			+ "hora_modifica "
			+ "FROM plantillas_evolucion "
			+ "WHERE plantilla = ? AND evolucion_odonto = ? ";

	/**
	 * Cadena para saber si se puede mostrar un componente sin registro de
	 * informacion parametrizable dependiendo de su fecha/hora inicio y
	 * fecha/hora fin del registro del componente
	 */
	private static final String strPuedoMostrarComponenteSinRegistroParametrico = "SELECT "
			+ "CASE WHEN count(1) > 0 THEN '"
			+ ConstantesBD.acronimoSi
			+ "' ELSE '"
			+ ConstantesBD.acronimoNo
			+ "' END AS mostrar_modificacion "
			+ "FROM plantillas_componentes WHERE codigo_pk = ? ";

	/**
	 * Cadena que consulta los campos de la seccion del componente
	 */
	private static final String strCargarCamposSeccionComponente = "SELECT "
			+ "cp.codigo_pk AS consecutivo, " + "cp.codigo_pk as codigo_pk, "
			+ "cp.codigo AS codigo, " + "cp.nombre AS nombre, "
			+ "coalesce(cp.etiqueta,'') AS etiqueta, "
			+ "cp.tipo AS codigo_tipo, " + "tc.nombre AS nombre_tipo, "
			+ "cp.tamanio AS tamanio, " + "coalesce(cp.signo,'') AS signo, "
			+ "coalesce(cp.unidad,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS codigo_unidad, "
			+ "coalesce(uc.acronimo,'') AS nombre_unidad, "
			+ "coalesce(cp.valor_predeterminado,'') AS valor_predeterminado, "
			+ "coalesce(cp.maximo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS maximo, "
			+ "coalesce(cp.minimo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS minimo, "
			+ "coalesce(cp.decimales,0) As decimales, "
			+ "coalesce(cp.columnas_ocupadas,1) AS columnas_ocupadas, "
			+ "coalesce(cp.orden,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS orden, "
			+ "cp.unico_fila AS unico_fila, "
			+ "cp.requerido AS requerido, "
			+ "coalesce(cp.formula,'') As formula, "
			+ "cp.institucion as codigo_institucion, "
			+ "cp.tipo_html AS tipo_html, "
			+ "cp.mostrar AS mostrar, "
			+ "cp.mostrar_modificacion AS mostrar_modificacion, "
			+ "coalesce(cp.maneja_imagen,' ') as maneja_img, "
			+ "coalesce(cp.imagen_a_asociar,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as img_asociar, "
			+ "coalesce(ib.codigo_pk,"
			+ ConstantesBD.codigoNuncaValido
			+ ") as cod_img_base, "
			+ "coalesce(ib.nombre_imagen,' ') as nom_img_base, "
			+ "coalesce(ib.imagen,' ') as path_img_base "
			+ "FROM historiaclinica.componentes_campos_sec pc "
			+ "INNER JOIN historiaclinica.campos_parametrizables cp ON(cp.codigo_pk=pc.campo_parametrizable) "
			+ "INNER JOIN historiaclinica.tipos_campo_param tc ON(tc.codigo=cp.tipo) "
			+ "LEFT OUTER JOIN historiaclinica.imagenes_base ib ON (cp.imagen_a_asociar = ib.codigo_pk ) "
			+ "LEFT OUTER JOIN historiaclinica.unidades_campo_param uc ON(uc.codigo=cp.unidad) "
			+ "WHERE pc.componente_seccion = ? ORDER BY orden";

	/**
	 * Cadena para consultar el valor del campo de una seccion de un componente
	 * puede ser por ingreso, numero solicitud o codigo paciente
	 */
	private static final String strCargarValorCampoSeccionComponente_INGRESO = "SELECT "
			+ "ci.codigo_pk AS consecutivo, "
			+ "coalesce(ci.nombre_archivo_original,'') AS nombre_archivo_original,"
			+ "coalesce(ci.generar_alerta,'"
			+ ConstantesBD.acronimoNo
			+ "') as generar_alerta "
			+ "FROM plantillas_ingresos pi "
			+ "INNER JOIN plantillas_comp_ing pci on(pci.plantilla_ingreso=pi.codigo_pk) "
			+ "INNER JOIN componentes_ingreso ci ON(ci.codigo_pk = pci.componente_ingreso) ";

	/**
	 * Cadena para consultar el valor del campo de una seccion de un componente
	 * de Respuesta de Procedimientos
	 */
	private static final String strCargarValorCampoSeccionComponente_RESPPROC = "SELECT "
			+ "cp.codigo_pk AS consecutivo, "
			+ "coalesce(cp.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM plantillas_res_proc prp "
			+ "INNER JOIN plantillas_comp_res_proc pcrp on(pcrp.plantilla_res_proc = prp.codigo_pk) "
			+ "INNER JOIN componentes_res_proc cp on(cp.codigo_pk = pcrp.componente_res_proc) "
			+ "WHERE prp.res_sol_proc = ? AND cp.componente_seccion = ? AND cp.campo_parametrizable = ? ";

	/**
	 * Cadena para consultar el valor del campo de una seccion de un componente
	 * de la evolucion.
	 */
	private static final String strCargarValorCampoSeccionComponente_EVOLUCION = "SELECT "
			+ "ce.codigo_pk AS consecutivo, "
			+ "coalesce(ce.nombre_archivo_original,'') AS nombre_archivo_original, "
			+ "coalesce(ce.generar_alerta,'"
			+ ConstantesBD.acronimoNo
			+ "') as generar_alerta "
			+ "FROM plantillas_evolucion pe "
			+ "INNER JOIN plantillas_comp_evo pce ON(pce.plantilla_evolucion=pe.codigo_pk) "
			+ "INNER JOIN componentes_evolucion ce ON(ce.codigo_pk=pce.componente_evolucion) "
			+ "WHERE  pe.evolucion=? AND ce.componente_seccion=? AND ce.campo_parametrizable=? ";

	/**
	 * Cadena Sql para consultar el valor del campo de una seccion de un
	 * Componente de la Evolucion de un paciente de Odontologia
	 */
	private static final String strCargarValorCampoSeccionComponente_EVOLUCION_ODONT = "SELECT "
			+ "ce.codigo_pk AS consecutivo, "
			+ "coalesce(ce.nombre_archivo_original,'') AS nombre_archivo_original, "
			+ "coalesce(ce.generar_alerta,'"
			+ ConstantesBD.acronimoNo
			+ "') as generar_alerta "
			+ "FROM plantillas_evolucion pe "
			+ "INNER JOIN plantillas_comp_evo pce ON(pce.plantilla_evolucion=pe.codigo_pk) "
			+ "INNER JOIN componentes_evolucion ce ON(ce.codigo_pk=pce.componente_evolucion) "
			+ "WHERE ce.evolucion_odonto = ? AND ce.componente_seccion=? AND ce.campo_parametrizable=? ";

	private static final String strCargarValorCampoSeleccionComponente_PACIENTE = "SELECT "
			+ "ci.codigo_pk AS consecutivo, "
			+ "coalesce(ci.nombre_archivo_original,'') AS nombre_archivo_original "
			+ "FROM manejopaciente.plantillas_pacientes ppac "
			+ "INNER JOIN manejopaciente.plantillas_comp_pac plcomp ON(plcomp.plantilla_paciente = ppac.codigo_pk ) "
			+ "INNER JOIN historiaclinica.componentes_ingreso ci ON(ci.codigo_pk = plcomp.componente_ingreso) "
			+ "WHERE ppac.codigo_paciente = ?  AND ci.componente_seccion = ? AND  ci.campo_parametrizable = ? ";

	/**
	 * Cadena para consultar los N valores de un campo de una seccion de un
	 * componente puede ser por ingreso, numero solicitud o codigo_paciente
	 */
	private static final String strCargarDetalleValorCampoSeccionComponente_INGRESO = "SELECT "
			+ "vc.codigo_pk as consecutivo, "
			+ "coalesce(vc.valor,'') AS valor,"
			+ "coalesce(vc.valores_opcion,'') as valores_opcion,"
			+ "coalesce(vc.seleccionado,'"
			+ ConstantesBD.acronimoNo
			+ "') as seleccionado "
			+ "FROM valores_comp_ingreso vc "
			+ "WHERE vc.componente_ingreso = ?";

	/**
	 * Cadena para consultar los N valores de un campo de una seccion de un
	 * componente de Respuesta de Procedimientos
	 */
	private static final String strCargarDetalleValorCampoSeccionComponente_RESPPROC = "SELECT "
			+ "coalesce(vc.valor,'') AS valor, "
			+ "coalesce(vc.valores_opcion,'') as valores_opcion "
			+ "FROM valores_comp_res_proc vc "
			+ "WHERE vc.componente_res_proc = ?";

	/**
	 * Cadena para consultar los N valores de un campo de una seccion de un
	 * componente de la evolucion.
	 */
	private static final String strCargarDetalleValorCampoSeccionComponente_EVOLUCION = "SELECT "
			+ "vc.codigo_pk as consecutivo, "
			+ "coalesce(vc.valor,'') AS valor, "
			+ "coalesce(vc.valores_opcion,'') as valores_opcion, "
			+ "coalesce(vc.seleccionado,'"
			+ ConstantesBD.acronimoNo
			+ "') as seleccionado "
			+ "FROM valores_comp_evolucion vc "
			+ "WHERE vc.componente_evolucion= ?";

	/**
	 * Cadena para consultar las subsecciones de una seccion de un componente
	 */
	private static final String strCargarSubseccionesSeccionComponente = "SELECT "
			+ "cs.codigo_pk as consecutivo, "
			+ "sp.codigo_pk AS codigo_pk, "
			+ "coalesce(sp.codigo||'','') AS codigo, "
			+ "coalesce(sp.descripcion,'') AS descripcion, "
			+ "coalesce(sp.orden,0) AS orden, "
			+ "coalesce(sp.columnas_seccion,10) AS columnas_seccion, "
			+ "sp.tipo, "
			+ "sp.mostrar As mostrar, "
			+ "sp.mostrar_modificacion AS mostrar_modificacion, "
			+ "coalesce(sp.sexo,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS sexo, "
			+ "coalesce(sp.edad_inicial_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, "
			+ "coalesce(sp.edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido
			+ ") AS edad_final_dias,"
			+ "sp.restriccion_val_campo "
			+ "FROM componentes_secciones cs "
			+ "INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=cs.seccion) "
			+ "WHERE cs.componente = ? AND sp.seccion_padre = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Secciones Parametrizables ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena que consulta la informacion de secciones Parametrizables
	 * */
	private static String strConsultarSeccionesParametrizables = "SELECT "
			+ "codigo," + "descripcion," + "orden," + "columnas_seccion,"
			+ "tipo," + "codigo_pk," + "seccion_padre," + "mostrar,"
			+ "mostrar_modificacion, " + "coalesce(sexo,"
			+ ConstantesBD.codigoNuncaValido + ") AS sexo, "
			+ "coalesce(edad_inicial_dias," + ConstantesBD.codigoNuncaValido
			+ ") AS edad_inicial_dias, " + "coalesce(edad_final_dias,"
			+ ConstantesBD.codigoNuncaValido + ") AS edad_final_dias,"
			+ "restriccion_val_campo " + "FROM secciones_parametrizables "
			+ "WHERE institucion = ? ";

	/**
	 * Cadena para insertar secciones Parametrizables
	 * */
	private static String strInsertarSeccionesParametrizables = "INSERT INTO secciones_parametrizables ("
			+ "codigo,"
			+ "descripcion,"
			+ "orden,"
			+ "columnas_seccion,"
			+ "tipo,"
			+ "codigo_pk,"
			+ "seccion_padre,"
			+ "institucion,"
			+ "mostrar,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "usuario_modifica,"
			+ "mostrar_modificacion, "
			+ "sexo, "
			+ "edad_inicial_dias, "
			+ "edad_final_dias,"
			+ "restriccion_val_campo) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena para actualizar secciones Parametrizables
	 * */
	private static String strActualizarSeccionesParametrizables = "UPDATE secciones_parametrizables SET "
			+ "codigo = ?, "
			+ "descripcion = ?, "
			+ "orden = ?, "
			+ "columnas_seccion = ?, "
			+ "tipo = ? ,"
			+ "mostrar = ? ,"
			+ "mostrar_modificacion = ?,"
			+ "fecha_modifica = ? ,"
			+ "hora_modifica = ?,"
			+ "usuario_modifica = ?, "
			+ "sexo=?, "
			+ "edad_inicial_dias=?, "
			+ "edad_final_dias=?,"
			+ "restriccion_val_campo = ? " + "WHERE codigo_pk = ?  ";

	/**
	 * Cadena para actualizar mostrarModificable de secciones_parametrizables
	 * */
	private static String strActualizarMostrarModSeccionParam = "UPDATE secciones_parametrizables SET "
			+ "mostrar_modificacion = ?,"
			+ ConstantesBD.separadorSplitComplejo
			+ "fecha_modifica = ? ,"
			+ "hora_modifica = ?,"
			+ "usuario_modifica = ?  " + "WHERE codigo_pk = ?  ";

	/**
	 * Cadena para insertar plantillas secciones
	 * */
	private static String strInsertarPlantillasSecciones = "INSERT INTO plantillas_secciones (codigo_pk,plantilla_sec_fija,seccion) "
			+ "VALUES(?,?,?) ";

	/**
	 * Cadena para insertar plantillas escalas
	 * */
	private static String strInsertarPlantillasEscalas = "INSERT INTO plantillas_escalas (codigo_pk,plantilla_sec_fija,escala,orden,mostrar,mostrar_modificacion, requerida) "
			+ "VALUES(?,?,?,?,?,?,?) ";

	/**
	 * 
	 */
	private static String strInsertarPlantillasComponentes = "INSERT INTO plantillas_componentes (codigo_pk,plantilla_sec_fija,componente,orden,mostrar,mostrar_modificacion,fecha_inicial_registro,hora_inicial_registro) "
			+ "VALUES(?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena para actualizar Plantillas Escalas
	 * */
	private static final String strActualizarPlantillasEscalas = "UPDATE "
			+ "plantillas_escalas " + "SET " + "orden = ?, " + "mostrar = ?, "
			+ "mostrar_modificacion = ? " + "WHERE codigo_pk = ? ";

	/**
	 * Cadena para actualizar Plantillas Componentes
	 * */
	private static final String strActualizarPlantillasComponentes = "UPDATE "
			+ "plantillas_componentes " + "SET " + "componente = ?, "
			+ "orden = ?, " + "mostrar = ?, " + "mostrar_modificacion = ?, "
			+ "fecha_final_registro = ?, " + "hora_final_registro = ? "
			+ "WHERE codigo_pk = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Plantillas Secciones Fijas ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Inserta informacion de las Plantillas Secciones Fijas
	 * */
	private static String strInsertarPlantillasSecFijas = "INSERT INTO plantillas_sec_fijas ("
			+ "codigo_pk,"
			+ "fun_param_secciones_fijas,"
			+ "seccion_param,"
			+ "plantilla,"
			+ "orden,"
			+ "mostrar,"
			+ "usuario_modifica,"
			+ "fecha_modifica,"
			+ "hora_modifica) "
			+ "VALUES(?,?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena para actualizar Plantillas Secciones Fijas
	 * */
	private static final String strActualizarPlantillasSecFijas = "UPDATE "
			+ "plantillas_sec_fijas " + "SET " + "orden = ?, "
			+ "mostrar = ?, " + "fun_param_secciones_fijas = ?, "
			+ "seccion_param = ?," + "usuario_modifica = ?,"
			+ "fecha_modifica = ?, " + "hora_modifica = ? "
			+ "WHERE codigo_pk = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Registro Plantillas Respuesta de Procedimientos ******/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena usada para insertar el encabezado del registro de plantilla res
	 * proc
	 * */
	private static final String strInsertarPlantillasResProc = "INSERT INTO plantillas_res_proc "
			+ "(codigo_pk,"
			+ "res_sol_proc,"
			+ "plantilla,"
			+ "fecha,"
			+ "hora,"
			+ "usuario_modifica,"
			+ "fecha_modifica,"
			+ "hora_modifica) VALUES(?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena usada para insertar Plantillas_proc_campos
	 * */
	private static final String strInsertarPlantillasProcCampos = "INSERT INTO plantillas_proc_campos "
			+ "(codigo_pk,"
			+ "plantilla_campo_sec,"
			+ "plantilla_res_proc,"
			+ "nombre_archivo_original) " + "VALUES(?,?,?,?)";

	/**
	 * Cadena usada para insertar en Valores plan proc camp
	 * */
	private static final String strInsertarValoresPlanProcCamp = "INSERT INTO valores_plan_proc_camp "
			+ "(codigo_pk,"
			+ "plantilla_proc_campos,"
			+ "valor,"
			+ "valores_opcion," + "imagen_opcion) " + "VALUES(?,?,?,?,?)";

	/**
	 * Cadena usada para insertar en Plantillas_res_proc_ing
	 * */
	private static final String strInsertarPlantillasResProcIng = "INSERT INTO plantillas_escala_res_proc "
			+ "(plantilla_res_proc,"
			+ "escalas_ingreso,"
			+ "plantillas_escala,"
			+ "componente,"
			+ "escala) "
			+ "VALUES(?,?,?,?,?) ";

	/**
	 * Cadena usada para insertar en componentes respuesta de procedimiento
	 * */
	private static final String strInsertarComponentesResProc = "INSERT INTO componentes_res_proc "
			+ "(codigo_pk,"
			+ "campo_parametrizable,"
			+ "componente_seccion,"
			+ "res_sol_proc,"
			+ "nombre_archivo_original,"
			+ "fecha,"
			+ "hora,"
			+ "usuario_modifica,"
			+ "fecha_modifica,"
			+ "hora_modifica) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena usada para insertar en Valores Componente Respuesta de
	 * Procedimiento
	 * */
	private static final String strInsertarValoresCompResProc = "INSERT INTO valores_comp_res_proc "
			+ "(codigo_pk,"
			+ "componente_res_proc,"
			+ "valor,"
			+ "valores_opcion) " + "VALUES(?,?,?,?)";

	/**
	 * Cadena usada para insertar en Plantillas Componentes Respuesta de
	 * Procedimientos
	 * */
	private static final String strInsertarPlantillasCompResProc = "INSERT INTO plantillas_comp_res_proc "
			+ "(plantilla_res_proc,"
			+ "plantilla_componente,"
			+ "componente_res_proc) " + "VALUES(?,?,?)";

	/**
	 * Cadena que consulta la informacion de los textos predeterminados para los
	 * resultados de los procedimientos
	 * */
	private static final String strTextoPredeterminados = "SELECT "
			+ "codigo, " + "descripcion_texto AS descripcion,"
			+ "texto_predeterminado AS texto " + "FROM textos_resp_proc "
			+ "WHERE institucion = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Registro Plantillas de la Evolucion ******************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena usada para insertar el encabezado del registro de plantilla
	 * evolucion
	 * */
	private static final String strInsertarPlantillasEvolucion = "INSERT INTO plantillas_evolucion "
			+ "(codigo_pk,"
			+ "evolucion,"
			+ "plantilla,"
			+ "fecha,"
			+ "hora,"
			+ "usuario_modifica,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "evolucion_odonto," 
			+ "usuario_creacion) VALUES(?,?,?,?,?,?,?,?,?,?) ";

	/**
	 * Cadena usada para modificar el encabezado del registro de la plantilla de
	 * evolucion
	 */
	private static final String strModificarPlantillaEvolucion = "UPDATE historiaclinica.plantillas_evolucion SET fecha = ?, hora = ?, usuario_modifica = ?,fecha_modifica = current_date, hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ " , evolucion_odonto=?  WHERE codigo_pk = ? ";

	/**
	 * Cadena usada para insertar en plantillas evo campos.
	 */
	private static final String strInsertarPlantillasEvoCampos = "INSERT INTO plantillas_evo_campos "
			+ "(codigo_pk,"
			+ "plantilla_campo_sec,"
			+ "plantilla_evolucion,"
			+ "nombre_archivo_original) " + "VALUES(?,?,?,?)";

	/**
	 * Cadena usada para modifiacr en plantillas evo campos
	 */
	private static final String strModificarPlantillasEvoCampos = "UPDATE historiaclinica.plantillas_evo_campos SET nombre_archivo_original = ? WHERE codigo_pk = ? ";

	/**
	 * Cadena usada para insertar en Valores plan evo camp.
	 * */
	private static final String strInsertarValoresPlanEvoCamp = "INSERT INTO valores_plan_evo_camp "
			+ "(codigo_pk,"
			+ "plantilla_evo_campo,"
			+ "valor,"
			+ "valores_opcion,"
			+ "imagen_opcion,"
			+ "seleccionado,"
			+ "convencion) " + "VALUES(?,?,?,?,?,?,?)";

	/**
	 * CAdena usada para modificar el valores de l campo de la plantilla de
	 * evolucion
	 */
	private static final String strModificarValoresPlanEvoCamp = "UPDATE historiaclinica.valores_plan_evo_camp SET valor = ?, valores_opcion = ?, imagen_opcion = ?, seleccionado = ?, convencion = ? WHERE codigo_pk = ?";

	/**
	 * Cadena usada para insertar en Plantillas_escalas_evo
	 * */
	private static final String strInsertarPlantillasEscalasEvo = "INSERT INTO plantillas_escalas_evo "
			+ "(plantilla_evolucion,"
			+ "escala_ingreso,"
			+ "plantilla_escala,"
			+ "componente," + "escala) " + "VALUES(?,?,?,?,?) ";

	/**
	 * Cadena usada para insertar en componentes evolucion.
	 * */
	private static final String strInsertarComponentesEvolucion = "INSERT INTO componentes_evolucion "
			+ "(codigo_pk,"
			+ "campo_parametrizable,"
			+ "componente_seccion,"
			+ "evolucion,"
			+ "nombre_archivo_original,"
			+ "fecha,"
			+ "hora,"
			+ "usuario_modifica,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "evolucion_odonto,"
			+ "generar_alerta) "
			+ "VALUES(?,?,?,?,?,?,?,?,current_date,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?,?) ";

	/**
	 * Cadena usada para modificar un componente de evolucion
	 */
	private static final String strModificarComponenteEvolucion = "UPDATE historiaclinica.componentes_evolucion SET nombre_archivo_original = ?, fecha = ?, hora=?, usuario_modifica = ?, fecha_modifica = current_date, hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ " WHERE codigo_pk = ? ";


	/**
	 * Cadena usada para modificar un componente de evolucion
	 */
	private static final String strExisteComponenteEvolucion = "select count(1) as resultado from historiaclinica.componentes_evolucion  WHERE codigo_pk = ? ";
	
	
	/**
	 * Cadena usada para insertar en Valores Componente evolucion
	 * */
	private static final String strInsertarValoresCompEvolucion = "INSERT INTO valores_comp_evolucion "
			+ "(codigo_pk,"
			+ "componente_evolucion,"
			+ "valor,"
			+ "valores_opcion," + "seleccionado) " + "VALUES(?,?,?,?,?)";

	/**
	 * Cadena usada para modificar en valores componente evolucion
	 */
	private static final String strModificarValoresCompEvolucion01 = "UPDATE historiaclinica.valores_comp_evolucion SET valor = ?, valores_opcion = ?, seleccionado = ? WHERE codigo_pk = ? ";

	/**
	 * Cadena usada para modificar en valores componente evolucion
	 */
	private static final String strModificarValoresCompEvolucion02 = "UPDATE historiaclinica.valores_comp_evolucion SET valor = ?, valores_opcion = ?, seleccionado = ? WHERE componente_evolucion = ? ";

	/**
	 * Cadena usada para insertar en Plantillas Componentes evolucion
	 * */
	private static final String strInsertarPlantillasCompEvo = "INSERT INTO plantillas_comp_evo "
			+ "(plantilla_evolucion,"
			+ "plantilla_componente,"
			+ "componente_evolucion) " + "VALUES(?,?,?)";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Registro Plantillas Ingresos ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena usada para insertar el encabezado del registro de la plantilla del
	 * ingreso
	 */
	private static final String strInsertarPlantillaIngreso = "INSERT INTO plantillas_ingresos "
			+ "(codigo_pk,"
			+ "ingreso,"
			+ "numero_solicitud,"
			+ "codigo_paciente,"
			+ "plantilla,"
			+ "fecha,"
			+ "hora,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "usuario_modifica,"
			+ "valoracion_odonto) "
			+ "Values "
			+ "(?,?,?,?,?,?,?,CURRENT_DATE,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?,?)";

	/**
	 * Cadena usada para modificar la plantilla Ingreso de la valoracion Odonto
	 */
	private static final String strModificarPlantillaIngresoOdonto = "UPDATE historiaclinica.plantillas_ingresos SET "
			+ "fecha = ?, hora = ?, fecha_modifica = current_date, hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ ", usuario_modifica = ? WHERE valoracion_odonto = ?";

	/**
	 * Cadena que inserta el campo de la plantilla del ingreso
	 */
	private static final String strInsertarCampoPlantillaIngreso = "INSERT INTO plantillas_ing_campos "
			+ "(codigo_pk,"
			+ "plantilla_campo_sec,"
			+ "nombre_archivo_original,"
			+ "plantilla_ingreso) "
			+ "VALUES "
			+ "(?,?,?,?)";

	/**
	 * Cadena implementada para modificar la informacion de un campo
	 * parametrizable que ya se habï¿½a registrado
	 */
	private static final String strModificarCampoPlantillaIngreso = "UPDATE historiaclinica.plantillas_ing_campos SET nombre_archivo_original = ? WHERE codigo_pk = ?";

	/**
	 * Cadena para isnertar el valor del campo de la plantilla del ingreso
	 */
	private static final String strInsertarValorPlantillaIngreso = "INSERT INTO valores_plan_ing_camp "
			+ "(codigo_pk,"
			+ "plantilla_ing_campo,"
			+ "valor,"
			+ "valores_opcion,"
			+ "imagen_opcion,"
			+ "seleccionado,"
			+ "convencion) " + "VALUES " + "(?,?,?,?,?,?,?)";

	/**
	 * Cadena para modificar el valor del campo de la plantilla
	 */
	private static final String strModificarValorPlantillaIngreso = "UPDATE historiaclinica.valores_plan_ing_camp SET "
			+ "valores_opcion = ?, imagen_opcion = ?, seleccionado = ?, convencion = ?, valor = ? WHERE ";

	/**
	 * Cadena para insertar los campos de un componente x ingreso
	 */
	private static final String strInsertarCampoComponenteIngreso = "INSERT INTO componentes_ingreso "
			+ "(codigo_pk,"
			+ "componente_seccion,"
			+ "campo_parametrizable,"
			+ "ingreso,"
			+ "codigo_paciente,"
			+ "numero_solicitud,"
			+ "nombre_archivo_original,"
			+ "fecha,"
			+ "hora,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "usuario_modifica,"
			+ "valoracion_odo,"
			+ "generar_alerta) "
			+ "VALUES "
			+ "(?,?,?,?,?,?,?,?,?,current_date,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?,?,?)";

	/**
	 * Cadena para modificar los campos de un componente x ingreso
	 */
	private static final String strModificarCampoComponenteIngresoOdontologia = "UPDATE historiaclinica.componentes_ingreso set "
			+ "nombre_archivo_original = ?, fecha = ?, hora = ?, fecha_modifica = current_date, hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ ", usuario_modifica = ? WHERE codigo_pk = ?";

	/**
	 * Cadena para insertar el valor del componente ingreso
	 */
	private static final String strInsertarValorComponenteIngreso = "INSERT INTO valores_comp_ingreso (codigo_pk,componente_ingreso,valor,valores_opcion,seleccionado) VALUES (?,?,?,?,?)";

	/**
	 * Cadena usada para la modificacion de los valores del campo de un
	 * componente
	 */
	private static final String strModificarValorComponenteIngreso01 = "UPDATE historiaclinica.valores_comp_ingreso set valor = ?, valores_opcion = ?, seleccionado = ? WHERE codigo_pk = ?";

	/**
	 * Cadena usada para la modificacion de los valores del campo de un
	 * componente
	 */
	private static final String strModificarValorComponenteIngreso02 = "UPDATE historiaclinica.valores_comp_ingreso set valor = ?, valores_opcion = ?, seleccionado = ? WHERE componente_ingreso = ?";

	/**
	 * Cadena para registrar un campo de un componente de una plantilla por
	 * ingreso
	 */
	private static final String strInsertarPlantillaComponenteIngreso = "INSERT INTO plantillas_comp_ing (plantilla_ingreso,plantilla_componente,componente_ingreso) VALUES (?,?,?)";

	/**
	 * Cadena para insertar el encabezado del registro de la escala x ingreso
	 */
	private static final String strInsertarEscalaIngreso = "INSERT INTO escalas_ingresos "
			+ "(codigo_pk,"
			+ "ingreso,"
			+ "escala,"
			+ "escala_factor_prediccion,"
			+ "valor_total,"
			+ "observaciones,"
			+ "fecha,"
			+ "hora,"
			+ "fecha_modifica,"
			+ "hora_modifica,"
			+ "usuario_modifica) "
			+ "VALUES "
			+ "(?,?,?,?,?,?,?,?,CURRENT_DATE,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?)";

	/**
	 * Cadena Sql para modificar la tabla escalas ingresos por ingresos Pacinte
	 * Odontologia
	 */
	private static final String strModificarEscalaIngresoXIngPacienteOdon = "UPDATE escalas_ingresos  SET "
			+ "escala_factor_prediccion = ?,"
			+ "valor_total = ?, "
			+ "observaciones = ?, "
			+ "fecha = ?, "
			+ "hora = ?, "
			+ "fecha_modifica = CURRENT_DATE, "
			+ "hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ ", "
			+ "usuario_modifica = ? " + "WHERE codigo_pk = ? ";

	/**
	 * Cadena Sql para modificar el valor de un campo de escala por Paciente
	 * Odontologia
	 */
	private static final String strmodificarEscalaCampoIngresoXIngPacienteOdon = "UPDATE escalas_campos_ingresos SET "
			+ "valor = ?, "
			+ "observaciones = ? "
			+ "WHERE  escala_ingreso = ? AND escala_campo_seccion = ?  ";

	/**
	 * Cadena Sql para modificar el valor de un campo de escala por Paciente
	 * Odontologia
	 */
	private static final String strmodificarEscalaCampoIngreso = "UPDATE escalas_campos_ingresos SET "
			+ "valor = ?, " + "observaciones = ? " + "WHERE  codigo_pk = ?  ";

	/**
	 * Cadena para insertar un campo de una escala x ingreso
	 */
	private static final String strInsertarEscalaCampoIngreso = "INSERT INTO escalas_campos_ingresos "
			+ "(codigo_pk,"
			+ "escala_campo_seccion,"
			+ "escala_ingreso,"
			+ "valor," + "observaciones) " + "VALUES " + "(?,?,?,?,?)";

	/**
	 * Cadena para relacionar el registro de la escala ingreso con el registro
	 * de la plantilla ingreso
	 */
	private static final String strInsertarPlantillaEscalaIngreso = "INSERT INTO plantillas_escala_ing "
			+ "(plantilla_ingreso,"
			+ "escala_ingreso,"
			+ "plantilla_escala,"
			+ "componente," + "escala) " + "values " + "(?,?,?,?,?)";

	/**
	 * Cadena para insertar los archivos adjuntos que se ingresaron en la escala
	 */
	private static final String strInsertarAdjuntosEscalaIngreso = "INSERT INTO escalas_adjuntos_ingreso "
			+ "(codigo_pk,"
			+ "escala_ingreso,"
			+ "nombre_archivo,"
			+ "nombre_original) " + "VALUES " + "(?,?,?,?)";

	/**
	 * Cadena para modificar los arhcivos adjuntos que se ingresaron en la
	 * escala
	 */
	private static final String strModificarAdjuntosEscalaIngreso = "UPDATE historiaclinica.escalas_adjuntos_ingreso SET nombre_archivo = ?, nombre_original = ? WHERE codigo_pk = ?";

	/**
	 * Cadena Sql para eliminar un archivo adjunto de la escala en la plantilla
	 * Ingreso Paciente Odontologia
	 */
	private static final String strEliminarAdjuntosEscalaIngresoXIngrPacienteOdonto = "DELETE historiaclinica.escalas_adjuntos_ingreso WHERE codigo_pk = ? ";

//	/**
//	 * Cadena Sql para consultar el codigo de la plantilla asociada a una
//	 * funcion parametrizable
//	 * 
//	 */
//	private static final String strConsultarCodigoPlantillaFunParametrizableGenerico = "SELECT  "
//			+ "pl.codigo_pk AS codigopk "
//			+ ""
//			+ ConstantesBD.separadorSplitComplejo
//			+ "_1 "
//			+ "FROM plantillas pl "
//			+ ""
//			+ ConstantesBD.separadorSplitComplejo
//			+ "_2 " + "WHERE 1=1 AND pl.fun_param = ? ";

	/**
	 * Cadena Sql para obtener el codigo de un componente
	 */
	private static final String strObtenerCodigoComponenteXtipoComp = "SELECT c.codigo AS codigo, tc.nombre AS nombre "
			+ "FROM historiaclinica.componentes c "
			+ "INNER  JOIN  historiaclinica.tipos_componente tc ON(tc.codigo = c.tipo_componente) "
			+ "WHERE c.tipo_componente = ? ";

	/**
	 * Cadena Sql para consultar el codigo de la platilla asociada a la
	 * Valoracion de un Paciente de Odontologia
	 */
	private static final String strConsultarCodigoPlantillaFucionParametrizableValoracion = "SELECT pl.codigo_pk  AS codigopk  "
			+ "FROM historiaclinica.plantillas_ingresos pling "
			+ "INNER JOIN plantillas pl ON (pl.codigo_pk = pling.plantilla) "
			+ "WHERE pling.codigo_paciente= ? AND pling.valoracion_odonto = ? AND pl.fun_param = ? ";

	/**
	 * Cadena Sql para consultar el codigo de la plantilla asociada a la
	 * Evolucion de un Paciente Odontologia
	 */
	private static final String strConsultarCodigoPlantillaFucionParametrizableEvolucion = "SELECT  pl.codigo_pk  AS codigopk "
			+ "FROM  historiaclinica.plantillas_evolucion ple  "
			+ "INNER JOIN historiaclinica.plantillas pl ON (pl.codigo_pk = ple.plantilla) "
			+ "WHERE  ple.evolucion_odonto = ? AND AND pl.fun_param = ? ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Campos Parametrizables ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Cadena que inserta informacion en la tabla Campos Parametrizables
	 * */
	private static final String strInsertarCampoParametrizable = "INSERT INTO "
			+ "campos_parametrizables ("
			+ "codigo_pk, "
			+ "codigo, "
			+ "nombre, "
			+ "etiqueta,"
			+ "tipo, "
			+ "tamanio, "
			+ "signo, "
			+ "unidad, "
			+ "valor_predeterminado,"
			+ "maximo, "
			+ "minimo, "
			+ "decimales, "
			+ "columnas_ocupadas, "
			+ "orden, "
			+ "unico_fila, "
			+ "requerido, "
			+ "formula, "
			+ "tipo_html, "
			+ "mostrar,"
			+ "mostrar_modificacion, "
			+ "institucion, "
			+ "usuario_modifica, "
			+ "fecha_modifica, "
			+ "hora_modifica,"
			+ "maneja_imagen,"
			+ "imagen_a_asociar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	/**
	 * Cadena para actualizar los campos parametrizables
	 * */
	private static final String strActualizarCamposParametrizable = "UPDATE campos_parametrizables SET "
			+ "codigo = ?, "
			+ "nombre = ?, "
			+ "etiqueta = ?, "
			+ "tipo = ?,  "
			+ "tamanio = ?,  "
			+ "signo = ?,  "
			+ "unidad = ?,  "
			+ "valor_predeterminado = ?, "
			+ "maximo = ?,  "
			+ "minimo = ?,  "
			+ "decimales = ?,  "
			+ "columnas_ocupadas = ?,  "
			+ "orden = ?,  "
			+ "unico_fila = ?,  "
			+ "requerido = ?,  "
			+ "formula = ?,  "
			+ "tipo_html = ?, "
			+ "mostrar = ?,  "
			+ "usuario_modifica = ?,  "
			+ "fecha_modifica = ?,  "
			+ "hora_modifica  = ?,"
			+ "maneja_imagen = ?, "
			+ "imagen_a_asociar = ? " + "WHERE codigo_pk = ? ";

	/**
	 * Cadena para actualizar el mostrar modificar
	 * */
	private static final String strActualizarMostrarModificacionCamposParam = "UPDATE campos_parametrizables SET "
			+ "mostrar_modificacion = ?,"
			+ "usuario_modifica = ?, "
			+ "fecha_modifica = ?, "
			+ "hora_modifica = ?  "
			+ "WHERE codigo_pk = ? ";

	/**
	 * Cadena que inserta informaciï¿½n en las opciones para el campo tipo
	 * select o check
	 * */
	private static final String StrInsertarOpciones = "INSERT INTO "
			+ "opciones_val_parametrizadas (" + "codigo_pk, "
			+ "campo_parametrizable," + "opcion, " + "valor, "
			+ "institucion, " + "usuario_modifica, " + "fecha_modifica, "
			+ "hora_modifica, "
			+ "convencion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Cadena para actualizar las opciones para el campo tipo select o check
	 * */
	private static final String strActualizarOpciones = "UPDATE opciones_val_parametrizadas SET "
			+ "opcion = ?, "
			+ "valor = ?,"
			+ "usuario_modifica = ?,"
			+ "fecha_modifica = ?, "
			+ "hora_modifica = ?,"
			+ "convencion = ? "
			+ "WHERE codigo_pk = ? ";

	/**
	 * Cadena para insertar en Plantillas Campos Seccion
	 * */
	private static final String strInsertarPlantillasCamposSec = "INSERT INTO plantillas_campos_sec (codigo_pk,plantilla_seccion,campo_parametrizable) VALUES (?,?,?) ";

	/**
	 * Elimina una opcion del campo
	 * */

	private static final String strEliminarOpcionCampoSec = "DELETE FROM opciones_val_parametrizadas WHERE codigo_pk = ? ";

	// **************************************************************************************************************
	// ********************** Metodos Para Insertar y Actualizar las detalles de
	// las Opciones de los Campos.*********

	/**
	 * Insertar Tabla det_secciones.
	 */
	private static final String strInsertarDetSecciones = " INSERT INTO "
			+ "det_secciones (" + "codigo_pk, " + "codigo_pk_opciones, "
			+ "codigo_pk_plan_seccion, " + "mostrar_modificacion, "
			+ "usuario_modifica, " + "fecha_modifica, "
			+ "hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";

	/*
	 * private static final String strInsertarDetSeccionesNuevos =
	 * "insert into " + "det_secciones (" + "codigo_pk, " +
	 * "codigo_pk_opciones, " + "codigo_pk_plan_seccion, " +
	 * "mostrar_modificacion, " + "usuario_modifica, " + "fecha_modifica, " +
	 * "hora_modifica) " +
	 * "(SELECT 'seq_det_secciones'), codigo_pk_opciones,?,?,?,?,? " +
	 * "FROM det_secciones " + "WHERE codigo_pk_plan_seccion = ?)";
	 */

	/**
	 * Actualizar Tabla det_secciones.
	 */
	private static final String strActualizarDetSecciones = "UPDATE det_secciones SET "
			+ "mostrar_modificacion=?, "
			+ "usuario_modifica=?, "
			+ "fecha_modifica=?, " + "hora_modifica=? " + "WHERE codigo_pk=?";

	/**
	 * 
	 * */
	private static final String strDesasociarSeccion = "UPDATE det_secciones SET "
			+ "mostrar_modificacion=?, "
			+ "usuario_modifica=?, "
			+ "fecha_modifica=?, "
			+ "hora_modifica=? "
			+ "WHERE  codigo_pk_plan_seccion = ? ";

	/**
	 * Insertar Tabla det_valores.
	 */
	private static final String strInsertarDetValores = " INSERT INTO "
			+ "det_valores (" + "codigo_pk, " + "codigo_pk_opciones, "
			+ "valor, " + "mostrar_modificacion, " + "usuario_modifica, "
			+ "fecha_modifica, "
			+ "hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Actualizar tabla det_valores.
	 */
	private static final String strActualizarDetValores = "UPDATE det_valores SET "
			+ "mostrar_modificacion=?, "
			+ "usuario_modifica=?, "
			+ "fecha_modifica=?, " + "hora_modifica=? " + "WHERE codigo_pk=?";

	// ******************** Fin Metodos detalle de opciones de
	// Campos********************************
	// **********************************************************************************************

	/**
	 * Mï¿½todo para obtener las escalas del ingreso
	 */
	private static final String strObtenerEscalasXIngreso = "SELECT "
			+ "ei.codigo_pk AS consecutivo, "
			+ "e.codigo_pk AS codigo_pk, "
			+ "e.codigo AS codigo_escala, "
			+ "e.nombre AS nombre_escala, "
			+ "to_char(ei.fecha,'"
			+ ConstantesBD.formatoFechaAp
			+ "') AS fecha, "
			+ "ei.hora AS hora, "
			+ "ei.valor_total AS valor_total, "
			+ "getnombreusuario2(ei.usuario_modifica) AS nombre_usuario, "
			+ "coalesce(ef.nombre,'') AS nombre_factor_prediccion, "
			+ "coalesce(ei.observaciones,'') AS observaciones "
			+ "FROM escalas_ingresos ei "
			+ "INNER JOIN escalas e ON(e.codigo_pk=ei.escala) "
			+ "LEFT OUTER JOIN escalas_factores_pred ef ON(ef.codigo_pk=ei.escala_factor_prediccion) "
			+ "WHERE " + "ei.ingreso = ? ";

	/**
	 * Cadenq para obtener las secciones de la escala x ingreso
	 */
	private static final String strObtenerSeccionesEscalaXIngreso = "SELECT DISTINCT "
			+ "es.codigo_pk AS consecutivo_seccion, "
			+ "es.nombre AS nombre_seccion "
			+ "FROM escalas_campos_ingresos eci "
			+ "INNER JOIN escalas_campos_seccion ecs ON(ecs.codigo_pk=eci.escala_campo_seccion) "
			+ "INNER JOIN escalas_secciones es ON(es.codigo_pk=ecs.escala_seccion) "
			+ "WHERE eci.escala_ingreso = ? ORDER BY es.nombre";

	/**
	 * Cadena para obtener los campos de la seccion de una escala x ingreso
	 */
	private static final String strObtenerCamposEscalaXIngreso = "SELECT DISTINCT "
			+ "cp.codigo AS codigo_campo, "
			+ "cp.nombre AS nombre_campo, "
			+ "eci.valor AS valor, "
			+ "coalesce(eci.observaciones,'') AS observaciones "
			+ "FROM escalas_campos_ingresos eci "
			+ "INNER JOIN escalas_campos_seccion ecs ON(ecs.codigo_pk=eci.escala_campo_seccion) "
			+ "INNER JOIN campos_parametrizables cp ON(cp.codigo_pk=ecs.campo_parametrizable) "
			+ "WHERE " + "eci.escala_ingreso = ? AND ecs.escala_seccion = ?";

	/**
	 * 
	 */
	private static final String consultaCamposExistentesStr = "SELECT "
			+ "cp.codigo as codigo, "
			+ "cp.nombre as nombre, "
			+ "cp.mostrar_modificacion as mostrarmodificacion "
			+ "FROM plantillas p "
			+ "INNER JOIN plantillas_sec_fijas psf on(psf.plantilla=p.codigo_pk) "
			+ "INNER JOIN plantillas_secciones ps on(ps.plantilla_sec_fija=psf.codigo_pk) "
			+ "INNER JOIN plantillas_campos_sec pcs on(pcs.plantilla_seccion=ps.codigo_pk) "
			+ "INNER JOIN campos_parametrizables cp on(cp.codigo_pk=pcs.campo_parametrizable) "
			+ "WHERE p.fun_param=? ";

	/**
	 * 
	 */
	private static final String consultaSeccionesExistentesStr = "SELECT "
			+ "sp.codigo as codigo, "
			+ "sp.descripcion as descripcion, "
			+ "sp.mostrar_modificacion as mostrarmodificacion "
			+ "FROM plantillas p "
			+ "INNER JOIN plantillas_sec_fijas psf on(psf.plantilla=p.codigo_pk) "
			+ "INNER JOIN plantillas_secciones ps on(ps.plantilla_sec_fija=psf.codigo_pk) "
			+ "INNER JOIN secciones_parametrizables sp on(sp.codigo_pk=ps.seccion) "
			+ "WHERE p.fun_param=?  ";

	/**
	 * 
	 */
	private static final String consultaEscalasComponentesStr = "SELECT "
			+ "ce.escala as escala, "
			+ "ce.componente as componente, "
			+ "ce.mostrar_modificacion as mostrarmodificacion "
			+ "FROM plantillas p "
			+ "INNER JOIN plantillas_sec_fijas psf on(psf.plantilla=p.codigo_pk) "
			+ "INNER JOIN plantillas_componentes pc on(pc.plantilla_sec_fija=psf.codigo_pk) "
			+ "INNER JOIN componentes_escalas ce on(ce.componente=pc.componente) "
			+ "where p.fun_param=? and ce.mostrar_modificacion='"
			+ ConstantesBD.acronimoSi + "' and pc.mostrar_modificacion='"
			+ ConstantesBD.acronimoSi + "' ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Secciones Parametrizables ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Inserta registro de parametrizacion de formulario servicio/diagnosticos
	 * */
	private static final String strInsertarFomularioRespServ = "INSERT INTO "
			+ "form_resp_serv (codigo_pk,institucion,plantilla,servicio,diag_proc_form) "
			+ "VALUES (?,?,?,?,?)";

	/**
	 * Elimina registro de parametrizacion de formulario servicio/diagnosticos
	 * */
	private static final String strEliminarFomularioRespServ = "DELETE FROM "
			+ "form_resp_serv WHERE codigo_pk = ? ";

	/**
	 * Modifica registro de parametrizacion de formulario servicio/diagnosticos
	 * */
	private static final String strModificarFomularioRespServ = "UPDATE "
			+ "form_resp_serv SET " + "plantilla = ?," + "servicio = ?, "
			+ "diag_proc_form= ? " + "WHERE codigo_pk = ? ";

	/**
	 * Consulta parametrizacion de formulario servicio/diagnosticos
	 * */
	private static final String strConsultarFormulariosRespServ = "SELECT "
			+ "frs.codigo_pk,"
			+ "frs.plantilla,"
			+ "p.codigo_plantilla,"
			+ "p.nombre_plantilla,"
			+ "frs.servicio, "
			+ "CASE WHEN frs.servicio IS NULL THEN '' ELSE getnombreservicio(frs.servicio,"
			+ ConstantesBD.codigoTarifarioCups
			+ ") END AS descripcion_servicio, "
			+ "CASE WHEN frs.servicio IS NULL THEN "
			+ ConstantesBD.codigoNuncaValido
			+ " ELSE getcodigoespecialidad(frs.servicio)  END AS especialidad_servicio, "
			+ "CASE WHEN frs.servicio IS NULL THEN '"
			+ ConstantesBD.codigoNuncaValido
			+ "' ELSE getcodigoservicio(frs.servicio,?)    END AS codigo_propietario_servicio, "
			+ "frs.diag_proc_form, "
			+ "diag.descripcion AS descripcion_diag "
			+ "FROM "
			+ "form_resp_serv frs "
			+ "INNER JOIN plantillas p ON (p.codigo_pk = frs.plantilla) "
			+ "LEFT OUTER JOIN diag_procedimientos_form diag ON (diag.codigo_pk = frs.diag_proc_form) "
			+ "WHERE frs.institucion = ? ";

	/**
	 * Consula los diagnosticos disponibles para la parametrizacion
	 * servicio/diagnostico
	 * */
	private static final String strConsultarDiagProcFormulario = "SELECT "
			+ "codigo_pk AS codigo," + "descripcion "
			+ "FROM diag_procedimientos_form " + "WHERE institucion = ? "
			+ "ORDER BY descripcion ";
	// ----------------------------------------------------------------------------------------------------------------
	// ****************************************************************************************************************
	/**
	 * PLANTILLAS PACIENTE ODONTOLOGIA
	 */
	// *****************************************************************************************************************
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Cadena Sql para insertar un nuevo registro en la tabla
	 * plantillas_pacientes
	 */

	private static final String strInsertarPlantillPacientes = "INSERT INTO manejopaciente.plantillas_pacientes ( "
			+ "codigo_pk, "
			+ "codigo_paciente, "
			+ "plantilla, "
			+ "fecha_modificacion, "
			+ "hora_modificacion, "
			+ "usuario_modificacion ) "
			+ "VALUES (?, ?, ?, CURRENT_DATE, "
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?) ";

	/**
	 * Cadena Sql para modificar la plantilla asociada a un paciente odontologia
	 */
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

//	/**
//      * 
//      */
//	private static final String strModificarPlantillasComponPac = "UPDATE  manejopaciente.plantillas_comp_pac SET "
//			+ "plantilla_componente = ?,"
//			+ "componente_ingreso = ? "
//			+ "WHERE plantilla_paciente = ?";
//
//	/**
//	 * Cadena Sql para modificar la plantilla asociada a un paciente odontologia
//	 */
//	private static final String strModificarPlantillPacientes = "UPDATE manejopaciente.plantillas_pacientes SET "
//			+ "plantilla = ?, "
//			+ "fecha_modificacion = CURRENT_DATE , "
//			+ "hora_modificacion = "
//			+ ValoresPorDefecto.getSentenciaHoraActualBD()
//			+ ", "
//			+ "usuario_modificacion = ? " + "WHERE codigo_paciente ? ";

	/**
	 * Cadena Sql para insertar un nuevo registro en la tabla
	 * plantillas_pac_campos
	 */
	private static final String strInsertarPlantillasPacCampos = "INSERT INTO manejopaciente.plantillas_pac_campos ("
			+ "codigo_pk, "
			+ "plantilla_paciente, "
			+ "plantilla_campo_sec, "
			+ "nombre_archivo_original, "
			+ "fecha_modificacion, "
			+ "hora_modificacion, "
			+ "usuario_modificacion ) "
			+ "VALUES (? ,? , ?, ?, CURRENT_DATE, "
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + " , ?)";

	/**
	 * Cadena sql para modificar la tabla plantillas_pac_campos
	 */
	private static final String strModificarPlantillasPacCampos = "UPDATE manejopaciente.plantillas_pac_campos SET "
			+ "nombre_archivo_original = ? , "
			+ "fecha_modificacion = CURRENT_DATE , "
			+ "hora_modificacion = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ " , "
			+ "usuario_modificacion = ? "
			+ "WHERE codigo_pk = ? AND plantilla_paciente = ?";

	/**
	 * Cadena Sql para insertar un nuevo registro en la tabla
	 * valores_plan_pac_campo
	 */
	private static final String strInsertarValoresPlantPacCampos = "INSERT INTO manejopaciente.valores_plan_pac_campo ( "
			+ "codigo_pk, "
			+ "plantilla_pac_campo, "
			+ "valor, "
			+ "valores_opcion,"
			+ "imagen_opcion,"
			+ "seleccionado, "
			+ "convencion ) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Cadena Sql para modificar la tabla valores_plan_pac_campo
	 */
	private static final String strModificarValoresPlantPacCampos = "UPDATE manejopaciente.valores_plan_pac_campo SET "
			+ "valor =  ?, "
			+ "valores_opcion = ?, "
			+ "imagen_opcion = ?, "
			+ "seleccionado = ?, " + "convencion = ? " + "WHERE codigo_pk = ? ";

	private static final String strModificarValoresPlantPacCampos2 = "UPDATE manejopaciente.valores_plan_pac_campo SET "
			+ "valor =  ?, "
			+ "valores_opcion = ?,"
			+ "imagen_opcion = ?,"
			+ "seleccionado = ?, "
			+ "convencion = ? "
			+ "WHERE plantilla_pac_campo = ? ";

//	/**
//	 * Cadena sql para modificar la tabla plantillas_escala_pac
//	 */
//	private static final String strModificarPlantEscalaPacinte = "UPDATE manejopaciente.plantillas_escala_pac SET "
//			+ "escala_ingreso = ?,"
//			+ "plantilla_escala = ?, "
//			+ "componente = ? ,"
//			+ "escala = ? "
//			+ "WHERE plantilla_paciente = ? ";
//
//	/**
//	 * Cadena Sql para insertar un nuevo registro en la tabla
//	 * plantillas_comp_pac
//	 */
//	private static final String strInsertarPlantillasComponPac = "INSERT INTO manejopaciente.plantillas_comp_pac ( "
//			+ "plantilla_paciente, "
//			+ "plantilla_componente, "
//			+ "componente_ingreso ) " + "VALUES (?, ?, ? )";

	/**
	 * Cadena para verificar que no se vayan a insertar plantillas que tengan la
	 * misma fun_param y el mismo nombre
	 */
	private static final String consultarPlantillaConIgualNombreStr = "SELECT *FROM "
			+ "historiaclinica.plantillas "
			+ "WHERE "
			+ "fun_param=? AND nombre_plantilla=upper(?) ";

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Metodos Plantillas ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Consulta si existe una plantilla para la misma funcion parametrizable y
	 * con el mismo nombre
	 */
	public static boolean consultarPlantillaConIgualNombre(
			double plantillaBase, String nombre) {
		Connection con = UtilidadBD.abrirConexion();
		boolean transaccionExitosa = false;
		PreparedStatementDecorator ps =null;
		ResultSetDecorator rs = null;
		try {
			ps = new PreparedStatementDecorator(con, consultarPlantillaConIgualNombreStr);
			ps.setDouble(1, plantillaBase);
			ps.setString(2, nombre);
			rs = new ResultSetDecorator(ps.executeQuery());

			logger.info("la consulta de la plantilla------>" + ps);

			if (rs.next())
				transaccionExitosa = true;
			else
				transaccionExitosa = false;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return transaccionExitosa;
	}

	/**
	 * Inserta informacion de la Plantilla
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * @param String
	 *            strInsertarPlantillas
	 * */
	public static int insertarPlantilla(Connection con, HashMap parametros) {
		/**
		 * "codigo_pk," + "fun_param," + "centro_costo," + "sexo," +
		 * "especialidad," + "codigo_plantilla," + "nombre_plantilla," +
		 * "institucion," + "usuario_modifica," + "fecha_modifica," +
		 * "hora_modifica) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
		 * 
		 * ----------------------+------------------------+--------------------
		 * ------------ codigo_pk | numeric(12,0) | not null fun_param | integer
		 * | not null centro_costo | integer | sexo | integer | especialidad |
		 * integer | codigo_plantilla | numeric(12,0) | nombre_plantilla |
		 * character varying(128) | institucion | integer | not null
		 * usuario_modifica | character varying(30) | not null fecha_modifica |
		 * date | not null hora_modifica | character(5) | not null
		 * mostrar_modificacion | character varying(1) | default 'S'::character
		 * varying
		 */
		PreparedStatementDecorator ps =null;
		
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillas));

			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_plantillas");

			ps.setDouble(1, Utilidades.convertirADouble(codigo + ""));

			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("funParam")
					+ ""));

			if (parametros.containsKey("centroCosto")
					&& !parametros.get("centroCosto").equals(""))
				ps.setInt(3, Utilidades.convertirAEntero(parametros
						.get("centroCosto")
						+ ""));
			else
				ps.setNull(3, Types.INTEGER);

			if (parametros.containsKey("sexo")
					&& !parametros.get("sexo").equals(""))
				ps.setInt(4, Utilidades.convertirAEntero(parametros.get("sexo")
						+ ""));
			else
				ps.setNull(4, Types.INTEGER);

			if (parametros.containsKey("especialidad")
					&& !parametros.get("especialidad").equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(parametros
						.get("especialidad")
						+ ""));
			else
				ps.setNull(5, Types.INTEGER);

			if (parametros.containsKey("codigoPlantilla")
					&& !parametros.get("codigoPlantilla").equals(""))
				ps.setDouble(6, Utilidades.convertirADouble(parametros
						.get("codigoPlantilla")
						+ ""));
			else
				ps.setNull(6, Types.INTEGER);

			if (parametros.containsKey("nombrePlantilla")
					&& !parametros.get("nombrePlantilla").equals(""))
				ps.setString(7, parametros.get("nombrePlantilla").toString()
						.toUpperCase());
			else
				ps.setNull(7, Types.NULL);

			ps.setInt(8, Utilidades.convertirAEntero(parametros
					.get("institucion")
					+ ""));

			ps.setString(9, parametros.get("usuarioModifica") + "");
			ps.setDate(10, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(11, parametros.get("horaModifica") + "");

			if (parametros.containsKey("tipoAtencion")
					&& !parametros.get("tipoAtencion").toString().equals(""))
				ps.setString(12, parametros.get("tipoAtencion") + "");
			else
				ps.setNull(12, Types.NULL);

			logger.info("TIPO FUN--->" + parametros.get("tipoFuncionalidad"));

			if (parametros.containsKey("tipoFuncionalidad")
					&& !parametros.get("tipoFuncionalidad").toString().equals(
							""))
				ps.setString(13, parametros.get("tipoFuncionalidad") + "");
			else
				ps.setNull(13, Types.NULL);

			if (ps.executeUpdate() > 0) {
				return codigo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Actualiza la informacion de la plantilla
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * **/
	public static boolean actualizarPlantilla(Connection con, HashMap parametros) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con,strActualizarPlantillas);

			if (parametros.containsKey("centroCosto")
					&& !parametros.get("centroCosto").equals(""))
				ps.setInt(1, Utilidades.convertirAEntero(parametros
						.get("centroCosto")
						+ ""));
			else
				ps.setNull(1, Types.INTEGER);

			if (parametros.containsKey("sexo")
					&& !parametros.get("sexo").equals(""))
				ps.setInt(2, Utilidades.convertirAEntero(parametros.get("sexo")
						+ ""));
			else
				ps.setNull(2, Types.INTEGER);

			if (parametros.containsKey("especialidad")
					&& !parametros.get("especialidad").equals(""))
				ps.setInt(3, Utilidades.convertirAEntero(parametros
						.get("especialidad")
						+ ""));
			else
				ps.setNull(3, Types.INTEGER);

			if (parametros.containsKey("codigoPlantilla")
					&& !parametros.get("codigoPlantilla").equals(""))
				ps.setDouble(4, Utilidades.convertirADouble(parametros
						.get("codigoPlantilla")
						+ ""));
			else
				ps.setNull(4, Types.INTEGER);

			if (parametros.containsKey("nombrePlantilla")
					&& !parametros.get("nombrePlantilla").equals(""))
				ps.setString(5, parametros.get("nombrePlantilla").toString()
						.toUpperCase());
			else
				ps.setNull(5, Types.NULL);

			ps.setString(6, parametros.get("usuarioModifica") + "");
			ps.setDate(7, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(8, parametros.get("horaModifica") + "");

			if (parametros.containsKey("mostrarModificacion")
					&& !parametros.get("mostrarModificacion").equals("")
					&& (parametros.get("mostrarModificacion").equals(
							ConstantesBD.acronimoSi) || parametros.get(
							"mostrarModificacion").equals(
							ConstantesBD.acronimoNo)))
				ps.setString(9, parametros.get("mostrarModificacion")
						.toString());
			else
				ps.setString(9, ConstantesBD.acronimoSi);

			if (parametros.containsKey("tipoAtencion")
					&& !parametros.get("tipoAtencion").toString().equals(""))
				ps.setString(10, parametros.get("tipoAtencion") + "");
			else
				ps.setNull(10, Types.NULL);

			if (parametros.containsKey("tipoFuncionalidad")
					&& !parametros.get("tipoFuncionalidad").toString().equals(
							""))
				ps.setString(11, parametros.get("tipoFuncionalidad") + "");
			else
				ps.setNull(11, Types.NULL);

			ps.setDouble(12, Utilidades.convertirADouble(parametros
					.get("codigoPk")
					+ ""));
			logger.info("\n\nquery actualizacion::: " + ps);
			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return false;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Metodo que consulta la informaciï¿½n de la tabla plantillas
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * @param boolean indicadorCargarServicios
	 * */
	public static HashMap consultarListadoPlantillas(Connection con,
			HashMap parametros, boolean indicadorCargarServicios) {
		String cadena = strConsultarPlantillas;
		HashMap mapa = new HashMap();
		HashMap tmp = new HashMap();
		String codigoServicios = "-1,";
		String codigoDiagnostico = "-1,";
		mapa.put("numRegistros", "0");

		// Validaciones para Filtros
		if (parametros.containsKey("codigoPk")
				&& !parametros.get("codigoPk").toString().equals(""))
			cadena += " AND codigo_pk = " + parametros.get("codigoPk");

		if (parametros.containsKey("codigoPlantilla")
				&& !parametros.get("codigoPlantilla").toString().equals(""))
			cadena += " AND codigo_plantilla = "
					+ parametros.get("codigoPlantilla");

		if (parametros.containsKey("funParam")
				&& !parametros.get("funParam").toString().equals(""))
			cadena += " AND fun_param = " + parametros.get("funParam");

		if (parametros.containsKey("mostrarModificacion")
				&& !parametros.get("mostrarModificacion").toString().equals("")
				&& (parametros.get("mostrarModificacion").equals(
						ConstantesBD.acronimoSi) || parametros.get(
						"mostrarModificacion").equals(ConstantesBD.acronimoNo)))
			cadena += " AND mostrar_modificacion = '"
					+ parametros.get("mostrarModificacion").toString() + "' ";

		if (parametros.containsKey("tipoAtencion")
				&& !parametros.get("tipoAtencion").toString().equals(""))
			cadena += " AND tipo_atencion = '" + parametros.get("tipoAtencion")
					+ "'";

		if (parametros.containsKey("especialidad")
				&& !parametros.get("especialidad").toString().trim().equals(""))
			cadena += " AND especialidad = " + parametros.get("especialidad")
					+ "";

		cadena += " ORDER BY codigo_plantilla ASC ";

		logger.info("LA CADENA------>" + cadena);
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(parametros
					.get("institucion")
					+ ""));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()), true, true);

			// Evalua si debe cargar los servicios asociados a la plantilla
			if (indicadorCargarServicios) {
				int numRegistros = Utilidades.convertirAEntero(mapa.get(
						"numRegistros").toString());
				tmp.put("tarifario", parametros.get("tarifario"));
				tmp
						.put("institucion", parametros.get("institucion")
								.toString());

				for (int i = 0; i < numRegistros; i++) {
					tmp.put("codigoPkPlantilla", mapa.get("codigoPk_" + i));
					mapa.put("servicios_" + i, consultarPlantillasServDiag(con,
							tmp));

					// Recorre los servicios para capturar los codigos
					for (int j = 0; j < ((ArrayList) mapa.get("servicios_" + i))
							.size(); j++) {
						DtoPlantillaServDiag dto = ((DtoPlantillaServDiag) ((ArrayList) mapa
								.get("servicios_" + i)).get(j));

						if (dto.getCodigoServicio() > 0)
							codigoServicios += dto.getCodigoServicio() + ",";

						if (dto.getCodigoDiagnostico() > 0)
							codigoDiagnostico += dto.getCodigoDiagnostico()
									+ ",";
					}
				}

				mapa.put("codigosServiciosInsertados", codigoServicios);
				mapa.put("codigosDiagnosticosInsertados", codigoDiagnostico);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return mapa;
	}

	// ************************************************************************
	// ************************************************************************

	/**
	 * Método implementado para cargar DTO de Plantilla
	 */
	public static DtoPlantilla cargarPlantilla(Connection con, HashMap campos) {
		DtoPlantilla plantilla = new DtoPlantilla();
		/*PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;*/
		/*PreparedStatementDecorator pst1 = null;
		ResultSetDecorator rs2 = null;*/
		try {

			// ********************SE TOMAN LOS PARÁMETROS*****************************************************
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			institucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoFuncionalidad = Utilidades.convertirAEntero(campos.get("codigoFuncionalidad").toString());
			int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
			int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			int codigoPK = Utilidades.convertirAEntero(campos.get("codigoPK").toString());
			String tipoAtencion = campos.get("tipoAtencion").toString();
			String nombre = campos.get("nombrePlantilla").toString();
			String tipoFuncionalidad = campos.get("tipoFuncionalidad").toString();
			//logger.info("TIPO FUNCIONALIDAD >>"+ campos.get("tipoFuncionalidad").toString());

			// Vairable que indica si se debe consultar lo que se halla
			// registrado de cada campo
			boolean consultarRegistro = UtilidadTexto.getBoolean(campos.get("consultarRegistro").toString());
			int codigoIngreso = Utilidades.convertirAEntero(campos.get("codigoIngreso").toString());
			int numeroSolicitud = Utilidades.convertirAEntero(campos.get("numeroSolicitud").toString());
			int codigoValoracionOdo = Utilidades.convertirAEntero(campos.get("codigoValoracionOdo")+"");
			int codigoPaciente = Utilidades.convertirAEntero(campos.get("codigoPaciente").toString());
			int codigoEvolucion = Utilidades.convertirAEntero(campos.get("codigoEvolucion").toString());

			int codigoEvolucionOdo = Utilidades.convertirAEntero(campos.get("codigoEvolucionOdo")+ "");

			int codigoRespuestaProcedimiento = Utilidades.convertirAEntero(campos.get("codigoRespuestaProcedimiento").toString());

			// Se agrega esta validacion para la valoracion
			boolean tomarRegistrosVacios = UtilidadTexto.getBoolean(campos.get("tomarRegistrosVacios"));

			// Parámetros de filtros de busqueda
			int codigoSexoPaciente = Utilidades.convertirAEntero(campos.get("codigoSexoPaciente").toString());
			int diasEdadPaciente = Utilidades.convertirAEntero(campos.get("diasEdadPaciente").toString());
			boolean filtroDatosPaciente = UtilidadTexto.getBoolean(campos.get("filtroDatosPaciente").toString());
			// ********************************************************************************************

			// logger.info("VOY A CONSULTAR EL ENCABEZADO DE LA PLANTILLA***********************************");
			// **************SE REALIZA LA CONSULTA DEL ENCABEZADO DE LA
			// PLANTILLA*************************
			String consulta = strCargarEncabezadoPlantilla;
			String seccionWHERE = "";

			if (codigoCentroCosto >= 0) {
				if (codigoCentroCosto == ConstantesBD.codigoCentroCostoTodos)
				{
					seccionWHERE += " AND centro_costo IS NULL ";
				}
				else
				{
					seccionWHERE += " AND centro_costo = " + codigoCentroCosto;
				}
			}

			if (codigoSexo >= 0) {
				if (codigoSexo == ConstantesBD.codigoSexoTodos)
				{
					seccionWHERE += " AND sexo IS NULL ";
				}
				else
				{
					seccionWHERE += " AND sexo = " + codigoSexo;
				}
			}

			if (codigoEspecialidad >= 0)
			{
				seccionWHERE += " AND especialidad = " + codigoEspecialidad;
			}

			// Si no se consulta la plantilla por el codigo especifico..
			if (codigoPK >= 0)
			{
				seccionWHERE += " AND codigo_pk = " + codigoPK;
			}
			// se verifica si la consulta es por parametrizacion o por captura
			// para permitir el filtro por funcionalidad
			else if (!consultarRegistro)
			{
				seccionWHERE += " AND fun_param = " + codigoFuncionalidad;
			}

			// se verifica si la consulta es por parametrizacion o por captura
			// para permitir el filtro de Mostrar Modicacion

			// logger.info("valor de consulta registro >> "+consultarRegistro);

			if (!consultarRegistro)
			{	
				seccionWHERE += " AND mostrar_modificacion = '"+ ConstantesBD.acronimoSi + "' ";
			}

			if (!tipoAtencion.equals(""))
			{
				seccionWHERE += "AND tipo_atencion='" + tipoAtencion + "'";
			}

			if (!nombre.trim().equals(""))
				seccionWHERE += " AND nombre_plantilla='"+ nombre.toUpperCase() + "'";

			if (!tipoFuncionalidad.equals(""))
			{
				seccionWHERE += " AND tipo_funcionalidad='" + tipoFuncionalidad+ "'";
			}

			// logger.info("cadena para la busqueda de plantillas >> "+consulta+seccionWHERE);

			// Si se encontro algun filtro por el cual realizar la consulta de
			// la plantilla se realiza
			if (!seccionWHERE.equals("")) {
				consulta += seccionWHERE;

				PreparedStatementDecorator pst = new PreparedStatementDecorator(con, consulta);
				pst.setInt(1, codigoInstitucion);
				logger.info("LA CONSULTA DE PLANTILLAS----->" + pst);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

				// logger.info("consulta de la plantilla=>"+consulta+", codigoFuncionalidad:"+codigoFuncionalidad+", codigoInstitucion:"+codigoInstitucion);

				if (rs.next()) {
					plantilla.setCodigoPK(rs.getString("codigo_pk"));
					plantilla.setCodigoFuncionalidad(rs
							.getInt("codigo_funcionalidad"));
					plantilla.setCodigoCentroCosto(rs
							.getInt("codigo_centro_costo"));
					plantilla.setNombreCentroCosto(rs
							.getString("nombre_centro_costo"));
					plantilla.setCodigoSexo(rs.getInt("codigo_sexo"));
					plantilla.setNombreSexo(rs.getString("nombre_sexo"));
					plantilla.setCodigoEspecialidad(rs
							.getInt("codigo_especialidad"));
					plantilla.setNombreEspecialidad(rs
							.getString("nombre_especialidad"));
					plantilla.setCodigoInstitucion(rs
							.getInt("codigo_institucion"));
					plantilla.setCodigo(rs.getString("codigo")); // solo aplica
																	// para
																	// procedimientos
					plantilla.setNombre(rs.getString("nombre")); // solo aplica
																	// para
																	// procedimientos
					plantilla.getFuncionalidad().setDescripcion(
							rs.getString("tipoFuncionalidad"));
					plantilla.setTipoAtencion(rs.getString("tipo_atencion"));
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				
			}

			Log4JManager.info("VOY A CONSULTAR LAS SECCIONES FIJAS DE LA PLANTILLA***********************************");
			// Si no se encontro plantilla se consultan las secciones fijas directamente
			if (plantilla.getCodigoPK().equals("")) {
				plantilla.setCodigoFuncionalidad(codigoFuncionalidad);
				String cadenaConsulta = strCargarSeccionesFijas;

				if (!tipoFuncionalidad.equals("")
						&& !tipoFuncionalidad.equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)) {
					cadenaConsulta = cadenaConsulta
							.replace(
									"AND 1=1",
									" AND fp.seccion_fija <> "+ ConstantesCamposParametrizables.seccionFijaConvenios +
									" ");
				}

				// ***********SE CARGAN LAS SECCIONES
				// FIJAS******************************************+
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con, cadenaConsulta);
				pst.setInt(1, codigoFuncionalidad);
				pst.setInt(2, codigoInstitucion);
				logger.info("paso por consulta de seccion fijas: " + pst);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

				while (rs.next()) {
					DtoSeccionFija seccionFija = new DtoSeccionFija();

					seccionFija.setCodigoPK(rs.getString("codigo_pk"));
					seccionFija.setCodigoPkFunParamSecFij(rs
							.getString("codigo_pk_fun_param_sec_fij"));
					seccionFija.setCodigoSeccion(rs.getInt("codigo_seccion"));
					seccionFija
							.setNombreSeccion(rs.getString("nombre_seccion"));
					seccionFija.setOrden(rs.getInt("orden"));
					seccionFija.setVisible(UtilidadTexto.getBoolean(rs
							.getString("mostrar")));

					// logger.info("* SECCION "+seccionFija.getNombreSeccion()+": "+rs.getString("mostrar_modificacion"));
					// Se aï¿½ade la seccion a la plantilla si estï¿½ visible
					if (UtilidadTexto.getBoolean(rs
							.getString("mostrar_modificacion"))) {
						plantilla.getSeccionesFijas().add(seccionFija);
					}
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				// logger.info("NUMERO DE SECCIONES FIJAS=> "+plantilla.getSeccionesFijas().size());
			}
			// Se pregunta si se encontró plantilla
			else {

				// Declaración de arreglo donde se almacenarán las secciones que
				// se cargarán dinámicamente
				ArrayList<DtoElementoParam> seccionesValores = new ArrayList<DtoElementoParam>();

				// logger.info("\n\n");
				// logger.info("-----------------------------------------------------");
				// logger.info("VOY A CONSULTAR LAS SECCIONES FIJAS PARAMETTRIZABLES DE LA PLANTILLA***********************************");

				// logger.info("-----------------------------------------------------");
				// logger.info("\n\n");

				// ***********SE CARGAN LAS SECCIONES
				// FIJAS******************************************+
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con, strCargarSeccionesFijasPlantilla);


				pst.setInt(1, Utilidades.convertirAEntero(plantilla.getCodigoPK()));
				pst.setInt(2, plantilla.getCodigoFuncionalidad());
				pst.setInt(3, plantilla.getCodigoInstitucion());
				pst.setInt(4, Utilidades.convertirAEntero(plantilla.getCodigoPK()));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

				logger.info("---->" + pst);

				/*
				 * logger.info("\n\n");logger.info(
				 * "-----------------------------------------------------");
				 * logger
				 * .info("consultaSeccionFija=>"+strCargarSeccionesFijasPlantilla
				 * +", codigoPlantilla=>"+plantilla.getCodigoPK()+
				 * ", codigoFuncionalidad=>"
				 * +plantilla.getCodigoFuncionalidad()+", codigoInstitucion=>"
				 * +plantilla.getCodigoInstitucion());logger.info(
				 * "-----------------------------------------------------");
				 * logger.info("\n\n");
				 */
				while (rs.next()) {
					DtoSeccionFija seccionFija = new DtoSeccionFija();
					
					/*
						 En esta condición se puede evitar colocar plantillas en la parametrizacion de plantillas
						 de información de paciente odontológico. Por modificaciones del anexo 860 se modifica la 
						 condicion de excluir la sección de convenios de la parametrica de asignación de datos básicos.
						 Se cambia y se coloca que la condición siempre sea true para que cargue e ingrese todas
						 las plantillas consultadas de la base de datos contenidas en las siguientes tablas: 
						 	valores_plan_ing_camp	plantillas_ing_campos	plantillas_ingresos
							det_secciones			plantillas_campos_sec	plantillas_secciones
							plantillas_sec_fijas	plantillas_ing_campos	plantillas 
						
						Por: Cristhian Murillo.
					 */
					
					/*
					if ( 
						(rs.getInt("codigo_seccion") == ConstantesCamposParametrizables.seccionFijaConvenios
							&& codigoFuncionalidad == ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico 
							&& tipoFuncionalidad.equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar))
						|| (rs.getInt("codigo_seccion") != ConstantesCamposParametrizables.seccionFijaConvenios)) {
					*/
					if(true){
						
						seccionFija.setCodigoPK(rs.getString("codigo_pk"));
						seccionFija.setEsFija(UtilidadTexto.getBoolean(rs
								.getString("es_fija")));
						if (seccionFija.isEsFija()) {
							seccionFija.setCodigoPkFunParamSecFij(rs
									.getString("codigo_pk_fun_param_sec_fij"));
						} else {
							seccionFija.setCodigoSeccionParam(rs
									.getString("codigo_pk_fun_param_sec_fij"));
						}

						seccionFija.setCodigoSeccion(rs.getInt("codigo_seccion"));
						seccionFija.setNombreSeccion(rs.getString("nombre_seccion"));
						seccionFija.setOrden(rs.getInt("orden"));
						seccionFija.setVisible(UtilidadTexto.getBoolean(rs.getString("mostrar")));
						seccionFija.setMostrarModificacion(UtilidadTexto.getBoolean(rs.getString("mostrar_modificacion")));

						// Se verifica si la sección fija tenía parametrización
						// asociada
						if (!seccionFija.getCodigoPK().equals("")) {
							// logger.info("VOY A CONSULTAR LOS ELEMENTOS DE LAS SECCIONES FIJAS DE LA PLANTILLA***********************************");
							// ******************SE CARGAN LOS ELEMENTOS DE LA
							// SECCION FIJA************************
							// Se verifica si las secciones se deben filtrar de
							// acuerdo a los datos del paciente
							PreparedStatementDecorator pst1 = null;
							if (filtroDatosPaciente) {
								pst1 = new PreparedStatementDecorator(con,
										strCargarElementosSeccionFijaPlantillaConFiltro);
								pst1.setInt(1, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
								pst1.setInt(2, codigoSexoPaciente);
								pst1.setInt(3, diasEdadPaciente);
								pst1.setInt(4, diasEdadPaciente);
								pst1.setInt(5, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
								pst1.setInt(6, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
								
							} else {
								pst1 = new PreparedStatementDecorator(con,strCargarElementosSeccionFijaPlantilla);
								pst1.setInt(1, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
								pst1.setInt(2, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
								pst1.setInt(3, Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
							}

							Log4JManager.info("cargar valores "+pst1);
							ResultSetDecorator rs2 = new ResultSetDecorator(pst1.executeQuery());

							while (rs2.next()) {
								int tipoElemento = rs2.getInt("tipo_elemento");
								DtoElementoParam elemento = null;
								if (tipoElemento == tipoElementoSeccion) {
									elemento = new DtoSeccionParametrizable();
								} else if (tipoElemento == tipoElementoComponente) {
									elemento = new DtoComponente();
								} else {
									elemento = new DtoEscala();
								}
								elemento.setConsecutivoParametrizacion(rs2.getString("consecutivo"));
								elemento.setOrden(rs2.getInt("orden"));
								// primero se verifica el campo mostrar
								elemento.setVisible(UtilidadTexto.getBoolean(rs2.getString("mostrar")));
								elemento.setMostrarModificacion(UtilidadTexto.getBoolean(rs2.getString("mostrar_modificacion")));
								elemento.setCodigoPK(rs2.getString("codigo_pk"));
								elemento.setCodigo(rs2.getString("codigo"));
								elemento.setDescripcion(rs2.getString("descripcion"));
								elemento.setCodigoInstitucion(plantilla.getCodigoInstitucion());

								switch (tipoElemento) {
								// SECCION
								case tipoElementoSeccion:
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									seccion.setColumnasSeccion(rs2
											.getInt("columnas_seccion"));
									seccion.setTipoSeccion(rs2
											.getString("tipo"));
									seccion.setCodigoSeccionPadre(rs2
											.getString("seccion_padre"));
									if (rs2.getString("sexo").equals(null)
											|| rs2
													.getString("sexo")
													.equals(
															ConstantesBD.codigoNuncaValido
																	+ "")) {
										seccion.setSexoSeccion("");
									} else {
										seccion.setSexoSeccion(rs2
												.getString("sexo"));
									}
									if (rs2.getString("edad_inicial_dias")
											.equals(null)
											|| rs2
													.getString(
															"edad_inicial_dias")
													.equals(
															ConstantesBD.codigoNuncaValido
																	+ "")) {
										seccion.setRangoInicialEdad("");
									} else {
										seccion
												.setRangoInicialEdad(rs2
														.getString("edad_inicial_dias"));
									}
									if (rs2.getString("edad_final_dias")
											.equals(null)
											|| rs2
													.getString(
															"edad_final_dias")
													.equals(
															ConstantesBD.codigoNuncaValido
																	+ "")) {
										seccion.setRangoFInalEdad("");
									} else {
										seccion.setRangoFInalEdad(rs2
												.getString("edad_final_dias"));
									}
									seccion
											.setIndicativoRestriccionValCamp(rs2
													.getString("restriccion_val_campo"));

									seccion.setSeccion(true);

									// logger.info("VOY A CONSULTAR SECCIONES***********************************");

									seccion = cargarSeccionPlantilla(con,
											seccion, consultarRegistro,
											codigoIngreso, numeroSolicitud,
											codigoPaciente, codigoEvolucion,
											codigoRespuestaProcedimiento,
											codigoEvolucionOdo,
											codigoValoracionOdo, plantilla
													.getCodigoPK(), seccionFija
													.getCodigoPK(),
											codigoSexoPaciente,
											diasEdadPaciente,
											filtroDatosPaciente,
											tomarRegistrosVacios);

									// Se verifica que la seccion sea visible
									// para saber si se aï¿½ade a la seccion
									// fija
									if (seccion.isMostrarModificacion()) {
										seccionFija.getElementos().add(seccion);
									}
									break;

								// COMPONENTE
								case tipoElementoComponente:
									DtoComponente componente = (DtoComponente) elemento;
									componente.setCodigoTipo(Utilidades
											.convertirAEntero(componente
													.getCodigo()));
									componente.setNombreTipo(componente
											.getDescripcion());
									componente.setComponente(true);

									logger.info("\n\n");
									logger
											.info("-----------------------------------------------------");
									logger
											.info("Componente antes de verificar >> codigo >>"
													+ componente
															.getConsecutivoParametrizacion()
													+ " >> nombre >> "
													+ componente
															.getDescripcion()
													+ " >> mostrar_modificacion >> "
													+ componente
															.isMostrarModificacion()
													+ " >> mostrar >> "
													+ componente.isVisible()
													+ " >> tipo >> "
													+ componente
															.getCodigoTipo());
									logger
											.info("-----------------------------------------------------");
									logger.info("\n\n");

									logger.info("VOY A CONSULTAR COMPONENTE**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************");

									logger.info("Esto es el componente "+codigoValoracionOdo);
									
									componente = cargarComponente(con,
											componente, consultarRegistro,
											codigoIngreso, numeroSolicitud,
											codigoPaciente, codigoEvolucion,
											codigoRespuestaProcedimiento,
											plantilla.getCodigoPK(),
											codigoSexoPaciente,
											diasEdadPaciente,
											filtroDatosPaciente,
											codigoValoracionOdo,
											codigoEvolucionOdo,
											tomarRegistrosVacios);
									
									// Si el componente estï¿½ visible se
									// añade a la sección fija
									if (componente.isMostrarModificacion()) {
										/*
										 * logger.info("\n\n");logger.info(
										 * "-----------------------------------------------------"
										 * );logger.info(
										 * "Componente adicionado a la seccion fija >> codigo >>"
										 * +
										 * componente.getConsecutivoParametrizacion
										 * ()+" >> nombre >> "
										 * +componente.getDescripcion
										 * ()+" >> mostrar_modificacion >> "
										 * +componente.isMostrarModificacion()+
										 * " >> mostrar >> "
										 * +componente.isVisible());
										 * logger.info(
										 * "-----------------------------------------------------"
										 * ); logger.info("\n\n");
										 */

										seccionFija.getElementos().add(
												componente);
									}

									break;

								// ESCALA
								case tipoElementoEscala:
									DtoEscala escala = (DtoEscala) elemento;
									escala.setObservacionesRequeridas(UtilidadTexto.getBoolean(rs2.getString("observaciones_requeridas")));
									escala.setEscala(true);

									logger.info("VOY A CONSULTAR ESCALA DESDE SECCION FIJA "+ seccionFija.getNombreSeccion()+ "***********************************");
									escala = cargarEscala(con, escala,
											consultarRegistro, codigoIngreso,
											numeroSolicitud, codigoPaciente,
											codigoEvolucion,
											codigoRespuestaProcedimiento,
											plantilla.getCodigoPK(), false,
											false,
											codigoValoracionOdo,
											codigoEvolucionOdo,
											true);

									logger.info("valor mostrar modificacion >> "+ escala.isMostrarModificacion()+ " >>"+ escala.getCodigoPK());
									logger.info("valor visible >> "+ escala.isVisible() + " >>"+ escala.getCodigoPK());

									// Si la escala es mostrar modificación se
									// añade a la sección fija
									if (escala.isMostrarModificacion()) {
										seccionFija.getElementos().add(escala);
									}
									break;
								}

							}
							UtilidadBD.cerrarObjetosPersistencia(pst1, rs2, null);
							// ******************************FIN WHILE******************************************************
							

						}
					}

					// Siempre para mostrar histï¿½rico se deben mostrar todas
					// las secciones fijas o las secciones parametrizables que
					// tenga histï¿½ricos para mostrar
					if ( (consultarRegistro && seccionFija.isEsFija() && seccionFija.isVisible())
							|| (consultarRegistro && !seccionFija.isEsFija() && !seccionFija.isVisible() && seccionFija.getElementos().size() > 0)
						)
					{
						seccionFija.setVisible(true);
					}

					// Se añade la seccion a la plantilla si cuando no se va a
					// consultar informacion se encuente activo
					// o cuando se vaya a consultar informacion tenga elementos
					// o por el solo hecho de que sea fija
					if ((!consultarRegistro && seccionFija.isMostrarModificacion())
							|| (consultarRegistro && seccionFija.getElementos().size() > 0) || seccionFija.isEsFija())
						plantilla.getSeccionesFijas().add(seccionFija);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);

				// **********************************************************************************

				// *******SE VERIFICA SI LOS COMPONENTES PARAMETRIZABLES TIENEN
				// INFORMACION************+
				if (consultarRegistro)
					plantilla.verificarInformacionComponentesParametrizables();
				// *************************************************************************************

				// ******SE AÑADEN LAS SECCIONES VALORES A LA
				// PLANTILLA*******************************
				/**
				 * for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
				 * for(DtoElementoParam elemento:seccionFija.getElementos()) {
				 * //Para secciones que estï¿½n dentro de una seccion
				 * if(elemento.isSeccion()) { DtoSeccionParametrizable seccion =
				 * (DtoSeccionParametrizable)elemento;
				 * if(UtilidadTexto.getBoolean
				 * (seccion.getIndicativoRestriccionValCamp())) {
				 * DtoSeccionParametrizable seccionCopia = new
				 * DtoSeccionParametrizable(); seccionCopia = seccion;
				 * seccionesValores.add(seccionCopia); } } //Es posible que la
				 * seccion de valores se encuentre en un componente else
				 * if(elemento.isComponente()) { for(DtoElementoParam
				 * elementoComp:elemento.getElementos())
				 * if(elementoComp.isSeccion()) { DtoSeccionParametrizable
				 * seccion = (DtoSeccionParametrizable)elementoComp;
				 * if(UtilidadTexto
				 * .getBoolean(seccion.getIndicativoRestriccionValCamp())) {
				 * DtoSeccionParametrizable seccionCopia = new
				 * DtoSeccionParametrizable(); seccionCopia = seccion;
				 * seccionesValores.add(seccionCopia); } } } }
				 **/
				for (DtoSeccionFija seccionFija : plantilla.getSeccionesFijas())
					for (DtoElementoParam elemento : seccionFija.getElementos()) {
						// Para secciones que estï¿½n dentro de una seccion
						if (elemento.isSeccion()) {
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
							if (UtilidadTexto.getBoolean(seccion
									.getIndicativoRestriccionValCamp()))
								seccionesValores.add(seccion);
						}
						// Es posible que la seccion de valores se encuentre en
						// un componente
						else if (elemento.isComponente()) {
							for (DtoElementoParam elementoComp : elemento
									.getElementos())
								if (elementoComp.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elementoComp;
									if (UtilidadTexto.getBoolean(seccion
											.getIndicativoRestriccionValCamp()))
										seccionesValores.add(seccion);
								}
						}
					}
				plantilla.setSeccionesValor(seccionesValores);
				// **************************************************************************************
			}

		} catch (SQLException e) {
			logger.error("Error en cargarPlantilla: ", e);
		}finally {			
			/*if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
			if(rs2!=null){
				try{
					rs2.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}				
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}				
			if(pst1!=null){
				try{
					pst1.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}*/	
		}
		return plantilla;
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaExistentePaciente(Connection con,	HashMap campos) {

		DtoPlantilla plantilla = new DtoPlantilla();

		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		ResultSetDecorator rs2 = null;
		try {

			Utilidades.imprimirMapa(campos);

			// ********************SE TOMAN PARÁMETROS*****************************************************
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoFuncionalidad = Utilidades.convertirAEntero(campos.get("codigoFuncionalidad").toString());
			int codigoPK = Utilidades.convertirAEntero(campos.get("codigoPK").toString());
			String tipoFuncionalidad = campos.get("tipoFuncionalidad").toString();

			// Vairable que indica si se debe consultar lo que se haya
			// registrado de cada campo
			boolean consultarRegistro = UtilidadTexto.getBoolean(campos.get("consultarRegistro").toString());
			int codigoPaciente = Utilidades.convertirAEntero(campos.get("codigoPaciente").toString());
			boolean filtroDatosPaciente = UtilidadTexto.getBoolean(campos.get("filtroDatosPaciente").toString());
			// ********************************************************************************************

			
			// **************SE REALIZA LA CONSULTA DEL ENCABEZADO DE LA
			// PLANTILLA*************************
			String consulta = strCargarEncabezadoPlantilla;
			String seccionWHERE = "";

			// Si no se consulta la plantilla por el codigo especifico..
			if (codigoPK > 0)
				seccionWHERE += " AND codigo_pk = " + codigoPK;
			// se verifica si la consulta es por parametrizacion o por captura
			// para permitir el filtro por funcionalidad
			else if (!consultarRegistro)
				seccionWHERE += " AND fun_param = " + codigoFuncionalidad;

			// se verifica si la consulta es por parametrizacion o por captura
			// para permitir el filtro de Mostrar Modicacion

			if (!consultarRegistro)
				seccionWHERE += " AND mostrar_modificacion = '"+ ConstantesBD.acronimoSi + "' ";

			if (!tipoFuncionalidad.equals(""))
				seccionWHERE += " AND tipo_funcionalidad='" + tipoFuncionalidad+ "'";

			logger.info("cadena para la busqueda de plantillas >> " + consulta+ seccionWHERE);

			// Si se encontro algun filtro por el cual realizar la consulta de
			// la plantilla se realiza
			if (!seccionWHERE.equals("")) {
				consulta += seccionWHERE;

				pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1, codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());

				logger.info("consulta de la plantilla=>"+consulta+", codigoFuncionalidad:"+codigoFuncionalidad+", codigoInstitucion:"+codigoInstitucion);

				if (rs.next()) {
					plantilla.setCodigoPK(rs.getString("codigo_pk"));
					plantilla.setCodigoFuncionalidad(rs.getInt("codigo_funcionalidad"));
					plantilla.setCodigoCentroCosto(rs.getInt("codigo_centro_costo"));
					plantilla.setNombreCentroCosto(rs.getString("nombre_centro_costo"));
					plantilla.setCodigoSexo(rs.getInt("codigo_sexo"));
					plantilla.setNombreSexo(rs.getString("nombre_sexo"));
					plantilla.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
					plantilla.setNombreEspecialidad(rs.getString("nombre_especialidad"));
					plantilla.setCodigoInstitucion(rs.getInt("codigo_institucion"));
					plantilla.setCodigo(rs.getString("codigo")); // solo aplicapara procedimientos 
					plantilla.setNombre(rs.getString("nombre")); // solo aplica para procedimientos
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			}

			logger.info("VOY A CONSULTAR LAS SECCIONES FIJAS DE LA PLANTILLA***********************************");
			// Si no se encontro plantilla se consultan las secciones fijas directamente
			if (plantilla.getCodigoPK().equals("")) {
				plantilla.setCodigoFuncionalidad(codigoFuncionalidad);

				// ***********SE CARGAN LAS SECCIONES
				// FIJAS******************************************+
				logger.info("paso por consulta de seccion fijas: "
						+ strCargarSeccionesFijas + "\n\nplantilla=>"
						+ ConstantesBD.codigoNuncaValido
						+ "\ncodigoFuncionalidad=>" + codigoFuncionalidad
						+ ", institucion=>" + codigoInstitucion);
				
				pst = new PreparedStatementDecorator(con.prepareStatement(strCargarSeccionesFijas));
				pst.setInt(1, codigoFuncionalidad);
				pst.setInt(2, codigoInstitucion);
				
				// FIXME campo mostrar
				rs = new ResultSetDecorator(pst.executeQuery());

				while (rs.next()) {
					DtoSeccionFija seccionFija = new DtoSeccionFija();

					seccionFija.setCodigoPK(rs.getString("codigo_pk"));
					seccionFija.setCodigoPkFunParamSecFij(rs.getString("codigo_pk_fun_param_sec_fij"));
					seccionFija.setCodigoSeccion(rs.getInt("codigo_seccion"));
					seccionFija.setNombreSeccion(rs.getString("nombre_seccion"));
					seccionFija.setOrden(rs.getInt("orden"));
					seccionFija.setVisible(UtilidadTexto.getBoolean(rs.getString("mostrar")));

					// logger.info("* SECCION "+seccionFija.getNombreSeccion()+": "+rs.getString("mostrar_modificacion"));
					// Se aï¿½ade la seccion a la plantilla si estï¿½ visible
					if (UtilidadTexto.getBoolean(rs.getString("mostrar_modificacion")))
						plantilla.getSeccionesFijas().add(seccionFija);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				
				logger.info("NUMERO DE SECCIONES FIJAS=> "+plantilla.getSeccionesFijas().size());
			}
			
			// Se pregunta si se encontro plantilla
			else {
				// Declaracion de arreglo donde se almacenarian las secciones que se cargarian dinamicamente
				ArrayList<DtoElementoParam> seccionesValores = new ArrayList<DtoElementoParam>();

				logger.info("VOY A CONSULTAR LAS SECCIONES FIJAS PARAMETTRIZABLES DE LA PLANTILLA***********************************");


				// ***********SE CARGAN LAS SECCIONES
				// FIJAS******************************************+
				pst = new PreparedStatementDecorator(con.prepareStatement(strCargarSeccionesFijasPlantilla));

				logger.info("consulta: " 			+ strCargarSeccionesFijasPlantilla);
				logger.info("codigo pk: " 			+ plantilla.getCodigoPK());
				logger.info("cod funcionalidad: "	+ plantilla.getCodigoFuncionalidad());
				logger.info("cod institucion: "		+ plantilla.getCodigoInstitucion());

				pst.setInt(1, Utilidades.convertirAEntero(plantilla.getCodigoPK()));
				pst.setInt(2, plantilla.getCodigoFuncionalidad());
				pst.setInt(3, plantilla.getCodigoInstitucion());
				pst.setInt(4, Utilidades.convertirAEntero(plantilla.getCodigoPK()));

				rs = new ResultSetDecorator(pst.executeQuery());

				logger.info("consultaSeccionFija=>" 
						+ strCargarSeccionesFijasPlantilla
						+ ", codigoPlantilla=>" + plantilla.getCodigoPK()
						+ ", codigoFuncionalidad=>"
						+ plantilla.getCodigoFuncionalidad()
						+ ", codigoInstitucion=>"
						+ plantilla.getCodigoInstitucion());

				while (rs.next()) {
					DtoSeccionFija seccionFija = new DtoSeccionFija();

					seccionFija.setCodigoPK(rs.getString("codigo_pk"));
					seccionFija.setEsFija(UtilidadTexto.getBoolean(rs.getString("es_fija")));
					if (seccionFija.isEsFija())
						seccionFija.setCodigoPkFunParamSecFij(rs.getString("codigo_pk_fun_param_sec_fij"));
					else
						seccionFija.setCodigoSeccionParam(rs.getString("codigo_pk_fun_param_sec_fij"));

					seccionFija.setCodigoSeccion(rs.getInt("codigo_seccion"));
					seccionFija.setNombreSeccion(rs.getString("nombre_seccion"));
					seccionFija.setOrden(rs.getInt("orden"));
					seccionFija.setVisible(UtilidadTexto.getBoolean(rs.getString("mostrar")));
					seccionFija.setMostrarModificacion(UtilidadTexto.getBoolean(rs.getString("mostrar_modificacion")));

					// logger.info("--------------CODIGO SECCION PARAM-------------"+seccionFija.getCodigoSeccionParam());
					// logger.info("--------------CODIGO PK FUN PARAM-------------"+seccionFija.getCodigoPkFunParamSecFij());

					// Se verifica si la seccion fija tenia parametrizaciï¿½n asociada
					if (!seccionFija.getCodigoPK().equals("")) {

						// logger.info("VOY A CONSULTAR LOS ELEMENTOS DE LAS SECCIONES FIJAS DE LA PLANTILLA***********************************");
						// ******************SE CARGAN LOS ELEMENTOS DE LA
						// SECCION FIJA************************
						// Se verifica si las secciones se deben filtrar de
						// acuerdo a los datos del paciente
						if (filtroDatosPaciente) {
							pst = new PreparedStatementDecorator(con.prepareStatement(
																		strCargarElementosSeccionFijaPlantillaConFiltro));
							
							pst.setInt(1,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
							pst.setInt(2, ConstantesBD.codigoNuncaValido);
							pst.setInt(3, ConstantesBD.codigoNuncaValido);
							pst.setInt(4, ConstantesBD.codigoNuncaValido);
							pst.setInt(5,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
							pst.setInt(6,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));

							/*
							 * logger.info("strCargarElementosSeccionFijaPlantillaConFiltro: "
							 * +
							 * strCargarElementosSeccionFijaPlantillaConFiltro);
							 * logger
							 * .info("codigo seccion fija: "+seccionFija.getCodigoPK
							 * ()); logger.info("codigo sexo: "+codigoSexo);
							 * logger
							 * .info("diasEdadPaciente: "+diasEdadPaciente);
							 */
						} else {
							// logger.info("3333333333333 - sin filtros");
							pst = new PreparedStatementDecorator(con.prepareStatement(strCargarElementosSeccionFijaPlantilla));
							
							pst.setInt(1,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
							pst.setInt(2,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
							pst.setInt(3,Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
						}

						/*
						 * logger.info("\n\n");logger.info(
						 * "-----------------------------------------------------"
						 * );
						 * logger.info("elementos de la seccion fija "+seccionFija
						 * .getNombreSeccion()+" >> "+
						 * strCargarElementosSeccionFijaPlantilla
						 * .replace("?",seccionFija.getCodigoPK()));
						 * //logger.info
						 * ("-----------------------------------------------------"
						 * ); logger.info("\n\n");
						 */

						rs2 = new ResultSetDecorator(pst.executeQuery());
						
						while (rs2.next()) {
								int tipoElemento = rs2.getInt("tipo_elemento");
								DtoElementoParam elemento = null;
								if (tipoElemento == tipoElementoSeccion)
									elemento = new DtoSeccionParametrizable();
								else if (tipoElemento == tipoElementoComponente)
									elemento = new DtoComponente();
								else
									elemento = new DtoEscala();
									elemento.setConsecutivoParametrizacion(rs2.getString("consecutivo"));
									elemento.setOrden(rs2.getInt("orden"));
									// primero se verifica el campo mostrar
									elemento.setVisible(UtilidadTexto.getBoolean(rs2.getString("mostrar")));
									elemento.setMostrarModificacion(UtilidadTexto.getBoolean(rs2.getString("mostrar_modificacion")));
									elemento.setCodigoPK(rs2.getString("codigo_pk"));
									elemento.setCodigo(rs2.getString("codigo"));
									elemento.setDescripcion(rs2.getString("descripcion"));
									elemento.setCodigoInstitucion(plantilla.getCodigoInstitucion());
									// logger.info("mostrar modificacion inicial de la seccion  >> "+elemento.getDescripcion()+" >> mostrar modificacion >> "+elemento.isMostrarModificacion());
	
								switch (tipoElemento) {
								// SECCION
								case tipoElementoSeccion:
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									seccion.setColumnasSeccion(rs2
											.getInt("columnas_seccion"));
									seccion.setTipoSeccion(rs2.getString("tipo"));
									seccion.setCodigoSeccionPadre(rs2
											.getString("seccion_padre"));
									if (rs2.getString("sexo").equals(null)
											|| rs2.getString("sexo").equals(
													ConstantesBD.codigoNuncaValido
															+ ""))
										seccion.setSexoSeccion("");
									else
										seccion.setSexoSeccion(rs2
												.getString("sexo"));
									if (rs2.getString("edad_inicial_dias").equals(
											null)
											|| rs2
													.getString("edad_inicial_dias")
													.equals(
															ConstantesBD.codigoNuncaValido
																	+ ""))
										seccion.setRangoInicialEdad("");
									else
										seccion.setRangoInicialEdad(rs2
												.getString("edad_inicial_dias"));
									if (rs2.getString("edad_final_dias").equals(
											null)
											|| rs2
													.getString("edad_final_dias")
													.equals(
															ConstantesBD.codigoNuncaValido
																	+ ""))
										seccion.setRangoFInalEdad("");
									else
										seccion.setRangoFInalEdad(rs2
												.getString("edad_final_dias"));
									seccion.setIndicativoRestriccionValCamp(rs2
											.getString("restriccion_val_campo"));
	
									seccion.setSeccion(true);
	
									// logger.info("VOY A CONSULTAR SECCIONES***********************************");
	
									seccion = cargarSeccionPlantilla(con, seccion,
											consultarRegistro,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											codigoPaciente,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											plantilla.getCodigoPK(), seccionFija
													.getCodigoPK(),
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											filtroDatosPaciente, false); // tomar
																			// registros
																			// vacios
	
									// Se verifica que la seccion sea visible para
									// saber si se aï¿½ade a la seccion fija
									if (seccion.isMostrarModificacion())
										seccionFija.getElementos().add(seccion);
									break;
	
								// COMPONENTE
								case tipoElementoComponente:
									DtoComponente componente = (DtoComponente) elemento;
									componente.setCodigoTipo(Utilidades
											.convertirAEntero(componente
													.getCodigo()));
									componente.setNombreTipo(componente
											.getDescripcion());
									componente.setComponente(true);
	
									/*
									 * logger.info("\n\n");logger.info(
									 * "-----------------------------------------------------"
									 * );logger.info(
									 * "Componente antes de verificar >> codigo >>"
									 * +componente
									 * .getConsecutivoParametrizacion()+" >> nombre >> "
									 * +componente.getDescripcion()+
									 * " >> mostrar_modificacion >> "
									 * +componente.isMostrarModificacion
									 * ()+" >> mostrar >> "+componente.isVisible());
									 * logger.info(
									 * "-----------------------------------------------------"
									 * ); logger.info("\n\n");
									 * 
									 * logger.info(
									 * "VOY A CONSULTAR COMPONENTE***********************************"
									 * );
									 */
	
									componente = cargarComponente(con, componente,
											consultarRegistro,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											codigoPaciente,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											plantilla.getCodigoPK(),
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											filtroDatosPaciente,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido, false);
	
									// Si el componente estï¿½ visible se aï¿½ade a
									// la secciï¿½n fija
									if (componente.isMostrarModificacion()) {
										/*
										 * logger.info("\n\n");logger.info(
										 * "-----------------------------------------------------"
										 * );logger.info(
										 * "Componente adicionado a la seccion fija >> codigo >>"
										 * +
										 * componente.getConsecutivoParametrizacion(
										 * )+" >> nombre >> "
										 * +componente.getDescripcion
										 * ()+" >> mostrar_modificacion >> "
										 * +componente
										 * .isMostrarModificacion()+" >> mostrar >> "
										 * +componente.isVisible());logger.info(
										 * "-----------------------------------------------------"
										 * ); logger.info("\n\n");
										 */
	
										seccionFija.getElementos().add(componente);
									}
	
									break;
	
								// ESCALA
								case tipoElementoEscala:
									DtoEscala escala = (DtoEscala) elemento;
									escala.setObservacionesRequeridas(UtilidadTexto.getBoolean(rs2.getString("observaciones_requeridas")));
									escala.setEscala(true);
	
									// logger.info("VOY A CONSULTAR ESCALA DESDE SECCION FIJA "+seccionFija.getNombreSeccion()+"***********************************");
									escala = cargarEscala(con, escala,
											consultarRegistro,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											codigoPaciente,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											plantilla.getCodigoPK(), false, true,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido, true);
	
									// logger.info("valor mostrar modificacion >> "+escala.isMostrarModificacion()+" >>"+escala.getCodigoPK());
									// logger.info("valor visible >> "+escala.isVisible()+" >>"+escala.getCodigoPK());
	
									// Si la escala es mostrar modificacion se
									// aï¿½ade a la secciï¿½n fija
									if (escala.isMostrarModificacion())
										seccionFija.getElementos().add(escala);
									break;
								}
	
							}
							UtilidadBD.cerrarObjetosPersistencia(pst, rs2, null);
						// ************************************************************************************
					}
					//ARMANDO.

					// Siempre para mostrar histï¿½rico se deben mostrar todas las secciones fijas o las secciones 
					// parametrizables que tenga histï¿½ricos para mostrar
					if (consultarRegistro && seccionFija.isEsFija() || consultarRegistro && !seccionFija.isEsFija()
							&& seccionFija.getElementos().size() > 0){

						// Se documenta esta linea para mostrar solo las qué esten parametrizadas como visibles Anexo 860, Cambio 1.12 - Cristhian Murillo
						// seccionFija.setVisible(true);  
					}
						
					// Se aï¿½ade la seccion a la plantilla si cuando no se va a consultar informacion se encuente activo
					// o cuando se vaya a consultar informacion tenga elementos o por el solo hecho de que sea fija
					if ((!consultarRegistro && seccionFija.isMostrarModificacion()) || (consultarRegistro && seccionFija.getElementos().size() > 0) 
							|| seccionFija.isEsFija()){
						
						plantilla.getSeccionesFijas().add(seccionFija);
					}
						
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				// **********************************************************************************

				// *******SE VERIFICA SI LOS COMPONENTES PARAMETRIZABLES TIENEN INFORMACION************+
				if (consultarRegistro)
					plantilla.verificarInformacionComponentesParametrizables();
				// *************************************************************************************

				// ******SE Aï¿½ADEN LAS SECCIONES VALORES A LA PLANTILLA*******************************
				/**
				 * for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
				 * for(DtoElementoParam elemento:seccionFija.getElementos()) {
				 * //Para secciones que estï¿½n dentro de una seccion
				 * if(elemento.isSeccion()) { DtoSeccionParametrizable seccion =
				 * (DtoSeccionParametrizable)elemento;
				 * if(UtilidadTexto.getBoolean
				 * (seccion.getIndicativoRestriccionValCamp())) {
				 * DtoSeccionParametrizable seccionCopia = new
				 * DtoSeccionParametrizable(); seccionCopia = seccion;
				 * seccionesValores.add(seccionCopia); } } //Es posible que la
				 * seccion de valores se encuentre en un componente else
				 * if(elemento.isComponente()) { for(DtoElementoParam
				 * elementoComp:elemento.getElementos())
				 * if(elementoComp.isSeccion()) { DtoSeccionParametrizable
				 * seccion = (DtoSeccionParametrizable)elementoComp;
				 * if(UtilidadTexto
				 * .getBoolean(seccion.getIndicativoRestriccionValCamp())) {
				 * DtoSeccionParametrizable seccionCopia = new
				 * DtoSeccionParametrizable(); seccionCopia = seccion;
				 * seccionesValores.add(seccionCopia); } } } }
				 **/
				for (DtoSeccionFija seccionFija : plantilla.getSeccionesFijas())
					for (DtoElementoParam elemento : seccionFija.getElementos()) {
						// Para secciones que estï¿½n dentro de una seccion
						if (elemento.isSeccion()) {
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
							if (UtilidadTexto.getBoolean(seccion
									.getIndicativoRestriccionValCamp()))
								seccionesValores.add(seccion);
						}
						// Es posible que la seccion de valores se encuentre en
						// un componente
						else if (elemento.isComponente()) {
							for (DtoElementoParam elementoComp : elemento
									.getElementos())
								if (elementoComp.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elementoComp;
									if (UtilidadTexto.getBoolean(seccion
											.getIndicativoRestriccionValCamp()))
										seccionesValores.add(seccion);
								}
						}
					}
				plantilla.setSeccionesValor(seccionesValores);
				// **************************************************************************************
			}

		} catch (SQLException e) {
			logger.error("Error en cargarPlantillaExistentePaciente : " + e);
		}finally {			
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try{
				if (rs2 != null ) {
					rs2.close();
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
			}
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
		}
		return plantilla;
	}

	/**
	 * Mï¿½todo para cargar los datos del componente
	 * 
	 * @param con
	 * @param componente
	 * @param consultarRegistro
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoEvolucion
	 * @param codigoRespuestaProcedimiento
	 * @param codigoPlantilla
	 * @param filtroDatosPaciente
	 * @param diasEdadPaciente
	 * @param codigoSexoPaciente
	 * @return
	 */
	private static DtoComponente cargarComponente(Connection con,
			DtoComponente componente, boolean consultarRegistro,
			int codigoIngreso, int numeroSolicitud, int codigoPaciente,
			int codigoEvolucion, int codigoRespuestaProcedimiento,
			String codigoPlantilla, int codigoSexoPaciente,
			int diasEdadPaciente, boolean filtroDatosPaciente,
			int codValoracionOdonto, int codEvolucionOdonto,
			boolean tomarRegistrosVacios) {
		ResultSetDecorator rs =null;
		PreparedStatementDecorator pst = null;
		try {
			
			logger.info("Este es el codigo que llega "+codValoracionOdonto);
			/*
			 * Nota * Esta variable sirve para en el momento de desear
			 * consultarRegistro adicionalmente se asigne al DTO el codigo del
			 * consecutivo historico
			 */
			boolean preservarHistorial = true;

			// Se verifica si se debe buscar la informacion grabada de un
			// componente
			if (!consultarRegistro
					&& componente.getCodigoTipo() == ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos
					&& (codValoracionOdonto > 0 || codEvolucionOdonto > 0)) {
				consultarRegistro = true;
				preservarHistorial = false;
			}

			// *******************SE CARGAN LOS ELEMENTOS DEL
			// COMPONENTE******************************************
			if (filtroDatosPaciente) {
				pst = new PreparedStatementDecorator(con.prepareStatement(strCargarElementosComponenteConFiltro));
				pst.setInt(1, Utilidades.convertirAEntero(componente.getCodigoPK()));
				pst.setInt(2, codigoSexoPaciente);
				pst.setInt(3, diasEdadPaciente);
				pst.setInt(4, diasEdadPaciente);
				pst.setInt(5, Utilidades.convertirAEntero(componente
						.getCodigoPK()));
			} else {
				pst = new PreparedStatementDecorator(con.prepareStatement(strCargarElementosComponente));
				pst.setInt(1, Utilidades.convertirAEntero(componente
						.getCodigoPK()));
				pst.setInt(2, Utilidades.convertirAEntero(componente
						.getCodigoPK()));
			}
			// logger.info("\n>> CONSULTA Elementos Componentes >>"+
			// strCargarElementosComponente
			// +"  Codigo >>"+componente.getCodigoPK());
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				int tipoElemento = rs.getInt("tipo_elemento");
				DtoElementoParam elemento = null;
				if (tipoElemento == tipoElementoSeccion)
					elemento = new DtoSeccionParametrizable();
				else
					elemento = new DtoEscala();

				elemento.setVisible(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				elemento.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				elemento.setConsecutivoParametrizacion(rs
						.getString("consecutivo"));
				elemento.setCodigoPK(rs.getString("codigo_pk"));
				elemento.setCodigo(rs.getString("codigo"));
				elemento.setDescripcion(rs.getString("descripcion"));
				elemento.setOrden(rs.getInt("orden"));
				elemento.setCodigoInstitucion(rs.getInt("codigo_institucion"));

				if (tipoElemento == tipoElementoSeccion) {
					DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
					seccion.setColumnasSeccion(rs.getInt("columnas_seccion"));
					seccion.setTipoSeccion(rs.getString("tipo"));
					if (rs.getString("sexo").equals(
							ConstantesBD.codigoNuncaValido + ""))
						seccion.setSexoSeccion("");
					else
						seccion.setSexoSeccion(rs.getString("sexo"));
					if (rs.getString("edad_inicial_dias").equals(
							ConstantesBD.codigoNuncaValido + ""))
						seccion.setRangoInicialEdad("");
					else
						seccion.setRangoInicialEdad(rs
								.getString("edad_inicial_dias"));
					if (rs.getString("edad_final_dias").equals(
							ConstantesBD.codigoNuncaValido + ""))
						seccion.setRangoFInalEdad("");
					else
						seccion.setRangoFInalEdad(rs
								.getString("edad_final_dias"));

					seccion.setIndicativoRestriccionValCamp(rs
							.getString("restriccion_val_campo"));

					seccion = cargarSeccionComponente(con, seccion,
							consultarRegistro, codigoIngreso, numeroSolicitud,
							codigoPaciente, codigoEvolucion,
							codigoRespuestaProcedimiento, componente,
							codigoSexoPaciente, diasEdadPaciente,
							filtroDatosPaciente, codValoracionOdonto,
							codEvolucionOdonto, tomarRegistrosVacios,
							preservarHistorial);

					// Para aï¿½adir la seccion al componente se tiene que
					// verifica si tanto la seccion
					// como el componente deben estar activos, sin embargo, para
					// el caso de consultar informacion
					// historica basta con que la secciï¿½n estï¿½ activa
					// logger.info("\n mostrarMODIFICACION >>"+seccion.isMostrarModificacion());
					if ((!consultarRegistro
							&& componente.isMostrarModificacion() && seccion
							.isMostrarModificacion())
							|| (consultarRegistro && seccion
									.isMostrarModificacion())
							|| tomarRegistrosVacios) {
						logger.info("\n\n Entro a AGREGAR SECCION ");
						componente.setMostrarModificacion(true);
						componente.getElementos().add(seccion);
						elemento.setSeccion(true);
					}
				} else if (tipoElemento == tipoElementoEscala) {
					DtoEscala escala = (DtoEscala) elemento;

					// logger.info("VOY A CONSULTAR ESCALA DESDE componente "+componente.getDescripcion()+"***********************************");
					escala = cargarEscala(con, escala, consultarRegistro,
							codigoIngreso, numeroSolicitud, codigoPaciente,
							codigoEvolucion, codigoRespuestaProcedimiento,
							codigoPlantilla, true, false, codValoracionOdonto,
							codEvolucionOdonto, preservarHistorial);

					// logger.info("valor mostrar modificacion >> "+escala.isMostrarModificacion()+" >>"+escala.getCodigoPK());
					// logger.info("valor visible >> "+escala.isVisible()+" >>"+escala.getCodigoPK());
					// Para aï¿½adir la escala al componente se tiene que
					// verifica si tanto la escala
					// como el componente deben estar activos, sin embargo, para
					// el caso de consultar informacion
					// historica basta con que la escala estï¿½ activa
					if ((!consultarRegistro
							&& componente.isMostrarModificacion() && escala
							.isMostrarModificacion())
							|| (consultarRegistro && escala
									.isMostrarModificacion())
							|| tomarRegistrosVacios) {
						componente.setMostrarModificacion(true);
						logger.info("\n\n Entro a AGREGAR ESCALA ");
						componente.getElementos().add(escala);
						elemento.setEscala(true);
					}
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// ****************************************************************************************************
			// /***********VALIDACIï¿½N QUE SOLO APLICA AL CARGAR
			// INFORMACIï¿½N**************************************
			puedoMostrarComponenteSinRegistroParametizacion(con,
					consultarRegistro, componente,
					codigoRespuestaProcedimiento, codigoEvolucion,
					codigoIngreso, numeroSolicitud, codigoPaciente,
					codigoPlantilla, codValoracionOdonto, codEvolucionOdonto,
					preservarHistorial);
			// **************************************************************************************************
		} catch (SQLException e) {
			logger.error("Error en cargarComponente: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		logger.info("mostrar componente " + componente.getDescripcion() + " ? "
				+ componente.isMostrarModificacion());
		return componente;
	}

	/**
	 * Mï¿½todo implementado para saber si puedo mostrar un componente que no
	 * tiene registro de parametrizacion
	 * 
	 * @param con
	 * @param consultarRegistro
	 * @param componente
	 * @param codigoRespuestaProcedimiento
	 * @param codigoEvolucion
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoPlantilla
	 * @param preservarHistorial
	 */
	private static void puedoMostrarComponenteSinRegistroParametizacion(
			Connection con, boolean consultarRegistro,
			DtoComponente componente, int codigoRespuestaProcedimiento,
			int codigoEvolucion, int codigoIngreso, int numeroSolicitud,
			int codigoPaciente, String codigoPlantilla,
			int codValoracionOdonto, int codEvolucionOdonto,
			boolean preservarHistorial) {
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try {

			logger.info("CONSULTAR REGISTRO => " + consultarRegistro);
			logger.info("componente a validar => "
					+ componente.getDescripcion());
			logger.info("tiene elementos => "
					+ componente.getElementos().size());
			logger.info("codigo evolucion odonto => " + codEvolucionOdonto);
			/**
			 * En esta parte si estï¿½ consultando informaciï¿½n histï¿½rico
			 * pero el componente encontrado no tenï¿½a informaciï¿½n
			 * paramï¿½trica ingresada, se debe contemplar su fecha/hora inicial
			 * de registro VS fecha/hora registro plantilla para saber si el
			 * componente sï¿½ llegï¿½ a usarse en esa fecha especï¿½fica
			 */
			if (consultarRegistro && preservarHistorial) {
				// Se consulta la fecha/hora del registro de la plantilla
				String fechaRegistroPlantilla = "";
				String horaRegistroPlantilla = "";
				String consulta = ""; // variable para editar consulta
				// Ese consecutivo puede ser por codigorespuesta procedimiento
				if (codigoRespuestaProcedimiento > 0) {
					consulta = strCargarFechaHoraRegistroPlantilla_RESPPROC;

					pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					pst.setInt(2, codigoRespuestaProcedimiento);
					rs = new ResultSetDecorator(pst.executeQuery());

					if (rs.next()) {
						fechaRegistroPlantilla = rs.getString("fecha_modifica");
						horaRegistroPlantilla = rs.getString("hora_modifica");
					}
				}
				// Evolucion
				else if (codigoEvolucion > 0) {
					consulta = strCargarFechaHoraRegistroPlantilla_EVOLUCION;

					pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					pst.setInt(2, codigoEvolucion);
					rs = new ResultSetDecorator(pst.executeQuery());

					if (rs.next()) {
						fechaRegistroPlantilla = rs.getString("fecha_modifica");
						horaRegistroPlantilla = rs.getString("hora_modifica");
					}
				} else if (codEvolucionOdonto > 0) {
					logger
							.info("CARGAR FECHA/HORA REGISTRO PLANTILLA EVOLUCION ODONTO: "
									+ strCargarFechaHoraRegistroPlantilla_EVOLUCION_ODONT);
					logger.info("codigoPlantilla: " + codigoPlantilla);
					logger.info("codEvolucionOdonto: " + codEvolucionOdonto);

					consulta = strCargarFechaHoraRegistroPlantilla_EVOLUCION_ODONT;

					pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					pst.setInt(2, codEvolucionOdonto);
					rs = new ResultSetDecorator(pst.executeQuery());

					if (rs.next()) {
						fechaRegistroPlantilla = rs.getString("fecha_modifica");
						horaRegistroPlantilla = rs.getString("hora_modifica");
					}
					logger.info("fechaREgistroPlantilla: "
							+ fechaRegistroPlantilla);
					logger.info("horaREgistroPlantilla: "
							+ horaRegistroPlantilla);
				}
				// Por ingreso, numero solicitud o
				// codigo_paciente-------------------------------------------------------
				else {
					consulta = strCargarFechaHoraRegistroPlantilla_INGRESO;

					// Se prepara la consulta dependiendo del filtro
					if (codigoIngreso > 0)
						consulta += " AND ingreso = " + codigoIngreso;
					if (numeroSolicitud > 0)
						consulta += " AND numero_solicitud = "
								+ numeroSolicitud;
					if (codigoPaciente > 0)
						consulta += " AND codigo_paciente = " + codigoPaciente;
					if (codValoracionOdonto > 0)
						consulta += " AND valoracion_odonto = "
								+ codValoracionOdonto;

					pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					rs = new ResultSetDecorator(pst.executeQuery());

					if (rs.next()) {
						fechaRegistroPlantilla = rs.getString("fecha_modifica");
						horaRegistroPlantilla = rs.getString("hora_modifica");
					}

				}
				// Fin consulta fecha /hora registro plantilla
				logger
						.info("Fecha/Hora registro plantilla=> "
								+ fechaRegistroPlantilla + ", "
								+ horaRegistroPlantilla);
				if (!fechaRegistroPlantilla.equals("")
						&& !horaRegistroPlantilla.equals("")) {
					consulta = strPuedoMostrarComponenteSinRegistroParametrico;
					consulta += " AND "
							+ "("
							+
							// Caso en el que puede que el componente aun se
							// encuentre activo (no se ha registrado su fecha
							// final)
							"(fecha_inicial_registro || '-' || hora_inicial_registro <= '"
							+ fechaRegistroPlantilla
							+ "-"
							+ horaRegistroPlantilla
							+ "' AND fecha_final_registro IS NULL) "
							+ "OR "
							+
							// Caso en el que el componente ya se elimino (ya
							// tiene registrada la fecha finl del registro)
							"('"
							+ fechaRegistroPlantilla
							+ "-"
							+ horaRegistroPlantilla
							+ "' between fecha_inicial_registro || '-' || hora_inicial_registro AND fecha_final_registro || '-' || hora_final_registro)"
							+ ")";
					// logger.info("consulta puedo mostrar componente=> "+consulta.replace("?",
					// componente.getConsecutivoParametrizacion()));
					pst = new PreparedStatementDecorator(con.prepareStatement(
							consulta));
					pst.setInt(1, Utilidades.convertirAEntero(componente
							.getConsecutivoParametrizacion()));
					rs = new ResultSetDecorator(pst.executeQuery());
					if (rs.next())
						componente.setMostrarModificacion(UtilidadTexto
								.getBoolean(rs
										.getString("mostrar_modificacion")));
					else
						componente.setMostrarModificacion(false);
				} else
					// Si no se encontrï¿½ registro de plantilla no se debe
					// mostrar el componente
					componente.setMostrarModificacion(false);
			}
			/*
			 * else if(
			 * componente.getCodigoTipo()==ConstantesCamposParametrizables
			 * .tipoComponenteOdontogramaEvol||
			 * componente.getCodigoTipo()==ConstantesCamposParametrizables
			 * .tipoComponenteOdontogramaDiag||
			 * componente.getCodigoTipo()==ConstantesCamposParametrizables
			 * .tipoComponenteIndicePlaca ) { logger.info("PASO POR AQUI!!!");
			 * componente.setMostrarModificacion(true); }
			 */
		} catch (SQLException e) {
			logger.error("Error realizan la verificacion de mostrar el componente: "
							+ e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		logger.info("mosdtrar modificacion componente? "
				+ componente.isMostrarModificacion());
	}

	/**
	 * 
	 * @param codigoPkEscala
	 * @param codigoPkPerfilNED_OPCIONAL
	 *            (SI ES MAYOR 0 EXISTE, DE LO CONTRARIO LA ESCALA VACIA)
	 * @return
	 */
	public static DtoEscala cargarEscalaPerfilNed(BigDecimal codigoPkEscala,
			double codigoPkPerfilNED_OPCIONAL) {
		Connection con = UtilidadBD.abrirConexion();
		DtoEscala escala = new DtoEscala();
		escala = cargarEncabezadoEscala(con, codigoPkEscala);
		escala = cargarSeccionesEscalaPerfilNED(con, escala,
				codigoPkPerfilNED_OPCIONAL);
		escala = cargarFactoresPrediccion(con, escala);
		UtilidadBD.closeConnection(con);
		return escala;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkEscala
	 * @return
	 */
	private static DtoEscala cargarEncabezadoEscala(Connection con,
			BigDecimal codigoPkEscala) {
		DtoEscala dto = new DtoEscala();
		String consultaStr = "select " + "codigo_pk||'' as codigo_pk , "
				+ "codigo||'' as codigo , " + "institucion as institucion , "
				+ "nombre as nombre , "
				+ "observaciones_requeridas as observaciones_requeridas , "
				+ "mostrar_modificacion as mostrar_modificacion " + "from "
				+ "historiaclinica.escalas " + "where " + "codigo_pk= "
				+ codigoPkEscala.doubleValue();

		logger.info("\n\n encabezado ESCALA-->" + consultaStr);
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(consultaStr));
			rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) {
				dto.setCodigoPK(rs.getString("codigo_pk"));
				dto.setCodigo(rs.getString("codigo"));
				dto.setCodigoInstitucion(rs.getInt("institucion"));
				dto.setDescripcion(rs.getString("nombre"));
				dto.setObservacionesRequeridas(UtilidadTexto.getBoolean(rs
						.getString("observaciones_requeridas")));
				dto.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return dto;
	}

	/**
	 * Mï¿½todo implementado para cargar los datos de la escala
	 * 
	 * @param con
	 * @param escala
	 * @param consultarRegistro
	 * @param codigoIngreso
	 * @param codigoRespuestaProcedimiento
	 * @param codigoEvolucion
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param codigoPlantilla
	 * @param vieneDeComponente
	 * @param preservarHistorial
	 * @param modificacion
	 *            : para saber si los campos se cargan para modificarlos o para
	 *            resumen
	 * @return
	 */
	private static DtoEscala cargarEscala(Connection con, DtoEscala escala,
			boolean consultarRegistro, int codigoIngreso, int numeroSolicitud,
			int codigoPaciente, int codigoEvolucion,
			int codigoRespuestaProcedimiento, String codigoPlantilla,
			boolean vieneDeComponente, boolean modificacion,
			int codValoracionOdonto, int codEvolucionOdonto,
			boolean preservarHistorial) {

		logger.info("\n\n ***************COdigo Valoracon ODONTO "+ codValoracionOdonto);
		// Variable que almacena el consecutivo del registro de la escala x
		// ingreso
		int consecutivoEscalaIngreso = ConstantesBD.codigoNuncaValido;
		/*ResultSetDecorator rs = null;
		PreparedStatementDecorator pst = null;*/
		try {

			if (codigoIngreso > 0) {
				// ***************SE CARGAN LOS HISTÓRICOS DE LA
				// ESCALA**************************************************
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con,strCargarHistoricoEscalaXIngreso);
				pst.setInt(1, codigoIngreso);
				pst.setInt(2, Utilidades.convertirAEntero(escala.getCodigoPK()));
				logger.info(pst);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

				while (rs.next()) {
					DtoEscala escalaHistorica = new DtoEscala();

					escalaHistorica.setConsecutivoHistorico(rs.getString("consecutivo"));
					escalaHistorica.setCodigoPK(rs.getString("consecutivo"));
					escalaHistorica.setCodigoFactorPrediccion(rs.getString("codigo_factor_pred"));
					escalaHistorica.setNombreFactorPrediccion(rs.getString("nombre_factor_pred"));
					escalaHistorica.setTotalEscala(rs.getDouble("total_escala"));
					escalaHistorica.setObservaciones(rs.getString("observaciones"));

					escala.getHistoricos().add(escalaHistorica);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				// ********************************************************************************************************
			}

			// *************CONSULTAR ESCALA DEL
			// REGISTRO*********************************
			// Si se desea consultar el registro de escala de la plantilla:
			logger.info("consultar el registro de escala de la plantilla "+ consultarRegistro);
			if (consultarRegistro) {
				if (codigoRespuestaProcedimiento > 0) {
					String consulta = strCargarEscalaPlantillaResProc;

					consulta += " AND ei.escala = " + escala.getCodigoPK();

					// Dependiendo de donde venga la escala (si componente o
					// plantilla), se realiza la consulta
					if (vieneDeComponente) {
						String[] vectorConsecutivo = escala.getConsecutivoParametrizacion().split(ConstantesBD.separadorSplit);
						consulta += " AND prpi.componente = "+ vectorConsecutivo[0] + " AND prpi.escala = "+ vectorConsecutivo[1];
					} else
					{
						consulta += " AND prpi.plantillas_escala = "+ escala.getConsecutivoParametrizacion();
					}

					// logger.info("Voy a consultar la escala dle proceddimiento => "+consulta+", codigoPlantilla=> "+codigoPlantilla+", codigoRespuestaProcedimiento=> "+codigoRespuestaProcedimiento);
					PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					pst.setInt(2, codigoRespuestaProcedimiento);
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

					// Si se encontró un registro se asigna el consecutivo de
					// la escala ingreso
					if (rs.next()) {
						consecutivoEscalaIngreso = rs.getInt("escala_ingreso");
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
					// logger.info("consecutivo Escala ingreso encontrado=========> "+consecutivoEscalaIngreso);
				} else if (codigoEvolucion > 0 || codEvolucionOdonto > 0) {

					String consulta = strCargarEscalaPlantillaEvolucion;

					if (codigoEvolucion > 0)
					{
						consulta += " AND pe.evolucion =  " + codigoEvolucion;
					}
					if (codEvolucionOdonto > 0)
					{
						consulta += " AND pe.evolucion_odonto =  "+ codEvolucionOdonto;
					}

					consulta += " AND ei.escala = " + escala.getCodigoPK();

					// Dependiendo de donde venga la escala (si componente o
					// plantilla), se realiza la consulta
					if (vieneDeComponente) {
						String[] vectorConsecutivo = escala.getConsecutivoParametrizacion().split(ConstantesBD.separadorSplit);
						consulta += " AND pee.componente = "+ vectorConsecutivo[0] + " AND pee.escala = "+ vectorConsecutivo[1];
					} else
					{
						consulta += " AND pee.plantilla_escala = "+ escala.getConsecutivoParametrizacion();
					}

					// logger.info("Voy a consultar la escala dle proceddimiento => "+consulta+", codigoPlantilla=> "+codigoPlantilla+", codigoRespuestaProcedimiento=> "+codigoRespuestaProcedimiento);
					PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

					// Si se encontró un registro se asigna el consecutivo de
					// la escala ingreso
					if (rs.next())
					{
						consecutivoEscalaIngreso = rs.getInt("escala_ingreso");
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
					// logger.info("consecutivo Escala ingreso encontrado=========> "+consecutivoEscalaIngreso);

				}

				// Por ingreso, numero solicitud o
				// codigo_paciente-------------------------------------------------------
				else if (numeroSolicitud > 0 || codValoracionOdonto > 0) {
					String consulta = strCargarEscalaPlantillaIngreso;
					if (numeroSolicitud > 0)
						consulta += " AND pi.numero_solicitud = "+ numeroSolicitud + " ";
					if (codigoPaciente > 0)
						consulta += " AND pi.codigo_paciente = "+ codigoPaciente;
					if (codigoIngreso > 0)
						consulta += " AND pi.ingreso = " + codigoIngreso;
					if (codValoracionOdonto > 0)
						consulta += " AND  pi.valoracion_odonto = "+ codValoracionOdonto + " ";
					consulta += " AND ei.escala = " + escala.getCodigoPK();
					// Dependiendo de donde venga la escala (si componente o
					// plantilla), se realiza la consulta
					if (vieneDeComponente) {
						String[] vectorConsecutivo = escala.getConsecutivoParametrizacion().split(ConstantesBD.separadorSplit);
						consulta += " AND pei.componente = "+ vectorConsecutivo[0] + " AND pei.escala = "+ vectorConsecutivo[1];
					} else
					{
						consulta += " AND pei.plantilla_escala = "+ escala.getConsecutivoParametrizacion();
					}

					// logger.info("CONSULTA DE LOS DATOS DE LA ESCALA=>"+consulta.replace("?",
					// codigoPlantilla));
					PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					logger.info("Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa Esta es la que mas me importa "+pst);
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

					// Si se encontró un registro se asigna el consecutivo de
					// la escala ingreso
					if (rs.next())
					{
						consecutivoEscalaIngreso = rs.getInt("escala_ingreso");
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
					logger.info(consecutivoEscalaIngreso);
				}

				else if (codigoPaciente > 0) {

					String consulta = strCargarEscalaPlantillaPaciente;

					consulta += " AND ei.escala = " + escala.getCodigoPK();

					// Dependiendo de donde venga la escala (si componente o
					// plantilla), se realiza la consulta
					if (vieneDeComponente) {
						String[] vectorConsecutivo = escala.getConsecutivoParametrizacion().split(ConstantesBD.separadorSplit);
						consulta += " AND escpac.componente = "+ vectorConsecutivo[0]+ " AND escpac.escala = "+ vectorConsecutivo[1];
					} else
					{
						consulta += " AND escpac.plantilla_escala = "+ escala.getConsecutivoParametrizacion();
					}

					// logger.info("Voy a consultar la escala del ingreso Paciente Odonto => "+consulta+", codigoPlantilla=> "+codigoPlantilla+", codigoRespuestaProcedimiento=> "+codigoRespuestaProcedimiento);
					PreparedStatementDecorator pst = new PreparedStatementDecorator(con, consulta);
					pst.setInt(1, Utilidades.convertirAEntero(codigoPlantilla));
					pst.setInt(2, codigoPaciente);

					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
					logger.info("%tratando de obtener la escala que se guardó de la plantilla del paciente: "+ pst);
					// Si se encontró un registro se asigna el consecutivo de la
					// escala ingreso
					if (rs.next()) {
						consecutivoEscalaIngreso = rs.getInt("escala_ingreso");
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
					logger.info("%consecutivo Escala ingreso encontrado=========> "+ consecutivoEscalaIngreso);
				}

			}

			if (preservarHistorial) {
				escala.setConsecutivoHistorico(consecutivoEscalaIngreso + "");
			}
			// ****************************************************************************
			// logger.info("CONSULTA DATOS DE LA ESCALA "+escala.getDescripcion());
			// ***********SE CARGAN LAS SECCIONES DE LA
			// ESCALA*****************************

			escala = cargarSeccionesEscala(con, escala,consecutivoEscalaIngreso, consultarRegistro, modificacion,preservarHistorial);
			// ******************************************************************************

			// *************SE CARGAN LOS FACTORES DE
			// PREDICCION*************************************
			escala = cargarFactoresPrediccion(con, escala);
			// **************************************************************************************

			logger.info("Aqui se debe cargar la información consultarRegistro "+consultarRegistro);
			
			logger.info("Aqui se debe cargar la información consecutivoEscalaIngreso "+consecutivoEscalaIngreso);
			
			// Si se deseaba consultar el registro de la escala y se encontró
			// un consecutivo de escala ingreso se consulta su información
			if (consultarRegistro && consecutivoEscalaIngreso > 0) {
				// *************SE CARGAN LOS ARCHIVOS
				// ADJUNTOS******************************************
				// Si se desean consultar lo guardado de la escala y se
				// encontrï¿½ registro x ingreso entonces se consultan los
				// adjuntos ingresados
				escala = cargarAdjuntosEscala(con, escala,consecutivoEscalaIngreso, preservarHistorial);
				// ******************************************************************************************
			}

		} catch (SQLException e) {
			logger.error("Error en cargarEscala: " + e);
		}finally {			
			/*if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	*/	
	
		}

		// Si se deseaba consultar el rgistro de una escala y no se ingresó
		// información no se debe mostrar
		 if (consultarRegistro && consecutivoEscalaIngreso <= 0){
			 escala.setMostrarModificacion(false);
		 }

		// logger.info("ï¿½Mostrar Modificacion Escala? "+escala.isMostrarModificacion());
		return escala;
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param preservarHistorial
	 * @return
	 */
	private static DtoEscala cargarAdjuntosEscala(Connection con,
			DtoEscala escala, int consecutivoEscalaIngreso,
			boolean preservarHistorial) {
		int numArchivos = 0;
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(strCargarAdjuntosEscala));
			pst.setDouble(1, Utilidades.convertirADouble(consecutivoEscalaIngreso + ""));
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				escala.setArchivosAdjuntos("adjEscalaOriginal"
						+ escala.getCodigoPK() + "_" + numArchivos, rs
						.getString("nombre_original"));
				escala.setArchivosAdjuntos("adjEscala" + escala.getCodigoPK()
						+ "_" + numArchivos, rs.getString("nombre_archivo"));
				if (preservarHistorial) {
					escala.setArchivosAdjuntos("adjCodigoPk"
							+ escala.getCodigoPK() + "_" + numArchivos, rs
							.getInt("codigo_pk"));
				} else {
					escala.setArchivosAdjuntos("adjCodigoPk"
							+ escala.getCodigoPK() + "_" + numArchivos, "");
				}
				numArchivos++;
			}
			escala.setArchivosAdjuntos("numRegistros", numArchivos);
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// **************************************************************************************
			// ************* SE CARGA LA INFORMACIï¿½N GENERAL DE LA
			// ESCALA*****************************
			pst = new PreparedStatementDecorator(con.prepareStatement(strCargarDatosGeneralesEscalaIngreso));
			pst.setDouble(1, Utilidades
					.convertirADouble(consecutivoEscalaIngreso + ""));
			rs = new ResultSetDecorator(pst.executeQuery());
			if (rs.next()) {
				escala.setCodigoFactorPrediccion(rs.getString("codigo_factor"));
				escala.setNombreFactorPrediccion(rs.getString("nombre_factor"));
				escala.setTotalEscala(rs.getDouble("valor_total"));
				escala.setObservaciones(rs.getString("observaciones"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		} catch (SQLException e) {
			logger.error("Error en cargarEscala: " + e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return escala;
	}

	/**
	 * 
	 * @param escala
	 * @return
	 */
	private static DtoEscala cargarFactoresPrediccion(Connection con,DtoEscala escala) {
		PreparedStatementDecorator pst =null;
		ResultSetDecorator rs = null;
		try {
			pst = new PreparedStatementDecorator(con,strCargarFactoresPrediccionEscala);
			pst.setInt(1, Utilidades.convertirAEntero(escala.getCodigoPK()));
			pst.setInt(2, escala.getCodigoInstitucion());
			logger.info("\n\nstrCargarFactoresPrediccionEscala->" + pst);
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoEscalaFactorPrediccion factor = new DtoEscalaFactorPrediccion();
				factor.setActivo(UtilidadTexto.getBoolean(rs.getString("mostrar")));
				if (factor.isActivo()) {
					factor.setActivo(UtilidadTexto.getBoolean(rs.getString("mostrar_modificacion")));
				}
				factor.setCodigoPK(rs.getString("codigo_pk"));
				factor.setCodigo(rs.getString("codigo"));
				factor.setNombre(rs.getString("nombre"));
				factor.setCodigoInstitucion(rs.getInt("codigo_institucion"));
				factor.setValorInicial(rs.getDouble("valor_inicial"));
				factor.setValorFinal(rs.getDouble("valor_final"));
				if (factor.isActivo()) {
					escala.getFactoresPrediccion().add(factor);
				}

			}
		} catch (SQLException e) {
			logger.error("Error",e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return escala;
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param preservarHistorial
	 * @param modificacion
	 *            : atributo usado para saber si los cmapos se usan para
	 *            modificacion
	 * @param soloDatosIngresados
	 * @param consecutivoEscalaIngresoOPCIONAL
	 * @param consultarRegistro
	 * @return
	 */
	private static DtoEscala cargarSeccionesEscala(Connection con,
			DtoEscala escala, int consecutivoEscala,
			boolean consultarRegistroEscala, boolean modificacion,
			boolean preservarHistorial) {
		
		logger.info("Cargar secciones escalas");
		
		String strCargarCamposSeccionEscala = strCargarCamposSeccionEscalaIngreso;
		/*PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		PreparedStatementDecorator pst1 = null;
		ResultSetDecorator rs2 = null;*/
		try {
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,strCargarSeccionesEscala);
			pst.setInt(1, Utilidades.convertirAEntero(escala.getCodigoPK()));
			logger.info(pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoSeccionParametrizable seccion = new DtoSeccionParametrizable();
				seccion.setVisible(UtilidadTexto.getBoolean(rs.getString("mostrar")));
				seccion.setMostrarModificacion(UtilidadTexto.getBoolean(rs.getString("mostrar_modificacion")));
				seccion.setCodigoPK(rs.getString("codigo_pk"));
				seccion.setDescripcion(rs.getString("descripcion"));
				seccion.setTipoRespuesta(rs.getString("tipo_respuesta"));
				seccion.setConsecutivoHistorico(rs.getString("codigo_pk"));

				// *****************SE CARGAN LOS CAMPOS DE LA
				// ESCALA***********************************************************
				PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con,strCargarCamposSeccionEscala);

				// logger.info("\n\n seccion-->"+strCargarCamposSeccionEscala+" sec-->"+seccion.getCodigoPK()
				// +"ESCala >>"+consecutivoEscala);
				pst1.setInt(1, consecutivoEscala);
				pst1.setInt(2, Utilidades.convertirAEntero(seccion.getCodigoPK()));

				logger.info(pst1);
				ResultSetDecorator rs2 = new ResultSetDecorator(pst1.executeQuery());

				while (rs2.next()) {
					DtoCampoParametrizable campo = new DtoCampoParametrizable();

					campo.setMostrar(UtilidadTexto.getBoolean(rs2.getString("mostrar")));
					campo.setMostrarModificacion(UtilidadTexto.getBoolean(rs2.getString("mostrar_modificacion")));
					campo.setConsecutivoParametrizacion(rs2.getString("consecutivo"));
					campo.setObservacionesRequeridas(UtilidadTexto.getBoolean(rs2.getString("observaciones_requeridas")));
					campo.setCodigoPK(rs2.getString("codigo_pk"));
					campo.setCodigo(campo.getCodigoPK());
					campo.setNombre(rs2.getString("nombre"));
					campo.setEtiqueta(rs2.getString("etiqueta"));
					campo.setMaximo(rs2.getDouble("maximo"));
					campo.setMinimo(rs2.getDouble("minimo"));
					campo.setDecimales(rs2.getInt("decimales"));
					campo.setRequerido(UtilidadTexto.getBoolean(rs2.getString("requerido")));
					campo.setCodigoInstitucion(rs2.getInt("codigo_institucion"));
					if (preservarHistorial) {
						campo.setConsecutivoHistorico(rs2.getString("consecutivo_historico"));
					}

					logger.info("consultarRegistroEscala "+consultarRegistroEscala);
					logger.info("modificacion "+modificacion);
					logger.info("campo.getConsecutivoHistorico() >>>"+campo.getConsecutivoHistorico()+"<<<");
					// Si se desea cargar los valores del campo
					if (consultarRegistroEscala) {
						campo.setValor(rs2.getString("valor"));
						campo.setObservaciones(rs2.getString("observaciones"));
						// logger.info("cmapo escala "+campo.getNombre()+" >> valor :*"+campo.getValor()+"*");
						// Si no hay valor no se muestra
						if (
								(!campo.getValor().equals("") || !campo.getObservaciones().equals(""))
								|| 
								(modificacion /*&& !campo.getConsecutivoHistorico().equals("") TAREA 151625 xplanner 2008 (Juan David Ramírez - Felipe Jaramillo)*/ )) 
						{
							campo.setMostrarModificacion(true);
							seccion.setMostrarModificacion(true);
							campo.setMostrar(true);
						} else {
							campo.setMostrarModificacion(false);
						}
					}

					// /logger.info("campo escala "+campo.getNombre()+" >> mostrar : "+campo.isMostrar()+" >> mostrarmodificacion : "+campo.isMostrarModificacion());
					// Si hay que mostrar campo se agrega a la secciï¿½n
					if (campo.isMostrarModificacion())
					{
						seccion.getCampos().add(campo);
					}
				}
				UtilidadBD.cerrarObjetosPersistencia(pst1, rs2, null);

/*				logger.info("voy a revisar los permisos");
				logger.info("consultarRegistroEscala "+consultarRegistroEscala);
				logger.info("escala.isMostrarModificacion() "+escala.isMostrarModificacion());
				logger.info("seccion.isMostrarModificacion() "+seccion.isMostrarModificacion());*/
				if (
						// Si se desea consultar histórico la sección debe tener
						// permisos para mostrar
						(consultarRegistroEscala && seccion.isMostrarModificacion()) ||
							// Si no se desea consultar la información la escala y la
							// sección deben tener permisos para mostrar
									(!consultarRegistroEscala
											&& escala.isMostrarModificacion() 
											&& seccion.isMostrarModificacion())) {
					// logger.info("SE ENCONTRO LA SECCION "+seccion.getDescripcion()+" así que amerita que se añada la escala");
					escala.setMostrarModificacion(true);
//					logger.info("escala.isMostrarModificacion() "+escala.isMostrarModificacion());
					escala.getSecciones().add(seccion);
				}

			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		} catch (SQLException e) {
			logger.error("Error",e);
		}finally {
			/*if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			
			if(rs2!=null){
				try{
					rs2.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(pst!=null  ){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(pst1!=null){
				try{
					pst1.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}*/
		}
		return escala;
	}

	/**
	 * Método para cargar la escala del perfil NED
	 * 
	 * @param con
	 * @param escala
	 * @param consecutivoEscalaIngresoOPCIONAL
	 * @param consultarRegistro
	 * @return
	 */
	private static DtoEscala cargarSeccionesEscalaPerfilNED(Connection con,
			DtoEscala escala, double codigoPkPerfilNED_OPCIONAL) {
		PreparedStatementDecorator pst = null;
		String strCargarCamposSeccionEscala = codigoPkPerfilNED_OPCIONAL <= 0 ? strCargarCamposSeccionEscalaPerfilNedNueva
				: strCargarCamposSeccionEscalaPerfilNedYaInsertada;

		logger.info("\n\n ESCALA --->" + strCargarSeccionesEscala + " --> " + escala.getCodigoPK());
		ResultSetDecorator rs = null;
		ResultSetDecorator rs2 = null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(strCargarSeccionesEscala));
			pst.setInt(1, Utilidades.convertirAEntero(escala.getCodigoPK()));
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoSeccionParametrizable seccion = new DtoSeccionParametrizable();
				seccion.setVisible(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				seccion.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				seccion.setCodigoPK(rs.getString("codigo_pk"));
				seccion.setDescripcion(rs.getString("descripcion"));
				seccion.setTipoRespuesta(rs.getString("tipo_respuesta"));

				// *****************SE CARGAN LOS CAMPOS DE LA
				// ESCALA***********************************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(strCargarCamposSeccionEscala));

				if (codigoPkPerfilNED_OPCIONAL > 0) {
					logger.info("\n\nseccion-->" + strCargarCamposSeccionEscala
							+ "-->" + codigoPkPerfilNED_OPCIONAL + " sec-->"
							+ seccion.getCodigoPK());
					pst.setDouble(1, codigoPkPerfilNED_OPCIONAL);
					pst.setInt(2, Utilidades.convertirAEntero(seccion
							.getCodigoPK()));
				} else {
					logger.info("\n\nseccion-->" + strCargarCamposSeccionEscala
							+ " sec-->" + seccion.getCodigoPK());
					pst.setInt(1, Utilidades.convertirAEntero(seccion
							.getCodigoPK()));
				}

				rs2 = new ResultSetDecorator(pst
						.executeQuery());

				while (rs2.next()) {
					DtoCampoParametrizable campo = new DtoCampoParametrizable();

					campo.setMostrar(UtilidadTexto.getBoolean(rs2
							.getString("mostrar")));
					campo.setMostrarModificacion(UtilidadTexto.getBoolean(rs2
							.getString("mostrar_modificacion")));
					campo.setConsecutivoParametrizacion(rs2
							.getString("consecutivo"));
					campo.setObservacionesRequeridas(UtilidadTexto
							.getBoolean(rs2
									.getString("observaciones_requeridas")));
					campo.setCodigoPK(rs2.getString("codigo_pk"));
					campo.setCodigo(campo.getCodigoPK());
					campo.setNombre(rs2.getString("nombre"));
					campo.setEtiqueta(rs2.getString("etiqueta"));
					campo.setMaximo(rs2.getDouble("maximo"));
					campo.setMinimo(rs2.getDouble("minimo"));
					campo.setDecimales(rs2.getInt("decimales"));
					campo.setRequerido(UtilidadTexto.getBoolean(rs2
							.getString("requerido")));
					campo
							.setCodigoInstitucion(rs2
									.getInt("codigo_institucion"));

					// Si se desea cargar los valores del campo
					if (codigoPkPerfilNED_OPCIONAL > 0) {
						campo.setValor(rs2.getString("valor"));
						campo.setObservaciones(rs2.getString("observaciones"));
						campo.setMostrarModificacion(true);
						seccion.setMostrarModificacion(true);
						campo.setMostrar(true);
					}

					logger.info("campo escala " + campo.getNombre()
							+ " >> mostrar : " + campo.isMostrar()
							+ " >> mostrarmodificacion : "
							+ campo.isMostrarModificacion());
					// Si hay que mostrar campo se agrega a la secciï¿½n
					if (campo.isMostrarModificacion())
						seccion.getCampos().add(campo);
				}
				logger.info("Nï¿½mero total de campos de la seccion: "
						+ seccion.getCampos().size());
				
				//XPLANNER  URG_Sonria_Integracion_Ene292010_Abril082010 - aplica la mista tarea 6409 - tanto para consutar - ingresar/modificar [id=6410]
				//URG_Sonria_Integracion_Ene292010_Abril082010 - se estan duplicando las etiquetas en las escalas [id=6409] 
				if(seccion.getCampos().size()==0 && codigoPkPerfilNED_OPCIONAL>0)
				{
					seccion.setVisible(false);
				}
				
				// **************************************************************************************************************

				if (
				// Si se desea consultar histï¿½rico la seccion debe tener
				// permisos para mostrar
				((codigoPkPerfilNED_OPCIONAL > 0) && seccion
						.isMostrarModificacion())
						||
						// Si no se desea consultar la informaciï¿½n la escala y
						// la seccion deben tener permisos para mostrar
						(codigoPkPerfilNED_OPCIONAL <= 0
								&& escala.isMostrarModificacion() && seccion
								.isMostrarModificacion())) {
					// logger.info("SE ENCONTRO LA SECCION "+seccion.getDescripcion()+" asï¿½ que amerita que se aï¿½ada la escala");
					escala.setMostrarModificacion(true);
					escala.getSecciones().add(seccion);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs2!=null){
				try{
					rs2.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return escala;
	}

	/**
	 * Se carga la seccion del componente
	 * 
	 * @param con
	 * @param seccion
	 * @param consultarRegistro
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoEvolucion
	 * @param codigoRespuestaProcedimiento
	 * @param componente
	 * @param filtroDatosPaciente
	 * @param diasEdadPaciente
	 * @param codigoSexoPaciente
	 * @param preservarHistorial
	 * @param seccionesValores
	 * @return
	 */
	private static DtoSeccionParametrizable cargarSeccionComponente(
			Connection con, DtoSeccionParametrizable seccion,
			boolean consultarRegistro, int codigoIngreso, int numeroSolicitud,
			int codigoPaciente, int codigoEvolucion,
			int codigoRespuestaProcedimiento, DtoComponente componente,
			int codigoSexoPaciente, int diasEdadPaciente,
			boolean filtroDatosPaciente, int codValoracionOdonto,
			int codEvolucionOdonto, boolean tomarRegistrosVacios,
			boolean preservarHistorial) {
		
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		/*PreparedStatementDecorator pst10 =  null;
		PreparedStatementDecorator pst30 = null;
		PreparedStatementDecorator pst50 = null;
		PreparedStatementDecorator pst60 = null;
		PreparedStatementDecorator pst70 = null;
		PreparedStatementDecorator pst40 = null;
		
		ResultSetDecorator rs3 = null;
		ResultSetDecorator rs4 = null;
		
		ResultSetDecorator rs2 = null;*/

		try {
			String consulta = "";
			// ************************SE CARGAN LOS CAMPOS DE LA
			// SECCION********************************************************

			// logger.info("Consulta BB: "+strCargarCamposSeccionComponente +
			// " \nCodigo Comp seccion "
			// +seccion.getConsecutivoParametrizacion());

			pst = new PreparedStatementDecorator(con.prepareStatement(strCargarCamposSeccionComponente));
			pst.setDouble(1, Utilidades.convertirADouble(seccion
					.getConsecutivoParametrizacion()));

			rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next()) {
				DtoCampoParametrizable campo = new DtoCampoParametrizable();

				campo.setMostrar(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				campo.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				campo
						.setConsecutivoParametrizacion(rs
								.getString("consecutivo"));
				campo.setCodigoPK(rs.getString("codigo_pk"));
				campo.setCodigo(rs.getString("codigo"));
				campo.setNombre(rs.getString("nombre"));
				campo.setEtiqueta(rs.getString("etiqueta"));
				campo.setCodigoTipo(rs.getInt("codigo_tipo"));
				campo.setNombreTipo(rs.getString("nombre_tipo"));
				campo.setTamanio(rs.getInt("tamanio"));
				campo.setSigno(rs.getString("signo"));
				campo.setCodigoUnidad(rs.getInt("codigo_unidad"));
				campo.setNombreUnidad(rs.getString("nombre_unidad"));
				campo.setValorPredeterminado(rs
						.getString("valor_predeterminado"));
				campo.setMaximo(rs.getDouble("maximo"));
				campo.setMinimo(rs.getDouble("minimo"));
				campo.setDecimales(rs.getInt("decimales"));
				campo.setColumnasOcupadas(rs.getInt("columnas_ocupadas"));
				campo.setOrden(rs.getInt("orden"));
				campo.setUnicoXFila(UtilidadTexto.getBoolean(rs
						.getString("unico_fila")));
				campo.setRequerido(UtilidadTexto.getBoolean(rs
						.getString("requerido")));
				campo.setFormula(rs.getString("formula"));
				campo.setCodigoInstitucion(rs.getInt("codigo_institucion"));
				campo.setTipoHtml(rs.getString("tipo_html"));

				// Anexo 841
				campo.setManejaImagen(rs.getString("maneja_img").trim());
				campo.setImagenAsociar(rs.getInt("img_asociar") + "");
				campo.setImagenBase(new InfoDatosString(rs
						.getString("cod_img_base"), rs
						.getString("nom_img_base").trim(), rs.getString(
						"path_img_base").trim()));
				campo.getImagenBase().setIndicativo(
						CarpetasArchivos.IMAGENES_BASE.getRuta()
								+ rs.getString("path_img_base").trim());

				// Se consultan las opciones del campo (solo aplica para chequeo
				// o
				// seleccion)--------------------------------------------------------------------------------------
				PreparedStatementDecorator pst10 = new PreparedStatementDecorator(con.prepareStatement(strCargarOpcionesCampo));
				pst10.setDouble(1, Utilidades.convertirADouble(campo
						.getCodigoPK()));
				pst10.setInt(2, campo.getCodigoInstitucion());
				ResultSetDecorator rs2 = new ResultSetDecorator(pst10.executeQuery());

				while (rs2.next()) {
					DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
					opcion.setCodigoPk(rs2.getString("codigo_pk"));
					opcion.setCodigoHistorico(rs2.getString("codigo_pk"));
					opcion.setOpcion(rs2.getString("opcion"));
					opcion.setValor(rs2.getString("valor"));
					opcion.setCodigoPkCampoParam(rs2
							.getString("campo_parametrizable"));

					// Anexo 841
					opcion.setConvencionOdon(new InfoDatosString(rs2
							.getString("consecutivo_con_odon"), rs2.getString(
							"nom_con_odon").trim(), rs2.getString(
							"path_con_odo").trim()));

					campo.getOpciones().add(opcion);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst10, rs2, null);

				// /Se verifica si se debe consultar la informaciï¿½n registrada
				// -----------------------------
				if (consultarRegistro) {
					if (codigoRespuestaProcedimiento > 0) {
						consulta = strCargarValorCampoSeccionComponente_RESPPROC;

						PreparedStatementDecorator pst30 = new PreparedStatementDecorator(
								con.prepareStatement(consulta));
						pst30.setDouble(1, Utilidades
								.convertirADouble(codigoRespuestaProcedimiento
										+ ""));
						pst30.setDouble(2, Utilidades.convertirADouble(seccion
								.getConsecutivoParametrizacion()));
						pst30.setDouble(3, Utilidades.convertirADouble(campo
								.getCodigoPK()));

						logger.info("\n\nCargando seccion Componentes >> "
								+ consulta + " >> "
								+ codigoRespuestaProcedimiento + " >> "
								+ seccion.getConsecutivoParametrizacion()
								+ " >> " + campo.getCodigoPK());

						ResultSetDecorator rs3 = new ResultSetDecorator(pst30
								.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));

							PreparedStatementDecorator pst40 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccionComponente_RESPPROC));
							pst40.setDouble(1, Utilidades.convertirADouble(rs3
									.getInt("consecutivo")
									+ ""));

							ResultSetDecorator rs4 = new ResultSetDecorator(
									pst40.executeQuery());

							while (rs4.next()) {
								campo.setMostrarModificacion(true);

								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)) {
									boolean encontroCampo = false;
									for (DtoOpcionCampoParam opcion : campo.getOpciones()) {
										if (opcion.getValor().equals(campo.getValor())) {
											opcion.setSeleccionado(ConstantesBD.acronimoSi);
											opcion.setValoresOpcionRegistrado(campo.getValoresOpcion());
											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo || tomarRegistrosVacios) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										opcion
												.setSeleccionado(ConstantesBD.acronimoSi);
										opcion.setOpcion(campo.getValor());
										campo.getOpciones().add(opcion);
									}

								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst40, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst30, rs3, null);
						
					} else if (codigoEvolucion > 0 || codEvolucionOdonto > 0) {

						consulta = codigoEvolucion > 0 ? strCargarValorCampoSeccionComponente_EVOLUCION
								: strCargarValorCampoSeccionComponente_EVOLUCION_ODONT;

						PreparedStatementDecorator pst50 = new PreparedStatementDecorator(
								con.prepareStatement(consulta));
						pst50.setInt(1, codigoEvolucion > 0 ? codigoEvolucion
								: codEvolucionOdonto);
						pst50.setDouble(2, Utilidades.convertirADouble(seccion
								.getConsecutivoParametrizacion()));
						pst50.setDouble(3, Utilidades.convertirAEntero(campo
								.getCodigoPK()));

						// logger.info("\n\nCargando seccion Componentes Evol. >> "+consulta+" >> "+codigoEvolucion+" >> "+seccion.getConsecutivoParametrizacion()+" >> "+campo.getCodigoPK());

						ResultSetDecorator rs3 = new ResultSetDecorator(pst50
								.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);
							if (preservarHistorial) {
								campo.setConsecutivoHistorico(rs3
										.getString("consecutivo"));
							}
							campo.setGenerarAlerta(rs3
									.getString("generar_alerta"));

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));

							PreparedStatementDecorator pst60 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccionComponente_EVOLUCION));
							pst60.setInt(1, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(
									pst60.executeQuery());

							while (rs4.next()) {
								// Si el campo tiene informaciï¿½n debe
								// mostrarse sin importar si estï¿½ inactivado
								campo.setMostrarModificacion(true);

								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)) {
									boolean encontroCampo = false;
									for (DtoOpcionCampoParam opcion : campo
											.getOpciones()) {
										if (opcion.getValor().equals(
												campo.getValor())) {
											if (preservarHistorial) {
												opcion
														.setCodigoHistorico(rs4
																.getString("consecutivo"));
											}
											if (codigoEvolucion > 0)
												opcion
														.setSeleccionado(ConstantesBD.acronimoSi);
											else
												opcion
														.setSeleccionado(rs4
																.getString("seleccionado"));
											opcion
													.setValoresOpcionRegistrado(campo
															.getValoresOpcion());
											encontroCampo = true;
										}

									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										if (preservarHistorial) {
											opcion.setCodigoHistorico(rs4
													.getString("consecutivo"));
										}
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										if (codigoEvolucion > 0)
											opcion
													.setSeleccionado(ConstantesBD.acronimoSi);
										else
											opcion.setSeleccionado(rs4
													.getString("seleccionado"));
										opcion.setOpcion(campo.getValor());
										campo.getOpciones().add(opcion);
									}
								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst60, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst50, rs3, null);
					}

					// Por ingreso, numero solicitud o codigo_paciente o
					// valoracion
					// odontologica-------------------------------------------------------
					else if (numeroSolicitud > 0 || codValoracionOdonto > 0) {
						consulta = strCargarValorCampoSeccionComponente_INGRESO;
						String seccionWHERE = "";	
						
						// Se prepara la consulta dependiendo del filtro
						if (codigoIngreso > 0)
							seccionWHERE += " pi.ingreso = " + codigoIngreso;
						
						if(institucion > 0 && UtilidadTexto.getBoolean(ValoresPorDefecto.getValoracionUrgenciasEnHospitalizacion(institucion))){
							int idCuentaAsociada=util.UtilidadValidacion.tieneCuentaAsociada(con, codigoIngreso);
							int numeroSolicitudAsocio = ConstantesBD.codigoNuncaValido;			
							
							if(idCuentaAsociada>0){
								numeroSolicitudAsocio = UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con,idCuentaAsociada);
							}
							

							if(numeroSolicitud > 0 && numeroSolicitudAsocio > 0)
								seccionWHERE += (seccionWHERE.length() > 0 ? " AND"
										: "")
										+ " pi.numero_solicitud = "
										+ numeroSolicitudAsocio +" ";
							
						}else{
							if (numeroSolicitud > 0)
								seccionWHERE += (seccionWHERE.length() > 0 ? " AND"
										: "")
										+ " pi.numero_solicitud = "
										+ numeroSolicitud;	
						}

						if (codigoPaciente > 0)
							seccionWHERE += (seccionWHERE.length() > 0 ? " AND"
									: "")
									+ " pi.codigo_paciente = " + codigoPaciente;
						if (codValoracionOdonto > 0)
							seccionWHERE += (seccionWHERE.length() > 0 ? " AND"
									: "")
									+ " pi.valoracion_odonto = "
									+ codValoracionOdonto;

						// Siempre se consulta por estos campos
						if (preservarHistorial) {
							seccionWHERE += " AND pci.plantilla_componente = "
									+ componente
											.getConsecutivoParametrizacion();
						}
						seccionWHERE += " AND ci.componente_seccion = "
								+ seccion.getConsecutivoParametrizacion();
						seccionWHERE += " AND ci.campo_parametrizable = "
								+ campo.getCodigoPK();

						consulta += " WHERE " + seccionWHERE;
						
						//Mt 6339 Se agrega validacion para oracle y postgres.
						//Estaban utilizando "LIMIT" lo cual aplicaba solo para postgres;
						if (System.getProperty("TIPOBD").equals("ORACLE")) {
							consulta+=" AND ROWNUM <= 1 ";	
						}
						
						consulta += " ORDER BY pi.numero_solicitud";
						
						if (System.getProperty("TIPOBD").equals("POSTGRESQL")) {
							consulta+=ValoresPorDefecto.getValorLimit1()+" 1 ";
						}
						// logger.info("Se trata de consultar historico: "+consulta);
						PreparedStatementDecorator pst80 = new PreparedStatementDecorator(con.prepareStatement(consulta));
						ResultSetDecorator rs3 = new ResultSetDecorator(pst80
								.executeQuery());

						if (rs3.next()) {
							// logger.info("¡Encontró informacion!");
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));
							if (preservarHistorial) {
								campo.setConsecutivoHistorico(rs3
										.getString("consecutivo"));
							}
							campo.setGenerarAlerta(rs3
									.getString("generar_alerta"));

							PreparedStatementDecorator pst70 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccionComponente_INGRESO));
							pst70.setInt(1, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(
									pst70.executeQuery());

							while (rs4.next()) {
								campo.setMostrarModificacion(true);

								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)) {
									boolean encontroCampo = false;
									for (DtoOpcionCampoParam opcion : campo
											.getOpciones()) {
										if (opcion.getValor().equals(
												campo.getValor())) {
											if (preservarHistorial) {
												opcion
														.setCodigoHistorico(rs4
																.getString("consecutivo"));
											}
											if (numeroSolicitud > 0)
												opcion
														.setSeleccionado(ConstantesBD.acronimoSi);
											else
												opcion
														.setSeleccionado(rs4
																.getString("seleccionado"));
											opcion
													.setValoresOpcionRegistrado(campo
															.getValoresOpcion());
											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										if (preservarHistorial) {
											opcion.setCodigoHistorico(rs4
													.getString("consecutivo"));
										}
										if (numeroSolicitud > 0)
											opcion
													.setSeleccionado(ConstantesBD.acronimoSi);
										else
											opcion.setSeleccionado(rs4
													.getString("seleccionado"));
										opcion.setOpcion(campo.getValor());
										campo.getOpciones().add(opcion);
									}
								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst70, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst80, rs3, null);

					}

					else if (codigoPaciente > 0) {
						consulta = strCargarValorCampoSeleccionComponente_PACIENTE;

						PreparedStatementDecorator pst50 = new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst50.setInt(1, codigoPaciente);
						pst50.setDouble(2, Utilidades.convertirADouble(seccion
								.getConsecutivoParametrizacion()));
						// pst50.setInt(3,Utilidades.convertirAEntero(seccion.getConsecutivoParametrizacion()));
						pst50.setInt(3, Utilidades.convertirAEntero(campo
								.getCodigoPK()));

						ResultSetDecorator rs3 = new ResultSetDecorator(pst50
								.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));

							PreparedStatementDecorator pst60 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccionComponente_EVOLUCION));
							pst60.setInt(1, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(
									pst60.executeQuery());

							while (rs4.next()) {
								// Si el campo tiene informaciï¿½n debe
								// mostrarse sin importar si estï¿½ inactivado
								campo.setMostrarModificacion(true);

								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)) {
									boolean encontroCampo = false;
									for (DtoOpcionCampoParam opcion : campo
											.getOpciones()) {
										if (opcion.getValor().equals(
												campo.getValor())) {
											opcion
													.setSeleccionado(ConstantesBD.acronimoSi);
											opcion
													.setValoresOpcionRegistrado(campo
															.getValoresOpcion());
											encontroCampo = true;
										}

									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										opcion
												.setSeleccionado(ConstantesBD.acronimoSi);
										opcion.setOpcion(campo.getValor());
										campo.getOpciones().add(opcion);
									}
								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst60, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst50, rs3, null);

					}
					// --------------------------------------------------------------------------------------------------------

				}
				// ---------------------------------------------------------------------------------

				if (
				// /La seccion y el campo deben estar habilitados para permitir
				// mostrarse
				(seccion.isMostrarModificacion() && campo
						.isMostrarModificacion())
						||
						// Pero si el campo no estï¿½ habilitado y se ha
						// ingresado valor, tambiï¿½n se puede mostrar
						(consultarRegistro && campo.isHistorico())) {

					// /*********LLAMADO PARA VALIDACION DE CADA OPCION DE LOS
					// CAMPOS TIPO CHECK Y SELECT******************
					validacionSeccionesValor(con, campo, consultarRegistro,
							codigoIngreso, numeroSolicitud, codigoPaciente,
							codigoEvolucion, codigoRespuestaProcedimiento, "",
							"", codigoSexoPaciente, diasEdadPaciente,
							filtroDatosPaciente, componente, false, false); // viene
																			// de
																			// plantilla

					seccion.setMostrarModificacion(true);
					seccion.getCampos().add(campo);
				}

			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// *******************************************************************************************************************

			// *************SE CARGAN LAS SUBSECCIONES DE LA
			// SECCION*****************************
			consulta = strCargarSubseccionesSeccionComponente;
			if (filtroDatosPaciente)
				consulta += " "
						+ " and (sp.sexo is null or sp.sexo = "
						+ codigoSexoPaciente
						+ ") "
						+ " and ((sp.edad_inicial_dias is null and sp.edad_final_dias is null) or ("
						+ diasEdadPaciente
						+ " between sp.edad_inicial_dias and sp.edad_final_dias)) ";
			consulta += " ORDER BY orden ";

			// logger.info("888888888888 sexo paciente: " + codigoSexoPaciente);
			// logger.info("Consulta XX: "+consulta);

			pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst
					.setInt(1, Utilidades.convertirAEntero(componente
							.getCodigoPK()));
			pst.setInt(2, Utilidades.convertirAEntero(seccion.getCodigoPK()));

			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoSeccionParametrizable subseccion = new DtoSeccionParametrizable();
				subseccion.setVisible(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				subseccion.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				subseccion.setConsecutivoParametrizacion(rs
						.getString("consecutivo"));
				subseccion.setOrden(rs.getInt("orden"));
				subseccion.setCodigoPK(rs.getString("codigo_pk"));
				subseccion.setCodigo(rs.getString("codigo"));
				subseccion.setDescripcion(rs.getString("descripcion"));
				subseccion.setColumnasSeccion(rs.getInt("columnas_seccion"));
				subseccion.setTipoSeccion(rs.getString("tipo"));
				if (rs.getString("sexo").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setSexoSeccion("");
				else
					subseccion.setSexoSeccion(rs.getString("sexo"));
				if (rs.getString("edad_inicial_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setRangoInicialEdad("");
				else
					subseccion.setRangoInicialEdad(rs
							.getString("edad_inicial_dias"));
				if (rs.getString("edad_final_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setRangoFInalEdad("");
				else
					subseccion.setRangoFInalEdad(rs
							.getString("edad_final_dias"));

				subseccion.setIndicativoRestriccionValCamp(rs
						.getString("restriccion_val_campo"));

				// APLICACION DE RECURSIVIDAD PARA LAS SUBSECCIONES
				subseccion = cargarSeccionComponente(con, subseccion,
						consultarRegistro, codigoIngreso, numeroSolicitud,
						codigoPaciente, codigoEvolucion,
						codigoRespuestaProcedimiento, componente,
						codigoSexoPaciente, diasEdadPaciente,
						filtroDatosPaciente, codValoracionOdonto,
						codEvolucionOdonto, tomarRegistrosVacios,
						preservarHistorial);

				// /Se aï¿½ade la subseccion a la seccion si estï¿½ activa
				if (
				// (Caso sin consultar registro) Para mostrar la subseccion la
				// seccion debe tener el mostrar modificacion
				(!consultarRegistro && seccion.isMostrarModificacion() && subseccion
						.isMostrarModificacion())
						||
						// (Caso consultar registro) la subseccion debe tener
						// campos con informaciï¿½n
						(consultarRegistro && subseccion
								.tieneSubSeccionInformacion())) {
					seccion.setMostrarModificacion(true);
					seccion.getSecciones().add(subseccion);
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// **********************************************************************************
		} catch (SQLException e) {
			logger.error("Error en cargarSeccionComponente: " + e);
		}finally {
			
			/*try {
				if (rs3 != null) {
					rs3.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (rs4 != null) {
					rs4.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}*/
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			/*try {
				if (rs2 != null) {
					rs2.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}	*/		
			
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			/*try {
				if (pst40 != null) {
					pst40.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (pst10 != null) {
					pst10.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (pst30 != null) {
					pst30.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (pst50 != null) {
					pst50.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (pst60 != null) {
					pst60.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}
			try {
				if (pst70 != null) {
					pst70.close();
				}
			} catch (SQLException sqlException) {
				logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
			}*/
			
		}

		// (Caso consultar registro) si la seccion no tiene campos ni secciones
		// entonces no la puedo mostrar
		if (consultarRegistro) {
			if (!seccion.tieneSeccionInformacion())
				seccion.setMostrarModificacion(false);

			// si a la secciï¿½n no se le llenaron campos (Historico) se
			// inhabilita el campo visible
			seccion.setVisible(false);
			for (DtoCampoParametrizable campo : seccion.getCampos())
				if (!campo.getValor().trim().equals(""))
					seccion.setVisible(true);

			for (DtoSeccionParametrizable subseccion : seccion.getSecciones())
				for (DtoCampoParametrizable campo : subseccion.getCampos())
					if (!campo.getValor().trim().equals(""))
						seccion.setVisible(true);

		}

		return seccion;
	}

	/**
	 * Mï¿½todo implementado para cargar el DTO de una seccion Parametrizable de
	 * una plantilla o componente
	 * 
	 * @param con
	 * @param seccion
	 * @param consultarRegistro
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoEvolucion
	 * @param codigoRespuestaProcedimiento
	 * @param consecutivoPlantilla
	 * @param consecutivoSeccionFija
	 * @param filtroDatosPaciente
	 * @param diasEdadPaciente
	 * @param codigoSexoPaciente
	 * @return
	 */
	private static DtoSeccionParametrizable cargarSeccionPlantilla(
			Connection con, DtoSeccionParametrizable seccion,
			boolean consultarRegistro, int codigoIngreso, int numeroSolicitud,
			int codigoPaciente, int codigoEvolucion,
			int codigoRespuestaProcedimiento, int codigoEvolucionOdo,
			int codigoValoracionOdo, String consecutivoPlantilla,
			String consecutivoSeccionFija, int codigoSexoPaciente,
			int diasEdadPaciente, boolean filtroDatosPaciente,
			boolean tomarRegistrosVacios) {
		/*PreparedStatementDecorator pst40 = null;
		PreparedStatementDecorator pst = null;
		PreparedStatementDecorator pst10 =  null;
		PreparedStatementDecorator pst20 = null;
		PreparedStatementDecorator pst30 = null;
		PreparedStatementDecorator pst50 = null;
		PreparedStatementDecorator pst60 = null;
		PreparedStatementDecorator pst70 = null;
		
		
		ResultSetDecorator rs3 = null;
		ResultSetDecorator rs4 = null;
		ResultSetDecorator rs = null;
		ResultSetDecorator rs2 = null;*/
		try {
			String consulta = "";
			// **********SE CARGAN LOS CAMPOS DE LA
			// SECCION***********************************
			// logger.info("Consulta: "+strCargarCamposSeccionPlantilla);
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(strCargarCamposSeccionPlantilla));
			pst.setInt(1, Utilidades.convertirAEntero(seccion.getConsecutivoParametrizacion()));
			// logger.info("\n\nCARGAR CAMPOS SECCION PLANTILLA consecutivo sec fja "+consecutivoSeccionFija+"=> "+strCargarCamposSeccionPlantilla.replace("?",
			// seccion.getConsecutivoParametrizacion()+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next()) {
				DtoCampoParametrizable campo = new DtoCampoParametrizable();

				campo.setMostrar(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				campo.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				campo
						.setConsecutivoParametrizacion(rs
								.getString("consecutivo"));
				campo.setCodigoPK(rs.getString("codigo_pk"));
				campo.setCodigo(rs.getString("codigo"));
				campo.setNombre(rs.getString("nombre"));
				campo.setEtiqueta(rs.getString("etiqueta"));
				campo.setCodigoTipo(rs.getInt("codigo_tipo"));
				campo.setNombreTipo(rs.getString("nombre_tipo"));
				campo.setTamanio(rs.getInt("tamanio"));
				campo.setSigno(rs.getString("signo"));
				campo.setCodigoUnidad(rs.getInt("codigo_unidad"));
				campo.setNombreUnidad(rs.getString("nombre_unidad"));
				campo.setValorPredeterminado(rs
						.getString("valor_predeterminado"));
				campo.setMaximo(rs.getDouble("maximo"));
				campo.setMinimo(rs.getDouble("minimo"));
				campo.setDecimales(rs.getInt("decimales"));
				campo.setColumnasOcupadas(rs.getInt("columnas_ocupadas"));
				campo.setOrden(rs.getInt("orden"));
				campo.setUnicoXFila(UtilidadTexto.getBoolean(rs
						.getString("unico_fila")));
				campo.setRequerido(UtilidadTexto.getBoolean(rs
						.getString("requerido")));
				campo.setFormula(rs.getString("formula"));
				campo.setCodigoInstitucion(rs.getInt("codigo_institucion"));
				campo.setTipoHtml(rs.getString("tipo_html"));

				// Anexo 841
				campo.setManejaImagen(rs.getString("maneja_img").trim());
				campo.setImagenAsociar(rs.getInt("img_asociar") + "");
				campo.setImagenBase(new InfoDatosString(rs
						.getString("cod_img_base"), rs
						.getString("nom_img_base").trim(), rs.getString(
						"path_img_base").trim()));

				campo.getImagenBase().setIndicativo(
						CarpetasArchivos.IMAGENES_BASE.getRuta()
								+ rs.getString("path_img_base").trim());

				// logger.info("mostrar modificacion inicial del campo >> "+campo.isMostrarModificacion());

				// Se consultan las opciones del campo (solo aplica para chequeo
				// o
				// seleccion)--------------------------------------------------------------------------------------
				PreparedStatementDecorator pst10 = new PreparedStatementDecorator(con.prepareStatement(strCargarOpcionesCampo));
				pst10.setInt(1, Utilidades.convertirAEntero(campo.getCodigoPK()));
				pst10.setInt(2, campo.getCodigoInstitucion());
				ResultSetDecorator rs2 = new ResultSetDecorator(pst10.executeQuery());

				while (rs2.next()) {
					DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
					opcion.setOpcion(rs2.getString("opcion"));
					opcion.setValor(rs2.getString("valor"));
					opcion.setCodigoPk(rs2.getString("codigo_pk"));
					opcion.setCodigoPkCampoParam(rs2
							.getString("campo_parametrizable"));

					// Anexo 841

					opcion.setConvencionOdon(new InfoDatosString(rs2
							.getString("consecutivo_con_odon"), rs2.getString(
							"nom_con_odon").trim(), rs2.getString(
							"path_con_odo").trim()));
					opcion.getConvencionOdon().setIndicativo(
							CarpetasArchivos.CONVENCION.getRuta()
									+ rs2.getString("path_con_odo").trim());
					campo.getOpciones().add(opcion);
				}
				UtilidadBD.cerrarObjetosPersistencia(pst10, rs2, null);

				// Se verifica si se debe consultar la informaciï¿½n registrada
				// -----------------------------
				logger.info("Consultar Registro sqlBasePlantillas>> "+ consultarRegistro);
				if (consultarRegistro)// ||vieneValoracion)
				{
					if (codigoRespuestaProcedimiento > 0) {
						consulta = strCargarValorCampoSeccion_RESPROC;

						if (codigoRespuestaProcedimiento > 0)
						{
							consulta += " AND prp.res_sol_proc = "+ codigoRespuestaProcedimiento;
						}
						consulta += " order by ppc.codigo_pk desc";

						PreparedStatementDecorator pst20 = new PreparedStatementDecorator(con,consulta);
						pst20.setInt(1, Utilidades.convertirAEntero(consecutivoPlantilla));
						pst20.setInt(2, Utilidades.convertirAEntero(campo.getConsecutivoParametrizacion()));

						logger.info(pst20);
						
						ResultSetDecorator rs3 = new ResultSetDecorator(pst20.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);

							campo.setNombreArchivoOriginal(rs3.getString("nombre_archivo_original"));

							PreparedStatementDecorator pst30 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccion_RESPROC));
							pst30.setInt(1, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(pst30.executeQuery());

							while (rs4.next()) {
								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)
										|| (campo
												.getTipoHtml()
												.equals(
														ConstantesCamposParametrizables.campoTipoSelect) && campo
												.getManejaImagen()
												.equals(ConstantesBD.acronimoSi))) {
									boolean encontroCampo = false;

									for (int i = 0; i < campo.getOpciones()
											.size(); i++) {
										if (campo.getOpciones().get(i)
												.getValor().equals(
														campo.getValor())) {
											campo
													.getOpciones()
													.get(i)
													.setSeleccionado(
															ConstantesBD.acronimoSi);
											campo
													.getOpciones()
													.get(i)
													.setValoresOpcionRegistrado(
															campo
																	.getValoresOpcion());
											if (campo
													.getTipoHtml()
													.equals(
															ConstantesCamposParametrizables.campoTipoSelect)) {
												campo
														.getOpciones()
														.get(i)
														.setNombreImgOpcion(
																rs4
																		.getString("imagen_opcion"));
												campo
														.getOpciones()
														.get(i)
														.setRutaImgOpcion(
																CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
																		.getRuta());
											}
											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										opcion
												.setSeleccionado(ConstantesBD.acronimoSi);
										opcion.setOpcion(campo.getValor());
										opcion.setNombreImgOpcion(rs4
												.getString("imagen_opcion"));
										opcion
												.setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
														.getRuta());
										campo.getOpciones().add(opcion);
									}

								}

							}
							UtilidadBD.cerrarObjetosPersistencia(pst30, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst20, rs3, null);
						
					} else if (codigoEvolucion > 0 || codigoEvolucionOdo > 0) {
						//

						consulta = strCargarValorCampoSeccion_EVOLUCION;

						if (codigoEvolucion > 0) {
							consulta += " AND pe.evolucion = "
									+ codigoEvolucion;
						} else if (codigoEvolucionOdo > 0) {
							consulta += " AND pe.evolucion_odonto = "
									+ codigoEvolucionOdo;
						}
						consulta += " order by pec.codigo_pk desc";
						// logger.info("consulta plantilla evolucion: "+consulta+", consecutivoPlantilla: "+consecutivoPlantilla+", consecutivoParametrizacion: "+campo.getConsecutivoParametrizacion());
						PreparedStatementDecorator pst40 = new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst40.setInt(1, Utilidades.convertirAEntero(consecutivoPlantilla));
						pst40.setInt(2, Utilidades.convertirAEntero(campo.getConsecutivoParametrizacion()));

						ResultSetDecorator rs3 = new ResultSetDecorator(pst40.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);
							campo.setConsecutivoHistorico(rs3
									.getString("consecutivo"));

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));

							PreparedStatementDecorator pst50 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccion_EVOLUCION));
							pst50.setInt(1, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(pst50.executeQuery());

							while (rs4.next()) {
								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								campo.setCodigoPkValorPlanEvoCamp(rs4.getInt("consecutivo"));
								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)
										|| (campo
												.getTipoHtml()
												.equals(
														ConstantesCamposParametrizables.campoTipoSelect) && campo
												.getManejaImagen()
												.equals(ConstantesBD.acronimoSi))) {
									boolean encontroCampo = false;

									for (int i = 0; i < campo.getOpciones()
											.size(); i++) {
										if (campo.getOpciones().get(i)
												.getValor().equals(
														campo.getValor())) {
											campo
													.getOpciones()
													.get(i)
													.setCodigoHistorico(
															rs4
																	.getString("consecutivo"));
											if (codigoEvolucionOdo > 0) {
												campo
														.getOpciones()
														.get(i)
														.setSeleccionado(
																rs4
																		.getString("seleccionado"));
											} else {
												campo
														.getOpciones()
														.get(i)
														.setSeleccionado(
																ConstantesBD.acronimoSi);
											}
											campo
													.getOpciones()
													.get(i)
													.setValoresOpcionRegistrado(
															campo
																	.getValoresOpcion());
											if (campo
													.getTipoHtml()
													.equals(
															ConstantesCamposParametrizables.campoTipoSelect)) {
												campo
														.getOpciones()
														.get(i)
														.setConvencionOdon(
																new InfoDatosString(
																		rs4
																				.getString("convencion"),
																		rs4
																				.getString("nombre_convencion")));
												campo
														.getOpciones()
														.get(i)
														.getConvencionOdon()
														.setIndicativo(
																CarpetasArchivos.CONVENCION
																		.getRuta()
																		+ rs4
																				.getString(
																						"archivo_convencion")
																				.trim());

												campo
														.getOpciones()
														.get(i)
														.setNombreImgOpcion(
																rs4
																		.getString("imagen_opcion"));
												campo
														.getOpciones()
														.get(i)
														.setRutaImgOpcion(
																CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
																		.getRuta());
											}

											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setCodigoHistorico(rs4
												.getString("consecutivo"));
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());

										// Si es una evolucion odontologica se
										// cargan valores adicionales porque
										// es una plantilla que se puede
										// modificar
										if (codigoEvolucionOdo > 0) {
											opcion.setSeleccionado(rs4
													.getString("seleccionado"));
											opcion
													.setConvencionOdon(new InfoDatosString(
															rs4
																	.getString("convencion"),
															rs4
																	.getString("nombre_convencion")));
											opcion
													.getConvencionOdon()
													.setIndicativo(
															CarpetasArchivos.CONVENCION
																	.getRuta()
																	+ rs4
																			.getString(
																					"archivo_convencion")
																			.trim());
											opcion.setSeleccionado(rs4
													.getString("seleccionado"));
											opcion
													.setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
															.getRuta());
										} else {
											opcion
													.setSeleccionado(ConstantesBD.acronimoSi);
										}
										opcion.setOpcion(campo.getValor());
										if (campo
												.getTipoHtml()
												.equals(
														ConstantesCamposParametrizables.campoTipoSelect)) {
											opcion
													.setConvencionOdon(new InfoDatosString(
															rs4
																	.getString("convencion"),
															rs4
																	.getString("nombre_convencion")));
											opcion
													.getConvencionOdon()
													.setIndicativo(
															CarpetasArchivos.CONVENCION
																	.getRuta()
																	+ rs4
																			.getString(
																					"archivo_convencion")
																			.trim());

											opcion
													.setNombreImgOpcion(rs4
															.getString("imagen_opcion"));
											opcion
													.setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
															.getRuta());
										}

										campo.getOpciones().add(opcion);
									}

								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst50, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst40, rs3, null);
					}
					// Por ingreso, numero solicitud o
					// codigo_paciente-------------------------------------------------------
					else if (numeroSolicitud > 0 || codigoValoracionOdo > 0) {
						consulta = strCargarValorCampoSeccion_INGRESO;

						String seccionWHERE = "";
						if (codigoIngreso > 0)
							seccionWHERE += " pi.ingreso = " + codigoIngreso;
						if (numeroSolicitud > 0)
							seccionWHERE += " "
									+ (seccionWHERE.equals("") ? "" : "AND")
									+ " pi.numero_solicitud = "
									+ numeroSolicitud;
						if (codigoPaciente > 0)
							seccionWHERE += " "
									+ (seccionWHERE.equals("") ? "" : "AND")
									+ " pi.codigo_paciente = " + codigoPaciente;
						if (codigoValoracionOdo > 0)
							seccionWHERE += " "
									+ (seccionWHERE.equals("") ? "" : "AND")
									+ " pi.valoracion_odonto = "
									+ codigoValoracionOdo;

						consulta += seccionWHERE
								+ " AND pi.plantilla = ? AND pic.plantilla_campo_sec = ? ";
						consulta += " order by pic.codigo_pk desc";

						PreparedStatementDecorator pst60 = new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst60.setInt(1, Utilidades
								.convertirAEntero(consecutivoPlantilla));
						pst60.setInt(2, Utilidades.convertirAEntero(campo
								.getConsecutivoParametrizacion()));

						ResultSetDecorator rs3 = new ResultSetDecorator(pst60
								.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);
							campo.setConsecutivoHistorico(rs3
									.getString("consecutivo"));

							campo.setNombreArchivoOriginal(rs3
									.getString("nombre_archivo_original"));
							// logger.info("Encontrï¿½ informacion !!! ");
							PreparedStatementDecorator pst70 = new PreparedStatementDecorator(con.prepareStatement(strCargarDetalleValorCampoSeccion_INGRESO));
							pst70.setInt(1, rs3.getInt("consecutivo"));
							pst70.setInt(2, rs3.getInt("consecutivo"));

							ResultSetDecorator rs4 = new ResultSetDecorator(
									pst70.executeQuery());

							while (rs4.next()) {

								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4
										.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox)
										|| (campo
												.getTipoHtml()
												.equals(
														ConstantesCamposParametrizables.campoTipoSelect) && campo
												.getManejaImagen()
												.equals(ConstantesBD.acronimoSi))) {
									boolean encontroCampo = false;

									for (int i = 0; i < campo.getOpciones()
											.size(); i++) {
										if (campo.getOpciones().get(i)
												.getValor().equals(
														campo.getValor())) {
											campo
													.getOpciones()
													.get(i)
													.setCodigoHistorico(
															rs4
																	.getString("consecutivo"));
											if (codigoValoracionOdo > 0) {
												campo
														.getOpciones()
														.get(i)
														.setSeleccionado(
																rs4
																		.getString("seleccionado"));

											} else {
												campo
														.getOpciones()
														.get(i)
														.setSeleccionado(
																ConstantesBD.acronimoSi);
											}
											campo
													.getOpciones()
													.get(i)
													.setValoresOpcionRegistrado(
															campo
																	.getValoresOpcion());
											if (campo
													.getTipoHtml()
													.equals(
															ConstantesCamposParametrizables.campoTipoSelect)) {
												campo
														.getOpciones()
														.get(i)
														.setConvencionOdon(
																new InfoDatosString(
																		rs4
																				.getString("convencion"),
																		rs4
																				.getString("nombre_convencion")));
												campo
														.getOpciones()
														.get(i)
														.getConvencionOdon()
														.setIndicativo(
																CarpetasArchivos.CONVENCION
																		.getRuta()
																		+ rs4
																				.getString(
																						"archivo_convencion")
																				.trim());
												campo
														.getOpciones()
														.get(i)
														.setNombreImgOpcion(
																rs4
																		.getString("imagen_opcion"));
												campo
														.getOpciones()
														.get(i)
														.setRutaImgOpcion(
																CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
																		.getRuta());
											}

											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setCodigoHistorico(rs4
												.getString("consecutivo"));
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										// Si es una valoracion odontologica se
										// cargan valores adicionales porque
										// es una plantilla que se puede
										// modificar
										if (codigoValoracionOdo > 0) {
											opcion.setSeleccionado(rs4
													.getString("seleccionado"));

										} else {
											opcion
													.setSeleccionado(ConstantesBD.acronimoSi);
										}
										opcion.setOpcion(campo.getValor());
										if (campo
												.getTipoHtml()
												.equals(
														ConstantesCamposParametrizables.campoTipoSelect)) {
											opcion
													.setConvencionOdon(new InfoDatosString(
															rs4
																	.getString("convencion"),
															rs4
																	.getString("nombre_convencion")));
											opcion
													.getConvencionOdon()
													.setIndicativo(
															CarpetasArchivos.CONVENCION
																	.getRuta()
																	+ rs4
																			.getString(
																					"archivo_convencion")
																			.trim());

											opcion
													.setNombreImgOpcion(rs4
															.getString("imagen_opcion"));
											opcion
													.setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
															.getRuta());
										}

										campo.getOpciones().add(opcion);
									}

								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst70, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst60, rs3, null);

					} else if (codigoPaciente > 0) {
						consulta = strCargarValorCampoSeccion_PacienteOdont;

						if (codigoPaciente > 0)
						{	
							consulta += " AND plpac.codigo_paciente = "+ codigoPaciente;
						}
						consulta += " order by camp.codigo_pk desc";
						PreparedStatementDecorator pst40 = new PreparedStatementDecorator(con,consulta);
						pst40.setInt(1, Utilidades.convertirAEntero(consecutivoPlantilla));
						pst40.setInt(2, Utilidades.convertirAEntero(campo.getConsecutivoParametrizacion()));

						logger.info(pst40);
						
						ResultSetDecorator rs3 = new ResultSetDecorator(pst40.executeQuery());

						if (rs3.next()) {
							// Como el campo tiene valores entonces se puede
							// mostrar
							campo.setHistorico(true);
							campo.setMostrar(true);

							campo.setNombreArchivoOriginal(rs3.getString("nombre_archivo_original"));
							campo.setConsecutivoHistorico(rs3.getInt("consecutivo")+ "");

							PreparedStatementDecorator pst50 = new PreparedStatementDecorator(con,strCargarDetalleValorCampoSeccion_PacienteOdont);
							pst50.setInt(1, rs3.getInt("consecutivo"));

							logger.info(pst50);
							
							ResultSetDecorator rs4 = new ResultSetDecorator(pst50.executeQuery());

							while (rs4.next()) {
								campo.setValor(rs4.getString("valor"));
								campo.setValoresOpcion(rs4.getString("valores_opcion"));

								// Si el campo es CHECKBOX (respuesta multiple)
								// se verifican cada uno de sus valores
								if (		campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)
										|| (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect)
												&& campo.getManejaImagen().equals(ConstantesBD.acronimoSi)
											)
									) 
								{
									boolean encontroCampo = false;

									for (int i = 0; i < campo.getOpciones().size(); i++) {
										if (campo.getOpciones().get(i).getValor().equals(campo.getValor())) {
											campo.getOpciones().get(i).setSeleccionado(ConstantesBD.acronimoSi);
											campo.getOpciones().get(i).setValoresOpcionRegistrado(campo.getValoresOpcion());
											if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect)) {
												campo.getOpciones().get(i).setNombreImgOpcion(rs4.getString("imagen_opcion"));
												campo.getOpciones().get(i).setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION.getRuta());
											}
											encontroCampo = true;
										}
									}

									// Si el campo no se encontro (pudo haberse
									// elimiando su opcion de la
									// parametrizacion)
									if (!encontroCampo) {
										DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
										opcion.setValor(campo.getValor());
										opcion.setValoresOpcionRegistrado(campo
												.getValoresOpcion());
										opcion
												.setSeleccionado(ConstantesBD.acronimoSi);
										opcion.setOpcion(campo.getValor());
										opcion.setNombreImgOpcion(rs4
												.getString("imagen_opcion"));
										opcion
												.setRutaImgOpcion(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION
														.getRuta());
										campo.getOpciones().add(opcion);
									}

								}
							}
							UtilidadBD.cerrarObjetosPersistencia(pst50, rs4, null);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst40, rs3, null);

					}
					// --------------------------------------------------------------------------------------------------------

				}

				// ---------------------------------------------------------------------------------

				// logger.info("mostrar modificacion posterior del campo  >> "+campo.isMostrarModificacion()+" >> valor del campo **"+campo.getValor()+"**");

				if (
				// La seccion y el campo deben estar habilitados para permitir
				// mostrarse
				(!consultarRegistro && seccion.isMostrarModificacion() && campo
						.isMostrarModificacion())
						||
						// Pero si el campo no estï¿½ habilitado y se ha
						// ingresado valor, tambiï¿½n se puede mostrar
						(campo.isHistorico() && (consultarRegistro || tomarRegistrosVacios))) {
					/*
					 * logger.info("CAMPO A EVALUAR: "+campo.getNombre()+" "+campo
					 * .getCodigoPK());
					 * logger.info("tipo de campo: "+campo.getTipoHtml());
					 * logger
					 * .info("opciones del campo: "+campo.getOpciones().size());
					 * for(DtoOpcionCampoParam opcion:campo.getOpciones())
					 * logger
					 * .info("codigo pk de la opcion seleccionada "+opcion.
					 * getSeleccionado()+" :"+opcion.getCodigoPk());
					 */
					// *********LLAMADO PARA VALIDACION DE CADA OPCION DE LOS
					// CAMPOS TIPO CHECK Y SELECT******************
					validacionSeccionesValor(con, campo, consultarRegistro,
							codigoIngreso, numeroSolicitud, codigoPaciente,
							codigoEvolucion, codigoRespuestaProcedimiento,
							consecutivoPlantilla, consecutivoSeccionFija,
							codigoSexoPaciente, diasEdadPaciente,
							filtroDatosPaciente, null, true,// viene de
															// plantilla
							tomarRegistrosVacios);

					seccion.setMostrarModificacion(true);
					seccion.getCampos().add(campo);

				}
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// ********************************************************************************

			// *************SE CARGAN LAS SUBSECCIONES DE LA
			// SECCION*****************************
			consulta = strCargarSubseccionesSeccionPlantilla;
			if (filtroDatosPaciente)
				consulta += " "
						+ " and (sp.sexo IS NULL or sp.sexo = "
						+ codigoSexoPaciente
						+ ")"
						+ " and ((sp.edad_inicial_dias is null and sp.edad_final_dias is null) or ("
						+ diasEdadPaciente
						+ " between sp.edad_inicial_dias and sp.edad_final_dias)) ";
			consulta += " ORDER BY orden ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1, Utilidades.convertirAEntero(consecutivoSeccionFija));
			pst.setInt(2, Utilidades.convertirAEntero(seccion.getCodigoPK()));

			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoSeccionParametrizable subseccion = new DtoSeccionParametrizable();
				subseccion.setVisible(UtilidadTexto.getBoolean(rs
						.getString("mostrar")));
				subseccion.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				subseccion.setConsecutivoParametrizacion(rs
						.getString("consecutivo"));
				subseccion.setOrden(rs.getInt("orden"));
				subseccion.setCodigoPK(rs.getString("codigo_pk"));
				subseccion.setCodigo(rs.getString("codigo"));
				subseccion.setDescripcion(rs.getString("descripcion"));
				subseccion.setColumnasSeccion(rs.getInt("columnas_seccion"));
				subseccion.setTipoSeccion(rs.getString("tipo"));
				if (rs.getString("sexo").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setSexoSeccion("");
				else
					subseccion.setSexoSeccion(rs.getString("sexo"));
				if (rs.getString("edad_inicial_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setRangoInicialEdad("");
				else
					subseccion.setRangoInicialEdad(rs
							.getString("edad_inicial_dias"));
				if (rs.getString("edad_final_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					subseccion.setRangoFInalEdad("");
				else
					subseccion.setRangoFInalEdad(rs
							.getString("edad_final_dias"));
				subseccion.setIndicativoRestriccionValCamp(rs
						.getString("restriccion_val_campo"));

				// APLICACION DE RECURSIVIDAD PARA LAS SUBSECCIONES
				subseccion = cargarSeccionPlantilla(con, subseccion,
						consultarRegistro, codigoIngreso, numeroSolicitud,
						codigoPaciente, codigoEvolucion,
						codigoRespuestaProcedimiento, codigoEvolucionOdo,
						codigoValoracionOdo, consecutivoPlantilla,
						consecutivoSeccionFija, codigoSexoPaciente,
						diasEdadPaciente, filtroDatosPaciente,
						tomarRegistrosVacios);

				// Se aï¿½ade la subseccion a la seccion si estï¿½ activa
				if (
				// (Caso sin consultar registro) Para mostrar la subseccion la
				// seccion debe tener el mostrar modificacion
				(!consultarRegistro && seccion.isMostrarModificacion() && subseccion
						.isMostrarModificacion())
						||
						// (Caso consultar registro) la subseccion debe tener
						// campos con informaciï¿½n
						(subseccion.tieneSubSeccionInformacion() && (consultarRegistro || tomarRegistrosVacios))) {
					seccion.setMostrarModificacion(true);
					seccion.getSecciones().add(subseccion);
				}

			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			// **********************************************************************************
		} catch (SQLException e) {
			logger.error("Error en cargarSeccionPlantilla: ", e);

		}	finally {
			
				/*try {
					if (rs3 != null) {
						rs3.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (rs4 != null) {
						rs4.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (rs2 != null) {
						rs2.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}			
			
				try {
					if (pst != null) {
						pst.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (pst40 != null) {
						pst40.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (pst10 != null) {
						pst10.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (pst30 != null) {
						pst30.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (pst50 != null) {
						pst50.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}
				try {
					if (pst60 != null) {
						pst60.close();
					}
				} catch (SQLException sqlException) {
					logger.warn(sqlException+ " Error al cerrar el recurso SqlBasePlantillasDao "+ sqlException.toString());
				}*/
		}

		// (Caso consultar registro)
		if (consultarRegistro) {
			logger
					.info("\n\n ENTRA >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.info("seccion tiene informacion? "
					+ seccion.tieneSeccionInformacion());

			// si la seccion no tiene campos ni secciones entonces no la puedo
			// mostrar
			if (!seccion.tieneSeccionInformacion())
				seccion.setMostrarModificacion(false);

			// si a la secciï¿½n no se le llenaron campos (Historico) se
			// inhabilita el campo visible
			seccion.setVisible(false);
			
			//contador para validad si todos los campos son tipo texto predeterminado
			// se muestra la sección
			int contador=0;
			for (DtoCampoParametrizable campo : seccion.getCampos())
				if (codigoPaciente > 0 && numeroSolicitud <= 0
						&& codigoEvolucion <= 0
						&& codigoRespuestaProcedimiento <= 0) {
					seccion.setVisible(true);
				} else {
					
					if(campo.getTipo().getCodigo()==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
					{
						contador++;
					}
					if (!campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)&&!campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect)
							&&!campo.getValor().trim().equals("")){
						seccion.setVisible(true);
					}else{
						for (DtoOpcionCampoParam opcionesChek : campo.getOpciones()){
							if(opcionesChek.getSeleccionado().equals(ConstantesBD.acronimoSi)){
								seccion.setVisible(true);
							}
						}
					}
						
				}
			
			if(contador==seccion.getCampos().size()){
				seccion.setVisible(true);
			}
				
			for (DtoSeccionParametrizable subseccion : seccion.getSecciones()) {
				subseccion.setVisible(false);
				for (DtoCampoParametrizable campo : subseccion.getCampos())
					if (codigoPaciente > 0 && numeroSolicitud <= 0
							&& codigoEvolucion <= 0
							&& codigoRespuestaProcedimiento <= 0) {
						seccion.setVisible(true);
						subseccion.setVisible(true);
					} else {
						if (!campo.getValor().trim().equals("")) {
							seccion.setVisible(true);
							subseccion.setVisible(true);
						}
					}
			}

		}

		return seccion;
	}

	// /************************************************************************************
	// ************************************************************************************

	/**
	 * Mï¿½todo que realiza las validaciones para saber si a cada opcion de un
	 * campo se le agrega secciones y/o valores
	 * 
	 * @param con
	 * @param campo
	 * @param consultarRegistro
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoEvolucion
	 * @param codigoRespuestaProcedimiento
	 * @param consecutivoPlantilla
	 * @param consecutivoSeccionFija
	 * @param codigoSexoPaciente
	 * @param diasEdadPaciente
	 * @param filtroDatosPaciente
	 * @param seccionesValores
	 */
	private static void validacionSeccionesValor(Connection con,
			DtoCampoParametrizable campo, boolean consultarRegistro,
			int codigoIngreso, int numeroSolicitud, int codigoPaciente,
			int codigoEvolucion, int codigoRespuestaProcedimiento,
			String consecutivoPlantilla, String consecutivoSeccionFija,
			int codigoSexoPaciente, int diasEdadPaciente,
			boolean filtroDatosPaciente, DtoComponente componente,
			boolean vieneDePlantilla, boolean tomarRegistrosVacios) {
		/*PreparedStatementDecorator pst = null;
		ResultSetDecorator rs2 = null;*/
		try {
			String consulta = "";

			/** Validaciï¿½n especial para los campos tipo select y tipo chequeo **/
			if (campo.getTipoHtml().equals(
					ConstantesCamposParametrizables.campoTipoSelect)
					|| campo.getTipoHtml().equals(
							ConstantesCamposParametrizables.campoTipoRadio)
					|| campo.getTipoHtml().equals(
							ConstantesCamposParametrizables.campoTipoCheckBox)) {
				// Iteracion de las opciones del campo
				for (DtoOpcionCampoParam opcion : campo.getOpciones())
					if (!opcion.getCodigoPk().equals("")) {
						// logger.info("paso por aqui seccion valor A ");
						// Se consultan los valores de la opcion
						PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(strCargarValoresOpcionCampo));
						pst.setInt(1, Integer.parseInt(opcion.getCodigoPk()));
						ResultSetDecorator rs2 = new ResultSetDecorator(pst.executeQuery());
						while (rs2.next()) {
							DtoValorOpcionCampoParam valorOpcion = new DtoValorOpcionCampoParam();
							valorOpcion.setCodigoPk(rs2.getString("codigo_pk"));
							valorOpcion.setCodigoPkOpcion(rs2
									.getString("codigo_pk_opciones"));
							valorOpcion.setValor(rs2.getString("valor"));
							valorOpcion
									.setMostrarModificacion(UtilidadTexto
											.getBoolean(rs2
													.getString("mostrar_modificacion")));
							opcion.getValoresOpcion().add(valorOpcion);
						}
						UtilidadBD.cerrarObjetosPersistencia(pst, rs2, null);

						// Se consultan los cï¿½digos de las secciones
						if (vieneDePlantilla)
							consulta = strCargarPlantillaSeccionesOpcionCampo;
						else
							consulta = strCargarComponenteSeccionesOpcionCampo;
						if (filtroDatosPaciente)
							consulta += " "
									+ " and (sp.sexo IS NULL or sp.sexo = "
									+ codigoSexoPaciente
									+ ")"
									+ " and ((sp.edad_inicial_dias is null and sp.edad_final_dias is null) or ("
									+ diasEdadPaciente
									+ " between sp.edad_inicial_dias and sp.edad_final_dias)) ";

						// logger.info("paso por aqui seccion valor B ");

						// logger.info("\n\nvalor de la consulta >> "+consulta+" >> "+opcion.getCodigoPk());

						pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst.setInt(1, Integer.parseInt(opcion.getCodigoPk()));
						rs2 = new ResultSetDecorator(pst.executeQuery());
						while (rs2.next()) {

							// logger.info("paso por aqui seccion valor C ");
							DtoElementoParam elementoValor = new DtoSeccionParametrizable();
							DtoSeccionParametrizable seccionValor = (DtoSeccionParametrizable) elementoValor;
							seccionValor.setCodigoPkDetSeccion(rs2
									.getString("consecutivo_det_secc"));
							seccionValor
									.setCodigoPK(rs2.getString("codigo_pk"));
							seccionValor.setConsecutivoParametrizacion(rs2
									.getString("consecutivo"));
							seccionValor.setVieneDePlantilla(true);
							seccionValor.setOrden(rs2.getInt("orden"));
							seccionValor
									.setCodigoPK(rs2.getString("codigo_pk"));
							seccionValor.setCodigo(rs2.getString("codigo"));
							seccionValor.setDescripcion(rs2
									.getString("descripcion"));
							seccionValor.setColumnasSeccion(rs2
									.getInt("columnas_seccion"));
							seccionValor.setTipoSeccion(rs2.getString("tipo"));
							seccionValor.setVisible(UtilidadTexto
									.getBoolean(rs2.getString("mostrar")));
							seccionValor
									.setMostrarModificacion(UtilidadTexto
											.getBoolean(rs2
													.getString("mostrar_modificacion")));
							seccionValor.setVieneDePlantilla(vieneDePlantilla);

							if (!rs2.getString("sexo").equals(
									ConstantesBD.codigoNuncaValido + ""))
								seccionValor.setSexoSeccion(rs2
										.getString("sexo"));
							else
								seccionValor.setSexoSeccion("");

							if (rs2.getString("edad_inicial_dias").equals(
									ConstantesBD.codigoNuncaValido + ""))
								seccionValor.setRangoInicialEdad("");
							else
								seccionValor.setRangoInicialEdad(rs2
										.getString("edad_inicial_dias"));
							if (rs2.getString("edad_final_dias").equals(
									ConstantesBD.codigoNuncaValido + ""))
								seccionValor.setRangoFInalEdad("");
							else
								seccionValor.setRangoFInalEdad(rs2
										.getString("edad_final_dias"));

							seccionValor.setIndicativoRestriccionValCamp(rs2
									.getString("restriccion_val_campo"));

							// logger.info("paso por aqui seccion valor D >> "+seccionValor.isMostrarModificacion());

							// Se aï¿½ade seccion a la opcion
							if (!consultarRegistro
									&& seccionValor.isMostrarModificacion()
									|| tomarRegistrosVacios)
								opcion.getSecciones().add(seccionValor);

							// logger.info("paso por aqui seccion valor E ");

						}
						UtilidadBD.cerrarObjetosPersistencia(pst, rs2, null);

					}
			}
		} catch (SQLException e) {
			logger.error("Error en validacionSeccionesValor: " + e);
		}finally {			
			//UtilidadBD.cerrarObjetosPersistencia(pst, rs2, null);		
		}

	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Inserta Resultados Procedimiento Requeridos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean insertarResultadosProcRequeridos(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarResultadosProcRequerido));
			ps.setDouble(1, Utilidades.convertirADouble(parametros
					.get("codigoPkPlantilla")
					+ ""));
			ps.setString(2, parametros.get("requerido") + "");
			ps.setString(3, parametros.get("usuarioModifica") + "");
			ps.setDate(4, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(5, parametros.get("horaModifica") + "");

			if (ps.executeUpdate() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Actualiza Resultados Procedimiento Requeridos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean actualizarResultadosProcRequeridos(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
		try {
			 ps = new PreparedStatementDecorator(con
					.prepareStatement(strActualizarResultadosProRequerido));
			ps.setString(1, parametros.get("requerido") + "");
			ps.setString(2, parametros.get("usuarioModifica") + "");
			ps.setDate(3, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(4, parametros.get("horaModifica") + "");
			ps.setDouble(5, Utilidades.convertirADouble(parametros
					.get("codigoPkPlantilla")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Consultar Resultados Procedimiento Requeridos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static String consultarResultadosProcRequeridos(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try {
			 ps = new PreparedStatementDecorator(con.prepareStatement(strConsultarResultadosProRequerido));
			ps.setDouble(1, Utilidades.convertirADouble(parametros
					.get("codigoPkPlantilla")
					+ ""));

			rs = new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		return "";
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Consulta la informacion basica de Plantillas Respuesta Procedimientos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static InfoDatosString consultarBasicaPlantillasResProc(
			Connection con, HashMap parametros) {
		InfoDatosString datos = new InfoDatosString();
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs  = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strCargarBasicaPlantillaResProc));
			ps.setDouble(1, Utilidades.convertirADouble(parametros
					.get("codigoPkResSolProc")
					+ ""));

			rs = new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				datos.setCodigo(rs.getString(1));
				datos.setId(rs.getString(2));
			} else {
				datos.setCodigo("");
				datos.setId("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		// logger.info("valor de datos >> "+datos.getId()+" >> "+datos.getCodigo());
		return datos;
	}

	// **********************************************************************
	// **********************************************************************

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Atributos Para Secciones Parametrizables ***************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Consulta la informaciï¿½n de la seccion Parametrizable
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static ArrayList<DtoSeccionParametrizable> consultarSeccionesParametrizables(
			Connection con, HashMap parametros) {
		String cadena = strConsultarSeccionesParametrizables;
		ArrayList<DtoSeccionParametrizable> array = new ArrayList<DtoSeccionParametrizable>();
		DtoSeccionParametrizable dto;

		if (parametros.containsKey("codigoPk")
				&& !parametros.get("codigoPk").toString().equals(""))
			cadena += " AND codigo_pk = "
					+ parametros.get("codigoPk").toString();

		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs  = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setObject(1, parametros.get("institucion"));

			rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				dto = new DtoSeccionParametrizable();
				dto.setCodigo(rs.getString("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setOrden(rs.getInt("orden"));
				dto.setColumnasSeccion(rs.getInt("columnas_seccion"));
				dto.setTipoSeccion(rs.getString("tipo"));
				dto.setCodigoPK(rs.getString("codigo_pk"));
				dto.setCodigoSeccionPadre(rs.getString("seccion_padre"));
				dto.setVisible(UtilidadTexto
						.getBoolean(rs.getString("mostrar")));
				dto.setMostrarModificacion(UtilidadTexto.getBoolean(rs
						.getString("mostrar_modificacion")));
				if (rs.getString("sexo").equals(
						ConstantesBD.codigoNuncaValido + ""))
					dto.setSexoSeccion("");
				else
					dto.setSexoSeccion(rs.getString("sexo"));
				if (rs.getString("edad_inicial_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					dto.setRangoInicialEdad("");
				else
					dto.setRangoInicialEdad(rs.getString("edad_inicial_dias"));
				if (rs.getString("edad_final_dias").equals(
						ConstantesBD.codigoNuncaValido + ""))
					dto.setRangoFInalEdad("");
				else
					dto.setRangoFInalEdad(rs.getString("edad_final_dias"));

				dto.setIndicativoRestriccionValCamp(rs
						.getString("restriccion_val_campo"));

				array.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return array;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Inserta informaciï¿½n en secciones parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static int insertarSeccionesParametrizables(Connection con,
			HashMap parametros) {

		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"seq_secciones_param");

		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs  = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarSeccionesParametrizables));

			if (parametros.containsKey("codigo")&& parametros.get("codigo").toString().equals(""))
				ps.setNull(1, Types.INTEGER);
			else
				ps.setDouble(1, Utilidades.convertirADouble(consecutivo+ ""));

			if (parametros.containsKey("descripcion")
					&& !parametros.get("descripcion").toString().equals(""))
				ps.setString(2, parametros.get("descripcion") + "");
			else
				ps.setString(2, " ");

			ps.setInt(3, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros
					.get("columnasSeccion")
					+ ""));
			ps.setString(5, parametros.get("tipo") + "");
			ps.setDouble(6, Utilidades.convertirADouble(consecutivo + ""));

			if (parametros.containsKey("seccionPadre")
					&& !parametros.get("seccionPadre").toString().equals(""))
				ps.setDouble(7, Utilidades.convertirADouble(parametros
						.get("seccionPadre")
						+ ""));
			else
				ps.setNull(7, Types.INTEGER);

			ps.setInt(8, Utilidades.convertirAEntero(parametros
					.get("institucion")
					+ ""));
			ps.setString(9, parametros.get("mostrar") + "");

			ps.setDate(10, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(11, parametros.get("horaModifica") + "");
			ps.setString(12, parametros.get("usuarioModifica") + "");
			ps.setString(13, parametros.get("mostrarModificacion") + "");
			if (parametros.containsKey("sexoSeccion")
					&& !parametros.get("sexoSeccion").toString().equals(""))
				ps.setDouble(14, Utilidades.convertirADouble(parametros
						.get("sexoSeccion")
						+ ""));
			else
				ps.setNull(14, Types.INTEGER);

			if (parametros.containsKey("edadInicialDias")
					&& !parametros.get("edadInicialDias").toString().equals(""))
				ps.setDouble(15, Utilidades.convertirADouble(parametros
						.get("edadInicialDias")
						+ ""));
			else
				ps.setNull(15, Types.INTEGER);

			if (parametros.containsKey("edadFinalDias")
					&& !parametros.get("edadFinalDias").toString().equals(""))
				ps.setDouble(16, Utilidades.convertirADouble(parametros
						.get("edadFinalDias")
						+ ""));
			else
				ps.setNull(16, Types.INTEGER);

			if (!parametros.get("restriccionValCampo").equals(""))
				ps.setString(17, parametros.get("restriccionValCampo") + "");
			else
				ps.setString(17, ConstantesBD.acronimoNo);

			if (ps.executeUpdate() > 0)
				return consecutivo;

		} catch (Exception e) {
			logger.info("parametros >> " + parametros + " >> "
					+ strInsertarSeccionesParametrizables);
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Actualizar secciones parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean actualizarSeccionesParametrizables(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
	
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strActualizarSeccionesParametrizables));

			if (parametros.containsKey("codigo")
					&& !parametros.get("codigo").toString().equals(""))
				ps.setDouble(1, Utilidades.convertirADouble(parametros
						.get("codigo")
						+ ""));
			else
				ps.setNull(1, Types.INTEGER);

			if (parametros.containsKey("descripcion")
					&& !parametros.get("descripcion").toString().equals(""))
				ps.setString(2, parametros.get("descripcion").toString());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.setInt(3, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros
					.get("columnasSeccion")
					+ ""));
			ps.setString(5, parametros.get("tipo").toString());

			ps.setString(6, parametros.get("mostrar").toString());
			ps.setString(7, parametros.get("mostrarModificacion").toString());

			ps.setDate(8, Date.valueOf(UtilidadFecha
					.conversionFormatoFechaABD(parametros
							.get("fechaModificacion")
							+ "")));
			ps.setString(9, parametros.get("horaModifica").toString());
			ps.setString(10, parametros.get("usuarioModifica").toString());
			if (parametros.containsKey("sexoSeccion")
					&& !parametros.get("sexoSeccion").toString().equals(""))
				ps.setDouble(11, Utilidades.convertirADouble(parametros
						.get("sexoSeccion")
						+ ""));
			else
				ps.setNull(11, Types.INTEGER);

			if (parametros.containsKey("edadInicialDias")
					&& !parametros.get("edadInicialDias").toString().equals(""))
				ps.setDouble(12, Utilidades.convertirADouble(parametros
						.get("edadInicialDias")
						+ ""));
			else
				ps.setNull(12, Types.INTEGER);

			if (parametros.containsKey("edadFinalDias")
					&& !parametros.get("edadFinalDias").toString().equals(""))
				ps.setDouble(13, Utilidades.convertirADouble(parametros
						.get("edadFinalDias")
						+ ""));
			else
				ps.setNull(13, Types.INTEGER);

			ps.setString(14, parametros.get("restriccionValCampo") + "");

			ps.setDouble(15, Utilidades.convertirADouble(parametros
					.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		return false;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Actualizar el campo mostrar modificacion en secciones parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean actualizarMostrarModSeccionParam(Connection con,
			HashMap parametros) {
		String cadena = strActualizarMostrarModSeccionParam;

		if (parametros.containsKey("orden")
				&& !parametros.get("orden").equals(""))
			cadena = cadena.replace(ConstantesBD.separadorSplitComplejo,
					" orden = " + parametros.get("orden") + ", ");
		else
			cadena = cadena.replace(ConstantesBD.separadorSplitComplejo, " ");

		// logger.info("valor de parametros >> "+parametros);
		PreparedStatementDecorator ps = null;

		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setString(1, parametros.get("mostrarModificacion") + "");
			// ps.setDate(2,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setDate(2, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(3, parametros.get("horaModifica") + "");
			ps.setString(4, parametros.get("usuarioModifica") + "");
			ps.setDouble(5, Utilidades.convertirADouble(parametros
					.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// ----------------------------------------------------------------------------------------------------
	// ************************************************************************************
	/** Metodos Para Fromularios de Plantillas Procedimientos *****************************/
	// ***********************************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * 1 Mï¿½todo implementado para guardar los campos parametrizables en
	 * plantillas_res_proc
	 * 
	 * @param Connection
	 *            con
	 * @param DtoPlantilla
	 *            plantilla
	 * @param HashMap
	 *            campos
	 */
	public static ResultadoBoolean guardarDatosRespuestaProcedimiento(
			Connection con, DtoPlantilla plantillaDto, HashMap campos) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;

		try {
			// Se verifica primero que todo que se haya ingresado informaciï¿½n
			// en la paramï¿½trico de la plantilla
			if (plantillaDto.tieneInformacion()) {
				// *************CAMPOS************************************************
				int codigoIngreso = Utilidades.convertirAEntero(campos
						.get("codigoIngreso")
						+ "");
				int codigoPkResSolProc = Utilidades.convertirAEntero(campos
						.get("codigoPkResSolProc").toString());

				String fecha = campos.get("fecha").toString();
				String hora = campos.get("hora").toString();
				String loginUsuario = campos.get("usuarioModifica").toString();
				String fechaModifica = campos.get("fechaModifica").toString();
				String horaModifica = campos.get("horaModifica").toString();

				// ********************************************************************

				// ***************************SE INSERTA EL ENCABEZADO DE LA
				// PLANTILLA****************************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasResProc));

				/**
				 * NSERT INTO plantillas_res_proc " + "(codigo_pk," +
				 * "res_sol_proc," + "plantilla," + "fecha," + "hora," +
				 * "usuario_modifica," + "fecha_modifica," + "hora_modifica)
				 * VALUES(?,?,?,?,?,?,?,?)
				 */

				int codigoPkPlantillaResProc = UtilidadBD
						.obtenerSiguienteValorSecuencia(con,
								"seq_plantillas_res_proc");

				pst.setDouble(1, Utilidades
						.convertirADouble(codigoPkPlantillaResProc + ""));
				pst.setDouble(2, Utilidades.convertirADouble(codigoPkResSolProc
						+ ""));
				pst.setDouble(3, Utilidades.convertirADouble(plantillaDto
						.getCodigoPK()));
				pst.setDate(4, Date.valueOf(fecha));
				pst.setString(5, hora);
				pst.setString(6, loginUsuario);
				pst.setDate(7, Date.valueOf(fechaModifica));
				pst.setString(8, horaModifica);

				if (pst.executeUpdate() > 0) {
					// **********************************REGISTRO DE LOS CAMPOS
					// DE CADA ELEMENTO DE LA SECCION
					// FIJA***************************
					// Se itera cada seccion fija
					for (DtoSeccionFija seccionFija : plantillaDto
							.getSeccionesFijas()) {
						// Se itera cada elemento de la seccion fija
						for (DtoElementoParam elemento : seccionFija
								.getElementos()) {
							// Mientras que todo continue siendo exitoso
							if (resultado.isTrue()) {

								// Si el tipo elemento es SECCION:
								// ---------------------------------------------------------------
								if (elemento.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									resultado = insertarCampoPlantillaProcedimiento(
											con, seccion,
											codigoPkPlantillaResProc);
								}
								// Si el tipo elemento es COMPONENTE
								// -----------------------------------------------------------------
								else if (elemento.isComponente()) {
									DtoComponente componente = (DtoComponente) elemento;

									// Se itera cada seccion del componente
									for (DtoElementoParam elemenComp : componente
											.getElementos()) {
										// Se verifica si el elemento del
										// componente es una seccion
										if (elemenComp.isSeccion()) {
											DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemenComp;
											resultado = insertarCampoComponenteRespProc(
													con,
													seccion,
													codigoPkResSolProc,
													codigoPkPlantillaResProc,
													Utilidades
															.convertirAEntero(componente
																	.getConsecutivoParametrizacion()),
													fecha, hora, loginUsuario);
										}
										// Se verifica si el elemento del
										// componente es una escala
										else if (elemenComp.isEscala()) {
											DtoEscala escala = (DtoEscala) elemenComp;
											resultado = insertarEscalaIngresoRespProc(
													con, escala, codigoIngreso,
													codigoPkPlantillaResProc,
													fecha, hora, loginUsuario,
													true);
										}
									}

								}
								// Si el tipo elemento es ESCALA
								// -----------------------------------------------------------------------
								else if (elemento.isEscala()) {
									DtoEscala escala = (DtoEscala) elemento;
									resultado = insertarEscalaIngresoRespProc(
											con, escala, codigoIngreso,
											codigoPkPlantillaResProc, fecha,
											hora, loginUsuario, false);
								}
							} // Fin if resultado

						} // fin for elementos seccion fijas
					} // Fin for secciones fijas de la plantilla
					// *********************************************************************************************************

					// ******************INSERCION DATOS SECCIONES
					// VALORES*****************************************************
					if (resultado.isTrue()) {
						for (DtoElementoParam elemento : plantillaDto
								.getSeccionesValor()) {
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;

							// Se verifica que la secciï¿½n haya sido activada
							// en el formulario y que todo marche bien
							if (seccion
									.estaSeccionValorOpcionActiva(plantillaDto
											.getListadoSeccionesValorActivas())
									&& resultado.isTrue()) {
								// Se pregunta si la seccion viene de la
								// plantilla o viene del componente
								if (seccion.isVieneDePlantilla())
									resultado = insertarCampoPlantillaProcedimiento(
											con, seccion,
											codigoPkPlantillaResProc);
								else
									resultado = insertarCampoComponenteRespProc(
											con,
											seccion,
											codigoPkResSolProc,
											codigoPkPlantillaResProc,
											Utilidades
													.convertirAEntero(plantillaDto
															.obtenerConsecutivoPlantillaComponenteSeccionValor(seccion
																	.getConsecutivoParametrizacion())),
											fecha, hora, loginUsuario);
							}
						}

					}
					// *********************************************************************************************************
				} else {
					resultado.setResultado(false);
					resultado
							.setDescripcion("Problemas realizando el registro inicial de los valores parametrizables de la plantilla");
				}
			}
		} catch (SQLException e) {
			logger.error("Error en guardarValoresParametrizables: " + e);
			resultado.setResultado(false);
			resultado
					.setDescripcion("Problemas Insertando Informacion Parametrizable");
			return resultado;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	/**
	 * 2 Mï¿½todo implementado para insertar un campo de la plantilla
	 * procedimiento
	 * 
	 * @param con
	 * @param seccion
	 * @param consecutivoPlantillaIngreso
	 * @return
	 */
	private static ResultadoBoolean insertarCampoPlantillaProcedimiento(
			Connection con, DtoSeccionParametrizable seccion,
			int codigoPkPlantillaResProc) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst= null;
		
		try {
			for (DtoCampoParametrizable campo : seccion.getCampos())
				// Se verifica que se haya llenado informaciï¿½n del campo
				// dependiendo del tipo HTML:
				if ((// Si no fueron CHECKBOX y la variable VALOR viene llena
						(!campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox)) ||
						// Si fue campo CHECKBOX y se chequeï¿½ al menos una de
						// sus opciones
						(campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox) && campo
								.fueLlenadoCheckBox()))
						&& resultado.isTrue() // mientras que todo marche bien
												// se continua
				)

				{
					// ***************************SE REGISTRA EL
					// CAMPO******************************************************
					pst = new PreparedStatementDecorator(
							con,strInsertarPlantillasProcCampos);

					/**
					 * NSERT INTO plantillas_proc_campos " + "(codigo_pk," +
					 * "plantilla_campo_sec," + "plantilla_res_proc," +
					 * "nombre_archivo_original) " + "VALUES(?,?,?,?)
					 */

					int codigoPkPlantillaProcCampo = UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_plantillas_proc_campos");

					// logger.info("consecutivo de parametrizacion >> "+campo.getConsecutivoParametrizacion()+" >> "+campo.getNombre()+" >> "+campo.getCodigoPK()+" >> valor >> "+campo.getValor());

					pst.setDouble(1, Utilidades
							.convertirADouble(codigoPkPlantillaProcCampo + ""));
					pst.setInt(2, Utilidades.convertirAEntero(campo
							.getConsecutivoParametrizacion()));
					pst.setDouble(3, Utilidades
							.convertirADouble(codigoPkPlantillaResProc + ""));

					// Si el campo fue de tipo archivo debe tener este campo
					// lleno
					if (!campo.getNombreArchivoOriginal().equals(""))
						pst.setString(4, campo.getNombreArchivoOriginal());
					else
						pst.setNull(4, Types.VARCHAR);

					// *******************************************************************************

					if (pst.executeUpdate() > 0) {
						// ***************INSERCION DE LOS VALORES DEL
						// CAMPO*******************************************************
						// Si el campo es de tipo CHECKBOX la forma de registrar
						// su informaciï¿½n es diferente
						if (campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox)
								|| (campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoSelect) && campo
										.getManejaImagen().equals(
												ConstantesBD.acronimoSi))) {
							for (DtoOpcionCampoParam opcion : campo
									.getOpciones()) {
								if ((UtilidadTexto.getBoolean(opcion
										.getSeleccionado()) || opcion
										.getPixelImg().length() > 0)
										&& resultado.isTrue()
										&& !UtilidadTexto.isEmpty(opcion
												.getValor())) {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarValoresPlanProcCamp));

									int codigoPkValPlanProcCamp = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_val_plan_proc_camp");

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(codigoPkValPlanProcCamp
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(codigoPkPlantillaProcCampo
																	+ ""));
									pst.setString(3, opcion.getValor());
									if (!opcion.getValoresOpcionRegistrado()
											.equals(""))
										pst.setString(4, opcion
												.getValoresOpcionRegistrado());
									else
										pst.setNull(4, Types.VARCHAR);

									// Genera la imagen
									InfoDatosStr nombreArchivo = opcion
											.generarImagen(codigoPkValPlanProcCamp
													+ "");

									if (!nombreArchivo.isIndicador()
											&& !nombreArchivo.getCodigo()
													.equals("")) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
														+ opcion.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
										pst.close();
									} else {
										if (!nombreArchivo.equals(""))
											pst.setString(5, nombreArchivo
													.getCodigo());
										else
											pst.setNull(5, Types.VARCHAR);

										if (pst.executeUpdate() <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas registrando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst.close();
										}
									}
								}
							}
						}
						// Si el campo es diferente de tipo CHECKBOX solo tiene
						// un valor para insertar
						else {
							pst = new PreparedStatementDecorator(con
									.prepareStatement(
											strInsertarValoresPlanProcCamp));

							/**
							 * INSERT INTO valores_plan_proc_camp " +
							 * "(codigo_pk," + "plantilla_proc_campos," +
							 * "valor) " + "VALUES(?,?,?)
							 */

							int codigoPkValPlanProcCamp = UtilidadBD
									.obtenerSiguienteValorSecuencia(con,
											"seq_val_plan_proc_camp");

							pst.setDouble(1, Utilidades
									.convertirADouble(codigoPkValPlanProcCamp
											+ ""));
							pst
									.setDouble(
											2,
											Utilidades
													.convertirADouble(codigoPkPlantillaProcCampo
															+ ""));
							pst.setString(3, campo.getValor());
							if (!campo.getValoresOpcion().equals(""))
								pst.setString(4, campo.getValoresOpcion());
							else
								pst.setNull(4, Types.VARCHAR);

							pst.setNull(5, Types.VARCHAR);

							if (pst.executeUpdate() <= 0) {
								resultado.setResultado(false);
								resultado
										.setDescripcion("Problemas registrando el valor "
												+ campo.getValor()
												+ " del campo "
												+ campo.getEtiqueta()
												+ " en la seccion "
												+ seccion.getDescripcion());
							}
						}
						// ******************************************************************
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la informaciï¿½n del campo "
										+ campo.getEtiqueta()
										+ " en la seccion "
										+ seccion.getDescripcion());
					}

				} // Fin for de campos

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoPlantillaProcedimiento(con,
							subseccion, codigoPkPlantillaResProc);
			// *********************************************************************************

		} catch (SQLException e) {
			logger.error("Error en insertarCampoPlantillaIngreso: " + e);
			resultado.setResultado(false);
			resultado
					.setDescripcion("Problemas Insertando Informacion Parametrizable");
			return resultado;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	/**
	 * 3 Mï¿½todo implementado para insertar los campos del componente Respuesta
	 * de Procedimientos
	 * 
	 * @param con
	 * @param seccion
	 * @param hora
	 * @param fecha
	 * @param loginUsuario
	 * @return
	 */
	private static ResultadoBoolean insertarCampoComponenteRespProc(
			Connection con, DtoSeccionParametrizable seccion,
			int codigoPkResSolProc, int codigoPkPlantillaResProc,
			int codigoPkPlantillaComponente, String fecha, String hora,
			String loginUsuario) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		
		try {
			int resp0 = 0, resp1 = 0;

			// ************************************INSERTAN LOS CAMPOS DEL
			// REGISTRO COMPONENTE X
			// INGRESO***********************************************
			for (DtoCampoParametrizable campo : seccion.getCampos())
				// Se verifica que se haya llenado informaciï¿½n del campo
				// dependiendo del tipo HTML:
				if ((// Si no fueron CHECKBOX
						(!campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox)) ||
						// Si fue campo CHECKBOX y se chequeï¿½ al menos una de
						// sus opciones
						(campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox) && campo
								.fueLlenadoCheckBox()))
						&& resultado.isTrue() // tambiï¿½n que todo marche bien
				)

				{
					pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarComponentesResProc));

					/**
					 * INSERT INTO componentes_res_proc " + "(codigo_pk," +
					 * "campo_parametrizable," + "componente_seccion," +
					 * "res_sol_proc," + "nombre_archivo_original," + "fecha," +
					 * "hora," + "usuario_modifica," + "fecha_modifica," +
					 * "hora_modifica) " + "VALUES(?,?,?,?,?,?,?,?,?,?)
					 */

					int codigoPkComponentesResProc = UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_componentes_res_proc");

					pst.setDouble(1, Utilidades
							.convertirADouble(codigoPkComponentesResProc + ""));
					pst.setDouble(2, Utilidades.convertirADouble(campo
							.getCodigoPK()));
					pst.setDouble(3, Utilidades.convertirADouble(seccion
							.getConsecutivoParametrizacion()));
					pst.setDouble(4, Utilidades
							.convertirADouble(codigoPkResSolProc + ""));

					// Aplica para campo que es tipo archivo
					if (!campo.getNombreArchivoOriginal().equals(""))
						pst.setString(5, campo.getNombreArchivoOriginal());
					else
						pst.setNull(5, Types.VARCHAR);

					pst.setDate(6, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(fecha)));
					pst.setString(7, hora);
					pst.setString(8, loginUsuario);
					pst.setDate(9, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(UtilidadFecha
									.getFechaActual())));
					pst.setString(10, UtilidadFecha.getHoraActual());

					resp0 = pst.executeUpdate();

					// ****************SE INSERTAN LOS VALORES DEL
					// CAMPO*************************************
					if (resp0 > 0) {
						// Si el campo es de tipo CHECKBOX la forma de registrar
						// su informaciï¿½n es diferente
						if (campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox)) {
							for (DtoOpcionCampoParam opcion : campo
									.getOpciones())
								if (UtilidadTexto.getBoolean(opcion
										.getSeleccionado())
										&& resultado.isTrue()) {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(strInsertarValoresCompResProc));

									int codigoPkValCompResProc = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_valores_comp_res_proc");

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(codigoPkValCompResProc
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(codigoPkComponentesResProc
																	+ ""));
									pst.setString(3, opcion.getValor());
									if (!opcion.getValoresOpcionRegistrado()
											.equals(""))
										pst.setString(4, opcion
												.getValoresOpcionRegistrado());
									else
										pst.setNull(4, Types.VARCHAR);

									resp1 = pst.executeUpdate();

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el valor "
														+ opcion.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
									}
								}
						}
						// Si el campo es diferente de tipo CHECKBOX solo tiene
						// un valor para insertar
						else {
							pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarValoresCompResProc));

							int codigoPkValCompResProc = UtilidadBD
									.obtenerSiguienteValorSecuencia(con,
											"seq_valores_comp_res_proc");

							pst.setDouble(1, Utilidades
									.convertirADouble(codigoPkValCompResProc
											+ ""));
							pst.setDouble(2, codigoPkComponentesResProc);
							pst.setString(3, campo.getValor());
							if (!campo.getValoresOpcion().equals(""))
								pst.setString(4, campo.getValoresOpcion());
							else
								pst.setNull(4, Types.VARCHAR);
							resp1 = pst.executeUpdate();

							if (resp1 <= 0) {
								resultado.setResultado(false);
								resultado
										.setDescripcion("Problemas registrando el valor "
												+ campo.getValor()
												+ " del campo "
												+ campo.getEtiqueta()
												+ " en la seccion "
												+ seccion.getDescripcion());
							}
						}

						// **************REGISTRO DE LA RELACION DEL CAMPO DEL
						// COMPONENTE CON LA
						// PLANTILLA*********************************
						if (resultado.isTrue()) {
							pst = new PreparedStatementDecorator(con
									.prepareStatement(
											strInsertarPlantillasCompResProc));

							/**
							 * INSERT INTO plantillas_comp_res_proc " +
							 * "(plantilla_res_proc," + "plantilla_componente,"
							 * + "componente_res_proc) " + "VALUES(?,?,?)
							 */

							pst.setDouble(1, Utilidades
									.convertirADouble(codigoPkPlantillaResProc
											+ ""));
							pst.setInt(2, codigoPkPlantillaComponente);
							pst
									.setDouble(
											3,
											Utilidades
													.convertirADouble(codigoPkComponentesResProc
															+ ""));

							if (pst.executeUpdate() <= 0) {
								resultado.setResultado(false);
								resultado
										.setDescripcion("Problemas registrando el campo "
												+ campo.getEtiqueta()
												+ " en la seccion "
												+ seccion.getDescripcion()
												+ " de un componente en la plantilla");
							}
						}
						// *****************************************************************************************************************

					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la informaciï¿½n del campo "
										+ campo.getEtiqueta()
										+ " en la seccion "
										+ seccion.getDescripcion());
					}
					// ***************************************************************************************

				} // Fin FOR
			// **************************************************************************************************************************************************

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoComponenteRespProc(con,
							subseccion, codigoPkResSolProc,
							codigoPkPlantillaResProc,
							codigoPkPlantillaComponente, fecha, hora,
							loginUsuario);

			// *********************************************************************************
			// *****************************************************************************
		} catch (SQLException e) {
			logger.error("Error en insertarCampoComponenteIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}
		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	/**
	 * 4 Mï¿½todo para registrar los valores de la escala x ingreso de la
	 * Respuesta de Procedimientos
	 * 
	 * @param con
	 * @param escala
	 * @param codigoIngreso
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param consecutivoPlantillaIngreso
	 * @param boolean vieneDeComponente
	 * @return
	 */
	private static ResultadoBoolean insertarEscalaIngresoRespProc(
			Connection con, DtoEscala escala, int codigoIngreso,
			int codigoPkPlantillaResProc, String fecha, String hora,
			String loginUsuario, boolean vieneDeComponente) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		
		try {
			int resp0 = 0, resp1 = 0;

			// Se verifica si la escala fue diligenciada
			if (escala.getTotalEscala() > 0) {

				// *****************SE INSERTA EL ENCABEZADO DEL REGISTRO DE LA
				// ESCALA X INGRESO**********************************
				pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarEscalaIngreso));

				/**
				 * INSERT INTO escalas_ingresos " + "(codigo_pk," + "ingreso," +
				 * "escala," + "escala_factor_prediccion," + "valor_total," +
				 * "observaciones," + "fecha," + "hora," + "fecha_modifica," +
				 * "hora_modifica," + "usuario_modifica) " + "VALUES " +
				 * "(?,?,?,?,?,?,?,?,CURRENT_DATE,"
				 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
				 */

				int consecutivoEscalaIngreso = UtilidadBD
						.obtenerSiguienteValorSecuencia(con,
								"seq_escalas_ingresos");
				pst.setDouble(1, Utilidades
						.convertirADouble(consecutivoEscalaIngreso + ""));
				pst.setInt(2, codigoIngreso);
				pst.setDouble(3, Utilidades.convertirADouble(escala
						.getCodigoPK()));
				if (!escala.getCodigoFactorPrediccion().equals(""))
					pst.setDouble(4, Utilidades.convertirADouble(escala
							.getCodigoFactorPrediccion()));
				else
					pst.setNull(4, Types.NUMERIC);
				pst.setDouble(5, escala.getTotalEscala());
				if (!escala.getObservaciones().equals(""))
					pst.setString(6, escala.getObservaciones());
				else
					pst.setNull(6, Types.VARCHAR);
				pst.setDate(7, Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(fecha)));
				pst.setString(8, hora);
				pst.setString(9, loginUsuario);

				resp0 = pst.executeUpdate();
				// ****************************************************************************************************************

				if (resp0 > 0) {

					// Se iteran las secciones y los campos de la escala
					for (DtoSeccionParametrizable seccion : escala
							.getSecciones())
						for (DtoCampoParametrizable campo : seccion.getCampos())
							if ((Utilidades.convertirADouble(campo.getValor()) > 0 || !campo
									.getObservaciones().trim().equals(""))
									&& resultado.isTrue()) {
								// *****************SE INSERTA EL CAMPO DE CADA
								// ESCALA**************************************
								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarEscalaCampoIngreso));

								/**
								 * INSERT INTO escalas_campos_ingresos " +
								 * "(codigo_pk," + "escala_campo_seccion," +
								 * "escala_ingreso," + "valor," +
								 * "observaciones) " + "VALUES " + "(?,?,?,?,?)
								 */

								int consecutivoEscalaCampoIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_escalas_campos_ing");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoEscalaCampoIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(campo
																.getConsecutivoParametrizacion()));
								pst
										.setDouble(
												3,
												Utilidades
														.convertirADouble(consecutivoEscalaIngreso
																+ ""));
								pst.setDouble(4, Utilidades.convertirADouble(
										campo.getValor(), true));
								if (!campo.getObservaciones().equals(""))
									pst.setString(5, campo.getObservaciones());
								else
									pst.setNull(5, Types.VARCHAR);

								resp1 = pst.executeUpdate();

								// *****************************************************************************************

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el campo "
													+ campo.getEtiqueta()
													+ " la escala "
													+ escala.getDescripcion());
								}
							}
				} else {
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas registrando la escala "
							+ escala.getDescripcion());
				}

				// ***********************SE RELACIONA EL REGISTRO DE LA ESCALA
				// CON LA PLANTILLA INGRESO*******************************
				if (resultado.isTrue()) {
					pst = new PreparedStatementDecorator(con.prepareStatement(
							strInsertarPlantillasResProcIng));

					/**
					 * INSERT INTO plantillas_escala_res_proc " +
					 * "(plantilla_res_proc," + "escalas_ingreso," +
					 * "plantillas_escala," + "componente," + "escala) " +
					 * "VALUES(?,?,?,?,?)
					 */

					pst.setDouble(1, Utilidades
							.convertirADouble(codigoPkPlantillaResProc + ""));
					pst.setDouble(2, Utilidades
							.convertirADouble(consecutivoEscalaIngreso + ""));

					// Dependiendo si la escala hace parte o no de un componente
					// se relaciona de forma diferente
					if (vieneDeComponente) {
						pst.setNull(3, Types.INTEGER);
						String[] vectorConsecutivo = escala
								.getConsecutivoParametrizacion().split(
										ConstantesBD.separadorSplit);
						pst.setDouble(4, Utilidades
								.convertirADouble(vectorConsecutivo[0]));
						pst.setDouble(5, Utilidades
								.convertirADouble(vectorConsecutivo[1]));
					} else {
						pst.setInt(3, Utilidades.convertirAEntero(escala
								.getConsecutivoParametrizacion()));
						pst.setNull(4, Types.NUMERIC);
						pst.setNull(5, Types.NUMERIC);
					}

					resp0 = pst.executeUpdate();

					if (resp0 <= 0) {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas al relacionar la escala "
										+ escala.getDescripcion()
										+ " con el registro de la plantilla");
					}
				}
				// ********************************************************************************************************************

				// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE LA
				// ESCALA*****************************************************
				if (resultado.isTrue()) {
					for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
						String adjEscala = "adjEscala" + escala.getCodigoPK()
								+ "_" + i;
						String adjEscalaOriginal = "adjEscalaOriginal"
								+ escala.getCodigoPK() + "_" + i;

						if (escala.getArchivosAdjuntos(adjEscala) != null
								&& !escala.getArchivosAdjuntos(adjEscala)
										.toString().equals("")
								&& resultado.isTrue()) {

							pst = new PreparedStatementDecorator(con
									.prepareStatement(strInsertarAdjuntosEscalaIngreso));

							/**
							 * INSERT INTO escalas_adjuntos_ingreso " +
							 * "(codigo_pk," + "escala_ingreso," +
							 * "nombre_archivo," + "nombre_original) " +
							 * "VALUES " + "(?,?,?,?)"
							 */

							int consecutivoAdjuntoEscalaIngreso = UtilidadBD
									.obtenerSiguienteValorSecuencia(con,
											"seq_escalas_adjuntos_ing");
							pst
									.setDouble(
											1,
											Utilidades
													.convertirADouble(consecutivoAdjuntoEscalaIngreso
															+ ""));
							pst.setDouble(2, Utilidades
									.convertirADouble(consecutivoEscalaIngreso
											+ ""));
							pst.setString(3, escala.getArchivosAdjuntos(
									adjEscala).toString());
							pst.setString(4, escala.getArchivosAdjuntos(
									adjEscalaOriginal).toString());

							resp0 = pst.executeUpdate();

							if (resp0 <= 0) {
								resultado.setResultado(false);
								resultado
										.setDescripcion("Problemas ingresando el archivo adjunto "
												+ escala
														.getArchivosAdjuntos(adjEscalaOriginal)
												+ " de la escala "
												+ escala.getDescripcion());
							}
						}
					}
				}
				// **********************************************************************************************************************
			}
		} catch (SQLException e) {
			logger.error("Error en insertarEscalaIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}
		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	// //Aqui vamos.

	/**
	 * Metodo para guardar la informacion parametrizable de la evolucion.
	 */
	public static ResultadoBoolean guardarDatosEvolucion(Connection con,
			DtoPlantilla plantillaDto, HashMap campos) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		
		try {

			// Se verifica primero que todo que se haya ingresado informaciï¿½n
			// en la paramï¿½trico de la plantilla
			if (plantillaDto.tieneInformacion()) {
				// *************CAMPOS************************************************
				int codigoIngreso = Utilidades.convertirAEntero(campos
						.get("codigoIngreso")
						+ "");
				int codigoPkEvolucion = Utilidades.convertirAEntero(campos.get(
						"codigoPkEvolucion").toString());

				String fecha = campos.get("fecha").toString();
				String hora = campos.get("hora").toString();
				String loginUsuario = campos.get("usuarioModifica").toString();
				String fechaModifica = campos.get("fechaModifica").toString();
				String horaModifica = campos.get("horaModifica").toString();

				// ********************************************************************

				// ***************************SE INSERTA EL ENCABEZADO DE LA
				// PLANTILLA****************************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasEvolucion));

				logger.info("Codigo Plantilla>>>>>>>>>>>>>>>>"
						+ plantillaDto.getCodigoPK());

				/**
				 * INSERT INTO plantillas_evolucion " + "(codigo_pk," +
				 * "evolucion," + "plantilla," + "fecha," + "hora," +
				 * "usuario_modifica," + "fecha_modifica," + "hora_modifica)
				 * VALUES(?,?,?,?,?,?,?,?)
				 */

				int codigoPkPlantillaEvolucion = UtilidadBD
						.obtenerSiguienteValorSecuencia(con,
								"seq_plantillas_evolucion");

				pst.setDouble(1, Utilidades
						.convertirADouble(codigoPkPlantillaEvolucion + ""));
				pst.setInt(2, codigoPkEvolucion);
				pst.setDouble(3, Utilidades.convertirADouble(plantillaDto
						.getCodigoPK()));
				pst.setDate(4, Date.valueOf(fecha));
				pst.setString(5, hora);
				pst.setString(6, loginUsuario);
				pst.setDate(7, Date.valueOf(fechaModifica));
				pst.setString(8, horaModifica);
				// Se agrega este campo para la evolucion odontologia, como
				// poeude ser valoracion normal u odontologica, para la evol.
				// normal se pone null el campo
				pst.setNull(9, Types.DOUBLE);
				pst.setString(10, loginUsuario);

				if (pst.executeUpdate() > 0) {
					// **********************************REGISTRO DE LOS CAMPOS
					// DE CADA ELEMENTO DE LA SECCION
					// FIJA***************************
					// Se itera cada seccion fija
					for (DtoSeccionFija seccionFija : plantillaDto
							.getSeccionesFijas()) {
						// Se itera cada elemento de la seccion fija
						for (DtoElementoParam elemento : seccionFija
								.getElementos()) {
							// Mientras que todo continue siendo exitoso
							if (resultado.isTrue()) {

								// Si el tipo elemento es SECCION:
								// ---------------------------------------------------------------
								if (elemento.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									resultado = insertarCampoPlantillaEvolucion(
											con, seccion,
											codigoPkPlantillaEvolucion, false);
								}
								// Si el tipo elemento es COMPONENTE
								// -----------------------------------------------------------------
								else if (elemento.isComponente()) {
									DtoComponente componente = (DtoComponente) elemento;

									// Se itera cada seccion del componente
									for (DtoElementoParam elemenComp : componente
											.getElementos()) {
										// Se verifica si el elemento del
										// componente es una seccion
										if (elemenComp.isSeccion()) {
											DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemenComp;
											resultado = insertarCampoComponenteEvolucion(
													con,
													seccion,
													codigoPkEvolucion,
													ConstantesBD.codigoNuncaValido,
													codigoPkPlantillaEvolucion,
													Utilidades
															.convertirAEntero(componente
																	.getConsecutivoParametrizacion()),
													fecha, hora, loginUsuario,
													false);
										}
										// Se verifica si el elemento del
										// componente es una escala
										else if (elemenComp.isEscala()) {
											DtoEscala escala = (DtoEscala) elemenComp;
											resultado = insertarEscalaIngresoEvolucion(
													con, escala, codigoIngreso,
													codigoPkPlantillaEvolucion,
													fecha, hora, loginUsuario,
													true, false);
										}
									}

								}
								// Si el tipo elemento es ESCALA
								// -----------------------------------------------------------------------
								else if (elemento.isEscala()) {
									DtoEscala escala = (DtoEscala) elemento;
									resultado = insertarEscalaIngresoEvolucion(
											con, escala, codigoIngreso,
											codigoPkPlantillaEvolucion, fecha,
											hora, loginUsuario, false, false);
								}
							} // Fin if resultado

						} // fin for elementos seccion fijas
					} // Fin for secciones fijas de la plantilla
					// *********************************************************************************************************

					// /******************INSERCION DATOS SECCIONES
					// VALORES*****************************************************
					if (resultado.isTrue()) {
						for (DtoElementoParam elemento : plantillaDto
								.getSeccionesValor()) {
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;

							// Se verifica que la secciï¿½n haya sido activada
							// en el formulario y que todo marche bien
							if (seccion
									.estaSeccionValorOpcionActiva(plantillaDto
											.getListadoSeccionesValorActivas())
									&& resultado.isTrue()) {
								// Se pregunta si la seccion viene de la
								// plantilla o viene del componente
								if (seccion.isVieneDePlantilla())
									resultado = insertarCampoPlantillaEvolucion(
											con, seccion,
											codigoPkPlantillaEvolucion, false);
								else
									resultado = insertarCampoComponenteEvolucion(
											con,
											seccion,
											codigoPkEvolucion,
											ConstantesBD.codigoNuncaValido,
											codigoPkPlantillaEvolucion,
											Utilidades
													.convertirAEntero(plantillaDto
															.obtenerConsecutivoPlantillaComponenteSeccionValor(seccion
																	.getConsecutivoParametrizacion())),
											fecha, hora, loginUsuario, false);
							}
						}

					}
					// *********************************************************************************************************

				} else {
					resultado.setResultado(false);
					resultado
							.setDescripcion("Problemas realizando el registro inicial de los valores parametrizables de la plantilla");
				}
			}
		} catch (SQLException e) {
			logger.error("Error en guardarValoresParametrizables: " + e);
			resultado.setResultado(false);
			resultado
					.setDescripcion("Problemas Insertando Informacion Parametrizable");
			return resultado;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @param codigoPkPlantillaEvolucion
	 * @return
	 */
	private static ResultadoBoolean insertarCampoPlantillaEvolucion(
			Connection con, DtoSeccionParametrizable seccion,
			int codigoPkPlantillaEvolucion, boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		PreparedStatementDecorator pst1 = null;
		
		try {
			for (DtoCampoParametrizable campo : seccion.getCampos())
				// Se verifica que se haya llenado informaciï¿½n del campo
				// dependiendo del tipo HTML:
				if ((// Si no fueron CHECKBOX y la variable VALOR viene llena
						(!campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox))
								||
								// Si fue campo CHECKBOX y se chequeï¿½ al menos
								// una de sus opciones
								(campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox) && campo
										.fueLlenadoCheckBox()) ||
						// No importa que tipo de campo sea se guardarï¿½
						esModificable)
						&& resultado.isTrue() // mientras que todo marche bien
												// se continua
				)

				{

					if (Utilidades.convertirAEntero(campo
							.getConsecutivoHistorico()) > 0) {
						int codigoPkPlantillaEvoCampo = Utilidades
								.convertirAEntero(campo
										.getConsecutivoHistorico());
						// **************************SE MODIFICA EL
						// CAMPO**********************************************
						pst = new PreparedStatementDecorator(
								con, strModificarPlantillasEvoCampos);
						// /Si el campo fue de tipo archivo debe tener este
						// campo lleno
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(1, campo.getNombreArchivoOriginal());
						else
							pst.setNull(1, Types.VARCHAR);
						pst.setInt(2, codigoPkPlantillaEvoCampo);

						if (pst.executeUpdate() > 0) {
							// *************MODIFICACION DE LOS VALORES DEL
							// CAMPO*********************************************
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {
									if ((UtilidadTexto.getBoolean(opcion.getSeleccionado())
											|| opcion.getPixelImg().length() > 0 || esModificable)
											&& resultado.isTrue()
											&& !UtilidadTexto.isEmpty(opcion.getValor())) {

										pst = new PreparedStatementDecorator(con, strModificarValoresPlanEvoCamp);

										int codigoPkValPlanEvoCamp = Utilidades.convertirAEntero(opcion.getCodigoHistorico());
										pst.setString(1, opcion.getValor());
										if (!opcion.getValoresOpcionRegistrado().equals(""))
											pst.setString(2, opcion.getValoresOpcionRegistrado());
										else
											pst.setNull(2, Types.VARCHAR);

										// Genera la imagen
										InfoDatosStr nombreArchivo = opcion
												.generarImagen(codigoPkValPlanEvoCamp
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst.close();
										} else {
											if (!nombreArchivo.equals(""))
												pst.setString(3, nombreArchivo
														.getCodigo());
											else
												pst.setNull(3, Types.VARCHAR);

											// Campos agregados por valoracion
											// odon Anexo 870
											if (!opcion.getSeleccionado()
													.equals(""))
												pst.setString(4, opcion
														.getSeleccionado());
											else
												pst.setNull(4, Types.VARCHAR);
											if (Utilidades
													.convertirAEntero(opcion
															.getConvencionOdon()
															.getCodigo()) > 0)
												pst
														.setInt(
																5,
																Utilidades
																		.convertirAEntero(opcion
																				.getConvencionOdon()
																				.getCodigo()));
											else
												pst.setNull(5, Types.INTEGER);
											pst.setInt(6,
													codigoPkValPlanEvoCamp);

											if (pst.executeUpdate() <= 0) {
												resultado.setResultado(false);
												resultado
														.setDescripcion("Problemas actualizando el valor "
																+ opcion
																		.getValor()
																+ " del campo "
																+ campo
																		.getEtiqueta()
																+ " en la seccion "
																+ seccion
																		.getDescripcion());
												
											}
										}
									}
								}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								pst1 = new PreparedStatementDecorator(
										con, strModificarValoresPlanEvoCamp);

								//int codigoPkValPlanEvoCamp = Utilidades.convertirAEntero(campo.getConsecutivoHistorico());

								pst1.setString(1, campo.getValor());
								if (!campo.getValoresOpcion().equals(""))
									pst1.setString(2, campo.getValoresOpcion());
								else
									pst1.setNull(2, Types.VARCHAR);

								pst1.setNull(3, Types.VARCHAR);
								pst1.setNull(4, Types.VARCHAR);
								pst1.setNull(5, Types.INTEGER);
								pst1.setInt(6, campo.getCodigoPkValorPlanEvoCamp());
								
								logger.info("------ Slq -- " + pst1);
								
								if (pst1.executeUpdate() <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}
							// ***********************************************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}
						// *********************************************************************************************
					} else {
						// /***************************SE REGISTRA EL
						// CAMPO******************************************************
						pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasEvoCampos));

						/**
						 * INSERT INTO plantillas_evo_campos " + "(codigo_pk," +
						 * "plantilla_campo_sec,"+ "plantilla_evolucion," +
						 * "nombre_archivo_original) " + "VALUES(?,?,?,?)
						 */

						int codigoPkPlantillaEvoCampo = UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_plantillas_evo_campos");

						// logger.info("consecutivo de parametrizacion >> "+campo.getConsecutivoParametrizacion()+" >> "+campo.getNombre()+" >> "+campo.getCodigoPK()+" >> valor >> "+campo.getValor());

						pst.setDouble(1, Utilidades
								.convertirADouble(codigoPkPlantillaEvoCampo
										+ ""));
						pst.setInt(2, Utilidades.convertirAEntero(campo
								.getConsecutivoParametrizacion()));
						pst.setDouble(3, Utilidades
								.convertirADouble(codigoPkPlantillaEvolucion
										+ ""));

						// Si el campo fue de tipo archivo debe tener este campo
						// lleno
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(4, campo.getNombreArchivoOriginal());
						else
							pst.setNull(4, Types.VARCHAR);

						// *******************************************************************************

						if (pst.executeUpdate() > 0) {
							// ***************INSERCION DE LOS VALORES DEL
							// CAMPO*******************************************************
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente

							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {
									if ((UtilidadTexto.getBoolean(opcion
											.getSeleccionado())
											|| opcion.getPixelImg().length() > 0 || esModificable)
											&& resultado.isTrue()
											&& !UtilidadTexto.isEmpty(opcion
													.getValor())) {

										pst = new PreparedStatementDecorator(
												con
														.prepareStatement(
																strInsertarValoresPlanEvoCamp));

										int codigoPkValPlanEvoCamp = UtilidadBD
												.obtenerSiguienteValorSecuencia(
														con,
														"seq_valores_plan_evo_camp");

										pst.setInt(1, codigoPkValPlanEvoCamp);
										pst
												.setInt(2,
														codigoPkPlantillaEvoCampo);
										pst.setString(3, opcion.getValor());
										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst
													.setString(
															4,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst.setNull(4, Types.VARCHAR);

										// Genera la imagen
										InfoDatosStr nombreArchivo = opcion
												.generarImagen(codigoPkValPlanEvoCamp
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst.close();
										} else {
											if (!nombreArchivo.equals(""))
												pst.setString(5, nombreArchivo
														.getCodigo());
											else
												pst.setNull(5, Types.VARCHAR);

											// Campos agregados por valoracion
											// odon Anexo 870
											if (!opcion.getSeleccionado()
													.equals(""))
												pst.setString(6, opcion
														.getSeleccionado());
											else
												pst.setNull(6, Types.VARCHAR);
											if (Utilidades
													.convertirAEntero(opcion
															.getConvencionOdon()
															.getCodigo()) > 0)
												pst
														.setInt(
																7,
																Utilidades
																		.convertirAEntero(opcion
																				.getConvencionOdon()
																				.getCodigo()));
											else {
												logger.info("--------- Aca 1");
												//pst.setNull(7, Types.INTEGER);
												//cambio de Armando
												pst.setObject(7, null);
											}

											if (pst.executeUpdate() <= 0) {
												resultado.setResultado(false);
												resultado
														.setDescripcion("Problemas registrando el valor "
																+ opcion
																		.getValor()
																+ " del campo "
																+ campo
																		.getEtiqueta()
																+ " en la seccion "
																+ seccion
																		.getDescripcion());
												
											}
										}
									}
								}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								logger.info("sQL --> "+ strInsertarValoresPlanEvoCamp);
								pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarValoresPlanEvoCamp));

								int codigoPkValPlanEvoCamp = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_valores_plan_evo_camp");

								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(codigoPkValPlanEvoCamp
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(codigoPkPlantillaEvoCampo
																+ ""));
								pst.setString(3, campo.getValor());
								if (!campo.getValoresOpcion().equals(""))
									pst.setString(4, campo.getValoresOpcion());
								else
									pst.setNull(4, Types.VARCHAR);

								
								logger.info("--------- Aca 2");
								
								pst.setNull(5, Types.VARCHAR);
								pst.setNull(6, Types.VARCHAR);
								//pst.setNull(7, Types.INTEGER);
								//cambio de Armando
								pst.setObject(7, null);
								
								if (pst.executeUpdate() <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}
							// ******************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}
					}

				} // Fin for de campos

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoPlantillaEvolucion(con,
							subseccion, codigoPkPlantillaEvolucion,
							esModificable);
			// *********************************************************************************

		} catch (SQLException e) {
			logger.error("Error en insertarCampoPlantillaIngreso: ", e);
			resultado.setResultado(false);
			resultado
					.setDescripcion("Problemas Insertando Informacion Parametrizable");
			return resultado;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(pst1!=null){
				try{
					pst1.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @param codigoPkEvolucion
	 * @param codigoPkPlantillaEvolucion
	 * @param codigoPkPlantillaComponente
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @return
	 */
	private static ResultadoBoolean insertarCampoComponenteEvolucion(
			Connection con, DtoSeccionParametrizable seccion,
			int codigoPkEvolucion, int codigoPkEvolucionOdo,
			int codigoPkPlantillaEvolucion, int codigoPkPlantillaComponente,
			String fecha, String hora, String loginUsuario,
			boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		
		
		try {
			int resp0 = 0, resp1 = 0;

			// ************************************INSERTAN LOS CAMPOS DEL
			// REGISTRO COMPONENTE X
			// INGRESO***********************************************
			for (DtoCampoParametrizable campo : seccion.getCampos())
				// Se verifica que se haya llenado informaciï¿½n del campo
				// dependiendo del tipo HTML:
				if ((// Si no fueron CHECKBOX
						(!campo
								.getTipoHtml()
								.equals(
										ConstantesCamposParametrizables.campoTipoCheckBox))
								||
								// Si fue campo CHECKBOX y se chequeï¿½ al menos
								// una de sus opciones
								(campo
										.getTipoHtml()
										.equals(
												ConstantesCamposParametrizables.campoTipoCheckBox) && campo
										.fueLlenadoCheckBox()) ||
						// De todas formas sin importar el tipo de campo se
						// registra
						esModificable)
						&& resultado.isTrue() // tambiï¿½n que todo marche bien
				)

				{
					if (Utilidades.convertirAEntero(campo.getConsecutivoHistorico()) > 0 && existeComponenteEnEvolucion(con,Utilidades.convertirAEntero(campo.getConsecutivoHistorico()))) 
					{
						// ************************************MODIFICACION DEL
						// CAMPO DEL COMPONENTE****************************
						pst = new PreparedStatementDecorator(con.prepareStatement(strModificarComponenteEvolucion));

						int codigoPkComponentesEvolucion = Utilidades
								.convertirAEntero(campo
										.getConsecutivoHistorico());
						// Aplica para campo que es tipo archivo
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(1, campo.getNombreArchivoOriginal());
						else
							pst.setNull(1, Types.VARCHAR);
						pst.setDate(2, Date.valueOf(UtilidadFecha
								.conversionFormatoFechaABD(fecha)));
						pst.setString(3, hora);
						pst.setString(4, loginUsuario);
						pst.setInt(5, codigoPkComponentesEvolucion);

						resp0 = pst.executeUpdate();

						// ****************SE MODIFICAN LOS VALORES DEL
						// CAMPO*************************************
						if (resp0 > 0) {
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones())
									if ((UtilidadTexto.getBoolean(opcion
											.getSeleccionado()) || esModificable)
											&& resultado.isTrue()) {
										pst = new PreparedStatementDecorator(con.prepareStatement(strModificarValoresCompEvolucion01));

										int codigoPkValCompEvolucion = Utilidades
												.convertirAEntero(opcion
														.getCodigoHistorico());

										pst.setString(1, opcion.getValor());
										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst
													.setString(
															2,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst.setNull(2, Types.VARCHAR);
										if (opcion.getSeleccionado().equals(""))
											pst.setNull(3, Types.VARCHAR);
										else
											pst.setString(3, opcion
													.getSeleccionado());
										pst.setInt(4, codigoPkValCompEvolucion);

										resp1 = pst.executeUpdate();

										if (resp1 <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas actualizando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
										}
									}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								pst = new PreparedStatementDecorator(con.prepareStatement(strModificarValoresCompEvolucion02));

								int codigoPkValCompEvolucion = Utilidades
										.convertirAEntero(campo
												.getConsecutivoHistorico());

								pst.setString(1, campo.getValor());
								if (!campo.getValoresOpcion().equals(""))
									pst.setString(2, campo.getValoresOpcion());
								else
									pst.setNull(2, Types.VARCHAR);
								pst.setNull(3, Types.VARCHAR);

								pst.setInt(4, codigoPkValCompEvolucion);

								resp1 = pst.executeUpdate();

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas actualizando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}

							// *****************************************************************************************************************

						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}

						// *****************************************************************************************************
					} else {
						// ****************************INSERCION DEL CAMPO DEL
						// COMPONENTE***************************************
						pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarComponentesEvolucion));

						/**
						 * INSERT INTO componentes_evolucion " + "(codigo_pk," +
						 * "campo_parametrizable," + "componente_seccion," +
						 * "evolucion," + "nombre_archivo_original," + "fecha,"
						 * + "hora," + "usuario_modifica," + "fecha_modifica," +
						 * "hora_modifica, evolucion_odonto, generar_alerta) " +
						 * "VALUES(?,?,?,?,?,?,?,?,?,?)
						 */

						int codigoPkComponentesEvolucion = UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_componentes_evolucion");

						pst.setDouble(1, Utilidades
								.convertirADouble(codigoPkComponentesEvolucion
										+ ""));
						pst.setDouble(2, Utilidades.convertirADouble(campo
								.getCodigoPK()));
						pst.setDouble(3, Utilidades.convertirADouble(seccion
								.getConsecutivoParametrizacion()));
						if (codigoPkEvolucion > 0)
							pst.setInt(4, codigoPkEvolucion);
						else
							pst.setNull(4, Types.INTEGER);

						// Aplica para campo que es tipo archivo
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(5, campo.getNombreArchivoOriginal());
						else
							pst.setNull(5, Types.VARCHAR);

						pst.setDate(6, Date.valueOf(UtilidadFecha
								.conversionFormatoFechaABD(fecha)));
						pst.setString(7, hora);
						pst.setString(8, loginUsuario);

						if (codigoPkEvolucionOdo > 0)
							pst.setInt(9, codigoPkEvolucionOdo);
						else
							pst.setNull(9, Types.INTEGER);
						if (campo.getGenerarAlerta().equals(""))
							pst.setString(10, ConstantesBD.acronimoNo);
						else
							pst.setString(10, campo.getGenerarAlerta());

						resp0 = pst.executeUpdate();

						// ****************SE INSERTAN LOS VALORES DEL
						// CAMPO*************************************
						if (resp0 > 0) {
							campo
									.setConsecutivoHistorico(codigoPkComponentesEvolucion
											+ "");
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones())
									if ((UtilidadTexto.getBoolean(opcion
											.getSeleccionado()) || esModificable)
											&& resultado.isTrue()) {
										pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarValoresCompEvolucion));

										/**
										 * INSERT INTO valores_comp_evolucion "
										 * + "(codigo_pk," +
										 * "componente_evolucion," + "valor) " +
										 * "VALUES(?,?,?)
										 */

										int codigoPkValCompEvolucion = UtilidadBD
												.obtenerSiguienteValorSecuencia(
														con,
														"seq_valores_comp_evolucion");

										pst
												.setDouble(
														1,
														Utilidades
																.convertirADouble(codigoPkValCompEvolucion
																		+ ""));
										pst
												.setDouble(
														2,
														Utilidades
																.convertirADouble(codigoPkComponentesEvolucion
																		+ ""));
										pst.setString(3, opcion.getValor());
										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst
													.setString(
															4,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst.setNull(4, Types.VARCHAR);
										if (opcion.getSeleccionado().equals(""))
											pst.setNull(5, Types.VARCHAR);
										else
											pst.setString(5, opcion
													.getSeleccionado());

										resp1 = pst.executeUpdate();

										if (resp1 <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas registrando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
										} else {
											opcion
													.setCodigoHistorico(codigoPkValCompEvolucion
															+ "");
										}
									}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarValoresCompEvolucion));

								/**
								 * INSERT INTO valores_comp_evolucion " +
								 * "(codigo_pk," + "componente_evolucion," +
								 * "valor) " + "VALUES(?,?,?)"
								 */

								int codigoPkValCompEvolucion = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_valores_comp_evolucion");

								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(codigoPkValCompEvolucion
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(codigoPkComponentesEvolucion
																+ ""));
								pst.setString(3, campo.getValor());
								if (!campo.getValoresOpcion().equals(""))
									pst.setString(4, campo.getValoresOpcion());
								else
									pst.setNull(4, Types.VARCHAR);
								pst.setNull(5, Types.VARCHAR);
								resp1 = pst.executeUpdate();

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}

							// **************REGISTRO DE LA RELACION DEL CAMPO
							// DEL COMPONENTE CON LA
							// PLANTILLA*********************************
							if (resultado.isTrue()) {
								pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasCompEvo));

								/**
								 * INSERT INTO plantillas_comp_evo " +
								 * "(plantilla_evolucion," +
								 * "plantilla_componente," +
								 * "componente_evolucion) " + "VALUES(?,?,?)
								 */

								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(codigoPkPlantillaEvolucion
																+ ""));
								pst.setInt(2, codigoPkPlantillaComponente);
								pst
										.setDouble(
												3,
												Utilidades
														.convertirADouble(codigoPkComponentesEvolucion
																+ ""));

								if (pst.executeUpdate() <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion()
													+ " de un componente en la plantilla");
								}
							}
							// *****************************************************************************************************************

						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}

						// ********************************************************************************************************
					}

				} // Fin FOR
			// **************************************************************************************************************************************************

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoComponenteEvolucion(con,
							subseccion, codigoPkEvolucion,
							codigoPkEvolucionOdo, codigoPkPlantillaEvolucion,
							codigoPkPlantillaComponente, fecha, hora,
							loginUsuario, esModificable);

			// *********************************************************************************
			// *****************************************************************************
		} catch (SQLException e) {
			logger.error("Error en insertarCampoComponenteIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkComponentesEvolucion
	 * @return
	 * @throws SQLException 
	 */
	private static boolean existeComponenteEnEvolucion(Connection con,int codigoPkComponentesEvolucion) throws SQLException 
	{
		boolean resultado=false;
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try {
			
			 pst = new PreparedStatementDecorator(con.prepareStatement(strExisteComponenteEvolucion));
			pst.setInt(1, codigoPkComponentesEvolucion);
			
			 rs =  new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1)>0;
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param codigoIngreso
	 * @param codigoPkPlantillaEvolucion
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param vieneDeComponente
	 * @param esModificable
	 *            : para saber si se debe modificar o solo insertar ,
	 *            permitiendo guardar datos vacï¿½os
	 * @return
	 */
	private static ResultadoBoolean insertarEscalaIngresoEvolucion(
			Connection con, DtoEscala escala, int codigoIngreso,
			int codigoPkPlantillaEvolucion, String fecha, String hora,
			String loginUsuario, boolean vieneDeComponente,
			boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
	
		try {
			int resp0 = 0, resp1 = 0;

			// Se verifica si la escala fue diligenciada
			if (escala.getTotalEscala() > 0 || esModificable) {
				if (Utilidades.convertirAEntero(escala
						.getConsecutivoHistorico()) <= 0) {
					// /*****************SE INSERTA EL ENCABEZADO DEL REGISTRO
					// DE LA ESCALA X INGRESO**********************************
					pst = new PreparedStatementDecorator(
							con.prepareStatement(strInsertarEscalaIngreso));

					/**
					 * INSERT INTO escalas_ingresos " + "(codigo_pk," +
					 * "ingreso," + "escala," + "escala_factor_prediccion," +
					 * "valor_total," + "observaciones," + "fecha," + "hora," +
					 * "fecha_modifica," + "hora_modifica," +
					 * "usuario_modifica) " + "VALUES " +
					 * "(?,?,?,?,?,?,?,?,CURRENT_DATE,"
					 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
					 */

					int consecutivoEscalaIngreso = UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_escalas_ingresos");
					pst.setDouble(1, Utilidades
							.convertirADouble(consecutivoEscalaIngreso + ""));
					pst.setInt(2, codigoIngreso);
					pst.setDouble(3, Utilidades.convertirADouble(escala
							.getCodigoPK()));
					if (!escala.getCodigoFactorPrediccion().equals(""))
						pst.setDouble(4, Utilidades.convertirADouble(escala
								.getCodigoFactorPrediccion()));
					else
						pst.setNull(4, Types.NUMERIC);
					pst.setDouble(5, escala.getTotalEscala());
					if (!escala.getObservaciones().equals(""))
						pst.setString(6, escala.getObservaciones());
					else
						pst.setNull(6, Types.VARCHAR);
					pst.setDate(7, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(fecha)));
					pst.setString(8, hora);
					pst.setString(9, loginUsuario);

					resp0 = pst.executeUpdate();
					// ****************************************************************************************************************

					if (resp0 > 0) {
						escala.setConsecutivoHistorico(consecutivoEscalaIngreso
								+ "");

						// Se iteran las secciones y los campos de la escala
						for (DtoSeccionParametrizable seccion : escala
								.getSecciones())
							for (DtoCampoParametrizable campo : seccion
									.getCampos())
								if ((Utilidades.convertirADouble(campo
										.getValor()) > 0
										|| !campo.getObservaciones().trim()
												.equals("") || esModificable)
										&& resultado.isTrue()) {
									// *****************SE INSERTA EL CAMPO DE
									// CADA
									// ESCALA**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarEscalaCampoIngreso));

									/**
									 * INSERT INTO escalas_campos_ingresos " +
									 * "(codigo_pk," + "escala_campo_seccion," +
									 * "escala_ingreso," + "valor," +
									 * "observaciones) " + "VALUES " +
									 * "(?,?,?,?,?)
									 */

									int consecutivoEscalaCampoIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_escalas_campos_ing");
									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoEscalaCampoIngreso
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(campo
																	.getConsecutivoParametrizacion()));
									pst
											.setDouble(
													3,
													Utilidades
															.convertirADouble(consecutivoEscalaIngreso
																	+ ""));
									pst.setDouble(4, Utilidades
											.convertirADouble(campo.getValor(),
													true));
									if (!campo.getObservaciones().equals(""))
										pst.setString(5, campo
												.getObservaciones());
									else
										pst.setNull(5, Types.VARCHAR);

									resp1 = pst.executeUpdate();

									// *****************************************************************************************

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el campo "
														+ campo.getEtiqueta()
														+ " la escala "
														+ escala
																.getDescripcion());
									} else {
										campo
												.setConsecutivoHistorico(consecutivoEscalaCampoIngreso
														+ "");
									}
								}
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la escala "
										+ escala.getDescripcion());
					}

					// ***********************SE RELACIONA EL REGISTRO DE LA
					// ESCALA CON LA PLANTILLA
					// INGRESO*******************************
					if (resultado.isTrue()) {
						pst = new PreparedStatementDecorator(con
								.prepareStatement(
										strInsertarPlantillasEscalasEvo));

						/**
						 * INSERT INTO plantillas_escalas_evo " +
						 * "(plantilla_evolucion," + "escala_ingreso," +
						 * "plantilla_escala," + "componente," + "escala) " +
						 * "VALUES(?,?,?,?,?)
						 */

						pst.setDouble(1, Utilidades
								.convertirADouble(codigoPkPlantillaEvolucion
										+ ""));
						pst
								.setDouble(
										2,
										Utilidades
												.convertirADouble(consecutivoEscalaIngreso
														+ ""));

						// Dependiendo si la escala hace parte o no de un
						// componente se relaciona de forma diferente
						if (vieneDeComponente) {
							pst.setNull(3, Types.INTEGER);
							String[] vectorConsecutivo = escala
									.getConsecutivoParametrizacion().split(
											ConstantesBD.separadorSplit);
							pst.setDouble(4, Utilidades
									.convertirADouble(vectorConsecutivo[0]));
							pst.setDouble(5, Utilidades
									.convertirADouble(vectorConsecutivo[1]));
						} else {
							pst.setInt(3, Utilidades.convertirAEntero(escala
									.getConsecutivoParametrizacion()));
							pst.setNull(4, Types.NUMERIC);
							pst.setNull(5, Types.NUMERIC);
						}

						resp0 = pst.executeUpdate();

						if (resp0 <= 0) {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas al relacionar la escala "
											+ escala.getDescripcion()
											+ " con el registro de la plantilla");
						}
					}
					// ********************************************************************************************************************

					// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE
					// LA
					// ESCALA*****************************************************
					if (resultado.isTrue()) {
						for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
							String adjEscala = "adjEscala"
									+ escala.getCodigoPK() + "_" + i;
							String adjEscalaOriginal = "adjEscalaOriginal"
									+ escala.getCodigoPK() + "_" + i;

							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala)
											.toString().equals("")
									&& resultado.isTrue()) {

								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarAdjuntosEscalaIngreso));

								/**
								 * INSERT INTO escalas_adjuntos_ingreso " +
								 * "(codigo_pk," + "escala_ingreso," +
								 * "nombre_archivo," + "nombre_original) " +
								 * "VALUES " + "(?,?,?,?)
								 */

								int consecutivoAdjuntoEscalaIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_escalas_adjuntos_ing");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoAdjuntoEscalaIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(consecutivoEscalaIngreso
																+ ""));
								pst.setString(3, escala.getArchivosAdjuntos(
										adjEscala).toString());
								pst.setString(4, escala.getArchivosAdjuntos(
										adjEscalaOriginal).toString());

								resp0 = pst.executeUpdate();

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								} else {
									escala.setArchivosAdjuntos("adjCodigoPk"
											+ escala.getCodigoPK() + "_" + i,
											consecutivoAdjuntoEscalaIngreso);
								}
							}
						}
					}
					// **********************************************************************************************************************
				} else {
					// ***************SE MODIFICA EL ENCABEZADO DEL REGISTRO DE
					// LA ESCALA X INGRESO**************************+
					int consecutivoEscalaIngreso = Utilidades
							.convertirAEntero(escala.getConsecutivoHistorico());

					pst = new PreparedStatementDecorator(
							con.prepareStatement(
									strModificarEscalaIngresoXIngPacienteOdon));
					if (!escala.getCodigoFactorPrediccion().equals(""))
						pst.setDouble(1, Utilidades.convertirADouble(escala
								.getCodigoFactorPrediccion()));
					else
						pst.setNull(1, Types.NUMERIC);
					pst.setDouble(2, escala.getTotalEscala());
					if (!escala.getObservaciones().equals(""))
						pst.setString(3, escala.getObservaciones());
					else
						pst.setNull(3, Types.VARCHAR);
					pst.setDate(4, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(fecha)));
					pst.setString(5, hora);
					pst.setString(6, loginUsuario);
					pst.setInt(7, consecutivoEscalaIngreso);

					resp0 = pst.executeUpdate();
					// ****************************************************************************************************************

					if (resp0 > 0) {

						// Se iteran las secciones y los campos de la escala
						for (DtoSeccionParametrizable seccion : escala
								.getSecciones())
							for (DtoCampoParametrizable campo : seccion
									.getCampos())
								if ((Utilidades.convertirADouble(campo
										.getValor()) > 0
										|| !campo.getObservaciones().trim()
												.equals("") || esModificable)
										&& resultado.isTrue()) {
									// *****************SE MODIFICA EL CAMPO DE
									// CADA
									// ESCALA**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strmodificarEscalaCampoIngreso));
									int consecutivoEscalaCampoIngreso = Utilidades
											.convertirAEntero(campo
													.getConsecutivoHistorico());
									pst.setDouble(1, Utilidades
											.convertirADouble(campo.getValor(),
													true));
									if (!campo.getObservaciones().equals(""))
										pst.setString(2, campo
												.getObservaciones());
									else
										pst.setNull(2, Types.VARCHAR);
									pst
											.setDouble(
													3,
													Utilidades
															.convertirADouble(consecutivoEscalaCampoIngreso
																	+ ""));

									resp1 = pst.executeUpdate();

									// *****************************************************************************************

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el campo "
														+ campo.getEtiqueta()
														+ " la escala "
														+ escala
																.getDescripcion());
									}
								}
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la escala "
										+ escala.getDescripcion());
					}

					// *******************SE MODIFICAN LOS ARCHIVOS ADJUNTOS DE
					// LA
					// ESCALA*****************************************************
					if (resultado.isTrue()) {
						for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
							String adjEscala = "adjEscala"
									+ escala.getCodigoPK() + "_" + i;
							String adjEscalaOriginal = "adjEscalaOriginal"
									+ escala.getCodigoPK() + "_" + i;
							String adjCodigoPk = "adjCodigoPk"
									+ escala.getCodigoPK() + "_" + i;

							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala)
											.toString().equals("")
									&& resultado.isTrue()) {
								// Si el archivo no existï¿½a se inserta como
								// nuevo
								if (Utilidades.convertirAEntero(escala
										.getArchivosAdjuntos(adjCodigoPk)
										+ "") <= 0) {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarAdjuntosEscalaIngreso));

									/**
									 * INSERT INTO escalas_adjuntos_ingreso " +
									 * "(codigo_pk," + "escala_ingreso," +
									 * "nombre_archivo," + "nombre_original) " +
									 * "VALUES " + "(?,?,?,?)
									 */

									int consecutivoAdjuntoEscalaIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_escalas_adjuntos_ing");
									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoAdjuntoEscalaIngreso
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(consecutivoEscalaIngreso
																	+ ""));
									pst.setString(3, escala
											.getArchivosAdjuntos(adjEscala)
											.toString());
									pst.setString(4, escala
											.getArchivosAdjuntos(
													adjEscalaOriginal)
											.toString());

									resp0 = pst.executeUpdate();
									pst.close();
								}
								// si ya existï¿½a se modifica
								else {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strModificarAdjuntosEscalaIngreso));

									pst.setString(1, escala
											.getArchivosAdjuntos(adjEscala)
											.toString());
									pst.setString(2, escala
											.getArchivosAdjuntos(
													adjEscalaOriginal)
											.toString());

									pst.setDouble(3, Utilidades
											.convertirADouble(escala
													.getArchivosAdjuntos(
															adjCodigoPk)
													.toString()));

									resp0 = pst.executeUpdate();
									pst.close();
								}

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							} else if (resultado.isTrue()
									&& Utilidades.convertirAEntero(escala
											.getArchivosAdjuntos(adjCodigoPk)
											+ "") > 0) {
								// strEliminarAdjuntosEscalaIngresoXIngrPacienteOdonto
								// =
								// "DELETE historiaclinica.escalas_adjuntos_ingreso WHERE codigo_pk = ? ";
								pst = new PreparedStatementDecorator(con,
										strEliminarAdjuntosEscalaIngresoXIngrPacienteOdonto);
								pst
										.setInt(
												1,
												Utilidades
														.convertirAEntero(escala
																.getArchivosAdjuntos(adjCodigoPk)
																+ ""));
								resp0 = pst.executeUpdate();
								pst.close();

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas eliminando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							}
						}
					}
					// ********************************************************************************************************
				}

			} // fIN IF
		} catch (SQLException e) {
			logger.error("Error en insertarEscalaIngreso: ", e);
		}finally {			
			try{
			if(pst!=null){
					pst.close();					
			}		
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
			}

		}
		return resultado;
	}

	// ///// Va hasta aca.

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Metodos Para Plantillas Secciones Fijas *****************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Inserta informacion en la plantilla secciones fijas
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * @param String
	 *            strInsertPlantillaSecFija
	 * */
	public static int insertarPlantillasSeccionesFijas(Connection con,
			HashMap parametros) {
		String cadena = "";
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps = null;

		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"seq_plantillas_sec_fijas");

		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasSecFijas));

			/**
			 * INSERT INTO plantillas_sec_fijas (" + "codigo_pk," +
			 * "fun_param_secciones_fijas," + "seccion_param," + "plantilla," +
			 * "orden," + "mostrar," + "usuario_modifica," + "fecha_modifica," +
			 * "hora_modifica) " + "VALUES(?,?,?,?,?,?,?,?,?)
			 */

			ps.setInt(1, consecutivo);

			if (parametros.containsKey("codigoPkFunParamSecFija")
					&& !parametros.get("codigoPkFunParamSecFija").equals(""))
				ps.setDouble(2, Utilidades.convertirADouble(parametros
						.get("codigoPkFunParamSecFija")
						+ ""));
			else
				ps.setNull(2, Types.NUMERIC);

			if (parametros.containsKey("codigoSeccionParam")
					&& !parametros.get("codigoSeccionParam").equals(""))
				ps.setDouble(3, Utilidades.convertirADouble(parametros
						.get("codigoSeccionParam")
						+ ""));
			else
				ps.setNull(3, Types.NUMERIC);

			ps.setDouble(4, Utilidades.convertirADouble(parametros
					.get("codigoPkPlantilla")
					+ ""));

			ps.setInt(5, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(6, parametros.get("mostrar") + "");
			ps.setString(7, parametros.get("usuarioModifica") + "");
			ps.setDate(8, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(9, parametros.get("horaModifica") + "");

			if (ps.executeUpdate() > 0)
				return consecutivo;
		} catch (SQLException e) {
			logger.info("valor de parametros >> " + parametros + " >> "
					+ strInsertarPlantillasSecFijas);
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Actualiza la informaciï¿½n de plantillas secciones fijas
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean actualizarPlantillasSeccionesFijas(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps  = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarPlantillasSecFijas));

			/**
			 * UPDATE " + "plantillas_sec_fijas " + "SET " + "orden = ?, " +
			 * "mostrar = ?, " + "fun_param_secciones_fijas = ?, " +
			 * "seccion_param = ?," + "usuario_modifica = ?," +
			 * "fecha_modifica = ?, " + "hora_modifica = ? " + "WHERE codigo_pk
			 * = ?
			 */

			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(2, parametros.get("mostrar") + "");

			if (parametros.containsKey("codigoPkFunParamSecFij")
					&& !parametros.get("codigoPkFunParamSecFij").equals(""))
				ps.setDouble(3, Utilidades.convertirADouble(parametros
						.get("codigoPkFunParamSecFij")
						+ ""));
			else
				ps.setNull(3, Types.NUMERIC);

			if (parametros.containsKey("codigoPkSeccionParam")
					&& !parametros.get("codigoPkSeccionParam").equals(""))
				ps.setDouble(4, Utilidades.convertirADouble(parametros
						.get("codigoPkSeccionParam")
						+ ""));
			else
				ps.setNull(4, Types.NUMERIC);

			ps.setString(5, parametros.get("usuarioModifica") + "");
			ps.setDate(6, Date.valueOf(parametros.get("fechaModifica") + ""));
			ps.setString(7, parametros.get("horaModifica") + "");
			ps.setInt(8, Utilidades.convertirAEntero(parametros.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
	
		return false;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Mï¿½todo implementado para guardar los campos parametrizables en
	 * plantillas_ingreso
	 */
	public static ResultadoBoolean guardarCamposParametrizablesIngreso(
			Connection con, DtoPlantilla plantilla, HashMap campos) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		try {
			logger.info("PLANTILLA TIENE INFORMACION? "
					+ plantilla.tieneInformacion());
			// Se verifica primero que todo que se haya ingresado informaciï¿½n
			// en la paramï¿½trico de la plantilla
			// Se adiciona validar que se encuentre la llave de validacion
			// odontologica con el fin de saber si es de odontologia y que no
			// valide si ahy informaciï¿½n de campos parametrizables en la
			// plantilla
			double valoracionOdonto = Utilidades.convertirADouble(campos
					.get("valoracionOdonto")
					+ "");

			if (plantilla.tieneInformacion()
					|| valoracionOdonto != ConstantesBD.codigoNuncaValidoDouble) {
				// *************CAMPOS************************************************
				int codigoIngreso = Utilidades.convertirAEntero(campos
						.get("codigoIngreso")
						+ "");
				int numeroSolicitud = Utilidades.convertirAEntero(campos
						.get("numeroSolicitud")
						+ "");
				int codigoPaciente = Utilidades.convertirAEntero(campos
						.get("codigoPaciente")
						+ "");
				String fecha = campos.get("fecha").toString();
				String hora = campos.get("hora").toString();
				String loginUsuario = campos.get("loginUsuario").toString();
				// ********************************************************************
				logger.info("SE VA A EJECUTAR INSERT: "
						+ strInsertarPlantillaIngreso);
				// ***************************SE INSERTA EL ENCABEZADO DE LA
				// PLANTILLA****************************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillaIngreso));

				/**
				 * INSERT INTO plantillas_ingresos " + "(codigo_pk," +
				 * "ingreso," + "numero_solicitud," + "codigo_paciente," +
				 * "plantilla," + "fecha," + "hora," + "fecha_modifica," +
				 * "hora_modifica," + "usuario_modifica) " + "Values " +
				 * "(?,?,?,?,?,?,?,CURRENT_DATE,"
				 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
				 */

				int consecutivoPlantillaIngreso = UtilidadBD
						.obtenerSiguienteValorSecuencia(con,
								"seq_plantilas_ingresos");
				logger.info("1) " + consecutivoPlantillaIngreso);
				pst.setDouble(1, Utilidades
						.convertirADouble(consecutivoPlantillaIngreso + ""));

				logger.info("2) " + codigoIngreso);
				if (codigoIngreso > 0)
					pst.setInt(2, codigoIngreso);
				else
					pst.setNull(2, Types.INTEGER);

				logger.info("3) " + numeroSolicitud);
				if (numeroSolicitud > 0)
					pst.setInt(3, numeroSolicitud);
				else
					pst.setNull(3, Types.INTEGER);

				logger.info("4) " + codigoPaciente);
				if (codigoPaciente > 0)
					pst.setInt(4, codigoPaciente);
				else
					pst.setNull(4, Types.INTEGER);

				logger.info("5) " + plantilla.getCodigoPK());
				pst.setDouble(5, Utilidades.convertirADouble(plantilla
						.getCodigoPK()));
				logger.info("6) " + fecha);
				pst.setDate(6, Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(fecha)));
				logger.info("7) " + hora);
				pst.setString(7, hora);
				logger.info("8) " + loginUsuario);
				pst.setString(8, loginUsuario);

				pst.setNull(9, Types.DOUBLE);

				int resp = pst.executeUpdate();
				// *******************************************************************************************************
				logger.info("RESULTADO EJECUCION:=> " + resp);
				if (resp > 0) {
					// **********************************REGISTRO DE LOS CAMPOS
					// DE CADA ELEMENTO DE LA SECCION
					// FIJA***************************
					// Se itera cada seccion fija
					for (DtoSeccionFija seccionFija : plantilla
							.getSeccionesFijas()) {
						// Se itera cada elemento de la seccion fija
						for (DtoElementoParam elemento : seccionFija
								.getElementos()) {
							// Mientras que todo continue siendo exitoso
							if (resultado.isTrue()) {

								// Si el tipo elemento es SECCION:
								// ---------------------------------------------------------------
								if (elemento.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									resultado = insertarCampoPlantillaIngreso(
											con, seccion,
											consecutivoPlantillaIngreso, false);
								}
								// Si el tipo elemento es COMPONENTE
								// -----------------------------------------------------------------
								else if (elemento.isComponente()) {
									DtoComponente componente = (DtoComponente) elemento;

									// Se itera cada seccion del componente
									for (DtoElementoParam elemenComp : componente
											.getElementos()) {
										// Se verifica si el elemento del
										// componente es una seccion
										if (elemenComp.isSeccion()) {
											DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemenComp;
											// SE retistran los campos de una
											// seccion del componente
											resultado = insertarCampoComponenteIngreso(
													con,
													seccion,
													codigoIngreso,
													numeroSolicitud,
													codigoPaciente,
													fecha,
													hora,
													loginUsuario,
													consecutivoPlantillaIngreso, // consecutivo
																					// tabla
																					// plantillas_ingresos
													componente
															.getConsecutivoParametrizacion(), // consecutivo
																								// tabla
																								// plantillas_componentes
													ConstantesBD.codigoNuncaValidoDouble,
													false);
										}
										// Se verifica si el elemento del
										// componente es una escala
										else if (elemenComp.isEscala()) {
											DtoEscala escala = (DtoEscala) elemenComp;
											resultado = insertarEscalaIngreso(
													con,
													escala,
													codigoIngreso,
													fecha,
													hora,
													loginUsuario,
													consecutivoPlantillaIngreso,
													true, false);
										}
									}

								}
								// Si el tipo elemento es ESCALA
								// -----------------------------------------------------------------------
								else if (elemento.isEscala()) {
									DtoEscala escala = (DtoEscala) elemento;
									resultado = insertarEscalaIngreso(con,
											escala, codigoIngreso, fecha, hora,
											loginUsuario,
											consecutivoPlantillaIngreso, false,
											false);
								}
							} // Fin if resultado

						} // fin for elementos seccion fijas
					} // Fin for secciones fijas de la plantilla
					// *********************************************************************************************************

					// ******************INSERCION DATOS SECCIONES
					// VALORES*****************************************************
					/**
					 * if(resultado.isTrue()) { for(DtoElementoParam
					 * elemento:plantilla.getSeccionesValor()) {
					 * DtoSeccionParametrizable seccion =
					 * (DtoSeccionParametrizable)elemento;
					 * 
					 * //Se verifica que la secciï¿½n haya sido activada en el
					 * formulario y que todo marche bien
					 * if(seccion.estaSeccionValorOpcionActiva
					 * (plantilla.getListadoSeccionesValorActivas
					 * ())&&resultado.isTrue()) { //Se pregunta si la seccion
					 * viene de la plantilla o viene del componente
					 * if(seccion.isVieneDePlantilla()) resultado =
					 * insertarCampoPlantillaIngreso
					 * (con,seccion,consecutivoPlantillaIngreso); else resultado
					 * = insertarCampoComponenteIngreso( con, seccion,
					 * codigoIngreso, numeroSolicitud, codigoPaciente, fecha,
					 * hora, loginUsuario, consecutivoPlantillaIngreso,
					 * //consecutivo tabla plantillas_ingresos plantilla.
					 * obtenerConsecutivoPlantillaComponenteSeccionValor
					 * (seccion.getConsecutivoParametrizacion()) //consecutivo
					 * tabla plantillas_componentes ); } }
					 * 
					 * }
					 **/
					// *********************************************************************************************************
				} else {
					resultado.setResultado(false);
					resultado
							.setDescripcion("Problemas realizando el registro inicial de los valores parametrizables de la plantilla");
				}
			}

		} catch (SQLException e) {
			logger.error("Error en guardarValoresParametrizables: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return resultado;
	}

	// **********************************************************************
	// **********************************************************************

	/**
	 * Inserta Plantillas Secciones
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static int insertarPlantillasSecciones(Connection con,
			HashMap parametros) {
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"seq_plantillas_secciones");
		PreparedStatementDecorator ps  = null;
		try {
			 ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasSecciones));

			logger.info("strInsertarPlantillasSecciones>>>>>>>>>>>>>>>>>>"
					+ strInsertarPlantillasSecciones);
			logger.info("consecutivo>>>>>>>>>>>>>>>>>>" + consecutivo);
			logger.info("seccionFija>>>>>>>>>>>>>>>>>>"
					+ parametros.get("codigoPkPlanSecFija"));
			logger.info("seccionParam>>>>>>>>>>>>>>>>>>"
					+ parametros.get("codigoPkSeccionParam"));

			/**
			 * INSERT INTO plantillas_secciones
			 * (codigo_pk,plantilla_sec_fija,seccion)
			 */

			ps.setInt(1, consecutivo);
			ps.setInt(2, Utilidades.convertirAEntero(parametros
					.get("codigoPkPlanSecFija")
					+ ""));
			ps.setDouble(3, Utilidades.convertirADouble(parametros
					.get("codigoPkSeccionParam")
					+ ""));

			if (ps.executeUpdate() > 0)
				return consecutivo;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Metodos Para Escalas Ingreso *****************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Mï¿½todo para registrar los valores de la escala x ingreso
	 * 
	 * @param con
	 * @param escala
	 * @param codigoIngreso
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param consecutivoPlantillaIngreso
	 * @param vieneDeComponente
	 * @param esModificable
	 * @return
	 */
	private static ResultadoBoolean insertarEscalaIngreso(Connection con,
			DtoEscala escala, int codigoIngreso, String fecha, String hora,
			String loginUsuario, double consecutivoPlantillaIngreso,
			boolean vieneDeComponente, boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		try {
			int resp0 = 0, resp1 = 0;
			boolean ingresado = false;

			// Se verifica si la escala fue
			// llenada-------------------------------------------------------------
			for (DtoSeccionParametrizable seccion : escala.getSecciones())
				for (DtoCampoParametrizable campo : seccion.getCampos()) {
					if (!campo.getValor().equals("")
							|| !campo.getObservaciones().equals(""))
						ingresado = true;
				}

			if (escala.getTotalEscala() > 0
					|| !escala.getObservaciones().equals("")
					|| escala.getNumArchivosAdjuntos() > 0)
				ingresado = true;

			// Si no se registrï¿½ nada en la escala pero se manda activo el
			// indicador de esModificable,
			// se toma la escala como ingresada
			if (!ingresado && esModificable) {
				ingresado = true;
			}

			// Se verifica si la escala fue diligenciada
			if (ingresado) {
				if (Utilidades.convertirAEntero(escala
						.getConsecutivoHistorico()) <= 0) {

					// *****************SE INSERTA EL ENCABEZADO DEL REGISTRO DE
					// LA ESCALA X INGRESO**********************************
					pst = new PreparedStatementDecorator(
							con.prepareStatement(strInsertarEscalaIngreso));

					/**
					 * INSERT INTO escalas_ingresos " + "(codigo_pk," +
					 * "ingreso," + "escala," + "escala_factor_prediccion," +
					 * "valor_total," + "observaciones," + "fecha," + "hora," +
					 * "fecha_modifica," + "hora_modifica," +
					 * "usuario_modifica) " + "VALUES " +
					 * "(?,?,?,?,?,?,?,?,CURRENT_DATE,"
					 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
					 */

					int consecutivoEscalaIngreso = UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_escalas_ingresos");
					pst.setDouble(1, Utilidades
							.convertirADouble(consecutivoEscalaIngreso + ""));
					if (codigoIngreso > 0)
						pst.setInt(2, codigoIngreso);
					else
						pst.setNull(2, Types.INTEGER);
					pst.setDouble(3, Utilidades.convertirADouble(escala
							.getCodigoPK()));
					// Tambiï¿½n se debe verificar que el codigo del factor de
					// prediccion sea un codigo vï¿½lido
					if (!escala.getCodigoFactorPrediccion().equals("")
							&& Utilidades.convertirAEntero(escala
									.getCodigoFactorPrediccion()) > 0)
						pst.setDouble(4, Utilidades.convertirADouble(escala
								.getCodigoFactorPrediccion()));
					else
						pst.setNull(4, Types.NUMERIC);
					pst.setDouble(5, escala.getTotalEscala());
					if (!escala.getObservaciones().equals(""))
						pst.setString(6, escala.getObservaciones());
					else
						pst.setNull(6, Types.VARCHAR);
					pst.setDate(7, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(fecha)));
					pst.setString(8, hora);
					pst.setString(9, loginUsuario);

					resp0 = pst.executeUpdate();
					// ****************************************************************************************************************

					if (resp0 > 0) {

						// Se iteran las secciones y los campos de la escala
						for (DtoSeccionParametrizable seccion : escala
								.getSecciones())
							for (DtoCampoParametrizable campo : seccion
									.getCampos()) {

								if ((Utilidades.convertirADouble(campo
										.getValor()) > 0
										|| !campo.getObservaciones().trim()
												.equals("") || esModificable)
										&& resultado.isTrue()) {
									// *****************SE INSERTA EL CAMPO DE
									// CADA
									// ESCALA**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarEscalaCampoIngreso));
									int consecutivoEscalaCampoIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_escalas_campos_ing");

									/**
									 * INSERT INTO escalas_campos_ingresos " +
									 * "(codigo_pk," + "escala_campo_seccion," +
									 * "escala_ingreso," + "valor," +
									 * "observaciones) " + "VALUES " +
									 * "(?,?,?,?,?)
									 */

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoEscalaCampoIngreso
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(campo
																	.getConsecutivoParametrizacion()));
									pst
											.setDouble(
													3,
													Utilidades
															.convertirADouble(consecutivoEscalaIngreso
																	+ ""));
									pst.setDouble(4, Utilidades
											.convertirADouble(campo.getValor(),
													true));
									if (!campo.getObservaciones().equals(""))
										pst.setString(5, campo
												.getObservaciones());
									else
										pst.setNull(5, Types.VARCHAR);

									resp1 = pst.executeUpdate();

									// *****************************************************************************************

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el campo "
														+ campo.getEtiqueta()
														+ " la escala "
														+ escala
																.getDescripcion());
									}
								}
							}
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la escala "
										+ escala.getDescripcion());
					}

					// ***********************SE RELACIONA EL REGISTRO DE LA
					// ESCALA CON LA PLANTILLA
					// INGRESO*******************************
					if (resultado.isTrue() || esModificable) {
						pst = new PreparedStatementDecorator(con
								.prepareStatement(
										strInsertarPlantillaEscalaIngreso));

						/**
						 * INSERT INTO plantillas_escala_ing " +
						 * "(plantilla_ingreso," + "escala_ingreso," +
						 * "plantilla_escala," + "componente," + "escala) " +
						 * "values " + "(?,?,?,?,?)
						 */

						pst.setDouble(1, Utilidades
								.convertirADouble(consecutivoPlantillaIngreso
										+ ""));
						pst
								.setDouble(
										2,
										Utilidades
												.convertirADouble(consecutivoEscalaIngreso
														+ ""));
						// Dependiendo si la escala hace parte o no de un
						// componente se relaciona de forma diferente
						if (vieneDeComponente) {
							pst.setNull(3, Types.INTEGER);
							String[] vectorConsecutivo = escala
									.getConsecutivoParametrizacion().split(
											ConstantesBD.separadorSplit);
							pst.setDouble(4, Utilidades
									.convertirADouble(vectorConsecutivo[0]));
							pst.setDouble(5, Utilidades
									.convertirADouble(vectorConsecutivo[1]));
						} else {
							pst.setInt(3, Utilidades.convertirAEntero(escala
									.getConsecutivoParametrizacion()));
							pst.setNull(4, Types.NUMERIC);
							pst.setNull(5, Types.NUMERIC);
						}

						resp0 = pst.executeUpdate();

						if (resp0 <= 0) {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas al relacionar la escala "
											+ escala.getDescripcion()
											+ " con el registro de la plantilla");
						}
					}
					// ********************************************************************************************************************

					// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE
					// LA
					// ESCALA*****************************************************
					if (resultado.isTrue() || esModificable) {
						for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
							String adjEscala = "adjEscala"
									+ escala.getCodigoPK() + "_" + i;
							String adjEscalaOriginal = "adjEscalaOriginal"
									+ escala.getCodigoPK() + "_" + i;

							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala)
											.toString().equals("")
									&& resultado.isTrue()) {

								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarAdjuntosEscalaIngreso));

								/**
								 * INSERT INTO escalas_adjuntos_ingreso " +
								 * "(codigo_pk," + "escala_ingreso," +
								 * "nombre_archivo," + "nombre_original) " +
								 * "VALUES " + "(?,?,?,?)
								 */

								int consecutivoAdjuntoEscalaIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_escalas_adjuntos_ing");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoAdjuntoEscalaIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(consecutivoEscalaIngreso
																+ ""));
								pst.setString(3, escala.getArchivosAdjuntos(
										adjEscala).toString());
								pst.setString(4, escala.getArchivosAdjuntos(
										adjEscalaOriginal).toString());

								resp0 = pst.executeUpdate();

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							}
						}
					}
					// **********************************************************************************************************************
				}
				// Modificacion de la escala
				else {
					// /*****************SE MODIFICA EL ENCABEZADO DEL REGISTRO
					// DE LA ESCALA X INGRESO**********************************
					pst = new PreparedStatementDecorator(
							con
									.prepareStatement(
											strModificarEscalaIngresoXIngPacienteOdon));
					// /Tambiï¿½n se debe verificar que el codigo del factor de
					// prediccion sea un codigo vï¿½lido
					if (!escala.getCodigoFactorPrediccion().equals("")
							&& Utilidades.convertirAEntero(escala
									.getCodigoFactorPrediccion()) > 0)
						pst.setDouble(1, Utilidades.convertirADouble(escala
								.getCodigoFactorPrediccion()));
					else
						pst.setNull(1, Types.NUMERIC);
					pst.setDouble(2, escala.getTotalEscala());
					if (!escala.getObservaciones().equals(""))
						pst.setString(3, escala.getObservaciones());
					else
						pst.setNull(3, Types.VARCHAR);
					pst.setDate(4, Date.valueOf(UtilidadFecha
							.conversionFormatoFechaABD(fecha)));
					pst.setString(5, hora);
					pst.setString(6, loginUsuario);
					pst.setInt(7, Utilidades.convertirAEntero(escala
							.getConsecutivoHistorico()));

					resp0 = pst.executeUpdate();
					// ****************************************************************************************************************

					if (resp0 > 0) {

						// Se iteran las secciones y los campos de la escala
						for (DtoSeccionParametrizable seccion : escala
								.getSecciones())
							for (DtoCampoParametrizable campo : seccion
									.getCampos()) {

								if ((Utilidades.convertirADouble(campo
										.getValor()) > 0
										|| !campo.getObservaciones().trim()
												.equals("") || esModificable)
										&& resultado.isTrue()) {
									// *****************SE MODIFICA EL CAMPO DE
									// CADA
									// ESCALA**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strmodificarEscalaCampoIngreso));

									pst.setDouble(1, Utilidades
											.convertirADouble(campo.getValor(),
													true));
									if (!campo.getObservaciones().equals(""))
										pst.setString(2, campo
												.getObservaciones());
									else
										pst.setNull(2, Types.VARCHAR);
									pst
											.setInt(
													3,
													Utilidades
															.convertirAEntero(campo
																	.getConsecutivoHistorico()));

									resp1 = pst.executeUpdate();

									// *****************************************************************************************

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el campo "
														+ campo.getEtiqueta()
														+ " la escala "
														+ escala
																.getDescripcion());
									}
								}
							}
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la escala "
										+ escala.getDescripcion());
					}

					// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE
					// LA
					// ESCALA*****************************************************
					if (resultado.isTrue() || esModificable) {
						for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
							String adjEscala = "adjEscala"
									+ escala.getCodigoPK() + "_" + i;
							String adjEscalaOriginal = "adjEscalaOriginal"
									+ escala.getCodigoPK() + "_" + i;
							String adjCodigoPk = "adjCodigoPk"
									+ escala.getCodigoPK() + "_" + i;

							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala)
											.toString().equals("")
									&& resultado.isTrue()) {
								if (Utilidades.convertirAEntero(escala
										.getArchivosAdjuntos(adjCodigoPk)
										+ "") > 0) {
									// **********************MODIFICACION DEL
									// ARCHIVO
									// ADJUNTO**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strModificarAdjuntosEscalaIngreso));
									pst.setString(1, escala
											.getArchivosAdjuntos(adjEscala)
											.toString());
									pst.setString(2, escala
											.getArchivosAdjuntos(
													adjEscalaOriginal)
											.toString());
									pst.setDouble(1, Utilidades
											.convertirADouble(escala
													.getArchivosAdjuntos(
															adjCodigoPk)
													.toString()));
									resp0 = pst.executeUpdate();
									pst.close();

									// **********************************************************************************************
								} else {
									// *********************INSERCION DEL
									// ARCHIVO
									// ADJUNTO********************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarAdjuntosEscalaIngreso));

									/**
									 * INSERT INTO escalas_adjuntos_ingreso " +
									 * "(codigo_pk," + "escala_ingreso," +
									 * "nombre_archivo," + "nombre_original) " +
									 * "VALUES " + "(?,?,?,?)
									 */

									int consecutivoAdjuntoEscalaIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_escalas_adjuntos_ing");
									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoAdjuntoEscalaIngreso
																	+ ""));
									pst.setDouble(2, Utilidades
											.convertirADouble(escala
													.getConsecutivoHistorico()
													+ ""));
									pst.setString(3, escala
											.getArchivosAdjuntos(adjEscala)
											.toString());
									pst.setString(4, escala
											.getArchivosAdjuntos(
													adjEscalaOriginal)
											.toString());

									resp0 = pst.executeUpdate();
									pst.close();
									// ***********************************************************************************************
								}

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							} else if (Utilidades.convertirAEntero(escala
									.getArchivosAdjuntos(adjCodigoPk)
									+ "") > 0
									&& resultado.isTrue()) {
								// ***************ELIMINACIï¿½N DEL ARCHIVO
								// ADJUNTO*******************************************************
								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarAdjuntosEscalaIngreso));
								pst
										.setInt(
												1,
												Utilidades
														.convertirAEntero(escala
																.getArchivosAdjuntos(adjCodigoPk)
																+ ""));

								resp0 = pst.executeUpdate();
								pst.close();
								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
								// ******************************************************************************************************

							}
						}
					}
					// **********************************************************************************************************************
				}
			} // Fin IF ingresado
		} catch (SQLException e) {
			logger.error("Error en insertarEscalaIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
	
		}
		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	/**
	 * Mï¿½todo implementado para insertar los campos del componente ingreso
	 * 
	 * @param con
	 * @param seccion
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param codigoIngreso
	 * @param hora
	 * @param fecha
	 * @param loginUsuario
	 * @param consecutivoPlantillaComponente
	 * @param consecutivoPlantillaIngreso
	 * @param esModificable
	 * @return
	 */
	private static ResultadoBoolean insertarCampoComponenteIngreso(
			Connection con, DtoSeccionParametrizable seccion,
			int codigoIngreso, int numeroSolicitud, int codigoPaciente,
			String fecha, String hora, String loginUsuario,
			double consecutivoPlantillaIngreso,
			String consecutivoPlantillaComponente, double valoracionOdon,
			boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst  = null;
		try {
			int resp0 = 0, resp1 = 0;

			// ************************************INSERTAN LOS CAMPOS DEL
			// REGISTRO COMPONENTE X
			// INGRESO***********************************************
			for (DtoCampoParametrizable campo : seccion.getCampos())
				if (resultado.isTrue() /* tambiï¿½n que todo marche bien */) {

					// En el caso de que el campo no este registrado se hace una
					// inserciï¿½n
					if (campo.getConsecutivoHistorico().equals("")) {
						pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarCampoComponenteIngreso));

						/**
						 * INSERT INTO componentes_ingreso " + "(codigo_pk," +
						 * "componente_seccion," + "campo_parametrizable," +
						 * "ingreso," + "codigo_paciente," + "numero_solicitud,"
						 * + "nombre_archivo_original," + "fecha," + "hora," +
						 * "fecha_modifica," + "hora_modifica," +
						 * "usuario_modifica,"+ "valoracion_odo) " + "VALUES " +
						 * "(?,?,?,?,?,?,?,?,?,current_date,"
						 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
						 */

						int consecutivoComponenteIngreso = UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_componentes_ingreso");
						pst.setDouble(1, Utilidades
								.convertirADouble(consecutivoComponenteIngreso
										+ ""));
						pst.setDouble(2, Utilidades.convertirADouble(seccion
								.getConsecutivoParametrizacion()));
						pst.setDouble(3, Utilidades.convertirADouble(campo
								.getCodigoPK()));

						if (codigoIngreso > 0)
							pst.setInt(4, codigoIngreso);
						else
							pst.setNull(4, Types.INTEGER);

						if (codigoPaciente > 0)
							pst.setInt(5, codigoPaciente);
						else
							pst.setNull(5, Types.INTEGER);

						if (numeroSolicitud > 0)
							pst.setInt(6, numeroSolicitud);
						else
							pst.setNull(6, Types.INTEGER);

						// Aplica para campo que es tipo archivo
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(7, campo.getNombreArchivoOriginal());
						else
							pst.setNull(7, Types.VARCHAR);

						pst.setDate(8, Date.valueOf(UtilidadFecha
								.conversionFormatoFechaABD(fecha)));
						pst.setString(9, hora);

						pst.setString(10, loginUsuario);

						// SE Aï¿½ADE LA VALORACION ODONTOLOGICA
						if (valoracionOdon != ConstantesBD.codigoNuncaValidoDouble)
							pst.setDouble(11, valoracionOdon);
						else
							pst.setNull(11, Types.NUMERIC);

						if (!campo.getGenerarAlerta().equals(""))
							pst.setString(12, campo.getGenerarAlerta());
						else
							pst.setString(12, ConstantesBD.acronimoNo);

						resp0 = pst.executeUpdate();

						// ****************SE INSERTAN LOS VALORES DEL
						// CAMPO*************************************
						if (resp0 > 0) {
							campo
									.setConsecutivoHistorico(consecutivoComponenteIngreso
											+ "");

							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones())
									if ((UtilidadTexto.getBoolean(opcion
											.getSeleccionado()) || esModificable)
											&& resultado.isTrue()) {
										pst = new PreparedStatementDecorator(
												con
														.prepareStatement(
																strInsertarValorComponenteIngreso));
										int consecutivoValorComponenteIngreso = UtilidadBD
												.obtenerSiguienteValorSecuencia(
														con,
														"seq_valores_comp_ingreso");
										pst
												.setDouble(
														1,
														Utilidades
																.convertirADouble(consecutivoValorComponenteIngreso
																		+ ""));
										pst
												.setDouble(
														2,
														Utilidades
																.convertirADouble(consecutivoComponenteIngreso
																		+ ""));
										pst.setString(3, opcion.getValor());
										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst
													.setString(
															4,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst.setNull(4, Types.VARCHAR);
										if (!opcion.getSeleccionado()
												.equals(""))
											pst.setString(5, opcion
													.getSeleccionado());
										else
											pst.setString(5,
													ConstantesBD.acronimoNo);
										resp1 = pst.executeUpdate();

										if (resp1 <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas registrando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
										} else {
											opcion
													.setCodigoHistorico(consecutivoValorComponenteIngreso
															+ "");
										}
									}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarValorComponenteIngreso));

								/**
								 * INSERT INTO valores_comp_ingreso
								 * (codigo_pk,componente_ingreso,valor) VALUES
								 * (?,?,?)
								 */

								int consecutivoValorComponenteIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_valores_comp_ingreso");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoValorComponenteIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(consecutivoComponenteIngreso
																+ ""));
								pst.setString(3, campo.getValor());
								if (!campo.getValoresOpcion().equals(""))
									pst.setString(4, campo.getValoresOpcion());
								else
									pst.setNull(4, Types.VARCHAR);
								pst.setNull(5, Types.VARCHAR);
								resp1 = pst.executeUpdate();

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}

							// **************REGISTRO DE LA RELACION DEL CAMPO
							// DEL COMPONENTE CON LA
							// PLANTILLA*********************************
							if (resultado.isTrue()) {
								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarPlantillaComponenteIngreso));

								/**
								 * INSERT INTO plantillas_comp_ing
								 * (plantilla_ingreso
								 * ,plantilla_componente,componente_ingreso)
								 * VALUES (?,?,?)
								 */

								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoPlantillaIngreso
																+ ""));
								pst
										.setInt(
												2,
												Utilidades
														.convertirAEntero(consecutivoPlantillaComponente));
								pst
										.setDouble(
												3,
												Utilidades
														.convertirADouble(consecutivoComponenteIngreso
																+ ""));

								if (pst.executeUpdate() <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion()
													+ " de un componente en la plantilla");
								}
							}
							// *****************************************************************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}
						// ***************************************************************************************

					}
					// En el caso de que el registro ya existï¿½a se debe
					// actualizar la informacion
					else {

						 pst = new PreparedStatementDecorator(con,strModificarCampoComponenteIngresoOdontologia);
						// Aplica para campo que es tipo archivo
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(1, campo.getNombreArchivoOriginal());
						else
							pst.setNull(1, Types.VARCHAR);
						pst.setDate(2, Date.valueOf(UtilidadFecha
								.conversionFormatoFechaABD(fecha)));
						pst.setString(3, hora);
						pst.setString(4, loginUsuario);
						pst.setBigDecimal(5, new BigDecimal(Integer
								.parseInt(campo.getConsecutivoHistorico())));

						resp0 = pst.executeUpdate();

						// ****************SE MODIFICAN LOS VALORES DEL
						// CAMPO*************************************
						if (resp0 > 0) {

							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones())
									if ((UtilidadTexto.getBoolean(opcion
											.getSeleccionado()) || esModificable)
											&& resultado.isTrue()) {
										int consecutivoValorComponenteIngreso = Utilidades
												.convertirAEntero(opcion
														.getCodigoHistorico());
										pst = new PreparedStatementDecorator(
												con
														.prepareStatement(
																strModificarValorComponenteIngreso01));
										pst.setString(1, opcion.getValor());
										pst.setString(2, opcion
												.getValoresOpcionRegistrado());
										if (!opcion.getSeleccionado()
												.equals(""))
											pst.setString(3, opcion
													.getSeleccionado());
										else
											pst.setNull(3, Types.VARCHAR);
										pst
												.setInt(4,
														consecutivoValorComponenteIngreso);

										resp1 = pst.executeUpdate();

										if (resp1 <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas registrando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
										}
									}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								int consecutivoValorComponenteIngreso = Utilidades
										.convertirAEntero(campo
												.getConsecutivoHistorico());
								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strModificarValorComponenteIngreso02));
								pst.setString(1, campo.getValor());
								pst.setString(2, campo.getValoresOpcion());
								pst.setNull(3, Types.VARCHAR);
								pst
										.setInt(4,
												consecutivoValorComponenteIngreso);

								resp1 = pst.executeUpdate();

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}
							}

							// *****************************************************************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}
						// ***************************************************************************************

					}

				} // Fin FOR
			// **************************************************************************************************************************************************

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoComponenteIngreso(con, subseccion,
							codigoIngreso, numeroSolicitud, codigoPaciente,
							fecha, hora, loginUsuario,
							consecutivoPlantillaIngreso,
							consecutivoPlantillaComponente, valoracionOdon,
							esModificable);
			// *********************************************************************************
			// *****************************************************************************
		} catch (SQLException e) {
			logger.error("Error en insertarCampoComponenteIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}
		return resultado;
	}

	// *************************************************************************************************************
	// *************************************************************************************************************

	/**
	 * Mï¿½todo implementado para insertar un campo de la plantilla de ingreso
	 * 
	 * @param con
	 * @param seccion
	 * @param consecutivoPlantillaIngreso
	 * @param esModificable
	 *            : atributo para saber si la plantilla va a manejar la
	 *            informacion como modificable
	 * @return
	 */
	private static ResultadoBoolean insertarCampoPlantillaIngreso(
			Connection con, DtoSeccionParametrizable seccion,
			double consecutivoPlantillaIngreso, boolean esModificable) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		try {
			int resp0 = 0, resp1 = 0;

			for (DtoCampoParametrizable campo : seccion.getCampos())
				if (resultado.isTrue() /*
										 * mientras que todo marche bien se
										 * continua
										 */)

				{
					logger.info("ACA EMPEIZA EL MENSAJE!!!!!!!!1");
					// Si el campo nunca se ha ingresado se realiza la insercion
					// del campo
					if (campo.getConsecutivoHistorico().equals("")) {
						// ***************************SE REGISTRA EL
						// CAMPO******************************************************
						pst = new PreparedStatementDecorator(con,strInsertarCampoPlantillaIngreso);

						/**
						 * INSERT INTO plantillas_ing_campos " + "(codigo_pk," +
						 * "plantilla_campo_sec," + "nombre_archivo_original," +
						 * "plantilla_ingreso) " + "VALUES " + "(?,?,?,?)
						 */

						int consecutivoPlantillaIngCampo = UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_plantilas_ing_campos");
						pst.setDouble(1, Utilidades
								.convertirADouble(consecutivoPlantillaIngCampo
										+ ""));
						pst.setInt(2, Utilidades.convertirAEntero(campo
								.getConsecutivoParametrizacion()));
						// Si el campo fue de tipo archivo debe tener este campo
						// lleno
						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(3, campo.getNombreArchivoOriginal());
						else
							pst.setNull(3, Types.VARCHAR);
						pst.setDouble(4, Utilidades
								.convertirADouble(consecutivoPlantillaIngreso
										+ ""));

						resp0 = pst.executeUpdate();
						// *******************************************************************************

						if (resp0 > 0) {
							// ***************INSERCION DE LOS VALORES DEL
							// CAMPO*******************************************************
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {
									logger.info("-->" + opcion.getValor()
											+ "<--");

									// Agrego vienneVacio a la validacion, como
									// parte adicional apra odontologica
									if (((UtilidadTexto.getBoolean(opcion
											.getSeleccionado())
											|| opcion.getPixelImg().length() > 0 || esModificable)
											&& resultado.isTrue() && !UtilidadTexto
											.isEmpty(opcion.getValor())))

									{
										pst = new PreparedStatementDecorator(
												con
														.prepareStatement(
																strInsertarValorPlantillaIngreso));

										int consecutivoValorPlantillaIngreso = UtilidadBD
												.obtenerSiguienteValorSecuencia(
														con,
														"seq_valores_plan_ing_camp");

										pst
												.setDouble(
														1,
														Utilidades
																.convertirADouble(consecutivoValorPlantillaIngreso
																		+ ""));
										pst
												.setDouble(
														2,
														Utilidades
																.convertirADouble(consecutivoPlantillaIngCampo
																		+ ""));
										pst.setString(3, opcion.getValor());
										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst
													.setString(
															4,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst.setNull(4, Types.VARCHAR);

										// Genera la imagen
										InfoDatosStr nombreArchivo = opcion
												.generarImagen(consecutivoValorPlantillaIngreso
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst.close();
										} else {
											if (!nombreArchivo.equals(""))
												pst.setString(5, nombreArchivo
														.getCodigo());
											else
												pst.setNull(5, Types.VARCHAR);

											// Campos agregados por valoracion
											// odon Anexo 870
											pst.setString(6, opcion
													.getSeleccionado());
											if (Utilidades
													.convertirAEntero(opcion
															.getConvencionOdon()
															.getCodigo()) > 0)
												pst
														.setInt(
																7,
																Utilidades
																		.convertirAEntero(opcion
																				.getConvencionOdon()
																				.getCodigo()));
											else
												pst.setNull(7, Types.INTEGER);
											// Fin campos agregados

											resp1 = pst.executeUpdate();
											pst.close();

											if (resp1 <= 0) {
												resultado.setResultado(false);
												resultado
														.setDescripcion("Problemas registrando el valor "
																+ opcion
																		.getValor()
																+ " del campo "
																+ campo
																		.getEtiqueta()
																+ " en la seccion "
																+ seccion
																		.getDescripcion());
											}
										}
									}
								}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {

								if (!UtilidadTexto.isEmpty(campo.getValor())
										|| esModificable) {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarValorPlantillaIngreso));
									int consecutivoValorPlantillaIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_valores_plan_ing_camp");

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoValorPlantillaIngreso
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(consecutivoPlantillaIngCampo
																	+ ""));

									if (!campo.getValor().isEmpty())
										pst.setString(3, campo.getValor());
									else
										pst.setNull(3, Types.VARCHAR);

									if (!campo.getValoresOpcion().equals(""))
										pst.setString(4, campo
												.getValoresOpcion());
									else
										pst.setNull(4, Types.VARCHAR);

									pst.setNull(5, Types.VARCHAR);

									// Campos agregados por valoracion odon
									// Anexo 870
									pst.setNull(6, Types.VARCHAR);
									pst.setNull(7, Types.INTEGER);
									// Fin campos agregados

									resp1 = pst.executeUpdate();
									pst.close();

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el valor "
														+ campo.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
									}
								}
							}
							// ******************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}
					}
					// De lo contrario se realiza la modificacion
					else {
						// **************************MODIFICACION DEL
						// CAMPO****************************************
						pst = new PreparedStatementDecorator(
								con, strModificarCampoPlantillaIngreso);
						pst.setString(1, campo.getNombreArchivoOriginal());
						pst.setLong(2, Long.parseLong(campo
								.getConsecutivoHistorico()));
						int consecutivoPlantillaIngCampo = Utilidades
								.convertirAEntero(campo
										.getConsecutivoHistorico());

						// *******************************************************************************************
						resp0 = pst.executeUpdate();
						pst.close();
						// *******************************************************************************

						if (resp0 > 0) {
							// ***************MODIFICACION DE LOS VALORES DEL
							// CAMPO*******************************************************
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {

									int consecutivoValorPlantillaIngreso = Utilidades
											.convertirAEntero(opcion
													.getCodigoHistorico());

									// Agrego vienneVacio a la validacion, como
									// parte adicional apra odontologica
									if (((UtilidadTexto.getBoolean(opcion
											.getSeleccionado())
											|| opcion.getPixelImg().length() > 0 || esModificable)
											&& resultado.isTrue() && !UtilidadTexto
											.isEmpty(opcion.getValor())))

									{

										pst = new PreparedStatementDecorator(
												con
														.prepareStatement(
																strModificarValorPlantillaIngreso
																		+ " codigo_pk = ?"));
										pst.setString(1, opcion
												.getValoresOpcionRegistrado());

										// Genera la imagen
										InfoDatosStr nombreArchivo = opcion
												.generarImagen(consecutivoValorPlantillaIngreso
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst.close();
											resp1 = 0;
										} else {
											if (!UtilidadTexto.isEmpty(nombreArchivo.getCodigo()))
												pst.setString(2, nombreArchivo
														.getCodigo());
											else
												pst.setNull(2, Types.VARCHAR);
											pst.setString(3, opcion
													.getSeleccionado());
											if (Utilidades
													.convertirAEntero(opcion
															.getConvencionOdon()
															.getCodigo()) > 0)
												pst
														.setInt(
																4,
																Utilidades
																		.convertirAEntero(opcion
																				.getConvencionOdon()
																				.getCodigo()));
											else
												pst.setNull(4, Types.INTEGER);
											
											if (!UtilidadTexto.isEmpty(opcion.getValor()))
												pst.setString(5, opcion.getValor());
											else
												pst.setNull(5, Types.VARCHAR);
												
											pst
											.setInt(6,
													consecutivoValorPlantillaIngreso);
											resp1 = pst.executeUpdate();
											pst.close();
										}

										if (resp1 <= 0) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas registrando el valor "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
										}

									}
								} // fin for opciones
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {

								if (!UtilidadTexto.isEmpty(campo.getValor())
										|| esModificable) {
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strModificarValorPlantillaIngreso
																	+ " plantilla_ing_campo = ?"));
									if (!UtilidadTexto.isEmpty(campo.getValor())){
										pst.setString(1, campo
												.getValoresOpcion());
										pst.setNull(2, Types.VARCHAR);
										pst.setNull(3, Types.VARCHAR);
										pst.setNull(4, Types.INTEGER);
										pst.setString(5, campo.getValor());
										pst.setInt(6, consecutivoPlantillaIngCampo);
									}
									else{
										pst.setNull(1, Types.VARCHAR);
										pst.setNull(2, Types.VARCHAR);
										pst.setNull(3, Types.VARCHAR);
										pst.setNull(4, Types.INTEGER);
										pst.setNull(5, Types.VARCHAR);
										pst.setInt(6, consecutivoPlantillaIngCampo);
									}

									resp1 = pst.executeUpdate();
									pst.close();

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el valor "
														+ campo.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
									}
								}
							}
							// ******************************************************************
						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas actualizando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}

					}

				} // Fin for de campos

			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarCampoPlantillaIngreso(con, subseccion,
							consecutivoPlantillaIngreso, esModificable);
			// *********************************************************************************

		} catch (SQLException e) {
			logger.error("Error en insertarCampoPlantillaIngreso: " + e);
			e.printStackTrace();
			resultado.setResultado(false);
			resultado
					.setDescripcion("Ocurriï¿½ error insertando un campo de la plantilla: "
							+ e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return resultado;
	}

	// ************************************************************************************************************************************************
	// Metodos de Escalas y Componentes.

	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static int insertarEscalaParametrizable(Connection con,
			HashMap parametros) {

		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"seq_plantillas_escalas");
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strInsertarPlantillasEscalas));

			/**
			 * INSERT INTO plantillas_escalas
			 * (codigo_pk,plantilla_sec_fija,escala
			 * ,orden,mostrar,mostrar_modificacion)
			 */

			ps.setInt(1, consecutivo);
			ps.setInt(2, Utilidades.convertirAEntero(parametros
					.get("plantillaSecFija")
					+ ""));
			ps.setDouble(3, Utilidades.convertirADouble(parametros
					.get("escala")
					+ ""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(5, parametros.get("mostrar") + "");
			ps.setString(6, parametros.get("mostrarModificacion") + "");
			ps.setString(7, parametros.get("requerida") + "");

			if (ps.executeUpdate() > 0)
				return consecutivo;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return ConstantesBD.codigoNuncaValido;

	}

	// ************************************************************************************************************************************************

	// ************************************************************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static int insertarComponenteParametrizable(Connection con,
			HashMap parametros) {

		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"seq_plantillas_componentes");
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasComponentes));

			/**
			 * INSERT INTO plantillas_componentes ( codigo_pk,
			 * plantilla_sec_fija, componente, orden, mostrar,
			 * mostrar_modificacion, fecha_inicial_registro,
			 * hora_inicial_registro)
			 */

			ps.setInt(1, consecutivo);
			ps.setInt(2, Utilidades.convertirAEntero(parametros
					.get("plantillaSecFija")
					+ ""));
			ps.setDouble(3, Utilidades.convertirADouble(parametros
					.get("componente")
					+ ""));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(5, parametros.get("mostrar") + "");
			ps.setString(6, parametros.get("mostrarModificacion") + "");
			ps.setDate(7, Date.valueOf(parametros.get("fechaInicialRegistro")
					+ ""));
			ps.setString(8, parametros.get("horaInicialRegistro") + "");

			if (ps.executeUpdate() > 0)
				return consecutivo;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}

		return ConstantesBD.codigoNuncaValido;

	}

	// ************************************************************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarComponenteParametrizable(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps  = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarPlantillasComponentes));

			/**
			 * UPDATE " + "plantillas_componentes " + "SET " +
			 * "componente = ?, " + "orden = ?, " + "mostrar = ?, " +
			 * "mostrar_modificacion = ?, " + "fecha_final_registro = ?, " +
			 * "hora_final_registro = ? " + "WHERE codigo_pk = ? "
			 */

			ps.setDouble(1, Utilidades.convertirADouble(parametros
					.get("componente")
					+ ""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(3, parametros.get("mostrar") + "");
			ps.setString(4, parametros.get("mostrarModificacion") + "");
			ps.setDate(5, Date.valueOf(parametros.get("fechaFinalRegistro")
					+ ""));
			ps.setString(6, parametros.get("horaFinalRegistro") + "");
			ps.setInt(7, Utilidades.convertirAEntero(parametros.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// ************************************************************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarEscalaParametrizable(Connection con,
			HashMap parametros) {
		// logger.info("valor del mapa >> "+parametros+" >> "+strActualizarPlantillasEscalas);
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strActualizarPlantillasEscalas));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("orden")
					+ ""));
			ps.setString(2, parametros.get("mostrar") + "");
			ps.setString(3, parametros.get("mostrarModificacion") + "");
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.info("parametros del error >> " + parametros);
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// ************************************************************************************************************************************************

	/**
	 * Mï¿½todo para obtener el cï¿½digo de una plantilla
	 */
	public static int obtenerCodigoPlantillaXIngreso(Connection con,
			HashMap campos) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		Statement st = null;
		ResultSetDecorator rs  = null;
		try {
			// ********************SE TOMAN LOS
			// CAMPOS********************************
			int codigoIngreso = Utilidades.convertirAEntero(campos.get(
					"codigoIngreso").toString());
			int codigoPaciente = Utilidades.convertirAEntero(campos.get(
					"codigoPaciente").toString());
			int numeroSolicitud = Utilidades.convertirAEntero(campos.get(
					"numeroSolicitud").toString());
			// **********************************************************************

			String consulta = "SELECT plantilla AS codigo_plantilla FROM plantillas_ingresos ";
			String seccionWHERE = "";

			if (codigoIngreso > 0)
				seccionWHERE += " ingreso = " + codigoIngreso;

			if (codigoPaciente > 0)
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND " : "")
						+ " codigo_paciente = " + codigoPaciente;

			if (numeroSolicitud > 0)
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND " : "")
						+ " numero_solicitud = " + numeroSolicitud;

			if (!seccionWHERE.equals("")) {
				consulta = consulta + " WHERE " + seccionWHERE
						+ " ORDER BY plantilla ASC ";
				 st = con.createStatement();
				 rs = new ResultSetDecorator(st
						.executeQuery(consulta));
				if (rs.next())
					codigoPlantilla = rs.getInt("codigo_plantilla");
			}

		} catch (SQLException e) {
			logger.error("Error en obtenerCodigoPlantillaXIngreso: " + e);
		}finally {			
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return codigoPlantilla;
	}

	/**
	 * Mï¿½todo para obtener el cï¿½digo de una plantilla
	 */
	public static int obtenerCodigoPlantillaXEvolucion(Connection con,
			int evolucion) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		Statement st = null;
		ResultSetDecorator rs  = null;
		try {
			String consulta = "SELECT plantilla AS codigo_plantilla FROM plantillas_evolucion  WHERE evolucion= "
					+ evolucion;
			st = con.createStatement();
			rs = new ResultSetDecorator(st
					.executeQuery(consulta));
			if (rs.next())
				codigoPlantilla = rs.getInt("codigo_plantilla");
		} catch (SQLException e) {
			logger.error("Error en obtenerCodigoPlantillaXIngreso: " + e);
		}finally {			
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return codigoPlantilla;
	}

	// ----------------------------------------------------------------------------------------------------
	// *********************************************************************
	/** Metodos Para Campos Parametrizables ********************************/
	// *********************************************************************
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Inserta datos en campo parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * @return
	 */
	public static int insertarCamposParametrizables(Connection con, HashMap vo) {
		// logger.info("valor del mapa >> "+vo);
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarCampoParametrizable));

			/**
			 * "campos_parametrizables (" + "codigo_pk, " + "codigo, " +
			 * "nombre, " + "etiqueta," + "tipo, " + "tamanio, " + "signo, " +
			 * "unidad, " + "valor_predeterminado," + "maximo, " + "minimo, " +
			 * "decimales, " + "columnas_ocupadas, " + "orden, " +
			 * "unico_fila, " + "requerido, " + "formula, " + "tipo_html, " +
			 * "mostrar," + "mostrar_modificacion, " + "institucion, " +
			 * "usuario_modifica, " + "fecha_modifica, " + "hora_modifica,
			 * "maneja_imagen," + "imagen_a_asociar)
			 */

			int codigoCampo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_campos_param");

			ps.setDouble(1, Utilidades.convertirADouble(codigoCampo + ""));

			if(vo.containsKey("codigo"))
				ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo") + ""));
			else
				ps.setDouble(2, Utilidades.convertirADouble(codigoCampo + ""));

			ps.setString(3, vo.get("nombre") + "");

			if (UtilidadTexto.isEmpty(vo.get("etiqueta") + ""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("etiqueta") + "");

			ps.setInt(5, Utilidades.convertirAEntero(vo.get("tipo") + ""));

			if (UtilidadTexto.isEmpty(vo.get("tamanio") + ""))
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, Utilidades
						.convertirAEntero(vo.get("tamanio") + ""));

			if (UtilidadTexto.isEmpty(vo.get("signo") + "")
					|| (vo.get("signo") + "")
							.equals(ConstantesBD.codigoNuncaValido + ""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, vo.get("signo") + "");

			if (UtilidadTexto.isEmpty(vo.get("unidad") + "")
					|| vo.get("unidad").toString().equals("")
					|| vo.get("unidad").toString().equals(
							ConstantesBD.codigoNuncaValido + "")
					|| vo.get("unidad").toString().equals("0"))
				ps.setNull(8, Types.INTEGER);
			else
				ps
						.setInt(8, Utilidades.convertirAEntero(vo.get("unidad")
								+ ""));

			if (UtilidadTexto.isEmpty(vo.get("valor_predeterminado") + ""))
				ps.setNull(9, Types.VARCHAR);
			else
				ps.setString(9, vo.get("valor_predeterminado") + "");

			if (UtilidadTexto.isEmpty(vo.get("maximo") + ""))
				ps.setNull(10, Types.NUMERIC);
			else
				ps.setDouble(10, Utilidades.convertirADouble(vo.get("maximo")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("minimo") + ""))
				ps.setNull(11, Types.NUMERIC);
			else
				ps.setDouble(11, Utilidades.convertirADouble(vo.get("minimo")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("decimales") + ""))
				ps.setNull(12, Types.INTEGER);
			else
				ps.setInt(12, Utilidades.convertirAEntero(vo.get("decimales")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("columnas_ocupadas") + ""))
				ps.setNull(13, Types.INTEGER);
			else
				ps.setInt(13, Utilidades.convertirAEntero(vo
						.get("columnas_ocupadas")
						+ ""));

			ps.setInt(14, Utilidades.convertirAEntero(vo.get("orden") + ""));

			ps.setString(15, vo.get("unico_fila") + "");

			if (UtilidadTexto.isEmpty(vo.get("requerido") + ""))
				ps.setString(16, ConstantesBD.acronimoNo);
			else
				ps.setString(16, vo.get("requerido") + "");

			if (UtilidadTexto.isEmpty(vo.get("formula") + ""))
				ps.setNull(17, Types.VARCHAR);
			else
				ps.setString(17, vo.get("formula") + "");

			ps.setString(18, vo.get("tipo_html") + "");

			if (UtilidadTexto.isEmpty(vo.get("mostrar") + ""))
				ps.setString(19, ConstantesBD.acronimoNo);
			else
				ps.setString(19, vo.get("mostrar") + "");

			ps.setString(20, vo.get("mostrarModificacion") + "");

			ps.setInt(21, Utilidades.convertirAEntero(vo.get("institucion")
					+ ""));

			ps.setString(22, vo.get("usuarioModifica") + "");

			ps.setDate(23, Date.valueOf(vo.get("fechaModifica") + ""));

			ps.setString(24, vo.get("horaModifica") + "");

			// Anexo 841
			if (vo.containsKey("manejaImg")
					&& !vo.get("manejaImg").toString().equals(""))
				ps.setString(25, vo.get("manejaImg").toString());
			else
				ps.setNull(25, Types.VARCHAR);

			// logger.info("consecutivo imagen base: "+vo.get("imagenAsociar").toString());
			if (vo.containsKey("imagenAsociar")
					&& !vo.get("imagenAsociar").toString().equals(""))
				ps.setString(26, vo.get("imagenAsociar").toString());
			else
				ps.setNull(26, Types.VARCHAR);
			// Fin Anexo 841

			if (ps.executeUpdate() > 0)
				return codigoCampo;
			else
				return ConstantesBD.codigoNuncaValido;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return ConstantesBD.codigoNuncaValido;
	}

	// ***********************************************************************************************************************************

	/**
	 * Actualiza Mostrar Modificacion de los campos parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * @return
	 */
	public static boolean actualizarMostrarModificacionCamposParametrizables(
			Connection con, HashMap vo) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarMostrarModificacionCamposParam));

			/**
			 * UPDATE campos_parametrizables SET " + "mostrar_modificacion = ?,"
			 * + "usuario_modifica = ?, " + "fecha_modifica = ?, " +
			 * "hora_modifica = ?  " + "WHERE codigo_pk = ?
			 */

			ps.setString(1, vo.get("mostrarModificacion") + "");
			ps.setString(2, vo.get("usuarioModifica") + "");
			ps.setDate(3, Date.valueOf(vo.get("fechaModifica") + ""));
			ps.setString(4, vo.get("horaModifica") + "");
			ps.setDouble(5, Utilidades
					.convertirADouble(vo.get("codigoPk") + ""));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	// ***********************************************************************************************************************************

	/**
	 * Actualiza las Opciones de los Campos Parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * @return
	 */
	public static boolean actualizarOpcionesCamposParam(Connection con,
			HashMap vo) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarOpciones));

			/**
			 * <<<<<<< .working <<<<<<< .working UPDATE
			 * opciones_val_parametrizadas SET " + ======= <<<<<<< .working
			 * UPDATE opciones_val_parametrizadas SET " + >>>>>>>
			 * .merge-right.r50091 ======= UPDATE opciones_val_parametrizadas
			 * SET " + >>>>>>> .merge-right.r50029 ======= <<<<<<< .working
			 * UPDATE opciones_val_parametrizadas SET " + ======= UPDATE
			 * opciones_val_parametrizadas SET " + >>>>>>> .merge-right.r50029
			 * >>>>>>> .merge-right.r50108 "opcion = ?, " + "valor = ?," +
			 * "usuario_modifica = ?," + "fecha_modifica = ?, " +
			 * "hora_modifica = ? " + "WHERE codigo_pk = ?
			 */

			ps.setString(1, vo.get("opcion") + "");
			ps.setString(2, vo.get("valor") + "");
			ps.setString(3, vo.get("usuarioModifica") + "");
			ps.setDate(4, Date.valueOf(vo.get("fechaModifica") + ""));
			ps.setString(5, vo.get("horaModifica") + "");

			if (vo.containsKey("convencion")
					&& !vo.get("convencion").equals("")
					&& Utilidades.convertirAEntero(vo.get("convencion") + "") > 0)
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("convencion")
						+ ""));
			else
				ps.setNull(6, Types.INTEGER);

			ps.setDouble(7, Utilidades
					.convertirADouble(vo.get("codigoPk") + ""));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		return false;
	}

	// ***********************************************************************************************************************************

	/**
	 * Actualiza datos en campo parametrizables
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * @return
	 */
	public static boolean actualizarCamposParametrizables(Connection con,
			HashMap vo) {
		// logger.info("valor del mapa >> "+vo);
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarCamposParametrizable));

			/**
			 * "codigo = ?, " + "nombre = ?, " + "etiqueta = ?, " +
			 * "tipo = ?,  " + "tamanio = ?,  " + "signo = ?,  " +
			 * "unidad = ?,  " + "valor_predeterminado = ?, " + "maximo = ?,  "
			 * + "minimo = ?,  " + "decimales = ?,  " +
			 * "columnas_ocupadas = ?,  " + "orden = ?,  " + "unico_fila = ?,  "
			 * + "requerido = ?,  " + "formula = ?,  " + "tipo_html = ?, " +
			 * "mostrar = ?,  " + "usuario_modifica = ?,  " +
			 * "fecha_modifica = ?,  " + "hora_modifica  = ? " +
			 * "WHERE codigo_pk = ? ";
			 */

			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigo") + ""));

			ps.setString(2, vo.get("nombre") + "");

			if (UtilidadTexto.isEmpty(vo.get("etiqueta") + ""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, vo.get("etiqueta") + "");

			ps.setInt(4, Utilidades.convertirAEntero(vo.get("tipo") + ""));

			if (UtilidadTexto.isEmpty(vo.get("tamanio") + ""))
				ps.setNull(5, Types.INTEGER);
			else
				ps.setInt(5, Utilidades
						.convertirAEntero(vo.get("tamanio") + ""));

			if (UtilidadTexto.isEmpty(vo.get("signo") + "")
					|| (vo.get("signo") + "")
							.equals(ConstantesBD.codigoNuncaValido + ""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("signo") + "");

			if (UtilidadTexto.isEmpty(vo.get("unidad") + "")
					|| vo.get("unidad").toString().equals("")
					|| vo.get("unidad").toString().equals(
							ConstantesBD.codigoNuncaValido + "")
					|| vo.get("unidad").toString().equals("0"))
				ps.setNull(7, Types.INTEGER);
			else
				ps
						.setInt(7, Utilidades.convertirAEntero(vo.get("unidad")
								+ ""));

			if (UtilidadTexto.isEmpty(vo.get("valor_predeterminado") + ""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, vo.get("valor_predeterminado") + "");

			if (UtilidadTexto.isEmpty(vo.get("maximo") + ""))
				ps.setNull(9, Types.NUMERIC);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("maximo")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("minimo") + ""))
				ps.setNull(10, Types.NUMERIC);
			else
				ps.setDouble(10, Utilidades.convertirADouble(vo.get("minimo")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("decimales") + ""))
				ps.setNull(11, Types.INTEGER);
			else
				ps.setInt(11, Utilidades.convertirAEntero(vo.get("decimales")
						+ ""));

			if (UtilidadTexto.isEmpty(vo.get("columnas_ocupadas") + ""))
				ps.setNull(12, Types.INTEGER);
			else
				ps.setInt(12, Utilidades.convertirAEntero(vo
						.get("columnas_ocupadas")
						+ ""));

			ps.setInt(13, Utilidades.convertirAEntero(vo.get("orden") + ""));

			ps.setString(14, vo.get("unico_fila") + "");

			if (UtilidadTexto.isEmpty(vo.get("requerido") + ""))
				ps.setString(15, ConstantesBD.acronimoNo);
			else
				ps.setString(15, vo.get("requerido") + "");

			if (UtilidadTexto.isEmpty(vo.get("formula") + ""))
				ps.setNull(16, Types.VARCHAR);
			else
				ps.setString(16, vo.get("formula") + "");

			ps.setString(17, vo.get("tipo_html") + "");

			if (UtilidadTexto.isEmpty(vo.get("mostrar") + ""))
				ps.setString(18, ConstantesBD.acronimoNo);
			else
				ps.setString(18, vo.get("mostrar") + "");

			ps.setString(19, vo.get("usuarioModifica") + "");

			ps.setDate(20, Date.valueOf(vo.get("fechaModifica") + ""));

			ps.setString(21, vo.get("horaModifica") + "");

			// Anexo 841
			if (vo.containsKey("maneja_imagen")
					&& !vo.get("maneja_imagen").equals(""))
				ps.setString(22, vo.get("maneja_imagen") + "");
			else
				ps.setNull(22, Types.VARCHAR);

			if (vo.containsKey("imagen_a_asociar")
					&& Utilidades.convertirAEntero(vo.get("imagen_a_asociar")
							+ "") > 0)
				ps.setInt(23, Utilidades.convertirAEntero(vo
						.get("imagen_a_asociar")
						+ ""));
			else
				ps.setNull(23, Types.INTEGER);

			ps.setDouble(24, Utilidades.convertirADouble(vo.get("codigoPk")
					+ ""));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	// ***********************************************************************************************************************************

	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con
	 * @param HashMap vo
	 * */
	public static int insertarOpcionesCamposParam(Connection con, HashMap vo) {
		PreparedStatementDecorator ps =  null;
		try {

			ps = new PreparedStatementDecorator(con.prepareStatement(StrInsertarOpciones));

			int codigoOpcion = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_opciones_val_param");

			ps.setDouble(1, Utilidades.convertirADouble(codigoOpcion + ""));
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("campo_parametrizable")+ ""));
			ps.setString(3, vo.get("opcion") + "");
			ps.setString(4, vo.get("valor") + "");
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion") + ""));
			ps.setString(6, vo.get("usuarioModifica") + "");
			ps.setDate(7, Date.valueOf(vo.get("fechaModifica") + ""));
			ps.setString(8, vo.get("horaModifica") + "");

			// logger.info("valor codigo conv odon: "+vo.get("convencion").toString());
			if (vo.containsKey("convencion")
					&& !vo.get("convencion").equals("")
					&& Utilidades.convertirAEntero(vo.get("convencion") + "") > 0)
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("convencion").toString()));
			else
				ps.setNull(9, Types.INTEGER);

			if (ps.executeUpdate() > 0)
				return codigoOpcion;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// ***********************************************************************************************************************************

	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * */
	public static int insertarPlantillasCamposSec(Connection con, HashMap vo) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strInsertarPlantillasCamposSec));

			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_plantillas_campos_sec");

			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(vo
					.get("codigoPkPlantillaSec")
					+ ""));
			ps.setDouble(3, Utilidades.convertirADouble(vo
					.get("codigoPkCampoParam")
					+ ""));

			if (ps.executeUpdate() > 0)
				return codigo;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// ***********************************************************************************************************************************

	/**
	 * Elimina las opciones de una campo
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            vo
	 * */
	public static boolean eliminarOpcionesCamposSec(Connection con, HashMap vo) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strEliminarOpcionCampoSec));
			ps.setDouble(1, Utilidades
					.convertirADouble(vo.get("codigoPk") + ""));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		return false;
	}

	// *********************************************************************************************************************************

	/**
	 * Mï¿½todo para obtener un listado de las escalas del ingreso
	 */
	public static ArrayList<DtoEscala> obtenerEscalasIngreso(Connection con,
			HashMap campos) {
		ArrayList<DtoEscala> escalas = new ArrayList<DtoEscala>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs =  null;
		ResultSetDecorator rs1 = null;
		ResultSetDecorator rs2 = null;
		try {
			// *********************CAMPOS**********************************************
			String idIngreso = campos.get("idIngreso").toString();
			String fechaInicial = UtilidadFecha
					.conversionFormatoFechaABD(campos.get("fechaInicial")
							.toString());
			String horaInicial = campos.get("horaInicial").toString();
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos
					.get("fechaFinal").toString());
			String horaFinal = campos.get("horaFinal").toString();
			// **************************************************************************

			String consulta = strObtenerEscalasXIngreso;
			if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
				if (!horaInicial.equals("") && !horaFinal.equals(""))
					consulta += " AND ei.fecha || '-' ||ei.hora >= '"
							+ fechaInicial + "-" + horaInicial
							+ "' AND ei.fecha || '-' ||ei.hora <= '"
							+ fechaFinal + "-" + horaFinal + "'";
				else
					consulta += " AND ei.fecha between '" + fechaInicial
							+ "' AND '" + fechaFinal + "'";

			}
			consulta += " ORDER BY ei.fecha, ei.hora";

			// logger.info("Consulta de las escalas x ingreso=> "+consulta.replace("?",
			// idIngreso));

			pst = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next()) {
				DtoEscala escala = new DtoEscala();
				escala.setConsecutivoParametrizacion(rs
						.getString("consecutivo"));
				escala.setCodigoPK(rs.getString("codigo_pk"));
				escala.setCodigo(rs.getString("codigo_escala"));
				escala.setDescripcion(rs.getString("nombre_escala"));
				escala.setFecha(rs.getString("fecha"));
				escala.setHora(rs.getString("hora"));
				escala.setTotalEscala(rs.getDouble("valor_total"));
				escala.setNombreResponsable(rs.getString("nombre_usuario"));
				escala.setNombreFactorPrediccion(rs
						.getString("nombre_factor_prediccion"));
				escala.setObservaciones(rs.getString("observaciones"));

				// ************SE CONSULTAN LAS SECCIONES DE LA
				// ESCALA****************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(
						strObtenerSeccionesEscalaXIngreso));
				pst.setInt(1, Utilidades.convertirAEntero(escala
						.getConsecutivoParametrizacion()));
				rs1 = new ResultSetDecorator(pst
						.executeQuery());

				while (rs1.next()) {
					DtoSeccionParametrizable seccion = new DtoSeccionParametrizable();
					seccion.setConsecutivoParametrizacion(rs1
							.getString("consecutivo_seccion"));
					seccion.setDescripcion(rs1.getString("nombre_seccion"));

					// *********************SE CONSULTAN LOS CAMPOS DE LA
					// SECCION -
					// ESCALA*******************************************************
					pst = new PreparedStatementDecorator(con.prepareStatement(
							strObtenerCamposEscalaXIngreso));
					pst.setDouble(1, Utilidades.convertirADouble(escala
							.getConsecutivoParametrizacion()));
					pst.setDouble(2, Utilidades.convertirADouble(seccion
							.getConsecutivoParametrizacion()));

					rs2 = new ResultSetDecorator(pst
							.executeQuery());

					while (rs2.next()) {
						DtoCampoParametrizable campo = new DtoCampoParametrizable();
						campo.setCodigo(rs2.getString("codigo_campo"));
						campo.setNombre(rs2.getString("nombre_campo"));
						campo.setValor(rs2.getString("valor"));
						campo.setObservaciones(rs2.getString("observaciones"));

						seccion.getCampos().add(campo);
					}

					// **************************************************************************************************************************

					escala.getSecciones().add(seccion);
				}
				// ******************************************************************************************
				// *************SE CONSULTAN LOS ARCHIVOS ADJUNTOS DE LA
				// ESCALA******************************
				int numArchivos = 0;
				pst = new PreparedStatementDecorator(con.prepareStatement(
						strCargarAdjuntosEscala));
				pst.setInt(1, Utilidades.convertirAEntero(escala
						.getConsecutivoParametrizacion()));
				rs1 = new ResultSetDecorator(pst.executeQuery());

				while (rs1.next()) {
					escala.setArchivosAdjuntos("adjEscalaOriginal"
							+ escala.getConsecutivoParametrizacion() + "_"
							+ numArchivos, rs1.getString("nombre_original"));
					escala.setArchivosAdjuntos("adjEscala"
							+ escala.getConsecutivoParametrizacion() + "_"
							+ numArchivos, rs1.getString("nombre_archivo"));
					numArchivos++;
				}
				escala.setArchivosAdjuntos("numRegistros", numArchivos);
				// *******************************************************************************************

				escalas.add(escala);
			}
		} catch (SQLException e) {
			logger.error("Error en obtenerEscalasIngreso: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(rs1!=null){
				try{
					rs1.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(rs2!=null){
				try{
					rs2.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
		}

		return escalas;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existePlantillaIngreso(Connection con,
			int numeroSolicitud) {
		String consulta = "SELECT codigo_pk FROM plantillas_ingresos WHERE numero_solicitud=?";
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);

			if (ps.executeQuery().next())
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public static boolean existePlantillaEvolucion(Connection con, int evolucion) {
		String consulta = "SELECT codigo_pk FROM plantillas_evolucion WHERE evolucion=?";
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			ps.setInt(1, evolucion);

			if (ps.executeQuery().next())
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public static HashMap<String, Object> consultarCamposExitentes(
			Connection con, int plantillaBase, int centroCosto, int sexo,
			int especialidad) {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps = null;
		try {

			String consulta = consultaCamposExistentesStr;

			if ((centroCosto) >= 0) {
				if (centroCosto == ConstantesBD.codigoCentroCostoTodos)
					consulta += " AND centro_costo IS NULL ";
				else
					consulta += " AND centro_costo = " + centroCosto;
			}
			if (sexo >= 0) {
				if (sexo == ConstantesBD.codigoSexoTodos)
					consulta += " AND sexo IS NULL ";
				else
					consulta += " AND sexo = " + sexo;
			}
			if (especialidad >= 0)
				consulta += " AND especialidad = " + especialidad;

			 ps = new PreparedStatementDecorator(con
					.prepareStatement(consulta));

			logger.info("consultaCamposExistentesStr>>>>>>>>>>>" + consulta);
			logger.info("plantillas>>>>>>>>>>>" + plantillaBase);

			ps.setInt(1, plantillaBase);

			mapa = (HashMap<String, Object>) UtilidadBD.cargarValueObject(
					new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public static HashMap<String, Object> consultarSeccionesExistentes(
			Connection con, int plantillaBase, int centroCosto, int sexo,
			int especialidad) {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		
		PreparedStatementDecorator ps = null;
		try {

			String consulta = consultaSeccionesExistentesStr;

			if ((centroCosto) >= 0) {
				if (centroCosto == ConstantesBD.codigoCentroCostoTodos)
					consulta += " AND centro_costo IS NULL ";
				else
					consulta += " AND centro_costo = " + centroCosto;
			}
			if (sexo >= 0) {
				if (sexo == ConstantesBD.codigoSexoTodos)
					consulta += " AND p.sexo IS NULL ";
				else
					consulta += " AND p.sexo = " + sexo;
			}
			if (especialidad >= 0)
				consulta += " AND especialidad = " + especialidad;

			ps = new PreparedStatementDecorator(con.prepareStatement(consulta));

			logger.info("consultaSeccionesExistentesStr>>>>>>>>>>>" + consulta);
			logger.info("plantilla>>>>>>>>>>>" + plantillaBase);

			ps.setInt(1, plantillaBase);

			mapa = (HashMap<String, Object>) UtilidadBD.cargarValueObject(
					new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public static HashMap<String, Object> consultarEscalasExitentesComponentes(
			Connection con, int plantillaBase, int centroCosto, int sexo,
			int especialidad) {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps = null;
		try {

			String consulta = consultaEscalasComponentesStr;

			if ((centroCosto) >= 0) {
				if (centroCosto == ConstantesBD.codigoCentroCostoTodos)
					consulta += " AND p.centro_costo IS NULL ";
				else
					consulta += " AND p.centro_costo = " + centroCosto;
			}
			if (sexo >= 0) {
				if (sexo == ConstantesBD.codigoSexoTodos)
					consulta += " AND p.sexo IS NULL ";
				else
					consulta += " AND p.sexo = " + sexo;
			}
			if (especialidad >= 0)
				consulta += " AND p.especialidad = " + especialidad;

			ps = new PreparedStatementDecorator(con.prepareStatement(consulta));

			logger.info("consultaEscalasComponentesStr>>>>>>>>>>>" + consulta);
			logger.info("plantilla>>>>>>>>>>>" + plantillaBase);

			ps.setInt(1, plantillaBase);

			mapa = (HashMap<String, Object>) UtilidadBD.cargarValueObject(
					new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}
		return mapa;
	}

	// **************************************************************************************************************
	// **************************************************************************************************************

	/**
	 * Consulta los textos predeterminados para el campo resultado de la
	 * respuesta de procedimientos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static HashMap consultarTextosRespuestaProc(Connection con,
			HashMap parametros) {
		HashMap respuesta = new HashMap();
		String cadena = strTextoPredeterminados;

		if (parametros.containsKey("activo")
				&& !parametros.get("activo").toString().equals(""))
			cadena += " AND activo = '" + parametros.get("activo") + "' ";

		if (parametros.containsKey("codigoServicio")
				&& !parametros.get("codigoServicio").toString().equals(""))
			cadena += " AND servicio = " + parametros.get("codigoServicio")
					+ " ";

		cadena += " order by descripcion_texto ASC ";
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			rs = new ResultSetDecorator(ps.executeQuery());
			respuesta = UtilidadBD.cargarValueObject(rs, true, true);
		} catch (SQLException e) {
			logger.info("valor de parametros >> " + parametros);
			e.printStackTrace();
		}finally {			
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		return respuesta;
	}

	// **************************************************************************************************************
	// **************************************************************************************************************

	/**
	 * Inserta parametrizaciones plantilla servicios/diagnosticos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static int insertarFormularioRespServ(Connection con,
			HashMap parametros) {
		int codigo = ConstantesBD.codigoNuncaValido;
		// logger.info("valor de parametros >> "+parametros);
		PreparedStatementDecorator ps = null;
		try {
			codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_form_resp_serv");

			ps = new PreparedStatementDecorator(con
					.prepareStatement(strInsertarFomularioRespServ));
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(
					"institucion").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get(
					"codigoPkPlantilla").toString()));

			if (parametros.containsKey("codigoServicio")
					&& !parametros.get("codigoServicio").toString().equals("")
					&& Utilidades.convertirAEntero(parametros.get(
							"codigoServicio").toString()) > 0)
				ps.setInt(4, Utilidades.convertirAEntero(parametros.get(
						"codigoServicio").toString()));
			else
				ps.setNull(4, Types.INTEGER);

			if (parametros.containsKey("codigoDiag")
					&& !parametros.get("codigoDiag").toString().equals("")
					&& Utilidades.convertirAEntero(parametros.get("codigoDiag")
							.toString()) > 0)
				ps.setInt(5, Utilidades.convertirAEntero(parametros.get(
						"codigoDiag").toString()));
			else
				ps.setNull(5, Types.INTEGER);

			if (ps.executeUpdate() > 0)
				return codigo;

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("valor de parametros >> " + parametros
					+ " codigoPk >> " + codigo);
			return ConstantesBD.codigoNuncaValido;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return ConstantesBD.codigoNuncaValido;
	}

	// **************************************************************************************************************

	/**
	 * Eliminar parametrizaciones plantilla servicios/diagnosticos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean eliminarFormularioRespServ(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(strEliminarFomularioRespServ));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigoPk")
					.toString()));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return true;
	}

	// **************************************************************************************************************

	/**
	 * Modifica parametrizaciones plantilla servicios/diagnosticos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean modificarFormularioRespServ(Connection con,
			HashMap parametros) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strModificarFomularioRespServ));

			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(
					"codigoPkPlantilla").toString()));

			if (parametros.containsKey("codigoServicio")
					&& !parametros.get("codigoServicio").toString().equals("")
					&& Utilidades.convertirAEntero(parametros.get(
							"codigoServicio").toString()) > 0)
				ps.setInt(2, Utilidades.convertirAEntero(parametros.get(
						"codigoServicio").toString()));
			else
				ps.setNull(2, Types.INTEGER);

			if (parametros.containsKey("codigoDiag")
					&& !parametros.get("codigoDiag").toString().equals("")
					&& Utilidades.convertirAEntero(parametros.get("codigoDiag")
							.toString()) > 0)
				ps.setInt(3, Utilidades.convertirAEntero(parametros.get(
						"codigoDiag").toString()));
			else
				ps.setNull(3, Types.INTEGER);

			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("codigoPk")
					.toString()));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return true;
	}

	// **************************************************************************************************************

	/**
	 * Consulta la informacion de las plantillas Servicios Diagnosticos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static ArrayList<DtoPlantillaServDiag> consultarPlantillasServDiag(
			Connection con, HashMap parametros) {
		ArrayList<DtoPlantillaServDiag> array = new ArrayList<DtoPlantillaServDiag>();
		DtoPlantillaServDiag dto;
		String cadena = strConsultarFormulariosRespServ;

		if (parametros.containsKey("codigoPkPlantilla")
				&& !parametros.get("codigoPkPlantilla").toString().equals("")
				&& Utilidades.convertirAEntero(parametros.get(
						"codigoPkPlantilla").toString()) > 0)
			cadena += " AND frs.plantilla = "
					+ parametros.get("codigoPkPlantilla").toString();

		if (parametros.containsKey("codigoPk")
				&& !parametros.get("codigoPk").toString().equals("")
				&& Utilidades.convertirAEntero(parametros.get("codigoPk")
						.toString()) > 0)
			cadena += " AND frs.codigo_pk = "
					+ parametros.get("codigoPk").toString();

		cadena += " ORDER BY p.nombre_plantilla,descripcion_servicio,descripcion_diag ASC ";

		// logger.info("valor de la consulta >> "+cadena);
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs =  null;
		try {
			// logger.info("\n\n//-----------------------------");
			// logger.info("Consulta Plantillas Servicio/Diagnostico >> "+cadena+" >> "+parametros);
			// logger.info("//-----------------------------\n");

			ps = new PreparedStatementDecorator(con
					.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(parametros
					.get("tarifario").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(
					"institucion").toString()));

			rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				dto = new DtoPlantillaServDiag();
				dto.setCodigpPk(rs.getInt(1));
				dto.setCodigoPkPlantilla(rs.getInt(2));
				dto.setCodigoPlantilla(Utilidades.convertirAEntero(rs
						.getString(3)));
				dto.setDescripcionPlantilla(rs.getString(4));
				dto.setCodigoServicio(Utilidades.convertirAEntero(rs
						.getString(5)));
				dto.setDescripcionServicio(rs.getString(6));
				dto.setCodigoEspecialidadServicio(Utilidades
						.convertirAEntero(rs.getString(7)));
				dto.setCodigoPropietarioServicio(rs.getString(8));
				dto.setCodigoDiagnostico(Utilidades.convertirAEntero(rs
						.getString(9)));
				dto.setDescripcionDiagnostico(rs.getString(10));
				dto.setEstaBD(ConstantesBD.acronimoSi);
				dto.setEsEliminado(ConstantesBD.acronimoNo);
				array.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return array;
	}

	// **************************************************************************************************************

	/**
	 * Consulta los diagnosticos parametrizados necesarios para la
	 * parametrizacion de plantillas servicios/diag
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static ArrayList<HashMap<String, Object>> consultarDiagnosticosPlantillas(
			Connection con, HashMap parametros) {
		ArrayList<HashMap<String, Object>> respuesta = new ArrayList();
		HashMap mapa;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs =  null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strConsultarDiagProcFormulario));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(
					"institucion").toString()));

			rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				mapa = new HashMap();
				mapa.put("codigo", rs.getString(1));
				mapa.put("descripcion", rs.getString(2));
				respuesta.add(mapa);
			}
		} catch (SQLException e) {
			logger.info("valor de parametros >> " + parametros);
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		return respuesta;
	}

	// **************************************************************************************************************
	// **************************************************************************************************************
	// **************************************************************************************************************
	// **************************************************************************************************************
	// INICIO SECCIONES Y VALORES ASOCIADOS A
	// OPCIONES*************************************************************//

	/**
	 * 
	 */
	public static boolean actualizarSeccionesAsociadasOpciones(Connection con,
			HashMap mapa) {
		PreparedStatementDecorator ps = null;
		
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strActualizarDetSecciones));

			ps.setString(1, mapa.get("mostrarModificacion") + "");

			ps.setString(2, mapa.get("usuarioModifica") + "");

			ps.setDate(3, Date.valueOf(mapa.get("fechaModifica") + ""));

			ps.setString(4, mapa.get("horaModifica") + "");

			ps.setInt(5, Utilidades.convertirAEntero(mapa
					.get("codigoPkDetSecciones")
					+ ""));

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean actualizarValoresAsociadosOpciones(Connection con,
			HashMap mapa) {
		
		PreparedStatementDecorator ps = null;
		
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strActualizarDetValores));

			ps.setString(1, mapa.get("mostrarModificacion") + "");

			ps.setString(2, mapa.get("usuarioModifica") + "");

			ps.setDate(3, Date.valueOf(mapa.get("fechaModifica") + ""));

			ps.setString(4, mapa.get("horaModifica") + "");

			ps.setInt(5, Utilidades.convertirAEntero(mapa
					.get("codigoPkDetValores")
					+ ""));

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarSeccionesAsociadasOpciones(Connection con,
			HashMap mapa) {
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strInsertarDetSecciones));

			int codigoSeccion = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_det_secciones");

			ps.setDouble(1, codigoSeccion);

			ps.setDouble(2, Utilidades.convertirADouble(mapa
					.get("codigoPkOpcionCampo")
					+ ""));

			ps.setInt(3, Utilidades.convertirAEntero(mapa
					.get("codigoPkPlantillaSeccion")
					+ ""));

			ps.setString(4, mapa.get("mostrarModificacion") + "");

			ps.setString(5, mapa.get("usuarioModifica") + "");

			ps.setString(6, mapa.get("fechaModifica") + "");

			ps.setString(7, mapa.get("horaModifica") + "");

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarValoresAsociadosOpciones(Connection con,
			HashMap mapa) {
		PreparedStatementDecorator ps = null; 
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(strInsertarDetValores));

			int codigoValorAsociado = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_valores");
			ps.setDouble(1, codigoValorAsociado);
			ps.setDouble(2, Utilidades.convertirADouble(mapa.get("codigoPkOpcionCampo")+ ""));
			ps.setString(3, mapa.get("valor") + "");
			ps.setString(4, mapa.get("mostrarModificacion") + "");
			ps.setString(5, mapa.get("usuarioModifica") + "");
			ps.setString(6, mapa.get("fechaModifica") + "");
			ps.setString(7, mapa.get("horaModfica") + "");
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean desasociarSeccionOculta(Connection con, HashMap mapa) {
		PreparedStatementDecorator ps = null;
		try {
			 ps = new PreparedStatementDecorator(con
					.prepareStatement(strDesasociarSeccion));

			ps.setString(1, mapa.get("mostrarModificacion") + "");

			ps.setString(2, mapa.get("usuarioModifica") + "");

			ps.setDate(3, Date.valueOf(mapa.get("fechaModifica") + ""));

			ps.setString(4, mapa.get("horaModifica") + "");

			ps.setInt(5, Utilidades.convertirAEntero(mapa
					.get("codigoPkPlanSeccion")
					+ ""));

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarSeccionesAsociadasOpcionesNuevas(
			Connection con, HashMap mapa, String cadenaInsertar) {
		PreparedStatementDecorator ps = null;
		try {
			 ps = new PreparedStatementDecorator(con
					.prepareStatement(cadenaInsertar));

			int codigoSeccion = UtilidadBD.obtenerSiguienteValorSecuencia(con,
					"seq_det_secciones");

			ps.setDouble(1, Utilidades.convertirADouble(mapa
					.get("codigoPkPlantillaSeccionNuevo")
					+ ""));

			ps.setString(2, mapa.get("mostrarModificacion") + "");

			ps.setString(3, mapa.get("usuarioModifica") + "");

			ps.setString(4, mapa.get("fechaModifica") + "");

			ps.setString(5, mapa.get("horaModifica") + "");

			ps.setInt(6, Utilidades.convertirAEntero(mapa
					.get("codigoPkPlantillaSeccionViejo")
					+ ""));

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}
		return false;
	}

	/**
	 * Desasocia todas las secciones ocultas que se encuentren asociadas a un
	 * seccion por medio de sus campos
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean desasociarSeccionesOcultasDeSeccionN(Connection con,
			HashMap mapa) {
		String cadena = "UPDATE det_secciones SET "
				+ "mostrar_modificacion=?, "
				+ "usuario_modifica=?, "
				+ "fecha_modifica=?, "
				+ "hora_modifica=? "
				+ "WHERE "
				+ "codigo_pk_opciones IN "
				+ "(SELECT "
				+ " pvp.codigo_pk "
				+ " FROM opciones_val_parametrizadas pvp "
				+ " INNER JOIN plantillas_campos_sec pcs ON (pcs.plantilla_seccion = ?) "
				+ " WHERE pvp.campo_parametrizable = pcs.campo_parametrizable ) ";
		PreparedStatementDecorator ps = null;
		try {
			ps = new PreparedStatementDecorator(con
					.prepareStatement(cadena));

			ps.setString(1, mapa.get("mostrarModificacion") + "");
			ps.setString(2, mapa.get("usuarioModifica") + "");
			ps.setDate(3, Date.valueOf(mapa.get("fechaModifica") + ""));
			ps.setString(4, mapa.get("horaModifica") + "");
			ps.setInt(5, Utilidades.convertirAEntero(mapa
					.get("codigoPkPlanSeccion")
					+ ""));

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("error valor del mapa >> " + mapa);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		return false;
	}

	/**
	 * Metodo para obtener el codigo de Plantilla asociado un paciente
	 * 
	 * @param con
	 * @param codPaciente
	 * @return
	 */
	public static int obtenerCodigoPlantillaXPacienteOdont(Connection con,
			int codPaciente) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		Statement st = null;
		ResultSetDecorator rs =  null;
		try {
			String consulta = "SELECT plantilla AS codigo_plantilla FROM manejopaciente.plantillas_pacientes  WHERE codigo_paciente= "
					+ codPaciente;
			 st = con.createStatement();
			 rs = new ResultSetDecorator(st.executeQuery(consulta));
			if (rs.next())
				codigoPlantilla = rs.getInt("codigo_plantilla");
		} catch (SQLException e) {
			logger.error("Error en obtenerCodigoPlantillaXPacienteOdont: " + e);
		}finally {			
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return codigoPlantilla;
	}

	/**
	 * Metodo para Guardar los campos parametrizables de la Plantilla de Ingreso
	 * PacienteOdontologia cuando el paciente no se le ha asigando Plantilla
	 * anteriormente
	 * 
	 * @param con
	 * @param plantilla
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean guardarCamposParametrizablesPacOdontologia(
			Connection con, DtoPlantilla plantilla, HashMap campos) {
		
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		String cadenaInsercionPlantilla = strInsertarPlantillPacientes;
		int codigoPlantillaPac = UtilidadBD.obtenerSiguienteValorSecuencia(con,
				"manejoPaciente.seq_plantillas_pacientes");
		int codigoPaciente = Utilidades.convertirAEntero(campos.get(
				"codigoPaciente").toString());
		String tipoFuncionalidad = campos.get("tipoFuncionalidad").toString();
		int codigoPlantilla = obtenerCodigoPlantillaXTipoFucionalidadIngresoPacOdont(
				con, tipoFuncionalidad);
		PreparedStatementDecorator pst = null;
				
		try {
			if (plantilla.tieneInformacion()) {

				pst = new PreparedStatementDecorator(
						con.prepareStatement(cadenaInsercionPlantilla));
				pst.setInt(1, codigoPlantillaPac);
				pst.setInt(2, codigoPaciente);
				pst.setInt(3, codigoPlantilla);
				pst.setString(4, campos.get("loginUsuario").toString());

				int resp = pst.executeUpdate();

				// *******************************************************************************************************
				logger.info("RESULTADO EJECUCION:=> " + resp);
				if (resp > 0) {
					// **********************************REGISTRO DE LOS CAMPOS
					// DE CADA ELEMENTO DE LA SECCION
					// FIJA***************************
					// Se itera cada seccion fija
					for (DtoSeccionFija seccionFija : plantilla
							.getSeccionesFijas()) {
						// Se itera cada elemento de la seccion fija
						for (DtoElementoParam elemento : seccionFija
								.getElementos()) {
							// Mientras que todo continue siendo exitoso
							if (resultado.isTrue()) {

								// Si el tipo elemento es SECCION:
								// ---------------------------------------------------------------
								if (elemento.isSeccion()) {
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
									logger.info("COdigoPlantilla >>"
											+ codigoPlantillaPac);
									resultado = insertarPlantillasPacCampos(
											con, seccion, codigoPlantillaPac,
											campos.get("loginUsuario")
													.toString());
								}
								// Si el tipo elemento es COMPONENTE
								// -----------------------------------------------------------------
								/*
								 * else if(elemento.isComponente()) {
								 * DtoComponente componente =
								 * (DtoComponente)elemento;
								 * 
								 * //Se itera cada seccion del componente
								 * for(DtoElementoParam
								 * elemenComp:componente.getElementos()) { //Se
								 * verifica si el elemento del componente es una
								 * seccion if(elemenComp.isSeccion()) {
								 * DtoSeccionParametrizable seccion =
								 * (DtoSeccionParametrizable)elemenComp; //SE
								 * retistran los campos de una seccion del
								 * componente resultado =
								 * insertarCampoComponenteIngreso( con, seccion,
								 * codigoIngreso, numeroSolicitud,
								 * codigoPaciente, fecha, hora, loginUsuario,
								 * consecutivoPlantillaIngreso, //consecutivo
								 * tabla plantillas_ingresos
								 * componente.getConsecutivoParametrizacion()
								 * //consecutivo tabla plantillas_componentes );
								 * } //Se verifica si el elemento del componente
								 * es una escala else if(elemenComp.isEscala())
								 * { DtoEscala escala = (DtoEscala)elemenComp;
								 * resultado =
								 * insertarEscalaIngreso(con,escala,codigoIngreso
								 * ,fecha,hora,loginUsuario,
								 * consecutivoPlantillaIngreso,true); } }
								 * 
								 * }
								 */
								// Si el tipo elemento es ESCALA
								// -----------------------------------------------------------------------
								else if (elemento.isEscala()) {
									DtoEscala escala = (DtoEscala) elemento;
									resultado = insertarEscalaPlantillaPacOdonto(
											con, escala, campos.get(
													"loginUsuario").toString(),
											codigoPlantillaPac, false, Utilidades.convertirAEntero(campos.get("institucion")+""));
								}
							} // Fin if resultado

						} // fin for elementos seccion fijas
					} // Fin for secciones fijas de la plantilla
					// *********************************************************************************************************

				} else {
					resultado.setResultado(false);
					resultado
							.setDescripcion("Problemas realizando el registro inicial de los valores parametrizables de la plantilla");
				}
			}
		} catch (SQLException e) {
			logger
					.error("Error en guardarValoresParametrizables Paciente Odontologia: "
							+ e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
			
		}
		return resultado;
	}

	/**
	 * Metodo para actualizar los Campos parametrizables de la plantilla
	 * Paciente Odontologia
	 * 
	 * @param con
	 * @param plantilla
	 * @param campos
	 * @return
	 */

	public static ResultadoBoolean modificarCamposParametrizablesPacOdontologia(
			Connection con, DtoPlantilla plantilla, HashMap campos) {
		
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		int codigoPlantillaPac = Utilidades.convertirAEntero(campos.get(
				"codigoPlantillaPac").toString());
/*		int codigoPaciente = Utilidades.convertirAEntero(campos.get(
				"codigoPaciente").toString());
		String tipoFuncionalidad = campos.get("tipoFuncionalidad").toString();
*/
		if (plantilla.tieneInformacion()) {
			// **********************************REGISTRO DE LOS CAMPOS DE CADA
			// ELEMENTO DE LA SECCION FIJA***************************
			// Se itera cada seccion fija
			for (DtoSeccionFija seccionFija : plantilla.getSeccionesFijas()) {
				// Se itera cada elemento de la seccion fija
				for (DtoElementoParam elemento : seccionFija.getElementos()) {
					// Mientras que todo continue siendo exitoso
					if (resultado.isTrue()) {

						// Si el tipo elemento es SECCION:
						// ---------------------------------------------------------------
						if (elemento.isSeccion()) {
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;

							resultado =  insertarPlantillasPacCampos(con,
									seccion, codigoPlantillaPac, campos.get(
											"loginUsuario").toString());
						}
						// Si el tipo elemento es COMPONENTE
						// -----------------------------------------------------------------
						/*
						 * else if(elemento.isComponente()) { DtoComponente
						 * componente = (DtoComponente)elemento;
						 * 
						 * //Se itera cada seccion del componente
						 * for(DtoElementoParam
						 * elemenComp:componente.getElementos()) { //Se verifica
						 * si el elemento del componente es una seccion
						 * if(elemenComp.isSeccion()) { DtoSeccionParametrizable
						 * seccion = (DtoSeccionParametrizable)elemenComp; //SE
						 * retistran los campos de una seccion del componente
						 * resultado = insertarCampoComponenteIngreso( con,
						 * seccion, codigoIngreso, numeroSolicitud,
						 * codigoPaciente, fecha, hora, loginUsuario,
						 * consecutivoPlantillaIngreso, //consecutivo tabla
						 * plantillas_ingresos
						 * componente.getConsecutivoParametrizacion()
						 * //consecutivo tabla plantillas_componentes ); } //Se
						 * verifica si el elemento del componente es una escala
						 * else if(elemenComp.isEscala()) { DtoEscala escala =
						 * (DtoEscala)elemenComp; resultado =
						 * insertarEscalaIngreso
						 * (con,escala,codigoIngreso,fecha,hora
						 * ,loginUsuario,consecutivoPlantillaIngreso,true); } }
						 * 
						 * }
						 */
						// Si el tipo elemento es ESCALA
						// -----------------------------------------------------------------------
						else if (elemento.isEscala()) {
							DtoEscala escala = (DtoEscala) elemento;
							resultado = insertarEscalaPlantillaPacOdonto(con,
									escala, campos.get("loginUsuario")
											.toString(), codigoPlantillaPac,
									false, Utilidades.convertirAEntero(campos.get("institucion")+""));
						}
					} // Fin if resultado

				} // fin for elementos seccion fijas
			} // Fin for secciones fijas de la plantilla
			// *********************************************************************************************************

		}

		return resultado;
	}

	/**
	 * Metodo para insertar Campos de Plantillas Paciente Odontologia
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	private static ResultadoBoolean insertarPlantillasPacCampos(Connection con,
			DtoSeccionParametrizable seccion, int conscutivoPlantillaPaciente,
			String loginUsuario) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		String cadenaInsertarPacCamp = strInsertarPlantillasPacCampos;
		String cadenaInsertarValores = strInsertarValoresPlantPacCampos;

		String cadenaModificar = strModificarPlantillasPacCampos;
		String modificarValores = strModificarValoresPlantPacCampos;
		String modificarValores2 = strModificarValoresPlantPacCampos2;
		PreparedStatementDecorator pst = null;
		PreparedStatementDecorator pst2 = null;
		try {
			int resp0 = 0, resp1 = 0;

			for (DtoCampoParametrizable campo : seccion.getCampos())
				if (resultado.isTrue() /*
										 * mientras que todo marche bien se
										 * continua
										 */) {
					int consecutivoPlantillaIngCampo = 0;
					logger.info("consecutivoHistorico CAMPO : "
							+ campo.getConsecutivoHistorico());
					// EXISTE HISTORICO - SE PROCEDE A MODIFICAR
					if (campo.getConsecutivoHistorico() != null
							&& !campo.getConsecutivoHistorico().equals("")
							&& Utilidades.convertirAEntero(campo
									.getConsecutivoHistorico()) > 0) {
						consecutivoPlantillaIngCampo = Utilidades
								.convertirAEntero(campo
										.getConsecutivoHistorico());

						try {
							pst = new PreparedStatementDecorator(
									con.prepareStatement(cadenaModificar));
							logger.info("Consulta >>" + cadenaModificar
									+ "  Consecutivo "
									+ campo.getConsecutivoParametrizacion()
									+ "Plantilla >> "
									+ conscutivoPlantillaPaciente);

							/*
							 * " "nombre_archivo_original = ? , " +
							 * "fecha_modificacion = CURRENT_DATE , " +
							 * "hora_modifiacion = "
							 * +ValoresPorDefecto.getSentenciaHoraActualBD
							 * ()+" , " + "usuario_modificacion = ? " + "WHERE
							 * codigo_pk = ? AND plantilla_paciente = ?
							 */

							if (!campo.getNombreArchivoOriginal().equals(""))
								pst.setString(1, campo
										.getNombreArchivoOriginal());
							else
								pst.setNull(1, Types.VARCHAR);
							pst.setString(2, loginUsuario);
							pst
									.setDouble(
											3,
											Utilidades
													.convertirADouble(consecutivoPlantillaIngCampo
															+ ""));
							pst.setInt(4, conscutivoPlantillaPaciente);

							resp0 = pst.executeUpdate();
							pst.close();

						} catch (SQLException e) {
							resp0 = 0;
							resultado.setResultado(false);
							resultado
									.setDescripcion("Ocurriï¿½ error insertando un campo de la plantilla: "
											+ e);
							logger
									.error("Error en modificar campo de Plantilla: "
											+ e);
						}

						if (resp0 > 0) {
							pst2 = new PreparedStatementDecorator(con.prepareStatement(cadenaModificar));
							// ***************MODIFICACION DE LOS VALORES DEL
							// CAMPO*******************************************************
							// Si el campo es de tipo CHECKBOX ï¿½ tipo SELECT
							// con imagen la forma de registrar su informaciï¿½n
							// es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {

								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {
									logger.info("-->" + opcion.getValor()
											+ "<--");
									logger.info("codigo Historico >>"
											+ opcion.getCodigoHistorico());
									if (!opcion.getCodigoHistorico().equals("")
											&& Utilidades
													.convertirAEntero(opcion
															.getCodigoHistorico()) > 0) {

										pst2 = new PreparedStatementDecorator(
												con
														.prepareStatement(
																modificarValores));

										if (!opcion.getValor().equals(""))
											pst2
													.setString(1, opcion
															.getValor());
										else
											pst2.setNull(1, Types.VARCHAR);

										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst2
													.setString(
															2,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst2.setNull(2, Types.VARCHAR);

										InfoDatosStr nombreArchivo = opcion
												.generarImagen(opcion
														.getCodigoHistorico()
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst2.close();
										} else {
											if (!nombreArchivo.equals(""))
												pst2.setString(3, nombreArchivo
														.getCodigo());
											else
												pst2.setNull(3, Types.VARCHAR);

											if (!opcion.getSeleccionado()
													.equals(""))
												pst2.setString(4, opcion
														.getSeleccionado());
											else
												pst2.setNull(4, Types.VARCHAR);

											if (!opcion.getCodConvencion()
													.equals("")
													&& Utilidades
															.convertirAEntero(opcion
																	.getCodConvencion()
																	+ "") > 0)
												pst2.setString(5, opcion
														.getCodConvencion());
											else
												pst2.setNull(5, Types.INTEGER);

											pst2
													.setDouble(
															6,
															Utilidades
																	.convertirADouble(opcion
																			.getCodigoHistorico()
																			+ ""));

											resp1 = pst2.executeUpdate();
											pst2.close();

										}

									} else {

										pst2 = new PreparedStatementDecorator(
												con
														.prepareStatement(
																cadenaInsertarValores));

										int consecutivoValorPlantillaPacOdonto = UtilidadBD
												.obtenerSiguienteValorSecuencia(
														con,
														"manejoPaciente.seq_val_plan_pac_camp");
										/*
										 * codigo_pk, " +
										 * "plantilla_pac_campo, " + "valor, " +
										 * "valores_opcion," + "imagen_opcion,"
										 * + "seleccionado, " + "convencion ) "
										 * + "VALUES (?, ?, ?, ?, ?, ?, ?)";
										 */

										pst2
												.setDouble(
														1,
														Utilidades
																.convertirADouble(consecutivoValorPlantillaPacOdonto
																		+ ""));
										pst2
												.setDouble(
														2,
														Utilidades
																.convertirADouble(consecutivoPlantillaIngCampo
																		+ ""));
										if (!opcion.getValor().equals(""))
											pst2
													.setString(3, opcion
															.getValor());
										else
											pst2.setNull(3, Types.VARCHAR);

										if (!opcion
												.getValoresOpcionRegistrado()
												.equals(""))
											pst2
													.setString(
															4,
															opcion
																	.getValoresOpcionRegistrado());
										else
											pst2.setNull(4, Types.VARCHAR);

										InfoDatosStr nombreArchivo = opcion
												.generarImagen(consecutivoValorPlantillaPacOdonto
														+ "");

										if (!nombreArchivo.isIndicador()
												&& !nombreArchivo.getCodigo()
														.equals("")) {
											resultado.setResultado(false);
											resultado
													.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
															+ opcion.getValor()
															+ " del campo "
															+ campo
																	.getEtiqueta()
															+ " en la seccion "
															+ seccion
																	.getDescripcion());
											pst2.close();
										} else {
											if (!nombreArchivo.equals(""))
												pst2.setString(5, nombreArchivo
														.getCodigo());
											else
												pst2.setNull(5, Types.VARCHAR);

											if (!opcion.getSeleccionado()
													.equals(""))
												pst2.setString(6, opcion
														.getSeleccionado());
											else
												pst2.setNull(6, Types.VARCHAR);

											if (!opcion.getCodConvencion()
													.equals("")
													&& Utilidades
															.convertirAEntero(opcion
																	.getCodConvencion()
																	+ "") > 0)
												pst2.setString(7, opcion
														.getCodConvencion());
											else
												pst2.setNull(7, Types.INTEGER);

											resp1 = pst2.executeUpdate();
											pst2.close();

										}

									}

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el valor "
														+ opcion.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
									}

								}
							}
							// Si el campo es diferente de tipo CHECKBOX y
							// SELECT con imagen solo tiene un valor para
							// insertar
							else {
								pst2 = new PreparedStatementDecorator(
										con
												.prepareStatement(modificarValores2));

								/*
								 * "valor =  ?, " + "valores_opcion = ?," +
								 * "imagen_opcion = ?," + "seleccionado = ?, " +
								 * "convencion = ? " +
								 * "WHERE plantilla_pac_campo = ? "
								 */
								if (!campo.getValor().equals(""))
									pst2.setString(1, campo.getValor());
								else
									pst2.setNull(1, Types.VARCHAR);

								if (!campo.getValoresOpcion().equals(""))
									pst2.setString(2, campo.getValoresOpcion());
								else
									pst2.setNull(2, Types.VARCHAR);

								pst2.setNull(3, Types.VARCHAR);
								pst2.setNull(4, Types.VARCHAR);
								pst2.setNull(5, Types.INTEGER);

								pst2.setInt(6, consecutivoPlantillaIngCampo);

								resp1 = pst2.executeUpdate();
								pst2.close();

								if (resp1 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}

							}

						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}

						// NO EXISTE HISTORICO ****** INSERTAR ******
					} else {
						logger.info("No existe campo y lo va INSERTAR");
						consecutivoPlantillaIngCampo = UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"manejoPaciente.seq_plantillas_pac_campos");
						// ***************************SE REGISTRA EL
						// CAMPO******************************************************

						pst = new PreparedStatementDecorator(
								con.prepareStatement(cadenaInsertarPacCamp));

						pst.setDouble(1, Utilidades
								.convertirADouble(consecutivoPlantillaIngCampo
										+ ""));
						pst.setInt(2, conscutivoPlantillaPaciente);
						pst.setInt(3, Utilidades.convertirAEntero(campo
								.getConsecutivoParametrizacion()));

						if (!campo.getNombreArchivoOriginal().equals(""))
							pst.setString(4, campo.getNombreArchivoOriginal());
						else
							pst.setNull(4, Types.VARCHAR);
						pst.setString(5, loginUsuario);

						resp0 = pst.executeUpdate();
						// *******************************************************************************
						logger.info("INSERTO CAMPO  cadena "
								+ cadenaInsertarPacCamp + " RESP >>" + resp0);

						if (resp0 > 0) {
							// ***************INSERCION DE LOS VALORES DEL
							// CAMPO*******************************************************
							// Si el campo es de tipo CHECKBOX la forma de
							// registrar su informaciï¿½n es diferente
							if (campo
									.getTipoHtml()
									.equals(
											ConstantesCamposParametrizables.campoTipoCheckBox)
									|| (campo
											.getTipoHtml()
											.equals(
													ConstantesCamposParametrizables.campoTipoSelect) && campo
											.getManejaImagen().equals(
													ConstantesBD.acronimoSi))) {
								for (DtoOpcionCampoParam opcion : campo
										.getOpciones()) {
									logger.info("-->" + opcion.getValor()
											+ "<--");

									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															cadenaInsertarValores,
															ConstantesBD.typeResultSet));

									int consecutivoValorPlantillaPacOdonto = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"manejoPaciente.seq_val_plan_pac_camp");
									/*
									 * codigo_pk, " + "plantilla_pac_campo, " +
									 * "valor, " + "valores_opcion," +
									 * "imagen_opcion," + "seleccionado, " +
									 * "convencion ) " +
									 * "VALUES (?, ?, ?, ?, ?, ?, ?)";
									 */

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoValorPlantillaPacOdonto
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(consecutivoPlantillaIngCampo
																	+ ""));
									if (!opcion.getValor().equals(""))
										pst.setString(3, opcion.getValor());
									else
										pst.setNull(3, Types.VARCHAR);

									if (!opcion.getValoresOpcionRegistrado()
											.equals(""))
										pst.setString(4, opcion
												.getValoresOpcionRegistrado());
									else
										pst.setNull(4, Types.VARCHAR);

									InfoDatosStr nombreArchivo = opcion
											.generarImagen(consecutivoValorPlantillaPacOdonto
													+ "");

									if (!nombreArchivo.isIndicador()
											&& !nombreArchivo.getCodigo()
													.equals("")) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas generando la Imagen para la opciï¿½n "
														+ opcion.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
										pst.close();
									} else {
										if (!nombreArchivo.equals(""))
											pst.setString(5, nombreArchivo
													.getCodigo());
										else
											pst.setNull(5, Types.VARCHAR);

										if (!opcion.getSeleccionado()
												.equals(""))
											pst.setString(6, opcion
													.getSeleccionado());
										else
											pst.setNull(6, Types.VARCHAR);

										if (!opcion.getCodConvencion().equals(
												"")
												&& Utilidades
														.convertirAEntero(opcion
																.getCodConvencion()
																+ "") > 0)
											pst.setString(7, opcion
													.getCodConvencion());
										else
											pst.setNull(7, Types.INTEGER);

										resp1 = pst.executeUpdate();
										pst.close();

									}

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el valor "
														+ opcion.getValor()
														+ " del campo "
														+ campo.getEtiqueta()
														+ " en la seccion "
														+ seccion
																.getDescripcion());
									}

								}
							}
							// Si el campo es diferente de tipo CHECKBOX solo
							// tiene un valor para insertar
							else {
								logger.info("-->" + campo.getValor() + "<--");

								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														cadenaInsertarValores,
														ConstantesBD.typeResultSet));
								int consecutivoValorPlantillaPacOdonto = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"manejoPaciente.seq_val_plan_pac_camp");

								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoValorPlantillaPacOdonto
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(consecutivoPlantillaIngCampo
																+ ""));

								if (!campo.getValor().equals(""))
									pst.setString(3, campo.getValor());
								else
									pst.setNull(3, Types.VARCHAR);

								if (!campo.getValoresOpcion().equals(""))
									pst.setString(4, campo.getValoresOpcion());
								else
									pst.setNull(4, Types.VARCHAR);

								pst.setNull(5, Types.VARCHAR);

								pst.setNull(6, Types.VARCHAR);

								pst.setNull(7, Types.INTEGER);

								resp1 = pst.executeUpdate();
								pst.close();

								if (resp1 <= 0) {

									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas registrando el valor "
													+ campo.getValor()
													+ " del campo "
													+ campo.getEtiqueta()
													+ " en la seccion "
													+ seccion.getDescripcion());
								}

							}

						} else {
							resultado.setResultado(false);
							resultado
									.setDescripcion("Problemas registrando la informaciï¿½n del campo "
											+ campo.getEtiqueta()
											+ " en la seccion "
											+ seccion.getDescripcion());
						}

					}
				}
			// ****************REGISTRAR INFORMACION DE LAS
			// SUBSECCIONES***********************
			if (resultado.isTrue())
				for (DtoSeccionParametrizable subseccion : seccion
						.getSecciones())
					resultado = insertarPlantillasPacCampos(con, subseccion,
							conscutivoPlantillaPaciente, loginUsuario);
			// *********************************************************************************
		} catch (SQLException e) {
			resultado.setResultado(false);
			resultado
					.setDescripcion("Ocurriï¿½ error insertando un campo de la plantilla: "
							+ e);
			logger.error("Error en insertar campo de Plantilla: " + e);
		}finally {			
			try{
			if(pst!=null ){
					pst.close();					
			}		
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
			}
			try{
				if(pst2!=null ){
						pst2.close();					
				}		
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
			}
	
		}
		return resultado;
	}

	/**
	 * Metodo para insertar los valores de una escala de la plantilla Paciente
	 * odontologia
	 * 
	 * @param con
	 * @param escala
	 * @param codigoIngreso
	 * @param loginUsuario
	 * @param consecutivoPlantillaPlanPaciente
	 * @param vieneDeComponente
	 * @return
	 */
	private static ResultadoBoolean insertarEscalaPlantillaPacOdonto(
			Connection con, DtoEscala escala, String loginUsuario,
			int consecutivoPlantillaPlanPaciente, boolean vieneDeComponente, int institucion) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		PreparedStatementDecorator pst = null;
		try {
			int resp0 = 0, resp1 = 0;
			boolean ingresado = false;

			// if(escala.getCodigoPK())
			// Se verifica si la escala fue
			// llenada-------------------------------------------------------------
			for (DtoSeccionParametrizable seccion : escala.getSecciones())
				for (DtoCampoParametrizable campo : seccion.getCampos())
					if (!campo.getValor().equals("")
							|| !campo.getObservaciones().equals(""))
						ingresado = true;

			if (escala.getTotalEscala() > 0
					|| !escala.getObservaciones().equals("")
					|| escala.getNumArchivosAdjuntos() > 0)
				ingresado = true;
			// -------------------------------------------------------------------------------------------------------

			// EXISTE ESCALA SE MODIFICA
			logger.info("consecutivo escala >> "
					+ escala.getConsecutivoHistorico());
			if (Utilidades.convertirAEntero(escala.getConsecutivoHistorico()) > 0) {
				logger.info("EXISTE ESCALA MODIFICA consecutivo escala >> "
						+ escala.getConsecutivoHistorico());
				 pst = new PreparedStatementDecorator(con,strModificarEscalaIngresoXIngPacienteOdon);
				/*
				 * "escala_factor_prediccion = ?," + "valor_total = ?, " +
				 * "observaciones = ?, " + "fecha = ?, " + "hora = ?, " +
				 * "fecha_modifica = CURRENT_DATE, " +
				 * "hora_modifica = "+ValoresPorDefecto
				 * .getSentenciaHoraActualBD()+"," + "usuario_modifica = ? " +
				 * "WHERE codigo_pk = ? ";
				 */
				if (!escala.getCodigoFactorPrediccion().equals("")
						&& Utilidades.convertirAEntero(escala
								.getCodigoFactorPrediccion()) > 0)
					pst.setDouble(1, Utilidades.convertirADouble(escala
							.getCodigoFactorPrediccion()));
				else
					pst.setNull(1, Types.NUMERIC);
				pst.setDouble(2, escala.getTotalEscala());
				if (!escala.getObservaciones().equals(""))
					pst.setString(3, escala.getObservaciones());
				else
					pst.setNull(3, Types.VARCHAR);
				pst.setString(4, UtilidadFecha
						.conversionFormatoFechaABD(UtilidadFecha
								.getFechaActual()));
				pst.setString(5, UtilidadFecha.getHoraActual());
				pst.setString(6, loginUsuario);
				pst.setInt(7, Utilidades.convertirAEntero(escala
						.getConsecutivoHistorico()));

				logger.info(pst.toString());
				
				resp0 = pst.executeUpdate();

				if (resp0 > 0) {

					// Se iteran las secciones y los campos de la escala
					for (DtoSeccionParametrizable seccion : escala
							.getSecciones())
						for (DtoCampoParametrizable campo2 : seccion
								.getCampos()) {

							// *****************SE MODIFICA EL CAMPO DE CADA
							// ESCALA**************************************
							pst = new PreparedStatementDecorator(
									con,	strmodificarEscalaCampoIngresoXIngPacienteOdon);

							/*
							 * "valor = ?, " + "observaciones = ? " +
							 * "WHERE  escala_ingreso = ? AND escala_campo_seccion = ?"
							 * ;
							 */

							pst.setDouble(1, Utilidades.convertirADouble(campo2
									.getValor(), true));
							if (!campo2.getObservaciones().equals(""))
								pst.setString(2, campo2.getObservaciones());
							else
								pst.setNull(2, Types.VARCHAR);

							pst.setInt(3, Utilidades.convertirAEntero(escala
									.getConsecutivoHistorico()));
							pst.setDouble(4, Utilidades.convertirADouble(campo2
									.getConsecutivoParametrizacion()));

							resp1 = pst.executeUpdate();

							// *****************************************************************************************

							logger.info("88888888888888888888888888888888888888888888888888888888888888888888pst-->"+pst);
							//&& !escala.getCodigoPK().equals(ValoresPorDefecto.getEscalaPacientePerfil(institucion))
							
							if (resp1 <= 0 ) 
							{
								logger.info("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERRRRRRRRRRRRRRRRRRRORRRRRRRRRRRRRR");
								resultado.setResultado(false);
								resultado
										.setDescripcion("Problemas Modificando el campo "
												+ campo2.getEtiqueta()
												+ " la escala "
												+ escala.getDescripcion());
							}
						}
				} else {
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas modificando la escala "
							+ escala.getDescripcion());
				}

				// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE LA
				// ESCALA*****************************************************
				if (resultado.isTrue()) {
					for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
						String adjEscala = "adjEscala" + escala.getCodigoPK()
								+ "_" + i;
						String adjEscalaOriginal = "adjEscalaOriginal"
								+ escala.getCodigoPK() + "_" + i;
						String adjCodigoPk = "adjCodigoPk"
								+ escala.getCodigoPK() + "_" + i;

						// SI NO Existia Archivo Adjunto INSERTA
						if (escala.getArchivosAdjuntos(adjCodigoPk) == null
								|| escala.getArchivosAdjuntos(adjCodigoPk)
										.toString().equals("")) {
							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala)
											.toString().equals("")
									&& resultado.isTrue()) {

								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarAdjuntosEscalaIngreso));

								/**
								 * INSERT INTO escalas_adjuntos_ingreso " +
								 * "(codigo_pk," + "escala_ingreso," +
								 * "nombre_archivo," + "nombre_original) " +
								 * "VALUES " + "(?,?,?,?)
								 */

								int consecutivoAdjuntoEscalaIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_escalas_adjuntos_ing");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoAdjuntoEscalaIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(Utilidades
																.convertirAEntero(escala
																		.getConsecutivoHistorico())
																+ ""));
								pst.setString(3, escala.getArchivosAdjuntos(
										adjEscala).toString());
								pst.setString(4, escala.getArchivosAdjuntos(
										adjEscalaOriginal).toString());

								resp0 = pst.executeUpdate();

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							}

						} else {
							// SI EXISTIA ELIMINA
							if (escala.getArchivosAdjuntos(adjCodigoPk) != null
									&& !escala.getArchivosAdjuntos(adjCodigoPk)
											.toString().equals("")) {
								if (escala.getArchivosAdjuntos(adjEscala) == null
										&& escala
												.getArchivosAdjuntos(adjEscala)
												.toString().equals("")
										&& resultado.isTrue()) {

									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strEliminarAdjuntosEscalaIngresoXIngrPacienteOdonto));

									/*
									 * DELETE
									 * historiaclinica.escalas_adjuntos_ingreso
									 * WHERE codigo_pk = ?
									 */

									pst.setString(1, escala
											.getArchivosAdjuntos(adjCodigoPk)
											.toString());

									resp0 = pst.executeUpdate();

									if (resp0 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas eliminando el archivo adjunto "
														+ escala
																.getArchivosAdjuntos(adjEscalaOriginal)
														+ " de la escala "
														+ escala
																.getDescripcion());
									}
								}
							}
						}
					}
				}

			} // NO EXISTE SE INSERTA
			else {
				logger.info("NO EXISTE ESCALA INSERTA ");

				// Se verifica si la escala fue diligenciada
				if (ingresado) {
					// *****************SE INSERTA EL ENCABEZADO DEL REGISTRO DE
					// LA ESCALA X INGRESO**********************************
					pst = new PreparedStatementDecorator(con.prepareStatement(strInsertarEscalaIngreso));

					/**
					 * INSERT INTO escalas_ingresos " + "(codigo_pk," +
					 * "ingreso," + "escala," + "escala_factor_prediccion," +
					 * "valor_total," + "observaciones," + "fecha," + "hora," +
					 * "fecha_modifica," + "hora_modifica," +
					 * "usuario_modifica) " + "VALUES " +
					 * "(?,?,?,?,?,?,?,?,CURRENT_DATE,"
					 * +ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
					 */

					int consecutivoEscalaIngreso = UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_escalas_ingresos");
					pst.setDouble(1, Utilidades
							.convertirADouble(consecutivoEscalaIngreso + ""));
					pst.setNull(2, Types.INTEGER);
					pst.setDouble(3, Utilidades.convertirADouble(escala
							.getCodigoPK()));
					
					
					boolean esEscalaPerfilNed= escala.getCodigoPK().equals(ValoresPorDefecto.getEscalaPacientePerfil(institucion));
					
					// Tambiï¿½n se debe verificar que el codigo del factor de
					// prediccion sea un codigo vï¿½lido
					if (!escala.getCodigoFactorPrediccion().equals("")
							&& Utilidades.convertirAEntero(escala
									.getCodigoFactorPrediccion()) > 0)
						pst.setDouble(4, Utilidades.convertirADouble(escala
								.getCodigoFactorPrediccion()));
					else
						pst.setNull(4, Types.NUMERIC);
					pst.setDouble(5, escala.getTotalEscala());
					if (!escala.getObservaciones().equals(""))
						pst.setString(6, escala.getObservaciones());
					else
						pst.setNull(6, Types.VARCHAR);
					pst.setString(7, UtilidadFecha
							.conversionFormatoFechaABD(UtilidadFecha
									.getFechaActual()));
					pst.setString(8, UtilidadFecha.getHoraActual());
					pst.setString(9, loginUsuario);

					resp0 = pst.executeUpdate();
					// ****************************************************************************************************************

					if (resp0 > 0) {

						// Se iteran las secciones y los campos de la escala
						for (DtoSeccionParametrizable seccion : escala
								.getSecciones())
							for (DtoCampoParametrizable campo : seccion
									.getCampos())
								if ((	Utilidades.convertirADouble(campo.getValor()) > 0 
										|| !campo.getObservaciones().trim().equals("") || esEscalaPerfilNed)
										&& resultado.isTrue()) {
									// *****************SE INSERTA EL CAMPO DE
									// CADA
									// ESCALA**************************************
									pst = new PreparedStatementDecorator(
											con
													.prepareStatement(
															strInsertarEscalaCampoIngreso));
									int consecutivoEscalaCampoIngreso = UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_escalas_campos_ing");

									/**
									 * INSERT INTO escalas_campos_ingresos " +
									 * "(codigo_pk," + "escala_campo_seccion," +
									 * "escala_ingreso," + "valor," +
									 * "observaciones) " + "VALUES " +
									 * "(?,?,?,?,?)
									 */

									pst
											.setDouble(
													1,
													Utilidades
															.convertirADouble(consecutivoEscalaCampoIngreso
																	+ ""));
									pst
											.setDouble(
													2,
													Utilidades
															.convertirADouble(campo
																	.getConsecutivoParametrizacion()));
									pst
											.setDouble(
													3,
													Utilidades
															.convertirADouble(consecutivoEscalaIngreso
																	+ ""));
									pst.setDouble(4, Utilidades
											.convertirADouble(campo.getValor(),
													true));
									if (!campo.getObservaciones().equals(""))
										pst.setString(5, campo
												.getObservaciones());
									else
										pst.setNull(5, Types.VARCHAR);

									resp1 = pst.executeUpdate();

									// *****************************************************************************************

									if (resp1 <= 0) {
										resultado.setResultado(false);
										resultado
												.setDescripcion("Problemas registrando el campo "
														+ campo.getEtiqueta()
														+ " la escala "
														+ escala
																.getDescripcion());
									}
								}
					} else {
						resultado.setResultado(false);
						resultado
								.setDescripcion("Problemas registrando la escala "
										+ escala.getDescripcion());
					}

					// ***********************SE RELACIONA EL REGISTRO DE LA
					// ESCALA CON LA PLANTILLA PACIENTES
					// ODONTOLOGIA*******************************
					if (resultado.isTrue()) {
						/**
						 * Cadena Sql para insertar un nuevo registro en la
						 * tabla plantillas_escala_pac
						 */
						String strInsertarPlantEscalaPaciente = "INSERT INTO manejopaciente.plantillas_escala_pac ( "
								+ "plantilla_paciente, "
								+ "escala_ingreso, "
								+ "plantilla_escala, "
								+ "componente, "
								+ "escala ) " + "VALUES (?, ?, ?, ?, ?)";

						pst = new PreparedStatementDecorator(con
								.prepareStatement(
										strInsertarPlantEscalaPaciente));

						/**
						 * INSERT INTO manejopaciente.plantillas_escala_pac ( "
						 * + "plantilla_paciente, " + "escala_ingreso, " +
						 * "plantilla_escala, " + "componente, " + "escala ) " +
						 * "VALUES (?, ?, ?, ?, ?)
						 */

						pst.setDouble(1,Utilidades.convertirADouble(consecutivoPlantillaPlanPaciente+ ""));
						pst.setDouble(2,Utilidades.convertirADouble(consecutivoEscalaIngreso+ ""));
						// Dependiendo si la escala hace parte o no de un
						// componente se relaciona de forma diferente
						if (vieneDeComponente) {
							pst.setNull(3, Types.INTEGER);
							String[] vectorConsecutivo = escala.getConsecutivoParametrizacion().split(ConstantesBD.separadorSplit);
							pst.setDouble(4, Utilidades.convertirADouble(vectorConsecutivo[0]));
							pst.setDouble(5, Utilidades.convertirADouble(vectorConsecutivo[1]));
						} else {
							pst.setInt(3, Utilidades.convertirAEntero(escala.getConsecutivoParametrizacion()));
							pst.setNull(4, Types.NUMERIC);
							pst.setNull(5, Types.NUMERIC);
						}

						resp0 = pst.executeUpdate();

						if (resp0 <= 0) {
							resultado.setResultado(false);
							resultado.setDescripcion("Problemas al relacionar la escala "+ escala.getDescripcion()+ " con el registro de la plantilla");
						}
					}
					// ********************************************************************************************************************

					// *******************SE INSERTAN LOS ARCHIVOS ADJUNTOS DE
					// LA
					// ESCALA*****************************************************
					if (resultado.isTrue()) {
						for (int i = 0; i < escala.getNumArchivosAdjuntos(); i++) {
							String adjEscala = "adjEscala"
									+ escala.getCodigoPK() + "_" + i;
							String adjEscalaOriginal = "adjEscalaOriginal"
									+ escala.getCodigoPK() + "_" + i;

							if (escala.getArchivosAdjuntos(adjEscala) != null
									&& !escala.getArchivosAdjuntos(adjEscala).toString().equals("")
									&& resultado.isTrue()) {

								pst = new PreparedStatementDecorator(
										con
												.prepareStatement(
														strInsertarAdjuntosEscalaIngreso));

								/**
								 * INSERT INTO escalas_adjuntos_ingreso " +
								 * "(codigo_pk," + "escala_ingreso," +
								 * "nombre_archivo," + "nombre_original) " +
								 * "VALUES " + "(?,?,?,?)
								 */

								int consecutivoAdjuntoEscalaIngreso = UtilidadBD
										.obtenerSiguienteValorSecuencia(con,
												"seq_escalas_adjuntos_ing");
								pst
										.setDouble(
												1,
												Utilidades
														.convertirADouble(consecutivoAdjuntoEscalaIngreso
																+ ""));
								pst
										.setDouble(
												2,
												Utilidades
														.convertirADouble(consecutivoEscalaIngreso
																+ ""));
								pst.setString(3, escala.getArchivosAdjuntos(
										adjEscala).toString());
								pst.setString(4, escala.getArchivosAdjuntos(
										adjEscalaOriginal).toString());

								resp0 = pst.executeUpdate();

								if (resp0 <= 0) {
									resultado.setResultado(false);
									resultado
											.setDescripcion("Problemas ingresando el archivo adjunto "
													+ escala
															.getArchivosAdjuntos(adjEscalaOriginal)
													+ " de la escala "
													+ escala.getDescripcion());
								}
							}
						}
					}
					// **********************************************************************************************************************
				}
			}

		} catch (SQLException e) {
			logger.error("Error en insertarEscalaPaciente: " + e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}
		return resultado;
	}

	/**
	 * Metodo para obtener el codigo_pk de la plantilla de la tabla
	 * historiaclinica.plantillas que esta asociada la funcionalidad
	 * parametrizable informacionPacienteOdontologico y cuyo tipo de funcion es
	 * parametro de entrada (reservar o asignar)
	 * 
	 * @param con
	 * @param tipoFuncionalidad
	 * @return
	 */
	public static int obtenerCodigoPlantillaXTipoFucionalidadIngresoPacOdont(
			Connection con, String tipoFuncionalidad) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		String consulta = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try {
			consulta = "SELECT codigo_pk, codigo_plantilla FROM historiaclinica.plantillas WHERE fun_param = "
					+ ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico
					+ " "
					+ "AND tipo_funcionalidad = '"
					+ tipoFuncionalidad
					+ "'";

			 st = con.createStatement();
			 rs = new ResultSetDecorator(st	.executeQuery(consulta));
			if (rs.next())
				codigoPlantilla = rs.getInt("codigo_pk");
		} catch (SQLException e) {
			logger.info("Consulta Obtener CodigoPlantilla  " + consulta
					+ " tipoFuncionalidad = " + tipoFuncionalidad);
			logger.error("Error en obtenerCodigoPlantillaXFuncionalidad: " + e);
		}finally {		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}				
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
	
		}
		logger.info(" CODIGO  " + codigoPlantilla);
		return codigoPlantilla;
	}

	/**
	 * Metodo para obtener Codigo de Plantilla Valoracion Consulta Externa
	 * Odontologia asociada a un pacinte
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerCodigoPlantillaValoracionConsultaExtOdonto(
			Connection con, HashMap campos) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		String consulta = strConsultarCodigoPlantillaFucionParametrizableValoracion;
		PreparedStatementDecorator st = null;
		ResultSetDecorator rs  = null;
		try {
			st = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			st.setInt(1, Utilidades.convertirAEntero(campos.get("codPaciente")+""));
			st.setInt(2, Utilidades.convertirAEntero(campos.get("codValoracionOdont")+""));
			st.setInt(3, ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia);

			rs = new ResultSetDecorator(st.executeQuery());
			if (rs.next())
				codigoPlantilla = rs.getInt("codigopk");
		} catch (SQLException e) {
			logger.info("Consulta Obtener CodigoPlantilla  " + consulta);
			logger.error("Error en obtenerCodigoPlantillaXFuncionalidad: " + e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
	
		}
		logger.info(" CODIGO  Plantilla " + codigoPlantilla);

		return codigoPlantilla;
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerCodigoPlantillaEvolucionOdonto(Connection con,
			HashMap campos) {
		int codigoPlantilla = ConstantesBD.codigoNuncaValido;
		String consulta = strConsultarCodigoPlantillaFucionParametrizableEvolucion;
		PreparedStatementDecorator st = null;
		ResultSetDecorator rs  = null;
		try {
			 st = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			st.setInt(1, Utilidades.convertirAEntero(campos.get(
					"codEvolucionOdonto").toString()));
			st
					.setInt(
							2,
							ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica);

			rs = new ResultSetDecorator(st.executeQuery());
			if (rs.next())
				codigoPlantilla = rs.getInt("codigopk");
		} catch (SQLException e) {
			logger.info("Consulta Obtener CodigoPlantilla  " + consulta);
			logger.error("Error en obtenerCodigoPlantillaXFuncionalidad: " + e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		
		}
		logger.info(" CODIGO  Plantilla " + codigoPlantilla);

		return codigoPlantilla;
	}

	/**
	 * Metodo que retorna un array list con las especialidades
	 * 
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEspecialidadesConPlantillaParametrizada(
			Connection con, HashMap campos) {
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String, Object>>();
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs  = null;
		try {
			// *****************FILTROS PARA OBTENER
			// ESPECIALIDADES*****************************
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get(
					"codigoEspecialidad").toString(), true);
			// int codigoMedico =
			// Utilidades.convertirAEntero(campos.get("codigoMedico").toString(),
			// true);
			String tipoEspecialidad = campos.get("tipoEspecialidad").toString();
			int funParam = Utilidades.convertirAEntero(campos.get("funParam")
					.toString());
			// *********************************************************************************

			String seccionSELECT = "SELECT DISTINCT "
					+ "e.codigo as codigoespecialidad,"
					+ "e.nombre as nombreespecialidad ";

			seccionSELECT += "FROM especialidades e "
					+ "INNER JOIN  historiaclinica.plantillas p ON (p.especialidad=e.codigo) ";

			String seccionWHERE = "WHERE 1=1 ";

			if (codigoEspecialidad > 0)
				seccionWHERE += " AND e.codigo = " + codigoEspecialidad;

			if (!tipoEspecialidad.isEmpty())
				seccionWHERE += " AND e.tipo_especialidad='" + tipoEspecialidad
						+ "'";

			if (funParam != ConstantesBD.codigoNuncaValido)
				seccionWHERE += " AND p.fun_param=" + funParam + "";

			String cadena = seccionSELECT + seccionWHERE
					+ " ORDER BY e.nombre DESC";
			logger.info("\n la cadena --> " + cadena);
			 ps = new PreparedStatementDecorator(con
					.prepareStatement(cadena));
			 rs = new ResultSetDecorator(ps.executeQuery());
			int i = 0;
			while (rs.next()) {
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("codigoespecialidad", rs.getObject(1));
				mapa.put("nombreespecialidad", rs.getObject(2));
				resultado.add(i, mapa);
			}
		} catch (SQLException e) {
			logger
					.error("Error en obtener las especialidades de SqlBaseUtilidadesDao: "
							+ e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}
		return resultado;
	}

	/**
	 * Metodo para cargar un componente de Antecedentes Odontologicos
	 * 
	 * @param con
	 * @param campos
	 */
	public static DtoComponente cargarComponenteAntecedentesOdonto(
			HashMap campos) {
		DtoComponente componente = new DtoComponente();
		int codPaciente = Utilidades.convertirAEntero(campos.get("codPaciente")
				.toString());
		int valoracionOdonto = ConstantesBD.codigoNuncaValido;
		int evolucionOdonto = ConstantesBD.codigoNuncaValido;
		String codigoPlantilla = new String("");
		Connection con = UtilidadBD.abrirConexion();

		String consulta = "";

		consulta = "SELECT c.codigo AS codigo, tc.nombre AS nombre "
				+ "FROM historiaclinica.componentes c "
				+ "INNER  JOIN  historiaclinica.tipos_componente tc ON(tc.codigo = c.tipo_componente) "
				+ "WHERE c.tipo_componente = "
				+ ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos
				+ " ";

		logger.info("Cadena SQL componente >" + consulta
				+ " \n COD VALORACION >> "
				+ campos.get("codValoracionOdo").toString());

		if (campos.get("codValoracionOdo") != null
				&& Utilidades.convertirAEntero(campos.get("codValoracionOdo")
						.toString()) > 0) {
			valoracionOdonto = Utilidades.convertirAEntero(campos.get(
					"codValoracionOdo").toString());
			campos.put("codValoracionOdont", valoracionOdonto);
			codigoPlantilla = obtenerCodigoPlantillaValoracionConsultaExtOdonto(
					con, campos)
					+ "";
		} else {
			if (campos.get("codEvolucionOdo") != null
					&& Utilidades.convertirAEntero(campos
							.get("codEvolucionOdo").toString()) > 0) {
				evolucionOdonto = Utilidades.convertirAEntero(campos.get(
						"codEvolucionOdo").toString());
				campos.put("codEvolucionOdonto", evolucionOdonto);
				codigoPlantilla = obtenerCodigoPlantillaValoracionConsultaExtOdonto(
						con, campos)
						+ "";
			}
		}
		PreparedStatementDecorator st = null;
		ResultSetDecorator rs  = null;
		try {
			 st = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			rs = new ResultSetDecorator(st.executeQuery());
			if (rs.next()) {

				componente.setCodigoPK(rs.getInt("codigo") + "");
				componente
						.setCodigoTipo(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos);
				componente
						.setCodigo(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos
								+ "");
				componente.setNombreTipo(rs.getString("nombre"));
				componente.setComponente(true);
				// componente.setMostrarModificacion(true);

				componente = cargarComponente(con, componente, true,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, codPaciente,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, codigoPlantilla,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false,
						valoracionOdonto, evolucionOdonto, false);

			}

			rs.close();

		} catch (SQLException e) {
			logger.info("Consulta Obtener CodigoComponente : " + consulta);
			logger.error("Error en obtenerCodigoComponente : " + e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		}

		UtilidadBD.closeConnection(con);
		return componente;
	}

	/**
	 * Metodo para consultar las secciones y escalas de un componente para poder
	 * ser visualizado como vista previa
	 * 
	 * @param campos
	 * @return
	 */
	public static DtoComponente cargarComponenteGenericoPreview(HashMap campos) {
		String consulta = strObtenerCodigoComponenteXtipoComp;
		Connection con = UtilidadBD.abrirConexion();
		DtoComponente componente = new DtoComponente();
		PreparedStatementDecorator st = null;
		ResultSetDecorator rs  = null;
		try {
			st = new PreparedStatementDecorator(con
					.prepareStatement(consulta));
			st.setInt(1, Utilidades.convertirAEntero(campos.get(
					"tipoComponente").toString()));

			rs = new ResultSetDecorator(st.executeQuery());
			if (rs.next()) {
				componente.setCodigoPK(rs.getInt("codigo") + "");
				componente.setCodigoTipo(Utilidades.convertirAEntero(campos
						.get("tipoComponente").toString()));
				componente.setCodigo(campos.get("tipoComponente").toString());
				componente.setNombreTipo(rs.getString("nombre"));
				componente.setComponente(true);
				componente.setMostrarModificacion(true);

				componente = cargarComponente(con, componente, false,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, "",
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false);

			}

			rs.close();

		} catch (SQLException e) {
			logger.info("Consulta Obtener CodigoComponente : " + consulta);
			logger.error("Error en obtenerCodigoComponente : " + e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
			if(st!=null){
				try{
					st.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
		}
		UtilidadBD.closeConnection(con);
		return componente;
	}

	// ****************** FIN SECCIONES Y VALORES ASOCIADAS A OPCIONES
	// ***********************//
	// ******************************************************************************************//

	// *********************************************************************************************************************************************************
	// ************************************* INICIO JULIO HERNANDEZ
	// *******************************************************************************************
	// ********************************************************************************************************************************************************

	/**
	 * M&eacute;todo implementado para guardar los campos parametrizables en
	 */
	public static double guardarCamposParametrizablesIngresoOdontologia(Connection con, DtoPlantilla plantilla, HashMap campos) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		int resp = ConstantesBD.codigoNuncaValido;
		double result = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator pst = null;

		try {

			// *************CAMPOS************************************************
			int codigoIngreso = Utilidades.convertirAEntero(campos
					.get("codigoIngreso")
					+ "");
			int codigoPaciente = Utilidades.convertirAEntero(campos
					.get("codigoPaciente")
					+ "");
			double consecutivoPlantillaIngreso = Utilidades
					.convertirADouble(campos.get("codigoPlantillaIngreso") + "");
			double valoracionOdonto = Utilidades.convertirADouble(campos
					.get("valoracionOdonto")
					+ "");
			String fecha = campos.get("fecha").toString();
			String hora = campos.get("hora").toString();
			String loginUsuario = campos.get("loginUsuario").toString();

			// ********************************************************************
			logger.info("consecutivoPlantillaIngreso: "
					+ consecutivoPlantillaIngreso);

			if (consecutivoPlantillaIngreso > 0) {
				// ************************SE MODIFICA EL ENCABEZADO DE LA
				// PLANTILLA********************************************
				pst = new PreparedStatementDecorator(
						con, strModificarPlantillaIngresoOdonto);
				pst.setDate(1, Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(fecha)));
				pst.setString(2, hora);
				pst.setString(3, loginUsuario);
				pst.setDouble(4, valoracionOdonto);
				resp = pst.executeUpdate();
				result = consecutivoPlantillaIngreso;
				pst.close();
				// ****************************************************************************************************************
			} else {
				// ***************************SE INSERTA EL ENCABEZADO DE LA
				// PLANTILLA****************************************************
				pst = new PreparedStatementDecorator(
						con.prepareStatement(strInsertarPlantillaIngreso));

				consecutivoPlantillaIngreso = UtilidadBD
						.obtenerSiguienteValorSecuencia(con,
								"seq_plantilas_ingresos");
				// Asigno el consecutivo a retornar

				result = consecutivoPlantillaIngreso;

				logger.info("1) " + consecutivoPlantillaIngreso);
				pst.setDouble(1, Utilidades.convertirADouble(consecutivoPlantillaIngreso + ""));
				logger.info("2) " + codigoIngreso);
				if (codigoIngreso > 0)
					pst.setInt(2, codigoIngreso);
				else
					pst.setNull(2, Types.INTEGER);

				pst.setNull(3, Types.INTEGER);

				logger.info("4) " + codigoPaciente);
				if (codigoPaciente > 0)
					pst.setInt(4, codigoPaciente);
				else
					pst.setNull(4, Types.INTEGER);

				logger.info("5) " + plantilla.getCodigoPK());
				pst.setDouble(5, Utilidades.convertirADouble(plantilla.getCodigoPK()));
				logger.info("6) " + fecha);
				pst.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
				logger.info("7) " + hora);
				pst.setString(7, hora);
				logger.info("8) " + loginUsuario);
				pst.setString(8, loginUsuario);
				logger.info("9) " + valoracionOdonto);
				pst.setDouble(9, valoracionOdonto);

				resp = pst.executeUpdate();
				
			}

			// *******************************************************************************************************
			logger.info("RESULTADO EJECUCION:=> " + resp);
			if (resp > 0) {
				// **********************************REGISTRO DE LOS CAMPOS DE
				// CADA ELEMENTO DE LA SECCION FIJA***************************
				// Se itera cada seccion fija
				for (DtoSeccionFija seccionFija : plantilla.getSeccionesFijas()) {
					// Se itera cada elemento de la seccion fija
					for (DtoElementoParam elemento : seccionFija.getElementos()) {
						// Mientras que todo continue siendo exitoso
						if (resultado.isTrue()) {
							// Si el tipo elemento es SECCION:
							// ---------------------------------------------------------------
							if (elemento.isSeccion()) {
								DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
								// En ï¿½ste mï¿½todo se insertan las
								// plantillas, Adicionalmente tengo que ver
								resultado = insertarCampoPlantillaIngreso(con,
										seccion, consecutivoPlantillaIngreso,
										true);
							}
							// Si el tipo elemento es COMPONENTE
							// -----------------------------------------------------------------
							else if (elemento.isComponente()) {
								DtoComponente componente = (DtoComponente) elemento;

								// Se itera cada seccion del componente
								for (DtoElementoParam elemenComp : componente
										.getElementos()) {
									// Se verifica si el elemento del componente
									// es una seccion
									if (elemenComp.isSeccion()) {
										DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemenComp;
										// SE retistran los campos de una
										// seccion del componente
										resultado = insertarCampoComponenteIngreso(
												con, seccion,
												codigoIngreso,
												ConstantesBD.codigoNuncaValido,
												codigoPaciente,
												fecha,
												hora,
												loginUsuario,
												consecutivoPlantillaIngreso, // consecutivo
																				// tabla
																				// plantillas_ingresos
												componente
														.getConsecutivoParametrizacion(), // consecutivo
																							// tabla
																							// plantillas_componentes
												valoracionOdonto,// Se agrega la
																	// valoracion
																	// odontologica
																	// - Anexo
																	// 870
												true);
									}
									// Se verifica si el elemento del componente
									// es una escala
									else if (elemenComp.isEscala()) {

										DtoEscala escala = (DtoEscala) elemenComp;
										resultado = insertarEscalaIngreso(con,
												escala, codigoIngreso, fecha,
												hora, loginUsuario,
												consecutivoPlantillaIngreso,
												true, true);
									}
								}

							}
							// Si el tipo elemento es ESCALA
							// -----------------------------------------------------------------------
							else if (elemento.isEscala()) {
								DtoEscala escala = (DtoEscala) elemento;
								resultado = insertarEscalaIngreso(con, escala,
										codigoIngreso, fecha, hora,
										loginUsuario,
										consecutivoPlantillaIngreso, false,
										true);
							}
						} // Fin if resultado

					} // fin for elementos seccion fijas
				} // Fin for secciones fijas de la plantilla
				// *********************************************************************************************************
			} else {
				resultado.setResultado(false);
				resultado
						.setDescripcion("Problemas realizando el registro inicial de los valores parametrizables de la plantilla");
			}

		} catch (SQLException e) {
			logger
					.error("Error en guardarCamposParametrizablesIngresoOdontologia: "
							+ e);
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

		if (resultado.isTrue())
			return result;
		else
			return result = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Metodo para guardar la informacion parametrizable de la evolucion.
	 */
	public static int guardarEvolucionOdon(Connection con,
			DtoPlantilla plantillaDto, HashMap campos) {
		int resultado = ConstantesBD.codigoNuncaValido;
		int codigoPkPlantillaEvolucion = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator pst = null;
		try {

			// *************CAMPOS************************************************
			int codigoIngreso = Utilidades.convertirAEntero(campos.get("codigoIngreso")+ "");
			int codigoPkEvolucion = Utilidades.convertirAEntero(campos.get("codigoPkEvolucion")+"");
			codigoPkPlantillaEvolucion = Utilidades.convertirAEntero(campos.get("codigoPkPlantillaEvolucion")+"");
			String fecha = campos.get("fecha")+"";
			String hora = campos.get("hora")+"";
			String loginUsuario = campos.get("usuarioModifica").toString();
			String fechaModifica = campos.get("fechaModifica").toString();
			String horaModifica = campos.get("horaModifica").toString();

			if (codigoPkPlantillaEvolucion > 0) {
				// ******************************MODIFICACION DE LA
				// PLANTILLA**********************************************
				
				
				pst = new PreparedStatementDecorator(con, strModificarPlantillaEvolucion);
				pst.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
				pst.setString(2, hora);
				pst.setString(3, loginUsuario);
				pst.setInt(4,codigoPkEvolucion );
				pst.setInt(5, codigoPkPlantillaEvolucion);
				logger.info("pst "+pst);
				resultado = pst.executeUpdate();

				// **********************************************************************************************************
			} else {
				// ***************************SE INSERTA EL ENCABEZADO DE LA
				// PLANTILLA****************************************************
				
				pst = new PreparedStatementDecorator(con,strInsertarPlantillasEvolucion);

				logger.info("CODIGO DE LA EVOLUCION--->" + codigoPkEvolucion);

				codigoPkPlantillaEvolucion = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_plantillas_evolucion");

				pst.setDouble(1, Utilidades.convertirADouble(codigoPkPlantillaEvolucion + ""));
				// Se inserta vacio porque esta evolucion es de odontologia
				pst.setNull(2, Types.DOUBLE);
				pst.setDouble(3, Utilidades.convertirADouble(plantillaDto.getCodigoPK()));
				pst.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
				pst.setString(5, hora);
				pst.setString(6, loginUsuario);
				pst.setDate(7, Date.valueOf(fechaModifica));
				pst.setString(8, horaModifica);
				// Se agrega este campo para la evolucion odontologia, como
				// puede ser valoracion normal u odontologica, para la evol.
				// normal se pone null el campo
				pst.setDouble(9, Utilidades.convertirADouble(codigoPkEvolucion+""));
				pst.setString(10, loginUsuario);

				logger.info(pst);
				resultado = pst.executeUpdate();

				// ***********************************************************************************************************
			}

			logger.info("resultado "+resultado);
			
			if (resultado > 0)
			{
				// **********************************REGISTRO DE LOS CAMPOS DE
				// CADA ELEMENTO DE LA SECCION FIJA***************************
				// Se itera cada seccion fija
				for (DtoSeccionFija seccionFija : plantillaDto.getSeccionesFijas())
				{
					// Se itera cada elemento de la seccion fija
					for (DtoElementoParam elemento : seccionFija.getElementos())
					{
						// Mientras que todo continue siendo exitoso
						if (codigoPkPlantillaEvolucion>0)
						{
							// Si el tipo elemento es SECCION:
							// ---------------------------------------------------------------
							if (elemento.isSeccion())
							{
								DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;
								resultado = insertarCampoPlantillaEvolucion(con, seccion,codigoPkPlantillaEvolucion, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
								logger.info("resultado "+resultado);
							}
							// Si el tipo elemento es COMPONENTE
							// -----------------------------------------------------------------
							else if (elemento.isComponente())
							{
								DtoComponente componente = (DtoComponente) elemento;

								// Se itera cada seccion del componente
								for (DtoElementoParam elemenComp : componente.getElementos())
								{
									// Se verifica si el elemento del componente
									// es una seccion
									if (elemenComp.isSeccion())
									{
										DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemenComp;
										resultado = insertarCampoComponenteEvolucion(
												con,
												seccion,
												ConstantesBD.codigoNuncaValido, // codigo
																				// evolucion
																				// normal
																				// (no
																				// aplica)
												codigoPkEvolucion,
												codigoPkPlantillaEvolucion,
												Utilidades
														.convertirAEntero(componente
																.getConsecutivoParametrizacion()),
												fecha, hora, loginUsuario, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
										logger.info("resultado "+resultado);
									}
									// Se verifica si el elemento del componente
									// es una escala
									else if (elemenComp.isEscala())
									{
										DtoEscala escala = (DtoEscala) elemenComp;
										resultado = insertarEscalaIngresoEvolucion(
												con, escala, codigoIngreso,
												codigoPkPlantillaEvolucion,
												fecha, hora, loginUsuario,
												true, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
										logger.info("resultado "+resultado);
									}
								}

							}
							// Si el tipo elemento es ESCALA
							// -----------------------------------------------------------------------
							else if (elemento.isEscala())
							{
								DtoEscala escala = (DtoEscala) elemento;
								resultado = insertarEscalaIngresoEvolucion(con,
										escala, codigoIngreso,
										codigoPkPlantillaEvolucion, fecha,
										hora, loginUsuario, false, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
								logger.info("resultado "+resultado);
							}
						} // Fin if resultado

					} // fin for elementos seccion fijas
				} // Fin for secciones fijas de la plantilla
				// *********************************************************************************************************

				// /******************INSERCION DATOS SECCIONES
				// VALORES*****************************************************
				if (resultado>0) {
					for (DtoElementoParam elemento : plantillaDto
							.getSeccionesValor()) {
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable) elemento;

						// Se verifica que la secciï¿½n haya sido activada en el
						// formulario y que todo marche bien
						if (seccion.estaSeccionValorOpcionActiva(plantillaDto
								.getListadoSeccionesValorActivas())
								&& resultado>0) {
							// Se pregunta si la seccion viene de la plantilla o
							// viene del componente
							if (seccion.isVieneDePlantilla())
								resultado = insertarCampoPlantillaEvolucion(
										con, seccion,
										codigoPkPlantillaEvolucion, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
							else
								resultado = insertarCampoComponenteEvolucion(
										con,
										seccion,
										ConstantesBD.codigoNuncaValido,
										codigoPkEvolucion,

										codigoPkPlantillaEvolucion,
										Utilidades
												.convertirAEntero(plantillaDto
														.obtenerConsecutivoPlantillaComponenteSeccionValor(seccion
																.getConsecutivoParametrizacion())),
										fecha, hora, loginUsuario, true).isTrue()?codigoPkPlantillaEvolucion:ConstantesBD.codigoNuncaValido;
						}
					}

				}
				// *********************************************************************************************************

			} else {
				resultado=ConstantesBD.codigoNuncaValido;
			}

		} catch (SQLException e) {
			logger.error("Error en guardarValoresParametrizables: " + e);
			resultado=ConstantesBD.codigoNuncaValido;
			return resultado;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		if(resultado > 0){
			
			return codigoPkPlantillaEvolucion;
			
		}else{
			
			return resultado;
		}
	}

	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoPlantilla> obtenerListadoPlantillasEnArray(
			Connection con, HashMap parametros) {
		String cadena = strConsultarPlantillas;
		ArrayList<DtoPlantilla> listadoPlantillas = new ArrayList<DtoPlantilla>();
		PreparedStatementDecorator psd  = null;
		// Validaciones para Filtros
		ResultSetDecorator rs = null;
		if (parametros.containsKey("codigoPk")
				&& !parametros.get("codigoPk").toString().equals(""))
			cadena += " AND codigo_pk = " + parametros.get("codigoPk");

		if (parametros.containsKey("codigoPlantilla")
				&& !parametros.get("codigoPlantilla").toString().equals(""))
			cadena += " AND codigo_plantilla = "
					+ parametros.get("codigoPlantilla");

		if (parametros.containsKey("funParam")
				&& !parametros.get("funParam").toString().equals(""))
			cadena += " AND fun_param = " + parametros.get("funParam");

		if (parametros.containsKey("mostrarModificacion")
				&& !parametros.get("mostrarModificacion").toString().equals("")
				&& (parametros.get("mostrarModificacion").equals(
						ConstantesBD.acronimoSi) || parametros.get(
						"mostrarModificacion").equals(ConstantesBD.acronimoNo)))
			cadena += " AND mostrar_modificacion = '"
					+ parametros.get("mostrarModificacion").toString() + "' ";

		if (parametros.containsKey("tipoAtencion")
				&& !parametros.get("tipoAtencion").toString().equals(""))
			cadena += " AND tipo_atencion = '" + parametros.get("tipoAtencion")
					+ "'";

		if (parametros.containsKey("especialidad")
				&& !parametros.get("especialidad").toString().trim().equals(""))
			cadena += " AND especialidad = " + parametros.get("especialidad")
					+ "";

		cadena += " ORDER BY codigo_plantilla ASC ";

		logger.info("LA CADENA------>" + cadena);

		try {
			 psd = new PreparedStatementDecorator(
					con, cadena);
			psd.setInt(1, Utilidades.convertirAEntero(parametros
					.get("institucion")
					+ ""));
			 rs = new ResultSetDecorator(psd.executeQuery());
			while (rs.next()) {
				DtoPlantilla dto = new DtoPlantilla();
				dto.setNombre(rs.getString("nombre_plantilla"));
				dto.setCodigoPK(rs.getString("codigo_pk"));
				dto.setTipoAtencion(rs.getString("tipo_atencion"));
				dto.setCodigoFuncionalidad(rs.getInt("fun_param"));

				listadoPlantillas.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, null);		
		}

		return listadoPlantillas;

	}
	
	
	/**
	 * 
	 */
	public static boolean plantillaValoracionEsUsada(String plantilla)
	{
		boolean usada=false;
		String cadena=	"SELECT *FROM  historiaclinica.plantillas_ingresos where plantilla=?";
		PreparedStatementDecorator psd =  null;
		ResultSetDecorator rs = null;
		
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			 psd = new PreparedStatementDecorator(con, cadena);
			psd.setString(1, plantilla);
			 rs = new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
				usada=true;
				
			UtilidadBD.cerrarConexion(con);
			psd.close();
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}finally {		
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, null);
		}
		
		return usada;
	}

	/**
	 * Metodo que obtiene las escalas que son requeridas en una plantilla
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public static ArrayList<DtoEscala> obtenerEscalasRequeridas(Connection con,HashMap campos) {
		ArrayList<DtoEscala> escalasRequeridas= new ArrayList<DtoEscala>();
		String sql = 	"SELECT DISTINCT " +
							"e.codigo_pk AS codigo_pk_escala, " +
							"e.nombre as nombre_escala " +
						"FROM " +
							"historiaclinica.plantillas p " +
						"INNER JOIN " +
							"historiaclinica.plantillas_sec_fijas psf ON (psf.plantilla=p.codigo_pk) " +
						"INNER JOIN " +
							"historiaclinica.plantillas_escalas pe ON (pe.plantilla_sec_fija=psf.codigo_pk) " +
						"INNER JOIN " +
							"escalas e ON (e.codigo_pk=pe.escala) " +
						"WHERE " +
							"p.codigo_pk = ? " +
							"AND pe.requerida='"+ConstantesBD.acronimoSi+"' " +
							"AND pe.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
		logger.info("\n\nSQL obtenerEscalasRequeridas / "+sql);
		logger.info("codigoPlantilla = "+campos.get("codigoPlantilla"));
		PreparedStatementDecorator psd = null;
		ResultSetDecorator rs = null;
		try {
			 psd = new PreparedStatementDecorator(con, sql);
			psd.setInt(1, Utilidades.convertirAEntero(campos.get("codigoPlantilla")+ ""));
			 rs = new ResultSetDecorator(psd.executeQuery());
			while (rs.next()) {
				DtoEscala dto = new DtoEscala();
				dto.setCodigoPK(rs.getString("codigo_pk_escala"));
				dto.setNombre(rs.getString("nombre_escala"));
				escalasRequeridas.add(dto);
			}
			psd.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("ERROR", e);
		}finally {		
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, null);
		}
		return escalasRequeridas;
	}

	/**
	 * Metodo que verifica si se ha guardado información de una escala para una plantilla determinada
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public static boolean existeInfoEscala(Connection con, HashMap campos) {
		boolean existe = false;
		String sql = 	"SELECT " +
							"ei.observaciones as escala_ingreso " +
						"FROM " +
							"manejopaciente.plantillas_pacientes ppac " +
						"INNER JOIN " +
							"manejopaciente.plantillas_escala_pac escpac ON(escpac.plantilla_paciente = ppac.codigo_pk) " +
						"INNER JOIN " +
							"historiaclinica.escalas_ingresos ei ON(ei.codigo_pk = escpac.escala_ingreso) " +
						"WHERE " +
							"ppac.plantilla=? AND ppac.codigo_paciente=? AND ei.escala=?";
		
		logger.info("\n\nSQL existeInfoEscala / "+sql);
		logger.info("codigoPlantilla = "+campos.get("codigoPlantilla"));
		logger.info("codigoPaciente = "+campos.get("codigoPaciente"));
		logger.info("codigoEscala = "+campos.get("codigoEscala"));
		PreparedStatementDecorator psd = null;
		ResultSetDecorator rs = null;
		try {
			psd = new PreparedStatementDecorator(con, sql);
			psd.setInt(1, Utilidades.convertirAEntero(campos.get("codigoPlantilla")+ ""));
			psd.setInt(2, Utilidades.convertirAEntero(campos.get("codigoPaciente")+ ""));
			psd.setInt(3, Utilidades.convertirAEntero(campos.get("codigoEscala")+ ""));
			rs = new ResultSetDecorator(psd.executeQuery());
			if (rs.next())
				existe=true;
			psd.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("ERROR", e);
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}			
			if(psd!=null){
				try{
					psd.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
		
		}
		return existe;
	}

	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static Boolean consultarPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException{
		Boolean existeRegistro=false;
		String consulta = "select * from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN where id_plantilla = ? and formato = ?   ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			
			pst = con.prepareStatement(consulta);
			pst.setInt(1, codigoPkPlantilla);
			pst.setString(2,tipoFormato);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				existeRegistro=true;
			}
			
		} catch (SQLException e) {
			throw e;
		}finally {		
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}				
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
	
		}
		return existeRegistro;
	}
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @param tipoBD
	 * @param sexo
	 * @param centroCosto
	 * @throws SQLException
	 */
	public static void guardarPlantillaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Boolean tipoBD,Integer sexo,Integer centroCosto,String tipoFormato) throws SQLException{
		String sql=" insert into HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN " +
				"(ID, ID_PLANTILLA, NOMBRE_SECCION , MOSTRAR, CENTRO_COSTO, SEXO, FORMATO )" +
				"values " +
				" (?,?,?,?,?,?,?)";

		PreparedStatement pst = null;
		try {
						
			/* Mt 8218 Relacionada a 1528
			 * Se estaba utilizando secuencia.nextval y esto solo aplica a Oracle
			 * Se utiliza utilidad general para obtener la secuencia
			 */
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"HISTORIACLINICA.seq_seccion_fija_sin_orden");					
					
			pst = con.prepareStatement(sql);
			pst.setInt(1, secuencia);
			pst.setInt(2, codigoPlantilla);
			pst.setString(3,nombreSeccion);
			
			if(!tipoBD){
				pst.setBoolean(4, visible);
			}else{
				if(visible){
					pst.setInt(4, 1);
				}else{
					pst.setInt(4, 0);
				}
			}
			if(centroCosto!=null){
				pst.setInt(5, centroCosto);
			}else{
				pst.setInt(5, -1);
			}
			
			if(sexo!=null){
				pst.setInt(6, sexo);
			}else{
				pst.setInt(6, -1);
			}
			pst.setString(7, tipoFormato);
						
			pst.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}

		

	}
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param tipoBD
	 * @throws SQLException
	 */
	public static void actualziarPlantillaFijaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible ,Integer sexo, Integer centroCosto,String tipoFormato) throws SQLException{
		String update="update HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN set mostrar=? where id_plantilla=? and sexo=? and centro_costo=? and formato = ? ";
		PreparedStatement pst = null;
		try {
			
			pst = con.prepareStatement(update);
			
			pst.setBoolean(1, visible);
			
			
			pst.setInt(2,codigoPlantilla);
			pst.setInt(3,sexo);
			pst.setInt(4,centroCosto);
			pst.setString(5, tipoFormato);
			
			pst.execute();
		} catch (SQLException e) {
			throw e;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
		}


	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static Boolean consultarVisibilidadPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException{
		Boolean visible=false;
		String consulta = "select mostrar from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN where id_plantilla = ? and formato  = ? ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			
			 pst = con.prepareStatement(consulta);
			pst.setInt(1, codigoPkPlantilla);
			pst.setString(2, tipoFormato);
			 rs = pst.executeQuery();
			
			if(rs.next()){
				if(rs.getBoolean("mostrar")){
					visible=true;
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}finally {			
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}

		return visible;
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static Boolean consultarVisibilidadPlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException{
		Boolean visible=null;
		String consulta = "select mostrar from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN where id_plantilla = ? and sexo = ? and centro_costo = ? and formato = ? ";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			for (int i = 0; i < 3; i++) {				
				pst = con.prepareStatement(consulta);
				pst.setInt(1, codigoPkPlantilla);
				pst.setInt(2, i);
				pst.setInt(3, 0);
				pst.setString(4, tipoFormato);
				rs = pst.executeQuery();
				
				if(rs.next()){
					if(rs.getBoolean("mostrar")){
						visible=true;
						break;
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);	
		}


		PreparedStatement pst2 = null;
		ResultSet rs2  = null;
		try {
			if(visible==null){
				pst2 = con.prepareStatement(consulta);
				pst2.setInt(1, codigoPkPlantilla);
				pst2.setInt(2, sexo);
				pst2.setInt(3, centroCosto);
				pst2.setString(4, tipoFormato);
				rs2 = pst2.executeQuery();
		
				if(rs2.next()){
					if(rs2.getBoolean("mostrar")){
						visible=true;
		
					}
				}
				
			}
		
			if (visible==null) {
				visible=false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			UtilidadBD.cerrarObjetosPersistencia(pst2, rs2, null);	
		}
	

		return visible;
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static Boolean existePlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException{
		Boolean existe=false;
		String consulta = "select mostrar from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN where id_plantilla = ? and sexo = ? and centro_costo = ? and formato = ? ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(consulta);
			pst.setInt(1, codigoPkPlantilla);
			pst.setInt(2, sexo);
			pst.setInt(3, centroCosto);
			pst.setString(4, tipoFormato);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				
				existe=true;
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
		}finally {			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);		
	
		}
			
		return existe;
	}
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static Boolean existePlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException{
		Boolean existe=false;
		String consulta = "select * from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN_CON_EXT   where id_plantilla = ? and especialidad = ? ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			
			pst = con.prepareStatement(consulta);
			pst.setInt(1, codigoPkPlantilla);
			pst.setInt(2, especialidad);
			
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				existe=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}	
		}

		return existe;
	}
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @param tipoBD
	 * @param especialdiad
	 * @throws SQLException
	 */
	public static void guardarPlantillaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Boolean tipoBD,Integer especialdiad) throws SQLException{
		String sql=" insert into HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN_CON_EXT values " +
						" (HISTORIACLINICA.SEQ_SECCION_FIJA_SO_CON_EXT.nextval,?," +
						" ?,?,?)";
		PreparedStatement pst = null;
	
		
		try {
			
			pst = con.prepareStatement(sql);
			pst.setInt(1, codigoPlantilla);
			pst.setString(2,nombreSeccion);
			
			if(!tipoBD){
				pst.setBoolean(3, visible);
			}else{
				if(visible){
					pst.setInt(3, 1);
				}else{
					pst.setInt(3, 0);
				}
			}
			pst.setInt(4, especialdiad);
			
			
			pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
				
		}
		
		
		
	}
	
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param especialidad
	 * @throws SQLException
	 */
	public static void actualziarPlantillaFijaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible ,Integer especialidad) throws SQLException{
		String update="update HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN_CON_EXT set mostrar=? where id_plantilla=? and especialidad=? ";
		
		PreparedStatement pst = null;
	
		try {
			 pst = con.prepareStatement(update);
			pst.setBoolean(1, visible);
			pst.setInt(2,codigoPlantilla);
			pst.setInt(3,especialidad);
			pst.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBasePlantillasDao "+sqlException.toString() );
				}
			}		
					
		}

	}
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public static Boolean consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException{
		Boolean visible=false;
		String consulta = "select mostrar from HISTORIACLINICA.SECCION_FIJA_SIN_ORDEN_CON_EXT where id_plantilla = ? and especialidad = ? ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = con.prepareStatement(consulta);
			pst.setInt(1, codigoPkPlantilla);
			pst.setInt(2, especialidad);
			
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				if(rs.getBoolean("mostrar")){
					visible=true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				
		}
		return visible;
	}
	
	

}