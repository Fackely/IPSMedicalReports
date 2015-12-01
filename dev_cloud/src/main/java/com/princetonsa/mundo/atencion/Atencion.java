/*
 * @(#)Atencion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;


import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import org.apache.log4j.Logger;
/**
 * Esta clase 
 *
 * @version 1.0, Sept. 09, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>.
 */

public class Atencion {
	
	private static final int CODIGO_HOSPITALIZACION = ConstantesBD.codigoViaIngresoHospitalizacion;
	//private static final int CODIGO_AMBULATORIOS = ConstantesBD.codigoViaIngresoAmbulatorios;		
	private static final int CODIGO_URGENCIAS = ConstantesBD.codigoViaIngresoUrgencias;	
	//private static final int CODIGO_CONSULTA_EXTERNA = ConstantesBD.codigoViaIngresoConsultaExterna;

	private static final int CODIGO_ESPECIALIDAD_GENERAL = 0;	
	private static final int CODIGO_ESPECIALIDAD_PEDIATRICA = 3;

	private static final int CODIGO_REMISION_HOSPITALIZACION = 3;
	private static final int CODIGO_REMISION_URGENCIAS = 2;	 
					
	private int codigoCuenta;
	
	/**
	 * Nombre de la via de ingreso que puede corresponder a: 
	 * 'Hospitalizacion', 'Urgencias', 'ConsultaExterna', 'Ambulatorios'
	 */
	private String nombreViaIngreso;
	private int codigoViaIngreso;
	
	/**
	 * Para Urgencias: Si origen de admisión es '2-remitido'
	 * Para Hospitalización: Si origen de admisión es '3-remitido'
	 * Para ConsultaExterna: ¿?
	 * Para Ambulatorios: ¿? 
	 */
	private String vieneRemitido;
	
	/**
	 * Lista de las valoraciones con sus correspondientes evoluciones.
	 * Ejemplo: 
	 * ValoracionUrgencias con sus EvolucionesUrgencias 
	 * ValoracionesHospitalizacion con sus EvolucionesHopitalizacion
	 * ValoracionesInterconsulta con sus EvolucionesInterconsulta 
	 */
	private ArrayList datosClinicos;
	
	/**
	 * Lista de objetos tipo 'Diagnostico'
	 * Diagnosticos dados al paciente en esta atencion que se le presto.
	 * Si corresponde a :
	 * Para Hospitalización: Diagnóstico de ingreso y diagnóstico principal de egreso.	 
	 * Para Urgencias; Diagnóstico ppal de egreso. Si no tiene egreso, pues no entro a cama de observación se toma ¿?
	 * Para Consulta Externa: De la única valoración de consulta externa que se le hace
	 * Para Ambulatorios: ¿?
	 */
	private ArrayList diagnosticos;
	
	/**
	 * Para Hospitalización y Urgencias: fecha y hora de la admisión
	 * Para Consulta Externa: fecha y hora de inicio de la cita médica
	 * Para Ambulatorios: ¿?
	 * 
	 */
	private String fechaInicioAtencion;
	private String horaInicioAtencion;
	
	/**
	 * Para Hospitalizacion: fecha y hora del egreso
	 * Para Urgencias: fecha y hora del egreso si lo hay, o ¿?
	 * Para ConsultaExterna: fecha y hora de fin de la cita médica.
	 * Para Ambulatorios : ¿?
	 */
	private String fechaFinAtencion;
	private String horaFinAtencion;	
	private DaoFactory myFactory;
	private Logger logger = Logger.getLogger(Atencion.class);
	
	public Atencion()
	{
		clean();		
		init(System.getProperty("TIPOBD"));
		
	}
	public Atencion(Connection con, int codigoCuenta)
	{
		clean();
		init(System.getProperty("TIPOBD"));
		this.codigoCuenta = codigoCuenta;
		this.cargar(con);
	}
	
