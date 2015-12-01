/*
 * Creado el 30/03/2007
 */

package com.sies.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.OcupacionDao;
import com.sies.dao.SiEsFactory;

/**
 * @author mono
 */

public class Ocupacion
{
	/**
	 * Manejador de logs de la clase
	 */
	//private Logger logger=Logger.getLogger(Ocupacion.class);
	
	/**
	 * Código de la ocupación
	 */
	private int codigo;
	
	/**
	 * Nombre de la ocupación
	 */
	private String nombre;
	
	/**
	 * Relacioncon el codigo
	 */
	private int ocupacion;
	
	/**
	 * Valor salario base
	 */
	private int valor;
	
	/**
	 * Fecha Inicio
	 */
	private String fecha_inicio;
	
	/**
	 * Fecha Fin
	 */
	private String fecha_fin;
	
	/**
	 * Tipo de Vinculacion
	 */
	private int tipo_vinculacion;
	
	/**
	 * Variable de Clase para la conexión con el DAO
	 */	
	private static OcupacionDao ocupacionDao;
	
	/**
	 * Metodo que define la conexion a la BD y que reemplaza al que esta documentado
	 */
	public Ocupacion()
	{
		ocupacionDao=SiEsFactory.getDaoFactory().getOcupacionDao();
	}
	
	/**
	 * Limpiar e inicializar atributos 
	 */
	public void clean()
	{
		codigo=0;
		nombre="";
		ocupacion=0;
		valor=0;
		fecha_inicio="";
		fecha_fin="";
	}
	
	/**
	 * Permite la inserción de una Nueva ocupacion al sistema
	 * @param con
	 * @param 
	 * @return la implementación del Dao correspondiente a este método
	 */
	public int insertarOcupacion (Connection con, int codigo, String nombre)
	{
		return Ocupacion.ocupacionDao.insertarOcupacion(con, codigo, nombre);
		
	}
	
	/**
	 * Modifica una ocupacion existente
	 * @param con
	 */
	public void modificarOcupacion (Connection con, int codigo, String nombre)
	{
		Ocupacion.ocupacionDao.modificarOcupacion(con, codigo, nombre);
	}
	
	
	
	/**
	 * consultar
	 */
	public Collection<HashMap<String, Object>> consultarOcupacion(Connection con)
	{
		return Ocupacion.ocupacionDao.consultarOcupacion(con);
	}
	
	/**
	 * Elimina una ocupacion
	 * @param con
	 */
	public int eliminarOcupacion(Connection con, int codigo)
	{
		return Ocupacion.ocupacionDao.eliminarOcupacion(con, codigo);
	}
	
	/**
	 * Metodo que consulta si una ocupacion existe
	 * @param con
	 * @param nombre
	 */
	public boolean consultaOcupacionExiste(Connection con, String nombre)
	{
		return Ocupacion.ocupacionDao.consultaOcupacionExiste(con, nombre);
	}
	
	/********************************ASIGNACION DE SALARIO BASE A OCUPACION*****************************/
	
	/**
	 * Modifica los datos del salario base de la ocupacion
	 */
	public void modificarOcupacionSalarioBase (Connection con, int ocupacion, int valor, String fecha_inicio, String fecha_fin,int tipo_vinculacion)
	{
		Ocupacion.ocupacionDao.modificarOcupacionSalarioBase(con, ocupacion, valor, fecha_inicio, fecha_fin, tipo_vinculacion);
	}
	
	/**
	 * Permite consultar las ocupaciones con sus respectivos datos
	 * @param con
	 * @param ocupacion
	 */
	public Collection consultarOcupacionSalarioBase (Connection con, int ocupacion)
	{
		return Ocupacion.ocupacionDao.consultarOcupacionSalarioBase(con, ocupacion);
	}
	
	/**
	 * Lista todos los tipos de vinculaciones existentes en el sistema
	 */
	public Collection<HashMap<String, Object>> listadoTiposVinculaciones(Connection con)
	{
		return Ocupacion.ocupacionDao.listadoTiposVinculaciones(con);
	}
	
	/**
	 * Lista los tipos de turnos existentes en el sistema
	 */
	public Collection<HashMap<String, Object>> listadoTiposTurnos (Connection con)
	{
		return Ocupacion.ocupacionDao.listadoTiposTurnos(con);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
	}

	public int getTipo_vinculacion() {
		return tipo_vinculacion;
	}

	public void setTipo_vinculacion(int tipo_vinculacion) {
		this.tipo_vinculacion = tipo_vinculacion;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
}