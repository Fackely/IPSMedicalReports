package com.princetonsa.actionform.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.administracion.EspecialidadesAction;
import com.princetonsa.action.administracion.FactorConversionMonedasAction;
import com.princetonsa.action.carteraPaciente.DocumentosGarantiaAction;
import com.princetonsa.dao.administracion.UtilidadesAdministracionDao;
import com.princetonsa.dto.administracion.DtoTiposMoneda;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Administracion.UtilidadesAdministracion;

/**
 * 
 * @author wilson
 *
 */
public class FactorConversionMonedasForm extends ValidatorForm  
{
	/**
     * Objeto para manejar los logs de esta clase
     */
    private Logger logger = Logger.getLogger(FactorConversionMonedasForm.class);
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DEL PAGER 
	 * ---------------------------------------------------*/
	
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * mapa 
	 */
	private HashMap factorMap;
	
	/**
	 * clon de factormap al momento de cargarlos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	private HashMap factorEliminadosMap;
	
	/**
	 * mapa para los tipos moneda
	 */
	private ArrayList<DtoTiposMoneda> tiposMonedaList;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
	/**
	 * indica cual es el index dentro del hashmap que se va a eliminar
	 */
	private String indexEliminado;
    
    /**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
    
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.factorMap= new HashMap();
    	this.factorMap.put("numRegistros", 0);
    	this.factorEliminadosMap= new HashMap();
    	this.factorEliminadosMap.put("numRegistros", 0);
    	this.tiposMonedaList = new ArrayList<DtoTiposMoneda>();
    	this.linkSiguiente="";
    	this.indexEliminado= "";
    	this.patronOrdenar="";
    	this.ultimoPatron="";
   }
    
    
    
    /**
     *Metodo encargado de inicializar los valores de la forma 
     * @param connection
     * @param institucion
     */
    public void init (Connection connection,int institucion)
    {
    
    	DtoTiposMoneda tiposMoneda = new DtoTiposMoneda (0,"","",institucion,"");
       	this.setTiposMonedaList(UtilidadesAdministracion.obtenerTiposMoneda(connection, tiposMoneda) );
    }
    
    
    
