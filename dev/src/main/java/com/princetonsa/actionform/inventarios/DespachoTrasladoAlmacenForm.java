
/*
 * Creado   23/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

/**
 * Clase que implementa los atributos para
 * comunicar la forma con el WorkFlow, y el validate
 * de la funcionalidad
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class DespachoTrasladoAlmacenForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * mapa que contine el listado de traslados
     * almacenes
     */
    private HashMap mapaTraslados;
    /**
     * posición del registro seleccionado
     * de las solicitudes
     */
    private int regSeleccionado;
    /**
     * patron por el cual se ordena el listado
     * de las solicitudes
     */
    private String patronOrdenar;
    /**
     * ultimo patron por el cual se ordeno
     * el listado de solicitudes
     */
    private String ultimoPatron;
    /**
     * contine el listado del detalle de lasolicitud
     */
    private HashMap mapaDetalleSolicitud;
    /**
     * almacena el estado del despacho 
     */
    private String esDespachar;
    /**
     * almacena mensajes al usuario
     */
    private HashMap warnings;
    
    /**
	 * 
	 */
	private boolean interfazCompras;
	
	/**
	 * 
	 */
	private HashMap<String, Object> almacenesConsignacion;
	
	/**
	 * 
	 */
	private HashMap<String, Object> conveniosProveedor;
	
	/**
	 * 
	 */
	private HashMap<String, Object> proveedorCatalogo;
	
	
    /**
     * inicializar atributos de esta forma     
     */
    public void reset (int codigoInstitucion)
    {   
       this.mapaTraslados=new HashMap(); 
       this.regSeleccionado=ConstantesBD.codigoNuncaValido;
       this.patronOrdenar="";
       this.ultimoPatron="";
       this.mapaDetalleSolicitud=new HashMap();
       this.esDespachar="";
       this.warnings=new HashMap();
       
       this.interfazCompras=UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(codigoInstitucion));
       this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(codigoInstitucion);
       this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(codigoInstitucion);
       this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(codigoInstitucion);
       
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
	    if(this.estado.equals("generarDespacho"))
	    {
	        boolean existeRegistroMayorACero=false;
	        for(int k=0;k<Integer.parseInt(this.mapaDetalleSolicitud.get("numRegistros")+"");k++)
	        {
	        	try
	            {
		        	if(Integer.parseInt(this.mapaDetalleSolicitud.get("cantidaddespachada_"+k)+"")>=0)
		            {
		                existeRegistroMayorACero=true;
		               
		               //tareas id=280316 y id=101437
		               //Al ingresar un valor en el campo Cant. Desp se debe validar que el valor sea menor o igual al valor registrado en Cant. Solic
		               if(Integer.parseInt(this.mapaDetalleSolicitud.get("cantidadsolicitada_"+k)+"")<Integer.parseInt(this.mapaDetalleSolicitud.get("cantidaddespachada_"+k)+""))
		            	   errores.add("despacho > solicitado.", new ActionMessage("errors.integerMenorIgualQue","La cantidad despachada del registros "+(k+1),"la cantidad solicitada."));
		            }
	            }
	            catch (Exception e) 
	            {
	            	//errores.add("Numero invalido",new ActionMessage("errors.invalid",this.mapaDetalleSolicitud.get("cantidaddespachada_"+k)));
	            	errores.add("Validación Cantidad ", new ActionMessage("errors.notEspecific","No se pueden dejar campos vacios"));
	            }
	            if(UtilidadTexto.getBoolean(this.mapaDetalleSolicitud.get("manejaLoteArticulo_"+k)+"") && (this.mapaDetalleSolicitud.get("lote_"+k)+"").trim().equals(""))
			    {
			      errores.add("falta campo", new ActionMessage("errors.required","El lote del Articulo "+this.mapaDetalleSolicitud.get("descripcion_"+k)));
			    }
			    if(UtilidadTexto.getBoolean(this.mapaDetalleSolicitud.get("manejaFechaVencimientoArticulo_"+k)+""))
			    {
			    	if((this.mapaDetalleSolicitud.get("fechavencimiento_"+k)+"").trim().equals(""))
				    {
				      errores.add("falta campo", new ActionMessage("errors.required","La fecha de Vencimiento del Articulo "+this.mapaDetalleSolicitud.get("descripcion_"+k)));
				    }
			    	else if(!UtilidadFecha.validarFecha(this.mapaDetalleSolicitud.get("fechavencimiento_"+k)+""))
				    {
			    		errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","La fecha de Vencimiento del Articulo "+this.mapaDetalleSolicitud.get("descripcion_"+k)));
				    }
			    }
			    if(this.isInterfazCompras())
			    {
			    	Utilidades.imprimirMapa(this.mapaTraslados);
			    	String tipoConSolicitante=(this.mapaTraslados.get("tipo_consignacion_solicitante_"+this.regSeleccionado)+"");
			    	String tipoConSolicitado=(this.mapaTraslados.get("tipo_consignacion_solicitado_"+this.regSeleccionado)+"");
			    	if(!tipoConSolicitado.equals(tipoConSolicitante))
			    	{
			    		if(UtilidadTexto.isEmpty(this.mapaDetalleSolicitud.get("proveedorconsignacion_"+k)+""))
				    	{
				    		errores.add("falta campo", new ActionMessage("errors.required","El Proveedor "));
				    	}
			    	}
			    }
	        }
	      
	        if(!existeRegistroMayorACero)
	        {
	            errores.add("definir cantidades > 0", new ActionMessage("error.inventarios.cantidadesDespachoInsuficientes"));	            
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
     * @return Retorna mapaTraslados.
     */
    public HashMap getMapaTraslados() {
        return mapaTraslados;
    }
    /**
     * @param mapaTraslados Asigna mapaTraslados.
     */
    public void setMapaTraslados(HashMap mapaTraslados) {
        this.mapaTraslados = mapaTraslados;
    }
    /**
     * @return Retorna mapaTraslados.
     */
    public Object getMapaTraslados(String key) {
        return mapaTraslados.get(key);
    }
    /**
     * @param mapaTraslados Asigna mapaTraslados.
     */
    public void setMapaTraslados(String key,Object value) {
        this.mapaTraslados.put(key, value);
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
     * @return Retorna patronOrdenar.
     */
    public String getPatronOrdenar() {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
     */
    public void setPatronOrdenar(String patronOrdenar) {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Retorna ultimoPatron.
     */
    public String getUltimoPatron() {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron Asigna ultimoPatron.
     */
    public void setUltimoPatron(String ultimoPatron) {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Retorna mapaDetalleSolicitud.
     */
    public Object getMapaDetalleSolicitud(String key) {
        return mapaDetalleSolicitud.get(key);
    }
    /**
     * @param mapaDetalleSolicitud Asigna mapaDetalleSolicitud.
     */
    public void setMapaDetalleSolicitud(String key,Object value) {
        this.mapaDetalleSolicitud.put(key, value) ;
    }
    /**
     * @return Retorna mapaDetalleSolicitud.
     */
    public HashMap getMapaDetalleSolicitud() {
        return mapaDetalleSolicitud;
    }
    /**
     * @param mapaDetalleSolicitud Asigna mapaDetalleSolicitud.
     */
    public void setMapaDetalleSolicitud(HashMap mapaDetalleSolicitud) {
        this.mapaDetalleSolicitud = mapaDetalleSolicitud;
    }
    /**
     * @return Retorna esDespachar.
     */
    public String getEsDespachar() {
        return esDespachar;
    }
    /**
     * @param esDespachar Asigna esDespachar.
     */
    public void setEsDespachar(String esDespachar) {
        this.esDespachar = esDespachar;
    }
    /**
     * @return Retorna warnings.
     */
    public HashMap getWarnings() {
        return warnings;
    }
    /**
     * @param warnings Asigna warnings.
     */
    public void setWarnings(HashMap warnings) {
        this.warnings = warnings;
    }
    /**
     * @return Retorna warnings.
     */
    public Object getWarnings(String key) {
        return warnings.get(key);
    }
    /**
     * @param warnings Asigna warnings.
     */
    public void setWarnings(String key,Object value) {
        this.warnings.put(key, value);
    }
	/**
	 * @return the almacenesConsignacion
	 */
	public HashMap<String, Object> getAlmacenesConsignacion() {
		return almacenesConsignacion;
	}
	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(
			HashMap<String, Object> almacenesConsignacion) {
		this.almacenesConsignacion = almacenesConsignacion;
	}
	/**
	 * @return the conveniosProveedor
	 */
	public HashMap<String, Object> getConveniosProveedor() {
		return conveniosProveedor;
	}
	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(HashMap<String, Object> conveniosProveedor) {
		this.conveniosProveedor = conveniosProveedor;
	}
	/**
	 * @return the interfazCompras
	 */
	public boolean isInterfazCompras() {
		return interfazCompras;
	}
	/**
	 * @param interfazCompras the interfazCompras to set
	 */
	public void setInterfazCompras(boolean interfazCompras) {
		this.interfazCompras = interfazCompras;
	}
	/**
	 * @return the proveedorCatalogo
	 */
	public HashMap<String, Object> getProveedorCatalogo() {
		return proveedorCatalogo;
	}
	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(HashMap<String, Object> proveedorCatalogo) {
		this.proveedorCatalogo = proveedorCatalogo;
	}

}
