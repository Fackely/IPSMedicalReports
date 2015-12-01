/*
 * @(#)FormatoImpresionPresupuestoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.presupuesto;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadCadena;
import util.UtilidadTexto;



/**
 * Forma para manejo presentación de la funcionalidad 
 * parametrica de Formato Impresión Presupuesto. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 10 /Ene/ 2006
 */
public class FormatoImpresionPresupuestoForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * Codigo del formato de impresión de presupuesto
	 */
	private int codigoFormato;
	
	/**
	 * String con el nombre del formato de impresión
	 */
	private String nombreFormato;

	/**
	 * Titulo de la impresión
	 */
	private String tituloFormato;
	
	/**
	 * Descripcion de la cantidad
	 */
	private String descripcionCantidad;
	
	/**
	 * Descripcion del valor unitario
	 */
	private String descripcionValUnitario;
	
	/**
	 * Descripcion del valor total
	 */
	private String descripcionValTotal;
	
	/**
	 * Entero que indica la prioridad de los servicios
	 */
	private int prioridadServicios;
	
	/**
	 * Entero que indica la prioridad de los articulos
	 */
	private int prioridadArticulos;
	
	/**
	 * Descripcion de la seccion de articulos
	 */
	private String descripcionSecArticulo;
	
	/**
	 * Nivel del detalle de articulos
	 */
	private String nivel;
	
	/**
	 * String con la nota de pie de pagina parametrizada
	 */
	private String piePagina;
	
	/**
	 * Almacena los datos del formato de impresion basico
	 */
	private HashMap mapaFormatoImpresionBasico;

    /**
    * Poscicion del mapa en la consulta de facturas
    */
    private int posicionMapa;
    
    /**
     * Nombre del grupo de servicios
     */
    private String grupoServicios;
    
    /**
     * Alamancena los formatos existentes
     */
    private HashMap mapaFormatosPrevios;
    
    /**
     * Alamacena el detalle de los servicios
     */
    private HashMap mapaDetServicios;
	
    /**
     * Almacena el detalle de los servicios
     */
    private HashMap mapaDetArticulos;
    
    /**
     * boolean para el campo de chequeo de la cantidad
     */
    private boolean cantidad;
    
    /**
     * boolean para el campo de chequeo para el valor unitario
     */
    private boolean valorUnitario;
    
    /**
     * boolean para el campo de chequeo del valor total
     */
    private boolean valorTotal;
    
    /**
     * boolean para el campo de chequeo de Mostrar detalle? en la seccion de servicios
     */
    private boolean detServicio;
    
    /**
     * boolean para el campo de chueuqeo MOstrar Valores Detalle? en la seccion de servicios
     */
    private boolean valDetServicio;
    
    /**
     * boolean para el campo de chequeo
     */
    private boolean subTotalGrupo;
    
    /**
     * boolean para el campo de chequeo de Mostrar Detalle? en la seccion de articulos
     */
    private boolean detNivel;
    
    /**
     * boolean para el campo de chequeo de Mostrar Valores Detalle? en la seccion de articulos
     */
    private boolean valDetArticulo;
    
    /**
     * boolean para el campon de chequeo de Mostrar Subtotal del Nivel? en la seccion de articulos
     */
    private boolean subTotalNivel;
    
    /**
     * boolean para el campo de chequeo de Imprimir Fecha Hora en el pie de Pagina?
     */
    private boolean fechaHora;
    
    
    /**
     * String para almacenar los mejsaes de transacciones exitosas
     */
    private String mensajeExitoso="";
    
    /**
     * Mapa para los grupos de servicios
     */
    private HashMap mapaGruposServicios;
    
    /**
     * Entero para manejar el codigo del grupo a adicionar
     */
    private int codigoGrupo;
    
    /**
     * Mapa auxiliar con la informacion basica del formato de impresión
     */
    private HashMap mapaAuxBasico;
    
    /**
     * Mapa auxiliar con la información del detalle de articulos de un formato de impresión
     */
    private HashMap mapaAuxDetArt;
    
    /**
     * Mapa Auxiliar con la información del detalle de servicios de un formato de impresión
     */
    private HashMap mapaAuxDetServ;
    
    /**
     * Indica si se muestra en un solo item Medicamentos y Articulos
     */
    private String medicamentosArticulos;
    
    /**
     * Reset de toda la forma
     */
	public void reset ()
	{
		this.mapaFormatoImpresionBasico = new HashMap();
		this.mapaFormatosPrevios= new HashMap();
		this.mapaDetServicios = new HashMap();
		this.mapaDetArticulos = new HashMap();
		this.mapaGruposServicios = new HashMap();
		this.mapaAuxBasico = new HashMap();
		this.mapaAuxDetArt = new HashMap();
		this.mapaAuxDetServ = new HashMap();
		this.estado="";
		this.codigoFormato=0;
		this.descripcionCantidad="";
		this.descripcionSecArticulo="";
		this.descripcionValTotal="";
		this.descripcionValUnitario="";
		this.nivel="";
		this.nombreFormato="";
		this.piePagina="";
		this.posicionMapa=0;
		this.prioridadArticulos=0;
		this.prioridadServicios=0;
		this.tituloFormato="";
		this.mensajeExitoso="";
		this.codigoGrupo=0;
		this.cantidad=false;
		this.valorUnitario=false;
		this.valorTotal=false;
		this.fechaHora=false;
		this.medicamentosArticulos="";
	}
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaFormatoImpresionBasico = new HashMap ();
		this.mapaFormatosPrevios = new HashMap();
		this.mapaDetServicios = new HashMap();
		this.mapaDetArticulos = new HashMap();
		this.mapaGruposServicios = new HashMap ();
		this.mapaAuxBasico = new HashMap();
		this.mapaAuxDetArt = new HashMap();
		this.mapaAuxDetServ = new HashMap();
	}
	
	
	
	
	/**
	 * @return Returns the codigoGrupo.
	 */
	public int getCodigoGrupo()
	{
		return codigoGrupo;
	}
	/**
	 * @param codigoGrupo The codigoGrupo to set.
	 */
	public void setCodigoGrupo(int codigoGrupo)
	{
		this.codigoGrupo=codigoGrupo;
	}
	
	/**
	 * @return Returns the mensajeExitoso.
	 */
	public String getMensajeExitoso()
	{
		return mensajeExitoso;
	}
	/**
	 * @param mensajeExitoso The mensajeExitoso to set.
	 */
	public void setMensajeExitoso(String mensajeExitoso)
	{
		this.mensajeExitoso=mensajeExitoso;
	}
	/**
	 * @return Returns the cantidad.
	 */
	public boolean isCantidad()
	{
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(boolean cantidad)
	{
		this.cantidad=cantidad;
	}
	/**
	 * @return Returns the detNivel.
	 */
	public boolean isDetNivel()
	{
		return detNivel;
	}
	/**
	 * @param detNivel The detNivel to set.
	 */
	public void setDetNivel(boolean detNivel)
	{
		this.detNivel=detNivel;
	}
	/**
	 * @return Returns the detServicio.
	 */
	public boolean isDetServicio()
	{
		return detServicio;
	}
	/**
	 * @param detServicio The detServicio to set.
	 */
	public void setDetServicio(boolean detServicio)
	{
		this.detServicio=detServicio;
	}
	/**
	 * @return Returns the fechaHora.
	 */
	public boolean isFechaHora()
	{
		return fechaHora;
	}
	/**
	 * @param fechaHora The fechaHora to set.
	 */
	public void setFechaHora(boolean fechaHora)
	{
		this.fechaHora=fechaHora;
	}
	/**
	 * @return Returns the subTotalGrupo.
	 */
	public boolean isSubTotalGrupo()
	{
		return subTotalGrupo;
	}
	/**
	 * @param subTotalGrupo The subTotalGrupo to set.
	 */
	public void setSubTotalGrupo(boolean subTotalGrupo)
	{
		this.subTotalGrupo=subTotalGrupo;
	}
	/**
	 * @return Returns the subTotalNivel.
	 */
	public boolean isSubTotalNivel()
	{
		return subTotalNivel;
	}
	/**
	 * @param subTotalNivel The subTotalNivel to set.
	 */
	public void setSubTotalNivel(boolean subTotalNivel)
	{
		this.subTotalNivel=subTotalNivel;
	}
	/**
	 * @return Returns the valDetArticulo.
	 */
	public boolean isValDetArticulo()
	{
		return valDetArticulo;
	}
	/**
	 * @param valDetArticulo The valDetArticulo to set.
	 */
	public void setValDetArticulo(boolean valDetArticulo)
	{
		this.valDetArticulo=valDetArticulo;
	}
	/**
	 * @return Returns the valDetServicio.
	 */
	public boolean isValDetServicio()
	{
		return valDetServicio;
	}
	/**
	 * @param valDetServicio The valDetServicio to set.
	 */
	public void setValDetServicio(boolean valDetServicio)
	{
		this.valDetServicio=valDetServicio;
	}
	/**
	 * @return Returns the valorTotal.
	 */
	public boolean isValorTotal()
	{
		return valorTotal;
	}
	/**
	 * @param valorTotal The valorTotal to set.
	 */
	public void setValorTotal(boolean valorTotal)
	{
		this.valorTotal=valorTotal;
	}
	/**
	 * @return Returns the valorUnitario.
	 */
	public boolean isValorUnitario()
	{
		return valorUnitario;
	}
	/**
	 * @param valorUnitario The valorUnitario to set.
	 */
	public void setValorUnitario(boolean valorUnitario)
	{
		this.valorUnitario=valorUnitario;
	}
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    /**
	 * @return Returns the mapaFormatoImpresionBasico.
	 */
	public HashMap getMapaFormatoImpresionBasico()
	{
		return mapaFormatoImpresionBasico;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatoImpresionBasico(String key, Object value) 
	{
		mapaFormatoImpresionBasico.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatoImpresionBasico(String key) 
	{
		return mapaFormatoImpresionBasico.get(key);
	}
	
	/**
	 * @param mapaFormatoImpresionBasico The mapaFormatoImpresionBasico to set.
	 */
	public void setMapaFormatoImpresionBasico(HashMap mapaFormatoImpresionBasico)
	{
		this.mapaFormatoImpresionBasico= mapaFormatoImpresionBasico;
	}
	
	/**
	 * @return Returns the codigoFormato.
	 */
	public int getCodigoFormato()
	{
		return codigoFormato;
	}
	/**
	 * @param codigoFormato The codigoFormato to set.
	 */
	public void setCodigoFormato(int codigoFormato)
	{
		this.codigoFormato=codigoFormato;
	}
	/**
	 * @return Returns the descripcionCantidad.
	 */
	public String getDescripcionCantidad()
	{
		return descripcionCantidad;
	}
	/**
	 * @param descripcionCantidad The descripcionCantidad to set.
	 */
	public void setDescripcionCantidad(String descripcionCantidad)
	{
		this.descripcionCantidad=descripcionCantidad;
	}
	/**
	 * @return Returns the descripcionSecArticulo.
	 */
	public String getDescripcionSecArticulo()
	{
		return descripcionSecArticulo;
	}
	/**
	 * @param descripcionSecArticulo The descripcionSecArticulo to set.
	 */
	public void setDescripcionSecArticulo(String descripcionSecArticulo)
	{
		this.descripcionSecArticulo=descripcionSecArticulo;
	}
	/**
	 * @return Returns the descripcionValTotal.
	 */
	public String getDescripcionValTotal()
	{
		return descripcionValTotal;
	}
	/**
	 * @param descripcionValTotal The descripcionValTotal to set.
	 */
	public void setDescripcionValTotal(String descripcionValTotal)
	{
		this.descripcionValTotal=descripcionValTotal;
	}
	/**
	 * @return Returns the descripcionValUnitario.
	 */
	public String getDescripcionValUnitario()
	{
		return descripcionValUnitario;
	}
	/**
	 * @param descripcionValUnitario The descripcionValUnitario to set.
	 */
	public void setDescripcionValUnitario(String descripcionValUnitario)
	{
		this.descripcionValUnitario=descripcionValUnitario;
	}
	/**
	 * @return Returns the grupoServicios.
	 */
	public String getGrupoServicios()
	{
		return grupoServicios;
	}
	/**
	 * @param grupoServicios The grupoServicios to set.
	 */
	public void setGrupoServicios(String grupoServicios)
	{
		this.grupoServicios=grupoServicios;
	}
	/**
	 * @return Returns the nivel.
	 */
	public String getNivel()
	{
		return nivel;
	}
	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(String nivel)
	{
		this.nivel=nivel;
	}
	/**
	 * @return Returns the nombreFormato.
	 */
	public String getNombreFormato()
	{
		return nombreFormato;
	}
	/**
	 * @param nombreFormato The nombreFormato to set.
	 */
	public void setNombreFormato(String nombreFormato)
	{
		this.nombreFormato=nombreFormato;
	}
	/**
	 * @return Returns the piePagina.
	 */
	public String getPiePagina()
	{
		return piePagina;
	}
	/**
	 * @param piePagina The piePagina to set.
	 */
	public void setPiePagina(String piePagina)
	{
		this.piePagina=piePagina;
	}
	/**
	 * @return Returns the prioridadArticulos.
	 */
	public int getPrioridadArticulos()
	{
		return prioridadArticulos;
	}
	/**
	 * @param prioridadArticulos The prioridadArticulos to set.
	 */
	public void setPrioridadArticulos(int prioridadArticulos)
	{
		this.prioridadArticulos=prioridadArticulos;
	}
	/**
	 * @return Returns the prioridadServicios.
	 */
	public int getPrioridadServicios()
	{
		return prioridadServicios;
	}
	/**
	 * @param prioridadServicios The prioridadServicios to set.
	 */
	public void setPrioridadServicios(int prioridadServicios)
	{
		this.prioridadServicios=prioridadServicios;
	}
	/**
	 * @return Returns the tituloFormato.
	 */
	public String getTituloFormato()
	{
		return tituloFormato;
	}
	/**
	 * @param tituloFormato The tituloFormato to set.
	 */
	public void setTituloFormato(String tituloFormato)
	{
		this.tituloFormato=tituloFormato;
	}
	
	/**
	 * @return Returns the mapaFormatosPrevios.
	 */
	public HashMap getMapaFormatosPrevios()
	{
		return mapaFormatosPrevios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatosPrevios(String key, Object value) 
	{
		mapaFormatosPrevios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatosPrevios(String key) 
	{
		return mapaFormatosPrevios.get(key);
	}
	
	/**
	 * @param mapaFormatosPrevios The mapaFormatosPrevios to set.
	 */
	public void setMapaFormatosPrevios(HashMap mapaFormatosPrevios)
	{
		this.mapaFormatosPrevios= mapaFormatosPrevios;
	}
	
	/**
	 * @return Returns the mapaDetServicios.
	 */
	public HashMap getMapaDetServicios()
	{
		return mapaDetServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetServicios(String key, Object value) 
	{
		mapaDetServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetServicios(String key) 
	{
		return mapaDetServicios.get(key);
	}
	
	/**
	 * @param mapaDetServicios The mapaDetServicios to set.
	 */
	public void setMapaDetServicios(HashMap mapaDetServicios)
	{
		this.mapaDetServicios= mapaDetServicios;
	}
	
	/**
	 * @return Returns the mapaDetArticulos.
	 */
	public HashMap getMapaDetArticulos()
	{
		return mapaDetArticulos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetArticulos(String key, Object value) 
	{
		mapaDetArticulos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetArticulos(String key) 
	{
		return mapaDetArticulos.get(key);
	}
	
	/**
	 * @param mapaDetArticulos The mapaDetArticulos to set.
	 */
	public void setMapaDetArticulos(HashMap mapaDetArticulos)
	{
		this.mapaDetArticulos= mapaDetArticulos;
	}
	
	
	
	/**
	 * @return Returns the mapaGruposServicios.
	 */
	public HashMap getMapaGruposServicios()
	{
		return mapaGruposServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaGruposServicios(String key, Object value) 
	{
		mapaGruposServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaGruposServicios(String key) 
	{
		return mapaGruposServicios.get(key);
	}
	
	/**
	 * @param mapaGruposServicios The mapaGruposServicios to set.
	 */
	public void setMapaGruposServicios(HashMap mapaGruposServicios)
	{
		this.mapaGruposServicios= mapaGruposServicios;
	}
	
	/**
	 * @return Returns the mapaAuxBasico.
	 */
	public HashMap getMapaAuxBasico()
	{
		return mapaAuxBasico;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaAuxBasico(String key, Object value) 
	{
		mapaAuxBasico.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaAuxBasico(String key) 
	{
		return mapaAuxBasico.get(key);
	}
	
	/**
	 * @param mapaAuxBasico The mapaAuxBasico to set.
	 */
	public void setMapaAuxBasico(HashMap mapaAuxBasico)
	{
		this.mapaAuxBasico= mapaAuxBasico;
	}
	
	/**
	 * @return Returns the mapaAuxDetArt.
	 */
	public HashMap getMapaAuxDetArt()
	{
		return mapaAuxDetArt;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaAuxDetArt(String key, Object value) 
	{
		mapaAuxDetArt.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaAuxDetArt(String key) 
	{
		return mapaAuxDetArt.get(key);
	}
	
	/**
	 * @param mapaAuxDetArt The mapaAuxDetArt to set.
	 */
	public void setMapaAuxDetArt(HashMap mapaAuxDetArt)
	{
		this.mapaAuxDetArt= mapaAuxDetArt;
	}
	
	/**
	 * @return Returns the mapaAuxDeServ.
	 */
	public HashMap getMapaAuxDetServ()
	{
		return mapaAuxDetServ;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaAuxDetServ(String key, Object value) 
	{
		mapaAuxDetServ.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaAuxDetServ(String key) 
	{
		return mapaAuxDetServ.get(key);
	}
	
	/**
	 * @param mapaAuxDetServ The mapaAuxDetServ to set.
	 */
	public void setMapaAuxDetServ(HashMap mapaAuxDetServ)
	{
		this.mapaAuxDetServ= mapaAuxDetServ;
	}
	
	/**
	 * @return the medicamentosArticulos
	 */
	public String getMedicamentosArticulos() {
		return medicamentosArticulos;
	}

	/**
	 * @param medicamentosArticulos the medicamentosArticulos to set
	 */
	public void setMedicamentosArticulos(String medicamentosArticulos) {
		this.medicamentosArticulos = medicamentosArticulos;
	}
	
	/**
	 * Metodo para revisar si hay prioridades duplicadas o no secuenciales
	 * @param errores
	 * @param mapaTemporal
	 * @param prioridadArticulos
	 * @return
	 */
	private ActionErrors verificarPrioridadesDuplicadas(ActionErrors errores, HashMap mapaTemporal, int prioridadArticulos) 
	{
		int contador=Integer.parseInt(mapaTemporal.get("numRegistros").toString());
		String prioridades[]=new String[contador+1];
		String aux="";
		int tamano=0;
		/**Se asignan las prioridades a un vector temporal**/
		for(int i=0;i<contador;i++)
		{
			aux=mapaTemporal.get("prioridad_"+i)+"";
			/**if(tamano>1)
			{
				if(aux==prioridades[(tamano-1)])
				{
					errores.add("prioridades sin orden secuencial", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.prioridadesNoSecuenciales"));
					i=contador;
				}
			}**/
			prioridades[tamano]=aux;
			tamano++;
		}
		for (int k = 0 ; k < tamano ; k++)
		{
			if(prioridades[k].equals(prioridadArticulos+""))
			{
				errores.add("prioridades sin orden secuencial", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.prioridadesNoSecuenciales"));
				k=tamano;
			}
		}
		prioridades[contador]=prioridadArticulos+"";
		String aux1 = "";
		
		/**Se recorre ciclo de comparación**/
		for(int i = 0 ; i < tamano ; i++)
		{
			for(int j = 0 ; j < i ; j++)
			{
				/**Se revisa si hay prioridades iguales**/
				if(Integer.parseInt(prioridades[i])<Integer.parseInt(prioridades[j]))
				{
					aux1=prioridades[j];
					prioridades[j]=prioridades[i];
					prioridades[i]=aux1;
				}
			}
		}
		
		/**Ordenamiento Burbuja**/
		aux1 = "";
		for (int k = 0 ; k < prioridades.length-1 ; k++)
		{
			for(int j = prioridades.length-1 ; j > k ; j--)
			{
				if(Integer.parseInt(prioridades[k]) > Integer.parseInt(prioridades[j]))
				{
					aux1 = prioridades[j];
					prioridades[j] = prioridades[k];
					prioridades[k] = aux1;
				}
			}
		}
		
		int temporal=1;
		
		/**Se recorre ciclo de comparación**/
		for(int i=0;i<(tamano);i++)
		{
			/**
			 * Con el vector ya organizado de forma ascendente si voy recorriendo y lo que hay en la posicion[i]
			 * no es igual al temporal, eso quiere decir que hay repetida o que no es secuencial
			 */
			if(Integer.parseInt(prioridades[i])!=temporal)
			{
				errores.add("prioridades sin orden secuencial", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.prioridadesNoSecuenciales"));
				i=tamano;
			}
			temporal++;
			
		}
		return errores;
	}
	
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		/*********************************************************************************/
		if(estado.equals("guardarNuevo") || estado.equals("guardarModificacion"))
		{
			if(this.getNombreFormato().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required","El Nombre del Formato "));
			}
			if(this.getTituloFormato().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required","El Titulo del Formato "));
			}
			
			String temp=""+this.getMapaDetArticulos("prioridad_0").toString();
			if(temp.equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required","La prioridad de Artículos "));
			}
			else if(!temp.equals("")||temp!=null)
			{
				if(Integer.parseInt(temp)==0)
				{
					errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad para la Sección de Artículos ","0"));
				}
			}
			temp=this.getMapaDetArticulos("descseccionarticulo_0").toString();
			if(temp.equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required","La descripción de la Sección de Artículos "));
			}
			if(this.isCantidad()==true)
			{
				if(this.getDescripcionCantidad().trim().equals(""))
				{
					errores.add("errors.required", new ActionMessage("errors.required","La descripción de la Cantidad "));
				}
			}
			if(this.isValorUnitario()==true)
			{
				if(this.getDescripcionValUnitario().trim().equals(""))
				{
					errores.add("errors.required", new ActionMessage("errors.required","La descripción del Valor Unitario "));
				}
			}
			if(this.isValorTotal()==true)
			{
				if(this.getDescripcionValTotal().trim().equals(""))
				{
					errores.add("errors.required", new ActionMessage("errors.required","La descripción del Valor Total "));
				}
			}
			if((this.mapaDetArticulos.get("medicamentosarticulos_0")+"").trim().equals("") || (this.mapaDetArticulos.get("medicamentosarticulos_0")+"").trim().equals("null"))
			{
				errores.add("errors.required", new ActionMessage("errors.required","Mostrar en un solo ítem Medicamentos y Artículos "));
			}
			boolean noHayNull=false;
			String prioridad=this.getMapaDetArticulos("prioridad_0")+"";
			if(this.getMapaDetServicios().containsKey("numRegistros")&&!prioridad.equals(""))
			{
				if(Integer.parseInt(this.getMapaDetServicios("numRegistros").toString())>0)
				{
					for(int i = 0 ; i < Integer.parseInt(this.getMapaDetServicios("numRegistros").toString()) ; i++)
					{
						if(this.getMapaDetServicios("prioridad_"+i).toString().equals("")||this.getMapaDetServicios("prioridad_"+i)==null)
						{
							noHayNull=true;
							errores.add("errors.required", new ActionMessage("errors.required","La prioridad para "+this.getMapaDetServicios("grupo_"+i)));
						}
						else if(!this.getMapaDetServicios("prioridad_"+i).toString().equals("")||this.getMapaDetServicios("prioridad_"+i)!=null)
						{
							if(Integer.parseInt(this.getMapaDetServicios("prioridad_"+i).toString())==0)
							{
								noHayNull=true;
								errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad para "+this.getMapaDetServicios("grupo_"+i),"0"));
								
							}
							
						}
					}
				}
				if(!noHayNull)
				{
					int prioridadArt=Integer.parseInt(prioridad);
					errores=this.verificarPrioridadesDuplicadas(errores,this.getMapaDetServicios(), prioridadArt);
				}
			}
			
			//------Si seleccionaron Mostrar detalle de artículos se valida que hayan ingresado el nivel----//
			if(UtilidadTexto.getBoolean(mapaDetArticulos.get("detarticulo_0")+""))
			{
				if(!UtilidadCadena.noEsVacio(mapaDetArticulos.get("nivel_0")+""))
				{
					errores.add("errors.required", new ActionMessage("errors.required","El Nivel en el Detalle de artículos"));
				}
			}
			
		}
		if(estado.equals("guardarNuevo"))
		{
			for (int i=0; i < Integer.parseInt(this.getMapaFormatosPrevios("numRegistros")+""); i++)
			{
				if(this.getNombreFormato().trim().toLowerCase().equals(this.getMapaFormatosPrevios("nombreformato_"+i).toString().trim().toLowerCase()))
				{
					errores.add("error.manejoPaciente.formatoImpresionPresupuesto.nombreFormatoExistente", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.nombreFormatoExistente",this.getNombreFormato()));
				}
			}
		}
		return errores;
	}


	

}