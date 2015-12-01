package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axis.components.jms.BeanVendorAdapter;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseAprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;

public class AprobacionAnulacionFacturasVarias 
{

	static Logger logger = Logger.getLogger(AprobacionAnulacionFacturasVarias.class);
	
	/**
	 * 
	 */
	private AprobacionAnulacionFacturasVariasDao objetoDao;
	
	private HashMap mapaConsulta;
	
	/**
	 * 
	 *
	 */
	public AprobacionAnulacionFacturasVarias() 
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getAprobacionAnulacionFacturasVariasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	private static AprobacionAnulacionFacturasVariasDao getDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionFacturasVariasDao();
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarFacturasVarias(Connection con) 
	{
		return  objetoDao.consultarFacturasVarias(con);
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factura
	 * @param centroAtencion
	 * @param tipoDeudor
	 * @param deudor
	 * @return
	 */
	public HashMap busquedaFacturas(
			Connection con, 
			String fechaInicial,
			String fechaFinal,
			String factura,
			String estado,
			String tipoDeudor, 
			String deudor,
			String centroAtencion) 
	{
		return  objetoDao.busquedaFacturas(
				con,
				fechaInicial,
				fechaFinal,
				factura,
				estado,
				tipoDeudor, 
				deudor,
				centroAtencion);
	}
	
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}


	public HashMap getMapaConsulta() {
		return mapaConsulta;
	}


	public void setMapaConsulta(HashMap mapaConsulta) {
		this.mapaConsulta = mapaConsulta;
	}
	
	/**
	 * indica si una multa de la factura varia esta asociada a otra factura
	 * @param Connection con
	 * @param String estado
	 * @param String codFacturaVar
	 * */
	public static boolean esMultaAsociadaOtraFactura(Connection con,String estado,String codFacturaVar)
	{		
		HashMap mapa = new HashMap();
		mapa.put("estado", estado);
		mapa.put("codFacturaVar", codFacturaVar);
		return getDao().esMultaAsociadaOtraFactura(con, mapa);
	}
	
	/**
	 * indica si la factura varia tiene asociado aplicaciones de pago y consecutivos de pagos
	 * @param Connection con
	 * @param String estadoAplicacione
	 * @param String estadoPagos
	 * @param String codFacturaVar
	 * */
	public static boolean esAsociadaPagos(Connection con,String estadoAplicaciones,String estadoPagos,String codFacturaVar)
	{
		HashMap mapa = new HashMap();
		mapa.put("estadoAplicaciones", estadoAplicaciones);
		mapa.put("estadoPagos", estadoPagos);
		mapa.put("codFacturaVar", codFacturaVar);		
		return getDao().esAsociadaPagos(con, mapa);
	}
	
