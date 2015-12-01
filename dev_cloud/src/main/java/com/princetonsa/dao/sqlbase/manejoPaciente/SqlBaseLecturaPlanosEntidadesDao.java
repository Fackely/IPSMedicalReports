/*
 * Enero 2, 2008
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.manejoPaciente.DtoLogLecturaPlanosEntidades;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad Lectura de Planos Pacientes entidades subcontratadas
 */
public class SqlBaseLecturaPlanosEntidadesDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseLecturaPlanosEntidadesDao.class);
	
	/**
	 * Cadena usada para consultar el codigo minsalud de una entidad subcontratada
	 */
	private static final String consultarCodMinSaludEntidadSubcontratadaStr = "SELECT codigo_minsalud FROM entidades_subcontratadas WHERE codigo_pk = ?";
	
	/**
	 * Cadena que inserta un registro del archivo AF
	 */
	private static final String insertarRegistroAFStr = "INSERT INTO lectura_planos_af " +
		"(codigo_prestador," +
		"razon_social," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"numero_factura," +
		"fecha_expedicion," +
		"fecha_inicio," +
		"fecha_final," +
		"codigo_entidad," +
		"nombre_entidad," +
		"numero_contrato," +
		"plan_beneficios," +
		"numero_poliza," +
		"valor_copago," +
		"valor_comision," +
		"valor_descuentos," +
		"valor_neto," +
		"usuario," +
		"centro_atencion," +
		"inconsistencia) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AC
	 */
	private static final String insertarRegistroACStr = "INSERT INTO lectura_planos_ac " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"fecha_consulta," +
		"numero_autorizacion," +
		"codigo_consulta," +
		"finalidad_consulta," +
		"causa_externa," +
		"codigo_diag_ppal," +
		"codigo_diag_rel_1," +
		"codigo_diag_rel_2," +
		"codigo_diag_rel_3," +
		"tipo_diagnostico," +
		"valor_consulta," +
		"valor_cuota," +
		"valor_neto, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * Cadena que inserta un registro del archivo AP
	 */
	private static final String insertarRegistroAPStr = "INSERT INTO lectura_planos_ap " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"fecha_procedimiento," +
		"numero_autorizacion," +
		"codigo_procedimiento," +
		"ambito_realizacion," +
		"finalidad_procedimiento," +
		"personal_atiende," +
		"diag_ppal," +
		"diag_relacionado," +
		"complicacion," +
		"forma_realizacion," +
		"valor_procedimiento, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AH
	 */
	private static final String insertarRegistroAHStr = "INSERT INTO lectura_planos_ah " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"via_ingreso," +
		"fecha_ingreso," +
		"hora_ingreso," +
		"numero_autorizacion," +
		"causa_externa," +
		"diag_ppal_ingreso," +
		"diag_ppal_egreso," +
		"diag_rel_egreso_1," +
		"diag_rel_egreso_2," +
		"diag_rel_egreso_3," +
		"diag_complicacion," +
		"estado_salida," +
		"diag_muerte," +
		"fecha_egreso," +
		"hora_egreso, " +
		"usuario," +
		"centro_atencion) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AU
	 */
	private static final String insertarRegistroAUStr = "INSERT INTO lectura_planos_au " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"fecha_ingreso," +
		"hora_ingreso," +
		"numero_autorizacion," +
		"causa_externa," +
		"diag_salida," +
		"diag_rel_salida_1," +
		"diag_rel_salida_2," +
		"diag_rel_salida_3," +
		"destino_salida," +
		"estado_salida," +
		"causa_muerte," +
		"fecha_salida," +
		"hora_salida, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AM
	 */
	private static final String insertarRegistroAMStr = "INSERT INTO lectura_planos_am " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"numero_autorizacion," +
		"codigo_medicamento," +
		"tipo_medicamento," +
		"nombre_medicamento," +
		"forma_farmaceutica," +
		"concentracion," +
		"unidad_medida," +
		"numero_unidades," +
		"valor_unitario," +
		"valor_total, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AN
	 */
	private static final String insertarRegistroANStr = "INSERT INTO lectura_planos_an " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"fecha_nacimiento," +
		"hora_nacimiento," +
		"edad_gestacional," +
		"control_prenatal," +
		"sexo," +
		"peso," +
		"diagnostico," +
		"causa_muerte," +
		"fecha_muerte," +
		"hora_muerte, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo AT
	 */
	private static final String insertarRegistroATStr = "INSERT INTO lectura_planos_at " +
		"(numero_factura," +
		"codigo_prestador," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"numero_autorizacion," +
		"tipo_servicio," +
		"codigo_servicio," +
		"nombre_servicio," +
		"cantidad," +
		"valor_unitario," +
		"valor_total, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que inserta un registro del archivo US
	 */
	private static final String insertarRegistroUSStr = "INSERT INTO lectura_planos_us " +
		"(tipo_identificacion," +
		"numero_identificacion," +
		"codigo_entidad," +
		"tipo_usuario," +
		"primer_apellido," +
		"segundo_apellido," +
		"primer_nombre," +
		"segundo_nombre," +
		"edad," +
		"unidad_medida," +
		"sexo," +
		"codigo_departamento," +
		"codigo_municipio," +
		"zona_residencia, " +
		"usuario," +
		"centro_atencion) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que verifica si una factura está en la tabla AF
	 */
	private static final String existeFacturaEnAFStr = "SELECT count(1) as cuenta FROM lectura_planos_af WHERE numero_factura = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que elimina una factura de la tabla AF
	 */
	private static final String actualizarFacturaComoInconsistenciaStr = "UPDATE lectura_planos_af SET inconsistencia = '"+ConstantesBD.acronimoSi+"' WHERE numero_factura = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta las facturas del paciente
	 */
	private static final String consultarFacturasPacienteStr = "" +
		"(SELECT numero_factura FROM lectura_planos_ac WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT numero_factura FROM lectura_planos_ap WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT numero_factura FROM lectura_planos_ah WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT numero_factura FROM lectura_planos_au WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT numero_factura FROM lectura_planos_am WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+
		"(SELECT numero_factura FROM lectura_planos_an WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT numero_factura FROM lectura_planos_at WHERE tipo_identificacion = ? AND numero_identificacion = ? AND usuario = ? AND centro_atencion = ?)"; 
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AF
	 */
	private static final String eliminarRegistrosAF_Str = "delete from lectura_planos_af where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AC
	 */
	private static final String eliminarRegistrosAC_Str = "delete from lectura_planos_ac where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo US
	 */
	private static final String eliminarRegistrosUS_Str = "delete from lectura_planos_us where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AP
	 */
	private static final String eliminarRegistrosAP_Str = "delete from lectura_planos_ap where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AN
	 */
	private static final String eliminarRegistrosAN_Str = "delete from lectura_planos_an where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AH
	 */
	private static final String eliminarRegistrosAH_Str = "delete from lectura_planos_ah where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AU
	 */
	private static final String eliminarRegistrosAU_Str = "delete from lectura_planos_au where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AM
	 */
	private static final String eliminarRegistrosAM_Str = "delete from lectura_planos_am where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena implementada para eliminar los registros insertados del archivo AT
	 */
	private static final String eliminarRegistrosAT_Str = "delete from lectura_planos_at where usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta las facturas registradas en la lectura de los planos
	 */
	private static final String cargarListadoFacturas_Str = "SELECT "+ 
		"codigo_prestador, "+
		"razon_social, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"numero_factura, "+
		"fecha_expedicion, "+
		"fecha_inicio, "+
		"fecha_final, "+
		"codigo_entidad, "+
		"nombre_entidad, "+
		"numero_contrato, "+
		"plan_beneficios, "+
		"numero_poliza, "+
		"valor_copago, "+
		"valor_comision, "+
		"valor_descuentos, "+
		"valor_neto "+ 
		"FROM lectura_planos_af WHERE usuario = ? AND centro_atencion = ? and inconsistencia = '"+ConstantesBD.acronimoNo+"'";
	
	/**
	 * Cadena que consulta los pacientes de una factura
	 */
	private static final String consultarPacientesFactura_Str = "" +
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_ac WHERE numero_factura = ? and usuario = ? and centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_ap WHERE numero_factura = ? and usuario = ? and centro_atencion = ?) "+  
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_ah WHERE numero_factura = ? and usuario = ? and centro_atencion = ?) "+  
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_au WHERE numero_factura = ? and usuario = ? and centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_am WHERE numero_factura = ? and usuario = ? and centro_atencion = ?) "+ 
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_an WHERE numero_factura= ? and usuario = ? and centro_atencion = ?) "+  
		"UNION "+ 
		"(SELECT tipo_identificacion,numero_identificacion FROM lectura_planos_at WHERE numero_factura = ? and usuario = ? and centro_atencion = ?)";
	
	/**
	 * Cadena que consulta la informacion del archivo AH de una factura, tipo id y numero id específico
	 */
	private static final String consultarInformacionAH_Str = "SELECT "+ 
		"numero_factura, "+ 
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"via_ingreso, "+
		"fecha_ingreso, "+
		"hora_ingreso, "+
		"numero_autorizacion, "+
		"causa_externa, "+
		"diag_ppal_ingreso, "+
		"diag_ppal_egreso, "+
		"diag_rel_egreso_1, "+
		"diag_rel_egreso_2, "+
		"diag_rel_egreso_3, "+
		"diag_complicacion, "+
		"estado_salida, "+
		"diag_muerte, "+
		"fecha_egreso, "+
		"hora_egreso "+ 
		"FROM lectura_planos_ah "+ 
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta la información del archivo AU de una factura, tipo id y numero id especifico
	 */
	private static final String consultarInformacionAU_Str = "SELECT "+ 
		"numero_factura, "+
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"fecha_ingreso, "+
		"hora_ingreso, "+
		"numero_autorizacion, "+
		"causa_externa, "+
		"diag_salida, "+
		"diag_rel_salida_1, "+
		"diag_rel_salida_2, "+
		"diag_rel_salida_3, "+
		"destino_salida, "+
		"estado_salida, "+
		"causa_muerte, "+
		"fecha_salida, "+
		"hora_salida "+ 
		"FROM lectura_planos_au "+
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta la información del archivo AC
	 */
	private static final String consultarInformacionAC_Str = "SELECT "+ 
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"numero_factura, "+
		"codigo_prestador, "+
		"fecha_consulta, "+
		"to_date(fecha_consulta,'"+ConstantesBD.formatoFechaAp+"') AS fecha_db, "+
		"numero_autorizacion, "+
		"codigo_consulta,  "+
		"finalidad_consulta, "+
		"causa_externa, "+
		"codigo_diag_ppal, "+
		"codigo_diag_rel_1, "+
		"codigo_diag_rel_2, "+
		"codigo_diag_rel_3, "+
		"tipo_diagnostico, "+
		"valor_consulta, "+
		"valor_cuota, "+
		"valor_neto "+ 
		"FROM lectura_planos_ac "+ 
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta la informacion del archivo AP
	 */
	private static final String consultarInformacionAP_Str = "SELECT "+
		"numero_factura, "+
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"fecha_procedimiento, "+
		"to_date(fecha_procedimiento,'"+ConstantesBD.formatoFechaAp+"') AS fecha_db, "+
		"numero_autorizacion, "+
		"codigo_procedimiento, "+
		"ambito_realizacion, "+
		"finalidad_procedimiento, "+
		"personal_atiende, "+
		"diag_ppal, "+
		"diag_relacionado, "+
		"complicacion, "+
		"forma_realizacion, "+
		"valor_procedimiento "+ 
		"FROM lectura_planos_ap "+ 
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta la información del archivo AT
	 */
	private static final String consultarInformacionAT_Str = "SELECT "+ 
		"numero_factura, "+
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"numero_autorizacion, "+
		"tipo_servicio, "+
		"codigo_servicio, "+
		"nombre_servicio, "+
		"cantidad, "+
		"valor_unitario, "+
		"valor_total "+ 
		"FROM lectura_planos_at "+ 
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ? ORDER BY numero_autorizacion";
	
	/**
	 * Cadena que consulta la información del archivo AM
	 */
	private static final String consultarInformacionAM_Str = "SELECT "+ 
		"numero_factura, "+
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"numero_autorizacion, "+
		"codigo_medicamento, "+
		"tipo_medicamento, "+
		"nombre_medicamento, "+
		"forma_farmaceutica, "+
		"concentracion, "+
		"unidad_medida, "+
		"numero_unidades, "+
		"valor_unitario, "+
		"valor_total "+  
		"FROM lectura_planos_am "+ 
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ? ORDER BY numero_autorizacion";
	
	/**
	 * Cadena que consulta la información del archivo US
	 */
	private static final String consultarInformacionUS_Str = "SELECT "+ 
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"codigo_entidad, "+
		"tipo_usuario, "+
		"primer_apellido, "+
		"segundo_apellido, "+
		"primer_nombre, "+
		"segundo_nombre, "+
		"edad, "+
		"unidad_medida, "+
		"sexo, "+
		"codigo_departamento, "+
		"codigo_municipio, "+
		"zona_residencia "+ 
		"FROM lectura_planos_us "+ 
		"WHERE tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?"; 
	
	/**
	 * Cadena para consultar la informacion del archivo AN
	 */
	private static final String consultarInformacionAN_Str = "SELECT "+
		"numero_factura, "+
		"codigo_prestador, "+
		"tipo_identificacion, "+
		"numero_identificacion, "+
		"fecha_nacimiento, "+
		"hora_nacimiento, "+
		"edad_gestacional, "+
		"control_prenatal, "+
		"sexo, "+
		"peso, "+
		"diagnostico, "+
		"causa_muerte, "+
		"fecha_muerte, "+
		"hora_muerte "+ 
		"FROM lectura_planos_an "+
		"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que consulta el consecutivo del encabezado del registro del paciente en la entidad subcontratada
	 */
	private static final String getDatosEncabezadoRegPacEntidadSubcontratada_Str = "SELECT " +
		"pes.consecutivo," +
		"pes.convenio AS codigo_convenio, " +
		"getnombreconvenio(pes.convenio) AS nombre_convenio, " +
		"pes.contrato AS contrato, " +
		"coalesce(pes.autorizacion_ingreso,'') AS numero_autorizacion " +
		"FROM pac_entidades_subcontratadas pes " +
		"INNER JOIN personas p ON(p.codigo=pes.codigo_paciente) " +
		"WHERE pes.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
	
	/**
	 * Cadena para obtener el servicio de un cargo
	 */
	private static final String consultarServicioCargoStr = "SELECT rs.servicio AS codigoServicio FROM referencias_servicio rs WHERE rs.codigo_propietario = ? AND rs.tipo_tarifario = ?";
	
	
	/**
	 * Cadena para obtener las autorzaciones y las cantidades de un servicio
	 */
	private static final String esValidoAutorizacionesServicioStr = "SELECT " +
		"consecutivo," +
		"coalesce(autorizacion,'') as autorizacion," +
		"cantidad " +
		"FROM det_serv_autorizados WHERE consecutivo_pac_entidades_sub = ? AND servicio = ?";
	
	/**
	 * Cadena para actualizar el numero de solicitud en el registro de entidad subcontratada de un servicio
	 */
	private static final String actualizarDetalleRegEntidadSubcontratada_Str = "UPDATE det_serv_autorizados SET solicitud = ? WHERE consecutivo = ?";
	
	/**
	 * Cade para insertar un registro del log de ejecución del proceso
	 */
	private static final String insertarLogEjecucionProceso_Str = "INSERT INTO log_lectura_planos_ent ( "+
		"consecutivo, "+
		"usuario_ejecucion, "+
		"fecha_ejecucion, "+
		"hora_ejecucion, "+
		"codigo_manual, "+
		"entidad_subcontratada, "+
		"numero_factura, "+
		"validar_archivos_carnet, "+
		"ubicacion_planos, "+
		"archivo_inconsistencias, "+
		"institucion, "+
		"centro_atencion, "+
		"directorio_archivos) "+ 
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena usada para consultar la cantidad del servicio AC
	 */
	private static final String consultarCantidadServicioAC_Str = "SELECT count(1) As cantidad from lectura_planos_ac WHERE numero_factura = ? and tipo_identificacion = ? and numero_identificacion = ? and codigo_consulta = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena usada para consultar la cantidad del servicio AP
	 */
	private static final String consultarCantidadServicioAP_Str = "SELECT count(1) as cantidad from lectura_planos_ap WHERE numero_factura = ? and tipo_identificacion = ? and numero_identificacion = ? and codigo_procedimiento = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena usada para consultar la cantidad del servicio AT
	 */
	private static final String consultarCantidadServicioAT_Str = "SELECT count(1) as cantidad from lectura_planos_at WHERE numero_factura = ? and tipo_identificacion = ? and numero_identificacion = ? and codigo_servicio = ? and usuario = ? and centro_atencion = ?";
	
	/**
	 * Cadena que finaliza un registro de paciente entidad subcontratada
	 */
	private static final String finalizarRegistroPacienteEntidadSubcontratada_Str = "UPDATE pac_entidades_subcontratadas SET " +
		"ingreso = ?, " +
		"estado = '"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' , " +
		"usuario_modifica = ?, " +
		"fecha_modifica=current_date, " +
		"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE consecutivo = ?";
	/**
	 * Método que consulta el codigo minsalud de una entidad subcontratada
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String consultarCodMinSaludEntidadSubcontratada(Connection con,String consecutivo)
	{
		String resultados = "";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCodMinSaludEntidadSubcontratadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resultados = rs.getString("codigo_minsalud");
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCodMinSaludEntidadSubcontratada: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para realizar la inserción en el archivo AF
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insercionRegistroArchivo(Connection con,HashMap campos)
	{
		int resp = 0;
		try
		{
			String archivo = campos.get("archivo").toString();
			int numeroCampos = Utilidades.convertirAEntero(campos.get("numeroCampos").toString());
			PreparedStatementDecorator pst = null;
			
			logger.info("voy a insertar registro de archivo "+archivo);
			//***************INSERCIÓN REGISTRO ARCHIVO AF**********************************
			if(archivo.equals(ConstantesBD.ripsAF))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAFStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AC**********************************
			else if(archivo.equals(ConstantesBD.ripsAC))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroACStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AP**********************************
			else if(archivo.equals(ConstantesBD.ripsAP))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAPStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AH**********************************
			else if(archivo.equals(ConstantesBD.ripsAH))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAHStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AU**********************************
			else if(archivo.equals(ConstantesBD.ripsAU))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAUStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AM**********************************
			else if(archivo.equals(ConstantesBD.ripsAM))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAMStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AN**********************************
			else if(archivo.equals(ConstantesBD.ripsAN))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroANStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO AT**********************************
			else if(archivo.equals(ConstantesBD.ripsAT))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroATStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//***************INSERCIÓN REGISTRO ARCHIVO US**********************************
			else if(archivo.equals(ConstantesBD.ripsUS))
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroUSStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//*******************************************************************************
			
			for(int i=1;i<=numeroCampos;i++)
			{
				
				pst.setString(i,campos.get("campo"+i).toString().trim());
			}
			pst.setString(numeroCampos+1, campos.get("usuario")+"");
			pst.setInt(numeroCampos+2, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
			//Si es el archivo AF se ingresa el campo inconsistencia
			if(archivo.equals(ConstantesBD.ripsAF))
				pst.setString(numeroCampos+3, campos.get("inconsistencia")+"");
			
			resp = pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insercionRegistroArchivo: "+e);
		}
		return resp;
	}
	
	/**
	 * Método que verifica si existe factura en la tabla de AF
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean existeFacturaEnAF(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		boolean existe = false;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeFacturaEnAFStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroFactura.trim());
			pst.setString(2,usuario);
			pst.setInt(3,codigoCentroAtencion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;	
		}
		catch(SQLException e)
		{
			logger.error("Error en existeFacturaEnAF: "+e);
		}
		return existe;
	}
	
	/**
	 * Método que actualiza una factura como inconsistencia
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static int actualizarFacturaComoInconsistencia(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		int resp = 0;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarFacturaComoInconsistenciaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroFactura);
			pst.setString(2,usuario);
			pst.setInt(3,codigoCentroAtencion);
			
			resp = pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarFacturaEnAF: "+e);
		}
		return resp;
	}
	
	/**
	 * Método para consultar las facturas de un paciente
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static ArrayList<String> consultarFacturasPaciente(Connection con,String tipoIdentificacion,String numeroIdentificacion,String usuario,int codigoCentroAtencion)
	{
		ArrayList<String> resultados = new ArrayList<String>();
		try
		{
			numeroIdentificacion = numeroIdentificacion.trim();
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFacturasPacienteStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1, tipoIdentificacion);
			pst.setString(2, numeroIdentificacion);
			pst.setString(3, usuario);
			pst.setInt(4, codigoCentroAtencion);
			pst.setString(5, tipoIdentificacion);
			pst.setString(6, numeroIdentificacion);
			pst.setString(7, usuario);
			pst.setInt(8, codigoCentroAtencion);
			pst.setString(9, tipoIdentificacion);
			pst.setString(10, numeroIdentificacion);
			pst.setString(11, usuario);
			pst.setInt(12, codigoCentroAtencion);
			pst.setString(13, tipoIdentificacion);
			pst.setString(14, numeroIdentificacion);
			pst.setString(15, usuario);
			pst.setInt(16, codigoCentroAtencion);
			pst.setString(17, tipoIdentificacion);
			pst.setString(18, numeroIdentificacion);
			pst.setString(19, usuario);
			pst.setInt(20, codigoCentroAtencion);
			pst.setString(21, tipoIdentificacion);
			pst.setString(22, numeroIdentificacion);
			pst.setString(23, usuario);
			pst.setInt(24, codigoCentroAtencion);
			pst.setString(25, tipoIdentificacion);
			pst.setString(26, numeroIdentificacion);
			pst.setString(27, usuario);
			pst.setInt(28, codigoCentroAtencion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
				resultados.add(rs.getString("numero_factura"));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFacturasPaciente: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que elimina los registros Rips cargados en base de datos
	 * @param con
	 */
	public static void eliminarRegistrosRips(Connection con,String usuario,int codigoCentroAtencion)
	{
		try
		{
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AF***************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAF_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AC***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO US***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosUS_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AP***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAP_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AN***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAN_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AH***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAH_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AU***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAU_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AM***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAM_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
			//********SE ELIMINAN REGISTROS DEL ARCHIVO AT***************
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosAT_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			pst.executeUpdate();
			//***********************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarRegistrosRips: "+e);
		}
	}
	
	//***********************************************************************************************************************
	//**************************MÉTODOS PARA EL FLUJO DE REGISTRO DE INFORMACIÓN*********************************************
	//***********************************************************************************************************************
	/**
	 * Método implementado para cargar el listado de facturas
	 */
	public static HashMap<String, Object> cargarListadoFacturas(Connection con,String usuario,int codigoCentroAtencion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarListadoFacturas_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,usuario);
			pst.setInt(2,codigoCentroAtencion);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarListadoFacturas: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que realiza la consulta de los pacientes de una factura
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static HashMap<String, Object> consultarPacientesFactura(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarPacientesFactura_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroFactura);
			pst.setString(2,usuario);
			pst.setInt(3,codigoCentroAtencion);
			pst.setString(4,numeroFactura);
			pst.setString(5,usuario);
			pst.setInt(6,codigoCentroAtencion);
			pst.setString(7,numeroFactura);
			pst.setString(8,usuario);
			pst.setInt(9,codigoCentroAtencion);
			pst.setString(10,numeroFactura);
			pst.setString(11,usuario);
			pst.setInt(12,codigoCentroAtencion);
			pst.setString(13,numeroFactura);
			pst.setString(14,usuario);
			pst.setInt(15,codigoCentroAtencion);
			pst.setString(16,numeroFactura);
			pst.setString(17,usuario);
			pst.setInt(18,codigoCentroAtencion);
			pst.setString(19,numeroFactura);
			pst.setString(20,usuario);
			pst.setInt(21,codigoCentroAtencion);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPacientesFactura: "+e);
		}
		return resultados;
	}
	
	/**
	 * Consultar información AH
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> consultarInformacionArchivo(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros","0");
		try
		{
			String archivo = campos.get("archivo").toString();
			boolean manejarIndices = UtilidadTexto.getBoolean(campos.get("manejarIndices").toString());
			
			if(archivo.equals(ConstantesBD.ripsAH))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionAH_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				/**
				 * lectura_planos_ah "+ 
						"WHERE numero_factura = ? AND tipo_identificacion = ? AND numero_identificacion = ? and usuario = ? and centro_atencion = ?
				 */
				
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if(archivo.equals(ConstantesBD.ripsAU))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionAU_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if(archivo.equals(ConstantesBD.ripsAC))
			{
				String consulta = consultarInformacionAC_Str;
				
				//Si no maneja indices se consulta en orden descendente
				if(!manejarIndices)
					consulta += " ORDER BY fecha_db DESC";
				
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if(archivo.equals(ConstantesBD.ripsAP))
			{
				String consulta = consultarInformacionAP_Str;
				
				//Si no maneja indices se consulta en orden descendente
				if(!manejarIndices)
					consulta += " ORDER BY fecha_db DESC";
				
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if(archivo.equals(ConstantesBD.ripsAT))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionAT_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if(archivo.equals(ConstantesBD.ripsAM))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionAM_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if (archivo.equals(ConstantesBD.ripsUS))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionUS_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				
				pst.setString(1,campos.get("tipoIdentificacion")+"");
				pst.setString(2,campos.get("numeroIdentificacion")+"");
				pst.setString(3,campos.get("usuario")+"");
				pst.setInt(4, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
			else if (archivo.equals(ConstantesBD.ripsAN))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInformacionAN_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,campos.get("numeroFactura")+"");
				pst.setString(2,campos.get("tipoIdentificacion")+"");
				pst.setString(3,campos.get("numeroIdentificacion")+"");
				pst.setString(4,campos.get("usuario")+"");
				pst.setInt(5, Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
				
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), manejarIndices, true);
			}
		}
		catch(SQLException e)
		{
			logger.error("error en consultarInformacionAH: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que consulta el consecutivo del registro del paciente en entidad subcontratada
	 * que concuerda con los datos encontrados en los archivos RIPS de la factura
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> getDatosRegPacEntidadSubcontratada(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			Statement st = null;
			
			String consulta = getDatosEncabezadoRegPacEntidadSubcontratada_Str +
				" and pes.entidad_subcontratada = "+campos.get("consecutivoEntidadSubcontratada")+
				" and pes.centro_atencion = "+campos.get("codigoCentroAtencion");
			
			//Si hay numero de autorizacion se filtra también por numero de autorizacion
			if(!campos.get("numeroAutorizacion").toString().equals(""))
				consulta += " and (pes.autorizacion_ingreso='"+campos.get("numeroAutorizacion")+"' or pes.autorizacion_ingreso IS NULL) ";
			
			//Se verifica si la validacion es con carnet o no
			if(UtilidadTexto.getBoolean(campos.get("validarConCarnet").toString()))
				consulta += " and pes.nro_carnet = '"+campos.get("numeroIdentificacion")+"' ";
			//De lo contrario se hace normal por el tipo y número de identificacion
			else
				consulta += " and p.tipo_identificacion = '"+campos.get("tipoIdentificacion")+"' and p.numero_identificacion = '"+campos.get("numeroIdentificacion")+"'";
			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en getConsecutivoRegPacEntidadSubcontratada: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para consultar el codigo del servicio de un cargo de servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int consultarServicioCargo(Connection con,HashMap campos)
	{
		int codigoServicio = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarServicioCargoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("codigoPropietario").toString());
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("tarifarioOficial").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigoServicio = rs.getInt("codigoServicio");
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarServicioCargo: "+e);
		}
		return codigoServicio;
	}
	
	/**
	 * Método que verifica si es válida la autorizacion del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esValidoAutorizacionesServicio(Connection con,HashMap campos)
	{
		boolean valido = true;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(esValidoAutorizacionesServicioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoEntidadesSubcontratadas")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoServicio").toString()));
			logger.info("?????????????????????consecutivoEntidadesSubcontratadas=> "+campos.get("consecutivoEntidadesSubcontratadas"));
			logger.info("?????????????????????codigoServicio=> "+campos.get("codigoServicio"));
			
			HashMap<String, Object> resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			int numRegistros = Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true);
			if(numRegistros>0)
			{
				logger.info("encontró!!!!!");
				//Como se encontró el servicio inicialmente se pone como no valido
				valido = false;
				
				int cantidad = Utilidades.convertirAEntero(campos.get("cantidad").toString());
				String numeroAutorizacion = campos.get("autorizacion").toString();
				
				//Se iteran los resultados verificando que la cantidad y la autorización sean iguales
				for(int i=0;i<numRegistros;i++)
				{
					//Si la cantidad y la autorizacion corresponde al registro de pacientes entidades subcontratadas es válido
					if(
						Utilidades.convertirAEntero(resultados.get("cantidad_"+i)+"", true)==cantidad&&
						resultados.get("autorizacion_"+i).toString().equals(numeroAutorizacion)
						)
						valido = true;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error esValidoAutorizacionesServicio: "+e);
		}
		return valido;
	}
	
	/**
	 * Método para consultar el artículo del cargo
	 * @param con
	 * @param valor
	 * @param parametro
	 * @return
	 */
	public static int consultarArticuloCargo(Connection con,String valor,String parametro)
	{
		int codigoArticulo = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "SELECT codigo FROM articulo WHERE ";
			
			//Se consulta el articulo por el codigo de axioma
			if(parametro.equals(ConstantesIntegridadDominio.acronimoAxioma))
				consulta += " codigo = "+valor;
			else
				consulta += " codigo_interfaz = '"+valor+"'";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				codigoArticulo = rs.getInt("codigo");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticuloCargo: "+e);
		}
		return codigoArticulo;
	}
	
	/**
	 * Método implementado para actualizar el detalle del registro de la entidad subcontratada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarDetalleRegEntidadSubcontratada(Connection con,HashMap campos)
	{
		int resp = 0;
		try
		{
			logger.info("para actualizar : consecutivoEntidadesSubcontratadas=> "+campos.get("consecutivoEntidadesSubcontratadas"));
			logger.info("para actualizar: codigoServicio=> "+campos.get("codigoServicio"));
			logger.info("para actualizar: numeroAutorizacion=> "+campos.get("numeroAutorizacion"));
			//1) Primero se consulta el consecutivo del detalle de registro de entidad subcontratada
			String consulta = esValidoAutorizacionesServicioStr + " and (autorizacion = '"+campos.get("numeroAutorizacion")+"' or autorizacion IS NULL )";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoEntidadesSubcontratadas")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoServicio").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				//2) Si existe el consecutivo se realiza la actualización del numero de solicitud en el detalle del registro entidad subcontratada
				String consecutivo = rs.getString("consecutivo");
				pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarDetalleRegEntidadSubcontratada_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1, Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
				pst.setInt(2,Utilidades.convertirAEntero(consecutivo));
				resp = pst.executeUpdate();
			}
			else
				resp = 1;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDetalleRegEntidadSubcontratada: "+e);
		}
		return resp;
	}
	
	/**
	 * Método implementado para insertar el log de ejecucion del proceso de lectura de planos
	 * @param con
	 * @param logLectura
	 * @return
	 */
	public static int insertarLogEjecucionProceso(Connection con,DtoLogLecturaPlanosEntidades logLectura)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarLogEjecucionProceso_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_lect_planos_ent");
			
			/**
			 * "consecutivo, "+
		"usuario_ejecucion, "+
		"fecha_ejecucion, "+
		"hora_ejecucion, "+
		"codigo_manual, "+
		"entidad_subcontratada, "+
		"numero_factura, "+
		"validar_archivos_carnet, "+
		"ubicacion_planos, "+
		"archivo_inconsistencias, "+
		"institucion, "+
		"centro_atencion, "+
		"directorio_archivos) "+ 
			 */
			
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
			pst.setString(2,logLectura.getLoginUsuarioEjecucion());
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(logLectura.getFechaEjecucion())));
			pst.setString(4,logLectura.getHoraEjecucion());
			pst.setInt(5,logLectura.getCodigoManual());
			pst.setDouble(6,Utilidades.convertirADouble(logLectura.getCodigoEntidadSubcontratada()));
			if(!logLectura.getNumeroFactura().equals(""))
				pst.setString(7,logLectura.getNumeroFactura());
			else
				pst.setNull(7,Types.VARCHAR);
			if(logLectura.getValidarArchivosCarnet()!=null)
				pst.setString(8,logLectura.getValidarArchivosCarnet().booleanValue()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			else
				pst.setNull(8,Types.VARCHAR);
			pst.setString(9,logLectura.getUbicacionPlanos());
			if(!logLectura.getArchivoInconsistencias().equals(""))
				pst.setString(10,logLectura.getArchivoInconsistencias());
			else
				pst.setNull(10,Types.VARCHAR);
			pst.setInt(11,logLectura.getCodigoInstitucion());
			pst.setInt(12,logLectura.getCodigoCentroAtencion());
			pst.setString(13,logLectura.getDirectorioArchivos());
			
			resp = pst.executeUpdate();
				
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogEjecucionProceso: "+e);
			resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método para consultar la cantidad de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int consultarCantidadServicio(Connection con,HashMap campos)
	{
		int cantidad = 0;
		try
		{
			String archivo = campos.get("archivo").toString();
			
			PreparedStatementDecorator pst = null;
			
			if(archivo.equals(ConstantesBD.ripsAC))
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCantidadServicioAC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			else if(archivo.equals(ConstantesBD.ripsAP))
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCantidadServicioAP_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			else if(archivo.equals(ConstantesBD.ripsAT))
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCantidadServicioAT_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * SELECT count(1) as cantidad 
			 * from lectura_planos_at WHERE 
			 * numero_factura = ? 
			 * and tipo_identificacion = ? 
			 * and numero_identificacion = ? 
			 * and codigo_servicio = ? 
			 * and usuario = ? 
			 * and centro_atencion = ?
			 */
			
			pst.setString(1,campos.get("numeroFactura")+"");
			pst.setString(2,campos.get("tipoIdentificacion")+"");
			pst.setString(3,campos.get("numeroIdentificacion")+"");
			pst.setString(4,campos.get("codigoServicio")+"");
			pst.setString(5,campos.get("usuario")+"");
			pst.setInt(6,Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				cantidad = rs.getInt("cantidad");
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCantidadServicio:_ "+e);
		}
		return cantidad;
	}
	
	/**
	 * Método implementado para finalizar 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean finalizarRegistroPacienteEntidadSubcontratada(Connection con,HashMap campos)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(finalizarRegistroPacienteEntidadSubcontratada_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * ingreso = ?, " +
				"estado = '"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' , " +
				"usuario_modifica = ?, " +
				"fecha_modifica=current_date, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE consecutivo = ?
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoIngreso")+""));
			pst.setString(2,campos.get("usuario")+"");
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			resp = pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en finalizarRegistroPacienteEntidadSubcontratada: "+e);
		}
		if(resp>0)
			return true;
		else
			return false;
	}
	
	
}
