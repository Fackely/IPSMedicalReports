/**
 * 
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.FormasPago;


/**
 * @author Cristhian Murillo
 */
public class TrasladosCajaPrincipalMayorForm extends ValidatorForm
{
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	private MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.TrasladosCajaPrincipalMayorForm");
	
	/** * Serial  */
	private static final long serialVersionUID = 1L;
	
	private String estado;
	
	/** * Posicion de las autorizaciones */
	private int posArray;
	
	/** * Parametros para ordenar */
	private String patronOrdenar;
	
	/** * Parametros para ordenar */
	private String esDescendente;
	
	/** * Lista de Centros de Atención  */
	private ArrayList<CentroAtencion> listaCentrosAtencion;

	/** * Centro de Atención Seleccionado  */
	private String selectCentroAtencion;
	
	/** * Caja principal Seleccionada  */
	private String selectCajaPrincipal;
	
	/** * Caja Mayor Seleccionada  */
	private String selectCajaMayor;
	
	/** * Lista Cajas Principales */
	private ArrayList<Cajas> listaCajasPrincipal;
	
	/** * Lista Cajas Mayores */
	private ArrayList<Cajas> listaCajasMayor;
	
	/** * Fecha Cierre */
	private Date fechaCierre;
	
	/** * Indica si el usuario es Cajero o no */
	private boolean pasoValidacionesIniciales;
	
	/** * Centro de Atención seleccionado */
	private CentroAtencion centroAtencionSeleccionado;
	
	/** * Caja Principal seleccionada */
	private Cajas cajaPrincipalSeleccionada;
	
	/** * Caja Mayor Seleccionada */
	private Cajas cajaMayorSeleccionada;
	
	/** * Lista de traslados */
	private ArrayList<DtoConsolidadoCierreReporte> listaTraslados;
	
	/** * Lista con las formas de pago parametrizadas en el sistema */
	private List<FormasPago> formasPago;
	
	/** * Cantidad de colspan para mostrar en jsp  */
	private Integer cantidadColSpanCierres;
	
	/** * Indica si se pudo realizar el traslado  */
	private boolean trasladoExitoso = true;
	
	/** * Institución cargada en el sistema. Utilizada para la impresión  */
	private InstitucionBasica institucionBasica;
	
	/** * Fecha Inicio (rango) */
	private Date fechaInicio;
	
	/** * Fecha Fin (rango) */
	private Date fechaFin;
	
	/** * Rango inicial de traslado */
	private String numeroInicioTraslado;
	
	/** * Rango final de traslado */
	private String numeroFinTraslado;
	
	/**	 * Indica si se ingreso a la funcionalidad por la opción de consulta	 */
	private boolean tipoConsulta;
	
	/** * Columnas adicionales a las formas de pago: Caja, Cierre, Cajero y Total  */
	private int columnasAdicionales = 4;
	
	/** * Indica si se debe mostrar agrupado por consecutivo */
	private boolean agruparSumadoPorConsecutivoTraslado;
	
	/** * Indica si la consulta del detalle debe hacerse filtrando por el consecutivo en este caso del traslado */
	private boolean filtrarPorconsecutivo;
	
	/** * Traslado seleccionado*/
	private DtoConsolidadoCierreReporte trasladoSeleccionado;
	
