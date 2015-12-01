package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;

/**
 * Clase para el intercambio de información entre la vista y el control
 * @author Jorge Andrés Ortiz
 * @version 1.1
 */
@SuppressWarnings("serial")
public class HallazgosOdontologicosForm  extends ActionForm{
	
	private String estado;
	private int posHallazgo;
	private String patronOrdenar;
	private String linkSiguiente;
	private String esDescendente;
	private ArrayList<DtoHallazgoOdontologico> hallazgosDentales;
	private DtoHallazgoOdontologico nuevoHallazgo;
	private ArrayList<TipoHallazgoCeoCop> listaTiposHallazgo = new ArrayList<TipoHallazgoCeoCop>();
	private String diagnosticoSel;
	
	/**
	 * DTO PARA BUSQUED AVANZADA 
	 */
	private DtoHallazgoOdontologico dtoHallazgoOdontologico;
	private ArrayList<DtoHallazgoOdontologico> listaHallazgos;
	private String codigoHallazgos;
	
	public ArrayList<DtoHallazgoOdontologico> getListaHallazgos() {
		return listaHallazgos;
	}

	public void setListaHallazgos(ArrayList<DtoHallazgoOdontologico> listaHallazgos) {
		this.listaHallazgos = listaHallazgos;
	}

	/**
	 * Constructor vacío
	 */
	public HallazgosOdontologicosForm()
	{
		this.reset();
	}

	/**
	 * Limpia los campos de la forma
	 */
	public void reset()
	{
		this.estado= new String("");
		this.patronOrdenar= new String("");
		this.linkSiguiente=new String("");
		this.posHallazgo= ConstantesBD.codigoNuncaValido;
		this.hallazgosDentales= new ArrayList<DtoHallazgoOdontologico>();
		this.nuevoHallazgo = new DtoHallazgoOdontologico();
		this.diagnosticoSel=new String("");
		this.esDescendente=new String("");
		this.dtoHallazgoOdontologico = new DtoHallazgoOdontologico();
		this.listaHallazgos= new ArrayList<DtoHallazgoOdontologico>();
		this.codigoHallazgos="";
	}

	/**
	 * Limpiar el DTO para la inserción de un nuevo hallazgo
	 */
	public void resetNuevoHallazgo()
	{
		this.nuevoHallazgo = new DtoHallazgoOdontologico();
	}

	/**
	 * Obtiene el valor del atributo estado
	 *
	 * @return  Retorna atributo estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Establece el valor del atributo estado
	 *
	 * @param valor para el atributo estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Obtiene el valor del atributo posHallazgo
	 *
	 * @return  Retorna atributo posHallazgo
	 */
	public int getPosHallazgo()
	{
		return posHallazgo;
	}

	/**
	 * Establece el valor del atributo posHallazgo
	 *
	 * @param valor para el atributo posHallazgo
	 */
	public void setPosHallazgo(int posHallazgo)
	{
		this.posHallazgo = posHallazgo;
	}

	/**
	 * Obtiene el valor del atributo patronOrdenar
	 *
	 * @return  Retorna atributo patronOrdenar
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * Establece el valor del atributo patronOrdenar
	 *
	 * @param valor para el atributo patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * Obtiene el valor del atributo linkSiguiente
	 *
	 * @return  Retorna atributo linkSiguiente
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * Establece el valor del atributo linkSiguiente
	 *
	 * @param valor para el atributo linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * Obtiene el valor del atributo esDescendente
	 *
	 * @return  Retorna atributo esDescendente
	 */
	public String getEsDescendente()
	{
		return esDescendente;
	}

	/**
	 * Establece el valor del atributo esDescendente
	 *
	 * @param valor para el atributo esDescendente
	 */
	public void setEsDescendente(String esDescendente)
	{
		this.esDescendente = esDescendente;
	}

	/**
	 * Obtiene el valor del atributo hallazgosDentales
	 *
	 * @return  Retorna atributo hallazgosDentales
	 */
	public ArrayList<DtoHallazgoOdontologico> getHallazgosDentales()
	{
		return hallazgosDentales;
	}

	/**
	 * Establece el valor del atributo hallazgosDentales
	 *
	 * @param valor para el atributo hallazgosDentales
	 */
	public void setHallazgosDentales(
			ArrayList<DtoHallazgoOdontologico> hallazgosDentales)
	{
		this.hallazgosDentales = hallazgosDentales;
	}

	/**
	 * Obtiene el valor del atributo nuevoHallazgo
	 *
	 * @return  Retorna atributo nuevoHallazgo
	 */
	public DtoHallazgoOdontologico getNuevoHallazgo()
	{
		return nuevoHallazgo;
	}

	/**
	 * Establece el valor del atributo nuevoHallazgo
	 *
	 * @param valor para el atributo nuevoHallazgo
	 */
	public void setNuevoHallazgo(DtoHallazgoOdontologico nuevoHallazgo)
	{
		this.nuevoHallazgo = nuevoHallazgo;
	}

	/**
	 * Obtiene el valor del atributo diagnosticoSel
	 *
	 * @return  Retorna atributo diagnosticoSel
	 */
	public String getDiagnosticoSel()
	{
		return diagnosticoSel;
	}

	/**
	 * Establece el valor del atributo diagnosticoSel
	 *
	 * @param valor para el atributo diagnosticoSel
	 */
	public void setDiagnosticoSel(String diagnosticoSel)
	{
		this.diagnosticoSel = diagnosticoSel;
	}

	/**
	 * Obtiene el valor del atributo dtoHallazgoOdontologico
	 *
	 * @return  Retorna atributo dtoHallazgoOdontologico
	 */
	public DtoHallazgoOdontologico getDtoHallazgoOdontologico()
	{
		return dtoHallazgoOdontologico;
	}

	/**
	 * Establece el valor del atributo dtoHallazgoOdontologico
	 *
	 * @param valor para el atributo dtoHallazgoOdontologico
	 */
	public void setDtoHallazgoOdontologico(
			DtoHallazgoOdontologico dtoHallazgoOdontologico)
	{
		this.dtoHallazgoOdontologico = dtoHallazgoOdontologico;
	}

	/**
	 * Obtiene el valor del atributo codigoHallazgos
	 *
	 * @return  Retorna atributo codigoHallazgos
	 */
	public String getCodigoHallazgos()
	{
		return codigoHallazgos;
	}

	/**
	 * Establece el valor del atributo codigoHallazgos
	 *
	 * @param valor para el atributo codigoHallazgos
	 */
	public void setCodigoHallazgos(String codigoHallazgos)
	{
		this.codigoHallazgos = codigoHallazgos;
	}

	/**
	 * @param listaTiposHallazgo the listaTiposHallazgo to set
	 */
	public void setListaTiposHallazgo(ArrayList<TipoHallazgoCeoCop> listaTiposHallazgo) {
		this.listaTiposHallazgo = listaTiposHallazgo;
	}

	/**
	 * @return the listaTiposHallazgo
	 */
	public ArrayList<TipoHallazgoCeoCop> getListaTiposHallazgo() {
		return listaTiposHallazgo;
	}
}
