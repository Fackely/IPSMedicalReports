package com.sysmedica.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.UtilidadFichasDao;
import com.sysmedica.mundo.FichasAnteriores;

public class UtilidadFichas {
	
	/**
    * Objeto para manejar los logs de esta clase
    */
    private static Logger logger = Logger.getLogger(UtilidadFichas.class);

	public static String codigoDx;
	public static String diagnostico;
	
//	public static int fichaPendiente = 0;
//	public static int codigoEnf = 0;
	
	public UtilidadFichasDao utilidadFichasDao;
	
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
				utilidadFichasDao = myFactory.getUtilidadFichasDao();
				wasInited = (utilidadFichasDao != null);
			}
			return wasInited;
	}
    
    
	
	public static boolean pacienteTieneFichas(Connection con, int codigoPaciente)
	{
		FichasAnteriores fichasAnteriores = new FichasAnteriores();
		
		fichasAnteriores.setCodigoDx(codigoDx);
		fichasAnteriores.setCodigoPaciente(codigoPaciente);
		fichasAnteriores.setDiagnostico(diagnostico);
		
		Collection coleccion = fichasAnteriores.consultaFichasPorPaciente(con);
		
		try {
			if (coleccion.size()>0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (NullPointerException npe) {
			return false;
		}
	}
	
	
	/**
	 * Metodo que permite eliminar todas las fichas inactivas correspondientes a una enfermedad, usuario y pacientes especificos.
	 * @param con
	 * @param loginUsuario
	 * @param codigoPaciente
	 * @param codigoEnfermedadesNotificables
	 * @return
	 */
	public static int eliminarFichasInactivas(Connection con, String loginUsuario,int codigoPaciente)
	{
		int resultado;
		
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().eliminarFichasInactivas(con, loginUsuario,codigoPaciente);
		
		return resultado;
	}
	
	/**
	 * Metodo que permite poner una ficha de vigilancia epidemiologica como ACTIVA para ser utilizado cuando se guarde la valoracion
	 * @param con
	 * @param codigoFicha
	 * @param codigoEnfermedadesNotificables
	 * @return
	 */
	public static int activarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		int resultado;
		
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().activarFichaPorCodigo(con,codigoFicha,codigoEnfermedadesNotificables);
		
		return resultado;
	}
	
	/**
	 * Metodo que permite insertar un log de tipo BD cuando se determina que el paciente ya le fue notificada una ficha de vigilancia epidemiologica con anterioridad, para un diagnostico especifico.
	 * @param con
	 * @param loginUsuario
	 * @param codigoPaciente
	 * @param acronimo
	 * @param numeroSolicitud
	 * @return
	 */
	public static int insertarLogFichasReportadas(Connection con, String loginUsuario,int codigoPaciente,String acronimo,int tipoCie,int numeroSolicitud)
	{
		int resultado;
		
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().insertarLogFichasReportadas(con,loginUsuario,codigoPaciente,acronimo,tipoCie,numeroSolicitud);
		
		return resultado;
	}
	
	/**
	 * Metodo que permite eliminar una ficha de vigilancia epidemiologica dado su codigo (consecutivo) y el codigo de la enfermedad
	 * @param con
	 * @param codigoFicha : codigo (consecutivo) de la ficha
	 * @param codigoEnfermedadesNotificables : codigo de la enfermedad a la que pertenece el diagnostico
	 * @return
	 */
	public static int eliminarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		int resultado;
		
		resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().eliminarFichaPorCodigo(con,codigoFicha,codigoEnfermedadesNotificables);
		
		return resultado;
	}
	
	
	
	/**
	 * Metodo para ser utilizado en la seleccion de diagnosticos en las valoraciones; permite saber el siguiente estado 
	 * para el flujo de epidemiologia: Devuelve 0 si no tiene que hacer nada, 1 si debe abrir una nueva ficha y 2 si debe
	 * mostrar los resultados de la busqueda de las fichas anteriores para el mismo paciente.
	 * @param con
	 * @param codigoEnfermedadNotificable
	 * @param codigoPaciente
	 * @param acronimo
	 * @return
	 */
	public static int siguienteEstadoEpidemiologia(Connection con, int codigoEnfermedadNotificable, int codigoPaciente, String acronimo)
	{
		logger.info("CODIGO ENFERMEDAD NOTIFICABLE=> "+codigoEnfermedadNotificable);
		if (codigoEnfermedadNotificable>0) {
			
			FichasAnteriores fichasAnterioresMundo = new FichasAnteriores();
			
			fichasAnterioresMundo.setCodigoPaciente(codigoPaciente);
	    	fichasAnterioresMundo.setCodigoDx(acronimo);
	    	fichasAnterioresMundo.setDiagnostico(Integer.toString(codigoEnfermedadNotificable));
	    	
	    	Collection resultados = fichasAnterioresMundo.consultaFichasPorPaciente(con);
	    	
	    	if (resultados.isEmpty()) {
	    		// Abre ficha nueva
	    		return 1;
	    	}
	    	else {
	    		// muestra resultados de la busqueda de fichas anteriores
	    		return 2;
	    	}
		}
		else {
			return 0;
		}
	}
	
	
	public static String obtenerUrlEpidemiologia(Connection con, int codigoEnfermedadNotificable, int codigoPaciente, int codigoConvenio, String acronimo, String loginUsuario)
	{
		String url = "";
		
		if (codigoEnfermedadNotificable>0) {
			
			FichasAnteriores fichasAnterioresMundo = new FichasAnteriores();
			
			fichasAnterioresMundo.setCodigoPaciente(codigoPaciente);
	    	fichasAnterioresMundo.setCodigoDx(acronimo);
	    	fichasAnterioresMundo.setDiagnostico(Integer.toString(codigoEnfermedadNotificable));
	    	
	    	Collection resultados = fichasAnterioresMundo.consultaFichasPorPaciente(con);
	    	
	    	String nombreFicha = "";
	    	String codigoFicha = "";
	    	
	    	if (resultados.isEmpty()) {
	    		// Abre ficha nueva
	    		
	    		if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaRabia) {
	    		
	    			nombreFicha = "fichaRabia.do?";
	    			codigoFicha = "&codigoFichaRabia=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaSarampion) {
	    			
	    			nombreFicha = "fichaSarampion.do?";
	    			codigoFicha = "&codigoFichaSarampion=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaRubeola) {
	    			
	    			nombreFicha = "fichaSarampion.do?";
	    			codigoFicha = "&codigoFichaSarampion=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaVIH) {
	    			
	    			nombreFicha = "fichaVIH.do?";
	    			codigoFicha = "&codigoFichaVIH=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaDengue) {
	    			
	    			nombreFicha = "fichaDengue.do?";
	    			codigoFicha = "&codigoFichaDengue=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaFiebreAmarilla) {
	    			
	    			nombreFicha = "fichaDengue.do?";
	    			codigoFicha = "&codigoFichaDengue=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaParalisisFlacida) {
	    			
	    			nombreFicha = "fichaParalisis.do?";
	    			codigoFicha = "&codigoFichaParalisis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaSifilisCongenita) {
	    			
	    			nombreFicha = "fichaSifilis.do?";
	    			codigoFicha = "&codigoFichaSifilis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaTetanosNeo) {
	    			
	    			nombreFicha = "fichaTetanos.do?";
	    			codigoFicha = "&codigoFichaTetanos=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaTuberculosis) {
	    			
	    			nombreFicha = "fichaTuberculosis.do?";
	    			codigoFicha = "&codigoFichaTuberculosis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaMortalidadMaterna) {
	    			
	    			nombreFicha = "fichaMortalidad.do?";
	    			codigoFicha = "&codigoFichaMortalidad=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaMortalidadPerinatal) {
	    			
	    			nombreFicha = "fichaMortalidad.do?";
	    			codigoFicha = "&codigoFichaMortalidad=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaInfecciones) {
	    			
	    			nombreFicha = "fichaInfecciones.do?";
	    			codigoFicha = "&codigoFichaInfecciones=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaLesiones) {
	    			
	    			nombreFicha = "fichaLesiones.do?";
	    			codigoFicha = "&codigoFichaLesiones=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaIntoxicaciones) {
	    			
	    			nombreFicha = "fichaIntoxicaciones.do?";
	    			codigoFicha = "&codigoFichaIntoxicaciones=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaRubCongenita) {
	    			
	    			nombreFicha = "fichaRubCongenita.do?";
	    			codigoFicha = "&codigoFichaRubCongenita=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaAcciOfidico) {
	    			
	    			nombreFicha = "fichaAcciOfidico.do?";
	    			codigoFicha = "&codigoFichaAcciOfidico=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaLepra) {
	    			
	    			nombreFicha = "fichaLepra.do?";
	    			codigoFicha = "&codigoFichaLepra=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaDifteria) {
	    			
	    			nombreFicha = "fichaDifteria.do?";
	    			codigoFicha = "&codigoFichaDifteria=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaEasv) {
	    			
	    			nombreFicha = "fichaEasv.do?";
	    			codigoFicha = "&codigoFichaEasv=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaEsi) {
	    			
	    			nombreFicha = "fichaEsi.do?";
	    			codigoFicha = "&codigoFichaEsi=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaEtas) {
	    			
	    			nombreFicha = "fichaEtas.do?";
	    			codigoFicha = "&codigoFichaEtas=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaHepatitis) {
	    			
	    			nombreFicha = "fichaHepatitis.do?";
	    			codigoFicha = "&codigoFichaHepatitis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaLeishmaniasis) {
	    			
	    			nombreFicha = "fichaLeishmaniasis.do?";
	    			codigoFicha = "&codigoFichaLeishmaniasis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaMalaria) {
	    			
	    			nombreFicha = "fichaMalaria.do?";
	    			codigoFicha = "&codigoFichaMalaria=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaMeningitis) {
	    			
	    			nombreFicha = "fichaMeningitis.do?";
	    			codigoFicha = "&codigoFichaMeningitis=";
	    		}
	    		else if (codigoEnfermedadNotificable==ConstantesBD.codigoFichaTosferina) {
	    			
	    			nombreFicha = "fichaTosferina.do?";
	    			codigoFicha = "&codigoFichaTosferina=";
	    		}
	    		else {
	    			
	    			nombreFicha = "fichaGenerica.do?";
	    			codigoFicha = "&codigoFichaGenerica=";
	    		}
	    		
	    		url = "../ingresarFichaEpidemiologica/"+nombreFicha+"estado=empezar&codigoPaciente="+Integer.toString(codigoPaciente)+"&codigoDiagnostico="+acronimo+"&codigoConvenio="+Integer.toString(codigoConvenio)+"&fichamodulo=false&codigoEnfNotificable="+Integer.toString(codigoEnfermedadNotificable)+"&activa=false&esPrimeraVez=true&vieneDeFichasAnteriores=false";
	    		
	    		int codigoFichaInactiva = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().consultarFichaInactiva(con,codigoEnfermedadNotificable,codigoPaciente,loginUsuario);
	    		
	    	//	if (fichaPendiente>0 && codigoEnf==codigoEnfermedadNotificable) {
	    		if (codigoFichaInactiva>0) {
	    			
	    			url = "../ingresarFichaEpidemiologica/"+nombreFicha+"estado=completar"+codigoFicha+codigoFichaInactiva+"&codigoPaciente="+Integer.toString(codigoPaciente)+"&codigoDiagnostico="+acronimo+"&codigoConvenio="+Integer.toString(codigoConvenio)+"&fichamodulo=false&codigoEnfNotificable="+Integer.toString(codigoEnfermedadNotificable)+"&activa=false&esPrimeraVez=true&vieneDeFichasAnteriores=false";
	    			
	    		}
	    		return url;
	    	}
	    	else {
	    		// muestra resultados de la busqueda de fichas anteriores
	    		url = "../fichasEpidemiologia/fichasAnteriores.do?estado=empezar&codigoPaciente="+Integer.toString(codigoPaciente)+"&codigoDx="+acronimo+"&diagnostico="+Integer.toString(codigoEnfermedadNotificable)+"&codigoConvenio="+Integer.toString(codigoConvenio)+"&fichamodulo=false";
	    		String url2 = url;
	    		
	    		url += "&url="+url2;
	    		
	    		return url;
	    	}
		}
		else {
			return "";
		}
	}
	
	
	public static boolean consultarParametrosFichas(Connection con,int codigo)
	{
		boolean resultado = false;
				
		ResultSet rs = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().consultarParametrosFichas(con,codigo);
		
		try {
			if (rs.next()) {
				resultado = true;
			}
		}
		catch (SQLException sqle) {
			return false;
		}
		
		return resultado;
	}
	
	
	public static boolean consultarParametrosServiciosExternosFichas(Connection con,int codigo)
	{
		boolean resultado = false;
				
		ResultSet rs = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().consultarParametrosFichasExternas(con,codigo);
		
		try {
			if (rs.next()) {
				resultado = true;
			}
		}
		catch (SQLException sqle) {
			return false;
		}
		
		return resultado;
	}
	
	
	
	
	public static int consultarFichaInactiva(Connection con, int codigoEnfNotificable, int codigoPaciente, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadFichasDao().consultarFichaInactiva(con,codigoEnfNotificable,codigoPaciente,loginUsuario);
	}
}
