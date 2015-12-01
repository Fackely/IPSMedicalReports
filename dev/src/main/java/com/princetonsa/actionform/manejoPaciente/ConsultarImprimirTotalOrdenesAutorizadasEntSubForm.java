package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoTotalesOrdenesEntidadesSub;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Convenios;

public class ConsultarImprimirTotalOrdenesAutorizadasEntSubForm extends ActionForm{

	
private static final long serialVersionUID = 1L;
	
	/** Atributo usado para definir la acción que se está realizando */
	private String estado;
	
	/** Atributo usado para indicar cuando no se tienen autorizaciones de capitación */
	private boolean sinOrdenes;
	
	/** Atributo que contiene las autorizaciones de capitación existentes */
	private ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones;
	
	/** Atributo que almacena el código de la autorización seleccionada para ver su detalle */
	private long codigoAutorizacion;
	
	/** Atributo usado para la paginación del listado de autorizaciones */
	private int posArray;
	
	/** Atributo que alamcena el nombre de la columna por la cual deben ser ordenados los registros encontrados */
	private String patronOrdenar;
	
	/** Atributo usado para ordenar descendentemente */
	private String esDescendente;
			
	/** Objeto que contiene los filtros de la consulta */
	private DtoBusquedaTotalOrdenesAutorizadasEntSub dtoFiltros;
	
	/** Lista de Estados*/
	private int ayudanteListaEstados;
	
	/**LISTA TMP CODIGO A ELIMINAR*/
	private ArrayList<String> listaEstadosEliminar;
	
	/** Atributo que contiene las entidades subcontratadas almacenadas en el sistema*/
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSub;
	
	/** Atributo que contiene los convenios de las autorizaciones 
	 *  de las entidades subcontratadas almacenadas en el sistema*/
	private ArrayList<Convenios> listaConvenios;
	
	/** Lista los estados de las autorizaciones */
	//private ArrayList<DtoAutorizacionEntSubContratada> listaEstadosAutorizaciones;

	/** Atributo de busqueda por rango de las auotrizaciones */
	//private String estadoAutorizacion;
	
	/** lista para los estados de las autorizaciones en la seleccion del jsp */
	private ArrayList<DtoCheckBox> listaOpcionesEstadosAutoriz;
	
	/** lista para los tipos de consulta de las autorizaciones en la seleccion del jsp */
	private ArrayList<DtoCheckBox> listaOpcionesTiposConsulta;
	
	/** Lista final de las Ordenes Autorizadas  */
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaFinalTotalOrdenesAutorizadas;
	
	/**HashMap que almacena los totales por Nivel de Atencion*/
	//private HashMap<String , DtoTotalesOrdenesEntidadesSub>sumNiveles;
	
	/** Lista final de las Ordenes Autorizadas  */
	private DtoConsultaTotalOrdenesAutorizadasEntSub dtoFinalTotalOrdenesAutorizadas;
	
	/**
	 * Atributo que almacena el tipo de salida del reporte
	 */
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	private String nombreArchivo;
	
	/** 
	 * Este método se encarga de inicializar los valores de la 
	 * página de consultar Imprimir Total Ordenes de las autorizaciones 
	 * 
	 * @author,Camilo Gómez
	 */
	public void reset(){	
		this.estado="";
		this.sinOrdenes=false;
		this.listaAutorizaciones=new ArrayList<DTOAdministracionAutorizacion>();
		this.codigoAutorizacion=ConstantesBD.codigoNuncaValidoLong;		
		this.dtoFiltros=new DtoBusquedaTotalOrdenesAutorizadasEntSub();
		this.ayudanteListaEstados=0;
		this.listaEstadosEliminar=new ArrayList<String>();
		this.listaEntidadesSub = new ArrayList<DtoEntidadSubcontratada>();
		//this.estadoAutorizacion="";		
		this.listaConvenios=new ArrayList<Convenios>();
		this.listaFinalTotalOrdenesAutorizadas=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		//this.sumNiveles=new HashMap<String, DtoTotalesOrdenesEntidadesSub>();
		this.dtoFinalTotalOrdenesAutorizadas=new DtoConsultaTotalOrdenesAutorizadasEntSub();
		this.tipoSalida="";
		this.enumTipoSalida=null;
		this.nombreArchivo="";
	}



