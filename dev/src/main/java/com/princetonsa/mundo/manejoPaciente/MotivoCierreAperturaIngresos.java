package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.MotivoCierreAperturaIngresosDao;



public class MotivoCierreAperturaIngresos {
	
	
//	--- Atributos
	private static Logger logger = Logger.getLogger(MotivoCierreAperturaIngresos.class);
	

	private static MotivoCierreAperturaIngresosDao motivoCierreAperturaIngresosDao; 
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String codigo;
	/**
	 * 
	 */
	private String descripcion;
	/**
	 * 
	 */
	private String tipo;
	/**
	 * 
	 */
	private String activo="1";
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String codigom;
	
	

	public MotivoCierreAperturaIngresos(){
		
		this.reset();
		this.init (System.getProperty("TIPOBD"));
		
	}
	
	
	/**
	 * 
	 */
	public void reset()
	{
		
		this.patronOrdenar="";
		this.codigo="";
		this.descripcion="";
		this.tipo="";
		this.activo="1";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.codigom="";
			
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
			motivoCierreAperturaIngresosDao = myFactory.getMotivoCierreAperturaIngresosDao();
			wasInited = (motivoCierreAperturaIngresosDao != null);
		}
		return wasInited;
	}


	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		MotivoCierreAperturaIngresos.logger = logger;
	}



	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
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


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	
	
	
	
	
	
	public static HashMap consultar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoCierreAperturaIngresosDao().consultarMotivoCierreAperturaIngresos(con, motivoCierreAperturaIngresos);
	}

	public static boolean modificar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoCierreAperturaIngresosDao().modificarMotivoCierreAperturaIngresos(con, motivoCierreAperturaIngresos);
	}
	public static boolean eliminar(Connection con, String motivoCierreAperturaIngresos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoCierreAperturaIngresosDao().eliminarMotivoCierreAperturaIngresos(con, motivoCierreAperturaIngresos);
	}
	public static boolean insertar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoCierreAperturaIngresosDao().insertarMotivoCierreAperturaIngresos(con, motivoCierreAperturaIngresos);
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	public String getCodigom() {
		return codigom;
	}


	public void setCodigom(String codigom) {
		this.codigom = codigom;
	}
		
	
	
	
	
	
	
	
	
}