package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.historiaClinica.ImagenesBaseDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseImagenesBaseDao;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public class PostgresqlImagenesBaseDao implements ImagenesBaseDao {
	
	public ArrayList<DtoImagenBase> consultarImagenesBase (Connection connection)
	{
		return SqlBaseImagenesBaseDao.consultarImagenesBase(connection);
	}
	
	public String insertarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario)
	{
		return SqlBaseImagenesBaseDao.insertarImagenesBase(connection, dtoImagenBase, usuario);
	}
	
	public String  eliminarImagenBase(Connection connection, int codigo_pk)
	{
		return SqlBaseImagenesBaseDao.eliminarImagenBase(connection, codigo_pk);
	}
	
	public String actualizarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario)
	{
		return SqlBaseImagenesBaseDao.actualizarImagenesBase(connection, dtoImagenBase, usuario);
	}
}