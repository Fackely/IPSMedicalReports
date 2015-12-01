
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
/**
 * 
 * Clase que implementa los atributos para
 * comunicar la forma con el WorkFlow, y el validate
 * de la funcionalidad
 *
 * @version 1.0, 25/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaImpresionTrasladosForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * almacena los atributos con sus respectivos
     * valore para realizar la consulta
     */
    private HashMap mapaAtributosBusqueda;
    /**
     * almacena el listado de traslados almacén
     * despues de realizar la busqueda
     */
    private HashMap mapaListadoTraslados;
    /**
     * patron por el cual se ordena el mapa
     * de listado traslados
     */
    private String patronOrdenar;
    /**
     * registro seleccionado del mpa de traslados
     * para consultar el detalle
     */
    private int regSeleccionado;
    /**
     * estado del listado de traslados
     */
    private String mostrarListado;
    /**
     * ultimo patron por le cual se ordeno el 
     * mapa de traslados
     */
    private String ultimoPatron;
    
    /****************************************************
     * Modificado por anexo 632
     ****************************************************/
    
    private ArrayList<HashMap<String, Object>> claseInventario = new ArrayList<HashMap<String,Object>>();
    
    private boolean operacionTrue=false;
    
    private boolean existeArchivo=false;
    
    private String ruta="";
    
    private String urlArchivo="";
    
    /****************************************************
     * Fin Modificacion por anexo 632
     ****************************************************/
    
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

	
	/**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {   
    	this.mapaAtributosBusqueda= new HashMap ();
        this.patronOrdenar="";        
        this.ultimoPatron="";
        this.regSeleccionado=ConstantesBD.codigoNuncaValido;
        this.mapaAtributosBusqueda.put("codAlmacenDespacha",ConstantesBD.codigoNuncaValido+"");
        this.mapaAtributosBusqueda.put("nomAlmacenDespacha","Seleccione");
        this.mapaAtributosBusqueda.put("codAlmacenSolicita",ConstantesBD.codigoNuncaValido+"");
        this.mapaAtributosBusqueda.put("nomAlmacenSolicita","Seleccione");
        this.mapaAtributosBusqueda.put("noTrasladoInicial","");
        this.mapaAtributosBusqueda.put("noTrasladoFinal","");
        this.mapaAtributosBusqueda.put("fechaInicialSolicitud","");
        this.mapaAtributosBusqueda.put("fechaFinalSolicitud","");
        this.mapaAtributosBusqueda.put("fechaInicialDespacho","");
        this.mapaAtributosBusqueda.put("fechaFinalDespacho","");
        this.mapaAtributosBusqueda.put("usuarioSolicita","Seleccione");
        this.mapaAtributosBusqueda.put("usuarioDespacha","Seleccione");
        this.mapaAtributosBusqueda.put("codEstado",ConstantesBD.codigoNuncaValido+"");
        this.mapaAtributosBusqueda.put("nomEstado","Seleccione");
        this.mapaAtributosBusqueda.put("prioridad","");
        this.operacionTrue=false;
        this.existeArchivo=false;
        this.ruta="";
        this.urlArchivo="";
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


	/////////////////////////////////////////////////////////////////////////////////////////
//--------------------------------------------------------------------------------------
    /****************************************************
     * Modificado por anexo 632
     ****************************************************/
    public ArrayList<HashMap<String, Object>> getClaseInventario() {
		return claseInventario;
	}
	public void setClaseInventario(
			ArrayList<HashMap<String, Object>> claseInventario) {
		this.claseInventario = claseInventario;
	}
	 /****************************************************
     * Fin Modificacion por anexo 632
     ****************************************************/
