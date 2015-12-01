/*
 * Creado en 2/09/2004
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ram�rez L�pez
 * Princeton S.A.
 */
public class SqlBaseSolicitudMedicamentosDao
{
	/**
	 * Manejador de logs de la Clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSolicitudMedicamentosDao.class);
	
	/**
	 * Statement con el ingreso de la justificaci�n
	 */
	private static String ingresarAtributoStr="INSERT INTO desc_atributos_solicitud(numero_solicitud, articulo, atributo, descripcion) VALUES(?, ?, ?, ?)";
	
	/**
	 * Statement para modificar un atributo de la justificaci�n
	 */
	private static String modificarAtributoStr="UPDATE desc_atributos_solicitud SET descripcion=? WHERE numero_solicitud=? AND articulo=? AND atributo=?";
	
	/**
	 * Statement para borrar un atributo
	 */
	private static String borrarAtributoStr="DELETE from desc_atributos_solicitud WHERE numero_solicitud=? AND articulo=? AND atributo=?";
	
	/**
	 * B�squeda del atributo para modificarlo
	 * o insertarlo en caso de que no exista
	 */
	private static final String busquedaAtributoStr="SELECT count(1) AS numResultados from desc_atributos_solicitud WHERE numero_solicitud=? AND articulo=? AND atributo=?";
	
	/**
	 * Statement para ingresar el detalle de la Solicitud de Medicamentos
	 */
	private static String ingresarDetalleSolicitudStr="INSERT INTO detalle_solicitudes(numero_solicitud, articulo, observaciones, dosis, frecuencia, tipo_frecuencia, via, cantidad, unidosis_articulo, dias_tratamiento, nro_dosis_total) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Statement para modificar el detalle de la Solicitud de Medicamentos
	 */
	private static String modificarDetalleSolicitudStr="UPDATE detalle_solicitudes SET observaciones =?, dosis=?, frecuencia=?, tipo_frecuencia=?, via=?, cantidad=?, unidosis_articulo=?, nro_dosis_total=?, dias_tratamiento=? WHERE numero_solicitud=? AND articulo=?";
	
	/**
	 * Statement para el ingreso de la solicitud de medicamentos
	 */
	private static String ingresarSolicitudMedicamentosStr="INSERT INTO solicitudes_medicamentos(numero_solicitud, observaciones_generales, centro_costo_principal, control_especial,entidad_subcontratada) VALUES (?, ?, ?, ?,?)";

	/**
	 * Statement para el ingreso de la solicitud de medicamentos
	 */
	private static String modificarSolicitudMedicamentosStr="UPDATE solicitudes_medicamentos SET observaciones_generales=? where numero_solicitud=?";

	/**
	 * Consultar solicitud de medicamentos
	 */
	private static String consultarSolicitud="SELECT observaciones_generales AS observacionesGenerales, control_especial AS controlEspecial from solicitudes_medicamentos where numero_solicitud=?";
	
	/**
	 * Consultar las solicitudes de un paciente
	 */
	private static String consultarSolicitudes="SELECT case when s.urgente = " + ValoresPorDefecto.getValorTrueParaConsultas() + " then 'true' else 'false' end as urgente" +
												", s.fecha_solicitud || ' ' || s.hora_solicitud as fechaHoraSolicitud" +
												", s.numero_solicitud as numeroSolicitud" +
												", s.estado_historia_clinica as codigoHistoriaClinica" +
												", s.n_estado_historia_clinica as descripcionHistoriaClinica" +
												", s.codigo_medico as codigoMedico" +
												", administracion.getnombremedico(s.codigo_medico) as nombreMedico" +
												", s.centro_costo_solicitado as codigoCentroCostoSolicitado" +
												", s.n_centro_costo_solicitado as descCentroCostoSol" +
												", s.cuenta as cuenta" +
												", c.codigo_paciente as codigopaciente" +
												", administracion.getnombremedico(c.codigo_paciente) as nombrePaciente" +
												", s.consecutivo_ordenes_medicas AS orden " +
												", essolmedsincant(s.numero_solicitud) AS sinCantidad " +
	
	", CASE WHEN s.pyp IS NULL THEN '' ELSE (CASE WHEN s.pyp = " + ValoresPorDefecto.getValorTrueParaConsultas() + " THEN 'Si' ELSE '' END) END AS esPyP " +
												"	FROM vista_solicitudes s" +
												"		INNER JOIN cuentas c on (c.id=s.cuenta) " +
												"		INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=s.numero_solicitud) " +
												"		WHERE s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+
												"		AND c.id=?" +
												"		AND sm.orden_dieta IS NULL" +
												"			ORDER BY fechaHoraSolicitud";
	
	

