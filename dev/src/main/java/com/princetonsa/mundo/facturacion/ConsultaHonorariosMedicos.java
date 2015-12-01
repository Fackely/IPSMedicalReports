/*
 * Creado el 28/12/2005
 * Jorge Armando Osorio Velasquez	
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.ConsultaHonorariosMedicosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 * 
 * CopyRight Princeton S.A.
 * 28/12/2005
 */
public class ConsultaHonorariosMedicos
{
	ConsultaHonorariosMedicosDao honorariosDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{ 
	    if ( honorariosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			honorariosDao= myFactory.getConsultaHonorariosMedicosDao();
			if( honorariosDao!= null )
				return true;
		}
		return false;
	}
	/**
     * Constructor de la clase.
     */
    public ConsultaHonorariosMedicos()
    {
        this.reset();
    	init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * Mapa que contien los profesionales de la salud.
	 */
	private HashMap mapaProfesionales;
	
	/**
	 * Mapa para cargar los honorarios de los medicos
	 */
	private HashMap mapaHonorarios;
	
    /**
     * Reset de atributos.
     *
     */
	private void reset()
	{
		this.mapaProfesionales=new HashMap();
		this.mapaHonorarios=new HashMap();
	}
	
	/**
	 * Metodo que consulta los profesionales de la salud que estan relacionados a las facturas.
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarProfesionalesSalud(Connection con, HashMap vo)
	{
		mapaProfesionales=honorariosDao.consultarProfesionalesSalud(con,vo);
		return (HashMap)mapaProfesionales.clone();
	}
	/**
	 * @return Retorna mapaProfesionales.
	 */
	public HashMap getMapaProfesionales()
	{
		return mapaProfesionales;
	}
	/**
	 * @param mapaProfesionales Asigna mapaProfesionales.
	 */
	public void setMapaProfesionales(HashMap mapaProfesionales)
	{
		this.mapaProfesionales = mapaProfesionales;
	}
	
	/**
	 * Metodo que carga los honorarios de un profesional
	 * @param con
	 * @param institucion
	 * @param profesional
	 * @param restriciones 
	 * @return
	 */
	public HashMap consultarHonorariosProfesional(Connection con, int institucion, int profesional, HashMap restricciones)
	{
		mapaHonorarios=honorariosDao.consultarHonorariosProfesional(con,institucion,profesional,restricciones);
		return (HashMap)mapaHonorarios.clone();
	}
	/**
	 * @return Retorna mapaHonorarios.
	 */
	public HashMap getMapaHonorarios()
	{
		return mapaHonorarios;
	}
	/**
	 * @param mapaHonorarios Asigna mapaHonorarios.
	 */
	public void setMapaHonorarios(HashMap mapaHonorarios)
	{
		this.mapaHonorarios = mapaHonorarios;
	}
}
