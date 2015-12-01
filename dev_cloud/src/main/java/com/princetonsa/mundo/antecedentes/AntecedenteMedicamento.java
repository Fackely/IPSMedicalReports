/*
 * @(#)AntecedenteMorbidoMedico.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.util.Date;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteMedicamentoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de la información de un antecedente a un medicamento
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class AntecedenteMedicamento
{
	/**
	 * Código del medicamento
	 */
	private int codigo;
	
	/**
	 * Codigo verdadero del medicamento
	 */
	private String codigoA;
	
	/**
	 * Nombre del medicamento
	 */
	private String nombre;
	
	/**
	 * Dosis que se toma del medicamento
	 */
	private String dosis;
	
	/**
	 * Frecuencia con la que se toma el medicamento
	 */
	private String frecuencia;
	
	/**
	 * Fecha en la que se empezo a tomar el medicamento.
	 */
	private String fechaInicio;
	
	/**
	 * Fecha en la que se dejó de tomar el medicamento.
	 */
	private String fechaFin;
	
	/**
	 * Observaciones adicionales para este antecedente
	 */
	private String observaciones;
	
	/**
	 * Medio para comunicación con la base de datos
	 */
	private AntecedenteMedicamentoDao antecedenteMedicamentoDao = null;
	
	/**
	 * 
	 */
	private String cantidad;
	
	/**
	 * 
	 */
	private String dosisD;
	
	/**
	 * 
	 */
	private String tiempoT;

	
	
	private String unidosis;
	private String tipoFrecuencia;
	
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	
	
	/**
	 * Constructora de la clase
	 */
	public AntecedenteMedicamento()
	{
		codigo = 0;
		nombre = "";
		dosis = "";
		frecuencia = "";
		fechaInicio = "";
		fechaFin = "";
		observaciones = "";

		tipoFrecuencia = "";
		unidosis = ""; //Integer.toString(ConstantesBD.codigoNuncaValido);

		this.cantidad="";
		this.dosisD="";
		this.tiempoT="";
		this.codigoA="";
		this.init(System.getProperty("TIPOBD"));
		
		this.fecha = null;
		this.hora = null;
	}
	
	public AntecedenteMedicamento(int codigo, String codigoA, String nombre, String dosis, String frecuencia, String fechaInicio, String fechaFin, String observaciones,String cantidad,String dosisD,String tiempot, String tipofrecuencia, String unidosis)
	{
		this.codigo = codigo;
		this.codigoA=codigoA;
		this.nombre = nombre;
		this.dosis = dosis;
		this.frecuencia = frecuencia;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.cantidad=cantidad;
		this.dosisD=dosisD;
		this.tiempoT=tiempot;
		this.observaciones = observaciones;
		
		this.tipoFrecuencia = tipofrecuencia;
		this.unidosis = unidosis;
		
		this.init(System.getProperty("TIPOBD"));		
	}
	
	/**
	 * Inicializa el objeto de acceso a la base de datos
	 * @param	String, tipoBD. Identificador único de la base de datos que se 
	 * 				está usando.
	 */
	public void init (String tipoBD) 
	{
		if (antecedenteMedicamentoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteMedicamentoDao = myFactory.getAntecedenteMedicamentoDao();
		}
	}

	
	/**
	 * Retorna el nombre del medicamento
	 * @return 	String, cadena de máximo 128 caracteres que define el nombre del
	 * 				medicamento
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna el nombre del medicamento
	 * @param 	String, nombre. Cadena de máximo 128 caracteres que define el
	 * 					nombre del	medicamento
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna la dosis que se toma del medicamento
	 * @return 		String, cadena de máximo 32 caracteres que define la dosis
	 * 					que se toma del medicamento.
	 */
	public String getDosis()
	{
		return dosis;
	}

	/**
	 * Asigna la dosis que se toma del medicamento
	 * @param 	String, cadena de máximo 32 caracteres que define la dosis
	 * 					que se toma del medicamento.
	 */
	public void setDosis(String dosis)
	{
		this.dosis = dosis;
	}

	/**
	 * Retorna la frecuencia con la que se toma el medicamento
	 * @return 		String, cadena de máximo 32 caracteres que define la
	 * 					frecuencia con la que se toma el medicamento
	 */
	public String getFrecuencia()
	{
		return frecuencia;
	}

	/**
	 * Asigna la frecuencia con la que se toma el medicamento
	 * @param 	String, cadena de máximo 32 caracteres que define la
	 * 					frecuencia con la que se toma el medicamento
	 */
	public void setFrecuencia(String frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	/**
	 * Retorna la fecha en la que se empezo a tomar el medicamento.
	 * @return 		String, cadena de máximo 32 caracteres que define la
	 * 					fecha en la que se empezó a tomar el medicamento.
	 * 					No tiene  ningún formato de fecha.
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * Asigna la fecha en la que se empezo a tomar el medicamento.
	 * @param		String, cadena de máximo 32 caracteres que define la
	 * 					fecha en la que se empezó a tomar el medicamento.
	 * 					No tiene  ningún formato de fecha.
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Retorna la fecha en la que se dejó de tomar el medicamento.
	 * @return 		String, cadena de máximo 32 caracteres que define la
	 * 					fecha en la que se dejó de tomar el medicamento.
	 * 					No tiene  ningún formato de fecha.
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * Asigna la fecha en la que se dejó de tomar el medicamento.
	 * @param		String, cadena de máximo 32 caracteres que define la
	 * 					fecha en la que se dejó de tomar el medicamento.
	 * 					No tiene  ningún formato de fecha.
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * Retorna las observaciones adicionales para este antecedente
	 * @return 		String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones adicionales para este antecedente
	 * @param 	String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * Retorna el código del medicamento
	 * @return	int, código
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código del medicamento
	 * @param int, código
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public ResultadoBoolean insertar(Connection con, int codigoPaciente)
	{
		return this.antecedenteMedicamentoDao.insertar(con, codigoPaciente, this.codigo, this.nombre, this.dosis, this.frecuencia, this.fechaInicio, this.fechaFin, this.observaciones,this.cantidad,this.dosisD,this.tiempoT, this.tipoFrecuencia, this.unidosis);
	}
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMedicamentoDao.insertarTransaccional(con, codigoPaciente, this.codigo, this.codigoA, this.nombre, this.dosis, this.frecuencia, this.fechaInicio, this.fechaFin, this.observaciones, estado,this.cantidad,this.dosisD,this.tiempoT, this.tipoFrecuencia, this.unidosis);
	}

	public ResultadoBoolean modificar(Connection con, int codigoPaciente)
	{
		return this.antecedenteMedicamentoDao.modificar(con, codigoPaciente, this.codigo, this.dosis, this.frecuencia, this.fechaInicio, this.fechaFin, this.observaciones,this.cantidad,this.dosisD,this.tiempoT, this.tipoFrecuencia, this.unidosis);
	}	

	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMedicamentoDao.modificarTransaccional(con, codigoPaciente, this.codigo, this.dosis, this.frecuencia, this.fechaInicio, this.fechaFin, this.observaciones, estado,this.cantidad,this.dosisD,this.tiempoT, this.tipoFrecuencia, this.unidosis);
	}	
	
	public ResultadoBoolean existeAntecedente(Connection con, int codigoPaciente)
	{
		return this.antecedenteMedicamentoDao.existeAntecedente(con, codigoPaciente, this.codigo);
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getDosisD() {
		return dosisD;
	}

	public void setDosisD(String dosisD) {
		this.dosisD = dosisD;
	}

	public String getTiempoT() {
		return tiempoT;
	}

	public void setTiempoT(String tiempoT) {
		this.tiempoT = tiempoT;
	}

	public String getCodigoA() {
		return codigoA;
	}

	public void setCodigoA(String codigoA) {
		this.codigoA = codigoA;
	}

	
	
	public String getTipoFrecuencia() {	return tipoFrecuencia;	}
	public void setTipoFrecuencia(String tipoFrecuencia) {	this.tipoFrecuencia = tipoFrecuencia;	}

	/** unidosis	 */
	public String getUnidosis() {	return unidosis;	}
	public void setUnidosis(String unidosis) {	this.unidosis = unidosis;	}

	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return valor de hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora el hora para asignar
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	
}