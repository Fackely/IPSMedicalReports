/*
 * Sep 09 / 2005
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PorcentajesCxMultiplesDao;
import com.princetonsa.mundo.salasCirugia.Grupos;



/**
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de PorcentajesCxMultiples
 */
public class PorcentajesCxMultiples {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(PorcentajesCxMultiples.class);
	
	/**
	 * DAO para el manejo de las PorcentajesCxMultiples
	 */
	private PorcentajesCxMultiplesDao porcentajesCxMultiplesDao=null;
	
	//*************** ATRIBUTOS *******************************************
	
	//Atributos del Encabezado
	/**
	 * codigo encabezado
	 * */
	int codigo_enca;
	
	/**
	 * esq_tar_particular
	 * */
	String esq_tar_particular;
	
	/**
	 * Nombre esquema_particular
	 * */
	String nombre_esq_tar_part;
	
	/**
	 * esq_tar_general
	 * */
	String esq_tar_general;
	
	/**
	 * Nombre esquema general 
	 * */
	String nombre_esq_tar_gen;	
	
	/**
	 * Convenio
	 * */
	int convenio;
	
	/**
	 * String nombre convenio
	 * */
	String nombre_convenio;
	
	/**
	 * Fecha Inicial
	 * */
	String fecha_inicial;
	
	/**
	 * Fecha Final
	 * */
	String fecha_final;
	
	/**
	 * Institucion
	 * */
	int institucion;
	//------------------------------------------
	
	//Atributos del Detalle
	
	/**
	 * Código Axioma del Registro 
	 */
	int codigo;		
	
	
	/**
	 * Código del tipo de cirugía del porcentaje
	 */
	String tipoCirugia;
	
	/**
	 * Código del tipo de asocio aplicado al porcentaje
	 */
	int asocio;
	
	/**
	 * Código que define si el médico es igual o diferente
	 * a la solicitud cirugia principal
	 */
	String medico;
	
	/**
	 * Código que define si la via de acceso es igual o diferente
	 * a la solicitud cirugía principal
	 */
	String via;
	
	/**
	 * Porcentaje  a aplicar en la liquidación de la cirugia
	 */
	float liquidacion;
	
	/**
	 * Porcentaje  adicional a aplicar en la liquidación de la cirugia
	 */
	float adicional;
	
	/**
	 * Porcentaje de politraumatismo a aplicar en la liquidación de cirugias
	 */
	float politra;
	
	
	/**
	 * tipos_servicio  
	 * */
	String tipos_servicio;
	
	
	/**
	 * tipos_salas
	 * */
	int tipos_salas;
	
	/**
	 * estabd
	 * */
	String estabd;
	

	//-------------------------------------------------
	
	
	
