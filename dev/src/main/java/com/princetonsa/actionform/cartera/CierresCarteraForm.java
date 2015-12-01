
/*
 * Creado   14/07/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.cartera;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadFecha;
import util.Utilidades;


/**
 * Form que contiene todos los datos específicos 
 * para interactuar con cierre de saldo inicial cartera
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * @version 1.0, 14/07/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class CierresCarteraForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    
    /**
     * año del cierre
     */
    private String yearCierre;
    
    /**
     * mes del cierre
     */
    private String mesCierre;
    
    /**
     * observaciones del cierre
     */
    private String observaciones;
    
    /**
     * estado para verificar si existen facturas
     * para generar cierre en 0 o diferente de 0     * 
     */
    private boolean existenFacturasParaCierre;
    
    /**
     * fecha de generacion de el cierre
     */
    private String fechaGeneracion;
    /**
     * hora de generacion del cierre
     */
    private String horaGeneracion;
    /**
     * usuario que gero el cierre
     */
    private String usuario;
    
    /**
     * estado para saber si el cierre es
     * cancelado.
     */
    private boolean cancelarCierre;
        
    /**
     * listado de las facturas que poseen pagos
     * sin aprobar y/o ajustes
     */
    private ArrayList factConPagosPendientesOAjustes;
    
    /**
     * contador de las facturas que poseen
     * cuenta de cobro.
     */
    private int contFactConCuentaCobro;
    
    /**
     * almacena el tipo de cierre que se esta
     * ejecutando
     */
    private String tipoCierre;
    
    /**
     * para identificar el cierre que se esta ejecutando
     * Anula, Mensual ó Inicial
     */
    private String accion="";
    
    /**
     * true si el mes ha cerrar ya ha sido generado
     */
    private boolean mesCierreYaGenerado;
    
    /**
     * almacena la lista de meses de cierre mensuales
     */
    private ArrayList cierresMensualesGenerados;
    
    /**
     * código del convenio, para la busqueda avanzada
     */
    private int codConvenio;   
    
    /**
     * código de la empresa
     */
    private int codEmpresa;
    
    /**
     * almacena toda la informacion de los
     * cierres para la busqueda avanzada
     */
    private HashMap cierres;
    
    /**
     * Codigos de los convenios, en formato(codigo-codigo, etc)
     */
    private String codigoConvenioStr;
    
    /**
     * patron por el cual se ordenara
     */
    private String patronOrdenar;
    
    /**
     * ultimo patron por el que se ordeno
     */
    private String ultimoPatron;
    
    /**
     * almacena le numero de registros del hashmap
     * cuando se realiza consulta
     */
    private int numRegHashMap;
    
    /**
     * mapa para almacenar datos tempe¡orales
     */
    private HashMap mapaTemp;
    
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {
      this.yearCierre = "";
      this.mesCierre = "";
      this.observaciones = "";
      this.existenFacturasParaCierre=true;
      this.fechaGeneracion = "";
      this.horaGeneracion = "";
      this.usuario = "";
      this.cancelarCierre = false;      
      this.factConPagosPendientesOAjustes = new ArrayList ();
      this.contFactConCuentaCobro = 0;
      this.tipoCierre = "";        
      this.mesCierreYaGenerado=false;
      this.cierresMensualesGenerados = new ArrayList();
      this.codConvenio=-1;
      this.codEmpresa=-1;
      this.cierres = new HashMap();
      this.codigoConvenioStr="";
      this.patronOrdenar="";
      this.ultimoPatron="";
      this.numRegHashMap=0;
      this.mapaTemp=new HashMap ();
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
		String fechaActual = UtilidadFecha.getFechaActual();
	    String fecha = "31/"+this.getMesCierre()+"/"+this.getYearCierre();
	    
		if(this.estado.equals("generarCierre") && this.accion.equals("cierreInicial"))
		{		   
	        if(UtilidadFecha.compararFechas(fecha, "00:00", fechaActual, "00:00").isTrue())
	        {
	            errores.add("fecha corte", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Cierre", "Actual - VERIFICAR EN PARAMETROS"));  
	        }                
		}
		if(this.estado.equals("generarCierre") && this.accion.equals("cierreMensual"))
		{
			if(this.yearCierre.equals(""))
			{
				 errores.add("fecha corte", new ActionMessage("error.cierre.anioCierreInvalido"));
			}
			if(this.mesCierre.equals(""))
			{
				errores.add("mes corte", new ActionMessage("error.cierre.mesCierreInvalido"));
			}
			else if(UtilidadFecha.compararFechas(fecha, "00:00", fechaActual, "00:00").isTrue())
            {
				errores.add("fecha corte", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Cierre Mensual", "Actual"));  
            }
			if(errores.isEmpty())
			{
				if(Utilidades.convertirAEntero(this.yearCierre)<1970)
				{
					errores.add("errors.fechaAnteriorAOtraDeReferencia",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Cierre", "1970"));
				}
				if(Utilidades.convertirAEntero(this.mesCierre)<1 || Utilidades.convertirAEntero(this.mesCierre)>12)
				{
					errores.add("errors.range",new ActionMessage("errors.range","El mes cierre", "1", "12"));
				}
			}
		}
		if(this.estado.equals("generarCierre") && this.accion.equals("cierreAnual"))
		{
			if(this.getYearCierre().equals(""))
            {
                errores.add("fecha corte", new ActionMessage("error.cierre.anioCierreInvalido"));  
            }
			else if(Integer.parseInt(this.getYearCierre()) > UtilidadFecha.getMesAnioDiaActual("anio"))
            {
                errores.add("fecha corte", new ActionMessage("error.cierre.anioCierreInvalido"));  
            }                
        
		}
		if(this.estado.equals("consultar"))
		{
		    if(!this.yearCierre.equals(""))
		    {
		        if(Integer.parseInt(this.yearCierre)>UtilidadFecha.getMesAnioDiaActual("anio"))
		        {
		            errores.add("anio corte", new ActionMessage("error.cierre.anioCierreInvalido")); 
		        }
		    }
		    if(!this.mesCierre.equals(""))
		    {
		        if(Integer.parseInt(this.yearCierre)==UtilidadFecha.getMesAnioDiaActual("anio")&&Integer.parseInt(this.mesCierre)>=UtilidadFecha.getMesAnioDiaActual("mes"))
		        {
		            errores.add("mes corte", new ActionMessage("error.cierre.mesCierreInvalido")); 
		        } 
		    }
		    if(this.codEmpresa==-1 && this.codConvenio==-1 && this.yearCierre.equals("") && this.mesCierre.equals(""))
		    {
		        errores.add("falta parametro", new ActionMessage("error.cierre.minimoUnFiltroRequerido")); 
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
     * @return Retorna mesCierre.
     */
    public String getMesCierre() {
        return mesCierre;
    }
    /**
     * @param mesCierre Asigna mesCierre.
     */
    public void setMesCierre(String mesCierre) 
    {
    	if(!mesCierre.trim().equals(""))
    	{
    		if(Integer.parseInt(mesCierre)<10)
	    		this.mesCierre="0"+Integer.parseInt(mesCierre);
	    	else
	    		this.mesCierre = mesCierre;
    	}
    	else
    	{
    		this.mesCierre="";
    	}
    }
    /**
     * @return Retorna observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Retorna yearCierre.
     */
    public String getYearCierre() {
        return yearCierre;
    }
    /**
     * @param yearCierre Asigna yearCierre.
     */
    public void setYearCierre(String yearCierre) {
        this.yearCierre = yearCierre;
    }
    /**
     * @return Retorna existenFacturasParaCierre.
     */
    public boolean isExistenFacturasParaCierre() {
        return existenFacturasParaCierre;
    }
    /**
     * @param existenFacturasParaCierre Asigna existenFacturasParaCierre.
     */
    public void setExistenFacturasParaCierre(boolean existenFacturasParaCierre) {
        this.existenFacturasParaCierre = existenFacturasParaCierre;
    }
    /**
     * @return Retorna fechaGeneracion.
     */
    public String getFechaGeneracion() {
        return fechaGeneracion;
    }
    /**
     * @param fechaGeneracion Asigna fechaGeneracion.
     */
    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    /**
     * @return Retorna horaGeneracion.
     */
    public String getHoraGeneracion() {
        return horaGeneracion;
    }
    /**
     * @param horaGeneracion Asigna horaGeneracion.
     */
    public void setHoraGeneracion(String horaGeneracion) {
        this.horaGeneracion = horaGeneracion;
    }
    /**
     * @return Retorna usuario.
     */
    public String getUsuario() {
        return usuario;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * @return Retorna cancelarCierre.
     */
    public boolean getCancelarCierre() {
        return cancelarCierre;
    }
    /**
     * @param cancelarCierre Asigna cancelarCierre.
     */
    public void setCancelarCierre(boolean cancelarCierre) {
        this.cancelarCierre = cancelarCierre;
    }
        
    /**
     * @return Retorna contFactConCuentaCobro.
     */
    public int getContFactConCuentaCobro() {
        return contFactConCuentaCobro;
    }
    /**
     * @param contFactConCuentaCobro Asigna contFactConCuentaCobro.
     */
    public void setContFactConCuentaCobro(int contFactConCuentaCobro) {
        this.contFactConCuentaCobro = contFactConCuentaCobro;
    }
    /**
     * @return Retorna tipoCierre.
     */
    public String getTipoCierre() {
        return tipoCierre;
    }
    /**
     * @param tipoCierre Asigna tipoCierre.
     */
    public void setTipoCierre(String tipoCierre) {
        this.tipoCierre = tipoCierre;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Retorna mesCierreYaGenerado.
     */
    public boolean getMesCierreYaGenerado() {
        return mesCierreYaGenerado;
    }
    /**
     * @param mesCierreYaGenerado Asigna mesCierreYaGenerado.
     */
    public void setMesCierreYaGenerado(boolean mesCierreYaGenerado) {
        this.mesCierreYaGenerado = mesCierreYaGenerado;
    }
    /**
     * @return Retorna factConPagosPendientesOAjustes.
     */
    public ArrayList getFactConPagosPendientesOAjustes() {
        return factConPagosPendientesOAjustes;
    }
    /**
     * @param factConPagosPendientesOAjustes Asigna factConPagosPendientesOAjustes.
     */
    public void setFactConPagosPendientesOAjustes(
            ArrayList factConPagosPendientesOAjustes) {
        this.factConPagosPendientesOAjustes = factConPagosPendientesOAjustes;
    }
    /**
     * @return Retorna cierresMensualesGenerados.
     */
    public ArrayList getCierresMensualesGenerados() {
        return cierresMensualesGenerados;
    }
    /**
     * @param cierresMensualesGenerados Asigna cierresMensualesGenerados.
     */
    public void setCierresMensualesGenerados(ArrayList cierresMensualesGenerados) {
        this.cierresMensualesGenerados = cierresMensualesGenerados;
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
     * @return Retorna codEmpresa.
     */
    public int getCodEmpresa() {
        return codEmpresa;
    }
    /**
     * @param codEmpresa Asigna codEmpresa.
     */
    public void setCodEmpresa(int codEmpresa) {
        this.codEmpresa = codEmpresa;
    }
    /**
     * @return Retorna cierres.
     */
    public HashMap getCierres() {
        return cierres;
    }
    /**
     * @param cierres Asigna cierres.
     */
    public void setCierres(HashMap cierres) {
        this.cierres = cierres;
    }
    /**
     * @return Retorna cierres.
     */
    public Object getCierres(String key) {
        return cierres.get(key);
    }
    /**
     * @param cierres Asigna cierres.
     */
    public void setCierres(String key,Object value) {
        this.cierres.put(key,value);
    }
    /**
     * @return Retorna codigoConvenioStr.
     */
    public String getCodigoConvenioStr() {
        return codigoConvenioStr;
    }
    /**
     * @param codigoConvenioStr Asigna codigoConvenioStr.
     */
    public void setCodigoConvenioStr(String codigoConvenioStr) {
        this.codigoConvenioStr = codigoConvenioStr;
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
     * @return Retorna numRegHashMap.
     */
    public int getNumRegHashMap() {
        return numRegHashMap;
    }
    /**
     * @param numRegHashMap Asigna numRegHashMap.
     */
    public void setNumRegHashMap(int numRegHashMap) {
        this.numRegHashMap = numRegHashMap;
    }
    /**
     * @return Retorna mapaTemp.
     */
    public HashMap getMapaTemp() {
        return mapaTemp;
    }
    /**
     * @param mapaTemp Asigna mapaTemp.
     */
    public void setMapaTemp(HashMap mapaTemp) {
        this.mapaTemp = mapaTemp;
    }
    /**
     * @return Retorna mapaTemp.
     */
    public Object getMapaTemp(String key) {
        return mapaTemp.get(key);
    }
    /**
     * @param mapaTemp Asigna mapaTemp.
     */
    public void setMapaTemp(String key, Object value) {
        this.mapaTemp.put(key,value);
    }
}
