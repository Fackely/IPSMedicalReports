package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;



public class SqlBaseUnidadPagoDao {

	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUnidadPagoDao.class);

	
	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public static HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		int nroConsulta = 0;
		String consulta = "";
		
		if ( !UtilidadCadena.noEsVacio(mapaParam.get("nroConsulta")+"") ) {	return new HashMap(); }
		else { nroConsulta = Integer.parseInt(mapaParam.get("nroConsulta")+""); }
			
		switch (nroConsulta)
		{
			case 1:  //---Consultar la informacion ya registrada. 
			{
				consulta = "  SELECT codigo as codigo, 																									" +
						   "		 to_char(fecha_inicial, 'DD/MM/YYYY') as fi, to_char(fecha_final, 'DD/MM/YYYY') as ff, valor as valor,		" +
						   " 		 to_char(fecha_inicial, 'DD/MM/YYYY') as h_fi, to_char(fecha_final, 'DD/MM/YYYY') as h_ff, valor as h_valor " +	
						   "		 FROM unidad_pago						 																	" +
						   "			  ORDER BY fecha_inicial			 																	";
			}
			break;
			default :
			{
				return new HashMap();
			}			
		}
		PreparedStatementDecorator pst= null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error en consultarInformacion de SqlBaseUnidadPagoDao: "+e); 
			return null;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}

	}


	/**
	 *  Metodo para insertar / modificar 
	 * @param con
	 * @param tipoAccion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param valor
	 * @return
	 */
	public static int insertar(Connection con, int tipoAccion, int codigo, String fechaInicial, String fechaFinal, String valor)
	{
		StringBuffer consulta = new StringBuffer(); 
		PreparedStatementDecorator stm= null;
		try
		{
			if ( tipoAccion == 0 ) //----- Se Insertara.   
			{
					consulta.append(" INSERT INTO capitacion.unidad_pago (codigo, fecha_inicial, fecha_final, valor) VALUES ("+ Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con,"capitacion.seq_unidad_pago")+"") +",?,?,?) ");
					 stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString()));
					
					stm.setDate(1, Date.valueOf(fechaInicial));
					stm.setDate(2, Date.valueOf(fechaFinal));
					stm.setDouble(3, Utilidades.convertirADouble(valor));
					return stm.executeUpdate();
			}

			if ( tipoAccion == 1 ) //------ Se Actualiza  
			{
					consulta.append(" UPDATE capitacion.unidad_pago SET fecha_inicial = ?, fecha_final = ?, valor = ? WHERE codigo = ? ");
					 stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString()));
					stm.setDate(1, Date.valueOf(fechaInicial));
					stm.setDate(2, Date.valueOf(fechaFinal));
					stm.setDouble(3, Utilidades.convertirADouble(valor));
					stm.setDouble(4, Utilidades.convertirADouble(codigo+""));
					return stm.executeUpdate();
			}

			if ( tipoAccion == 2 ) //------ Se Eliminara  
			{
					consulta.append(" DELETE FROM capitacion.unidad_pago WHERE codigo = ? ");
					 stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString()));
					stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
					return stm.executeUpdate();
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error EN (insertar) EN SqlBaseUnidadPagoDao  consutal [" + consulta.toString() + "] ERROR ["+ e +"] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}	
		return 0;
	}
}