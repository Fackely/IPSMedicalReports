/*
 * Creado en 4/02/2005
 *
 * Princeton S.A.
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
public interface UtilidadesDao
{
	/**
	 * Metodo para verificar si una solicitud de medicamentos tiene despachos
	 * @param con Conexiï¿½n con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene despachos, false de lo contrario
	 */
	public boolean hayDespachosEnSolicitud(Connection con, int numeroSolicitud);

	/**
	 * Mï¿½todo que carga la fecha de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @return String con la fecha del sistema formato BD
	 */
	public String capturarFechaBD(Connection con);

	/**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public String capturarHoraBD(Connection con);
	/**
	 * Mï¿½todo para cargar la Fecha-Hora del egreso de la cuenta Urgencias cuando
	 * se hace un Asocio de Cuenta
	 * @param con
	 * @param codigo_paciente
	 * @return
	 */
	public String capturarFechayHoraEgresoUrgenciasEnAsocio(Connection con,String idIngreso,String viaIngreso);
	
	/**
	 * Mï¿½todo para obtener el codigo del contrato el cual
	 * fue utilizado en su ï¿½ltima cuenta abierta
	 * @param con Conexiï¿½n con la BD
	 * @param codigoPaciente Cï¿½digo del paciente
	 * @return Codigo del ultimo contrato del paciente
	 */
	public int obtenerUltimoContrtatoPaciente(Connection con, int codigoPaciente);

	/**
	 * Metodo que retorna el codigo del Cie Actual.
	 * @param con
	 * @return
	 */
	public int codigoCieActual(Connection con);
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el destino de salida de un egreso de evoluciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param obtenerDestinoSalidaStr
	 * @return
	 */
	public int obtenerDestinoSalidaEgresoEvolucion(Connection con,int idCuenta);

	/**
	 * Mï¿½todo que carga el numero de solicitud
	 * de la ultima valoraciï¿½n de interconsulta
	 * @param con
	 * @param codigoPersona
	 * @return numeroSolicitud ï¿½ltima interconsulta
	 */
	public int obtenerUltimaValoracionInterconsulta(Connection con, int codigoPersona);
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para saber el nï¿½mero de solicitudes de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return nï¿½mero de solicitudes
	 */
	public int obtenerNumeroSolicitudesCuenta(Connection con,int idCuenta);

	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public int obtenerCodigoFactura(Connection con, int consecutivoFactura);

	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public int obtenerCodigoFactura(Connection con, int consecutivoFactura,int institucion);
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerFechaFacturacion(Connection con, int codigoFactura);

	/**
	 * Metodo que retorna la cuenta de cobro de una factura.
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerCuentaCobroFactura(Connection con, int codigoFactura);

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerEstadoFactura(Connection con, int codigoFactura);

	/**
	 * Este mï¿½todo devuelve los cï¿½digos de las justificaciones
	 * filtrando por instituciï¿½n
	 * @param con
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo
	 * @return Array de enteros con los cï¿½digos de las justificaciones
	 */
	public int[] buscarCodigosJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo);
	
	/**
	 * Este mï¿½todo devuelve los cï¿½digos y los nombres de las justificaciones
	 * filtrando por instituciï¿½n del usuario
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo
	 * @return Vector con los cï¿½digos y los nombres de los Atributos de la justificaciï¿½n
	 */
	public Vector buscarCodigosNombresJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo);
	
	/**
	 * Mï¿½todo que verifica si el tipo de identificaciï¿½n maneja consecutivo
	 * automï¿½tico
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public boolean esAutomaticoTipoId(Connection con,String acronimo,int codigoInstitucion);
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el nï¿½mero de la factura (prefijo+consecutivo)
	 * @param con
	 * @param numeroSolicitud
	 * @return numero de factura (prefijo+consecutivo)
	 */
	public String obtenerNumeroFactura(Connection con, int numeroSolicitud);
	

	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo que adiciona el diagnï¿½stico y tipo cie a pagos paciente.
	 * @param con
	 * @param codigoPaciente
	 * @param numeroDocumento
	 * @param diagnostico
	 * @param tipoCie
	 * @return -1 no exitoso, 1 exitoso
	 */
	public int actualizarDiagnosticoTopesPaciente(Connection con,
			int codigoPaciente,String numeroDocumento,String diagnostico, String tipoCie);
	
	/**
	 * Mï¿½todo para capturar el cï¿½digo de la ultima via de ingreso del paciente
	 * que no tenga cuenta activa.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int obtenerCodigoUltimaViaIngreso(Connection con,int codigoPaciente);
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo obtener el ID de la ï¿½ltima cuenta del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int obtenerIdUltimaCuenta(Connection con,int codigoPaciente);

    /**
     * Funciï¿½n que verifica el estado true o false de la autorizacion en la tabla atributo_solicitud
     * @param con
     * @param codAtributoSolicitud
     * @return
     */
    public boolean verificarAutorizacion(Connection con, int codAtributoSolicitud);

    /**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para verificar si un servicio o un artï¿½culo que requiera justificaciï¿½n
	 * ha sido justificado. 
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param codigoInstitucion
	 * @param esArticulo
	 * @return
	 */
	public boolean esSolicitudJustificada(Connection con,int numeroSolicitud,int parametro,int codigoInstitucion,boolean esArticulo);
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el cï¿½digo y la descripcion de cada atributo de la justificacion del medicamento
	 * o servicio
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param esArticulo
	 * @return
	 */
	public ResultSetDecorator obtenerAtributosJustificacion(Connection con,int numeroSolicitud,int parametro,boolean esArticulo);

	/**
	 * Metodo para cargar los datos del embarazo necesarios en la valoracion de gineco!
	 * Embarazada - FUR - FPP - Edad Gestacional
	 * @param con
	 * @param codigoPersona
	 */
	public Vector cargarDatosEmbarazo(Connection con, int codigoPersona);

	/**
	 * Mï¿½todo para el ï¿½ltimo tipo de parto
	 * @param con
	 * @param codigoPaciente
	 * @return InfoDatosInt con codigo y nombre del ï¿½ltimo tipo de parto
	 */
	public InfoDatosInt obtenerUltimoTipoParto(Connection con, int codigoPaciente);

	/**
	 * Metodo para cargar los datos de los antecedentes gineco-obstetricos necesarios en la valoracion de gineco!
	 * EdadMenarquia - OtraEdadMenarquia - EdadMenopausia - OtraEdadMenopausia
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public Vector cargarDatosAntGineco(Connection con, int codigoPersona);

	/**
	 * Mï¿½todo para obtener el ï¿½ltimo nï¿½mero del embarazo del paciente
	 * @param con -> Connection
	 * @param codigoPaciente -> int
	 * @return numeroEmbarazo -> int
	 */
	public int obtenerUltimoNumeroEmbarazo(Connection con, int codigoPaciente);

	/**
	 * @param con
	 * @param padre
	 * @return
	 */
	public int[] consultarTiposPartoVaginal(Connection con, String padre);

	/**
	 * Mï¿½todo para consultar embarazos sin finalizar
	 * @param con Conexion con la BD
	 * @param codigoPaciente Codigo del paciente
	 * @param codigoEmbarazo Codigo del embarazo
	 * @return true si el embarazo no se a finalizado
	 */
	public boolean esEmbarazoSinTerminar(Connection con, int codigoPaciente, int codigoEmbarazo);

	/**
	 * Mï¿½todo para consultar en la BD el nombre de la edad menarquia
	 * o menopausia
	 * @param con
	 * @param codigoRango
	 * @param esMenarquia true consulta la menarqui, false la menopausia
	 * @return Striong con el nombre obtenido
	 */
	public String obtenerNombreEdadMenarquiaOMenopausia(Connection con, int codigoRango, boolean esMenarquia);
	
	/**
	 * Mï¿½todo para obtener el ï¿½ltimo valor de la altura uterina en la valoraciï¿½n gineco-obstï¿½trica
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaciente
	 */
	
	public String obtenerAlturaUterinaValoracion(Connection con, int institucion, int codigoPaciente);

	/**
	 * Mï¿½todo para obtener el noimbre del concepto de menstruaciï¿½n
	 * recibiendo como parï¿½mero el cï¿½digo
	 * @param con Connection Conexiï¿½n con la BD
	 * @param conceptoMenstruacion int Codigo del concepto Menstruaciï¿½n
	 * @return
	 */
	public String obtenerNombreConceptoMenstruacion(Connection con, int conceptoMenstruacion);
	
	/**	 
	 * Mï¿½todo usado para obtener los pooles vigentes en los cuales 
	 * se encuentra el medico, 
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param fecha String
	 * @param codMedico int  
	 * @return ResultSet
	 * @author jarloc
	 */
	public ResultSetDecorator obtenerPoolesMedico(Connection con, String fecha, int codMedico, boolean activos);

	/**
	 * @param con
	 * @param nombreSecuencia
	 * @return
	 */
	public int getSiguienteValorSecuencia(Connection con, String nombreSecuencia);
	

	
	/**
	 * @param con
	 * @param idCuenta
	 * @param esquemaTarifario
	 * @return
	 */
	public Vector obtenerPoolesCuenta(Connection con, int idCuenta);

	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public int obtenerEstadoCuentaCobro(Connection con, double cuentaCobro, int institucion);

	/**
	 * @param con
	 * @param cuentaCobro
	 * @return
	 */
	public String obtenerFechaRadicacionCuentaCobro(Connection con, double cuentaCobro);
	
	/**
	 * Mï¿½todo que consulta los datos del tipo de monitoreo
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public String[] getTipoMonitoreo(Connection con,int tipoMonitoreo);
	
	/**
	 * Mï¿½todo usado para consultar la fecha de la orden de salida
	 * de una cuenta de Urgencias u Hospitalizaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param institucion
	 * @return si no se encuentra la fecha devuelve cadena vacï¿½a
	 */
	public String obtenerFechaOrdenSalida(Connection con,int idCuenta,int institucion);
	
	/**
	 * Mï¿½todo que retorna TRUE para saber si la cuenta tiene solicitud de estancia
	 * para la fecha estipulada, de lo contrario retorna false
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public boolean haySolicitudEstancia(Connection con,int idCuenta,String fechaEstancia,int institucion);
	
	/**
	 * Mï¿½todo para consultar los datos generales de una persona en AXIOMA
	 * sin importar si es mï¿½dico, usuario o paciente
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public HashMap obtenerDatosPersona(Connection con,String tipoId,String numeroId);

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerSaldoFactura(Connection con, int codigoFactura);
	
	/**
	 * Mï¿½todo usado para verificar si en la fucnionalidad de 2ï¿½ Nivel
	 * se debe esconder los datos del paciente del cabezaote superior
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean deboEsconderPaciente(Connection con,int codigo);

	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public boolean existeAjusteNivelFacturaEmpresa(Connection con, double codigo, int factura);

	/**
	 * @param con
	 * @param cuentacobro
	 * @param institucion
	 * @return
	 */
	public int numFacturasCuentaCobro(Connection con, double cuentacobro, int institucion);

	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public int numServicosArticulosFactura(Connection con, int factura);

	/**
	 * @param con
	 * @param detFactSolicitud
	 * @return
	 */
	public double obtenerPorcentajePoolFacturacion(Connection con, int detFactSolicitud);

	/**
	 * @param con
	 * @param loginUsuario
	 * @param codigo_func
	 * @return
	 */
	public boolean tieneRolFuncionalidad(Connection con, String loginUsuario, int codigo_func);

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean isDefinidoConsecutivoAjustesDebito(Connection con, int institucion);

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean isDefinidoConsecutivoAjustesCredito(Connection con, int institucion);

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public InfoDatosInt obtenerConvenioCuentaCobro(Connection con, double cuentaCobro, int institucion);

	/**
	 * @param con
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public String obtenerDescripcionConceptoAjuste(Connection con, String conceptoAjuste, int institucion);

	/**
	 * @param con
	 * @param metodoAjuste
	 * @return
	 */
	public String obtenerDescripcionMetodoAjuste(Connection con, String metodoAjuste);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public int obtenerFacturaAjuste(Connection con, double codigoAjuste);

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerConsecutivoFactura(Connection con, int codigoFactura);

	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public InfoDatosInt obtenerConvenioFactura(Connection con, int factura);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public String obtenerFechaAprobacionAjustes(Connection con, double codigoAjuste);
	
	/**
	 * Mï¿½todo usado para consultar la fecha de reversiï¿½n de egreso
	  * @param con
	 * @param idCuenta
	 * @return fecha de reversiï¿½n
	 */
	public String obtenerFechaReversionEgreso(Connection con,int idCuenta);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteDeReversion(Connection con, double codigoAjuste);

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public String obtenerestadoAjuste(Connection con, double codigoAjuste);

	/**
	 * @param con
	 * @param numeroAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public double obtenercodigoAjusteEmpresa(Connection con, String numeroAjuste, String tipoAjuste, int institucion);
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo usado para obtener el nombre de un tipo de sala
	 * parametrizado en el sistema
	 * @param con
	 * @param codigoTipoSala
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTipoSala(Connection con,int codigoTipoSala);
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo que consulta el nombre [0] y acrï¿½nimo [1] del tipo
	 * de asocio.
	 * @param con
	 * @param codigoTipoAsocio
	 * @return
	 */
	public String[] obtenerNombreTipoAsocio(Connection con,int codigoTipoAsocio);
	
	/**
	 * Mï¿½todo que consulta la descripciï¿½n de un esquema tarifario
	 * esGeneral => "true" se consultarï¿½ un esquema tarifario general de la tabla  esq_tar_porcen_cx
	 * esGeneral=> "false" se cosnultarï¿½ un esquema tarifario especï¿½fico de la tabla esquemas_tarifarios
	 * @param con
	 * @param esGeneral
	 * @param codigo
	 * @param obtenerNomEsquemaTarifarioStr
	 * @return retorna null si no hay resultados
	 */
	public String obtenerNomEsquemaTarifario(Connection con,String esGeneral,int codigo);
	
	
	/**
	 * Metodoq eu me retorna el nombre de un convenio dado la cuenta
	 * @param con
	 * @param codigoCnvenio
	 * @return
	 */
	public String obtenerNombreConvenio(Connection con, int cuenta);

    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public String obtenerEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion);

    
    /**
     * @param con
     * @param institucion
     * @param nombreConsecutivo
     * @return
     */
    public boolean isDefinidoConsecutivo(Connection con, int institucion, String nombreConsecutivo);

    
    /**
     * @param con
     * @param codigoFactura
     * @param estado
     * @return
     */
    public boolean cambiarEstadoPacienteFactura(Connection con, int codigoFactura, int estado);

    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @return
     */
    public int obtenerCodigoFacturaPagosPaciente(Connection con, int codigoTipoDocumento);

    
    /**
     * 
     * @param con
     * @param codigoPago
     * @param codigoPagoPaciente
     * @return
     */
    public boolean cambiarEstadoPagoFacturaPacient(Connection con, int codigoPago, int codigoPagoPaciente);

    
    /**
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public boolean cambiarEstadoPagosGeneralEmpresa(Connection con, int codigoPago, int codigoEstado);

    
    /**
     * @param con
     * @param codigoPago
     * @return
     */
    public String obtenerEstadoPagosEmpresa(Connection con, int codigoPago);

    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public int pacienteAbonoReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion);

    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public int numCajasAsociadasUsuario(Connection con, String loginUsuario);

    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion);

	/**
	 * Numero de cajas de un usuario por centro de atenciï¿½n, segï¿½n el o los tipos de caja
	 * @param con
	 * @param loginUsuario
	 * @param codigoCentroAtencion
	 * @param tipos
	 * @return
	 */
    public int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion, int tipos[]);

    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public ResultSetDecorator getConsecutivoCajaUsuario(Connection con, String loginUsuario, String codigoCentroAtencion);

    
    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public int getCodigoCaja(Connection con, int consecutivoCaja);

    
    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public String getDescripcionCaja(Connection con, int consecutivoCaja);
    
    /**
     * Mï¿½todo que verifica si una solicitud de cirugï¿½as tiene un pedido Qx.
     * asociado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean tienePedidoSolicitudQx(Connection con,int numeroSolicitud);
    
    /**
     * Mï¿½todo que verifica si una Peticion Qx tiene solicitud asociada
     * @param con
     * @param numeroPeticion
     * @return
     */
    public boolean tieneSolicitudPeticionQxStr(Connection con,int numeroPeticion);
    
    /**
     * Mï¿½todo que consulta la subcuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getSubCuentaSolicitud(Connection con,int numeroSolicitud);
    
    /**
     * Mï¿½todo que consulta la cuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getCuentaSolicitud(Connection con,int numeroSolicitud);
    
    /**
     * Mï¿½todo que consulta el nombre de una especialidad
     * @param con
     * @param especialidad
     * @return
     */
    public String getNombreEspecialidad(Connection con,int especialidad);
    
    /**
     * Mï¿½todo que consulta el nombre de un pool
     * @param con
     * @param pool
     * @return
     */
    public String getNombrePool(Connection con,int pool);
    
    /**
     * Mï¿½todo que retorna la peticiï¿½n de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getPeticionSolicitudCx(Connection con,int numeroSolicitud);
    
    /**
     * Mï¿½todo que consulta la fecha del cargo de una orden quirï¿½rgica
     * retorna una cadena vacï¿½a en el caso de que la orden no tenga cargo generado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public String getFechaCargoOrdenCirugia(Connection con,int numeroSolicitud);
    
    /**
     * Mï¿½todo para saber si un convenio tiene (False - True) el atributo
	 * de info_adic_ingreso_convenios
     * @param con
     * @param codigoConvenio
     * @return
     */
	public boolean convenioTieneIngresoInfoAdic(Connection con, int codigoConvenio);
	
	/**
	 * Mï¿½todo para obtener el saldo de un convenio dada su cuenta y el convenio
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
    public double obtenerSaldoConvenio(Connection con,String idSubCuenta);
    
    /**
     * Mï¿½todo para obtener el codigo de un convenio segun una cuenta dada
     * @param con
     * @param idCuenta
     * @return
     */
    public int obtenerCodigoConvenio(Connection con, int idCuenta);    
	
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerFechaAdmisionUrg(Connection con, int idCuenta);
 
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerFechaAdmisionHosp(Connection con, int idCuenta);

    /**
     * Mï¿½todo para obtener el tipo de anestesia de una solicitud de cirugï¿½as
     * @param con
     * @param numeroSolicitud
     * @return
     */
	public InfoDatosInt obtenerTipoAnestesia(Connection con, int numeroSolicitud);
    
    /**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @param consultaHora String con la sentencia para consultar la hora y los segundos de acuerdo a la BD
	 */
	public String capturarHoraSegundosBD(Connection con);
	
	/**
	 * Mï¿½todo para obtener el nombre de un grupo de servicio dado su codigo
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public String getNombreGrupoServicios(Connection con, int codigoGrupo);
	
	/**
	 * Mï¿½todo para obtener el total de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 */
	public double obtenerTotalPresupuesto(Connection con, int codigoPresupuesto);
    
	/**
	 * Mï¿½todo para obtener el total de un grupo de servicios dado, relacionado
	 * con un presupuesto de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public double obtenertotalGrupoServicios(Connection con, int presupuesto, int grupo);
	
	/**
	 * carga el esuqema tarifario de una solicitud de cx
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int getEsquemaTarifarioSolCx(Connection con, String codigo);
	
	/**
	 * Mï¿½todo para obtener el total de los articulos de un presupuesto
	 * @param con
	 * @param presupuesto
	 * @return
	 */
	public double obtenerTotalArticulosPresupuesto(Connection con, int presupuesto);

    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int obtenerNumeroOrdenSolicitud(Connection con, int numeroSolicitud);

    /**
     * 
     * @param con
     * @param codDetFactura
     * @return
     */
    public int numAsociosCirugiaDetFactura(Connection con, int codDetFactura);
    /**
	 * Mï¿½todo apra obtener el total de un grupo de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public int obtenerTotalGrupoArticulos(Connection con, int presupuesto, int grupo);
	
	/**
	 * Mï¿½todo para obtener el total de un subgrupo de articulos
	 * @param con
	 * @param presupuesto
	 * @param subgrupo
	 * @return
	 */
	public int obtenerTotalSubGrupoArticulos(Connection con, int presupuesto, int subgrupo);
	
	/**
	 * Mï¿½todo para obtener el total de los articulos de  un presupuesto dado asociado por calse de inventario
	 * @param con
	 * @param presupuesto
	 * @param claseinventario
	 * @return
	 */
	public int obtenerTotalClaseInventarioArticulos(Connection con, int presupuesto, int claseinventario);

	/**
	 * Mï¿½todo que me indica si una cuenta tiene subcuentas
	 * independiente del estado de la misma
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean haySubcuentasParaCuentaDada(Connection con, int codigoCuenta);

	/**
     * Mï¿½todo que consulta la fecha de apertura de la cuenta de un paciente dado
	 * @param con
	 * @param numeroCuenta
	 * @return
	 */
	public String getFechaAperturaCuenta(Connection con, int numeroCuenta);

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String getFechaSolicitud(Connection con, int numeroSolicitud);
	
	/**
	 * Mï¿½todo para obtener el consecutivo de ordenes medicas dado su numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int getConsecutivoOrdenesMedicas(Connection con, int numeroSolicitud);
	
	/**
	 * Mï¿½todo para obtener las observaciones de la informacion adicional de un paciente cargado en session
	 * @param con
	 * @param codPaciente
	 * @return
	 */
	public String getObservacionesPacienteActivo(Connection con, int codPaciente);
	
	/**
	 * obtener el codigo de la cuenta asociada dado el codigo de la cuenta final
	 * retorna "" en caso de que no exista
	 */
	public String getCuentaAsociada(Connection con, String codigoCuentaFinal,boolean activo);
	
	/**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public  String obtenerHoraAdmisionHosp(Connection con, int idCuenta);
    
    /**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerHoraAdmisionUrg(Connection con, int idCuenta);

    /**
     * Metodo que retorna el nombre del responsable de una factura.
     * @param con
     * @param codFactura
     * @return
     */
	public String getResponsableFactura(Connection con, int codFactura);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucionInt
	 * @param numReciboCaja
	 * @return
	 */
	public double obtenerValorTotalReciboCaja(Connection con, int codigoInstitucionInt, String numReciboCaja);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param numReciboCaja
	 * @return
	 */
	public String reciboCajaRecibidoDe(Connection con, int institucion, String numReciboCaja);

	/**
	 * Metodo que retorna la informacion basica de la anulacion de una factura.
	 * codigoFactura+separadorSplit+fechaanulacion+separadorSplit+horaanulacion+separadorSplit+usuarioanula+separadorSplit+consecutivoanulacion;
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerInformacionAnulacionFactura(Connection con, String codigoFactura);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerCodigoEstadoFacturacionFactura(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerCodigoEstadoPacienteFactura(Connection con, int codigoFactura);

		/**
	 * obtiene el total admin farmacia
	 * @param con
	 * @param codigoArticuloStr
	 * @param numeroSolicitudStr
	 * @param traidoUsuario
	 * @return
	 */
	public int getTotalAdminFarmacia(Connection con, String codigoArticuloStr, String numeroSolicitudStr, boolean traidoUsuario);
	
	/**
	 * Mï¿½todo que devuelve el nombre del ï¿½ltimo tipo de monitoreo del paciente cuando estï¿½ en Cama UCI,
	 * ya sea de la orden mï¿½dica o la evoluciï¿½n
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @param obtenerNombre @todo
	 * @return nombre del ï¿½ltimo tipo de monitoreo
	 */
	public String obtenerUltimoTipoMonitoreo (Connection con, int codigoCuenta, int institucion, boolean obtenerNombre);
	
	/**
	 * Mï¿½todo implementado para obtener el codigo de la cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public int getUltimaCamaTraslado(Connection con,int cuenta);
	
	/**
	 * Metodo para obtener el codigo de la ocupacion Medica dado el codigo del medico.
	 * Retorna -1 si no encuentra la ocupacion con el codigo del medico dado.
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
    public int obtenerOcupacionMedica(Connection con, int codigoMedico);

    /**
     * 
     * @param con
     * @param codigoCama
     * @return
     */
	public int obtenerCodigoEstadoCama(Connection con, int codigoCama);
	
    /**
     * Metodo que retorna el numero de registros de la consulta.
     * @param con
     * @param Sentencia
     * @return
     */
	public int nroRegistrosConsulta(Connection con, String consulta);
	
	/**
	 * Mï¿½todo que consulta la fecha de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getFechaEgreso(Connection con,int idCuenta);
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion completa
	 * del diagnï¿½stico de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getNombreDiagnosticoEgreso(Connection con,int idCuenta);
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion compelta
	 * del diagnï¿½stico de ingreso de un admision hospitalaria
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getNombreDiagnosticoIngreso(Connection con,int idCuenta);
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirDetalleTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato);
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirDetalleTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato);
	
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato);
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de servicios parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato);

	/**
	 * Mï¿½todo para actualizar en la agenda el medico que atiende una cita 
	 * @param con
	 * @param codigoMedico
	 * @param codigoAgenda
	 * @return
	 */
	public boolean actualizarMedicoAgenda(Connection con, int codigoMedico, int codigoAgenda);
	
	/**
	 * Metodo para saber si un centro de costo esta asociado a un usuairo x alamacen
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean centroCostoAsociadoUsuarioXAlmacen(Connection con, int centroCosto, int institucion);
	
	/**
	 * Mï¿½todo para saber si existe un centro de costo en la tabla centros_costo
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean existeCentroCosto(Connection con, int centroCosto, int institucion);
	
	
	/**
	 * Mï¿½todo para saber si un centro de costo esta asociado a algun usuario
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public boolean centroCostoAsociadoUsuario(Connection con, int centroCosto);

	/**
	 * Mï¿½todo usado para obtener el nombre de un centro de atenciï¿½n,
	 * de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreCentroAtencion(Connection con, int consecutivoCentroAtencion);
	
	/**
	 * Metodo para obtener el nombre (razon social) de la institucion 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public String obtenerNombreInstitucion(Connection con, int institucion);
	
	/**
	 * Mï¿½todo para obtener el nombre de un centro de costo dado su codigo para una
	 * institucion determinada
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public String obtenerNombreCentroCosto(Connection con, int centroCosto, int institucion);
	
	/**
	 * Mï¿½todo para obtener el nombre de una via de ingreso dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreViaIngreso(Connection con, int codigo);

	/**
	 * 
	 * @param con
	 * @param codigoArea
	 * @return
	 */
	public int obtenerCentroAtencionPaciente(Connection con, int codigoArea);

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCentroCostoTratante
	 * @return
	 */
	public boolean actualizarAreaCuenta(Connection con, int codigoCuenta, int codigoCentroCostoTratante);
	
	/**
	 * Mï¿½todo que consulta los centros de costo de una vï¿½a de ingreso
	 * en un centro de atenciï¿½n especï¿½fico
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @param tipo paciente
	 * @return
	 */
	public HashMap getCentrosCostoXViaIngresoXCAtencion(Connection con,int viaIngreso,int centroAtencion,int institucion, String tipoPaciente);

	
	/**
	 * Metodo que consulta el nombre del convenio dado el codigo del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNombreConvenioOriginal(Connection con, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerLLaveTraigeAdmisionUrgeciasXCuenta(Connection con, int idCuenta);

	/**
	 * 
	 * @param con
	 * @param numOrdenDieta
	 * @param codigoArticulo
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarCantidadArticuloSolMedicamentos(Connection con, String numSolicitud, String codigoArticulo, String cantidad);
	
	/**
	 * Metodo para saber si una orden de dieta esta finalizada. 
	 * como la finalizacion genera otro registro en orden_dieta,  
	 * entonces buscar los registros siguientes a este.  
	 * @param con
	 * @param codigoOrdenDieta
	 */
	public boolean getOrdenDietaFinaliza(Connection con, String codigoOrdenDieta, int codigoCuenta, int codigoCuentaAsocio);

	/**
	 * Mï¿½todo para obtener el valor inicial de la cuenta de cobro
	 * @param con
	 * @param codigoCuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public double obtenerValorInicialCuentaCobroCapitacion(Connection con, int codigoCuentaCobro, int codigoInstitucion);
	
	/**
	 * Mï¿½etodo apra saber si como tiene parametrizado un formato de impresion 
	 * determinado el campo que indica si se deben mostrar los servicios qx o no 
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public boolean getMostrarServiciosQx(Connection con, int codigoFormato);

	/**
	 * Utilidad para obtener los datos de los campos parametrizables
	 * @param con
	 * @param medico
	 * @param funcionalidad
	 * @param seccion
	 * @param centroCosto
	 * @param institucion
	 * @param codigoTabla
	 * @return
	 */
	public HashMap<Object, Object> obtenerInformacionParametrizada(Connection con, int medico, String funcionalidad, String seccion, int centroCosto, String institucion, int codigoTabla);

	/**
	 * Mï¿½todo para obtener el nï¿½mero de la ï¿½ltima cuenta de cobro
	 * de capitaciï¿½n
	 * @param con -> Connection
	 * @param codigoInstitucion
	 * @return ultimaCuentaCobro
	 */
	public int obtenerUltimaCuentaCobroCapitacion(Connection con, int codigoInstitucion);
	
	/**
	 * Mï¿½todo para obtener el codigo del registro de enfermeria para una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoRegistroEnfermeria(Connection con, int idCuenta);

	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param contrato 
	 * @return
	 */
	public boolean esNivelServicioContratado(Connection con, String servicio, int contrato);

	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de calificaciï¿½n
	 * pyp de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTipoCalificacionPyP(Connection con, int consecutivo);
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de tipo de transacciï¿½n de inventario
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTransaccionInventario(Connection con,int consecutivo);
	
	/**
	 * Mï¿½todo implementado para consultar el nombre de una ocupaciï¿½n mï¿½dica
	 * @param con
	 * @param codigoOcupacion
	 * @return
	 */
	public String obtenerNombreOcupacionMedica(Connection con,int codigoOcupacion);

	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @param totalSubCuenta
	 * @return
	 */
	public boolean actualizarTotalSubCuenta(Connection con, int codigoSubCuenta, double totalSubCuenta);

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public String obtenerTipoServicio(Connection con, String servicio);

	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifarioCups
	 * @return
	 */
	public String obtenerCodigoPropietarioServicio(Connection con, String servicio, int codigoTarifario);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerEstadoHistoriaClinicaSolicitud(Connection con, String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerTipoSolicitud(Connection con, String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public String[] obtenerRango_UnidadMedidaGrupoEtareoPYP(Connection con, String grEtareo);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudPYP(Connection con, int numeroSolicitud);

	
	/**
	 * 
	 * @param con
	 * @param numeroOrden
	 * @param institucion
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroOrden, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean asignarSolicitudToActividadPYP(Connection con, String codigoOrden, String numeroSolicitud);
	
	/**
	 * M+etodo que verifica si el convenio de una solicitud tiene
	 * activo el campo de unificar PYP
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean tieneConvenioUnificarPYP(Connection con,String numeroSolicitud);

	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmbulatoriaArticulos(Connection con, String ordenAmbulatoria);
	
	/**
	 * Mï¿½todo implementado para actualizar el acumulado de PYP por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @return
	 */
	public int actualizarAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoActividad
	 * @param estado
	 * @param loginUsuario
	 * @param comentario
	 * @return
	 */
	public boolean actualizarEstadoActividadProgramaPypPaciente(Connection con, String codigoActividad, String estado, String loginUsuario, String comentario);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoriaActividad(Connection con, String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroSolicitud);
	
	/**
	 * Mï¿½todo que verifica si en Actividades por Programa estï¿½
	 * definida como requerida 
	 * @param con
	 * @param codigoActividadProgPypPac
	 * @return el codigo de la actividad programa pyp si estï¿½ requerido sino retorna vacï¿½o
	 */
	public String esRequeridoActividadesXPrograma(Connection con, String codigoActividadProgPypPac);

	/**
	 * Mï¿½todo que consulta si existen programas y actividades por convenio, y
	 * verifica si estï¿½n activas 
	 * @param con
	 * @param actividadProgramaPyp
	 * @param convenio 
	 * @return true si hay actividad(es) en true para la actividad de programa pyp
	 */
	public boolean activaProgramaActividadesConvenio (Connection con, String actividadProgramaPyp, String convenio);

	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public String obtenerSexoGrupoEtareoPYP(Connection con, String grEtareo);

	/**
	 * Mï¿½todo usado para obtener el nombre de una cuenta contable
	 * de acuerdo al codigo enviado como parï¿½metro
	 * @param con
	 * @param codigo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreCuentaContable(Connection con,int codigo);
	
	/**
	 * metodo que obtiene el nombre de la finalidad del servicio dado el codigo de la finalidad
	 * @param con
	 * @param codigoFinalidad
	 * @return
	 */
	public String getNombreFinalidadServicio(Connection con, String codigoFinalidad);
	
	/**
	 * metodo que obtiene tipo del servicio dado el codigo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public String getTipoServicio(Connection con, String codigoServicio);

	/**
	 * 
	 * @param con 
	 * @param codigoConvenio
	 * @return
	 */
	public double obtenerPorcentajePypContratoConvenio(Connection con, String codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerFrecuenciaTipoFrecSolPoc(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerViaIngresoFactura(Connection con, String codigoFactura);

	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerCuentaFactura(Connection con, String codigoFactura);

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esActividadSolicitudCubiertaConvenio(Connection con, String numeroSolicitud, String codigoPaciente, String codigoConvenio);

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String esSolicitudFacturada(Connection con, String numeroSolicitud);
	
	/**
	 * Mï¿½todo que verifica que el paciente no tenga cuentas abiertas en otros centros de atencion
	 * y en el caso de que la encuentre retorna la descripcion de su centro de atencion asociado
	 * @param con
	 * @param codigoPaciente
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public String getCentroAtencionIngresoAbiertoPaciente(Connection con,String codigoPaciente,String institucion,String centroAtencion);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String[] obtenerCentroAtencionFactura(Connection con, int codigoFactura);

	/**
	 * Mï¿½todo usado para obtener el nombre del usuario dado el login y el parï¿½metro primeroApellidos
	 * para indicar como se quiere el orden primero los nombres o los apellidos
	 * @param con
	 * @param login
	 * @param primeroApellidos
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreUsuarioXLogin(Connection con, String login, boolean primeroApellidos);
	
	/**
	 * Mï¿½todo implementado para consultar el centro de atencion de la ultima cuenta
	 * activa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String getNomCentroAtencionIngresoAbierto(Connection con,String codigoPaciente);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @return
	 */
	public String obtenerCodigoActividadPorPrograma(Connection con, int institucion, String programa, String actividad);
	
	/**
	 * Mï¿½todo que verifica si una solicitud estï¿½ asociada a una cita
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeCitaParaSolicitud(Connection con,String numeroSolicitud);

	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public String obtenerCodigoActividadDadaOrdenAmbulatoria(Connection con, String ordenAmbulatoria);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoConvenioSolicitud(Connection con, String numeroSolicitud);

	/**
	 * 
	 */
	public HashMap obtenerEspecialidades(Connection con,HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param codigoEspecilidad
	 * @return
	 */
	public HashMap obtenerMedicosEspecialidad(Connection con, String codigoEspecilidad);

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerUnidadesConsulta(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoEspecilidad
	 * @return
	 */
	public HashMap obtenerMedicosUnidadConsulta(Connection con, String codigoUnidadConsulta);

	/**
	 * 
	 * @param con
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public HashMap obtenerAgendaDisponibleMedicoUnidadConsulta(Connection con, String codigoUnidadConsulta,String codigoMedico);

	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public String obtenerNaturalezaServicio(Connection con, String servicio);

	/**
	 * 
	 * @param con
	 * @param actividad
	 * @return
	 */
	public String obtenerArtSerActividadPYP(Connection con, String actividad);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public ArrayList obtenerCiudades(Connection con);

	/**
	 * Método que consulta las localidades relacionadas con la ciudad
	 * @param con
	 * @return
	 */
	public ArrayList obtenerLocalidadesCiudades(Connection con);
	
	/**
	 * Mï¿½todo que consulta las ciudades por pais
	 * @param con
	 * @param codigoPais
	 * @return
	 */
	public ArrayList obtenerCiudadesXPais(Connection con,String codigoPais);
	/**
	 * 
	 * @param con
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @return
	 */
	public ArrayList obtenerConvenios(Connection con, HashMap campos);

	
	/**
	 * 
	 * @param con
	 * @param tipoFiltro
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerTiposIdentificacion(Connection con,String tipoFiltro,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerCodigoIngresoDadaCuenta(Connection con, String idCuenta);

	/**
	 * 
	 * @param con
	 * @param tipoSolicitud 
	 * @param codigoEstadoHCRespondida
	 * @return
	 */
	public ArrayList obtenerSolicitudesEstado(Connection con, int estado, int tipoSolicitud);
	
	/**
	 * Mï¿½todo implementado para consultar la fecha de ingreso de una cuenta dependiendo de su via de ingreso
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @return
	 */
	public String getFechaIngreso(Connection con,String idCuenta,int viaIngreso);

	/**
	 * 
	 * @param con
	 * @param tipoId
	 * @param numeroIdentificacion
	 * @return
	 */
	public String getCodigoPaisDeptoCiudadPersona(Connection con, String tipoId, String numeroIdentificacion);
	
	/**
	 * 
	 * @param idCuenta
	 * @param fechaFormatoApp
	 * @return
	 */
	public boolean esCamaUciDadaFecha(Connection con, String idCuenta, String fechaFormatoApp, String hora);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esMotivoSircUsado(Connection con, String consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esServicioSircUsado(Connection con, String codigo, String institucion);
	
	
	/**	  
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public boolean esConceptosPagoPoolesUsado(Connection con, String codigo, String institucion);
	
	
	/**	  
	 * Metodo que indica si se esta
	 * usando un registro de entidades subcontratadas
	 * en otra tabla
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public boolean esEntidadSubContratadaUsado (Connection con, String codigo, String institucion);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap obtenerCodigoNombreTablaMap(Connection con, String nombreTabla);
	
	/**
	 * Mï¿½todo que consulta el id de la ultima cuenta valida del paciente de su ingreso abierto o cerrado,
	 * 
	 * en el caso de que no haya cuenta abierta se devuelve cadena vacï¿½a
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerIdCuentaValidaIngresoAbiertoCerrado(Connection con,String codigoPaciente);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public Object obtenerViasIngreso(Connection con,boolean usarVector);

	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngreso(Connection con, HashMap campos);
	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngresoBloqueaDeudorBloqueaPaciente(Connection con, HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngresoTipoPaciente(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String obtenerViaIngresoCuenta(Connection con, String codigoCuenta);
	
	/**
	 * Mï¿½todo que verifica si un procedimiento tiene respuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean tieneProcedimientoRespuestaMultiple(Connection con,String numeroSolicitud);
	
	/**
	 * Mï¿½todo que verifica si un procedimiento ya tiene finalizada
	 * la repsuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean estaFinalizadaRespuestaMultiple(Connection con,String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public boolean esServicioRespuestaMultiple(Connection con, String codigoServicio);

	/**
	 * Metodo que consulta los numeros de solicitud de procedimiento que tienen el
	 * estado de historia clï¿½nica especificado en el parï¿½metro, para el paciente de acuedo a la cuenta
	 * @param con
	 * @param cuenta
	 * @param estadoHistoriaClinica
	 * @return
	 */
	public HashMap getSolProcedimientosEstadoHistoClinica(Connection con, int cuenta, int estadoHistoriaClinica);

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap obtenerResultadosRespuestasSolicitudesProcedimientos(Connection con, String numeroSolicitud);
	
	/**
	 * Mï¿½todo que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 * acronimo + ConstantesBD.separadorSplit + tipoCie 
	 * si no encuentra nada retorna vacï¿½o
	 */
	public String consultarDiagnosticosPaciente(Connection con,String idCuenta,int viaIngreso);

	/**
	 * 
	 */
	public int obtenerEstadoSolicitudTraslado(Connection con, String numeroSolicitudTraslado);
	
	/**
	 * Mï¿½todo que consulta los usuarios por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerUsuarios(Connection con,int institucion, boolean estado);

	
	/**
	 * Mï¿½todo que consulta los conceptos generales del modulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerConceptosGenerales (Connection con,int institucion, String Glosa);
	

	/**
	 * Mï¿½todo que consulta los conceptos glosa del modulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerConceptosGlosa(Connection con,int institucion);
	
	/**
	 * Mï¿½todo para obtener los conceptos de glosas parametrizados
	 * @param con
	 * @param tipo
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosGlosa(Connection con,String tipo,int institucion);
	
	
	/** Mï¿½todo que consulta los conceptos ajustes del modulo de glosas
	 * @param con
	 * @param institucion
	 * @return	 */
	public HashMap obtenerConceptosAjuste(Connection con, int institucion);
	
	
	/**
	 * Mï¿½todo que consulta los conceptos especificos del modulo de glosas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerConceptosEspecificos (Connection con,int institucion);

	/**
	 * 
	 * @param con
	 * @param login
	 * @return
	 */
	public String obtenerCentrosCostoUsuario(Connection con, String login);
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */	
	public  boolean esSolicitudProcedimientosFinalizada(Connection con, String numeroSolicitud);

	
	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @return
	 */
	public String obtenerDescripcionEstadoHC(Connection con, String codigoEstado);

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public HashMap consultarDetalleAjustesImpresion(Connection con, String codigoAjuste);

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public HashMap consultarEncabezadoAjuste(Connection con, String codigoAjuste);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList obtenerMedicosInstitucion(Connection con, String codigoInstitucion);
	
	
	/**
	 * Metodo para Extraer el numero de embarazos de la funcionalidad informacion del parto.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public String obtenerNrosEmbarazosInformacionParto(Connection con, int codigoPersona);

	/**
     * Metodo que consulta la informacion del paciente que se coloca en cada una
     * de las fichas de epidemiologï¿½a
     * @param con
     * @param codigoPaciente
     * @return
     */
	public ResultSetDecorator consultarDatosPacienteFicha(Connection con, int codigoPaciente);
	
	/**
	 * Mï¿½todo que consulta la fecha de admisionde la cuenta de una cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public String consultarFechaAdmisionCirugia(Connection con,String codigoCirugia);

	/**
	 * Metodo para obtener de la informacion del parto el numero del embarazo de acuerdo al codigo
	 * de la cirugï¿½a y al paciente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCirugia
	 * @return -1 si no existe de lo contrario retorna el nro del embarazo
	 */
	public int obtenerNroEmbarazoCirugiaPaciente(Connection con, int codigoPaciente, String codigoCirugia);
	
	
	/**
	 * Mï¿½todo que consulta los tipos de Identificacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public  HashMap consultarTiposidentificacion(Connection con,int institucion);
	
	/**
	 * M?todo que consulta las Ciudades 
	 * @param con
	 * @return
	 */
	public  HashMap consultarCiudades(Connection con);
	
	
	/**
	 * M?todo que consulta las Tipos de Sangre 
	 * @param con
	 * @return
	 */
		
	public  HashMap consultarTiposSangre(Connection con);

	/**
	 * M?todo que consulta las Estados Civiles 
	 * @param con
	 * @return
	 */
	
	public  HashMap consultarEstadosCiviles(Connection con);

	/**
	 * M?todo que consulta las Zonas Domiciliarias 
	 * @param con
	 * @return
	 */
	
	public  HashMap consultarZonasDomicilio(Connection con);

	/**
	 * M?todo que consulta las Ocupaciones 
	 * @param con
	 * @return
	 */
	
	public  HashMap consultarOcupaciones(Connection con);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerNumeroMedicamentosSolicitud(Connection con, String numeroSolicitud);	
	
	
	/**
	 * Mï¿½todo para saber si el tipo y nï¿½mero de identificaciï¿½n del paciente
	 * existe en usuario capitados, si existe retorna el codigo sino retorna -1
	 * @param con
	 * @param tipoIdentificacion
	 * @param nroIdentificacion
	 * @return
	 */
	public int getCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String nroIdentificacion);

	/**
	 * Metodo que obtiene la informaciï¿½n del convenio capitado, de acuerdo
	 * al codigo del usuario capitado enviado por parï¿½metro
	 * @param con
	 * @param codigoUsuarioCapitado
	 */
	public HashMap convenioUsuarioCapitado(Connection con, int codigoUsuarioCapitado);
	
	/**
	 * Mï¿½todo implementado para consultar tipo id, numero id y nombre de las personas
	 * que tienen el mismo numero id
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 */
	public HashMap personasConMismoNumeroId(Connection con,String numeroIdentificacion, String tipoIdentificacion);
	
	/**
	 * Mï¿½todo que marca como atendido los registros de pacientes para triage
	 * del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int actualizarPacienteParaTriageVencido(Connection con,String codigoPaciente);
	
	/**
	 * Mï¿½todo que condulta el codigo UPGD del centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public String getCodigoUPGDCentroAtencion(Connection con,int centroAtencion);
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public String getLoginUsuarioSolicitaOrdenAmbulatoria(Connection con,String consecutivoOrden, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public HashMap consultarInformacionServicio(Connection con, String codigoServicio, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoEnfermedad
	 * @param institucion
	 * @return
	 */
	public ArrayList consultarServiciosEnfermedadEpidemiologia(Connection con, String codigoEnfermedad, String institucion);

	/**
	 * 
	 * @param con
	 * @param sexo
	 * @return
	 */
	public String getDescripcionSexo(Connection con, int sexo);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int obtenerNumeroSolicitudesPypXCuenta(Connection con, String cuenta,String busquedaPYP);
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param codigoTipoCaja
	 * @param codigoCentroAtencion
	 * @return
	 */
	public HashMap obtenerCajasCajero(Connection con, String loginUsuario, int codigoInstitucion, int codigoTipoCaja, int codigoCentroAtencion);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerCajas(Connection con, int codigoInstitucion, int codigoTipoCaja);
	
	/**
	 * 
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public HashMap consultarDatosGeneralesPaciente(Connection con, String numeroDocumento);
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param fecha
	 * @param institucion
	 * @return
	 */
	public InfoDatosString obtenerTipoRegimenSolicitudUsuarioCapitado(Connection con, String codigoPersona, String fecha, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap obtenerCentrosAtencion(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param inactivo
	 * @return
	 */
	public HashMap obtenerCentrosAtencionInactivos(Connection con, int codigoInstitucion, boolean inactivo);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @param anioVigencia 
	 * @return
	 */
	public HashMap obtenerCuentasContables(Connection con, int codigoInstitucion, String cuentaContable, String descripcion, String anioVigencia, String naturCuenta);

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public String obtenerNombreArticulo(Connection con, int codigoArticulo);

	/**
	 * 
	 * @param con
	 * @param unidosis
	 * @return
	 */
	public String obtenerUnidadMedidadUnidosisArticulo(Connection con, String unidosis);

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarTerceros(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public int obtenerTipoCaja(Connection con, int consecutivoCaja);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esFormaFarmaceuticaUsada(Connection con, String consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esClaseInventarioUsada(Connection con, int consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param clase
	 * @return
	 */
	public  boolean esGrupoInventarioUsada(Connection con, int consecutivo, int clase);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param subgrupo
	 * @param grupo
	 * @param clase
	 * @return
	 */
	public boolean esSubgrupoInventarioUsada(Connection con, int consecutivo, int subgrupo , int grupo, int clase);

	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	public ArrayList obtenerTercerosInstitucionActivos(Connection con, int institucion);
	
	
	
	/**
	 * Metodo encargado de consultar los terceros que se encuentren activos
	 * y no se encuentren relacionados a alguna empresa.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return
	 * ArrayList<HashMap<String, Object>>
	 * -----------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * -----------------------------------
	 * codigo,numeroid,descripcion
	 */
	public ArrayList obtenerTercerosSinEmpresaInstitucionActivos(Connection con, int institucion);
	
	
	/**
	 * Mï¿½todo que consulta las unidades funcionales por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarUnidadesFuncionales(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codEstadoCuenta
	 * @return
	 */
	public int obtenerCuentaDadoPacienteEstado(Connection con, int codPaciente, int codEstadoCuenta);
	
	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerTipoRegimenConvenio(Connection con,String codigoConvenio);
	
	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCodigoTipoRegimenConvenio(Connection con,String codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param todos
	 * @param validarNumeroContratos: verifica que el numero del contrato no sea NULL ni cadena vacï¿½a
	 * @return
	 */
	public ArrayList obtenerContratos(Connection con, int codigoConvenio, boolean todos,boolean validarNumeroContratos, boolean pendientesFinalizar);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String,Object> obtenerNaturalezasEventosCatastroficos(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return
	 */
	public HashMap<String,Object> obtenerInformacionReferenciaTramite(Connection con, int codigoPaciente, int idIngreso);

	/**
	 * 
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public Vector<InfoDatosString> obtenerNaturalezasPaciente(Connection con, String tipoRegimen, int convenio, int viaIngreso);
	
	/**
	 * 
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public Vector<InfoDatosString> obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
			Connection con, String tipoRegimen, int convenio, int viaIngreso,
			String codigoTipoAfiliado, int codigoEstratoSocial, String fechaReferencia);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String getNombreEstadoCartera(Connection con, int codigo);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param tiposArea
	 * @param todos
	 * @param filtro_externos
	 * @return
	 */
	public HashMap obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos);

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerEmpresasInstitucion(Connection con, int institucion);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public Vector<InfoDatosString> obtenerTiposComplejidad(Connection con);

	/**
	 * 
	 * @param con
	 * @param identificadorTabla
	 * @return
	 */
	public String consultarNombreTablaInterfaz(Connection con, String identificadorTabla,int institucion);

	/**
	 * 
	 * @param con
	 * @param abono
	 * @return
	 */
	public boolean insertarAbono(Connection con, DtoInterfazAbonos abono);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public Vector<String> obetenerCodigosInstituciones(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public InfoDatosString obtenerInstitucionSirCentroAtencion(Connection con, int codigoCentroAtencion);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap obtenerInstituciones(Connection con);
	
	
	
	
	/**
	 * Mï¿½todo implementado parta obtener los paises
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPaises(Connection con);
	
	/**
	 * Mï¿½todo que consulta las localidades 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerLocalidades(Connection con,HashMap campos);
	
	/**
	 * Mï¿½todo que consulta la localidad de un barrio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerLocalidadDeBarrio(Connection con,HashMap campos);
	
	
	/**
	 * Mï¿½todo que consulta los sexos parametrizados en el sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerSexos(Connection con);

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public int getCodigoResponsable(Connection con, int idIngreso, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public int obtenerPrioridadResponsabe(Connection con, String subCuenta) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean convenioManejaPyp(Connection con, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public InfoDatosInt obtenerTipoComplejidadCuenta(Connection con, int cuenta) throws BDException;

	
	/**
	 * Adiciï¿½n Jhony Alexander Duque
	 * Mï¿½todo usado para obtener los grupos de servicios y con la posibilidad de ser filtrado
	 * con cualquiera de los campos de la tabla
	 * @param Connection connection
	 * @param HashMap grupoServicios
	 * @return ArrayList<HashMap> 
	 */
	public ArrayList<HashMap<String, Object>> obtenerGrupoServicios (Connection connection, HashMap grupoServicios);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerViasIngresoCuenta(Connection con, int cuenta);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public String obtenerPorcentajeAutorizadoVerficacionDerechos(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public HashMap obtenerCoberturaAccidenteTransitos(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String obtenerFechaAccidenteTransito(Connection con, int codigoIngreso);

	/**
	 * 
	 * @param con
	 * @param fecha
	 * @return
	 */
	public double obtenerSalarioMinimoVigente(Connection con, String fecha);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatosString obtenerEstadoHC(Connection con, String numeroSolicitud)throws SQLException,Exception;

	/**
	 * 
	 * @param con
	 * @param codigoMonto
	 * @return
	 */
	public ResultadoBoolean esPorcentajeMonto(Connection con, int codigoMonto)throws Exception;

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public double obtenerTotalCargadoResponsable(Connection con, String subCuenta);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerNombreTipoComplejidad (Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param subcuenta
	 * @return
	 */
	public String obtenerNumeroCarnet(Connection con, double subcuenta);
	
	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public String obtenerPathFuncionalidad(Connection con, int codigoFuncionalidad);

	/**
	 * 
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap obtenerEsquemasTarifarios(Connection con, boolean esInventarios, int codigoInstitucion); 

	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosInArray(Connection con, boolean esInventarios, int codigoInstitucion,int tarifarioOficial); 
	
	
	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return
	 */
	public HashMap obtenerDiasSemana(Connection con);
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de liquidacion
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public String getNombreEstadoLiquidacion(Connection con,String acronimo);
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de la cita
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String getNombreEstadoCita(Connection con,int codigo);
	
	/**
	 * Mï¿½todo que consulta las finalidades de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerFinalidadesServicio(Connection con,HashMap campos);
	
	/**
	 * Metodo que trae el nombre del esquema tarifario segun el codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String getNombreEsquemaTarifario(Connection con, int codigo);
	
	/**
	 * Metodo que retorna el tarifario oficial del esquema tarifario segun el codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int getTarifarioOficial(Connection con, int codigo);	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerSexoServicio(Connection con, String codigoServicio);	
	
	
	/**
	 * Metodo encargado de consultar todos los servicios,
	 * pudiendolos filtrar por diferentes tipos
	 * de servicio y por el estado activo:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --tiposServicio --> ejemplo, este es el valor de esta variable  'R','Q','D'
	 * --activo --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerServicos(Connection connection,String tiposServicio, String activo);
	
	
	/**
	 * Metodo encargado de consultar todos los tipos de cirugia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTipoCirugia(Connection connection,String acronimo);
	

	/**
	 * Metodo encargado de consultar todos los tipos de anestecia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTipoAnestecia(Connection connection,String acronimos);
	
	
	/**
	 * Metodo encargado de consultar todos los asocios,
	 * pudiendolos filtrar por el codigo_asocio,
	 * tipo de servicio y si participa por cirugia.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --codAsocio --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 * --tipoServ --> ejemplo, este es el valor de esta variable  'Q','R','D'
	 * --participa --> es S o N
	 * @param connection
	 * @param codAsocio
	 * @param tipoServ
	 * @param participa
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerAsocios(Connection connection,String codAsocio,String tipoServ,String participa);
	
	
	/** 
	 * Mï¿½todo implementado parta obtener Tipos de Sala
	 * si se envian vacios esQuirurjica y esUrgencias no se toman en cuenta
	 * @param con
	 * @param int institucion
	 * @param String esQuirurjica (t,f) 
	 * @param String esUrgencias (t,f)
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposSala(Connection con,int institucion,String esQuirurjica,String esUrgencias);
	
	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios Generales y Particulares
	 * adicionalmente se carga el codigo del encabezado asociado a la parametrica (porcentajescx,asociosserv)
	 * @param con
	 * @param codigoInstitucion
	 * @param String parametrica
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosGenPartInArray(Connection con,int codigoInstitucion,String parametrica);
	
	/**
	 * Metodo que retorna los esquemas tarifarios ISS
	 * @param con
	 * @return
	 */
	public ArrayList obtenerEsquemastarifariosIss(Connection con);
	
	/**
	 * Metodo que retorna todos los tipos de asocio
	 * @param con
	 * @return
	 */
	public ArrayList obtenerTiposAsocio(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param mostrarEnHqx
	 * @return
	 */
	public ArrayList obtenerTiposAnestesia(Connection con,String mostrarEnHqx);
	
	/**
	 * Metodo que carga un array list con las ocupaciones medicas 
	 * @param con
	 * @return
	 */
	public ArrayList obtenerOcupacionesMedicas(Connection con);
	
	/**
	 * Metodo que retorna un array list con las especialidades
	 * @param con
	 * @return
	 */
	public ArrayList obtenerEspecialidadesEnArray(Connection con,HashMap campos);
	
	/**
	 * Metodo que retorna un array list con los tarifarios oficiales filtrando por la columna tarifarios si se quiere se le envia un string con 'S' , 'N' o vacio, ejemplo sin filtro-->>Utilidades.obtenerTarifariosOficiales(con,""); ejemplo con filtro-->>Utilidades.obtenerTarifariosOficiales(con,"S");
	 * @param con
	 * @return
	 */
	public ArrayList obtenerTarifariosOficiales(Connection con,String tarifarios);
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> obtenerTransaccionesCentroCosto(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param almacen
	 * @param grupoInventario 
	 * @param claseInventario 
	 * @param existenciasPositivas
	 * @return
	 */
	public HashMap<String, Object> obtenerArticulosCantidadPosNeg(Connection con, String almacen, String claseInventario, String grupoInventario, boolean existenciasPositivas);

	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param almacen
	 * @param transaccionEntrada
	 * @param transaccionSalida
	 * @param observaciones
	 * @return
	 */
	public boolean generarLogTransSaldosInventario(Connection con, String loginUsuario, String almacen, String transaccionEntrada, String transaccionSalida, String observaciones);

	/**
	 * 
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarFacturasMismoConsecutivo(Connection con, int factura, int codigoInstitucionInt);

	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public String obtenerRazonSocialEmpresaInstitucion(Connection con, String empresaInstitucion);

	
	
	
	/**
     * @param con
     * @param Acronimo Tipo Identificacion
     * @return
     */
    public String getDescripcionTipoIdentificacion(Connection con, String acronimoTipoIdentificacion);
    
     
    /**
	 * Consulta las empresas que pertenecen a la institucion
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String, Object>> obtenerEmpresasXInstitucion(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param numDiasControl
	 * @param articulos
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarArticulosSolicitadosUltimosXDias(Connection con, int numDiasControl, HashMap articulos, int codigoPersona);


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param loginUsuario
	 * @param articulosConfirmacion
	 * @return
	 */
	public boolean generarLogConfirmacionOrdenAmbSolMed(Connection con, String codigoOrden, String loginUsuario,String esSolOrdenConfirmada, HashMap<String, Object> articulosConfirmacion);
	
	
	/**
	 * Obtener centros Costos usuario
	 * @param Connection con
	 * @param String usuarioLogin
	 * @param int Institucion 
	 * @param String tipoArea
	 * @param String codigoViaIngreso
	 * @return
	 */
	public  HashMap obtenerCentrosCostoUsuario(Connection con,String usuarioLogin, int institucion, String tipoArea, String codigoViaIngreso);
	
	/**
	 * Obtener Especialidades Medicos
	 * @param Connection con
	 * @param String codigoMedico 
	 * @return
	 */
	public HashMap obtenerEspecialidadesMedico(Connection con,String codigoMedico); 
	
	/**
	 * 
	 * @param con
	 * @param concepto
	 * @param institucion
	 * @return
	 */
	public int obtenerTipoConceptoTesoreria(Connection con, String concepto, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param ingreso 
	 * @return
	 */
	public double obtenerAbonosDisponiblesPaciente(Connection con, int codigoPaciente, int ingreso);

	/**
	 * 
	 * @param con
	 * @param reciboCaja
	 * @param institucion
	 * @return
	 */
	public boolean reciboCajaConPagosGeneralEmpresa(Connection con, String reciboCaja, int institucion);

	/**
	 * Mï¿½todo implementado para disminuir el acumulado de PYP por solicitud 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @param secuencia
	 * @param fechaSalPac --> dd/mm/yyyy
	 * @return
	 */
	public int disminuirAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion, String fechaSalPac);

	/**
	 * 
	 * @param con
	 * @param servart
	 * @param servart2 
	 * @param esServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public int obtenerEsquemasTarifarioServicioArticulo(Connection con,String subCuenta,int contrato, int servart, boolean esServicio, String fechaCalculoVigencia, int centroAtencion) throws BDException;
	
	/**
	 * Metodo que dada una fechaEvaluar/horaEvaluar, evalua que este entre la fechaInicial - HoraInicial y FechaFinal - Hora Final,
	 * NOTA LAS FECHAS DEBEN ESTAR EN FORMATO DD/MM/AAAA 
	 * @return
	 */
	public boolean betweenFechas(String fechaEvaluar, String horaEvaluar, String fechaInicial, String horaInicial, String fechaFinal, String horaFinal);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param esServicios
	 * @return
	 */
	public boolean requiererJustificacionServiciosArticulos(Connection con, int codigoIngreso, boolean esServicios);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public int obtenerCodigoContratoSubcuenta(Connection con, double subCuenta); 
	
	/**
	 * Metodo encargado de  obtener los tipos de convenio
	 * en un arrayList de Hashmap
	 * @param con
	 * @return
	 */
	public ArrayList obtenerTiposConvenio(Connection con, String institucion);

	/**
	 * Metodo que consulta los motivos de apertura o de cierre
	 * @param con
	 * @param tipo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMotivosAperturaCierre(Connection con, String tipo);
	
	/**
	 * Metodo consulta Ultimo egreso segun via de ingreso para evolucion
	 * @param con
	 * @param codigoPaciente
	 * @param viaIngreso
	 * @return
	 */
	public HashMap consultaUltimoEgresoEvolucion(Connection con, int codigoPaciente, int viaIngreso);
	
	/**
	 * Metodo que consulta los usuarios de apertura o de cierre de ingresos
	 * @param con
	 * @param tipo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerUsuarioAperturaCierre(Connection con, String tipo);
	
	/**
	 * Metodo para marcar un Reingreso
	 * @param con
	 * @param codIngreso
	 * @param codPaciente
	 * @return
	 */
	public boolean marcarReingreso(Connection con, int codIngreso, int codPaciente);
	
	/**
	 * Consulta la informacion de la Unidades de Campo
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public ArrayList consultarUnidadesCampoParam(Connection con, HashMap parametros);	
	
	/**
	 * Consulta la informaciï¿½n de Tipo Campos
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public ArrayList consultarTiposCamp(Connection con, HashMap parametros);
	
	/**
	 * Metodo consulta Centro Costo por via de Ingreso mas filtros
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public HashMap consultaCentrosCostoFiltros(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public ArrayList consultarEscalasParam(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public ArrayList consultarComponentesParam(Connection con, String funParam, HashMap parametros);
	
	/**
	 * Metodo consulta Tipo de Monitoreo por Centro de Costo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public HashMap consultaTipoMonitoreoxCC(Connection con, int centroCosto);
	
	/**
	 * Metodo inserta nuevo registro Ingresos por Cuidados Epseciales
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @param indicativo
	 * @param tipoMonitoreo
	 * @param usuario
	 * @return
	 */
	public boolean insertarIngresoCuidadosEspeciales(Connection con, int ingreso, String estado, String indicativo, int tipoMonitoreo, String usuario, int evolucion, int valoracion, String centroCosto);
	
	/**
	 * Metodo actualiza el estado a Finalizado de un Ingreso de Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @param usuario 
	 * @return
	 */
	public boolean actualizarEstadoCuidadosEspeciales(Connection con, HashMap campos);
	
	/**
	 * Metodo verificar Ingreso Cuidados Especiales y requiere valoracion
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int verificarValoracionIngresoCuidadosEspeciales(Connection con, int ingreso);
	
	/**
	 * Metodo consulta de Tipo de Monitoreo por Ingreso de Cuidados Especiales
	 * @param ingreso
	 * @return
	 */
	public String consultaTipoMonitoreoxCE(int ingreso);
	
	
	/**
	 * Metodo encargado de consultar los centros de costo
	 * y devolverlos en un ArrayList de HashMap con los keys
	 * codigo,nombre.
	 * @author Jhony Alexander Duque A.
	 * Se puede filtrar por los siguientes criterios:
	 * -- viaIngreso --> Opcional 
	 * -- centroAtencion --> Opcional
	 * -- institucion --> Requerido
	 * -- tipoPaciente --> Opcional
	 * -- tipoMonitoreo --> Opcional
	 * -- filtroTipoMonitoreo --> Requerido
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @param tipoPaciente
	 * @param tipoMonitoreo
	 * @param filtroTipoMonitoreo
	 * @param consultarViaIngreso 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto(Connection con,String viaIngreso,int centroAtencion,int institucion, String tipoPaciente, int tipoMonitoreo, boolean filtroTipoMonitoreo,boolean noTipoMonitoreo, boolean consultarViaIngreso);
	
	/**
	 * Metodo encargado de devolver los tipo de paciente
	 * segun la via de ingreso en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerTiposPacientePorViaIngreso(Connection con, String viaIngreso);
	
	/**
	 * Metodo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerMotivosDevolucionInventarios(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public int obtenerGrupoServicio(Connection con, int servicio);

	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @return
	 */
	public int obtenertipoTarifarioEsquema(Connection con, int esquemaTarifario);
	
	
	/**
	 * Consulta cuantas solicitudes se encuentran o no en los estado medicos dados a partir de una cuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int consultarCuantosSolicitudesEstadoMedico(Connection con, HashMap parametros);

	/**
	 * Metodo implementado para traer las
	 * naturalezas de articulo filtradas por
	 * la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerNaturalezasArticulo(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param acronimo
	 * @param Institucion
	 * @return
	 */
	public String esMedicamento(Connection con, String acronimo, int Institucion);
	
	
	/**
	 * Metodo implementado para traer el
	 * nombre de la naturaleza
	 * @param con
	 * @param acronimoNaturaleza
	 * @return
	 */
	public String obtenerNombreNaturalezasArticulo(Connection con, String acronimoNaturaleza);
	
	
	/**
	 * Metodo encargado de consultar el nombre del estado de la cama
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public String obtenerNombreEstadoCama (Connection con, String codigoEstadoCama);
	
	/**
	 * Metodo encargado de consultar el nombre del piso
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public String obtenerNombrePiso(Connection con, String codigoPiso);
	
	/**
	 * Metodo encargado de consultar la cantidad de camas
	 * de una institucion
	 * @param con
	 * @param centroAtencion 
	 * @return
	 */
	public int obtenerNumeroCamas(Connection con,String institucion, String centroAtencion);

	/**
	 * Metodo encargado de consultar el codigo del centro
	 * de costo principal segun el centro de costo solicitado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public String obtenerCentroCostoPrincipal(Connection con, String centroCosto, String institucion);
	
	/**
	 * Metodo de consulta los totales de un factura para su impresion
	 * @param con
	 * @param centroCosto
	 * @return
	 */	
	public HashMap consultaTotalesFactura(Connection con, int codFactura);
	
	
	/**
	 * Metodo encargado de verificar si existe evolucion con 
	 * orden de salida por Admision de Urgencias
	 * @param con
	 * @param codigoAdminUrg
	 * @param anioAdminUrg
	 * @return
	 */
	public boolean  tieneEvolucionConSalidaXAdminUrg  (Connection con, String codigoAdminUrg, String anioAdminUrg);
	
	
	/**
	 * Metodo encargado de identificar si un ingreso es reingreso;
	 * si es reingreso devuelve el ingreso asociado, si no devuelve -1
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public int esIngresoReingreso (Connection connection, String ingreso);
	
	/**
	 * Metodo encargado de consultar la descripcion del estado 
	 * de aplicacion de pagos segun el codigo del estado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public String obtenerNombreEstadoAplicacionPagos(Connection con, int codigoEstado);
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public String obtenerNombreComponente(Connection con, String tipoComponente);
	
	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @return
	 */
	public String obtenerNombrePlantilla(Connection con, String plantilla);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerTiposPacienteCuenta(Connection con, int cuenta);
	
	/**
	 * Consulta los Centros de Costo para Area de Asocio de Cuentas segun Ingreso Cuidados Especiales
	 * @param con
	 * @param codigoCentroAtencion
	 * @param cuenta
	 * @param egresoV
	 * @return
	 */
	public HashMap areasAsocioEspeciales(Connection con, int centroAtencion, int cuenta, boolean egresoV);
	
	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public String obtenerDescripcionComponente(Connection con, String codigoComponente);
	
	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @return
	 */
	public String obtenerDescripcionEscala(Connection con, String codigoEscala);
	
	/**
	 * Metodo que consulta la Evolucio de una cuenta de urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int consultaEvolucionCuentaUrgencias(Connection con, int cuenta);
	
	/**
	 * Metodo que consulta la Valoracion de una cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int consultaValoracionCuentaUrgencias(Connection con, int cuenta);

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerConceptosPagos(Connection con, int institucion);

	/**
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int obtenerSubCuentaIngreso(Connection con, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public String obtenerNombreTipoMonitoreo(Connection con, String tipoMonitoreo);
	
	/**
	 * Mï¿½todo implementado para obtener el codigo del tralado de cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public int getCodigoTrasladoUltimoTraslado(Connection con,int cuenta);
	
	/**
	 * Metodo encargado de Actualizar la fecha y hora de finalizacion del 
	 * traslado de la cama
	 * @param connection
	 * @param codigoTrasladoCama
	 * @return
	 */
	public boolean actualizarFechaHoraActualizacion (Connection connection, HashMap datos);
	
	/**
	 * Metodo que consulta si un Ingreso es un Preingreso sin importar estado
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean esPreingresoNormal(Connection con, int ingreso);

	/**
	 * Metodo que consulta la descripcion de un tipo de convenio especifico
	 * @param con
	 * @param tipoConvenio
	 * @param codigoInstitucion
	 * @return
	 */
	public String obtenerDescripcionTipoConvenio(Connection con, String tipoConvenio, int codigoInstitucion);
	
	
	/**
	 * Metodo encargado de consultar los tipos de tratamiento
	 * odontologico
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- activo --> Opcional (S --> filtra los activos, N --> filtra inactivos, "" --> no filtra)
	 * -- codigo --> Opcional
	 * -- institucion --> Requerido
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposTratamientoOdontologico (Connection connection,HashMap criterios);
	
	/**
	 * Metodo encargado de obtener los tipos de Habitacion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposHabitacion (Connection connection,String institucion);

	/**
	 * Metodo para obtener el nombre del tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public String obtenerNombreTipoPaciente(Connection con, String tipoPaciente);

	/**
	 * Metodo encargado de obtener los grupos parametrizados en la funcionalidad transacciones validas por centro de costo concatenados por comas
	 * @param connection
	 * @param institucion
	 * @param centro_costo
	 * @param tipos_trans_inventario
	 * @param clase_inventario
	 * @return
	 */
	public String obtenerGruposTransValidasXCC (Connection connection,int institucion,int centro_costo,int tipos_trans_inventario, int clase_inventario);

	
	/**
	 * Metodo encargado de obtener los deudores
	 * @param con
	 * @param tipoDeudor
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerDeudores(Connection con, String tipoDeudor);

	/**
	 * Mï¿½todo que indica si una Entidad Financiera se encuentra
	 * inactiva ï¿½ activa, segï¿½n el consecutivo de la entidad
	 * financiera
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public boolean obtenerActivoInactivoEntidadFinanciera(Connection con, int consecutivoEntidad);
	
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en le sistema, con un deudor
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con, String institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEmpresa
	 * @return
	 */
	public String obtenerNombreEmpresa(Connection con, String codigoEmpresa);
	
	
	/**
	 * Metodo Que retorna el numero de la cuenta contable para un codigo de cuenta
	 * @author Andres Silva Monsalve
	 * @param cuentaConvenio
	 * @return
	 */
	public String obtenerCuentaContable(Connection con, String cuentaConvenio);

	/**
	 * Mï¿½todo que retorna el codigo de procedimiento principal
	 * asociado al servicio principal antes ingresado
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerCodigoProcedimientoPrincipalIncluidos(Connection con, String codigoServicio);
	
	
/** Mï¿½todo que retorna el codigo de procedimiento incluido asociado al servicio incluido antes ingresado
 * @param con, codigoServiPpal, codigoServiInclu
 * @return */
	public int obtenerCodigoServicioIncluido(Connection con, String codigoServiPpal, String codigoServiInclu);
	
	
	/**
	 * Metodo encargado de Consultar los subgrupos
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * -- codigo
	 * -- subgrupo
	 * -- grupo
	 * -- clase
	 * -- cuentaInventario
	 * -- cuentaCosto
	 * @return ARRAYLIST <HASHMAP< > >
	 * ---------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * codigo,subgrupo,grupo,clase,nombre,
	 * cuentaInventario,cuentaCosto
	 */
	public ArrayList<HashMap<String, Object>> obtenerSubGrupo(Connection con, HashMap criterios);
	
	/**
	 * Mï¿½todo encargado de averiguar si hay una descripciï¿½n de textos en la BD
	 * @autor Ing. Felipe Pï¿½rez G.
	 * @param con
	 * @param descripcionTexto
	 */
	
	public boolean existeDescripcionTextoProcedimiento (Connection con, String descripcionTexto, String servicio);

	
	/**
	 * Metodo para Traer el codigo del Convenio a partir de un codigo Interfaz
	 * @author Andres Silva Monsalve
	 * @param codigoInterfaz
	 * @return
	 */
	public int obtenerCodigoConvenioDeCodInterfaz(Connection con, String codigoInterfaz);

	/**
	 * Metodo para Traer el codigo interfaz del Convenio a partir de un codigo del convenio
	 * @author Andres Silva Monsalve
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCodigoInterfazConvenioDeCodigo(Connection con, int codigoConvenio);

	/**
	 * Mï¿½todo que devuelve la fecha del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerFechaIngreso(Connection con, String idIngreso);
	
	/**
	 * Mï¿½todo que devuelve la hora del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerHoraIngreso(Connection con, String idIngreso);
	
	/**
	 * Mï¿½todo que devuelve el consecutivo de ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerConsecutivoIngreso(Connection con, String idIngreso);

	/**
	 * Mï¿½todo que devuelve el nombre del tipo
	 * de solicitud segï¿½n un tipo de solicitud
	 * dado
	 * @param con
	 * @param tipoSolicitud
	 * @return
	 */
	public String obtenerNombreTipoSolicitud(Connection con, String tipoSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param tiposArea
	 * @param todos
	 * @param filtro_externos
	 * @return
	 */
	public HashMap obtenerCentrosCostoTipoConsignacion(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos);
	
	/**
	 * 
	 * @param con
	 * @param codProveedor
	 * @return
	 */
	public HashMap obtenerProveedores(Connection con, int codProveedor);

	/**
	 * Consultar el Centro de Costo Interfaz a partir del Codigo del Centro de Costo. 
	 * Tener en cuenta para los centros de costo SubAlmacen enviar el codigoInterfaz del Centro de Costo Interfaz
	 * Octubre 20 de 2008
	 * @author Andres Silva M
	 * @param codigoCentroCosto
	 * @return
	 */
	public String obtenerCentroCostoInterfaz(Connection con, int codigoCentroCosto);
	
	/**
	 * Consulta el NIT del Convenio por medio del codigo interfaz del mismo
	 * @author Andres Silva M --Oct 21 / 08
	 * @param con
	 * @param codigoInterfazConvenio
	 * @return
	 */
	public String obtenerNitConveniodeCodInterfaz(Connection con, String codigoInterfazConvenio);

	/**
	 * Metodo encargado de obtener los tipos de servicio
	 * de vehiculos
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param codigo --> Opcional
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposServiciosVehiculos (Connection connection,String codigo);

	/**
	 * Mï¿½todo encargado de traer los tipos de regimen
	 * segï¿½n el filtro
	 * @author Carlos Mauricio Jaramillo H.
	 * @param con
	 * @param todos
	 * @param codigoFiltros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposRegimen(Connection con, boolean todos, String codigoFiltros);

	/**
	 * Consulta el NIT del convenio por medio del codigo de la factura
	 * @author Andres Silva M --Oct 24 /08
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerNitConveniodeCodfactura(Connection con, int codigoFactura);

	/**
	 * Consulta el codigo Interfaz del tipo de Identificacion
	 * @author Andres Silva Monsalve --Oct 25/08
	 * @param codigoTipoIdentificacionPersona
	 * @return
	 */	
	public String codigoInterfaztipoIdentificacion(Connection con, String codigoTipoIdentificacionPersona);

	/**
	 * Consulta para saber si el paciente tiene informacion en la historia del sistema anterior
	 * @author Andres Silva M
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public boolean existeHistoriaSistemaAnterior(Connection con, String tipoIdentificacion, String numeroIdentificacion);
	
	/**
	 * Metodo encargado de consultar las instituciones Sirc
	 * pudiendo filtrar por diferentes criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * --institucion --> Requerido
	 * --nivelServicio --> Opcional
	 * --tipoRed --> Opcional
	 * --tipoInstReferencia --> Opcional
	 * --tipoInstAmbulancia --> Opcional
	 * --activo --> Opcional
	 * @param connection
	 * @param criterios
	 * @return ArrayList<HashMap>
	 * --------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * --------------------------------------
	 * codigo,descripcion,tipoRed,
	 * tipoInstReferencia,tipoInstAmbulancia,
	 * nivelServicio, activo
	 */
	public ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection connection,HashMap criterios);
	
	/**
	 * * Consulta codigo Interfaz del centro de costo subalmacen en Parametros Almacen
	 * @author Andres Silva M Oct28/08
	 * @param con
	 * @param codigoAlmacen
	 * @return
	 */
	public String obtenerCodigoInterfazParametroAlmacen(Connection con, int codigoAlmacen);

	/**
	 * Consulta el Codigo Interfaz para un Articulo
	 * @author Andres Silva M Oct30/08
	 * @param codigoArticulo
	 * @return
	 */
	public String obtenerCodigoInterfazArticulo(Connection con,	int codigoArticulo);
	
	/**
	 * Consulta el Codigo del Articulo a partir del codigo interfaz del mismo
	 * @author Andres Silva M Oct30/08
	 * @param codigoInterfazArticulo
	 * @return
	 */
	public int obtenerCodigoArticulodeCodInterfaz(Connection con, String codigoInterfazArticulo);

	/**
	 * Consulta el codigo del centro de costo asociado al interfaz del almacen en la tabla parametros almacen
	 * @author Andres Silva Monsalve Oct30/08
	 * @param codigoInterfazAlmacen
	 * @return
	 */
	public int obtenerAlmacenDeCodigoInterfaz(Connection con, String codigoInterfazAlmacen);

	/**
	 * Consultar Consecutivo de la Transaccion Inventarios a Partir del Codigo
	 * @author Andres Silva M Oct30/08
	 * @param codigoTrans
	 * @return
	 */
	public int obtenerConsecutivoDeTransaccionInv(Connection con, String codigoTrans);
	
	/**
	 * Metodo encargado de identificar si un ingreso tiene facturas
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public int esIngresoFacturado(Connection connection, String ingreso);
	
	/**
	 * Consulta el Codigo del Tercero A partir del Numero de Identificacion del Mismo
	 * @author Andres Silva Monsalve Oct31/08
	 * @param numeroIdTercero
	 * @return
	 */
	public int obtenercodigoTercerodeNumeroIdentificacion(Connection con, String numeroIdTercero);
	
	/**
	 * Pregunta Si el login del usuario Existe en Axioma
	 * @author Andres Silva M Nov06/08
	 * @param loginUsuario
	 * @return
	 */
	public boolean existeUsuario(Connection con, String loginUsuario);

	/**
	 * @param con
	 * @param consecutivo
	 * @param todos
	 * @return
	 */
	public HashMap<String, Object> consultarTransaccionesInventarios(Connection con, String consecutivo, boolean todos, boolean validarCodigos, String codigosValidados);
	
	/**
	 * Metodo encargado de obtener el tipo de sala standar
	 * que esta en el grupo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerTipoSalaStandar(Connection con, String codigoServicio );

	/**
	 * Metodo para Consultar el Consecutivo del Ingreso de un paciente y el Codigo Persona a partir de un Numero de Pedido QX
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public HashMap consultarIngresoYpersonadeunPedidoQx(Connection con,	int numeroPedido); 
	
	/**
	 * Metodo encargado de consultar el nombre de la 
	 * salida del paciente.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreSalidaPaciente(Connection con, String codigo ); 
	
	/**
	 * Metodo encargado de consultar el codigo de un servicio 
	 * dependiendo del tarifario y del codigo.
	 * @param con
	 * @param codigo
	 * @param tarifario
	 * @return
	 */
	public int obtenerCodigoServicio(Connection con, String codigo,String tarifario ); 
	
	
	/**
	 * Mï¿½todo que devuelve la sumatoria del valor total cargado
	 * de los N paquetes que tiene uns subcuenta
	 * @param con
	 * @param subCuenta
	 */
	public double obtenerValorTotalPaquetesResponsable(Connection con, String subCuenta) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param codigoTercero
	 * @return
	 */
	public String obtenerNitProveedor(Connection con, String codigoTercero);

	/**
	 * Verificar si Abono generado por otro sistema existe en Axioma
	 * @param con
	 * @param reciboCaja
	 * @param tipoMovimiento
	 * @return
	 */
	public boolean existeRegistroAbonosRC(Connection con, String reciboCaja,String tipoMovimiento);

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String asignarValorPacienteValorAbonos(Connection con, String codigoConvenio);

	/**
	 * Metodo que consulta todos los Rublos Presupuestales
	 * @param con
	 * @param anioVigenciaRublo
	 * @param codigoRublo
	 * @param descripcionRublo
	 * @param institucion 
	 * @return
	 */
	public HashMap obtenerRublosPresupuestales(Connection con, String anioVigenciaRublo, String codigoRublo, String descripcionRublo, int institucion);
	
	/**
	 * Mï¿½todo encargado de Obtener el cï¿½digo de un centro de atenciï¿½n dado su nombre
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion);
	
	/**
	 * Encargado de devolver la via de ingreso de una cuenta.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerViaIngresoCuenta(Connection con, int cuenta); 
	
	/**
	 * Encargado de devolver las solicitudes de procedimientos, cirugias, interconsultas, cargos directos y asociados a una factra
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerSolicitudesFacturas(Connection con, int codFactura);
	
	/**
	 * Metodo encargado de consultar los datos del Centro
	 * de Atencion, pudiendo se filtrar por diferentes criterios.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- consecutivo --> Requerido
	 * -- institucion --> Opcional
	 * -- codigo --> Opcional
	 * -- activo --> Opcional
	 * @return Mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------
	 * codigo,descripcion,codigo_inst_sirc,
	 * empresa_institucion,direccion
	 */
	public HashMap obtenerDatosCentroAtencion (Connection connection,HashMap criterios);

	/**
	 * 
	 * @param con
	 * @param consecutivos
	 * @param matrizLaboratorios
	 * @return
	 */
	public boolean insertarRegistrosInterfazWINLAB(Connection con,ArrayList consecutivos, HashMap matrizLaboratorios);

	/**
	 * 
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String obtenerCodigoPisoCama(Connection con, String codigoCama);

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarEncabezadoLaboratoriosPendientesWINLAB(Connection con);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap consultarDetalleLaboratoriosPendientesWINLAB(Connection con,String consecutivo);

	/**
	 * 
	 * @param con
	 * @param consecutivos
	 * @return
	 */
	public boolean eliminarLaboratoriosPendientesWINLAB(Connection con,String consecutivos);
	
	/**
	 * Consulta si un centro de costos esta parametrizado como un Registro Respuesta Proce X Terceros
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public String esCentroCostoRespuestaProcTercero(Connection con,HashMap parametros);
	
	/**
	 * Metodo encargado de verificar si existe algun registro de la cuenta
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public boolean existeAlgunRegistroEgreso(Connection con, int cuenta);
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en el sistema
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con,HashMap parametros);	
	
	/**
	 * Metodo encargado para consultar el codigo de clase inventario de un articulo especifico
	 * @param con
	 * @param articulo
	 * @return
	 */
	public String consultarClaseInterfazArticulo(Connection con, String articulo);

	/**
	 * Esta sentencia modifica la definiciï¿½n del motor de BD, para que trabaje con Fechas en formato YYYY-DD-MM
	 * @return
	 */
	public boolean actualizarFormatoFechaBD();
	
	/**
	 * Metodo para bsucar el tipo de entidad que ejecuta de la tabla centros_costo
	 * @return
	 */
	public String getTipoEntidadEjecutaCC(Connection con, String codigoCC);
	
	/**
	 * Metodo para obtener el CC solicitado de la solicitud
	 * @return
	 */
	public String obtenerCCSolicitadoSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * Metodo para verificar si existe la autorizacion para la entidad subcontratada
	 * @return
	 */
	public boolean existeAutorizacionSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * Metodo para obtener el CC solicitado de la solicitud
	 * @return
	 */
	public String obtenerTipoAutorizacionEntidadSubcontratada(Connection con, String numeroSolicitud);
	
	/**
	 * Metodo para verificar si el usuario se encuentrara registrado en la entidad subcontratada para que peuda responder ordenes
	 * @return
	 */
	public String verificarregistroUsuarioEntidadSubcontratada(Connection con, String numeroSolicitud, String login);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable 
	 * @return
	 */
	public HashMap obtenerCuentaContableXCodigo(Connection con, int codigoInstitucion, String cuentaContable);

	public ArrayList<ArrayList<Object>> listarProgramasPyP(Connection con, boolean activo, int codigoInstitucion);

	/**
	 * Metodo para verificar si un convenio tiene chekeado Reporte de Atencion de Urgencias
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public boolean esConvenioConReportAtencionUrg(Connection con,String codConvenio);
	
	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Gï¿½mez 
	 */
	public ArrayList<HashMap> obtenerDiasSemanaArray(Connection con);

	
	/**
	 * Metodo que consulta los parentezcos
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarParentezcos(Connection con);

	
	/**
	 * Metodo que consulta los parentezcos
	 * @param con
	 * @param tipoMotivo 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarMotivosCita(	Connection con, int codInstitucion, String tipoMotivo);

	
	/**
	 * Metodo que consulta los Medios por el cual se adquirio conociminto del servicio
	 * @param con
	 * @param codInstitucion
	 */
	public ArrayList<HashMap<String, Object>> consultarMediosConocimientoServ(Connection con,	int codInstitucion);

	/**
	 * Obtener los convenios del parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * 
	 * Keys del mapa
	 * 		- codigo
	 * 		- descripcion
	 * 
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConveniosAMostrarPresupuestoOdo(Connection con, int codInstitucion);

	/**
	 * Actualizar el parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * @param con
	 * @param conveniosAMostrarPresupuestoOdo
	 * @param institucion 
	 * @param usuario 
	 */
	public Object actualizarConveniosAMostrarPresupuestoOdo(Connection con,ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo, String usuario, int institucion);

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList obtenerConveniosContrato(Connection con, HashMap campos);

	/**
	 * Obtener informacion sobre la cuenta solicitante de una orden ambulatoria
	 * @param con
	 * @param numeroOrden
	 * @return
	 */
	public HashMap obtenerInfoCuentaSolicitanteOrdenAmbulatoria(Connection con, int codigoOrden);

	/**
	 * Obtener una lista de DtoIntegridadDominio con las constantes enviadas en el String[]
	 * 
	 * @param con 
	 * @param listaConstantes Array con las constatnes solicitadas
	 * @param ordenar
	 * 
	 * @return ArrayList<<code>DtoIntegridadDominio</code>> con las constantes de interidad solicitadas
	 */

	public ArrayList<DtoIntegridadDominio> generarListadoConstantesIntegridadDominio(Connection con, String[] listaConstantes, boolean ordenar);

	/**
	 * Obtener Motivos Atencion Odontologica
	 * @param con
	 * @param tipo atencion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMotivosAtencionOdontologica(Connection con, int tipo);
	
	
	/**+
	 * 
	 * 
	 * 
	 */
	public  double guardarProcAutoEstados(DtoLogProcAutoEstados dto) ;
	
	/**
	 * 
	 * 
	 * 
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasXTipo(Connection con, int institucion, String tipoCobertura);
	
	/**
	 * 
	 */
	public ArrayList<DtoConceptosIngTesoreria> obtenerConceptosIngresoTesoreria(int tipoIngreso,String valorFiltro);
	
	
	/**
	 * 
	 */
	public String obtenerIndicativoInterfazPersona(Connection con, String nroId);
	
	/**
	 * 
	 */
	public String obtenerIndicativoInterfazMedico(Connection con, int codigoPersona);
	
	/**
	 * 
	 */
	public HashMap obtenerDatosTercero(Connection con, int codigoTercero);
	
	/**
	 * 
	 * Método que se encarga de consultar el filtro asociado al concepto de ingreso
	 * de tesoreria.
	 * 
	 * @param con
	 * @param concepto
	 * @return
	 */
	public String obtenerFiltroValorConceptoIngreso(Connection con, int concepto);

	/**
	 * 
	 * @param con
	 * @param orden
	 * @param institucion
	 * @return
	 */
	public boolean esOrdenAmbulatoriaPYP(Connection con, String orden,int institucion);
	
	/**
	 * OBTENER EL NOMBRE DE LA IDENTIFICACION, 
	 * RECIBE EL ACRONIMO
	 * @param acronimo
	 * @return
	 */
	public String obtenerNombreTipoIdentificacion(String acronimo);

	/**
	 * Método encargado de arreglar los consecutivos de una tabla
	 * @param parametros
	 * @return
	 */
	public boolean arreglarConsecutivos(HashMap<String, Object> parametros);

	/**
	 * 
	 * @param loginUsuario
	 * @return
	 */
	public String obtenerEmailUsuario(String loginUsuario);

	/**
	 * 
	 * @param codigoPersona
	 * @param tipoMovimiento
	 * @param codigoCita
	 * @return
	 */
	public double obtenerAbonoPacienteTipoYNumeroDocumento(int codigoPersona,int tipoMovimiento, int codigoCita);

	public HashMap obtenerInformacionUsuarioCapitado(Connection con,
			String tipoID, String numeroID);

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public DtoDiagnostico getDiagnosticoPacienteIngreso(Connection con, int idIngreso);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public DtoDiagnostico getDiagnosticoPacienteCuenta(Connection con, int idCuenta);

	/**
	 * 
	 * @param con
	 * @param numReciboCaja
	 * @param codigoInstitucionInt
	 * @param codigoCentroAtencion
	 * @return
	 */
	public String obtenerCodigoReciboCaja(Connection con, String numReciboCaja,int codigoInstitucionInt, int codigoCentroAtencion);

	
	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @return
	 */
	public String obtenerTipoRegimenClasificacionSocioEconomica(Connection con,int clasificacionSE);

	/**
	 * Verfica si existen conceptos notas pacientes creados
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public boolean existenConceptosNotasPaciente(Connection con);
	
	/**
	 * Verfica si existen notas pacientes creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public boolean existenNotasPaciente(Connection con);
	
	/**
	 * Verfica si existen movimientos abonos creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public boolean existenMovimientosAbonos(Connection con);
	
	/**
	 * Este metodo consulta los centros de atención por usuario y estado activo
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el consecutivo del centro de atención
	 * 		   y en la posición 1 la descripción del mismo
	 */
	public ArrayList<String[]> obtenerCentrosAtencionxUsarioEstadoActivo(Connection con, String usuario);
	
	
	/**
	 * Este metodo consulta los centros de costo por usuario
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el codigo del centro de costo,
	 * 		   en la posición 1 el nombrem en la 2 la descripción del centro de atención
	 * 		   y la posición 3 el consecutivo del centro de atención al que pertenece
	 */
	public ArrayList<String[]> obtenerCentrosCostoxUsario(Connection con, String usuario);
	
	/**
	 * @param con
	 * @param centroAtencion
	 * @param usuario
	 * @param valorParametro
	 * @return cantidad de cajas por usuario 
	 */
	public  int obtenerCantidadCajasCajero(Connection con,int centroAtencion, String usuario,String valorParametro);
	
	
	/**
	 * Método que busca el numero de la cuenta de la autorizacion por orden ambulatoria
	 * @param con
	 * @param numeroSolcitud
	 * @return numero de la cuenta 
	 */
	public Integer obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(Connection con,Integer numeroSolcitud);
	
	/**
	 * @param con
	 * @param codigoVia
	 * @return nombres via de administracion 
	 */
	public String obtenerNombreViaAdministracion(Connection con,int codigoVia);
	
	/**
	 * Valida si el convenio es capitacion subcontratada 
	 * @param con
	 * @param codigoConvenio
	 * @return si un convenio es capitacion subcontratada
	 */
	public  Boolean esCapitacionSubcontratada(Connection con,Integer codigoConvenio);
	
	public boolean liberarUltimaCamaPaciente(Connection con, Integer codigoPersona);
	
	/**
	 * Consulta todas las especialidades que posee un profesional de la salud
	 * 
	 * @param connection
	 * @param profesionalHQxDto
	 * @param codigoInstitucion
	 * @param especiliadadesActivas
	 * @param descartarEspecialidades
	 * @return lista de especialidades de un profesional
	 * @throws BDException
	 * @author jeilones
	 * @created 5/07/2013
	 */
	List<EspecialidadDto> consultarEspecialidadesProfesional(Connection connection,ProfesionalHQxDto profesionalHQxDto,int codigoInstitucion, Boolean especiliadadesActivas,List<EspecialidadDto>descartarEspecialidades) throws BDException;
}