	//*************** CONSTRUCTOR Y MÉTODOS DE INICIALIZACIÓN *************
	/**
	 * Constructor
	 */
	public PorcentajesCxMultiples() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigo=0;
		this.tipoCirugia="0";
		this.asocio=0;
		this.medico="";
		this.via="";
		this.liquidacion=0;
		this.adicional=0;
		this.politra=0;
		this.convenio = ConstantesBD.codigoNuncaValido;
		this.fecha_inicial = "";
		this.fecha_final = "";
		this.tipos_servicio = "";
		this.tipos_salas = 0;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (porcentajesCxMultiplesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			porcentajesCxMultiplesDao = myFactory.getPorcentajesCxMultiplesDao();
		}	
	}
	//************************* MÉTODOS ********************************************
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @param HashMap
	 * @return
	 */
	public HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.cargarEncaPorcentajes(con, institucion, parametros);
	}
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.cargarPorcentajes(con, institucion, parametros);
	}
	
	
	/**
	 * Método usado para modificar un registros de encabezado de porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.actualizarEncaPorcentaje(con, parametros);
	}
	
	/**
	 * Método usado para modificar un registros de los porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarPorcentaje(Connection con,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.actualizarPorcentaje(con, parametros);
	}
	
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarEncaPorcentaje(Connection con,int codigo)
	{
		return porcentajesCxMultiplesDao.eliminarEncaPorcentaje(con, codigo);
	}
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarPorcentaje(Connection con,int codigo)
	{
		return porcentajesCxMultiplesDao.eliminarPorcentaje(con, codigo);
	}
	
	
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarEncaPorcentaje(Connection con,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.insertarEncaPorcentaje(con, parametros);
	}	
	
	
	/**
	 * Método usado para insertar un nuevo Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarPorcentaje(Connection con,HashMap parametros)
	{
		return porcentajesCxMultiplesDao.insertarPorcentaje(con, parametros);
	}
	
	
	/**
	 * Método usao para cargar un registro de los porcentajes
	 * de cirugías múltiples por su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarPorcentaje(Connection con,int codigo)
	{
		return porcentajesCxMultiplesDao.cargarPorcentaje(con, codigo);
	}
	
	
	/**
	 * Método usado para la búsqueda de registros en 
	 * Consultar Porcentajes Cx Múltiples.
	 * @param con
	 * @param esGeneral
	 * @param esquemaTarifario
	 * @param tipoCirugia
	 * @param asocio
	 * @param medico
	 * @param via
	 * @param liquidacion
	 * @param politra
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaPorcentajes(
			Connection con,
			String esGeneral,
			int esquemaTarifario,
			String tipoCirugia,
			int asocio,
			int medico,
			int via,
			float liquidacion,
			float adicional,
			float politra,
			int institucion)
	{
		return porcentajesCxMultiplesDao.busquedaPorcentajes(con, esGeneral, esquemaTarifario, tipoCirugia, asocio, medico, via, liquidacion, adicional, politra, institucion);
	}
	
	
	//************************ GETTERS & SETTERS ************************************
	
	
	public static  ArrayList listarTiposServicio(Connection con)
	{		
		Grupos grp = new Grupos();
		return grp.listarTiposServicio(con);
	}
	
	

	/**
	 * @return Returns the asocio.
	 */
	public int getAsocio() {
		return asocio;
	}
	/**
	 * @param asocio The asocio to set.
	 */
	public void setAsocio(int asocio) {
		this.asocio = asocio;
	}	
	/**
	 * @return Returns the liquidacion.
	 */
	public float getLiquidacion() {
		return liquidacion;
	}
	/**
	 * @param liquidacion The liquidacion to set.
	 */
	public void setLiquidacion(float liquidacion) {
		this.liquidacion = liquidacion;
	}
	
	/**
	 * @return Returns the politra.
	 */
	public float getPolitra() {
		return politra;
	}
	/**
	 * @param politra The politra to set.
	 */
	public void setPolitra(float politra) {
		this.politra = politra;
	}
	/**
	 * @return Returns the tipoCirugia.
	 */
	public String getTipoCirugia() {
		return tipoCirugia;
	}
	/**
	 * @param tipoCirugia The tipoCirugia to set.
	 */
	public void setTipoCirugia(String tipoCirugia) {
		this.tipoCirugia = tipoCirugia;
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
	 * @return Returns the adicional.
	 */
	public float getAdicional() {
		return adicional;
	}
	/**
	 * @param adicional The adicional to set.
	 */
	public void setAdicional(float adicional) {
		this.adicional = adicional;
	}
	
	/**
	 * @return the fecha_final
	 */
	public String getFecha_final() {
		return fecha_final;
	}
	/**
	 * @param fecha_final the fecha_final to set
	 */
	public void setFecha_final(String fecha_final) {
		this.fecha_final = fecha_final;
	}
	/**
	 * @return the fecha_inicial
	 */
	public String getFecha_inicial() {
		return fecha_inicial;
	}
	/**
	 * @param fecha_inicial the fecha_inicial to set
	 */
	public void setFecha_inicial(String fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}
	/**
	 * @return the porcentajesCxMultiplesDao
	 */
	public PorcentajesCxMultiplesDao getPorcentajesCxMultiplesDao() {
		return porcentajesCxMultiplesDao;
	}
	/**
	 * @param porcentajesCxMultiplesDao the porcentajesCxMultiplesDao to set
	 */
	public void setPorcentajesCxMultiplesDao(
			PorcentajesCxMultiplesDao porcentajesCxMultiplesDao) {
		this.porcentajesCxMultiplesDao = porcentajesCxMultiplesDao;
	}
	
	/**
	 * @return the tipos_servicio
	 */
	public String getTipos_servicio() {
		return tipos_servicio;
	}
	/**
	 * @param tipos_servicio the tipos_servicio to set
	 */
	public void setTipos_servicio(String tipos_servicio) {
		this.tipos_servicio = tipos_servicio;
	}
	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return the tipos_salas
	 */
	public int getTipos_salas() {
		return tipos_salas;
	}
	/**
	 * @param tipos_salas the tipos_salas to set
	 */
	public void setTipos_salas(int tipos_salas) {
		this.tipos_salas = tipos_salas;
	}
	/**
	 * @return the estabd
	 */
	public String getEstabd() {
		return estabd;
	}
	/**
	 * @param estabd the estabd to set
	 */
	public void setEstabd(String estabd) {
		this.estabd = estabd;
	}	
	/**
	 * @return the codigo_enca
	 */
	public int getCodigo_enca() {
		return codigo_enca;
	}
	/**
	 * @param codigo_enca the codigo_enca to set
	 */
	public void setCodigo_enca(int codigo_enca) {
		this.codigo_enca = codigo_enca;
	}
	/**
	 * @return the esq_tar_general
	 */
	public String getEsq_tar_general() {
		return esq_tar_general;
	}
	/**
	 * @param esq_tar_general the esq_tar_general to set
	 */
	public void setEsq_tar_general(String esq_tar_general) {
		this.esq_tar_general = esq_tar_general;
	}
	/**
	 * @return the esq_tar_particular
	 */
	public String getEsq_tar_particular() {
		return esq_tar_particular;
	}
	/**
	 * @param esq_tar_particular the esq_tar_particular to set
	 */
	public void setEsq_tar_particular(String esq_tar_particular) {
		this.esq_tar_particular = esq_tar_particular;
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
	 * @return the nombre_convenio
	 */
	public String getNombre_convenio() {
		return nombre_convenio;
	}
	/**
	 * @param nombre_convenio the nombre_convenio to set
	 */
	public void setNombre_convenio(String nombre_convenio) {
		this.nombre_convenio = nombre_convenio;
	}
	/**
	 * @return the nombre_esq_tar_gen
	 */
	public String getNombre_esq_tar_gen() {
		return nombre_esq_tar_gen;
	}
	/**
	 * @param nombre_esq_tar_gen the nombre_esq_tar_gen to set
	 */
	public void setNombre_esq_tar_gen(String nombre_esq_tar_gen) {
		this.nombre_esq_tar_gen = nombre_esq_tar_gen;
	}
	/**
	 * @return the nombre_esq_tar_part
	 */
	public String getNombre_esq_tar_part() {
		return nombre_esq_tar_part;
	}
	/**
	 * @param nombre_esq_tar_part the nombre_esq_tar_part to set
	 */
	public void setNombre_esq_tar_part(String nombre_esq_tar_part) {
		this.nombre_esq_tar_part = nombre_esq_tar_part;
	}
	/**
	 * @return the medico
	 */
	public String getMedico() {
		return medico;
	}
	/**
	 * @param medico the medico to set
	 */
	public void setMedico(String medico) {
		this.medico = medico;
	}
	/**
	 * @return the via
	 */
	public String getVia() {
		return via;
	}
	/**
	 * @param via the via to set
	 */
	public void setVia(String via) {
		this.via = via;
	}

}
