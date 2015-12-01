/*
 * @(#)AntecedentesToxicos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
 
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.AntecedentesToxicosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase para el manejo de todos los antecedentes tóxicos de todos los tipos del
 * paciente.
 *
 * @version 1.0, Noviembre 28, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see com.princetonsa.mundo.antecedentes.AntecedenteToxico
 * @see com.princetonsa.mundo.PersonaBasica
 */
@SuppressWarnings("rawtypes")
public class AntecedentesToxicos
{
	private Logger logger = Logger.getLogger(AntecedentesToxicos.class);
	
	/**
	 * Paciente al cual pertence este antecedente tóxico
	 */
	private PersonaBasica paciente;
	
	/**
	 * Lista con los antecedentes tóxicos predefinidos en la base de datos. Cada
	 * elemento es de tipo "AntecedenteToxico".
	 */
	private ArrayList antecedentesToxicosPredefinidos;

	/**
	 * Lista con los antecedentes tóxicos no predefinidos en la base de datos.
	 * Cada elemento es de tipo "AntecedenteToxico".
	 */	
	private ArrayList antecedentesToxicosAdicionales;
	
	
	/**
	 * Observaciones generales para antecedentes toxicos
	 */
	private String observaciones;
	
	/** antecedentesToxicosDao * */
	private AntecedentesToxicosDao antecedentesToxicosDao;
	
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;
	
	
	/**
	 * Constructor que inicializa los atributos de esta clase
	 */
	public AntecedentesToxicos()
	{
		this.init(System.getProperty("TIPOBD"));
		this.reset();				
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init (String tipoBD) 
	{
		if (antecedentesToxicosDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedentesToxicosDao = myFactory.getAntecedentesToxicosDao();
		}
	}
	
	public void reset()	
	{
		paciente = new PersonaBasica();
		this.antecedentesToxicosPredefinidos = new ArrayList();
		this.antecedentesToxicosAdicionales = new ArrayList();	
		observaciones = "";	
		this.fecha = null;
		this.hora = null;
	}	
	
	/**
	 * Retorna el paciente al cual pertence este antecedente tóxico
	 * @return PersonaBasica
	 */
	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/**
	 * Asigna el paciente al cual pertence este antecedente tóxico
	 * @param paciente The paciente to set
	 */
	public void setPaciente(PersonaBasica paciente)
	{
		this.paciente = paciente;
	}

	/**
	 * Retorna la lista con los antecedentes tóxicos predefinidos en la base de
	 * datos. Cada elemento es de tipo "AntecedenteToxico".
	 * @return ArrayList
	 */
	public ArrayList getAntecedentesToxicosPredefinidos()
	{
		return antecedentesToxicosPredefinidos;
	}

	/**
	 * Asigna la lista con los antecedentes tóxicos predefinidos en la base de
	 * datos. Cada elemento es de tipo "AntecedenteToxico".
	 * @param antecedentesToxicosPredefinidos The antecedentesToxicosPredefinidos to set
	 */
	public void setAntecedentesToxicosPredefinidos(ArrayList antecedentesToxicosPredefinidos)
	{
		this.antecedentesToxicosPredefinidos = antecedentesToxicosPredefinidos;
	}
	
	@SuppressWarnings("unchecked")
	public void addAntecedenteToxicoPredefinido(AntecedenteToxico antecedente)
	{
		this.antecedentesToxicosPredefinidos.add(antecedente);
	}
	
	public AntecedenteToxico getAntecedenteToxicoPredefinido(int indice)
	{
		return (AntecedenteToxico)this.antecedentesToxicosPredefinidos.get(indice);
	}

	/**
	 * Retorna la lista con los antecedentes tóxicos no predefinidos en la base
	 * de datos. Cada elemento es de tipo "AntecedenteToxico".
	 * @return ArrayList
	 */
	public ArrayList getAntecedentesToxicosAdicionales()
	{
		return antecedentesToxicosAdicionales;
	}

	/**
	 * Asigna la lista con los antecedentes tóxicos no predefinidos en la base
	 * de datos. Cada elemento es de tipo "AntecedenteToxico".
	 * @param antecedentesToxicosAdicionales The antecedentesToxicosAdicionales to set
	 */
	public void setAntecedentesToxicosAdicionales(ArrayList antecedentesToxicosAdicionales)
	{
		this.antecedentesToxicosAdicionales = antecedentesToxicosAdicionales;
	}

	@SuppressWarnings("unchecked")
	public void addAntecedenteToxicoAdicional(AntecedenteToxico antecedente)
	{
		this.antecedentesToxicosAdicionales.add(antecedente);
	}
	
	public AntecedenteToxico getAntecedenteToxicoAdicional(int indice)
	{
		return (AntecedenteToxico)this.antecedentesToxicosAdicionales.get(indice);
	}

	/**
	 * Retorna las observaciones generales para antecedentes toxicos
	 * @return String
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales para antecedentes toxicos
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return this.antecedentesToxicosDao.existenAntecedentes(con, codigoPaciente);
	}
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return this.antecedentesToxicosDao.insertarAntecedenteGeneral(con, codigoPaciente);
	}

	public ResultadoBoolean existenAntecedentesToxicos(Connection con, int codigoPaciente)
	{
		return this.antecedentesToxicosDao.existenAntecedentesToxicos(con, codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteToxico(Connection con, int codigoPaciente)
	{
		return this.antecedentesToxicosDao.insertar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean insertarAntecedenteToxicoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesToxicosDao.insertarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}

	public ResultadoBoolean modificarAntecedenteToxico(Connection con, int codigoPaciente)
	{
		return this.antecedentesToxicosDao.modificar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean modificarAntecedenteToxicoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesToxicosDao.modificarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}
	
	public void cargarToxicos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesToxicosDao.consultarAntecedentesToxicos(con, codigoPaciente);
		
		if( resultado != null )
		{
			try
			{
				if( resultado.next() )
				{
					observaciones = resultado.getString("observaciones");
					
					if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
						fecha = UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+"");
						hora = resultado.getString("hora");
					}
					
				}
			}
			catch(SQLException e)
			{
				logger.warn("No se puede recorrer el ResultSetDecorator con las observaciones generales de los antecedentes tóxicos \n"+e);
			}
		}		
	}
	
	
	public ArrayList cargarToxicosPredefinidos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesToxicosDao.cargarToxicosPredefinidos(con, codigoPaciente);

		if( resultado == null )
		{
			this.antecedentesToxicosPredefinidos = new ArrayList();
			return antecedentesToxicosPredefinidos;
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteToxico antecedente = new AntecedenteToxico();
				antecedente.setCodigo(resultado.getInt("codigoAntecedente"));
				antecedente.setNombre(resultado.getString("nombreAntecedente"));
				antecedente.setActual(resultado.getBoolean("habitoActual"));
				antecedente.setCantidad(resultado.getString("cantidad"));
				antecedente.setFrecuencia(resultado.getString("frecuencia"));
				antecedente.setTiempoHabito(resultado.getString("tiempo"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				antecedente.setFechaGrabacion(resultado.getString("fechaGrabacion"));
				antecedente.setHoraGrabacion(resultado.getString("horaGrabacion"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")));
					antecedente.setHora(resultado.getString("hora"));
				}
				
				
				this.addAntecedenteToxicoPredefinido(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes tóxicos predefinidos \n"+e);
			return null;
		}
		catch(NullPointerException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes tóxicos predefinidos \n"+e);
			return null;			
		}
		
		return this.antecedentesToxicosPredefinidos;
	}
	
	public ArrayList cargarToxicosAdicionales(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesToxicosDao.cargarToxicosOtros(con, codigoPaciente);
		
		if( resultado == null )
		{
			this.antecedentesToxicosAdicionales = new ArrayList();
			return antecedentesToxicosAdicionales;
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteToxico antecedente = new AntecedenteToxico();
				antecedente.setCodigo(resultado.getInt("codigoAntecedente"));
				antecedente.setNombre(resultado.getString("nombreAntecedente"));
				antecedente.setActual(resultado.getBoolean("habitoActual"));
				antecedente.setCantidad(resultado.getString("cantidad"));
				antecedente.setFrecuencia(resultado.getString("frecuencia"));
				antecedente.setTiempoHabito(resultado.getString("tiempo"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				antecedente.setFechaGrabacion(resultado.getString("fechaGrabacion"));
				antecedente.setHoraGrabacion(resultado.getString("horaGrabacion"));		
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")));
					antecedente.setHora(resultado.getString("hora"));
				}
				
				this.addAntecedenteToxicoAdicional(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes tóxicos otros \n"+e);
			return null;
		}
		
		return this.antecedentesToxicosAdicionales;
	}

	
	public void cargar(Connection con)
	{
		this.cargarToxicos(con, paciente.getCodigoPersona());
		this.cargarToxicosPredefinidos(con, paciente.getCodigoPersona());
		this.cargarToxicosAdicionales(con, paciente.getCodigoPersona());	
	}
	
	public ResultadoBoolean updateTransaccional(Connection con) throws Exception
	{
		ResultadoBoolean resultado;
			
		// Verificar si existen otros antecedentes para este paciente, si no, se debe insertar en la tabla general.
		resultado = this.existenAntecedentes(con, paciente.getCodigoPersona()); 
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteGeneral(con, paciente.getCodigoPersona());
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		
		// Verificar si existen algun registro de antecedentes Toxicos, si no, se debe insertar uno nuevo
		resultado = this.existenAntecedentesToxicos(con, paciente.getCodigoPersona());
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteToxico(con, paciente.getCodigoPersona());
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		else
		if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
		{
			resultado = this.modificarAntecedenteToxico(con, paciente.getCodigoPersona());
			
			if( !resultado.isTrue() )
				return resultado;
		}
		else
			return resultado;
		
		String estado = ConstantesBD.inicioTransaccion;
		// Insertar Toxicos medicos predefinidos
		int tamTP = this.antecedentesToxicosPredefinidos.size();
		int tamTO = this.antecedentesToxicosAdicionales.size();
		
		for(int i=0; i<tamTP; i++)
		{
			if( i!= 0 )
				estado = ConstantesBD.continuarTransaccion;

			if( i==tamTP-1 && tamTO == 0  )
				estado = ConstantesBD.finTransaccion;				
				
			AntecedenteToxico antecedente = (AntecedenteToxico) this.antecedentesToxicosPredefinidos.get(i);
			
			resultado = antecedente.existeAntecedentePredefinido(con, paciente.getCodigoPersona());
			
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarPredefinidoTransaccional(con, paciente.getCodigoPersona(), estado);
			
				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccionalPredefinido(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
				return resultado;										
		}		

		// Insertar Toxicos otros
		if( tamTP == 0 )
			estado = ConstantesBD.inicioTransaccion;
		
		for(int i=0; i<tamTO; i++)
		{
			if( i!= 0 )
				estado = ConstantesBD.continuarTransaccion;

			if( i == tamTO-1  )
				estado = ConstantesBD.finTransaccion;				
				
			AntecedenteToxico antecedente = (AntecedenteToxico) this.antecedentesToxicosAdicionales.get(i);
			
			resultado = antecedente.existeAntecedenteOtro(con, paciente.getCodigoPersona());
			
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarOtroTransaccional(con, paciente.getCodigoPersona(), estado);
				
				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccionalOtro(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
				return resultado;										
		}		
		
		return new ResultadoBoolean(true);
	}
	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return valor de hora
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora el hora para asignar
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

}
