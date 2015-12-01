package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.util.Date;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteToxicoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de un  antecedente tóxico de todos los tipos del
 * paciente.
 *
 * @version 1.0, Noviembre 28, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see com.princetonsa.mundo.antecedentes.AntecedenteToxico
 * @see com.princetonsa.mundo.PersonaBasica
 */
public class AntecedenteToxico
{
	/**
	 * Código del toxico en la base de datos
	 */
	private int codigo;
	
	/**
	 * Código del tipo de ant tóxico predefinido
	 */
	private int codigoTipoPredefinido;
	
	/**
	 * Nombre del toxico
	 */
	private String nombre;
	
	/**
	 * Dice si el antecedente tóxico es actual o no
	 */	
	private boolean actual;
	
	/**
	 * Cantidad ingerida del tóxico
	 */
	private String cantidad;
	
	/**
	 * Frecuencia de comsumo del tóxico
	 */
	private String frecuencia;
	
	/**
	 * Cuanto tiempo lleva o duró consumiendo el tóxico
	 */
	private String tiempoHabito;
	
	/**
	 * Observaciones del tóxico
	 */
	private String observaciones;
	
	/**
	 * Fecha de grabación del antecedente
	 */
	private String fechaGrabacion;
	
	/**
	 * Hora de grabación del antecedente
	 */
	private String horaGrabacion;
	
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	
	/**
	 * Para comunicarse con la fuente de datos
	 */
	private AntecedenteToxicoDao antecedenteToxicoDao = null;

	public AntecedenteToxico()
	{
		this.codigo = 0;
		this.codigoTipoPredefinido = 0;
		this.nombre = "";
		this.cantidad = "";
		this.frecuencia = "";
		this.tiempoHabito = "";
		this.observaciones = "";
		this.fecha = null;
		this.hora = null;
		this.init(System.getProperty("TIPOBD"));		
	}
	
	public AntecedenteToxico(int codigo, int codTipoPredefinido, boolean actual, String nombre, String cantidad, String frecuencia, String tiempo, String observaciones)
	{
		this.codigo = codigo;
		this.codigoTipoPredefinido = codTipoPredefinido;
		this.actual = actual;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.frecuencia = frecuencia;
		this.tiempoHabito = tiempo;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));		
	}
	
	public void init (String tipoBD) 
	{
		if (antecedenteToxicoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteToxicoDao = myFactory.getAntecedenteToxicoDao();
		}
	}
	
	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	
	/**
	 * Retorna el código del tipo de ant tóxico predefinido
	 * @return int
	 */
	public int getCodigoTipoPredefinido()
	{
		return codigoTipoPredefinido;
	}

	/**
	 * Asigna el código del tipo de ant tóxico predefinido
	 * @param codigoTipoPredefinido
	 */
	public void setCodigoTipoPredefinido(int codigoTipoPredefinido)
	{
		this.codigoTipoPredefinido = codigoTipoPredefinido;
	}

	/**
	 * Returns the nombre.
	 * @return String
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Sets the nombre.
	 * @param nombre The nombre to set
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Returns the actual.
	 * @return boolean
	 */
	public boolean isActual()
	{
		return actual;
	}

	/**
	 * Sets the actual.
	 * @param actual The actual to set
	 */
	public void setActual(boolean actual)
	{
		this.actual = actual;
	}

	/**
	 * Returns the cantidad.
	 * @return String
	 */
	public String getCantidad()
	{
		return cantidad;
	}

	/**
	 * Sets the cantidad.
	 * @param cantidad The cantidad to set
	 */
	public void setCantidad(String cantidad)
	{
		this.cantidad = cantidad;
	}

	/**
	 * Returns the frecuencia.
	 * @return String
	 */
	public String getFrecuencia()
	{
		return frecuencia;
	}

	/**
	 * Sets the frecuencia.
	 * @param frecuencia The frecuencia to set
	 */
	public void setFrecuencia(String frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	/**
	 * Returns the tiempoHabito.
	 * @return String
	 */
	public String getTiempoHabito()
	{
		return tiempoHabito;
	}

	/**
	 * Sets the tiempoHabito.
	 * @param tiempoHabito The tiempoHabito to set
	 */
	public void setTiempoHabito(String tiempoHabito)
	{
		this.tiempoHabito = tiempoHabito;
	}

	/**
	 * Returns the observaciones.
	 * @return String
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Sets the observaciones.
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}	
	
	/**
	 * Retorna la fecha de grabación del antecedente
	 * @return String
	 */
	public String getFechaGrabacion()
	{
		return fechaGrabacion;
	}

	/**
	 * Asigna la fecha de grabación del antecedente
	 * @param fechaGrabacion 
	 */
	public void setFechaGrabacion(String fechaGrabacion)
	{
		this.fechaGrabacion = fechaGrabacion;
	}

	/**
	 * Retorna la hora de grabación del antecedente
	 * @return String
	 */
	public String getHoraGrabacion()
	{
		return horaGrabacion;
	}

	/**
	 * Asigna la hora de grabación del antecedente
	 * @param horaGrabacion The horaGrabacion to set
	 */
	public void setHoraGrabacion(String horaGrabacion)
	{
		this.horaGrabacion = horaGrabacion;
	}

	public ResultadoBoolean insertarPredefinido(Connection con, int codigoPaciente)
	{		
		return this.antecedenteToxicoDao.insertarPredefinido(con, codigoPaciente, this.codigoTipoPredefinido, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones);
	}
	
	public ResultadoBoolean insertarTransaccionalPredefinido(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteToxicoDao.insertarPredefinidoTransaccional(con, codigoPaciente, this.codigoTipoPredefinido, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones, estado);
	}

	public ResultadoBoolean insertarOtro(Connection con, int codigoPaciente)
	{
		return this.antecedenteToxicoDao.insertarOtro(con, codigoPaciente, this.codigo, this.nombre, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones);
	}

	public ResultadoBoolean insertarTransaccionalOtro(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteToxicoDao.insertarOtroTransaccional(con, codigoPaciente, this.codigo, this.nombre, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones, estado);
	}

	public ResultadoBoolean modificarPredefinido(Connection con, int codigoPaciente)
	{
		return this.antecedenteToxicoDao.modificarPredefinido(con, codigoPaciente, this.codigo, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones);
	}
	
	public ResultadoBoolean modificarPredefinidoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteToxicoDao.modificarPredefinidoTransaccional(con, codigoPaciente, this.codigo, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones, estado);
	}			
	
	public ResultadoBoolean modificarOtro(Connection con, int codigoPaciente)
	{
		return this.antecedenteToxicoDao.modificarOtro(con, codigoPaciente, this.codigo, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones);
	}

	public ResultadoBoolean modificarOtroTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedenteToxicoDao.modificarOtroTransaccional(con, codigoPaciente, this.codigo, this.nombre, this.actual, this.cantidad, this.frecuencia, this.tiempoHabito, this.observaciones, estado);		
	}
	
	public ResultadoBoolean existeAntecedentePredefinido(Connection con, int codigoPaciente)
	{
		return this.antecedenteToxicoDao.existeAntecedentePredefinido(con, codigoPaciente, this.codigo);	
	}

	public ResultadoBoolean existeAntecedenteOtro(Connection con, int codigoPaciente)
	{
		return this.antecedenteToxicoDao.existeAntecedenteOtro(con, codigoPaciente, this.codigo);	
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
