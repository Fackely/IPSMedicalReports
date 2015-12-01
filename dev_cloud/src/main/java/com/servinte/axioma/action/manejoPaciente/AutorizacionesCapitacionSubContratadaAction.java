package com.servinte.axioma.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.capitacion.ConstantesCapitacion;
import util.facturacion.InfoCobertura;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.actionForm.manejoPaciente.AutorizacionesCapitacionSubContratadaForm;
import com.servinte.axioma.bl.capitacion.facade.CapitacionFacade;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.util.facade.UtilidadesFacade;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.DetalleServicioDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ClaseOrdenDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;


/**
 * Clase encargada de manejar la lógica web de la funcionalidad de Autorizaciones
 * Manuales de Capitación Sub Contratada
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class AutorizacionesCapitacionSubContratadaAction extends DispatchAction{

	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	
	private static final String KEY_ERROR_REQUIRED="errors.required";
	
	private static final String KEY_ERROR_CAST="errors.castForm";
	
	private static final String KEY_ERROR_FECHA_VENCIMIENTO="errors.autorizacion.fechaVencimientoMenorActual";
	
	private static final String KEY_SESSION_USUARIO="usuarioBasico";
	
	private static final String KEY_SESSION_PACIENTE="pacienteActivo";
	
	private static final String FORWARD_POR_PACIENTE="ordenesPorPaciente";
	
	private static final String FORWARD_POR_RANGO="ordenesPorRango";
	
	private static final String FORWARD_DETALLE="detalleOrden";
	
	private static final String FORWARD_SECCION_AUTORIZACION="detalleAutorizacion";
	
	private static final int NUM_DIAS_MAXIMO=30;
	
	private static final String FORWARD_POPUP_PRIMERA_IMPRESION = "mostrarPopUpPrimeraImpresion";
	
	private static final String PROPERTIES_AUTORIZACION_CAPITACION_SUBCONTRATADA = "com.servinte.mensajes.manejoPaciente.AutorizacionesCapitacionSubcontratadaForm";
	
	private static final String FORWARD_RESUMEN_AUTORIZACION = "resumenAutorizacion";
	
	private static final String KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE = "errores.manejoPaciente.parametroGeneral";
	
	/**
	 * Método que implementa la lógica web al momento de generar la autorización
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionAutorizar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		String forward="resumenAutorizacion";
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.setMostrarReporte(false);
			forma.setNombreReporte(null);
			//Se validan los campos requeridos de la forma
			this.validarForma(forma, errores);
			if(errores.isEmpty()){
				//Se asigna el nombre del convenio recobro
				if(forma.getAutorizacionCapitacion().isConvenioRecobro() &&
						forma.getAutorizacionCapitacion().getCodConvenioRecobro() != null
							&& forma.getAutorizacionCapitacion().getCodConvenioRecobro().intValue() != 0){
						for(ConvenioDto dtoConvenio:forma.getListaConvenios()){
							if(forma.getAutorizacionCapitacion().getCodConvenioRecobro().intValue()==dtoConvenio.getCodigo()){
								forma.getAutorizacionCapitacion().setDescripcionConvenioRecobro(dtoConvenio.getNombre());
								break;
							}
						}
				}
				ManejoPacienteFacade manejoPacienteFacade= new ManejoPacienteFacade();
				forma.getAutorizacionCapitacion().setOrdenesAutorizar(forma.getOrdenesPorAutorizar());
				forma.getAutorizacionCapitacion().setFechaAutorizacion(Calendar.getInstance().getTime());
				UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
				forma.getAutorizacionCapitacion().setCentroAtencion(usuario.getCodigoCentroAtencion());
				//Se asigna el centro de costo seleccionado a cada una de las ordenes de la autorizacion
				for(OrdenAutorizacionDto dtoOrden:forma.getAutorizacionCapitacion().getOrdenesAutorizar()){
					if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria
							|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
						dtoOrden.setCodigoCentroCostoEjecuta(forma.getCodigoCentroCosto());
						dtoOrden.setTipoEntidadEjecuta(forma.getTipoEntidadEjecuta());
					}
				}
				AutorizacionCapitacionDto dtoAutorizacion=manejoPacienteFacade.generarAutorizacion(forma.getAutorizacionCapitacion());
				//Se verifica si el proceso fue exitoso o no
				if(dtoAutorizacion.isProcesoExitoso()){
					//Si el proceso fue exitoso se verifica si se deben verificar los detalles
					//de la(s) orden(es) lo cual quiere decir que se realizó autorización pero que algunos 
					//Servicios/Medicamentos insumos NO se pudieron Autorizar
					//Si no se debe verificar detalle quiere decir que todos los Servicios/Medicamentos insumos
					//que se mandaron autorizar fue autorizados
					if(dtoAutorizacion.isVerificarDetalleError()){
						this.obtenerMensajesError(dtoAutorizacion, errores);
					}
					StringBuffer descripcionMonto= new StringBuffer();
						
					if(dtoAutorizacion.getMontoCobroAutorizacion() != null
							&& dtoAutorizacion.getMontoCobroAutorizacion().getTipoMontoNombre() != null){
						descripcionMonto.append(dtoAutorizacion.getMontoCobroAutorizacion().getTipoMontoNombre());
						descripcionMonto.append(" ");
					}
					//Si la autorización es temporal no se calcula el monto por tanto no se muestra este valor MT 5181
					if (dtoAutorizacion.getMontoCobroAutorizacion().getValorMontoCalculado()!=null){
						descripcionMonto.append(dtoAutorizacion.getMontoCobroAutorizacion().getValorMontoCalculado().toString());
					}
					dtoAutorizacion.getMontoCobroAutorizacion().setDescripcionMonto(descripcionMonto.toString());
					forma.setAutorizacionCapitacion(dtoAutorizacion);
				}
				else{
					if(dtoAutorizacion.isVerificarDetalleError()){
						this.obtenerMensajesError(dtoAutorizacion, errores);
					}
					else{
						if(dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg() != null
								&& dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg().length > 0){
							errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey(), dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg()));
						}
						else{
							errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
						}
					}
					forward=FORWARD_DETALLE;
				}
			}
			else{
				forward=FORWARD_DETALLE;
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
			forward=FORWARD_DETALLE;
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			forward=FORWARD_DETALLE;
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
			forward=FORWARD_DETALLE;
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(forward);
	}
	
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void obtenerMensajesError(AutorizacionCapitacionDto dtoAutorizacion, ActionMessages errores){
		for(OrdenAutorizacionDto dtoOrden:dtoAutorizacion.getOrdenesAutorizar()){
			if(dtoOrden.getServiciosPorAutorizar() != null
					&& !dtoOrden.getServiciosPorAutorizar().isEmpty()){
				for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
					if(dtoServicio.isAutorizar() && !dtoServicio.isAutorizado()){
						if(errores.isEmpty()){
							errores.add("", new ActionMessage("errors.autorizacion.messageGeneral"));
						}
						if(dtoServicio.getMensajeError().getParamsMsg() != null 
								&& dtoServicio.getMensajeError().getParamsMsg().length > 0){
							errores.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey(),dtoServicio.getMensajeError().getParamsMsg()));
						}
						else{
							errores.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey()));
						}
					}
				}
			}
			else if(dtoOrden.getMedicamentosInsumosPorAutorizar() != null
					&& !dtoOrden.getMedicamentosInsumosPorAutorizar().isEmpty()){
				for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
					if(dtoMedicamento.isAutorizar() && !dtoMedicamento.isAutorizado()){
						if(errores.isEmpty()){
							errores.add("", new ActionMessage("errors.autorizacion.messageGeneral"));
						}
						if(dtoMedicamento.getMensajeError().getParamsMsg() != null
								&& dtoMedicamento.getMensajeError().getParamsMsg().length > 0){
							errores.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey(), dtoMedicamento.getMensajeError().getParamsMsg()));
						}
						else{
							errores.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Método que valida los campos requeridos antes de permitir
	 * generar una autorización
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void validarForma(AutorizacionesCapitacionSubContratadaForm forma, ActionMessages errores){
		//Se valida si la información de el codigo entidad es Otra pra verificar los campos requeridos
		if(forma.getCodigoCentroCosto() < 0){
			if(forma.isAutorizacionServicios()){
				errores.add("codigoEntidadSubcontratada", new ActionMessage(KEY_ERROR_REQUIRED, "Centro Costo Responde"));
			}
			else{
				errores.add("codigoEntidadSubcontratada", new ActionMessage(KEY_ERROR_REQUIRED, "Farmacia de Entrega"));
			}
		}
		//Se valida si la información de convenio recobro fue diligenciada
		if(forma.getAutorizacionCapitacion().isConvenioRecobro()){
			//Se valida que hayan seleccionado convenio recobro
			if(forma.getAutorizacionCapitacion().getCodConvenioRecobro() != null
					&& forma.getAutorizacionCapitacion().getCodConvenioRecobro().intValue() != ConstantesBD.codigoNuncaValido){
				//Se valida que si seleccionaron convenio OTRO hayan ingresado información en
				//la descripcion del convenio
				if(forma.getAutorizacionCapitacion().getCodConvenioRecobro().intValue() == 0
						&& (forma.getAutorizacionCapitacion().getDescripcionConvenioRecobro() == null
								|| forma.getAutorizacionCapitacion().getDescripcionConvenioRecobro().trim().isEmpty())){
					errores.add("descripcionConvenioRecobro", new ActionMessage(KEY_ERROR_REQUIRED, "Descripción Entidad a Recobrar"));
				}
			}
			else{
				errores.add("codigoConvenioRecobro", new ActionMessage(KEY_ERROR_REQUIRED, "Entidad a Recobrar"));
			}
		}
		
		if(forma.getTipoEntidadEjecuta() == null || forma.getTipoEntidadEjecuta().trim().isEmpty()){
			errores.add("codigoEntidadSubcontratada", new ActionMessage(KEY_ERROR_REQUIRED, "Tipo Entidad a la que se Autoriza"));
		}
		if(forma.getCodigoEntidadSubcontratada() < 0){
			errores.add("codigoEntidadSubcontratada", new ActionMessage(KEY_ERROR_REQUIRED, "Entidad Subcontratada"));
		}
		else{
			if(forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getRazonSocial() == null
					|| forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getRazonSocial().trim().isEmpty()){
				errores.add("autorizacionCapitacion.entidadSubAutorizarCapitacion.razonSocial", new ActionMessage(KEY_ERROR_REQUIRED, "Descripción Entidad"));
			}
			if(forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getDireccionEntidad() == null
					|| forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getDireccionEntidad().trim().isEmpty()){
				errores.add("autorizacionCapitacion.entidadSubAutorizarCapitacion.direccionEntidad", new ActionMessage(KEY_ERROR_REQUIRED, "Dirección Entidad"));
			}
			if(forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getTelefonoEntidad() == null
					|| forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getTelefonoEntidad().trim().isEmpty()){
				errores.add("autorizacionCapitacion.entidadSubAutorizarCapitacion.telefonoEntidad", new ActionMessage(KEY_ERROR_REQUIRED, "Teléfono Entidad"));
			}
			if(forma.getCodigoEntidadSubcontratada() == 0){
				if(forma.getAutorizacionCapitacion().getObservacionesGenerales() == null
						|| forma.getAutorizacionCapitacion().getObservacionesGenerales().trim().isEmpty()){
					errores.add("autorizacionCapitacion.observacionesGenerales", new ActionMessage(KEY_ERROR_REQUIRED, "Observaciones Generales"));
				}
			}
			else{
				//Se valida si la entidad subcontratada seleccionada tiene prioridad diferente de 1
				//validar que las observaciones generales se haya diligenciado
				if(forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion() != null
						&& forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getNumeroPrioridad() != 1
						&& (forma.getAutorizacionCapitacion().getObservacionesGenerales() == null
							|| forma.getAutorizacionCapitacion().getObservacionesGenerales().trim().isEmpty())){
						errores.add("autorizacionCapitacion.observacionesGenerales", new ActionMessage(KEY_ERROR_REQUIRED, "Observaciones Generales"));
				}
			}
		}
		
		if(forma.getAutorizacionCapitacion().getObservacionesGenerales() != null
				&& !forma.getAutorizacionCapitacion().getObservacionesGenerales().trim().isEmpty()
				&& forma.getAutorizacionCapitacion().getObservacionesGenerales().length() > 1000){
			errores.add("autorizacionCapitacion.observacionesGenerales", new ActionMessage("errors.tamanio", "Observaciones Generales", "1000"));
		}
		
		String label="Fecha Vencimiento Medicamentos/Insumos";
		if(forma.isAutorizacionServicios()){
			label="Fecha Vencimiento Servicios";
		}
		if(forma.getAutorizacionCapitacion().getFechaVencimientoAutorizacion() == null){
			errores.add("autorizacionCapitacion.fechaVencimientoAutorizacion", new ActionMessage(KEY_ERROR_REQUIRED, label));
		}
		else{
			Calendar fechaActual = Calendar.getInstance();
			if(!forma.getAutorizacionCapitacion().getFechaVencimientoAutorizacion().after(fechaActual.getTime())){
				errores.add("autorizacionCapitacion.fechaVencimientoAutorizacion", new ActionMessage(KEY_ERROR_FECHA_VENCIMIENTO, label));
			}
		}
		//Se valida que se haya seleccionado por lo menos un servicio o medicamento insumo
		boolean valido=false;
		for(OrdenAutorizacionDto dtoOrden:forma.getOrdenesPorAutorizar()){
			if(valido){
				break;
			}
			if(forma.isAutorizacionServicios()){
				for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
					if(dtoServicio.isAutorizar()){
						valido=true;
						break;
					}
				}
			}
			else{
				for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
					if(dtoMedicamento.isAutorizar()){
						valido=true;
						break;
					}
				}
			}
		}
		if(!valido){
			errores.add("", new ActionMessage("errors.autorizacion.seleccionServicioMedicamento"));
		}
		//Se valida que si la orden o peticion o cargo directo es de cirugía se hayan seleccionado
		//todos los servicios de cirugia
		valido=true;
		for(OrdenAutorizacionDto dtoOrden:forma.getOrdenesPorAutorizar()){
			if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion || dtoOrden.getTipoOrden()==ConstantesBD.codigoTipoSolicitudCirugia){
				for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
					if(!dtoServicio.isAutorizar()){
						valido=false;
						break;
					}
				}
			}
		}
		if(!valido){
			errores.add("", new ActionMessage("errors.autorizacion.seleccionServiciosCirugia"));
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionDetalleOrden(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.setOrdenesPorAutorizar(new ArrayList<OrdenAutorizacionDto>());
			for(OrdenAutorizacionDto dtoOrden: forma.getOrdenesPorAutorizarPaciente()){
				if(dtoOrden.getCodigoOrden().equals(forma.getCodigoOrden())){
					forma.getOrdenesPorAutorizar().add(dtoOrden);
					break;
				}
			}
			forma.setAutorizacionMultiple(false);
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward(FORWARD_POR_PACIENTE);
		}
		return initDetalleAutorizacion(mapping, form, request, response);
	}

	/**
	 * Método encargado de realizar la lógica web para la action de redireccionar
	 * al detalle por paciente
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionDetallePaciente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			PersonaBasica paciente 	= (PersonaBasica)request.getSession().getAttribute(KEY_SESSION_PACIENTE);
			forma.setFromPacienteORango(true);
			//Se limpia la lista de ordenes por rango para liberar memoria
			forma.getOrdenesPorAutorizarRangos().clear();
			//Se carga la información del paciente para subirla a session
			UtilidadesFacade utilidadFacade = new UtilidadesFacade();
			paciente=utilidadFacade.cargarPaciente(forma.getCodigoPaciente(), usuario.getCodigoInstitucionInt(),
																	usuario.getCodigoCentroAtencion());
			request.getSession().setAttribute(KEY_SESSION_PACIENTE,paciente);
			//Se implemeta el patrón observable para que el paciente
			// pueda ser visto por todos los usuario en la aplicacion
			ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
			if (observable != null) {
				synchronized (observable) {
					observable.setChanged();
					observable.notifyObservers(Integer.valueOf(paciente.getCodigoPersona()));
				}
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward(FORWARD_POR_RANGO);
		}
		return this.initOrdenesPorPaciente(mapping, form, request, response);
	}

	/**
	 * Método que implementa la lógica web para la impresión de la autorización de capitación
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionImprimirAutorizacion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		ManejoPacienteFacade manejoPacienteFacade = null;
		AutorizacionEntregaDto autorizacionEntregaDto = null;
		Long idAutorizacionEntSub = null;
		
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			//Si existe 'Autorizacion de Entidad Subcontratada' se debe validar que existan los datos de entrega de autorizacion
			if(forma.getAutorizacionCapitacion().getConsecutivoAutorizacion() != null && (!forma.getAutorizacionCapitacion().getConsecutivoAutorizacion().isEmpty() && !forma.getAutorizacionCapitacion().getConsecutivoAutorizacion().equals(String.valueOf(ConstantesBD.codigoNuncaValido)))) {
				
				idAutorizacionEntSub = manejoPacienteFacade.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(forma.getAutorizacionCapitacion().getConsecutivoAutorizacion());
				
				autorizacionEntregaDto = manejoPacienteFacade.consultarEntregaAutorizacionEntidadSubContratada(idAutorizacionEntSub);
				forma.getAutorizacionCapitacion().setAutorizacionEntrega(autorizacionEntregaDto);
			}
			
			String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
			if(tipoFormatoImpresion != null && !tipoFormatoImpresion.trim().isEmpty()){
				
				//hermorhu - MT5966
				//Si existe Autorizacion de Entidad Subcontratada
				if(forma.getAutorizacionCapitacion().getConsecutivoAutorizacion() != null && (!forma.getAutorizacionCapitacion().getConsecutivoAutorizacion().isEmpty() && !forma.getAutorizacionCapitacion().getConsecutivoAutorizacion().equals(String.valueOf(ConstantesBD.codigoNuncaValido)))) {
					//Si no existe la Entrega de la Autorizacion se muestra popup para ingresar datos de entrega
					if(forma.getAutorizacionCapitacion().getAutorizacionEntrega() == null) {
						forma.setMostrarPopupPrimeraImpresion(true);
						forma.setEsImpresionOriginal(true);
					} else {
						//No se muestra popup de primera impresion
						forma.setMostrarPopupPrimeraImpresion(false);
						//Si existe la Entrega de la autorizacion se imprime el label 'Copia'
						forma.setEsImpresionOriginal(false);
						
//						accionImprimirAutorizacion(con, forma,usuario,paciente, request);
						if(forma.isAutorizacionServicios()){
							this.generarReporteAutorizacionFormatoEstandarServicio(forma, usuario, institucion);
						} else {
							this.generarReporteAutorizacionFormatoEstandarArticulo(forma, usuario, institucion);
						}
					}
				} else {
					//No se muestra popup de primera impresion
					forma.setMostrarPopupPrimeraImpresion(false);
					//Si existe la Entrega de la autorizacion se imprime el label 'Copia'
					forma.setEsImpresionOriginal(false);
					
//					imprimirAutorizacion(con, forma, usuario, paciente, institucion, mapping, request);
					if(forma.isAutorizacionServicios()) {
						this.generarReporteAutorizacionFormatoEstandarServicio(forma, usuario, institucion);
					} else {
					this.generarReporteAutorizacionFormatoEstandarArticulo(forma, usuario, institucion);
				}
			}

			}
			else{
				MessageResources mensajes = MessageResources.getMessageResources(PROPERTIES_AUTORIZACION_CAPITACION_SUBCONTRATADA);	
				errores.add("", new ActionMessage(KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE, mensajes.getMessage("AutorizacionesCapitacionSubcontratadaForm.errorDefinirFormatoImpresion")));
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_RESUMEN_AUTORIZACION);
	}

	
	/**
	 * Método encargado de Generar el reporte de la autorización de capitación de Medicamentos/Insumo 
	 *
	 * @param forma
	 * @param usuario
	 * @param institucion
	 */
	private void generarReporteAutorizacionFormatoEstandarArticulo(AutorizacionesCapitacionSubContratadaForm forma, UsuarioBasico usuario,
    																	InstitucionBasica institucion)
    {
    	
    	String nombreReporte = "AUTORIZACION CAPITACION SUBCONTRATADA";
		String nombreArchivo = "";
		DtoGeneralReporteArticulosAutorizados dtoReporte = new DtoGeneralReporteArticulosAutorizados();
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoInfoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
		GeneradorReporteFormatoCapitacionAutorArticulos generadorReporte = new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporte);
		String infoEncabezadoPagina = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		if(reporteMediaCarta== null || reporteMediaCarta.trim().isEmpty()){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}
		
		AutorizacionCapitacionDto dtoAutorizacion=forma.getAutorizacionCapitacion();	
		
		dtoPaciente.setNombrePaciente(dtoAutorizacion.getDatosPacienteAutorizar().getNombresPaciente());
		dtoPaciente.setTipoDocPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getTipoIdPaciente());
		dtoPaciente.setNumeroDocPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getNumIdPaciente());
		dtoPaciente.setTipoContratoPaciente(dtoAutorizacion.getOrdenesAutorizar().get(0).getContrato().getConvenio().getNombreTipoContrato());
		dtoPaciente.setEdadPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getEdadPaciente());
		dtoPaciente.setConvenioPaciente(dtoAutorizacion.getOrdenesAutorizar().get(0).getContrato().getConvenio().getNombre());
		dtoPaciente.setCategoriaSocioEconomica(dtoAutorizacion.getDatosPacienteAutorizar().getNombreClasificacionSocieconomica());
		if(dtoAutorizacion.isConvenioRecobro()){
			dtoPaciente.setRecobro(ConstantesBD.acronimoSi);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getDescripcionConvenioRecobro());
		}
		else{
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getDescripcionConvenioRecobro());
		}
		dtoPaciente.setTipoAfiliado(dtoAutorizacion.getDatosPacienteAutorizar().getNombreTipoAfiliado());
		dtoPaciente.setMontoCobro(dtoAutorizacion.getMontoCobroAutorizacion().getDescripcionMonto());
		
		dtoInfoAutorizacion.setNumeroAutorizacion(dtoAutorizacion.getConsecutivoAutorizacion());
		dtoInfoAutorizacion.setEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getRazonSocial());
		dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getDireccionEntidad());
		dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getTelefonoEntidad());
		dtoInfoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
		dtoInfoAutorizacion.setUsuarioAutoriza(dtoAutorizacion.getLoginUsuario());
		SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
		dtoInfoAutorizacion.setFechaAutorizacion(format.format(dtoAutorizacion.getFechaAutorizacion()));
		dtoInfoAutorizacion.setFechaVencimiento(format.format(dtoAutorizacion.getFechaVencimientoAutorizacion()));
		String nombreEstado = (String)ValoresPorDefecto.getIntegridadDominio(dtoAutorizacion.getEstadoAutorizacion());
		dtoInfoAutorizacion.setEstadoAutorizacion(nombreEstado);
		dtoReporte.setObservaciones(dtoAutorizacion.getObservacionesGenerales());
		
		dtoReporte.setDatosEncabezado(infoEncabezadoPagina);
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setDtoPaciente(dtoPaciente);
		dtoReporte.setDtoAutorizacion(dtoInfoAutorizacion);
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit(institucion.getNit());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setDireccion(institucion.getDireccion());
		dtoReporte.setTelefono(institucion.getTelefono());
		StringBuilder nombreUsuario= new StringBuilder();
		nombreUsuario.append(usuario.getNombreUsuario());
		nombreUsuario.append(" (");
		nombreUsuario.append(usuario.getLoginUsuario());
		nombreUsuario.append(")");
		dtoReporte.setUsuario(nombreUsuario.toString());
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
			 			     					
		ArrayList<DtoArticulosAutorizaciones> listaMedicamentosInsumo = new ArrayList<DtoArticulosAutorizaciones>();
		for(OrdenAutorizacionDto dtoOrden:dtoAutorizacion.getOrdenesAutorizar()){
			for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
				if(dtoMedicamento.isAutorizado()){
					DtoArticulosAutorizaciones medicamentoAutorizado= new DtoArticulosAutorizaciones();
					medicamentoAutorizado.setNumeroOrden(Integer.valueOf(dtoOrden.getConsecutivoOrden()));
					medicamentoAutorizado.setFechaOrden(dtoOrden.getFechaOrden());
					medicamentoAutorizado.setCodigoArticulo(Integer.valueOf(dtoMedicamento.getCodigoPropietario()));
					StringBuffer descripcionArticulo=new StringBuffer();
					descripcionArticulo.append(dtoMedicamento.getDescripcion());
					descripcionArticulo.append(" ");
					if(dtoMedicamento.getConcentracion() != null){
						descripcionArticulo.append(dtoMedicamento.getConcentracion());
						descripcionArticulo.append(" ");
					}
					if(dtoMedicamento.getFormaFarmaceutica() != null){
						descripcionArticulo.append(dtoMedicamento.getFormaFarmaceutica());
						descripcionArticulo.append(" ");
					}
					if(dtoMedicamento.getUnidadMedida() != null){
						descripcionArticulo.append(dtoMedicamento.getUnidadMedida());
					}
					medicamentoAutorizado.setDescripcionArticulo(descripcionArticulo.toString());
					medicamentoAutorizado.setNaturalezaArticulo(dtoMedicamento.getNombreNaturaleza());
					medicamentoAutorizado.setCantidadSolicitada(dtoMedicamento.getCantidad().intValue());
					StringBuffer diagnostico=new StringBuffer();
					if(dtoMedicamento.getAcronimoDx() != null && dtoMedicamento.getTipoCieDx() != null){
						diagnostico.append(dtoMedicamento.getAcronimoDx());
						diagnostico.append(" - ");
						diagnostico.append(dtoMedicamento.getTipoCieDx());
					}
					medicamentoAutorizado.setDiagnostico(diagnostico.toString());
					listaMedicamentosInsumo.add(medicamentoAutorizado);
				}
			}
		}
		dtoReporte.setListaArticulos(listaMedicamentosInsumo);
		
		//hermorhu - MT5966
		//Dependiendo el valor de 'esImpresionOriginal' se envia al reporte el tipo de impresion
		if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == true) {
			dtoReporte.setTipoImpresion("Original");
			dtoReporte.setFechaHoraEntrega("");
		} else if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == false) {
			dtoReporte.setTipoImpresion("Copia");
			dtoReporte.setFechaHoraEntrega("Fecha Entrega: "+ forma.getAutorizacionCapitacion().getAutorizacionEntrega().getFechaEntrega() +" "+forma.getAutorizacionCapitacion().getAutorizacionEntrega().getHoraEntrega());
		} else {
			dtoReporte.setTipoImpresion("");
		}
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		if(!nombreArchivo.trim().isEmpty()){
			forma.setNombreReporte(nombreArchivo);
			forma.setMostrarReporte(true);
		}
    }
	
	
	/**
	 * Método encargado de Generar el reporte de la autorización de capitación de Medicamentos/Insumo 
	 *
	 * @param forma
	 * @param usuario
	 * @param institucion
	 */
	private void generarReporteAutorizacionFormatoEstandarServicio(AutorizacionesCapitacionSubContratadaForm forma, UsuarioBasico usuario,
																		InstitucionBasica institucion)
    {
		String nombreReporte = "AUTORIZACION CAPITACION SUBCONTRATADA";
		String nombreArchivo = "";
		
		DtoGeneralReporteServiciosAutorizados dtoReporte = new DtoGeneralReporteServiciosAutorizados();
		GeneradorReporteFormatoCapitacionAutorservicio generadorReporte 	= new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporte);
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoInfoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
		
		String infoEncabezadoPagina 	= ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina			= ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		if(reporteMediaCarta== null || reporteMediaCarta.trim().isEmpty()){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}
		
		AutorizacionCapitacionDto dtoAutorizacion=forma.getAutorizacionCapitacion();	
		
		dtoPaciente.setNombrePaciente(dtoAutorizacion.getDatosPacienteAutorizar().getNombresPaciente());
		dtoPaciente.setTipoDocPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getTipoIdPaciente());
		dtoPaciente.setNumeroDocPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getNumIdPaciente());
		dtoPaciente.setTipoContratoPaciente(dtoAutorizacion.getOrdenesAutorizar().get(0).getContrato().getConvenio().getNombreTipoContrato());
		dtoPaciente.setEdadPaciente(dtoAutorizacion.getDatosPacienteAutorizar().getEdadPaciente());
		dtoPaciente.setConvenioPaciente(dtoAutorizacion.getOrdenesAutorizar().get(0).getContrato().getConvenio().getNombre());
		dtoPaciente.setCategoriaSocioEconomica(dtoAutorizacion.getDatosPacienteAutorizar().getNombreClasificacionSocieconomica());
		if(dtoAutorizacion.isConvenioRecobro()){
			dtoPaciente.setRecobro(ConstantesBD.acronimoSi);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getDescripcionConvenioRecobro());
		}
		else{
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getDescripcionConvenioRecobro());
		}
		dtoPaciente.setTipoAfiliado(dtoAutorizacion.getDatosPacienteAutorizar().getNombreTipoAfiliado());
		dtoPaciente.setMontoCobro(dtoAutorizacion.getMontoCobroAutorizacion().getDescripcionMonto());
		
		dtoInfoAutorizacion.setNumeroAutorizacion(dtoAutorizacion.getConsecutivoAutorizacion());
		dtoInfoAutorizacion.setEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getRazonSocial());
		dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getDireccionEntidad());
		dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getEntidadSubAutorizarCapitacion().getTelefonoEntidad());
		dtoInfoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
		dtoInfoAutorizacion.setUsuarioAutoriza(dtoAutorizacion.getLoginUsuario());
		SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
		dtoInfoAutorizacion.setFechaAutorizacion(format.format(dtoAutorizacion.getFechaAutorizacion()));
		dtoInfoAutorizacion.setFechaVencimiento(format.format(dtoAutorizacion.getFechaVencimientoAutorizacion()));
		String nombreEstado = (String)ValoresPorDefecto.getIntegridadDominio(dtoAutorizacion.getEstadoAutorizacion());
		dtoInfoAutorizacion.setEstadoAutorizacion(nombreEstado);
		dtoReporte.setObservaciones(dtoAutorizacion.getObservacionesGenerales());
		
		dtoReporte.setDatosEncabezado(infoEncabezadoPagina);
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setDtoPaciente(dtoPaciente);
		dtoReporte.setDtoAutorizacion(dtoInfoAutorizacion);
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit(institucion.getNit());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setDireccion(institucion.getDireccion());
		dtoReporte.setTelefono(institucion.getTelefono());
		StringBuilder nombreUsuario= new StringBuilder();
		nombreUsuario.append(usuario.getNombreUsuario());
		nombreUsuario.append(" (");
		nombreUsuario.append(usuario.getLoginUsuario());
		nombreUsuario.append(")");
		dtoReporte.setUsuario(nombreUsuario.toString());
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		for(OrdenAutorizacionDto dtoOrden:dtoAutorizacion.getOrdenesAutorizar()){
			for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
				if(dtoServicio.isAutorizado()){
					DtoServiciosAutorizaciones servicioAutorizado= new DtoServiciosAutorizaciones();
					servicioAutorizado.setConsecutivoOrdenMed(Integer.valueOf(dtoOrden.getConsecutivoOrden()));
					servicioAutorizado.setFechaOrden(dtoOrden.getFechaOrden());
					servicioAutorizado.setCodigoPropietario(dtoServicio.getCodigoPropietario());
					servicioAutorizado.setDescripcionServicio(dtoServicio.getDescripcion());
					servicioAutorizado.setCantidadSolicitada(dtoServicio.getCantidad().intValue());
					servicioAutorizado.setDescripcionNivelAutorizacion(dtoServicio.getDescripcionNivelAtencion());
					StringBuffer diagnostico=new StringBuffer();
					if(dtoServicio.getAcronimoDx() != null && dtoServicio.getTipoCieDx() != null){
						diagnostico.append(dtoServicio.getAcronimoDx());
						diagnostico.append(" - ");
						diagnostico.append(dtoServicio.getTipoCieDx());
					}
					servicioAutorizado.setDiagnostico(diagnostico.toString());
					listaServicios.add(servicioAutorizado);
				}
			}
		}
		dtoReporte.setListaServicios(listaServicios);
		
		//hermorhu - MT5966
		//Dependiendo el valor de 'esImpresionOriginal' se envia al reporte el tipo de impresion
		if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == true) {
			dtoReporte.setTipoImpresion("Original");
			dtoReporte.setFechaHoraEntrega("");
		} else if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == false) {
			dtoReporte.setTipoImpresion("Copia");
			dtoReporte.setFechaHoraEntrega("Fecha Entrega: "+ forma.getAutorizacionCapitacion().getAutorizacionEntrega().getFechaEntrega() +" "+forma.getAutorizacionCapitacion().getAutorizacionEntrega().getHoraEntrega());
		} else {
			dtoReporte.setTipoImpresion("");
		}
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		if(!nombreArchivo.trim().isEmpty())
		{
			forma.setNombreReporte(nombreArchivo);
			forma.setMostrarReporte(true);
		}
		
    }
	
	
	/**
	 * Método encargado de implementar la lógica web cuando se selecciona un centro de costo
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionSeleccionCentroCosto(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			if(forma.getCodigoCentroCosto() > 0){
				if(forma.isMostrarListaCentrosCosto()){
					for(CentroCostoDto dtoCentroCosto:forma.getListaCentrosCosto()){
						if(forma.getCodigoCentroCosto()==dtoCentroCosto.getCodigo()){
							forma.setTipoEntidadEjecuta(dtoCentroCosto.getTipoEntidadEjecuta());
							//Se crea un nuevo objeto y se pasa por referencia dato que más adelante 
							//los datos se cambian
							CentroCostoDto dto=new CentroCostoDto();
							dto.setCodigo(dtoCentroCosto.getCodigo());
							dto.setNombre(dtoCentroCosto.getNombre());
							dto.setTipoEntidadEjecuta(dtoCentroCosto.getTipoEntidadEjecuta());
							forma.getAutorizacionCapitacion().setCentroCosto(dto);
							break;
						}
					}
				}
				
				List<DtoCheckBox> listaTiposEntidad = new ArrayList<DtoCheckBox>();
				forma.setMostrarSeccionAutorizacion(true);
				boolean filtrarEntidad=false;
				if(forma.getTipoEntidadEjecuta() == null
						|| forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)){
					DtoCheckBox tipo = new DtoCheckBox();
					tipo.setNombre(ConstantesIntegridadDominio.acronimoInterna);
					tipo.setDescripcion("Interna");
					listaTiposEntidad.add(tipo);
					filtrarEntidad=true;
					forma.setTipoEntidadEjecuta(ConstantesIntegridadDominio.acronimoInterna);
				}
				else if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)){
					DtoCheckBox tipo = new DtoCheckBox();
					tipo.setNombre(ConstantesIntegridadDominio.acronimoAmbos);
					tipo.setDescripcion("Seleccione");
					listaTiposEntidad.add(tipo);
					tipo = new DtoCheckBox();
					tipo.setNombre(ConstantesIntegridadDominio.acronimoInterna);
					tipo.setDescripcion("Interna");
					listaTiposEntidad.add(tipo);
					tipo = new DtoCheckBox();
					tipo.setNombre(ConstantesIntegridadDominio.acronimoExterna);
					tipo.setDescripcion("Externa");
					listaTiposEntidad.add(tipo);
				}
				else if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)){
					DtoCheckBox tipo = new DtoCheckBox();
					tipo.setNombre(ConstantesIntegridadDominio.acronimoExterna);
					tipo.setDescripcion("Externa");
					listaTiposEntidad.add(tipo);
					filtrarEntidad=true;
				}
				forma.setListaTipoEntidades(listaTiposEntidad);
				if(filtrarEntidad){
					this.actionSeleccionTipoEntidad(mapping, forma, request, response);
				}
				else{
					forma.setListaEntidadesSubcontratadas(new ArrayList<EntidadSubContratadaDto>());
					forma.setCodigoEntidadSubcontratada(ConstantesBD.codigoNuncaValidoLong);
					forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
				}
			}
			else{
				if(forma.isAutorizacionServicios()){
					errores.add("tipoEntidadEjecuta", new ActionMessage(KEY_ERROR_REQUIRED, "Centro Costo Responde"));
				}
				else{
					errores.add("tipoEntidadEjecuta", new ActionMessage(KEY_ERROR_REQUIRED, "Farmacia de Entrega"));
				}
				forma.setTipoEntidadEjecuta(null);
				forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
				forma.getListaTipoEntidades().clear();
				forma.getListaEntidadesSubcontratadas().clear();
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_SECCION_AUTORIZACION);
	}
	
	
	/**
	 * Método encargado de implementar la lógica web cuando se selecciona un tipo de entidad que ejecuta
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionSeleccionTipoEntidad(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			forma.setCodigoEntidadSubcontratada(ConstantesBD.codigoNuncaValidoLong);
			List<EntidadSubContratadaDto> listaEntidades= new ArrayList<EntidadSubContratadaDto>();
			if(!forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)){
				forma.getAutorizacionCapitacion().getCentroCosto().setTipoEntidadEjecuta(forma.getTipoEntidadEjecuta());
				ManejoPacienteFacade manejoPacienteFacade= new ManejoPacienteFacade();
				if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)){
					String entidadInterna = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(usuario.getCodigoInstitucionInt());
					if(entidadInterna != null && !entidadInterna.trim().isEmpty()){
						try{
							String codigoEntidad=entidadInterna.split("-")[0];
							String nombreEntidad=entidadInterna.split("-")[1];
							// Se verifica que la entidad parametrizada se encuentre activa y tenga un contrato vigente
							EntidadSubContratadaDto infoContratoEntidad=manejoPacienteFacade.verificarEntidadSubContratadaParametrizada(codigoEntidad);
							if(infoContratoEntidad != null){
								EntidadSubContratadaDto entidadDto = new EntidadSubContratadaDto();
								entidadDto.setCodEntidadSubcontratada(infoContratoEntidad.getCodEntidadSubcontratada());
								entidadDto.setRazonSocial(infoContratoEntidad.getRazonSocial());
								entidadDto.setDireccionEntidad(infoContratoEntidad.getDireccionEntidad());
								entidadDto.setTelefonoEntidad(infoContratoEntidad.getTelefonoEntidad());
								entidadDto.setNumeroPrioridad(1);
								entidadDto.setCodContratoEntidadSub(infoContratoEntidad.getCodContratoEntidadSub());
								entidadDto.setTipoTarifa(infoContratoEntidad.getTipoTarifa());
								listaEntidades.add(entidadDto);
							}
							else{
								forma.setPermiteAutorizar(false);
								errores.add("codigoEntidadSubcontratada",new ActionMessage("errors.autorizacion.entidadNoVigente", nombreEntidad));
							}
						}
						catch (ArrayIndexOutOfBoundsException aiobe) {
							Log4JManager.error("LA ENTIDAD SUBCONTRATADA PARAMETRIZADA NO ESTA BIEN DEFINIDA o NO TIENE UN FORMATO VALIDO");
							forma.setPermiteAutorizar(false);
							errores.add("codigoEntidadSubcontratada",new ActionMessage("errors.parametroGeneral", "Entidad Subcontratada para Centros de Costo Internos"));
						}
					}
					else{
						forma.setPermiteAutorizar(false);
						errores.add("codigoEntidadSubcontratada",new ActionMessage("errors.parametroGeneral", "Entidad Subcontratada para Centros de Costo Internos"));
					}
				}
				else if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)){
					listaEntidades=manejoPacienteFacade.obtenerEntidadesSubContratadasExternas(
														forma.getCodigoCentroCosto(), 
														forma.getNivelesAutorizacionUsuario());
					if(listaEntidades == null || listaEntidades.isEmpty()){
						forma.setPermiteAutorizar(true);
						errores.add("codigoEntidadSubcontratada",new ActionMessage("errors.autorizacion.noEntidadesPorCentroCosto"));
					}
				}
			}
			else{
				errores.add("codigoEntidadSubcontratada",new ActionMessage(KEY_ERROR_REQUIRED, "Tipo Entidad a la que se Autoriza"));
				forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
			}
			forma.setListaEntidadesSubcontratadas(listaEntidades);
			if(forma.getListaEntidadesSubcontratadas() != null && !forma.getListaEntidadesSubcontratadas().isEmpty()){
				if(forma.getListaEntidadesSubcontratadas().size() == 1){
					forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(forma.getListaEntidadesSubcontratadas().get(0));
					forma.setCodigoEntidadSubcontratada(forma.getListaEntidadesSubcontratadas().get(0).getCodEntidadSubcontratada());
					this.actionSeleccionEntidadSubContratada(mapping, forma, request, response);
				}
				else{
					forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
				}
			}
			else{
				forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_SECCION_AUTORIZACION);
	}

	/**
	 * Método encargado de implementar la lógica web cuando se selecciona la entidad subcontratada
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward actionSeleccionEntidadSubContratada(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			if(forma.getCodigoEntidadSubcontratada() >= 0){
				//Se asignan los datos de la entidad subcontratada seleccionada
				if(forma.getCodigoEntidadSubcontratada() > 0){
					for(EntidadSubContratadaDto dtoEntidad:forma.getListaEntidadesSubcontratadas()){
						if(forma.getCodigoEntidadSubcontratada()==dtoEntidad.getCodEntidadSubcontratada()){
							forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(dtoEntidad);
							break;
						}
					}
				}
				else{
					forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
				}
				boolean validar=true;
				int numPrioridadEntidad=0;
				Integer codigoTipoMonto=0;
				long codigoContratoEntidad=forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getCodContratoEntidadSub(); 
				String tipoPaciente=forma.getAutorizacionCapitacion().getDatosPacienteAutorizar().getTipoPaciente();
				Integer codigoNaturaleza=forma.getAutorizacionCapitacion().getDatosPacienteAutorizar().getNaturalezaPaciente(); 
				int codigoInstitucion=usuario.getCodigoInstitucionInt();
				//Se verifica si la entidad subcontratada es otra para no realizar validación
				//de niveles ni de prioridades
				if(forma.getCodigoEntidadSubcontratada()==0){
					validar=false;
				}
				else{
					numPrioridadEntidad=forma.getAutorizacionCapitacion().getEntidadSubAutorizarCapitacion().getNumeroPrioridad().intValue();
					codigoTipoMonto=forma.getAutorizacionCapitacion().getMontoCobroAutorizacion().getTipoMonto();
				}
				boolean cuentaManejaMontos=forma.getAutorizacionCapitacion().getDatosPacienteAutorizar().isCuentaManejaMontos();
				ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
				for(OrdenAutorizacionDto dtoOrden:forma.getOrdenesPorAutorizar()){
					//Se valida si la autorización es de Servicios o de Medicamentos/Insumos
					if(dtoOrden.getServiciosPorAutorizar() != null 
							&& !dtoOrden.getServiciosPorAutorizar().isEmpty()){
						this.activarCheckServiciosOrden(dtoOrden, validar, numPrioridadEntidad, codigoTipoMonto, manejoPacienteFacade,
															codigoContratoEntidad, tipoPaciente, codigoNaturaleza, codigoInstitucion, 
															cuentaManejaMontos, forma.getCodigoCentroCosto());
					}
					else{
						if(dtoOrden.getMedicamentosInsumosPorAutorizar() != null 
								&& !dtoOrden.getMedicamentosInsumosPorAutorizar().isEmpty()){
							this.activarCheckMedicamentosInsumosOrden(dtoOrden, validar, numPrioridadEntidad, manejoPacienteFacade,
																		codigoContratoEntidad, tipoPaciente, codigoNaturaleza, codigoInstitucion);
						}
					}
				}
			}
			else{
				forma.getAutorizacionCapitacion().setEntidadSubAutorizarCapitacion(new EntidadSubContratadaDto());
				this.desActivarCheckOrdenes(forma.getOrdenesPorAutorizar());
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_SECCION_AUTORIZACION);
	}

		
	/**
	 * Método encargado de realizar las validaciones necesarias para
	 * verificar si un servicio es candidato a autorizar
	 * 
	 * @param dtoOrden
	 * @param validar
	 * @param numPrioridadEntidad
	 * @param codigoTipoMonto
	 */
	private void desActivarCheckOrdenes(List<OrdenAutorizacionDto> ordenes){
		for(OrdenAutorizacionDto dtoOrden:ordenes){
			//Se valida si la autorización es de Servicios o de Medicamentos/Insumos
			if(dtoOrden.getServiciosPorAutorizar() != null 
					&& !dtoOrden.getServiciosPorAutorizar().isEmpty()){
				for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
					dtoServicio.setValidoAutorizar(false);
				}
			}
			else if(dtoOrden.getMedicamentosInsumosPorAutorizar() != null 
						&& !dtoOrden.getMedicamentosInsumosPorAutorizar().isEmpty()){
				for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
					dtoMedicamento.setValidoAutorizar(false);
				}
			}
		}
	}
		
	/**
	 * Método encargado de realizar las validaciones necesarias para
	 * verificar si un servicio es candidato a autorizar
	 * 
	 * @param dtoOrden
	 * @param validar
	 * @param numPrioridadEntidad
	 * @param codigoTipoMonto
	 */
	private void activarCheckServiciosOrden(OrdenAutorizacionDto dtoOrden, boolean validar, int numPrioridadEntidad, Integer codigoTipoMonto,
												ManejoPacienteFacade manejoPacienteFacade, long codigoContratoEntidad, String tipoPaciente, 
												Integer codigoNaturalezaPaciente, int codigoInstitucion, boolean cuentaManejaMontos,
												int codigoCentroCosto) throws IPSException{
		for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
			boolean activarCheck=false;
			NivelAutorizacionDto nivelMismaPrioridad=null;
			if(validar){
				//Se Verifica si el servicio tiene niveles de autorización que corresponden
				//a los niveles del usuario
				if(dtoServicio.getNivelesAutorizacion() != null){
					for(NivelAutorizacionDto dtoNivel:dtoServicio.getNivelesAutorizacion()){
						if(activarCheck){
							break;
						}
						for(Integer prioridad:dtoNivel.getPrioridades()){
							if(numPrioridadEntidad==prioridad.intValue()){
								activarCheck=true;
								nivelMismaPrioridad=dtoNivel;
								break;
							}
						}
					}
				}
				dtoServicio.setNivelAutorizacion(nivelMismaPrioridad);
				//Si pasa la validación de niveles y prioridades se valida la cobertura
				if(dtoServicio.isPuedeAutorizar() && dtoServicio.getNivelAutorizacion() != null && activarCheck){
					InfoCobertura cobertura=manejoPacienteFacade.validarCoberturaServicioEntidadSubcontratada(codigoContratoEntidad, 
													dtoOrden.getCodigoViaIngreso(), tipoPaciente, dtoServicio.getCodigo(), 
													codigoNaturalezaPaciente, codigoInstitucion);					
					if(!cobertura.getIncluido()){
						activarCheck=false;
					}
					//Si la ordenes es Orden Ambulatoria o Petición
					if(activarCheck 
							&& (dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria
										|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion) 
							&& cuentaManejaMontos
							&&(dtoServicio.getTipoMonto() == null || !codigoTipoMonto.equals(dtoServicio.getTipoMonto()))){
						activarCheck=false;
					}
					if(activarCheck){
						//Se verifica si la orden ambulatoria es de procedimientos
						//o es una petición para verificar el centro de costo por el grupo del servicio
						//asociado al servicio
						if((dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria
								&& dtoServicio.getAcronimoTipoServicio().equals(String.valueOf(ConstantesBD.codigoServicioProcedimiento)))
								|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
							if(!manejoPacienteFacade.existeCentroCostoParametrizadoPorGrupoServicio(dtoServicio.getCodigoGrupoServicio(), codigoCentroCosto)){
								activarCheck=false;
							}
						}
						//Se verifica si la orden ambulatoria es de consulta
						//para verificar el centro de costo por la unidad de consulta
						//asociado al servicio
						else if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria
								&& dtoServicio.getAcronimoTipoServicio().equals(String.valueOf(ConstantesBD.codigoServicioInterconsulta))){
							if(!manejoPacienteFacade.existeCentroCostoParametrizadoPorUnidadConsulta(dtoServicio.getCodigo(), codigoCentroCosto)){
								activarCheck=false;
							}
						}
					}
				}
			}
			else{
				activarCheck=true;
				dtoServicio.setPuedeAutorizar(activarCheck);
			}
			dtoServicio.setValidoAutorizar(activarCheck);
			dtoServicio.setAutorizar(activarCheck);
		}
	}
	
	
	/**
	 * Método encargado de realizar las validaciones necesarias para
	 * verificar si un Medicamento/Insumo es candidato a autorizar
	 * 
	 * @param dtoOrden
	 * @param validar
	 * @param numPrioridadEntidad
	 * 
	 */
	private void activarCheckMedicamentosInsumosOrden(OrdenAutorizacionDto dtoOrden, boolean validar, int numPrioridadEntidad,
														ManejoPacienteFacade manejoPacienteFacade, long codigoContratoEntidad,
														String tipoPaciente, Integer codigoNaturalezaPaciente, int codigoInstitucion) throws IPSException{
		for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
			boolean activarCheck=false;
			NivelAutorizacionDto nivelMismaPrioridad=null;
			if(validar){
				//Se Verifica si el servicio tiene niveles de autorización que corresponden
				//a los niveles del usuario
				if(dtoMedicamento.getNivelesAutorizacion() != null){
					for(NivelAutorizacionDto dtoNivel:dtoMedicamento.getNivelesAutorizacion()){
						if(activarCheck){
							break;
						}
						for(Integer prioridad:dtoNivel.getPrioridades()){
							if(numPrioridadEntidad==prioridad.intValue()){
								activarCheck=true;
								nivelMismaPrioridad=dtoNivel;
								break;
							}
						}
					}
				}
				dtoMedicamento.setNivelAutorizacion(nivelMismaPrioridad);
				//Si pasa la validación de niveles y prioridades se valida la cobertura
				if(dtoMedicamento.isPuedeAutorizar() && dtoMedicamento.getNivelAutorizacion() != null && activarCheck){
					InfoCobertura cobertura=manejoPacienteFacade.validarCoberturaMedicamentoInsumoEntidadSubcontratada(codigoContratoEntidad, 
													dtoOrden.getCodigoViaIngreso(), tipoPaciente, dtoMedicamento.getCodigo(), 
													codigoNaturalezaPaciente, codigoInstitucion);					
					if(!cobertura.getIncluido()){
						activarCheck=false;
					}			
				}
			}
			else{
				activarCheck=true;
				dtoMedicamento.setPuedeAutorizar(activarCheck);
			}
			dtoMedicamento.setValidoAutorizar(activarCheck);
			dtoMedicamento.setAutorizar(activarCheck);
		}
	}
	
	/**
	 * Método encargado de realizar las validaciones necesarias para
	 * verificar si una orden es candidada a autorizar
	 * 
	 * @param ordenesPorAutorizar
	 * @param viaIngresoOrdenAmb
	 * @param viaIngresoPeticion
	 */
	private void activarDetalleOrden(AutorizacionesCapitacionSubContratadaForm forma, List<OrdenAutorizacionDto> ordenesPorAutorizar, 
										String viaIngresoOrdenAmb, String viaIngresoPeticion) throws IPSException{
		Calendar fechaActual=Calendar.getInstance();
		//Se valida si las validaciones de parametrización se cumplen
		//para poder relizar la validaciones que activan el link de detalle de la orden
		boolean parametrizacionValida=false;
		if(!forma.isMensajeConsecutivoAutorizacionCapitacion()
				&& !forma.isMensajeConsecutivoAutorizacionEntidad()
				&& !forma.isMensajeNivelesAutorizacionUsuario()){
			parametrizacionValida=true;
		}
		CapitacionFacade capitacionFacade = new CapitacionFacade();
		for(OrdenAutorizacionDto dtoOrden:ordenesPorAutorizar){
			boolean valido=true;
			//Validar que el MES/AÑO de la fecha a posponer sea menor o igual al MES/AÑO de la fecha Actual
			if(dtoOrden.isPosponer()){
				Calendar fechaPosponer = Calendar.getInstance();
				fechaPosponer.setTime(dtoOrden.getFechaPosponer());
				if(fechaPosponer.get(Calendar.YEAR) > fechaActual.get(Calendar.YEAR) 
						|| (fechaPosponer.get(Calendar.YEAR) == fechaActual.get(Calendar.YEAR) 
								&& fechaPosponer.get(Calendar.MONTH) > fechaActual.get(Calendar.MONTH))){
					valido=false;
				}
				fechaPosponer=null;
			}
			if(valido){
				if(parametrizacionValida){
					//Se asigna la via de ingreso parametrizada para las ordenes ambulatorias
					//y para las peticiones
					if(dtoOrden.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria){
						dtoOrden.setCodigoViaIngreso(Integer.valueOf(viaIngresoOrdenAmb));
					}
					else if(dtoOrden.getClaseOrden() == ConstantesBD.claseOrdenPeticion){
						dtoOrden.setCodigoViaIngreso(Integer.valueOf(viaIngresoPeticion));
					}
					//Se filtran los niveles de autorización del usuario válidos de acuerdo a la via de ingreso de la orden
					List<Integer> nivelesValidos=this.filtrarNivelAutorizacionUsuarioPorViaIngreso(forma.getNivelesAutorizacionUsuario(), dtoOrden.getCodigoViaIngreso());
					if(!nivelesValidos.isEmpty()){
						boolean nivelAutoServMedicValido=false;
						if(dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos
								|| dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
								|| dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudMedicamentos){
							nivelAutoServMedicValido=capitacionFacade.existeNivelAutorizacionMedicamentoInsumo(dtoOrden.getMedicamentosInsumosPorAutorizar(), nivelesValidos);
						}
						else{
							nivelAutoServMedicValido=capitacionFacade.existeNivelAutorizacionServicio(dtoOrden.getServiciosPorAutorizar(), nivelesValidos);
						}
						//Si por lo menos un servicios o Medicamento/Insumo de la orden tiene parametrización valida
						//respecto a los niveles de autorización del usuario, se activa el link de detalle de la orden
						if(nivelAutoServMedicValido){
							//Se valida si es una orden medica o cargo directo de cirugía o si es una petición
							//para realizar la validación de cobertura MT 5204
							boolean activarLink=true;
							if(dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia || 
									dtoOrden.getClaseOrden() == ConstantesBD.claseOrdenPeticion){
								int contratoOrden=dtoOrden.getContrato().getCodigo();
								//Se verifica que todos los servicios de la cirugia tengan un contrato capitado y que esten cubiertos,
								//además se valida que el contrato de cada sevicio corresponda al contrato de la orden
								for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
									if(dtoServicio.getCodigoContrato() == null || !dtoServicio.isCubierto()
											|| contratoOrden != dtoServicio.getCodigoContrato().intValue()){
										activarLink=false;
										break;
									}
								}
							}
							dtoOrden.setMostrarLink(activarLink);
							if(forma.isPorPaciente() && dtoOrden.isMostrarLink()){
								//Se activa el check de autorización multiple de acuerdo a la clase y tipo de orden
								if(dtoOrden.getClaseOrden() != ConstantesBD.claseOrdenPeticion
								    && (dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios
										|| dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
										|| dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudProcedimiento
										|| dtoOrden.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCita)){
									dtoOrden.setMostrarAutoMultiple(true);
								}
								//Se activa el check de posponer si la orden no tiene un registro previo de posponer
								if(dtoOrden.getFechaPosponer() == null){
									dtoOrden.setMostrarPosponer(true);
								}
							}
						}
					}
				}
				if(forma.isPorPaciente()){
					forma.getOrdenesPorAutorizarPaciente().add(dtoOrden);
				}
				if(forma.isPorRango()){
					forma.getOrdenesPorAutorizarRangos().add(dtoOrden);
				}
			}
		}
	}

	
	/**
	 * Método que permite filtrar los niveles de autorización del usuario
	 * de acuerdo a la via de ingreso de la orden pasada pro parámetro
	 * 
	 * @param nivelesAutorizacionUsuario
	 * @param codigoViaIngreso
	 * @return
	 */
	private List<Integer> filtrarNivelAutorizacionUsuarioPorViaIngreso(List<NivelAutorizacionDto> nivelesAutorizacionUsuario, int codigoViaIngreso){
		List<Integer> nivelesFiltrados=new ArrayList<Integer>();
		for(NivelAutorizacionDto dtoNivelAuto:nivelesAutorizacionUsuario){
			if(codigoViaIngreso==dtoNivelAuto.getViaIngreso().getCodigo()){
				nivelesFiltrados.add(dtoNivelAuto.getCodigo());
			}
		}
		return nivelesFiltrados;
	}
	
	/**
	 * Método encargado de llenar las listas necesarias para la búsqueda por 
	 * rangos
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 * @throws IPSException
	 */
	private void cargarValoresForm(AutorizacionesCapitacionSubContratadaForm forma, int codigoInstitucion) throws IPSException{
		FacturacionFacade facturacionFacade = new FacturacionFacade();
		CapitacionFacade capitacionFacade = new CapitacionFacade();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		forma.setListaConvenios(facturacionFacade.consultarConveniosPorInstitucion(codigoInstitucion, ConstantesBD.codigoTipoContratoCapitado, 
									ConstantesBD.acronimoSiChar, true));
		forma.setListaViasIngreso(manejoPacienteFacade.consultarViasIngreso());
		forma.setListaNivelesAtencion(capitacionFacade.consultarNivelesAtencion());
		forma.setListaClasesOrden(this.obtenerClasesOrden());
	}

	/**
	 * Método encargado de filtrar los niveles de autorización del usuario de acuerdo a la via de
	 * ingreso de las ordenes y a cada uno de los servicios/medicamentos 
	 * 
	 * @param forma
	 * @throws IPSException
	 */
	private void filtrarNivelesAutorizacionPorOrdenes(AutorizacionesCapitacionSubContratadaForm forma) throws IPSException{
		List<NivelAutorizacionDto> nivelesAutorizacionViaIngreso = new ArrayList<NivelAutorizacionDto>();
		List<NivelAutorizacionDto> nivelesAutorizacionFiltrados = new ArrayList<NivelAutorizacionDto>();
		//se filtran los niveles de autorización para correspondan a la via de ingreso del 
		//la(s) orden(es) seleccionada(s)
		for(OrdenAutorizacionDto ordenDto:forma.getOrdenesPorAutorizar()){
			for(NivelAutorizacionDto dtoNivel:forma.getNivelesAutorizacionUsuario()){
				if(ordenDto.getCodigoViaIngreso()==dtoNivel.getViaIngreso().getCodigo() 
						&& !nivelesAutorizacionViaIngreso.contains(dtoNivel)){
					nivelesAutorizacionViaIngreso.add(dtoNivel);
				}
			}
		}
		forma.getNivelesAutorizacionUsuario().clear();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		List<Integer> nivelesAutorizacionOrdenes=manejoPacienteFacade.verificarNivelesAutorizacionOrdenes(
													forma.getOrdenesPorAutorizar(), nivelesAutorizacionViaIngreso);
		if(nivelesAutorizacionOrdenes == null
				|| nivelesAutorizacionOrdenes.isEmpty()){
			throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_NO_NIVEL_AUTORIZACION);
		}
		//se filtran los niveles de autorización para el detalle
		for(Integer codigoNivel:nivelesAutorizacionOrdenes){
			for(NivelAutorizacionDto dtoNivel:nivelesAutorizacionViaIngreso){
				if(codigoNivel.intValue()==dtoNivel.getCodigo()){
					nivelesAutorizacionFiltrados.add(dtoNivel);
					break;
				}
			}
		}
		//Se limpia la lista de niveles del usuario y se asignan los niveles
		//de atención filtrados
		forma.getNivelesAutorizacionUsuario().addAll(nivelesAutorizacionFiltrados);
	}

	/**
	 * Método encargado de realizar toda la lógica web para inicial el proceso de autorización
	 * de la(s) orden(es)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward initDetalleAutorizacion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		AutorizacionesCapitacionSubContratadaForm forma=null;
		try{
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			PersonaBasica paciente 	= (PersonaBasica)request.getSession().getAttribute(KEY_SESSION_PACIENTE);
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			forma.setMensajeCentroAtencionPaciente(false);
			//Se verifica la clase de orden que se va a autorizar, se toma el primer registro
			//porque previamente se garantiza que lleguen ordenes de la misma clase
			int claseOrden=forma.getOrdenesPorAutorizar().get(0).getClaseOrden();
			if(claseOrden==ConstantesBD.claseOrdenOrdenAmbulatoria
					|| claseOrden==ConstantesBD.claseOrdenPeticion){
				forma.setCentroAtencionPaciente(manejoPacienteFacade.obtenerCentroAtencionPaciente(paciente.getCodigoPersona()));
				if(forma.getCentroAtencionPaciente() == null){
					forma.setMensajeCentroAtencionPaciente(true);
					forma.setPermiteAutorizar(false);
				}
			}
			this.filtrarNivelesAutorizacionPorOrdenes(forma);
			this.llenarForma(forma, paciente, usuario, mapping, request, response);
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			forma.setPermiteAutorizar(false);
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_DETALLE);
	}

	/**
	 * Método encagado de realizar toda la lógica web para mostrar las ordenes pendientes
	 * por autorizar para un paciente
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward initOrdenesPorPaciente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.reset();
			forma.setPorPaciente(true);
			forma.setMostrarRutaJsp(ValoresPorDefecto.getMostrarNombreJSP());
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			PersonaBasica paciente 	= (PersonaBasica)request.getSession().getAttribute(KEY_SESSION_PACIENTE);
			this.verificarPaciente(errores, paciente);
			if(!errores.isEmpty()){
				saveErrors(request, errores);
				return mapping.findForward("paginaErroresWeb");
			}
			this.verificarParametrizacionInicial(forma, usuario);
			//Se instancia la fachada para consultar las ordenes pendientes por autorizar
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			List<OrdenAutorizacionDto> ordenesPorAutorizar=manejoPacienteFacade.obtenerOrdenesPorAutorizarPorPaciente(paciente.getCodigoPersona());
			if(ordenesPorAutorizar != null && !ordenesPorAutorizar.isEmpty()){
				//Se verifican las ordenes para validar cuales son candidatas
				// a autorizar y se validan las ordenes que tienen registro de posponer
				this.activarDetalleOrden(forma, ordenesPorAutorizar, forma.getParametroViaIngresoOrdenAmbulatoria(), forma.getParametroViaIngresoPeticion());
				forma.setNumeroOrdenesPorAutorizar(forma.getOrdenesPorAutorizarPaciente().size());
				if(forma.getOrdenesPorAutorizarPaciente().isEmpty()){
					forma.setMensajeNoOrdenesPorAutorizar(true);
				}
				else{
					this.asignarNivelesAtencionOrden(forma, usuario.getCodigoInstitucionInt());
				}
			}
			else{
				forma.setMensajeNoOrdenesPorAutorizar(true);
			}
			
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_POR_PACIENTE);
	}

	/**
	 * Método encagado de realizar toda la lógica web para mostrar la pantalla de búsqueda de ordenes pendientes
	 * por autorizar por rango
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward initOrdenesPorRango(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.setMostrarRutaJsp(ValoresPorDefecto.getMostrarNombreJSP());
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			//Se valida si se deben o no mantener los filtros de busqueda de la funcionalida
			//por Rangos
			if(forma.isFromPacienteORango()  && !forma.isFromURL()){
				forma.reset();
			}
			else{
				forma.resetAll();
			}
			forma.setPorRango(true);
			//Se verifica que exista la parametrización necesario
			//sino para generar los mensajes informativos
			this.verificarParametrizacionInicial(forma, usuario);
			//Se llenan las listas de los Selects de la forma
			this.cargarValoresForm(forma, usuario.getCodigoInstitucionInt());
			//Se valida si viene de la funcionalidad por Paciente
			//para ejecutar la búsqueda con mismos filtros que se inicio 
			//el flujo
			if(forma.isFromPacienteORango() && !forma.isFromURL()){
				this.actionSeleccionarConvenio(mapping, forma, request, response);
				this.actionBuscarPorRango(mapping, forma, request, response);
			}
			forma.setFromURL(false);
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_POR_RANGO);
	}
	
	/**
	 * Método encagado de realizar toda la lógica web para mostrar consultar los contratos asociados 
	 * al convenio seleccionado
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionSeleccionarConvenio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			if(forma.getFiltrosBusquedaRangos().getCodigoConvenio() != ConstantesBD.codigoNuncaValido){
				FacturacionFacade facturacionFacade= new FacturacionFacade();
				forma.setListaContratos(facturacionFacade.consultarContratosVigentesPorConvenio(
															forma.getFiltrosBusquedaRangos().getCodigoConvenio()));
			}
			else{
				forma.setListaContratos(new ArrayList<ContratoDto>());
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward("listarContratos");
	}

	
	/**
	 * Método encagado de realizar la búsqueda de ordenes por autorizar de acuerdo a los filtros
	 * de búsqueda seleccionados
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionBuscarPorRango(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			//Se verifican los valores requeridos de los filtros
			this.validarBusquedaPorRangos(errores, forma.getFiltrosBusquedaRangos());
			if(!errores.isEmpty()){
				saveErrors(request, errores);
				return mapping.findForward(FORWARD_POR_RANGO);
			}
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			forma.setMensajeNoOrdenesPorAutorizar(false);
			forma.setFromPacienteORango(false);
			forma.getOrdenesPorAutorizarRangos().clear();
			//se setean los valores de los parametros generales
			forma.getFiltrosBusquedaRangos().setParametroViaIngresoOrdenAmb(forma.getParametroViaIngresoOrdenAmbulatoria());
			forma.getFiltrosBusquedaRangos().setParametroViaIngresoPeticion(forma.getParametroViaIngresoPeticion());
			List<OrdenAutorizacionDto> ordenesPorAutorizar=manejoPacienteFacade.obtenerOrdenesPorAutorizarPorRango(forma.getFiltrosBusquedaRangos());
			if(ordenesPorAutorizar != null && !ordenesPorAutorizar.isEmpty()){
				//Se verifican las ordenes para validar cuales son candidatas
				// a autorizar y se validan las ordenes que tienen registro de posponer
				this.activarDetalleOrden(forma, ordenesPorAutorizar, forma.getParametroViaIngresoOrdenAmbulatoria(), forma.getParametroViaIngresoPeticion());
				forma.setNumeroOrdenesPorAutorizar(forma.getOrdenesPorAutorizarRangos().size());
				forma.setNumeroRegistrosPagina(Integer.valueOf(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				if(forma.getOrdenesPorAutorizarRangos().isEmpty()){
					forma.setMensajeNoOrdenesPorAutorizar(true);
				}
				else{
					this.asignarNivelesAtencionOrden(forma, usuario.getCodigoInstitucionInt());
				}
			}
			else{
				forma.setMensajeNoOrdenesPorAutorizar(true);
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_POR_RANGO);
	}
	
	private void llenarForma(AutorizacionesCapitacionSubContratadaForm forma,PersonaBasica paciente, UsuarioBasico usuario,
						ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws IPSException, SQLException{
		AutorizacionCapitacionDto autorizacionCapitacionDto = new AutorizacionCapitacionDto();
		autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoManual);
		autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
		autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
		boolean existenCentrosCosto=true;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
		FacturacionFacade facturacionFacade = new FacturacionFacade();
		forma.setCodigoCentroCosto(ConstantesBD.codigoNuncaValido);
		forma.setCodigoMontoCobro(ConstantesBD.codigoNuncaValido);		
		forma.setListaConvenios(facturacionFacade.consultarConveniosPorInstitucion(usuario.getCodigoInstitucionInt(),
														null, null, true));
		OrdenAutorizacionDto dtoOrden=forma.getOrdenesPorAutorizar().get(0);
		datosPacienteAutorizar.setCodigoPaciente(paciente.getCodigoPersona());
		datosPacienteAutorizar.setNombresPaciente(StringEscapeUtils.unescapeHtml(paciente.getApellidosNombresPersona()));
		datosPacienteAutorizar.setTipoIdPaciente(StringEscapeUtils.unescapeHtml(paciente.getTipoIdentificacionPersona()));
		datosPacienteAutorizar.setNumIdPaciente(paciente.getNumeroIdentificacionPersona());
		datosPacienteAutorizar.setEdadPaciente(paciente.getEdadDetallada());
		datosPacienteAutorizar.setCodConvenioCuenta(dtoOrden.getContrato().getConvenio().getCodigo());
		datosPacienteAutorizar.setCodigoIngresoPaciente(dtoOrden.getCodigoIngreso());
		datosPacienteAutorizar.setContrato(dtoOrden.getContrato());
		ManejoPacienteFacade manejoPacienteFacade= new ManejoPacienteFacade();
		//Se obtiene la información de la VIa de Ingreso
		datosPacienteAutorizar.setViaIngreso(manejoPacienteFacade.consultarViaIngresoPorId(dtoOrden.getCodigoViaIngreso()));
		if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
				|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
			//Se obtiene la información del tipo de paciente y si la cuenta es activa o no
			DtoCheckBox dtoInfoCuenta=manejoPacienteFacade.consultarInfoCuentaPorIngreso(dtoOrden.getCodigoIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoInfoCuenta.getCodigo());
		}
		else if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
			//Se obtiene la información del tipo de paciente del parametro general para ordenes ambulatorias
			datosPacienteAutorizar.setTipoPaciente(ValoresPorDefecto.getTipoPacienteValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt()));
		}
		else if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
			//Se obtiene la información del tipo de paciente del parametro general para peticiones
			datosPacienteAutorizar.setTipoPaciente(ValoresPorDefecto.getTipoPacienteValidacionesPeticiones(usuario.getCodigoInstitucionInt()));
		}
		//Siempre es false por ser un atributo utilizado solo para Ordenes Ambulatorias y Peticiones 
		datosPacienteAutorizar.setCuentaAbierta(false);
		//Se obtiene la información de el tipo Afiliado, la clasificación Socioeconómica y Naturaleza del paciente 
		InfoSubCuentaDto dtoDatosSubCuenta=manejoPacienteFacade.consultarInfoSubCuentaPorIngresoPorConvenio(dtoOrden.getCodigoIngreso(), dtoOrden.getContrato().getConvenio().getCodigo());
		if(dtoDatosSubCuenta != null){
			if(dtoDatosSubCuenta.getCodigoTipoAfiliado() != null){
				datosPacienteAutorizar.setTipoAfiliado(dtoDatosSubCuenta.getCodigoTipoAfiliado().toString());
				datosPacienteAutorizar.setNombreTipoAfiliado(dtoDatosSubCuenta.getNombreTipoAfiliado());
			}
			if(dtoDatosSubCuenta.getCodigoEstratoSocial() != null){
				datosPacienteAutorizar.setClasificacionSocieconomica(dtoDatosSubCuenta.getCodigoEstratoSocial().intValue());
				datosPacienteAutorizar.setNombreClasificacionSocieconomica(dtoDatosSubCuenta.getNombreEstratoSocial());
			}
			if(dtoDatosSubCuenta.getCodigoNaturaleza() != null){
				datosPacienteAutorizar.setNaturalezaPaciente(dtoDatosSubCuenta.getCodigoNaturaleza());
				datosPacienteAutorizar.setNombreNaturalezaPaciente(dtoDatosSubCuenta.getNombreNaturaleza());
			}
			datosPacienteAutorizar.setCuentaManejaMontos(dtoDatosSubCuenta.isSubCuentaManejaMontos());
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoDatosSubCuenta.getSubCuentaPorcentajeMonto());
		}
		autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
		
		//Se obtiene la informacion de montos de cobro
		List<MontoCobroDto> listaMontosCobro = new ArrayList<MontoCobroDto>();
		if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
				|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
			if(dtoDatosSubCuenta != null){
				listaMontosCobro.add(dtoDatosSubCuenta.getMontoCobro());
			}
		}
		else{
			//Se valida si el convenio de la cuenta maneja montos MT 5389
			if(autorizacionCapitacionDto.getDatosPacienteAutorizar().isCuentaManejaMontos()){
				//Busqueda de MontosCobro DCU 986
				FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro = new FiltroBusquedaMontosCobroDto();   
			    filtroBusquedaMontosCobro.setCuentaAbierta(autorizacionCapitacionDto.getDatosPacienteAutorizar().isCuentaAbierta());
			    filtroBusquedaMontosCobro.setConvenio(dtoOrden.getContrato().getConvenio().getCodigo());
			    filtroBusquedaMontosCobro.setViaIngreso(dtoOrden.getCodigoViaIngreso());
			    filtroBusquedaMontosCobro.setTipoPaciente(autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoPaciente());
			    filtroBusquedaMontosCobro.setTipoAfiliado(autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoAfiliado());
			    filtroBusquedaMontosCobro.setClasificacionSocioEconomica(autorizacionCapitacionDto.getDatosPacienteAutorizar().getClasificacionSocieconomica());
			    if(autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente() != null){
			    	filtroBusquedaMontosCobro.setNaturalezaPaciente(autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente());
			    }
			    List<BusquedaMontosCobroDto> montosCobroDto=manejoPacienteFacade.obtenerMontosCobroAutorizacion(filtroBusquedaMontosCobro);
			    if(montosCobroDto != null && !montosCobroDto.isEmpty()){
			    	for(BusquedaMontosCobroDto dtoBusqueda:montosCobroDto){
			    		MontoCobroDto dtoMonto = new MontoCobroDto();
			    		dtoMonto.setCodDetalleMonto(dtoBusqueda.getDetalleCodigo());
			    		dtoMonto.setTipoMonto(dtoBusqueda.getTipoMontoID());
			    		dtoMonto.setTipoMontoNombre(dtoBusqueda.getTipoMontoNombre());
			    		dtoMonto.setTipoDetalleMonto(dtoBusqueda.getTipoDetalle());
			    		dtoMonto.setCantidadMonto(dtoBusqueda.getCantidadMonto());
			    		dtoMonto.setPorcentajeMonto(dtoBusqueda.getPorcentaje());
			    		dtoMonto.setValorMonto(dtoBusqueda.getValor());
			    		StringBuffer descripcion=new StringBuffer();
		    			if(dtoMonto.getTipoDetalleMonto().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET)){
		    				descripcion.append(dtoMonto.getTipoMontoNombre());
		    				descripcion.append(" Detallado");
		    			}
		    			else if(dtoMonto.getTipoDetalleMonto().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
		    				descripcion.append(dtoMonto.getTipoMontoNombre());
		    				if(dtoMonto.getPorcentajeMonto() != null){
		    					descripcion.append(" ");
		    					descripcion.append(dtoMonto.getPorcentajeMonto().toString());
		    					descripcion.append(" %");
		    				}
		    				else if(dtoMonto.getValorMonto() != null){
		    					descripcion.append(" $");
		    					descripcion.append(dtoMonto.getValorMonto().toString());
		    				}
		    				if(dtoMonto.getCantidadMonto() != null){
		    					descripcion.append(" Cantidad (");
		    					descripcion.append(dtoMonto.getCantidadMonto().toString());
		    					descripcion.append(")");
		    				}
		    				descripcion.append(" General");
		    			}
		    			dtoMonto.setDescripcionMonto(descripcion.toString());
		    			listaMontosCobro.add(dtoMonto);
			    	}
			    }
			}
			else{
				MontoCobroDto dtoMonto = new MontoCobroDto();
	    		dtoMonto.setCodDetalleMonto(0);
	    		StringBuffer descripcion=new StringBuffer();
	    		
	    	 //MT6703
	    		Boolean isPacientePagaAtencion = manejoPacienteFacade.consultarSiPacientePagaAtencion(paciente.getCodigoContrato(),true);
				
				if(isPacientePagaAtencion)
					{	
						descripcion.append("100.0 %");			    		
	    		}
				else
				    {	
						descripcion.append("0.0 %");	    					    					    	 
	    		}
	    		dtoMonto.setDescripcionMonto(descripcion.toString());
	    		listaMontosCobro.add(dtoMonto);
			}
		}
		
		//Se evalua si el tipo de detalle que tienen los servicios o medicamentos de las ordenes a autorizar
		forma.setAutorizacionServicios(false);
		forma.setAutorizacionMedicamentos(false);
		forma.setAutorizacionInsumos(false);
		InventarioFacade inventarioFacade = new InventarioFacade();
		for(OrdenAutorizacionDto orden:forma.getOrdenesPorAutorizar()){
			if(orden.getServiciosPorAutorizar() != null && !orden.getServiciosPorAutorizar().isEmpty()){
				forma.setAutorizacionServicios(true);
			}
			else if(orden.getMedicamentosInsumosPorAutorizar() != null && !orden.getMedicamentosInsumosPorAutorizar().isEmpty()){
				List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosCubiertos= new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
				for(MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumo: orden.getMedicamentosInsumosPorAutorizar()){
					//Se valida que el medicamento/Insumo se encuentre cubierto por el convenio
					boolean cubierto=manejoPacienteFacade.tieneCoberturaMedicamentoInsumo(orden.getClaseOrden(), orden.getContrato().getConvenio().getCodigo(),
																orden.getCodigoOrden(), medicamentoInsumo.getCodigo());
					if(cubierto){
						//Se obtiene la información de la clase de inventario del Medicamento/Insumo
						if(medicamentoInsumo.getSubGrupoInventario() != null){
							ClaseInventarioDto dtoClaseInv=inventarioFacade.obtenerClaseInventarioPorSubGrupo(medicamentoInsumo.getSubGrupoInventario().intValue());
							if(dtoClaseInv != null){
								medicamentoInsumo.setClaseInventario(dtoClaseInv.getCodigo());
								medicamentoInsumo.setNombreClaseInventario(dtoClaseInv.getNombre());
							}
						}
						if(medicamentoInsumo.getEsMedicamento()==ConstantesBD.acronimoSiChar){
							forma.setAutorizacionMedicamentos(true);
						}
						else{
							forma.setAutorizacionInsumos(true);
						}
						medicamentosCubiertos.add(medicamentoInsumo);
					}
				}
				orden.getMedicamentosInsumosPorAutorizar().clear();
				orden.getMedicamentosInsumosPorAutorizar().addAll(medicamentosCubiertos);
			} 
		}
		
		//se obtiene la información de los centros de costo dependiento la clase de orden
		if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenOrdenMedica
				|| dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenCargoDirecto){
			CentroCostoDto centroCostoDto= new CentroCostoDto();
			centroCostoDto.setCodigo(dtoOrden.getCodigoCentroCostoEjecuta());
			centroCostoDto.setNombre(dtoOrden.getNombreCentroCostoEjecuta());
			centroCostoDto.setTipoEntidadEjecuta(dtoOrden.getTipoEntidadEjecuta());
			autorizacionCapitacionDto.setCentroCosto(centroCostoDto);
			forma.setMostrarListaCentrosCosto(false);
			forma.setTipoEntidadEjecuta(centroCostoDto.getTipoEntidadEjecuta());
			forma.setCodigoCentroCosto(centroCostoDto.getCodigo());
		}
		else{
			//Se valida que el paciente tenga información de centro de atención asigando
			if(!forma.isMensajeCentroAtencionPaciente()){
				forma.setListaCentrosCosto(facturacionFacade.obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(
												forma.getCentroAtencionPaciente().getCodigo(), forma.getOrdenesPorAutorizar(), 
												forma.getNivelesAutorizacionUsuario()));
				if(forma.getListaCentrosCosto() != null && !forma.getListaCentrosCosto().isEmpty()){
					if(forma.getListaCentrosCosto().size()==1){
						autorizacionCapitacionDto.setCentroCosto(forma.getListaCentrosCosto().get(0));
						forma.setMostrarListaCentrosCosto(false);
						forma.setCodigoCentroCosto(autorizacionCapitacionDto.getCentroCosto().getCodigo());
						forma.setTipoEntidadEjecuta(autorizacionCapitacionDto.getCentroCosto().getTipoEntidadEjecuta());
					}
					else{
						forma.setMostrarListaCentrosCosto(true);
					}
				}
				else{
					forma.setMostrarListaCentrosCosto(false);
					existenCentrosCosto=false;
				}
			}
			else{
				forma.setMostrarListaCentrosCosto(false);
			}
		}
		//Se calcula la fecha de vencimiento de la autorizacion
		String paramDiasVencimientoAuto=null;
		if(forma.isAutorizacionServicios()){
			paramDiasVencimientoAuto=ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(usuario.getCodigoInstitucionInt());
		}
		else{
			if(forma.isAutorizacionMedicamentos() || forma.isAutorizacionInsumos()){
				paramDiasVencimientoAuto=ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(usuario.getCodigoInstitucionInt());
			}
		}
		if(paramDiasVencimientoAuto != null && !paramDiasVencimientoAuto.trim().isEmpty()){
			int numDias=Integer.valueOf(paramDiasVencimientoAuto);
			Calendar fechaVencimiento=Calendar.getInstance();
			fechaVencimiento.add(Calendar.DATE, numDias);
			autorizacionCapitacionDto.setFechaVencimientoAutorizacion(fechaVencimiento.getTime());
		}
		forma.setAutorizacionCapitacion(autorizacionCapitacionDto);
		forma.setMostrarSeccionAutorizacion(existenCentrosCosto);
		forma.setListaMontosCobro(listaMontosCobro);
		if(forma.getListaMontosCobro().isEmpty()){
			forma.setMostrarSeccionAutorizacion(false);
			forma.getAutorizacionCapitacion().setMontoCobroAutorizacion(new MontoCobroDto());
			throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_NO_MONTOS_COBRO);
		}
		else{
			MontoCobroDto montoDto=forma.getListaMontosCobro().get(0);
			forma.setCodigoMontoCobro(montoDto.getCodDetalleMonto());
			forma.getAutorizacionCapitacion().setMontoCobroAutorizacion(montoDto);
		}
		if(existenCentrosCosto){
			if(!forma.isMostrarListaCentrosCosto()){
				this.actionSeleccionCentroCosto(mapping, forma, request, response);
			}
		}
		else{
			forma.setMostrarSeccionAutorizacion(false);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_NO_CENTROS_COSTO_ORDENES);
		}
	}


	/**
	 * Método encargado de asignar los niveles de atención a la orden de acuerdo a los
	 * servicios o medicamentos/insumos de la misma así como asignar y validar los codigos y descripciones 
	 * de los servicios o medicamentos/insumos de acuerdo a los parametros generales 
	 *
	 * @param forma
	 * @param codigoInstitucion
	 * @throws IPSException
	 */
	private void asignarNivelesAtencionOrden(AutorizacionesCapitacionSubContratadaForm forma, int codigoInstitucion) throws IPSException{

		FacturacionFacade facturacionFacade= new FacturacionFacade();
		//Se obtienen la parametrica de codigo estándar para busqueda de articulos y de servicios
		String codigoEstandarArticulos=ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion);
		String codigoEstandarServicios=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
		if(forma.isPorPaciente()){
			for(OrdenAutorizacionDto dtoOrden:forma.getOrdenesPorAutorizarPaciente()){
				this.consolidarDatosOrden(dtoOrden, codigoEstandarArticulos, codigoEstandarServicios, facturacionFacade);
			}
		}
		else if(forma.isPorRango()){
			for(OrdenAutorizacionDto dtoOrden:forma.getOrdenesPorAutorizarRangos()){
				this.consolidarDatosOrden(dtoOrden, codigoEstandarArticulos, codigoEstandarServicios, facturacionFacade);
			}
		}
	}

	/**
	 * Método encargado de asignar los niveles de atención a la orden de acuerdo a los
	 * servicios o medicamentos/insumos de la misma así como asignar y validar los codigos y descripciones 
	 * de los servicios o medicamentos/insumos de acuerdo a los parametros generales 
	 * 
	 * @param dtoOrden
	 * @param codigoEstandarArticulos
	 * @param codigoEstandarServicios
	 * @param facturacionFacade
	 * @throws IPSException
	 */
	private void consolidarDatosOrden(OrdenAutorizacionDto dtoOrden, String codigoEstandarArticulos, 
						String codigoEstandarServicios, FacturacionFacade facturacionFacade) throws IPSException{
		StringBuffer nivelesAtencion= new StringBuffer();
		HashMap<String , String> niveles = new HashMap<String, String>();
		//Se valida si es una orden de Medicamentos/Insumos o si es de Servicios
		if(dtoOrden.getTipoOrden()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
				|| dtoOrden.getTipoOrden()==ConstantesBD.codigoTipoSolicitudMedicamentos
				|| dtoOrden.getTipoOrden()==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
			int numArticulos=dtoOrden.getMedicamentosInsumosPorAutorizar().size();
			int elementos=0;
			for (MedicamentoInsumoAutorizacionOrdenDto dtoArticulos : dtoOrden.getMedicamentosInsumosPorAutorizar()){
				elementos++;
				boolean valido=false;
				//De acuerdo al parametro general "Código Manual Estándar Búsqueda y Presentación de Artículos"
				//Se valida cual es el código a mostrar para cada articulo
				if(codigoEstandarArticulos != null && !codigoEstandarArticulos.trim().isEmpty()){
					if(codigoEstandarArticulos.equals(ConstantesIntegridadDominio.acronimoInterfaz)){
						dtoArticulos.setCodigoPropietario(dtoArticulos.getCodigoInterfaz());
					}
					else{
						dtoArticulos.setCodigoPropietario(String.valueOf(dtoArticulos.getCodigo()));
					}
				}
				else{
					dtoArticulos.setCodigoPropietario(String.valueOf(dtoArticulos.getCodigo()));
				}
				//Se obtienen los niveles de atención de los medicamentos/insumos de la orden
				//y se valida que no se repitan
				if(dtoArticulos.getDescripcionNivelAtencion() != null
						&& !niveles.containsKey(dtoArticulos.getDescripcionNivelAtencion())){
					valido=true;
					nivelesAtencion.append(dtoArticulos.getDescripcionNivelAtencion());
					niveles.put(dtoArticulos.getDescripcionNivelAtencion(), dtoArticulos.getDescripcionNivelAtencion());
				}
				else{
					if(elementos==numArticulos){
						if(nivelesAtencion.toString().length() >= 2){
							int fin=nivelesAtencion.toString().length();
							int ini=fin-2;
							nivelesAtencion.delete(ini, fin);
						}
					}
				}
				if(elementos < numArticulos && valido){
					nivelesAtencion.append(", ");
				}
			}
		}
		else{
			int numServicios=dtoOrden.getServiciosPorAutorizar().size();
			int elementos=0;
			for (ServicioAutorizacionOrdenDto dtoServicios: dtoOrden.getServiciosPorAutorizar()){
				elementos++;
				boolean valido=false;
				//Se valida que el parametro este definido y si esta definido que sea diferente de CUPS
				//debido a que por defecto el código que se trae es el CUPS para de esta manera evitar
				//realizar la consulta a la BD por cada servicio
				if(codigoEstandarServicios != null && !codigoEstandarServicios.trim().isEmpty()
						&& Integer.valueOf(codigoEstandarServicios.trim()).intValue() != ConstantesBD.codigoTarifarioCups){
					//De acuerdo al parametro general "Código Manual Estándar Búsqueda y Presentación de Servicios"
					//Se obtiene el código y la descripción a mostrar para cada Servicio
					DetalleServicioDto detalleServicio=facturacionFacade.obtenerDetalleServicioXTarifarioOficial(dtoServicios.getCodigo(), 
																				Integer.valueOf(codigoEstandarServicios.trim()));

					if(detalleServicio != null && detalleServicio.getCodigoPropietario() != null && !detalleServicio.getCodigoPropietario().trim().isEmpty()){
						dtoServicios.setCodigoPropietario(detalleServicio.getCodigoPropietario());
					}
					if(detalleServicio != null && detalleServicio.getDescripcionPropietario() != null && !detalleServicio.getDescripcionPropietario().trim().isEmpty()){
						dtoServicios.setDescripcion(detalleServicio.getDescripcionPropietario());
					}
				}
				//Se obtienen los niveles de atención de los servicios de la orden
				//y se valida que no se repitan
				if(dtoServicios.getDescripcionNivelAtencion() != null
						&& !niveles.containsKey(dtoServicios.getDescripcionNivelAtencion())){
					valido=true;
					nivelesAtencion.append(dtoServicios.getDescripcionNivelAtencion());
					niveles.put(dtoServicios.getDescripcionNivelAtencion(), dtoServicios.getDescripcionNivelAtencion());
				}
				else{
					if(elementos==numServicios){
						int fin=nivelesAtencion.toString().length();
						int ini=fin-2;
						nivelesAtencion.delete(ini, fin);
					}
				}
				if(elementos < numServicios && valido){
					nivelesAtencion.append(", ");
				}
			}
		}
		dtoOrden.setNombreNivelesAtencion(nivelesAtencion.toString());
	}
	
	/**
	 * Método encargado de llenar la lista de clases de orden
	 * necesarias para el filtro de búsqueda de las ordenes por rango
	 * 
	 * @return
	 */
	private List<ClaseOrdenDto> obtenerClasesOrden(){
		List<ClaseOrdenDto> clasesOrden = new ArrayList<ClaseOrdenDto>();
		ClaseOrdenDto claseOrden=null; 
		claseOrden = new ClaseOrdenDto(ConstantesBD.claseOrdenOrdenAmbulatoria, ConstantesBD.claseOrdenOrdenAmbulatoriaDesc);
		clasesOrden.add(claseOrden);
		claseOrden = new ClaseOrdenDto(ConstantesBD.claseOrdenPeticion, ConstantesBD.claseOrdenPeticionDesc);
		clasesOrden.add(claseOrden);
		claseOrden = new ClaseOrdenDto(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.claseOrdenOrdenMedicaDesc);
		clasesOrden.add(claseOrden);
		return clasesOrden;
	}

	/**
	 * Método encargado de validar los filtros requeridos para la búsqueda
	 * por rangos
	 * 
	 * @param errores
	 * @param filtros
	 */
	private void validarBusquedaPorRangos(ActionMessages errores, ParametroBusquedaOrdenAutorizacionDto filtros){
		MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesCapitacionSubcontratadaForm");
		boolean fechasValidas=true;
		if(filtros.getFechaInicio() == null){
			fechasValidas=false;
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.fechaInicio")));
		}
		if(filtros.getFechaFin() == null){
			fechasValidas=false;
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.fechaFin")));
		}
		if(filtros.getCodigoConvenio() == ConstantesBD.codigoNuncaValido){
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.convenio")));
		}
		if(filtros.getCodigoContrato() == ConstantesBD.codigoNuncaValido){
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.contrato")));
		}
		if(filtros.getCodigoViaIngreso() == ConstantesBD.codigoNuncaValido){
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.viaIngreso")));
		}
		if(filtros.getCodigoClaseOrden() == ConstantesBD.codigoNuncaValido){
			errores.add("",new ActionMessage(KEY_ERROR_REQUIRED, messageResource.getMessage("label.claseOrden")));
		}
		if(fechasValidas){
			Calendar fechaActual = Calendar.getInstance();
			// Se valida que Fecha Inicial <= Fecha Sistema
			if(filtros.getFechaInicio().after(fechaActual.getTime()))
			{
				fechasValidas=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", messageResource.getMessage("label.fechaInicioSufijo"), messageResource.getMessage("label.fechaActualSufijo")));
			}
			// Se valida que Fecha Final <= Fecha Sistema
			if(filtros.getFechaFin().after(fechaActual.getTime()))
			{
				fechasValidas=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", messageResource.getMessage("label.fechaFinSufijo"), messageResource.getMessage("label.fechaActualSufijo")));
			}
			if(fechasValidas){			
				// Se valida que Fecha Inicial <= Fecha Final
				if(filtros.getFechaInicio().after(filtros.getFechaFin()))
				{
					fechasValidas=false;
					errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", messageResource.getMessage("label.fechaInicioSufijo"), messageResource.getMessage("label.fechaFinSufijo")));
				}
				if(fechasValidas)
				{
					//Se valida que el numero de dias entre la fecha inicio y fin no supere 30 días
					Calendar fechaPosterior= Calendar.getInstance();
					fechaPosterior.setTime(filtros.getFechaInicio());
					fechaPosterior.add(Calendar.DATE, NUM_DIAS_MAXIMO);
					if(fechaPosterior.getTime().before(filtros.getFechaFin())){
						errores.add("", new ActionMessage("errors.rangoFechasExcedeNumDias", "30"));
					}
				}
			}
		}
	}

	/**
	 * Método encargado de realizar las validaciones
	 * correspondientes para permitir posponer la(s) ordenes
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward actionValidarPosponer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			List<OrdenAutorizacionDto> ordenesPorPosponer = new ArrayList<OrdenAutorizacionDto>();
			for(OrdenAutorizacionDto ordenDto: forma.getOrdenesPorAutorizarPaciente()){
				if(ordenDto.isCheckeadoPosponer()){
					ordenesPorPosponer.add(ordenDto);
				}
			}
			forma.setMostrarPopUpPosponer(false);
			//Se valida que se haya seleccionado por lo menos una orden
			if(!ordenesPorPosponer.isEmpty()){
				//Se valida que todas las ordenes seleccionadas tengan el mismo mes/año
				boolean ordenesValidas=true;
				int i=0;
				int mes=0;
				int anio=0;
				for(OrdenAutorizacionDto ordenDto: ordenesPorPosponer){
					Calendar fechaOrden= Calendar.getInstance();
					fechaOrden.setTime(ordenDto.getFechaOrden());
					if(i==0){
						mes=fechaOrden.get(Calendar.MONTH);
						anio=fechaOrden.get(Calendar.YEAR);
					}
					else{
						if(mes != fechaOrden.get(Calendar.MONTH)
								|| anio != fechaOrden.get(Calendar.YEAR)){
							ordenesValidas=false;
							break;
						}
					}
					i++;
				}
				if(ordenesValidas){
					forma.setOrdenesPorPosponer(ordenesPorPosponer);
					forma.setMostrarPopUpPosponer(true);
					Calendar fechaPosponer=Calendar.getInstance();
					fechaPosponer.add(Calendar.MONTH, 1);
					forma.setFechaPosponer(fechaPosponer.getTime());
				}
				else{
					errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_DIFERENTE_MES_ANIO.toString()));
				}
			}
			else{
				errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_REQUERIDAS.toString(), "Posponer"));
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_POR_PACIENTE);
	}

	
	/**
	 * Método encargado de realizar las validaciones
	 * correspondientes para permitir posponer la(s) ordenes
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward actionValidarAutorizarMultiple(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.setMostrarPopUpPosponer(false);
			List<OrdenAutorizacionDto> ordenesPorAutorizar = new ArrayList<OrdenAutorizacionDto>();
			for(OrdenAutorizacionDto ordenDto: forma.getOrdenesPorAutorizarPaciente()){
				if(ordenDto.isCheckeadoAutoMultiple()){
					ordenesPorAutorizar.add(ordenDto);
				}
			}
			//Se valida que se haya seleccionado por lo menos una orden
			if(!ordenesPorAutorizar.isEmpty()){
				//Se realizar las validaciones correspondientes para las ordenes seleccionadas
				int codigoIngreso=0;
				int codigoContrato=0;
				int codigoCentroCosto=0;
				int codigoClaseOrden=0;
				boolean pyp=false;
				int i=0;
				for(OrdenAutorizacionDto ordenDto: ordenesPorAutorizar){
					if(i==0){
						codigoIngreso=ordenDto.getCodigoIngreso();
						codigoContrato=ordenDto.getContrato().getCodigo();
						codigoCentroCosto=ordenDto.getCodigoCentroCostoEjecuta();
						if(ordenDto.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica
								|| ordenDto.getClaseOrden() == ConstantesBD.claseOrdenCargoDirecto){
							codigoClaseOrden=ConstantesBD.claseOrdenOrdenMedica;
						}
						else{
							codigoClaseOrden=ordenDto.getClaseOrden();
						}
						pyp=ordenDto.isEsPyp();
					}
					else{
						//Se valida que las ordenes sean del mismo ingreso
						if(codigoIngreso != ordenDto.getCodigoIngreso()){
							errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Ingreso"));
							break;
						}
						//Se valida que las ordenes sean del mismo contrato/convenio
						if(codigoContrato != ordenDto.getContrato().getCodigo()){
							errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Convenio/Contrato"));
							break;
						}
						if(ordenDto.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica
								|| ordenDto.getClaseOrden() == ConstantesBD.claseOrdenCargoDirecto){
							int claseOrden=ConstantesBD.claseOrdenOrdenMedica;
							//Se valida que las ordenes sean del mismo Tipo (Clase Orden)
							if(codigoClaseOrden != claseOrden){
								errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Tipo de Orden"));
								break;
							}
							//Se valida que las ordenes sean del mismo Centro de Costo
							if(codigoCentroCosto != ordenDto.getCodigoCentroCostoEjecuta()){
								errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Centro de Costo que Ejecuta"));
								break;
							}
						}
						else{
							//Se valida que las ordenes sean del mismo Tipo (Clase Orden)
							if(codigoClaseOrden != ordenDto.getClaseOrden()){
								errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Tipo de Orden"));
								break;
							}
						}
						//Se valida que las ordenes tengan la misma marca pyp
						if(pyp != ordenDto.isEsPyp()){
							errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_DIFERENTES.toString(), "Indicativo de PyP si o no"));
							break;
						}
					}
					i++;
				}
			}
			else{
				errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.MANEJO_PACIENTE_ORDENES_REQUERIDAS.toString(), "Autorizar Múltiple"));
			}
			if(errores.isEmpty()){
				forma.setOrdenesPorAutorizar(ordenesPorAutorizar);
				if(forma.getOrdenesPorAutorizar().size() > 1){
					forma.setAutorizacionMultiple(true);
				}
				else{
					forma.setAutorizacionMultiple(false);
				}
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward(FORWARD_POR_PACIENTE);
		}
		return this.initDetalleAutorizacion(mapping, form, request, response);
	}
	
	/**
	 * Método encargado de verificar si existe paciente cargado en session
	 * 
	 * @param errores
	 * @param paciente
	 */
	private void verificarPaciente(ActionMessages errores, PersonaBasica paciente){
		if(paciente==null || paciente.getCodigoPersona()<1){
			errores.add("",new ActionMessage("errors.paciente.noCargado"));
		}
	}

	/**
	 * Método encargado de verificar la parametrización para la funcionalidad
	 * de ordenes por paciente
	 * 
	 * @param forma
	 * @param usuario
	 * @throws IPSException
	 */
	private void verificarParametrizacionInicial(AutorizacionesCapitacionSubContratadaForm forma, UsuarioBasico usuario) throws IPSException{
		//Se definen las instancias de los FACADE a utilizar
		CapitacionFacade capitacionFacade= new CapitacionFacade();
		UtilidadesFacade utilidadesFacade= new UtilidadesFacade();
		//Se consultan y validan los niveles de autorización de usuario
		forma.setNivelesAutorizacionUsuario(capitacionFacade.consultarNivelesAutorizacionUsuario(usuario.getLoginUsuario(), usuario.getCodigoPersona(), ConstantesIntegridadDominio.acronimoTipoAutorizacionManual, true));
		if(forma.getNivelesAutorizacionUsuario() == null || forma.getNivelesAutorizacionUsuario().isEmpty()){
			forma.setMensajeNivelesAutorizacionUsuario(true);
		}
		//Se consulta y valida el parámetro general de ViaIngresoValidacionesOrdenesAmbulatorias
		forma.setParametroViaIngresoOrdenAmbulatoria(ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt()));
		if(forma.getParametroViaIngresoOrdenAmbulatoria() == null || forma.getParametroViaIngresoOrdenAmbulatoria().trim().isEmpty()){
			forma.setMensajeParametroViaIngresoOrden(true);
		}
		//Se consulta y valida el parámetro general de ViaIngresoValidacionesPeticiones
		forma.setParametroViaIngresoPeticion(ValoresPorDefecto.getViaIngresoValidacionesPeticiones(usuario.getCodigoInstitucionInt()));
		if(forma.getParametroViaIngresoPeticion() == null || forma.getParametroViaIngresoPeticion().trim().isEmpty()){
			forma.setMensajeParametroViaIngresoPeticion(true);
		}
		//Se verifica si existe consecutivo disponible de Autorizaciones Capitación
		if(!utilidadesFacade.existeConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt())){
			forma.setMensajeConsecutivoAutorizacionCapitacion(true);
		}
		//Se verifica si existe consecutivo disponible de Autorizaciones Entidad Subcontratada
		if(!utilidadesFacade.existeConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt())){
			forma.setMensajeConsecutivoAutorizacionEntidad(true);
		}
	}
	
	/**
	 * Método para hacer el forward del botón volver de la funcionalidad
	 * de autrorizaciones por rango
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionVolverPorRango(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("menu");
	}
	
	/**
	 * Método para hacer el forward del botón volver de la funcionalidad
	 * de autrorizaciones por paciente
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionVolverPorPaciente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			if(forma.isFromPacienteORango()){
				return this.initOrdenesPorRango(mapping, forma, request, response);
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresWeb");
		}
		return mapping.findForward("menu");
	}
	
	/**
	 * Método encargado de realizar la lógica web para la action de redireccionar
	 * a la consulta de autorizaciones
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionConsultarAutorizaciones(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			StringBuffer ruta=new StringBuffer();
			ruta.append(request.getContextPath());
			ruta.append("/autorizacionesCapitacionSubcontratada/consultaAutorizacionesCapitacionSubPorPaciente.do?estado=consultarPorPaciente&integracion=");
			ruta.append(ConstantesCapitacion.INTEGRACION_ORDENES_PACIENTE);
			forma.reset();
			response.sendRedirect(ruta.toString());
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return null;
	}
	
	/**
	 * Método encargado de realizar la lógica web para la action de redireccionar
	 * a las autorizaciones de ingreso estancia
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionIngresoEstancia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			StringBuffer ruta=new StringBuffer();
			ruta.append(request.getContextPath());
			ruta.append("/autorizacionesIngresoEstancia/autorizacionesIngresoEstancia.do?estado=empezar&integracion=");
			ruta.append(ConstantesCapitacion.INTEGRACION_ORDENES_PACIENTE);
			forma.reset();
			response.sendRedirect(ruta.toString());
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return null;
	}
	
	public ActionForward actionGuardarPosponer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			forma.setMostrarPopUpPosponer(false);
			manejoPacienteFacade.posponerOrdenes(forma.getOrdenesPorPosponer(), usuario.getLoginUsuario(), forma.getFechaPosponer(),
													forma.getObservacionesPosponer());
			return this.initOrdenesPorPaciente(mapping, forma, request, response);
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(IPSException ipse){
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresWeb");
		}
		return mapping.findForward(FORWARD_POR_PACIENTE);
	}
	
	/**
	 * Método encargado de implementar la lógica web cuando se selecciona un monto de cobro
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionSeleccionMontoCobro(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			if(forma.getCodigoMontoCobro() >= 0){
				for(MontoCobroDto dtoMonto:forma.getListaMontosCobro()){
					if(forma.getCodigoMontoCobro()==dtoMonto.getCodDetalleMonto()){
						forma.getAutorizacionCapitacion().setMontoCobroAutorizacion(dtoMonto);
						break;
					}
				}
				this.actionSeleccionEntidadSubContratada(mapping, forma, request, response);
			}
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_SECCION_AUTORIZACION);
	}
	

	/**
	 * Método encargado de implementar la lógica web cuando se realiza ordenamiento por alguna columna
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionOrdenar(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm){
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			boolean ordenamiento = false;
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
				ordenamiento = true;
			}
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(forma.getOrdenesPorAutorizarPaciente(),sortG);
			
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_POR_PACIENTE);
	}
	
	/**
	 * Metodo encargado de cargar los datos para abrir el popup de Entrega de Autorizacion 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @return
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public ActionForward accionCargarPopupEntregaPrimeraImpresion (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){

		ActionMessages errores = new ActionMessages();
		ManejoPacienteFacade manejoPacienteFacade = null;
		Long idAutorizacionEntSub = null;
		
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm) {
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			} else {
				throw new ClassCastException();
			}
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);	
				
			forma.getAutorizacionCapitacion().setAutorizacionEntrega(new AutorizacionEntregaDto());
			
			//carga de info necesaria para mostrar el popup de Entrega de autorizacion
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setFechaEntrega(UtilidadFecha.getFechaActual());
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setHoraEntrega(UtilidadFecha.getHoraActual());
			
			idAutorizacionEntSub = manejoPacienteFacade.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(forma.getAutorizacionCapitacion().getConsecutivoAutorizacion());
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setIdAutorizacionEntidadSub(idAutorizacionEntSub != null ? idAutorizacionEntSub : 0L);
	
			DtoUsuarioPersona usuarioPersona = new DtoUsuarioPersona();
			usuarioPersona.setLogin(usuario.getLoginUsuario());
			usuarioPersona.setNombreOrganizado(usuario.getNombreUsuario());
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setUsuarioEntrega(usuarioPersona);
			
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setObservaciones("");
			forma.getAutorizacionCapitacion().getAutorizacionEntrega().setPersonaRecibe("");
			
			request.setAttribute("formaPopUpPrimeraImpresion", "AutorizacionesCapitacionSubcontratadaForm");
		
		} catch(ClassCastException cce) {
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		} catch(Exception e) {
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()) {
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_POPUP_PRIMERA_IMPRESION);	
	}
	
	/**
	 * Metodo encargado de guardar los datos de la impresion original de la Autorizacion 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public ActionForward guardarDatosPrimeraImpresion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm) {
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			} else {
				throw new ClassCastException();
			}
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute(KEY_SESSION_USUARIO);
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

			MessageResources mensajes = MessageResources.getMessageResources(PROPERTIES_AUTORIZACION_CAPITACION_SUBCONTRATADA);	
			
			if(UtilidadTexto.isEmpty(forma.getPersonaRecibeAutorizacion())) {
				errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, mensajes.getMessage("detalleAdministracionAutorizacion.errorPersonaRecibeAutorizacionRequerido")));	
				forma.getAutorizacionCapitacion().setAutorizacionEntrega(null);
			
			} else {

				ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
				
				forma.getAutorizacionCapitacion().getAutorizacionEntrega().setPersonaRecibe(forma.getPersonaRecibeAutorizacion());
				forma.getAutorizacionCapitacion().getAutorizacionEntrega().setObservaciones(forma.getObservacionesEntregaAutorizacion());
				
				//guarda los datos de entrega de la autorizacion original 
				manejoPacienteFacade.guardarEntregaAutorizacionEntidadSubContratadaOriginal(forma.getAutorizacionCapitacion().getAutorizacionEntrega());
			
				//imprime la autorizacion
				if(forma.isAutorizacionServicios()) {
					this.generarReporteAutorizacionFormatoEstandarServicio(forma, usuario, institucion);
				} else {
					this.generarReporteAutorizacionFormatoEstandarArticulo(forma, usuario, institucion);
				}

			}
		
		} catch(ClassCastException cce) {
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		} catch (IPSException ipse) {
			Log4JManager.error(ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		} catch(Exception e) {
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()) {
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_RESUMEN_AUTORIZACION);	
	}
	
	/**
	 * Metodo encargado de cancelar cuando el popUp de Primera Impresion esta abierto
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 26-feb-2013
	 */
	public ActionForward cancelarPopUpPrimeraImpresion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		ActionMessages errores = new ActionMessages();
		try{
			AutorizacionesCapitacionSubContratadaForm forma=null;
			if(form instanceof AutorizacionesCapitacionSubContratadaForm) {
				forma = (AutorizacionesCapitacionSubContratadaForm) form;
			} else {
				throw new ClassCastException();
			}
			
			forma.getAutorizacionCapitacion().setAutorizacionEntrega(null);
			
		} catch(ClassCastException cce) {
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		} catch(Exception e) {
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()) {
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_RESUMEN_AUTORIZACION);	
	}
}