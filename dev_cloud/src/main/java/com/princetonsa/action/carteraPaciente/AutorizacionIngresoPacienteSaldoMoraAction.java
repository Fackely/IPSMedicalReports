package com.princetonsa.action.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.carteraPaciente.AutorizacionIngresoPacienteSaldoMoraForm;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.AutorizacionIngresoPacienteSaldoMora;

public class AutorizacionIngresoPacienteSaldoMoraAction extends Action
{
	Logger logger = Logger.getLogger(ExtractoDeudoresCPAction.class);
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			{
		Connection con = null;
		try{
			if(response == null);
			
			if (form instanceof AutorizacionIngresoPacienteSaldoMoraForm) 
			{
				
				con = UtilidadBD.abrirConexion();
				
				if(con == null)
				{
				request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				return mapping.findForward("paginaError");
				}
				
				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				
				//ActionErrors
				ActionErrors errores = new ActionErrors();
				
				//Paciente
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				
				
				AutorizacionIngresoPacienteSaldoMoraForm forma = (AutorizacionIngresoPacienteSaldoMoraForm)form;		
				String estado = forma.getEstado();	
				
				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");
				
				if (estado.equals("empezar"))
				{
					return accionEmpezar(forma, usuario, request, mapping, paciente);
				}
				if (estado.equals("buscarCuotasDocumentos"))
				{
					return buscarCuotasDocumentos(forma, usuario, request, mapping, paciente);
				}
				if (estado.equals("buscarTipoPaciente"))
				{
					return buscarTipoPaciente(forma, usuario, request, mapping, paciente);
				}
				if (estado.equals("grabarAutorizacion"))
				{
					return grabarAutorizacion(forma, usuario, request, mapping, paciente,errores);
				}
				if (estado.equals("consultar"))
				{
					return consultarAutorizacion(forma, usuario, request, mapping, paciente,errores);
				}
				if (estado.equals("buscarTipoPacienteConsulta"))
				{
					return buscarTipoPacienteConsulta(forma, usuario, request, mapping, paciente);
				}
				
				if (estado.equals("consultaAutorizaciones"))
				{
					return consultaAutorizaciones(forma, usuario, request, mapping, paciente,errores);
				}
				
				if (estado.equals("detalleAutorizacion"))
				{
					return detalleAutorizacion(forma, usuario, request, mapping, paciente,errores);
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
		
		private ActionForward detalleAutorizacion(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente, ActionErrors errores) 
		{
			forma.setListadoDocs(AutorizacionIngresoPacienteSaldoMora.consultaDocsPacienteDeudor(forma.getListadoBusqueda().get(forma.getIndiceDocs()).getCodigoPaciente()));
			return mapping.findForward("detalleAutorizacion");
		}
	
	
		private ActionForward consultaAutorizaciones(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente, ActionErrors errores) 
		{
			if (!forma.getDtoAutorizacion().getFecha().equals("")&&!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoAutorizacion().getFecha()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+forma.getDtoAutorizacion().getFecha() ));
				saveErrors(request, errores);
			}
				
			if (!forma.getDtoAutorizacion().getFechaFinal().equals("")&&!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoAutorizacion().getFechaFinal()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+forma.getDtoAutorizacion().getFechaFinal()));
				saveErrors(request, errores);
			}
			
			if (!forma.getDtoAutorizacion().getFecha().equals("")&&forma.getDtoAutorizacion().getFechaFinal().equals(""))
			{
				errores.add("",new ActionMessage("errors.required", "La fecha final "));
    			saveErrors(request, errores);
			}
			
			if (forma.getDtoAutorizacion().getFecha().equals("")&&!forma.getDtoAutorizacion().getFechaFinal().equals(""))
			{
				errores.add("",new ActionMessage("errors.required", "La fecha inicial "));
    			saveErrors(request, errores);
			}
			
			
			if ((!forma.getDtoAutorizacion().getFecha().equals("")&&UtilidadFecha.esFechaValidaSegunAp(forma.getDtoAutorizacion().getFecha()))
					&&!forma.getDtoAutorizacion().getFechaFinal().equals("")&&UtilidadFecha.esFechaValidaSegunAp(forma.getDtoAutorizacion().getFechaFinal()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getDtoAutorizacion().getFecha(), forma.getDtoAutorizacion().getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final "+forma.getDtoAutorizacion().getFechaFinal(),"Inicial  "+forma.getDtoAutorizacion().getFecha()));
					saveErrors(request, errores);
				}
				else
				{
					if (UtilidadFecha.numeroDiasEntreFechas(forma.getDtoAutorizacion().getFecha(), forma.getDtoAutorizacion().getFechaFinal())>180)
					{
						errores.add("", new ActionMessage("errors.notEspecific", "Las fechas no pueden tener una diferencia mayor a 180 días."));
						saveErrors(request, errores);
					}
				}
			}
			
			if (forma.getDtoAutorizacion().getFechaFinal().equals("")&&forma.getDtoAutorizacion().getFecha().equals("")&&forma.getDtoAutorizacion().getViaIngreso()==ConstantesBD.codigoNuncaValido
					&&forma.getDtoAutorizacion().getTiposPaciente().equals("")&&forma.getDtoAutorizacion().getPersonaAutoriza().equals("")
						&&forma.getDtoAutorizacion().getTipoId().equals("")&&forma.getDtoAutorizacion().getIdPaciente().equals(""))
			{
    			errores.add("",new ActionMessage("errors.required", "Al menos un parámetro de búsqueda "));
    			saveErrors(request, errores);
			}
			
			if (errores.isEmpty())
			{
				forma.setListadoBusqueda(AutorizacionIngresoPacienteSaldoMora.consultaAutorizaciones(forma.getDtoAutorizacion()));
				forma.setNumRegistros(forma.getListadoBusqueda().size());
				if (forma.getNumRegistros()==0)	
				{
					forma.setMensaje("No hay resultados para la búsqueda.");
					return mapping.findForward("consultarAutorizacion");
				}
				else
					return mapping.findForward("resultadoBusqueda");
			}
			else
				return mapping.findForward("consultarAutorizacion");
		}
		
	
	