    /**
     * inicializa los tags de la forma
     * @param codigoInstitucionInt
     */
    public void inicializarTags(int codigoInstitucion) 
    {
		//@todo hacer el cargar  
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
        errores=super.validate(mapping,request);
        boolean numero =true;
        logger.info("el valor es "+this.getFactorMap());
        if (estado.equals("guardar"))
		{
        	int numReg=Integer.parseInt(this.getFactorMap("numRegistros")+"");
			
			for (int i=0;i<numReg;i++)
			{
				// aqui se valida si el valor del factor es un numero
				try 
				{
					Double.parseDouble(this.getFactorMap("factor_"+i).toString());
				} 
				catch (Exception e)
				{
					numero=false;
					errores.add("descripcion",new ActionMessage("errors.float","El factor "+this.getFactorMap("factor_"+i).toString()+" del registro "+(i+1)));
				}
				
				try 
				{
					if (Double.parseDouble(this.getFactorMap("factor_"+i).toString())<=0)
						errores.add("descripcion",new ActionMessage("errors.MayorQue","El factor "+this.getFactorMap("factor_"+i).toString()+" del registro "+(i+1),"0"));
						
				} 
				catch (Exception e)
				{
					
				}
					//aqui se valida si cumple con la cantidad de digitos enteros y decimales.			
				if (numero)
				{
					String [] tmp =UtilidadTexto.separarParteEnteraYDecimal(this.getFactorMap("factor_"+i).toString());
					if (tmp[0].length()>10)
						errores.add("descripcion",new ActionMessage("error.cantidadParteEntera",this.getFactorMap("factor_"+i).toString()+" del registro "+(i+1),"10"));
					if (tmp[1].length()>10)
						errores.add("descripcion",new ActionMessage("error.cantidadParteDecimal",this.getFactorMap("factor_"+i).toString()+" del registro "+(i+1),"10"));
						
				}
				
				
				
				if (!this.getFactorMap("fechainicial_"+i).toString().equals("") && !this.getFactorMap("fechafinal_"+i).toString().equals(""))
					if( !UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFactorMap("fechainicial_"+i).toString(),this.getFactorMap("fechafinal_"+i).toString()))
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Final "+this.getFactorMap("fechafinal_"+i).toString()+" del registro "+(i+1),"Inicial "+this.getFactorMap("fechainicial_"+i).toString()));
					
				
				
				if(this.getFactorMap("codigotipomoneda_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Moneda del registro "+(i+1)));
				
				}
				if(this.getFactorMap("fechainicial_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial del registro "+(i+1)));
				}
				if(this.getFactorMap("fechafinal_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final del registro "+(i+1)));
				}
				if(this.getFactorMap("factor_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","El Factor del registro "+(i+1)));
				}
				
				for (int k=0; k<numReg;k++)
				{
					if(k>i && !this.getFactorMap("codigotipomoneda_"+i).toString().trim().equals("") && !this.getFactorMap("codigotipomoneda_"+k).toString().trim().equals(""))
					{
						if (this.getFactorMap("codigotipomoneda_"+i).toString().equals(this.getFactorMap("codigotipomoneda_"+k).toString()))
						{
							if (!this.getFactorMap("fechainicial_"+k).toString().equals("") && !this.getFactorMap("fechafinal_"+i).toString().equals("") && !this.getFactorMap("fechainicial_"+i).toString().equals("") && !this.getFactorMap("fechafinal_"+k).toString().equals(""))
							if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFactorMap("fechainicial_"+k).toString(), this.getFactorMap("fechafinal_"+i).toString()))
							errores.add("descripcion",new ActionMessage("error.rangoFechasInvalido",this.getFactorMap("fechainicial_"+k).toString(),
									this.getFactorMap("fechafinal_"+k).toString()+" del registro "+(k+1),this.getFactorMap("fechainicial_"+i).toString(),
									this.getFactorMap("fechafinal_"+i).toString()+" del registro "+(i+1)));
							
						}
					}
				}
				
				
			}
		}
        
         return errores;
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
	 * @return the factorEliminadosMap
	 */
	public HashMap getFactorEliminadosMap() {
		return factorEliminadosMap;
	}

	/**
	 * @param factorEliminadosMap the factorEliminadosMap to set
	 */
	public void setFactorEliminadosMap(HashMap factorEliminadosMap) {
		this.factorEliminadosMap = factorEliminadosMap;
	}

	/**
	 * @return the factorMap
	 */
	public HashMap getFactorMap() {
		return factorMap;
	}

	/**
	 * @param factorMap the factorMap to set
	 */
	public void setFactorMap(HashMap factorMap) {
		this.factorMap = factorMap;
	}

	/**
	 * @return the indexEliminado
	 */
	public String getIndexEliminado() {
		return this.indexEliminado;
	}

	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
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
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
    
	/**
	 * @return the factorEliminadosMap
	 */
	public Object getFactorEliminadosMap(Object key) {
		return factorEliminadosMap.get(key);
	}

	/**
	 * @param factorEliminadosMap the factorEliminadosMap to set
	 */
	public void setFactorEliminadosMap(Object key, Object value) {
		this.factorEliminadosMap.put(key, value);
	}

	/**
	 * @return the factorMap
	 */
	public Object getFactorMap(Object key) {
		return factorMap.get(key);
	}

	/**
	 * @param factorMap the factorMap to set
	 */
	public void setFactorMap(Object key, Object value) {
		this.factorMap.put(key, value);
	}
	
	

	public ArrayList<DtoTiposMoneda> getTiposMonedaList() {
		return tiposMonedaList;
	}

	public void setTiposMonedaList(ArrayList<DtoTiposMoneda> tiposMonedaList) {
		this.tiposMonedaList = tiposMonedaList;
	}
}