	public void clean()
	{
		this.codigoCuenta = -1;
		this.nombreViaIngreso = "";
		this.codigoViaIngreso = -1;		
		this.vieneRemitido = "";
		this.datosClinicos = new ArrayList();
		this.diagnosticos = new ArrayList();
		this.fechaInicioAtencion = "";
		this.horaInicioAtencion = "";
		this.fechaFinAtencion = "";
		this.horaFinAtencion = "";
	}
	public void init(String tipoBD)
	{
		myFactory = DaoFactory.getDaoFactory(tipoBD);
	}
	//Retorna true si se pudo cargar exitosamente
	public boolean cargar (Connection con)
	{
		//Cargando los datos clinicos
		ResultSetDecorator rs_egreso, rs_admision;
		Object[] admision;				
		int codigoOrigen = -1;
		Diagnostico diagPpal;

		//CARGANDO DATOS INICIALES SEGUN VIA DE INGRESO
		//Caso Hospitalizacion  o urgencias
		admision = this.cargarIdAdmision(con, this.codigoCuenta);		
		//Caso interconsulta o ambulatorios ¿?
		
		//Si paciente en Hospitalizacion o Urgencias
		if(admision != null)
		{
			this.codigoViaIngreso = ((Integer)admision[0]).intValue();
			this.nombreViaIngreso = (String)admision[1];
			rs_admision = (ResultSetDecorator) admision[2];	
			//rs_admision != null, por construccion
			try
			{			
				this.fechaInicioAtencion = rs_admision.getString("fechaAdmision");
				this.horaInicioAtencion = rs_admision.getString("horaAdmision");
				codigoOrigen = rs_admision.getInt("origenAdmision");
				this.vieneRemitido = "false";
							
				if (this.codigoViaIngreso == CODIGO_URGENCIAS && codigoOrigen == CODIGO_REMISION_URGENCIAS) this.vieneRemitido = "true";
				if (this.codigoViaIngreso == CODIGO_HOSPITALIZACION && codigoOrigen == CODIGO_REMISION_HOSPITALIZACION) this.vieneRemitido = "true";
				this.diagnosticos.clear();
				//cargarValsEvolsDiag(con);
				//Obteniendo los datos del egreso
				try					
				{											
					rs_egreso = myFactory.getEgresoDao().getBasicoEgreso(con, this.codigoCuenta);
					if(rs_egreso != null && rs_egreso.next() )
					{							
						this.fechaFinAtencion = rs_egreso.getString("fechaEgreso");
						this.horaFinAtencion = rs_egreso.getString("horaEgreso");
						diagPpal = new Diagnostico();
						diagPpal.setNombre(rs_egreso.getString("nombreDiagnosticoPrincipal"));
						diagPpal.setAcronimo(rs_egreso.getString("codigoDiagnosticoPrincipal"));
						diagPpal.setTipoCIE(rs_egreso.getInt("codigoDiagnosticoPrincipalCie"));						
						this.diagnosticos.add(diagPpal);							
					}
				}
				catch (SQLException sql2)
				{
					
					
						logger.warn(sql2);
						
				
					return false;
				}
			}
			catch (SQLException sql1)
			{
				logger.warn(sql1);
					
			
				return false;
			}															
			return true;
		}
		return false;
	}
	
	
	/**
	 * Se organizan las valoraciones con sus respectivas evoluciones según el centro de costo
	 * @param valoracionInicial
	 * @param valoracionesInterconsultas
	 * @param evoluciones
	 * @return
	 */
	public ArrayList organizarValoracionesEvoluciones(HashMap valoracionInicial, Collection valoracionesInterconsultas, Collection evoluciones )
	{
		//Todas las valoraciones con sus evoluciones agrupadas por centros de costo
		ArrayList datosClinicosOrganizados = new ArrayList();
		//Arreglo de maximo dos posiciones. En la primera tiene las valoraciones y en la segunda las evoluciones del mismo centro de costo
		ArrayList valsEvols = new ArrayList();
		//Lista con las valoraciones de un mismo centro de costo
		ArrayList vals = new ArrayList();
		//Lista con las evoluciones de un mismo centro de costo
		ArrayList evols = new ArrayList();				
		
		int centroCostoValoracion = -1, centroCostoAnt = -1;
		Iterator iter_valoracionesInterconsulta;
		HashMap valoracion = null;
		boolean cambioCentroCosto = false;
		centroCostoValoracion = ((Integer)valoracionInicial.get("codigocentrocosto")).intValue();
		vals.add(valoracionInicial);

		//Encontrar las evoluciones asociadas a la valoracionInicial
		evols = obtenerEvolucionesCentroCosto(centroCostoValoracion, evoluciones);
		valsEvols.add(vals);
		valsEvols.add(evols);
		datosClinicosOrganizados.add(valsEvols);				
		//Iterando sobre las valoraciones de interconsulta
		centroCostoAnt = -1;
		centroCostoValoracion = -1;
		vals = new ArrayList();
		iter_valoracionesInterconsulta = valoracionesInterconsultas.iterator();
		while(iter_valoracionesInterconsulta.hasNext())
		{				
			while(iter_valoracionesInterconsulta.hasNext() && !cambioCentroCosto)
			{
				valoracion = (HashMap)iter_valoracionesInterconsulta.next();
				centroCostoValoracion = ((Integer)valoracion.get("codigocentrocosto")).intValue();													
				if( centroCostoAnt == -1 || (centroCostoValoracion == centroCostoAnt))
				{				
					vals.add(valoracion);
					//iter_valoracionesInterconsulta.remove();
					centroCostoAnt = centroCostoValoracion;					
				}
				else cambioCentroCosto = true;				
			}
			valsEvols = new ArrayList();			
			valsEvols.add(vals);
			valsEvols.add(obtenerEvolucionesCentroCosto(centroCostoAnt, evoluciones));
			datosClinicosOrganizados.add(valsEvols);
			
			//La valoracion con nuevo centro de costo
			vals = new ArrayList();			
			vals.add(valoracion);
			//iter_valoracionesInterconsulta.remove();
			centroCostoAnt = centroCostoValoracion;
			cambioCentroCosto = false;
		}
		return datosClinicosOrganizados;
	}
	/**
	 * Obtiene el listado de evoluciones pertenecientes a un mismo centro de costo y las remueve de la coleccion dada por parametro
	 * @param centroCosto
	 * @param iter_evoluciones
	 * @return
	 */
	private ArrayList obtenerEvolucionesCentroCosto(int centroCosto, Collection evoluciones)
	{
		boolean encontreCentroCosto = false, cambioCentroCosto = false;
		HashMap evolucion = null;
		//Lista con las evoluciones de un mismo centro de costo
		ArrayList evols = new ArrayList();				
		int centroCostoEvolucion = -1;	
		Iterator iter_evoluciones; 
		iter_evoluciones = evoluciones.iterator();				
		//Encontrar la primera evolucion con el centro de costo dado
		while(iter_evoluciones.hasNext() && !encontreCentroCosto)
		{
			evolucion = (HashMap)iter_evoluciones.next();
			centroCostoEvolucion = ((Integer)evolucion.get("codigocentrocosto")).intValue();
			
			//Una vez se ha encontrado la primera evolucion asociada a ese centro de costo. Empiezo a agrupar
			//todas las que corresponden al mismo centro de costo						
			if( centroCosto == centroCostoEvolucion )
			{				
				evols.add(evolucion);
				iter_evoluciones.remove();
				while(iter_evoluciones.hasNext() && !cambioCentroCosto)
				{
					evolucion = (HashMap)iter_evoluciones.next();
					centroCostoEvolucion = ((Integer)evolucion.get("codigocentrocosto")).intValue();													
					if(centroCostoEvolucion == centroCosto)
					{				
						evols.add(evolucion);
						iter_evoluciones.remove();
					}
					else cambioCentroCosto = true;
				}			
				encontreCentroCosto = true;
			}					
		}
		return evols;
		
	}
	
	
	/**
	 * Debe contener los siguientes datos
	 * codigoViaIngreso int
	 * nombreViaIngreso String
	 * Y ResultSetDecorator que contiene las siguientes columnas:
	 * idAdmision int 
	 * fechaAdmision String
	 * horaAdmision String
	 * origenAdmision int
	 * @param con
	 * @param numeroCuenta
	 * @return
	 */
	public Object[] cargarIdAdmision(Connection con, int numeroCuenta)
	{
		ResultSetDecorator admision = null;
		try
		{		
			//primero busco en hospitalizacion
			admision = myFactory.getAdmisionHospitalariaDao().getBasicoAdmision(con, numeroCuenta);			
			if (admision != null && admision.next()) 
				return new Object[]{new Integer(CODIGO_HOSPITALIZACION), "Hospitalización", admision}; 
			//Si no se encontro, busco en urgencias			
			admision = myFactory.getAdmisionUrgenciasDao().getBasicoAdmision(con, numeroCuenta);
			if (admision != null && admision.next()) 
				return new Object[]{new Integer(CODIGO_URGENCIAS), "Urgencias", admision};
		}
		catch (SQLException sql)
		{
				logger.warn(sql);
		}					
		return null;
	}
	
