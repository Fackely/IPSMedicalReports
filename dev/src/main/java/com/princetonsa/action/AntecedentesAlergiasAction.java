/*
 * @(#)AntecedentesAlergiasAction.java
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
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.InfoDatosBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.AntecedentesAlergiasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesAlergias;
import com.princetonsa.mundo.antecedentes.CategoriaAlergia;


/**
 * <code>Action</code> para el Ingreso/Actualizacion de una
 * <code>AntecedentesAlergias</code>, toma los atributos de un objeto
 * <code>AntecedentesAlergiasForm</code> y los transforma en el formato
 * apropiado para una<code>AntecedentesAlergias</code>.
 *
 * @version 1.0, Ago 6, 2003
 * @author <a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 */

public class AntecedentesAlergiasAction extends Action {

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	protected Logger logger = Logger.getLogger(AntecedentesAlergiasAction.class);
	protected static final String errorNoHayPaciente = "No se encontró un Paciente Actual, debe buscar primero un paciente.";
	protected static final String errorNoNumeroRegistro = "Usted no está autorizado para entrar a esta funcionalidad, debe tener un número de registro de salud";
	protected static final String errorNoUsuarioSesion = "Se perdió el usuario de la sesión, debe ingresar nuevamente a la aplicación";
	protected static final String errorUsuarioNoAutorizado = "Usted no está autorizado para entrar a esta funcionalidad. Sólo puede usar esta funcionalidad si pertenece al Centro de Costo de Urgencias.";	
	protected AntecedentesAlergiasForm antAleForm = null;
	protected Connection con = null; 	
	protected AntecedentesAlergias antAle = null;
	protected DaoFactory myFactory = null;
	protected PersonaBasica paciente = null;
	protected UsuarioBasico medico = null;
	private String especialidadesMedico = null;
	/**
	 * Ejecuta las acciones correspondientes a antecedentes alergias: ingresar/cargar/modificar
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param form objeto con los datos provenientes del formulario
	 * @param request el <i>servlet request</i> que está siendo procesado en este momento
	 * @param response el <i>servlet response</i> resultado de procesar este request
	 * @return un <code>ActionForward</code> indicando la siguiente página
	 * dentro de la navegación
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {													

		if (response == null); // NOP para evitar el warning "The argument response is never read"
		ActionForward actionForwardErrorValidacion = validacionesUsuario(mapping, request);		 
		if (actionForwardErrorValidacion != null) return actionForwardErrorValidacion;
		//Si no hubo errores de validacion basica de usuario
		init(form, request);	
		String accion = antAleForm.getEstado();
				
		if( accion == null )
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug("estado no valido dentro del flujo de antecedentes alergias (null) ");
			}
			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			return mapping.findForward("descripcionError");				
		}				
		
		accion = accion.trim().toLowerCase();
		
		logger.warn("\n\n En AntecedentesAlergiasAction El Estado [" + accion + "]  \n\n");
		
		
		if(accion.equals("cargar")) {
			return this.cargar(mapping, request);
		}
		
		if(accion.equals("continuar")) {
			return mapping.findForward("paginaPrincipal");
		}
		
		if(accion.equals("resumen")) {
//		if(accion.equals("resumen") && this.antAleForm.isExisteAntecedente() ) {
			this.antAleForm.setItemActual("");
			return mapping.findForward("paginaResumen");			
		}
		
		if(accion.equals("resumenatenciones")) {
			this.antAleForm.setItemActual("");
			this.cargar(mapping, request);
			return mapping.findForward("paginaResumen");			
		}		
		
		
		//Dependiendo de si existe un antecedente de alergias previamente grabado en el sistema, 
		//se llama a ingresar o modificar
		 
		if(accion.equals("grabar_salir") && this.antAleForm.isExisteAntecedente()) {
			return this.modificar(mapping, request, response);
		}
		
		if(accion.equals("grabar_salir") && !this.antAleForm.isExisteAntecedente()) {
			return this.ingresar(mapping, request, response);
		}

		if(accion.equals("grabar") && this.antAleForm.isExisteAntecedente()) {
			return this.modificar(mapping, request, null);
		}
		
		if(accion.equals("grabar") && !this.antAleForm.isExisteAntecedente()) {
			return this.ingresar(mapping, request, null);
		}

		//Si no corresponde a ninguna de las acciones anteriores, se muestra un error
		antAleForm.reset();		
		request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
		return mapping.findForward("descripcionError");		
	}
	/**
	 * Inicializa los atributos de esta clase.
	 * @param form
	 * @param request
	 * @throws SQLException
	 */
	protected void init(ActionForm form, HttpServletRequest request) 
	{
		paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");				
		medico = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		especialidadesMedico = "";
		
		//Obteniendo las especialidades del medico que seran usadas para datos de control de las observaciones
		if((medico.getEspecialidades() != null)&& (medico.getEspecialidades().length > 0))
		{
			for(int i=0; i<medico.getEspecialidades().length; i++)
			{

				if(i == medico.getEspecialidades().length -1)
					especialidadesMedico+=medico.getEspecialidades()[i].getNombre();
				else especialidadesMedico+=medico.getEspecialidades()[i].getNombre()+", ";
			}
		}
		//Inicializando el tipo de bd
		myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		//No se abrirá la conexión todavía
		con = null;			
		// Creando el objeto de antecedentes alergias que se va a usar en todas las acciones. 
		antAle = new AntecedentesAlergias();		
		antAle.setPaciente(paciente);

		//Haciendo el cast de la forma
		antAleForm = (AntecedentesAlergiasForm)form;			
	}

