package com.princetonsa.action.interfaz;

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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.interfaz.DesmarcarDocProcesadosForm;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.DesmarcarDocProcesados;
import com.princetonsa.mundo.interfaz.ParamInterfazSistema1E;


public class DesmarcarDocProcesadosAction extends Action{

	private Logger logger = Logger.getLogger(DesmarcarDocProcesadosAction.class);
	DesmarcarDocProcesados mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {

		Connection con = null;
		try{

			if(response == null);

			if (form instanceof DesmarcarDocProcesadosForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo = new DesmarcarDocProcesados();

				DesmarcarDocProcesadosForm forma = (DesmarcarDocProcesadosForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();
					ActionForward forward = new ActionForward();
					forward=validarParametros(con, forma,request, mapping);
					UtilidadBD.closeConnection(con);
					if(forward != null)
						return forward;	  
					UtilidadBD.closeConnection(con);
					forma.setParametrosFiltros("operacionExitosa","");	
					return listarTiposMovientosXDesmarcar(forma, mapping);				
				}
				else if(estado.equals("guardar"))
				{	
					forma.setParametrosFiltros("operacionExitosa","");
					return desmarcarDocumentos(con,forma,usuario,mapping,request);
				}
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
	 * Metodo para realizar el proceso de desmarcacion de los Documentos
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward desmarcarDocumentos(Connection con, DesmarcarDocProcesadosForm forma, UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) {
		
		  
		  HashMap resultado = new HashMap();
		  ActionErrors errores = new ActionErrors();
		  forma.getParametrosBusqueda().put("fechaControl",forma.getFechaControl());
		  forma.getParametrosBusqueda().put("motivoDesmarcacion",forma.getMotivoDesmaracacion());
		  forma.getParametrosBusqueda().put("contDesmarcar", forma.getContDocsaDesMarcar());
		  errores = mundo.validacionBusqueda(forma.getParametrosBusqueda());
		  boolean bandera = false, banderaLogVentas= false, banderaLogRecuados= false, banderaLogAjustes= false,banderaLogServicios = false;
		  int codLogVentas=0,codLogRecaudos=0, codLogAjustes=0, codLogServicios=0;
		  
		  if(errores.isEmpty())
			{
			  UtilidadBD.iniciarTransaccion(con);
				
			 // Desmarcacion Movimientos Ventas y Honorarios
			   
				  for(int i=0; i<forma.getMvtosVentas().size();i++)
					{
					   //Facturas Pacientes
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteFacturaPaciente && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{
							resultado = mundo.desmarcarFacturasPacientes(con, forma.getParametrosBusqueda());							
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							  if(resultado.containsKey("actualizo") &&
										resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {
								  
								if(!banderaLogVentas)
								{
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteFacturaPaciente );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
									 {
										banderaLogVentas= true;
										codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteFacturaPaciente+"" );
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										{
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										}
									 }
								}
								else
								{
									resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteFacturaPaciente+"" );
									if(resultado.containsKey("codLogDoc") &&
											resultado.get("codLogDoc").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
								}
							  }
							
							}
						
						}
					
						//Anulacion Facturas Pacientes
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{
							resultado=mundo.desmarcarAnulacionFacturasPacientes(con, forma.getParametrosBusqueda());	
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{
							 if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {	
								if(!banderaLogVentas)
								{	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
										if(resultado.containsKey("codLog") &&
												resultado.get("codLog").toString().equals(""))
										{
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										}
										else
										{
											banderaLogVentas= true;
											codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
											resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+"" );
											if(resultado.containsKey("codLogDoc") &&
													resultado.get("codLogDoc").toString().equals(""))
											{
												bandera = true;
												saveErrors(request,(ActionErrors)resultado.get("error"));
											}
										}
								  }
								  else
									 {
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente+"" );
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										{
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										}
									}
							    }	
							}
						}
					    //Facturas Varias
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteFacturasVarias && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarFacturasVarias(con, forma.getParametrosBusqueda(), false);
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {	
								if(!banderaLogVentas)
								{
								
								DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
								dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteFacturasVarias );
								resultado = mundo.guardarLogInterfaz1E(con, dto);
								
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
									{
										banderaLogVentas= true;
										codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteFacturasVarias+"" );
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										{
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										}
									}
							      }
								  else
							       {
									  resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteFacturasVarias+"" );
									  
									   if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }
							        }
							    }	
							}
						}
						
						//Anulacion Factura Varias
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteAnulaFacturasVarias && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{
							resultado=mundo.desmarcarFacturasVarias(con, forma.getParametrosBusqueda(), true);
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{ 
							 if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							  {	
								if(!banderaLogVentas)
								  {	
								  DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
								  dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteAnulaFacturasVarias );
								  resultado = mundo.guardarLogInterfaz1E(con, dto);
								
									   if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									    {
										 bandera = true;
										 saveErrors(request,(ActionErrors)resultado.get("error"));
									    }
									     else
									     {
										    banderaLogVentas= true;
											codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
											resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAnulaFacturasVarias+"" );
											
											if(resultado.containsKey("codLogDoc") &&
													resultado.get("codLogDoc").toString().equals(""))
											 {
												bandera = true;
												saveErrors(request,(ActionErrors)resultado.get("error"));
											 }   
									     }
									  }
									  else
									  {
										  resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteAnulaFacturasVarias+"" );
										  
										   if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
											 {
												bandera = true;
												saveErrors(request,(ActionErrors)resultado.get("error"));
											 }  
									  }
								  }
							  }
						 }
						
						//Ajustes Facturas Varias
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteAjusFacturasVarias && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAjustesFacturasVarias(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							 if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							  {	
								if(!banderaLogVentas)
								  {		 
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteAjusFacturasVarias );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									} 
									else
								     {
									    banderaLogVentas= true;
										codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAjusFacturasVarias+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								  }
								else
								{
									resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteAjusFacturasVarias+"" );									  
									
									if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }  
								}
							  }
						  }
						}
						
						//Cuentas de Cobro Capitacion
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteCCCapitacion && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarCuentasCobroCapitacion(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							  {	
								if(!banderaLogVentas)
								  {	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteCCCapitacion );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogVentas= true;
										codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteCCCapitacion+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								  }
								else
								{
                                 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteCCCapitacion+"" );									  
									
									if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  } 
								}
							  }
							}
						}
						
						//Ajustes Cuentas Cobro Capitacion
						if(Utilidades.convertirAEntero(forma.getMvtosVentas().get(i).getCodigo())==ConstantesBD.codigoTipoDocInteAjustesCCCapitacion && forma.getMvtosVentas().get(i).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAjustesCuentasCobroCapitacion(con, forma.getParametrosBusqueda());		
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}		
						   else
						    {
						   if(resultado.containsKey("actualizo") && resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							  {	
							  if(!banderaLogVentas)
								{   
								 DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
								 dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte, ConstantesBD.codigoTipoDocInteAjustesCCCapitacion );
								 resultado = mundo.guardarLogInterfaz1E(con, dto);
								 
								 if(resultado.containsKey("codLog") &&
									resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
								 else
							     {
								    banderaLogVentas= true;
									codLogVentas=Utilidades.convertirAEntero(resultado.get("codLog").toString());
									resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAjustesCCCapitacion+"" );
									
									if(resultado.containsKey("codLogDoc") &&
											resultado.get("codLogDoc").toString().equals(""))
									 {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									 }   
							     }
								}
							   else
							    {
							    resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogVentas, ConstantesBD.codigoTipoDocInteAjustesCCCapitacion+"" );									  
								
								if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
								  {
									bandera = true;
									saveErrors(request,(ActionErrors)resultado.get("error"));
								  } 
							    }
							  }
						   }
					     }
										
						logger.info("\n VENTAS Desmarcado >> "+ forma.getMvtosVentas().get(i).getDesmarcado()+ " Codigo :  "+forma.getMvtosVentas().get(i).getCodigo());
					}
				  
			// Desmarcacion Recuados
				  
				  for(int j=0; j<forma.getMvtosRecaudos().size();j++)
				  {
					  
				     //Recibos de Caja
						if(Utilidades.convertirAEntero(forma.getMvtosRecaudos().get(j).getCodigo())==ConstantesBD.codigoTipoDocInteReciboCaja && forma.getMvtosRecaudos().get(j).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarRecibosdeCaja(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							 if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								if(!banderaLogRecuados)
								{
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovRecaudos, ConstantesBD.codigoTipoDocInteReciboCaja );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
										banderaLogRecuados = true;
										codLogRecaudos=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteReciboCaja+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								}
								else
								{
								 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogRecaudos, ConstantesBD.codigoTipoDocInteReciboCaja+"" );									  
								
								 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
								  {
									bandera = true;
									saveErrors(request,(ActionErrors)resultado.get("error"));
								  }
								}
							   }
							}
						}
						
						//Anulacion Recibos
						if(Utilidades.convertirAEntero(forma.getMvtosRecaudos().get(j).getCodigo())==ConstantesBD.codigoTipoDocInteAnulaRecCaja && forma.getMvtosRecaudos().get(j).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAnulacionRecibosdeCaja(con, forma.getParametrosBusqueda());	
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogRecuados)
								 { 	
								  DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
								  dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovRecaudos, ConstantesBD.codigoTipoDocInteAnulaRecCaja );
								  resultado = mundo.guardarLogInterfaz1E(con, dto);
								
								  if(resultado.containsKey("codLog") &&
										resultado.get("codLog").toString().equals(""))
								   {
									 bandera = true;
									 saveErrors(request,(ActionErrors)resultado.get("error"));
								   }
								  else
								     {
										banderaLogRecuados = true;
										codLogRecaudos=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAnulaRecCaja+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								}
								else
								{
									resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogRecaudos, ConstantesBD.codigoTipoDocInteAnulaRecCaja+"" );									  
									
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }	
								}
							   }
							}
							
						}
						
						// Devolucion Recibos
						if(Utilidades.convertirAEntero(forma.getMvtosRecaudos().get(j).getCodigo())==ConstantesBD.codigoTipoDocInteDevolRecibosCaja && forma.getMvtosRecaudos().get(j).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDevolucionRecibosdeCaja(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							 if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								if(!banderaLogRecuados)
								{
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovRecaudos, ConstantesBD.codigoTipoDocInteDevolRecibosCaja );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
										banderaLogRecuados = true;
										codLogRecaudos=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDevolRecibosCaja+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								}
								else
								{
								 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogRecaudos, ConstantesBD.codigoTipoDocInteDevolRecibosCaja+"" );									  
								
								 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
								  {
									bandera = true;
									saveErrors(request,(ActionErrors)resultado.get("error"));
								  }
								}
							   }
							}
						}
						
						
						
						
						logger.info("\n RECAUDOS Desmarcado >> "+ forma.getMvtosRecaudos().get(j).getDesmarcado()+ " Codigo :  "+forma.getMvtosRecaudos().get(j).getCodigo());
					
				  }
				  
			//Desmarcacion Ajustes Reclasificaciones
			     
				  for(int k=0; k<forma.getMvtosAjustes().size();k++)
				  {
				    //Ajustes Facturas Pacientes	  
					  if(Utilidades.convertirAEntero(forma.getMvtosAjustes().get(k).getCodigo())==ConstantesBD.codigoTipoDocInteAjustesFactPaciente && forma.getMvtosAjustes().get(k).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAjustesFacuturasPacientes(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{ 
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogAjustes)
								 { 	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi, ConstantesBD.codigoTipoDocInteAjustesFactPaciente );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
										banderaLogAjustes = true;
										codLogAjustes=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAjustesFactPaciente+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogAjustes, ConstantesBD.codigoTipoDocInteAjustesFactPaciente+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }	
								 }
							   }
							}
						}
                     // Registro Glosas
					  if(Utilidades.convertirAEntero(forma.getMvtosAjustes().get(k).getCodigo())==ConstantesBD.codigoTipoDocInteRegistroGlosas && forma.getMvtosAjustes().get(k).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAjustesFacuturasPacientes(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{ 
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogAjustes)
								 { 	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi, ConstantesBD.codigoTipoDocInteRegistroGlosas );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
										banderaLogAjustes = true;
										codLogAjustes=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteRegistroGlosas+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogAjustes, ConstantesBD.codigoTipoDocInteRegistroGlosas+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }	
								 }
							   }
							}
						}
					  
					  logger.info("\n AJUSTES Desmarcado >> "+ forma.getMvtosAjustes().get(k).getDesmarcado()+ " Codigo :  "+forma.getMvtosAjustes().get(k).getCodigo());  
				  }
				  
		 // Desmarcacion Servicios Entidades Externas		 
				  
				  for(int l=0; l<forma.getMvtosServicios().size();l++)
				  {
					  logger.info("tipo de movimient >> "+forma.getMvtosServicios().get(l).getCodigo());
					 //Registro Autorizacion Servicios Entidades Subcontratadas 
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteAutoServicioEntSub && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
						  logger.info("entro");
							resultado=mundo.desmarcarAutorServEntidadesSub(con, forma.getParametrosBusqueda(),false);	
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 { 
								  DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
								  dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteAutoServicioEntSub );
								  resultado = mundo.guardarLogInterfaz1E(con, dto);
								
								   if(resultado.containsKey("codLog") &&
										resultado.get("codLog").toString().equals(""))
								    {
									 bandera = true;
									 saveErrors(request,(ActionErrors)resultado.get("error"));
								    }
								   else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAutoServicioEntSub+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteAutoServicioEntSub+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }
								 }
							   }
							}
						}
					  
					  //Anulacion Autorizacion Servicios Entidades Subcontratadas
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAutorServEntidadesSub(con, forma.getParametrosBusqueda(),true);
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }
								 }
							   }
							}
						}
					  
					  //Despacho de Medicamentos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDespachoMed && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDespachoMedicamentos(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{   
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDespachoMed );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDespachoMed+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDespachoMed+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  } 
								 }
							   }
							}
						}
					  
					  //Devolucion de Medicamentos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDevolucionMedi && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDevolucionMedicamentos(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDevolucionMedi );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDevolucionMedi+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDevolucionMedi+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }  
								 }
							   }
							
							}
						}
					  
					  //Despacho Pedidos Insumos					  
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDespaPedidoInsumo && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDespachoPedidos(con, forma.getParametrosBusqueda(), false);
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {	
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDespaPedidoInsumo );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDespaPedidoInsumo+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDespaPedidoInsumo+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }   
								 }
							   }
							}
						}
					  
					  //Devolucion Pedidos Insumos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDevolPedidoInsumo && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDevolucionPedidos(con, forma.getParametrosBusqueda(), false);	
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDevolPedidoInsumo );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDevolPedidoInsumo+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }   
								 }
							   }
							}
						}					  
					  
					  //Despacho Pedidos Quirurgicos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDespachoPedidoQx && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDespachoPedidos(con, forma.getParametrosBusqueda(), true);	
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{ 
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDespachoPedidoQx );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDespachoPedidoQx+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDespachoPedidoQx+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }  
								 }
							   }
							}
						}
					  
					  //Devolucion pedidos Quirurgicos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteDevolucionPedidoQx && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarDevolucionPedidos(con, forma.getParametrosBusqueda(), true);
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{ 
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteDevolucionPedidoQx );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteDevolucionPedidoQx+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }  
								 }
							   }
							}
						}
					  
					  //Cargos Directos Articulos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteCargosDirectosArt && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarCargosDirectosArticulos(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteCargosDirectosArt );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteCargosDirectosArt+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteCargosDirectosArt+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  }  
								 }
							   }
							
							}
						}
					  
					  //Anulacion Cargos Articulos
					  if(Utilidades.convertirAEntero(forma.getMvtosServicios().get(l).getCodigo())==ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo && forma.getMvtosServicios().get(l).getDesmarcado().equals(ValoresPorDefecto.getValorTrueCortoParaConsultas()))
						{							
							resultado=mundo.desmarcarAnulacionCargosArticulos(con, forma.getParametrosBusqueda());
							if(resultado.containsKey("estadoCont") &&
									resultado.get("estadoCont").toString().equals(""))
							{
								bandera = true;
								saveErrors(request,(ActionErrors)resultado.get("error"));
							}
							else
							{  
							if(resultado.containsKey("actualizo") &&resultado.get("actualizo").toString().equals(ConstantesBD.acronimoSi))
							   {								  
								 if(!banderaLogServicios)
								 {		
									DtoLogInterfaz1E dto=new DtoLogInterfaz1E();
									dto = llenarDtoLogInterfaz1E(forma, usuario, ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter, ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo );
									resultado = mundo.guardarLogInterfaz1E(con, dto);
									
									if(resultado.containsKey("codLog") &&
											resultado.get("codLog").toString().equals(""))
									{
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									}
									else
								     {
									    banderaLogServicios = true;
									    codLogServicios=Utilidades.convertirAEntero(resultado.get("codLog").toString());
										resultado = mundo.guardarLogInterfazTiposDoc1E(con, Utilidades.convertirAEntero(resultado.get("codLog").toString()), ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+"" );
										
										if(resultado.containsKey("codLogDoc") &&
												resultado.get("codLogDoc").toString().equals(""))
										 {
											bandera = true;
											saveErrors(request,(ActionErrors)resultado.get("error"));
										 }   
								     }
								 }
								 else
								 {
									 resultado = mundo.guardarLogInterfazTiposDoc1E(con, codLogServicios, ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo+"" );									  
										
									 if(resultado.containsKey("codLogDoc") && resultado.get("codLogDoc").toString().equals(""))
									  {
										bandera = true;
										saveErrors(request,(ActionErrors)resultado.get("error"));
									  } 
								 }
							   }
							}
						}
					  
					  
					  logger.info("\n Servicios Desmarcado >> "+ forma.getMvtosServicios().get(l).getDesmarcado()+ " Codigo :  "+forma.getMvtosServicios().get(l).getCodigo());
				  }
				
			    if(!bandera)
			     {
				  forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
		      	  forma.setParametrosFiltros("mensaje","Proceso de Desmarcación Exitoso"); 
		      	  UtilidadBD.finalizarTransaccion(con);
		          UtilidadBD.closeConnection(con); 
			      }else
			       {  
			    	   UtilidadBD.abortarTransaccion(con);
			    	   forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
			    	   forma.setParametrosFiltros("mensaje","NO se pudo realizar el Proceso de Desmarcación ");
				       UtilidadBD.closeConnection(con); 
				  
			       }
			  
			  
				return mapping.findForward("principal");	  
			}
		    else
			 {		    	
		    	UtilidadBD.closeConnection(con);
				saveErrors(request, errores);
				return mapping.findForward("principal");
			 }
		
		
	}


	/**
	 * Metodo para validar los parametros ( Fecha de Control para Desmarcar Docuementos
	 * @param con
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward validarParametros(Connection con, DesmarcarDocProcesadosForm forma,HttpServletRequest request, ActionMapping mapping)
	{
		 DtoInterfazParamContaS1E dto = new DtoInterfazParamContaS1E();
		 dto=ParamInterfazSistema1E.consultarParamGenerales(con);
		 
		
		 if(dto.getFechaControlDesmarcar().equals(""))
		 {
			 return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Falta definir parámetro Fecha Control para desmarcar Documentos Procesados ", false);
		  
		  }else
		    {
			  if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), dto.getFechaControlDesmarcar()))
			    {
				 return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "La fecha Control es Mayor a la fecha del Sistema. No puede realizar el proceso de Desmarcación. Por favor Verifique ", false);
			    }else
			    {
			    	forma.setFechaControl(dto.getFechaControlDesmarcar());
			    }
		    }
		 
		 return null;
	}
	
	/**
	 * Metodo para consultar los tipos de Movimientos a desmarcar
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward listarTiposMovientosXDesmarcar(DesmarcarDocProcesadosForm forma, ActionMapping mapping) {
		
		forma.setMvtosAjustes(mundo.consultarDocumentosXtipoMvto(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi)); 
		forma.setMvtosRecaudos(mundo.consultarDocumentosXtipoMvto(ConstantesIntegridadDominio.acronimoTipoMovRecaudos));
		forma.setMvtosServicios(mundo.consultarDocumentosXtipoMvto(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter));
		forma.setMvtosVentas(mundo.consultarDocumentosXtipoMvto(ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte));	
		return mapping.findForward("principal");
	}

  
	/**
	 * Metodo para cargar el DTO necesario para realizar el Insert del LOG Interfaz 1E
	 * @param forma
	 * @param usuario
	 * @param tipoMovto
	 * @param tipoDocumento
	 * @return
	 */
	private DtoLogInterfaz1E llenarDtoLogInterfaz1E(DesmarcarDocProcesadosForm forma,UsuarioBasico usuario, String tipoMovto, int tipoDocumento)
	{
		DtoLogInterfaz1E dtoInterfaz= new DtoLogInterfaz1E();
		
		dtoInterfaz.setUsuarioProcesa(usuario.getLoginUsuario());
		dtoInterfaz.setTipoMovimiento(tipoMovto);
		dtoInterfaz.setTipoDocumento(tipoDocumento+"");
		dtoInterfaz.setTipoProceso(ConstantesIntegridadDominio.acronimoTipoProcesoDesmarcar);
		dtoInterfaz.setInstitucion(usuario.getCodigoInstitucionInt()+"");
		dtoInterfaz.setFechaInicioDesmarcacion(UtilidadFecha.conversionFormatoFechaABD(forma.getParametrosBusqueda().get("fechaInicialDesmaracion").toString()));
		dtoInterfaz.setFechaFinalDesmaracion(UtilidadFecha.conversionFormatoFechaABD(forma.getParametrosBusqueda().get("fechaFinalDesmarcacion").toString()));
		dtoInterfaz.setMotivoDesmarcacion(forma.getMotivoDesmaracacion());
		
		
		return dtoInterfaz;
	}
	
	


	
	
	
}
