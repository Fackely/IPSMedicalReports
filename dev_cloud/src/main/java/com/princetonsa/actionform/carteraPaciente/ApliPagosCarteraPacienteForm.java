package com.princetonsa.actionform.carteraPaciente;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.princetonsa.action.carteraPaciente.ApliPagosCarteraPacienteAction;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDocGarantiaAplicarPago;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;

/**
 * @author Julio Hernández
 * jfhernandez@axioma-md.com
 * */
public class ApliPagosCarteraPacienteForm extends ValidatorForm 
{
	//---Atributos
	
	Logger logger = Logger.getLogger(ApliPagosCarteraPacienteForm.class);
	
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
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request); 
    	
    	
    	return errores;
	}
	
    //Variables
    
	String estado;
	DtoAplicacPagosCarteraPac dtoAplipagosCarteraPaciente;
	ArrayList<DtoRecibosCaja> listaRecibosCaja;
	ArrayList<DtoAplicacPagosCarteraPac> listaApliPagosCarteraPaciente;
	String tipo;
	String fechaInicialGen;
	String fechaFinalGen;
	String docInicial;
	String docFinal;
	HashMap<String,Object> filtros;
	ArrayList<DtoRecibosCaja> listaRecibosCajaFiltrados;
	String indiceAplicacionPago;
	ArrayList<DtoAplicacPagosCarteraPac> listadoDocsGarantia;
	String fechaAplicacion;
	HashMap valorAplicar;
	ArrayList<DtoCuotasDatosFinanciacion> listaCuotasDatosFin;
	ArrayList<DtoCuotasDatosFinanciacion> listadoCuotas;
	ArrayList<DtoAplicacPagosCarteraPac> listaAplicacionesCreadas;
	
	//Fin Variables
	
	
	//Resets
	public void reset()
	{
		this.estado="";
		this.dtoAplipagosCarteraPaciente=new DtoAplicacPagosCarteraPac();
		this.listaApliPagosCarteraPaciente=new ArrayList<DtoAplicacPagosCarteraPac>();
		this.listaRecibosCaja=new ArrayList<DtoRecibosCaja>();
		this.tipo="";
		this.fechaFinalGen="";
		this.fechaInicialGen="";
		this.docFinal="";
		this.docInicial="";
		this.filtros=new HashMap<String, Object>();
		this.indiceAplicacionPago="";
		this.listadoDocsGarantia=new ArrayList<DtoAplicacPagosCarteraPac>();
		this.fechaAplicacion=UtilidadFecha.getFechaActual();
		this.valorAplicar=new HashMap(); 
		this.listaCuotasDatosFin=new ArrayList<DtoCuotasDatosFinanciacion>();
		this.listaAplicacionesCreadas=new ArrayList<DtoAplicacPagosCarteraPac>();
		
	}
	
	public void resetAplicacion()
	{
		this.fechaAplicacion=UtilidadFecha.getFechaActual();
	}
	//Fin Resets
	
	public void resetListadoRC()
	{
		this.listaRecibosCaja=new ArrayList<DtoRecibosCaja>();
		this.valorAplicar=new HashMap();
	}

	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtoAplicacPagosCarteraPac getDtoAplipagosCarteraPaciente() {
		return dtoAplipagosCarteraPaciente;
	}

	public void setDtoAplipagosCarteraPaciente(
			DtoAplicacPagosCarteraPac dtoAplipagosCarteraPaciente) {
		this.dtoAplipagosCarteraPaciente = dtoAplipagosCarteraPaciente;
	}

	public ArrayList<DtoRecibosCaja> getListaRecibosCaja() {
		return listaRecibosCaja;
	}

	public void setListaRecibosCaja(ArrayList<DtoRecibosCaja> listaRecibosCaja) {
		this.listaRecibosCaja = listaRecibosCaja;
	}

	public ArrayList<DtoAplicacPagosCarteraPac> getListaApliPagosCarteraPaciente() {
		return listaApliPagosCarteraPaciente;
	}

	public void setListaApliPagosCarteraPaciente(
			ArrayList<DtoAplicacPagosCarteraPac> listaApliPagosCarteraPaciente) {
		this.listaApliPagosCarteraPaciente = listaApliPagosCarteraPaciente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFechaInicialGen() {
		return fechaInicialGen;
	}

	public void setFechaInicialGen(String fechaInicialGen) {
		this.fechaInicialGen = fechaInicialGen;
	}

	public String getFechaFinalGen() {
		return fechaFinalGen;
	}

	public void setFechaFinalGen(String fechaFinalGen) {
		this.fechaFinalGen = fechaFinalGen;
	}

	public String getDocInicial() {
		return docInicial;
	}

	public void setDocInicial(String docInicial) {
		this.docInicial = docInicial;
	}

	public String getDocFinal() {
		return docFinal;
	}

	public void setDocFinal(String docFinal) {
		this.docFinal = docFinal;
	}

	public HashMap<String, Object> getFiltros() {
		return filtros;
	}

	public void setFiltros(HashMap<String, Object> filtros) {
		this.filtros = filtros;
	}

	public ArrayList<DtoRecibosCaja> getListaRecibosCajaFiltrados() {
		return listaRecibosCajaFiltrados;
	}

	public void setListaRecibosCajaFiltrados(
			ArrayList<DtoRecibosCaja> listaRecibosCajaFiltrados) {
		this.listaRecibosCajaFiltrados = listaRecibosCajaFiltrados;
	}

	public String getIndiceAplicacionPago() {
		return indiceAplicacionPago;
	}

	public void setIndiceAplicacionPago(String indiceAplicacionPago) {
		this.indiceAplicacionPago = indiceAplicacionPago;
	}

	public ArrayList<DtoAplicacPagosCarteraPac> getListadoDocsGarantia() {
		return listadoDocsGarantia;
	}

	public void setListadoDocsGarantia(
			ArrayList<DtoAplicacPagosCarteraPac> listadoDocsGarantia) {
		this.listadoDocsGarantia = listadoDocsGarantia;
	}

	public String getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(String fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public HashMap getValorAplicar() {
		return valorAplicar;
	}

	public void setValorAplicar(HashMap valorAplicar) {
		this.valorAplicar = valorAplicar;
	}
	
	public Object getValorAplicar(String llave) {
		return valorAplicar.get(llave);
	}

	public ArrayList<DtoCuotasDatosFinanciacion> getListaCuotasDatosFin() {
		return listaCuotasDatosFin;
	}

	public void setListaCuotasDatosFin(
			ArrayList<DtoCuotasDatosFinanciacion> listaCuotasDatosFin) {
		this.listaCuotasDatosFin = listaCuotasDatosFin;
	}

	public ArrayList<DtoCuotasDatosFinanciacion> getListadoCuotas() {
		return listadoCuotas;
	}

	public void setListadoCuotas(ArrayList<DtoCuotasDatosFinanciacion> listadoCuotas) {
		this.listadoCuotas = listadoCuotas;
	}

	public ArrayList<DtoAplicacPagosCarteraPac> getListaAplicacionesCreadas() {
		return listaAplicacionesCreadas;
	}

	public void setListaAplicacionesCreadas(
			ArrayList<DtoAplicacPagosCarteraPac> listaAplicacionesCreadas) {
		this.listaAplicacionesCreadas = listaAplicacionesCreadas;
	}
}