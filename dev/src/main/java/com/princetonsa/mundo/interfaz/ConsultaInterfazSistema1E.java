package com.princetonsa.mundo.interfaz;

import java.io.BufferedReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dao.interfaz.ConsultaInterfazSistema1EDao;
import com.princetonsa.dao.interfaz.ParamInterfazSistema1EDao;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;


public class ConsultaInterfazSistema1E{
	
	//---------------------ATRIBUTOS
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ConsultaInterfazSistema1E.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultaInterfazSistema1EDao consultaInterfazSistema1EDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	
	// ----------------	METODOS
	
	/**
	 * Reset
	 */
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
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
			consultaInterfazSistema1EDao = myFactory.getConsultaInterfazSistema1EDao();
			wasInited = (consultaInterfazSistema1EDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	private static ConsultaInterfazSistema1EDao getConsultaInterfazSistema1EDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaInterfazSistema1EDao();
	}
	
	/**
	 * Método para buscar el log de la auditoria
	 */
	public static ArrayList<DtoLogInterfaz1E> consultarLog(Connection con, HashMap filtros)
	{
		return getConsultaInterfazSistema1EDao().consultarLog(con,filtros);
	}
}