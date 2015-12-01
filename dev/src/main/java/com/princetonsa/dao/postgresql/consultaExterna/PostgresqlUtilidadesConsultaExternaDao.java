/*
 * Septiembre 26, 2007
 */
package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.UtilidadesConsultaExternaDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseUtilidadesConsultaExternaDao;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;


/**
 * 
 * @author sgomez
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en las utilidades del módulo de CONSULTA EXTERNA
 */
public class PostgresqlUtilidadesConsultaExternaDao implements UtilidadesConsultaExternaDao 
{
	
	/**
	 * Cadena que verifica si se deben mostrar otros servicios en la respuesta de citas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean deboMostrarOtrosServiciosCita(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesConsultaExternaDao.deboMostrarOtrosServiciosCita(con, campos);
	}
	
	/**
	 * Método que consulta los otros servicios que no estan en la cita pero que aplican a su unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> consultarOtrosServiciosCita(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesConsultaExternaDao.consultarOtrosServiciosCita(con, campos);
	}
	
	/**
	 * Método que consulta los centros de costo X unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarCentrosCostoXUnidadAgenda(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesConsultaExternaDao.consultarCentrosCostoXUnidadAgenda(con, campos);
	}
	
	/**
	 * Método que consulta los consultorios libres que no se encuentren en los horarios de atencion indicados por el 
	 * dia y la hora inicio y final
	 * @param Connection con
	 * @param String centroAtencion
	 * @param int diaSemana
	 * @param String horaInicio
	 * @param String horaFinal
	 * */
	public 	HashMap consultarConsultoriosLibresHorarioAtencion(Connection con,
			int centroAtencion,
			int diaSemana,
			String horaInicio,
			String horaFinal)
	{
		return SqlBaseUtilidadesConsultaExternaDao.consultarConsultoriosLibresHorarioAtencion(con,centroAtencion,diaSemana,horaInicio,horaFinal,"true");
	}
	
	/**
	 * Método implementado para obtener los codigos propietarios de los servicios asociados a una
	 * cita separados por coma
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerListadoCodigosServiciosCita (Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerListadoCodigosServiciosCita(con, campos);
	}
	
	/**
	 * Metodo encargado de consultar el codigo
	 * medico de la cita.
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public String obtenerCodigoMedicoCita (Connection connection,String numSol)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCodigoMedicoCita(connection, numSol);
	}

	/**
	 * 
	 */
	public int consultarCantidadMaximaCitasControlPostOperatorioConvenio(Connection con, int convenio) {
		
		return SqlBaseUtilidadesConsultaExternaDao.consultarCantidadMaximaCitasControlPostOperatorioConvenio(con, convenio);
	}
	/**
	 * 
	 */
	public int consultarDiasControlPostOperatorioConvenio(Connection con, int convenio) {
		
		return SqlBaseUtilidadesConsultaExternaDao.consultarDiasControlPostOperatorioConvenio(con,convenio);
	}

	public HashMap validarControlPostOperatorio(Connection con, int paciente, String fechaCita, int cantCPO, int diasCPO, int especialidad, Diagnostico diagnostico) {
		
		return SqlBaseUtilidadesConsultaExternaDao.validarControlPostOperatorio(con,paciente,fechaCita,cantCPO,diasCPO, especialidad, diagnostico);
	}
	
