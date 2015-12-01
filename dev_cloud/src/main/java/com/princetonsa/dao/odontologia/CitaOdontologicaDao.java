package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.InfoDatosString;
import util.odontologia.InfoServicios;

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

public interface CitaOdontologicaDao 
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
	public ArrayList<DtoServicioCitaOdontologica> consultarServiciosCitaOdontologica(Connection con,int codigoCita,int institucion, int codigoPaciente);
	
	/**
	 * se obtiene un listado de las citas odontologicas x una agenda especifica
	 * @param con
	 * @param cuposDisponibles 
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXAgenda(Connection con,HashMap parametros, HashMap<String, Integer> cuposDisponibles);
	
	/**
	 * se obtiene un listado de las citas odontologicas x un paciente especifico
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXPaciente(Connection con,HashMap parametros);
	
	/**
	 * metodo que inserta una cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarCitaOdontologica(Connection con, DtoCitaOdontologica dto);
	
	/**
	 * actualizar srvicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarServicioCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto);
	
	/**
	 * metodo que consulta una cita especifica
	 * @param con
	 * @param codigo_cita
	 * @return
	 */
	public DtoCitaOdontologica obtenerCitaOdontologica(Connection con, int codigo_cita, int codInstitucion);
	
	/**
	 * insert de log de citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertLogCitaOdontologica(Connection con, DtoCitaOdontologica dto);
	
	/**
	 * metodo que inserta el log servicios cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertLogServiciosCitaOdon (Connection con, DtoServicioCitaOdontologica dto);
	
	/**
	 * metodo que inactiva el servicio de una cita odontologica
	 * @param con
	 * @param usuario
	 * @param codigoPk
	 * @return
	 */
	public boolean inactivarServicioCitaOdontologica(Connection con, String usuario, int codigoPk);
	
	/**
	 * insert servicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarServiciosCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto);
	
	/**
	 * actualizar cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarCitaOdontologica(Connection con, DtoCitaOdontologica dto);

	/**
	 * actualizar cita odontologica
	 * y sus servicios
	 * @param con
	 * @param dto
	 * @param esCambioServicio Indica si es cambio de servicio
	 * @return
	 */
	public boolean actualizarCitaOdontologicaYServicios(Connection con, DtoCitaOdontologica dto, boolean esCambioServicio);

	/**
	 * metodo que vverifica si al menos un servicios asociado a una cita esta facturado
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public boolean almenosUnServicioFacturado(Connection con, int codigoCita);
	
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
	 * @param institucion 
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultaCitaOdontologicaConsExt (Connection connection,int codigoPaciente, String fechaHoraInicial, 
			String fechaHorafinal, String centroAtencion, int unidadConsulta, String[] estadosCita, int consultorio, String tipoCita, 
			int profesionalSalud, int institucion);
	
	/**
	 * Método q consulta el programa, servicio, garantia y estado en el plande tratamiento
	 * @param connection
	 * @param institucion
	 * @param cita
	 * @return
	 */
	public ArrayList<DtoProgramasServiciosPlanT> consultarDetalleCitaConsExterna(Connection connection, int institucion, int cita);
	
	/**
	 * metodo que obtiene los datos informativos del centro de atención
	 * @param consecutivoCenAten
	 * @return
	 */
	public InfoDatosString obtenerInfoCentroAtencion(int consecutivoCenAten);
	
	/**
	 * Método para cargar la informacion de la cita asociada a un servicio de un plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoCitaOdontologica obtenerCitaOdontologicaXProgServPlanT(Connection con,BigDecimal codigoPkProgServ,int codigoInstitucion);

	
	/**
	 * Metodo para insertar los servicios a la pr&oacute;xima cita
	 * @param con
	 * @param arrayServicios
	 * @param centroCosto
	 * @param contrato 
	 * @return
	 */
	public int insertarProximaCitaOdontologia(Connection con,ArrayList<InfoServicios> arrayServicios, String fechaCita, int codigoPaciente, String loginUsuario, String tipoCita, int codigoEvolucion, int centroCosto, int totalDuracionCita);

	/**
	 * Metodo para insertar una proxima cita odontologica 
	 * @param con
	 * @param parametrosBusqueda
	 * @param centroAtencionUsuario TODO
	 * @param usuario
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXRango(Connection con, HashMap parametrosBusqueda, int centroAtencionUsuario);

	
	/**
	 * Método par actualizar una cita odontologica por Reprogramacion
	 * @param con
	 * @param codigoCita
	 * @param codAgenda
	 * @param fechaCita
	 * @param horaInicio
	 * @param horaFin
	 * @param duracion
	 * @param usuarioModifica
	 * @return
	 */
	public boolean actualizarCitaOdontologicaXReprogramacion(Connection con,int codigoCita, int codAgenda, String fechaCita, String horaInicio,String horaFin, int duracion, String usuarioModifica,String tipoCita, ArrayList<DtoServicioCitaOdontologica> arrayServicios);

	/**
	 * Metodo para actualizar Cita Odontologica por Reserva
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarCitaOdontologicaXReserva(Connection con,DtoCitaOdontologica dto);

	/**
	 * Método que verifica si existen cupos disponibles en los espacios de tiempo continuos
	 * en caso de que en la agenda se selecciones servicios conduración mayor a la
	 * asignada inicialmente.  
	 * @param con
	 * @param codigoAge
	 * @param horaInicio
	 * @param horaFinal
	 * @param return true en caso de poder asignar el nuevo cupo
	 */
	public boolean validarCuposSiguientesDisponibles(DtoAgendaOdontologica dto);

	/**
	 * Método que asocia el sevicio de la cita con la solicitud
	 * @param con Conexi{on con la BD
	 * @param servicioCita <code>DtoServicioCitaOdontologica</code> dto con los datos necesarios para el cambio (codigo_pk, numero solicitud)
	 * @return true en caso de ser exitoso, false de lo contrario
	 */
	public boolean asignarSolicitudServicioCitaOdo(Connection con, DtoServicioCitaOdontologica servicioCita);

	
	/**
	 * 
	 * @param codigo
	 * @param codigoPrograma
	 * @param ordenServicio
	 * @param codigoInstitucionInt
	 * @param fecha
	 * @param horaFinal
	 * @param loginUsuario
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultarProxCitasPacienteXCancelacion(int codigo, double codigoPrograma, int ordenServicio,	int codigoInstitucionInt, String fecha, String horaFinal,String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public boolean cambiarEstadoCita(Connection con, BigDecimal codigo,	String estado, String loginUsuario);

//	/**
//	 * 
//	 * Metodo para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
//	 * @param codigoPkProgServ
//	 * @param fechaFormatoAppNOREQUERIDA
//	 * @return
//	 * @author   Wilson Rios
//	 * @version  1.0.0 
//	 * @param estado 
//	 * @see
//	 */
//	public BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado);
//	
//	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  BigDecimal obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion(DtoPlanTratamientoOdo dto); 
	
	
	/**
	 * METODO QUE CONSULTA LA CITA CON RESPECTO AL LA EVOLUCION
	 * NOTA SE ENVIA CON DTO PLAN DE TRATAMIENTO PARA LA ESCALABILIDAD DE LA CONSULTA 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  DtoCitaOdontologica cargarCitaXEvolucionPlanTratamiento(DtoPlanTratamientoOdo dto) ;
	
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param codigoCita
	 * @return
	 */
	public boolean esCitaConfirmada(int codigoCita );

	/**
	 * Verifica si las citas están siendo atendidas
	 * @param codigoPkCita codigo_pk (Llave primaria) de la cita
	 * @param con Conexión con la BD
	 * @return true en caso de que la cita esté siendo atendida, false de lo contrario
	 */
	public boolean estaCitaEnAtencion(int codigoPkCita, Connection con);
	
	
	/**
	 * Método que actualiza el programa Hallazgo pieza de un servicio asociado 
	 * a una cita odontológica
	 * 
	 * @param con
	 * @param programaHallazgoPieza
	 * @param codigoCita
	 * @return
	 */
	public boolean actualizarProgramaHallazgoPiezaServicioCita(Connection con, long programaHallazgoPieza, long codigoPk);
	
	/**
	 * Este mètodo se encarga de consultar el histórico de estados
	 * de una cita odontológica. 
	 *
	 * @param con
	 * @param codigoPk
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoCitaOdontologica> obtenerHistoricoEstadosCita(Connection con, int codigoPk);
	
	/**
	 * Este mètodo se encarga de actualizar el tipo de cancelación de la cita.
	 *
	 * @param tipoCancelacion
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean actualizarTipoCancelacionCita (Connection con, String tipoCancelacion, long codigoCita);
	
	/**
	 * Permite consultar la cita asociada a una cita programada
	 * @param con
	 * @param codigoCitaProgramada
	 * @return
	 */
	public List<Integer> consultarCitaAsociadaProgramada(Connection con, int codigoCitaProgramada);
}
