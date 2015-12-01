package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.ParametrizacionFormulariosRespuForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoValorOpcionCampoParam;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ParametrizacionFormulariosRespu;
import com.princetonsa.mundo.historiaClinica.ParametrizacionPlantillas;
import com.princetonsa.mundo.historiaClinica.Plantillas;


/**
 * @author Jose Eduardo Arias Doncel 
 * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class ParametrizacionFormulariosRespuAction extends Action
{	
	Logger logger = Logger.getLogger(ParametrizacionFormulariosRespuAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
	{		
		 
		Connection con = null;
		try {
		if(response == null);
		
		 if (form instanceof ParametrizacionFormulariosRespuForm) 
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
			 
			 ParametrizacionFormulariosRespuForm forma = (ParametrizacionFormulariosRespuForm)form;		
			 String estado = forma.getEstado();		
			 //Inicializa el estado para el indicador de exito de la operación
			 if(forma.getPlantillaDto() != null)
				 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);
			 
			 logger.info("-------------------------------------");
			 logger.info("Valor del Estado    >> "+forma.getEstado());			 
			 logger.info("-------------------------------------");
			 logger.info("-------------------------------------");
			 logger.info("Valor del Nivel    >> "+forma.getIndexNivel());
			 logger.info("Index Seccion Fija >> "+forma.getIndexSeccionFija());
			 logger.info("Index Elemento     >> "+forma.getIndexElemento());
			 logger.info("Index Seccion Nivel 2 >> "+forma.getIndexSeccionNivel2());
			 logger.info("Index Campo           >> "+forma.getIndexCampo());
			 logger.info("Index Opcion Campo    >> "+forma.getIndexOpcionCampo());		
			 logger.info("Index Secciones Asociadas  >> "+forma.getIndexSeccionesAsocidas());
			 logger.info("Index Valores Asociados >> "+forma.getIndexValoresAsocidos());
			 logger.info("-------------------------------------");
			 
			 if(estado == null)
			 {				 
				 logger.warn("Estado no Valido dentro del Flujo de Hoja de Parametrizacion Formularios Respuesta (null)");				 
				 request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("paginaError");
			 }
			 
			 //-----------------------------------------------------------------------------------			 
			 //***********************************************************************************
			 //*****************Estados del Action ***********************************************
			 //-----------------------------------------------------------------------------------			 
			 else if(estado.equals("empezar"))
			 {
				 forma.reset();
				 //Realiza las validaciones de ingreso a la funcionalidad
				 validacionesPermisos(usuario,forma,false);
				 metodoEmpezar(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 else if(estado.equals("preview"))
			 {					 
				 metodoPreview(con,request,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("preview");
			 }
			 else if (estado.equals("recargarPlantilla"))
			 {
			 	UtilidadBD.closeConnection(con);
			 	return mapping.findForward("seccionesValoresParametrizables");
			 }
			 //Metodo empezar para consultas
			 else if(estado.equals("empezarConsulta"))
			 {
				 forma.reset();
				 //Realiza las validaciones de ingreso a la funcionalidad
				 validacionesPermisos(usuario,forma,true);
				 metodoEmpezar(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }			 
			 //Estado ordena el listado de Planillas 
			 else if(estado.equals("ordenarListado"))
			 {				 
				 metodoEstadoOrdenarListado(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");				 
			 }		
			 //Estado para la paginacion
			 else if(estado.equals("redireccionListado"))
			 {
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			 }
			 else if(estado.equals("volverListadoFormularios"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 
			 /******************************************************************************************************/
			 //Estados para los formularios (Encabezado)*********************************************
			 
			 else if(estado.equals("nuevoFormulario"))
			 {
				 metodoNuevoFormulario(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 else if(estado.equals("guardarFormulario"))
			 {				 
				 errores = validacionesGuardarFormulario(forma);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("listado");
				 }
				 
				 //Guarda la información
				 errores = metodoGuardarFormulario(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("listado");
				 }
				 
				 //Recarga los datos del listado
				 metodoEmpezar(con,forma,usuario);
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 else if(estado.equals("modificarFormulario"))
			 {
				 cargarFormularioModificar(con,forma);				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 else if(estado.equals("detalleFormulario"))
			 {
				 cargarFormularioModificar(con,forma);	
				 metodoDetallePlantilla(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesFijas");
			 }
			 else if(estado.equals("eliminarFormulario"))
			 {
				 cargarFormularioModificar(con,forma);	
				 metodoEliminarPlantilla(con,forma,usuario);			 
				 				 
				 forma.reset();
				 //Realiza las validaciones de ingreso a la funcionalidad
				 validacionesPermisos(usuario,forma,false);
				 metodoEmpezar(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");				 
			 }
			 
			 /******************************************************************************************************/
			 //Estados para los formularios (Plantillas Secciones Fijas o Secciones Parametrizables de Primer Nivel)
			 //******************************************************************************************************				
			 else if(estado.equals("modificarPlantillasSecFijas"))
			 {
				 errores = metodoModificarPlantillasSecFijas(con,forma,usuario,false);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("seccionesFijas");
				 }
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesFijas");
			 }
			 else if(estado.equals("eliminarSeccionParam"))
			 {
				 errores = metodoEliminarSeccionParam(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 if(forma.getIndexNivel().equals("fija") || forma.getIndexNivel().equals("1"))
						 return mapping.findForward("seccionesFijas");
					 else
						 return mapping.findForward("detalleSeccion");
				 }
				 
				 UtilidadBD.closeConnection(con);
				 if(forma.getIndexNivel().equals("fija") || forma.getIndexNivel().equals("1"))
					 return mapping.findForward("seccionesFijas");
				 else
					 return mapping.findForward("detalleSeccion");
			 }			
			 //Carga la información de una Sección Parametrizable para modificarla
			 else if(estado.equals("cargarModificarSeccionParam"))
			 {
				 metodoCargarModificarSeccionParam(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			 }
			 /******************************************************************************************************/
			 //Estados para los Formularios (Secciones Parametrizables de Segundo Nivel)****************************
			 else if(estado.equals("detalleSeccionNivel1"))
			 {				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("detalleSeccion");
			 }			 
			 else if(estado.equals("modificarDetalleSeccion"))
			 {
				 errores = metodoModificarDetalleSeccion(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("detalleSeccion");
				 }
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("detalleSeccion");
			 }
			 /******************************************************************************************************/
			 //Estados para los formularios (Elementos Parametrizables)*********************************************
			 
			 //------------------------------------------------------------
			 //Elementos Sección
			 else if(estado.equals("volverSeccionFija"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesFijas");
			 }
			 else if(estado.equals("volverDetalleSeccion"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("detalleSeccion");
			 }
			 else if(estado.equals("crearSeccion"))
			 {
				 forma.setListSeccionTemporal(new ArrayList<DtoSeccionParametrizable>());
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			 }
			 else if(estado.equals("crearNuevaSeccion"))
			 {
				 metodoCrearNuevaSeccion(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			 }
			 else if(estado.equals("eliminarNuevaSeccion"))
			 {
				 metodoEliminarNuevaSeccion(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			 }
			 else if(estado.equals("guardarSeccionParam") || estado.equals("modificarSeccionParam"))
			 {		 				 
				 errores = validacionesGuardarSeccionParam(con,forma);
				 
				 if(!errores.isEmpty())
				 {
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Seccion (Nueva o Modificar)
					 if(estado.equals("guardarSeccionParam"))
						 forma.setEstado("crearSeccion");
					 else
						 forma.setEstado("cargarModificarSeccionParam");
					 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("seccion");
				 }
				 
				 errores = metodoGuardarSeccionParm(con,forma,usuario);			 
				 
				 if(!errores.isEmpty())
				 {		
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Seccion (Nueva o Modificar)
					 if(estado.equals("guardarSeccionParam"))
						 forma.setEstado("crearSeccion");
					 else
						 forma.setEstado("cargarModificarSeccionParam");
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("seccion");
				 }		 				 
				 
				 //Se modifican los estado debido a las validaciones presentes en la jsp de Seccion (Nueva o Modificar)
				 if(estado.equals("guardarSeccionParam"))
					 forma.setEstado("crearSeccion");
				 else
					 forma.setEstado("cargarModificarSeccionParam");
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			 }			
			 //------------------------------------------------------------
			 //Elementos Campos
			 //Direcciona el flujo para crear nuevos campos
			 else if(estado.equals("crearCampo"))
			 {
				 metodoCrearCampo(con,forma);				
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo"); 
			 }
			 //Inserta un nuevo campo en el array de campos
			 else if(estado.equals("crearNuevoCampo"))
			 {
				 metodoCrearNuevaCampo(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo");
			 }
			 else if(estado.equals("eliminarNuevoCampo"))
			 {
				 metodoEliminarNuevoCampo(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo");
			 }
			 //Guarda los Campos Parametrización
			 else if(estado.equals("guardarCampoParam") || estado.equals("modificarCampoParam"))
			 {
				 //Actualiza el ordenamiento de los campos 
				 forma.setListCampoTemporal(Plantillas.ordenarArrayCamposPorOrden(forma.getListCampoTemporal()));
				 
				 errores = validacionesGuardarCampoParam(forma);
				 
				 if(!errores.isEmpty())
				 {
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
					 if(estado.equals("guardarCampoParam"))
						 forma.setEstado("crearCampo");
					 else
						 forma.setEstado("cargarModificarCampoParam");
					 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("campo");
				 }
				 
				 errores = metodoGuardarCampoParam(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {				 
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
					 if(estado.equals("guardarCampoParam"))
						 forma.setEstado("crearCampo");
					 else
						 forma.setEstado("cargarModificarCampoParam");
					 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("campo");
				 }
				 
				 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
				 if(estado.equals("guardarCampoParam"))
					 forma.setEstado("crearCampo");
				 else
					 forma.setEstado("cargarModificarCampoParam");
				 				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo");				 
			 }
			 else if(estado.equals("cargarModificarCampoParam"))
			 {
				 metodoCargarModificarCampoParam(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo");
			 }
			 //--------------------------------------------------------------
			 //Estados Campos Tipo Formula 
			 else if(estado.equals("cargarFormulaCampoParam"))
			 {
				 if (!(request.getParameter("esOperacionExitosa")+"").equals("") && !(request.getParameter("esOperacionExitosa")+"").equals("null"))
					 forma.setEsOperacionExitosa(request.getParameter("esOperacionExitosa").toString());				 
				 metodoCargarFormulaCampoParam(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("formula");
			 }
			 else if(estado.equals("elimarCaracter"))
			 {				 
				 metodoEliminarFormulaCampoParam(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("formula");
			 }
			 else if(estado.equals("guardarDeclaracion"))
			 {
				 metodoGuardarDeclaracion(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("formula");
			 }
			 else if(estado.equals("restringirCamposParam"))
			 {
				 metodoRestringirCamposParam(con,forma,true);				
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campo");
			 }
				 
			 //------------------------------------------------------------
			 //Elementos Campos - Opciones
			 //Muestra el popup de opciones del campo
			 else if(estado.equals("verOpcionesCampo"))
			 {					
				 if (!(request.getParameter("esOperacionExitosa")+"").equals("") && !(request.getParameter("esOperacionExitosa")+"").equals("null"))
					 forma.setEsOperacionExitosa(request.getParameter("esOperacionExitosa").toString());
				 
				 forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setManejaImagen(forma.getManejaIMgtmp());
				 forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setCodigoTipo(forma.getCodigoTipotmp());
				 forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setImagenAsociar(forma.getImagenAsociartmp());
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }
			 //Inserta una nueva opcion para el campo
			 else if(estado.equals("crearNuevaOpcion"))
			 {
				 metodoCrearNuevaOpcion(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }
			 //elimina la opcion de campo
			 else if(estado.equals("eliminarOpcionCampo"))
			 {				 
				 metodoEliminarOpcion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }			
			 else if(estado.equals("cerrarVentaOpcion"))
			 {
				 errores = validacionesOpcionCampo(
						 forma,
						 forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())),
						 Integer.parseInt(forma.getIndexCampo()));
				 
				 if(!errores.isEmpty())
				 {	 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("opcionesCampo");
				 }			
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }
			 else if(estado.equals("eliminarCampoParam"))
			 {
				 errores = metodoEliminarCampoParam(con,forma,usuario);
				 saveErrors(request,errores);
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("detalleSeccion");				
			 }
			 //------------------------------------------------------------
			 //Elementos Campos - Opciones Asociadas
			 //Muestra el popup de Secciones/Valores Asociadas
			 else if(estado.equals("opcionesSeccion") || estado.equals("resumenOpcionesSeccion"))
			 {
				 if(estado.equals("resumenOpcionesSeccion"))
					 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
				 
				 metodoSeccionAsociada(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesSeccion");
			 }
			 else if(estado.equals("addOpcionesSeccion"))
			 {
				 metodoAddOpcionesSeccion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesSeccion");
			 }
			 else if(estado.equals("eliminarOpcionesSeccion"))
			 {
				 metodoEliminarOpcionesSeccion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesSeccion");
			 }
			 else if(estado.equals("opcionesValores") || estado.equals("resumenOpcionesValores"))
			 {				 
				 if(estado.equals("resumenOpcionesValores"))
					 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
					 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesValores");
			 }
			 else if(estado.equals("addOpcionesValores"))
			 {
				 metodoAddOpcionesValores(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesValores");
			 }
			 else if(estado.equals("eliminarOpcionesValores"))
			 {				 
				 metodoEliminarOpcionesValores(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesValores");
			 }
			 else if(estado.equals("validarOpcionesSecciones") || estado.equals("resumenValidarOpcionesSecciones"))
			 {
				 if(estado.equals("resumenValidarOpcionesSecciones"))
					 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
					 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }
			 else if(estado.equals("validarOpcionesValores")  || estado.equals("resumenValidarOpcionesValores"))
			 {
				 if(estado.equals("resumenValidarOpcionesValores"))
					 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opcionesCampo");
			 }
			 //------------------------------------------------------------
			 //Elementos Escalas
			 //Adiciona una nueva escala
			 else if(estado.equals("crearEscala"))
			 {
				 //Carga las escalas
				 forma.setArrayUtilitario1(Utilidades.consultarEscalasParam(con,Plantillas.obtenerCodigosInsertadosEscalas(forma.getPlantillaDto()),ConstantesBD.acronimoSi));				 
				 forma.setListEscalaTemporal(new ArrayList<DtoEscala>());
				 metodoNuevaEscala(con,forma);
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("escala");
			 }
			 else if(estado.equals("crearNuevaEscala"))
			 {
				 metodoNuevaEscala(con,forma);
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("escala");
			 }
			 else if(estado.equals("guardarEscala"))
			 {
				 errores = validacionesGuardarEscala(forma);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("escala");
				 }
				 
				 errores = metodoGuardarEscala(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("escala");
				 }
				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("escala");
			 }
			 else if(estado.equals("eliminarEscalaNueva"))
			 {
				 metodoEliminarEscala(con,forma,true,usuario);				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("escala");
			 }
			 else if(estado.equals("eliminarEscala"))
			 {
				 errores = metodoEliminarEscala(con,forma,false,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("seccionesFijas");
				 }
				 				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("seccionesFijas");
			 }
			 //------------------------------------------------------------
			 //------------------------------------------------------------
			 //Elementos Componentes
			 //Adiciona una nuevo Componente
			 else if(estado.equals("crearComponente"))
			 {
				 //Carga Componente
				 forma.setArrayUtilitario1(Utilidades.consultarComponentesParam(
						 con,
						 ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos+"",
						 Plantillas.obtenerCodigosInsertadosComponentes(forma.getPlantillaDto())));
				 
				 forma.setListComponenteTemporal(new ArrayList<DtoComponente>());
				 metodoNuevoComponente(con,forma);
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("componente");
			 }
			 else if(estado.equals("crearNuevoComponente"))
			 {
				 metodoNuevoComponente(con,forma);
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("componente");
			 }
			 else if(estado.equals("guardarComponente"))
			 {
				 errores = validacionesGuardarComponente(forma);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("componente");
				 }
				 
				 errores = metodoGuardarComponente(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("componente");
				 }
				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("componente");
			 }
			 else if(estado.equals("eliminarComponenteNuevo"))
			 {
				 metodoEliminarComponente(con,forma,true,usuario);				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("componente");
			 }
			 else if(estado.equals("eliminarComponente"))
			 {
				 errores = metodoEliminarComponente(con,forma,false,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("seccionesFijas");
				 }
				 				 
				 UtilidadBD.closeConnection(con);				 
				 return mapping.findForward("seccionesFijas");
			 }
			 
			 /******************************************************************************************************/
			 //Estados Para los Servicios / Diagnosticos************************************************************			 
			 else if(estado.equals("agregarServicioBusqueda"))
			 {
				 metodoAgregarServicios(forma,request);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }
			 else if (estado.equals("eliminarServicioDiag"))
			 {
				 eliminarServicioDiag(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("listado");
			 }			 
			 else if (estado.equals("modificarDiagnos"))
			 {	
				 metodoModificarDiagnos(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("diagnostico");
			 }
			 else if (estado.equals("irDiagnos"))
			 {					 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("diagnostico");
			 }
			 else if (estado.equals("nuevoDiagnos"))
			 {
				 metedoNuevoDiagnos(forma,request);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("diagnostico"); 
			 }
		 }
		 
		 return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	
	
	//**********************************************************************************************
	//Metodos***************************************************************************************
	
	/**
	 * Metodo para el estado empezar
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void metodoEmpezar(Connection con, ParametrizacionFormulariosRespuForm forma, UsuarioBasico usuario)
	{
		//Almacena la información de los formularios parametrizados
		forma.setListadoPlantillaMap(Plantillas.consultarListadoPlantillas(
				con, 
				usuario.getCodigoInstitucion(),
				ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos,
				"",
				"",
				true,
				ConstantesBD.acronimoSi));			
		
		//Consulta otros diagnosticos parametrizados
		forma.setDiagnosticosParamArray(Plantillas.consultarDiagnosticosPlantillas(con,usuario.getCodigoInstitucionInt()));		
	}
	
	//**********************************************************************************************
	
	/**
	 * Metodo que carga la plantilla preview
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void metodoPreview(Connection con,HttpServletRequest request,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	{
		forma.setPlantillaPreviewDto(new DtoPlantilla());
		int codigoPlatilla = 0;
		
		if (!(request.getParameter("codigoPlantillaPk")+"").equals("") && !(request.getParameter("codigoPlantillaPk")+"").equals("null"))
			codigoPlatilla = Utilidades.convertirAEntero(request.getParameter("codigoPlantillaPk")+"");
		
		forma.setPlantillaPreviewDto(Plantillas.cargarPlantillaParametrica(
				con, 
				codigoPlatilla, 
				usuario.getCodigoInstitucionInt(),
				ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos,
				ConstantesBD.codigoNuncaValido, 
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido, //codigo sexo paciente
				ConstantesBD.codigoNuncaValido, //dias edad paciente,
				false, //filtro datos paciente
				"",
				"",
				""));		
		
		PersonaBasica paciente = new PersonaBasica();
		paciente.setEdad(1);
		paciente.setEdadMeses(12);
		paciente.setEdadDias(365);		
		forma.getPlantillaPreviewDto().cargarEdadesPaciente(paciente);
	}
	
	//**********************************************************************************************
	
	/**
	 * Inicializa los valores para el nuevo registro
	 * @param ParametrizacionFormulariosRespuForm forma
	 * */
	private void metodoNuevoFormulario(ParametrizacionFormulariosRespuForm forma)
	{
		forma.setPlantillaSerDiagArray(new ArrayList<DtoPlantillaServDiag>());
		forma.setPlantillaMap(new HashMap());
		forma.setPlantillaMap("codigoPk","");
		forma.setPlantillaMap("estabd",ConstantesBD.acronimoNo);
		forma.setPlantillaMap("numRegistros","0");
		forma.setListadoPlantillaMap("codigosServiciosInsertados","-1");		
	}
	
	//**********************************************************************************************
	
	/**
	 * Guarda la información del encabezado del formulario (Codigo,nombre y servicios)
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param UsuarioBasico usuario
	 * @throws SQLException 
	 * */
	private ActionErrors metodoGuardarFormulario(Connection con, ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario) throws SQLException
	{
		ActionErrors errores = new ActionErrors();
		int consecutivo = 0;
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		
		UtilidadBD.iniciarTransaccion(con);
		
		//Valida que la información no se encuentre en la base de datos
		if(forma.getPlantillaMap("estabd").toString().equals(ConstantesBD.acronimoNo))
		{
			//Guarda la información del encabezado de la plantilla
			consecutivo = Plantillas.insertarPlantilla(
								con, 
								ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos, 
								usuario.getCodigoInstitucion(), 
								"", 
								"", 
								"", 
								forma.getPlantillaMap("codigoPlantilla").toString(),
								forma.getPlantillaMap("nombrePlantilla").toString(),				
								usuario,
								"",
								"");	
						
			if(consecutivo != ConstantesBD.codigoNuncaValido)
			{
				//Inserta el indicador de resultado requerido				
				Plantillas.insertarResultadosProcRequeridos(con,consecutivo+"",true,usuario);
				
				//Carga el Dto para almacenar la información de las secciones fijas lijadas a toda plantilla 
				forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(
					 con, 
					 consecutivo, 
					 usuario.getCodigoInstitucionInt(), 
					 ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos, 
					 ConstantesBD.codigoNuncaValido, 
					 ConstantesBD.codigoNuncaValido, 
					 ConstantesBD.codigoNuncaValido,
					 ConstantesBD.codigoNuncaValido,
					 ConstantesBD.codigoNuncaValido,
					 false,
					 "",
					 "",
					 ""));				
				
				if(errores.isEmpty())
				{
					if(!Plantillas.actualizarInsertarFormularioServicio(con,consecutivo,forma.getPlantillaSerDiagArray(),usuario))
						errores.add("descripcion",new ActionMessage("errors.notEspecific","No se puede Ingresar/Modificar Servicios/Diagnosticos."));
					
					//Inserta las Secciones Fijas a la Plantilla
					errores = metodoModificarPlantillasSecFijas(con, forma, usuario,true);
					
					if(errores.isEmpty())
					{					
						forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
						UtilidadBD.finalizarTransaccion(con);
					}
					else
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","No se pudo Ingresar las Secciones Fijas."));
						UtilidadBD.abortarTransaccion(con);						
					}
				}
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de la plantilla no es Valido."));
					UtilidadBD.abortarTransaccion(con);
				}						
			}
			else
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de la plantilla no es Valido."));
				UtilidadBD.abortarTransaccion(con);	
			}
		}
		else if(forma.getPlantillaMap("estabd").toString().equals(ConstantesBD.acronimoSi))
		{	
			consecutivo = Integer.parseInt(forma.getPlantillaMap("codigoPk").toString());
			
			Plantillas.actualizarPlantilla(con, 
					consecutivo, 
					"", 
					"", 
					"", 
					forma.getPlantillaMap("codigoPlantilla").toString(),
					forma.getPlantillaMap("nombrePlantilla").toString(), 
					usuario,
					ConstantesBD.acronimoSi,
					"",
					""
					);			
			
			if(!Plantillas.actualizarInsertarFormularioServicio(con,consecutivo,forma.getPlantillaSerDiagArray(),usuario))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No se puede Ingresar/Modificar Servicios/Diagnosticos."));
			
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
			UtilidadBD.finalizarTransaccion(con);
		}
		
		//Actualiza el estado que muestra el formulario de encabezado el cual sirve tanto para agregar una nueva plantilla como para modificarla
		if(!errores.isEmpty())
		{
			metodoNuevoFormulario(forma);
			forma.setEstado("nuevoFormulario");
		}
		
		return errores; 		
	}
	
	//**********************************************************************************************
	
	/**
	 * Carga la información de la plantilla para ser modificada (codigo, nombre y servicios)
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 * */
	private void cargarFormularioModificar(Connection con,ParametrizacionFormulariosRespuForm forma) throws NumberFormatException, SQLException
	{
		//Carga la información de los servicios
		forma.setPlantillaMap(new HashMap());			
		forma.setPlantillaMap("servicios_"+forma.getIndexListadoPlantilla(),ConstantesBD.acronimoNo);
		forma.setPlantillaMap("numRegistros","0");
		forma.setPlantillaSerDiagArray(new ArrayList<DtoPlantillaServDiag>());
		
		//Utilidades.imprimirMapa(forma.getListadoPlantillaMap());
				
		//Se copia el array de servicios para esa plantilla
		if(!forma.getIndexListadoPlantilla().equals("") 
				&& forma.getListadoPlantillaMap().containsKey("servicios_"+forma.getIndexListadoPlantilla()) && 
					!forma.getListadoPlantillaMap("servicios_"+forma.getIndexListadoPlantilla()).toString().equals(ConstantesBD.acronimoNo))
		{
			ArrayList<DtoPlantillaServDiag> array = (ArrayList)forma.getListadoPlantillaMap("servicios_"+forma.getIndexListadoPlantilla());
			
			for(int i=0; i<array.size(); i++)
			{
				DtoPlantillaServDiag dto = new DtoPlantillaServDiag();  
				 
				try 
				{
					PropertyUtils.copyProperties(dto,array.get(i));				
					forma.getPlantillaSerDiagArray().add(dto);	
					
				}catch(Exception e){
					logger.warn(e);								
				}
			}
			
			forma.setPlantillaMap("numRegistros",array.size());
		}
		
		//Carga la información de codigo y nombre de la plantilla
		forma.setPlantillaMap("codigoPk",forma.getListadoPlantillaMap("codigoPk_"+forma.getIndexListadoPlantilla()));
		forma.setPlantillaMap("codigoPlantilla",forma.getListadoPlantillaMap("codigoPlantilla_"+forma.getIndexListadoPlantilla()));
		forma.setPlantillaMap("nombrePlantilla",forma.getListadoPlantillaMap("nombrePlantilla_"+forma.getIndexListadoPlantilla()));
		forma.setPlantillaMap("estabd",ConstantesBD.acronimoSi);
		forma.setEstado("nuevoFormulario");
		
		forma.setListadoPlantillaMap("codigosServiciosInsertados",
				ParametrizacionFormulariosRespu.quitarAnadeServicioRestriccion(
						forma.getListadoPlantillaMap(),
						forma.getPlantillaMap("codigoPk").toString(),
						"reCalcularIndep",
						Utilidades.convertirAEntero(forma.getIndexListadoPlantilla())));		
		
 Integer indexMapa= new Integer(0);
		 
		 if(!forma.getIndexListadoPlantilla().trim().equals("")){
			 indexMapa = Integer.valueOf(forma.getIndexListadoPlantilla());
		 }
		forma.setCheckLinkOrdenesAmbulatorias(
		Plantillas.consultarVisibilidadPlantillaFijaSinOrden(con, Integer.valueOf(String.valueOf(forma.getListadoPlantillaMap().get("codigoPk_"+indexMapa))),ConstantesCamposParametrizables.formatoRespuestaCitas ));
		
		
	}
	
	//**********************************************************************************************
	/**
	 * Muestra el detalle de la Plantilla 
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param UsuarioBasico usuario
	 * */
	 private void metodoDetallePlantilla(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {		 				 
		 //Almacena la información de la plantilla parametrica 
		 forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(
				 con, 
				 Integer.parseInt(forma.getPlantillaMap("codigoPk").toString()), 
				 usuario.getCodigoInstitucionInt(), 
				 ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos, 
				 ConstantesBD.codigoNuncaValido, 
				 ConstantesBD.codigoNuncaValido, 
				 ConstantesBD.codigoNuncaValido,
				 ConstantesBD.codigoNuncaValido,
				 ConstantesBD.codigoNuncaValido,
				 false,
				 "",
				 "",
				 ""));
		 
		 //Consulta el indicador de resultado requerido				
		 forma.setPlantillaMap("requerido",Plantillas.consultarResultadosProcRequeridos(con,forma.getPlantillaDto().getCodigoPK()));
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Elimina una plantilla 
	  * @param Connection con
	  *	@param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private void metodoEliminarPlantilla(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {		
		 UtilidadBD.iniciarTransaccion(con);		 
		 int consecutivo = Utilidades.convertirAEntero(forma.getPlantillaMap("codigoPk").toString());
		 
		 logger.info("valor de la plantilla a eliminar >> "+consecutivo);
			
		 Plantillas.actualizarPlantilla(con, 
				consecutivo, 
				"", 
				"", 
				"", 
				forma.getPlantillaMap("codigoPlantilla").toString(),
				forma.getPlantillaMap("nombrePlantilla").toString(), 
				usuario,
				ConstantesBD.acronimoNo,
				"",
				"");			
				 			
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
		 forma.setEstado("empezar");
		 
		 UtilidadBD.finalizarTransaccion(con);		 
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Metodo que modifica la información de las secciones fijas parametrizadas
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * @param boolean continuarTransaccion
	 * @throws SQLException 
	  * */
	 private ActionErrors metodoModificarPlantillasSecFijas(
			 Connection con,
			 ParametrizacionFormulariosRespuForm forma,
			 UsuarioBasico usuario,
			 boolean continuarTransaccion) throws SQLException
	 {
		 //Recorre el array de secciones fijas y evalua si encuentran parametrizadas o No
		 int numRegistros = forma.getPlantillaDto().getSeccionesFijas().size();
		 int consecutivo = ConstantesBD.codigoNuncaValido;
		 ActionErrors errores = new ActionErrors();
		 DtoElementoParam elemento;
		 
		 if(!continuarTransaccion)
		 {
			 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);		
			 UtilidadBD.iniciarTransaccion(con);
		 }
		 	
		 //Verifica que se encuentre el campo requerido
		 if(forma.getPlantillaMap().containsKey("requerido") && 
				 !forma.getPlantillaMap("requerido").toString().equals(""))
		 {		 
			 //Modifica el indicador de resultado requerido	 
			 Plantillas.actualizarResultadosProcRequeridos(
					 con,
					 forma.getPlantillaDto().getCodigoPK(),
					 UtilidadTexto.getBoolean(forma.getPlantillaMap("requerido").toString()),
					 usuario);
		 }
		 

		 for(int i=0; i<numRegistros; i++)
		 {			 
			 //Valida si la sección fija se encuentra relacionada con la plantilla
			 if(!forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPK().equals(""))
			 {				 
				 Plantillas.actualizarPlantillaSeccionesFijas(
						 con, 
						 forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPK(), 
						 forma.getPlantillaDto().getSeccionesFijasPos(i).getOrden(),
						 forma.getPlantillaDto().getSeccionesFijasPos(i).isVisible(),
						 forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPkFunParamSecFij(),
						 forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoSeccionParam(),
						 usuario);
				 
				 //Solo actualiza el contenido de las seccion fijas, las demas son actualizadas en el procedimiento anterior
				 if(forma.getPlantillaDto().getSeccionesFijasPos(i).isEsFija())
				 {
					 //Recorre los elementos de Primer Nivel para la sección 
					 for(int j = 0; j<forma.getPlantillaDto().getSeccionesFijasPos(i).getElementos().size(); j++)
					 {						 
						 elemento = forma.getPlantillaDto().getSeccionesFijasPos(i).getElementosPos(j);					 
						 
						 //Valida que el elemento sea de tipo Sección
						 if(elemento.isSeccion())
						 {					
							 Plantillas.actualizarMostrarModOrdenSeccionParam(
									 con, 
									 elemento.isMostrarModificacion(),
									 elemento.getOrden()+"", 
									 elemento.getCodigoPK(), 
									 usuario);
						 }
					 }
				 }
			 }
			 else
			 {
				 if(Plantillas.insertarPlantillasSeccionesFijas(
						 con, 
						 forma.getPlantillaDto().getCodigoPK(), 
						 forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPkFunParamSecFij(), 
						 "", 
						 (i+1), 
						 true, 
						 usuario) == ConstantesBD.codigoNuncaValido)
				 {
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Plantillas Secciones Fijas es Incorrecto."));
					 return errores;						 
				 }
			 }
		 }	 
		 
		 if(!continuarTransaccion)
		 {
			 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
			 UtilidadBD.finalizarTransaccion(con);	 
			 //Recarga el Dto de Plantilla
			 metodoDetallePlantilla(con, forma, usuario);
		 }
		 
		 
		 metodoModificarPlantillasSecFijasSinOrden(con, forma, usuario);
		 
		 return errores;		 		 		 
	 }	 
	 
	 
	 
	 private void metodoModificarPlantillasSecFijasSinOrden(
			 Connection con,
			 ParametrizacionFormulariosRespuForm forma,
			 UsuarioBasico usuario) throws SQLException{
		 
		 Integer indexMapa= new Integer(0);
		 
		 if(!forma.getIndexListadoPlantilla().trim().equals("")){
			 indexMapa = Integer.valueOf(forma.getIndexListadoPlantilla());
		 }
		 //codigoPk_
		 Integer codigoPlantilla=Integer.valueOf(String.valueOf(forma.getListadoPlantillaMap().get("codigoPk_"+indexMapa)));
		 
		Boolean exsitePlantilla= Plantillas.consultarPlantillaFijaSinOrden(con, codigoPlantilla,ConstantesCamposParametrizables.formatoRespuestaCitas );
		
		if(!exsitePlantilla){
			Plantillas.guardarPlantillaSinOrden(con, codigoPlantilla,
					forma.getCheckLinkOrdenesAmbulatorias(),
					IConstanteSeccionsFijasSinOrden.SECCION_ENLACES_ORDENES_AMBULATORIAS,null,null,ConstantesCamposParametrizables.formatoRespuestaCitas);
		}else{
			Plantillas.actualziarPlantillaFijaSinOrden(con, codigoPlantilla, forma.getCheckLinkOrdenesAmbulatorias(),-1,-1,ConstantesCamposParametrizables.formatoRespuestaCitas);
		}
		
		 
		 
	 }
	 
	 
	//*********************************************************************************************
	 
	 /**
	  * Metodo que elimina la Sección Parametrica
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 public ActionErrors metodoEliminarSeccionParam(Connection con,ParametrizacionFormulariosRespuForm forma, UsuarioBasico usuario)
	 {
		 ActionErrors errores = new ActionErrors();
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);
		 String codigoPkSeccionParam = "", codigoPkPlantillasSecc = ""; 
			
		 UtilidadBD.iniciarTransaccion(con);
		 
		 if(forma.getIndexNivel().equals("fija"))
		 {
			 codigoPkSeccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getCodigoSeccionParam();
			 codigoPkPlantillasSecc = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(0).getConsecutivoParametrizacion();
		 }
		 else if(forma.getIndexNivel().equals("1"))
		 { 
			 codigoPkSeccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCodigoPK();
			 codigoPkPlantillasSecc = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getConsecutivoParametrizacion();
		 }
		 else if(forma.getIndexNivel().equals("2"))
			 codigoPkSeccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getCodigoPK();
				 
		 if(!codigoPkPlantillasSecc.equals(""))
		 {
			//Desasocia la seccion de los campos tipo select que la tengan asociadas													
			logger.info("Desasociar Seccion de Opciones >> codigo Pk Plantillas Secciones >> "+codigoPkPlantillasSecc);			
			Plantillas.desasociarSeccionOculta(
					con,
					codigoPkPlantillasSecc,
					false,
					usuario);
		 }
		 
		 //Actualiza la información de la seccion parametrizable
		 Plantillas.actualizarMostrarModOrdenSeccionParam(
				 con,
				 false,
				 "",
				 codigoPkSeccionParam,
				 usuario);
		 
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
		UtilidadBD.finalizarTransaccion(con);		 
		//Recarga el Dto de Plantilla
		metodoDetallePlantilla(con, forma, usuario);
		return errores;
	 }
	 
	//**********************************************************************************************
	 
	/**
	 * Modifica la información de una Seccion Parametrizable 
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * */
	 public void metodoCargarModificarSeccionParam(Connection con,ParametrizacionFormulariosRespuForm forma, UsuarioBasico usuario)
	 {		 				 
		forma.setListSeccionTemporal(new ArrayList<DtoSeccionParametrizable>());
		String codigoPkSeccion = "";
				
		if(forma.getIndexNivel().equals("fija"))		
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getCodigoSeccionParam();	
		else if(forma.getIndexNivel().equals("1"))
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCodigoPK();
		else if(forma.getIndexNivel().equals("2"))
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getCodigoPK();
					
					
		//Carga la información de la sección Parametrizable, ya que se envia el codigoPk solo retornara un valor
		forma.setListSeccionTemporal(Plantillas.consultarSeccionesParametrizables(
				con,
				usuario.getCodigoInstitucionInt(),
				codigoPkSeccion));
		
		//Almacena la información del tipo de sección, ya que si cambia se debe dar un mensaje al usuario
		forma.setPlantillaMap("tipoSeccionAux",forma.getListSeccionTemporalPos(0).getTipoSeccion());
		forma.setPlantillaMap("indicativoRestriccionValCampAux",forma.getListSeccionTemporalPos(0).getIndicativoRestriccionValCamp());
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Actualiza los elementos presentes en el detalle de una seccion de Primer Nivel
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 public ActionErrors metodoModificarDetalleSeccion(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		 ActionErrors errores  = new ActionErrors();
		 int numRegistros = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento())).getSecciones().size();
		 DtoSeccionParametrizable seccionParam;
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		 UtilidadBD.iniciarTransaccion(con);
		 
		 //Recorre los elementos pertenecientes a la sección de primer nivel 
		 for(int i = 0 ; i < numRegistros; i++)
		 {
			 seccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(i);
			 
			 Plantillas.actualizarMostrarModOrdenSeccionParam(
					 con,
					 seccionParam.isMostrarModificacion(), 
					 seccionParam.getOrden()+"",
					 seccionParam.getCodigoPK()+"",
					 usuario);
		 }	 
		 
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		 UtilidadBD.finalizarTransaccion(con);	 
		 //Recarga el Dto de Plantilla
		 metodoDetallePlantilla(con, forma, usuario);
		 return errores;	 
	 }
	//**********************************************************************************************
	 
	 
	 /**
	  * Valida la información antes de guardar
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma	
	  * */
	 public ActionErrors validacionesGuardarSeccionParam(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		ActionErrors errores = new ActionErrors();
		
		//Organiza los elementos dependiendo del orden 		
		int numRegistros = forma.getListSeccionTemporal().size();
		
		//Solo ordena el array cuando se ingresan nuevas secciones, debido a que si se va ha modificar
		//una seccion ya parametrizada solo se cargara una en el array y no se le puede dar orden con respecto a otras
		if(forma.getEstado().equals("guardarSeccionParam"))
		{
			ArrayList<DtoSeccionParametrizable> tmp = new ArrayList<DtoSeccionParametrizable>();
					
			for(int j = 0; j<numRegistros; j++)		
				tmp.add(new DtoSeccionParametrizable());			
							
			for(int j = 0; j<numRegistros; j++)		
				tmp.set(forma.getListSeccionTemporalPos(j).getOrden()-1,forma.getListSeccionTemporalPos(j));		
			
			forma.setListSeccionTemporal(tmp);		
		}
		//--------------------------------------------
		
		for(int i = 0; i < numRegistros; i++)
		{		
			//Valida el codigo 
			if(forma.getListSeccionTemporalPos(i).getCodigo().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Código del Registro Nro. "+(i+1)));		
						
			//Valida el numero de columnas
			if((forma.getListSeccionTemporalPos(i).getColumnasSeccion()+"").equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero de Columnas del Registro Nro. "+(i+1)));
			else
			{
				if(forma.getListSeccionTemporalPos(i).getColumnasSeccion() == 0 || forma.getListSeccionTemporalPos(i).getColumnasSeccion() > 500)
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El numero Maximo de Columnas para el Registro Nro. "+(i+1)+" debe estar entre un Rango de 1 a 500 "));			 
			}
			
			//Valida la edad Inicial y Edad Final
			if(!forma.getListSeccionTemporalPos(i).getRangoInicialEdad().equals("") ||
				!forma.getListSeccionTemporalPos(i).getRangoFInalEdad().equals(""))
			{
				if(Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoInicialEdad()) < 0)
					errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","El Campo Edad Inicial del Registro Nro. "+(i+1),"0"));
				else
				{
					if(Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoInicialEdad()) > Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoFInalEdad()))					
						errores.add("descripcion",new ActionMessage("errors.integerMenorQue","El Campo Edad Inicial del Registro Nro. "+(i+1)," El Campo Edad Final del mismo Registro "));					
				}
				
				if(Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoFInalEdad()) < 0)
					errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","El Campo Edad Final del Registro Nro. "+(i+1),"0"));
			}				
			
			//Verifica que no existan codigos Repetidos entre si
			String mensaje = "";
			for(int j=0; j<numRegistros; j++) 
			{
				if(forma.getListSeccionTemporalPos(i).getCodigo().equals(forma.getListSeccionTemporalPos(j).getCodigo()) && 
						i!=j)
					mensaje = "El Codigo";
				
				if(!forma.getListSeccionTemporalPos(i).getDescripcion().equals("") && 
						forma.getListSeccionTemporalPos(i).getDescripcion().equals(forma.getListSeccionTemporalPos(j).getDescripcion()) && 
							i!=j)
				{
					if(mensaje.equals(""))
						mensaje = "EL Nombre";
					else
						mensaje += " y Nombre ";
				}
				
				if(!mensaje.equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific",mensaje+" del  Registro Nro. "+(i+1)+" se encuentra repetido con "+mensaje+" del Registro Nro. "+(j+1)+". Verificar los Nuevos Registros."));
					return errores;
				}
			}
			
			if(errores.isEmpty())
			{
				//Verifica que no exista codigos repetidos entre las secciones parametrizadas anteriormente
				if(!Plantillas.validarRepetidoSeccionParametrica(
						con,
						forma.getPlantillaDto(),
						forma.getListSeccionTemporalPos(i),
						forma.getIndexNivel(),
						forma.getIndexSeccionFija()))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro Nro. "+(i+1)+" se encuentra repetido con Secciones Ya Existentes (Codigo y/o Nombre de Sección)."));
					return errores;
				}
			}			
		}
				
		return errores;
	 }	 
	 
	//**********************************************************************************************
	 
	 /**
	  * Guarda la información de una sección parametrizable
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 public ActionErrors metodoGuardarSeccionParm(
			 Connection con,
			 ParametrizacionFormulariosRespuForm forma,
			 UsuarioBasico usuario)
	 {
		 ActionErrors errores =  new ActionErrors();
		 
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);	 
		 UtilidadBD.iniciarTransaccion(con);
		 
		 if(forma.getEstado().equals("guardarSeccionParam"))
		 {
			 //Inserta los registros
			 errores = Plantillas.insertarSeccionesParametrizable(
					 con, 
					 forma.getPlantillaDto(), 
					 forma.getListSeccionTemporal(), 
					 forma.getIndexSeccionFija(), 
					 forma.getIndexElemento(),					  
					 forma.getIndexSeccionNivel2(), 
					 forma.getIndexNivel(), 
					 usuario,
					 true);
		 }
		 else if(forma.getEstado().equals("modificarSeccionParam"))
		 {
			 //Modifica los registros
			 errores = Plantillas. modificaSeccionesParametrizable(
					 con, 
					 forma.getPlantillaDto(), 
					 forma.getListSeccionTemporal(), 
					 forma.getIndexSeccionFija(), 
					 forma.getIndexElemento(),					  
					 forma.getIndexSeccionNivel2(), 
					 forma.getIndexNivel(), 
					 usuario,
					 true);		 
		 }		 
		 
		 if(!errores.isEmpty())
		 {
			 UtilidadBD.abortarTransaccion(con);
			 return errores;
		 }
		 else
		 {
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
			//Recarga el Dto de Plantilla
			metodoDetallePlantilla(con, forma, usuario);
			
			if(forma.getEstado().equals("guardarSeccionParam"))
				//Recarga el array de secciones temporales
				forma.setListSeccionTemporal(new ArrayList<DtoSeccionParametrizable>());						
						
			UtilidadBD.finalizarTransaccion(con);
		 }			  
		 
		 return errores;
	 }
	 
	 //**********************************************************************************************	 
	 /**
	  * Metodo para crear las secciones 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoCrearNuevaSeccion(ParametrizacionFormulariosRespuForm forma)
	 {		 		 
		 //Reinicia el DtoElemento temporal
		 DtoSeccionParametrizable dtoSeccionParam = new DtoSeccionParametrizable();
		 dtoSeccionParam.setDescripcion("");
		 dtoSeccionParam.setCodigo("");
		 dtoSeccionParam.setColumnasSeccion(1);
		 dtoSeccionParam.setVisible(true);
		 dtoSeccionParam.setTipoSeccion("");
		 dtoSeccionParam.setIndicativoRestriccionValCamp("");
		 
		 //Captura el orden, lo ubica en la ultima posición
		 int numRegistros = forma.getListSeccionTemporal().size();		 
		 dtoSeccionParam.setOrden(numRegistros+1);	 
		 
		 forma.getListSeccionTemporal().add(dtoSeccionParam);
		 forma.setEstado("crearSeccion");
		 
		 //Almacena la información del tipo de sección, ya que si cambia se debe dar un mensaje al usuario
		 forma.setPlantillaMap("tipoSeccionAux","");
		 forma.setPlantillaMap("indicativoRestriccionValCampAux","");
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Metodo para eliminar un seccion parametrizable no guardada aun
	  * @param  ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoEliminarNuevaSeccion(ParametrizacionFormulariosRespuForm forma)
	 {		 
		 if(Utilidades.convertirAEntero(forma.getIndexElemento()) >= 0  && 
				 Utilidades.convertirAEntero(forma.getIndexElemento()) <= forma.getListSeccionTemporal().size())
		 {
			 forma.getListSeccionTemporal().remove(Utilidades.convertirAEntero(forma.getIndexElemento()));
			 forma.setEstado("crearSeccion");
		 }
	 }
	 
	//**********************************************************************************************
	 /**
	  * Metodo para iniciar la creacion de campo
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoCrearCampo(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		 forma.getListCampoTemporal().clear();
		 forma.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());
		 forma.setArrayUtilitario1(Utilidades.consultarTiposCampo(con));
		 forma.setArrayUtilitario2(Utilidades.consultarUnidadesCampoParam(con));			
	 }
	//**********************************************************************************************
	 
	 /**
	  * Metodo para crear Campo
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoCrearNuevaCampo(ParametrizacionFormulariosRespuForm forma)
	 {
		boolean esPermitidoAsocidos = false;		
		
		//Indicador Restricción por Valores de Campo de la Sección Contenedora
		if(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
			 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento()))
			 	.getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi))
			 esPermitidoAsocidos = false;
		else
			esPermitidoAsocidos = true;
		 
		 
		//Carga la Informacion de la seccion seleccionado
		if(forma.getIndexNivel().equals("2"))
		{			 
			forma.setPlantillaMap("maxNumColumnas",forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
					 	.getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getColumnasSeccion());
		}
		else if(forma.getIndexNivel().equals("3"))
		{			 
			forma.setPlantillaMap("maxNumColumnas",forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
					 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getColumnasSeccion());
		}
		
		//Reinicio el DtoCampo temporal
		DtoCampoParametrizable dtoCampo = new DtoCampoParametrizable();
		dtoCampo.setCodigo("");
		dtoCampo.setNombre("");
		dtoCampo.setEtiqueta("");
		dtoCampo.setTipoHtml("");
		dtoCampo.setTamanio(0);
		dtoCampo.setSigno("");
		dtoCampo.setCodigoUnidad(0);
		dtoCampo.setValorPredeterminado("");
		dtoCampo.setMaximo(0);
		dtoCampo.setMinimo(0);
		dtoCampo.setCodigoUnidad(ConstantesBD.codigoNuncaValido);
		dtoCampo.setDecimales(0);
		dtoCampo.setOpciones(new ArrayList<DtoOpcionCampoParam>());
		dtoCampo.setUnicoXFila(false);
		dtoCampo.setColumnasOcupadas(0);
		dtoCampo.setRequerido(false);
		dtoCampo.setPermitirAsociados(esPermitidoAsocidos);
		 
		//Captura el orden, lo ubica en la ultima posición
		int numRegistros = forma.getListCampoTemporal().size();
		dtoCampo.setOrden(numRegistros+1);
		 
		forma.getListCampoTemporal().add(dtoCampo);
		forma.setEstado("crearCampo");
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Metodo para eliminar un nuevo campo 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoEliminarNuevoCampo(ParametrizacionFormulariosRespuForm forma)
	 {		 
		 if(Utilidades.convertirAEntero(forma.getIndexCampo()) >= 0  && 
				 Utilidades.convertirAEntero(forma.getIndexCampo()) <= forma.getListCampoTemporal().size())
		 {
			 forma.getListCampoTemporal().remove(Utilidades.convertirAEntero(forma.getIndexCampo()));
			 forma.setEstado("crearCampo");
		 }
	 }
	 
	//**********************************************************************************************
	 
	 /**
	  * Validaciones antes de guardar informacion de los Campos Parametrizables
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private ActionErrors validacionesGuardarCampoParam(ParametrizacionFormulariosRespuForm forma)
	 {
		 ActionErrors errores = new ActionErrors();
		 DtoCampoParametrizable campo;
		 
		 //Recorremos los campos 
		 for(int i = 0; i < forma.getListCampoTemporal().size(); i++)
		 {
			 campo = forma.getListCampoTemporal().get(i);
			 
			//Validamos el codigo
			 if(campo.getCodigo().equals(""))
				 errores.add("descripcion",new ActionMessage("errors.required","El Codigo del Registro Nro. "+(i+1)));
			 
			 //Validamos el nombre
			 if(campo.getNombre().equals(""))
				 errores.add("descripcion",new ActionMessage("errors.required","El Nombre del Registro Nro. "+(i+1)));
			 
			 //Validamos el tipo		 			 
			 if(campo.getCodigoTipo() == ConstantesBD.codigoNuncaValido)
			 {
				 errores.add("descripcion",new ActionMessage("errors.required","El Tipo del Registro Nro. "+(i+1)));
			 }
			 else
			 {				 					 
				 if(campo.isUsadoFormula() 
						 && !(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero ||
						 			campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal))
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Tipo del Registro Nro. "+(i+1)+" no puede ser modificado, esta siendo usado en el Campo Tipo Formula [ "+campo.getInfoFormulaMeUsa().getNombre()+" ] "));					 
			 
				 //Cuando el tipo es Texto Predeterminado
				 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
				 {
					 if(campo.getEtiqueta().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.required","La Etiqueta del Registro Nro. "+(i+1)));
				 }
				 
				 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoCaracter || 
						 campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoAreaTexto ||						 	 
						 		campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero ||
						 			campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal)
				 {
					 if(campo.getTamanio() <= 0)
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Tamaño del Registro Nro. "+(i+1)+" debe ser un Entero mayo a cero"));
				 }				 
				 
				 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero || 
						 campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal)
				 {
					 if(campo.getSigno().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.required","El Signo del Registro Nro. "+(i+1)));
										 
					 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero)
					 {
						 if(!UtilidadTexto.isNumber(campo.getMaximo()+""))
							 errores.add("descripcion",new ActionMessage("errors.required","El Máximo del Registro Nro. "+(i+1)+" debe ser un Entero."));
						 
						 if(!UtilidadTexto.isNumber(campo.getMinimo()+""))						 
							 errores.add("descripcion",new ActionMessage("errors.required","El Mínimo del Registro Nro. "+(i+1)+" debe ser un Entero."));						 
					 }
					 else if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal)
					 {						 
						 if(!UtilidadTexto.isNumber(campo.getMaximo()+""))						 
							 errores.add("descripcion",new ActionMessage("errors.required","El Máximo del Registro Nro. "+(i+1)+" debe ser un Decimal. "));
						 
						 if(!UtilidadTexto.isNumber(campo.getMinimo()+""))						 
							 errores.add("descripcion",new ActionMessage("errors.required","El Mínimo del Registro Nro. "+(i+1)+" debe ser un Decimal. "));
					 }
					 
					 if(errores.isEmpty())
					 {
						 if(campo.getMaximo() < campo.getMinimo())
							 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Valor Máximo del Registro Nro. "+(i+1)+" debe ser mayor al Valor Mínimo del mismo Registro. "));							 
					 }
					 
					 
					 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal)
					 {
						 if(campo.getDecimales() <= 0)
							 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Numero de Decimales del Registro Nro. "+(i+1)+" debe ser un Entero mayor a cero"));						 
					 }					 
				 }
				 
				 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo ||
						 campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion)
				 {
					 errores.add(validacionesOpcionCampo(forma,campo,i));
				 }
				 
				 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)				
				 {					 
					 if(campo.getFormulaCompleta().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","La Declaración de la formula del Registro Nro. "+(i+1)+" es Requerida."));
					 					 
					 if(campo.getDecimales() <= 0)
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Numero de Decimales del Registro Nro. "+(i+1)+" debe ser un Entero mayor a cero")); 	
				 }
			 }		
			 
			 if(!campo.isUnicoXFila())
			 {
				 if(campo.getColumnasOcupadas() <= 0)
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Las Columnas Ocupadas del Registro Nro. "+(i+1)+" debe ser un Entero mayor a cero"));					 
			 }
		 }
		 
		 return errores; 
	 }
	 
	//*********************************************************************************************
	 
	 /**
	  * Guarda Campo Parametrizable
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoGuardarCampoParam(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		ActionErrors errores  = new ActionErrors();
		int codigoPkPlantillaSec = ConstantesBD.codigoNuncaValido;
		ArrayList<DtoCampoParametrizable> arrayAnterior = new ArrayList<DtoCampoParametrizable>(); 		
		
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		UtilidadBD.iniciarTransaccion(con);
		
		if(forma.getIndexNivel().equals("2"))
		{
			codigoPkPlantillaSec = Integer.parseInt(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
					getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getConsecutivoParametrizacion());
		
			//Carga la Información de la seccion Selecciona Original, sin modificaciones
			arrayAnterior = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
					 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos();
		}
		if(forma.getIndexNivel().equals("3"))
		{
			codigoPkPlantillaSec = Integer.parseInt(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
					getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).
						getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).
							getConsecutivoParametrizacion());
			
			//Carga la Información de la seccion Selecciona Original, sin modificaciones
			arrayAnterior = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
			 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2()))
			 	.getCampos();		
			
			/*logger.info("valor de nombre >> "+forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
			 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2()))
			 	.getCampos().get(0).getNombre());*/			
		}
				
		//Operaciones segun el estado de la forma		
		if(forma.getEstado().equals("guardarCampoParam"))
		{
			//Actualiza los valores del orden de cada campo de acuerdo al numero de elementos presentes
			for(int i = 0; i < forma.getListCampoTemporal().size(); i++)
				forma.getListCampoTemporal().get(i).setOrden(forma.getListCampoTemporal().get(i).getOrden()+arrayAnterior.size());
			
			errores = Plantillas.insertarCamposParametrizablesSeccion(
					con,
					codigoPkPlantillaSec+"",
					forma.getListCampoTemporal(),
					usuario);
		}
		else if(forma.getEstado().equals("modificarCampoParam"))
		{
			errores = Plantillas.actualizarCamposParametrizables(
					con, 
					codigoPkPlantillaSec+"",
					forma.getListCampoTemporal(),
					arrayAnterior,
					usuario);				
		}		
		
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			return errores;
		}	 		
		
		if(forma.getEstado().equals("guardarCampoParam"))
		{
			//Recarga el array de Campo Temporales
			forma.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());
		}				
		
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		UtilidadBD.finalizarTransaccion(con);	
		//Recarga el Dto de Plantilla
		metodoDetallePlantilla(con, forma, usuario);		
		return errores;		
	 }
	 
	 //*********************************************************************************************
	 
	 /**
	  * Carga la informacion necesaria para la modificacion de la formula
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoCargarFormulaCampoParam(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {	
		 //Carga la información de la formula
		 String formula = "";
		 forma.setFormulaMap(new HashMap());
		 forma.setFormulaMap("numRegistros","0");
		 forma.setFormulaMap("formula","");		 
		 DtoCampoParametrizable campoParam = new DtoCampoParametrizable();
		 DtoElementoParam elementoParam = new DtoElementoParam();
				 
		 if(forma.getIndexNivel().equals("2"))
		 {			 
			 elementoParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento()));
			 
			 formula = forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getFormulaCompleta();		 
		 }
		 else if(forma.getIndexNivel().equals("3"))
		 {			 
			 elementoParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2()));
			 
			 formula = forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getFormulaCompleta();			 				 
		 }
		 
		 logger.info("valor de la formula >> "+formula);
		 
		 if(!formula.toString().equals(""))
		 {
			 String [] formulaArray = formula.split(ConstantesBD.separadorSplit);			 
			 
			 for(int i=0; i<formulaArray.length; i++)
			 {
				forma.setFormulaMap("orden_"+i,i);
				forma.setFormulaMap("valor_"+i,formulaArray[i]);				
				forma.setFormulaMap("activo_"+i,ConstantesBD.acronimoSi);
				forma.setFormulaMap("tipo_"+i,Plantillas.tipoCaracterFormula(formulaArray[i]).toString());				
				
				if(forma.getFormulaMap("tipo_"+i).toString().equals("campos"))	
				{
					//logger.info("valor de la key >> "+formulaArray[i].replace("__",""));
					campoParam = elementoParam.getCampoParametrizable(formulaArray[i].replace("__",""));
										
					if(campoParam != null)					
						forma.setFormulaMap("descripcion_"+i,campoParam.getNombre());					
					else
						forma.setFormulaMap("descripcion_"+i,"error");			
				}
				else if(forma.getFormulaMap("tipo_"+i).toString().equals("otrasconstantes"))
				{					
					if(formulaArray[i].replace("__","").toString().equals(ConstantesCamposParametrizables.edadPacienteAnios))
						forma.setFormulaMap("descripcion_"+i,"Edad Paciente Años");
					else if(formulaArray[i].replace("__","").toString().equals(ConstantesCamposParametrizables.edadPacienteMeses))
						forma.setFormulaMap("descripcion_"+i,"Edad Paciente Meses");
					else if(formulaArray[i].replace("__","").toString().equals(ConstantesCamposParametrizables.edadPacienteDias))
						forma.setFormulaMap("descripcion_"+i,"Edad Paciente Dias");
					else
						forma.setFormulaMap("descripcion_"+i,"error");
				}
				else
					forma.setFormulaMap("descripcion_"+i,formulaArray[i]);				
			 }			
			 
			 forma.setFormulaMap("numRegistros",formulaArray.length);
		 }	
		 
		 cargarCamposFormula(forma);
		 
		//Utilidades.imprimirMapa(forma.getFormulaMap());
	 }
	 
	 //*********************************************************************************************
	 
	 /**
	  * Carga los campos para la formula	
	  * @param boolean baseDatos
	  * */
	 public void cargarCamposFormula(ParametrizacionFormulariosRespuForm forma)
	 {
		 HashMap respuesta = new HashMap();
		 respuesta.put("numRegistros","0");
		 		 
		 if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getCodigoPK().equals(""))		 
			 forma.setListCamposFormula(forma.getListCampoTemporal());		 
		 else
		 {			 
			 if(forma.getIndexNivel().equals("2"))
			 {		
				 forma.setListCamposFormula(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
				 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos());						 
			 }
			 else if(forma.getIndexNivel().equals("3"))
			 {			 
				 forma.setListCamposFormula(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
				 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getCampos());						 				 
			 }			
		 }		 		 
	 }
	 
	 //********************************************************************************************* 
	 /**
	  * Elimina un caracter de la formula
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoEliminarFormulaCampoParam(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {					 
		 HashMap nuevaFormula = new HashMap(); 
		 int cont = 0; 
		 
		 //Ordenar el mapa
		 for(int i=0; i<Integer.parseInt(forma.getFormulaMap("numRegistros").toString()); i++)
		 {
			 nuevaFormula.put("orden_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("orden_"+i));
			 nuevaFormula.put("valor_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("valor_"+i));
			 nuevaFormula.put("activo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("activo_"+i));
			 nuevaFormula.put("tipo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("tipo_"+i));
			 nuevaFormula.put("descripcion_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("descripcion_"+i));
		 }
		 		 
		 for(int i=0; i<Integer.parseInt(forma.getFormulaMap("numRegistros").toString()); i++)
		 {
			 if(forma.getFormulaMap("activo_"+i).toString().equals(ConstantesBD.acronimoSi))
			 {				
				 nuevaFormula.put("orden_"+cont,cont);
				 nuevaFormula.put("valor_"+cont,forma.getFormulaMap("valor_"+i));
				 nuevaFormula.put("activo_"+cont,ConstantesBD.acronimoSi);
				 nuevaFormula.put("tipo_"+cont,forma.getFormulaMap("tipo_"+i));
				 nuevaFormula.put("descripcion_"+cont,forma.getFormulaMap("descripcion_"+i));
				 cont++;				
			 }
		 }
		 
		 nuevaFormula.put("formula","");		 
		 forma.setFormulaMap(nuevaFormula);
		 forma.setFormulaMap("numRegistros",cont);
		 
		 //Utilidades.imprimirMapa(forma.getFormulaMap());
	 }
	 
	 //*********************************************************************************************
	 
	 /**
	  * Metodo para guardar la declaración de la formula
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoGuardarDeclaracion(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		 String formulaComprobacion = "";
		 String formula = "";
		 HashMap nuevaFormula = new HashMap();
		 boolean indicador = false;
		 Random rando = new Random();
		 int numRegistros = Integer.parseInt(forma.getFormulaMap("numRegistros").toString());
		 
		 //Ordenar el mapa
		 for(int i=0; i < numRegistros; i++)
		 {
			 nuevaFormula.put("orden_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("orden_"+i));
			 nuevaFormula.put("valor_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("valor_"+i));
			 nuevaFormula.put("activo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("activo_"+i));
			 nuevaFormula.put("tipo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("tipo_"+i));
			 nuevaFormula.put("descripcion_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("descripcion_"+i));
		 }		 
		 		 
		 Utilidades.imprimirMapa(nuevaFormula);
		 
		 //Recorre la formula parametrizada		 
		 for(int i = 0 ; i < numRegistros; i++)
		 {
			 indicador = false;
			 
			 //Validacion para cuando se de este caso = ConstanteCampo o CampoCampo o ConstanteConstante o CampoConstante
			 if(i>0 && (Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("campos") 
					 		|| Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("constantes")
					 			|| Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("otrasconstantes"))
					 			&& (Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("campos") 
					 					|| Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("constantes") 
							 					|| Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("otrasconstantes")))
				 {
				 	logger.info("Validacion formula. Error No. 1. ConstanteCampo o CampoCampo o ConstanteConstante o CampoConstante o OtrasConstantesCampo");
				 	formulaComprobacion+="*/*/*";
				 	indicador = true;
				 }			 			 
			 
			 if(!indicador)
			 {
				 if(Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("campos") || 
					 Plantillas.tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("otrasconstantes"))
				 {
					 formulaComprobacion+=Math.abs(rando.nextInt(1000))+"";
				 }
				 else
					 formulaComprobacion+=nuevaFormula.get("valor_"+i).toString();
			 }
			 
			 formula += nuevaFormula.get("valor_"+i).toString()+ConstantesBD.separadorSplit;			 
		 } 
		 
		 nuevaFormula.put("formula",formula);		 
		 forma.setFormulaMap(nuevaFormula);
		 forma.setFormulaMap("numRegistros",numRegistros);
		 forma.setFormulaComprobar(formulaComprobacion);
		 logger.info("valor de la formula >> "+formula+" >> "+formulaComprobacion);
		 forma.setEstado("respuestaFormulaValidada");	
	 } 
	 
	 	 
	 //*********************************************************************************************
	 
	 /**
	  * Metodo restringir los campos que han sido usados en la formula
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param esLLamadoPopUp
	  * */
	 private void metodoRestringirCamposParam(Connection con,ParametrizacionFormulariosRespuForm forma,boolean esLLamadoPopUp)
	 {
		 //Carga la información de la formula		 
		 DtoCampoParametrizable campoParam = new DtoCampoParametrizable();	
		 String [] formulaArray; 
		 
		 //Esta lleno solo cuando se va a modificar el campo que posee la formula
		 if(esLLamadoPopUp && !forma.getIndexCampo().equals(""))
			 logger.info("valor de la formula >> "+forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getFormulaCompleta());
		 
		 
		 //Inicializa todos los campos en Usado Formula 
		 for(int ca=0; ca<forma.getListCampoTemporal().size(); ca++)
			 forma.getListCampoTemporal().get(ca).setUsadoFormula("","", false);			 
		 
		 for(int ca=0; ca<forma.getListCampoTemporal().size(); ca++)
		 {
			campoParam = forma.getListCampoTemporal().get(ca);				
			 
			if(!campoParam.getFormulaCompleta().equals(""))
			{
				formulaArray = campoParam.getFormulaCompleta().split(ConstantesBD.separadorSplit);
			 
				for(int i=0; i<formulaArray.length; i++)
				{
					if(Plantillas.tipoCaracterFormula(formulaArray[i]).equals("campos"))
					{						
						forma.getListCampoTemporalCodigoPk(formulaArray[i].replace("__","")).setUsadoFormula(campoParam.getCodigoPK(),campoParam.getCodigo()+" - "+campoParam.getNombre(),true);							
					}
				}
			}
	 	}
				 
		 //Actualiza el estado dependiendo si es la modificacion de un campo o la creacion de uno nuevo
		 if(esLLamadoPopUp && !forma.getIndexCampo().equals(""))
		 { 
			if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getCodigoPK().equals(""))
				forma.setEstado("cargarModificarCampoParam");			 
			else
				forma.setEstado("crearCampo");
		 }
	 }
	 
	 //*********************************************************************************************
	 
	 /**
	  * Carga la información de los Campos de una seccion 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param Connection con
	  * */
	 private void metodoCargarModificarCampoParam(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		 ArrayList<DtoCampoParametrizable> tmp = new ArrayList<DtoCampoParametrizable>(); 
		 boolean esPermitirAsociados = false;
		 forma.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());
		 forma.setArrayUtilitario1(Utilidades.consultarTiposCampo(con));
		 forma.setArrayUtilitario2(Utilidades.consultarUnidadesCampoParam(con));
		 		 
		 //Indicador Restricción por Valores de Campo de la Sección Contenedora
		 if(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
			 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento()))
			 	.getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi))
		 	esPermitirAsociados = false;
		 else
			esPermitirAsociados = true;
		 
		 //Carga la Informacion de la seccion seleccionado
		 if(forma.getIndexNivel().equals("2"))
		 {
			 tmp = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
			 	.getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos();
			 
			 forma.setPlantillaMap("maxNumColumnas",forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
					 	.getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getColumnasSeccion());
		 }
		 else if(forma.getIndexNivel().equals("3"))
		 {
			 tmp = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
				 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2()))
				 	.getCampos();
			 
			 forma.setPlantillaMap("maxNumColumnas",forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija()))
					 .getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getColumnasSeccion());
		 }
		 
		 for(int i = 0; i<tmp.size(); i++)
		 {
			DtoCampoParametrizable campo = new DtoCampoParametrizable();  
			 
			try 
			{
				tmp.get(i).setPermitirAsociados(esPermitirAsociados);
				PropertyUtils.copyProperties(campo,tmp.get(i));	
				
				forma.setManejaIMgtmp(campo.getManejaImagen());
				forma.setCodigoTipotmp(campo.getCodigoTipo());
				forma.setImagenAsociartmp(campo.getImagenAsociar());
				
				forma.getListCampoTemporal().add(campo);	
				
			}catch(Exception e){
				logger.warn(e);
			}
		 }	 
		 
		 //Restringe los campos que son usados en la formula
		 metodoRestringirCamposParam(con, forma,false);
	 }
	 
	 //*********************************************************************************************	 
	 
	 /**
	  * Crea una nueva opción
	  * @param ParametrizacionFormulariosRespuForm forma 
	  * */
	 private void metodoCrearNuevaOpcion(ParametrizacionFormulariosRespuForm forma)
	 {
		 
		 DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
		 opcion.setCodigoPk("");
		 opcion.setOpcion("");
		 opcion.setValor("");
		 
		 //almancena el dto en el listado 
		 forma.getListCampoTemporal().get(Integer.parseInt(forma.getIndexCampo())).getOpciones().add(opcion);
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Elimina una opcion de un campo
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoEliminarOpcion(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		ActionErrors errores  = new ActionErrors();		
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		UtilidadBD.iniciarTransaccion(con);
				
		//Verifica que exista la opción del campo en base de datos
		if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().
				get(Integer.parseInt(forma.getIndexOpcionCampo())).getCodigoPk().equals(""))
		{
			if(!Plantillas.eliminarOpcionesCamposSec(
					con,
					forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(Integer.parseInt(forma.getIndexOpcionCampo())).getCodigoPk()))
			{
				UtilidadBD.abortarTransaccion(con);
				return errores;
			}
		}			
		
		//Elimina el registro del array de Opciones
		forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().remove(Integer.parseInt(forma.getIndexOpcionCampo()));
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		UtilidadBD.finalizarTransaccion(con);		 
		return errores;		
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Realiza validaciones para guardar las opciones de los campos
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private ActionErrors validacionesOpcionCampo(ParametrizacionFormulariosRespuForm forma, DtoCampoParametrizable campo, int pos)
	 {
		 
		 ActionErrors errores = new ActionErrors();
		 ArrayList<DtoOpcionCampoParam> array = new ArrayList<DtoOpcionCampoParam>();
		 array = campo.getOpciones();
		 
		 if(array.size()<=0){
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existen Opciones para el Campo Nro. "+(pos+1)+"."));
			 
			 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion)
			 {
				 if(campo.getManejaImagen().equals("")){
					 errores.add("descripcion",new ActionMessage("errors.required","El Campo Maneja Imagen Nro. "+(pos+1)+" "));
			 	 }else{
			 		if(campo.getImagenAsociar().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Campo Maneja Imagen Nro. "+(pos+1)+" Requiere que se le Asocie una Imagen Base. "));
				 }
			 }
		 }
		 
		 //Se reccore las opciones
		 for(int i = 0 ; i < array.size(); i++)
		 {
			//Verifica que el valor y la opcion no esten vacios
			 if(array.get(i).getValor().equals("") || array.get(i).getOpcion().equals(""))
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Valor y Nombre de la Opción Nro. "+(i+1)+" del Campo Nro. "+(pos+1)+" son Requeridos "));
			 else
			 {
				 //se valida que no se encuentre repetidos
				 for(int j = 0; j < array.size() && j!=i; j++)
				 {
					 if(array.get(j).getOpcion().equals(array.get(i).getOpcion()))
					 {
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Valor de la Opción Nro. "+(i+1)+" se encuentra Repetido con la Opción  Nro. "+(j+1)+" del Campo Nro. "+(pos+1)+"."));
						 return errores;
					 }
					 
					 //si seleccion convencion se valida que no este repetida.
					 if(array.get(i).getConvencionOdon().getCodigo()!=null && !array.get(i).getConvencionOdon().getCodigo().equals(""))
					 {
						 if(array.get(j).getConvencionOdon().getCodigo().equals(array.get(i).getConvencionOdon().getCodigo()))
						 {
							 errores.add("descripcion",new ActionMessage("errors.notEspecific","La convención de la Opción Nro. "+(i+1)+" se encuentra Repetido con la convención  de la opción Nro. "+(j+1)+" del Campo Nro. "+(pos+1)+"."));
							 return errores;
						 }
					 }
				 }
			 }
			 
			 // validaccion convenciones odontologiaca
			 // Anexo 841
			 if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion)
			 {
				 if(campo.getManejaImagen().equals(ConstantesBD.acronimoSi))
				 {
					 if(array.get(i).getConvencionOdon().getCodigo()==null || array.get(i).getConvencionOdon().getCodigo().equals(""))
					 {
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existen Convención Odontologica para el Campo Nro. "+(pos+1)+"."));
					 }
					 if(campo.getImagenAsociar().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Campo Maneja Imagen Nro. "+(pos+1)+" Requiere que se le Asocie una Imagen Base. "));
					 
				 }else{
					 if(campo.getManejaImagen().equals(""))
						 errores.add("descripcion",new ActionMessage("errors.required","El Campo Maneja Imagen Nro. "+(pos+1)+" "));
				 }
			 }
		 } 
		 
		 return errores;
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Cambia el indicador de Mostrar modificación del campo  
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoEliminarCampoParam(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		 ActionErrors errores  = new ActionErrors();
		 String codigoPkCampoParam = "";
		 DtoCampoParametrizable campoEliminar = new DtoCampoParametrizable();
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		 UtilidadBD.iniciarTransaccion(con);		 
		 
		 if(forma.getIndexNivel().equals("2"))
		 {
			 codigoPkCampoParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos().get(Integer.parseInt(forma.getIndexCampo())).getCodigoPK();
			 
			 campoEliminar = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos().get(Integer.parseInt(forma.getIndexCampo()));
			 
			 //Evalua si el campo esta siendo utilizado por la formula
			 if(campoEliminar.getFormulaCompleta().equals(""))
				 campoEliminar = Plantillas.validarUsoCampoPorFormula(
						 campoEliminar,
						 forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
						 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos());
		 }
		 else if(forma.getIndexNivel().equals("3"))
		 {
			 codigoPkCampoParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).
			 		getCampos().get(Integer.parseInt(forma.getIndexCampo())).getCodigoPK();
			 
			 campoEliminar = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).
		 		getCampos().get(Integer.parseInt(forma.getIndexCampo()));
			 
			 //Evalua si el campo esta siendo utilizado por la formula
			 if(campoEliminar.getFormulaCompleta().equals(""))
				 campoEliminar = Plantillas.validarUsoCampoPorFormula(
						 campoEliminar,
						 forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
						 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).
					 		getCampos());
		 }
		 	
		 
		 if(campoEliminar.isUsadoFormula())
		 {
			UtilidadBD.abortarTransaccion(con);
			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Campo [ "+campoEliminar.getCodigo()+" - "+campoEliminar.getNombre()+" ] esta siendo utilizado por el Campo Tipo Formula [ "+campoEliminar.getInfoFormulaMeUsa().getNombre()+" ]. No se Permite la Eliminación del Campo."));				
			return errores;
		 }
		 
		 if(!Plantillas.actualizarMostrarModCamposParametrizables(
				 con,
				 codigoPkCampoParam,
				 false, 
				 usuario))
		 {
			UtilidadBD.abortarTransaccion(con);
			return errores;
		 }	
		 
		 if(forma.getIndexNivel().equals("2"))
		 {
			 forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos().remove(Integer.parseInt(forma.getIndexCampo()));
		 }
		 else if(forma.getIndexNivel().equals("3"))
		 {
			 forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
			 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).
			 		getCampos().remove(Integer.parseInt(forma.getIndexCampo()));
		 }
		 
		 
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		UtilidadBD.finalizarTransaccion(con);		 
		return errores;
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Metodo carga las secciones asociadas a los campos
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private void metodoSeccionAsociada(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {				 
		 //Almacena la información de todos las secciones disponibles para la opción		 
		 forma.setListSeccionTemporal(
				 forma.getPlantillaDto().obtenerListadoSeccionValor(
						 forma.getListCampoTemporal().get(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
								 Utilidades.convertirAEntero(forma.getIndexOpcionCampo())).getSecciones()));		 
	 }
	 
	 // **********************************************************************************************
	 /**
	  * Metodo Adicionar una nueva secciones asociadas
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private void metodoAddOpcionesSeccion(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {		 		 
		 //Agrega la Sección seleccionada
		 forma.getListCampoTemporal().get(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
				 Utilidades.convertirAEntero(forma.getIndexOpcionCampo())).getSecciones().add(forma.getListSeccionTemporalPos(Utilidades.convertirAEntero(forma.getIndexSeccionesAsocidas())));
		 
		 //Actualiza el listado
		 metodoSeccionAsociada(con, forma, usuario);
	 }	 
	 // **********************************************************************************************
	 
	 /**
	  * Metodo Eliminar secciones asociadas
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario 
	  * */
	 private void metodoEliminarOpcionesSeccion(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		ActionErrors errores  = new ActionErrors();		
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		UtilidadBD.iniciarTransaccion(con);
			
		//Verifica que exista la Seccion dentro de la opción del campo en base de datos
		if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().
				get(Integer.parseInt(forma.getIndexOpcionCampo())).getSecciones().
					get(Utilidades.convertirAEntero(forma.getIndexSeccionesAsocidas())).getCodigoPkDetSeccion().equals(""))
		{
			if(!Plantillas.actualizarSeccionesAsociadasOpciones(
					con,
					forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().
						get(Integer.parseInt(forma.getIndexOpcionCampo())).getSecciones().
						get(Utilidades.convertirAEntero(forma.getIndexSeccionesAsocidas())).getCodigoPkDetSeccion(),
					false,
					usuario))
			{
				UtilidadBD.abortarTransaccion(con);
				return;
			}
		}			
		
		//Elimina el registro del array de Opciones
		forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(Integer.parseInt(forma.getIndexOpcionCampo())).getSecciones().remove(Utilidades.convertirAEntero(forma.getIndexSeccionesAsocidas()));		
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		UtilidadBD.finalizarTransaccion(con);			
	 }	 
	 
	 // **********************************************************************************************
	 
	 /**
	  * Metodo adiciones opciones valores
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario 
	  * */
	 private void  metodoAddOpcionesValores(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		 //Agrega un Nuevo valor
		 if(!forma.getIndexValoresAsocidos().equals(""))
		 {
			 DtoValorOpcionCampoParam dto = new DtoValorOpcionCampoParam();
			 dto.setValor(forma.getIndexValoresAsocidos());
			 
			 forma.getListCampoTemporal().get(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
					 Utilidades.convertirAEntero(forma.getIndexOpcionCampo())).getValoresOpcion().add(dto);
		 }		
	 }
	 
	 // **********************************************************************************************
	 
	 /**
	  * Metodo eliminar opciones valores
	  * @param Connection con
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private void metodoEliminarOpcionesValores(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {		
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		 UtilidadBD.iniciarTransaccion(con);
			
		 //Verifica que exista la Sección dentro de la opción del campo en base de datos
		 if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().
				 get(Integer.parseInt(forma.getIndexOpcionCampo())).getValoresOpcion().
						get(Utilidades.convertirAEntero(forma.getIndexValoresAsocidos())).getCodigoPk().equals(""))
		 {
			 if(!Plantillas.actualizarValoresAsociadosOpciones(
					 con,
					 forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().
					 	get(Integer.parseInt(forma.getIndexOpcionCampo())).getValoresOpcion().
					 		get(Utilidades.convertirAEntero(forma.getIndexValoresAsocidos())).getCodigoPk(),
					 false, 
					 usuario))
			 {
				 UtilidadBD.abortarTransaccion(con);
				 return;
			 }
		 }
		
		 //Elimina el registro del array de Opciones
		 forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getOpciones().get(Integer.parseInt(forma.getIndexOpcionCampo())).getValoresOpcion().remove(Utilidades.convertirAEntero(forma.getIndexValoresAsocidos()));
		 forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);				
		 UtilidadBD.finalizarTransaccion(con);
	 }
	 
	 // **********************************************************************************************
	 
	 /**
	  * Añade un nuevo elemento de escala
	  * @parama Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoNuevaEscala(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		 DtoEscala escala = new DtoEscala();		 
		 escala.setOrden(forma.getListEscalaTemporal().size()+1);		 
		 forma.getListEscalaTemporal().add(escala);		 
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Añade un nuevo elemento Componente
	  * @parama Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private void metodoNuevoComponente(Connection con,ParametrizacionFormulariosRespuForm forma)
	 {
		 DtoComponente componente  = new DtoComponente();		 
		 componente.setOrden(forma.getListComponenteTemporal().size()+1);		 
		 forma.getListComponenteTemporal().add(componente);		 
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Validaciones antes de guardar las escalas
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private ActionErrors validacionesGuardarEscala(ParametrizacionFormulariosRespuForm forma)
	 {
		 ActionErrors errores = new ActionErrors();
		 
		 for(int i = 0 ; i < forma.getListEscalaTemporal().size(); i++)
		 {			 
			 //Valida que no se encuentre vacia
			 if(forma.getListEscalaTemporal().get(i).getCodigoPK().equals("") || 
					 forma.getListEscalaTemporal().get(i).getCodigoPK().equals(ConstantesBD.codigoNuncaValido+""))
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","No se ha Seleccionado Escala para el Registro Nro. "+(i+1)+"."));
			 
			 
			 for(int j = 0 ; j <forma.getListEscalaTemporal().size() && j!=i; j++)
			 {
				 if(forma.getListEscalaTemporal().get(j).getCodigoPK().equals(forma.getListEscalaTemporal().get(i).getCodigoPK()))
				 {
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","La Escala del Registro Nro. "+(j+1)+" se Encuentra Repetida en el Registro Nro. "+(i+1)+" ."));
					 return errores;
				 }
			 }
		 }
		 
		 return errores;
	 }
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Validaciones antes de guardar Componentes
	  * @param ParametrizacionFormulariosRespuForm forma
	  * */
	 private ActionErrors validacionesGuardarComponente(ParametrizacionFormulariosRespuForm forma)
	 {
		 ActionErrors errores = new ActionErrors();
		 
		 for(int i = 0 ; i < forma.getListComponenteTemporal().size(); i++)
		 {			 
			 //Valida que no se encuentre vacia
			 if(forma.getListComponenteTemporal().get(i).getCodigoPK().equals("") || 
					 forma.getListComponenteTemporal().get(i).getCodigoPK().equals(ConstantesBD.codigoNuncaValido+""))
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","No se ha Seleccionado Componente para el Registro Nro. "+(i+1)+"."));
			 
			 
			 for(int j = 0 ; j <forma.getListComponenteTemporal().size() && j!=i; j++)
			 {
				 if(forma.getListComponenteTemporal().get(j).getCodigoPK().equals(forma.getListComponenteTemporal().get(i).getCodigoPK()))
				 {
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","El Componente del Registro Nro. "+(j+1)+" se Encuentra Repetido en el Registro Nro. "+(i+1)+" ."));
					 return errores;
				 }
			 }
		 }
		 
		 return errores;
	 }
	 
	 //	**********************************************************************************************
	 
	 
	 /**
	  * Guarda la información de una escala
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoGuardarEscala(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		ActionErrors errores  = new ActionErrors();
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		UtilidadBD.iniciarTransaccion(con);		
		
		errores = Plantillas.insertarEscalaParametrizableSeccion(
				con, 
				forma.getPlantillaDto(),
				forma.getListEscalaTemporal(),
				forma.getIndexSeccionFija(),
				forma.getIndexNivel(),
				true,
				usuario);
				
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			return errores;
		}		
		 
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
		forma.setListEscalaTemporal(new ArrayList<DtoEscala>());
		//Recarga el Dto de Plantilla
		metodoDetallePlantilla(con, forma, usuario);
		forma.setArrayUtilitario1(Utilidades.consultarEscalasParam(con,Plantillas.obtenerCodigosInsertadosEscalas(forma.getPlantillaDto()),ConstantesBD.acronimoSi));	
		UtilidadBD.finalizarTransaccion(con);		 
		return errores;
		 
	 }	 
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Guarda la información del Componenten
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoGuardarComponente(Connection con,ParametrizacionFormulariosRespuForm forma,UsuarioBasico usuario)
	 {
		ActionErrors errores  = new ActionErrors();
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
		UtilidadBD.iniciarTransaccion(con);		
		
		errores = Plantillas.insertarComponenteParametrizableSeccion(
				con, 
				forma.getPlantillaDto(),
				forma.getListComponenteTemporal(),
				forma.getIndexSeccionFija(),
				forma.getIndexNivel(),
				true,
				usuario);
				
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			return errores;
		}		
		 
		forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);
		forma.setListComponenteTemporal(new ArrayList<DtoComponente>());
		//Recarga el Dto de Plantilla
		metodoDetallePlantilla(con, forma, usuario);
		forma.setArrayUtilitario1(Utilidades.consultarComponentesParam(
				con,
				ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos+"",
				Plantillas.obtenerCodigosInsertadosComponentes(forma.getPlantillaDto())));
		
		UtilidadBD.finalizarTransaccion(con);		 
		return errores;		 
	 }
	 //	**********************************************************************************************
	 
	 
	 
	 /**
	  * Elimina una Escala
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param boolean esNueva
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoEliminarEscala(
			 Connection con,
			 ParametrizacionFormulariosRespuForm forma,
			 boolean esNueva,
			 UsuarioBasico usuario)
	 {
		 ActionErrors errores = new ActionErrors();
		 String codigoPkSeccionParam = "";
		 
		 if(!esNueva)
		 {
			 DtoEscala escala = new DtoEscala();
			 
			 //Valida el nivel 
			 if(forma.getIndexNivel().equals("fija"))
			 {
				 escala = (DtoEscala)forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento()));
				 codigoPkSeccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getCodigoSeccionParam();
			 }
			 else if(forma.getIndexNivel().equals("1"))
			 {
				escala = (DtoEscala)forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento()));
				//Para este nivel no se crea una seccion parametrizable para la escala
			 	codigoPkSeccionParam = "";				 
			 }							 			 
			 
			 
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
			UtilidadBD.iniciarTransaccion(con);			
			
			if(!Plantillas.actualizarEscalaParametrizable(
					con, 
					escala.getConsecutivoParametrizacion()+"",					 
					escala.getOrden()+"",					
					ConstantesBD.acronimoSi,
					ConstantesBD.acronimoNo))
			{			
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La Escala No fue Eliminada."));					
				return errores;
			}		
			else if(!codigoPkSeccionParam.equals("") && !Plantillas.actualizarMostrarModOrdenSeccionParam(
					con,
					false, 
					forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getOrden()+"", 
					codigoPkSeccionParam+"", 
					usuario))
			{
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La Seccion de la Escala No fue Eliminada."));
				return errores;
			}
			 
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);			
			//Recarga el Dto de Plantilla
			metodoDetallePlantilla(con, forma, usuario);			
			UtilidadBD.finalizarTransaccion(con);		 						
		 }
		 else
		 {			 
			 forma.getListEscalaTemporal().remove(Integer.parseInt(forma.getIndexEscala()));			 
		 }		 
		 		 
		 return errores;
	 }
	 
	 
	 //	**********************************************************************************************
	 
	 /**
	  * Elimina un Componente
	  * @param Connection con 
	  * @param ParametrizacionFormulariosRespuForm forma
	  * @param boolean esNueva
	  * @param UsuarioBasico usuario
	  * */
	 private ActionErrors metodoEliminarComponente(
			 Connection con,
			 ParametrizacionFormulariosRespuForm forma,
			 boolean esNueva,
			 UsuarioBasico usuario)
	 {
		 ActionErrors errores = new ActionErrors();
		 String codigoPkSeccionParam = "";
		 
		 if(!esNueva)
		 {
			 DtoComponente componente = new DtoComponente();
			 
			 //Valida el nivel 
			 if(forma.getIndexNivel().equals("fija"))
			 {
				 componente = (DtoComponente)forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento()));
				 codigoPkSeccionParam = forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getCodigoSeccionParam();
			 }
			 else if(forma.getIndexNivel().equals("1"))
			 {
				componente = (DtoComponente)forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(Integer.parseInt(forma.getIndexElemento()));
			 	codigoPkSeccionParam = "";				 
			 }							 			 			 
			 
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoNo);			
			UtilidadBD.iniciarTransaccion(con);			
			
			if(!Plantillas.actualizarComponenteParametrizable(
					con, 
					componente.getConsecutivoParametrizacion(),
					componente.getCodigoPK()+"",
					componente.getOrden()+"", 
					ConstantesBD.acronimoSi, 
					ConstantesBD.acronimoNo))
			{			
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Componente No fue Eliminado."));					
				return errores;
			}		
			else if(!codigoPkSeccionParam.equals("") && !Plantillas.actualizarMostrarModOrdenSeccionParam(
					con,
					false, 
					forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).getOrden()+"", 
					codigoPkSeccionParam+"", 
					usuario))
			{
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La Seccion del Componente No fue Eliminado."));
				return errores;
			}
			 
			forma.setPlantillaMap("esOperacionExitosa",ConstantesBD.acronimoSi);			
			//Recarga el Dto de Plantilla
			metodoDetallePlantilla(con, forma, usuario);			
			UtilidadBD.finalizarTransaccion(con);		 						
		 }
		 else
		 {			 
			 forma.getListComponenteTemporal().remove(Integer.parseInt(forma.getIndexComponente()));			 
		 }		 
		 		 
		 return errores;
	 }
	 
	 
	 //	**********************************************************************************************
	 
	 
	
	/**
	 * Valida los permisos para las operaciones dentro de la funcionalidad
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	private void validacionesPermisos(UsuarioBasico usuario,ParametrizacionFormulariosRespuForm forma,boolean soloConsultar)
	{
		//Valida permisos para la consulta
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(),766))
			forma.setSePuedeConsultar(ConstantesBD.acronimoSi);
		else
			forma.setSePuedeConsultar(ConstantesBD.acronimoNo);
		
		//Valida permisos para el ingreso y modificación 
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(),767))
			forma.setSePuedeModificar(ConstantesBD.acronimoSi);
		else
			forma.setSePuedeModificar(ConstantesBD.acronimoNo);
		
		if(soloConsultar)
		{
			forma.setSePuedeConsultar(ConstantesBD.acronimoSi);
			forma.setSePuedeModificar(ConstantesBD.acronimoNo);
		}			
	}
	
	//**********************************************************************************************
	
	/**
	 * Metodo que agrega un nuevo servicio
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param HttpServletRequest request
	 * */
	private void metodoAgregarServicios(ParametrizacionFormulariosRespuForm forma, HttpServletRequest request)
	{
		DtoPlantillaServDiag dto = new DtoPlantillaServDiag();
		dto.setCodigoServicio(Utilidades.convertirAEntero(forma.getPlantillaMap("codigoAxiomaNueva").toString()));
		dto.setDescripcionServicio(forma.getPlantillaMap("descripcionNueva").toString());
		dto.setEstaBD(ConstantesBD.acronimoNo);
		dto.setEsEliminado(ConstantesBD.acronimoNo);
		
		//Valida que la combinacion de parametrizacion no se repita
		if(ParametrizacionFormulariosRespu.accionValidarNuevoAtributo(forma.getPlantillaSerDiagArray(),dto))	
			forma.getPlantillaSerDiagArray().add(dto);
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Servicio ["+dto.getDescripcionServicio().toLowerCase()+"] ya Existe para la Plantilla."));
			saveErrors(request, errores);
		}
		
		forma.setEstado("nuevoFormulario");
		
		forma.setListadoPlantillaMap("codigosServiciosInsertados",
				ParametrizacionFormulariosRespu.quitarAnadeServicioRestriccion(
						forma.getListadoPlantillaMap(),
						forma.getPlantillaMap("codigoAxiomaNueva").toString(),
						"agregar",
						0));		
	}
	
	//**********************************************************************************************	
	
	/**
	 * Elimina los servicios / Diagnóstico
	 * @param  ParametrizacionFormulariosRespuForm forma 
	 * */
	private void eliminarServicioDiag(ParametrizacionFormulariosRespuForm forma)	
	{			
		//Elimina los registros que posean el codigo del servicio dado
		if(!forma.getPlantillaMap("indexServDiag").toString().equals(""))
		{
			for(DtoPlantillaServDiag dto:forma.getPlantillaSerDiagArray())
			{	
				if(forma.getPlantillaMap("indexServDiag").toString().equals(dto.getCodigoServicio()+""))					
					dto.setEsEliminado(ConstantesBD.acronimoSi);				
			}
		}
				
		forma.setListadoPlantillaMap("codigosServiciosInsertados",
				ParametrizacionFormulariosRespu.quitarAnadeServicioRestriccion(
						forma.getListadoPlantillaMap(),
						forma.getPlantillaMap("indexServDiag").toString(),
						"quitar",
						0));
		
		//Actualiza el estado que muestra el formulario de encabezado el cual sirve tanto para 
		//agregar una nueva plantilla como para modificarla
		forma.setEstado("nuevoFormulario");				
	}
	
	//**********************************************************************************************
	
	/**
	 * Carga la informacion parametrizada del servicio/diagnostico
	 * @param ParametrizacionFormulariosRespuForm forma
	 * */
	private void metodoModificarDiagnos(ParametrizacionFormulariosRespuForm forma)	
	{		
		int pos = ParametrizacionPlantillas.getPosArrayServicio(forma.getPlantillaMap("indexServDiag").toString(),forma.getPlantillaSerDiagArray());
		forma.setPlantillaMap("servicioDiagParam","");
		
		if(pos >= 0)
			forma.setPlantillaMap("servicioDiagParam",forma.getPlantillaSerDiagArray().get(pos).getCodigoPropietarioServicio()+" - "+forma.getPlantillaSerDiagArray().get(pos).getDescripcionServicio());		
	}
	
	//**********************************************************************************************
	
	/**
	 * Modifica los diagnosticos existentes
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param HttpServletRequest request
	 * */
	private void metedoNuevoDiagnos(ParametrizacionFormulariosRespuForm forma, HttpServletRequest request)
	{
		DtoPlantillaServDiag dto = new DtoPlantillaServDiag();
						
		//Evalua la posicion del array de diagnosticos
		if(forma.getPlantillaMap().containsKey("indexDiagnosticoArrary") && 
				!forma.getPlantillaMap("indexDiagnosticoArrary").toString().equals(""))
		{
			//logger.info("log Modificar Diagnos >> "+forma.getPlantillaMap("indexDiagnosticoArrary")+" >> "+forma.getPlantillaMap("indexServDiag").toString() );
			
			HashMap tmp = new HashMap();
			int pos = 0;
			
			//Captura la información base del diagnostico
			tmp = (HashMap)forma.getDiagnosticosParamArray().get(Utilidades.convertirAEntero(forma.getPlantillaMap("indexDiagnosticoArrary").toString()));			
			pos = ParametrizacionPlantillas.getPosArrayServicio(forma.getPlantillaMap("indexServDiag").toString(),forma.getPlantillaSerDiagArray());
						
			//Valida que la posicion sea valida
			if(pos >= 0)
			{
				dto.setCodigoServicio(forma.getPlantillaSerDiagArray().get(pos).getCodigoServicio());			
				dto.setDescripcionServicio(forma.getPlantillaSerDiagArray().get(pos).getDescripcionServicio());
				dto.setCodigoDiagnostico(Utilidades.convertirAEntero(tmp.get("codigo").toString()));
				dto.setDescripcionDiagnostico(tmp.get("descripcion").toString());
				dto.setEstaBD(ConstantesBD.acronimoNo);
				dto.setEsEliminado(ConstantesBD.acronimoNo);		
							
				//Valida que la combinacion de parametrizacion no se repita
				if(dto.getCodigoDiagnostico() > 0)
				{
					if(ParametrizacionFormulariosRespu.accionValidarNuevoAtributo(forma.getPlantillaSerDiagArray(),dto))	
						forma.getPlantillaSerDiagArray().add(dto);
					else
					{
						ActionErrors errores = new ActionErrors();
						errores.add("descripcion",new ActionMessage("errors.notEspecific","El Diagnostico [ "+dto.getCodigoDiagnostico()+" - "+dto.getDescripcionDiagnostico().toLowerCase()+"] ya Existe para la Plantilla."));
						saveErrors(request, errores);
					}
				}
			}
			else
			{
				logger.info("no se encontro el servicio >> "+forma.getPlantillaMap("indexServDiag").toString());
			}
		}

		forma.setEstado("nuevoFormulario");
	}
	
	//**********************************************************************************************
	
	/**
	 * Valida los datos antes de guardar la información de la platilla     
	 * @param ParametrizacionFormulariosRespuForm forma
	 * */	
	private ActionErrors validacionesGuardarFormulario(ParametrizacionFormulariosRespuForm forma)
	{
		ActionErrors errores = new ActionErrors();
		boolean tmp = false;
		
		//Valida la información de Codigo y Nombre
		if(forma.getPlantillaMap("codigoPlantilla").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","EL codigo del nuevo Registro es "));
				
		if(forma.getPlantillaMap("nombrePlantilla").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","EL nombre del nuevo Registro es "));
				
		//Valida que no se encuentre repetido el codigo de la plantilla
		for(int j = 0; j<Integer.parseInt(forma.getListadoPlantillaMap("numRegistros").toString()) && !tmp; j++)
		{
			if(forma.getPlantillaMap("codigoPlantilla").toString().equals(forma.getListadoPlantillaMap("codigoPlantilla_"+j).toString()) && 
				!forma.getPlantillaMap("codigoPk").toString().equals(forma.getListadoPlantillaMap("codigoPk_"+j).toString()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Codigo "+forma.getPlantillaMap("codigoPlantilla")+" se encuentra repetido. Ver registro Nro. "+(j+1)));
				tmp = true;
			}				
		}		
		
		//Actualiza el estado que muestra el formulario de encabezado el cual sirve tanto para 
		//agregar una nueva plantilla como para modificarla
		if(!errores.isEmpty())			
			forma.setEstado("nuevoFormulario");
		
		return errores;
	}	
	//**********************************************************************************************
	
	/**
	 * Operaciones del estado ordenar
	 * @param Connection con
	 * @param ParametrizacionFormulariosRespuForm forma
	 * @param ParametrizacionFormulariosRespu mundo
	 * */
	private void metodoEstadoOrdenarListado(Connection con,ParametrizacionFormulariosRespuForm forma)
	{
		String codigoInsertados = forma.getListadoPlantillaMap("codigosServiciosInsertados").toString();
		forma.setListadoPlantillaMap("INDICES_MAPA",ParametrizacionFormulariosRespu.indicesListadoPlantilla);
		forma.setListadoPlantillaMap(ParametrizacionFormulariosRespu.accionOrdenarMapa(forma.getListadoPlantillaMap(), forma.getPatronOrdenar(),forma.getUltimoPatronOrdenar()));
		forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
		forma.setListadoPlantillaMap("codigosServiciosInsertados",codigoInsertados);
		
		Utilidades.imprimirMapa(forma.getListadoPlantillaMap());
	}
	
	//******************************************************************************************
}