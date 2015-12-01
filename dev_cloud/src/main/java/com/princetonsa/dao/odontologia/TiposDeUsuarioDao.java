package com.princetonsa.dao.odontologia;

import java.util.ArrayList;


import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;

public interface TiposDeUsuarioDao {

	
	public ArrayList<DtoTiposDeUsuario> cargar(DtoTiposDeUsuario dtoWhere);
	
	
	
	
}