	/** * Fecha Cierre */
	private Date fechaTraslado;
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.setListaCentrosAtencion(new ArrayList<CentroAtencion>());
		this.setListaCajasPrincipal(new ArrayList<Cajas>());
		this.setListaCajasMayor(new ArrayList<Cajas>());
		this.selectCentroAtencion					= null;
		this.selectCajaPrincipal					= null;
		this.selectCajaMayor						= null;
		this.fechaCierre							= null;
		this.pasoValidacionesIniciales				= true;
		this.centroAtencionSeleccionado				= new CentroAtencion();
		this.cajaPrincipalSeleccionada				= new Cajas();
		this.cajaMayorSeleccionada					= new Cajas();
		this.listaTraslados							= new ArrayList<DtoConsolidadoCierreReporte>();
		this.formasPago 							= new ArrayList<FormasPago>();
		this.cantidadColSpanCierres 				= new Integer(0);
		this.trasladoExitoso						= false;
		this.institucionBasica						= new InstitucionBasica();
		this.fechaInicio							= null;
		this.fechaFin								= null;
		this.numeroInicioTraslado					= null;
		this.numeroFinTraslado						= null;
		this.tipoConsulta							= false;
		this.agruparSumadoPorConsecutivoTraslado	= false;
		this.filtrarPorconsecutivo					= false;
		this.trasladoSeleccionado					= new DtoConsolidadoCierreReporte();
		this.fechaTraslado							= null;
	}
	
	/**
	 * valida si se debe asignar la opción de seleccione a las listas
	 * @param listaGenerica
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	private boolean validarAsignarSeleccione(ArrayList listaGenerica)
	{
		boolean asignarObjetoSeleccione = false;
		
		if(Utilidades.isEmpty(listaGenerica)){
			asignarObjetoSeleccione = true;
		}
		else{
			if(listaGenerica.size() == 1){
				asignarObjetoSeleccione = false;
			}
			else{
				asignarObjetoSeleccione = true;
			}
		}
		return asignarObjetoSeleccione;
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
	 * @return the listaCentrosAtencion
	 */
	public ArrayList<CentroAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * @param listaCentrosAtencion the listaCentrosAtencion to set
	 */
	public void setListaCentrosAtencion(ArrayList<CentroAtencion> listaCentrosAtencion) 
	{
		this.listaCentrosAtencion = new ArrayList<CentroAtencion>();
		
		if(validarAsignarSeleccione(listaCentrosAtencion))
		{
			CentroAtencion centroAtencionseleccione = new CentroAtencion();
			centroAtencionseleccione.setConsecutivo(ConstantesBD.codigoNuncaValido);
			centroAtencionseleccione.setDescripcion(this.fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.Seleccione"));
			this.listaCentrosAtencion.add(0, centroAtencionseleccione);
		}
		this.listaCentrosAtencion.addAll(listaCentrosAtencion);
	}
	
	/**
	 * @return the selectCentroAtencion
	 */
	public String getSelectCentroAtencion() {
		return selectCentroAtencion;
	}

	/**
	 * @param selectCentroAtencion the selectCentroAtencion to set
	 */
	public void setSelectCentroAtencion(String selectCentroAtencion) {
		this.selectCentroAtencion = selectCentroAtencion;
	}

	/**
	 * @return the selectCajaPrincipal
	 */
	public String getSelectCajaPrincipal() {
		return selectCajaPrincipal;
	}

	/**
	 * @param selectCajaPrincipal the selectCajaPrincipal to set
	 */
	public void setSelectCajaPrincipal(String selectCajaPrincipal) {
		this.selectCajaPrincipal = selectCajaPrincipal;
	}

	/**
	 * @return the selectCajaMayor
	 */
	public String getSelectCajaMayor() {
		return selectCajaMayor;
	}

	/**
	 * @param selectCajaMayor the selectCajaMayor to set
	 */
	public void setSelectCajaMayor(String selectCajaMayor) {
		this.selectCajaMayor = selectCajaMayor;
	}

	/**
	 * @return the listaCajasPrincipal
	 */
	public ArrayList<Cajas> getListaCajasPrincipal() {
		return listaCajasPrincipal;
	}

	/**
	 * @param listaCajasPrincipal the listaCajasPrincipal to set
	 */
	public void setListaCajasPrincipal(ArrayList<Cajas> listaCajasPrincipal) 
	{
		this.listaCajasPrincipal = new ArrayList<Cajas>();
		
		if(validarAsignarSeleccione(listaCajasPrincipal))
		{
			Cajas cajaSeleccione = new Cajas();
			cajaSeleccione.setConsecutivo(ConstantesBD.codigoNuncaValido);
			cajaSeleccione.setDescripcion(this.fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.Seleccione"));
			this.listaCajasPrincipal.add(0, cajaSeleccione);
		}
		this.listaCajasPrincipal.addAll(listaCajasPrincipal);
	}

	/**
	 * @return the listaCajasMayor
	 */
	public ArrayList<Cajas> getListaCajasMayor() {
		return listaCajasMayor;
	}

	/**
	 * @param listaCajasMayor the listaCajasMayor to set
	 */
	public void setListaCajasMayor(ArrayList<Cajas> listaCajasMayor) 
	{
		this.listaCajasMayor = new ArrayList<Cajas>(); 
		if(validarAsignarSeleccione(listaCajasMayor))
		{
			Cajas cajaSeleccione = new Cajas();
			cajaSeleccione.setConsecutivo(ConstantesBD.codigoNuncaValido);
			cajaSeleccione.setDescripcion(this.fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.Seleccione"));
			this.listaCajasMayor.add(0, cajaSeleccione);
		}
		this.listaCajasMayor.addAll(listaCajasMayor);
	}

	/**
	 * @return the fechaCierre
	 */
	public Date getFechaCierre() {
		return fechaCierre;
	}

	/**
	 * @param fechaCierre the fechaCierre to set
	 */
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	/**
	 * @return the pasoValidacionesIniciales
	 */
	public boolean isPasoValidacionesIniciales() {
		return pasoValidacionesIniciales;
	}

	/**
	 * @param pasoValidacionesIniciales the pasoValidacionesIniciales to set
	 */
	public void setPasoValidacionesIniciales(boolean pasoValidacionesIniciales) {
		this.pasoValidacionesIniciales = pasoValidacionesIniciales;
	}

	/**
	 * @return the centroAtencionSeleccionado
	 */
	public CentroAtencion getCentroAtencionSeleccionado() {
		return centroAtencionSeleccionado;
	}

	/**
	 * @param centroAtencionSeleccionado the centroAtencionSeleccionado to set
	 */
	public void setCentroAtencionSeleccionado(
			CentroAtencion centroAtencionSeleccionado) {
		this.centroAtencionSeleccionado = centroAtencionSeleccionado;
	}

	/**
	 * @return the cajaPrincipalSeleccionada
	 */
	public Cajas getCajaPrincipalSeleccionada() {
		return cajaPrincipalSeleccionada;
	}

	/**
	 * @param cajaPrincipalSeleccionada the cajaPrincipalSeleccionada to set
	 */
	public void setCajaPrincipalSeleccionada(Cajas cajaPrincipalSeleccionada) {
		this.cajaPrincipalSeleccionada = cajaPrincipalSeleccionada;
	}

	/**
	 * @return the cajaMayorSeleccionada
	 */
	public Cajas getCajaMayorSeleccionada() {
		return cajaMayorSeleccionada;
	}

	/**
	 * @param cajaMayorSeleccionada the cajaMayorSeleccionada to set
	 */
	public void setCajaMayorSeleccionada(Cajas cajaMayorSeleccionada) {
		this.cajaMayorSeleccionada = cajaMayorSeleccionada;
	}

	/**
	 * @return the listaTraslados
	 */
	public ArrayList<DtoConsolidadoCierreReporte> getListaTraslados() {
		return listaTraslados;
	}

	/**
	 * @param listaTraslados the listaTraslados to set
	 */
	public void setListaTraslados(ArrayList<DtoConsolidadoCierreReporte> listaTraslados) {
		this.listaTraslados = listaTraslados;
	}

	/**
	 * @return the formasPago
	 */
	public List<FormasPago> getFormasPago() {
		return formasPago;
	}

	/**
	 * @param formasPago the formasPago to set
	 */
	public void setFormasPago(List<FormasPago> formasPago) {
		this.formasPago = formasPago;
	}

	/**
	 * @return the cantidadColSpanCierres
	 */
	public Integer getCantidadColSpanCierres() {
		return cantidadColSpanCierres;
	}

	/**
	 * @param cantidadColSpanCierres the cantidadColSpanCierres to set
	 */
	public void setCantidadColSpanCierres(Integer cantidadColSpanCierres) {
		this.cantidadColSpanCierres = cantidadColSpanCierres;
	}

	/**
	 * @return the trasladoExitoso
	 */
	public boolean isTrasladoExitoso() {
		return trasladoExitoso;
	}

	/**
	 * @param trasladoExitoso the trasladoExitoso to set
	 */
	public void setTrasladoExitoso(boolean trasladoExitoso) {
		this.trasladoExitoso = trasladoExitoso;
	}

	/**
	 * @return the institucionBasica
	 */
	public InstitucionBasica getInstitucionBasica() {
		return institucionBasica;
	}

	/**
	 * @param institucionBasica the institucionBasica to set
	 */
	public void setInstitucionBasica(InstitucionBasica institucionBasica) {
		this.institucionBasica = institucionBasica;
	}

	/**
	 * @return valor de fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio el fechaInicio para asignar
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return valor de fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin el fechaFin para asignar
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return valor de numeroInicioTraslado
	 */
	public String getNumeroInicioTraslado() {
		return numeroInicioTraslado;
	}

	/**
	 * @param numeroInicioTraslado el numeroInicioTraslado para asignar
	 */
	public void setNumeroInicioTraslado(String numeroInicioTraslado) {
		this.numeroInicioTraslado = numeroInicioTraslado;
	}

	/**
	 * @return valor de numeroFinTraslado
	 */
	public String getNumeroFinTraslado() {
		return numeroFinTraslado;
	}

	/**
	 * @param numeroFinTraslado el numeroFinTraslado para asignar
	 */
	public void setNumeroFinTraslado(String numeroFinTraslado) {
		this.numeroFinTraslado = numeroFinTraslado;
	}

	/**
	 * @return valor de tipoConsulta
	 */
	public boolean isTipoConsulta() {
		return tipoConsulta;
	}

	/**
	 * @param tipoConsulta el tipoConsulta para asignar
	 */
	public void setTipoConsulta(boolean tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	/**
	 * @return valor de columnasAdicionales
	 */
	public int getColumnasAdicionales() {
		return columnasAdicionales;
	}

	/**
	 * @return valor de agruparSumadoPorConsecutivoTraslado
	 */
	public boolean isAgruparSumadoPorConsecutivoTraslado() {
		return agruparSumadoPorConsecutivoTraslado;
	}

	/**
	 * @param agruparSumadoPorConsecutivoTraslado el agruparSumadoPorConsecutivoTraslado para asignar
	 */
	public void setAgruparSumadoPorConsecutivoTraslado(
			boolean agruparSumadoPorConsecutivoTraslado) {
		this.agruparSumadoPorConsecutivoTraslado = agruparSumadoPorConsecutivoTraslado;
	}

	/**
	 * @return valor de filtrarPorconsecutivo
	 */
	public boolean isFiltrarPorconsecutivo() {
		return filtrarPorconsecutivo;
	}

	/**
	 * @param filtrarPorconsecutivo el filtrarPorconsecutivo para asignar
	 */
	public void setFiltrarPorconsecutivo(boolean filtrarPorconsecutivo) {
		this.filtrarPorconsecutivo = filtrarPorconsecutivo;
	}

	/**
	 * @return valor de trasladoSeleccionado
	 */
	public DtoConsolidadoCierreReporte getTrasladoSeleccionado() {
		return trasladoSeleccionado;
	}

	/**
	 * @param trasladoSeleccionado el trasladoSeleccionado para asignar
	 */
	public void setTrasladoSeleccionado(
			DtoConsolidadoCierreReporte trasladoSeleccionado) {
		this.trasladoSeleccionado = trasladoSeleccionado;
	}

	/**
	 * @return valor de fechaTraslado
	 */
	public Date getFechaTraslado() {
		return fechaTraslado;
	}

	/**
	 * @param fechaTraslado el fechaTraslado para asignar
	 */
	public void setFechaTraslado(Date fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

}
