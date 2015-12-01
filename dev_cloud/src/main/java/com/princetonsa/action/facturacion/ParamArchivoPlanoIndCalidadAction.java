package com.princetonsa.action.facturacion;

//Autor Diego Fernando Bedoya

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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ParamArchivoPlanoIndCalidadForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParamArchivoPlanoIndCalidad;

public class ParamArchivoPlanoIndCalidadAction extends Action
{
	Logger logger = Logger.getLogger(ParamArchivoPlanoIndCalidadAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{
		Connection con = null;
		try{
			if(response==null);
			if(form instanceof ParamArchivoPlanoIndCalidadForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ParamArchivoPlanoIndCalidadForm forma = (ParamArchivoPlanoIndCalidadForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n ESTADO PARAMETRIZACION ARCHIVO PLANO INDICADORES DE CALIDAD---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());				
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarP"))
				{
					return this.accionEmpezarP(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarEspecialidad"))
				{
					return this.accionEmpezarES(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarCentroCosto"))
				{
					return this.accionEmpezarCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarDiagnostico"))
				{
					return this.accionEmpezarD(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarGenerales"))
				{
					return this.accionEmpezarG(forma, con, mapping, usuario);
				}
				else if(estado.equals("nuevoEspecialidad"))
				{
					return this.accionguardarNuevoES(forma, con, mapping, usuario);
				}
				else if(estado.equals("nuevoCentro"))
				{
					return this.accionguardarNuevoCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("nuevoDiagnostico"))
				{
					return this.accionguardarNuevoDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("subirEspecialidad"))
				{
					return this.accionSubirEspecialidad(forma, con, mapping, usuario);
				}
				else if(estado.equals("subirCentro"))
				{
					return this.accionSubirCentro(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreN"))
				{
					return this.accionCargarNombre(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreM"))
				{
					return this.accionCargarNombre(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreCCN"))
				{
					return this.accionCargarNombreCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreCCM"))
				{
					return this.accionCargarNombreCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreDXN"))
				{
					return this.accionCargarNombreDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNombreDXM"))
				{
					return this.accionCargarNombreDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("insertarEspecialidad"))
				{
					return this.accionInsertarEspecialidad(forma, con, mapping, usuario, request);
				}
				else if(estado.equals("insertarCentro"))
				{
					return this.accionInsertarCentro(forma, con, mapping, usuario, request);
				}
				else if(estado.equals("insertarDiagnostico"))
				{
					return this.accionInsertarDiagnostico(forma, con, mapping, usuario, request);
				}			
				else if(estado.equals("eliminarEspecialidad"))
				{
					return this.accionEliminarES(forma, con, mapping, usuario);
				}
				else if(estado.equals("eliminarCentro"))
				{
					return this.accionEliminarCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("eliminarDiagnostico"))
				{
					return this.accionEliminarDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("modificarEspecialidad"))
				{
					return this.accionModificarES(forma, con, mapping, usuario);
				}
				else if(estado.equals("modificarCentro"))
				{
					return this.accionModificarCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("modificarDiagnostico"))
				{
					return this.accionModificarDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("guardarEspecialidad"))
				{
					return this.accionGuardarModificarES(forma, con, mapping, usuario);
				}
				else if(estado.equals("guardarCentro"))
				{
					return this.accionGuardarModificarCC(forma, con, mapping, usuario);
				}
				else if(estado.equals("guardarDiagnostico"))
				{
					return this.accionGuardarModificarDX(forma, con, mapping, usuario);
				}
				else if(estado.equals("guardarSigno"))
				{
					return this.accionGuardarModificarS(forma, con, mapping, usuario, request);
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
	 * Metodo para entrar la Funcionalidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCalidad");
		
	}
	
	/**
	 * Metodo para mostrar las Secciones de la Funcionalidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarP(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paramArchivoPlano");
	}
	
	/**
	 * Metodo para mostrar la Seccion Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarES(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		//***
		forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con, "ES"));
		forma.setEspecialidadesMap(ParamArchivoPlanoIndCalidad.consultaEspe(con));
		//***
		
		forma.setEspecialidadMap(ParamArchivoPlanoIndCalidad.consultaES(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para Guardar un nuevo Registro de la Seccion de Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarNuevoES(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setCodigo("");
		forma.setAcronimo("");
		forma.setDescripcion("");
		forma.setEspecialidad("");
		forma.setIndexMap("");
		forma.resetMapasGenerados();
		forma.setEspecialidadesMap(ParamArchivoPlanoIndCalidad.consultaEspe(con));
		
		//forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para Guardar una nuevo Registro de la Seccion Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarNuevoCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setCodigo("");
		forma.setAcronimo("");
		forma.setDescripcion("");
		forma.setCentroCosto("");
		forma.setIndexMap("");
		forma.resetMapasGenerados();
		forma.setCentrosCostoMap(ParamArchivoPlanoIndCalidad.consultaCentros(con,usuario.getCodigoCentroAtencion()));
		
		//forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para Guardar un nuevo Registro de la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarNuevoDX(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setCodigo("");
		forma.setAcronimo("");
		forma.setDescripcion("");
		forma.setDiagnostico("");
		forma.setIndexMap("");
		forma.resetMapasGenerados();
		
		forma.setDiagnosticosDefinitivos(new HashMap());
		forma.setNumDiagnosticosDefinitivos(0);
		
		//forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */	
	private ActionForward accionSubirEspecialidad(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String nombre="";
		for(int i=0;i<Integer.parseInt(forma.getEspecialidadesMap("numRegistros").toString());i++)
		{
			if(forma.getEspecialidad().equals(forma.getEspecialidadesMap("codigo_"+i).toString()))
			{
				nombre=forma.getEspecialidadesMap("nombre_"+i).toString();
				forma.setEspecialidadesMap("selec_"+i, ConstantesBD.acronimoSi);
			}
		}
		forma.setNumEspecialidades(forma.getNumEspecialidades()+1);
		
		forma.setEspecialidadesGeneradasMap("codes_"+forma.getNumEspecialidades(), forma.getEspecialidad());
		forma.setEspecialidadesGeneradasMap("checkes_"+forma.getNumEspecialidades(), "1");
		forma.setEspecialidadesGeneradasMap("espec_"+forma.getNumEspecialidades(), nombre);
		forma.setEspecialidadesGeneradasMap("numRegistros", forma.getNumEspecialidades());
		
		if(Utilidades.convertirAEntero(forma.getIndexMap())>=0)
			forma.setEstado("modificarEspecialidad");
		else
			forma.setEstado("nuevoEspecialidad");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSubirCentro(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String nombre="";
		for(int i=0;i<Integer.parseInt(forma.getCentrosCostoMap("numRegistros").toString());i++)
		{
			if(forma.getCentroCosto().equals(forma.getCentrosCostoMap("codigo_"+i).toString()))
			{
				nombre=forma.getCentrosCostoMap("nombre_"+i).toString();
				forma.setCentrosCostoMap("selec_"+i, ConstantesBD.acronimoSi);
			}
		}
		forma.setNumCentros(forma.getNumCentros()+1);
		
		forma.setCentrosGeneradosMap("codcc_"+forma.getNumCentros(), forma.getCentroCosto());
		forma.setCentrosGeneradosMap("checkcc_"+forma.getNumCentros(), "1");
		forma.setCentrosGeneradosMap("cc_"+forma.getNumCentros(), nombre);
		forma.setCentrosGeneradosMap("numRegistros", forma.getNumCentros());
		
		if(Utilidades.convertirAEntero(forma.getIndexMap())>=0)
			forma.setEstado("modificarCentro");
		else
			forma.setEstado("nuevoCentro");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para cargar en Pantalla el nombre del Codigo del Indicador Seleccionado en Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarNombre(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		/*if(Utilidades.convertirAEntero(forma.getIndex())>=0)
		{
			forma.setDescripcion(forma.getCodigosMap("descripcion_"+forma.getIndex()).toString());
			//forma.setEspecialidadesMap(ParamArchivoPlanoIndCalidad.consultaEspe(con));
		}*/
		for(int i=0;i<Integer.parseInt(forma.getCodigosMap("numRegistros").toString());i++)
		{
			if(forma.getCodigosMap("codigo_"+i).toString().equals(forma.getIndex()))
				forma.setDescripcion(forma.getCodigosMap("descripcion_"+i).toString());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para cargar en Pantalla el nombre del Codigo del Indicador Seleccionado en Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarNombreCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		/*if(Utilidades.convertirAEntero(forma.getIndex())>=0)
		{
			forma.setDescripcion(forma.getCodigosMap("descripcion_"+forma.getIndex()).toString());
			//forma.setEspecialidadesMap(ParamArchivoPlanoIndCalidad.consultaEspe(con));
		}*/
		for(int i=0;i<Integer.parseInt(forma.getCodigosMap("numRegistros").toString());i++)
		{
			if(forma.getCodigosMap("codigo_"+i).toString().equals(forma.getIndex()))
				forma.setDescripcion(forma.getCodigosMap("descripcion_"+i).toString());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para cargar en Pantalla el nombre del Codigo del Indicador Seleccionado en Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarNombreDX(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		/*if(Utilidades.convertirAEntero(forma.getIndex())>=0)
		{
			forma.setDescripcion(forma.getCodigosMap("descripcion_"+forma.getIndex()).toString());
			//forma.setEspecialidadesMap(ParamArchivoPlanoIndCalidad.consultaEspe(con));
		}*/
		for(int i=0;i<Integer.parseInt(forma.getCodigosMap("numRegistros").toString());i++)
		{
			if(forma.getCodigosMap("codigo_"+i).toString().equals(forma.getIndex()))
				forma.setDescripcion(forma.getCodigosMap("descripcion_"+i).toString());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * Metodo para cargar los valores desde la forma hasta el mundo dependiendo de la Seccion
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param estado
	 */
	private void llenarMundo (ParamArchivoPlanoIndCalidadForm forma, ParamArchivoPlanoIndCalidad mundo, UsuarioBasico usuario, String estado)
	{
		String [] diagnosP;
		if(estado.equals("Especialidad"))
		{
			mundo.setCodigo(forma.getCodigo());
			mundo.setAcronimo(forma.getAcronimo());
			mundo.setDescripcion(forma.getDescripcion());
			mundo.setEspecialidad(forma.getEspecialidad());
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuario(usuario.getLoginUsuario());
		}
		
		if(estado.equals("Centro"))
		{
			mundo.setCodigo(forma.getCodigo());
			mundo.setAcronimo(forma.getAcronimo());
			mundo.setDescripcion(forma.getDescripcion());
			mundo.setCentroCosto(forma.getCentroCosto());
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuario(usuario.getLoginUsuario());
		}
		if(estado.equals("Diagnostico"))
		{
			mundo.setCodigo(forma.getCodigo());
			mundo.setAcronimo(forma.getAcronimo());
			mundo.setDescripcion(forma.getDescripcion());			
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuario(usuario.getLoginUsuario());
			//diagnosP=forma.getDiagnostico().split(ConstantesBD.separadorSplit);
			//mundo.setDiagnostico(diagnosP[0]);
			//mundo.setCie(diagnosP[1]);
		}
		if(estado.equals("ModDiagnostico"))
		{
			mundo.setCodigo(forma.getCodigo());
			mundo.setAcronimo(forma.getAcronimo());
			mundo.setDescripcion(forma.getDescripcion());			
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuario(usuario.getLoginUsuario());
			diagnosP=forma.getDiagnostico().split(ConstantesBD.separadorSplit);
			mundo.setDiagnostico(diagnosP[0]);
			mundo.setCie(diagnosP[1]);
		}
		if(estado.equals("Signo"))
		{
			mundo.setCero(forma.getCero());
			mundo.setTad(forma.getTad());
			mundo.setMtad(forma.getMtad());
			mundo.setTas(forma.getTas());
			mundo.setMtas(forma.getMtas());
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setUsuario(usuario.getLoginUsuario());
		}
	}
	
	/**
	 * Metodo para Insertar en BD el nuevo registro de la Seccion Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertarEspecialidad(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		int nume=0, ins=0;
		if(forma.getEstado().equals("insertarEspecialidad"))
        {
        	if(forma.getCodigo().equals("-1"))
        	{
        		errores.add("Campo Código Indicador requerido",new ActionMessage("errors.required", "El Código del Indicador "));
        	}
        	if(Integer.parseInt(forma.getEspecialidadesGeneradasMap("numRegistros").toString())>0)
        	{
        		for(int z=1;z<=Integer.parseInt(forma.getEspecialidadesGeneradasMap("numRegistros").toString());z++)
        		{
        			if(forma.getEspecialidadesGeneradasMap("checkes_"+z).toString().equals("1"))
        				nume=1;
        		}
        		if(nume==0)
        			errores.add("Campo Especialidad requerido",new ActionMessage("errors.required", "La Especialidad "));
        	}
        	else
        	{
        		errores.add("Campo Especialidad requerido",new ActionMessage("errors.required", "La Especialidad "));
        	}
        }
		
		if(errores.isEmpty())
		{
			ParamArchivoPlanoIndCalidad mundo = new ParamArchivoPlanoIndCalidad();
			llenarMundo(forma, mundo, usuario, "Especialidad");
			
			for(int i=1;i<=Integer.parseInt(forma.getEspecialidadesGeneradasMap("numRegistros").toString());i++)
			{
				if(forma.getEspecialidadesGeneradasMap("checkes_"+i).toString().equals("1"))
				{
					mundo.setEspecialidad(forma.getEspecialidadesGeneradasMap("codes_"+i).toString());
					if(ParamArchivoPlanoIndCalidad.insertarEspecialidad(con, mundo))
						logger.info("bien>>>>>>"+i);
					else
						ins=1;
				}
			}
			if(ins == 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"No se pudo realiar todas las Operaciones con Exito"));
			
			forma.setEspecialidadMap(ParamArchivoPlanoIndCalidad.consultaES(con));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresEspecialidad");
		}
		else
		{
			saveErrors(request,errores);
			forma.setEstado("nuevoEspecialidad");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresEspecialidad");
		}
	}
	
	/**
	 * Metodo para Insertar en BD el nuevo Registro de la Seccion Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertarCentro(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		int nume=0, ins=0;
		if(forma.getEstado().equals("insertarCentro"))
        {
        	if(forma.getCodigo().equals("-1"))
        	{
        		errores.add("Campo Código Indicador requerido",new ActionMessage("errors.required", "El Código del Indicador "));
        	}
        	if(Integer.parseInt(forma.getCentrosGeneradosMap("numRegistros").toString())>0)
        	{
        		for(int z=1;z<=Integer.parseInt(forma.getCentrosGeneradosMap("numRegistros").toString());z++)
        		{
        			if(forma.getCentrosGeneradosMap("checkcc_"+z).toString().equals("1"))
        				nume=1;
        		}
        		if(nume==0)
        			errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
        	}
        	else
        	{
        		errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
        	}
        }
		
		if(errores.isEmpty())
		{
			ParamArchivoPlanoIndCalidad mundo = new ParamArchivoPlanoIndCalidad();
			llenarMundo(forma, mundo, usuario, "Centro");
			
			for(int i=1;i<=Integer.parseInt(forma.getCentrosGeneradosMap("numRegistros").toString());i++)
			{
				if(forma.getCentrosGeneradosMap("checkcc_"+i).toString().equals("1"))
				{
					mundo.setCentroCosto(forma.getCentrosGeneradosMap("codcc_"+i).toString());
					if(ParamArchivoPlanoIndCalidad.insertarCentro(con, mundo))
						logger.info("bien>>>>>>"+i);
					else
						ins=1;
				}
			}
			if(ins == 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar todas las Operaciones con Exito"));
			
			forma.setCentroCostoMap(ParamArchivoPlanoIndCalidad.consultaCC(con));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresCentroCosto");
		}
		else
		{
			saveErrors(request,errores);
			forma.setEstado("nuevoCentro");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresCentroCosto");
		}
	}
	
	/**
	 * Metodo para Insertar en BD el nuevo Registro de la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertarDiagnostico(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		String [] diagP;		
		int ins=0, nume=0;
		if(forma.getEstado().equals("insertarDiagnostico"))
        {
        	if(forma.getCodigo().equals("-1"))
        	{
        		errores.add("Campo Código Indicador requerido",new ActionMessage("errors.required", "El Código del Indicador "));
        	}
        	if(forma.getNumDiagnosticosDefinitivos()>0)
        	{
        		for(int z=0;z<forma.getNumDiagnosticosDefinitivos();z++)
        		{
        			if(forma.getDiagnosticosDefinitivos().get("checkbox_"+z).toString().equals("true"))
        				nume=1;
        		}
        		if(nume==0)
        			errores.add("Campo Diagnostico Asociado requerido",new ActionMessage("errors.required", "El Diagnostico Asociado "));
        	}
        	else
        	{
        		errores.add("Campo Diagnostico Asociado requerido",new ActionMessage("errors.required", "El Diagnostico Asociado "));
        	}
        }
		
		if(errores.isEmpty())
		{
			ParamArchivoPlanoIndCalidad mundo = new ParamArchivoPlanoIndCalidad();
			llenarMundo(forma, mundo, usuario, "Diagnostico");
			
			for(int p=0;p<forma.getNumDiagnosticosDefinitivos();p++)
			{
				if(forma.getDiagnosticosDefinitivos().get("checkbox_"+p).toString().equals("true"))
				{
					diagP=forma.getDiagnosticosDefinitivos().get("relacionado_"+p).toString().split(ConstantesBD.separadorSplit);
					mundo.setDiagnostico(diagP[0]);
					mundo.setCie(diagP[1]);
					if(ParamArchivoPlanoIndCalidad.insertarDiagnostico(con, mundo))
						logger.info("bien>>>>>>"+p);
					else
						ins=1;
				}
			}
			if(ins == 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar todas las Operaciones con Exito"));
			
			forma.setDiagnosticoMap(ParamArchivoPlanoIndCalidad.consultaDX(con));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresDiagnostico");
		}
		else
		{
			saveErrors(request,errores);
			forma.setEstado("nuevoDiagnostico");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresDiagnostico");
		}
	}
	
	/**
	 * Metodo para la Eliminacion de un registro de la Seccion Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarES(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());
			if(ParamArchivoPlanoIndCalidad.eliminarEspecialidad(con, forma.getEspecialidadMap("codigo_"+forma.getIndexMap()).toString()))
			{
				Utilidades.generarLogGenerico(forma.getEspecialidadMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito. Se elimino el indicador "+forma.getEspecialidadMap("acronimo_"+forma.getIndexMap())+" con Especialidad "+forma.getEspecialidadMap("nombre_"+forma.getIndexMap())));
			}
			forma.setEspecialidadMap(ParamArchivoPlanoIndCalidad.consultaES(con));
			UtilidadBD.closeConnection(con);			
			return mapping.findForward("indicadoresEspecialidad");
    	}
		forma.setEspecialidadMap(ParamArchivoPlanoIndCalidad.consultaES(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para la Eliminacion de un Registro de la Seccion Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());
			if(ParamArchivoPlanoIndCalidad.eliminarCentro(con, forma.getCentroCostoMap("codigo_"+forma.getIndexMap()).toString()))
			{
				Utilidades.generarLogGenerico(forma.getCentroCostoMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
    			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito. Se elimino el indicador "+forma.getCentroCostoMap("acronimo_"+forma.getIndexMap())+" con Centro de Costo "+forma.getCentroCostoMap("nombre_"+forma.getIndexMap())));
			}
			forma.setCentroCostoMap(ParamArchivoPlanoIndCalidad.consultaCC(con));
			UtilidadBD.closeConnection(con);			
			return mapping.findForward("indicadoresCentroCosto");
    	}
		forma.setCentroCostoMap(ParamArchivoPlanoIndCalidad.consultaCC(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para la Eliminacion de un Registro de la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDX(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());
			if(ParamArchivoPlanoIndCalidad.eliminarDiagnostico(con, forma.getDiagnosticoMap("codigo_"+forma.getIndexMap()).toString()))
			{
				Utilidades.generarLogGenerico(forma.getDiagnosticoMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito. Se elimino el indicador "+forma.getDiagnosticoMap("acronimo_"+forma.getIndexMap())+" con Diagnostico "+forma.getDiagnosticoMap("nombre_"+forma.getIndexMap())));
			}
			forma.setDiagnosticoMap(ParamArchivoPlanoIndCalidad.consultaDX(con));
			UtilidadBD.closeConnection(con);			
			return mapping.findForward("indicadoresDiagnostico");
    	}
		forma.setDiagnosticoMap(ParamArchivoPlanoIndCalidad.consultaDX(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * Metodo para Modificar un registro seleccionado de la Seccion Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarES(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.resetMapasGenerados();
		if(!forma.getIndexMap().equals(""))
    	{
			forma.setCodigo(forma.getEspecialidadMap("codigoi_"+forma.getIndexMap()).toString());
			forma.setDescripcion(forma.getEspecialidadMap("descripcion_"+forma.getIndexMap()).toString());
			forma.setEspecialidad(forma.getEspecialidadMap("codigoe_"+forma.getIndexMap()).toString());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresEspecialidad");
    	}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para Modificar un registro seleccionado de la Seccion Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(!forma.getIndexMap().equals(""))
    	{
			forma.setCodigo(forma.getCentroCostoMap("codigoi_"+forma.getIndexMap()).toString());
			forma.setDescripcion(forma.getCentroCostoMap("descripcion_"+forma.getIndexMap()).toString());
			forma.setCentroCosto(forma.getCentroCostoMap("codigoc_"+forma.getIndexMap()).toString());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresCentroCosto");
    	}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para la Modificacion de un registro seleccionado de la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarDX(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(!forma.getIndexMap().equals(""))
    	{
			forma.setCodigo(forma.getDiagnosticoMap("codigoi_"+forma.getIndexMap()).toString());
			forma.setDescripcion(forma.getDiagnosticoMap("descripcion_"+forma.getIndexMap()).toString());
			forma.setDiagnostico(forma.getDiagnosticoMap("codigod_"+forma.getIndexMap()).toString()+ConstantesBD.separadorSplit+forma.getDiagnosticoMap("cie_"+forma.getIndexMap()).toString()+ConstantesBD.separadorSplit+forma.getDiagnosticoMap("nombre_"+forma.getIndexMap()).toString());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("indicadoresDiagnostico");
    	}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * Metodo para Guardar la Modificacion en BD del registro a modificar de la Seccion Especialidad
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarES(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		ParamArchivoPlanoIndCalidad mundo=new ParamArchivoPlanoIndCalidad();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("acronimo_"+forma.getIndexMap(), forma.getCodigo());
		mapaM.put("descripcion_"+forma.getIndexMap(), forma.getDescripcion());
		mapaM.put("nombre_"+forma.getIndexMap(), forma.getEspecialidad());
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("acronimo_0", forma.getEspecialidadMap("acronimo_"+forma.getIndexMap()));
		mapaN.put("descripcion_0", forma.getEspecialidadMap("descripcion_"+forma.getIndexMap()));
		mapaN.put("nombre_0", forma.getEspecialidadMap("nombre_"+forma.getIndexMap()));
		
		llenarMundo(forma, mundo, usuario, "Especialidad");
		
		if(mundo.modificarES(con, mundo, forma.getEspecialidadMap("codigo_"+forma.getIndexMap()).toString()))
		{
			Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operación con Exito"));
		}
		
		forma.setEspecialidadMap(ParamArchivoPlanoIndCalidad.consultaES(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresEspecialidad");
	}
	
	/**
	 * Metodo para Guardar la Modificacion en BD del registro a modificar de la Seccion Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		ParamArchivoPlanoIndCalidad mundo=new ParamArchivoPlanoIndCalidad();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("acronimo_"+forma.getIndexMap(), forma.getCodigo());
		mapaM.put("descripcion_"+forma.getIndexMap(), forma.getDescripcion());
		mapaM.put("nombre_"+forma.getIndexMap(), forma.getCentroCosto());
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("acronimo_0", forma.getCentroCostoMap("acronimo_"+forma.getIndexMap()));
		mapaN.put("descripcion_0", forma.getCentroCostoMap("descripcion_"+forma.getIndexMap()));
		mapaN.put("nombre_0", forma.getCentroCostoMap("nombre_"+forma.getIndexMap()));
		
		llenarMundo(forma, mundo, usuario, "Centro");
		
		if(mundo.modificarCC(con, mundo, forma.getCentroCostoMap("codigo_"+forma.getIndexMap()).toString()))
		{
			Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operación con Exito"));
		}
		
		forma.setCentroCostoMap(ParamArchivoPlanoIndCalidad.consultaCC(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para Guardar la Modificacion en BD del registro a modificar de la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarDX(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String [] indicesMap={"acronimo_","descripcion_","nombre_","puedoEliminar_"};
		String [] diagP;
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		ParamArchivoPlanoIndCalidad mundo=new ParamArchivoPlanoIndCalidad();
		
		diagP=forma.getDiagnostico().split(ConstantesBD.separadorSplit);
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("acronimo_"+forma.getIndexMap(), forma.getCodigo());
		mapaM.put("descripcion_"+forma.getIndexMap(), forma.getDescripcion());
		mapaM.put("nombre_"+forma.getIndexMap(), diagP[2]);
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("acronimo_0", forma.getDiagnosticoMap("acronimo_"+forma.getIndexMap()));
		mapaN.put("descripcion_0", forma.getDiagnosticoMap("descripcion_"+forma.getIndexMap()));
		mapaN.put("nombre_0", forma.getDiagnosticoMap("nombre_"+forma.getIndexMap()));
		
		llenarMundo(forma, mundo, usuario, "ModDiagnostico");
		
		if(mundo.modificarDX(con, mundo, forma.getDiagnosticoMap("codigo_"+forma.getIndexMap()).toString()))
		{
			Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operación con Exito"));
		}
		
		forma.setDiagnosticoMap(ParamArchivoPlanoIndCalidad.consultaDX(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * Metodo para Guardar la Modificacion en BD del registro a modificar de la Seccion Generales
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarS(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		if(forma.getEstado().equals("guardarSigno"))
        {
			if(UtilidadTexto.isEmpty(forma.getMtad()))
        	{
        		errores.add("Campo Máximo Normal del TAD requerido",new ActionMessage("errors.required", "El Máximo Normal del TAD "));
        	}
			if(UtilidadTexto.isEmpty(forma.getMtas()))
        	{
        		errores.add("Campo Máximo Normal del TAS requerido",new ActionMessage("errors.required", "El Máximo Normal del TAS "));
        	}
			if(forma.getTad().equals("-1"))
        	{
        		errores.add("Campo Tensión Arterial Diastólica requerido",new ActionMessage("errors.required", "El Campo Tensión Arterial Diastólica "));
        	}
			if(forma.getTas().equals("-1"))
        	{
        		errores.add("Campo Tensión Arterial Sistólica requerido",new ActionMessage("errors.required", "El Campo Tensión Arterial Sistólica "));
        	}
        }
		if(errores.isEmpty())
		{
			String [] indicesMap={"cero_","tad_","mtad_","tas_","mtas"};
			forma.setMensaje(new ResultadoBoolean(false,""));
			ParamArchivoPlanoIndCalidad mundo=new ParamArchivoPlanoIndCalidad();
			
			HashMap<String, Object> mapaM = new HashMap<String, Object> ();
			mapaM.put("cero_"+forma.getIndexMap(), forma.getCero());
			mapaM.put("tad_"+forma.getIndexMap(), forma.getTad());
			mapaM.put("mtad_"+forma.getIndexMap(), forma.getMtad());
			mapaM.put("tas_"+forma.getIndexMap(), forma.getTas());
			mapaM.put("mtas_"+forma.getIndexMap(), forma.getMtas());
			
			HashMap<String, Object> mapaN = new HashMap<String, Object> ();
			mapaN.put("cero_0", forma.getGeneralesMap("cero_0"));
			mapaN.put("tad_0", forma.getGeneralesMap("tad_0"));
			mapaN.put("mtad_0", forma.getGeneralesMap("mtad_0"));
			mapaN.put("tas_0", forma.getGeneralesMap("tas_0"));
			mapaN.put("mtas_0", forma.getGeneralesMap("mtas_0"));
			
			llenarMundo(forma, mundo, usuario, "Signo");
			if(forma.getGuiaG().equals("s"))
			{
				if(mundo.modificarS(con, mundo, "", forma.getGuiaG()))
				{
					Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,0,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operación con Exito"));
				}
			}
			else
			{
				if(mundo.modificarS(con, mundo, forma.getGeneralesMap("codigo_0").toString(), forma.getGuiaG()))
				{
					Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,0,ConstantesBD.logParamArchivoPlanoIndCalidadCodigo, indicesMap);
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"No se pudo realizar la Operación con Exito"));
				}
			}
			
			forma.setGeneralesMap(ParamArchivoPlanoIndCalidad.consultaG(con));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("generales");
        }
		else
		{
			saveErrors(request,errores);
			forma.setEstado("empezarGenerales");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("generales");
		}
	}
	
	/**
	 * Metodo para mostrar la seccion de Centro de Costo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarCC(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		//***
		forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con, "CC"));
		forma.setCentrosCostoMap(ParamArchivoPlanoIndCalidad.consultaCentros(con,usuario.getCodigoCentroAtencion()));
		//***
		
		forma.setCentroCostoMap(ParamArchivoPlanoIndCalidad.consultaCC(con));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresCentroCosto");
	}
	
	/**
	 * Metodo para mostrar la Seccion Diagnostico
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarD(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		//***
		forma.setCodigosMap(ParamArchivoPlanoIndCalidad.consultaCOD(con, "DX"));
		//forma.setDiagnosticosMap(ParamArchivoPlanoIndCalidad.consultaDiagnosticos(con));
		//***
		
		forma.setDiagnosticoMap(ParamArchivoPlanoIndCalidad.consultaDX(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("indicadoresDiagnostico");
	}
	
	/**
	 * Metodo para mostrar la Seccion Generales
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarG(ParamArchivoPlanoIndCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		logger.info("MAPA DE LOS NULL>>>>>>>>>>"+forma.getGeneralesMap());
		forma.reset(usuario.getCodigoInstitucionInt());
		
		forma.setDiagnosticosMap(ParamArchivoPlanoIndCalidad.consultaSignos(con));
		
		forma.setGeneralesMap(ParamArchivoPlanoIndCalidad.consultaG(con));
		
		if(Integer.parseInt(forma.getGeneralesMap("numRegistros").toString())==0)
		{
			forma.setGuiaG("s");
			forma.setTad("-1");		
			forma.setTas("-1");
			forma.setMtad("");
			forma.setMtas("");
			forma.setCero("0");
		}
		else
		{
			forma.setTad(forma.getGeneralesMap("tad_0").toString());		
			forma.setTas(forma.getGeneralesMap("tas_0").toString());
			forma.setMtad(forma.getGeneralesMap("mtad_0").toString());
			forma.setMtas(forma.getGeneralesMap("mtas_0").toString());
			
			if(forma.getGeneralesMap("cero_0").toString().equals("S"))
			{
				forma.setCero("1");
			}
			else
			{
				forma.setCero("0");
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("generales");
	}
}