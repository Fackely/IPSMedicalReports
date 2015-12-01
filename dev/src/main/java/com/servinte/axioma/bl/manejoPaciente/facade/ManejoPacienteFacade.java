package com.servinte.axioma.bl.manejoPaciente.facade;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.facturacion.InfoCobertura;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.servinte.axioma.bl.capitacion.impl.NivelAutorizacionMundo;
import com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo;
import com.servinte.axioma.bl.facturacion.impl.CatalogoFacturacionMundo;
import com.servinte.axioma.bl.facturacion.impl.CoberturaMundo;
import com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo;
import com.servinte.axioma.bl.facturacion.interfaz.ICoberturaMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.AutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.CatalogoManejoPacienteMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.EstanciaAutomaticaMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.IngresoMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.PresupuestoMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.ProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IEstanciaAutomaticaMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IPresupuestoMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.ordenes.impl.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.impl.SolicitudesMundo;
import com.servinte.axioma.bl.ordenes.interfaz.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.bl.salasCirugia.impl.PeticionesMundo;
import com.servinte.axioma.bl.salasCirugia.interfaz.IPeticionesMundo;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionesSolicitudesDto;
import com.servinte.axioma.dto.manejoPaciente.EncabezadoRepUsuConDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.dto.ordenes.OrdenAmbulatoriaDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;

