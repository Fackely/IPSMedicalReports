package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.interfaz.DtoConceptosParam1E;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;

public interface ParamInterfazSistema1EDao
{
	public boolean ingresarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E);
	
	public DtoInterfazParamContaS1E consultarParamGenerales(Connection con,HashMap parametros);
	
	public boolean actualizarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E);
	
	public boolean ingresarTiposDoc(Connection con, DtoTiposInterfazDocumentosParam1E dto);	
	
	public ArrayList<DtoTiposInterfazDocumentosParam1E> consultarTiposDocs(Connection con);
	
	public ArrayList consultarTiposDoc1E(Connection con);
	
	public boolean eliminarTiposDoc(Connection con,String indice);
	
	public DtoTiposInterfazDocumentosParam1E consultarInfoTipoDocUnitario(Connection con, String indice);
	
	public boolean actualizarDocParam(Connection con, String indice, DtoTiposInterfazDocumentosParam1E dto);
	
	public boolean insertarLog(Connection con, DtoLogParamGenerales1E dto);
	
	/**
	 * Consulta Listado Concepto Param 1E 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoConceptosParam1E> cargarConceptosparam1E(Connection con, HashMap parametros);
	
	/**
	 * Insercion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public int insertarConceptoParam1E(Connection con, DtoConceptosParam1E dto);
	
	/**
	 * Inactivacion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public int inactivarConceptoParam1E(Connection con, DtoConceptosParam1E dto);
	
	public ArrayList consultarEventos(Connection con);
	
	public boolean insertarEventosParam1E(Connection con, DtoEventosParam1E dto);
	
	public ArrayList<DtoEventosParam1E> consultarEventosParam1E(Connection con,String consecutivo);
	
	public boolean inactivarEvento(Connection con, DtoEventosParam1E dto);
	
	public boolean validarEventoUnico(Connection con, DtoEventosParam1E dto);
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap<String, Object> getTiposConseTipDoc(Connection con, HashMap parametros);
}