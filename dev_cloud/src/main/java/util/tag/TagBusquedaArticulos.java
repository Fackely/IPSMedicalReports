/*
 * @(#)TagBusquedaArticulos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package util.tag;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Tag que busca los articulos 
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagBusquedaArticulos extends BodyTagSupport
{
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaArticulos.class);
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * String con el criterio de busqueda del servicio
	 */
	private String criterioBusqueda="";
	
	/**
	 * Boolean que nos dice si la búsqueda es por nombre
	 * o por codigo
	 */
	private boolean esPorNombre;

	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "@@@@";
	
	/**
	 * Número de la solicitud, dato requerido solo
	 * para Despacho Medicamentos
	 */
	private int numeroSolicitud;
	
	/**
	 * Entero para determinar el tipo de consulta 
	 * y ejecutar el correspondiente select
	 * 
	 * Sustituto inventarios=1-100;
	 * Tarifas de inventarios=2;
	 * Despachos de medicamentos=3;
	 */
	private int tipoConsulta;
	
	private HashMap articulosSeleccionadosMap;
	
	private int numeroIngresos;
	
	/**
	 * Indica si el articulo que se quiere buscar es
	 * ne aturaleza insumos
	 */
	private boolean esInsumo;

	/**
	 * codigo de la institucion
	 * [campo no requerido]
	 */
	private int codigoInstitucion;
	
	/**
	 * indica si debe filtrar por inventarios y transaccion
	 * */
	private boolean filtrarXInventarios;
	
	/**
	 * codigo del almacen 
	 * [campo no requerido]
	 */
	private int codigoAlmacen;
	
	/**
	 * codigo de la transaccion valida
	 * */
	private int codigoTransaccion;
	
	private String validarEstado=ConstantesBD.acronimoSi;
	
	private String controlEspecial;
	
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
					throw new JspTagException("Error escribiendo TagBusquedaArticulos : " + e.getMessage());
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
		is_separador = "@@@@";
		this.criterioBusqueda = "";
		this.esPorNombre=false;
		this.tipoConsulta=0;
		this.numeroSolicitud=0;
		this.articulosSeleccionadosMap= new HashMap();
		this.numeroIngresos=0;
		this.codigoInstitucion=0;
		this.codigoAlmacen=0;
		this.filtrarXInventarios = false;
		this.controlEspecial="";
	}
	
	/**
	 * Este método obtiene de la BD las Tarifas de acuerdo a los
	 * parámetros pasados a este tag, y los pone en el contexto de la 
	 * página como <i>scripting variables</i>.
	 */
	private void funcionalidad() throws JspTagException 
	{
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
		{
			
			String consulta="";
			
			String restriccionAnteriores="";
		
			if (this.articulosSeleccionadosMap!=null)
			{
				String temp;
				
				for(int w=0; w<this.numeroIngresos; w++)
				{
					temp= (String) getArticulosSeleccionadosMap("articulo_"+w);
					String temp1[]=temp.split("-",2);
					restriccionAnteriores=restriccionAnteriores+" and a.codigo<> "+temp1[0];
				}
			}
			
			//logger.info("LOS ARTICULOS SELECCIONADOS SON= "+restriccionAnteriores);
			
			
			if (this.articulosSeleccionadosMap!=null)
			{
				logger.info("hash map no nulo");
			}
			
			if(this.tipoConsulta==1||this.tipoConsulta==100)
			{	
				if(this.esPorNombre)
				{	
					consulta= "SELECT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida  " +
									"  FROM " +
									"  articulo a " +
									"  inner join naturaleza_articulo na ON (na.acronimo=a.naturaleza and na.institucion=a.institucion)" +
									//arreglo tarea 280125
									/*
									 Al seleccionar búsqueda por nombre NO _debe traer insumos solo debe traer MEDICAMENTOS
									 Diana Beltran
									 */
									"  WHERE na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
									" AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') ")) +" order by 2" ;
				}
				else if(!this.esPorNombre)
				{	
					consulta= "SELECT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida  " +
									"  FROM " +
									"  articulo a " +
									"  inner join naturaleza_articulo na ON (na.acronimo=a.naturaleza and na.institucion=a.institucion) " +
									"  WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
									//Para buscar por un codigo especifico en el tag. tipo de consulta 100
									if(this.tipoConsulta==100)
									{
										consulta+="and a.codigo="+criterioBusqueda;
											
									}
									else
									{
										consulta+= (criterioBusqueda.equals("")?"":("and  a.codigo ="+criterioBusqueda));
									}
									consulta+=" order by 2";
				}	
			}
			else if(this.tipoConsulta==2)
			{
				if(this.esPorNombre)
				{	
					consulta= "SELECT " +
					"  a.codigo AS codigo" +
					", a.descripcion AS descripcion" +
					", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
					", coalesce(a.minsalud,'') AS codigoMinSalud" +
					", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
					", coalesce(a.concentracion, '') AS concentracion" +
					", getNomUnidadMedida(a.unidad_medida) AS unidadMedida  " +
					"  FROM " +
					"  articulo a " +
					"  WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') ")) ;
				}	
				else if(!this.esPorNombre)
				{
					consulta= "SELECT " +
					"  a.codigo AS codigo" +
					", a.descripcion AS descripcion" +
					", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
					", coalesce(a.minsalud,'') AS codigoMinSalud" +
					", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
					", coalesce(a.concentracion, '') AS concentracion " +
					", getNomUnidadMedida(a.unidad_medida) AS unidadMedida  " +
					"  FROM " +
					"  articulo a " +
					"  WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
					
					//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
					logger.info("===>Codigo Institucion: "+codigoInstitucion);
					logger.info("===>Valor por defecto: "+ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion));
					if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
					{
						logger.info("===>Se realiza el filtro por el Código Axioma");
						consulta+=" AND a.codigo = "+criterioBusqueda+" ";
					}
					else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
					{
						logger.info("===>Se realiza el filtro por el Código Interfaz");
						consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
					}
					
				}	
			}
			else if(this.tipoConsulta==3)
			{
				if(this.esPorNombre)
				{	
					consulta= 	"SELECT  distinct " +
								"a.codigo,  " +
								"a.descripcion,  " +
								"getNomUnidadMedida(a.unidad_medida), " +
								"getInfoArticulos("+numeroSolicitud+",a.codigo) as cantidad,  " +
								"getTotalExisArticulosXAlmacen( "+codigoAlmacen+" , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen " +
								"FROM " +
								"articulo a, subgrupo_inventario si, trans_validas_x_cc_inven tv, naturaleza_articulo na  " +
								"WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and na.institucion=a.institucion and a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'  " +
								"and na.es_medicamento='"+ConstantesBD.acronimoNo+"' " +
								"and UPPER(a.descripcion) LIKE UPPER ('%"+criterioBusqueda+"%') "+restriccionAnteriores+ " "+
								"  AND tv.centros_costo= "+codigoAlmacen+
								//CAMBIO SHAIO 728
								//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
								"  AND (tv.clase_inventario=si.clase) " +
								"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
								"  AND tv.institucion="+codigoInstitucion;
				}	
				else if(!this.esPorNombre)
				{
					consulta= 	"SELECT distinct " +
								"a.codigo,  " +
								"a.descripcion,  " +
								"getNomUnidadMedida(a.unidad_medida), " +
								"getInfoArticulos("+numeroSolicitud+",a.codigo) as cantidad,  " +
								"getTotalExisArticulosXAlmacen( "+codigoAlmacen+" , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen " +
								"FROM " +
								"articulo a, subgrupo_inventario si, trans_validas_x_cc_inven tv, naturaleza_articulo na  " +
								"WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and na.institucion=a.institucion and a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'  " +
								"and na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
								
								if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
								{
									logger.info("===>Se realiza el filtro por el Código Axioma");
									consulta+=" AND a.codigo = "+criterioBusqueda+" ";
								}
								else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
								{
									logger.info("===>Se realiza el filtro por el Código Interfaz");
									consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
								}
										
					consulta+=	" "+restriccionAnteriores+" " +
								"  AND tv.centros_costo= "+codigoAlmacen+
								//CAMBIO SHAIO 728
								//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
								"  AND (tv.clase_inventario=si.clase) " +
								"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
								"  AND tv.institucion="+codigoInstitucion;;
				}
			}
			else if(this.tipoConsulta==4)
			{
				String restriccionNaturaleza;
				if(esInsumo)
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
				}
				else
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoSi+"'";
				}
				if(this.esPorNombre)
				{
					consulta= "SELECT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion " +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									"  FROM " +
									"   ";
					consulta += esInsumo&&filtrarXInventarios?" subgrupo_inventario si ,trans_validas_x_cc_inven tv, ":" ";
					consulta +=	" articulo a " +
								" INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo and na.institucion=a.institucion)" +
								" WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') "))+
								restriccionNaturaleza;
				}
				else if(!this.esPorNombre)
				{	
					consulta= "SELECT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									"  FROM " +
									"   ";
					consulta += esInsumo&&filtrarXInventarios?" subgrupo_inventario si ,trans_validas_x_cc_inven tv, ":" ";	
					consulta +=	"articulo a " +
								"INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo and na.institucion=a.institucion)" +
								"WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
									
									//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
									{
										logger.info("===>Se realiza el filtro por el Código Axioma");
										consulta+=" AND a.codigo = "+criterioBusqueda+" ";
									}
									else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										logger.info("===>Se realiza el filtro por el Código Interfaz");
										consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
									}
									
									consulta += restriccionNaturaleza;
				}	
				//logger.info(consulta);
				
				//es insumo y se filtra por inventarios
				if(esInsumo && filtrarXInventarios)
				{
					consulta += " AND a.subgrupo = si.codigo " +
								" AND tv.centros_costo="+codigoAlmacen+" "+								
								" AND (tv.clase_inventario=si.clase) " +	
								" AND tv.tipos_trans_inventario= " +codigoTransaccion+
								" AND tv.institucion="+codigoInstitucion+" " +
								" AND si.grupo = tv.grupo_inventario ";
				}
				
				consulta +="  ORDER BY a.descripcion";
			}
			else if(this.tipoConsulta==5)
			{	
				if(this.esPorNombre)
				{
					logger.error("El tipo de consulta "+tipoConsulta+" sólo está definida para búsqueda por código");
				}
				else if(!this.esPorNombre)
				{	
					consulta= "SELECT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos AS es_pos" +
									", ca.control_especial AS controlespecial " +
									"  FROM " +
									"  articulo a INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
									"  INNER JOIN categoria_articulos ca ON (a.categoria=ca.codigo) " +
									"  WHERE ";
					
									//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
									{
										logger.info("===>Se realiza el filtro por el Código Axioma");
										consulta+=" a.codigo = "+criterioBusqueda+" ";
									}
									else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										logger.info("===>Se realiza el filtro por el Código Interfaz");
										consulta+=" a.codigo_interfaz = '"+criterioBusqueda+"' ";
									}
					
					if(UtilidadTexto.getBoolean(validarEstado))
					{
						consulta=consulta+" and a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'";
					}
				}	
			}
			
			else if(this.tipoConsulta==6)
			{
				if(this.esPorNombre)
				{	
					consulta= 	"SELECT  " +
								"a.codigo, " +
								"a.descripcion, " +
								"coalesce(a.concentracion, '') AS concentracion, " +
								"getNomFormaFarmaceutica(a.forma_farmaceutica), " +								
								"getNomUnidadMedida(a.unidad_medida), " +
								"inventarios.getNomNaturalezaArticulo(a.naturaleza) "+
								"FROM " +
								"articulo a  " +
								"WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'  " +
								"and UPPER(a.descripcion) LIKE UPPER ('%"+criterioBusqueda+"%') "+restriccionAnteriores+ " ";
					
				}	
				else if(!this.esPorNombre)
				{
					consulta= 	"SELECT  " +
								"a.codigo, " +
								"a.descripcion, " +
								"coalesce(a.concentracion, '') AS concentracion, " +
								"getNomFormaFarmaceutica(a.forma_farmaceutica), " +								
								"getNomUnidadMedida(a.unidad_medida), " +
								"inventarios.getNomNaturalezaArticulo(a.naturaleza) "+
								"FROM " +
								"articulo a  " +
								"WHERE a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'  ";
					
								//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
								if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
								{
									logger.info("===>Se realiza el filtro por el Código Axioma");
									consulta+=" AND a.codigo = "+criterioBusqueda+" ";
								}
								else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
								{
									logger.info("===>Se realiza el filtro por el Código Interfaz");
									consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
								}
								
								consulta += restriccionAnteriores;
					
				}
			}
			else if(this.tipoConsulta==7)
			{
				String restriccionNaturaleza;
			 
				
				if(esInsumo)
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
				}
				else
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoSi+"' ";
				}
				if(this.esPorNombre)
				{
					consulta= "SELECT distinct " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									", ca.control_especial" +
									", getTotalExisArticulosXAlmacen("+codigoAlmacen+", a.codigo,  "+codigoInstitucion+") AS existenciaXAlmacen " +
									", a.codigo_interfaz AS codigo_interfaz" +
									"  FROM " +
									"  articulo a, subgrupo_inventario si, trans_validas_x_cc_inven tv, naturaleza_articulo na, categoria_articulos ca " +
									"  WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and a.institucion=na.institucion " +
									"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " 
									+(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') "))+
									restriccionNaturaleza+
									"  AND tv.centros_costo= "+codigoAlmacen+
									//CAMBIO SHAIO 728
									//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
									"  AND (tv.clase_inventario=si.clase) " +
									"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
									"  AND tv.institucion="+codigoInstitucion;
									//"  AND ca.codigo=a.categoria";
								   	
									if (controlEspecial.equals(ConstantesBD.acronimoSi))
								   		consulta+=" AND ca.codigo=a.categoria AND ca.control_especial='"+ConstantesBD.acronimoSi+"' ";
									else
										consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoNo+"' ";
								   				  
									
								   	consulta+="  ORDER BY a.descripcion";
				}
				else if(!this.esPorNombre)
				{   
					consulta="SELECT distinct " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									", getTotalExisArticulosXAlmacen("+codigoAlmacen+", a.codigo,  "+codigoInstitucion+") AS existenciaXAlmacen " +
									", a.codigo_interfaz AS codigo_interfaz" +
									"  FROM " +
									"  articulo a, subgrupo_inventario si, trans_validas_x_cc_inven tv, naturaleza_articulo na, categoria_articulos ca " +
									"  WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and a.institucion=na.institucion " +
									"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
											
									//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
									{
										logger.info("===>Se realiza el filtro por el Código Axioma");
										consulta+=" AND a.codigo = "+criterioBusqueda+" ";
									}
									else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										logger.info("===>Se realiza el filtro por el Código Interfaz");
										consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
									}
									//Mt 6828
									if (controlEspecial.equals(ConstantesBD.acronimoSi)){
								   		consulta+=" AND ca.codigo=a.categoria AND ca.control_especial='"+ConstantesBD.acronimoSi+"' ";
									}
									else{
										consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoNo+"' ";
									}
					
									consulta += restriccionNaturaleza+
												"  AND tv.centros_costo= "+codigoAlmacen+
												//CAMBIO SHAIO 728
												//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
												"  AND (tv.clase_inventario=si.clase) " +
												"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
												"  AND tv.institucion="+codigoInstitucion+
												"  ORDER BY a.descripcion";
				}   
				logger.info(consulta);
			}
			else if(this.tipoConsulta==8)
			{
				String restriccionNaturaleza;
				if(esInsumo)
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoNo+"'";
				}
				else
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoSi+"'";
				}
				if(this.esPorNombre)
				{
					consulta= "SELECT distinct " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza " +
									", getTotalExisArticulosXAlmacen("+codigoAlmacen+", a.codigo,  "+codigoInstitucion+") AS existenciaXAlmacen " +
									"  FROM " +
									"  articulo a, subgrupo_inventario si, naturaleza_articulo na, trans_validas_x_cc_inven tv " +
									"  WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and na.institucion=a.institucion " +
									"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') "))+
									restriccionNaturaleza+
									"  AND tv.centros_costo= "+codigoAlmacen+
									//CAMBIO SHAIO 728
									//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
									"  AND (tv.clase_inventario=si.clase) " +
									"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
									"  AND tv.institucion="+codigoInstitucion+
									"  ORDER BY a.descripcion";
				}
				else if(!this.esPorNombre)
				{   
					consulta= "SELECT distinct " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", inventarios.getNomNaturalezaArticulo(a.naturaleza) AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									", getTotalExisArticulosXAlmacen("+codigoAlmacen+", a.codigo,  "+codigoInstitucion+") AS existenciaXAlmacen " +
									"  FROM " +
									"  articulo a, subgrupo_inventario si, naturaleza_articulo na, trans_validas_x_cc_inven tv " +
									"  WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo  and na.institucion=a.institucion " +
									"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
									
									//Modificado por la Tarea 38488 lo cual argumenta que debe evaluar el parametro general Código Manual para Búsqueda de Artículos (Axioma, Interfaz)
									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
									{
										logger.info("===>Se realiza el filtro por el Código Axioma");
										consulta+=" AND a.codigo = "+criterioBusqueda+" ";
									}
									else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										logger.info("===>Se realiza el filtro por el Código Interfaz");
										consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
									}
					
									consulta += restriccionNaturaleza+
												"  AND tv.centros_costo= "+codigoAlmacen+
												//CAMBIO SHAIO 728
												//"  AND (tv.clase_inventario=si.clase AND tv.grupo_inventario=si.grupo) " +
												"  AND (tv.clase_inventario=si.clase) " +
												"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
												"  AND tv.institucion="+codigoInstitucion+
												"  ORDER BY a.descripcion";
				}   
				//logger.info(consulta);
			}
			else if(this.tipoConsulta==9)
			{
				String restriccionNaturaleza;
				if(esInsumo)
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
				}
				else
				{
					restriccionNaturaleza=" AND na.es_medicamento='"+ConstantesBD.acronimoSi+"'";
				}
				if(this.esPorNombre)
				{
					consulta= "SELECT DISTINCT " +
										"  a.codigo AS codigo" +
										", a.descripcion AS descripcion" +
										", na.nombre AS naturaleza" +
										", coalesce(a.minsalud,'') AS codigoMinSalud" +
										", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
										", coalesce(a.concentracion, '') AS concentracion " +
										", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
										", na.es_pos" +
										", a.naturaleza AS codigoNaturaleza" +
									"  FROM " +
									"   ";					
					consulta +=	" articulo a " +
								" INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo and na.institucion=a.institucion)" +
								" INNER JOIN categoria_articulos ca ON(ca.codigo=a.categoria ) "+
                                " INNER JOIN trans_validas_x_cc_inven tv ON (tv.institucion= na.institucion) "+ 
                                " INNER JOIN subgrupo_inventario si ON (si.clase=tv.clase_inventario)  "+
								" WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and a.institucion=na.institucion " +
								"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +(criterioBusqueda.equals("")?"": ("and UPPER(a.descripcion) LIKE UPPER('%"+criterioBusqueda+"%') "))+
								"  AND (tv.clase_inventario=si.clase) " +
								"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
								"  AND tv.institucion="+codigoInstitucion+
								"  AND ca.codigo=a.categoria "+
								"  AND tv.centros_costo= "+codigoAlmacen+" "+ 
								restriccionNaturaleza;
								
								
						
				}
				else if(!this.esPorNombre)
				{	
					consulta= "SELECT DISTINCT " +
									"  a.codigo AS codigo" +
									", a.descripcion AS descripcion" +
									", na.nombre AS naturaleza" +
									", coalesce(a.minsalud,'') AS codigoMinSalud" +
									", getNomFormaFarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica" +
									", coalesce(a.concentracion, '') AS concentracion" +
									", getNomUnidadMedida(a.unidad_medida) AS unidadMedida" +
									", na.es_pos" +
									", a.naturaleza AS codigoNaturaleza" +
									"  FROM " +
									"   ";					
					consulta +=	"articulo a " +
								"INNER JOIN naturaleza_articulo na ON(a.naturaleza=na.acronimo and na.institucion=a.institucion)" +
								"INNER JOIN categoria_articulos ca ON(ca.codigo=a.categoria ) "+
                                "INNER JOIN trans_validas_x_cc_inven tv ON (tv.institucion= na.institucion) "+ 
                                "INNER JOIN subgrupo_inventario si ON (si.clase=tv.clase_inventario)  "+
								" WHERE si.codigo=a.subgrupo and a.naturaleza=na.acronimo and a.institucion=na.institucion " +
								"  AND a.estado='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' "+
								"  AND (tv.clase_inventario=si.clase) " +
								"  AND tv.tipos_trans_inventario= " +ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion, true)+
								"  AND tv.institucion="+codigoInstitucion+
								"  AND ca.codigo=a.categoria"+
								"  AND tv.centros_costo= "+codigoAlmacen+" " ; 				
					
									
									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoAxioma))
									{
										logger.info("===>Se realiza el filtro por el Código Axioma");
										consulta+=" AND a.codigo = "+criterioBusqueda+" ";
									}
									else if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										logger.info("===>Se realiza el filtro por el Código Interfaz");
										consulta+=" AND a.codigo_interfaz = '"+criterioBusqueda+"' ";
									}
									
									consulta += restriccionNaturaleza;
				}	
				//logger.info(consulta);
				
				if (controlEspecial==null || controlEspecial=="")
					logger.info("El control especial llegó con valor"+controlEspecial);
				else
				{
					if (controlEspecial.equals(ConstantesBD.acronimoSi))
					{
						consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoSi+"' ";
					}
					else
					{
						consulta+=" AND ca.control_especial='"+ConstantesBD.acronimoNo+"' ";
					}	
				}			
				consulta +="  ORDER BY a.descripcion";
				
				
				
				
				
			}
			
			ps = new PreparedStatementDecorator(con, consulta);
			
			rs  = new ResultSetDecorator(ps.executeQuery());
			
			//Variables locales para no tener problemas con pools de conexiones

			Vector resultados=new Vector(15,5);
			
			String parejasResultado="";

			boolean primerEncuentro;
			
			while (rs.next())
			{
				if(this.tipoConsulta!=4 && this.tipoConsulta!=7 && this.tipoConsulta!=8 && this.tipoConsulta!=9)
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
					//descripcion del articulo para solicitud de medicamentos, si tiene algun problema quitarlo,
					//lo pongo por separado para no modificar la consulta los casos de wilson y joan en el tag.
					if(this.tipoConsulta==5)
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
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
						parejasResultado=parejasResultado + rs.getString(8);
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
						parejasResultado=parejasResultado + rs.getString(9);
					}
					if(this.tipoConsulta==1 || this.tipoConsulta==2 || this.tipoConsulta==6 || this.tipoConsulta==3)
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
					
					if( this.tipoConsulta==6 )
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
					}
					
					if(this.tipoConsulta==1 || this.tipoConsulta==2)
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
					
					resultados.add(parejasResultado);
				}
				else
				{
					Vector vector = new Vector();
					vector.add(new Integer(rs.getInt("codigo")));
					vector.add(rs.getString("descripcion"));
					vector.add(rs.getString("naturaleza"));
					vector.add(rs.getString("codigoMinSalud"));
					vector.add(rs.getString("formaFarmaceutica"));
					vector.add(rs.getString("concentracion"));
					vector.add(rs.getString("unidadMedida"));
					vector.add(rs.getString("es_pos"));
					vector.add(rs.getString("codigoNaturaleza"));
					if(this.tipoConsulta==7 || this.tipoConsulta==8)
						vector.add(rs.getString("existenciaXAlmacen"));
					resultados.add(vector);
				}
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.warn(e.getMessage());
			throw new JspTagException("TagBusquedaArticulos: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagBusquedaArticulos: "+e.getMessage()+e.toString());
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
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
	public int getTipoConsulta() {
		return tipoConsulta;
	}
	/**
	 * @param tipoConsulta The tipoConsulta to set.
	 */
	public void setTipoConsulta(int tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	/**
	 * @return Retorna  numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud asigna numeroSolicitud.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the articulosSeleccionados.
	 */
	public HashMap getArticulosSeleccionadosMap() {
		return articulosSeleccionadosMap;
	}
	/**
	 * @param articulosSeleccionados The articulosSeleccionados to set.
	 */
	public void setArticulosSeleccionadosMap(HashMap articulosSeleccionadosMap) {
		this.articulosSeleccionadosMap = articulosSeleccionadosMap;
	}
	/**
	 * Set del mapa de codigosArticulos
	 * @param key
	 * @param value
	 */
	public void setArticulosSeleccionadosMap(String key, Object value) 
	{
		articulosSeleccionadosMap.put(key, value);
	}
	/**
	 * Get del mapa de despacho
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getArticulosSeleccionadosMap(String key) 
	{
		return articulosSeleccionadosMap.get(key);
	}
	
	/**
	 * @return Returns the numeroIngresos.
	 */
	public int getNumeroIngresos() {
		return numeroIngresos;
	}
	/**
	 * @param numeroIngresos The numeroIngresos to set.
	 */
	public void setNumeroIngresos(int numeroIngresos) {
		this.numeroIngresos = numeroIngresos;
	}
	/**
	 * @return Retorna esInsumo.
	 */
	public boolean getEsInsumo()
	{
		return esInsumo;
	}
	/**
	 * @param esInsumo Asigna esInsumo.
	 */
	public void setEsInsumo(boolean esInsumo)
	{
		this.esInsumo = esInsumo;
	}
	/**
	 * @return Returns the codigoInstitucion.
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * @param codigoInstitucion The codigoInstitucion to set.
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	/**
	 * @return Returns the codigoAlmacen.
	 */
	public int getCodigoAlmacen() {
		return codigoAlmacen;
	}
	/**
	 * @param codigoAlmacen The codigoAlmacen to set.
	 */
	public void setCodigoAlmacen(int codigoAlmacen) {
		this.codigoAlmacen = codigoAlmacen;
	}

	public String getValidarEstado() {
		return validarEstado;
	}

	public void setValidarEstado(String validarEstado) {
		this.validarEstado = validarEstado;
	}

	/**
	 * @return the filtrarXInventarios
	 */
	public boolean isFiltrarXInventarios() {
		return filtrarXInventarios;
	}
	
	/**
	 * @return the filtrarXInventarios
	 */
	public boolean getFiltrarXInventarios() {
		return filtrarXInventarios;
	}

	/**
	 * @param filtrarXInventarios the filtrarXInventarios to set
	 */
	public void setFiltrarXInventarios(boolean filtrarXInventarios) {
		this.filtrarXInventarios = filtrarXInventarios;
	}

	/**
	 * @return the codigoTransaccion
	 */
	public int getCodigoTransaccion() {
		return codigoTransaccion;
	}

	/**
	 * @param codigoTransaccion the codigoTransaccion to set
	 */
	public void setCodigoTransaccion(int codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	public String getControlEspecial() {
		return controlEspecial;
	}

	public void setControlEspecial(String controlEspecial) {
		this.controlEspecial = controlEspecial;
	}
}
