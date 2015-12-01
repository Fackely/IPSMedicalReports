/*
 * Creado el 12/04/2005
 * Juan David Ram�rez L�pez
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DiasFestivosDao;

/**
 * @author Juan David Ram�rez
 * 
 * CopyRight Princeton S.A.
 * 12/04/2005
 */
public class DiasFestivos
{
	/**
	 * Interfas encargada de la comunicaci�n
	 * con la BD
	 */
	private DiasFestivosDao diasFestivosDao;
	
	/**
	 * Fecha del d�a festivo
	 */
	private String fecha;
	/**
	 * Tipo del dia Festivo
	 */
	private int tipo;
	
	/**
	 * Nombre del tipo de d�a festivo
	 */
	private String nombreTipo; 
	
	/**
	 * Constructora de la clase 
	 */
	public DiasFestivos()
	{
		reset();
	}
	/**
	 * Descripci�n del d�a festivo
	 */
	private String descripcion;

	/**
	 * Inicializar la conexion con la BD
	 * @param tipoBD String con el tipo de BD a utilizar
	 */
	public void init(String tipoBD)
	{
		diasFestivosDao=DaoFactory.getDaoFactory(tipoBD).getDiasFestivosDao();
	}
	
	/**
	 * Metodo para inizializar los
	 * atributos de la clase
	 */
	public void reset()
	{
		init(System.getProperty("TIPOBD"));
		fecha="";
		tipo=0;
		descripcion="";
	}
	
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param descripcion Asigna descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Retorna fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param fecha Asigna fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return Retorna tipo.
	 */
	public int getTipo()
	{
		return tipo;
	}
	/**
	 * @param tipo Asigna tipo.
	 */
	public void setTipo(int tipo)
	{
		this.tipo = tipo;
	}

	/**
	 * M�todo para inserar un d�a festivo
	 * @param con Conexi�n con la BD
	 * @return n�mero de elementos ingresados
	 */
	public int insertar(Connection con)
	{
		return diasFestivosDao.insertar(con, fecha, descripcion, tipo);
	}

	/**
	 * M�todo para modificar un d�a festivo
	 * @param con Conexi�n con la BD
	 * @return n�mero de elementos  modificados
	 */
	public int modificar(Connection con)
	{
		return diasFestivosDao.modificar(con, fecha, descripcion, tipo);
	}

	/**
	 * M�todo para eliminar un d�a festivo
	 * @param con Conexi�n con la BD
	 * @return numero de elementos eliminados
	 */
	public int eliminar(Connection con)
	{
		return diasFestivosDao.eliminar(con, fecha);
	}

	/**
	 * M�todo para listar todos los d�as desftivos parametrizados para determinado a�o
	 * @param con Conexion con la BD
	 * @param anio A�o que se quiere consultar
	 * @return Collection con el listado de todos los d�as festivos
	 */
	public Collection listar(Connection con, String anio)
	{
		return diasFestivosDao.listar(con, anio);
	}

	/**
	 * M�todo para consultar si un d�a es festivo o no
	 * @param con Conexi�n con la BD
	 * @param fecha Fecha que se desea consultar
	 * @param incluirDomingos  true para incluir los domingos como festivos
	 * @return true si se carg� correcamente el d�a festivo
	 */
	public boolean cargar(Connection con, String fecha, boolean incluirDomingos)
	{
		Collection col=diasFestivosDao.cargar(con, fecha, incluirDomingos);
		Iterator iterador=col.iterator();
		if(iterador.hasNext())
		{
			HashMap fila=(HashMap)iterador.next();
			descripcion=(String)fila.get("descripcion");
			tipo=Integer.parseInt(fila.get("codigotipo")+"");
			nombreTipo=(String)fila.get("tipo");
			return true;
		}
		return false;
	}
	
	/**
	 * M�todo para consultar si un d�a es festivo o no
	 * @param con @todo
	 * @param fecha Fecha que se desea consultar
	 * @param incluirDomingos  true para incluir los domingos como festivos
	 * @param con Conexi�n con la BD
	 * @return true si se carg� correcamente el d�a festivo
	 */
	public static boolean esFestivo(Connection con, String fecha, boolean incluirDomingos)
	{
		Collection col=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDiasFestivosDao().cargar(con, fecha, incluirDomingos);
		Iterator iterador=col.iterator();
		if(iterador.hasNext())
			return true;
		else
			return false;
	}
	
	/**
	 * @return Retorna nombreTipo.
	 */
	public String getNombreTipo()
	{
		return nombreTipo;
	}
	/**
	 * @param nombreTipo Asigna nombreTipo.
	 */
	public void setNombreTipo(String nombreTipo)
	{
		this.nombreTipo = nombreTipo;
	}
}
