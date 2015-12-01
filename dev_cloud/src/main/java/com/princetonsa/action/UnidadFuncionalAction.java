/*
 * Mayo 04, 2006 
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.UnidadFuncionalForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UnidadFuncional;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * @author Sebastián Gómez
 *
 * Action usado para controlar los procesos de parametrización de 
 * las unidades funcionales
 */
public class UnidadFuncionalAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(UnidadFuncionalAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof UnidadFuncionalForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				UnidadFuncionalForm unidadForm =(UnidadFuncionalForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=unidadForm.getEstado(); 
				logger.warn("estado UNIDADES FUNCIONALES-->"+estado);


				if(estado == null)
				{
					unidadForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Unidades Funcionales (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar")||estado.equals("consultar"))
				{
					return accionEmpezar(con,unidadForm,mapping,usuario);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,unidadForm,mapping,usuario);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,unidadForm,mapping,usuario,request);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,unidadForm,mapping,usuario,request);
				}
				else if(estado.equals("ordenar"))
				{
					UtilidadBD.cerrarConexion(con);
					return accionOrdenar(unidadForm,mapping);
				}
				else
				{
					unidadForm.reset();
					logger.warn("Estado no valido dentro del flujo de unidades funcionales (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * Método implementado para ordenar el listado de unidades funcionales
	 * @param unidadForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(UnidadFuncionalForm unidadForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"codigo_",
				"descripcion_",
				"activo_",
				"institucion_",
				"esta_usado_",
				"existe_"
			};
		
		int numeroElementos = unidadForm.getNumUnidades();
		
		
		unidadForm.setUnidades(Listado.ordenarMapa(indices,
				unidadForm.getIndice(),
				unidadForm.getUltimoIndice(),
				unidadForm.getUnidades(),
				numeroElementos));
		
		unidadForm.setUnidades("numRegistros",numeroElementos+"");
		
		unidadForm.setUltimoIndice(unidadForm.getIndice());
		unidadForm.setEstado("empezar");
		
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para eliminar una unidad funcional
	 * @param con
	 * @param unidadForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, UnidadFuncionalForm unidadForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
			
		int resp=1; //variable para almacenar el resultado de la eliminación
		
		//se instancia objeto de Unidad Funcioanl
		UnidadFuncional unidad = new UnidadFuncional();
		
		//se llena el mundo con los datos del formulario
		this.llenarMundo(unidadForm,unidad,unidadForm.getPosRegistro());
		//se verifica que sea un registro existente 
		if(unidad.cargar(con))
		{
				
				
				//se realiza la eliminación
				resp=unidad.eliminar(con);
				//se verifica resultado de la transacción
				if(resp<=0)
				{
					logger.error("No se pudo eliminar el registro");
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UnidadFuncionalAction", "errors.sinEliminar", true);
				}
				//se inserta LOG
				this.generarLog(con,unidad,null,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			
		}
		
		if(resp>0)
		{
			//Se elimina registro del mapa
			for(int i=unidadForm.getPosRegistro();i<(unidadForm.getNumUnidades()-1);i++)
			{
				unidadForm.setUnidades("codigo_"+i,unidadForm.getUnidades("codigo_"+(i+1)));
				unidadForm.setUnidades("descripcion_"+i,unidadForm.getUnidades("descripcion_"+(i+1)));
				unidadForm.setUnidades("activo_"+i,unidadForm.getUnidades("activo_"+(i+1)));
				unidadForm.setUnidades("institucion_"+i,unidadForm.getUnidades("institucion_"+(i+1)));
				unidadForm.setUnidades("esta_usado_"+i,unidadForm.getUnidades("esta_usado_"+(i+1)));
			}
			
			unidadForm.getUnidades().remove("codigo_"+(unidadForm.getNumUnidades()-1));
			unidadForm.getUnidades().remove("descripcion_"+(unidadForm.getNumUnidades()-1));
			unidadForm.getUnidades().remove("activo_"+(unidadForm.getNumUnidades()-1));
			unidadForm.getUnidades().remove("institucion_"+(unidadForm.getNumUnidades()-1));
			unidadForm.getUnidades().remove("esta_usado_"+(unidadForm.getNumUnidades()-1));
			
			//se actualiza tamaño del mapa
			unidadForm.setNumUnidades(unidadForm.getNumUnidades()-1);
			unidadForm.setUnidades("numRegistros",unidadForm.getNumUnidades()+"");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para guardar los cambios en el listado de
	 * unidades funcionales
	 * @param con
	 * @param unidadForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, UnidadFuncionalForm unidadForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		UtilidadBD.iniciarTransaccion(con);
		UnidadFuncional unidadBD=new UnidadFuncional(); //objeto usado para almacenar el registro antes de ser modificado
		boolean exitoActualizacion=true;
		boolean exitoInsercion=true;
		int resp=0;
		
		
		//se instancia objeto UnidadesFuncionales
		UnidadFuncional unidad=new UnidadFuncional();
		//se itera arreglo de registros
		for(int i=0;i<unidadForm.getNumUnidades();i++)
		{
			//Se llenan los datos del registro modificado
			this.llenarMundo(unidadForm,unidad,i);
			
			//Se carga el registro actual de la BD
			this.llenarMundo(unidadForm,unidadBD,i);
			//se verifica que el registro es nuevo o de modificación TRUE=> existe , FALSE => no existe
			if(unidadBD.cargar(con))
			{
				//Se revisa si el registro fue modificado
				if(fueModificado(unidad,unidadBD))
				{
					//se actualizan los datos
					resp=unidad.modificar(con);
					//se revisa estado actualizacion
					if(resp<=0)
						exitoActualizacion=false;
					else
						this.generarLog(con,unidad,unidadBD,ConstantesBD.tipoRegistroLogModificacion,usuario);
				}
			}
			//se inserta nuevo registro
			else
			{
				//se insertan los datos
				resp=unidad.insertar(con);
				//se revisa estado actualizacion
				if(resp<=0)
					exitoInsercion=false;
			}
		}
		//verificación de la transacción
		if(exitoActualizacion&&exitoInsercion)
		{
			//éxito!!!!
			UtilidadBD.finalizarTransaccion(con);
			this.cargarListado(con,unidadForm,usuario);
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		//el problema fue en la insercion
		else if(!exitoInsercion)
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UnidadFuncionalAction", "errors.sinIngresar",true);
		}
		// o el problema fue en la actualización
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UnidadFuncionalAction", "errors.sinActualizar",true);
		}
			
	
	}

	/**
	 * Método implementado para generar el LOG modificacion/eliminacion de unidades funcionales
	 * @param con 
	 * @param unidad
	 * @param unidadBD
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(Connection con, UnidadFuncional unidad, UnidadFuncional unidadBD, int tipo, UsuarioBasico usuario) 
	{
		String log = "";
		
		//Carga de información Institución
		ParametrizacionInstitucion institucion = new ParametrizacionInstitucion();
		institucion.cargar(con,unidad.getInstitucion());
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			
		    log="\n            ====INFORMACION ORIGINAL UNIDAD FUNCIONAL===== " +
		    "\n*  Código [" +unidadBD.getCodigo()+"] "+
			"\n*  Descripción ["+unidadBD.getDescripcion()+"] " +
			"\n*  Activo ["+(unidadBD.isActivo()?"Sí":"No")+"] " +
			"\n*  Institucion ["+institucion.getRazonSocial()+"] " +
			""  ;
			
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN UNIDAD FUNCIONAL===== " +
		    "\n*  Codigo [" +unidad.getCodigo()+"] "+
			"\n*  Descripción ["+unidad.getDescripcion()+"] " +
			"\n*  Activo ["+(unidad.isActivo()?"Sí":"No")+"] " +
			"\n*  Institucion ["+institucion.getRazonSocial()+"] " +
			""  ;
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		    log="\n            ====INFORMACION ELIMINADA DE UNIDAD FUNCIONAL===== " +
			"\n*  Codigo [" +unidad.getCodigo()+"] "+
			"\n*  Descripción ["+unidad.getDescripcion()+"] " +
			"\n*  Activo ["+(unidad.isActivo()?"Sí":"No")+"] " +
			"\n*  Institucion ["+institucion.getRazonSocial()+"] " +
			""  ;
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logUnidadesFuncionalesCodigo, log, tipo,usuario.getLoginUsuario()); 
		
	}

	/**
	 * Método implementado para verificar si el registro fue modificado
	 * comparando el registro actual con el registro de la base de datos
	 * @param unidad
	 * @param unidadBD
	 * @return
	 */
	private boolean fueModificado(UnidadFuncional unidad, UnidadFuncional unidadBD) 
	{
		boolean fueModificado = false;
		
		if(!unidad.getDescripcion().equals(unidadBD.getDescripcion()))
			fueModificado = true;
		else if(unidad.isActivo()!=unidadBD.isActivo())
			fueModificado = true;
		return fueModificado;
	}

	/**
	 * Método implementado para cargar el mundo
	 * de un unidad funcional con los datos de la forma
	 * @param unidadForm
	 * @param unidad
	 * @param pos
	 */
	private void llenarMundo(UnidadFuncionalForm unidadForm, UnidadFuncional unidad, int pos) 
	{
		unidad.setCodigo(unidadForm.getUnidades("codigo_"+pos).toString());
		unidad.setDescripcion(unidadForm.getUnidades("descripcion_"+pos).toString());
		unidad.setActivo(UtilidadTexto.getBoolean(unidadForm.getUnidades("activo_"+pos).toString()));
		unidad.setInstitucion(Integer.parseInt(unidadForm.getUnidades("institucion_"+pos).toString()));
		
	}

	/**
	 * Método implementado para postular un nuevo registro en el listado
	 * de unidades funcionales
	 * @param con
	 * @param unidadForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, UnidadFuncionalForm unidadForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//se obtiene nueva posicion
		int pos=unidadForm.getNumUnidades();
		
		//se crea un nuevo registro
		unidadForm.setUnidades("codigo_"+pos,""); //se agrega código vacío
		unidadForm.setUnidades("descripcion_"+pos,""); //sin descripción
		unidadForm.setUnidades("activo_"+pos,"false"); //por defecto desactivado
		unidadForm.setUnidades("institucion_"+pos,usuario.getCodigoInstitucion()); //institucion
		unidadForm.setUnidades("esta_usado_"+pos,"false"); //no se está usando
		unidadForm.setUnidades("existe_"+pos,"false"); //no existe en la BD
		
		//se aumenta número de registros
		pos++;
		
		//se actualiza tamaño del arreglo en el formulario
		unidadForm.setUnidades("numRegistros",pos+"");
		unidadForm.setNumUnidades(pos);
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para iniciar el ingreso/modificación
	 * de las unidades funcionales
	 * @param con
	 * @param unidadForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, UnidadFuncionalForm unidadForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se reinicia forma
		unidadForm.reset();
		
		this.cargarListado(con,unidadForm,usuario);
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método para cargar el listado de unidades funcionales a la forma
	 * @param con
	 * @param unidadForm
	 * @param usuario 
	 */
	private void cargarListado(Connection con, UnidadFuncionalForm unidadForm, UsuarioBasico usuario) 
	{
		//Se instancia objeto de Unidades Funcionales
		UnidadFuncional unidad = new UnidadFuncional();
		
		//Se cargan las unidades funcionales por institucion
		unidad.setInstitucion(usuario.getCodigoInstitucionInt());
		unidadForm.setUnidades(unidad.consultarListado(con));
		//se asigna el tamaño de la consulta
		unidadForm.setNumUnidades(Integer.parseInt(unidadForm.getUnidades("numRegistros").toString()));
		
		
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
