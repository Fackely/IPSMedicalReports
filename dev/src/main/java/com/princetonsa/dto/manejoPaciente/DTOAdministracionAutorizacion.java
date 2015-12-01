package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.TiposContrato;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/01/2011
 */
public class DTOAdministracionAutorizacion implements Serializable {
	
	/** Serial 	* */
	private static final long serialVersionUID = 1L;
	
	private DtoEntidadSubcontratada entidadSubcontratada;
	private Convenios convenio;
	private long consecutivoAutorizacion;
	private long consecutivoAutorizacionEntSub;
	private Date fechaAutorizacionEntSub;
	private Date fechaInicioAutorizacion;
	private Date fechaVencimientoAutorizacion;
	private int diasEstanciaAutorizados;
	private String indicativoTemporal;
	private String tipoAutorizacion;
	private DtoPaciente paciente;
	private long codigoPk;
	private long codigoPkAutoEntSub;
	private boolean administracionPoblacionCapitada; 
	private boolean ordenarDescendente;
	private long codIngresoEstancia;
	private Date fechaModifica;
//	private Integer numeroSolicitud;
	private String estado;
	
	private String observaciones;
	private String usuarioContacta;
	private String cargoUsuarioContacta;
	
	private boolean esSolicitudArticulo;
	private boolean excluirRegistrosTemporales;
	
	private String descripcionEntidadSubIngEst;
	
	/**Atributo que almacena la fecha de admisi&oacute;n del paciente en el ingreso instancia
	 * @author Diana Carolina G  **/
	private Date fechaAdmision;
	
	private int codigoViaIngreso;
	private String acronimoDiagnosticoPrinc;
	
	private Long idIngresoEstanciaCapitacion;
	
	/**Almacena atributos del centro de costo Ingreso Estancia*/
	private DtoCentroCosto dtoCentroCosto;
	private String tipoAfiliado;
	private String clasificacionSE;
	
	private int codigoInstitucion;
	
	/**
	 * Atributo que almacena la bandera de auotizacion entregada
	 */
	private boolean esAutorizacionEntregada;
	
