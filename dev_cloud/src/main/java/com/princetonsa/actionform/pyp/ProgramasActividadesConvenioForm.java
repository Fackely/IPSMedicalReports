/**
 * Ago 08, 2006
 * 
 */
package com.princetonsa.actionform.pyp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Programas y Actividades por Convenio 
 */
public class ProgramasActividadesConvenioForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
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
	 * Objeto donde se almacena el listado de registros 
	 * parametrizados
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
	
	/**
	 * Posicion de la busqueda
	 */
	private int posBusqueda;
	
	//***ATRIBUTOS PARA LA ORDENACION Y PAGINACION**********************************************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private int offset;
	/**
	 * reset de los datos de la forma
	 *
	 */
	
	private ArrayList<ArrayList<Object>> listadoProgramas;
	
	public ArrayList<ArrayList<Object>> getListadoProgramas() {
		return listadoProgramas;
	}

	public void setListadoProgramas(ArrayList<ArrayList<Object>> listadoProgramas) {
		this.listadoProgramas = listadoProgramas;
	}

	public void reset()
	{
		this.estado="";
		this.convenio = "";
		this.programa = "";
		this.institucion = "";
		this.actividades = new HashMap();
		this.listadoProgramas=new ArrayList<ArrayList<Object>>();
		
		this.listado = new HashMap();
		this.numRegistros = 0;		
		this.posicion = 0;
		
		reset_pager();
		
		this.posBusqueda = 0;
		
		
	}
	
	/**
	 * Inicializa los datos del pager
	 * */
	public void reset_pager()
	{
		this.maxPageItems = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
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
			if(this.convenio.equals(""))
				errores.add("Convenio requerido",new ActionMessage("errors.required","El convenio"));
			
			if(this.programa.equals(""))
				errores.add("Programa requerido",new ActionMessage("errors.required","El programa"));
		}
		else if(this.estado.equals("guardar"))
		{
			//Se acomoda el mapa para registros nuevos
			for(int i=0;i<this.numRegistros;i++)
			{
				String aux =  this.getListado("codigo_app_"+i).toString();
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
					auxS0=this.getListado("codigo_app_"+i).toString().trim();
					
					for(int j=(this.getNumRegistros()-1);j>i;j--)
					{
						
						auxS1=this.getListado("codigo_app_"+j).toString().trim();
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
			
			if(!errores.isEmpty())
				this.estado = "parametrizar";
		}
		else if(this.estado.equals("nuevo")||this.estado.equals("ordenar")||this.estado.equals("redireccion")||this.estado.equals("eliminar"))
		{
			//Se acomoda el mapa para registros nuevos
			for(int i=0;i<this.numRegistros;i++)
			{
				String aux =  this.getListado("codigo_app_"+i).toString();
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
	 * @return Returns the posBusqueda.
	 */
	public int getPosBusqueda() {
		return posBusqueda;
	}

	/**
	 * @param posBusqueda The posBusqueda to set.
	 */
	public void setPosBusqueda(int posBusqueda) {
		this.posBusqueda = posBusqueda;
	}
	
	
}
