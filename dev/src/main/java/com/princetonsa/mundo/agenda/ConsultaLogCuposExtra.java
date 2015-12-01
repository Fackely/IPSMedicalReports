/*
 * @(#)ConsultaLogCuposExtra.java
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
import com.princetonsa.dao.ConsultaLogCuposExtraDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacci�n entre la capa
 * de control y el acceso a la informaci�n de la 
 * Consulta LOG de Cupos Extra
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /MAy/ 2006
 */
public class ConsultaLogCuposExtra
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaLogCuposExtra.class);
	
	/**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaLogCuposExtra()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultaLogCuposExtraDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaLogCuposExtraDao consultaLogCuposExtraDao ;

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
			consultaLogCuposExtraDao = myFactory.getConsultaLogCuposExtraDao();
			wasInited = (consultaLogCuposExtraDao != null);
		}

		return wasInited;
	}
	
	/**
	 * M�todo para consultar el LOG de cupos extras segun los parametros de busqueda definidos
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoMedico
	 * @param codigoUnidadConsulta
	 * @param centroAtencion 
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarLogCuposExtra (Connection con, String fechaIncial, String fechaFinal, int codigoMedico, int codigoUnidadConsulta, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		return consultaLogCuposExtraDao.consultarLogCuposExtra(con, fechaIncial, fechaFinal, codigoMedico, codigoUnidadConsulta, centroAtencion, centrosAtencion, unidadesAgenda);
	}
}
	