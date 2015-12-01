package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

public class AperturaCuentaPacienteOdontologicoForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private String estado;
	
	private String mensaje;
	
	private ArrayList<DtoSubCuentas> dtoSubCuentas;
	
	private ArrayList<DtoConvenio> convenios;
	 
	private ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo;
	
	private ArrayList<ArrayList<HashMap<String, Object>>> contratos;
	
	private String nombreViaIngreso;
	
	private HashMap<String, Object> listaAreas;
	
	private int area;
	
	private HashMap<String, String> valorPaciente;
	
	private int indice;
	
	private int convenioSeleccionado;
	
	private ArrayList<Boolean> manejaBonos;
	
	private ArrayList<Boolean> requiereIngresoValidacionAuto;
	
	private ArrayList<Boolean> ingresoPacienteRequiereAutorizacion;
	
	private ArrayList<DtoIntegridadDominio> listaMediosAutorizacion;
	
	private String urlRetorno;
	
	private int numeroDocumento;
	
	private DtoCuentas cuenta;
	
	/**
	 * Variable para saber si se está modificando o creando una nueva cuenta
	 */
	private boolean flujoModificacion;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("guardarCuenta"))
		{
			if(dtoSubCuentas!=null&&dtoSubCuentas.size()>0)
			{
				for(DtoSubCuentas subCuenta:dtoSubCuentas)
				{
					String valAuto=UtilidadTexto.formatearValores(subCuenta.getValorAutorizacion(),"#");
					if(valAuto.indexOf(".")>0)
					{
						String[] valAutoVect=valAuto.split(".");
						if(valAutoVect[0].length()>12)
						{
							errores.add("VALOR SUPERADO", new ActionMessage("error.cantidadParteEntera","Valor Autorizado","12"));  
						}
						if(valAutoVect[1].length()>2)
						{
							errores.add("VALOR SUPERADO", new ActionMessage("error.cantidadParteDecimal","Valor Autorizado","2"));  
						}
					}
					else
					{
						if(valAuto.length()>12)
						{
							errores.add("VALOR SUPERADO", new ActionMessage("error.cantidadParteEntera","Valor Autorizado","12"));  
						}
					}
				}
			}
		}
		return errores;
	}
	
	public void reset()
	{
		//this.estado="";
		this.mensaje="";
		this.dtoSubCuentas=new ArrayList<DtoSubCuentas>();
		this.convenios=new ArrayList<DtoConvenio>();
		this.contratos=new ArrayList<ArrayList<HashMap<String, Object>>>();
		this.requiereIngresoValidacionAuto=new ArrayList<Boolean>();
		this.ingresoPacienteRequiereAutorizacion=new ArrayList<Boolean>();
		this.manejaBonos=new ArrayList<Boolean>();
		
		this.listaAreas=null;
		this.valorPaciente=new HashMap<String, String>();
		
		this.flujoModificacion = false;
		this.cuenta = new DtoCuentas();
		this.conveniosAMostrarPresupuestoOdo = new ArrayList<HashMap<String, Object>>();
	}
	
	public void adicionarConvenioVacio()
	{
		this.dtoSubCuentas.add(new DtoSubCuentas());
		this.contratos.add(new ArrayList<HashMap<String,Object>>());
		this.manejaBonos.add(false);
		this.requiereIngresoValidacionAuto.add(false);
		this.ingresoPacienteRequiereAutorizacion.add(false);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ArrayList<DtoConvenio> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<DtoConvenio> convenios) {
		this.convenios = convenios;
	}

	public ArrayList<ArrayList<HashMap<String, Object>>> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<ArrayList<HashMap<String, Object>>> contratos) {
		this.contratos = contratos;
	}

	/**
	 * @return the nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	/**
	 * @param nombreViaIngreso the nombreViaIngreso to set
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	/**
	 * @return the listaAreas
	 */
	public HashMap<String, Object> getListaAreas() {
		return listaAreas;
	}

	/**
	 * @param listaAreas the listaAreas to set
	 */
	public void setListaAreas(HashMap<String, Object> listaAreas) {
		this.listaAreas = listaAreas;
	}

	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}

	/**
	 * @return the valorPaciente
	 */
	public HashMap<String, String> getValorPaciente() {
		return valorPaciente;
	}

	/**
	 * @param valorPaciente the valorPaciente to set
	 */
	public void setValorPaciente(HashMap<String, String> valorPaciente) {
		this.valorPaciente = valorPaciente;
	}

	/**
	 * @return the dtoSubCuentas
	 */
	public ArrayList<DtoSubCuentas> getDtoSubCuentas() {
		return dtoSubCuentas;
	}

	/**
	 * @param dtoSubCuentas the dtoCuentas to set
	 */
	public void setDtoSubCuentas(ArrayList<DtoSubCuentas> dtoSubCuentas) {
		this.dtoSubCuentas = dtoSubCuentas;
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * @return the convenioSeleccionado
	 */
	public int getConvenioSeleccionado() {
		return convenioSeleccionado;
	}

	/**
	 * @param convenioSeleccionado the convenioSeleccionado to set
	 */
	public void setConvenioSeleccionado(int convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}

	/**
	 * @return the manejaBonos
	 */
	public ArrayList<Boolean> getManejaBonos() {
		return manejaBonos;
	}

	/**
	 * @param manejaBonos the manejaBono to set
	 */
	public void setManejaBonos(ArrayList<Boolean> manejaBonos) {
		this.manejaBonos = manejaBonos;
	}

	/**
	 * @return the requiereIngresoValidacionAuto
	 */
	public ArrayList<Boolean> getRequiereIngresoValidacionAuto() {
		return requiereIngresoValidacionAuto;
	}

	/**
	 * @param requiereIngresoValidacionAuto the requiereIngresoValidacionAuto to set
	 */
	public void setRequiereIngresoValidacionAuto(ArrayList<Boolean> requiereIngresoValidacionAuto) {
		this.requiereIngresoValidacionAuto = requiereIngresoValidacionAuto;
	}

	/**
	 * @return the listaTiposAutorizacion
	 */
	public ArrayList<DtoIntegridadDominio> getListaMediosAutorizacion() {
		return listaMediosAutorizacion;
	}

	/**
	 * @param listaTiposAutorizacion the listaTiposAutorizacion to set
	 */
	public void setListaMediosAutorizacion(
			ArrayList<DtoIntegridadDominio> listaMediosAutorizacion) {
		this.listaMediosAutorizacion = listaMediosAutorizacion;
	}
	
	/**
	 * Obtener el numero de convenios
	 * @return
	 */
	public int getNumeroConvenios()
	{
		return dtoSubCuentas.size();
	}

	/**
	 * @return the urlRetorno
	 */
	public String getUrlRetorno() {
		return urlRetorno;
	}

	/**
	 * @param urlRetorno the urlRetorno to set
	 */
	public void setUrlRetorno(String urlRetorno) {
		this.urlRetorno = urlRetorno;
	}

	/**
	 * @return the ingresoPacienteRequiereAutorizacion
	 */
	public ArrayList<Boolean> getIngresoPacienteRequiereAutorizacion() {
		return ingresoPacienteRequiereAutorizacion;
	}

	/**
	 * @param ingresoPacienteRequiereAutorizacion the ingresoPacienteRequiereAutorizacion to set
	 */
	public void setIngresoPacienteRequiereAutorizacion(
			ArrayList<Boolean> ingresoPacienteRequiereAutorizacion) {
		this.ingresoPacienteRequiereAutorizacion = ingresoPacienteRequiereAutorizacion;
	}

	/**
	 * @return the numeroDocumento
	 */
	public int getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the flujoModificacion
	 */
	public boolean isFlujoModificacion() {
		return flujoModificacion;
	}

	/**
	 * @param flujoModificacion the flujoModificacion to set
	 */
	public void setFlujoModificacion(boolean flujoModificacion) {
		this.flujoModificacion = flujoModificacion;
	}

	/**
	 * @return the cuenta
	 */
	public DtoCuentas getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(DtoCuentas cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the conveniosAMostrarPresupuestoOdo
	 */
	public ArrayList<HashMap<String, Object>> getConveniosAMostrarPresupuestoOdo() {
		return conveniosAMostrarPresupuestoOdo;
	}

	/**
	 * @param conveniosAMostrarPresupuestoOdo the conveniosAMostrarPresupuestoOdo to set
	 */
	public void setConveniosAMostrarPresupuestoOdo(
			ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo) {
		this.conveniosAMostrarPresupuestoOdo = conveniosAMostrarPresupuestoOdo;
	}

}