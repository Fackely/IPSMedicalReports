/**
 * 
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.princetonsa.enu.impresion.EnumTiposSalida;


/**
 * @author Cristhian Murillo
 */
public class ConsultaAbonosPacienteForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String saldoAbonos;
	
	/**
	 * 
	 */
	private String saldoAbonosListado;
	
	
	/**
	 * 
	 */
	private int tipoMovimientoBusqueda;

	/**
	 * 
	 */
	private int centroAtencionBusqueda;

	/**
	 * 
	 */
	private String ingresoBusqueda;

	/**
	 * 
	 */
	private String fechaInicialBusqueda;

	/**
	 * 
	 */
	private String fechaFinalBusqueda;
	
	
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	/**
	 * Almacena la posición del movimiento de abono seleccionado.
	 */
	private int indiceMovimientoSeleccionado;
	
	/**
	 * Listado de los abonos realizados
	 */
	private ArrayList<DtoMovmimientosAbonos> abonosRealizados;
	
	/**
	 * Almacena el movimiento de abono que ha sido seleccionado para ver 
	 * el detalle del documento del movimiento del abono.
	 */
	private DtoMovmimientosAbonos dtoMovimientoSeleccionado;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicación.
	 */
	private String path;
	
	/**
	 * Almacena el listado con el consolidado por tipo de movimiento
	 *  de abono que ha tenido un paciente.
	 */
	private ArrayList<DtoMovmimientosAbonos> listadoConsolidadoMovimientos;
	
	/**
	 * Almacena un valor booleano que indica si se debe 
	 * mostrar o no la ubicación de la JSP.
	 */
	private String mostrarUbicacionJSP;
	
	/**
	 * Atributo que indica el tipo de salida de impresión 
	 * del reporte generado.
	 */
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Almacena el nombre del archivo generado.
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	
	
	/**
	 * Atributo que almacena el índice de la posición
	 * en donde se encuentra el registro seleccionado.
	 */
	private int index;
	
	/**
	 * atributo usado para la paginaci&oacute;n del listado de faltantes sobrantes encontrados.
	 */
	private int posArray;
	
	/**
	 * Atributo que permite definir si se debe mostrar el mensaje 
	 * informativo en el resultado de la búsqueda avanzada.
	 */
	private String mostrarMensajeInfo;

	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.tipoMovimientoBusqueda=ConstantesBD.codigoNuncaValido;
		this.centroAtencionBusqueda=ConstantesBD.codigoNuncaValido;
		this.ingresoBusqueda="";
		this.fechaInicialBusqueda="";
		this.fechaFinalBusqueda="";
		this.centrosAtencion=new ArrayList<DtoCentrosAtencion>();
		this.saldoAbonos="";
		this.saldoAbonosListado="";
		this.indiceMovimientoSeleccionado = ConstantesBD.codigoNuncaValido;
		this.dtoMovimientoSeleccionado = new DtoMovmimientosAbonos();
		this.path = "";
		this.listadoConsolidadoMovimientos = new ArrayList<DtoMovmimientosAbonos>();
		this.mostrarUbicacionJSP = ConstantesBD.acronimoNo;
		this.tipoSalida = "";
		this.enumTipoSalida = null;
		this.nombreArchivoGenerado = "";
		this.patronOrdenar = "";
		this.esDescendente = "";
		this.index = ConstantesBD.codigoNuncaValido;
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.mostrarMensajeInfo = ConstantesBD.acronimoNo;
	}
	
	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public void resetBusquedaAvanzada(){
		this.tipoMovimientoBusqueda=ConstantesBD.codigoNuncaValido;
		this.centroAtencionBusqueda=ConstantesBD.codigoNuncaValido;
		this.ingresoBusqueda="";
		this.fechaInicialBusqueda="";
		this.fechaFinalBusqueda="";
		this.mostrarMensajeInfo = ConstantesBD.acronimoNo;
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

	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}

	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	public int getTipoMovimientoBusqueda() {
		return tipoMovimientoBusqueda;
	}

	public void setTipoMovimientoBusqueda(int tipoMovimientoBusqueda) {
		this.tipoMovimientoBusqueda = tipoMovimientoBusqueda;
	}

	public int getCentroAtencionBusqueda() {
		return centroAtencionBusqueda;
	}

	public void setCentroAtencionBusqueda(int centroAtencionBusqueda) {
		this.centroAtencionBusqueda = centroAtencionBusqueda;
	}

	public String getIngresoBusqueda() {
		return ingresoBusqueda;
	}

	public void setIngresoBusqueda(String ingresoBusqueda) {
		this.ingresoBusqueda = ingresoBusqueda;
	}

	public String getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}

	public void setFechaInicialBusqueda(String fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}

	public String getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}

	public void setFechaFinalBusqueda(String fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSaldoAbonos() {
		return saldoAbonos;
	}

	public void setSaldoAbonos(String saldoAbonos) {
		this.saldoAbonos = saldoAbonos;
	}

	/**
	 * @return Retorna atributo abonosRealizados
	 */
	public ArrayList<DtoMovmimientosAbonos> getAbonosRealizados()
	{
		return abonosRealizados;
	}

	/**
	 * @param abonosRealizados Asigna atributo abonosRealizados
	 */
	public void setAbonosRealizados(
			ArrayList<DtoMovmimientosAbonos> abonosRealizados)
	{
		this.abonosRealizados = abonosRealizados;
	}

	public String getSaldoAbonosListado() {
		return saldoAbonosListado;
	}

	public void setSaldoAbonosListado(String saldoAbonosListado) {
		this.saldoAbonosListado = saldoAbonosListado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indiceMovimientoSeleccionado
	
	 * @return retorna la variable indiceMovimientoSeleccionado 
	 * @author Yennifer Guerrero 
	 */
	public int getIndiceMovimientoSeleccionado() {
		return indiceMovimientoSeleccionado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indiceMovimientoSeleccionado
	
	 * @param valor para el atributo indiceMovimientoSeleccionado 
	 * @author Yennifer Guerrero
	 */
	public void setIndiceMovimientoSeleccionado(int indiceMovimientoSeleccionado) {
		this.indiceMovimientoSeleccionado = indiceMovimientoSeleccionado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoMovimientoSeleccionado
	
	 * @return retorna la variable dtoMovimientoSeleccionado 
	 * @author Yennifer Guerrero 
	 */
	public DtoMovmimientosAbonos getDtoMovimientoSeleccionado() {
		return dtoMovimientoSeleccionado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoMovimientoSeleccionado
	
	 * @param valor para el atributo dtoMovimientoSeleccionado 
	 * @author Yennifer Guerrero
	 */
	public void setDtoMovimientoSeleccionado(
			DtoMovmimientosAbonos dtoMovimientoSeleccionado) {
		this.dtoMovimientoSeleccionado = dtoMovimientoSeleccionado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo path
	
	 * @return retorna la variable path 
	 * @author Yennifer Guerrero 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo path
	
	 * @param valor para el atributo path 
	 * @author Yennifer Guerrero
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoConsolidadoMovimientos
	
	 * @return retorna la variable listadoConsolidadoMovimientos 
	 * @author Yennifer Guerrero 
	 */
	public ArrayList<DtoMovmimientosAbonos> getListadoConsolidadoMovimientos() {
		return listadoConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoConsolidadoMovimientos
	
	 * @param valor para el atributo listadoConsolidadoMovimientos 
	 * @author Yennifer Guerrero
	 */
	public void setListadoConsolidadoMovimientos(
			ArrayList<DtoMovmimientosAbonos> listadoConsolidadoMovimientos) {
		this.listadoConsolidadoMovimientos = listadoConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostrarUbicacionJSP
	
	 * @return retorna la variable mostrarUbicacionJSP 
	 * @author Yennifer Guerrero 
	 */
	public String getMostrarUbicacionJSP() {
		
		if (util.ValoresPorDefecto.getMostrarNombreJSP()) {
			this.mostrarUbicacionJSP =  ConstantesBD.acronimoSi;
		}
		
		return mostrarUbicacionJSP;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostrarUbicacionJSP
	
	 * @param valor para el atributo mostrarUbicacionJSP 
	 * @author Yennifer Guerrero
	 */
	public void setMostrarUbicacionJSP(String mostrarUbicacionJSP) {
		this.mostrarUbicacionJSP = mostrarUbicacionJSP;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoSalida
	
	 * @return retorna la variable tipoSalida 
	 * @author Yennifer Guerrero 
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoSalida
	
	 * @param valor para el atributo tipoSalida 
	 * @author Yennifer Guerrero
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo enumTipoSalida
	
	 * @return retorna la variable enumTipoSalida 
	 * @author Yennifer Guerrero 
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo enumTipoSalida
	
	 * @param valor para el atributo enumTipoSalida 
	 * @author Yennifer Guerrero
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreArchivoGenerado
	
	 * @return retorna la variable nombreArchivoGenerado 
	 * @author Yennifer Guerrero 
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreArchivoGenerado
	
	 * @param valor para el atributo nombreArchivoGenerado 
	 * @author Yennifer Guerrero
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostrarMensajeInfo
	 * @return retorna la variable mostrarMensajeInfo 
	 * @author Yennifer Guerrero 
	 */
	public String getMostrarMensajeInfo() {
		return mostrarMensajeInfo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostrarMensajeInfo
	 * @param valor para el atributo mostrarMensajeInfo 
	 * @author Yennifer Guerrero
	 */
	public void setMostrarMensajeInfo(String mostrarMensajeInfo) {
		this.mostrarMensajeInfo = mostrarMensajeInfo;
	}
	
}