	/**
	 * Pre: idValoracion es el codigo de una valoracion respondida, es decir que existe como numero de solicitud y ademas se le
	 * ha llenado un registro en la tabla de valoraciones
	 * posicion 0 => 'codigoEspecialidad'
	 * posicion 1 => 'nombreEspecialidad'  
	 * @param con
	 * @param idValoracion
	 * @return
	 */
	public String[] obtenerEspecialidadValoracion(Connection con, int idValoracion) 
	{
				
		//Sino se encuentra en ninguna especialidad se considera con especialidad general
		return new String[]{""+CODIGO_ESPECIALIDAD_GENERAL, "General"};
	}
	

	/**
	 * Modifica la coleccion dada por parametro
	 * @param con
	 * @param valoraciones
	 */
	public void completarEspecialidadValoraciones(Connection con, Collection valoraciones)
	{
		if(valoraciones == null) return;
				
		String[] especialidad;		
		HashMap valoracion;
		Iterator iter = valoraciones.iterator();		
		
		while(iter.hasNext())
		{
			valoracion = (HashMap)iter.next();
			//Busco en cada una de las especialidades		
			especialidad = obtenerEspecialidadValoracion(con, ((Integer)valoracion.get("idvaloracion")).intValue());
			valoracion.put("codigoespecialidad", especialidad[0]);
			valoracion.put("nombreespecialidad", especialidad[1]);								
		}		
	}
	/**
	 * Collection de HashMaps. Organizados por centro de Costo y numero de Solicitud
	 * Cada HashMap debe tener:
	 * idEvolucion int
	 * FechaEvolucion String
	 * HoraEvolucion String 
	 * codigoCentroCosto int 
	 * nombreCentroCosto String 
	 * @param numeroCuenta
	 * @return
	 */	
	
