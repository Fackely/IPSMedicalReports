
/*
 * Creado   23/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

/**
 * 
 *
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class UsuariosXAlmacenForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * almacena los registros de 
     * usuarios  por almacen
     */
    private HashMap mapaUXA;
    /**
     * código del almacén
     */
    private int codAlmacen;
    /**
     * nombre del almacén
     */
    private String nomAlmacen;
    /**
     * posición del registro
     */
    private int regEliminar;
    /**
     * códigos de los registros a
     * eliminar
     */
    private Vector registrosBDEliminar;
    /**
     * patron para ordenar
     */
    private String patronOrdenar;
    /**
     * ultimo patron para ordenar
     */
    private String ultimoPatron;
    
    /**
     * Número de registros por página 
     */
    private int maxPageItems;
    
    /**
     * URL utilizada para paginar
     */
    private String linkSiguiente;
    
    /**
     * Offset del pager
     */
    private int offset;
    
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {   
        this.mapaUXA=new HashMap();
        this.codAlmacen=ConstantesBD.codigoNuncaValido;
        this.nomAlmacen="Seleccione";
        this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.registrosBDEliminar=new Vector ();
        this.patronOrdenar="";
        this.ultimoPatron="";
        this.maxPageItems = 0;
        this.linkSiguiente = "";
        this.offset = 0;
        this.centroAtencion=ConstantesBD.codigoNuncaValido;
    }
    /**limpiar listados **/
    public void resetListados()
    {
        this.registrosBDEliminar=new Vector ();
        this.mapaUXA=new HashMap();
        this.regEliminar=ConstantesBD.codigoNuncaValido;
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
	    int reg=0;
	    Vector vectorCod=new Vector();
	    boolean existeCod=false;
	    if(estado.equals("guardar"))
        {		        
	        for(int k=0;k<Integer.parseInt(this.mapaUXA.get("numRegistros")+"");k++)
	        {
	            if((this.mapaUXA.get("login_usuario_"+k)+"").equals("Seleccione"))
	            {
	                reg = k;
		            reg ++;
		            errores.add("falta campo", new ActionMessage("errors.required","El login para el registro "+reg));  
	            }
	            for(int j=0;j<Integer.parseInt(this.mapaUXA.get("numRegistros")+"");j++)
	            {	                
	        	    existeCod=validarCodigosExistentes(vectorCod,this.mapaUXA.get("login_usuario_"+j));
	        	    if(j!=k && !existeCod)
	                {	        	        
	                    if((this.mapaUXA.get("login_usuario_"+k)+"").equals(this.mapaUXA.get("login_usuario_"+j)+""))	                        
	                    {
		                    errores.add("falta campo", new ActionMessage("errors.registroExistente","Login "+this.mapaUXA.get("login_usuario_"+k)+""));
		                    vectorCod.addElement(this.mapaUXA.get("login_usuario_"+k));					                    
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
     * @return Retorna mapaUXA.
     */
    public HashMap getMapaUXA() {
        return mapaUXA;
    }
    /**
     * @param mapaUXA Asigna mapaUXA.
     */
    public void setMapaUXA(HashMap mapaUXA) {
        this.mapaUXA = mapaUXA;
    }
    /**
     * @return Retorna mapaUXA.
     */
    public Object getMapaUXA(String key) {
        return mapaUXA.get(key);
    }
    /**
     * @param mapaUXA Asigna mapaUXA.
     */
    public void setMapaUXA(String key,Object value) {
        this.mapaUXA.put(key, value);
    }
    /**
     * @return Retorna codAlmacen.
     */
    public int getCodAlmacen() {
        return codAlmacen;
    }
    /**
     * @param codAlmacen Asigna codAlmacen.
     */
    public void setCodAlmacen(int codAlmacen) {
        this.codAlmacen = codAlmacen;
    }
    /**
     * @return Retorna nomAlmacen.
     */
    public String getNomAlmacen() {
        return nomAlmacen;
    }
    /**
     * @param nomAlmacen Asigna nomAlmacen.
     */
    public void setNomAlmacen(String nomAlmacen) {
        this.nomAlmacen = nomAlmacen;
    }
    /**
     * @return Retorna regEliminar.
     */
    public int getRegEliminar() {
        return regEliminar;
    }
    /**
     * @param regEliminar Asigna regEliminar.
     */
    public void setRegEliminar(int regEliminar) {
        this.regEliminar = regEliminar;
    }
    /**
     * @return Retorna registrosBDEliminar.
     */
    public Vector getRegistrosBDEliminar() {
        return registrosBDEliminar;
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(Vector registrosBDEliminar) {
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
    public void setRegistrosBDEliminar(Object value) {
        this.registrosBDEliminar.add(value);
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
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}
	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
}
