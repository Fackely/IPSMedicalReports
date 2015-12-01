package com.princetonsa.mundo.administracion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;

public class EnvioEmailAutomatico {
	public static boolean insertar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico, Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEnvioEmailAutomaticoDao().insertar(dtoEnvioEmailAutomatico, con);
	}
	
	
	
	public static boolean eliminar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico, Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEnvioEmailAutomaticoDao().eliminar(dtoEnvioEmailAutomatico, con);
	}
	
	
	
	

	public static ArrayList<DtoEnvioEmailAutomatico> listar(Connection con,
			int codigoInstitucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEnvioEmailAutomaticoDao().listar(con, codigoInstitucion);
	}



	public static boolean modificar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico, Connection con) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEnvioEmailAutomaticoDao().modificar(dtoEnvioEmailAutomatico, con);
		
	}
}
