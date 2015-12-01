package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoCamposSeccionesHistoricoHC;
import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoHistoricosHC;
import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoSeccionesHC;

public class SqlBaseHistoricoAtencionesDao 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseHistoricoAtencionesDao.class);

	/**
	 * 
	 */
	private static String consultaHistoricoAtenciones="select codigo_paciente as codpaciente,to_char(fecha_atencion,'dd/mm/yyyy') as fechaatencion, consecutivo_hc as consecutivohc, codigo_via_ingreso as viaingreso,manejopaciente.getnombreviaingreso(codigo_via_ingreso) as nomviaingreso from HISTORIACLINICA.HISTORICOS_HC where codigo_paciente=? group by codigo_paciente,consecutivo_hc,codigo_via_ingreso,fecha_atencion order by consecutivo_hc";

	/**
	 * 
	 */
	private static String consultaSeccionesHC="select DISTINCT " +
															" shc.codigo as codseccion," +
															" shc.nombre as nomseccion, " +
															" shc.activo as activoseccion, " +
															" shc.num_columnas as numcol " +
											" from historiaclinica.historicos_hc hhc " +
											" inner join historiaclinica.secciones_histo_hc shc on(hhc.codigo_seccion=shc.codigo) " +
											" where hhc.codigo_paciente=? and hhc.consecutivo_hc=? and hhc.codigo_via_ingreso=? ";

	/**
	 * 
	 */
	private static String consultaCamposSecHC="select " +
														" to_char(hhc.fecha_atencion,'dd/mm/yyyy') as fechaatencion," +
														" cshh.codigo as codigocampo," +
														" cshh.codigo_seccion as codigoseccion," +
														" cshh.colspan as colspan," +
														" cshh.nombre as nombre," +
														" cshh.activo as activo," +
														" hhc.valor as valor" +
											" from historiaclinica.historicos_hc hhc " +
											" inner join historiaclinica.secciones_histo_hc shc on(hhc.codigo_seccion=shc.codigo) " +
											" inner join historiaclinica.campos_secciones_histo_hc cshh on (cshh.codigo=hhc.codigo_campo and cshh.codigo_seccion=hhc.codigo_seccion) " +
											" where hhc.codigo_paciente=? and hhc.consecutivo_hc=? and hhc.codigo_via_ingreso=? and hhc.codigo_seccion=? " +
											" order by consecutivo ";
	/**
	 * 
	 * @param codigoPaciente
	 */
	public static ArrayList<DtoHistoricosHC> cargarHistoricoAtenciones(int codigoPaciente) 
	{
		ArrayList<DtoHistoricosHC> arrayDto=new ArrayList<DtoHistoricosHC>();
		ResultSetDecorator rsHistoricos=null;
		PreparedStatementDecorator psHistorico=null;
		ResultSetDecorator rsSecciones = null;
		PreparedStatementDecorator psSecciones = null;
		PreparedStatementDecorator psCampos = null;
		ResultSetDecorator rsCampos = null;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
		
			psHistorico= new PreparedStatementDecorator(con.prepareStatement(consultaHistoricoAtenciones));
			psHistorico.setInt(1, codigoPaciente);
			rsHistoricos=new ResultSetDecorator(psHistorico.executeQuery());
			while(rsHistoricos.next())
			{
				DtoHistoricosHC historico=new DtoHistoricosHC();
				historico.setCodigoPaciente(rsHistoricos.getInt("codpaciente"));
				historico.setConsecutivoHC(rsHistoricos.getInt("consecutivohc"));
				historico.setViaIngreso( new InfoDatosInt(rsHistoricos.getInt("viaingreso"),rsHistoricos.getString("nomviaingreso")));
				historico.setFechaAtencion(rsHistoricos.getString("fechaatencion"));


				//consultar las secciones
				psSecciones= new PreparedStatementDecorator(con.prepareStatement(consultaSeccionesHC));
				psSecciones.setInt(1, codigoPaciente);
				psSecciones.setInt(2, historico.getConsecutivoHC());
				psSecciones.setInt(3, historico.getViaIngreso().getCodigo());
				rsSecciones=new ResultSetDecorator(psSecciones.executeQuery());
				
				//cargar las secciones
				ArrayList<DtoSeccionesHC> secciones=new ArrayList<DtoSeccionesHC>();
				while(rsSecciones.next())
				{
					DtoSeccionesHC dtoSecc=new DtoSeccionesHC();
					dtoSecc.setCodigo(rsSecciones.getInt("codseccion"));
					dtoSecc.setNombre(rsSecciones.getString("nomseccion"));
					dtoSecc.setActivo(rsSecciones.getString("activoseccion"));
					dtoSecc.setNumeroColumnas(rsSecciones.getInt("numcol"));
					
					//consultar las secciones
					psCampos= new PreparedStatementDecorator(con,consultaCamposSecHC);
					psCampos.setInt(1, codigoPaciente);
					psCampos.setInt(2, historico.getConsecutivoHC());
					psCampos.setInt(3, historico.getViaIngreso().getCodigo());
					psCampos.setInt(4, dtoSecc.getCodigo());
					rsCampos=new ResultSetDecorator(psCampos.executeQuery());
					
					logger.info("---->"+psCampos);
					
					

					//cargar los campos de las secciones
					ArrayList<DtoCamposSeccionesHistoricoHC> campos=new ArrayList<DtoCamposSeccionesHistoricoHC>();
					while(rsCampos.next())
					{
						DtoCamposSeccionesHistoricoHC dtoCampos=new DtoCamposSeccionesHistoricoHC();
						dtoCampos.setActivo(rsCampos.getString("activo"));
						dtoCampos.setCodigo(rsCampos.getInt("codigocampo"));
						dtoCampos.setCodigoSeccion(rsCampos.getInt("codigoseccion"));
						dtoCampos.setFechaAtencion(rsCampos.getString("fechaatencion"));
						dtoCampos.setNombre(rsCampos.getString("nombre"));
						dtoCampos.setValor(rsCampos.getString("valor"));
						dtoCampos.setColspan(rsCampos.getInt("colspan"));
						campos.add(dtoCampos);
					}
					
					
					
					dtoSecc.setCamposSecciones(campos);
					secciones.add(dtoSecc);
				}
			
				
				historico.setSecciones(secciones);
				arrayDto.add(historico);
			}
			
			
		
		}
		catch(SQLException e)
		{
			logger.error("ERROR CARGANDO INFO",e);
		}finally{
			if (rsCampos != null){
				try{
					rsCampos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			if (psCampos != null){
				try{
					psCampos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			if (rsHistoricos != null){
				try{
					rsHistoricos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			if (psHistorico != null){
				try{
					psHistorico.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			if (rsSecciones != null){
				try{
					rsSecciones.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			if (psSecciones != null){
				try{
					psSecciones.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
				}
			}
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseHistoricoAtencionesDao " + sqlException.toString() );
			}
		}
		return arrayDto;
	}

}
