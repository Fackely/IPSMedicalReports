/*
 * @(#)AntecedentesFamiliaresAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesFamiliaresForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesFamiliares;
import com.princetonsa.mundo.antecedentes.TipoAntecedenteFamiliar;

/**
 * <code>Action</code> para el Ingreso/Actualizacion de una
 * <code>AntecedentesFamiliares</code>, toma los atributos de un objeto
 * <code>AntecedentesFamiliaresForm</code> y los transforma en el formato
 * apropiado para una<code>AntecedentesFamiliares</code>.
 *
 */

public class AntecedentesFamiliaresAction  extends  Action 
{

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	protected Logger logger = Logger.getLogger(AntecedentesFamiliaresAction.class);
	protected static final String errorNoHayPaciente = "No se encontró un Paciente Actual, debe buscar primero un paciente.";
	protected static final String errorNoNumeroRegistro = "Usted no está autorizado para entrar a esta funcionalidad, debe tener un nmero de registro de salud";
	protected static final String errorNoUsuarioEnSesion = "Se perdió el usuario de la sesión, debe ingresar nuevamente a la aplicación";
	protected static final String errorUsuarioNoAutorizado = "Usted no está autorizado para entrar a esta funcionalidad. ";
	protected Connection con = null; 	
	protected DaoFactory myFactory = null;
	protected PersonaBasica paciente = null;
	protected UsuarioBasico medico = null;
	private String estado;
	private AntecedentesFamiliaresForm familiaresForm;
	private AntecedentesFamiliares antecedentesFamiliares=null;
			
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		init(form, request);	
		ActionForward  errorValidacionUsuario = validacionesUsuario(mapping, request);
		
		if (errorValidacionUsuario != null) 
			return errorValidacionUsuario;			

		if( estado == null)
	   {
		   if( logger.isDebugEnabled() )
		   {
			   logger.debug("estado no valido  ");
		   }
		   request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
		   return mapping.findForward("paginaError");				
	   }				
	
	   estado = (estado != null) ? estado.trim().toLowerCase() : "";
	   
	   logger.warn("\n\n\t\t\t\t AntecedentesFamiliaresAction en Estado [" + estado + "] \n\n");

	   if (estado.equals("cargar"))
	   {
	   		return this.cargar(mapping,request);
	   }

		if (estado.equals("resumenatenciones"))
		{
			this.cargar(mapping,request);
			return mapping.findForward("paginaResumen");
		}
		
		if (estado.equals("resumen"))
		{
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaResumen");
		}
				
		if(estado.equals("ingresar_salir") && this.familiaresForm.isExisteAntecedente()) 
		{
			this.antecedentesFamiliares.setPaciente(this.paciente);
			return this.modificar(mapping, request, response);
		}
		
		if(estado.equals("ingresar_salir") && !this.familiaresForm.isExisteAntecedente()) 
		{
			this.antecedentesFamiliares.setPaciente(this.paciente);
			return this.ingresar(mapping,request, response);
		}
	
		if ( estado.equals("ingresar") && !this.familiaresForm.isExisteAntecedente() )
		{
			this.antecedentesFamiliares.setPaciente(this.paciente);		
			return this.ingresar(mapping,request,response);			
		}
		
		if ( estado.equals("ingresar") && this.familiaresForm.isExisteAntecedente() )
		{
			this.antecedentesFamiliares.setPaciente(this.paciente);
		
			return this.modificar(mapping,request,response);
		}
		
		if (estado.equals("adicionarfamiliar"))
		{
			familiaresForm.setNumAntAdicionales(familiaresForm.getNumAntAdicionales()+1);
			familiaresForm.setAntAdicional("checkbox_"+familiaresForm.getNumAntAdicionales(), "true");
									
			UtilidadBD.cerrarConexion(con);
			
			return mapping.findForward("paginaPrincipal");				
		}
		
		if (estado.equals("continuar"))
		{
			UtilidadBD.cerrarConexion(con);

			return mapping.findForward("paginaPrincipal");
		}
		
