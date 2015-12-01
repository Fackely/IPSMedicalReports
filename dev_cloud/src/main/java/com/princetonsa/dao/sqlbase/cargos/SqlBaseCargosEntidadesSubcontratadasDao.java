/*
 * Junio 24, 2009
 */
package com.princetonsa.dao.sqlbase.cargos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ElementoApResource;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cargos.DtoCargoEntidadSub;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;


/**
 * Clase de atributos y metodos estáticos que menja los procesos SQL genéricos del dao de Cargos Entidades subcotnratadas
 * @author axioma
 *
 */
public class SqlBaseCargosEntidadesSubcontratadasDao 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCargosEntidadesSubcontratadasDao.class);
	
	/**
	 * Cadena que carga las entidades subcontratadas asociadas al centro de costo
	 */
	private static final String obtenerEntidadesSubcontratadasCentroCostoStr = "SELECT " +
		"es.codigo_pk as consecutivo," +
		"es.codigo," +
		"es.razon_social," +
		"es.direccion, " +
		"es.telefono, " +
		"cces.resp_otros_usuarios, " +
		"cces.nro_prioridad AS prioridad " +
		"from centros_costo_entidades_sub cces " +
    	"inner join entidades_subcontratadas es ON(es.codigo_pk = cces.entidad_subcontratada and es.activo = '"+ConstantesBD.acronimoSi+"') " +
		"WHERE cces.centro_costo = ? and cces.institucion = ? " +
		"order by cces.nro_prioridad";
	
	/**
	 * Cadena para obtener el contrato vigente de la entidad subcontratada
	 */
	private static final String obtenerContratoVigenteEntidadSubcontratadaStr = "SELECT " +
		"ces.consecutivo," +
		"ces.numero_contrato," +
		"ces.valor_contrato," +
		"to_char(ces.fecha_inicial,'"+ConstantesBD.formatoFechaAp+"') as fecha_inicial," +
		"to_char(ces.fecha_final,'"+ConstantesBD.formatoFechaAp+"') as fecha_final," +
		"coalesce(to_char(ces.fecha_firma_contrato,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_firma_contrato," +
		"coalesce(ces.observaciones,'') as observaciones, " +
		"ces.tipo_tarifa as tipo_tarifa " +
		"FROM contratos_entidades_sub ces " +
		"WHERE " +
		"ces.entidad_subcontratada = ? and " +
		"ces.fecha_inicial <= ? and " +
		"ces.fecha_final >= ?";
	
	/**
	 * Cadena usada para insertar un cargo de la entidad subcontratada
	 */
	private static final String insertarCargoEntidadSubcontratadaStr = "INSERT INTO tarifas_entidad_sub (" +
		"codigo_detalle_cargo," + //1
		"entidad_subcontratada," + //2
		"contrato," + //3
		"fecha," + //4
		"hora," + //5
		"solicitud," + //6
		"articulo," + //7
		"pedido," + //8
		"valor_unitario," + //9
		"art_principal," + //10
		"viene_despacho," + //11
		"estado," + //12
		"esquema_tarifario," + //13
		"autorizacion, " + //14
		"fecha_modifica," + 
		"hora_modifica," + 
		"usuario_modifica," +//15
		"observaciones) " ;//16 
	
	/**
	 * Cadena que elimina los errores de un cargto de entidad subcontratada
	 */
	private static final String eliminarErroresCargoEntidadSubcontratadaStr = "DELETE FROM errores_tarifas_ent_sub where tarifa_entidad_sub = ?";
	
	/**
	 * Cadena que inserta un error de un cargo entidad subcotnratada
	 */
	private static final String insertarErrorCargoEntidadSubcontratadaStr = "INSERT INTO errores_tarifas_ent_sub (consecutivo,tarifa_entidad_sub,error) values (?,?,?)";
	
	private static final String consultaCentroCostoSolicitanteIgualACubiertoStr = " SELECT " +
			"CASE " +
			"WHEN COUNT(1) > 0 " +
			"THEN '"+ConstantesBD.acronimoSi+"' " +
			"ELSE '"+ConstantesBD.acronimoNo+"' " +
			"END AS existe " +
			"FROM ordenes.solicitudes s " +
			"INNER JOIN administracion.centros_costo cc " +
			"ON (s.centro_costo_solicitado = cc.codigo) " +
			"INNER JOIN facturacion.centros_costo_entidades_sub cces  ON (cces.centro_costo  = cc.codigo) "+ 
			"WHERE s.numero_solicitud    = ? " +
			"AND cces.entidad_subcontratada = ? ";
	
		
	
	
	
	/**
	 * Método para obtener las entidades subcontratadas asociadas al centro de costo
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(Connection con,int codigoCentroCosto,int codigoInstitucion)
	{
		ArrayList<DtoEntidadSubcontratada> resultados = new ArrayList<DtoEntidadSubcontratada>();
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerEntidadesSubcontratadasCentroCostoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroCosto);
			pst.setInt(2,codigoInstitucion);
			
			logger.info("SQL / obtenerEntidadesSubcontratadasCentroCosto "+obtenerEntidadesSubcontratadasCentroCostoStr);
			logger.info("codigoCentroCosto "+codigoCentroCosto);
			logger.info("codigoInstitucion "+codigoInstitucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoEntidadSubcontratada entidad = new DtoEntidadSubcontratada();
				entidad.setConsecutivo(rs.getString("consecutivo"));
				entidad.setCodigo(rs.getString("codigo"));
				entidad.setRazonSocial(rs.getString("razon_social"));
				entidad.setDireccion(rs.getString("direccion"));
				entidad.setTelefono(rs.getString("telefono"));
				entidad.setPrioridad(rs.getString("prioridad"));
				entidad.setRespOtrosUsua(UtilidadTexto.getBoolean(rs.getString("resp_otros_usuarios")));		
				
				resultados.add(entidad);
			}
			
			//logger.info("valor de la cadena >> "+obtenerEntidadesSubcontratadasCentroCostoStr+" "+codigoCentroCosto+" "+codigoInstitucion );
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEntidadesSubcontratadasCentroCosto: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener el contrato vigente 
	 * @param con
	 * @param codigoEntidadSubcontratada
	 * @param fechaReferencia
	 * @return
	 */
	public static DtoContratoEntidadSub obtenerContratoVigenteEntidadSubcontratada(Connection con,String codigoEntidadSubcontratada,String fechaReferencia)
	{
		DtoContratoEntidadSub contrato = new DtoContratoEntidadSub();
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerContratoVigenteEntidadSubcontratadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1, Long.parseLong(codigoEntidadSubcontratada));
			pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaReferencia)));
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaReferencia)));
			logger.info("SQL/ obtenerContratoVigenteEntidadSubcontratada-->"+obtenerContratoVigenteEntidadSubcontratadaStr);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				contrato.setConsecutivo(rs.getString("consecutivo"));
				contrato.setNumeroContrato(rs.getString("numero_contrato"));
				contrato.setValorContrato(rs.getString("valor_contrato"));
				contrato.setFechaInicial(rs.getString("fecha_inicial"));
				contrato.setFechaFinal(rs.getString("fecha_final"));
				contrato.setFechaFirmaContrato(rs.getString("fecha_firma_contrato"));
				contrato.setObservaciones(rs.getString("observaciones"));
				contrato.setTipoTarifa(rs.getString("tipo_tarifa"));
				
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerContratoVigenteEntidadSubcontratada: "+e);
		}
		return contrato;
	}
	
	/**
	 * Método para insertar el cargo de la entidad subcontratada
	 * @param con
	 * @param cargo
	 * @return
	 */
	public static ResultadoBoolean insertarCargoEntidadSubcontratada(Connection con, DtoCargoEntidadSub cargo)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta = insertarCargoEntidadSubcontratadaStr;
			consulta += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_tarifas_entidad_sub");
			cargo.setCodigoDetalleCargo(secuencia+"");
			pst.setLong(1,Long.parseLong(secuencia+""));
			if(!cargo.getEntidad().getConsecutivo().equals(""))
			{
				pst.setLong(2,Long.parseLong(cargo.getEntidad().getConsecutivo()));
			}
			else
			{
				pst.setNull(2,Types.NUMERIC);
			}
			if(!cargo.getContrato().getConsecutivo().equals(""))
			{
				pst.setLong(3,Long.parseLong(cargo.getContrato().getConsecutivo()));
			}
			else
			{
				pst.setNull(3,Types.NUMERIC);
			}
			pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargo.getFecha())));
			if(!cargo.getHora().equals(""))
			{
				pst.setString(5,cargo.getHora());
			}
			else
			{
				pst.setString(5,UtilidadFecha.getHoraActual());
			}
			if(!cargo.getNumeroSolicitud().equals(""))
			{
				pst.setLong(6,Long.parseLong(cargo.getNumeroSolicitud()));
			}
			else
			{
				pst.setNull(6,Types.INTEGER);
			}
			if(cargo.getCodigoArticulo()>0)
			{
				pst.setInt(7,cargo.getCodigoArticulo());
			}
			else
			{
				pst.setNull(7,Types.INTEGER);
			}
			if(!UtilidadTexto.isEmpty(cargo.getNumeroPedido()))
			{
				pst.setLong(8,Long.parseLong(cargo.getNumeroPedido()));
			}
			else
			{
				pst.setNull(8,Types.INTEGER);
			}
			if(!cargo.getValorUnitario().equals(""))
			{
				pst.setDouble(9,Utilidades.convertirADouble(cargo.getValorUnitario()));
			}
			else
			{
				pst.setNull(9,Types.NUMERIC);
			}
			if(cargo.getCodigoArticuloPrincipal()>0)
			{
				pst.setInt(10, cargo.getCodigoArticuloPrincipal());
			}
			else
			{
				pst.setNull(10,Types.INTEGER);
			}
			pst.setString(11,cargo.isVieneDespacho()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			pst.setInt(12, cargo.getEstado().getCodigo());
			if(cargo.getEsquemaTarifario().getCodigo()>0)
			{
				pst.setInt(13,cargo.getEsquemaTarifario().getCodigo());
			}
			else
			{
				pst.setNull(13,Types.INTEGER);
			}
			if(!cargo.getConsecutivoAutorizacion().equals(""))
			{
				pst.setLong(14,Long.parseLong(cargo.getConsecutivoAutorizacion()));
			}
			else
			{
				pst.setNull(14,Types.NUMERIC);
			}
			pst.setString(15,cargo.getUsuarioModifica().getLoginUsuario());
			
			pst.setString(16,cargo.getObservaciones());
			
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("No se pudo registrar el cargo a la entidad subcontratada");
			}
			
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarCargoEntidadSubcontratada: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error al tratar de ingresar la tarifa entidad subcontratada:"+e);
		}
		return resultado;
	}
	
	/**
	 * Método para obtener el codigo del cargo de la entidad subcontratada
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String obtenerCodigoCargoEntidadSubcontratada(Connection con, HashMap campos)
	{
		String codigoCargo = "";
		try
		{
			//******************OBTENER PARÁMETROS*******************************+
			String numeroSolicitud = campos.get("numeroSolicitud")==null?"":campos.get("numeroSolicitud").toString();
			String numeroPedido = campos.get("numeroPedido")==null?"":campos.get("numeroPedido").toString();
			int codigoArticulo = Utilidades.convertirAEntero(campos.get("codigoArticulo")+"");
			int codigoArticuloPrincipal = Utilidades.convertirAEntero(campos.get("codigoArticuloPrincipal")+"");
			String codigoAutorizacion = campos.get("codigoAutorizacion")==null?"":campos.get("codigoAutorizacion").toString();
			//********************************************************************
			
			String consulta = "SELECT codigo_detalle_cargo as codigo from tarifas_entidad_sub  ";
			String where = "";
			if(!numeroSolicitud.equals(""))
			{
				where += (where.equals("")?"":" AND ") + " solicitud = "+numeroSolicitud;
			}
			if(!numeroPedido.equals(""))
			{
				where += (where.equals("")?"":" AND ") + " pedido = "+numeroPedido;
			}
			if(codigoArticulo>0)
			{
				where += (where.equals("")?"":" AND ") + " articulo = "+codigoArticulo;
			}
			if(codigoArticuloPrincipal>0)
			{
				where += (where.equals("")?"":" AND ") + " art_principal = "+codigoArticuloPrincipal;
			}
			if(!codigoAutorizacion.equals(""))
			{
				where += (where.equals("")?"":" AND ") + " autorizacion = "+codigoAutorizacion;
			}
			
			consulta = consulta + " WHERE " + where;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				codigoCargo = rs.getString("codigo");
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoCargoEntidadSubcontratada: "+e);
			codigoCargo = "";
		}
		
		return codigoCargo;
	}
	
	/**
	 * Método implementado para modificar un cargo
	 * @param con
	 * @param cargo
	 * @return
	 */
	public static ResultadoBoolean modificarCargoEntidadSubcontratada(Connection con,DtoCargoEntidadSub cargo)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta = "UPDATE tarifas_entidad_sub SET " +
				"entidad_subcontratada = ?, " + 		//1
				"contrato = ?, " + 						//2
				"fecha = ?, " + 						//3
				"hora = ?, " + 							//4
				"solicitud = ?, " + 					//5
				"articulo = ?, " + 						//6
				"pedido = ?, " + 						//7
				"valor_unitario = ?, " + 				//8
				"art_principal = ?, " + 				//9
				"viene_despacho = ?, " + 				//10
				"estado = ?, " + 						//11
				"esquema_tarifario = ?, " + 			//12
				"autorizacion = ?, " + 					//13
				"fecha_modifica = current_date, " + 
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"usuario_modifica = ?," +				//14
				"observaciones=? " + 					//15
				"WHERE codigo_detalle_cargo = ?"; 		//16
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			if(!cargo.getEntidad().getConsecutivo().equals(""))
			{
				pst.setLong(1,Long.parseLong(cargo.getEntidad().getConsecutivo()));
			}
			else
			{
				pst.setNull(1,Types.NUMERIC);
			}
			if(!cargo.getContrato().getConsecutivo().equals(""))
			{
				pst.setLong(2,Long.parseLong(cargo.getContrato().getConsecutivo()));
			}
			else
			{
				pst.setNull(2,Types.NUMERIC);
			}
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargo.getFecha())));
			if(!cargo.getHora().equals(""))
			{
				pst.setString(4,cargo.getHora());
			}
			else
			{
				pst.setString(4,UtilidadFecha.getHoraActual(con));
			}
			if(!cargo.getNumeroSolicitud().equals(""))
			{
				pst.setLong(5,Long.parseLong(cargo.getNumeroSolicitud()));
			}
			else
			{
				pst.setNull(5, Types.INTEGER);
			}
			if(cargo.getCodigoArticulo()>0)
			{
				pst.setInt(6,cargo.getCodigoArticulo());
			}
			else
			{
				pst.setNull(6,Types.INTEGER);
			}
			if(!UtilidadTexto.isEmpty(cargo.getNumeroPedido()))
			{
				pst.setLong(7,Long.parseLong(cargo.getNumeroPedido()));
			}
			else
			{
				pst.setNull(7,Types.INTEGER);
				
			}
			if(!cargo.getValorUnitario().equals(""))
			{
				pst.setDouble(8,Utilidades.convertirADouble(cargo.getValorUnitario()));
			}
			else
			{
				pst.setNull(8,Types.NUMERIC);
			}
			if(cargo.getCodigoArticuloPrincipal()>0)
			{
				pst.setLong(9,cargo.getCodigoArticuloPrincipal());
			}
			else
			{
				pst.setNull(9,Types.INTEGER);
			}
			pst.setString(10, cargo.isVieneDespacho()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			pst.setInt(11,cargo.getEstado().getCodigo());
			if(cargo.getEsquemaTarifario().getCodigo()>0)
			{
				pst.setInt(12,cargo.getEsquemaTarifario().getCodigo());
			}
			else
			{
				pst.setNull(12, Types.INTEGER);
			}
			if(!cargo.getConsecutivoAutorizacion().equals(""))
			{
				pst.setLong(13,Long.parseLong(cargo.getConsecutivoAutorizacion()));
			}
			else
			{
				pst.setNull(13,Types.NUMERIC);
			}
			pst.setString(14, cargo.getUsuarioModifica().getLoginUsuario());
			
			pst.setString(15,cargo.getObservaciones());
			
			pst.setLong(16,Long.parseLong(cargo.getCodigoDetalleCargo()));
			
			
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("No se pudo actualizar el cargo a la entidad subcontratada");
			}
			
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarCargoEntidadSubcontratada: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error al modificar el cargo entidad subcontratada: "+e);
		}
		
		return resultado;
	}
	
	
	/**
	 * Método para registrar los errores del cargo de la entidad subcotnratada
	 * @param con
	 * @param codigoDetalleCargo
	 * @param erroresCargo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ResultadoBoolean registrarErroresCargoEntidadSub(Connection con,String codigoDetalleCargo,ArrayList<ElementoApResource> erroresCargo)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			//*********PRIMERO SE ELIMINAN LOS ERRORES QUE ANTES EXISTÍAN*******************************+
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(eliminarErroresCargoEntidadSubcontratadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(codigoDetalleCargo));
			pst.executeUpdate();
			pst.close();
			//*********************************************************************************************
			
			//**************INSERCIÓN DE CADA ERROR DE CARGO****************************************************+++
			for(ElementoApResource elemento:erroresCargo)
			{
				String error = elemento.getLlave();
				Iterator iterador = elemento.getAtributosIterator();
				while(iterador.hasNext())
				{
					error += ConstantesBD.separadorSplit + iterador.next();
				}
				
				pst = new PreparedStatementDecorator(con.prepareStatement(insertarErrorCargoEntidadSubcontratadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_errores_tarifas_ent_sub")+""));
				pst.setLong(2,Long.parseLong(codigoDetalleCargo));
				pst.setString(3,error);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("No se pudo insertar el mensaje de error del cargo entidad subcontratada");
				}
				pst.close();
			}
			
			//****************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error en registrarErroresCargoEntidadSub: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error al registrar el error del cargo de la entidad subcontratada: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * Método que verifica si el centro de costo solicitante esta relacionado con el centro de atencion cubierto de la unidad subcontratada.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static String consultaCentroCostoSolicitanteIgualACubierto(Connection connection, HashMap<String, Object> parametros){
		String resultado = ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, consultaCentroCostoSolicitanteIgualACubiertoStr);
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("solicitud")+""));
			//ps.setInt(2, Utilidades.convertirAEntero(parametros.get("centro_costo_solicitado")+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("ent_subcontratada")+""));
			logger.info("consultaCentroCostoSolicitanteIgualACubiertoStr---->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				resultado = rs.getString("existe");
			}
		} catch (Exception e) {
		}
		return resultado;
	}
}
