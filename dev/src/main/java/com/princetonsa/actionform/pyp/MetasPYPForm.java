/*
 * Ago 09, 2006
 */
package com.princetonsa.actionform.pyp;

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
 *Parametrización de Metas PYP 
 */
public class MetasPYPForm extends ValidatorForm 
{

	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Año de la planeación de metas
	 */
	private String anio;
	
	/**
	 * Código del convenio seleccionado
	 */
	private String convenio;
	
	/**
	 * Código del programa seleccionado
	 */
	private String programa;
	
	/**
	 * Código de la institucion
	 */
	private String institucion = "";
	
	/**
	 * Objeto que almacena las actividades de un programa específico
	 */
	private HashMap actividades = new HashMap();
	
	/**
	 * Objeto que almacena los registros parametrizados de actividades por programa, convenio y año
	 */
	private HashMap listado = new HashMap();
	
	/**
	 * Número de registros del mapa listado
	 */
	private int numRegistros;
	
	/**
	 * Número máximo de filas por página
	 */
	private int maxPageItems;
	
	/**
	 * Posicion del mapa del registro que se desea eliminar
	 */
	private int posicion;
	
	//***ATRIBUTOS PARA LA ORDENACION Y PAGINACION**********************************************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private int offset;
	
	//**ATRIBUTOS DESTINADOS PARA EL POPUP DE CENTROS DE ATENCION X ACTIVIDAD***********************
	/**
	 * Objeto donde se almacenan los centros de atencion por actividad
	 */
	private HashMap centrosAtencion = new HashMap();
	/**
	 * Número de registros del mapa centrosAtencion
	 */
	private int numCentrosAtencion ;
	
	private String indiceCA;
	private String ultimoIndiceCA;
	private String linkSiguienteCA;
	private int offsetCA;
	private int posicionCA;
	private int totalActividadesCA;
	//**********************************************************************************************
	
	//**ATRIBUTOS DESTINADOS PARA EL POPUP DE OCUPACIONES MÉDICAS X CENTRO ATENCION******************
	/**
	 * Objeto donde se almacenan las ocupaciones por centro de atención
	 */
	private HashMap ocupaciones = new HashMap();
	/**
	 * Número de registros del mapa ocupaciones
	 */
	private int numOcupaciones;
	
	private String indiceOM;
	private String ultimoIndiceOM;
	private String linkSiguienteOM;
	private int offsetOM;
	private int posicionOM;
	private int totalActividadesOM;
	//*************************************************************************************************
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.anio = "";
		this.convenio = "";
		this.programa = "";
		this.institucion = "";
		this.actividades = new HashMap();
		this.listado = new HashMap();
		this.numRegistros = 0;
		this.maxPageItems = 0;
		
		this.posicion = 0;
		
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
		
		//atributos del popUp de centros atencion x actividad
		this.centrosAtencion = new HashMap();
		this.numCentrosAtencion = 0;
		
		//atributos del popUp de ocupaciones x centro de atencion
		this.ocupaciones = new HashMap();
		this.numOcupaciones = 0;
		
		this.resetCA();
		
