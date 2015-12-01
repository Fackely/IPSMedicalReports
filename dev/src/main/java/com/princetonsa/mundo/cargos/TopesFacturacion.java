/*
 * @(#)TopesFacturacion.java
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

import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TopesFacturacionDao;



/**
 * Mundo para el manejo de topes facturación
 * @version 1.0, Junio 30, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class TopesFacturacion 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TopesFacturacionDao topesDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(TopesFacturacion.class);
	
	/**
	 * Código del tope
	 */
	private int codigo;

	/**
	 * Campo para capturar el acronimo y nombre del 
	 *  tipo de régimen previamente creado en el sistema
	 */
	private InfoDatos tipoRegimen;
	
	/**
	 * Clasificación socio económica
	 * Código - descripcion
	 */
	private InfoDatosInt estrato;
	
	/**
	 * Tipo de Monto
	 * Código -Nombre
	 */
	private InfoDatosInt tipoMonto;
	
	/**
	 * tope por evento
	 */
	private double topeEvento;
	
	/**
	 * tope anio de calendario
	 */
	private double topeAnioCalendario;
	
	/**
	 * Institucion
	 * Código - razón Social
	 */
	private InfoDatosInt institucion;
	
	/**
	 * String del tope por evento para postular en pantalla
	 */
	private String topeEventoString;
	
	/**
	 * String del tope anio calendario para postular en pantalla
	 */
	private String topeAnioCalendarioString;
	
	/**
	 * String para guardar la fecha de vigencia inicial
	 */
	private InfoDatosString fechaVigenciaInicial;
	
	/**
	 * resetea los datos pertinentes 
	 */
	public void reset()
	{
		this.codigo=0;
		this.tipoRegimen= new InfoDatos();
		this.estrato= new InfoDatosInt();
		this.tipoMonto= new InfoDatosInt();
		this.topeAnioCalendario=0;
		this.topeEvento=0;
		this.institucion= new InfoDatosInt();
		this.topeEventoString = "";
		this.topeAnioCalendarioString = "";
		this.fechaVigenciaInicial=new InfoDatosString();
	}
	
	/**
	 * Constructor de la clase 
	 * @param codigo
	 * @param fechaVigencia
	 * @param topeEvento
	 * @param topeAnioCalendario
	 */
	public TopesFacturacion(	int codigo,
											float topeEvento,
											float topeAnioCalendario)
	{

		this.codigo=codigo;
		this.tipoRegimen= new InfoDatos();
		this.estrato= new InfoDatosInt();
		this.tipoMonto=new InfoDatosInt();
		this.topeEvento=topeEvento;
		this.topeAnioCalendario=topeAnioCalendario;
		this.fechaVigenciaInicial=new InfoDatosString();
		this.init (System.getProperty("TIPOBD"));																	
	}	
	
	/**
	* Constructor de la clase, inicializa en vacio todos los parámetros
	*/		
	public TopesFacturacion()
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
				topesDao = myFactory.getTopesFacturacionDao();
				wasInited = (topesDao != null);
			}
			return wasInited;
	}

	/**
	 * Método para insertar un tope de facturación
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 * @throws TopeFacturacionException
	 */
	public int insertarTope(Connection con) throws TopeFacturacionException,Exception 
	{
		int  resp1=-1;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (topesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (TopeFacturacionDao - insertarTope )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans = false;
		
		try {
			
			inicioTrans=myFactory.beginTransaction(con);			
			resp1=topesDao.insertar(con,tipoRegimen.getAcronimo(),estrato.getCodigo(),tipoMonto.getCodigo(),topeEvento,topeAnioCalendario,institucion.getCodigo(),UtilidadFecha.conversionFormatoFechaStringDate(fechaVigenciaInicial.getId()));
			
		} catch (Exception e) {
			inicioTrans = false;
			final String nombre_constraint = "UN_TOPES_FACTURACION";
			final String mensajeExcepcion = (e.getMessage()  == null ? "": e.getMessage()).toUpperCase();
			if(mensajeExcepcion.indexOf(nombre_constraint) > 0){
				throw new TopeFacturacionException(e);
			}
			else{
				throw e;
			}			
		}finally{
			
			if (!inicioTrans||resp1<1  ){
				myFactory.abortTransaction(con);
			}
			else{
				myFactory.endTransaction(con);
			}
		}

		return resp1;
	}

	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @param  codigo tope evento 
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int codigo) 
	{
		ResultSetDecorator rs;
		try 
		{
			rs = topesDao.cargarResumen(con,codigo);
			if (rs.next())
			{
				this.codigo=rs.getInt("codigo");
				this.tipoRegimen= new InfoDatos(rs.getString("acronimoTipoRegimen"), rs.getString("nombreTipoRegimen"));
				this.estrato= new InfoDatosInt(rs.getInt("codigoEstratoSocial"), rs.getString("nombreEstratoSocial"));
				this.tipoMonto= new InfoDatosInt(rs.getInt("codigoTipoMonto"), rs.getString("nombreTipoMonto"));
				logger.info("tope evento cargar resumen->"+rs.getString("topeEvento"));
				this.topeEvento=rs.getFloat("topeEvento");
				this.topeEventoString=rs.getString("topeEvento");
				this.topeAnioCalendario=rs.getFloat("topeAnioCalendario");
				this.topeAnioCalendarioString=rs.getString("topeAnioCalendario");
				this.institucion= new InfoDatosInt(rs.getInt("codigoInstitucion"), rs.getString("nombreInstitucion"));
				this.fechaVigenciaInicial=new InfoDatosString(rs.getString("vigenciaInicial"));
				return true;
			}
			else
				return false;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;
	}

	public boolean cargarUltimoCodigo(Connection con)
	{
		try
		{
			ResultSetDecorator rs=topesDao.cargarUltimoCodigo(con);
			if(rs.next())
			{
				this.codigo=rs.getInt("codigo");
				return true;
			}
			else
			{
				return false;
			}
		}catch(SQLException e)
		{
			logger.warn("Error mundo topes" +e.toString());	
			return false;
		}
	}	

	/**
	 * Método utilizado en la funcionalidad Modificar topes
	 * que tiene como finalidad modificar el tope por evento y tope por anio calendario
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarTopes(Connection con) throws SQLException 
	{
			int resp=0;
			resp=topesDao.modificar(con,this.getCodigo(),this.getTopeEvento(), this.getTopeAnioCalendario(),this.getFechaVigenciaInicial().getId());
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
	public int modificarTopesTransaccional (Connection con, String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (TopesFacturacion - modificarTopes) no esta especificado");
		}
		if (topesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (clasificacionDao - modificarClasificacionTransaccional )");
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
		resp1=topesDao.modificar(con,this.getCodigo(),this.getTopeEvento(), this.getTopeAnioCalendario(),this.getFechaVigenciaInicial().toString());
	
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
	 * Método que obtiene todos los resultados de la tabla topes_facturacion
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoTopes(Connection con)
	{
		topesDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTopesFacturacionDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(topesDao.listado(con));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo topes " +e.toString());
			coleccion=null;
		}
		
		return coleccion;
	}

	/**
	 * Método utilizado para borrar un tope de facturacion
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int eliminar(Connection con) throws SQLException 
	{
			int resp=0;
			resp=topesDao.eliminar(con,this.getCodigo());
			return resp;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla topes_facturacion
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoTopesPosteriores(Connection con)
	{
		topesDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTopesFacturacionDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(topesDao.listadoPosteriores(con));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo topes " +e.toString());
			coleccion=null;
		}
		
		return coleccion;
	}
	
	/**
	 * Returns the tipoRegimen
	 * @return
	 */
	public InfoDatos getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * Retorna el acrónimo del tipo de regimen
	 * @return
	 */
	public String getAcronimoTipoRegimen(){
		return tipoRegimen.getAcronimo();
	}

	/**
	 * Retorna el nombre del tipo de regimen
	 * @return
	 */
	public String  getNombreTipoRegimen(){
		return tipoRegimen.getNombre();
	}

	/**
	 * Sets the tipoRegimen
	 * @param s1
	 */
	public void setTipoRegimen(InfoDatos s1) {
		tipoRegimen = s1;
	}
	
	/**
	 * Asigna el acronimo del tipo de regimen
	 * @param acronimoTipoRegimen
	*/
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen){
		this.tipoRegimen.setAcronimo(acronimoTipoRegimen);
	}
	
	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Returns the estrato
	 * @return
	 */
	public InfoDatosInt getEstrato() {
		return estrato;
	}

	/**
	 * Retorna el código del estrato 
	 * @return
	 */
	public int getCodigoEstrato(){
		return estrato.getCodigo();
	}

	/**
	 * Retorna el nombre del formato factura
	 * @return
	 */
	public String  getDescripcionEstrato(){
		return estrato.getNombre();
	}

	/**
	 * Sets the estrato
	 * @param s1
	 */
	public void setEstrato(InfoDatosInt s1) {
		estrato = s1;
	}
	
	/**
	 * Asigna el codigo del estrato
	 * @param codigoEstrato
	*/
	public void setCodigoEstrato(int codigoEstrato){
		this.estrato.setCodigo(codigoEstrato);
	}

	/**
	 * Returns the tipo monto
	 * @return
	 */
	public InfoDatosInt getTipoMonto() {
		return tipoMonto;
	}

	/**
	 * Retorna el código del tipoMonto 
	 * @return
	 */
	public int getCodigoTipoMonto(){
		return tipoMonto.getCodigo();
	}

	/**
	 * Retorna el nombre del tipoMonto
	 * @return
	 */
	public String  getNombreTipoMonto(){
		return tipoMonto.getNombre();
	}

	/**
	 * Sets the tipoMonto
	 * @param s1
	 */
	public void setTipoMonto(InfoDatosInt s1) {
		tipoMonto = s1;
	}
	
	/**
	* Asigna el codigo del tipo de Monto
	* @param codigoTipoMonto
	*/
	public void setCodigoTipoMonto(int codigoTipoMonto){
		this.tipoMonto.setCodigo(codigoTipoMonto);
	}

	/**
	 * Returns the institucion
	 * @return
	 */
	public InfoDatosInt getInstitucion() {
		return  institucion;
	}

	/**
	 * Retorna el código de la Institucion 
	 * @return
	 */
	public int getCodigoInstitucion(){
		return  institucion.getCodigo();
	}

	/**
	 * Retorna el nombre de la Institucion
	 * @return
	 */
	public String  getRazonSocialInstitucion(){
		return  institucion.getNombre();
	}

	/**
	 * Sets the Institucion
	 * @param s1
	 */
	public void setInstitucion(InfoDatosInt s1) {
		 institucion = s1;
	}
	
	/**
	 * Asigna el codigo del tipo de Institucion
	 * @param codigoInstitucion
	*/
	public void setCodigoInstitucion(int codigoInstitucion){
		this. institucion.setCodigo(codigoInstitucion);
	}

	
	/**
	 * @return Returns the topeAnioCalendario.
	 */
	public double getTopeAnioCalendario() {
		return topeAnioCalendario;
	}
	/**
	 * @param topeAnioCalendario The topeAnioCalendario to set.
	 */
	public void setTopeAnioCalendario(double topeAnioCalendario) {
		this.topeAnioCalendario = topeAnioCalendario;
	}
	/**
	 * @return Returns the topeEvento.
	 */
	public double getTopeEvento() {
		return topeEvento;
	}
	/**
	 * @param topeEvento The topeEvento to set.
	 */
	public void setTopeEvento(double topeEvento) {
		this.topeEvento = topeEvento;
	}
	
	/**
	 * @return Returns the topeEventoString.
	 */
	public String getTopeEventoString()
	{
		return topeEventoString;
	}
	/**
	 * @param topeEventoString The topeEventoString to set.
	 */
	public void setTopeEventoString(String topeEventoString)
	{
		this.topeEventoString = topeEventoString;
	}
	/**
	 * @return Returns the topeAnioCalendarioString.
	 */
	public String getTopeAnioCalendarioString()
	{
		return topeAnioCalendarioString;
	}
	/**
	 * @param topeAnioCalendarioString The topeAnioCalendarioString to set.
	 */
	public void setTopeAnioCalendarioString(String topeAnioCalendarioString)
	{
		this.topeAnioCalendarioString = topeAnioCalendarioString;
	}
	
	
	public InfoDatosString getFechaVigenciaInicial() {
		return fechaVigenciaInicial;
	}

	public void setFechaVigenciaInicial(InfoDatosString fechaVigenciaInicial) {
		this.fechaVigenciaInicial = fechaVigenciaInicial;
	}

	/**
	 * Carga el codigo del tope
	 * @param con
	 * @param tipoRegimen
	 * @param estratoSocial
	 * @param tipoMonto
	 * @return entero mayor que 0 si no hubo errores en la consulta
	 */
	public static int cargarTopeParaFacturacion(Connection con, String tipoRegimen, int estratoSocial, int tipoMonto, String fechaVigencia)
	{
		int codigoTope=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTopesFacturacionDao().cargarTopeParaFacturacion(con, tipoRegimen, estratoSocial, tipoMonto, fechaVigencia);
		logger.info("*******************************************************************************************************");
		logger.info("*******************************************************************************************************");
		logger.info("TOPE DE FACTURA-->"+codigoTope);
		logger.info("*******************************************************************************************************");
		logger.info("*******************************************************************************************************");
		return codigoTope;
	}
	
}