	/**
	 * actualiza las multas dependientes de una factura varia
	 * @param Connection con
	 * @param String estado
	 * @param String codFacturaVar
	 * */
	public static boolean actualizarEstadoMultasXFacturaVaria(Connection con,String estado,String codFacturaVar)
	{
		HashMap mapa = new HashMap();
		mapa.put("estado", estado);	
		mapa.put("codFacturaVar", codFacturaVar);		
		return getDao().actualizarEstadoMultasXFacturaVaria(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap consultarFacturaVaria(Connection con,String codFacturaVar)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk", codFacturaVar);	
		return getDao().consultarFacturaVaria(con, mapa);
	}
	
	/**
	 * Cambia el estado de la factura a anulada
	 * @param Connection con
	 * @param String codigoFactVaria
	 * @param String usuarioLogin
	 * @return HashMap, key -> boolean exito, ActionErrors error
	 * */
	public static HashMap anulacionFacturaVaria(
			Connection con,
			String codigoFactVaria,
			String usuarioLogin,
			String fechaAnulacion,
			String motivoAnulacion)
	{
		ActionErrors errores = new ActionErrors();
		HashMap respuesta = new HashMap();
		respuesta.put("exito",false);
		respuesta.put("error",new ActionErrors());
		HashMap datosOri = consultarFacturaVaria(con,codigoFactVaria);
		
		if(!datosOri.get("numRegistros").equals("0"))
		{
			if(!esAsociadaPagos(
					con,
					ConstantesBD.codigoEstadoAplicacionPagosAprobado+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente,
					ConstantesBD.codigoEstadoPagosAplicado+"", codigoFactVaria))
			{
				HashMap vo = new HashMap();
				
				vo.put("usuario_anulacion",usuarioLogin);
				vo.put("motivo_anulacion",motivoAnulacion);
				vo.put("fecha_anulacion",fechaAnulacion);
				vo.put("usuario_modifica",usuarioLogin);
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("estado_factura",ConstantesIntegridadDominio.acronimoEstadoAnulado);
				vo.put("codigo_fac_var",codigoFactVaria);
				vo.put("fecha_gene_anulacion",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_gene_anulacion",UtilidadFecha.getHoraActual());
				logger.info("anulacionFacturaVaria ----> MAPA ---> "+vo);

				if(datosOri.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
					vo.put("anulaFacApro",ConstantesBD.acronimoSi);
				else
					vo.put("anulaFacApro",ConstantesBD.acronimoNo);

				
				if(getDao().modificar(con, vo))
				{
					//Actualiza las multas en el caso en que el concepto sea de tipo multa
					if(datosOri.get("tipoConcepto").toString().equals(ConstantesIntegridadDominio.acronimoMultas))
					{                                                                              
						actualizarEstadoMultasXFacturaVaria(con, ConstantesIntegridadDominio.acronimoEstadoGenerado, codigoFactVaria);
					}
					///
					logger.info(" -------	ANULACION DE LA FACTURA VARIA 		----------------------------------");
					logger.info("------------------------------- ESTADO  FACTURA----------------------------------"); 
					logger.info(datosOri.get("estado").toString());
					logger.info("**********************************************************************************");
				}
				else
				{
					errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el estado de la factura Varia"));
				}
			}
			else
			{
				errores.add("",new ActionMessage("errors.notEspecific","La Factura Varia ["+datosOri.get("consecutivo")+"] tiene asociado Aplicaciones de Pagos en estado Aprobado y Consecutivos de Pago en Estado Aplicado."));
			}
		}
		else
		{
			errores.add("",new ActionMessage("errors.notEspecific","No se encontro Información de la Factura Varia"));
		}
		
		
		//*********************
		if(!errores.isEmpty())
		{
			respuesta.put("exito",false);
			respuesta.put("error",errores);
		}
		else
		{
			respuesta.put("exito",true);			
		}
			
		return respuesta;
	}
	
	/**
	 * Cambia el estado de la factura a aprobada
	 * @param Connection con
	 * @param String codigoFactVaria
	 * @param String fechaAprobacion
	 * @param String motivoAprobacion,
	 * @param String usuarioLogin
	 * @return HashMap, key -> boolean exito, ActionErrors error
	 * */
	public static HashMap aprobacionFacturaVaria(
			Connection con,
			String codigoFactVaria,
			String fechaAprobacion,
			String motivoAprobacion,
			String usuarioLogin)
	{
		ActionErrors errores = new ActionErrors();
		HashMap respuesta = new HashMap();
		respuesta.put("exito",false);
		respuesta.put("error",new ActionErrors());
		HashMap datosOri = consultarFacturaVaria(con,codigoFactVaria);
		logger.info("DATOS ORIGEN: "+datosOri);
		if(!datosOri.get("numRegistros").equals("0"))
		{
			//logger.info("ES MULLTA ASOCIADA A OTRA FACTURA=? "+esMultaAsociadaOtraFactura(con,ConstantesIntegridadDominio.acronimoEstadoGenerado, codigoFactVaria));
			if(!esMultaAsociadaOtraFactura(con,ConstantesIntegridadDominio.acronimoEstadoGenerado, codigoFactVaria))
			{				
				HashMap vo = new HashMap();
				
				vo.put("usuario_aprobacion",usuarioLogin);
				vo.put("motivo_aprobacion",motivoAprobacion);
				vo.put("fecha_aprobacion",UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion));
				vo.put("usuario_modifica",usuarioLogin);
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("estado_factura",ConstantesIntegridadDominio.acronimoEstadoAprobado);
				vo.put("codigo_fac_var",codigoFactVaria);
				vo.put("anulaFacApro",ConstantesBD.acronimoNo);
				vo.put("fecha_gene_aprobacion",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_gene_aprobacion",UtilidadFecha.getHoraActual());
				logger.info("aprobacionFacturaVaria MAPA ---> "+vo);
				logger.info("Q ES ESTO ? "+getDao().modificar(con, vo));
				if(getDao().modificar(con, vo))
				{
					//Actualiza las multas en el caso en que el concepto sea de tipo multa
					if(datosOri.get("tipoConcepto").toString().equals(ConstantesIntegridadDominio.acronimoMultas))
					{                                                                              
						actualizarEstadoMultasXFacturaVaria(con, ConstantesIntegridadDominio.acronimoEstadoFacturado, codigoFactVaria);
					}
				}
				else
				{
					errores.add("",new ActionMessage("errors.notEspecific","Error al actualizar el estado de la factura Varia"));
				}
			}
			else
			{
				errores.add("",new ActionMessage("errors.notEspecific","Existen Multas Asociadas a la Factura Varia ["+datosOri.get("consecutivo")+"] que estan asociadas a otras Facturas Varias."));
			}
		}
		else
		{
			errores.add("",new ActionMessage("errors.notEspecific","No se encontro Información de la Factura Varia"));
		}
		
		
		//*********************
		if(!errores.isEmpty())
		{
			respuesta.put("exito",false);
			respuesta.put("error",errores);
		}
		else
		{
			respuesta.put("exito",true);			
		}
			
		logger.info("valor del mapa resultado >> "+respuesta);
		return respuesta;
	}
	
	/**
	 * actualiza el estado de la factura varia
	 * @param Connection con
	 * @param String codigoFactVaria
	 * @param String estado
	 * @param String usuarioLogin
	 * */
	public static boolean modificarEstadoFacturaVaria(
			Connection con,
			String codigoFactVaria,
			String fechaAprobacion,
			String motivo,
			String estado,
			String usuarioLogin)
	{
		HashMap vo = new HashMap();
		logger.info("\n\n\n\n CUANDO ENTRA ACA****\n\n\n\n");
		vo.put("usuario_anulacion",usuarioLogin);
		vo.put("motivo_anulacion",motivo);
		vo.put("fecha_anulacion",UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion));
		vo.put("usuario_modifica",usuarioLogin);
		vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("hora_modifica",UtilidadFecha.getHoraActual());
		vo.put("estado_factura",estado);
		vo.put("codigo_fac_var",codigoFactVaria);
		vo.put("anulaFacApro",ConstantesBD.acronimoNo);
		vo.put("usuario_aprobacion",usuarioLogin);
		vo.put("fecha_aprobacion",UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion));
		logger.info("\n\n\n\n\n ****  modificarEstadoFacturaVaria --> MAPA ---> "+vo+"\n\n\n\n");
		return getDao().modificar(con, vo);
	}
	
	
	/**
	 * actualizarEstadoFacturaVentaTarjeta
	 * @param con
	 * @param codigoFactua
	 * @param fechaAnulacion
	 * @param estado
	 * @param usuario
	 * @param hora
	 * @return
	 */
	public static boolean actualizarEstadoFacturaVentaTarjeta(Connection con,  String codigoFactura, String fechaAnulacion, String estado , String usuario, String hora )
	{
			actualizarEstadoVentas(con, codigoFactura, fechaAnulacion, estado, usuario, hora);
			eliminarBeneficiarios(codigoFactura);
			return true;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoFactua
	 * @param fechaAnulacion
	 * @param estado
	 * @param usuario
	 * @param hora
	 * @return
	 */
	public static boolean actualizarEstadoVentas(Connection con,  String codigoFactua, String fechaAnulacion, String estado , String usuario, String hora ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionFacturasVariasDao().actualizarEstadoFacturaVentaTarjeta(con, codigoFactua, fechaAnulacion, estado, usuario, hora);
		
	}
	
	/**
	 * Metodo para Eliminar los Beneficiarios y
	 * @param codigoFacturaVaria
	 * @return
	 */
	public static  boolean eliminarBeneficiarios(String codigoFacturaVaria){
		
		DtoVentaTarjetasCliente dto = new DtoVentaTarjetasCliente();
		dto.setCodigoPkFacturaVaria(Utilidades.convertirAEntero(codigoFacturaVaria));
		ArrayList<DtoVentaTarjetasCliente> listaVentas= new ArrayList<DtoVentaTarjetasCliente>();
		ArrayList<DtoBeneficiarioCliente> listaBenefi = new ArrayList<DtoBeneficiarioCliente>();
		listaVentas=VentasTarjetasCliente.cargar(dto);
		
		if(listaVentas.size()>0) 
		{
			DtoBeneficiarioCliente dtoBene = new DtoBeneficiarioCliente();
			logger.info("////////////////////////////CODIGO VENTA*---->"+listaVentas.get(0).getCodigoPk());
			logger.info("**********************");
			dtoBene.setVentaTarjetaCliente(listaVentas.get(0).getCodigoPk()); 
			listaBenefi= BeneficiariosTarjetaCliente.cargar(dtoBene);
			
			if(listaBenefi.size()>0)
			{
				logger.info("SI EXISTEN BENEFICIARIOS");
				 ArrayList<String> listaCodigoBeneficiarios = new ArrayList<String>(); 
				 
				 for(DtoBeneficiarioCliente obj : listaBenefi)
				 {
					 logger.info("BENEFICIARIOS codig-->"+obj.getCodigoPk());
					 listaCodigoBeneficiarios.add(obj.getCodigoPk()+"");
				 }
				 //Eliminar  Bene_TC_Paciente 
				 logger.info("*************************************************************************************");
				 logger.info("\n\n\n*-----------------------------------ELIMINAR BENEFICIARIOS--------------------------*\n\n\n\n\n--");
				 if(listaCodigoBeneficiarios.size()>0)
				 {
					logger.info("LOGGER---> "+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(listaCodigoBeneficiarios));
					 BeneficiariosTarjetaCliente.eliminarTcPaciente(listaCodigoBeneficiarios);
				 }
				 BeneficiariosTarjetaCliente.eliminar(dtoBene);
			}
			//
		}
		return true;
	}
	
	
	
	
	
	
}