/*
 * @(#)TagMuestraOpcionesStruts.java
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
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.cache.CacheFacade;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase es un Tag que permite separa un poco más la funcionalidad
 * de la presentación en una pagina JPS. Una de las operaciones más
 * frecuentes en los JSP's usados es la generacion dinamica de select's
 * donde el usuario ve la informacion y el select ve el codigo usado en
 * la Base de datos para almacenar este valor. La funcionalidad de este
 * tag es hacer todo este proceso, sin necesidad de conectar a la base
 * de datos o escribir consultas en SQL. La principal diferencia con el
 * TagMuestraOpciones es que trabaja con variables TEI que permiten definir
 * variables que son manejadas como tales en el JSP
 *
 * @version 1.0, Mar 7, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

@SuppressWarnings("serial")
public class TagMuestraOpcionesStruts extends BodyTagSupport
{
	private Logger logger = Logger.getLogger(TagMuestraOpcionesStruts.class);

	/** Separador de la cadena de resultados */
	private String is_separador = "-";


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
	 * Decimo campo de este tag.
	 */
	private String campo10="";

	/**
	 * Tabla que contiene la información que será mostrada por este tag.
	 */
	private String tabla;

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
     * String para cuando se desea colocar una restriccion de tipo is null de un campo
     */
    private String rest1isnull;

	private String orderby;
	private String ordertype;

	private String restOr1nombre;
	private String restOr1valor;
	private String restOr1diferente;

	private String is_tabla2;
	private String is_aliastabla;
	private String is_aliastabla2;
	private String is_rest1valorcampo;
	private String is_rest2valorcampo;
	private String is_rest3valorcampo;
	private String is_rest4valorcampo;
	private String is_rest5valorcampo;
	private String is_restOr1valorcampo;
	
	private String in1nombre;
	private String in1valor;
	private String in2nombre;
	private String in2valor;

	private String menorigualquenombre;
	private String menorigualquevalor;
	private String menorigualquevalorstr;
	
	private String mayorigualquenombre;
	private String mayorigualquevalor;
	private String mayorigualquevalorstr;
	
	private String groupby;
	
	private boolean usarVector;
	
	private String limit;
	
	private String like1nombre;
	private String like1valor;
	
	private String notIn1nombre;
	private String notIn1valor;
	private String distinct;
	
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag() throws JspTagException {
			funcionalidad();
			return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Opciones Struts : " + e.getMessage()+e.toString());
			}
		}

		clean();
		return EVAL_PAGE;
	}

	private void clean()
	{
		is_separador = "-";

		this.campo1 = "";
		this.campo2 = "";
		this.campo3 = "";
		this.campo4 = "";
		this.campo5 = "";
		this.campo6 = "";
		this.campo7 = "";
		this.campo8 = "";
		this.campo9 ="";
		this.campo10 ="";

		this.tabla="";
		//Las restricciones como no son obligatorias y hay que revisarlas todas
		//se inicializan en null, de tal manera que si no han sido seleccionadas quedan
		//en este valor y se logra una mayor eficiencia que  utilizando métodos para
		//comparar con cadenas vacias

		this.rest1valor=null;
		this.rest1nombre=null;
		this.rest1diferente=null;

		this.rest2valor=null;
		this.rest2nombre=null;
		this.rest2diferente=null;

		this.rest3valor=null;
		this.rest3nombre=null;
		this.rest3diferente=null;
        
        this.rest4valor=null;
        this.rest4nombre=null;
        this.rest4diferente=null;
        
        this.rest5valor=null;
        this.rest5nombre=null;
        this.rest5diferente=null;
        
        this.menorigualquenombre=null;
        this.menorigualquevalor=null;
        this.menorigualquevalorstr=null;
        
        
        this.mayorigualquenombre=null;
        this.mayorigualquevalor=null;
        this.mayorigualquevalorstr=null;
        
        this.rest1isnull=null;

		this.restOr1valor = null;
		this.restOr1diferente = null;

		setOrderby(null);
		setOrdertype(null);

		is_tabla2		= "";
		is_aliastabla	= "";
		is_aliastabla2	= "";

		is_rest1valorcampo		= null;
		is_rest2valorcampo		= null;
		is_rest3valorcampo		= null;
		is_rest4valorcampo		= null;
		is_restOr1valorcampo	= null;
		
		in1nombre=null;
		in1valor=null;
		in2nombre=null;
		in2valor=null;
		
		usarVector=false;
		this.limit=null;
		
		this.like1nombre=null;
		this.like1valor=null;
		
		this.notIn1nombre ="";
		this.notIn1valor = "";
		this.distinct="";
	}

	private String getOracleBoolean(int constanteTipoBD, String restValor){

		if(constanteTipoBD==DaoFactory.ORACLE && restValor != null)
		{
			
			if(restValor.equalsIgnoreCase("true")||restValor.equals("t")||restValor.equalsIgnoreCase("'true'")||restValor.equals("'t'")){
				
				return "1";
			}
			else if(restValor.equalsIgnoreCase("false")||restValor.equals("f")||restValor.equalsIgnoreCase("'false'")||restValor.equals("'f'")){
			
				return "0";
			} 
		}
		return restValor;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void funcionalidad() throws JspTagException {

		PreparedStatementDecorator pstd = null;
		ResultSetDecorator rs = null;
		String consulta="";
		try {

			//TagDao tagDao;
			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			
			int constanteTipoBD = -1;
			constanteTipoBD = DaoFactory.getConstanteTipoBD(tipoBD);

			//DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			//tagDao = myFactory.getTagDao();
			
			//Answer a;
			//ResultSetDecorator rs = null;
			
			boolean primero=true;

			/*A continuacion vamos a generar la consulta
			  dinamicamente. La estructura general es
			  select campo1, campo2 ... from tabla */

			consulta="SELECT  ";
			
			if (!UtilidadTexto.isEmpty(this.distinct)&&!this.distinct.equals(ConstantesBD.acronimoNo))
			{
				logger.info("ENTRE A HACER EL DISTINCT");
				consulta+=" DISTINCT ";
				
			}

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
			
			if(!campo9.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo9;
			}
			
			if(!campo10.equals(""))
			{
				if (!primero)
				consulta=consulta + ", ";
				else
				primero=false;
				consulta= consulta + campo10;
			}

			//Si en este punto primero es todavia
			//falso o tabla es vacio, se lanza una
			//excepcion avisando que la consulta
			//está mal hecha

			consulta = consulta + " FROM " + tabla;

			if(is_aliastabla != null && !is_aliastabla.equals("") )
				consulta += " " + is_aliastabla;

			if(is_tabla2 != null && !is_tabla2.equals("") )
				consulta += "," + is_tabla2;

			if(is_aliastabla2 != null && !is_aliastabla2.equals("") )
				consulta += " " + is_aliastabla2;

			boolean primeraRestriccion=true;
			String comparacion="=";

			if(rest1nombre != null && (rest1valor != null || is_rest1valorcampo != null) )
			{
			    boolean isLike=false;
				if (rest1diferente==null)
					comparacion="=";
				else if (rest1diferente!=null&&(rest1diferente.equals("true")||rest1diferente.equals("!=")))
					comparacion="!=";
				if (rest1diferente!=null&&(rest1diferente.equals("like")))
				{    
				    comparacion=" like ";
				    isLike=true;
				}    
				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += rest1nombre + comparacion;
				
				//Validacion de campos tipo boolean
				rest1valor = getOracleBoolean(constanteTipoBD, rest1valor);
				if (isLike)
				    consulta += (rest1valor != null  && !rest1valor.equals("null")) ? "" + rest1valor + "" : is_rest1valorcampo;
				else    
				    consulta += (rest1valor != null && !rest1valor.equals("null")) ? "'" + rest1valor + "'" : is_rest1valorcampo;
				
				primeraRestriccion=false;
			}
			if(rest2nombre != null && (rest2valor != null || is_rest2valorcampo != null) )
			{
				if (rest2diferente==null)
					comparacion="=";
				else if (rest2diferente!=null||rest2diferente.equals("true")||rest2diferente.equals("!="))
					comparacion="!=";

				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += rest2nombre + comparacion;
				
				
				//Validacion de campos tipo boolean
				rest2valor = getOracleBoolean(constanteTipoBD, rest2valor);
				is_rest2valorcampo = getOracleBoolean(constanteTipoBD, is_rest2valorcampo);
				logger.info("rest2valor--->"+rest2valor+"\nis_rest2valorcampo--->"+is_rest2valorcampo);				
				consulta += (rest2valor != null && !rest2valor.equals("null")) ? "'" + rest2valor + "'" : is_rest2valorcampo;
				logger.info("consulta--->"+consulta);
				primeraRestriccion=false;
			}
			if(rest3nombre != null && (rest3valor != null || is_rest3valorcampo != null) )
			{
				if (rest3diferente==null)
					comparacion="=";
				else if (rest3diferente!=null||rest3diferente.equals("true")||rest3diferente.equals("!="))
					comparacion="!=";

				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += rest3nombre + comparacion;
				
				//Validacion de campos tipo boolean
				rest3valor = getOracleBoolean(constanteTipoBD, rest3valor);
				is_rest3valorcampo = getOracleBoolean(constanteTipoBD, is_rest3valorcampo);
				
				consulta += (rest3valor != null && !rest3valor.equals("null")) ? "'" + rest3valor + "'" : is_rest3valorcampo;
				
				primeraRestriccion=false;
			}
			if(rest4nombre != null && (rest4valor != null || is_rest4valorcampo != null) )
			{
				if (rest4diferente==null)
					comparacion="=";
				else if (rest4diferente!=null||rest4diferente.equals("true")||rest4diferente.equals("!="))
					comparacion="!=";

				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += rest4nombre + comparacion;
				
				//Validacion de campos tipo boolean
				rest4valor = getOracleBoolean(constanteTipoBD, rest4valor);
				is_rest4valorcampo = getOracleBoolean(constanteTipoBD, is_rest4valorcampo);
				
				consulta += (rest4valor != null  && !rest4valor.equals("null")) ? "'" + rest4valor + "'" : is_rest4valorcampo;
				
				primeraRestriccion=false;
			}
			if(rest5nombre != null && (rest5valor != null || is_rest5valorcampo != null) )
			{
				if (rest5diferente==null)
					comparacion="=";
				else if (rest5diferente!=null||rest5diferente.equals("true")||rest5diferente.equals("!="))
					comparacion="!=";

				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += rest5nombre + comparacion;
				
				//Validacion de campos tipo boolean
				rest5valor = getOracleBoolean(constanteTipoBD, rest5valor);
				is_rest5valorcampo = getOracleBoolean(constanteTipoBD, is_rest5valorcampo);
				
				consulta += (rest5valor != null && !rest5valor.equals("null")) ? "'" + rest5valor + "'" : is_rest5valorcampo;

				primeraRestriccion=false;
			}
			
            if(rest1isnull != null)
            {
                consulta += !primeraRestriccion ? " and " : " where ";
                consulta += rest4nombre + " is null";
            }
			
			if(menorigualquenombre!=null && (menorigualquevalor!=null||menorigualquevalorstr!=null))
			{
				consulta+=!primeraRestriccion ? " and " : " where ";
				consulta+=menorigualquenombre+" <= ";
				consulta+=(menorigualquevalor!=null)?menorigualquevalor:"'" + menorigualquevalorstr+ "'";
				primeraRestriccion=false;
			}
			if(mayorigualquenombre!=null && (mayorigualquevalor!=null||mayorigualquevalorstr!=null))
			{
				consulta+=!primeraRestriccion ? " and " : " where ";
				consulta+=mayorigualquenombre+" >= ";
				consulta+=(mayorigualquevalor!=null)?mayorigualquevalor:"'" + mayorigualquevalorstr+ "'";
				primeraRestriccion=false;
			}
			if(restOr1nombre != null && (restOr1valor != null || is_restOr1valorcampo != null) )
			{
				if( restOr1diferente==null )
					comparacion="=";
				else
				if( restOr1diferente!=null || restOr1diferente.equals("true") || restOr1diferente.equals("!=") )
					comparacion="!=";

				consulta += !primeraRestriccion ? " or " : " where ";
				consulta += restOr1nombre + comparacion;
				consulta += (restOr1valor != null) ? "'" + restOr1valor + "'" : is_restOr1valorcampo;

				primeraRestriccion=false;
			}
			
			if(in1nombre!=null && in1valor!=null)
			{
				if(in1valor.equals(""))
				{
					in1valor="-1";
				}
				consulta+=!primeraRestriccion ? " and " : " where ";
				consulta+=in1nombre+" IN("+in1valor+")";
				primeraRestriccion=false;
			}

			if(in2nombre!=null && in2valor!=null)
			{
				if(in2valor.equals(""))
				{
					in2valor="-1";
				}
				consulta+=!primeraRestriccion ? " and " : " where ";
				consulta+=in2nombre+" IN("+in2valor+")";
				primeraRestriccion=false;
			}
			
			//if(rest2nombre != null && (rest2valor != null || is_rest2valorcampo != null) )
			if(like1nombre != null && like1valor != null)
			{

				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += "upper("+ like1nombre+") like upper('%"+like1valor+"%') ";
				primeraRestriccion=false;
			}
			
			if(!UtilidadTexto.isEmpty(this.notIn1nombre)&&!UtilidadTexto.isEmpty(this.notIn1valor))
			{
				consulta += !primeraRestriccion ? " and " : " where ";
				consulta += notIn1nombre+" not in ("+notIn1valor+") ";
			}
			
			if(constanteTipoBD==DaoFactory.ORACLE&&getLimit()!=null && !getLimit().equals(""))
			{
				consulta += !primeraRestriccion ? " and " : " where ";
				consulta+=" rownum = " + getLimit();
			}
		
			
			if(groupby!=null)
			{
				consulta+=" group by "+groupby;
			}
			
			if(getOrderby()!=null && !getOrderby().equals("")){
				consulta+=" order by " + getOrderby();
				if(getOrdertype()!=null && (getOrdertype().equals("desc") || getOrdertype().equals("asc"))){
					consulta+=" "+getOrdertype();
				}
				
			
			if(constanteTipoBD==DaoFactory.POSTGRESQL&&getLimit()!=null && !getLimit().equals(""))
				{
				consulta+=" "+ValoresPorDefecto.getValorLimit1()+" " + getLimit();
				}
			}
			
			logger.info(rest1nombre+","+rest2nombre+","+rest3nombre+","+rest4nombre+","+rest5nombre);
			logger.info("CONSULTA ARMADA>>>>>>>>>=> \n"+consulta);
			
//			long ini = System.currentTimeMillis();
			boolean cacheCiudadesPrimeraVez = false;
			boolean otraTabla = false;
			CacheFacade fachada = new CacheFacade();
			if(UtilidadCadena.noEsVacio(consulta) && consulta.toUpperCase().contains("CIUDADES"))
			{
				Vector resultados = null;
				Object objetosEnChache = fachada.obtenerDeCache(consulta+is_separador, String.class);//XXX cache
				if (objetosEnChache != null )
				{
					resultados = (Vector)objetosEnChache;
					pageContext.setAttribute("resultados" ,  resultados);
				}
				else
				{
					cacheCiudadesPrimeraVez = true;
				}
			}
			else
			{
				otraTabla = true;
			}
			
			if(otraTabla || cacheCiudadesPrimeraVez)
			{
			
				pstd = new PreparedStatementDecorator(con, consulta);
				rs = new ResultSetDecorator(pstd.executeQuery());
				
				//a=tagDao.resultadoConsulta(con, consulta);
				
				//rs  = a.getResultSet();
				//con = a.getConnection();
	
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
					
					Vector resultadosVector=new Vector();
	
					if (!campo1.equals(""))
					{
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
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(1));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(1);
						}
					}
					if (!campo2.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado= parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(2));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(2);
						}
					}
					if (!campo3.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(3));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(3);
						}
					}
					if (!campo4.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado= parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(4));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(4);
						}
					}
					if (!campo5.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(5));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(5);
						}
					}
					if (!campo6.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(6));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(6);
						}
					}
					if (!campo7.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(7));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(7);
						}
					}
					if (!campo8.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(8));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(8);
						}
					}
					if (!campo9.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(9));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(9);
						}
					}
					if (!campo10.equals(""))
					{
						if (primerEncuentro==true)
						{
							primerEncuentro=false;
						}
						else
						{
							parejasResultado=parejasResultado + is_separador;
						}
	
						if(usarVector)
						{
							resultadosVector.add(rs.getString(10));
						}
						else
						{
							parejasResultado=parejasResultado + rs.getString(10);
						}
					}
					if(usarVector)
					{
						resultados.add(resultadosVector);
					}
					else
					{
						resultados.add(parejasResultado);
					}
				}
	
				logger.info("tamaño resultados "+resultados.size());
				
				pageContext.setAttribute("resultados" ,  resultados);
				if(cacheCiudadesPrimeraVez)
				{
					fachada.guardarEnCache(consulta+is_separador, resultados);//XXX cache
				}
			}
