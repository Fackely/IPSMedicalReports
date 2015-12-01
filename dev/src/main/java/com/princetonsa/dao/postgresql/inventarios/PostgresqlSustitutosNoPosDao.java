package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.SustitutosNoPosForm;
import com.princetonsa.dao.inventarios.SustitutosNoPosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseSustitutosNoPosDao;
import com.princetonsa.mundo.inventarios.SustitutosNoPos;


public class PostgresqlSustitutosNoPosDao implements SustitutosNoPosDao
{
	private static String consultaStr2="SELECT sub.subgrupo, " +
											"sub.grupo, " +
											"sub.clase, " +
											"UPPER(gi.nombre) AS nombreg, " +
											"UPPER(ci.nombre) AS nombrec, " +
											"ff.nombre AS nombreff, " +
											"UPPER(sub.nombre) AS nombresbg, " +
											"art.descripcion || ' CONC: ' || art.concentracion || ' NAT: ' || na.nombre || ' - ' || CASE WHEN na.es_pos='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN 'POS' ELSE 'NOPOS' END AS descart " +
										"FROM articulo art " +
											"INNER JOIN subgrupo_inventario sub ON (sub.codigo = art.subgrupo) " +
											"INNER JOIN grupo_inventario gi ON (sub.grupo=gi.codigo AND sub.clase=gi.clase) " +
											"INNER JOIN clase_inventario ci ON (gi.clase=ci.codigo) " +
											"INNER JOIN forma_farmaceutica ff ON (art.forma_farmaceutica=ff.acronimo AND art.institucion=ff.institucion) " +
											"INNER JOIN naturaleza_articulo na ON (art.naturaleza=na.acronimo AND art.institucion=na.institucion) " +
										"WHERE art.codigo = ?";

	
	/**
	 * Metodo de consulta de articulos sustitutos
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaSus (Connection con, int codigoArtPpal)
	{
		return SqlBaseSustitutosNoPosDao.consultaSus(con, codigoArtPpal);
	}
	
	public HashMap<String, Object> consultaCG (Connection con, int codigoArtPpal)
	{
		return SqlBaseSustitutosNoPosDao.consultaCG(con, codigoArtPpal, consultaStr2);
	}
	
	public boolean eliminarSustitutosNoPos(Connection con, String sustitutosNoPos) {
		
		return SqlBaseSustitutosNoPosDao.eliminar(con, sustitutosNoPos);
	}
	
	public boolean modificarSustitutosNoPos(Connection con, SustitutosNoPos sustitutosNoPos){
		return SqlBaseSustitutosNoPosDao.modificar(con, sustitutosNoPos);
	}
	
	public boolean insertarSustitutosNoPos(Connection con, SustitutosNoPos sustitutosNoPos){
		return SqlBaseSustitutosNoPosDao.insertar(con, sustitutosNoPos);
	}
}