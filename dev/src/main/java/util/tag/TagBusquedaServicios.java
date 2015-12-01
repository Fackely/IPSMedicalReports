/*
 * @(#)TagBusquedaServicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package util.tag;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;

/**
 * Clase encargada de la busqueda de servicios, manejada
 * en todas las solicitudes
 * 
 *	@version 1.0, Feb 13, 2004
 */
public class TagBusquedaServicios extends BodyTagSupport
{
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el criterio de busqueda del servicio
	 */
	private String criterioBusqueda;
	
	/**
	 * Boolean que nos dice si la búsqueda es por nombre
	 * (Si es false es por el código de axioma)
	 */
	private boolean esPorNombre;
	
	/**
	 * Boolean que nos dice si debemos buscar servicios con 
	 * o sin formulario asociado
	 */
	private boolean buscarConFormulario;
	
	/**
	 * Boolean que nos dice si el servicio se piensa manejar
	 * como externo
	 */
	private boolean esExterno;

	/**
	 * Código del tipo de servicio buscado
	 */
	private char tipoServicioBuscado;

	/**
	 * Boolean que indica si se debe restringir por
	 * formulario
	 */
	private boolean restringirPorFormulario;
	
	/**
	 * Código de la cuenta del paciente al que se
	 * le va a realizar la solicitud, es necesaria
	 * para validar que no haya más de una 
	 * solicitud de interconsulta por paciente
	 * - especialidad
	 */
	private int idCuenta;
	
	/**
	 * Código de la ocupación solicitada
	 */
	private String ocupacionSolicitada;
	

	/**
	 * 
	 */
	private boolean porCodigoCUPS;

	private int limit;
	
	private int offset;

	/**
	 * Al trabajar con Tags siempre tenemos que definir al menos este método.
	 * Siempre se ejecuta antes que doEndTag. Para los Tags hechos anteriormente
	 * se manejaba toda la funcionalidad en este método, ahora lo limitamos
	 * únicamente a validar que los datos que llegan ni sean nulos, ni sean
	 * vacios
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		
		PersonaBasica paciente=((PersonaBasica)this.pageContext.getSession().getAttribute("pacienteActivo"));
		if (criterioBusqueda != null && !criterioBusqueda.trim().equals("")&&paciente!=null) 
		{
			//Si es por código tratamos de partir esp-codigo, si no se puede
			//es porque es el número como tal
			if (!esPorNombre)
			{
				try
				{
					String arregloTemp[]=criterioBusqueda.split("-");
					if (arregloTemp.length==2)
					{
						this.criterioBusqueda=arregloTemp[1];
					}
				}
				catch (Exception e){}
			}
			
			this.setIdCuenta(paciente.getCodigoCuenta());

			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
		else 
		{
			return SKIP_BODY;
		}
	}

	/**
	 * Este método escribe en la página el valor y nombre por defecto pedidos
	 * según el parámetro, usando la presentación presente en la página. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) 
		{

			try 
			{
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	
			catch (Exception e) 
			{
					e.printStackTrace();
					throw new JspTagException("Error escribiendo TagBusquedaProcedimientosClinicos : " + e.getMessage());
			}

		}

		clean();
		return EVAL_PAGE;
	}

	/**
	 * Como Tomcat a partir de su versión 4.1.12 ? maneja un pool de Tags,
	 * tenemos que asegurarnos de limpiar los datos que recibimos, si no cuando
	 * Tomcat reutiliza este Tag nos vamos a encontrar con datos viejos /
	 * inconsistentes
	 */
	private void clean() 
	{
		this.criterioBusqueda = "";
		this.esPorNombre=false;
		this.buscarConFormulario=false;
		this.esExterno=false;
		this.porCodigoCUPS=false;
		this.restringirPorFormulario=false;
		this.tipoServicioBuscado=ConstantesBD.codigoServicioFalso;
		this.idCuenta=0;
		this.ocupacionSolicitada="-1024";
	}

