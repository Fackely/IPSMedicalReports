/*
 * @(#)AntecedenteMorbidoQuirurgico.java
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

import com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de todos los antecedentes mórbidos de tipo quirurgicos.
 *
 * @version 1.0, Julio 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class AntecedenteMorbidoQuirurgico
{
	/**
	 * Código del antecedente mórbido quirurgico en la base de datos
	 */
	private int codigo;
	
	/**
	 * Nombre del antecedente mórbido quirurgico
	 */
	private String nombre;
	
	/**
	 * Fecha en la que se realizo la cirugia.
	 */
	private String fecha;
	
	/**
	 * Causa que llevo a la realización de la cirugia.
	 */
	private String causa;
	
	/**
	 * Complicaciones de la cirugía.
	 */
	private String complicaciones;
	
	/**
	 * Recomendaciones hechas con respecto a la cirugia
	 */
	private String recomendaciones;
	
	/**
	 * Observaciones adicionales del antecedente quirurgico
	 */
	private String observaciones;
	
	/** antecedenteMorbidoQuirurgicoDao **/
	private AntecedenteMorbidoQuirurgicoDao antecedenteMorbidoQuirurgicoDao = null;
	
	/** * Fecha */
	private Date fecha_ant;
	
	/** * Hora */
	private String hora_ant;
	
	
	/**
	 * Constructora de la clase. Inicializa todos sus atributos
	 */
	public AntecedenteMorbidoQuirurgico()
	{
		this.codigo = 0;
		this.nombre = "";
		this.fecha = "";
		this.causa = "";
		this.complicaciones = "";
		this.recomendaciones = "";
		this.observaciones = "";
		this.fecha_ant =null;
		this.hora_ant = null;
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Constructora de la clase. Inicializa todos sus atributos en los valores
	 * dados.
	 * @param int, codigo
	 * @param String, nombre
	 * @param String, fecha
	 * @param String, causa
	 * @param String, complicaciones
	 * @param String, recomendaciones
	 * @param String, observaciones
	 */
	public AntecedenteMorbidoQuirurgico(int codigo, String nombre, String fecha, String causa, String complicaciones, String recomendaciones, String observaciones)
	{
		this.codigo = codigo;
		this.nombre = nombre;
		this.fecha = fecha;
		this.causa = causa;
		this.complicaciones = complicaciones;
		this.recomendaciones = recomendaciones;
		this.observaciones = observaciones;		
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void init (String tipoBD) 
	{
		if (antecedenteMorbidoQuirurgicoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteMorbidoQuirurgicoDao = myFactory.getAntecedenteMorbidoQuirurgicoDao();
		}
	}

	
	/**
	 * Retorna el código del antecedente mórbido quirurgico en la base de datos
	 * @return int, codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código del antecedente mórbido quirurgico en la base de datos
	 * @param int, codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el nombre del antecedente mórbido quirurgico
	 * @return String, nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna el nombre del antecedente mórbido quirurgico
	 * @param String, nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna la fecha en la que se realizo la cirugia.
	 * @return String, fecha
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * Asigna la fecha en la que se realizo la cirugia.
	 * @param String, fecha
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * Retorna la causa que llevo a la realización de la cirugia
	 * @return String, causa
	 */
	public String getCausa()
	{
		return causa;
	}

	/**
	 * Asigna la causa que llevo a la realización de la cirugia
	 * @param String, causa
	 */
	public void setCausa(String causa)
	{
		this.causa = causa;
	}

	/**
	 * Retorna las complicaciones de la cirugía
	 * @return String, compliaciones
	 */
	public String getComplicaciones()
	{
		return complicaciones;
	}

	/**
	 * Asigna las complicaciones de la cirugía
	 * @param String, complicaciones
	 */
	public void setComplicaciones(String complicaciones)
	{
		this.complicaciones = complicaciones;
	}

	/**
	 * Retorna las recomendaciones hechas con respecto a la cirugia
	 * @return String, recomendaciones
	 */
	public String getRecomendaciones()
	{
		return recomendaciones;
	}

	/**
	 * Asigna las recomendaciones hechas con respecto a la cirugia
	 * @param String, recomendaciones
	 */
	public void setRecomendaciones(String recomendaciones)
	{
		this.recomendaciones = recomendaciones;
	}

	/**
	 * Retorna las observaciones adicionales del antecedente quirurgico
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones adicionales del antecedente quirurgico
	 * @param String, observaciones	
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoQuirurgicoDao.insertarTransaccional(con, codigoPaciente, this.codigo, this.nombre, this.fecha, this.causa, this.complicaciones, this.recomendaciones, this.observaciones, estado);
	}

	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteMorbidoQuirurgicoDao.modificarTransaccional(con, codigoPaciente, this.codigo, this.fecha, this.causa, this.complicaciones, this.recomendaciones, this.observaciones, estado);
	}	
	
	public ResultadoBoolean existeAntecedente(Connection con, int codigoPaciente)
	{
		return this.antecedenteMorbidoQuirurgicoDao.existeAntecedente(con, codigoPaciente, this.codigo);
	}

	

	/**
	 * @return valor de fecha_ant
	 */
	public Date getFecha_ant() {
		return fecha_ant;
	}

	/**
	 * @param fecha_ant el fecha_ant para asignar
	 */
	public void setFecha_ant(Date fecha_ant) {
		this.fecha_ant = fecha_ant;
	}

	/**
	 * @return valor de hora_ant
	 */
	public String getHora_ant() {
		return hora_ant;
	}

	/**
	 * @param hora_ant el hora_ant para asignar
	 */
	public void setHora_ant(String hora_ant) {
		this.hora_ant = hora_ant;
	}
}
