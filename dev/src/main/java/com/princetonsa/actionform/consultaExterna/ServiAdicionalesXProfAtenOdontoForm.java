package com.princetonsa.actionform.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;

public class ServiAdicionalesXProfAtenOdontoForm extends ValidatorForm{

	private String estado;
	private String index;
	private ArrayList<HashMap<String,Object>> listProfesionales;
	private HashMap<String, Object> listServiciosSel;
	private String codProfesionalSel;
	private String linkSiguiente;
	private int posServicioAdd;
	private boolean buscarServicios;
	private String patronOrdenar;
	private String esDescendente;
	private String flujo;
	private HashMap mensajeExito;
	private ArrayList<DtoServiciosAdicionalesProfesionales> listServiciosAdicionales;
	private DtoServiciosAdicionalesProfesionales nuevoServicio;
	
	public ServiAdicionalesXProfAtenOdontoForm()
	{
		this.reset();
		
	}
	
	public void reset()
	{
		//this.estado=new String("");
		this.codProfesionalSel=new String("");
		this.index=new String("");
		this.linkSiguiente=new String("");
		this.patronOrdenar= new String("");
		this.esDescendente= new String("");
		this.flujo= new String("");
		this.mensajeExito=new HashMap();
		this.mensajeExito.put("operacionExitosa", "");
		this.mensajeExito.put("mensaje", "");
		
		this.buscarServicios=false;
		this.posServicioAdd=ConstantesBD.codigoNuncaValido;
		this.listProfesionales=new ArrayList<HashMap<String,Object>>();
		this.listServiciosSel = new HashMap<String, Object>();
		this.nuevoServicio= new DtoServiciosAdicionalesProfesionales();
		this.listServiciosAdicionales=new ArrayList<DtoServiciosAdicionalesProfesionales>();
		
	}
	
	
	
	public void resetListServicios()
	{
		this.listServiciosSel = new HashMap<String, Object>();
		this.listServiciosSel.put("numRegistros","0");
		this.listServiciosAdicionales=new ArrayList<DtoServiciosAdicionalesProfesionales>();
	}
	
    
	public void resetMensajeExito()
	{
		this.mensajeExito=new HashMap();
		this.mensajeExito.put("operacionExitosa", "");
		this.mensajeExito.put("mensaje", "");
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

	public void resetNuevoServicio()
	{
		this.nuevoServicio= new DtoServiciosAdicionalesProfesionales();
	}
	
	/**
	 * @return the listProfesionales
	 */
	public ArrayList<HashMap<String, Object>> getListProfesionales() {
		return listProfesionales;
	}

	/**
	 * @param listProfesionales the listProfesionales to set
	 */
	public void setListProfesionales(
			ArrayList<HashMap<String, Object>> listProfesionales) {
		this.listProfesionales = listProfesionales;
	}

	/**
	 * @return the listServiciosAdicionales
	 */
	public ArrayList<DtoServiciosAdicionalesProfesionales> getListServiciosAdicionales() {
		return listServiciosAdicionales;
	}

	/**
	 * @param listServiciosAdicionales the listServiciosAdicionales to set
	 */
	public void setListServiciosAdicionales(
			ArrayList<DtoServiciosAdicionalesProfesionales> listServiciosAdicionales) {
		this.listServiciosAdicionales = listServiciosAdicionales;
	}

	/**
	 * @return the nuevoServicio
	 */
	public DtoServiciosAdicionalesProfesionales getNuevoServicio() {
		return nuevoServicio;
	}

	/**
	 * @param nuevoServicio the nuevoServicio to set
	 */
	public void setNuevoServicio(DtoServiciosAdicionalesProfesionales nuevoServicio) {
		this.nuevoServicio = nuevoServicio;
	}

	/**
	 * @return the codProfesionalSel
	 */
	public String getCodProfesionalSel() {
		return codProfesionalSel;
	}

	/**
	 * @param codProfesionalSel the codProfesionalSel to set
	 */
	public void setCodProfesionalSel(String codProfesionalSel) {
		this.codProfesionalSel = codProfesionalSel;
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
	 * @return the posServicioAdd
	 */
	public int getPosServicioAdd() {
		return posServicioAdd;
	}

	/**
	 * @param posServicioAdd the posServicioAdd to set
	 */
	public void setPosServicioAdd(int posServicioAdd) {
		this.posServicioAdd = posServicioAdd;
	}

	/**
	 * @return the listServiciosSel
	 */
	public HashMap<String, Object> getListServiciosSel() {
		return listServiciosSel;
	}

	/**
	 * @param listServiciosSel the listServiciosSel to set
	 */
	public void setListServiciosSel(HashMap<String, Object> listServiciosSel) {
		this.listServiciosSel = listServiciosSel;
	}
	
	public Object getListServiciosSel(String key) {
		return listServiciosSel.get(key);
	}

	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setListServiciosSel(String key,Object obj) {
		this.listServiciosSel.put(key, obj);
	}
	
	/**
	 * Método para consultar el número de Servicios
	 * @return
	 */
	public int getNumServicios()
	{
		return Utilidades.convertirAEntero(this.listServiciosSel.get("numRegistros")+"",true);
	}
	
	/**
	 * Método para obtener el número real de Servicios que no han sido eliminadas
	 * @return
	 */
	public int getNumServiciosReales()
	{
		int contador = 0;
		
		for(int i=0;i<getNumServicios();i++)
			if(!UtilidadTexto.getBoolean(this.listServiciosSel.get("fueEliminado_"+i)+""))
				contador++;
		
		return contador;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the buscarServicios
	 */
	public boolean isBuscarServicios() {
		return buscarServicios;
	}

	/**
	 * @param buscarServicios the buscarServicios to set
	 */
	public void setBuscarServicios(boolean buscarServicios) {
		this.buscarServicios = buscarServicios;
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
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the flujo
	 */
	public String getFlujo() {
		return flujo;
	}

	/**
	 * @param flujo the flujo to set
	 */
	public void setFlujo(String flujo) {
		this.flujo = flujo;
	}

	/**
	 * @return the mensajeExito
	 */
	public HashMap getMensajeExito() {
		return mensajeExito;
	}

	/**
	 * @param mensajeExito the mensajeExito to set
	 */
	public void setMensajeExito(HashMap mensajeExito) {
		this.mensajeExito = mensajeExito;
	}
	
}
