/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.lowagie.text.pdf.AcroFields;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.princetonsa.dto.odontologia.DtoDetallePaquetesOdontologicosConvenios;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicosConvenio;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosConvenioForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int registroSeleccionado;
	
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	private int codigoInstitucion;
	
	/**
	  * 
	  */
	 private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
	 
	 /**
	 * 
	 */
	private int contrato;
	
	
	/**
	 * 
	 */
	private int codigoConvenio;
	 
	/**
	 * 
	 */
	private String activo;
	 
	/**
	 * 
	 */
	boolean modificacion;
	
	

	public boolean isModificacion() {
		return modificacion;
	}


	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}


	/**
	 * 
	 */
	private DtoPaquetesOdontologicosConvenio paqueteConvenio;
	
	private int codigoPkDetalle;
	
	/**
	 * 
	 */
	private ArrayList<DtoPaquetesOdontologicos> paquetesOdontologicos;
	
	/**
	 * 
	 */
	private boolean esPorProgramas;
	
	/**
	 * USADO PARA MOSTRAR EL LA PAGINA DE DETALLE DEL PAQUETE
	 */
	private int codigoEsquema;
	
	/**
	 * USADO PARA MOSTRAR EL LA PAGINA DE DETALLE DEL PAQUETE
	 */
	private String descripcionEsquema;

	/**
	 * Codigopk del paquete que se quiere consultar en el resumen.
	 */
	private int codigoPaquete;
	
	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	
	/**
	 * 
	 */
	private String fechaFinalAnterior;
	
	/**
	 * 
	 */
	private boolean puedoModificar;
	
	/**
	 * Varible en la que se carga el detalle del paquete que se esta consultando.
	 */
	private DtoPaquetesOdontologicos paqueteOdontologico;
	
	/**
	 * tipo tarifario con el cual se estan consultando las tarifas.
	 */
	private int tipoTarifario;
	
	private ArrayList<DtoEsquemasTarifarios> esquemasTarifario;
	
	/***
	 * 
	 */
	private String patronOrdenar;
	/**
	 * 
	 */
	private String esDescendente;
	
	/**
	 * 
	 */
	private boolean esConsulta;
	
	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
	{
      ActionErrors errores = new ActionErrors();
      if(this.getEstado().equals("guardar"))
      {
    	  if(this.codigoPaquete<=0)
    	  {
    		  errores.add("", new ActionMessage("errors.required","El Paquete "));
    	  }
    	  if(this.codigoEsquema<=0)
    	  {
    		  errores.add("", new ActionMessage("errors.required","El Esquema tarifario "));
    	  }
    	  if((this.fechaInicial.trim().equals("")))
    	  {
    		  errores.add("fechaInicial", new ActionMessage("errors.required","La Fecha Inicial "));
    	  }
    	  if((this.fechaFinal.trim().equals("")))
    	  {
    		  errores.add("fechaFinal", new ActionMessage("errors.required","La Fecha Final "));
    	  }
    	  if(!this.fechaInicial.trim().equals("")&&!this.fechaFinal.trim().equals(""))
    	  {
    		  String fechaActual=UtilidadFecha.getFechaActual();
    		  boolean centinelaErrorFechaInicial=false;
    		  boolean centinelaErrorFechaFinal=false;
    		  if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
    		  {
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "La fecha Inicial "));
					centinelaErrorFechaInicial=true;				
    		  }
    		  if(!centinelaErrorFechaInicial)
    		  {
    			  if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaActual, this.fechaInicial))
        		  {
        			  errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "La fecha Inicial "+this.fechaInicial, "La fecha Actual "+fechaActual));
        		  }
    		  }
    		  if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
    		  {
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "La fecha Final "));
					centinelaErrorFechaFinal=true;				
    		  }
    		  if(!centinelaErrorFechaInicial&&!centinelaErrorFechaFinal)
    		  {
    			  if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
        		  {
        			  errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "La fecha Final "+this.fechaFinal, "La fecha Inicial "+this.fechaInicial));
        		  }
    			  if(modificacion&&!puedoModificar)
    			  {
    				  if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinalAnterior,this.fechaFinal))
            		  {
            			  errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "La fecha Final "+this.fechaFinal, "La fecha Final Anterior "+this.fechaFinalAnterior));
            		  }
    			  }
    			  
    		  }
    	  }
    	  //si no se han presentado errores, verificar traslape de fecha.
    	  if(errores.isEmpty())
    	  {
    		  for(DtoDetallePaquetesOdontologicosConvenios det:this.getPaqueteConvenio().getDetallePaquete())
    		  {
    			  if(this.getCodigoPaquete()==det.getPaquete().getCodigoPk())
    			  {
    				  if(det.getCodigoPk()!=this.getCodigoPkDetalle())
    				  {
	    				  if(det.getActivo()!=null && det.getActivo().equals(ConstantesBD.acronimoSi))
	    				  {
		    				  if(UtilidadFecha.existeTraslapeEntreFechas(this.fechaInicial, this.fechaFinal, UtilidadFecha.conversionFormatoFechaAAp(det.getFechaIncial()), UtilidadFecha.conversionFormatoFechaAAp(det.getFechaFinal())))
		    				  {
		    					  errores.add("", new ActionMessage("error.rangoFechasInvalido",this.fechaInicial,this.fechaFinal,det.getFechaIncial(),det.getFechaFinal()+" del mismo paquete ya parametrizado."));
		    				  }
	    				  }
    				  }
    			  }
    		  }
    	  }
    	  //existeTraslapeEntreFechas
    	  if(!errores.isEmpty())
    	  {
    		  this.estado="registroNuevo";
    	  }
    	 
      }
      return errores;
    }
	

	/**
	 * 
	 */
	public void reset()
	{
		this.registroSeleccionado=ConstantesBD.codigoNuncaValido;
		this.mensaje=new ResultadoBoolean(false);
		this.paqueteConvenio=new DtoPaquetesOdontologicosConvenio();
		this.convenios=new ArrayList();
		this.contratos=new ArrayList();
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.paqueteOdontologico=new DtoPaquetesOdontologicos();
		this.codigoEsquema=ConstantesBD.codigoNuncaValido;
		this.descripcionEsquema="";
		this.codigoPaquete=ConstantesBD.codigoNuncaValido;
		this.tipoTarifario=ConstantesBD.codigoNuncaValido;
		this.fechaInicial="";
		this.fechaFinal="";
		this.paquetesOdontologicos=new ArrayList<DtoPaquetesOdontologicos>();
		this.esquemasTarifario=new ArrayList<DtoEsquemasTarifarios>();
		this.patronOrdenar = "";
		this.esDescendente="";
		this.activo=ConstantesBD.acronimoSi;
		this.modificacion=false;
		this.codigoPkDetalle=ConstantesBD.codigoNuncaValido;
		this.esConsulta=false;
	}
	
	public void resetNuevoRegistro()
	{

		this.codigoPaquete=ConstantesBD.codigoNuncaValido;
		this.codigoEsquema=ConstantesBD.codigoNuncaValido;
		this.fechaInicial="";
		this.fechaFinal="";
		this.paquetesOdontologicos=new ArrayList<DtoPaquetesOdontologicos>();
		this.activo=ConstantesBD.acronimoSi;
		this.codigoPkDetalle=ConstantesBD.codigoNuncaValido;
	}


	public int getCodigoPkDetalle() {
		return codigoPkDetalle;
	}


	public void setCodigoPkDetalle(int codigoPkDetalle) {
		this.codigoPkDetalle = codigoPkDetalle;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public int getRegistroSeleccionado() {
		return registroSeleccionado;
	}


	public void setRegistroSeleccionado(int registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public DtoPaquetesOdontologicosConvenio getPaqueteConvenio() {
		return paqueteConvenio;
	}


	public void setPaqueteConvenio(DtoPaquetesOdontologicosConvenio paqueteConvenio) {
		this.paqueteConvenio = paqueteConvenio;
	}


	public ArrayList getConvenios() {
		return convenios;
	}


	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}


	public ArrayList getContratos() {
		return contratos;
	}


	public void setContratos(ArrayList contratos) {
		this.contratos = contratos;
	}


	public int getContrato() {
		return contrato;
	}


	public void setContrato(int contrato) {
		this.contrato = contrato;
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public DtoPaquetesOdontologicos getPaqueteOdontologico() {
		return paqueteOdontologico;
	}


	public void setPaqueteOdontologico(DtoPaquetesOdontologicos paqueteOdontologico) {
		this.paqueteOdontologico = paqueteOdontologico;
	}


	public boolean isEsPorProgramas() {
		return esPorProgramas;
	}


	public void setEsPorProgramas(boolean esPorProgramas) {
		this.esPorProgramas = esPorProgramas;
	}


	public int getCodigoEsquema() {
		return codigoEsquema;
	}


	public void setCodigoEsquema(int codigoEsquema) {
		this.codigoEsquema = codigoEsquema;
	}


	public String getDescripcionEsquema() {
		return descripcionEsquema;
	}


	public void setDescripcionEsquema(String descripcionEsquema) {
		this.descripcionEsquema = descripcionEsquema;
	}


	public int getCodigoPaquete() {
		return codigoPaquete;
	}


	public void setCodigoPaquete(int codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}


	public int getTipoTarifario() {
		return tipoTarifario;
	}


	public void setTipoTarifario(int tipoTarifario) {
		this.tipoTarifario = tipoTarifario;
	}


	public String getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public String getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public ArrayList<DtoPaquetesOdontologicos> getPaquetesOdontologicos() {
		return paquetesOdontologicos;
	}


	public void setPaquetesOdontologicos(
			ArrayList<DtoPaquetesOdontologicos> paquetesOdontologicos) {
		this.paquetesOdontologicos = paquetesOdontologicos;
	}


	public ArrayList<DtoEsquemasTarifarios> getEsquemasTarifario() {
		return esquemasTarifario;
	}


	public void setEsquemasTarifario(
			ArrayList<DtoEsquemasTarifarios> esquemasTarifario) {
		this.esquemasTarifario = esquemasTarifario;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getEsDescendente() {
		return esDescendente;
	}


	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}
	
	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}


	public String getFechaFinalAnterior() {
		return fechaFinalAnterior;
	}


	public void setFechaFinalAnterior(String fechaFinalAnterior) {
		this.fechaFinalAnterior = fechaFinalAnterior;
	}


	public boolean isPuedoModificar() {
		return puedoModificar;
	}


	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}


	public boolean isEsConsulta() {
		return esConsulta;
	}


	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

}
