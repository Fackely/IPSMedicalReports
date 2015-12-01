package com.servinte.axioma.action.consultaExterna;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ExpresionesRegulares;

import com.servinte.axioma.actionForm.consultaExterna.ParametrizacionValorEstandarOrdenadoresForm;
import com.servinte.axioma.bl.consultaExterna.facade.ConsultaExternaFacade;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author ginsotfu
 * @created 26/10/2012
 *
 */

public class ParametrizacionValorEstandarOrdenadoresAction extends DispatchAction{

	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	
	private static final String KEY_ERROR_REQUIRED="errors.required";
	
	private static final String KEY_ERROR_CAST="errors.castForm";
	
	private static final String PROPERTIES_PARAMETRIZACION_VALOR="com.servinte.mensajes.consultaExterna.ParametrizacionValorEstandarOrdenadoresForm";
	
	private static final String FILTRAR_TIPO_ORDEN="filtrarTipoOrden";
	
	private static final String ELIMINAR_ORDEN_POPUP="eliminarOrdenPopUp";
	/**
	 * Metodo encargado de realizar toda la logica web para iniciar el proceso de 
	 * parametrizacion valor estandar ordenadores 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward initParametricaValorEstandar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		try{
			ParametrizacionValorEstandarOrdenadoresForm forma=null;
			FacturacionFacade facturacionFacade=  new FacturacionFacade();
			InventarioFacade inventarioFacade= new InventarioFacade();
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
				forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
			}
			else{
				throw new ClassCastException();
			}
			forma.setMostrarSeccionCaptura(false);
			forma.setMensajeIngresoExitoso(false);
			forma.setMostrarSeccionModificar(false);
			forma.setTipoOrden(ConstantesBD.codigoNuncaValido);
			
			forma.setValorEstandarOrdenadoresDto(new ValorEstandarOrdenadoresDto());
			
			List<GrupoServicioDto> listaGruposServiciosDto= facturacionFacade.consultarGruposServicio();
			forma.setGruposServiciosDto (listaGruposServiciosDto);
			
			List<ClaseInventarioDto> listaClasesInventarioDto= inventarioFacade.consultarClaseInventario();
			forma.setClasesInventariosDto (listaClasesInventarioDto);
			
			List<DtoUnidadesConsulta> listaUnidadAgenda= consultaExternaFacade.consultarUnidadAgenda();
			forma.setUnidadAgendaDto(listaUnidadAgenda);
			
			forma.getListaOrdenadores().clear();
			
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
			
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
			
	}
	/**
	 * Metodo encargado de realizar la busqueda de tipo de orden de acuerdo al filtro de
	 * busqueda seleccionados por el usuario (Servicios, medicamentos)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward obtenerParametrica (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			ParametrizacionValorEstandarOrdenadoresForm forma=null;
			if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
				forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
			}
			else{
				throw new ClassCastException();
			}

			consultarParametrica(forma);
			
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
			return mapping.findForward(FILTRAR_TIPO_ORDEN);

	}
	/**
	 * Metodo encargado de realizar la
	 * busqueda seleccionados por el usuario (Servicios, medicamentos)
	 * 
	 * @param form
	 */
	
