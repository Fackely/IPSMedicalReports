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
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.ParametrizacionPlantillasForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoValorOpcionCampoParam;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ParametrizacionPlantillas;
import com.princetonsa.mundo.historiaClinica.Plantillas;

public class ParametrizacionPlantillasAction extends Action {

	/**
	 * 
	 */
	Logger logger = Logger.getLogger(ParametrizacionPlantillasAction.class);

	/**
	 * 
	 */
	private static String[] indicesEspecialidades = { "codigo_",
			"descripcion_", "tiporegistro_" };

	/**
	 * Mï¿½todo excute del Action
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
		if (form instanceof ParametrizacionPlantillasForm) 
		{
			ParametrizacionPlantillasForm forma=(ParametrizacionPlantillasForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con = UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			ActionErrors errores = new ActionErrors();
			
			ParametrizacionPlantillas mundo=new ParametrizacionPlantillas();
			
			
			Plantillas mundoPlantillas=new Plantillas();
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ParametrizacionPlantillasAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("consultarParametrizacion"))
			{
				//forma.reset();
				//Se resetea las variables para odontologia
				forma.resetOdon();
				
				//Por Anexo 843 se agrega la siguiente validaciÃ³n
				forma.setPlantillaDto(new DtoPlantilla());
				//forma.resetElementosOdontologia();
				
				if (!forma.getPlantillaBase().trim().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+"")
						&&!forma.getPlantillaBase().trim().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+""))
					this.metodoCargarPlantilla(con, forma, usuario);
					
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("adicionarEspecialidad"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminarEspecialidad"))
			{
				//eliminar especialidad
				Utilidades.eliminarRegistroMapaGenerico(forma.getEspecialidades(),forma.getEspecialidadesEliminadas(),forma.getIndiceEspecialidadEliminar(),indicesEspecialidades,"numRegistros","tiporegistro_","BD",false);
				forma.setIndiceEspecialidadEliminar(ConstantesBD.codigoNuncaValido);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("adicionSeccion"))
			{
				forma.resetMensaje();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("seccion");
			}
			else if(estado.equals("adicionEscala"))
			{
				forma.resetMensaje();
				//forma.resetElementos();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("escalas");
			}
			else if(estado.equals("adicionComponente"))
			{
				forma.resetMensaje();
				//forma.resetElementos();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("componentes");
			}
			else if(estado.equals("adicionCampo"))
			{
				forma.resetMensaje();
				forma.getListCampoTemporal().clear();
				forma.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());
				UtilidadBD.closeConnection(con);
				return mapping.findForward("campos");
			}
			else if(estado.equals("nuevaEscala"))
			{
				//forma.reset();
				forma.resetMensaje();
				this.nuevaEscala(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("escalas");
			}
			else if(estado.equals("nuevoComponente"))
			{
				//forma.reset();
				forma.resetMensaje();
				this.nuevoComponente(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("componentes");
			}
			else if(estado.equals("nuevoCampo"))
			{
				//forma.reset();
				forma.resetMensaje();
				metodoCrearNuevaCampo(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("campos");
			}
			else if(estado.equals("ingresoOpciones"))
			{
				//forma.reset();
				forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setManejaImagen(forma.getManejaIMgtmp());
				forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setCodigoTipo(forma.getCodigoTipotmp());
				forma.getListCampoTemporal().get(Utilidades.convertirAEntero(forma.getIndexCampo())).setImagenAsociar(forma.getImagenAsociartmp());
				UtilidadBD.closeConnection(con);
				return mapping.findForward("opciones");
			}
			else if(estado.equals("nuevaOpcion"))
			{
				//forma.reset();
				this.nuevaOpcion(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("opciones");
			}			
			else if(estado.equals("guardarOpcion"))
			{
				//forma.reset();
				//logger.info("\n\n\n\n ********** INDEX >> "+forma.getIndexCampo());
				 errores = validacionesOpcionCampo(
						 forma,
						 forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())),
						 Integer.parseInt(forma.getIndexCampo()));
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("opciones");
				 }
				UtilidadBD.closeConnection(con);
				return mapping.findForward("opciones");
			}
			else if(estado.equals("parametrizar"))
			{
				
				errores = metodoModificarPlantillasSecFijas(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("principal");
				 }
				 this.metodoCargarPlantilla(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");
				
			}
			else if(estado.equals("parametrizarEspecialidad"))
			{
				
				//Se hacen las validaciones respectivas para la evolucion y valoracion odontologica
				errores=this.validarPlantillaConIgualNombre(forma, errores, request);
				
				if (errores.isEmpty()){
					errores = metodoModificarPlantillasSecFijasEspecialidad(con,forma,usuario);
					
				}
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("principal");
				 }
				 //forma.setMapaConsultaEspecialidades(mundo.consultarEspecialidades(con));
				 //forma.setEspecialidades("numRegistros", "0");
				 this.metodoCargarPlantilla(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");
				
			}
			else if(estado.equals("volverPrincipal"))
			{
				forma.resetMensaje();
				forma.resetElementos();
				this.metodoCargarPlantilla(con, forma, usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("volverPrimera"))
			{
				forma.resetElementos();
				this.metodoCargarPlantilla(con, forma, usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("cargarPlantilla"))
			{
				this.metodoCargarPlantillaEspecialidad(con, forma, usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("especialidad");
			}
			else if(estado.equals("crearNuevaSeccion"))
			{
				metodoCrearNuevaSeccion(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("seccion");
			}
			else if(estado.equals("guardarEscalas"))
			{
				
				errores=this.validarPlantillaConIgualNombre(forma, errores, request);
				
				if (errores.isEmpty())
					errores = validacionesGuardarEscalaParam(con, forma);
				 
				 if(!errores.isEmpty())
				 {
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				 }
				
				errores= metodoGuardarEscalas(con,forma,usuario);
				
				if(!errores.isEmpty())
				{
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("escalas");
				
				
			}
			else if(estado.equals("guardarComponentes"))
			{
				
				errores=this.validarPlantillaConIgualNombre(forma, errores, request);
				
				if (errores.isEmpty())
					errores = validacionesGuardarComponentesParam(con, forma);
				 
				 if(!errores.isEmpty())
				 {
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("componentes");
				 }
				
				errores= metodoGuardarComponentes(con,forma,usuario);
				
				if(!errores.isEmpty())
				{
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("componentes");
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("componentes");
				
				
			}
			else if(estado.equals("guardarSeccion"))
			{
				errores=this.validarPlantillaConIgualNombre(forma, errores, request);
				
				if (errores.isEmpty())
					errores = validacionesGuardarSeccionParam(con,forma);
				 
				 if(!errores.isEmpty())
				 {
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				 }
				 
				errores= metodoGuardarSecciones(con,forma,usuario);
				
				if(!errores.isEmpty())
				{
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("seccion");
				
				
			}
			else if(estado.equals("modificarSeccion"))
			{
				
				errores = validacionesGuardarSeccionParam(con,forma);
				 
				if(!errores.isEmpty())
				{
					if(estado.equals("modificarSeccion"))
						forma.setEstado("cargarModificarSeccionParam");
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				
				errores= metodoModificarSecciones(con,forma,usuario);
				
				if(!errores.isEmpty())
				{
					if(estado.equals("modificarSeccion"))
						forma.setEstado("cargarModificarSeccionParam");
					saveErrors(request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				
				if(estado.equals("modificarSeccion"))
					forma.setEstado("cargarModificarSeccionParam");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("seccion");
				
				
			}
			else if(estado.equals("guardarCampo") || estado.equals("modificarCampo"))
			{
				
				 //Actualiza el ordenamiento de los campos 
				 forma.setListCampoTemporal(Plantillas.ordenarArrayCamposPorOrden(forma.getListCampoTemporal()));
				 
				errores = validacionesGuardarCampoParam(con, forma);
				
				 if(!errores.isEmpty())
				 {
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
					 if(estado.equals("guardarCampo"))
						 forma.setEstado("adicionCampo");
					 else
						 forma.setEstado("cargarModificarCampoParam");
					 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("campos");
				 }
				 
				 errores = metodoGuardarCampoParam(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {				 
					 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
					 if(estado.equals("guardarCampo"))
						 forma.setEstado("adicionCampo");
					 else
						 forma.setEstado("cargarModificarCampoParam");
					 
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("campos");
				 }
				 
				 //Se modifican los estado debido a las validaciones presentes en la jsp de Campo (Nueva o Modificar)
				 if(estado.equals("guardarCampo"))
					 forma.setEstado("adicionCampo");
				 else
					 forma.setEstado("cargarModificarCampoParam");
				 				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campos");
				
			}
			else if(estado.equals("cargarModificarCampoParam"))
			{
				 metodoCargarModificarCampoParam(con,forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campos");
			}
			else if(estado.equals("eliminarCampoParam"))
			{
				metodoEliminarCampoParam(con,forma,usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleSeccion");				
			}
			else if(estado.equals("detalleSeccionFija"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleSeccionFija");
			}
			else if(estado.equals("modificarSeccionFija"))
			{
				errores = metodoModificarSecFijas(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("detalleSeccionFija");
				 }
				 this.metodoCargarPlantilla(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("detalleSeccionFija");
				
			}
			else if(estado.equals("detalleSeccionNivel1"))
			{				 
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleSeccion");
			}
			else if(estado.equals("cargarModificarSeccionParam"))
			{
				 metodoCargarModificarSeccionParam(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			}
			else if(estado.equals("eliminarOpcionCampo"))
			{				 
				 metodoEliminarOpcion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("opciones");
			}
			else if(estado.equals("eliminarCampo"))
			{				 
				 forma.getListCampoTemporal().remove(Integer.parseInt(forma.getIndexCampo()));
				 forma.setEstado("adicionCampo");
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("campos");
			}
			else if(estado.equals("eliminarSeccion"))
			{				 
				 forma.getListSeccionTemporal().remove(forma.getIndexSeccionEliminar());
				 forma.setEstado("adicionSeccion");
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccion");
			}
			else if(estado.equals("eliminarEscalaNueva"))
			{				 
				 forma.getEscalasList().remove(forma.getIndexEscalaEliminar());
				 forma.setEstado("nuevaEscala");
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("escalas");
			}
			else if(estado.equals("eliminarComponenteNuevo"))
			{				 
				 forma.getComponentesList().remove(forma.getIndexComponenteEliminar());
				 forma.setEstado("nuevoComponente");
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("componentes");
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
			else if(estado.equals("eliminarSeccionParam"))
			{
				 errores = metodoEliminarSeccionParam(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 if(forma.getIndexNivel().equals("fija"))
						 return mapping.findForward("principal");
					 else if(forma.getIndexNivel().equals("1"))
						 return mapping.findForward("detalleSeccionFija");
					 else
						 return mapping.findForward("detalleSeccion");
				 }
				 
				 UtilidadBD.closeConnection(con);
				 if(forma.getIndexNivel().equals("fija"))
					 return mapping.findForward("principal");
				 else if(forma.getIndexNivel().equals("1"))
					 return mapping.findForward("detalleSeccionFija");
				 else
					 return mapping.findForward("detalleSeccion");
			}
			else if(estado.equals("modificarEspecialidad"))
			{
				
				errores = metodoModificarPlantillasSecFijasEspecialidad(con,forma,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 return mapping.findForward("especialidad");
				 }
				 this.metodoCargarPlantilla(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("especialidad");
				
			}
			else if(estado.equals("eliminarEscala"))
			{
				 errores = metodoEliminarEscala(con,forma,false,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 if(forma.getIndexNivel().equals("fija"))
						 return mapping.findForward("principal");
					 else
						 return mapping.findForward("detalleSeccionFija");
					 
				 }
				 				 
				 UtilidadBD.closeConnection(con);				 
				 if(forma.getIndexNivel().equals("fija"))
					 return mapping.findForward("principal");
				 else
					 return mapping.findForward("detalleSeccionFija");
				 
			}
			else if(estado.equals("eliminarComponente"))
			{
				 errores = metodoEliminarComponente(con,forma,false,usuario);
				 
				 if(!errores.isEmpty())
				 {
					 saveErrors(request,errores);
					 UtilidadBD.closeConnection(con);
					 if(forma.getIndexNivel().equals("fija"))
						 return mapping.findForward("principal");
					 else
						 return mapping.findForward("detalleSeccionFija");
					 
				 }
				 				 
				 UtilidadBD.closeConnection(con);				 
				 if(forma.getIndexNivel().equals("fija"))
					 return mapping.findForward("principal");
				 else
					 return mapping.findForward("detalleSeccionFija");
				 
			}
			//--------------------------------------------------------------
			 //Estados Campos Tipo Formula 
			 else if(estado.equals("cargarFormulaCampoParam"))
			 {
				 /*if (!(request.getParameter("esOperacionExitosa")+"").equals("") && !(request.getParameter("esOperacionExitosa")+"").equals("null"))
					 forma.setEsOperacionExitosa(request.getParameter("esOperacionExitosa").toString());*/				 
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
				 return mapping.findForward("campos");
			 }
			 else if(estado.equals("ingresoSeccionesAsoc"))
			 {
				 this.metodoSeccionAsociada(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesAsociadas");
			 }
			 else if(estado.equals("addOpcionesSeccion"))
			 {
				 metodoAddOpcionesSeccion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesAsociadas");
			 }
			 else if(estado.equals("eliminarOpcionesSeccion"))
			 {
				 metodoEliminarOpcionesSeccion(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesAsociadas");
			 }
			 else if(estado.equals("validarOpcionesSecciones"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("seccionesAsociadas");
			 }
			 else if(estado.equals("ingresoValoresAsoc"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("valoresAsociados");
			 }
			 else if(estado.equals("adicionarValores"))
			 {
				 metodoAddOpcionesValores(con, forma, usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("valoresAsociados");
			 }
			 else if(estado.equals("validarOpcionesValores"))
			 {
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("valoresAsociados");
			 }
			 else if(estado.equals("eliminarOpcionesValores"))
			 {				 
				 metodoEliminarOpcionesValores(con,forma,usuario);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("valoresAsociados");
			 }
			//********Anexo 843
			 else if(estado.equals("refrescarOdontologia"))
			 {
				 //Se cargan las especialidades. si es apra consultar para modificar se cargan las especialidades que ya tienen plantillas parametrizadas en el sistema, sino se cargan las que existan y sean odontologicas
				 forma.resetElementosOdontologia();
				 if (forma.getProcesoASeguirOdontologia().equals(ConstantesBD.acronimoSi))
					 forma.setEspecialidadesOdontologia(Plantillas.obtenerEspecialidadesConPlantillaParametrizada(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+"",ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia));
				 else
					 forma.setEspecialidadesOdontologia (Utilidades.obtenerEspecialidadesEnArray(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
				 forma.setPlantillaDto(new DtoPlantilla());
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");
			 }
			
			//********Anexo 877
			 else if(estado.equals("refrescarEvoOdon"))
			 {
				//Se cargan las especialidades. si es apra consultar para modificar se cargan las especialidades que ya tienen plantillas parametrizadas en el sistema, sino se cargan las que existan y sean odontologicas
				 forma.resetElementosOdontologia();
				 if (forma.getProcesoASeguirOdontologia().equals(ConstantesBD.acronimoSi))
				 	forma.setEspecialidadesOdontologia(Plantillas.obtenerEspecialidadesConPlantillaParametrizada(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+"",ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica));
				 else
					 forma.setEspecialidadesOdontologia (Utilidades.obtenerEspecialidadesEnArray(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
				 forma.setPlantillaDto(new DtoPlantilla());
				 
				 //forma.setMostrarElementosVista(ConstantesBD.acronimoNo);
				 
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");
			 }
			
			 else if (estado.equals("cargarPlantillaOdontologia"))
			 {
				 //forma.resetElementosOdontologia();
				 //forma.setEspecialidadesOdontologia(Plantillas.obtenerEspecialidadesConPlantillaParametrizada(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
				 forma.getPlantillaDto().clean();
				 cargarPlantillaNuevaOdon(con, forma, usuario);
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			 }
			
			 else if (estado.equals("refrescar"))
			 {
				 forma.getPlantillaDto().clean();
				 return mapping.findForward("principal");
			 }
			
			 else if(estado.equals("consultarListadoPlantillasOdon"))
			 {
				 String tipoAtencion=forma.getTipoAtencion() ;
				 String consecutivoPk=forma.getNombreSelect();
				 String  especialidad= forma.getEspecialidadParametrizable();
				 int funParametrica=Utilidades.convertirAEntero(forma.getPlantillaBase());
				 
				 forma.setListadoPlantillasOdon(Plantillas.consultarListadoPlantillas(con, usuario.getCodigoInstitucion(), funParametrica, consecutivoPk, "", false, "", tipoAtencion, especialidad));
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("listarPlantillasOdon");
			 }
			
			 else if (estado.equals("buscarPlantillasOdonEvo"))
			 {
				
				 forma.getPlantillaDto().clean();
				 forma.setMostrarElementosVista(ConstantesBD.acronimoNo);
				 
				 forma.setPlantillasOdontologia(Plantillas.consultarListadoPlantillas(con, usuario.getCodigoInstitucion(), ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica, "", "", false, "", forma.getTipoAtencion(), forma.getEspecialidadParametrizable()));
				 
				 Utilidades.imprimirMapa(forma.getPlantillasOdontologia());
				//Si existe un solo registro se recarga automaticamnte
				 if (Utilidades.convertirAEntero(forma.getPlantillasOdontologia().get("numRegistros").toString())==1)
				 {
					 forma.setCodigoPkPlantilla(Utilidades.convertirAEntero(forma.getPlantillasOdontologia().get("codigoPk_0")+""));
					 metodoCargarPlantilla(con, forma, usuario);
					 forma.getPlantillasOdontologia().put("codigoPk_0",forma.getPlantillasOdontologia().get("codigoPk_0")+"");
					 forma.setNombreSelect(forma.getPlantillasOdontologia().get("codigoPk_0")+"");
				 }
					 
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("principal");
			 }
			
			 else if (estado.equals("buscarPlantillasOdonVal"))
			 {
				
				 forma.getPlantillaDto().clean();
				 forma.setMostrarElementosVista(ConstantesBD.acronimoNo);
				 
				 forma.setPlantillasOdontologia(Plantillas.consultarListadoPlantillas(con, usuario.getCodigoInstitucion(), ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia, "", "", false, "", forma.getTipoAtencion(), forma.getEspecialidadParametrizable()));
				 
				 Utilidades.imprimirMapa(forma.getPlantillasOdontologia());
				 //Si existe un solo registro se recarga automaticamnte
				 if (Utilidades.convertirAEntero(forma.getPlantillasOdontologia().get("numRegistros").toString())==1)
				 {
					 forma.setCodigoPkPlantilla(Utilidades.convertirAEntero(forma.getPlantillasOdontologia().get("codigoPk_0")+""));
					 metodoCargarPlantilla(con, forma, usuario);
					 forma.getPlantillasOdontologia().put("codigoPk_0",forma.getPlantillasOdontologia().get("codigoPk_0")+"");
					 forma.setNombreSelect(forma.getPlantillasOdontologia().get("codigoPk_0")+"");
				 }
				 
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("principal");
			 }
			
//			 else if (estado.equals("buscarPlantillasXTipoAtencionEvolucion"))
//			 {
//				 forma.setEspecialidadesOdontologia(Utilidades.obtenerEspecialidadesEnArray(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
//				 forma.setPlantillasOdontologia(Plantillas.consultarListadoPlantillas(con, usuario.getCodigoInstitucion(), ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica, "", "", false, "", forma.getTipoAtencion(), forma.getEspecialidadParametrizable()));
//				 UtilidadBD.cerrarConexion(con);
//				 return mapping.findForward("principal");
//			 }
			
			 else if (estado.equals("cargarPlantillaModificar"))
			 {
				 //Se carga el codigo del elemento seleccionado del mapa en el popup para que se carge en la pagina ppal
				 forma.setCodigoPkPlantilla(Utilidades.convertirAEntero(forma.getListadoPlantillasOdon().get("codigoPk_"+forma.getPosPlantilla()).toString()));
				 metodoCargarPlantilla(con, forma, usuario);
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("principal");
			 }
			
			//***Fin Cambios Anexo 843
			
			
			//**Cambios Anexo 860
			 else if (estado.equals("cargarInfoPacOdon"))
			 {
				 logger.info("Tipo Funcionalidad 1>> "+forma.getTipoFuncionalidad());
				 metodoCargarPlantilla(con, forma, usuario);
				
				 return mapping.findForward("principal"); 
			 }
			//**Fin Cambios Anexo 860
			
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de PARAMETRIZACION PLANTILLAS ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}		
		}
		else
		{
			logger.error("El form no es compatible con el form de ParametrizacionPlantillasForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		} catch (Exception e) {
			
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoEliminarOpcionesValores(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		UtilidadBD.iniciarTransaccion(con);

		// Verifica que exista la Secciï¿½n dentro de la opciï¿½n del campo en
		// base de datos
		if (!forma.getListCampoTemporalPos(
				Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
				Integer.parseInt(forma.getIndexOpciones())).getValoresOpcion()
				.get(
						Utilidades.convertirAEntero(forma
								.getIndexValoresAsocidos())).getCodigoPk()
				.equals("")) {
			if (!Plantillas.actualizarValoresAsociadosOpciones(con, forma
					.getListCampoTemporalPos(
							Integer.parseInt(forma.getIndexCampo()))
					.getOpciones().get(
							Integer.parseInt(forma.getIndexOpciones()))
					.getValoresOpcion().get(
							Utilidades.convertirAEntero(forma
									.getIndexValoresAsocidos())).getCodigoPk(),
					false, usuario)) {
				UtilidadBD.abortarTransaccion(con);
				return;
			}
		}

		// Elimina el registro del array de Opciones
		forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo()))
				.getOpciones().get(Integer.parseInt(forma.getIndexOpciones()))
				.getValoresOpcion().remove(
						Utilidades.convertirAEntero(forma
								.getIndexValoresAsocidos()));
		UtilidadBD.finalizarTransaccion(con);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoAddOpcionesValores(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		DtoValorOpcionCampoParam dtoValores = new DtoValorOpcionCampoParam();

		dtoValores.setValor(forma.getIndexValoresAsocidos());

		// Agrega el valor ingresado
		forma.getListCampoTemporal().get(
				Utilidades.convertirAEntero(forma.getIndexCampo()))
				.getOpciones().get(
						Utilidades.convertirAEntero(forma.getIndexOpciones()))
				.getValoresOpcion().add(dtoValores);

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoEliminarOpcionesSeccion(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		UtilidadBD.iniciarTransaccion(con);

		// Verifica que exista la Seccion dentro de la opciï¿½n del campo en base
		// de datos
		if (!forma.getListCampoTemporalPos(
				Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
				Integer.parseInt(forma.getIndexOpciones())).getSecciones().get(
				Utilidades.convertirAEntero(forma.getIndexSeccionesAsocidas()))
				.getCodigoPkDetSeccion().equals("")) {
			if (!Plantillas.actualizarSeccionesAsociadasOpciones(con, forma
					.getListCampoTemporalPos(
							Integer.parseInt(forma.getIndexCampo()))
					.getOpciones().get(
							Integer.parseInt(forma.getIndexOpciones()))
					.getSecciones().get(
							Utilidades.convertirAEntero(forma
									.getIndexSeccionesAsocidas()))
					.getCodigoPkDetSeccion(), false, usuario)) {
				UtilidadBD.abortarTransaccion(con);
				return;
			}
		}

		// Elimina el registro del array de Opciones
		forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo()))
				.getOpciones().get(Integer.parseInt(forma.getIndexOpciones()))
				.getSecciones().remove(
						Utilidades.convertirAEntero(forma
								.getIndexSeccionesAsocidas()));
		UtilidadBD.finalizarTransaccion(con);
		// Actualiza el listado
		metodoSeccionAsociada(con, forma, usuario);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoAddOpcionesSeccion(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		// Agrega la Secciï¿½n seleccionada
		forma.getListCampoTemporal().get(
				Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
				Utilidades.convertirAEntero(forma.getIndexOpciones()))
				.getSecciones().add(
						forma.getListSeccionTemporalPos(Utilidades
								.convertirAEntero(forma
										.getIndexSeccionesAsocidas())));

		// Actualiza el listado
		metodoSeccionAsociada(con, forma, usuario);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoSeccionAsociada(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		logger.info("valor de secciones >> "
				+ forma.getPlantillaDto().getSeccionesValor().size());
		// Almacena la informaciï¿½n de todos las secciones disponibles para la
		// opciï¿½n
		forma.setListSeccionTemporal(forma.getPlantillaDto()
				.obtenerListadoSeccionValor(
						forma.getListCampoTemporal().get(
								Integer.parseInt(forma.getIndexCampo()))
								.getOpciones().get(
										Utilidades.convertirAEntero(forma
												.getIndexOpciones()))
								.getSecciones()));

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param esLLamadoPopUp
	 */
	private void metodoRestringirCamposParam(Connection con,
			ParametrizacionPlantillasForm forma, boolean esLLamadoPopUp) {
		// Carga la informaciï¿½n de la formula
		DtoCampoParametrizable campoParam = new DtoCampoParametrizable();
		String[] formulaArray;

		// Esta lleno solo cuando se va a modificar el campo que posee la
		// formula
		if (esLLamadoPopUp && !forma.getIndexCampo().equals(""))
			logger.info("valor de la formula >> "
					+ forma.getListCampoTemporalPos(
							Integer.parseInt(forma.getIndexCampo()))
							.getFormulaCompleta());

		// Inicializa todos los campos en Usado Formula
		for (int ca = 0; ca < forma.getListCampoTemporal().size(); ca++)
			forma.getListCampoTemporal().get(ca).setUsadoFormula("", "", false);

		for (int ca = 0; ca < forma.getListCampoTemporal().size(); ca++) {
			campoParam = forma.getListCampoTemporal().get(ca);

			if (!campoParam.getFormulaCompleta().equals("")) {
				formulaArray = campoParam.getFormulaCompleta().split(
						ConstantesBD.separadorSplit);

				for (int i = 0; i < formulaArray.length; i++) {
					if (tipoCaracterFormula(formulaArray[i]).equals("campos")) {
						forma.getListCampoTemporalCodigoPk(
								formulaArray[i].replace("__", ""))
								.setUsadoFormula(
										campoParam.getCodigoPK(),
										campoParam.getCodigo() + " - "
												+ campoParam.getNombre(), true);
					}
				}
			}
		}
		// Actualiza el estado dependiendo si es la modificacion de un campo o
		// la creacion de uno nuevo
		if (esLLamadoPopUp && !forma.getIndexCampo().equals("")) {
			if (!forma.getListCampoTemporalPos(
					Integer.parseInt(forma.getIndexCampo())).getCodigoPK()
					.equals(""))
				forma.setEstado("cargarModificarCampoParam");
			else
				forma.setEstado("adicionCampo");
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void metodoGuardarDeclaracion(Connection con,
			ParametrizacionPlantillasForm forma) {
		String formulaComprobacion = "";
		String formula = "";
		HashMap nuevaFormula = new HashMap();
		boolean indicador = false;
		Random rando = new Random();
		int numRegistros = Integer.parseInt(forma.getFormulaMap("numRegistros")
				.toString());

		// Ordenar el mapa
		for (int i = 0; i < numRegistros; i++) {
			nuevaFormula.put("orden_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("orden_" + i));
			nuevaFormula.put("valor_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("valor_" + i));
			nuevaFormula.put("activo_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("activo_" + i));
			nuevaFormula.put("tipo_" + forma.getFormulaMap("orden_" + i), forma
					.getFormulaMap("tipo_" + i));
			nuevaFormula.put(
					"descripcion_" + forma.getFormulaMap("orden_" + i), forma
							.getFormulaMap("descripcion_" + i));
		}

		Utilidades.imprimirMapa(nuevaFormula);

		// Recorre la formula parametrizada
		for (int i = 0; i < numRegistros; i++) {
			indicador = false;

			// Validacion para cuando se de este caso = ConstanteCampo o
			// CampoCampo o ConstanteConstante o CampoConstante
			if (i > 0
					&& (tipoCaracterFormula(
							nuevaFormula.get("valor_" + (i - 1)).toString())
							.equals("campos")
							|| tipoCaracterFormula(
									nuevaFormula.get("valor_" + (i - 1))
											.toString()).equals("constantes") || tipoCaracterFormula(
							nuevaFormula.get("valor_" + (i - 1)).toString())
							.equals("otrasConstantes"))
					&& (tipoCaracterFormula(
							nuevaFormula.get("valor_" + i).toString()).equals(
							"campos")
							|| tipoCaracterFormula(
									nuevaFormula.get("valor_" + i).toString())
									.equals("constantes") || tipoCaracterFormula(
							nuevaFormula.get("valor_" + i).toString()).equals(
							"otrasConstantes"))) {
				logger
						.info("Validacion formula. Error No. 1. ConstanteCampo o CampoCampo o ConstanteConstante o CampoConstante");
				formulaComprobacion += "*/*/*";
				indicador = true;
			}

			if (!indicador) {
				if (tipoCaracterFormula(
						nuevaFormula.get("valor_" + i).toString()).equals(
						"campos")
						|| tipoCaracterFormula(
								nuevaFormula.get("valor_" + i).toString())
								.equals("otrasConstantes")) {
					formulaComprobacion += Math.abs(rando.nextInt(1000)) + "";
				} else
					formulaComprobacion += nuevaFormula.get("valor_" + i)
							.toString();
			}

			formula += nuevaFormula.get("valor_" + i).toString()
					+ ConstantesBD.separadorSplit;
		}

		nuevaFormula.put("formula", formula);
		forma.setFormulaMap(nuevaFormula);
		forma.setFormulaMap("numRegistros", numRegistros);
		forma.setFormulaComprobar(formulaComprobacion);
		logger.info("valor de la formula >> " + formula + " >> "
				+ formulaComprobacion);
		forma.setEstado("respuestaFormulaValidada");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void metodoEliminarFormulaCampoParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		HashMap nuevaFormula = new HashMap();
		int cont = 0;

		// Ordenar el mapa
		for (int i = 0; i < Integer.parseInt(forma
				.getFormulaMap("numRegistros").toString()); i++) {
			nuevaFormula.put("orden_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("orden_" + i));
			nuevaFormula.put("valor_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("valor_" + i));
			nuevaFormula.put("activo_" + forma.getFormulaMap("orden_" + i),
					forma.getFormulaMap("activo_" + i));
			nuevaFormula.put("tipo_" + forma.getFormulaMap("orden_" + i), forma
					.getFormulaMap("tipo_" + i));
			nuevaFormula.put(
					"descripcion_" + forma.getFormulaMap("orden_" + i), forma
							.getFormulaMap("descripcion_" + i));
		}

		for (int i = 0; i < Integer.parseInt(forma
				.getFormulaMap("numRegistros").toString()); i++) {
			if (forma.getFormulaMap("activo_" + i).toString().equals(
					ConstantesBD.acronimoSi)) {
				nuevaFormula.put("orden_" + cont, cont);
				nuevaFormula.put("valor_" + cont, forma.getFormulaMap("valor_"
						+ i));
				nuevaFormula.put("activo_" + cont, ConstantesBD.acronimoSi);
				nuevaFormula.put("tipo_" + cont, forma.getFormulaMap("tipo_"
						+ i));
				nuevaFormula.put("descripcion_" + cont, forma
						.getFormulaMap("descripcion_" + i));
				cont++;
			}
		}

		nuevaFormula.put("formula", "");
		forma.setFormulaMap(nuevaFormula);
		forma.setFormulaMap("numRegistros", cont);

		// Utilidades.imprimirMapa(forma.getFormulaMap());
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void metodoCargarFormulaCampoParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		// Carga la informaciï¿½n de la formula
		String formula = "";
		forma.setFormulaMap(new HashMap());
		forma.setFormulaMap("numRegistros", "0");
		forma.setFormulaMap("formula", "");
		DtoCampoParametrizable campoParam = new DtoCampoParametrizable();
		DtoElementoParam elementoParam = new DtoElementoParam();

		if (forma.getIndexNivel().equals("2")) {
			elementoParam = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()));

			formula = forma.getListCampoTemporalPos(
					Integer.parseInt(forma.getIndexCampo()))
					.getFormulaCompleta();
		} else if (forma.getIndexNivel().equals("3")) {
			elementoParam = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()));

			formula = forma.getListCampoTemporalPos(
					Integer.parseInt(forma.getIndexCampo()))
					.getFormulaCompleta();
		}

		logger.info("valor de la formula >> " + formula);

		if (!formula.toString().equals("")) {
			String[] formulaArray = formula.split(ConstantesBD.separadorSplit);

			for (int i = 0; i < formulaArray.length; i++) {

				forma.setFormulaMap("orden_" + i, i);
				forma.setFormulaMap("valor_" + i, formulaArray[i]);
				forma.setFormulaMap("activo_" + i, ConstantesBD.acronimoSi);
				forma.setFormulaMap("tipo_" + i, tipoCaracterFormula(
						formulaArray[i]).toString());

				if (forma.getFormulaMap("tipo_" + i).toString()
						.equals("campos")) {
					// logger.info("valor de la key >> "+formulaArray[i].replace("__",""));
					campoParam = elementoParam
							.getCampoParametrizable(formulaArray[i].replace(
									"__", ""));

					if (campoParam != null)
						forma.setFormulaMap("descripcion_" + i, campoParam
								.getNombre());
					else
						forma.setFormulaMap("descripcion_" + i, "error");
				} else if (forma.getFormulaMap("tipo_" + i).toString().equals(
						"otrasConstantes")) {
					if (formulaArray[i]
							.toString()
							.equals(
									"__"
											+ ConstantesCamposParametrizables.edadPacienteDias
											+ "__"))
						forma.setFormulaMap("descripcion_" + i,
								"Edad Pac. Dias");
					else if (formulaArray[i]
							.toString()
							.equals(
									"__"
											+ ConstantesCamposParametrizables.edadPacienteMeses
											+ "__"))
						forma.setFormulaMap("descripcion_" + i,
								"Edad Pac. Meses");
					else if (formulaArray[i]
							.toString()
							.equals(
									"__"
											+ ConstantesCamposParametrizables.edadPacienteAnios
											+ "__"))
						forma.setFormulaMap("descripcion_" + i,
								"Edad Pac. Aï¿½os");
				} else
					forma.setFormulaMap("descripcion_" + i, formulaArray[i]);

			}

			forma.setFormulaMap("numRegistros", formulaArray.length);
		}

		cargarCamposFormula(forma);

		// Utilidades.imprimirMapa(forma.getFormulaMap());
	}

	/**
	 * 
	 * @param forma
	 */
	public void cargarCamposFormula(ParametrizacionPlantillasForm forma) {
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros", "0");

		if (!forma.getListCampoTemporalPos(
				Integer.parseInt(forma.getIndexCampo())).getCodigoPK().equals(
				""))
			forma.setListCamposFormula(forma.getListCampoTemporal());
		else {
			if (forma.getIndexNivel().equals("2")) {
				forma.setListCamposFormula(forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getElementoSeccionPos(
								Integer.parseInt(forma.getIndexElemento()))
						.getCampos());
			} else if (forma.getIndexNivel().equals("3")) {
				forma
						.setListCamposFormula(forma.getPlantillaDto()
								.getSeccionesFijasPos(
										Integer.parseInt(forma
												.getIndexSeccionFija()))
								.getElementoSeccionPos(
										Integer.parseInt(forma
												.getIndexElemento()))
								.getSeccionesPos(
										Integer.parseInt(forma
												.getIndexSeccionNivel2()))
								.getCampos());
			}
		}
	}

	/**
	 * 
	 * @param caracter
	 * @return
	 */
	private String tipoCaracterFormula(String caracter) {
		if (caracter.trim().equals("-") || caracter.trim().equals("+")
				|| caracter.trim().equals("/") || caracter.trim().equals("*")
				|| caracter.trim().equals("^"))
			return "operadores";
		else if (caracter.trim().equals("(") || caracter.trim().equals(")"))
			return "signos";
		else if (caracter.trim().equals(
				"__" + ConstantesCamposParametrizables.edadPacienteDias + "__")
				|| caracter
						.trim()
						.equals(
								"__"
										+ ConstantesCamposParametrizables.edadPacienteMeses
										+ "__")
				|| caracter
						.trim()
						.equals(
								"__"
										+ ConstantesCamposParametrizables.edadPacienteAnios
										+ "__"))
			return "otrasConstantes";
		else if (caracter.startsWith("__")
				&& caracter.toString().endsWith("__"))
			return "campos";
		else
			return "constantes";

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoEliminarCampoParam(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String codigoPkCampoParam = "";
		UtilidadBD.iniciarTransaccion(con);

		if (forma.getIndexNivel().equals("2")) {
			codigoPkCampoParam = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCampos().get(Integer.parseInt(forma.getIndexCampo()))
					.getCodigoPK();
		} else if (forma.getIndexNivel().equals("3")) {
			codigoPkCampoParam = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCampos().get(Integer.parseInt(forma.getIndexCampo()))
					.getCodigoPK();
		}

		if (!Plantillas.actualizarMostrarModCamposParametrizables(con,
				codigoPkCampoParam, false, usuario)) {
			UtilidadBD.abortarTransaccion(con);
			return errores;
		}

		if (forma.getIndexNivel().equals("2")) {
			forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCampos()
					.remove(Integer.parseInt(forma.getIndexCampo()));
		} else if (forma.getIndexNivel().equals("3")) {
			forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCampos()
					.remove(Integer.parseInt(forma.getIndexCampo()));
		}

		forma.setIndexSeccionNivel2("");

		UtilidadBD.finalizarTransaccion(con);
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void metodoCargarModificarCampoParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		ArrayList<DtoCampoParametrizable> tmp = new ArrayList<DtoCampoParametrizable>();
		boolean esPermitirAsociados = false;
		forma.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());

		// Indicador Restricciï¿½n por Valores de Campo de la Secciï¿½n Contenedora
		if (forma.getPlantillaDto().getSeccionesFijasPos(
				Integer.parseInt(forma.getIndexSeccionFija()))
				.getElementoSeccionPos(
						Integer.parseInt(forma.getIndexElemento()))
				.getIndicativoRestriccionValCamp().equals(
						ConstantesBD.acronimoSi))
			esPermitirAsociados = false;
		else
			esPermitirAsociados = true;

		// Carga la Informacion de la seccion seleccionado
		if (forma.getIndexNivel().equals("2")) {
			tmp = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCampos();
		} else if (forma.getIndexNivel().equals("3")) {
			tmp = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCampos();
		}

		for (int i = 0; i < tmp.size(); i++) {
			DtoCampoParametrizable campo = new DtoCampoParametrizable();

			try {
				tmp.get(i).setPermitirAsociados(esPermitirAsociados);
				PropertyUtils.copyProperties(campo, tmp.get(i));

				forma.setManejaIMgtmp(campo.getManejaImagen());
				forma.setCodigoTipotmp(campo.getCodigoTipo());
				forma.setImagenAsociartmp(campo.getImagenAsociar());

				forma.getListCampoTemporal().add(campo);

			} catch (Exception e) {
				logger.warn(e);
			}
		}

		// Restringe los campos que son usados en la formula
		metodoRestringirCamposParam(con, forma, false);

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoGuardarCampoParam(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		ActionErrors errores = new ActionErrors();
		int codigoPkPlantillaSec = ConstantesBD.codigoNuncaValido;
		ArrayList<DtoCampoParametrizable> arrayAnterior = new ArrayList<DtoCampoParametrizable>();

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		if (forma.getIndexNivel().equals("2")) {
			codigoPkPlantillaSec = Integer.parseInt(forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getConsecutivoParametrizacion());

			// Carga la Informaciï¿½n de la seccion Selecciona Original, sin
			// modificaciones
			arrayAnterior = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCampos();
		}
		if (forma.getIndexNivel().equals("3")) {
			codigoPkPlantillaSec = Integer.parseInt(forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getConsecutivoParametrizacion());

			// Carga la Informaciï¿½n de la seccion Selecciona Original, sin
			// modificaciones
			arrayAnterior = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCampos();
		}

		// Operaciones segun el estado de la forma
		if (forma.getEstado().equals("guardarCampo")) {

			for (int i = 0; i < forma.getListCampoTemporal().size(); i++)
				forma.getListCampoTemporal().get(i).setOrden(
						forma.getListCampoTemporal().get(i).getOrden()
								+ arrayAnterior.size());

			errores = Plantillas.insertarCamposParametrizablesSeccion(con,
					codigoPkPlantillaSec + "", forma.getListCampoTemporal(),
					usuario);
		} else if (forma.getEstado().equals("modificarCampo")) {
			errores = Plantillas.actualizarCamposParametrizables(con,
					codigoPkPlantillaSec + "", forma.getListCampoTemporal(),
					arrayAnterior, usuario);
		}

		if (!errores.isEmpty()) {
			transaccion = false;
			UtilidadBD.abortarTransaccion(con);
			return errores;
		}

		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			if (forma.getEstado().equals("guardarCampo"))
				// Recarga el array de Campo Temporales
				forma
						.setListCampoTemporal(new ArrayList<DtoCampoParametrizable>());

			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);
		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));

		}
		// Recarga el Dto de Plantilla
		// metodoCargarPlantilla(con, forma, usuario);
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors validacionesGuardarCampoParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		ActionErrors errores = new ActionErrors();

		Plantillas plantilla = new Plantillas();

		DtoCampoParametrizable campo;

		int centroCosto = ConstantesBD.codigoNuncaValido;
		int sexo = ConstantesBD.codigoNuncaValido;
		int especialidad = ConstantesBD.codigoNuncaValido;

		if (forma
				.getPlantillaBase()
				.equals(
						ConstantesCamposParametrizables.funcParametrizableValoracionConsulta
								+ "")
				|| forma
						.getPlantillaBase()
						.equals(
								ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta
										+ "")) {
			if (forma.getEspecialidadParametrizable().equals("")) {
				especialidad = ConstantesBD.codigoEspecialidadMedicaTodos;
			} else {
				especialidad = Utilidades.convertirAEntero(forma
						.getEspecialidadParametrizable());
			}

		} else {
			if (forma.getCentroCosto().equals("")) {
				centroCosto = ConstantesBD.codigoCentroCostoTodos;
			} else {
				centroCosto = Utilidades.convertirAEntero(forma
						.getCentroCosto());
			}
			if (forma.getSexo().equals("")) {
				sexo = ConstantesBD.codigoSexoTodos;
			} else {
				sexo = Utilidades.convertirAEntero(forma.getSexo());
			}
		}

		forma.setMapaCamposExistentes(plantilla.consultarCamposExitentes(con,
				Utilidades.convertirAEntero(forma.getPlantillaBase()),
				centroCosto, sexo, especialidad));

		// Recorremos los campos
		for (int i = 0; i < forma.getListCampoTemporal().size(); i++) {
			campo = forma.getListCampoTemporal().get(i);

			/*
			 * ahora la asignacion es automatica
			// Validamos el codigo
			if (campo.getCodigo().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Codigo del Registro Nro. " + (i + 1)));

			*/
			// Validamos el nombre
			if (campo.getNombre().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Nombre del Registro Nro. " + (i + 1)));

			for (int j = 0; j < forma.getListCampoTemporal().size(); j++) {

				if (campo.getCodigo().equals(
						forma.getListCampoTemporal().get(j).getCodigo())
						&& i != j && !(campo.getCodigo().equals("") && forma.getListCampoTemporal().get(j).getCodigo().equals("")) ) {
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"El codigo del Registro Nro. "
													+ (i + 1)
													+ " se encuentra repetido con el codigo Registro Nro. "
													+ (j + 1)
													+ ". Verificar los Nuevos Registros."));
				}
				if (campo.getNombre().equals(
						forma.getListCampoTemporal().get(j).getNombre())
						&& i != j) {
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"El nombre del Registro Nro. "
													+ (i + 1)
													+ " se encuentra repetido con el nombre del Registro Nro. "
													+ (j + 1)
													+ ". Verificar los Nuevos Registros."));
				}

			}

			// Validamos el tipo
			if (campo.getCodigoTipo() <=0) {
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Tipo del Registro Nro. " + (i + 1)));
			} else {
				// Cuando el tipo es Texto Predeterminado
				if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoTextoPredeterminado) {
					if (campo.getEtiqueta().equals(""))
						errores.add("descripcion", new ActionMessage(
								"errors.required",
								"La Etiqueta del Registro Nro. " + (i + 1)));
				}

				if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoCaracter
						|| campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoAreaTexto
						|| campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero
						|| campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal) {
					if (campo.getTamanio() <= 0)
						errores.add("descripcion", new ActionMessage(
								"errors.notEspecific",
								"El Tamaï¿½o del Registro Nro. " + (i + 1)
										+ " debe ser un Entero mayo a cero"));
				}

				if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoEntero
						|| campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal) {
					if (campo.getSigno().equals(""))
						errores.add("descripcion", new ActionMessage(
								"errors.required",
								"El Signo del Registro Nro. " + (i + 1)));

					// if(campo.getCodigoUnidad() ==
					// ConstantesBD.codigoNuncaValido)
					// errores.add("descripcion",new
					// ActionMessage("errors.required","La Unidad del Registro Nro. "+(i+1)));

					if (campo.getMaximo() <= 0)
						errores.add("descripcion", new ActionMessage(
								"errors.required",
								"El Mï¿½ximo del Registro Nro. " + (i + 1)
										+ " debe ser un Entero mayor a cero"));

					if (campo.getMinimo() < 0)
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.required",
												"El Mï¿½nimo del Registro Nro. "
														+ (i + 1)
														+ " debe ser un Entero mayor/igual a cero"));

					if (campo.getMinimo() > campo.getMaximo())
						errores.add("codigo", new ActionMessage(
								"errors.notEspecific",
								"El Valor maximo del registro " + (i + 1)
										+ " debe ser mayor al valor minimo"));

					if (!campo.getValorPredeterminado().equals("")
							&& (Utilidades.convertirADouble(campo
									.getValorPredeterminado()) < campo
									.getMinimo() || Utilidades
									.convertirADouble(campo
											.getValorPredeterminado()) > campo
									.getMaximo()))
						errores
								.add(
										"codigo",
										new ActionMessage(
												"errors.notEspecific",
												"El Valor predeterminado del registro "
														+ (i + 1)
														+ " debe estar entre el valor minimo y el valor maximo."));

					if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoNumericoDecimal) {
						if (campo.getDecimales() <= 0)
							errores
									.add(
											"descripcion",
											new ActionMessage(
													"errors.notEspecific",
													"El Numero de Decimales del Registro Nro. "
															+ (i + 1)
															+ " debe ser un Entero mayor a cero"));
					}
				}

				if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo
						|| campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion) {
					errores.add(validacionesOpcionCampo(forma, campo, i));
				}
			}

			if (!campo.isUnicoXFila()) {
				if (campo.getColumnasOcupadas() <= 0)
					errores.add("descripcion", new ActionMessage(
							"errors.notEspecific",
							"Las Columnas Ocupadas del Registro Nro. "
									+ (i + 1)
									+ " debe ser un Entero mayor a cero"));
			}

			for (int k = 0; k < Utilidades.convertirAEntero(forma
					.getMapaCamposExistentes("numRegistros")
					+ ""); k++) {

				// logger.info("mostrarMod>>>>>>>>>>"+forma.getMapaCamposExistentes("mostrarmodificacion_"+k));

				if ((forma.getMapaCamposExistentes("mostrarmodificacion_" + k) + "")
						.equals(ConstantesBD.acronimoSi)
						&& !forma.getEstado().equals("modificarCampo")) {

					// logger.info("codigo>>>>>>>>"+campo.getCodigo());

					// logger.info("codigoEx"+forma.getMapaCamposExistentes("codigo_"+k));

					if (campo.getCodigo().equals(
							forma.getMapaCamposExistentes("codigo_" + k) + "")) {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"Ya existe un campo creado con el codigo "
														+ forma
																.getMapaCamposExistentes("codigo_"
																		+ k)
														+ ", por favor verifique."));
					}
					if (!(forma.getMapaCamposExistentes("nombre_" + k) + "")
							.equals("")) {

						// logger.info("nombreEx>>>>>>>"+forma.getMapaCamposExistentes("nombre_"+k));

						// logger.info("nombre>>>>>>>"+campo.getNombre());

						if (campo.getNombre().equals(
								forma.getMapaCamposExistentes("nombre_" + k)
										+ "")) {
							errores
									.add(
											"descripcion",
											new ActionMessage(
													"errors.notEspecific",
													"Ya existe un campo creado con el nombre "
															+ forma
																	.getMapaCamposExistentes("nombre_"
																			+ k)
															+ ", por favor verifique."));
						}
					}
				}
			}
		}

		return errores;
	}

	/**
	 * 
	 * @param forma
	 * @param campo
	 * @param pos
	 * @return
	 */
	private ActionErrors validacionesOpcionCampo(
			ParametrizacionPlantillasForm forma, DtoCampoParametrizable campo,
			int pos) {

		ActionErrors errores = new ActionErrors();
		ArrayList<DtoOpcionCampoParam> array = new ArrayList<DtoOpcionCampoParam>();
		array = campo.getOpciones();

		if (array.size() <= 0) {
			errores.add("descripcion",
					new ActionMessage("errors.notEspecific",
							"No Existen Opciones para el Campo Nro. "
									+ (pos + 1) + "."));

			if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion) {
				if (campo.getManejaImagen().equals("")) {
					errores.add("descripcion", new ActionMessage(
							"errors.required", "El Campo Maneja Imagen Nro. "
									+ (pos + 1) + " "));
				} else {
					if (campo.getImagenAsociar().equals(""))
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Campo Maneja Imagen Nro. "
														+ (pos + 1)
														+ " Requiere que se le Asocie una Imagen Base. "));
				}
			}
		}

		// Se reccore las opciones
		for (int i = 0; i < array.size(); i++) {
			// Verifica que el valor y la opcion no esten vacios
			if (array.get(i).getValor().equals("")
					|| array.get(i).getOpcion().equals(""))
				errores.add("descripcion", new ActionMessage(
						"errors.notEspecific",
						"El Valor y Nombre de la Opciï¿½n Nro. " + (i + 1)
								+ " del Campo Nro. " + (pos + 1)
								+ " son Requeridos "));
			else {
				// se valida que no se encuentre repetidos
				for (int j = 0; j < array.size() && j != i; j++) {
					if (array.get(j).getOpcion().equals(
							array.get(i).getOpcion())) {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Valor de la Opciï¿½n Nro. "
														+ (i + 1)
														+ " se encuentra Repetido con la Opciï¿½n  Nro. "
														+ (j + 1)
														+ " del Campo Nro. "
														+ (pos + 1) + "."));
						return errores;
					}

				}
			}

			// validaccion convenciones odontologiaca
			// Anexo 841
			if (campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion) {
				logger.info("\n\n\n\n ENTRA VALIDACION convenciones Codigo "+array.get(i).getConvencionOdon().getCod()+" pos >>"+i);
				if (campo.getManejaImagen().equals(ConstantesBD.acronimoSi)) {
					if (array.get(i).getConvencionOdon().getCod() == null
							|| array.get(i).getConvencionOdon().getCod()
									.equals("")) {
						errores.add("descripcion", new ActionMessage(
								"errors.notEspecific",
								"No Existen Convenciï¿½n Odontologica para el Campo Nro. "
										+ (pos + 1) + "."));
					}
					if (campo.getImagenAsociar().equals(""))
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Campo Maneja Imagen Nro. "
														+ (pos + 1)
														+ " Requiere que se le Asocie una Imagen Base. "));

				} else {
					if (campo.getManejaImagen().equals(""))
						errores.add("descripcion", new ActionMessage(
								"errors.required",
								"El Campo Maneja Imagen Nro. " + (pos + 1)
										+ " "));
				}
			}
		}

		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public ActionErrors metodoEliminarSeccionParam(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String codigoPkSeccionParam = "", codigoPkPlantillasSecc = "";

		UtilidadBD.iniciarTransaccion(con);

		if (forma.getIndexNivel().equals("fija")) {
			codigoPkSeccionParam = forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getCodigoSeccionParam();
			codigoPkPlantillasSecc = forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(0).getConsecutivoParametrizacion();
		} else if (forma.getIndexNivel().equals("1")) {
			codigoPkSeccionParam = forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCodigoPK();
			codigoPkPlantillasSecc = forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getConsecutivoParametrizacion();
		} else if (forma.getIndexNivel().equals("2"))
			codigoPkSeccionParam = forma.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCodigoPK();

		if (!codigoPkPlantillasSecc.equals("")) {
			// Desasocia la seccion de los campos tipo select que la tengan
			// asociadas
			logger
					.info("Desasociar Seccion de Opciones >> codigo Pk Plantillas Secciones >> "
							+ codigoPkPlantillasSecc);
			Plantillas.desasociarSeccionOculta(con, codigoPkPlantillasSecc,
					false, usuario);
		}

		// Actualiza la informaciï¿½n de la seccion parametrizable
		Plantillas.actualizarMostrarModOrdenSeccionParam(con, false, "",
				codigoPkSeccionParam, usuario);

		UtilidadBD.finalizarTransaccion(con);
		// Recarga el Dto de Plantilla
		metodoCargarPlantilla(con, forma, usuario);
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public ActionErrors metodoModificarDetalleSeccion(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		int numRegistros = forma.getPlantillaDto().getSeccionesFijasPos(
				Integer.parseInt(forma.getIndexSeccionFija())).getElementosPos(
				Integer.parseInt(forma.getIndexElemento())).getSecciones()
				.size();
		DtoSeccionParametrizable seccionParam;

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		// Recorre los elementos pertenecientes a la secciï¿½n de primer nivel
		for (int i = 0; i < numRegistros; i++) {
			seccionParam = forma
					.getPlantillaDto()
					.getSeccionesFijasPos(
							Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementosPos(Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(i);

			transaccion = Plantillas.actualizarMostrarModOrdenSeccionParam(con,
					seccionParam.isMostrarModificacion(), seccionParam
							.getOrden()
							+ "", seccionParam.getCodigoPK() + "", usuario);
		}

		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}

		// Recarga el Dto de Plantilla
		metodoCargarPlantilla(con, forma, usuario);
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public ActionErrors validacionesGuardarSeccionParam(Connection con,ParametrizacionPlantillasForm forma) 
	{
		ActionErrors errores = new ActionErrors();

		Plantillas plantilla = new Plantillas();

		int numRegistros = forma.getListSeccionTemporal().size();

		int centroCosto = ConstantesBD.codigoNuncaValido;
		int sexo = ConstantesBD.codigoNuncaValido;
		int especialidad = ConstantesBD.codigoNuncaValido;

		if (forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsulta+ "")|| forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta+ "")) 
		{
			if (forma.getEspecialidadParametrizable().equals("")) 
			{
				especialidad = ConstantesBD.codigoEspecialidadMedicaTodos;
			} 
			else 
			{
				especialidad = Utilidades.convertirAEntero(forma.getEspecialidadParametrizable());
			}

		} 
		else 
		{
			if (forma.getCentroCosto().equals("")) 
			{
				centroCosto = ConstantesBD.codigoCentroCostoTodos;
			} 
			else 
			{
				centroCosto = Utilidades.convertirAEntero(forma.getCentroCosto());
			}
			if (forma.getSexo().equals("")) 
			{
				sexo = ConstantesBD.codigoSexoTodos;
			} 
			else 
			{
				sexo = Utilidades.convertirAEntero(forma.getSexo());
			}
		}

		forma.setMapaSeccionesExistentes(plantilla.consultarSeccionesExistentes(con, Utilidades.convertirAEntero(forma.getPlantillaBase()),centroCosto, sexo, especialidad));

		// una seccion ya parametrizada solo se cargara una en el array y no se
		// le puede dar orden con respecto a otras
		if (forma.getEstado().equals("guardarSeccion")) 
		{
			ArrayList<DtoSeccionParametrizable> tmp = new ArrayList<DtoSeccionParametrizable>();

			for (int j = 0; j < numRegistros; j++)
				tmp.add(new DtoSeccionParametrizable());

			for (int j = 0; j < numRegistros; j++)
				tmp.set(forma.getListSeccionTemporalPos(j).getOrden() - 1,
						forma.getListSeccionTemporalPos(j));

			forma.setListSeccionTemporal(tmp);
		}
		// --------------------------------------------

		for (int i = 0; i < numRegistros; i++) 
		{
			// Valida el codigo
			/*
			 * se asigna automaticamente.
			if (forma.getListSeccionTemporalPos(i).getCodigo().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required","El Código del Registro Nro. " + (i + 1)));
				*/

			// Valida el numero de columnas
			if ((forma.getListSeccionTemporalPos(i).getColumnasSeccion() + "").equals(""))
				errores.add("descripcion", new ActionMessage("errors.required","El Numero de Columnas del Registro Nro. " + (i + 1)));
			else 
			{
				if (forma.getListSeccionTemporalPos(i).getColumnasSeccion() == 0)
					errores.add("descripcion", new ActionMessage("errors.notEspecific","El numero de Columnas para el Registro Nro. "+ (i + 1) + " debe ser mayor a cero "));
			}
			if (!forma.getListSeccionTemporalPos(i).getRangoInicialEdad().equals("")&& forma.getListSeccionTemporalPos(i).getRangoFInalEdad().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required","La edad final en dias del Registro Nro. " + (i + 1)));
			if (!forma.getListSeccionTemporalPos(i).getRangoFInalEdad().equals("")&& forma.getListSeccionTemporalPos(i).getRangoInicialEdad().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required","La edad inicial en dias del Registro Nro. "+ (i + 1)));

			else if (Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoInicialEdad()) > Utilidades.convertirAEntero(forma.getListSeccionTemporalPos(i).getRangoFInalEdad()))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La edad final en dias  del Registro Nro. "+ (i + 1)+ " debe ser mayor o igual que la edad inicial en dias. Verificar los Nuevos Registros."));

			// Verifica que no existan codigos Repetidos entre si
			for (int j = 0; j < numRegistros; j++) 
			{
				/*
				 * 
				if (forma.getListSeccionTemporalPos(i).getCodigo().equals(forma.getListSeccionTemporalPos(j).getCodigo())&& i != j) 
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El codigo del Registro Nro. "+ (i + 1) +" se encuentra repetido con el codigo Registro Nro. "+ (j + 1)+ ". Verificar los Nuevos Registros."));
				}
				*/
				if (forma.getListSeccionTemporalPos(i).getDescripcion().equals(forma.getListSeccionTemporalPos(j).getDescripcion())&& i != j) 
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El nombre del Registro Nro. "+ (i + 1)+ " se encuentra repetido con el nombre del Registro Nro. "+ (j + 1)+ ". Verificar los Nuevos Registros."));
				}
			}

			if(!forma.getEstado().equals("modificarSeccion"))
			{
				for (int k = 0; k < Utilidades.convertirAEntero(forma.getMapaSeccionesExistentes("numRegistros")+ ""); k++) 
				{
	
					// logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("mostrarmodificacion_"+i));
	
					if ((forma.getMapaSeccionesExistentes("mostrarmodificacion_"+ k) + "").equals(ConstantesBD.acronimoSi)) 
					{
	
						// logger.info("codigoEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("codigo_"+i));
	
						// logger.info("Codigo>>>>>>>>>>>>>"+forma.getListSeccionTemporalPos(i).getCodigo());
	
						// forma.getPlantillaDto().getSeccionesFijasPos(Utilidades.convertirAEntero(forma.getIndexSeccionFija())).getNombreSeccion()
	
						// logger.info("COMO VIENE EL INDEX DE LA SECCION FIJA >>>>>>>>>>>>>"+forma.getIndexSeccionFija());
	
						if (forma.getIndexSeccionFija().equals("")) 
						{
							if (forma.getListSeccionTemporalPos(i).getCodigo().equals(forma.getMapaSeccionesExistentes("codigo_"+ k)+ "")) 
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el codigo "+ forma.getMapaSeccionesExistentes("codigo_"+ k)+ ", por favor verifique."));
							}
						} 
						else 
						{
							if (forma.getListSeccionTemporalPos(i).getCodigo().equals(forma.getMapaSeccionesExistentes("codigo_"+ k)+ "")&&(forma.getPlantillaDto().getSeccionesFijasPos(Utilidades.convertirAEntero(forma.getIndexSeccionFija())).getCodigoSeccion() + "").equals(forma.getListSeccionTemporalPos(i).getCodigo())) 
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el codigo "+ forma.getMapaSeccionesExistentes("codigo_"+ k)+ ", por favor verifique."));
							}
						}
						if (!(forma.getMapaSeccionesExistentes("descripcion_" + i) + "").equals("")) 
						{
	
							// logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("descripcion_"+i));
	
							// logger.info("descripcion>>>>>>>>>>>>>"+forma.getListSeccionTemporalPos(i).getDescripcion());
	
							if (forma.getIndexSeccionFija().equals("")) 
							{
								if (forma.getListSeccionTemporalPos(i).getDescripcion().equals(forma.getMapaSeccionesExistentes("descripcion_"+ k)+ "")) 
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el nombre "+ forma.getMapaSeccionesExistentes("descripcion_"+ k)+ ", por favor verifique."));
								}
							} 
							else 
							{
								if (forma.getListSeccionTemporalPos(i).getDescripcion().equals(forma.getMapaSeccionesExistentes("descripcion_"+ k)+ "")&& !forma.getPlantillaDto().getSeccionesFijasPos(Utilidades.convertirAEntero(forma.getIndexSeccionFija())).getNombreSeccion().equals(forma.getMapaSeccionesExistentes("descripcion_"+ k)+ "")) 
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el nombre "+ forma.getMapaSeccionesExistentes("descripcion_"+ k)+ ", por favor verifique."));
								}
							}
						}
					}
				}
			}

			// *****Validaciones Anexo 860
			// En esta validacion se pregunta si
			if (forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico)) 
			{
				if (forma.getTipoFuncionalidad().equals(""))
					errores.add("descripcion", new ActionMessage("errors.notEspecific","Porfavor seleccione un Tipo de Funcionalidad."));
			}
			// ***Fin Validaciones Anexo 860

			if (!errores.isEmpty()) {
				// Verifica que no exista codigos repetidos entre las secciones
				// parametrizadas anteriormente
				if (!Plantillas.validarRepetidoSeccionParametrica(con, forma.getPlantillaDto(), forma.getListSeccionTemporalPos(i),forma.getIndexNivel(), forma.getIndexSeccionFija())) 
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro Nro. "+ (i + 1)+ " se encuentra repetido. Verificar los Registros ya Parametrizados."));
					return errores;
				}
			}
		}

		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public ActionErrors validacionesGuardarEscalaParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		ActionErrors errores = new ActionErrors();

		Plantillas plantilla = new Plantillas();

		int numRegistros = forma.getEscalasList().size();

		String[] codigosEscalas = Plantillas.obtenerCodigosInsertadosEscalas(
				forma.getPlantillaDto()).split(",");

		int centroCosto = ConstantesBD.codigoNuncaValido;
		int sexo = ConstantesBD.codigoNuncaValido;
		int especialidad = ConstantesBD.codigoNuncaValido;

		if (forma
				.getPlantillaBase()
				.equals(
						ConstantesCamposParametrizables.funcParametrizableValoracionConsulta
								+ "")
				|| forma
						.getPlantillaBase()
						.equals(
								ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta
										+ "")) {
			if (forma.getEspecialidadParametrizable().equals("")) {
				especialidad = ConstantesBD.codigoEspecialidadMedicaTodos;
			} else {
				especialidad = Utilidades.convertirAEntero(forma
						.getEspecialidadParametrizable());
			}

		} else {
			if (forma.getCentroCosto().equals("")) {
				centroCosto = ConstantesBD.codigoCentroCostoTodos;
			} else {
				centroCosto = Utilidades.convertirAEntero(forma
						.getCentroCosto());
			}
			if (forma.getSexo().equals("")) {
				sexo = ConstantesBD.codigoSexoTodos;
			} else {
				sexo = Utilidades.convertirAEntero(forma.getSexo());
			}
		}

		forma.setMapaEscalasComponentes(plantilla
				.consultarEscalasExitentesComponentes(con, Utilidades
						.convertirAEntero(forma.getPlantillaBase()),
						centroCosto, sexo, especialidad));

		// --------------------------------------------

		for (int i = 0; i < numRegistros; i++) {
			// Valida la escala
			if (forma.getEscalasListPos(i).getCodigoPK().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"Seleccionar la escala del Registro Nro. " + (i + 1)));

			for (int k = 0; k < codigosEscalas.length; k++) {

				if (forma.getEscalasListPos(i).getCodigoPK().equals(
						codigosEscalas[k]))
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"La escala "
													+ Utilidades
															.obtenerDescripcionEscala(
																	con,
																	forma
																			.getEscalasListPos(
																					i)
																			.getCodigoPK())
													+ " ya fue ingresada para esta plantilla. "));

			}

			// Verifica que no existan codigos Repetidos entre si
			for (int j = 0; j < numRegistros; j++) {
				if (forma.getEscalasListPos(i).getCodigoPK().equals(
						forma.getEscalasListPos(j).getCodigoPK())
						&& i != j) {
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"El Registro Nro. "
													+ (i + 1)
													+ " se encuentra repetido con el Registro Nro. "
													+ (j + 1)
													+ ". Verificar los Nuevos Registros."));
					return errores;
				}
			}

			for (int t = 0; t < Utilidades.convertirAEntero(forma
					.getMapaEscalasComponentes("numRegistros")
					+ ""); t++) {
				if ((forma
						.getMapaEscalasComponentes("mostrarmodificacion_" + t) + "")
						.equals(ConstantesBD.acronimoSi)) {

					if (forma.getEscalasListPos(i).getCodigoPK()
							.equals(
									forma.getMapaEscalasComponentes("escala_"
											+ t)
											+ "")) {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"La escala "
														+ Utilidades
																.obtenerDescripcionEscala(
																		con,
																		forma
																				.getEscalasListPos(
																						i)
																				.getCodigoPK())
														+ " ya se encuentra dentro del componente "
														+ Utilidades
																.obtenerDescripcionComponente(
																		con,
																		forma
																				.getMapaEscalasComponentes("componente_"
																						+ t)
																				+ "")
														+ " . "));
					}

				}
			}

		}
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public ActionErrors validacionesGuardarComponentesParam(Connection con,
			ParametrizacionPlantillasForm forma) {
		ActionErrors errores = new ActionErrors();

		int numRegistros = forma.getComponentesList().size();

		String[] codigosComponentes = Plantillas
				.obtenerCodigosInsertadosComponentes(forma.getPlantillaDto())
				.split(",");

		// --------------------------------------------

		for (int i = 0; i < numRegistros; i++) {

			// Valida el componente
			if (forma.getComponentesListPos(i).getCodigoPK().equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"Seleccionar el componente del Registro Nro. "
								+ (i + 1)));

			for (int k = 0; k < codigosComponentes.length; k++) {
				if (forma.getComponentesListPos(i).getCodigoPK().equals(
						codigosComponentes[k] + ""))
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"El componente "
													+ Utilidades
															.obtenerDescripcionComponente(
																	con,
																	forma
																			.getComponentesListPos(
																					i)
																			.getCodigoPK())
													+ " ya fue ingresado para esta plantilla. "));
			}

			// Verifica que no existan codigos Repetidos entre si
			for (int j = 0; j < numRegistros; j++) {
				if (forma.getComponentesListPos(i).getCodigoPK().equals(
						forma.getComponentesListPos(j).getCodigoPK())
						&& i != j) {
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"errors.notEspecific",
											"El Registro Nro. "
													+ (i + 1)
													+ " se encuentra repetido con el Registro Nro. "
													+ (j + 1)
													+ ". Verificar los Nuevos Registros."));
					return errores;
				}
			}

		}
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoEliminarOpcion(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();

		UtilidadBD.iniciarTransaccion(con);

		// Verifica que exista la opciï¿½n del campo en base de datos
		// Verifica que exista la opciï¿½n del campo en base de datos
		if (!forma.getListCampoTemporalPos(
				Integer.parseInt(forma.getIndexCampo())).getOpciones().get(
				Integer.parseInt(forma.getIndexOpcionCampo())).getCodigoPk()
				.equals("")) {
			if (!Plantillas.eliminarOpcionesCamposSec(con, forma
					.getListCampoTemporalPos(
							Integer.parseInt(forma.getIndexCampo()))
					.getOpciones().get(
							Integer.parseInt(forma.getIndexOpcionCampo()))
					.getCodigoPk())) {
				UtilidadBD.abortarTransaccion(con);
				return errores;
			}
		}

		// Elimina el registro del array de Opciones
		forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo()))
				.getOpciones().remove(
						Integer.parseInt(forma.getIndexOpcionCampo()));
		UtilidadBD.finalizarTransaccion(con);
		return errores;
	}

	/**
	 * 
	 * @param forma
	 */
	private void metodoCrearNuevaCampo(ParametrizacionPlantillasForm forma) {

		boolean esPermitidoAsocidos = false;

		// Indicador Restricciï¿½n por Valores de Campo de la Secciï¿½n Contenedora
		if (forma.getPlantillaDto().getSeccionesFijasPos(
				Integer.parseInt(forma.getIndexSeccionFija()))
				.getElementoSeccionPos(
						Integer.parseInt(forma.getIndexElemento()))
				.getIndicativoRestriccionValCamp().equals(
						ConstantesBD.acronimoSi))
			esPermitidoAsocidos = false;
		else
			esPermitidoAsocidos = true;

		// Reinicio el DtoCampo temporal
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
		dtoCampo.setDecimales(0);
		dtoCampo.setOpciones(new ArrayList<DtoOpcionCampoParam>());
		dtoCampo.setUnicoXFila(false);
		dtoCampo.setColumnasOcupadas(0);
		dtoCampo.setRequerido(false);
		dtoCampo.setPermitirAsociados(esPermitidoAsocidos);

		// Anexo 841
		dtoCampo.setManejaImagen("");
		dtoCampo.setImagenAsociar("");

		// Captura el orden, lo ubica en la ultima posiciï¿½n
		int numRegistros = forma.getListCampoTemporal().size();
		dtoCampo.setOrden(numRegistros + 1);

		forma.getListCampoTemporal().add(dtoCampo);
		forma.setEstado("adicionCampo");

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void metodoCargarModificarSeccionParam(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		forma.setListSeccionTemporal(new ArrayList<DtoSeccionParametrizable>());
		String codigoPkSeccion = "";

		if (forma.getIndexNivel().equals("fija"))
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getCodigoSeccionParam();
		else if (forma.getIndexNivel().equals("1"))
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getCodigoPK();
		else if (forma.getIndexNivel().equals("2"))
			codigoPkSeccion = forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()))
					.getElementoSeccionPos(
							Integer.parseInt(forma.getIndexElemento()))
					.getSeccionesPos(
							Integer.parseInt(forma.getIndexSeccionNivel2()))
					.getCodigoPK();

		// Carga la informaciï¿½n de la secciï¿½n Parametrizable, ya que se envia el
		// codigoPk solo retornara un valor
		forma.setListSeccionTemporal(Plantillas
				.consultarSeccionesParametrizables(con, usuario
						.getCodigoInstitucionInt(), codigoPkSeccion));

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoGuardarSecciones(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		String centroCosto = "";
		String sexo = "";

		if (forma.getCentroCosto().equals(
				ConstantesBD.codigoCentroCostoTodos + "")) {
			centroCosto = "";
		} else {
			centroCosto = forma.getCentroCosto();
		}
		if (forma.getSexo().equals(ConstantesBD.codigoSexoTodos + "")) {
			sexo = "";
		} else {
			sexo = forma.getSexo();
		}

		int consecutivoPlantillas = 0;
		
		if (forma.getPlantillaDto().getCodigoPK().equals("")) 
		{
			String especialidad = "";
			if (
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsulta+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+ "")
				) 
			{

				// logger.info("Pasa a guardar la plantilla>>>>>>>>>>");

				
				if (forma.getEspecialidadParametrizable().equals("")) {
					especialidad = ConstantesBD.codigoEspecialidadMedicaTodos
							+ "";
				} else {
					especialidad = forma.getEspecialidadParametrizable();
				}
			}
//Armando
			consecutivoPlantillas = Plantillas.insertarPlantilla(con,
					Utilidades.convertirAEntero(forma.getPlantillaBase()),
					usuario.getCodigoInstitucion(), centroCosto, sexo, especialidad,
					"", forma.getNombre(), usuario,
					forma.getTipoAtencion(), forma.getTipoFuncionalidad());

				// logger.info("--------->>ZZZZZZZZZZ"+consecutivoPlantillas);

			

			forma.getPlantillaDto().setCodigoPK(consecutivoPlantillas + "");
			logger.info("pasï¿½ por aqui tratando de insertar la plantilla=> "
					+ consecutivoPlantillas);

		} else {
			consecutivoPlantillas = Utilidades.convertirAEntero(forma
					.getPlantillaDto().getCodigoPK());
		}

		if (consecutivoPlantillas > 0) {

			logger.info("secfija>>>>>>>>>>>" + forma.getIndexSeccionFija());
			logger.info("indexelem>>>>>>>>>>>" + forma.getIndexElemento());
			logger.info("indexnivel>>>>>>>>>>>" + forma.getIndexNivel());

			// Inserta los registros
			errores = Plantillas.insertarSeccionesParametrizable(con, forma
					.getPlantillaDto(), forma.getListSeccionTemporal(), forma
					.getIndexSeccionFija(), forma.getIndexElemento(), forma
					.getIndexSeccionNivel2(), forma.getIndexNivel(), usuario,
					true);
			if (!errores.isEmpty()) {
				transaccion = false;
				UtilidadBD.abortarTransaccion(con);
				return errores;
			} else {
				transaccion = true;
				UtilidadBD.finalizarTransaccion(con);
			}
		} else {
			transaccion = false;
			UtilidadBD.abortarTransaccion(con);
		}
		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			forma
					.setListSeccionTemporal(new ArrayList<DtoSeccionParametrizable>());
			metodoCargarPlantilla(con, forma, usuario);

		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));

		}

		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoModificarSecciones(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		ActionErrors errores = new ActionErrors();

		errores = Plantillas.modificaSeccionesParametrizable(con, forma
				.getPlantillaDto(), forma.getListSeccionTemporal(), forma
				.getIndexSeccionFija(), forma.getIndexElemento(), forma
				.getIndexSeccionNivel2(), forma.getIndexNivel(), usuario, true);
		if (!errores.isEmpty()) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
			return errores;
		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);
		}

		return errores;

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoGuardarComponentes(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		int consecutivoComponetes = 0;
		int numRegistros = forma.getComponentesList().size();
		int consecutivoSeccionesFijas = 0;
		int consecutivoSeccionesParam = 0;
		int numReg = forma.getPlantillaDto().getSeccionesFijas().size();
		int orden = numReg;
		ActionErrors errores = new ActionErrors();

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		String centroCosto = "";
		String sexo = "";

		if (forma.getCentroCosto().equals(
				ConstantesBD.codigoCentroCostoTodos + "")) {
			centroCosto = "";
		} else {
			centroCosto = forma.getCentroCosto();
		}
		if (forma.getSexo().equals(ConstantesBD.codigoSexoTodos + "")) {
			sexo = "";
		} else {
			sexo = forma.getSexo();
		}

		int consecutivoPlantillas = 0;

		if (forma.getPlantillaDto().getCodigoPK().equals("")) 
		{

			String especialidad = "";
			if (forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsulta+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+ "")
				|| 
				forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+ "")
				) 
			{

				
				if (forma.getEspecialidadParametrizable().equals("")) {
					especialidad = ConstantesBD.codigoEspecialidadMedicaTodos
							+ "";
				} else {
					especialidad = forma.getEspecialidadParametrizable();
				}
			}
			consecutivoPlantillas = Plantillas.insertarPlantilla(con,
					Utilidades.convertirAEntero(forma.getPlantillaBase()),
					usuario.getCodigoInstitucion(), centroCosto, sexo, especialidad,
					"", forma.getNombre(), usuario, forma.getTipoAtencion(), forma
							.getTipoFuncionalidad());

			

		} else {
			consecutivoPlantillas = Utilidades.convertirAEntero(forma
					.getPlantillaDto().getCodigoPK());
		}

		if (forma.getIndexNivel().equals("fija")) {

			for (int i = 0; i < numReg; i++) {
				// Valida si la secciï¿½n fija se encuentra relacionada con la
				// plantilla, si no la relaciona creando el registro
				if (!forma.getPlantillaDto().getSeccionesFijasPos(i)
						.getCodigoPK().equals("")) {
					Plantillas.actualizarPlantillaSeccionesFijas(con, forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.getCodigoPK(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i).getOrden(), forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.isVisible(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i)
							.getCodigoPkFunParamSecFij()
							+ "", forma.getPlantillaDto().getSeccionesFijasPos(
							i).getCodigoSeccionParam()
							+ "", usuario);
				} else {
					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(
									con,
									consecutivoPlantillas + "",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.getCodigoPkFunParamSecFij(),
									"",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i).getOrden(),
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.isVisible(), usuario);

					if (consecutivoSeccionesFijas != ConstantesBD.codigoNuncaValido)
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.setCodigoPK(consecutivoSeccionesFijas + "");
					else {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
						transaccion = false;
						UtilidadBD.abortarTransaccion(con);
						return errores;
					}
				}
			}

			for (int i = 0; i < numRegistros; i++) {

				consecutivoSeccionesParam = Plantillas
						.insertarSeccionesParametrizablesConCodigo(con, "", "",
								(orden + (forma.getComponentesList().get(i)
										.getOrden()))
										+ "", 1 + "", "", "", "",
								ConstantesBD.acronimoNo,
								ConstantesIntegridadDominio.acronimoSeccion,
								"", true, true, usuario);

				if (consecutivoSeccionesParam > 0) {

					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(con,
									consecutivoPlantillas + "", "",
									consecutivoSeccionesParam + "", orden
											+ (forma.getComponentesList()
													.get(i).getOrden()), true,
									usuario);

				} else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}

				if (consecutivoSeccionesFijas > 0) {
					consecutivoComponetes = Plantillas
							.insertarComponenteParametrizable(con,
									consecutivoSeccionesFijas + "", forma
											.getComponentesList().get(i)
											.getCodigoPK(), orden
											+ (forma.getComponentesList()
													.get(i).getOrden()) + "",
									ConstantesBD.acronimoSi,
									ConstantesBD.acronimoSi);
				}

				else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}

			}
		}

		if (forma.getIndexNivel().equals("1")) {
			forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()));

			for (int i = 0; i < numReg; i++) {
				// Valida si la secciï¿½n fija se encuentra relacionada con la
				// plantilla, si no la relaciona creando el registro
				if (!forma.getPlantillaDto().getSeccionesFijasPos(i)
						.getCodigoPK().equals("")) {
					Plantillas.actualizarPlantillaSeccionesFijas(con, forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.getCodigoPK(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i).getOrden(), forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.isVisible(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i)
							.getCodigoPkFunParamSecFij()
							+ "", forma.getPlantillaDto().getSeccionesFijasPos(
							i).getCodigoSeccionParam()
							+ "", usuario);
				} else {
					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(
									con,
									consecutivoPlantillas + "",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.getCodigoPkFunParamSecFij(),
									"",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i).getOrden(),
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.isVisible(), usuario);

					if (consecutivoSeccionesFijas != ConstantesBD.codigoNuncaValido)
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.setCodigoPK(consecutivoSeccionesFijas + "");
					else {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
						transaccion = false;
						UtilidadBD.abortarTransaccion(con);
						return errores;
					}
				}
			}

			for (int i = 0; i < numRegistros; i++) {

				consecutivoComponetes = Plantillas
						.insertarComponenteParametrizable(con, forma
								.getPlantillaDto().getSeccionesFijasPos(
										Integer.parseInt(forma
												.getIndexSeccionFija()))
								.getCodigoPK(), forma.getComponentesList().get(
								i).getCodigoPK(),
								orden
										+ (forma.getComponentesList().get(i)
												.getOrden()) + "",
								ConstantesBD.acronimoSi,
								ConstantesBD.acronimoSi);

				if (consecutivoComponetes < 0) {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}
			}

		}
		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			forma.setComponentesList(new ArrayList<DtoComponente>());
			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);

		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}

		return errores;

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoGuardarEscalas(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		int consecutivoEscalas = 0;
		int consecutivoSeccionesFijas = 0;
		int consecutivoSeccionesParam = 0;
		int numRegistros = forma.getEscalasList().size();
		int numReg = forma.getPlantillaDto().getSeccionesFijas().size();
		int orden = numReg;
		ActionErrors errores = new ActionErrors();

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		String centroCosto = "";
		String sexo = "";

		if (forma.getCentroCosto().equals(
				ConstantesBD.codigoCentroCostoTodos + "")) {
			centroCosto = "";
		} else {
			centroCosto = forma.getCentroCosto();
		}
		if (forma.getSexo().equals(ConstantesBD.codigoSexoTodos + "")) {
			sexo = "";
		} else {
			sexo = forma.getSexo();
		}

		int consecutivoPlantillas = 0;

		// logger.info("MIRAMOS COMO VIENE LA PLANTILLA"+forma.getPlantillaDto().getCodigoPK());

		if (forma.getPlantillaDto().getCodigoPK().equals("")) 
		{

			

				String especialidad = "";

				
				if(
					forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsulta+ "")
					|| 
					forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta+ "")
					|| 
					forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+ "")
					|| 
					forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+ "")
					)
				{
				
					// logger.info("VIENE POR ESPECIALIDAD >>>>>>>>>>>"+forma.getEspecialidadParametrizable());
	
					if (forma.getEspecialidadParametrizable().equals("")) {
						especialidad = ConstantesBD.codigoEspecialidadMedicaTodos
								+ "";
						;
					} else {
	
						// logger.info("DA VALOR A LA ESPECIALIDAD >>>>>>>>>>>>>>>");
						especialidad = forma.getEspecialidadParametrizable();
					}
				}

				consecutivoPlantillas = Plantillas.insertarPlantilla(con,
						Utilidades.convertirAEntero(forma.getPlantillaBase()),
						usuario.getCodigoInstitucion(), centroCosto, sexo, especialidad,
						"", forma.getNombre(), usuario, forma.getTipoAtencion(), forma
								.getTipoFuncionalidad());

			

		} 
		else 
		{
			consecutivoPlantillas = Utilidades.convertirAEntero(forma
					.getPlantillaDto().getCodigoPK());
		}
		if (forma.getIndexNivel().equals("fija")) {

			// logger.info("ENTRA A INSERTAR EN UNA SECCION FIJA 1 >>>>>>>>");

			for (int i = 0; i < numReg; i++) {
				// Valida si la secciï¿½n fija se encuentra relacionada con la
				// plantilla, si no la relaciona creando el registro
				if (!forma.getPlantillaDto().getSeccionesFijasPos(i)
						.getCodigoPK().equals("")) {
					Plantillas.actualizarPlantillaSeccionesFijas(con, forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.getCodigoPK(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i).getOrden(), forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.isVisible(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i)
							.getCodigoPkFunParamSecFij()
							+ "", forma.getPlantillaDto().getSeccionesFijasPos(
							i).getCodigoSeccionParam()
							+ "", usuario);
				} else {
					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(
									con,
									consecutivoPlantillas + "",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.getCodigoPkFunParamSecFij(),
									"",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i).getOrden(),
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.isVisible(), usuario);

					if (consecutivoSeccionesFijas != ConstantesBD.codigoNuncaValido)
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.setCodigoPK(consecutivoSeccionesFijas + "");
					else {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
						transaccion = false;
						UtilidadBD.abortarTransaccion(con);
						return errores;
					}
				}
			}

			for (int i = 0; i < numRegistros; i++) {

				logger.info("ENTRA A INSERTAR EN UNA SECCION FIJA 2 >>>>>>>>");

				consecutivoSeccionesParam = Plantillas
						.insertarSeccionesParametrizablesConCodigo(con, "", "",
								(orden + (forma.getEscalasList().get(i)
										.getOrden()))
										+ "", 1 + "", "", "", "",
								ConstantesBD.acronimoNo,
								ConstantesIntegridadDominio.acronimoSeccion,
								"", true, true, usuario);

				if (consecutivoSeccionesParam > 0) {

					logger
							.info("ENTRA A INSERTAR EN UNA SECCION FIJA 3 >>>>>>>>"
									+ consecutivoSeccionesParam);

					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(con,
									consecutivoPlantillas + "", "",
									consecutivoSeccionesParam + "", orden
											+ (forma.getEscalasList().get(i)
													.getOrden()), true, usuario);

				} else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}

				if (consecutivoSeccionesFijas > 0) {
					consecutivoEscalas = Plantillas
							.insertarEscalaParametrizable(con,
									consecutivoSeccionesFijas + "", forma
											.getEscalasList().get(i)
											.getCodigoPK(), orden
											+ (forma.getEscalasList().get(i)
													.getOrden()) + "",
									ConstantesBD.acronimoSi,
									ConstantesBD.acronimoSi,
									forma.getEscalasList().get(i).getRequerida()
									);
				}

				else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}

			}
		}

		// logger.info("indexNivel>>>>>>>>>>"+forma.getIndexNivel());
		if (forma.getIndexNivel().equals("1")) {
			forma.getPlantillaDto().getSeccionesFijasPos(
					Integer.parseInt(forma.getIndexSeccionFija()));

			for (int i = 0; i < numReg; i++) {
				// Valida si la secciï¿½n fija se encuentra relacionada con la
				// plantilla, si no la relaciona creando el registro
				if (!forma.getPlantillaDto().getSeccionesFijasPos(i)
						.getCodigoPK().equals("")) {
					Plantillas.actualizarPlantillaSeccionesFijas(con, forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.getCodigoPK(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i).getOrden(), forma
							.getPlantillaDto().getSeccionesFijasPos(i)
							.isVisible(), forma.getPlantillaDto()
							.getSeccionesFijasPos(i)
							.getCodigoPkFunParamSecFij()
							+ "", forma.getPlantillaDto().getSeccionesFijasPos(
							i).getCodigoSeccionParam()
							+ "", usuario);
				} else {
					consecutivoSeccionesFijas = Plantillas
							.insertarPlantillasSeccionesFijas(
									con,
									consecutivoPlantillas + "",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.getCodigoPkFunParamSecFij(),
									"",
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i).getOrden(),
									forma.getPlantillaDto()
											.getSeccionesFijasPos(i)
											.isVisible(), usuario);

					if (consecutivoSeccionesFijas != ConstantesBD.codigoNuncaValido)
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.setCodigoPK(consecutivoSeccionesFijas + "");
					else {
						errores
								.add(
										"descripcion",
										new ActionMessage(
												"errors.notEspecific",
												"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
						transaccion = false;
						UtilidadBD.abortarTransaccion(con);
						return errores;
					}
				}
			}

			for (int i = 0; i < numRegistros; i++) {

				consecutivoEscalas = Plantillas.insertarEscalaParametrizable(
						con, forma.getPlantillaDto().getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
								.getCodigoPK(), forma.getEscalasList().get(i)
								.getCodigoPK(), orden
								+ (forma.getEscalasList().get(i).getOrden())
								+ "", ConstantesBD.acronimoSi,
								ConstantesBD.acronimoSi,
								forma.getEscalasList().get(i).getRequerida());

				if (consecutivoEscalas < 0) {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}
			}

		}

		// logger.info("transaccion>>>>>>>>>>>>>>>>"+transaccion);

		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			forma.setEscalasList(new ArrayList<DtoEscala>());
			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);

		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		return errores;
	}

	/**
	 * 
	 * @param forma
	 */
	private void metodoCrearNuevaSeccion(ParametrizacionPlantillasForm forma) {
		// Reinicia el DtoElemento temporal
		DtoSeccionParametrizable dtoSeccionParam = new DtoSeccionParametrizable();
		dtoSeccionParam.setDescripcion("");
		dtoSeccionParam.setCodigo("");
		dtoSeccionParam.setColumnasSeccion(1);
		dtoSeccionParam.setVisible(true);

		// Captura el orden, lo ubica en la ultima posiciï¿½n
		int numRegistros = forma.getListSeccionTemporal().size();
		dtoSeccionParam.setOrden(numRegistros + 1);

		forma.getListSeccionTemporal().add(dtoSeccionParam);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private ActionErrors metodoModificarPlantillasSecFijasEspecialidad(
			Connection con, ParametrizacionPlantillasForm forma,
			UsuarioBasico usuario) throws NumberFormatException, SQLException {
		// Recorre el array de secciones fijas y evalua si encuentran
		// parametrizadas o no
		int numRegistros = forma.getPlantillaDto().getSeccionesFijas().size();
		ActionErrors errores = new ActionErrors();
		// forma.setMostrarMensaje(new ResultadoBoolean(false,""));

		UtilidadBD.iniciarTransaccion(con);

		String especialidadConsulta = "";

		if (forma.getEspecialidadParametrizable().equals("")) 
			especialidadConsulta=forma.getPlantillaDto().getCodigoEspecialidad()+"";
		else 
			especialidadConsulta = forma.getEspecialidadParametrizable();

		// Campos agregados a las funciones y por anexo 843, auqnue el campo
		// nombre ya existia no se usaba en ninguna otra plantilla
		
		//Se setean los elementos de la plantilla dependiendo si éstos vana  ser uasdos para modifcar o insertar la plantilla
		String tipoAtencion="";
		String tipoFuncionalidad="";
		String nombre="";
		
		if (forma.getTipoAtencion().equals(""))
			tipoAtencion=forma.getPlantillaDto().getTipoAtencion();
		else
			tipoAtencion=forma.getTipoAtencion();
		
		if (forma.getTipoFuncionalidad().equals(""))
			tipoFuncionalidad=forma.getPlantillaDto().getFuncionalidad().getDescripcion()+"";
		else
			tipoFuncionalidad=forma.getTipoFuncionalidad();
		
		if (forma.getNombre().equals(""))
			nombre = forma.getPlantillaDto().getNombre();
		else
			nombre=forma.getNombre();
		
		//Validación agregada el 5 de Enero de 2010 que faltaba  por realizar para insertar/modificar

		int consecutivoPlantillas = 0;
		if (forma.getPlantillaDto().getCodigoPK().equals("")) 
		{

			// logger.info("Paso Insertar"+forma.getPlantillaDto().getCodigoPK());
			//Si no se pone nada en la especialidad aplica para todos
			if (forma.getEspecialidadParametrizable().equals("")) 
				especialidadConsulta = ConstantesBD.codigoEspecialidadMedicaTodos+ "";
			
			
			consecutivoPlantillas = Plantillas.insertarPlantilla(con,
					Utilidades.convertirAEntero(forma.getPlantillaBase()),
					usuario.getCodigoInstitucion(), "", "",
					especialidadConsulta, "", nombre, usuario, tipoAtencion,
					forma.getTipoFuncionalidad());
			
			//Si la plantilla es de valoracion o evolucion odontologica, la cargo
			logger.info("LA FUNC PARAM--->"+forma.getPlantillaBase()+"COD DE LA PLANTILLA--->"+consecutivoPlantillas);
			if (Utilidades.convertirAEntero(forma.getPlantillaBase())==ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia
				||
				Utilidades.convertirAEntero(forma.getPlantillaBase())==ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica)
			{
				 forma.setCodigoPkPlantilla(consecutivoPlantillas);
				 forma.setProcesoASeguirOdontologia(ConstantesBD.acronimoSi);
				 metodoCargarPlantilla(con, forma, usuario);
			}
			
			
		} else {

			// logger.info("Paso Modificar"+forma.getPlantillaDto().getCodigoPK());

			consecutivoPlantillas = Utilidades.convertirAEntero(forma
					.getPlantillaDto().getCodigoPK());
			Plantillas.actualizarPlantilla(con, Utilidades
					.convertirAEntero(forma.getPlantillaDto().getCodigoPK()),
					"", "", especialidadConsulta, "", nombre, usuario,
					ConstantesBD.acronimoSi, tipoAtencion, tipoFuncionalidad);
		}

		for (int i = 0; i < numRegistros; i++) {
			// Valida si la secciï¿½n fija se encuentra relacionada con la
			// plantilla, si no la relaciona creando el registro
			if (!forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPK()
					.equals("")) {
				Plantillas.actualizarPlantillaSeccionesFijas(con, forma
						.getPlantillaDto().getSeccionesFijasPos(i)
						.getCodigoPK(), forma.getPlantillaDto()
						.getSeccionesFijasPos(i).getOrden(), forma
						.getPlantillaDto().getSeccionesFijasPos(i).isVisible(),
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.getCodigoPkFunParamSecFij()
								+ "", forma.getPlantillaDto()
								.getSeccionesFijasPos(i)
								.getCodigoSeccionParam()
								+ "", usuario);
			} else {
				int consecutivo = Plantillas.insertarPlantillasSeccionesFijas(
						con, consecutivoPlantillas + "", forma
								.getPlantillaDto().getSeccionesFijasPos(i)
								.getCodigoPkFunParamSecFij(), "", forma
								.getPlantillaDto().getSeccionesFijasPos(i)
								.getOrden(), forma.getPlantillaDto()
								.getSeccionesFijasPos(i).isVisible(), usuario);

				if (consecutivo != ConstantesBD.codigoNuncaValido)
					forma.getPlantillaDto().getSeccionesFijasPos(i)
							.setCodigoPK(consecutivo + "");
				else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}
			}
		}
		
		if ( Integer.valueOf(forma.getPlantillaBase()) == ConstantesCamposParametrizables.funcParametrizableValoracionConsulta
				||  Integer.valueOf(forma.getPlantillaBase()) == ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias) {
			metodoModificarPlantillasSecFijasSinOrden(con, forma, usuario, Integer.valueOf(forma.getCodigoPkPlantilla()));
		}
		UtilidadBD.finalizarTransaccion(con);
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private ActionErrors metodoModificarPlantillasSecFijas(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) throws NumberFormatException, SQLException {
		// Recorre el array de secciones fijas y evalua si encuentran
		// parametrizadas o no
		int numRegistros = forma.getPlantillaDto().getSeccionesFijas().size();
		ActionErrors errores = new ActionErrors();
		// forma.setMostrarMensaje(new ResultadoBoolean(false,""));

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		String centroCosto = "";
		String sexo = "";

		if (forma.getCentroCosto().equals(
				ConstantesBD.codigoCentroCostoTodos + "")) {
			centroCosto = "";
		} else {
			centroCosto = forma.getCentroCosto();
		}
		if (forma.getSexo().equals(ConstantesBD.codigoSexoTodos + "")) {
			sexo = "";
		} else {
			sexo = forma.getSexo();
		}

		int consecutivoPlantillas = 0;
		if (forma.getPlantillaDto().getCodigoPK().equals("")) {

			// logger.info("Paso Insertar"+forma.getPlantillaDto().getCodigoPK());

			consecutivoPlantillas = Plantillas.insertarPlantilla(con,
					Utilidades.convertirAEntero(forma.getPlantillaBase()),
					usuario.getCodigoInstitucion(), centroCosto, sexo, "", "",
					"", usuario, forma.getTipoAtencion(), forma
							.getTipoFuncionalidad());
			if (consecutivoPlantillas < 0)
				transaccion = false;
		} else {

			// logger.info("Paso Modificar"+forma.getPlantillaDto().getCodigoPK());

			consecutivoPlantillas = Utilidades.convertirAEntero(forma
					.getPlantillaDto().getCodigoPK());
			transaccion = Plantillas.actualizarPlantilla(con, Utilidades
					.convertirAEntero(forma.getPlantillaDto().getCodigoPK()),
					centroCosto, sexo, "", "", "", usuario,
					ConstantesBD.acronimoSi, forma.getTipoAtencion(), forma
							.getTipoFuncionalidad());
		}

		for (int i = 0; i < numRegistros; i++) {
			// Valida si la secciï¿½n fija se encuentra relacionada con la
			// plantilla, si no la relaciona creando el registro
			if (!forma.getPlantillaDto().getSeccionesFijasPos(i).getCodigoPK()
					.equals("")) {
				transaccion = Plantillas.actualizarPlantillaSeccionesFijas(con,
						forma.getPlantillaDto().getSeccionesFijasPos(i)
								.getCodigoPK(), forma.getPlantillaDto()
								.getSeccionesFijasPos(i).getOrden(), forma
								.getPlantillaDto().getSeccionesFijasPos(i)
								.isVisible(), forma.getPlantillaDto()
								.getSeccionesFijasPos(i)
								.getCodigoPkFunParamSecFij()
								+ "", forma.getPlantillaDto()
								.getSeccionesFijasPos(i)
								.getCodigoSeccionParam()
								+ "", usuario);
			} else {
				int consecutivo = Plantillas.insertarPlantillasSeccionesFijas(
						con, consecutivoPlantillas + "", forma
								.getPlantillaDto().getSeccionesFijasPos(i)
								.getCodigoPkFunParamSecFij(), "", forma
								.getPlantillaDto().getSeccionesFijasPos(i)
								.getOrden(), forma.getPlantillaDto()
								.getSeccionesFijasPos(i).isVisible(), usuario);

				if (consecutivo != ConstantesBD.codigoNuncaValido)
					forma.getPlantillaDto().getSeccionesFijasPos(i)
							.setCodigoPK(consecutivo + "");
				else {
					errores
							.add(
									"descripcion",
									new ActionMessage("errors.notEspecific",
											"El Consecutivo de la plantilla Secciones Fijas no es Valido."));
					transaccion = false;
					return errores;
				}
			}
		}
		
		
		
		if ( Integer.valueOf(forma.getPlantillaBase()) == ConstantesCamposParametrizables.funcParametrizableValoracionConsulta
				||  Integer.valueOf(forma.getPlantillaBase()) == ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias) {
			metodoModificarPlantillasSecFijasSinOrden(con, forma, usuario, Integer.valueOf(forma.getCodigoPkPlantilla()));
		}
		

		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
		

		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors metodoModificarSecFijas(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		// Recorre el array de secciones fijas y evalua si encuentran
		// parametrizadas o no
		ActionErrors errores = new ActionErrors();

		DtoElementoParam elemento;

		boolean transaccion = UtilidadBD.iniciarTransaccion(con);

		// logger.info(">>>>>>>>>>>>>"+forma.getPlantillaDto().getSeccionesFijasPos(Utilidades.convertirAEntero(forma.getIndexSeccionFija())).getElementos().size());

		// Recorre los elementos de Primer Nivel para la secciï¿½n
		for (int j = 0; j < forma.getPlantillaDto().getSeccionesFijasPos(
				Utilidades.convertirAEntero(forma.getIndexSeccionFija()))
				.getElementos().size(); j++) {
			elemento = forma.getPlantillaDto().getSeccionesFijasPos(
					Utilidades.convertirAEntero(forma.getIndexSeccionFija()))
					.getElementosPos(j);

			// logger.info("elemento>>>>>>>>>"+elemento);

			// logger.info("isseccion>>>>>>>>>"+elemento.isSeccion());

			// logger.info("MM>>>>>>>>>"+elemento.isMostrarModificacion());

			// logger.info("Orden>>>>>>>>>"+elemento.getOrden());

			// logger.info("PK>>>>>>>>>"+elemento.getCodigoPK());

			// logger.info("Consecutivo Parametrizacion >>>>>>>>>"+elemento.getConsecutivoParametrizacion());

			// Valida que el elemento sea de tipo Secciï¿½n
			if (elemento.isSeccion()) {
				transaccion = Plantillas.actualizarMostrarModOrdenSeccionParam(
						con, elemento.isMostrarModificacion(), elemento
								.getOrden()
								+ "", elemento.getCodigoPK(), usuario);
			} else if (elemento.isEscala()) {
				transaccion = Plantillas
						.actualizarEscalaParametrizable(
								con,
								elemento.getConsecutivoParametrizacion(),
								elemento.getOrden() + "",
								elemento.isVisible() ? ConstantesBD.acronimoSi
										: ConstantesBD.acronimoNo,
								elemento.isMostrarModificacion() ? ConstantesBD.acronimoSi
										: ConstantesBD.acronimoNo);

			} else if (elemento.isComponente()) {
				transaccion = Plantillas
						.actualizarComponenteParametrizable(
								con,
								elemento.getConsecutivoParametrizacion(),
								elemento.getCodigoPK(),
								elemento.getOrden() + "",
								elemento.isVisible() ? ConstantesBD.acronimoSi
										: ConstantesBD.acronimoNo,
								elemento.isMostrarModificacion() ? ConstantesBD.acronimoSi
										: ConstantesBD.acronimoNo);
			}

		}

		if (transaccion) {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		} else {
			forma.setMostrarMensaje(new ResultadoBoolean(true,
					"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}

		return errores;
	}

	/**
	 * 
	 * @param forma
	 */
	private void nuevaOpcion(ParametrizacionPlantillasForm forma) {

		DtoOpcionCampoParam opcion = new DtoOpcionCampoParam();
		opcion.setCodigoPk("");
		opcion.setOpcion("");
		opcion.setValor("");
		// almancena el dto en el listado
		logger.info("en la lista: "
				+ forma.getListCampoTemporal().get(
						Utilidades.convertirAEntero(forma.getIndexCampo()))
						.getManejaImagen());

		forma.getListCampoTemporal().get(
				Integer.parseInt(forma.getIndexCampo())).getOpciones().add(
				opcion);

	}

	/**
	 * 
	 * @param forma
	 */
	private void nuevoComponente(ParametrizacionPlantillasForm forma) {
		DtoComponente dtoComponente = new DtoComponente();
		dtoComponente.setOrden(forma.getComponentesList().size() + 1);
		forma.getComponentesList().add(dtoComponente);
	}

	/**
	 * 
	 * @param forma
	 */
	private void nuevaEscala(ParametrizacionPlantillasForm forma) {
		DtoEscala dtoEscala = new DtoEscala();
		dtoEscala.setOrden((forma.getEscalasList().size() + 1));
		forma.getEscalasList().add(dtoEscala);
	}

	
	
	 
	 private void metodoModificarPlantillasSecFijasSinOrden(
			 Connection con,
			 ParametrizacionPlantillasForm forma,
			 UsuarioBasico usuario,Integer codigoPlantilla ) throws SQLException{

		 Integer indexMapa= new Integer(0);
		 Boolean exsitePlantilla = new Boolean(false);

		 int plantilla = Utilidades.convertirAEntero(forma.getPlantillaBase());
		 Integer sexoPlantilla = new Integer(0);
		 Integer centroCostoPlantilla= new Integer(0);
		 Integer especialidad = new Integer(0);

		 if(!UtilidadTexto.isEmpty(forma.getSexo())){
			 sexoPlantilla= Integer.valueOf(forma.getSexo());
		 }
		 if(!UtilidadTexto.isEmpty(forma.getCentroCosto()))
		 {
			 centroCostoPlantilla= Integer.valueOf(forma.getCentroCosto());
		 }

		 if(!UtilidadTexto.isEmpty(forma.getEspecialidadParametrizable())){
			 especialidad=Integer.valueOf(forma.getEspecialidadParametrizable());
		 }


		 if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionConsulta  ) {	

			 exsitePlantilla=	Plantillas.existePlantillaFijaSinOrdenConsultaExterna(con, forma.getCodigoPkPlantilla(),especialidad);

			 if(!exsitePlantilla){
				 Plantillas.guardarPlantillaSinOrdenConsultaExterna(con, codigoPlantilla,
						 forma.getCheckLinkOrdenesAmbulatorias(),
						 IConstanteSeccionsFijasSinOrden.SECCION_ENLACES_ORDENES_AMBULATORIAS,especialidad);
			 }else{
				 Plantillas.actualziarPlantillaFijaSinOrdenConsultaExterna(con, codigoPlantilla, forma.getCheckLinkOrdenesAmbulatorias(),especialidad);
			 }
			 
		 }else if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias)
		 {
//			 
//			 if(centroCostoPlantilla==0){
//				 centroCostoPlantilla=-1;
//			 }
			 
			 exsitePlantilla= Plantillas.existePlantillaFijaSinOrdenUrgencias(con, forma.getCodigoPkPlantilla(),sexoPlantilla,centroCostoPlantilla,ConstantesCamposParametrizables.formatoUrgencias);
			 if(!exsitePlantilla){
				 Plantillas.guardarPlantillaSinOrden(con, codigoPlantilla,
						 forma.getCheckLinkOrdenesAmbulatorias(),
						 IConstanteSeccionsFijasSinOrden.SECCION_ENLACES_ORDENES_AMBULATORIAS,Integer.valueOf(forma.getSexo()),  Integer.valueOf(forma.getCentroCosto()),ConstantesCamposParametrizables.formatoUrgencias);
			 }else{
				 Plantillas.actualziarPlantillaFijaSinOrden(con, codigoPlantilla, forma.getCheckLinkOrdenesAmbulatorias(),Integer.valueOf(forma.getSexo()),  Integer.valueOf(forma.getCentroCosto()),ConstantesCamposParametrizables.formatoUrgencias);
			 }
		 }



		



	 }
	 
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoCargarPlantilla(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {

		int plantilla = Utilidades.convertirAEntero(forma.getPlantillaBase());
		
		Integer sexoPlantilla = new Integer(0);
		Integer centroCostoPlantilla= new Integer(0);
		Integer especiaidad = new Integer(0);

		if(!UtilidadTexto.isEmpty(forma.getSexo())){
			sexoPlantilla= Integer.valueOf(forma.getSexo());
		}
		if(!UtilidadTexto.isEmpty(forma.getCentroCosto()))
		{
			centroCostoPlantilla= Integer.valueOf(forma.getCentroCosto());
		}
		
		if(!UtilidadTexto.isEmpty(forma.getEspecialidadParametrizable())){
			especiaidad=Integer.valueOf(forma.getEspecialidadParametrizable());
		}

		try {

			if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionConsulta  ) {	

				forma.setCheckLinkOrdenesAmbulatorias(Plantillas.consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(con, forma.getCodigoPkPlantilla(),especiaidad));

			}else if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias)
			{
				forma.setCheckLinkOrdenesAmbulatorias(Plantillas.consultarVisibilidadPlantillaFijaSinOrdenUrgencias(con, forma.getCodigoPkPlantilla(),sexoPlantilla,centroCostoPlantilla,ConstantesCamposParametrizables.formatoUrgencias));
			}


			if (plantilla == ConstantesCamposParametrizables.funcParametrizableEvolucion
					|| plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion
					|| plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias) {

				// logger.info("plantilla>>>>>>>"+forma.getPlantillaBase());
				// logger.info("centroCosto>>>>>>>"+forma.getCentroCosto());
				// logger.info("sexo>>>>>>>"+forma.getSexo());

				// logger.info("especialidad>>>>>>>"+forma.getEspecialidadParametrizable());

				int centroCosto = ConstantesBD.codigoNuncaValido;
				int sexo = ConstantesBD.codigoNuncaValido;

				if (forma.getCentroCosto().equals("")) {
					centroCosto = ConstantesBD.codigoCentroCostoTodos;
				} else {
					centroCosto = Utilidades.convertirAEntero(forma
							.getCentroCosto());
				}
				if (forma.getSexo().equals("")) {
					sexo = ConstantesBD.codigoSexoTodos;
				} else {
					sexo = Utilidades.convertirAEntero(forma.getSexo());
				}

				// logger.info("centroCostoDes>>>>>>>"+centroCosto);
				// logger.info("sexoDes>>>>>>>"+sexo);

				// Almacena la información de la plantilla parametrica
				forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
						ConstantesBD.codigoNuncaValido, usuario
						.getCodigoInstitucionInt(), Utilidades
						.convertirAEntero(forma.getPlantillaBase()),
						centroCosto, sexo, ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false, "", "", ""));

				forma.setMostrarElementosVista(ConstantesBD.acronimoSi);

			}
			if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionConsulta
					|| plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta) {

				int especialidad = ConstantesBD.codigoNuncaValido;

				if (forma.getEspecialidadParametrizable().equals("")) {
					especialidad = ConstantesBD.codigoEspecialidadMedicaTodos;
				} else {
					especialidad = Utilidades.convertirAEntero(forma
							.getEspecialidadParametrizable());
				}

				forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
						ConstantesBD.codigoNuncaValido, usuario
						.getCodigoInstitucionInt(), Utilidades
						.convertirAEntero(forma.getPlantillaBase()),
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, especialidad,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false, "", "", ""));

				forma.setMostrarElementosVista(ConstantesBD.acronimoSi);

			}

			// -******************Cambio Anexo 843
			if (plantilla == ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia) {
				int especialidad = Utilidades.convertirAEntero(forma
						.getEspecialidadParametrizable());

				String tipoAtencion = forma.getTipoAtencion();

				String nombre = forma.getNombre();

				int codigoPkPlantilla = forma.getCodigoPkPlantilla();

				forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
						codigoPkPlantilla, usuario.getCodigoInstitucionInt(),
						Utilidades.convertirAEntero(forma.getPlantillaBase()),
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, especialidad,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false, tipoAtencion,
						nombre, ""));

				forma.setMostrarElementosVista(ConstantesBD.acronimoSi);
			}

			// -******************Cambio Anexo 877
			if (plantilla == ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica) {
				int especialidad = Utilidades.convertirAEntero(forma
						.getEspecialidadParametrizable());

				String tipoAtencion = forma.getTipoAtencion();

				String nombre = forma.getNombre();

				int codigoPkPlantilla = forma.getCodigoPkPlantilla();

				forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
						codigoPkPlantilla, usuario.getCodigoInstitucionInt(),
						Utilidades.convertirAEntero(forma.getPlantillaBase()),
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, especialidad,
						ConstantesBD.codigoNuncaValido,
						ConstantesBD.codigoNuncaValido, false, tipoAtencion,
						nombre, ""));

				forma.setMostrarElementosVista(ConstantesBD.acronimoSi);
			}


			// ***Fin Cambio Anexo 860
			if (plantilla == ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico) 
			{
				String tipoFuncionalidad = forma.getTipoFuncionalidad();

				if(!tipoFuncionalidad.equals(""))
				{
					Log4JManager.info("Entro por INFO paciente >> "+forma.getTipoFuncionalidad());

					forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica
							(
									con,
									ConstantesBD.codigoNuncaValido, 
									usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getPlantillaBase()),
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido, 
									false,
									"",
									"",
									tipoFuncionalidad
							));

					Log4JManager.info("CodigoPk Plantilla >> "+forma.getPlantillaDto().getCodigoPK());
					Log4JManager.info("Codigo Plantilla >> "+forma.getPlantillaDto().getCodigo());

				}
				else{
					forma.resetPlantillaDto();
				}
			}

		} catch (SQLException e) {
			logger.error("error consultando la visibilidad del campo ordenes ambulatorias");
		}

	}

	
	
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * 
	 * Las siguientes validaciones dependen si se va a crear una
	 * plantilla nueva de odontología o si se va a modificar una ya
	 * existente. Cuando es No, se hace que la consulta no saque
	 * ningún resultado para poder generar una nueva plantilla
	 */
	private void cargarPlantillaNuevaOdon(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		forma.setMostrarElementosVista(ConstantesBD.acronimoSi);
		forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
				ConstantesBD.codigoNuncaValido, usuario
						.getCodigoInstitucionInt(), Utilidades
						.convertirAEntero(forma.getPlantillaBase()),
				ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido, false,
				ConstantesBD.codigoNuncaValido + "",
				ConstantesBD.codigoNuncaValido + "", ""));

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void metodoCargarPlantillaEspecialidad(Connection con,
			ParametrizacionPlantillasForm forma, UsuarioBasico usuario) {
		// Almacena la informaciï¿½n de la plantilla parametrica
		forma.setPlantillaDto(Plantillas.cargarPlantillaParametrica(con,
				Utilidades.convertirAEntero(forma.getIndexPlantilla()), usuario
						.getCodigoInstitucionInt(), Utilidades
						.convertirAEntero(forma.getPlantillaBase()),
				ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
				Utilidades.convertirAEntero(forma.getIndexEspecialidad()),
				ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
				false, "", "", ""));
	}

	/**
	 * Elimina una Escala
	 * 
	 * @param Connection
	 *            con
	 * @param ParametrizacionPlantillasForm
	 *            forma
	 * @param boolean esNueva
	 * @param UsuarioBasico
	 *            usuario
	 * */
	private ActionErrors metodoEliminarEscala(Connection con,
			ParametrizacionPlantillasForm forma, boolean esNueva,
			UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String codigoPkSeccionParam = "";

		if (!esNueva) {
			DtoEscala escala = new DtoEscala();

			// Valida el nivel
			if (forma.getIndexNivel().equals("fija")) {
				escala = (DtoEscala) forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getElementosPos(
								Integer.parseInt(forma.getIndexElemento()));
				codigoPkSeccionParam = forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getCodigoSeccionParam();
			} else if (forma.getIndexNivel().equals("1")) {
				escala = (DtoEscala) forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getElementosPos(
								Integer.parseInt(forma.getIndexElemento()));
				// Para este nivel no se crea una seccion parametrizable para la
				// escala
				codigoPkSeccionParam = "";
			}

			UtilidadBD.iniciarTransaccion(con);

			if (!Plantillas.actualizarEscalaParametrizable(con, escala
					.getConsecutivoParametrizacion()
					+ "", escala.getOrden() + "", ConstantesBD.acronimoSi,
					ConstantesBD.acronimoNo)) {
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion", new ActionMessage(
						"errors.notEspecific", "La Escala No fue Eliminada."));
				return errores;
			} else if (!codigoPkSeccionParam.equals("")
					&& !Plantillas.actualizarMostrarModOrdenSeccionParam(con,
							false, forma.getPlantillaDto()
									.getSeccionesFijasPos(
											Integer.parseInt(forma
													.getIndexSeccionFija()))
									.getOrden()
									+ "", codigoPkSeccionParam + "", usuario)) {
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion", new ActionMessage(
						"errors.notEspecific",
						"La Seccion de la Escala No fue Eliminada."));
				return errores;
			}

			// Recarga el Dto de Plantilla
			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);
		}

		return errores;
	}

	/**
	 * Elimina un Componente
	 * 
	 * @param Connection
	 *            con
	 * @param ParametrizacionPlantillasForm
	 *            forma
	 * @param boolean esNueva
	 * @param UsuarioBasico
	 *            usuario
	 * */
	private ActionErrors metodoEliminarComponente(Connection con,
			ParametrizacionPlantillasForm forma, boolean esNueva,
			UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String codigoPkSeccionParam = "";

		if (!esNueva) {
			DtoComponente componente = new DtoComponente();

			// Valida el nivel
			if (forma.getIndexNivel().equals("fija")) {
				componente = (DtoComponente) forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getElementosPos(
								Integer.parseInt(forma.getIndexElemento()));
				codigoPkSeccionParam = forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getCodigoSeccionParam();
			} else if (forma.getIndexNivel().equals("1")) {
				componente = (DtoComponente) forma.getPlantillaDto()
						.getSeccionesFijasPos(
								Integer.parseInt(forma.getIndexSeccionFija()))
						.getElementosPos(
								Integer.parseInt(forma.getIndexElemento()));
				codigoPkSeccionParam = "";
			}

			UtilidadBD.iniciarTransaccion(con);

			if (!Plantillas.actualizarComponenteParametrizable(con, componente
					.getConsecutivoParametrizacion(), componente.getCodigoPK()
					+ "", componente.getOrden() + "", ConstantesBD.acronimoSi,
					ConstantesBD.acronimoNo)) {
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion", new ActionMessage(
						"errors.notEspecific",
						"El Componente No fue Eliminado."));
				return errores;
			} else if (!codigoPkSeccionParam.equals("")
					&& !Plantillas.actualizarMostrarModOrdenSeccionParam(con,
							false, forma.getPlantillaDto()
									.getSeccionesFijasPos(
											Integer.parseInt(forma
													.getIndexSeccionFija()))
									.getOrden()
									+ "", codigoPkSeccionParam + "", usuario)) {
				UtilidadBD.abortarTransaccion(con);
				errores.add("descripcion", new ActionMessage(
						"errors.notEspecific",
						"La Seccion del Componente No fue Eliminado."));
				return errores;
			}

			// Recarga el Dto de Plantilla
			UtilidadBD.finalizarTransaccion(con);
			metodoCargarPlantilla(con, forma, usuario);
		}

		return errores;
	}
	
	private ActionErrors validarPlantillaConIgualNombre(ParametrizacionPlantillasForm forma, ActionErrors errores, HttpServletRequest request)
	{
		//Se hacen las validaciones respectivas para la evolucion y valoracion odontologica
		//if ((forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+"")||forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+""))&&forma.getProcesoASeguirOdontologia().equals(ConstantesBD.acronimoNo))
		//las anteriores validaciones se deben hacer si es una plantilla nueva.
		if ((forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+"")||forma.getPlantillaBase().equals(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+""))&&Utilidades.convertirAEntero(forma.getPlantillaDto().getCodigoPK())<=0)
		{
					if (Plantillas.consultarPlantillaConIgualNombre(Utilidades.convertirADouble(forma.getPlantillaBase()), forma.getNombre()))
					{
						errores.add("descripcion", new ActionMessage("errors.notEspecific","Ya existe una plantilla con el nombre "+forma.getNombre()+" por favor seleccionar otro"));
						saveErrors(request, errores);
					}
					if (forma.getNombre().trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","Un Nombre para la Plantilla "));
						saveErrors(request, errores);
					}
					if (forma.getTipoAtencion().trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","El Tipo de Atenciï¿½n para la Plantilla "));
						saveErrors(request, errores);
					}
		}
		
		return errores;
		
	}

}