/*
 * Septiembre 26, 2007
 */
package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;

/**
 * 
 * @author sgomez
 *	Interface usada para gestionar los m�todos de acceso a la base
 * de datos en utilidades del modulo de CONSULTA EXTERNA
 */
public interface UtilidadesConsultaExternaDao 
{
	/**
	 * Cadena que verifica si se deben mostrar otros servicios en la respuesta de citas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean deboMostrarOtrosServiciosCita(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los otros servicios que no estan en la cita pero que aplican a su unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> consultarOtrosServiciosCita(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los centros de costo X unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarCentrosCostoXUnidadAgenda(Connection con,HashMap campos);
	
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public int consultarCantidadMaximaCitasControlPostOperatorioConvenio(Connection con, int convenio);
	
	public int consultarDiasControlPostOperatorioConvenio(Connection con, int convenio);
	
	public HashMap validarControlPostOperatorio(Connection con, int paciente, String fechaCita, int cantCPO, int diasCPO, int especialidad, Diagnostico diagnostico);
	
	
	/**
	 * M�todo que consulta los consultorios libres que no se encuentren en los horarios de atencion indicados por el 
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
			String horaFinal);
	
	/**
	 * M�todo implementado para obtener los codigos propietarios de los servicios asociados a una
	 * cita separados por coma
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerListadoCodigosServiciosCita (Connection con,HashMap campos);
	
	/**
	 * Metodo encargado de consultar el codigo
	 * medico de la cita.
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public String obtenerCodigoMedicoCita (Connection connection,String numSol);
	
	/**
	 * M�todo implementado para obtener la fecha/hora de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String[] obtenerFechaHoraCita (Connection con,String codigoCita);
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public HashMap centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad);
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return ArrayList<HashMap>
	 * @author V�ctor G�mez
	 */
	public ArrayList<HashMap<String,Object>> centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad, String tipoAtencion);
	
	/**
	 * Metodo implementado para obtener las unidades de agenda validas para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public HashMap unidadesAgendaXUsuario(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigoServicio);
	
	
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
	public ArrayList<HashMap> unidadesAgendaXUsuarioArray(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios);
	
	/**
	 * M�todo para obtener el codigo de la agenda de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String obtenerCodigoAgendaCita(Connection con,String codigoCita);
	
	/**
	 * M�todo que verifica si el profesional tiene agenda generada
	 * @param con
	 * @param codigoProfesional
	 * @param tipoBD
	 * @return
	 */
	public boolean tieneProfesionalAgendaGenerada(Connection con,int codigoProfesional);
	
	/**
	 * M�todo que verifica si el paciente tiene citas con estado NO ASISTI� 
	 * que esten entre los d�as de restricci�n.
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean pacienteConInasistencia(Connection con,HashMap campos);

	/**
	 * Metodo que verifica si una actividad asociada a una unidad de agenda est� autorizada
	 * @param con
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public boolean esActividadAurtorizada(Connection con, int unidadAgenda,int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen);

	/**
	 * 
	 * @param connection
	 * @param codigosEstados
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultaEstadosCita(Connection connection,
			ArrayList<Integer> codigosEstados);
	
	/**
	 * Metodo que retorna los motivos de Anulacion y/o Condonacion de Multas que estan activos en una institucion
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public HashMap consultaMotivosAnulacionCondonacionMulta(Connection con, int codInstitucion);
	
	/**
	 * Verifica si la Institucion Maneja Multas poerImcumplimiento de Citas Seg�n la Institucion y el Modulo
	 * que para este caso es Consulta Externa
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 * 
	 */
	public ArrayList<HashMap<String, Object>> institucionManejaMultasIncumCitas(Connection con, HashMap parametros);
	
	/**
	 * Verifica el estado de las citas del paciente
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 */
	public ArrayList<HashMap<String, Object>> estadoCitasPaciente(Connection con, HashMap parametros);
	
	
	
	public HashMap UnidadesAgendaXcentrosAtencion(Connection con,
			int conscutivoCentroAtencion, String tipoAtencion, Boolean activas);
	
	public ArrayList<DtoConsultorios> consultoriosXcentrosAtencion(Connection con,
			int conscutivoCentroAtencion);

	public HashMap validacionesBloqueoAtencionCitas(Connection con, UsuarioBasico usuario, int codigoPacientes);
	
	public ArrayList<Integer> consultarSolicitudesSinFacturar (Connection con, int codigoCita);
	
	/**
	 * consulta los consutorios 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public ArrayList<HashMap> consultoriosCentroAtencionTipo(Connection con, int institucion, int centroAtencion);
	
	/**
	 * M�todo para obtener el codigo medico de la agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public int obtenerCodigoMedicoAgenda(Connection con,int codigoAgenda);
	
	/**
	 * metodo que obtiene las actividades autorizadas por una unidad de agenda
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param usuario
	 * @param institucion
	 * @return ArrayList<HashMap>
	 * @author V�ctor Hugo G�mez L.
	 */
	public ArrayList<HashMap> actividadesAutorizadasXUniAgend(Connection con, int centroAtencion, int unidadAgenda, String usuario, int institucion);
	
	/**
	 * obtener los motivos cancelacion de un cita
	 * @param con
	 * @param activo
	 * @param tipoCancelacion
	 * @return
	 */
	public ArrayList<HashMap> obtenerMotivosCancelacion(Connection con, String activo, String tipoCancelacion);
	
	/**
	 * M�todo implementado para verificar 
	 * @param con
	 * @param usuario
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public boolean validarCentrosCostoUsuarioEnUnidadAgenda(Connection con,UsuarioBasico usuario, int codigoUnidadAgenda);
	
	/**
	 * M�todo para obtener los paises permitidos para un usuario seg�n la parametrizacion de la funcinalidad
	 * unidade sde agenda por susuario por centro de atencion
	 * @param con
	 * @param loginUsuario
	 * @param tipoAtencion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(Connection con,String loginUsuario,String tipoAtencion);
	
	/**
	 * M�todo implementado para cargar las ciudades permitidas para un usuario seg�n la parametrizacion
	 * de unidades de agenda x usuario x centro de atencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(Connection con,HashMap parametros);
	
	/**
	 * M�todo para obtener los centros de atencion permitios x usuario seg�n la parametrizacion
	 * de unidades de agenda x usuario x centro de atencionn filtrando por ciudad
	 * @param con
	 * @param parametros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosAtencionPermitidosUsuarioXCiudad(Connection con,HashMap parametros);
	
	/**
	 * 
	 */
	public int obtenerCodigoMedicoAgendaXCita(Connection con,int codigoCita);
	
	/**
	 * 
	 */
	public int obtenerCodigoMedicoRespondeSolicitud(Connection con, int nroSolicitud);
	
	/**
	 * 
	 */
	public int obtenerSolicitudXCita(Connection con, int nroCita);

	/**
	 * M�todo que verifica las actividades autorizadas para el usuario al listar las citas odontol�gicas programadas
	 * @param con Comexi�n con la bD
	 * @param cita C�digo de la cita
	 * @param actividad Actividad a avalidar
	 * @param usuario Usuario que realiza la consulta
	 * @param centroAtencion Centro de atenci�n del usuario
	 * @return true en caso de tener autorizada la actividad
	 */
	public boolean esActividadAurtorizadaProgramacionCitaOdo(Connection con, int cita, int actividad, String usuario, int centroAtencion);

	/**
	 * 
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String esReservaOrdenAmbulatoria(Connection con, int codigoCita);

	
	/**
	 * M�todo que valida si una actividad asociada a una unidad de agenda un usuario y un centro de atencion est� autorizada
	 * o la Cita Tiene un servicio que est� se encuentre entre los Servicios adicionales del Profesional 
	 * @param con
	 * @param codigoCita
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @param isUniAgenOrAgen
	 * @return
	 */
	public boolean esActividadAurtorizadaOServAddProf(Connection con,int codigoCita, int unidadAgenda, int actividad, String usuario,int centroAtencion, boolean isUniAgenOrAgen);

	/**
	 * 
	 * @param con
	 * @param tipo
	 * @param codigoEspecialidad
	 * @param codigoCentroAtencion
	 * @param filtrarActivas
	 * @return
	 */
	public ArrayList<DtoUnidadesConsulta> obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(
			Connection con, String tipo, int codigoEspecialidad,
			int codigoCentroAtencion, boolean filtrarActivas);
	
	/**
	 * M�todo que permite verificar si existe agenda creada para un centro de costo y las unidades de agenda
	 * asociadas a un servicio para una fecha mayor o igual o la fecha pasada por par�metro
	 * @param codigoCentroCostro
	 * @param codigoServicio
	 * @param fecha
	 * @return
	 */
	public boolean existeAgendaXCentroCostoXServicio(Connection con, int codigoCentroCosto, int codigoServicio, String fecha);
}
