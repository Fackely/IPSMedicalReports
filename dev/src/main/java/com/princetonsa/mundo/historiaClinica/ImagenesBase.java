package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ImagenesBaseDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseImagenesBaseDao;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public class ImagenesBase {

	Logger logger = Logger.getLogger(ImagenesBase.class);
	private DtoImagenBase dtoImagenBase;
	
	public void clean()
	{
		this.dtoImagenBase = new DtoImagenBase();
	}
	
	/**
	 * Constructor de la Clase
	 */
	private static ImagenesBaseDao getImagenesBaseDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImagenesBaseDao();
	}

	public ArrayList<DtoImagenBase> consultarImagenesBase (Connection connection)
	{
		return getImagenesBaseDao().consultarImagenesBase(connection);
	}
	
	public String insertarImagenesBase (Connection connection, UsuarioBasico usuario)
	{
		return getImagenesBaseDao().insertarImagenesBase(connection, this.dtoImagenBase, usuario);
	}
	
	public String eliminarImagenBase (Connection connection, int codigo_pk)
	{
		return getImagenesBaseDao().eliminarImagenBase(connection, codigo_pk);
	}
	
	public String actualizarImagenesBase (Connection connection, UsuarioBasico usuario)
	{
		return getImagenesBaseDao().actualizarImagenesBase(connection, this.dtoImagenBase, usuario);
	}

	/**
	 * @return the dtoImagenBase
	 */
	public DtoImagenBase getDtoImagenBase() {
		return dtoImagenBase;
	}

	/**
	 * @param dtoImagenBase the dtoImagenBase to set
	 */
	public void setDtoImagenBase(DtoImagenBase dtoImagenBase) {
		this.dtoImagenBase = dtoImagenBase;
	}
}