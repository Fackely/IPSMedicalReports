package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author Angela Maria Aguirre
 * @since 29/12/2010
 */
public class AdministracionAutorizacionCapitacionRangoForm extends
		ActionForm {

	private static final long serialVersionUID = 1L;
	private static final String ERROR_NO_ESPECIFICO="errors.notEspecific";
	private static final String ERROR_CAMPO_REQUERIDO="errors.required";
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
	/**
	 * Atributo usado para indicar cuando no se tienen autorizaciones de capitación
	 */
	private boolean sinAutorizaciones;
	
	
	/**
	 * Atributo que contiene las autorizaciones de capitación existentes
	 */
	private List<DTOAdministracionAutorizacion> listaAutorizaciones;
	
	/**
	 * Atributo que almacena el código de la autorización seleccionada para ver su detalle
	 */
	private long codigoAutorizacion;
	
	
	/**
	 * atributo usado para la paginación del listado de autorizaciones.
	 */
	private int posArray;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;
			
	/**
	 *Objeto que contiene los filtros de la consulta 
	 */
	private DTOBusquedaAutorizacionCapitacionRango dtoFiltros;
	
	
	private List<DtoEntidadSubcontratada> listaEntidadesSub;
	
	/**
	 * Almacena el consecutivo digitado inicial
	 */
	private Long consInAutorizacion;
	
	/**
	 * Almacena el consecutivo digitado final
	 */
	private Long consFinAutorizacion;

	/**
	 * 
	 * lista para los tipos de consecutivos de Autorizacion
	 */
	private List<DtoCheckBox> listaTiposConsecutivoAutorizacion;
	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de administración de las autorizaciones de capitación por rango
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		this.estado="";
		this.sinAutorizaciones=true;
		this.listaAutorizaciones=new ArrayList<DTOAdministracionAutorizacion>();
		this.codigoAutorizacion=ConstantesBD.codigoNuncaValidoLong;		
		this.dtoFiltros=new DTOBusquedaAutorizacionCapitacionRango();
		this.listaEntidadesSub = new ArrayList<DtoEntidadSubcontratada>();
		this.consInAutorizacion=ConstantesBD.codigoNuncaValidoLong;
		this.consFinAutorizacion=ConstantesBD.codigoNuncaValidoLong;
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();		
		MessageResources mensajes=MessageResources.getMessageResources(
		"com.servinte.mensajes.manejoPaciente.AdministracionAutorizacionCapitacionRangoForm");
		
		if(estado.equals("consultar")){
			
			boolean filtrosValidos=false;
			
			if(dtoFiltros.getFechaIncioBusqueda()!=null){
				filtrosValidos=true;
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.getFechaActual(),
						UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaIncioBusqueda()))){
					errores.add("fecha inicial mayor que la fecha actual", new ActionMessage(ERROR_NO_ESPECIFICO, 
							mensajes.getMessage("administracionAutorizacionCapitacionRangoForm.errorFechaInicialInvalida")));
				}
			}
			
			if(dtoFiltros.getFechaFinBusqueda()!=null&&dtoFiltros.getFechaIncioBusqueda()!=null){
				filtrosValidos=true;
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.getFechaActual(),
						UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinBusqueda()))){
					errores.add("fecha final mayor que la fecha actual", new ActionMessage(ERROR_NO_ESPECIFICO, 
							mensajes.getMessage("administracionAutorizacionCapitacionRangoForm.errorFechaFinalInvalida")));
				}else
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinBusqueda()),
							UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaIncioBusqueda()))){
						errores.add("fecha final mayor que la fecha actual", new ActionMessage(ERROR_NO_ESPECIFICO, 
								mensajes.getMessage("administracionAutorizacionCapitacionRangoForm.errorFechaInicialMenorInicial")));
					}
			}else
				if(dtoFiltros.getFechaFinBusqueda()==null&&dtoFiltros.getFechaIncioBusqueda()!=null)
				{
					filtrosValidos=true;
					errores.add("fecha final requerida", new ActionMessage(ERROR_CAMPO_REQUERIDO, "La Fecha Final Autorización "));
					
				}
				else
					if(dtoFiltros.getFechaFinBusqueda()!=null&&dtoFiltros.getFechaIncioBusqueda()==null)
					{
						filtrosValidos=true;
						errores.add("fecha inicial requerida", new ActionMessage(ERROR_CAMPO_REQUERIDO, "La Fecha Inicial Autorización "));
					}
			
			if( this.consFinAutorizacion!=null && this.getConsInAutorizacion()!=null
					&& this.consFinAutorizacion.longValue()!=ConstantesBD.codigoNuncaValido && this.getConsInAutorizacion().longValue()!=ConstantesBD.codigoNuncaValido){
				filtrosValidos=true;
				if(dtoFiltros.getConsecutivoFinAutorizacion()<dtoFiltros.getConsecutivoIncioAutorizacion()){
					errores.add("consecutivo final menor que inicial", new ActionMessage(ERROR_NO_ESPECIFICO, 
							mensajes.getMessage("administracionAutorizacionCapitacionRangoForm.errorConsecutivoFinalMenorInicial")));
				}
			}else
				if(( this.consFinAutorizacion==null&&this.getConsInAutorizacion()!=null)
					|| ( this.consFinAutorizacion.longValue()==ConstantesBD.codigoNuncaValido && this.getConsInAutorizacion().longValue()!=ConstantesBD.codigoNuncaValido))
				{
					filtrosValidos=true;
					errores.add("autorizacion final requerida", new ActionMessage(ERROR_CAMPO_REQUERIDO, "La Autorización Final "));
					this.getDtoFiltros().setConsecutivoFinAutorizacion(null);
				}else
					if((this.consFinAutorizacion!=null&&this.getConsInAutorizacion()==null)
						|| (this.consFinAutorizacion.longValue()!=ConstantesBD.codigoNuncaValido && this.getConsInAutorizacion().longValue()==ConstantesBD.codigoNuncaValido))
					{
						filtrosValidos=true;
						errores.add("autorizacion inicial requerida", new ActionMessage(ERROR_CAMPO_REQUERIDO, "La Autorización Inicial "));
						this.getDtoFiltros().setConsecutivoIncioAutorizacion(null);
					}
			
			if(!filtrosValidos && dtoFiltros.getCodigoEntidadSub().longValue()==ConstantesBD.codigoNuncaValidoLong){
				dtoFiltros.setConsecutivoFinAutorizacion(null);
				dtoFiltros.setConsecutivoIncioAutorizacion(null);
				errores.add("Se debe seleccionar por lo menos un filtro", new ActionMessage(ERROR_NO_ESPECIFICO, 
						mensajes.getMessage("administracionAutorizacionCapitacionRangoForm.busquedaSinFiltros")));
			}
		}		
		return errores;		
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sinAutorizaciones
	
	 * @return retorna la variable sinAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isSinAutorizaciones() {
		return sinAutorizaciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sinAutorizaciones
	
	 * @param valor para el atributo sinAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setSinAutorizaciones(boolean sinAutorizaciones) {
		this.sinAutorizaciones = sinAutorizaciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAutorizaciones
	
	 * @return retorna la variable listaAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public List<DTOAdministracionAutorizacion> getListaAutorizaciones() {
		return listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAutorizaciones
	
	 * @param valor para el atributo listaAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAutorizaciones(
			List<DTOAdministracionAutorizacion> listaAutorizaciones) {
		this.listaAutorizaciones = listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAutorizacion
	
	 * @return retorna la variable codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoAutorizacion() {
		return codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAutorizacion
	
	 * @param valor para el atributo codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoAutorizacion(long codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Angela Maria Aguirre 
	 */
	public int getPosArray() {
		return this.posArray;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Angela Maria Aguirre 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoFiltros
	
	 * @return retorna la variable dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public DTOBusquedaAutorizacionCapitacionRango getDtoFiltros() {
		return this.dtoFiltros;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoFiltros
	
	 * @param valor para el atributo dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoFiltros(DTOBusquedaAutorizacionCapitacionRango dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEntidadesSub
	
	 * @return retorna la variable listaEntidadesSub 
	 * @author Angela Maria Aguirre 
	 */
	public List<DtoEntidadSubcontratada> getListaEntidadesSub() {
		return this.listaEntidadesSub;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEntidadesSub
	
	 * @param valor para el atributo listaEntidadesSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEntidadesSub(
			List<DtoEntidadSubcontratada> listaEntidadesSub) {
		this.listaEntidadesSub = listaEntidadesSub;
	}


	public void setConsFinAutorizacion(Long consFinAutorizacion) {
		this.consFinAutorizacion = consFinAutorizacion;
	}


	public Long getConsFinAutorizacion() {
		return consFinAutorizacion;
	}


	public void setConsInAutorizacion(Long consInAutorizacion) {
		this.consInAutorizacion = consInAutorizacion;
	}


	public Long getConsInAutorizacion() {
		return consInAutorizacion;
	}


	/**
	 * @return the listaTiposConsecutivoAutorizacion
	 */
	public List<DtoCheckBox> getListaTiposConsecutivoAutorizacion() {
		return listaTiposConsecutivoAutorizacion;
	}


	/**
	 * @param listaTiposConsecutivoAutorizacion the listaTiposConsecutivoAutorizacion to set
	 */
	public void setListaTiposConsecutivoAutorizacion(
			List<DtoCheckBox> listaTiposConsecutivoAutorizacion) {
		this.listaTiposConsecutivoAutorizacion = listaTiposConsecutivoAutorizacion;
	}	

}
