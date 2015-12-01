/*
 * @(#)TerceroForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
 package com.princetonsa.actionform.cargos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de tercero.
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Junio 11, 2004
 * @author wrios 
 */
public class TerceroForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TerceroForm.class);
	
	/** 
	 * codigo del tercero
	 */
	private int codigo;
	
	/** 
	 * numero de identificacion del tercero	 
	 */
	private String numeroIdentificacion;
	
	/** 
	 * Descripcion del tercero 
	 */
	private String descripcion;
	
	/** 
	 * estado del tercero 
	 */
	private boolean activo;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;

	/**
	* Estado anterior al que que se encuentra en el proceso.       
	*/
	private String estadoAnt;

	public String getEstadoAnt() {
		return estadoAnt;
	}

	public void setEstadoAnt(String estadoAnt) {
		this.estadoAnt = estadoAnt;
	}

	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;	
	
	/**
	 * Campo donde se restringe por qué criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;

	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginalTercero;
	
	/**
	 * Boolean que permite modificar  o no un 
	 * número de identificación.
	 */
	private boolean hanUtilizadoTerceros;
	
	/**
	 * Institucion
	 */
	private String institucion;
	
	/**
	 * cadena para modificar el numero de id de un tercero
	 */
	private String numeroIdentificacionNuevo;
	
	/**
	 * MAPA PARA CONTENER TODOS LOS DATOS DE LA TABLA TIPO TERCERO
	 */
	
	private  ArrayList<HashMap<String, Object>> mapaTipoTercero = new ArrayList<HashMap<String, Object>>();
	
	/**
	 * 
	 */
	private int codigoTipoTercero;
	
	/**
	 * 
	 */
	private String digitoVerificacion;
	
	private DtoConceptosRetencionTercero dtoConceptoTercero;
	
	public ArrayList<DtoConceptosRetencionTercero> listaConceptosRetencionTercero;
	
	public ArrayList<DtoConceptosRetencion> listaConceptosRetencion;	
	
	private String descripcionTipoRet;
	
	private String consecutivoTipoRet;
	
	private String consecutivoConRet;
	
	private String tipoAplicacion;
	
	private boolean habilitaTipoApl;
	
	private String agenteRet;
	
	private String indiceDetalle;
	
	private String accion;
	
	private boolean modifico; 

	private String indiceTercero;
	
	private String tipoImpresion;
	 
	private String direccion;
	
	private String telefono;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);

	public void reset()
	{
		this.codigo=-1;
		this.numeroIdentificacion="";
		this.descripcion="";
		
		this.activo=true;
		mapaTipoTercero =  new ArrayList<HashMap<String, Object>>();
		this.activaAux=0;
		this.numeroIdentificacionNuevo="";
		this.hanUtilizadoTerceros=false;
		
		this.institucion = "";
		
		this.codigoTipoTercero=ConstantesBD.codigoNuncaValido;
		this.digitoVerificacion="";
		
		this.dtoConceptoTercero= new DtoConceptosRetencionTercero();
		this.listaConceptosRetencionTercero= new ArrayList<DtoConceptosRetencionTercero>();
		this.descripcionTipoRet="";
		this.consecutivoTipoRet="-1";
		this.listaConceptosRetencion=new ArrayList<DtoConceptosRetencion>();
		this.consecutivoConRet="-1";
		this.tipoAplicacion="-1";
		this.habilitaTipoApl=true;
		this.agenteRet="N";		
		this.indiceDetalle="";
		this.accion="";
		this.modifico=false;
		this.indiceTercero="";
		this.tipoImpresion="";
		this.direccion="";
		this.telefono="";
	}	

	public void resetConceptos()
	{
		this.setAccion("");
		this.setConsecutivoConRet("-1");
		this.setTipoAplicacion("-1");
		this.setAgenteRet("N");
		this.setDescripcionTipoRet("");
		this.setConsecutivoTipoRet("-1");
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
		if(estado.equals("salir")||estado.equals("guardarModificacion"))
		{
			if(numeroIdentificacion.equals("") || !Utilidades.validarEspacios(numeroIdentificacion))
			{
				errores.add("Campo Numero Identificacion vacio", new ActionMessage("errors.required","El campo Número Identificación"));				
			}
			if(descripcion.equals("") || !Utilidades.validarEspacios(descripcion))
			{
				errores.add("Campo Descripcion vacio", new ActionMessage("errors.required","El campo Descripción"));
			}
			if(codigoTipoTercero<=ConstantesBD.codigoNuncaValido)
			{
				errores.add("Campo Tipo Tercero Vacio", new ActionMessage("errors.required","El campo Tipo Tercero"));
			}
			if (descripcion!=null&&descripcion.length()>128)
			{
				descripcion=descripcion.substring(0,127);
				errores.add("tamanioOtroServExc", new ActionMessage("errors.maximoTamanioExcedido"," 128 ", " Descripcion"));
			}
			
			
			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					this.setEstado("empezar");
									
				if(estado.equals("guardarModificacion"))
					this.setEstado("modificar");
			}		
		}
		else if(estado.equals("inserta"))
		{
				mapping.findForward("principal");
		}
		else if(estado.equals("modificar"))
		{
				errores=super.validate(mapping,request);
		}
		else if(estado.equals("listar") || estado.equals("listarModificar"))
		{
				errores=super.validate(mapping,request);
		}	
		else if(estado.equals("busquedaAvanzada"))
		{
				errores=super.validate(mapping,request);
		}	
		else if(estado.equals("resultadoBusquedaAvanzada"))
		{
				errores=super.validate(mapping,request);
		}		
		return errores;
	}	

	
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoImpresion() {
		return tipoImpresion;
	}

	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}

	public String getIndiceTercero() {
		return indiceTercero;
	}

	public void setIndiceTercero(String indiceTercero) {
		this.indiceTercero = indiceTercero;
	}

	public boolean isModifico() {
		return modifico;
	}

	public void setModifico(boolean modifico) {
		this.modifico = modifico;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getIndiceDetalle() {
		return indiceDetalle;
	}

	public void setIndiceDetalle(String indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getAgenteRet() {
		return agenteRet;
	}

	public void setAgenteRet(String agenteRet) {
		this.agenteRet = agenteRet;
	}

	public boolean isHabilitaTipoApl() {
		return habilitaTipoApl;
	}

	public void setHabilitaTipoApl(boolean habilitaTipoApl) {
		this.habilitaTipoApl = habilitaTipoApl;
	}

	public String getTipoAplicacion() {
		return tipoAplicacion;
	}

	public void setTipoAplicacion(String tipoAplicacion) {
		this.tipoAplicacion = tipoAplicacion;
	}

	public String getConsecutivoConRet() {
		return consecutivoConRet;
	}

	public void setConsecutivoConRet(String consecutivoConRet) {
		this.consecutivoConRet = consecutivoConRet;
	}

	public ArrayList<DtoConceptosRetencion> getListaConceptosRetencion() {
		return listaConceptosRetencion;
	}

	public void setListaConceptosRetencion(
			ArrayList<DtoConceptosRetencion> listaConceptosRetencion) {
		this.listaConceptosRetencion = listaConceptosRetencion;
	}

	public String getConsecutivoTipoRet() {
		return consecutivoTipoRet;
	}

	public void setConsecutivoTipoRet(String consecutivoTipoRet) {
		this.consecutivoTipoRet = consecutivoTipoRet;
	}

	public String getDescripcionTipoRet() {
		return descripcionTipoRet;
	}

	public void setDescripcionTipoRet(String descripcionTipoRet) {
		this.descripcionTipoRet = descripcionTipoRet;
	}

	public ArrayList<DtoConceptosRetencionTercero> getListaConceptosRetencionTercero() {
		return listaConceptosRetencionTercero;
	}

	public void setListaConceptosRetencionTercero(
			ArrayList<DtoConceptosRetencionTercero> listaConceptosRetencionTercero) {
		this.listaConceptosRetencionTercero = listaConceptosRetencionTercero;
	}

	public DtoConceptosRetencionTercero getDtoConceptoTercero() {
		return dtoConceptoTercero;
	}

	public void setDtoConceptoTercero(
			DtoConceptosRetencionTercero dtoConceptoTercero) {
		this.dtoConceptoTercero = dtoConceptoTercero;
	}

	/**
	 * resetea en vector de strings que
	 * contiene los criterios de búsqueda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		try
		{
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			logger.warn(" error en el reset de busqueda "+e);
		}
	}

	/**
	 * Retorna el estado en que se encuentra el registro del tercero
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Asigna el estado en que se encuentra el registro del tercero
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * Returns the activo.
	 * @return boolean
	 */
	public boolean getActivo() {
		return activo;
	}

	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * Returns the descripcion.
	 * @return String
	 */
	public String getDescripcion() {
		return descripcion;
	}
 
	/**
	 * Returns the numeroIdentificacion.
	 * @return String
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	

	

	/**
	 * Sets the activo.
	 * @param activo The activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Sets the descripcion.
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna los criterios de búsqueda como strings
	 * @return
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Asigna los criterios de búsqueda como strings
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings) {
		criteriosBusqueda = strings;
	}

	/**
	 * Sets the numeroIdentificacion.
	 * @param numeroIdentificacion The numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	
	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}

	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginalTercero() {
		return logInfoOriginalTercero;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginalTercero(String string) {
		logInfoOriginalTercero = string;
	}
	
	
	
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}

	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * retorna el número de id MODIFICADO
	 * @return
	 */
	public String getNumeroIdentificacionNuevo() {
		return numeroIdentificacionNuevo;
	}

	/**
	 * asigna el número de id MODIFICADO
	 * @param string
	 */
	public void setNumeroIdentificacionNuevo(String string) {
		numeroIdentificacionNuevo = string;
	}

	/**
	 * Retorna Boolean que permite modificar  o no un 
	 * número de identificación.
	 * @return
	 */
	public boolean getHanUtilizadoTerceros() {
		return hanUtilizadoTerceros;
	}

	/**
	 * Asigna Boolean que permite modificar  o no un 
	 * número de identificación.
	 * @param b
	 */
	public void setHanUtilizadoTerceros(boolean b) {
		hanUtilizadoTerceros = b;
	}

	public ArrayList<HashMap<String, Object>> getMapaTipoTercero() {
		return mapaTipoTercero;
	}

	public void setMapaTipoTercero(
			ArrayList<HashMap<String, Object>> mapaTipoTercero) {
		this.mapaTipoTercero = mapaTipoTercero;
	}

	/**
	 * @return the codigoTipoTercero
	 */
	public int getCodigoTipoTercero() {
		return codigoTipoTercero;
	}

	/**
	 * @param codigoTipoTercero the codigoTipoTercero to set
	 */
	public void setCodigoTipoTercero(int codigoTipoTercero) {
		this.codigoTipoTercero = codigoTipoTercero;
	}

	/**
	 * @return the digitoVerificacion
	 */
	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	/**
	 * @param digitoVerificacion the digitoVerificacion to set
	 */
	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

}
