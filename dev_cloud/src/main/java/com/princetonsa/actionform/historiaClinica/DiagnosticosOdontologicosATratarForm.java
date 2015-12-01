package com.princetonsa.actionform.historiaClinica;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;




/**
 */
public class DiagnosticosOdontologicosATratarForm extends ValidatorForm
{
	

	/**
	 * 
	 */
	private String estado= "";
	
	/**
	 * 
	 */
	private int indiceDiagnosticoEliminar;
	
	/**
	 * 
	 */
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	/**
	 * 
	 */
	private HashMap mapaDiagnosticos;
	
	/**
	 * 
	 */
	private HashMap mapaDiagnosticosEliminados;
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		
		this.estado="";
		this.indiceDiagnosticoEliminar=ConstantesBD.codigoNuncaValido;
		this.mapaDiagnosticos= new HashMap();
		this.mapaDiagnosticos.put("numRegistros", "0");
		this.mapaDiagnosticosEliminados= new HashMap();
		this.mapaDiagnosticosEliminados.put("numRegistros", "0");
	}
	
	/**
	 * 
	 */
	public void resetMensaje(){
		this.mensaje=new ResultadoBoolean(false);
	}
	/**
	 * 
	 */
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			
			if(this.estado.equals("guardar"))
			{
				int numReg=Integer.parseInt(this.mapaDiagnosticos.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.mapaDiagnosticos.get("nombre_"+i)+"").trim().equals(""))
					{
						errores.add("", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
					}
					if((this.mapaDiagnosticos.get("acronimo_"+i)+"").trim().equals(""))
					{
						errores.add("", new ActionMessage("errors.required","El Acronimo del registro "+(i+1)));
					}
					else
					{
						for(int l=0;l<i;l++)
						{
							if((this.mapaDiagnosticos.get("acronimo_"+i)+"").equals(this.mapaDiagnosticos.get("acronimo_"+l)+""+""))  
				             {		                  		                  
				                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Acronimo "+this.mapaDiagnosticos.get("acronimo_"+l)+""));                 
				             }
						}
					}
				}
			}
			return errores;
		}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIndiceDiagnosticoEliminar() {
		return indiceDiagnosticoEliminar;
	}

	public void setIndiceDiagnosticoEliminar(int indiceDiagnosticoEliminar) {
		this.indiceDiagnosticoEliminar = indiceDiagnosticoEliminar;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public HashMap getMapaDiagnosticos() {
		return mapaDiagnosticos;
	}

	public void setMapaDiagnosticos(HashMap mapaDiagnosticos) {
		this.mapaDiagnosticos = mapaDiagnosticos;
	}
	
	public Object getMapaDiagnosticos(String key) {
		return mapaDiagnosticos.get(key);
	}

	public void setMapaDiagnosticos(String key,Object value) {
		this.mapaDiagnosticos.put(key, value);
	}

	public Object getMapaDiagnosticosEliminados(String key) {
		return mapaDiagnosticosEliminados.get(key);
	}

	public void setMapaDiagnosticosEliminados(String key,Object value) {
		this.mapaDiagnosticosEliminados.put(key, value);
	}

	public HashMap getMapaDiagnosticosEliminados() {
		return mapaDiagnosticosEliminados;
	}

	public void setMapaDiagnosticosEliminados(HashMap mapaDiagnosticosEliminados) {
		this.mapaDiagnosticosEliminados = mapaDiagnosticosEliminados;
	}

	
	


}