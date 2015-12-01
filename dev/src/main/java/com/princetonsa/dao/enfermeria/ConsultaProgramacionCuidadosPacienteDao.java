package com.princetonsa.dao.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public interface ConsultaProgramacionCuidadosPacienteDao {

	     
	
   /**
    * 
    * @param con
    * @param parametros
    * @return
    */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con, HashMap parametros);

	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
    public ArrayList<HashMap<String, Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros);
	

	
}