		this.resetOM();
	}
	
	/**
	 * Reset de los datos de la forma relacionados con los centros de atencion por actividad
	 *
	 */
	public void resetCA()
	{
		this.indiceCA = "";
		this.ultimoIndiceCA = "";
		this.linkSiguienteCA = "";
		this.offsetCA = 0;
		this.posicionCA = 0;
		this.totalActividadesCA = 0;
	}
	
	
	/**
	 * Reset de los datos de la forma relacionados con las ocupaciones x centro de atención
	 *
	 */
	public void resetOM()
	{
		this.indiceOM = "";
		this.ultimoIndiceOM = "";
		this.linkSiguienteOM = "";
		this.offsetOM = 0;
		this.posicionOM = 0;
		this.totalActividadesOM = 0;
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
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("parametrizar"))
		{
			if(this.anio.equals(""))
				errores.add("Año requerido",new ActionMessage("errors.MayorQue","El año","0"));
			
			if(this.convenio.equals(""))
				errores.add("Convenio requerido",new ActionMessage("errors.required","El convenio"));
			
			if(this.programa.equals(""))
				errores.add("Programa requerido",new ActionMessage("errors.required","El programa"));
			else
			{
				try
				{
					int anioInt = Integer.parseInt(this.anio);
					if(anioInt<=0)
						errores.add("Año > 0", new ActionMessage("errors.MayorQue","El año","0"));
				}
				catch(Exception e)
				{
					errores.add("Debe ser entero", new ActionMessage("errors.integer","El año"));
				}
			}
			
			if(!errores.isEmpty())
				this.estado = "empezar";
		}
		else if(this.estado.equals("guardar"))
		{
			//Se acomoda el mapa para registros nuevos
			for(int i=0;i<this.numRegistros;i++)
			{
				//Se verifica el codigo programa actividad por convenio
				String aux =  this.getListado("codigo_papc_"+i).toString();
				if(!aux.equals(""))
				{
					for(int j=0;j<Integer.parseInt(this.getActividades("numRegistros").toString());j++)
					{
						String aux1 = this.getActividades("consecutivo_"+j).toString();
						if(aux.equals(aux1))
						{
							this.setListado("codigo_actividad_"+i,this.getActividades("codigo_actividad_"+j));
							this.setListado("descripcion_actividad_"+i,this.getActividades("descripcion_actividad_"+j));
						}
					}
					
				}
				else
					errores.add("Actividad requerido",new ActionMessage("errors.required","La actividad en el registro N° "+(i+1)));
				
				//se verifica el número de actividades
				aux = this.getListado("numero_actividades_"+i).toString();
				if(aux.equals(""))
					errores.add("Número de Actividad >= 0",new ActionMessage("errors.integerMayorIgualQue","El número de actividades en el registro N° "+(i+1),"0"));
				else
				{
					try
					{
						Integer.parseInt(aux);
					}
					catch(Exception e)
					{
						errores.add("Debe ser entero", new ActionMessage("errors.integer","El número de actividades en el registro N° "+(i+1)));
					}
				}
			}
			
			//Se verifica que no hayan registros repetidos
			if(errores.isEmpty())
			{
				// sección para validar que no hayan actividades repetidas
				String auxS1 = "";
				String auxS0 = "";
				HashMap codigosComparados = new HashMap();
				String descripcion = "";
				for(int i=0;i<this.numRegistros;i++)
				{
					auxS0=this.getListado("codigo_papc_"+i).toString().trim();
					
					for(int j=(this.getNumRegistros()-1);j>i;j--)
					{
						
						auxS1=this.getListado("codigo_papc_"+j).toString().trim();
						//se compara
						if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")
							&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0.toUpperCase()))
						{
							if(descripcion.equals(""))
								descripcion = (i+1) + "";
							descripcion += "," + (j+1);
							
						}
					}
					
					if(!descripcion.equals(""))
					{
						errores.add("Actividades iguales", 
								new ActionMessage("error.salasCirugia.igualesGeneral",
									"actividades","en los registros Nº "+descripcion));
					}
					
					descripcion = "";
					codigosComparados.put(i+"",auxS0.toUpperCase());
					
				}
			}
		}
		else if(this.estado.equals("nuevo")||this.estado.equals("ordenar")||this.estado.equals("redireccion"))
		{
			//Se acomoda el mapa para registros nuevos
			for(int i=0;i<this.numRegistros;i++)
			{
				String aux =  this.getListado("codigo_papc_"+i).toString();
				if(!aux.equals(""))
				{
					for(int j=0;j<Integer.parseInt(this.getActividades("numRegistros").toString());j++)
					{
						String aux1 = this.getActividades("consecutivo_"+j).toString();
						if(aux.equals(aux1))
						{
							this.setListado("codigo_actividad_"+i,this.getActividades("codigo_actividad_"+j));
							this.setListado("descripcion_actividad_"+i,this.getActividades("descripcion_actividad_"+j));
						}
					}
					
				}
			}
		}
		return errores;
	}

	/**
	 * @return Returns the actividades.
	 */
	public HashMap getActividades() {
		return actividades;
	}

	/**
	 * @param actividades The actividades to set.
	 */
	public void setActividades(HashMap actividades) {
		this.actividades = actividades;
	}
	
	/**
	 * @return Retorna un elemento del mapa actividades.
	 */
	public Object getActividades(String key) {
		return actividades.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa actividades.
	 */
	public void setActividades(String key, Object obj) {
		this.actividades.put(key,obj);
	}

	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
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
	 * @return Returns the programa.
	 */
	public String getPrograma() {
		return programa;
	}

	/**
	 * @param programa The programa to set.
	 */
	public void setPrograma(String programa) {
		this.programa = programa;
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
	 * @return Returns the anio.
	 */
	public String getAnio() {
		return anio;
	}

	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(String anio) {
		this.anio = anio;
	}

	/**
	 * @return Returns the listado.
	 */
	public HashMap getListado() {
		return listado;
	}

	/**
	 * @param listado The listado to set.
	 */
	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	/**
	 * @return Retorna un elemento del mapa listado.
	 */
	public Object getListado(String key) {
		return listado.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa listado.
	 */
	public void setListado(String key,Object obj) {
		this.listado.put(key,obj);
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
	 * @return Returns the centrosAtencion.
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion The centrosAtencion to set.
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return Retorna un elemento del mapa centrosAtencion.
	 */
	public Object getCentrosAtencion(String key) {
		return centrosAtencion.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa centrosAtencion.
	 */
	public void setCentrosAtencion(String key, Object obj) {
		this.centrosAtencion.put(key,obj);
	}
	
	/**
	 * @return Returns the numCentrosAtencion.
	 */
	public int getNumCentrosAtencion() {
		return numCentrosAtencion;
	}

	/**
	 * @param numCentrosAtencion The numCentrosAtencion to set.
	 */
	public void setNumCentrosAtencion(int numCentrosAtencion) {
		this.numCentrosAtencion = numCentrosAtencion;
	}

	/**
	 * @return Returns the indiceCA.
	 */
	public String getIndiceCA() {
		return indiceCA;
	}

	/**
	 * @param indiceCA The indiceCA to set.
	 */
	public void setIndiceCA(String indiceCA) {
		this.indiceCA = indiceCA;
	}

	/**
	 * @return Returns the linkSiguienteCA.
	 */
	public String getLinkSiguienteCA() {
		return linkSiguienteCA;
	}

	/**
	 * @param linkSiguienteCA The linkSiguienteCA to set.
	 */
	public void setLinkSiguienteCA(String linkSiguienteCA) {
		this.linkSiguienteCA = linkSiguienteCA;
	}

	/**
	 * @return Returns the offsetCA.
	 */
	public int getOffsetCA() {
		return offsetCA;
	}

	/**
	 * @param offsetCA The offsetCA to set.
	 */
	public void setOffsetCA(int offsetCA) {
		this.offsetCA = offsetCA;
	}

	/**
	 * @return Returns the ultimoIndiceCA.
	 */
	public String getUltimoIndiceCA() {
		return ultimoIndiceCA;
	}

	/**
	 * @param ultimoIndiceCA The ultimoIndiceCA to set.
	 */
	public void setUltimoIndiceCA(String ultimoIndiceCA) {
		this.ultimoIndiceCA = ultimoIndiceCA;
	}

	/**
	 * @return Returns the posicionCA.
	 */
	public int getPosicionCA() {
		return posicionCA;
	}

	/**
	 * @param posicionCA The posicionCA to set.
	 */
	public void setPosicionCA(int posicionCA) {
		this.posicionCA = posicionCA;
	}

	/**
	 * @return Returns the totalActividadesCA.
	 */
	public int getTotalActividadesCA() {
		return totalActividadesCA;
	}

	/**
	 * @param totalActividadesCA The totalActividadesCA to set.
	 */
	public void setTotalActividadesCA(int totalActividadesCA) {
		this.totalActividadesCA = totalActividadesCA;
	}

	/**
	 * @return Returns the indiceOM.
	 */
	public String getIndiceOM() {
		return indiceOM;
	}

	/**
	 * @param indiceOM The indiceOM to set.
	 */
	public void setIndiceOM(String indiceOM) {
		this.indiceOM = indiceOM;
	}

	/**
	 * @return Returns the linkSiguienteOM.
	 */
	public String getLinkSiguienteOM() {
		return linkSiguienteOM;
	}

	/**
	 * @param linkSiguienteOM The linkSiguienteOM to set.
	 */
	public void setLinkSiguienteOM(String linkSiguienteOM) {
		this.linkSiguienteOM = linkSiguienteOM;
	}

	/**
	 * @return Returns the numOcupaciones.
	 */
	public int getNumOcupaciones() {
		return numOcupaciones;
	}

	/**
	 * @param numOcupaciones The numOcupaciones to set.
	 */
	public void setNumOcupaciones(int numOcupaciones) {
		this.numOcupaciones = numOcupaciones;
	}

	/**
	 * @return Returns the ocupaciones.
	 */
	public HashMap getOcupaciones() {
		return ocupaciones;
	}

	/**
	 * @param ocupaciones The ocupaciones to set.
	 */
	public void setOcupaciones(HashMap ocupaciones) {
		this.ocupaciones = ocupaciones;
	}
	
	/**
	 * @return Retorna un elemento del mapa ocupaciones.
	 */
	public Object getOcupaciones(String key) {
		return ocupaciones.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa ocupaciones.
	 */
	public void setOcupaciones(String key,Object obj) {
		this.ocupaciones.put(key,obj);
	}

	/**
	 * @return Returns the offsetOM.
	 */
	public int getOffsetOM() {
		return offsetOM;
	}

	/**
	 * @param offsetOM The offsetOM to set.
	 */
	public void setOffsetOM(int offsetOM) {
		this.offsetOM = offsetOM;
	}

	/**
	 * @return Returns the posicionOM.
	 */
	public int getPosicionOM() {
		return posicionOM;
	}

	/**
	 * @param posicionOM The posicionOM to set.
	 */
	public void setPosicionOM(int posicionOM) {
		this.posicionOM = posicionOM;
	}

	/**
	 * @return Returns the totalActividadesOM.
	 */
	public int getTotalActividadesOM() {
		return totalActividadesOM;
	}

	/**
	 * @param totalActividadesOM The totalActividadesOM to set.
	 */
	public void setTotalActividadesOM(int totalActividadesOM) {
		this.totalActividadesOM = totalActividadesOM;
	}

	/**
	 * @return Returns the ultimoIndiceOM.
	 */
	public String getUltimoIndiceOM() {
		return ultimoIndiceOM;
	}

	/**
	 * @param ultimoIndiceOM The ultimoIndiceOM to set.
	 */
	public void setUltimoIndiceOM(String ultimoIndiceOM) {
		this.ultimoIndiceOM = ultimoIndiceOM;
	}
	
	
}
