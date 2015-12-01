/*
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
package com.princetonsa.dao.postgresql.ordenesmedicas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import util.ConstantesBD;

import com.princetonsa.dao.ordenesmedicas.OrdenesAmbulatoriasDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.SqlBaseOrdenesAmbulatoriasDao;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.ordenes.InfoArticuloOrdenAmbulatoriaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
public class PostgresqlOrdenesAmbulatoriasDao implements OrdenesAmbulatoriasDao 
{
	/**
	 * 
	 */
	private static String cadenaConsultaOrdenes="SELECT " +
															" oa.codigo as codigo," +
															" 'false' as numeroCheck," +
															" oa.consecutivo_orden as numero," +
															" to_char(oa.fecha,'dd/mm/yyyy') as fecha," +
															" oa.fecha as fechabd," +
															" oa.hora AS hora," +
															" getdescordenambulatorio(oa.codigo,oa.tipo_orden) as descripcion," +
															" case when oa.tipo_orden = 1 then doas.cantidad else null end as cantidad," +
															" case when oa.tipo_orden = 1 then getcodservordenambser(oa.codigo) else null end as servicio," +
															" oa.tipo_orden as tipoorden," +
															" toa.descripcion as nomtipoorden, " +
															" oa.urgente as urgente," +
															" oa.centro_atencion_solicita as centrosolicita," +
															" u.codigo_persona as profesional," +
															" oa.especialidad_solicita as especialidad," +
															" oa.observaciones as observaciones," +
															" oa.consulta_externa as consultaexterna," +
															" oa.estado as estado, " +
															" eoa.descripcion as descestado, " +
															" oa.control_especial AS controlespecial," +
															" oa.pyp as pyp," +
															" CASE WHEN " +
																" oa.estado in" +
																"( " +
																	ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+", " +
																	ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada+"" +
																") " +
																"THEN " +
																"( " +
																	"CASE WHEN oa.centro_costo_solicita IS NULL THEN '' " +
																	"ELSE getnomcentrocosto(centro_costo_solicita)" +
																	"END" +
																") " +
																"ELSE " +
																 "ORDENES.GETCCTANTEORDENAMB(oa.codigo) END AS nombrecentrocostosolicita," +
														" oa.otros " +
														" from ordenes_ambulatorias oa " +
														" inner join usuarios u on (oa.usuario_solicita=u.login) " +
														" inner join tipos_ordenes_ambulatorias toa on(toa.codigo=oa.tipo_orden) " +
														" inner join estados_ord_ambulatorias eoa on (eoa.codigo=oa.estado) " +
														" left outer join det_orden_amb_servicio doas on(oa.codigo=doas.codigo_orden) " +
														" where 1=1";
	
	/**
	 * 
	 */
	private static String cadenaConsultaResultadoOrdenes =	"SELECT resultado " +
															"FROM resultado_orden_ambulatorias " +
															"WHERE codigo_orden = " +
															"(SELECT codigo FROM ordenes_ambulatorias WHERE consecutivo_orden=?)";
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param institucion 
	 * @param estado
	 * @return
	 */
	public HashMap consultarOrdenesAmbulatoriasPaciente(Connection con, int codigoPersona, String institucion, int estado, int idIngreso)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarOrdenesAmbulatoriasPaciente(con,codigoPersona,institucion,estado,cadenaConsultaOrdenes, idIngreso);
	}

	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias x codigo
	 * @param con
	 * @param codigoPersona
	 * @param codigoInstitucion
	 * @param i
	 */
	public HashMap consultarOrdenesAmbulatoriasXCodigoOrden(Connection con, String codigoOrden , String institucion)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarOrdenesAmbulatoriasXCodigoOrden(con, codigoOrden, institucion, cadenaConsultaOrdenes);
	}


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public HashMap cargarDetalleOrdenArticulos(Connection con, String codigoOrden)
	{
		return SqlBaseOrdenesAmbulatoriasDao.cargarDetalleOrdenArticulos(con,codigoOrden);
	}
	

	/**
	 * 
	 * @param con
	 * @param mapaInfoGeneral
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean guardarInformacionGeneralAmbulatorios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO ordenes_ambulatorias(codigo,consecutivo_orden,institucion,codigo_paciente,tipo_orden,pyp,urgente,fecha," +
						"hora,estado,observaciones,centro_atencion_solicita,usuario_solicita,especialidad_solicita,fecha_confirmacion," +
						"hora_confirmacion,usuario_confirma,consulta_externa, centro_costo_solicita,otros, ingreso,CUENTA_SOLICITANTE," +
						"control_especial, acronimo_diagnostico,tipo_cie_diagnostico,cita_asociada)" +
						" values (nextval('seq_ordenes_ambulatorias'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		return SqlBaseOrdenesAmbulatoriasDao.guardarInformacionGeneralAmbulatorios(con,vo,cadena);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param detalle
	 * @return
	 */
	public boolean guardarInformacionDetalleOrdenAmbulatorioServicio(Connection con, HashMap vo)
	{
		return SqlBaseOrdenesAmbulatoriasDao.guardarInformacionDetalleOrdenAmbulatorioServicio(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean guardarInformacionDetalleOrdenAmbulatorioArticulo(Connection con, HashMap vo)
	{
		return SqlBaseOrdenesAmbulatoriasDao.guardarInformacionDetalleOrdenAmbulatorioArticulo(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean ingresarResultado(Connection con, HashMap vo)
	{
		return SqlBaseOrdenesAmbulatoriasDao.ingresarResultado(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean anularOrden(Connection con, HashMap vo)
	{
		return SqlBaseOrdenesAmbulatoriasDao.anularOrden(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param estado
	 * @return
	 */
	public boolean actualizarEstadoOrdenAmbulatoria(Connection con, String codigo, int estado)
	{
		return SqlBaseOrdenesAmbulatoriasDao.actualizarEstadoOrdenAmbulatoria(con,codigo,estado);
	}
	

	/**
	 * 
	 * @param con
	 * @param codOrden
	 * @param estado
	 */
	public boolean actualizarEstadoActividadProgramaPYPPAcienteNumOrden(Connection con, String codOrden, String estado,String usuario,String comentario)
	{
		return SqlBaseOrdenesAmbulatoriasDao.actualizarEstadoActividadProgramaPYPPAcienteNumOrden(con,codOrden,estado,usuario,comentario);
	}
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarSolicitudEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		return SqlBaseOrdenesAmbulatoriasDao.actualizarSolicitudEnOrdenAmbulatoria(con,campos);
	}
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria para la reserva de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public int confirmarReservaCitaEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		return SqlBaseOrdenesAmbulatoriasDao.confirmarReservaCitaEnOrdenAmbulatoria(con,campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 * Formato: urgente | observaciones | centro_atencion_solicita | usuario_solicita | especialidad_solicita | servicio | finalidad
	 */
	public String obtenerInfoServicioProcOrdenAmbulatoria(Connection con, String ordenAmbulatoria, int institucion)
	{
		return SqlBaseOrdenesAmbulatoriasDao.obtenerInfoServicioProcOrdenAmbulatoria(con,ordenAmbulatoria, institucion);
	}
	

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public int confirmarOrdenAmbulatoria(Connection con, HashMap campos)
	{
		return SqlBaseOrdenesAmbulatoriasDao.confirmarOrdenAmbulatoria(con,campos);
	}
	
	/**
	 * Método implementado para ingresar informacion de referencia de consulta
	 * externa a la orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public int ingresarInformacionReferenciaExterna(Connection con,HashMap campos)
	{
		return SqlBaseOrdenesAmbulatoriasDao.ingresarInformacionReferenciaExterna(con,campos);
	}
	

	/**
	 * 
	 */
	public HashMap consultarServiciosOrdenAmbulatoria(Connection con, String numeroOrden, int institucion)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarServiciosOrdenAmbulatoria(con,numeroOrden, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulosMap
	 * @param codigoOrden
	 * @return
	 */
	public boolean updateOrdenAmbulatoriaArticulos(Connection con, HashMap articulosMap, String codigoOrden)
	{
		return SqlBaseOrdenesAmbulatoriasDao.updateOrdenAmbulatoriaArticulos(con, articulosMap, codigoOrden);
	}
	
	/**
	 * 
	 */
	public String consultarCentroCostoSolicitante(Connection con, String codigoOA) throws SQLException
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarCentroCostoSolicitante(con, codigoOA);
	}
	
	/**
	 * Metodo para consultar los centros de costo asociados a las unidades de agenda del servicio
	 * @param criterios
	 * @return
	 */
	public HashMap consultaCentrosCostoXUnidadAgendaServ(Connection con, int codigoServicio)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultaCentrosCostoXUnidadAgendaServ(con, codigoServicio);
	}
	
	public HashMap consultarCodigoOrdenAmb(String consecutivo)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarCodigoOrdenAmb(consecutivo);
	}

	@Override
	public int otenerUltimaCuentaPacienteValidaParaOrden(Connection con,int codigoPersona) 
	{
		return SqlBaseOrdenesAmbulatoriasDao.otenerUltimaCuentaPacienteValidaParaOrden(con,codigoPersona);
	}
	

	@Override
	public DtoDiagnostico consultarDiagnosticoOrden(Connection con,int codigoOrden)
	{
		return SqlBaseOrdenesAmbulatoriasDao.consultarDiagnosticoOrden(con,codigoOrden);
	}

	@Override
	public DtoDiagnostico consultarDiagnosticoOrdenAmbulatoria(Connection con,
			int codigoOrdenAmbulatoria) {
		return SqlBaseOrdenesAmbulatoriasDao.consultarDiagnosticoOrdenAmbulatoria(con, codigoOrdenAmbulatoria);
	}

	/**
	 * Método que consulta los resultados asociados a las órdenes ambulatorias.
	 * 
	 * @param con
	 * @param numeroOrden
	 * 
	 * @return resultado
	 */
	@Override
	public String consultarResultadoOrdenesAmbulatorias(Connection con, String numeroOrden) {
		return SqlBaseOrdenesAmbulatoriasDao.consultarResultadoOrdenesAmbulatorias(con, numeroOrden, cadenaConsultaResultadoOrdenes);
	}

	/**
	 * @see OrdenesAmbulatoriasDao#detalleToolTipArticulosOrdenAmbulatoria(Connection, int, boolean)
	 */
	@Override
	public List<InfoArticuloOrdenAmbulatoriaDto> detalleToolTipArticulosOrdenAmbulatoria(Connection con, int codigoOrden, boolean esMedicamento) throws IPSException {	
		return SqlBaseOrdenesAmbulatoriasDao.consultarResultadoOrdenesAmbulatorias(con, codigoOrden, esMedicamento);
	}
	
	
}
