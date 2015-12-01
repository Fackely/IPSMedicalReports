package com.mercury.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.mercury.dto.odontologia.DtoCartaDental;
import com.mercury.dto.odontologia.DtoDiagCartaDental;
import com.mercury.dto.odontologia.DtoDienteCartaDental;
import com.mercury.dto.odontologia.DtoTratamientoCartaDental;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseCartaDentalDao
{
	private static Logger logger = Logger.getLogger(SqlBaseCartaDentalDao.class); 
		
	/**
	 *Consulta el encabezado de la carta dental 
	 * */
	private static String strConsultarCartaDental = 
			"SELECT " +
			"cd.codigo_pk," +
			"cd.cod_tratamiento_odo," +
			"cd.cod_medico_registro," +
			"to_char(cd.fecha_registro,'dd/mm/yyyy') AS fecha_registro," +
			"cd.hora_registro," +			
			"cd.otros_hallazgos " +
			"FROM " +
			"carta_dental cd " +
			"WHERE 1=1 " ;
	
	/**
	 * Inserta informacion en la carta dental
	 * */
	private static String strInsertarCartaDental = 
			"INSERT INTO carta_dental(" +
			"codigo_pk," +
			"cod_tratamiento_odo," +
			"cod_medico_registro," +
			"fecha_registro," +
			"hora_registro," +
			"fecha_modifica," +
			"hora_modifica," +
			"usuario_modifica," +
			"otros_hallazgos " +
			") VALUES (?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Consulta la informacion de la carta dental
	 * */
	private static String strConsultarDiagnosticosCartaDental = 
			"SELECT " +
			"dcd.codigo_pk," +
			"dcd.codigopk_carta_dental," +
			"dcd.diente," +
			"dcd.permanente," +
			"dcd.est_sec_diente_inst," +						
			"es.nombre AS nombre_diag," +
			"coalesce(dcd.cod_superficie_dental,"+ConstantesBD.codigoNuncaValido+") AS cod_superficie_dental," +
			"coalesce(sd.nombre,'') AS nombre_superficie " +
			"FROM diag_carta_dental dcd " +			
			"INNER JOIN est_sec_diente_inst es ON (es.codigo = dcd.est_sec_diente_inst) " +
			"LEFT OUTER JOIN superficie_dental sd ON (sd.codigo = dcd.cod_superficie_dental) " +
			"WHERE codigopk_carta_dental = ? " +
			"ORDER BY diente ASC ";
	
	/**
	 * Elimina los diagnosticos de la carta dental
	 * */
	private static String strEliminarDiagnosticosCartaDental = "" +
			"DELETE FROM diag_carta_dental WHERE codigo_pk = ? ";
	
	/**
	 * Actualiza la informacion del diente en los diagnosticos de la carta dental
	 * */
	private static String strActualizarDiagnostivosCartaDental = "" +
			"UPDATE diag_carta_dental " +
			"SET diente = ?," +
			"permanente = ? " +
			"WHERE codigopk_carta_dental = ? AND diente = ? AND  permanente = ? ";
	
	/**
	 * actualiza la informacion de la superficie de un diagnostico
	 * */
	private static String strActualizarSuperficieDiagCartaDental = "" +
			"UPDATE diag_carta_dental " +
			"SET cod_superficie_dental = ? " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * Insertar la informacion de diagnosticos de la carta dental
	 * */
	private static String strInsertarDiagnosticosCartaDental = 
			"INSERT INTO diag_carta_dental " +
			"(" +
			"codigo_pk," +
			"codigopk_carta_dental," +
			"diente," +
			"permanente," +
			"est_sec_diente_inst," +
			"cod_superficie_dental " +
			") VALUES (?,?,?,?,?,?) ";
	
	/**
	 * Consulta la informacion de los tratamientos de la carta dental
	 * */
	private static String strConsultarTratamientoCartaDental = 
			"SELECT " +
			"tcd.codigo_pk," +
			"tcd.codigopk_carta_dental," +
			"tcd.diente," +
			"tcd.permanente," +
			"tcd.tipo_trat_odo_inst," +
			"tt.nombre AS nombre_superficie," +
			"tcd.activo " +
			"FROM " +
			"tratamiento_carta_dental tcd " +
			"INNER JOIN tipo_tratamiento_odo_inst tt ON (tt.codigo = tcd.tipo_trat_odo_inst) " +
			"WHERE tcd.codigopk_carta_dental = ? ";
	
	/**
	 * Insertar informacion de tratamiento de la carta dental
	 * */
	private static String strInsertarTratamientoCartaDental = 
			"INSERT INTO tratamiento_carta_dental (" +
			"codigo_pk," +
			"codigopk_carta_dental," +
			"diente," +
			"permanente," +
			"tipo_trat_odo_inst," +
			"activo," +
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica " +
			")" +
			"VALUES (?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Elimina la informacion de tratamientos
	 * */
	private static String strModActivoTratamientoCartaDental = "" +
			"UPDATE tipo_trat_odo_inst " +
			"SET activo = ?, usuario_finaliza = ?, fecha_finaliza = ?, hora_finaliza = ? " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * Actualiza la informacion del diente en los tratamientos de la carta dental
	 * */
	private static String strActualizarTratamientoCartaDental = "" +
			"UPDATE tipo_trat_odo_inst " +
			"SET diente = ?," +
			"permanente = ? " +
			"WHERE codigopk_carta_dental = ? AND diente = ? AND  permanente = ? ";
	
	
	/**
	 * Inserta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int insertarCartaDental(Connection con,HashMap parametros)
	{
		try
		{
			int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_carta_dental");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPk);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codTratamientoOdo").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("codMedicoRegistra").toString()));
			ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5,UtilidadFecha.getHoraActual());
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7,UtilidadFecha.getHoraActual());
			ps.setString(8,parametros.get("usuarioModifica").toString());
			ps.setString(9,parametros.get("otrosHallazgos").toString());
			
			if(ps.executeUpdate()>0)
				return codigoPk;			
		}
		catch (Exception e) {
			logger.info("error insertando la carta dental "+e+" "+parametros);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inserta la informacion del diagnostico de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public static int insertarDiagnosticoCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_diag_carta_dental");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDiagnosticosCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPk);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPkCartaDental").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("diente").toString()));
			ps.setString(4,parametros.get("permanente").toString());
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("estSecDienteInst").toString()));
			
			if(parametros.containsKey("codSuperficieDental") 
					&& Utilidades.convertirAEntero(parametros.get("codSuperficieDental").toString())>0)
				ps.setInt(6,Utilidades.convertirAEntero(parametros.get("codSuperficieDental").toString()));
			else
				ps.setNull(6,Types.INTEGER);
			
			if(ps.executeUpdate()>0)
				return codigoPk;						
		}
		catch (Exception e) {
			logger.info("error insertando diagnostico  "+e+" "+parametros);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inserta la informacion del tratamiento de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public static int insertarTratamientoCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tratamiento_carta_dental");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarTratamientoCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPk);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPkCartaDental").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("diente").toString()));
			ps.setString(4,parametros.get("permanente").toString());
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("tipoTratOdoInst").toString()));
			ps.setString(6,parametros.get("activo").toString());
			ps.setString(7,parametros.get("usuarioModifica").toString());
			ps.setDate(8,Date.valueOf(parametros.get("fechaModifica").toString()));
			ps.setString(9,parametros.get("horaModifica").toString());
			
			if(ps.executeUpdate()>0)
				return codigoPk;						
		}
		catch (Exception e) {
			logger.info("error insertando diagnostico  "+e+" "+parametros);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Consulta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static DtoCartaDental cargarCartaDental(Connection con, HashMap parametros)
	{
		DtoCartaDental dto = new DtoCartaDental();
		//Inicializa la estructura del diente, solo se inicializan los cuadrantes permanentes
		dto.inicializarDientesPermanentes();
		
		String cadena = strConsultarCartaDental;
		
		if(parametros.containsKey("codTratamientoOdo") 
				&& !parametros.get("codTratamientoOdo").toString().equals(""))
			cadena += " AND cd.cod_tratamiento_odo = "+parametros.get("codTratamientoOdo").toString();
				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo_pk"));
				dto.setCodPkTratamientoOdo(rs.getInt("cod_tratamiento_odo"));
				dto.setCodMedicoRegistra(rs.getInt("cod_medico_registro"));
				dto.setFechaRegistra(rs.getString("fecha_registro"));
				dto.setHoraRegistra(rs.getString("hora_registro"));
				dto.setOtrosHallazgos(rs.getString("otros_hallazgos"));
				
				//Diagnosticos
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarDiagnosticosCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,rs.getInt("codigo_pk"));
				ResultSetDecorator rsd =new ResultSetDecorator(ps.executeQuery());							
					
				int posDiente = ConstantesBD.codigoNuncaValido;
				while(rsd.next())
				{
					posDiente = ConstantesBD.codigoNuncaValido;
					
					//Si el diente es un diente temporal se debe actualizar el diente
					//como no permanente y modificar el campo numero de diente
					if(rsd.getString("permanente").equals(ConstantesBD.acronimoNo))
					{
						//envia el numero del diente permanente
						posDiente = dto.getPosDiente(rsd.getInt("diente"));						
						if(posDiente < 0)
						{
							posDiente = dto.getPosDiente(rsd.getInt("diente") - 40);

							if(posDiente > 0)
							{
								dto.getArrayDtoDienteCartaDental(posDiente).setNumeroDiente(rsd.getInt("diente")+40);
								dto.getArrayDtoDienteCartaDental(posDiente).setPermanente(UtilidadTexto.getBoolean(rsd.getString("permanente")));
								dto.getArrayDtoDienteCartaDental(posDiente).setNumeroDienteAnterior(rsd.getInt("diente")+40);
								dto.getArrayDtoDienteCartaDental(posDiente).setPermanenteAnterior(UtilidadTexto.getBoolean(rsd.getString("permanente")));
							}
						}
					}
					else
						posDiente = dto.getPosDiente(rsd.getInt("diente"));
					
					//Si no encuentra la posicion, indica que no se ha creado en el dto
					if(posDiente<0)
					{
						dto.getArrayDtoDienteCartaDental().add(new DtoDienteCartaDental(rsd.getInt("diente"),UtilidadTexto.getBoolean(rsd.getString("permanente"))));
						posDiente = dto.getArrayDtoDienteCartaDental().size()-1;
					}
					
					DtoDiagCartaDental dtoDiag = new DtoDiagCartaDental();
					dtoDiag.setCodigoPk(rsd.getInt("codigo_pk"));
					dtoDiag.setCodigoPkCartaDental(rsd.getInt("codigopk_carta_dental"));
					dtoDiag.setCodigoPkEstSecDienteInst(rsd.getInt("est_sec_diente_inst"));
					dtoDiag.setNombreEstSecDiente(rsd.getString("nombre_diag"));
					dtoDiag.setCodigoPkSuperficieDental(rsd.getInt("cod_superficie_dental"));
					dtoDiag.setCodigoPkSuperficieDentalAnterior(rsd.getInt("cod_superficie_dental"));
					dtoDiag.setNombreSuperficieDental(rsd.getString("nombre_superficie"));
					dtoDiag.setActivo(ConstantesBD.acronimoSi);
					
					dto.getArrayDtoDienteCartaDental(posDiente).getArrayDtoDiagCartaDental().add(dtoDiag);					
				}				
				
				//Tratamientos
				ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarTratamientoCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,rs.getInt("codigo_pk"));
				rsd =new ResultSetDecorator(ps.executeQuery());							
								
				while(rsd.next())
				{
					posDiente = ConstantesBD.codigoNuncaValido;
					
					//Si el diente es un diente temporal se debe actualizar el diente
					//como no permanente y modificar el campo numero de diente
					if(rsd.getString("permanente").equals(ConstantesBD.acronimoNo))
					{
						//envia el numero del diente permanente
						posDiente = dto.getPosDiente(rsd.getInt("diente"));						
						if(posDiente < 0)
						{		
							posDiente = dto.getPosDiente(rsd.getInt("diente") - 40);
							if(posDiente > 0)
							{
								dto.getArrayDtoDienteCartaDental(posDiente).setNumeroDiente(rsd.getInt("diente")+40);
								dto.getArrayDtoDienteCartaDental(posDiente).setPermanente(UtilidadTexto.getBoolean(rsd.getString("permanente")));
							}
						}
					}
					else					
						posDiente = dto.getPosDiente(rsd.getInt("diente"));					

					//Si no encuentra la posicion, indica que no se ha creado en el dto
					if(posDiente<0)
					{
						dto.getArrayDtoDienteCartaDental().add(new DtoDienteCartaDental(rsd.getInt("diente"),UtilidadTexto.getBoolean(rsd.getString("permanente"))));
						posDiente = dto.getArrayDtoDienteCartaDental().size()-1;
					}
					
					DtoTratamientoCartaDental dtoTrata = new DtoTratamientoCartaDental();
					dtoTrata.setCodigoPk(rsd.getInt("codigo_pk"));
					dtoTrata.setCodigoPkCartaDental(rsd.getInt("codigopk_carta_dental"));
					dtoTrata.setCodigoPkTipoTratOdoInst(rsd.getInt("tipo_trat_odo_inst"));
					dtoTrata.setNombreTratamiento(rsd.getString("nombre_superficie"));
					dtoTrata.setActivo(UtilidadTexto.getBoolean(rsd.getString("activo")));
					
					dto.getArrayDtoDienteCartaDental(posDiente).getArrayDtoTratamientoCartaDental().add(dtoTrata);					
				}
			}
		}
		catch (SQLException e) {
			//logger.info("Error al cargar la carta dental "+e+" "+parametros);
		}
		
		return dto;
	}
	
	/**
	 * Elimina la informarcion de diagnosticos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean eliminarDiagnosticoCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarDiagnosticosCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch (Exception e) {
			logger.info("error al tratar de eliminar un diagnostico "+e+" "+parametros);
		}
		
		return false;
	}
	
	/**
	 * Elimina la informarcion de tratamientos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean modificarActivoTratamientosCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strModActivoTratamientoCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("activo").toString());
			
			if(parametros.containsKey("usuarioFinaliza") && 
					!parametros.get("usuarioFinaliza").toString().equals(""))				
				ps.setString(1,parametros.get("usuarioFinaliza").toString());
			else
				ps.setNull(1,Types.VARCHAR);
			
			if(parametros.containsKey("fechaFinaliza") && 
					!parametros.get("fechaFinaliza").toString().equals(""))				
				ps.setDate(1,Date.valueOf(parametros.get("fechaFinaliza").toString()));
			else
				ps.setNull(1,Types.DATE);
			
			if(parametros.containsKey("horaFinaliza") && 
					!parametros.get("horaFinaliza").toString().equals(""))				
				ps.setString(1,parametros.get("horaFinaliza").toString());
			else
				ps.setNull(1,Types.VARCHAR);			
			
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch (Exception e) {
			logger.info("error al tratar de modificar el activo de un tratamiento "+e+" "+parametros);
		}
		
		return false;
	}
	
	/**
	 * Actualiza la informacion del diente en los tratamientos y los diagnosticos
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDienteDiagTrataCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarDiagnostivosCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("diente").toString()));
			ps.setString(2,parametros.get("permanente").toString());
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get("codigopkCartaDental").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("dienteAnterior").toString()));
			ps.setString(5, parametros.get("permanente").toString());			
			ps.executeUpdate();
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarTratamientoCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("diente").toString()));
			ps.setString(2,parametros.get("permanente").toString());
			ps.setInt(3, Utilidades.convertirAEntero(parametros.get("codigopkCartaDental").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(parametros.get("dienteAnterior").toString()));
			ps.setString(5, parametros.get("permanente").toString());			
			ps.executeUpdate();			
		}
		catch (Exception e) {
			logger.info("error en la actualizacion del diente en los diagnosticos y en los tratamientos "+e+" "+parametros);			
		}
		
		return true;
	}
	
	/**
	 * Actualiza la informacion de la superficie de un diagnostico de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarSuperficieDiagTrataCartaDental(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarSuperficieDiagCartaDental,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(parametros.containsKey("codSuperficieDental") 
					&& Utilidades.convertirAEntero(parametros.get("codSuperficieDental").toString())>0)
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codSuperficieDental").toString()));
			else
				ps.setNull(1,Types.INTEGER);			
			
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) {
			logger.info("error actualizando la información de la superficie de un diagnostico "+e+" "+parametros);
		}
		
		return false;
	}
}