//---------------------------------------------------------------------------------------	
//////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void resetListados()
    {
        this.mapaListadoTraslados=new HashMap();  
        this.mostrarListado="";
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
        boolean existeErrorFecha1=false,existeErrorFecha2=false;
        if(this.estado.equals("generarBusqueda"))
        {
        	/**El rango final debe ser mayor al rango inicial*/
            if(!(this.mapaAtributosBusqueda.get("noTrasladoInicial")+"").equals("") && !(this.mapaAtributosBusqueda.get("noTrasladoFinal")+"").equals(""))
                if(Integer.parseInt((this.mapaAtributosBusqueda.get("noTrasladoFinal")+""))<Integer.parseInt((this.mapaAtributosBusqueda.get("noTrasladoInicial")+"")))
                    errores.add("rango final menor", new ActionMessage("errors.rangoMayorIgual","RANGO FINAL","AL RANGO INICIAL"));
          
            /**Validación del formato de la fecha solicitud inicial, y debe ser menor a la del sistema*/
            if(!(this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+"").equals(""))
            {           
                if(!UtilidadFecha.validarFecha((this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+"")))
                {
                    errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de solicitud inicial"));
                    existeErrorFecha1=true;
                }
                if(!existeErrorFecha1)
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+""),UtilidadFecha.getFechaActual()))
                    {
                        errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de solicitud inicial "+(this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+""),UtilidadFecha.getFechaActual()));
                    }
            }
            /**Validación del formato de la fecha solicitud final, y debe ser menor a la del sistema*/
            if(!(this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+"").equals(""))
            {                  
                if(!UtilidadFecha.validarFecha((this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+"")))
                {
                    errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de solicitud final"));
                    existeErrorFecha2=true;
                }
                if(!existeErrorFecha2)
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+""),UtilidadFecha.getFechaActual()))
                    {
                        errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de solicitud final "+(this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+""),UtilidadFecha.getFechaActual()));
                    }
            }
            /**La fecha solicitud final debe ser mayor a la fecha solicitud inicial*/
            if(!existeErrorFecha1 && !existeErrorFecha2)
            {
                if(!(this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+"").equals("") && !(this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+"").equals(""))
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+""),(this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+"")))
                        errores.add("fecha anterior", new ActionMessage("errors.fechaAnteriorIgualActual","de solicitud final "+(this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+""),(this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+"")));
            }           
            existeErrorFecha1=false;existeErrorFecha2=false;
            /**Validación del formato de la fecha despacho inicial, y debe ser menor a la del sistema*/
            if(!(this.mapaAtributosBusqueda.get("fechaInicialDespacho")+"").equals(""))
            {           
                if(!UtilidadFecha.validarFecha((this.mapaAtributosBusqueda.get("fechaInicialDespacho")+"")))
                {
                    errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de despacho inicial"));
                    existeErrorFecha1=true;
                }
                if(!existeErrorFecha1)
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaInicialDespacho")+""),UtilidadFecha.getFechaActual()))
                    {
                        errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de despacho inicial "+(this.mapaAtributosBusqueda.get("fechaInicialDespacho")+""),UtilidadFecha.getFechaActual()));
                    }
            }
            /**Validación del formato de la fecha despacho final, y debe ser menor a la del sistema*/
            if(!(this.mapaAtributosBusqueda.get("fechaFinalDespacho")+"").equals(""))
            {                  
                if(!UtilidadFecha.validarFecha((this.mapaAtributosBusqueda.get("fechaFinalDespacho")+"")))
                {
                    errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de despacho final"));
                    existeErrorFecha2=true;
                }
                if(!existeErrorFecha2)
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaFinalDespacho")+""),UtilidadFecha.getFechaActual()))
                    {
                        errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de despacho final "+(this.mapaAtributosBusqueda.get("fechaFinalDespacho")+""),UtilidadFecha.getFechaActual()));
                    }
            }
            /**La fecha despacho final debe ser mayor a la fecha despacho inicial*/
            if(!existeErrorFecha1 && !existeErrorFecha2)
            {
                if(!(this.mapaAtributosBusqueda.get("fechaInicialDespacho")+"").equals("") && !(this.mapaAtributosBusqueda.get("fechaFinalDespacho")+"").equals(""))
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((this.mapaAtributosBusqueda.get("fechaInicialDespacho")+""),(this.mapaAtributosBusqueda.get("fechaFinalDespacho")+"")))
                        errores.add("fecha anterior", new ActionMessage("errors.fechaAnteriorIgualActual","de despacho final "+(this.mapaAtributosBusqueda.get("fechaFinalDespacho")+""),(this.mapaAtributosBusqueda.get("fechaInicialDespacho")+"")));
            }
            /**Es requerido el ingreso de un parametro para realizar la busqueda*/
            if(
                    (this.mapaAtributosBusqueda.get("noTrasladoInicial")+"").equals("") && (this.mapaAtributosBusqueda.get("noTrasladoFinal")+"").equals("")
                 && (this.mapaAtributosBusqueda.get("fechaInicialSolicitud")+"").equals("") && (this.mapaAtributosBusqueda.get("fechaFinalSolicitud")+"").equals("")
                 && (this.mapaAtributosBusqueda.get("fechaInicialDespacho")+"").equals("") && (this.mapaAtributosBusqueda.get("fechaFinalDespacho")+"").equals("")
                 && (this.mapaAtributosBusqueda.get("codAlmacenDespacha")+"").equals(ConstantesBD.codigoNuncaValido+"") && (this.mapaAtributosBusqueda.get("codAlmacenSolicita")+"").equals(ConstantesBD.codigoNuncaValido+"")
                 && (this.mapaAtributosBusqueda.get("usuarioSolicita")+"").equals("Seleccione") && (this.mapaAtributosBusqueda.get("usuarioDespacha")+"").equals("Seleccione")
                 && (this.mapaAtributosBusqueda.get("codEstado")+"").equals(ConstantesBD.codigoNuncaValido+"") && (this.mapaAtributosBusqueda.get("prioridad")+"").equals("")
                 && !UtilidadCadena.noEsVacio(this.mapaAtributosBusqueda.get("articulo")+"") && (this.mapaAtributosBusqueda.get("claseInventario")+"").equals(ConstantesBD.codigoNuncaValido+"")  
            
            )
                errores.add("minimo un parametro", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta","UN"));
        }
        else
        	if (this.estado.equals("generar"))
        	{
        		if (!UtilidadCadena.noEsVacio(this.mapaAtributosBusqueda.get("tipoSalida")+"") || (this.mapaAtributosBusqueda.get("tipoSalida")+"").equals(ConstantesBD.codigoNuncaValido+""))
        			errores.add("descripcion",new ActionMessage("errors.required","El tipo de salida "));
        		
        		if (!UtilidadCadena.noEsVacio(this.mapaAtributosBusqueda.get("tipoReporte")+"") || (this.mapaAtributosBusqueda.get("tipoReporte")+"").equals(ConstantesBD.codigoNuncaValido+""))
        			errores.add("descripcion",new ActionMessage("errors.required","El tipo de reporte "));
        		
        		if (!UtilidadCadena.noEsVacio(this.mapaAtributosBusqueda.get("tipoCodigoArticulo")+"") || (this.mapaAtributosBusqueda.get("tipoCodigoArticulo")+"").equals(ConstantesBD.codigoNuncaValido+""))
        			errores.add("descripcion",new ActionMessage("errors.required","El tipo de codigo articulo "));
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
     * @return Retorna mapaAtributosBusqueda.
     */
    public HashMap getMapaAtributosBusqueda() {
        return mapaAtributosBusqueda;
    }
    /**
     * @param mapaAtributosBusqueda Asigna mapaAtributosBusqueda.
     */
    public void setMapaAtributosBusqueda(HashMap mapaAtributosBusqueda) {
        this.mapaAtributosBusqueda = mapaAtributosBusqueda;
    }
    /**
     * @return Retorna mapaAtributosBusqueda.
     */
    public Object getMapaAtributosBusqueda(String key) {
        return mapaAtributosBusqueda.get(key);
    }
    /**
     * @param mapaAtributosBusqueda Asigna mapaAtributosBusqueda.
     */
    public void setMapaAtributosBusqueda(String key,Object value) {
        this.mapaAtributosBusqueda.put(key, value);
    }
    /**
     * @return Retorna mapaListadoTraslados.
     */
    public HashMap getMapaListadoTraslados() {
        return mapaListadoTraslados;
    }
    /**
     * @param mapaListadoTraslados Asigna mapaListadoTraslados.
     */
    public void setMapaListadoTraslados(HashMap mapaListadoTraslados) {
        this.mapaListadoTraslados = mapaListadoTraslados;
    }
    /**
     * @return Retorna mapaListadoTraslados.
     */
    public Object getMapaListadoTraslados(String key) {
        return mapaListadoTraslados.get(key);
    }
    /**
     * @param mapaListadoTraslados Asigna mapaListadoTraslados.
     */
    public void setMapaListadoTraslados(String key,Object value) {
        this.mapaListadoTraslados.put(key, value);
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
     * @return Retorna mostrarListado.
     */
    public String getMostrarListado() {
        return mostrarListado;
    }
    /**
     * @param mostrarListado Asigna mostrarListado.
     */
    public void setMostrarListado(String mostrarListado) {
        this.mostrarListado = mostrarListado;
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
}
