package com.princetonsa.actionform.facturasVarias;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.Utilidades;

/**
 * @author Juan Sebastián Castaño 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *ConceptosFacturasVarias
 */

public class ConceptosFacturasVariasForm extends ValidatorForm {

	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	private String estado;
	private HashMap<String, Object> listadoFactVar = new HashMap<String, Object>();
	private String linkSiguiente;
	private String codigoEliminarPos;
	private String indice;
	private String ultimoIndice;
	private String tipoconcepto;
	private String tercero;
	private String descripciontercero;

	public void reset ()
	{
		
		this.estado = "";
		this.listadoFactVar = new HashMap<String, Object>();
		this.linkSiguiente = "";
		this.indice = "";
		this.ultimoIndice = "";
		this.tipoconcepto="";
		this.tercero="";
		this.descripciontercero="";
	}

	public String getDescripciontercero() {
		return descripciontercero;
	}

	public void setDescripciontercero(String descripciontercero) {
		this.descripciontercero = descripciontercero;
	}

	public String getTipoconcepto() {
		return tipoconcepto;
	}

	public void setTipoconcepto(String tipoconcepto) {
		this.tipoconcepto = tipoconcepto;
	}
	public String getTercero() {
		return tercero;
	}
	public void setTercero(String tercero) {
		this.tercero = tercero;
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap<String, Object> getListadoFactVar() {
		return listadoFactVar;
	}

	public void setListadoFactVar(HashMap<String, Object> listadoFactVar) {
		this.listadoFactVar = listadoFactVar;
	}
	
	public Object getListadoFactVar(String key) {
		return listadoFactVar.get(key);
	}

	public void setListadoFactVar(String key,Object obj) {
		this.listadoFactVar.put(key, obj);
	}
	
	/**
	 * Metodo que retorna el tamaño del mapa listadoFactVar
	 * @return
	 */
	public int getNumListadoFactVar ()
	{
		return Utilidades.convertirAEntero(this.listadoFactVar.get("numRegistros")+"", true);
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        HashMap<String, Object> ListConFacVarTemp = new HashMap<String, Object>();
		ListConFacVarTemp = getListadoFactVar();
		
        if(this.estado.equals("guardar"))
        {
        	
        	for(int i=0;i<this.getNumListadoFactVar();i++)
        	{
        		//*********VALIDACION DEL CODIGO***********************
        		if(this.getListadoFactVar("codigo_"+i).toString().equals(""))
        		{
        			errores.add("",new ActionMessage("errors.required","El código en el registro N° "+(i+1)));        		
        		}
        		else
        		{
        			if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.getListadoFactVar("codigo_"+i).toString()))
        			{
        				errores.add("", new ActionMessage("errors.caracteresInvalidos","El código en el registro N° "+(i+1)));
        			}
        			
        			for(int j=0;j<this.getNumListadoFactVar();j++)
                	{
        				if (ListConFacVarTemp.get("codigo_"+i).toString().equals(this.getListadoFactVar("codigo_"+j).toString()) && j!=i)
            				{
                			
                				errores.add("",new ActionMessage("errors.required","Por favor, cambie el valor del código en el registro N° "+(j+1)+" debido a que esta repetido y "));
                    			//j=this.getNumListadoFactVar() + 1;
                    			break;
            				}
                			
                	}
        		}
        		if(this.getListadoFactVar("descripcion_"+i).toString().equals(""))
        		{
        			errores.add("",new ActionMessage("errors.required", "El campo de descripción en el registro N° "+(i+1)));
        		}
        		else
        		{
        			if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.getListadoFactVar("descripcion_"+i).toString()))
        			{
        				errores.add("", new ActionMessage("errors.caracteresInvalidos","La descripción en el registro N° "+(i+1)));
        			}
        		}
        		if(this.getListadoFactVar("tipoconcepto_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
        		{
        			errores.add("",new ActionMessage("errors.required", "El tipo de concepto en el registro N° "+(i+1)));
        		}
        	
        	}
        	
        	if(!errores.isEmpty())
        		this.estado = "";
        	
        }
                
        return errores;
    }

	public String getcodigoEliminarPos() {
		return codigoEliminarPos;
	}

	public void setcodigoEliminarPos(String codigoEliminarPos) {
		this.codigoEliminarPos = codigoEliminarPos;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getUltimoIndice() {
		return ultimoIndice;
	}

	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	
}
