
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;

/**
 * clase para manejar los atributos del 
 * formulario y el validate
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TiposTransaccionesInvForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * almacena los registros de 
     * tipos de transacciones
     */
    private HashMap mapaTipos;
    /**
     * almacena el link de la sig página
     */
    private String linkSiguiente;
    /**
     * para controlar la página actual
     * del pager.
     */
    private int offset;
    /**
     * código que sera eliminado
     */
    private String codigoRegEliminar;
    /**
     * códigos de los registroa a eliminar
     */
    private ArrayList registrosBDEliminar;
    /**
     * número de registros que seran eliminados
     */
    private int contRegistrosEliminar;
    /**
     * patron por el cual se ordena el mapa
     */
    private String patronOrdenar;
    /**
     * patron por el cual se ordena el mapa
     */
    private String ultimoPatron;
    /**
     * código del tipo de transaccion
     */
    private String codigo;
    /**
     * descripción del tipo de transacción
     */
    private String descripcion;
    /**
     * código del tipo de concepto
     */
    private int codConcepto;
    /**
     * código del tipo de costo
     */
    private int codCosto;
    /**
     * true o false
     */
    private String activo;
    /**
     * 
     */
    private String indicativo_consignacion;
    
    /**
     * Código de la interfaz
     */
    private String codigoInterfaz;
    
    private boolean operacionTrue=false;
    
    public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	/**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {    
        this.mapaTipos=new HashMap() ;
        this.linkSiguiente="";
        this.offset=0;     
        this.codigoRegEliminar="";
        this.registrosBDEliminar=new ArrayList();
        this.contRegistrosEliminar=0;
        this.patronOrdenar="";
        this.ultimoPatron="";
        this.activo="";
        this.indicativo_consignacion="";
        this.codigo="";
        this.descripcion="";
        this.codConcepto=ConstantesBD.codigoNuncaValido;
        this.codCosto=ConstantesBD.codigoNuncaValido;
        this.codigoInterfaz = "";
        this.operacionTrue=false;
    }
    /**inicializar mapa principal**/
    public void resetMapa ()
    {
        this.mapaTipos=new HashMap(); 
    }
    /**inicializar campos de busqueda**/
    public void resetCamposBusqueda()
    {
        this.activo="";
        this.indicativo_consignacion="";
        this.codigo="";
        this.descripcion="";
        this.codConcepto=ConstantesBD.codigoNuncaValido;
        this.codCosto=ConstantesBD.codigoNuncaValido;
    }
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	    int reg=0;   
	    ActionErrors errores = new ActionErrors();
	    Vector vectorCod=new Vector();
	    boolean existeCod=false;
	    if(estado.equals("guardar"))
	    {
	        for(int k=0;k<Integer.parseInt(this.mapaTipos.get("numRegistros")+"");k++)
	        {
	            if((this.mapaTipos.get("codigo_"+k)+"").equals(""))
	            {
	                reg = k;
		            reg ++;
		            errores.add("falta campo", new ActionMessage("errors.required","El código para el registro "+reg));  
	            }
	            if((this.mapaTipos.get("descripcion_"+k)+"").equals(""))
	            {
	                reg = k;
		            reg ++;
		            errores.add("falta campo", new ActionMessage("errors.required","La Descripción para el registro "+reg));  
	            }
	            if((this.mapaTipos.get("indicativo_consignacion_"+k)+"").equals(""))
	            {
	                reg = k;
		            reg ++;
		            errores.add("falta campo", new ActionMessage("errors.required","El Indicativo de Consignación para el registro "+reg));  
	            }
	            for(int j=0;j<Integer.parseInt(this.mapaTipos.get("numRegistros")+"");j++)
	            {	                
	                existeCod=validarCodigosExistentes(vectorCod,this.mapaTipos.get("codigo_"+k));
	                if(j!=k && !existeCod)
	                {
		                if((this.mapaTipos.get("codigo_"+k)+"").equals(this.mapaTipos.get("codigo_"+j)+""))
		                {
		                    errores.add("falta campo", new ActionMessage("errors.registroExistente","CODIGO "+this.mapaTipos.get("codigo_"+k)+""));
		                    vectorCod.addElement(this.mapaTipos.get("codigo_"+k));
		                }
	                }
	            }	            
	        }	        
	    }
	    return errores;
	}
	public boolean validarCodigosExistentes (Vector vectorCod,Object codigo)
	{
	    if(!vectorCod.isEmpty())
	    {
		    for(int i=0;i<vectorCod.size();i++)
	        {
	          if(codigo.equals(vectorCod.get(i)))  
	          {
	              i=vectorCod.size();
	              return true;	             
	          }	          
	        }
	    }
	    return false;  
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
     * @return Retorna mapaTipos.
     */
    public HashMap getMapaTipos() {
        return mapaTipos;
    }
    /**
     * @param mapaTipos Asigna mapaTipos.
     */
    public void setMapaTipos(HashMap mapaTipos) {
        this.mapaTipos = mapaTipos;
    }
    /**
     * @return Retorna mapaTipos.
     */
    public Object getMapaTipos(String key) {
        return mapaTipos.get(key);
    }
    /**
     * @param mapaTipos Asigna mapaTipos.
     */
    public void setMapaTipos(String key,Object value) {
        this.mapaTipos.put(key, value);
    }
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
    /**
     * @return Retorna offset.
     */
    public int getOffset() {
        return offset;
    }
    /**
     * @param offset Asigna offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return Retorna codigoRegEliminar.
     */
    public String getCodigoRegEliminar() {
        return codigoRegEliminar;
    }
    /**
     * @param codigoRegEliminar Asigna codigoRegEliminar.
     */
    public void setCodigoRegEliminar(String codigoRegEliminar) {
        this.codigoRegEliminar = codigoRegEliminar;
    }
    /**
     * @return Retorna registrosBDEliminar.
     */
    public ArrayList getRegistrosBDEliminar() {
        return registrosBDEliminar;
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(ArrayList registrosBDEliminar) {
        this.registrosBDEliminar = registrosBDEliminar;
    }
    /**
     * @return Retorna registrosBDEliminar.
     */
    public Object getRegistrosBDEliminar(int pos) {
        return registrosBDEliminar.get(pos);
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(int pos,Object value) {
        this.registrosBDEliminar.add(pos, value);
    }
    /**
     * @return Retorna contRegistrosEliminar.
     */
    public int getContRegistrosEliminar() {
        return contRegistrosEliminar;
    }
    /**
     * @param contRegistrosEliminar Asigna contRegistrosEliminar.
     */
    public void setContRegistrosEliminar(int contRegistrosEliminar) {
        this.contRegistrosEliminar = contRegistrosEliminar;
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
     * @return Retorna activo.
     */
    public String getActivo() {
        return activo;
    }
    /**
     * @param activo Asigna activo.
     */
    public void setActivo(String activo) {
        this.activo = activo;
    }
    /**
     * @return Retorna codConcepto.
     */
    public int getCodConcepto() {
        return codConcepto;
    }
    /**
     * @param codConcepto Asigna codConcepto.
     */
    public void setCodConcepto(int codConcepto) {
        this.codConcepto = codConcepto;
    }
    /**
     * @return Retorna codCosto.
     */
    public int getCodCosto() {
        return codCosto;
    }
    /**
     * @param codCosto Asigna codCosto.
     */
    public void setCodCosto(int codCosto) {
        this.codCosto = codCosto;
    }
    /**
     * @return Retorna codigo.
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * @param codigo Asigna codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Retorna descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion Asigna descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
	public String getIndicativo_consignacion() {
		return indicativo_consignacion;
	}
	public void setIndicativo_consignacion(String indicativo_consignacion) {
		this.indicativo_consignacion = indicativo_consignacion;
	}
	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}
	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
}
