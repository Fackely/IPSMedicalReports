package com.sies.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.IngresarTurnoDao;
import com.sies.dao.SiEsFactory;

public class IngresarTurno
{
	/**
	 * Conexi�n con la BD
	 */
	IngresarTurnoDao ingresarTurnoDao;
	
	/**
	 * C�digo del turno
	 */
	private int codigo;
	
	/**
	 * Descripci�n del turno
	 */
	private String descripcion;
	
	/**
	 * Manejo de los s�mbolos
	 */
	private String simbolo;
	
	/**
	 * Color de la letra representativa del turno
	 */
	private String colorLetra;

	/**
	 * Color del fondo del turno
	 */
	private String colorFondo;

	/**
	 * Hora de inicio del turno
	 */
	private String horaInicio;

	/**
	 * Hora finalizaci�n del turno
	 */
	private String numeroHoras;
	
	/**
	 * Tipo de turno (Ma�ana, tarde, corrido o otro)
	 */
	private char tipoTurno;
	
	/**
	 * Indica en que d�as se realiza el turno
	 */
	private int esFestivo;
	
	/**
	 * Centro de costo relacionado al turno
	 */
	private Integer centroCosto;
	
	// Constructores
	
	public IngresarTurno()
	{
		ingresarTurnoDao=SiEsFactory.getDaoFactory().getIngresarTurnoDao();
	}
	
	// M�todos
	
	/**
	 * M�todo para consultar los tipos de turnos
	 * @param tipoConsulta
	 */
	public Collection<HashMap<String, Object>> consultarTipos(Connection con, int tipoConsulta)
	{
		return ingresarTurnoDao.consultarTipos(con, tipoConsulta);
	}

	/**
	 * M�todo para guardar un turno nuevo
	 * @param con
	 * @return true si insert� bien
	 */
	public boolean guardar(Connection con)
	{
		codigo=ingresarTurnoDao.guardarModificar(con, descripcion, horaInicio, numeroHoras, simbolo, tipoTurno, colorLetra, colorFondo, codigo, false, esFestivo, centroCosto);
		return codigo>0;
	}

	/**
	 * M�todo para guardar un turno nuevo
	 * @param con
	 * @return true si insert� bien
	 */
	public boolean modificar(Connection con)
	{
		codigo=ingresarTurnoDao.guardarModificar(con, descripcion, horaInicio, numeroHoras, simbolo, tipoTurno, colorLetra, colorFondo, codigo, true, esFestivo, centroCosto);
		return codigo>0;
	}

	/**
	 * M�todo para eliminar un tipo de turno
	 * La eliminaci�n se hace inactivando el turno espec�fico
	 * @param con
	 * @param codigo
	 */
	public int inactivar(Connection con, int codigo)
	{
		return ingresarTurnoDao.inactivar(con, codigo);
	}

	// getters y setters
	
	/**
	 * @return colorFondo
	 */
	public String getColorFondo()
	{
		return colorFondo;
	}

	/**
	 * @param colorFondo Asigna colorFondo
	 */
	public void setColorFondo(String colorFondo)
	{
		this.colorFondo = colorFondo;
	}

	/**
	 * @return colorLetra
	 */
	public String getColorLetra()
	{
		return colorLetra;
	}

	/**
	 * @param colorLetra Asigna colorLetra
	 */
	public void setColorLetra(String colorLetra)
	{
		this.colorLetra = colorLetra;
	}

	/**
	 * @return descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * @return numeroHoras
	 */
	public String getNumeroHoras()
	{
		return numeroHoras;
	}

	/**
	 * @param numeroHoras Asigna numeroHoras
	 */
	public void setNumeroHoras(String numeroHoras)
	{
		this.numeroHoras = numeroHoras;
	}

	/**
	 * @return horaInicio
	 */
	public String getHoraInicio()
	{
		return horaInicio;
	}

	/**
	 * @param horaInicio Asigna horaInicio
	 */
	public void setHoraInicio(String horaInicio)
	{
		this.horaInicio = horaInicio;
	}

	/**
	 * @return simbolo
	 */
	public String getSimbolo()
	{
		return simbolo;
	}

	/**
	 * @param simbolo Asigna simbolo
	 */
	public void setSimbolo(String simbolo)
	{
		this.simbolo = simbolo;
	}

	/**
	 * @return tipoTurno
	 */
	public char getTipoTurno()
	{
		return tipoTurno;
	}

	/**
	 * @param tipoTurno Asigna tipoTurno
	 */
	public void setTipoTurno(char tipoTurno)
	{
		this.tipoTurno = tipoTurno;
	}

	/**
	 * @return codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return esFestivo
	 */
	public int getEsFestivo()
	{
		return esFestivo;
	}

	/**
	 * @param esFestivo Asigna esFestivo
	 */
	public void setEsFestivo(int esFestivo)
	{
		this.esFestivo = esFestivo;
	}

	/**
	 * @return Obtiene centroCosto
	 */
	public Integer getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna centroCosto
	 */
	public void setCentroCosto(Integer centroCosto)
	{
		this.centroCosto = centroCosto;
	}
}
