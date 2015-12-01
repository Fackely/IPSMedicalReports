package com.princetonsa.actionform;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos de los antecedentes medicamentos. Y adicionalmente hace el manejo
 * de reset de la forma y de validación de errores de datos de entrada.
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesMedicamentosForm extends ValidatorForm
{
	/**
	 * Estado actual dentro del flujo de la funcionalidad
	 */	
	private String estado;
	
	/**
	 * 
	 */
	private HashMap informacionArtPrin;
	
	/**
	 * 
	 */
	private HashMap formaFconcMap;
	
	/**
	 * Map para manejar los medicamentos
	 */
	private final Map medicamentos = new HashMap();	
	
	/**
	 * @return the medicamentos
	 */
	public Map getMedicamentos() {
		return medicamentos;
	}

	/**
	 * Número de medicamentos almacenados en la base de datos
	 */
	private int numMedicamentosBD;
	
	/**
	 * Número total de medicamentos
	 */
	private int numMedicamentos;
	
	/**
	 * Observaciones generales previamente ingresadas
	 */
	private String observacionesAnteriores;
	
	/**
	 * Observaciones generales nuevas
	 */
	private String observacionesNuevas;
	
	private String ocultarCabezotes;
	
	private String operacionExistosa;
	
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
		ActionErrors errores = new ActionErrors();
		
		if( estado.equals("adicionarMedicamento") || estado.equals("finalizar") )
		{
			int tam = this.numMedicamentos;
			
			if( estado.equals("adicionarMedicamento") )
				tam += 1;
			
			for(int i=1; i<=tam; i++)
			{
				String nomM = "nombre_"+i;
				if( this.getMedicamento(nomM) == null || this.getMedicamento(nomM).equals("") )
				{
					if( estado.equals("adicionarMedicamento") )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "medicamento"));
					else
					if( noVacio((String)this.getMedicamento("dosis_"+i)) || noVacio((String)this.getMedicamento("frecuencia_"+i)) || noVacio((String)this.getMedicamento("fechaInicio_"+i)) || noVacio((String)this.getMedicamento("fechaFin_"+i)) || noVacio((String)this.getMedicamento("observaciones_"+i)) )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "medicamento"));
				}
				
				String fecha = this.getMedicamento("fechaInicio_"+i)+"";
				if(!fecha.equals(""))
				{
					if(!UtilidadFecha.validarFecha(fecha))
						errores.add("Validar formato fecha inicio ", new ActionMessage("errors.formatoFechaInvalido", "inicio"));
					else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha, UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","de inicio","actual"));
						
					
				}
				
				
				String fechaFin = this.getMedicamento("fechaFin_"+i)+"";
				if(!fechaFin.equals(""))
				{
					if(!UtilidadFecha.validarFecha(fechaFin))
						errores.add("Validar formato fecha fin ", new ActionMessage("errors.formatoFechaInvalido", "final"));
					else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","de finalización","actual"));
				}
				
				//if(fecha.compareTo(fechaFin)>0)
				if(!fecha.equals("")&&!fechaFin.equals(""))
				{
					if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fecha))
						errores.add("errors.debeSerMenorIgualGenerico ", new ActionMessage("errors.debeSerMenorIgualGenerico","Fecha Inicial","Fecha Final"));
				}
			}
		}
		
		return errores;
	}
	
	public void reset()
	{
		this.medicamentos.clear();
		this.numMedicamentosBD = 0;
		this.numMedicamentos = 0;
		this.observacionesAnteriores = "";
		this.observacionesNuevas = "";
		this.informacionArtPrin = new HashMap();
		informacionArtPrin.put("numRegistros", 0);
		this.formaFconcMap = new HashMap();
		formaFconcMap.put("numRegistros", 0);
		this.operacionExistosa = ConstantesBD.acronimoNo;
	}
	
	/**
	 * Retorna el estado actual dentro del flujo de la funcionalidad
	 * @return 	String, estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual dentro del flujo de la funcionalidad
	 * @param 	String, estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * Retorna el  medicamento dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getMedicamento(String key)
	{
		return this.medicamentos.get(key);
	}
	
	/**
	 * Almacena un valor de  medicamento en el map, bajo la llave dada.
	 * @param Object, value
	 * @param String, key
	 */
	public void setMedicamento(String key, Object value)
	{
		this.medicamentos.put(key, value);		
	}	

	/**
	 * Retorna el número de medicamentos almacenados en la base de datos
	 * @return 	int, número de medicamentos en la bd
	 */
	public int getNumMedicamentosBD()
	{
		return numMedicamentosBD;
	}

	/**
	 * Asigna el número de medicamentos almacenados en la base de datos
	 * @param int, número de medicamentos en la bd
	 */
	public void setNumMedicamentosBD(int numMedicamentosBD)
	{
		this.numMedicamentosBD = numMedicamentosBD;
	}

	/**
	 * Retorna el número total de medicamentos
	 * @return 	int, número de medicamentos
	 */
	public int getNumMedicamentos()
	{
		return numMedicamentos;
	}

	/**
	 * Asigna el número total de medicamentos
	 * @param int, número de medicamentos
	 */
	public void setNumMedicamentos(int numMedicamentos)
	{
		this.numMedicamentos = numMedicamentos;
	}

	/**
	 * Retorna las observaciones generales previamente ingresadas
	 * @return	 String, observaciones
	 */
	public String getObservacionesAnteriores()
	{
		return observacionesAnteriores;
	}

	/**
	 * Asigna las observaciones generales previamente ingresadas
	 * @param 	String, observaciones anteriores
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}

	/**
	 * Retorna las observaciones generales nuevas
	 * @return 	String, observaciones nuevas
	 */
	public String getObservacionesNuevas()
	{
		return observacionesNuevas;
	}

	/**
	 * Asgina las observaciones generales nuevas
	 * @param 	String, observaciones nuevas
	 */
	public void setObservacionesNuevas(String observacionesNuevas)
	{
		this.observacionesNuevas = observacionesNuevas;
	}

	private boolean noVacio(String valor)
	{
		if( valor != null && !valor.equals("") )
			return true;
		else
			return false; 
	}
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}

	public HashMap getInformacionArtPrin() {
		return informacionArtPrin;
	}

	public void setInformacionArtPrin(HashMap informacionArtPrin) {
		this.informacionArtPrin = informacionArtPrin;
	}
	
	public Object getInformacionArtPrin(String key) {
		return informacionArtPrin.get(key);
	}


	public void setInformacionArtPrin(String key, Object value) {
		this.informacionArtPrin.put(key, value);
	}

	public HashMap getFormaFconcMap() {
		return formaFconcMap;
	}

	public void setFormaFconcMap(HashMap formaFconcMap) {
		this.formaFconcMap = formaFconcMap;
	}

	/**
	 * @return the operacionExistosa
	 */
	public String getOperacionExistosa() {
		return operacionExistosa;
	}

	/**
	 * @param operacionExistosa the operacionExistosa to set
	 */
	public void setOperacionExistosa(String operacionExistosa) {
		this.operacionExistosa = operacionExistosa;
	}
}
