/*
 * @(#)TagMuestraOpciones.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase es un Tag que permite separa un poco más la funcionalidad
 * de la presentación en una pagina JPS. Una de las operaciones más
 * frecuentes en los JSP's usados es la generacion dinamica de select's
 * donde el usuario ve la informacion y el select ve el codigo usado en
 * la Base de datos para almacenar este valor. La funcionalidad de este
 * tag es hacer todo este proceso, sin necesidad de conectar a la base
 * de datos o escribir consultas en SQL.
 *
 * @version 1.0, Sep 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraOpciones extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(TagMuestraOpciones.class);
	
	
	/**
	 * Primer campo de este tag.
	 */
	private String campo1="";

	/**
	 * Segundo campo de este tag.
	 */
	private String campo2="";

	/**
	 * Tercer campo de este tag.
	 */
	private String campo3="";

	/**
	 * Cuarto campo de este tag.
	 */
	private String campo4="";

	/**
	 * Quinto campo de este tag.
	 */
	private String campo5="";

	/**
	 * Sexto campo de este tag.
	 */
	private String campo6="";

	/**
	 * Séptimo campo de este tag.
	 */
	private String campo7="";

	/**
	 * Octavo campo de este tag.
	 */
	private String campo8="";
	
	/**
	 * Noveno campo de este tag.
	 */
	private String campo9="";

	/**
	 * Tabla que contiene la información que será mostrada por este tag.
	 */
	private String tabla;

	/**
	 * Formato en el que se desea mostrar la información extraída de la tabla.
	 * Los campos siempre empiezan con '$' y terminan con ';'
	 */
	private String formato;

	/**
	 * Atributo que dice si se debe mostrar codificado o no en html
	 */
	private boolean mostrarCodificado=true;

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;

	/**
	 * String con el nombre del campo sobre el que se quiere hacer la primera
	 * restricción
	 */
	private String rest1nombre;
	
	/**
	 * String con el valor del campo sobre el que se quiere hacer la primera
	 * restricción
	 */
	
	private String rest1valor;
	private String rest1diferente;

	/**
	 * String con el nombre del campo sobre el que se quiere hacer la segunda
	 * restricción
	 */
	private String rest2nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la segunda
	 * restricción
	 */
	private String rest2valor;

	/**
	 * Define si se quiere buscar por este criterio, o por la negación de éste,
	 * sobre la segunda restricción.
	 */
	private String rest2diferente;
	
	/**
	 * String con el nombre del campo sobre el que se quiere hacer la tercera
	 * restricción
	 */
	private String rest3nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la tercera
	 * restricción
	 */
	private String rest3valor;
	private String rest3diferente;

	/**
	 * String con el nombre del campo sobre el que se quiere hacer la cuarta
	 * restricción
	 */
	private String rest4nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la cuarta
	 * restricción
	 */
	private String rest4valor;
	private String rest4diferente;
	
	/**
	 * String con el nombre del campo sobre el que se quiere hacer la quinta
	 * restricción
	 */
	private String rest5nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la quinta
	 * restricción
	 */
	private String rest5valor;
	private String rest5diferente;
	
	/**
	 * String con el nombre del campo sobre el que se quiere hacer la sexta
	 * restricción
	 */
	private String rest6nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la sexta
	 * restricción
	 */
	private String rest6valor;
	private String rest6diferente;

	/**
	 * String con el nombre del campo sobre el que se quiere hacer la séptima
	 * restricción
	 */
	private String rest7nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la séptima
	 * restricción
	 */
	private String rest7valor;
	private String rest7diferente;
	
	/**
	 * String con el nombre del campo sobre el que se quiere hacer la octava
	 * restricción
	 */
	private String rest8nombre;

	/**
	 * String con el valor del campo sobre el que se quiere hacer la octava
	 * restricción
	 */
	private String rest8valor;
	private String rest8diferente;
	
	/**
	 * Variables para hacer la restriccion IN
	 */
	private String in1nombre;
	private String in1valor;
	private String in1diferente;

	private String orderby;
	private String ordertype;


	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP). Este metodo recibe a través del <code>&lt;jsp:param&gt;</code>
	 * los campos que se necesita sacar de la tabla, el nombre de
	 * la tabla y el formato en que se desea el resultado.
	 */
	public int doStartTag() throws JspException
	{
		PreparedStatementDecorator pstd = null;
		ResultSetDecorator rs = null;
		try
		{
			//TagDao tagDao;
			//ServletContext sc=pageContext.getServletContext();
			//String tipoBD=(String)sc.getInitParameter("TIPOBD");
			
			//DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			//tagDao = myFactory.getTagDao();
			
			//Answer a;

			String s, consulta="";
			boolean primero=true;

			/*A continuacion vamos a generar la consulta
			  dinamicamente. La estructura general es
			  select campo1, campo2 ... from tabla */

			consulta="SELECT  ";

			if (!campo1.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo1;
			}
			if (!campo2.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo2;
			}
			if (!campo3.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo3;
			}
			if (!campo4.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo4;
			}
			if (!campo5.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo5;
			}
			if (!campo6.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo6;
			}
			if (!campo7.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo7;
			}
			if (!campo8.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo8;
			}
			if (!campo9.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo9;
			}

			//Si en este punto primero es todavia
			//falso o tabla es vacio, se lanza una
			//excepcion avisando que la consulta
			//está mal hecha

			consulta = consulta + " FROM " + tabla;
			
			boolean primeraRestriccion=true;
			String comparacion="=";
			
			if (!(rest1nombre==null)&&!(rest1valor==null))
			{
				if (rest1diferente==null)
					comparacion="=";
				else if (rest1diferente!=null||rest1diferente.equals("true")||rest1diferente.equals("!="))
					comparacion="!=";
				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest1nombre + comparacion + "'" + rest1valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest1nombre + comparacion +  "'" + rest1valor + "'";
					primeraRestriccion=false;
				}
			}
			if (!(rest2nombre==null)&&!(rest2valor==null))
			{
				if (rest2diferente==null)
					comparacion="=";
				else if (rest2diferente!=null||rest2diferente.equals("true")||rest2diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest2nombre + comparacion + "'" + rest2valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest2nombre + comparacion + "'" + rest2valor + "'";
					primeraRestriccion=false;
				}
			}
			if (!(rest3nombre==null)&&!(rest3valor==null))
			{
				if (rest3diferente==null)
					comparacion="=";
				else if (rest3diferente!=null||rest3diferente.equals("true")||rest3diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest3nombre +  comparacion + "'" + rest3valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest3nombre + comparacion + "'" + rest3valor + "'";
					primeraRestriccion=false;
				}
			}

			if (!(rest4nombre==null)&&!(rest4valor==null))
			{
				if (rest4diferente==null)
					comparacion="=";
				else if (rest4diferente!=null||rest4diferente.equals("true")||rest4diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest4nombre +  comparacion + "'" + rest4valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest4nombre + comparacion + "'" + rest4valor + "'";
					primeraRestriccion=false;
				}
			}

			if (!(rest5nombre==null)&&!(rest5valor==null))
			{
				if (rest5diferente==null)
					comparacion="=";
				else if (rest5diferente!=null||rest5diferente.equals("true")||rest5diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest5nombre +  comparacion + "'" + rest5valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest5nombre + comparacion + "'" + rest5valor + "'";
					primeraRestriccion=false;
				}
			}
			
			if (!(rest6nombre==null)&&!(rest6valor==null))
			{
				if (rest6diferente==null)
					comparacion="=";
				else if (rest6diferente!=null||rest6diferente.equals("true")||rest6diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest6nombre +  comparacion + "'" + rest6valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest6nombre + comparacion + "'" + rest6valor + "'";
					primeraRestriccion=false;
				}
			}
			
			if (!(rest7nombre==null)&&!(rest7valor==null))
			{
				if (rest7diferente==null)
					comparacion="=";
				else if (rest7diferente!=null||rest7diferente.equals("true")||rest7diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest7nombre +  comparacion + "'" + rest7valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest7nombre + comparacion + "'" + rest7valor + "'";
					primeraRestriccion=false;
				}
			}
			
			if (!(rest8nombre==null)&&!(rest8valor==null))
			{
				if (rest8diferente==null)
					comparacion="=";
				else if (rest8diferente!=null||rest8diferente.equals("true")||rest8diferente.equals("!="))
					comparacion="!=";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + rest8nombre +  comparacion + "'" + rest8valor + "'";
				}
				else
				{
					consulta=consulta +" where " + rest8nombre + comparacion + "'" + rest8valor + "'";
					primeraRestriccion=false;
				}
			}
			
			if (!(in1nombre==null)&&!(in1valor==null))
			{
				if (in1diferente==null)
					comparacion=" IN ";
				else if (in1diferente!=null||in1diferente.equals("true")||in1diferente.equals("!="))
					comparacion=" NOT IN ";

				if (!primeraRestriccion)
				{
					consulta=consulta+" and " + in1nombre +  comparacion + "(" + in1valor + ")";
				}
				else
				{
					consulta=consulta +" where " + in1nombre + comparacion + "(" + in1valor + ")";
					primeraRestriccion=false;
				}
			}

			if(getOrderby()!=null && !getOrderby().equals("") 
				&& (getOrderby().equals(campo1) || getOrderby().equals(campo2) || getOrderby().equals(campo3) || getOrderby().equals(campo4) || getOrderby().equals(campo5) || getOrderby().equals(campo6) || getOrderby().equals(campo7) || getOrderby().equals(campo8) || getOrderby().equals(campo9) )){
				consulta+=" order by " + getOrderby();
				if(getOrdertype()!=null && (getOrdertype().equals("desc") || getOrdertype().equals("asc"))){
					consulta+=" "+getOrdertype();
				}
			}
			else
			{
				if (campo2 != null && !campo2.equals("")) {
					consulta += (" ORDER BY " + campo2);
				}
			}

			//a=tagDao.resultadoConsulta(con, consulta);
			
			pstd = new PreparedStatementDecorator(con, consulta);
			rs = new ResultSetDecorator(pstd.executeQuery());
			
			//rs  = a.getResultSet();
			//con = a.getConnection();
			while (rs.next()) {
				//Este metodo genera el pseudo formato
				s=generarFormato(rs);
				pageContext.getOut().print(s);
			}
			logger.info("CONSULTA------------------->>>>>>"+consulta);
		} catch (java.io.IOException e) {
			throw new JspTagException("TagMuestraOpciones: "+e.getMessage());
		} catch (java.sql.SQLException e) {
			throw new JspTagException("TagMuestraOpciones: "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		throw new JspTagException("TagMuestraOpciones: "+e.getMessage());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstd != null) {
					pstd.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("CERRANDO PREPAREDSTATEMENT: " + e);
			}
		}
		return SKIP_BODY;

	}

	/**
	 * Metodo necesario al extender la clase <code>TagSupport</code>
	 * en este caso no se usa para nada más
	 * @return la constante EVAL_PAGE
	 */
	public int doEndTag() 
	{
		return EVAL_PAGE;
	}

	/**
	 * Metodo "Get" que retorna el valor del primer campo
	 * @return el valor del primer campo
	 */
	public String getCampo1 ()
	{
		return this.campo1;
	}

	/**
	 * Metodo "Get" que retorna el valor del segundo campo
	 * @return el valor del segundo campo
	 */
	public String getCampo2 ()
	{
		return this.campo2;
	}

	/**
	 * Metodo "Get" que retorna el valor del tercer campo
	 * @return el valor del tercer campo
	 */
	public String getCampo3 ()
	{
		return this.campo3;
	}

	/**
	 * Metodo "Get" que retorna el valor del cuarto campo
	 * @return el valor del cuarto campo
	 */
	public String getCampo4 ()
	{
		return this.campo4;
	}

	/**
	 * Metodo "Get" que retorna el valor del quinto campo
	 * @return el valor del quinto campo
	 */
	public String getCampo5 ()
	{
		return this.campo5;
	}

	/**
	 * Metodo "Get" que retorna el nombre de la tabla consultada
	 * @return el nombre de la tabla a consultar
	 */
	public String getTabla ()
	{
		return this.tabla;
	}

	/**
	 * Metodo "Get" que retorna el formato en que saldra
	 * el resultado de la consulta que se genera automaticamente
	 * @return el formato en que saldran los resultados
	 */
	public String getFormato ()
	{
		return this.formato;
	}

	/**
	 * Metodo "Set" que asigna el nombre del primer campo
	 * que se buscara en la consulta
	 * @param campo1 Valor con que quedara el primer campo
	 * que se buscara en la consulta
	 */
	public void setCampo1 (String campo1)
	{
		this.campo1=campo1;
	}

	/**
	 * Metodo "Set" que asigna el nombre del segundo campo
	 * que se buscara en la consulta
	 * @param campo2 Valor con que quedara el segundo campo
	 * que se buscara en la consulta
	 */
	public void setCampo2 (String campo2)
	{
		this.campo2=campo2;
	}

	/**
	 * Metodo "Set" que asigna el nombre del tercer campo
	 * que se buscara en la consulta
	 * @param campo3 Valor con que quedara el tercer campo
	 * que se buscara en la consulta
	 */
	public void setCampo3 (String campo3)
	{
		this.campo3=campo3;
	}

	/**
	 * Metodo "Set" que asigna el nombre del cuarto campo
	 * que se buscara en la consulta
	 * @param campo4 Valor con que quedara el cuarto campo
	 * que se buscara en la consulta
	 */
	public void setCampo4 (String campo4)
	{
		this.campo4=campo4;
	}

	/**
	 * Metodo "Set" que asigna el nombre del quinto campo
	 * que se buscara en la consulta
	 * @param campo5 Valor con que quedara el quinto campo
	 * que se buscara en la consulta
	 */
	public void setCampo5 (String campo5)
	{
		this.campo5=campo5;
	}

	/**
	 * Metodo "Set" que define el nombre de la tabla usada
	 * en la consulta
	 * @param tabla nombre de la tabla que se va a consultar
	 */
	public void setTabla (String tabla)
	{
		this.tabla=tabla;
	}

	/**
	 * Metodo "Set" que define el formato usado en la
	 * la respuesta
	 * @param formato el formato en que debe presentarse la
	 * información consultada de la tabla
	 */
	public void setFormato (String formato)
	{
		this.formato=formato;
	}

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
		this.con=con;
	}
	
	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Metodo que recibe un String con el formato y reemplaza
	 * todas las ocurrencias de variables ($nombreVar;) por
	 * sus valores reales, obtenidos de la consulta.
	 * @param rs <code>ResultSet</code> del que se sacan
	 * los valores correspondientes a los campos que definió
	 * el usuario
	 * @return Un <code>String</code> con una fila del resultado
	 * de la consulta, en el formato especificado
	 */
	private String generarFormato (ResultSetDecorator rs) throws SQLException
	{

	/*Primero definimos una variable temporal
	para hacer alli las operaciones y no dañar
	el formato original*/

	String temporal = formato;

	if (this.mostrarCodificado)
	{
		if (!campo1.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo1=verificacionCampo(campo1);
			
			temporal=reemplazar(temporal, "$campo1;",  rs.getString(campo1)==null?"":rs.getString(campo1) );
		}
		if (!campo2.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo2=verificacionCampo(campo2);
			
			temporal=reemplazar(temporal, "$campo2;", rs.getString(campo2)==null?"":rs.getString(campo2));
		}
		if (!campo3.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo3=verificacionCampo(campo3);
			
			temporal=reemplazar(temporal, "$campo3;", rs.getString(campo3)==null?"":rs.getString(campo3));
		}
		if (!campo4.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo4=verificacionCampo(campo4);
			
			temporal=reemplazar(temporal, "$campo4;", rs.getString(campo4)==null?"":rs.getString(campo4));
		}
		if (!campo5.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo5=verificacionCampo(campo5);
			
			temporal=reemplazar(temporal, "$campo5;", rs.getString(campo5)==null?"":rs.getString(campo5));
		}
		if (!campo6.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo6=verificacionCampo(campo6);
			
			temporal=reemplazar(temporal, "$campo6;", rs.getString(campo6)==null?"":rs.getString(campo6));
		}
		if (!campo7.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo7=verificacionCampo(campo7);
			temporal=reemplazar(temporal, "$campo7;", rs.getString(campo7)==null?"":rs.getString(campo7));
		}
		if (!campo8.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo8=verificacionCampo(campo8);
			temporal=reemplazar(temporal, "$campo8;", rs.getString(campo8)==null?"":rs.getString(campo8));
		}
		if (!campo9.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo9=verificacionCampo(campo9);
			temporal=reemplazar(temporal, "$campo9;", rs.getString(campo9)==null?"":rs.getString(campo9));
		}
	}
	else
	{
		if (!campo1.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo1=verificacionCampo(campo1);
			temporal=reemplazar(temporal, "$campo1;",  rs.getString(campo1)==null?"":rs.getString(campo1) );
		}
		if (!campo2.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo2=verificacionCampo(campo2);
			temporal=reemplazar(temporal, "$campo2;", rs.getString(campo2)==null?"":rs.getString(campo2));
		}
		if (!campo3.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo3=verificacionCampo(campo3);
			temporal=reemplazar(temporal, "$campo3;", rs.getString(campo3)==null?"":rs.getString(campo3));
		}
		if (!campo4.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo4=verificacionCampo(campo4);
			temporal=reemplazar(temporal, "$campo4;", rs.getString(campo4)==null?"":rs.getString(campo4));
		}
		if (!campo5.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo5=verificacionCampo(campo5);
			temporal=reemplazar(temporal, "$campo5;", rs.getString(campo5)==null?"":rs.getString(campo5));
		}
		if (!campo6.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo6=verificacionCampo(campo6);
			temporal=reemplazar(temporal, "$campo6;", rs.getString(campo6)==null?"":rs.getString(campo6));
		}
		if (!campo7.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo7=verificacionCampo(campo7);
			temporal=reemplazar(temporal, "$campo7;", rs.getString(campo7)==null?"":rs.getString(campo7));
		}
		if (!campo8.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo8=verificacionCampo(campo8);
			temporal=reemplazar(temporal, "$campo8;", rs.getString(campo8)==null?"":rs.getString(campo8));
		}
		if (!campo9.equals(""))
		{
			//se verifica si campo tiene indice de tabla
			campo9=verificacionCampo(campo9);
			temporal=reemplazar(temporal, "$campo9;", rs.getString(campo9)==null?"":rs.getString(campo9));
		}


	}
	return temporal;
	}

	/**
	 * Método que verifica si el campo tiene indice de tabla
	 * para quitárselo y poder obtener el campo del resultSet sin error
	 * Tambien verifica si hay alias para dejar solo el alias
	 * @param campo
	 */
	private String verificacionCampo(String campo) 
	{
		String nuevoCampo="";
		//Se verifica si hay alias
		boolean contieneAlias = false;
		String[] vector = campo.toLowerCase().split(" as ");
		if(vector.length==2)
			contieneAlias = true;
		
		if(contieneAlias)
		{
			campo = vector[1].trim();
		}
		else
		{
			//verifica punto******************************
			boolean contienePunto = false;
			for(int i=0;i<campo.length();i++)
			{
				if(contienePunto)
					nuevoCampo += campo.charAt(i);
				
				if(campo.charAt(i)=='.')
					contienePunto = true;
				
			}
			
			if(contienePunto)
				campo = nuevoCampo;
		}
		
		
		return campo;
	}

	/**
	 * Este método sirve para reemplazar dentro de una frase
	 * todas las ocurrencias de un texto determinado por otro.
	 * Aunque en String hay un replaceAll y replace, estos no
	 * funcionan en modo tradicional, sino con expresiones
	 * regulares.
	 * @param fraseCompleta La frase en la que se desea reemplazar
	 * todas las ocurrencias de textoAReemplazar por textoReemplazo
	 * @param textoAReemplazar El texto que se desea reemplazar en
	 * la frase original
	 * @param textoReemplazo El texto por el cual se va a reemplazar
	 * textoAReemplazar en la frase original
	 * @return <code>String</code> con la frase original, donde
	 * todas las ocurrencias de textoAReemplazar han sido cambiadas
	 * por textoReemplazo
	 */
	private String reemplazar (String fraseCompleta, String textoAReemplazar, String textoReemplazo)
	{
	int indiceEncontrado=0;
	boolean finalizarBusqueda=false;
	String temporal="";
	while (!finalizarBusqueda)
	{
		indiceEncontrado = fraseCompleta.indexOf(textoAReemplazar);

		if (indiceEncontrado!=-1)
		{
		temporal=temporal + fraseCompleta.substring(0 ,indiceEncontrado);
		temporal=temporal + textoReemplazo;
		temporal=temporal + fraseCompleta.substring( (indiceEncontrado + textoAReemplazar.length() ) );
		fraseCompleta=temporal;
		temporal="";
		}
		else
		{
		finalizarBusqueda=true;
		}
	}
	return fraseCompleta;
	}

	/**
	 * Retorna el nombre de la primera restricción.
	 * @return String con el nombre de la primera restricción
	 */
	public String getRest1nombre() {
		return rest1nombre;
	}

	/**
	 * Retorna el valor de la primera restricción.
	 * @return String con el valor de la primera restricción
	 */
	public String getRest1valor() {
		return rest1valor;
	}

	/**
	 * Retorna el nombre de la segunda restricción.
	 * @return String con el nombre de la segunda restricción
	 */
	public String getRest2nombre() {
		return rest2nombre;
	}

	/**
	 * Retorna el valor de la segunda restricción.
	 * @return String con el valor de la segunda restricción
	 */
	public String getRest2valor() {
		return rest2valor;
	}

	/**
	 * Retorna el nombre de la tercera restricción.
	 * @return String con el nombre de la tercera restricción
	 */
	public String getRest3nombre() {
		return rest3nombre;
	}

	/**
	 * Retorna el valor de la tercera restricción.
	 * @return String con el valor de la tercera restricción
	 */
	public String getRest3valor() {
		return rest3valor;
	}

	/**
	 * Establece el nombre de la primera restricción.
	 * @param rest1nombre El nombre de la primera restricción a establecer
	 */
	public void setRest1nombre(String rest1nombre) {
		this.rest1nombre = rest1nombre;
	}

	/**
	 * Establece el valor de la primera restricción.
	 * @param rest1valor El valor de la primera restricción a establecer
	 */
	public void setRest1valor(String rest1valor) {
		this.rest1valor = rest1valor;
	}

	/**
	 * Establece el nombre de la segunda restricción.
	 * @param rest2nombre El nombre de la segunda restricción a establecer
	 */
	public void setRest2nombre(String rest2nombre) {
		this.rest2nombre = rest2nombre;
	}

	/**
	 * Establece el valor de la segunda restricción.
	 * @param rest2valor El valor de la segunda restricción a establecer
	 */
	public void setRest2valor(String rest2valor) {
		this.rest2valor = rest2valor;
	}

	/**
	 * Establece el nombre de la tercera restricción.
	 * @param rest3nombre El nombre de la tercera restricción a establecer
	 */
	public void setRest3nombre(String rest3nombre) {
		this.rest3nombre = rest3nombre;
	}

	/**
	 * Establece el valor de la tercera restricción.
	 * @param rest3valor El valor de la tercera restricción a establecer
	 */
	public void setRest3valor(String rest3valor) {
		this.rest3valor = rest3valor;
	}

	/**
	 * Returns the rest1diferente.
	 * @return String
	 */
	public String getRest1diferente() {
		return rest1diferente;
	}

	/**
	 * Returns the rest2diferente.
	 * @return String
	 */
	public String getRest2diferente() {
		return rest2diferente;
	}

	/**
	 * Returns the rest3diferente.
	 * @return String
	 */
	public String getRest3diferente() {
		return rest3diferente;
	}

	/**
	 * Sets the rest1diferente.
	 * @param rest1diferente The rest1diferente to set
	 */
	public void setRest1diferente(String rest1diferente) {
		this.rest1diferente = rest1diferente;
	}

	/**
	 * Establece la rest2diferente.
	 * @param rest2diferente The rest2diferente to set
	 */
	public void setRest2diferente(String rest2diferente) {
		this.rest2diferente = rest2diferente;
	}

	/**
	 * Sets the rest3diferente.
	 * @param rest3diferente The rest3diferente to set
	 */
	public void setRest3diferente(String rest3diferente) {
		this.rest3diferente = rest3diferente;
	}

	/**
	 * Returns the campo6.
	 * @return String
	 */
	public String getCampo6() {
		return campo6;
	}

	/**
	 * Sets the campo6.
	 * @param campo6 The campo6 to set
	 */
	public void setCampo6(String campo6) {
		this.campo6 = campo6;
	}

	/**
	 * Returns the campo7.
	 * @return String
	 */
	public String getCampo7() {
		return campo7;
	}

	/**
	 * Returns the campo8.
	 * @return String
	 */
	public String getCampo8() {
		return campo8;
	}
	
	

	/**
	 * @return the campo9
	 */
	public String getCampo9() {
		return campo9;
	}

	/**
	 * @param campo9 the campo9 to set
	 */
	public void setCampo9(String campo9) {
		this.campo9 = campo9;
	}

	/**
	 * Sets the campo7.
	 * @param campo7 The campo7 to set
	 */
	public void setCampo7(String campo7) {
		this.campo7 = campo7;
	}

	/**
	 * Sets the campo8.
	 * @param campo8 The campo8 to set
	 */
	public void setCampo8(String campo8) {
		this.campo8 = campo8;
	}

	/**
	 * Returns the rest4diferente.
	 * @return String
	 */
	public String getRest4diferente() {
		return rest4diferente;
	}

	/**
	 * Returns the rest4nombre.
	 * @return String
	 */
	public String getRest4nombre() {
		return rest4nombre;
	}

	/**
	 * Returns the rest4valor.
	 * @return String
	 */
	public String getRest4valor() {
		return rest4valor;
	}

	/**
	 * Sets the rest4diferente.
	 * @param rest4diferente The rest4diferente to set
	 */
	public void setRest4diferente(String rest4diferente) {
		this.rest4diferente = rest4diferente;
	}

	/**
	 * Sets the rest4nombre.
	 * @param rest4nombre The rest4nombre to set
	 */
	public void setRest4nombre(String rest4nombre) {
		this.rest4nombre = rest4nombre;
	}

	/**
	 * Sets the rest4valor.
	 * @param rest4valor The rest4valor to set
	 */
	public void setRest4valor(String rest4valor) {
		this.rest4valor = rest4valor;
	}
	
	/**
	 * @return Returns the rest5diferente.
	 */
	public String getRest5diferente() {
		return rest5diferente;
	}
	/**
	 * @param rest5diferente The rest5diferente to set.
	 */
	public void setRest5diferente(String rest5diferente) {
		this.rest5diferente = rest5diferente;
	}
	/**
	 * @return Returns the rest5nombre.
	 */
	public String getRest5nombre() {
		return rest5nombre;
	}
	/**
	 * @param rest5nombre The rest5nombre to set.
	 */
	public void setRest5nombre(String rest5nombre) {
		this.rest5nombre = rest5nombre;
	}
	/**
	 * @return Returns the rest5valor.
	 */
	public String getRest5valor() {
		return rest5valor;
	}
	/**
	 * @param rest5valor The rest5valor to set.
	 */
	public void setRest5valor(String rest5valor) {
		this.rest5valor = rest5valor;
	}
	
	
	/**
	 * @return Returns the rest6diferente.
	 */
	public String getRest6diferente() {
		return rest6diferente;
	}
	/**
	 * @param rest6diferente The rest6diferente to set.
	 */
	public void setRest6diferente(String rest6diferente) {
		this.rest6diferente = rest6diferente;
	}
	/**
	 * @return Returns the rest6nombre.
	 */
	public String getRest6nombre() {
		return rest6nombre;
	}
	/**
	 * @param rest6nombre The rest6nombre to set.
	 */
	public void setRest6nombre(String rest6nombre) {
		this.rest6nombre = rest6nombre;
	}
	/**
	 * @return Returns the rest6valor.
	 */
	public String getRest6valor() {
		return rest6valor;
	}
	/**
	 * @param rest6valor The rest6valor to set.
	 */
	public void setRest6valor(String rest6valor) {
		this.rest6valor = rest6valor;
	}
	/**
	 * @return Returns the rest7diferente.
	 */
	public String getRest7diferente() {
		return rest7diferente;
	}
	/**
	 * @param rest7diferente The rest7diferente to set.
	 */
	public void setRest7diferente(String rest7diferente) {
		this.rest7diferente = rest7diferente;
	}
	/**
	 * @return Returns the rest7nombre.
	 */
	public String getRest7nombre() {
		return rest7nombre;
	}
	/**
	 * @param rest7nombre The rest7nombre to set.
	 */
	public void setRest7nombre(String rest7nombre) {
		this.rest7nombre = rest7nombre;
	}
	/**
	 * @return Returns the rest7valor.
	 */
	public String getRest7valor() {
		return rest7valor;
	}
	/**
	 * @param rest7valor The rest7valor to set.
	 */
	public void setRest7valor(String rest7valor) {
		this.rest7valor = rest7valor;
	}
	
	/**
	 * @return Returns the rest8diferente.
	 */
	public String getRest8diferente() {
		return rest8diferente;
	}
	/**
	 * @param rest8diferente The rest8diferente to set.
	 */
	public void setRest8diferente(String rest8diferente) {
		this.rest8diferente = rest8diferente;
	}
	/**
	 * @return Returns the rest8nombre.
	 */
	public String getRest8nombre() {
		return rest8nombre;
	}
	/**
	 * @param rest8nombre The rest8nombre to set.
	 */
	public void setRest8nombre(String rest8nombre) {
		this.rest8nombre = rest8nombre;
	}
	/**
	 * @return Returns the rest8valor.
	 */
	public String getRest8valor() {
		return rest8valor;
	}
	/**
	 * @param rest8valor The rest8valor to set.
	 */
	public void setRest8valor(String rest8valor) {
		this.rest8valor = rest8valor;
	}
	/**
	 * @return
	 */
	public boolean isMostrarCodificado() {
		return mostrarCodificado;
	}

	/**
	 * @param b
	 */
	public void setMostrarCodificado(boolean b) {
		mostrarCodificado = b;
	}

	/**
	 * @return
	 */
	public String getOrderby() {
		return orderby;
	}

	/**
	 * @return
	 */
	public String getOrdertype() {
		return ordertype;
	}

	/**
	 * @param string
	 */
	public void setOrderby(String string) {
		orderby = string;
	}

	/**
	 * @param string
	 */
	public void setOrdertype(String string) {
		ordertype = string;
	}

	public String getIn1diferente() {
		return in1diferente;
	}

	public void setIn1diferente(String in1diferente) {
		this.in1diferente = in1diferente;
	}

	public String getIn1nombre() {
		return in1nombre;
	}

	public void setIn1nombre(String in1nombre) {
		this.in1nombre = in1nombre;
	}

	public String getIn1valor() {
		return in1valor;
	}

	public void setIn1valor(String in1valor) {
		this.in1valor = in1valor;
	}

}