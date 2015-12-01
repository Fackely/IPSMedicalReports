package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ConsultaPreingresosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultaPreingresosDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsultaPreingresos
{
	
	/**
	 *Atributos aperuta ingresos 
	 */

	private static Logger logger = Logger.getLogger(ConsultaPreingresos.class);
	

	
	/*-----------------------------------------------------------
	 *         		INDICES PARA EL MANEJO DE LOS MAPAS
	 ------------------------------------------------------------*/
	
	/**
	 * indices de los criterios de busqueda
	 */
	public static final String [] indicesCriterios = {"centroAten0","fechaIni1","fechaFin2","estados3","usuarioGen4","institucion5","usuarioPen6","paciente7",
													  "ingreso8","usuario9"}; 

	/**
	 * indices del listado de preingresos
	 */
	public static final String [] indicesListadoPreingresos = {"centroAtencion0_","nombreCentroAtencion1_","id2_","consecutivo3_","fechaPreingresoPen4_",
															   "horaPreingresoPen5_","usuarioPreingresoPen6_","fechaPreingresoGen7_","horaPreingresoGen8_",
															   "usuarioPreingresoGen9_","estado10_","tipoIdent11_","identPac12_","nombrePac13_","codigoPac14_"};
	
	public static final String [] indicesDetalle = {"convenioPpal0_","codigoCuenta1_","viaIngreso2_","nombreViaIngreso3_","estadoCuenta4_",
													"nombreEstadoCuenta5_","fechaPreingresoPen6_","horaPreingresoPen7_","usuarioPreingresoPen8_",
													"fechaPreingresoGen9_","horaPreingresoGen10_","usuarioPreingresoGen11_"};

	
	/*-----------------------------------------------------------
	 *         		FIN INDICES PARA EL MANEJO DE LOS MAPAS
	 ------------------------------------------------------------*/
	
	
	/*-----------------------------------------------------------
	 *         		METODOS PARA CONSULTA PREINGRESOS
	 ------------------------------------------------------------*/
	

	
	/**
	 * Se inicializa el Dao
	 */
	public static ConsultaPreingresosDao consultaPreingresosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaPreingresosDao();
	}
	
	
	public static void cargarCriterios (Connection connection, UsuarioBasico usuario, ConsultaPreingresosForm forma  )
	{
		//se cargan los centros de atencion
		forma.setCentrosAtent(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
		//se pone el centro de atencion de la sesion como seleccionado
		forma.setCriterios(indicesCriterios[0], usuario.getCodigoCentroAtencion());
		//se cargan ponen los usuarios en seleccione
		forma.setUsuarios(cargarUsuarios(connection, ConstantesIntegridadDominio.acronimoEstadoPendiente, usuario.getCodigoInstitucion()));
		
	}
	
	
	/**
	 * Metodo encargado de consultar el listado 
	 * de preingresos
	 * @author Jhony Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * 	KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- centroAten0 --> Opcional
	 * -- fechaIni1 --> Opcional
	 * -- fechaFin2 --> Opcional
	 * -- estados3 --> Opcional
	 * -- usuarioGen4 --> Opcional
	 * -- institucion5 --> Requerido
	 * -- usuarioPen6
	 * -- paciente7
	 * @return  HashMap
	 * -------------------------------
	 * KEY'S DEL MAPA RSULTADO
	 * -------------------------------
	 * -- centroAtencion0_
	 * -- nombreCentroAtencion1_
	 * -- id2_
	 * -- consecutivo3_
	 * -- fechaPreingresoPen4_
	 * -- horaPreingresoPen5_
	 * -- usuarioPreingresoPen6_
	 * -- fechaPreingresoGen7_
	 * -- horaPreingresoGen8_
	 * -- usuarioPreingresoGen9_
	 * -- estado10_
	 * -- tipoIdent11_
	 * -- identPac12_
	 * -- nombrePac13_
	 * -- codigoPac14_
	 */
	private static HashMap cargarListadoPreingresos (Connection connection, HashMap criterios )
	{
		return  consultaPreingresosDao().cargarListadoPreingresos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de obtener los usuarios de preingresos
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- estados3 --> Requerido
	 * -- institucion5 --> Requerido
	 */
	private static ArrayList<HashMap<String, Object>> obtenerUsuariosPreingreso (Connection connection, HashMap criterios)
	{
		return  consultaPreingresosDao().obtenerUsuariosPreingreso(connection, criterios);
	}
	
	
	public static ArrayList<HashMap<String, Object>> cargarUsuarios (Connection connection, String estado,String institucion)
	{
		HashMap criterios = new HashMap ();
		//estado
		criterios.put(indicesCriterios[3], estado);
		//institucion
		criterios.put(indicesCriterios[5], institucion);
		
		return obtenerUsuariosPreingreso(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de cargar el detalle
	 * del preingresos
	 * @param connection
	 * @param criterios
	 * -------------------------------
	 * KEY'S DE MAPA CRITERIOS
	 * -------------------------------
	 * -- institucion5 --> Requerido
	 * -- ingreso8 --> Requerido
	 */
	private static HashMap cargarDetallePreingreso (Connection connection, HashMap criterios )
	{
		return consultaPreingresosDao().cargarDetallePreingreso(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de cargar el detalle de los preingresos.
	 * @param connection
	 * @param ingreso
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarDetalle (Connection connection,String ingreso, String institucion)
	{
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put(indicesCriterios[5], institucion);
		//ingreso
		criterios.put(indicesCriterios[8], ingreso);
		
		return cargarDetallePreingreso(connection, criterios);
	}
	
	
	
	/**
	 * metodo encargado de 
	 * @param connection
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public static HashMap cargarListadoPreingresos (Connection connection, PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put(indicesCriterios[5], usuario.getCodigoInstitucion());
		//codigo paciente
		criterios.put(indicesCriterios[7], paciente.getCodigoPersona());
		//estados preingreso
		criterios.put(indicesCriterios[3], "'"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"','"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'");
		
		return cargarListadoPreingresos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de realizar la consulta por rangos
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @return
	 */
	public static HashMap cargarListadoPreingresosRangos (Connection connection, UsuarioBasico usuario,ConsultaPreingresosForm forma )
	{
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put(indicesCriterios[5], usuario.getCodigoInstitucion());
		//centro atencion
		criterios.put(indicesCriterios[0], forma.getCriterios(indicesCriterios[0]));
		//fecha inicial
		criterios.put(indicesCriterios[1], forma.getCriterios(indicesCriterios[1]));
		//fecha  final 
		criterios.put(indicesCriterios[2], forma.getCriterios(indicesCriterios[2]));
		//estado 
		criterios.put(indicesCriterios[3], "'"+forma.getCriterios(indicesCriterios[3])+"'");
		
		//se mete el usuario
		if (UtilidadCadena.noEsVacio(forma.getCriterios(indicesCriterios[9])+"") && !(forma.getCriterios(indicesCriterios[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
			if ((forma.getCriterios(indicesCriterios[3])+"").equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				criterios.put(indicesCriterios[6], forma.getCriterios(indicesCriterios[9]));
			else
				if ((forma.getCriterios(indicesCriterios[3])+"").equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
					criterios.put(indicesCriterios[4], forma.getCriterios(indicesCriterios[9]));
		
		return cargarListadoPreingresos(connection, criterios);
	}
	
	
	/*-----------------------------------------------------------
	 *         		FIN METODOS PARA CONSULTA PREINGRESOS
	 ------------------------------------------------------------*/
	
	
}