/**
 * Clase Fachada que provee todos los servicios de lógica de negocio del módulo de
 * Manejo del Paciente a todos los Action de la Capa Web
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class ManejoPacienteFacade {

	/**
	 * Servicio encargado de obtener las vias de ingreso parametrizadas
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<ViaIngresoDto> consultarViasIngreso() throws IPSException{
		ICatalogoManejoPacienteMundo mundoCatalogoManejoPaciente = new CatalogoManejoPacienteMundo();		
		return mundoCatalogoManejoPaciente.consultarViasIngreso();
	}
	/**
	 * Servicio encargado de obtener los tipos de identificacion
	 * 
	 * @return
	 * @throws IPSException
	 */
	public ArrayList<TiposIdentificacion> listaTiposIdentificacion() throws IPSException{
		ITiposIdentificacionServicio tiposIdentificacionServicio= AdministracionFabricaServicio.crearTiposIdentificacionServicio();
		return tiposIdentificacionServicio.listarTodos();
	}
	/**
	 * Servicio encargado de obtener una lista de usuarios consumidores del presupueto segun los parámetros recibidos
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
     * @param autorizaciones
     * @param convenio
     * @param viaIngreso
     * @param grupoSeleccionado
     * @param inventarioSeleccionado
     * @param nombreDiagnostico
     * @param valorInicial
     * @param valorFinal
     * @param tipoIdentificacion
     * @param numeroIdentificacion
	 * @return 
	 * @throws IPSException
	 */
	public List<UsuariosConsumidoresPresupuestoDto> listaUsuariosConsumidoresPresupuesto(String fechaInicial,String fechaFinal, String autorizaciones, String convenio, String viaIngreso, String grupoSeleccionado, String inventarioSeleccionado, String nombreDiagnostico, String valorInicial, String valorFinal, String tipoIdentificacion, String numeroIdentificacion)
		throws IPSException{
		IPresupuestoMundo mundoPresupuesto= new PresupuestoMundo();
		return mundoPresupuesto.consultarUsuariosConsumidoresAutor(fechaInicial, fechaFinal, autorizaciones, convenio, viaIngreso, grupoSeleccionado, inventarioSeleccionado, nombreDiagnostico, valorInicial, valorFinal, tipoIdentificacion, numeroIdentificacion);
		}
	/**
	 * @param nombreConsecutivo
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeConsecutivoDisponible(String nombreConsecutivo, int codigoInstitucion) throws IPSException{
		return false;
	}

	/**
	 * Método encargado de obtener la información del centro de atención
	 * asigando al paciente
	 * 
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public CentroAtencionDto obtenerCentroAtencionPaciente(int codigoPaciente) throws IPSException{
		IIngresoMundo ingresoMundo= new IngresoMundo();
		return ingresoMundo.obtenerCentroAtencionAsignadoPaciente(codigoPaciente);
	}

	/**
	 * Servicio encargado de obtener las entidades subcontratadas que correspondan
	 * a las prioridades de los niveles de autorización del usuario
	 * y que esten activas y tengan un contrato vigente
	 * 
	 * @param codigoCentroCosto
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<EntidadSubContratadaDto> obtenerEntidadesSubContratadasExternas(int codigoCentroCosto, List<NivelAutorizacionDto> nivelesAutorizacionUsuario) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo= new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.obtenerEntidadesSubContratadasExternas(codigoCentroCosto, nivelesAutorizacionUsuario, true);
	}

	/**
	 * @param autorizacionCapitacion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<BusquedaMontosCobroDto> obtenerMontosCobroAutorizacion(FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro) throws IPSException{
		ICatalogoFacturacionMundo catalogoFacturacionMundo = new CatalogoFacturacionMundo();
		return catalogoFacturacionMundo.buscarMontosCobro(filtroBusquedaMontosCobro,true);
	}

	/**
	 * Servicio que obtiene las ordenes pendientes por autorizar de un paciente
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorPaciente(int codigoPaciente) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.obtenerOrdenesPorAutorizarPorPaciente(codigoPaciente);
	}

	/**
	 * Servicio que obtiene las ordenes pendientes por autorizar según los filtros
	 * de búsqueda seleccionados por el usuario
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param parametroBusqueda
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<OrdenAutorizacionDto> obtenerOrdenesPorAutorizarPorRango(ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.obtenerOrdenesPorAutorizarPorRango(parametrosBusqueda);
	}

	
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
	 * @author ricruico
	 */
	public boolean posponerOrdenes(List<OrdenAutorizacionDto> ordenesPosponer, String loginUsuario, Date fechaPosponer, String observaciones) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo= new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.posponerOrdenes(ordenesPosponer, loginUsuario, fechaPosponer, observaciones);
	}

	/**
	 * Servicio encargado de validar la cobertura del Medicamento/Insumo de la orden DCU 800
	 * 
	 * @param codigoContrato de la Entidad Subcontratada
	 * @param codigoViaIngreso de la Orden
	 * @param tipoPaciente
	 * @param codigoMedicamentoInsumo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public InfoCobertura validarCoberturaMedicamentoInsumoEntidadSubcontratada(long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoMedicamentoInsumo,
								Integer codigoNaturalezaPaciente, int codigoInstitucion) throws IPSException{
		ICoberturaMundo coberturaMundo= new CoberturaMundo();
		return coberturaMundo.validacionCoberturaArticuloEntidadSub(codigoContrato, codigoViaIngreso, tipoPaciente, codigoMedicamentoInsumo, codigoNaturalezaPaciente, codigoInstitucion);
	}

	/**
	 * Servicio encargado de validar la cobertura del Servicio de la orden DCU 800
	 * 
	 * @param codigoContrato de la Entidad Subcontratada
	 * @param codigoViaIngreso de la Orden
	 * @param tipoPaciente
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public InfoCobertura validarCoberturaServicioEntidadSubcontratada(long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio,
								Integer codigoNaturalezaPaciente, int codigoInstitucion) throws IPSException{
		ICoberturaMundo coberturaMundo= new CoberturaMundo();
		return coberturaMundo.validacionCoberturaServicioEntidadSub(codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
	}

	
	/**
	 * Servicio que permite verificar si la entidad subcontratada se encuentra activa
	 * y si tiene un contrato vigente
	 * 
	 * @param codigoEntidad
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public EntidadSubContratadaDto verificarEntidadSubContratadaParametrizada(String codigoEntidad) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo= new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.verificarEntidadSubContratadaParametrizada(codigoEntidad, true);
	}

	/**
	 * Servicio que obtiene las ordenes pendientes por autorizar según los filtros
	 * de búsqueda seleccionados por el usuario
	 * incluye Ordenes Médicas, Solicitudes de Cargos Directos, Ordenes Ambulatorias
	 * y Peticiones de Cirugía
	 * 
	 * @param parametroBusqueda
	 * @return
	 * @throws IPSException
	 */
	public EncabezadoRepUsuConDto obtenerEncabezado() throws IPSException{
		IPresupuestoMundo obtenerEncabezado = new PresupuestoMundo();
		return obtenerEncabezado.obtenerEncabezado();
	}
	
	/**
	 * Servicio encargado de obtener el detalle de la viaIngrso por id
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public ViaIngresoDto consultarViaIngresoPorId(int codigo) throws IPSException{
		ICatalogoManejoPacienteMundo mundoCatalogoManejoPaciente = new CatalogoManejoPacienteMundo();		
		return mundoCatalogoManejoPaciente.consultarViaIngresoPorId(codigo);
	}
	
	
	/**
	 * Servicio encargado de obtener la información mas reciente de la subcuenta de acuerdo a
	 * el código de ingreso y del convenio de la orden
	 * 
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public InfoSubCuentaDto consultarInfoSubCuentaPorIngresoPorConvenio(int codigoIngreso, int codigoConvenio) throws IPSException{
		IIngresoMundo ingresoMundo = new IngresoMundo();		
		return ingresoMundo.consultarInfoSubCuentaPorIngresoPorConvenio(codigoIngreso, codigoConvenio, true);
	}
	
	/**
	 * Servicio encargado de obtener la información mas reciente de la cuenta de acuerdo a
	 * el código de ingreso
	 * 
	 * @param codigoIngreso
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public DtoCheckBox consultarInfoCuentaPorIngreso(int codigoIngreso) throws IPSException{
		IIngresoMundo ingresoMundo = new IngresoMundo();		
		return ingresoMundo.consultarInfoCuentaPorIngreso(codigoIngreso);
	}
	
	
	/**
	 * Servicio que permite generar el proceso de autorización de entidad subcontratada 
	 * generado por la capitación para la estancia automatica y los cargos pendientes
	 * 
	 * @throws IPSException
	 */
	@SuppressWarnings("rawtypes")
	public void generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(AutorizacionCapitacionDto autorizacionCapitacion, EstanciaAutomatica estanciaAutomatica,  
			HashMap mapaCuentas, UsuarioBasico usuario, int servicio, int pos, boolean esEstancia, Cargos cargos) throws IPSException{
		IProcesoGeneracionAutorizacionMundo generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes = new ProcesoGeneracionAutorizacionMundo();
		generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes.generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(autorizacionCapitacion, 
				estanciaAutomatica, mapaCuentas, usuario, servicio, pos, esEstancia, cargos);
	}
	
	/**
	 * Método que permite generar la estancia automatica, generando
	 * la solicitud, los cargos y la estancia automatica.
	 * 
	 * @param con
	 * @param estanciaAutomatica
	 * @param pos
	 * @param mapaCuentas
	 * @param usuario
	 * @param servicio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int generarEstanciaAutomatica(Connection con, EstanciaAutomatica estanciaAutomatica, int pos, HashMap mapaCuentas, 
			UsuarioBasico usuario, int servicio) throws Exception{
		IEstanciaAutomaticaMundo generarEstanciaAutomatica = new EstanciaAutomaticaMundo();
		return generarEstanciaAutomatica.generarEstanciaAutomatica(con, estanciaAutomatica, pos, mapaCuentas, usuario, servicio);
	}
	
	
	/**
	 * Retorna las solicitudes de una autorizacion y que provienen de una orden ambulatoria
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Jeison Londono
	 */
	public List<AutorizacionesSolicitudesDto> obtenerSolicitudesAutorizacionConOrdenAmbulatoria(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionEntSubcontratadasCapitacion) throws IPSException{
		ISolicitudesMundo solicitudesMundo=new SolicitudesMundo();
		
		return solicitudesMundo.obtenerSolicitudesAutorizacionConOrdenAmbulatoria(dtoAutorizacionEntSubcontratadasCapitacion);
	}
	
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAutorizadosCapitacion(long consecutivo,long tarifario,boolean isMigrado)throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.consultarServiciosAutorizadosCapitacion(consecutivo, tarifario, isMigrado);
	}
	
	/**
	 * Consulta todos los servicios autorizados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAutorizadosCapitacion(long consecutivo,int institucion,boolean isMigrado)throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.consultarArticulosAutorizadosCapitacion(consecutivo,institucion,isMigrado);
	}
	
	/**
	 * Servicio que genera la autorizacion DCU 1106
	 * @param autorizacionCapitacion
	 * @throws IPSException
	 */
	public AutorizacionCapitacionDto generarAutorizacion(AutorizacionCapitacionDto autorizacionCapitacion)throws IPSException{
		IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo=new ProcesoGeneracionAutorizacionMundo();
		return procesoGeneracionAutorizacionMundo.generacionAutorizacion(autorizacionCapitacion, true);
	}
	
	/**
	 * Servicio que permite verificar los niveles de autorización de los Servicios/Medicamentos/Insumo de las ordenes
	 * versus los niveles de autorizacion del usuario
	 * @param autorizacionCapitacion
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<Integer> verificarNivelesAutorizacionOrdenes(List<OrdenAutorizacionDto> ordenesPorAutorizar, 
													List<NivelAutorizacionDto> nivelesAutorizacionUsuario)throws IPSException{
		INivelAutorizacionMundo nivelAutorizacionMundo=new NivelAutorizacionMundo();
		return nivelAutorizacionMundo.verificarNivelesAutorizacionOrdenes(ordenesPorAutorizar, nivelesAutorizacionUsuario, true, new AutorizacionCapitacionDto());
	}

	/**
	 * Consultar una solicitud dado su ID
	 * 
	 * @param numeroSolicitud
	 * @return solicitud consultada
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	public Solicitudes obtenerSolicitudPorId(int numeroSolicitud)throws IPSException
	{
		ISolicitudesMundo solicitudesMundo=new SolicitudesMundo();
	
		return solicitudesMundo.obtenerSolicitudPorId(numeroSolicitud);
	}

	/**
	 * Consultar una orden ambulatoria dado su codigo
	 * 
	 * @param codigoOrden
	 * @return ordenAmbultaria
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	public OrdenAmbulatoriaDto obtenerEstadoOrdenesAmbulatoriasPorId(long codigoOrden)throws IPSException{
		IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo=new OrdenesAmbulatoriasMundo();
		
		return ordenesAmbulatoriasMundo.obtenerEstadoOrdenesAmbulatoriasPorId(codigoOrden);
	}
	
	/**
	 * Consultar el estado de una peticion qx dado su codigo
	 * 
	 * @param codigoPeticion
	 * @return dtoPeticion
	 * @throws IPSException
	 * @author jeilones
	 * @created 17/08/2012
	 */
	public PeticionQxDto obtenerEstadoPeticionQxPorId(long codigoPeticion)throws IPSException
	{
		IPeticionesMundo peticionMundo=new PeticionesMundo();
		
		return peticionMundo.obtenerEstadoPeticionQxPorId(codigoPeticion);
	}
	
	/**
	 * Consulta las autorizaciones de entidad subcontratada de una autorizacion de capitacion que pertenece a un 
	 * ingreso estancia
	 * 
	 * @param idAutoIngEst
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @param requiereTransaccion 
	 * @created 3/09/2012
	 */
	public List<AutorizacionesEntidadesSub> obtenerAutorizacionEntSubDeIngEstancia(long idAutoIngEst, boolean requiereTransaccion)throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.obtenerAutorizacionEntSubDeIngEstancia(idAutoIngEst, requiereTransaccion);
	}
	/**
	 * Retorna una autorizacion de entidad subcontratada vs articulo
	 * 
	 * @param codigoAutoEntSub
	 * @param codigoArticulo
	 * @param requiereTransaccion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 22/08/2012
	 */
	public List<AutorizacionesEntSubArticu> obtenerAutorizacionEntSubArticulo(long codigoAutoEntSub,int codigoArticulo,boolean requiereTransaccion)throws IPSException
	{
		IAutorizacionCapitacionMundo autorizacionCapMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapMundo.obtenerAutorizacionEntSubArticulo(codigoAutoEntSub, codigoArticulo, requiereTransaccion);
	}
	
	/**
	 * Método que se encarga de obtener la descripcion del servicio y 
	 * el acronimo según la urgencia de tramite de la autorizacion
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 */
	public String[] obtenerDescripcionServicioAutorizar(
			int codigoServicio, int codigoTarifarioServicio,
			boolean requiereTransaccion) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.obtenerDescripcionServicioAutorizar(codigoServicio, codigoTarifarioServicio, requiereTransaccion);
	}
	
	/**
	 * Método que se encarga de obtener la descripcion del servicio y 
	 * el acronimo según la urgencia de tramite de la autorizacion
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 */
	public String[] obtenerDescripcionMedicamentoInsumoAutorizar(int codigoMedicamentoInsumo, int codigoTarifarioMedicamento, boolean requiereTransaccion) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.obtenerDescripcionMedicamentoInsumoAutorizar(codigoMedicamentoInsumo, codigoTarifarioMedicamento, requiereTransaccion);
	}
	
	/**
	 * Metodo que se encarga de verificar si se generara autorizacion de capitacion para las solicitudes
	 * @author Wilgomcr
	 * @param autorizacionCapitacionDto
	 * @return
	 * @throws IPSException
	 */
	public List<AutorizacionCapitacionDto> verificarGenerarAutorizacionCapitacion(AutorizacionCapitacionDto autorizacionCapitacionDto)throws IPSException{
		IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = new ProcesoGeneracionAutorizacionMundo();
		return procesoGeneracionAutorizacionMundo.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
	}

	/**
	 * Método que se encarga Valdiar la Anulación de la Autorización de Capitación para el Tipo de Solicitud.
	 * 1. Anular solicitud	2. Anular Autorizacion	3. Calcular cierre Anuladas 
	 * @author wilgomcr
	 * @param anulacionAutorizacionDto
	 * @param claseOrden
	 * @param tipoOrden
	 * @param tipoOrdenPyp
	 * @throws IPSException
	 */
	public List<AutorizacionCapitacionDto> validarAnulacionAutorizacionCapitaSolictud(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			int claseOrden, int tipoOrden, String tipoOrdenPyp,int institucion)throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.validarAnulacionAutorizacionCapitaSolictud(anulacionAutorizacionDto, claseOrden, tipoOrden, tipoOrdenPyp, institucion);
	}
	/**
	 * Actualiza el nivel de autorizacion una autorizacion de entidad subcontratada para servicios
	 * 
	 * @param autorizacionesEntSubServi
	 * @param nivelAutorizacion
	 * @param requiereTransaccion
	 * @throws IPSException
	 * @author jeilones
	 * @created 28/08/2012
	 */
	public void actualizarNivelAutorizacionAutoEntSubServicio(AutorizacionesEntSubServi autorizacionesEntSubServi,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		autorizacionCapitacionMundo.actualizarNivelAutorizacionAutoEntSubServicio(autorizacionesEntSubServi,nivelAutorizacion,requiereTransaccion);
	}
	
	/**
	 * Actualiza el nivel de autorizacion una autorizacion de entidad subcontratada para servicios
	 * 
	 * @param autorizacionesEntSubServi
	 * @param nivelAutorizacion
	 * @param requiereTransaccion
	 * @throws IPSException
	 * @author jeilones
	 * @created 28/08/2012
	 */
	public void actualizarNivelAutorizacionAutoEntSubArticulo(AutorizacionesEntSubArticu autorizacionesEntSubArticu,NivelAutorizacion nivelAutorizacion,boolean requiereTransaccion) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		autorizacionCapitacionMundo.actualizarNivelAutorizacionAutoEntSubArticulo(autorizacionesEntSubArticu,nivelAutorizacion,requiereTransaccion);
	}
	
	/**
	 * Método que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Convenio Paciente'.
	 *  
	 * @param autorizacionEntSubCapita
	 * @param con
	 * @throws IPSException
	 * @author jeilones
	 * @param requiereTransaccion 
	 * @created 30/08/2012
	 */
	public void generarAutorizacionServicioTemporal(AutorizacionCapitacionDto autorizacionEntSubCapita, boolean requiereTransaccion) throws IPSException{
		IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo=new ProcesoGeneracionAutorizacionMundo();
		procesoGeneracionAutorizacionMundo.generarAutorizacionServicioTemporal(autorizacionEntSubCapita, requiereTransaccion);
	}
	
	/**
	 * Se encarga de general el acumulado del cierre temporal de una autorizacion de capitacion temporal
	 * 
	 * @param autorizacionCapitacion
	 * @throws IPSException
	 * @author jeilones
	 * @param requiereTransaccion 
	 * @created 30/08/2012
	 */
	public void generarAcumuladoCierreTemporal(AutorizacionCapitacionDto autorizacionCapitacion, boolean requiereTransaccion) throws IPSException{
		IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo=new ProcesoGeneracionAutorizacionMundo();
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			procesoGeneracionAutorizacionMundo.generarAcumuladoCierreTemporal(autorizacionCapitacion);
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}catch (IPSException ipse) {			
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;			
		} 
		catch (Exception e){
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * Método que permite guardar los montos generados por la autorización de capitación subcontratada
	 * 
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 * @author jeilones
	 * @created 25/09/2012
	 */
	public void generarAutorizacionEntSubMontos(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,boolean requiereTransaccion) throws IPSException{
		IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo=new ProcesoGeneracionAutorizacionMundo();
		
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			procesoGeneracionAutorizacionMundo.generarAutorizacionEntSubMontos(autorizacionEntSubCapita, autorizacionesEntidadesSub);
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}catch (IPSException ipse) {			
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;			
		} 
		catch (Exception e){
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param listDtoAutorizacion
	 * @param errores
	 */
	public void obtenerMensajesError(List<AutorizacionCapitacionDto> listDtoAutorizacion, ActionMessages errores) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		autorizacionCapitacionMundo.obtenerMensajesError(listDtoAutorizacion, errores);
	}
	
	/**
	 * Servicio encargado de validar si un Medicamento/Insumo esta cubierto por el convenio capitado de la orden
	 * 
	 * @param claseOrden
	 * @param codigoConvenio
	 * @param codigoOrden
	 * @param codigoMedicamentoInsumo
	 * @return
	 * @throws IPSException
	 */
	public boolean tieneCoberturaMedicamentoInsumo(int claseOrden, int codigoConvenio, Long codigoOrden, int codigoMedicamentoInsumo) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.tieneCoberturaMedicamentoInsumo(claseOrden, codigoConvenio, codigoOrden, codigoMedicamentoInsumo);
	}
	
	/**
	 * Método encargado de realizar la validación de niveles de autorización
	 * para las autorizaciones de población capitada.
	 * Este metodo debe recibir un objeto AutorizacionCapitacionDto con la 
	 * Entidad SubContratada de la autorización y los datos del usuario que
	 * se requiere validar (login - codigoUsuario) y las ordenes asociadas
	 * a la autorización con sus respectivos servicios o medicamentos.
	 * Si el parámetro esTodosRequeridos viene en true todos los servicios o
	 * medicamentos deben pasar la validación, caso contrario se cancela el 
	 * proceso de validación. Las modificaciones son guardadas en el objeto
	 * autorizacionCapitacionDto y pasadas por referencia
	 * DCU - 1105
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 * @param esTodosRequeridos
	 * @param requiereTransaccion
	 * @throws IPSException
	 */
	public void validarNivelesAutorizacionAutorizacionesPoblacionCapitada(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos) throws IPSException {
		INivelAutorizacionMundo nivelAutorizacionMundo = new NivelAutorizacionMundo();
		nivelAutorizacionMundo.validarNivelesAutorizacionAutorizacionesPoblacionCapitada(
				autorizacionCapitacionDto, esTodosRequeridos, true);
	}
	
	/**
	 * Servicio encargado de verificar si para un servicio existe parámetrización de 
	 * unidad de consulta por centro de costo
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeCentroCostoParametrizadoPorUnidadConsulta(int codigoServicio, int codigoCentroCosto) throws IPSException{
		ICatalogoManejoPacienteMundo catalogoManejoPacienteMundo= new CatalogoManejoPacienteMundo();
		return catalogoManejoPacienteMundo.existeCentroCostoParametrizadoPorUnidadConsulta(codigoServicio, codigoCentroCosto);
	}
	
	/**
	 * Servicio encargado de verificar si para un grupo de servicio existe parámetrización de 
	 * grupo servicios por centro de costo
	 * @param codigoGrupoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeCentroCostoParametrizadoPorGrupoServicio(Integer codigoGrupoServicio, int codigoCentroCosto) throws IPSException{
		ICatalogoManejoPacienteMundo catalogoManejoPacienteMundo= new CatalogoManejoPacienteMundo();
		return catalogoManejoPacienteMundo.existeCentroCostoParametrizadoPorGrupoServicio(codigoGrupoServicio, codigoCentroCosto);
	}
	
	/**
	* Metodo de encargado de realizar la validacion para autorizaciones Capitación Subcontratada
	* Autorización Servicios Medicamentos de Ingreso Estancia 
	* En este no se tiene en cuenta la prioridad de los niveles de autorizacion DCU 1105
	* @author ginsotfu
	* @param autorizacionCapitacionDto
	* @param esTodosRequeridos
	* @param requiereTransaccion
	* @throws IPSException
	*
	*/
	public void validarNivelesAutorizacionAutorizacionesPoblacionCapitadaAutorServMedIngresoInstancia(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos) throws IPSException {
		INivelAutorizacionMundo nivelAutorizacionMundo = new NivelAutorizacionMundo();
		nivelAutorizacionMundo.validarNivelesAutorizacionAutorizacionesPoblacionCapitadaAutorServMedIngresoInstancia(
				autorizacionCapitacionDto, esTodosRequeridos, true);
	}
	
	/**
	 * Retorna los centros de atencion dependiendo su estado que tengan parametrizada una ciudad.
	 * @param Boolean estado. Si estado true retorna los activos, si estado false retorna los inactivos, si es null retorna todos los centros de atencion.
	 * @param boolean requiereTransaccion 
	 * @param int codigoInstitucion
	 * @return
	 * @throws IPSException
	 */
	public List<DtoCentrosAtencion> listarTodosCentrosAtencion(boolean requiereTransaccion, Boolean estado, int codigoInstitucion) throws IPSException {
		ICatalogoManejoPacienteMundo catalogoManejoPacienteMundo = new CatalogoManejoPacienteMundo();
		return catalogoManejoPacienteMundo.listarTodosCentrosAtencion(requiereTransaccion, estado, codigoInstitucion);
	}
	
	/**
	 * Consulta todos los servicios anulados de capitacion (Solicitud, Orden Ambulatoria o Peticion)
	 * @param consecutivo
	 * @param tarifario
	 * @param isMigrado
	 * @throws IPSException
	 * @author hermorhu
	 */
	public List<ServicioAutorizadoCapitacionDto> consultarServiciosAnuladosCapitacion(long consecutivo, long tarifario, boolean isMigrado) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.consultarServiciosAnuladosCapitacion(consecutivo, tarifario, isMigrado);
	}
	
	/**
	 * Consulta todos los articulos anulados de capitacion (Solicitud o Orden Ambulatoria)
	 * @param consecutivo
	 * @param institucion
	 * @param isMigrado
	 * @throws IPSException
	 */
	public List<ArticuloAutorizadoCapitacionDto> consultarArticulosAnuladosCapitacion(long consecutivo, int institucion, boolean isMigrado) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		
		return autorizacionCapitacionMundo.consultarArticulosAnuladosCapitacion(consecutivo,institucion,isMigrado);
	}
	
	/**
	* M�todo que evalua si una solicitud tiene asociadas autorizaciones de capitacion 
	* Generadas desde la Petici�n de Cirugia
	* @author ricruico
	* @param numeroSolicitud
	* @return existe
	* @throws IPSException
	*
	*/
	public boolean existeAutorizacionCapitacionGeneradaPorPeticion(int numeroSolicitud) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.existeAutorizacionCapitacionGeneradaPorPeticion(numeroSolicitud);
	}
	
	
	/**
	 * Metodo que Obtiene informacion relevante de un ingreso referente a su id 
	 * @param idIngreso
	 * @return
	 * @throws IPSException
	 * @author javrammo
	 */
	public InfoIngresoDto obtenerInfoIngreso(int idIngreso) throws IPSException {
		IIngresoMundo ingresoMundo = new IngresoMundo();
		return ingresoMundo.obtenerInfoIngreso(idIngreso);
	}
	
	/**
	 * Metodo encargado de consultar los datos de la entrega de la autorizacion
	 * @param idAutorizacionEntidadSub
	 * @return {@link AutorizacionEntregaDto}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 20-feb-2013 
	 */
	public AutorizacionEntregaDto consultarEntregaAutorizacionEntidadSubContratada (long idAutorizacionEntidadSub) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.consultarEntregaAutorizacionEntidadSubContratada(idAutorizacionEntidadSub);
	}
	
	/**
	 * Metodo encargado de guardar los datos de entrega de la autorizacion original
	 * @param autorizacionEntregaDto
	 * @return {@link Boolean}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public boolean guardarEntregaAutorizacionEntidadSubContratadaOriginal (AutorizacionEntregaDto autorizacionEntregaDto) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.guardarEntregaAutorizacionEntidadSubContratadaOriginal(autorizacionEntregaDto);
	} 
	
	/**
	 * Metodo encargado de consultar el id de la Autorizacion de Entidad Subcontratada
	 * apartir de el Consecutivo de Autorizacion de Entidad SubContratada
	 * @param consecutivoAutorizacion
	 * @return {@link Long}
	 * @throws IPSException
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public Long consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion (String consecutivoAutorizacion) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(consecutivoAutorizacion);
	}
	
	
	/**
	 * Metodo encargado de consulta y el paciente paga atenci�n o No 
	 * 
	 * @param codigoContrato
	 * @return Boolean
	 * @author sanbarga
	 * @throws IPSException 
	 * @created 12-Abr-2013
	 * MT6703
	 */
	public Boolean consultarSiPacientePagaAtencion(int codigoContrato, boolean requiereTransaccion) throws IPSException {
		
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.consultarSiPacientePagaAtencion(codigoContrato, requiereTransaccion);
	}
		
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
	public Boolean consultarSiConvenioManejaMontos(int codigoConvenio) throws IPSException {
		
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.consultarSiConvenioManejaMontos(codigoConvenio);
	}
		
		
		
		
	
	
}