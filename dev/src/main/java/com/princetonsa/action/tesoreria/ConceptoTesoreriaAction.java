/*
 * Creado   Jun 17 2005
 * Modificado Sep 28 2005 
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */

package com.princetonsa.action.tesoreria;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ConceptoTesoreriaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.ConceptoTesoreria;


/**
 * @author Angela Bibiana Cardona
 * @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Conceptos Ingreso Tesorería
 */

public class ConceptoTesoreriaAction extends Action 
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConceptoTesoreriaAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {
		Connection con = null;
		try{
			if(form instanceof ConceptoTesoreriaForm )
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConceptoTesoreriaForm conceptosForm = (ConceptoTesoreriaForm) form;
				HttpSession sesion = request.getSession();
				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);

				String estado = conceptosForm.getEstado();

				logger.info("Estado Actual: "+estado);

				conceptosForm.setMostrarMensaje(new ResultadoBoolean(false, ""));


				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return this.cerrarConexion(con,mapping,"paginaError",request);
				}	
				//*********ESTADOS PARA INGRESAR/MODIFICAR***************************
				else if (estado.equals("empezarTesoreria"))
				{
					//empezar la modificación, inserción ó busqueda avanzada
					conceptosForm.setOffsetHash(0);
					return accionEmpezarTesoreria(con,conceptosForm,mapping,request,usuario);
				}
				else if (estado.equals("volver"))
				{
					return this.cerrarConexion(con,mapping,"ingresarModificarTesoreria",request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,conceptosForm,mapping,request);
				}
				else if(estado.equals("nuevo"))
				{
					//ingresar un registro nuevo
					return accionNuevo(con,conceptosForm,mapping,request,usuario); 
				}
				else if(estado.equals("detalle"))
				{
					//ingresar al detalle de un registro existente
					return accionDetalle(con,conceptosForm,mapping,request,usuario);
				}
				else if(estado.equals("recarga")) 
				{
					//usado cuando se cambia el tipo y se recargan los filtros de un registro
					return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
				}
				else if(estado.equals("recargaBusqueda"))  
				{
					///usado cuando se cambia el tipo y se recargan los filtros en la busqueda avanzada
					return this.cerrarConexion(con,mapping,"busqueda",request);
				}
				else if(estado.equals("guardarInsertar"))
				{
					//usado para insertar un nuevo concepto de ingreso tesoreria
					return accionGuardarInsertar(con,conceptosForm,mapping,usuario,request);
				}
				else if(estado.equals("guardarModificar"))
				{
					//usado para modificar un concepto de ingreso tesoreria
					return accionGuardarModificar(con,conceptosForm,mapping,usuario,request);
				}
				else if (estado.equals("redireccion"))
				{			    
					// estado para mantener los datos del pager
					return accionRedireccion(con,conceptosForm,response,mapping,request);
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con,conceptosForm,mapping,usuario,request);  
				}
				else if(estado.equals("busquedaIngresar")) 
				{
					//ingreso a la busqueda avanzada de Ingresar/Modificar
					return accionBusqueda(con,conceptosForm,mapping,request);
				}
				else if(estado.equals("consultaIngresar")) 
				{
					//realizar consulta en la busqueda avanzada de Ingresar/Modificar
					return accionConsultaAvanzada(con,conceptosForm,mapping,"ingresarModificarTesoreria",request,"ingreso",usuario);
				}
				//**********ESTADOS PARA CONSULTAR*************************************
				else if(estado.equals("empezarConsultaTesoreria"))
				{
					return accionEmpezarConsultaTesoreria(con,conceptosForm,mapping,usuario,request);   
				}
				else if(estado.equals("busquedaConsultar"))
				{
					//ingreso a la busqueda avanzada de Consultar
					return accionBusqueda(con,conceptosForm,mapping,request);
				}
				else if(estado.equals("consultaConsultar")) 
				{
					//realizar consulta en la busqueda avanzada de Consultar
					return accionConsultaAvanzada(con,conceptosForm,mapping,"consultarTesoreria",request,"consulta",usuario);
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptoTesoreriaForm");
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
	 * Método implementado para modificar un concepto de ingreso tesorería
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificar(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		//se instancia objeto Conceptos ingreso Tesorería
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		this.llenarMundo(conceptosForm,concepto);
		//se consulta si el registro está siendo utilizado en otras tablas
		boolean esUtilizado=concepto.revisarUsoConcepto(con,usuario.getCodigoInstitucionInt());
		//obtener registro viejo
		
		HashMap registroAntiguo=concepto.cargarConceptoTesoreriaAntesActualizar(con, conceptosForm.getCodigoDetalle(), usuario.getCodigoInstitucionInt());
		
		//se revisa si fue modificado
		if(fueModificado(registroAntiguo,concepto))
		{
			//se verifica que el registro no se esté utilizando
			//o si se utilizó que no se haya cambiado el código
			//if(!esUtilizado && )
			//{
				//se actualizan los datos
				int resp=concepto.actualizarConceptoTesoreria(con, conceptosForm.getCodigoDetalle(), usuario.getCodigoInstitucionInt());
				//se revisa estado actualizacion
				if(resp<=0)
				{
					errores.add("error grabar informacion",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO CON CÓDIGO "+concepto.getCodigo()));
					saveErrors(request,errores);
					conceptosForm.setEstado("detalle");
				}
				else
				{
					conceptosForm.setNombreTipoPago(concepto.cargarTipoPagoEspecial(con, concepto.getTipo()));
					conceptosForm.setEstado("guardar");
					this.generarLog(con,registroAntiguo,concepto,ConstantesBD.tipoRegistroLogModificacion,usuario);
				}
				
			//}
			//conceptosForm.setEstado("detalle");
			/*else
			{
				if(conceptosForm.getCuenta())
				{
					
				}
				else
				{
					errores.add("esta siendo usado",new ActionMessage("error.registroUtilizado","CON EL CÓDIGO "+concepto.getCodigo(),"POR EL SISTEMA, NO SE PUEDE MODIFICAR"));
					saveErrors(request,errores);
					conceptosForm.setEstado("detalle");
				}
			}*/
			
		}
		
		//se cierra concexión y se hace forward
		//return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
		UtilidadBD.closeConnection(con);
		return  mapping.findForward("ingresarConcepto");
		
	}


	/**
	 * Método que carga el detalle de un concepto de tesorería específico
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		//Se instancia mundo de Concepto Tesoreria
		ConceptoTesoreria conceptos = new ConceptoTesoreria();
		
		//se consulta si el registro está siendo utilizado en otras tablas
		boolean esUtilizado=conceptos.revisarUsoConcepto(con,usuario.getCodigoInstitucionInt());
		//obtener registro viejo
		conceptosForm.setActivo(esUtilizado);
		
		
		
		
		//Cambio tarea 109632
		if(UtilidadValidacion.esConceptoUsado(con,conceptosForm.getMapConceptos("codigo_"+conceptosForm.getPos()).toString(),usuario.getCodigoInstitucionInt()))
			conceptosForm.setPoseeRC(true);
		else
			conceptosForm.setPoseeRC(false);
		
		logger.info("EL USO DEL CONCEPTO EN UN RECIBO DE CAJA ES----->"+conceptosForm.isPoseeRC());
		
		conceptosForm.resetDetalle();
		
		
		conceptosForm.setEsInsertar(true);
		conceptosForm.setTiposPago(conceptos.cargarTiposPagos(con));
		conceptosForm.setTiposDocContabilidad(conceptos.cargarTiposDocContabilidad(con,usuario.getCodigoInstitucionInt()));
		conceptosForm.setCentrosCosto(conceptos.cargarCentrosCosto(con,usuario.getCodigoInstitucionInt()));
		conceptosForm.setTerceros(conceptos.cargarTerceros(con,usuario.getCodigoInstitucionInt()));
		conceptosForm.setCodigoDetalle(conceptosForm.getMapConceptos("codigo_"+conceptosForm.getPos()).toString());
		conceptosForm.setCodigoDetalle(conceptosForm.getMapConceptos("codigo_"+conceptosForm.getPos()).toString());
	
	
				//Se carga el registro
		conceptos.setCodigo(conceptosForm.getMapConceptos("codigo_"+conceptosForm.getPos()).toString());
		HashMap mapConceptos = conceptos.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
		llenarForma(conceptosForm,mapConceptos);
		
		//se cierra concexión y se hace forward
	    return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
	}


	/**
	 * Método implementado para insertar un nuevo concepto de ingreso tesoreria
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarInsertar(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		//se instancia objeto Conceptos ingreso Tesorería
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		this.llenarMundo(conceptosForm,concepto);
		
		//se verifica si ya existe un concepto con el mismo código
		HashMap mapConcepto = concepto.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
		
		if(Integer.parseInt(mapConcepto.get("numRegistros").toString())>0)
		{
			conceptosForm.setEstado("nuevo");
			errores.add("Ya fue ingresado",new ActionMessage("errors.registroExistente","CONCEPTO CON EL CÓDIGO "+concepto.getCodigo()));
			saveErrors(request,errores);
			
		}
		else
		{
			int resp=concepto.insertarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
			
			if(resp>0)
			{
				//Cargamos de nuevo los datos
				mapConcepto = concepto.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
				this.llenarForma(conceptosForm,mapConcepto);
				conceptosForm.setEstado("guardar");
			}
			else
			{
				conceptosForm.setEstado("nuevo");
				errores.add("error al ingresar los datos",new ActionMessage("errors.noSeGraboInformacion","DEL CONCEPTO CON CÓDIGO "+concepto.getCodigo()));
				saveErrors(request,errores);
			}
		}
		
		return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
	}


	/**
	 * Método implementado para llenar la forma con los datos del concepto de ingreso tesoreria
	 * @param conceptosForm
	 * @param mapConcepto
	 */
	private void llenarForma(ConceptoTesoreriaForm conceptosForm, HashMap mapConcepto) 
	{
		conceptosForm.setCodigo(mapConcepto.get("codigo_0").toString());
		conceptosForm.setDescripcion(mapConcepto.get("descripcion_0").toString());
		conceptosForm.setCodigoTipoPago(Integer.parseInt(mapConcepto.get("tipo_0").toString()));
		conceptosForm.setNombreTipoPago(mapConcepto.get("nom_tipo_0").toString());
		conceptosForm.setCodigoFiltroValor(mapConcepto.get("valor_0").toString());
		conceptosForm.setCuenta(mapConcepto.get("cuenta_0").toString());
		conceptosForm.setCodigoDocumentoIngreso(Integer.parseInt(mapConcepto.get("doc_ingreso_0").toString()));
		conceptosForm.setCodigoDocumentoAnulacion(Integer.parseInt(mapConcepto.get("doc_anulacion_0").toString()));
		conceptosForm.setCodigoCentroCosto(Integer.parseInt(mapConcepto.get("centro_costo_0").toString()));
		conceptosForm.setNit(Integer.parseInt(mapConcepto.get("nit_0").toString()));
		conceptosForm.setActivo(UtilidadTexto.getBoolean(mapConcepto.get("activo_0").toString()));
		conceptosForm.setRubroPresupuestal(mapConcepto.get("rubropresupuestal_0")+"");
				
		
	}


	/**
	 * Método implementado para ordenar el listado de conceptos
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, HttpServletRequest request) 
	{
		String[] indices={
				"codigo_",
				"descripcion_",
				"tipo_pago_",
				"activo_"
			};
		
		
		
		conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
				conceptosForm.getIndice(),
				conceptosForm.getUltimoIndice(),
				conceptosForm.getMapConceptos(),
				conceptosForm.getNumRegistros()));
		
		conceptosForm.setMapConceptos("numRegistros",conceptosForm.getNumRegistros()+"");
		conceptosForm.setUltimoIndice(conceptosForm.getIndice());
		
		conceptosForm.setEstado("empezarTesoreria");
		return this.cerrarConexion(con,mapping,"ingresarModificarTesoreria",request);
	}


	/**
	 * Método implementado para realizar la consulta del listado de
	 * conceptos de ingreso tesorería en la opcion "Consultar"
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezarConsultaTesoreria(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//se limpian los datos
		conceptosForm.reset();
		
		//se genera instancia de Conceptos Tesorerái
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		//se llena el mundo con datos inválidos
		this.llenarMundo(concepto,"","",-1,"","",-1,-1,-1,-1);
		
		//se consultan los conceptos de tesorería
		conceptosForm.setMapConceptos(concepto.busquedaConceptosTesoreria(con,"",usuario.getCodigoInstitucionInt()));
		
		//se obtiene el tamaño del listado
		conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
		
		return this.cerrarConexion(con,mapping,"consultarTesoreria",request);
	}


	/**
	 * Método implementado para ejecutar las busquedas avanzadas, bien sea de
	 * la opción de Ingresar/Modificar o de la opción Consultar de Conceptos
	 * Ingreso de Tesorería
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param forward (nombre del forward)
	 * @param request
	 * @param opcion (definicion de la opción usada "ingreso" o "consulta")
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultaAvanzada(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, String forward, HttpServletRequest request, String opcion, UsuarioBasico usuario) 
	{
		//datos auxiliares de los campos de búsqueda
		String codigo="";
		String descripcion="";
		int tipo=-1;
		String valor="-1";
		String cuenta="";
		int docIngreso=-1;
		int docAnulacion=-1;
		int centroCosto=-1;
		int nit=-1;
		String activo="";
		
		
		//verificar campo CODIGO*******************************
		if((conceptosForm.getMapBusqueda("check_codigo")+"").equals("true"))
			codigo=conceptosForm.getMapBusqueda("codigo")+"";
		
		//verificar campo DESCRIPCIÓN*************************
		if((conceptosForm.getMapBusqueda("check_descripcion")+"").equals("true"))
			descripcion=conceptosForm.getMapBusqueda("descripcion")+"";
		
		//verificar campo TIPO*****************************
		if((conceptosForm.getMapBusqueda("check_tipo")+"").equals("true"))
			tipo=Integer.parseInt(conceptosForm.getMapBusqueda("tipo")+"");
		
		//verificar campo VALOR****************************
		if((conceptosForm.getMapBusqueda("check_valor")+"").equals("true"))
			valor=conceptosForm.getMapBusqueda("valor")+"";
		
		//verificar campo CUENTA***************************
		if((conceptosForm.getMapBusqueda("check_cuenta")+"").equals("true"))
			cuenta=conceptosForm.getMapBusqueda("cuenta")+"";
		
		//verificar campo DOC INGRESO*************************
		if((conceptosForm.getMapBusqueda("check_doc_ingreso")+"").equals("true"))
			docIngreso=Integer.parseInt(conceptosForm.getMapBusqueda("doc_ingreso")+"");
		
		//verificar campo DOC INGRESO*************************
		if((conceptosForm.getMapBusqueda("check_doc_anulacion")+"").equals("true"))
			docAnulacion=Integer.parseInt(conceptosForm.getMapBusqueda("doc_anulacion")+"");
		
		//verificar campo CENTRO COSTO*************************
		if((conceptosForm.getMapBusqueda("check_centro_costo")+"").equals("true"))
			centroCosto=Integer.parseInt(conceptosForm.getMapBusqueda("centro_costo")+"");
		
		//verificar campo NIT HOMOLOGACIÓN*************************
		if((conceptosForm.getMapBusqueda("check_nit")+"").equals("true"))
			nit=Integer.parseInt(conceptosForm.getMapBusqueda("nit")+"");
			
		//verificar campo ACTIVO*****************************
		if((conceptosForm.getMapBusqueda("check_activo")+"").equals("true"))
			if(conceptosForm.getMapBusqueda("activo")!=null)
				activo=conceptosForm.getMapBusqueda("activo")+"";
		
		//se limpian los datos	
		conceptosForm.reset();
		
		//se instancia mundo de Conceptos
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		//se llenan los datos del mundo
		this.llenarMundo(concepto,codigo,descripcion,tipo,valor,cuenta,docIngreso,docAnulacion,centroCosto,nit);
		
		
		//si la búsqueda avanzada es de Ingresar/Modificar
		if(opcion.equals("ingreso"))
		{
			//se asigna nuevo estado
			conceptosForm.setEstado("empezarTesoreria");
			//se realiza la búsqueda para el Ingreso/Modificación
			conceptosForm.setMapConceptos(concepto.busquedaConceptosTesoreria2(con,activo,usuario.getCodigoInstitucionInt()));
		}
		//si la búsqueda Avanzada es de Consultar
		else if(opcion.equals("consulta"))
		{
			//se asigna nuevo estado
			conceptosForm.setEstado("consultaConsultar");
			
			//se realiza la búsqueda para la Consulta
			conceptosForm.setMapConceptos(concepto.busquedaConceptosTesoreria(con,activo,usuario.getCodigoInstitucionInt()));
			
			
		}
		
		conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
		
		return this.cerrarConexion(con,mapping,forward,request);
		
	}


	/**
	 * llenarMundo usado para la carga del mundo en las
	 * opciones de consulta y búsqueda Avanzada
	 * @param concepto
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param docIngreso
	 * @param docAnulacion
	 * @param centroCosto
	 * @param nit
	 */
	private void llenarMundo(ConceptoTesoreria concepto, String codigo, String descripcion, int tipo, String valor, String cuenta, int docIngreso, int docAnulacion, int centroCosto, int nit) 
	{
		concepto.setCodigo(codigo);
		concepto.setDescripcion(descripcion);
		concepto.setTipo(tipo);
		concepto.setValor(valor);
		concepto.setCuenta(cuenta);
		concepto.setTipoDocumentoIngreso(docIngreso);
		concepto.setTipoDocumentoAnulacion(docAnulacion);
		concepto.setCodCentroCosto(centroCosto);
		concepto.setNitHomologacion(nit);
		
		
	}


	/**
	 * Método usado para preparar los datos en el inicio de la búsqueda avanzada
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionBusqueda(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, HttpServletRequest request) 
	{
		conceptosForm.reset();
		//preparación de los datos para la búsqueda
		conceptosForm.setMapBusqueda("tipo","-1");
		conceptosForm.setMapBusqueda("cuenta","-1");
		conceptosForm.setMapBusqueda("doc_ingreso","-1");
		conceptosForm.setMapBusqueda("doc_anulacion","-1");
		conceptosForm.setMapBusqueda("centro_costo","-1");
		conceptosForm.setMapBusqueda("nit","-1");
		return this.cerrarConexion(con,mapping,"busqueda",request);
	}


	/**
	 * Método usado para eliminar un registro del listado
	 * de Conceptos de Ingreso Tesorería
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		boolean esUtilizado=false;
		//se instancia objeto Conceptos ingreso Tesorería
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		this.llenarMundo(conceptosForm,concepto);
		
		//se verifica si ya existe un concepto con el mismo código
		HashMap mapConcepto = concepto.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
		
		logger.info("1111111111111");
		if(Integer.parseInt(mapConcepto.get("numRegistros").toString())<=0) //||!conceptosForm.isEsModificar())
		{
			logger.info("11111111111112222222222222 "+conceptosForm.isEsModificar());
			conceptosForm.setEstado("empezarTesoreria");
			return accionEmpezarTesoreria(con,conceptosForm,mapping,request,usuario);
		}
		else
		{
			logger.info("11111111111113333333333333");
			//se consulta si el registro está siendo utilizado en otras tablas
			esUtilizado=concepto.revisarUsoConcepto(con,usuario.getCodigoInstitucionInt());
			//se cargan los datos del registro a eliminar
			HashMap registroEliminacion=concepto.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
			
			
			if(!esUtilizado)
			{	
				//se realiza la eliminación
				int resp=concepto.eliminarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
				//se verifica resultado de la transacción
				if(resp<=0)
				{
					errores.add("error grabar informacion",new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO CON CÓDIGO "+concepto.getCodigo()));
					saveErrors(request,errores);
					conceptosForm.setEstado("detalle");
				}
				else
				{
					//se inserta LOG
					this.generarLog(con,registroEliminacion,null,ConstantesBD.tipoRegistroLogEliminacion,usuario);
					conceptosForm.setEstado("empezarTesoreria");
					ActionForward forward=accionEmpezarTesoreria(con,conceptosForm,mapping,request,usuario);
					conceptosForm.setMostrarMensaje(new ResultadoBoolean(true, "PROCESO EXITOSO."));
					return forward;
				}
			}
			else
			{
				errores.add("esta siendo usado",new ActionMessage("error.registroUtilizado","CON EL CÓDIGO "+concepto.getCodigo(),"POR EL SISTEMA, NO SE PUEDE ELIMINAR"));
				saveErrors(request,errores);
				conceptosForm.setEstado("detalle");
			}
		}
		return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
		
	}


	/**
	 * Método usado para hacer los procesos vinculados con el cambio
	 * de página en el listado conceptos ingreso tesorería
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ConceptoTesoreriaForm conceptosForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(conceptosForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ConceptoTesoreriaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConceptoTesoreriaAction", "errors.problemasDatos", true);
		}
	}


	

	/**
	 * Método usado para generar el LOG tipo archivo
	 * @param con
	 * @param registro (antiguo)
	 * @param concepto (nuevo)
	 * @param tipo de LOG (modificacion-eliminacion)
	 * @param usuario
	 */
	private void generarLog(Connection con, HashMap registro, ConceptoTesoreria concepto, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    //***********************************************************
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			//se carga la información adicional nueva*****************
			HashMap nuevo=concepto.cargarConceptoTesoreria(con,usuario.getCodigoInstitucionInt());
			//***************************************************
		    log="\n            ====INFORMACION ORIGINAL DEL CONCEPTO INGRESO DE TESORERÍA===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("descripcion_0")+"] " +
			"\n*  Tipo de Pago ["+registro.get("nom_tipo_0")+"] " +
			"\n*  Filtro-Valor ["+registro.get("filtro_0")+"] " +
			"\n*  Cuenta ["+registro.get("nom_cuenta_0")+"] " +
			"\n*  Doc. Ingreso ["+registro.get("nom_doc_ingreso_0")+"] " +
			"\n*  Doc. Anulación ["+registro.get("nom_doc_anulacion_0")+"] " +
			"\n*  Centro Costo ["+registro.get("nom_centro_costo_0")+"] " +
			"\n*  Nit Homologación ["+registro.get("nom_nit_0")+"] " +
			"\n*  Activo ["+UtilidadTexto.getBoolean(registro.get("activo_0")+"")+"] " +
			"\n*  Rubro Presupuestal ["+registro.get("rubropresupuestal_0")+""+"] " +

			"";

			//***************************************************
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL CONCEPTO INGRESO DE TESORERÍA===== " +
			"\n*  Codigo [" +nuevo.get("codigo_0")+"] "+
			"\n*  Descripción ["+nuevo.get("descripcion_0")+"] " +
			"\n*  Tipo de Pago ["+nuevo.get("nom_tipo_0")+"] " +
			"\n*  Filtro-Valor ["+nuevo.get("filtro_0")+"] " +
			"\n*  Cuenta ["+nuevo.get("nom_cuenta_0")+"] " +
			"\n*  Doc. Ingreso ["+nuevo.get("nom_doc_ingreso_0")+"] " +
			"\n*  Doc. Anulación ["+nuevo.get("nom_doc_anulacion_0")+"] " +
			"\n*  Centro Costo ["+nuevo.get("nom_centro_costo_0")+"] " +
			"\n*  Nit Homologación ["+nuevo.get("nom_nit_0")+"] " +
			"\n*  Activo ["+UtilidadTexto.getBoolean(nuevo.get("activo_0")+"")+"] " +
			"\n*  Rubro Presupuestal ["+nuevo.get("rubropresupuestal_0")+""+"] " +
			"";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			
			//***************************************************
		    log="\n            ====INFORMACION ELIMINADA DE CONCEPTOS DE INGRESO TESORERÍA==== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("descripcion_0")+"] " +
			"\n*  Tipo de Pago ["+registro.get("nom_tipo_0")+"] " +
			"\n*  Filtro-Valor ["+registro.get("filtro_0")+"] " +
			"\n*  Cuenta ["+registro.get("nom_cuenta_0")+"] " +
			"\n*  Doc. Ingreso ["+registro.get("nom_doc_ingreso_0")+"] " +
			"\n*  Doc. Anulación ["+registro.get("nom_doc_anulacion_0")+"] " +
			"\n*  Centro Costo ["+registro.get("nom_centro_costo_0")+"] " +
			"\n*  Nit Homologación ["+registro.get("nom_nit_0")+"] " +
			"\n*  Activo ["+UtilidadTexto.getBoolean(registro.get("activo_0")+"")+"] " +
			"\n*  Rubro Presupuestal ["+registro.get("rubropresupuestal_0")+""+"] " +
			"";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logTesoreriaCodigo, log, tipo,usuario.getLoginUsuario());
		
	}



	
	/**
	 * Método usado para revisar si un registro fue modificado
	 * @param registro (antiguo)
	 * @param concepto (nuevo)
	 * @return
	 */
	private boolean fueModificado(HashMap registro, ConceptoTesoreria concepto) 
	{
		boolean cambio=false;
		
		//revisión del campo codigo
		if(concepto.getCodigo().compareTo(registro.get("codigo_0")+"")!=0)
			cambio=true;
		
		//revision del campo descripcion
		if(concepto.getDescripcion().compareTo(registro.get("descripcion_0")+"")!=0)
			cambio=true;
		
		//revisión del campo tipo de ingreso
		if(concepto.getTipo()!=Integer.parseInt(registro.get("tipo_0")+""))
			cambio=true;
		
		//revision del campo descripcion
		if(concepto.getValor().compareTo(registro.get("valor_0")+"")!=0)
			cambio=true;
		
		//revisión del campo cuenta
		if(!concepto.getCuenta().equals(registro.get("cuenta_0")+""))
			cambio=true;
		
		//revisión de documento de ingreso
		if(concepto.getTipoDocumentoIngreso()!=Integer.parseInt(registro.get("doc_ingreso_0")+""))
			cambio=true;
		
		//revisión de documento de ingreso
		if(concepto.getTipoDocumentoAnulacion()!=Integer.parseInt(registro.get("doc_anulacion_0")+""))
			cambio=true;
		
		//revisión de documento de ingreso
		if(concepto.getCodCentroCosto()!=Integer.parseInt(registro.get("centro_costo_0")+""))
			cambio=true;
		
		//revisión de documento de ingreso
		if(concepto.getNitHomologacion()!=Integer.parseInt(registro.get("nit_0")+""))
			cambio=true;
		
		//revision del campo activo
		if(concepto.isActivo()!=UtilidadTexto.getBoolean(registro.get("activo_0")+""))
			cambio=true;
		
		if(!concepto.getRubroPresupuestal().equals(registro.get("rubropresupuestal_0")+""))
			cambio=true;
		
		return cambio;
	}


	/**
	 * Método usado para cargar el objeto del mundo
	 * con los datos del formulario
	 * @param conceptosForm
	 * @param concepto
	 */
	private void llenarMundo(ConceptoTesoreriaForm conceptosForm, ConceptoTesoreria concepto) 
	{
		concepto.setCodigo(conceptosForm.getCodigo());
		concepto.setDescripcion(conceptosForm.getDescripcion());
		concepto.setTipo(conceptosForm.getCodigoTipoPago());
		concepto.setValor(conceptosForm.getCodigoFiltroValor());
		concepto.setCuenta(conceptosForm.getCuenta());
		concepto.setTipoDocumentoIngreso(conceptosForm.getCodigoDocumentoIngreso());
		concepto.setTipoDocumentoAnulacion(conceptosForm.getCodigoDocumentoAnulacion());
		concepto.setCodCentroCosto(conceptosForm.getCodigoCentroCosto());
		concepto.setNitHomologacion(conceptosForm.getNit());
		concepto.setActivo(conceptosForm.isActivo());
		concepto.setRubroPresupuestal(conceptosForm.getRubroPresupuestal());
	}


	/**
	 * Método usado para ingresar un nuevo concepto dentro del listado
	 * de conceptos de ingreso tesorería
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		//Se instancia mundo de Concepto Tesoreria
		ConceptoTesoreria conceptos = new ConceptoTesoreria();
		
		conceptosForm.resetDetalle();
		conceptosForm.setTiposPago(conceptos.cargarTiposPagos(con));
		conceptosForm.setTiposDocContabilidad(conceptos.cargarTiposDocContabilidad(con,usuario.getCodigoInstitucionInt()));
		conceptosForm.setCentrosCosto(conceptos.cargarCentrosCosto(con,usuario.getCodigoInstitucionInt()));
		//Ya no se usa la bsuqueda de terceros como un select, ahora por Tarea 1415 se usa la bsuqueda generica
		//Pero Igual lo voy a utilizar para obtener el nombre del tercero parametrizado
		conceptosForm.setTerceros(conceptos.cargarTerceros(con,usuario.getCodigoInstitucionInt()));
		conceptosForm.setPoseeRC(false);
		//se cierra concexión y se hace forward
	    return this.cerrarConexion(con,mapping,"ingresarConcepto",request);
	}


	/**
	 * Método inicial para postular los registros existentes de los
	 * conceptos de ingreso de tesorería
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarTesoreria(Connection con, ConceptoTesoreriaForm conceptosForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		//se conservan datos antiguos
		String estado=conceptosForm.getEstado();
		String registrosUsados=conceptosForm.getRegistrosUsados();
		int offset=conceptosForm.getOffsetHash();
		
		//se limpian los datos
		conceptosForm.reset();			    
		
		//se asigna datos antiguos
		conceptosForm.setEstado(estado);
		conceptosForm.setRegistrosUsados(registrosUsados);
		conceptosForm.setOffsetHash(offset);
		
		//se instancia objeto de Conceptos Ingreso Tesorería
		ConceptoTesoreria concepto=new ConceptoTesoreria();
		
		//cargamos los registros de conceptos actuales
	    conceptosForm.setMapConceptos(concepto.cargarConceptosTesoreria(con,usuario.getCodigoInstitucionInt()));
	    
	    //añadimos el numero de registros del mapa
	    conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
	    //se cierra concexión y se hace forward
	    return this.cerrarConexion(con,mapping,"ingresarModificarTesoreria",request);
	}


	
	/**
	 * Método usado para abrir la Conexión
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		return con;
					
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
		    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
			return null;
		}
				
		return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public ActionForward cerrarConexion (Connection con, ActionMapping mapping,String forward,HttpServletRequest request)
	{
	    try
		{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);	        
	        }
	        return mapping.findForward(forward);
	    }
	    catch(Exception e){
	    	logger.error("Error cerrando la conexión en ConceptoTesoreriaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConceptoTesoreriaAction", "errors.problemasDatos", true);
	    }
	}
	
	/**
	 * Se carga el usuario en sesión
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
		if(usuario == null)
		    logger.warn("El usuario no esta cargado (null)");
			
		return usuario;
	}
}