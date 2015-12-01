package com.princetonsa.actionform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos necesarios de la cama. Y adicionalmente hace el manejo de reset de
 * la forma y de validación de errores de datos de entrada.
 * @version 1.0,Junio 9, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */

public class CamasForm extends ValidatorForm 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CamasForm.class);
	
	/**
	 * Para la búsqueda
	 * numero de cama a buscar
	 */
	private String numeroCama;
	
	/**
	 * Para la búsqueda
	 * rango inicial Fecha de traslado del paciente
	 */
	private String fechaTrasladoInicial;

	/**
	 * Para la búsqueda
	 * rango final Fecha de traslado del paciente
	 */
	private String fechaTrasladoFinal;
	
	/**
	 * Para la búsqueda
	 * rango inicial Hora del traslado del paciente
	 */
	private String horaTrasladoInicial;
	
	/**
	 * Para la búsqueda
	 * rango Final Hora del traslado del paciente
	 */
	private String horaTrasladoFinal;
	
	/**
	 * Para la búsqueda
	 * Usuario que hizo el traslado
	 */
	private String usuario;
	
	
	/**
	 * Colección con los datos del listado para búsqueda avanzada (pager)
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
	 * Centro de costo al que pertencen las camas listadas, 0 si es cualquiera
	 */
	private int centroCosto;
	
	/**
	 * Map con el listado de camas
	 */ 
	private final Map camas = new HashMap();
	
	private ArrayList camasAL = new ArrayList();
	
	/**
	 * Estado actual del flujo
	 */
	private String estado;
	
	/**
	 * Número de camas dentro del action
	 */
	private int numCamas;
	
	/**
	 * CENSO CAMAS!
	 */
	private int camasDisponibles;
	private int camasOcupadas;
	private int camasDesinfeccion;
	private int camasMantenimiento;
	private int camasFueraDeServicio;
	private String porcentajeCamasDisponibles;
	private String porcentajeCamasOcupadas;
	private String porcentajeCamasDesinfeccion;
	private String porcentajeCamasMantenimiento;
	private String porcentajeCamasFueraDeServicio;

	private String fechaActual;	
	
	/**
	 * codigo de la cuenta del paciente para 
	 * hacer la consulta de traslado de camas
	 */
	private int cuenta;
	
	/**
	 *Asigna la info de la admisión seleccionada
	 * en el link de consulta por paciente
	 */
	private String infoAdmision;
	
	/**
	 * Variable que indica por cual columna se va a ordenar
	 * el listado de censos camas
	 */
	private String criterio;
	
	/**
	 * Variable que define como se hace la ordenación 'ASC' o 'DESC'
	 */
	private String estadoOrdenacion;
	
	//********ATRIBUTO DE LA VALIDACION CENTRO ATENCION***************
	private int codigoCentroAtencion;
	private String nombreCentroAtencion;
	private String institucion;
	
	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();	
		if(estado.equals("resultadoBusquedaAvanzada"))
		{
		    boolean existioError=false;
			String bb[]= this.getCriteriosBusqueda();
			if(this.numeroCama.equals(""))
			{
				errores=super.validate(mapping,request);
				errores.add("Número de cama no seleccionado", new ActionMessage("errors.required","El campo Número de Cama"));
			}
			for(int i=0; i<bb.length; i++)
			{
			    existioError=false;
				if(bb[i].equals("rangoFechas"))
				{
				    if(!this.fechaTrasladoInicial.equals(""))
					{
						if(validacionFecha1(fechaTrasladoInicial))
						{
							errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));	
							existioError=true;
						}
					}
					else if(this.fechaTrasladoInicial.equals("")&&!this.fechaTrasladoFinal.equals(""))
					{
						errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","Si selecciona búsqueda por rangos de fecha entonces la Fecha Inicial "));
						existioError=true;
					}
					if(!existioError)
					{    
					    try
					    {
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),this.fechaTrasladoInicial))
							{
							    errores.add("errors.fechaPosteriorIgualActual",new ActionMessage("errors.fechaPosteriorIgualActual","Rango Traslado Inicial", "Actual"));
							}
					    }
					    catch(Exception e)
					    {
					    	errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));	
					    	existioError=true;
					    }
					}	
					if(!this.fechaTrasladoFinal.equals(""))
					{
						if(validacionFecha1(fechaTrasladoFinal))
						{
							errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", " Final"));
							existioError=true;
						}
					}
					else if(!this.fechaTrasladoInicial.equals("")&&this.fechaTrasladoFinal.equals(""))
					{
						errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","Si selecciona búsqueda por rangos de fecha entonces la Fecha Final "));
						existioError=true;
					}
					
					if(!existioError)
					{
					    try
					    {
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),this.fechaTrasladoFinal))
							{
							    errores.add("errors.fechaPosteriorIgualActual",new ActionMessage("errors.fechaPosteriorIgualActual","Rango Traslado Final", "Actual"));
							}
					    }	
						catch(Exception e)
						{
						   	errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", "Final"));	
						   existioError=true;
						}	
					}
					if(!existioError)
					{    
					    try
					    {
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaTrasladoFinal,this.fechaTrasladoInicial))
							{
							    errores.add("errors.fechaPosteriorIgualActual",new ActionMessage("errors.fechaPosteriorIgualActual","Rango Traslado Inicial", "Rango Traslado Final"));
							}
					    }
					    catch(Exception e)
					    {
					    	errores.add("Fecha inicial o Final", new ActionMessage("errors.formatoFechaInvalido", "Inicial-Final"));	
					    	existioError=true;
					    }
					}
					existioError=false;
				}
				if(bb[i].equals("rangoHoras"))
				{
					if(!UtilidadFecha.validacionHora(this.horaTrasladoInicial).puedoSeguir)
					{
						errores.add("Hora inicial", new ActionMessage("errors.formatoHoraInvalido", " Inicial"));
					}
					else if(this.horaTrasladoInicial.equals("")&&!this.horaTrasladoFinal.equals(""))
					{
						errores.add("Campo Hora Inicial vacio", new ActionMessage("errors.required","Si selecciona búsqueda por rangos de hora  entonces la Hora Inicial "));		
					}
					if(!UtilidadFecha.validacionHora(this.horaTrasladoFinal).puedoSeguir)
					{
						errores.add("Hora final", new ActionMessage("errors.formatoHoraInvalido", " Final"));
					}
					else if(!this.horaTrasladoInicial.equals("")&&this.horaTrasladoFinal.equals(""))
					{
						errores.add("Campo Hora Final vacio", new ActionMessage("errors.required","Si selecciona búsqueda por rangos de hora  entonces la Hora Final "));			
					}	
				}
			}		
			if(!errores.isEmpty())
			{
				if(estado.equals("resultadoBusquedaAvanzada"))
					this.setEstado("busquedaAvanzada");
			}		
		}
		else if(estado.equals("verCensoCamaCentroCosto"))
		{
			if(this.codigoCentroAtencion<=0)
				errores.add("falta centro atención",new ActionMessage("errors.seleccion","Centro de Atención"));
			else if(this.centroCosto==ConstantesBD.codigoCentroCostoNoSeleccionado)
				errores.add("falta centro costo",new ActionMessage("errors.seleccion","Centro de Costo"));
		}
		return errores;
	}

	private boolean validacionFecha1(String fecha)
	{
		boolean tieneErroresFecha= false;
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
		try
		{
			dateFormatter.parse(fecha);
		}
		catch (java.text.ParseException e)
		{
			tieneErroresFecha=true;
		}
		catch(Exception e)
		{
			tieneErroresFecha=true;
		}
		fecha="";
		return tieneErroresFecha;
	}


	
	/**
	 * Limpia el form
	 */
	public void reset()
	{
		camas.clear();
		camasAL.clear();
		estado = "";
		numCamas = 0;
		
		centroCosto = -1;
		camasDisponibles = 0;
		camasOcupadas = 0;
		camasDesinfeccion = 0;
		camasMantenimiento = 0;
		camasFueraDeServicio = 0;

		porcentajeCamasDisponibles = "";
		porcentajeCamasOcupadas = "";
		porcentajeCamasDesinfeccion = "";
		porcentajeCamasMantenimiento = "";
		porcentajeCamasFueraDeServicio = "";		
		
		fechaActual = "";
		
		this.criterio="";
		this.estadoOrdenacion="ASC";
		
		this.codigoCentroAtencion = 0;
		this.nombreCentroAtencion = "";
		this.institucion = "";
	}	
	
	/**
	 * resetea en vector de strings que
	 * contiene los criterios de búsqueda 
	 * de un traslado de camas
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
	 * Adiciona una cama al map
 	 */
	public void setCama(String key, Object value)
	{
		camas.put(key, value);
	}
	
	/**
	 * Retorna el valor de la cama dada su llave(codigo)
	 */
	public Object getCama(String key) 
	{
		return camas.get(key);
	}		
	/**
	 * Retorna el estado actual del flujo
	 * @return String
	 */
	public String getEstado() 
	{
		return estado;
	}

	/**
	 * Asigna el estado actual del flujo
	 * @param estado The estado to set
	 */
	public void setEstado(String estado) 
	{
		this.estado = estado;
	}

	/**
	 * Retorna el número de camas
	 * @return int
	 */
	public int getNumCamas()
	{
		return numCamas;
	}

	/**
	 *Asigna el número de camas
	 * @param numCamas The numCamas to set
	 */
	public void setNumCamas(int numCamas) 
	{
		this.numCamas = numCamas;
	}

	/**
	 * Returns the centroCosto.
	 * @return int
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * Sets the centroCosto.
	 * @param centroCosto The centroCosto to set
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	/**
	 * Returns the camasAL.
	 * @return ArrayList
	 */
	public ArrayList getCamasAL()
	{
		return camasAL;
	}

	/**
	 * Sets the camasAL.
	 * @param camasAL The camasAL to set
	 */
	public void setCamasAL(ArrayList camasAL)
	{
		this.camasAL = camasAL;
	}

	/**
	 * Returns the camasDesinfeccion.
	 * @return int
	 */
	public int getCamasDesinfeccion()
	{
		return camasDesinfeccion;
	}

	/**
	 * Returns the camasDisponibles.
	 * @return int
	 */
	public int getCamasDisponibles()
	{
		return camasDisponibles;
	}

	/**
	 * Returns the camasFueraDeServicio.
	 * @return int
	 */
	public int getCamasFueraDeServicio()
	{
		return camasFueraDeServicio;
	}

	/**
	 * Returns the camasMantenimiento.
	 * @return int
	 */
	public int getCamasMantenimiento()
	{
		return camasMantenimiento;
	}

	/**
	 * Returns the camasOcupadas.
	 * @return int
	 */
	public int getCamasOcupadas()
	{
		return camasOcupadas;
	}

	/**
	 * Sets the camasDesinfeccion.
	 * @param camasDesinfeccion The camasDesinfeccion to set
	 */
	public void setCamasDesinfeccion(int camasDesinfeccion)
	{
		this.camasDesinfeccion = camasDesinfeccion;
	}

	/**
	 * Sets the camasDisponibles.
	 * @param camasDisponibles The camasDisponibles to set
	 */
	public void setCamasDisponibles(int camasDisponibles)
	{
		this.camasDisponibles = camasDisponibles;
	}

	/**
	 * Sets the camasFueraDeServicio.
	 * @param camasFueraDeServicio The camasFueraDeServicio to set
	 */
	public void setCamasFueraDeServicio(int camasFueraDeServicio)
	{
		this.camasFueraDeServicio = camasFueraDeServicio;
	}

	/**
	 * Sets the camasMantenimiento.
	 * @param camasMantenimiento The camasMantenimiento to set
	 */
	public void setCamasMantenimiento(int camasMantenimiento)
	{
		this.camasMantenimiento = camasMantenimiento;
	}

	/**
	 * Sets the camasOcupadas.
	 * @param camasOcupadas The camasOcupadas to set
	 */
	public void setCamasOcupadas(int camasOcupadas)
	{
		this.camasOcupadas = camasOcupadas;
	}

	/**
	 * Returns the porcentajeCamasDesinfeccion.
	 * @return String
	 */
	public String getPorcentajeCamasDesinfeccion()
	{
		return porcentajeCamasDesinfeccion;
	}

	/**
	 * Returns the porcentajeCamasDisponibles.
	 * @return String
	 */
	public String getPorcentajeCamasDisponibles()
	{
		return porcentajeCamasDisponibles;
	}

	/**
	 * Returns the porcentajeCamasFueraDeServicio.
	 * @return String
	 */
	public String getPorcentajeCamasFueraDeServicio()
	{
		return porcentajeCamasFueraDeServicio;
	}

	/**
	 * Returns the porcentajeCamasMantenimiento.
	 * @return String
	 */
	public String getPorcentajeCamasMantenimiento()
	{
		return porcentajeCamasMantenimiento;
	}

	/**
	 * Returns the porcentajeCamasOcupadas.
	 * @return String
	 */
	public String getPorcentajeCamasOcupadas()
	{
		return porcentajeCamasOcupadas;
	}

	/**
	 * Sets the porcentajeCamasDesinfeccion.
	 * @param porcentajeCamasDesinfeccion The porcentajeCamasDesinfeccion to set
	 */
	public void setPorcentajeCamasDesinfeccion(String porcentajeCamasDesinfeccion)
	{
		this.porcentajeCamasDesinfeccion = porcentajeCamasDesinfeccion;
	}

	/**
	 * Sets the porcentajeCamasDisponibles.
	 * @param porcentajeCamasDisponibles The porcentajeCamasDisponibles to set
	 */
	public void setPorcentajeCamasDisponibles(String porcentajeCamasDisponibles)
	{
		this.porcentajeCamasDisponibles = porcentajeCamasDisponibles;
	}

	/**
	 * Sets the porcentajeCamasFueraDeServicio.
	 * @param porcentajeCamasFueraDeServicio The porcentajeCamasFueraDeServicio to set
	 */
	public void setPorcentajeCamasFueraDeServicio(String porcentajeCamasFueraDeServicio)
	{
		this.porcentajeCamasFueraDeServicio = porcentajeCamasFueraDeServicio;
	}

	/**
	 * Sets the porcentajeCamasMantenimiento.
	 * @param porcentajeCamasMantenimiento The porcentajeCamasMantenimiento to set
	 */
	public void setPorcentajeCamasMantenimiento(String porcentajeCamasMantenimiento)
	{
		this.porcentajeCamasMantenimiento = porcentajeCamasMantenimiento;
	}

	/**
	 * Sets the porcentajeCamasOcupadas.
	 * @param porcentajeCamasOcupadas The porcentajeCamasOcupadas to set
	 */
	public void setPorcentajeCamasOcupadas(String porcentajeCamasOcupadas)
	{
		this.porcentajeCamasOcupadas = porcentajeCamasOcupadas;
	}
	
	/**
	 * Returns the fechaActual.
	 * @return String
	 */
	public String getFechaActual()
	{
		return fechaActual;
	}

	/**
	 * Sets the fechaActual.
	 * @param fechaActual The fechaActual to set
	 */
	public void setFechaActual(String fechaActual)
	{
		this.fechaActual = fechaActual;
	}


	/**
	 * @return rango final Fecha de traslado del paciente
	 */
	public String getFechaTrasladoFinal()
	{
		return fechaTrasladoFinal;
	}

	/**
	 * @return rango inicial Hora de traslado del paciente
	 */
	public String getHoraTrasladoInicial()
	{
		return horaTrasladoInicial;
	}

	/**
	 * @return Número de Cama
	 */
	public String getNumeroCama()
	{
		return numeroCama;
	}

	/**
	 * @return Usuario que hizo el traslado
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * Asignar el rango final de la fecha de traslado del paciente
	 * @param string
	 */
	public void setFechaTrasladoFinal(String string)
	{
		fechaTrasladoFinal = string;
	}

	/**
	 * Asigna rango inicial la hora de traslado del paciente
	 * @param string
	 */
	public void setHoraTrasladoInicial(String string)
	{
		horaTrasladoInicial = string;
	}

	/**
	 * Asigna el número de Cama
	 * @param string
	 */
	public void setNumeroCama(String string)
	{
		numeroCama = string;
	}

	/**
	 * Asigna el usuario que hizo el traslado
	 * @param string
	 */
	public void setUsuario(String string)
	{
		usuario = string;
	}

	public void resetBusqueda()
	{
		numeroCama="";
		fechaTrasladoInicial="";
		fechaTrasladoFinal="";
		horaTrasladoInicial="";
		horaTrasladoFinal="";
		usuario="";
	}

	/**
	 * @return el rango de fecha inicial del traslado
	 */
	public String getFechaTrasladoInicial() {
		return fechaTrasladoInicial;
	}

	/**
	 * @return la hora final del rango de búsqueda
	 */
	public String getHoraTrasladoFinal() {
		return horaTrasladoFinal;
	}

	/**
	 * Asigna la fecha inicial en un rango de búsqueda
	 * @param string
	 */
	public void setFechaTrasladoInicial(String string) {
		fechaTrasladoInicial = string;
	}

	/**
	 * Asigna la hora final en un rango de búsqueda
	 * @param string
	 */
	public void setHoraTrasladoFinal(String string) {
		horaTrasladoFinal = string;
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
	 * Retorna codigo de la cuenta del paciente para 
	 * hacer la consulta de traslado de camas
	 * @return
	 */
	public int getCuenta() {
		return cuenta;
	}

	/**
	 * codigo de la cuenta del paciente para 
	 * hacer la consulta de traslado de camas
	 * @param i
	 */
	public void setCuenta(int i) {
		cuenta = i;
	}

	/**
	 * Retorna la info de la admisión seleccionada
	 * en el link de consulta por paciente
	 * @return
	 */
	public String getInfoAdmision() {
		return infoAdmision;
	}

	/**
	 * Asigna la info de la admisión seleccionada
	 * en el link de consulta por paciente
	 * @param string
	 */
	public void setInfoAdmision(String string) {
		infoAdmision = string;
	}

	/**
	 * @return Returns the criterio.
	 */
	public String getCriterio() {
		return criterio;
	}
	/**
	 * @param criterio The criterio to set.
	 */
	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	/**
	 * @return Returns the estadoOrdenacion.
	 */
	public String getEstadoOrdenacion() {
		return estadoOrdenacion;
	}
	/**
	 * @param estadoOrdenacion The estadoOrdenacion to set.
	 */
	public void setEstadoOrdenacion(String estadoOrdenacion) {
		this.estadoOrdenacion = estadoOrdenacion;
	}

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
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
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}
}
