package com.princetonsa.mundo.enfermeria;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dao.postgresql.enfermeria.PostgresqlConsultaProgramacionCuidadosPacienteDao;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class ConsultaProgramacionCuidadosPaciente {

	private static ConsultaProgramacionCuidadosPacienteDao ObjetoDao;
	
	
	
	public ConsultaProgramacionCuidadosPaciente(){
		init (System.getProperty("TIPOBD"));
	}
	
	private boolean init(String tipoBD){
		if(ObjetoDao==null){
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			ObjetoDao =  myFactory.getConsultaProgramacionCuidadosPacienteDao();
			if(ObjetoDao!=null){
				return true;
			}
		}
		return false;
	}
/**
 * 
 * @param con
 * @param ingreso
 * @param codigoPkFrecCuidadoEnfer
 * @param activo
 * @return
 */
	public static ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(
			Connection con,
			String ingreso,
			int codigoPkFrecCuidadoEnfer,
			boolean activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPkFrecCuidadoEnfer",codigoPkFrecCuidadoEnfer);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return ObjetoDao.consultarFrecuenciaCuidado(con, parametros);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> consultarTipoFrecuenciaInst(Connection con,int institucion)
	{				
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		return ObjetoDao.consultarTipoFrecuenciaInst(con, parametros);
	}
	
	
	
	
	
}