	/**
	 * Lista de articulos por autorizacion
	 * */
	private List<DtoArticulosAutorizaciones> listaArticulosAuto= new ArrayList<DtoArticulosAutorizaciones>(0);
	/**
	 * Lista de servicios por autorizacion
	 * */
	private List<DtoServiciosAutorizaciones> listaServiciosAuto=new ArrayList<DtoServiciosAutorizaciones>(0);
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author Angela Maria Aguirre
	 */
	public DTOAdministracionAutorizacion()
	{
		this.entidadSubcontratada				= new DtoEntidadSubcontratada();
		this.convenio							= new Convenios();
		this.consecutivoAutorizacion			= ConstantesBD.codigoNuncaValidoLong;
		this.fechaInicioAutorizacion			= null;
		this.fechaVencimientoAutorizacion		= null;
		this.diasEstanciaAutorizados			= ConstantesBD.codigoNuncaValido;
		this.indicativoTemporal				= "";
		this.tipoAutorizacion					= "";
		this.paciente							= new DtoPaciente();
		this.codigoPk							= ConstantesBD.codigoNuncaValidoLong;
		this.administracionPoblacionCapitada	= false; 
		this.ordenarDescendente					= false;
		this.codIngresoEstancia					= ConstantesBD.codigoNuncaValido;
		this.fechaModifica						= null;
		this.observaciones						= "";
		this.usuarioContacta					= "";
		this.cargoUsuarioContacta				= "";
		this.estado								= "";		
		this.excluirRegistrosTemporales			= false;
		this.descripcionEntidadSubIngEst		= "";
		this.idIngresoEstanciaCapitacion		= null;
		this.dtoCentroCosto						= new DtoCentroCosto();
		this.tipoAfiliado						= "";
		this.clasificacionSE					= "";
		this.esAutorizacionEntregada 			= false;
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de ingreso 
	 * estancia por paciente
	 * 
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param int diasEstancia
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * 
	 * @author, Angela Maria Aguirre
	 */
	public DTOAdministracionAutorizacion(long codigoPk,long consecutivo, Date fechaAutorizacion,
			int diasEstancia, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio, 
			long codIngresoEstancia,String estadoAutorizIngrEstan, Date fechaModifica,String descripcionEntidadSubIngEst, 
			Date fechaAdmision, Long idIngresoEstanciaCapitacion){
		
		this.codigoPk                			= codigoPk;	
		this.consecutivoAutorizacion 			= consecutivo;
		this.fechaInicioAutorizacion 			= fechaAutorizacion;
		this.diasEstanciaAutorizados 			= diasEstancia;
		this.indicativoTemporal      			= String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    			= new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 			= new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada 	= true;
		this.ordenarDescendente		 			= false;
		this.codIngresoEstancia					= codIngresoEstancia;	
		this.estado								= estadoAutorizIngrEstan;
		this.fechaModifica						= fechaModifica;
		this.descripcionEntidadSubIngEst		= descripcionEntidadSubIngEst;
		this.fechaAdmision						= fechaAdmision;
		this.idIngresoEstanciaCapitacion		= idIngresoEstanciaCapitacion;
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de ingreso 
	 * estancia por paciente
	 * 
	 * Se elimina la fecha de modificacion proveniente del historico y el codigo de la entidad AUTORIZACIONES_ESTANCIA_CAPITA 
	 * debido a que no interesa si el ingreso tiene autorizaciones de capitacion
	 * 
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param int diasEstancia
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * 
	 * @author, Jeison Londono
	 */
	public DTOAdministracionAutorizacion(long codigoPk,long consecutivo, Date fechaAutorizacion,
			int diasEstancia, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio, 
			long codIngresoEstancia,String estadoAutorizIngrEstan, String descripcionEntidadSubIngEst, 
			Date fechaAdmision){
		
		this.codigoPk                			= codigoPk;	
		this.consecutivoAutorizacion 			= consecutivo;
		this.fechaInicioAutorizacion 			= fechaAutorizacion;
		this.diasEstanciaAutorizados 			= diasEstancia;
		this.indicativoTemporal      			= String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    			= new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 			= new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada 	= true;
		this.ordenarDescendente		 			= false;
		this.codIngresoEstancia					= codIngresoEstancia;	
		this.estado								= estadoAutorizIngrEstan;
		//this.fechaModifica						= fechaModifica;
		this.descripcionEntidadSubIngEst		= descripcionEntidadSubIngEst;
		this.fechaAdmision						= fechaAdmision;
		//this.idIngresoEstanciaCapitacion		= idIngresoEstanciaCapitacion;
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por paciente
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param Date fechaVencimiento
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * @author Angela Maria Aguirre
	 */
	public DTOAdministracionAutorizacion(long codigoPk, long consecutivo, Date fechaAutorizacion,
			Date fechaVencimiento, char indicativoTemporal, String entidadSubcontratada,
			String nombreConvenio, String estadoAutorizacion,
			Long idAutorizArticulo,Long idAutorizServicio,String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		this.convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		this.estado						=estadoAutorizacion;
		if(idAutorizArticulo!=null&&idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
			
		this.descripcionEntidadSubIngEst = descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
	}
	
	
	/**
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por paciente
	 * 
	 * @param codigoPk
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicativoTemporal
	 * @param entidadSubcontratada
	 * @param nombreConvenio
	 * @param estadoAutorizacion
	 * @param idAutorizArticulo
	 * @param idAutorizServicio
	 * @param descripcionEntidadSubIngEst
	 * @param idIngresoEstanciaCapitacion
	 * @param consecutivoAutorizacionEntSub
	 * @author Jeison Londono
	 */
	public DTOAdministracionAutorizacion(long codigoPk, long consecutivo, Date fechaAutorizacion,
			Date fechaVencimiento, char indicativoTemporal, String entidadSubcontratada,
			String nombreConvenio, String estadoAutorizacion,
			Long idAutorizArticulo,Long idAutorizServicio,String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion,
			long codigoPkAutoEntSub,
			String consecutivoAutorizacionEntSub, Date fechaAutorizacionEntSub){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		this.convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		this.estado						=estadoAutorizacion;
		if(idAutorizArticulo!=null&&idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
			
		this.descripcionEntidadSubIngEst = descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
		this.codigoPkAutoEntSub=codigoPkAutoEntSub;
		if(consecutivoAutorizacionEntSub!=null && !consecutivoAutorizacionEntSub.trim().isEmpty()){
			this.consecutivoAutorizacionEntSub=Long.parseLong(consecutivoAutorizacionEntSub);
		}
		this.fechaAutorizacionEntSub=fechaAutorizacionEntSub;
	}
	
	/**
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por paciente
	 * 
	 * @param codigoPk
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicativoTemporal
	 * @param entidadSubcontratada
	 * @param nombreConvenio
	 * @param estadoAutorizacion
	 * @param idAutorizArticulo
	 * @param idAutorizServicio
	 * @param descripcionEntidadSubIngEst
	 * @param idIngresoEstanciaCapitacion
	 * @param codigoPkAutoEntSub
	 * @param consecutivoAutorizacionEntSub
	 * @param fechaAutorizacionEntSub
	 * @param idAutorizacionEntregada
	 * @author jeilones
	 * @created 13/08/2012
	 */
	public DTOAdministracionAutorizacion(
			long codigoPk,
			long consecutivo,
			Date fechaAutorizacion,
			Date fechaVencimiento,
			char indicativoTemporal,
			String entidadSubcontratada,
			String nombreConvenio,
			String estadoAutorizacion,
			long idAutorizArticulo,
			long idAutorizServicio,
			String descripcionEntidadSubIngEst,
			Long idIngresoEstanciaCapitacion,
			long codigoPkAutoEntSub,
			String consecutivoAutorizacionEntSub, 
			Date fechaAutorizacionEntSub,
			Integer idAutorizacionEntregada){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		this.convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		this.estado						=estadoAutorizacion;
		if(idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
			
		this.descripcionEntidadSubIngEst = descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
		
		this.codigoPkAutoEntSub=codigoPkAutoEntSub;
		if(consecutivoAutorizacionEntSub!=null && !consecutivoAutorizacionEntSub.trim().isEmpty()){
			this.consecutivoAutorizacionEntSub=Long.parseLong(consecutivoAutorizacionEntSub);
		}
		this.fechaAutorizacionEntSub=fechaAutorizacionEntSub;
		
		if(idAutorizacionEntregada != null) {
			this.esAutorizacionEntregada = true;
		}	
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de ingreso 
	 * estancia por rango
	 * 
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param int diasEstancia
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * 
	 * @author, Angela Maria Aguirre
	 */
	@SuppressWarnings("deprecation")
	public DTOAdministracionAutorizacion(long codigoPk,long consecutivo, Date fechaAutorizacion,
			int diasEstancia, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio,
			String primerNombrePaciente,String segundoNombrePaciente,String primerApellidoPaciente,
			String segundoApellidoPaciente, int codigoPaciente, String tipoID, String acronimo,String estadoAutoriz,
			String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion){
		
		this.codigoPk                	= codigoPk;	
		this.consecutivoAutorizacion 	= consecutivo;
		this.fechaInicioAutorizacion 	= fechaAutorizacion;		
		this.diasEstanciaAutorizados 	= diasEstancia;
		this.indicativoTemporal      	= String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    	= new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 	= new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		
		this.paciente = new DtoPaciente();
		this.paciente.setPrimerNombre(primerNombrePaciente + " " + 
				(UtilidadTexto.isEmpty(segundoNombrePaciente)?"":segundoNombrePaciente) + " " +
				primerApellidoPaciente + " " + (UtilidadTexto.isEmpty(segundoApellidoPaciente)?"":segundoApellidoPaciente));
		this.paciente.setCodigo(codigoPaciente);
		this.paciente.setTipoId(tipoID + " " +acronimo);
		this.estado						=estadoAutoriz;
		this.descripcionEntidadSubIngEst=descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion=idIngresoEstanciaCapitacion;
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de ingreso 
	 * estancia por rango
	 * 
	 * Se remueve el ultimo argumento de idIngresoEstanciaCapitacion 
	 * puesto q no interesa si las autorizaciones de 
	 * ingreso estancia tiene autorizaciones de capitacion
	 * 
	 * @param codigoPk
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param diasEstancia
	 * @param indicativoTemporal
	 * @param entidadSubcontratada
	 * @param nombreConvenio
	 * @param primerNombrePaciente
	 * @param segundoNombrePaciente
	 * @param primerApellidoPaciente
	 * @param segundoApellidoPaciente
	 * @param codigoPaciente
	 * @param tipoID
	 * @param acronimo
	 * @param estadoAutoriz
	 * @param descripcionEntidadSubIngEst
	 * @author jeilones
	 * @created 13/08/2012
	 */
	@SuppressWarnings("deprecation")
	public DTOAdministracionAutorizacion(long codigoPk,long consecutivo, Date fechaAutorizacion,
			int diasEstancia, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio,
			String primerNombrePaciente,String segundoNombrePaciente,String primerApellidoPaciente,
			String segundoApellidoPaciente, int codigoPaciente, String tipoID, String acronimo,String estadoAutoriz,
			String descripcionEntidadSubIngEst){
		
		this.codigoPk                	= codigoPk;	
		this.consecutivoAutorizacion 	= consecutivo;
		this.fechaInicioAutorizacion 	= fechaAutorizacion;		
		this.diasEstanciaAutorizados 	= diasEstancia;
		this.indicativoTemporal      	= String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    	= new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 	= new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		
		this.paciente = new DtoPaciente();
		this.paciente.setPrimerNombre(primerNombrePaciente + " " + 
				(UtilidadTexto.isEmpty(segundoNombrePaciente)?"":segundoNombrePaciente) + " " +
				primerApellidoPaciente + " " + (UtilidadTexto.isEmpty(segundoApellidoPaciente)?"":segundoApellidoPaciente));
		this.paciente.setCodigo(codigoPaciente);
		this.paciente.setTipoId(tipoID + " " +acronimo);
		this.estado						=estadoAutoriz;
		this.descripcionEntidadSubIngEst=descripcionEntidadSubIngEst;
		//this.idIngresoEstanciaCapitacion=idIngresoEstanciaCapitacion;
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por rango
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param Date fechaVencimiento
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * @author Angela Maria Aguirre
	 */
	@SuppressWarnings("deprecation")
	public DTOAdministracionAutorizacion(long codigoPk, long consecutivo, Date fechaAutorizacion,
			Date fechaVencimiento, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio,
			String primerNombrePaciente,String segundoNombrePaciente,String primerApellidoPaciente,
			String segundoApellidoPaciente, int codigoPaciente, String tipoID, String acronimo,
			String estadoAutorizIngrEstan, Long idAutorizArticulo,Long idAutorizServicio, 
			String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		
		this.paciente = new DtoPaciente();
		this.paciente = new DtoPaciente();
		this.paciente.setPrimerNombre(primerNombrePaciente + " " + 
				(UtilidadTexto.isEmpty(segundoNombrePaciente)?"":segundoNombrePaciente) + " " +
				primerApellidoPaciente + " " + (UtilidadTexto.isEmpty(segundoApellidoPaciente)?"":segundoApellidoPaciente));
		
		this.paciente.setCodigo(codigoPaciente);		
		this.paciente.setTipoId(tipoID + " " +acronimo);		
		this.estado				 =estadoAutorizIngrEstan;
		if(idAutorizArticulo!=null&&idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
		
		this.descripcionEntidadSubIngEst=descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
		
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por rango
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param Date fechaVencimiento
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * @author Angela Maria Aguirre
	 * @param codigoPkAutoEntSub 
	 */
	@SuppressWarnings("deprecation")
	public DTOAdministracionAutorizacion(long codigoPk, long consecutivo, Date fechaAutorizacion,
			Date fechaVencimiento, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio,
			String primerNombrePaciente,String segundoNombrePaciente,String primerApellidoPaciente,
			String segundoApellidoPaciente, int codigoPaciente, String tipoID, String acronimo,
			String estadoAutorizIngrEstan, Long idAutorizArticulo,Long idAutorizServicio, 
			String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion,long codigoPkAutoEntSub,String consecutivoAutorizacionEntSub,Date fechaAutorizacionEntSub){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		
		this.paciente = new DtoPaciente();
		this.paciente = new DtoPaciente();
		this.paciente.setPrimerNombre(primerNombrePaciente + " " + 
				(UtilidadTexto.isEmpty(segundoNombrePaciente)?"":segundoNombrePaciente) + " " +
				primerApellidoPaciente + " " + (UtilidadTexto.isEmpty(segundoApellidoPaciente)?"":segundoApellidoPaciente));
		
		this.paciente.setCodigo(codigoPaciente);		
		this.paciente.setTipoId(tipoID + " " +acronimo);		
		this.estado				 =estadoAutorizIngrEstan;
		if(idAutorizArticulo!=null&&idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
		
		this.descripcionEntidadSubIngEst=descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
		
		this.codigoPkAutoEntSub=codigoPkAutoEntSub;
		if(consecutivoAutorizacionEntSub!=null && !consecutivoAutorizacionEntSub.trim().isEmpty()){
			this.consecutivoAutorizacionEntSub=Long.parseLong(consecutivoAutorizacionEntSub);
		}
		this.fechaAutorizacionEntSub=fechaAutorizacionEntSub;
	}
	
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de capitación por rango
	 * 
	 * Se cambia el tipo de idAutorizArticulo y idAutorizServicio ya que es un conteo 
	 * cuyo tipo es long primitivo 
	 * 
	 * @param codigoPk
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicativoTemporal
	 * @param entidadSubcontratada
	 * @param nombreConvenio
	 * @param primerNombrePaciente
	 * @param segundoNombrePaciente
	 * @param primerApellidoPaciente
	 * @param segundoApellidoPaciente
	 * @param codigoPaciente
	 * @param tipoID
	 * @param acronimo
	 * @param estadoAutorizIngrEstan
	 * @param idAutorizArticulo
	 * @param idAutorizServicio
	 * @param descripcionEntidadSubIngEst
	 * @param idIngresoEstanciaCapitacion
	 * @param consecutivoAutorizacionEntSub
	 * @param fechaAutorizacionEntSub
	 * @param idAutorizacionEntregada
	 * @author jeilones
	 * @created 13/08/2012
	 */
	@SuppressWarnings("deprecation")
	public DTOAdministracionAutorizacion(long codigoPk, long consecutivo, Date fechaAutorizacion,
			Date fechaVencimiento, char indicativoTemporal, String entidadSubcontratada,String nombreConvenio,
			String primerNombrePaciente,String segundoNombrePaciente,String primerApellidoPaciente,
			String segundoApellidoPaciente, int codigoPaciente, String tipoID, String acronimo,
			String estadoAutorizIngrEstan, long idAutorizArticulo,long idAutorizServicio, 
			String descripcionEntidadSubIngEst, Long idIngresoEstanciaCapitacion,
			long codigoPkAutoEntSub,
			String consecutivoAutorizacionEntSub,Date fechaAutorizacionEntSub,
			Integer idAutorizacionEntregada){
	
		this.codigoPk                = codigoPk;
		this.consecutivoAutorizacion = consecutivo;
		this.fechaInicioAutorizacion = fechaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.indicativoTemporal      = String.valueOf(indicativoTemporal);
		this.entidadSubcontratada    = new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 = new Convenios();
		convenio.setNombre(nombreConvenio);		
		this.administracionPoblacionCapitada = true;
		this.ordenarDescendente		 	= false;
		this.codIngresoEstancia			= ConstantesBD.codigoNuncaValidoLong;	
		
		this.paciente = new DtoPaciente();
		this.paciente = new DtoPaciente();
		this.paciente.setPrimerNombre(primerNombrePaciente + " " + 
				(UtilidadTexto.isEmpty(segundoNombrePaciente)?"":segundoNombrePaciente) + " " +
				primerApellidoPaciente + " " + (UtilidadTexto.isEmpty(segundoApellidoPaciente)?"":segundoApellidoPaciente));
		
		this.paciente.setCodigo(codigoPaciente);		
		this.paciente.setTipoId(tipoID + " " +acronimo);		
		this.estado				 =estadoAutorizIngrEstan;
		if(idAutorizArticulo>0){
			this.esSolicitudArticulo=true;
		}else{
			this.esSolicitudArticulo=false;
		}
		
		this.descripcionEntidadSubIngEst=descripcionEntidadSubIngEst;
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
		
		this.codigoPkAutoEntSub=codigoPkAutoEntSub;
		if(consecutivoAutorizacionEntSub!=null && !consecutivoAutorizacionEntSub.trim().isEmpty()){
			this.consecutivoAutorizacionEntSub=Long.parseLong(consecutivoAutorizacionEntSub);
		}
		this.fechaAutorizacionEntSub=fechaAutorizacionEntSub;
		
		if(idAutorizacionEntregada != null) {
			this.esAutorizacionEntregada = true;
		}	
	}
	
	/**
	 * 
	 * Método constructor de la clase, usado para mapear los datos
	 * del select de la consulta de autorizaciones de ingreso 
	 * estancia por paciente
	 * 
	 * @param long consecutivo
	 * @param Date fechaAutorizacion
	 * @param int diasEstancia
	 * @param char indicativoTemporal
	 * @param String entidadSubcontratada
	 * @param String nombreConvenio
	 * 
	 * @author, Camilo Gomez FIXME
	 */
	public DTOAdministracionAutorizacion(long codigoPk,long consecutivo, Date fechaAutorizacion,
			int diasEstancia, char indicativoTemporal, String tipoAfiliado,String clasificacionSE,
			String entidadSubcontratada,String nombreConvenio, String tipoContrato,	long codIngresoEstancia,
			String estadoAutorizIngrEstan, Date fechaModifica,String descripcionEntidadSubIngEst,Date fechaAdmision,
			int codigoCentroCosto, String nombreCentroCosto, String tipoEntidadEjecuta){
		
		this.codigoPk                			= codigoPk;	
		this.consecutivoAutorizacion 			= consecutivo;
		this.fechaInicioAutorizacion 			= fechaAutorizacion;
		this.diasEstanciaAutorizados 			= diasEstancia;
		this.indicativoTemporal      			= String.valueOf(indicativoTemporal);
		this.tipoAfiliado						= tipoAfiliado;
		this.clasificacionSE					= clasificacionSE;
		this.entidadSubcontratada    			= new DtoEntidadSubcontratada();
		this.entidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.convenio 				 			= new Convenios();
		convenio.setNombre(nombreConvenio);
		convenio.setTiposContrato(new TiposContrato());
		convenio.getTiposContrato().setNombre(tipoContrato);
		this.administracionPoblacionCapitada 	= true;
		this.ordenarDescendente		 			= false;
		this.codIngresoEstancia					= codIngresoEstancia;	
		this.estado								= estadoAutorizIngrEstan;
		this.fechaModifica						= fechaModifica;
		this.descripcionEntidadSubIngEst		= descripcionEntidadSubIngEst;
		this.fechaAdmision						= fechaAdmision;
		this.dtoCentroCosto						= new DtoCentroCosto();
		this.dtoCentroCosto.setCodigoCentroCosto(codigoCentroCosto);
		this.dtoCentroCosto.setNombre(nombreCentroCosto);
		this.dtoCentroCosto.setTipoEntidadEjecuta(tipoEntidadEjecuta);
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSubcontratada
	
	 * @return retorna la variable entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getEntidadSubcontratada() {
		return entidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSubcontratada
	
	 * @param valor para el atributo entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSubcontratada(DtoEntidadSubcontratada entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo convenio
	
	 * @return retorna la variable convenio 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getConvenio() {
		return convenio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenio
	
	 * @param valor para el atributo convenio 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoAutorizacion
	
	 * @return retorna la variable consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoAutorizacion
	
	 * @param valor para el atributo consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoAutorizacion(long consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaInicioAutorizacion
	
	 * @return retorna la variable fechaInicioAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaInicioAutorizacion() {
		return fechaInicioAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaInicioAutorizacion
	
	 * @param valor para el atributo fechaInicioAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaInicioAutorizacion(Date fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaVencimientoAutorizacion
	
	 * @return retorna la variable fechaVencimientoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaVencimientoAutorizacion() {
		return fechaVencimientoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaVencimientoAutorizacion
	
	 * @param valor para el atributo fechaVencimientoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaVencimientoAutorizacion(Date fechaVencimientoAutorizacion) {
		this.fechaVencimientoAutorizacion = fechaVencimientoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasEstanciaAutorizados
	
	 * @return retorna la variable diasEstanciaAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public int getDiasEstanciaAutorizados() {
		return diasEstanciaAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasEstanciaAutorizados
	
	 * @param valor para el atributo diasEstanciaAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasEstanciaAutorizados(int diasEstanciaAutorizados) {
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indicativoTemporal
	
	 * @return retorna la variable indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public String getIndicativoTemporal() {
		return indicativoTemporal;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indicativoTemporal
	
	 * @param valor para el atributo indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndicativoTemporal(String indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAutorizacion
	
	 * @return retorna la variable tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAutorizacion
	
	 * @param valor para el atributo tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo paciente
	
	 * @return retorna la variable paciente 
	 * @author Angela Maria Aguirre 
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo paciente
	
	 * @param valor para el atributo paciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo administracionPoblacionCapitada
	
	 * @param valor para el atributo administracionPoblacionCapitada 
	 * @author Cristhian Mirollo
	 */
	public boolean isAdministracionPoblacionCapitada() {
		return administracionPoblacionCapitada;
	}

	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo administracionPoblacionCapitada
	
	 * @param valor para el atributo administracionPoblacionCapitada 
	 * @author Cristhian Mirollo
	 */
	public void setAdministracionPoblacionCapitada(
			boolean administracionPoblacionCapitada) {
		this.administracionPoblacionCapitada = administracionPoblacionCapitada;
	}

	public boolean isOrdenarDescendente() {
		return ordenarDescendente;
	}

	public void setOrdenarDescendente(boolean ordenarDescendente) {
		this.ordenarDescendente = ordenarDescendente;
	}

	public long getCodIngresoEstancia() {
		return codIngresoEstancia;
	}

	public void setCodIngresoEstancia(long codIngresoEstancia) {
		this.codIngresoEstancia = codIngresoEstancia;
	}

	public Date getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getUsuarioContacta() {
		return usuarioContacta;
	}

	public void setUsuarioContacta(String usuarioContacta) {
		this.usuarioContacta = usuarioContacta;
	}

	public String getCargoUsuarioContacta() {
		return cargoUsuarioContacta;
	}

	public void setCargoUsuarioContacta(String cargoUsuarioContacta) {
		this.cargoUsuarioContacta = cargoUsuarioContacta;
	}
	/**
	 * Metodo para establecer el valor del estado de la autorizacion
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el valor del estado de la autoriazacion
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}

	public void setEsSolicitudArticulo(boolean esSolicitudArticulo) {
		this.esSolicitudArticulo = esSolicitudArticulo;
	}

	public boolean isEsSolicitudArticulo() {
		return esSolicitudArticulo;
	}

	public boolean isExcluirRegistrosTemporales() {
		return excluirRegistrosTemporales;
	}

	public void setExcluirRegistrosTemporales(boolean excluirRegistrosTemporales) {
		this.excluirRegistrosTemporales = excluirRegistrosTemporales;
	}

	public void setDescripcionEntidadSubIngEst(
			String descripcionEntidadSubIngEst) {
		this.descripcionEntidadSubIngEst = descripcionEntidadSubIngEst;
	}

	public String getDescripcionEntidadSubIngEst() {
		return descripcionEntidadSubIngEst;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor 
	 * del atributo fechaAdmision
	
	 * @param valor para el atributo fechaAdmision 
	 * @author Diana Carolina G
	 */
	public void setFechaAdmision(Date fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor 
	 * del atributo fechaAdmision
	 * 
	 * @return fechaAdmision
	 * @author Diana Carolina G
	 */
	public Date getFechaAdmision() {
		return fechaAdmision;
	}

	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	public void setAcronimoDiagnosticoPrinc(String acronimoDiagnosticoPrinc) {
		this.acronimoDiagnosticoPrinc = acronimoDiagnosticoPrinc;
	}

	public String getAcronimoDiagnosticoPrinc() {
		return acronimoDiagnosticoPrinc;
	}

	/**
	 * @return the idIngresoEstanciaCapitacion
	 */
	public Long getIdIngresoEstanciaCapitacion() {
		return idIngresoEstanciaCapitacion;
	}

	/**
	 * @param idIngresoEstanciaCapitacion the idIngresoEstanciaCapitacion to set
	 */
	public void setIdIngresoEstanciaCapitacion(Long idIngresoEstanciaCapitacion) {
		this.idIngresoEstanciaCapitacion = idIngresoEstanciaCapitacion;
	}
	
	
	
	
	/* Métodos utilizados para implementar el ordenar  */
	
	/**
	 * Obtiene el nombre del convenio asociado
	 * @return nombre del convenio
	 * @autor Cristhian Murillo
	*/
	public String getNombreConvenio(){
		return convenio.getNombre();
	}
	
	/**
	 * Obtiene la razón social de la entidad subcotnratada
	 * @return razón sicial de la entidad subcotnrtada
	 * @autor Cristhian Murillo
	*/
	public String getRazonSocialEntidadSubcontratada(){
		return entidadSubcontratada.getRazonSocial();
	}
	
	/**
	 * Obtiene el primer nombre del paciente
	 * @return primer nombre del paciente
	 * @autor Cristhian Murillo
	*/
	public String getNombrePaciente(){
		return paciente.getPrimerNombre();
	}

	/**
	 * Obtiene el tipo de ID del paciente
	 * @return tipo ID del paciente
	 * @autor Cristhian Murillo
	*/
	public String getTipoIDPaciente(){
		return paciente.getTipoId();
	}
	/* ---------------------------------------------  */

	/**
	 * Se obtiene atributos del centro de costo del Ingreso Estancia
	 * @param dtoCentroCosto
	 * @author Camilo Gomez
	 */
	public void setDtoCentroCosto(DtoCentroCosto dtoCentroCosto) {
		this.dtoCentroCosto = dtoCentroCosto;
	}

	/**
	 * Alamcena atributos del centro de costo del ingreso de estancia
	 * @return DtoCentroCosto
	 * @author Camilo Gomez
	 */
	public DtoCentroCosto getDtoCentroCosto() {
		return dtoCentroCosto;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setClasificacionSE(String clasificacionSE) {
		this.clasificacionSE = clasificacionSE;
	}

	public String getClasificacionSE() {
		return clasificacionSE;
	}

	/**
	 * @return the consecutivoAutorizacionEntSub
	 */
	public long getConsecutivoAutorizacionEntSub() {
		return consecutivoAutorizacionEntSub;
	}

	/**
	 * @param consecutivoAutorizacionEntSub the consecutivoAutorizacionEntSub to set
	 */
	public void setConsecutivoAutorizacionEntSub(long consecutivoAutorizacionEntSub) {
		this.consecutivoAutorizacionEntSub = consecutivoAutorizacionEntSub;
	}

	/**
	 * @return the fechaAutorizacionEntSub
	 */
	public Date getFechaAutorizacionEntSub() {
		return fechaAutorizacionEntSub;
	}

	/**
	 * @param fechaAutorizacionEntSub the fechaAutorizacionEntSub to set
	 */
	public void setFechaAutorizacionEntSub(Date fechaAutorizacionCap) {
		this.fechaAutorizacionEntSub = fechaAutorizacionCap;
	}

	/**
	 * @return the listaArticulosAuto
	 */
	public List<DtoArticulosAutorizaciones> getListaArticulosAuto() {
		return listaArticulosAuto;
	}

	/**
	 * @param listaArticulosAuto the listaArticulosAuto to set
	 */
	public void setListaArticulosAuto(
			List<DtoArticulosAutorizaciones> listaArticulosAuto) {
		this.listaArticulosAuto = listaArticulosAuto;
	}

	/**
	 * @return the listaServiciosAuto
	 */
	public List<DtoServiciosAutorizaciones> getListaServiciosAuto() {
		return listaServiciosAuto;
	}

	/**
	 * @param listaServiciosAuto the listaServiciosAuto to set
	 */
	public void setListaServiciosAuto(
			List<DtoServiciosAutorizaciones> listaServiciosAuto) {
		this.listaServiciosAuto = listaServiciosAuto;
	}

	/**
	 * @return the codigoPkAutoEntSub
	 */
	public long getCodigoPkAutoEntSub() {
		return codigoPkAutoEntSub;
	}

	/**
	 * @param codigoPkAutoEntSub the codigoPkAutoEntSub to set
	 */
	public void setCodigoPkAutoEntSub(long codigoPkAutoEntSub) {
		this.codigoPkAutoEntSub = codigoPkAutoEntSub;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the esAutorizacionEntregada
	 */
	public boolean isEsAutorizacionEntregada() {
		return esAutorizacionEntregada;
	}
	
	/**
	 * @param esAutorizacionEntregada the esAutorizacionEntregada to set
	 */
	public void setEsAutorizacionEntregada(boolean esAutorizacionEntregada) {
		this.esAutorizacionEntregada = esAutorizacionEntregada;
	}
	
}
