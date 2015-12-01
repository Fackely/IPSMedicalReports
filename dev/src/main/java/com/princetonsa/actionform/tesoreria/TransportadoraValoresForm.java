/*
 * Marzo 30, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;


/**
 * @author Cristhian Murillo , Edgar Carvajal
 *
 * Clase que almacena y carga la información a la vista utilizada para la funcionalidad
 */
public class TransportadoraValoresForm extends ValidatorForm 
{
	
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9083087278940449159L;
	
	
	/**
	 * Lista de Codigos
	 */
	private int ayudanteListaCodigos;


	/**
	 * Objeto para manejar los logs de esta clase
	 */
	transient private Logger logger = Logger.getLogger(TransportadoraValoresForm.class);
	
	
	private String[] listaCodigosCentro;
	
	/**
	 * LISTA TMP CODIGO A ELIMINAR
	 */
	private ArrayList<Integer> listaCodigosEliminar;
	
	
	/**
	 * ARRAYLISt DE CENTRO DE ATENCION, SIRVER COMO  AYUDANTE PARA CONSULTAR LOS CENTROS DE ATENCION DE UNA TRANSPORTADORA
	 */
	private ArrayList<CentroAtencion> listaCentroAtenAyudanteTransportadora;
	
	
	/**
	 * Variable para manejar la dirección del workflow 
	 */
	private String estado;
	
	/**
	 * DTO 
	 */
	private TransportadoraValores dto = new TransportadoraValores();
	
	/**
	 * STRING BUILDER ARMAR ARCHIVO PLANO  
	 */
	private StringBuilder archivoPlano;
	
	
	
	/**
	 * DTO CENTRO DE ATENCION
	 */
	private CentroAtencion dtoCentroAtencion;
	
	
	/**
	 * LISTA PARA CARGAR LOS CENTROS DE ATENCION
	 */
	private  ArrayList<CentroAtencion> listaCentro = new ArrayList<CentroAtencion>();
	
	/**
	 * ATRIBUTO PARA MANEJAR CONSULTASr
	 */
	
	private String esConsulta;
	
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<TransportadoraValores> listaDto;

	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar 
	 */
	private boolean mostrarFormularioIngreso;

	/**
	 * Parametros para ordenar 
	 */
	private String patronOrdenar;
	
	/**
	 * Parametros para ordenar 
	 */
	private String esDescendente;
	/**
	 * 
	 */
	private String estadoAnterior;
	
	/**
	 * Paginador 
	 */
	private int posArray;
	
	
	/**
	 * 	TMP PARA CARGAR EL NOMBRE TERCERO
	 */
	
