package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;

import com.princetonsa.dao.consultaExterna.MultasDao;
import com.princetonsa.dao.historiaClinica.ImagenesBaseDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMultasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseImagenesBaseDao;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ImagenesBase;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public class OracleImagenesBaseDao implements ImagenesBaseDao {
	
	public ArrayList<DtoImagenBase> consultarImagenesBase (Connection connection)
	{
		return SqlBaseImagenesBaseDao.consultarImagenesBase(connection);
	}
	
	public String insertarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario)
	{
		return SqlBaseImagenesBaseDao.insertarImagenesBase(connection, dtoImagenBase, usuario);
	}
	
	public String eliminarImagenBase(Connection connection, int codigo_pk)
	{
		return SqlBaseImagenesBaseDao.eliminarImagenBase(connection, codigo_pk);
	}
	
	public String actualizarImagenesBase (Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario)
	{
		return SqlBaseImagenesBaseDao.actualizarImagenesBase(connection, dtoImagenBase, usuario);
	}
}