	public Collection cargarIdEvoluciones(Connection con, int numeroCuenta)
	{
		try
		{		
			//Deben estar ordenadas por Centro de Costo y por numero de solicitud 
			ResultSetDecorator rs = myFactory.getEvolucionDao().getBasicoEvolucionesRespondidas(con, numeroCuenta);
			return UtilidadBD.resultSet2Collection(rs);
		}
		catch (SQLException sql)
		{
			
				logger.warn(sql);
			
			
		}		
		return null;
	}	
	/**
	 * @return
	 */
	public int getCodigoCuenta() {
		return codigoCuenta;
	}

	/**
	 * @return
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @return
	 */
	public ArrayList getDatosClinicos() {
		return datosClinicos;
	}

	/**
	 * @return
	 */
	public ArrayList getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @return
	 */
	public String getFechaFinAtencion() {
		return fechaFinAtencion;
	}

	/**
	 * @return
	 */
	public String getFechaInicioAtencion() {
		return fechaInicioAtencion;
	}

	/**
	 * @return
	 */
	public String getHoraFinAtencion() {
		return horaFinAtencion;
	}

	/**
	 * @return
	 */
	public String getHoraInicioAtencion() {
		return horaInicioAtencion;
	}

	/**
	 * @return
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	/**
	 * @return
	 */
	public String getVieneRemitido() {
		return vieneRemitido;
	}

	/**
	 * @param i
	 */
	public void setCodigoCuenta(int i) {
		codigoCuenta = i;
	}

	/**
	 * @param i
	 */
	public void setCodigoViaIngreso(int i) {
		codigoViaIngreso = i;
	}

	/**
	 * @param list
	 */
	public void setDatosClinicos(ArrayList list) {
		datosClinicos = list;
	}

	/**
	 * @param list
	 */
	public void setDiagnosticos(ArrayList list) {
		diagnosticos = list;
	}

	/**
	 * @param string
	 */
	public void setFechaFinAtencion(String string) {
		fechaFinAtencion = string;
	}

	/**
	 * @param string
	 */
	public void setFechaInicioAtencion(String string) {
		fechaInicioAtencion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraFinAtencion(String string) {
		horaFinAtencion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraInicioAtencion(String string) {
		horaInicioAtencion = string;
	}

	/**
	 * @param string
	 */
	public void setNombreViaIngreso(String string) {
		nombreViaIngreso = string;
	}

	/**
	 * @param string
	 */
	public void setVieneRemitido(String string) {
		vieneRemitido = string;
	}

}