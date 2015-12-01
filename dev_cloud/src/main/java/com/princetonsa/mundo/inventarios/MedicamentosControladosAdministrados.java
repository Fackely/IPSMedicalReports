package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import util.ConstantesBD;
import util.Utilidades;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MedicamentosControladosAdministradosDao;


public class MedicamentosControladosAdministrados {

    // DAO de este objeto, para trabajar con la fuente de datos
	private static MedicamentosControladosAdministradosDao aplicacionDao;
	
	
	// Constructor de la Clase
    public MedicamentosControladosAdministrados() {
        this.init(System.getProperty("TIPOBD"));
    }
	

	/** Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>. */
	public boolean init(String tipoBD) {
	    if (aplicacionDao == null ) { 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getMedicamentosControladosAdministradosDao();
			if(aplicacionDao!= null )
				return true;
		}
		return false;
	}


	/** Metodo que llena el HashMap con los datos de Medicamentos Controlados Administrados
	 * @param con, centroAtencion, viaIngreso, centroCosto, convenio, fechaInicial, fechaFinal, articulo, tipoCodigo
	 * @return */
	public static HashMap generarResultados(Connection con, String centroAtencion, String viaIngreso, String centroCosto, String convenio, String fechaInicial, String fechaFinal, String articulo, String tipoCodigo) {		
			
		HashMap<String, Object> criterios = new HashMap<String, Object>();

		criterios.put("centroAtencion", centroAtencion);
		criterios.put("fechaInicial", fechaInicial);
		criterios.put("fechaFinal", fechaFinal);
		criterios.put("convenio", convenio);
		criterios.put("centroCosto", centroCosto);
		criterios.put("articulo", articulo);
		criterios.put("tipoCodigo", tipoCodigo);		

		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMedicamentosControladosAdministradosDao().generarResultados(con, criterios);
	}


	/**	 * Metodo que organiza los datos del mapa para generar el archivo plano de MedicamentosControladosAdministrados
	 * @param mapa, nombreReporte, fechaInicial, fechaFinal, encabezado, usuario
	 * @return	 */
	public static StringBuffer cargarMapaMedicamentosControladosAdministrados(HashMap<String, Object> mapa, String nombreReporte, String fechaInicial, String fechaFinal, String encabezado, String usuario) {
		StringBuffer datos = new StringBuffer();
		
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("PERIODO: "+fechaInicial+" - "+fechaFinal+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
			datos.append(mapa.get("usuario_"+i) + ", " + mapa.get("numingreso_"+i) + ", " +mapa.get("fechaingresoadmin_"+i) + ", " + mapa.get("nomcentrocosto_"+i) + ", " + mapa.get("codmed_"+i) + ", " + mapa.get("nomarticulo_"+i) + ", " + mapa.get("unidadmedida_"+i) + ", " + mapa.get("cantidadart_"+i) + ", " + mapa.get("nombreconvenio_"+i) + ", " + mapa.get("viaingreso_"+i) + ", " + mapa.get("valortotal_"+i) + ", " + mapa.get("paciente_"+i) + ", " + mapa.get("idpaciente_"+i) + "\n");

		return datos;
	}
	
	
	// Mapa que se envia a la consulta de Medicamentos Controlados Administrados en el SqlBase del mismo
	public HashMap<String, Object> consultarMediControAdmin(Connection con, String centroAtencion, String viaIngreso, String centroCosto, String convenio, String fechaInicial, String fechaFinal, String articulo, String tipoCodigo, String institucion) {

		HashMap mapa = new HashMap<String, Object>();

		//Variable para separar los codigos de via de ingreso y tipo paciente
		String tmpViaIngreso[];
		tmpViaIngreso = viaIngreso.split(ConstantesBD.separadorSplit);
		
		//cargo los valores de los criterios de busqueda en un mapa
		mapa.put("centroAtencion", centroAtencion);
		mapa.put("centroCosto", centroCosto);
		mapa.put("convenio", convenio);
		mapa.put("fechaInicial", fechaInicial);
		mapa.put("fechaFinal", fechaFinal);
		mapa.put("articulo", articulo);
		mapa.put("tipoCodigo", tipoCodigo);
		mapa.put("institucion", institucion);

		if(viaIngreso.equals("") || viaIngreso.equals(null)) {
			mapa.put("viaIngresoVia", "");		//codigo de la via de ingreso
			mapa.put("viaIngresoTipoPac", "");	//codigo del paciente
		}
		else {
			mapa.put("viaIngresoVia", tmpViaIngreso[0]);		//codigo de la via de ingreso
			mapa.put("viaIngresoTipoPac", tmpViaIngreso[1]);	//codigo del paciente
		}
		
		return aplicacionDao.consultarMediControAdmin(con, mapa);
	}

}