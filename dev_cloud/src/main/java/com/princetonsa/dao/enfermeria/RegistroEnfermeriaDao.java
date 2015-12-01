/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.dao.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */

/**
 * Interfaz para el manejo de la funcionalidad Registro de Enfermería 
 */
public interface RegistroEnfermeriaDao
{
	/**
	 * Metodo para insertar el registro de enfermeria.
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int insertarRegistroEnfermeria(Connection con, int codigoCuenta); 
	
	/**
	 * Metodo que realiza la busqueda de lista de los pacientes que se encuentran en las camas ocupadas 
	 * cuyo centro de costo de la cama corresponde al centro de costo seleccionado.
	 * @param con
	 * @param campos
	 * @return listadoPacientesCentroCosto
	 */
	public HashMap consultarPacientesPiso(Connection con, HashMap campos);

	/**
	 * Metodo que realiza la busqueda de lista de los pacientes centro de costo 
	 * corresponde al centro de costo seleccionado.
	 * @param con
	 * @param campos
	 * @return listadoPacientesCentroCosto
	 */
	public HashMap consultarPacientesCentroCosto(Connection con, HashMap campos);
	
	/**
	 * Método para consultar el soporte respiratorio
	 * ingresado en la orden médica
	 * @param cuenta Código de la cuenta del paciente
	 * @param fechaInicio @todo
	 * @param incluirAnterior @todo
	 * @param fechaFin @todo
	 * @param cuentaAsocio @todo
	 * @return HashMap con los datos del sporte respiratorio
	 */
	public HashMap consultarSoporteOrden(Connection con, String cuentas, String fechaInicio, boolean incluirAnterior, String fechaFin);

	/**
	 * Método para consultar los históricos de
	 * cuidados de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return Collection con los registros consultados
	 */
	public HashMap consultarCuidadosEnfermeria(Connection con,String cuentas, String fechaInicio, String fechaFin);

	/**
	 * Consultar Histroricos de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param fechaInicio
	 * @param fechaFin @todo
	 * @param codigoCuentaAsocio @todo
	 * @return
	 */
	public HashMap consultarHistoricoSoporteEnfer(Connection con, String cuentas, String fechaInicio, String fechaFin);

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return
	 */
	public Collection consultarFechasHistoricoSoporte(Connection con, String cuentas);

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de diagnósticos de enfermería
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return Colección de fechas
	 */
	public Collection consultarFechasHistoricoNanda(Connection con, String cuentas);

	/**
     * Metodo para consultar y cargar la información de la dieta registrada en orden medica.
     * @param con
     * @param codigoCuenta
	 * @param codigoCuentaAsocio
     * @return Collection con los datos de la última dieta
     */
    public Collection cargarDieta(Connection con, String cuentas, int tipoConsulta);

	/**
	 * @param con
	 * @param codigoRegistroEnferia
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @param loginUsuario 
	 * @todo
	 * @return
	 */
	public int finalizarTurnoBalanceLiquidos(Connection con, int codigoRegistroEnferia, String fechaRegistro, String horaRegistro, String loginUsuario);

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
	public HashMap cargarHistoricosDieta(Connection con, String cuentas, int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno, String fechaInicio);

   /**
    * Metodo para insertar el balance de liquidos de la seccion dieta
    * @param con
    * @param parametrizado
    * @param codEncabezado
    * @param codigoMedicamento
    * @param valor
    * @return
    */
	public int insertarBalanceLiquidos(Connection con, int parametrizado, int codEncabezado, int codigoMedicamento, float valor);


	/**
	 * Metodo para insertar el detalle del la finalizacion del turno.
	 * @param con
	 * @param codigoTurno
	 * @param codigoMedicamento
	 * @param valor
	 * @param tabla
	 * @return
	 */
	public int insertarDetFinTurnoBalanceLiquidos(Connection con, int codigoTurno, int codigoMedicamento, float valor, int tabla);

