package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ParamArchivoPlanoIndCalidadDao;

public class ParamArchivoPlanoIndCalidad
{
	/**
	 * Codigo del Indicador
	 */
	private String codigo;
	
	/**
	 * Atributo para el codigo del Indicador
	 */
	private String acronimo;
	
	/**
	 * Atributo para la descripcion del Indicador
	 */
	private String descripcion;
	
	/**
	 * Atributo para la Especialidad
	 */
	private String especialidad;
	
	/**
	 * Atributo para el Centro de Costo
	 */
	private String centroCosto;
	
	/**
	 * Atributo para el Diagnostico
	 */
	private String diagnostico;
	
	/**
	 * Atributo para el Tad
	 */
	private String tad;
	
	/**
	 * Atributo para el Tas
	 */
	private String tas;
	
	/**
	 * Atributo para el MTad
	 */
	private String mtad;
	
	/**
	 * Atributo para el MTas
	 */
	private String mtas;
	
	/**
	 * Atributo para el Reporte de Cero
	 */
	private String cero;
	
	/**
	 * Atributo Tipo Cie del Diagnostico
	 */
	private String cie;
	
	/**
	 * Institucion
	 */
	private String institucion;
	
	/**
	 * Usuario
	 */
	private String usuario;
	
	
	
	
	private static Logger logger = Logger.getLogger(ParamArchivoPlanoIndCalidad.class);
	
	/**
	 * 
	 * @return
	 */
	private static ParamArchivoPlanoIndCalidadDao getParamArchivoPlanoIndCalidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParamArchivoPlanoIndCalidadDao();
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaES (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaES(con);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaCC (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaCC(con);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaDX (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaDX(con);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaG (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaG(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static HashMap<String, Object> consultaCOD (Connection con, String tipo)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaCOD(con, tipo);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaEspe (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaEspe(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultaCentros (Connection con, int centroAtencion)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaCentros(con, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaSignos (Connection con)
	{
		return getParamArchivoPlanoIndCalidadDao().consultaSignos(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean insertarEspecialidad(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return getParamArchivoPlanoIndCalidadDao().insertarEspecialidad(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean insertarCentro(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return getParamArchivoPlanoIndCalidadDao().insertarCentro(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean insertarDiagnostico(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return getParamArchivoPlanoIndCalidadDao().insertarDiagnostico(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarEspecialidad(Connection con, String codigo)
	{
		return getParamArchivoPlanoIndCalidadDao().eliminarEspecialidad(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarCentro(Connection con, String codigo)
	{
		return getParamArchivoPlanoIndCalidadDao().eliminarCentro(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarDiagnostico(Connection con, String codigo)
	{
		return getParamArchivoPlanoIndCalidadDao().eliminarDiagnostico(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarES(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return getParamArchivoPlanoIndCalidadDao().modificarES(con, mundo, codigoM);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarCC(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return getParamArchivoPlanoIndCalidadDao().modificarCC(con, mundo, codigoM);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarDX(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return getParamArchivoPlanoIndCalidadDao().modificarDX(con, mundo, codigoM);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarS(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM, String esNuevo)
	{
		return getParamArchivoPlanoIndCalidadDao().modificarS(con, mundo, codigoM, esNuevo);
	}

	/**
	 * 
	 * @return
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * 
	 * @param acronimo
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @param codigo
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * 
	 * @param especialidad
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * 
	 * @param institucion
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiagnostico() {
		return diagnostico;
	}

	/**
	 * 
	 * @param diagnostico
	 */
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	/**
	 * 
	 * @return
	 */
	public String getCie() {
		return cie;
	}

	/**
	 * 
	 * @param cie
	 */
	public void setCie(String cie) {
		this.cie = cie;
	}

	/**
	 * 
	 * @return
	 */
	public String getCero() {
		return cero;
	}

	/**
	 * 
	 * @param cero
	 */
	public void setCero(String cero) {
		this.cero = cero;
	}

	/**
	 * 
	 * @return
	 */
	public String getMtad() {
		return mtad;
	}

	/**
	 * 
	 * @param mtad
	 */
	public void setMtad(String mtad) {
		this.mtad = mtad;
	}

	/**
	 * 
	 * @return
	 */
	public String getMtas() {
		return mtas;
	}

	/**
	 * 
	 * @param mtas
	 */
	public void setMtas(String mtas) {
		this.mtas = mtas;
	}

	/**
	 * 
	 * @return
	 */
	public String getTad() {
		return tad;
	}

	/**
	 * 
	 * @param tad
	 */
	public void setTad(String tad) {
		this.tad = tad;
	}

	/**
	 * 
	 * @return
	 */
	public String getTas() {
		return tas;
	}

	/**
	 * 
	 * @param tas
	 */
	public void setTas(String tas) {
		this.tas = tas;
	}
}