/*
 * @(#)GruposForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.salasCirugia;

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

import com.princetonsa.mundo.cargos.SalarioMinimo;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * grupos 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Sep 07 , 2005
 * @author wrios 
 */
public class GruposForm extends ValidatorForm
{
	/**
	 * codigo interno del grupo
	 */
	private int codigoPKGrupo;
	
	/**
	 * codigo del esquema tar
	 * contiene consecutivo + ConstantesBD.separadorSplit + (S/N) para saber si es general o particular
	 */
	private String codigoEsquemaTarifario;
	
	/**
	 * Consecutivo esquema tarifario
	 */
	private int consecutivoEsquemaTarifario;
	
	/**
	 * indicador para saber si el esquema tarifarioe s general o particular
	 */
	private boolean esquemaTarifarioGeneral;

	/**
	 * nombre del esquemaTar
	 */
	private String nombreEsquemaTarifario;
	
	/**
	 * codigo del grupo
	 */
	private String grupo;
	
	/**
	 * codigo del asocio
	 */
	private int codigoAsocio;
	
	
	/**
	 * codigo del tipo de liquidacion
	 */
	private int codigoTipoLiquidacion;
	
	/**
	 * nombre del tipo de liquidacion
	 */
	private String nombreTipoLiquidacion;
	
	/**
	 * unidades soat del asocio
	 */
	private String unidades;
	
	/**
	 * valor del grupo
	 */
	private String valor;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * codigo de la institucion
	 */
	private int codigoInstitucion;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Mapa con los grupos
	 */
	private HashMap mapaGrupos= new HashMap();
	
	
	/**
	 * La consulta de la info de grupos está devolviendo 
	 * actualmente estos campos= 
	 * ("codigoPKGrupo", 
		 "codigoEsquemaTarifario", "nombreEsquemaTarifario", 
		 "grupo", "codigoAsocio", 
		 "acronimoTipoServicio", "nombreTipoServicio", "descTipoServicio", 
		 "codigoCups", "codigoSoat", 
		 "codigoTipoLiquidacion", "nombreTipoLiquidacion", 
		 "unidades", "valor", "activo", 
		 "estaBD", "esEliminada", "estaSiendoUtilizadaEnOtraFunc" ),
	 * como cada uno de ellos ocupa una posición en el HashMap, entonces
	 * toca calcular el verdadero tamanio en filas= HashMap.size/18;
	 */
	private int numeroRealFilasMapa;
	
	/**
	 * //num de columnas que devuelve la consulta de la info de los 
	 * grupos en este caso=  
	 * ("codigoPKGrupo", 
		 "codigoEsquemaTarifario", "nombreEsquemaTarifario", 
		 "grupo", "codigoAsocio", 
		 "acronimoTipoServicio", "nombreTipoServicio", "descTipoServicio", 
		 "codigoCups", "codigoSoat", 
		 "codigoTipoLiquidacion", "nombreTipoLiquidacion", 
		 "unidades", "valor", "activo", 
		 "estaBD", "esEliminada","estaSiendoUtilizadaEnOtraFunc" ),
	 * este valor es necesario a la hora de calcular el numero de filas del mapa 
	 */
	final int tamanioNumeroColumnas=19;
	
	/**
	 * Cód del index del Mapa
	 */
	private int indexMapa;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
	 * 
	 */
	private ArrayList convenios;
	
	/**
	 * 
	 */
	private ArrayList tiposServicio;
	
	/**
	 * 
	 */
	public ArrayList tarifariosOficiales;
	
	/**
	 * Mapa que almacena el detalle de los codigos de los posibles codigos segun la cantidad de tarifarios oficiales
	 */
	public HashMap detCodigosGruposMap;
	
	/**
	 * 
	 */
	private String convenio;
	
	/**
	 * indice que para localizar la posicion correspondiente al codigo del detalle segun el tarifario oficial
	 */
	private int indiceTarifario;
	
	/**
	 * 
	 */
	private HashMap mapaVigencias;
	
	/**
	 * 
	 */
	private HashMap mapaVigenciasEliminado;
	
	/**
	 * 
	 */
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	 private int offset;
	 
	 /**
	  * 
	  */
	 private int indiceEliminado;
	 
