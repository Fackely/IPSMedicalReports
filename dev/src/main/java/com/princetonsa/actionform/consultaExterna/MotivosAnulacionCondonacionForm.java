package com.princetonsa.actionform.consultaExterna;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.Utilidades;


public class MotivosAnulacionCondonacionForm extends ActionForm {
	
	private static Logger logger=Logger.getLogger(MotivosAnulacionCondonacionForm.class);
	
	private int consecutivo;

	private String codigo;

	private int institucion;

	private String descripcion;

	private String activo;

	private String estado;

	private String ultimaColumnaOrdenada = "";

	private String ordenar = ConstantesBD.tipoOrdenamientoAscendente;

	private String columna = "descripcion";

	private HashMap<String, Object> motivosAnulacionCondonacion=new HashMap<String, Object> ();

	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	private String codigoEliminarPos;
	
    private int index;
    
    private boolean activa;
    
    private boolean check;
    
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private boolean temporal=false;
	private boolean temporal1=true;
	

	
	public boolean isCheck() {
		return check;
	}



	public void setCheck(boolean check) {
		this.check = check;
	}



	public boolean isActiva() {
		return activa;
	}



	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	private HashMap<String, Object> listado = new HashMap<String, Object>();
    
	
	
	
	
    
    public boolean isTemporal1() {
		return temporal1;
	}



	public void setTemporal1(boolean temporal1) {
		this.temporal1 = temporal1;
	}



	public HashMap<String, Object> getListado() {
		return listado;
	}



	public void setListado(HashMap<String, Object> listado) {
		this.listado = listado;
	}


	public Object getListado(String key) {
		return listado.get(key);
	}
	
	public void setListado(String key, Object value) {
		this.listado.put(key, value);
	}
	
	public int getNumListado()
	{
		return Utilidades.convertirAEntero(this.getListado("numRegistros")+"", true);
	}
	public int getPosicion() {
		return posicion;
	}



	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	private int posicion;

	public void clean() {
		
		this.consecutivo = getConsecutivo();

		this.codigo = "";

		this.descripcion = "";

		this.activo = "-1";
		
		this.institucion = 0;
		
		

		this.ultimaColumnaOrdenada = "";

		this.ordenar = ConstantesBD.tipoOrdenamientoAscendente;

		motivosAnulacionCondonacion = new HashMap<String, Object>();
		
		this.motivosAnulacionCondonacion.put("numRegistros", "0");
		this.activa=false;
		this.check=false;
		this.index=0;
		this.linkSiguiente="";
		this.indice = "";
		this.ultimoIndice = "";
		this.listado=new HashMap();
		this.listado.put("numRegistros", "0");
		

	}

	
	
	



	public boolean isTemporal() {
		return temporal;
	}



	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}



	public String getIndice() {
		return indice;
	}



	public void setIndice(String indice) {
		this.indice = indice;
	}



	public String getUltimoIndice() {
		return ultimoIndice;
	}



	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}



	public String getLinkSiguiente() {
		return linkSiguiente;
	}



	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}



	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	public String getCodigoEliminarPos() {
		return codigoEliminarPos;
	}



	public void setCodigoEliminarPos(String codigoEliminarPos) {
		this.codigoEliminarPos = codigoEliminarPos;
	}



	public HashMap<String, Object> getMotivosAnulacionCondonacion() {
		return motivosAnulacionCondonacion;
	}

	public void setMotivosAnulacionCondonacion(
			HashMap<String, Object> motivosAnulacionCondonacion) {
		this.motivosAnulacionCondonacion = motivosAnulacionCondonacion;
	}

	public Object getMotivosAnulacionCondonacion(String key){
		return motivosAnulacionCondonacion.get(key);
	}
	
	public void setMotivosAnulacionCondonacion(String key, Object value){
		this.motivosAnulacionCondonacion.put(key, value);
	}
	
	public int getMotivosAnulacionCondonacion1 ()
	{
		return Utilidades.convertirAEntero(this.motivosAnulacionCondonacion.get("numRegistros")+"", true);
	}

	public String getUltimaColumnaOrdenada() {
		return ultimaColumnaOrdenada;
	}

	public void setUltimaColumnaOrdenada(String ultimaColumnaOrdenada) {
		this.ultimaColumnaOrdenada = ultimaColumnaOrdenada;
	}

	public String getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getColumna() {
		return columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		
		ActionErrors errores= new ActionErrors();  
        
        if(this.estado.equals("guardar"))
        {        	
        	if(this.descripcion.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La descripcion"));
        		
        	}        	
        	if(this.codigo.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El código"));
        	}
        	else
        	{
        		for(int i=0;i<Integer.parseInt(this.getMotivosAnulacionCondonacion("numRegistros").toString());i++)
        		{
        			if(this.codigo.equals(this.getMotivosAnulacionCondonacion("codigo_"+i).toString())&&
        				this.consecutivo!=Integer.parseInt(this.getMotivosAnulacionCondonacion("consecutivo_"+i).toString()))
        			{
        				errores.add("", new ActionMessage("errors.yaExiste","El código : "+this.codigo));
        			}
        		}
        	}
        	/*if(this.activo.equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El estado activo/no activo"));
        	}*/
        	
        	
        	if(!errores.isEmpty())
        	{
        		if(this.consecutivo<=0)
        		{
        			this.estado="ingresar";
        		}
        		else
        		{
        			this.estado="modificar";
        		}
        	}
        	
            
        	/*if(!temporal1)
            {
           	 errores.add("descripcion",new ActionMessage("errors.notEspecific","Caracter invalido en codigo"));
     
            }*/
        	
        }
        if(this.estado.equals("eliminar"))
        {
        	
        	if(temporal)
            {
        		
           	 errores.add("descripcion",new ActionMessage("errors.notEspecific","No se puede eliminar este campo ya que hay relacion en otra tabla"));
           	 this.temporal=false;
          		 this.estado="empezar";
                
           	
            }
       
        	
        }
        
   
        
             	            
	    return errores;     
    
    }

}
