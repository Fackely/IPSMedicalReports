package com.princetonsa.mundo.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;
  
import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.agendaProcedimiento.UnidadProcedimientoDao;
import com.princetonsa.dao.sqlbase.agendaProcedimiento.SqlBaseUnidadProcedimiento;

import org.apache.log4j.Logger;

public class UnidadProcedimiento
{
	private Logger logger = Logger.getLogger(UnidadProcedimiento.class); 
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 * */
	private static UnidadProcedimientoDao unidadProcedimientoDao;
	
	
	/**
	 * Cadena que almacena la secuencia de Unidad de Procedimiento
	 * */
	private String cadenaSecuenciaUndProceStr;
	
	/**
	 * Cadena que almacena la secuencia de Servicio por Unidad de Procedimiento
	 * */
	private String cadenaSecuenciaServicioUndProceStr;
	
	/**
	 * Cadena que almacena la secuencia de Detalle del Servicio por Unidad de Procedimiento
	 * */
	private String cadenaSecuenciaDetServicioUndProceStr;
	
	//-- Unidad de Procedimiento 
	
	/**
	 * Codigo de la unidad de procedimiento
	 * */
	private int codigoUndProce;
	
	/**
	 * Codigo de la institucion
	 * */
	private int	institucion;
	
	/**
	 * Codigo de la especialidad
	 * */
	private int codigoEspeciali; 
	
	/**
	 * Descripcion de la Unidad de Procedimiento
	 * */
	private String descripUndProce;
	
	/**
	 * Activada Unidad de Procedimiento 
	 * */
	private String activoUndProce;
	
	//-- Fin Unidad de Procedimiento
	
	
	//-- Servicios por Unidad de Procedimiento 
	
	/**
	 * Codigo del Servicio por Unidad de Procedimiento 
	 * */
	private int codigoConsecutivoServiUndproce;
	
	/**
	 * Codigo del Servicio
	 * */
	private int codigoServicioUndProce;
	
	/**
	 * Tiempo de Duracion de la toma del examen 
	 * */
	private String tiempoServi;
	
	//-- Fin Servicios por Unidad de Procedimiento
	
	
	//-- Detalle Servicios por Unidad de Procedimiento
	
	/**
	 * Codigo del Detalle del Servicio por Unidad de Procedimiento 
	 * */
	private int codigoConsecutivoServiDetalle;
	
	/**
	 * Codigo del Servicio por Servicio 
	 * */
	private int codigoServicio;
	
	/**
	 * Codigo de Condicion de Toma de Examen
	 * */
	private int codigoExamenct;
	
	/**
	 * Codigo de Articulo
	 * */
	private int codigoArticulo;