	/**
	 * Método implementado para obtener la fecha/hora de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String[] obtenerFechaHoraCita (Connection con,String codigoCita)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerFechaHoraCita(con, codigoCita);
	}
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public HashMap centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad)
	{
		return SqlBaseUtilidadesConsultaExternaDao.centrosAtencionValidosXUsuario(con, usuario, actividad);
	}
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return ArrayList<HashMap>
	 * @author Víctor Gómez
	 */
	public ArrayList<HashMap<String,Object>> centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad, String tipoAtencion)
	{
		return SqlBaseUtilidadesConsultaExternaDao.centrosAtencionValidosXUsuario(con, usuario, actividad, tipoAtencion);
	}
	
	/**
	 * Metodo implementado para obtener las unidades de agenda validas para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public HashMap unidadesAgendaXUsuario(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios)
	{
		return SqlBaseUtilidadesConsultaExternaDao.unidadesAgendaXUsuario(con, usuario, centroAtencion, actividad, tipoAtencion, codigosServicios);
	}
	
	/**
	 * M&eacute;todo implementado para obtener las unidades de agenda v&aacute;lidas para el usuario.
	 * Tambi&eacute;n se involucra en la consulta, si se envian los c&oacute;digos, los servicios 
	 * que deben estar asociados a las unidades de agenda.
	 * 
	 * @param con
	 * @param usuario
	 * @param centroAtencion
	 * @param actividad
	 * @param tipoAtencion
	 * @param codigosServicios
	 * @return ArrayList<HashMap>
	 * @author Jorge Armando Agudelo
	 */
	public ArrayList<HashMap> unidadesAgendaXUsuarioArray(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios)
	{
		return SqlBaseUtilidadesConsultaExternaDao.unidadesAgendaXUsuarioArray(con, usuario, centroAtencion, actividad, tipoAtencion, codigosServicios);
	}
	
	/**
	 * Método para obtener el codigo de la agenda de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String obtenerCodigoAgendaCita(Connection con,String codigoCita)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCodigoAgendaCita(con, codigoCita);
	}
	
	/**
	 * Método que verifica si el profesional tiene agenda generada
	 * @param con
	 * @param codigoProfesional
	 * @param tipoBD
	 * @return
	 */
	public boolean tieneProfesionalAgendaGenerada(Connection con,int codigoProfesional)
	{
		return SqlBaseUtilidadesConsultaExternaDao.tieneProfesionalAgendaGenerada(con, codigoProfesional, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Método que verifica si el paciente tiene citas con estado NO ASISTIÓ 
	 * que esten entre los días de restricción.
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean pacienteConInasistencia(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesConsultaExternaDao.pacienteConInasistencia(con, campos);
	}
	
	
/**
 * Metodo que verifica si una actividad asociada a una unidad de agenda está autorizada
 */
	public boolean esActividadAurtorizada(Connection con, int unidadAgenda,int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen) {
		
		return SqlBaseUtilidadesConsultaExternaDao.esActividadAurtorizada(con,unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
	}
	
	/**
	 * 
	 */
	public ArrayList<HashMap<String, Object>> consultaEstadosCita(Connection connection,
			ArrayList<Integer> codigosEstados) {
		return SqlBaseUtilidadesConsultaExternaDao.consultaEstadosCita(connection, codigosEstados);
	}

	@Override
	/**
	 *  Metodo que retorna los motivos de Anulacion y/o Condonacion de Multas que estan activos en una institucion
	 */
	public HashMap consultaMotivosAnulacionCondonacionMulta(Connection con,int codInstitucion)
	{
		
		return SqlBaseUtilidadesConsultaExternaDao.consultaMotivosAnulacionCondonacionMulta(con, codInstitucion);
	}
	
	/**
	 * Verifica si la Institucion Maneja Multas poerImcumplimiento de Citas Según la Institucion y el Modulo
	 * que para este caso es Consulta Externa
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 * 
	 */
	public ArrayList<HashMap<String, Object>> institucionManejaMultasIncumCitas(Connection con, HashMap parametros){
		return SqlBaseUtilidadesConsultaExternaDao.institucionManejaMultasIncumCitas(con, parametros);
	}
	
	/**
	 * Verifica el estado de las citas del paciente
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 */
	public ArrayList<HashMap<String, Object>> estadoCitasPaciente(Connection con, HashMap parametros){
		return SqlBaseUtilidadesConsultaExternaDao.estadoCitasPaciente(con, parametros);
	}
	@Override
	public HashMap UnidadesAgendaXcentrosAtencion(Connection con,
			int conscutivoCentroAtencion, String tipoAtencion, Boolean activas) {
		
		return  SqlBaseUtilidadesConsultaExternaDao.UnidadesAgendaXcentrosAtencion(con,conscutivoCentroAtencion, tipoAtencion, activas);
	}

	@Override
	public HashMap validacionesBloqueoAtencionCitas(Connection con,UsuarioBasico usuario,int codigoPacientes) {
		
		return SqlBaseUtilidadesConsultaExternaDao.validacionesBloqueoAtencionCitas(con, usuario,codigoPacientes);
	}
	
	public ArrayList<Integer> consultarSolicitudesSinFacturar (Connection con, int codigoCita)
	{
		return SqlBaseUtilidadesConsultaExternaDao.consultarSolicitudesSinFacturar(con, codigoCita);
	}
	
	/**
	 * consulta los consutorios 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public ArrayList<HashMap> consultoriosCentroAtencionTipo(Connection con, int institucion, int centroAtencion)
	{
		return SqlBaseUtilidadesConsultaExternaDao.consultoriosCentroAtencionTipo(con, institucion, centroAtencion);
	}
	
	/**
	 * Método para obtener el codigo medico de la agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public int obtenerCodigoMedicoAgenda(Connection con,int codigoAgenda)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCodigoMedicoAgenda(con, codigoAgenda);
	}
	
	/**
	 * metodo que obtiene las actividades autorizadas por una unidad de agenda
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param usuario
	 * @param institucion
	 * @return ArrayList<HashMap>
	 * @author Víctor Hugo Gómez L.
	 */
	public ArrayList<HashMap> actividadesAutorizadasXUniAgend(Connection con, int centroAtencion, int unidadAgenda, String usuario, int institucion)
	{
		return SqlBaseUtilidadesConsultaExternaDao.actividadesAutorizadasXUniAgend(con, centroAtencion, unidadAgenda, usuario, institucion);
	}
	
	/**
	 * obtener los motivos cancelacion de un cita
	 * @param con
	 * @param activo
	 * @param tipoCancelacion
	 * @return
	 */
	public ArrayList<HashMap> obtenerMotivosCancelacion(Connection con, String activo, String tipoCancelacion)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerMotivosCancelacion(con, activo, tipoCancelacion);
	}
	
	/**
	 * Método implementado para verificar 
	 * @param con
	 * @param usuario
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public boolean validarCentrosCostoUsuarioEnUnidadAgenda(Connection con,UsuarioBasico usuario, int codigoUnidadAgenda)
	{
		return SqlBaseUtilidadesConsultaExternaDao.validarCentrosCostoUsuarioEnUnidadAgenda(con, usuario, codigoUnidadAgenda);
	}

	@Override
	public ArrayList<DtoConsultorios> consultoriosXcentrosAtencion(
			Connection con, int conscutivoCentroAtencion) {
		
		
		String sentencia=" SELECT Distinct descripcion as descripcion ,"+
		" codigo as codigo "+
		"FROM consultaexterna.consultorios where centro_atencion= ? and activo='"+ConstantesBD.acronimoTrueCorto+"'";
		
		return SqlBaseUtilidadesConsultaExternaDao.consultoriosXcentrosAtencion(con,conscutivoCentroAtencion,sentencia);
	}
	
	/**
	 * Método para obtener los paises permitidos para un usuario según la parametrizacion de la funcinalidad
	 * unidade sde agenda por susuario por centro de atencion
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	@Override
	public ArrayList<HashMap<String, Object>> obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(Connection con,String loginUsuario,String tipoAtencion)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(con, loginUsuario,tipoAtencion);
	}
	
	/**
	 * Método implementado para cargar las ciudades permitidas para un usuario según la parametrizacion
	 * de unidades de agenda x usuario x centro de atencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	@Override
	public ArrayList<HashMap<String, Object>> obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(con, parametros);
	}
	
	/**
	 * Método para obtener los centros de atencion permitios x usuario según la parametrizacion
	 * de unidades de agenda x usuario x centro de atencionn filtrando por ciudad
	 * @param con
	 * @param parametros
	 * @return
	 */
	@Override
	public ArrayList<HashMap<String, Object>> obtenerCentrosAtencionPermitidosUsuarioXCiudad(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCentrosAtencionPermitidosUsuarioXCiudad(con, parametros);
	}
	
	/**
	 * 
	 */
	public int obtenerCodigoMedicoAgendaXCita(Connection con,int codigoCita)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCodigoMedicoAgendaXCita(con,codigoCita);
	}
	
	/**
	 * 
	 */
	public int obtenerCodigoMedicoRespondeSolicitud(Connection con, int nroSolicitud)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerCodigoMedicoRespondeSolicitud(con,nroSolicitud);
	}
	
	/**
	 * 
	 */
	public int obtenerSolicitudXCita(Connection con, int nroCita)
	{
		return SqlBaseUtilidadesConsultaExternaDao.obtenerSolicitudXCita(con, nroCita);
	}

	@Override
	public boolean esActividadAurtorizadaProgramacionCitaOdo(Connection con, int cita, int actividad, String usuario, int centroAtencion){
		return SqlBaseUtilidadesConsultaExternaDao.esActividadAurtorizadaProgramacionCitaOdo(con, cita, actividad, usuario, centroAtencion);
	}

	@Override
	public String esReservaOrdenAmbulatoria(Connection con, int codigoCita) 
	{
		return SqlBaseUtilidadesConsultaExternaDao.esReservaOrdenAmbulatoria(con, codigoCita);
	}

	@Override
	public boolean esActividadAurtorizadaOServAddProf(Connection con,int codigoCita, int unidadAgenda, int actividad, String usuario,int centroAtencion, boolean isUniAgenOrAgen) {
	
		return SqlBaseUtilidadesConsultaExternaDao.esActividadAurtorizadaOServAddProf(con, codigoCita, unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
	}
	

	@Override
	public ArrayList<DtoUnidadesConsulta> obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(
			Connection con, String tipo, int codigoEspecialidad,
			int codigoCentroAtencion, boolean filtrarActivas) {
		return SqlBaseUtilidadesConsultaExternaDao.obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(con, tipo, codigoEspecialidad, codigoCentroAtencion, filtrarActivas);
	}
	
	@Override
	public boolean existeAgendaXCentroCostoXServicio(Connection con, int codigoCentroCosto, int codigoServicio, String fecha) {
		return SqlBaseUtilidadesConsultaExternaDao.existeAgendaXCentroCostoXServicio(con, codigoCentroCosto, codigoServicio, fecha);
	}
}
