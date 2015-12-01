package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.ViasIngreso;

public class AutorizacionesIngresoEstanciaForm extends ValidatorForm 
{
	
	/*--------------------------*/
	/* Propias de la Forma
	/*--------------------------*/
	/** * Serial */
	private static final long serialVersionUID = 1L;
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	/** * Posicion de las autorizaciones */
	private int posArray;
	/** * Parametros para ordenar */
	private String patronOrdenar;
	/** * Parametros para ordenar */
	private String esDescendente;
	
	/*--------------------------*/
	/*Listas y opciones a mostrar
	/*--------------------------*/
	/** * Lista de convenios Capitados */
	private ArrayList<Convenios> listaConvenios;
	/** * Convenios Capitado seleccionado */
	private String selectListaConvenios;
	/** * Lista de Tipos Afiliado */
	private ArrayList<TiposAfiliado> listaTiposAfiliado;
	/** * Convenios Capitado seleccionado */
	private String selectListaTiposAfiliado;
	/** * Lista de Tipos Identificación */
	ArrayList<TiposIdentificacion> listaTiposIdentificacion;
	/** * Tipo Identificación Seleccionada */
	private String selectListaTiposIdentificacion;
	/** * Parametros de busqueda de la funcionalidad */
	private DtoUsuariosCapitados parametrosBusqueda;
	/** * Lista de Usuarios Capitados */
	private ArrayList<DtoUsuariosCapitados> listaUsuariosCapitados;
	/** * Usuario Capitado sdeleccionado de la lista */
	private DtoUsuariosCapitados usuarioCapitadoSeleccionado;
	/** * Lista de entidades subcontratadas para la autorización */
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas;
	/** * Lista de Vías de Ingreso del sistema */
	private ArrayList<ViasIngreso> listaViasIngreso;
	/** * Lista de convenios Activos (Entidad de recobro) */
	private ArrayList<Convenios> listaConvenioEntidadRecobro;
	/** * Atributo se usa para determinar cuando se genero un reporte de autorizacion **/
	private boolean mostrarImprimirAutorizacion;
	/** * lista que contiene los nombres de los reportes de las autorzaciones **/ 
	private ArrayList<String> listaNombresReportes;
	/** * Atributo que contiene el numero de la autorizacion cuando es generada **/
	private Long numeroAutorizacion;
	
	private String nombreArchivoGenerado;
	
	private boolean formatoImpresionDefinido;
	
	
	/*-----------------------------*/
	/* Flujos
	/*-----------------------------*/
	/** * Indica si se debe mostra para ingresar una nueva autorización */
	private boolean nuevaAutorizacion;
	/** * Indica si se puede extender N días la Autorización */
	private boolean extenderAutorizacion;
	/** * Indica la Autorización está vigente o no*/
	private boolean autorizacionVigente;
	/** * Indica si la opcion seleccionada fue posponer */
	private boolean entroPorPosponer;
	
	private String integracion="";
	
	
	
	
	/*-------------------------------------------------------*/
	/* RESETS
	/*-------------------------------------------------------*/
	
	/** * Reset de la forma  */
	public void reset()
	{
		this.patronOrdenar 					= "";
		this.esDescendente					= "";
		
		this.listaConvenios					= new ArrayList<Convenios>();
		this.selectListaConvenios			= "";
		
		this.listaTiposAfiliado				= new ArrayList<TiposAfiliado>();
		this.selectListaTiposAfiliado		= "";
		
		this.listaTiposIdentificacion		= new ArrayList<TiposIdentificacion>();
		this.selectListaTiposIdentificacion	= "";
		
		this.parametrosBusqueda				= new DtoUsuariosCapitados();
		this.listaUsuariosCapitados			= new ArrayList<DtoUsuariosCapitados>();
		this.usuarioCapitadoSeleccionado	= new DtoUsuariosCapitados();
		this.listaEntidadesSubcontratadas	= new ArrayList<DtoEntidadSubcontratada>();
		this.listaViasIngreso				= new ArrayList<ViasIngreso>();
		this.nuevaAutorizacion				= false;
		this.extenderAutorizacion			= false;
		this.autorizacionVigente			= false;
		this.entroPorPosponer				= false;
		this.listaConvenioEntidadRecobro	= new ArrayList<Convenios>();
		this.mostrarImprimirAutorizacion	= false;
		this.listaNombresReportes			= new ArrayList<String>();
		this.numeroAutorizacion				= ConstantesBD.codigoNuncaValidoLong;
		
		this.nombreArchivoGenerado			= "";
		this.formatoImpresionDefinido		= false;
	}

	
	/**
	 * Reset de los parametros de búsqueda
	 */
	public void resetParametrosBusqueda(){
		this.parametrosBusqueda				= new DtoUsuariosCapitados();
	}
	

