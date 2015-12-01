package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;


public class PazYSalvoCarteraPacienteForm extends ValidatorForm{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PazYSalvoCarteraPacienteForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Atributos que guardan los datos del deudor para la busqueda
	 */
	
	private ArrayList<HashMap> tiposId = new ArrayList<HashMap>();
	private String tipoIdDeudor; 
	private String numeroIdDeudor; 	
	private String nombreDeudor;
	private String apellidoDeudor;
	
	/**
	 * Atributos que guardan los datos del paciente para la busqueda
	 */
	
	private String tipoIdPaciente; 
	private String numeroIdPaciente; 	
	private String nombrePaciente;
	private String apellidoPaciente;
	
	/**
	 * Atributos que guardan los datos de garantia y factura para la busqueda
	 * @return 
	 */
	private String codGarantia;
	private String estadoGarantia;
	private String numFactura;
	
	/**
	 * Arreglo para guardar los docuentos segun criterios de busqueda
	 */
	private ArrayList<DtoPazYSalvoCarteraPaciente> listaDocXDeudor= new ArrayList<DtoPazYSalvoCarteraPaciente>();
	private DtoPazYSalvoCarteraPaciente dtoPazYSalvo = new DtoPazYSalvoCarteraPaciente();
	
	/**
	 * Atributos que guardan la informacion del documento seleccionado para generar paz y salvo
	 */
	private int indice;
	
	private boolean guardo;
	
	private String codigopk;
	private String consecutivo;
	private String anioConsecutivo;
	private String fecha;
	private String hora;
	
	private int numDto;
	
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void resetBusqueda(int codigoInstitucion)
	{
		this.tipoIdDeudor="";
		this.numeroIdDeudor="";
		this.nombreDeudor="";
		this.apellidoDeudor="";
		this.tipoIdPaciente="";
		this.numeroIdPaciente="";
		this.nombrePaciente="";
		this.apellidoPaciente="";
		this.codGarantia="";
		this.estadoGarantia="";
		this.numFactura="";
		this.guardo=false;		
	}
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.tiposId= new ArrayList<HashMap>();
		this.tipoIdDeudor="";
		this.numeroIdDeudor="";
		this.nombreDeudor="";
		this.apellidoDeudor="";
		this.tipoIdPaciente="";
		this.numeroIdPaciente="";
		this.nombrePaciente="";
		this.apellidoPaciente="";
		this.codGarantia="";
		this.estadoGarantia="";
		this.numFactura="";
		this.indice=0;
		this.listaDocXDeudor= new ArrayList<DtoPazYSalvoCarteraPaciente>();
		this.guardo=false;
		this.codigopk="";
		this.consecutivo="";
		this.anioConsecutivo="";
		this.fecha="";
		this.hora="";
		this.numDto=0;
		this.dtoPazYSalvo= new DtoPazYSalvoCarteraPaciente();
	}
	
	public DtoPazYSalvoCarteraPaciente getDtoPazYSalvo() {
		return dtoPazYSalvo;
	}

	public void setDtoPazYSalvo(DtoPazYSalvoCarteraPaciente dtoPazYSalvo) {
		this.dtoPazYSalvo = dtoPazYSalvo;
	}

	public int getNumDto() {
		return numDto;
	}

	public void setNumDto(int numDto) {
		this.numDto = numDto;
	}

	public String getCodigopk() {
		return codigopk;
	}

	public void setCodigopk(String codigopk) {
		this.codigopk = codigopk;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public boolean isGuardo() {
		return guardo;
	}

	public void setGuardo(boolean guardo) {
		this.guardo = guardo;
	}

	public ArrayList<DtoPazYSalvoCarteraPaciente> getListaDocXDeudor() {
		return listaDocXDeudor;
	}

	public void setListaDocXDeudor(
			ArrayList<DtoPazYSalvoCarteraPaciente> listaDocXDeudor) {
		this.listaDocXDeudor = listaDocXDeudor;
	}
	
	

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getCodGarantia() {
		return codGarantia;
	}


	public void setCodGarantia(String codGarantia) {
		this.codGarantia = codGarantia;
	}


	public String getEstadoGarantia() {
		return estadoGarantia;
	}


	public void setEstadoGarantia(String estadoGarantia) {
		this.estadoGarantia = estadoGarantia;
	}


	public String getNumFactura() {
		return numFactura;
	}


	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}


	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}


	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}


	public String getNumeroIdPaciente() {
		return numeroIdPaciente;
	}


	public void setNumeroIdPaciente(String numeroIdPaciente) {
		this.numeroIdPaciente = numeroIdPaciente;
	}


	public String getNombrePaciente() {
		return nombrePaciente;
	}


	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}


	public String getApellidoPaciente() {
		return apellidoPaciente;
	}


	public void setApellidoPaciente(String apellidoPaciente) {
		this.apellidoPaciente = apellidoPaciente;
	}


	public String getNombreDeudor() {
		return nombreDeudor;
	}


	public void setNombreDeudor(String nombreDeudor) {
		this.nombreDeudor = nombreDeudor;
	}


	public String getApellidoDeudor() {
		return apellidoDeudor;
	}



	public void setApellidoDeudor(String apellidoDeudor) {
		this.apellidoDeudor = apellidoDeudor;
	}



	public String getTipoIdDeudor() {
		return tipoIdDeudor;
	}


	public void setTipoIdDeudor(String tipoIdDeudor) {
		this.tipoIdDeudor = tipoIdDeudor;
	}


	public String getNumeroIdDeudor() {
		return numeroIdDeudor;
	}


	public void setNumeroIdDeudor(String numeroIdDeudor) {
		this.numeroIdDeudor = numeroIdDeudor;
	}


	public ArrayList<HashMap> getTiposId() {
		return tiposId;
	}


	public void setTiposId(ArrayList<HashMap> tiposId) {
		this.tiposId = tiposId;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        if(this.estado.equals("realizarBusquedaDoc"))
        {
        	if(!this.tipoIdDeudor.equals("-1") && this.numeroIdDeudor.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Numero de Identificación del Deudor "));
        	if(!this.tipoIdPaciente.equals("-1") && this.numeroIdPaciente.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Numero de Identificación del Paciente "));
        }
        return errores;
    }
}