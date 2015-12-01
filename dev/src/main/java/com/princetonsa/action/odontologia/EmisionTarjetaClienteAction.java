package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.clonacion.UtilidadClonacion;

import com.princetonsa.actionform.odontologia.EmisionTarjetaClienteForm;
import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.DetalleEmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.EmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.HistoricoDetalleEmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.HistoricoEmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;
import com.princetonsa.sort.odontologia.SortGenerico;

public class EmisionTarjetaClienteAction extends Action { 

	private Logger logger = Logger.getLogger(EmisionTarjetaClienteAction.class);
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof EmisionTarjetaClienteForm) 
		{
			
			EmisionTarjetaClienteForm forma = (EmisionTarjetaClienteForm) form;
			logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxestado-->" + forma.getEstado());
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			ActionErrors errores = new ActionErrors();
			logger.info("*******************************************************************************");
			logger.info(" -------------------------Estado--->"+forma.getEstado());
			logger.info("*******************************************************************************");
			
			
			if (forma.getEstado().equals("empezar")) 
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			if (forma.getEstado().equals("empezarDetalle")) 
				
			{
				return accionEmpezarDetalle(mapping, forma, usuario, response, request);
			}
			else if (forma.getEstado().equals("ingresar")) 
			{
				return accionIngresar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ingresarDetalle")) 
			{
				return accionIngresarDetalle(mapping, forma, usuario);
			}
						
			else if (forma.getEstado().equals("modificar")) 
			{
				return accionModificar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("modificar2")) 
			{
				return accionModificar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("modificarDetalle")) 
			{
				return accionModificarDetalle(mapping, forma, usuario, errores, request);
			}
			
			else if (forma.getEstado().equals("guardar")) 
			{
				return accionGuardar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("guardarDetalle")) 
			{
				return accionGuardarDetalle(mapping, forma, usuario, response, request);
			}
			
			else if (forma.getEstado().equals("guardarModificar")) 
			{
				return accionGuardarModificar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("guardarModificar2")) 
			{
				return accionGuardarModificar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("guardarModificarDetalle")) 
			{
				return accionGuardarModificarDetalle(mapping, forma, usuario, response, request);
			}
			else if (forma.getEstado().equals("ordenar")) 
			{
				return accionOrdenar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ordenar_detalle")) 
			{
				return accionOrdenarDetalle(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("eliminar")) 
			{
				return accionEliminar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("eliminarDetalle")) 
			{
				return accionEliminarDetalle(mapping, forma, usuario, response, request, errores);
			}
			else if (forma.getEstado().equals("posSerialInicial")) 
			{
				return accionPostularSerialInicial(mapping, forma, usuario, errores, request);
			}
			
			else if (forma.getEstado().equals("llenarUsuarios")) 
			{
				return accionllenarUsuarios(mapping, forma);
			}
			else if(forma.getEstado().equals("volverEncabezado"))
			{		
				logger.info("LINK SIGUIENTE ENCABEZADO: "+forma.getLinkSiguiente());
                return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),forma.getListaEmisiones().size(), response, request, "emisionTarjetaCliente.jsp", false);				
			}
			else if(forma.getEstado().equals("redireccionar"))
			{
				response.sendRedirect(forma.getLinkSiguiente());
			}
			else if(forma.getEstado().equals("redireccionarDetalle"))
			{
				response.sendRedirect(forma.getLinkSiguienteDetalle());
			}
		}
	return null;	
	}
	private ActionForward accionllenarUsuarios(ActionMapping mapping,
			EmisionTarjetaClienteForm forma) {
		
        
		
		
		if(forma.getPosArrayConvenio() > ConstantesBD.codigoNuncaValido){
			
			
		if(!UtilidadTexto.isEmpty((String.valueOf(forma.getCentrosAtencion().get("consecutivo_"+forma.getPosArrayConvenio()))))){
			
			logger.info("CENTRO ATENCION UTILIZADO"+" "+(String.valueOf(forma.getCentrosAtencion().get("consecutivo_"+forma.getPosArrayConvenio()))));
		forma.setArrayUsuarios(UtilidadesAdministracion.obtenerUsariosCentroCosto(Integer.parseInt(String.valueOf(forma.getCentrosAtencion().get("consecutivo_"+forma.getPosArrayConvenio())))));
		}else{
			forma.setArrayUsuarios(new ArrayList<InfoDatosStr>());
		}
		}else{
			forma.setArrayUsuarios(new ArrayList<InfoDatosStr>());
		}
		
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("paginaDetalle");
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param errores
	 * @param request
	 * @return
	 */
	private ActionForward accionPostularSerialInicial(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) {
		
		if(forma.getPosTarjeta() > ConstantesBD.codigoNuncaValido){
	    forma.getDtoEmision().setSerialInicial(new BigDecimal(forma.getListTarjetasCliente().get(forma.getPosTarjeta()).getConsecutivoSerial()));
		    if(forma.getDtoEmision().getSerialInicial().intValue()<=0)
		    {
		    	errores.add("", new ActionMessage("errors.notEspecific", "El tipo de tarjeta seleccionado, No tiene 'Consecutivo Serial' parametrizado. NO se permite realizar Emisión de tarjetas. Por favor verifique"));
				saveErrors(request, errores);
		    }
	    
	    //forma.getDtoEmision().getTipoTargeta().setDescripcion(forma.getListTarjetasCliente().get(forma.getPosTarjeta()).getConvenio().getNombre());
		}else{
			
			forma.getDtoEmision().setSerialInicial(new BigDecimal(0));
		
		}
		
		logger.info("Estado Anterior ==="+forma.getEstadoAnterior());
		forma.setEstado(forma.getEstadoAnterior());
		
		
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @return
	 */
	public DtoHistoricoDetalleEmisionTarjetaCliente llenarHistoricoAlmacenar(EmisionTarjetaClienteForm forma){
		
		DtoHistoricoDetalleEmisionTarjetaCliente dtoHis = new DtoHistoricoDetalleEmisionTarjetaCliente();
		dtoHis.setCodigoDetalleEmision(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getCodigo());
		dtoHis.setCentroAtencion(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getCentroAtencion());
		dtoHis.setSerialInicial(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialInicial().doubleValue());
		dtoHis.setSerialFinal(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialFinal().doubleValue());
		dtoHis.setEliminado(ConstantesBD.acronimoSi);
		dtoHis.setUsuarioResponsable(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getUsuarioResponsable());
		dtoHis.setFechaModifica(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getFechaModifica());
		dtoHis.setHoraModifica(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getHoraModifica());
		dtoHis.setUsuarioModifica(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getUsuarioModifica());
		return dtoHis;
		
		
	}
	
	/**	
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @return
	 */
	public DtoHistoricoEmisionTarjetaCliente llenarEncabezadoHistoricoAlmacenar(EmisionTarjetaClienteForm forma){
		
		DtoHistoricoEmisionTarjetaCliente dtoHis = new DtoHistoricoEmisionTarjetaCliente();
		dtoHis.setCodigoEmisionTarjeta(forma.getListaEmisiones().get(forma.getPosArray()).getCodigo());
		dtoHis.setEliminado(ConstantesBD.acronimoSi);
		dtoHis.setTipoTarjeta(forma.getListaEmisiones().get(forma.getPosArray()).getTipoTarjeta());
		dtoHis.setSerialInicial(forma.getListaEmisiones().get(forma.getPosArray()).getSerialInicial().doubleValue());
		dtoHis.setSerialFinal(forma.getListaEmisiones().get(forma.getPosArray()).getSerialFinal().doubleValue());
		
		dtoHis.setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(forma.getListaEmisiones().get(forma.getPosArray()).getFechaModifica()));
		dtoHis.setHoraModifica(forma.getListaEmisiones().get(forma.getPosArray()).getHoraModifica());
		dtoHis.setUsuarioModifica(forma.getListaEmisiones().get(forma.getPosArray()).getUsuarioModifica());
		return dtoHis;
		
		
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param request
	 * @param errores
	 * @return
	 */
	private ActionForward accionEliminarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request, ActionErrors errores) {
		
		DtoHistoricoDetalleEmisionTarjetaCliente dtoWhereHistorico = new DtoHistoricoDetalleEmisionTarjetaCliente();
		DtoDetalleEmisionesTarjetaCliente dtoWhere= new DtoDetalleEmisionesTarjetaCliente();
		
		dtoWhere.setCodigo(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getCodigo());
		dtoWhereHistorico.setCodigoDetalleEmision(dtoWhere.getCodigo());
		
		DtoHistoricoDetalleEmisionTarjetaCliente dtoNuevoHistorico = new DtoHistoricoDetalleEmisionTarjetaCliente();
		
		
		dtoNuevoHistorico.setEliminado(ConstantesBD.acronimoSi);
		
		
		
		logger.info("EL CODIGO DEL ELEMENTO A ELIMINAR ES ****************************************************************"+this.llenarHistoricoAlmacenar(forma).getCodigoDetalleEmision());
		
		
		/**
		 * 
		 */
		if( BeneficiariosTarjetaCliente.validarSerialesRangoBeneficiarios(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialInicial().doubleValue() , forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialFinal().doubleValue(), usuario.getCodigoInstitucionInt()) )
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Se Permite Eliminacion, Los seriales ya estan asignados a un Beneficiario"));
			saveErrors(request, errores);
			forma.setEstado("empezarDetalle");
			return mapping.findForward("paginaDetalle");
		}
		if( VentasTarjetasCliente.existeRangoEnVenta(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialInicial().doubleValue() , forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getSerialFinal().doubleValue(), usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNo /*	aliado */ ))
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Se Permite Eliminacion, Los seriales ya estan asignados en una Venta Tarjeta Cliente"));
			saveErrors(request, errores);
			forma.setEstado("empezarDetalle");
			return mapping.findForward("paginaDetalle");
		}
		
		HistoricoDetalleEmisionTarjetaCliente.modificar(dtoNuevoHistorico, dtoWhereHistorico);
		HistoricoDetalleEmisionTarjetaCliente.guardar(this.llenarHistoricoAlmacenar(forma));
		DetalleEmisionTarjetaCliente.eliminar(dtoWhere);
		
		forma.setEstado("empezarDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario, response, request);
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		DtoHistoricoEmisionTarjetaCliente dtoWhereHistorico = new DtoHistoricoEmisionTarjetaCliente();
		DtoEmisionTarjetaCliente dtoWhere= new DtoEmisionTarjetaCliente();
		
		dtoWhere.setCodigo(forma.getListaEmisiones().get(forma.getPosArray()).getCodigo());
		dtoWhereHistorico.setCodigoEmisionTarjeta(dtoWhere.getCodigo());
		
		DtoHistoricoEmisionTarjetaCliente dtoNuevoHistorico = new DtoHistoricoEmisionTarjetaCliente();
		
		
		dtoNuevoHistorico.setEliminado(ConstantesBD.acronimoSi);
		
		
		EmisionTarjetaCliente.eliminar(dtoWhere);
		
		HistoricoEmisionTarjetaCliente.modificar(dtoNuevoHistorico, dtoWhereHistorico);
		HistoricoEmisionTarjetaCliente.guardar(this.llenarEncabezadoHistoricoAlmacenar(forma));
		forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario);
		
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaDetalleEmisiones(),sortG);
		forma.setEstado("empezarDetalle");
		return mapping.findForward("paginaDetalle");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) {
		

		DtoDetalleEmisionesTarjetaCliente dtoWhere= new DtoDetalleEmisionesTarjetaCliente(); 
		
	
		dtoWhere.setCodigo(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).getCodigo());
		DtoDetalleEmisionesTarjetaCliente dtoNuevo= new DtoDetalleEmisionesTarjetaCliente(); 		
		
		dtoNuevo=forma.getDtoDetalleEmision();
		
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoNuevo.setUsuarioModifica(usuario.getLoginUsuario());
		
		logger.info("Viejo"+UtilidadLog.obtenerString(dtoWhere, true));
		logger.info("Nuevo"+UtilidadLog.obtenerString(dtoNuevo, true));
	   DetalleEmisionTarjetaCliente.modificar(dtoNuevo, dtoWhere);
	  
	     logger.info(forma.getDtoDetalleEmision());
	     
	     HistoricoDetalleEmisionTarjetaCliente.guardar(forma.getDtoHistoricoDetalleEmision());
	     
	  
	   
	    forma.setEstado("empezarDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario, response, request);
		
	}
	
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param errores
	 * @return
	 */
	
	private ActionForward accionModificarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request ) {
		
		
		logger.info("*************************************************************************************************************");
		logger.info("************************************** MODIFICANDO  EMISION DE SERIALES ***********************************************************************");
		
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.setDtoDetalleEmision((DtoDetalleEmisionesTarjetaCliente)forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).clone());
	
		logger.info("VALIDAR--------------");
		if( BeneficiariosTarjetaCliente.validarSerialesRangoBeneficiarios(forma.getDtoDetalleEmision().getSerialInicial().doubleValue(), forma.getDtoDetalleEmision().getSerialFinal().doubleValue() , usuario.getCodigoInstitucionInt()) )
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Se Permite Eliminacion, Los seriales ya estan asignados a un Beneficiario"));
			saveErrors(request, errores);
			forma.setEstado("empezarDetalle");
			return mapping.findForward("paginaDetalle");
		}
		
		if( VentasTarjetasCliente.existeRangoEnVenta(forma.getDtoDetalleEmision().getSerialInicial().doubleValue() ,forma.getDtoDetalleEmision().getSerialFinal().doubleValue() , usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNo /* ALIADO NO */) )
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Se Permite Eliminacion, Los seriales ya estan asignados en una Venta Tarjeta Cliente"));
			saveErrors(request, errores);
			forma.setEstado("empezarDetalle");
			return mapping.findForward("paginaDetalle");
		}
		
		
	
		logger.info("llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
		logger.info("				NO TIENE NI VENTAS NI BENEFICIARIOS ASOCIADOS		");
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.setDtoDetalleEmision((DtoDetalleEmisionesTarjetaCliente)forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()).clone());
		
		logger.info("El coso aca arriba es "+ UtilidadLog.obtenerString(forma.getListaDetalleEmisiones().get(forma.getPosArrayDetalle()), true));
		forma.setArrayUsuarios(UtilidadesAdministracion.obtenerUsariosCentroCosto(forma.getDtoDetalleEmision().getCentroAtencion().getCodigo()));
		forma.getDtoHistoricoDetalleEmision().setCodigoDetalleEmision(forma.getDtoDetalleEmision().getCodigo());
		forma.getDtoHistoricoDetalleEmision().setSerialInicial(forma.getDtoDetalleEmision().getSerialInicial().doubleValue());
		forma.getDtoHistoricoDetalleEmision().setSerialFinal(forma.getDtoDetalleEmision().getSerialFinal().doubleValue());
		
		forma.getDtoHistoricoDetalleEmision().setCentroAtencion(forma.getDtoDetalleEmision().getCentroAtencion());
		forma.getDtoHistoricoDetalleEmision().setFechaModifica(forma.getDtoDetalleEmision().getFechaModifica());
		forma.getDtoHistoricoDetalleEmision().setHoraModifica(forma.getDtoDetalleEmision().getHoraModifica());
		forma.getDtoHistoricoDetalleEmision().setUsuarioModifica(forma.getDtoDetalleEmision().getUsuarioModifica());
		forma.getDtoHistoricoDetalleEmision().setUsuarioResponsable(forma.getDtoDetalleEmision().getUsuarioResponsable());
		forma.getDtoHistoricoDetalleEmision().setEliminado(ConstantesBD.acronimoNo);
		logger.info(UtilidadLog.obtenerString(forma.getDtoHistoricoDetalleEmision(), true));
				
		return mapping.findForward("paginaDetalle");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) {
		
		forma.getDtoDetalleEmision().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoDetalleEmision().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleEmision().setUsuarioModifica(usuario.getLoginUsuario());
	
		forma.getDtoDetalleEmision().setCodigoEmisiontargeta(forma.getListaEmisiones().get(forma.getPosArray()).getCodigo());
		
		forma.getDtoDetalleEmision().setInstitucion(usuario.getCodigoInstitucionInt());
		
		//logger.info(UtilidadLog.obtenerString(forma.getDtoEmision(), true));
		DetalleEmisionTarjetaCliente.guardar(forma.getDtoDetalleEmision());
		forma.setEstado("empezarDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario, response, request);
		
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaEmisiones(),sortG);
		forma.setEstado("empezar");
		return mapping.findForward("paginaPrincipal");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		DtoEmisionTarjetaCliente dtoWhere= new DtoEmisionTarjetaCliente(); 
		dtoWhere.setCodigo(forma.getListaEmisiones().get(forma.getPosArray()).getCodigo());
		DtoEmisionTarjetaCliente dtoNuevo= new DtoEmisionTarjetaCliente(); 		
		
		dtoNuevo=forma.getDtoEmision();
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoNuevo.setUsuarioModifica(usuario.getLoginUsuario());
		//forma.getDtoHistoricoDescuentoOdontologico().setFechaInicioVigenciaMod(dtoNuevo.getFechaInicioVigenciaFromatoBD());
		//forma.getDtoHistoricoDescuentoOdontologico().setFechaFinVigenciaMod(dtoNuevo.getFechaFinVigenciaFromatoBD());
		logger.info("Viejo"+UtilidadLog.obtenerString(dtoWhere, true));
		logger.info("Nuevo"+UtilidadLog.obtenerString(dtoNuevo, true));
	   EmisionTarjetaCliente.modificar(dtoNuevo, dtoWhere);
	  //  HistoricoDescuentoOdontologico.guardar(forma.getDtoHistoricoDescuentoOdontologico());
	     logger.info(forma.getDtoEmision());
	     
	     HistoricoEmisionTarjetaCliente.guardar(forma.getDtoHistoricoEmision());
	     
	     logger.info("Log"+UtilidadLog.obtenerString(forma.getDtoHistoricoDetalleEmision(), true));
	  //  forma.setDtoHistoricoDescuentoOdontologico(new DtoHistoricoDescuentoOdontologico());
	   forma.getDtoHistoricoEmision().reset();
	    forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario);
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) {
	
		DtoDetalleEmisionesTarjetaCliente dtoWhere = new DtoDetalleEmisionesTarjetaCliente();
		dtoWhere.setCodigoEmisiontargeta(forma.getListaEmisiones().get(forma.getPosArray()).getCodigo());
		forma.setListaDetalleEmisiones(DetalleEmisionTarjetaCliente.cargar(dtoWhere));
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguienteDetalle(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), forma.getListaDetalleEmisiones().size(), response, request, "detalleEmisionTarjetaCliente.jsp", false);
		//return mapping.findForward("paginaDetalle");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarDetalle(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		forma.getDtoDetalleEmision().reset();
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.getDtoDetalleEmision().setInstitucion(usuario.getCodigoInstitucionInt());
		return mapping.findForward("paginaDetalle");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
           forma.reset();
           forma.setListaEmisiones(EmisionTarjetaCliente.cargar(new DtoEmisionTarjetaCliente()));
           return mapping.findForward("paginaPrincipal");
	}
	
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		
		forma.getDtoEmision().reset();
		DtoTarjetaCliente dto =new DtoTarjetaCliente();
		
		
		dto.setAliado(ConstantesBD.acronimoNo); //???
		forma.getDtoEmision().getTipoTarjeta().setDescripcion("");
		
		forma.setListTarjetasCliente(TarjetaCliente.cargar(dto));
		
		forma.getDtoEmision().setInstitucion(usuario.getCodigoInstitucionInt());
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) {
		
		
		
		
		
		
		
		DtoTarjetaCliente dto =new DtoTarjetaCliente();
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListTarjetasCliente(TarjetaCliente.cargar(dto));
		
		DtoEmisionTarjetaCliente original=forma.getListaEmisiones().get(forma.getPosArray());
		forma.setDtoEmision((DtoEmisionTarjetaCliente)UtilidadClonacion.clonar(original));
		
		

    	DtoDetalleEmisionesTarjetaCliente dtoDetalle = new DtoDetalleEmisionesTarjetaCliente();
  		dtoDetalle.setCodigoEmisiontargeta(forma.getDtoEmision().getCodigo());
  		if(DetalleEmisionTarjetaCliente.cargar(dtoDetalle).size() > 0){
  			//EXISTE DETALLE
  			forma.setExisteDetalle(ConstantesBD.acronimoSi);
    	}
  		else
  		{
  			forma.setExisteDetalle(ConstantesBD.acronimoNo);
  		}


		
		
		
  		
		/*
		DtoBeneficiarioCliente dtoBene = new DtoBeneficiarioCliente();
		dtoBene.getTipoTarjetaCliente().setCodigoPk(Utilidades.convertirADouble(forma.getDtoEmision().getTipoTargeta().getCodigo()));
		if(BeneficiariosTarjetaCliente.cargar(dtoBene).size()>0)
		{
			
		}
		
		DtoVentaTarjetasCLiente dtoVenta = new DtoVentaTarjetasCLiente();
		dtoVenta.setTipoTarjeta( Utilidades.convertirADouble(forma.getDtoEmision().getTipoTargeta().getCodigo()));
		if(	VentasTarjetasCliente.cargar( dtoVenta).size()>0)
		{
	
		}
		
		*/
		forma.getDtoEmision().setInstitucion(usuario.getCodigoInstitucionInt());
		
		forma.getDtoHistoricoEmision().setCodigoEmisionTarjeta(forma.getDtoEmision().getCodigo());
		
		forma.getDtoHistoricoEmision().setSerialInicial(forma.getDtoEmision().getSerialInicial().doubleValue());
		forma.getDtoHistoricoEmision().setSerialFinal(forma.getDtoEmision().getSerialFinal().doubleValue());
		forma.getDtoHistoricoEmision().setTipoTarjeta(forma.getDtoEmision().getTipoTarjeta());
		forma.getDtoHistoricoEmision().setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoEmision().getFechaModifica()));
		forma.getDtoHistoricoEmision().setHoraModifica(forma.getDtoEmision().getHoraModifica());
		forma.getDtoHistoricoEmision().setUsuarioModifica(forma.getDtoEmision().getUsuarioModifica());
		forma.getDtoHistoricoEmision().setEliminado(ConstantesBD.acronimoNo);
		
		

		forma.setEstadoAnterior("modificar");
		
		
		return mapping.findForward("paginaPrincipal");
		
		
		
		
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
			EmisionTarjetaClienteForm forma, UsuarioBasico usuario) 
	{
			
		
		forma.getDtoEmision().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoEmision().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoEmision().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoEmision().setInstitucion(usuario.getCodigoInstitucionInt());
		
		logger.info(UtilidadLog.obtenerString(forma.getDtoEmision(), true));
		
		EmisionTarjetaCliente.guardar(forma.getDtoEmision());
		forma.setEstado("empezar");
		return accionEmpezar(mapping, forma, usuario);
		
	}
	
	
	
}
