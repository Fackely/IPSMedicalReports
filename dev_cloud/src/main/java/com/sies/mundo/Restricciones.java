/*
 * Created on 24/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.RestriccionesDao;
import com.sies.dao.SiEsFactory;

/**
 *
 */
public class Restricciones
{
    /**
	 * Variable de Clase para la conexión con el DAO
	 */	
	private static RestriccionesDao restriccionesDao;
	
	/**
	 * codigo del medico
	 */
	private int codigoMedico;
    
	/**
	 * codigo de la asociación de la restriccion a la enfermera
	 */
	private int codigoAsociacion;
	
	/**
	 * @return Returns the codigoMedico.
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}
	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}
	
	static
	{
		if(restriccionesDao==null)
		{
			restriccionesDao=SiEsFactory.getDaoFactory().getRestriccionesDao();
		}
	}
	
	public Restricciones()
	{
		//clean();
	}
	
	/**
	 * Limpiar e inicializar atributos 
	 */
	public void clean()
	{
		codigoMedico=0;
		codigoAsociacion=0;
	}
	
	/**
	 * Método que hace una consulta de todas las restricciones que hay en la base de datos
	 * @param con
	 * @param tipo
	 * @return Collección con las restricciones
	 */
	public Collection consultarRestricciones(Connection con, int tipo)
	{
		return restriccionesDao.consultarRestricciones(con, tipo);
	}
    
	/**
	 * Método que consulta las restricciones que se encuentran asignadas en el momento 
	 * a determinada enfermera
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public Collection consultarRestriccionEnfermera(Connection con, int codigoMedico)
	{
		return restriccionesDao.consultarRestriccionEnfermera(con, codigoMedico);
	}
	
	/**
	 * Método que actualiza la fecha de finalización de la asociación a la fecha actual
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoMedico
	 * @param esRestriccion
	 * @return
	 */
	public int actualizarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoMedico, boolean esRestriccion)
	{
	    return restriccionesDao.actualizarRestriccionEnfermera(con, codigoRestriccion, codigoMedico, esRestriccion);
	}
	
	/**
	 * Método que inserta una nueva Restricción a la categoría
	 * en la tabla enfermera_restriccion
	 * @param con
	 * @param esRestriccion
	 * @param todosDias
	 * @return
	 */
	public int insertarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoMedico, String valor, boolean esRestriccion, boolean todosDias)
	{
		return Restricciones.restriccionesDao.insertarRestriccionPersona(con, codigoRestriccion, codigoMedico, valor, esRestriccion, todosDias);
	}

	/**
	 * Método para consultar las enfermeras que se encuentran activas en el sistema
	 * @param con
	 * @param restriccion
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeras(Connection con, String restriccion, String codigoInstitucion)
	{
	    return Restricciones.restriccionesDao.consultarPersonas(con, restriccion, codigoInstitucion);
	}

	/**
	 * @return Returns the codigoAsociacion.
	 */
	public int getCodigoAsociacion() {
        return codigoAsociacion;
    }
    /**
     * @param codigoAsociacion The codigoAsociacion to set.
     */
    public void setCodigoAsociacion(int codigoAsociacion) {
        this.codigoAsociacion = codigoAsociacion;
    }
}