	/**
	 * Consultar las solicitudes de medicamentos por area
	 */
	private static String consultarSolicitudesAreaPos="SELECT case when s.urgente = " + ValoresPorDefecto.getValorTrueParaConsultas() + " then 'true' else 'false' end as urgente" +
												", s.fecha_solicitud || ' ' || s.hora_solicitud as fechaHoraSolicitud" +
												", s.numero_solicitud as numeroSolicitud" +
												", s.estado_historia_clinica as codigoHistoriaClinica" +
												", s.n_estado_historia_clinica as descripcionHistoriaClinica" +
												", s.codigo_medico as codigoMedico" +
												", administracion.getnombremedico(s.codigo_medico) as nombreMedico" +
												", s.centro_costo_solicitado as codigoCentroCostoSolicitado" +
												", s.n_centro_costo_solicitado as descCentroCostoSol" +
												", s.cuenta as cuenta" +
												", c.codigo_paciente as codigopaciente" +
												", administracion.getnombremedico(c.codigo_paciente) as nombrePaciente" +
												", s.consecutivo_ordenes_medicas AS orden " +
												", essolmedsincant(s.numero_solicitud) AS sinCantidad " +
												", per.primer_nombre || ' ' ||"+
												"  per.segundo_nombre || ' ' ||"+
												"  per.primer_apellido || ' ' ||"+
												"  per.segundo_apellido AS nombre "+
												", per.codigo AS codigopersona "+
                                                ", CASE WHEN s.pyp IS NULL THEN '' ELSE (CASE WHEN s.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+"  THEN 'Si' ELSE '' END) END AS esPyP " +
												", coalesce(getcamacuenta(c.id, c.via_ingreso), '') as infocama " +
												"	FROM vista_solicitudes s" +
												       "		INNER JOIN cuentas c on (c.id=s.cuenta) " +
												       "		INNER JOIN personas per ON(c.codigo_paciente=per.codigo) " +
												       "		INNER JOIN solicitudes_medicamentos sm on (sm.numero_solicitud=s.numero_solicitud) " +
												       "			WHERE " +
												       "				sm.orden_dieta IS NULL" +
												       "				AND s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos;
	private static String consultarSolicitudesAreaOra="SELECT case when s.urgente = " + ValoresPorDefecto.getValorTrueParaConsultas() + " then 'true' else 'false' end as urgente" +
													  ", s.fecha_solicitud || ' ' || s.hora_solicitud as fechaHoraSolicitud" +
													", s.numero_solicitud as numeroSolicitud" +
													", s.estado_historia_clinica as codigoHistoriaClinica" +
													", s.n_estado_historia_clinica as descripcionHistoriaClinica" +
													", s.codigo_medico as codigoMedico" +
													", administracion.getnombremedico(s.codigo_medico) as nombreMedico" +
													", s.centro_costo_solicitado as codigoCentroCostoSolicitado" +
													", s.n_centro_costo_solicitado as descCentroCostoSol" +
													", s.cuenta as cuenta" +
													", c.codigo_paciente as codigopaciente" +
													", administracion.getnombremedico(c.codigo_paciente) as nombrePaciente" +
													", s.consecutivo_ordenes_medicas AS orden " +
													", essolmedsincant(s.numero_solicitud) AS sinCantidad " +
													", per.primer_nombre || ' ' ||"+
													"  per.segundo_nombre || ' ' ||"+
													"  per.primer_apellido || ' ' ||"+
													"  per.segundo_apellido AS nombre "+
													", per.codigo AS codigopersona "+
												    ", CASE WHEN s.pyp IS NULL THEN '' ELSE (CASE WHEN s.pyp = 1  THEN 'Si' ELSE '' END) END AS esPyP " +
													", coalesce(getcamacuenta(c.id, c.via_ingreso), '') as infocama " +
													"	FROM vista_solicitudes s" +
													       "		INNER JOIN cuentas c on (c.id=s.cuenta) " +
													       "		INNER JOIN personas per ON(c.codigo_paciente=per.codigo) " +
													       "		INNER JOIN solicitudes_medicamentos sm on (sm.numero_solicitud=s.numero_solicitud) " +
													       "			WHERE " +
													       "				sm.orden_dieta IS NULL" +
													       "				AND s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos;
													
	
	/**
	 * Consultar las solicitudes que tienen administracion
	 */
	private static String consultarSolicitudesConAdministracion="SELECT case when s.urgente = " + ValoresPorDefecto.getValorTrueParaConsultas() + " then 'true' else 'false' end as urgente" +
												", s.fecha_solicitud || ' ' || s.hora_solicitud as fechaHoraSolicitud" +
												", s.numero_solicitud as numeroSolicitud" +
												", s.estado_historia_clinica as codigoHistoriaClinica" +
												", s.n_estado_historia_clinica as descripcionHistoriaClinica" +
												", s.codigo_medico as codigoMedico" +
												", administracion.getnombremedico(s.codigo_medico) as nombreMedico" +
												", s.centro_costo_solicitado as codigoCentroCostoSolicitado" +
												", s.n_centro_costo_solicitado as descCentroCostoSol" +
												", s.cuenta as cuenta" +
												", c.codigo_paciente as codigopaciente" +
												", administracion.getnombremedico(c.codigo_paciente) as nombrePaciente" +
												", s.consecutivo_ordenes_medicas AS orden " +
												"from vista_solicitudes s inner join cuentas c on (c.id=s.cuenta) inner join admin_medicamentos am on (s.numero_solicitud=am.numero_solicitud)" +
												"where s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+
												" and c.id=? ORDER BY fechaHoraSolicitud DESC";
	
	/**
	 * Consultar las solicitudes historico 
	 * */	
	private static String consultarSolicitudesHist=
		"SELECT " +
		"case when s.urgente = " + ValoresPorDefecto.getValorTrueParaConsultas() + " then 'true' else 'false' end as urgente," +
		"s.numero_solicitud as numeroSolicitud, " +
		"s.consecutivo_ordenes_medicas AS orden, " +
		"s.fecha_solicitud || ' ' || s.hora_solicitud as fechaHoraSolicitud, " +
		"s.estado_historia_clinica as codigoHistoriaClinica, " +
		"s.n_estado_historia_clinica as descripcionHistoriaClinica," +
		"s.n_centro_costo_solicitante as descccsolicitante," +
		"s.codigo_medico as codigoMedico," +
		"administracion.getnombremedico(s.codigo_medico) || ' ' || getespecialidadesmedico(us.login,', ') as nombreMedico, " +												
		"s.cuenta as cuenta, " +
		"essolmedsincant(s.numero_solicitud) AS sinCantidad " +												
		"FROM vista_solicitudes s " +
		"INNER JOIN cuentas c on (c.id=s.cuenta) " +
		"INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud = s.numero_solicitud AND sm.orden_dieta IS NULL) " +
		"INNER JOIN usuarios us ON (us.codigo_persona = s.codigo_medico ) " ;
	
	/** 
	 * Consultar los medicamentos de la solicitud
	 * */
	private static String consultarMedicamentosHist ="SELECT  " +
													"CASE WHEN sa.numero_solicitud IS NULL THEN '' ELSE 'Suspendido' END AS indicativo," +
													"ds.articulo AS cod_articulo, " +
													"ds.numero_solicitud AS numero_solicitud, " +
													"getdescarticulo(ds.articulo) AS articulo," +
													"ds.dosis," +
													"ds.frecuencia," +
													"ds.tipo_frecuencia," +
													"ds.via," +
													"ds.cantidad," +
                                                    "CASE WHEN ds.dosis IS NOT NULL THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS esMedicamento," +													
                                                    "getespos(ds.articulo) AS es_pos," +
													"getTotalExisArticulosXAlmacen(?,ds.articulo,?) AS existencias," +
													"a.naturaleza AS codigonaturaleza," +
													"ds.unidosis_articulo AS unidosis," +
													"ds.observaciones,  " +
													"a.estado AS estado, "+
													"sol.consecutivo_ordenes_medicas AS conce "+
													"FROM " +
													"detalle_solicitudes ds " +
													"INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud = ?) " +
													"INNER JOIN articulo a ON (a.codigo = ds.articulo) " +
													"INNER JOIN solicitudes sol ON (sol.numero_solicitud = sm.numero_solicitud) "+
													"LEFT OUTER JOIN suspension_articulo sa ON (sa.numero_solicitud = ? AND sa.articulo = ds.articulo) " +												
													"WHERE ds.numero_solicitud = ?  and ds.articulo_principal is null " +
													"ORDER BY articulo ASC " ;		
		
	/**
	 * Statement para consultar los insumos despachados de una solicitud
	 */
	private static String consultarInsumosStr="SELECT a.codigo || ' - ' || a.descripcion AS articulo, getnomUnidadMedida(a.unidad_medida) AS unidadmedida, getDespacho(a.codigo,d.numero_solicitud) AS despachototal from detalle_despachos dd INNER JOIN despacho d ON(d.orden=dd.despacho AND d.numero_solicitud=?) INNER JOIN articulo a ON(a.codigo=dd.articulo AND getesmedicamento(a.codigo)=0) GROUP BY a.codigo, a.descripcion, a.unidad_medida,d.numero_solicitud";
	
	/**
	 * Statement para eliminar los medicamentos (tabla descripcion_medicamentos) en la modificaci�n de la solicitud
	 * La BD est� creada de tal manera que borra en cascada de la tabla "desc_atributos_solicitud",
	 * por lo tanto con una sentencia se eliminan las referencias del art�culo en las 2 tablas
	 */
	private static String eliminarArticulosStr="DELETE FROM detalle_solicitudes where numero_solicitud=? and articulo=?";
	
	/**
	 * Statement para ingresar la suspensi�n de un medicamento medicamento
	 */
	private static String suspenderMedicamentoStr="INSERT INTO suspension_articulo (numero_solicitud, articulo, motivo, fecha, hora, codigo_medico) VALUES (?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)"; 

	/**
	 * Statement para ingresar la suspensi�n de un medicamento medicamento
	 */
	private static String modificarMotivoStr="UPDATE suspension_articulo SET motivo=? WHERE numero_solicitud=? AND articulo=?"; 

	/**
	 * Statement para consultar si existe o no un medicamento para la solicitud dada
	 */
	private static String existeMedicamentoStr="SELECT articulo from detalle_solicitudes WHERE numero_solicitud=? AND articulo=?"; 

	/**
	 * almacena la consulta de los insumos que se cargaron
	 * a la solicitud por parte de la farmacia
	 */
	private static String consultaInsumosFarmaciaStr = " SELECT a.codigo || ' - ' || a.descripcion AS articulo, " +
			"getnomUnidadMedida(a.unidad_medida) AS unidadmedida, " +
			"getDespacho(a.codigo,d.numero_solicitud) AS despachototal " +
			"from detalle_despachos dd " +
			"INNER JOIN despacho d ON(d.orden=dd.despacho AND d.numero_solicitud = ?) " +
			"INNER JOIN articulo a ON(a.codigo=dd.articulo AND getesmedicamento(a.codigo) like '0') " +
			"where dd.art_principal not in (select ds.articulo from detalle_solicitudes ds where ds.numero_solicitud=?) "+
			"GROUP BY a.codigo, a.descripcion, a.unidad_medida,d.numero_solicitud ";
	
	/**
	 * String para actualizar las solicitudes de medicamentos (Cantidad solicitada)
	 * Esta sentencia se utiliza desde el despacho de medicamentos
	 */
	private static String actualizarCantidadSolicitadaStr="UPDATE detalle_solicitudes SET cantidad =? WHERE articulo=? AND numero_solicitud=?"; 
	
	/**
	 * Sentencia para listar el detalle de todas los medicamentos de las solicitudes del paciente
	 */
	private static String consultarListadoMedicamentosPacienteStr=	"	(" +
																			"SELECT  " +
																			"ds.articulo AS cod_articulo, " +
																			"ds.numero_solicitud AS numero_solicitud, " +
																			"getdescripcionarticulo(ds.articulo) AS articulo " +
																			"FROM " +
																			"detalle_solicitudes ds " +
																			"INNER JOIN articulo a ON(a.codigo=ds.articulo) " +
																			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=ds.numero_solicitud) " +
																			"WHERE sol.cuenta=?" +
																		") " +
																		"UNION " +
																		"(" +
																			"SELECT " +
																			"dd.articulo AS cod_articulo, " +
																			"d.numero_solicitud AS numero_solicitud, " +
																			"getdescripcionarticulo(dd.articulo) as articulo " +
																			"FROM " +
																			"detalle_despachos dd " +
																			"INNER JOIN despacho d ON(dd.despacho=d.orden) " +
																			"INNER JOIN detalle_solicitudes ds ON(dd.art_principal=ds.articulo AND dd.articulo<>dd.art_principal AND ds.numero_solicitud=d.numero_solicitud) " +
																			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=d.numero_solicitud) WHERE sol.cuenta=?" +
																		") " +
																		"ORDER BY articulo ";
	
	
	private static String consultarConveniosPlanEspecialPacienteStr = "SELECT count(con.centro_costo_plan_especial) as planEspecialConvenio " +
																			"from convenios con INNER JOIN sub_cuentas scu " +
																			"ON(scu.convenio=con.codigo) " +
																			"where scu.ingreso=? AND " +
																			"con.centro_costo_plan_especial > 0 ";
	
	private static final String obtenerEntidadesSubcontratadasCentroCostoStr = "SELECT " +
																"es.codigo_pk as consecutivo," +
																"es.codigo," +
																"es.razon_social " +
																"from centros_costo_entidades_sub cces " +
																"inner join entidades_subcontratadas es ON(es.codigo_pk = cces.entidad_subcontratada and es.activo = '"+ConstantesBD.acronimoSi+"') " +
																"WHERE cces.centro_costo = ? and cces.institucion = ? " +
																"order by cces.nro_prioridad";
	
	
	/**
	 * M�todo para ingresar la justificaci�n del medicamento
	 * este m�todo solo inserta un atributo, as� que se llama
	 * cada vez que se quiere insertar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo Par�metro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacci�n
	 * @return int mayor que 0 si se insert� correctamente el atributo espec�fico.
	 */
	/*
	public static int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String descripcion, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(ingresarAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresoJustificacion.setInt(1,numeroSolicitud);
			ingresoJustificacion.setInt(2,articulo);
			ingresoJustificacion.setInt(3,atributo);
			ingresoJustificacion.setString(4,descripcion);
			int resultado=ingresoJustificacion.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;

		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la Justificaci�n del medicamento: "+e);
			return 0;
		}
	}
	*/
	/**
	 * M�todo para modificar la justificaci�n del medicamento
	 * este m�todo solo modifica un atributo, as� que se llama
	 * cada vez que se quiere modificar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo Par�metro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacci�n
	 * @return int mayor que 0 si se modific� correctamente el atributo espec�fico.
	 */
	public static int modificarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String descripcion, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			int resultado=0;
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(busquedaAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresoJustificacion.setInt(1,numeroSolicitud);
			ingresoJustificacion.setInt(2,articulo);
			ingresoJustificacion.setInt(3,atributo);
			ResultSetDecorator res=new ResultSetDecorator(ingresoJustificacion.executeQuery());
			if(res.next())
			{
				if(res.getInt("numResultados")>0)
				{
					ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(modificarAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ingresoJustificacion.setString(1,descripcion);
					ingresoJustificacion.setInt(2,numeroSolicitud);
					ingresoJustificacion.setInt(3,articulo);
					ingresoJustificacion.setInt(4,atributo);
					resultado=ingresoJustificacion.executeUpdate();
				}
				else
				{
					ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(ingresarAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ingresoJustificacion.setInt(1,numeroSolicitud);
					ingresoJustificacion.setInt(2,articulo);
					ingresoJustificacion.setInt(3,atributo);
					ingresoJustificacion.setString(4,descripcion);
					resultado=ingresoJustificacion.executeUpdate();
				}
			}
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;

		}
		catch (SQLException e)
		{
			logger.error("Error modificando la Justificaci�n del medicamento: "+e);
			return 0;
		}
	}

	/**
	 * M�todo para borrar atributos en caso de modificaci�n de la justificaci�n
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param estado
	 * @return
	 */
	public static int borrarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			int resultado=0;
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(borrarAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresoJustificacion.setInt(1,numeroSolicitud);
			ingresoJustificacion.setInt(2,articulo);
			ingresoJustificacion.setInt(3,atributo);
			resultado=ingresoJustificacion.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;

		}
		catch (SQLException e)
		{
			logger.error("Error borrando atributo de la Justificaci�n del medicamento: "+e);
			return 0;
		}
	}

	/**
	 * M�todo para ingresar el detalle de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param observaciones
	 * @param dosis
	 * @param frecuencia
	 * @param tipoFrecuencia Unidad de medida de la frecuencia
	 * @param via
	 * @param cantidadSolicitada Cantidad solicitada
	 * @param estado
	 * @return int mayor que 0 si se insert� correctamente el detalle de la solicitud.
	 */
	public static int ingresarDetalleSolicitudTransaccional(Connection con, int numeroSolicitud, int articulo, String observaciones, String dosis, String unidosis, int frecuencia, String tipoFrecuencia, String via, int cantidadSolicitada, String estado, String diasTratamiento)
	{
		logger.info("INGRESAR DETALLE SOLICITUD TRANSACCIONAL!!!");
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			
			PreparedStatementDecorator ingresarDetalle= new PreparedStatementDecorator(con.prepareStatement(ingresarDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ingresarDetalle.setInt(1,numeroSolicitud);
			
			ingresarDetalle.setInt(2,articulo);
			
			if(UtilidadTexto.isEmpty(observaciones))
			{
				ingresarDetalle.setObject(3, null);
			
			}
			else
			{
				ingresarDetalle.setString(3,observaciones);
			
			}

			if(UtilidadTexto.isEmpty(dosis))
			{
				ingresarDetalle.setObject(4, null);
			
			}
			else
			{
				ingresarDetalle.setString(4,dosis);
			
			}
			
			if(UtilidadTexto.isEmpty(tipoFrecuencia))
			{
				ingresarDetalle.setString(5, null);
			
			}
			else
			{
				ingresarDetalle.setInt(5, frecuencia);
			
			}
			
			
			if(UtilidadTexto.isEmpty(tipoFrecuencia))
			{
				ingresarDetalle.setObject(6, null);
			
			}
			else
			{
				ingresarDetalle.setString(6,tipoFrecuencia);
			
			}
			
			if(UtilidadTexto.isEmpty(via))
			{
				ingresarDetalle.setObject(7, null);
			
			}
			else
			{
				ingresarDetalle.setString(7,via);
			
			}
			
			ingresarDetalle.setInt(8,cantidadSolicitada);
			
			
			if(UtilidadTexto.isEmpty(unidosis))
			{
				ingresarDetalle.setObject(9, null);
			
			}
			else
			{
				ingresarDetalle.setString(9, unidosis);
			
			}
			
			if(UtilidadTexto.isEmpty(diasTratamiento))
			{	
				ingresarDetalle.setObject(10, null);
			
			}	
			else
			{	
				ingresarDetalle.setString(10, diasTratamiento);
			
			}	
			
			int nroDosisTotal=calculoNroDosisTotal(tipoFrecuencia, diasTratamiento, frecuencia);
			//int nroDosisTotal=0;
			if(!UtilidadTexto.isEmpty(Integer.toString(frecuencia)))
			{
				nroDosisTotal=calculoNroDosisTotal(tipoFrecuencia, diasTratamiento, frecuencia);
			}
/*puso armando
			int nroDosisTotal=0;
			if(!UtilidadTexto.isEmpty(frecuencia))
			{
				nroDosisTotal=calculoNroDosisTotal(tipoFrecuencia, diasTratamiento, Utilidades.convertirAEntero(frecuencia));
			}
*/
			if(nroDosisTotal>0)
				ingresarDetalle.setInt(11, nroDosisTotal);
			else
				ingresarDetalle.setObject(11, null);
			
			int resultado=ingresarDetalle.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el detalle de la solicitud "+e);
			return 0;
		}
	}

	
	/**
	 * 
	 * @param tipoFrecuencia
	 * @param diasTratamiento
	 * @param frecuencia
	 * @return
	 */
	private static int calculoNroDosisTotal(String tipoFrecuencia, String diasTratamiento, int frecuencia)
	{
		double nroDosisTotal=0;
		double diasTratamientoInt=0;
		if(UtilidadTexto.isEmpty(diasTratamiento))
			return -1;
		diasTratamientoInt=Double.parseDouble(diasTratamiento);
		
		logger.info("diastratamiento->"+diasTratamiento+" frecuencia->"+frecuencia);
		
		if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaHoras) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaHoras))
		{
			nroDosisTotal=(diasTratamientoInt*24)/(frecuencia*1.0);
		}
		else if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaMinutos) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaMinutos))
		{
			nroDosisTotal=(diasTratamientoInt*60*24)/(frecuencia*1.0);
		}
		else if(tipoFrecuencia.equals(ConstantesBD.codigoTipoFrecuenciaDias) || tipoFrecuencia.equals(ConstantesBD.nombreTipoFrecuenciaDias))
		{
			nroDosisTotal=diasTratamientoInt/(frecuencia*1.0);
		}
		logger.info("\n\n\n\n\n\n\n\n\nNUMERO DE DOSIS------->"+nroDosisTotal+"\n\n\n\n\n\n\n\n\n");
		return UtilidadTexto.aproximarSiguienteUnidad(nroDosisTotal+"");
	}
	
	
	
	
	/**
	 * M�todo para modificar el detalle de la solicitud de medicamentos
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param observaciones
	 * @param dosis
	 * @param frecuencia
	 * @param tipoFrecuencia Unidad de medida de la frecuencia
	 * @param via
	 * @param cantidadSolicitada Cantidad Solicitada
	 * @param estado
	 * @return int mayor que 0 si se insert� correctamente el detalle de la solicitud.
	 */
	public static int modificarDetalleSolicitudTransaccional(Connection con, int numeroSolicitud, int articulo, String observaciones, String dosis, int frecuencia, String tipoFrecuencia, String via, int cantidadSolicitada, String estado, String unidosis, String diasTratamiento)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresarDetalle= new PreparedStatementDecorator(con.prepareStatement(modificarDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(UtilidadTexto.isEmpty(observaciones))
				ingresarDetalle.setObject(1, null);
			else
				ingresarDetalle.setString(1,observaciones);
			
			if(UtilidadTexto.isEmpty(dosis))
				ingresarDetalle.setObject(2, null);
			else
				ingresarDetalle.setString(2,dosis);
			ingresarDetalle.setInt(3,frecuencia);
			if(UtilidadTexto.isEmpty(tipoFrecuencia))
				ingresarDetalle.setObject(4, null);
			else
				ingresarDetalle.setString(4,tipoFrecuencia);
			if(UtilidadTexto.isEmpty(via))
				ingresarDetalle.setObject(5, null);
			else
				ingresarDetalle.setString(5,via);
			
			ingresarDetalle.setInt(6,cantidadSolicitada);
			
			if(UtilidadTexto.isEmpty(unidosis))
				ingresarDetalle.setObject(7, null);
			else
				ingresarDetalle.setString(7, unidosis);
			
			int nroDosisTotal= calculoNroDosisTotal(tipoFrecuencia, obtenerDiasTratamientoSolicitud(con, numeroSolicitud, articulo)+"", frecuencia);
			if(nroDosisTotal>0)
				ingresarDetalle.setInt(8, nroDosisTotal);
			else	
				ingresarDetalle.setObject(8, null);
			
			if(UtilidadTexto.isEmpty(diasTratamiento))
				ingresarDetalle.setNull(9, Types.INTEGER);
			else
				ingresarDetalle.setInt(9, Utilidades.convertirAEntero(diasTratamiento));
			
			ingresarDetalle.setInt(10,numeroSolicitud);
			ingresarDetalle.setInt(11,articulo);
			int resultado=ingresarDetalle.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el detalle de la solicitud "+e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @return
	 */
	private static String obtenerDiasTratamientoSolicitud(Connection con, int numeroSolicitud, int articulo)
	{
		try
		{
			String cadena= "SELECT CASE WHEN dias_tratamiento IS NULL THEN '' ELSE dias_tratamiento||'' end as dias  FROM detalle_solicitudes where numero_solicitud=? and  articulo=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, articulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("dias");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error obtenerDiasTratamientoSolicitud "+e);
		}
		return "";
	}
	
	
	/**
	 * M�todo para el ingreso de la solicitud de medicamentos
	 * @param con
	 * @param numerosolicitud
	 * @param observacionesGenerales
	 * @param estado
	 * @param entidadSubcontratada 
	 * @return int mayor que 0 si se insert� correctamente la solicitud.
	 */
	public static int ingresarSolicitudMedicamentos(Connection con, int numerosolicitud, String observacionesGenerales, String estado, String centroCostoPrincipal, String controlEspecial, String entidadSubcontratada)
	{
	
		String consulta="";
		
		
		logger.info("\n entre a SQL ingresarSolicitudMedicamentos  numero solicitud-->"+numerosolicitud+" obsergen-->"+observacionesGenerales+" estado -->"+estado+" CentroCostoPpal -->"+centroCostoPrincipal+"controlEspecial------>"+controlEspecial+" entidad subcontratada : "+entidadSubcontratada);
			consulta=ingresarSolicitudMedicamentosStr;
		
				
		
		
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			
			
			PreparedStatementDecorator ingresoSolicitud= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			logger.info("Consulta----> "+consulta);
			
			
		
		
		
					ingresoSolicitud.setInt(1, numerosolicitud);
					ingresoSolicitud.setString(2, observacionesGenerales);
					ingresoSolicitud.setObject(3, centroCostoPrincipal);
					ingresoSolicitud.setString(4, controlEspecial);
								
			  if(entidadSubcontratada!=null){
					if(entidadSubcontratada.equals(""))
						ingresoSolicitud.setNull(5, Types.VARCHAR);
					else
						ingresoSolicitud.setString(5, entidadSubcontratada);
				}else
					ingresoSolicitud.setNull(5, Types.VARCHAR);
			int resultado=ingresoSolicitud.executeUpdate()
			
			;
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la solicitud de medicamentos "+e);
			return 0;
		}
	}
	

	/**
	 * M�todo para la modificaci�n de la solicitud de medicamentos
	 * @param con
	 * @param numerosolicitud
	 * @param observacionesGenerales
	 * @param estado
	 * @return int mayor que 0 si se insert� correctamente la solicitud.
	 */
	public static int modificarSolicitudMedicamentos(Connection con, int numerosolicitud, String observacionesGenerales, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresoSolicitud= new PreparedStatementDecorator(con.prepareStatement(modificarSolicitudMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresoSolicitud.setString(1, observacionesGenerales);
			ingresoSolicitud.setInt(2, numerosolicitud);
			int resultado=ingresoSolicitud.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la solicitud de medicamentos "+e);
			return 0;
		}
	}

	/**
	 * Consultar el detalle de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @retur Collection con las observaciones generales de la solicitud de medicamentos 
	 */
	public static Collection consultarSolicitudMedicamentos(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator consulta= new PreparedStatementDecorator(con.prepareStatement(consultarSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\nconsultarSolicitud :"+consultarSolicitud);
			logger.info("\n\nnumeroSolicitud : "+numeroSolicitud+"\n\n");
			consulta.setInt(1, numeroSolicitud);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle de la solicitud "+numeroSolicitud+" "+e);
			return null;
		}
	}
	
	/**
	 * Consultar el detalle de los art�culos que contiene la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion Institucion a la cual pertenece el usuario
	 * @return Collection con el detalle de los articulos contenidos en la solicitud
	 */
	public static Collection consultarDetalleSolicitudArticulos(Connection con, int numeroSolicitud, int codigoInstitucion)
	{
		/**
		 * Statement para consultar el detalle de los articulos de una solicitud
		 */
		String consultarDetalleArticulos=	"SELECT 	" +
													"articulos.*, " +
													"sa.motivo AS motivoSuspension, " +
													"CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS esSuspendido, " +
													"gettotaladminfarmacia(articulos.articulo,articulos.numerosolicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") AS totalFarmacia, " +
													"gettotaladminfarmacia(articulos.articulo,articulos.numerosolicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") AS totalPaciente " +
													"from ((	SELECT " +
																"ds.articulo AS articulo, " +
																"null AS artPrincipal, " +
																"ds.numero_solicitud AS numeroSolicitud, " +
																"ds.observaciones AS observaciones,";
		
		int[] codigosJustificaciones=Utilidades.buscarCodigosJustificaciones(con, codigoInstitucion, false, true);
		for(int i=0; i<codigosJustificaciones.length; i++)
		{
			consultarDetalleArticulos+="getAtributo("+codigosJustificaciones[i]+",ds.articulo,ds.numero_solicitud) as just"+codigosJustificaciones[i]+", ";
		}
		consultarDetalleArticulos+=	"ds.dosis AS dosis, " +
									"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END AS unidosis, " +
									"coalesce(ds.frecuencia,0) AS frecuencia, " +
									"CASE WHEN ds.tipo_frecuencia IS NULL THEN '' ELSE ds.tipo_frecuencia END AS tipoFrecuencia, " +
									"ds.via AS via, " +
									"ds.cantidad AS cantidad, " +
									"nat.es_pos AS esPos, " +
									"getDespacho(ds.articulo,ds.numero_solicitud) AS despachoTotal, " +
									"CASE WHEN ds.dias_tratamiento IS NULL THEN '' ELSE ds.dias_tratamiento||'' END as diastratamiento, " +
									"nat.es_medicamento AS esMedicamento " +
									"from " +
									"detalle_solicitudes ds " +
									"INNER JOIN articulo a ON(a.codigo=ds.articulo) " +
									"INNER JOIN naturaleza_articulo nat ON(a.naturaleza=nat.acronimo)) " +
									"UNION (" +
									"SELECT dd.articulo as articulo, " +
									"dd.art_principal AS artPrincipal, " +
									"ds.numero_solicitud AS numeroSolicitud, " +
									"ds.observaciones AS observaciones, ";
		for(int i=0; i<codigosJustificaciones.length; i++)
		{
			consultarDetalleArticulos+="getAtributo("+codigosJustificaciones[i]+",ds.articulo,ds.numero_solicitud) AS just"+codigosJustificaciones[i]+", ";
		}
		consultarDetalleArticulos+=	"ds.dosis AS dosis, " +
									"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END AS unidosis, " +
									"coalesce(ds.frecuencia,0) AS frecuencia, " +
									"CASE WHEN ds.tipo_frecuencia IS NULL THEN '' ELSE ds.tipo_frecuencia END AS tipoFrecuencia, " +
									"ds.via AS via, " +
									"ds.cantidad AS cantidad, " +
									"nat.es_pos AS espos, " +
									"getDespacho(dd.articulo,ds.numero_solicitud) AS despachoTotal, " +
									"CASE WHEN ds.dias_tratamiento IS NULL THEN '' ELSE ds.dias_tratamiento||'' END as diastratamiento, " +
									"nat.es_medicamento AS esMedicamento " +
									"from " +
									"detalle_despachos dd " +
									"INNER JOIN despacho d ON(dd.despacho=d.orden) " +
									"INNER JOIN detalle_solicitudes ds ON(dd.art_principal=ds.articulo AND dd.articulo<>dd.art_principal AND ds.numero_solicitud=d.numero_solicitud) " +
									"INNER JOIN articulo a ON(a.codigo=dd.articulo) " +
									"INNER JOIN naturaleza_articulo nat ON(a.naturaleza=nat.acronimo AND nat.es_medicamento='"+ConstantesBD.acronimoSi+"'))) " +
									"articulos LEFT OUTER JOIN suspension_articulo sa ON(articulos.articulo=sa.articulo AND articulos.numerosolicitud=sa.numero_solicitud) where articulos.numerosolicitud=?";
		
		try
		{
			logger.info("--->"+consultarDetalleArticulos+"------"+numeroSolicitud);
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarDetalleArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1, numeroSolicitud);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle de los articulos de la solicitud n�mero "+numeroSolicitud+": "+e);
			return null;
		}
	}
	
	/**
	 * Listado de los medicamentos de todas las solicitudes del paciente
	 * @param con
	 * @param cuentaPaciente
	 * @return
	 */
	public static Collection consultarListadoMedicamentosPaciente(Connection con, int cuentaPaciente)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarListadoMedicamentosPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, cuentaPaciente);
			stm.setInt(2, cuentaPaciente);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error listando los medicamentos de todas las solicitudes del paciente "+e);
			return null;
		}
	}
	
	/**
	 * Consulta todas las solicitudes de medicamentos que se tienen por paciente
	 * @param Tipo_BD 
	 * @param con, Conexion
	 * @param cuenta, codigo de la cuenta del paciente que se desea consultar (Si esta llega en 0 no se hace el filtrado)
	 * @param centroCosto Centro de costo del paciente (Si este llega en 0 no se hace el filtrado)
	 * @return Collection, Con las solicitudes que tiene un paciente.
	 */
	public static Collection consultarSolicitudesMedicamento(Connection con, HashMap<String, Object> criteriosBusquedaMap, int Tipo_BD)
	{
		logger.info("\n\n ********************consultarSolicitudesMedicamento*******************************");
		try
		{
			String consultarSolicitudesArea="";
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
			
				consultarSolicitudesArea+=consultarSolicitudesAreaOra;
				break;
			case DaoFactory.POSTGRESQL:
			
				consultarSolicitudesArea+=consultarSolicitudesAreaPos;
				break;
				
			default:
				break;
			
			}
			
			if(Integer.parseInt(criteriosBusquedaMap.get("cuenta").toString())>0)
			{
				PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("*******************************************************************************");
				
				logger.info("\n\n *consultarSolicitudes-->"+consultarSolicitudes.replace("?", criteriosBusquedaMap.get("cuenta")+""));
				
				consulta.setInt(1, Integer.parseInt(criteriosBusquedaMap.get("cuenta").toString()));
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
			}
			else
			{
				String consultaStr=consultarSolicitudesArea;
				
				if(UtilidadFecha.esFechaValidaSegunAp(criteriosBusquedaMap.get("fechaInicialFiltro").toString())
					&& UtilidadFecha.esFechaValidaSegunAp(criteriosBusquedaMap.get("fechaFinalFiltro").toString()))
				{
					logger.info("\n\n FECHA : "+(criteriosBusquedaMap.get("fechaInicialFiltro").toString())+"\n\n");
					consultaStr+=" AND (s.fecha_solicitud>='"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicialFiltro").toString())+"' and s.fecha_solicitud<='"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinalFiltro").toString())+"') ";
				}
				
				//filtrado por area
				if(Integer.parseInt(criteriosBusquedaMap.get("areaFiltro").toString())>0)
				{
					logger.info("\n\n AREA : "+criteriosBusquedaMap.get("areaFiltro")+"\n\n");
					consultaStr+=" AND c.area = "+criteriosBusquedaMap.get("areaFiltro")+" ";
				}
					//listadoGeneradoStr+=" AND sol.centro_costo_solicitante = "+criteriosBusquedaMap.get("areaFiltro").toString();
				
				//filtrado por piso, habitacion cama
				if(Integer.parseInt(criteriosBusquedaMap.get("camaFiltro").toString())>0)
				{
					logger.info("\n\n AREA : "+criteriosBusquedaMap.get("camaFiltro")+"\n\n");
					consultaStr+=" AND  c.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigocama="+criteriosBusquedaMap.get("camaFiltro")+") ";
				}
				else
				{
					if(Integer.parseInt(criteriosBusquedaMap.get("habitacionFiltro").toString())>0)
					{
						logger.info("\n\n AREA : "+criteriosBusquedaMap.get("habitacionFiltro")+"\n\n");
						consultaStr+=" AND  c.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigopkhabitacion="+criteriosBusquedaMap.get("habitacionFiltro")+") ";
					}
					else
					{
						if(Integer.parseInt(criteriosBusquedaMap.get("pisoFiltro").toString())>0)
						{
							logger.info("\n\n AREA : "+criteriosBusquedaMap.get("pisoFiltro")+"\n\n");
							consultaStr+=" AND  c.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigopkpiso="+criteriosBusquedaMap.get("pisoFiltro")+") ";
						}
					}
				}
				
				consultaStr+=" ORDER BY fechaHoraSolicitud ";
				logger.info("**************************************************************");
				logger.info("\n\n******CONSULTA SOLICITUDES:  "+consultaStr+" \n");
				PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las solicitudes : "+e);
			return null;
		}
	}
	
	/**
	 * Consulta las solicitudes Historicas
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static HashMap consultarHistorialSolicitudes(Connection con,HashMap parametros)
	{
		HashMap mapa = new HashMap();
		HashMap medicamentos;
		
		String cadena = consultarSolicitudesHist;
		
		if(parametros.get("esUltimaSolicitud").toString().equals(ConstantesBD.acronimoSi))
		{
			cadena+= " AND s.numero_solicitud = (SELECT MAX(soli.numero_solicitud) " +
					 "		  				     FROM solicitudes soli " +
					 "						     INNER JOIN ingresos ing ON (ing.id = "+parametros.get("codigoIngreso")+") " +
					 "							 INNER JOIN cuentas cue ON (cue.id_ingreso = "+parametros.get("codigoIngreso")+" ) " +
					 "		   		  		     WHERE soli.cuenta = cue.id  " +
					 "							 AND soli.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+
					 "          				 AND soli.estado_historia_clinica !=  "+ConstantesBD.codigoEstadoHCAnulada +
					 "							 ) ";
		}
		else		
			cadena+="AND c.codigo_paciente = "+parametros.get("codigoPaciente")+" "+
					//"AND c.via_ingreso = "+parametros.get("codigoViaIngreso")+" " +
					"AND s.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada+" ";
		
		cadena+=" ORDER BY s.fecha_solicitud DESC ,s.hora_solicitud DESC ";
		
		logger.info("\n\n cadena >> "+cadena+" >> "+parametros);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false); 
			
			//Captura la informaci�n de los medicamentos de la solicitud 
			for(int i = 0 ; i < Integer.parseInt(mapa.get("numRegistros").toString()); i++)
			{
				medicamentos = new HashMap();
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultarMedicamentosHist,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Integer.parseInt(parametros.get("codigoAlmacen").toString()));
				ps.setInt(2,Integer.parseInt(parametros.get("institucion").toString()));
				ps.setInt(3,Integer.parseInt(mapa.get("numerosolicitud_"+i).toString()));				
				ps.setInt(4,Integer.parseInt(mapa.get("numerosolicitud_"+i).toString()));
				ps.setInt(5,Integer.parseInt(mapa.get("numerosolicitud_"+i).toString()));
				
				logger.info("\n\n articulos >> "+consultarMedicamentosHist+" >> almacen >> "+parametros.get("codigoAlmacen").toString()+" >> "+mapa.get("numerosolicitud_"+i).toString());
				
				medicamentos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
				medicamentos.put("cargar_"+i,ConstantesBD.acronimoNo);				
				
				if(!medicamentos.get("numRegistros").toString().equals("0"))
				{
					mapa.put("tienemedicamentos_"+i,ConstantesBD.acronimoSi);
					mapa.put("medicamentos_"+i,medicamentos);
				}
				else
				{
					
					mapa.put("tienemedicamentos_"+i,ConstantesBD.acronimoNo);
					mapa.put("medicamentos_"+i,medicamentos);
					
					//Si se consulta la ultima solicitud y la solicitud no tiene medicamentos se interpreta que como si no existiera tal 
					if(parametros.get("esUltimaSolicitud").toString().equals(ConstantesBD.acronimoSi))					
						mapa.put("numRegistros","0");					
				}
			}			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
				
		return mapa;
	}
	

	/**
	 * @param con
	 * @param i 
	 * @param numeroSolicitud
	 * @return
	 */
	public static Collection consultarSolicitudesMedicamentoConAdministracion(Connection con, int cuenta, int Tipo_BD) 
	{
		try
		{
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudesConAdministracion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n\n consultarSolicitudesConAdministracion :"+consultarSolicitudesConAdministracion);
			logger.info("\n\n cuenta : "+cuenta);
			consulta.setInt(1, cuenta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las solicitudes"+e);
			return null;
		}
	}

	
	/**
	 * M�todo para cargar los insumos que fueron despachados a para una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return Collection con la consulta de los insumos para la solicitud dada
	 */
	public static Collection consultarInsumos(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator insumos= new PreparedStatementDecorator(con.prepareStatement(consultarInsumosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n\n consultarInsumosStr :"+consultarInsumosStr);
			logger.info("\n\n cuenta : "+numeroSolicitud);
			insumos.setInt(1, numeroSolicitud);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(insumos.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error cargando los insumos de la solicitud "+numeroSolicitud+": "+e);
			return null;
		}
	}
	
	/**
	 * M�todo para cargar los insumos que fueron cargados
	 * a la solicitud en la farmacia
	 * @param con, Connection con la fuente de datos 
	 * @param numeroSolicitud, int numero de la solicitud
	 * @return Collection con la consulta de los insumos 
	 * @see com.princetonsa.dao.SolicitudMedicamentosDao#consultarInsumosFarmacia(java.sql.Connection)
	 */
	public static Collection consultarInsumosFarmacia(Connection con,int numeroSolicitud) {
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("Numero Solicitud: " + numeroSolicitud);

		try	{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInsumosFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, numeroSolicitud);

			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) {
			logger.error("Error cargando los insumos Farmacia [ SqlBaseSolicitudMedicamentosDao ] "+e);
			return null;
		}
	}
	
	/**
	 * M�todo para borrar articulos de una solicitud
	 * El m�todo borra en cascada la descripcion de los articulos.
	 * @param con
	 * @param numeroSolicitud Solicitud a la cual pertenece el art�culo que se desea borrar
	 * @param articulo C�digo del art�culo que se desea borrar
	 * @return Mayor que 0 si se elimin� alg�n art�culo
	 */
	public static int eliminarArticulo(Connection con, int numeroSolicitud, int articulo)
	{
		try
		{
			logger.info("\n\neliminarArticulosStr-->"+eliminarArticulosStr+" numsol-->"+numeroSolicitud+" articulo-->"+articulo);
			PreparedStatementDecorator eliminar= new PreparedStatementDecorator(con.prepareStatement(eliminarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminar.setInt(1,numeroSolicitud);
			eliminar.setInt(2,articulo);
			return eliminar.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando el art�culo "+articulo+" de la solicitud "+numeroSolicitud+": "+e);
			return 0;
		}
	}
	
	/**
	 * M�todo para suspender un medicamento dada la solicitud y el medicamento
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solicitud a la cual pertenece el medicamento
	 * @param articulo Articulo que se desea suspender
	 * @param motivo Motivo de la suspensi�n
	 * @param codigoMedico M�dico que realiz� la suspensi�n
	 * @return true si el medicamento se suspendi� correctamente
	 */
	public static boolean suspenderMedicamento(Connection con, int numeroSolicitud, int articulo, String motivo, int codigoMedico)
	{
		try
		{
			PreparedStatementDecorator suspension= new PreparedStatementDecorator(con.prepareStatement(suspenderMedicamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			suspension.setInt(1, numeroSolicitud);
			suspension.setInt(2, articulo);
			suspension.setString(3, motivo);
			suspension.setInt(4, codigoMedico);
			if(suspension.executeUpdate()>0)
			{
				return true;
			}
			else
			{
				logger.error("Error al suspender el medicamento "+articulo+" en la solicitud "+numeroSolicitud+": "+suspension.getWarnings().getMessage());
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error al suspender el medicamento "+articulo+" en la solicitud "+numeroSolicitud+": "+e);
			return false;
		}
	}
	
	/**
	 * M�todo para modificar el motivo se suspensi�n de un
	 * medicamento dada la solicitud y el codigo del medicamento
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solicitud a la cual pertenece el medicamento
	 * @param articulo Articulo que se desea suspender
	 * @param motivo Motivo de la suspensi�n
	 * @return true si el medicamento se suspendi� correctamente
	 */
	public static boolean modificarMotivoSuspension(Connection con, int numeroSolicitud, int articulo, String motivo)
	{
		try
		{
			PreparedStatementDecorator suspension= new PreparedStatementDecorator(con.prepareStatement(modificarMotivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			suspension.setString(1, motivo);
			suspension.setInt(2, numeroSolicitud);
			suspension.setInt(3, articulo);
			if(suspension.executeUpdate()>0)
			{
				return true;
			}
			else
			{
				logger.error("Error al suspender el medicamento "+articulo+" en la solicitud "+numeroSolicitud+": "+suspension.getWarnings().getMessage());
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error al suspender el medicamento "+articulo+" en la solicitud "+numeroSolicitud+": "+e);
			return false;
		}
	}

	/**
	 * M�todo para verificar la existencia de un articulo en la solictud
	 * @param con Conecci�n con la BD
	 * @param numeroSolicitud Numero de solicitud en la cual se solicita el articulo
	 * @param articulo C�digo del articulo
	 * @return true si el articulo existe en a solicitud dada, false de lo contrario
	 */
	public static boolean existeMedicamento(Connection con, int numeroSolicitud, int articulo)
	{
		PreparedStatementDecorator existe;
		try
		{
			existe =  new PreparedStatementDecorator(con.prepareStatement(existeMedicamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existe.setInt(1, numeroSolicitud);
			existe.setInt(2, articulo);
			logger.info("existeMedicamernto-->"+existeMedicamentoStr+" numsol->"+numeroSolicitud+" art->"+articulo);
			if(new ResultSetDecorator(existe.executeQuery()).next())
			{
				logger.info("existeMedicamernto-->true");
				return true;
			}
			else
			{
				logger.info("existeMedicamernto-->false");
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error(" "+e);
			return false;
		}
	}
	
	/**
	 * M�todo para actualizar cantidades solicitadas
	 * de una solicitud de medicamentos
	 * @param con Conexi�n con la BD
	 * @param codigo del articulo 
	 * @param cantidad Cantidad solicitada
	 * @param numeroSolicitud Solicitud a modificar
	 * @return int con el n�mero de elementos actualizados
	 * -1 en caso de alg�n error
	 */
	public static int actualizarCantidades(Connection con, int codigoArticulo, int cantidad, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator actualizarCantidadStm= new PreparedStatementDecorator(con.prepareStatement(actualizarCantidadSolicitadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarCantidadStm.setInt(1, cantidad);
			actualizarCantidadStm.setInt(2, codigoArticulo);
			actualizarCantidadStm.setInt(3, numeroSolicitud);
			return actualizarCantidadStm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando las cantidades de las solicitudes : "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Numero de Convenios Plan Especial asociados al ingreso del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarConveniosPlanEspecialPacienteStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("planEspecialConvenio");
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}

	public static ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(
			Connection con, String codigoCentroCostoPrincipal,
			int codigoInstitucionInt) {
		ArrayList<DtoEntidadSubcontratada> resultados = new ArrayList<DtoEntidadSubcontratada>();
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerEntidadesSubcontratadasCentroCostoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n ************* obtenerEntidadesSubcontratadasCentroCostoStr : "+obtenerEntidadesSubcontratadasCentroCostoStr);
			logger.info("\n\ncodigoCentroCostoPrincipal :"+codigoCentroCostoPrincipal);
			logger.info("\n\n codigoInstitucionInt : "+codigoInstitucionInt);
			pst.setString(1,codigoCentroCostoPrincipal);
			pst.setInt(2,codigoInstitucionInt);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoEntidadSubcontratada entidad = new DtoEntidadSubcontratada();
				entidad.setConsecutivo(rs.getString("consecutivo"));
				entidad.setCodigo(rs.getString("codigo"));
				entidad.setRazonSocial(rs.getString("razon_social"));
				
				
				resultados.add(entidad);
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEntidadesSubcontratadasCentroCosto: "+e);
		}
		return resultados;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articuloEquivalente
	 * @param articuloPrincipal
	 * @return
	 */
	public static boolean insertarAticulosEquivalenteSolicitud(Connection con, int numeroSolicitud, int articuloEquivalente, int articuloPrincipal) 
	{
		String cadena="insert into ordenes.detalle_solicitudes(numero_solicitud,articulo,articulo_principal,dosis,frecuencia,tipo_frecuencia,via,observaciones)(select numero_solicitud,?,?, dosis, frecuencia, tipo_frecuencia, via, observaciones from ordenes.detalle_solicitudes where numero_solicitud=? and articulo=?)";
		int res=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator ps;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, articuloEquivalente);
			ps.setInt(2, articuloPrincipal);
			ps.setInt(3, numeroSolicitud);
			ps.setInt(4, articuloPrincipal);
			res=ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res>0;
	}

	/**
	 * Este m&eacute;todo se encarga de buscar los art&iacute;culos
	 * asociados a una solicitud de medicamentos
	 * @param conn
	 * @param dto
	 * @return ArrayList<DtoArticulos>
	 * @author Diana Carolina G
	 */
	public static ArrayList<DtoArticulos> buscarArticulosXSolicitudMedicamentos(Connection conn,
			int numeroSolicitud) {
		
		ArrayList<DtoArticulos> lista = new ArrayList<DtoArticulos>();
		try
		{
			
			String consulta="select art.codigo, art.descripcion, art.naturaleza, um.nombre, na.nombre, na.es_medicamento, "+
					        "ds.cantidad, ds.dias_tratamiento, um.acronimo, ds.dosis, uni.codigo, " +
					        "ds.frecuencia, ds.tipo_frecuencia, ds.via " +
							"from ordenes.detalle_solicitudes ds "+
				            "INNER JOIN inventarios.articulo art ON(ds.articulo = art.codigo) "+
				            "INNER JOIN inventarios.naturaleza_articulo na ON(art.naturaleza = na.acronimo) "+
							"LEFT JOIN inventarios.unidosis_x_articulo uni on (ds.unidosis_articulo = uni.codigo) "+
							"LEFT JOIN inventarios.unidad_medida um on (uni.unidad_medida = um.acronimo) "+						
							"where art.institucion = na.institucion "	+						
							"and ds.numero_solicitud = ? ";
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(conn.prepareStatement(consulta,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, numeroSolicitud);
			ResultSet rs = ps.executeQuery();
			lista = poblarArticulosSolicitudMedicamentos(rs);
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lista;
	}

	/**
	 * Este m&eacute;todo se encarga de poblar un objeto de tipo
	 * DtoArticulos
	 * @param rs
	 * @return ArrayList<DtoArticulos>
	 * @author Diana Carolina G
	 */
	
	private static ArrayList<DtoArticulos> poblarArticulosSolicitudMedicamentos (
			ResultSet rs) throws Exception{
		
		ArrayList<DtoArticulos> lista = new ArrayList<DtoArticulos>();
		DtoArticulos dto=null;
		while(rs.next()){
			dto= new DtoArticulos();
			dto.setCodigoArticulo(rs.getInt(1));
			dto.setDescripcionArticulo(rs.getString(2));
			dto.setNaturalezaArticulo(rs.getString(3));
			dto.setUnidad_medida(rs.getString(4));
			dto.setNombreNaturaleza(rs.getString(5));
			dto.setEsMedicamento((rs.getString(6)).charAt(0));
			dto.setCantidadArticulo(rs.getInt(7));
			dto.setDiasTratamiento(rs.getLong(8));
			dto.setAcronimoUnidadMedida(rs.getString(9));
			dto.setDosis(rs.getString(10));
			dto.setDosisXArticuloID(rs.getLong(11));
			dto.setFrecuencia(rs.getInt(12));
			dto.setTipoFrecuencia(rs.getString(13));
			dto.setVia(rs.getString(14));
			lista.add(dto);
			
		}
		
		return lista;
	}
	
	
	/**
	 * @param conn
	 * @param codigoMedico
	 * @return datos del medico que anula 
	 * @throws SQLException
	 */
	public static String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException{
		
		String res=" "; 
			
			String consulta=" select per.primer_nombre||' '||per.segundo_nombre||' '||per.primer_apellido||' '||per.segundo_apellido||' - ' " +
			" ||med.numero_registro||' '||GETESPECIALIDADESMEDICO1(med.codigo_medico,',') as nombre_Medico_Anulacion  " +
			" from medicos med join personas per on(med.codigo_medico=per.codigo)   " +
			" where  med.codigo_medico =?"; 
			PreparedStatement pst = conn.prepareStatement(consulta);
			pst.setInt(1,codigoMedico);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()){
				res=rs.getString("nombre_Medico_Anulacion");
			}
			
		
		
		return res;
	}
	
}
