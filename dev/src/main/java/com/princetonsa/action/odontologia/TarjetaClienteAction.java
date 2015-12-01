package com.princetonsa.action.odontologia;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.TarjetaClienteForm;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.EmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;
import com.princetonsa.sort.odontologia.SortTarjetaCliente;


/**
 * 
 * @author axioma
 *
 */
public class TarjetaClienteAction extends Action 
{
	private Logger logger = Logger.getLogger(TarjetaClienteAction.class);
	TarjetaCliente tarjetaCliente = new TarjetaCliente();
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
								 	ActionForm form,
								 	HttpServletRequest request,
								 	HttpServletResponse response) throws Exception
	{
		if(form instanceof TarjetaClienteForm)
		{
			TarjetaClienteForm forma= (TarjetaClienteForm)form;
            UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
            ActionErrors errores = new ActionErrors(); 
            logger.info("************************************************************************estado-->"+forma.getEstado());
            if(forma.getEstado().equals("empezar"))
            {
            	return accionEmpezar(mapping, forma, usuario);
            }
            else if(forma.getEstado().equals("guardar"))
            {
            	return accionGuardar(mapping, forma, usuario);
            }
            else if(forma.getEstado().equals("mostrarErrores") || forma.getEstado().equals("mostrarErroresModificar"))
            {
            	return mapping.findForward("paginaPrincipal");
            }
            else if(forma.getEstado().equals("nuevo"))
            {
            	return accionNuevo(mapping, forma, usuario);
            }
            else if(forma.getEstado().equals("ordenar"))
            {    
            	return accionOrdenar(mapping,  forma);
            }
            else if(forma.getEstado().equals("modificar"))
            {
            	return accionModificar(mapping, forma);                   	
            }
            else if(forma.getEstado().equals("guardarModificar"))
            {
	            return guardarModificar(mapping, forma, usuario);
            }	
            else if(forma.getEstado().equals("eliminar"))
            {
            	return accionEliminar(mapping, forma, usuario,  errores, request);
            }
      
            else
            {
            	return accionIncorrecta(mapping, request, forma);
            }
           
            	
            	
        }
		return null;
	}//fin 


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward guardarModificar(ActionMapping mapping,
											TarjetaClienteForm forma,
											UsuarioBasico usuario) 
	{
		//forma.setTarjetaCliente(    forma.getListTarjetasCliente().get(forma.getPosArray()));
		forma.getTarjetaCliente().setCodigoPk(forma.getListTarjetasCliente().get(forma.getPosArray()).getCodigoPk());
		forma.getTarjetaCliente().setFechaModificada(UtilidadFecha.getFechaActual());
		forma.getTarjetaCliente().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getTarjetaCliente().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getTarjetaCliente().setInstitucion(usuario.getCodigoInstitucionInt());
		
		TarjetaCliente.modificar(forma.getTarjetaCliente());
		forma.reset();
		forma.setListTarjetasCliente(TarjetaCliente.cargar(forma.getTarjetaCliente()));
   	    forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping,
										TarjetaClienteForm forma, UsuarioBasico usuario)  
	{
		forma.reset();
		this.accionCargarCodigoTarifario(forma, usuario);
		forma.getTarjetaCliente().setInstitucion(usuario.getCodigoInstitucionInt()); 
		forma.setListTarjetasCliente(TarjetaCliente.cargar(forma.getTarjetaCliente()));
		forma.setTarjetaCliente(new DtoTarjetaCliente());
		forma.getTarjetaCliente().setAliado(ConstantesBD.acronimoNo);
		return mapping.findForward("paginaPrincipal");
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,
										TarjetaClienteForm forma, UsuarioBasico usuario , ActionErrors errores, HttpServletRequest request)  
	{
		DtoTarjetaCliente dtoEliminar= new DtoTarjetaCliente();
		dtoEliminar.setCodigoPk(forma.getListTarjetasCliente().get(forma.getPosArray()).getCodigoPk());
		dtoEliminar.setInstitucion(usuario.getCodigoInstitucionInt());
		
		DtoVentaTarjetasCliente dto = new DtoVentaTarjetasCliente();
		dto.setTipoTarjeta(dtoEliminar.getCodigoPk());
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		
		
		DtoBeneficiarioCliente dtoBene = new DtoBeneficiarioCliente();
		dtoBene.setTipoTarjetaCliente(dtoEliminar);
		dtoBene.setInstitucion(usuario.getCodigoInstitucionInt());
		
		DtoEmisionTarjetaCliente dtoEmision = new DtoEmisionTarjetaCliente();
		dtoEmision.getTipoTarjeta().setCodigo(dtoEliminar.getCodigoPk()+"");
		dtoEmision.setInstitucion(usuario.getCodigoInstitucionInt());
		
		if(BeneficiariosTarjetaCliente.existeTarjetasBeneficiarios(dtoBene))
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Tipo de Tarjeta Cliente  Ya Tiene un Benficiario Asociado"));
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");
		}
		else if(VentasTarjetasCliente.cargar(dto).size()>0)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Tipo de Tarjeta Cliente Tiene ventas asociadas"));
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");
		}
		else if (EmisionTarjetaCliente.cargar(dtoEmision).size()>0)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "El Tipo de Tarjeta Cliente  Ya esta Resgistrada en la Funcionalidad  Emision Tarjeta Cliente Odontologico"));
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");
		}
		else
		{
			TarjetaCliente.eliminar(dtoEliminar);
			forma.reset();
			forma.getTarjetaCliente().setInstitucion(usuario.getCodigoInstitucionInt()); 
			forma.setListTarjetasCliente(TarjetaCliente.cargar(forma.getTarjetaCliente()));
			forma.setTarjetaCliente(new DtoTarjetaCliente());
			forma.getTarjetaCliente().setAliado(ConstantesBD.acronimoNo);
			forma.setEstado("resumen");
			return mapping.findForward("paginaPrincipal");
		}
		
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(ActionMapping mapping,
											TarjetaClienteForm forma, 
											UsuarioBasico usuario) 
	{
		forma.getTarjetaCliente().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListTarjetasCliente(TarjetaCliente.cargar(forma.getTarjetaCliente()));
		return mapping.findForward("paginaPrincipal");
	}
    /**
     * 
     * @param mapping
     * @param forma
     * @return
     */
	private ActionForward accionModificar(	ActionMapping mapping,
											TarjetaClienteForm forma) 
	{
		forma.setTarjetaCliente(forma.getListTarjetasCliente().get(forma.getPosArray()));
		
		//this.accionValidarConsecutivoSerial(forma);
		
		return mapping.findForward("paginaPrincipal");
	}

	
	
	/**
	 * Metodo que valida si un consecutivo se puede valiar o no
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionValidarConsecutivoSerial(TarjetaClienteForm forma) {
		
		boolean esModificableConsecutivoSerial= EmisionTarjetaCliente.existeTarjetaRegistrada(forma.getTarjetaCliente());
		forma.setEsModificableConsecutivoSerial(esModificableConsecutivoSerial);
		
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @return
	 */
	private ActionForward accionIncorrecta(	ActionMapping mapping,
											HttpServletRequest request, 
											TarjetaClienteForm forma) 
	{
		forma.reset();	
		logger.warn("Estado no valido dentro del flujo de Tarjetas Cliente Odon (null) ");
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		return mapping.findForward("paginaError");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
										TarjetaClienteForm forma, 
										UsuarioBasico usuario) 
	{
		forma.getTarjetaCliente().setFechaModificada(UtilidadFecha.getFechaActual());
		forma.getTarjetaCliente().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getTarjetaCliente().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getTarjetaCliente().setInstitucion(usuario.getCodigoInstitucionInt());
		TarjetaCliente.guardar(forma.getTarjetaCliente());
		forma.reset();
		forma.setListTarjetasCliente(TarjetaCliente.cargar(forma.getTarjetaCliente()));
		forma.setEstado("resumen");
		return accionNuevo(mapping, forma, usuario);
	}

	/**
	 * Metodo accion Empezar
	 * Este Metodo cargar las lista de tarjeta ya persistidas en el sistema.
	 * Cargar la lista de Convenios
	 * Cargar el Codigo Tarifario
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
										TarjetaClienteForm forma,
										UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setListConvenios(Utilidades.obtenerConvenios("", ""/*tipoContrato*/, false/*verificarVencimientoContrato*/, ""/*fechaReferencia*/, false/*activo*/,ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico, "", "" /*Maneja Promociones*/));
		
		
		
		return accionConsultar(mapping, forma, usuario);
	}
	

	/**
	 * Metodo para Cargar el codigo Tarifario 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarCodigoTarifario(TarjetaClienteForm forma,
											UsuarioBasico usuario) 
	{
		/*
		 *Cargar codigo tarifa 
		 */
		forma.setCodigoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		
		/*
		 * Cargar Nombre Tarifa 
		 */
		forma.setNombreTarifa(ValoresPorDefecto.getNombreManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,  TarjetaClienteForm forma) 
	{
		Collections.sort(forma.getListTarjetasCliente(), new SortTarjetaCliente(forma.getPatronOrdenar()));
		//	Collections.binarySearch(forma.getListTarjetasCliente(), forma.getListTarjetasCliente().get(0), SortTarjetaCliente(forma.getPatronOrdenar() ));
		return mapping.findForward("paginaPrincipal");
	}
	
	
	



	
}
