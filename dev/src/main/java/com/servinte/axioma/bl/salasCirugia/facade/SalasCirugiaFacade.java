/**
 * 
 */
package com.servinte.axioma.bl.salasCirugia.facade;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.salasCirugia.impl.HojaQuirurgicaMundo;
import com.servinte.axioma.bl.salasCirugia.interfaz.IHojaQuirurgicaMundo;
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
public class SalasCirugiaFacade {
	
	/**
	 * Consulta de peticiones o solicitudes de cirugia de un paciente en estado diferente a anulada
	 * 
	 * @param codigoInstitucion
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @return Lista de peticiones
	 * @throws IPSException
	 * @author jeilones
	 * @created 18/06/2013
	 */
	public List<PeticionQxDto> consultarPeticiones(int codigoInstitucion,int codigoIngreso, int codigoPaciente) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarPeticiones(codigoInstitucion, codigoIngreso, codigoPaciente);
	}
	
	/**
	 * Consulta las especialidades que intervienen en una cirugia
	 * 
	 * @param codigoSolicitudCx
	 * @return Lista de especialidades
	 * @throws IPSException
	 * @author jeilones
	 * @created 19/06/2013
	 */
	public List<EspecialidadDto> consultarEspecialidadesInformeQx(int codigoSolicitudCx)
			throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarEspecialidadesInformeQx(codigoSolicitudCx);
	}
	
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
	public PeticionQxDto consultarPeticionCirugia(int codigoInstitucion,int codigoIngreso, int codigoPaciente,int codigoPeticionSolicitud, boolean esSolicitud) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarPeticionCirugia(codigoInstitucion, codigoIngreso, codigoPaciente, codigoPeticionSolicitud, esSolicitud);
	}

	/**
	 * Consulta la informacion del acto quirurgica
	 *  
	 * @param solicitudCirugiaDto
	 * @return InformacionActoQxDto
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public InformacionActoQxDto consultarInformacionActoQx(SolicitudCirugiaDto solicitudCirugiaDto, int codigoIngreso)
			throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarInformacionActoQx(solicitudCirugiaDto,codigoIngreso);
	}

	/**
	 * Consultar anestesiologos activos en el aplicativo segun parametro general:
	 * "Codigo Especialidad Anestesiologia".
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @author jeilones
	 * @throws IPSException 
	 * @created 24/06/2013
	 */
	public List<ProfesionalHQxDto> consultarAnestesiologos(int codigoInstitucion)
			throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarAnestesiologos(codigoInstitucion);
	}

	/**
	 * Consulta los tipos de anestesia, si el parametro "Se maneja Hoja de Anestesia" esta definido como no, consulta los tipos de anestesia que estan marcados como
	 * "Mostrar en hoja qx".
	 * <br/><br/>
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
	public List<TipoAnestesiaDto> consultarTiposAnestesia(int codigoInstitucion, int codigoCentroCostos) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarTiposAnestesia(codigoInstitucion, codigoCentroCostos);
	}

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
	public List<FinalidadDto> consultarFinalidades(String codigoNaturalezaServicio,int codigoInstitucion) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarFinalidades(codigoNaturalezaServicio, codigoInstitucion);
	}

	/**
	 * Carga los tipos de via Igual/Diferente estaticamente 
	 * 
	 * @return tipos de via
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/06/2013
	 */
	public List<TipoViaDto> consultarTiposVia() throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarTiposVia();
	}

	/**
	 * Carga los tipos de herida estaticamente
	 * 
	 * @return tipos de herida
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/06/2013
	 */
	public List<TipoHeridaDto> consultarTiposHerida() throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarTiposHerida();
	}

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
	public InformeQxEspecialidadDto consultarInformeQxXEspecialidad(
			PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto, PersonaBasica paciente, int institucion) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarInformeQxXEspecialidad(peticionQxDto, especialidadDto, paciente, institucion);
	}

	/**
	 * Consultar profesionales activos en el aplicativo
	 * 
	 * @param connection
	 * @return
	 * @author jeilones
	 * @param codigoEspecialidadAnestesiologia 
	 * @throws IPSException 
	 * @created 24/06/2013
	 */
	public List<ProfesionalHQxDto> consultarProfesionales(int codigoInstitucion) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarProfesionales(codigoInstitucion);
	}

	
	/**
	 * Consulta los tipos de asocio que se pueden usar para relacionar un profesional a una cirugia
	 * 
	 * @param codigoInstitucion
	 * @return tipos de profesional (tipos de asocio)
	 * @throws IPSException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	public List<TipoProfesionalDto> consultarTiposProfesionales(int codigoInstitucion) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarTiposProfesionales(codigoInstitucion);
	}
	
	/**
	 * Consulta todas las especialidades que posee un profesional de la salud
	 * 
	 * @param profesionalHQxDto
	 * @param descartarEspecialidades
	 * @return lista de especialidades de un profesional
	 * @throws BDException
	 * @author jeilones
	 * @created 5/07/2013
	 */
	public List<EspecialidadDto> consultarEspecialidadesProfesional(ProfesionalHQxDto profesionalHQxDto,int codigoInstitucion,List<EspecialidadDto>descartarEspecialidades)
			throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarEspecialidadesProfesional(profesionalHQxDto,codigoInstitucion,descartarEspecialidades);
	}

	/**
	 * Metodo que consulta las notas aclaratorias relacionadas con un Informe Qx por Especialidad.
	 * @param codigoInformeQxEspecialidad
	 * @return List<NotaAclaratoriaDto>
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public List<NotaAclaratoriaDto> consultarNotasAclaratorias(int codigoInformeQxEspecialidad, boolean esAscendente) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarNotasAclaratorias(codigoInformeQxEspecialidad,esAscendente);
	}
	
	/**
	 * Metodo que persiste la informacion de una nota aclaratoria, el objeto "notaAclaratoriaDto" debe tener descripcion y 
	 * codigoInformeQxEspecialidad seteados.
	 * @param notaAclaratoriaDto
	 * @param usuarioModifica
	 * @param peticionQxDto
	 * @param codigoIngreso
	 * @param institucion
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 12/07/2013
	 */
	public void guardarNotaAclaratotia (NotaAclaratoriaDto notaAclaratoriaDto, UsuarioBasico usuarioModifica, PeticionQxDto peticionQxDto, int codigoIngreso) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarNotaAclaratotia(notaAclaratoriaDto, peticionQxDto, usuarioModifica, codigoIngreso);
	}

	/**
	 * Metodo que Consulta seccion de ingreso / salida paciente de la hoja quirurgica  dado un numero de Solicitud
	 * @param numeroSolicitud
	 * @return IngresoSalidaPacienteDto dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(int numeroSolicitud) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarIngresoSalidaPaciente(numeroSolicitud);
	}
	
	/**
	 * Metodo que consulta los tipos de sala para una institucion dada
	 * @param institucion
	 * @return List<TipoSalaDto> lista de tipos sala
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public List<TipoSalaDto> consultarTiposSala(int institucion) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarTiposSala(institucion);
	}

	/**
	 * Metodo que consulta las salas de cirugia de un tipo de sala dado
	 * @param tipoSalaDto
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public List<SalaCirugiaDto> consultarSalasCirugia(TipoSalaDto tipoSalaDto) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarSalasCirugia(tipoSalaDto);
	}
	
	/**
	 * Metodo que consulta los destinosPaciente parametrizados para una institucion dada
	 * @param institucion
	 * @return List<DestinoPacienteDto> lista de destinos
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public List<DestinoPacienteDto> consultarDestinosPaciente(int institucion) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarDestinosPaciente(institucion);
	}

	/**
	 * Metodo que registra el indicativo ha_sido_reversada de la hoja quirurgica
	 * @param numeroSolicitud
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public void reversarHojaQx(int numeroSolicitud)throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.reversarHojaQx(numeroSolicitud);
	}

	/**
	 * Metodo que Persiste la informacion relacionada con la seccion "IngresoSalidaPaciente" de la Hoja quirurgica,
	 * si la hqx ya existe y no es necesario crearla se actualiza, 
	 * de lo contrario se hace la creacion de un nuevo registro de hqx.
	 * @param ingresoSalidaPacienteDto
	 * @param usuarioModifica
	 * @param peticionQxDto
	 * @param codigoIngreso
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public void guardarIngresoSalidaPaciente (IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica, PeticionQxDto peticionQxDto, int codigoIngreso) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarIngresoSalidaPaciente(peticionQxDto, ingresoSalidaPacienteDto, usuarioModifica, codigoIngreso);
	}

	/**
	 * Metodo que carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public List<NotaEnfermeriaDto> consultarNotasEnfermeria(int numeroSolicitud, boolean esAscendente) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarNotasEnfermeria(numeroSolicitud, esAscendente);
	}
	
	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaEnfermeria(NotaEnfermeriaDto dto)throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarNotaEnfermeria(dto);
	}

	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * si se desea cargar unicamente la estructura de la nota se debe enviar el parametro "soloEstructuraParametrizada" en true
	 * en ese caso no es necesario enviar valor para el parametro "numeroSolicitud"
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public List<NotaRecuperacionDto> consultarNotasRecuperacion(int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarNotasRecuperacion(codigoInstitucion, numeroSolicitud, soloEstructuraParametrizada,esAscendente);
	}
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param con
	 * @param dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaRecuperacion(NotaRecuperacionDto dto)throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarNotaRecuperacion(dto);
	}
	
	/**
	 * Permite persistir la informacion de la seccion informacion del acto Qx (Diagnosticos y participacion de anestesiologia)
	 * 
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @param usuarioBasico
	 * @param codigoIngreso
	 * 
	 * @throws IPSException
	 * @author jeilones
	 * @created 27/06/2013
	 */
	public void guardarInformacionActoQuirurgico(PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico, int codigoIngreso)throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarInformacionActoQuirurgico(peticionQxDto, informacionActoQxDto, dxRelacionadosEliminar, usuarioBasico, codigoIngreso);
	}
	
	/**
	 * Permite persistir la informacion de la seccion descripcion operatoria del Informe Qx (Servicios, Diagnosticos y Patologias)
	 * 
	 * @param paciente
	 * @param peticionQxDto
	 * @param informeQxEspecialidadDto
	 * @param dxRelacionadosEliminar
	 * @param usuarioModifica
	 * @param serviciosEliminar
	 * @param codigoIngreso
	 * @param serviciosNOPOS
	 * @param justificacionesNOPOSServicios
	 * 
	 * @throws IPSException
	 * @author jeilones 
	 * @param justificacionesNOPOSServicios 
	 * @param serviciosNOPOS 
	 * @created 2/07/2013
	 */
	public void guardarDescripcionOperatoria(PersonaBasica paciente,PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioModifica, List<ServicioHQxDto> serviciosEliminar, int codigoIngreso, Map<String, String> serviciosNOPOS, HashMap justificacionesNOPOSServicios) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarDescripcionOperatoria(paciente, peticionQxDto, informeQxEspecialidadDto, usuarioModifica, dxRelacionadosEliminar, serviciosEliminar, codigoIngreso,serviciosNOPOS,justificacionesNOPOSServicios);
	}
	
	/**
	 * Consulta los profesionales del acto Qx, los profesionales relacionados a una especialidad y los relacionados a los servicios
	 * 
	 * @param peticionQxDto
	 * @param especialidadDto
	 * @param institucion
	 * @return profesionales que participan en el Acto Qx
	 * @throws IPSException
	 * @author jeilones
	 * @param paciente 
	 * @created 11/07/2013
	 */
	public InformeQxDto consultarProfesionalesInformeQx(PeticionQxDto peticionQxDto,EspecialidadDto especialidadDto, PersonaBasica paciente, int institucion) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarProfesionalesInformeQx(peticionQxDto, especialidadDto, paciente, institucion);
	}
	
	/**
	 * Consulta la naturaleza de un servicio
	 * 
	 * @param codigoServicio
	 * @return codigo de la naturaleza
	 * @throws IPSException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	public String consultarNaturalezaServicio(int codigoServicio) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarNaturalezaServicio(codigoServicio);
	}

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
	public void guardarProfesionalesInformeQx(PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico, int codigoIngreso) throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		hojaQuirurgicaMundo.guardarProfesionalesInformeQx(peticionQxDto, informeQxDto, usuarioBasico, codigoIngreso);
	}
	
	/**
	 * Metodo que consulta los datos de la hoja de anestesia relacionada a un numero de solicitud
	 * @param con
	 * @param dto
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	public HojaAnestesiaDto consultarHojaAnestesia(int numeroSolicitud)  throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarHojaAnestesia(numeroSolicitud);
	}

	/**
	 * Metodo que consulta si esta ocupada la sala de cirugia seleccionada para el rango de fechas dado
	 * @param numSol
	 * @param codSala
	 * @param fechaIni
	 * @param horaIni
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 * @throws IPSException
	 * @author Oscar Pulido
	 * @created 19/07/2013
	 */
	public HashMap<?, ?> estaSalaOcupada(int numSol,int codSala,String fechaIni,String horaIni,String fechaFin,String horaFin) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.estaSalaOcupada(numSol, codSala, fechaIni, horaIni, fechaFin,horaFin);
	}

	/**
	 * Metodo que consulta los estados de facturacion y liquidacion de los cargos asociados a una solicitud
	 * @param numeroSolicitud
	 * @return List<CargoSolicitudDto> 
	 * @autor Oscar Pulido
	 * @created 22/07/2013
	 */
	public List<CargoSolicitudDto> consultarEstadoCargosSolicitud(int numeroSolicitud) throws IPSException {
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarEstadoCargosSolicitud(numeroSolicitud);
	}

	/**
	 * Metodo que consulta la programacion de cirugia, si existe, para una peticion dada
	 * @param codigoPeticion
	 * @return
	 * @throws IPSException
	 * @autor Oscar Pulido
	 * @created 23/07/2013
	 */
	public ProgramacionPeticionQxDto consultarProgramacionPeticionQx(int codigoPeticion)  throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.consultarProgramacionPeticionQx(codigoPeticion);
	}

	/**
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnosticoInt
	 * @return
	 * @throws IPSException 
	 */
	public String getNombreDiagnostico(String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws IPSException
	{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.getNombreDiagnostico(acronimoDiagnostico, tipoCieDiagnosticoInt);
	}
	
	/**
	 * Consulta la parametrizacion del convenio para verificar si este hace requerido la justificacion NO POS de los servicios
	 * 
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/08/2013
	 */
	public boolean requiereJustificacioServicio(int codigoConvenio)
			throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.requiereJustificacioServicio(codigoConvenio);
	}
	
	/**
	 * Verifica que el profesional sea medico especialista
	 * 
	 * @param usuario
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/08/2013
	 */
	public boolean validarEspecialidadProfesionalSalud(UsuarioBasico usuario)
			throws IPSException{
		IHojaQuirurgicaMundo hojaQuirurgicaMundo=new HojaQuirurgicaMundo();
		return hojaQuirurgicaMundo.validarEspecialidadProfesionalSalud(usuario);
	}
}