	/**
	 * Metodo para saber la ultima fecha donde se finalizo el turno de la enfermera. 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String ultimaHoraFinTurno(Connection con, int codigoCuenta);
	
	
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

      public Collection consultarTiposInstitucionCCosto(Connection con, int institucion, String cuentas, int nroConsulta);

	/**
	 * Metodo para insertar los datos del encabezado de registro de enfermeria
	 * @param con
	 * @param codigoRegistroEnfermeria
	 * @param loginUsuario
	 * @param datosMedico
	 * @param obsSoporte @todo
	 * @param FechaRegistro
	 * @param HoraRegistro
	 * @return
	 */														 
	public int insertarEncabezadoRegistroEnfermeria(Connection con, int codigoRegistroEnfermeria, String fechaRegistro, String horaRegistro, String loginUsuario, String datosMedico, String obsSoporte);

	/**
	 * Metodo para insertar la parte de liquiquidos y medicamentos de infusion de la seccion Dieta. 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoRegistroEnferia
	 * @param descripcion 
	 * @param volumen
	 * @param velosidad 
	 * @param Suspendida
	 * @return
	 */
	public int insertarLiqMedInfusion(Connection con, int codigoCuenta, int codigoRegistroEnferia, String descripcion, String volumen, String velocidad, boolean suspendida, int tipoOperacion);

	
	   
    /**
     * Metodo para traer un mapa con el listado de liquidos y medicamentos registrados para
     * un paciente especifico. 
     * @param con
     * @param codigoCuenta
     * @param fechaUltimoFinTurno
     * @param fechaInicio
     * @param codigoCuentaAsocio 
     * @return
     */
	public HashMap consultarLiqMedicamentosPaciente (Connection con, String cuentas,int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno, String fechaInicio);

	/**
	 * Método para insertar los exámenes fìsicos del registro de enfermería
	 * @param con una conexion abierta con una fuente de datos
	 * @param codRegEnfer
	 * @param examenFisicoCcIns
	 * @param valorExamenFisico
	 * @return 
	 */
	public int insertarExamenesFisicos (Connection con, int codRegEnfer, int examenFisicoCcIns, String valorExamenFisico);
	
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
	public int insertarSignosVitalesFijos (Connection con, int codigoEncabezado, String frecuenciaCardiaca, String frecuenciaRespiratoria, String presionArterialSistolica, String presionArterialDiastolica, String presionArterialMedia, String temperaturaPaciente);
	
	
	/**
	 * Método que inserta un signo vital parametrizado por institución centro de costo
	 * @param con
	 * @param codigoEncabezado
	 * @param signoVitalCcIns
	 * @param valorSignoVital
	 * @return
	 */
	public int insertarSignoVitalPametrizado (Connection con, int codigoEncabezado, int signoVitalCcIns, String valorSignoVital);
	
	  /**
	   * Metodo para consultar y cargar la información de la sección exámenes físicos
	   * @param con
	   * @param codigoCuenta
	   * @param cuentaAsocio
	   * @return Collection -> Con la información de los exámenes físicos
	   */
	public Collection cargarExamenesFisicos (Connection con, String cuentas);
	
    /**
     * Método que inserta la anotación de enfermería
     * @param con
     * @param codigoEncabezado
     * @param anotacionEnfermeria
     * @return
     */
	public int insertarAnotacionEnfermeria (Connection con, int codigoEncabezado, String anotacionEnfermeria);
	
	/**
	 * Método para consultar el listado de anotaciones de enfermería realizadas en el registro 
	 * de enfermería de acuerdo a la cuenta del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return listadoAnotacionesEnfermeria
	 */
	public Collection consultarAnotacionesEnfermeria (Connection con, String cuentas, String fechaInicio, String fechaFin);

	/**
	 * Método para consultar el listado de la toma de muestras
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return listadoAnotacionesEnfermeria
	 */
	public HashMap consultarTomaMuestrasHistorico (Connection con, HashMap parametros);

