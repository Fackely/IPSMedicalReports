package com.servinte.axioma.bl.manejoPaciente.interfaz;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.NivelAutorizacion;

/**
 * Interface que expone los servicios de Negocio correspondientes a la lógica asociada a las
 * Autorizaciones de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public interface IAutorizacionCapitacionMundo {

	/**
	 * Servicio encargado de obtener las entidades subcontratadas que correspondan
	 * a las prioridades de los niveles de autorización del usuario
	 * y que esten activas y tengan un contrato vigente
	 * 
	 * @param codigoCentroCosto
	 * @param nivelesAutorizacion
	 * @return
	 * @throws IPSException
	 */
	List<EntidadSubContratadaDto> obtenerEntidadesSubContratadasExternas(int codigoCentroCosto, List<NivelAutorizacionDto> nivelesAutorizacion, boolean requiereTransaccion) throws IPSException;

	/**
	 * Servicio que obtiene las ordenes pendientes por autorizar de un paciente
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 */
	List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorPaciente(int codigoPaciente) throws IPSException;
	
	/**
	 * Servicio que obtiene las ordenes pendientes por autorizar según los filtros
	 * de búsqueda seleccionados por el usuario
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param parametrosBusqueda
	 * @return
	 * @throws IPSException
	 */
	List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorRango(ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda) throws IPSException;

	/**
	 * Método encargado de guardar el registro de posponer de las ordenes seleccionadas
	 * en dicha funcionalidad
	 * 
	 * @param ordenesPosponer
	 * @param loginUsuario
	 * @param fechaPosponer
	 * @param observaciones
	 * @return
	 * @throws IPSException
	 */
	boolean posponerOrdenes(List<OrdenAutorizacionDto> ordenesPosponer, String loginUsuario, Date fechaPosponer, String observaciones) throws IPSException;

	/**
	 * Servicio que permite verificar si la entidad subcontratada se encuentra activa
	 * y si tiene un contrato vigente
	 * 
	 * @param codigoEntidad
	 * @return
	 * @throws IPSException
	 */
	EntidadSubContratadaDto verificarEntidadSubContratadaParametrizada(String codigoEntidad, boolean requiereTransaccion) throws IPSException;
	
	/**
	 *  Método que se encarga Valdiar la Anulación de la Autorización de Capitación para el Tipo de Solicitud.
	 *
	 * @param anulacionAutorizacionDto
	 * @param claseOrden
	 * @param tipoOrden
	 * @param tipoOrdenPyp
	 * @param institucion
	 * @throws IPSException
	 */
	List<AutorizacionCapitacionDto> validarAnulacionAutorizacionCapitaSolictud(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			int claseOrden, int tipoOrden, String tipoOrdenPyp, int institucion)throws IPSException;

	/**
	 * Método que se encarga de anular la Autorización de Capitación de la Solicitud Anulada
	 * @param anulacionAutorizacionsolicitudDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @throws IPSException
	 */
	void anularAutorizacionCapitacion(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto, 
			AutorizacionPorOrdenDto autorizacionPorOrdenDto, int institucion)throws IPSException;

	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	List<ServicioAutorizadoCapitacionDto> consultarServiciosAutorizadosCapitacion(long consecutivo,long tarifario,boolean isMigrado)throws IPSException;
	
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud o Orden Ambulatoria)
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	List<ArticuloAutorizadoCapitacionDto> consultarArticulosAutorizadosCapitacion(long consecutivo,int institucion,boolean isMigrado)throws IPSException;
	
	/**
	 * Método encargado de validar los datos de la autorizacion para determinar si esta 
	 * se debe generar o determinar si encuentran servicios o medicamentos que no pueden
	 * ser autorizados
	 * Recibe una autorizacion con la lista de ordenes a autorizar y retorna las autorizaciones
	 * según las validaciones requeridas
	 * @param autorizacionCapitacionDto
	 * @return List<AutorizacionCapitacionDto>
	 */
	List<AutorizacionCapitacionDto> validarGenerarAutorizacionCapitacion(AutorizacionCapitacionDto autorizacionCapitacionDto) throws IPSException;
	
	/**
	 * Método que se encarga de verificar los servicios o medicamentos/insumos que no se pueden 
	 * autorizar para agregar el mensaje correspondiente obteniendo la descripcion del servicio o medicamento y 
	 * el acronimo según la urgencia de tramite de la autorizacion
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 */
	void obtenerMensajesServiciosMedicamentosInsumosAutorizar(AutorizacionCapitacionDto autorizacionCapitacionDto, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * Método que se encarga de obtener la descripcion del servicio y 
	 * el acronimo según la urgencia de tramite de la autorizacion
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 * @return String[]
	 * DESCRIPCIONSERVICIO = 0;
	 * CODIGOPROPIETARIO   = 1;
	 * NUMDIASURGENTE 	   = 2;
	 * ACRONIMODIASURGENTE = 3;
	 * NUMDIASNORMAL 	   = 4;
	 * ACRONIMODIASNORMAL  = 5;
	 */
	String[] obtenerDescripcionServicioAutorizar(int codigoServicio, int codigoTarifarioServicio, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * Método que se encarga de obtener la descripcion del medicamento / insumo y 
	 * datos relacionados
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 * DESCRIPCIONARTICULO = 0;
	 * CODIGOPROPIETARIO   = 1;
	 * CONCENTRACION       = 2;
	 * UNIDADMEDIDA 	   = 3;
	 */
	String[] obtenerDescripcionMedicamentoInsumoAutorizar(int codigoMedicamentoInsumo, 
			int codigoInstitucion, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * Retorna las autorizaciones de entidad subcontratada vs articulo
	 * 
	 * @param codigoAutoEntSub
	 * @param codigoArticulo
	 * @param requiereTransaccion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 22/08/2012
	 */
	List<AutorizacionesEntSubArticu> obtenerAutorizacionEntSubArticulo(long codigoAutoEntSub,int codigoArticulo,boolean requiereTransaccion)throws IPSException;
	
	/**
	 * Actualiza el nivel de autorizacion de una autorizacion de entidad subcontratada para servicios
	 * 
	 * @param autorizacionesEntSubServi
	 * @param nivelAutorizacion
	 * @param requiereTransaccion
	 * @throws IPSException
	 * @author jeilones
	 * @created 28/08/2012
	 */
	void actualizarNivelAutorizacionAutoEntSubServicio(AutorizacionesEntSubServi autorizacionesEntSubServi,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion)throws IPSException;
	
	/**
	 * Actualiza el nivel de autorizacion de una autorizacion de entidad subcontratada para servicios
	 * 
	 * @param autorizacionesEntSubArticu
	 * @param nivelAutorizacion
	 * @param requiereTransaccion
	 * @throws IPSException
	 * @author jeilones
	 * @created 28/08/2012
	 */
	void actualizarNivelAutorizacionAutoEntSubArticulo(AutorizacionesEntSubArticu autorizacionesEntSubArticu,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion)throws IPSException;
	
	/**
	 * Consulta las autorizaciones de entidad subcontratada de una autorizacion de capitacion que pertenece a un 
	 * ingreso estancia
	 * 
	 * @param idAutoIngEst
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 3/09/2012
	 */
	List<AutorizacionesEntidadesSub> obtenerAutorizacionEntSubDeIngEstancia(long idAutoIngEst,boolean requiereTransaccion)throws IPSException;
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param listDtoAutorizacion
	 * @param errores
	 */
	public void obtenerMensajesError(List<AutorizacionCapitacionDto> listDtoAutorizacion, ActionMessages errores) throws IPSException;
	
	/**
	 * Servicio encargado de validar si un Medicamento/Insumo esta cubierto por el convenio capitado de la orden
	 * 
	 * @param claseOrden
	 * @param codigoConvenio
	 * @param codigoOrden
	 * @param codigoMedicamentoInsumo
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	boolean tieneCoberturaMedicamentoInsumo(int claseOrden, int codigoConvenio, Long codigoOrden, int codigoMedicamentoInsumo) throws IPSException;
	
	/**
	 * Servicio que permite obtener las autorizaciones de capitación y entidad subcontratada asociadas a las 
	 * ordenes medicas, ordenes ambulatorias y peticiones. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param codOrden
	 * @param Estado
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	
	List<AutorizacionPorOrdenDto> obtenerAutorizacionCapitacion(int claseOrden, int tipoOrden, Long codOrden, List<String> estadosAutorizacion) throws IPSException ; 
	
	/**
	 * Servicio que permite realizar la anulación  y recalcular el cierre temporal
	 * para las ordenes medicas, ordenes ambulatorias y peticiones que se esten modificando, 
	 * las cuales tengan una autorización asociada. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param autorizacionesPorOrdenDto
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	boolean procesoAnulacionAutorizacion (int claseOrden, int tipoOrden, List<AutorizacionPorOrdenDto> autorizacionesPorOrdenDto, 
			AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto, int codInstitucion) throws IPSException;
	
	/**
	 * Sevicio que permite identificar si una orden ambulatoria se encuentra asociada a una solicitud.
	 * 
	 * @param numeroSolicitud
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	String existeOrdenAmbAsociadaSolicitud (int numeroSolicitud) throws IPSException; 

	/**
	 * Consulta todos los servicios anulados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param consecutivo
	 * @param tarifario
	 * @param isMigrado
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<ServicioAutorizadoCapitacionDto> consultarServiciosAnuladosCapitacion(long consecutivo, long tarifario, boolean isMigrado) throws IPSException;
	
	/**
	 * Consulta todos los articulos anulados de capitacion (Orden Ambulatoria o Solicitud de Medicamentos)
	 * @param consecutivo
	 * @param institucion
	 * @param isMigrado
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<ArticuloAutorizadoCapitacionDto> consultarArticulosAnuladosCapitacion(long consecutivo, int institucion, boolean isMigrado) throws IPSException;
	
	/**
	* Método que evalua si una solicitud tiene asociadas autorizaciones de capitacion 
	* Generadas desde la Petición de Cirugia
	* @author ricruico
	* @param numeroSolicitud
	* @return existe
	* @throws IPSException
	*
	*/
	public boolean existeAutorizacionCapitacionGeneradaPorPeticion(int numeroSolicitud) throws IPSException;

	/**
	 * Metodo encargado de consultar los datos de la entrega de la autorizacion
	 * @param idAutorizacionEntidadSub
	 * @return {@link AutorizacionEntregaDto}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 20-feb-2013 
	 */
	public AutorizacionEntregaDto consultarEntregaAutorizacionEntidadSubContratada (long idAutorizacionEntidadSub) throws IPSException;

	/**
	 * Metodo encargado de guardar los datos de entrega de la autorizacion original
	 * @param autorizacionEntregaDto
	 * @return {@link Boolean}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public boolean guardarEntregaAutorizacionEntidadSubContratadaOriginal (AutorizacionEntregaDto autorizacionEntregaDto) throws IPSException;
	
	/**
	 * Metodo encargado de consultar el id de la Autorizacion de Entidad Subcontratada
	 * apartir de el Consecutivo de Autorizacion de Entidad SubContratada
	 * @param consecutivoAutorizacion
	 * @return {@link Long}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public Long consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion (String consecutivoAutorizacion) throws IPSException;
	
	/**
	 * Metodo encargado de consulta y el paciente paga atención o No 
	 * 
	 * @param codigoContrato
	 * @return Boolean
	 * @throws BDException
	 * @author sanbarga
	 * MT6703
	 * @created 12-Abr-2013
	 */
	
	public Boolean consultarSiPacientePagaAtencion(int codigoContrato, boolean requiereTransaccion) throws IPSException;
	
	
	/**
	 * Metodo encargado de consulta Si el contrato manejo monto de cobro
	 * 
	 * @param codigoConveio
	 * @return Boolean
	 * @author sanbarga
	 * @throws IPSException 
	 * @created 15-Abr-2013
	 * MT6703
	 */
	public Boolean consultarSiConvenioManejaMontos(int codigoConvenio) throws IPSException ;
}