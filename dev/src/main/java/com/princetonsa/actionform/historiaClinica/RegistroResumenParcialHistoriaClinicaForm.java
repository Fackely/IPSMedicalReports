package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import javax.servlet.http.HttpServletRequest;

public class RegistroResumenParcialHistoriaClinicaForm extends ValidatorForm {
	
	private String estado="";
	
	private HashMap filtro=new HashMap();
	
	private HashMap filtrodet=new HashMap();
	
	private int indexmap;
	
	private String nota;
	
	private int emisor=0;
	
	private int ingreso;
	
	public void reset(){
		this.filtro=new HashMap();
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.filtro.put("numRegistros", 0);
		this.indexmap=ConstantesBD.codigoNuncaValido;
		this.filtrodet=new HashMap();
		this.filtrodet.put("numRegistros", 0);
		this.nota="";
		this.emisor=0;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (this.estado.equals("guardarNuevo"))
		{
			
			if(this.nota.equals(""))
			{
				errores.add("Motivo", new ActionMessage("errors.required"," Nota "));
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

	/**
	 * @return the filtro
	 */
	public HashMap getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(HashMap filtro) {
		this.filtro = filtro;
	}
	
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(String key, Object o) {
		this.filtro.put(key, o);
	}

	/**
	 * @return the indexmap
	 */
	public int getIndexmap() {
		return indexmap;
	}

	/**
	 * @param indexmap the indexmap to set
	 */
	public void setIndexmap(int indexmap) {
		this.indexmap = indexmap;
	}

	/**
	 * @return the filtrodet
	 */
	public HashMap getFiltrodet() {
		return filtrodet;
	}

	/**
	 * @param filtrodet the filtrodet to set
	 */
	public void setFiltrodet(HashMap filtrodet) {
		this.filtrodet = filtrodet;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @return the emisor
	 */
	public int getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(int emisor) {
		this.emisor = emisor;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	
	

}
