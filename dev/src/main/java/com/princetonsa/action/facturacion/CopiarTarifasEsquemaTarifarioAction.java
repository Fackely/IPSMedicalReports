package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.CopiarTarifasEsquemaTarifarioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CopiarTarifasEsquemaTarifario;
import com.servinte.axioma.fwk.exception.IPSException;


public class CopiarTarifasEsquemaTarifarioAction extends Action 
{

	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(CopiarTarifasEsquemaTarifarioAction.class);
	
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof CopiarTarifasEsquemaTarifarioForm) 
			{
				CopiarTarifasEsquemaTarifarioForm forma=(CopiarTarifasEsquemaTarifarioForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				CopiarTarifasEsquemaTarifario mundo=new CopiarTarifasEsquemaTarifario();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("tarifarioDestino"))
				{
					forma.setEstado("empezar");
					forma.setTarifarioDestino("-");
					forma.resetMensaje();
					forma.setTarifarioOrigenMap(mundo.obtenerTarifario(con,forma.getTarifarioOrigen(),usuario.getCodigoInstitucionInt()+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con, forma, mundo, usuario);
					//mundo.procesoGurdar(con, forma.getTarifarioOrigen(), forma.getTarifarioDestino(), forma.getPorcentaje(), forma.getChequeo());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, CopiarTarifasEsquemaTarifarioForm forma, CopiarTarifasEsquemaTarifario mundo, UsuarioBasico usuario) throws IPSException 
	{
		
		forma.setTarifarioDestinoMap(mundo.obtenerTarifario(con, forma.getTarifarioDestino(), usuario.getCodigoInstitucion()));
		forma.setMapaTarifas(mundo.consultarTarifas(con, forma.getTarifarioOrigen(), forma.getTarifarioOrigenMap().get("esinventario_0")+"", forma.getTarifarioOrigenMap().get("tarifariooficial_0")+""));
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		for(int i=0;i<Integer.parseInt(forma.getMapaTarifas("numRegistros")+"");i++)
		{
			
			if(UtilidadTexto.getBoolean(forma.getTarifarioOrigenMap().get("esinventario_0")+""))
			{
				if(!CopiarTarifasEsquemaTarifario.existeTarifaInventarios(con, Utilidades.convertirAEntero(forma.getTarifarioDestino()), Utilidades.convertirAEntero(forma.getMapaTarifas("articulo_"+i)+"")))
				{
					if(forma.getMapaTarifas().get("tipotarifa_"+i).toString().equals(ConstantesIntegridadDominio.acronimoValorFijo))
					{	
						HashMap vo=new HashMap();
						vo.put("codigo",forma.getMapaTarifas("codigo_"+i));
						vo.put("esquema_tarifario",forma.getTarifarioDestino());
						vo.put("articulo",forma.getMapaTarifas("articulo_"+i));
						vo.put("valor_tarifa", UtilidadValidacion.aproximarMetodoAjuste(forma.getTarifarioDestinoMap().get("metodoajuste_0")+"", Utilidades.convertirADouble(forma.getMapaTarifas("valortarifa_"+i)+"")));
						vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
						vo.put("tipo_tarifa",forma.getMapaTarifas("tipotarifa_"+i));
						vo.put("porcentaje",forma.getMapaTarifas("porcentaje_"+i));
						vo.put("actualiz_automatic",forma.getMapaTarifas("actualizautomatic_"+i));
						vo.put("usuario_modifica",usuario.getLoginUsuario());
						vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						vo.put("hora_modifica",UtilidadFecha.getHoraActual());
						vo.put("metodo_ajuste", forma.getTarifarioDestinoMap().get("metodoajuste_0"));
						vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
						transaccion=mundo.insertarInventarioPorcentaje(con, vo, forma.getPorcentaje(), forma.getChequeo());
					}
					else
					{
						HashMap vo=new HashMap();
						vo.put("codigo",forma.getMapaTarifas("codigo_"+i));
						vo.put("esquema_tarifario",forma.getTarifarioDestino());
						vo.put("articulo",forma.getMapaTarifas("articulo_"+i));
						vo.put("valor_tarifa",forma.getMapaTarifas("valortarifa_"+i));
						vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
						vo.put("tipo_tarifa",forma.getMapaTarifas("tipotarifa_"+i));
						vo.put("porcentaje",forma.getMapaTarifas("porcentaje_"+i));
						vo.put("actualiz_automatic",forma.getMapaTarifas("actualizautomatic_"+i));
						vo.put("usuario_modifica",usuario.getLoginUsuario());
						vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						vo.put("hora_modifica",UtilidadFecha.getHoraActual());
						vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
						
						transaccion=mundo.insertarInventario(con, vo);
					}
				}	
			}
			else
			{
				if(forma.getTarifarioOrigenMap().get("tarifariooficial_0").toString().equals(ConstantesBD.codigoTarifarioISS+""))
				{
					if(!CopiarTarifasEsquemaTarifario.existeTarifaServicios(con, Utilidades.convertirAEntero(forma.getTarifarioDestino()), Utilidades.convertirAEntero(forma.getMapaTarifas("servicio_"+i)+""), ConstantesBD.codigoTarifarioISS))
					{
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatValor+""))
						{	
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigoiss_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa", UtilidadValidacion.aproximarMetodoAjuste(forma.getTarifarioDestinoMap().get("metodoajuste_0")+"", Utilidades.convertirADouble(forma.getMapaTarifas("valortarifa_"+i)+"")));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("metodo_ajuste", forma.getTarifarioDestinoMap().get("metodoajuste_0"));
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasIssValor(con, vo, forma.getPorcentaje(), forma.getChequeo());
						}
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatGrupo+"")||forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatUvr+""))
						{	
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigoiss_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa",forma.getMapaTarifas("valortarifa_"+i));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasIss(con, vo);
						}
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatUnidades+""))
						{	
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigoiss_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa",forma.getMapaTarifas("valortarifa_"+i));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("metodo_ajuste", forma.getTarifarioDestinoMap().get("metodoajuste_0"));
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasIssUnidades(con, vo, forma.getTarifarioDestinoMap().get("cantidad_0")+"", forma.getMapaTarifas().get("unidades_"+i)+"");
						}
					}	
				}
				else
				{
					if(!CopiarTarifasEsquemaTarifario.existeTarifaServicios(con, Utilidades.convertirAEntero(forma.getTarifarioDestino()), Utilidades.convertirAEntero(forma.getMapaTarifas("servicio_"+i)+""), ConstantesBD.codigoTarifarioSoat))
					{
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatValor+""))
						{
							
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigosoat_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa", UtilidadValidacion.aproximarMetodoAjuste(forma.getTarifarioDestinoMap().get("metodoajuste_0")+"", Utilidades.convertirADouble(forma.getMapaTarifas("valortarifa_"+i)+"")));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("metodo_ajuste", forma.getTarifarioDestinoMap().get("metodoajuste_0"));
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasSoatValor(con, vo, forma.getPorcentaje(), forma.getChequeo());
						}
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatGrupo+"")||forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatUvr+""))
						{
							
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigosoat_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa",forma.getMapaTarifas("valortarifa_"+i));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasSoat(con, vo);
						}
						if(forma.getMapaTarifas().get("tipoliquidacion_"+i).toString().equals(ConstantesBD.codigoTipoLiquidacionSoatUnidades+""))
						{
							
							HashMap vo=new HashMap();
							vo.put("codigo",forma.getMapaTarifas("codigosoat_"+i));
							vo.put("esquema_tarifario",forma.getTarifarioDestino());
							vo.put("valor_tarifa",forma.getMapaTarifas("valortarifa_"+i));
							vo.put("porcentaje_iva",forma.getMapaTarifas("porcentajeiva_"+i));
							vo.put("servicio",forma.getMapaTarifas("servicio_"+i));
							vo.put("tipo_liquidacion",forma.getMapaTarifas("tipoliquidacion_"+i));
							vo.put("unidades",forma.getMapaTarifas("unidades_"+i));
							vo.put("actualiza_automatica",forma.getMapaTarifas("actualizaautomatica_"+i));
							vo.put("liq_asocios",forma.getMapaTarifas("liqasocios_"+i));
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							vo.put("hora_modifica",UtilidadFecha.getHoraActual());
							vo.put("metodo_ajuste", forma.getTarifarioDestinoMap().get("metodoajuste_0"));
							vo.put("fechavigencia", forma.getMapaTarifas("fechavigencia_"+i));
							
							transaccion=mundo.insertarTarifasSoatUnidades(con, vo, forma.getTarifarioDestinoMap().get("cantidad_0")+"", forma.getMapaTarifas().get("unidades_"+i)+"");
						}
					}
				}
			}
			
			if(!transaccion)
				i=Utilidades.convertirAEntero(forma.getMapaTarifas("numRegistros")+"");
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			forma.resetPorcentaje();
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			forma.resetPorcentaje();
			UtilidadBD.abortarTransaccion(con);
		}
		
	}
	
	
}