	private void consultarParametrica(ParametrizacionValorEstandarOrdenadoresForm forma) throws IPSException {
		ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
		forma.getListaOrdenadores().clear();
		forma.setMostrarSeccionCaptura(false);
		forma.setMensajeIngresoExitoso(false);
		forma.setMostrarSeccionModificar(false);
		List<ValorEstandarOrdenadoresDto> listaOrdenadores=consultaExternaFacade.obtenerParametrica(forma.getTipoOrden());
		forma.setListaOrdenadores(listaOrdenadores);
		
	}
	/**
	 * Metodo encargado de cargar seccion de una parametrica nueva
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward crearNuevoParametro (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionValorEstandarOrdenadoresForm forma=null;
		if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
			forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
		}
		else{
			throw new ClassCastException();
		}
		forma.setMostrarSeccionModificar(false);
		forma.setMostrarSeccionCaptura(true);
		forma.setMensajeIngresoExitoso(false);
		forma.getValorEstandarOrdenadoresDto().setCodigoClaseInventarios(null);
		forma.getValorEstandarOrdenadoresDto().setCodigoGruposServicio(null);
		forma.getValorEstandarOrdenadoresDto().setUnidadAgenda(0);
		forma.setValEstOrdCita("");
		forma.setValEstSermedCita("");
		forma.setValEstSermedOrden("");
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
		
	}
	/**
	 * Metodo encargado de cargar seccion de parametrica a modificar
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward crearModificarParametro (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionValorEstandarOrdenadoresForm forma=null;
		if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
			forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
		}
		else{
			throw new ClassCastException();
		}
		forma.setMostrarSeccionCaptura(false);
		ValorEstandarOrdenadoresDto valorEstandar=forma.getListaOrdenadores().get(Integer.parseInt(forma.getPosicionOrden()));
		forma.setValorEstandarOrdenadoresDto(valorEstandar);
		DecimalFormat format=new DecimalFormat("#.##");
		forma.getValorEstandarOrdenadoresDto().setCodigoGruposServicio(valorEstandar.getCodigoGrupoServicio());
		forma.getValorEstandarOrdenadoresDto().setCodigoClaseInventarios(valorEstandar.getCodigoClaseInventarios());
		forma.getValorEstandarOrdenadoresDto().setUnidadAgenda(valorEstandar.getUnidadAgenda());
		String valor1=format.format(valorEstandar.getValorEstOrdCita());
		forma.setValEstOrdCita(valor1.replace(",", "."));
		String valor2=format.format(valorEstandar.getValorEstSermedOrden());
		forma.setValEstSermedOrden(valor2.replace(",", "."));
		String valor3=format.format(valorEstandar.getValorEstSermedCita());
		forma.setValEstSermedCita(valor3.replace(",", "."));
		forma.setOrdenSeleccionada(valorEstandar);
		forma.setMensajeIngresoExitoso(false);
		forma.setMostrarSeccionModificar(true);
		
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
		
	}
	/**
	 * Metodo encargado de validar datos numericos cuando se ingresa o modifica parametrica,
	 * y valida que la parametrica a modificar o ingresar no se encuentre ya parametrizada
	 * 
	 * @param form
	 * @param errores
	 * @throws IPSException 
	 */
	
