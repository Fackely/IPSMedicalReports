/*
 * @(#)ExcepcionesFarmaciaConvenioAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.action.facturacion;

import java.sql.Connection;

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
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ExcepcionesFarmaciaConvenioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.medicamentos.ExcepcionesFarmacia;

/**
 * Clase encargada del control de la funcionalidad
 * Excepciones Farmacia
 *
 * @version 1.0 Nov 29, 2004
 */
public class ExcepcionesFarmaciaConvenioAction extends Action
{
    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	private Logger logger = Logger.getLogger(ExcepcionesFarmaciaConvenioAction.class);
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
		{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if( logger.isDebugEnabled() )
			{
				logger.debug("Entro al Action de Excepciones Farmacia");
			}
			if (form instanceof ExcepcionesFarmaciaConvenioForm)
			{
				ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm=(ExcepcionesFarmaciaConvenioForm)form;
				String estado=excepcionesFarmaciaConvenioForm.getEstado();
				logger.info("[ExcepcionesFarmaciaConvenioAction] - estado->"+estado);



				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.
				HttpSession session=request.getSession();			
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				if (estado==null||estado.equals(""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (RequisitosPacienteConvenioAction)", "errors.estadoInvalido", true);
				}
				else if (estado.equals("inicioConsultar"))
				{
					return this.accionInicioConsultar(mapping, con, excepcionesFarmaciaConvenioForm);
				}
				else if (estado.equals("inicioModificar"))
				{
					return this.accionInicioModificar(mapping, con, excepcionesFarmaciaConvenioForm);
				}
				else if (estado.equals("modificar"))
				{
					if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar()==null)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (RequisitosPacienteConvenioAction)", "errors.estadoInvalido", true);
					}
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("busquedaAvanzada"))
					{
						return accionBusquedaAvanzadaModificar(mapping, excepcionesFarmaciaConvenioForm, con) ;
					}
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("cambioConvenio"))
					{
						return accionCambioConvenioModificar(mapping, excepcionesFarmaciaConvenioForm, con) ;
					}
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("eliminarRequisito"))
					{
						return accionEliminarExcepcion (mapping, request, excepcionesFarmaciaConvenioForm, con, usuario) ;
					}
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("agregarNuevo"))
					{
						excepcionesFarmaciaConvenioForm.agregarExcepcionMemoria();
						return accionHacerSubmitArtificial(con, excepcionesFarmaciaConvenioForm, response) ;
					}
					//El único objetivo de este caso es permitir que el form
					//se llene con los datos dados por el usuario
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("guardarRequisito"))
					{
						return guardarRequisito (mapping, request, excepcionesFarmaciaConvenioForm, con, usuario) ;
					}
					else if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("hacerSubmitArtificial"))
					{
						return accionHacerSubmitArtificial(con, excepcionesFarmaciaConvenioForm, response) ;
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ExcepcionesFarmaciaConvenioAction)", "errors.estadoInvalido", true);
					}
				}
				else if (estado.equals("consultar"))
				{
					if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar().equals("busquedaAvanzada"))
					{
						return accionBusquedaAvanzadaConsulta (con, mapping, excepcionesFarmaciaConvenioForm);
					}
					this.seleccionarNuevoConvenio(excepcionesFarmaciaConvenioForm, con);
					return mapping.findForward("consultar");
				}
				else
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ExcepcionesFarmaciaConvenioAction)", "errors.estadoInvalido", true);
				}
			}
			else
			{
				//No se cierra la conexión porque solo se abre si nos encontramos
				//con una forma válida
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
	 * Método que se encarga de realizar la
	 * carga de datos para el caso de búsqueda avanzada
	 * en consultar
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapping Mapa de recursos de struts
	 * @param excepcionesFarmaciaConvenioForm Forma de este
	 * action
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionBusquedaAvanzadaConsulta (Connection con, ActionMapping mapping, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm) throws Exception
	{
        ExcepcionesFarmacia excepcionFarmacia=new ExcepcionesFarmacia ();
        excepcionFarmacia.getConvenio().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoConvenioBusqueda());
        excepcionFarmacia.getCentroCosto().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoCentroCostoBusqueda());
        excepcionFarmacia.getArticulo().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoArticuloBusqueda());
        excepcionFarmacia.getArticulo().setNombre(excepcionesFarmaciaConvenioForm.getNombreArticuloBusqueda());
        excepcionFarmacia.cargarExcepcionesBaseFarmacia(excepcionesFarmaciaConvenioForm.obtenerMapa(), con);

        
        //En este punto el código del convenio buscado se asimila
        //al código del convenio de la búsqueda
        excepcionesFarmaciaConvenioForm.setCodigoConvenioSeleccionado(excepcionesFarmaciaConvenioForm.getCodigoConvenioBusqueda());
	    excepcionesFarmaciaConvenioForm.generarBackup();
	    excepcionesFarmaciaConvenioForm.setAccionAFinalizar("");
	    UtilidadBD.cerrarConexion(con);

        return mapping.findForward("consultar");
	}

	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida en caso que el usuario seleccione
	 * la inicioConsultar
	 * 
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param excepcionesFarmaciaConvenioForm Forma de este
	 * action
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionInicioConsultar (ActionMapping mapping, Connection con, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm) throws Exception
	{
	    UtilidadBD.cerrarConexion(con);
	    excepcionesFarmaciaConvenioForm.reset();
	    return mapping.findForward("consultar");
	}
	
	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida en caso que el usuario seleccione
	 * la inicioModificar
	 * 
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param excepcionesFarmaciaConvenioForm Forma de este
	 * action
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionInicioModificar (ActionMapping mapping, Connection con, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm)throws Exception 
	{
	    UtilidadBD.cerrarConexion(con);
	    excepcionesFarmaciaConvenioForm.reset();
	    excepcionesFarmaciaConvenioForm.setEstado("modificar");
	    return mapping.findForward("modificar");
	}
	
	/**
	 * Método privado que se encarga de enviar al usuario a la
	 * pàgina que pidío sin hacer nada. Su único objetivo es
	 * el de permitir a la infraestructura struts, llenar los
	 * datos del mapa
	 * 
	 * @param con
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionHacerSubmitArtificial(Connection con, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, HttpServletResponse response) throws Exception
	{
        UtilidadBD.cerrarConexion(con);
        
        response.sendRedirect(excepcionesFarmaciaConvenioForm.getPaginaSiguiente());
        return null;
	}

	/**
	 * Método que hace toda la funcionalidad necesaria
	 * para seleccionar un nuevo convenio. Lo único que no
	 * hace es el mapping, ya que la acción es común entre
	 * modificar y consultar. Ojo, cierra la conexión
	 * @param excepcionesFarmaciaConvenioForm Forma de este
	 * action
	 * @param con Conexión con la fuente de datos
	 * @throws Exception
	 */
	private void seleccionarNuevoConvenio (ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, Connection con) throws Exception
	{
	    if (excepcionesFarmaciaConvenioForm.getAccionAFinalizar()!=null)
	    {
		    if (excepcionesFarmaciaConvenioForm.getCodigoConvenioSeleccionado()!=ConstantesBD.codigoNuncaValido)
		    {
		        ExcepcionesFarmacia excepcionFarmacia=new ExcepcionesFarmacia ();
		        excepcionFarmacia.getConvenio().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoConvenioSeleccionado());
		        excepcionFarmacia.cargarExcepcionesBaseFarmacia(excepcionesFarmaciaConvenioForm.obtenerMapa(), con);
		    }
		    excepcionesFarmaciaConvenioForm.generarBackup();
		    excepcionesFarmaciaConvenioForm.setAccionAFinalizar("");
		    UtilidadBD.cerrarConexion(con);
	    }
	}
	
	/**
	 * Método que se encarga de realizar la
	 * carga de datos para el caso de búsqueda avanzada
	 * en modificar
	 * 
	 * @param mapping Mapping de Struts
	 * @param excepcionesFarmaciaConvenioForm Forma
	 * de este action
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionBusquedaAvanzadaModificar(ActionMapping mapping, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, Connection con) throws Exception
	{
        ExcepcionesFarmacia excepcionFarmacia=new ExcepcionesFarmacia ();
        excepcionFarmacia.getConvenio().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoConvenioBusqueda());
        excepcionFarmacia.getCentroCosto().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoCentroCostoBusqueda());
        excepcionFarmacia.getArticulo().setCodigo(excepcionesFarmaciaConvenioForm.getCodigoArticuloBusqueda());
        excepcionFarmacia.getArticulo().setNombre(excepcionesFarmaciaConvenioForm.getNombreArticuloBusqueda());
        excepcionFarmacia.cargarExcepcionesBaseFarmacia(excepcionesFarmaciaConvenioForm.obtenerMapa(), con);

        
        //En este punto el código del convenio buscado se asimila
        //al código del convenio de la búsqueda
        excepcionesFarmaciaConvenioForm.setCodigoConvenioSeleccionado(excepcionesFarmaciaConvenioForm.getCodigoConvenioBusqueda());
	    excepcionesFarmaciaConvenioForm.generarBackup();
	    excepcionesFarmaciaConvenioForm.setAccionAFinalizar("");
	    UtilidadBD.cerrarConexion(con);

        return mapping.findForward("modificar");
	}
	
	/**
	 * Método que se encarga de realizar la carga
	 * de datos para el caso de búsqueda avanzada
	 * en modificar
	 * 
	 * @param mapping Mapping de Struts
	 * @param excepcionesFarmaciaConvenioForm Forma
	 * de este action
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionCambioConvenioModificar(ActionMapping mapping, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, Connection con) throws Exception
	{
        //Antes que nada vamos a limpiar los datos
        //de búsqueda
        excepcionesFarmaciaConvenioForm.limpiarCriteriosBusqueda();
        this.seleccionarNuevoConvenio(excepcionesFarmaciaConvenioForm, con);
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("modificar");
	}
	
	/**
	 * Método que se encarga de realizar la
	 * eliminación de una excepción
	 * 
	 * @param mapping Mapping de Struts
	 * @param request Request de http
	 * @param excepcionesFarmaciaConvenioForm Forma
	 * de este action
	 * @param con Conexión con la fuente de datos
	 * @param usuario UsuarioBasico usuario que
	 * realiza la eliminación
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEliminarExcepcion (ActionMapping mapping, HttpServletRequest request, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, Connection con, UsuarioBasico usuario) throws Exception
	{
        //Si el elemento es invalido, no se debe eliminar de la BD
        int indiceElementoAEliminar=excepcionesFarmaciaConvenioForm.getIndiceRequisitoAEliminar();
        int codigoRecibido=Integer.parseInt((String)excepcionesFarmaciaConvenioForm.getUsoMapa("codigo_" + indiceElementoAEliminar));
        if(codigoRecibido!=ConstantesBD.codigoNuncaValido)
        {
	        ExcepcionesFarmacia excepcionFarmacia=new ExcepcionesFarmacia ();
	        excepcionFarmacia.setCodigo(codigoRecibido);
	        if (excepcionFarmacia.eliminarExcepcionBaseFarmacia(con)<=0)
	        {
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger,"Error al modificar el Servicios (ExcepcionFarmaciaAction) ", "errors.problemasBd", true);
	        }
	        else
	        {
                //Si todo salió bien guardamos un log de la eliminación
                String posibleLog=
                    "\n            ====ELEMENTO ELIMINADO EXCEPCIONES FARMACIA===== " +
                    "\n*  Código Excepción [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigo_" + indiceElementoAEliminar) + "]"+
                    "\n*  Convenio [" + excepcionesFarmaciaConvenioForm.getUsoMapa("convenio_" + indiceElementoAEliminar) +"]"+
                    "\n*  Centro Costo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("centroCosto_" + indiceElementoAEliminar) + "]"+
                    "\n*  Artículo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("articulo_" + indiceElementoAEliminar) + "]"+
                    "\n*  No Cubre [" + excepcionesFarmaciaConvenioForm.getUsoMapa("noCubre_" + indiceElementoAEliminar) + "]"+
            		"\n========================================================\n\n\n ";
                
                LogsAxioma.enviarLog(ConstantesBD.logExcepcionesFarmaciaConvenioCodigo, 
                        posibleLog,
                		ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
	        }
        }
        
        //En cualquier caso debo seleccionar de nuevo el convenio 
        //dado por el usuario
        this.seleccionarNuevoConvenio(excepcionesFarmaciaConvenioForm, con);

        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("modificar");
        //La siguiente línea solo aplica para el
        //enfoque anterior
        //excepcionesFarmaciaConvenioForm.eliminarExcepcion();
	}
	
	/**
	 * Método que se encarga de guardar los cambios
	 * a una excepción
	 * 
	 * @param mapping Mapping de Struts
	 * @param request Request de http
	 * @param excepcionesFarmaciaConvenioForm Forma
	 * de este action
	 * @param con Conexión con la fuente de datos
	 * @param usuario UsuarioBasico usuario que
	 * realiza la eliminación
	 * @return
	 * @throws Exception
	 */
	private ActionForward guardarRequisito (ActionMapping mapping, HttpServletRequest request, ExcepcionesFarmaciaConvenioForm excepcionesFarmaciaConvenioForm, Connection con, UsuarioBasico usuario) throws Exception
	{
        int indiceAGuardar=excepcionesFarmaciaConvenioForm.getIndiceRequisitoAGuardar();
        //Antes de insertar revisamos si el elemento existe
        ExcepcionesFarmacia excepcion=new ExcepcionesFarmacia();
        excepcion.setCodigoConvenio(excepcionesFarmaciaConvenioForm.getCodigoConvenioSeleccionado());
        excepcion.setCodigoCentroCosto(
                Integer.parseInt (
                        (String)excepcionesFarmaciaConvenioForm.getUsoMapa("codigoCentroCosto_" + indiceAGuardar)
                        )
                );
        excepcion.setCodigoArticulo(
                Integer.parseInt(
                        (String)excepcionesFarmaciaConvenioForm.getUsoMapa("codigoArticulo_" + indiceAGuardar)
                        )
                );
        excepcion.setCodigo(
                Integer.parseInt(
                        (String)excepcionesFarmaciaConvenioForm.getUsoMapa("codigo_" + indiceAGuardar)
                        )
                );
        excepcion.setNoCubre(
                Double.parseDouble(
                        (String)excepcionesFarmaciaConvenioForm.getUsoMapa("noCubre_" + indiceAGuardar)
                        )
                );
        
        if (excepcion.existeBaseExcepcionFarmaciaPrevio(con))
        {
            //Si existe en la Base de datos, sacamos un error
            ActionErrors errores = new ActionErrors();
            errores.add("error1", new ActionMessage("error.excepcionFarmaciaConvenio.excepcionRepetida"));

            saveErrors(request, errores);
            
            //debo retornar a la pàgina, 
            //sin continuar un proceso
		    UtilidadBD.cerrarConexion(con);
		    return mapping.findForward("modificar");
            
        }
        
        //Si todo sale bien, hay dos casos:
        //1. Que sea una inserción - Cuando hay un código inválido
        //2. Que sea una modificación - Cuando el código es válido
        
        if (excepcion.getCodigo()==ConstantesBD.codigoNuncaValido)
        {
            if (excepcion.insertarBaseExcepcionFarmacia(con)<=0)
            {
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger,"Error al modificar el Servicios (ExcepcionFarmaciaAction) ", "errors.problemasBd", true);
            }
        }
        else
        {
            if (excepcion.modificarBaseExcepcionFarmacia(con)<=0)
            {
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger,"Error al modificar el Servicios (ExcepcionFarmaciaAction) ", "errors.problemasBd", true);
            }
            else
            {
                //Si todo salió bien guardamos un log de la modificación
                
                String posibleLog=
                    "\n            ====ELEMENTO MODIFICADo EXCEPCIONES FARMACIA===== " +
                    "\n------------------------------------------------------------- " +
                    "\n*  Datos Originales"+
                    "\n------------------------------------------------------------- " +
                    "\n*  Código Excepción  [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigo_" + indiceAGuardar) + "]"+
                    "\n*  Código Convenio [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoConvenioBackup_" + indiceAGuardar) +"]"+
                    "\n*  Convenio [" + excepcionesFarmaciaConvenioForm.getUsoMapa("convenioBackup_" + indiceAGuardar) +"]"+
                    "\n*  Código Centro Costo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoCentroCostoBackup_" + indiceAGuardar) + "]"+
                    "\n*  Centro Costo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("centroCostoBackup_" + indiceAGuardar) + "]"+
                    "\n*  Código Artículo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoArticuloBackup_" + indiceAGuardar) + "]"+
                    "\n*  Artículo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("articuloBackup_" + indiceAGuardar) + "]"+
                    "\n*  No Cubre [" + excepcionesFarmaciaConvenioForm.getUsoMapa("noCubreBackup_" + indiceAGuardar) + "]"+
                    "\n------------------------------------------------------------- " +
                    "\n*  Datos Después Modificación"+
                    "\n------------------------------------------------------------- " +
                    "\n*  Código Convenio [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoConvenio_" + indiceAGuardar) +"]"+
                    "\n*  Código Centro Costo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoCentroCosto_" + indiceAGuardar) + "]"+
                    "\n*  Código Artículo [" + excepcionesFarmaciaConvenioForm.getUsoMapa("codigoArticulo_" + indiceAGuardar) + "]"+
                    "\n*  No Cubre [" + excepcionesFarmaciaConvenioForm.getUsoMapa("noCubre_" + indiceAGuardar) + "]"+
            		"\n========================================================\n\n\n ";
                
                LogsAxioma.enviarLog(ConstantesBD.logExcepcionesFarmaciaConvenioCodigo, 
                        posibleLog,
                		ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());

            }
        }
        
        this.seleccionarNuevoConvenio(excepcionesFarmaciaConvenioForm, con);
        
        //Una vez terminado se debe ir a la pàgina siguiente
        //Eso lo hace gracias al hidden con el normal
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("modificar");
        
	}

}
