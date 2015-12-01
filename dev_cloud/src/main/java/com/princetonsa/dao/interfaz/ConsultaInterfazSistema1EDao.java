package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;

public interface ConsultaInterfazSistema1EDao
{
	public ArrayList<DtoLogInterfaz1E> consultarLog(Connection con, HashMap filtros);
}