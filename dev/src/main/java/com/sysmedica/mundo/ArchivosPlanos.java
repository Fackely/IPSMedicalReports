package com.sysmedica.mundo;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.ArchivosPlanosDao;
import com.sysmedica.util.CalendarioEpidemiologico;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import com.sysmedica.util.UtilidadGenArchivos;
import java.util.HashMap;

import java.sql.Connection;

public class ArchivosPlanos {

	private Logger logger = Logger.getLogger(ArchivosPlanos.class);
		
	private boolean caracterizacion;
	private boolean talento;
	private boolean notificacionIndividual;
	private boolean notificacionColectiva;
	private boolean datosComplementarios;
	private boolean control;
	
	private int semana;
	private int anyo;
	
	ArchivosPlanosDao archivosPlanosDao;
	
	
	public ArchivosPlanos() {
		
		reset();
		init(System.getProperty("TIPOBD"));
	}


	public void reset()
	{
		semana = 1;
		anyo = 1900;
	}
	
	
	/**
     * Inicializa el acceso a Base de Datos de este objeto
     * @param tipoBD
     * @return
     */
    public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			archivosPlanosDao = myFactory.getArchivosPlanosDao();
			wasInited = (archivosPlanosDao != null);
		}
		return wasInited;
	}
    
    
    public boolean generarArchivoNotificaciones(Connection con)
    {
    	String nombreArchivo = "asdfg.txt";
		String ruta = UtilidadGenArchivos.rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
    	boolean resultado = false;
    	Vector lineasArchivo = new Vector();
    	
    	if (semana==0) {
    		
    		String fechaActual = UtilidadFecha.getFechaActual();
    		
			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
			
			anyo = Integer.parseInt(fechaActual.split("/")[2]);
			
			if (numSemanaActual==1) {
				semana = 52;
				anyo = anyo-1;
			}
			else {
				semana = numSemanaActual-1;
			}
    	}
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE DENGUE
    	
    	ResultSet rs = archivosPlanosDao.consultaCasosDengue(con, semana, anyo);
    	ResultSet rsAuxiliar = rs;
    	Vector lineasHallazgos = new Vector();
    	
    	try {
    		
	    	while (rsAuxiliar.next()) {
	    		
	    		int codigo = rsAuxiliar.getInt("codigoFichaDengue");
	    		
	    		ResultSet rs2 = archivosPlanosDao.consultaHallazgosDengue(con, codigo);
	    		Vector lineaHallazgos = new Vector();
	    		
	    		while (rs2.next()) {
	    			
	    			lineaHallazgos.add(rs2.getString("codigoHallazgo"));
	    		}
	    		
	    		lineasHallazgos.add(lineaHallazgos);
	    	}
	    	
	    	rsAuxiliar.beforeFirst();
	    	rs.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		System.err.println ("Error generando archivo plano de notificaciones individuales (consulta de casos de dengue) : "+sqle.getMessage());
    	}
    	
    	Vector lineaDatosDengue = UtilidadGenArchivos.generarVectorDengue(rs, lineasHallazgos, semana, anyo);
    	
    	for (int i=0;i<lineaDatosDengue.size();i++) {
    		
    		lineasArchivo.add(lineaDatosDengue.get(i));
    	}
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE INTOXICACIONES
    	
    	ResultSet rs3 = archivosPlanosDao.consultaCasosIntoxicacion(con, semana, anyo);
    	
    	Vector lineaDatosIntoxicacion = UtilidadGenArchivos.generarVectorIntoxicacion(rs3, semana, anyo);
    	
    	for (int i=0;i<lineaDatosIntoxicacion.size();i++) {
    		
    		lineasArchivo.add(lineaDatosIntoxicacion.get(i));
    	}
    	
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE LEPRA
    	

    	ResultSet rs4 = archivosPlanosDao.consultaCasosLepra(con, semana, anyo);
    	
    	Vector lineaDatosLepra = UtilidadGenArchivos.generarVectorLepra(rs4, semana, anyo);
    	
    	for (int i=0;i<lineaDatosLepra.size();i++) {
    		
    		lineasArchivo.add(lineaDatosLepra.get(i));
    	}  	
    	
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE PARALISIS FLACIDA
    	
    	HashMap mapaExtremidades = new HashMap();
    	HashMap mapaGruposEdad = new HashMap();
    	
    	ResultSet rs5 = archivosPlanosDao.consultaCasosParalisis(con, semana, anyo);
    	
    	try {
	    	
	    	
	    	
	    	int codigo1 = rs5.getInt("codigoextremidad1");
	    	int codigo2 = rs5.getInt("codigoextremidad2");
	    	int codigo3 = rs5.getInt("codigoextremidad3");
	    	int codigo4 = rs5.getInt("codigoextremidad4");
	    	
	    	
	    	
	    	ResultSet rs6 = archivosPlanosDao.consultaExtremidadesParalisis(con, codigo1);
	    	
	    	mapaExtremidades.put("paresia_1", rs6.getString("paresia"));
	    	mapaExtremidades.put("paralisis_1", rs6.getString("paralisis"));
	    	mapaExtremidades.put("flaccida_1", rs6.getString("flaccida"));
	    	mapaExtremidades.put("localizacion_1", rs6.getString("localizacion"));
	    	mapaExtremidades.put("sensibilidad_1", rs6.getString("sensibilidad"));
	    	mapaExtremidades.put("rot_1", rs6.getString("rot"));
	    	
	    	rs6 = archivosPlanosDao.consultaExtremidadesParalisis(con, codigo2);
	    	
	    	mapaExtremidades.put("paresia_2", rs6.getString("paresia"));
	    	mapaExtremidades.put("paralisis_2", rs6.getString("paralisis"));
	    	mapaExtremidades.put("flaccida_2", rs6.getString("flaccida"));
	    	mapaExtremidades.put("localizacion_2", rs6.getString("localizacion"));
	    	mapaExtremidades.put("sensibilidad_2", rs6.getString("sensibilidad"));
	    	mapaExtremidades.put("rot_2", rs6.getString("rot"));
	    	
	    	rs6 = archivosPlanosDao.consultaExtremidadesParalisis(con, codigo3);
	    	
	    	mapaExtremidades.put("paresia_3", rs6.getString("paresia"));
	    	mapaExtremidades.put("paralisis_3", rs6.getString("paralisis"));
	    	mapaExtremidades.put("flaccida_3", rs6.getString("flaccida"));
	    	mapaExtremidades.put("localizacion_3", rs6.getString("localizacion"));
	    	mapaExtremidades.put("sensibilidad_3", rs6.getString("sensibilidad"));
	    	mapaExtremidades.put("rot_3", rs6.getString("rot"));
	    	
	    	rs6 = archivosPlanosDao.consultaExtremidadesParalisis(con, codigo4);
	    	
	    	mapaExtremidades.put("paresia_4", rs6.getString("paresia"));
	    	mapaExtremidades.put("paralisis_4", rs6.getString("paralisis"));
	    	mapaExtremidades.put("flaccida_4", rs6.getString("flaccida"));
	    	mapaExtremidades.put("localizacion_4", rs6.getString("localizacion"));
	    	mapaExtremidades.put("sensibilidad_4", rs6.getString("sensibilidad"));
	    	mapaExtremidades.put("rot_4", rs6.getString("rot"));
	    		
	    	
	    	
	    	
	    	int codigoGrupo1 = rs5.getInt("codigogrupo1");
	    	int codigoGrupo2 = rs5.getInt("codigogrupo2");
	    	int codigoGrupo3 = rs5.getInt("codigogrupo3");
	    	
	    	
	    	
	    	ResultSet rs7 = archivosPlanosDao.consultaGrupoEdadParalisis(con, codigoGrupo1);
	    	
	    	mapaGruposEdad.put("poblacionmeta_1", rs7.getString("poblacionmeta"));
	    	mapaGruposEdad.put("reciennacido_1", rs7.getString("reciennacido"));
	    	mapaGruposEdad.put("vop1_1", rs7.getString("vop1"));
	    	mapaGruposEdad.put("vop2_1", rs7.getString("vop2"));
	    	mapaGruposEdad.put("vop3_1", rs7.getString("vop3"));
	    	mapaGruposEdad.put("adicionales_1", rs7.getString("adicional"));
	    	
	    	rs7 = archivosPlanosDao.consultaGrupoEdadParalisis(con, codigoGrupo2);
	    	
	    	mapaGruposEdad.put("poblacionmeta_2", rs7.getString("poblacionmeta"));
	    	mapaGruposEdad.put("vop1_2", rs7.getString("vop1"));
	    	mapaGruposEdad.put("vop2_2", rs7.getString("vop2"));
	    	mapaGruposEdad.put("vop3_2", rs7.getString("vop3"));
	    	mapaGruposEdad.put("adicionales_2", rs7.getString("adicional"));
	    	
	    	rs7 = archivosPlanosDao.consultaGrupoEdadParalisis(con, codigoGrupo3);
	    	
	    	mapaGruposEdad.put("poblacionmeta_3", rs7.getString("poblacionmeta"));
	    	mapaGruposEdad.put("vop1_3", rs7.getString("vop1"));
	    	mapaGruposEdad.put("vop2_3", rs7.getString("vop2"));
	    	mapaGruposEdad.put("vop3_3", rs7.getString("vop3"));
	    	mapaGruposEdad.put("adicionales_3", rs7.getString("adicional"));
    	}
    	catch (SQLException sqle) {
    		
    	}
    	
    	Vector lineaDatosParalisis = UtilidadGenArchivos.generarVectorParalisis(rs5, mapaExtremidades, mapaGruposEdad, semana, anyo);
    	

    	for (int i=0;i<lineaDatosParalisis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosParalisis.get(i));
    	}  	
    	
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE RABIA
    	

    	ResultSet rs8 = archivosPlanosDao.consultaCasosRabia(con, semana, anyo);
    	
    	Vector lineaDatosRabia = UtilidadGenArchivos.generarVectorRabia(rs8, semana, anyo);
    	
    	for (int i=0;i<lineaDatosRabia.size();i++) {
    		
    		lineasArchivo.add(lineaDatosRabia.get(i));
    	}  	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE SARAMPION
    	

    	ResultSet rs9 = archivosPlanosDao.consultaCasosSarampion(con, semana, anyo);
    	
    	Vector lineaDatosSarampion = UtilidadGenArchivos.generarVectorSarampion(rs9, semana, anyo);
    	
    	for (int i=0;i<lineaDatosSarampion.size();i++) {
    		
    		lineasArchivo.add(lineaDatosSarampion.get(i));
    	}  
    	
    	
    	
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE VIH

    	ResultSet rs10 = archivosPlanosDao.consultaCasosVIH(con, semana, anyo);
    	
    	HashMap mecanismos = new HashMap();
    	HashMap enfermedades = new HashMap();
    	
    	for (int x=1;x<10;x++) {
    		
    		mecanismos.put("mecanismo_"+x, "2");
    	}
    	
    	for (int x=1;x<28;x++) {
    		
    		enfermedades.put("enf_"+x, "2");
    	}
    	
    	try {
    		
    		while (rs10.next()) {
    			
    			int codigoFichaVIH = rs10.getInt("codigoFichaVIH");
    			
    			ResultSet rs11 = archivosPlanosDao.consultaMecanismosVIH(con, codigoFichaVIH);
    			
    			while (rs11.next()) {
    				
    				mecanismos.put("mecanismo_"+rs11.getInt("codigoMecanismo"), "1");
    			}
    			
    			
    			ResultSet rs12 = archivosPlanosDao.consultaEnfermedadesVIH(con, codigoFichaVIH);
    			
    			while (rs12.next()) {
    				
    				enfermedades.put("enf_"+rs12.getInt("codigoEnfermedad"), "1");
    			}
    		}
    		
    		rs10.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los mecanismos de transmision (Ficha de VIH)");
    	}
    	
    	
    	
    	Vector lineaDatosVIH = UtilidadGenArchivos.generarVectorVIH(rs10, mecanismos, enfermedades, semana, anyo);
    	
    	for (int i=0;i<lineaDatosVIH.size();i++) {
    		
    		lineasArchivo.add(lineaDatosVIH.get(i));
    	}  
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ACCIDENTE OFIDICO
    	

    	ResultSet rs13 = archivosPlanosDao.consultaCasosOfidico(con, semana, anyo);
    	
    	HashMap maniLocales = new HashMap();
    	HashMap maniSistemica = new HashMap();
    	HashMap compliLocales = new HashMap();
    	HashMap compliSistemica = new HashMap();
    	
    	for (int x=1;x<9;x++) {
    		
    		maniLocales.put("mani_"+x, "2");
    	}
    	
    	for (int x=1;x<15;x++) {
    		
    		maniSistemica.put("mani_"+x, "2");
    	}
    	
    	for (int x=1;x<7;x++) {
    		
    		compliLocales.put("compli_"+x, "2");
    	}
    	
    	for (int x=1;x<7;x++) {
    		
    		compliSistemica.put("compli_"+x, "2");
    	}
    	

    	try {
    		
    		while (rs13.next()) {
    			
    			int codigoFichaOfidico = rs10.getInt("codigoFichaOfidico");
    			
    			ResultSet rs14 = archivosPlanosDao.consultaManiLocalesOfidico(con, codigoFichaOfidico);
    			
    			while (rs14.next()) {
    				
    				maniLocales.put("mani_"+rs14.getInt("codigoManifestacion"), "1");
    			}
    			
    			
    			ResultSet rs15 = archivosPlanosDao.consultaManiSistemicaOfidico(con, codigoFichaOfidico);
    			
    			while (rs15.next()) {
    				
    				maniSistemica.put("mani_"+rs15.getInt("codigoManifestacion"), "1");
    			}
    			

    			ResultSet rs16 = archivosPlanosDao.consultaCompliLocalesOfidico(con, codigoFichaOfidico);
    			
    			while (rs16.next()) {
    				
    				compliLocales.put("compli_"+rs16.getInt("codigoComplicacion"), "1");
    			}
    			
    			
    			ResultSet rs17 = archivosPlanosDao.consultaCompliSistemicaOfidico(con, codigoFichaOfidico);
    			
    			while (rs17.next()) {
    				
    				compliSistemica.put("compli_"+rs17.getInt("codigoComplicacion"), "1");
    			}
    		}
    		
    		rs13.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los mecanismos de transmision (Ficha de VIH)");
    	}
    	
    	
    	
    	Vector lineaDatosOfidico = UtilidadGenArchivos.generarVectorOfidico(rs13, maniLocales, maniSistemica, compliLocales, compliSistemica, semana, anyo);
    	
    	for (int i=0;i<lineaDatosOfidico.size();i++) {
    		
    		lineasArchivo.add(lineaDatosOfidico.get(i));
    	} 
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MORTALIDAD MATERNA / PERINATAL
    	

    	ResultSet rs18 = archivosPlanosDao.consultaCasosMortalidad(con, semana, anyo);
    	
    	HashMap antecedentes = new HashMap();
    	HashMap complicaciones = new HashMap();
    	
    	for (int x=1;x<21;x++) {
    		
    		antecedentes.put("antecedente_"+x, "2");
    	}
    	

    	for (int x=1;x<16;x++) {
    		
    		complicaciones.put("antecedente_"+x, "2");
    	}
    	
    	

    	try {
    		
    		while (rs18.next()) {
    			
    			int codigoFichaMortalidad = rs18.getInt("codigoFichaMortalidad");
    			
    			ResultSet rs19 = archivosPlanosDao.consultaAntecedentesMortalidad(con, codigoFichaMortalidad);
    			
    			while (rs19.next()) {
    				
    				antecedentes.put("mani_"+rs19.getInt("codigoAntecedente"), "1");
    			}
    			
    			
    			ResultSet rs20 = archivosPlanosDao.consultaComplicacionesMortalidad(con, codigoFichaMortalidad);
    			
    			while (rs20.next()) {
    				
    				complicaciones.put("mani_"+rs20.getInt("codigoComplicacion"), "1");
    			}
    		}
    		
    		rs18.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los Antecedentes (Ficha de Mortalidad)");
    	}
    	
    	

    	Vector lineaDatosMortalidad = UtilidadGenArchivos.generarVectorMortalidad(rs18, antecedentes, complicaciones, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMortalidad.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMortalidad.get(i));
    	} 
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE RUBEOLA CONGENITA
    	

    	ResultSet rs19 = archivosPlanosDao.consultaCasosRubCongenita(con, semana, anyo);
    	
    	Vector lineaDatosRubCongenita = UtilidadGenArchivos.generarVectorRubCongenita(rs19, semana, anyo);
    	
    	for (int i=0;i<lineaDatosRubCongenita.size();i++) {
    		
    		lineasArchivo.add(lineaDatosRubCongenita.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE SIFILIS CONGENITA
    	

    	ResultSet rs20 = archivosPlanosDao.consultaCasosSifilis(con, semana, anyo);
    	
    	Vector lineaDatosSifilis = UtilidadGenArchivos.generarVectorSifilis(rs20, semana, anyo);
    	
    	for (int i=0;i<lineaDatosSifilis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosSifilis.get(i));
    	}  
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TETANOS NEONATAL
    	

    	ResultSet rs21 = archivosPlanosDao.consultaCasosTetanos(con, semana, anyo);
    	
    	Vector lineaDatosTetanos = UtilidadGenArchivos.generarVectorTetanos(rs21, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTetanos.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTetanos.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TUBERCULOSIS
    	

    	ResultSet rs22 = archivosPlanosDao.consultaCasosTetanos(con, semana, anyo);
    	
    	Vector lineaDatosTuberculosis = UtilidadGenArchivos.generarVectorTuberculosis(rs22, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTuberculosis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTuberculosis.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE DIFTERIA
    	

    	ResultSet rs23 = archivosPlanosDao.consultaCasosDifteria(con, semana, anyo);
    	
    	Vector lineaDatosDifteria = UtilidadGenArchivos.generarVectorDifteria(rs23, semana, anyo);
    	
    	for (int i=0;i<lineaDatosDifteria.size();i++) {
    		
    		lineasArchivo.add(lineaDatosDifteria.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE EASV
    	

    	ResultSet rs24 = archivosPlanosDao.consultaCasosEasv(con, semana, anyo);
    	
    	HashMap vacunas = new HashMap();
    	HashMap hallazgosEasv = new HashMap();
    	

    	for (int x=1;x<17;x++) {
    		
    		hallazgosEasv.put("hallazgo_"+x, "2");
    	}
    	

    	try {
    		
    		while (rs24.next()) {
    			
    			int codigoFichaEasv = rs24.getInt("codigoFichaEasv");
    			
    			ResultSet rs25 = archivosPlanosDao.consultaVacunasEasv(con, codigoFichaEasv);
    			
    			int j = 1;
    			
    			while (rs25.next()) {
    				
    				vacunas.put("vacuna_"+j, rs25.getInt("vacuna"));
    				vacunas.put("dosis_"+j, rs25.getInt("dosis"));
    				vacunas.put("via_"+j, rs25.getInt("via"));
    				vacunas.put("sitio_"+j, rs25.getInt("sitio"));
    				vacunas.put("fechaVacunacion_"+j, rs25.getInt("fechaVacunacion"));
    				
    				j++;
    			}
    			
    			
    			ResultSet rs26 = archivosPlanosDao.consultaHallazgosEasv(con, codigoFichaEasv);
    			
    			while (rs26.next()) {
    				
    				hallazgosEasv.put("hallazgo_"+rs26.getInt("codigoHallazgo"), "1");
    			}
    		}
    		
    		rs24.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los Antecedentes (Ficha de Mortalidad)");
    	}
    	
    	
    	Vector lineaDatosEasv = UtilidadGenArchivos.generarVectorEasv(rs24, vacunas, hallazgosEasv, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEasv.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEasv.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE HEPATITIS
    	

    	ResultSet rs27 = archivosPlanosDao.consultaCasosHepatitis(con, semana, anyo);
    	
    	HashMap poblacionHepatitis = new HashMap();
    	HashMap sintomasHepatitis = new HashMap();
    	

    	for (int x=1;x<12;x++) {
    		
    		poblacionHepatitis.put("poblacion_"+x, "2");
    	}
    	

    	for (int x=1;x<11;x++) {
    		
    		sintomasHepatitis.put("sintoma_"+x, "2");
    	}
    	

    	try {
    		
    		while (rs27.next()) {
    			
    			int codigoFichaHepatitis = rs27.getInt("codigoFichaHepatitis");
    			
    			ResultSet rs28 = archivosPlanosDao.consultaPoblacionHepatitis(con, codigoFichaHepatitis);
    			
    			while (rs28.next()) {
    				
    				poblacionHepatitis.put("poblacion_"+rs28.getInt("codigo"), "1");
    			}
    			
    			
    			ResultSet rs29 = archivosPlanosDao.consultaSintomasHepatitis(con, codigoFichaHepatitis);
    			
    			while (rs29.next()) {
    				
    				sintomasHepatitis.put("sintoma_"+rs29.getInt("codigo"), "1");
    			}
    		}
    		
    		rs27.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando la poblacion (Ficha de Hepatitis)");
    	}
    	
    	
    	Vector lineaDatosHepatitis = UtilidadGenArchivos.generarVectorHepatitis(rs27, poblacionHepatitis, sintomasHepatitis, semana, anyo);
    	
    	for (int i=0;i<lineaDatosHepatitis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosHepatitis.get(i));
    	}  
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE LEISHMANIASIS
    	

    	ResultSet rs30 = archivosPlanosDao.consultaCasosLeishmaniasis(con, semana, anyo);
    	
    	HashMap lesiones = new HashMap();
    	
    	hallazgosEasv.put("lesion_1", "");
    	hallazgosEasv.put("lesion_2", "");
    	hallazgosEasv.put("lesion_3", "");
    	

    	try {
    		
    		while (rs30.next()) {
    			
    			int codigoFichaLeish = rs30.getInt("codigoFichaLeishmaniasis");
    			
    			ResultSet rs31 = archivosPlanosDao.consultaTamLesionLeish(con, codigoFichaLeish);
    			
    			int j = 1;
    			
    			while (rs31.next()) {
    				
    				String largo = rs31.getString("largo");
    				String ancho = rs31.getString("ancho");
    				String tamanio = largo+" "+ancho;
    				
    				lesiones.put("lesion_"+j, tamanio);
    				
    				j++;
    			}
    			
    		}
    		
    		rs30.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando las lesiones (Ficha de Leishmaniasis)");
    	}
    	
    	
    	Vector lineaDatosLeish = UtilidadGenArchivos.generarVectorLeishmaniasis(rs30, lesiones, semana, anyo);
    	
    	for (int i=0;i<lineaDatosLeish.size();i++) {
    		
    		lineasArchivo.add(lineaDatosLeish.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MALARIA
    	

    	ResultSet rs32 = archivosPlanosDao.consultaCasosMalaria(con, semana, anyo);
    	
    	HashMap sintomasMalaria = new HashMap();
    	HashMap tratamientosMalaria = new HashMap();
    	

    	for (int x=1;x<26;x++) {
    		
    		sintomasMalaria.put("sintoma_"+x, "2");
    	}
    	

    	for (int x=1;x<11;x++) {
    		
    		tratamientosMalaria.put("tratamiento_"+x, "2");
    	}
    	

    	try {
    		
    		while (rs32.next()) {
    			
    			int codigoFichaMalaria = rs32.getInt("codigoFichaMalaria");
    			
    			ResultSet rs33 = archivosPlanosDao.consultaSintomasMalaria(con, codigoFichaMalaria);
    			
    			while (rs33.next()) {
    				
    				sintomasMalaria.put("sintoma_"+rs33.getInt("codigo"), "1");
    			}
    			
    			
    			ResultSet rs34 = archivosPlanosDao.consultaTratamientoMalaria(con, codigoFichaMalaria);
    			
    			while (rs34.next()) {
    				
    				tratamientosMalaria.put("sintoma_"+rs34.getInt("codigo"), "1");
    			}
    		}
    		
    		rs32.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los sintomas (Ficha de Malaria)");
    	}
    	
    	
    	Vector lineaDatosMalaria = UtilidadGenArchivos.generarVectorMalaria(rs32, sintomasMalaria, tratamientosMalaria, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMalaria.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMalaria.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MENINGITIS
    	

    	ResultSet rs35 = archivosPlanosDao.consultaCasosMeningitis(con, semana, anyo);
    	
    	Vector lineaDatosMeningitis = UtilidadGenArchivos.generarVectorMeningitis(rs35, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMeningitis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMeningitis.get(i));
    	}  
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ESI
    	

    	ResultSet rs36 = archivosPlanosDao.consultaCasosEsi(con, semana, anyo);
    	
    	Vector lineaDatosEsi = UtilidadGenArchivos.generarVectorEsi(rs36, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEsi.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEsi.get(i));
    	}  
    	
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ETAS
    	

    	ResultSet rs37 = archivosPlanosDao.consultaCasosEtas(con, semana, anyo);
    	
    	HashMap sintomasEtas = new HashMap();
    	

    	for (int x=1;x<18;x++) {
    		
    		sintomasEtas.put("sintoma_"+x, "2");
    	}
    	


    	try {
    		
    		while (rs37.next()) {
    			
    			int codigoFichaEtas = rs37.getInt("codigoFichaEtas");
    			
    			ResultSet rs38 = archivosPlanosDao.consultaSintomasEtas(con, codigoFichaEtas);
    			
    			while (rs38.next()) {
    				
    				sintomasEtas.put("sintoma_"+rs38.getInt("codigo"), "1");
    			}
    			
    		}
    		
    		rs37.beforeFirst();
    	}
    	catch (SQLException sqle) {
    		
    		System.err.println("Error consultando los sintomas (Ficha de Etas)");
    	}
    	
    	
    	Vector lineaDatosEtas = UtilidadGenArchivos.generarVectorEtas(rs37, sintomasMalaria, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEtas.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEtas.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TOSFERINA
    	

    	ResultSet rs39 = archivosPlanosDao.consultaCasosTosferina(con, semana, anyo);
    	
    	Vector lineaDatosTosferina = UtilidadGenArchivos.generarVectorTosferina(rs39, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTosferina.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTosferina.get(i));
    	}  
    	
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA DE LABORATORIOS
    	

    	ResultSet rs40 = archivosPlanosDao.consultaLaboratorios(con, semana, anyo);
    	
    	Vector lineaDatosLaboratorios = UtilidadGenArchivos.generarVectorLaboratorios(rs40, semana, anyo);
    	
    	for (int i=0;i<lineaDatosLaboratorios.size();i++) {
    		
    		lineasArchivo.add(lineaDatosLaboratorios.get(i));
    	}  
    	
    	
    	///////////////////////////////////////////////////
    	// Generacion de archivo de texto
    	
    	FileOutputStream out; 
        PrintStream p; 
        
        if (lineasArchivo.size()==0) {
        	return false;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
            
            for (int i=0;i<lineasArchivo.size();i++) {
            	
            	String linea = lineasArchivo.get(i).toString();
            
            	
            	p.println(linea);
            }
            
            resultado = true;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = false;
            System.err.println ("Error generando archivo plano de notificaciones individuales : "+e.getMessage());
        }
    	
    	return resultado;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public boolean generarArchivoInfoBasica(Connection con)
    {
    	String nombreArchivo = "infobasica.txt";
		String ruta = UtilidadGenArchivos.rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
    	boolean resultado = false;
    	Vector lineasArchivo = new Vector();
    	
    	if (semana==0) {
    		
    		String fechaActual = UtilidadFecha.getFechaActual();
    		
			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
			
			anyo = Integer.parseInt(fechaActual.split("/")[2]);
			
			if (numSemanaActual==1) {
				semana = 52;
				anyo = anyo-1;
			}
			else {
				semana = numSemanaActual-1;
			}
    	}
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE DENGUE
    	

    	ResultSet rs1 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaDengue");
    	
    	Vector lineaDatosDengue = UtilidadGenArchivos.generarVectorInfoBasica(rs1, semana, anyo);
    	
    	for (int i=0;i<lineaDatosDengue.size();i++) {
    		
    		lineasArchivo.add(lineaDatosDengue.get(i));
    	}  
    	
    	
    	
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE RABIA
    	
    	ResultSet rs2 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaRabia");
    	
    	Vector lineaDatosRabia = UtilidadGenArchivos.generarVectorInfoBasica(rs2, semana, anyo);
    	
    	for (int i=0;i<lineaDatosRabia.size();i++) {
    		
    		lineasArchivo.add(lineaDatosRabia.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE SARAMPION
    	
    	ResultSet rs3 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaSarampion");
    	
    	Vector lineaDatosSarampion = UtilidadGenArchivos.generarVectorInfoBasica(rs3, semana, anyo);
    	
    	for (int i=0;i<lineaDatosSarampion.size();i++) {
    		
    		lineasArchivo.add(lineaDatosSarampion.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE VIH
    	
    	ResultSet rs4 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaVih");
    	
    	Vector lineaDatosVIH = UtilidadGenArchivos.generarVectorInfoBasica(rs4, semana, anyo);
    	
    	for (int i=0;i<lineaDatosVIH.size();i++) {
    		
    		lineasArchivo.add(lineaDatosVIH.get(i));
    	}  
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE PARALISIS
    	
    	ResultSet rs5 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaParalisis");
    	
    	Vector lineaDatosParalisis = UtilidadGenArchivos.generarVectorInfoBasica(rs5, semana, anyo);
    	
    	for (int i=0;i<lineaDatosParalisis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosParalisis.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE SIFILIS
    	
    	ResultSet rs6 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaSifilis");
    	
    	Vector lineaDatosSifilis = UtilidadGenArchivos.generarVectorInfoBasica(rs6, semana, anyo);
    	
    	for (int i=0;i<lineaDatosSifilis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosSifilis.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TETANOS
    	
    	ResultSet rs7 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaTetanos");
    	
    	Vector lineaDatosTetanos = UtilidadGenArchivos.generarVectorInfoBasica(rs7, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTetanos.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTetanos.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TUBERCULOSIS
    	
    	ResultSet rs8 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaTuberculosis");
    	
    	Vector lineaDatosTuberculosis = UtilidadGenArchivos.generarVectorInfoBasica(rs8, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTuberculosis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTuberculosis.get(i));
    	}  
    	
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MORTALIDAD
    	
    	ResultSet rs9 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaMortalidad");
    	
    	Vector lineaDatosMortalidad = UtilidadGenArchivos.generarVectorInfoBasica(rs9, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMortalidad.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMortalidad.get(i));
    	}  
    	
    	
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE INTOXICACIONES
    	
    	ResultSet rs10 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaIntoxicacion");
    	
    	Vector lineaDatosIntoxicaciones = UtilidadGenArchivos.generarVectorInfoBasica(rs10, semana, anyo);
    	
    	for (int i=0;i<lineaDatosIntoxicaciones.size();i++) {
    		
    		lineasArchivo.add(lineaDatosIntoxicaciones.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE RUBEOLA CONGENITA
    	
    	ResultSet rs11 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaRubCongenita");
    	
    	Vector lineaDatosRubCongenita = UtilidadGenArchivos.generarVectorInfoBasica(rs11, semana, anyo);
    	
    	for (int i=0;i<lineaDatosRubCongenita.size();i++) {
    		
    		lineasArchivo.add(lineaDatosRubCongenita.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ACCIDENTE OFIDICO
    	
    	ResultSet rs12 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaOfidico");
    	
    	Vector lineaDatosOfidico = UtilidadGenArchivos.generarVectorInfoBasica(rs12, semana, anyo);
    	
    	for (int i=0;i<lineaDatosOfidico.size();i++) {
    		
    		lineasArchivo.add(lineaDatosOfidico.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE LEPRA
    	
    	ResultSet rs13 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaLepra");
    	
    	Vector lineaDatosLepra = UtilidadGenArchivos.generarVectorInfoBasica(rs13, semana, anyo);
    	
    	for (int i=0;i<lineaDatosLepra.size();i++) {
    		
    		lineasArchivo.add(lineaDatosLepra.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE DIFTERIA
    	
    	ResultSet rs14 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaDifteria");
    	
    	Vector lineaDatosDifteria = UtilidadGenArchivos.generarVectorInfoBasica(rs14, semana, anyo);
    	
    	for (int i=0;i<lineaDatosDifteria.size();i++) {
    		
    		lineasArchivo.add(lineaDatosDifteria.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE EASV
    	
    	ResultSet rs15 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaEasv");
    	
    	Vector lineaDatosEasv = UtilidadGenArchivos.generarVectorInfoBasica(rs15, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEasv.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEasv.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ESI
    	
    	ResultSet rs16 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaEsi");
    	
    	Vector lineaDatosEsi = UtilidadGenArchivos.generarVectorInfoBasica(rs16, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEsi.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEsi.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE ETAS
    	
    	ResultSet rs17 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaEtas");
    	
    	Vector lineaDatosEtas = UtilidadGenArchivos.generarVectorInfoBasica(rs17, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEtas.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEtas.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE HEPATITIS
    	
    	ResultSet rs18 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaHepatitis");
    	
    	Vector lineaDatosHepatitis = UtilidadGenArchivos.generarVectorInfoBasica(rs18, semana, anyo);
    	
    	for (int i=0;i<lineaDatosHepatitis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosHepatitis.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE LEISHMANIASIS
    	
    	ResultSet rs19 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaLeishmaniasis");
    	
    	Vector lineaDatosLeishmaniasis = UtilidadGenArchivos.generarVectorInfoBasica(rs19, semana, anyo);
    	
    	for (int i=0;i<lineaDatosLeishmaniasis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosLeishmaniasis.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MALARIA
    	
    	ResultSet rs20 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaMalaria");
    	
    	Vector lineaDatosMalaria = UtilidadGenArchivos.generarVectorInfoBasica(rs20, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMalaria.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMalaria.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE MENINGITIS
    	
    	ResultSet rs21 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaMeningitis");
    	
    	Vector lineaDatosMeningitis = UtilidadGenArchivos.generarVectorInfoBasica(rs21, semana, anyo);
    	
    	for (int i=0;i<lineaDatosMeningitis.size();i++) {
    		
    		lineasArchivo.add(lineaDatosMeningitis.get(i));
    	}  
    	
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA CASOS DE TOSFERINA
    	
    	ResultSet rs22 = archivosPlanosDao.consultaInfoBasica(con, semana, anyo, "vigiFichaTosferina");
    	
    	Vector lineaDatosTosferina = UtilidadGenArchivos.generarVectorInfoBasica(rs22, semana, anyo);
    	
    	for (int i=0;i<lineaDatosTosferina.size();i++) {
    		
    		lineasArchivo.add(lineaDatosTosferina.get(i));
    	}  
    	
    	

    	///////////////////////////////////////////////////
    	// Generacion de archivo de texto
    	
    	FileOutputStream out; 
        PrintStream p; 
        
        if (lineasArchivo.size()==0) {
        	return false;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
            
            for (int i=0;i<lineasArchivo.size();i++) {
            	
            	String linea = lineasArchivo.get(i).toString();
            
            	
            	p.println(linea);
            }
            
            resultado = true;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = false;
            System.err.println ("Error generando archivo plano de datos basicos : "+e.getMessage());
        }
    	
    	return resultado;
    	
    }
    
    
    
    
    
    
    
    
    

    public boolean generarArchivoBrotes(Connection con)
    {
    	String nombreArchivo = "brotes.txt";
		String ruta = UtilidadGenArchivos.rutaEpidemiologia+"secretariaDistrital/"+nombreArchivo;
		
    	boolean resultado = false;
    	Vector lineasArchivo = new Vector();
    	
    	if (semana==0) {
    		
    		String fechaActual = UtilidadFecha.getFechaActual();
    		
			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
			
			anyo = Integer.parseInt(fechaActual.split("/")[2]);
			
			if (numSemanaActual==1) {
				semana = 52;
				anyo = anyo-1;
			}
			else {
				semana = numSemanaActual-1;
			}
    	}
    	
    	//////////////////////////////////////////////////////
    	// CONSULTA DE BROTES 
    	

    	ResultSet rs1 = archivosPlanosDao.consultaBrotes(con, semana, anyo);
    	
    	Vector lineaDatosBrotes = UtilidadGenArchivos.generarVectorBrotes(rs1, semana, anyo);
    	
    	for (int i=0;i<lineaDatosBrotes.size();i++) {
    		
    		lineasArchivo.add(lineaDatosBrotes.get(i));
    	}  
    	
    	
    	

    	//////////////////////////////////////////////////////
    	// CONSULTA BROTES ETAS
    	

    	ResultSet rs2 = archivosPlanosDao.consultaBrotesEtas(con, semana, anyo);
    	
    	Vector lineaDatosEtas = UtilidadGenArchivos.generarVectorBrotesEtas(rs2, semana, anyo);
    	
    	for (int i=0;i<lineaDatosEtas.size();i++) {
    		
    		lineasArchivo.add(lineaDatosEtas.get(i));
    	}  
    

    	///////////////////////////////////////////////////
    	// Generacion de archivo de texto
    	
    	FileOutputStream out; 
        PrintStream p; 
        
        if (lineasArchivo.size()==0) {
        	return false;
        }
        
        try
        {
            out = new FileOutputStream(ruta);

            p = new PrintStream( out );
            
            for (int i=0;i<lineasArchivo.size();i++) {
            	
            	String linea = lineasArchivo.get(i).toString();
            
            	
            	p.println(linea);
            }
            
            resultado = true;

            out.close();
            p.close();
        }
        catch (Exception e)
        {
            resultado = false;
            System.err.println ("Error generando archivo plano de brotes : "+e.getMessage());
        }
    	
    	return resultado;
    	
    }
    
    
    
    
    
    public boolean consultarArchivos(Connection con)
    {
    	boolean resultado = false;
    	
    	return resultado;
    }
    
	
	public int getAnyo() {
		return anyo;
	}
	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}
	public int getSemana() {
		return semana;
	}
	public void setSemana(int semana) {
		this.semana = semana;
	}


	public boolean isCaracterizacion() {
		return caracterizacion;
	}


	public void setCaracterizacion(boolean caracterizacion) {
		this.caracterizacion = caracterizacion;
	}


	public boolean isControl() {
		return control;
	}


	public void setControl(boolean control) {
		this.control = control;
	}


	public boolean isDatosComplementarios() {
		return datosComplementarios;
	}


	public void setDatosComplementarios(boolean datosComplementarios) {
		this.datosComplementarios = datosComplementarios;
	}


	public boolean isNotificacionColectiva() {
		return notificacionColectiva;
	}


	public void setNotificacionColectiva(boolean notificacionColectiva) {
		this.notificacionColectiva = notificacionColectiva;
	}


	public boolean isNotificacionIndividual() {
		return notificacionIndividual;
	}


	public void setNotificacionIndividual(boolean notificacionIndividual) {
		this.notificacionIndividual = notificacionIndividual;
	}


	public boolean isTalento() {
		return talento;
	}


	public void setTalento(boolean talento) {
		this.talento = talento;
	}
}
