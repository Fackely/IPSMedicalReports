package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;

public class SqlBaseEmisionTarjetaClienteDao {
	
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEmisionTarjetaClienteDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 

	 
	private static String inserccionStr = "INSERT INTO odontologia.enca_emi_tarjeta_cliente (   " +
																					"codigo , " +//1
																					"tipo_tarjeta , " +//2
																					"serial_inicial , " +//3
																					"serial_final , " +//4
																					"fecha_modifica , " +//5
																					"hora_modifica , " +//6
																					"institucion ,"+ //7
																					"usuario_modifica," + //8
																					"cantidad ) " +//9
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? , " +//6
																					"? , " +//7
																					
																					"?, " +//8
																					"?) ";//9


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoEmisionTarjetaCliente dtoNuevo, DtoEmisionTarjetaCliente dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.enca_emi_tarjeta_cliente  set codigo=codigo";
		
		consultaStr+= (Double.parseDouble(dtoNuevo.getTipoTarjeta().getCodigo()) > ConstantesBD.codigoNuncaValido)?" , tipo_tarjeta= "+dtoNuevo.getTipoTarjeta().getCodigo():"";
		consultaStr+= (dtoNuevo.getSerialInicial().doubleValue()> ConstantesBD.codigoNuncaValido)?" , serial_inicial= "+dtoNuevo.getSerialInicial():"";
		consultaStr+= (dtoNuevo.getSerialFinal().doubleValue()> ConstantesBD.codigoNuncaValido)?" , serial_final= "+dtoNuevo.getSerialFinal():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		consultaStr+= (dtoNuevo.getCantidad()>ConstantesBD.codigoNuncaValido)?" , cantidad = '"+dtoNuevo.getCantidad()+"' ":"";
		
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		if(! UtilidadTexto.isEmpty(dtoWhere.getTipoTarjeta().getCodigo())){
		consultaStr+= (Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo())> 0)?" and tipo_tarjeta= "+Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo()):"";
			
		}
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":" ";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		
		consultaStr+= (dtoWhere.getCantidad()>ConstantesBD.codigoNuncaValido) ?" and cantidad= "+dtoWhere.getCantidad()+" ":"";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Emision Tarjeta Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoEmisionTarjetaCliente> cargar(DtoEmisionTarjetaCliente dtoWhere) 
	{
		ArrayList<DtoEmisionTarjetaCliente> arrayDto = new ArrayList<DtoEmisionTarjetaCliente>();
		String consultaStr = 	"SELECT " +
										"emit.codigo as codigo , " +
										"emit.tipo_tarjeta as tipo_tarjeta , "+
										"emit.serial_inicial as serial_inicial , "+
										"emit.serial_final as serial_final , "+
										
										"(select tar.nombre from odontologia.tipos_tarj_cliente tar where tar.codigo_pk=emit.tipo_tarjeta) as nombre_tipo_tarjeta ,"+ 
										"getnombreconvenio((select tar2.convenio from odontologia.tipos_tarj_cliente tar2 where tar2.codigo_pk=emit.tipo_tarjeta)) as nombre_convenio ,"+ 
										
										"emit.fecha_modifica as fecha_modifica , " +
										"emit.hora_modifica as hora_modifica , " +
										"emit.institucion as institucion , " +
										"emit.usuario_modifica as usuario_modifica, " +
										"coalesce (emit.cantidad,"+ConstantesBD.codigoNuncaValido+") as cantidad " +
									"from " +
										"odontologia.enca_emi_tarjeta_cliente  emit  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and emit.codigo= "+dtoWhere.getCodigo():"";
		if(!UtilidadTexto.isEmpty(dtoWhere.getTipoTarjeta().getCodigo())){
		consultaStr+= (Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo())>0)?" and emit.tipo_tarjeta= "+dtoWhere.getTipoTarjeta().getCodigo():"";
		}
		consultaStr+= (dtoWhere.getSerialInicial().doubleValue()>0)?" and emit.serial_inicial= "+dtoWhere.getSerialInicial():"";
		consultaStr+= (dtoWhere.getSerialFinal().doubleValue()>0)?" and emit.serial_final = "+dtoWhere.getSerialFinal():"";
		consultaStr+= (dtoWhere.getCantidad()>0)?" and emit.cantidad = "+dtoWhere.getCantidad():"";
		
		consultaStr+=" order by emit.serial_inicial asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoEmisionTarjetaCliente dto = new DtoEmisionTarjetaCliente();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setTipoTarjeta(new InfoDatosStr(String.valueOf(rs.getDouble("tipo_tarjeta")),rs.getString("nombre_tipo_tarjeta"),rs.getString("nombre_convenio")));
				dto.setSerialInicial(rs.getBigDecimal("serial_inicial"));
				dto.setSerialFinal(rs.getBigDecimal("serial_final"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setCantidad(rs.getInt("cantidad"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Emision Odon==> " + e);
		}
		return arrayDto;
	}

	
	
	
	/**
	 * Metodo que valida si una tarjeta esta registrada en emision de serial tarjeta cliente
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static boolean existeTarjetaRegistrada(DtoTarjetaCliente dto){
		
		
		boolean retorno=Boolean.FALSE;
	
		
		String consulta= "select count(0) as cantidad from odontologia.enca_emi_tarjeta_cliente where  tipo_tarjeta="+dto.getCodigoPk();
			
		
		Connection con = UtilidadBD.abrirConexion();
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consulta);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) 
			{
				retorno=rs.getInt("cantidad")>0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Emision Odon==> " + e);
		}
		finally{
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(null, null, con);
		}
		
		return retorno;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoEmisionTarjetaCliente dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.enca_emi_tarjeta_cliente  WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		
		logger.info("elmi-->"+consultaStr);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= null;
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  emi tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoEmisionTarjetaCliente dto) 
	{
		
		
		//logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_enca_emi_tarjeta_cli");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, Double.parseDouble(dto.getTipoTarjeta().getCodigo()));		    
		    ps.setBigDecimal(3, dto.getSerialInicial());
		    ps.setBigDecimal(4, dto.getSerialFinal());
		    		
			ps.setString(5, dto.getFechaModificaFromatoBD());
			ps.setString(6, dto.getHoraModifica());
			ps.setInt(7, dto.getInstitucion());
			
			ps.setString(8, dto.getUsuarioModifica());
			
			
			if(dto.getCantidad()>0)
		    {
		    	ps.setInt(9, dto.getCantidad());
		    }
		    else
		    {
		    	ps.setNull(9, Types.INTEGER);
		    }
			
			
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				
				if(actualizarSerialTipoTarjeta(dto.getSerialFinal().intValue(),Utilidades.convertirAEntero(dto.getTipoTarjeta().getCodigo())))
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
					return dto.getCodigo();
				}else
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
					return ConstantesBD.codigoNuncaValidoDouble;
					
				}
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert Emi tar " + e + dto.getCodigo());
			
			
		}
		return dto.getCodigo();
	}
	
	
	
	/**
	 * 
	 * @param codigo
	 * @param convenio
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public static boolean existeRangoSeriales(double codigo, String convenio, double ValorMinimo, double ValorMaximo){
		
		
		String consultaStr="SELECT enca.codigo FROM odontologia.enca_emi_tarjeta_cliente enca  INNER JOIN " +
				"odontologia.tipos_tarj_cliente tt on (enca.tipo_tarjeta=tt.codigo_pk)  " +
				"WHERE ( (  enca.serial_inicial  between "+ValorMinimo+"   and  "+ValorMaximo+" ) or " +
						"( enca.serial_final between  "+ValorMinimo+"   and  "+ValorMaximo+" ) )"; 

		
		if(codigo > ConstantesBD.codigoNuncaValido){
			 consultaStr+="and enca.codigo <> "+codigo;
		}
		
		
		
		try 
		{
			logger.info("Consulta existe Rango"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
			catch (SQLException e) 
			{
				logger.error("error consulta existen " + e);
				
			}
		
		
		
		return false;
		
		
		
		
	}

	/**
	 * 
	 * @param codigoPKTipoTarjeta
	 * @param centroAtencion
	 * @param rangoInicialSerial
	 * @param rangoFinalSerial
	 * @return
	 */
	public static boolean existeSerialEnEmisionTarjetaDetalle(double codigoPKTipoTarjeta, int centroAtencion,double rangoInicialSerial, double rangoFinalSerial, int codigoInstitucion) 
	{
		logger.info("*****************************************************************************************************************");
		logger.info("********************************EXISTE SERIAL EN EMISION TARJETA*******************************");
		logger.info("PRIMERO EVALUAMOS QUE EXISTA EN EL ENCABEZADO--->");
		double codigoEmisionTarjeta= existeSerialEnEmisionTarjetaEncabezado(codigoPKTipoTarjeta, rangoInicialSerial, rangoFinalSerial, codigoInstitucion);
		if(codigoEmisionTarjeta<=0)
		{
			logger.info("NO EXISTE EN EL ENCABEZADO X ENDE EN EL DETALLE TAMPOCO EXISTE!!!!!!!!!!!!!!");
			return false;
		}
		
		logger.info("TIENE ENCABEZADO, CONTINUO FLUJO, CODIGO-->"+codigoEmisionTarjeta);
		
		String consultaStr="SELECT " +
								"codigo " +
							"FROM " +
								"odontologia.det_emi_tarjeta_cliente " +
							"WHERE " +
								"enca_emi_tarjeta="+codigoEmisionTarjeta+" " +
								"AND centro_atencion="+centroAtencion+" " +
								"AND  " +
									"(" +
										"("+rangoInicialSerial+" between serial_inicial and serial_final) " +
										"and " +
										"("+rangoFinalSerial+" between serial_inicial and serial_final) " +
									") " +
							"	AND institucion ="+codigoInstitucion;
		
		try 
		{
			logger.info("existeSerialEnEmisionTarjetaDetalle---->"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("LISTO LO ENCONTRO!!!!! ESTA EN EL DETALLE CON COD-->"+rs.getDouble(1)+" RETORNA TRUE");
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error consulta existen " + e);
		}
		return false;
		
	}

	/**
	 * Metodo que verifica si un rango de seriales esta dentro de
	 * @param codigoPkTipoTarjeta
	 * @param rangoInicialSerial
	 * @param rangoFinalSerial
	 * @return
	 */
	private static double existeSerialEnEmisionTarjetaEncabezado(double codigoPkTipoTarjeta, double rangoInicialSerial, double rangoFinalSerial, int codigoInstitucion)
	{
		double codigoEmisionTarjeta=ConstantesBD.codigoNuncaValidoDouble;
		String consultaStr="SELECT " +
								"codigo " +
							"FROM " +
								"odontologia.enca_emi_tarjeta_cliente " +
							"where " +
								"tipo_tarjeta="+codigoPkTipoTarjeta+" " +
								"and " +
									"(" +
										"("+rangoInicialSerial+" between serial_inicial and serial_final) " +
										"and " +
										"("+rangoFinalSerial+" between serial_inicial and serial_final) " +
									")" +
							"	and institucion="+codigoInstitucion;
		
		try 
		{
			logger.info("existeSerialEnEmisionTarjetaEncabezado---->"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigoEmisionTarjeta= rs.getDouble(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error consulta existen " + e);
		}
		return codigoEmisionTarjeta;
	}
	
	
	
	/**
	 * Metodo para actualizar el serial del tipo de tarjeta cliente 
	 * @param numSerialFinal
	 * @param codigoPkTarjeta
	 * @return
	 */
	private static boolean actualizarSerialTipoTarjeta(int numSerialFinal, int codigoPkTarjeta)
	{
		boolean retorna=false;
		int numSerialFinalGuardar=numSerialFinal+1;
		String consultaStr = "UPDATE odontologia.tipos_tarj_cliente set consecutivo_serial= ? where codigo_pk = ? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
			ps.setInt(1, numSerialFinalGuardar);
			ps.setInt(2, codigoPkTarjeta);
			logger.info("actualizar consetuvivo Serial-->"+ps);
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar consetuvivo Serial Tarjeta Odontologia" + e);
			
		}
		
		return retorna;
	   	
	}
	
		
}