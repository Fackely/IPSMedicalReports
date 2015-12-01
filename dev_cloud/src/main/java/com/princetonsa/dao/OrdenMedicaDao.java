/*
 * Creado en May 31, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface OrdenMedicaDao
{

	/**
	 * Método para obtener el codigo de la orden que está asociado a una secuencia
	 * @param con -> conexion
	 * @return codigoOrden
	 */
	public int obtenerCodigoOrden(Connection con);

	/**
	 * Método para insertar una orden médica
	 * @param con
	 * @param cuenta
	 * @param descripcionDieta
	 * @param observacionesGenerales
	 * @param descripcionDietaParenteral @todo
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @param descripcionDiete
	 * @param descripcionSoporte
	 * @param tipoMonitoreo
	 * @param fechaOrden
	 * @param horaOrden
	 * @param descripcionSoporte
	 * @return
	 */
	
	public  DtoResultado insertarOrdenMedica(Connection con,  int cuenta, String descripcionSoporteRespiratorio, String descripcionDieta,  String observacionesGenerales, String descripcionDietaParenteral,Boolean tieneDatos);
	
	
	/**
	 * Método para asociar el encabezado historico de la orden
	 * con el detalle de la observación de la orden para poder manejar
	 * las observaciones cronológicamente
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPkDetalleObservacion
	 * @param codigoPkEncabezado
	 * @throws SQLException
	 */	
	public void asociarDetalleObservacionEncabezado (Connection con, int codigoPkDetalleObservacion, int codigoPkEncabezado) throws SQLException;
	
	/**
	 *  Método para insertar el tipo de monitoreo a una orden médica
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @param tipoMonitoreo
	 * @return
	 */
	 public int insertarOrdenTipoMonitoreo(Connection con, int codigoEncabezado, int tipoMonitoreo);
	
	/**
	 *  Método para insertar el soporte respiratorio a una orden médica
	 * @param con
	 * @param codigoEncabezado
	 * @param equipoElemento
	 * @param cantidadSoporteRespiratorio
	 * @param oxigenoTerapia
	 * @param descripcionSoporteRespiratorio 
	 * @return
	 */
	
	public int insertarOrdenSoporteRespiratorio(Connection	con, int codigoEncabezado, int equipoElemento, float cantidadSoporteRespiratorio, String oxigenoTerapia, String descripcionSoporteRespiratorio);

	/**
	 * Funcion Para retornar una collecion con el listado de los tipos de nutricion Oral
	 * @param con
	 * @param mezcla
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param inicioEncabezado @todo
	 * @param finEncabezado @todo
	 * @param nroConsulta el indicador del tipò de consulta
	 * @param codigo de la institucion
	 * @param codigo del centro_costo
	 * @param Nro Consulta parametro que indica la informacion a sacar
	 *        1  Listado de tipos de nutricion Oral        
	 * 		  2  Listado de tipos de nutricion parenteral
	 * 		  3  Listado de los tipos de cuidados de emfermeria
	 * 		  4  Listado de tipos de elemnetos en el soporte respiratorio
	 *  	  5  Listado de tipos de monitoreo	
	 * @return Una  Collection 
	 */	
	
	
	public Collection consultarTipos(Connection con,int institucion, int centro_costo, int Nro_consulta, int mezcla, int codigoCuenta, int cuentaAsocio, int inicioEncabezado, int finEncabezado);
	/**
	 *  Metodo para insertar los tipo de nutricion (oral y parenteral)
	 * @param con
	 * @param paciente 
	 * @param usuario 
	 * @param ordenMedica
	 * @return
	 */
	
	public int insertarNutricion(
			Connection con,
			int codigoEncabezado,
			int tipoNutricion,
			float volumen,
			String unidadVolumen, 
			int tipoNut, 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			String esMedicamento) throws IPSException;

								
	/**
	 *  Metodo para insertar los datos de Orden Dieta 
	 * @param con
	 * @param codigoEncabezado
	 * @param velocidadInfusion
	 * @param farmacia
	 * @param nutricionOral
	 * @param nutricionParenteral
	 * @param finalizarDieta
	 * @param mezcla @todo
	 * @param numeroSolicitud @todo
	 * @param dosificacion 
	 * @param volumentTotal
	 * @return
	 */
	
	public int insertarOrdenDieta(Connection con,int codigoEncabezado, String volumenTotal,String unidadVolumenTotal, String velocidadInfusion, int farmacia,
			 					  							  boolean nutricionOral, boolean nutricionParenteral, boolean finalizarDieta, int mezcla, int numeroSolicitud, String codigoInstitucion, String dosificacion);
	

	/**
	 * Metodo Para Insertar Los cuidados especiales de emfermeria 
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @return
	 */
	public int insertarOrdenCuidadoEnf(Connection con, int codigoOrden, String login, String datosMedico);

	/**
	 * Metodo Para Insertar Los detalles de cuidados especiales de emfermeria
	 * @param con
	 * @param codigoOrdenCuidadoEnf
	 * @param codigosTipoCuidado
	 * @param presenta
	 * @param descripcion
	 * @param OtroCuidadoEnf
	 * @return
	 */
	
	public int insertarDetalleOrdenCuidadoEnf(Connection con, int codigoOrdenCuidadoEnf, int codigosTipoCuidado, String presenta, String descripcion, int OtroCuidadoEnf);

	/**
	 * Funcion para cargar la orden Medica de un paciente Especifico
	 * @param con
	 * @param codigoCuenta
	 * @return Collection con los datos de la orden médica
	 */
	public Collection cargarOrdenMedica(Connection con, int codigoCuenta);

	/**
	 * Metodo para consultar el historico de nutricion parenteral 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param mezcla @todo
	 * @return
	 */
	public Collection consultarNutricionParentHisto(Connection con, int codigoCuenta, int cuentaAsocio, int mezcla);
	
	/**
	 * Metodo para consultar el historico de nutricion Oral  
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarNutricionOralHisto(Connection con, int codigoCuenta, int cuentaAsocio);
	
	
	
	/**
	 * Funcion para retornar los historicos de los monitoreos a una persona especifica 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarMonitoreosHisto(Connection con, int codigoCuenta, int cuentaAsocio);

	/**
	 * Metodo para consultar los historicos del soporte respiratorio
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarSoporteRespiraHisto(Connection con, int codigoCuenta, int cuentaAsocio);

	/**
	 * Metodo para consultar los historicos de cuidado enfermeria 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @param fechaFinal 
	 * @param fechaInicial 
	 * @return
	 */
	public Collection consultarCuidadosEnfHisto(Connection con, int codigoCuenta, int cuentaAsocio, int institucion, int centroCosto, String fechaInicial, String fechaFinal);
	
	/**
     * Metodo para consultar el tipo de monitoreo para una orden médica
     * @param con
     * @param codigoOrden
   * @return Collection con los datos del tipo de monitoreo
	 */
	public Collection cargarTipoMonitoreo(Connection con, int codigoOrden);
	
	 /**
     * Metodo para consultar y cargar la información en la sección soporte respiratorio 
     * @param con
     * @param codigoCuenta
     * @param asocio
     * @param institucion
     * @param centroCosto
     * @return Collection con los datos del soporte respiratorio
     */
    public Collection cargarSoporteRespiratorio(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto);
    
    /**
     * Metodo para consultar y cargar la información de la dieta
     * @param con
     * @param codigoCuenta
     * @return Collection con los datos de la última dieta
     */
    public Collection cargarDieta(Connection con, int codigoCuenta); 
    
    /**
     * Metodo para consultar y cargar la información de la nutrición oral
     * @param con
     * @param codigoCuenta
     * @param asocio
     * @param institucion
     * @param centroCosto
     * @return Collection con los datos de la última nutrición oral
     */
    public Collection cargarNutricionOral(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto); 
    
	/**
	 * Metodo para insertar el encabezado historico de todas las ordenes (nutricion--soporte-monitoreo)
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param fechaOrden
	 * @param horaOrden
	 * @param login
	 * @param datosMedico
	 * @return
	 */
	public int insertarEncabezadoOrdenMedica(Connection con,int codigoOrden, String fechaOrden, String horaOrden, String login, String  datosMedico);
	
	/**
	 * Metodo para insertar otra nutrición oral
	 * @param con
	 * @param otroNutOral 
	  * @return
	 **/
	public int insertarOtroNutricionOral(Connection con, String otroNutOral);
	
	/**
	 * Metodo para insertar el detalle de otro nutrición oral
	 * @param con
	 * @param codigoDieta
	 * @param codigoOtroOral
	  * @return
	 **/
	public int insertarDetalleOtroNutriOral(Connection con, int codigoDieta, int codigoOtroOral);
	
	/**
 	 * Funcion que retorna una collecion con el listado de los otros tipos de nutrición oral a una cuenta específica
 	 * @param con
 	 * @param cuenta
 	 * @param cuentaAsocio
 	 * @return Collection 
 	 */
      public Collection consultarOtrosNutricionOral(Connection con, int cuenta, int cuentaAsocio );
      
      /**
       * Metodo para consultar y cargar la información de otros tipos de nutrición oral ingresados
       * @param con
       * @param codigoCuenta
       * @return Collection con los datos de los últimos tipos de nutrición oral ingresados
       */
      public Collection cargarOtroNutricionOral(Connection con, int codigoCuenta);
      
      /**
       * Metodo para cargar la información ingresada en la cuenta de asocio, referente a descripción del soporte, dieta
       * y observaciones generales 
       * @param con
       * @param cuentaAsocio
       * @return Collection con los datos de la cuenta de asocio
       */
      
      public Collection cargarDatosUrgencias(Connection con, int cuentaAsocio); 
      
      /**
   	 * Funcion que retorna una collecion con el listado de los otros tipos de cuidados de enfermería  a una(s) cuenta(s) específica
   	 * @param con
   	 * @param cuenta
   	 * @param cuentaAsocio
   	 * @return Collection 
   	 */
        public Collection consultarOtrosCuidadosEnfer(Connection con, int cuenta, int cuentaAsocio );
        
    /**
	 *  Metodo para insertar otro tipo de cuidado de enfermería
	 * @param con
	 * @param otroCuidadoEnf
	  * @return
	 **/
	public int insertarOtroTipoCuidadoEnf(Connection con, String otroCuidadoEnf);

	 /**
     * Metodo para cargar la información de la orden de la hoja
     * neurológica
     * @param con
     * @param codigoOrden
     * @return true si existe orden hoja neurológica
     */
	public Collection cargarOrdenHojaNeurologica(Connection con, int codigoOrden);

	/**
	 * Método que inserta o modifica la orden de la Hoja Neurológica
	 * @param con
	 * @param codigoOrden
	 * @param presenta
	 * @param observaciones
	 * @param finalizada
	 * @param fechaFin @todo
	 * @param horaFin @todo
	 * @param login @todo
	 * @return
	 */
	public int insertarModificarOrdenHojaNeurologica(Connection con, int codigoOrden, boolean presenta, String observaciones, boolean finalizada, String fechaFin, String horaFin, String login);

	
    /**
     * Método que consulta las mezclas parenterales y los articulos con su correspondiente
     * información de detalle
     * @param con
     * @param codigoCuenta
     * @param codigoCuentaAsocio
     * @param nroConsulta 
     * @return
     */
	public Collection consultarMezclasParenteral(Connection con, int codigoCuenta, int codigoCuentaAsocio, int nroConsulta);

    /**
     * Método que consulta la información de los articulos asociados a la mezcla en el ver anteriores
     * @param con
     * @param codigoCuenta
     * @param codigoCuentaAsocio
     * @param codMezcla
     * @param codEncaMin
     * @param codEncabezadoAnterior
     * @return
     */
	public Collection consultarDetalleMezclaAnteriores(Connection con, int codigoCuenta, int codigoCuentaAsocio, int codMezcla, int codEncaMin, int codEncabezadoAnterior);

	/**
	 * @param con
	 * @param codHistoricosParent
	 * @return @todo
	 */
	public int finalizarParenteral(Connection con, Vector codHistoricosParent);

	/**
	 * 
	 * @param con 
	 * @param orden
	 * @param solicitud
	 * @return
	 */
	public HashMap consultarMezclaModificar(Connection con, String orden, String solicitud);

	/**
	 * 
	 * @param con
	 * @param mezclaModificar
	 * @param paciente 
	 * @param medico 
	 * @return
	 */
	public boolean guardarModificacionMezcla(Connection con, HashMap mezclaModificar, UsuarioBasico medico, PersonaBasica paciente) throws IPSException;

	/**
	 * 
	 * @param con
	 * @param mezcla
	 * @return
	 */
	public boolean accionAnularMezcla(Connection con, HashMap mezcla);

	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public HashMap consultarFechasHistoCuidadosEspe(Connection con, int codigoCuenta, int codigoCuentaAsocio);
	
	
	/**
	 * Metodo para consultar el estado del Parametro Interfaz Nutricion
	 * @param con
	 * @return
	 */
	public String consultarParametroInterfazNutricion(Connection con);

	/**
	 * Metodo para consultar el Piso al que pertence una Cama
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarPisoCama(Connection con, int codigoCama);
	
	/**
	 * Metodo para consultar el Numero de la cama comun para la interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarNumeroCama(Connection con, int codigoCama);
	
	/**
	 * Metodo para consultar la Fecha de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarFechaDieta(Connection con, int codigoEncabezado);

	
	/**
	 * Metodo para consultar la Hora de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarHoraDieta(Connection con, int codigoEncabezado);

	/**
	 * Metodo para consultar la Fecha de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarFechaGrabacion(Connection con, int codigoEncabezado);

	/**
	 * Metodo para consultar la Hora de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarHoraGrabacion(Connection con, int codigoEncabezado);

	/**
	 * Metodo para Consultar el campo VIP del Convenio asociado al Ingreso del Paciente
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String consultarConvenioVip(Connection con, int codigoConvenio);

	/**
	 * Metodo para Consultar los tipos de dieta activos para la dieta actual del paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta);

	/**
	 * Metodo para Consultar la Descripcion de la Dieta del Paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String consultarDescripcionDieta(Connection con, int codigoCuenta);
	
	/**
	 * Método para consultar el arreglo de un campo de la seccion prescripcion diálisis según tipo de consulta
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> cargarArregloPrescripcionDialisis(Connection con,HashMap campos);
	
	/**
	 * Método para insertar una prescripcion de diálisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public int insertarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis);
	
	/**
	 * Método implementado para cargar el histórico de prescripciones diálisis de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoPrescripcionDialisis> getHistoricoPrescripcionDialisis(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar una prescripción de dialisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public int modificarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis);

	/**
	 * 
	 * @param con
	 * @param mezcla
	 * @return
	 */
	public String consultarDosificacionMezcla(Connection con, int mezcla,int codigoCuenta, int cuentaAsocio);
	
	/**
	 * Consulta las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap obtenerObservacionesMezcla(Connection con, HashMap parametros);
	
	/**
	 * Actualiza las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarObservacionesMezcla(Connection con, HashMap parametros);

	/**
	 * Metodo encargado de suspender una mezcla
	 * @param connection
	 * @param finaliza
	 * @param codigoHistoEnca
	 * @return
	 */
	public boolean suspenderMezcla (Connection connection, String finaliza,int codigoHistoEnca,String usuario);
	
	/**
	 * Metodo encargado de Finalizar una Mezcla, esto se hace desde el registro de enfermeria.
	 * Se actualiza el campo que se llama suspendido en true, y se llenan los campos de fecha, hora y usuario
	 * finaliza
	 * @param connection
	 * @param finalizar
	 * @param codigoHistoEnca
	 * @param usuario
	 * @return
	 */
	public boolean finalizarMezcla (Connection connection,String finalizar,int codigoHistoEnca,String usuario );
	

	/**
	 * 
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @return
	 */
	public ArrayList<Object> cargarResultadoLaboratorios(Connection con,int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente,boolean cargarParametrizacion,boolean esHistoricos);

	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public int insertaResultadosLaboratorios(Connection con,int codigoEncabezado,ArrayList<Object> resultadoLaboratorios);
	

	/**
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public  ArrayList<String> cargarOtrosDietaReproteHC(Connection con, int codigoCuenta);
	
	/**
	 * @param con
	 * @param codigoCuenta
	 * @return
	 * @throws SQLException
	 */
	public List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica(Connection con, int codigoCuenta) throws SQLException;
	
	
	/**
	 * Metodo encargado de actualizar la descripcion de un soporte respiratorio
	 * @param con
	 * @param codigoEncabezadoSoporteRespira
	 * @param descripcionSoporteRespiratorio
	 * @return
	 * @author hermorhu
	 */
	public boolean actualizarDescripcionSoporteRespiratorio (Connection con, int codigoEncabezadoSoporteRespira, String descripcionSoporteRespiratorio);
	
	/**
	 * Metodo encargado de consultar el historico de descripciones de soporte respiratorio
	 * @param con
	 * @param codigoCuenta
	 * @return
	 * @author hermorhu
	 */
	public String consultarDescripcionSoporteRespiratorio(Connection con, int codigoCuenta);
	
}