	//-- Fin Detalle Servicios por Unidad de Procedimiento

	
	//-- Metodos
	/**
	 * Constructor de la Clase
	 * */
	public void UnidadProcedimiento()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));		
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (unidadProcedimientoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			unidadProcedimientoDao = myFactory.getUnidadProcedimientoDao();
		}	
	}
	
	/**
	 * Resetea los atributos de la clase
	 * */
	public void reset()
	{
		this.codigoUndProce=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.codigoEspeciali=ConstantesBD.codigoNuncaValido;
		this.descripUndProce="";
		this.activoUndProce="";
		
		this.codigoConsecutivoServiUndproce=ConstantesBD.codigoNuncaValido;
		this.codigoServicioUndProce=ConstantesBD.codigoNuncaValido;
		this.tiempoServi="";
		
		this.codigoConsecutivoServiDetalle=ConstantesBD.codigoNuncaValido;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.codigoExamenct=ConstantesBD.codigoNuncaValido;
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;		
	}
	
	/**
	 * */
	public static UnidadProcedimientoDao unidadProcedimientoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUnidadProcedimientoDao();
	}
	
	
	/**
	 * Consulta basica de Unidad de Procedimiento 
	 * @param Connection con 
	 * @param int codigo unidad de procedimiento 
	 * @param int institucion 
	 * */
	public static HashMap consultarUnidadProcBasica(Connection con, int codigoUnidadProc, int institucion)
	{
		return unidadProcedimientoDao().consultarUnidadProcBasica(con, codigoUnidadProc, institucion);
	}
	
	/**
	 * Consulta basica de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiUndProce
	 * @param int codigoUndProce
	 * */
	public static  HashMap consultarServicioUnidadProcBasica(Connection con, int codigoServiUndProce, int codigoUndProce)
	{
		HashMap temp = new HashMap();
		String cadena ="";
		temp = unidadProcedimientoDao().consultarServicioUnidadProcBasica(con, codigoServiUndProce, codigoUndProce);
		
		//separa el valor de tiempo en horas y minutos
		for(int i=0; i<Integer.parseInt(temp.get("numRegistros").toString());i++)
		{
			temp.put("hora_"+i,temp.get("tiempo_"+i).toString().substring(0,2));
			temp.put("minuto_"+i,temp.get("tiempo_"+i).toString().substring(3,5));
			
			cadena+=temp.get("codigoservicio_"+i).toString()+",";			
			temp.remove("tiempo_"+i);			
			
		}		
		temp.put("codServInsert",cadena);
		return temp;
	}
	
	/**
	 * Consulta basica de Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * */	
	public static HashMap consultarServicioDetUnidadProcBasica(Connection con, int codigoServiDetalle, int codigoServiUndProce)
	{
		return unidadProcedimientoDao().consultarServicioDetUnidadProcBasica(con, codigoServiDetalle, codigoServiUndProce);
	}
	
	
	/**
	 * Consulta basica de Detalle de Servicios por -> servicio, condicion de toma de examne, articulo 
	 * @param Connection con
	 * @param int codigoServiDetalle
	 * @param int codigoServiUndProce
	 * @param String tipo de consulta
	 * */	
	public static HashMap consultarServicioDetalles(Connection con, int codigoServiDetalle, int codigoServiUndProce, String tipo)
	{
		
		HashMap temp = new HashMap();
		String cadena ="", codigoTipo="";
		
		if(tipo.equals("servicio"))
			codigoTipo="codigoservicio";
		else if(tipo.equals("examen"))
			codigoTipo="codigoexamen";
		else if(tipo.equals("articulo"))
			codigoTipo="codigoarticulo";
		
		temp = unidadProcedimientoDao().consultarServicioDetalles(con, codigoServiDetalle, codigoServiUndProce, tipo);
		
		//separa el valor de tiempo en horas y minutos
		for(int i=0; i<Integer.parseInt(temp.get("numRegistros").toString());i++)
		{
			cadena+=temp.get(codigoTipo+"_"+i).toString()+",";			
		}		
		
		temp.put(codigoTipo+"Insert",cadena);
		return temp;	
	}
	
	/**
	 * Inserta un registro en Unidad Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean insertarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().insertarUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Inserta un registro en Servicios por Unidad Procedimiento 
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean insertarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().insertarServiciosUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Insertar un registro en Detalle de Servicios por Unidad Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public boolean insertarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().insertarServiciosDetUnidadProcedimiento(con, unidadProcedimiento);
	} 
	
	
	/**
	 * Modifica un registro de Unidad de Procedimiento
	 * @param Connecition con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean modificarUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().modificarUnidadProcedimiento(con, unidadProcedimiento);		
	}
	
	/**
	 * Modifica un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento
	 * */
	public boolean modificarServiciosUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().modificarServiciosUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Modifica un registro del Detalle del Servico por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimiento unidadProcedimiento 
	 * */
	public boolean modificarServiciosDetUnidadProcedimiento(Connection con, UnidadProcedimiento unidadProcedimiento)
	{
		return unidadProcedimientoDao().modificarServiciosDetUnidadProcedimiento(con, unidadProcedimiento);
	}
	
	/**
	 * Elimina un registro de Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @param int institucion
	 * */
	public boolean eliminarUnidadProcedimiento(Connection con, int codigoUndProce, int institucion)
	{
		return unidadProcedimientoDao().eliminarUnidadProcedimiento(con, codigoUndProce, institucion);
	}
	
	/**
	 * Elimina un registro de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoConsecutivoServiUndproce
	 * @param int CodigoUndProce
	 * */
	public boolean eliminarServiciosUnidadProcedimiento(Connection con, int codigoConsecutivoServiUndProce, int codigoUndProce)
	{
		return unidadProcedimientoDao().eliminarServiciosUnidadProcedimiento(con, codigoConsecutivoServiUndProce, codigoUndProce);
	}
	
	/**
	 * Eliminar un registro del Detalle de Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param codigoConsecutivoServiDetalle
	 * @param codigoConsecutivoServiUndProce 
	 * */
	public boolean eliminarServiciosDetUnidadProcedimiento(Connection con, int codigoConsecutivoServiDetalle, int codigoConsecutivoServiUndProce)
	{
		return unidadProcedimientoDao().eliminarServiciosDetUnidadProcedimiento(con, codigoConsecutivoServiDetalle, codigoConsecutivoServiUndProce);
	}
	
	/**
	 * Elimina Todos los registros de un Servicio por Unidad de Procedimiento asociado a un codigo de Unidad de Procedimiento
	 * @param Connection con
	 * @param int CodigoUndProce 
	 * */
	public boolean eliminarServiciosTodoUndProc(Connection con, int codigoUndProce)
	{
		return unidadProcedimientoDao().eliminarServiciosTodoUndProc(con, codigoUndProce);
	}
	
	/**
	 * Elimina Todo el Detalle de un Servicio por Unidad de Procedimiento
	 * @param Connection con
	 * @param int codigoConsecutivoServiUndProce
	 * */
	public boolean eliminarServiciosDetTodoUndProc(Connection con, int codigoUndProce, int codigoServiUndProce, int codigoServiDetalle)
	{
		return unidadProcedimientoDao().eliminarServiciosDetTodoUndProc(con, codigoUndProce, codigoServiUndProce, codigoServiDetalle);
	}
	
	
	/**
	 * Retorna cuantos servicios se encuentra dentro de una unidad de procedimiento
	 * @param Connection con
	 * @param int codigoUndProce
	 * @return int numero de registros 
	 * */
	public int consultaServicioUndProcCuantos(Connection con, int codigoUndProce)
	{
		return unidadProcedimientoDao().consultaServicioUndProcCuantos(con, codigoUndProce);
	}
	
	/**
	 * Retorna cuantos detalles (servicios, condiciones de toma de examen, articulos) existen en un servicio por unidad de procedimiento
	 * @param Connection con
	 * @param int codifgoUndProce
	 * @param int codigoServiUndProce
	 * @return int numero de registros 
	 * */
	public  int consultaDetalleCuantos(Connection con, int codigoUndProce, int codigoServiUndProce)
	{
		return unidadProcedimientoDao().consultaDetalleCuantos(con,codigoUndProce,codigoServiUndProce);	
	}
	
	/**
	 * Administra la cadena de codigos de servicios insertados desde la busqueda avanzada
	 * @param String cadena 
	 * @param int operacion
	 * @param String cadenaOperacion
	 * */
	public String CodigosInsertados(String cadena, int operacion, String cadenaOperacion)
	{
		if(!cadenaOperacion.equals(""))
		{
			if(operacion == 1) //adiccionar		
				cadena+=cadenaOperacion+",";	
		
			else if(operacion == 2) //eliminar
			{	
				if(cadena.equals(cadenaOperacion+","))
					cadena="";
				else	
					cadena = cadena.replaceAll(cadenaOperacion+",","");				
			}
		}		
		return cadena;
	}
	
	
	/**
	 * @return the activoUndProce
	 */
	public String getActivoUndProce() {
		return activoUndProce;
	}

	/**
	 * @param activoUndProce the activoUndProce to set
	 */
	public void setActivoUndProce(String activoUndProce) {
		this.activoUndProce = activoUndProce;
	}

	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the codigoEspeciali
	 */
	public int getCodigoEspeciali() {
		return codigoEspeciali;
	}

	/**
	 * @param codigoEspeciali the codigoEspeciali to set
	 */
	public void setCodigoEspeciali(int codigoEspeciali) {
		this.codigoEspeciali = codigoEspeciali;
	}

	/**
	 * @return the codigoExamenct
	 */
	public int getCodigoExamenct() {
		return codigoExamenct;
	}

	/**
	 * @param codigoExamenct the codigoExamenct to set
	 */
	public void setCodigoExamenct(int codigoExamenct) {
		this.codigoExamenct = codigoExamenct;
	}

	/**
	 * Codigo de Servicio del Detalle del Servicio por Unidad de Procedimiento
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * Codigo de Servicio del Detalle del Servicio por Unidad de Procedimiento
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the codigoServicioUndProce
	 */
	public int getCodigoServicioUndProce() {
		return codigoServicioUndProce;
	}

	/**
	 * @param codigoServicioUndProce the codigoServicioUndProce to set
	 */
	public void setCodigoServicioUndProce(int codigoServicioUndProce) {
		this.codigoServicioUndProce = codigoServicioUndProce;
	}

	/**
	 * @return the codigoUndProce
	 */
	public int getCodigoUndProce() {
		return codigoUndProce;
	}

	/**
	 * @param codigoUndProce the codigoUndProce to set
	 */
	public void setCodigoUndProce(int codigoUndProce) {
		this.codigoUndProce = codigoUndProce;
	}

	/**
	 * @return the descripUndProce
	 */
	public String getDescripUndProce() {
		return descripUndProce;
	}

	/**
	 * @param descripUndProce the descripUndProce to set
	 */
	public void setDescripUndProce(String descripUndProce) {
		this.descripUndProce = descripUndProce;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the tiempoServi
	 */
	public String getTiempoServi() {
		return tiempoServi;
	}

	/**
	 * @param tiempoServi the tiempoServi to set
	 */
	public void setTiempoServi(String tiempoServi) {
		this.tiempoServi = tiempoServi;
	}


	/**
	 * @return the cadenaSecuenciaUndProceStr
	 */
	public String getCadenaSecuenciaUndProceStr() {
		return cadenaSecuenciaUndProceStr;
	}


	/**
	 * @param cadenaSecuenciaUndProceStr the cadenaSecuenciaUndProceStr to set
	 */
	public void setCadenaSecuenciaUndProceStr(String cadenaSecuenciaUndProceStr) {
		this.cadenaSecuenciaUndProceStr = cadenaSecuenciaUndProceStr;
	}


	/**
	 * @return the cadenaSecuenciaServicioUndProceStr
	 */
	public String getCadenaSecuenciaServicioUndProceStr() {
		return cadenaSecuenciaServicioUndProceStr;
	}


	/**
	 * @param cadenaSecuenciaServicioUndProceStr the cadenaSecuenciaServicioUndProceStr to set
	 */
	public void setCadenaSecuenciaServicioUndProceStr(
			String cadenaSecuenciaServicioUndProceStr) {
		this.cadenaSecuenciaServicioUndProceStr = cadenaSecuenciaServicioUndProceStr;
	}


	/**
	 * @return the cadenaSecuenciaDetServicioUndProceStr
	 */
	public String getCadenaSecuenciaDetServicioUndProceStr() {
		return cadenaSecuenciaDetServicioUndProceStr;
	}


	/**
	 * @param cadenaSecuenciaDetServicioUndProceStr the cadenaSecuenciaDetServicioUndProceStr to set
	 */
	public void setCadenaSecuenciaDetServicioUndProceStr(
			String cadenaSecuenciaDetServicioUndProceStr) {
		this.cadenaSecuenciaDetServicioUndProceStr = cadenaSecuenciaDetServicioUndProceStr;
	}


	/**
	 * @return the codigoConsecutivoServiUndproce
	 */
	public int getCodigoConsecutivoServiUndproce() {
		return codigoConsecutivoServiUndproce;
	}


	/**
	 * @param codigoConsecutivoServiUndproce the codigoConsecutivoServiUndproce to set
	 */
	public void setCodigoConsecutivoServiUndproce(int codigoConsecutivoServiUndproce) {
		this.codigoConsecutivoServiUndproce = codigoConsecutivoServiUndproce;
	}


	/**
	 * @return the codigoConsecutivoServiDetalle
	 */
	public int getCodigoConsecutivoServiDetalle() {
		return codigoConsecutivoServiDetalle;
	}


	/**
	 * @param codigoConsecutivoServiDetalle the codigoConsecutivoServiDetalle to set
	 */
	public void setCodigoConsecutivoServiDetalle(int codigoConsecutivoServiDetalle) {
		this.codigoConsecutivoServiDetalle = codigoConsecutivoServiDetalle;
	}		
	
	//-- Fin Metodos	
}