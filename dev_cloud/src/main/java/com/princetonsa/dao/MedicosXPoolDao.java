
/*
 * Creado   29/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;



/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la 
 * clase que presta el servicio de acceso a datos para el objeto <code>MedicosXPool</code>.
 *
 * @version 1.0, 29/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface MedicosXPoolDao 
{
    /**
	 * Metodo que realiza la consulta de uno ó varios MedicosXPool.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, Codigo del tipo de pool.
	 * @param consultaUno, Boolean indica que tipo de consulta se realiza.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.SqlBaseMedicosXPoolDao#consultaPooles(java.sql.Connection,int,boolean)
	 */
	public ResultSetDecorator consultaPooles (Connection con, int codigoPool,boolean consultaUno);
	
	
	/**
	 *  Inserta los datos de uno ó varios médicos por pool
	 * @param con, Connection con la fuente de datos
	 * @param fechaIngreso, fecha de ingreso del medico al pool
	 * @param fechaRetiro, fecha de retiro del medico al pool
	 * @param horaIngreso, hora de ingreso del medico al pool
	 * @param horaRetiro, hora de retiro del medico al pool
	 * @param medico, código del médico
	 * @param pool, código del pool
	 * @param porcentaje, porcentaje de participación
	 * @see com.princetonsa.dao.SqlBaseMedicosXPoolDao#insertarMedicoPool(java.sql.Connection,String,String,String,String,int,int,double)
	 * @return siInserto, boolean true efectivo, false de lo contrario
	 */
	public boolean insertarMedicoPool (Connection con,
													        String fechaIngreso,
													        String fechaRetiro,
													        String horaIngreso,
													        String horaRetiro,
													        int medico, 
													        int pool, 
													        double porcentaje);
	
	/**
	 * Modifica uno ó varios registros de médicos x pool
	 * @param con, Connection con la fuente de datos
	 * @param medico, código del médico
	 * @param pool, código del pool
	 * @param fechaIngreso, fecha de ingreso del medico al pool
	 * @param fechaRetiro, fecha de retiro del medico al pool
	 * @param porcentaje, porcentaje de participación
	 * @see com.princetonsa.dao.SqlBaseMedicosXPoolDao#modificar(java.sql.Connection,int,int,String,String,double, ValoracionHospitalariaForm)
	 * @return boolean, true si modifico, false de lo contrario
	 */
	public boolean modificar (Connection con,int medico,int pool,String fechaIngreso,String fechaRetiro,double porcentaje);
	
	/**
	 * Consulta Avanzada, segun los parametros recibidos
	 * @param con, Connection con la fuente de datos
	 * @param fechaIngreso, fecha de ingreso
	 * @param fechaRetiro, fecha de retiro
	 * @param medico, codigo del medico
	 * @param pool, codigo del pool
	 * @param porcentaje, porcentaje de participación
	 * @return
	 * @see com.princetonsa.dao.SqlBaseMedicosXPoolDao#consultaPoolesAvanzada(java.sql.Connection,String,String,int,int,double)
	 */
	public  ResultSetDecorator consultaPoolesAvanzada (Connection con, 
			        												String fechaIngreso,
															        String fechaRetiro,
															        int medico, 
															        int pool, 
															        double porcentaje);
}
