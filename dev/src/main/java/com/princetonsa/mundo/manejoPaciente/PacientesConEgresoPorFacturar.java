/*
 * @author artotor
 */
package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PacientesConEgresoPorFacturarDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;
import com.servinte.axioma.mundo.fabrica.ordenes.OrdenesFabricaMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;

/**
 * 
 * @author artotor
 *
 */
public class PacientesConEgresoPorFacturar 
{
	/**
	 * 
	 */
	private HashMap pacientes;

	private PacientesConEgresoPorFacturarDao dao;
	
	
	 /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( dao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			dao= myFactory.getPacientesConEgresoPorFacturarDao();
			if( dao!= null )
				return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 *
	 */
	public PacientesConEgresoPorFacturar()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	/**
	 *
	 */
	public void reset()
	{
		this.pacientes=new HashMap();
		this.pacientes.put("numRegistros","0");
	}
	
	
	public HashMap getPacientes() {
		return pacientes;
	}

	public void setPacientes(HashMap pacientes) {
		this.pacientes = pacientes;
	}


	/**
	 * 
	 *
	 */
	@SuppressWarnings({ "rawtypes" })
	public void cargarPacientesConEgresoPorFacturar(Connection con, UsuarioBasico usuario)
	{
		//this.pacientes=dao.cargarPacientesConEgresoPorFacturar(con,usuario);
		HashMap temp=new HashMap();
		temp=dao.cargarPacientesConEgresoPorFacturar(con,usuario);
		cargarMapaPacientes(temp);
	}


	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param dtoFiltro
	 */
	@SuppressWarnings("rawtypes")
	public void cargarPacientesConEgresoPorFacturarAvanzado(Connection con,
			UsuarioBasico usuario,
			DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro) 
	{
		//this.pacientes=dao.cargarPacientesConEgresoPorFacturarAvanzado(con,usuario,dtoFiltro);
		HashMap temp=new HashMap();
		temp=dao.cargarPacientesConEgresoPorFacturarAvanzado(con,usuario,dtoFiltro);
		cargarMapaPacientes(temp);
	}
	
	
	/**
	 * Metodo que se encarga de cargar los campos en el mapa pacientes.De acuerdo a las validaciones de la consulta
	 * de órdenes de medicamentos en listado de pacientes con egreso por facturar.
	 * MT 1430
	 * @author Camilo Gómez 
	 * @param temp
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cargarMapaPacientes(HashMap temp)
	{
		int totalRegistros=Utilidades.convertirAEntero(temp.get("numRegistros")+"");
		int cont=0;
		ISolicitudesMundo solicitudesMundo=OrdenesFabricaMundo.crearSolicitudesMundo(); 
		for(int i=0;i<totalRegistros;i++)
		{//MT 1430 Validacion estado de órdenes de medicamentos en listado de pacientes con egreso por facturar
			if(solicitudesMundo.consultarSolicitudesMedicamentosPorCuenta(Utilidades.convertirAEntero(temp.get("cuenta_"+i)+"")))
			{
				this.pacientes.put("admision_"+cont,temp.get("admision_"+i));
				this.pacientes.put("fechahoraadmision_"+cont,temp.get("fechahoraadmision_"+i));
				this.pacientes.put("viaingreso_"+cont,temp.get("viaingreso_"+i));
				this.pacientes.put("fechaegreso_"+cont,temp.get("fechaegreso_"+i));
				this.pacientes.put("horaegreso_"+cont,temp.get("hora_egreso_"+i));
				this.pacientes.put("fechahoraengreso_"+cont,temp.get("fechahoraengreso_"+i));
				this.pacientes.put("area_"+cont,temp.get("area_"+i));
				this.pacientes.put("nombrepaciente_"+cont,temp.get("nombrepaciente_"+i));
				this.pacientes.put("tipoid_"+cont,temp.get("tipoid_"+i));
				this.pacientes.put("numeroid_"+cont,temp.get("numeroid_"+i));
				this.pacientes.put("nombremedico_"+cont,temp.get("nombremedico_"+i));
				this.pacientes.put("codigopaciente_"+cont,temp.get("codigopaciente_"+i));
				this.pacientes.put("descentidadsub_"+cont,temp.get("descentidadsub_"+i));
				this.pacientes.put("codigoingreso_"+cont,temp.get("codigoingreso_"+i));
				this.pacientes.put("cuenta_"+cont,temp.get("cuenta_"+i));
				cont++;
				this.pacientes.put("numRegistros", cont+"");
			}
		}
	}
}
