/*
 * @(#)TagAdminMedicamentos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
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

import util.Answer;
import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Tag que muestra la info de un artículo que ya ha sido administrado
 * 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios
 */
public class TagAdminMedicamentos extends BodyTagSupport
{
	/**
	 * Para manejar los logs
	 */
	private Logger logger = Logger.getLogger(TagAdminMedicamentos.class);
	
	/** 
	 * Separador de la cadena de resultados 
	 */
	private String is_separador = "@";
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * Código del artículo
	 */
	private int codigoArticulo;
	
	/**
	 * Número de la solicitud
	 */
	private int numeroSolicitud;

	/**
	 * La información de éste artículo se divide en dos partes,
	 * entonces se deja a elección la que se quiere mostrar
	 */
	private boolean part1;
	
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag() throws JspTagException
	{
			funcionalidad();
			return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspTagException
	{
		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Admin medicamentos : " + e.getMessage()+e.toString());
			}
		}
		clean();
		return EVAL_PAGE;
	}

	private void funcionalidad() throws JspTagException
	{
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
			
			if(this.part1)
			{
				consulta= "(SELECT " +
								"va.codigo AS codigo, " +
								"va.descripcion AS descripcion, " +
								"CASE WHEN va.concentracion IS NULL THEN '' ELSE va.concentracion||'' END AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(va.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(va.forma_farmaceutica) END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(va.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(va.unidad_medida) END  AS unidadMedida, " +
								"ds.dosis as dosis, " +
								"ds.frecuencia || ' ' || ds.tipo_frecuencia as frecuencia, " +
								"ds.via as via, " +
								"ds.observaciones AS observaciones, " +
								"'"+ConstantesBD.acronimoNo+"' AS adelanto_x_necesidad," +
								"'"+ConstantesBD.acronimoNo+"' AS nada_via_oral," +
								"'"+ConstantesBD.acronimoNo+"' AS usuario_rechazo, " +
								"CASE WHEN getDespacho(va.codigo,ds.numero_solicitud) IS NULL THEN '0' ELSE getDespacho(va.codigo,ds.numero_solicitud)||'' END AS despachoTotal, " +
								"CASE WHEN getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+") IS NULL THEN '0' ELSE getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+")||'' END AS totalAdministradoFarmacia, " +
								"CASE WHEN getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+") IS NULL THEN '0' ELSE getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+")||'' END AS totalAdministradoPaciente, " +
								"CASE WHEN getTotalAdmin(va.codigo,ds.numero_solicitud) IS NULL THEN '0' ELSE  getTotalAdmin(va.codigo,ds.numero_solicitud)||'' END  AS totalAdministrado " +
								"FROM view_articulos va " +
								" inner join detalle_solicitudes ds on (va.codigo=ds.articulo) " +
								"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
								"where "+ 
								"ds.numero_solicitud =" +this.numeroSolicitud+" "; 
				//CASE WHEN exep.servicio IS NULL THEN false ELSE true END AS existeExcepcion,
				if(this.codigoArticulo!=0)
				{	
					consulta+=" AND  va.codigo = "+this.codigoArticulo;
				}
				//XPLANNER id=14506
				/*else
				{
					consulta+=" AND va.naturaleza<>'"+ConstantesBD.codigoNaturalezaArticuloMaterialesInsumos+"' ";
				}*/
				
				consulta+=") ";
				
				consulta+="UNION ALL " +
								"(SELECT " +
								"distinct(deta.articulo) AS codigo, " +
								"va.descripcion AS descripcion, " +
								"CASE WHEN va.concentracion IS NULL THEN '' ELSE va.concentracion||'' END AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(va.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(va.forma_farmaceutica)||'' END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(va.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(va.unidad_medida) END  AS unidadMedida, " +
								"ds.dosis as dosis, " +
								"ds.frecuencia || ' ' || ds.tipo_frecuencia as frecuencia, " +
								"ds.via as via, " +
								"ds.observaciones AS observaciones, " +
								"deta.adelanto_x_necesidad," +
								"deta.nada_via_oral," +
								"deta.usuario_rechazo, " +
								"CASE WHEN getDespacho(va.codigo,ds.numero_solicitud) IS NULL THEN '0' ELSE getDespacho(va.codigo,ds.numero_solicitud)||'' END AS despachoTotal, " +
								"CASE WHEN getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+") IS NULL THEN '0' ELSE getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+")||'' END AS totalAdministradoFarmacia, " +
								"CASE WHEN getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+") IS NULL THEN '0' ELSE getTotalAdminFarmacia(va.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+")||'' END AS totalAdministradoPaciente, " +
								"CASE WHEN getTotalAdmin(va.codigo,ds.numero_solicitud) IS NULL THEN '0' ELSE getTotalAdmin(va.codigo,ds.numero_solicitud)||'' END AS totalAdministrado " +
								"FROM detalle_admin deta " +
								"INNER JOIN admin_medicamentos admin ON (admin.codigo=deta.administracion) " +
								"INNER JOIN despacho desp ON (desp.numero_solicitud=admin.numero_solicitud) " +
								"INNER JOIN detalle_despachos detd ON (detd.despacho=desp.orden AND detd.articulo=deta.articulo) " +
								"INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= admin.numero_solicitud) " +
								"INNER JOIN view_articulos va ON (va.codigo=deta.articulo)  " +
								"where admin.numero_solicitud=" +this.numeroSolicitud+" and detd.articulo<>detd.art_principal ";
				
				if(this.codigoArticulo!=0)
				{	
					consulta+=" AND  deta.articulo = "+this.codigoArticulo;
				}
				//XPLANNER id=14506
				/*else
				{
					consulta+=" AND va.naturaleza<>'"+ConstantesBD.codigoNaturalezaArticuloMaterialesInsumos+"' ";
				}*/
				
				consulta+=") ";
				
				
			}
			else
			{
				consulta= 	"SELECT " +
								"deta.articulo AS articulo, " +
								"deta.cantidad AS cantidad, " +
								"to_char(deta.fecha,'dd/mm/yyyy') AS fechaAdministracion, " +
								"substr(deta.hora,1,5) AS horaAdministracion, " +
								"deta.observaciones AS observaciones, " +
								"admi.usuario AS usuario, " +
								"to_char(admi.fecha_grabacion,'dd/mm/yyyy') AS fechaGrabacion, " +
								"substr(admi.hora_grabacion,1,5) AS horaGrabacion, " +
								"CASE WHEN deta.traido_usuario="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' ELSE 'No' END  AS traidoPaciente, " +
								"deta.adelanto_x_necesidad," +
								"deta.nada_via_oral," +
								"deta.usuario_rechazo " +
								"FROM " +
								"detalle_admin deta, " +
								"admin_medicamentos admi " +
								"where " +
								"admi.numero_solicitud=" +this.numeroSolicitud+" "+
								"AND " +
								"admi.codigo=deta.administracion " ;
				
				if(this.codigoArticulo!=0)
				{
					consulta+=" AND deta.articulo="+this.codigoArticulo;
				}
				consulta+="  ORDER BY deta.fecha DESC, deta.hora DESC ";
								
			}
			
			logger.info("\n\nconsulta Admin Med tag-->"+consulta+"\n\n");
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
						parejasResultado= parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(4);
				
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
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(10);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(11);
				
				if (primerEncuentro==true)
				{
					primerEncuentro=false;
				}
				else
				{
					parejasResultado=parejasResultado + is_separador;
				}
				parejasResultado=parejasResultado + rs.getString(12);
				
				if(this.part1)
				{
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(13);
					
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(14);
					
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(15);
					
					if (primerEncuentro==true)
					{
						primerEncuentro=false;
					}
					else
					{
						parejasResultado=parejasResultado + is_separador;
					}
					parejasResultado=parejasResultado + rs.getString(16);
					
					
					
				}
				resultados.add(parejasResultado);  
			}	
			
			pageContext.setAttribute("resultados" ,  resultados);
			
		}
		catch (java.sql.SQLException e)
		{
			logger.warn(e.getMessage());
			throw new JspTagException("TagAdminMedicamentos: "+e.getMessage()+e.toString());
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagAdminMedicamentos: "+e.getMessage()+e.toString());
		}
	}
	
	/**
	 * resetea los valores
	 */
	private void clean()
	{
		is_separador = "@";
		this.codigoArticulo=-1;
		this.numeroSolicitud=0;
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
	 * @return conexion usada por el tag
	 */
	public Connection getCon()
	{
		return con;
	}

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon(Connection connection) 
	{
		con = connection;
	}
	
	/**
	 * @return Returns the codigoArticulo.
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo The codigoArticulo to set.
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the part1.
	 */
	public boolean isPart1() {
		return part1;
	}
	/**
	 * @param part1 The part1 to set.
	 */
	public void setPart1(boolean part1) {
		this.part1 = part1;
	}
}
