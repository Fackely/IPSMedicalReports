
/*
 * Creado   21/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.TransaccionesValidasXCCForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.TransaccionesValidasXCC;

/**
 * Clase para manejar el workflow de la funcionalidad
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TransaccionesValidasXCCAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(TransaccionesValidasXCCAction.class);
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
			if(form instanceof TransaccionesValidasXCCForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				TransaccionesValidasXCCForm formTran=(TransaccionesValidasXCCForm)form;
				TransaccionesValidasXCC mundoTrans=new TransaccionesValidasXCC(); 
				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(sesion);
				String estado=formTran.getEstado();
				logger.warn("estado->"+estado);
				formTran.setProcesoExitoso(false);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de TransaccionesValidasXCCAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezar"))
				{
					formTran.reset(usuario.getCodigoInstitucionInt());			    
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");   
				}
				else if(estado.equals("generarConsulta"))
				{
					return accionConsulta(con,formTran,mapping,mundoTrans,usuario); 
				}
				else if(estado.equals("nuevoRegistro"))
				{
					return accionNuevoRegistro(con,formTran,mapping, usuario); 
				}
				else if(estado.equals("guardar"))
				{
					return accionGuardar(con,formTran,mapping,mundoTrans,usuario, request); 
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarMapa(con,formTran,mapping);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarRegistros(con,formTran,mapping);
				}

				//*********************INICIO MODIFICACIONES INGRESADOS POR EL ANEXO 728*****************************
				else if(estado.equals("cargarTransacciones"))
				{
					return this.accionCargarTransacciones(con, formTran, mundoTrans, usuario, mapping, request);
				}
				else if(estado.equals("nuevaTransaccion"))
				{
					return this.accionNuevaTransaccion(con, formTran, mundoTrans, usuario, mapping, request);
				}
				else if(estado.equals("detalleTransaccion"))
				{
					return this.accionDetalleTransaccion(con, formTran, mundoTrans, usuario, mapping);
				}
				else if(estado.equals("eliminarTransaccion"))
				{
					return this.accionEliminarTransaccion(con, formTran, mundoTrans, usuario, mapping, request);
				}
				else if(estado.equals("guardarTransaccion"))
				{
					return this.accionGuardarTransaccion(con, formTran, mundoTrans, usuario, mapping, request);
				}
				//************************FIN MODIFICACIONES INGRESADOS POR EL ANEXO 728*******************************

				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de TransaccionesValidasXCCForm");
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
	 * Método incluido por el Anexo 728. El cuál indica que se debe poder eliminar
	 * una transacción directamente; la cuál eliminara también todos sus detalles
	 * @param con
	 * @param formTran
	 * @param mundoTrans
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionEliminarTransaccion(Connection con, TransaccionesValidasXCCForm formTran, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("===>Posición: "+formTran.getPosicionSeleccionada());
		logger.info("===>Código Transacción a Eliminar: "+formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada()));
		logger.info("===>Código Centro de Costo: "+formTran.getCodCentroCosto());
		UtilidadBD.iniciarTransaccion(con);
		formTran.setProcesoExitoso(mundoTrans.eliminarTransaccion(con, formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+"", formTran.getCodCentroCosto(), usuario.getCodigoInstitucionInt()));
		
		if(formTran.isProcesoExitoso())
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		formTran.setEstado("cargarTransacciones");
		return this.accionCargarTransacciones(con, formTran, mundoTrans, usuario, mapping, request);
	}

	/**
	 * @param con
	 * @param formTran
	 * @param mundoTrans
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionNuevaTransaccion(Connection con, TransaccionesValidasXCCForm formTran, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		String codigoTransacciones = cargarCodigosTransacciones(formTran);
		logger.info("===>Codigos Transacciones: "+codigoTransacciones);
		formTran.setNuevoRegistro(true);
		formTran.resetListados();
		formTran.resetTransaccionesInventarios();
		formTran.setTransaccionesInventariosASeleccionar(Utilidades.consultarTransaccionesInventarios(con, "", true, true, codigoTransacciones));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * @param formTran
	 * @return
	 */
	private String cargarCodigosTransacciones(TransaccionesValidasXCCForm formTran)
	{
		String codigoTransacciones = "";
		//Guardamos el numRegistros del mapa
		int numReg = Utilidades.convertirAEntero(formTran.getTransaccionesInventarios("numRegistros")+"");
		
		if(numReg == 1)
		{
			if (!(formTran.getTransaccionesInventarios("consecutivo_0")+"").equals(""))
				codigoTransacciones = formTran.getTransaccionesInventarios("consecutivo_0")+"";
		}
		else if(numReg == 0)
		{
			codigoTransacciones = "";
		}
		else
		{
			if(numReg > 1)
			{
				for(int i=0; i<numReg; i++)
				{
					if (i == 0)
						codigoTransacciones = formTran.getTransaccionesInventarios("consecutivo_"+i)+"";
					else
						codigoTransacciones += ","+formTran.getTransaccionesInventarios("consecutivo_"+i)+"";
				}
			}
		}
		return codigoTransacciones;
	}

	/**
	 * @param con
	 * @param formTran
	 * @param mundoTrans
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleTransaccion(Connection con, TransaccionesValidasXCCForm formTran, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, ActionMapping mapping)
	{
		formTran.setNuevoRegistro(false);
		logger.info("===>Posicion Seleccionada: "+formTran.getPosicionSeleccionada());
		mundoTrans.setCodCentroCosto(formTran.getCodCentroCosto());
        mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoTrans.setTransaccionFiltro(Utilidades.convertirAEntero(formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+""));
		formTran.setMapaTrans(mundoTrans.generarConsulta(con,false));
		//Utilidades.imprimirMapa(formTran.getMapaTrans());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("nuevaTransaccion");
	}

	/**
	 * Método incluido por el Anexo 728. El cuál es el encargado de
	 * guardar un transacción y los N detalles (Clase de Inventario y Grupo)
	 * @param con
	 * @param formTran
	 * @param mundoTrans
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarTransaccion(Connection con, TransaccionesValidasXCCForm formTran, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("===>Codigo Transaccion Seleccionada: "+formTran.getCodigoTransaccionSeleccionada());
		logger.info("===>Centro de Costo Seleccionado: "+formTran.getCodCentroCosto());
		ActionErrors errores = new ActionErrors();
		errores = validacionCentroCosto(formTran); 
		if(!errores.isEmpty())
		{
			formTran.setEstado("nuevaTransaccion");
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");
		}
		else
		{
			formTran.setProcesoExitoso(true);
			mundoTrans.insertarTransaccion(con, formTran, usuario); 
			formTran.setEstado("cargarTransacciones");
			return this.accionCargarTransacciones(con, formTran, mundoTrans, usuario, mapping, request);
		}
	}

	/**
	 * Método que realiza las validaciones para poder realizar el almacenamiento
	 * de las Transacciones por Centro de Costo
	 * @param formTran
	 * @return
	 */
	private ActionErrors validacionCentroCosto(TransaccionesValidasXCCForm formTran)
	{
		ActionErrors errores = new ActionErrors();
		
		if(formTran.getCodCentroCosto() == ConstantesBD.codigoNuncaValido)
		{
            errores.add("centroCosto", new ActionMessage("errors.required","El Centro de Costo "));
            formTran.setErrores(true);
		}
		if(formTran.getEstado().equals("guardarTransaccion"))
		{	
			if(UtilidadTexto.isEmpty(formTran.getCodigoTransaccionSeleccionada()))
			{
				errores.add("falta campo", new ActionMessage("errors.required","La Transacción"));
	            formTran.setErrores(true);
			}
		}	
		
		if(errores.isEmpty())
			formTran.setErrores(false);
		
		return errores;
	}

	/**
	 * Método incluido por el Anexo 728. El cuál es el encargado de cargar
	 * las transacciones según el centro de costo seleccionado
	 * @param con
	 * @param formTran
	 * @param mundoTrans
	 * @param usuario 
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarTransacciones(Connection con, TransaccionesValidasXCCForm formTran, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		errores = validacionCentroCosto(formTran); 
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
		}
		else
		{
			formTran.setTransaccionesInventarios(mundoTrans.cargarTransaccionesInventarios(con, formTran.getCodCentroCosto(), usuario.getCodigoInstitucionInt()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
		}
	}
	
	/**
     * metodo para eliminar registros de memoria
     * @param con
     * @param formTran
     * @param mapping
     * @return
     */
    private ActionForward accionEliminarRegistros(Connection con, TransaccionesValidasXCCForm formTran, ActionMapping mapping) 
    {
        int posEli=formTran.getRegEliminar();
        int nuevaPos=Integer.parseInt(formTran.getMapaTrans("numRegistros")+"")-1;
                
        if((formTran.getMapaTrans("tipoReg_"+posEli)+"").equals("BD"))
        { 
          formTran.setRegistrosBDEliminar(formTran.getMapaTrans("codigo_"+posEli));          
        }
        formTran.setMapaTrans("numRegistros",nuevaPos+"");
        if(posEli!=nuevaPos)        
	        for(int j=posEli;j<Integer.parseInt(formTran.getMapaTrans("numRegistros")+"");j++)
	        {                                 
	            formTran.setMapaTrans("tipoReg_"+j,formTran.getMapaTrans("tipoReg_"+(j+1)));
	            formTran.setMapaTrans("cod_tipos_trans_inventario_"+j,formTran.getMapaTrans("cod_tipos_trans_inventario_"+(j+1)));
	            formTran.setMapaTrans("desc_tipos_trans_inventario_"+j,formTran.getMapaTrans("desc_tipos_trans_inventario_"+(j+1)));
	            formTran.setMapaTrans("cod_clase_inventario_"+j,formTran.getMapaTrans("cod_clase_inventario_"+(j+1)));
	            formTran.setMapaTrans("nom_clase_inventario_"+j,formTran.getMapaTrans("nom_clase_inventario_"+(j+1)));
	            formTran.setMapaTrans("cod_grupo_inventario_"+j,formTran.getMapaTrans("cod_grupo_inventario_"+(j+1)));
	            formTran.setMapaTrans("nom_grupo_inventario_"+j,formTran.getMapaTrans("nom_grupo_inventario_"+(j+1)));
	            formTran.setMapaTrans("codigo_"+j,formTran.getMapaTrans("codigo_"+(j+1)));	             
	        }
        formTran.getMapaTrans().remove("tipoReg_"+nuevaPos);
        formTran.getMapaTrans().remove("cod_tipos_trans_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("desc_tipos_trans_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("cod_clase_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("nom_clase_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("cod_grupo_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("nom_grupo_inventario_"+nuevaPos);
        formTran.getMapaTrans().remove("codigo_"+nuevaPos);        
        UtilidadBD.closeConnection(con);
	    return mapping.findForward("nuevaTransaccion");
    }
    
    /**
     * metodo para ordenar
     * @param con
     * @param formTran
     * @param mapping
     * @return
     */
    private ActionForward accionOrdenarMapa(Connection con, TransaccionesValidasXCCForm formTran, ActionMapping mapping) 
    {
        int numRegTemp=0;
        numRegTemp=Integer.parseInt(formTran.getMapaTrans("numRegistros")+"");        
        String[] indices={
				            "tipoReg_", 
				            "cod_tipos_trans_inventario_", 
				            "desc_tipos_trans_inventario_", 
				            "cod_clase_inventario_",
				            "nom_clase_inventario_",
				            "cod_grupo_inventario_",
				            "nom_grupo_inventario_",
				            "codigo_"				            				            
	            		};
        formTran.setMapaTrans(Listado.ordenarMapa(indices,
															formTran.getPatronOrdenar(),
															formTran.getUltimoPatron(),
															formTran.getMapaTrans(),
															numRegTemp));
        formTran.setMapaTrans("numRegistros",numRegTemp+"");
        formTran.setUltimoPatron(formTran.getPatronOrdenar());
        UtilidadBD.closeConnection(con);
	    
        //Se modifica por el Anexo 728
        //return mapping.findForward("paginaPrincipal");
        return mapping.findForward("nuevaTransaccion");
    }
    
    /**
     * metodo para guardar las operaciones
     * realizadas, insertar,modificar,eliminar
     * @param con
     * @param formTran
     * @param mapping
     * @param mundoTrans
     * @param usuario
     * @param request 
     * @return
     */
    private ActionForward accionGuardar(Connection con, TransaccionesValidasXCCForm formTran, ActionMapping mapping, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	mundoTrans.setCodCentroCosto(formTran.getCodCentroCosto());
        mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoTrans.setMapaTrans(formTran.getMapaTrans());
        mundoTrans.setRegistrosBDEliminar(formTran.getRegistrosBDEliminar());
        mundoTrans.setUsuario(usuario.getLoginUsuario());
        
        //Modificado por el Anexo 728
        //formTran.setProcesoExitoso(mundoTrans.guardarCambiosEnBDTrans(con));     
        logger.info("===>Nuevo Registro: "+formTran.isNuevoRegistro());
        if(formTran.isNuevoRegistro())
        {
        	logger.info("===>Codigo Transaccion Nuevo: "+formTran.getCodigoTransaccionSeleccionada());
        	formTran.setProcesoExitoso(mundoTrans.guardarCambiosEnBDTrans(con, formTran.getCodigoTransaccionSeleccionada()));
        }
        else
        {
        	logger.info("===>Codigo Transaccion Modificado: "+formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+"");
        	formTran.setProcesoExitoso(mundoTrans.guardarCambiosEnBDTrans(con, formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+""));
        }
        
        formTran.resetListados();
        return accionConsulta(con,formTran,mapping,mundoTrans,usuario);
    }
    
    /**
     * metodo para generar la consulta de 
     * transacciones validas
     * @param con
     * @param formTran
     * @param mapping
     * @param mundoTrans
     * @param usuario
     * @return
     */
    private ActionForward accionConsulta(Connection con, TransaccionesValidasXCCForm formTran, ActionMapping mapping, TransaccionesValidasXCC mundoTrans, UsuarioBasico usuario) 
    {
        mundoTrans.setCodCentroCosto(formTran.getCodCentroCosto());
        mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());

        //Modificado por el Anexo 728
        logger.info("===>Nuevo Registro: "+formTran.isNuevoRegistro());
        if(formTran.isNuevoRegistro())
        {
        	logger.info("===>Codigo Transaccion Nuevo: "+formTran.getCodigoTransaccionSeleccionada());
        	mundoTrans.setTransaccionFiltro(Utilidades.convertirAEntero(formTran.getCodigoTransaccionSeleccionada()));
        }
        else
        {
        	logger.info("===>Codigo Transaccion Modificado: "+formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+"");
        	mundoTrans.setTransaccionFiltro(Utilidades.convertirAEntero(formTran.getTransaccionesInventarios("consecutivo_"+formTran.getPosicionSeleccionada())+""));
        }
        
        formTran.setMapaTrans(mundoTrans.generarConsulta(con,false));	    
	    UtilidadBD.closeConnection(con);
	    //Se modifica por el Anexo 728
        //return mapping.findForward("paginaPrincipal");
        return mapping.findForward("nuevaTransaccion");
    }
    
    /**
     * metodo para generar un nuevo registro
     * @param con
     * @param formTran
     * @param mapping
     * @return
     */
    private ActionForward accionNuevoRegistro(Connection con, TransaccionesValidasXCCForm formTran, ActionMapping mapping, UsuarioBasico usuario) 
    {
        int pos=Integer.parseInt(formTran.getMapaTrans("numRegistros")+"");
        formTran.setMapaTrans("tipoReg_"+pos, "MEM");           
        formTran.setMapaTrans("cod_tipos_trans_inventario_"+pos, formTran.getTransaccionFiltro());
        formTran.setMapaTrans("desc_tipos_trans_inventario_"+pos, UtilidadInventarios.obtenerNombreTipoTransaccion(con, formTran.getTransaccionFiltro(), usuario.getCodigoInstitucionInt()));
        formTran.setMapaTrans("cod_clase_inventario_"+pos, "-1");
        formTran.setMapaTrans("nom_clase_inventario_"+pos, "Seleccione");
        formTran.setMapaTrans("cod_grupo_inventario_"+pos, "-1");
        formTran.setMapaTrans("nom_grupo_inventario_"+pos, "Seleccione");
        formTran.setMapaTrans("codigo_"+pos, "-1");
        pos++;
        formTran.setMapaTrans("numRegistros",pos+"");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("nuevaTransaccion");   
    }    
}
