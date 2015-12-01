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

import com.princetonsa.dao.AntecedenteMorbidoMedicoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de todos los antecedentes mórbidos de tipo médico.
 *
 * @version 1.0, Agosto 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class AntecedenteMorbidoMedico
{
	/**
	 * Código del antecedente mórbido médico en la base de datos
	 */	
	private int codigo;

	/**
	 * Nombre del antecedente mórbido médico
	 */	
	private String nombre;
	
	/**
	 * Fecha de inicio del antecedente
	 */
	private String fechaInicio;
	
	/**
	 * Tratamiento que se tiene para manejar el antecedente mórbido médico
	 */
	private String tratamiento;
	
	/**
	 * Restriccion dietaria que se tiene para manejar el antecedente mórbido
	 * médico
	 */
	private String restriccionDietaria;
	
	/**
	 * Observaciones para este antecedente mórbido médico
	 */
	private String observaciones;
	
	/** * antecedenteMorbidoMedicoDao */
	private AntecedenteMorbidoMedicoDao antecedenteMorbidoMedicoDao = null;

	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	
	/**
	 * Constructora de la clase. Inicializa los atributos.
	 */
	public AntecedenteMorbidoMedico()
	{
		this.codigo = 0;
		this.nombre = "";
		this.fechaInicio = "";
		this.tratamiento = "";
		this.restriccionDietaria = "";
		this.observaciones = "";	
		this.fecha = null;
		this.hora = null;
		this.init(System.getProperty("TIPOBD"));
	}
	
	public AntecedenteMorbidoMedico(int codigo, String nombre, String fechaInicio, String tratamiento, String restriccionDietaria, String observaciones)
	{
		this.codigo = codigo;
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.tratamiento = tratamiento;
		this.restriccionDietaria = restriccionDietaria;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void init (String tipoBD) 
	{
		if (antecedenteMorbidoMedicoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteMorbidoMedicoDao = myFactory.getAntecedenteMorbidoMedicoDao();
		}
	}
	
	
	/**
	 * Retorna el código del antecedente mórbido médico en la base de datos
	 * @return int, código
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código del antecedente mórbido médico en la base de datos
	 * @param int, codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el nombre del antecedente mórbido médico
	 * @return String, nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna el nombre del antecedente mórbido médico
	 * @param String, nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna la fecha de inicio del antecedente
	 * @return String, fecha de inicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * Asigna la fecha de inicio del antecedente
	 * @param String, fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Retorna el tratamiento que se tiene para manejar el antecedente mórbido
	 * médico
	 * @return String, tratamiento
	 */
	public String getTratamiento()
	{
		return tratamiento;
	}

	/**
	 * Asigna el tratamiento que se tiene para manejar el antecedente mórbido
	 * médico
	 * @param String, tratamiento
	 */
	public void setTratamiento(String tratamiento)
	{
		this.tratamiento = tratamiento;
	}

	/**
	 * Retorna la restriccion dietaria que se tiene para manejar el antecedente mórbido
	 * médico
	 * @return String, restriccionDietaria
	 */
	public String getRestriccionDietaria()
	{
		return restriccionDietaria;
	}

	/**
	 * Asigna la restriccion dietaria que se tiene para manejar el antecedente mórbido
	 * médico
	 * @param String, restriccionDietaria
	 */
	public void setRestriccionDietaria(String restriccionDietaria)
	{
		this.restriccionDietaria = restriccionDietaria;
	}

	/**
	 * Retorna las observaciones para este antecedente mórbido médico
	 * @return String, obse
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones para este antecedente mórbido médico
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public ResultadoBoolean insertarTransaccionalPredefinido(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoMedicoDao.insertarPredefinidoTransaccional(con, codigoPaciente, this.codigo, this.fechaInicio, this.tratamiento, this.restriccionDietaria, this.observaciones, estado);
	}

	public ResultadoBoolean insertarTransaccionalOtro(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoMedicoDao.insertarOtroTransaccional(con, codigoPaciente, this.codigo, this.nombre, this.fechaInicio, this.tratamiento, this.restriccionDietaria, this.observaciones, estado);
	}

	
	public ResultadoBoolean modificarPredefinidoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoMedicoDao.modificarPredefinidoTransaccional(con, codigoPaciente, this.codigo, this.fechaInicio, this.tratamiento, this.restriccionDietaria, this.observaciones, estado);
	}			
	
	public ResultadoBoolean modificarOtroTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoMedicoDao.modificarOtroTransaccional(con, codigoPaciente, this.codigo, this.nombre, this.fechaInicio, this.tratamiento, this.restriccionDietaria, this.observaciones, estado);		
	}
	
	public ResultadoBoolean existeAntecedentePredefinido(Connection con, int codigoPaciente)
	{
		return this.antecedenteMorbidoMedicoDao.existeAntecedentePredefinido(con, codigoPaciente, this.codigo);	
	}

	public ResultadoBoolean existeAntecedenteOtro(Connection con, int codigoPaciente)
	{
		return this.antecedenteMorbidoMedicoDao.existeAntecedenteOtro(con, codigoPaciente, this.codigo);	
	}

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
