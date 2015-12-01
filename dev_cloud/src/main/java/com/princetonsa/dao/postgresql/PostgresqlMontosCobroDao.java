/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.dao.MontosCobroDao;
import com.princetonsa.dao.sqlbase.SqlBaseMontosCobroDao;
import com.princetonsa.mundo.cargos.MontosCobro;

/**
 * @author Juan David Ramírez
 * Implementación de MontosCobroDao para manejar los montos de cobro
 */
public class PostgresqlMontosCobroDao implements MontosCobroDao
{
	/**
	 * Sentencia para insertar montos de cobro
	 */
	private static final String insertarMontoStr="INSERT INTO montos_cobro (codigo, convenio, via_ingreso, tipo_afiliado, estrato_social, tipo_monto, valor, porcentaje, activo, vigencia_inicial) values (nextval('seq_montos_cobro'), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Ingresar montos
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @return
	 */
	public boolean insertarMonto(Connection con, int convenio,
			int viaIngreso, String tipoAfiliado, int estratoSocial,
			int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
	    return SqlBaseMontosCobroDao.insertarMonto(con, convenio,
				viaIngreso, tipoAfiliado, estratoSocial,
				tipoMonto, valor, porcentaje, activo, fecha, insertarMontoStr);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @param esModificar
	 * @return collection con los resultados de la búsqueda de montos
	 */
	public Collection buscarMontos(Connection con, int codigo, int convenio,
			int viaIngreso, String tipoAfiliado, int estratoSocial,
			int tipoMonto, float valor, float porcentaje, String activo, boolean esModificar, String fecha)
	{
		return SqlBaseMontosCobroDao.buscarMontos(con, codigo, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, esModificar, fecha);
	}

	/**
	 * Clase para modificar los montos de cobro
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @return
	 */
	public boolean modificarMonto(Connection con, int codigo, int convenio,
			int viaIngreso, String tipoAfiliado, int estratoSocial,
			int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
		return SqlBaseMontosCobroDao.modificarMonto(con, codigo, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, fecha);
	}

	/**
	 * Método para cargar un monto de cobro
	 * @param con Conexión con la BD
	 * @param codigo del monto de cobro
	 * @return Collection con los datos del monto de cobro
	 */
	public Collection cargar(Connection con, int codigo)
	{
		return SqlBaseMontosCobroDao.cargar(con, codigo);
	}
	
	/**
	 * Adición Sebastián
	 * Método para cargar los códigos de los montos de cobro de acuerdo
	 * a ciertos parámetros, estos parámetros de búsqueda pueden omitirse
	 * con 0 (Enteros) o "" (String)
	 * @param con
	 * @param convenio
	 * @param tipoAfiliado
	 * @param estrato
	 * @param viaIngreso
	 * @return
	 */
	public Collection consultarMontosCobro(Connection con,int convenio,String tipoAfiliado,int estrato,int viaIngreso){
		return SqlBaseMontosCobroDao.consultarMontosCobro(con,convenio,tipoAfiliado,estrato,viaIngreso);
	}
}
