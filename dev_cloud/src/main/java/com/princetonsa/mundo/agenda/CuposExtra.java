/*
 * @(#)CuposExtra.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.agenda;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.princetonsa.dao.CuposExtraDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacci�n entre la capa
 * de control y el acceso a la informaci�n de la 
 * Cupos Extra
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /May/ 2006
 */
public class CuposExtra
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CuposExtra.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public CuposExtra()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>CuposExtraDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private CuposExtraDao cuposExtraDao ;

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			cuposExtraDao = myFactory.getCuposExtraDao();
			wasInited = (cuposExtraDao != null);
		}

		return wasInited;
	}
	
	
	
	/**
	 * M�todo para consultar las agendas disponibles segun unos parametros
	 * de b�squeda especificados
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoUnidadConsulta
	 * @param codigoConsultorio
	 * @param codigoDiaSemana
	 * @param codigoMedico
	 * @param centroAtencion 
	 * @return
	 */
	public HashMap busquedaAgendaGenerada (Connection con, String fechaIncial, String fechaFinal, int codigoUnidadConsulta, int codigoConsultorio, int codigoDiaSemana, int codigoMedico, String minutosEspera, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		return cuposExtraDao.busquedaAgendaGenerada(con, fechaIncial, fechaFinal, codigoUnidadConsulta, codigoConsultorio, codigoDiaSemana, codigoMedico, minutosEspera, centroAtencion, centrosAtencion, unidadesAgenda);
	}
	
	/**
	 * M�todo para actualizar el numero de cupos de una agenda determinada
	 * @param con
	 * @param numeroCupos
	 * @param codigoAgenda
	 * @return
	 */
	public int actualizarCuposEnAgenda(Connection con , int numeroCupos, int codigoAgenda)throws SQLException
	{
		return cuposExtraDao.actualizarCuposEnAgenda(con, numeroCupos, codigoAgenda);
	}
	
	/**
	 * M�todo para insertar
	 * @param con
	 * @param mapaCuposExtra
	 * @param usuario
	 * @param cadenaInsertar
	 * @return
	 */
	public int insertarCuposExtra(Connection con, HashMap mapaCuposExtra, String usuario) throws SQLException
	{
		return cuposExtraDao.insertarCuposExtra(con, mapaCuposExtra, usuario);
	}
	
}
