package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * @author rcancino
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SqlBaseSalarioMinimoDao
{
	/**
	 * Consultar salarios
	 */
	private static String consultarSalarioStr="SELECT fecha_inicial AS fechaInicial,fecha_final AS fechaFinal, codigo AS codigo, salario || '' AS salario FROM salario_minimo  order by fecha_inicial desc" ;
	
	/**
	 * Consultar el salario vigente
	 */
	private static String consultarSalarioVigenteStr="SELECT fecha_inicial AS fechaInicial,fecha_final AS fechaFinal, codigo AS codigo, salario || '' AS salario FROM salario_minimo  where current_date between fecha_inicial and fecha_final" ;
	/**
	/**
	 * verificar si las fechas asociadas a la vigencia de un salario se cruzan
	 * con otro existente
	 */
	private static String verificarCruzeStr="SELECT COUNT(*) as cuenta FROM salario_minimo where fecha_inicial<=? AND fecha_final>=?";
	/**
	 * modificar un recargo
	 */
	public static String modificarSalarioStr="UPDATE salario_minimo " +
																		"SET ";
	
	public static ResultadoBoolean insertar(Connection con,double salario,String fechaInicial,String fechaFinal, String ingresarSalarioStr) throws SQLException{
		
		PreparedStatementDecorator insertarSalarioStatement =  new PreparedStatementDecorator(con.prepareStatement(ingresarSalarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarSalarioStatement.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
		insertarSalarioStatement.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
		insertarSalarioStatement.setDouble(3,salario);
		if(insertarSalarioStatement.executeUpdate()<1){
			return new ResultadoBoolean(false);
		}else{
			return new ResultadoBoolean(true);
		}
		
	}
	public static boolean existeCruzeSalario(Connection con,String fechaInicial,String fechaFinal) throws SQLException{
		
		
			PreparedStatementDecorator existeCruzeStmnt =  new PreparedStatementDecorator(con.prepareStatement(verificarCruzeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			existeCruzeStmnt.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			existeCruzeStmnt.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			ResultSetDecorator rs=new ResultSetDecorator(existeCruzeStmnt.executeQuery());
			rs.next();
			if(rs.getInt("cuenta")>0){
				return true;
			}else{
				return false;
			}
		}
	
	
	public static ResultadoCollectionDB consultar(Connection con) throws SQLException
	{

		PreparedStatementDecorator consultarSalarioStatement =  new PreparedStatementDecorator(con.prepareStatement(consultarSalarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(consultarSalarioStatement.executeQuery());
		Collection listadoCollection = UtilidadBD.resultSet2Collection(rs);
		if( listadoCollection.isEmpty())
		{
			return new ResultadoCollectionDB(false, "El salario no se encuentra parametrizado", listadoCollection);
		}
		else
		{	
			return new ResultadoCollectionDB(true, "", listadoCollection);	
		}
		
	
	}
	
	
	public static ResultadoCollectionDB consultarVigente (Connection con) throws SQLException
	{

		PreparedStatementDecorator consultarSalarioStatement =  new PreparedStatementDecorator(con.prepareStatement(consultarSalarioVigenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(consultarSalarioStatement.executeQuery());
		Collection listadoCollection = UtilidadBD.resultSet2Collection(rs);
		if( listadoCollection.isEmpty())
		{
			return new ResultadoCollectionDB(false, "El salario no se encuentra parametrizado", listadoCollection);
		}
		else
		{	
			return new ResultadoCollectionDB(true, "", listadoCollection);	
		}
		
	
	}
	
	public static ResultadoBoolean modificar(	Connection con,int codigo,double salario,String fechaInicial,String fechaFinal) throws SQLException
	{
		String condiciones="";
		String consulta="";
		boolean primeraCondicion=true;
		if(salario>=0){
			if(primeraCondicion){
				condiciones+=" salario="+salario;
				primeraCondicion=false;
			}else{
				condiciones+=", salario="+salario;
			}
		}
		if(!fechaInicial.equals("")){
			if(primeraCondicion){
				condiciones+=" fecha_inicial='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ";
				primeraCondicion=false;
			}else{
				condiciones+=", fecha_inicial='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' ";
			}
		}
		if(!fechaFinal.equals("")){
			if(primeraCondicion){
				condiciones+=" fecha_final='"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
				primeraCondicion=false;
			}else{
				condiciones+=", fecha_final='"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
			}
		}
		if(!primeraCondicion){
			condiciones+=" ";
		
			consulta=modificarSalarioStr+condiciones+"WHERE codigo="+codigo;
			
			PreparedStatementDecorator modificarSalarioStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			if(modificarSalarioStatement.executeUpdate()<1){
				return new ResultadoBoolean(false);
			}else{
				return new ResultadoBoolean(true);
			}
		}else{
			return new ResultadoBoolean(true);
		}
			
	}
	
	
}
