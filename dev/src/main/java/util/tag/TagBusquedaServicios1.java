/*
 * @(#)TagBusquedaServicios1.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * Tag que busca los servicios  para las siguientes funcionalidades: 
 * 
 * Generación de cargos pendientes 
 * Excepciones de servicios  
 * 
 * devuelve el código Axioma y la descripción CUPS y otra info del servicio - especialidad,
 * contemplando cada una de las restricciones impuestas en cada funcionalidad. 
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagBusquedaServicios1 extends BodyTagSupport
{
	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaServicios1.class);
	
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
	 * Boolean para saber si el tag es para 
	 * la generación de cargos pendientes = true, 
	 * o es para las excepciones de servicios= false
	 */
	private boolean tipoConsultaCargos;
	
	/**
	 * True= femenino
	 * False= masculino
	 */
	private int tipoSexo;
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "@";
	
	/**
	 * Indica si es ISS o es SOAT
	 */
	private int tipoTarifarioISSoSOAT;
	
	/**
	 * Código del contrato
	 */
	private int codigoContrato;
	
	/**
	 * 
	 */
	private String restTipoServicio;
	
	
	/**
	 * En caso de que el "tipoConsultaCargos=true" 
	 * se debe especificar si la consulta es para:
	 * 
	 *  1...solicitud de valoraciones iniciales de Urgencias u Hospitalización
	 *  2...solicitud de interconsulta - procedimiento - evoluciones
	 */
	private boolean esLinkSolicitudValoracionInicial;

	/**
	 * 
	 */
	private boolean validarActivo;
	
	/**
	 * 
	 */
	private boolean porCodigoCUPS;
	
	
	/**
	 * 
	 */
	private String atencionOdontologica;
	
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
			funcionalidad();
			return EVAL_BODY_BUFFERED;
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
					throw new JspTagException("Error escribiendo TagBusquedaServicios1 : " + e.getMessage());
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
		is_separador = "@";
		this.criterioBusqueda = "";
		this.esPorNombre=false;
		this.tipoConsultaCargos=false;
		this.tipoSexo=0;
		this.tipoTarifarioISSoSOAT=0;
		this.codigoContrato=0;
		this.esLinkSolicitudValoracionInicial=false;
		this.restTipoServicio = "";
		this.validarActivo=true;
		this.porCodigoCUPS=false;
		this.atencionOdontologica="";
		
	}
	
	/**
	 * Este método obtiene de la BD los Servicios de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		
		if(tipoTarifarioISSoSOAT<0)
			tipoTarifarioISSoSOAT=ConstantesBD.codigoTarifarioCups;
		
		logger.info("\n\n\nENTRA A LA BUSQUEDA DE SERVICIOS!!!!!!!!!!!");
		try
		{
			TagDao tagDao;
			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			

			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			Answer a;
			ResultSetDecorator rs = null;
			String consulta="";
			
			String consultaActivo=" AND s.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
			if(!validarActivo)
				consultaActivo=" ";
			
			
			
			/*En este caso es que la consulta es para las excepciones de servicios*/
			if(!this.tipoConsultaCargos)
			{	
				if(this.esPorNombre)
				{	
					logger.info("NO TENGO NI LA MENOR IDEA!!!!!!!!");
					consulta= 	"SELECT " +
									"ref.servicio AS codigoServicio, " +
									"ref.descripcion AS descripcionServicio, " +
									"s.especialidad AS especialidad," +
									"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS espos,  " +
									"administracion.getnombreespecialidad(s.especialidad) as nombreEspecialidad, "+
									"ref.codigo_propietario ||'-'|| getnombretarfioofi("+tipoTarifarioISSoSOAT+") ||'-'|| coalesce(s.minutos_duracion,"+ConstantesBD.codigoNuncaValido+") AS codigoPropietario " +
									"FROM " +
									"facturacion.referencias_servicio ref, " +
									"facturacion.servicios s " +
									"WHERE " +
									"ref.tipo_tarifario = "+tipoTarifarioISSoSOAT+"  " +
									"AND UPPER(descripcion) LIKE UPPER('%"+criterioBusqueda+"%') " +
									consultaActivo+
									" AND ref.servicio=s.codigo ";
									logger.info("restTipoServicio----------->"+restTipoServicio);
									try
									{	
										if(!this.restTipoServicio.equals("") && this.restTipoServicio!=null)
										{
											restTipoServicio="('D','C','P')";
										    consulta+="AND s.tipo_servicio IN "+restTipoServicio+"";
									}
									}catch(NullPointerException e)
									{}
				}
				else if(!this.esPorNombre)
				{	
					
					logger.info("===>Viene por Codigo CUPS 1: "+porCodigoCUPS);
					consulta=	"SELECT " +
									"ref.servicio AS codigoServicio, " +
									"ref.descripcion AS descripcionServicio, " +
									"s.especialidad AS especialidad, " +
									" CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS espos, " +
									"getnombreespecialidad(s.especialidad) as nombreEspecialidad, "+
									"ref.codigo_propietario ||'-'|| getnombretarfioofi("+tipoTarifarioISSoSOAT+") ||'-'|| coalesce(s.minutos_duracion,"+ConstantesBD.codigoNuncaValido+")   AS codigoPropietario " +
									"FROM " +
									"referencias_servicio ref, " +
									"servicios s " +
									"WHERE " +
									"ref.tipo_tarifario = "+tipoTarifarioISSoSOAT+"  " +
									consultaActivo+
									" AND ref.servicio=s.codigo ";
									
									if(porCodigoCUPS)
										consulta=consulta+" AND ref.codigo_propietario = '"+criterioBusqueda+"' ";
									else
										consulta=consulta+" AND ref.servicio = "+criterioBusqueda ;
									
									try
									{
										if(!this.restTipoServicio.equals("") && this.restTipoServicio!=null)
										{
											restTipoServicio="('D','C','P')";
										    consulta+=" AND s.tipo_servicio IN "+restTipoServicio+"";
										}
									}catch(NullPointerException e)
									{}	
				}	
			}
			
			/*caso en el cual es para la generación de cargos pendientes*/ 
			else if(this.tipoConsultaCargos)
			{
				/*EN CASO QUE SEA UNA SOLICITUD DE VALORACIÓN INICIAL DE URGENCIAS U HOSPITALIZACIÓN Y EVOLUCIONES*/
				if(this.esLinkSolicitudValoracionInicial)
				{	
					//SE FILTRAN LOS SERVICIOS DE TIPO CONSULTA
					if(this.esPorNombre)
					{	
						consulta = " SELECT "+  
							"(s.especialidad ||'-'|| s.codigo) AS codigoAxioma, "+
							"CASE WHEN  facturacion.getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") IS NULL THEN " +
								"' ' " +
							"ELSE " +
								"facturacion.getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") " +
							"END AS codigoISSoSOAT, "+
							"ref.descripcion AS descripcionServicio, "+ 
							"e.nombre AS descripcionEspecialidad, "+ 
							"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'POS' ELSE 'NOPOS' END  AS tipoPos, "+ 
							"CASE WHEN exep.servicio IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS existeExcepcion, "+ 
							"CASE WHEN ((s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND exep.servicio IS NOT NULL) OR (s.espos="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND exep.servicio IS NULL)) THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS cubreConvenio, "+
							"facturacion.getcodigopropservicio2(s.codigo, "+tipoTarifarioISSoSOAT+") AS codigoCups "+
							"FROM facturacion.servicios s "+ 
							"INNER JOIN facturacion.referencias_servicio ref ON (s.codigo=ref.servicio ) "+ 
							"INNER JOIN administracion.especialidades e ON (e.codigo=s.especialidad) "+ 
							"LEFT OUTER JOIN (SELECT servicio, contrato FROM facturacion.excepciones_servicios WHERE contrato="+codigoContrato+") exep ON(s.codigo=exep.servicio) " + 
							"WHERE "+ 
							"ref.tipo_tarifario="+tipoTarifarioISSoSOAT+" AND "+ ////////////
							"UPPER(ref.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') " +
							consultaActivo+
							" AND "+ 
							"s.tipo_servicio = '"+ConstantesBD.codigoServicioCargosConsultaExterna+"' AND "+
							"(s.sexo is null OR s.sexo="+tipoSexo+") ";
							
					}	
					else if(!this.esPorNombre)
					{	
						logger.info("===>Viene por Codigo CUPS 2: "+porCodigoCUPS);
						consulta = " SELECT " +  
							"(s.especialidad ||'-'|| s.codigo) AS codigoAxioma, "+ 
							"CASE WHEN  getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") IS NULL THEN " +
								"' ' " +
							"ELSE " +
								"getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") " +
							"END AS codigoISSoSOAT, "+
							"ref.descripcion AS descripcionServicio, "+ 
							"e.nombre AS descripcionEspecialidad, "+ 
							"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'POS' ELSE 'NOPOS' END  AS tipoPos, "+ 
							"CASE WHEN exep.servicio IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+"  ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS existeExcepcion, "+ 
							"CASE WHEN ((s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND exep.servicio IS NOT NULL) OR (s.espos="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND exep.servicio IS NULL)) THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS cubreConvenio, "+
							"getcodigopropservicio2(s.codigo, "+tipoTarifarioISSoSOAT+") AS codigoCups "+
							"FROM servicios s "+ 
							"INNER JOIN referencias_servicio ref ON (s.codigo=ref.servicio  ) "+ 
							"INNER JOIN especialidades e ON (e.codigo=s.especialidad) "+ 
							"LEFT OUTER JOIN (SELECT servicio, contrato FROM excepciones_servicios WHERE contrato="+codigoContrato+") exep ON(s.codigo=exep.servicio) "+ 
							"WHERE "+ 
							"ref.tipo_tarifario="+tipoTarifarioISSoSOAT+" AND ";
							
							if(porCodigoCUPS)
								consulta=consulta+"ref.codigo_propietario = '"+criterioBusqueda+"' ";
							else
								consulta=consulta+"ref.servicio = "+criterioBusqueda ;
							
							consulta=consulta+consultaActivo+
							" AND "+ 
							"s.tipo_servicio = '"+ConstantesBD.codigoServicioCargosConsultaExterna+"' AND "+
							"(s.sexo is null OR s.sexo="+tipoSexo+") ";
					}	
				}
				/*EN CASO QUE LA SOLICITUD SEA DE  INTERCONSULTA, PROCEDIMIENTOS Y CARGOS DIRECTOS */
				else
				{
					//NO SE FILTRAN LOS SERVICIOS DE TIPO CONSULTA
					if(this.esPorNombre)
					{	
						
						consulta = " SELECT "+  
						"(s.especialidad ||'-'|| s.codigo) AS codigoAxioma, "+ 
						"CASE WHEN  getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") IS NULL THEN " +
							"' ' " +
						"ELSE " +
							"getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") " +
						"END AS codigoISSoSOAT, "+
						"ref.descripcion AS descripcionServicio, "+ 
						"e.nombre AS descripcionEspecialidad, "+ 
						"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'POS' ELSE 'NOPOS' END  AS tipoPos, "+ 
						"CASE WHEN exep.servicio IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS existeExcepcion, "+ 
						"CASE WHEN ((s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND exep.servicio IS NOT NULL) OR (s.espos="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND exep.servicio IS NULL)) THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS cubreConvenio, "+
						"getcodigopropservicio2(s.codigo, "+tipoTarifarioISSoSOAT+") AS codigoCups "+
						"FROM servicios s "+ 
						"INNER JOIN referencias_servicio ref ON (s.codigo=ref.servicio ) "+ 
						"INNER JOIN especialidades e ON (e.codigo=s.especialidad) "+ 
						"LEFT OUTER JOIN (SELECT servicio, contrato FROM excepciones_servicios WHERE contrato="+codigoContrato+") exep ON(s.codigo=exep.servicio) " + 
						"WHERE "+ 
						"ref.tipo_tarifario="+tipoTarifarioISSoSOAT+" AND "+ 
						"UPPER(ref.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') " +
						consultaActivo+
						" AND "+ 
						"(s.sexo is null OR s.sexo="+tipoSexo+") ";
							
					}
					else if(!this.esPorNombre)
					{	
						logger.info("===>Viene por Codigo CUPS 3: "+porCodigoCUPS);
						//se revisa que el código del servicio no venga con especialidad
						String vectorCriterio[] = criterioBusqueda.split("-");
						if(vectorCriterio.length>1)
							criterioBusqueda = vectorCriterio[1];
						
						consulta = " SELECT " +  
						"(s.especialidad ||'-'|| s.codigo) AS codigoAxioma, "+
						"CASE WHEN  getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") IS NULL THEN " +
							"' ' " +
						"ELSE " +
							"getcodigopropservicio2(s.codigo,"+tipoTarifarioISSoSOAT+") " +
						"END AS codigoISSoSOAT, "+
						"ref.descripcion AS descripcionServicio, "+ 
						"e.nombre AS descripcionEspecialidad, "+ 
						"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'POS' ELSE 'NOPOS' END  AS tipoPos, "+ 
						"CASE WHEN exep.servicio IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS existeExcepcion, "+ 
						"CASE WHEN ((s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND exep.servicio IS NOT NULL) OR (s.espos="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND exep.servicio IS NULL)) THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS cubreConvenio, "+
						"getcodigopropservicio2(s.codigo, "+tipoTarifarioISSoSOAT+") AS codigoCups "+
						"FROM servicios s "+ 
						"INNER JOIN referencias_servicio ref ON (s.codigo=ref.servicio  ) "+ 
						"INNER JOIN especialidades e ON (e.codigo=s.especialidad) "+ 
						"LEFT OUTER JOIN (SELECT servicio, contrato FROM excepciones_servicios WHERE contrato="+codigoContrato+") exep ON(s.codigo=exep.servicio) "+ 
						"WHERE "+ 
						"ref.tipo_tarifario="+tipoTarifarioISSoSOAT+" AND ";
						
						if(porCodigoCUPS)
							consulta=consulta+" ref.codigo_propietario = '"+criterioBusqueda+"' ";
						else
							consulta=consulta+" ref.servicio = "+criterioBusqueda ;
						
						consulta=consulta+consultaActivo+
						" AND "+ 
						"(s.sexo is null OR s.sexo="+tipoSexo+") ";
					}		
					this.esLinkSolicitudValoracionInicial = true; //vuelve y se reestablece el valor para la toma de datos
				}
			}
			
			if(!UtilidadTexto.isEmpty(this.atencionOdontologica))
			{
				consulta+=" and s.atencion_odontologica='"+this.atencionOdontologica+"'";
			}
			
			
			logger.info("--------------->CONSULTA SERVICIOS 1->"+consulta);
			
			a=tagDao.resultadoConsulta(con, consulta);

			rs  = a.getResultSet();
			con = a.getConnection();
			
			//Variables locales para no tener problemas con pools de conexiones

			Vector resultados=new Vector(15,5);
			String parejasResultado="";

			boolean primerEncuentro;
			
			while (rs.next())
			{
				//No he encontrado el primer campo, en cuyo caso NO debo poner
				//guión anterior
				primerEncuentro=true;
				parejasResultado="";
				
				//Si es la primera vez que lo encontramos, ponemos el valor
				//en true y si no ponemos guion anterior
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(1);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(2);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(3);

				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(4);
				
				
				
				
				
				
				if(!this.tipoConsultaCargos)
				{	
										
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString("nombreespecialidad");
					
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString("codigopropietario");
					
					
				}
				
				/* para la generación de cargos pendientes 
				 * de la solicitud de procedimientos - interconsultas - evoluciones
				 * se tienen 5 columnas*/
				if(this.tipoConsultaCargos)
				{	
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(5);
				}
				
				/* para las solicitudes de valoraciones iniciales 
				 * de urgencias y hospitalización se tienen 7 columnas*/
				if(this.tipoConsultaCargos && this.esLinkSolicitudValoracionInicial)
				{
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(6);
					
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(7);
				}
				
				/*
				 * Campo adicionado para meter el codigo CUPS de los servicios
				 * se realizo en las consultas de Tipo Consulta Cargos
				 */
				if(this.tipoConsultaCargos)
				{	
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(8);
				}
				
				//logger.info("parejasResultado=> "+parejasResultado);
				resultados.add(parejasResultado);  
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.error(e.getMessage());
			throw new JspTagException("TagBusquedaServicios1: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagBusquedaServicios1: "+e.getMessage()+e.toString());
		}
	}

	/** Obtiene el separador de la cadena de resultados */
	public String getSeparador()
	{
		return is_separador;
	}
	
	/** Asigna el separador de la cadena de resultados */
	public void setSeparador(String as_separador)
	{
		if(as_separador != null && !(as_separador = as_separador.trim() ).equals("") )
			is_separador = as_separador;
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
	public String getCriterioBusqueda()
	{
		return criterioBusqueda;
	}

	/**
	 * @return
	 */
	public boolean getEsPorNombre()
	{
		return esPorNombre;
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
	public void setEsPorNombre(boolean b)
	{
		esPorNombre = b;
	}


	/**
	 * @return Returns the tipoConsulta.
	 */
	public boolean getTipoConsultaCargos() {
		return tipoConsultaCargos;
	}
	/**
	 * @param tipoConsulta The tipoConsulta to set.
	 */
	public void setTipoConsultaCargos(boolean tipoConsultaCargos) {
		this.tipoConsultaCargos = tipoConsultaCargos;
	}
	/**
	 * @return Returns the tipoSexo.
	 */
	public int getTipoSexo() {
		return tipoSexo;
	}
	/**
	 * @param tipoSexo The tipoSexo to set.
	 */
	public void setTipoSexo(int tipoSexo) {
		this.tipoSexo = tipoSexo;
	}
	/**
	 * @return Returns the tipoTarifarioISSoSOAT.
	 */
	public int getTipoTarifarioISSoSOAT() {
		return tipoTarifarioISSoSOAT;
	}
	/**
	 * @param tipoTarifarioISSoSOAT The tipoTarifarioISSoSOAT to set.
	 */
	public void setTipoTarifarioISSoSOAT(int tipoTarifarioISSoSOAT) {
		this.tipoTarifarioISSoSOAT = tipoTarifarioISSoSOAT;
	}
	/**
	 * @return Returns the codigoContrato.
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * @param codigoContrato The codigoContrato to set.
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	/**
	 * @return Returns the esLinkSolicitudValoracionInicial.
	 */
	public boolean getEsLinkSolicitudValoracionInicial() {
		return esLinkSolicitudValoracionInicial;
	}
	/**
	 * @param esLinkSolicitudValoracionInicial The esLinkSolicitudValoracionInicial to set.
	 */
	public void setEsLinkSolicitudValoracionInicial(
			boolean esLinkSolicitudValoracionInicial) {
		this.esLinkSolicitudValoracionInicial = esLinkSolicitudValoracionInicial;
	}
    /**
     * @return Retorna restTipoServicio.
     */
    public String getRestTipoServicio() {
        return restTipoServicio;
    }
    /**
     * @param restTipoServicio Asigna restTipoServicio.
     */
    public void setRestTipoServicio(String restTipoServicio) {
        this.restTipoServicio = restTipoServicio;
    }

	/**
	 * @return Returns the validarActivo.
	 */
	public boolean getValidarActivo()
	{
		return validarActivo;
	}

	/**
	 * @param validarActivo The validarActivo to set.
	 */
	public void setValidarActivo(boolean validarActivo)
	{
		this.validarActivo = validarActivo;
	}

	public boolean isPorCodigoCUPS() {
		return porCodigoCUPS;
	}

	public void setPorCodigoCUPS(boolean porCodigoCUPS) {
		this.porCodigoCUPS = porCodigoCUPS;
	}

	public void setAtencionOdontologica(String atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}

	public String getAtencionOdontologica() {
		return atencionOdontologica;
	}
}