	/*-------------------------------------------------------*/
	/* METODOS SETs Y GETs
	/*-------------------------------------------------------*/
	
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public int getPosArray() {
		return posArray;
	}


	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getEsDescendente() {
		return esDescendente;
	}


	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}


	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}



	public String getSelectListaConvenios() {
		return selectListaConvenios;
	}



	public void setSelectListaConvenios(String selectListaConvenios) {
		this.selectListaConvenios = selectListaConvenios;
	}



	public ArrayList<TiposAfiliado> getListaTiposAfiliado() {
		return listaTiposAfiliado;
	}



	public void setListaTiposAfiliado(ArrayList<TiposAfiliado> listaTiposAfiliado) {
		this.listaTiposAfiliado = listaTiposAfiliado;
	}



	public String getSelectListaTiposAfiliado() {
		return selectListaTiposAfiliado;
	}



	public void setSelectListaTiposAfiliado(String selectListaTiposAfiliado) {
		this.selectListaTiposAfiliado = selectListaTiposAfiliado;
	}



	public ArrayList<TiposIdentificacion> getListaTiposIdentificacion() {
		return listaTiposIdentificacion;
	}



	public void setListaTiposIdentificacion(
			ArrayList<TiposIdentificacion> listaTiposIdentificacion) {
		this.listaTiposIdentificacion = listaTiposIdentificacion;
	}



	public String getSelectListaTiposIdentificacion() {
		return selectListaTiposIdentificacion;
	}



	public void setSelectListaTiposIdentificacion(
			String selectListaTiposIdentificacion) {
		this.selectListaTiposIdentificacion = selectListaTiposIdentificacion;
	}



	public DtoUsuariosCapitados getParametrosBusqueda() {
		return parametrosBusqueda;
	}



	public void setParametrosBusqueda(DtoUsuariosCapitados parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}



	public ArrayList<DtoUsuariosCapitados> getListaUsuariosCapitados() {
		return listaUsuariosCapitados;
	}



	public void setListaUsuariosCapitados(
			ArrayList<DtoUsuariosCapitados> listaUsuariosCapitados) {
		this.listaUsuariosCapitados = listaUsuariosCapitados;
	}


	public boolean isNuevaAutorizacion() {
		return nuevaAutorizacion;
	}


	public void setNuevaAutorizacion(boolean nuevaAutorizacion) {
		this.nuevaAutorizacion = nuevaAutorizacion;
	}


	public boolean isExtenderAutorizacion() {
		return extenderAutorizacion;
	}


	public void setExtenderAutorizacion(boolean extenderAutorizacion) {
		this.extenderAutorizacion = extenderAutorizacion;
	}


	public DtoUsuariosCapitados getUsuarioCapitadoSeleccionado() {
		return usuarioCapitadoSeleccionado;
	}


	public void setUsuarioCapitadoSeleccionado(
			DtoUsuariosCapitados usuarioCapitadoSeleccionado) {
		this.usuarioCapitadoSeleccionado = usuarioCapitadoSeleccionado;
	}


	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSubcontratadas() {
		return listaEntidadesSubcontratadas;
	}


	public void setListaEntidadesSubcontratadas(
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas) {
		this.listaEntidadesSubcontratadas = listaEntidadesSubcontratadas;
	}


	public ArrayList<ViasIngreso> getListaViasIngreso() {
		return listaViasIngreso;
	}


	public void setListaViasIngreso(ArrayList<ViasIngreso> listaViasIngreso) {
		this.listaViasIngreso = listaViasIngreso;
	}


	public ArrayList<Convenios> getListaConvenioEntidadRecobro() {
		return listaConvenioEntidadRecobro;
	}


	public void setListaConvenioEntidadRecobro(
			ArrayList<Convenios> listaConvenioEntidadRecobro) {
		this.listaConvenioEntidadRecobro = listaConvenioEntidadRecobro;
	}


	public boolean isAutorizacionVigente() {
		return autorizacionVigente;
	}


	public void setAutorizacionVigente(boolean autorizacionVigente) {
		this.autorizacionVigente = autorizacionVigente;
	}


	public boolean isEntroPorPosponer() {
		return entroPorPosponer;
	}


	public void setEntroPorPosponer(boolean entroPorPosponer) {
		this.entroPorPosponer = entroPorPosponer;
	}


	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}


	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}


	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	public void setNumeroAutorizacion(Long numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}


	public Long getNumeroAutorizacion() {
		return numeroAutorizacion;
	}


	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}


	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}


	public void setFormatoImpresionDefinido(boolean formatoImpresionDefinido) {
		this.formatoImpresionDefinido = formatoImpresionDefinido;
	}


	public boolean isFormatoImpresionDefinido() {
		return formatoImpresionDefinido;
	}


	/**
	 * @return the integracion
	 */
	public String getIntegracion() {
		return integracion;
	}


	/**
	 * @param integracion the integracion to set
	 */
	public void setIntegracion(String integracion) {
		this.integracion = integracion;
	}

}