	/**
	 * Este método obtiene de la BD los Servicios de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		TagDao tagDao;

		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		tagDao = myFactory.getTagDao();
		HashMap resultado;
		HttpSession sesion=this.pageContext.getSession();
		PersonaBasica paciente=(PersonaBasica)sesion.getAttribute("pacienteActivo");
		UsuarioBasico usuario = (UsuarioBasico)sesion.getAttribute("usuarioBasico");
		paciente.getCodigoSexo();

		try 
		{

			int codigoContratoPaciente=paciente.getCodigoContrato();
			
			/*
			 * Se necesita buscar en todos los esquemas tarifarios,
			 * cambiando el "false" por "true"
			 * en el parametro buscarEnTodosLosTarifarios
			 * creo que se soluciona el problema
			 */
			/*
			if (this.esExterno)
			{
				rs=tagDao.consultaTagBusquedaServicios(con, tipoServicioBuscado, criterioBusqueda, esPorNombre, 0, true, buscarConFormulario, paciente.getCodigoSexo(), codigoContratoPaciente, restringirPorFormulario, idCuenta, ocupacionSolicitada);
			}
			else
			{
				rs=tagDao.consultaTagBusquedaServicios(con, tipoServicioBuscado, criterioBusqueda, esPorNombre, codigoTarifarioActual, false, buscarConFormulario, paciente.getCodigoSexo(), codigoContratoPaciente, restringirPorFormulario, idCuenta, ocupacionSolicitada);
			}
			*/
			if (this.esExterno)
			{
				resultado=tagDao.consultaTagBusquedaServicios(con, tipoServicioBuscado, criterioBusqueda, esPorNombre, true, buscarConFormulario, paciente.getCodigoSexo(), codigoContratoPaciente, restringirPorFormulario, idCuenta, ocupacionSolicitada,porCodigoCUPS, usuario.getCodigoInstitucionInt(), offset, limit);
			}
			else
			{
				//AQUÍ ENTRA PARA PROCEDIMIENTOS
				boolean buscarEnTodosLosTarifarios=true;
				if(tipoServicioBuscado==ConstantesBD.codigoServicioProcedimiento||tipoServicioBuscado==ConstantesBD.codigoServicioInterconsulta)
					buscarEnTodosLosTarifarios=false;
				
				resultado=tagDao.consultaTagBusquedaServicios(con, tipoServicioBuscado, criterioBusqueda, esPorNombre, buscarEnTodosLosTarifarios, buscarConFormulario, paciente.getCodigoSexo(), codigoContratoPaciente, restringirPorFormulario, idCuenta, ocupacionSolicitada,porCodigoCUPS, usuario.getCodigoInstitucionInt(), offset, limit);
			}
			//Variables locales para no tener problemas con pools de tags
			
			Vector codigosTemp=new Vector(8,3);
			Vector nombresTemp=new Vector(8,3);
			Vector especialidadesTemp=new Vector(8,3);
			Vector codigosEspecialidadesTemp=new Vector(8,3);
			Vector esExcepcionTemp=new Vector(8,3);
			Vector posTemp=new Vector(8,3);
			Vector codigosPropietariosTemp=new Vector(8,3);			
			Vector solicitadasTemp=new Vector(8,3);
			Vector gruposServiciosTemp=new Vector(8,3);
			Vector tomaMuestraTemp=new Vector(8,3);
			Vector repuestaMultipleTemp=new Vector(8,3);
			Vector coberturaTemp=new Vector(8,3);
			Vector subcuentaTemp=new Vector(8,3);
			Vector justificarTemp=new Vector(8,3);
			Vector portatilTemp=new Vector(8,3);
			Vector grupoMultipleTemp=new Vector(8,3);
			
			int numRegistros=Integer.parseInt(resultado.get("numRegistros").toString());
			//Utilidades.imprimirMapa(resultado);
			for(int indexMapa=0; indexMapa<numRegistros; indexMapa++)	
			{
				
				if (UtilidadTexto.getBoolean(resultado.get("espos_"+indexMapa).toString()))
				{
					posTemp.add("t");
				}
				else
				{
					posTemp.add("f");
				}
				
				if (UtilidadTexto.getBoolean(resultado.get("esexcepcion_"+indexMapa).toString()))
				{
					esExcepcionTemp.add("t");
				}
				else
				{
					esExcepcionTemp.add("f");
				}
				if(UtilidadTexto.getBoolean(resultado.get("tomamuestra_"+indexMapa).toString()))
				{
					tomaMuestraTemp.add("t");
				}
				else
				{
					tomaMuestraTemp.add("f");
				}
				if(UtilidadTexto.getBoolean(resultado.get("respuestamultiple_"+indexMapa).toString()))
				{
					repuestaMultipleTemp.add("t");
				}
				else
				{
					repuestaMultipleTemp.add("f");
				}
				codigosTemp.add(resultado.get("codigo_"+indexMapa).toString());
				nombresTemp.add(resultado.get("nombre_"+indexMapa).toString());
				especialidadesTemp.add(resultado.get("nombreespecialidad_"+indexMapa).toString());
				codigosEspecialidadesTemp.add(resultado.get("especialidad_"+indexMapa).toString());
				codigosPropietariosTemp.add(resultado.get("codigopropietario_"+indexMapa).toString());
				if(UtilidadTexto.getBoolean(resultado.get("solicitada_"+indexMapa).toString()))
				{
					solicitadasTemp.add("t");
				}
				else
				{
					solicitadasTemp.add("f");
				}
				gruposServiciosTemp.add(resultado.get("gruposervicio_"+indexMapa).toString());
				portatilTemp.add(resultado.get("portatil_"+indexMapa).toString());
				if(UtilidadTexto.getBoolean(resultado.get("grupomultiple_"+indexMapa).toString()))
				{
					grupoMultipleTemp.add("t");
				}
				else
				{
					grupoMultipleTemp.add("f");
				}
				
				//Asignación de parametros para la justificación no pos
				boolean valProfesionalSalud = UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false);
				String justificar="false";
				