	/**
	 * Método para consultar los insumos solicitados al paciente a través de solicitud de
     * medicamentos que hacen parte de catéteres y sondas que no se encuentran anuladas
     * y se encuentran ya despachadas
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @return articulosDespachados
	 */
	public Collection consultarCateterSondaDespachados (Connection con, int codigoCuenta, int institucion);

	/**
	 * Método para ingresar los datos del soporte respiratorio
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarSoporteRespiratorio(Connection con, int codEncabezado, Vector soporteRespiratorio);

	/**
	 * Método para ingresar los diagnosticos de enfermería
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarDiagnosticosNanda(Connection con, int codEncabezado, Vector diagnosticosEnfermeria); 

	/**
	 * Método para consultar el historico de los diagnosticos de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio @todo
	 * @param institucion @todo
	 * @param fechaInicio @todo
	 * @param fechaFin @todo
	 * @return
	 */
	public HashMap consultarDiagnosticosNanda(Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin);
	
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
	public Collection consultarSignosVitalesFijosHisto (Connection con, String cuentas, String fechaInicio, String fechaFin);
	
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
	public Collection consultarSignosVitalesParamHisto (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin);
	
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
	public Collection consultarSignosVitalesHistoTodos (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin);
	
	/**
	 * Método que inserta el encabezado del cateter sonda para el articulo seleccionado
	 * @param con
	 * @param codigoEncabezado
	 * @param articulo
	 */
	public int 	insertarEncabezadoCateterSonda (Connection con, int codigoEncabezado, int articulo);
	
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
	public int insertarCateterSondaFijo (Connection con, int cateterEncabezado, String viaInsercion, String  fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro, String curaciones, String observaciones);
	
	/**
	 * Método para insertar el valor de los catéteres fijos de la sección para un artículo determinado
	 * @param con
	 * @param cateterEncabezado
	 * @param colCateterCcIns
	 * @param valorCateterCcIns
	 */
	public int 	insertarCateterSondaParam (Connection con, int cateterEncabezado, int colCateterCcIns, String valor);
	
	/**
	 * Método que consulta el histórico de los cateteres sonda fijos del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarCateterSondaFijosHisto(Connection con, String cuentas);
	
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
	public Collection consultarCateterSondaParamHisto(Connection con, String cuentas, int institucion, int centroCosto);
	
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
	public Collection consultarCateterSondaTodosHisto (Connection con, String cuentas, int institucion, int centroCosto);

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
	public int actualizarCateterSondaFijo(Connection con, int catSondaRegEnfer, String curacionesAntNueva, String observacionesAntNueva, String fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro);

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
	public int insertarDetalleCuidadoEnfermeria(Connection con, int codEncabezado, int codigoCuidadoEnf, String presenta, String descripcion, int tipoCuidado);
	
	/**
	 * Método que consulta los cuidados especiales de enfermería de la orden médica o del registro de enfermería
	 * para formar las columnas que irían en el juego de info de los cuidados
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @param tipoConsulta
	 * @return HashMap
	 */
	public HashMap consultarColsCuidadosEspeciales (Connection con, String cuentas, String fechaInicio, String fechaFin);

	/**
	 * Método que cargar la información ingresada en la orden médica de la hoja neurológica
	 * del paciente en la orden médica
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return Collection
	 */
	public Collection cargarInfoHojaNeurologica(Connection con, String cuentas);  

	/**
	 * Metodo para Cargar Las Mezclas Registradas en ordenes Medicas. 
	 * @param con
	 * @param tipoConsulta
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public HashMap consultarMezclaOrdenes(Connection con, int tipoConsulta, String cuentas);

	//-**********************************/////////////////metodos convulsiones ////////////////******************************************************//
	/**
	 * 
	 * 
	 */
	public HashMap obtenerTiposConvulsiones(Connection con);
	
