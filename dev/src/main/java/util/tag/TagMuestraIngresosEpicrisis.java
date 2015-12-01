/*
 * @(#)TagMuestraIngresosEpicrisis.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.tag;

import java.io.IOException;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Tag que muestra todos los ingresos para un paciente, que pueden
 * cambiar datos en la epicris dadas las reglas del negocio (Ver 
 * documentación en papel). Un resumen de como se sacaron las 
 * consultas y las reglas del negocio se encuentra al finalizar este 
 * archivo, como comentario
 * 
 * @version 1.0, Jul 3, 2003
 */
public class TagMuestraIngresosEpicrisis extends BodyTagSupport
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * El código de la institución a la cual pertenece esta persona.
	 */
	private String codigoInstitucion = "";

	/**
	 * <code>ResultSet</code> con los resultados de la consulta de Admision
	 * Hospitalaria
	 */
	private ResultSetDecorator rs = null;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * Código del centro de costo del médico que está atendiendo
	 */
	private int codigoCentroCostoMedico=0;
	
	/**
	 * Datos del paciente sobre el que se va a consultar
	 */
	private PersonaBasica paciente;
	
	/**
	 * Define si se muestran todos los ingresos (Validacion
	 * consulta) o con las restricciones del negocio (Validación
	 * Nota Aclaratoria / Definición).
	 * 
	 * valores posibles de modo: "accesoTotal" y "accesoRestringido"
	 */
	private String modo="";

	/**
	 * El listado puede pertenecer tanto a una epicrisis de urgencias
	 * como a una de hospitalización por defecto sera de hospitalizacion 
	 */
	private boolean mostrarHospitalizacion=true;

	/**
	 * Atributo que define si de debe mostrar solo la epicrisis actual o
	 * todas
	 */
	private boolean mostrarSoloActual=false;
	
	/**
	 * Atributo que define si de debe mostrar solo las epicrisis que no
	 * sean actuales
	 */
	private boolean mostrarSoloNoActual=false;

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		//No se valida que los codigos sean 0, pues el paciente
		//puede tener la cuenta cerrada
		if (modo!=null&& paciente!=null) 
		{
			abrirResultSet();
			
			
			try 
			{
				if (rs!=null&&rs.next()) 
				{
					output = new StringBuffer();
					recorrerResultSet();
					return EVAL_BODY_BUFFERED;
				}
				else 
				{
					return SKIP_BODY;
				}
			}
			catch (SQLException e) 
			{
				throw new JspTagException("Error Escribiendo El Tag Muestra Ingresos Epicrisis  : "+e.getMessage());
			}
		}
		else 
		{
			return SKIP_BODY;
		}

	}

	/**
	 * Procesa el cuerpo del tag.
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) 
		{

			output.append(bodyContent.getString());
			try 
			{
				bodyContent.clear();
			}
			catch (IOException ex) 
			{
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del Tag Muestra Ingresos Epicrisis");
			}
		}

		try {
			if (rs.next()) 
			{

				recorrerResultSet();
				return EVAL_BODY_AGAIN;
			}

			else 
			{

				return SKIP_BODY;
			}
		}
		catch (SQLException e) 
		{

			throw new JspTagException("Error Recorriendo El Tag Muestra Ingresos Epicrisis : "+e.getMessage());
		}

	}

	/**
	 * Este método escribe en la página las <i>scripting variables</i>
	 * solicitadas por la página. También limpia el estado interno de los
	 * atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output!=null && output.length() > 0) 
		{

			try 
			{
				bodyContent.getEnclosingWriter().write(output.toString());
				clean();
			}
			catch (Exception e) 
			{

				throw new JspTagException("Error Escribiendo El Tag Muestra Ingresos Epicrisis: "+e.getMessage());
			}

		}

		return EVAL_PAGE;

	}

	/**
	 * Este método deja en valores vacíos los atributos de este Tag. Debe
	 * llamarse en doEndTag justo antes del return de dicho método; esta es una
	 * precaución que se debe tener en caso que el container donde corra esta
	 * aplicación haga pooling de tag handlers y no "limpie" los atributos
	 * deinstancia de los tags al momento de reutilizarlos.
	 */
	private void clean() throws SQLException 
	{
		modo="";
		codigoInstitucion = "";
		output = null;
		this.mostrarHospitalizacion=true;
		this.mostrarSoloActual=false;
		this.mostrarSoloNoActual=false;
		
		if (rs!=null)
		{
			rs.close();
			rs = null;
		}

	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void abrirResultSet() throws JspTagException 
	{
		try 
		{
			this.rs=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao().consultaTagMuestraIngresosEpicrisis (con, paciente, codigoCentroCostoMedico, modo, mostrarHospitalizacion, mostrarSoloActual, mostrarSoloNoActual, codigoInstitucion);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Ingresos Epicrisis : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{

		String fechaIngreso="", fechaEgreso="", horaIngreso="", horaEgreso="", codigoIngreso="";
		try 
		{
			fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_admision"));
			fechaEgreso=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_egreso"));
			horaIngreso=rs.getString("hora_admision");
			horaEgreso=rs.getString("hora_egreso");
			codigoIngreso=rs.getString("codigo_ingreso");
			if (horaIngreso!=null)
			{
				horaIngreso=UtilidadFecha.convertirHoraACincoCaracteres(horaIngreso);
			}
			if (horaEgreso!=null)
			{
				horaEgreso=UtilidadFecha.convertirHoraACincoCaracteres(horaEgreso);
			}
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos del  Tag Muestra Ingresos Epicrisis En PageContext : " + sqle.getMessage());
		}

		pageContext.setAttribute("fechaIngreso",  fechaIngreso);
		pageContext.setAttribute("horaIngreso",  horaIngreso);
		pageContext.setAttribute("codigoIngreso",  codigoIngreso);
	
		if (fechaEgreso==null)
		{
			//Caso en el que tiene ya una entrada en egresos
			//por una orden de salida PERO está en nulo
			//porque el egreso no se ha completado
			pageContext.setAttribute("fechaEgreso",  "");
			pageContext.setAttribute("horaEgreso",  "");
		}
		else if (fechaEgreso.equals("05/05/1810"))
		{
			pageContext.setAttribute("fechaEgreso",  "");
			pageContext.setAttribute("horaEgreso",  "");
		}
		else
		{
			pageContext.setAttribute("fechaEgreso",  fechaEgreso);
			pageContext.setAttribute("horaEgreso",  horaEgreso);
		}
	
	}
	/**
	 * Returns the codigoInstitucion.
	 * @return String
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Returns the con.
	 * @return Connection
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Sets the codigoInstitucion.
	 * @param codigoInstitucion The codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Sets the con.
	 * @param con The con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * @return
	 */
	public int getCodigoCentroCostoMedico() {
		return codigoCentroCostoMedico;
	}

	/**
	 * @param i
	 */
	public void setCodigoCentroCostoMedico(int i) {
		codigoCentroCostoMedico = i;
	}

	/**
	 * @return
	 */
	public String getModo() 
	{
		return modo;
	}

	/**
	 * @param string
	 */
	public void setModo(String string) 
	{
		modo = string;
	}

	/**
	 * @return
	 */
	public boolean isMostrarHospitalizacion() {
		return mostrarHospitalizacion;
	}

	/**
	 * @return
	 */
	public boolean setMostrarHospitalizacion() {
		return mostrarHospitalizacion;
	}

	/**
	 * @param b
	 */
	public void setMostrarHospitalizacion(boolean b) {
		mostrarHospitalizacion = b;
	}

	/**
	 * @return
	 */
	public boolean isMostrarSoloActual()
	{
		return mostrarSoloActual;
	}

	/**
	 * @return
	 */
	public boolean isMostrarSoloNoActual()
	{
		return mostrarSoloNoActual;
	}

	/**
	 * @param b
	 */
	public void setMostrarSoloActual(boolean b)
	{
		mostrarSoloActual = b;
	}

	/**
	 * @param b
	 */
	public void setMostrarSoloNoActual(boolean b)
	{
		mostrarSoloNoActual = b;
	}

/*
 * Validaciones:

Dados una cuenta y un centro de costo,
nos da el codigo de la epicrisis (ingreso) que cumple las siguientes
condiciones
:

(Ep Medico- abierta)
- Epicrisis en las cuales este médico es el tratante para esta institucion

SELECT cue.id from cuentas cue, ingresos ing where ing.id=cue.id_ingreso and cue.id IN (SELECT concuen.id_cuenta from consultas_cuenta concuen, solicitudes_consulta solcon, val_hospitalizacion valh where concuen.id_cuenta=solcon.id_cuenta and concuen.centro_costo=solcon.centro_costo and solcon.numero_solicitud=valh.numero_solicitud and concuen.id_cuenta=2 and concuen.centro_costo=1 and concuen.tratante=true) and cue.estado_cuenta=0 or cue.estado_cuenta=3 and ing.institucion=2


(Ep Usuario)

- Cuentas en estado facturado para un paciente dado

SELECT id_ingreso from cuentas where estado_cuenta=1 and numero_identificacion_paciente='prueba2' and tipo_identificacion_paciente='CC'

- Epicrisis de un paciente dado que tenga : una cuenta facturada y que esta tenga una valoracion hospitalaria llena

Saca lo del usuario mas el medico

SELECT cuentasPer.id from val_hospitalizacion valh, solicitudes_consulta solcon, (SELECT cue.id_ingreso as id_ingreso, cue.id as id_cuenta from cuentas cue, ingresos ing where cue.id_ingreso=ing.id and cue.estado_cuenta=1 and cue.numero_identificacion_paciente='prueba2' and cue.tipo_identificacion_paciente='CC' and ing.institucion=2) cuentasPer where valh.numero_solicitud=solcon.numero_solicitud and solcon.id_cuenta=cuentasPer.id_cuenta



UNION consultas (saca numero ingreso y numero cuenta)
(SELECT cue.id as id_cuenta, cue.id_ingreso from cuentas cue, ingresos ing where ing.id=cue.id_ingreso and cue.id IN (SELECT concuen.id_cuenta from consultas_cuenta concuen, solicitudes_consulta solcon, val_hospitalizacion valh where concuen.id_cuenta=solcon.id_cuenta and concuen.centro_costo=solcon.centro_costo and solcon.numero_solicitud=valh.numero_solicitud and concuen.id_cuenta=2 and concuen.centro_costo=1 and concuen.tratante=true) and cue.estado_cuenta=0 or cue.estado_cuenta=3 and ing.institucion=2) UNION (SELECT cuentasPer.id_cuenta, cuentasPer.id_ingreso from val_hospitalizacion valh, solicitudes_consulta solcon, (SELECT cue.id_ingreso as id_ingreso, cue.id as id_cuenta from cuentas cue, ingresos ing where cue.id_ingreso=ing.id and cue.estado_cuenta=1 and cue.numero_identificacion_paciente='prueba2' and cue.tipo_identificacion_paciente='CC' and ing.institucion=2) cuentasPer where valh.numero_solicitud=solcon.numero_solicitud and solcon.id_cuenta=cuentasPer.id_cuenta)

Ahora tenemos que sacar la fecha y hora de admision
y si existen la fecha y hora de egreso

Con este sacamos todos los ingresos y para todos estos se saca un
join de los que no existen - Para evitar hacer la misma consulta varias
veces

La primera consulta me saca los datos de las que tienen admision Y egreso
(El egreso debe tener el médico en no nulo, o si no nos encontramos con
una reversión de egreso):
SELECT eg.fecha_egreso, eg.hora_egreso, adh.fecha_admision, adh.hora_admision from egresos eg, admisiones_hospi adh where eg.cuenta=adh.cuenta and eg.numero_identificacion_medico is not null and eg.cuenta=6

El siguiente saca los datos que tienen admision Y NO egreso (en el dato fecha_egreso
y hora_egreso se pone un 'no')
SELECT '1810-05-05' as fecha_egreso, '9:35' as hora_egreso, adh.fecha_admision, adh.hora_admision from admisiones_hospi adh where adh.cuenta not in (SELECT cuenta from egresos where cuenta=6) and adh.cuenta=6

Unimos las dos consultas y obtenemos el select que se desea (Este se genera
dinámicamente en el tag)
(  (SELECT eg.fecha_egreso, eg.hora_egreso, adh.fecha_admision, adh.hora_admision from egresos eg, admisiones_hospi adh where eg.cuenta=adh.cuenta and eg.numero_identificacion_medico is not null and eg.cuenta=6) UNION (SELECT '1810-05-05' as fecha_egreso, '9:35' as hora_egreso, adh.fecha_admision, adh.hora_admision from admisiones_hospi adh where adh.cuenta not in (SELECT cuenta from egresos where cuenta=6) and adh.cuenta=6) )
 */


	/**
	 * @return
	 */
	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/**
	 * @param basica
	 */
	public void setPaciente(PersonaBasica basica)
	{
		paciente = basica;
	}

}