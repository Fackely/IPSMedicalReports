/*
 * @(#)SistemasMotivoConsulta.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.historiaClinica;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.SistemasMotivoConsultaDao;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * Sistemas Motivo de COnsulta de Urgencias
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 31 /May/ 2006
 */
public class SistemasMotivoConsulta
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(SistemasMotivoConsulta.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public SistemasMotivoConsulta()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>SistemasMotivoConsultaDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private SistemasMotivoConsultaDao sistemasMotivoConsultaDao ;

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			sistemasMotivoConsultaDao = myFactory.getSistemasMotivoConsultaDao();
			wasInited = (sistemasMotivoConsultaDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para consultar los sistemas motivos de consulta de urgencias existenes en la base de datos
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMotivosConsultaUrg(Connection con , int institucion) throws SQLException
	{
		return sistemasMotivoConsultaDao.consultarMotivosConsultaUrg(con, institucion);
	}
	
	/**
	 * Método para eliminar un motivo de consulta de urgencias dado su codigo
	 * @param con
	 * @param codigoMotivo
	 * @return int
	 * @throws SQLException
	 */
	public int eliminarMotivoConsultaUrg(Connection con, int codigoMotivo) throws SQLException
	{
		return sistemasMotivoConsultaDao.eliminarMotivoConsultaUrg(con, codigoMotivo);
	}
	
	/**
	 * Método para la insercion de un nuevo Motivo de Consulta de Urgencias con todos sus atributos
	 * @param con
	 * @param codigoMotivo
	 * @param descripcion
	 * @param identificador
	 * @param institucion
	 * @return int
	 * @throws SQLException
	 */
	public int insertarMotivoConsultaUrg(Connection con, String codigoMotivo, String descripcion, String identificador, int institucion) throws SQLException
	{
		 return sistemasMotivoConsultaDao.insertarMotivoConsultaUrg(con, codigoMotivo, descripcion, identificador, institucion);
	}
	
	/**
	 * Método para saber si un sistema motivo de consulta de urgencias esta siendo utilizado en 
	 * la funcionalidad de signos sintomas x sistema
	 * @param con
	 * @param codigoMotivo
	 * @param institucion
	 * @return
	 */
	public boolean estaSiendoUtilizada(Connection con, int codigoMotivo, int institucion)
	{
		return sistemasMotivoConsultaDao.estaSiendoUtilizada(con, codigoMotivo, institucion);
	}
	
}
	