	/**
	 * Método privado para insertar antecedente de alergias a un paciente.
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param form objeto con los datos provenientes del formulario
	 * @param request el <i>servlet request</i> que está siendo procesado en
	 * este momento
	 * @return un <code>ActionForward</code> indicando la siguiente página
	 * dentro de la navegación
	 */
	private ActionForward ingresar (ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{		
		int resp = 0;	
		if (con == null)
		{
			//Abriendo conexion
			myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
			con = myFactory.getConnection();		
		}
		
		//cargamos un objeto alergias con todo lo que viene del form		
		cargarObjetoAlergiasIngresar();
		
		//Comenzamos la transaccion				
		myFactory.beginTransaction(con);		
		//Insertamos antecedentes alergias
		try 
		{			
			resp = antAle.insertar(con);			
			
		}
		catch (SQLException ex) 
		{
			//Si algo salio mal, hacemos rollback de los cambios
			myFactory.abortTransaction(con);			
			// Cerramos la conexión antes de retornar
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se pudo insertar el antecedente de alergias. \nDetalle de la excepción: \n"+ex, "errors.problemasBd", true);
		}
		if(resp == 0) 
		{
			//Si algo salio mal, hacemos rollback de los cambios
			myFactory.abortTransaction(con);			
			// Cerramos la conexión antes de retornar
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se produjo una SQLException pero una de las sentencias de inserción retorno 0", "errors.problemasBd", true);
		}
		
		//cerramos la transaccion si todo salio bien.
		myFactory.endTransaction(con);
		// Cerramos la conexión antes de retornar
		UtilidadBD.cerrarConexion(con);
		
		//Si se debe salir del flujo normal de grabación de antecedentes alergias para irse a otra página
		if(response != null)
		{	
			String paginaSiguiente=request.getParameter("paginaSiguiente");		
			if(paginaSiguiente != null )
			{

				antAleForm.reset();
				response.sendRedirect(paginaSiguiente);
			}
			return null;
		}
		//Si sigue el flujo normal debe mostrar los datos grabados
		else return this.cargar(mapping, request);
	}
	
	/**
	 * Este método toma todo lo que viene en el form y lo pasa al objeto siguiendo la lógica de esta transformación.
	 * El objeto queda inicializado para que todos sus datos sean para ingresar a la bd.
	 */
	private void cargarObjetoAlergiasIngresar () 
	{		
		// Pasamos los datos basicos del form al objeto
		int numAleAdic = 0, numAlePredef = 0;
		int cat = 0;
		int codigoAle;
		String nombre, observaciones;
		CategoriaAlergia catAle = null;
		ArrayList categoriasAlergias = new ArrayList();
		String [] codNomCat;		
		for(int i=0; i<this.antAleForm.getCategorias().length; i++)
		{			
			cat = this.antAleForm.getCategorias()[i];
			
			numAleAdic = Integer.parseInt((String)this.antAleForm.getAleAdic().get(""+cat));
			numAlePredef = Integer.parseInt((String)this.antAleForm.getAlePredef().get(""+cat));
			//Si existe al menos una alergia seleccionada para esa categoria, se crea un objeto CategoriaAlergia
			//y se le pegan las alergias adicionales y predefinidas que fueron chequeadas
			if(numAleAdic > 0 || numAlePredef > 0)
			{
				catAle = new CategoriaAlergia(cat, "", new ArrayList(), new ArrayList());
				categoriasAlergias.add(catAle);
			}
			int k=1;
			for (int j=0; j<numAleAdic; j++)
			{	
				if(this.antAleForm.getAleAdic().get(cat+"_"+j) != null)
				{					
					nombre = (String)this.antAleForm.getAleAdic().get(cat+"_"+j);
					observaciones = (String)this.antAleForm.getAleAdicObs().get(cat+"_"+j);
					if(!observaciones.equals(""))
						observaciones="Presenta alergia a: "+nombre+"\n"+observaciones;
					else observaciones="Presenta alergia a: "+nombre;			
					catAle.getTiposAlergiasAdicionales().add(new InfoDatosBD(k,nombre, this.agregarDatosControl(observaciones)));
					k++;
				}				
			}
			for (int j=0; j<numAlePredef; j++)
			{
				codigoAle = Integer.parseInt((String)this.antAleForm.getAlePredef().get("cod_"+cat+"_"+j));
				if( this.antAleForm.getAlePredef().get(cat+"_"+codigoAle) != null  
					&& !((String)this.antAleForm.getAlePredef().get(cat+"_"+codigoAle)).equals("false"))
				{				
					observaciones = (String)this.antAleForm.getAlePredefObs().get(cat+"_"+codigoAle);
					//[cod_categoria]_[codigo_alergia]= [codigo_alergia]-[nombre_alergia]-[cod_categoria]
					codNomCat = ((String)this.antAleForm.getAlePredef().get(cat+"_"+codigoAle)).split("-", 3);
					if(!observaciones.equals(""))
						observaciones="Presenta alergia a: "+codNomCat[1]+"\n"+observaciones;
					else observaciones="Presenta alergia a: "+codNomCat[1];					
					catAle.getTiposAlergiasPredefinidas().add(new InfoDatosBD(codigoAle,"", this.agregarDatosControl(observaciones)));
				}
			}							
		}
		this.antAle.setCategoriasAlergias(categoriasAlergias);		
		this.antAle.setObservaciones(this.agregarDatosControl(this.antAleForm.getObservaciones()));		
	}
	
	/**
	 * Este método toma todo lo que viene en el form y lo pasa al objeto siguiendo la lógica de esta transformación.
	 * El objeto queda inicializado para modificar y/o ingresar datos a la bd.
	 */
	private void cargarObjetoAlergiasModificar () {
		
		// Pasamos los datos basicos del form al objeto
		int numAleAdic = 0, numAlePredef = 0;
		int cat = 0;
		int codigoAle, k;
		String nombre, observaciones, observacionesAnteriores;
		CategoriaAlergia catAle = null;
		ArrayList categoriasAlergias = new ArrayList();
		String [] codNomCat;		
		for(int i=0; i<this.antAleForm.getCategorias().length; i++)
		{			
			cat = this.antAleForm.getCategorias()[i];
	
			//Ingresando las alergias predefinidas y adicionales nuevas:
						
			numAleAdic = Integer.parseInt((String)this.antAleForm.getAleAdic().get(""+cat));
			numAlePredef = Integer.parseInt((String)this.antAleForm.getAlePredef().get(""+cat));
			//Si existe al menos una alergia seleccionada para esa categoria, se crea un objeto CategoriaAlergia
			//y se le pegan las alergias adicionales y predefinidas que fueron chequeadas
			if(numAleAdic > 0 || numAlePredef > 0)
			{
				catAle = new CategoriaAlergia(cat, "", new ArrayList(), new ArrayList());
				categoriasAlergias.add(catAle);
			}
			if(this.antAleForm.getAleAdicCarga().get("SEQ-"+cat) == null ) k=0;
			else k=Integer.parseInt((String)this.antAleForm.getAleAdicCarga().get("SEQ-"+cat));
//OJO			
			//Necesitamos encontrar el codigo más alto de las alergias adicionales cargadas.
		
			for (int j=0 ; j<numAleAdic; j++)
			{ 							
				if(this.antAleForm.getAleAdic().get(cat+"_"+j) != null)
				{					
					nombre = (String)this.antAleForm.getAleAdic().get(cat+"_"+j);
					observaciones = (String)this.antAleForm.getAleAdicObs().get(cat+"_"+j);
					if(!observaciones.equals(""))
						observaciones="Presenta alergia a: "+nombre+"\n"+observaciones;
					else observaciones="Presenta alergia a: "+nombre;				
					//Removiendo las observaciones que ya fueron cargadas en el objeto				
					this.antAleForm.getAleAdicObs().remove(cat+"_"+j);
					catAle.getTiposAlergiasAdicionales().add(new InfoDatosBD(k+1,nombre, this.agregarDatosControl(observaciones)));
					k++;
				}				
			}
			for (int j=0; j<numAlePredef; j++)
			{
				codigoAle = Integer.parseInt((String)this.antAleForm.getAlePredef().get("cod_"+cat+"_"+j));
				if( this.antAleForm.getAlePredef().get(cat+"_"+codigoAle) != null  
					&& !((String)this.antAleForm.getAlePredef().get(cat+"_"+codigoAle)).equals("false"))
				{		
					observaciones = (String)this.antAleForm.getAlePredefObs().get(cat+"_"+codigoAle);
					codNomCat = ((String)this.antAleForm.getAlePredef().get(cat+"_"+codigoAle)).split("-", 3);
					if(!observaciones.equals(""))
						observaciones="Presenta alergia a: "+codNomCat[1]+"\n"+observaciones;
					else observaciones="Presenta alergia a: "+codNomCat[1];					
					
					//Removiendo las observaciones que ya fueron cargadas en el objeto
					this.antAleForm.getAlePredefObs().remove(cat+"_"+codigoAle);				
					catAle.getTiposAlergiasPredefinidas().add(new InfoDatosBD(codigoAle,"", this.agregarDatosControl(observaciones)));
				}
			}
		}
				
		//Ingresando las observaciones nuevas de las alergias ya grabadas
		
		//Como ya se eliminaron las observaciones de las alergias nuevas solo quedan las observaciones de las
		//alergias que ya estan grabadas
		
		//Alergias predefinidas
		Collection c =	(Collection) this.antAleForm.getAlePredefObs().keySet();
		Iterator iter = c.iterator();
		String key;
		String [] keyArray;
			
		while(iter.hasNext()) 
		{
			key = (String)iter.next();
			if( this.antAleForm.getAlePredefCarga().get(key) != null)
			{			
				keyArray = key.split("_");
				observaciones = (String)this.antAleForm.getAlePredefObs().get(key);
				if(!observaciones.trim().equals("")) {			
					observacionesAnteriores = (String)this.antAleForm.getAlePredefObsCarga().get(key);

					//Agregando al objeto la alergia y notificando que se trata de modificacion						
					catAle = new CategoriaAlergia(Integer.parseInt(keyArray[0]), "", new ArrayList(), new ArrayList());
					catAle.getTiposAlergiasPredefinidas().add(new InfoDatosBD(Integer.parseInt(keyArray[1]), "", observacionesAnteriores+"\n\n"+this.agregarDatosControl(observaciones), true));
					categoriasAlergias.add(catAle);
				}
			}							  							 
		}										
		
		//Alergias adicionales
		c =	(Collection) this.antAleForm.getAleAdicObs().keySet();
		iter = c.iterator();
	
		while(iter.hasNext()) 
		{
		  key = (String)iter.next();				
		  if(key.indexOf("nueva") != -1)
		  {
			keyArray = key.split("_");
			observaciones = (String)this.antAleForm.getAleAdicObs().get(key);
			if(!observaciones.trim().equals("")) 
			{			
				observacionesAnteriores = (String)this.antAleForm.getAleAdicObsCarga().get(keyArray[1]+"_"+keyArray[2]);

				//Agregando al objeto la alergia y notificando que se trata de modificacion						
				catAle = new CategoriaAlergia(Integer.parseInt(keyArray[1]), "", new ArrayList(), new ArrayList());
				catAle.getTiposAlergiasAdicionales().add(new InfoDatosBD(Integer.parseInt(keyArray[2]), "", observacionesAnteriores+"\n\n"+this.agregarDatosControl(observaciones), true));
				categoriasAlergias.add(catAle);
			}				
		  }							 
		}										
		
		this.antAle.setCategoriasAlergias(categoriasAlergias);		
		if(!this.antAleForm.getObservaciones().trim().equals(""))
		{			
			if(!this.antAleForm.getObservacionesAnteriores().equals(""))
				this.antAle.setObservaciones(this.antAleForm.getObservacionesAnteriores()+"\n\n"+this.agregarDatosControl(this.antAleForm.getObservaciones()));
			else this.antAle.setObservaciones(this.agregarDatosControl(this.antAleForm.getObservaciones()));	
		}
		else this.antAle.setObservaciones(this.antAleForm.getObservacionesAnteriores());
	}
	
	/**
	 * Método privado para modificar antecedentes alergias a un paciente.
	 * @param request el <i>servlet request</i> que está siendo procesado en este momento
	 * @return un <code>ActionForward</code> indicando la siguiente página dentro de la navegación
	 */
	private ActionForward modificar (ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		int resp = 0;
		if (con == null){
			//Abriendo conexion
			myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
			con = myFactory.getConnection();		
		}		
		//Cargamos un objeto alergias con todo lo que viene del form, teniendo en cuenta que hay datos 
		//para ingresar y otros para modificar.	
		cargarObjetoAlergiasModificar();	
		
		//Comenzamos la transacción	
		myFactory.beginTransaction(con);

		try
		{			
			resp = antAle.modificar(con);
			
		}
		catch (SQLException ex) 
		{
			//Si algo salio mal, hacemos rollback de los cambios
			myFactory.abortTransaction(con);			
			// Cerramos la conexin antes de retornar
			if (con != null && !con.isClosed()) 
                UtilidadBD.closeConnection(con);

				
			logger.warn("No se pudo modificar los antecedentes alergias. \nDetalle de la excepción: \n"+ex);
						
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("descripcionError");													
					
		}
		if(resp == 0)
		{
			//Si algo salio mal, hacemos rollback de los cambios
			myFactory.abortTransaction(con);			
			// Cerramos la conexión antes de retornar
			if (con != null && !con.isClosed()) 
                UtilidadBD.closeConnection(con);


			if( logger.isDebugEnabled() )
			{
				logger.debug("No se produjo una SQLException pero una de las sentencias de modificación retorno 0");
			}					
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");			
			return mapping.findForward("descripcionError");						
		}
		
		//cerramos la transaccion si todo salio bien.
		myFactory.endTransaction(con);		
		// Cerramos la conexion antes de retornar
		if (con != null && !con.isClosed()) 
            UtilidadBD.closeConnection(con);

		
		//Si se debe salir del flujo normal de grabación de la valoración para irse a otra página
		if(response != null)
		{	
			String paginaSiguiente=request.getParameter("paginaSiguiente");		
			if(paginaSiguiente != null )
			{

				antAleForm.reset();
				response.sendRedirect(paginaSiguiente);
			}
			return null;
		}
		//Si sigue el flujo normal debe mostrar los datos grabados
		else return this.cargar(mapping, request);
	}
	
	/**
	 * Se usa para mostrar los datos del mundo en el form, ya sea para consultarlos o modificarlos
	 * Se encarga de cargar el form con todos los datos que tiene un objeto valoración de urgencias.
	 * Si todo salio bien hace un forward a la pagina de resumen que muestra todos los datos. 
	 * @param mapping
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private ActionForward cargar(ActionMapping mapping, HttpServletRequest request) throws Exception {
	
		if (con == null || con.isClosed())
		{
			//Abriendo conexion
			myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
			con = myFactory.getConnection();		
		}		
		
		boolean existeAntecedentesAlergias = false;				
		antAle = new AntecedentesAlergias();
		//limpiando la forma
		antAleForm.reset();				
		try 
		{
			//Inicializando los objetos con sus identificadores en la base de datos									
			antAle.setPaciente(paciente);
			//Cargando los datos en el objeto
			existeAntecedentesAlergias = antAle.cargar(con);						
			logger.info(existeAntecedentesAlergias);
			// Cerramos la conexion antes de retornar
			if (con != null && !con.isClosed()) 
                UtilidadBD.closeConnection(con);

		} 
		catch (SQLException ex) 
		{
			// Cerramos la conexión antes de retornar
			if (con != null && !con.isClosed()) 
                UtilidadBD.closeConnection(con);

	
			logger.warn("No se pudieron cargar los antecedentes alergias! "+ex.getMessage());
						
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");			
			return mapping.findForward("descripcionError");						
		}
		// Cerramos la conexion antes de retornar
		if (con != null && !con.isClosed()) 
            UtilidadBD.closeConnection(con);

		logger.info(existeAntecedentesAlergias);
		//Si se cargo antecedentes alergias
		if (existeAntecedentesAlergias)	
		{			
			//Pasamos al form los datos de la valoracion de urgencias y de la valoración base
			this.antAleForm.setExisteAntecedente(true);			
			logger.info("entro a existeAntecedentesAlergia en cargar action");
			cargarFormAlergias();
		}
		else this.antAleForm.setExisteAntecedente(false);
		
		return mapping.findForward("paginaPrincipal");	 		
	}
	
	/**
	 * Pasa los datos del objeto AntecedentesAlergias al form de valoracion de urgencias.
	 */
	protected void cargarFormAlergias() {
		
		// Pasamos los datos basicos del objeto al form
		CategoriaAlergia catAle = null;		
		InfoDatosBD ale;
		int ultimoCodigo = 0; 
		
		this.antAleForm.setObservacionesAnteriores(this.antAle.getObservaciones());
		
		//Recorriendo cada categoria para cargar su lista de alergias predefinidas y adicionales grabadas en la bd
		for(int i=0; i<this.antAle.getCategoriasAlergias().size(); i++)
		{
			catAle = this.antAle.getCategoriaAlergia(i);
			
			this.antAleForm.getAleAdicCarga().put(catAle.getCodigo()+"", catAle.getTiposAlergiasAdicionales().size()+"");
			this.antAleForm.getAlePredefCarga().put(catAle.getCodigo()+"", catAle.getTiposAlergiasPredefinidas().size()+"");			
			//Cargando por categoria la lista de alergias predefinidas grabadas en la bd.
			for(int k=0; k < catAle.getTiposAlergiasPredefinidas().size(); k++)
			{
				
				ale = catAle.getAlergiaPredefinida(k);
				
				this.antAleForm.getAlePredefCarga().put("cod_"+catAle.getCodigo()+"_"+k, ale.getCodigo()+"");				
				this.antAleForm.getAlePredefCarga().put(catAle.getCodigo()+"_"+ale.getCodigo(), ale.getCodigo()+"-"+ale.getNombre()+"-"+catAle.getCodigo() );
				this.antAleForm.getAlePredefObsCarga().put(catAle.getCodigo()+"_"+ale.getCodigo(), ale.getDescripcion());
				
			}
			//Cargando por categoria la lista de alergias adicionales grabadas en la bd			
			for(int k=0; k < catAle.getTiposAlergiasAdicionales().size(); k++)
			{				
				ale = catAle.getAlergiaAdicional(k);
				this.antAleForm.getAleAdicCarga().put("cod_"+catAle.getCodigo()+"_"+k, ale.getCodigo()+"");				
				this.antAleForm.getAleAdicCarga().put(catAle.getCodigo()+"_"+ale.getCodigo(), ale.getNombre());
				this.antAleForm.getAleAdicObsCarga().put(catAle.getCodigo()+"_"+ale.getCodigo(), ale.getDescripcion());
				//Actualizando el codigo más alto de alergia adicional que aparecen en la bd para esa categoria y paciente.
				ultimoCodigo = (ultimoCodigo > ale.getCodigo())? ultimoCodigo : ale.getCodigo();										
			}
			//En el map queda el dato del codigo último código para seguir la secuencia cuando se inserte una nueva alergia
			//adicional para esta categoria y paciente
			this.antAleForm.getAleAdicCarga().put("SEQ-"+catAle.getCodigo(), ultimoCodigo+"");			
		}
	}	
	
	/**
	 * Validaciones básicas de usuario para ingresar a antecedentes
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward validacionesUsuario (ActionMapping mapping, HttpServletRequest request) 
	{
		HttpSession session=request.getSession();			
		PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
		UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");
							
		if( medico == null )
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug("Profesional de la salud no válido (null)");
			}
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");				
		}
		
		if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug("El paciente no es válido (null)");
			}
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");				
		}
		
		UsuarioBasico usuarioMedico = (UsuarioBasico) session.getAttribute("usuarioBasico");
	  
		if(!util.UtilidadValidacion.esProfesionalSalud(usuarioMedico)) {
			request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
			return mapping.findForward("descripcionError");		  				
		}
	
		return null;		
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
}
