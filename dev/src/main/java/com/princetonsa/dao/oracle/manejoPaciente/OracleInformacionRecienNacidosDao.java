/*
 * @author Jorge Armando Osorio Velasquez.
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.historiaClinica.InformacionRecienNacidosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseInformacionRecienNacidosDao;

/**
 * @author Jorge Armando Osorio Velasquez.
 */
public class OracleInformacionRecienNacidosDao implements InformacionRecienNacidosDao
{
	private static String insertarInfoParto="INSERT INTO info_parto_hijos (" +
										" consecutivo," +
										" consecutivo_hijo," +
										" fecha_nacimiento," +
										" hora_nacimiento," +
										" sexo," +
										" vivo," +
										" diagnostico_rn," +
										" cie_rn," +
										" fecha_muerte," +
										" hora_muerte," +
										" diagnostico_muerte," +
										" cie_muerte," +
										" cirugia," +
										" fallece_sala_parto," +
										" momento_muerte," +
										" peso_edad_gestacion," +
										" vitamina_k," +
										" profilaxis_oftalmico," +
										" hemoclasificacion," +
										" sensibilizado," +
										" defectos_congenitos," +
										" acro_diag_def_cong," +
										" tipo_cie_diag_def_cong," +
										" fecha_egreso," +
										" hora_egreso," +
										" condicion_egreso," +
										" lactancia," +
										" peso_egreso," +
										" nuip," +
										" vacuna_polio," +
										" vacuna_bcg," +
										" vacuna_hepatitis_b," +
										" sano_enfermo," +
										" conducta_seguir," +
										" observaciones_egreso," +
										" fecha_proceso," +
										" hora_proceso," +
										" usuario_proceso," +
										" codigo_profesional_atendio," +
										" conducta_seguir_ani," +
										" edad_gesta_examen," +
										" numero_cert_nacimiento," +
										" finalizada," +
										" codigo_enfermedad " +
										" ) " +
									" values(" +
										" seq_info_parto_hijos.nextval , " +
										" (SELECT  case when max(consecutivo_hijo) is null then 1 else max(consecutivo_hijo)+1 end from info_parto_hijos where cirugia = ?)," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?,?,?,?," +
										" ?,?) ";
	
	private static final String cadenaInsercionDx="INSERT INTO diag_egreso_rn (" +
													"codigo," +
													"codigo_iph," +
													"acronimo_diagnostico," +
													"tipo_cie_diagnostico," +
													"tipo_diagnostico," +
													"principal) values(seq_diag_egre_rn.nextval,?,?,?,?,?)";

	
	
	public HashMap consultarSolicitudes(Connection con, HashMap vo)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarSolicitudes(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public HashMap consultarHijosCirugia(Connection con, String cirugia)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarHijosCirugia(con,cirugia);
	}
	

	/**
	 * 
	 */
	public HashMap consultarHijo(Connection con, String codigoInfoParto,String institucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarHijo(con,codigoInfoParto,institucion);
	}


	
	/**
	 * 
	 */
	public boolean actualizarInformacionRecienNacido(Connection con,HashMap vo)
	{
		return SqlBaseInformacionRecienNacidosDao.actualizarInformacionRecienNacido(con,vo,cadenaInsercionDx);
	}


	/**
	 * 
	 */
	public boolean insertarInformacionGeneral(Connection con, HashMap vo)
	{
		return SqlBaseInformacionRecienNacidosDao.insertarInformacionGeneral(con,vo,insertarInfoParto,cadenaInsercionDx);
	}


	public HashMap consultarAdaptacionNeonatalInmediata(Connection con, String codigoInstitucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarAdaptacionNeonatalInmediata(con,codigoInstitucion);
	}


	public HashMap consultarReanimacion(Connection con)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarReanimacion(con);
	}


	public HashMap consultarTamizacionNeonatal(Connection con)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarTamizacionNeonatal(con);
	}


	public HashMap consultarExamenesFisicos(Connection con, String codigoInstitucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarExamenesFisicos(con, codigoInstitucion);
	}


	public HashMap consultarDiagnosticoRecienNacido(Connection con, String codigoInstitucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarDiagnosticoRecienNacido(con, codigoInstitucion);
	}


	public HashMap consultarApgar(Connection con, String codigoInstitucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarApgar(con,codigoInstitucion);
	}


	public HashMap consultarSano(Connection con, String codigoInstitucion)
	{
		return SqlBaseInformacionRecienNacidosDao.consultarSano(con,codigoInstitucion);
	}
	

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esInformacionHijoFinalizada(Connection con, String consecutivo)
	{
		return SqlBaseInformacionRecienNacidosDao.esInformacionHijoFinalizada(con,consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codCx
	 * @return
	 */
	public Vector obtenerConsecutivosInfoRecienNacidoDadoCx(Connection con, String codCx, String buscarFinalizada)
	{
		return SqlBaseInformacionRecienNacidosDao.obtenerConsecutivosInfoRecienNacidoDadoCx(con, codCx, buscarFinalizada);
	}
}
