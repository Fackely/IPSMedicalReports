/*
 * @(#)ExcepcionesServiciosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;


/**
 * Form que contiene todos los datos específicos para generar 
 * las excepciones de servicios
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Julio 21, 2004
 * @author wrios 
 */
public class ExcepcionesServiciosForm extends ValidatorForm
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ExcepcionesServiciosForm.class);
	
	/**
	 * Código del servicio
	 */
	private int codigoServicio;
	
	/**
	 * Descripción del servicio
	 */
	private String descripcionServicio;

	/**
	 * Código del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Nombre del convenio
	 */
	private String nombreConvenio;
	
	/**
	 * Código del contrato
	 */
	private int codigoContrato;
	
	/**
	 * Número del contrato
	 */
	private String numeroContrato;

	/**
	 * Código del servicio Nuevo
	 */
	private int codigoServicioNuevo;
	
	/**
	 * Descripción del servicio Nuevo
	 */
	private String descripcionServicioNuevo;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
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
	 * Campo donde se restringe por qué criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
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
	private String logInfoOriginal;
	
	private int codigoEspecialidad;

	private String codigoAxioma;
	
	private int codigoServicioTemp;

	/**
	 * fecha de inicio y fin del contrato
	 */
	private String fechasVigenciaContrato;
	
	/**
	 * indica si es pos o nopos
	 */
	private String tipoPos;
	
	/**
	 * estado Temporal
	 */
	private String estadoTemp;
	
	/**
	 * Aux para la busqueda avanzada del servicio
	 */
	private int esposAux;
	
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
						errores=super.validate(mapping,request);
						if(this.codigoServicio<=0)
						{	
								errores.add("Campo Servicio no seleccionado", new ActionMessage("errors.required","El campo Servicio"));
						}
						if(this.codigoConvenio<=0)
						{	
								errores.add("Campo Convenio no seleccionado", new ActionMessage("errors.required","El campo Convenio"));
						}
						if(this.codigoContrato<=0)
						{	
								errores.add("Campo Contrato no seleccionado", new ActionMessage("errors.required","El campo Contrato"));
						}
						
						if(!errores.isEmpty())
						{
							if(estado.equals("salir"))
								this.setEstado("ingresar");
						
							if(estado.equals("guardarModificacion"))
								this.setEstado("modificar");
						}		
		}
		else if(estado.equals("ingresar"))
		{
			errores=super.validate(mapping,request);
			if(this.codigoConvenio<=0)
			{	
					errores.add("Campo Convenio no seleccionado", new ActionMessage("errors.required","El campo Convenio"));
			}
			if(!errores.isEmpty())
			{
					this.setEstado("empezar");
			}
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

	public void reset()
	{
		this.codigoServicio=0;
		this.descripcionServicio="";
		this.codigoConvenio=0;
		this.nombreConvenio="";
		this.codigoContrato=0;
		this.numeroContrato="";
		
		this.codigoServicioNuevo=0;
		this.descripcionServicioNuevo="";
		
		this.codigoEspecialidad=-1;
		this.codigoAxioma="";
		this.fechasVigenciaContrato="";
		this.codigoServicioTemp=0;
		this.tipoPos="";
		this.esposAux=ConstantesBD.codigoNuncaValido;
		
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
	 * @return Returns the codigoContrato.
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}
	
	/**
	 * @param codigoContrato The codigoContrato to set.
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	/**
	 * @return Returns the codigoConvenio.
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}
	
	/**
	 * @param codigoConvenio The codigoConvenio to set.
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	
	/**
	 * @return Returns the codigoServicio.
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}
	
	/**
	 * @param codigoServicio The codigoServicio to set.
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	
	/**
	 * @return Returns the codigoServicioNuevo.
	 */
	public int getCodigoServicioNuevo() {
		return codigoServicioNuevo;
	}
	
	/**
	 * @param codigoServicioNuevo The codigoServicioNuevo to set.
	 */
	public void setCodigoServicioNuevo(int codigoServicioNuevo) {
		this.codigoServicioNuevo = codigoServicioNuevo;
	}
	
	/**
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}
	
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}
	
	/**
	 * @return Returns the descripcionServicioNuevo.
	 */
	public String getDescripcionServicioNuevo() {
		return descripcionServicioNuevo;
	}
	
	/**
	 * @param descripcionServicioNuevo The descripcionServicioNuevo to set.
	 */
	public void setDescripcionServicioNuevo(String descripcionServicioNuevo) {
		this.descripcionServicioNuevo = descripcionServicioNuevo;
	}
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}
	
	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}
	
	/**
	 * @return Returns the numeroContrato.
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}
	
	/**
	 * @param numeroContrato The numeroContrato to set.
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginal() {
		return logInfoOriginal;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginal(String string) {
		logInfoOriginal = string;
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
	 * @return Returns the codigoEspecialidad.
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}
	/**
	 * @param codigoEspecialidad The codigoEspecialidad to set.
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	/**
	 * @return Returns the codigoAxioma.
	 */
	public String getCodigoAxioma() {
		return codigoAxioma;
	}
	/**
	 * @param codigoAxioma The codigoAxioma to set.
	 */
	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}
	/**
	 * @return Returns the codigoServicioTemp.
	 */
	public int getCodigoServicioTemp() {
		return codigoServicioTemp;
	}
	/**
	 * @param codigoServicioTemp The codigoServicioTemp to set.
	 */
	public void setCodigoServicioTemp(int codigoServicioTemp) {
		this.codigoServicioTemp = codigoServicioTemp;
	}
	
	/**
	 * @return Returns the fechasVigenciaContrato.
	 */
	public String getFechasVigenciaContrato() {
		return fechasVigenciaContrato;
	}
	/**
	 * @param fechasVigenciaContrato The fechasVigenciaContrato to set.
	 */
	public void setFechasVigenciaContrato(String fechasVigenciaContrato) {
		this.fechasVigenciaContrato = fechasVigenciaContrato;
	}
    /**
     * @return Returns the tipoPos.
     */
    public String getTipoPos() {
        return tipoPos;
    }
    /**
     * @param tipoPos The tipoPos to set.
     */
    public void setTipoPos(String tipoPos) {
        this.tipoPos = tipoPos;
    }
    /**
     * @return Returns the estadoTemp.
     */
    public String getEstadoTemp() {
        return estadoTemp;
    }
    /**
     * @param estadoTemp The estadoTemp to set.
     */
    public void setEstadoTemp(String estadoTemp) {
        this.estadoTemp = estadoTemp;
    }
    /**
     * @return Returns the esposAux.
     */
    public int getEsposAux() {
        return esposAux;
    }
    /**
     * @param esposAux The esposAux to set.
     */
    public void setEsposAux(int esposAux) {
        this.esposAux = esposAux;
    }
}