				if(!UtilidadTexto.getBoolean(resultado.get("espos_"+indexMapa).toString()))
				{					
					// Evaluamos la cobertura del Servicio
					InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
            		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Integer.parseInt(resultado.get("codigo_"+indexMapa).toString()), Integer.parseInt(usuario.getCodigoInstitucion().toString()), false, "" /*subCuentaCoberturaOPCIONAL*/);
            		coberturaTemp.add(infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
            		subcuentaTemp.add(infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");
            		
            		// Evaluamos si el convenio que cubre el servicio requiere de justificación de servicios
            		if (UtilidadesFacturacion.requiereJustificacioServ(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
            			justificar="true";
            			
            			// Validacion 'Especialidad profesional de la salud'
            			if (!valProfesionalSalud){
            				justificar="pendiente";
            			}
            		}
            		
            		justificarTemp.add(justificar);
            		
				}
				else{
					justificarTemp.add(justificar);
					coberturaTemp.add("");
					subcuentaTemp.add("");
				}
				
				
			}

			pageContext.setAttribute("esPos" ,  posTemp);
			pageContext.setAttribute("esExcepcion" ,  esExcepcionTemp);
			pageContext.setAttribute("codigos" ,  codigosTemp);
			pageContext.setAttribute("nombres",   nombresTemp);
			pageContext.setAttribute("especialidades",   especialidadesTemp);
			pageContext.setAttribute("codigosEspecialidades",   codigosEspecialidadesTemp);
			pageContext.setAttribute("codigosPropietarios", codigosPropietariosTemp);			
			pageContext.setAttribute("solicitadas", solicitadasTemp);
			pageContext.setAttribute("gruposServicios",gruposServiciosTemp);
			pageContext.setAttribute("tomaMuestra", tomaMuestraTemp);
			pageContext.setAttribute("repuestaMultiple",repuestaMultipleTemp);
			pageContext.setAttribute("cobertura",coberturaTemp);
			pageContext.setAttribute("subcuenta", subcuentaTemp);
			pageContext.setAttribute("justificar",justificarTemp);
			pageContext.setAttribute("portatil",portatilTemp);
			pageContext.setAttribute("grupoMultiple",grupoMultipleTemp);
			
			
			pageContext.setAttribute("numResultadosTotal", (Integer)resultado.get("numResultadosTotal"));
			

		}	catch (SQLException sqle) 
		{
				sqle.printStackTrace();
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag de Busqueda de Servicios: " + sqle.getMessage());
		}
		 catch (Exception e){
			 Log4JManager.error(e);
		 }
	}

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexión usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Método "Set" que recibe una conexion para permitir manejar todos los tags
	 * de una misma pagina con la misma conexión
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
		this.con = con;
	}

	/**
	 * @return
	 */
	public boolean getBuscarConFormulario()
	{
		return buscarConFormulario;
	}

	/**
	 * @return
	 */
	public String getCriterioBusqueda()
	{
		return criterioBusqueda;
	}

	/**
	 * @return
	 */
	public boolean getEsExterno()
	{
		return esExterno;
	}

	/**
	 * @return
	 */
	public boolean getEsPorNombre()
	{
		return esPorNombre;
	}

	/**
	 * @return
	 */
	public char getTipoServicioBuscado()
	{
		return tipoServicioBuscado;
	}

	/**
	 * @param b
	 */
	public void setBuscarConFormulario(boolean b)
	{
		buscarConFormulario = b;
	}

	/**
	 * @param string
	 */
	public void setCriterioBusqueda(String string)
	{
		criterioBusqueda = string;
	}

	/**
	 * @param b
	 */
	public void setEsExterno(boolean b)
	{
		esExterno = b;
	}

	/**
	 * @param b
	 */
	public void setEsPorNombre(boolean b)
	{
		esPorNombre = b;
	}

	/**
	 * @param c
	 */
	public void setTipoServicioBuscado(char c)
	{
		tipoServicioBuscado = c;
	}

	/**
	 * @param b
	 */
	public void setRestringirPorFormulario(boolean b)
	{
		restringirPorFormulario = b;
	}

	/**
	 * @return
	 */
	public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param i
	 */
	public void setIdCuenta(int i) {
		idCuenta = i;
	}

	/**
	 * @return
	 */
	public String getOcupacionSolicitada() {
		return ocupacionSolicitada;
	}

	/**
	 * @param i
	 */
	public void setOcupacionSolicitada(String i) {
		ocupacionSolicitada = i;
	}

	public boolean isPorCodigoCUPS() {
		return porCodigoCUPS;
	}

	public void setPorCodigoCUPS(boolean porCodigoCUPS) {
		this.porCodigoCUPS = porCodigoCUPS;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
