/*
 * Creado en Apr 18, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.interfaz;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

public class CuentaInventarioForm extends ValidatorForm
{
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Centro de costo que se selecciona en la primera página de clase de inventario
	 */
	private int centroCostoSeleccionado;
	
	/**
	 * Nombre del centro de costo seleccionado
	 */
	private String nombreCentroCosto;
	
	/**
	 * Código de la clase de inventario seleccionado
	 */
	private int codigoClaseInventario;
	
	/**
	 * Nombre de la clase de inventario seleccionado
	 */
	private String nombreClaseInventario;
	
	/**
	 * Código del grupo de inventario seleccionado
	 */
	private int codigoGrupoInventario;
	
	/**
	 * Nombre del grupo de inventario seleccionado
	 */
	private String nombreGrupoInventario;
	
	/**
	 * Código del subgrupo de inventario seleccionado
	 */
	private int codigoSubGrupoInventario;
	
	/**
	 * Nombre del subgrupo de inventario seleccionado
	 */
	private String nombreSubGrupoInventario;
	
	/**
	 * Mapa para guardar información de las clases de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaClaseInventarios;
	
	/**
	 * Mapa para guardar información de los grupos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaGrupoInventarios;
	
	/**
	 * Mapa para guardar información de los subgrupos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaSubGrupoInventarios;
	
	/**
	 * Mapa para guardar información de los articulos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaArticuloInventarios;
	
	//-------------------- Eliminación de cuenta contable ----------------------//
	/**
	 * Codigo del clase de inventario a la cuál se le elimina la cuenta contable
	 */
	private int claseInventarioEliminar;
	
	/**
	 * Codigo del grupo de inventario a la cuál se le elimina la cuenta contable
	 */
	private int grupoInventarioEliminar;
	
	/**
	 * Codigo del subGrupo de inventario a la cuál se le elimina la cuenta contable
	 */
	private int subGrupoInventarioEliminar;
	
	/**
	 * Codigo del artículo de inventario a la cuál se le elimina la cuenta contable
	 */
	private int articuloInventarioEliminar;
	
	/**
	 * Nombre de la cuenta contable que se elimina
	 */
	private String nombreCuentaEliminar;
	
	/**
	 * Posición en el mapa del registro escogido al eliminar la cuenta contable
	 */
	private int posicionEliminar;
	
	/**
	 * Nombre de la cuenta vigencia anterior que se elimina
	 */
	private String nombreCuentaVigenciaAnteriorEliminar;
	
	//--------------------Pager del listado de articulos ------------------------------//
	/**
     * Número de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Variables para manejar el paginador de la consulta de pacientes del centro de costo
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
	
//	---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.centroCostoSeleccionado=-1;
		this.nombreCentroCosto = "";
		this.nombreClaseInventario = "";
		this.nombreGrupoInventario = "";
		this.nombreSubGrupoInventario = "";
		this.mapaClaseInventarios = new HashMap();
		this.mapaGrupoInventarios = new HashMap();
		this.mapaSubGrupoInventarios = new HashMap();
		this.mapaArticuloInventarios = new HashMap();
		
		//----------Eliminación de cuenta contable -------------//
		this.claseInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.grupoInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.subGrupoInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.articuloInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.nombreCuentaEliminar="";
		this.nombreCuentaVigenciaAnteriorEliminar = "";
		this.posicionEliminar=ConstantesBD.codigoNuncaValido;
		
		this.maxPageItems = 0;
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
		
		return errores;
		
	}
	
	
	
	/**
	 * @return Retorna the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna the centroCostoSeleccionado.
	 */
	public int getCentroCostoSeleccionado()
	{
		return centroCostoSeleccionado;
	}

	/**
	 * @param centroCostoSeleccionado The centroCostoSeleccionado to set.
	 */
	public void setCentroCostoSeleccionado(int centroCostoSeleccionado)
	{
		this.centroCostoSeleccionado = centroCostoSeleccionado;
	}

	/**
	 * @return Retorna the nombreCentroCosto.
	 */
	public String getNombreCentroCosto()
	{
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto The nombreCentroCosto to set.
	 */
	public void setNombreCentroCosto(String nombreCentroCosto)
	{
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return Retorna the mapaClaseInventarios.
	 */
	public HashMap getMapaClaseInventarios()
	{
		return mapaClaseInventarios;
	}

	/**
	 * @param mapaClaseInventarios The mapaClaseInventarios to set.
	 */
	public void setMapaClaseInventarios(HashMap mapaClaseInventarios)
	{
		this.mapaClaseInventarios = mapaClaseInventarios;
	}
	
	/**
	 * @return Retorna mapaClaseInventarios.
	 */
	public Object getMapaClaseInventarios(Object key) {
		return mapaClaseInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaClaseInventarios(Object key, Object dato) {
		this.mapaClaseInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the mapaGrupoInventarios.
	 */
	public HashMap getMapaGrupoInventarios()
	{
		return mapaGrupoInventarios;
	}

	/**
	 * @param mapaGrupoInventarios The mapaGrupoInventarios to set.
	 */
	public void setMapaGrupoInventarios(HashMap mapaGrupoInventarios)
	{
		this.mapaGrupoInventarios = mapaGrupoInventarios;
	}
	
	/**
	 * @return Retorna mapaGrupoInventarios.
	 */
	public Object getMapaGrupoInventarios(Object key) {
		return mapaGrupoInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaGrupoInventarios(Object key, Object dato) {
		this.mapaGrupoInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the codigoClaseInventario.
	 */
	public int getCodigoClaseInventario()
	{
		return codigoClaseInventario;
	}

	/**
	 * @param codigoClaseInventario The codigoClaseInventario to set.
	 */
	public void setCodigoClaseInventario(int codigoClaseInventario)
	{
		this.codigoClaseInventario = codigoClaseInventario;
	}

	/**
	 * @return Retorna the nombreClaseInventario.
	 */
	public String getNombreClaseInventario()
	{
		return nombreClaseInventario;
	}

	/**
	 * @param nombreClaseInventario The nombreClaseInventario to set.
	 */
	public void setNombreClaseInventario(String nombreClaseInventario)
	{
		this.nombreClaseInventario = nombreClaseInventario;
	}

	/**
	 * @return Retorna the codigoGrupoInventario.
	 */
	public int getCodigoGrupoInventario()
	{
		return codigoGrupoInventario;
	}

	/**
	 * @param codigoGrupoInventario The codigoGrupoInventario to set.
	 */
	public void setCodigoGrupoInventario(int codigoGrupoInventario)
	{
		this.codigoGrupoInventario = codigoGrupoInventario;
	}

	/**
	 * @return Retorna the codigoSubGrupoInventario.
	 */
	public int getCodigoSubGrupoInventario()
	{
		return codigoSubGrupoInventario;
	}

	/**
	 * @param codigoSubGrupoInventario The codigoSubGrupoInventario to set.
	 */
	public void setCodigoSubGrupoInventario(int codigoSubGrupoInventario)
	{
		this.codigoSubGrupoInventario = codigoSubGrupoInventario;
	}

	/**
	 * @return Retorna the mapaArticuloInventarios.
	 */
	public HashMap getMapaArticuloInventarios()
	{
		return mapaArticuloInventarios;
	}

	/**
	 * @param mapaArticuloInventarios The mapaArticuloInventarios to set.
	 */
	public void setMapaArticuloInventarios(HashMap mapaArticuloInventarios)
	{
		this.mapaArticuloInventarios = mapaArticuloInventarios;
	}
	
	/**
	 * @return Retorna mapaArticuloInventarios.
	 */
	public Object getMapaArticuloInventarios(Object key) {
		return mapaArticuloInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaArticuloInventarios(Object key, Object dato) {
		this.mapaArticuloInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the mapaSubGrupoInventarios.
	 */
	public HashMap getMapaSubGrupoInventarios()
	{
		return mapaSubGrupoInventarios;
	}

	/**
	 * @param mapaSubGrupoInventarios The mapaSubGrupoInventarios to set.
	 */
	public void setMapaSubGrupoInventarios(HashMap mapaSubGrupoInventarios)
	{
		this.mapaSubGrupoInventarios = mapaSubGrupoInventarios;
	}
	
	/**
	 * @return Retorna mapaSubGrupoInventarios.
	 */
	public Object getMapaSubGrupoInventarios(Object key) {
		return mapaSubGrupoInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaSubGrupoInventarios(Object key, Object dato) {
		this.mapaSubGrupoInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the nombreGrupoInventario.
	 */
	public String getNombreGrupoInventario()
	{
		return nombreGrupoInventario;
	}

	/**
	 * @param nombreGrupoInventario The nombreGrupoInventario to set.
	 */
	public void setNombreGrupoInventario(String nombreGrupoInventario)
	{
		this.nombreGrupoInventario = nombreGrupoInventario;
	}

	/**
	 * @return Retorna the nombreSubGrupoInventario.
	 */
	public String getNombreSubGrupoInventario()
	{
		return nombreSubGrupoInventario;
	}

	/**
	 * @param nombreSubGrupoInventario The nombreSubGrupoInventario to set.
	 */
	public void setNombreSubGrupoInventario(String nombreSubGrupoInventario)
	{
		this.nombreSubGrupoInventario = nombreSubGrupoInventario;
	}

	/**
	 * @return Retorna the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna the index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return Retorna the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	/**
	 * @return Retorna the pager.
	 */
	public int getPager()
	{
		return pager;
	}

	/**
	 * @param pager The pager to set.
	 */
	public void setPager(int pager)
	{
		this.pager = pager;
	}

	public int getArticuloInventarioEliminar() {
		return articuloInventarioEliminar;
	}

	public void setArticuloInventarioEliminar(int articuloInventarioEliminar) {
		this.articuloInventarioEliminar = articuloInventarioEliminar;
	}

	public int getClaseInventarioEliminar() {
		return claseInventarioEliminar;
	}

	public void setClaseInventarioEliminar(int claseInventarioEliminar) {
		this.claseInventarioEliminar = claseInventarioEliminar;
	}

	public int getGrupoInventarioEliminar() {
		return grupoInventarioEliminar;
	}

	public void setGrupoInventarioEliminar(int grupoInventarioEliminar) {
		this.grupoInventarioEliminar = grupoInventarioEliminar;
	}

	public String getNombreCuentaEliminar() {
		return nombreCuentaEliminar;
	}

	public void setNombreCuentaEliminar(String nombreCuentaEliminar) {
		this.nombreCuentaEliminar = nombreCuentaEliminar;
	}

	public int getPosicionEliminar() {
		return posicionEliminar;
	}

	public void setPosicionEliminar(int posicionEliminar) {
		this.posicionEliminar = posicionEliminar;
	}

	public int getSubGrupoInventarioEliminar() {
		return subGrupoInventarioEliminar;
	}

	public void setSubGrupoInventarioEliminar(int subGrupoInventarioEliminar) {
		this.subGrupoInventarioEliminar = subGrupoInventarioEliminar;
	}

	public String getNombreCuentaVigenciaAnteriorEliminar() {
		return nombreCuentaVigenciaAnteriorEliminar;
	}

	public void setNombreCuentaVigenciaAnteriorEliminar(
			String nombreCuentaVigenciaAnteriorEliminar) {
		this.nombreCuentaVigenciaAnteriorEliminar = nombreCuentaVigenciaAnteriorEliminar;
	}

}
