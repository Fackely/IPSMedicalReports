/*
 * Nov 09, 2006
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Instituciones SIRC
 */
public class InstitucionesSircForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto donde se almacenan las instituciones
	 */
	private HashMap instituciones = new HashMap();
	
	/**
	 * Número de registros del mapa instituciones
	 */
	private int numRegistros;
	/**
	 * Posicion del mapa que indica un registro seleccionado
	 */
	private int posicion;
	
	/**
	 * Objeto donde se almacenan los registros eliminados
	 */
	private HashMap eliminados = new HashMap();
	/**
	 * Número de registros del mapa eliminados
	 */
	private int numEliminados;
	
	//objetos de valores de seleccion
	private HashMap niveles = new HashMap();
	private int numNiveles;
	private HashMap tiposRed = new HashMap();
	private int numTiposRed;
	
	//atributos para la paginacion y ordenacion
	private int maxPageItems;
	private String linkSiguiente;
	private int offset;
	private String indice;
	private String ultimoIndice;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.instituciones = new HashMap();
		this.numRegistros = 0;
		this.posicion = 0;
		this.eliminados = new HashMap();
		this.numEliminados = 0;
		
		//atributos para la seleccion de valores
		this.niveles = new HashMap();
		this.numNiveles = 0;
		this.tiposRed = new HashMap();
		this.numTiposRed = 0;
		
		//attributos para paginacion y ordenacion
		this.linkSiguiente = "";
		this.offset = 0;
		this.maxPageItems = 0;
		this.indice = "";
		this.ultimoIndice = "";
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
		if(estado.equals("nuevo")||estado.equals("redireccion")||estado.equals("ordenar")||estado.equals("guardar")||estado.equals("eliminar"))
			this.verificarInstituciones();
		
		if(estado.equals("guardar"))
		{
			String aux = "";
			for(int i=0;i<this.numRegistros;i++)
			{
				//Verifica codigos
				aux = this.getInstituciones("codigo_"+i).toString();
				if(aux.equals(""))
					errores.add("codigo requerido",new ActionMessage("errors.required","El código del registro N° "+(i+1)));
				
				//Verifica descripciones
				aux = this.getInstituciones("descripcion_"+i).toString();
				if(aux.equals(""))
					errores.add("descripcion requerida",new ActionMessage("errors.required","La descripción del registro N° "+(i+1)));
				
				//Verifica nivel
				aux = this.getInstituciones("codigo_nivel_"+i).toString();
				if(aux.equals("0"))
					errores.add("nivel requerida",new ActionMessage("errors.required","El nivel del registro N° "+(i+1)));
				
				//Verifica red
				aux = this.getInstituciones("codigo_tipo_red_"+i).toString();
				if(aux.equals("0"))
					errores.add("tipo red requerida",new ActionMessage("errors.required","La red del registro N° "+(i+1)));
				
				//Verifica red
				if(this.getInstituciones("codigo_tipo_refe_"+i).toString().equals("N") && (this.getInstituciones("codigo_tipo_ambu_"+i).toString().equals("N")))				
					errores.add("Tipo Institucion requerida",new ActionMessage("errors.required","Se Requiere Minino un Tipo de Institucion Activa. El Tipo del registro N° "+(i+1)));				
				
			}
			
			//********VALIDAR REPETIDOS********************************
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			String aux1 = "";
			String aux2 = "";
			
			for(int i=0;i<this.numRegistros;i++)
			{
				aux1=this.getInstituciones("codigo_"+i).toString();
				
				for(int j=this.numRegistros-1;j>i;j--)
				{
					
					aux2=this.getInstituciones("codigo_"+j).toString();
					//se compara
					if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals("")
						&&!aux2.equals("")&&!codigosComparados.containsValue(aux1))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("códigos iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"códigos","en los registros Nº "+descripcion));
				}
				
				descripcion = "";
				codigosComparados.put(i+"",aux1);
				
			}
			
			if(!errores.isEmpty())
				this.estado = "empezar";
		}
		
		return errores;
	}
	
	/**
	 * Método que organiza el mapa de instituciones para las descripciones de los select nivel y tipo red queden cargadas
	 *
	 */
	public void verificarInstituciones()
	{
		String aux = "";
		String aux0 = "";
		for(int i=0;i<this.numRegistros;i++)
		{
			//Arreglar los niveles de servicio
			aux = this.getInstituciones("codigo_nivel_"+i).toString();
			if(aux!=null&&!aux.equals("0"))
			{
				for(int j=0;j<this.numNiveles;j++)
				{
					aux0 = this.getNiveles("consecutivo_"+j).toString();
					if(aux.equals(aux0))
						this.setInstituciones("nombre_nivel_"+i,this.getNiveles("descripcion_"+j));
				}
			}
			else
				this.setInstituciones("nombre_nivel_"+i,"");
			
			//Arreglar los tipos de red
			aux = this.getInstituciones("codigo_tipo_red_"+i).toString();
			if(aux!=null&&!aux.equals("0"))
			{
				for(int j=0;j<this.numTiposRed;j++)
				{
					aux0 = this.getTiposRed("consecutivo_"+j).toString();
					if(aux.equals(aux0))
						this.setInstituciones("nombre_tipo_red_"+i,this.getTiposRed("descripcion_"+j));
				}
			}
			else
				this.setInstituciones("nombre_tipo_red_"+i,"");
		}
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
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return Returns the instituciones.
	 */
	public HashMap getInstituciones() {
		return instituciones;
	}

	/**
	 * @param instituciones The instituciones to set.
	 */
	public void setInstituciones(HashMap instituciones) {
		this.instituciones = instituciones;
	}
	
	/**
	 * @return Retorna elemento del mapa instituciones.
	 */
	public Object getInstituciones(String key) {
		return instituciones.get(key);
	}

	/**
	 * @param Asigna elemento al mapa instituciones.
	 */
	public void setInstituciones(String key,Object obj) {
		this.instituciones.put(key,obj);
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Returns the niveles.
	 */
	public HashMap getNiveles() {
		return niveles;
	}

	/**
	 * @param niveles The niveles to set.
	 */
	public void setNiveles(HashMap niveles) {
		this.niveles = niveles;
	}
	
	/**
	 * @return Retorna elemento del mapa niveles.
	 */
	public Object getNiveles(String key) {
		return niveles.get(key);
	}

	/**
	 * @param Asigna elemento al mapa niveles.
	 */
	public void setNiveles(String key,Object obj) {
		this.niveles.put(key,obj);
	}

	/**
	 * @return Returns the numNiveles.
	 */
	public int getNumNiveles() {
		return numNiveles;
	}

	/**
	 * @param numNiveles The numNiveles to set.
	 */
	public void setNumNiveles(int numNiveles) {
		this.numNiveles = numNiveles;
	}

	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return Returns the numTiposRed.
	 */
	public int getNumTiposRed() {
		return numTiposRed;
	}

	/**
	 * @param numTiposRed The numTiposRed to set.
	 */
	public void setNumTiposRed(int numTiposRed) {
		this.numTiposRed = numTiposRed;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return Returns the posicion.
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion The posicion to set.
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return Returns the tiposRed.
	 */
	public HashMap getTiposRed() {
		return tiposRed;
	}

	/**
	 * @param tiposRed The tiposRed to set.
	 */
	public void setTiposRed(HashMap tiposRed) {
		this.tiposRed = tiposRed;
	}
	
	/**
	 * @return Retorna elemento del mapa tiposRed.
	 */
	public Object getTiposRed(String key) {
		return tiposRed.get(key);
	}

	/**
	 * @param Asigna elemento al mapa tiposRed.
	 */
	public void setTiposRed(String key,Object obj) {
		this.tiposRed.put(key,obj);
	}

	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return Returns the eliminados.
	 */
	public HashMap getEliminados() {
		return eliminados;
	}

	/**
	 * @param eliminados The eliminados to set.
	 */
	public void setEliminados(HashMap eliminados) {
		this.eliminados = eliminados;
	}
	
	/**
	 * @return Retorna elemento del mapa eliminados.
	 */
	public Object getEliminados(String key) {
		return eliminados.get(key);
	}

	/**
	 * @param Asigna elemento al mapa eliminados.
	 */
	public void setEliminados(String key,Object obj) {
		this.eliminados.put(key,obj);
	}

	/**
	 * @return Returns the numEliminados.
	 */
	public int getNumEliminados() {
		return numEliminados;
	}

	/**
	 * @param numEliminados The numEliminados to set.
	 */
	public void setNumEliminados(int numEliminados) {
		this.numEliminados = numEliminados;
	}
	
}
