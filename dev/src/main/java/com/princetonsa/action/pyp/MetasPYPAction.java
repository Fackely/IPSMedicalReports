/*
 * Ago 09, 2006
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pyp.MetasPYPForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.MetasPYP;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Metas PYP
 */
public class MetasPYPAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MetasPYPAction.class);
	
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
		//SE ABRE CONEXION

		try{


			if (response==null); //Para evitar que salga el warning
			if(form instanceof MetasPYPForm)
			{ 

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				MetasPYPForm metasForm =(MetasPYPForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=metasForm.getEstado(); 
				logger.warn("estado MetasPYPAction-->"+estado);


				if(estado == null)
				{
					metasForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Metas PYP (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				//*******ESTADOS DE LA VISTA DE PARAMETRIZACION DE ACTIVIDADES*****************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,metasForm,mapping,usuario,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,metasForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,metasForm,response,mapping,request);
				}
				//***************************************************************************************************************
				//********ESTADOS DEL POPUP DE PARAMETRIZACION DE CENTROS DE ATENCION POR ACTIVIDAD******************************
				else if (estado.equals("empezarCA"))
				{
					return accionEmpezarCA(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("nuevoCA"))
				{
					return accionNuevoCA(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("guardarCA"))
				{
					return accionGuardarCA(con,metasForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminarCA"))
				{
					return accionEliminarCA(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("ordenarCA"))
				{
					return accionOrdenarCA(con,metasForm,mapping);
				}
				else if (estado.equals("redireccionCA"))
				{
					return accionRedireccionCA(con,metasForm,response,mapping,request);
				}
				//****************************************************************************************************************
				//*****ESTADOS DEL POPUP DE PARAMETRIZACIÓN DE OCUPACIONES MÉDICAS POR CENTRO ATENCIÓN*************************++
				else if (estado.equals("empezarOM"))
				{
					return accionEmpezarOM(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("nuevoOM"))
				{
					return accionNuevoOM(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("guardarOM"))
				{
					return accionGuardarOM(con,metasForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminarOM"))
				{
					return accionEliminarOM(con,metasForm,mapping,usuario);
				}
				else if (estado.equals("ordenarOM"))
				{
					return accionOrdenarOM(con,metasForm,mapping);
				}
				else if (estado.equals("redireccionOM"))
				{
					return accionRedireccionOM(con,metasForm,response,mapping,request);
				}
				//***************************************************************************************************************
				else
				{
					metasForm.reset();
					logger.warn("Estado no valido dentro del flujo de MetasPYPAction (null) ");
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
	 * Método implementado para paginar el listado de ocupaciones médicas por centro atención, actividad,
	 * programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccionOM(Connection con, MetasPYPForm metasForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
		    this.cerrarConexion(con);
			response.sendRedirect(metasForm.getLinkSiguienteOM());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccionOM de MetasPYPAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en MetasPYPAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que ordena el listado de ocupaciones médicas por centro atención, actividad,
	 * programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarOM(Connection con, MetasPYPForm metasForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"consecutivo_",
				"codigo_ocupacion_medica_",
				"nombre_ocupacion_medica_",
				"numero_actividades_"
			};

		//Actualización del nombre de la ocupacion médica
		String aux = "";
		for(int i=0;i<metasForm.getNumOcupaciones();i++)
		{
			aux = metasForm.getOcupaciones("codigo_ocupacion_medica_"+i).toString();
			if(!aux.equals(""))
				metasForm.setOcupaciones("nombre_ocupacion_medica_"+i,Utilidades.obtenerNombreOcupacionMedica(con,Integer.parseInt(aux)));
			else
				metasForm.setOcupaciones("nombre_ocupacion_medica_"+i,"Seleccione");
				
		}
		
		metasForm.setOcupaciones(Listado.ordenarMapa(indices,
				metasForm.getIndiceOM(),
				metasForm.getUltimoIndiceOM(),
				metasForm.getOcupaciones(),
				metasForm.getNumOcupaciones()));
		
		metasForm.setOcupaciones("numRegistros",metasForm.getNumOcupaciones());
		
		metasForm.setUltimoIndiceOM(metasForm.getIndiceOM());
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleOcupacion");
	}

	/**
	 * Método implementado para eliminar una ocupación médica por centro atención, actividad,
	 * programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarOM(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		//se toma posición del registro a eliminar
		int pos = metasForm.getPosicionOM();
		int resp = 1;
		String auxS0 = metasForm.getOcupaciones("consecutivo_"+pos).toString();
		
		
		
		//se verifica si el registro existe en la BD
		if(!auxS0.equals(""))
		{
			//Se consulta registro dela BD
			metas.clean();
			metas.setCampos("consecutivoAxioma",auxS0);
			HashMap registroDB = metas.consultarOcupaciones(con);
			
			//El registro existe por tal motivo se debe eliminar
			this.llenarMundoOM(metas,metasForm,pos);
			resp = metas.eliminar(con);
			if(resp>0)
				this.generarLogOM(null,metasForm,pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
		}
		
		//si el proceso es exitoso entonces se elimina del mapa y se envía el LOG
		if(resp>0)
		{
			//Se borra el registro del mapa
			for(int i=pos;i<(metasForm.getNumOcupaciones()-1);i++)
			{
				metasForm.setOcupaciones("consecutivo_"+i,metasForm.getOcupaciones("consecutivo_"+(i+1)));
				metasForm.setOcupaciones("codigo_ocupacion_medica_"+i,metasForm.getOcupaciones("codigo_ocupacion_medica_"+(i+1)));
				metasForm.setOcupaciones("nombre_ocupacion_medica_"+i,metasForm.getOcupaciones("nombre_ocupacion_medica_"+(i+1)));
				metasForm.setOcupaciones("numero_actividades_"+i,metasForm.getOcupaciones("numero_actividades_"+(i+1)));
			}
			
			//se elimina ultima posicion
			pos = metasForm.getNumOcupaciones();
			pos--;
			
			metasForm.getOcupaciones().remove("consecutivo_"+pos);
			metasForm.getOcupaciones().remove("codigo_ocupacion_medica_"+pos);
			metasForm.getOcupaciones().remove("nombre_ocupacion_medica_"+pos);
			metasForm.getOcupaciones().remove("numero_actividades_"+pos);
			
			metasForm.setNumOcupaciones(pos);
			metasForm.setOcupaciones("numRegistros",pos);
			metasForm.setEstado("guardarOM");
			
			//Se calcula el total de actividades
			int total = 0;
			for(int i=0;i<metasForm.getNumOcupaciones();i++)
			{
				int cantidad = 0;
				try
				{
					cantidad = Integer.parseInt(metasForm.getOcupaciones("numero_actividades_"+i).toString());
				}
				catch(Exception e)
				{
					cantidad = 0;
				}
				total += cantidad;
			}
			metasForm.setTotalActividadesOM(total);
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Error al eliminar",new ActionMessage("error.pyp.metasPYP.errorOcupacion","al eliminar",metasForm.getOcupaciones("nombre_ocupacion_medica_"+pos)));
			metasForm.setEstado("empezarOM");
		}
		
		//se verifica el offset
		if(metasForm.getNumOcupaciones()==metasForm.getOffsetOM()&&metasForm.getOffsetOM()>0)
		{
			int offset = metasForm.getOffsetOM();
			offset = offset - metasForm.getMaxPageItems();
			metasForm.setOffsetOM(offset);
		}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleOcupacion");
	}

	/**
	 * Método que ingresa/modifica un registro de ocupaciones médicas por centro atencion, actividad,
	 * programa, convenio y año.
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarOM(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();
		
		//***********SE REALIZAN LAS VALIDACIONES***********************************************
		errores = validacionesOcupaciones(errores,metasForm);
		//**************************************************************************************
		
		//Se verifica que no hayan errores en la validación
		if(errores.isEmpty())
		{
			//Se instancia objeto de MetasPYP
			MetasPYP metas = new MetasPYP();
			
			for(int i=0;i<metasForm.getNumOcupaciones();i++)
			{
				auxS0 = metasForm.getOcupaciones("consecutivo_"+i).toString();
				
				//Se consulta el nombre de la ocupación médica
				
				metasForm.setOcupaciones("nombre_ocupacion_medica_"+i,Utilidades.obtenerNombreOcupacionMedica(con,Integer.parseInt(metasForm.getOcupaciones("codigo_ocupacion_medica_"+i).toString())));
				
				//Se verifica estado del registro
				if(auxS0.equals(""))
				{
					//******INSERCIÓN DE REGISTRO *********************************
					this.llenarMundoOM(metas,metasForm,i);
					resp = metas.insertarOcupaciones(con);
					if(resp<=0)
						errores.add("Error insertando ocupación médica",new ActionMessage("error.pyp.metasPYP.errorOcupacion","al ingresar",metasForm.getOcupaciones("nombre_ocupacion_medica_"+i)));
					else
						metasForm.setOcupaciones("consecutivo_"+i,resp);
					//*************************************************************
				}
				else
				{
					//*****MODIFICACION DE REGISTRO******************************
					//Se consulta registro dela BD
					metas.clean();
					metas.setCampos("consecutivoAxioma",metasForm.getOcupaciones("consecutivo_"+i));
					HashMap registroDB = metas.consultarOcupaciones(con);
					
					
					this.llenarMundoOM(metas,metasForm,i);
					
					//Se verifica si el registro fue modificado
					if(this.fueModificadoOM(registroDB,metasForm,i))
					{
						resp = metas.modificar(con);
						if(resp<=0)
							errores.add("Error modificando ocupación médica",new ActionMessage("error.pyp.metasPYP.errorOcupacion","al modificar",metasForm.getOcupaciones("nombre_ocupacion_medica_"+i)));
						else
							this.generarLogOM(registroDB,metasForm,i,ConstantesBD.tipoRegistroLogModificacion,usuario);
					}
					//***********************************************************
				}
			}
		}
		
		if(errores.isEmpty())
		{
			metasForm.setEstado("guardarOM");
		}
		else
		{
			saveErrors(request,errores);
			metasForm.setEstado("empezarOM");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleOcupacion");
	}

	/**
	 * Metodo implementado para generar el LOG archivos de insercion/modificacion para registros de ocupaciones médicas
	 * por centro atención, actividad, programa, convenio y año
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @param tipo
	 * @param usuario
	 */
	private void generarLogOM(HashMap registroDB, MetasPYPForm metasForm, int pos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    int posR = metasForm.getPosicion(); //posicion del mapa donde se encuntra la actividad del centro de atencion
	    int posCA = metasForm.getPosicionCA(); //posicion del mapa donde se encuentra el centro de atencion de la ocupacion
		
		log += "\n=========================INFORMACIÓN ACTIVIDAD (OCUPACIONES MÉDICAS)============================== " +
			"\n*  Convenio [" +metasForm.getListado("nombre_convenio_"+posR)+"] "+
		    "\n*  Año [" +metasForm.getListado("anio_"+posR)+"] "+
			"\n*  Programa ["+metasForm.getListado("nombre_programa_"+posR)+"] " +
			"\n*  Actividad ["+metasForm.getListado("descripcion_actividad_"+posR)+"] " +
			"\n*  Número Actividades [" + metasForm.getListado("numero_actividades_"+posR) +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] "+
		    "\n*  Centro Atencion [" +metasForm.getCentrosAtencion("nombre_centro_atencion_"+posCA)+"] "+
		    "\n*  Número Actividades del Centro Atención [" + metasForm.getCentrosAtencion("numero_actividades_"+posCA) +"]";
		
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log+="\n            ====INFORMACION ORIGINAL METAS PYP (OCUPACIONES MÉDICAS)===== " +
		    "\n*  Consecutivo [" +registroDB.get("consecutivo_0")+"] "+
		    "\n*  Ocupación Médica [" +registroDB.get("nombre_ocupacion_medica_0")+"] "+
			"\n*  Número Actividades [" + registroDB.get("numero_actividades_0") +"]";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN METAS PYP (OCUPACIONES MÉDICAS)===== " +
		    "\n*  Consecutivo [" +metasForm.getOcupaciones("consecutivo_"+pos)+"] "+
		    "\n*  Ocupación Médica [" +metasForm.getOcupaciones("nombre_ocupacion_medica_"+pos)+"] "+
			"\n*  Número Actividades [" + metasForm.getOcupaciones("numero_actividades_"+pos) +"]";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log+="\n            ====INFORMACION ELIMINADA DE METAS PYP (OCUPACIONES MÉDICAS)===== " +
		    "\n*  Consecutivo [" +metasForm.getOcupaciones("consecutivo_"+pos)+"] "+
		    "\n*  Ocupación Médica [" +metasForm.getOcupaciones("nombre_ocupacion_medica_"+pos)+"] "+
			"\n*  Número Actividades [" + metasForm.getOcupaciones("numero_actividades_"+pos) +"]";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logMetasPYPCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método implementado para verificar si un registro de ocupaciones médicas fue
	 * modificado con respecto a la BD
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @return
	 */
	private boolean fueModificadoOM(HashMap registroDB, MetasPYPForm metasForm, int pos) 
	{
		boolean fueModificado = false;
		
		//se verifica ocupacion médica
		String aux0 = registroDB.get("codigo_ocupacion_medica_0").toString();
		String aux1 = metasForm.getOcupaciones("codigo_ocupacion_medica_"+pos).toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		//se verifica numero de actividades
		aux0 = registroDB.get("numero_actividades_0").toString();
		aux1 = metasForm.getOcupaciones("numero_actividades_"+pos).toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		return fueModificado;
	}

	/**
	 * Método implementado para cargar el mundo con datos de la forma 
	 * sobre informacion relacionada con las ocupaciones médocas por centro atención,
	 * actividad, programa, convenio y año.
	 * @param metas
	 * @param metasForm
	 * @param pos
	 */
	private void llenarMundoOM(MetasPYP metas, MetasPYPForm metasForm, int pos) 
	{
		metas.setCampos("consecutivo",metasForm.getOcupaciones("consecutivo_"+pos));
		metas.setCampos("numeroActividades",metasForm.getOcupaciones("numero_actividades_"+pos));
		metas.setCampos("ocupacionMedica",metasForm.getOcupaciones("codigo_ocupacion_medica_"+pos));
		metas.setCampos("codigoMetaCAPYP",metasForm.getCentrosAtencion("consecutivo_"+metasForm.getPosicionCA()));
		metas.setCampos("tipo","ocupacionMedica");
		
	}

	/**
	 * Método que realiza las validaciones de ocupaciones médicas antes de insertar/modificar
	 * @param errores
	 * @param metasForm
	 * @return
	 */
	private ActionErrors validacionesOcupaciones(ActionErrors errores, MetasPYPForm metasForm) 
	{
		String aux = "";
		
		//1) verificar campos requeridos
		for(int i=0;i<metasForm.getNumOcupaciones();i++)
		{
			//A) Ocupación Médica
			aux = metasForm.getOcupaciones("codigo_ocupacion_medica_"+i).toString();
			if(aux.equals(""))
				errores.add("Ocupación médica requerida",new ActionMessage("errors.required","La ocupación médica en el registro N° "+(i+1)));
			
			//B) Número Actividades
			aux = metasForm.getOcupaciones("numero_actividades_"+i).toString();
			if(aux.equals(""))
				errores.add("Número Actividades requerido",new ActionMessage("errors.required","El número de actividades en el registro N° "+(i+1)));
			else
			{
				try
				{
					Integer.parseInt(aux);	
				}
				catch(Exception e)
				{
					errores.add("Debe ser entero", new ActionMessage("errors.integer","El número de actividades en el registro N° "+(i+1)));
				}
			}
		}
		
		//2) Verificar ocupaciones repetidas
		if(errores.isEmpty())
		{
			// sección para validar que no hayan actividades repetidas
			String auxS1 = "";
			String auxS0 = "";
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			for(int i=0;i<metasForm.getNumOcupaciones();i++)
			{
				auxS0=metasForm.getOcupaciones("codigo_ocupacion_medica_"+i).toString().trim();
				
				for(int j=(metasForm.getNumOcupaciones()-1);j>i;j--)
				{
					
					auxS1=metasForm.getOcupaciones("codigo_ocupacion_medica_"+j).toString().trim();
					//se compara
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")
						&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0.toUpperCase()))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("Ocupaciones Médicas iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"ocupaciones médicas","en los registros Nº "+descripcion));
				}
				
				descripcion = "";
				codigosComparados.put(i+"",auxS0.toUpperCase());
				
			}
		}
		
		//3) Verificar si se excede el total de actividades
		if(errores.isEmpty())
		{
			if(metasForm.getTotalActividadesOM()>Integer.parseInt(metasForm.getCentrosAtencion("numero_actividades_"+metasForm.getPosicionCA()).toString()))
				errores.add("Total Actividades menor a numero actividades de centro atención",
					new ActionMessage("errors.MenorIgualQue","El total de actividades","el número de actividades del centro atención seleccionado"));
					
		}
		
		
		return errores;
	}

	/**
	 * Método implementado para agregar un nuevo registro de ocupacion médica por centro atencion,
	 * actividad, programa, convenio y año.
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoOM(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = metasForm.getNumOcupaciones();
		metasForm.setOcupaciones("consecutivo_"+pos,"");
		metasForm.setOcupaciones("codigo_ocupacion_medica_"+pos,"");
		metasForm.setOcupaciones("nombre_ocupacion_medica_"+pos,"");
		metasForm.setOcupaciones("numero_actividades_"+pos,"");
		
		pos++;
		metasForm.setOcupaciones("numRegistros",pos);
		metasForm.setNumOcupaciones(pos);
		
		//se ubica el pager en el último registro
		if(metasForm.getNumOcupaciones()>metasForm.getMaxPageItems())
		{
			int numReg = metasForm.getNumOcupaciones();
			int maxPag = metasForm.getMaxPageItems();
			metasForm.setOffsetOM(numReg-(numReg%maxPag==0?maxPag:numReg%maxPag));
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleOcupacion");
	}

	/**
	 * Método implementado para postular la vista inicial de parametrizacion de ocupaciones médicas por centro atencion,
	 * actividad, programa, convenio y año específicos
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarOM(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		metasForm.resetOM();
		
		//Se instancia mundo de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		//se toma la posicion de la actividad elegida en el mapa
		int pos = metasForm.getPosicionCA();
		
		//Se consultan las ocupaciones previamente parametrizadas
		metas.setCampos("consecutivo",metasForm.getCentrosAtencion("consecutivo_"+pos));
		metasForm.setOcupaciones(metas.consultarOcupaciones(con));
		metasForm.setNumOcupaciones(Integer.parseInt(metasForm.getOcupaciones("numRegistros").toString()));
		
		//Se calcula el total de actividades
		int total = 0;
		for(int i=0;i<metasForm.getNumOcupaciones();i++)
			total += Integer.parseInt(metasForm.getOcupaciones("numero_actividades_"+i).toString());
		metasForm.setTotalActividadesOM(total);
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleOcupacion");
	}


	/**
	 * Método implementado para paginar el listado de centros de costo de una actividad, programa
	 * convenio y año específicos
	 * @param con
	 * @param metasForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccionCA(Connection con, MetasPYPForm metasForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(metasForm.getLinkSiguienteCA());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccionCA de MetasPYPAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en MetasPYPAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para ordenar el listado de centros de atencion de una actividad, programa
	 * ,convenio y año específicos
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarCA(Connection con, MetasPYPForm metasForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"consecutivo_",
				"codigo_centro_atencion_",
				"nombre_centro_atencion_",
				"numero_actividades_",
				"es_usado_"
			};

		
		//Actualización del nombre del centro de atencion
		String aux = "";
		for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
		{
			aux = metasForm.getCentrosAtencion("codigo_centro_atencion_"+i).toString();
			if(!aux.equals(""))
				metasForm.setCentrosAtencion("nombre_centro_atencion_"+i,Utilidades.obtenerNombreCentroAtencion(con,Integer.parseInt(aux)));
			else
				metasForm.setCentrosAtencion("nombre_centro_atencion_"+i,"Seleccione");		
		}
		
		metasForm.setCentrosAtencion(Listado.ordenarMapa(indices,
				metasForm.getIndiceCA(),
				metasForm.getUltimoIndiceCA(),
				metasForm.getCentrosAtencion(),
				metasForm.getNumCentrosAtencion()));
		
		metasForm.setCentrosAtencion("numRegistros",metasForm.getNumCentrosAtencion());
		
		metasForm.setUltimoIndiceCA(metasForm.getIndiceCA());
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleCentroAtencion");
	}

	/**
	 * Método implementado para eliminar un centro de atencion perteneciente a una
	 * actividad, programa , convenio y año específico
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarCA(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		//se toma posición del registro a eliminar
		int pos = metasForm.getPosicionCA();
		int resp = 1;
		String auxS0 = metasForm.getCentrosAtencion("consecutivo_"+pos).toString();
		
		
		
		//se verifica si el registro existe en la BD
		if(!auxS0.equals(""))
		{
			//Se consulta registro dela BD
			metas.clean();
			metas.setCampos("consecutivoAxioma",auxS0);
			HashMap registroDB = metas.consultarCentrosAtencion(con);
			
			//El registro existe por tal motivo se debe eliminar
			this.llenarMundoCA(metas,metasForm,pos);
			resp = metas.eliminar(con);
			if(resp>0)
				this.generarLogCA(null,metasForm,pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			else
				metasForm.setCentrosAtencion("es_usado_"+pos,registroDB.get("es_usado_0"));
		}
		
		//si el proceso es exitoso entonces se elimina del mapa y se envía el LOG
		if(resp>0)
		{
			//Se borra el registro del mapa
			for(int i=pos;i<(metasForm.getNumCentrosAtencion()-1);i++)
			{
				metasForm.setCentrosAtencion("consecutivo_"+i,metasForm.getCentrosAtencion("consecutivo_"+(i+1)));
				metasForm.setCentrosAtencion("codigo_centro_atencion_"+i,metasForm.getCentrosAtencion("codigo_centro_atencion_"+(i+1)));
				metasForm.setCentrosAtencion("nombre_centro_atencion_"+i,metasForm.getCentrosAtencion("nombre_centro_atencion_"+(i+1)));
				metasForm.setCentrosAtencion("numero_actividades_"+i,metasForm.getCentrosAtencion("numero_actividades_"+(i+1)));
				metasForm.setCentrosAtencion("es_usado_"+i,metasForm.getCentrosAtencion("es_usado_"+(i+1)));
				
			}
			
			//se elimina ultima posicion
			pos = metasForm.getNumCentrosAtencion();
			pos--;
			
			metasForm.getCentrosAtencion().remove("consecutivo_"+pos);
			metasForm.getCentrosAtencion().remove("codigo_centro_atencion_"+pos);
			metasForm.getCentrosAtencion().remove("nombre_centro_atencion_"+pos);
			metasForm.getCentrosAtencion().remove("numero_actividades_"+pos);
			metasForm.getCentrosAtencion().remove("es_usado_"+pos);
			
			metasForm.setNumCentrosAtencion(pos);
			metasForm.setCentrosAtencion("numRegistros",pos);
			metasForm.setEstado("guardarCA");
			
			//Se calcula el total de actividades
			int total = 0;
			for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
			{
				int cantidad = 0;
				try
				{
					cantidad = Integer.parseInt(metasForm.getCentrosAtencion("numero_actividades_"+i).toString()); 
				}
				catch(Exception e)
				{
					cantidad = 0;
				}
				total += cantidad;
			}
			metasForm.setTotalActividadesCA(total);
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Error al eliminar",new ActionMessage("error.pyp.metasPYP.errorCentroAtencion","al eliminar",metasForm.getCentrosAtencion("nombre_centro_atencion_"+pos)));
			metasForm.setEstado("empezarCA");
		}
		
		//se verifica el offset
		if(metasForm.getNumCentrosAtencion()==metasForm.getOffsetCA()&&metasForm.getOffsetCA()>0)
		{
			int offset = metasForm.getOffsetCA();
			offset = offset - metasForm.getMaxPageItems();
			metasForm.setOffsetCA(offset);
		}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleCentroAtencion");
	}

	/**
	 * Método implementado para ingresar/modificar centros de atencion por actividad, programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarCA(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();
		
		//***********SE REALIZAN LAS VALIDACIONES***********************************************
		errores = validacionesCentrosAtencion(errores,metasForm);
		//**************************************************************************************
		
		//Se verifica que no hayan errores en la validación
		if(errores.isEmpty())
		{
			//Se instancia objeto de MetasPYP
			MetasPYP metas = new MetasPYP();
			
			for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
			{
				auxS0 = metasForm.getCentrosAtencion("consecutivo_"+i).toString();
				
				//Se consulta el nombre del centro de atencion
				metasForm.setCentrosAtencion("nombre_centro_atencion_"+i,Utilidades.obtenerNombreCentroAtencion(con,Integer.parseInt(metasForm.getCentrosAtencion("codigo_centro_atencion_"+i).toString())));
				
				//Se verifica estado del registro
				if(auxS0.equals(""))
				{
					//******INSERCIÓN DE REGISTRO *********************************
					this.llenarMundoCA(metas,metasForm,i);
					resp = metas.insertarCentrosAtencion(con);
					if(resp<=0)
						errores.add("Error insertando centro atención",new ActionMessage("error.pyp.metasPYP.errorCentroAtencion","al ingresar",metasForm.getCentrosAtencion("nombre_centro_atencion_"+i)));
					else
						metasForm.setCentrosAtencion("consecutivo_"+i,resp);
					//*************************************************************
				}
				else
				{
					//*****MODIFICACION DE REGISTRO******************************
					//Se consulta registro dela BD
					metas.clean();
					metas.setCampos("consecutivoAxioma",metasForm.getCentrosAtencion("consecutivo_"+i));
					HashMap registroDB = metas.consultarCentrosAtencion(con);
					
					
					this.llenarMundoCA(metas,metasForm,i);
					
					//Se verifica si el registro fue modificado
					if(this.fueModificadoCA(registroDB,metasForm,i))
					{
						resp = metas.modificar(con);
						if(resp<=0)
							errores.add("Error modificando centro de atención",new ActionMessage("error.pyp.metasPYP.errorCentroAtencion","al modificar",metasForm.getCentrosAtencion("nombre_centro_atencion_"+i)));
						else
							this.generarLogCA(registroDB,metasForm,i,ConstantesBD.tipoRegistroLogModificacion,usuario);
					}
					//***********************************************************
				}
			}
		}
		
		if(errores.isEmpty())
		{
			metasForm.setEstado("guardarCA");
		}
		else
		{
			saveErrors(request,errores);
			metasForm.setEstado("empezarCA");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleCentroAtencion");
	}


	/**
	 * Método que realiza las validaciones de los centros de atención
	 * @param errores
	 * @param metasForm
	 * @return
	 */
	private ActionErrors validacionesCentrosAtencion(ActionErrors errores, MetasPYPForm metasForm) 
	{
		String aux = "";
		
		//1) verificar campos requeridos
		for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
		{
			//A) Centro Atencion
			aux = metasForm.getCentrosAtencion("codigo_centro_atencion_"+i).toString();
			if(aux.equals(""))
				errores.add("Centro de Atencion requerido",new ActionMessage("errors.required","El centro de atención en el registro N° "+(i+1)));
			
			//B) Número Actividades
			aux = metasForm.getCentrosAtencion("numero_actividades_"+i).toString();
			if(aux.equals(""))
				errores.add("Número Actividades requerido",new ActionMessage("errors.required","El número de actividades en el registro N° "+(i+1)));
			else
			{
				try
				{
					Integer.parseInt(aux);	
				}
				catch(Exception e)
				{
					errores.add("Debe ser entero", new ActionMessage("errors.integer","El número de actividades en el registro N° "+(i+1)));
				}
			}
		}
		
		//2) Verificar centros de atención repetidos
		if(errores.isEmpty())
		{
			// sección para validar que no hayan actividades repetidas
			String auxS1 = "";
			String auxS0 = "";
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
			{
				auxS0=metasForm.getCentrosAtencion("codigo_centro_atencion_"+i).toString().trim();
				
				for(int j=(metasForm.getNumCentrosAtencion()-1);j>i;j--)
				{
					
					auxS1=metasForm.getCentrosAtencion("codigo_centro_atencion_"+j).toString().trim();
					//se compara
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")
						&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0.toUpperCase()))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("Centros de Atención iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"centros de atención","en los registros Nº "+descripcion));
				}
				
				descripcion = "";
				codigosComparados.put(i+"",auxS0.toUpperCase());
				
			}
		}
		
		//3) Verificar si se excede el total de actividades
		if(errores.isEmpty())
		{
			if(metasForm.getTotalActividadesCA()>Integer.parseInt(metasForm.getListado("numero_actividades_"+metasForm.getPosicion()).toString()))
				errores.add("Total Actividades menor a numero actividades de actividad",
					new ActionMessage("errors.MenorIgualQue","El total de actividades","el número de actividades del convenio, programa y actividad seleccionada"));
					
		}
		
		
		return errores;
	}

	/**
	 * Método implementado para generar el LOG modificacion/eliminacion de los registros de centro de atencion
	 * por actividad, programa, convenio y año.
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @param tipoRegistroLogModificacion
	 * @param usuario
	 */
	private void generarLogCA(HashMap registroDB, MetasPYPForm metasForm, int pos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    int posR = metasForm.getPosicion(); //posicion del mapa donde se encuntra la actividad del centro de atencion
		
		log += "\n=========================INFORMACIÓN ACTIVIDAD (CENTROS DE ATENCIÓN)============================== " +
			"\n*  Convenio [" +metasForm.getListado("nombre_convenio_"+posR)+"] "+
		    "\n*  Año [" +metasForm.getListado("anio_"+posR)+"] "+
			"\n*  Programa ["+metasForm.getListado("nombre_programa_"+posR)+"] " +
			"\n*  Actividad ["+metasForm.getListado("descripcion_actividad_"+posR)+"] " +
			"\n*  Número Actividades [" + metasForm.getListado("numero_actividades_"+posR) +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";
		
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log+="\n            ====INFORMACION ORIGINAL METAS PYP (CENTROS DE ATENCIÓN)===== " +
		    "\n*  Consecutivo [" +registroDB.get("consecutivo_0")+"] "+
		    "\n*  Centro Atencion [" +registroDB.get("nombre_centro_atencion_0")+"] "+
			"\n*  Número Actividades [" + registroDB.get("numero_actividades_0") +"]";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN METAS PYP (CENTROS ATENCIÓN)===== " +
		    "\n*  Consecutivo [" +metasForm.getCentrosAtencion("consecutivo_"+pos)+"] "+
		    "\n*  Centro Atención [" +metasForm.getCentrosAtencion("nombre_centro_atencion_"+pos)+"] "+
			"\n*  Número Actividades [" + metasForm.getCentrosAtencion("numero_actividades_"+pos) +"]";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log+="\n            ====INFORMACION ELIMINADA DE METAS PYP (CENTROS ATENCIÓN)===== " +
		    "\n*  Consecutivo [" +metasForm.getCentrosAtencion("consecutivo_"+pos)+"] "+
		    "\n*  Centro Atención [" +metasForm.getCentrosAtencion("nombre_centro_atencion_"+pos)+"] "+
			"\n*  Número Actividades [" + metasForm.getCentrosAtencion("numero_actividades_"+pos) +"]";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logMetasPYPCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método que verifica si un registro de centro de atencion fue modificado
	 * respecto a la BD
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @return
	 */
	private boolean fueModificadoCA(HashMap registroDB, MetasPYPForm metasForm, int pos) 
	{
		boolean fueModificado = false;
		
		//Se verifica el centro de atencion
		String aux0 = registroDB.get("codigo_centro_atencion_0").toString();
		String aux1 = metasForm.getCentrosAtencion("codigo_centro_atencion_"+pos).toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		//Se verifica numero de actividades
		aux0 = registroDB.get("numero_actividades_0").toString();
		aux1 = metasForm.getCentrosAtencion("numero_actividades_"+pos).toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		return fueModificado;
	}

	/**
	 * Método que carga el mundo con los datos de la forma relacionada con la parametrizacion
	 * de los centros de atención
	 * @param metas
	 * @param metasForm
	 * @param pos
	 */
	private void llenarMundoCA(MetasPYP metas, MetasPYPForm metasForm, int pos) 
	{
		metas.setCampos("consecutivo",metasForm.getCentrosAtencion("consecutivo_"+pos));
		metas.setCampos("numeroActividades",metasForm.getCentrosAtencion("numero_actividades_"+pos));
		metas.setCampos("centroAtencion",metasForm.getCentrosAtencion("codigo_centro_atencion_"+pos));
		metas.setCampos("codigoMetaPYP",metasForm.getListado("consecutivo_"+metasForm.getPosicion()));
		metas.setCampos("tipo","centroAtencion");
	}

	/**
	 * Método implementado para postular un nuevo registro de centro de atencion, por actividad, programa. convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoCA(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = metasForm.getNumCentrosAtencion();
		metasForm.setCentrosAtencion("consecutivo_"+pos,"");
		metasForm.setCentrosAtencion("codigo_centro_atencion_"+pos,"");
		metasForm.setCentrosAtencion("nombre_centro_atencion_"+pos,"");
		metasForm.setCentrosAtencion("numero_actividades_"+pos,"");
		metasForm.setCentrosAtencion("es_usado_"+pos,"false");
		
		pos++;
		metasForm.setCentrosAtencion("numRegistros",pos);
		metasForm.setNumCentrosAtencion(pos);
		
		//se ubica el pager en el último registro
		if(metasForm.getNumCentrosAtencion()>metasForm.getMaxPageItems())
		{
			int numReg = metasForm.getNumCentrosAtencion();
			int maxPag = metasForm.getMaxPageItems();
			metasForm.setOffsetCA(numReg-(numReg%maxPag==0?maxPag:numReg%maxPag));
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleCentroAtencion");
	}

	/**
	 * Método implementado para postular el inicio de la parametrizacion de numero de actividades
	 * para cada centro de atención dentro de una actividad
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarCA(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		metasForm.resetCA();
		
		//Se instancia mundo de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		//se toma la posicion de la actividad elegida en el mapa
		int pos = metasForm.getPosicion();
		
		//Se consultan los centros de atencion previamente parametrizados
		metas.setCampos("consecutivo",metasForm.getListado("consecutivo_"+pos));
		metasForm.setCentrosAtencion(metas.consultarCentrosAtencion(con));
		metasForm.setNumCentrosAtencion(Integer.parseInt(metasForm.getCentrosAtencion("numRegistros").toString()));
		
		//Se calcula el total de actividades
		int total = 0;
		for(int i=0;i<metasForm.getNumCentrosAtencion();i++)
			total += Integer.parseInt(metasForm.getCentrosAtencion("numero_actividades_"+i).toString());
		metasForm.setTotalActividadesCA(total);
		
		this.cerrarConexion(con);
		return mapping.findForward("detalleCentroAtencion");
	}

	/**
	 * Método implementado para paginar un listado de actividades, por programa, convenio, año
	 * @param con
	 * @param metasForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, MetasPYPForm metasForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(metasForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de MetasPYPAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en MetasPYPAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para ordenar el listado de actividades, por programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, MetasPYPForm metasForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"consecutivo_",
				"anio_",
				"codigo_papc_",
				"numero_actividades_",
				"institucion_",
				"codigo_convenio_",
				"nombre_convenio_",
				"codigo_app_",
				"codigo_programa_",
				"nombre_programa_",
				"codigo_actividad_",
				"descripcion_actividad_"
			};

		metasForm.setListado(Listado.ordenarMapa(indices,
				metasForm.getIndice(),
				metasForm.getUltimoIndice(),
				metasForm.getListado(),
				metasForm.getNumRegistros()));
		
		metasForm.setListado("numRegistros",metasForm.getNumRegistros());
		
		metasForm.setUltimoIndice(metasForm.getIndice());
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para insertar / modificar registros de actividades por programa, convenio, año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();
		
		
		//Se instancia objeto de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		for(int i=0;i<metasForm.getNumRegistros();i++)
		{
			auxS0 = metasForm.getListado("consecutivo_"+i).toString();
			
			//Se verifica estado del registro
			if(auxS0.equals(""))
			{
				//******INSERCIÓN DE REGISTRO *********************************
				this.llenarMundo(metas,metasForm,i);
				resp = metas.insertarActividades(con);
				if(resp<=0)
					errores.add("Error insertando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al ingresar",metasForm.getListado("descripcion_actividad_"+i)));
				else
					metasForm.setListado("consecutivo_"+i,resp);
				//*************************************************************
			}
			else
			{
				//*****MODIFICACION DE REGISTRO******************************
				//Se consulta registro dela BD
				metas.clean();
				metas.setCampos("consecutivo",metasForm.getListado("consecutivo_"+i));
				HashMap registroDB = metas.consultarActividades(con);
				
				
				this.llenarMundo(metas,metasForm,i);
				
				//Se verifica si el registro fue modificado
				if(this.fueModificado(registroDB,metasForm,i))
				{
					//se llenan datos faltantes
					metasForm.setListado("nombre_convenio_"+i,registroDB.get("nombre_convenio_0"));
					metasForm.setListado("nombre_programa_"+i,registroDB.get("nombre_programa_0"));
					
					resp = metas.modificar(con);
					if(resp<=0)
						errores.add("Error modificando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al modificar",metasForm.getListado("descripcion_actividad_"+i)));
					else
						this.generarLog(registroDB,metasForm,i,ConstantesBD.tipoRegistroLogModificacion,usuario);
				}
				//***********************************************************
			}
		}
		
		if(errores.isEmpty())
		{
			metasForm.setEstado("guardar");
		}
		else
		{
			saveErrors(request,errores);
			metasForm.setEstado("parametrizar");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * Método implementado para generar el LOG archivo de eliminacion/insercion de las actividades, por programa, convenio y año
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(HashMap registroDB, MetasPYPForm metasForm, int pos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL METAS PYP (ACTIVIDADES)===== " +
		    "\n*  Consecutivo [" +registroDB.get("consecutivo_0")+"] "+
		    "\n*  Convenio [" +registroDB.get("nombre_convenio_0")+"] "+
		    "\n*  Año [" +registroDB.get("anio_0")+"] "+
			"\n*  Programa ["+registroDB.get("nombre_programa_0")+"] " +
			"\n*  Actividad ["+registroDB.get("descripcion_actividad_0")+"] " +
			"\n*  Número Actividades [" + registroDB.get("numero_actividades_0") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN METAS PYP (ACTIVIDADES)===== " +
		    "\n*  Consecutivo [" +metasForm.getListado("consecutivo_"+pos)+"] "+
		    "\n*  Convenio [" +metasForm.getListado("nombre_convenio_"+pos)+"] "+
		    "\n*  Año [" +metasForm.getListado("anio_"+pos)+"] "+
			"\n*  Programa ["+metasForm.getListado("nombre_programa_"+pos)+"] " +
			"\n*  Actividad ["+metasForm.getListado("descripcion_actividad_"+pos)+"] " +
			"\n*  Número Actividades [" + metasForm.getListado("numero_actividades_"+pos) +"]"+
			"\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE METAS PYP (ACTIVIDADES)===== " +
		    "\n*  Consecutivo [" +metasForm.getListado("consecutivo_"+pos)+"] "+
		    "\n*  Convenio [" +metasForm.getListado("nombre_convenio_"+pos)+"] "+
		    "\n*  Año [" +metasForm.getListado("anio_"+pos)+"] "+
			"\n*  Programa ["+metasForm.getListado("nombre_programa_"+pos)+"] " +
			"\n*  Actividad ["+metasForm.getListado("descripcion_actividad_"+pos)+"] " +
			"\n*  Número Actividades [" + metasForm.getListado("numero_actividades_"+pos) +"]"+
			"\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logMetasPYPCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método implementado para verificar si un registro fue modificado con respecto a la BD
	 * @param registroDB
	 * @param metasForm
	 * @param pos
	 * @return
	 */
	private boolean fueModificado(HashMap registroDB, MetasPYPForm metasForm, int pos) 
	{
		boolean fueModificado = false;
		String aux0 = "";
		String aux1 = "";
		
		//se verifica la actividad
		aux0 = metasForm.getListado("codigo_papc_"+pos).toString();
		aux1 = registroDB.get("codigo_papc_0").toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		//se verifica el número de actividades
		aux0 = metasForm.getListado("numero_actividades_"+pos).toString();
		aux1 = registroDB.get("numero_actividades_0").toString();
		if(!aux0.equals(aux1))
			fueModificado = true;
		
		
		return fueModificado;
	}

	/**
	 * Método implementado para cargar el mundo de MetasPYP con los datos de la forma
	 * @param metas
	 * @param metasForm
	 * @param pos
	 */
	private void llenarMundo(MetasPYP metas, MetasPYPForm metasForm, int pos) 
	{
		metas.setCampos("consecutivo",metasForm.getListado("consecutivo_"+pos));
		metas.setCampos("anio",metasForm.getListado("anio_"+pos));
		metas.setCampos("institucion",metasForm.getListado("institucion_"+pos));
		metas.setCampos("convenio",metasForm.getListado("codigo_convenio_"+pos));
		metas.setCampos("programa",metasForm.getListado("codigo_programa_"+pos));
		metas.setCampos("codigoProgramaActividadConvenio",metasForm.getListado("codigo_papc_"+pos));
		metas.setCampos("numeroActividades",metasForm.getListado("numero_actividades_"+pos));
		metas.setCampos("tipo","actividad");
		
	}

	/**
	 * Método implementado para ingresar una nueva actividad por programa, convenio y año
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = metasForm.getNumRegistros();
		metasForm.setListado("consecutivo_"+pos,"");
		metasForm.setListado("anio_"+pos,metasForm.getAnio());
		metasForm.setListado("codigo_papc_"+pos,"");
		metasForm.setListado("numero_actividades_"+pos,"");
		metasForm.setListado("institucion_"+pos,metasForm.getInstitucion());
		metasForm.setListado("codigo_convenio_"+pos,metasForm.getConvenio());
		metasForm.setListado("nombre_convenio_"+pos,"");
		metasForm.setListado("codigo_app_"+pos,"");
		metasForm.setListado("codigo_programa_"+pos,metasForm.getPrograma());
		metasForm.setListado("nombre_programa_"+pos,"");
		metasForm.setListado("codigo_actividad_"+pos,"");
		metasForm.setListado("descripcion_actividad_"+pos,"");
		
		pos++;
		metasForm.setListado("numRegistros",pos);
		metasForm.setNumRegistros(pos);
		
		//se ubica el pager en el último registro
		if(metasForm.getNumRegistros()>metasForm.getMaxPageItems())
		{
			int numReg = metasForm.getNumRegistros();
			int maxPag = metasForm.getMaxPageItems();
			metasForm.setOffset(numReg-(numReg%maxPag==0?maxPag:numReg%maxPag));
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para postular la parametrización de
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionParametrizar(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Instanciar objeto de MetasPYP
		MetasPYP metas = new MetasPYP();
		
		//Se consultan los registros existentes
		metas.setCampos("anio",metasForm.getAnio());
		metas.setCampos("convenio",metasForm.getConvenio());
		metas.setCampos("programa",metasForm.getPrograma());
		metas.setCampos("institucion",metasForm.getInstitucion());
		metasForm.setListado(metas.consultarActividades(con));
		metasForm.setNumRegistros(Integer.parseInt(metasForm.getListado("numRegistros").toString()));
		
		//Se consultan las actividades del programa por convenio
		metasForm.setActividades(metas.cargarActividadesProgramaConvenio(con));
		
		//Se asigna el maxPageItems
		metasForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que postula el inicio del flujo de Metas
	 * para parametrizar el año, convenio y programa
	 * @param con
	 * @param metasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, MetasPYPForm metasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		metasForm.reset();
		metasForm.setEstado("empezar");
		metasForm.setInstitucion(usuario.getCodigoInstitucion());
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
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