		private ActionForward buscarTipoPacienteConsulta(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente) 
		{
			Connection con =UtilidadBD.abrirConexion();
			forma.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getDtoAutorizacion().getViaIngreso()+""));
			//Se cierra al conexión para las utilidades que la usan
			try 
			{
				UtilidadBD.cerrarConexion(con);
			} 
			catch (Exception e) 
			{
				logger.info("ERROR----->"+e);
			}
			return mapping.findForward("consultarAutorizacion");
		}
	
		
		
		private ActionForward consultarAutorizacion(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente, ActionErrors errores) 
		{
			forma.reset(); 
			Connection con =UtilidadBD.abrirConexion();
			forma.setViasIngreso(Utilidades.obtenerViasIngresoBloqueaDeudorBloqueaPaciente(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
			forma.setUsuarios(Utilidades.obtenerUsuarios(con,usuario.getCodigoInstitucionInt(), true));
			forma.setListadoMotivos(AutorizacionIngresoPacienteSaldoMora.consultaMotivosAutSaldoMora());
			forma.setListaTiposId(Utilidades.obtenerTiposIdentificacion(con, "", usuario.getCodigoInstitucionInt()));
			try 
			{
				UtilidadBD.cerrarConexion(con);
			} 
			catch (Exception e) 
			{
				logger.info("ERROR----->"+e);
			}
			
			return mapping.findForward("consultarAutorizacion");
		}
	
	
		
		private ActionForward grabarAutorizacion(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente, ActionErrors errores) 
		{
			forma.setMensaje("");
			
			if (forma.getDtoAutorizacion().getViaIngreso()==ConstantesBD.codigoNuncaValido)
			{
    			errores.add("",new ActionMessage("errors.required", "La Via de Ingreso "));
    			saveErrors(request, errores);
			}
    		if (forma.getDtoAutorizacion().getTiposPaciente().equals(""))
    		{
    			errores.add("",new ActionMessage("errors.required", "El Tipo de Paciente "));
    			saveErrors(request, errores);
    		}
    		if (forma.getDtoAutorizacion().getPersonaAutoriza().equals(""))
    		{
    			errores.add("",new ActionMessage("errors.required", "La Persona que Autoriza "));
    			saveErrors(request, errores);
    		}
    		if (forma.getDtoAutorizacion().getHorasVigencia().equals(""))
    		{
    			errores.add("",new ActionMessage("errors.required", "Las Horas de Vigencia de Autorización "));
    			saveErrors(request, errores);
    		}
    		if (forma.getDtoAutorizacion().getMotivo()==ConstantesBD.codigoNuncaValido)
    		{
    			errores.add("",new ActionMessage("errors.required", "El Código de Autorización "));
    			saveErrors(request, errores);
    		}
    		if (forma.getDtoAutorizacion().getObservaciones().equals(""))
    		{
    			errores.add("",new ActionMessage("errors.required", "Las Observaciones "));
    			saveErrors(request, errores);
    		}
    		
    		
    		if (errores.isEmpty())
    		{
    			//Se añade para la bsuqueda
    			forma.getDtoAutorizacion().setCodigoPaciente(forma.getListadoDocs().get(0).getCodigoPaciente());
    			
				forma.setListadoExistente(AutorizacionIngresoPacienteSaldoMora.existeAutorizacionConViaIngreso(forma.getDtoAutorizacion()));
				
				if (forma.getListadoExistente().size()==0)
				{
				
					//Se ingresan los datos del usuario que modifica y del paciente cargado en sesión
					forma.getDtoAutorizacion().setCodigoPaciente(forma.getListadoDocs().get(0).getCodigoPaciente());
					forma.getDtoAutorizacion().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					forma.getDtoAutorizacion().setHoraModifica(UtilidadFecha.getHoraActual());
					forma.getDtoAutorizacion().setUsuarioModifica(usuario.getLoginUsuario());
					forma.getDtoAutorizacion().setIngreso(forma.getListadoDocs().get(0).getIngreso());
					forma.getDtoAutorizacion().setFechaIngreso(UtilidadFecha.conversionFormatoFechaABD(forma.getListadoDocs().get(0).getFechaIngreso()));
					forma.getDtoAutorizacion().setHoraIngreso(forma.getListadoDocs().get(0).getHoraIngreso());
					forma.getDtoAutorizacion().setInstitucion(usuario.getCodigoInstitucionInt());
					forma.getDtoAutorizacion().setCentroAtencion(usuario.getCodigoCentroAtencion());
					
					double nroAutorizacion=AutorizacionIngresoPacienteSaldoMora.insertarAutorizacionIngreso(forma.getDtoAutorizacion());
					if (nroAutorizacion!=ConstantesBD.codigoNuncaValido)
					{
						int numRegs=forma.getListadoDocs().size();
						//Se inserta un registro por cada documento con su dato financiacion 
						for (int i=0;i<numRegs;i++)
						{
							//Se setea el dto
							forma.getDtoDocsSaldoMora().setAutorizacionSaldoMora(nroAutorizacion);
							forma.getDtoDocsSaldoMora().setDatosFinanciacion(forma.getListadoDocs().get(i).getDatoFinanciacion());
							forma.getDtoDocsSaldoMora().setSaldo(forma.getListadoDocs().get(i).getValorTotal());
							
							AutorizacionIngresoPacienteSaldoMora.insertarDocsAutorizacionSaldoM(forma.getDtoDocsSaldoMora());
						}
						forma.setMensaje("Autorización Generada Correctamente!");
						return mapping.findForward("autorizarIngreso");
					}
					else
					{
						errores.add("",new ActionMessage("errors.notEspecific", "No se pudo Ingresar la Autorización. Por Favor verificar"));
						saveErrors(request, errores);
						return mapping.findForward("autorizarIngreso");
					}
				}
				else
				{
					errores.add("",new ActionMessage("errors.notEspecific", "El Paciente ya tiene Autorización de ingreso para la misma vía de ingreso. Por favor verifique. Proceso Cancelado."));
					saveErrors(request, errores);
					return mapping.findForward("autorizarIngreso");
				}
    		}
    		else
    		{
    			return mapping.findForward("autorizarIngreso");
    		}
		}
		
		private ActionForward buscarTipoPaciente(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente) 
		{
			Connection con =UtilidadBD.abrirConexion();
			forma.setTiposPaciente(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getDtoAutorizacion().getViaIngreso()+""));
			//Se cierra al conexión para las utilidades que la usan
			try 
			{
				UtilidadBD.cerrarConexion(con);
			} 
			catch (Exception e) 
			{
				logger.info("ERROR----->"+e);
			}
			return mapping.findForward("autorizarIngreso");
		}
	
		private ActionForward buscarCuotasDocumentos(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente) 
		{
			int diasParametro=Utilidades.convertirAEntero((ValoresPorDefecto.getDiasParaDefinirMoraXDeudaPacientes(usuario.getCodigoInstitucionInt())));
			
			//Se bsucan los documentos de garantia que tenga asosciados el paciente y al documento en especifico
			DtoDocumentosGarantia dtoDocs= forma.getListadoDocs().get(forma.getIndiceDocs());
			//Se consultan Las cuotas asociadas al documento
			forma.setListadoCuotas(AutorizacionIngresoPacienteSaldoMora.consultaCuotas(dtoDocs));
			
			int numDias=forma.getListadoDocs().get(forma.getIndiceDocs()).getDiasCuota();
			
			for (int j=0;j<forma.getListadoCuotas().size();j++)
			{
				int numeroCuota=forma.getListadoCuotas().get(forma.getIndiceDocs()).getNroCuota();
				int numeroDiasAdicionar=0;
				int numDiasFVenFActual=0;
				String fechaActual=UtilidadFecha.getFechaActual();
				String fechaVencimiento="";
				String fechaInicial=forma.getListadoDocs().get(forma.getIndiceDocs()).getFechaInicioDatoFin();
				
				numeroDiasAdicionar=numDias*numeroCuota;
				fechaVencimiento=UtilidadFecha.calcularFechaSobreFechaReferencia(numeroDiasAdicionar, fechaInicial,true);
				
				//Se asigna la fecha de vigencia de la cuota
				forma.getListadoCuotas().get(j).setFechaVigencia(fechaVencimiento);
				
				//Se asigna el saldo de la cuota (valor-aplicaciones)
				forma.getListadoCuotas().get(j).setFechaVigencia(fechaVencimiento);
				
				

				//Solo se hace la comparacion si lafecha de vencimiento es menos o = a la fecha actual, debido a que las demas estarán a futuro y por ende no estan en mora aún
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(fechaVencimiento, fechaActual))
				{
					//Si el numero de dias de la fecha de vencimiento y la fecha actual sobrepasa al parametro
					numDiasFVenFActual=UtilidadFecha.numeroDiasEntreFechas(fechaVencimiento, fechaActual);
					
					if (numDiasFVenFActual>numDias)
						forma.getListadoCuotas().get(j).setDiasMora(numDiasFVenFActual);
				}
				//De resto la cuota no esta en mora
				else
					forma.getListadoCuotas().get(j).setDiasMora(ConstantesBD.codigoNuncaValido);
			}
			return mapping.findForward("cuotasDocumento");
		}
		
	
		private ActionForward accionEmpezar(AutorizacionIngresoPacienteSaldoMoraForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping, PersonaBasica paciente) 
		{
			forma.reset();
			//1. Se carga la fecha actual en el dto
			forma.getDtoAutorizacion().setFecha(UtilidadFecha.getFechaActual());
			//2. Se carga la hora actual
			forma.getDtoAutorizacion().setHora(UtilidadFecha.getHoraActual());
			//3. Obtener vias ingreso 
			Connection con =UtilidadBD.abrirConexion();
			forma.setViasIngreso(Utilidades.obtenerViasIngresoBloqueaDeudorBloqueaPaciente(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
			//4. Obtener las personas y setear el dto de personas con el login actual
			forma.getDtoAutorizacion().setPersonaAutoriza(usuario.getLoginUsuario());
			forma.setUsuarios(Utilidades.obtenerUsuarios(con,usuario.getCodigoInstitucionInt(), true));
			//5. Obtener los motivos
			forma.setListadoMotivos(AutorizacionIngresoPacienteSaldoMora.consultaMotivosAutSaldoMora());
			
			
			//Se cierra al conexión para las utilidades que la usan
			try 
			{
				UtilidadBD.cerrarConexion(con);
			} 
			catch (Exception e) 
			{
				logger.info("ERROR----->"+e);
			}
			
			//Si no esta cargado un paciente
			if (paciente.getCodigoPersona()<1)
			{
				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				return mapping.findForward("paginaError");     
			}
			//Si esta cargado
			else
			{
				//Se bsucan los documentos de garantia que tenga asosciados el apciente
				forma.setListadoDocs(AutorizacionIngresoPacienteSaldoMora.consultaDocsPacienteDeudor(paciente.getCodigoPersona()));
				//Se bsuca el parámetro general de dias
				int diasParametro=Utilidades.convertirAEntero((ValoresPorDefecto.getDiasParaDefinirMoraXDeudaPacientes(usuario.getCodigoInstitucionInt())));
				
				//Bandera para indicar que el paciente no tiene deudas pendientes
				boolean pacienteConDeudas=false;
				
				//Si el paciente tiene documentos
				if (forma.getListadoDocs().size()>0)
				{
					//Se buscan las cuotas que dicho paciente posee para cada documento de garantía que éste tenga
					for (int i=0;i<forma.getListadoDocs().size();i++)
					{
						DtoDocumentosGarantia dtoDocs= forma.getListadoDocs().get(i);
						//Se consultan Las cuotas asociadas al documento
						forma.setListadoCuotas(AutorizacionIngresoPacienteSaldoMora.consultaCuotas(dtoDocs));
						//Se mira como estan las fechas para ver si estan en mora
						int numDias=forma.getListadoDocs().get(i).getDiasCuota();
						
						for (int j=0;j<forma.getListadoCuotas().size();j++)
						{
							
							BigDecimal zero= new BigDecimal(0.0);
							BigDecimal resta= forma.getListadoCuotas().get(j).getValorCuota().subtract(forma.getListadoCuotas().get(j).getTotalAplicado());
							//Si la resta entre la suma de aplicaciones y el valor de la cuota es 0, es que ya esta paga la cuota sino es que esta en mora
							int numeroCuota=forma.getListadoCuotas().get(j).getNroCuota();
							int numeroDiasAdicionar=0;
							int numDiasFVenFActual=0;
							String fechaActual=UtilidadFecha.getFechaActual();
							String fechaVencimiento="";
							String fechaInicial=forma.getListadoDocs().get(i).getFechaInicioDatoFin();
							
							
							if (resta.compareTo(zero)==0)
							{
								forma.getListadoCuotas().get(j).setEnMora(false);
							}
							else
							{
								
								numeroDiasAdicionar=numDias*numeroCuota;
								logger.info("DIAS A ADICIONAR->"+numeroDiasAdicionar);
								fechaVencimiento=UtilidadFecha.calcularFechaSobreFechaReferencia(numeroDiasAdicionar, fechaInicial,true);
								logger.info("FECHA VENC.CUOTA->"+fechaVencimiento);
								//Solo se hace la comparacion si lafecha de vencimiento es menos o = a la fecha actual, debido a que las demas estarán a futuro y por ende no estan en mora aún
								if (UtilidadFecha.esFechaMenorQueOtraReferencia(fechaVencimiento, fechaActual))
								{
									//Si el numero de dias de la fecha de vencimiento y la fecha actual sobrepasa al parametro
									numDiasFVenFActual=UtilidadFecha.numeroDiasEntreFechas(fechaVencimiento, fechaActual);
									logger.info("DIAS ENTRE FECHA Y F ACTUAL->"+numDiasFVenFActual);
									if (diasParametro<numDias)
									{
										forma.getListadoCuotas().get(j).setEnMora(true);
										pacienteConDeudas=true;
									}
								}
								else
									forma.getListadoCuotas().get(j).setEnMora(false);
							}
						}
					}
					
					if (!pacienteConDeudas)
					{
						request.setAttribute("codigoDescripcionError", "errors.paciente.sinCuotasPendientes");
						return mapping.findForward("paginaError"); 
					}
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.paciente.sinCuotasPendientes");
					return mapping.findForward("paginaError"); 
				}
				return mapping.findForward("autorizarIngreso");
			}	
		}
}