		return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Estado Inválido en Antecedentes Familiares Action", "errors.estadoInvalido", true);
	}
	
	protected void init(ActionForm form, HttpServletRequest request)throws SQLException
	{
		if (form instanceof AntecedentesFamiliaresForm)
		{
		   familiaresForm=(AntecedentesFamiliaresForm)form;
		}
		estado=familiaresForm.getEstado();  
		this.paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		this.medico=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		try
		{
			myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
			this.con = myFactory.getConnection();	
		
		}
		catch(SQLException ex)
		{
			String error = "Error inesperado ingresando la valoración de hospitalizacion. \nDetalle del error: \n"+ex+"\n";
			for(int i=0; i<ex.getStackTrace().length; i++)
			{
				error+=	"\n"+ex.getStackTrace()[i];			
			}
		}
		

		this.antecedentesFamiliares = new AntecedentesFamiliares();		
		antecedentesFamiliares.setPaciente(paciente);
	}
	
	protected ActionForward ingresar(ActionMapping mapping, HttpServletRequest request,HttpServletResponse response)
	{
		try 
		{
			if (response==null); //Para mantener los mismos datos de entrada del execute y no tener warnings
	
			//		cargamos un objeto antecedentesFamiliares con todo lo que viene del form		
			cargarObjetoAntecedentesFamiliaresIngresar();
	
			//		Comenzamos la transaccion
		
			if(con==null || con.isClosed())
			{
				con=UtilidadBD.abrirConexion();	
			}
			myFactory.beginTransaction(con);		
			//Insertamos antecedentes familiares
			try 
			{			
			  	this.antecedentesFamiliares.insertarFamiliares(con);
			}
			catch (SQLException ex) 
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				ex.printStackTrace();
				logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
				request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
				return mapping.findForward("paginaError");		
			}
				  
			myFactory.endTransaction(con);
			UtilidadBD.cerrarConexion(con);
			return this.cargar(mapping, request);
		} 
		catch (SQLException e) 
		{
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			return mapping.findForward("paginaError");	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			UtilidadBD.closeConnection(con);
			logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error :"+e.toString());
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : "+e.toString());
			return mapping.findForward("paginaError");
		}

	}
	
	private void cargarObjetoAntecedentesFamiliaresIngresar()
	{
		ArrayList antFamiliares=new ArrayList();		
		String codigoStr,nombre,parentesco,observaciones;
		int tam=this.familiaresForm.getNumAntecedentesFamiliares();
		
		for(int i=1; i<=tam; i++)
		{
			codigoStr=(String)familiaresForm.getAntecedentesFamiliares().get("codigo_"+i);
			int codigo=0;

			if (codigoStr!=null &&!codigoStr.equals("") )
			{
				try
				{
					codigo=Integer.parseInt(codigoStr);
				}
				catch(java.lang.NumberFormatException e)
				{
					logger.warn("Number Exception"+ e.getStackTrace());
				}
							
				TipoAntecedenteFamiliar 	tipo = new TipoAntecedenteFamiliar();
				
				String checkBox = (String)familiaresForm.getAntecedentesFamiliares().get("checkbox_I_"+codigo);
				
				observaciones=(String)familiaresForm.getAntecedenteFamiliar("observaciones_"+i);
				parentesco=(String)familiaresForm.getAntecedenteFamiliar("parentesco_I_"+i);
				nombre=(String)familiaresForm.getAntecedenteFamiliar("nombre_I_"+i);
					
					
				if( noVacio(observaciones) )
					observaciones="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentesco+"\n"+"Observaciones:"+observaciones.replaceAll("<br>", "\n");					
				else 
					observaciones="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentesco;
					
				tipo.setCodigo(codigo);
				tipo.setObservaciones(this.agregarDatosControl(observaciones));
				tipo.setParentesco(parentesco);
					
				if (noVacio(checkBox))
				{
					antFamiliares.add(tipo);
				}
			}	
		}
		this.antecedentesFamiliares.setTiposAntecedentesFamiliares(antFamiliares);
			
			
	
		/*******Antecedentes adicionales que se van a ingresar en la BD********/	
		ArrayList antecedentesFamiliaresOtros=new ArrayList();
		int tamAd=this.familiaresForm.getNumAntAdicionales();
		
		int codAnt = 1;
		for(int i=1; i<=tamAd; i++)
		{
			nombre=(String)familiaresForm.getAntecedentesAdicionales().get("nombre_"+i);
	
			if (nombre!=null &&!nombre.equals("") )
			{
				TipoAntecedenteFamiliar 	tipo = new TipoAntecedenteFamiliar();
					
				String checkBox = (String)familiaresForm.getAntecedentesAdicionales().get("checkbox_"+i);
				String observacionesOtros = (String)familiaresForm.getAntecedentesAdicionales().get("observaciones_"+i);
				String parentescoOtros=(String)familiaresForm.getAntecedentesAdicionales().get("parentesco_"+i);
					
				if( noVacio(observacionesOtros) )
					observacionesOtros="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentescoOtros+"\n"+"Observaciones:"+observacionesOtros.replaceAll("<br>", "\n");
				else 
					observacionesOtros="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentescoOtros;

				tipo.setCodigo(codAnt);
				tipo.setNombre(nombre);
				tipo.setObservaciones(this.agregarDatosControl(observacionesOtros));
				tipo.setParentesco(parentescoOtros);
					
				if (noVacio(checkBox))
				{
					antecedentesFamiliaresOtros.add(tipo);
					codAnt++;					
				}
			}	
		}

		this.antecedentesFamiliares.setTiposAntecedentesFamiliaresOtros(antecedentesFamiliaresOtros);
	
		String observacionesGenerales = this.agregarDatosControl(this.familiaresForm.getObservaciones());

		this.antecedentesFamiliares.setObservaciones(observacionesGenerales);
	}
			
	private boolean noVacio(String valor)
	{
		if( valor != null && !valor.equals("") )
			return true;
		else
			return false; 
	}
			
			
	/**
	 * Agrega los datos médicos de control a la cadena que recibe por parametro
	 * @param observaciones
	 * @return
	*/
	private String agregarDatosControl (String observaciones)
	{
		if(!observaciones.trim().equals(""))								
			return UtilidadTexto.agregarTextoAObservacion("",observaciones, medico, true);
		
		return "";						
	}	
	
	protected ActionForward cargar(ActionMapping mapping, HttpServletRequest request)
	{
		try
		{
			if(con==null || con.isClosed())
			{
				myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));		
				con = myFactory.getConnection();	
			}
			
			boolean  existeAntecedente=false;
			this.familiaresForm.reset();	
	
			try
			{
				this.antecedentesFamiliares.setPaciente(this.paciente);
				existeAntecedente=this.antecedentesFamiliares.cargar(con);
							
				UtilidadBD.cerrarConexion(con);
			}
			catch(SQLException e)
			{
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Excepción en Ant. Familiaraes Action " + e, "errors.problemasBd", true);
			}
	
			UtilidadBD.cerrarConexion(con);
					
			if( existeAntecedente )
			{
				this.familiaresForm.setExisteAntecedente(true);
				cargarFormAntecedentesFamiliares();
			}
			else 
			{		
				this.familiaresForm.setNumAntAdicionales(this.antecedentesFamiliares.getTiposAntecedentesFamiliaresOtros().size());	
				this.familiaresForm.setExisteAntecedente(false);
			}
			return mapping.findForward("paginaPrincipal");
		}
		catch (SQLException e) 
		{
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			return mapping.findForward("paginaError");	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			UtilidadBD.closeConnection(con);
			logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error :"+e.toString());
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : "+e.toString());
			return mapping.findForward("paginaError");
		}
	}
	
	protected void cargarFormAntecedentesFamiliares()
	{
		ArrayList anteFamiliaresPredefinidos=this.antecedentesFamiliares.getTiposAntecedentesFamiliares();		
		this.familiaresForm.setNumAntecedentesFamiliares(anteFamiliaresPredefinidos.size());
		
		logger.info("************Antecedentes Familiares Predefinidos************************************************");
		for(int i=0; i<anteFamiliaresPredefinidos.size(); i++)
		{
			TipoAntecedenteFamiliar tipo=new TipoAntecedenteFamiliar();
			tipo=(TipoAntecedenteFamiliar)anteFamiliaresPredefinidos.get(i);
			int indice=i+1;
				
			this.familiaresForm.setAntecedenteFamiliar("codigo_"+indice,""+tipo.getCodigo());
			//indice=tipo.getCodigo();
			this.familiaresForm.setAntecedenteFamiliar("cb_grabadoBD_"+indice, "true");
			this.familiaresForm.setAntecedenteFamiliar("cb_grabadoBD_I_"+tipo.getCodigo(), "true");
			this.familiaresForm.setAntecedenteFamiliar("checkbox_"+indice, "true");
			this.familiaresForm.setAntecedenteFamiliar("checkbox_I_"+tipo.getCodigo(), "true");
			this.familiaresForm.setAntecedenteFamiliar("nombre_"+indice,tipo.getNombre());
			this.familiaresForm.setAntecedenteFamiliar("nombre_I_"+tipo.getCodigo(),tipo.getNombre());
			if( noVacio(tipo.getObservaciones()) )
			{
				this.familiaresForm.setAntecedenteFamiliar("observacionesAnteriores_"+indice,tipo.getObservaciones().replaceAll("\n", "<br>"));
				this.familiaresForm.setAntecedenteFamiliar("observacionesAnteriores_I_"+tipo.getCodigo(),tipo.getObservaciones().replaceAll("\n", "<br>"));
				logger.info("OBSERVACIONES=> "+this.familiaresForm.getAntecedenteFamiliar("observacionesAnteriores_"+indice));
			}
			
			this.familiaresForm.setAntecedenteFamiliar("parentesco_"+indice,tipo.getParentesco());
			this.familiaresForm.setAntecedenteFamiliar("parentesco_I_"+tipo.getCodigo(),tipo.getParentesco());
	
			if( tipo.getParentesco() != null && !tipo.getParentesco().equals("") )
			{
				this.familiaresForm.setAntecedenteFamiliar("p_grabadoBD_"+indice, "true");
				this.familiaresForm.setAntecedenteFamiliar("p_grabadoBD_I_"+tipo.getCodigo(), "true");
			}
		}
		
		for(int i=0; i<this.antecedentesFamiliares.getTiposAntecedentesFamiliaresOtros().size(); i++)
		{
			TipoAntecedenteFamiliar tipo=new TipoAntecedenteFamiliar();
			tipo=(TipoAntecedenteFamiliar)this.antecedentesFamiliares.getTiposAntecedentesFamiliaresOtros().get(i);
				
			int indice=i+1;

			this.familiaresForm.setAntAdicional("codigo_"+indice,""+tipo.getCodigo());
			//indice=tipo.getCodigo();
			this.familiaresForm.setAntAdicional("cb_noGrabadoBD_"+indice, "true");
			this.familiaresForm.setAntAdicional("checkbox_"+indice, "true");
			this.familiaresForm.setAntAdicional("nombre_"+indice,tipo.getNombre());			
			
			if( tipo.getNombre() != null && !tipo.getNombre().equals("") )
				this.familiaresForm.setAntAdicional("n_grabadoBD_"+indice, "true");
				
			if( noVacio(tipo.getObservaciones()) )
				this.familiaresForm.setAntAdicional("observacionesAnteriores_"+indice,tipo.getObservaciones().replaceAll("\n", "<br>"));
				
			this.familiaresForm.setAntAdicional("parentesco_"+indice,tipo.getParentesco());
			
			if( tipo.getParentesco() != null && !tipo.getParentesco().equals("") )
				this.familiaresForm.setAntAdicional("dc_noGrabadoBD_"+indice, "true");
		}
		this.familiaresForm.setNumAntAdicionales(this.antecedentesFamiliares.getTiposAntecedentesFamiliaresOtros().size());
		this.familiaresForm.setObservacionesAnteriores(this.antecedentesFamiliares.getObservaciones());
	}
	
	protected ActionForward modificar(ActionMapping mapping, HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			if (response==null); //Para mantener los mismos datos de entrada del execute y no tener warnings
		
			if(con==null || con.isClosed())
			{
				myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));		
				con = myFactory.getConnection();	
			}
	
			cargarObjetoAntecedentesFamiliaresModificar();
	
			//		Comenzamos la transaccion				
			myFactory.beginTransaction(con);
			try
			{
				this.antecedentesFamiliares.modificar(con);
			}
			catch (SQLException ex) 
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				ex.printStackTrace();
				logger.error("No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
				request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
				return mapping.findForward("paginaError");		
			}
			myFactory.endTransaction(con);
			UtilidadBD.cerrarConexion(con);
	
			return this.cargar(mapping, request);
		}
		catch (SQLException e) 
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			logger.error("No se pudo hacer la inserción/modificación de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : Posiblemente otro usuario esta ingresando/modificando los Antecedentes.");
			return mapping.findForward("paginaError");	
		}
		catch(Exception e)
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			logger.error("No se pudo hacer la inserción/modificación de los antecedentes, se presentó Un Error :"+e.toString());
			request.setAttribute("descripcionError","No se pudo hacer la inserción de los antecedentes, se presentó Un Error : "+e.toString());
			return mapping.findForward("paginaError");
		}
	}	
	
	private void cargarObjetoAntecedentesFamiliaresModificar()
	{
		ArrayList antFamiliares=new ArrayList();
			
		String codigoStr,nombre,parentesco,observacionesNuevas;
		int tam=this.familiaresForm.getNumAntecedentesFamiliares();
				
		for(int i=1; i<=tam; i++)
		{
			codigoStr=(String)familiaresForm.getAntecedentesFamiliares().get("codigo_"+i);
			int codigo=0;

			if (codigoStr!=null &&!codigoStr.equals("") )
			{
				try
				{
					codigo=Integer.parseInt(codigoStr);
				}
				catch(java.lang.NumberFormatException e)
				{
					logger.warn("Number Exception"+ e.getStackTrace());
				}
				TipoAntecedenteFamiliar 	tipo = new TipoAntecedenteFamiliar();
								
				String checkBox = (String)familiaresForm.getAntecedentesFamiliares().get("checkbox_I_"+codigo);
				
				String grabadoBD_cb = (String)familiaresForm.getAntecedentesFamiliares().get("cb_grabadoBD_I_"+codigo);

				observacionesNuevas=(String)familiaresForm.getAntecedenteFamiliar("observaciones_"+i);
				String observacionesAnt = (String)familiaresForm.getAntecedenteFamiliar("observacionesAnteriores_I_"+codigo);
				parentesco=(String)familiaresForm.getAntecedenteFamiliar("parentesco_I_"+i);
				nombre=(String)familiaresForm.getAntecedenteFamiliar("nombre_I_"+i);
									
				String obs=new String();
				if(noVacio(observacionesAnt)&&!noVacio(observacionesNuevas))
				{
					obs=observacionesAnt.replaceAll("<br>","\n");
					tipo.setObservaciones(obs);
				}
								
				if(noVacio(observacionesAnt)&&noVacio(observacionesNuevas))
				{
					obs ="\n"+observacionesAnt.replaceAll("<br>", "\n")+"\n"+this.agregarDatosControl(observacionesNuevas);
					tipo.setObservaciones(obs);
				}
								
				if(noVacio(observacionesNuevas)&&!noVacio(observacionesAnt) && noVacio(checkBox))
				{
					obs ="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentesco+"Observaciones:"+observacionesNuevas;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
								
				if(!noVacio(observacionesNuevas)&&!noVacio(observacionesAnt) && noVacio(checkBox))
				{
					obs ="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentesco;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
				
				if(noVacio(observacionesNuevas)&&!noVacio(observacionesAnt) && !noVacio(checkBox))
				{
					obs ="Observaciones: "+observacionesNuevas;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
									
				tipo.setCodigo(codigo);
				tipo.setParentesco(parentesco);
									
				if (noVacio(checkBox)&&!noVacio(grabadoBD_cb))
				{
					tipo.setIngresadoBD(false);
					antFamiliares.add(tipo);
				}

				if(noVacio(checkBox)&&noVacio(grabadoBD_cb))
				{
					tipo.setIngresadoBD(true);
					antFamiliares.add(tipo);
				}
			}
		}
		this.antecedentesFamiliares.setTiposAntecedentesFamiliares(antFamiliares);
	
			
		/******Antecedentes adicionales que se van a ingresar en la BD*******/
	
		ArrayList antecedentesFamiliaresOtros=new ArrayList();
	
		int tamAd=this.familiaresForm.getNumAntAdicionales();
	
		int codAnt = 1;
		for(int i=1; i<=tamAd; i++)
		{
			nombre=(String)familiaresForm.getAntecedentesAdicionales().get("nombre_"+i);
			if (nombre!=null &&!nombre.equals("") )
			{
				TipoAntecedenteFamiliar 	tipo = new TipoAntecedenteFamiliar();
					
				String checkBox = (String)familiaresForm.getAntecedentesAdicionales().get("checkbox_"+i);
				String grabadoBD_cb = (String)familiaresForm.getAntecedentesAdicionales().get("cb_noGrabadoBD_"+i);
				String observacionesNuevasOtros = (String)familiaresForm.getAntecedentesAdicionales().get("observaciones_"+i);
				String observacionesAnterioresOtros = (String)familiaresForm.getAntecedentesAdicionales().get("observacionesAnteriores_"+i);
				String parentescoOtros=(String)familiaresForm.getAntecedentesAdicionales().get("parentesco_"+i);
				String obs=new String();
							  
				if(noVacio(observacionesAnterioresOtros)&&!noVacio(observacionesNuevasOtros))
			  	{
					obs=observacionesAnterioresOtros.replaceAll("<br>","\n");
					tipo.setObservaciones(obs);
				}
								
				if(noVacio(observacionesAnterioresOtros)&&noVacio(observacionesNuevasOtros))
				{
					obs ="\n"+observacionesAnterioresOtros+"\n"+this.agregarDatosControl(observacionesNuevasOtros);
					tipo.setObservaciones(obs);
				}
								
				if(noVacio(observacionesNuevasOtros)&&!noVacio(observacionesAnterioresOtros) && noVacio(checkBox))
				{
					obs ="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentescoOtros+"Observaciones:"+observacionesNuevasOtros;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
								
				if(!noVacio(observacionesNuevasOtros)&&!noVacio(observacionesAnterioresOtros) && noVacio(checkBox))
				{
					obs ="Nombre de la Enfermedad Familiar: "+nombre+"\n"+"Parentesco de quien la padece:"+parentescoOtros;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
				
				if(noVacio(observacionesNuevasOtros)&&!noVacio(observacionesAnterioresOtros) && !noVacio(checkBox))
				{																
					obs ="Observaciones: "+observacionesNuevasOtros;
					tipo.setObservaciones(this.agregarDatosControl(obs));
				}
				
				tipo.setCodigo(codAnt);
				tipo.setNombre(nombre);
				tipo.setParentesco(parentescoOtros);
				
				if (noVacio(checkBox)&&!noVacio(grabadoBD_cb))
				{	
					tipo.setIngresadoBD(false);
					antecedentesFamiliaresOtros.add(tipo);
					codAnt++;
				}
				else
				if(noVacio(checkBox)&&noVacio(grabadoBD_cb))
				{
					tipo.setIngresadoBD(true);
					antecedentesFamiliaresOtros.add(tipo);
					codAnt++;
				}
			}
		}
		this.antecedentesFamiliares.setTiposAntecedentesFamiliaresOtros(antecedentesFamiliaresOtros);


		String observacionesGenerales = familiaresForm.getObservacionesAnteriores() + this.agregarDatosControl(this.familiaresForm.getObservaciones());

		this.antecedentesFamiliares.setObservaciones(observacionesGenerales);

	}
	
	protected ActionForward validacionesUsuario(ActionMapping mapping,  HttpServletRequest request)
	{
		if(this.medico == null )
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug("Profesional de la salud no válido (null)");
			}
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");				
		}
		else
		if( this.paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug("El paciente no es válido (null)");
			}
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");				
		}
		else
		if( !UtilidadValidacion.esProfesionalSalud(medico) )
		{
			logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad ");				
			request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
			return mapping.findForward("paginaError");								
		}	

		return null;
	}
	
}