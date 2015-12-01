/*
 * @(#)SqlBaseSolicitudesCxDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.ordenesmedicas.cirugias.SolicitudesCxForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.Articulo;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la estructura de las solicitudes cx
 *
 * @version 1.0, Nov 09 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseSolicitudesCxDao 
{
    /**
    * Objeto para manejar los logs de esta clase
    */
    private static Logger logger = Logger.getLogger(SqlBaseSolicitudesCxDao.class);

    /**
     * inserta la solicitud de cirugia general
     */
    private static String insertarSolicitudCxGeneralStr="INSERT INTO solicitudes_cirugia " +
    	"(numero_solicitud, " +
    	"codigo_peticion, " +
    	"sub_cuenta," +
    	"ind_qx," +
    	"fecha_inicial_cx," +
    	"hora_inicial_cx," +
    	"fecha_final_cx," +
    	"hora_final_cx," +
    	"fecha_ingreso_sala," +
    	"hora_ingreso_sala," +
    	"fecha_salida_sala," +
    	"hora_salida_sala," +
    	"liquidar_anestesia," +
    	"duracion_final_cx) " +
    	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Selecciona el maximo codigo insertado en la tabla articulos_sol_cirugia
     */
    private static String maxCodigoArticulosSolCxStr= "SELECT MAX(codigo) AS codigoMaximo FROM articulos_sol_cirugia";
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) PARAMETRIZADOS para la solicitud de Cx
     */
    private static String insertarDetalleXArticuloStr= "INSERT INTO det_articulo_sol_cx (articulos_sol_cirugia, articulo) VALUES (?,?)";
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) NO parametrizados para la solicitud de Cx
     */
    private static String insertarDetalleXOtrosArticulosStr= "INSERT INTO det_otros_art_sol_cx (articulos_sol_cirugia, nombre) VALUES (?, ?)"; 
    
    /**
     * inserta los otros profesionales pertenecientes a la solicitud de Cx
     */
    private static String insertarOtrosProfesionalesSolCxStr="INSERT INTO otros_prof_sol_cx (numero_solicitud, codigo_profesional, institucion, tipo_profesional, especialidad ) VALUES (?, ?, ?, ?, ?)";

    /**
     * consulta las peticiones que estan sin orden asociada
     */
    private static String cargarPeticionesSinOrdenAsociadaStr="SELECT " +
                                                                                            "pq.codigo AS codigoPeticion " +
                                                                                            "FROM " +
                                                                                            "peticion_qx pq, " +
                                                                                            "peticiones_servicio ps " +
                                                                                            "WHERE " +
                                                                                            "ps.peticion_qx= pq.codigo " +
                                                                                            "AND pq.paciente=? " +
                                                                                            "AND pq.estado_peticion IN ("+ConstantesBD.codigoEstadoPeticionPendiente+","+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+") "+
                                                                                            "AND pq.codigo NOT IN ( " +
                                                                                                                                    "SELECT " +
                                                                                                                                    "CASE WHEN sc.codigo_peticion " +
                                                                                                                                    "IS NULL THEN -1 " +
                                                                                                                                    "ELSE sc.codigo_peticion END " +
                                                                                                                                    "FROM " +
                                                                                                                                    "solicitudes_cirugia sc, " +
                                                                                                                                    "solicitudes s, " +
                                                                                                                                    "cuentas c " +
                                                                                                                                    "WHERE " +
                                                                                                                                    "sc.numero_solicitud=s.numero_solicitud " +
                                                                                                                                    "AND c.id=s.cuenta " +
                                                                                                                                    "AND c.codigo_paciente=?) ";
    
    /**
     * evalua si existe o no una orden de cx en estado medico solicitada y un(os) servicios 
     */
    private static String existeOrdenConEstadoMedicoPendienteYServicioStr=  "SELECT " +
                                                                                                                    "'Num.Solicitud='|| s.consecutivo_ordenes_medicas ||' Servicio: '|| " +
                                                                                                                    "scs.servicio  ||' - '|| rs.descripcion AS descripcionOrden " +
                                                                                                                    "FROM " +
                                                                                                                    "sol_cirugia_por_servicio scs " +
                                                                                                                    "INNER JOIN solicitudes s ON (s.numero_solicitud=scs.numero_solicitud) " +
                                                                                                                    "INNER JOIN cuentas c ON (c.id=s.cuenta) " +
                                                                                                                    "INNER JOIN referencias_servicio rs ON (rs.servicio=scs.servicio) "+
                                                                                                                    "WHERE " +
                                                                                                                    "c.codigo_paciente=? " +
                                                                                                                    "AND s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" " +
                                                                                                                    "AND s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" " +
                                                                                                                    "AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" ";
    
    /**
     * Carga toda la información de los servicios de una solicitud de Cx
     */
    private static String cargarServiciosXSolicitudCxStr=
        "SELECT DISTINCT " +
        			"scps.codigo AS codigo, " +
        			"scps.servicio AS codigoServicio, " +
        			"scps.consecutivo AS numeroServicio, " +
        			"rs.codigo_propietario AS codigoCups, " +
        			//	"s.especialidad ||'-'|| " +
        			//"scps.servicio ||' '|| " +
        			"'(' || rs.codigo_propietario || ')  ' || rs.descripcion ||' - '|| CASE WHEN s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS descripcionServicio, " +
        			"s.especialidad AS codigoEspecialidad, " +
        			"e.nombre AS descripcionEspecialidad, " +
        			"CASE WHEN s.espos  = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS'  ELSE 'NOPOS' END  AS esPos, " +
        			"CASE WHEN scps.tipos_cirugia  IS NULL THEN -1  ELSE scps.tipos_cirugia END AS codigoTipoCirugia, " +
        			"CASE WHEN tc.nombre  IS NULL THEN ''  ELSE tc.nombre END AS nombreTipoCirugia, " +
        			"CASE WHEN tc.acronimo  IS NULL THEN '-1'  ELSE tc.acronimo END AS acronimoTipoCirugia, " +
        //			"CASE WHEN scps.codigo_cirujano  IS NULL THEN -1 ELSE scps.codigo_cirujano END AS codigoCirujano, " +
        //			"CASE WHEN getNombrePersona(scps.codigo_cirujano)  IS NULL THEN ''  ELSE getNombrePersona(scps.codigo_cirujano) END AS nombreCirujano, " +
        //			"CASE WHEN scps.codigo_ayudante  IS NULL THEN -1 ELSE scps.codigo_ayudante END AS codigoAyudante, " +
        //			"CASE WHEN getNombrePersona(scps.codigo_ayudante)  IS NULL THEN ''  ELSE getNombrePersona(scps.codigo_ayudante) END AS nombreAyudante, " +
        			"CASE WHEN scps.esquema_tarifario  IS NULL THEN -1  ELSE  scps.esquema_tarifario END AS codigoEsquemaTarifario, "+
        			"CASE WHEN et.nombre  IS NULL THEN ''  ELSE  et.nombre END AS nombreEsquemaTarifario, "+
        			"CASE WHEN scps.grupo_uvr IS NULL THEN -1 ELSE  scps.grupo_uvr END AS grupoUvr, "+
        			"coalesce(scps.via_cx,'') AS codigoViaCx, "+
        			"coalesce(getintegridaddominio(scps.via_cx),'') AS nombreViaCx, "+
        //			"CASE WHEN scps.numero_autorizacion  IS NULL THEN ''  ELSE scps.numero_autorizacion END AS autorizacion, "+
        			"getValorCirugia2(scps.codigo) AS valor, "+
        			"CASE WHEN scps.finalidad  IS NULL THEN -1  ELSE scps.finalidad END AS codigoFinalidadCx, " +
        			"CASE WHEN fs.nombre IS NULL THEN '' ELSE fs.nombre END AS nombreFinalidadCx, "+
        			"" + ValoresPorDefecto.getValorFalseParaConsultas() +"  AS fueEliminadoServicio, " +
        			"" + ValoresPorDefecto.getValorTrueParaConsultas() + "  AS estaBD," +
        			"CASE WHEN scps.observaciones IS NULL THEN '' ELSE scps.observaciones END AS observaciones, " +
        "coalesce(scps.ind_bilateral,'')                          AS indBilateral, "+
        "coalesce(scps.ind_via_acceso,'')                         AS indViaAcceso, "+
        "coalesce(scps.especialidad,"+ConstantesBD.codigoNuncaValido+") AS codigoEspecialidadInterviene, " +
        "coalesce(getnombreespecialidad(scps.especialidad),'')          AS nombreEspecialidadInterviene, " +
        "coalesce(scps.liquidar_servicio,'')                           AS liquidarServicio, " +
        "gettienejustificacionnoposserv(scps.servicio,scps.numero_solicitud) AS tieneJustificacion, " +
        "getCodigoFormularioCx(scps.codigo) as codigoFormulario, " +
        "scps.cubierto AS cubierto, " +
        "scps.contrato_convenio AS contratoConvenio "+
        "FROM sol_cirugia_por_servicio scps " +
        "INNER JOIN servicios s ON (s.codigo = scps.servicio) " +
        "INNER JOIN referencias_servicio rs ON (rs.servicio=scps.servicio) " +
        "INNER JOIN especialidades e ON (e.codigo=s.especialidad) " +
        "LEFT OUTER JOIN tipos_cirugia_inst tci ON (tci.codigo=scps.tipos_cirugia) " +
        "LEFT OUTER JOIN tipos_cirugia tc ON (tc.acronimo= tci.tipo_cirugia) "+
        "LEFT OUTER JOIN esquemas_tarifarios et ON (et.codigo=scps.esquema_tarifario) " +
    	"LEFT OUTER JOIN finalidades_servicio fs ON (fs.codigo=scps.finalidad) ";
    
    /**
     * Cadena para cargar el codigo de la peticion que esta asociada a una
     * solicitud de cx
     */
    private static String cargarCodPeticionDadoNumSolCxStr= "SELECT " +
                                                                                            "CASE WHEN codigo_peticion IS NULL THEN 0 " +
                                                                                            "ELSE codigo_peticion END AS codigoPeticion  " +
                                                                                            "FROM solicitudes_cirugia WHERE numero_solicitud=?";
    
    /**
     * Cadena para cargar la información de los otros articulos x solicitudes
     */
    private static String cargarOtrosArticulosXSolicitudesCxStr=    "SELECT " +
                                                                                                "doasc.nombre AS descripcionOtrosArticulo, " +
                                                                                                "ascx.cantidad AS cantdesotrosarticulos, " +
                                                                                                "'false' AS fueEliminadoOtrosArticulo " +
                                                                                                "FROM " +
                                                                                                "articulos_sol_cirugia ascx, " +
                                                                                                "det_otros_art_sol_cx doasc " +
                                                                                                "WHERE " +
                                                                                                "ascx.codigo=doasc.articulos_sol_cirugia " +
                                                                                                "AND ascx.numero_solicitud=?";
    
    /**
     * carga los profesionales x solicitud cx
     */
    private static String cargarProfesionalesXSolicitudesCxStr= "SELECT " +
                                                                                            "op.codigo_profesional AS codigoProfesional, " +
                                                                                            "getNombrePersona(op.codigo_profesional) AS nombreProfesional, " +
                                                                                            "op.especialidad AS codigoEspecialidad, " +
                                                                                            "getNombreEspecialidad(op.especialidad) AS nombreEspecialidad, " +
                                                                                            "op.tipo_profesional AS codigoTipoParticipante, " +
                                                                                            "tpq.nombre AS nombreTipoParticipante  " +
                                                                                            "FROM " +
                                                                                            "otros_prof_sol_cx op, " +
                                                                                            "tipos_participantes_inst_qx tp, " +
                                                                                            "tipos_participantes_qx tpq " +
                                                                                            "WHERE " +
                                                                                            "op.tipo_profesional=tp.codigo " +
                                                                                            "AND tpq.codigo=tp.tipo_profesional " +
                                                                                            "AND  op.numero_solicitud=? ";
    
    /**
     * Borra los servicios de una solicitud Cx
     */
    private static String eliminarServiciosXSolicitudCxStr="DELETE FROM sol_cirugia_por_servicio WHERE numero_solicitud=? ";
    
    /**
     * Borra el detalle de los articulos x solicitud cx
     */
    private static String eliminarDetalleArticulosXSolicitudCx=   "DELETE FROM det_articulo_sol_cx " +
                                                                                            "WHERE " +
                                                                                            "articulos_sol_cirugia IN " +
                                                                                            "(SELECT codigo from articulos_sol_cirugia WHERE numero_solicitud=?)";
    
    /**
     * Borra el detalle de los articulos x solicitud cx
     */
    private static String eliminarDetalleOtrosArticulosXSolicitudCx=   "DELETE FROM det_otros_art_sol_cx " +
                                                                                                    "WHERE " +
                                                                                                    "articulos_sol_cirugia IN " +
                                                                                                    "(SELECT codigo from articulos_sol_cirugia WHERE numero_solicitud=?)";
    
    /**
     * Borra la información general de los articulos x solicitud cx
     */
    private static String eliminarArticulosXSolicitudCxTablaGeneralStr="DELETE FROM articulos_sol_cirugia WHERE numero_solicitud = ?";
    
    /**
     * Borra la información de los otros profesionales  x solicitud cx
     */
    private static String eliminarOtrosProfesionalesXSolicitudCxStr="DELETE FROM otros_prof_sol_cx WHERE numero_solicitud=?";
    
    /**
     * Inserta la anulación de una solicitud cx
     */
    private static String insertarAnulacionSolCxStr="INSERT INTO anulacion_sol_cx (numero_solicitud, motivo, usuario_anula, comentarios, fecha, hora) VALUES (?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ";
    
    /**
     * inserta la respuesta de la sol cx para centros de costo externos
     */
    private static String insertarRespuestaSolCxCentroCostoExternoStr="INSERT INTO res_sol_cx (numero_solicitud, fecha_grabacion, hora_grabacion, resultados) VALUES (?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
    
    /**
     * Modifica el detalle de servicio de una solicitud
     */
    // "numero_autorizacion = ?, "+
    private static String modificarServiciosXSolicitudCxStr ="UPDATE sol_cirugia_por_servicio SET "+ 
    	"servicio=?, "+
    	"tipos_cirugia=?, "+
    	"consecutivo=?, "+
    	"esquema_tarifario = ?, "+
    	"grupo_uvr = ?, "+    	
    	"finalidad = ?, "+
    	"observaciones = ?, "+
    	"via_cx = ?, "+
    	"ind_bilateral = ?, "+
    	"ind_via_acceso = ?, "+
    	"especialidad = ?, "+
    	"liquidar_servicio = ? "+ 
    	"WHERE codigo=?";
    
    /**
     * listado de las solicitudes cx pendientes
     */
    private static String listadoSolicitudesCxPendientesStr=    "SELECT " +
                                                                                         "sc.numero_solicitud                                   AS numeroSolicitud, " +
                                                                                         "sc.codigo_peticion                                     AS codigoPeticion, " +
                                                                                         "to_char(pq.fecha_cirugia, 'DD/MM/YYYY')    AS fechaCirugia, " +
                                                                                         "s.consecutivo_ordenes_medicas                 AS consecutivoOrden, " +
                                                                                         "getnombrepersona(s.codigo_medico)           AS nombreMedicoSolicitante, " +
                                                                                         "getnombreespecialidad(s.especialidad_solicitante) AS nombreEspecialidadSolicitante, " +
                                                                                         "case when s.pyp is null or s.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'false' else 'true' end as pyp " +
                                                                                         "FROM solicitudes_cirugia sc " +
                                                                                         "INNER JOIN solicitudes s ON (s.numero_solicitud=sc.numero_solicitud) " +
                                                                                         "INNER JOIN peticion_qx pq ON(pq.codigo=sc.codigo_peticion) " +
                                                                                         "INNER JOIN cuentas c ON (s.cuenta= c.id) " +
                                                                                         "WHERE " +
                                                                                         "s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" " +
                                                                                         "AND c.codigo_paciente=?  " +
                                                                                         "AND sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' "+
                                                                                         "ORDER BY consecutivoOrden DESC" ;
    /**
     * listado general de las solicitudes Cx.
     */
    private static final String listadoGeneralSolicitudesCxStr = "SELECT "+ 
		"s.numero_solicitud AS numero_solicitud, "+
		"s.consecutivo_ordenes_medicas AS orden, "+ 
		"to_char(s.fecha_solicitud,'DD/MM/YYYY') AS fecha_orden, "+ 
		"CASE WHEN sc.fecha_inicial_cx IS NULL THEN " +
		" to_char(p.fecha_cirugia,'DD/MM/YYYY') " +
		"ELSE " +
		"to_char(sc.fecha_inicial_cx,'DD/MM/YYYY') END AS fecha_cirugia, "+
		"getnombrepersona(p.solicitante) AS medico_solicita, "+
		"getestadosolhis(s.estado_historia_clinica) AS estado_medico, "+ 
        "case when s.pyp is null or s.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'false' else 'true' end as pyp " +
		"FROM solicitudes s "+ 
		"INNER JOIN solicitudes_cirugia sc on(sc.numero_solicitud=s.numero_solicitud) "+ 
		"LEFT OUTER JOIN hoja_quirurgica hq on(hq.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN peticion_qx p ON(sc.codigo_peticion=p.codigo) "+
		"where s.cuenta  = ? ORDER BY orden";
    
    /**
     * Cadena que consulta los datos de anulación de la solicitud
     */
    private static final String cargarAnulacionSolCxStr = "SELECT "+ 
		"a.usuario_anula AS usuario, "+
		"a.comentarios AS comentarios, "+
		"to_char(a.fecha,'DD/MM/YYYY') AS fecha, "+
		"a.hora AS hora, "+
		"mm.nombre AS motivo "+ 
		"FROM anulacion_sol_cx a "+ 
		"INNER JOIN motivos_anul_qx_inst m ON(a.motivo=m.codigo) "+ 
		"INNER JOIN motivo_anulacion_pet mm ON(mm.codigo=m.motivos_anulacion) "+ 
		"WHERE a.numero_solicitud = ?";
 
   /**
    * Cadena para cargar el codigo de la solicitud cx dado el numero de la solicitud y el servicio
    * los cuales son unique
    */
    private static final String cargarCodigoSolCxDadoNumeroSolicitudYServicioStr="SELECT codigo FROM sol_cirugia_por_servicio WHERE numero_solicitud=? and servicio=?";
    
    /**
     * carga el detalle cx asociadas factura
     */
    private static final String cargarDetalleCxAsociadasFacturaStr=	"SELECT " +
    																"s.consecutivo_ordenes_medicas AS orden, " +
    																"to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechaOrden, " +
    																//"scx.numero_autorizacion AS numeroAutorizacion, " +
    																"scx.consecutivo AS consecutivoSolCx, " +
    																"ser.especialidad||' - '||ser.codigo as codigoAxiomaServicio, " +
    																"getnombreservicio(scx.servicio,0) as nombreservicio, " +
    																"dfs.valor_total AS valorTotal, " +
    																"scx.esquema_tarifario AS codigoEsquemaTarifario, " +
    																"getnombreesquematarifario(scx.esquema_tarifario) AS nombreEsquemaTarifario, " +
    																"scx.grupo_uvr AS grupoUvr, " +
    																"scx.tipos_cirugia AS codigoTipoCirugia, " +
    																"tc.nombre AS nombreTipoCirugia, " +
    																//"tmc.descripcion AS noMedicos, " +
    																"'' AS noMedicos, " +
    																"getintegridaddominio(via_cx) AS noVias, " +
    																"dfs.codigo AS codigoDetalleFactura " +
    																"FROM " +
    																"det_factura_solicitud dfs " +
    																"INNER JOIN solicitudes s ON (s.numero_solicitud=dfs.solicitud) " +
    																"INNER JOIN sol_cirugia_por_servicio scx ON (scx.numero_solicitud=s.numero_solicitud AND scx.servicio=dfs.servicio) " +
    																"INNER JOIN servicios ser on (scx.servicio=ser.codigo) " +
    																"INNER JOIN tipos_cirugia_inst tci ON (tci.codigo=scx.tipos_cirugia) " +
    																"INNER JOIN tipos_cirugia tc ON (tc.acronimo=tci.tipo_cirugia) " +
    																//"LEFT OUTER JOIN tipo_medico_cx tmc ON (scx.medico_cx=tmc.codigo) " +
    																"WHERE " +
    																"dfs.factura=? AND dfs.servicio=? AND tci.institucion=?";
    
    /**
     * carga el asocio
     */
    private static final String cargarDetalleCxAsociadasFactura2ParteStr=	"SELECT " +
    																			"getnombretipoasocio(adf.tipo_asocio) AS nombreTipoServicio, " +
    																			"(SELECT  administracion.getnombremedico(dh1.medico) FROM det_cx_honorarios dh1  WHERE dh1.cod_sol_cx_servicio in(select sc.codigo from sol_cirugia_por_servicio sc WHERE sc.numero_solicitud=d.solicitud and sc.servicio=d.servicio) and dh1.tipo_asocio = adf.tipo_asocio "+ValoresPorDefecto.getValorLimit1()+" 1 ) AS nombresMedico, " +
    																			"adf.valor_total AS valorTotal " +
    																		"FROM " +
    																			"asocios_det_factura adf " +
    																			"inner join det_factura_solicitud d on(d.codigo=adf.codigo) " +
    																		"WHERE adf.codigo=? ";
    
    /**
     * Cadena para cargar el encabezado de la solicitud de cirugía
     */
    private static final String cargarEncabezadoSolicitudCx_Str = "SELECT "+ 
    	"sc.numero_solicitud, "+ 
    	"sc.codigo_peticion, "+ 
    	"sc.ind_qx, "+ 
    	"coalesce(to_char(sc.fecha_inicial_cx,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_inicial_cx, "+ 
    	"coalesce(to_char(sc.fecha_final_cx,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_final_cx, "+ 
    	"coalesce(sc.hora_inicial_cx,'') As hora_inicial_cx, "+ 
    	"coalesce(sc.hora_final_cx,'') As hora_final_cx, "+
    	"coalesce(to_char(sc.fecha_ingreso_sala,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_ingreso_sala, "+ 
    	"coalesce(to_char(sc.fecha_salida_sala,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_salida_sala, "+ 
    	"coalesce(sc.hora_ingreso_sala,'') As hora_ingreso_sala, "+ 
    	"coalesce(sc.hora_salida_sala,'') As hora_salida_sala," +
    	"coalesce(sc.liquidar_anestesia,'') AS liquidar_anestesia," +
    	"sc.salida_sala_paciente," +
    	"sal.salida_paciente_cc," +
    	"CASE WHEN sal.salida_paciente_cc IS NULL THEN '' ELSE getdescripcionsalidapac(sal.salida_paciente_cc) END AS descripcion_salida_paciente_cc "+
    	"FROM solicitudes_cirugia sc " +
    	"LEFT OUTER JOIN salidas_sala_paciente sal ON (sal.consecutivo = sc.salida_sala_paciente)" +
    	"WHERE sc.numero_solicitud = ?";
    
    /**
     * Cadena para actualizar los datos del encabezado de la orden de cirugía
     */
    private static final String actualizarEncabezadoSolicitudCx_Str = "UPDATE solicitudes_cirugia SET "+ 
    	"fecha_inicial_cx = ?, hora_inicial_cx = ?, fecha_final_cx = ?, hora_final_cx = ?, "+
    	"fecha_ingreso_sala = ?, hora_ingreso_sala = ?, fecha_salida_sala = ?, hora_salida_sala = ?, liquidar_anestesia = ? "+ 
    	"WHERE numero_solicitud = ?";
    

    
     /**
     * Metodo que inserta la solicitud Cx General, en caso de que sea centro costo esterno entonces no se
     * hace relación a ninguna petición, (peticion=null)
     * @param con
     * @param numeroSolicitud
     * @param codigoPeticion
     * @param esCentroCostoSolicitadoExterno
     * @return
     */
    public static boolean  insertarSolicitudCxGeneral1(  Connection con,
                                                        String numeroSolicitud,
                                                        String codigoPeticion,
                                                        boolean esCentroCostoSolicitadoExterrno,
                                                        double subCuenta,
                                                        String indicativoQx,
                                                        String fechaInicialCx,
                                                        String horaInicialCx,
                                                        String fechaFinalCx,
                                                        String horaFinalCx,
                                                        String fechaIngresoSala,
                                                        String horaIngresoSala,
                                                        String fechaSalidaSala,
                                                        String horaSalidaSala,
                                                        String liquidarAnestesia,
                                                        String duracionCx
                                                     )  
    {
    	logger.info("\n entro a insertarSolicitudCxGeneral1 codigo peticion --> "+codigoPeticion);
    	
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarSolicitudCxGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            if(esCentroCostoSolicitadoExterrno)
                ps.setObject(2, null);
            else
                ps.setString(2, codigoPeticion);
            
            if(subCuenta<1)
            	ps.setObject(3, null);
            else
            	ps.setDouble(3, subCuenta);
            
            if(!indicativoQx.equals(""))
            	ps.setObject(4,indicativoQx);  
            
            if(!fechaInicialCx.equals(""))
            	ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicialCx)));
            else
            	ps.setNull(5,Types.DATE);
            
            if(!horaInicialCx.equals(""))
            	ps.setString(6,horaInicialCx);
            else
            	ps.setNull(6,Types.VARCHAR);
            
            if(!fechaFinalCx.equals(""))
            	ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinalCx)));
            else
            	ps.setNull(7,Types.DATE);
            
            if(!horaFinalCx.equals(""))
            	ps.setString(8,horaFinalCx);
            else
            	ps.setNull(8,Types.VARCHAR);
            
            if(!fechaIngresoSala.equals(""))
            	ps.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaIngresoSala)));
            else
            	ps.setNull(9,Types.DATE);
            
            if(!horaIngresoSala.equals(""))
            	ps.setString(10,horaIngresoSala);
            else
            	ps.setNull(10,Types.VARCHAR);
            
            if(!fechaSalidaSala.equals(""))
            	ps.setDate(11,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaSalidaSala)));
            else
            	ps.setNull(11,Types.DATE);
            
            if(!horaSalidaSala.equals(""))
            	ps.setString(12,horaSalidaSala);
            else
            	ps.setNull(12,Types.VARCHAR);
            
            //segun lo hablado y acordado con oscar y nury, por defecto siempre se debe liquidar la anestesia, 19 de mayo de 2011.  PARAMETRO LIQUIDAR ANESTESIA DEBE ESTAR POR DEFECTO = SI  - DCU 532
            
            /**
             * Inc 2056
             * Se revisa el tema con Nury Rincon, aclarando que se debe mostrar por defecto el campo
             * 'Liquidar Anestesia' en SI pero el usuario puede modificar esta información.
             * Diana Ruiz 
             */     
            if(!liquidarAnestesia.equals(""))
            	ps.setString(13,liquidarAnestesia);
            else
            	ps.setNull(13,Types.VARCHAR);  
                        
            if(!duracionCx.equals(""))
            	ps.setString(14,duracionCx);
            else
            	ps.setNull(14,Types.VARCHAR);
            
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(" Error en la inserción de datos de la solicitud Cx General: SqlBaseSolicitudesCxDao "+e );
            return false;
        }
    }
    
    public static Integer secuenciaSolCirugiaPorServicio;
    public static ArrayList<Integer> listaSecuenciaSolCirugiaPorServicio = new ArrayList<Integer>();
    
    /**
     * Inserta las solicitud Cx X servicio, 
     * entonces se insertan como null.
     * @param con
     * @param numeroSolicitud
     * @param codigoServicio
     * @param codigoTipoCirugia
     * @param consecutivo
     * @param codigoEsquemaTarifario
     * @param codigoGrupoUvr
     * @param codigoInstitucion
     * @param autorizacion
     * @param finalidad
     * @param observaciones
     * @param viaCx
     * @param indBilateral
     * @param indViaAcceso
     * @param codigoEspecialidad
     * @param liquidarServicio
     * @return
     */
    public static int  insertarSolicitudCxXServicio(    Connection con,
             String numeroSolicitud,
             String codigoServicio,
             int codigoTipoCirugia,
             int consecutivo,
             int codigoEsquemaTarifario,
             double codigoGrupoUvr,
             int codigoInstitucion,
			 /*String autorizacion,*/
             int finalidad,
             String observaciones,
             String viaCx,
             String indBilateral,
             String indViaAcceso,
             int codigoEspecialidad,
             String liquidarServicio,
             String cubierto,
             Integer codigoContrato              
         )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            
            //"numero_autorizacion," +
            String consulta=  "INSERT INTO sol_cirugia_por_servicio " +
            	"(codigo, " +
            	"numero_solicitud, " +
            	"servicio, " +
            	"tipos_cirugia, " +
            	"consecutivo, " +
            	"esquema_tarifario, " +
            	"grupo_uvr, " +
            	"institucion, " +            	
            	"finalidad, " +
            	"observaciones," +
            	"via_cx," +
            	"ind_bilateral," +
            	"ind_via_acceso," +
            	"especialidad," +
            	"liquidar_servicio," +
            	"cubierto," +
            	"contrato_convenio) " +
            	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            int secuenciaSolCirugiaPorServicio = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_sol_cx_ser");
            SqlBaseSolicitudesCxDao.secuenciaSolCirugiaPorServicio = secuenciaSolCirugiaPorServicio;
            if(listaSecuenciaSolCirugiaPorServicio!=null){
            	listaSecuenciaSolCirugiaPorServicio.add(secuenciaSolCirugiaPorServicio);
            }
            PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
            pst.setInt(1,secuenciaSolCirugiaPorServicio);
            pst.setInt(2,Integer.parseInt(numeroSolicitud));
            pst.setInt(3,Integer.parseInt(codigoServicio));
            if(codigoTipoCirugia>0)
            	pst.setInt(4,codigoTipoCirugia);
            else
            	pst.setNull(4,Types.INTEGER);
            pst.setInt(5,consecutivo);
            if(codigoEsquemaTarifario>0)
            	pst.setInt(6,codigoEsquemaTarifario);
            else
            	pst.setNull(6,Types.INTEGER);
            if(codigoGrupoUvr>0)
            	pst.setDouble(7, codigoGrupoUvr);
            else
            	pst.setNull(7,Types.DOUBLE);
            pst.setInt(8,codigoInstitucion);
            /*
            if(!autorizacion.trim().equals(""))
            	pst.setString(9,autorizacion);
            else
            	pst.setNull(9,Types.VARCHAR);
            */
            if(finalidad>0)
            	pst.setInt(9,finalidad);
            else
            	pst.setNull(9,Types.INTEGER);
            if(!observaciones.trim().equals(""))
            	pst.setString(10,observaciones);
            else
            	pst.setNull(10, Types.VARCHAR);
            if(!viaCx.trim().equals(""))
            	pst.setString(11,viaCx);
            else
            	pst.setNull(11,Types.VARCHAR);
            if(!indBilateral.trim().equals(""))
            	pst.setString(12,indBilateral);
            else
            	pst.setString(12,ConstantesBD.acronimoNo);
            if(!indViaAcceso.trim().equals(""))
            	pst.setString(13,indViaAcceso);
            else
            	pst.setString(13,ConstantesBD.acronimoNo);
            if(codigoEspecialidad>0)
            	pst.setInt(14,codigoEspecialidad);
            else
            	pst.setNull(14,Types.INTEGER);
            if(!liquidarServicio.trim().equals(""))
            	pst.setString(15,liquidarServicio);
            else
                pst.setString(15,ConstantesBD.acronimoSi);
            /*
             * Se debe guardar el contrato con el que se valida la cobertura del servicio y si lo cubre o no                     
             */
            if(cubierto.isEmpty()){
            	pst.setNull(16, Types.VARCHAR);
			}else{
				pst.setString(16, cubierto);
			}
            
			if (codigoContrato == null){
				pst.setNull(17, Types.INTEGER);
			}else {
				pst.setInt(17, codigoContrato);
			}
            
           int resp = pst.executeUpdate();
           
           if(resp>0)
        	   resp = secuenciaSolCirugiaPorServicio;
           
           
           return resp;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos de la solicitud Cx X Servicio: SqlBaseSolicitudesCxDao "+e.toString() );
            return -1;
        }
    }
    
    /**
     * Método que inserta las solicitudes Cx X Articulo general y retorna el codigo Max insertado para
     * que lo utilicemos en el detalle
     * @param con
     * @param numeroSolicitud
     * @param cantidad
     * @param insertarSolicitudCxXArticuloGeneralStr
     * @return
     */
    public static int  insertarSolicitudCxXArticuloGeneral(  Connection con,
                                                                                            String numeroSolicitud,
                                                                                            int cantidad,
                                                                                            String insertarSolicitudCxXArticuloGeneralStr
                                                                                         )  
    {
        int codigoInsertado= ConstantesBD.codigoNuncaValido;
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarSolicitudCxXArticuloGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.setInt(2, cantidad);
            if(ps.executeUpdate()>0)
            {
                PreparedStatementDecorator ps1=  new PreparedStatementDecorator(con.prepareStatement(maxCodigoArticulosSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ResultSetDecorator rs= new ResultSetDecorator(ps1.executeQuery());
                if(rs.next())
                    codigoInsertado= rs.getInt("codigoMaximo");
            }
            return codigoInsertado;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos de la solicitud Cx X Articulo general: SqlBaseSolicitudesCxDao "+e.toString() );
            return ConstantesBD.codigoNuncaValido;
        }
    }
    
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) PARAMETRIZADOS para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param codigoArticulo
     * @return
     */
    public static boolean  insertarDetalleXArticulo(  Connection con,
                                                                            int codigoTableArticulosSolCx,
                                                                            int codigoArticulo
                                                                          )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleXArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoTableArticulosSolCx);
            ps.setInt(2, codigoArticulo);
            if(ps.executeUpdate()>0)
            {
                return true;
            }
            return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos del detalle de los articulos parametrizados: SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) NO parametrizados para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param nombreArticulo
     * @return
     */
    public static boolean  insertarDetalleXOtrosArticulos(  Connection con,
                                                                                      int codigoTableArticulosSolCx,
                                                                                      String nombreArticulo
                                                                                   )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleXOtrosArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoTableArticulosSolCx);
            ps.setString(2, nombreArticulo);
            if(ps.executeUpdate()>0)
            {
                return true;
            }
            return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos del detalle de los otros (NO parametrizados) articulos: SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * metodo que inserta los otros profesionales x sol cx
     * @param con
     * @param numeroSolicitud
     * @param codigoProfesional
     * @param codigoInstitucion
     * @param codigoTipoProfesional
     * @param codigoEspecialidad
     * @return
     */
    public static boolean  insertarOtrosProfesionalesSolCx(   Connection con,
                                                                                          String numeroSolicitud,
                                                                                          int codigoProfesional, 
                                                                                          int codigoInstitucion,
                                                                                          int codigoTipoProfesional,
                                                                                          int codigoEspecialidad
                                                                                       )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarOtrosProfesionalesSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            
            ps.setString(1, numeroSolicitud);
            ps.setInt(2, codigoProfesional);
            ps.setInt(3, codigoInstitucion);
            ps.setInt(4, codigoTipoProfesional);
            ps.setInt(5, codigoEspecialidad);
            
            if(ps.executeUpdate()>0)
            {
                return true;
            }
            return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos del detalle de los otros profesionales sol cx: SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * consulta las peticiones que estan sin orden asociada
     * @param con
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public static Vector cargarPeticionesSinOrdenAsociada(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas)
    {
        try
        {
            String restriccionServicios=" AND ps.servicio IN ( "+codigosServiciosSeparadosPorComas+" )";
            String cadena=cargarPeticionesSinOrdenAsociadaStr+restriccionServicios;
            PreparedStatementDecorator pStatement= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pStatement.setInt(1, codigoPaciente);
            pStatement.setInt(2, codigoPaciente);
            logger.info("-->"+cadena+"-->"+codigoPaciente);
            ResultSetDecorator rs= new ResultSetDecorator(pStatement.executeQuery());
            Vector vector = new Vector();
            int index=0;
            while(rs.next())
            {
                vector.add( index, rs.getInt("codigoPeticion")+"" );
                index++;
            }
            return vector;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la consulta de cargarPeticionesSinOrdenAsociada: SqlBaseSolicitudesCxDao "+e.toString());
            return null;
        }
    }
    
    /**
     * evalua si existe o no una orden de cx en estado medico solicitada y un(os) servicios 
     * @param con
     * @param codigoPaciente
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public static Vector existeOrdenConEstadoMedicoPendienteYServicio(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas)
    {
        try
        {
            String restriccionServicios=" AND scs.servicio IN ( "+codigosServiciosSeparadosPorComas+" )";
            PreparedStatementDecorator pStatement= new PreparedStatementDecorator(con.prepareStatement(existeOrdenConEstadoMedicoPendienteYServicioStr+restriccionServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pStatement.setInt(1, codigoPaciente);
            ResultSetDecorator rs= new ResultSetDecorator(pStatement.executeQuery());
            Vector vector = new Vector();
            int index=0;
            while(rs.next())
            {
                vector.add( index, rs.getString("descripcionOrden")+"" );
                index++;
            }
            return vector;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la consulta de existeOrdenConEstadoMedicoPendienteYServicio: SqlBaseSolicitudesCxDao "+e.toString());
            return null;
        }
    }
        
   /**
    * Carga toda la información de los servicios de una solicitud de Cx
    * @param con
    * @param numeroSolicitud
    * @return
    */
    public static HashMap cargarServiciosXSolicitudCx(Connection con, String numeroSolicitud, boolean liquidados)
    {
    	logger.info("\n\nCARGANDO LOS SERVICIOS");
        HashMap map= new HashMap();
        try
        {
   
            String[] colums={   "codigo",    
	                            "codigoServicio",
	                            "numeroServicio",
	                            "codigoCups",
	                            "descripcionServicio",
	                            "codigoEspecialidad",
	                            "descripcionEspecialidad",
	                            "esPos",
	                            "codigoTipoCirugia",
	                            "nombreTipoCirugia",
	                            "acronimoTipoCirugia",
	                            //"codigoCirujano",
	                            //"nombreCirujano",
	                            //"codigoAyudante",
	                            //"nombreAyudante",
	                            "codigoEsquemaTarifario",
	                            "nombreEsquemaTarifario",
	                            "grupoUvr",
	                            "codigoViaCx",
	                            "nombreViaCx",
	                            //"codigoMedicoCx",
	                            //"nombreMedicoCx",
								//"autorizacion", 
								"valor",
								"codigoFinalidadCx",
								"nombreFinalidadCx",
	                            "fueEliminadoServicio",
	                            "estaBD",
	                            "observaciones",
	                            "indBilateral",
	                            "indViaAcceso",
	                            "codigoEspecialidadInterviene",
	                            "nombreEspecialidadInterviene",
	                            "liquidarServicio",
	                            "tieneJustificacion",
	                            "codigoFormulario",
	                            "cubierto",
	                            "contratoConvenio"
                                   };
            
            
            String consulta=cargarServiciosXSolicitudCxStr;
            consulta+=" WHERE " +
                            "  scps.numero_solicitud = ? "+            
                            " AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" ";
            
            if(liquidados)
            	consulta += " AND scps.liquidar_servicio ='"+ConstantesBD.acronimoSi+"' ";
            
            consulta += " ORDER BY numeroServicio ";
            
            logger.info(".--->"+consulta+"---"+numeroSolicitud);
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1,numeroSolicitud);
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            map.put("numColumnas", colums.length+"");
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en cargarServiciosXSolicitudCx de : SqlBaseSolicitudesCxDao "+e.toString());
            return map;
        }
    }
    
    @SuppressWarnings("unchecked")
	public static void serviciosConInformacionParto(Connection con, SolicitudesCxForm cxForm)
    {
    	PreparedStatement stm=null;
        ResultSet rs=null;
        String consultaServiciosConIfnoParto = 
    			"SELECT "+ 
				"sol_cirugia_por_servicio.codigo, "+
				"informacion_parto.consecutivo "+
				"FROM "+ 
				  "salascirugia.sol_cirugia_por_servicio, "+ 
				  "historiaclinica.informacion_parto "+
				"WHERE "+
				  "sol_cirugia_por_servicio.codigo = informacion_parto.cirugia AND "+
				  "sol_cirugia_por_servicio.codigo = ?";
    	try
    	{
    		int numRegistros = Utilidades.convertirAEntero(cxForm.getServiciosMap("numRegistros").toString());
            for (int i = 0; i < numRegistros; i++)
            {
            	stm = con.prepareStatement(consultaServiciosConIfnoParto);
            	stm.setInt(1, Utilidades.convertirAEntero(cxForm.getServiciosMap("codigo_"+i).toString()));
            	rs = stm.executeQuery();
            	if(rs.next()){
            		cxForm.getServiciosMap().put("tienInfoParto_"+i, ConstantesBD.acronimoSi);
            		cxForm.getServiciosMap().put("codigoInfoParto_"+i, rs.getInt("codigo"));
            		cxForm.getServiciosMap().put("consecutivoInfoParto_"+i, rs.getInt("consecutivo"));
            	}
    		}
		} 
    	catch (Exception e)
    	{
    		logger.error(e);
		}
    	finally
    	{
    		try
        	{
        		rs.close();
                stm.close();
    		}
        	catch (Exception e)
        	{
        		logger.error(e);
    		}
    	}
    }
    
    /**
    * Metodo que carga el codigo de la peticion que esta asociada a una solicitud de cx, teniendo
    * en cuenta que:
    * 
    * Retorna 0= Es porque la solicitud cx se hizo a un centro costo externo entonces no existe peticion asociada
    * Retorna -1= Porque no encontro ese numero_solicitud en las solicitudes Cx
    * 
    * @param con
    * @param numeroSolicitud
    * @return
    * Retorna 0= Es porque la solicitud cx se hizo a un centro costo externo entonces no existe peticion asociada
    * Retorna -1= Porque no encontro ese numero_solicitud en las solicitudes Cx
    */
    public static String cargarCodPeticionDadoNumSolCx(Connection con, String numeroSolicitud)
    {
        try
        {
            PreparedStatementDecorator pStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodPeticionDadoNumSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pStatement.setString(1, numeroSolicitud);
            logger.info("-->"+cargarCodPeticionDadoNumSolCxStr);
            ResultSetDecorator rs= new ResultSetDecorator(pStatement.executeQuery());
            if(rs.next())
                return rs.getString("codigoPeticion");
            else
                return "-1";
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la consulta del cargarCodPeticionDadoNumSolCx: SqlBaseSolicitudesCxDao "+e.toString());
            return null;
        }
    }
    
    /**
     * Metodo que carga los materiales especiales PARAMETRIZADOS de una solicitud cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap cargarArticulosXSolicitudesCx(Connection con, String numeroSolicitud, String consulta)
    {
        HashMap map= new HashMap();
        PreparedStatementDecorator cargarStatement  = null;
        ResultSetDecorator rs = null;
        try
        {
            String[] colums={   "codigoArticulo",
                                        "descripcionArticulo",
                                        "unidadMedidaArticulo",
                                        "cantidadDespachadaArticulo",
                                        "autorizacionArticulo",
                                        "fueEliminadoArticulo",
                                        "tipoPosArticulo"
                                   };
            
            cargarStatement = new PreparedStatementDecorator(con.prepareStatement(consulta+" ORDER BY descripcionArticulo",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1, numeroSolicitud);
            rs = new ResultSetDecorator(cargarStatement.executeQuery());
            map=UtilidadBD.resultSet2HashMap(colums, rs, false, true).getMapa();
            map.put("numColumnas", colums.length+"");
            return map;
        }
        catch(SQLException e) {
            logger.warn(e+" Error en cargarArticulosXSolicitudesCx de : SqlBaseSolicitudesCxDao "+e.toString());
            return map;
        } finally {
        	UtilidadBD.cerrarObjetosPersistencia(cargarStatement, rs, null);
        }
    }
    
    /**
     * Metodo para cargar los otros materiales de una solicitud cx especifica 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static HashMap cargarOtrosArticulosXSolicitudesCx(Connection con, String numeroSolicitud)
    {
        //columnas
        String[] columnas={ "descripcionOtrosArticulo",   "cantdesotrosarticulos", "fueEliminadoOtrosArticulo" };
        
        try
        {
            PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarOtrosArticulosXSolicitudesCxStr+ " ORDER BY descripcionOtrosArticulo",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
            listado.getMapa().put("numColumnas", columnas.length+"");
            return listado.getMapa();
        }
        catch(SQLException e)
        {
            logger.error("Error en cargarOtrosArticulosXSolicitudesCx de SqlBaseSolicitudesCxDao: "+e);
            return null;
        }
    }
    
    /**
     * Metodo para cargar los otros materiales de una solicitud cx especifica, este metodo será utilizado
     * para la consulta  
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static HashMap cargarProfesionalesXSolicitudesCx(Connection con, String numeroSolicitud)
    {
        //columnas
        String[] columnas={    "codigoProfesional",   
                                        "nombreProfesional", 
                                        "codigoEspecialidad",
                                        "nombreEspecialidad",
                                        "codigoTipoParticipante",
                                        "nombreTipoParticipante"
                                    };
        
        try
        {
            PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarProfesionalesXSolicitudesCxStr+ " ORDER BY nombreProfesional",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
            listado.getMapa().put("numColumnas", columnas.length+"");
            return listado.getMapa();
        }
        catch(SQLException e)
        {
            logger.error("Error en cargarProfesionalesXSolicitudesCx de SqlBaseSolicitudesCxDao: "+e);
            return null;
        }
    }
    
    /**
     * Borra los servicios de una solicitud Cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean  eliminarServiciosXSolicitudCx(  Connection con,
                                                                                     String numeroSolicitud   
                                                                                  )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosXSolicitudCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            if(ps.executeUpdate()>0)
            {
                return true;
            }
            return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la eliminacion de los Servicios para el numero de solicitud "+numeroSolicitud+" SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * Borra la información general de los articulos x solicitud cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean  eliminarArticulosXSolicitudCx(  Connection con,
                                                                                    String numeroSolicitud   
                                                                                  )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleOtrosArticulosXSolicitudCx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.executeUpdate();
            ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleArticulosXSolicitudCx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.executeUpdate();
            ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarArticulosXSolicitudCxTablaGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.executeUpdate();
            
            return true;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en eliminarArticulosXSolicitudCxTablaGeneralStr para el numero de solicitud "+numeroSolicitud+" SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * Borra los profesionales  de una solicitud Cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean  eliminarOtrosProfesionalesXSolicitudCx(  Connection con,
                                                                                                     String numeroSolicitud   
                                                                                                  )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarOtrosProfesionalesXSolicitudCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en eliminarOtrosProfesionalesXSolicitudCx para el numero de solicitud "+numeroSolicitud+" SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * metodo que inserta la anulacion de una solicitud cx 
     * @param con
     * @param numeroSolicitud
     * @param codigoMotivoAnulacion
     * @param loginUsuario
     * @param comentariosAnulacion
     * @return
     */
    public static boolean  insertarAnulacionSolCx(  Connection con,
                                                                           String numeroSolicitud,
                                                                           int codigoMotivoAnulacion,
                                                                           String loginUsuario,
                                                                           String comentariosAnulacion
                                                                        )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarAnulacionSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.setInt(2, codigoMotivoAnulacion);
            ps.setString(3, loginUsuario);
            ps.setString(4, comentariosAnulacion);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos de la anulación de la solicitud Cx con el numeroSolicitud="+numeroSolicitud+": SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * Metodo que inserta la respuesta a la solicitud cx a centro de costo externo
     * @param con
     * @param numeroSolicitud
     * @param resultados
     * @return
     */
    public static boolean  insertarRespuestaSolCxCentroCostoExterno(  Connection con,
                                                                                                            String numeroSolicitud,
                                                                                                            String resultados
                                                                                                         )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarRespuestaSolCxCentroCostoExternoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, numeroSolicitud);
            ps.setString(2, resultados);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos de la respuesta solicitud Cx CC Externo: SqlBaseSolicitudesCxDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * Método que actualiza los datos de una cirugía de la orden
     * @param con
     * @param servicio
     * @param codigoTipoCirugia
     * @param numeroServicio
     * @param codigoEsquemaTarifario
     * @param grupoUvr
     * @param autorizacion
     * @param finalidad
     * @param observaciones
     * @param viaCx
     * @param indBilateral
     * @param indViaAcceso
     * @param codigoEspecialidad
     * @param liquidarServicio
     * @param consecutivoSolCx
     * @param estado
     * @return
     */
    public static int modificarServiciosXSolicitudCx(
    		Connection con,int servicio,int codigoTipoCirugia,
    		int numeroServicio,int codigoEsquemaTarifario,double grupoUvr,
    		/*String autorizacion,*/int finalidad,String observaciones,String viaCx,
    		String indBilateral, String indViaAcceso, int codigoEspecialidad,
    		String liquidarServicio,String consecutivoSolCx,String estado)
    		
    {
    	try
		{
    		int resp =0;
    		if(estado.equals(ConstantesBD.inicioTransaccion))
    			UtilidadBD.iniciarTransaccion(con);
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarServiciosXSolicitudCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,servicio);
    		if(codigoTipoCirugia>0)
    			pst.setInt(2,codigoTipoCirugia);
    		else
    			pst.setNull(2,Types.INTEGER);
    		pst.setInt(3,numeroServicio);
    		if(codigoEsquemaTarifario>0)
    			pst.setInt(4,codigoEsquemaTarifario);
    		else
    			pst.setNull(4,Types.INTEGER);
    		if(grupoUvr>0)
    			pst.setDouble(5,grupoUvr);
    		else
    			pst.setNull(5,Types.DOUBLE);
    		/*
    		if(!autorizacion.trim().equals(""))
    			pst.setString(6,autorizacion);
    		else
    			pst.setNull(6,Types.VARCHAR);
    		*/	
    		if(finalidad>0)
    			pst.setInt(6,finalidad);
    		else
    			pst.setNull(6,Types.INTEGER);
    		if(!observaciones.trim().equals(""))
    			pst.setString(7,observaciones);
    		else
    			pst.setNull(7,Types.VARCHAR);
    		if(!viaCx.trim().equals(""))
    			pst.setString(8,viaCx);
    		else
    			pst.setNull(8,Types.VARCHAR);
    		if(!indBilateral.trim().equals(""))
    			pst.setString(9,indBilateral);
    		else
    			pst.setNull(9,Types.VARCHAR);
    		if(!indViaAcceso.trim().equals(""))
    			pst.setString(10,indViaAcceso);
    		else
    			pst.setNull(10,Types.VARCHAR);
    		if(codigoEspecialidad>0)
    			pst.setInt(11,codigoEspecialidad);
    		else
    			pst.setNull(11,Types.INTEGER);
    		if(!liquidarServicio.trim().equals(""))
    			pst.setString(12,liquidarServicio);
    		else
    			pst.setNull(12,Types.VARCHAR);
    		pst.setInt(13,Integer.parseInt(consecutivoSolCx));
    		
    		resp = pst.executeUpdate();
    		
    		if(estado.equals(ConstantesBD.finTransaccion))
    			UtilidadBD.finalizarTransaccion(con);
    		
    		return resp;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en modificarServiciosXSolicitudCx de SqlBaseSolicitudesCxDao: "+e);
    		return -1;
		}
    }
    
    /**
     * carga el listado de las solicitudes cx con estado HC Solicitada para un paciente particular, 
     * dentro de este mapa se encapsula otro mapa que contiene el detalle de los servicios (cx) 
     * pertenecientes a cada numero de solicitud consultado
     * @param con
     * @param codigoPaciente
     * @return
     */
    public static HashMap listadoSolicitudesCxPendientes(Connection con, String codigoPaciente)
    {
    	logger.info("\n entre a listadoSolicitudesCxPendientes ");
    	
        HashMap map= new HashMap();
        try
        {
            String[] colums={   "numeroSolicitud",
                                        "codigoPeticion",
                                        "fechaCirugia",
                                        "consecutivoOrden",
                                        "nombreMedicoSolicitante",
                                        "nombreEspecialidadSolicitante",
                                        "pyp"
                                   };
            
            logger.info("\n cosulta -->"+listadoSolicitudesCxPendientesStr);
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoSolicitudesCxPendientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1, codigoPaciente);
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            for(int i=0; i<Integer.parseInt(map.get("numRegistros").toString()) ; i++)
            {
                String numeroSolicitud= map.get("numeroSolicitud_"+i).toString();
                map.put("detalle_"+i,  cargarServiciosXSolicitudCx(con, numeroSolicitud,false));
            }
            
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en listadoSolicitudesCxPendientes de : SqlBaseSolicitudesCxDao "+e.toString());
            return map;
        }
    }
    
    
    /**
     * Método implementado para cargar el listado general de solicitudes Cx
     * Nota * Se está usando en Historia de Atenciones
     * @param con
     * @param cuenta
     * @return
     */
    public static HashMap listadoGeneralSolicitudesCx(Connection con, int cuenta)
    {
    	//columnas
    	String[] columnas={   
    			"numero_solicitud",
    			"orden",
                "fecha_orden",
                "fecha_cirugia",
                "medico_solicita",
                "estado_medico" ,
                "pyp"
           };
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(listadoGeneralSolicitudesCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,cuenta);
    		
    		HashMap mapa=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()), false, true).getMapa();
    		
    		//se toma el detalle de cada orden de cirugía
    		 for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString()) ; i++)
             {
                 String numeroSolicitud= mapa.get("numero_solicitud_"+i).toString();
                 mapa.put("detalle_"+i,  cargarServiciosXSolicitudCx(con, numeroSolicitud,false));
             }
             
             return mapa;
    		
		}
    	catch(SQLException e)
		{
    		logger.error("Error en listadoGeneralSolicitudesCx de SqlBaseSolicitudesCxDao: "+e);
    		return null;
		}
    }
    
    /**
     * Método que carga los datos de anulación de una solicitud
     * anulada
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static HashMap cargarAnulacionSolCx(Connection con,int numeroSolicitud)
    {
    	//columnas
    	String[] columnas={   
    			"usuario",
    			"comentarios",
                "fecha",
                "hora",
                "motivo"
           };
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarAnulacionSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		
    		return UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()), false, true).getMapa();
		}
    	catch(SQLException e)
		{
    		logger.error("Error en cargarAnulacionSolCx de SqlBaseSolicitudesCxDao: "+e);
    		return null;
		}
    }
    
    /**
     * Metodo para cargar el codigo de la solicitud cx dado el numero de la solicitud y el servicio
     * los cuales son unique
     * @param con
     * @param numeroSolicitud
     * @param codigoServicio
     * @return
     */
    public static String cargarCodigoSolCxDadoNumeroSolicitudYServicio(Connection con, String numeroSolicitud, String codigoServicio)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarCodigoSolCxDadoNumeroSolicitudYServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setString(1,numeroSolicitud);
    		pst.setString(2, codigoServicio);
    		ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    			return rs.getString("codigo");
    		else
    			return "-1";
    	}
    	catch(SQLException e)
		{
    		logger.error("Error en cargarCodigoSolCxDadoNumeroSolicitudYServicio de SqlBaseSolicitudesCxDao: "+e);
    		return "-1";
		}
    }
    
    /**
     * Carga el detalle de un acto quirurgico con sus correspondientes asocios los cuales ya han sido facturados
     * @param con
     * @param codigoFactura
     * @param codigoServicio
     * @param codigoInstitucion
     * @return
     */
    public static HashMap cargarDetalleCxAsociadasFactura(Connection con, String codigoFactura, String codigoServicio, int codigoInstitucion)
    {
    	//columnas
    	String[] columnas={   
    			"orden",
    			"fechaOrden",
                //"numeroAutorizacion",
                "consecutivoSolCx",
                "codigoAxiomaServicio",
                "nombreservicio",
                "valorTotal",
                "codigoEsquemaTarifario",
                "nombreEsquemaTarifario",
                "grupoUvr",
                "codigoTipoCirugia",
                "nombreTipoCirugia",
                "noMedicos",
                "noVias",
                "codigoDetalleFactura"
           };
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDetalleCxAsociadasFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		logger.info("\n***********************************************************************************************");
    		logger.info("cargarDetalleCxAsociadasFacturaStr-->"+cargarDetalleCxAsociadasFacturaStr+" ->codFact="+codigoFactura+" codServ->"+codigoServicio+ " inst->"+codigoInstitucion);
    		
    		pst.setString(1,codigoFactura);
    		pst.setString(2, codigoServicio);
    		pst.setInt(3, codigoInstitucion);
    		
    		
    		HashMap mapa= UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()), false, true).getMapa();
    		
    		//se toma el asocio x cada detalle de factura
   		 	for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString()) ; i++)
            {
                String codigoDetalleFactura= mapa.get("codigoDetalleFactura_"+i).toString();
                mapa.put("detalle_"+i,  cargarDetalleCxAsociadasFactura2Parte(con, codigoDetalleFactura));
            }
   		 	
   		 	logger.info("***********************************************************************************************\n");
   		 	
   		 	return mapa;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en cargarDetalleCxAsociadasFactura de SqlBaseSolicitudesCxDao: "+e);
    		return null;
		}
    }
    
    /**
     * carga el asocio
     * @param con
     * @param codigoDetalleFactura
     * @return
     */
    private static HashMap cargarDetalleCxAsociadasFactura2Parte (Connection con, String codigoDetalleFactura)
    {
    	//columnas
    	String[] columnas={   
    			"nombreTipoServicio",
    			"nombresMedico",
                "valorTotal"
           };
    	try
		{
    		logger.info("\ncargarDetalleCxAsociadasFactura2ParteStr-->"+cargarDetalleCxAsociadasFactura2ParteStr+" ->codDer->"+codigoDetalleFactura+"\n");
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDetalleCxAsociadasFactura2ParteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setString(1,codigoDetalleFactura);
    		return UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()), false, true).getMapa();
    	}
    	catch(SQLException e)
		{
    		logger.error("Error en cargarDetalleCxAsociadasFactura2Parte de SqlBaseSolicitudesCxDao: "+e);
    		return null;
		}
    }
    
    /**
     * 
     * @param con
     * @param subCuenta
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarSubCuentaCx(Connection con, double subCuenta, String numeroSolicitud ) throws BDException
    {
    	PreparedStatement pst=null;
    	boolean resultado=false;
    	
    	try {
    		Log4JManager.info("############## Inicio actualizarSubCuentaCx");
    		String consulta="UPDATE solicitudes_cirugia SET sub_cuenta=? WHERE numero_solicitud= "+numeroSolicitud;
        	
    		pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
    		pst.setLong(1,(long)subCuenta);
    		if(pst.executeUpdate()>0){
    			resultado=true;
    		}
    	}
    	catch(SQLException sqe){
    		Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
    	Log4JManager.info("############## Fin actualizarSubCuentaCx");
    	return resultado;
    }
    
    /**
     * 
     * @param con
     * @param codigoServicio
     * @param numeroSolicitud
     * @return
     */
    public static int obtenerConsecutivoServicioCx(Connection con, int codigoServicio, String numeroSolicitud) throws BDException
    {
    	int consecutivo=ConstantesBD.codigoNuncaValido;
    	PreparedStatement pst=null;
    	ResultSet rs = null;
    	
    	try	{
    		Log4JManager.info("############## Inicio obtenerConsecutivoServicioCx");
    		String consulta="SELECT consecutivo as consecu FROM sol_cirugia_por_servicio WHERE numero_solicitud="+numeroSolicitud+" AND servicio=?";
        	pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
    		pst.setInt(1,codigoServicio);
    		rs= pst.executeQuery();
    		if(rs.next()){
    			consecutivo= rs.getInt("consecu");
    		}
    	}
    	catch(SQLException sqe){
    		Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
    	Log4JManager.info("############## Fin obtenerConsecutivoServicioCx");
    	return consecutivo;
    	
    }
    
    /**
     * 
     * @param con
     * @param codigoServicio
     * @param numeroSolicitud
     * @return
     */
    public static Vector<String> obtenerConsecutivoSolCx(Connection con, String numeroSolicitud)
    {
    	Vector<String> consecutivosSolCx= new Vector<String>();
    	String consulta="SELECT codigo FROM sol_cirugia_por_servicio WHERE numero_solicitud="+numeroSolicitud;
    	
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
    		while(rs.next())
    			consecutivosSolCx.add((rs.getInt("codigo")+""));
    	}
    	catch(SQLException e)
		{
    		e.printStackTrace();
    	}
    	return consecutivosSolCx;
    }
    
    
    
    /**
     * Método que realiza la eliminación de una cirugia
     * @param con
     * @param codigoCirugia
     * @return
     */
    public static int eliminarCirugiaSolicitud(Connection con,String codigoCirugia)
    {
    	try
    	{
    		int resp0 = 0, resp1 = 0;
    		
    		String consulta = "DELETE FROM diag_post_opera_sol_cx WHERE cod_sol_cx_servicio = "+codigoCirugia;
    		Statement st = con.createStatement();
    		resp0 = st.executeUpdate(consulta);
    		
    		consulta = "DELETE FROM sol_cirugia_por_servicio WHERE codigo = "+codigoCirugia;
    		st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
    		
    		resp1 = st.executeUpdate(consulta);
    		
    		if(resp0>0&&resp1>0)
    			return 1;
    		else
    			return 0;
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en eliminarCirugiaSolicitud: "+e);
    		return 0;
    	}
    }
    
    
    /**
     * Método implementado para actualizar el consecutivo de un cirugia
     * @param con
     * @param campos
     * @return
     */
    public static int actualizarConsecutivoCirugia(Connection con,HashMap campos)
    {
    	try
    	{
    		String consulta = "UPDATE sol_cirugia_por_servicio SET consecutivo = "+campos.get("consecutivo")+" WHERE codigo = "+campos.get("codigoCirugia");
    		Statement st = con.createStatement();
    		return st.executeUpdate(consulta);
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en actualizarConsecutivoCirugia: "+e);
    		return 0;
    	}
    }
 
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean existeServicioEnSolicitudCx(Connection con, int numeroSolicitud)
    {
    	try
    	{
    		String consulta = " SELECT codigo FROM sol_cirugia_por_servicio WHERE numero_solicitud= "+numeroSolicitud;
			Statement st = con.createStatement();
    		if( st.executeQuery(consulta).next())
    			return true;
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    		logger.error("Error en actualizarConsecutivoCirugia: "+e);
    	}
    	return false;
    }
    
    /**
     * Método implementado para consultar el encabezado de a solicitud de cirugías
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static HashMap<String,Object> cargarEncabezadoSolicitudCx(Connection con,String numeroSolicitud)
    {
    	HashMap<String, Object> resultados = new HashMap<String, Object>();
    	try
    	{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarEncabezadoSolicitudCx_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		logger.info("\n cargarEncabezadoSolicitudCx_Str->"+cargarEncabezadoSolicitudCx_Str+" numsol->"+numeroSolicitud+" \n");
    		pst.setInt(1,Integer.parseInt(numeroSolicitud));
    		
    		resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
    		
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en cargarEncabezadoSolicitudCx: "+e);
    	}
    	return resultados;
    }
    
    /**
     * Método implementado para realizar la actualización de los datos del encabezado de la solicitud
     * @param con
     * @param campos
     * @return
     */
    public static int actualizarEncabezadoSolicitudCx(Connection con,HashMap campos)
    {
    	int respuesta = 0;
    	try
    	{
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarEncabezadoSolicitudCx_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		if(!campos.get("fechaInicialCx").toString().trim().equals(""))
    			pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicialCx").toString())));
    		else
    			pst.setNull(1,Types.DATE);
    		if(!campos.get("horaInicialCx").toString().trim().equals(""))
    			pst.setString(2,campos.get("horaInicialCx").toString());
    		else
    			pst.setNull(2,Types.VARCHAR);
    		if(!campos.get("fechaFinalCx").toString().trim().equals(""))
    			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinalCx").toString())));
    		else
    			pst.setNull(3,Types.DATE);
    		if(!campos.get("horaFinalCx").toString().trim().equals(""))
    			pst.setString(4,campos.get("horaFinalCx").toString());
    		else
    			pst.setNull(4,Types.VARCHAR);
    		if(!campos.get("fechaIngresoSala").toString().trim().equals(""))
    			pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaIngresoSala").toString())));
    		else
    			pst.setNull(5,Types.DATE);
    		if(!campos.get("horaIngresoSala").toString().trim().equals(""))
    			pst.setString(6,campos.get("horaIngresoSala").toString());
    		else
    			pst.setNull(6,Types.VARCHAR);
    		if(!campos.get("fechaSalidaSala").toString().trim().equals(""))
    			pst.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaSalidaSala").toString())));
    		else
    			pst.setNull(7,Types.DATE);
    		if(!campos.get("horaSalidaSala").toString().trim().equals(""))
    			pst.setString(8,campos.get("horaSalidaSala").toString());
    		else
    			pst.setNull(8,Types.VARCHAR);
    		if(!campos.get("liquidarAnestesia").toString().trim().equals(""))
    			pst.setString(9, campos.get("liquidarAnestesia").toString());
    		else
    			pst.setNull(9,Types.VARCHAR);
    		pst.setInt(10,Integer.parseInt(campos.get("numeroSolicitud").toString()));
    		
    		respuesta = pst.executeUpdate();
    		
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en actualizarEncabezadoSolicitudCx: "+e);
    	}
    	return respuesta;
    }
    
    /**
     * Método para obtener el indicativo de cargo de una solicitud de cirugía
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static String obtenerIndicativoCargoSolicitud(Connection con,String numeroSolicitud)
    {
    	String indQx = "";
    	try
    	{
    		String consulta = "SELECT ind_qx FROM solicitudes_cirugia WHERE numero_solicitud = "+numeroSolicitud;
    		logger.info("consulta --->"+consulta);
    		PreparedStatementDecorator st = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery());
    		if(rs.next())
    			indQx = rs.getString("ind_qx");
    		rs.close();
    		logger.info("resultado -->"+indQx);
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en obtenerIndicativoCargoSolicitud: ",e);
    	}
    	return indQx;
    }
    
    /**
     * Método para actualizar la finalidad del servicio cx
     * @param con
     * @param campos
     * @return
     */
    public static boolean actualizarFinalidadServicioCx(Connection con,HashMap campos)
    {
    	boolean exito = false;
    	try
    	{
    		String consulta = "UPDATE sol_cirugia_por_servicio SET finalidad = "+campos.get("codigoFinalidad")+" WHERE codigo = "+campos.get("codigo");
    		
    		Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
    		if(st.executeUpdate(consulta)>0)
    			exito = true;
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en actualizarFinalidadServicioCx: "+e);
    	}
    	return exito;
    }
    
    /**
     * 
     * Este Método se encarga de consultar los códigos de los servicios 
     * asociados a una solicitud de cirugía
     * 
     * @param Connection con, String numeroSolicitud
     * @return ArrayList<Servicios>
     * @author, Angela Maria Aguirre
     *
     */
    public static ArrayList<DtoServicios> buscarServiciosSolicitudQx(Connection con, 
    		String numeroSolicitud, int codigoTarifario){
    	
    	ArrayList<DtoServicios> listaServicios = new ArrayList<DtoServicios>();
    	
    	String query = "select ser.codigo, ref.descripcion from salascirugia.sol_cirugia_por_servicio sol, " +
    			       "servicios ser, facturacion.referencias_servicio ref, facturacion.tarifarios_oficiales tar " +
    			       "where  sol.servicio = ser.codigo " +
    			       "and ref.servicio = ser.codigo " +
    			       "and ref.tipo_tarifario = tar.codigo " +
    			       "and numero_solicitud = " + numeroSolicitud + " and tar.codigo = " + codigoTarifario;
	    try
        {
	    	PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DtoServicios servicio = new DtoServicios();
				servicio.setCodigoServicio(rs.getInt(1));
				servicio.setDescripcionServicio(rs.getString(2));
				listaServicios.add(servicio);
			}
			
        }catch(SQLException e){
          logger.warn(e + " Error en buscarServiciosSolicitudQx de : SqlBaseSolicitudesCxDao "+e.toString());
        }
    	return listaServicios;
    }
    
    /**
     * 
     * Este Método se encarga de consultar los códigos de los artículos 
     * asociados a una solicitud de cirugía
     * 
     * @param Connection con, String numeroSolicitud
     * @return ArrayList<Servicios>
     * @author, Angela Maria Aguirre
     *
     */
    public static ArrayList<Articulo> buscarArticulosSolicitudQx(Connection con, String numeroSolicitud){
    	
    	ArrayList<Articulo> listaArticulos = new ArrayList<Articulo>();
    	
    	String query = "select det.articulo from salascirugia.articulos_sol_cirugia art, " +
    			       "salascirugia.det_articulo_sol_cx det " +
    			       "where art.codigo =  det.articulos_sol_cirugia " +
    			       "and art.numero_solicitud = " + numeroSolicitud;
	    try
        {
	    	PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setString(1,numeroSolicitud);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Articulo articulo = new Articulo();
				articulo.setCodigo(rs.getInt(1));
				listaArticulos.add(articulo);
			}
			
        }catch(SQLException e){
          logger.warn(e + " Error en buscarArticulosSolicitudQx de : SqlBaseSolicitudesCxDao "+e.toString());
        }
    	return listaArticulos;
    }
    
}