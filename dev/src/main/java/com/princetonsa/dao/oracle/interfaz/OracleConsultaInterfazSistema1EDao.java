package com.princetonsa.dao.oracle.interfaz;

import com.princetonsa.dao.interfaz.ConsultaInterfazSistema1EDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseConsultaInterfazSistema1EDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;;


public class OracleConsultaInterfazSistema1EDao implements ConsultaInterfazSistema1EDao
{
	public ArrayList<DtoLogInterfaz1E> consultarLog(Connection con, HashMap filtros)
	{
		return SqlBaseConsultaInterfazSistema1EDao.consultarLog(con,filtros);
	}
}