/**
 * 
 */
package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AmparosPorReclamarForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurips;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurpro;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAmparosPorReclamarServicio;


/**
 * @author axioma
 *
 */
public class AmparosPorReclamarAction extends Action 
{
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		

		if (form instanceof AmparosPorReclamarForm) 
		{
			AmparosPorReclamarForm forma=(AmparosPorReclamarForm)form;
			
			PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			if(forma.getEstado().equals("empezar"))
			{
				
				if(paciente.getCodigoPersona()<1)
				{
				    return ComunAction.accionSalirCasoError(mapping, request, null, "paciente null o sin  id", "errors.paciente.noCargado", true);
				}
				return accionEmpezar(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("ordenarIngresos"))
			{
				return ordenarIngresos(forma,mapping);
			}
			else if(forma.getEstado().equals("cargarIngreso"))
			{
				return accionCargarIngreso(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("ordenarFacturas"))
			{
				return ordenarFacturas(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("reclamacion"))
			{
				return accionListadoReclamaciones(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("cambiarTipoReclamacion"))
			{
				return accionCambiarTipoReclamacion(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("nuevaReclamacion"))
			{
				return nuevaReclamacion(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("guardar"))
			{
				return accionGuardarReclamacion(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("detalleReclamacion"))
			{
				return detalleReclamacion(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("iniciarReclamacion"))
			{
				forma.getAmparoXReclamar().setFechaRadicacion(UtilidadFecha.getFechaActual());
				forma.getAmparoXReclamar().setHoraRadicacion(UtilidadFecha.getHoraActual());
				return mapping.findForward("radicarReclamacion");
			}
			else if(forma.getEstado().equals("radicar"))
			{
				return accionRadicarReclamacion(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("iniciarAnulacion"))
			{
				forma.getAmparoXReclamar().setMotivoAnulacion("");
				return mapping.findForward("anularReclamacion");
			}
			else if(forma.getEstado().equals("anular"))
			{
				return accionAnularReclamacion(forma,mapping,request,paciente);
			}
			
			/*********************************ACCIONES DE CONSULTA*************************************/
			else if(forma.getEstado().equals("empezarConsulta"))
			{
				forma.reset();
				return mapping.findForward("iniciarConsulta");
			}
			else if(forma.getEstado().equals("consultaPorPaciente"))
			{

				if(paciente.getCodigoPersona()<1)
				{
				    return ComunAction.accionSalirCasoError(mapping, request, null, "paciente null o sin  id", "errors.paciente.noCargado", true);
				}
				return accionEmpezar(forma,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("consultaPorRango"))
			{
				forma.reset();
				forma.setConveniosAseguradora(Utilidades.obtenerConveniosAseguradoras());
				return mapping.findForward("busquedaAvanzada");
			}
			else if(forma.getEstado().equals("busquedaAvanzada"))
			{
				return accionEjecutarBusquedaAvanzada(forma,mapping,request);
			}
			else if(forma.getEstado().equals("cambiarTipoEventoBusqueda"))
			{
				forma.getFiltro().setTipoReclamacion("");
				return mapping.findForward("busquedaAvanzada");
			}
		}
		return null;
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEjecutarBusquedaAvanzada(AmparosPorReclamarForm forma, ActionMapping mapping,HttpServletRequest request) 
	{
		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		UtilidadTransaccion.getTransaccion().begin();
		try
		{
			forma.setListadoReclamaciones(amparosPorReclamarServicio.consultarReclamacionesBusquedaAvanzada(forma.getFiltro()));
		}
		catch(Exception e)
		{
			Log4JManager.error("ERROR CONSULTANDO LA INFORMACION",e);
		}
		finally
		{
			UtilidadTransaccion.getTransaccion().commit();
		}
		return mapping.findForward("listadoReclamacionesBusqueda");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionRadicarReclamacion(
			AmparosPorReclamarForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) {

		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		UsuarioBasico usuario =  (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		String fechaActual=UtilidadFecha.getFechaActual();
		String horaActual=UtilidadFecha.getHoraActual();
		forma.getAmparoXReclamar().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getAmparoXReclamar().setFechaModifica(fechaActual);
		forma.getAmparoXReclamar().setHoraModifica(horaActual);
		forma.getAmparoXReclamar().setUsuarioRadicacion(usuario.getLoginUsuario());
		amparosPorReclamarServicio.radicarReclamacion(forma.getAmparoXReclamar());
		return mapping.findForward("recargarDetalleRaclamacion");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionAnularReclamacion(
			AmparosPorReclamarForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) {

		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		UsuarioBasico usuario =  (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		String fechaActual=UtilidadFecha.getFechaActual();
		String horaActual=UtilidadFecha.getHoraActual();
		forma.getAmparoXReclamar().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getAmparoXReclamar().setFechaModifica(fechaActual);
		forma.getAmparoXReclamar().setHoraModifica(horaActual);
		forma.getAmparoXReclamar().setUsuarioAnulacion(usuario.getLoginUsuario());
		amparosPorReclamarServicio.anularReclamacion(forma.getAmparoXReclamar());
		return mapping.findForward("recargarDetalleRaclamacion");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward detalleReclamacion(AmparosPorReclamarForm forma,
			ActionMapping mapping, HttpServletRequest request,
			PersonaBasica paciente) 
	{
		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		forma.setAmparoXReclamar(amparosPorReclamarServicio.consultarReclamacion(forma.getListadoReclamaciones().get(forma.getIndiceReclamacionSeleccionada()).getCodigoPk()));
		return mapping.findForward("detalleReclamacion");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionListadoReclamaciones(
			AmparosPorReclamarForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) 
	{
		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		UtilidadTransaccion.getTransaccion().begin();
		try
		{
			forma.setListadoReclamaciones(amparosPorReclamarServicio.consultarReclamacionesFactura(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getCodigo()));
		}
		catch(Exception e)
		{
			Log4JManager.error("ERROR CONSULTANDO LA INFORMACION",e);
		}
		finally
		{
			UtilidadTransaccion.getTransaccion().commit();
		}
		return mapping.findForward("listadoReclamaciones");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionGuardarReclamacion(
			AmparosPorReclamarForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) {
		// TODO Auto-generated method stub
		IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
		UtilidadTransaccion.getTransaccion().begin();
		UsuarioBasico usuario =  (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		String valCons=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt());
		String anioCons=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt(), valCons);
		
		ActionMessages errores=new ActionMessages();
		
		try
		{
			String fechaActual=UtilidadFecha.getFechaActual();
			String horaActual=UtilidadFecha.getHoraActual();
			forma.getAmparoXReclamar().setUsuarioModifica(usuario.getLoginUsuario());
			forma.getAmparoXReclamar().setFechaModifica(fechaActual);
			forma.getAmparoXReclamar().setHoraModifica(horaActual);
			forma.getAmparoXReclamar().setUsuarioRegistro(usuario.getLoginUsuario());
			forma.getAmparoXReclamar().setFechaRegistro(fechaActual);
			forma.getAmparoXReclamar().setHoraRegistro(horaActual);
			forma.getAmparoXReclamar().setNroReclamacion(valCons);
			forma.getAmparoXReclamar().setAnioConsReclamacion(anioCons);
			amparosPorReclamarServicio.insertarNuevaReclamacion(forma.getAmparoXReclamar());
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt(), valCons, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			if(forma.getAmparoXReclamar().getCodigoPk()>0)
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt(), valCons, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			else
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt(), valCons, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);				
		}
		catch(Exception e)
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt(), valCons, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			Log4JManager.error("ERROR ALMACENANDO LA INFORMACION",e);
		}
		finally
		{
			UtilidadTransaccion.getTransaccion().commit();
		}
		return mapping.findForward("resumen");
		
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCambiarTipoReclamacion(
			AmparosPorReclamarForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) {
		

		if(forma.getAmparoXReclamar().getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURIPS))
		{
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastMedQX(new BigDecimal(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getValorConvenio()));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastMedQX(new BigDecimal(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getValorConvenio()));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastTransMov(new BigDecimal(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getValorConvenio()));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastTransMov(new BigDecimal(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getValorConvenio()));
			forma.getAmparoXReclamar().setCertAtenMedicaFurips(UtilidadesManejoPaciente.cargarCetificacionMedicaFurips(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getIngreso()));
			
			if(forma.getAmparoXReclamar().getCertAtenMedicaFurips().getViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios){
				if(forma.getAmparoXReclamar().getCertAtenMedicaFurips().getTipoCieDxIngreso()>0){
					forma.setTieneDxOrdenAmbulatoria(true);
					
					forma.setCodigoDiagnosticoIngreso(forma.getAmparoXReclamar().getCertAtenMedicaFurips().getAcronimoDxIngreso()+
							ConstantesBD.separadorSplit+forma.getAmparoXReclamar().getCertAtenMedicaFurips().getTipoCieDxIngreso());
					
					forma.setCodigoDiagnosticoEgreso(forma.getAmparoXReclamar().getCertAtenMedicaFurips().getAcronimoDxEgreso()+
							ConstantesBD.separadorSplit+forma.getAmparoXReclamar().getCertAtenMedicaFurips().getTipoCieDxEgreso());
				}else{
					forma.setTieneDxOrdenAmbulatoria(false);
				}
			}
			
			forma.getAmparoXReclamar().setCertAtenMedicaFurpro(new DtoCertAtenMedicaFurpro());
		}
		else if(forma.getAmparoXReclamar().getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURPRO))
		{
			forma.getAmparoXReclamar().getServiciosReclamados().setProtesis(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setAdaptacionProtesis(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setRehabilitacion(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setValorProtesis(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setValorAdaptacionProtesis(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setValorRehabilitacion(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setDescProtesisServicioPrestado("");
			forma.getAmparoXReclamar().setCertAtenMedicaFurips(new DtoCertAtenMedicaFurips());
			forma.getAmparoXReclamar().setCertAtenMedicaFurpro(UtilidadesManejoPaciente.cargarCetificacionMedicaFurpro(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getIngreso()));
		}
		else
		{
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastMedQX(new BigDecimal(0));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastMedQX(new BigDecimal(0));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastTransMov(new BigDecimal(0));
			forma.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastTransMov(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setProtesis(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setAdaptacionProtesis(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setRehabilitacion(ConstantesBD.acronimoNo);
			forma.getAmparoXReclamar().getServiciosReclamados().setValorProtesis(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setValorAdaptacionProtesis(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setValorRehabilitacion(new BigDecimal(0));
			forma.getAmparoXReclamar().getServiciosReclamados().setDescProtesisServicioPrestado("");
			forma.getAmparoXReclamar().setCertAtenMedicaFurips(new DtoCertAtenMedicaFurips());
			forma.getAmparoXReclamar().setCertAtenMedicaFurpro(new DtoCertAtenMedicaFurpro());
		}
		
		
		
		return mapping.findForward("nuevaReclamacion");
		
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward nuevaReclamacion(AmparosPorReclamarForm forma,
			ActionMapping mapping, HttpServletRequest request,
			PersonaBasica paciente) 
	{
		
		UsuarioBasico usuario =  (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		boolean tieneReclamacionesEstadoGenerado=false;
		if(forma.getListadoReclamaciones()!=null && forma.getListadoReclamaciones().size()>0)
		{
			for(DtoReclamacionesAccEveFact reclama:forma.getListadoReclamaciones())
			{
				if(reclama.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
				{
					tieneReclamacionesEstadoGenerado=true;
					break;
				}
			}
		}
		if(tieneReclamacionesEstadoGenerado)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, "Ya tiene reclamaciones en estado Generado", "Ya tiene reclamaciones en estado Generado", false);
		}
		
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(usuario.getCodigoInstitucionInt())))
		{
			if(!forma.getIngresoSeleccionado().getEstadoEvento().equals(ConstantesIntegridadDominio.acronimoEstadoProcesado)&&!forma.getIngresoSeleccionado().getEstadoEvento().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, "Solo se permiten reclamaciones a eventos Finalizados", "Solo se permiten reclamaciones a eventos Finalizados", false);
			}
		}
		int numMaxiomReclamaciones=Utilidades.convertirAEntero(ValoresPorDefecto.getNumMaximoReclamacionesAccEventoXFactura(usuario.getCodigoInstitucionInt()));
		if(numMaxiomReclamaciones>0&&numMaxiomReclamaciones<=numeroReclamacionesRadicadas(forma))
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, "Se ha superado la cantidad de reclamaciones permitidas por factura. No es posible generar otra reclamación sobre esta factura. Por favor verifique'.", "Se ha superado la cantidad de reclamaciones permitidas por factura. No es posible generar otra reclamación sobre esta factura. Por favor verifique'.", false);
		}
		
		forma.setAmparoXReclamar(new DtoReclamacionesAccEveFact());
		forma.getAmparoXReclamar().setCodigoFactura(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getCodigo());
		forma.getAmparoXReclamar().setIngreso(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getIngreso());
		forma.getAmparoXReclamar().setTipoEvento(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getTipoEvento());
		forma.getAmparoXReclamar().setFechaReclamacion(UtilidadFecha.getFechaActual());
		forma.getAmparoXReclamar().setHoraReclamacion(UtilidadFecha.getHoraActual());
		if(forma.getAmparoXReclamar().getTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			forma.getAmparoXReclamar().setCodigoAccidente(Utilidades.convertirAEntero(forma.getIngresoSeleccionado().getCodigoEvento()));
		}
		else
		{
			forma.getAmparoXReclamar().setCodigoEvento(Utilidades.convertirAEntero(forma.getIngresoSeleccionado().getCodigoEvento()));
		}
		String radicacionPrevia=UtilidadesManejoPaciente.facturaTieneReclamacionRadicadaPrevia(forma.getFacturas().get(forma.getIndiceFacturaSeleccionado()).getCodigo());
		if(!UtilidadTexto.isEmpty(radicacionPrevia))
		{
			forma.setTienerReclamacionRadicadaPrevia(true);
			forma.getAmparoXReclamar().setNumRadicacionAnterior(radicacionPrevia);
		}
		else
		{
			forma.setTienerReclamacionRadicadaPrevia(false);
			forma.getAmparoXReclamar().setNumRadicacionAnterior("");
		}
		if(forma.getAmparoXReclamar().getTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			forma.getAmparoXReclamar().setTipoReclamacion(ConstantesIntegridadDominio.acronimoFURIPS);
			return accionCambiarTipoReclamacion(forma, mapping, request, paciente);
		}

		return mapping.findForward("nuevaReclamacion");
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private int numeroReclamacionesRadicadas(AmparosPorReclamarForm forma) 
	{
		int cont=0;
		for(int i=0;i<forma.getListadoReclamaciones().size();i++)
		{
			if(forma.getListadoReclamaciones().get(i).getEstado().equals(ConstantesIntegridadDominio.acronimoRadicado))
				cont++;
		}
		return cont;
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward ordenarFacturas(AmparosPorReclamarForm forma,
			ActionMapping mapping, HttpServletRequest request,
			PersonaBasica paciente) {
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente()!=null&&forma.getEsDescendente().equals(forma.getPropiedadOrdenar()))
		{
			forma.setEsDescendente(forma.getPropiedadOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPropiedadOrdenar());
		}	
		
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPropiedadOrdenar(),ordenamiento);
		Collections.sort(forma.getFacturas(),sortG);
		return mapping.findForward("listadoFacturasIngreso");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCargarIngreso(AmparosPorReclamarForm forma,ActionMapping mapping, HttpServletRequest request,PersonaBasica paciente) 
	{
		forma.setFacturas(UtilidadesManejoPaciente.consultarFacturasIngresosConveniosAseguradoras(forma.getIngresos().get(forma.getIndiceIngresoSeleccionado()).getIngreso()));
		return mapping.findForward("listadoFacturasIngreso");	
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward ordenarIngresos(AmparosPorReclamarForm forma,
			ActionMapping mapping) {
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente()!=null&&forma.getEsDescendente().equals(forma.getPropiedadOrdenar()))
		{
			forma.setEsDescendente(forma.getPropiedadOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPropiedadOrdenar());
		}	
		
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPropiedadOrdenar(),ordenamiento);
		Collections.sort(forma.getIngresos(),sortG);
		return mapping.findForward("listadoIngresos");
	}


	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionEmpezar(AmparosPorReclamarForm forma,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		UsuarioBasico usuario =  (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		Connection con=UtilidadBD.abrirConexion();
		if(Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con,  ConstantesBD.nombreConsecutivoAmparosXReclamar, usuario.getCodigoInstitucionInt()))<=0)
		{
			  return ComunAction.accionSalirCasoError(mapping, request, con, "Falta definir el consecutivo para Amparos X Reclamar", "Falta definir el consecutivo para Amparos X Reclamar", false);
		}
		forma.setIngresos(UtilidadesManejoPaciente.consultarIngresosRegistrosEventosYAccidentes(paciente.getCodigoPersona()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoIngresos");	
	}
	
	
}
