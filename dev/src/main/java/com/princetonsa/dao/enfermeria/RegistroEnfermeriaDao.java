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
 * @author Andr�s Mauricio Ruiz V�lez
 * Princeton S.A. (Parquesoft-Manizales)
 */

/**
 * Interfaz para el manejo de la funcionalidad Registro de Enfermer�a 
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
	 * M�todo para consultar el soporte respiratorio
	 * ingresado en la orden m�dica
	 * @param cuenta C�digo de la cuenta del paciente
	 * @param fechaInicio @todo
	 * @param incluirAnterior @todo
	 * @param fechaFin @todo
	 * @param cuentaAsocio @todo
	 * @return HashMap con los datos del sporte respiratorio
	 */
	public HashMap consultarSoporteOrden(Connection con, String cuentas, String fechaInicio, boolean incluirAnterior, String fechaFin);

	/**
	 * M�todo para consultar los hist�ricos de
	 * cuidados de enfermer�a
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
	 * M�todo para consultar las fechas en las cuales existen
	 * registros de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return
	 */
	public Collection consultarFechasHistoricoSoporte(Connection con, String cuentas);

	/**
	 * M�todo para consultar las fechas en las cuales existen
	 * registros de diagn�sticos de enfermer�a
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return Colecci�n de fechas
	 */
	public Collection consultarFechasHistoricoNanda(Connection con, String cuentas);

	/**
     * Metodo para consultar y cargar la informaci�n de la dieta registrada en orden medica.
     * @param con
     * @param codigoCuenta
	 * @param codigoCuentaAsocio
     * @return Collection con los datos de la �ltima dieta
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
 	 * por centro de costo e instituci�n en el registro de enfermer�a 
 	 * @param con
     * @param codigo de la institucion
     * @param codigo del centroCosto
     * @param codigoCuenta
     * @param cuentaAsocio
     * @param Nro Consulta parametro que indica la informacion a sacar
 	 *        1  Listado de tipos de signos vitales de enfermer�a        
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
	 * M�todo para insertar los ex�menes f�sicos del registro de enfermer�a
	 * @param con una conexion abierta con una fuente de datos
	 * @param codRegEnfer
	 * @param examenFisicoCcIns
	 * @param valorExamenFisico
	 * @return 
	 */
	public int insertarExamenesFisicos (Connection con, int codRegEnfer, int examenFisicoCcIns, String valorExamenFisico);
	
	/**
	 * M�todo que ingresa los signos vitales fijos del registro de enfermer�a
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
	 * M�todo que inserta un signo vital parametrizado por instituci�n centro de costo
	 * @param con
	 * @param codigoEncabezado
	 * @param signoVitalCcIns
	 * @param valorSignoVital
	 * @return
	 */
	public int insertarSignoVitalPametrizado (Connection con, int codigoEncabezado, int signoVitalCcIns, String valorSignoVital);
	
	  /**
	   * Metodo para consultar y cargar la informaci�n de la secci�n ex�menes f�sicos
	   * @param con
	   * @param codigoCuenta
	   * @param cuentaAsocio
	   * @return Collection -> Con la informaci�n de los ex�menes f�sicos
	   */
	public Collection cargarExamenesFisicos (Connection con, String cuentas);
	
    /**
     * M�todo que inserta la anotaci�n de enfermer�a
     * @param con
     * @param codigoEncabezado
     * @param anotacionEnfermeria
     * @return
     */
	public int insertarAnotacionEnfermeria (Connection con, int codigoEncabezado, String anotacionEnfermeria);
	
	/**
	 * M�todo para consultar el listado de anotaciones de enfermer�a realizadas en el registro 
	 * de enfermer�a de acuerdo a la cuenta del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return listadoAnotacionesEnfermeria
	 */
	public Collection consultarAnotacionesEnfermeria (Connection con, String cuentas, String fechaInicio, String fechaFin);

	/**
	 * M�todo para consultar el listado de la toma de muestras
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return listadoAnotacionesEnfermeria
	 */
	public HashMap consultarTomaMuestrasHistorico (Connection con, HashMap parametros);

	/**
	 * M�todo para consultar los insumos solicitados al paciente a trav�s de solicitud de
     * medicamentos que hacen parte de cat�teres y sondas que no se encuentran anuladas
     * y se encuentran ya despachadas
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @return articulosDespachados
	 */
	public Collection consultarCateterSondaDespachados (Connection con, int codigoCuenta, int institucion);

	/**
	 * M�todo para ingresar los datos del soporte respiratorio
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarSoporteRespiratorio(Connection con, int codEncabezado, Vector soporteRespiratorio);

	/**
	 * M�todo para ingresar los diagnosticos de enfermer�a
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarDiagnosticosNanda(Connection con, int codEncabezado, Vector diagnosticosEnfermeria); 

	/**
	 * M�todo para consultar el historico de los diagnosticos de enfermer�a
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
	 * M�todo para consultar el hist�rico de los signos vitales fijos de la secci�n
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
	 * M�todo para consultar el hist�rico de los signos vitales parametrizados por instituci�n y centro de costo de la secci�n
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
	 * M�todo para consultar el istado con los c�digos hist�ricos, fecha registro y hora registro,
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
	 * M�todo que inserta el encabezado del cateter sonda para el articulo seleccionado
	 * @param con
	 * @param codigoEncabezado
	 * @param articulo
	 */
	public int 	insertarEncabezadoCateterSonda (Connection con, int codigoEncabezado, int articulo);
	
	/**
	 * M�todo para insertar el valor de los cat�teres fijos de la secci�n para un art�culo determinado
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
	 * M�todo para insertar el valor de los cat�teres fijos de la secci�n para un art�culo determinado
	 * @param con
	 * @param cateterEncabezado
	 * @param colCateterCcIns
	 * @param valorCateterCcIns
	 */
	public int 	insertarCateterSondaParam (Connection con, int cateterEncabezado, int colCateterCcIns, String valor);
	
	/**
	 * M�todo que consulta el hist�rico de los cateteres sonda fijos del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarCateterSondaFijosHisto(Connection con, String cuentas);
	
	/**
	 * M�todo que consulta el hist�rico de los cateteres sonda parametrizados por instituci�n centro costo
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
	 * M�todo que consulta el hist�rico de los cat�teres sonda fijos y parametrizados, despues de realizar
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
	 * M�todo que actualiza el campo curaciones y observaciones del cateter sonda hist�rico
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
	 * M�todo que inserta la informaci�n ingresada en los cuidados especiales de enfermer�a
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
	 * M�todo que consulta los cuidados especiales de enfermer�a de la orden m�dica o del registro de enfermer�a
	 * para formar las columnas que ir�an en el juego de info de los cuidados
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
	 * M�todo que cargar la informaci�n ingresada en la orden m�dica de la hoja neurol�gica
	 * del paciente en la orden m�dica
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
	 * M�todo para consultar el historico de la seccion de control de esfinteres
	 * segun un la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarHistoricoControlEsfinteres(Connection con, String cuentas);
	
	/**
	 * M�todo para insertar el detalle de la seccion de Control de Esfinteres
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
	 * M�todo que inserta el detalle de la escala glasgow
	 * @param con
	 * @param codEncabezado
	 * @param especificacionGlasgow
	 * @param observacion
	 * @return
	 */
	public int insertarDetalleEscalaGlasgow(Connection con, int codEncabezado, int especificacionGlasgow, String observacionEspGlasgow);

	/**
	 * M�todo que consulta el hist�rico de la escala glasgow
	 * del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoEscalaGlasgow(Connection con, String cuentas);

	/**
	 * M�todo para consultar la informaci�n de anotaciones de enfermer�a de acuerdo a los par�metros
	 * de b�squeda, para ser mostrada como secci�n en la impresi�n de histor�a cl�nica
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
	 * M�todo para consultar la informaci�n de cateter sonda de las columnas fijas, para la 
	 * impresi�n de la historia cl�nica
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
	 * M�todo para consultar la informaci�n de cateter sonda de las columnas parametrizadas, para la 
	 * impresi�n de la historia cl�nica 
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
	 * M�todo para consultar los codigos hist�ricos, fecha hora de registro, nombre usuario de los cateter sonda
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
	 * M�todo que consulta la informaci�n del encabezado hist�rico de los cuidados especiales de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el detalle hist�rico de los cuidados especiales de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el hist�rico de escala glagow de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el hist�rico de pupilas de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el hist�rico de convulsiones de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el hist�rico de control de esfinteres de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo que consulta el hist�rico de fuerza muscular de acuerdo
	 * a los par�metros de b�squeda para mostrarse en la impresi�n de historia cl�nica
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
	 * M�todo para saber si existe o no el registro de enfermeria para el paciente. 
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
	 * Met�do encargado de consultar las secciones con alerta de registro de �rden m�dica para
	 * para una cuenta dada y estado activo
	 * @param con
	 * @param cuenta
	 * @param seccion
	 * @return
	 */
	public HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> consultarAlertaOrdenMedicaCuenta (Connection con, long cuenta);
	
	/**
	 * M�todo encargado de actualizar el registro de alerta de nuevas ordenes medicas e
	 * inactivar los registros que ya fueron revisados.
	 * Se recibe la fecha en que se inicio el registro de enfermer�a para validar que 
	 * no se cambie el estado a las alertas que hayan sido generadas por el medico mientras
	 * la enfermera ten�a abierta la ventana de registro de enfermer�a.
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