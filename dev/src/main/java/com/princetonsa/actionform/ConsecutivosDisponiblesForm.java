package com.princetonsa.actionform;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadValidacion;
import util.Utilidades;
/**
 * 
 * @version 1.1, 1/12/2005
 * @author [restructurada] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsecutivosDisponiblesForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private transient Logger logger = Logger.getLogger(ConsecutivosDisponiblesForm.class);
	
	private String nombre, descripcion, valor, anioVigencia;
	
	/**
     * manejo de estados de la forma
     */
    private String estado;
    
    private boolean checkbox;
    
     /**
     * para almacenar todos los
     * datos correspondientes al formulario. 
     * estados de los datos.
     */
    private HashMap mapConsecutivos;
    
    /**
     * para almacenar los datos que provienen
     * de la BD, a los cuales no se les 
     * realizaran modificaciones.
     */
    private HashMap mapConsecutivosBD;
    
    
    /**
     * para almacenar los datos que provienen
     * de las multiempresas por institucion 
     * */
    private ArrayList listMultiEmpresa;
    
    
    /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
    /**
     * el numero de registros por pager
     */
    private final int maxPageItems = 10;
    
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
    
    private int pager;
    /**
     * código del modulo
     */
    private int modulo;   
    /**
     * código del almacen
     */
    private int almacen;
    /**
     * código de la empresa
     * */
    private String empresa;
    /**
     * almacena los consecutivos de 
     * inventarios por almacen o por
     * unico sistema
     */
    private HashMap mapConsecInv;
    /**
     * almacena el tipo de consecutivo
     * inventario seleccionado, por almacen
     * o unico en el sistema
     */
    private String casoConsecInv;
    /**
     * almcena los modulos validos
     */
    private HashMap mapModulos;
    
    /**
     * Almacena el valor actual del consecutivo, se utiliza para validar
     * el consecutivo de cuentas de cobro capitación
     */
    private String valorConsecutivoCapitacion;
    
    /**
     * Indicador si se puede editar el consecutivo facturas varias. Segun Anexo 562
     */
    private int modificaConsecutivoFacturasV;
    
    /**
     * Atributo que define si se permiten modificar los consecutivos de notas paciente
     * si el parámetro Maneja Consecutivo Notas Pacientes por centro de Atención 
     * se encuentra en NO
     */
    private boolean permiteModificarConsecutivoNotasPaciente;
    
    /**
     * Atributo que define si se permiten modificar los consecutivos de notas paciente
     * si ya se ha ingresado alguno y se pretende modificar se valida que no existan
     * notas paciente creadas
     */
    private boolean existenNotasPaciente;
    
    /**
     * inicializar atributos de esta forma
     *
     */
    public void reset (boolean inicio)
    {
       this.anioVigencia="";
       this.valor="";
       this.checkbox=false;
       this.mapConsecutivos = new HashMap ();
       this.mapConsecutivosBD = new HashMap ();
       this.linkSiguiente = "";
       this.numRegistros=0;
       this.offset=ConstantesBD.codigoNuncaValido;
       this.log = "";
       this.index=0;
       this.accion = "";
       this.posRegistro = -1;
       this.registrosEliminados = new ArrayList ();
       this.numRegEliminados = 0;
       this.patronOrdenar = "";
       this.ultimoPatron = "";
       this.valorConsecutivoCapitacion="";
       this.listMultiEmpresa = new ArrayList();
       this.empresa = "";
       this.modificaConsecutivoFacturasV=0;
       this.setPermiteModificarConsecutivoNotasPaciente(true);
       this.setExistenNotasPaciente(false);
       if(inicio)
    	   this.mapConsecInv=new HashMap();   
    }
    
    public void resetModulo()
    {
        this.modulo=ConstantesBD.codigoNuncaValido;  
        this.almacen=ConstantesBD.codigoNuncaValido;   
        this.casoConsecInv="";
    }
    public void resetMapModulos()
    {
        this.mapModulos=new HashMap();
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
	    		
		if(estado.equals("salirGuardar"))
		{
		    reg = 0;	
		    for(int k=0;k<Integer.parseInt(this.getMapConsecutivos("numRegistros")+"");k++)
		    {
		      if((this.getMapConsecutivos("nombre_"+k)+"").equals(""))  
		      {
		          reg = k;
		          reg ++;
		          errores.add("", new ActionMessage("errors.required","El nombre para el registro "+reg));  
		      }
		    }   
		}
		
		
		
		  if(estado.equals("salirGuardarConsecutivo"))
			{
		    //---------Si el módulo es capitación se realizan las siguientes validaciones ------//
		    if(modulo==ConstantesBD.codigoModuloCapitacion)
		    {
		    	if(mapConsecutivos!=null)
		    	{
		    		Connection con=null;
					try
						{
							con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
						}
					catch(SQLException e)
						{
							logger.warn("No se pudo abrir la conexión"+e.toString());
						}
					
					UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
					
		    		
		    		int numRegistros=Integer.parseInt(mapConsecutivos.get("numRegistros")+"");
		    		for(int i=0; i<numRegistros; i++)
			    		{	
			    		String nombreConsecutivo=mapConsecutivos.get("nombre_"+i)+"";
			    		if(nombreConsecutivo.equals(ConstantesBD.nombreConsecutivoCuentaCobroCapitacion))
				    		{
				    			if(UtilidadCadena.noEsVacio(this.valorConsecutivoCapitacion))
					    			{
					    				int nuevoValorConsecutivo=Integer.parseInt(mapConsecutivos.get("valor_"+i)+"");
					    				int valorAnterior=Integer.parseInt(this.valorConsecutivoCapitacion);
					    				int ultimaCuentaCobro=Utilidades.obtenerUltimaCuentaCobroCapitacion(con, usuario.getCodigoInstitucionInt());
					    				
					    				if(ultimaCuentaCobro>0)
						    				{
					    						//--------Se verifica que el consecutivo sea mayor al último consecutivo de cuenta de cobro guardada ---//
							    				if (nuevoValorConsecutivo<=ultimaCuentaCobro)
							    				{
							    					this.mapConsecutivos.put("valor_"+i, valorAnterior);
						    						errores.add("valorConsecutivoMenor", new ActionMessage("errors.integerMayorQue", "El Consecutivo de Cuentas de Cobro de capitación", ultimaCuentaCobro));
							    				}
						    				}
					    				else
					    					if(nuevoValorConsecutivo!=valorAnterior)
						    				{
					    						//--------Se verifica que el consecutivo sea mayor al consecutivo anterior --------//
							    				if(nuevoValorConsecutivo<=valorAnterior)
								    				{
							    						this.mapConsecutivos.put("valor_"+i, valorAnterior);
							    						errores.add("valorConsecutivoMenor", new ActionMessage("errors.integerMayorQue", "El Consecutivo de Cuentas de Cobro de capitación", valorAnterior));
								    				}
						    				}//else
					    				
					    			}//if
				    		}//if
			    		}//for
		    		try
					{
						UtilidadBD.cerrarConexion(con);
					} 
					catch (SQLException e1)
					{
						logger.warn("No se pudo cerrar la conexión"+e1.toString());
					}	
		    	}//if
		    }//if
		    else if(modulo==ConstantesBD.codigoModuloOrdenes)
		    {
		    	if(mapConsecutivos!=null)
		    	{
		    		int numRegistros=Integer.parseInt(mapConsecutivos.get("numRegistros")+"");
		    		for(int i=0; i<numRegistros; i++)
		    		{	
			    		String nombreConsecutivo=mapConsecutivos.get("nombre_"+i)+"";
			    		if(nombreConsecutivo.equals(ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos))
			    		{
		    				double valorConsecutivo=Utilidades.convertirADouble(mapConsecutivos.get("valor_"+i)+"");
		    				double maximoValorUsado=Utilidades.convertirADouble(UtilidadValidacion.obtenerMaximoConsecutivoJustificacionNoPosArticulos());
		    				
		    				if(valorConsecutivo<=maximoValorUsado)
			    			{
		    					errores.add("valorConsecutivoMenor", new ActionMessage("errors.integerMayorQue", "El Consecutivo de Justificacion No Pos Articulos", maximoValorUsado));
			    			}
			    		}//if
			    		if(nombreConsecutivo.equals(ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios))
			    		{
		    				double valorConsecutivo=Utilidades.convertirADouble(mapConsecutivos.get("valor_"+i)+"");
		    				double maximoValorUsado=Utilidades.convertirADouble(UtilidadValidacion.obtenerMaximoConsecutivoJustificacionNoPosServicios());
		    				
		    				if(valorConsecutivo<=maximoValorUsado)
			    			{
		    					errores.add("valorConsecutivoMenor", new ActionMessage("errors.integerMayorQue", "El Consecutivo de Justificacion No Pos Servicios", maximoValorUsado));
			    			}
			    		}//if
		    		}//for
		    	}//if
		    }
		}
		return errores;
	}
    
	/**
	 * @return Returns the checkbox.
	 */
	public boolean isCheckbox() {
		return checkbox;
	}
	/**
	 * @param checkbox The checkbox to set.
	 */
	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
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
     * @return Retorna mapConsecutivos.
     */
    public HashMap getMapConsecutivos() {
        return mapConsecutivos;
    }
    /**
     * @param mapConsecutivos Asigna mapConsecutivos.
     */
    public void setMapConsecutivos(HashMap mapConsecutivos) {
        this.mapConsecutivos = mapConsecutivos;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConsecutivos(String key) {
        return mapConsecutivos.get(key);
    }
    /**
     * @param key String.
     * @param value Object.
     */
    public void setMapConsecutivos(String key,Object value) {   	
    	this.mapConsecutivos.put(key,value);
    }
        
    /**
     * @return Retorna mapConsecutivosBD.
     */
    public HashMap getMapConsecutivosBD() {
        return mapConsecutivosBD;
    }
    /**
     * @param mapConsecutivosBD Asigna mapConsecutivosBD.
     */
    public void setMapConsecutivosBD(HashMap mapConsecutivosBD) {
        this.mapConsecutivosBD = mapConsecutivosBD;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConsecutivosBD(String key) {
        return mapConsecutivosBD.get(key);
    }
    /**
     * @param key String.
     * @param value Object
     */
    public void setMapConsecutivosBD(String key, Object value) {
        this.mapConsecutivosBD.put(key,value);
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
	 * @return Returns the anioVigencia.
	 */
	public String getAnioVigencia() {
		return anioVigencia;
	}
	/**
	 * @param anioVigencia The anioVigencia to set.
	 */
	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
     * @return Retorna modulo.
     */
    public int getModulo() {
        return modulo;
    }
    /**
     * @param modulo Asigna modulo.
     */
    public void setModulo(int modulo) {
        this.modulo = modulo;
    }
    /**
     * @return Retorna almacen.
     */
    public int getAlmacen() {
        return almacen;
    }
    /**
     * @param almacen Asigna almacen.
     */
    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }
    /**
     * @return Retorna mapConsecInv.
     */
    public HashMap getMapConsecInv() {
        return mapConsecInv;
    }
    /**
     * @param mapConsecInv Asigna mapConsecInv.
     */
    public void setMapConsecInv(HashMap mapConsecInv) {
        this.mapConsecInv = mapConsecInv;
    }
    
    /**
     * @return Retorna mapConsecInv.
     */
    public Object getMapConsecInv(String key) {
        return mapConsecInv.get(key);
    }
    /**
     * @param mapConsecInv Asigna mapConsecInv.
     */
    public void setMapConsecInv(String key, Object value) {
        this.mapConsecInv.put(key, value);
    }
    
    /**
     * @return Retorna casoConsecInv.
     */
    public String getCasoConsecInv() {
        return casoConsecInv;
    }
    /**
     * @param casoConsecInv Asigna casoConsecInv.
     */
    public void setCasoConsecInv(String casoConsecInv) {
        this.casoConsecInv = casoConsecInv;
    }
    /**
     * @return Retorna mapModulos.
     */
    public HashMap getMapModulos() {
        return mapModulos;
    }
    /**
     * @param mapModulos Asigna mapModulos.
     */
    public void setMapModulos(HashMap mapModulos) {
        this.mapModulos = mapModulos;
    }
    /**
     * @return Retorna mapModulos.
     */
    public Object getMapModulos(String key) {
        return mapModulos.get(key);
    }
    /**
     * @param mapModulos Asigna mapModulos.
     */
    public void setMapModulos(String key,Object value) {
        this.mapModulos.put(key, value);
    }
	/**
	 * @return Retorna the valorConsecutivoCapitacion.
	 */
	public String getValorConsecutivoCapitacion()
	{
		return valorConsecutivoCapitacion;
	}
	/**
	 * @param valorConsecutivoCapitacion The valorConsecutivoCapitacion to set.
	 */
	public void setValorConsecutivoCapitacion(String valorConsecutivoCapitacion)
	{
		this.valorConsecutivoCapitacion = valorConsecutivoCapitacion;
	}
	/**
	 * @return the listMultiEmpresa
	 */
	public ArrayList getListMultiEmpresa() {
		return listMultiEmpresa;
	}
	/**
	 * @param listMultiEmpresa the listMultiEmpresa to set
	 */
	public void setListMultiEmpresa(ArrayList listMultiEmpresa) {
		this.listMultiEmpresa = listMultiEmpresa;
	}
	/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}
	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public int getModificaConsecutivoFacturasV() {
		return modificaConsecutivoFacturasV;
	}
	public void setModificaConsecutivoFacturasV(int modificaConsecutivoFacturasV) {
		this.modificaConsecutivoFacturasV = modificaConsecutivoFacturasV;
	}

	/**
	 * @param permiteModificarConsecutivoNotasPaciente the permiteModificarConsecutivoNotasPaciente to set
	 */
	public void setPermiteModificarConsecutivoNotasPaciente(
			boolean permiteModificarConsecutivoNotasPaciente) {
		this.permiteModificarConsecutivoNotasPaciente = permiteModificarConsecutivoNotasPaciente;
	}

	/**
	 * @return the permiteModificarConsecutivoNotasPaciente
	 */
	public boolean isPermiteModificarConsecutivoNotasPaciente() {
		return permiteModificarConsecutivoNotasPaciente;
	}

	/**
	 * @param existenNotasPaciente the existenNotasPaciente to set
	 */
	public void setExistenNotasPaciente(boolean existenNotasPaciente) {
		this.existenNotasPaciente = existenNotasPaciente;
	}

	/**
	 * @return the existenNotasPaciente
	 */
	public boolean isExistenNotasPaciente() {
		return existenNotasPaciente;
	}
}