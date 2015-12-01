package com.princetonsa.actionform.inventarios;

import org.apache.struts.action.ActionForm;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

public class TiposInventarioForm extends ActionForm {
	
	private String estado;
	
	//mapa de Clase
	private HashMap claseInventario;
	private HashMap claseInventarioEliminado;
	private String patronOrdenar;
	private String ultimoPatron;

	//mapa de Gr
	private HashMap grupoInventario;
	private HashMap grupoInventarioEliminado;
	private String patronOrdenarGrupo;
	private String ultimoPatronGrupo;

	//mapa de Sub-Gr
	private HashMap subgrupoInventario;
	private HashMap subgrupoInventarioEliminado;
	private String patronOrdenarSubGrupo;
	private String ultimoPatronSubGrupo;	
	
    private int maxPageItems;

    private String linkSiguiente;
    
    private int offset;
    
    //offset para el mapa de grupo
    private int offsetgrupo;
    
    private int posEliminar;
    
    private int codigoEliminar;
    
    /**
     * Posicion del item clase al que se le quieren consultar los detalles (grupo)
     */
    private int indexDetalle;
    
    /**
     * Posicion del item grupo al que se le quieren consultar los detalles (Subgrupo)
     */
    private int indexDetalleGrupo;
    
    private boolean procesoExitoso;
    
