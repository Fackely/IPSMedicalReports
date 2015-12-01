/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.dao.postgresql.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseRegistroEnfermeriaDao;
import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PostgresqlRegistroEnfermeriaDao implements RegistroEnfermeriaDao
{

	/**
	 * Método para ingresar los datos del soporte respiratorio
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */ 
	public int ingresarSoporteRespiratorio(Connection con, int codEncabezado, Vector soporteRespiratorio)
	{
		return SqlBaseRegistroEnfermeriaDao.ingresarSoporteRespiratorio(con, codEncabezado, soporteRespiratorio);
	}

	/**
	 * Método para ingresar los diagnosticos de enfermería
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarDiagnosticosNanda(Connection con, int codEncabezado, Vector diagnosticosEnfermeria)
	{
		return SqlBaseRegistroEnfermeriaDao.ingresarDiagnosticosNanda(con, codEncabezado, diagnosticosEnfermeria);
	}

	/**
	 * Método para consultar el historico de los diagnosticos de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap consultarDiagnosticosNanda(Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarDiagnosticosNanda(con, cuentas, institucion, fechaInicio, fechaFin);
	}

	/**
	 * Método para consultar los históricos de
	 * cuidados de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return Collection con los registros consultados
	 */
	public HashMap consultarCuidadosEnfermeria(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCuidadosEnfermeria(con, cuentas,fechaInicio,fechaFin);
	}

	/**
	 * Método para consultar el soporte respiratorio
	 * ingresado en la orden médica
	 * @param cuenta Código de la cuenta del paciente
	 * @return HashMap con los datos del sporte respiratorio
	 */
	public HashMap consultarSoporteOrden(Connection con, String cuentas, String fechaInicio, boolean incluirAnterior, String fechaFin)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarSoporteOrden(con, cuentas, fechaInicio, incluirAnterior, fechaFin);
	}

	/**
	 * Consultar Histroricos de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param fechaInicio
	 * @return
	 */
	public HashMap consultarHistoricoSoporteEnfer(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarHistoricoSoporteEnfer(con, cuentas, fechaInicio, fechaFin);
	}

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public Collection consultarFechasHistoricoSoporte(Connection con, String cuentas)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarFechasHistoricoSoporte(con, cuentas);
	}

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de diagnósticos de enfermería
	 * @param con
	 * @param cuenta
	 * @return Colección de fechas
	 */
	public Collection consultarFechasHistoricoNanda(Connection con, String cuentas)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarFechasHistoricoNanda(con, cuentas);
	}

	/**
	 * Metodo para insertar en la tabla principal sobre el registro de enfermeria.
	 */
	public  int insertarRegistroEnfermeria(Connection con,  int cuenta)
	{
		String secuencia="nextval('seq_registro_enfermeria') as codigo";
		return SqlBaseRegistroEnfermeriaDao.insertarRegistroEnfermeria(con,  secuencia, cuenta);
	}
	
	
	/**
	 * Metodo para insertar los datos del encabezado de registro de enfermeria
	 * @param con
	 * @param codigoRegistroEnfermeria
	 * @param loginUsuario
	 * @param datosMedico
	 * @param FechaRegistro
	 * @param HoraRegistro
	 * @return
	 */														 
	public int insertarEncabezadoRegistroEnfermeria(Connection con, int codigoRegistroEnfermeria, String fechaRegistro, String horaRegistro, String loginUsuario, String datosMedico, String obsSoporte)
	{
		String secuencia="nextval('seq_enca_histo_registro_enfer') as codigo";
		return SqlBaseRegistroEnfermeriaDao.insertarEncabezadoRegistroEnfermeria(con, codigoRegistroEnfermeria,  secuencia, fechaRegistro, horaRegistro, loginUsuario, datosMedico, obsSoporte);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao#consultarPacientesPiso(java.sql.Connection, java.util.HashMap)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public HashMap consultarPacientesPiso(Connection con, HashMap campos)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarPacientesPiso (con, campos);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao#consultarPacientesCentroCosto(java.sql.Connection, java.util.HashMap)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public HashMap consultarPacientesCentroCosto(Connection con, HashMap campos) {
		return SqlBaseRegistroEnfermeriaDao.consultarPacientesCentroCosto(con, campos);
	}
	
    /**
     * Metodo para consultar y cargar la información de la dieta 
     * @param con
     * @param codigoCuenta
     * @return Collection con los datos de la última dieta
     */
    public Collection cargarDieta(Connection con, String cuentas, int tipoConsulta)
    {
    	return SqlBaseRegistroEnfermeriaDao.cargarDieta(con, cuentas, tipoConsulta);
    }

	/**
	 * Metodo para Finalizar Turno de enfermeria.
	 * @param con
	 * @param codigoRegistroEnferia
	 * @return
	 */
	public int finalizarTurnoBalanceLiquidos(Connection con, int codigoRegistroEnferia,  String fechaRegistro, String horaRegistro, String loginUsuario)
    {
		String secuencia="  nextval('seq_fin_turno_balance_liq') as codigo ";
    	return SqlBaseRegistroEnfermeriaDao.finalizarTurnoBalanceLiquidos(con, secuencia, codigoRegistroEnferia, fechaRegistro, horaRegistro, loginUsuario); 
    }
    
	/**
	 * Metodo para consultar los historicos de dieta 
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param institucion
	 * @param nroConsulta
	 * @param fechaUltimoFinTurno
	 * @param fechaInicio
	 * @return
	 */
	public HashMap cargarHistoricosDieta(Connection con, String cuentas, int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno, String fechaInicio)
    {
    	return SqlBaseRegistroEnfermeriaDao.cargarHistoricosDieta(con,  cuentas, centroCosto, institucion, nroConsulta, fechaUltimoFinTurno, fechaInicio); 
    }

    
	/**
	 * Metodo para insertar la parte de liquiquidos y medicamentos de infusion de la seccion Dieta. 
	 * @param con
	 * @param codigoRegistroEnferia
	 * @param descripcion 
	 * @param volumen
	 * @param velosidad 
	 * @param Suspendido
	 * @return
	 */
	public int insertarLiqMedInfusion(Connection con, int codigoCuenta,  int codigoRegistroEnferia, String descripcion, String volumen, String velocidad, boolean suspendido, int tipoOperacion)
    {
		String secuencia="nextval('seq_medicamento_infusion_enfer') as codigo";
    	return SqlBaseRegistroEnfermeriaDao.insertarLiqMedInfusion(con, codigoCuenta, secuencia, codigoRegistroEnferia, descripcion, volumen, velocidad, suspendido, tipoOperacion); 
    }

	/**
	 * Metodo para insertar el detalle del la finalizacion del turno.
	 * @param con
	 * @param codigoTurno
	 * @param codigoMedicamento
	 * @param valor
	 * @param tabla
	 * @return
	 */
	public int insertarDetFinTurnoBalanceLiquidos(Connection con, int codigoTurno, int codigoMedicamento, float valor, int tabla)
    {
    	return SqlBaseRegistroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con,  codigoTurno, codigoMedicamento,  valor,  tabla); 
    }

	/**
	 * Metodo para saber la ultima fecha donde se finalizo el turno de la enfermera. 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String ultimaHoraFinTurno(Connection con, int codigoCuenta)
	{
		return SqlBaseRegistroEnfermeriaDao.ultimaHoraFinTurno(con,  codigoCuenta);  
	}

    
    /**
 	 * Funcion para retornar una collecion con el listado de los tipos parametrizados
 	 * por centro de costo e institución en el registro de enfermería 
 	 * @param con
     * @param codigo de la institucion
     * @param codigo del centroCosto
     * @param codigoCuenta
     * @param cuentaAsocio
     * @param Nro Consulta parametro que indica la informacion a sacar
 	 *        1  Listado de tipos de signos vitales de enfermería        
     * @return Collection 
 	 */

      public Collection consultarTiposInstitucionCCosto(Connection con, int institucion, String cuentas,int nroConsulta)
      {
      	return SqlBaseRegistroEnfermeriaDao.consultarTiposInstitucionCCosto (con, institucion, cuentas, nroConsulta);
      }
      
      /**
       * Metodo para traer un mapa con el listado de liquidos y medicamentos registrados para
       * un paciente especifico. 
       * @param con
       * @param codigoCuenta
       * @return
       */
        public HashMap consultarLiqMedicamentosPaciente (Connection con, String cuentas, int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno,  String fechaInicio)      
        {
        	return SqlBaseRegistroEnfermeriaDao.consultarLiqMedicamentosPaciente (con, cuentas, centroCosto, institucion, nroConsulta, fechaUltimoFinTurno, fechaInicio);	
        }

    	/**
    	 * Metodo para guardar el detalle del balance de liquidos
    	 * @param con
    	 * @param parametrizado
    	 * @param codEncabezado
    	 * @param codigoMedicamento
    	 * @param valor
    	 * @return
    	 */
    	public int insertarBalanceLiquidos(Connection con, int parametrizado, int codEncabezado, int codigoMedicamento, float valor)
        {
        	return SqlBaseRegistroEnfermeriaDao.insertarBalanceLiquidos (con, parametrizado,  codEncabezado, codigoMedicamento, valor);	
        }
        
        

      /**
  	 * Método para insertar los exámenes fìsicos del registro de enfermería
  	 * @param con una conexion abierta con una fuente de datos
  	 * @param codRegEnfer
  	 * @param examenFisicoCcIns
  	 * @param valorExamenFisico
  	 * @return 
  	 */
  	public int insertarExamenesFisicos (Connection con, int codRegEnfer, int examenFisicoCcIns, String valorExamenFisico)
  	{
  		return SqlBaseRegistroEnfermeriaDao.insertarExamenesFisicos (con, codRegEnfer, examenFisicoCcIns, valorExamenFisico);
  	}
  	
  	/**
	 * Método que ingresa los signos vitales fijos del registro de enfermería
	 * @param con
	 * @param codigoEncabezado
	 * @param frecuenciaCardiaca
	 * @param frecuenciaRespiratoria
	 * @param presionArterialSistolica
	 * @param presionArterialDiastolica
	 * @param presionArterialMedia
	 * @param temperaturaPaciente
	 * @return
	 */
	public int insertarSignosVitalesFijos (Connection con, int codigoEncabezado, String frecuenciaCardiaca, String frecuenciaRespiratoria, String presionArterialSistolica, String presionArterialDiastolica, String presionArterialMedia, String temperaturaPaciente)
	{
		return SqlBaseRegistroEnfermeriaDao.insertarSignosVitalesFijos (con, codigoEncabezado, frecuenciaCardiaca, frecuenciaRespiratoria, presionArterialSistolica, presionArterialDiastolica, presionArterialMedia, temperaturaPaciente);
	}
	
	/**
	 * Método que inserta un signo vital parametrizado por institución centro de costo
	 * @param con
	 * @param codigoEncabezado
	 * @param signoVitalCcIns
	 * @param valorSignoVital
	 * @return
	 */
	public int insertarSignoVitalPametrizado (Connection con, int codigoEncabezado, int signoVitalCcIns, String valorSignoVital)
	{
		return SqlBaseRegistroEnfermeriaDao.insertarSignoVitalPametrizado (con, codigoEncabezado, signoVitalCcIns, valorSignoVital);
	}
	
	  /**
	   * Metodo para consultar y cargar la información de la sección exámenes físicos
	   * @param con
	   * @param codigoCuenta
	   * @param cuentaAsocio
	   * @return Collection -> Con la información de los exámenes físicos
	   */
	  public Collection cargarExamenesFisicos (Connection con, String cuentas)
	  {
	  	return SqlBaseRegistroEnfermeriaDao.cargarExamenesFisicos (con, cuentas);
	  }
   
	  	
	    /**
	     * Método que inserta la anotación de enfermería
	     * @param con
	     * @param codigoEncabezado
	     * @param anotacionEnfermeria
	     * @return
	     */
		public int insertarAnotacionEnfermeria (Connection con, int codigoEncabezado, String anotacionEnfermeria)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarAnotacionEnfermeria (con, codigoEncabezado, anotacionEnfermeria);
		}
		
		/**
		 * Método para consultar los insumos solicitados al paciente a través de solicitud de
	     * medicamentos que hacen parte de catéteres y sondas que no se encuentran anuladas
	     * y se encuentran ya despachadas
		 * @param con
		 * @param codigoCuenta
		 * @param institucion
		 * @return articulosDespachados
		 */
		public Collection consultarCateterSondaDespachados (Connection con, int codigoCuenta, int institucion)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaDespachados (con, codigoCuenta, institucion);
		}
		
		/**
		 * Método para consultar el listado de anotaciones de enfermería realizadas en el registro 
		 * de enfermería de acuerdo a la cuenta del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return listadoAnotacionesEnfermeria
		 */
		public Collection consultarAnotacionesEnfermeria (Connection con, String cuentas, String fechaInicio, String fechaFin)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarAnotacionesEnfermeria (con, cuentas,fechaInicio,fechaFin);
		}
		
		/**
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @return
		 */
		public Collection consultarAnotacionesEnfermeriaFechas(Connection con, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarAnotacionesEnfermeriaFechas(con,cuentas);
		}
		
		/**
		 * Método para consultar el histórico de los signos vitales fijos de la sección
	     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesFijosHisto
		 */
		public Collection consultarSignosVitalesFijosHisto (Connection con, String cuentas, String fechaInicio, String fechaFin)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesFijosHisto (con, cuentas, fechaInicio, fechaFin);
		}
		
		/**
		 * Método para consultar el histórico de los signos vitales parametrizados por institución y centro de costo de la sección
	     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesParamHisto
		 */
		public Collection consultarSignosVitalesParamHisto (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesParamHisto (con, cuentas, institucion, fechaInicio, fechaFin);
		}
		
		/**
		 * Método para consultar el istado con los códigos històricos, fecha registro y hora registro,
	     * de los signos vitales fijos y parametrizados 
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesHistoTodos
		 */
		public Collection consultarSignosVitalesHistoTodos (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesHistoTodos (con, cuentas, institucion, fechaInicio, fechaFin);
		}
		
		/**
		 * Método que inserta el encabezado del cateter sonda para el articulo seleccionado
		 * @param con
		 * @param codigoEncabezado
		 * @param articulo
		 */
		public int 	insertarEncabezadoCateterSonda (Connection con, int codigoEncabezado, int articulo)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarEncabezadoCateterSonda (con, codigoEncabezado, articulo);
		}
		
		/**
		 * Método para insertar el valor de los catéteres fijos de la sección para un artículo determinado
		 * @param con
		 * @param cateterEncabezado
		 * @param viaInsercion
		 * @param fechaInsercion
		 * @param horaInsercion
		 * @param fechaRetiro
		 * @param horaRetiro
		 * @param curaciones
		 * @param observaciones
		 */
		public int insertarCateterSondaFijo (Connection con, int cateterEncabezado, String viaInsercion, String  fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro, String curaciones, String observaciones)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarCateterSondaFijo (con, cateterEncabezado, viaInsercion, fechaInsercion, horaInsercion, fechaRetiro, horaRetiro, curaciones, observaciones);
		}
		
		/**
		 * Método para insertar el valor de los catéteres fijos de la sección para un artículo determinado
		 * @param con
		 * @param cateterEncabezado
		 * @param colCateterCcIns
		 * @param valorCateterCcIns
		 */
		public int 	insertarCateterSondaParam (Connection con, int cateterEncabezado, int colCateterCcIns, String valor)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarCateterSondaParam (con, cateterEncabezado, colCateterCcIns , valor);
		}
		
		/**
		 * Método que consulta el histórico de los cateteres sonda fijos del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return
		 */
		public Collection consultarCateterSondaFijosHisto(Connection con, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaFijosHisto(con, cuentas);
		}
		
		/**
		 * Método que consulta el histórico de los cateteres sonda parametrizados por institución centro costo
		 * del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @return
		 */
		public Collection consultarCateterSondaParamHisto(Connection con, String cuentas, int institucion, int centroCosto)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaParamHisto (con, cuentas, institucion, centroCosto);
		}
		
		/**
		 * Método que consulta el histórico de los catéteres sonda fijos y parametrizados, despues de realizar
		 * el agrupamiento por el codigo del articulo despachado
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @return
		 */
		public Collection consultarCateterSondaTodosHisto (Connection con, String cuentas, int institucion, int centroCosto)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaTodosHisto (con, cuentas, institucion, centroCosto);
		}
		
		/**
		 * Método que actualiza el campo curaciones y observaciones del cateter sonda histórico
		 * @param con
		 * @param catSondaRegEnfer
		 * @param curacionesAntNueva
		 * @param observacionesAntNueva
		 * @param fechaInsercion
		 * @param horaInsercion
		 * @param fechaRetiro
		 * @param horaRetiro
		 * @return
		 */
		public int actualizarCateterSondaFijo(Connection con, int catSondaRegEnfer, String curacionesAntNueva, String observacionesAntNueva, String fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro)
		{
			return SqlBaseRegistroEnfermeriaDao.actualizarCateterSondaFijo(con, catSondaRegEnfer, curacionesAntNueva, observacionesAntNueva, fechaInsercion, horaInsercion, fechaRetiro, horaRetiro);
		}
		
		/**
		 * Método que inserta la información ingresada en los cuidados especiales de enfermería
		 * @param con
		 * @param codEncabezado
		 * @param codigoCuidadoEnf
		 * @param presenta
		 * @param descripcion
		 * @param tipoCuidado
		 * @return
		 */
		public int insertarDetalleCuidadoEnfermeria(Connection con, int codEncabezado, int codigoCuidadoEnf, String presenta, String descripcion, int tipoCuidado)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarDetalleCuidadoEnfermeria (con, codEncabezado, codigoCuidadoEnf, presenta, descripcion, tipoCuidado);
		}
		
		/**
		 * Método que consulta las columnas de los cuidados especiales de enfermería de la orden médica 
		 * y del registro de enfermería en el juego de información de la sección
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return HashMap
		 */
		public HashMap consultarColsCuidadosEspeciales (Connection con, String cuentas, String fechaInicio, String fechaFin)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarColsCuidadosEspeciales (con, cuentas,fechaInicio,fechaFin);
		}
		
		/**
		 * Método que cargar la información ingresada en la orden médica de la hoja neurológica
		 * del paciente en la orden médica
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @return Collection
		 */
		public Collection cargarInfoHojaNeurologica(Connection con, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.cargarInfoHojaNeurologica(con, cuentas);
		}
		
		
		/**
		 * Metodo para Cargar Las Mezclas Registradas en ordenes Medicas. 
		 * @param con
		 * @param tipoConsulta
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @return
		 */
		public HashMap consultarMezclaOrdenes(Connection con, int tipoConsulta, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarMezclaOrdenes (con, tipoConsulta, cuentas);
		}
		
		//*********************************METODOS CONVULSIONES***********//////////////////
		public HashMap obtenerTiposConvulsiones(Connection con)
		{
			return SqlBaseRegistroEnfermeriaDao.obtenerTiposConvulsiones(con);
		}
		
		/**
		 * 
		 */
		public boolean guardarConvulsiones(Connection con, HashMap vo)
		{
			return SqlBaseRegistroEnfermeriaDao.guardarConvulsiones(con,vo);
		}
		
		/**
		 * 
		 */ 
		public HashMap obtenerHistoricosConvulsiones(Connection con, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.obtenerHistoricosConvulsiones(con,cuentas);
		}
		//*********************************FIN METODOS CONVULSIONES***********//////////////////
		
