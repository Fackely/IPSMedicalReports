
/*
 * Creado   1/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.cartera;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;

/** 
 * Form que contiene todos los datos específicos 
 * para interactuar con aprobacion pagos cartera
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionPagosCarteraForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * tipo de documento correspondiente a la
     * aplicacion de pago
     */
    private int tipo;
    /**
     * número del documento
     */
    private String documento;
    /**
     * consecutivo de la aplicación de pago para consulta
     */
    private String numeroAplicacionPago;
    /**
     * código del convenio
     */
    private int codConvenio;
    /**
     * mapa que almacena los datos de la
     * aplicación de pagos
     */
    private HashMap mapaApliPagos;
    /**
     * almacena el número del registro seleccionado
     * de aplicación de pagos para consultar el 
     * detalle
     */
    private int regSeleccionado;
    /**
     * almacena el código de la aplicación
     * de pago para el registro seleccionado
     */
    private int codAplicacionPago;
    /**
     * almacena la fecha de aprobacion
     */
    private String fechaAprobacion;
    /**
     * almacena el indicativo del tipo
     * de funcionalidad aprobacion o
     * consulta
     */
    private String tipoFuncionalidad;
    /**
     * inicializar atributos de esta forma     
     */
    
    /**
     * Cambio por Tarea 88494
     */
    private String estadoPago;
    
    public void reset ()
    {         
        this.documento="";
        this.numeroAplicacionPago="";
        this.tipo=ConstantesBD.codigoNuncaValido;
        this.codConvenio=ConstantesBD.codigoNuncaValido;
        this.mapaApliPagos=new HashMap();
        this.regSeleccionado=ConstantesBD.codigoNuncaValido;
        this.codAplicacionPago=ConstantesBD.codigoNuncaValido;
        this.fechaAprobacion=UtilidadFecha.getFechaActual();
        this.tipoFuncionalidad="";
        this.estadoPago="";
        
    }
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("generarConsulta"))
		{
		  if(this.documento.equals("")&&this.numeroAplicacionPago.equals("")&&this.tipo==ConstantesBD.codigoNuncaValido
		          &&this.codConvenio==ConstantesBD.codigoNuncaValido&&this.estadoPago.equals(""))
		  {
		      errores.add("minimo un parametro", new ActionMessage("error.cierre.minimoUnFiltroRequerido"));
		  }		  
		}
		if(estado.equals("generarAprobacion"))
		{
		  String fechaAplicacion=UtilidadFecha.conversionFormatoFechaAAp(this.mapaApliPagos.get("fecha_aplicacion_"+this.regSeleccionado)+"");		  
		  if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaAprobacion,fechaAplicacion))
		  {
		      errores.add("minimo un parametro", new ActionMessage("errors.fechaAnteriorIgualActual","Aprobacion","Aplicación"));
		  }
		  if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAprobacion,UtilidadFecha.getFechaActual()))
		  {
		      errores.add("minimo un parametro", new ActionMessage("errors.fechaPosteriorIgualActual","Aprobacion","Sistema"));
		  }
		}
		return errores;
	}
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna codConvenio.
     */
    public int getCodConvenio() {
        return codConvenio;
    }
    /**
     * @param codConvenio Asigna codConvenio.
     */
    public void setCodConvenio(int codConvenio) {
        this.codConvenio = codConvenio;
    }
    /**
     * @return Retorna documento.
     */
    public String getDocumento() {
        return documento;
    }
    /**
     * @param documento Asigna documento.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    /**
     * @return Retorna numeroAplicacionPago.
     */
    public String getNumeroAplicacionPago() {
        return numeroAplicacionPago;
    }
    /**
     * @param numeroAplicacionPago Asigna numeroAplicacionPago.
     */
    public void setNumeroAplicacionPago(String numeroAplicacionPago) {
        this.numeroAplicacionPago = numeroAplicacionPago;
    }
    /**
     * @return Retorna tipo.
     */
    public int getTipo() {
        return tipo;
    }
    /**
     * @param tipo Asigna tipo.
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    /**
     * @return Retorna mapaApliPagos.
     */
    public HashMap getMapaApliPagos() {
        return mapaApliPagos;
    }    
    /**
     * @return Retorna mapaApliPagos.
     */
    public Object getMapaApliPagos(String key) {
        return mapaApliPagos.get(key);
    }
    /**
     * @param mapaApliPagos Asigna mapaApliPagos.
     */
    public void setMapaApliPagos(HashMap mapaApliPagos) {
        this.mapaApliPagos = mapaApliPagos;
    }
    /**
     * @param mapaApliPagos Asigna mapaApliPagos.
     */
    public void setMapaApliPagos(String key,Object value) {
        this.mapaApliPagos.put(key, value);
    }
    /**
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) {
        this.regSeleccionado = regSeleccionado;
    }
    /**
     * @return Retorna codAplicacionPago.
     */
    public int getCodAplicacionPago() {
        return codAplicacionPago;
    }
    /**
     * @param codAplicacionPago Asigna codAplicacionPago.
     */
    public void setCodAplicacionPago(int codAplicacionPago) {
        this.codAplicacionPago = codAplicacionPago;
    }
    /**
     * @return Retorna fechaAprobacion.
     */
    public String getFechaAprobacion() {
        return fechaAprobacion;
    }
    /**
     * @param fechaAprobacion Asigna fechaAprobacion.
     */
    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    /**
     * @return Retorna tipoFuncionalidad.
     */
    public String getTipoFuncionalidad() {
        return tipoFuncionalidad;
    }
    /**
     * @param tipoFuncionalidad Asigna tipoFuncionalidad.
     */
    public void setTipoFuncionalidad(String tipoFuncionalidad) {
        this.tipoFuncionalidad = tipoFuncionalidad;
    }
	public String getEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}
}