	 /**
	  * 
	  */
	 private String tipoServicio;
	 
	 private boolean operacionTrue=false;
	
	 private String funcionalidad="";
	 
	 private String liquidarPor="";
	 
	 private String cups="";
	 
	 
	public String getFuncionalidad() {
		return funcionalidad;
	}

	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
	    this.codigoPKGrupo=ConstantesBD.codigoNuncaValido;
	    this.codigoEsquemaTarifario="";
	    this.consecutivoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
	    this.esquemaTarifarioGeneral = false;
	    this.nombreEsquemaTarifario="";
	    this.grupo="";
	    this.codigoAsocio=ConstantesBD.codigoNuncaValido;
	    this.codigoTipoLiquidacion=ConstantesBD.codigoNuncaValido;
	    this.nombreTipoLiquidacion="";
	    this.unidades="";
	    this.valor="";
	    this.numeroRealFilasMapa=0;
	    this.convenio="";
	    this.convenios=new ArrayList();
	    this.tiposServicio=new ArrayList();
	    this.tarifariosOficiales=new ArrayList();
	    this.detCodigosGruposMap=new HashMap();
	    this.detCodigosGruposMap.put("numRegistros", "0");
	    this.indiceTarifario=ConstantesBD.codigoNuncaValido;
	    this.mapaVigencias=new HashMap();
	    this.mapaVigencias.put("numRegistros", "0");
	    this.linkSiguiente="";
       	this.maxPageItems=10;
    	this.ultimoPatron="";
    	this.offset=0;
    	this.indiceEliminado=ConstantesBD.codigoNuncaValido;
    	this.mapaVigenciasEliminado=new HashMap();
	    this.mapaVigenciasEliminado.put("numRegistros", "0");
	    this.tipoServicio="";
	    this.operacionTrue=false;
	    this.funcionalidad="";
	    this.cups="";
	    this.liquidarPor="";
	}
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public String getLiquidarPor() {
		return liquidarPor;
	}

	public void setLiquidarPor(String liquidarPor) {
		this.liquidarPor = liquidarPor;
	}

	public String getCups() {
		return cups;
	}

	public void setCups(String cups) {
		this.cups = cups;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	    ActionErrors errores= new ActionErrors();
	    errores=super.validate(mapping,request);
	    if(estado.equals("empezarIngresarModificarEliminar"))
	    {
	        try
	        {
		        Connection con= UtilidadBD.abrirConexion();
		        SalarioMinimo salarioMinimo = new SalarioMinimo();
		    	ResultadoBoolean rb=salarioMinimo.consultar(con);
		    	if(!rb.isTrue())
		    	    errores.add("error.salasCirugia.salarioMinimo", new ActionMessage("error.salasCirugia.salarioMinimo"));
		    	if(!UtilidadValidacion.esTiposAsociosParametrizados(con, this.getCodigoInstitucion()))
		    	    errores.add("errors.faltaParametrizacion", new ActionMessage("errors.faltaParametrizacion","Tipos Asocio"));
		    	UtilidadBD.cerrarConexion(con);
	        }
	        catch(SQLException sqle)
	        {
	            sqle.printStackTrace();
	        }
	    }
	    else if(estado.equals("guardar"))
	    {
	        for(int i=0; i<this.getNumeroRealFilasMapa(); i++)
			{
		        if(!UtilidadTexto.getBoolean(this.getMapaGrupos("esEliminada_"+i)+""))
		        {
		            String tempoNomGrupo=" [Grupo: "+this.getMapaGrupos("grupo_"+i)+"] ";
		            if(this.getCodigoEsquemaTarifario().equals("")&& this.getConvenio().equals(""))
		            {
		                errores.add("El esquema Tarifario requerido", new ActionMessage("errors.required","El campo esquema Tarifario "));
		            }
		            try
		            {
		                if(!UtilidadCadena.noEsVacio(this.getMapaGrupos("grupo_"+i)+""))
		                    errores.add("El Grupo es requerido", new ActionMessage("errors.required","El Grupo "));
		                else
		                {    
			                double tempDouble= Double.parseDouble(this.getMapaGrupos("grupo_"+i)+"");
			                if(tempDouble<=0 )
			                {
			                    errores.add("grupo", new ActionMessage("errors.debeSerNumeroMayor", "El grupo", "0"));
			                }
			            }    
		            }catch(Exception e)
		            {
		                errores.add("grupo", new ActionMessage("errors.integer", "El grupo"));
		            }
		            
		            if(errores.isEmpty())
		            {
		            	for(int a=0;a<i;a++)
		            	{
		            		if(this.getMapaGrupos("tipoServicio_"+i).equals(this.getMapaGrupos("tipoServicio_"+a))&&this.getMapaGrupos("grupo_"+i).equals(this.getMapaGrupos("grupo_"+a))&&this.getMapaGrupos("codigoAsocio_"+i).equals(this.getMapaGrupos("codigoAsocio_"+a)))
		            		{
		            			errores.add("actualización Grupos", new ActionMessage("error.salasCirugia.yaExisteUniqueTipoServicio","los datos de grupos quirúrgicos que se encuentran asociados a los esquemas tarifarios basados en SOAT (Transacción)"));
		            		}
		            	}
			           
		            	if (!UtilidadCadena.noEsVacio(this.getMapaGrupos("tipoServicio_"+i)+""))
		            		errores.add("El Tipo Liquidacion requerido", new ActionMessage("errors.required","El campo Tipo Servicio "+tempoNomGrupo));
		            	
		            	if(    (this.getMapaGrupos("codigoAsocio_"+i)+"").equals("-1")  ||
				               (this.getMapaGrupos("codigoAsocio_"+i)+"").equals("0")   ||  
				               (this.getMapaGrupos("codigoAsocio_"+i)+"").equals("null")   )
			            {
			                errores.add("El Tipo Asocio requerido", new ActionMessage("errors.required","El campo Asocio "+tempoNomGrupo));
			            }
		            	
			            if(    ((UtilidadTexto.isEmpty(this.getMapaGrupos("valorTarifario_"+i+"_"+0)+"")) || (this.getMapaGrupos("valorTarifario_"+i+"_"+0)+"").trim().equals("") ))
				        {
			                errores.add("El codigo cups requerido", new ActionMessage("errors.required","El campo Código CUPS "+tempoNomGrupo));
				        }
			            
			            if(!UtilidadCadena.noEsVacio(this.getMapaGrupos("liquidarpor_"+i)+"")) 
				        {
			                errores.add("El codigo cups requerido", new ActionMessage("errors.required","El campo Liquidar Por "+tempoNomGrupo));
				        }
			            
			            
			            if(    (this.getMapaGrupos("codigoTipoLiquidacion_"+i)+"").equals("-1")  ||
					               (this.getMapaGrupos("codigoTipoLiquidacion_"+i)+"").equals("0")   ||  
					               (this.getMapaGrupos("codigoTipoLiquidacion_"+i)+"").equals("null")   )
				        {
			                errores.add("El Tipo Liquidacion requerido", new ActionMessage("errors.required","El campo Tipo Liquidación "+tempoNomGrupo));
				        }
			            else
			            {
			                if((this.getMapaGrupos("codigoTipoLiquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionSoatUnidades+""))
			                {
			                    try
					            {
			                        if((this.getMapaGrupos("unidades_"+i)+"").trim().equals(""))
					                    errores.add("Las unidades es requerido", new ActionMessage("errors.required","Las Unidades "+tempoNomGrupo));
					                else
					                {    
						                double tempDouble= Double.parseDouble(this.getMapaGrupos("unidades_"+i)+"");
						                if(tempDouble<=0 )
						                {
						                    errores.add("unidades", new ActionMessage("errors.debeSerNumeroMayor", "Las unidades "+tempoNomGrupo, "0"));
						                }
						            }    
					            }
			                    catch(Exception e)
					            {
					                errores.add("unidades", new ActionMessage("errors.debeSerNumeroMayor", "Las unidades "+tempoNomGrupo, "0"));
					            }
			                }
			             }
			            try
			            {
			                if((this.getMapaGrupos("valor_"+i)+"").equals(""))
			                { 
			                    errores.add("El Valor es requerido", new ActionMessage("errors.required","El valor "+tempoNomGrupo));
			                }    
			                else
			                {
			                    double valorDouble=Double.parseDouble(this.getMapaGrupos("valor_"+i)+"");
			                    if(valorDouble<0)
			                    {
			                        errores.add("valor", new ActionMessage("errors.MayorIgualQue", "El valor "+tempoNomGrupo, "0"));
			                    }
			                }    
			            }catch(Exception e)
			            {
			                errores.add("valor", new ActionMessage("errors.MayorIgualQue", "El valor "+tempoNomGrupo, "0"));
			            }
		            }    
		        }
			}
	    }
	    else if(estado.equals("guardarVigencia"))
	    {
	    	for(int a=0;a<Utilidades.convertirAEntero(this.getMapaVigencias("numRegistros")+"");a++)
	    	{
	    		
	    		if(this.getMapaVigencias("fechainicial_"+a).toString().equals("") || this.getMapaVigencias("fechainicial_"+a)==null)
	    		{
	    			errores.add("fechainicial", new ActionMessage("errors.required","La Fecha inicial del registro "+a));
	    		}
	    		if(this.getMapaVigencias("fechafinal_"+a).toString().equals("") || this.getMapaVigencias("fechafinal_"+a)==null)
	    		{
	    			errores.add("fechafinal", new ActionMessage("errors.required","La Fecha final del registro "+a));
	    		}
	    		if(!UtilidadTexto.isEmpty(this.getMapaVigencias("fechainicial_"+a).toString()) || !UtilidadTexto.isEmpty(this.getMapaVigencias("fechafinal_"+a).toString()))
	    		{
	    			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getMapaVigencias("fechafinal_"+a)+""), UtilidadFecha.conversionFormatoFechaAAp(this.getMapaVigencias("fechainicial_"+a)+"")))
		    		{
		    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Inicial, ",UtilidadFecha.conversionFormatoFechaAAp(this.getMapaVigencias("fechainicial_"+a)+"")));
		    		}
		    		if(this.getMapaVigencias("estabd_"+a).equals(ConstantesBD.acronimoNo))
		    		{
			    		if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getMapaVigencias("fechafinal_"+a)+""), UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
			    		{
			    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Fecha del Sistema, ",UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())));
			    		}
		    		}
	    		}
	    		
	    		for(int m=a;m<Utilidades.convertirAEntero(this.getMapaVigencias("numRegistros")+"");m++)
	    		{
	    			if(m>a&&UtilidadFecha.existeTraslapeEntreFechas(this.getMapaVigencias("fechainicial_"+a)+"", this.getMapaVigencias("fechafinal_"+a)+"", this.getMapaVigencias("fechainicial_"+m)+"", this.getMapaVigencias("fechafinal_"+m)+""))
	    			{
	    				errores.add("", new ActionMessage("error.rangoFechasInvalido",this.getMapaVigencias("fechainicial_"+a),this.getMapaVigencias("fechafinal_"+a)+" del registro "+a+1,this.getMapaVigencias("fechainicial_"+m),this.getMapaVigencias("fechafinal_"+m)+" del registro "+m+1));
	    				m=Utilidades.convertirAEntero(this.getMapaVigencias("numRegistros")+"");
	    			}
	    			
	    		}
	    	}
	    }
	    else if(estado.equals("busquedaAvanzada"))
	    {
	        if(!this.getGrupo().trim().equals(""))
	        {
	            try
	            {
	                Integer.parseInt(this.getGrupo());
	            }
	            catch (Exception e)
	            {
	                errores.add("grupo", new ActionMessage("errors.debeSerNumeroMayor", "El grupo", "0"));
	            }
	        }
	        if(!this.getUnidades().trim().equals(""))
	        {
	            try
	            {
	                Double.parseDouble(this.getUnidades());
	            }
	            catch (Exception e)
	            {
	                errores.add("unidades", new ActionMessage("errors.debeSerNumeroMayor", "Las unidades ", "0"));
	            }
	        }
	        if(!this.getValor().trim().equals(""))
	        {
	            try
	            {
	                Double.parseDouble(this.getValor());
	            }
	            catch (Exception e)
	            {
	                errores.add("valor", new ActionMessage("errors.debeSerNumeroMayor", "El valor ", "0"));
	            }
	        }
	    }
	    return errores;
	}
	
	/**
	 * reset del mapa
	 *
	 */
	public void resetMapa()
	{
	    this.mapaGrupos= new HashMap();
	    this.mapaGrupos.put("numRegistros", "0");
	}
	
	
	/**
	 * Set del mapa de grupos
	 * @param key
	 * @param value
	 */
	public void setMapaGrupos(String key, Object value){
		mapaGrupos.put(key, value);
	}
	
	/**
	 * Get del mapa de Grupos
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaGrupos(String key){
		return mapaGrupos.get(key);
	}
	
	/**
	 * Tamanio de la Col
	 * @return
	 */
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	 /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa()
    {
    	if(numeroRealFilasMapa==0);//Evitar el warning
      this.numeroRealFilasMapa = mapaGrupos.size()/tamanioNumeroColumnas;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa(int numeroRealFilasMapa) {
        this.numeroRealFilasMapa = numeroRealFilasMapa;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapa(){
        try
        {
	        if(this.mapaGrupos==null)
	            return 0;
	        if(this.mapaGrupos.isEmpty())
	            return 0;
        }
        catch(NullPointerException npe)
        {
            return 0;
        }
        numeroRealFilasMapa=Utilidades.convertirAEntero(this.mapaGrupos.get("numRegistros")+"");
        return numeroRealFilasMapa;
    }
    /**
     * @return Returns the mapaGrupos.
     */
    public HashMap getMapaGrupos() {
        return mapaGrupos;
    }
    /**
     * @param mapaGrupos The mapaGrupos to set.
     */
    public void setMapaGrupos(HashMap mapaGrupos) {
        this.mapaGrupos = mapaGrupos;
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
    /**
     * @return Returns the codigoAsocio.
     */
    public int getCodigoAsocio() {
        return codigoAsocio;
    }
    /**
     * @param codigoAsocio The codigoAsocio to set.
     */
    public void setCodigoAsocio(int codigoAsocio) {
        this.codigoAsocio = codigoAsocio;
    }
    /**
     * @return Returns the codigoEsquemaTarifario.
     */
    public String getCodigoEsquemaTarifario() {
        return codigoEsquemaTarifario;
    }
    /**
     * @param codigoEsquemaTarifario The codigoEsquemaTarifario to set.
     */
    public void setCodigoEsquemaTarifario(String codigoEsquemaTarifario) {
        this.codigoEsquemaTarifario = codigoEsquemaTarifario;
    }
    /**
     * @return Returns the codigoPKGrupo.
     */
    public int getCodigoPKGrupo() {
        return codigoPKGrupo;
    }
    /**
     * @param codigoPKGrupo The codigoPKGrupo to set.
     */
    public void setCodigoPKGrupo(int codigoPKGrupo) {
        this.codigoPKGrupo = codigoPKGrupo;
    }
    /**
     * @return Returns the codigoTipoLiquidacion.
     */
    public int getCodigoTipoLiquidacion() {
        return codigoTipoLiquidacion;
    }
    /**
     * @param codigoTipoLiquidacion The codigoTipoLiquidacion to set.
     */
    public void setCodigoTipoLiquidacion(int codigoTipoLiquidacion) {
        this.codigoTipoLiquidacion = codigoTipoLiquidacion;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the grupo.
     */
    public String getGrupo() {
        return grupo;
    }
    /**
     * @param grupo The grupo to set.
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    /**
     * @return Returns the nombreEsquemaTarifario.
     */
    public String getNombreEsquemaTarifario() {
        return nombreEsquemaTarifario;
    }
    /**
     * @param nombreEsquemaTarifario The nombreEsquemaTarifario to set.
     */
    public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
        this.nombreEsquemaTarifario = nombreEsquemaTarifario;
    }
    /**
     * @return Returns the nombreTipoLiquidacion.
     */
    public String getNombreTipoLiquidacion() {
        return nombreTipoLiquidacion;
    }
    /**
     * @param nombreTipoLiquidacion The nombreTipoLiquidacion to set.
     */
    public void setNombreTipoLiquidacion(String nombreTipoLiquidacion) {
        this.nombreTipoLiquidacion = nombreTipoLiquidacion;
    }
    /**
     * @return Returns the unidades.
     */
    public String getUnidades() {
        return unidades;
    }
    /**
     * @param unidades The unidades to set.
     */
    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
    /**
     * @return Returns the valor.
     */
    public String getValor() {
        return valor;
    }
    /**
     * @param valor The valor to set.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
    /**
     * @return Returns the indexMapa.
     */
    public int getIndexMapa() {
        return indexMapa;
    }
    /**
     * @param indexMapa The indexMapa to set.
     */
    public void setIndexMapa(int indexMapa) {
        this.indexMapa = indexMapa;
    }
    /**
     * @return Returns the codigoInstitucion.
     */
    public int getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(int codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }
    /**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the tiposServicio
	 */
	public ArrayList getTiposServicio() {
		return tiposServicio;
	}

	/**
	 * @param tiposServicio the tiposServicio to set
	 */
	public void setTiposServicio(ArrayList tiposServicio) {
		this.tiposServicio = tiposServicio;
	}

	/**
	 * @return the tarifariosOficiales
	 */
	public ArrayList getTarifariosOficiales() {
		return tarifariosOficiales;
	}

	/**
	 * @param tarifariosOficiales the tarifariosOficiales to set
	 */
	public void setTarifariosOficiales(ArrayList tarifariosOficiales) {
		this.tarifariosOficiales = tarifariosOficiales;
	}

	/**
	 * @return the detCodigosGruposMap
	 */
	public HashMap getDetCodigosGruposMap() {
		return detCodigosGruposMap;
	}

	/**
	 * @param detCodigosGruposMap the detCodigosGruposMap to set
	 */
	public void setDetCodigosGruposMap(HashMap detCodigosGruposMap) {
		this.detCodigosGruposMap = detCodigosGruposMap;
	}

	/**
	 * @return the indiceTarifario
	 */
	public int getIndiceTarifario() {
		return indiceTarifario;
	}

	/**
	 * @param indiceTarifario the indiceTarifario to set
	 */
	public void setIndiceTarifario(int indiceTarifario) {
		this.indiceTarifario = indiceTarifario;
	}

	/**
	 * @return the mapaVigencias
	 */
	public HashMap getMapaVigencias() {
		return mapaVigencias;
	}

	/**
	 * @param mapaVigencias the mapaVigencias to set
	 */
	public void setMapaVigencias(HashMap mapaVigencias) {
		this.mapaVigencias = mapaVigencias;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the indiceEliminado
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado the indiceEliminado to set
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}
	
	public Object getMapaVigencias(String key)
	{
		return mapaVigencias.get(key);
	}
	
	public void setMapaVigencias(String key,Object value)
	{
		this.mapaVigencias.put(key,value);
	}

	/**
	 * @return the mapaVigenciasEliminado
	 */
	public HashMap getMapaVigenciasEliminado() {
		return mapaVigenciasEliminado;
	}

	/**
	 * @param mapaVigenciasEliminado the mapaVigenciasEliminado to set
	 */
	public void setMapaVigenciasEliminado(HashMap mapaVigenciasEliminado) {
		this.mapaVigenciasEliminado = mapaVigenciasEliminado;
	}
	
	public Object getMapaVigenciasEliminado(String key)
	{
		return mapaVigenciasEliminado.get(key);
	}
	
	public void setMapaVigenciasEliminado(String key,Object value)
	{
		this.mapaVigenciasEliminado.put(key,value);
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	
	/**
	 * Método que retorna el consecutivo del esquema tarifario
	 * @return
	 */
	public int getConsecutivoEsquemaTarifario()
	{
		return consecutivoEsquemaTarifario;
	}
	
	/**
	 * Método para asignar el consecutivo del esquema tarifario
	 * @param consecutivoEsquemaTarifario
	 */
	public void setConsecutivoEsquemaTarifario(int consecutivoEsquemaTarifario)
	{
		this.consecutivoEsquemaTarifario = consecutivoEsquemaTarifario;
	}
	
	/**
	 * Método que verifica si el esquema tarifario es general o particular
	 * @return
	 */
	public boolean isEsquemaTarifarioGeneral()
	{
		return this.esquemaTarifarioGeneral;
	}
	
	/**
	 * Método para asignar el indicador si el esquema tarifario es general o particular
	 * @param esEsquemaGeneral
	 */
	public void setEsquemaTarifarioGeneral(boolean esEsquemaGeneral)
	{
		this.esquemaTarifarioGeneral = esEsquemaGeneral;
	}
}
