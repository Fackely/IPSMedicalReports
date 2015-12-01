package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.InfoDatosString;
import util.odontologia.InfoServicios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.CitaOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseCitaOdontologicaDao;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;

/**
 * 
 * @author Víctor Hugo Gómez L. 
 *
 */

public class PostgresqlCitaOdontologicaDao  implements CitaOdontologicaDao
{
	/**
	 * se obtiene un listado de los servicios de cada cita odontologica
	 * 
	 * @param con
	 * @param codigoCita
	 * @param institucion
	 * @param codigoPaciente
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public ArrayList<DtoServicioCitaOdontologica> consultarServiciosCitaOdontologica(Connection con,int codigoCita,int institucion, int codigoPaciente)
	{
		return SqlBaseCitaOdontologicaDao.consultarServiciosCitaOdontologica(con, codigoCita,institucion, false, codigoPaciente);
	}

	/**
	 * se obtiene un listado de las citas odontologicas x una agenda especifica
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXAgenda(Connection con,HashMap parametros, HashMap<String, Integer> cuposDisponibles)
	{
		return SqlBaseCitaOdontologicaDao.consultarCitaOdontologicaXAgenda(con, parametros);
	}
	
	/**
	 * se obtiene un listado de las citas odontologicas x un paciente especifico
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXPaciente(Connection con,HashMap parametros)
	{
		return SqlBaseCitaOdontologicaDao.consultarCitaOdontologicaXPaciente(con, parametros);
	}
	
	/**
	 * metodo que inserta una cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.insertarCitaOdontologica(con, dto);
	}
	
	/**
	 * actualizar srvicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarServicioCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.actualizarServicioCitaOdontologica(con, dto);
	}
	
	/**
	 * metodo que consulta una cita especifica
	 * @param con
	 * @param codigo_cita
	 * @return
	 */
	public DtoCitaOdontologica obtenerCitaOdontologica(Connection con, int codigo_cita, int codInstitucion)
	{
		return SqlBaseCitaOdontologicaDao.obtenerCitaOdontologica(con, codigo_cita, codInstitucion);
	}
	
	/**
	 * insert de log de citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertLogCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.insertLogCitaOdontologica(con, dto);
	}
	
	/**
	 * metodo que inserta el log servicios cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertLogServiciosCitaOdon (Connection con, DtoServicioCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.insertLogServiciosCitaOdon(con, dto);
	}
	
	/**
	 * metodo que inactiva el servicio de una cita odontologica
	 * @param con
	 * @param usuario
	 * @param codigoPk
	 * @return
	 */
	public boolean inactivarServicioCitaOdontologica(Connection con, String usuario, int codigoPk)
	{
		return SqlBaseCitaOdontologicaDao.inactivarServicioCitaOdontologica(con, usuario, codigoPk);
	}
	
