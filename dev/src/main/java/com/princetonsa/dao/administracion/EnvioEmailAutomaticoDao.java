package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;

public interface EnvioEmailAutomaticoDao {
	public boolean insertar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico, Connection con);

	public ArrayList<DtoEnvioEmailAutomatico> listar(Connection con,
			int codigoInstitucion);

	public boolean eliminar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con);

	public boolean  modificar(DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico,
			Connection con);
	
	
	
	
	
}