	public void reset()
	{
		this.claseInventario=new HashMap();
		this.claseInventario.put("numRegistros", "0");
		this.claseInventarioEliminado=new HashMap();
		this.claseInventarioEliminado.put("numRegistros", "0");
		///
		this.grupoInventario=new HashMap();
		this.grupoInventario.put("numRegistros", "0");
		this.grupoInventarioEliminado=new HashMap();
		this.grupoInventarioEliminado.put("numRegistros", "0");
		///
		this.subgrupoInventario=new HashMap();
		this.subgrupoInventario.put("numRegistros", "0");
		this.subgrupoInventarioEliminado=new HashMap();
		this.subgrupoInventarioEliminado.put("numRegistros", "0");
		///
		
		this.linkSiguiente="inventarios.do";/////////////////////////???????????????????????????????'
		this.maxPageItems=20;
		this.offset=0;
		this.offsetgrupo=0;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.codigoEliminar=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.patronOrdenarGrupo="";
		this.ultimoPatronGrupo="";
		this.patronOrdenarSubGrupo="";
		this.ultimoPatronSubGrupo="";
		
		this.indexDetalle=ConstantesBD.codigoNuncaValido;
		this.indexDetalleGrupo=ConstantesBD.codigoNuncaValido;
		this.procesoExitoso=false;
	}    

	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			if(this.estado.equals("guardarClaseInventario"))
			{
				int numReg=Integer.parseInt(this.claseInventario.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.claseInventario.get("codigo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.claseInventario.get("codigo_"+i)+"").equalsIgnoreCase(this.claseInventario.get("codigo_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.claseInventario.get("codigo_"+i)));
							}
						}
					}
					if((this.claseInventario.get("nombre_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
					}
				}
			}
			
			if(this.estado.equals("guardarSubGrupoInventario"))
			{
				int numReg=Integer.parseInt(this.subgrupoInventario.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.subgrupoInventario.get("subgrupo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.subgrupoInventario.get("subgrupo_"+i)+"").equalsIgnoreCase(this.subgrupoInventario.get("subgrupo_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.subgrupoInventario.get("subgrupo_"+i)));
							}
						}
					}
					if((this.subgrupoInventario.get("nombre_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
					}
				}
			}			
			
			return errores;
		}


	public HashMap getClaseInventario() {
		return claseInventario;
	}


	public void setClaseInventario(HashMap claseInventario) {
		this.claseInventario = claseInventario;
	}

	//////////////////////
	public Object getClaseInventario(String key)
	{
		return claseInventario.get(key);
	}

	public void setClaseInventario(String key,Object value)
	{
		this.claseInventario.put(key, value);
	}
	//////////////////////
	public HashMap getClaseInventarioEliminado() {
		return claseInventarioEliminado;
	}


	public void setClaseInventarioEliminado(HashMap claseInventarioEliminado) {
		this.claseInventarioEliminado = claseInventarioEliminado;
	}

	//////////////////////
	public Object getClaseInventarioEliminado(String key)
	{
		return claseInventarioEliminado.get(key);
	}

	public void setClaseInventarioEliminado(String key,Object value)
	{
		this.claseInventarioEliminado.put(key, value);
	}
	//////////////////////	

	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public int getMaxPageItems() {
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public int getPosEliminar() {
		return posEliminar;
	}


	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	public int getIndexDetalle() {
		return indexDetalle;
	}


	public void setIndexDetalle(int indexDetalle) {
		this.indexDetalle = indexDetalle;
	}


	public HashMap getGrupoInventario() {
		return grupoInventario;
	}


	public void setGrupoInventario(HashMap grupoInventario) {
		this.grupoInventario = grupoInventario;
	}
	//////////////////////
	public Object getGrupoInventario(String key)
	{
		return grupoInventario.get(key);
	}

	public void setGrupoInventario(String key,Object value)
	{
		this.grupoInventario.put(key, value);
	}	
	/////////////////////

	public HashMap getGrupoInventarioEliminado() {
		return grupoInventarioEliminado;
	}


	public void setGrupoInventarioEliminado(HashMap grupoInventarioEliminado) {
		this.grupoInventarioEliminado = grupoInventarioEliminado;
	}
	 
	//////////////////////
	public Object getGrupoInventarioEliminado(String key)
	{
		return grupoInventarioEliminado.get(key);
	}

	public void setGrupoInventarioEliminado(String key,Object value)
	{
		this.grupoInventarioEliminado.put(key, value);
	}
	//////////////////////	


	public int getOffsetgrupo() {
		return offsetgrupo;
	}


	public void setOffsetgrupo(int offsetgrupo) {
		this.offsetgrupo = offsetgrupo;
	}


	public String getPatronOrdenarGrupo() {
		return patronOrdenarGrupo;
	}


	public void setPatronOrdenarGrupo(String patronOrdenarGrupo) {
		this.patronOrdenarGrupo = patronOrdenarGrupo;
	}


	public String getUltimoPatronGrupo() {
		return ultimoPatronGrupo;
	}


	public void setUltimoPatronGrupo(String ultimoPatronGrupo) {
		this.ultimoPatronGrupo = ultimoPatronGrupo;
	}


	public int getIndexDetalleGrupo() {
		return indexDetalleGrupo;
	}


	public void setIndexDetalleGrupo(int indexDetalleGrupo) {
		this.indexDetalleGrupo = indexDetalleGrupo;
	}


	public String getPatronOrdenarSubGrupo() {
		return patronOrdenarSubGrupo;
	}


	public void setPatronOrdenarSubGrupo(String patronOrdenarSubGrupo) {
		this.patronOrdenarSubGrupo = patronOrdenarSubGrupo;
	}


	public HashMap getSubgrupoInventario() {
		return subgrupoInventario;
	}


	public void setSubgrupoInventario(HashMap subgrupoInventario) {
		this.subgrupoInventario = subgrupoInventario;
	}

	//////////////////////
	public Object getSubgrupoInventario(String key)
	{
		return subgrupoInventario.get(key);
	}

	public void setSubgrupoInventario(String key,Object value)
	{
		this.subgrupoInventario.put(key, value);
	}
	//////////////////////		

	public HashMap getSubgrupoInventarioEliminado() {
		return subgrupoInventarioEliminado;
	}


	public void setSubgrupoInventarioEliminado(HashMap subgrupoInventarioEliminado) {
		this.subgrupoInventarioEliminado = subgrupoInventarioEliminado;
	}

	//////////////////////
	public Object getSubgrupoInventarioEliminado(String key)
	{
		return subgrupoInventarioEliminado.get(key);
	}

	public void setSubgrupoInventarioEliminado(String key,Object value)
	{
		this.subgrupoInventarioEliminado.put(key, value);
	}
	//////////////////////		

	public String getUltimoPatronSubGrupo() {
		return ultimoPatronSubGrupo;
	}


	public void setUltimoPatronSubGrupo(String ultimoPatronSubGrupo) {
		this.ultimoPatronSubGrupo = ultimoPatronSubGrupo;
	}


	public boolean isProcesoExitoso()
	{
		return procesoExitoso;
	}


	public void setProcesoExitoso(boolean procesoExitoso)
	{
		this.procesoExitoso = procesoExitoso;
	}


	/**
	 * @return the codigoEliminar
	 */
	public int getCodigoEliminar() {
		return codigoEliminar;
	}


	/**
	 * @param codigoEliminar the codigoEliminar to set
	 */
	public void setCodigoEliminar(int codigoEliminar) {
		this.codigoEliminar = codigoEliminar;
	}
	
	
}
