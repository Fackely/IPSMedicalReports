/*
 * 22 Oct 2005
 *
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.Listado;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PeticionDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa los procedimientos con Peticiones Qx para las funcionalidades
 * Generar - Consultar - Modificar - Anular Petición
 */
public class Peticiones {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(Peticiones.class);
	
	/**
	 * DAO para el manejo de Peticiones de Cirugía
	 */
	private PeticionDao peticionDao = null;
	
	/**
	 * Colección de peticiones
	 */
	private Peticion[] peticiones;
	
	
	/**
	 * Tamaño de la colección de peticiones
	 */
	private int numPeticiones;
	
	
	
	
	/**
	 * Constructor
	 *
	 */
	public Peticiones()
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
		if (peticionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			peticionDao = myFactory.getPeticionDao();
		}	
	}
	
	/**
	 * Método implementado para cargar los datos generales de las peticiones de un paciente
	 * @param paciente
	 * @return
	 */
	public void cargarPeticiones(Connection con,int codigoPaciente)
	{
		HashMap peticionesMap=peticionDao.cargarDatosGeneralesPeticion(con,codigoPaciente,null);
		this.numPeticiones = Integer.parseInt(peticionesMap.get("numRegistros")+"");
		
		peticiones = new Peticion[numPeticiones];
		
		for(int i=0;i<this.numPeticiones;i++)
		{
			peticiones[i] = new Peticion();
			peticiones[i].setNumeroPeticion(Integer.parseInt(peticionesMap.get("peticion_"+i)+""));
			peticiones[i].setNumeroIngreso(peticionesMap.get("numero_ingreso_"+i)+"");
			peticiones[i].setCodigoMedicoSolicitante(Integer.parseInt(peticionesMap.get("codigo_medico_solicita_"+i)+""));
			peticiones[i].setNombreMedicoSolicitante(peticionesMap.get("medico_solicita_"+i)+"");
			peticiones[i].setFecha(peticionesMap.get("fecha_peticion_"+i)+"");
			peticiones[i].setHora(peticionesMap.get("hora_peticion_"+i)+"");
			peticiones[i].setFechaCirugia(peticionesMap.get("fecha_cirugia_"+i)+"");
			peticiones[i].setNombreEstado(peticionesMap.get("estado_"+i)+"");
			peticiones[i].setOrden(Integer.parseInt(peticionesMap.get("orden_"+i)+""));
			peticiones[i].setCodigoMedicoResponde(Integer.parseInt(peticionesMap.get("codigo_medico_responde_"+i)+""));
			peticiones[i].setNombreMedicoResponde(peticionesMap.get("medico_responde_"+i)+"");
			
		}
	}
	
	/**
	 * Método implementado para ordenar un arreglo de peticiones
	 * tomando como criterio un campo índice de ordenación
	 * @param indice
	 * @param ultimoIndice
	 * @return retorna el índice por el cual se ordenó el arreglo
	 */
	public String ordenarPeticiones(String indice,String ultimoIndice)
	{
		//columnas del listado
		String[] indices={
				"peticion_",
				"apellidos_paciente_",
				"nombres_paciente_",
				"numero_ingreso_",
				"codigo_medico_solicita_",
				"medico_solicita_",
				"fecha_peticion_",
				"hora_peticion_",
				"fecha_cirugia_",
				"orden_",
				"estado_",
				"codigo_medico_responde_",
				"medico_responde_",
			};
		
		HashMap peticionesMap = new HashMap();
		//se carga el HashMap temporal con el arreglo de peticiones
		for(int i=0;i<this.numPeticiones;i++)
		{
			peticionesMap.put("peticion_"+i,peticiones[i].getNumeroPeticion()+"");
			peticionesMap.put("codigo_medico_solicita_"+i,peticiones[i].getCodigoMedicoSolicitante()+"");
			peticionesMap.put("medico_solicita_"+i,peticiones[i].getnombreMedicoSolicitante());
			peticionesMap.put("fecha_peticion_"+i,UtilidadFecha.conversionFormatoFechaABD(peticiones[i].getFecha()));
			peticionesMap.put("hora_peticion_"+i,peticiones[i].getHora());
			peticionesMap.put("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(peticiones[i].getFechaCirugia()));
			peticionesMap.put("orden_"+i,peticiones[i].getOrden()+"");
			peticionesMap.put("estado_"+i,peticiones[i].getNombreEstado()+"");
			peticionesMap.put("codigo_medico_responde_"+i,peticiones[i].getCodigoMedicoResponde()+"");
			peticionesMap.put("medico_responde_"+i,peticiones[i].getnombreMedicoResponde());
			
		}
		
		//se ejecuta la ordenación
		peticionesMap=Listado.ordenarMapa(indices,indice,ultimoIndice,peticionesMap,this.numPeticiones);
		
		
		//se carga el arreglo de peticiones con el HashMap ordenado
		for(int i=0;i<this.numPeticiones;i++)
		{
			peticiones[i] = new Peticion();
			peticiones[i].setNumeroPeticion(Integer.parseInt(peticionesMap.get("peticion_"+i)+""));
			peticiones[i].setCodigoMedicoSolicitante(Integer.parseInt(peticionesMap.get("codigo_medico_solicita_"+i)+""));
			peticiones[i].setNombreMedicoSolicitante(peticionesMap.get("medico_solicita_"+i)+"");
			peticiones[i].setFecha(UtilidadFecha.conversionFormatoFechaAAp(peticionesMap.get("fecha_peticion_"+i)+""));
			peticiones[i].setHora(peticionesMap.get("hora_peticion_"+i)+"");
			peticiones[i].setFechaCirugia(UtilidadFecha.conversionFormatoFechaAAp(peticionesMap.get("fecha_cirugia_"+i)+""));
			peticiones[i].setOrden(Integer.parseInt(peticionesMap.get("orden_"+i)+""));
			peticiones[i].setNombreEstado(peticionesMap.get("estado_"+i)+"");
			peticiones[i].setCodigoMedicoResponde(Integer.parseInt(peticionesMap.get("codigo_medico_responde_"+i)+""));
			peticiones[i].setNombreMedicoResponde(peticionesMap.get("medico_responde_"+i)+"");
			
		}
		
		
		return indice;
	}
	

	/**
	 * @return Returns the numPeticiones.
	 */
	public int getNumPeticiones() {
		return numPeticiones;
	}
	/**
	 * @param numPeticiones The numPeticiones to set.
	 */
	public void setNumPeticiones(int numPeticiones) {
		this.numPeticiones = numPeticiones;
	}
	/**
	 * @return Returns the peticiones.
	 */
	public Peticion[] getPeticiones() {
		return peticiones;
	}
	/**
	 * @param peticiones The peticiones to set.
	 */
	public void setPeticiones(Peticion[] peticiones) {
		this.peticiones = peticiones;
	}
	
}
