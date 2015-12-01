package com.sysmedica.util;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.PersonaBasica;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

public class UtilidadGenArchivos {

	private Logger logger = Logger.getLogger(UtilidadGenArchivos.class);
	
	public static String rutaEpidemiologia;
	
	public static String rutaReportesSecretaria;
	
	public static String rutaReportesGenerales;
	
	public static String rutaReportesSivigila;
	
	
	
	/**
	 * Esta funcion devuelve las lineas de texto (casos de dengue) que van en el archivo plano de notificaciones individuales.
	 * Cada linea esta ubicada en una posicion del Vector que se retorna.
	 * @param resultadosConsulta
	 * @param resultadosHallazgos
	 * @param semana
	 * @param anyo
	 * @return
	 */
	public static Vector generarVectorDengue(ResultSet resultadosConsulta, Vector resultadosHallazgos, int semana, int anyo)
	{
		int resultado = 0;
		String nombreArchivo = "asdfg.txt";
		String ruta = rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		Vector vectorHallazgos = new Vector();
		
		Vector elementos = new Vector();
		
		Vector lineaHallazgos = new Vector();
				
		for (int i=0; i<26; i++) {
			
			lineaHallazgos.add("2");
		}
		
		
		for (int i=0; i<resultadosHallazgos.size(); i++) {
			
			Vector lineaResultados = (Vector)resultadosHallazgos.get(i);
			
			for (int j=0; j<lineaResultados.size(); j++) {
				
				lineaHallazgos.set(Integer.parseInt(lineaResultados.get(0).toString()), "1");
			}
		}
		
		
		
		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				Vector lineaResultados = (Vector)resultadosHallazgos.get(x);
				
				for (int j=0; j<lineaResultados.size(); j++) {
					
					lineaHallazgos.set(Integer.parseInt(lineaResultados.get(j).toString()), "1");
				}
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaFiebreAmarilla")));
				lineaDatos.add(resultadosConsulta.getString("fechaApliVacunaFiebre"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaHepatitisBDosis1")));
				lineaDatos.add(resultadosConsulta.getString("fechaVacunaHepaDosis1"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaHepatitisBDosis2")));
				lineaDatos.add(resultadosConsulta.getString("fechaVacunaHepaDosis2"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaHepatitisBDosis3")));
				lineaDatos.add(resultadosConsulta.getString("fechaVacunaHepaDosis3"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaHepatitisADosis1")));
				lineaDatos.add(resultadosConsulta.getString("fechaVacunaHepatADosis1"));
				lineaDatos.add(lineaHallazgos.get(0));
				lineaDatos.add(lineaHallazgos.get(1));
				lineaDatos.add(lineaHallazgos.get(2));
				lineaDatos.add(lineaHallazgos.get(3));
				lineaDatos.add(lineaHallazgos.get(4));
				lineaDatos.add(lineaHallazgos.get(5));
				lineaDatos.add(lineaHallazgos.get(6));
				lineaDatos.add(lineaHallazgos.get(7));
				lineaDatos.add(lineaHallazgos.get(8));
				lineaDatos.add(lineaHallazgos.get(9));
				lineaDatos.add(lineaHallazgos.get(10));
				lineaDatos.add(lineaHallazgos.get(11));
				lineaDatos.add(lineaHallazgos.get(12));
				lineaDatos.add(lineaHallazgos.get(13));
				lineaDatos.add(lineaHallazgos.get(14));
				lineaDatos.add(lineaHallazgos.get(15));
				lineaDatos.add(lineaHallazgos.get(16));
				lineaDatos.add(lineaHallazgos.get(17));
				lineaDatos.add(lineaHallazgos.get(18));
				lineaDatos.add(lineaHallazgos.get(19));
				lineaDatos.add(lineaHallazgos.get(20));
				lineaDatos.add(lineaHallazgos.get(21));
				lineaDatos.add(lineaHallazgos.get(22));
				lineaDatos.add(lineaHallazgos.get(23));
				lineaDatos.add(lineaHallazgos.get(24));
				lineaDatos.add(lineaHallazgos.get(25));
				lineaDatos.add(bool2num(resultadosConsulta.getString("desplazamiento")));
				lineaDatos.add(resultadosConsulta.getString("fechaDesplazamiento"));
				lineaDatos.add(resultadosConsulta.getString("codigoDepartamento")+resultadosConsulta.getString("codigoMunicipio"));
				lineaDatos.add(resultadosConsulta.getString("casoFiebreAmarilla"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("casoEpizootia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("presenciaAedes")));
				
				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
				x++;
			}
		}
		catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
		
		return elementos;
	}
	
	
	
	
	
	public static Vector generarVectorIntoxicacion(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("tipoIntoxicacion"));
				lineaDatos.add(resultadosConsulta.getString("nombreProducto"));
				lineaDatos.add(resultadosConsulta.getString("tipoExposicion"));
				lineaDatos.add(num2num(resultadosConsulta.getInt("produccion")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("almacenamiento")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("agricola")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("saludPublica")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("domiciliaria")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("tratHumano")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("tratVeterinario")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("transporte")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("mezcla")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("mantenimiento")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("cultivo")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("otros")));
				lineaDatos.add(resultadosConsulta.getString("otraActividad"));
				lineaDatos.add(resultadosConsulta.getString("fechaExposicion"));
				lineaDatos.add(resultadosConsulta.getString("horaExposicion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("viaExposicion")));
				lineaDatos.add(resultadosConsulta.getString("otraViaExposicion"));
				lineaDatos.add(resultadosConsulta.getString("escolaridad"));
				lineaDatos.add(num2num(resultadosConsulta.getInt("embarazada")));
				lineaDatos.add("ZZ");
				lineaDatos.add("WW");
				lineaDatos.add(estadoCivil(resultadosConsulta.getString("estado_civil")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("alerta")));
				lineaDatos.add(num2num(resultadosConsulta.getInt("investigacion")));
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacion"));
				lineaDatos.add(resultadosConsulta.getString("fechaInforma"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	public static Vector generarVectorLepra(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("criterioClinico"));
				lineaDatos.add(resultadosConsulta.getString("indiceBacilar"));
				lineaDatos.add(resultadosConsulta.getString("clasificacion"));
				lineaDatos.add(resultadosConsulta.getString("resultadosBiopsia"));
				lineaDatos.add(resultadosConsulta.getString("ojoDerecho"));
				lineaDatos.add(resultadosConsulta.getString("manoDerecha"));
				lineaDatos.add(resultadosConsulta.getString("pieDerecho"));
				lineaDatos.add(resultadosConsulta.getString("ojoIzquierdo"));
				lineaDatos.add(resultadosConsulta.getString("manoIzquierda"));
				lineaDatos.add(resultadosConsulta.getString("pieIzquierdo"));
				lineaDatos.add(resultadosConsulta.getString("tipoCasoLepra"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tieneCicatriz")));
				lineaDatos.add(resultadosConsulta.getString("fuenteContagio"));
				lineaDatos.add(resultadosConsulta.getString("metodoCaptacion"));
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tieneConvivientes")));
				lineaDatos.add(resultadosConsulta.getString("totalConvivientes"));
				lineaDatos.add(resultadosConsulta.getString("totalExaminados"));
				lineaDatos.add(resultadosConsulta.getString("sanosConCicatriz"));
				lineaDatos.add(resultadosConsulta.getString("sanosSinCicatriz"));
				lineaDatos.add(resultadosConsulta.getString("sintomaticosConCicatriz"));
				lineaDatos.add(resultadosConsulta.getString("sintomaticosSinCicatriz"));
				lineaDatos.add(resultadosConsulta.getString("vacunadosBcg"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	

	public static Vector generarVectorParalisis(ResultSet resultadosConsulta, HashMap resultadosExtremidades, HashMap resultadosGruposEdad, int semana, int anyo)
	{
		Vector elementos = new Vector();
		
		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombreMadre"));
				lineaDatos.add(resultadosConsulta.getString("nombrePadre"));
				lineaDatos.add(resultadosConsulta.getString("fechaInicioInvestigacion"));
				lineaDatos.add(resultadosConsulta.getString("numeroDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tieneCarnet")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("respiratorios")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("digestivos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("dolorMuscular")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("signosMeningeos1")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebreInicioParalisis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("instalacion")));
				lineaDatos.add(resultadosConsulta.getString("progresion"));
				lineaDatos.add(resultadosConsulta.getString("fechaInicioParalisis"));
				
				lineaDatos.add(bool2num(resultadosExtremidades.get("paresia_1").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("paralisis_1").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("flacida_1").toString()));
				lineaDatos.add(resultadosExtremidades.get("localizacion_1").toString());
				lineaDatos.add(resultadosExtremidades.get("sensibilidad_1").toString());
				lineaDatos.add(resultadosExtremidades.get("rot_1").toString());
				
				lineaDatos.add(bool2num(resultadosExtremidades.get("paresia_2").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("paralisis_2").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("flacida_2").toString()));
				lineaDatos.add(resultadosExtremidades.get("localizacion_2").toString());
				lineaDatos.add(resultadosExtremidades.get("sensibilidad_2").toString());
				lineaDatos.add(resultadosExtremidades.get("rot_2").toString());
				
				lineaDatos.add(bool2num(resultadosExtremidades.get("paresia_3").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("paralisis_3").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("flacida_3").toString()));
				lineaDatos.add(resultadosExtremidades.get("localizacion_3").toString());
				lineaDatos.add(resultadosExtremidades.get("sensibilidad_3").toString());
				lineaDatos.add(resultadosExtremidades.get("rot_3").toString());
				
				lineaDatos.add(bool2num(resultadosExtremidades.get("paresia_4").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("paralisis_4").toString()));
				lineaDatos.add(bool2num(resultadosExtremidades.get("flacida_4").toString()));
				lineaDatos.add(resultadosExtremidades.get("localizacion_4").toString());
				lineaDatos.add(resultadosExtremidades.get("sensibilidad_4").toString());
				lineaDatos.add(resultadosExtremidades.get("rot_4").toString());
				
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("musculosRespiratorios")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("signosMeningeos2")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("babinsky")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("brudzinsky")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("paresCraneanos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("liquidoCefalo")));
				lineaDatos.add(resultadosConsulta.getString("fechaTomaLiquido"));
				lineaDatos.add(resultadosConsulta.getString("celulas"));
				lineaDatos.add(resultadosConsulta.getString("globulosRojos"));
				lineaDatos.add(resultadosConsulta.getString("leucocitos"));
				lineaDatos.add(resultadosConsulta.getString("linfocitos"));
				lineaDatos.add(resultadosConsulta.getString("proteinas"));
				lineaDatos.add(resultadosConsulta.getString("glucosa"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("electromiografia")));
				lineaDatos.add(resultadosConsulta.getString("fechaTomaElectro"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("velocidadConduccion")));
				lineaDatos.add(resultadosConsulta.getString("resultadoConduccion"));
				lineaDatos.add(resultadosConsulta.getString("fechaTomaVelocidad"));
				lineaDatos.add(resultadosConsulta.getString("impresionDiagnostica"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("muestraMateriaFecal")));
				lineaDatos.add(resultadosConsulta.getString("fechaTomaFecal"));
				lineaDatos.add(resultadosConsulta.getString("fechaEnvioFecal"));
				lineaDatos.add(resultadosConsulta.getString("fechaRecepcionFecal"));
				lineaDatos.add(resultadosConsulta.getString("fechaResultadoFecal"));
				lineaDatos.add(resultadosConsulta.getString("virusAislado"));
				
				lineaDatos.add(resultadosGruposEdad.get("poblacionmeta_1").toString());
				lineaDatos.add(resultadosGruposEdad.get("reciennacido_1").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop1_1").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop2_1").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop3_1").toString());
				lineaDatos.add(resultadosGruposEdad.get("adicionales_1").toString());
				
				lineaDatos.add(resultadosGruposEdad.get("poblacionmeta_2").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop1_2").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop2_2").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop3_2").toString());
				lineaDatos.add(resultadosGruposEdad.get("adicionales_2").toString());
				
				lineaDatos.add(resultadosGruposEdad.get("poblacionmeta_3").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop1_3").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop2_3").toString());
				lineaDatos.add(resultadosGruposEdad.get("vop3_3").toString());
				lineaDatos.add(resultadosGruposEdad.get("adicionales_3").toString());
				
				lineaDatos.add(resultadosConsulta.getString("fechaVacunacionBloqueo"));
				lineaDatos.add(resultadosConsulta.getString("fechaCulminacionVacunacion"));
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	

	public static Vector generarVectorRabia(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("tipoAgresion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("provocada")));
				lineaDatos.add(resultadosConsulta.getString("tipoLesion"));
				lineaDatos.add(resultadosConsulta.getString("profundidadlesion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("cabeza")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("manos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tronco")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("extresuperiores")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("extreinferiores")));
				lineaDatos.add(resultadosConsulta.getString("tipoExposicion"));
				lineaDatos.add(resultadosConsulta.getString("fechaAgresion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("sueroAntirrabico")));
				lineaDatos.add(resultadosConsulta.getString("fechaAplicacion"));
				lineaDatos.add(resultadosConsulta.getString("vacunaAntirrabica"));
				lineaDatos.add(resultadosConsulta.getString("dosisAplicadas"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("lavadoherida")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("suturaherida")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("aplicacionsuero")));
				lineaDatos.add(resultadosConsulta.getString("fechaaplicacionsuero"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("aplicarvacuna")));
				lineaDatos.add(resultadosConsulta.getString("fechavacunadosis1"));
				lineaDatos.add(resultadosConsulta.getString("fechavacunadosis2"));
				lineaDatos.add(resultadosConsulta.getString("fechavacunadosis3"));
				lineaDatos.add(resultadosConsulta.getString("fechavacunadosis4"));
				lineaDatos.add(resultadosConsulta.getString("fechavacunadosis5"));
				lineaDatos.add(resultadosConsulta.getString("suspensiontratamiento"));
				lineaDatos.add(resultadosConsulta.getString("reaccionesvacunasuero"));
				lineaDatos.add(resultadosConsulta.getString("evolucionpaciente"));
				lineaDatos.add(resultadosConsulta.getString("especie"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunado")));
				lineaDatos.add(resultadosConsulta.getString("fechaultimadosisanimal"));
				lineaDatos.add(resultadosConsulta.getString("nombrepropietario"));
				lineaDatos.add(resultadosConsulta.getString("direccionpropietario"));
				lineaDatos.add(resultadosConsulta.getString("estadomomentoagresion"));
				lineaDatos.add(resultadosConsulta.getString("ubicacionanimal"));
				lineaDatos.add(resultadosConsulta.getString("numerodiasobserva"));
				lineaDatos.add(resultadosConsulta.getString("lugarobservacion"));
				
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	

	public static Vector generarVectorSarampion(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				String lugarParto = resultadosConsulta.getString("departamentoParto") + resultadosConsulta.getString("municipioParto");
				String lugarViaje = resultadosConsulta.getString("departamentoViaje") + resultadosConsulta.getString("municipioViaje");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombrePadre"));
				lineaDatos.add(resultadosConsulta.getString("ocupacionPadre"));
				lineaDatos.add(resultadosConsulta.getString("fechaVisita1"));
				lineaDatos.add(resultadosConsulta.getString("fuenteNotificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaSarampion")));
				lineaDatos.add(resultadosConsulta.getString("numeroDosisSarampion"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosisSarampion"));
				lineaDatos.add(resultadosConsulta.getString("fuenteDatosSarampion"));
				lineaDatos.add(resultadosConsulta.getString("vacunaRubeola"));
				lineaDatos.add(resultadosConsulta.getString("numeroDosisRubeola"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosisRubeola"));
				lineaDatos.add(resultadosConsulta.getString("fuenteDatosRubeola"));
				lineaDatos.add(resultadosConsulta.getString("fechaVisitaDomiciliaria"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(resultadosConsulta.getString("fechaInicioFiebre"));
				lineaDatos.add(resultadosConsulta.getString("tipoErupcion"));
				lineaDatos.add(resultadosConsulta.getString("fechaInicioErupcion"));
				lineaDatos.add(resultadosConsulta.getString("duracionErupcion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("coriza")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("conjuntivitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("adenopatia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("artralgia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("embarazada")));
				lineaDatos.add(resultadosConsulta.getString("numeroSemanas"));
				lineaDatos.add(lugarParto);
				lineaDatos.add(resultadosConsulta.getString("huboContacto"));
				lineaDatos.add(resultadosConsulta.getString("huboCasoConfirmado"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("huboViaje")));
				lineaDatos.add(lugarViaje);
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("huboContactoEmbarazada")));
				lineaDatos.add(resultadosConsulta.getString("diagnosticoFinal"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorVIH(ResultSet resultadosConsulta, HashMap mecanismos, HashMap enfermedades, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(mecanismos.get("mecanismo_1"));
				lineaDatos.add(mecanismos.get("mecanismo_2"));
				lineaDatos.add(mecanismos.get("mecanismo_3"));
				lineaDatos.add(mecanismos.get("mecanismo_4"));
				lineaDatos.add(mecanismos.get("mecanismo_5"));
				lineaDatos.add(mecanismos.get("mecanismo_6"));
				lineaDatos.add(mecanismos.get("mecanismo_7"));
				lineaDatos.add(mecanismos.get("mecanismo_8"));
				lineaDatos.add(mecanismos.get("mecanismo_9"));
				lineaDatos.add(resultadosConsulta.getString("tipoMuestra"));
				lineaDatos.add(resultadosConsulta.getString("tipoPrueba"));
				lineaDatos.add(resultadosConsulta.getString("resultado"));
				lineaDatos.add(resultadosConsulta.getString("fechaResultado"));
				lineaDatos.add(resultadosConsulta.getString("valor"));
				lineaDatos.add(resultadosConsulta.getString("estadioClinico"));
				lineaDatos.add(resultadosConsulta.getString("numeroHijos"));
				lineaDatos.add(resultadosConsulta.getString("numeroHijas"));
				lineaDatos.add(resultadosConsulta.getString("embarazo"));
				lineaDatos.add(resultadosConsulta.getString("numeroSemanas"));
				lineaDatos.add(enfermedades.get("enf_1"));
				lineaDatos.add(enfermedades.get("enf_2"));
				lineaDatos.add(enfermedades.get("enf_3"));
				lineaDatos.add(enfermedades.get("enf_4"));
				lineaDatos.add(enfermedades.get("enf_5"));
				lineaDatos.add(enfermedades.get("enf_6"));
				lineaDatos.add(enfermedades.get("enf_7"));
				lineaDatos.add(enfermedades.get("enf_8"));
				lineaDatos.add(enfermedades.get("enf_9"));
				lineaDatos.add(enfermedades.get("enf_10"));
				lineaDatos.add(enfermedades.get("enf_11"));
				lineaDatos.add(enfermedades.get("enf_12"));
				lineaDatos.add(enfermedades.get("enf_13"));
				lineaDatos.add(enfermedades.get("enf_14"));
				lineaDatos.add(enfermedades.get("enf_15"));
				lineaDatos.add(enfermedades.get("enf_16"));
				lineaDatos.add(enfermedades.get("enf_17"));
				lineaDatos.add(enfermedades.get("enf_18"));
				lineaDatos.add(enfermedades.get("enf_19"));
				lineaDatos.add(enfermedades.get("enf_20"));
				lineaDatos.add(enfermedades.get("enf_21"));
				lineaDatos.add(enfermedades.get("enf_22"));
				lineaDatos.add(enfermedades.get("enf_23"));
				lineaDatos.add(enfermedades.get("enf_24"));
				lineaDatos.add(enfermedades.get("enf_25"));
				lineaDatos.add(enfermedades.get("enf_26"));
				lineaDatos.add(enfermedades.get("enf_27"));
				
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	

	public static Vector generarVectorOfidico(ResultSet resultadosConsulta, HashMap maniLocales, HashMap maniSistemica, HashMap compliLocales, HashMap compliSistemica, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				String diasHoras = resultadosConsulta.getString("diasTranscurridos") + ":" + resultadosConsulta.getString("horasTranscurridas");
				String horasMinutos = resultadosConsulta.getString("horasSuero") + ":" + resultadosConsulta.getString("minutosSuero");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("fechaAccidente"));
				lineaDatos.add(resultadosConsulta.getString("nombreVereda"));
				lineaDatos.add(resultadosConsulta.getString("tipoAtencionInicial"));
				lineaDatos.add(resultadosConsulta.getString("practicasNoMedicas"));
				lineaDatos.add(resultadosConsulta.getString("localizacionMordedura"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("huellasColmillos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("serpienteIdentificada")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("serpienteCapturada")));
				lineaDatos.add(resultadosConsulta.getString("generoAgenteAgresor"));
				lineaDatos.add(resultadosConsulta.getString("nombreAgenteAgresor"));
				lineaDatos.add(maniLocales.get("mani_1"));
				lineaDatos.add(maniLocales.get("mani_2"));
				lineaDatos.add(maniLocales.get("mani_3"));
				lineaDatos.add(maniLocales.get("mani_4"));
				lineaDatos.add(maniLocales.get("mani_5"));
				lineaDatos.add(maniLocales.get("mani_6"));
				lineaDatos.add(maniLocales.get("mani_7"));
				lineaDatos.add(maniLocales.get("mani_8"));
				lineaDatos.add(resultadosConsulta.getString("cualLocal"));
				lineaDatos.add(maniSistemica.get("mani_1"));
				lineaDatos.add(maniSistemica.get("mani_2"));
				lineaDatos.add(maniSistemica.get("mani_3"));
				lineaDatos.add(maniSistemica.get("mani_4"));
				lineaDatos.add(maniSistemica.get("mani_5"));
				lineaDatos.add(maniSistemica.get("mani_6"));
				lineaDatos.add(maniSistemica.get("mani_7"));
				lineaDatos.add(maniSistemica.get("mani_8"));
				lineaDatos.add(maniSistemica.get("mani_9"));
				lineaDatos.add(maniSistemica.get("mani_10"));
				lineaDatos.add(maniSistemica.get("mani_11"));
				lineaDatos.add(maniSistemica.get("mani_12"));
				lineaDatos.add(maniSistemica.get("mani_13"));
				lineaDatos.add(maniSistemica.get("mani_14"));
				lineaDatos.add(compliLocales.get("compli_1"));
				lineaDatos.add(compliLocales.get("compli_2"));
				lineaDatos.add(compliLocales.get("compli_3"));
				lineaDatos.add(compliLocales.get("compli_4"));
				lineaDatos.add(compliLocales.get("compli_5"));
				lineaDatos.add(compliLocales.get("compli_6"));
				lineaDatos.add(compliSistemica.get("compli_1"));
				lineaDatos.add(compliSistemica.get("compli_2"));
				lineaDatos.add(compliSistemica.get("compli_3"));
				lineaDatos.add(compliSistemica.get("compli_4"));
				lineaDatos.add(compliSistemica.get("compli_5"));
				lineaDatos.add(compliSistemica.get("compli_6"));
				lineaDatos.add(resultadosConsulta.getString("severidadAccidente"));
				lineaDatos.add(resultadosConsulta.getString("empleoSuero"));
				lineaDatos.add(diasHoras);
				lineaDatos.add(resultadosConsulta.getString("tipoSueroAntiofidico"));
				lineaDatos.add(resultadosConsulta.getString("dosisSuero"));
				lineaDatos.add(horasMinutos);
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tratamientoQuirurgico")));
				lineaDatos.add(resultadosConsulta.getString("tipoTratamiento"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorMortalidad(ResultSet resultadosConsulta, HashMap antecedentes, HashMap complicaciones, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("sitioDefuncion"));
				lineaDatos.add(resultadosConsulta.getString("convivencia"));
				lineaDatos.add(resultadosConsulta.getString("otroConvivencia"));
				lineaDatos.add(resultadosConsulta.getString("escolaridad"));
				lineaDatos.add(resultadosConsulta.getString("fecundidad"));
				lineaDatos.add(resultadosConsulta.getString("gestaciones"));
				lineaDatos.add(resultadosConsulta.getString("partos"));
				lineaDatos.add(resultadosConsulta.getString("cesareas"));
				lineaDatos.add(resultadosConsulta.getString("abortos"));
				lineaDatos.add(resultadosConsulta.getString("muertos"));
				lineaDatos.add(resultadosConsulta.getString("vivos"));
				lineaDatos.add(antecedentes.get("antecedente_1"));
				lineaDatos.add(antecedentes.get("antecedente_2"));
				lineaDatos.add(antecedentes.get("antecedente_3"));
				lineaDatos.add(antecedentes.get("antecedente_4"));
				lineaDatos.add(antecedentes.get("antecedente_5"));
				lineaDatos.add(antecedentes.get("antecedente_6"));
				lineaDatos.add(antecedentes.get("antecedente_7"));
				lineaDatos.add(antecedentes.get("antecedente_8"));
				lineaDatos.add(antecedentes.get("antecedente_9"));
				lineaDatos.add(antecedentes.get("antecedente_10"));
				lineaDatos.add(antecedentes.get("antecedente_11"));
				lineaDatos.add(antecedentes.get("antecedente_12"));
				lineaDatos.add(antecedentes.get("antecedente_13"));
				lineaDatos.add(antecedentes.get("antecedente_14"));
				lineaDatos.add(antecedentes.get("antecedente_15"));
				lineaDatos.add(antecedentes.get("antecedente_16"));
				lineaDatos.add(antecedentes.get("antecedente_17"));
				lineaDatos.add(antecedentes.get("antecedente_18"));
				lineaDatos.add(antecedentes.get("antecedente_19"));
				lineaDatos.add(antecedentes.get("antecedente_20"));
				lineaDatos.add(complicaciones.get("complicacion_1"));
				lineaDatos.add(complicaciones.get("complicacion_2"));
				lineaDatos.add(complicaciones.get("complicacion_3"));
				lineaDatos.add(complicaciones.get("complicacion_4"));
				lineaDatos.add(complicaciones.get("complicacion_5"));
				lineaDatos.add(complicaciones.get("complicacion_6"));
				lineaDatos.add(complicaciones.get("complicacion_7"));
				lineaDatos.add(complicaciones.get("complicacion_8"));
				lineaDatos.add(complicaciones.get("complicacion_9"));
				lineaDatos.add(complicaciones.get("complicacion_10"));
				lineaDatos.add(complicaciones.get("complicacion_11"));
				lineaDatos.add(complicaciones.get("complicacion_12"));
				lineaDatos.add(complicaciones.get("complicacion_13"));
				lineaDatos.add(complicaciones.get("complicacion_14"));
				lineaDatos.add(complicaciones.get("complicacion_15"));
				lineaDatos.add(resultadosConsulta.getString("infecciones"));
				lineaDatos.add(resultadosConsulta.getString("factoresRiesgo"));
				lineaDatos.add(resultadosConsulta.getString("cuantosControles"));
				lineaDatos.add(resultadosConsulta.getString("semanaInicioCpn"));
				lineaDatos.add(resultadosConsulta.getString("controlesRealizadosPor"));
				lineaDatos.add(resultadosConsulta.getString("nivelAtencion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("clasificacionRiesgo")));
				lineaDatos.add(resultadosConsulta.getString("quienClasificoRiesgo"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("remisionesOportunas")));
				lineaDatos.add(resultadosConsulta.getString("complicaciones"));
				lineaDatos.add(resultadosConsulta.getString("momentoFallecimiento"));
				lineaDatos.add(resultadosConsulta.getString("semanasGestacion"));
				lineaDatos.add(resultadosConsulta.getString("tipoParto"));
				lineaDatos.add(resultadosConsulta.getString("atendidoPor"));
				lineaDatos.add(resultadosConsulta.getString("nivelAtencion2"));
				lineaDatos.add(resultadosConsulta.getString("momentoMuerteRelacion"));
				lineaDatos.add(resultadosConsulta.getString("edadGestacional"));
				lineaDatos.add(resultadosConsulta.getString("pesoNacimiento"));
				lineaDatos.add(resultadosConsulta.getString("tallaNacimiento"));
				lineaDatos.add(resultadosConsulta.getString("apgarNacimiento1"));
				lineaDatos.add(resultadosConsulta.getString("apgarNacimiento5"));
				lineaDatos.add(resultadosConsulta.getString("nivelAtencion3"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("remisionOportunaComplica")));
				lineaDatos.add(resultadosConsulta.getString("adaptacionNeonatal"));
				lineaDatos.add(resultadosConsulta.getString("causaBasicaDefuncion"));
				lineaDatos.add(resultadosConsulta.getString("muerteDemora"));
				lineaDatos.add(resultadosConsulta.getString("causaMuerteDet"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	

	public static Vector generarVectorRubCongenita(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("clasificacionInicial"));
				lineaDatos.add(resultadosConsulta.getString("nombreTutor"));
				lineaDatos.add(resultadosConsulta.getString("lugarNacimientoPaciente"));
				lineaDatos.add(resultadosConsulta.getString("fuenteNotificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombreMadre"));
				lineaDatos.add(resultadosConsulta.getString("edadMadre"));
				lineaDatos.add(resultadosConsulta.getString("embarazos"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("carneVacunacion")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaRubeola")));
				lineaDatos.add(resultadosConsulta.getString("numeroDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("rubeolaConfirmada")));
				lineaDatos.add(resultadosConsulta.getString("semanasEmbarazo"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("similarRubeola")));
				lineaDatos.add(resultadosConsulta.getString("semanasEmbarazo2"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("expuestaRubeola")));
				lineaDatos.add(resultadosConsulta.getString("semanasEmbarazo3"));
				lineaDatos.add(resultadosConsulta.getString("donde"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("viajes")));
				lineaDatos.add(resultadosConsulta.getString("semanasEmbarazo4"));
				lineaDatos.add(resultadosConsulta.getString("dondeViajo"));
				lineaDatos.add(resultadosConsulta.getString("apgar"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("bajoPesoNacimiento")));
				lineaDatos.add(resultadosConsulta.getString("peso"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("pequenoEdadGesta")));
				lineaDatos.add(resultadosConsulta.getString("semanasEdad"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("cataratas")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("glaucoma")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("retinopatia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("otrosOjo")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("arterioso")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("estenosis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("otrosCorazon")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("sordera")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("otrosOido")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("microCefalia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("sicomotor")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("purpura")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("hepatomegalia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("ictericia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("esplenomegalia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("osteopatia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("meningoencefalitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("otrosGeneral")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("examenesEspeciales")));
				lineaDatos.add(resultadosConsulta.getString("examen"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("anatomoPatologico")));
				lineaDatos.add(resultadosConsulta.getString("examen2"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("compatibleSrc")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("dxFinal")));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorSifilis(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("controlPrenatal")));
				lineaDatos.add(resultadosConsulta.getString("edadGestacional"));
				lineaDatos.add(resultadosConsulta.getString("edadGestacionalSero1"));
				lineaDatos.add(resultadosConsulta.getString("edadGestacionalTrat"));
				lineaDatos.add(resultadosConsulta.getString("condicionmomentodx"));
				lineaDatos.add(resultadosConsulta.getString("lugarAtencionParto"));
				lineaDatos.add(resultadosConsulta.getString("estadoNacimiento"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("recibioTratamiento")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tipoTratamiento")));
				lineaDatos.add(resultadosConsulta.getString("medicamentoAdmin"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("esquemaCompleto")));
				lineaDatos.add(resultadosConsulta.getString("otrasIts"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("alergiaPenicilina")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("desensibilizaPenicilina")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("diagnosticoContactos")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("tratamientoContactos")));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorTetanos(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				String lugarVivienda = resultadosConsulta.getString("codigoDepVivienda") + resultadosConsulta.getString("codigoMunicipioVivienda");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombreMadre"));
				lineaDatos.add(resultadosConsulta.getString("edadMadre"));
				lineaDatos.add(resultadosConsulta.getString("fechaNacimiento"));
				lineaDatos.add(resultadosConsulta.getString("fechaEgresoHospital"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("nacimientoTraumatico")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("llantoNacer")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("mamabaNormal")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("dejoMamar")));
				lineaDatos.add(resultadosConsulta.getString("fechaDejo"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("dificultadRespiratoria")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("episodiosApnea")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("hipotermia")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("hipertermia")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("fontAbombada")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("rigidezNuca")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("trismus")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("convulsiones")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("espasmos")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("contracciones")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("opistotonos")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("llantoExcesivo")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("sepsisUmbilical")));
				lineaDatos.add(resultadosConsulta.getString("numeroEmbarazos"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("asistioControl")));
				lineaDatos.add(resultadosConsulta.getString("explicacionNoAsistencia"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("atendidoPorMedico")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("atendidoPorEnfermero")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("atendidoPorAuxiliar")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("atendidoPorPromotor")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("atendidoPorOtro")));
				lineaDatos.add(resultadosConsulta.getString("quienAtendio"));
				lineaDatos.add(resultadosConsulta.getString("numeroControlesPrevios"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimoControl"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("madreVivioMismoLugar")));
				lineaDatos.add(lugarVivienda);
				lineaDatos.add(bool2num(resultadosConsulta.getString("antecedenteVacunaAnti")));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt"));
				lineaDatos.add(resultadosConsulta.getString("explicacionNoVacuna"));
				lineaDatos.add(resultadosConsulta.getString("fechaDosisTd1"));
				lineaDatos.add(resultadosConsulta.getString("fechaDosisTd2"));
				lineaDatos.add(resultadosConsulta.getString("fechaDosisTd3"));
				lineaDatos.add(resultadosConsulta.getString("fechaDosisTd4"));
				lineaDatos.add(resultadosConsulta.getString("lugarParto"));
				lineaDatos.add(resultadosConsulta.getString("institucionParto"));
				lineaDatos.add(resultadosConsulta.getString("fechaIngresoParto"));
				lineaDatos.add(resultadosConsulta.getString("fechaEgresoParto"));
				lineaDatos.add(resultadosConsulta.getString("quienAtendioParto"));
				lineaDatos.add(resultadosConsulta.getString("instrumentoCordon"));
				lineaDatos.add(resultadosConsulta.getString("metodoEsterilizacion"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("recibioInformacionMunon")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("aplicacionSustanciasMunon")));
				lineaDatos.add(resultadosConsulta.getString("cualesSustancias"));
				lineaDatos.add(resultadosConsulta.getString("distanciaMinutos"));
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacionCampo"));
				lineaDatos.add(resultadosConsulta.getString("fechaVacunacion"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd1AplicadasMef"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd1AplicadasGest"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd2AplicadasMef"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd2AplicadasGest"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd3AplicadasMef"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd3AplicadasGest"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd4AplicadasMef"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd4AplicadasGest"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd5AplicadasMef"));
				lineaDatos.add(resultadosConsulta.getString("dosisTd5AplicadasGest"));
				lineaDatos.add(resultadosConsulta.getString("coberturaLograda"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	

	public static Vector generarVectorTuberculosis(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("baciloscopia")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("cultivo")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("histopatologia")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("clinicapaciente")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("nexoepidemiologico")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("radiologico")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("ada")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("otroDx")));
				lineaDatos.add(resultadosConsulta.getString("bk"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("realizoCultivo")));
				lineaDatos.add(resultadosConsulta.getString("tipotuberculosis"));
				lineaDatos.add(resultadosConsulta.getString("otroTipoTuberculosis"));
				lineaDatos.add(bool2num(resultadosConsulta.getString("cicatrizvacuna")));
				lineaDatos.add(resultadosConsulta.getString("fuentecontagio"));
				lineaDatos.add(resultadosConsulta.getString("metodohallazgo"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("asociacionvih")));
				lineaDatos.add(bool2num(resultadosConsulta.getString("asesoriavih")));

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorDifteria(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombrePadre"));
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacion"));
				lineaDatos.add(resultadosConsulta.getString("casoIdentificadoPor"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("contactoCasoConfirmado")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("carneVacunacion")));
				lineaDatos.add(resultadosConsulta.getString("dosisAplicadas"));
				lineaDatos.add(resultadosConsulta.getString("tipoVacuna"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("amigdalitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("faringitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("laringitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("membranas")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("complicaciones")));
				lineaDatos.add(resultadosConsulta.getString("tipoComplicacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tratAntibiotico")));
				lineaDatos.add(resultadosConsulta.getString("tipoAntibiotico"));
				lineaDatos.add(resultadosConsulta.getString("duracionTratamiento"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("antitoxina")));
				lineaDatos.add(resultadosConsulta.getString("dosisAntitoxina"));
				lineaDatos.add(resultadosConsulta.getString("fechaAplicacionAntitox"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("investigacionCampo")));
				lineaDatos.add(resultadosConsulta.getString("fechaOperacionBarrido"));
				lineaDatos.add(resultadosConsulta.getString("numeroContactos"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("quimioprofilaxis")));
				lineaDatos.add(resultadosConsulta.getString("poblacionGrupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("poblacionGrupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("poblacionGrupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo3"));
				
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorEasv(ResultSet resultadosConsulta, HashMap vacunas, HashMap hallazgos, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				String lugarVacunacion = resultadosConsulta.getString("codDepVacunacion") + resultadosConsulta.getString("codMunVacunacion");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(vacunas.get("vacuna_1"));
				lineaDatos.add(vacunas.get("dosis_1"));
				lineaDatos.add(vacunas.get("via_1"));
				lineaDatos.add(vacunas.get("sitio_1"));
				lineaDatos.add(vacunas.get("fecha_1"));
				lineaDatos.add(vacunas.get("vacuna_2"));
				lineaDatos.add(vacunas.get("dosis_2"));
				lineaDatos.add(vacunas.get("via_2"));
				lineaDatos.add(vacunas.get("sitio_2"));
				lineaDatos.add(vacunas.get("fecha_2"));
				lineaDatos.add(vacunas.get("vacuna_3"));
				lineaDatos.add(vacunas.get("dosis_3"));
				lineaDatos.add(vacunas.get("via_3"));
				lineaDatos.add(vacunas.get("sitio_3"));
				lineaDatos.add(vacunas.get("fecha_3"));
				lineaDatos.add(vacunas.get("vacuna_4"));
				lineaDatos.add(vacunas.get("dosis_4"));
				lineaDatos.add(vacunas.get("via_4"));
				lineaDatos.add(vacunas.get("sitio_4"));
				lineaDatos.add(vacunas.get("fecha_4"));
				lineaDatos.add(hallazgos.get("hallazgo_1"));
				lineaDatos.add(hallazgos.get("hallazgo_2"));
				lineaDatos.add(hallazgos.get("hallazgo_3"));
				lineaDatos.add(hallazgos.get("hallazgo_4"));
				lineaDatos.add(hallazgos.get("hallazgo_5"));
				lineaDatos.add(hallazgos.get("hallazgo_6"));
				lineaDatos.add(hallazgos.get("hallazgo_7"));
				lineaDatos.add(hallazgos.get("hallazgo_8"));
				lineaDatos.add(hallazgos.get("hallazgo_9"));
				lineaDatos.add(hallazgos.get("hallazgo_10"));
				lineaDatos.add(hallazgos.get("hallazgo_11"));
				lineaDatos.add(hallazgos.get("hallazgo_12"));
				lineaDatos.add(hallazgos.get("hallazgo_13"));
				lineaDatos.add(hallazgos.get("hallazgo_14"));
				lineaDatos.add(hallazgos.get("hallazgo_15"));
				lineaDatos.add(hallazgos.get("hallazgo_16"));
				lineaDatos.add(resultadosConsulta.getString("otroHallazgo"));
				lineaDatos.add(resultadosConsulta.getString("tiempo"));
				lineaDatos.add(resultadosConsulta.getString("unidadTiempo"));
				lineaDatos.add(resultadosConsulta.getString("lugarVacunacion"));
				lineaDatos.add(lugarVacunacion);
				lineaDatos.add(resultadosConsulta.getString("estadoSalud"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("recibiaMedicamentos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("antPatologicos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("antAlergicos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("antEasv")));
				lineaDatos.add(resultadosConsulta.getString("biologico1"));
				lineaDatos.add(resultadosConsulta.getString("fabricanteMuestra1"));
				lineaDatos.add(resultadosConsulta.getString("loteMuestra1"));
				lineaDatos.add(resultadosConsulta.getString("biologico2"));
				lineaDatos.add(resultadosConsulta.getString("fabricanteMuestra2"));
				lineaDatos.add(resultadosConsulta.getString("loteMuestra2"));
				lineaDatos.add(resultadosConsulta.getString("estadoFinal"));
				
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorHepatitis(ResultSet resultadosConsulta, HashMap poblacion, HashMap sintomas, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("embarazada")));
				lineaDatos.add(resultadosConsulta.getString("edadGestacional"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("controlPrenatal")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("donanteSangre")));
				lineaDatos.add(poblacion.get("poblacion_1"));
				lineaDatos.add(poblacion.get("poblacion_2"));
				lineaDatos.add(poblacion.get("poblacion_3"));
				lineaDatos.add(poblacion.get("poblacion_4"));
				lineaDatos.add(poblacion.get("poblacion_5"));
				lineaDatos.add(poblacion.get("poblacion_6"));
				lineaDatos.add(poblacion.get("poblacion_7"));
				lineaDatos.add(poblacion.get("poblacion_8"));
				lineaDatos.add(poblacion.get("poblacion_9"));
				lineaDatos.add(poblacion.get("poblacion_10"));
				lineaDatos.add(poblacion.get("poblacion_11"));
				lineaDatos.add(resultadosConsulta.getString("modoTransmision"));
				lineaDatos.add(resultadosConsulta.getString("otrasIts"));
				lineaDatos.add(resultadosConsulta.getString("vacunaAntihepatitis"));
				lineaDatos.add(resultadosConsulta.getString("numeroDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaPrimeraDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("fuenteInformacion")));
				lineaDatos.add(sintomas.get("sintoma_1"));
				lineaDatos.add(sintomas.get("sintoma_2"));
				lineaDatos.add(sintomas.get("sintoma_3"));
				lineaDatos.add(sintomas.get("sintoma_4"));
				lineaDatos.add(sintomas.get("sintoma_5"));
				lineaDatos.add(sintomas.get("sintoma_6"));
				lineaDatos.add(sintomas.get("sintoma_7"));
				lineaDatos.add(sintomas.get("sintoma_8"));
				lineaDatos.add(sintomas.get("sintoma_9"));
				lineaDatos.add(sintomas.get("sintoma_10"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tratamiento")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("complicacion")));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorLeishmaniasis(ResultSet resultadosConsulta, HashMap lesiones, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("numeroLesiones"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("cara")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tronco")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("superiores")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("inferiores")));
				lineaDatos.add(lesiones.get("lesion_1"));
				lineaDatos.add(lesiones.get("lesion_2"));
				lineaDatos.add(lesiones.get("lesion_3"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("cicatrices")));
				lineaDatos.add(resultadosConsulta.getString("tiempo"));
				lineaDatos.add(resultadosConsulta.getString("unidadTiempo"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("antecedenteTrauma")));
				lineaDatos.add(resultadosConsulta.getString("mucosaAfectada"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("rinorrea")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("epistaxis")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("obstruccion")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("disfonia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("disfagia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("hiperemia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("ulceracion")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("perforacion")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("destruccion")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("hepatomegalia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("esplenomegalia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("anemia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("leucopenia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("trombocitopenia")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("recibioTratamiento")));
				lineaDatos.add(resultadosConsulta.getString("numeroVeces"));
				lineaDatos.add(resultadosConsulta.getString("medicamentoRecibio"));
				lineaDatos.add(resultadosConsulta.getString("otroMedicamento"));
				lineaDatos.add(resultadosConsulta.getString("pesoPaciente"));
				lineaDatos.add(resultadosConsulta.getString("volumenDiario"));
				lineaDatos.add(resultadosConsulta.getString("diasTratamiento"));
				lineaDatos.add(resultadosConsulta.getString("totalAmpollas"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	

	public static Vector generarVectorMalaria(ResultSet resultadosConsulta, HashMap sintomas, HashMap tratamientos, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				String lugarViajo = resultadosConsulta.getString("codDepViajo") + resultadosConsulta.getString("codMunViajo");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("viajo")));
				lineaDatos.add(lugarViajo);
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("padecioMalaria")));
				lineaDatos.add(resultadosConsulta.getString("fechaAproximada"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("automedicacion")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("antecedenteTrans")));
				lineaDatos.add(resultadosConsulta.getString("fechaAntecedente"));
				lineaDatos.add(resultadosConsulta.getString("tipoComplicacion"));
				lineaDatos.add(sintomas.get("sintoma_1"));
				lineaDatos.add(sintomas.get("sintoma_2"));
				lineaDatos.add(sintomas.get("sintoma_3"));
				lineaDatos.add(sintomas.get("sintoma_4"));
				lineaDatos.add(sintomas.get("sintoma_5"));
				lineaDatos.add(sintomas.get("sintoma_6"));
				lineaDatos.add(sintomas.get("sintoma_7"));
				lineaDatos.add(sintomas.get("sintoma_8"));
				lineaDatos.add(sintomas.get("sintoma_9"));
				lineaDatos.add(sintomas.get("sintoma_10"));
				lineaDatos.add(sintomas.get("sintoma_11"));
				lineaDatos.add(sintomas.get("sintoma_12"));
				lineaDatos.add(sintomas.get("sintoma_13"));
				lineaDatos.add(sintomas.get("sintoma_14"));
				lineaDatos.add(sintomas.get("sintoma_15"));
				lineaDatos.add(sintomas.get("sintoma_16"));
				lineaDatos.add(sintomas.get("sintoma_17"));
				lineaDatos.add(sintomas.get("sintoma_18"));
				lineaDatos.add(sintomas.get("sintoma_19"));
				lineaDatos.add(sintomas.get("sintoma_20"));
				lineaDatos.add(sintomas.get("sintoma_21"));
				lineaDatos.add(sintomas.get("sintoma_22"));
				lineaDatos.add(sintomas.get("sintoma_23"));
				lineaDatos.add(sintomas.get("sintoma_24"));
				lineaDatos.add(sintomas.get("sintoma_25"));
				lineaDatos.add(resultadosConsulta.getString("especiePlasmodium"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("embarazo")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tratAntimalarico")));
				lineaDatos.add(tratamientos.get("tratamiento_1"));
				lineaDatos.add(tratamientos.get("tratamiento_2"));
				lineaDatos.add(tratamientos.get("tratamiento_3"));
				lineaDatos.add(tratamientos.get("tratamiento_4"));
				lineaDatos.add(tratamientos.get("tratamiento_5"));
				lineaDatos.add(tratamientos.get("tratamiento_6"));
				lineaDatos.add(tratamientos.get("tratamiento_7"));
				lineaDatos.add(tratamientos.get("tratamiento_8"));
				lineaDatos.add(tratamientos.get("tratamiento_9"));
				lineaDatos.add(tratamientos.get("tratamiento_10"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorMeningitis(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaAntihib")));
				lineaDatos.add(resultadosConsulta.getString("dosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaPrimeraDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tieneCarne")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaAntimenin")));
				lineaDatos.add(resultadosConsulta.getString("dosis2"));
				lineaDatos.add(resultadosConsulta.getString("fechaPrimeraDosis2"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis2"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tieneCarne2")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaAntineumo")));
				lineaDatos.add(resultadosConsulta.getString("dosis3"));
				lineaDatos.add(resultadosConsulta.getString("fechaPrimeraDosis3"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis3"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tieneCarne3")));
				lineaDatos.add(resultadosConsulta.getString("fiebre"));
				lineaDatos.add(resultadosConsulta.getString("rigidez"));
				lineaDatos.add(resultadosConsulta.getString("irritacion"));
				lineaDatos.add(resultadosConsulta.getString("rash"));
				lineaDatos.add(resultadosConsulta.getString("abombamiento"));
				lineaDatos.add(resultadosConsulta.getString("alteracion"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("usoAntibioticos")));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis4"));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorEsi(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				String lugarViajo = resultadosConsulta.getString("codDepViajo") + resultadosConsulta.getString("codMunViajo");
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("clasificacionInicial"));
				lineaDatos.add(resultadosConsulta.getString("lugarTrabajo"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vacunaEstacional")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("carneVacunacion")));
				lineaDatos.add(resultadosConsulta.getString("numeroDosis"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(resultadosConsulta.getString("verificacion"));
				lineaDatos.add(resultadosConsulta.getString("fuenteNotificacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("viajo")));
				lineaDatos.add(lugarViajo);
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("contactoAves")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("contactoPersona")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("casoEsporadico")));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("casoEpidemico")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("dolorGarganta")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tos")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("dificultadRespiratoria")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("hipoxia")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("taquipnea")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("rinorrea")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("coriza")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("conjuntivitis")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("cefalea")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("mialgias")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("postracion")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("infiltrados")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("dolorAbdominal")));
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorEtas(ResultSet resultadosConsulta, HashMap sintomas, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(sintomas.get("sintoma_1"));
				lineaDatos.add(sintomas.get("sintoma_2"));
				lineaDatos.add(sintomas.get("sintoma_3"));
				lineaDatos.add(sintomas.get("sintoma_4"));
				lineaDatos.add(sintomas.get("sintoma_5"));
				lineaDatos.add(sintomas.get("sintoma_6"));
				lineaDatos.add(sintomas.get("sintoma_7"));
				lineaDatos.add(sintomas.get("sintoma_8"));
				lineaDatos.add(sintomas.get("sintoma_9"));
				lineaDatos.add(sintomas.get("sintoma_10"));
				lineaDatos.add(sintomas.get("sintoma_11"));
				lineaDatos.add(sintomas.get("sintoma_12"));
				lineaDatos.add(sintomas.get("sintoma_13"));
				lineaDatos.add(sintomas.get("sintoma_14"));
				lineaDatos.add(sintomas.get("sintoma_15"));
				lineaDatos.add(sintomas.get("sintoma_16"));
				lineaDatos.add(sintomas.get("sintoma_17"));
				lineaDatos.add(resultadosConsulta.getString("otroSintoma"));
				lineaDatos.add(resultadosConsulta.getString("horaInicioSintomas"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("asociadoBrote")));
				lineaDatos.add(resultadosConsulta.getString("captadoPor"));
				lineaDatos.add(resultadosConsulta.getString("relacionExposicion"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("tomoMuestra")));
				lineaDatos.add(resultadosConsulta.getString("tipoMuestra"));
				lineaDatos.add(resultadosConsulta.getString("agente1"));
				lineaDatos.add(resultadosConsulta.getString("agente2"));
				lineaDatos.add(resultadosConsulta.getString("agente3"));
				lineaDatos.add(resultadosConsulta.getString("agente4"));

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorTosferina(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(resultadosConsulta.getString("nombrePadre"));
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacion"));
				lineaDatos.add(resultadosConsulta.getString("identificacionCaso"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("contactoCaso")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("carneVacunacion")));
				lineaDatos.add(resultadosConsulta.getString("dosisAplicadas"));
				lineaDatos.add(resultadosConsulta.getString("tipoVacuna"));
				lineaDatos.add(resultadosConsulta.getString("fechaUltimaDosis"));
				lineaDatos.add(resultadosConsulta.getString("etapaEnfermedad"));
				lineaDatos.add(resultadosConsulta.getString("tos"));
				lineaDatos.add(resultadosConsulta.getString("duracionTos"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tosParoxistica")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("estridor")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("apnea")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("fiebre")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("vomitoPostusivo")));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("complicaciones")));
				lineaDatos.add(resultadosConsulta.getString("tipoComplicacion"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("tratamientoAntibiotico")));
				lineaDatos.add(resultadosConsulta.getString("tipoAntibiotico"));
				lineaDatos.add(resultadosConsulta.getString("duracionTratamiento"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("investigacionCampo")));
				lineaDatos.add(resultadosConsulta.getString("fechaOperacionBarrido"));
				lineaDatos.add(resultadosConsulta.getString("numeroContactos"));
				lineaDatos.add(valorOpcionBasica(resultadosConsulta.getInt("quimioprofilaxis")));
				lineaDatos.add(resultadosConsulta.getString("totalPoblacion1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo1"));
				lineaDatos.add(resultadosConsulta.getString("totalPoblacion2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo2"));
				lineaDatos.add(resultadosConsulta.getString("totalPoblacion3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt1Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt2Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisDpt3Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef1Grupo3"));
				lineaDatos.add(resultadosConsulta.getString("dosisRef2Grupo3"));

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	
	

	public static Vector generarVectorBrotes(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				lineaDatos.add(obtenerCodigoBrote(resultadosConsulta.getInt("evento")));
				lineaDatos.add(resultadosConsulta.getString("fechaDiligenciamiento"));
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo1"));
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo2"));
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo3"));
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo4"));
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo5"));
				lineaDatos.add(resultadosConsulta.getString("pacientesgrupo6"));
				lineaDatos.add(resultadosConsulta.getString("probables"));
				lineaDatos.add(resultadosConsulta.getString("confirmadoslaboratorio"));
				lineaDatos.add(resultadosConsulta.getString("confirmadosclinica"));
				lineaDatos.add(resultadosConsulta.getString("confirmadosnexo"));
				lineaDatos.add(resultadosConsulta.getString("hombres"));
				lineaDatos.add(resultadosConsulta.getString("mujeres"));
				lineaDatos.add(resultadosConsulta.getString("vivos"));
				lineaDatos.add(resultadosConsulta.getString("muertos"));
				lineaDatos.add(resultadosConsulta.getString("departamentoprocedencia"));
				lineaDatos.add(resultadosConsulta.getString("municipioprocedencia"));
				
				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	

	public static Vector generarVectorBrotesEtas(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add("350");
				lineaDatos.add(resultadosConsulta.getString("fechaInvestigacion"));
				lineaDatos.add(resultadosConsulta.getString("alimentos"));
				lineaDatos.add(resultadosConsulta.getString("muestrabiologica"));
				lineaDatos.add(resultadosConsulta.getString("agenteBiologica1"));
				lineaDatos.add(resultadosConsulta.getString("agenteBiologica2"));
				lineaDatos.add(resultadosConsulta.getString("agenteBiologica3"));
				lineaDatos.add(resultadosConsulta.getString("agenteBiologica4"));
				lineaDatos.add(resultadosConsulta.getString("muestraalimentos"));
				lineaDatos.add(resultadosConsulta.getString("agenteAlimentos1"));
				lineaDatos.add(resultadosConsulta.getString("agenteAlimentos2"));
				lineaDatos.add(resultadosConsulta.getString("agenteAlimentos3"));
				lineaDatos.add(resultadosConsulta.getString("agenteAlimentos4"));
				lineaDatos.add(resultadosConsulta.getString("muestraSuperficies"));
				lineaDatos.add(resultadosConsulta.getString("agenteSuperficies1"));
				lineaDatos.add(resultadosConsulta.getString("agenteSuperficies2"));
				lineaDatos.add(resultadosConsulta.getString("agenteSuperficies3"));
				lineaDatos.add(resultadosConsulta.getString("agenteSuperficies4"));
				lineaDatos.add(resultadosConsulta.getString("estudioManipuladores"));
				lineaDatos.add(resultadosConsulta.getString("agenteManipuladores1"));
				lineaDatos.add(resultadosConsulta.getString("agenteManipuladores2"));
				lineaDatos.add(resultadosConsulta.getString("agenteManipuladores3"));
				lineaDatos.add(resultadosConsulta.getString("agenteManipuladores4"));
				lineaDatos.add(resultadosConsulta.getString("lugarConsumoImplicado"));
				lineaDatos.add(resultadosConsulta.getString("frio"));
				lineaDatos.add(resultadosConsulta.getString("conservacion"));
				lineaDatos.add(resultadosConsulta.getString("almacenado"));
				lineaDatos.add(resultadosConsulta.getString("coccion"));
				lineaDatos.add(resultadosConsulta.getString("higiene"));
				lineaDatos.add(resultadosConsulta.getString("contaminacion"));
				lineaDatos.add(resultadosConsulta.getString("limpieza"));
				lineaDatos.add(resultadosConsulta.getString("ambiental"));
				lineaDatos.add(resultadosConsulta.getString("fuente"));
				lineaDatos.add(resultadosConsulta.getString("utensilios"));
				lineaDatos.add(resultadosConsulta.getString("adicion"));
				lineaDatos.add(resultadosConsulta.getString("agua"));
				lineaDatos.add(resultadosConsulta.getString("tejido"));
				lineaDatos.add(resultadosConsulta.getString("ingredientes"));
				lineaDatos.add(resultadosConsulta.getString("manipulador"));
				lineaDatos.add(resultadosConsulta.getString("acidificacion"));
				lineaDatos.add(resultadosConsulta.getString("descongelado"));
				lineaDatos.add(resultadosConsulta.getString("enfriado"));
				lineaDatos.add(resultadosConsulta.getString("ninguna"));
				lineaDatos.add(resultadosConsulta.getString("clausura"));
				lineaDatos.add(resultadosConsulta.getString("suspension"));
				lineaDatos.add(resultadosConsulta.getString("congelacion"));
				lineaDatos.add(resultadosConsulta.getString("decomiso"));
				lineaDatos.add(resultadosConsulta.getString("aislamiento"));
				lineaDatos.add(resultadosConsulta.getString("vacunacion"));
				lineaDatos.add(resultadosConsulta.getString("insectos"));
				
				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	

	public static Vector generarVectorLaboratorios(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add("LABORATORIOS");
				lineaDatos.add(resultadosConsulta.getString("fechaToma"));
				lineaDatos.add(resultadosConsulta.getString("fechaRecepcion"));
				lineaDatos.add(resultadosConsulta.getString("muestra"));
				lineaDatos.add(resultadosConsulta.getString("prueba"));
				lineaDatos.add(resultadosConsulta.getString("agente"));
				lineaDatos.add(resultadosConsulta.getString("resultado"));
				lineaDatos.add(resultadosConsulta.getString("fechaResultado"));
				lineaDatos.add(resultadosConsulta.getString("valor"));
				
				
				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	

	public static Vector generarVectorInfoBasica(ResultSet resultadosConsulta, int semana, int anyo)
	{
		Vector elementos = new Vector();
		

		try {
			
			int x=0;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();

				String edad = "";
				String unidadEdad = "";
				int codsexo = resultadosConsulta.getInt("sexo");
				String sexo = "M";
				
				if (codsexo == 2) {
					sexo = "F";
				}
				
				String codRegimen = resultadosConsulta.getString("aseguradora");
				String tipoRegimen = "";
				
				if (codRegimen.equals("C")) {
					
					tipoRegimen = "1";
				}
				else if (codRegimen.equals("S")) {
					
					tipoRegimen = "2";
				}
				else {
					
					tipoRegimen = "4";
				}
				
				
				int etnia = resultadosConsulta.getInt("etnia");
				
				String pertenenciaEtnica = "6";
				
				if (etnia == 4) {
					
					pertenenciaEtnica = "1";
				}
				else if (etnia == 2) {
					
					pertenenciaEtnica = "5";
				}
				
				lineaDatos.add(resultadosConsulta.getString("acronimo"));
				lineaDatos.add(resultadosConsulta.getString("fechaDiligenciamiento"));
				lineaDatos.add(Integer.toString(semana));
				lineaDatos.add(Integer.toString(anyo));
				lineaDatos.add("XXX");
				lineaDatos.add("YYY");
				lineaDatos.add(resultadosConsulta.getString("primer_nombre"));
				lineaDatos.add(resultadosConsulta.getString("segundo_nombre"));
				lineaDatos.add(resultadosConsulta.getString("primer_apellido"));
				lineaDatos.add(resultadosConsulta.getString("segundo_apellido"));
				lineaDatos.add(resultadosConsulta.getString("tipoId"));
				lineaDatos.add(resultadosConsulta.getString("numero_identificacion"));
				lineaDatos.add(edad);
				lineaDatos.add(unidadEdad);
				lineaDatos.add(sexo);
				lineaDatos.add(resultadosConsulta.getString("paisProcedencia"));
				lineaDatos.add(resultadosConsulta.getString("departamentoProcedencia"));
				lineaDatos.add(resultadosConsulta.getString("municipioProcedencia"));
				lineaDatos.add(resultadosConsulta.getString("areaProcedencia"));
				lineaDatos.add(resultadosConsulta.getString("barrio"));
				lineaDatos.add(resultadosConsulta.getString("direccion_paciente"));
				lineaDatos.add(resultadosConsulta.getString("ocupacion_paciente"));
				lineaDatos.add(tipoRegimen);
				lineaDatos.add(resultadosConsulta.getString("codigo_min_salud"));
				lineaDatos.add(pertenenciaEtnica);
				lineaDatos.add(resultadosConsulta.getString("grupoPoblacional"));
				lineaDatos.add(resultadosConsulta.getString("dep_vivienda"));
				lineaDatos.add(resultadosConsulta.getString("ciu_vivienda"));
				lineaDatos.add(UtilidadFecha.conversionFormatoFechaABD(resultadosConsulta.getString("fechaConsultaGeneral")));
				lineaDatos.add(UtilidadFecha.conversionFormatoFechaABD(resultadosConsulta.getString("fechaInicioSintomas")));
				lineaDatos.add(resultadosConsulta.getString("tipoCaso"));
				lineaDatos.add(valorOpcionBasica2(resultadosConsulta.getInt("hospitalizado")));
				lineaDatos.add(resultadosConsulta.getString("fechaHospitalizacion"));
				lineaDatos.add(resultadosConsulta.getString("condicionFinal"));
				lineaDatos.add(UtilidadFecha.conversionFormatoFechaABD(resultadosConsulta.getString("fechaDefuncion")));
				lineaDatos.add("0");
				
				

				String lineaTexto = "";
				String contenido = "\"";
				
				for (int i=0; i<lineaDatos.size(); i++) {
					
					String elemento = "";
            		try {
            			elemento = lineaDatos.get(i).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (i<lineaDatos.size()-1) {
            			contenido += elemento+"\",\"";
            		}
            		else if (i==lineaDatos.size()-1) {
            			contenido += elemento+"\"";
            		}
            		
				}
				
				elementos.add(contenido);
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return elementos;
	}
	
	
	
	
	
	
	
	private static String valorOpcionBasica(int valor)
	{
		if (valor==1)
		{
			return "2";
		}
		else if (valor==2)
		{
			return "1";
		}
		else {
			return "3";
		}
	}
	
	
	

	private static String valorOpcionBasica2(int valor)
	{
		if (valor==1)
		{
			return "2";
		}
		else if (valor==2)
		{
			return "1";
		}
		else {
			return "0";
		}
	}
	
	
	
	private static String bool2num(String valor)
	{
		if (valor=="t")
		{
			return "1";
		}
		else {
			return "2";
		}
	}
	
	
	
	private static String num2num(int valor)
	{
		if (valor==1)
		{
			return "1";
		}
		else {
			return "2";
		}
	}
	
	
	
	private static int estadoCivil(String estado)
	{
		if (estado.equals("S")) {
			
			return 1;
		}
		else if (estado.equals("C")) {
			
			return 2;
		}
		else if (estado.equals("U")) {
			
			return 3;
		}
		else if (estado.equals("V")) {
			
			return 4;
		}
		else {
			
			return 5;
		}
	}
	
	
	
	
	
	public static String obtenerCodigoBrote(int codigo)
	{
		String valor = "";
		
		if (codigo == 1) {
			valor = "998";
		}
		else if (codigo == 2) {
			
			valor = "999";
		}
		else if (codigo == 3) {
			
			valor = "830";
		}
		else if (codigo == 4) {
			
			valor = "350";
		}
		
		return valor;
	}
	
	/**
	 * Metodo que genera el archivo de texto de Morbilidad
	 * @param resultadosConsulta
	 * @return
	 */
	public static int generarArchivoMorbilidad(ResultSet resultadosConsulta,int semana,int anyo)
	{
		int resultado = 0;
		String nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-MORBI.SDF";
		String ruta = rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
		Vector elementos = new Vector();
		
		try {
			
			int x=1;
						
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				String identificacion = resultadosConsulta.getString("numero_identificacion");
				
				
				//*****************************************************************
				Date fecha = new Date();
				GregorianCalendar calendario = new GregorianCalendar();
				calendario.setTime(fecha);
				
				String fechaActual = "";
				String diaActual = "";
				String mesActual = "";
				String anyoActual = "";
				
				if (calendario.get(Calendar.DAY_OF_MONTH)<10) {
					
					diaActual = "0"+Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				else {
					diaActual = Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				
				if (calendario.get(Calendar.MONTH)+1<10) {
					
					mesActual = "0"+Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				else {
					mesActual = Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				
				anyoActual = Integer.toString(calendario.get(Calendar.YEAR));
				fechaActual = mesActual+"/"+diaActual+"/"+anyoActual;
				
				String fechaActualFormato = diaActual+"/"+mesActual+"/"+anyoActual;
				
				//*****************************************************************
				
				
				String fechaCon = resultadosConsulta.getString("fechaDiligenciamiento");
				String diaConsulta = "";
				String mesConsulta = "";
				String anyoConsulta = "";
				
				diaConsulta = fechaCon.split("-")[2];
				mesConsulta = fechaCon.split("-")[1];
				anyoConsulta = fechaCon.split("-")[0];
				String fechaConsulta = diaConsulta+"/"+mesConsulta+"/"+anyoConsulta;
				
				//*******************************************************************
				
				String fechaNacimiento = resultadosConsulta.getString("fecha_nacimiento");
				
				String diaNacimiento = fechaNacimiento.split("-")[2];
				String mesNacimiento = fechaNacimiento.split("-")[1];
				String anyoNacimiento = fechaNacimiento.split("-")[0];
				
				String fechaNac = diaNacimiento+"/"+mesNacimiento+"/"+anyoNacimiento;
				
				Vector edadDetallada = CalendarioEpidemiologico.obtenerEdadDetallada(fechaNac,fechaConsulta);
				
				String edad = edadDetallada.get(0).toString();
				String tipoEdad = edadDetallada.get(1).toString();
				
				//*******************************************************************
				
				String sexo = resultadosConsulta.getString("sexoPaciente");
				String genero = "";
				
				if (Integer.parseInt(sexo)==1) {
					
					genero = "M";
				}
				else {
					genero = "F";
				}
				
				//******************************************************************
				
				String codigoEnfermedad = resultadosConsulta.getString("codigoEnfNot");
				int codigoMorbilidad = TipoDiagnosticos.convertirASistemaSecretaria(Integer.parseInt(codigoEnfermedad));
				String nombreEvento = TipoDiagnosticos.obtenerNombreMorbilidad(codigoMorbilidad);
				
				//******************************************************************
				
				String nombreBarrio = resultadosConsulta.getString("nombreBarrio");
				int codigoBarrio = resultadosConsulta.getInt("codigoBarrio");
				
				if (codigoBarrio==0) {
					
					nombreBarrio = "";
				}
				
				//******************************************************************
				
				String fechaInicioSintomas = resultadosConsulta.getString("fechaInicioSintomas");
				
				//******************************************************************
				
				String nombreDepOri = resultadosConsulta.getString("nombreDepOrigen");
				String nombreMunOri = resultadosConsulta.getString("nombreMunOrigen");
				
				if (nombreDepOri.equals("BOGOTA D.C.")) {
					nombreDepOri = "BOGOTA";
				}
				
				
				//******************************************************************
				
				int tipoCaso = resultadosConsulta.getInt("tipocaso");
				String clasificacion = "";
				
				// Conversion tipo de caso :
				/*
				 * SOSPECHOSO - 1 - H
				 * PROBABLE - 2 - A
				 * CONF. LABORATORIO - 3 - D
				 * CONF. CLINICA - 4 - F
				 * CONF. NEXO - 5 - E
				 */
				
				if (tipoCaso==1) {
					clasificacion = "H";
				}
				else if (tipoCaso==2) {
					clasificacion = "A";
				}
				else if (tipoCaso==3) {
					clasificacion = "D";
				}
				else if (tipoCaso==4) {
					clasificacion = "F";
				}
				else if (tipoCaso==5) {
					clasificacion = "E";
				}
				
								
				/////////////////////////////////////////////////////////////////////
				
				lineaDatos.add("1");
				lineaDatos.add(fechaActual);
				lineaDatos.add(fechaConsulta);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroSemana(fechaConsulta)[0]);
				lineaDatos.add(fechaInicioSintomas);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroSemana(fechaInicioSintomas)[0]);
				lineaDatos.add(fechaActualFormato);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroPeriodo(fechaActualFormato)[0]);
				lineaDatos.add(fechaActualFormato);					// fecha cierre caso
				lineaDatos.add("BOGOTA");		// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add("SUBA");			// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add(resultadosConsulta.getString("nombreInstitucion"));
				lineaDatos.add(resultadosConsulta.getInt("codigoInstitucion"));
			//	lineaDatos.add(resultadosConsulta.getString("nombreMunResidencia"));
			//	lineaDatos.add(resultadosConsulta.getString("codigoMunResidencia"));
				lineaDatos.add("SUBA");
				lineaDatos.add("11");
				lineaDatos.add(resultadosConsulta.getString("zonaResidencia"));
				lineaDatos.add(nombreDepOri);
				lineaDatos.add(nombreMunOri);
				lineaDatos.add(Integer.toString(x));
				lineaDatos.add(identificacion);
				lineaDatos.add(resultadosConsulta.getString("zonaResidencia"));
				lineaDatos.add(resultadosConsulta.getString("primer_nombre"));
				lineaDatos.add(resultadosConsulta.getString("segundo_nombre"));
				lineaDatos.add(resultadosConsulta.getString("primer_apellido"));
				lineaDatos.add(resultadosConsulta.getString("segundo_apellido"));
				lineaDatos.add(identificacion);
				lineaDatos.add(resultadosConsulta.getString("tipo_identificacion"));
				lineaDatos.add(edad);
				lineaDatos.add(tipoEdad);
				lineaDatos.add(CalendarioEpidemiologico.obtenerGrupoEdad(fechaNac,fechaConsulta));
				lineaDatos.add(genero);
				lineaDatos.add(nombreBarrio);
				lineaDatos.add(codigoBarrio);
				lineaDatos.add(resultadosConsulta.getString("direccion"));
				lineaDatos.add(resultadosConsulta.getString("telefono"));
				lineaDatos.add(resultadosConsulta.getString("regimenSalud"));
				lineaDatos.add(resultadosConsulta.getString("nombreAseguradora"));
				lineaDatos.add(nombreEvento);
				lineaDatos.add(codigoMorbilidad);
				lineaDatos.add("");				// Falta gestante
				lineaDatos.add("");				// Falta SUBCL5
				lineaDatos.add("");				// Falta SUBCL6
				lineaDatos.add("");				// Falta SUBCL12
				lineaDatos.add("");				// Falta SUBCL15
				lineaDatos.add("");				// Falta SUBCL18
				lineaDatos.add("");				// Falta SUBCL21
				lineaDatos.add("");				// Falta SUBCL22
				lineaDatos.add("");				// Falta SUBCL27
				lineaDatos.add("");				// Falta SUBCL29
				lineaDatos.add("");				// Falta SUBCL31
				lineaDatos.add("");				// Falta SUBCL24
				lineaDatos.add("");				// Falta SUBCL32
				lineaDatos.add(clasificacion);
				lineaDatos.add("N");			// laboratorio
				lineaDatos.add("");				// Radicado LS
				
				for (int i=1;i<77;i++) {
					lineaDatos.add("");
				}
				
				lineaDatos.add("V");
				lineaDatos.add("N");
				
				
				
				
				/*
				for (int i=1;i<29;i++) {
					
					lineaDatos.add(resultadosConsulta.getString(i));
				}
				*/
				x++;
				
				elementos.add(lineaDatos);
			}
		}
		catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
		
		FileOutputStream out; 
        PrintStream p; 
        
        if (elementos.size()==0) {
        	return 0;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
            
            for (int i=0;i<elementos.size();i++) {
            	
            	Vector linea = (Vector)elementos.get(i);
            	String contenido = "\"";
            	      	
            	for (int j=0;j<135;j++) {
            		
            		String elemento = "";
            		try {
            			elemento = linea.get(j).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (j<134) {
            			contenido += elemento+"\",\"";
            		}
            		else if (j==134) {
            			contenido += elemento+"\"";
            		}
            	}
            	
            	// p.print(contenido+"\n\r");
            	p.println(contenido);
            }
            
            resultado = 1;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = 0;
            System.err.println ("Error generando archivo de morbilidad : "+e.getMessage());
        }
        
        return resultado;
	}
	
	
	
	
	public static int generarArchivoMortalidad(ResultSet resultadosConsulta,int semana, int anyo)
	{
		int resultado = 0;
		String nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-MORTAL.SDF";
		String ruta = rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
		Vector elementos = new Vector();
		
		try {
			
			int x=1;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();
				String identificacion = resultadosConsulta.getString("numero_identificacion");
				
				
				//*****************************************************************
				Date fecha = new Date();
				GregorianCalendar calendario = new GregorianCalendar();
				calendario.setTime(fecha);
				
				String fechaActual = "";
				String diaActual = "";
				String mesActual = "";
				String anyoActual = "";
				
				if (calendario.get(Calendar.DAY_OF_MONTH)<10) {
					
					diaActual = "0"+Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				else {
					diaActual = Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				
				if (calendario.get(Calendar.MONTH)+1<10) {
					
					mesActual = "0"+Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				else {
					mesActual = Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				
				anyoActual = Integer.toString(calendario.get(Calendar.YEAR));
				fechaActual = mesActual+"/"+diaActual+"/"+anyoActual;
				
				String fechaActualFormato = diaActual+"/"+mesActual+"/"+anyoActual;
				
				//*****************************************************************
				
				
				String fechaCon = UtilidadFecha.conversionFormatoFechaAAp(resultadosConsulta.getString("fechaNotificacion"));
				String diaConsulta = "";
				String mesConsulta = "";
				String anyoConsulta = "";
				
				/*
				diaConsulta = fechaCon.split("-")[0];
				mesConsulta = fechaCon.split("-")[1];
				anyoConsulta = fechaCon.split("-")[2];
				
				String fechaConsulta = diaConsulta+"/"+mesConsulta+"/"+anyoConsulta;
				*/
				String fechaConsulta = fechaCon;
				
				//*******************************************************************
				
				String fechaNacimiento = resultadosConsulta.getString("fecha_nacimiento");
				
				String diaNacimiento = fechaNacimiento.split("-")[2];
				String mesNacimiento = fechaNacimiento.split("-")[1];
				String anyoNacimiento = fechaNacimiento.split("-")[0];
				
				String fechaNac = diaNacimiento+"/"+mesNacimiento+"/"+anyoNacimiento;
				
				Vector edadDetallada = CalendarioEpidemiologico.obtenerEdadDetallada(fechaNac,fechaConsulta);
				
				String edad = edadDetallada.get(0).toString();
				String tipoEdad = edadDetallada.get(1).toString();
				
				//*******************************************************************
				
				String sexo = resultadosConsulta.getString("sexoPaciente");
				String genero = "";
				
				if (Integer.parseInt(sexo)==1) {
					
					genero = "M";
				}
				else {
					genero = "F";
				}
				
				//******************************************************************
				
			//	String codigoEnfermedad = resultadosConsulta.getString("codigoEnfNot");
			//	int codigoMorbilidad = TipoDiagnosticos.convertirASistemaSecretaria(Integer.parseInt(codigoEnfermedad));
			//	String nombreEvento = TipoDiagnosticos.obtenerNombreMorbilidad(codigoMorbilidad);
				
				
				//******************************************************************
				
				String nombreBarrio = resultadosConsulta.getString("nombreBarrio");
				int codigoBarrio = resultadosConsulta.getInt("codigoBarrio");
				
				if (codigoBarrio==0) {
					
					nombreBarrio = "";
				}
				
				//******************************************************************
				
				String fechaInicioSintomas = resultadosConsulta.getString("fechaInicioSintomas");
				String fechaDefuncion = resultadosConsulta.getString("fechaDefuncion");
				String fechaNotificacion = fechaCon;
				
				int codigoMunRes = resultadosConsulta.getInt("codigoMunResidencia");
				int codigoDepRes = resultadosConsulta.getInt("codigoDepResidencia");
				int codigoMunOrigen = resultadosConsulta.getInt("codigoMunOrigen");
				int codigoDepOrigen = resultadosConsulta.getInt("codigoDepOrigen");
				
				String nombreMunRes = "";
				String nombreDepRes = "";
				String codMunRes = "";
				
				String nombreMunOrigen = "";
				String nombreDepOrigen = "";
				String codMunOrigen = "";
				
				if (codigoMunRes==21 && codigoDepRes==11) {
					nombreMunRes = "SUBA";
					nombreDepRes = "BOGOTA";
					codMunRes = "11";
				}
				else {
					nombreMunRes = resultadosConsulta.getString("nombreMunResidencia");
					nombreDepRes = resultadosConsulta.getString("nombreDepResidencia");
					codigoMunRes = resultadosConsulta.getInt("codigoMunResidencia");
				}
				
				if (codigoMunOrigen==21 && codigoDepOrigen==11) {
					nombreMunOrigen = "SUBA";
					nombreDepOrigen = "BOGOTA";
					codMunOrigen = "11";
				}
				else {
					nombreMunOrigen = resultadosConsulta.getString("nombreMunResidencia");
					nombreDepOrigen = resultadosConsulta.getString("nombreDepResidencia");
					codigoMunOrigen = resultadosConsulta.getInt("codigoMunResidencia");
				}
				
				int tipoMortal = resultadosConsulta.getInt("tipoMortalidad");
				String tipoMort = "";
				int codMort = 1;
				
				if (tipoMortal == ConstantesBD.codigoFichaMortalidadMaterna) {
					tipoMort = "MUERTE MATERNA";
					codMort = 2;
				}
				else if (tipoMortal == ConstantesBD.codigoFichaMortalidadPerinatal) {
					tipoMort = "PERINATAL";
					codMort = 1;
				}
				
				/////////////////////////////////////////////////////////////////////
				
				lineaDatos.add("3");
				lineaDatos.add(fechaActual);
				lineaDatos.add("");				// Falta certificado defuncion
				lineaDatos.add(resultadosConsulta.getString("horaDefuncion"));
				lineaDatos.add(fechaDefuncion);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroSemana(fechaDefuncion)[0]);
				lineaDatos.add(fechaInicioSintomas);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroSemana(fechaInicioSintomas)[0]);
				lineaDatos.add(fechaNotificacion);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroPeriodo(fechaNotificacion)[0]);
				lineaDatos.add(fechaActualFormato);					// fecha cierre caso
				lineaDatos.add("BOGOTA");		// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add("SUBA");			// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add(resultadosConsulta.getString("nombreInstitucion"));
				lineaDatos.add(resultadosConsulta.getInt("codigoInstitucion"));
				lineaDatos.add(nombreMunRes);
				lineaDatos.add(codMunRes);
				lineaDatos.add(resultadosConsulta.getInt("sitioDefuncion"));
				lineaDatos.add(resultadosConsulta.getString("zonaResidencia"));
				lineaDatos.add(nombreDepOrigen);
				lineaDatos.add(nombreMunOrigen);
				lineaDatos.add("");				// Institucion de Origen
				lineaDatos.add("");				// Referido localidad
				lineaDatos.add("");				// Fecha referido
				lineaDatos.add(Integer.toString(x));
				lineaDatos.add(identificacion);			// # Historia
				lineaDatos.add("");				// Llave ??
				lineaDatos.add("");				// Notifica ??
				lineaDatos.add(resultadosConsulta.getString("primer_nombre"));
				lineaDatos.add(resultadosConsulta.getString("segundo_nombre"));
				lineaDatos.add(resultadosConsulta.getString("primer_apellido"));
				lineaDatos.add(resultadosConsulta.getString("segundo_apellido"));
				lineaDatos.add(edad);
				lineaDatos.add(tipoEdad);
				lineaDatos.add(CalendarioEpidemiologico.obtenerGrupoEdad(fechaNac,fechaConsulta));
				lineaDatos.add(genero);
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
			//	lineaDatos.add(identificacion);
			//	lineaDatos.add(resultadosConsulta.getString("tipo_identificacion"));
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add(nombreBarrio);
				lineaDatos.add(codigoBarrio);
				lineaDatos.add(resultadosConsulta.getString("direccion"));
				lineaDatos.add(resultadosConsulta.getString("telefono"));
				lineaDatos.add(resultadosConsulta.getString("regimenSalud"));
				lineaDatos.add(resultadosConsulta.getString("nombreAseguradora"));
				lineaDatos.add(tipoMort);
				lineaDatos.add(codMort);
				lineaDatos.add("");				// clasificacion ??
				lineaDatos.add("");				// cuarto digito ??
				lineaDatos.add(resultadosConsulta.getString("pesoNacimiento"));
				lineaDatos.add(resultadosConsulta.getString("edadGestacional"));
				lineaDatos.add("");				// Analisis de caso
				lineaDatos.add("");				// Intervencion
				
				x++;
				
				elementos.add(lineaDatos);
			}
		}
		catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
		
		FileOutputStream out; 
        PrintStream p; 
        
        if (elementos.size()==0) {
        	return 0;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
            
            for (int i=0;i<elementos.size();i++) {
            	
            	Vector linea = (Vector)elementos.get(i);
            	String contenido = "\"";
            	      	
            	for (int j=0;j<58;j++) {
            		
            		String elemento = "";
            		try {
            			elemento = linea.get(j).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (j<57) {
            			contenido += elemento+"\",\"";
            		}
            		else if (j==57) {
            			contenido += elemento+"\"";
            		}
            	}
            	
            	p.println(contenido);
            }
            
            resultado = 1;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = 0;
            System.err.println ("Error generando archivo de mortalidad : "+e.getMessage());
        }
        
        return resultado;
	}
	
	
	
	
	public static int generarArchivoBrotes(ResultSet resultadosConsulta,int semana,int anyo)
	{
		int resultado = 0;
		String nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-BROTE.SDF";
		String ruta = rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
		Vector elementos = new Vector();
		
		try {
			
			int x=1;
			
			while (resultadosConsulta.next()) {
				
				Vector lineaDatos = new Vector();				
				
				//*****************************************************************
				Date fecha = new Date();
				GregorianCalendar calendario = new GregorianCalendar();
				calendario.setTime(fecha);
				
				String fechaActual = "";
				String diaActual = "";
				String mesActual = "";
				String anyoActual = "";
				
				if (calendario.get(Calendar.DAY_OF_MONTH)<10) {
					
					diaActual = "0"+Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				else {
					diaActual = Integer.toString(calendario.get(Calendar.DAY_OF_MONTH));
				}
				
				if (calendario.get(Calendar.MONTH)+1<10) {
					
					mesActual = "0"+Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				else {
					mesActual = Integer.toString(calendario.get(Calendar.MONTH)+1);
				}
				
				anyoActual = Integer.toString(calendario.get(Calendar.YEAR));
				fechaActual = mesActual+"/"+diaActual+"/"+anyoActual;
				
				String fechaActualFormato = diaActual+"/"+mesActual+"/"+anyoActual;
				
				//*****************************************************************
				
				
				String fechaCon = resultadosConsulta.getString("fechaDiligenciamiento");
				String diaConsulta = "";
				String mesConsulta = "";
				String anyoConsulta = "";
				
				diaConsulta = fechaCon.split("-")[2];
				mesConsulta = fechaCon.split("-")[1];
				anyoConsulta = fechaCon.split("-")[0];
				String fechaConsulta = diaConsulta+"/"+mesConsulta+"/"+anyoConsulta;
				
				//******************************************************************
				
				String fechaNot = resultadosConsulta.getString("fechaNotificacion");
				String fechaNotificacion = UtilidadFecha.conversionFormatoFechaAAp(fechaNot);
				
				int codigoMunProcedencia = resultadosConsulta.getInt("codigoMunProcedencia");
				int codigoDepProcedencia = resultadosConsulta.getInt("codigoDepProcedencia");
				
				String nombreMunProcedencia = "";
				String nombreDepProcedencia = "";
				String codMunProcedencia = "";
				
				
				if (codigoMunProcedencia==21 && codigoDepProcedencia==11) {
					nombreMunProcedencia = "SUBA";
					nombreDepProcedencia = "BOGOTA";
					codMunProcedencia = "11";
				}
				else {
					nombreMunProcedencia = resultadosConsulta.getString("nombreMunResidencia");
					nombreDepProcedencia = resultadosConsulta.getString("nombreDepResidencia");
					codigoMunProcedencia = resultadosConsulta.getInt("codigoMunResidencia");
				}
				
				int codEvento = resultadosConsulta.getInt("evento");
				String nombreEvento = TipoDiagnosticos.obtenerNombreBrotes(codEvento);
				
				int numeroMujeres = resultadosConsulta.getInt("mujeres");
				int numeroHombres = resultadosConsulta.getInt("hombres");
				int numeroTotalCasos = numeroHombres + numeroMujeres;	
				
				//**********************************************************
				
				String fechaCierre = UtilidadFecha.getFechaActual();
				
				/////////////////////////////////////////////////////////////////////
				
				lineaDatos.add("2");
				lineaDatos.add(fechaActual);
				lineaDatos.add(fechaConsulta);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroSemana(fechaConsulta)[0]);
				lineaDatos.add("");				// Fecha Inicio Sintomas
				lineaDatos.add("");
				lineaDatos.add(fechaNotificacion);
				lineaDatos.add(CalendarioEpidemiologico.obtenerNumeroPeriodo(fechaNotificacion)[0]);
				lineaDatos.add(fechaCierre);		// Fecha cierre de caso
				lineaDatos.add("BOGOTA");		// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add("SUBA");			// Hacer parametrizable
				lineaDatos.add("11");			// Hacer parametrizable
				lineaDatos.add(resultadosConsulta.getString("nombreInstitucion"));
				lineaDatos.add(resultadosConsulta.getInt("codigoInstitucion"));
				lineaDatos.add(nombreMunProcedencia);
				lineaDatos.add(codMunProcedencia);
				lineaDatos.add("U");			// Zona de Residencia - hacer parametrizable
				lineaDatos.add("");				// Nombre sitio origen
				lineaDatos.add("");				// Direccion sitio origen
				lineaDatos.add("");				// Barrio origen
				lineaDatos.add("");				// Codigo barrio
				lineaDatos.add("");				// Depto. residencia fuera Bogota
				lineaDatos.add("");				// Municipio res. fuera Bogota
				lineaDatos.add("");				// Referido localidad
				lineaDatos.add("");				// Fecha Referencia
				lineaDatos.add(Integer.toString(x));
				lineaDatos.add("");				// # de historia clinica ??? WTF ??
				lineaDatos.add("");				// Llave
				lineaDatos.add("");				// Notifica ? (S,N)
				lineaDatos.add(nombreEvento);
				lineaDatos.add(codEvento);
				
				
				lineaDatos.add("");				// SUBCL3
				lineaDatos.add("");				// Clasificacion
				lineaDatos.add("");				// Laboratorio
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				lineaDatos.add("");
				
				lineaDatos.add(Integer.toString(numeroTotalCasos));
				lineaDatos.add(resultadosConsulta.getString("muertos"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo1"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo2"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo3"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo4"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo5"));
				lineaDatos.add(resultadosConsulta.getString("pacientesGrupo6"));
				lineaDatos.add(Integer.toString(numeroMujeres));
				lineaDatos.add(Integer.toString(numeroHombres));
				lineaDatos.add("");
				
				x++;
				
				elementos.add(lineaDatos);
			}
		}
		catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
		FileOutputStream out; 
        PrintStream p; 
        
        if (elementos.size()==0) {
        	return 0;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
         
            for (int i=0;i<elementos.size();i++) {
            	
            	Vector linea = (Vector)elementos.get(i);
            	String contenido = "\"";
            	      	
            	for (int j=0;j<87;j++) {
            		
            		String elemento = "";
            		try {
            			elemento = linea.get(j).toString();
            		}
            		catch (NullPointerException npe) {
            			elemento = "";
            		}
            		
            		if (j<86) {
            			contenido += elemento+"\",\"";
            		}
            		else if (j==86) {
            			contenido += elemento+"\"";
            		}
            	}
            	
            	p.println(contenido);
            }
            
            resultado = 1;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = 0;
            System.err.println ("Error generando archivo de brotes : "+e.getMessage());
        }
        
        return resultado;
	}
	
	
	
	
	
	/*
	 * Chequea si realmente existe un archivo en el sistema; Codificacion : 1 - archivo de morbilidad, 2 - archivo de
	 * mortalidad y 3 - archivo de brotes.
	 */
	public static boolean archivoExiste(int tipoArchivo,int semana,int anyo) 
	{
		String nombreArchivo = "";
		
		if (tipoArchivo==1) {
		
			nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-MORBI.SDF";
		}
		else if (tipoArchivo==2) {
			
			nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-MORTAL.SDF";
		}
		else if (tipoArchivo==3) {
			
			nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-BROTE.SDF";
		}
		else if (tipoArchivo==4) {
			
			nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-SIVIM.SDF";
		}
		else if (tipoArchivo==5) {
			
			nombreArchivo = Integer.toString(anyo)+"-"+Integer.toString(semana)+"-SISVAN.SDF";
		}
		
		String ruta = rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo; 
		
		File f = new File(ruta);
		
		if (f.exists()) {
			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	
	public static String getRutaReportesSecretaria() {
		return rutaReportesSecretaria;
	}
	public static void setRutaReportesSecretaria(String rutaReportesSecretaria) {
		UtilidadGenArchivos.rutaReportesSecretaria = rutaReportesSecretaria;
	}
	public static String getRutaReportesGenerales() {
		return rutaReportesGenerales;
	}
	public static void setRutaReportesGenerales(String rutaReportesGenerales) {
		UtilidadGenArchivos.rutaReportesGenerales = rutaReportesGenerales;
	}
	public static String getRutaReportesSivigila() {
		return rutaReportesSivigila;
	}
	public static void setRutaReportesSivigila(String rutaReportesSivigila) {
		UtilidadGenArchivos.rutaReportesSivigila = rutaReportesSivigila;
	}




	public static String getRutaEpidemiologia() {
		return rutaEpidemiologia;
	}




	public static void setRutaEpidemiologia(String rutaEpidemiologia) {
		UtilidadGenArchivos.rutaEpidemiologia = rutaEpidemiologia;
	}
}
