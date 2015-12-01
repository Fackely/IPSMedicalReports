package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturasVarias.AprobacionAnulacionFacturasVariasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionFacturasVarias;


public class AprobacionAnulacionFacturasVariasAction extends Action 
{

	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(AprobacionAnulacionFacturasVariasAction.class);
	
	
	
	/**
	 *  Matriz que contiene los indices del mapa.
	 */
	
	String[] indices={"motivoanulacion_","codigo_empresa_","codigoconcepto_","consecutivo_","tipodeudor_","fechaelaboracion_","descripciondeudor_","estadold_","codigofactura_","centroatencion_","valor_","centrocosto_","idpersona_","codigo_tercero_","codigodeudor_","descripcionpersona_","codigo_paciente_","descripcionconcepto_","estado_","fechaaprobanulacion_","observaciones_","iddeudor_","tipo_concepto_","descripcionrazonsocial_","puedo_anular_","mensaje_validacion_"};
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con=null;
		try{
			if (form instanceof AprobacionAnulacionFacturasVariasForm) 
			{
				AprobacionAnulacionFacturasVariasForm forma=(AprobacionAnulacionFacturasVariasForm) form;

				//ActionErrors errores = new ActionErrors();

				String estado=forma.getEstado();

				int pos= 0;

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				AprobacionAnulacionFacturasVarias mundo=new AprobacionAnulacionFacturasVarias();

				forma.setMostrarMensaje(new ResultadoBoolean(false,""));

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de AprobacionAnulacionDevolucionesAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");

					/**
					 * forma.reset();
				forma.setMapaConsulta(mundo.consultarFacturasVarias(con));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal"); 
					 * */
				}			
				else if(estado.equals("busquedaAvanzada"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("buscar"))
				{				
					forma.setMaxItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
					forma.setMapaConsulta(mundo.busquedaFacturas(
							con,
							forma.getFechaInicial(),
							forma.getFechaFinal(),
							forma.getFactura(),
							forma.getEstadoFactura(),
							forma.getTipoDeudor(),
							forma.getCodigoDeudor(),
							forma.getCentroAtencion()));
					logger.info("****************************************************************************************");
					logger.info("	-------------------------------------------- RESULTADO MAPA	-------------------------");
					logger.info("\n\n mapa resultado consulta \n\n\n\n\n\n\n\n\n\n  -------->"+forma.getMapaConsulta());
					logger.info("\n\n\n\n\n\n");
					logger.info("****************************************************************************************");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					//guardamos en BD.
					if(this.accionGuardarRegistros(con,request,forma,mundo,usuario, pos))
					{
						//forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));

						forma.setMapaConsulta(mundo.busquedaFacturas(
								con,
								forma.getFechaInicial(),
								forma.getFechaFinal(),
								forma.getFactura(),
								forma.getEstadoFactura(),
								forma.getTipoDeudor(),
								forma.getCodigoDeudor(),
								forma.getCentroAtencion()));
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				/*****************************************************
				 * Estado para el control del pager  
				 *****************************************************/
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}			
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de APROBACION/ANULACION FACTURAS VARIAS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
			}
			else
			{
				logger.error("El form no es compatible con el form de AprobacionAnulacionFacturasVariasForm");
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
	private boolean accionGuardarRegistros(
			Connection con, 
			HttpServletRequest request,
			AprobacionAnulacionFacturasVariasForm forma,
			AprobacionAnulacionFacturasVarias mundo,			
			UsuarioBasico usuario,
			int pos)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap resultado = new HashMap();
		
		boolean huboDiferentes=false;
		logger.info("mapaFacturas-->"+forma.getMapaConsulta());
		
		for(int i=0;i<Integer.parseInt(forma.getMapaConsulta("numRegistros")+"");i++) // forma.getMapaConsulta("numRegistros") <---ERROR PARA MODIFICAR ALGORITMO  
		{	
			String tmp= forma.getMapaConsulta().get("estado_"+i).toString();
			String tmp2 =forma.getMapaConsulta().get("estadold_"+i).toString();
			logger.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
			logger.info("COMPARCIONES de estado para MODIFICACIONES RESPECTIVAS ");
			logger.info( tmp + "--------"+tmp2);
			logger.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
				
			
			if(!forma.getMapaConsulta().get("estado_"+i).toString().equals(forma.getMapaConsulta().get("estadold_"+i).toString()))
			{
				huboDiferentes=true;
				if(forma.getMapaConsulta().get("estado_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				{
					resultado = AprobacionAnulacionFacturasVarias.anulacionFacturaVaria(
								con, 
								forma.getMapaConsulta("codigofactura_"+i).toString(),
								usuario.getLoginUsuario(),
								UtilidadFecha.conversionFormatoFechaABD(forma.getMapaConsulta("fechaaprobanulacion_"+i)+"").toString(),
								forma.getMapaConsulta("motivoanulacion_"+i).toString());
					/*
					 *	Modificar el estado de la Venta. ANULACION
					 * Anexo 830
					 */
					AprobacionAnulacionFacturasVarias.actualizarEstadoFacturaVentaTarjeta( con, forma.getMapaConsulta("codigofactura_"+i).toString() ,  UtilidadFecha.conversionFormatoFechaABD(forma.getMapaConsulta("fechaaprobanulacion_"+i)+"").toString() ,  ConstantesIntegridadDominio.acronimoEstadoAnulado, usuario.getLoginUsuario(), UtilidadFecha.getHoraActual()  );
					
					if(!UtilidadTexto.getBoolean(resultado.get("exito").toString()))
					{
						transaccion = false;
						saveErrors(request,(ActionErrors)resultado.get("error"));
					}
				}
				else if(forma.getMapaConsulta().get("estado_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
				{
					resultado = AprobacionAnulacionFacturasVarias.aprobacionFacturaVaria(
							con,
							forma.getMapaConsulta("codigofactura_"+i).toString(),
							UtilidadFecha.conversionFormatoFechaABD(forma.getMapaConsulta("fechaaprobanulacion_"+i)+"").toString(),
							forma.getMapaConsulta("motivoanulacion_"+i).toString(),
							usuario.getLoginUsuario());
					
					if(!UtilidadTexto.getBoolean(resultado.get("exito").toString()))
					{
						transaccion = false;
						saveErrors(request,(ActionErrors)resultado.get("error"));
					}
				}
				else
				{
					if(!AprobacionAnulacionFacturasVarias.modificarEstadoFacturaVaria(
						con,
						forma.getMapaConsulta("codigofactura_"+i).toString(),
						UtilidadFecha.conversionFormatoFechaABD(forma.getMapaConsulta("fechaaprobanulacion_"+i)+"").toString(),
						forma.getMapaConsulta("motivoanulacion_"+i).toString(),
						forma.getMapaConsulta("estado_"+i).toString(), 
						usuario.getLoginUsuario()))
					{
						transaccion = false;
						ActionErrors errores = new ActionErrors();
						errores.add("",new ActionMessage("errors.notEspecific","Error al Modificar la Factura Varia ["+forma.getMapaConsulta("consecutivo_"+i).toString()+"]"));
						saveErrors(request,(ActionErrors)resultado.get("error"));
					}
				}
			}
		}
		
		if(transaccion)
		{
			if (huboDiferentes)
				forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			else
				forma.setMostrarMensaje(new ResultadoBoolean(true,"No Hubo Cambios en los estados de las Facturas Varias."));
			//this.generarLog(forma, mundo, usuario, pos);
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param pos
	 */
	private void generarLog(AprobacionAnulacionFacturasVariasForm forma, AprobacionAnulacionFacturasVarias mundo, UsuarioBasico usuario, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
		for(int i=0;i<(indices.length-1);i++)
		{
			log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getMapaConsulta().get(indices[i]+"0")+""):mundo.getMapaConsulta().get(indices[i]+"0")) + " ]";
		}
		log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
		for(int i=0;i<(indices.length-1);i++)
		{
			log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getMapaConsulta(indices[i]+""+pos)+""):forma.getMapaConsulta(indices[i]+""+pos)) +" ]";
		}
		log+= "\n========================================================\n\n\n ";
		tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		
		LogsAxioma.enviarLog(ConstantesBD.logPaquetesCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(AprobacionAnulacionFacturasVariasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaConsulta("numRegistros")+"");
		forma.setMapaConsulta(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaConsulta(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaConsulta("numRegistros",numReg+"");
		logger.info("\n\nMAPA CONSULTAAAA----->"+forma.getMapaConsulta());
		
	}
	
	
	
	
}
