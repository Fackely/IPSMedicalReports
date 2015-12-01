/*
 * Created on 30/09/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velï¿½squez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.AnulacionRecibosCajaForm;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.carteraPaciente.ApliPagosCarteraPaciente;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;
import com.princetonsa.mundo.tesoreria.AnulacionRecibosCaja;
import com.princetonsa.mundo.tesoreria.RecibosCaja;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;

/**
 * @version 1.0, 30/09/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velï¿½squez/a>
 */
public class AnulacionRecibosCajaAction extends Action
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AnulacionRecibosCajaAction.class);
	
	/**
	 * Mï¿½todo execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try{

			if(form instanceof AnulacionRecibosCajaForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				AnulacionRecibosCajaForm forma=(AnulacionRecibosCajaForm)form;
				AnulacionRecibosCaja mundo=new AnulacionRecibosCaja();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [AnulacionRecibosCajaAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
				else if (estado.equals("empezar"))
				{
					boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(usuario.getCodigoInstitucionInt()));

					boolean errorConsecutivo=false;
					if(manejaConsecutivoTesoreriaXCentroAtencion)
					{
						BigDecimal consecutivo=ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
								usuario.getCodigoCentroAtencion(), 
								EmunConsecutivosTesoreriaCentroAtencion.AnulacionReciboCaja.getNombreConsecutivoBaseDatos(), 
								UtilidadFecha.getMesAnioDiaActual("anio"));
						if(consecutivo==null || consecutivo.intValue()<=0)
						{
							errorConsecutivo=true;
						}
					}
					if(!Utilidades.isDefinidoConsecutivo(con,usuario.getCodigoInstitucionInt(),ConstantesBD.nombreConsecutivoAnulacionRecibosCaja))
					{
						errorConsecutivo=true;
					}
					if(errorConsecutivo)
					{
						logger.warn("FALTA DEFINIR EL CONSECUTIVO DE ANULACION RECIBOS CAJA Verifique. Proceso Cancelado.");
						request.setAttribute("codigoDescripcionError", "error.faltaDefinirConsecutivoAnulacionReciboCaja");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
					forma.reset();
					forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarReciboCaja"))
				{
					return accionCargarReciboCaja(con,forma,mundo, usuario, mapping, request);
				}
				else if(estado.equals("guardar"))
				{
					return accionGuardarAnulacionReciboCaja(con,forma,mundo,usuario,request,mapping);

				}
				else if(estado.equals("generarReporte"))
				{                
					this.generarReporte(con, usuario, request);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptosCarteraForm");
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
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     * @param mapping
     * @param request
     * @throws SQLException
     */
    private ActionForward accionGuardarAnulacionReciboCaja(Connection con, AnulacionRecibosCajaForm forma, AnulacionRecibosCaja mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws SQLException
    {
    	ActionErrors errores= new ActionErrors();
    	
    	IMovimientosCajaServicio movimientosCajaServicio = TesoreriaFabricaServicio.crearMovimientosCajaServicio();

		UtilidadTransaccion.getTransaccion().begin();

		Cajas caja=new CajasDelegate().findById(forma.getConsecutivoCaja());
    	boolean esCajaTipoRecaudo=caja.getTiposCaja().getCodigo()==ConstantesBD.codigoTipoCajaRecaudado;
    	
		UsuarioBasico usuarioCaja=new UsuarioBasico();
		usuarioCaja.setLoginUsuario(forma.getLoginUsuarioCaja());
		DtoTurnoDeCajaApta dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuarioCaja, caja);
		UtilidadTransaccion.getTransaccion().commit();
		int codigoAnulacion=ConstantesBD.codigoNuncaValido;
		if( (dtoApto.isTurnoAbierto() && esCajaTipoRecaudo) || !esCajaTipoRecaudo)
		{

			if(
					!RecibosCaja.estaRegistradoEnAplicacionPagosFacturasVarias(con, forma.getCodigoReciboCaja(), usuario.getCodigoInstitucion()) 
					|| 
					(
							(forma.getConceptosRC("tipoconceptorc_0")+"").equals("3")
							&& 
							forma.getConceptosRC("valortipoconceptorc_0").toString().equals(ConstantesIntegridadDominio.acronimoFactura)
					)
			)
			{
    	  
				boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
				//en la transaccion
				{
					if(enTransaccion)
					{
						RecibosCaja mundoRecibosCaja= new RecibosCaja();
						ArrayList filtroAnul=new ArrayList();
						filtroAnul.add(forma.getCodigoReciboCaja());
						filtroAnul.add(usuario.getCodigoInstitucionInt());
						
						logger.info("\n\n BLOQUEA REGISTRO --------------------------------------------------");
						boolean pudoBloquear=UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoReciboCaja,filtroAnul);
						
						
						if(!pudoBloquear)
						{
							errores.add("", new ActionMessage("errors.notEspecific","Este recibo de caja se encuentra en proceso de Arqueo Definitivo/Cierre Caja."));
							saveErrors(request,errores);
							UtilidadBD.finalizarTransaccion(con);
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("paginaPrincipal");
						}
						logger.info("\n\n CARGAR ESTADO RECIBO DE CAJA ");
						String estadoReciboCaja[]=(Utilidades.obtenerEstadoReciboCaja(forma.getCodigoReciboCaja(),usuario.getCodigoInstitucionInt()).split(ConstantesBD.separadorSplit));
						
						if(Integer.parseInt(estadoReciboCaja[0])!=ConstantesBD.codigoEstadoReciboCajaRecaudado)
						{
							errores.add("Recibo Caja Estado Diferente", new ActionMessage("error.reciboCajaEstadoDiferente",forma.getNumeroReciboCaja(),estadoReciboCaja[1]));
						}
						
						if(!errores.isEmpty())
						{
							saveErrors(request,errores);
							UtilidadBD.finalizarTransaccion(con);
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("paginaPrincipal");
						}
						
						logger.info("\n\n ACTUALIZAR ESTADOS RECIBO DE CAJA ");
						enTransaccion=mundoRecibosCaja.actualizarEstadoReciboCaja(con,forma.getCodigoReciboCaja(),forma.getInstitucion(),ConstantesBD.codigoEstadoReciboCajaAnulado);
						
					}    
					if(enTransaccion)
					{
						mundo.setNumeroReciboCaja(forma.getCodigoReciboCaja());
						mundo.setInstitucion(forma.getInstitucion());
						mundo.setUsuarioAnulacion(usuario.getLoginUsuario());
						mundo.setFechaAnulacion(UtilidadFecha.getFechaActual());
						mundo.setHoraAnulacion(UtilidadFecha.getHoraActual());
						mundo.setMotivo(forma.getMotivo());
						mundo.setObservaciones(forma.getObservaciones());
						mundo.setTotalRecibidoCaja(forma.getTotalRecibidoCaja());
						mundo.setCodigoCentroAtencion(forma.getCodigoCentroAtencion());
						//mundo.setValorAntRecConvenio();
						
						String consecutivo="";
						boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(usuario.getCodigoInstitucionInt()));
						if(manejaConsecutivoTesoreriaXCentroAtencion)
						{
							consecutivo=ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(usuario.getCodigoCentroAtencion(), 
									EmunConsecutivosTesoreriaCentroAtencion.AnulacionReciboCaja.getNombreConsecutivoBaseDatos(), usuario.getCodigoInstitucionInt()).toString();
						}
						else
						{
							consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAnulacionRecibosCaja,forma.getInstitucion());
						}
						mundo.setConsecutivoAnulacionReciboCaja(consecutivo);
						
						logger.info("\n\n\n INGRESAR ANULACION RECIBO DE CAJA ");
						codigoAnulacion=mundo.ingresarAnulacionReciboCaja(con);    
						forma.setNumeroAnulacionReciboCaja(mundo.getConsecutivoAnulacionReciboCaja());
						forma.setFechaAnulacionReciboCaja(mundo.getFechaAnulacion());
						forma.setHoraAnulacionReciboCaja(mundo.getHoraAnulacion());
						forma.setUsuarioAnula(usuario.getLoginUsuario());
						
						
						enTransaccion=codigoAnulacion>0;
						if(enTransaccion &&  forma.isExisteReciboCajaXConceptAnticipConvenioOdonto() )
						{
							logger.info("Entro a Actualizar el valor Anticipo Recibido Convenio");
							
							// Cargar dtoControlAnticipos 
							ArrayList<DtoControlAnticiposContrato> dtoControlAnticipos = new ArrayList<DtoControlAnticiposContrato>();
							dtoControlAnticipos = Contrato.obtenerControlAnticipos(con, forma.getNumContratoOdontologia());
							
							logger.info(dtoControlAnticipos.get(0).getValorAnticipoDisponible()+" - "+mundo.getTotalRecibidoCaja());
							double nuevoValorAnticipoRecConv=dtoControlAnticipos.get(0).getValorAnticipoRecibidoConvenio()-mundo.getTotalRecibidoCaja();
							
							logger.info(dtoControlAnticipos.get(0).getValorAnticipoRecibidoConvenio()-mundo.getTotalRecibidoCaja());
							logger.info("\n\nnuevoValorAnticipoRecConv "+nuevoValorAnticipoRecConv);
							
							/*valor_ant_rec_convenio
							 * 
							 * Validacion Anexo 164.
							 * 
							 *Verificar que el Valor del Recibo de caja sea Menor al Valor Anticipo Disponible del contrato asociado 
							 *al convenio del recibo de caja.
							 */
							if(nuevoValorAnticipoRecConv>0)
							{
								double tmpFinal= dtoControlAnticipos.get(0).getValorAnticipoReservadoConvenio()-mundo.getTotalRecibidoCaja();
								
								logger.info("nuevo Valor Total ->"+ tmpFinal);
								
								enTransaccion = mundo.actualizarValorAnticipo(con, forma.getNumContratoOdontologia() , nuevoValorAnticipoRecConv , usuario.getLoginUsuario());
							}
							else
							{
								errores.add("", new ActionMessage("errors.notEspecific", " Recibo de caja No. "+forma.getNumeroReciboCaja()+", no se puede Anular por no tener Saldo Disponible "));
								saveErrors(request,errores);
								UtilidadBD.finalizarTransaccion(con);
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("paginaPrincipal");
							}
						}
						
						
						
						if(enTransaccion)
						{
							
							UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAnulacionRecibosCaja, forma.getInstitucion(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						}
						else
						{
							UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAnulacionRecibosCaja, forma.getInstitucion(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						}
					}
					if(enTransaccion)
					{
						//actualizaciones necesarias
						
						boolean paciente=false,convenios=false,abonos=false;
						for(int i=0;i<Integer.parseInt(forma.getConceptosRC("numeroregistros")+"");i++)
						{
							if(!paciente&&Integer.parseInt(forma.getConceptosRC("tipoconceptorc_"+i)+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
							{
								/*
                        se actualiza con el primero concepto todos los pagos, y luego no se vuelve a entrar en caso de que exits otro concepto de este tipo
                        por esta razon paciente=true.
								 */
								String[] codigoPagosFacturaPaciente=mundo.getPagosFacturaPacienteReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion());
								for(int j=0;j<codigoPagosFacturaPaciente.length;j++)
								{
									enTransaccion=Utilidades.cambiarEstadoPacienteFactura(con,Utilidades.obtenerCodigoFacturaPagosPaciente(con,Integer.parseInt(codigoPagosFacturaPaciente[j])),ConstantesBD.codigoEstadoFacturacionPacientePorCobrar);
									enTransaccion=Utilidades.cambiarEstadoPagoFacturaPacient(con,Integer.parseInt(codigoPagosFacturaPaciente[j]),ConstantesBD.codigoEstadoPagosAnulado);
									
									//****************************************************************************
									// 	Anexo 762
									logger.info("valor is_eliminar: "+forma.getEliminarRC());
									if(!forma.getEliminarRC().equals(ConstantesBD.acronimoSi))
									{
										String aux[] = forma.getEliminarRC().split("-");
										for(String elem:aux)
											logger.info("elem: "+elem);
										if(forma.getEliminarRC().contains("-"))
											DatosFinanciacion.actualizarDocGarantiaAnuRC(con,Utilidades.convertirAEntero(aux[0]),aux[1],Utilidades.convertirAEntero(aux[3]),aux[2]);
										logger.info("se reslizo la modificacion ");
									}
									// Fin Anexo 762
									//****************************************************************************
								}	
								paciente=true;
								
							} 
							if(!convenios&&Integer.parseInt(forma.getConceptosRC("tipoconceptorc_"+i)+"")==ConstantesBD.codigoTipoIngresoTesoreriaConvenios)
							{
								String[] codigoPagosEmpresa=mundo.getPagosEmpresaReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion());
								for(int j=0;j<codigoPagosEmpresa.length;j++)
								{
									logger.info("\n\n OBTENER ESTADOS PAGOS EMPRESA ");
									String[] estado=(Utilidades.obtenerEstadoPagosEmpresa(con,Integer.parseInt(codigoPagosEmpresa[j]))).split(ConstantesBD.separadorSplit);
									
									if(Integer.parseInt(estado[0])!=ConstantesBD.codigoEstadoPagosRecaudado)
									{
										ActionErrors error=new ActionErrors();
										error.add("Recibo Caja con pagos en otros estados", new ActionMessage("error.recibosCaja.pagosAnuladosYRecaudados"));
										saveErrors(request,error);
										UtilidadBD.abortarTransaccion(con);
										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("paginaPrincipal");
									}
									enTransaccion=Utilidades.cambiarEstadoPagosGeneralEmpresa(con,Integer.parseInt(codigoPagosEmpresa[j]),ConstantesBD.codigoEstadoPagosAnulado);
									convenios=false;
								}
								
							} 
							
							if(!abonos&&Integer.parseInt(forma.getConceptosRC("tipoconceptorc_"+i)+"")==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
							{
								String[] codigoAbonos=mundo.getAbonosReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion());
								enTransaccion=mundo.generarAbonos(con,codigoAbonos);
								double valor=0;
								// FIXME Arreglar esto para que utilice el método centralizado
								String[] codigoTodosAbonos=mundo.getTodosAbonos(con,Utilidades.pacienteAbonoReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion()), mundo.getIngresoReciboCaja());
								for(int j=0;j<codigoTodosAbonos.length;j++)
								{
									String[] abono=mundo.consultarValorOperacionAbono(con,Integer.parseInt(codigoTodosAbonos[j])).split(ConstantesBD.separadorSplit);
									if(abono[1].equals("resta"))
									{
										valor=valor-Double.parseDouble(abono[0]);
									}
									else if(abono[1].equals("suma"))
									{
										valor=valor+Double.parseDouble(abono[0]);
									}
								}
								if(valor<0)
								{
									ActionErrors error=new ActionErrors();
									error.add("Saldo menor a cero", new ActionMessage("error.recibosCaja.saldoAbonosMenorACero"));
									saveErrors(request,error);
									UtilidadBD.abortarTransaccion(con);
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("paginaPrincipal");
								}
								/*
								 * Se valida el saldo por ingreso
								 */
								boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(forma.getInstitucion()));
								if(controlarAbonoPaciente && errores.isEmpty() && mundo.getIngresoReciboCaja()!=null)
								{
									codigoAbonos=mundo.getAbonosReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion());
									enTransaccion=mundo.generarAbonos(con,codigoAbonos);
									valor=0;
									
									codigoTodosAbonos=mundo.getTodosAbonos(con,Utilidades.pacienteAbonoReciboCaja(con,ConstantesBD.codigoTipoDocumentoPagosReciboCaja,forma.getCodigoReciboCaja(),forma.getInstitucion()), mundo.getIngresoReciboCaja());
									for(int j=0;j<codigoTodosAbonos.length;j++)
									{
										
										String[] abono=mundo.consultarValorOperacionAbono(con,Integer.parseInt(codigoTodosAbonos[j])).split(ConstantesBD.separadorSplit);
										if(abono[1].equals("resta"))
										{
											valor=valor-Double.parseDouble(abono[0]);
										}
										else if(abono[1].equals("suma"))
										{
											valor=valor+Double.parseDouble(abono[0]);
										}
									}
									if(valor<0)
									{
										ActionErrors error=new ActionErrors();
										error.add("Saldo menor a cero", new ActionMessage("error.recibosCaja.saldoAbonosMenorACero"));
										saveErrors(request,error);
										UtilidadBD.abortarTransaccion(con);
										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("paginaPrincipal");
									}
								}
								
								abonos=false;
							} 
							// Facturas varias
							//-------------------------------------
							if(!abonos&&Integer.parseInt(forma.getConceptosRC("tipoconceptorc_"+i)+"")==ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)
							{
								
								if(forma.getConceptosRC("valortipoconceptorc_"+i).toString().equals(ConstantesIntegridadDominio.acronimoFactura))
								{
									int resultado = 0;
									resultado = mundo.anularReciboCajaFacturasVarias(con, forma.getCodigoReciboCaja(), usuario);
									if(resultado==1)
										enTransaccion = true;
									else if(resultado==0)
										enTransaccion = false;
									else if(resultado==ConstantesBD.codigoNuncaValido)
									{
										ActionErrors error=new ActionErrors();
										error.add("Saldo menor a cero", new ActionMessage("error.recibosCaja.saldoAbonosMenorACero"));
										saveErrors(request,error);
										UtilidadBD.abortarTransaccion(con);
										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("paginaPrincipal");
									}
									
								}
							}
							//-------------------------------------
						}
					}
				}
				
				if(enTransaccion)
				{
					UtilidadBD.finalizarTransaccion(con);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					logger.warn("Transaccion Abortada ");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				
				
				errores.add("", new ActionMessage("error.recibosCaja.NopuedeAular2",forma.getNumeroReciboCaja()," estar registrado en aplicación de pagos de facturas varias"));
				saveErrors(request,errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");
     	
			}
		}
		else
		{
			errores.add("no tiene turno abierto", new ActionMessage("error.recibosCaja.turnoCaja"));
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");
		}
		
    }


    /**
     * Metodo que realiza la consulta de un recibo de caja y lo cargar en la forma.
     * @param con
     * @param forma
     * @param mundo
     */
    private ActionForward accionCargarReciboCaja(	Connection con, AnulacionRecibosCajaForm forma, 
    										AnulacionRecibosCaja mundo, UsuarioBasico usuario, 
    										ActionMapping mapping, HttpServletRequest request)
    {
        mundo.cargarReciboCaja(con,forma.getCodigoReciboCaja(),forma.getInstitucion());
        forma.setExisteReciboCajaXConceptAnticipConvenioOdonto(false);
             
        if(mundo.getCodigoCentroAtencion()!=ConstantesBD.codigoNuncaValido)
        {	
	        if(mundo.getCodigoCentroAtencion()!=usuario.getCodigoCentroAtencion())
	        {
	        	
	        	ActionErrors error=new ActionErrors();
				error.add("", new ActionMessage("error.recibosCaja.noPuedeAnular", forma.getNumeroReciboCaja(), mundo.getNombreCentroAtencion()));
				saveErrors(request,error);
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");
	        }
        }    
        
        if(mundo.getEliminarRC().equals(ConstantesBD.acronimoNo))
        {
        	ActionErrors error=new ActionErrors();
			error.add("", new ActionMessage("errors.notEspecific", "No se puede anular el recibo de caja "+forma.getNumeroReciboCaja()+" por que existen aplicaciones de pagos. "));
			saveErrors(request,error);
			forma.reset();
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
        }
        
        if(ApliPagosCarteraPaciente.existeApliPagosReciboCaja(con, forma.getCodigoReciboCaja()))
        {
        	ActionErrors error=new ActionErrors();
			error.add("", new ActionMessage("errors.notEspecific", "El recibo de caja "+forma.getNumeroReciboCaja()+" tiene aplicación de pagos. "));
			saveErrors(request,error);
			forma.reset();
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
        }
        
        int numeroContrato= mundo.esReciboCajaXConceptAnticipConvenioOdonto(con, mundo.getNumeroReciboCaja());
        
        if(numeroContrato > 0)
         {
        	logger.info("ENTRO a VALIDAR  ReciboCajaXConceptAnticipConvenioOdonto ");
        	logger.info("CONTRATO: "+numeroContrato);
            if(!mundo.validarValorReciboCajaVsValorAnticipoDispContr(con, numeroContrato, mundo.getTotalRecibidoCaja()+"")) {
				ActionErrors error=new ActionErrors();
				error.add("", new ActionMessage("errors.notEspecific", "No se puede anular el recibo de caja "+forma.getNumeroReciboCaja()+" por que No tiene Saldo Disponible. "));
				saveErrors(request,error);
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");
            } else {           
            	forma.setExisteReciboCajaXConceptAnticipConvenioOdonto(true);           	
            	forma.setNumContratoOdontologia(numeroContrato);
            }
        }       
        forma.setFechaReciboCaja(mundo.getFechaReciboCaja());
        forma.setHoraReciboCaja(mundo.getHoraReciboCaja());
        forma.setConsecutivoCaja(mundo.getConsecutivoCaja());
        forma.setCodigoCaja(mundo.getCodigoCaja());
        forma.setDescripcionCaja(mundo.getDescripcionCaja());
        forma.setLoginUsuarioCaja(mundo.getLoginUsuarioCaja());
        forma.setNombreUsuarioCaja(mundo.getNombreUsuarioCaja());
        forma.setRecibidoDe(mundo.getRecibidoDe());
        forma.setCodigoCentroAtencion(mundo.getCodigoCentroAtencion());
        forma.setNombreCentroAtencion(mundo.getNombreCentroAtencion());
        
        forma.setConceptosRC((HashMap)mundo.getConceptosRC().clone());
        forma.setTotalRecibidoCaja(mundo.getTotalRecibidoCaja());
        forma.setInformacionCargada(true);
        forma.setEliminarRC(mundo.getEliminarRC());
        
        UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
    }
    
    
    /**
     * Metodo para generar los reportes
     * @param tipoReporte
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
	@SuppressWarnings("unchecked")
	private void generarReporte(Connection con, 
								UsuarioBasico usuario,
								HttpServletRequest request) 
    {
		try{
			HibernateUtil.beginTransaction();
	        DesignEngineApi comp;  
	        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	        
	        //ENCABEZADO------------------------
	        Vector v=new Vector();
	        v.add(ins.getRazonSocial());
	        if (ins.getTipoIdentificacion().equals("NI")) {
	        	v.add("NIT: "+ins.getNit());
	        } else {
	        	v.add(ins.getTipoIdentificacion()+": "+ins.getNit());
	        }  
	        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
	            v.add("Actividad Económica: "+ins.getActividadEconomica());
	        CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
			CentroAtencion centroAtencion = new CentroAtencion();
			centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());
	        //v.add(ins.getDireccion()+"              "+ins.getTelefono());
			v.add(centroAtencion.getCodigo().trim()+ " - "+centroAtencion.getDescripcion());
			v.add(centroAtencion.getDireccion()+(centroAtencion.getTelefono()!=null?" Tels: "+centroAtencion.getTelefono():""));
	        //-----------------------------------
	        
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/","ConsultaAnulacionReciboCajaFormatoResumen.rptdesign");
	        comp.obtenerComponentesDataSet("listadoAnulacionRC");
			
			if (ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) 
			{
				comp.insertImageHeaderOfMasterPageCenter(0, 2, ins.getLogoReportes());
			} else if (ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
				comp.insertImageHeaderOfMasterPageCenter(0, 0,ins.getLogoReportes());
			}
			
			comp.insertGridHeaderOfMasterPage(0,1,1,5);
		        
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        for(int t = 0 ; t < v.size() ; t++)
			{
	        	comp.fontWeightCellGridHeaderOfMasterPage(0,1,t,0,DesignChoiceConstants.FONT_WEIGHT_BOLD);
			}
		
			UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
			comp.insertLabelInGridPpalOfFooter(0, 1, usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
					);
	        
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	      	comp.lowerAliasDataSet();
			String newPathReport = comp.saveReport1(false);
	        if(!newPathReport.equals(""))
	        {
	            request.setAttribute("isOpenReport", "true");
	            request.setAttribute("newPathReport", newPathReport);
	        }            
	//          por ultimo se modifica la conexion a BD
	        comp.updateJDBCParameters(newPathReport);
	        HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR generarReporte",e);
			HibernateUtil.abortTransaction();
		}
    }
	
	 
	
	private ActionErrors verificarValorAnticipConveniosOdonto()
	{
		ActionErrors errores = new ActionErrors();
		
		
		
		return errores;
		
	}
	 
	
	
	
}
