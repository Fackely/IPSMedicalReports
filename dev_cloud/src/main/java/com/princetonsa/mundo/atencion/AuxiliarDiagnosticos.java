/*
 * @(#)AuxiliarDiagnosticos.java
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

import com.princetonsa.dao.AuxiliarDiagnosticosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase es auxiliar de  Evolucion y permite consultar
 * los diagnosticos que deben postularse en una Evolucion
 * Particular
 * 
 *
 * @version 1.0, Jun 13, 2003
 */
public class AuxiliarDiagnosticos 
{

	/**
	 * Conjunto de Diagnosticos Presuntivos
	 */
	private Collection diagnosticosPresuntivos;
	
	/**
	 * Conjunto de Diagnosticos Definitivos
	 */
	private Collection diagnosticosDefinitivos;

	/**
	 * Diagnostico de Complicacion
	 */
	private Diagnostico diagnosticoComplicacion;

	/**
	 * Número de la cuenta a la que se le van
	 * a cargar los diagnosticos 
	 */
	private int numeroCuenta=0;

	/**
	 * Boolean que indica si este auxiliar diagnostico
	 * se esta trabajando con una evolución o no
	 * (caso en que se estaría trabajando con interconsulta)
	 */
	private boolean esEvolucion;

	/**
	 * DAO de este objeto, para trabajar con esta clase auxiliar y la
	 * fuente de datos
	 */
	private AuxiliarDiagnosticosDao auxiliarDiagnosticosDao;

	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void clean ()
	{
		diagnosticosPresuntivos=new ArrayList();
		diagnosticosDefinitivos=new ArrayList();
		diagnosticoComplicacion=new Diagnostico();
		//El diagnostico de complicación inicialmente
		diagnosticoComplicacion.setAcronimo("1");
		diagnosticoComplicacion.setTipoCIE(0);
		diagnosticoComplicacion.setNombre("No seleccionado");
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( auxiliarDiagnosticosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			auxiliarDiagnosticosDao = myFactory.getAuxiliarDiagnosticosDao();
			if( auxiliarDiagnosticosDao != null )
				return true;
		}

		return false;
	}
	/**
	 * Crea un nuevo objeto <code>AuxiliarDiagnosticos</code>.
	 */
	public AuxiliarDiagnosticos (int numeroCuenta, boolean esEvolucion)
	{
		clean();
		this.numeroCuenta=numeroCuenta;
		init(System.getProperty("TIPOBD"));
		this.esEvolucion=esEvolucion;
	}

	/**
	 * Crea un nuevo objeto <code>AuxiliarDiagnosticos</code>.
	 */
	public AuxiliarDiagnosticos (boolean esEvolucion)
	{
		clean();
		this.numeroCuenta=0;
		init(System.getProperty("TIPOBD"));
		this.esEvolucion=esEvolucion;
	}
	
	/**
	 * Método que carga los diagnosticos de valoración o evolución,
	 * dependiendo del caso
	 * 
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cargar(Connection con) throws SQLException
	{
		clean();
		ResultSetDecorator rs=auxiliarDiagnosticosDao.cargarDiagnosticos(con, numeroCuenta, esEvolucion);

		boolean definitivo=true;
		
		if (rs!=null)
		{
			while (rs.next())
			{
				Diagnostico diagnosticoTemporal= new Diagnostico();
				diagnosticoTemporal.setAcronimo(rs.getString("acronimoDiagnostico"));
				diagnosticoTemporal.setTipoCIE(rs.getInt("tipoCieDiagnostico"));
				diagnosticoTemporal.setNombre(rs.getString("diagnostico"));
				diagnosticoTemporal.setNumero(rs.getInt("numero"));
				diagnosticoTemporal.setPrincipal(rs.getBoolean("principal"));
	
				definitivo=rs.getBoolean("definitivo");
				if (definitivo)
				{
					diagnosticosDefinitivos.add(diagnosticoTemporal);
				}
				else
				{
					diagnosticosPresuntivos.add(diagnosticoTemporal);
				}
				
			}
			rs.close();
		}
		
		rs=auxiliarDiagnosticosDao.cargarDiagnosticoComplicacion(con, numeroCuenta);
		if (rs!=null)
		{
			if (rs.next())
			{
				//diagnostico_complicacion as codigoDiagnostico, diagnostico_complicacion_cie as tipoCieDiagnostico 
				diagnosticoComplicacion.setAcronimo(rs.getString("acronimoDiagnostico"));
				diagnosticoComplicacion.setTipoCIE(rs.getInt("tipoCieDiagnostico"));
				diagnosticoComplicacion.setNombre(rs.getString("nombre"));
			}
			else
			{
				//Si no hay ninguno, ponemos los valores por defecto del diagnostico
				//complicacion
				diagnosticoComplicacion.setAcronimo("1");
				diagnosticoComplicacion.setTipoCIE(0);
				diagnosticoComplicacion.setNombre("No seleccionado");
			}
		}
		else
		{
			//Si no hay ninguno, ponemos los valores por defecto del diagnostico
			//complicacion
			diagnosticoComplicacion.setAcronimo("1");
			diagnosticoComplicacion.setTipoCIE(0);
			diagnosticoComplicacion.setNombre("No seleccionado");
		}
		
		//Si no es evolución (es interconsulta), los diagnosticos
		//definitivos se postulan como presuntivos
		if (!esEvolucion)
		{
			this.diagnosticosPresuntivos=this.diagnosticosDefinitivos;
		}
	}
	/**
	 * @return
	 */
	public int getNumeroCuenta() {
		return numeroCuenta;
	}

	/**
	 * @param i
	 */
	public void setNumeroCuenta(int i) {
		numeroCuenta = i;
	}

	/**
	 * Método que me da acceso al diagnostico presuntivo i, de esta manera no
	 * se depende tanto de la implementación (En este momento es ArrayList)
	 * 
	 * @param indice Número del diagnostico presuntivo que deseo obtener
	 * @return
	 */
	public Diagnostico getDiagnosticoPresuntivo (int indice)
	{
		return (Diagnostico)((ArrayList)diagnosticosPresuntivos).get(indice);
	}

	/**
	 * Método que me da acceso al diagnostico definitivo i, de esta manera no
	 * se depende tanto de la implementación (En este momento es ArrayList)
	 * 
	 * @param indice Número del diagnostico definitivo que deseo obtener
	 * @return
	 */
	public Diagnostico getDiagnosticoDefinitivo (int indice)
	{
		return (Diagnostico)((ArrayList)diagnosticosDefinitivos).get(indice);
	}
	
	/**
	 * Este método retorna el número de diagnosticos definitivos
	 * @return
	 */
	public int getNumeroDiagnosticosDefinitivos()
	{
		return diagnosticosDefinitivos.size();
	}
	
	/**
	 * Este método retorna el número de diagnosticos presuntivos
	 * @return
	 */
	public int getNumeroDiagnosticosPresuntivos()
	{
		return diagnosticosPresuntivos.size();
	}
	
	/**
	 * Este método retorna el diagnostico de complicación 
	 * encontrado
	 * @return
	 */
	public Diagnostico getDiagnosticoComplicacion() 
	{
		return diagnosticoComplicacion;
	}

}