	/**
	 * 
	 * Metodo que se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author Camilo Gómez
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();		
		MessageResources mensajes=MessageResources.getMessageResources(
		"com.servinte.mensajes.manejoPaciente.ConsultarImprimirTotalOrdenesAutorizadasEntSubForm");
		
		if(estado.equals("consultar"))
		{
			if(dtoFiltros.getFechaInicioBusqueda()!=null)
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),
							UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaInicioBusqueda())))
				{
					errores.add("fecha inicial mayor que la fecha actual", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.errorFechaInicialInvalida")));
				}
			}else{
				errores.add("fecha inicial es requerida", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.campoRequerido","Fecha Inicial")));
			}
			
			if(dtoFiltros.getFechaFinBusqueda()!=null)
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),
						UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinBusqueda())))
				{
					errores.add("fecha final mayor que la fecha actual", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.errorFechaFinalInvalida")));
				}
				if(!UtilidadTexto.isEmpty(dtoFiltros.getFechaInicioBusqueda().toString()))
				{					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinBusqueda()),
							UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaInicioBusqueda())))
					{
						errores.add("fecha final mayor que la fecha actual", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.errorFechaInicialMenorInicial")));
					}
				}
			}else{
				errores.add("fecha final es requerida", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.campoRequerido","Fecha Final")));
			}
			
			if(dtoFiltros.getTipoConsulta().equals(ConstantesBD.codigoNuncaValidoLong+""))
			{
				errores.add("tipo consulta es requerida", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.campoRequerido","Tipo de Consulta")));
			}else{
				if(dtoFiltros.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario) ||
						dtoFiltros.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado))
				{
					if(dtoFiltros.getCodigoEntidadSub().equals(ConstantesBD.codigoNuncaValidoLong))
					{
						errores.add("entidad subcontratada es requerida", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.campoRequeridoPorTipoConsulta","Entidad Subcontratada")));
					}
				}
			}

			
		}
		
		if(!errores.isEmpty())
		{
			setTipoSalida(null);
			setEnumTipoSalida(null);
			setNombreArchivo("");
			
		}
		return errores;		
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Camilo Gómez
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Camilo Gómez
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sinAutorizaciones
	
	 * @return retorna la variable sinAutorizaciones 
	 * @author Camilo Gómez
	 */
	public boolean isSinOrdenes() {
		return sinOrdenes;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sinAutorizaciones
	
	 * @param valor para el atributo sinAutorizaciones 
	 * @author Camilo Gómez
	 */
	public void setSinOrdenes(boolean sinOrdenes) {
		this.sinOrdenes = sinOrdenes;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAutorizaciones
	
	 * @return retorna la variable listaAutorizaciones 
	 * @author Camilo Gómez
	 */
	public ArrayList<DTOAdministracionAutorizacion> getListaAutorizaciones() {
		return listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAutorizaciones
	
	 * @param valor para el atributo listaAutorizaciones 
	 * @author Camilo Gómez
	 */
	public void setListaAutorizaciones(
			ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones) {
		this.listaAutorizaciones = listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAutorizacion
	
	 * @return retorna la variable codigoAutorizacion 
	 * @author Camilo Gómez
	 */
	public long getCodigoAutorizacion() {
		return codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAutorizacion
	
	 * @param valor para el atributo codigoAutorizacion 
	 * @author Camilo Gómez
	 */
	public void setCodigoAutorizacion(long codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Camilo Gómez
	 */
	public int getPosArray() {
		return this.posArray;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Camilo Gómez 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Camilo Gómez
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Camilo Gómez 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Camilo Gómez
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Camilo Gómez
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoFiltros
	
	 * @return retorna la variable dtoFiltros 
	 * @author Camilo Gómez 
	 */
	public DtoBusquedaTotalOrdenesAutorizadasEntSub getDtoFiltros() {
		return this.dtoFiltros;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoFiltros
	
	 * @param valor para el atributo dtoFiltros 
	 * @author Camilo Gómez 
	 */
	public void setDtoFiltros(DtoBusquedaTotalOrdenesAutorizadasEntSub dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEntidadesSub
	
	 * @return retorna la variable listaEntidadesSub 
	 * @author Camilo Gómez 
	 */
	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSub() {
		return this.listaEntidadesSub;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEntidadesSub
	
	 * @param valor para el atributo listaEntidadesSub 
	 * @author Camilo Gómez 
	 */
	public void setListaEntidadesSub(
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSub) {
		this.listaEntidadesSub = listaEntidadesSub;
	}


	public void setListaOpcionesEstadosAutoriz(
			ArrayList<DtoCheckBox> listaOpcionesEstadosAutoriz) {
		this.listaOpcionesEstadosAutoriz = listaOpcionesEstadosAutoriz;
	}



	public ArrayList<DtoCheckBox> getListaOpcionesEstadosAutoriz() {
		return listaOpcionesEstadosAutoriz;
	}



	public void setListaOpcionesTiposConsulta(
			ArrayList<DtoCheckBox> listaOpcionesTiposConsulta) {
		this.listaOpcionesTiposConsulta = listaOpcionesTiposConsulta;
	}



	public ArrayList<DtoCheckBox> getListaOpcionesTiposConsulta() {
		return listaOpcionesTiposConsulta;
	}



	/*public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}


	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}*/



	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}



	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}



	public void setListaFinalTotalOrdenesAutorizadas(
			ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaFinalTotalOrdenesAutorizadas) {
		this.listaFinalTotalOrdenesAutorizadas = listaFinalTotalOrdenesAutorizadas;
	}



	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> getListaFinalTotalOrdenesAutorizadas() {
		return listaFinalTotalOrdenesAutorizadas;
	}



	public void setDtoFinalTotalOrdenesAutorizadas(
			DtoConsultaTotalOrdenesAutorizadasEntSub dtoFinalTotalOrdenesAutorizadas) {
		this.dtoFinalTotalOrdenesAutorizadas = dtoFinalTotalOrdenesAutorizadas;
	}



	public DtoConsultaTotalOrdenesAutorizadasEntSub getDtoFinalTotalOrdenesAutorizadas() {
		return dtoFinalTotalOrdenesAutorizadas;
	}



	public void setAyudanteListaEstados(int ayudanteListaEstados) {
		this.ayudanteListaEstados = ayudanteListaEstados;
	}



	public int getAyudanteListaEstados() {
		return ayudanteListaEstados;
	}



	public void setListaEstadosEliminar(ArrayList<String> listaEstadosEliminar) {
		this.listaEstadosEliminar = listaEstadosEliminar;
	}



	public ArrayList<String> getListaEstadosEliminar() {
		return listaEstadosEliminar;
	}



	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}



	public String getTipoSalida() {
		return tipoSalida;
	}



	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}



	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}



	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}



	public String getNombreArchivo() {
		return nombreArchivo;
	}



	/*public void setSumNiveles(HashMap<String , DtoTotalesOrdenesEntidadesSub> sumNiveles) {
		this.sumNiveles = sumNiveles;
	}



	public HashMap<String , DtoTotalesOrdenesEntidadesSub> getSumNiveles() {
		return sumNiveles;
	}*/

}