	/**
	 * insert servicios citas odontologicas
	 * @param con
	 * @param dto 
	 * @return
	 */
	public int insertarServiciosCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.insertarServiciosCitaOdontologica(con, dto);
	}
	
	/**
	 * actualizar cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return SqlBaseCitaOdontologicaDao.actualizarCitaOdontologica(con, dto);
	}
	
	/**
	 * metodo que vverifica si al menos un servicios asociado a una cita esta facturado
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public boolean almenosUnServicioFacturado(Connection con, int codigoCita)
	{
		return SqlBaseCitaOdontologicaDao.almenosUnServicioFacturado(con, codigoCita);
	}
	
	/**
	 * Metodo q retorna arraylist de citas segun parametros de busqueda funcionaluidad consulta externa/consultar citas
	 * @param connection
	 * @param codigoPaciente
	 * @param fechaHoraInicial
	 * @param fechaHorafinal
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param estadosCita
	 * @param consultorio
	 * @param tipoCita
	 * @param profesionalSalud
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultaCitaOdontologicaConsExt (Connection connection,int codigoPaciente, String fechaHoraInicial, 
			String fechaHorafinal, String centroAtencion, int unidadConsulta, String[] estadosCita, int consultorio, String tipoCita, 
			int profesionalSalud, int institucion){
		return SqlBaseCitaOdontologicaDao.consultaCitaOdontologicaConsExt(connection, codigoPaciente, fechaHoraInicial, fechaHorafinal, centroAtencion, unidadConsulta, estadosCita, consultorio, tipoCita, profesionalSalud, DaoFactory.POSTGRESQL,institucion);
	}
	
	/**
	 * Método q consulta el programa, servicio, garantia y estado en el plande tratamiento
	 * @param connection
	 * @param institucion
	 * @param cita
	 * @return
	 */
	public ArrayList<DtoProgramasServiciosPlanT> consultarDetalleCitaConsExterna(Connection connection, int institucion, int cita){
		return SqlBaseCitaOdontologicaDao.consultarDetalleCitaConsExterna(connection, institucion, cita);
	}
	
	/**
	 * metodo que obtiene los datos informativos del centro de atención
	 * @param consecutivoCenAten
	 * @return
	 */
	public InfoDatosString obtenerInfoCentroAtencion(int consecutivoCenAten)
	{
		return SqlBaseCitaOdontologicaDao.obtenerInfoCentroAtencion(consecutivoCenAten);
	}
	
	/**
	 * Método para cargar la informacion de la cita asociada a un servicio de un plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoCitaOdontologica obtenerCitaOdontologicaXProgServPlanT(Connection con,BigDecimal codigoPkProgServ,int codigoInstitucion)
	{
		return SqlBaseCitaOdontologicaDao.obtenerCitaOdontologicaXProgServPlanT(con, codigoPkProgServ, codigoInstitucion);
	}

	@Override
	public int insertarProximaCitaOdontologia(Connection con,ArrayList<InfoServicios> arrayServicios, String fechaCita,int codigoPaciente, String loginUsuario, String tipoCita, int codigoEvolucion, int centroCosto, int totalDuracionCita) {
		
		return SqlBaseCitaOdontologicaDao.insertarProximaCitaOdontologia(con, arrayServicios, fechaCita, codigoPaciente, loginUsuario, tipoCita, codigoEvolucion, centroCosto, totalDuracionCita);
	}

	@Override
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXRango(Connection con, HashMap parametrosBusqueda, int centroAtencionUsuario) {
		String ultimoPlanTratamiento="SELECT pt.estado FROM odontologia.plan_tratamiento pt WHERE pt.codigo_paciente = co.codigo_paciente ORDER BY codigo_pk DESC LIMIT 1";
		return SqlBaseCitaOdontologicaDao.consultarCitaOdontologicaXRango(con, parametrosBusqueda, centroAtencionUsuario, ultimoPlanTratamiento);
	}

	@Override
	public boolean actualizarCitaOdontologicaXReprogramacion(Connection con,int codigoCita, int codAgenda, String fechaCita, String horaInicio,	String horaFin, int duracion, String usuarioModifica, String tipoCita, ArrayList<DtoServicioCitaOdontologica> arrayServicios) {
		
		return SqlBaseCitaOdontologicaDao.actualizarCitaOdontologicaXReprogramacion(con, codigoCita, codAgenda, fechaCita, horaInicio, horaFin, duracion, usuarioModifica, tipoCita,arrayServicios);
	}

	@Override
	public boolean actualizarCitaOdontologicaXReserva(Connection con,DtoCitaOdontologica dto) {
		
		return SqlBaseCitaOdontologicaDao.actualizarCitaOdontologicaXReserva(con, dto);
	}

	@Override
	public boolean validarCuposSiguientesDisponibles(DtoAgendaOdontologica dto) {
		return SqlBaseCitaOdontologicaDao.validarCuposSiguientesDisponibles(dto);
	}
	@Override
	public boolean asignarSolicitudServicioCitaOdo(Connection con,
			DtoServicioCitaOdontologica servicioCita) {
		return SqlBaseCitaOdontologicaDao.asignarSolicitudServicioCitaOdo(con, servicioCita);
	}

	@Override
	public boolean actualizarCitaOdontologicaYServicios(Connection con, DtoCitaOdontologica dto, boolean esCambioServicio) {
		return SqlBaseCitaOdontologicaDao.actualizarCitaOdontologicaYServicios(con, dto, esCambioServicio);
	}

	@Override
	public ArrayList<DtoCitaOdontologica> consultarProxCitasPacienteXCancelacion(
			int codigoPaciente, double codigoPrograma, int ordenServicio,int codigoInstitucionInt, String fechaCita, String horaFinCita,String loginUsuario) {
		
		return SqlBaseCitaOdontologicaDao.consultarProxCitasPacienteXCancelacion(codigoPaciente, codigoPrograma, ordenServicio, codigoInstitucionInt, fechaCita, horaFinCita, loginUsuario);
	}

	@Override
	public boolean cambiarEstadoCita(Connection con, BigDecimal codigo,	String estado, String loginUsuario) 
	{
		return SqlBaseCitaOdontologicaDao.cambiarEstadoCita(con, codigo, estado, loginUsuario);
	}
	
//	@Override
//	public BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA,ArrayList<String> estado) 
//	{
//		return SqlBaseCitaOdontologicaDao.obtenerCodigoPkUltimaCitaProgServPlanT(codigoPkProgServPlanT, fechaFormatoAppNOREQUERIDA,estado);
//	}
	
	

	@Override
	public BigDecimal obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion(
			DtoPlanTratamientoOdo dto) {
		
		return SqlBaseCitaOdontologicaDao.obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion(dto);
	}

	@Override
	public DtoCitaOdontologica cargarCitaXEvolucionPlanTratamiento(
			DtoPlanTratamientoOdo dto) {
	
		return SqlBaseCitaOdontologicaDao.cargarCitaXEvolucionPlanTratamiento(dto);
	}

	@Override
	public boolean esCitaConfirmada(int codigoCita) 
	{
		return SqlBaseCitaOdontologicaDao.esCitaConfirmada(codigoCita);
	}

	@Override
	public boolean estaCitaEnAtencion(int codigoPkCita, Connection con) {
		return SqlBaseCitaOdontologicaDao.estaCitaEnAtencion(codigoPkCita, con);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.CitaOdontologicaDao#actualizarProgramaHallazgoPiezaServicioCita(java.sql.Connection, long, long)
	 */
	@Override
	public boolean actualizarProgramaHallazgoPiezaServicioCita(Connection con, long programaHallazgoPieza, long codigoPk) {
	
		return SqlBaseCitaOdontologicaDao.actualizarProgramaHallazgoPiezaServicioCita(con, programaHallazgoPieza, codigoPk);
	}
	
	@Override
	public ArrayList<DtoCitaOdontologica> obtenerHistoricoEstadosCita(Connection con, int codigoPk){
		return SqlBaseCitaOdontologicaDao.obtenerHistoricoEstadosCita(con, codigoPk);
	}
	
	@Override
	public boolean actualizarTipoCancelacionCita (Connection con, String tipoCancelacion, long codigoCita){
		return SqlBaseCitaOdontologicaDao.actualizarTipoCancelacionCita(con, tipoCancelacion, codigoCita);
	}
	
	@Override
	public List<Integer> consultarCitaAsociadaProgramada(Connection con, int codigoCitaProgramada){
		return SqlBaseCitaOdontologicaDao.consultarCitaAsociadaProgramada(con, codigoCitaProgramada);
	}
}
