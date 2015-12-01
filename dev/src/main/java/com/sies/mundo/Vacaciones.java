/*
 * Created on 11/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sies.dao.SiEsFactory;
import com.sies.dao.VacacionesDao;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Vacaciones {
	
	/**
	 * Código del profesional de la salud
	 */
	private int codigo;
	
	/**
	 * Fecha de inicio de las vacaciones del profesional de la salud
	 */
	private String fechaInicio;
	
	
	private String fechaAnterior;
	
	/**
	 * Fecha de finalización de las vacaciones del profesional de la Salud 
	 */
	private String fechaFin;
	
	private static VacacionesDao vacacionesDao;

	static
	{
		if(vacacionesDao==null)
		{
			vacacionesDao=SiEsFactory.getDaoFactory().getVacacionesDao();
		}
	}
	
	public Vacaciones()
	{
		this.clean();
	}
	
	/**
	 * Limpiar e inicializar atributos 
	 */
	public void clean()
	{
		codigo=0;
		fechaInicio="";
		fechaFin="";
	}
	
	public int insertar (Connection con)
	{
		return Vacaciones.vacacionesDao.insertarVacaciones(con, codigo, fechaInicio, fechaFin);		
	}
	
	public Collection<HashMap<String, Object>> consultarVacaciones(Connection con, Integer codigoPersona, String fechaInicio, String fechaFin)
	{
	    return Vacaciones.vacacionesDao.consultarVacaciones(con, codigoPersona, fechaInicio, fechaFin);
	}
	
	public void consultarModificar (Connection con) throws SQLException
	{
		Collection vacaciones = vacacionesDao.consultarModificar(con, codigo, fechaInicio);
		
		Iterator<HashMap<String, Object>> iterador=vacaciones.iterator();
		
		for(int i=0; i<vacaciones.size(); i++)
		{
			HashMap<String, Object> fila=iterador.next();
			this.setCodigo((Integer)fila.get("codigo"));
			this.setFechaInicio((String)fila.get("fecha_inicio"));
			this.setFechaFin((String)fila.get("fecha_fin"));
			this.setFechaAnterior((String)fila.get("fecha_inicio"));
		}
	}
	
	
	public void modificar(Connection con)
	{
	    Vacaciones.vacacionesDao.modificar(con,codigo,fechaInicio, fechaFin, fechaAnterior);
	}
	
	public int eliminarVacaciones(Connection con, int codigo, String fecha_inicio)
	{
	    return Vacaciones.vacacionesDao.eliminarVacaciones(con, codigo, fecha_inicio);
	}
	
	
    /**
     * @return Returns the fechaAnterior.
     */
    public String getFechaAnterior() {
        return fechaAnterior;
    }
    /**
     * @param fechaAnterior The fechaAnterior to set.
     */
    public void setFechaAnterior(String fecha_ant) {
        this.fechaAnterior = fecha_ant;
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
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}
}

