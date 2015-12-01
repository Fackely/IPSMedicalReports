package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ConsultaReferenciaContrareferenciaDao;

/**
 * Mundo consulta referencia contrareferencia
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class ConsultaReferenciaContrareferencia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultaReferenciaContrareferenciaDao consultaDao;
	
	/**
	 * indica si la busqueda es x paciente o por periodo
	 */
	private boolean esBusquedaXpaciente;
	
	/**
	 * codigo del paciente
	 */
	private String codigoPaciente;
	
	/**
	 * tipo referencia
	 */
	private String tipoReferenciaInterna;
	
	/**
	 * tipo contrareferencia
	 */
	private String tipoContrareferencia;
	
	
	/**
	 * rango inicial del nro referencia
	 */
	private String rangoNroReferenciaInicial;
	
	/**
	 * rango final del nro referencia
	 */
	private String rangoNroReferenciaFinal;
	
	/**
	 * fecha finalizacion incial
	 */
	private String fechaFinalizacionInicial;
	
	/**
	 * fecha finalizacion final
	 */
	private String fechaFinalizacionFinal;
	
	/**
	 * codigo de la institucion Origen
	 */
	private String codigoInstitucionOrigen;
	
	/**
	 * codigo de la institcuion
	 */
	private int codigoInstitucion;
	
	/**
	 * reset
	 *
	 */
	public void reset()
	{
		this.esBusquedaXpaciente=true;
		this.codigoPaciente="";
		this.tipoReferenciaInterna=ConstantesBD.acronimoNo;
		this.tipoContrareferencia=ConstantesBD.acronimoNo;
		this.rangoNroReferenciaInicial="";
		this.rangoNroReferenciaFinal="";
		this.fechaFinalizacionInicial="";
		this.fechaFinalizacionFinal="";
		this.codigoInstitucionOrigen="";
		this.codigoInstitucion=ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * constructor
	 *
	 */
	public ConsultaReferenciaContrareferencia()
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
			consultaDao = myFactory.getConsultaReferenciaContrareferenciaDao();
			wasInited = (consultaDao != null);
		}
		return wasInited;
	}

	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param esBusquedaXpaciente
	 * @return
	 */
	public static HashMap busquedaReferenciaContrareferencia(Connection con, ConsultaReferenciaContrareferencia mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaReferenciaContrareferenciaDao().busquedaReferenciaContrareferencia(con, mundo);
	}
	

	/**
	 * @return the consultaDao
	 */
	public static ConsultaReferenciaContrareferenciaDao getConsultaDao() {
		return consultaDao;
	}


	/**
	 * @param consultaDao the consultaDao to set
	 */
	public static void setConsultaDao(
			ConsultaReferenciaContrareferenciaDao consultaDao) {
		ConsultaReferenciaContrareferencia.consultaDao = consultaDao;
	}


	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	/**
	 * @return the codigoInstitucionOrigen
	 */
	public String getCodigoInstitucionOrigen() {
		return codigoInstitucionOrigen;
	}


	/**
	 * @param codigoInstitucionOrigen the codigoInstitucionOrigen to set
	 */
	public void setCodigoInstitucionOrigen(String codigoInstitucionOrigen) {
		this.codigoInstitucionOrigen = codigoInstitucionOrigen;
	}


	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	/**
	 * @return the esBusquedaXpaciente
	 */
	public boolean getEsBusquedaXpaciente() {
		return esBusquedaXpaciente;
	}


	/**
	 * @param esBusquedaXpaciente the esBusquedaXpaciente to set
	 */
	public void setEsBusquedaXpaciente(boolean esBusquedaXpaciente) {
		this.esBusquedaXpaciente = esBusquedaXpaciente;
	}


	/**
	 * @return the fechaFinalizacionFinal
	 */
	public String getFechaFinalizacionFinal() {
		return fechaFinalizacionFinal;
	}


	/**
	 * @param fechaFinalizacionFinal the fechaFinalizacionFinal to set
	 */
	public void setFechaFinalizacionFinal(String fechaFinalizacionFinal) {
		this.fechaFinalizacionFinal = fechaFinalizacionFinal;
	}


	/**
	 * @return the fechaFinalizacionInicial
	 */
	public String getFechaFinalizacionInicial() {
		return fechaFinalizacionInicial;
	}


	/**
	 * @param fechaFinalizacionInicial the fechaFinalizacionInicial to set
	 */
	public void setFechaFinalizacionInicial(String fechaFinalizacionInicial) {
		this.fechaFinalizacionInicial = fechaFinalizacionInicial;
	}


	/**
	 * @return the rangoNroReferenciaFinal
	 */
	public String getRangoNroReferenciaFinal() {
		return rangoNroReferenciaFinal;
	}


	/**
	 * @param rangoNroReferenciaFinal the rangoNroReferenciaFinal to set
	 */
	public void setRangoNroReferenciaFinal(String rangoNroReferenciaFinal) {
		this.rangoNroReferenciaFinal = rangoNroReferenciaFinal;
	}


	/**
	 * @return the rangoNroReferenciaInicial
	 */
	public String getRangoNroReferenciaInicial() {
		return rangoNroReferenciaInicial;
	}


	/**
	 * @param rangoNroReferenciaInicial the rangoNroReferenciaInicial to set
	 */
	public void setRangoNroReferenciaInicial(String rangoNroReferenciaInicial) {
		this.rangoNroReferenciaInicial = rangoNroReferenciaInicial;
	}


	/**
	 * @return the tipoContrareferencia
	 */
	public String getTipoContrareferencia() {
		return tipoContrareferencia;
	}


	/**
	 * @param tipoContrareferencia the tipoContrareferencia to set
	 */
	public void setTipoContrareferencia(String tipoContrareferencia) {
		this.tipoContrareferencia = tipoContrareferencia;
	}


	/**
	 * @return the tipoReferenciaInterna
	 */
	public String getTipoReferenciaInterna() {
		return tipoReferenciaInterna;
	}


	/**
	 * @param tipoReferenciaInterna the tipoReferenciaInterna to set
	 */
	public void setTipoReferenciaInterna(String tipoReferenciaInterna) {
		this.tipoReferenciaInterna = tipoReferenciaInterna;
	}

}