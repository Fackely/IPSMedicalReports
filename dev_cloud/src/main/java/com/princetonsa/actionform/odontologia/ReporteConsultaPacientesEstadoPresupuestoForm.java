package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno
 * 
 * @author Angela Maria Aguirre
 * @since 9/10/2010
 */
public class ReporteConsultaPacientesEstadoPresupuestoForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
	
	/**
	 * Atributo que almacena el listado de los países.
	 */
	private ArrayList<Paises> listaPaises;
	
	/**
	 * Atributo que almacena el listado de las ciudades 
	 * pertenecientes a un país determinado.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Atributo que almacena el listado de regiones de 
	 * cobertura que se encuentran activas en el sistema.
	 */
	private ArrayList<RegionesCobertura> listaRegiones;
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institución existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el listado de los centros de atención.
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoCheckBox> listaSexoPaciente;
	
	/**
	 * Atributo que almacena un listado con los estados del presupuesto.
	 */
	private ArrayList<DtoIntegridadDominio> listadoEstadosPresupuesto;
	
	/**
	 * Atributo que almacena un listado con los indicativos de contrato
	 * del presupuesto en estado contratado.
	 */
	private ArrayList<DtoIntegridadDominio> listadoIndicativoContrato;
	
	/**
	 * Atributo que almacena un listado con las especialidades
	 * odontol&oacute;gicas.
	 */
	private List<Especialidades> listadoEspecialidadesOdonto;
	
	/**
	 * Atributo que almacena el listado de los paquetes 
	 * odontol&oacute;gicos creados en el sistema.
	 */
	private ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto;
	
	/**
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	private String listaCodigoProgramaServicios;
	
	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Atributo que almacena los filtros de búsqueda seleccinados por el usuario
	 */
	private DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros;
	
	/**
	 * Atributo que determina si de debe mostrar el filtro de instituciones
	 */
	private boolean mostarFiltroInstitucion;
	
	/**
	 * Atributo que determina si de debe mostrar el filtro de indicativo de contrato
	 */
	private boolean mostarFiltroIndicativoContrato;
	
	/**
	 * Atributo que determina si de debe mostrar el filtro de Solicitud
	 * autorización descuento
	 */
	private boolean mostarFiltroSolicitudAutorDescuento;
	
	/**
	 * Atributo que determina si de debe mostrar el filtro de programas
	 */
	private boolean mostarFiltroPrograma;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	/**
	 * Almacena los registros de estado de presupuesto encontrados
	 */
	private ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstituciones;
	
	/**
	 * Indice de la institución del estado seleccionado
	 */
	private int indiceInstitucion;
	
	/**
	 * Indice del centro de atención del estado seleccionado
	 */
	private int indiceCA;
	
	/**
	 * Almacena los registros de estado de presupuesto encontrados
	 */
	private int indiceEstado;
	
	/**
	 * Indice del estado seleccionado
	 */
	private ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>  listaPacientes;
	
	/**
	 * Atributo que indica si se debe deshabilitar o no 
	 * el campo de programas odontol&oacute;gicos.
	 */
	private String deshabilitaPrograma;
	
	
	/**
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Angela Aguirre
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.odontologia.ReporteConsultaPacientesEstadoPresupuestoForm");
		
		if(this.estado.equals("consultar")){
			if(this.dtoFiltros.getFechaInicial()==null || 
					this.dtoFiltros.getFechaFinal() == null	) {
				errores.add("Fechas Nulas", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.fechasNulas")));
			}else{
				if(this.dtoFiltros.getFechaInicial().compareTo(Calendar.getInstance().getTime())>0){
					errores.add("Rango Inicial mayor q la fecha actual", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.fechaInicialMayor")));
				}
				if(dtoFiltros.getFechaFinal().compareTo(Calendar.getInstance().getTime())>0){
					errores.add("Fecha final menor que la fecha inicial", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.fechaFinalMayorSistema")));
				}
				
				ResultadoBoolean fechaFinalMayorIgual = UtilidadFecha.compararFechas(
						UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinal()),
						"00:00",UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaInicial()),"00:00");
				
				if(!fechaFinalMayorIgual.isResultado()){
					errores.add("Fecha final menor que la fecha inicial", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.fechaFinalMenor")));
				}				
			}
			if(dtoFiltros.getEdadFinal()!=null && dtoFiltros.getEdadInicial()!=null){
				if(dtoFiltros.getEdadFinal().intValue() < dtoFiltros.getEdadInicial().intValue()){
					errores.add("Rango edad final menor que el rango de edad inicial", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.edadFinalMenor")));
				}
			}
			if(UtilidadTexto.isEmpty(this.dtoFiltros.getCodigoPais())){
				errores.add("País Nulo", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.PaisNulo")));
			}
			if(!UtilidadTexto.isEmpty(this.dtoFiltros.getEstadoPresupuesto()) && 
					this.dtoFiltros.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
				
				if(UtilidadTexto.isEmpty(this.dtoFiltros.getConSolicitudDcto())){
					errores.add("Indicativo Solicitud Nulo", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ReporteConsultaPacientesEstadoPresupuestoForm.indicativoSolicitudNulo")));
				}
				
			}
			
		}
		
		return errores;
	}
	
	
	/**
	 * Este método se encarga de inicializar todos 
	 * los valores de la forma.
	 * @author Angela Aguirre
	 */
	public void reset(){
		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.listaCiudades = new ArrayList<Ciudades>();
		this.listaCodigoProgramaServicios = "";
		this.listadoEspecialidadesOdonto = new ArrayList<Especialidades>();
		this.listadoEstadosPresupuesto = new ArrayList<DtoIntegridadDominio>();
		this.listadoIndicativoContrato = new ArrayList<DtoIntegridadDominio>();
		this.listadoPaquetesOdonto = new ArrayList<PaquetesOdontologicos>();
		this.listaEmpresaInstitucion = new ArrayList<EmpresasInstitucion>();
		this.listaPaises = new ArrayList<Paises>();
		this.listaRegiones = new ArrayList<RegionesCobertura>();
		this.listaSexoPaciente = new ArrayList<DtoCheckBox>();		
		this.tipoSalida="";
		this.enumTipoSalida = null;
		this.estado = "";
		this.mostarFiltroInstitucion=false;
		this.mostarFiltroIndicativoContrato=false;
		this.mostarFiltroSolicitudAutorDescuento=false;
		this.mostarFiltroPrograma=false;
		this.dtoFiltros = new DtoReporteConsultaPacienteEstadoPresupuesto();
		this.dtoFiltros.setCiudadDeptoPais("");
		this.dtoSerProSerPro =new RecomSerproSerpro();
		this.dtoSerProSerPro.setProgramas(new Programas());
		this.dtoSerProSerPro.setCodigoPk(ConstantesBD.codigoNuncaValido);
		this.dtoSerProSerPro.getProgramas().setCodigo(ConstantesBD.codigoNuncaValidoLong);
		this.dtoFiltros.setEstadoPresupuesto("");
		this.dtoFiltros.setCodigoPrograma(ConstantesBD.codigoNuncaValidoLong);
		this.listaInstituciones = new ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>();
		this.nombreArchivoGenerado = "";
		this.dtoFiltros.setCodigoEmpresaInstitucion(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setConSolicitudDcto("");
		this.deshabilitaPrograma="false";
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaPaises
	
	 * @return retorna la variable listaPaises 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaPaises
	
	 * @param valor para el atributo listaPaises 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaCiudades
	
	 * @return retorna la variable listaCiudades 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaCiudades
	
	 * @param valor para el atributo listaCiudades 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaRegiones
	
	 * @return retorna la variable listaRegiones 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaRegiones
	
	 * @param valor para el atributo listaRegiones 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEmpresaInstitucion
	
	 * @return retorna la variable listaEmpresaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEmpresaInstitucion
	
	 * @param valor para el atributo listaEmpresaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaCentrosAtencion
	
	 * @return retorna la variable listaCentrosAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaCentrosAtencion
	
	 * @param valor para el atributo listaCentrosAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaSexoPaciente
	
	 * @return retorna la variable listaSexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoCheckBox> getListaSexoPaciente() {
		return listaSexoPaciente;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaSexoPaciente
	
	 * @param valor para el atributo listaSexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaSexoPaciente(ArrayList<DtoCheckBox> listaSexoPaciente) {
		this.listaSexoPaciente = listaSexoPaciente;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoEstadosPresupuesto
	
	 * @return retorna la variable listadoEstadosPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoIntegridadDominio> getListadoEstadosPresupuesto() {
		return listadoEstadosPresupuesto;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoEstadosPresupuesto
	
	 * @param valor para el atributo listadoEstadosPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoEstadosPresupuesto(
			ArrayList<DtoIntegridadDominio> listadoEstadosPresupuesto) {
		this.listadoEstadosPresupuesto = listadoEstadosPresupuesto;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoIndicativoContrato
	
	 * @return retorna la variable listadoIndicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoIntegridadDominio> getListadoIndicativoContrato() {
		return listadoIndicativoContrato;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoIndicativoContrato
	
	 * @param valor para el atributo listadoIndicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoIndicativoContrato(
			ArrayList<DtoIntegridadDominio> listadoIndicativoContrato) {
		this.listadoIndicativoContrato = listadoIndicativoContrato;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoEspecialidadesOdonto
	
	 * @return retorna la variable listadoEspecialidadesOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public List<Especialidades> getListadoEspecialidadesOdonto() {
		return listadoEspecialidadesOdonto;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoEspecialidadesOdonto
	
	 * @param valor para el atributo listadoEspecialidadesOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoEspecialidadesOdonto(
			List<Especialidades> listadoEspecialidadesOdonto) {
		this.listadoEspecialidadesOdonto = listadoEspecialidadesOdonto;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoPaquetesOdonto
	
	 * @return retorna la variable listadoPaquetesOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<PaquetesOdontologicos> getListadoPaquetesOdonto() {
		return listadoPaquetesOdonto;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoPaquetesOdonto
	
	 * @param valor para el atributo listadoPaquetesOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoPaquetesOdonto(
			ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto) {
		this.listadoPaquetesOdonto = listadoPaquetesOdonto;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaCodigoProgramaServicios
	
	 * @return retorna la variable listaCodigoProgramaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaCodigoProgramaServicios
	
	 * @param valor para el atributo listaCodigoProgramaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCodigoProgramaServicios(String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoSalida
	
	 * @return retorna la variable tipoSalida 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoSalida
	
	 * @param valor para el atributo tipoSalida 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo enumTipoSalida
	
	 * @return retorna la variable enumTipoSalida 
	 * @author Angela Maria Aguirre 
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo enumTipoSalida
	
	 * @param valor para el atributo enumTipoSalida 
	 * @author Angela Maria Aguirre 
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoFiltros
	
	 * @return retorna la variable dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public DtoReporteConsultaPacienteEstadoPresupuesto getDtoFiltros() {
		return dtoFiltros;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoFiltros
	
	 * @param valor para el atributo dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoFiltros(DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostarFiltroInstitucion
	
	 * @return retorna la variable mostarFiltroInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostarFiltroInstitucion() {
		return mostarFiltroInstitucion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostarFiltroInstitucion
	
	 * @param valor para el atributo mostarFiltroInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostarFiltroInstitucion(boolean mostarFiltroInstitucion) {
		this.mostarFiltroInstitucion = mostarFiltroInstitucion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostarFiltroIndicativoContrato
	
	 * @return retorna la variable mostarFiltroIndicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostarFiltroIndicativoContrato() {
		return mostarFiltroIndicativoContrato;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostarFiltroIndicativoContrato
	
	 * @param valor para el atributo mostarFiltroIndicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostarFiltroIndicativoContrato(
			boolean mostarFiltroIndicativoContrato) {
		this.mostarFiltroIndicativoContrato = mostarFiltroIndicativoContrato;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostarFiltroSolicitudAutorDescuento
	
	 * @return retorna la variable mostarFiltroSolicitudAutorDescuento 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostarFiltroSolicitudAutorDescuento() {
		return mostarFiltroSolicitudAutorDescuento;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostarFiltroSolicitudAutorDescuento
	
	 * @param valor para el atributo mostarFiltroSolicitudAutorDescuento 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostarFiltroSolicitudAutorDescuento(
			boolean mostarFiltroSolicitudAutorDescuento) {
		this.mostarFiltroSolicitudAutorDescuento = mostarFiltroSolicitudAutorDescuento;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostarFiltroPrograma
	
	 * @return retorna la variable mostarFiltroPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostarFiltroPrograma() {
		return mostarFiltroPrograma;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostarFiltroPrograma
	
	 * @param valor para el atributo mostarFiltroPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostarFiltroPrograma(boolean mostarFiltroPrograma) {
		this.mostarFiltroPrograma = mostarFiltroPrograma;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreArchivoGenerado
	
	 * @return retorna la variable nombreArchivoGenerado 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreArchivoGenerado
	
	 * @param valor para el atributo nombreArchivoGenerado 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoSerProSerPro
	
	 * @return retorna la variable dtoSerProSerPro 
	 * @author Angela Maria Aguirre 
	 */
	public RecomSerproSerpro getDtoSerProSerPro() {
		
		return dtoSerProSerPro;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoSerProSerPro
	
	 * @param valor para el atributo dtoSerProSerPro 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaInstituciones
	
	 * @return retorna la variable listaInstituciones 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> getListaInstituciones() {
		return listaInstituciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaInstituciones
	
	 * @param valor para el atributo listaInstituciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaInstituciones(
			ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indiceInstitucion
	
	 * @return retorna la variable indiceInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceInstitucion() {
		return indiceInstitucion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indiceInstitucion
	
	 * @param valor para el atributo indiceInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceInstitucion(int indiceInstitucion) {
		this.indiceInstitucion = indiceInstitucion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indiceCA
	
	 * @return retorna la variable indiceCA 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceCA() {
		return indiceCA;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indiceCA
	
	 * @param valor para el atributo indiceCA 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceCA(int indiceCA) {
		this.indiceCA = indiceCA;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indiceEstado
	
	 * @return retorna la variable indiceEstado 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceEstado() {
		return indiceEstado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indiceEstado
	
	 * @param valor para el atributo indiceEstado 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceEstado(int indiceEstado) {
		this.indiceEstado = indiceEstado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaPacientes
	
	 * @return retorna la variable listaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> getListaPacientes() {
		return listaPacientes;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaPacientes
	
	 * @param valor para el atributo listaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaPacientes(
			ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo deshabilitaPrograma
	
	 * @return retorna la variable deshabilitaPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo deshabilitaPrograma
	
	 * @param valor para el atributo deshabilitaPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}	

}
