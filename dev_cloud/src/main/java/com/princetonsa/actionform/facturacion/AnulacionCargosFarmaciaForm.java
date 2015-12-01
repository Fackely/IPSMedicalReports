/*
 * Aug 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.actionform.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class AnulacionCargosFarmaciaForm extends ActionForm 
{
	
	private String estado;
	
	/**
	 * Mapa para manejar los ingresos que tiene un paciente.
	 */
	private HashMap<String, Object> ingresos;

	/**
	 * Indice seleccionado del ingreso.
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * 
	 */
	private int indiceResponsableSeleccionado;
	
	/**
	 * Array list que contiene la informacion de los responsables.
	 */
	private ArrayList<DtoSubCuentas> responsables;
	
	/**
	 * Atributo que contiene el codigo del ingreso al cual se le esta haciendo la distribucion.
	 */
	private int codigoIngreso;
	
	/**
	 * Mapa para manejar las solicitudes
	 */
	private HashMap<String, Object> solicitudes;
	
	/**
	 * Mapa que contiene el detalle de cargos de una solicitud.
	 */
	private HashMap<String, Object> detalleSolicitudes;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int indiceSolSeleccionado;
	
	/**
	 * 
	 */
	private String motivoAnulacion;
	
	/**
	 * 
	 */
	private ResultadoBoolean mostrarMensaje;
	
	/**
	 * 
	 */
	private boolean esResumen;
	
	/**
	 * Mapa para manejar las solicitudes
	 */
	private HashMap<String, Object> solicitudesOrdenes;
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.ingresos=new HashMap<String, Object>();
		this.ingresos.put("numRegistros", "0");
		this.responsables=new ArrayList<DtoSubCuentas>();
		this.codigoIngreso=ConstantesBD.codigoNuncaValido;
		this.indiceResponsableSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceIngresoSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceSolSeleccionado=ConstantesBD.codigoNuncaValido;
		this.solicitudes=new HashMap<String, Object>();
		this.solicitudes.put("numRegistros", "0");
		this.detalleSolicitudes=new HashMap<String, Object>();
		this.detalleSolicitudes.put("numRegistros", "0");
		this.patronOrdenar="consecutivosolicitud_";
		this.ultimoPatron="consecutivosolicitud_";
		this.motivoAnulacion="";
		this.mostrarMensaje=new ResultadoBoolean(false);
		this.esResumen=false;
		this.solicitudesOrdenes=new HashMap<String, Object>();
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
		int cargosAnulados=0;
		if(estado.equals("guardar") || estado.equals("guardar2"))
		{
			if(this.motivoAnulacion.trim().equals(""))
			{
				errores.add("motivo",new ActionMessage("errors.required","Motivo Anulación "));
			}
			for(int a=0;a<Utilidades.convertirAEntero(detalleSolicitudes.get("numRegistros")+"");a++)
			{
				if(Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+"")>0)
				{
					cargosAnulados++;
				}
				if(Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+"")>Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadcargada_"+a)+""))
				{
					errores.add("anulada > cargada.", new ActionMessage("errors.integerMenorIgualQue","La cantidad Anulada del articulo "+detalleSolicitudes.get("articulo_"+a),"la cantidad cargada"));
				}
			}
			if(cargosAnulados==0)
			{
				errores.add("no cargos anulados",new ActionMessage("error.anulacionCargosFarmacia.ningunCargo"));
			}
		}
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
	 * @return the ingresos
	 */
	public HashMap<String, Object> getIngresos() {
		return ingresos;
	}


	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(HashMap<String, Object> ingresos) {
		this.ingresos = ingresos;
	}


	/**
	 * @return the indiceIngresoSeleccionado
	 */
	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}


	/**
	 * @param indiceIngresoSeleccionado the indiceIngresoSeleccionado to set
	 */
	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}

	/**
	 * @return the responsables
	 */
	public ArrayList<DtoSubCuentas> getResponsables() {
		return responsables;
	}


	/**
	 * @param responsables the responsables to set
	 */
	public void setResponsables(ArrayList<DtoSubCuentas> responsables) {
		this.responsables = responsables;
	}

	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}


	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}


	/**
	 * @return the indiceResponsableSeleccionado
	 */
	public int getIndiceResponsableSeleccionado() {
		return indiceResponsableSeleccionado;
	}


	/**
	 * @param indiceResponsableSeleccionado the indiceResponsableSeleccionado to set
	 */
	public void setIndiceResponsableSeleccionado(int indiceResponsableSeleccionado) {
		this.indiceResponsableSeleccionado = indiceResponsableSeleccionado;
	}


	/**
	 * @return the solicitudes
	 */
	public HashMap<String, Object> getSolicitudes() 
	{
		return solicitudes;
	}


	/**
	 * @param solicitudes the solicitudes to set
	 */
	public void setSolicitudes(HashMap<String, Object> solicitudes) 
	{
		this.solicitudes = solicitudes;
	}

	/**
	 * @return the solicitudes
	 */
	public Object getSolicitudes(String key) 
	{
		return solicitudes.get(key);
	}


	/**
	 * @param solicitudes the solicitudes to set
	 */
	public void setSolicitudes(String key,Object value) 
	{
		this.solicitudes.put(key, value);
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
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the indiceSolSeleccionado
	 */
	public int getIndiceSolSeleccionado() {
		return indiceSolSeleccionado;
	}


	/**
	 * @param indiceSolSeleccionado the indiceSolSeleccionado to set
	 */
	public void setIndiceSolSeleccionado(int indiceSolSeleccionado) {
		this.indiceSolSeleccionado = indiceSolSeleccionado;
	}


	/**
	 * @return the detalleSolicitudes
	 */
	public HashMap<String, Object> getDetalleSolicitudes() {
		return detalleSolicitudes;
	}


	/**
	 * @param detalleSolicitudes the detalleSolicitudes to set
	 */
	public void setDetalleSolicitudes(HashMap<String, Object> detalleSolicitudes) {
		this.detalleSolicitudes = detalleSolicitudes;
	}


	/**
	 * @return the detalleSolicitudes
	 */
	public Object getDetalleSolicitudes(String key) 
	{
		return detalleSolicitudes.get(key);
	}


	/**
	 * @param detalleSolicitudes the detalleSolicitudes to set
	 */
	public void setDetalleSolicitudes(String key,Object value) 
	{
		this.detalleSolicitudes.put(key, value);
	}


	/**
	 * @return the motivoAnulacion
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}


	/**
	 * @param motivoAnulacion the motivoAnulacion to set
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


	/**
	 * @return the esResumen
	 */
	public boolean isEsResumen() {
		return esResumen;
	}


	/**
	 * @param esResumen the esResumen to set
	 */
	public void setEsResumen(boolean esResumen) {
		this.esResumen = esResumen;
	}


	/**
	 * @return the mostrarMensaje
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}


	/**
	 * @param mostrarMensaje the mostrarMensaje to set
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumeroResponsables()
	{
		return this.responsables.size();
	}


	/**
	 * Método que retorna el valor del atributo solicitudesOrdenes
	 * @return solicitudesOrdenes
	 */
	public HashMap<String, Object> getSolicitudesOrdenes() {
		return solicitudesOrdenes;
	}


	/**
	 * Método que almacena el valor del atributo solicitudesOrdenes
	 * @param solicitudesOrdenes
	 */
	public void setSolicitudesOrdenes(HashMap<String, Object> solicitudesOrdenes) {
		this.solicitudesOrdenes = solicitudesOrdenes;
	}
	
	

}
