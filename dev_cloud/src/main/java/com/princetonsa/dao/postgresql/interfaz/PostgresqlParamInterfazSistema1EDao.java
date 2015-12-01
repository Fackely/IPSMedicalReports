package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import util.InfoDatosInt;
import com.princetonsa.dao.interfaz.ParamInterfazSistema1EDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseParamInterfazSistema1EDao;
import com.princetonsa.dto.interfaz.DtoConceptosParam1E;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;


public class PostgresqlParamInterfazSistema1EDao implements ParamInterfazSistema1EDao
{
	public boolean ingresarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		return SqlBaseParamInterfazSistema1EDao.ingresarParamGenerales(con,dtoInterfazSis1E);
	}
	
	public DtoInterfazParamContaS1E consultarParamGenerales(Connection con,HashMap parametros)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarParamGenerales(con,parametros);
	}
	
	public boolean actualizarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		return SqlBaseParamInterfazSistema1EDao.actualizarParamGenerales(con,dtoInterfazSis1E);
	}
	
	public boolean ingresarTiposDoc(Connection con, DtoTiposInterfazDocumentosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.ingresarTiposDoc(con,dto);
	}
	
	public ArrayList<DtoTiposInterfazDocumentosParam1E> consultarTiposDocs(Connection con)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarTiposDocs(con);
	}
	
	public ArrayList consultarTiposDoc1E(Connection con)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarTiposDoc1E(con);
	}
	
	public boolean eliminarTiposDoc(Connection con, String indice)
	{
		return SqlBaseParamInterfazSistema1EDao.eliminarTiposDoc(con,indice);
	}
	
	public DtoTiposInterfazDocumentosParam1E consultarInfoTipoDocUnitario(Connection con, String indice)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarInfoTipoDocUnitario(con,indice);
	}
	
	public boolean actualizarDocParam(Connection con, String indice, DtoTiposInterfazDocumentosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.actualizarDocParam(con,indice,dto);
	}
	
	public boolean insertarLog(Connection con, DtoLogParamGenerales1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.insertarLog(con,dto);
	}
	
	/**
	 * Consulta Listado Concepto Param 1E 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoConceptosParam1E> cargarConceptosparam1E(Connection con, HashMap parametros)
	{
		return SqlBaseParamInterfazSistema1EDao.cargarConceptosparam1E(con, parametros);
	}
	
	/**
	 * Insercion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public int insertarConceptoParam1E(Connection con, DtoConceptosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.insertarConceptoParam1E(con, dto);
	}
	
	/**
	 * Inactivacion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public int inactivarConceptoParam1E(Connection con, DtoConceptosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.inactivarConceptoParam1E(con, dto);
	}
	
	public ArrayList consultarEventos(Connection con)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarEventos(con);
	}
	
	public boolean insertarEventosParam1E(Connection con, DtoEventosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.insertarEventosParam1E(con, dto);
	}
	
	public ArrayList<DtoEventosParam1E> consultarEventosParam1E(Connection con, String consecutivo)
	{
		return SqlBaseParamInterfazSistema1EDao.consultarEventosParam1E(con, consecutivo);
	}
	
	public boolean inactivarEvento (Connection con, DtoEventosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.inactivarEvento(con, dto);
	}
	
	public boolean validarEventoUnico(Connection con, DtoEventosParam1E dto)
	{
		return SqlBaseParamInterfazSistema1EDao.validarEventoUnico(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap<String, Object> getTiposConseTipDoc(Connection con, HashMap parametros)
	{
		return SqlBaseParamInterfazSistema1EDao.getTiposConseTipDoc(con, parametros);
	}
}