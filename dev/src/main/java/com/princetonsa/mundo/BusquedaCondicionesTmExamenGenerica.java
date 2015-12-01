package com.princetonsa.mundo;

import util.UtilidadBD;
import util.ConstantesBD;
import java.sql.Connection; 
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.BusquedaCondicionTmExamenGenericaDao;

public class BusquedaCondicionesTmExamenGenerica
{
	
	//--- Atributos
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 * */
	private static BusquedaCondicionTmExamenGenericaDao busquedaCondicionTmExamenGenericaDao;
	
	/**
	* int Codigo de Condicion de Toma de Examen
	*/
	private String codigoExamenCt;
	
	/**
	 * int Codigo de la institucion
	 * */
	private int institucion;
	
	/**
	 * String descripcion de la condicion de toma de examen  
	 * */
	private String descripcionExamenCt;
	
	/**
	 * String activo de la condicion de toma de examen
	 * */
	private String activo;
	
	//--- Fin Atributos
	
	//--- Metodos 
	
	/**
	 * Constructor de Busqueda Condiciones Toma Examen
	 * */
	public void BusquedaCondicionesTmExamenGenerica()
	{
		this.reset();		
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	/*
	public void init(String tipoBD)
	{
		if(busquedaCondicionTmExamenGenericaDao == null)
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory("tipoBD");
			busquedaCondicionTmExamenGenericaDao = myFactory.getBusquedaCondicionTmExamenGenericaDao();
		}
	}
	*/
	
	
	/**
	 * 
	 * */
	public static BusquedaCondicionTmExamenGenericaDao busquedaCondicionesTmExamenGenericaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaCondicionTmExamenGenericaDao();		
	}
	
	/**
	 * Inicializa los atributos de la clase
	 * */
	public void reset()
	{
		this.codigoExamenCt="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.descripcionExamenCt = "";
		this.activo = "";
	}
	
	/**
	 * Consulta Generica de Condiciones de Toma de Examen
	 * @param Connection con
	 * @param Int codigoExamenCt
	 * @param Int institucion
	 * @param String descripcionExamenCt
	 * @param String activo	 
	 * @param String codigosExamenesInsertados
	 * */
	public static HashMap consultarCondicionesTmExamenBasica(Connection con, String codigoExamenCt, int institucion, String descripcionExamenCt, String activo, String codigosExamenesInsertados)
	{
		return busquedaCondicionesTmExamenGenericaDao().consultarCondicionesTmExamenBasica(con, codigoExamenCt, institucion, descripcionExamenCt, activo, codigosExamenesInsertados);
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}


	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}


	/**
	 * @return the codigoExamenCt
	 */
	public String getCodigoExamenCt() {
		return codigoExamenCt;
	}


	/**
	 * @param codigoExamenCt the codigoExamenCt to set
	 */
	public void setCodigoExamenCt(String codigoExamenCt) {
		this.codigoExamenCt = codigoExamenCt;
	}


	/**
	 * @return the descripcionExamenCt
	 */
	public String getDescripcionExamenCt() {
		return descripcionExamenCt;
	}


	/**
	 * @param descripcionExamenCt the descripcionExamenCt to set
	 */
	public void setDescripcionExamenCt(String descripcionExamenCt) {
		this.descripcionExamenCt = descripcionExamenCt;
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
	
	//--- Fin Metodos
}