/*
 * @(#)ConsultaPresupuestoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.presupuesto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ConsultaPresupuestoDao;


/**
 *  Objeto que maneja la interacción entre la capa
 *  de control y el acceso a la información de la 
 *  Consulta /  Impresióin de Presupuesto. 
 *  @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 17 /Ene/ 2006
 */
public class ConsultaPresupuesto
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaPresupuesto.class);
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Entero con el codigo de presupuesto con el que se trabaja
	 */
	private int codigoPresupuesto;
	
	/**
	 * Descripcion del Servicio
	 */
	private String  descripcionServicio;
	
	/**
	 * Fecha Y Hora de elaboracion De la Factura
	 */
	private String fechaHoraElaboracion;
	
	/**
	 * almacena los datos de la consulta para la seccion de servicios
	 */
	private HashMap mapaServicios;
	
	/**
	 * almacena los datos de la consulta para la seccion de articulos
	 */
	private HashMap mapaArticulos;
	
	/**
	 * almacena los datos de la consulta de un presupuesto en especifico
	 */
	private HashMap mapaDetallePresupuesto;
	
	/**
	 * Mapa para los tipos de Id
	 */
	private HashMap mapaTipoId;
	
	/**
	 * Mapa para los medicos tratantes
	 */
	private HashMap mapaMedicos;
	
	/**
	 * almacena el numero de filas en el HashMap mapaActualizacion
	 */
	private int numeroElementos;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * String del responsable de la factura
	 */
	private String responsable;
	
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;

          
     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
      * Mapa para los presupuestos en la busqueda Avanzada
      */
     private HashMap mapaPresupuestos;
     
     /**
      * String de la fecha inicial para la busqueda de Presupuestos
      */
     private String fechaElaboracionInicial;
     
     /**
      * String de la fehca final para la busqueda de Presupuestos
      */
     private String fechaElaboracionFinal;
     
     /**
      * int para el numero de presupuesto en la busqueda de Presupuestos
      */
     private int presupuestoInicial;
     
     /**
      * int para el numero de presupuestoFinal de la busqueda de Presupuestos
      */
     private int presupuestoFinal;
     
          
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempResponsable;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempTipoId;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempMedico;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * Entero para el codigo del medico a buscar
 	 */
 	private int hcodigoMedico;
 	
 	/**
 	 * String para el tipo de Id de paciente a buscar
 	 */
 	private String hTipoId;
 	
 	
 	
 	/**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaPresupuesto()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultaPresupuestoDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaPresupuestoDao consultaPresupuestoDao ;

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			consultaPresupuestoDao = myFactory.getConsultaPresupuestoDao();
			wasInited = (consultaPresupuestoDao != null);
		}

		return wasInited;
	}
	
	
	
	/**
	 * @return Returns the codigoPresupuesto.
	 */
	public int getCodigoPresupuesto()
	{
		return codigoPresupuesto;
	}
	/**
	 * @param codigoPresupuesto The codigoPresupuesto to set.
	 */
	public void setCodigoPresupuesto(int codigoPresupuesto)
	{
		this.codigoPresupuesto=codigoPresupuesto;
	}
	/**
	 * @return Returns the hcodigoMedico.
	 */
	public int getHcodigoMedico()
	{
		return hcodigoMedico;
	}
	/**
	 * @param hcodigoMedico The hcodigoMedico to set.
	 */
	public void setHcodigoMedico(int hcodigoMedico)
	{
		this.hcodigoMedico=hcodigoMedico;
	}
	/**
	 * @return Returns the hTipoId.
	 */
	public String getHTipoId()
	{
		return hTipoId;
	}
	/**
	 * @param tipoId The hTipoId to set.
	 */
	public void setHTipoId(String tipoId)
	{
		hTipoId=tipoId;
	}
	/**
	 * @return Returns the propiedadTempMedico.
	 */
	public int getPropiedadTempMedico()
	{
		return propiedadTempMedico;
	}
	/**
	 * @param propiedadTempMedico The propiedadTempMedico to set.
	 */
	public void setPropiedadTempMedico(int propiedadTempMedico)
	{
		this.propiedadTempMedico=propiedadTempMedico;
	}
	/**
	 * @return Returns the propiedadTempResponsable.
	 */
	public int getPropiedadTempResponsable()
	{
		return propiedadTempResponsable;
	}
	/**
	 * @param propiedadTempResponsable The propiedadTempResponsable to set.
	 */
	public void setPropiedadTempResponsable(int propiedadTempResponsable)
	{
		this.propiedadTempResponsable=propiedadTempResponsable;
	}
	/**
	 * @return Returns the propiedadTempTipoId.
	 */
	public int getPropiedadTempTipoId()
	{
		return propiedadTempTipoId;
	}
	/**
	 * @param propiedadTempTipoId The propiedadTempTipoId to set.
	 */
	public void setPropiedadTempTipoId(int propiedadTempTipoId)
	{
		this.propiedadTempTipoId=propiedadTempTipoId;
	}
	/**
	 * @return Returns the responsable.
	 */
	public String getResponsable()
	{
		return responsable;
	}
	/**
	 * @param responsable The responsable to set.
	 */
	public void setResponsable(String responsable)
	{
		this.responsable= responsable;
	}
	
	/**
	 * @return Returns the presupuestoFinal.
	 */
	public int getPresupuestoFinal()
	{
		return presupuestoFinal;
	}
	/**
	 * @param presupuestoFinal The presupuestoFinal to set.
	 */
	public void setPresupuestoFinal(int presupuestoFinal)
	{
		this.presupuestoFinal=presupuestoFinal;
	}
	/**
	 * @return Returns the presupuestoInicial.
	 */
	public int getPresupuestoInicial()
	{
		return presupuestoInicial;
	}
	/**
	 * @param presupuestoInicial The presupuestoInicial to set.
	 */
	public void setPresupuestoInicial(int presupuestoInicial)
	{
		this.presupuestoInicial= presupuestoInicial;
	}
	/**
	 * @return Returns the fechaElaboracionFinal.
	 */
	public String getFechaElaboracionFinal()
	{
		return fechaElaboracionFinal;
	}
	/**
	 * @param fechaElaboracionFinal The fechaElaboracionFinal to set.
	 */
	public void setFechaElaboracionFinal(String fechaElaboracionFinal)
	{
		this.fechaElaboracionFinal= fechaElaboracionFinal;
	}
	/**
	 * @return Returns the fechaElaboracionInicial.
	 */
	public String getFechaElaboracionInicial()
	{
		return fechaElaboracionInicial;
	}
	/**
	 * @param fechaElaboracionInicial The fechaElaboracionInicial to set.
	 */
	public void setFechaElaboracionInicial(String fechaElaboracionInicial)
	{
		this.fechaElaboracionInicial= fechaElaboracionInicial;
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
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio= descripcionServicio;
	}
	
	/**
	 * @return Returns the fechaHoraElaboracion
	 */
	public String getFechaHoraElaboracion()
	{
		return fechaHoraElaboracion;
	}
	/**
	 * @param fechaHoraElaboracion The fechaSolicitud to set.
	 */
	public void setFechaHoraElaboracion(String fechaHoraElaboracion)
	{
		this.fechaHoraElaboracion= fechaHoraElaboracion;
	}
	
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente= linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset= offset;
	}
	
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
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
	 * @return Returns the mapaDetallePresupuesto.
	 */
	public HashMap getMapaDetallePresupuesto()
	{
		return mapaDetallePresupuesto;
	}
	
	/**
	 * @param mapaDetallePresupuesto The mapaDetallePresupuesto to set.
	 */
	public void setMapaDetallePresupuesto(HashMap mapaDetallePresupuesto)
	{
		this.mapaDetallePresupuesto= mapaDetallePresupuesto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetallePresupuesto(String key, Object value) 
	{
		mapaDetallePresupuesto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetallePresupuesto(String key) 
	{
		return mapaDetallePresupuesto.get(key);
	}
	
	/**
	 * @return Returns the mapaTipoId.
	 */
	public HashMap getMapaTipoId()
	{
		return mapaTipoId;
	}
	
	/**
	 * @param mapaTipoId The mapaTipoId to set.
	 */
	public void setMapaTipoId(HashMap mapaTipoId)
	{
		this.mapaTipoId= mapaTipoId;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTipoId(String key, Object value) 
	{
		mapaTipoId.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTipoId(String key) 
	{
		return mapaTipoId.get(key);
	}
	
	/**
	 * @return Returns the mapaMedicos.
	 */
	public HashMap getMapaMedicos()
	{
		return mapaMedicos;
	}
	
	/**
	 * @param mapaMedicos The mapaMedicos to set.
	 */
	public void setMapaMedicos(HashMap mapaMedicos)
	{
		this.mapaMedicos= mapaMedicos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaMedicos(String key, Object value) 
	{
		mapaMedicos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaMedicos(String key) 
	{
		return mapaMedicos.get(key);
	}
	
	/**
	 * @return Returns the mapaServicios.
	 */
	public HashMap getMapaServicios()
	{
		return mapaServicios;
	}
	
	/**
	 * @param mapaServicios The mapaServicios to set.
	 */
	public void setMapaServicios(HashMap mapaServicios)
	{
		this.mapaServicios= mapaServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaServicios(String key, Object value) 
	{
		mapaServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaServicios(String key) 
	{
		return mapaServicios.get(key);
	}
	
	/**
	 * @return Returns the mapaArticulos.
	 */
	public HashMap getMapaArticulos()
	{
		return mapaArticulos;
	}
	
	/**
	 * @param mapaArticulos The mapaArticulos to set.
	 */
	public void setMapaArticulos(HashMap mapaArticulos)
	{
		this.mapaArticulos= mapaArticulos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaArticulos(String key, Object value) 
	{
		mapaArticulos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaArticulos(String key) 
	{
		return mapaArticulos.get(key);
	}
	
	/**
	 * @return Returns the numeroElementos.
	 */
	public int getNumeroElementos()
	{
		return numeroElementos;
	}
	/**
	 * @param numeroElementos The numeroElementos to set.
	 */
	public void setNumeroElementos(int numeroElementos)
	{
		this.numeroElementos= numeroElementos;
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
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	/**
	 * @return Returns the mapaPresupuestos.
	 */
	public HashMap getMapaPresupuestos()
	{
		return mapaPresupuestos;
	}
	
	/**
	 * @param mapaPresupuestos The mapaPresupuestos to set.
	 */
	public void setMapaPresupuestos(HashMap mapaPresupuestos)
	{
		this.mapaPresupuestos= mapaPresupuestos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPresupuestos(String key, Object value) 
	{
		mapaPresupuestos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPresupuestos(String key) 
	{
		return mapaPresupuestos.get(key);
	}
	
	
	/**************************************************************************************
	 *						     IMPLEMETACION DE METODOS								  *
	**************************************************************************************/
	
	/**
	 * Método para consultar el detalle básico de un presupuesto
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetallePresupuesto(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return consultaPresupuestoDao.consultarDetallePresupuesto(con, codigoPresupuesto);
	}
	
	/**
	 * Método para buscar un presupuesto segun unos parametros dados
	 * @param con
	 * @param presupuestoInicial
	 * @param presupuestoFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param tipoId
	 * @param numeroId
	 * @param codigoMedico
	 * @param responsable
	 * @return
	 */
	public HashMap busquedaPresupuestos (Connection con,int presupuestoInicial, int presupuestoFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, String tipoId, int numeroId, int codigoMedico, int responsable)  throws SQLException
	{
		return consultaPresupuestoDao.busquedaPresupuestos(con, presupuestoInicial, presupuestoFinal, fechaElaboracionIncial, fechaElaboracionFinal, tipoId, numeroId, codigoMedico, responsable);
	}
	
	
	/**
	 * Método para consultar los Articulos relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarArticulos(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return consultaPresupuestoDao.consultarArticulos(con, codigoPresupuesto);
	}
	
	/**
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarArticulosMedIns(Connection con, int codigoPresupuesto, boolean dividirMedIns)  throws SQLException
	{
		return consultaPresupuestoDao.consultarArticulosMedIns(con, codigoPresupuesto, dividirMedIns);
	}
	
	/**
	 * Método para consultar los servicios relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarServicios(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return consultaPresupuestoDao.consultarServicios(con, codigoPresupuesto);
	}
	
	/**
	 * Método para consultar las intervenciones (servicio - especialidad) de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarIntenervenciones(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return consultaPresupuestoDao.consultarIntenervenciones(con, codigoPresupuesto);
	}
	
	/**
	 * Método apra consultar ñlos tipos de identificacion existentes en el sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarTiposId(Connection con)  throws SQLException
	{
		return consultaPresupuestoDao.consultarTiposId(con);
	}
	
	/**
	 * Metodo para consultar los profesionales de la salud que
	 * su ocupacion medica sea estrictamente "MEDICO"
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMedicos(Connection con, int codigoOcupMedicoGeneral, int codigoOcupMedicoEspecialista)  throws SQLException
	{
		return consultaPresupuestoDao.consultarMedicos(con, codigoOcupMedicoGeneral, codigoOcupMedicoEspecialista);
	}
}
