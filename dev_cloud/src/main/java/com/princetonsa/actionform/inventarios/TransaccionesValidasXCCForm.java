
/*
 * Creado   21/11/2005
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
import util.UtilidadCadena;

/**
 * 
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TransaccionesValidasXCCForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * para el manejo en el pager
     */
    private String linkSiguiente;
    /**
     * mapa que almacena los registros
     * de la funcionalidad
     */
    private HashMap mapaTrans;    
    /**
     * código del centro de costo
     */
    private int codCentroCosto;
    /**
     * nombre del centro de costo
     */
    private String nomCentroCosto;
    
    /**
     * 
     */
    private int transaccionFiltro;
    
    /**
     * patron para ordenar
     */
    private String patronOrdenar;
    /**
     * patron para ordenar
     */
    private String ultimoPatron;
    /**
     * número del registro a eliminar
     */
    private int regEliminar;
    /**
     * almacena los registros a eliminar
     */
    private Vector registrosBDEliminar;
 
    private String codCentroAtencion;
    
    private String nomCentroAtencion;
    
    private boolean procesoExitoso;
    
    private int institucion;
    
    /**
     * Mapa empleado para cargar los tipos de transacciones según el centro de costo seleccionado
     */
    private HashMap<String, Object> transaccionesInventarios;
    
    /**
     * Mapa empleado para cargar todos los tipos de transacciones
     */
    private HashMap<String, Object> transaccionesInventariosASeleccionar;
    
    /**
     * Código de la Transacción
     */
    private String codigoTransaccionSeleccionada;
    
    /**
     * Boolean que indica si se presentaron errores
     */
    private boolean errores;
    
    /**
     * String con la posición seleccionada desde el listado de Transacciones
     */
    private String posicionSeleccionada;
    
    /**
     * Boolean que indica si se va a parametrizar un nuevo registro
     */
    private boolean nuevoRegistro;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset (int institucion)
    {   
       this.linkSiguiente = ""; 
       this.mapaTrans = new HashMap();
       this.codCentroCosto = ConstantesBD.codigoNuncaValido;
       this.nomCentroCosto = "Seleccione";
       this.patronOrdenar = "";
       this.ultimoPatron = "";
       this.regEliminar = ConstantesBD.codigoNuncaValido;
       this.registrosBDEliminar = new Vector();
       this.nomCentroAtencion = "";
       this.codCentroAtencion = "";
       this.transaccionFiltro = ConstantesBD.codigoNuncaValido;
       this.procesoExitoso = false;
       this.institucion = institucion;
       
       this.transaccionesInventarios = new HashMap<String, Object>();
       transaccionesInventarios.put("numRegistros", "0");
       this.transaccionesInventariosASeleccionar = new HashMap<String, Object>();
       transaccionesInventariosASeleccionar.put("numRegistros", "0");
       this.codigoTransaccionSeleccionada = "";
       this.errores = false;
       this.posicionSeleccionada = "";
       this.nuevoRegistro = false;
    }
    
    /**reset de los listados**/
    public void resetListados()
    {
        this.mapaTrans=new HashMap();
        mapaTrans.put("numRegistros", "0");
        this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.registrosBDEliminar=new Vector();          
    }
    
    /**
     * 
     */
    public void resetTransaccionesInventarios()
    {
    	this.transaccionesInventariosASeleccionar = new HashMap<String, Object>();
        transaccionesInventariosASeleccionar.put("numRegistros", "0");
        this.codigoTransaccionSeleccionada = "";
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
	        /*if(this.nuevoRegistro == true)
	        {
				if(!UtilidadCadena.noEsVacio(this.codigoTransaccionSeleccionada))
					errores.add("centroCosto", new ActionMessage("errors.required","La Transacción de Inventario "));
				
				if(!UtilidadCadena.noEsVacio(this.mapaTrans.get("numRegistros")+"") || Utilidades.convertirAEntero(this.mapaTrans.get("numRegistros")+"") == 0 )
					errores.add("claseGrupo", new ActionMessage("error.inventarios.esNecesarioSeleccionar"));
	        }*/
				
	    	if(UtilidadCadena.noEsVacio(this.mapaTrans.get("numRegistros")+""))
	        {
		    	for(int k=0;k<Integer.parseInt(this.mapaTrans.get("numRegistros")+"");k++)
		        {
		    		//la clase es requerida
		    		if((this.mapaTrans.get("cod_clase_inventario_"+k)+"").equals("-1"))
		            {
		                errores.add("falta campo", new ActionMessage("errors.required","La Clase de Inventario para el Registro "+reg));
			            this.errores = true;
		            }
		        }
	        }
	    	if(errores.isEmpty())
    		{
		    	if(UtilidadCadena.noEsVacio(this.mapaTrans.get("numRegistros")+""))
		        {
			    	for(int k=0;k<Integer.parseInt(this.mapaTrans.get("numRegistros")+"");k++)
			        {
			            //Modificado según Anexo 728
			        	/*if((this.codCentroCosto+"").equals("-1"))
			            {
			                reg = k;
				            reg ++;
				            errores.add("falta campo", new ActionMessage("errors.required","El centro de costo para el registro "+reg));
			            }*/
			        	
			            /*if((this.mapaTrans.get("cod_tipos_trans_inventario_"+k)+"").equals(ConstantesBD.codigoNuncaValido))
			            {
			                reg = k;
				            reg ++;
				            errores.add("falta campo", new ActionMessage("errors.required","El Tipo de Transacción para el registro "+reg));
			            }*/
			            
			            /*if(UtilidadValidacion.esCentroCostoSubalmacen(this.codCentroCosto))
			            {
				            if((this.mapaTrans.get("cod_clase_inventario_"+k)+"").equals("-1"))
				            {
				                reg = k;
					            reg ++;
					            errores.add("falta campo", new ActionMessage("errors.required","La Clase de Inventario para el Registro "+reg));
					            this.errores = true;
				            }
				            /*if((this.mapaTrans.get("cod_grupo_inventario_"+k)+"").equals("-1"))
				            {
				                reg = k;
					            reg ++;
					            errores.add("falta campo", new ActionMessage("errors.required","El grupo de inventario para el registro "+reg));  
				            }///////// ESTE PEDAZO ESTABA COMENTARIADO
			            }*/
			            
		    		    for(int j=0;j<Integer.parseInt(this.mapaTrans.get("numRegistros")+"");j++)
			            {	                
			        	    existeCod=validarCodigosExistentes(vectorCod,j+"");
			        	    if(j!=k && !existeCod)
			                {	        	        
			                    if((this.mapaTrans.get("cod_tipos_trans_inventario_"+k)+"").equals(this.mapaTrans.get("cod_tipos_trans_inventario_"+j)+""))
			                    {	
			                        if((this.mapaTrans.get("cod_clase_inventario_"+k)+"").equals(this.mapaTrans.get("cod_clase_inventario_"+j)+"") && Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+"")!=ConstantesBD.codigoNuncaValido)
			                        {	
			                        	if((this.mapaTrans.get("cod_grupo_inventario_"+k)+"").equals(this.mapaTrans.get("cod_grupo_inventario_"+j)+""))
					                    {
						                    errores.add("falta campo", new ActionMessage("errors.registroExistente","registro con Centro de Costo "+this.nomCentroCosto+" Transacción "+this.mapaTrans.get("desc_tipos_trans_inventario_"+k)+" Clase "+this.mapaTrans.get("nom_clase_inventario_"+k)+" y Grupo "+this.mapaTrans.get("nom_grupo_inventario_"+k)));
						                    vectorCod.addElement(k+"");					                    
						                }
			                        }	
			                    }    
			                }
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
     * @return Retorna mapaTrans.
     */
    public HashMap getMapaTrans() {
        return mapaTrans;
    }
    /**
     * @param mapaTrans Asigna mapaTrans.
     */
    public void setMapaTrans(HashMap mapaTrans) {
        this.mapaTrans = mapaTrans;
    }
    /**
     * @return Retorna mapaTrans.
     */
    public Object getMapaTrans(String key) {
        return mapaTrans.get(key);
    }
    /**
     * @param mapaTrans Asigna mapaTrans.
     */
    public void setMapaTrans(String key,Object value) {
        this.mapaTrans.put(key, value);
    }    
    /**
     * @return Retorna codCentroCosto.
     */
    public int getCodCentroCosto() {
        return codCentroCosto;
    }
    /**
     * @param codCentroCosto Asigna codCentroCosto.
     */
    public void setCodCentroCosto(int codCentroCosto) {
        this.codCentroCosto = codCentroCosto;
    }
    /**
     * @return Retorna nomCentroCosto.
     */
    public String getNomCentroCosto() {
        return nomCentroCosto;
    }
    /**
     * @param nomCentroCosto Asigna nomCentroCosto.
     */
    public void setNomCentroCosto(String nomCentroCosto) {
        this.nomCentroCosto = nomCentroCosto;
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
    public Object getRegistrosBDEliminar(int index) {
        return registrosBDEliminar.get(index);
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(Object value) {
        this.registrosBDEliminar.add(value);
    }
	public String getNomCentroAtencion() {
		return nomCentroAtencion;
	}
	public void setNomCentroAtencion(String nomCentroAtencion) {
		this.nomCentroAtencion = nomCentroAtencion;
	}
	public String getCodCentroAtencion() {
		return codCentroAtencion;
	}
	public void setCodCentroAtencion(String codCentroAtencion) {
		this.codCentroAtencion = codCentroAtencion;
	}
	/**
	 * @return the transaccionFiltro
	 */
	public int getTransaccionFiltro() {
		return transaccionFiltro;
	}
	/**
	 * @param transaccionFiltro the transaccionFiltro to set
	 */
	public void setTransaccionFiltro(int transaccionFiltro) {
		this.transaccionFiltro = transaccionFiltro;
	}
	/**
	 * @return the procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}
	/**
	 * @param procesoExitoso the procesoExitoso to set
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	/**
	 * @return the transaccionesInventarios
	 */
	public HashMap<String, Object> getTransaccionesInventarios() {
		return transaccionesInventarios;
	}
	
	/**
	 * @param transaccionesInventarios the transaccionesInventarios to set
	 */
	public void setTransaccionesInventarios(
			HashMap<String, Object> transaccionesInventarios) {
		this.transaccionesInventarios = transaccionesInventarios;
	}

	/**
     * @return Retorna transaccionesInventarios
     */
    public Object getTransaccionesInventarios(String key) {
        return transaccionesInventarios.get(key);
    }
    
    /**
     * @param transaccionesInventarios Asigna transaccionesInventarios
     */
    public void setTransaccionesInventarios(String key,Object value) {
        this.transaccionesInventarios.put(key, value);
    }
    
	/**
	 * @return the transaccionesInventariosASeleccionar
	 */
	public HashMap<String, Object> getTransaccionesInventariosASeleccionar() {
		return transaccionesInventariosASeleccionar;
	}
	
	/**
	 * @param transaccionesInventariosASeleccionar the transaccionesInventariosASeleccionar to set
	 */
	public void setTransaccionesInventariosASeleccionar(
			HashMap<String, Object> transaccionesInventariosASeleccionar) {
		this.transaccionesInventariosASeleccionar = transaccionesInventariosASeleccionar;
	}
	
	/**
     * @return Retorna transaccionesInventariosASeleccionar
     */
    public Object getTransaccionesInventariosASeleccionar(String key) {
        return transaccionesInventariosASeleccionar.get(key);
    }
    
    /**
     * @param transaccionesInventariosASeleccionar Asigna transaccionesInventariosASeleccionar
     */
    public void setTransaccionesInventariosASeleccionar(String key,Object value) {
        this.transaccionesInventariosASeleccionar.put(key, value);
    }
	
    /**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}
	
	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return the codigoTransaccionSeleccionada
	 */
	public String getCodigoTransaccionSeleccionada() {
		return codigoTransaccionSeleccionada;
	}

	/**
	 * @param codigoTransaccionSeleccionada the codigoTransaccionSeleccionada to set
	 */
	public void setCodigoTransaccionSeleccionada(
			String codigoTransaccionSeleccionada) {
		this.codigoTransaccionSeleccionada = codigoTransaccionSeleccionada;
	}

	/**
	 * @return the posicionSeleccionada
	 */
	public String getPosicionSeleccionada() {
		return posicionSeleccionada;
	}

	/**
	 * @param posicionSeleccionada the posicionSeleccionada to set
	 */
	public void setPosicionSeleccionada(String posicionSeleccionada) {
		this.posicionSeleccionada = posicionSeleccionada;
	}

	/**
	 * @return the nuevoRegistro
	 */
	public boolean isNuevoRegistro() {
		return nuevoRegistro;
	}

	/**
	 * @param nuevoRegistro the nuevoRegistro to set
	 */
	public void setNuevoRegistro(boolean nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}
	
}