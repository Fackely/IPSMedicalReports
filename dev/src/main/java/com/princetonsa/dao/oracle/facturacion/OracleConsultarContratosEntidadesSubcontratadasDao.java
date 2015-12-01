package com.princetonsa.dao.oracle.facturacion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;

/**
 * 
 * @author Julio Hernandez jfhernandez@axioma-md.com
 *
 */
public class OracleConsultarContratosEntidadesSubcontratadasDao implements ConsultarContratosEntidadesSubcontratadasDao {
	
	public ArrayList obtenerEntidades(Connection con) {
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.obtenerEntidades(con);
	}
	
	public ArrayList obtenerClaseInventarios(Connection con) {
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.obtenerClaseInventarios(con);
	}
	
	public ArrayList obtenerEsquemas(Connection con) {
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.obtenerEsquemas(con);
	}
	
	public ArrayList obtenerGruposServicio(Connection con) {
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.obtenerGruposServicio(con);
	}
	
	public ArrayList obtenerEsquemasProcedimientos(Connection con) {
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.obtenerEsquemasProcedimientos(con);
	}
	
	public HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.consultaContratos(con, encabezado,inventarios,servicios);
	}
	
	public HashMap consultaEsquemasInventarios(Connection con)
	{
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.consultarEsquemasInventarios(con);
	}
	
	public HashMap consultaEsquemasServicios(Connection con)
	{
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.consultaEsquemasServicios(con);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo de de tarifa 
	 * manejado en el contrato de una entidad subcontratada, a través
	 * de la entidad subcontratada y el número de contrato
	 * 
	 * @param Connection con, DtoContratoEntidadSub dto
	 * @return DtoContratoEntidadSub
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoContratoEntidadSub  consultarTipoTarifaEntidadSubcontratada(Connection con, DtoContratoEntidadSub dto){
		return SqlBaseConsultarContratosEntidadesSubcontratadasDao.consultarTipoTarifaEntidadSubcontratada(con, dto);
	}
}