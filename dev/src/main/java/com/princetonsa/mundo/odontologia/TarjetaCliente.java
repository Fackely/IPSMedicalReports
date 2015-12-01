package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.facturacion.InfoTarifaTarjetaCliente;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.fwk.exception.IPSException;

public class TarjetaCliente 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(TarjetaCliente.class);

	/**
	 * 
	 * @param objetoTarjetaCliente
	 * @return Double
	 */
	public static double guardar(DtoTarjetaCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().guardar(dto);
	}

	/**
	 * 
	 * @param objetoTarjetaCliente
	 * @return Arraylist de DtoTarjetaCliente
	 */
	public static ArrayList<DtoTarjetaCliente> cargar(DtoTarjetaCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoTarjetaCliente>  cargarConvenio(DtoTarjetaCliente dto,boolean  modificar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().cargarConvenio(dto, modificar);
	}
	/**
	 * 
	 * @param objetoTarjetaCliente
	 * @return
	 */
	public static boolean modificar(DtoTarjetaCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param objetoTarjetaCliente
	 * @return
	 */
	public static boolean  eliminar(DtoTarjetaCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().eliminar(dto);
	}
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @param tipoVenta
	 * @return
	 */
	public static InfoTarifaTarjetaCliente obtenerTarifaBaseServicioTarjetaCliente(double codigoPkTarjetaCliente, String tipoVenta) throws IPSException
	{
		logger.info("\n\n**********************************OBTENER TARIFA BASE SERVICIO TARJETA CLIENTE******************************************");
		logger.info("****************************************************************************************************************************");
		InfoTarifaTarjetaCliente infoTarjetaTarifa= new InfoTarifaTarjetaCliente();
		DtoTarjetaCliente dto= new DtoTarjetaCliente();
		dto.setCodigoPk(codigoPkTarjetaCliente);
		ArrayList<DtoTarjetaCliente> arrayDto= cargar(dto);
		logger.info("codigoPkTarjeta-->"+codigoPkTarjetaCliente+" tipoVenta-->"+tipoVenta);
		//si no existe entonces retornamos que no existe
		if(arrayDto.size()!=1)
		{	
			logger.info("nunca debe entrar aca, si entra es error x codigo retorna null");
			return null;
		}	
		else
		{	
			logger.info("encontro la tarjeta cliente.......");
			dto= arrayDto.get(0);
			int codigoServicio= ConstantesBD.codigoNuncaValido;
			
			if(tipoVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
			{	
				codigoServicio= dto.getServicioEmpresarial().getCodigo();
			}
			else if(tipoVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
			{	
				codigoServicio= dto.getServicioFamiliar().getCodigo();
			}
			else if(tipoVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
			{	
				codigoServicio= dto.getServicioPersonal().getCodigo();
			}
			
			logger.info("SERVICIO encontrado?--->"+codigoServicio);
			
			
			infoTarjetaTarifa.setCodigoServicio(codigoServicio);
			//no existe servicio
			if(codigoServicio<=0)
			{
				logger.info("No existe servicio!!!!!!!!!!!!!!!!!!!!!");
				//retorna el objeto con el servicio vacio
				return infoTarjetaTarifa;
			}
			
			//de lo contrario debemos cargar la tarifa
			Connection con= UtilidadBD.abrirConexion();
			infoTarjetaTarifa.setEsquemaTarifario(obtenerEsquemaTarifarioTarjetaCliente(codigoPkTarjetaCliente));
			infoTarjetaTarifa.setCodigoContrato(obtenerContratoTarjetaCliente(codigoPkTarjetaCliente));
			logger.info("*****************************************************************");
			logger.info("*****************************************************************");
			logger.info("*****************************************************************");
			logger.info("*****************************************************************");
			logger.info("*****************************************************************");
			logger.info(" CODIGO CONTRATO--------> "+infoTarjetaTarifa.getCodigoContrato());
			logger.info("codigoEsquemaTarifario encontrado?---->"+infoTarjetaTarifa.getEsquemaTarifario());
			if(infoTarjetaTarifa.getEsquemaTarifario()>0)
			{	
				InfoTarifaVigente infoTarifa=  Cargos.obtenerTarifaBaseServicio(con, EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, infoTarjetaTarifa.getEsquemaTarifario()), codigoServicio, infoTarjetaTarifa.getEsquemaTarifario(), UtilidadFecha.getFechaActual());
				infoTarjetaTarifa.setCodigo(infoTarifa.getCodigo());
				infoTarjetaTarifa.setCodigoServicio(codigoServicio);
				infoTarjetaTarifa.setExiste(infoTarifa.isExiste());
				infoTarjetaTarifa.setFechaVigencia(infoTarifa.getFechaVigencia());
				infoTarjetaTarifa.setLiquidarAsocios(infoTarifa.isLiquidarAsocios());
				infoTarjetaTarifa.setTipoLiquidacion(infoTarifa.getTipoLiquidacion());
				infoTarjetaTarifa.setValorTarifa(infoTarifa.getValorTarifa());
				
				logger.info(infoTarjetaTarifa.getValorTarifa());
				
				UtilidadBD.closeConnection(con);
			}
			logger.info(UtilidadLog.obtenerString(infoTarjetaTarifa, true));
			logger.info("**************************************************************************************************************************************");
			return infoTarjetaTarifa;
		}	
	}
	
	/**
	 * 
	 * @param contrato
	 * @return
	 */
	public static int obtenerEsquemaTarifarioTarjetaCliente(double contrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().obtenerEsquemaTarifarioTarjetaCliente(contrato); 
	}
	
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @return
	 */
	public static int  obtenerContratoTarjetaCliente(double codigoPkTarjetaCliente) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().obtenerContratoTarjetaCliente(codigoPkTarjetaCliente);
	}

	public static ArrayList<DtoResultadoReporteVentaTarjetas> consultarDatosReporte(
			DtoFiltroReporteIngresosTarjetasCliente dtoFiltro) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().consultarDatosReporte(dtoFiltro);
	}
	
}