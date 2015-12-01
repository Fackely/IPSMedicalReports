/*
 * @(#)AntecedenteMorbidoMedico.java
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
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.AntecedentesMedicamentosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase para el manejo de todos los antecedentes de medicamentos para el
 * paciente
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class AntecedentesMedicamentos
{
	private Logger logger = Logger.getLogger(AntecedentesMedicamentos.class);
	/**
	 * Arreglo con todos los medicamentos que son antecedente.
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList medicamentos;
	
	/**
	 * Observaciones generales de antecedentes de medicamentos
	 */
	private String observaciones;
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;
	
	
	/**
	 * Medio para comunicación con la base de datos
	 */
	private AntecedentesMedicamentosDao antecedentesMedicamentosDao = null;
	
	private static AntecedentesMedicamentosDao getAntecedentesMedicamentosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesMedicamentosDao();
	}
	
	@SuppressWarnings("rawtypes")
	public AntecedentesMedicamentos()
	{
		this.medicamentos = new ArrayList();
		this.observaciones = "";
		this.init(System.getProperty("TIPOBD"));
	}
	
	public AntecedentesMedicamentos(@SuppressWarnings("rawtypes") ArrayList antMedicamentos, String observaciones)
	{
		this.medicamentos = antMedicamentos;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el objeto de acceso a la base de datos
	 * @param	String, tipoBD. Identificador único de la base de datos que se 
	 * 				está usando.
	 */
	public void init (String tipoBD) 
	{
		if (antecedentesMedicamentosDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedentesMedicamentosDao = myFactory.getAntecedentesMedicamentosDao();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void reset()
	{
		this.medicamentos = new ArrayList();
		this.observaciones = "";
	}
	
	/**
	 * Retorna el arreglo con todos los medicamentos que son antecedente.
	 * @return	 ArrayList (AntecedenteMedicamento), antecedentes de medicamentos
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList getMedicamentos()
	{
		return medicamentos;
	}

	/**
	 * Asigna el arreglo con todos los medicamentos que son antecedente.
	 * @param ArrayList (AntecedenteMedicamento), antecedentes de medicamentos
	 */
	@SuppressWarnings("rawtypes")
	public void setMedicamentos(ArrayList medicamentos)
	{
		this.medicamentos = medicamentos;
	}

	/**
	 * Retorna la definición del antecedente que corresponde al indice dado. Si
	 * no existe el indice dentro del arreglo retorna null
	 * @param indice
	 * @return AntecedenteMedicamento
	 */
	public AntecedenteMedicamento getMedicamento(int indice)
	{
		if( indice >= this.medicamentos.size() || indice < 0 )
			return null;
		else
			return (AntecedenteMedicamento)this.medicamentos.get(indice);
	}
	
	/**
	 * Asigna un nuevo antecedente de medicamento al final del arreglo
	 */
	@SuppressWarnings("unchecked")
	public void setMedicamento(AntecedenteMedicamento medicamento)
	{
		this.medicamentos.add(medicamento);
	}
	
	/**
	 * Retorna las observaciones generales de antecedentes de medicamentos
	 * @return 	String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales de antecedentes de medicamentos
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return this.antecedentesMedicamentosDao.existenAntecedentes(con, codigoPaciente);
	}
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return this.antecedentesMedicamentosDao.insertarAntecedenteGeneral(con, codigoPaciente);
	}

	public ResultadoBoolean existeAntecedenteMedicamentos(Connection con, int codigoPaciente)
	{
		return this.antecedentesMedicamentosDao.existenAntecedentesMedicamentos(con, codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteMedicamentos(Connection con, int codigoPaciente)
	{
		return this.antecedentesMedicamentosDao.insertar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean insertarAntecedenteMedicamentosTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesMedicamentosDao.insertarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}

	public ResultadoBoolean modificarAntecedenteMedicamentos(Connection con, int codigoPaciente)
	{
		return this.antecedentesMedicamentosDao.modificar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean modificarAntecedenteMedicamentosTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesMedicamentosDao.modificarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}
	
	public static HashMap<String, Object> consultaFormaConc (Connection con, int codigoArticulo)
	{
		return getAntecedentesMedicamentosDao().consultaFormaConc(con, codigoArticulo);
	}

	public void cargar(Connection con, int codigoPaciente)
	{
		this.cargarAntecedentesMedicamentos(con, codigoPaciente); 
		logger.info("cargarAntecedentesMedicamentos. Cod Paciente" + codigoPaciente);		

		
		//se keda pegado aca
		this.cargarMedicamentos(con, codigoPaciente);
		logger.info("cargarMedicamentos");		
	}	
	
	public ResultadoBoolean updateTransaccional(Connection con, int codigoPaciente) throws Exception
	{
		ResultadoBoolean resultado;
			
		// Verificar si existen otros antecedentes para este paciente, si no, se debe insertar en la tabla general.
		resultado = this.existenAntecedentes(con, codigoPaciente); 
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteGeneral(con, codigoPaciente);
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}

		// Verificar si existen algun registro de antecedentes medicamentos, si no, se debe insertar uno nuevo
		resultado = this.existeAntecedenteMedicamentos(con, codigoPaciente);
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteMedicamentos(con, codigoPaciente);
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		else
		if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
		{
			resultado = this.modificarAntecedenteMedicamentos(con, codigoPaciente);
			
			if( !resultado.isTrue() )
				return resultado;
		}
		else
			return resultado;

		// Insertar/Actualizar medicamentos
		int tamM = this.medicamentos.size();
		String estado = ConstantesBD.inicioTransaccion;
		
		for(int i=0; i<tamM; i++)
		{
			if( i!=0  )
				estado = ConstantesBD.continuarTransaccion;
				
			if( i==tamM-1 )
				estado = ConstantesBD.finTransaccion;
				
			AntecedenteMedicamento antecedente = (AntecedenteMedicamento) this.medicamentos.get(i);
				
			resultado = antecedente.existeAntecedente(con, codigoPaciente);
				
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarTransaccional(con, codigoPaciente, estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;					
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccional(con, codigoPaciente, estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;				
			}
			else
				return resultado;													
		}
	
		return new ResultadoBoolean(true);
	}

	public void cargarAntecedentesMedicamentos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMedicamentosDao.consultarAntecedentesMedicamentos(con, codigoPaciente);
		
		if( resultado != null )
		{
			try
			{
				if( resultado.next() )
				{
					observaciones = resultado.getString("observaciones"); 
					hora = resultado.getString("hora");
					fecha = UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+"");
				}
			}
			catch(SQLException e)
			{
				logger.warn("No se puede recorrer el ResultSetDecorator con las observaciones, fecha y hora generales de los antecedentes medicamentos \n"+e);
			}
		}				
	}
	
	
	@SuppressWarnings("rawtypes")
	public void cargarMedicamentos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMedicamentosDao.consultarMedicamentos(con, codigoPaciente);
		
		if( resultado == null )
		{
			logger.info("Resultado es nulo!");
			this.medicamentos = new ArrayList();
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteMedicamento antecedente = new AntecedenteMedicamento();
				antecedente.setCodigo(resultado.getInt("codigo"));
				antecedente.setCodigoA(resultado.getString("codigoa"));
				antecedente.setNombre(resultado.getString("nombre"));
				antecedente.setDosis(resultado.getString("dosis"));
				antecedente.setFrecuencia(resultado.getString("frecuencia"));
				antecedente.setTipoFrecuencia(resultado.getString("tipofrecuencia"));
				antecedente.setUnidosis(resultado.getString("unidosis"));
				
				antecedente.setDosisD(resultado.getString("dosisd"));
				antecedente.setCantidad(resultado.getString("cantidad"));
				antecedente.setTiempoT(resultado.getString("tiempot"));
				antecedente.setFechaInicio(resultado.getString("fechaInicio"));
				antecedente.setFechaFin(resultado.getString("fechaFin"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+""));
					antecedente.setHora(resultado.getString("hora"));
				}
				
				this.setMedicamento(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los medicamentos \n"+e);
			this.medicamentos = null;
		}
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
