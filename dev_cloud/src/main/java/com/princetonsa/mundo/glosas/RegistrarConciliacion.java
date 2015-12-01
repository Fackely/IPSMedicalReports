package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RegistrarConciliacionDao;
import com.princetonsa.dto.glosas.DtoConceptoRespuesta;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AjustesDetalleFacturaEmpresa;
import com.princetonsa.mundo.cartera.AjustesEmpresa;
import com.princetonsa.mundo.cartera.AjustesFacturaEmpresa;


public class RegistrarConciliacion
{
	private static Logger logger = Logger.getLogger(RegistrarConciliacion.class);
	
	private static RegistrarConciliacionDao getRegistrarConciliacionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistrarConciliacionDao();
	}

	public ArrayList<DtoConceptoRespuesta> consultarTiposConcepto()
	{
		return getRegistrarConciliacionDao().consultarTiposConcepto();
	}

	public int guardarConciliacion(HashMap<String, Object> mapa) {
		return getRegistrarConciliacionDao().guardarConciliacion(mapa);
	}

	public ArrayList<DtoFacturaGlosa> consultarFacturasGlosa(HashMap<String, Object> mapa) {
		return getRegistrarConciliacionDao().consultarFacturasGlosa(mapa);
	}

	public String insertarFacturasRespGlosa(DtoRespuestaFacturaGlosa dtoRespuestaFacturaGlosa, int institucion) {
		return getRegistrarConciliacionDao().insertarFacturasRespGlosa(dtoRespuestaFacturaGlosa, institucion);
	}

	public ArrayList<DtoDetalleFacturaGlosa> consultaDetFactGlosa(String auditoria, String codigoTarifario) {
		return getRegistrarConciliacionDao().consultaConceptosGlosa(auditoria, codigoTarifario);
	}
	
	public String insertarDetalleFacturaConciliacion(Connection con,DtoDetalleFacturaGlosa dto, int institucion, DtoRespuestaFacturaGlosa dtoFactResp, double ajuste){
		return getRegistrarConciliacionDao().insertarDetalleFacturaConciliacion(con, dto, institucion, dtoFactResp, ajuste);
	}
	
	public boolean guardarDetalleFactExtConciliacion(DtoRespuestaFacturaGlosa dtoRespuestaFacturaGlosa, String parametro, UsuarioBasico usuario) throws SQLException 
	{
		double ajuste=0;
		String tipoAjuste="";
		String codigoAjuste="", codAjusteDet = "";
		double valorTotalAjusteD=0, valorTotalAjusteC=0;
		AjustesEmpresa mundoAjustes = new AjustesEmpresa();
		AjustesFacturaEmpresa mundoAjustesFactura = new AjustesFacturaEmpresa();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		HashMap<String, Object> criterios= new HashMap<String, Object>();
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		
		String codigoDetalleFacturaGlosa="";
		
		if(parametro.equals(ConstantesBD.acronimoSi))
		{				
			codigoAjuste=dtoRespuestaFacturaGlosa.getConcepto().split(ConstantesBD.separadorSplit)[2];
			tipoAjuste=dtoRespuestaFacturaGlosa.getConcepto().split(ConstantesBD.separadorSplit)[1];
			
			criterios.put("institucion",usuario.getCodigoInstitucionInt());
			criterios.put("castigocartera","false");
			criterios.put("conceptocastigocartera","");
			criterios.put("usuario",usuario.getLoginUsuario());
			criterios.put("cuentacobro",dtoRespuestaFacturaGlosa.getCuentaCobro());
			criterios.put("metodoajuste",ConstantesBD.tipoMetodoAjusteCarteraManual);
			criterios.put("observaciones","");
			criterios.put("estado",ConstantesBD.codigoEstadoCarteraAprobado);
			criterios.put("ajustereversado","false");
			criterios.put("codajustereversado","0");
			
			if(tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
			{
				valorTotalAjusteD = dtoRespuestaFacturaGlosa.getValorAceptado();
				if(Utilidades.convertirAEntero(dtoRespuestaFacturaGlosa.getCuentaCobro())> 0)
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoFactura);
				}
				else
				{	
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoCuentaCobro);
				}
			}
			else
			{
				valorTotalAjusteC = dtoRespuestaFacturaGlosa.getValorAceptado();
				if(Utilidades.convertirAEntero(dtoRespuestaFacturaGlosa.getCuentaCobro()) <= 0)
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoFactura);
				}
				else
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoCuentaCobro);
				}
			}		
			
			criterios.put("conceptoajuste",codigoAjuste);
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteD);
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteC);
					
			mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, criterios);			
		}
		
		if(mundoAjustes.getCodigo()>0)
		{
			ajuste=mundoAjustes.getCodigo();
			transaccion=true;			
			dtoRespuestaFacturaGlosa.setAjuste(ajuste+"");
			logger.info("\n\ncod ajuste encabezado: "+dtoRespuestaFacturaGlosa.getAjuste());
		}
		else
		{
			transaccion=false;
		}
		
		if(transaccion)
		{
			criterios.put("codigoajuste", mundoAjustes.getCodigo());
			criterios.put("factura", dtoRespuestaFacturaGlosa.getCodigoFactura()+"");
			// inserta ajuste factura
			transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, criterios);
		}			
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("\n\n#####################TRANSACCION FINALIZADA CON EXITO###################");
			
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			logger.info("\n\n#####################ERROR. TRANSACCION ABORTADA######################");
		}				
		
		UtilidadBD.cerrarConexion(con);
		
		return false;
	}

	public boolean guardarDetalleFactConciliacion(ArrayList<DtoDetalleAsociosGlosa> listaAsociosGlosa, ArrayList<DtoDetalleFacturaGlosa> listaDetalleFacturaGlosa, DtoRespuestaFacturaGlosa dtoRespuestaFacturaGlosa, String parametro, UsuarioBasico usuario) throws SQLException 
	{
		double ajuste=0;
		String tipoAjuste="";
		String codigoAjuste="", codAjusteDet = "";
		double valorTotalAjusteD=0, valorTotalAjusteC=0, codigoAjusteInsertadoD=0, codigoAjusteInsertadoC=0;
		AjustesEmpresa mundoAjustes = new AjustesEmpresa();
		AjustesFacturaEmpresa mundoAjustesFactura = new AjustesFacturaEmpresa();
		AjustesDetalleFacturaEmpresa mundoAjusteDetalleFactura = new AjustesDetalleFacturaEmpresa();
		HashMap<String, Object> asociosGuardar = new HashMap<String, Object>();
		int encontro=0;
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		HashMap<String, Object> criterios= new HashMap<String, Object>();
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		
		String codigoDetalleFacturaGlosa="";
		
		if(parametro.equals(ConstantesBD.acronimoSi))
		{				
			codigoAjuste=dtoRespuestaFacturaGlosa.getConcepto().split(ConstantesBD.separadorSplit)[2];
			tipoAjuste=dtoRespuestaFacturaGlosa.getConcepto().split(ConstantesBD.separadorSplit)[1];
			
			criterios.put("institucion",usuario.getCodigoInstitucionInt());
			criterios.put("castigocartera","false");
			criterios.put("conceptocastigocartera","");
			criterios.put("usuario",usuario.getLoginUsuario());
			criterios.put("cuentacobro",listaDetalleFacturaGlosa.get(0).getFactura().getNumeroCuentaCobro());
			criterios.put("metodoajuste",ConstantesBD.tipoMetodoAjusteCarteraManual);
			criterios.put("observaciones","");
			criterios.put("estado",ConstantesBD.codigoEstadoCarteraAprobado);
			criterios.put("ajustereversado","false");
			criterios.put("codajustereversado","0");
			
			if(tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
			{
				valorTotalAjusteD = dtoRespuestaFacturaGlosa.getValorAceptado();
				if(listaDetalleFacturaGlosa.get(0).getFactura().getNumeroCuentaCobro()> 0)
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoFactura);
				}
				else
				{	
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoCuentaCobro);
				}
			}
			else
			{
				valorTotalAjusteC = dtoRespuestaFacturaGlosa.getValorAceptado();
				if(listaDetalleFacturaGlosa.get(0).getFactura().getNumeroCuentaCobro() <= 0)
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoFactura);
				}
				else
				{
					criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
					criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoCuentaCobro);
				}
			}		
			
			criterios.put("conceptoajuste",codigoAjuste);
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteD);
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteC);
					
			mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, criterios);			
		}
		
		if(mundoAjustes.getCodigo()>0)
		{
			ajuste=mundoAjustes.getCodigo();
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
				codigoAjusteInsertadoD=mundoAjustes.getCodigo();
			if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
				codigoAjusteInsertadoC=mundoAjustes.getCodigo();
			transaccion=true;			
			dtoRespuestaFacturaGlosa.setAjuste(ajuste+"");
			logger.info("\n\ncod ajuste encabezado: "+dtoRespuestaFacturaGlosa.getAjuste());
		}
		else
		{
			transaccion=false;
		}
		
		if(transaccion)
		{
			criterios.put("codigoajuste", mundoAjustes.getCodigo());
			criterios.put("factura", dtoRespuestaFacturaGlosa.getCodigoFactura()+"");
			// inserta ajuste factura
			transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, criterios);
		}		
		
		for(int k=0;k<(listaDetalleFacturaGlosa.size());k++)
		{
			if((Utilidades.convertirAEntero(tipoAjuste) != Utilidades.convertirAEntero(listaDetalleFacturaGlosa.get(k).getConcepto().getTipo())) && encontro == 0)
			{				
				codigoAjuste=listaDetalleFacturaGlosa.get(k).getConcepto().getCodigo();
				tipoAjuste=listaDetalleFacturaGlosa.get(k).getConcepto().getTipo();
				
				criterios.put("institucion",usuario.getCodigoInstitucionInt());
				criterios.put("castigocartera","false");
				criterios.put("conceptocastigocartera","");
				criterios.put("usuario",usuario.getLoginUsuario());
				criterios.put("cuentacobro",listaDetalleFacturaGlosa.get(k).getFactura().getNumeroCuentaCobro());
				criterios.put("metodoajuste",ConstantesBD.tipoMetodoAjusteCarteraManual);
				criterios.put("observaciones","");
				criterios.put("estado",ConstantesBD.codigoEstadoCarteraAprobado);
				criterios.put("ajustereversado","false");
				criterios.put("codajustereversado","0");
				
				if(tipoAjuste.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
				{
					valorTotalAjusteD = dtoRespuestaFacturaGlosa.getValorAceptado();
					if(listaDetalleFacturaGlosa.get(k).getFactura().getNumeroCuentaCobro()> 0)
					{
						criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
						criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoFactura);
					}
					else
					{	
						criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
						criterios.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoCuentaCobro);
					}
				}
				else
				{
					valorTotalAjusteC = dtoRespuestaFacturaGlosa.getValorAceptado();
					if(listaDetalleFacturaGlosa.get(k).getFactura().getNumeroCuentaCobro() <= 0)
					{
						criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
						criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoFactura);
					}
					else
					{
						criterios.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
						criterios.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoCuentaCobro);
					}
				}		
				
				criterios.put("conceptoajuste",codigoAjuste);
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
					criterios.put("valorajuste",valorTotalAjusteD);
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
					criterios.put("valorajuste",valorTotalAjusteC);
									
				encontro=1;
			}
		}	
		
		if(encontro > 0)
		{
			if(transaccion)
			{
				// inserta encabezado ajuste
				mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, criterios);	
			}
			
			if(mundoAjustes.getCodigo()>0)
			{
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
					codigoAjusteInsertadoD=mundoAjustes.getCodigo();
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
					codigoAjusteInsertadoC=mundoAjustes.getCodigo();
				transaccion=true;
			}
			else
				transaccion=false;
			
			if(transaccion)
			{
				criterios.put("codigoajuste", mundoAjustes.getCodigo());
				criterios.put("factura", dtoRespuestaFacturaGlosa.getCodigoFactura()+"");
				// inserta ajuste factura
				transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, criterios);					
			}
		}
		
		for(int k=0;k<(listaDetalleFacturaGlosa.size());k++)
		{
			if(transaccion)
			{			
				criterios.put("detfactsolicitud", listaDetalleFacturaGlosa.get(k).getCodigoDetalleFacturaSolicitud());
				criterios.put("pool", listaDetalleFacturaGlosa.get(k).getPool());
				criterios.put("codigomedico", listaDetalleFacturaGlosa.get(k).getCodigoMedico());
				criterios.put("escirugia", listaDetalleFacturaGlosa.get(k).getEsCirugia());
				// inserta ajuste detalle factura
									
				transaccion=generarAjusteDetalleFacturaEmpresa(con, mundoAjusteDetalleFactura, criterios);		
				codAjusteDet=mundoAjusteDetalleFactura.getCodigopk()+"";
				listaDetalleFacturaGlosa.get(k).setAjuste(codAjusteDet);
				logger.info("\n\ninserta ajuste det factura: "+listaDetalleFacturaGlosa.get(k).getAjuste());
			}			
		}	
		
		if(transaccion)
		{ 
			if(!codAjusteDet.equals(""))				
			{	
				transaccion=RegistrarRespuesta.getRegistrarRespuestaDao().actualizarValoresFacturas(con,Utilidades.convertirAEntero(criterios.get("factura")+""),
				Utilidades.convertirADouble(criterios.get("valorajuste")+""),usuario.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(criterios.get("tipoAjuste")+""));
			}
		}	
		
		for(int k=0;k<(listaDetalleFacturaGlosa.size());k++)
		{
			ajuste=Utilidades.convertirADouble(listaDetalleFacturaGlosa.get(k).getAjuste());
			listaDetalleFacturaGlosa.get(k).setCodigo(insertarDetalleFacturaConciliacion(con,listaDetalleFacturaGlosa.get(k), usuario.getCodigoInstitucionInt(), dtoRespuestaFacturaGlosa,ajuste));
			if(!listaDetalleFacturaGlosa.get(k).getCodigo().equals(""))
				transaccion=true;
			else
				transaccion= false;
			logger.info("\n\ninserta detalle factura conc: "+listaDetalleFacturaGlosa.get(k).getCodigo());
		}	

		int cont=0;
		for(int i=0; i<(listaAsociosGlosa.size()); i++)
		{			
			for(int k=0;k<(listaDetalleFacturaGlosa.size());k++)
			{			
				
				if(Utilidades.convertirAEntero(listaAsociosGlosa.get(i).getDetalleFacturaGlosa().getCodigo()) == Utilidades.convertirAEntero(listaDetalleFacturaGlosa.get(k).getConcepto().getCodigoDetalleFacturaGlosa()))
				{					
					asociosGuardar.put("codigoajuste", listaDetalleFacturaGlosa.get(k).getAjuste());
					asociosGuardar.put("codigopkserart", codAjusteDet);
					asociosGuardar.put("factura", dtoRespuestaFacturaGlosa.getCodigoFactura()+"");
					asociosGuardar.put("consecitivoasodetfac_"+cont, listaAsociosGlosa.get(i).getConsecutivoAsoDetFact());
					asociosGuardar.put("codigodetfactura_"+cont, Utilidades.convertirAEntero(listaAsociosGlosa.get(i).getDetalleFacturaGlosa().getCodigo()));
					asociosGuardar.put("codigoservicio_"+cont, listaAsociosGlosa.get(i).getCodigoServicioAsocio());
					asociosGuardar.put("codigomedico_"+cont, listaAsociosGlosa.get(i).getCodigoMedico());					
					asociosGuardar.put("codigopool_"+cont, listaAsociosGlosa.get(i).getPoolAsocio());				
					asociosGuardar.put("valorajuste_"+cont, listaAsociosGlosa.get(i).getValorStr()); 
					
					double valAjuste=Utilidades.convertirADouble(asociosGuardar.get("valorajuste_"+cont)+"");
					double valAjustePool=0;
			        double valAjusteInstitucion=0;
			        double porcentajePool=Utilidades.convertirADouble(listaAsociosGlosa.get(i).getPorcentajePool());
			        valAjustePool=valAjuste*porcentajePool/100;
			        valAjusteInstitucion=valAjuste-valAjustePool;
					
			        asociosGuardar.put("valajustepool_"+cont, valAjustePool);
			        asociosGuardar.put("valajusteinstitucion_"+cont, valAjusteInstitucion);
						    
				    asociosGuardar.put("concepto_"+cont, listaAsociosGlosa.get(i).getConceptoResp().getCodigo().split(ConstantesBD.separadorSplit)[2]);
				    asociosGuardar.put("institucion", usuario.getCodigoInstitucionInt());
				    cont++;
				}			  
			}			
		}    
		
		asociosGuardar.put("numRegistros", cont);
		
		if(transaccion)
	    	transaccion=generarAjusteAsociosDetalleFacturaEmpresa(con, mundoAjusteDetalleFactura, asociosGuardar);
	
		int codigoAsocioDetalleFacturaRespInsertado=0;
		HashMap<String, Object> mapaInsertAsoSol = new HashMap<String, Object>();
		for(int k=0;k<(listaDetalleFacturaGlosa.size());k++)
		{			   
			String codigoConcepto=listaDetalleFacturaGlosa.get(k).getConcepto().getCodigo();
			// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
			mapaInsertAsoSol.put("det_fact_resp_glosa",listaDetalleFacturaGlosa.get(k).getCodigo());
			mapaInsertAsoSol.put("asocio_auditoria_glosa",listaAsociosGlosa.get(k).getCodigoAsocio());
			mapaInsertAsoSol.put("asocio_det_factura",listaAsociosGlosa.get(k).getConsecutivoAsoDetFact());
			mapaInsertAsoSol.put("concepto_glosa",codigoConcepto);
			mapaInsertAsoSol.put("concepto_respuesta",listaAsociosGlosa.get(k).getConceptoResp().getCodigo().split(ConstantesBD.separadorSplit)[0]);
			mapaInsertAsoSol.put("institucion",usuario.getCodigoInstitucionInt());
			mapaInsertAsoSol.put("motivo",listaAsociosGlosa.get(k).getMotivo());
			mapaInsertAsoSol.put("cantidad_respuesta",listaAsociosGlosa.get(k).getCantidad());
			mapaInsertAsoSol.put("valor_respuesta",listaAsociosGlosa.get(k).getValor());
			mapaInsertAsoSol.put("ajuste",mundoAjusteDetalleFactura.getCodigopk());
					
		}
		
		logger.info("\n\nconcepto:: "+mapaInsertAsoSol.get("concepto_respuesta")+"\n\nasocios: "+asociosGuardar.get("numRegistros"));
		if(transaccion && Utilidades.convertirAEntero(asociosGuardar.get("numRegistros")+"") > 0)
			codigoAsocioDetalleFacturaRespInsertado=RegistrarRespuesta.insertarAsocioDetalleFacturaRespuesta(con, mapaInsertAsoSol);
		
		logger.info("\n\ncodigo asocio insertado:: "+codigoAsocioDetalleFacturaRespInsertado);
		if(codigoAsocioDetalleFacturaRespInsertado > 0)
			transaccion=true;
		else
			transaccion=false;
				
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("\n\n#####################TRANSACCION FINALIZADA CON EXITO###################");
			
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			logger.info("\n\n#####################ERROR. TRANSACCION ABORTADA######################");
		}				
		
		UtilidadBD.cerrarConexion(con);
		
		return false;
	}
	
	/**
	 * Metodo que inserta los ajustes de los asocios del detalle factura
	 * @param con
	 * @param mundoAjustes
	 * @param asociosGuardarMap
	 * @return
	 */
	private static boolean generarAjusteAsociosDetalleFacturaEmpresa(Connection con, AjustesDetalleFacturaEmpresa mundoAjustes, HashMap asociosGuardarMap)
	{
		// Se carga el mapa de los asocios
		mundoAjustes.setAsociosServicio(asociosGuardarMap);
		if(mundoAjustes.insertarAsociosServicio(con))
			return true;
		return false;
	}
	
	/**
	 * Metodo que inserta el Ajuste Detalle Factura
	 * @param con
	 * @param mundoAjustes
	 * @param criteriosMap
	 * @return
	 */
	private static boolean generarAjusteDetalleFacturaEmpresa(Connection con, AjustesDetalleFacturaEmpresa mundoAjustes, HashMap criteriosMap)
	{
		// Se llenan los atributos del mundo Ajuste necesario para generar Ajuste Detalle Factura

		mundoAjustes.setCodigo(Utilidades.convertirADouble(criteriosMap.get("codigoajuste")+""));
		mundoAjustes.setFactura(Utilidades.convertirAEntero(criteriosMap.get("factura")+""));
		mundoAjustes.setDetFactSolicitud(Utilidades.convertirAEntero(criteriosMap.get("detfactsolicitud")+""));
		mundoAjustes.setCodigoPool(Utilidades.convertirAEntero(criteriosMap.get("pool")+""));
		mundoAjustes.setCodigoMedicoResponde(Utilidades.convertirAEntero("codigomedico"));
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		
		double valAjuste=Utilidades.convertirADouble(criteriosMap.get("valorajuste")+"");
		double valAjustePool=0;
        double valAjusteInstitucion=0;
        if(UtilidadTexto.getBoolean(criteriosMap.get("escirugia")+""))
        {
        	valAjustePool=valAjuste*Utilidades.obtenerPorcentajePoolFacturacion(con, Utilidades.convertirAEntero(criteriosMap.get("detfactsolicitud")+""))/100;
        	valAjusteInstitucion=valAjuste-valAjustePool;
        }
		
		mundoAjustes.setValorAjustePool(valAjustePool);
		mundoAjustes.setValorAjusteInstitucion(valAjusteInstitucion);
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se inserta Ajuste Detalle Factura
		if(mundoAjustes.insertarAjuste(con))
			return true;
		return false;
	}
	
	/**
	 * Metodo que inserta el Ajuste Factura
	 * @param con
	 * @param mundoAjustes
	 * @param criteriosMap
	 * @return
	 */
	private static boolean generarAjusteFacturaEmpresa(Connection con, AjustesFacturaEmpresa mundoAjustes, HashMap criteriosMap)
	{
		// Se llenan los atributos del mundo Ajuste necesario para generar Ajuste Factura
		mundoAjustes.setCodigo(Utilidades.convertirADouble(criteriosMap.get("codigoajuste")+""));
		mundoAjustes.setFactura(Utilidades.convertirAEntero(criteriosMap.get("factura")+""));
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se inserta Ajuste Factura
		if(mundoAjustes.insertarAjusteFacturaEmpresa(con))
			return true;
		return false;
	}
	
	private static AjustesEmpresa generarEncabezadoAjuste(Connection con, AjustesEmpresa mundoAjustes, HashMap criteriosMap) 
	{
		// fecha Actual
		String fechaActual = UtilidadFecha.getFechaActual();
		// hora Actual
		String horaActual = UtilidadFecha.getHoraActual();
		// Se obtiene el consecutivo de Ajuste
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(criteriosMap.get("naturaleza")+"",Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se llenan los atributos del mundo Ajuste necesario para generar Encabezado Ajuste
		mundoAjustes.setConsecutivoAjuste(consecutivo);
		mundoAjustes.setTipoAjuste(Utilidades.convertirAEntero(criteriosMap.get("tipoAjuste")+""));
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		mundoAjustes.setCastigoCartera(UtilidadTexto.getBoolean(criteriosMap.get("castigocartera")+""));
		mundoAjustes.setConceptoCastigoCartera(criteriosMap.get("conceptocastigocartera")+"");
		mundoAjustes.setFechaAjuste(fechaActual);
		mundoAjustes.setFechaElaboracion(fechaActual);
		mundoAjustes.setHoraElaboracion(horaActual);
		mundoAjustes.setUsuario(criteriosMap.get("usuario")+"");
		mundoAjustes.setCuentaCobro(Utilidades.convertirADouble(criteriosMap.get("cuentacobro")+""));
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		mundoAjustes.setObservaciones(criteriosMap.get("observaciones")+"");
		mundoAjustes.setEstado(Utilidades.convertirAEntero(criteriosMap.get("estado")+""));
		mundoAjustes.setAjusteResversado(UtilidadTexto.getBoolean(criteriosMap.get("ajustereversado")+""));
		mundoAjustes.setCodAjusteReversado(Utilidades.convertirADouble(criteriosMap.get("codajustereversado")+""));
		// Se inserta encabezado Ajuste
		logger.info("\n\ncriteriosMap: "+criteriosMap);
		if(mundoAjustes.insertarAjusteGeneral(con))
			//si guardo encabezado se finaliza consecutivo
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,criteriosMap.get("naturaleza")+"", Utilidades.convertirAEntero(criteriosMap.get("institucion")+""), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		else
			//sino guardo encabezado se deja consecutivo pa reutilizar
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,criteriosMap.get("naturaleza")+"", Utilidades.convertirAEntero(criteriosMap.get("institucion")+""), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		return mundoAjustes;
	}	
	
	public ArrayList<DtoRespuestaFacturaGlosa> consultaFacturasConciliacion(String codconciliacion) 
	{
		return getRegistrarConciliacionDao().consultaFacturasConciliacion(codconciliacion);
	}

	public ArrayList<DtoRespuestaSolicitudGlosa> consultaSolResp(int respuesta, String codTarifario, int institucion) 
	{
		return getRegistrarConciliacionDao().consultaSolResp(respuesta, codTarifario, institucion);
	}

	public ArrayList<DtoDetalleAsociosGlosa> consultarAsociosDetFacturaResp(int detAuditoriaGlosa) {
		return getRegistrarConciliacionDao().consultarAsociosDetFacturaResp(detAuditoriaGlosa);
	}

	public ArrayList<DtoFacturaGlosa> consultarConceptosGlosaFact(String auditoria) {
		return getRegistrarConciliacionDao().consultarConceptosGlosaFact(auditoria);
	}


}