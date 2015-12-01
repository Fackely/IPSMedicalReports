package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.util.Date;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteTransfusionalDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de la información de un antecedente transfusional
 *
 * @version 1.0, Septiembre 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class AntecedenteTransfusional
{
	/**
	 * Código del antecedente
	 */
	private int codigo;
	
	/**
	 * Nombre del componente transfundido
	 */
	private String componente;
	
	/**
	 * Fecha en la cual se realizó la transfusión
	 */
	private String fecha;
	
	/**
	 * Causa de la transfusión
	 */
	private String causa;
	
	/**
	 * Lugar donde se realizó la transfusión
	 */
	private String lugar;
	
	/**
	 * Edad del paciente en el momento de la transfusión.
	 */
	private String edad;
	
	/**
	 * Nombre o relación del donante
	 */
	private String donante;
	
	/**
	 * Observaciones a esta transfusión
	 */
	private String observaciones;
	
	
	/** * Fecha */
	private Date fecha_at;
	
	/** * Hora */
	private String hora_at;
	
	/**
	 * Medio para comunicación con la base de datos
	 */
	private AntecedenteTransfusionalDao antecedenteTransfusionalDao = null;

	
	
	public AntecedenteTransfusional()
	{
		this.codigo = 0;
		this.componente = "";
		this.fecha = "";
		this.causa = "";
		this.lugar = "";
		this.edad = "";
		this.donante = "";
		this.observaciones = "";
		this.init(System.getProperty("TIPOBD"));		
	}
	
	public AntecedenteTransfusional(int codigo, String componente, String fecha, String causa, String lugar, String edad, String donante, String observaciones)
	{
		this.codigo = codigo;
		this.componente = componente;
		this.fecha = fecha;
		this.causa = causa;
		this.lugar = lugar;
		this.edad = edad;
		this.donante = donante;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));		
	}
	
	/**
	 * Inicializa el objeto de acceso a la base de datos
	 * @param	String, tipoBD. Identificador único de la base de datos que se 
	 * 				está usando.
	 */
	public void init (String tipoBD) 
	{
		if (antecedenteTransfusionalDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteTransfusionalDao = myFactory.getAntecedenteTransfusionalDao();
		}
	}
	
	/**
	 * Retorna el código del antecedente
	 * @return int, código
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código del antecedente
	 * @param int, codigo 
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el nombre del componente transfundido
	 * @return String, nombre
	 */
	public String getComponente()
	{
		return componente;
	}

	/**
	 * Asigna el nombre del componente transfundido
	 * @param String, componente, nombre
	 */
	public void setComponente(String componente)
	{
		this.componente = componente;
	}

	/**
	 * Retorna la fecha en la cual se realizó la transfusión
	 * @return String, fecha
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * Asigna la fecha en la cual se realizó la transfusión
	 * @param String, fecha
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * Retorna la causa de la transfusión
	 * @return String, causa
	 */
	public String getCausa()
	{
		return causa;
	}

	/**
	 * Asigna la causa de la transfusión
	 * @param String, causa
	 */
	public void setCausa(String causa)
	{
		this.causa = causa;
	}

	/**
	 * Retorna el lugar donde se realizó la transfusión
	 * @return String, lugar
	 */
	public String getLugar()
	{
		return lugar;
	}

	/**
	 * Asigna el lugar donde se realizó la transfusión
	 * @param String, lugar
	 */
	public void setLugar(String lugar)
	{
		this.lugar = lugar;
	}

	/**
	 * Retorna la edad del paciente en el momento de la transfusión
	 * @return String, edad
	 */
	public String getEdad()
	{
		return edad;
	}

	/**
	 * Asigna la edad del paciente en el momento de la transfusión
	 * @param String, edad
	 */
	public void setEdad(String edad)
	{
		this.edad = edad;
	}

	/**
	 * Retorna el nombre o relación del donante
	 * @return String, donante
	 */
	public String getDonante()
	{
		return donante;
	}

	/**
	 * Asigna el nombre o relación del donante
	 * @param String, donante 
	 */
	public void setDonante(String donante)
	{
		this.donante = donante;
	}

	/**
	 * Retorna las observaciones a esta transfusión
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones a esta transfusión
	 * @param String, observaciones 
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public ResultadoBoolean insertar(Connection con, int codigoPaciente)
	{
		return this.antecedenteTransfusionalDao.insertar(con, codigoPaciente, this.codigo, this.componente, this.fecha, this.causa, this.lugar, this.edad, this.donante, this.observaciones);
	}
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteTransfusionalDao.insertarTransaccional(con, codigoPaciente, this.codigo, this.componente, this.fecha, this.causa, this.lugar, this.edad, this.donante, this.observaciones, estado);
	}
	
	public ResultadoBoolean modificar(Connection con, int codigoPaciente)
	{
		return this.antecedenteTransfusionalDao.modificar(con, codigoPaciente, this.codigo, this.fecha, this.causa, this.lugar, this.edad, this.donante, this.observaciones);
	}	

	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String estado)  throws Exception
	{
		return this.antecedenteTransfusionalDao.modificarTransaccional(con, codigoPaciente, this.codigo, this.fecha, this.causa, this.lugar, this.edad, this.donante, this.observaciones, estado);
	}	
	
	public ResultadoBoolean existeAntecedente(Connection con, int codigoPaciente)
	{
		return this.antecedenteTransfusionalDao.existeAntecedente(con, codigoPaciente, this.codigo);
	}

	/**
	 * @return valor de fecha_at
	 */
	public Date getFecha_at() {
		return fecha_at;
	}

	/**
	 * @param fecha_at el fecha_at para asignar
	 */
	public void setFecha_at(Date fecha_at) {
		this.fecha_at = fecha_at;
	}

	/**
	 * @return valor de hora_at
	 */
	public String getHora_at() {
		return hora_at;
	}

	/**
	 * @param hora_at el hora_at para asignar
	 */
	public void setHora_at(String hora_at) {
		this.hora_at = hora_at;
	}

}
