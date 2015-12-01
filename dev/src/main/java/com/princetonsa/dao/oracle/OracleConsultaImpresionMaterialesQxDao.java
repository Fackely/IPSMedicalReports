/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import com.princetonsa.dao.ConsultaImpresionMaterialesQxDao;
import com.princetonsa.dao.MaterialesQxDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaImpresionMaterialesQxDao;
import com.princetonsa.dao.sqlbase.SqlBaseMaterialesQxDao;

/**
 * @author Luis Gabriel Chavez Salazar
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Consulta Materiales Qx
 */
public class OracleConsultaImpresionMaterialesQxDao implements ConsultaImpresionMaterialesQxDao 
{
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(OracleMaterialesQxDao.class);
	  
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param espaciente
	 * @return
	 */
	public HashMap consultaIngresosPaciente(Connection con, HashMap criterios) {
		
		return SqlBaseConsultaImpresionMaterialesQxDao.consultaIngresosPaciente(con, criterios);
	}
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @return
	 */        
	public HashMap cargarSolicitudesCx(Connection con, HashMap criterios, boolean espaciente)
	{
		return SqlBaseConsultaImpresionMaterialesQxDao.cargarSolicitudesCx(con, criterios,espaciente);
	}
	
	
	/**
 	 * @param con
	 * @param criterios
	 * @param esacto 
	 */
	public HashMap cargarConsumoMaterialesCirugias(Connection con, HashMap criterios, boolean esacto) {
		
		return SqlBaseConsultaImpresionMaterialesQxDao.cargarConsumoMaterialesCirugias(con,criterios,esacto);
	}
}
