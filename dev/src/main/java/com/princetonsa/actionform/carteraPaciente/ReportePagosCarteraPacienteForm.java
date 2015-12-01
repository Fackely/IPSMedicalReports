package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;


public class ReportePagosCarteraPacienteForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ReportePagosCarteraPacienteForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	private ArrayList<DtoDatosFinanciacion> listaDatosFinanciacion= new ArrayList<DtoDatosFinanciacion>();
	
	private ArrayList<HashMap> listaAplicPagosCar= new ArrayList<HashMap>();
	
	private HashMap pagos= new HashMap();
	
	/**
	 * Atributos de la busqueda
	 */
	private HashMap centrosAtencion = new HashMap();
	private int centroAtencion;	
	private String tipoDoc;
	private String tipoPeriodo;
	private int anioIni;
	private int anioFin;
	private String tipoSalida;
	private String fechaIni;
	private String fechaFin;
	
	/**
	 * Atributos para generar el archivo plano
	 */
	private String ruta="";
	private String urlArchivo="";
	private boolean existeArchivo=false;	
	private boolean operacionTrue = false;
	private String criteriosConsulta;	
	
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.centrosAtencion= new HashMap();
		this.centroAtencion= ConstantesBD.codigoNuncaValido;
		this.tipoDoc="-1";
		this.tipoPeriodo="-1";
		this.anioIni=0;
		this.anioFin=0;
		this.tipoSalida="-1";
		this.fechaIni="";
		this.fechaFin="";
		this.listaDatosFinanciacion= new ArrayList<DtoDatosFinanciacion>();
		this.listaAplicPagosCar= new ArrayList<HashMap>();
		this.ruta ="";
		this.urlArchivo="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.criteriosConsulta="";
		this.pagos=new HashMap();
	}
	

	public Object getPagos(String key) {
		return pagos.get(key);
	}
	
	public void setPagos(String key, Object value) {
		this.pagos.put(key, value);
	}

	public HashMap getPagos() {
		return pagos;
	}

	public void setPagos(HashMap pagos) {
		this.pagos = pagos;
	}



	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}



	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}



	public String getRuta() {
		return ruta;
	}



	public void setRuta(String ruta) {
		this.ruta = ruta;
	}



	public String getUrlArchivo() {
		return urlArchivo;
	}



	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}



	public boolean isExisteArchivo() {
		return existeArchivo;
	}



	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}



	public boolean isOperacionTrue() {
		return operacionTrue;
	}



	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}



	public ArrayList<HashMap> getListaAplicPagosCar() {
		return listaAplicPagosCar;
	}



	public void setListaAplicPagosCar(ArrayList<HashMap> listaAplicPagosCar) {
		this.listaAplicPagosCar = listaAplicPagosCar;
	}



	public ArrayList<DtoDatosFinanciacion> getListaDatosFinanciacion() {
		return listaDatosFinanciacion;
	}


	public void setListaDatosFinanciacion(
			ArrayList<DtoDatosFinanciacion> listaDatosFinanciacion) {
		this.listaDatosFinanciacion = listaDatosFinanciacion;
	}


	public String getFechaIni() {
		return fechaIni;
	}


	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}


	public String getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getTipoSalida() {
		return tipoSalida;
	}


	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	public int getAnioIni() {
		return anioIni;
	}


	public void setAnioIni(int anioIni) {
		this.anioIni = anioIni;
	}


	public int getAnioFin() {
		return anioFin;
	}


	public void setAnioFin(int anioFin) {
		this.anioFin = anioFin;
	}


	public String getTipoPeriodo() {
		return tipoPeriodo;
	}


	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}


	public String getTipoDoc() {
		return tipoDoc;
	}


	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}


	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}


	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
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
        
        if(this.estado.equals("buscar"))
        {
        	if(this.tipoPeriodo.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo Periodo "));
        	if(this.tipoDoc.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo Documento "));
        	if(this.tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
        	{
        		if(this.fechaIni.equals("") || this.fechaFin.equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","El Rango de Fechas "));
        		else
        		{
        			if(!UtilidadFecha.compararFechas(this.fechaFin.toString(), "00:00", this.fechaIni.toString(), "00:00").isTrue())
    					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.fechaIni.toString()+" mayor a la Fecha Final "+this.fechaFin.toString()+" "));
        		}
        	}
        	if(this.tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
        	{
        		if(this.anioIni == 0 || this.anioFin == 0)
        			errores.add("descripcion",new ActionMessage("errors.required","El Rango de Años "));
        	}
        	if(this.tipoSalida.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo Salida "));
        	if(!errores.isEmpty())
        		this.estado="empezar";
        }
        
        return errores;
    }
}