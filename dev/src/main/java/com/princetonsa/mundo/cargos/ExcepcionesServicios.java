/*
 * @(#)ExcepcionesServicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionesServiciosDao;


/**
 * Mundo para el manejo de las excepciones de servicios
 * @version 1.0, Julio 21, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ExcepcionesServicios
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ExcepcionesServiciosDao excDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ExcepcionesServicios.class);
	
	/**
	 * Servicios
	 * Código - descripcion
	 */
	private InfoDatosInt servicio;
	
	/**
	 * Convenios
	 * Código - nombre
	 */
	private InfoDatosInt convenio;
	
	/**
	 * Contratos
	 * Código - numeroContrato
	 */
	private InfoDatosInt contrato;
	
	/**
	 * Servicio Nuevo (para la modificación)
	 * Código - Descripción
	 */
	private InfoDatosInt servicioNuevo;
	
	private int codigoEspecilidad;
	
	private String codigoAxioma;
	
	/**
	 * fecha de inicio y fin del contrato
	 */
	private String fechasVigenciaContrato;
	
	/**
	 * indica si es pos o nopos
	 */
	private String tipoPos;
	
	/**
	 * Aux para la busqueda avanzada del servicio
	 */
	private int esposAux;
	
	/**
	 * resetea los datos pertinentes 
	 */
	public void reset()
	{
		this.servicio= new InfoDatosInt();
		this.convenio= new InfoDatosInt();
		this.contrato=new InfoDatosInt();
		this.servicioNuevo= new InfoDatosInt();
		this.fechasVigenciaContrato="";
		this.codigoEspecilidad=-1;
		this.tipoPos="";
		this.esposAux=ConstantesBD.codigoNuncaValido;
	}	
	
	/**
	 * Constructor de la clase 
	 * @param servicio
	 * @param convenio
	 * @param contrato
	 */
	public ExcepcionesServicios(InfoDatosInt servicio, InfoDatosInt convenio, InfoDatosInt contrato	)
	{
		this.servicio=servicio;
		this.convenio=convenio;
		this.contrato=contrato;
		this.init (System.getProperty("TIPOBD"));																	
	}	
	
	/**
	* Constructor de la clase, inicializa en vacio todos los parámetros
	*/		
	public ExcepcionesServicios()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
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
				excDao = myFactory.getExcepcionesServiciosDao();
				wasInited = (excDao != null);
			}
			return wasInited;
	}

	/**
	 * Método para insertar una excepción de servicio
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarExc(Connection con) throws SQLException 
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (excDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionesTarifasDao - insertarExc )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=excDao.insertar(con,servicio.getCodigo(),contrato.getCodigo());
		
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}

	/**
	 * Método para cargar el resumen
	 * @param con
	 * @param servicio
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int servicio, int contrato) throws SQLException
	{
		ResultSetDecorator rs=excDao.cargarResumen(con,servicio, contrato);
		
		if (rs.next())
		{
			this.servicio= new InfoDatosInt(rs.getInt("codigoServicio"), rs.getString("descripcionServicio"));
			this.convenio= new InfoDatosInt(rs.getInt("codigoConvenio"), rs.getString("nombreConvenio"));
			this.contrato= new InfoDatosInt(rs.getInt("codigoContrato"), rs.getString("numeroContrato"));
			this.codigoEspecilidad=rs.getInt("especialidad");
			this.codigoAxioma=rs.getString("codigoAxioma");
			this.fechasVigenciaContrato=rs.getString("fechaInicial")+"  -  "+rs.getString("fechaFinal");
			this.tipoPos=rs.getString("espos");
			
			return true;
		}
		else
			return false;
	}

	public boolean hayRepetidas(Connection con) throws SQLException
	{
		ResultSetDecorator rs=excDao.hayRepetidas(con,servicio.getCodigo(),contrato.getCodigo());
	
		if (rs.next())
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Método utilizado en la funcionalidad Modificar 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarExc(Connection con) throws SQLException 
	{
			int resp=0;
			resp=excDao.modificar(con, this.getCodigoServicioNuevo(), this.getCodigoContrato(), this.getCodigoServicio());
			return resp;
	}
	
	/**
	 * Método utilizado para borrar una excepción de servicio
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int eliminar(Connection con) throws SQLException 
	{
			int resp=0;
			resp=excDao.eliminar(con,this.getCodigoServicio(), this.getCodigoContrato());
			return resp;
	}
	

	/**
	 * Actualiza los datos del objeto en una fuente de datos, reutilizando una conexión existente,
	 * con la información presente en los atributos del objeto.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return número de filas insertadas (1 o 0)
	 */
	public int modificarExcTransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int resp1=0;
		if (estado==null)
		{
		    myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (ExcepcionesServicios - modificarExcTransaccional ) no esta especificado");
		}
		if (excDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ExcepcionesServicios - modificarExcTransaccional )");
		}
				//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		resp1=excDao.modificar(con,this.getCodigoServicioNuevo(), this.getCodigoContrato(), this.getCodigoServicio());
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
			    myFactory.endTransaction(con);
			}
		}
		return resp1;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla excepciones_servicios
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoExc(Connection con)
	{
		excDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesServiciosDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(excDao.listado(con));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo exc servicio " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}

	/**
	 * Método que contiene los resultados de la búsqueda de excepciones servicios,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con)
	{
		excDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesServiciosDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(excDao.busqueda(con,this.getNombreConvenio(), this.getDescripcionServicio(), this.getNumeroContrato(), this.getEsposAux()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo exc servicios " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}

	/**
	 * Returns the servicio
	 * @return
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * Sets the servicio
	 * @param s1
	 */
	public void setServicio(InfoDatosInt s1) {
		servicio = s1;
	}
	
	/**
	 * Retorna el código del servicio 
	 * @return
	 */
	public int getCodigoServicio(){
		return servicio.getCodigo();
	}

	/**
	 * Retorna la descripcion del servicio
	 * @return
	 */
	public String  getDescripcionServicio(){
		return servicio.getNombre();
	}
	
	/**
	 * Asigna el codigo del servicio
	 * @param codigoServicio
	*/
	public void setCodigoServicio(int codigoServicio){
		this.servicio.setCodigo(codigoServicio);
	}
	
	/**
	 * Asigna la descripción del servicio
	 * @param descripcionServicio
	 */
	public void setDescripcionServicio(String descripcionServicio){
		this.servicio.setNombre(descripcionServicio);
	}

	/**
	 * Returns the convenio
	 * @return
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * Sets the convenio
	 * @param s1
	 */
	public void setConvenio(InfoDatosInt s1) {
		convenio = s1;
	}
	
	/**
	 * Retorna el código del convenio 
	 * @return
	 */
	public int getCodigoConvenio(){
		return convenio.getCodigo();
	}

	/**
	 * Retorna la nombre del convenio
	 * @return
	 */
	public String  getNombreConvenio(){
		return convenio.getNombre();
	}
	
	/**
	 * Asigna el codigo del convenio
	 * @param codigoConvenio
	*/
	public void setCodigoConvenio(int codigoConvenio){
		this.convenio.setCodigo(codigoConvenio);
	}

	/**
	 * Asigna el nombre del convenio
	 * @param nombreConvenio
	 */
	public void setNombreConvenio(String nombreConvenio){
		this.convenio.setNombre(nombreConvenio);
	}
	
	/**
	 * Returns the contrato
	 * @return
	 */
	public InfoDatosInt getContrato() {
		return contrato;
	}

	/**
	 * Sets the contrato
	 * @param s1
	 */
	public void setContrato(InfoDatosInt s1) {
		contrato = s1;
	}
	
	/**
	 * Retorna el código del contrato 
	 * @return
	 */
	public int getCodigoContrato(){
		return contrato.getCodigo();
	}

	/**
	 * Retorna la número del contrato
	 * @return
	 */
	public String  getNumeroContrato(){
		return contrato.getNombre();
	}
	
	/**
	 * Asigna el codigo del contrato
	 * @param codigoContrato
	*/
	public void setCodigoContrato(int codigoContrato){
		this.contrato.setCodigo(codigoContrato);
	}

	/**
	 * Asigna el numero del contrato
	 * @param numeroContrato
	 */
	public void setNumeroContrato(String numeroContrato){
		this.contrato.setNombre(numeroContrato);
	}
	
	/**
	 * Returns the servicio
	 * @return
	 */
	public InfoDatosInt getServicioNuevo() {
		return servicioNuevo;
	}

	/**
	 * Sets the servicioNuevo
	 * @param s1
	 */
	public void setServicioNuevo(InfoDatosInt s1) {
		servicioNuevo = s1;
	}
	
	/**
	 * Retorna el código del servicioNuevo 
	 * @return
	 */
	public int getCodigoServicioNuevo(){
		return servicioNuevo.getCodigo();
	}

	/**
	 * Retorna la descripcion del servicioNuevo
	 * @return
	 */
	public String  getDescripcionServicioNuevo(){
		return servicioNuevo.getNombre();
	}
	
	/**
	 * Asigna el codigo del servicio Nuevo
	 * @param codigoServicioNuevo
	*/
	public void setCodigoServicioNuevo(int codigoServicioNuevo){
		this.servicioNuevo.setCodigo(codigoServicioNuevo);
	}

	/**
	 * Asigna la descripción del servicioNuevo
	 * @param descripcionServicioNuevo
	 */
	public void setDescripcionServicioNuevo(String descripcionServicioNuevo){
		this.servicio.setNombre(descripcionServicioNuevo);
	}
	
	/**
	 * @return Returns the codigoAxioma.
	 */
	public String getCodigoAxioma() {
		return codigoAxioma;
	}
	/**
	 * @param codigoAxioma The codigoAxioma to set.
	 */
	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}
	/**
	 * @return Returns the codigoEspecilidad.
	 */
	public int getCodigoEspecilidad() {
		return codigoEspecilidad;
	}
	/**
	 * @param codigoEspecilidad The codigoEspecilidad to set.
	 */
	public void setCodigoEspecilidad(int codigoEspecilidad) {
		this.codigoEspecilidad = codigoEspecilidad;
	}
	
	/**
	 * @return Returns the fechasVigenciaContrato.
	 */
	public String getFechasVigenciaContrato() {
		return fechasVigenciaContrato;
	}
	/**
	 * @param fechasVigenciaContrato The fechasVigenciaContrato to set.
	 */
	public void setFechasVigenciaContrato(String fechasVigenciaContrato) {
		this.fechasVigenciaContrato = fechasVigenciaContrato;
	}
    /**
     * @return Returns the tipoPos.
     */
    public String getTipoPos() {
        return tipoPos;
    }
    /**
     * @param tipoPos The tipoPos to set.
     */
    public void setTipoPos(String tipoPos) {
        this.tipoPos = tipoPos;
    }
    /**
     * @return Returns the esposAux.
     */
    public int getEsposAux() {
        return esposAux;
    }
    /**
     * @param esposAux The esposAux to set.
     */
    public void setEsposAux(int esposAux) {
        this.esposAux = esposAux;
    }
}
