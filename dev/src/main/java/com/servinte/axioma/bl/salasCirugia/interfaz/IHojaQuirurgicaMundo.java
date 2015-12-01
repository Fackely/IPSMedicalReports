/**
 * 
 */
package com.servinte.axioma.bl.salasCirugia.interfaz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.salascirugia.CargoSolicitudDto;
import com.servinte.axioma.dto.salascirugia.DestinoPacienteDto;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.FinalidadDto;
import com.servinte.axioma.dto.salascirugia.HojaAnestesiaDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxEspecialidadDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.dto.salascirugia.ProgramacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.SalaCirugiaDto;
import com.servinte.axioma.dto.salascirugia.ServicioHQxDto;
import com.servinte.axioma.dto.salascirugia.SolicitudCirugiaDto;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.dto.salascirugia.TipoHeridaDto;
import com.servinte.axioma.dto.salascirugia.TipoProfesionalDto;
import com.servinte.axioma.dto.salascirugia.TipoSalaDto;
import com.servinte.axioma.dto.salascirugia.TipoViaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public interface IHojaQuirurgicaMundo {

	/**
	 * Consulta de peticiones o solicitudes de cirugia de un paciente
	 * 
	 * @param codigoInstitucion
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @return Lista de peticiones
	 * @throws IPSException
	 * @author jeilones
	 * @created 18/06/2013
	 */
	List<PeticionQxDto> consultarPeticiones(int codigoInstitucion,int codigoIngreso, int codigoPaciente) throws IPSException;
	
	/**
	 * Consulta las especialidades que intervienen en una cirugia
	 * 
	 * @param codigoSolicitudCx
	 * @return Lista de especialidades
	 * @throws IPSException
	 * @author jeilones
	 * @created 19/06/2013
	 */
	List<EspecialidadDto> consultarEspecialidadesInformeQx(int codigoSolicitudCx) throws IPSException;
	
	/**
	 * Consulta una peticion o solicitud de cirugia especifica de un paciente 
	 * 
	 * @param codigoInstitucion
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @param codigoPeticionSolicitud
	 * @param esSolicitud
	 * @return peticion Cx
	 * @throws IPSException
	 * @author jeilones
	 * @created 18/07/2013
	 */
	PeticionQxDto consultarPeticionCirugia(int codigoInstitucion,int codigoIngreso, int codigoPaciente,int codigoPeticionSolicitud,boolean esSolicitud) throws IPSException;
	
	/**
	 * Consulta la informacion del acto quirurgica
	 *  
	 * @param solicitudCirugiaDto
	 * @return InformacionActoQxDto
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	InformacionActoQxDto consultarInformacionActoQx(SolicitudCirugiaDto solicitudCirugiaDto,int codigoIngreso) throws IPSException;
	
	/**
	 * Consultar anestesiologos activos en el aplicativo segun parametro general:
	 * "Codigo Especialidad Anestesiologia".
	 * 
	 * @return
	 * @author jeilones
	 * @throws IPSException 
	 * @created 24/06/2013
	 */
	List<ProfesionalHQxDto> consultarAnestesiologos(int codigoInstitucion) throws IPSException;
	
	/**
	 * Consulta los tipos de anestesia, si el parametro "Se maneja Hoja de Anestesia" esta definido como no, consulta los tipos de anestesia que estan marcados como
	 * "Mostrar en hoja qx".
	 * <br/>
	 * Tambien consulta los tipos de anestesia registrados al centro de costos especificado, si este no esta definido, consulta los tipos de anestesia que no estan asociados a 
	 * ningun centro de costos
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroCostos
	 * @return lista de tipos de anestesia
	 * @throws IPSException
	 * @author jeilones
	 * @created 24/06/2013
	 */
	List<TipoAnestesiaDto> consultarTiposAnestesia(int codigoInstitucion, int codigoCentroCostos) throws IPSException;
	
	/**
	 * Consulta las finalidades de un servicio dada su naturaleza y la institucion de la que se consulta
	 * 
	 * @param codigoNaturalezaServicio
	 * @param codigoInstitucion
	 * @return finalidades del servicio
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/06/2013
	 */
	List<FinalidadDto> consultarFinalidades(String codigoNaturalezaServicio,int codigoInstitucion) throws IPSException;
	
	/**
	 * Carga los tipos de via Igual/Diferente estaticamente 
	 * 
	 * @return tipos de via
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/06/2013
	 */
	List<TipoViaDto> consultarTiposVia() throws IPSException;
	
	/**
	 * Carga los tipos de herida estaticamente
	 * 
	 * @return tipos de herida
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/06/2013
	 */
	List<TipoHeridaDto> consultarTiposHerida() throws IPSException;
	
	/**
	 * Consulta el informe qx por especialidad, a este se le suman los diagnosticos (principal, relacionados o de complicacion), 
	 * si no tiene registrados diagnosticos se consulta los diagnosticos registrados a la primera descripcion operatoria registrada.
	 * 
	 * @param peticionQxDto
	 * @param especialidadDto
	 * @param paciente
	 * @param institucion
	 * @return informe qx
	 * @throws IPSException
	 * @author jeilones
	 * @created 26/06/2013
	 */
	InformeQxEspecialidadDto consultarInformeQxXEspecialidad(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto,PersonaBasica paciente, int institucion) throws IPSException;
	
	/**
	 * Consulta todas las especialidades que posee un profesional de la salud
	 * 
	 * @param connection
	 * @param profesionalHQxDto
	 * @param descartarEspecialidades
	 * @return lista de especialidades de un profesional
	 * @throws IPSException
	 * @author jeilones
	 * @created 5/07/2013
	 */
	List<EspecialidadDto> consultarEspecialidadesProfesional(ProfesionalHQxDto profesionalHQxDto, int codigoInstitucion, List<EspecialidadDto> descartarEspecialidades) throws IPSException;
	
	/**
	 * Metodo que consulta las notas aclaratorias relacionadas con un Informe Qx por Especialidad.
	 * @param codigoInformeQxEspecialidad
	 * @return List<NotaAclaratoriaDto>
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	List<NotaAclaratoriaDto> consultarNotasAclaratorias(int codigoInformeQxEspecialidad, boolean esAscendente) throws IPSException;
	
	/**
	 * Metodo que persiste la informacion de una nota aclaratoria, el objeto "notaAclaratoriaDto" debe tener descripcion y 
	 * codigoInformeQxEspecialidad seteados.
	 * @param notaAclaratoriaDto
	 * @param peticionQxDto
	 * @param usuarioModifica
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 12/07/2013
	 */
	void guardarNotaAclaratotia(NotaAclaratoriaDto notaAclaratoriaDto, PeticionQxDto peticionQxDto, UsuarioBasico usuarioModifica, int codigoIngreso) throws IPSException ;
	
	/**
	 * Metodo que Consulta seccion de ingreso / salida paciente de la hoja quirurgica dado un numero de Solicitud
	 * @param numeroSolicitud
	 * @return IngresoSalidaPacienteDto dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(int numeroSolicitud) throws IPSException;
	
	/**
	 * Metodo que consulta los tipos de sala para una institucion dada
	 * @param institucion
	 * @return List<TipoSalaDto> lista de tipos sala
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<TipoSalaDto> consultarTiposSala(int institucion) throws IPSException;
	
	/**
	 * Metodo que consulta los destinosPaciente parametrizados para una institucion dada
	 * @param institucion
	 * @return List<DestinoPacienteDto> lista de destinos
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<DestinoPacienteDto> consultarDestinosPaciente(int institucion) throws IPSException;
	
	/**
	 * Metodo que consulta las salas de cirugia de un tipo de sala dado
	 * @param tipoSalaDto
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<SalaCirugiaDto> consultarSalasCirugia(TipoSalaDto tipoSalaDto) throws IPSException;
	
	/**
	 * Metodo que registra el indicativo ha_sido_reversada de la hoja quirurgica
	 * @param numeroSolicitud
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	void reversarHojaQx(int numeroSolicitud) throws IPSException;

	/**
	 * Metodo que Persiste la informacion relacionada con la seccion "IngresoSalidaPaciente" de la Hoja quirurgica,
	 * si la hqx ya existe y no es necesario crearla se actualiza, 
	 * de lo contrario se hace la creacion de un nuevo registro de hqx.
	 * @param peticionQxDto
	 * @param ingresoSalidaPacienteDto
	 * @param usuarioModifica
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	void guardarIngresoSalidaPaciente(PeticionQxDto peticionQxDto,IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica, int codigoIngreso) throws IPSException ;
	
	/**
	 * Carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotas
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<NotaEnfermeriaDto> consultarNotasEnfermeria(int numeroSolicitud, boolean esAscendente) throws IPSException;
	
	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	void guardarNotaEnfermeria(NotaEnfermeriaDto dto)throws IPSException;
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 06/07/2013
	 */
	void guardarNotaRecuperacion(NotaRecuperacionDto dto)throws IPSException;
	
	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 06/07/2013
	 */
	List<NotaRecuperacionDto> consultarNotasRecuperacion(int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws IPSException;
	
	/**
	 * Permite persistir la informacion de la seccion informacion del acto Qx (Diagnosticos y participacion de anestesiologia)
	 * 
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @param usuarioBasico
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author jeilones
	 * @created 27/06/2013
	 */
	void guardarInformacionActoQuirurgico(PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico, int codigoIngreso)throws IPSException;
	
	/**
	 * Permite persistir la informacion de la seccion descripcion operatoria del Informe Qx (Servicios, Diagnosticos y Patologias)
	 * 
	 * @param paciente
	 * @param peticionQxDto
	 * @param informeQxEspecialidadDto
	 * @param usuarioModifica
	 * @param dxRelacionadosEliminar
	 * @param serviciosEliminar
	 * @param codigoIngreso
	 * @param serviciosNOPOS 
	 * @param justificacionesNOPOSServicios 
	 * @throws IPSException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	@SuppressWarnings("rawtypes")
	void guardarDescripcionOperatoria(PersonaBasica paciente, PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar, List<ServicioHQxDto> serviciosEliminar, int codigoIngreso, Map<String, String> serviciosNOPOS, HashMap justificacionesNOPOSServicios)
			throws IPSException;
	
	
	/**
	 * Consultar profesionales activos en el aplicativo
	 * 
	 * @param codigoInstitucion
	 * @return lista de profesionales del sistema
	 * @throws IPSException
	 * @author jeilones
	 * @created 24/06/2013
	 */
	List<ProfesionalHQxDto> consultarProfesionales(int codigoInstitucion) throws IPSException;

	/**
	 * Consulta los profesionales del acto Qx, los profesionales relacionados a una especialidad y los relacionados a los servicios
	 * 
	 * @param peticionQxDto
	 * @param especialidadDto
	 * @param paciente
	 * @param institucion
	 * @return profesionales que participan en el Acto Qx
	 * @throws IPSException
	 * @author jeilones
	 * @created 11/07/2013
	 */
	InformeQxDto consultarProfesionalesInformeQx(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto, PersonaBasica paciente, int institucion) throws IPSException;
	
	/**
	 * Consulta los tipos de asocio que se pueden usar para relacionar un profesional a una cirugia
	 * 
	 * @param codigoInstitucion
	 * @return tipos de profesional (tipos de asocio)
	 * @throws IPSException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	List<TipoProfesionalDto> consultarTiposProfesionales(int codigoInstitucion) throws IPSException;
	
	/**
	 * Guarda los profesionales que participan por servicio, por especialidad y otros profesionales relacionados al acto Qx
	 * 
	 * @param peticionQxDto
	 * @param informeQxDto
	 * @param usuarioBasico
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	void guardarProfesionalesInformeQx(PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico, int codigoIngreso) throws IPSException;

	/**
	 * Consulta la naturaleza de un servicio
	 * 
	 * @param codigoServicio
	 * @return codigo de la naturaleza
	 * @throws IPSException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	String consultarNaturalezaServicio(int codigoServicio) throws IPSException;
	
	/**
	 * Metodo que consulta los datos de la hoja de anestesia relacionada a un numero de solicitud
	 * @param con
	 * @param dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	HojaAnestesiaDto consultarHojaAnestesia(int numeroSolicitud) throws IPSException;

	/**
	 *  Metodo que consulta si esta ocupada la sala de cirugia seleccionada para el rango de fechas dado
	 * @param numSol
	 * @param codSala
	 * @param fechaIni
	 * @param horaIni
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	HashMap<?, ?> estaSalaOcupada(int numSol, int codSala, String fechaIni,String horaIni, String fechaFin, String horaFin) throws BDException;

	/**
	 * Metodo que consulta los estados de facturacion y liquidacion de los cargos asociados a una solicitud
	 * @param numeroSolicitud
	 * @return List<CargoSolicitudDto> 
	 * @autor Oscar Pulido
	 * @created 22/07/2013
	 */
	List<CargoSolicitudDto> consultarEstadoCargosSolicitud(int numeroSolicitud)  throws BDException;
	
	/**
	 * Metodo que consulta la programacion de cirugia, si existe, para una peticion dada
	 * @param codigoPeticion
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 23/07/2013
	 */
	ProgramacionPeticionQxDto consultarProgramacionPeticionQx(int codigoPeticion)  throws BDException;

	/**
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnosticoInt
	 * @return
	 * @throws IPSException 
	 */
	String getNombreDiagnostico(String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws IPSException;

	/**
	 * Consulta la parametrizacion del convenio para verificar si este hace requerido la justificacion NO POS de los servicios
	 * 
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/08/2013
	 */
	boolean requiereJustificacioServicio(int codigoConvenio)
			throws IPSException;

	/**
	 * Verifica que el profesional sea medico especialista
	 * 
	 * @param usuario
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/08/2013
	 */
	boolean validarEspecialidadProfesionalSalud(UsuarioBasico usuario)
			throws IPSException;
}