	public void validarDatos(ActionForm form, ActionMessages errores) throws IPSException{
		MessageResources mensajes=MessageResources.getMessageResources(PROPERTIES_PARAMETRIZACION_VALOR);
		ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
		ValorEstandarOrdenadoresDto valorEstandarDto = new ValorEstandarOrdenadoresDto();
		ParametrizacionValorEstandarOrdenadoresForm forma=null;
		if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
			forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
		}
		else{
			throw new ClassCastException();
		}
		valorEstandarDto=forma.getValorEstandarOrdenadoresDto();
		Integer codigoParametrica= valorEstandarDto.getCodigo()>0?valorEstandarDto.getCodigo():null;
		int codigoUnidadAgenda= valorEstandarDto.getUnidadAgenda();
		/**
		* Tipo Modificacion: Segun incidencia 6312
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 28/01/2013
		* Descripcion: Se verifica mediante un if si el valor del dropbox es -1
		* 			   es decir que aun esta en la opcion seleccione. En caso 
		* 			   de ser asi se lanza el error de GrupoServicio.  
		**/
		if (valorEstandarDto.getCodigoGrupoServicio() != null){
			int codigoGrupoServicio= valorEstandarDto.getCodigoGrupoServicio();
			if(codigoGrupoServicio!=-1){
				boolean resultado= consultaExternaFacade.consultarValidacionServicio(codigoParametrica, codigoGrupoServicio, codigoUnidadAgenda);
				if(resultado){
					errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroServicio"));
				}
			}
			else{
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.GrupoServicio"));
			}
			/**
			* Tipo Modificacion: Segun incidencia 6312
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 28/01/2013
			* Descripcion: Se verifica mediante un if si el valor del dropbox es -1
			* 			   es decir que aun esta en la opcion seleccione. En caso 
			* 			   de ser asi se lanza el error de UnidadAgenda.  
			**/
			if(codigoUnidadAgenda!=-1){
				if(forma.getTipoOrden()==0){
					codigoGrupoServicio= valorEstandarDto.getCodigoGrupoServicio();
					if(codigoGrupoServicio!=-1){
						boolean resultado= consultaExternaFacade.consultarValidacionServicio(codigoParametrica, codigoGrupoServicio, codigoUnidadAgenda);
						if(resultado){
							errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroServicio"));
						}
					}
					else{
						errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.GrupoServicio"));
					}
				}
				else {
					int codigoClaseInventario= valorEstandarDto.getCodigoClaseInventarios();
					boolean resultado1=consultaExternaFacade.consultarValidacionMedicamento(codigoParametrica, codigoClaseInventario, codigoUnidadAgenda);
					if(resultado1){
						errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroMedicamento"));
					}	
				}
			}
			else{
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.UnidadAgenda"));
			}
		}
		else{
			int codigoClaseInventarios= valorEstandarDto.getCodigoClaseInventarios();
			if(codigoClaseInventarios!=-1){
				boolean resultado= consultaExternaFacade.consultarValidacionServicio(codigoParametrica, codigoClaseInventarios, codigoUnidadAgenda);
				if(resultado){
					errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroServicio"));
				}
			}
			else{
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.ClaseInventarios"));
			}
			/**
			* Tipo Modificacion: Segun incidencia 6312
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 28/01/2013
			* Descripcion: Se verifica mediante un if si el valor del dropbox es -1
			* 			   es decir que aun esta en la opcion seleccione. En caso 
			* 			   de ser asi se lanza el error de UnidadAgenda.  
			**/
			if(codigoUnidadAgenda!=-1){
				if(forma.getTipoOrden()==0){
					codigoClaseInventarios= valorEstandarDto.getCodigoGrupoServicio();
					if(codigoClaseInventarios!=-1){
						boolean resultado= consultaExternaFacade.consultarValidacionServicio(codigoParametrica, codigoClaseInventarios, codigoUnidadAgenda);
						if(resultado){
							errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroServicio"));
						}
					}
					else{
						errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.ClaseInventarios"));
					}
				}
				else {
					int codigoClaseInventario= valorEstandarDto.getCodigoClaseInventarios();
					boolean resultado1=consultaExternaFacade.consultarValidacionMedicamento(codigoParametrica, codigoClaseInventario, codigoUnidadAgenda);
					if(resultado1){
						errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.registroMedicamento"));
					}	
				}
			}
			else{
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.UnidadAgenda"));
			}
		}
			
		
		
		
		
		Pattern pattern=null;
		Matcher matcher=null;
		
