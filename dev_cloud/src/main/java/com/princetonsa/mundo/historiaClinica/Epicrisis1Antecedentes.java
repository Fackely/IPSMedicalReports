package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.mundo.AntecedentesVacunas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.antecedentes.Embarazo;
import com.princetonsa.mundo.antecedentes.HijoBasico;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1Antecedentes 
{
	/**
	 * Maneja los logs del módulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(Epicrisis1Antecedentes.class);
	
	/**
	 * 
	 * @param saltoLinea
	 * @param cierre
	 * @return
	 */
	public static String getNegrilla(String saltoLinea, boolean abrir)
	{
		if(saltoLinea.equals("<br>"))
		{
			if(!abrir)
				return "</b>";
			else
				return "<b>";
		}
		return "";
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @return
	 */
 	public static String cargarAntecedentesAlergias(int codigoPersona, String saltoLinea) 
	{
 		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","2");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			int codAux=ConstantesBD.codigoNuncaValido;
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				int cod=Utilidades.convertirAEntero(mapaResultado.get("cod_categoria_"+w)+"");
				if ( cod != codAux )
				{
					buffer.append(mapaResultado.get("nom_categoria_"+w) + ":  "+UtilidadCadena.listado("cod_alergia", mapaResultado, "cod_categoria", mapaResultado.get("cod_categoria_"+w)+"") );
					buffer.append(saltoLinea);
				}
				codAux = cod;
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","1"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado= getNegrilla(saltoLinea, true)+"ANTECEDENTES ALERGIAS INFECCIONES "+getNegrilla(saltoLinea, false)  +saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}

 	/**
 	 * 
 	 * @param codigoPersona
 	 * @return
 	 */
 	public static HashMap cargarAntecedentesAlergias(int codigoPersona) 
	{
 		Connection con=UtilidadBD.abrirConexion();
 	
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaObservacionesResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","2");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		
		
		criteriosBusqueda.clear();
		criteriosBusqueda.put("nroConsulta","1"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		mapaObservacionesResultado = mundo.consultarInformacion(con, criteriosBusqueda);
		mapaResultado.put("observaciones", "");
		
		if(Utilidades.convertirAEntero((mapaObservacionesResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaObservacionesResultado.get("observaciones_0")+""))
			{	
				mapaResultado.put("observaciones", mapaObservacionesResultado.get("observaciones_0")+"");
			}	
		}
		
		Utilidades.imprimirMapa(mapaResultado);

		UtilidadBD.closeConnection(con);
		return mapaResultado;
	}
 	
 	
 	
 	
 	
 	/**
 	 * 
 	 * @param codigoPersona
 	 * @param saltoLinea
 	 */
	public static String cargarAntecedentesFamiliares(int codigoPersona, String saltoLinea)  
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","4");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			int codAux=ConstantesBD.codigoNuncaValido;
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(mapaResultado.get("nom_enfer_"+w) + ":  "+mapaResultado.get("padece_"+w));
				buffer.append(saltoLinea);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","3"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0")+saltoLinea);
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES FAMILIARES "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @return
	 * @throws SQLException
	 */
	public static String cargarAntecedentesFamiliaresOculares(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","6");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			String codAux="";
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				String cod=(mapaResultado.get("cod_enfer_"+w)+"");
				if ( !cod.equals(codAux) )
				{
					buffer.append(mapaResultado.get("nom_enfer_"+w) + ":  "+UtilidadCadena.listado("nom_parentesco", mapaResultado, "cod_enfer", mapaResultado.get("cod_enfer_"+w)+"") );
					buffer.append(saltoLinea);
				}
				codAux = cod;
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","5"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES PERSONALES OCULARES "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesMedicamentos(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","12");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	"NOMBRE DEL MEDICAMENTO: "+mapaResultado.get("nombre_"+w) +saltoLinea+" "+
								"DOSIS: "+mapaResultado.get("dosis_"+w)+"  FRECUENCIA: "+mapaResultado.get("frecuencia_"+w)+saltoLinea+" "+
								"FECHA INICIO: "+mapaResultado.get("fecha_inicio_"+w)+"  FECHA FINALIZACION: "+mapaResultado.get("fecha_finalizacion_"+w)+" "+saltoLinea+saltoLinea
							);
				buffer.append(saltoLinea);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","11"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES MEDICAMENTOS"+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesMedicos(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","14");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	"NOMBRE: "+mapaResultado.get("nombre_med_"+w) +saltoLinea+" "+
								"DESDE CUANDO: "+mapaResultado.get("desde_cuando_"+w)+saltoLinea+" " +
								"TRATAMIENTO: "+mapaResultado.get("tratamiento_"+w)+saltoLinea+" " +
								"OBSERVACIONES: "+mapaResultado.get("restriccion_dietaria_"+w)+saltoLinea+saltoLinea
							);
				buffer.append(saltoLinea);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","13"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES MEDICOS"+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesQx(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","15");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	"NOMBRE DEL PROCEDIMIENTO QUIRURGICO REALIZADO: "+mapaResultado.get("q_nombre_"+w) +saltoLinea+
								"FECHA: "+mapaResultado.get("q_fecha_"+w)+"  CAUSA: "+mapaResultado.get("q_causa_"+w)+" "+saltoLinea+
								"COMPLICACIONES: "+mapaResultado.get("q_complicaciones_"+w)+"  RECOMENDACIONES: "+mapaResultado.get("q_recomendaciones_"+w)+saltoLinea
							);
				buffer.append(saltoLinea);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","13"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES QUIRURGICOS "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	public static String cargarAntecedentesOdontologicos(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","25");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				if(w==0)
				{
					buffer.append("HABITOS ODONTOLOGICOS: "+saltoLinea);
				}
				
				buffer.append(	
								mapaResultado.get("nombre_"+w)+saltoLinea
							);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","24"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES ODONTOLOGICOS "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	public static String cargarAntecedentesTraumatismoYfracturasDentales(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","26");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	mapaResultado.get("tnombre_"+w)+saltoLinea
							);
				buffer.append(saltoLinea);
			}
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"TRAUMATISMOS Y FRACTURAS DENTALES "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	public static String cargarAntecedentesTratamientosOdontologicosPrevios(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","27");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append( "TRATAMIENTO: "+mapaResultado.get("pnombre_"+w)+"  FECHA INICIO:"+mapaResultado.get("fecha_inicio_"+w)+"  FECHA FINALIZACION: "+mapaResultado.get("fecha_finalizacion_"+w)+saltoLinea
							);
			}
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"TRATAMIENTOS ODONTOLOGICOS"+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesPersonalesOculares(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","8");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				if(w==0)
				{
					buffer.append("ANTECEDENTES MEDICOS: "+saltoLinea);
				}
				
				buffer.append(	
								"NOMBRE: "+mapaResultado.get("nom_enfer_"+w)+" "+
								"DESDE CUANDO: "+mapaResultado.get("desde_cuando_"+w)+" "+
								"TRATAMIENTO: "+mapaResultado.get("tratamiento_"+w)+saltoLinea
							);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","7"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES PERSONALES OCULARES "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesPersonalesOcularesQx(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","9");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append( "NOMBRE PROCEDIMIENTO: "+mapaResultado.get("nom_quirur_"+w)+"  FECHA:"+mapaResultado.get("fecha_"+w)+"  CAUSA: "+mapaResultado.get("causa_"+w)+saltoLinea
							);
			}
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES QUIRURGICOS "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesToxicos(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","17");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			String fechaHoraAux="";
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				String fechaHora=mapaResultado.get("fechagrabacion_"+w)+" "+mapaResultado.get("horagrabacion_"+w);
				if(!fechaHora.equals(fechaHoraAux))
				{
					buffer.append("FECHA: "+ UtilidadFecha.conversionFormatoFechaAAp(mapaResultado.get("fechagrabacion_"+w)+"")+"   HORA: "+mapaResultado.get("horagrabacion_"+w)+", ");
				}
				fechaHoraAux=fechaHora;
				buffer.append(	
								"NOMBRE: "+mapaResultado.get("nombre_"+w)+", "+
								"HABITO ACTUAL?: "+UtilidadTexto.imprimirSiNo(mapaResultado.get("actual_"+w)+"")+", "+
								"TIEMPO: "+mapaResultado.get("tiempo_habito_"+w)+", "+
								"CANTIDAD: "+mapaResultado.get("cantidad_"+w)+", "+
								"FECUENCIA: "+mapaResultado.get("frecuencia_"+w)+saltoLinea
							);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","16"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES TOXICOS "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesTransfuncionales(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","19");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	
								"COMPONENTE TRANSFUNDIDO: "+mapaResultado.get("nombre_"+w)+" "+
								"FECHA: "+mapaResultado.get("fecha_"+w)+" "+
								"LUGAR: "+mapaResultado.get("lugar_"+w)+" "+
								"EDAD PACIENTE: "+mapaResultado.get("edad_paciente_"+w)+" "+
								"DONANTE: "+mapaResultado.get("donante_"+w)+" "+
								"CAUSA: "+mapaResultado.get("causa_"+w)+saltoLinea
							);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","18"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES TRANSFUNCIONALES "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @param institucion
	 * @return
	 */
	public static String cargarAntecedentesOtros(int codigoPersona, String saltoLinea, int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
 		StringBuffer buffer= new StringBuffer();
 		String resultado="";
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		
		criteriosBusqueda.put("nroConsulta","22");
		criteriosBusqueda.put("paciente",codigoPersona+"");
		criteriosBusqueda.put("institucion", institucion);
		
		mapaResultado=mundo.consultarInformacion(con, criteriosBusqueda);	
		Utilidades.imprimirMapa(mapaResultado);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+"")); w++)
			{
				buffer.append(	
								//"FECHA: "+mapaResultado.get("fecha_"+w)+" "+
								//"HORA: "+mapaResultado.get("hora_"+w)+" "+
								"ANTECEDENTE: "+mapaResultado.get("nombre_"+w)+" "+
								"DESCRIPCION: "+mapaResultado.get("descripcion_"+w)+saltoLinea
							);
			}
		}
		
		criteriosBusqueda.clear();
		mapaResultado.clear();
		criteriosBusqueda.put("nroConsulta","21"); //consultar las observaciones
		criteriosBusqueda.put("paciente", codigoPersona + "");
		criteriosBusqueda.put("institucion", institucion);
		mapaResultado= mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(Utilidades.convertirAEntero((mapaResultado.get("numRegistros")+""))>0)
		{
			if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+""))
			{	
				buffer.append("OBSERVACIONES GENERALES: "+mapaResultado.get("observaciones_0"));
			}	
		}
		
		if(!UtilidadTexto.isEmpty(buffer.toString()))
		{
			resultado=getNegrilla(saltoLinea, true)+"ANTECEDENTES ENTORNO FAMILIAR Y SITUACION PERSONAL "+getNegrilla(saltoLinea, false)+saltoLinea+" "+buffer.toString()+saltoLinea;
		}
		UtilidadBD.closeConnection(con);
		return resultado.toUpperCase();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @param saltoLinea
	 * @return
	 */
	public static HashMap<Object, Object> cargarAntecedentesVacunas(int codigoPersona)  
	{
		Connection con=UtilidadBD.abrirConexion();
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap<Object,Object> criteriosBusqueda = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaResultado = new HashMap<Object,Object>();
		HashMap<Object,Object> mapaTiposInmunizacion = new HashMap<Object,Object>();
		String observaciones="";
		StringBuffer buffer= new StringBuffer();
		int colspan=5, posicion=-1;
		
		criteriosBusqueda.put("nroConsulta","20");
		criteriosBusqueda.put("paciente", codigoPersona + "");
			
		mapaResultado = mundo.consultarInformacion(con, criteriosBusqueda);
		
		if(!UtilidadTexto.isEmpty(mapaResultado.get("observaciones_0")+"".trim()))
		{
			observaciones=mapaResultado.get("observaciones_0")+"";
		}	
		
		criteriosBusqueda.clear(); 
		AntecedentesVacunas mundoVacu = new AntecedentesVacunas();

		criteriosBusqueda.put("codigoPaciente", codigoPersona+"");
		criteriosBusqueda.put("nroConsulta","1"); 
			
		try 
		{
			mapaTiposInmunizacion=(mundoVacu.consultarInformacion(con, criteriosBusqueda));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
			
		criteriosBusqueda.put("nroConsulta","3");

		try 
		{
			mapaResultado=(mundoVacu.consultarInformacion(con, criteriosBusqueda));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		mapaResultado.put("mapaTiposInmunizacion", mapaTiposInmunizacion);
		mapaResultado.put("observaciones", observaciones);
		
		UtilidadBD.closeConnection(con);
		return mapaResultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @param institucion
	 * @param request
	 * @return
	 */
	public static HashMap<Object, Object> cargarAntecedentesGinecoObstetricos(Connection con, PersonaBasica paciente, int institucion, HttpServletRequest request) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap<Object,Object> mp = new HashMap<Object,Object>();
		HashMap<Object, Object> mapaAntGineco= new HashMap<Object, Object>();

		mapaAntGineco.clear();
		mp.put("nroConsulta","10");
		mp.put("paciente", paciente.getCodigoPersona() + "");
		mp.put("institucion", institucion + "");
		mp = mundo.consultarInformacion(con, mp);
			
		mapaAntGineco.put("observaciones", mp.get("observaciones_0")+"");

		AntecedentesGinecoObstetricosForm antecedentesBean = new AntecedentesGinecoObstetricosForm();
			
		// Se carga toda la informaci?n de la bd en el momento para mostrar un resumen completo
		AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
		antecedentes.setPaciente(paciente);
			
		//-- Hacer Otra Funci?n de Cargar. Con los Parametros de Busqueda en el MAPA
		HashMap mapa = new HashMap();
		mapa.put("paciente", paciente.getCodigoPersona()+"" );
		try 
		{
			antecedentes.cargar(con, mapa);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		mapa=cargarBeanCompleto(antecedentes,antecedentesBean,mapaAntGineco);
			
		//request.setAttribute("numeroEmbarazos", ""+antecedentesBean.getNumeroEmbarazos());
		request.setAttribute("numeroEmbarazos", ""+mapaAntGineco.get("NumeroEmbarazos"));
			
		//request.setAttribute("numMetodosAnticonceptivos", ""+antecedentesBean.getNumMetodosAnticonceptivos());
		request.setAttribute("numeroEmbarazos", ""+mapaAntGineco.get("NumMetodosAnticonceptivos"));

		//--
		int nroRows = UtilidadCadena.vInt(mapaAntGineco.get("NumeroEmbarazos")+"");
		for(int i=1; i<=nroRows; i++)
		{
			String nH = (String)antecedentesBean.getValue("numeroHijos_" +i);
			int numH = Integer.parseInt(nH);
				
			request.setAttribute("numeroHijos_"+i, nH);
			mapaAntGineco.put("numeroHijos_"+i, nH);
				
			for(int j=1; j<=numH; j++)
			{
				//String numeroTiposPartoVaginalTemp=(String)antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j);
				String numeroTiposPartoVaginalTemp = mapaAntGineco.get("numTiposPartoVaginal_"+i+"_"+j)+"";
					
				if (numeroTiposPartoVaginalTemp!=null)
				{
					request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j));
				}
				else
				{
					request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, "0");
					mapaAntGineco.put("numTiposPartoVaginal_"+i+"_"+j, "0");
				}
					
			}
		}
		
		Utilidades.imprimirMapa(mapaAntGineco);
		
		return mapaAntGineco;
	}
	
	/**
	 * Carga el bean de antecedentes ginecoobstetricos con los historicos
	 * @param forma 
 	 */
	public static HashMap<Object, Object> cargarBeanCompleto(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, HashMap<Object, Object> mapaAntGineco)
	{
		// carga los basicos del bean
		mapaAntGineco=cargarBean(antecedentes, bean, mapaAntGineco);
		
		// los historicos
		ArrayList<Object> historicos = antecedentes.getAntecedentesHistoricos();
		bean.setHistoricos(historicos);
		mapaAntGineco.put("historicos", historicos);
		return mapaAntGineco;
	}
	
	
	/**
	 * Carga el form de antecedentes ginecoobst?tricos con la informaci?n
	 * pertinente contenida en el objeto.
	 * @param forma 
	 * @param 	AntecedentesGinecoObstetricos, antecedentes
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 */
	public static HashMap<Object, Object> cargarBean(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, HashMap<Object, Object> mapaAntGineco)
	{	
		//---No historicos y no modificables despues de grabados
		if( !antecedentes.getRangoEdadMenarquia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getCodigo());
			mapaAntGineco.put("RangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getCodigo()+"");

			if( antecedentes.getRangoEdadMenarquia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenarquia("No se ha grabado informaci?n");
				mapaAntGineco.put("RangoEdadMenarquia","-1");
				mapaAntGineco.put("NombreRangoEdadMenarquia","No se ha grabado informaci?n");
			}	
			else
			{
				mapaAntGineco.put("ExisteRangoEdadMenarquia","true");
				mapaAntGineco.put("NombreRangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getValue()+"");
				bean.setNombreRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getValue());
				bean.setExisteRangoEdadMenarquia(true);
			}
		}

		mapaAntGineco.put("OtroEdadMenarquia",antecedentes.getOtroEdadMenarquia());
		bean.setOtraEdadMenarquia(antecedentes.getOtroEdadMenarquia());
		
		
		if( !antecedentes.getRangoEdadMenopausia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getCodigo());
			mapaAntGineco.put("RangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getCodigo()+"");
			
			if( antecedentes.getRangoEdadMenopausia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenopausia("No se ha grabado informacion");
				mapaAntGineco.put("NombreRangoEdadMenopausia","No se ha grabado informacion");
			}	
			else
			{
				mapaAntGineco.put("NombreRangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getValue()+"");
				mapaAntGineco.put("ExisteRangoEdadMenopausia","true");
				
				bean.setNombreRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getValue());
				bean.setExisteRangoEdadMenopausia(true);
			}
		}
		bean.setOtraEdadMenopausia(antecedentes.getOtroEdadMenopausia());
		mapaAntGineco.put("OtraEdadMenopausia",antecedentes.getOtroEdadMenopausia()+"");
		
		if(antecedentes.getInicioVidaSexual() == 0 )
		{
			bean.setInicioVidaSexual("");
			mapaAntGineco.put("InicioVidaSexual","");
		}	
		else
		{
			bean.setInicioVidaSexual(""+antecedentes.getInicioVidaSexual());
			mapaAntGineco.put("InicioVidaSexual",""+antecedentes.getInicioVidaSexual());
		}	

		if(antecedentes.getInicioVidaObstetrica() == 0 ) 
		{
			bean.setInicioVidaObstetrica("");
			mapaAntGineco.put("InicioVidaObstetrica","");
		}	
		else
		{
			bean.setInicioVidaObstetrica(""+antecedentes.getInicioVidaObstetrica());
			mapaAntGineco.put("InicioVidaObstetrica",""+antecedentes.getInicioVidaObstetrica());
		}	
		
		String observacionesStr =  antecedentes.getObservaciones();
		
		if( observacionesStr == null )
			observacionesStr = "";
		
		mapaAntGineco.put("observacionesStr",observacionesStr);
				
		
		//if( bean.estado.equals("resumen") )
		{
			bean.setObservacionesViejas(observacionesStr.replaceAll("\n", "<br>"));
			mapaAntGineco.put("ObservacionesViejas",observacionesStr.replaceAll("\n", "<br>"));
		}	
		//else
			//bean.setObservacionesViejas(observacionesStr.replaceAll("<br>", "\n"));
		//		Fin no historicos y no modificables despues de grabados
		
		// 		M?todos anticonceptivos
		ArrayList metodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		bean.setNumMetodosAnticonceptivos(metodosAnticonceptivos.size());
		mapaAntGineco.put("NumMetodosAnticonceptivos",metodosAnticonceptivos.size()+"");
		

		for( int i=0; i < metodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)metodosAnticonceptivos.get(i);
			
			bean.setValue("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			mapaAntGineco.put("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			bean.setValue("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getValue());
			mapaAntGineco.put("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getDescripcion());
			mapaAntGineco.put("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
			mapaAntGineco.put("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			
			if( !metodo.getDescripcion().trim().equals("") )
			{
				bean.setValue("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
				mapaAntGineco.put("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			}	
		}
		//		Fin m?todos anticonceptivos
		
		//		Embarazos
		ArrayList embarazos = antecedentes.getEmbarazos();
		
		bean.setNumeroEmbarazos(embarazos.size());
		mapaAntGineco.put("NumeroEmbarazos", embarazos.size()+"");
		mapaAntGineco.put("NumGestaciones", embarazos.size()+"");
		
		bean.setNumGestaciones(bean.getNumeroEmbarazos());
		for(int i=1; i<=embarazos.size(); i++)
		{
			Embarazo embarazo = (Embarazo)embarazos.get(i-1);
			
			bean.setValue("codigo_"+i, embarazo.getCodigo()+"");
			mapaAntGineco.put("codigo_"+i, embarazo.getCodigo()+"");
			
			//Guardamos el n?mero de hijos por embarazo
			bean.setValue("numeroHijos_" + i, embarazo.getHijos().size() + "");
			mapaAntGineco.put("numeroHijos_" + i, embarazo.getHijos().size() + "");
						
			bean.setValue("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			mapaAntGineco.put("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			
			bean.setValue("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			mapaAntGineco.put("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			
			bean.setValue("duracion_"+i,embarazo.getDuracion());
			mapaAntGineco.put("duracion_"+i,embarazo.getDuracion());

			bean.setValue("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			mapaAntGineco.put("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			
			bean.setValue("legrado_"+i,embarazo.getLegrado());
			mapaAntGineco.put("legrado_"+i,embarazo.getLegrado());
			
			int compTempo[]=embarazo.getComplicacion();
			for(int y=0;y<compTempo.length;y++)
			{
				if(compTempo[y]!=0)
				{
					bean.setValue("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
					mapaAntGineco.put("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
				}
			}
			Vector nombresComplicaciones=embarazo.getNombresComplicaciones();
			for(int y=0;y<nombresComplicaciones.size();y++)
			{
				bean.setValue("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y));
				mapaAntGineco.put("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y)+"");
			}
			
			Vector otrasComplicaciones=embarazo.getOtraComplicacion();
			for(int j=0; j<otrasComplicaciones.size();j++)
			{
				bean.setValue("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
				mapaAntGineco.put("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
			}
			bean.setValue("numOtraComplicacion_"+i, new Integer(otrasComplicaciones.size()));
			mapaAntGineco.put("numOtraComplicacion_"+i, otrasComplicaciones.size()+"");
						
			bean.setValue("tipoTrabajoParto_"+i, Integer.toString(embarazo.getTrabajoParto().getCodigo()));
			mapaAntGineco.put("tipoTrabajoParto_"+i, embarazo.getTrabajoParto().getCodigo()+"");
			
			
			if( embarazo.getTrabajoParto().getCodigo() == -1 )
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, "");
				mapaAntGineco.put("nombreTipoTrabajoParto_"+i, "");
			}
			else
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
				mapaAntGineco.put("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
			}
				
			bean.setValue("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());
			mapaAntGineco.put("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());

			//	Hijos embarazo.
			ArrayList hijos = embarazo.getHijos();
			ArrayList formasPartoVaginal;
			bean.setValue("numeroHijos_"+i, ""+hijos.size());
			mapaAntGineco.put("numeroHijos_"+i, ""+hijos.size());

			for(int j=1; j<=hijos.size(); j++)
			{
				HijoBasico hijo = new HijoBasico(); 
				hijo = (HijoBasico)hijos.get(j-1);
				
				boolean partoVaginal = true;
				
				if( hijo.isVivo() )
				{
					bean.setNumVivos(bean.getNumVivos()+1);
					bean.setNumVivosGrabados(bean.getNumVivos());
					bean.setValue("vitalidad_"+i+"_"+j, "vivo");
					mapaAntGineco.put("vitalidad_"+i+"_"+j, "vivo");
				}
				else
				{
					bean.setNumMuertos(bean.getNumMuertos()+1);
					bean.setNumMuertosGrabados(bean.getNumMuertos());
					bean.setValue("vitalidad_"+i+"_"+j, "muerto");
					mapaAntGineco.put("vitalidad_"+i+"_"+j, "muerto");
				}
								
				if( hijo.isAborto() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					mapaAntGineco.put("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("4"));
					mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("4"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));
					mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));

					partoVaginal = false;
					bean.setNumAbortos(bean.getNumAbortos()+1);
					mapaAntGineco.put("NumAbortos", ""+bean.getNumAbortos()+1);

					bean.setNumAbortosGrabados(bean.getNumAbortos());
					mapaAntGineco.put("NumAbortosGrabados", ""+bean.getNumAbortos());
					
				} 
				else
				if( hijo.isCesarea() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					mapaAntGineco.put("cargadoBD_"+i+"_"+j, "true");

					bean.setValue("tiposParto_"+i+"_"+j, new String("5"));
					mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("5"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					
					partoVaginal = false;
					bean.setNumCesareas(bean.getNumCesareas()+1);
					mapaAntGineco.put("NumCesareas", ""+(bean.getNumCesareas()+1));

					bean.setNumCesareasGrabadas(bean.getNumCesareas());
					mapaAntGineco.put("NumCesareasGrabadas", ""+bean.getNumCesareas());
				} 
				else
				if( hijo.getOtroTipoParto() != null && !hijo.getOtroTipoParto().equals("") )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					mapaAntGineco.put("cargadoBD_"+i+"_"+j, "true");
					
					bean.setValue("tiposParto_"+i+"_"+j, new String("0"));
					mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("0"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					
					bean.setValue("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					mapaAntGineco.put("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					
					partoVaginal = false;
				}
				else
				if( ( formasPartoVaginal = hijo.getFormasNacimientoVaginal() ).size() > 0 )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					mapaAntGineco.put("cargadoBD_"+i+"_"+j, "true");
					
					bean.setNumPartos(bean.getNumPartos()+1);
					mapaAntGineco.put("NumPartos", ""+(bean.getNumPartos()+1));
					
					bean.setNumPartosGrabados(bean.getNumPartos());
					mapaAntGineco.put("NumPartosGrabados", ""+bean.getNumPartos());
					
					boolean esvalido = true;					
					if(formasPartoVaginal.size()==1)
					{
						InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(0);
						// En este caso no debemos mostrarlo de forma tradicional
						if( tipoPVInfo.getCodigo() == -2 )
						{
							bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
							mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("3"));

							bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
							mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

							bean.setValue("numTiposPartoVaginal_"+i+"_"+j, "0");
							mapaAntGineco.put("numTiposPartoVaginal_"+i+"_"+j, "0");

							esvalido = false;
						}
					}
					if( esvalido )
					{

						bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
						mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("3"));

						bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
						mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

						bean.setValue("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
						mapaAntGineco.put("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
								
						for(int k=0; k<formasPartoVaginal.size(); k++)
						{
							InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(k);
							
							bean.setValue("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
							mapaAntGineco.put("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
								
							bean.setValue("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
							mapaAntGineco.put("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
														
						}

					}
					
				}
				if( partoVaginal && hijo.getOtraFormaNacimientoVaginal() != null && !hijo.getOtraFormaNacimientoVaginal().equals("") )
				{
					bean.setValue("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());
					mapaAntGineco.put("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());

					bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
					mapaAntGineco.put("tiposParto_"+i+"_"+j, new String("3"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
					mapaAntGineco.put("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
				}
				String tempoSexo=hijo.getSexo()+"";
				if(tempoSexo!=null && !tempoSexo.equals("null") && (tempoSexo.equals(ConstantesBD.codigoSexoMasculino+"") || tempoSexo.equals(ConstantesBD.codigoSexoFemenino+"")))
				{
					bean.setValue("sexo_"+i+"_"+j,hijo.getSexo()+"");			
					mapaAntGineco.put("sexo_"+i+"_"+j,hijo.getSexo()+"");
				}
				else
				{
					bean.setValue("sexo_"+i+"_"+j,"-1");
					mapaAntGineco.put("sexo_"+i+"_"+j,"-1");
				}
				bean.setValue("peso_"+i+"_"+j, hijo.getPeso());
				mapaAntGineco.put("peso_"+i+"_"+j, hijo.getPeso());

				bean.setValue("lugar_"+i+"_"+j, hijo.getLugar());
				mapaAntGineco.put("lugar_"+i+"_"+j, hijo.getLugar());
			}
			//	Fin hijos embarazo			
		}
		//		Fin embarazos
		return mapaAntGineco;
	}
	
}	
