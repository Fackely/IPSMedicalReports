package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;

public interface IConsultaConsolidadoCierreDAO {
	
	
	/**
	 * @param institucion
	 * @return lista de empresasInstitucion
	 */
	public ArrayList<EmpresasInstitucion> consultarEmpresaInstitucionXinstitucion(Integer institucion);
	
	/**
	 * @return lista de centros de atencion
	 */
	public ArrayList<CentroAtencion> consultarCentrosAtencion();

}
