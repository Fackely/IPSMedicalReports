
/*
 * Creado   24/05/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.cartera;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.ConceptosCartera;

/**
 * Form que contiene todos los datos específicos 
 * para generar la consulta de conceptos de cartera
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConceptosCarteraForm extends ValidatorForm 
{
    
    /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * manejo de estados de la forma
     */
    private String estado;
    
    /**
     * código del concepto (alfanumerico)
     */
    private String codigoConcepto;
    
    /**
     * descripción del concepto
     */
    private String descripcion;
    
    /**
     * código del tipo 
     */
    private int codigoTipo;
    
    /**
     * código de la cuenta contable
     */
    private int codigoCuenta;
    
    /**
     * porcentaje del concepto
     */
    private double porcentaje;
    
    /**
     * para almacenar todos los
     * datos correspondientes al formulario.
     * estados de los datos.
     */
    private HashMap mapConceptos;
    
    /**
     * para almacenar los datos que provienen
     * de la BD, a los cuales no se les 
     * realizaran modificaciones.
     */
    private HashMap mapConceptosBD;
    
    /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
    /**
     * el numero de registros por pager
     */
    private int maxPageItems = 10;
    
    /**
     * para controlar el numero de registros del
     * HashMap.
     */
    private int numRegistros;
    
    /**
     * para controlar la página actual
     * del pager.
     */
    private int offset;
    
    /**
     * para almacenar los logs
     */
    private String log;
    
   /**
     * almacenar acciones, eventos.
     */
    private String accion;
    
    /**
     * almacena la posición de un registro en 
     * particular.
     */
    private int posRegistro;
    
    /**
     * lamacena los códigos de los registros 
     * eliminados
     */
    private ArrayList registrosEliminados;
    
    /**
     * contador del número de registros que
     * se van eliminando.
     */
    private int numRegEliminados;
    /**
     * 
     * ajuste del crédito
     */
    private String ajusteCredito;
   
    /**
     * Variable para manejar el index
     */
    private int index;
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    private String cuentaDebito;
    private String cuentaCredito;
    
    /**
     * código de la naturaleza
     */
    private int codNaturaleza;
    
    /**
	 * Campo donde se restringe por qué criterios se va a buscar los conceptos castigo
	 */
	private String criteriosBusqueda[];
	/**
	 * Colección con los datos del listado para búsqueda avanzada
	 */
	private Collection col=null;
	
	private int pager;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Atributo Tipo Concepto
	 */
	private String tipoConcepto;
	
	/**
	 * Atributo Concepto General
	 */
	private String conceptoGeneral;
	
	/**
	 * Atributo Concepto Especifico
	 */
	private String conceptoEspecifico;
	
	/**
	 * HashMap que Almacena conceptosGenerales
	 */
	private HashMap<String, Object> conceptosGenerales = new HashMap<String,Object>();
	
	/**
	 * HashMap que Almacena conceptosEspecificos
	 */
	private HashMap<String, Object> conceptosEspecificos = new HashMap<String,Object>();
	
	/**
	 * Bandera para manejar los mensajes
	 */
	private int bandera =0;
	
	/**
	 * 
	 */
	private int tipoCartera;
	
	public int getTipoCartera() {
		return tipoCartera;
	}

	public void setTipoCartera(int tipoCartera) {
		this.tipoCartera = tipoCartera;
	}
	//manejo de variables para modificar un registro
	private String posicionConceptoModificar;		
	private HashMap conceptoModificarMap=new HashMap();
	
	
	/**
	 * Arreglo para almacenar los conceptos generales
	 */
	private HashMap<String, Object> arregloConceptosGenerales = new HashMap<String,Object>();
	private ArrayList<HashMap<String, Object>> conceptGrales = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Arreglo para almacenar los conceptos especificos
	 */
	private HashMap<String, Object> arregloConceptosEspecificos = new HashMap<String,Object>();
	private ArrayList<HashMap<String, Object>> conceptEspec = new ArrayList<HashMap<String,Object>>();
	
	
	//Atributos para el manejo del ordenamiento
	private String indice;
	private String ultimoIndice;
	
	/**
     * inicializar atributos de esta forma
     *
     */
    public void reset (int institucion)
    {
       this.codigoConcepto = "";
       this.codigoCuenta = -1;
       this.codigoTipo = -1;
       this.descripcion = "";
       this.porcentaje = -1;
       this.mapConceptos = new HashMap ();
       this.mapConceptosBD = new HashMap ();
       this.linkSiguiente = "";
       this.numRegistros = 0;
       this.offset = 0;
       this.log = "";
       this.index=0;
       this.accion = "";
       this.posRegistro = -1;
       this.registrosEliminados = new ArrayList ();
       this.numRegEliminados = 0;
       this.patronOrdenar = "";
       this.ultimoPatron = "";
       this.cuentaDebito="";
       this.cuentaCredito="";
       this.codNaturaleza = -1;
       this.ajusteCredito="";
       this.tipoConcepto="";
       this.conceptoGeneral="";
       this.conceptoEspecifico="";
       this.bandera = 0;
       this.tipoCartera=-2;
       this.posicionConceptoModificar="";
       this.arregloConceptosGenerales= new HashMap<String,Object>();
       this.arregloConceptosGenerales.put("numregistros", "0");
       this.arregloConceptosEspecificos= new HashMap<String,Object>();
       this.arregloConceptosEspecificos.put("numregistros", "0");
       this.conceptoModificarMap= new HashMap();
       this.conceptoModificarMap.put("numRegistros", "0");
       this.conceptGrales = new ArrayList<HashMap<String,Object>>();
       this.conceptEspec = new ArrayList<HashMap<String,Object>>();
       this.maxPageItems=ValoresPorDefecto.getMaxPageItemsInt(institucion);
       this.indice="";
       this.ultimoIndice="";
    }
    

	public String getUltimoIndice() {
		return ultimoIndice;
	}

	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public HashMap<String, Object> getArregloConceptosEspecificos() {
		return arregloConceptosEspecificos;
	}

	public void setArregloConceptosEspecificos(
			HashMap<String, Object> arregloConceptosEspecificos) {
		this.arregloConceptosEspecificos = arregloConceptosEspecificos;
	}
	
	public Object getArregloConceptosEspecificos(String key){
		return this.arregloConceptosEspecificos.get(key);
	}
	
	public void setArregloConceptosEspecificos(String key, Object value){
		this.arregloConceptosEspecificos.put(key, value);
	}

	public ArrayList<HashMap<String, Object>> getConceptEspec() {
		return conceptEspec;
	}

	public void setConceptEspec(ArrayList<HashMap<String, Object>> conceptEspec) {
		this.conceptEspec = conceptEspec;
	}

	public ArrayList<HashMap<String, Object>> getConceptGrales() {
		return conceptGrales;
	}

	public void setConceptGrales(ArrayList<HashMap<String, Object>> conceptGrales) {
		this.conceptGrales = conceptGrales;
	}

	public HashMap getConceptoModificarMap() {
		return conceptoModificarMap;
	}

	public void setConceptoModificarMap(HashMap conceptoModificarMap) {
		this.conceptoModificarMap = conceptoModificarMap;
	}
	
	public Object getConceptoModificarMap(String key){
		return this.conceptoModificarMap.get(key);
	}
	
	public void setConceptoModificarMap(String key, Object value){
		this.conceptoModificarMap.put(key, value);
	}
	
	public HashMap<String, Object> getArregloConceptosGenerales() {
		return arregloConceptosGenerales;
	}

	public void setArregloConceptosGenerales(
			HashMap<String, Object> arregloConceptosGenerales) {
		this.arregloConceptosGenerales = arregloConceptosGenerales;
	}
	
	public Object getArregloConceptosGenerales(String key){
		return this.arregloConceptosGenerales.get(key);
	}
	
	public void setArregloConceptosGenerales(String key, Object value){
		this.arregloConceptosGenerales.put(key, value);
	}

	public String getPosicionConceptoModificar() {
		return posicionConceptoModificar;
	}

	public void setPosicionConceptoModificar(String posicionConceptoModificar) {
		this.posicionConceptoModificar = posicionConceptoModificar;
	}

	/**
     * reset de los campos para las busqueda
     *
     */
    public void resetBusqueda()
    {
        this.codigoConcepto = "";
        this.codigoCuenta = -1;
        this.codigoTipo = -1;
        this.descripcion = "";
        this.porcentaje = -1;
        this.cuentaDebito="";
        this.cuentaCredito="";
        this.codNaturaleza = -1;
        this.ajusteCredito="";
        this.tipoCartera=-2;
    }
    
    /**
     * Reset del mensaje
     */
    public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
    
    /**
     * Reset del HashMap de conceptos generales
     */
    public void resetConceptosGenerales()
    {
    	this.conceptosGenerales = new HashMap<String, Object>();
    }
    
    /**
     * Reset del HashMap de conceptos especificos
     */
    public void resetConceptosEspecificos()
    {
    	this.conceptosEspecificos = new HashMap<String, Object>();
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
		int reg = 0;
	    String codigo="";
		
	    if(estado.equals("salirGuardarPagos") || estado.equals("salirGuardarAjustes"))
		{
		    reg = 0;	
		    codigo="";
		    for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)//El código es requerido
		    {
		      if((this.getMapConceptos("codigoConcepto_"+k)+"").equals(""))  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.required","El código para el registro "+reg));  
		      }
		      else if((this.getMapConceptos("codigoConcepto_"+k)+"").length()>6)
		      {
		    	  
		          reg = k;
		          reg ++;
		    	  errores.add("", new ActionMessage("errors.maxlength","El código para el registro "+reg,"6"));
		      }
		      
		      if((this.getMapConceptos("descripcion_"+k)+"").equals(""))  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.required","La descripción para el registro "+reg));  
		      }
		      else if((this.getMapConceptos("descripcion_"+k)+"").length()>256)
		      {
		    	  reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.notEspecific","Se han ingresado caracteres especiales en la descripción del registro "+(reg)+". Por favor verifique"));
		      }
		      if((this.getMapConceptos("ajusteCredito_"+k)+"").equals("") ||(this.getMapConceptos("ajusteCredito_"+k)+"").equals(""))  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.required","el tipo de ajuste para el registro "+reg));  
		      }
		    
		    //El código debe ser unico dentro de los conceptos de pago
		    
		      if((this.getMapConceptos("tipo_"+k)+"").equals("MEM"))
		        codigo = this.getMapConceptos("codigoConcepto_"+k)+"";
		      
		      for(int l=0;l<Integer.parseInt(this.getMapConceptos("numRegistros")+"");l++)
			  {		         
		          if(k != l)
			     {
		             if(codigo.equals(this.getMapConceptos("codigoConcepto_"+l)+"") && !(this.getMapConceptos("codigoConcepto_"+l)+"").equals(""))  
		             {		                  		                  
		                  errores.add("", new ActionMessage("errors.yaExiste","El código "+codigo));                 
		             }
			     }  
			  }
		      codigo="";
		    }
		}
		if(estado.equals("salirGuardarPagos"))
		{
		    reg = 0;	
		    codigo="";	    
		    for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)//El código es requerido
		    {		     
		      if((this.getMapConceptos("codigoTipo_"+k)+"").equals("-1"))  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.required","El tipo de concepto para el registro "+reg));  
		      }
		    
		    //Validación del porcentaje debe estar entre 0 - 100
		    
		      if((this.getMapConceptos("porcentaje_"+k)+"").equals(""))  
			  {
			    reg = k;
			    reg ++;
			    errores.add("", new ActionMessage("errors.required","El porcentaje para el registro "+reg));  
			  }
		      else
		      if(Double.parseDouble(this.getMapConceptos("porcentaje_"+k)+"") > 100)  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.porcentajeMayor100","El porcentaje para el registro "+reg));  
		      }
		    }
		}
		if(estado.equals("salirGuardarAjustes"))
		{
			reg = 0;	
			codigo="";
			for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)//La naturaleza es requerida
			{
				if((this.getMapConceptos("codigoNaturaleza_"+k)+"").equals("-1"))  
				{
					errores.add("", new ActionMessage("errors.required","La naturaleza del concepto para el registro "+(k+1)));  
				}
				if((this.getMapConceptos("tipoCartera_"+k)+"").equals("0"))  
				{
					errores.add("", new ActionMessage("errors.required","El tipo de Cartera del concepto para el registro "+(k+1)));  
				}
			}
		}
		if(estado.equals("guardarConceptoCastigo"))
		{
			codigo="";
			String posiciones=""; //variable donde se almacenan las posiciones repetidas
		    for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)
		    {
		      if((this.getMapConceptos("codigoConcepto_"+k)+"").equals(""))  //El código es requerido
		      {
		          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
		      }
		      else if((this.getMapConceptos("codigoConcepto_"+k)+"").length()>6)
		      {
		    	  
		    	  errores.add("", new ActionMessage("errors.maxlength","El código para el registro "+(k+1),"6"));
		      }

		      if((this.getMapConceptos("descripcion_"+k)+"").equals(""))  //La descripción es requerida
		      {
		          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
		      }
		      if((this.getMapConceptos("ajusteCredito_"+k)+"").equals("-1"))  //La descripción es requerida
		      {
		          errores.add("AJUSTE CREDITO REQUERIDO", new ActionMessage("errors.required","El ajuste de crédito para el registro "+(k+1)));  
		      }
		      //validar que el codigo no exista
		      codigo = this.getMapConceptos("codigoConcepto_"+k)+"";
		      boolean bandera=false;
		      //for(int l=0;l<k;l++)
		      for(int l=Integer.parseInt(this.getMapConceptos("numRegistros")+"")-1;l>k;l--)
			  {		         
		      	 //se verifica que no exista en el mapa
	             if(!yaFueVerificado(k,posiciones)&&codigo.equals(this.getMapConceptos("codigoConcepto_"+l)+"") && !(this.getMapConceptos("codigoConcepto_"+l)+"").equals(""))  
	             {		                  		                  
	                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));
	                  bandera=true;
	                  //se almacena las posiciones de los registros iguales
						if(!posiciones.equals(""))
							posiciones+=ConstantesBD.separadorSplit;
						posiciones+=k+ConstantesBD.separadorSplit+l;
	             }
	             
	             
			  }
		      //se verifica que el nuevo registro no exista en la BD
		      //el registro debe ser nuevo o modificado en el código
             if(!bandera&&!codigo.equals("")&&!yaFueVerificado(k,posiciones)&&
             	(mapConceptos.get("codigoConceptoAntiguo_"+k)==null||codigo.compareTo(mapConceptos.get("codigoConceptoAntiguo_"+k)+"")!=0))
             {
             	Connection con=null;
				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{;}
				//objetos usados
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				ConceptosCartera conceptos=new ConceptosCartera();
				//se preparan los datos para cosnultar solo por código
				conceptos.setCodigo(codigo);
				conceptos.setDescripcion("");
				conceptos.setAjusteCredito("-1");
				//se ejecuta búsqueda avanzada
				HashMap resultados=conceptos.consultarConceptoCastigo(con,true,usuario.getCodigoInstitucionInt());
				//si hay resultados quiere decir que el registro ya existe
				if(Integer.parseInt(resultados.get("numRegistros")+"")>0)
				{
					errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));
					//se almacena las posiciones de los registros iguales
					if(!posiciones.equals(""))
						posiciones+=ConstantesBD.separadorSplit;
					posiciones+=k;
				}
				
				//SE CIERRA LA CONEXION
				try
				{
					UtilidadBD.cerrarConexion(con);
				}
				catch(SQLException e)
				{;}
             }
		      codigo="";
		    }
		}
		//-------------------------------------------------------
		if(estado.equals("GuardarConceptoGlosas"))
		{
			codigo="";
		    for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)
		    {
		      if((this.getMapConceptos("codigoConcepto_"+k)+"").equals(""))  //El código es requerido
		      {
		          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
		      }
		      else if((this.getMapConceptos("codigoConcepto_"+k)+"").length()>6)
		      {
		    	  
		    	  errores.add("", new ActionMessage("errors.maxlength","El código para el registro "+(k+1),"6"));
		      }
		      if((this.getMapConceptos("descripcion_"+k)+"").equals(""))  //La descripción es requerida
		      {
		          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
		      }
		      
		      // El concepto especifico es requerido
		      if(
		    		  (UtilidadTexto.isEmpty(this.getMapConceptos("conceptoEspecifico_"+k)+"")) ||
		    		  (this.getMapConceptos("conceptoEspecifico_"+k).toString().equals(ConstantesBD.codigoNuncaValido+""))
		      )  
		      {
		          errores.add("CONCEPTO ESPECIFICO", new ActionMessage("errors.required","El Concepto Especifico para el registro "+(k+1) + "es Invalido, Por Favor seleccione uno."));  
		      }
		      
		      
		      //validar que el codigo no exista
		      codigo = this.getMapConceptos("codigoConcepto_"+k)+"";
		      for(int l=0;l<k;l++)
			  {		         
	             if(codigo.equals(this.getMapConceptos("codigoConcepto_"+l)+"") && !(this.getMapConceptos("codigoConcepto_"+l)+"").equals(""))  
	             {		                  		                  
	                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));                 
	             }
			  }
		      codigo="";
		    }
		}
		//-------------------------------------------------------
		
		if(estado.equals("GuardarConceptoRespuestasGlosas"))
		{
			codigo="";
		    for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++)
		    {
		      if((this.getMapConceptos("codigoRespuestaConcepto_"+k)+"").equals(""))  //El código es requerido
		      {
		          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
		      }
		      if((this.getMapConceptos("descripcion_"+k)+"").equals(""))  //La descripción es requerida
		      {
		          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
		      }
		      //validar que el codigo no exista
		      codigo = this.getMapConceptos("codigoRespuestaConcepto_"+k)+"";
		      for(int l=0;l<k;l++)
			  {		         
	             if(codigo.equals(this.getMapConceptos("codigoRespuestaConcepto_"+l)+"") && !(this.getMapConceptos("codigoRespuestaConcepto_"+l)+"").equals(""))  
	             {		                  		                  
	                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));                 
	             }
			  }
		      codigo="";
		    }
		}		
		
		if(estado.equals("guardarConceptoModificado"))
		{
			int posicion= Utilidades.convertirAEntero(this.getPosicionConceptoModificar())+1;
			if(this.conceptoModificarMap.get("codConcepto").equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El codigo del Concepto para el registro "+posicion));
        	}
			if(this.conceptoModificarMap.get("descConcepto").equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La descripcion del Concepto para el registro "+posicion));
        	}
        	if(this.conceptoModificarMap.get("tipoConcepto").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El tipo de Concepto para el registro "+posicion));
        	}
        	if(this.conceptoModificarMap.get("codConceptoGral").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Concepto General para el registro "+posicion));
        	}
        	if(this.conceptoModificarMap.get("codConceptoEsp").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Concepto Especifico para el registro "+posicion));
        	}
		}	
		
		if(estado.equals("insertarNuevoConcepto"))
		{			
			int posicion= Utilidades.convertirAEntero(this.getPosicionConceptoModificar())+1;
			if(this.conceptoModificarMap.get("codConcepto").equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El codigo del Concepto para el registro Nuevo"));
        	}
			if(this.conceptoModificarMap.get("descConcepto").equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La descripcion del Concepto para el registro Nuevo"));
        	}
        	if(this.conceptoModificarMap.get("tipoConcepto").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El tipo de Concepto para el registro Nuevo"));
        	}
        	if(this.conceptoModificarMap.get("codConceptoGral").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Concepto General para el registro Nuevo"));
        	}
        	if(this.conceptoModificarMap.get("codConceptoEsp").equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Concepto Especifico para el registro Nuevo"));
        	}        	
        	codigo = this.conceptoModificarMap.get("codConcepto")+"";
		      for(int l=0;l<Utilidades.convertirAEntero(this.mapConceptos.get("numRegistros")+"");l++)  {		         
	             if(codigo.equals(this.getMapConceptos("codigoConcepto_"+l)+"") && !(this.getMapConceptos("codigoConcepto_"+l)+"").equals("")) {		                  		                  
	                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));                 
	             }
			  }
		      codigo="";
		}	
		
		return errores;
		
	}
    
	
	/**
	 * Método que revisa si el registro ya fue verificado
	 * @param pos
	 * @param posiciones
	 * @return
	 */
	private boolean yaFueVerificado(int pos, String posiciones) {
		boolean verificado=false;
		if(posiciones.length()>0)
		{
			String vector[]=posiciones.split(ConstantesBD.separadorSplit);
			for(int i=0;i<vector.length;i++)
			{
				//se compara si el registro actual es igual a alguno de los antes
				//verificados y considerados como repetidos
				if((this.mapConceptos.get("codigoConcepto_"+pos)+"").compareTo(this.mapConceptos.get("codigoConcepto_"+Integer.parseInt(vector[i]))+"")==0)
					verificado=true;
			}
		}
		
		return verificado;
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
     * @return Retorna codigoConcepto.
     */
    public String getCodigoConcepto() {       
        return this.codigoConcepto;
    }
    /**
     * @param codigoConcepto Asigna codigoConcepto.
     */
    public void setCodigoConcepto(String codigoConcepto) {        
        this.codigoConcepto = codigoConcepto;
    }
    /**
     * @return Retorna codigoCuenta.
     */
    public int getCodigoCuenta() {
        return codigoCuenta;
    }
    /**
     * @param codigoCuenta Asigna codigoCuenta.
     */
    public void setCodigoCuenta(int codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }
    /**
     * @return Retorna codigoTipo.
     */
    public int getCodigoTipo() {
        return codigoTipo;
    }
    /**
     * @param codigoTipo Asigna codigoTipo.
     */
    public void setCodigoTipo(int codigoTipo) {
        this.codigoTipo = codigoTipo;
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
    /**
     * @return Retorna mapConceptos.
     */
    public HashMap getMapConceptos() {
        return mapConceptos;
    }
    /**
     * @param mapConceptos Asigna mapConceptos.
     */
    public void setMapConceptos(HashMap mapConceptos) {
        this.mapConceptos = mapConceptos;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConceptos(String key) {
        return mapConceptos.get(key);
    }
    /**
     * @param key String.
     * @param value Object.
     */
    public void setMapConceptos(String key,Object value) {
        this.mapConceptos.put(key,value);
    }
    /**
     * @return Retorna porcentaje.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
    /**
     * @param porcentaje Asigna porcentaje.
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }    
    /**
     * @return Retorna mapConceptosBD.
     */
    public HashMap getMapConceptosBD() {
        return mapConceptosBD;
    }
    /**
     * @param mapConceptosBD Asigna mapConceptosBD.
     */
    public void setMapConceptosBD(HashMap mapConceptosBD) {
        this.mapConceptosBD = mapConceptosBD;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConceptosBD(String key) {
        return mapConceptosBD.get(key);
    }
    /**
     * @param key String.
     * @param value Object
     */
    public void setMapConceptosBD(String key, Object value) {
        this.mapConceptosBD.put(key,value);
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
     * @return Retorna maxPageItems.
     */
    public int getMaxPageItems() {
        return maxPageItems;
    }    
    /**
     * @return Retorna numRegistros.
     */
    public int getNumRegistros() {
        return numRegistros;
    }
    /**
     * @param numRegistros Asigna numRegistros.
     */
    public void setNumRegistros(int numRegistros) {
        this.numRegistros = numRegistros;
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
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
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
	 * @return Returns the ajusteCredito.
	 */
	public String getAjusteCredito() {
		return ajusteCredito;
	}
	/**
	 * @param ajusteCredito The ajusteCredito to set.
	 */
	public void setAjusteCredito(String ajusteCredito) {
		this.ajusteCredito = ajusteCredito;
	}
	/**
     * @return Retorna posRegistro.
     */
    public int getPosRegistro() {
        return posRegistro;
    }
    /**
     * @param posRegistro Asigna posRegistro.
     */
    public void setPosRegistro(int posRegistro) {
        this.posRegistro = posRegistro;
    }
    /**
     * @return Retorna registrosEliminados.
     */
    public ArrayList getRegistrosEliminados() {
        return registrosEliminados;
    }
    /**
     * @param registrosEliminados Asigna registrosEliminados.
     */
    public void setRegistrosEliminados(ArrayList registrosEliminados) {
        this.registrosEliminados = registrosEliminados;
    }
    /**
     * @param pos int, posición del objeto
     * @return  Object
     */
    public Object getRegistrosEliminados(int pos) {
        return registrosEliminados.get(pos);
    }
    /**
     * @param pos int, posición del objeto
     * @param value Object, Objeto 
     */
    public void setRegistrosEliminados(int pos,Object value) {
        this.registrosEliminados.add(pos,value);
    }	
    /**
     * @return Retorna numRegEliminados.
     */
    public int getNumRegEliminados() {
        return numRegEliminados;
    }
    /**
     * @param numRegEliminados Asigna numRegEliminados.
     */
    public void setNumRegEliminados(int numRegEliminados) {
        this.numRegEliminados = numRegEliminados;
    }    
    
	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
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
	 * @return Returns the cuentaCredito.
	 */
	public String getCuentaCredito() {
		return cuentaCredito;
	}
	/**
	 * @param cuentaCredito The cuentaCredito to set.
	 */
	public void setCuentaCredito(String cuentaCredito) {
		this.cuentaCredito = cuentaCredito;
	}
	/**
	 * @return Returns the cuentaDebito.
	 */
	public String getCuentaDebito() {
		return cuentaDebito;
	}
	/**
	 * @param cuentaDebito The cuentaDebito to set.
	 */
	public void setCuentaDebito(String cuentaDebito) {
		this.cuentaDebito = cuentaDebito;
	}
	/**
     * @return Retorna codNaturaleza.
     */
    public int getCodNaturaleza() {
        return codNaturaleza;
    }
    /**
     * @param codNaturaleza Asigna codNaturaleza.
     */
    public void setCodNaturaleza(int codNaturaleza) {
        this.codNaturaleza = codNaturaleza;
    }
	
	/**
	 * @return Returns the criteriosBusqueda.
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}
	/**
	 * @param criteriosBusqueda The criteriosBusqueda to set.
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}
	/**
	 * resetea en vector de strings que
	 * contiene los criterios de búsqueda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		try
		{
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the col.
	 */
	public Collection getCol() {
		return col;
	}
	/**
	 * @param col The col to set.
	 */
	public void setCol(Collection col) {
		this.col = col;
	}
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	
	/**
	 * @return Returns the pager.
	 */
	public int getPager() {
		return pager;
	}
	/**
	 * @param pager The pager to set.
	 */
	public void setPager(int pager) {
		this.pager = pager;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Get de Tipo Concepto
	 * @return tipoConcepto
	 */
	public String getTipoConcepto() {
		return tipoConcepto;
	}

	/**
	 * Set de Tipo Concepto
	 * @param tipoConcepto
	 */
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	/**
	 * Get de Concepto General
	 * @return conceptoGeneral
	 */
	public String getConceptoGeneral() {
		return conceptoGeneral;
	}

	/**
	 * Set de Concepto General
	 * @param conceptoGeneral
	 */
	public void setConceptoGeneral(String conceptoGeneral) {
		this.conceptoGeneral = conceptoGeneral;
	}

	/**
	 * Get de Concepto General
	 * @return conceptoEspecifico
	 */
	public String getConceptoEspecifico() {
		return conceptoEspecifico;
	}

	/**
	 * Set de ConceptoEspecifico
	 * @param conceptoEspecifico
	 */
	public void setConceptoEspecifico(String conceptoEspecifico) {
		this.conceptoEspecifico = conceptoEspecifico;
	}

	/**
	 * Get de Conceptos Generales
	 * @return conceptosGenerales
	 */
	public HashMap<String, Object> getConceptosGenerales() {
		return conceptosGenerales;
	}

	/**
	 * Set de Conceptos Generales
	 * @param conceptosGenerales
	 */
	public void setConceptosGenerales(HashMap<String, Object> conceptosGenerales) {
		this.conceptosGenerales = conceptosGenerales;
	}

	/**
	 * Get de Conceptos Especificos
	 * @return conceptosEspecificos
	 */
	public HashMap<String, Object> getConceptosEspecificos() {
		return conceptosEspecificos;
	}

	/**
	 * Set de Conceptos Especificos
	 * @param conceptosEspecificos
	 */
	public void setConceptosEspecificos(HashMap<String, Object> conceptosEspecificos) {
		this.conceptosEspecificos = conceptosEspecificos;
	}
	
	/**
	 * Método get individual de un concepto general
	 * @param key
	 * @return conceptosEspecificos.get(key)
	 */
	public Object getConceptosGenerales(String key) {
		return conceptosGenerales.get(key);
	}
	
	/**
	 * Método set individual de un oncepto general
	 * @param key
	 * @param value conceptosEspecificos
	 */
	public void setConceptosGenerales(String key,Object value) {
		this.conceptosGenerales.put(key, value);
	}
	
	/**
	 * Método get individual de un concepto especifico
	 * @param key
	 * @return conceptosEspecificos.get(key)
	 */
	public Object getConceptosEspecificos(String key) {
		return conceptosEspecificos.get(key);
	}
	
	/**
	 * Método set individual de un oncepto especifico
	 * @param key
	 * @param value conceptosEspecificos
	 */
	public void setConceptosEspecificos(String key,Object value) {
		this.conceptosEspecificos.put(key, value);
	}

	/**
	 * Método set de bandera
	 * @param bandera
	 */
	public void setBandera(int bandera) {
		this.bandera = bandera;
	}



	/**
	 * Método get de bandera
	 * @return bandera
	 */
	public int getBandera() {
		return bandera;
	}
	
}
