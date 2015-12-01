/*
 * @(#)SqlBaseCuentasProcesoFacturacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para cuentas proceso facturacion 
 *
 * @version 1.0, Julio 06 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseCuentasProcesoFacturacionDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCuentasProcesoFacturacionDao.class);
	
	/**
	 * listado de las cuentas en proceso de facturacion
	 */
	private final static String listadoStr="SELECT " +
											"cpf.cuenta AS numero_cuenta, " +
											"getnombreviaingreso(c.via_ingreso) AS via_ingreso, " +
											"getnombreestadocuenta(cpf.estado) AS estado_anterior, " +
											"getnombreusuario(cpf.usuario) AS usuario, " +
											"to_char(cpf.fecha, 'DD/MM/YYYY') AS fecha, " +
											"substr(cpf.hora, 0,6) AS hora, " +
											"getnombretipoidentificacion(p.tipo_identificacion) ||' '|| p.numero_identificacion AS tipo_no_id_paciente, " +
											"getnombrepersona(p.codigo) AS paciente, " +
											"cpf.id_sesion as idsesion " +
											"from " +
											"cuentas_proceso_fact cpf " +
											"INNER JOIN cuentas c ON (c.id=cpf.cuenta) " +
											"INNER JOIN personas p ON(c.codigo_paciente=p.codigo) " +
											"INNER JOIN usuarios u ON (u.login=cpf.usuario) " +
											"INNER JOIN centros_costo cc ON (cc.codigo=c.area)" +
											"WHERE cc.centro_atencion=? and u.institucion=? ";
	
	
	/**
	 * listado X Paciente
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap listadoXPaciente(	Connection con, 
	        								String codigoCentroAtencion,
	        								int codigoInstitucion,
	        								String codigoPersona
	        								)
	{
		String listado= listadoStr+" and p.codigo= ? ";
	    try
		{
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setString(1, codigoCentroAtencion);
			cargarStatement.setInt(2, codigoInstitucion);
			cargarStatement.setString(3, codigoPersona);
			HashMap mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			mapa.put("esXPaciente", "true");
			return mapa;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBaseCuentasProcesoFacturacionDao "+e.toString());
			return new HashMap();
		}
	}
	
	
	/**
	 * listado X Todos
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listadoXTodos(	Connection con, 
	        								String codigoCentroAtencion,
	        								int codigoInstitucion
	        								)
	{
		String fechaHora[]=UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), -1*(Integer.parseInt(ValoresPorDefecto.getModificarMinutosEsperaCuentasProcFact(codigoInstitucion))), false);
		String listado= listadoStr+" " +
						"and ((cpf.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(fechaHora[0])+ "') or "+
						"     (cpf.fecha = '"+UtilidadFecha.conversionFormatoFechaABD(fechaHora[0])+ "' and cpf.hora<= '"+fechaHora[1]+"')) ";
	    try
		{
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setString(1, codigoCentroAtencion);
			cargarStatement.setInt(2, codigoInstitucion);
			HashMap mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			return mapa;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado X Todos: SqlBaseCuentasProcesoFacturacionDao "+e.toString());
			return new HashMap();
		}
	}
	
	
	
	
}
