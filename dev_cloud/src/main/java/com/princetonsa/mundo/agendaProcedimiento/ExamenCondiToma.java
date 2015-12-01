package com.princetonsa.mundo.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.agendaProcedimiento.ExamenCondiTomaDao;

public class ExamenCondiToma
{
	//---Atributos 
	
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 * */
	private static ExamenCondiTomaDao examenCondiTomaDao;
	
	/**
	 * String codigo de la condicion para la toma de examen
	 * */
	private String codigoExamenCt;
	
	/**
	 * int institucion  
	 * */
	private int institucion;
	
	/**
	 * String descripcion de la condicion para la toma de examen 
	 * */
	private String descripcionExamenCt;
	
	/**
	 * char activo S - N
	 * */
	private String activoExamenCt;
	
	/**
	 * Usuario quien modifico por ultima vez o creo la cobertura
	 * */
	private String usuarioModifica;
	
	/**
	 * Fecha de ultima modificacion o creacion de la cobertura
	 * */
	private String fechaModifica;
	
	/**
	 * Hora de ultima modificacion o creacion de la cobertura
	 * */
	private String horaModifica;
	
	/*
	 * Cadena indica de que manera capturar el codigo secuencial (Postgres u Oracle)
	 * */
	private String cadenaSecuenciaStr="";
	
	//---Fin Atributos
	
	
	
	
	//--- Metodos
	
	/**
	 * 
	 * */
	public ExamenCondiToma()
	{
		reset();
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
		if (examenCondiTomaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			examenCondiTomaDao = myFactory.getExamenCondiTomaDao();
		}	
	}
	
	/**
	 * Resetea los atributos de la clase
	 * */
	public void reset()
	{
		this.codigoExamenCt = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.descripcionExamenCt="";
		this.activoExamenCt="";
		this.usuarioModifica="";
		this.fechaModifica="";
		this.horaModifica="";
	}
	
	/**
	 * */
	public static ExamenCondiTomaDao examenCondiTomaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExamenCondiTomaDao();
	}
	
	
	/**
	 * Inserta un registro de condicion de toma de examen 
	 * @param Connection con 
	 * @param ExamenCondiToma exameCt
	 * */
	public boolean insertarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		return examenCondiTomaDao.insertarExamenCt(con, examenCt);
	}
	
	/**
	 * Modificar un registro de condicion de toma de examen 
	 * @param Connection con
	 * @param ExamenCondiToma examenCt 
	 * */
	public boolean modificarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		return examenCondiTomaDao.modificarExamenCt(con, examenCt);
	}
	
	/**
	 * Elimina un registro de condicion de toma de examen
	 * @param Connection con
	 * @param String codigoExamenCt
	 * @param int institucion 
	 * */
	public boolean eliminarExamenCt(Connection con, String codigoExamenCt, int institucion)
	{
		return examenCondiTomaDao.eliminarExamenCt(con, codigoExamenCt, institucion);
	}
	
	/**
	 * Consulta basica de condiciones de toma de examen
	 * si los parametros son -> ("",int) genera una consulta de todos los campos por institucion
	 * @param String codigoExamenCt
	 * @param int institucion
	 * */
	public static HashMap consultarExamentCtBasica(Connection con, String codigoExamenCt, int institucion)
	{	return examenCondiTomaDao().consultarExamentCtBasica(con, codigoExamenCt, institucion);		
	}
	
	
	//--- Getters And Setters
	
	/**
	 * @return the activoExamenCt
	 */
	public String getActivoExamenCt() {
		return activoExamenCt;
	}

	/**
	 * @param activoExamenCt the activoExamenCt to set
	 */
	public void setActivoExamenCt(String activoExamenCt) {
		this.activoExamenCt = activoExamenCt;
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
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
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	/**
	 * @param cadenaSecuenciaStr the usuarioModifica to set 
	 * */
	public void SetCadenaSecuenciaStr(String cadenaSecuenciaStr){
		this.cadenaSecuenciaStr =cadenaSecuenciaStr; 
	}
	
	/**
	 * @return the cadenaSecuenciaStr
	 * */
	public String GetCadenaSecuenciaStr(){
		return cadenaSecuenciaStr;
	}
	
	//---Fin Metodos
}