//		**************************METODOS DE LA SECCION DE CONTROL DE ESFINTERES *************************
		/**
		 * Método para consultar el historico de la seccion de control de esfinteres
		 * segun un la cuenta del paciente
		 * @param con
		 * @param idCuenta
		 * @return
		 */
		public HashMap consultarHistoricoControlEsfinteres(Connection con, String cuentas)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarHistoricoControlEsfinteres(con, cuentas);
		}
		/**
		 * Método para insertar el detalle de la seccion de Control de Esfinteres
		 * @param con
		 * @param codigoEncabeRegEnfer
		 * @param codigoCaracControlEnfinter
		 * @param observacion
		 * @return
		 */
		public int insertarDetControlEsfinteres(Connection con, int codigoEncabeRegEnfer, int codigoCaracControlEnfinter, String observacion)
		{
			return SqlBaseRegistroEnfermeriaDao.insertarDetControlEsfinteres(con, codigoEncabeRegEnfer, codigoCaracControlEnfinter, observacion);
		}
		//******************************FIN METODOS SECCION DE CONTROL DE ESFINTERES ****************************

		//*******************************METODOS FUERZA MUSCUALR***********//////////////////

		/**
		 * 
		 * @param con
		 * @return
		 */
		public HashMap consultarTiposFuerzaMuscular(Connection con)
		{
			return SqlBaseRegistroEnfermeriaDao.consultarTiposFuerzaMuscular(con);
		}
		
		/**
		 * 
		 * @param con
		 * @param vo
		 * @return
		 */
		public boolean guardarFuerzaMuscular(Connection con, HashMap vo)
		{
			return SqlBaseRegistroEnfermeriaDao.guardarFuerzaMuscular(con, vo);
		}
		
		/**
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @return
		 */
		public HashMap obtenerHistoricosFuerzaMuscular(Connection con, String codigoCuenta)
		{
			return SqlBaseRegistroEnfermeriaDao.obtenerHistoricosFuerzaMuscular(con, codigoCuenta);
		}

	/**
	 * 
	 */
	public int accionGuardarPupila(Connection con, int codEncabezado, int tamanioD, int tamanioI, String reaccionD, String reaccionI, String obsDerecha, String obsIzquierda)
	{
		return SqlBaseRegistroEnfermeriaDao.accionGuardarPupila(con, codEncabezado, tamanioD, tamanioI, reaccionD, reaccionI, obsDerecha, obsIzquierda);
	}


	/**
	 * 
	 */
	public HashMap consultarHistoricoPupilas(Connection con, String fechaInicio, String cuentas)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarHistoricoPupilas(con, fechaInicio, cuentas);
	}
	
	/**
	 * Método que inserta el detalle de la escala glasgow
	 * @param con
	 * @param codEncabezado
	 * @param especificacionGlasgow
	 * @param observacion
	 * @return
	 */
	public int insertarDetalleEscalaGlasgow(Connection con, int codEncabezado, int especificacionGlasgow, String observacionEspGlasgow)
	{
		return SqlBaseRegistroEnfermeriaDao.insertarDetalleEscalaGlasgow (con, codEncabezado, especificacionGlasgow, observacionEspGlasgow);
	}
	
	/**
	 * Método que consulta el histórico de la escala glasgow
	 * del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoEscalaGlasgow(Connection con, String cuentas)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarHistoricoEscalaGlasgow (con, cuentas);
	}
	
	/**
	 * Método para consultar la información de anotaciones de enfermería de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return listadoAnotacionesEnfermeria
	 */
	public Collection consultarAnotacionesEnfermeriaImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarAnotacionesEnfermeriaImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información historica de los signos vitales fijos de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return 
	 */
	public Collection consultarSignosVitalesFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesFijosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Mètodo que consulta la información histórica de los signos vitales parametrizados por institución centro de
	 * costo, de acuerdo a los parámetros de busqueda, para ser mostrada como sección en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarSignosVitalesParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesParamHistoImpresionHC(con,cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método para consultar el listado con los códigos històricos, fecha registro,hora registro, usuario
     * de los signos vitales fijos y parametrizados, para la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarSignosVitalesHistoTodosImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarSignosVitalesHistoTodosImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas fijas, para la 
	 * impresión de la historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarCateterSondaFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaFijosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas parametrizadas, para la 
	 * impresión de la historia clínica 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public Collection consultarCateterSondaParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaParamHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Mètodo para consultar los codigos históricos, fecha hora de registro, nombre usuario de los cateter sonda
	 * fijos y parametrizados
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public Collection consultarCateterSondaTodosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCateterSondaTodosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta la información del encabezado histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarCuidadosEspecialesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCuidadosEspecialesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
		
	/**
	 * Método que consulta el detalle histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarCuidadosEspecialesDetalleHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarCuidadosEspecialesDetalleHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de escala glagow de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarEscalaGlasgowHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarEscalaGlasgowHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de pupilas de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarPupilasHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarPupilasHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de convulsiones de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarConvulsionesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarConvulsionesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de control de esfinteres de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarControlEsfinteresHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarControlEsfinteresHistoImpresionHC(con,cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de fuerza muscular de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarFuerzaMuscularHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return SqlBaseRegistroEnfermeriaDao.consultarFuerzaMuscularHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}

	/**
	 * Metodo para consultar el Control de Liquidos  desde Historia de Atenciones
	 */
	public HashMap consultarControlLiquidos(Connection con, HashMap parametros)
	{
    	return SqlBaseRegistroEnfermeriaDao.consultarControlLiquidos(con, parametros);
	}
	
	/**
	 * Consultar las solicitudes.
	 * @param con
	 * @param mp
	 * @return
	 */
	public HashMap consultarTomaMuestras(Connection con, HashMap mp)
	{
    	return SqlBaseRegistroEnfermeriaDao.consultarTomaMuestras(con, mp, false);
	}

	/**
	 * 
	 */
	public int consultarUltimaDietaActiva(Connection con, int codigoCuenta) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarUltimaDietaActiva(con, codigoCuenta);
	}

	/**
	 * 
	 */
	public boolean actualizarOrdenDieta(Connection con, int codOrdenDieta, String suspendidoEnfermeria, String observacionesEnfermeria) 
	{
		return SqlBaseRegistroEnfermeriaDao.actualizarOrdenDieta(con, codOrdenDieta, suspendidoEnfermeria, observacionesEnfermeria);
	}	
	
	/**
	 * Metodo para consultar el estado de la interfaz Nutricion con AS400
	 */
	public String consultarParametroInterfaz(Connection con) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarInterfazNutricion(con);
	}

	
	public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
	{
		return SqlBaseRegistroEnfermeriaDao.tiposNutricionOralActivo(con, codigoCuenta);
	}

	public String consultarFechaDieta(Connection con, int codOrdenDieta) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarFechaDieta(con, codOrdenDieta);
	}

	public String consultarHoraDieta(Connection con, int codOrdenDieta) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarHoraDieta(con, codOrdenDieta);
	}

	public String consultarPisoCama(Connection con, int codigoCama) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarPisoCama(con, codigoCama);
	}
	
	
	public String consultarNumeroCama(Connection con, int codigoCama) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarNumeroCama(con, codigoCama);
	}
	
	public String consultarConvenioVip(Connection con, int codigoConvenio) 
	{
		return SqlBaseRegistroEnfermeriaDao.consultarConvenioVip(con, codigoConvenio);
	}
	
	/**
	 * Método para saber si existe o no el registro de enfermeria para el paciente. 
	 * @param con -> conexion
	 * @param cuenta
	 * @return codigo si existe sino retorna -1
	 */
	public InfoDatosInt existeRegistroEnfermeria(Connection con, int cuenta)
	{
		return SqlBaseRegistroEnfermeriaDao.existeRegistroEnfermeria(con, cuenta);
	}
	
	/**
     * Actualiza el indicador de Nota finalizada
     * @param Connection con 
     * @param HashMap parametros
     * */
    public boolean actualizarRegistroEnfermeria(Connection con, HashMap parametros)
    {
    	return SqlBaseRegistroEnfermeriaDao.actualizarRegistroEnfermeria(con, parametros);
    }

    /**
     * 
     * @param con
     * @param cuentasPacientes
     */
	public boolean pacienteTieneOrdenesMezclas(Connection con,ArrayList<CuentasPaciente> cuentasPacientes, boolean todasMezclas)
	{
		return SqlBaseRegistroEnfermeriaDao.pacienteTieneOrdenesMezclas(con,cuentasPacientes,todasMezclas);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuentasPacienteArray
	 * @return
	 */
	public HashMap mezclasPendientesPaciente(Connection con,ArrayList<CuentasPaciente> cuentasPacientes)
	{
		return SqlBaseRegistroEnfermeriaDao.mezclasPendientesPaciente(con,cuentasPacientes);	
	}

	@Override
	public HashMap consultarTomaMuestrasHistorico(Connection con, HashMap parametros) {
		return SqlBaseRegistroEnfermeriaDao.consultarTomaMuestrasHistorico(con, parametros);

	}


	@Override
	public ArrayList<Object> cargarValoracionEnfermeria(Connection con,
			int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente,
			boolean cargarParametrizacion, boolean esHistoricos) {
		return SqlBaseRegistroEnfermeriaDao.cargarValoracionEnfermeria(con, ingresoPaciente, cuentaPaciente, centroCostoPaciente, cargarParametrizacion, esHistoricos);
	}

	@Override
	public int insertarValoracionEnfermeria(Connection con,
			int codigoEncabezado, ArrayList<Object> valoracionEnfermeria) {
		return SqlBaseRegistroEnfermeriaDao.insertarValoracionEnfermeria(con, codigoEncabezado, valoracionEnfermeria);
	}
	
	
	@Override
	public int insertaResultadosLaboratorios(Connection con,
			int codigoEncabezado,
			ArrayList<Object> resultadoLaboratorios) {
		return SqlBaseRegistroEnfermeriaDao.insertaResultadosLaboratorios(con,codigoEncabezado,resultadoLaboratorios);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao#insertarRegistroAlertaOrdenesMedicas(java.sql.Connection, java.util.ArrayList)
	 */
	@Override
	public ArrayList<String> insertarRegistroAlertaOrdenesMedicas(Connection con, 
			ArrayList<DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas) {
		return SqlBaseRegistroEnfermeriaDao.insertarRegistroAlertaOrdenesMedicas(con,listaRegistroOrdenesMedicas);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao#consultarAlertaOrdenMedicaCuenta(java.sql.Connection, long)
	 */
	@Override
	public HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> consultarAlertaOrdenMedicaCuenta (Connection con, long cuenta) {
		return SqlBaseRegistroEnfermeriaDao.consultarAlertaOrdenMedicaCuenta(con, cuenta);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao#actualizarRegistroAlertaOrdenesMedicas(java.sql.Connection, java.util.ArrayList, java.sql.Date, java.lang.String)
	 */
	@Override
	public boolean actualizarRegistroAlertaOrdenesMedicas(
			Connection con,
			HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas,
			String fechaInicioRevision, String horaInicioRevision, long registroEnfermeria,
			String usuarioModifica) {
		return SqlBaseRegistroEnfermeriaDao.actualizarRegistroAlertaOrdenesMedicas(con, listaRegistroOrdenesMedicas, 
				fechaInicioRevision, horaInicioRevision, registroEnfermeria, usuarioModifica);
	}
	
}