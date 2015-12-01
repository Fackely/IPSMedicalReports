package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseImagenesBaseDao;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public interface ImagenesBaseDao {

	public ArrayList<DtoImagenBase> consultarImagenesBase(Connection connection);
	
	public String insertarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario);
	
	public String  eliminarImagenBase(Connection connection,int codigo_pk);
	
	public String actualizarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario);
}
