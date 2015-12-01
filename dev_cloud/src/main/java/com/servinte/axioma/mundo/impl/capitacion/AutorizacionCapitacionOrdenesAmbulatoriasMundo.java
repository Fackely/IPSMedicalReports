package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.ordenes.OrdenesFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCierrePresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IGruposServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutoEntsubSolicitudesHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de las Autorizaciones de Capitación de Ordenes Ambulatorias
 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
 * 
 * @author Camilo Gómez
 */
public class AutorizacionCapitacionOrdenesAmbulatoriasMundo implements IAutorizacionCapitacionOrdenesAmbulatoriasMundo {

	
	
	IAutorizacionesEntidadesSubDAO autorizacionesEntidadesSubDAO;
	
	/**
	 * Método constructor de la clase
	 */
	public AutorizacionCapitacionOrdenesAmbulatoriasMundo(){
		autorizacionesEntidadesSubDAO = ManejoPacienteDAOFabrica.crearAutorizacionesEntidadesSubDAO();
	}
	
	
	/**
	 * Método que se encarga de validar la descripción del servicio de acuerdo a la información del campo
	 * Días Tramite Autorización Capitación de la funcionalidad de Grupos de Servicios
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * 
	 * @param dtoAutorizAmbulatoria
	 * @param institucion
	 * @return dtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria validarDescripcionServicio(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizAmbulatoria,InstitucionBasica institucion) 
	{
		int tipoTarifario=Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion.getCodigoInstitucionBasica()));		
		Log4JManager.info("tipoTarifario	-->"+tipoTarifario);
		Log4JManager.info("codigoServicio	-->"+dtoAutorizAmbulatoria.getCodigoServicioAutorizar());
		Log4JManager.info("numeroSolicitud	-->"+dtoAutorizAmbulatoria.getNumeroSolicitudAutorizar());
		
		IGruposServiciosMundo gruposServiciosMundo			=FacturacionFabricaMundo.crearGruposServiciosMundo();
		ISolicitudesMundo solicitudesMundo					=OrdenesFabricaMundo.crearSolicitudesMundo();
		IServiciosMundo serviciosMundo						=FacturacionFabricaMundo.crearServiciosMundo();
		IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo	=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo(); 
		GruposServicios gruposServicios						=new GruposServicios();
		Solicitudes solicitud								=null;
		ArrayList<DtoServicios>dtoServicios					=new ArrayList<DtoServicios>();
		DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias		=new DtoOrdenesAmbulatorias();
		ArrayList<DtoOrdenesAmbulatorias> listaOrdenesAmbul	=null;
		String nombreServicio="";
		boolean urgente=false;
		
		
		if(dtoAutorizAmbulatoria.isOrdenAmbulatoria())
		{
			dtoOrdenesAmbulatorias.setInstitucion(institucion.getCodigoInstitucionBasica());
			//consecutivoOrdenAmbulatoria	=new ArrayList<String>();
			listaOrdenesAmbul			=new ArrayList<DtoOrdenesAmbulatorias>();
			//consecutivoOrdenAmbulatoria.add(dtoAutorizAmbulatoria.getNumeroSolicitudAutorizar()+"");			
			//dtoOrdenesAmbulatorias.setParametrosOrdenesAmbulatorias(consecutivoOrdenAmbulatoria);
			dtoOrdenesAmbulatorias.setNumeroOrden(dtoAutorizAmbulatoria.getNumeroSolicitudAutorizar()+"");
			listaOrdenesAmbul=ordenesAmbulatoriasMundo.obtenerOrdenesAmbulatorias(dtoOrdenesAmbulatorias);
			
		}else{
			
			solicitud=new Solicitudes();
			solicitud=solicitudesMundo.obtenerSolicitudPorId(dtoAutorizAmbulatoria.getNumeroSolicitudAutorizar());
		}
		
		//Se setea el campo 'urgente' de la Solictud o de la Orden Ambulatoria  		
		if(solicitud!=null)
		{
			urgente=solicitud.isUrgente();
		}
		else if(!Utilidades.isEmpty(listaOrdenesAmbul))
		{
			for (DtoOrdenesAmbulatorias ordenAmbul : listaOrdenesAmbul) 
			{
				urgente=ordenAmbul.isUrgente();
			}
		}
		
		//Consulta la descripcion del servicio de acuerdo al tipoTarifario Oficial.(De la solicitud o de la orden ambulatoria)
		dtoServicios=serviciosMundo.obtenerServiciosXTipoTarifarioOficial(dtoAutorizAmbulatoria.getCodigoServicioAutorizar(), tipoTarifario);
		for (DtoServicios dtoServicio : dtoServicios) {
			nombreServicio=dtoServicio.getDescripcionServicio();
		}
		
		//Consulta el grupo de servicio al que pertence el servicio.
		gruposServicios=gruposServiciosMundo.buscarGrupoServicioPorServicio(dtoAutorizAmbulatoria.getCodigoServicioAutorizar());
		
		if(urgente)
		{
			String descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio);
			if(dtoAutorizAmbulatoria.isProcesoGeneracionAutoriz())
			{
				descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio)+
						(UtilidadTexto.isEmpty(gruposServicios.getAcroDiasUrgente())?"":" - "+gruposServicios.getAcroDiasUrgente());
				
			}else{
				descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio);
				if(gruposServicios.getNumDiasUrgente() != null){
					descripcionServicio+= " " + gruposServicios.getNumDiasUrgente().intValue()+" Días";
				}
			}
			dtoAutorizAmbulatoria.setNombreServicioAutorizar(descripcionServicio);
		}else{
			
			String descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio);
			if(dtoAutorizAmbulatoria.isProcesoGeneracionAutoriz())
			{
				descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio)+
						(UtilidadTexto.isEmpty(gruposServicios.getAcroDiasUrgente())?"":" - "+gruposServicios.getAcroDiasNormal());
				
			}else{
				descripcionServicio=(UtilidadTexto.isEmpty(nombreServicio)?"":nombreServicio);
				if(gruposServicios.getNumDiasNormal() != null){
					descripcionServicio+= " " +gruposServicios.getNumDiasNormal().intValue()+" Días";
				}
			}
			dtoAutorizAmbulatoria.setNombreServicioAutorizar(descripcionServicio);
		}
		
		return dtoAutorizAmbulatoria;
	}
	
	
	/**
	 * Método que se encarga de validar si la orden está asociada a una autorización de capitación subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionesOrdenAmbul(
			DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu)
	{
		//ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listaTempo=null;
		ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listaAutorizacion=null;
		dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden());
		listaAutorizacion	= consultarExisteAutorizacionCapitaOrdenAmbulatoria(dtoAutorizCapitaOrdenAmbu);
				
		if(!Utilidades.isEmpty(listaAutorizacion))
		{
			//listaAutorizacion	= new ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>();
			for (DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizacion : listaAutorizacion) {
				if(dtoAutorizacion.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado)){
					dtoAutorizacion.setExisteAutorizOrden(true);
		}
				if(dtoAutorizacion.getCodigoCentroAtencionAutoriz().equals(dtoAutorizacion.getCodigoCentroAtencionPaciente())){
					dtoAutorizacion.setCentroAtencionCorresponde(true);
	}
				//listaAutorizacion.add(dtoAutorizacion);
			}
		}
		return listaAutorizacion;
	}
	
	/**
	 * Método que se encarga de anular la autorización cuando la Orden Ambulatoria es anulada
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria anularAutorizacionOrdenAnulada(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu)
	{
			
		if(dtoAutorizCapitaOrdenAmbu.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
		{	//Se actualiza datos de anulación de la autorización
			
			AutorizacionesEntidadesSub autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
			autorizacionesEntidadesSub = autorizacionesEntidadesSubDAO.obtenerAutorizacionesEntidadesSubPorId(dtoAutorizCapitaOrdenAmbu.getConsecutivoAutorEntSub());
			
				Usuarios usuarios=new Usuarios();
				usuarios.setLogin(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getUsuario());
				autorizacionesEntidadesSub.setUsuarios(usuarios);
				autorizacionesEntidadesSub.setFechaAnulacion(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getFechaAnulacion());
				autorizacionesEntidadesSub.setHoraAnulacion(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getHoraAnulacion());
				autorizacionesEntidadesSub.setMotivoAnulacion(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getMotivoAnulacion());
				autorizacionesEntidadesSub.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
				autorizacionesEntidadesSubDAO.actualizarAutorizacionEntidadSub(autorizacionesEntidadesSub); 
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(UtilidadFecha.getFechaActualTipoBD());
			int mesActual=calendar.get(Calendar.MONTH);
			int anioActual=calendar.get(Calendar.YEAR);
			
			calendar.setTime(dtoAutorizCapitaOrdenAmbu.getFechaAutorizacion());
			int mesAutori=calendar.get(Calendar.MONTH);
			int anioAutori=calendar.get(Calendar.YEAR);
			
			//Se verifica si la Autorización corresponde con el periodo actual,si la Fecha de Autorización corresponde al mismo mes/año del sistema
			if((mesActual+"/"+anioActual).equals(mesAutori+"/"+anioAutori))
			{//Se actualizan los Procesos de Presupuesto de Capitación(Tarifas y cantidades)
				//Se realiza nuevamente el proceso de cierre
				IProcesoCierrePresupuestoMundo procesoCierrePresupuestoMundo=CapitacionFabricaMundo.crearProcesoCierrePresupuestoMundo();
				DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado	=new DtoProcesoPresupuestoCapitado();
					dtoProcesoPresupuestoCapitado.setFechaInicio(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getFechaAnulacion());
					dtoProcesoPresupuestoCapitado.setFechaFin(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getFechaAnulacion());
					dtoProcesoPresupuestoCapitado.setConvenio(dtoAutorizCapitaOrdenAmbu.getConvenio());
					dtoProcesoPresupuestoCapitado.setInstitucion(dtoAutorizCapitaOrdenAmbu.getInstitucion());
				procesoCierrePresupuestoMundo.generarCierrePresupuesto(dtoProcesoPresupuestoCapitado);
			}
		}
		
		return dtoAutorizCapitaOrdenAmbu;
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Orden Ambulatoria a la Autorización de Capitación
	 * que ya existe de la Orden Ambulatoria
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAsociaSoliciAutorizOrdenAmbu
	 */
	public void asociarSolicitudAutorizacionOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAsociaSoliciAutorizOrdenAmbu)
	{

		try {
			AutoEntsubSolicitudesHome autoEntsubSolicitudesHome	=new AutoEntsubSolicitudesHome();
			AutoEntsubSolicitudes autoEntsubSolicitudes			=new AutoEntsubSolicitudes();

			Solicitudes solicitudes=new Solicitudes();
			solicitudes.setNumeroSolicitud(dtoAsociaSoliciAutorizOrdenAmbu.getNumeroSolicitudAutorizar());
			autoEntsubSolicitudes.setSolicitudes(solicitudes);

			AutorizacionesEntidadesSub autorizacionesEntidadesSub=new AutorizacionesEntidadesSub();
			autorizacionesEntidadesSub.setConsecutivo(dtoAsociaSoliciAutorizOrdenAmbu.getConsecutivoAutorEntSub());
			autoEntsubSolicitudes.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);

			autoEntsubSolicitudesHome.attachDirty(autoEntsubSolicitudes);

		} catch (Exception e) {
			Log4JManager.error("Error actualizando autorizaciones_ent_sub"+e);
		}
	}
	
	
	
	
	/**
	 * Hace el llamado al método que verifica si existe una autorización asociada.
	 * Para este flujo (SolicitarAction y SolicitarForm) se está tomando mal el codigo de la orden ya que se está
	 * utilizando para todas las consultas es consecutivo. Lo que se hace en este metodo es que se obtiene el código 
	 * de la orden a partir del consecutivo mientras se hace la consulta y despues se deja todo como venia
	 * 
	 * MT: 1707
	 * 
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 *
	 * @autor Cristhian Murillo
	 */
	private ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> consultarExisteAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu)
	{
		ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>listaAutorizaciones=null;
		/* Se obtiene el código de la orden a partir del consecutivo mientras se hace la consulta y despues se deja todo como venia */
		try {
			IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo	=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();		
			ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias> listaOrdenesAmbulatorias = new  ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias>(); 
			String numeroOrdenTemp = dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden();
			DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias = new DtoOrdenesAmbulatorias();
			dtoOrdenesAmbulatorias.setConsecutivoOrden(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden());
			listaOrdenesAmbulatorias = ordenesAmbulatoriasMundo.buscarPorParametros(dtoOrdenesAmbulatorias);
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(listaOrdenesAmbulatorias.get(0).getCodigo()+"");
			dtoAutorizCapitaOrdenAmbu.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);

			listaAutorizaciones = ordenesAmbulatoriasMundo.existeAutorizacionCapitaOrdenAmbulatoria(dtoAutorizCapitaOrdenAmbu);

			if(!Utilidades.isEmpty(listaAutorizaciones)){
				for (DtoAutorizacionCapitacionOrdenAmbulatoria ordenesAmbulatoria : listaAutorizaciones) {
					ordenesAmbulatoria.getDtoOrdenesAmbulatorias().setNumeroOrden(numeroOrdenTemp);
					//dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(numeroOrdenTemp);
				}
			}
		} catch (Exception e) {
			Log4JManager.error("Existen Autorizaciones Capitacion Orden Ambulatoria: " + e);
		}
		return listaAutorizaciones;
	}
	
}