//			if(!otraTabla)
//			{
//				long fin = System.currentTimeMillis();
//				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTIEMPO: " + (fin - ini) + "\n\n\n");
//			}
		}
		catch (java.sql.SQLException e)
		{
			logger.error(consulta + " - " + e.getMessage());
			throw new JspTagException("TagMuestraOpcionesStruts: "+e.getMessage()+e.toString() + " " + consulta);
			
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			throw new JspTagException("TagMuestraOpcionesStruts: "+e.getMessage()+e.toString() + " " + consulta);
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
	}

	/** Obtiene el separador de la cadena de resultados */
	public String getSeparador()
	{
		return is_separador;
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
     * Sets rest1isnull
     * @param rest1isnull el valor a cambiar
     */
    public void setRest1isnull(String rest1isnull)
    {
        this.rest1isnull=rest1isnull;
    }

    /**
     * Gets rest1isnull
     * @return el valor de que tiene rest1isnull
     */
    public String getRest1isnull()
    {
        return this.rest1isnull;
    }

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getOrdertype() {
		return ordertype;
	}


	/**
	 * Returns the restOr1diferente.
	 * @return String
	 */
	public String getRestOr1diferente() {
		return restOr1diferente;
	}

	/**
	 * Returns the restOr1nombre.
	 * @return String
	 */
	public String getRestOr1nombre() {
		return restOr1nombre;
	}

	/**
	 * Returns the restOr1valor.
	 * @return String
	 */
	public String getRestOr1valor() {
		return restOr1valor;
	}

	/**
	 * Sets the restOr1diferente.
	 * @param restOr1diferente The restOr1diferente to set
	 */
	public void setRestOr1diferente(String restOr1diferente) {
		this.restOr1diferente = restOr1diferente;
	}

	/**
	 * Sets the restOr1nombre.
	 * @param restOr1nombre The restOr1nombre to set
	 */
	public void setRestOr1nombre(String restOr1nombre) {
		this.restOr1nombre = restOr1nombre;
	}

	/**
	 * Sets the restOr1valor.
	 * @param restOr1valor The restOr1valor to set
	 */
	public void setRestOr1valor(String restOr1valor) {
		this.restOr1valor = restOr1valor;
	}

	/** Asigna el separador de la cadena de resultados */
	public void setSeparador(String as_separador)
	{
		if(as_separador != null && !(as_separador = as_separador.trim() ).equals("") )
			is_separador = as_separador;
	}

	/** Obtiene el alias de la tabla de la consulta */
	public String getAliastabla()
	{
		return is_aliastabla;
	}

	/** Obtiene el alias de la segunda tabla de la consulta */
	public String getAliastabla2()
	{
		return is_aliastabla2;
	}
	/** Obtiene la segunda tabla de la consulta  */
	public String getTabla2()
	{
		return is_aliastabla2;
	}

	/** Establece el alias de la tabla de la consulta */
	public void setAliastabla(String as_aliastabla)
	{
		is_aliastabla = (as_aliastabla != null) ? as_aliastabla.trim() : "";
	}

	/** Establece el alias de la segunda tabla de la consulta */
	public void setAliastabla2(String as_aliastabla2)
	{
		is_aliastabla2 = (as_aliastabla2 != null) ? as_aliastabla2.trim() : "";
	}

	/** Establece la segunda tabla de la consulta */
	public void setTabla2(String as_tabla2)
	{
		is_tabla2 = (as_tabla2 != null) ? as_tabla2.trim() : "";
	}

	public void setRest1valorcampo(String as_rest1valorcampo)
	{
		is_rest1valorcampo = as_rest1valorcampo;
	}
	public void setRest2valorcampo(String as_rest2valorcampo)
	{
		is_rest2valorcampo = as_rest2valorcampo;
	}
	public void setRest3valorcampo(String as_rest3valorcampo)
	{
		is_rest3valorcampo = as_rest3valorcampo;
	}
	public void setRest4valorcampo(String as_rest4valorcampo)
	{
		is_rest4valorcampo = as_rest4valorcampo;
	}
	public void setRestOr1valorcampo(String as_restOr1valorcampo)
	{
		is_restOr1valorcampo = as_restOr1valorcampo;
	}

	public String getRest1valorcampo()
	{
		return is_rest1valorcampo;
	}
	public String getRest2valorcampo()
	{
		return is_rest2valorcampo;
	}
	public String getRest3valorcampo()
	{
		return is_rest3valorcampo;
	}
	public String getRest4valorcampo()
	{
		return is_rest4valorcampo;
	}
	public String getRestOr1valorcampo()
	{
		return is_restOr1valorcampo;
	}
	/**
	 * @return Retorna in1nombre.
	 */
	public String getIn1nombre()
	{
		return in1nombre;
	}
	/**
	 * @param in1nombre Asigna in1nombre.
	 */
	public void setIn1nombre(String in1nombre)
	{
		this.in1nombre = in1nombre;
	}
	/**
	 * @return Retorna in1valor.
	 */
	public String getIn1valor()
	{
		return in1valor;
	}
	/**
	 * @param in1valor Asigna in1valor.
	 */
	public void setIn1valor(String in1valor)
	{
		this.in1valor = in1valor;
	}
	/**
	 * @return Retorna in2nombre.
	 */
	public String getIn2nombre()
	{
		return in2nombre;
	}
	/**
	 * @param in2nombre Asigna in2nombre.
	 */
	public void setIn2nombre(String in2nombre)
	{
		this.in2nombre = in2nombre;
	}
	/**
	 * @return Retorna in2valor.
	 */
	public String getIn2valor()
	{
		return in2valor;
	}
	/**
	 * @param in2valor Asigna in2valor.
	 */
	public void setIn2valor(String in2valor)
	{
		this.in2valor = in2valor;
	}
	/**
	 * @return Returns the menorigualquenombre.
	 */
	public String getMenorigualquenombre() {
		return menorigualquenombre;
	}
	/**
	 * @param menorigualquenombre The menorigualquenombre to set.
	 */
	public void setMenorigualquenombre(String menorigualquenombre) {
		this.menorigualquenombre = menorigualquenombre;
	}
	/**
	 * @return Returns the menorigualquevalor.
	 */
	public String getMenorigualquevalor() {
		return menorigualquevalor;
	}
	/**
	 * @param menorigualquevalor The menorigualquevalor to set.
	 */
	public void setMenorigualquevalor(String menorigualquevalor) {
		this.menorigualquevalor = menorigualquevalor;
	}
	/**
	 * @return Returns the mayorigualquenombre.
	 */
	public String getMayorigualquenombre()
	{
		return mayorigualquenombre;
	}

	/**
	 * @param mayorigualquenombre The mayorigualquenombre to set.
	 */
	public void setMayorigualquenombre(String mayorigualquenombre)
	{
		this.mayorigualquenombre = mayorigualquenombre;
	}

	/**
	 * @return Returns the mayorigualquevalor.
	 */
	public String getMayorigualquevalor()
	{
		return mayorigualquevalor;
	}

	/**
	 * @param mayorigualquevalor The mayorigualquevalor to set.
	 */
	public void setMayorigualquevalor(String mayorigualquevalor)
	{
		this.mayorigualquevalor = mayorigualquevalor;
	}

	/**
	 * @return Returns the mayorigualquevalorstr.
	 */
	public String getMayorigualquevalorstr()
	{
		return mayorigualquevalorstr;
	}

	/**
	 * @param mayorigualquevalorstr The mayorigualquevalorstr to set.
	 */
	public void setMayorigualquevalorstr(String mayorigualquevalorstr)
	{
		this.mayorigualquevalorstr = mayorigualquevalorstr;
	}

	/**
	 * @return Returns the groupby.
	 */
	public String getGroupby() {
		return groupby;
	}
	/**
	 * @param groupby The groupby to set.
	 */
	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}
	/**
	 * @return Returns the menorigualquevalorStr.
	 */
	public String getMenorigualquevalorstr() {
		return menorigualquevalorstr;
	}
	/**
	 * @param menorigualquevalorStr The menorigualquevalorStr to set.
	 */
	public void setMenorigualquevalorstr(String menorigualquevalorstr) {
		this.menorigualquevalorstr = menorigualquevalorstr;
	}
	/**
	 * @return Retorna usarVector.
	 */
	public boolean getUsarVector()
	{
		return usarVector;
	}
	/**
	 * @param usarVector Asigna usarVector.
	 */
	public void setUsarVector(boolean usarVector)
	{
		this.usarVector = usarVector;
	}
	/**
	 * @return Returns the limit.
	 */
	public String getLimit()
	{
		return limit;
	}
	/**
	 * @param "+ValoresPorDefecto.getValorLimit1()+" The "+ValoresPorDefecto.getValorLimit1()+"  to set.
	 */
	public void setLimit(String limit)
	{
		this.limit = limit;
	}

	public String getLike1nombre() {
		return like1nombre;
	}

	public void setLike1nombre(String like1nombre) {
		this.like1nombre = like1nombre;
	}

	public String getLike1valor() {
		return like1valor;
	}

	public void setLike1valor(String like1valor) {
		this.like1valor = like1valor;
	}

	/**
	 * @return Retorna the rest5diferente.
	 */
	public String getRest5diferente()
	{
		return rest5diferente;
	}

	/**
	 * @param rest5diferente The rest5diferente to set.
	 */
	public void setRest5diferente(String rest5diferente)
	{
		this.rest5diferente = rest5diferente;
	}

	/**
	 * @return Retorna the rest5nombre.
	 */
	public String getRest5nombre()
	{
		return rest5nombre;
	}

	/**
	 * @param rest5nombre The rest5nombre to set.
	 */
	public void setRest5nombre(String rest5nombre)
	{
		this.rest5nombre = rest5nombre;
	}

	/**
	 * @return Retorna the rest5valor.
	 */
	public String getRest5valor()
	{
		return rest5valor;
	}

	/**
	 * @param rest5valor The rest5valor to set.
	 */
	public void setRest5valor(String rest5valor)
	{
		this.rest5valor = rest5valor;
	}

	/**
	 * @return the campo10
	 */
	public String getCampo10() {
		return campo10;
	}

	/**
	 * @param campo10 the campo10 to set
	 */
	public void setCampo10(String campo10) {
		this.campo10 = campo10;
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

	public String getNotIn1nombre() {
		return notIn1nombre;
	}

	public void setNotIn1nombre(String notIn1nombre) {
		this.notIn1nombre = notIn1nombre;
	}

	public String getNotIn1valor() {
		return notIn1valor;
	}

	public void setNotIn1valor(String notIn1valor) {
		this.notIn1valor = notIn1valor;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}
}