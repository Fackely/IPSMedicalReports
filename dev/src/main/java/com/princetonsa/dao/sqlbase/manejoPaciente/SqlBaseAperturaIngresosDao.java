package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;






/**
 * Clase Apertura ingresos
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseAperturaIngresosDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseAperturaIngresosDao.class);

	
	
	
	/*----------------------------------------------------------------------------------------
	 *                              ATRIBUTOS APERTURA INGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	
	
	
	
	//String que actualiza el estado del cierre.
	private static final String strActualizarEstadoCierre = "UPDATE cierre_ingresos SET  activo=?, motivo_apertura=?, fecha_apertura=current_date," +
																						" hora_apertura="+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_apertura=? WHERE codigo=?";
	
	/*----------------------------------------------------------------------------------------
	 *                              FIN ATRIBUTOS APERTURA INGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	/*----------------------------------------------------------------------------------------
	 								METODOS DE APERTURA INGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de actualizar el estado del cierre.
	 * @param connection
	 * @param datosCierre
	 * ----------------------------
	 * KEY'S DEL MAPA CIERRE
	 * ----------------------------
	 * -- activo
	 * -- codigo
	 * -- motivoApertura
	 * -- usuario
	 */
	public static boolean ActualizarEstadoCierre (Connection connection,HashMap datosCierre)
	{
		logger.info("\n entre a actualizar el estado del ingreso --> "+datosCierre);
		
		String cadena =strActualizarEstadoCierre;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cierre_ingresos SET  activo=?, motivo_apertura=?, fecha_apertura=current_date," +
						" hora_apertura="+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_apertura=? WHERE codigo=?";
			 */
			
			//estado activo
			ps.setString(1, datosCierre.get("activo")+"");
			//motivo de la apertura
			ps.setString(2, datosCierre.get("motivoApertura")+"");
			//usuario
			ps.setString(3, datosCierre.get("usuario")+"");
			//codigo del cierre
			ps.setDouble(4, Utilidades.convertirADouble(datosCierre.get("codigo")+""));
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
		 logger.info("\n problema actualizando el estado del cierre "+e);
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de hacer una apertera de un ingreso cerrado.
	 * @param connection
	 * @param datosIngreso
	 * ------------------------------
	 * KEY'S DEL MAPA DATOSINGRESO
	 * -------------------------------
	 * -- estado			requerido
	 * -- cierreManual
	 * -- ingreso			requerido
	 * -- aperturaAuto
	 * -- usuarioModifica
	 */
	public static boolean ActualizarEstadoIngreso (Connection connection,HashMap datosIngreso)
	{
		logger.info("\n entre a actualizar el estado del ingreso --> "+datosIngreso);
		
		String cadena = "UPDATE ingresos SET estado=? ";
		
		if(!UtilidadTexto.isEmpty(datosIngreso.get("cierreManual")+""))
			cadena+=" , cierre_manual= '"+datosIngreso.get("cierreManual")+"' ";
		
		if(!UtilidadTexto.isEmpty(datosIngreso.get("aperturaAuto")+""))
			cadena+=" , reapertura_automatica= '"+datosIngreso.get("aperturaAuto")+"' " ;
		
		if(!UtilidadTexto.isEmpty( datosIngreso.get("usuarioModifica")+""))
			cadena+=" , usuario_modifica= '"+datosIngreso.get("usuarioModifica")+"' , fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" ";
		
		cadena+= " WHERE id=? ";
		logger.info("\n ActualizarEstadoIngreso->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//estado del ingreso
			ps.setString(1, datosIngreso.get("estado")+"");
			ps.setInt(2, Utilidades.convertirAEntero(datosIngreso.get("ingreso")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
		 logger.info("\n problema actualizando el estado del ingreso "+e);
		}
		
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de listar los ingresos de un paciente
	 * @param connection
	 * @param codigoPaciente
	 * ------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- consecutivo0_
	 * -- fechaIngreso1_
	 * -- horaIngreso2_
	 * -- fechaCierre3_
	 * -- horaCierrre4_
	 * -- nombreUsuario5_
	 * -- codigoIngreso6_
	 * -- codigoPaciente7_
	 * -- centroAtencion8_
	 * -- codigoCentroAtencion9_
	 * -- cuenta10_
	 * -- viaIngreso12_
	 * -- nomViaIngreso13_
	 * @param strConsultaIngresosXPaciente
	 */
	public static HashMap cargarListadoIngresos (Connection connection,String codigoPaciente, String ingreso, String strConsultaIngresosXPaciente)
	{
		logger.info("\n entro a consultar cargarListadoIngresos --> "+codigoPaciente);
		String cadena = strConsultaIngresosXPaciente;
		
		if (!ingreso.equals("") && !ingreso.equals("null") && !ingreso.equals(ConstantesBD.codigoNuncaValido+""))
			cadena+=" AND ing.id="+ingreso;
		
		cadena+=" order by ci.fecha_cierre,hora_cierre ";
		HashMap resultado= new HashMap();
		try 
		{
			logger.info("\n consulta --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//cosigo del paciente
			ps.setInt(1, Utilidades.convertirAEntero(codigoPaciente));
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException  e)
		{
		 logger.info("\n problema consultando el listado de ingresos del paciente "+e);
		}
		
		return resultado;
	}
	
	
	/*----------------------------------------------------------------------------------------
	 *                              FIN METODOS DE APERTURA INGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	
	
	
}