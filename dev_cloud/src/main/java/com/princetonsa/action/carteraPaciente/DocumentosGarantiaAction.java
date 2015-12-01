package com.princetonsa.action.carteraPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.carteraPaciente.DocumentosGarantiaForm;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class DocumentosGarantiaAction extends Action
{	
	
	Logger logger = Logger.getLogger(DocumentosGarantiaAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {

		Connection con = null;
		try{
			if (form instanceof DocumentosGarantiaForm) 
			{


				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				DocumentosGarantiaForm forma = (DocumentosGarantiaForm)form;
				String estado = forma.getEstado();
				permisoRol(con, forma, usuario);
				ActionErrors errores = new ActionErrors();

				String [] filtro={"imprimirVoucher","imprimirCheque","imprimirLetraCambio","imprimirpagare","imprimirChequeConsulta",
						"imprimirLetraCambioConsulta","imprimirVoucherConsulta","imprimirpagareConsulta"};

				logger.info("-------------------------------------");
				logger.info("Valor del Estado >> "+forma.getEstado());
				logger.info("Valor de la conexion  >> "+con);
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.resetDeudorCo();
					logger.warn("Estado no Valido dentro del Flujo de Documentos Garantia (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}


				//******************************************************************************
				//******************************************************************************
				//*****************************ESTADO INGRESOS
				else if(estado.equals("ordenarIngreso"))
				{	
					forma.setIngresosMap(this.accionOrdenarMapa(forma.getIngresosMap(),forma));				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarIngreso");					
				}	



				else if(estado.equals("ordenarDocumentos"))
				{	
					forma.setListaDocGarantiaMap(this.accionOrdenarMapa(forma.getListaDocGarantiaMap(),forma));				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarDoc");					
				}

				else if(estado.equals("irListarIngreso"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarIngreso");
				}



				//******************************************************************************
				//******************************************************************************
				//******************************************************************************
				//******************************************************************************
				//*****************************ESTADO DEUDORCO


				//********************************** Inicio Listar Informacion de los Registros de Ingresos para Deudor 
				else if(estado.equals("initdeudor"))
				{
					//valida que el paciente se encuentre cargardo en sesion 
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con);

					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;

					//lista los ingresos
					errores = this.accionListarIngreso(forma, mapping, con, usuario,paciente,ConstantesIntegridadDominio.acronimoDeudor,errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}


					UtilidadBD.closeConnection(con);

					//llama la jsp de lista ingresos
					return mapping.findForward("listarIngreso");
				}

				//************************************ Inicio Listar Informacion de los Registros de Ingresos para Codeudor
				else if(estado.equals("initcodeudor"))
				{
					//valida que el paciente se encuentre cargardo en sesion 
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con);

					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;

					//lista los ingresos
					errores = this.accionListarIngreso(forma, mapping, con, usuario,paciente,ConstantesIntegridadDominio.acronimoCoDeudor,errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}

					UtilidadBD.closeConnection(con);

					//llama la jsp de lista ingresos
					return mapping.findForward("listarIngreso");
				}


				//************************************ Carga el formulario de Verificacion de Datos
				else if(estado.equals("verificarDatos"))
				{
					UtilidadBD.closeConnection(con);

					//carga el usuario en session para efectos del formulario
					forma.setDeudorCoMap("usuarioDefault",usuario.getNombreUsuario());

					//llama la jsp de Verificar Datos
					return mapping.findForward("verificarDeudorCo");
				}			 
				//************************************** estado de ingreso a la informacion del deudor
				else if(estado.equals("irTipoDeudorCo"))
				{
					//carga la informacion de la Clase de DeudorCo (Deudor o Codeudor)				
					errores = this.accionCargarDeudorCoExistente(forma, con, usuario, errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}										

					//valida si se puede modifica el tipo de Deudor Co (paciente,responsable paciente, otro), 
					//si es posible modificarlo envia a la pagia de tipoDeudorCo, si no lo envia directamente
					//a la pagina del DeudorCo principal
					UtilidadBD.closeConnection(con);
					if(accionMostrarPaginaTipoDeudor(forma))
						return mapping.findForward("tipoDeudorCo");
					else
					{
						//direcciona la salida dependiendo del action. principal es la vista de captura de 
						//informacion del deudorCo
						return mapping.findForward("principal"); 
					}	
				}	
				/**
				 * Estado dummy para listar los documentos de garantï¿½a enviando el ingreso
				 */
				else if (estado.equals("irTipoDeudorCoDummy")) 
				{
					return accionIrTipoDeudorCoDummy(con,forma,errores,mapping,usuario,request);
				}

				//********************************************* Estado de carga de DedurCo a partir de la informacion dada en TipoDeudorCo
				else if(estado.equals("buscarXidentificacion"))
				{
					errores = validarCambioTipoDeudorCo(forma,errores,paciente);

					if(!errores.isEmpty())
					{
						logger.info("------Existen Errores en validar Cambio Tipo Deudor");	
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);										
						return mapping.findForward("tipoDeudorCo");					
					} 
					else
					{				
						//carga la informacion segun el tipo de DeudorCo Ingresado
						accionCargarInterfazDeudorCoTipo(forma,con,usuario,"buscar");

						errores = validarEdadDeudorCo(forma,errores);

						if(!errores.isEmpty())
						{						
							saveErrors(request,errores);	
							UtilidadBD.closeConnection(con);
							return mapping.findForward("tipoDeudorCo");
						}


						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}	
				}


				//estado de registro de informacion del DeudorCo
				//este ESTADO solo es llamado desde la jsp asociada a tipoDeudorCo
				//esto indica que es posible que el tipo de DeudorCo hubiera sido cambiado
				else if(estado.equals("principal"))
				{
					accionCargarInterfazDeudorCoTipo(forma,con,usuario,"cargar");
					UtilidadBD.closeConnection(con);


					errores =  validarEdadDeudorCo(forma,errores);

					if(!errores.isEmpty())
					{						
						saveErrors(request,errores);					
						return mapping.findForward("tipoDeudorCo");
					}				 

					return mapping.findForward("principal"); 
				}

				//*************************************** Guarda la informacion del DeudorCo
				else if(estado.equals("guardarDeudorCo"))
				{
					accionGuardarDeudorCo(forma,con,usuario);

					//carga la informacion de la Clase de DeudorCo (Deudor o Codeudor)				
					errores = this.accionCargarDeudorCoExistente(forma, con, usuario, errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenDeudorCo");				
				}	


				//************************************ Guarda la informacion de la verificacion
				else if(estado.equals("guardarVerificar"))
				{								 
					errores = this.accionGuardarVerificar(forma, con, errores, usuario);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);				
					}

					UtilidadBD.closeConnection(con);				
					//llama la jsp de Verificar Datos
					return mapping.findForward("verificarDeudorCo");			 				 
				}

				//**************************************** Eliminar Informacion DeudorCo
				else if(estado.equals("eliminarDeudorCo"))
				{
					accionEliminarDeudorCo(forma,con);				 

					if(!forma.isFuncionalidadDummy())
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}
					else
						return accionIrTipoDeudorCoDummy(con,forma,errores,mapping,usuario,request);					 
				}
				else if(estado.equals("irResumen"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenDeudorCo");				 
				}



				//******************************************************************************
				//******************************************************************************
				//******************************ESTADO CAMBIO DE UBICACION GEOGRAFICA
				else if(estado.equals("cambiarUbicacion"))
				{
					accionCambiarUbicacion(con,forma,response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 
				}


				//******************************************************************************
				//******************************************************************************
				//******************************************************************************
				//******************************************************************************
				//*****************************REGISTRAR DOCUMENTOS

				//********************************** Inicio Listar Informacion de los Registros de Ingresos para Deudor 
				else if(estado.equals("initregidoc"))
				{
					//valida que el paciente se encuentre cargardo en sesion 
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con);

					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;

					//lista los ingresos
					errores = this.accionListarIngreso(forma, mapping, con, usuario,paciente,"RegiDocumento",errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}

					UtilidadBD.closeConnection(con);

					//llama la jsp de lista ingresos
					return mapping.findForward("listarIngreso");
				}

				//	********************************** Inicio Listar Informacion de Documentos de Garantia 
				else if (estado.equals("irListarDoc"))
				{
					errores = this.accionListarDocumentos(forma,con,errores);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listarIngreso");
					}

					UtilidadBD.closeConnection(con);

					if(forma.getListaDocGarantiaMap("numRegistros").equals("0"))
						return mapping.findForward("tipoDocumento"); //llama la jsp para ingresar un nuevo documento
					else				
						return mapping.findForward("listarDoc"); //llama la jsp de lista Documentos			 				 
				}		
				/**
				 * Estado dummy para listar los documentos de garantï¿½a enviando el ingreso
				 */
				else if (estado.equals("irListarDocDummy")) 
				{
					return accionIrListarDocDummy(con,forma,errores,mapping,request);
				}
				//********************************** Carga y valida del Registro para ser almacenado en la BD.
				else if(estado.equals("irValidarDoc"))
				{
					errores = this.accionValidarRegistroDocumento(forma,con,errores,usuario.getCodigoInstitucionInt());

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("tipoDocumento");
					}

					UtilidadBD.closeConnection(con);
					return this.accionCargarInterfazNuevoDocumentoGarantia(forma,usuario,mapping);											 
				}

				//********************************** Inserta o Modifica el registro en la Base de Datos  
				else if(estado.equals("guardarDocumento"))
				{
					errores = this.accionInterfazGuardarDocumentoGarantia(forma, con,usuario, errores, paciente);

					if(!errores.isEmpty())
					{
						forma.setEstado("error");
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward(forma.getDocumentosGarantiaMap("nameForward").toString());
					}

					this.accionListarDocumentos(forma,con,errores);
					return mapping.findForward("listarDoc");			
				}

				//********************************** Distribulle el flujo dependiendo del Documento de Garantia
				else if(estado.equals("irDistribuirDoc"))
				{
					return this.accionIntefazDistribuirDocumentosGarantia(forma,usuario, mapping);				 
				}

				//************************************En este metodo se imprimen los diferentes tipos de documentos
				else if(estado.equals("imprimirVoucher")||estado.equals("imprimirCheque")||
						estado.equals("imprimirChequeConsulta")||estado.equals("imprimirLetraCambio")||
						estado.equals("imprimirActaCompromiso")||estado.equals("imprimirActaCompromisoConsulta")||
						estado.equals("imprimirpagare")||estado.equals("imprimirCartaInst")||
						estado.equals("imprimirLetraCambioConsulta")||estado.equals("imprimirVoucherConsulta")||
						estado.equals("imprimirpagareConsulta")||estado.equals("imprimirCartaInstConsulta"))
				{



					if (UtilidadCadena.indexOf(filtro, estado)>=0)
					{
						this.accionImprimirVoucherChequeLetraActa(con, forma, mapping, request, usuario, estado,0,"original");
						return this.accionImprimirVoucherChequeLetraActa(con, forma, mapping, request, usuario, estado,1,"copia");	
					}
					else
						return this.accionImprimirVoucherChequeLetraActa(con, forma, mapping, request, usuario, estado,0,"");
				}
				else
					if (estado.equals("imprimirTodos"))
					{

						return imprimirTodos(con, forma, mapping, request, usuario,filtro);

					}



				/**
				 * Adicionado por Jhony Alexande Duque A.
				 */			 
				//***********************ESTADOS PARA EL MANEJO DE LA CONSULTA DE LOS DOCUMENTOS DE GARANTIA************************

				//lista todos los ingresos pertenecientes al paciente
				//que tenga asociados un documento de garantia 
					else if(estado.equals("consultarDocGrarantia"))
					{

						//valida que el paciente se encuentre cargardo en sesion 
						ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con);

						if(forwardPaginaErrores!=null)
							return forwardPaginaErrores;

						//lista los ingresos
						return   this.accionListarDocsGarantPaciente(con, forma, mapping, request,paciente);

					}
				//ordena los campos de la forma.
					else if(estado.equals("ordenarIngresosXpaciente"))
					{	

						//se toma el forward hacia donde se va a enviar la pagina.
						String forward = forma.getIngresosMap("forward").toString();
						forma.setIngresosMap(this.accionOrdenarMapa(forma.getIngresosMap(),forma));				 
						UtilidadBD.closeConnection(con);
						//se vuelve a ingresar al hashmap el forward.
						forma.setIngresosMap("forward",forward);
						return mapping.findForward(forward);					
					}
				//carga el detallle de cada docuemnto de garantia, junto con la 
				//informacion del deudor y del codeudor
					else if(estado.equals("irDetalle"))
					{	

						return accionCargarDetalle(con, forma, mapping,usuario,paciente,request);

					} 
				//guarda las modificaciones hechas al destado del documento de garantia.
					else if(estado.equals("guardarDocumentoDetalle"))
					{
						forma.setOcultarEncabezado(false);
						errores = this.accionInterfazGuardarDocumentoGarantia(forma, con,usuario, errores, paciente);

						if(!errores.isEmpty())
						{
							forma.setEstado("error");
							saveErrors(request,errores);	
							UtilidadBD.closeConnection(con);
							return mapping.findForward(queDocEs(forma.getDocumentosGarantiaMap("tipodocumento").toString(),"detalle"));
						}

						return accionCargarDetalle(con, forma, mapping,usuario,paciente,request);			
					}

				//verifica los datos del deudor con la central de riesgos.
					else if(estado.equals("verificarDatosDeudor"))
					{	
						forma.setOcultarEncabezado(false);
						return this.accionVerificarDatosDeudor(forma, mapping, usuario);

					}
				//verifica los datos del codeudor con la central de riesgos.
					else if(estado.equals("verificarDatosCodeudor"))
					{	
						forma.setOcultarEncabezado(false);
						return this.accionVerificarDatosDeudor(forma, mapping, usuario);

					} 
				//muestra el detalle del deudor
					else if(estado.equals("irDetalleDeudor"))
					{	
						forma.setOcultarEncabezado(false);
						return this.accionCargarDeudorCodeudor(con, mapping, forma, usuario);



					} 
				//muestra el detalle del codeudor
					else if(estado.equals("irDetalleCodeudor"))
					{	
						forma.setOcultarEncabezado(false);
						return this.accionCargarDeudorCodeudor(con, mapping, forma, usuario);

					} 


					else if(estado.equals("guardarDetalleDeudorCo"))
					{

						forma.setOcultarEncabezado(false);
						//carga la informacion de la Clase de DeudorCo (Deudor o Codeudor)				
						errores = this.erroresDeudorCodeudor(forma);

						if (!errores.isEmpty())
						{
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("detalleDeudorCo") ;
						}
						else
						{
							accionGuardarDeudorCo(forma,con,usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("detalleDeudorCo");
						}
					}

					else if(estado.equals("busquedaRangos"))
					{
						forma.setOcultarEncabezado(false);			
						//lista los ingresos
						return   this.accionCargarCriteriosBusqueda(forma, mapping, usuario);

					}
					else if(estado.equals("buscarXRangos"))
					{
						forma.setOcultarEncabezado(false);
						errores = this.validarBusquedaXRangos(forma);

						if (!errores.isEmpty())
						{
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							forma.setEstado("busquedaRangos");
							return mapping.findForward("busquedaRangos") ;
						}


						//lista los ingresos
						return   this.accionListarDocsGarantRangos(con, forma, mapping, request,usuario);

					}
					else if (estado.equals("redireccion"))
					{
						forma.setOcultarEncabezado(false);
						UtilidadBD.closeConnection(con);
						response.sendRedirect(forma.getLinkSiguiente());
						return null;
					}




				//*********************FIN DE LOS ESTADOS CONSULTAR DOCUMENTOS GARANTIA*************************************************

					else if(estado.equals("cambiarPais") || estado.equals("cambiarPaisResidencia")){
						listarCiudades(forma);
						return mapping.findForward("principal");
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
	 * Este método se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado país.
	 * 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private void listarCiudades(DocumentosGarantiaForm forma)
	{
		String codigoPais="";
		
		if(forma.getEstado().equals("cambiarPaisResidencia"))
		{
			codigoPais= (String)forma.getDeudorCoMap().get("codigoPaisReside");
		}
		else
		{			
			codigoPais=(String)forma.getDeudorCoMap().get("codigoPais");
		}
		
		if(!UtilidadTexto.isEmpty(codigoPais))
		{
			try
			{
				HibernateUtil.beginTransaction();
				ArrayList<Ciudades> listaCiudades =AdministracionFabricaServicio.crearLocalizacionServicio().listarCiudadesPorPais(codigoPais);
				
				if(listaCiudades!=null && listaCiudades.size()==1)
				{
					Ciudades ciudad = listaCiudades.get(0);
					
					String codigoCiudad=ciudad.getId().getCodigoCiudad()+ ConstantesBD.separadorSplit
					+ ciudad.getDepartamentos().getId().getCodigoDepartamento()+ ConstantesBD.separadorSplit
					+ ciudad.getPaises().getCodigoPais();
					
					if(forma.getEstado().equals("cambiarPaisResidencia"))
					{
						forma.setCiudadResidencia(codigoCiudad);
					}
					else
					{			
						forma.setCiudadExpedicion(codigoCiudad);
					}
				}
				if(forma.getEstado().equals("cambiarPaisResidencia"))
				{
					forma.setCiudadesResidenciaMap(listaCiudades);
				}
				else
				{			
					forma.setCiudadesMap(listaCiudades);
				}
			}
			catch (Exception e)
			{
				HibernateUtil.abortTransaction();
				logger.error(e);
				e.printStackTrace();
			}
			finally
			{
				HibernateUtil.endTransaction();
			}
		}		
	}

	public void permisoRol(Connection  connection,DocumentosGarantiaForm forma,UsuarioBasico usuario)
	{		
		logger.info("valor del Login Usuario >> "+usuario.getLoginUsuario());
		forma.setPermisoModificar(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(),630));
	
		//logger.info("\n\n *************** entro a permisoRol y es"+forma.isPermisoModificar());
	}
	

	/*-----------------------------------------------------
	 * METODOS DOCUMENTOS GARANTIA
	 * ---------------------------------------------------*/
	
	
	private ActionForward imprimirTodos (Connection connection, DocumentosGarantiaForm forma, ActionMapping  mapping, HttpServletRequest request, UsuarioBasico usuario,String [] filtro)
	{
		ActionForward retorno = new ActionForward();
		//se toma la variable documentosimpresion para identificar cuantos documentos se van a imprimir
		String [] estados;
		
		int cant =0;
		//se sacan los diferentes estados y se meten en un arreglo
		estados=forma.getDocumentosImpresion().split(ConstantesBD.separadorSplit);
		cant=estados.length;
		int k=0;
		//logger.info("\n el valor de cant es "+cant);
		for (int i=0;i<cant;i++)
		{
			logger.info("\n ######################################################### estado --> "+estados[i]+ "   --- k-->"+k +" i -->"+i);
				if (UtilidadCadena.indexOf(filtro, estados[i])>=0)
				{
					this.accionImprimirVoucherChequeLetraActa(connection, forma, mapping, request, usuario, estados[i],k,"original");
					k++;
					retorno=this.accionImprimirVoucherChequeLetraActa(connection, forma, mapping, request, usuario, estados[i],k,"copia");
				}
				else
					retorno=this.accionImprimirVoucherChequeLetraActa(connection, forma, mapping, request, usuario, estados[i],k,"");
				k++;
		}
		//se llama el garbage collector
		//System.gc();
		return retorno;
	}
	
	
	/**
	 * Metodo encargado de listar los documentos de garantia pertenecientes a un paciente.
	 * @author Jhony Alexander Duque A.
	 * @Param 
	 */
	private ActionForward accionListarDocsGarantPaciente (Connection connection, DocumentosGarantiaForm forma, ActionMapping mapping, HttpServletRequest request,PersonaBasica paciente)
	{
		HashMap parametros = new HashMap();
		forma.setOcultarEncabezado(false);
		parametros.put("codigopaciente", paciente.getCodigoPersona());
		forma.setIngresosMap(DocumentosGarantia.consultarListadoingresosDocumentosGarantia(connection, parametros));
		forma.setIngresosMap("forward", "principalBusXPac");
		UtilidadBD.closeConnection(connection);
		logger.info("\n\n*************YA HICE LA CONSULTA***************");
		logger.info("\n**EL VALOR DEL HASHMAP ES**");
		logger.info("\n****"+forma.getIngresosMap());
		
		return mapping.findForward("principalBusXPac");
	}
	
	/**
	 * Metodo encargado de listar los documentos de garantia entre Rangos.
	 * @author Jhony Alexander Duque A.
	 * @Param 
	 */
	private ActionForward accionListarDocsGarantRangos (Connection connection, DocumentosGarantiaForm forma, ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario)
	{
		//se le ingresa el parametro de busqueda institucion el cual se saca de la sesion.
		forma.setBusquedaRangosMap("institucion",usuario.getCodigoInstitucion());
		
		forma.setIngresosMap(DocumentosGarantia.consultaXRangosDocumentosGarantia(connection, forma.getBusquedaRangosMap()));
		forma.setIngresosMap("forward", "busquedaRangos");
		UtilidadBD.closeConnection(connection);
		//logger.info("\n\n*************YA HICE LA CONSULTA***************");
		//logger.info("\n**EL VALOR DEL HASHMAP ES**");
		//logger.info("\n****"+forma.getIngresosMap());
		
		return mapping.findForward("busquedaRangos");
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de cargar los criterios para la busqueda por rangos.
	 * @author Jhony Alexander Duque A.
	 * @Param 
	 */
	private ActionForward accionCargarCriteriosBusqueda (DocumentosGarantiaForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		
		forma.iniciarCentroAtencion(usuario.getCodigoInstitucionInt());
		forma.setCentrosAtencionMap("centroatencionactual", usuario.getCentroAtencion());
		forma.setCentrosAtencionMap("codigocentroactual", usuario.getCodigoCentroAtencion());
		forma.iniciarBusquedaRangos();
		forma.resetBusquedaRangos ();
		
		return mapping.findForward("busquedaRangos");
	}
	
	
	
	
	
	/**
	 * Metodo encargado de cargar en la forma la informacion del documento de garantia
	 * @author Jhony alexander Duque
	 * @param connection
	 * @param forma DocumentosGarantiaForm
	 * @param mapping
	 */
	private ActionForward accionCargarDetalle (Connection connection, DocumentosGarantiaForm forma, ActionMapping mapping,UsuarioBasico usuario, PersonaBasica paciente,HttpServletRequest request)
	{
		//logger.info("\n\n******* si entro a accionCargarDetalle y el index es "+forma.getIndexIngresosMap());
		//logger.info("\n\n******* hashmap IngresosMap "+forma.getIngresosMap());
		forma.setOcultarEncabezado(false);
		String forward = "";
		String []dependencia;
		HashMap parametros = new HashMap();
		HashMap parametrosAux = new HashMap();
		//Se cargan los parametros a ser enviados a la consulta para que nos retornen los valores 
		//del documento de garantia.
		parametros.put("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));		
		parametros.put("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));
		parametros.put("tipodocumento",forma.getIngresosMap("tipodocumento_"+forma.getIndexIngresosMap()));		
		parametros.put("consecutivo",forma.getIngresosMap("consecutivo_"+forma.getIndexIngresosMap()));
		parametros.put("anioconsecutivo",forma.getIngresosMap("anioconsecutivo_"+forma.getIndexIngresosMap()));
		
		
		if((forma.getIngresosMap("cartera_"+forma.getIndexIngresosMap())+"").equals(ConstantesBD.acronimoSi))
			forma.setPermisoModificar(false);
		if((forma.getIngresosMap("cartera_"+forma.getIndexIngresosMap())+"").equals(ConstantesBD.acronimoNo))
			forma.setPermisoModificar(true);
		
		//se carga la forma con la respuesta de la consulta
		forma.setDocumentosGarantiaMap(DocumentosGarantia.consultarListaDocumentosGarantia(connection, parametros));
		logger.info("\n\ncarteraaaaaaaaa indicativo inicial:: "+forma.getIngresosMap("cartera_"+forma.getIndexIngresosMap()));
		forma.setDocumentosGarantiaMap("cartera",forma.getIngresosMap("cartera_"+forma.getIndexIngresosMap()));		
		logger.info("\n\ncarteraaaaaaaaa indicativo:: "+forma.getDocumentosGarantiaMap("cartera"));
		forma.setDocumentosGarantiaMap("usuariomodifica",usuario.getLoginUsuario());		
		forma.setDocumentosGarantiaMap("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setDocumentosGarantiaMap("horamodifica",UtilidadFecha.getHoraActual());
		//es necesario borrar estos valores del hashmap e introducirlos solamente cuando se va a anular
		forma.getDocumentosGarantiaMap().remove("fechaanula");
		forma.getDocumentosGarantiaMap().remove("usuarioanula");
		forma.getDocumentosGarantiaMap().remove("horaanula");
		//se carga el HashMap parametros con el paramtero  claseDeudorCo (indica si es deudor o no) que es DEU o CDEU
		parametros.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoDeudor);	
		//se carga la forma con los datos del deudor
		forma.setDeudorMap(DocumentosGarantia.consultarDeudorCo(connection, parametros));
		
		//cargo el estado del docuemnto para ese deudor
		forma.setDeudorMap("estadodoc",forma.getDocumentosGarantiaMap("estado"));
		
		//se cargan los parametros para la bsqueda de dependencias de documentos de garantias.
		//esto quiere decir que se mira si el deudor tiene asociado algun documento de garantia.
		parametrosAux.put("ingreso",forma.getDocumentosGarantiaMap("ingreso"));
		parametrosAux.put("codigoPaciente",forma.getDocumentosGarantiaMap("codigopaciente"));
		parametrosAux.put("institucion",forma.getDocumentosGarantiaMap("institucion"));
		parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoDeudor);
		
		//se pregunta si existen registros en el HashMap DeudorMap 
		if(Integer.parseInt(forma.getDeudorMap().get("numRegistros").toString())>0)//existe informacion de la Clase DeudorCo
		{
			//depencia nos almacena la informacion de que si existen dependencia,
			//esto quiere decir  que si el deudor tiene algun documento de garantia
			//asociado a el y mustra si la informacion de que si es modificable el deudor
			//*** "existenDocsGarantia" --> indica si existe dependencia y su valor es (S o N)
			//*** "esModInfoDeudorCo" --> indica si los datos del deudor son modificables y su (S o N) 
			dependencia = DocumentosGarantia.darDependenciasDoscGarantias(DocumentosGarantia.infoDocsDependientes(connection, parametrosAux));
			forma.setDeudorMap("existenDocsGarantia",dependencia[0]);
			forma.setDeudorMap("esModInfoDeudorCo",dependencia[1]);
		
		}
		
		//se consulta la informacion del paciente para adquirir el numero y tipo de identificacion
		//del paciente, necesarios para la busqueda del responsable.
		parametros = DocumentosGarantia.consultarDatosPaciente(connection, parametrosAux);
		// Se consulta el Responsable del paciente utilizando el numero de identificacion
		//y el tipo anteriormente consultados
		parametros = DocumentosGarantia.consultarResponsablePaciente(connection, parametros);
		
		//se cambia el para metro claseDeudorCo por CDEU para consultar los datos del codeudor
		parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoCoDeudor);
		//se carga la forma con los datos del Codeudor
		forma.setCodeudorMap(DocumentosGarantia.consultarDeudorCo(connection, parametrosAux));

//		cargo el estado del docuemnto para ese deudor
		forma.setCodeudorMap("estadodoc",forma.getDocumentosGarantiaMap("estado"));
		
		//se pregunta si existen registros en el HashMap CoDeudorMap
		if(Integer.parseInt(forma.getDeudorMap().get("numRegistros").toString())>0)//existe informacion de la Clase DeudorCo
		{
			//depencia nos almacena la informacion de que si existen dependencia,
			//esto quiere decir  que si el codeudor tiene algun documento de garantia
			//asociado a el y mustra si la informacion de que si es modificable el codeudor
			//*** "existenDocsGarantia" --> indica si existe dependencia y su valor es (S o N)
			//*** "esModInfoDeudorCo" --> indica si los datos del codeudor son modificables y su (S o N)
			dependencia = DocumentosGarantia.darDependenciasDoscGarantias(DocumentosGarantia.infoDocsDependientes(connection, parametrosAux));
			forma.setCodeudorMap("existenDocsGarantia",dependencia[0]);
			forma.setCodeudorMap("esModInfoDeudorCo",dependencia[1]);
			
		
		}
		
		//Es bien importante saber si "***"parametros"***" tiene registros; parametros
		//trae la informacion del Responsable
		if((Integer.parseInt(parametros.get("numRegistros").toString())>0))
		{
			//introducimos la informacion del responsable para el deudor
			forma.setDeudorMap("datosResponsablePaciente"," ");
			forma.setDeudorMap("existeResponsablePaciente",ConstantesBD.acronimoSi);
			//indica si existe informacion del Responsable en la Tabla Responsable pero no esta 
			//relacionado con la cuenta
			forma.setDeudorMap("existeResponsableHistorico",ConstantesBD.acronimoNo);	
			
			
		}	
		else
		{
			
			forma.setDeudorMap("existeResponsablePaciente",ConstantesBD.acronimoNo);			
			//no exite informacion del Responsable Paciente asociado a la cuenta
			forma.setDeudorMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
			
			
		}
		
		//se pregunta si existen registros en el HashMap CoDeudorMap
		if(Integer.parseInt(forma.getCodeudorMap().get("numRegistros").toString())>0)//existe informacion de la Clase DeudorCo
		{
			parametros.put("tipoIdentificacion",forma.getCodeudorMap("tipoIdentificacion"));
			parametros.put("numeroIdentificacion",forma.getCodeudorMap("numeroIdentificacion"));
			
			parametros = DocumentosGarantia.consultarResponsablePaciente(connection, parametros);
			
			if((Integer.parseInt(parametros.get("numRegistros").toString())>0))
			{
				
				//introducimos la informacion del responsable del codeudor 
				forma.setCodeudorMap("datosResponsablePaciente"," ");
				forma.setCodeudorMap("existeResponsablePaciente",ConstantesBD.acronimoSi);
				//indica si existe informacion del Responsable en la Tabla Responsable pero no esta 
				//relacionado con la cuenta
				forma.setCodeudorMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
				
			}
			else
			{
				//para el codeudor
				forma.setCodeudorMap("existeResponsablePaciente",ConstantesBD.acronimoNo);			
				//no exite informacion del Responsable Paciente asociado a la cuenta
				forma.setCodeudorMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
			}
			
		}
		else
		{
			//para el codeudor
			forma.setCodeudorMap("existeResponsablePaciente",ConstantesBD.acronimoNo);			
			//no exite informacion del Responsable Paciente asociado a la cuenta
			forma.setCodeudorMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
		}
		
		
		
		
		//Este metodo identifica a que forward va a ser enviado
		forward=queDocEs(forma.getIngresosMap("tipodocumento_"+forma.getIndexIngresosMap()).toString(),"detalle");
		logger.info("\n\n******************************************************************************");
		logger.info("****************************LOGGER PRUEBA***************************************");
		logger.info("\n******************************************************************************");
		logger.info("\n** EL VALOR DEL HASHMAP DocumentosGarantiaMap ES: "+forma.getDocumentosGarantiaMap());
		logger.info("\n** EL VALOR DEL HASHMAP DeudorMap ES: "+forma.getDeudorMap());
		logger.info("\n** EL VALOR DEL HASHMAP CodeudorMap ES: "+forma.getCodeudorMap());
		logger.info("\n** EL FORWARD ES: "+forward);
		logger.info("\n\n******************************************************************************");
		logger.info("************ codigo de la persona es "+paciente.getCodigoPersona());
		
		if (forward.equals("infoPagare")||forward.equals("detallePagare"))
		{
			if(!ValoresPorDefecto.getFormatoDocumentosGarantia_Pagare(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoFormatoDocGarantiaShaio))
			{
				forma.setImprimePagare(false);
				forma.setMensaje("Sin formato definido no se puede realizar impresiï¿½n");
			}
		}
		
		if (forma.getIngresosMap().containsKey("codigopaciente_"+forma.getIndexIngresosMap())) 
		{
			if (paciente.getCodigoPersona()==-1)
			{
				logger.info("si entre a la conparacion de codigos");
				paciente.setCodigoPersona(Integer.parseInt(forma.getIngresosMap("codigopaciente_"+forma.getIndexIngresosMap()).toString()));
				UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente,request);
			}
			else	
				if (Integer.parseInt(forma.getIngresosMap("codigopaciente_"+forma.getIndexIngresosMap()).toString())!= paciente.getCodigoPersona())
				{
					logger.info("si entre a la conparacion de codigos");
					paciente.setCodigoPersona(Integer.parseInt(forma.getIngresosMap("codigopaciente_"+forma.getIndexIngresosMap()).toString()));
					UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente,request);
				}
		}
		UtilidadBD.closeConnection(connection);
		return mapping.findForward(forward);
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo que se encargade cargar los datos para el deudor y el codeudor
	 * @author Jhony Alexander Duque A.
	 */
	public ActionForward accionCargarDeudorCodeudor (Connection connection, ActionMapping mapping, DocumentosGarantiaForm forma, UsuarioBasico usuario)
	{
		String forward="";
	
		
		if (forma.getEstado().equals("irDetalleDeudor"))
			forma.setDeudorCoMap(forma.getDeudorMap());
		
		if (forma.getEstado().equals("irDetalleCodeudor"))
			forma.setDeudorCoMap(forma.getCodeudorMap());
		
		//carga el HashMap de Tipo de Identificacion
		forma.setTipoIdentificacionMap(Utilidades.obtenerTiposIdentificacion(connection,"",usuario.getCodigoInstitucionInt()));	
		
		//carga los paises
		forma.setPaisesMap(Utilidades.obtenerPaises(connection));
		
		//carga las ciudades
		forma.setCiudadesMap(AdministracionFabricaServicio.crearLocalizacionServicio()
				.listarCiudades());
		forma.setCiudadesResidenciaMap(AdministracionFabricaServicio.crearLocalizacionServicio()
				.listarCiudades());
		
		forward=queDocEs(forma.getDocumentosGarantiaMap().get("tipodocumento").toString(),"detalledeudorco");
		accionMostrarPaginaTipoDeudor(forma);
		//logger.info("\n\n***********************************************ENTRO A CARGAR DEUDOR/CODEUDOR "+forma.getDeudorCoMap());
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward(forward);
		
	}
	
	/**
	 * Metodo que valida que la informacion del deudor y el codeudor.
	 * @author Jhony Alexander Duque A.
	 * @param forma
	 * @return
	 */
	public ActionErrors erroresDeudorCodeudor (DocumentosGarantiaForm forma)
	{
//		enviar los errores a l apagina de detalle, puesto que normalmente el la envia el principal
		ActionErrors errores = new ActionErrors();
		
		
		//validacion
		
			
			if(forma.getEstado().equals("guardarDetalleDeudorCo"))
	    	{
				logger.info("\n\n entro a erroresDeudorCodeudor "+forma.getDeudorCoMap("fechaNacimiento")+"");
				
				if(forma.getDeudorCoMap("tipoIdentificacion").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El tipo de Identificacion "));
	    		
	    		if(forma.getDeudorCoMap("numeroIdentificacion").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Numero de Identificacion "));
	    		
	    		if(forma.getDeudorCoMap("codigoPais").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Pais de Expedicion "));
	    		
	    		if(forma.getDeudorCoMap("codigoCiudad").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","La Ciudad de Expedicion "));
	    		
	    		if(forma.getDeudorCoMap("primerApellido").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Apellido "));
	    		
	    		if(forma.getDeudorCoMap("primerApellido").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Apellido "));
	    		
	    		if(forma.getDeudorCoMap("primerNombre").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Nombre "));
	    		
	    		if(forma.getDeudorCoMap("direccionReside").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Direcciï¿½n de Residencia "));
	    		
	    		if(forma.getDeudorCoMap("codigoPaisReside").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Codigo del Pais de Residencia "));
	    		
	    		if(forma.getDeudorCoMap("codigoCiudadReside").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Codigo de la Ciudad de Residencia "));
	    		
	    		if(forma.getDeudorCoMap("telefonoReside").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","El Telefono del Paciente "));    		
	    		
	    		if(forma.getDeudorCoMap("fechaNacimiento").toString().equals(""))
	    		{
	    			logger.info("\n\n entro a erroresDeudorCodeudor ********* 1 *************** "+forma.getDeudorCoMap("fechaNacimiento")+"");
	    			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Nacimiento "));
	    		}
	    		else
	    		{
					if(!UtilidadFecha.validarFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getDeudorCoMap("fechaNacimiento")+"")+""))
					{
						logger.info("\n\n entro a erroresDeudorCodeudor ********* 2 *************** "+forma.getDeudorCoMap("fechaNacimiento")+"");
						errores.add("descripcion",new ActionMessage("errors.invalid","La Fecha de Nacimiento "+forma.getDeudorCoMap("fechaNacimiento").toString()+" "));
					}
					else
					{
						if(ValoresPorDefecto.getValidarEdadDeudorPaciente(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString().equals(ConstantesBD.acronimoSi))
						{
							logger.info("\n\n entro a erroresDeudorCodeudor ********* 3 *************** "+forma.getDeudorCoMap("fechaNacimiento")+"");
							if(UtilidadFecha.calcularEdad(forma.getDeudorCoMap("fechaNacimiento").toString()) < Integer.parseInt(ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString()))
								errores.add("descripcion",new ActionMessage("errors.invalid","La Edad del Responsable debe ser Mayor a "+ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString()));
						}
					}
					
					//valida que la fecha no se ha igual a la actual
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",forma.getDeudorCoMap("fechaNacimiento").toString(),"00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getDeudorCoMap("fechaNacimiento").toString(),UtilidadFecha.getFechaActual()));						
	    		}
	    		
	    		
	    		if(forma.getDeudorCoMap("tipoDeudorCo").equals(ConstantesIntegridadDominio.acronimoPaciente))
	    		{    			
		    		if(!forma.getDeudorCoMap("relacionPaciente").toString().equals(""))
		    			errores.add("descripcion",new ActionMessage("errors.invalid","El Campo Relacion Paciente en el caso Tipo Deudor igual Paciente debe estar Vacio. Campo Relacion Paciente  "));
	    		}	
	    		else
	    		{    		
	    			if(forma.getDeudorCoMap("relacionPaciente").toString().equals(""))
		    			errores.add("descripcion",new ActionMessage("errors.required","La Relacion Paciente "));    		
	    		}
	    		
	    		
	    		if(forma.getDeudorCoMap("tipoOcupacion").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","Tipo Ocupacion "));
	    		
	    		if(forma.getDeudorCoMap("ocupacion").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","Ocupacion "));
	    		
	    		
	    		if(forma.getDeudorCoMap("tipoOcupacion").toString().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
	    		{
	    			logger.info("\n\n entro a tipo empleado "+forma.getDeudorCoMap("tipoOcupacion")+"");
	    			if(forma.getDeudorCoMap("empresa").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Empresa "));
	    			
	    			if(forma.getDeudorCoMap("cargo").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Cargo "));
	    			
	    			if(forma.getDeudorCoMap("antiguedad").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Antiguedad ")); 
	    			
	    			if(forma.getDeudorCoMap("direccionOficina").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Direccion Oficina "));  			
	    			
	    			if(forma.getDeudorCoMap("telefonoOficina").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Telefono Oficina "));
	    		}
	    		else
		    		if(forma.getDeudorCoMap("tipoOcupacion").toString().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente))
		    		{
		    			if(forma.getDeudorCoMap("direccionOficina").toString().equals(""))
		        			errores.add("descripcion",new ActionMessage("errors.required","Direccion Oficina "));  			
		    			
		    			if(forma.getDeudorCoMap("telefonoOficina").toString().equals(""))
		        			errores.add("descripcion",new ActionMessage("errors.required","Telefono Oficina "));
		    		}
	    		
	    		if(forma.getDeudorCoMap("nombresReferencia").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","Nombre de la Referencia "));
	    		
	    		if(forma.getDeudorCoMap("direccionReferencia").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","Direccion de la Referencia "));
	    		
	    		if(forma.getDeudorCoMap("telefonoReferencia").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","Telefono de la Referencia "));
	    	}   	
	
		return errores;
	}
	
	
	/**
	 * Funcion que verifica que los datos esten correctos antes de hacer la busqueda X Rangos.
	 * @author Jhony Alexander Duque A.
	 * @return errores
	 */
	public ActionErrors validarBusquedaXRangos (DocumentosGarantiaForm forma)
	{
		ActionErrors errores = new ActionErrors();
		
		if (!forma.getBusquedaRangosMap("fechainicial").equals(""))
		if( !UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getBusquedaRangosMap("fechainicial").toString(),UtilidadFecha.getFechaActual()))
			errores.add("descripcion",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia"," Inicial "+forma.getBusquedaRangosMap("fechainicial").toString(),UtilidadFecha.getFechaActual()));
		
		if (!forma.getBusquedaRangosMap("fechafinal").equals(""))
			if (forma.getBusquedaRangosMap("fechainicial").equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial "));
			else	
				if( UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getBusquedaRangosMap("fechafinal").toString(),forma.getBusquedaRangosMap("fechainicial").toString()) )
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Final "+forma.getBusquedaRangosMap("fechafinal").toString()," Inicial "+forma.getBusquedaRangosMap("fechainicial").toString()));
				else
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getBusquedaRangosMap("fechafinal").toString(),UtilidadFecha.getFechaActual()))
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia"," Final "+forma.getBusquedaRangosMap("fechafinal").toString(),UtilidadFecha.getFechaActual()));
		return errores;
	}
	
	
	
	
	
	
	/**
	 * Metodo Que se encarga de verificar en la central de riesgos
	 * al deudor  y al codeudor
	 * @author Jhony Alexander Duque A.
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public ActionForward accionVerificarDatosDeudor (DocumentosGarantiaForm forma,ActionMapping mapping, UsuarioBasico usuario)
	{
		String forward="";
		//identificamos a cual forward se va a ir
		if(forma.getEstado().equals("verificarDatosDeudor")||forma.getEstado().equals("verificarDatosCodeudor"))
			forward="verificarDeudorCo";
		else
			if(forma.getEstado().equals("irDetalleDeudor")||forma.getEstado().equals("irDetalleCodeudor"))
				forward="detalleDeudorCo";
		//se verifica que cual es el estado para asi  cargar el hashmap correspondiente
		if (forma.getEstado().equals("verificarDatosDeudor")||forma.getEstado().equals("irDetalleDeudor"))
			//se esta cargando el hashmap que debe de ir a la jsp verificarDeudorCo
			forma.setDeudorCoMap(forma.getDeudorMap());
		else
			if (forma.getEstado().equals("verificarDatosCodeudor")||forma.getEstado().equals("irDetalleCodeudor"))
				//se esta cargando el hashmap que debe de ir a la jsp verificarDeudorCo
				forma.setDeudorCoMap(forma.getCodeudorMap());
		 //carga el usuario en session para efectos del formulario
		 forma.setDeudorCoMap("usuarioDefault",usuario.getNombreUsuario());
		 
		 //llama la jsp de Verificar Datos
	     return mapping.findForward(forward);
		
		
	}

	
	
	
	
	/**
	 * Mï¿½todo implemetado para listar los documentos de garantï¿½a enviando el ingreso desde otra funcionalidad
	 * @param con
	 * @param forma
	 * @param errores
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionIrListarDocDummy(Connection con, DocumentosGarantiaForm forma, ActionErrors errores, ActionMapping mapping, HttpServletRequest request) 
	{
		//***********SE ACOMODAN LOS DATOS AL REGISTRO DE DOCUMENTO DE GARANTï¿½A***************************
		String consecutivoIngreso = "";
		forma.resetDeudorCo();
		forma.setFuncionalidadDummy(true);
		forma.setOcultarEncabezado(true);
		forma.setIndexIngresosMap("0");
		
		consecutivoIngreso =  IngresoGeneral.getConsecutivoIngreso(con, forma.getIdIngreso());
		
		forma.setIngresosMap("ingreso_"+forma.getIndexIngresosMap(), forma.getIdIngreso());
		forma.setIngresosMap("consecutivoingreso_"+forma.getIndexIngresosMap(),consecutivoIngreso);		
		forma.setIngresosMap("institucion_"+forma.getIndexIngresosMap(), forma.getInstitucion());
		forma.setIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap(), forma.getCodigoPaciente());
		//*************************************************************************************************
		logger.info("SOLO DOCUMENTOS VIGETNES? "+forma.isSoloDocumentosVigentes());
		errores = this.accionListarDocumentos(forma,con,errores);
		//**********SE VERIFICA SI HAY DOCUMENTOS EN ESTADO VIGENTE (cuando solo lo requiera)********************
		if(forma.isSoloDocumentosVigentes()&&errores.isEmpty()&&Integer.parseInt(forma.getListaDocGarantiaMap("numRegistros").toString())<=0)
			errores.add("",new ActionMessage("errors.notEspecific","El ingreso Nï¿½ "+consecutivoIngreso+" no tiene documentos de garantï¿½a vigentes"));
		//*****************************************************************************************************
		
		if(!errores.isEmpty())
		{
			forma.setEstado("error");
			saveErrors(request,errores);	
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}
		
		UtilidadBD.closeConnection(con);
		
		if(forma.getListaDocGarantiaMap("numRegistros").equals("0"))
			return mapping.findForward("tipoDocumento"); //llama la jsp para ingresar un nuevo documento
		else				
			return mapping.findForward("listarDoc"); //llama la jsp de lista Documentos		
	}
	
	
	/**
	 * Lista los Documentos de Garantia Almacenados 
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * */
	public ActionErrors accionListarDocumentos(DocumentosGarantiaForm forma, 
											   Connection con, 
											   ActionErrors errores)
	{
		HashMap parametros = new HashMap();
		String existeCodeudorCo = "";
		forma.resetDocumentosGarantia();
		
		//parametros de busqueda 
		parametros.put("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));		
		parametros.put("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));
		
		parametros.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoDeudor);	
			
		parametros = DocumentosGarantia.consultarDependenciaDeudorCodeudor(con, parametros);		
		
		if(Integer.parseInt(parametros.get("numRegistros").toString())<1)
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Informacion del Deudor "));
			return errores;
		}		
				
		
		//realiza la consulta apartir del ingreso y la instucion		
		parametros.put("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));
		parametros.put("soloVigente",forma.isSoloDocumentosVigentes());
		forma.setListaDocGarantiaMap(DocumentosGarantia.consultarListaDocumentosGarantia(con, parametros));	
		
		//captura la informacion del Deudor
		forma.setListaDocGarantiaMap("primerNombreDeu",parametros.get("primerNombre"));
		forma.setListaDocGarantiaMap("segundoNombreDeu",parametros.get("segundoNombre"));
		forma.setListaDocGarantiaMap("primerApellidoDeu",parametros.get("primerApellido"));
		forma.setListaDocGarantiaMap("segundoApellidoDeu",parametros.get("segundoApellido"));
		forma.setDeudorCoMap("fechaNacimiento",parametros.get("fechaNacimiento")); 
		
		//Valida si la edad del paciente a sido cambiada desde las funcionalidades de Manejo Paciente y Modificar Cuentas y verificar que sea valida
		errores = validarEdadDeudorCo(forma, errores);		
		
		// verifica si existe informacion del Codeudor		
		parametros.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoCoDeudor);
			
		parametros = DocumentosGarantia.consultarDependenciaDeudorCodeudor(con, parametros);		
		
		if(Integer.parseInt(parametros.get("numRegistros").toString())<1)
			forma.setListaDocGarantiaMap("existeCodeudorOdeudor",ConstantesBD.acronimoNo);
		else
			forma.setListaDocGarantiaMap("existeCodeudorOdeudor",ConstantesBD.acronimoSi);
		
		
		//actualiza campos para el ingreso de un nuevo documento
		forma.setDocumentosGarantiaMap("nuevoTipoDocumento","");
		forma.setDocumentosGarantiaMap("nuevoEstado","");		
		
		return errores;
	}
	
	
	
	/**
	 * Carga la informacion del documento en el HashMap de Documentos para ser
	 * mostrado en el formato del documento
	 * @param DocumentosGarantiaForm forma
	 * @param ActionMapping mapping
	 * */
	public ActionForward accionIntefazDistribuirDocumentosGarantia(DocumentosGarantiaForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{		
		
		//Informacion Comun para todos los documentos 
		forma.setDocumentosGarantiaMap("cartera",forma.getListaDocGarantiaMap("cartera_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("ingreso",forma.getListaDocGarantiaMap("ingreso_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("consecutivoingreso",forma.getListaDocGarantiaMap("consecutivoingreso_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("consecutivo",forma.getListaDocGarantiaMap("consecutivo_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("anioconsecutivo",forma.getListaDocGarantiaMap("anioconsecutivo_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("codigopaciente",forma.getListaDocGarantiaMap("codigopaciente_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("institucion",forma.getListaDocGarantiaMap("institucion_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("fechageneracion",forma.getListaDocGarantiaMap("fechageneracion_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("horageneracion",forma.getListaDocGarantiaMap("horageneracion_"+forma.getIndexDocumentosMap()));
					
		forma.setDocumentosGarantiaMap("tipodocumento",forma.getListaDocGarantiaMap("tipodocumento_"+forma.getIndexDocumentosMap()));
		forma.setDocumentosGarantiaMap("estado",forma.getListaDocGarantiaMap("estado_"+forma.getIndexDocumentosMap()));
		
		forma.setDocumentosGarantiaMap("usuariomodifica",usuario.getLoginUsuario());
		forma.setDocumentosGarantiaMap("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setDocumentosGarantiaMap("horamodifica",UtilidadFecha.getHoraActual());
		
		forma.setDocumentosGarantiaMap("motivoanulacion",forma.getListaDocGarantiaMap("motivoanulacion_"+forma.getIndexDocumentosMap()));
		
		forma.setDocumentosGarantiaMap("estabd",ConstantesBD.acronimoSi);
		
		if(forma.getListaDocGarantiaMap("tipodocumento_"+forma.getIndexDocumentosMap()).toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
		{
			if(!ValoresPorDefecto.getFormatoDocumentosGarantia_Pagare(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoFormatoDocGarantiaShaio))
			{
				forma.setImprimePagare(false);
				forma.setMensaje("Sin formato definido no se puede realizar impresiï¿½n");
			}
			forma.setDocumentosGarantiaMap("nameForward","infoPagare");
			return mapping.findForward("infoPagare");
		}

		if(forma.getListaDocGarantiaMap("tipodocumento_"+forma.getIndexDocumentosMap()).toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoCheque))
		{		
			//datos propios del cheque
			forma.setDocumentosGarantiaMap("entidadfinanciera",forma.getListaDocGarantiaMap("entidadfinanciera_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("numerocuenta",forma.getListaDocGarantiaMap("numerocuenta_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("numerodocumento",forma.getListaDocGarantiaMap("numerodocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("fechadocumento",forma.getListaDocGarantiaMap("fechadocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("giradordocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("valor",forma.getListaDocGarantiaMap("valor_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("clavecovinoc",forma.getListaDocGarantiaMap("clavecovinoc_"+forma.getIndexDocumentosMap()));			
			
			forma.setDocumentosGarantiaMap("nameForward","infoCheque");
			return mapping.findForward("infoCheque");		
		}
		
		if(forma.getListaDocGarantiaMap("tipodocumento_"+forma.getIndexDocumentosMap()).toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
		{		
			//datos propios del Letra de Cambio			
			forma.setDocumentosGarantiaMap("numerodocumento",forma.getListaDocGarantiaMap("numerodocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("fechadocumento",forma.getListaDocGarantiaMap("fechadocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("giradordocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("valor",forma.getListaDocGarantiaMap("valor_"+forma.getIndexDocumentosMap()));						
			
			forma.setDocumentosGarantiaMap("nameForward","infoLetra");
			return mapping.findForward("infoLetra");		
		}
		
		if(forma.getListaDocGarantiaMap("tipodocumento_"+forma.getIndexDocumentosMap()).toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoVaucher))
		{		
			//datos propios del Voucher
			forma.setDocumentosGarantiaMap("entidadfinanciera",forma.getListaDocGarantiaMap("entidadfinanciera_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("numerocuenta",forma.getListaDocGarantiaMap("numerocuenta_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("numerodocumento",forma.getListaDocGarantiaMap("numerodocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("fechadocumento",forma.getListaDocGarantiaMap("fechadocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("giradordocumento_"+forma.getIndexDocumentosMap()));
			forma.setDocumentosGarantiaMap("valor",forma.getListaDocGarantiaMap("valor_"+forma.getIndexDocumentosMap()));					
			
			forma.setDocumentosGarantiaMap("nameForward","infoVoucher");
			return mapping.findForward("infoVoucher");		
		}
		
		
		return null;
	}
	
	
	/**
	 * Distribulle las acciones dependiendo del tipo de documento (Nuevo Documento) 
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public ActionForward accionCargarInterfazNuevoDocumentoGarantia(DocumentosGarantiaForm forma,															    
															   UsuarioBasico usuario, 
															   ActionMapping mapping )
	{		
			
		//parametros comunes a todos los documentos
		forma.setDocumentosGarantiaMap("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));
		forma.setDocumentosGarantiaMap("consecutivoingreso",forma.getIngresosMap("consecutivoingreso_"+forma.getIndexIngresosMap()));		
		forma.setDocumentosGarantiaMap("codigopaciente",forma.getIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap()));
		forma.setDocumentosGarantiaMap("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));
		forma.setDocumentosGarantiaMap("fechageneracion",UtilidadFecha.getFechaActual());
		forma.setDocumentosGarantiaMap("horageneracion",UtilidadFecha.getHoraActual());
		
		forma.setDocumentosGarantiaMap("usuariomodifica",usuario.getLoginUsuario());
		forma.setDocumentosGarantiaMap("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setDocumentosGarantiaMap("horamodifica",UtilidadFecha.getHoraActual());
		
		forma.setDocumentosGarantiaMap("motivoanulacion","");
		
		forma.setDocumentosGarantiaMap("tipodocumento",forma.getDocumentosGarantiaMap("nuevoTipoDocumento"));
		forma.setDocumentosGarantiaMap("estado",forma.getDocumentosGarantiaMap("nuevoEstado"));
		forma.setDocumentosGarantiaMap("estabd",ConstantesBD.acronimoNo);
		
		
		//Tipo de Documento Pagare
		if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
		{
			forma.setDocumentosGarantiaMap("nameForward","infoPagare");
			return mapping.findForward("infoPagare");
		}	
			
		//Tipo de Documento Cheque
		if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoCheque))
		{
			//datos propios del cheque
			forma.setDocumentosGarantiaMap("entidadfinanciera","");
			forma.setDocumentosGarantiaMap("numerocuenta","");
			forma.setDocumentosGarantiaMap("numerodocumento","");
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.getFechaActual());
			
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("primerNombreDeu").toString()+" "+
					forma.getListaDocGarantiaMap("segundoNombreDeu").toString()+" "+forma.getListaDocGarantiaMap("primerApellidoDeu").toString()+
					forma.getListaDocGarantiaMap("segundoApellidoDeu").toString());
			
			forma.setDocumentosGarantiaMap("valor","");
			forma.setDocumentosGarantiaMap("clavecovinoc","");
			
			forma.setDocumentosGarantiaMap("nameForward","infoCheque");
			return mapping.findForward("infoCheque");
		}	
		
		//Tipo de Documento Letra de Cambio 
		if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
		{
			//datos propios del Letra			
			forma.setDocumentosGarantiaMap("numerodocumento","");
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.getFechaActual());
			
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("primerNombreDeu").toString()+" "+
					forma.getListaDocGarantiaMap("segundoNombreDeu").toString()+" "+forma.getListaDocGarantiaMap("primerApellidoDeu").toString()+
					forma.getListaDocGarantiaMap("segundoApellidoDeu").toString());
			
			forma.setDocumentosGarantiaMap("valor","");	
			
			forma.setDocumentosGarantiaMap("nameForward","infoLetra");
			return mapping.findForward("infoLetra");
		}
		
		//Tipo de Documento Voucher
		if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoVaucher))
		{
			//datos propios del Voucher
			forma.setDocumentosGarantiaMap("entidadfinanciera","");
			forma.setDocumentosGarantiaMap("numerocuenta","");
			forma.setDocumentosGarantiaMap("numerodocumento","");
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.getFechaActual());
			
			forma.setDocumentosGarantiaMap("giradordocumento",forma.getListaDocGarantiaMap("primerNombreDeu").toString()+" "+
					forma.getListaDocGarantiaMap("segundoNombreDeu").toString()+" "+forma.getListaDocGarantiaMap("primerApellidoDeu").toString()+
					forma.getListaDocGarantiaMap("segundoApellidoDeu").toString());
			
			forma.setDocumentosGarantiaMap("valor","");			
			
			forma.setDocumentosGarantiaMap("nameForward","infoVoucher");
			return mapping.findForward("infoVoucher");
		}
		
		return null;	
	}
	
	
	
	/**
	 * Modifica la informacion de Un Documento en Garantia
	 * @param paciente 
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping 
	 * */
	public ActionErrors accionInterfazGuardarDocumentoGarantia(DocumentosGarantiaForm forma,
															   Connection con, 
															   UsuarioBasico usuario, 
															   ActionErrors errores, PersonaBasica paciente)
	{			
		String estado = "";		

		//**************************************************Validaciones
		
		//Validaciones Generales antes de Guardar los Documentos de Garantia
		if(forma.getDocumentosGarantiaMap("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
		{
			if(forma.getDocumentosGarantiaMap("motivoanulacion").toString().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El Motivo de Anulacion "));
			}
			forma.setDocumentosGarantiaMap("usuarioanula",usuario.getLoginUsuario());
			forma.setDocumentosGarantiaMap("fechaanula",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			forma.setDocumentosGarantiaMap("horaanula",UtilidadFecha.getHoraActual());
		}
		
		
		if(forma.getDocumentosGarantiaMap("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) &&
			(forma.getDocumentosGarantiaMap("estabd").equals(ConstantesBD.acronimoNo)))
		{
			errores.add("descripcion",new ActionMessage("errors.invalid","El Estado de Creacion de Documento en Garantia "));
		}
		
		//validaciones para el Tipo de Documento en Garantia cheque
		if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoCheque))
		{
			if(forma.getDocumentosGarantiaMap("entidadfinanciera").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Entidad Financiera "));
			
			
			if(forma.getDocumentosGarantiaMap("numerocuenta").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero de Cuenta "));
			else
			{
				try
				{
					Double.parseDouble(forma.getDocumentosGarantiaMap("numerocuenta").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Numerico, Numero de Cuenta "));
				}
			}
			
			if(!UtilidadFecha.validarFecha(forma.getDocumentosGarantiaMap("fechadocumento").toString()))
				errores.add("descripcion",new ActionMessage("errors.invalid","Fecha del Documento "));
			
			if(forma.getDocumentosGarantiaMap("numerodocumento").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero del Cheque "));
			else
			{
				try
				{
					Double.parseDouble(forma.getDocumentosGarantiaMap("numerodocumento").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Numerico, Numero del Cheque "));
				}
			}
		}	
			
		
		//validaciones para el Tipo de Documento en Garantia Letra de Cambio
		if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
		{				
						
			if(forma.getDocumentosGarantiaMap("numerodocumento").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero de Letra de Cambio "));
			else
			{
				try
				{
					Double.parseDouble(forma.getDocumentosGarantiaMap("numerodocumento").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Numerico, Numero de Letra de Cambio "));
				}
			}
			
			if(!UtilidadFecha.validarFecha(forma.getDocumentosGarantiaMap("fechadocumento").toString()))
				errores.add("descripcion",new ActionMessage("errors.invalid","Fecha del Documento "));		
		
			if(forma.getDocumentosGarantiaMap("fechadocumento").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha del Letra de Cambio  "));
			
			if(forma.getDocumentosGarantiaMap("giradordocumento").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Girador "));
			
			if(forma.getDocumentosGarantiaMap("valor").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Valor de la Letra de Cambio "));			
			else
			{
				try
				{
					 Double.parseDouble(forma.getDocumentosGarantiaMap("valor").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Flotante, Valor de la Letra de Cambio "));
				}				
			}
		}
		
		
		//validaciones para el Tipo de Documento en Garantia Voucher
		if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoVaucher))
		{
			if(forma.getDocumentosGarantiaMap("entidadfinanciera").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Entidad Financiera "));
			
			
			if(forma.getDocumentosGarantiaMap("numerocuenta").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero de Cuenta "));
			else
			{
				try
				{
					Double.parseDouble(forma.getDocumentosGarantiaMap("numerocuenta").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Numerico, Numero de Cuenta "));
				}
			}
			
			
			if(!UtilidadFecha.validarFecha(forma.getDocumentosGarantiaMap("fechadocumento").toString()))
				errores.add("descripcion",new ActionMessage("errors.invalid","Fecha del Documento "));
			
			
			if(forma.getDocumentosGarantiaMap("numerodocumento").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero del Vaucher "));
			else
			{
				try
				{
					Double.parseDouble(forma.getDocumentosGarantiaMap("numerodocumento").toString());
				}
				catch(Exception e)
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Valor Numerico, Numero del Vaucher "));
				}
			}
		}
		
		
		
		//retorna los errores antes del ingreso en BD
		if(!errores.isEmpty())
			return errores;
		
		
		if(forma.getDocumentosGarantiaMap("estabd").equals(ConstantesBD.acronimoNo))
			estado = "operacionTrue";
			
		if(forma.getDocumentosGarantiaMap("estabd").equals(ConstantesBD.acronimoSi))
			estado = "modificacionTrue";		
		
		forma.setDocumentosGarantiaMap("fechageneracion",UtilidadFecha.conversionFormatoFechaABD(forma.getDocumentosGarantiaMap("fechageneracion").toString()));
		forma.setDocumentosGarantiaMap("garantiaIngreso",ConstantesBD.acronimoSi);
		forma.setDocumentosGarantiaMap("cartera",ConstantesBD.acronimoNo);
		
		//Tipo de Documento Pagare 
		if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
		{									
			forma.setDocumentosGarantiaMap("nombreConsecutivo",ConstantesBD.nombreConsecutivoPagare);						
			//se almacena la informacion en Base de Datos
			if(accionRegistrarDocumentoGarantia(forma.getDocumentosGarantiaMap(),con, usuario, paciente,forma))
				forma.setEstado(estado);				
		}
		
		else if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoCheque))
		{									
			forma.setDocumentosGarantiaMap("nombreConsecutivo",ConstantesBD.nombreConsecutivoCheque);
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.conversionFormatoFechaABD(forma.getDocumentosGarantiaMap("fechadocumento").toString()));
			
			//se almacena la informacion en Base de Datos
			if(accionRegistrarDocumentoGarantia(forma.getDocumentosGarantiaMap(),con, usuario, paciente,forma))
				forma.setEstado(estado);				
		}
		
		else if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
		{									
			forma.setDocumentosGarantiaMap("nombreConsecutivo",ConstantesBD.nombreConsecutivoLetraCambio);
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.conversionFormatoFechaABD(forma.getDocumentosGarantiaMap("fechadocumento").toString()));
			
			//se almacena la informacion en Base de Datos
			if(accionRegistrarDocumentoGarantia(forma.getDocumentosGarantiaMap(),con, usuario, paciente,forma))
				forma.setEstado(estado);				
		}
		
		else if(forma.getDocumentosGarantiaMap("tipodocumento").toString().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoVaucher))
		{									
			forma.setDocumentosGarantiaMap("nombreConsecutivo",ConstantesBD.nombreConsecutivoVoucher);
			forma.setDocumentosGarantiaMap("fechadocumento",UtilidadFecha.conversionFormatoFechaABD(forma.getDocumentosGarantiaMap("fechadocumento").toString()));
			
			//se almacena la informacion en Base de Datos
			if(accionRegistrarDocumentoGarantia(forma.getDocumentosGarantiaMap(),con, usuario, paciente,forma))
				forma.setEstado(estado);				
		}

				
		return errores;		
	}
	
	
	
	/**
	 * Guarda registros en Documentos en Garantia
	 * @param paciente 
	 * @param usuario 
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public boolean accionRegistrarDocumentoGarantia(HashMap parametros,
													Connection con, 
													UsuarioBasico usuario, 
													PersonaBasica paciente,
													DocumentosGarantiaForm forma)
	{
		String estadoIngreso;
		ArrayList filtro = new ArrayList();
		
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		estadoIngreso = UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,Integer.parseInt(parametros.get("ingreso").toString())).getAcronimo();
		
		//Crea un Nuevo Registro
		if(parametros.get("estabd").equals(ConstantesBD.acronimoNo))
		{
//			consulta el consecutivo definitivo
			String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(parametros.get("nombreConsecutivo").toString(),Integer.parseInt(parametros.get("institucion").toString()));
			String anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(parametros.get("nombreConsecutivo").toString(),Integer.parseInt(parametros.get("institucion").toString()), consecutivo);
			
			parametros.put("consecutivo",consecutivo);
			parametros.put("anioconsecutivo",anioConsecutivo);	
		
			
			filtro.add(parametros.get("nombreConsecutivo").toString());
			filtro.add(parametros.get("institucion").toString());
			
			
			transacction = DocumentosGarantia.insertarDocumentoGarantia(con, parametros)>0;									
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,parametros.get("nombreConsecutivo").toString(), Integer.parseInt(parametros.get("institucion").toString()), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		}
		else if(parametros.get("estabd").equals(ConstantesBD.acronimoSi))
		{
			transacction = DocumentosGarantia.actualizarDocumentoGarantia(con, parametros);				
		}
				
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% DOCUMENTOS GARANTIA");
			forma.setRefrescarPaciente(false);
			
			//********************SE CARGA EL PACIENTE*******************************************
			if(parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) ||
					parametros.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoPolizaVigente))
			{			
				if(estadoIngreso.equals(ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias))
				{					
					forma.setRefrescarPaciente(true);
					ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
					try 
					{
						paciente.cargar(con, paciente.getCodigoPersona());
						paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
					} 
					catch (Exception e) 
					{
						logger.info("Error en accionDetalle: "+e);
					}
					observable.addObserver(paciente);
					UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());				
				}
			}			
			//***********************************************************************************			
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return false;
		}	
	}
	
	
	
	
	
	/**
	 * Valida la Generacion del nuevo documento
	 * @param DocumentosGarantiaForm forma
	 * @param ActionErrors errores
	 * */
	public ActionErrors accionValidarRegistroDocumento(DocumentosGarantiaForm forma, 
													   Connection con, 
													   ActionErrors errores, 
													   int institucion)
	{
		int numRegistros = Integer.parseInt(forma.getListaDocGarantiaMap("numRegistros").toString());
		String consecutivo="";
		String anioConsecutivo="";
		
		if(!forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals("") && !forma.getDocumentosGarantiaMap("nuevoEstado").equals(""))
		{
			
			
			//consulta y verifica que exista el numero de consecutivo del documento 
			if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoCheque))
			{
				consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCheque,institucion);
				anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoCheque, institucion);

			}
			else if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
			{
				consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoLetraCambio,institucion);
				anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoLetraCambio, institucion);

			}
			
			else if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
			{
				consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoPagare,institucion);
				anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoPagare, institucion);

			}
			else if(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").equals(ConstantesIntegridadDominio.acronimoTipoDocumentoVaucher))
			{
				consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoVoucher,institucion);
				anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoVoucher, institucion);

			}
			
			//se adjudica temporalmente el consecutivo del documento de garantia, al momento de guardar se volvera a consultar
			if(UtilidadTexto.isEmpty(consecutivo) || consecutivo.equals("-1"))			
				errores.add("descripcion",new ActionMessage("errors.required","No Existe Consecutivo Creado para el Documento de Garantia Tipo >> " 
						+ValoresPorDefecto.getIntegridadDominio(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").toString())));
			else
			{
				forma.setDocumentosGarantiaMap("consecutivo",consecutivo);
				forma.setDocumentosGarantiaMap("anioconsecutivo",anioConsecutivo);
			}
			
					
			// valida que no exista otro tipo de documento igual al nuevo con estado distinto a Anulado
			for(int i = 0; i<numRegistros; i++)
			{
				if((forma.getListaDocGarantiaMap("tipodocumento_"+i).equals(forma.getDocumentosGarantiaMap("nuevoTipoDocumento").toString())) 
						&& (!forma.getListaDocGarantiaMap("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)))
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","Ya Existe un Documento en Garantia de Tipo "+
							ValoresPorDefecto.getIntegridadDominio(forma.getListaDocGarantiaMap("tipodocumento_"+i).toString())+".  "));
				}
			}
		}
		else
			errores.add("descripcion",new ActionMessage("errors.required","Debe Ingresar el Tipo y Estado del Documento de Garantia"));
		
		return errores;
	}

	
	
	/**
	 * Mï¿½todo que realiza la impresiï¿½n del voucher Cheque Letra Acta y la cartad e compromiso de documentos de garantia
	 * @param con
	 * @param ingresoPacienteForm
	 * @param mapping
	 * @param request
	 * @param usuarioActual 
	 * @return
	 */
	private ActionForward accionImprimirVoucherChequeLetraActa(Connection con, DocumentosGarantiaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual,String estado,int index,String texto) 
	{
		logger.info("entre a accionImprimirVoucherChequeLetraActa --> "+index);
		String nombreRptDesign="",forward="",tipo="";
		
		if (estado.equals("imprimirVoucher"))
		{
			nombreRptDesign = "docgarantiaVoucher1.rptdesign";
			tipo="impresion";
		}
		else
			if (estado.equals("imprimirCheque"))
			{
				nombreRptDesign = "docgarantiaCheque1.rptdesign";
				tipo="impresion";
			}
			else
				if (estado.equals("imprimirLetraCambio"))
				{
					nombreRptDesign = "docgarantiaLetraCambio1.rptdesign";
					tipo="impresion";
				}
				else
					if (estado.equals("imprimirActaCompromiso"))
					{
						nombreRptDesign = "docgarantiaActaCompromiso.rptdesign";
						tipo="impresion";
					}
					else
						if (estado.equals("imprimirpagare"))
						{
							nombreRptDesign = "docgarantiaPagare1.rptdesign";
							tipo="impresion";
						}
						else
							if (estado.equals("imprimirCartaInst"))
							{
								nombreRptDesign = "docgarantiaCartaInst.rptdesign";
								tipo="impresion";
							}
							else
								if (estado.equals("imprimirChequeConsulta"))
								{
									nombreRptDesign = "docgarantiaCheque1.rptdesign";
									tipo="detalle";
								}
								else
									if (estado.equals("imprimirActaCompromisoConsulta"))
									{
										nombreRptDesign = "docgarantiaActaCompromiso.rptdesign";
										tipo="detalle";
									}
									else 
										if (estado.equals("imprimirVoucherConsulta"))
										{
											nombreRptDesign = "docgarantiaVoucher1.rptdesign";
											tipo="detalle";
										}
										else
											if (estado.equals("imprimirLetraCambioConsulta"))
											{
												nombreRptDesign = "docgarantiaLetraCambio1.rptdesign";
												tipo="detalle";
											}
											else
												if (estado.equals("imprimirpagareConsulta"))
												{
													nombreRptDesign = "docgarantiaPagare1.rptdesign";
													tipo="detalle";
												}
												else
													if (estado.equals("imprimirCartaInstConsulta"))
													{
														nombreRptDesign = "docgarantiaCartaInst.rptdesign";
														tipo="detalle";
													}
		
		
		forward= queDocEs(forma.getDocumentosGarantiaMap("tipodocumento").toString(),tipo);
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		logger.info("nombreRptDesign-->"+nombreRptDesign+" path reports->"+ParamsBirtApplication.getReportsPath());
		
		DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"carteraPaciente/",nombreRptDesign);
        
        /**
         * Realizar metodo en el DesignEngineApi el cual permita determinar el numero de columnas
         * del grid en el header principal para saber cual es el numero de columnas que posee el grid y
         * determinar el extremo de la ubicacion del logo en la derecha. Esto con el fin de que si el reporte no cumple con el 
         * estandar adoptado de 3 columnas
         */

        logger.info("Ubica Logo: " + institucionBasica.getUbicacionLogo());
        
        if(institucionBasica.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
        	// se pasa posicion 2 por el momento hasat que se realize el metodo descrito anteriormente,
        	// no es machete ya que se hablo para adoptar el estandar de tres columnas

            comp.insertImageHeaderOfMasterPage1(0, ConstantesBD.codigoUbicacionLogoDer, institucionBasica.getLogoReportes());
        }
        else {
        	comp.insertImageHeaderOfMasterPage1(0, ConstantesBD.codigoUbicacionLogoIzq, institucionBasica.getLogoReportes());
        }

        
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        
        
        //Informaciï¿½n de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(1, 0, "Usuario: " + usuarioActual.getLoginUsuario());

        /*se le quita el encabezado con la informacion de la institucion
         * Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        */
        
        if (estado.equals("imprimirCartaInst")||estado.equals("imprimirCartaInstConsulta"))
		{
        	comp.obtenerComponentesDataSet("cartaInstruc");
        	
        	comp.modificarQueryDataSet(ConsultasBirt.cartaInstruccionesGarantia(forma.getDocumentosGarantiaMap("ingreso")+""));
        	logger.info("query a imprimir: "+ConsultasBirt.cartaInstruccionesGarantia(forma.getDocumentosGarantiaMap("ingreso")+""));
		}
        if (estado.equals("imprimirActaCompromiso") || estado.equals("imprimirActaCompromisoConsulta") )
        {
        	ArrayList<String> consultas = ConsultasBirt.actaCompromisoCondicionesIngreso(
        		forma.getDocumentosGarantiaMap("ingreso").toString(), 
        		forma.getDocumentosGarantiaMap("tipodocumento").toString(), 
        		forma.getDocumentosGarantiaMap("consecutivo").toString(), 
        		forma.getDocumentosGarantiaMap("anioconsecutivo").toString());
        	
        	comp.obtenerComponentesDataSet("actaCompromiso");
        	comp.modificarQueryDataSet(consultas.get(0));
        	comp.obtenerComponentesDataSet("convenios");
        	comp.modificarQueryDataSet(consultas.get(1));
        	comp.obtenerComponentesDataSet("fechaGeneracion");
        	comp.modificarQueryDataSet(consultas.get(2));
        }
        if(estado.equals("imprimirpagare")||estado.equals("imprimirpagareConsulta"))
        {
        	comp.obtenerComponentesDataSet("pagare");
        	comp.modificarQueryDataSet(ConsultasBirt.docGarantiaPagare1(forma.getDocumentosGarantiaMap("ingreso").toString(), 
        		forma.getDocumentosGarantiaMap("tipodocumento").toString(), 
        		forma.getDocumentosGarantiaMap("estado").toString()));
        	
        }
        if(estado.equals("imprimirCheque")||estado.equals("imprimirChequeConsulta") || 
        	estado.equals("imprimirVoucher") || estado.equals("imprimirVoucherConsulta")||
        	estado.equals("imprimirLetraCambio") || estado.equals("imprimirLetraCambioConsulta"))
        {
        	comp.obtenerComponentesDataSet("documentosGarantia");
        	comp.modificarQueryDataSet(ConsultasBirt.docGarantiaCheque1(
        			forma.getDocumentosGarantiaMap("ingreso").toString(),
        			forma.getDocumentosGarantiaMap("consecutivo").toString(),
            		forma.getDocumentosGarantiaMap("tipodocumento").toString(),
            		forma.getDocumentosGarantiaMap("anioconsecutivo").toString(),
            		forma.getDocumentosGarantiaMap("institucion").toString()));
        }
        
        
        
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);

        logger.info("numero del ingreso: "+forma.getDocumentosGarantiaMap("ingreso"));
        //se mandan los parï¿½metros al Reporte
        newPathReport += "&ingreso="+forma.getDocumentosGarantiaMap("ingreso")+
        	"&consecutivo="+forma.getDocumentosGarantiaMap("consecutivo")+
        	"&tipoDocumento="+forma.getDocumentosGarantiaMap("tipodocumento")+
        	"&anioConsecutivo="+forma.getDocumentosGarantiaMap("anioconsecutivo")+
        	"&estado="+forma.getDocumentosGarantiaMap("estado")+
        	"&institucion="+forma.getDocumentosGarantiaMap("institucion")+
        	"&texto="+texto;
        	
        if(!newPathReport.equals("")) {
        	request.setAttribute("isOpenReport_"+index, "true");
        	request.setAttribute("newPathReport_"+index, newPathReport);
        }
        
        logger.info("3333333333333-->"+newPathReport);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward(forward);
	}
	
	
	
	/**
	 * Metodo encargado se identificar hacia donde va a hacer el forward el metodo
	 * accionImprimirVoucherChequeLetraActa
	 * @param tipo
	 * @param funcion
	 * @return nombre
	 */
	public String queDocEs (String tipo, String funcion)
	{
		/*
		logger.info("\n\n***************** ENTRO A queDocEs *********************");
		logger.info("\n*** el tipo es "+tipo);
		logger.info("\n*** la funcion es "+funcion);
		logger.info("\n***************** END ENTRO A queDocEs *********************\n\n\n");
		*/
		String nombre="";
		if (tipo.equals("PAGA") && funcion.equals("impresion"))
			nombre = "infoPagare";
		else
			if (tipo.equals("PAGA") && funcion.equals("detalle"))
				nombre = "detallePagare";
		
		if (tipo.equals("CHEQ") && funcion.equals("impresion"))
			nombre = "infoCheque";
		else
			if (tipo.equals("CHEQ") && funcion.equals("detalle"))
			nombre = "detalleCheque";
		
		if (tipo.equals("LETR") && funcion.equals("impresion"))
			nombre = "infoLetra";
		else
			if (tipo.equals("LETR") && funcion.equals("detalle"))
			nombre = "detalleLetra";
		
		if (tipo.equals("VAUC") && funcion.equals("impresion"))
			nombre = "infoVoucher";
		else
			if (tipo.equals("VAUC") && funcion.equals("detalle"))
			nombre = "detalleVoucher";
			else
				if (funcion.equals("detalledeudorco"))
					nombre = "detalleDeudorCo";
		
		return nombre;
	}
	
	
	/*-----------------------------------------------------
	 * FIN METODOS DOCUMENTOS GARANTIA  
	 * ---------------------------------------------------*/
	
	
	
	
	
	
	
	/*-----------------------------------------------------
	 * METODOS INGRESO PACIENTE 
	 * ---------------------------------------------------*/
		
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar,
									 DocumentosGarantiaForm forma)
	{			
		
		String[] indices = (String[])mapaOrdenar.get("INDICES_MAPA");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);	
		
		return mapaOrdenar;
	}	
	
	
	
	/**
	 * Accion inicial del registro del DeudorCo
	 * @param request 
	 * @param DocumentosGarantiaForm forma
	 * @param ActionMapping mapping
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @return
	 * */
	public ActionErrors accionListarIngreso(DocumentosGarantiaForm forma,
									   ActionMapping mapping,
									   Connection con,
									   UsuarioBasico usuario,
									   PersonaBasica paciente,
									   String claseDeudorCo, 
									   ActionErrors errores)
	{		
		forma.resetDeudorCo();
		forma.setFuncionalidadDummy(false);                                                                                      
		forma.setOcultarEncabezado(false);
		forma.setSoloDocumentosVigentes(false);
		
		HashMap parametros = new HashMap();
		
		
		if(!claseDeudorCo.equals("RegiDocumento"))
			forma.setValorClaseDeudorCo(claseDeudorCo);
		
		parametros.put("codigoPaciente",paciente.getCodigoPersona());		
		parametros.put("centroAtencion",usuario.getCodigoCentroAtencion());
		
		forma.setIngresosMap(DocumentosGarantia.consultarIngresos(con, parametros));
		
		
		if(Integer.parseInt(forma.getIngresosMap("numRegistros").toString()) <= 0)
		{
			if(forma.getIngresosMap("error").toString().equals("errorIngresos"))
			{
				errores.add("descripcion",new ActionMessage("error.errorEnBlanco","Paciente con Ingresos en Estados No Validos para Ingreso de Funcionalidad"));				
			}
			else if(forma.getIngresosMap("error").toString().equals("ErrorCuentaValida"))
			{
				errores.add("descripcion",new ActionMessage("error.errorEnBlanco","Paciente con Cuentas No Validas para Ingreso de Funcionalidad "));
			}
		}		
		
		return errores;
	}
	
	
	
	/**
	 * validaciones de acceso
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(PersonaBasica paciente, 
													ActionMapping mapping, 
													HttpServletRequest request, 
													Connection con)
	{		
		if(paciente.getCodigoPersona()<1)
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		
		return null;
	}	
	
	
	
	
	
	/*-----------------------------------------------------
	 * METODOS DEUDOR CO 
	 * ---------------------------------------------------*/	
	
	
	//**************************************************************************************	
	//************************************************Funciones De Validacion
	//**************************************************************************************
	//**************************************************************************************
	/**
	 * Mï¿½todo implementado para ir a la funcionalidad deudor / codeudor desde otras funcionalidades
	 * @param con
	 * @param forma
	 * @param errores
	 * @param mapping
	 * @param usuario 
	 * @param request
	 * @return
	 */
	private ActionForward accionIrTipoDeudorCoDummy(
													Connection con, 
													DocumentosGarantiaForm forma, 
													ActionErrors errores, 
													ActionMapping mapping, 
													UsuarioBasico usuario, 
													HttpServletRequest request) 
	{
		forma.resetDeudorCo();
		forma.setFuncionalidadDummy(true);
		forma.setOcultarEncabezado(true);
		
		forma.setIndexIngresosMap("0");
		forma.setIngresosMap("ingreso_"+forma.getIndexIngresosMap(), forma.getIdIngreso());
		forma.setIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap(), forma.getCodigoPaciente());
		forma.setIngresosMap("institucion_"+forma.getIndexIngresosMap(), forma.getInstitucion());
		forma.setIngresosMap("datosResponsablePaciente_"+forma.getIndexIngresosMap(), forma.getDatosResponsablePaciente());
		forma.setIngresosMap("numeroCuenta_"+forma.getIndexIngresosMap(), forma.getIdCuenta());
		
		logger.info("valorClaseDeudor=> "+forma.getValorClaseDeudorCo());
		
		//carga la informacion de la Clase de DeudorCo (Deudor o Codeudor)				
		errores = this.accionCargarDeudorCoExistente(forma, con, usuario, errores);
		 
		if(!errores.isEmpty())
		{
			forma.setEstado("error");
			saveErrors(request,errores);	
			 UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}										
		
		//valida si se puede modifica el tipo de Deudor Co (paciente,responsable paciente, otro), 
		//si es posible modificarlo envia a la pagia de tipoDeudorCo, si no lo envia directamente
		//a la pagina del DeudorCo principal
		UtilidadBD.closeConnection(con);
		if(accionMostrarPaginaTipoDeudor(forma))
			return mapping.findForward("tipoDeudorCo");
		else
		{
			//direcciona la salida dependiendo del action. principal es la vista de captura de 
			//informacion del deudorCo
			return mapping.findForward("principal"); 
		}	
	}
	
	
	
	/**
	 * Valida si es posible el cambio de tipo de deudor
	 * @param DocumentosGarantiaForm forma
	 * */
	public boolean accionMostrarPaginaTipoDeudor(DocumentosGarantiaForm forma)
	{
		if(Integer.parseInt(forma.getDeudorCoMap("numRegistros").toString())>0)
		{
			if((forma.getDeudorCoMap("existenDocsGarantia").toString().equals(ConstantesBD.acronimoNo)) || 
					(forma.getDeudorCoMap("existenDocsGarantia").toString().equals(ConstantesBD.acronimoSi) &&
							(forma.getDeudorCoMap("esModInfoDeudorCo").toString().equals(ConstantesBD.acronimoSi)))
					)
			{	
				forma.setDeudorCoMap("numeroIdentificacionNew","");
				forma.setDeudorCoMap("tipoIdentificacionNew","");
				return true;
				
			}	
		}
		else	
		{
			forma.setDeudorCoMap("numeroIdentificacionNew","");
			forma.setDeudorCoMap("tipoIdentificacionNew","");			
			return true;
		}	
		
		return false;		
	}
	
	
	
	
	/**
	 * Valida que la informacion del deudor tipo Otro se ha valida, esta funcion es 
	 * llamada solo si existe informacion del DeudorCo almancenada
	 * @param DocumentosGarantiaForm forma
	 * @param ActionErrors errores
	 * @param PersonaBasica persona
	 * */
	public ActionErrors validarCambioTipoDeudorCo(DocumentosGarantiaForm forma, ActionErrors errores, PersonaBasica persona)
	{					
		///valida que la informacion del deudor no sea la misma del Codeudor
		if(!validarIgualdadDeudorCodeudor(forma))
		{
			errores.add("descripcion",new ActionMessage("errors.invalid","El Deudor y Codeudor No Pueden Ser Iguales. Tipo Deudor "));
		}
		
		//si el tipo escogido fue Responsable Paciente 
		if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoResponsablePaci)) 			
		{
			//si existe informacion de Responsable Paciente
			if(forma.getDeudorCoMap("existeResponsablePaciente").toString().equals(ConstantesBD.acronimoNo))
			{
				//verfica que la informacion ingresda no se ha igual a la informacion del paciente cargado en session		
				if((forma.getDeudorCoMap("tipoIdentificacionNew").toString()+ConstantesBD.separadorSplit+forma.getDeudorCoMap("numeroIdentificacionNew")).toString().equals(persona.getCodigoTipoIdentificacionPersona()+ConstantesBD.separadorSplit+persona.getNumeroIdentificacionPersona()))
				{					
					errores.add("descripcion",new ActionMessage("errors.invalid","El tipo y numero de Identificacion es la misma para el Paciente. Tipo Deudor "));
				}				
			}
			else
			{				
				forma.setDeudorCoMap("tipoIdentificacion",forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[0]);
				forma.setDeudorCoMap("numeroIdentificacion",forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[1]);
			}
		}	
					
		//si el tipo de Deudor fue Otro
		if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro)) 			
		{
			//si existe informacion del ResponsablePaciente
			if(forma.getDeudorCoMap("existeResponsablePaciente").toString().equals(ConstantesBD.acronimoSi))
			{
				if(((forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[0]).toString().equals(forma.getDeudorCoMap("tipoIdentificacionNew").toString())) &&
					((forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[1]).toString().equals(forma.getDeudorCoMap("numeroIdentificacionNew").toString())))
					{						
						errores.add("descripcion",new ActionMessage("errors.invalid","El tipo y numero de Identificacion es la misma para el Responsable Paciente. Tipo Deudor "));
					}
			}
						
			//verifica que la informacion ingresada no se ha igual a la informacion del paciente cargado en session			
			if((forma.getDeudorCoMap("tipoIdentificacionNew").toString()+ConstantesBD.separadorSplit+forma.getDeudorCoMap("numeroIdentificacionNew")).toString().equals(persona.getCodigoTipoIdentificacionPersona()+ConstantesBD.separadorSplit+persona.getNumeroIdentificacionPersona()))
			{					
				errores.add("descripcion",new ActionMessage("errors.invalid","El tipo y numero de Identificacion es la misma para el Paciente. Tipo Deudor"));
			}
			
			//verifica que la informacion del tipo deudor otro no se ha el mismo que el DeudorCo existente
			if(forma.getDeudorCoMap("existeCodeudorOdeudor").equals(ConstantesBD.acronimoSi))
			{				
				if((forma.getDeudorCoMap("tipoIdentificacionNew").toString()+ConstantesBD.separadorSplit+forma.getDeudorCoMap("numeroIdentificacionNew")).toString().equals(forma.getDeudorCoMap("tipoIdentificacionCodeudorOdeudor").toString()+ConstantesBD.separadorSplit+forma.getDeudorCoMap("numeroIdentificacionCodeudorOdeudor")))
				{					
					if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoDeudor))
							errores.add("descripcion",new ActionMessage("errors.invalid","El tipo y numero de Identificacion es la misma para el Co Deudor. Informacion "));
					else if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoCoDeudor))
							errores.add("descripcion",new ActionMessage("errors.invalid","El tipo y numero de Identificacion es la misma para el Deudor. Informacion "));
				}				
			}
		}		
	
		logger.info("--------------Fin Validadacion Cambio Tipo Deudor ");
		
		if(!errores.isEmpty())
		{
			//si existe un error retorna al valor anterior del TipoDeudorCo
			if(forma.getDeudorCoMap().containsKey("tipoDeudorCoOld"))						
				forma.setDeudorCoMap("tipoDeudorCo",forma.getDeudorCoMap("tipoDeudorCoOld").toString());
		}
		
		return errores;	
	}
	
	
	
	
	/**
	 * verifica que las personas definias en el Codeudor no sea la misma 
	 * definida en el Deudor y viciversa   
	 * @param DocumentosGarantiaForm forma 	 
	 * * */
	public boolean validarIgualdadDeudorCodeudor(DocumentosGarantiaForm forma)
	{			
		if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoDeudor) &&
				forma.getDeudorCoMap("estabd").toString().equals(ConstantesBD.acronimoSi))
		{
			if(forma.getDeudorCoMap("existeCodeudorOdeudor").equals(ConstantesBD.acronimoSi))
			{				
				//si el tipo de deudor es el mismo que el codeudor, devuelve false de error 
				if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(forma.getDeudorCoMap("tipoCodeudorOdeudor").toString())
						&& !forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					return false;															
				}				
			}
		}
		else if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoCoDeudor))
		{			
			//si el tipo de deudor es el mismo que el codeudor, devuelve false de error 
			if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(forma.getDeudorCoMap("tipoCodeudorOdeudor").toString())
					&& !forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				return false;															
			}
		}
		
		return true;
	}
	
	

	
	/**
	 * Prepara el HasMap para ser almacenado o Guardado
	 * @param DocumentoGarantiaForm
	 * */
	public void verificarGuardarModificar(DocumentosGarantiaForm forma)
	{
		String temp = forma.getDeudorCoMap("codigoCiudad").toString();
		
		forma.setDeudorCoMap("codigoCiudad",temp.split(ConstantesBD.separadorSplit)[1]);		
		forma.setDeudorCoMap("codigoDepartamento",temp.split(ConstantesBD.separadorSplit)[0]);	
		
		temp = forma.getDeudorCoMap("codigoCiudadReside").toString();
		
		forma.setDeudorCoMap("codigoCiudadReside",temp.split(ConstantesBD.separadorSplit)[1]);
		forma.setDeudorCoMap("codigoDepartamentoReside",temp.split(ConstantesBD.separadorSplit)[0]);
		
		forma.setDeudorCoMap("codigoBarrioReside",forma.getCodigoBarrio());
		
		forma.setDeudorCoMap("fechaNacimiento",UtilidadFecha.conversionFormatoFechaABD(forma.getDeudorCoMap("fechaNacimiento").toString()));
				
		forma.setDeudorCoMap("fechaVerificacion","");
		forma.setDeudorCoMap("usuarioVerificacion","");
		forma.setDeudorCoMap("observacionesVerificacion","");
		
		forma.setDeudorCoMap("fechaVeriRiesgos","");
		forma.setDeudorCoMap("usuarioVeriRiesgos","");
		forma.setDeudorCoMap("observacionesVeriRiesgos","");
	}	
	
	
	/**
	 * Validar la Edad de DeudorCo
	 * @param DocumentosGarantiaForm forma
	 * @param ActionErrors errores
	 * */
	public ActionErrors validarEdadDeudorCo(DocumentosGarantiaForm forma, ActionErrors errores)
	{
		//Validacion de Edad del Paciente, Segun Requerimiento se debe de realizar antes de mostrar la informacion		
		if(forma.getIndexIngresosMap()!= null && (forma.getDeudorCoMap().containsKey("fechaNacimiento") && forma.getIngresosMap().containsKey("institucion_"+forma.getIndexIngresosMap())))
		{
			if(!forma.getDeudorCoMap("fechaNacimiento").toString().equals("") )
			{
				if(ValoresPorDefecto.getValidarEdadDeudorPaciente(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString().equals(ConstantesBD.acronimoSi))
				{
					if(UtilidadFecha.calcularEdad(forma.getDeudorCoMap("fechaNacimiento").toString()) < Integer.parseInt(ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString()))
						errores.add("descripcion",new ActionMessage("errors.invalid","La Edad del Deudor o Codeudor debe ser Mayor a "+ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()).toString())).toString()+". La Edad "));
					else
						return errores;
				}
			}
		}	
		
		return errores;
	}
	
	
	
	
	
	//**************************************************************************************	
	//************************************************Funciones Carga de datos
	//**************************************************************************************
	//**************************************************************************************
	
	
	/**
	 * carga la informacion del DeudorCo
	 * @param Connection con
	 * @param DocumentosGarantiaForm forma 
	 * @param UsuarioBasico usuario
	 * */
	public ActionErrors accionCargarDeudorCoExistente(DocumentosGarantiaForm forma,
													  Connection con,
													  UsuarioBasico usuario,
													  ActionErrors errores)
	{
	
		
		
		HashMap parametros = new HashMap();
		HashMap parametrosAux = new HashMap();
		
		//parametros de busqueda 
		parametrosAux.put("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));
		parametrosAux.put("codigoPaciente",forma.getIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap()));
		parametrosAux.put("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));	
		
		//Clase de DeudorCo >> Deudor
		if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoDeudor))
		{
			parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoDeudor);
			
			//existe informacion del Deudor en la Base de Datos
			if(consultarDeudorCo(forma, con,parametrosAux))		
			{
				//valores por defecto para los datos de Codeudor del Deudor				
				forma.setDeudorCoMap("existeCodeudorOdeudor",ConstantesBD.acronimoNo);
				
				//consulta si el deudor posee codeudor y que tipo de DeudorCo es (paciente,acompaï¿½ante,otro) 
				parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoCoDeudor);
				parametros = DocumentosGarantia.consultarDependenciaDeudorCodeudor(con, parametrosAux);
								
				//existe Codeudor
				if(Integer.parseInt(parametros.get("numRegistros").toString())>0)
				{
					//se carga la informacion de Codeudor
					forma.setDeudorCoMap("existeCodeudorOdeudor",ConstantesBD.acronimoSi);
					forma.setDeudorCoMap("tipoCodeudorOdeudor",parametros.get("tipoDeudor"));					
					forma.setDeudorCoMap("tipoIdentificacionCodeudorOdeudor",parametros.get("tipoIdentificacion"));
					forma.setDeudorCoMap("numeroIdentificacionCodeudorOdeudor",parametros.get("numeroIdentificacion"));
				}
				
				//se replica el valor del tipo de deudor para verificar si este cambio al entrar al estado <<irTipoDeudorCo>>
				forma.setDeudorCoMap("tipoDeudorCoOld",forma.getDeudorCoMap("tipoDeudorCo").toString());
				
				//carga los datos para la consulta generica de barrio
				forma.setCodigoBarrio(forma.getDeudorCoMap("codigoBarrioReside").toString());
				forma.setNombreBarrio(forma.getDeudorCoMap("descripcionBarrioReside").toString());
			}
			else
			{
				//no existe informacion del Deudor en la Base de Datos
				forma.setDeudorCoMap("estabd",ConstantesBD.acronimoNo);
				forma.setDeudorCoMap("existeCodeudorOdeudor",ConstantesBD.acronimoNo);
			}							
		}		
				
		
		
		//Clase de DeudorCo >> Codeudor
		else if(forma.getValorClaseDeudorCo().toString().equals(ConstantesIntegridadDominio.acronimoCoDeudor))
		{
			//valores por defecto para los datos de Deudor del Codeudor				
			forma.setDeudorCoMap("existeCodeudorOdeudor",ConstantesBD.acronimoNo);
			
			//consulta si el Codeudor posee deudor y que tipo de DeudorCo es (paciente,acompaï¿½ante,otro) 
			parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoDeudor);
			parametros = DocumentosGarantia.consultarDependenciaDeudorCodeudor(con, parametrosAux);			
						
			//existe Deudor para el Codeudor
			if(Integer.parseInt(parametros.get("numRegistros").toString())>0)
			{									
				//parametros de busqueda
				parametrosAux.put("claseDeudorCo",ConstantesIntegridadDominio.acronimoCoDeudor);
				parametrosAux.put("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));
				parametrosAux.put("codigoPaciente",forma.getIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap()));
				parametrosAux.put("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));	

				//existe informacion del Codeudor en la Base de Datos
				if(consultarDeudorCo(forma, con,parametrosAux))		
				{
					//se replica el valor del tipo de deudor para verificar si este cambio al entrar al estado <<irTipoDeudorCo>>
					forma.setDeudorCoMap("tipoDeudorCoOld",forma.getDeudorCoMap("tipoDeudorCo").toString());
					
					//carga los datos para la consulta generica de barrio
					forma.setCodigoBarrio(forma.getDeudorCoMap("codigoBarrioReside").toString());
					forma.setNombreBarrio(forma.getDeudorCoMap("descripcionBarrioReside").toString());
				}	
				//no existe informacion del Deudor en la Base de Datos
				else
				{
					forma.setDeudorCoMap("estabd",ConstantesBD.acronimoNo);
				}
				
				//se carga la informacion de Deudor				
				forma.setDeudorCoMap("tipoCodeudorOdeudor",parametros.get("tipoDeudor"));					
				forma.setDeudorCoMap("tipoIdentificacionCodeudorOdeudor",parametros.get("tipoIdentificacion"));
				forma.setDeudorCoMap("numeroIdentificacionCodeudorOdeudor",parametros.get("numeroIdentificacion"));	
				//existe informacion del deudor				
				forma.setDeudorCoMap("existeCodeudorOdeudor",ConstantesBD.acronimoSi);
				
			}	
			//no existe deudor, genera un mensaje de Error no se puede crear un Codeudor si no Existe el Deudor
			else
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Informacion del Deudor "));
			}	
		}
		
				
		
		
		//si existe la informacion del ResponsablePaciente tomado de la consulta del ingreso y la cuenta 
		if(!forma.getIngresosMap("datosResponsablePaciente_"+forma.getIndexIngresosMap()).toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setDeudorCoMap("datosResponsablePaciente",forma.getIngresosMap("datosResponsablePaciente_"+forma.getIndexIngresosMap()));
			forma.setDeudorCoMap("existeResponsablePaciente",ConstantesBD.acronimoSi);
			//indica si existe informacion del Responsable en la Tabla Responsable pero no esta 
			//relacionado con la cuenta
			forma.setDeudorCoMap("existeResponsableHistorico",ConstantesBD.acronimoNo);			
		}	
		else
		{
			forma.setDeudorCoMap("existeResponsablePaciente",ConstantesBD.acronimoNo);			
			//no exite informacion del Responsable Paciente asociado a la cuenta
			forma.setDeudorCoMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
		}			
		
		
		
		//Carga los permisos sobre las funcionalidades 
		forma.setDeudorCoMap("funcionalidadDeudor",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),607)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		forma.setDeudorCoMap("funcionalidadCodeudor",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),608)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		forma.setDeudorCoMap("funcionalidadRegistrarDocumentos",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),609)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		forma.setDeudorCoMap("funcionalidadConsultarDocumentos",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),610)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//carga el HashMap de Tipo de Identificacion
		forma.setTipoIdentificacionMap(Utilidades.obtenerTiposIdentificacion(con,"",usuario.getCodigoInstitucionInt()));	
		
		//carga los paises
		forma.setPaisesMap(Utilidades.obtenerPaises(con));
		
		//carga las ciudades
		try
		{
			HibernateUtil.beginTransaction();
			forma.setCiudadesMap(AdministracionFabricaServicio.crearLocalizacionServicio().listarCiudades());	
			forma.setCiudadesResidenciaMap(AdministracionFabricaServicio.crearLocalizacionServicio().listarCiudades());
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			logger.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		return errores;		
	}
	

	
	
	
	
	
	/**
	 * Carga la informacion del DeudorCo dependiendo del tipo de DeudorCo escogido
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario 
	 * */
	public void cargarDeudorCoTipo(DocumentosGarantiaForm forma,
								   Connection con,
								   String opcion)
	{
		
		HashMap parametros = new HashMap();		
		
		
		//****************************************************************
		//si el tipo de DeudorCo es paciente		
		if(opcion.equals("paciente"))
		{
			parametros.put("codigoPaciente",forma.getIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap()));
			parametros = DocumentosGarantia.consultarDatosPaciente(con, parametros);		
		}
				
		
		//****************************************************************		
		//si el tipo de Deudor es Responsable Paciente
		else if(opcion.equals("responsable"))
		{			
			//si no existe la informacion del Responsable paciente, paso los parametros ingresados por el usuario
			//para realizar la busqueda en la tabla de Responsables Paciente
			if(forma.getDeudorCoMap("existeResponsablePaciente").toString().equals(ConstantesBD.acronimoNo))
			{
				parametros.put("tipoIdentificacion",forma.getDeudorCoMap("tipoIdentificacionNew").toString());
				parametros.put("numeroIdentificacion",forma.getDeudorCoMap("numeroIdentificacionNew").toString());
			}
			else
			{
				
				if(forma.getIngresosMap("codigoResponsablePaciente_"+forma.getIndexIngresosMap())!=null)
					parametros.put("codigoResponsablePaciente",forma.getIngresosMap("codigoResponsablePaciente_"+forma.getIndexIngresosMap()));
				else if(forma.getDeudorCoMap("datosResponsablePaciente")!=null&&!forma.getDeudorCoMap("datosResponsablePaciente").toString().equals(""))
				{
					parametros.put("tipoIdentificacion",forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[0]);
					parametros.put("numeroIdentificacion",forma.getDeudorCoMap("datosResponsablePaciente").toString().split(ConstantesBD.separadorSplit)[1]);
				}
				
				
			}	
				
			parametros = DocumentosGarantia.consultarResponsablePaciente(con, parametros);			
			
			if(Integer.parseInt(parametros.get("numRegistros").toString())==0)
			{
				parametros.put("tipoIdentificacion",forma.getDeudorCoMap("tipoIdentificacionNew").toString());
				parametros.put("numeroIdentificacion",forma.getDeudorCoMap("numeroIdentificacionNew").toString());				
				parametros.put("codigoPais","");
				parametros.put("codigoDepartamento","");
				parametros.put("codigoCiudad","");
				parametros.put("primerNombre","");		
				parametros.put("segundoNombre","");
				parametros.put("primerApellido","");
				parametros.put("segundoApellido","");
				parametros.put("direccionReside","");
				parametros.put("codigoPaisReside","");
				parametros.put("codigoDepartamentoReside","");		
				parametros.put("codigoCiudadReside","");
				parametros.put("codigoBarrioReside","");
				parametros.put("descripcionBarrioReside","");
				parametros.put("telefonoReside","");
				parametros.put("fechaNacimiento","");
				parametros.put("relacionPaciente","");
				
				//indica que la informacion del Responsable Paciente fue buscado en la tabla Responsable Paciente
				//pero no se encontro ningun registro historico
				forma.setDeudorCoMap("existeResponsableHistorico",ConstantesBD.acronimoNo);
			}		
			else
			{
				//indica que la informacion del Responsable Paciente fue buscada en la tabla Responsable Paciente
				//y se encontro, pero no esta relacionado a la cuenta
				forma.setDeudorCoMap("existeResponsableHistorico",ConstantesBD.acronimoSi);				
				forma.setDeudorCoMap("codigoResponsableHistorico",parametros.get("codigoResponsablePaciente"));
			}
		}		
			
		
		
		//***************************************************************
		//si el tipo de Deudor es Otro
		else if(opcion.equals("otro"))
		{
			//se consulta si el tipo y numero de identificacion se encuentra en la tabla DeudorCo			
			parametros.put("tipoIdentificacion",forma.getDeudorCoMap("tipoIdentificacionNew").toString());
			parametros.put("numeroIdentificacion",forma.getDeudorCoMap("numeroIdentificacionNew").toString());
			parametros.put("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));
			
			parametros = DocumentosGarantia.consultarDeudorCo(con, parametros);
			
			if(Integer.parseInt(parametros.get("numRegistros").toString())==0)
			{
				//inserta informacion vacia para el tipo DeudorCo Otro			
				parametros.put("tipoIdentificacion",forma.getDeudorCoMap("tipoIdentificacionNew").toString());
				parametros.put("numeroIdentificacion",forma.getDeudorCoMap("numeroIdentificacionNew").toString());
				parametros.put("codigoPais","");
				parametros.put("codigoDepartamento","");
				parametros.put("codigoCiudad","");
				parametros.put("primerNombre","");		
				parametros.put("segundoNombre","");
				parametros.put("primerApellido","");
				parametros.put("segundoApellido","");
				parametros.put("direccionReside","");
				parametros.put("codigoPaisReside","");
				parametros.put("codigoDepartamentoReside","");		
				parametros.put("codigoCiudadReside","");
				parametros.put("codigoBarrioReside","");
				parametros.put("descripcionBarrioReside","");
				parametros.put("telefonoReside","");
				parametros.put("fechaNacimiento","");
				parametros.put("relacionPaciente","");		
				parametros.put("datosResponsablePaciente","");
			}	
		}
			
		
		//****************************************************************
		//cargo el HashMap DeudorCo
		forma.setDeudorCoMap("ingreso",forma.getIngresosMap("ingreso_"+forma.getIndexIngresosMap()));
		forma.setDeudorCoMap("codigoPaciente",forma.getIngresosMap("codigoPaciente_"+forma.getIndexIngresosMap()));
		forma.setDeudorCoMap("institucion",forma.getIngresosMap("institucion_"+forma.getIndexIngresosMap()));			
		forma.setDeudorCoMap("claseDeudorCo",forma.getValorClaseDeudorCo());			
		
		//informacion dependiente de la persona
		forma.setDeudorCoMap("tipoIdentificacion",parametros.get("tipoIdentificacion"));
		forma.setDeudorCoMap("numeroIdentificacion",parametros.get("numeroIdentificacion"));
		forma.setDeudorCoMap("codigoPais",parametros.get("codigoPais"));
		forma.setDeudorCoMap("codigoDepartamento",parametros.get("codigoDepartamento"));
		forma.setDeudorCoMap("codigoCiudad",parametros.get("codigoCiudad"));
		forma.setDeudorCoMap("primerNombre",parametros.get("primerNombre"));		
		forma.setDeudorCoMap("segundoNombre",parametros.get("segundoNombre"));
		forma.setDeudorCoMap("primerApellido",parametros.get("primerApellido"));
		forma.setDeudorCoMap("segundoApellido",parametros.get("segundoApellido"));
		forma.setDeudorCoMap("direccionReside",parametros.get("direccionReside"));
		forma.setDeudorCoMap("codigoPaisReside",parametros.get("codigoPaisReside"));
		forma.setDeudorCoMap("codigoDepartamentoReside",parametros.get("codigoDepartamentoReside"));		
		forma.setDeudorCoMap("codigoCiudadReside",parametros.get("codigoCiudadReside"));
		forma.setDeudorCoMap("codigoBarrioReside",parametros.get("codigoBarrioReside"));
		forma.setDeudorCoMap("descripcionBarrioReside",parametros.get("descripcionBarrioReside"));
		
		//actualiza las variables del form para la busqueda del Barrio
		forma.setCodigoBarrio(parametros.get("codigoBarrioReside").toString());		
		forma.setNombreBarrio(parametros.get("descripcionBarrioReside").toString());		
		
		forma.setDeudorCoMap("telefonoReside",parametros.get("telefonoReside"));
		forma.setDeudorCoMap("fechaNacimiento",parametros.get("fechaNacimiento"));
		forma.setDeudorCoMap("relacionPaciente",parametros.get("relacionPaciente"));		
		
		//informacion propia de la funcionalidad DeudorCo
		forma.setDeudorCoMap("tipoOcupacion","");		
		forma.setDeudorCoMap("ocupacion","");
		forma.setDeudorCoMap("empresa","");
		forma.setDeudorCoMap("cargo","");
		forma.setDeudorCoMap("antiguedad","");
		forma.setDeudorCoMap("direccionOficina","");		
		forma.setDeudorCoMap("telefonoOficina","");
		forma.setDeudorCoMap("nombresReferencia","");
		forma.setDeudorCoMap("direccionReferencia","");
		forma.setDeudorCoMap("telefonoReferencia","");
		forma.setDeudorCoMap("observaciones","");				
	}	
		

	/**
	 * carga los datos de DeudorCo evaluando un cambio de tipo de Deudor o que no exista informacion del DeudorCo
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario 
	 * @param String estado
	 * */ 
	public void accionCargarInterfazDeudorCoTipo(DocumentosGarantiaForm forma, 
												 Connection con,
												 UsuarioBasico usuario,
												 String estado)
	{
		//si no existe informacion del DeudorCo
		if(forma.getDeudorCoMap("estabd").toString().equals(ConstantesBD.acronimoNo))
		{		
			//si el tipo de Deudor es Paciente
			if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoPaciente))
			{
				cargarDeudorCoTipo(forma,con,"paciente");
				return;
			}	
			
			//si el tipo de Deudor es Responsable de Paciente
			if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoResponsablePaci))
			{					
				cargarDeudorCoTipo(forma,con,"responsable");
				return;
			}	
							
			//si el tipo de Deudor es Otro
			if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				cargarDeudorCoTipo(forma,con,"otro");
				return;
			}	
		}
		//existe informacion del DeudoCo
		else if(forma.getDeudorCoMap("estabd").toString().equals(ConstantesBD.acronimoSi))
		{			
			if(forma.getDeudorCoMap().get("codigoCiudad")!=null){
				forma.setCiudadExpedicion((String)forma.getDeudorCoMap().get("codigoCiudad"));
			}
			if(forma.getDeudorCoMap().get("codigoCiudadReside")!=null){
				forma.setCiudadResidencia((String)forma.getDeudorCoMap().get("codigoCiudadReside"));
			}
			if(!forma.getDeudorCoMap("tipoDeudorCo").toString().equals(forma.getDeudorCoMap("tipoDeudorCoOld").toString())) //si cambio
			{							
				//si el tipo de Deudor es Paciente
				if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoPaciente))
				{
					cargarDeudorCoTipo(forma,con,"paciente");
					return;
				}
				
				//si el tipo de Deudor es Responsable de Paciente
				if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoResponsablePaci))
				{	
					cargarDeudorCoTipo(forma,con,"responsable");
					return;
				}	
								
				//si el tipo de Deudor es Responsable de Paciente
				if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					cargarDeudorCoTipo(forma,con,"otro");
					return;
				}	
			}
			else
			{
				//si se realizo un cambio de tipo deudor otro a otro con diferente identificacion
				if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoOtro) && 
						forma.getDeudorCoMap("tipoDeudorCoOld").toString().equals(ConstantesIntegridadDominio.acronimoOtro)
							&& estado.equals("buscar"))
				{
					cargarDeudorCoTipo(forma,con,"otro");
					return;
				}
			}
		}	
	}
	
	
			
	/**
	 * Carga el HashMap de Datos del Responsables para ser guardado
	 * @param DocumentosGarantiaForm forma
	 * */
	public HashMap cargarDatosReponsable(DocumentosGarantiaForm forma,UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		
		//campo para la actualizacion de la cuenta
		mapa.put("numeroCuenta",forma.getIngresosMap("numeroCuenta_"+forma.getIndexIngresosMap()));
		
		//campos para la inserccion en Responsable Paciente
		mapa.put("numeroIdentificacion",forma.getDeudorCoMap("numeroIdentificacion").toString());
		mapa.put("tipoIdentificacion",forma.getDeudorCoMap("tipoIdentificacion").toString());
		mapa.put("direccionReside",forma.getDeudorCoMap("direccionReside").toString());
		mapa.put("telefonoReside",forma.getDeudorCoMap("telefonoReside").toString());
		mapa.put("relacionPaciente",forma.getDeudorCoMap("relacionPaciente").toString());
		mapa.put("codigoPais",forma.getDeudorCoMap("codigoPais").toString());
		mapa.put("codigoCiudad",forma.getDeudorCoMap("codigoCiudad").toString());
		mapa.put("codigoDepartamento",forma.getDeudorCoMap("codigoDepartamento").toString());
		mapa.put("primerApellido",forma.getDeudorCoMap("primerApellido").toString());
		mapa.put("segundoApellido",forma.getDeudorCoMap("segundoApellido").toString());
		mapa.put("primerNombre",forma.getDeudorCoMap("primerNombre").toString());
		mapa.put("segundoNombre",forma.getDeudorCoMap("segundoNombre").toString());
		mapa.put("codigoPaisReside",forma.getDeudorCoMap("codigoPaisReside").toString());
		mapa.put("codigoCiudadReside",forma.getDeudorCoMap("codigoCiudadReside").toString());
		mapa.put("codigoDepartamentoReside",forma.getDeudorCoMap("codigoDepartamentoReside").toString());
		mapa.put("codigoBarrioReside",forma.getDeudorCoMap("codigoBarrioReside").toString());
		mapa.put("fechaNacimiento",forma.getDeudorCoMap("fechaNacimiento").toString());
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		return mapa;
	}
	
	
	
	/**
	 * Consulta la informacion de la clase de DeudorCo (Deudor o Codeudor)
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public boolean consultarDeudorCo(DocumentosGarantiaForm forma,
									 Connection con,
									 HashMap parametros)
	{		
		String []dependencia;
		int numRegistros;	
		
		forma.setDeudorCoMap(DocumentosGarantia.consultarDeudorCo(con,parametros));
		numRegistros = Integer.parseInt(forma.getDeudorCoMap("numRegistros").toString());
		
		if(numRegistros > 0)//existe informacion de la Clase DeudorCo
		{
			dependencia = DocumentosGarantia.darDependenciasDoscGarantias(DocumentosGarantia.infoDocsDependientes(con, parametros));
			forma.setDeudorCoMap("existenDocsGarantia",dependencia[0]);
			forma.setDeudorCoMap("esModInfoDeudorCo",dependencia[1]);
			
			return true;
		}
		else
		{
			forma.setDeudorCoMap("existenDocsGarantia",ConstantesBD.acronimoNo);
			forma.setDeudorCoMap("esModInfoDeudorCo",ConstantesBD.acronimoSi);
			return false;
		}			
	}
		
	

	//**************************************************************************************	
	//************************************************Funciones De Modificacion de Datos BD
	//**************************************************************************************
	//**************************************************************************************
	
	/**
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public void accionGuardarDeudorCo(DocumentosGarantiaForm forma,
									  Connection con,
									  UsuarioBasico usuario)
	{			
		
		//logger.info("\n\n*********** ENTRO A accionGuardarDeudorCo Y EL HASHMAP ES "+forma.getDeudorCoMap());
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estado="ninguno";		
		HashMap parametros = new HashMap();
						
		parametros.put("usuarioModifica", usuario.getLoginUsuario());
		//parametros de busqueda >> existe modificacion		
		if(forma.getDeudorCoMap("estabd").equals(ConstantesBD.acronimoSi))
		{
			parametros.put("ingreso",forma.getDeudorCoMap("ingreso").toString());
			parametros.put("codigoPaciente",forma.getDeudorCoMap("codigoPaciente").toString());
			parametros.put("institucion",forma.getDeudorCoMap("institucion").toString());
			parametros.put("claseDeudorCo",forma.getDeudorCoMap("claseDeudorCo").toString());
			
			if(DocumentosGarantia.existeModificacion(con, forma.getDeudorCoMap(), parametros, "",1))
			{
				//prepara el HashMap de DeudorCo 
				String temp = forma.getDeudorCoMap("codigoCiudad").toString();
				String temp1= forma.getDeudorCoMap("codigoCiudadReside").toString();
				if (temp.contains(ConstantesBD.separadorSplit))
					verificarGuardarModificar(forma);
						
				forma.setDeudorCoMap("usuarioModifica",usuario.getLoginUsuario());							
				forma.setDeudorCoMap("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));				
				forma.setDeudorCoMap("horaModifica",UtilidadFecha.getHoraActual());	
				
				logger.info("***********entro al if");
				transacction = DocumentosGarantia.actualizarDeudorCo(con,forma.getDeudorCoMap());
				forma.setDeudorCoMap("codigoCiudad",temp);
				forma.setDeudorCoMap("codigoCiudadReside",temp1);
				logger.info("***********sali de la tranccion DocumentosGarantia.actualizarDeudorCo(con,forma.getDeudorCoMap())");
				estado="operacionTrue";
				//se vuelve la fecha al formato de la presentacion, para evitar problemas con el formato.
				forma.setDeudorCoMap("fechaNacimiento", UtilidadFecha.conversionFormatoFechaAAp((forma.getDeudorCoMap("fechaNacimiento").toString())));
			}
		}	
		else		
		{
			//prepara el HashMap de DeudorCo 
			String temp = forma.getDeudorCoMap("codigoCiudad").toString();
			if (temp.contains(ConstantesBD.separadorSplit))
				verificarGuardarModificar(forma);
					
			forma.setDeudorCoMap("usuarioModifica",usuario.getLoginUsuario());							
			forma.setDeudorCoMap("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));				
			forma.setDeudorCoMap("horaModifica",UtilidadFecha.getHoraActual());
	
			transacction = DocumentosGarantia.insertarDeudorCo(con, forma.getDeudorCoMap())>0;
			estado="operacionTrue";			
		}		
		
		
		//Evalua el almacenaniento del Responsable Paciente y la actualizacion del Cuenta
		//no existe informacion del Responsable, solo es valido cuando el tipo de Deudor es Responsable Paciente			
		
		if(forma.getDeudorCoMap("tipoDeudorCo").toString().equals(ConstantesIntegridadDominio.acronimoResponsablePaci))
		{	
			
			parametros = cargarDatosReponsable(forma,usuario);
			
			if(forma.getDeudorCoMap("existeResponsablePaciente").toString().equals(ConstantesBD.acronimoNo))
			{				
				logger.info("valor de parametros >> "+parametros);
				
				//existe informacion del Responsable 
				if(forma.getDeudorCoMap("existeResponsableHistorico").toString().equals(ConstantesBD.acronimoSi))
				{
					transacction = DocumentosGarantia.actualizarCuenta(con, parametros);
					estado="operacionTrue";
					
					logger.info("----->Actualizo Cuenta");
				}
				//no existe informacion del Responsable en la tabla Responsable Paciente
				else if(forma.getDeudorCoMap("existeResponsableHistorico").toString().equals(ConstantesBD.acronimoNo))
				{
					transacction = DocumentosGarantia.insertarResponsablePaciente(con, parametros);
					transacction = DocumentosGarantia.actualizarCuenta(con, parametros);
					estado="operacionTrue";
					
					logger.info("----->INSERTO Informacion del Responsable Paciente");
					logger.info("----->Actualizo Cuenta");
				}
			}
			else if(forma.getDeudorCoMap("existeResponsablePaciente").toString().equals(ConstantesBD.acronimoSi))
			{				
				transacction = DocumentosGarantia.actualizarResponsablePaciente(con, parametros);				
				estado="operacionTrue";
				
				logger.info("----->ACTUALIZO Informacion del Responsable Paciente");												
			}
		}

		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("----->INSERTO 100% DEUDORCO");
		}
		else
			UtilidadBD.abortarTransaccion(con);		
		forma.setEstado(estado);
	}
			
	
	
	
	/**
	 * Guarda la informacion de la verificacion
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * */
	public ActionErrors accionGuardarVerificar(DocumentosGarantiaForm forma, Connection con, ActionErrors errores, UsuarioBasico usuario)	
	{
		HashMap parametros = new HashMap();
		
		//inicializa el estado en vacio para validar que se realizo un cambio 
		forma.setEstado("");
		
		if(!UtilidadFecha.validarFecha(forma.getDeudorCoMap("fechaVerificacion").toString()))
		{
			errores.add("descripcion",new ActionMessage("errors.invalid",forma.getDeudorCoMap("fechaVerificacion").toString()));			
		}
		
		if(!UtilidadFecha.validarFecha(forma.getDeudorCoMap("fechaVeriRiesgos").toString()))
		{
			errores.add("descripcion",new ActionMessage("errors.invalid",forma.getDeudorCoMap("fechaVeriRiesgos").toString()));			
		}
		
		if(!errores.isEmpty())
			return errores;
		
		parametros.put("fechaVerificacion",UtilidadFecha.conversionFormatoFechaABD(forma.getDeudorCoMap("fechaVerificacion").toString()));
		parametros.put("usuarioVerificacion",forma.getDeudorCoMap("usuarioVerificacion").toString());
		parametros.put("observacionesVerificacion",forma.getDeudorCoMap("observacionesVerificacion").toString());
		
		parametros.put("fechaVeriRiesgos",UtilidadFecha.conversionFormatoFechaABD(forma.getDeudorCoMap("fechaVeriRiesgos").toString()));
		parametros.put("usuarioVeriRiesgos",forma.getDeudorCoMap("usuarioVeriRiesgos").toString());
		parametros.put("observacionesVeriRiesgos",forma.getDeudorCoMap("observacionesVeriRiesgos").toString());
		
		parametros.put("ingreso",forma.getDeudorCoMap("ingreso").toString());
		parametros.put("codigoPaciente",forma.getDeudorCoMap("codigoPaciente").toString());
		parametros.put("institucion",forma.getDeudorCoMap("institucion").toString());
		parametros.put("claseDeudorCo",forma.getDeudorCoMap("claseDeudorCo").toString());

		parametros.put("usuarioModificaVerifica",usuario.getLoginUsuario());							
		parametros.put("fechaModificaVerifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));				
		parametros.put("horaModificaVerifica",UtilidadFecha.getHoraActual());
		
		if(DocumentosGarantia.actualizarverficacion(con, parametros)){
			logger.fatal("-----------Actualizo Verificacion >> ");
			forma.setEstado("operacionTrue");
		}
						
		
		return errores;
	}	
	
	
	
	/**
	 * Eliminar Un registro del DeudorCo
	 * @param DocumentosGarantiaForm forma
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public void accionEliminarDeudorCo(DocumentosGarantiaForm forma,
									  Connection con)
	{			
		if(forma.getDeudorCoMap("existenDocsGarantia").equals(ConstantesBD.acronimoNo))
		{
			if(forma.getValorClaseDeudorCo().equals(ConstantesIntegridadDominio.acronimoDeudor))
			{			
				if(forma.getDeudorCoMap("existeCodeudorOdeudor").equals(ConstantesBD.acronimoNo))
				{					
					boolean transacction = UtilidadBD.iniciarTransaccion(con);
					
					transacction = DocumentosGarantia.eliminarDeudorCo(con, forma.getDeudorCoMap());		
					
					if(transacction)
					{
						//inicializa los datos del DeudorCo				
						forma.setDeudorCoMap(new HashMap());
						UtilidadBD.finalizarTransaccion(con);
						logger.info("----->DEUDORCO ELIMINADO");
					}
					else
						UtilidadBD.abortarTransaccion(con);
				}	
			}
			else if(forma.getValorClaseDeudorCo().equals(ConstantesIntegridadDominio.acronimoCoDeudor))
			{
				boolean transacction = UtilidadBD.iniciarTransaccion(con);
				
				transacction = DocumentosGarantia.eliminarDeudorCo(con, forma.getDeudorCoMap());		
				
				if(transacction)
				{
					//inicializa los datos del DeudorCo				
					forma.setDeudorCoMap(new HashMap());
					UtilidadBD.finalizarTransaccion(con);
					logger.info("----->DEUDORCO ELIMINADO");
				}
				else
					UtilidadBD.abortarTransaccion(con);												
			}				
		}	
	}	

	
	
	/*-----------------------------------------------------
	 * METODOS CAMBIO UBICACION GEOGRAFICA 
	 * ---------------------------------------------------*/
	
	/**
	 * Recarga las Ciudad dependiendo del pais
	 * */
	public ActionForward accionCambiarUbicacion(Connection con,DocumentosGarantiaForm forma,HttpServletResponse response)
	{
		String resultado = "<respuesta>";
		String codigoPais = "";
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
				
		codigoPais = forma.getCodigoPais();
		forma.setCiudadesMap(Utilidades.obtenerCiudadesXPais(con, codigoPais));
		/*arregloAux = forma.getCiudadesMap();
		
		
		logger.info("Nï¿½MERO DE ELEMENTO ARREGLO CIUDADES=> "+arregloAux.size());
		logger.info("CODIGO PAIS=>*"+codigoPais+"*");
		
		//Revision de las ciudades segun pais seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			if(elemento.get("codigoPais").toString().equals(codigoPais))
				resultado += "<ciudad>" +
					"<codigo-departamento>"+elemento.get("codigoDepartamento")+"</codigo-departamento>"+
					"<codigo-ciudad>"+elemento.get("codigoCiudad")+"</codigo-ciudad>"+
					"<nombre-departamento>"+elemento.get("nombreDepartamento")+"</nombre-departamento>"+
					"<nombre-ciudad>"+elemento.get("nombreCiudad")+"</nombre-ciudad>"+
				 "</ciudad>";
		}*/
		
		resultado += "</respuesta>";

		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.reset();
			response.resetBuffer();
			
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(resultado);
	        response.getWriter().close();
	        
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
		}
		return null;
	}		
	
	
	
}