		if(forma.getValEstOrdCita()==null || forma.getValEstOrdCita().trim().isEmpty()){
			errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, mensajes.getMessage("ParametrizacionValorEstandarOrdenadores.valorEstOrdCita")));
		}else{
			pattern=Pattern.compile(ExpresionesRegulares.permitirNumerosPunto);
			matcher=pattern.matcher(forma.getValEstOrdCita());
			if(!matcher.matches()){
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.soloNumeros1"));
			}else{
				BigDecimal bigDecimal=new BigDecimal(forma.getValEstOrdCita());
				forma.getValorEstandarOrdenadoresDto().setValorEstOrdCita(bigDecimal);
			}
		}
		
		if(forma.getValEstSermedOrden()==null || forma.getValEstSermedOrden().trim().isEmpty() ){
			errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, mensajes.getMessage("ParametrizacionValorEstandarOrdenadores.valorEstSermedOrden")));
		}else{
			pattern=Pattern.compile(ExpresionesRegulares.permitirNumerosPunto);
			matcher=pattern.matcher(forma.getValEstSermedOrden());
			if(!matcher.matches()){
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.soloNumeros2"));
			}else{
				BigDecimal bigDecimal=new BigDecimal(forma.getValEstSermedOrden());
				forma.getValorEstandarOrdenadoresDto().setValorEstSermedOrden(bigDecimal);
			}
		}
		
		if(forma.getValEstSermedCita()==null || forma.getValEstSermedCita().trim().isEmpty() ){
			errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, mensajes.getMessage("ParametrizacionValorEstandarOrdenadores.valorEstSermedCita")));
		}else{
			pattern=Pattern.compile(ExpresionesRegulares.permitirNumerosPunto);
			matcher=pattern.matcher(forma.getValEstSermedCita());
			if(!matcher.matches()){
				errores.add("", new ActionMessage("errores.ParametrizacionValorEstandarOrdenadores.soloNumeros3"));
			}else{
				BigDecimal bigDecimal=new BigDecimal(forma.getValEstSermedCita());
				forma.getValorEstandarOrdenadoresDto().setValorEstSermedCita(bigDecimal);
			}
		}
		
		
	}
		
	/**
	 * Metodo encargado de guardar parametrica nueva
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward ingresarOrden(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();

		try{
			ParametrizacionValorEstandarOrdenadoresForm forma=null;
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
				forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			this.validarDatos(forma, errores);
			
			if(errores.isEmpty()){
				forma.getValorEstandarOrdenadoresDto().setTipoOrden(forma.getTipoOrden());
				consultaExternaFacade.ingresarOrden(forma.getValorEstandarOrdenadoresDto());
				consultarParametrica(forma);
				forma.setMensajeIngresoExitoso(true);
				forma.setMostrarSeccionCaptura(false);
			
			}
			else{
				forma.setMensajeIngresoExitoso(false);
				forma.setMostrarSeccionCaptura(true);
			}
			
		
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
	}
	
	
	/**
	 * Metodo encargado de guardar modificacion parametrica
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward modificarOrden(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		
		try{
			ParametrizacionValorEstandarOrdenadoresForm forma=null;
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
				forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
			}
			else{
				throw new ClassCastException();
			}
			this.validarDatos(forma, errores);
			
			if(errores.isEmpty()){
			forma.getValorEstandarOrdenadoresDto().setTipoOrden(forma.getTipoOrden());
			consultaExternaFacade.modificarOrden(forma.getValorEstandarOrdenadoresDto());
			consultarParametrica(forma);
				forma.setMensajeIngresoExitoso(true);
				forma.setMostrarSeccionModificar(false);
			}
			else{
				forma.setMensajeIngresoExitoso(false);
				forma.setMostrarSeccionModificar(true);
			}
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
	}
	/**
	 * Metodo encargado de cargar popup de eliminar orden 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward abrirPopUpEliminar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward(ELIMINAR_ORDEN_POPUP);
	}
	/**
	 * Metodo encargado de eliminar parametrica
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward eliminarOrden(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			ParametrizacionValorEstandarOrdenadoresForm forma=null;
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			if(form instanceof ParametrizacionValorEstandarOrdenadoresForm){
				forma = (ParametrizacionValorEstandarOrdenadoresForm) form;
			}
			else{
				throw new ClassCastException();
			}
			ValorEstandarOrdenadoresDto valorEstandar=forma.getListaOrdenadores().get(Integer.parseInt(forma.getPosicionEliminar()));
			forma.setValorEstandarOrdenadoresDto(valorEstandar);
			consultaExternaFacade.eliminarOrden(forma.getValorEstandarOrdenadoresDto());
			forma.setMostrarSeccionCaptura(false);
			forma.setMostrarSeccionModificar(false);
			consultarParametrica(forma);
			forma.setMensajeIngresoExitoso(true);			
			

		
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FILTRAR_TIPO_ORDEN);
	}
	
	
}
