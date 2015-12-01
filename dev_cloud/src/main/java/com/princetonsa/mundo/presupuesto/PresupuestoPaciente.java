/*
 * Creado en Dec 15, 2005
 */
package com.princetonsa.mundo.presupuesto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PresupuestoPacienteDao;
import com.princetonsa.dao.sqlbase.SqlBasePresupuestoPacienteDao;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PresupuestoPaciente
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(PresupuestoPaciente.class);
	
	/**
	 * DAO para el manejo del Presupuesto del paciente
	 */
	private PresupuestoPacienteDao presupuestoPacienteDao = null;
	
	//************************************** SECCION INFORMACION DEL RESPONSABLE ***********************************//
	/**
	 * 
	 */
	private String paquete;
	
	/**
	 * 
	 */
	private String nombrePaquete;
	
	/**
	 * 
	 */
	private String numeroContrato;
	
	/**
	 * 
	 */
	private int contrato;
	
	/**
	 * Convenio del responsable 
	 */
	private int convenio;
	
	/**
	 * Nombre del convenio
	 */
	private String nombreConvenio;
	
	//*************************** SECCION INTERVENCION *****************************************//
	/**
	 * Médico tratante
	 */
	private int medicoTratante;
	
	/**
	 * Nombre del médico tratante
	 */
	private String nombreMedicoTratante;
	
	/**
	 * Còdigo del diagnóstico en la intervenciòn
	 */
	private String diagnosticoIntervencion;
	
	/**
	 * Tipo Cie del diagnóstico
	 */
	private int cieDiagnostico;
	
	/**
	 * Nombre del diagnòstico de intervención
	 */
	private String nombreDiagnosticoIntervencion;
	
	/**
	 * Mapa para almacenar los servicios de la secciòn intervención
	 */
	private HashMap mapaServiciosIntervencion;
	
	//**********************SECCIONES SERVICIOS Y ARTICULOS***************************//
	/**
	 * Mapa para almacenar los servicios del presupuesto generado
	 */
	private HashMap mapaServicios;
	
	/**
	 * Mapa para almacenar los articulos del presupuesto generado
	 */
	private HashMap mapaArticulos;

	
	/**
	 * Número de filas existentes en el hashmap (este sirve para articulos)
	 */
	private int numeroFilasArticulos;

	/**
	 * Número de filas existentes en el hashmap (este sirve para los servicios)
	 */
	private int numeroFilasServicios;
	
	

	//----------------------------CONSTRUCTORES E INICIALIZADORES-----------------------------------------//
	 /**
	   * Constructor de la clase, inicializa en vacío todos los atributos
	   */
	public PresupuestoPaciente()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (presupuestoPacienteDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			presupuestoPacienteDao = myFactory.getPresupuestoPacienteDao();
		}	
	}
	
	/**
	 * Método que consulta los convenios que están activos y tiene un contrato vigente a la fecha actual
	 * @param con
	 * @return Collection con los convenios vigentes
	 */
	public Collection consultarConveniosVigentes (Connection con, boolean cargarContrato)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = presupuestoPacienteDao.consultarConveniosVigentes(con, cargarContrato);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar los convenios activos con contrato vigente"+e.toString());
		  coleccion=null;
		}
		return coleccion;	
	}

	/**
	 * Metodo para insertar los materiales de la peticion 
	 * @param con
	 * @param consecutivoCotizacion
	 * @throws SQLException
	 */
	public int  insertarMateriales(Connection con, int consecutivoCotizacion) throws SQLException
	{
		
		boolean inicioTrans;
		int  res=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (presupuestoPacienteDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PresupuestoPacienteDao - insertarMateriales )");
		}

		inicioTrans=myFactory.beginTransaction(con);
		String nro =  (String) this.getMapaArticulos("numeroFilasArticulos");

		for(int i=0; i<this.getNumeroFilasArticulos() ;i++)
		{

			if(!UtilidadTexto.getBoolean(mapaArticulos.get("fueEliminadoArticulo_"+i)+""))
			{    
				int codigoArticulo = Utilidades.convertirAEntero(mapaArticulos.get("codigoArticulo_"+i)+"" );
				int cantidad  = Integer.parseInt( (String)mapaArticulos.get("cantidad_"+i) );
				float valor = Float.parseFloat( (String)mapaArticulos.get("valorUnitarioResultados_"+i) );
				int esquemaTarifario=Integer.parseInt( (String)mapaArticulos.get("esquematarifario_"+i) );
				
				res = presupuestoPacienteDao.insertarMateriales (con, consecutivoCotizacion, codigoArticulo, cantidad , valor, esquemaTarifario);
				
				if (res < 1) //-Abortar la transaccion
					break;
			}
		}
		
		
		if (!inicioTrans||res<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
			
		return res;
	}

	/**
	 * Metodo para insertar los servicios del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @throws SQLException
	 */
	public int insertarServicios(Connection con, int consecutivoCotizacion) throws SQLException 
	{
		boolean inicioTrans;
		int  res=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (presupuestoPacienteDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PresupuestoPacienteDao - insertarServicios )");
		}

		inicioTrans = myFactory.beginTransaction(con);
		
		String nro = (String) mapaServicios.get("numeroFilasMapaServicios");

		if (UtilidadCadena.noEsVacio(nro))
		{
			
			for(int i=0; i< Integer.parseInt(nro); i++)
			{
				if(!(mapaServicios.get("fueEliminadoServicio_"+i)+"").equals("true"))
				{    
					int codigoServicio = Utilidades.convertirAEntero(mapaServicios.get("codigoServicio_"+i)+"" );
					int cantidad  = Integer.parseInt( (String) mapaServicios.get("cantidadSer_"+i) );
					int esquemaTarifario=Integer.parseInt( (String)mapaServicios.get("esquematarifario_"+i) );
					
					logger.info("VALOR UNITARIO["+i+"]=> "+mapaServicios.get("valor_unitario_"+i));
					if(mapaServicios.containsKey("valor_unitario_"+i) && !(UtilidadTexto.isEmpty(mapaServicios.get("valor_unitario_"+i)+"")))
					{
						float valor = Float.parseFloat( (String) mapaServicios.get("valor_unitario_"+i) );
						res = presupuestoPacienteDao.insertarServicios (con, consecutivoCotizacion, codigoServicio, cantidad , valor,esquemaTarifario);
						
					}
					else
					{
						float valor=0;
						res = presupuestoPacienteDao.insertarServicios (con, consecutivoCotizacion, codigoServicio, cantidad , valor,esquemaTarifario);
					}
					
					if (res < 1) //-Abortar la transaccion
					  break;
				}
			}

			if (!inicioTrans||res<1)
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}

		}	
	return res;
	}

	/**
	 * Mètodo que inserta la informaciòn básica del presupuesto del paciente
	 * @param con
	 * @param codigoPersona
	 * @param usuario
	 * @return consecutivoPresupuesto
	 * @throws SQLException
	 */
	
	public int insertarPresupuesto (Connection con, int codigoPersona, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (presupuestoPacienteDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (presupuestoPacienteDao - insertarPresupuesto )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoPresupuesto, usuario.getCodigoInstitucionInt());
			
		resp1=presupuestoPacienteDao.insertarPresupuesto(con, Integer.parseInt(consecutivo), codigoPersona, convenio,medicoTratante, diagnosticoIntervencion, cieDiagnostico, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), contrato, paquete);
		
		
		if (!inicioTrans||resp1<1)
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoPresupuesto, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoPresupuesto, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para cargar la información básica del presupuesto
	 * @param con
	 * @param consecutivoPresupuesto
	 * @return true si cargo bien sino retorna false
	 */
	public boolean cargarInfoPresupuesto (Connection con, int consecutivoPresupuesto)
	{
		PresupuestoPacienteDao presupuestoPacienteDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPacienteDao();
		Collection coleccion=null;
				
		try
		{	
			coleccion = presupuestoPacienteDao.cargarInfoPresupuesto (con, consecutivoPresupuesto);
			
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
					this.nombrePaquete = (col.get("nombre_paquete")+"").equals("null")  ? "" : (col.get("nombre_paquete")+"");
					this.numeroContrato = (col.get("numero_contrato")+"").equals("null")  ? "" : (col.get("numero_contrato")+"");
					this.nombreConvenio = (col.get("nombre_convenio")+"").equals("null")  ? "" : (col.get("nombre_convenio")+"");
					this.nombreMedicoTratante = (col.get("nombre_medico")+"").equals("null")  ? "" : (col.get("nombre_medico")+"");
					this.nombreDiagnosticoIntervencion = (col.get("nombre_diagnostico")+"").equals("null")  ? "" : (col.get("nombre_diagnostico")+"");
					
					return true;
				}
				else
				{
				 return false;	
				}
		}//try
	catch(Exception e)
		{
		  logger.warn("Error al Consultar la Informaciòn básica del presupuesto " +e.toString());
		  coleccion=null;
		}
	 return false;		
	}
	
	/**
	 * Método para insertar los servicios de intervención del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @return
	 * @throws SQLException
	 */
	
	public int insertarServiciosIntervencion(Connection con, int consecutivoCotizacion) throws SQLException 
	{
		boolean inicioTrans;
		int  res=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (presupuestoPacienteDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PresupuestoPacienteDao - insertarServiciosIntervencion )");
		}

		inicioTrans = myFactory.beginTransaction(con);
		
		
		String nro = (String) mapaServiciosIntervencion.get("numeroFilasMapaServicios");

		
		if (UtilidadCadena.noEsVacio(nro))
		{
			
			for(int i=0; i< Integer.parseInt(nro); i++)
			{
				if(!(mapaServiciosIntervencion.get("fueEliminadoIntervencion_"+i)+"").equals("true"))
				{    
					int codigoServicio = Integer.parseInt( (String) mapaServiciosIntervencion.get("codigoServicio_"+i) );
					
										
					res = presupuestoPacienteDao.insertarServiciosIntervencion (con, consecutivoCotizacion, codigoServicio);
					if (res < 1) //-Abortar la transaccion
					  break;
				}
			}

			if (!inicioTrans||res<1)
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}

		}	
	return res;
	}
	
	

	//********************************************* SETTERS Y GETTERS*********************************************************//

	/**
	 * @return Returns the cieDiagnostico.
	 */
	public int getCieDiagnostico()
	{
		return cieDiagnostico;
	}
	/**
	 * @param cieDiagnostico The cieDiagnostico to set.
	 */
	public void setCieDiagnostico(int cieDiagnostico)
	{
		this.cieDiagnostico = cieDiagnostico;
	}
	/**
	 * @return Returns the diagnosticoIntervencion.
	 */
	public String getDiagnosticoIntervencion()
	{
		return diagnosticoIntervencion;
	}
	/**
	 * @param diagnosticoIntervencion The diagnosticoIntervencion to set.
	 */
	public void setDiagnosticoIntervencion(String diagnosticoIntervencion)
	{
		this.diagnosticoIntervencion = diagnosticoIntervencion;
	}
	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio()
	{
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}
	
	/**
	 * @return Returns the mapaServicios.
	 */
	public HashMap getMapaServicios()
	{
		return mapaServicios;
	}
	/**
	 * @param mapaServicios The mapaServicios to set.
	 */
	public void setMapaServicios(HashMap mapaServicios)
	{
		this.mapaServicios = mapaServicios;
	}
	
	/**
	 *Obtiene el valor del mapa de servicios de acuerdo al key 
	 * @param key
	 * @return
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}
	/**
	 * @param Asigna mapaServicios
	 */
	public void setMapaServicios(String key, Object obj) {
		this.mapaServicios.put(key, obj) ;
	}
	
	/**
	 * @return Returns the mapaServiciosIntervencion.
	 */
	public HashMap getMapaServiciosIntervencion()
	{
		return mapaServiciosIntervencion;
	}
	/**
	 * @param mapaServiciosIntervencion The mapaServiciosIntervencion to set.
	 */
	public void setMapaServiciosIntervencion(HashMap mapaServiciosIntervencion)
	{
		this.mapaServiciosIntervencion = mapaServiciosIntervencion;
	}
	
	/**
	 * Obtiene el valor del mapa de servicios de interveción
	 * @param key
	 * @return
	 */
	public Object getMapaServiciosIntervencion(String key) {
		return mapaServicios.get(key);
	}
	/**
	 * @param Asigna mapaServiciosIntervencion
	 */
	public void setMapaServiciosIntervencion(String key, Object obj) {
		this.mapaServicios.put(key, obj) ;
	}
	
	/**
	 * @return Returns the medicoTratante.
	 */
	public int getMedicoTratante()
	{
		return medicoTratante;
	}
	/**
	 * @param medicoTratante The medicoTratante to set.
	 */
	public void setMedicoTratante(int medicoTratante)
	{
		this.medicoTratante = medicoTratante;
	}
		
	/**
	 * @return Retorna mapaArticulos.
	 */
	public HashMap getMapaArticulos() {
		return mapaArticulos;
	}
	/**
	 * @param Asigna mapaArticulos.
	 */
	public void setMapaArticulos(HashMap mapaArticulos) {
		this.mapaArticulos = mapaArticulos;
	}
	
	public Object getMapaArticulos(String key) {
		return mapaArticulos.get(key);
	}
	/**
	 * @param Asigna mapaArticulos.
	 */
	public void setMapaArticulos(String key, Object obj) {
		this.mapaArticulos.put(key, obj) ;
	}


	/**
	 * @return Retorna numeroFilasArticulos.
	 */
	public int getNumeroFilasArticulos() {
		return numeroFilasArticulos;
	}
	/**
	 * @param Asigna numeroFilasArticulos.
	 */
	public void setNumeroFilasArticulos(int numeroFilasArticulos) {
		this.numeroFilasArticulos = numeroFilasArticulos;
	}
	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}
	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}
	/**
	 * @return Returns the nombreDiagnosticoIntervencion.
	 */
	public String getNombreDiagnosticoIntervencion()
	{
		return nombreDiagnosticoIntervencion;
	}
	/**
	 * @param nombreDiagnosticoIntervencion The nombreDiagnosticoIntervencion to set.
	 */
	public void setNombreDiagnosticoIntervencion(
			String nombreDiagnosticoIntervencion)
	{
		this.nombreDiagnosticoIntervencion = nombreDiagnosticoIntervencion;
	}
	/**
	 * @return Returns the nombreMedicoTratante.
	 */
	public String getNombreMedicoTratante()
	{
		return nombreMedicoTratante;
	}
	/**
	 * @param nombreMedicoTratante The nombreMedicoTratante to set.
	 */
	public void setNombreMedicoTratante(String nombreMedicoTratante)
	{
		this.nombreMedicoTratante = nombreMedicoTratante;
	}

	/**
	 * @return Retorna numeroFilasServicios.
	 */
	public int getNumeroFilasServicios() {
		return numeroFilasServicios;
	}
	/**
	 * @param Asigna numeroFilasServicios.
	 */
	public void setNumeroFilasServicios(int numeroFilasServicios) {
		this.numeroFilasServicios = numeroFilasServicios;
	}

	public int getContrato() {
		return contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getNombrePaquete() {
		return nombrePaquete;
	}

	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	public static PresupuestoPacienteDao utilidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPacienteDao();
	}
	
	
	/**
	 * 
	 * @param con
	 * @param convenio 
	 * @param servicios
	 * @return
	 */
	public static HashMap obtenerPaquetesValidos(Connection con, int convenio, String servicios) 
	{
		return utilidadDao().obtenerPaquetesValidos(con,convenio,servicios);
	}
	
	/**
	 * Método que consulta los presupuesto de un paciente para unos convenios
	 * determinados cuando no tengan ingreso definido
	 * @param con
	 * @param codigoPaciente
	 * @param listadoConvenios
	 * @return
	 */
	public  static HashMap obtenerPrespuestosSinIngreso(Connection con,String codigoPaciente, String listadoConvenios)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("listadoConvenios", listadoConvenios);
		return utilidadDao().obtenerPrespuestosSinIngreso(con, campos);
	}
	
	/**
	 * Método que actualiza el ingreso de un prespuesto
	 * @param con
	 * @param idIngreso
	 * @param consecutivo
	 * @return
	 */
	public static int actualizarIngreso(Connection con,String idIngreso,String consecutivo)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		campos.put("consecutivo", consecutivo);
		return utilidadDao().actualizarIngreso(con, campos);
	}
	
	/**
	 * Método implementado para obtener el presupuesto de un ingreso
	 * @param con
	 * @param idingreso
	 * @return
	 */
	public static HashMap obtenerPresupuestoXIngreso(Connection con,String idIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		return utilidadDao().obtenerPresupuestoXIngreso(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public HashMap cargarServiciosPaquetes(Connection con, String codigoPaquete, int convenio, int contrato)
	{
		return presupuestoPacienteDao.cargarServiciosPaquetes(con,codigoPaquete,convenio,contrato);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @return
	 */
	public HashMap cargarArticulosPaquetes(Connection con, String codigoPaquete)
	{
		return presupuestoPacienteDao.cargarArticulosPaquetes(con, codigoPaquete);
	}
	
	/**
	 * Metodo para la consulta de los contratos por convenio
	 */
	public HashMap consultarContratos(Connection con,int convenio)
	{
		return presupuestoPacienteDao.consultarContratos(con, convenio);
	}
	

}