	private String descripcionTercero;
	

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}



	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * @return the dto
	 */
	public TransportadoraValores getDto() {
		return dto;
	}


	/**
	 * @param dto the dto to set
	 */
	public void setDto(TransportadoraValores dto) {
		this.dto = dto;
	}


	/**
	 * @return the listaDto
	 */
	public ArrayList<TransportadoraValores> getListaDto() {
		return listaDto;
	}


	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<TransportadoraValores> listaDto) {
		this.listaDto = listaDto;
	}


	
	
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		
		this.dto 						= new TransportadoraValores();
		
		
		this.dto.setTerceros(new Terceros());
		this.dto.getTerceros().setCodigo(ConstantesBD.codigoNuncaValido); 
		this.listaDto 					= new ArrayList<TransportadoraValores>();
		this.patronOrdenar 				= "";
		this.esDescendente 				= "";
		this.mostrarFormularioIngreso	= false;
		
		this.descripcionTercero="";
		this.dtoCentroAtencion= new CentroAtencion();
		this.listaCentro= new ArrayList<CentroAtencion>();
		this.listaCodigosCentro= new String[]{};
		this.listaCodigosEliminar = new ArrayList<Integer>();
		this.archivoPlano= new StringBuilder();
		this.estadoAnterior="";
		this.esConsulta=ConstantesBD.acronimoNo;
		this.listaCentroAtenAyudanteTransportadora = new ArrayList<CentroAtencion>();
		this.ayudanteListaCodigos=0;
	}
	
	
	
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		
		if(estado.equals("guardar"))
		{
			this.validacioneCodigoPkTerceros(errores);
			this.buscarCodigoTransportadora(errores, usuario);
			this.validarCantidadCentroAtencion(errores);
		}
		
		else if(estado.equals("guardarmodificar"))
		{
			this.validacioneCodigoPkTerceros(errores);
			this.buscarCodigoTransportadoraModificar(errores, usuario);
			this.validarCantidadCentroAtencion(errores);
		}
		
		if(!errores.isEmpty())
		{
			if(this.estado.equals("guardar"))
			{
				this.setEstado("nuevo");
			}
					
			if(this.estado.equals("guardarmodificar"))
			{
				this.setEstado("modificar");
			}
		}
		
		return errores;
	}


	private void validarCantidadCentroAtencion(ActionErrors errores) {
		if( this.ayudanteListaCodigos==0)
		{
			listaCodigosCentro= new String[]{};
			errores.add("", new ActionMessage("errors.notEspecific",	" Debe Existe por lo menos un Centro de Atención"));
		}
	}

	
	/**
	 * VALIDACIONES BASICAS 
	 * @param errores
	 */

	private void validacioneCodigoPkTerceros(ActionErrors errores) {
	
		if(UtilidadTexto.isEmpty(dto.getCodigo()))
		{
			errores.add("error codigo", new ActionMessage("errors.required", "El código"));
			setMostrarFormularioIngreso(true);
		}
		
		if(dto.getTerceros().getCodigo()<=0)
		{
			errores.add("error razon social", new ActionMessage("errors.required", "La razón social"));
			setMostrarFormularioIngreso(true);
		}
	}
	
	
	
	
	
	/**
	 * BUSCAR CODIGO TRANSPORTADORA
	 * @param errores
	 * @param usuario
	 */
	private void buscarCodigoTransportadora(ActionErrors errores, UsuarioBasico usuario){
		
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		
		TransportadoraValores dto= new TransportadoraValores();
		if(!UtilidadTexto.isEmpty(this.getDto().getCodigo()))
		{
			
			dto.setCodigo(this.getDto().getCodigo().trim());
			dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
			
			if(transportadoraValoresServicio.consultar(dto, usuario.getCodigoInstitucionInt()).size()>0)
			{
				errores.add("", new ActionMessage("errors.notEspecific",	" El Codigo Ya Existe"));
			}
		}
	}
	
	

	/**
	 * 
	 * @param errores
	 * @param usuario
	 */
	private void buscarCodigoTransportadoraModificar(ActionErrors errores, UsuarioBasico usuario)
	{
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		
		
		TransportadoraValores dto= new TransportadoraValores();
		dto.setCodigoPk(this.getDto().getCodigoPk());
		
		if(!UtilidadTexto.isEmpty(this.getDto().getCodigo()))
		{
			dto.setCodigo(this.getDto().getCodigo().trim());
			dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
			if(transportadoraValoresServicio.consultar(dto, usuario.getCodigoInstitucionInt()).size()>0)
			{
				errores.add("", new ActionMessage("errors.notEspecific",	" El Codigo Ya Existe"));
			}
		}
	}
	
	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}
	
	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}


	public void setDescripcionTercero(String descripcionTercero) {
		this.descripcionTercero = descripcionTercero;
	}


	public String getDescripcionTercero() {
		return descripcionTercero;
	}


	public void setDtoCentroAtencion(CentroAtencion dtoCentroAtencion) {
		this.dtoCentroAtencion = dtoCentroAtencion;
	}


	public CentroAtencion getDtoCentroAtencion() {
		return dtoCentroAtencion;
	}


	public void setListaCentro(ArrayList<CentroAtencion> listaCentro) {
		this.listaCentro = listaCentro;
	}


	public ArrayList<CentroAtencion> getListaCentro() {
		return listaCentro;
	}


	public void setListaCodigosCentro(String[] listaCodigosCentro) {
		this.listaCodigosCentro = listaCodigosCentro;
	}


	public String[] getListaCodigosCentro() {
		return listaCodigosCentro;
	}


	public void setListaCodigosEliminar(ArrayList<Integer> listaCodigosEliminar) {
		this.listaCodigosEliminar = listaCodigosEliminar;
	}


	public ArrayList<Integer> getListaCodigosEliminar() {
		return listaCodigosEliminar;
	}


	public void setArchivoPlano(StringBuilder archivoPlano) {
		this.archivoPlano = archivoPlano;
	}


	public StringBuilder getArchivoPlano() {
		return archivoPlano;
	}


	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}


	public String getEstadoAnterior() {
		return estadoAnterior;
	}


	public void setEsConsulta(String esConsulta) {
		this.esConsulta = esConsulta;
	}


	public String getEsConsulta() {
		return esConsulta;
	}


	public void setListaCentroAtenAyudanteTransportadora(
			ArrayList<CentroAtencion> listaCentroAtenAyudanteTransportadora) {
		this.listaCentroAtenAyudanteTransportadora = listaCentroAtenAyudanteTransportadora;
	}


	public ArrayList<CentroAtencion> getListaCentroAtenAyudanteTransportadora() {
		return listaCentroAtenAyudanteTransportadora;
	}


	private ITransportadoraValoresServicio getTransportadoraValoresServicio(){
		
		return TesoreriaFabricaServicio.crearTransportadoraValoresServicio();
	}


	public void setAyudanteListaCodigos(int ayudanteListaCodigos) {
		this.ayudanteListaCodigos = ayudanteListaCodigos;
	}


	public int getAyudanteListaCodigos() {
		return ayudanteListaCodigos;
	}
	
}