	/**
	 * 
	 */
	public boolean guardarConvulsiones(Connection con, HashMap vo);
	
	/**
	 * 
	 */ 
	public HashMap obtenerHistoricosConvulsiones(Connection con, String cuentas);
	//-**********************************//////////////fin metodos convulsiones ////////////////******************************************************//	

	
	//*************************************Metodos De la seccion de Control de esfinteres ***********************
	/**
	 * Método para consultar el historico de la seccion de control de esfinteres
	 * segun un la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarHistoricoControlEsfinteres(Connection con, String cuentas);
	
	/**
	 * Método para insertar el detalle de la seccion de Control de Esfinteres
	 * @param con
	 * @param codigoEncabeRegEnfer
	 * @param codigoCaracControlEnfinter
	 * @param observacion
	 * @return
	 */
	public int insertarDetControlEsfinteres(Connection con, int codigoEncabeRegEnfer, int codigoCaracControlEnfinter, String observacion);

	
	//************************************Fin de Metodos de la seccion de control de esfinteres ***********************************
	
	
	//*******************************METODOS FUERZA MUSCUALR***********//////////////////

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarTiposFuerzaMuscular(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean guardarFuerzaMuscular(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap obtenerHistoricosFuerzaMuscular(Connection con, String codigoCuenta);

	/**
	 * @param con
	 * @param codEncabezado
	 * @param tamanioD
	 * @param tamanioI
	 * @param reaccionD
	 * @param reaccionI
	 * @param obsDerecha @todo
	 * @param obsIzquierda @todo
	 * @return
	 */
	public int accionGuardarPupila(Connection con, int codEncabezado, int tamanioD, int tamanioI, String reaccionD, String reaccionI, String obsDerecha, String obsIzquierda);

	/**
	 * @param con
	 * @param fechaInicio
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoPupilas(Connection con, String fechaInicio, String cuentas);

	/**
	 * Método que inserta el detalle de la escala glasgow
	 * @param con
	 * @param codEncabezado
	 * @param especificacionGlasgow
	 * @param observacion
	 * @return
	 */
	public int insertarDetalleEscalaGlasgow(Connection con, int codEncabezado, int especificacionGlasgow, String observacionEspGlasgow);

	/**
	 * Método que consulta el histórico de la escala glasgow
	 * del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoEscalaGlasgow(Connection con, String cuentas);

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
	public Collection consultarAnotacionesEnfermeriaImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
	/**
	 * M?todo para consultar la informaci?n historica de los signos vitales fijos de acuerdo a los par?metros
	 * de b?squeda, para ser mostrada como secci?n en la impresi?n de histor?a cl?nica
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
	public Collection consultarSignosVitalesFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
		
	/**
	 * M?todo que consulta la informaci?n hist?rica de los signos vitales parametrizados por instituci?n centro de
	 * costo, de acuerdo a los par?metros de busqueda, para ser mostrada como secci?n en la impresi?n de historia cl?nica
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
	public Collection consultarSignosVitalesParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
	/**
	 * M?todo para consultar el listado con los c?digos hist?ricos, fecha registro,hora registro, usuario
     * de los signos vitales fijos y parametrizados, para la impresi?n de historia cl?nica
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
	public Collection consultarSignosVitalesHistoTodosImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public Collection consultarCateterSondaFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public Collection consultarCateterSondaParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public Collection consultarCateterSondaTodosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarCuidadosEspecialesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarCuidadosEspecialesDetalleHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarEscalaGlasgowHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarPupilasHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarConvulsionesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarControlEsfinteresHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
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
	public HashMap consultarFuerzaMuscularHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion);
	
	/**
	 * Metodo para consultar el Control de Liquidos  
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param institucion
	 * @param nroConsulta
	 * @param fechaUltimoFinTurno
	 * @param fechaInicio
	 * @return
	 */
	public HashMap consultarControlLiquidos(Connection con, HashMap parametros);

	/**
	 * Consultar las solicitudes.
	 * @param con
	 * @param mp
	 * @return
	 */
	public HashMap consultarTomaMuestras(Connection con, HashMap mp);

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public Collection consultarAnotacionesEnfermeriaFechas(Connection con, String cuentas);

	public int consultarUltimaDietaActiva(Connection con, int codigoCuenta);

	public boolean actualizarOrdenDieta(Connection con, int codOrdenDieta, String suspendidoEnfermeria, String observacionesEnfermeria);

	public String consultarParametroInterfaz(Connection con);

	public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta);

	public String consultarFechaDieta(Connection con, int codOrdenDieta);

	public String consultarHoraDieta(Connection con, int codOrdenDieta);

	public String consultarPisoCama(Connection con, int codigoCama);
	
	public String consultarNumeroCama(Connection con, int codigoCama);

	public String consultarConvenioVip(Connection con, int codigoConvenio);
	
	/**
	 * Método para saber si existe o no el registro de enfermeria para el paciente. 
	 * @param con -> conexion
	 * @param cuenta
	 * @return codigo si existe sino retorna -1
	 */
	public InfoDatosInt existeRegistroEnfermeria(Connection con, int cuenta);
	
	/**
     * Actualiza el indicador de Nota finalizada
     * @param Connection con 
     * @param HashMap parametros
     * */
    public boolean actualizarRegistroEnfermeria(Connection con, HashMap parametros);

    /**
     * 
     * @param con
     * @param cuentasPacientes
     * @param todasMezclas 
     */
	public boolean pacienteTieneOrdenesMezclas(Connection con,ArrayList<CuentasPaciente> cuentasPacientes, boolean todasMezclas);

	/**
	 * 
	 * @param con
	 * @param cuentasPacienteArray
	 * @return
	 */
	public HashMap mezclasPendientesPaciente(Connection con,ArrayList<CuentasPaciente> cuentasPacienteArray);
	

	/**
	 * 
	 * @param con
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @param esHistoricos
	 * @return
	 */
	public ArrayList<Object> cargarValoracionEnfermeria(Connection con,
			int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente,
			boolean cargarParametrizacion, boolean esHistoricos);

	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public int insertarValoracionEnfermeria(Connection con,
			int codigoEncabezado, ArrayList<Object> valoracionEnfermeria);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public int insertaResultadosLaboratorios(Connection con,int codigoEncabezado,ArrayList<Object> resultadoLaboratorios);
	
	/**
	 * Inserta los registros de alerta cada vez que se genera un registro de orden medica
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @return Arreglo con el nombre de las secciones que no se guardaron para mostrar en el log de errores.
	 * 		   Si se guardaron todos los registros este arreglo debe retornar vacio.
	 */
	public ArrayList<String> insertarRegistroAlertaOrdenesMedicas(Connection con, 
			ArrayList<DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas);
	
	/**
	 * Metódo encargado de consultar las secciones con alerta de registro de órden mádica para
	 * para una cuenta dada y estado activo
	 * @param con
	 * @param cuenta
	 * @param seccion
	 * @return
	 */
	public HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> consultarAlertaOrdenMedicaCuenta (Connection con, long cuenta);
	
	/**
	 * Método encargado de actualizar el registro de alerta de nuevas ordenes medicas e
	 * inactivar los registros que ya fueron revisados.
	 * Se recibe la fecha en que se inicio el registro de enfermería para validar que 
	 * no se cambie el estado a las alertas que hayan sido generadas por el medico mientras
	 * la enfermera tenía abierta la ventana de registro de enfermería.
	 * 
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @param fechaInicioRevision
	 * @param horaInicioRevision
	 * @return 
	 */
	public boolean actualizarRegistroAlertaOrdenesMedicas(Connection con, 
			HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas, 
			String fechaInicioRevision, String horaInicioRevision, long registroEnfermeria,
			String usuarioModifica);
	
}