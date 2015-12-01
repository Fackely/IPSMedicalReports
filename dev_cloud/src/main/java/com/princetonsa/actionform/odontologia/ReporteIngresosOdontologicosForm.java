

package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 * @author Yennifer Guerrero
 * @since  25/08/2010
 *
 */
public class ReporteIngresosOdontologicosForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
	
	/**
	 * Dto para almacenar los par&aacute;metros de b&uacute;squeda 
	 * de la secci&oacute;n de Ingresos en los reportes de ingresos odontol&oacute;gicos.
	 */
	private DtoReporteIngresosOdontologicos filtroIngresos;
	
	/**
	 * Atributo que almacena el listado de los paises.
	 */
	private ArrayList<Paises> listaPaises;
	
	/**
	 * Atributo que almacena el listado de las ciudades 
	 * pertenecientes a un pa&iacute;s determinado.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Atributo que almacena el listado de regiones de 
	 * cobertura que se encuentran activas en el sistema.
	 */
	private ArrayList<RegionesCobertura> listaRegiones;
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String esMultiempresa;
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institucion existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el listado de los centros de atenci&oacute;n.
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoCheckBox> listaSexoPaciente;
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	/**
	 * Atributo que indica si el campo de regi&oacute;n se encuentra deshabilitado.
	 */
	private boolean deshabilitaRegion;
	
	/**
	 * Atributo que almacena el acr&oacute;nimo de la ubicaci&oacute;n del
	 * logo seg&uacute;n est&eacute; definido en el par&aacute;metro general.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena el listado de los ingresos odontol&oacute;gicos 
	 * que cumplen con los filtros indicados por el usuario.
	 */
	private ArrayList<DtoIngresosOdontologicos> listadoIngresosOdonto;
	
	/**
	 * Atributo que almacena el resultado de la consulta del primer filtro de b&uacute;squeda
	 * correspondiente a los ingresos.
	 */
	private ArrayList<DtoResultadoConsultaIngresosOdonto> listaResultadoConsultaIngresos;
	
	/**
	 * Atributo que almacena el listado de profesionales de la salud 
	 * con ocupaciones auxiliar y odontólogo
	 */
	private ArrayList<DtoPersonas> listaProfesionales;
	
	/**
	 * Atributo que almacena un listado con los centros de atenci&oacute;n en
	 * los cuales se ha registrado ingresos.
	 */
	private ArrayList<DtoCentrosAtencion> listadoCentroAtencionIngreso;
	
	/**
	 * Atributo que permite mostrar el segundo filtro de la consulta.
	 */
	private String mostrarSegundoFiltro;
	
	/**
	 * Atributo que permite mostrar el tercer filtro de la consulta.
	 */
	private String mostrarTercerFiltro;
	
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
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	/**
	 * Atributo que almacena el listado de promociones odontol&oacute;gicas.
	 */
	private ArrayList<DtoDetPromocionOdo> listaDetPromociones = new ArrayList<DtoDetPromocionOdo>();
	
	/**
	 * DTO PARA MANEJAR LA RECOMENDACIONES SERVICIO PROGRAMA
	 */
	private RecomendacionesServProg dtoRecomenServicioPrograma;
	
	/**
	 * Atributo que indica si la instituci&oacute;n utiliza programas odontol&oacute;gicos.
	 */
	private String utilizaProgramasOdonto; 
	
	/**
	 * Atributo que permite obtener el valor del indice de una lista en
	 * la ultima posici&oacute;n.
	 */
	private int ayudanteUltimoIndice;
	
	/**
	 * Atributo que inidca si se debe mostrar o no
	 * el encabezado de los ingresos por centro de atenci&oacute;n.
	 */
	private String mostrarEncabezadoIngresos;
	
	/**
	 * Atributo que indica si se debe deshabilitar o no 
	 * el campo de programas odontol&oacute;gicos.
	 */
	private String deshabilitaPrograma;
	
	/**
	 * Atributo que almacena un valor que determina
	 * si debe ser habilitado o no el campo edad final.
	 */
	private boolean deshabilitaEdadFinal;
	
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
	 * Atributo que almacena la ruta del logo de la institución.
	 */
	private String rutaLogo;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Fecha en la cual es generado el reporte.
	 */
	private String fechaActual;
	
	/**
	 * Almacena un valor según el parámetro general para indicar si se debe deshabilitar
	 * la busqueda de servicios.
	 */
	private String utilizaServiciosOdonto;
	
	/**
	 * Permite definir si se debe habilitar o deshabilitar el 
	 * campo paquete odontológico.
	 */
	private boolean deshabilitaPaquete;
	
	/**
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Yennifer Guerrero
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("generarReporte")){
			if((filtroIngresos.getFechaInicial()==null) || 
					((filtroIngresos.getFechaInicial().toString()).equals(""))){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						filtroIngresos.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));	
				}
			}
			if((filtroIngresos.getFechaFinal()==null) || 
						((filtroIngresos.getFechaFinal().toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						filtroIngresos.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				}
				if((filtroIngresos.getFechaInicial()!=null) && 
						(!(filtroIngresos.getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							filtroIngresos.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				}
			}
			
			if (filtroIngresos.getEdadInicial()!= null) {
				
				if (filtroIngresos.getEdadFinal()== null) {
					
					errores.add("EL RANGO DE EDAD FINAL ES REQUERIDO.", new ActionMessage(
					 "errors.rangoEdadFinalRequerido"));
					
				}
				else {
					
					if (filtroIngresos.getEdadInicial() >= filtroIngresos.getEdadFinal()){
						
						errores.add("EDAD FIN MENOR QUE EDAD INICIAL.", new ActionMessage(
								 "errors.edadInicialMayorIgualEdadFinal"," Inicial "
								 +filtroIngresos.getEdadInicial()," Final "
								 +filtroIngresos.getEdadFinal()));
					}
				}
				
			}
			
			if (UtilidadTexto.isEmpty(filtroIngresos.getCodigoPaisResidencia())
					|| filtroIngresos.getCodigoPaisResidencia().trim().equals("-1")) {
				
				errores.add("PAÍS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
			}
			
			
			if (UtilidadTexto.isEmpty(filtroIngresos.getConValoracion())) {
				
				errores.add("CAMPO INGRESOS REQUERIDO.", new ActionMessage(
						 "errors.ingresosRequerido"));
			}
			
			if (!UtilidadTexto.isEmpty(filtroIngresos.getConValoracion()) && !filtroIngresos.getConValoracion().equals(ConstantesBD.acronimoNo)) {
				if (UtilidadTexto.isEmpty(filtroIngresos.getConPresupuesto())) {
					
					errores.add("CAMPO REGISTROS REQUERIDO.", new ActionMessage(
							 "errors.registrosRequerido"));
				}
			}
			
			if (!UtilidadTexto.isEmpty(filtroIngresos.getConPresupuesto()) && filtroIngresos.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)) {
				if (UtilidadTexto.isEmpty(filtroIngresos.getConSolicitudDcto())) {
					
					errores.add("SOLICITUD DE DESCUENTO REQUERIDO.", new ActionMessage(
							 "errors.solicitudDctoRequerido"));
				
					tipoSalida = null;
					enumTipoSalida = null;
					this.nombreArchivoGenerado = 			null;
				}
			}
		}
		return errores;
	}
	
	/**
	 * Este m&eacute;todo se encarga de inicializar todos los valores de la forma.
	 * @author Yennifer Guerrero
	 */
	public void reset(){
		
		this.estado="";
		this.filtroIngresos = 					new DtoReporteIngresosOdontologicos();
		this.listaPaises = 						new ArrayList<Paises>();
		this.listaCiudades = 					new ArrayList<Ciudades>();
		this.listaRegiones = 					new ArrayList<RegionesCobertura>();
		this.listaCentrosAtencion = 			new ArrayList<DtoCentrosAtencion>();
		this.listaEmpresaInstitucion = 			new ArrayList<EmpresasInstitucion>();
		this.listaSexoPaciente = 				new ArrayList<DtoCheckBox>();
		this.deshabilitaCiudad= 				false;
		this.deshabilitaRegion= 				false;
		this.listadoIngresosOdonto= 			new ArrayList<DtoIngresosOdontologicos>();
		this.ubicacionLogo=						"";
		this.path= 								"";
		this.esMultiempresa=					"";		
		this.ubicacionLogo = 					"";		
		this.listadoIngresosOdonto = 			new ArrayList<DtoIngresosOdontologicos>();		
		this.listaResultadoConsultaIngresos = 	new ArrayList<DtoResultadoConsultaIngresosOdonto>();		
		this.listaProfesionales = 				new ArrayList<DtoPersonas>();		
		this.listadoCentroAtencionIngreso = 	new ArrayList<DtoCentrosAtencion>();		
		this.mostrarSegundoFiltro = 			"";
		this.listadoEspecialidadesOdonto =		new ArrayList<Especialidades>();
		this.setDtoSerProSerPro(				new RecomSerproSerpro());
		this.dtoSerProSerPro = 					new RecomSerproSerpro();
		this.dtoSerProSerPro.setProgramas(		new Programas());
		this.dtoRecomenServicioPrograma = 		new RecomendacionesServProg();
		this.filtroIngresos.setCodigoPrograma(0);
		this.dtoSerProSerPro.setCodigoPk(0);
		this.dtoSerProSerPro.setProgramas(		new Programas());
		this.ayudanteUltimoIndice=ConstantesBD.codigoNuncaValido;
		this.deshabilitaPrograma = "false";
		this.deshabilitaEdadFinal=				true;
		this.tipoSalida= 						null;
		this.enumTipoSalida= 					null;
		this.nombreArchivoGenerado = 			null;
		this.deshabilitaPaquete = false;
	}
	
	public void resetSegundoFiltro (){
		filtroIngresos.setLogin("");
		filtroIngresos.setRangoEdadFinal("");
		this.tipoSalida= 						null;
		this.enumTipoSalida= 					null;
		this.nombreArchivoGenerado = 			null;
		this.filtroIngresos.setConSolicitudDcto(null);
	}
	
	public void resetTercerFiltro (){
		filtroIngresos.setEstadoPresupuesto("");
		filtroIngresos.setIndicativoContrato("");
		filtroIngresos.setCodigoEspecialidad(ConstantesBD.codigoNuncaValido);
		filtroIngresos.setCodigoPaqueteOdonto(ConstantesBD.codigoNuncaValido);
		filtroIngresos.setEstadoPresupuesto("");
		filtroIngresos.setIndicativoContrato("");
		filtroIngresos.setCodigoEspecialidad(ConstantesBD.codigoNuncaValido);
		filtroIngresos.setCodigoPaqueteOdonto(ConstantesBD.codigoNuncaValido);
		filtroIngresos.setConSolicitudDcto(null);
		filtroIngresos.getServicio().setNombre(null);
		filtroIngresos.getServicio().setCodigo(null);
		this.tipoSalida= 						null;
		this.enumTipoSalida= 					null;
		this.nombreArchivoGenerado = 			null;
		this.deshabilitaPaquete= false;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estado
	 * 
	 * @param  valor para el atributo estado 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo path
	 * 
	 * @param  valor para el atributo path 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo filtroIngresos
	 * 
	 * @return  Retorna la variable filtroIngresos
	 */
	public DtoReporteIngresosOdontologicos getFiltroIngresos() {
		return filtroIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo filtroIngresos
	 * 
	 * @param  valor para el atributo filtroIngresos 
	 */
	public void setFiltroIngresos(DtoReporteIngresosOdontologicos filtroIngresos) {
		this.filtroIngresos = filtroIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaPaises
	 * 
	 * @return  Retorna la variable listaPaises
	 */
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaPaises
	 * 
	 * @param  valor para el atributo listaPaises 
	 */
	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCiudades
	 * 
	 * @return  Retorna la variable listaCiudades
	 */
	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaCiudades
	 * 
	 * @param  valor para el atributo listaCiudades 
	 */
	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaRegiones
	 * 
	 * @return  Retorna la variable listaRegiones
	 */
	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaRegiones
	 * 
	 * @param  valor para el atributo listaRegiones 
	 */
	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo esMultiempresa
	 * 
	 * @return  Retorna la variable esMultiempresa
	 */
	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiempresa
	 * 
	 * @param  valor para el atributo esMultiempresa 
	 */
	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaEmpresaInstitucion
	 * 
	 * @return  Retorna la variable listaEmpresaInstitucion
	 */
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaEmpresaInstitucion
	 * 
	 * @param  valor para el atributo listaEmpresaInstitucion 
	 */
	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCentrosAtencion
	 * 
	 * @return  Retorna la variable listaCentrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaCentrosAtencion
	 * 
	 * @param  valor para el atributo listaCentrosAtencion 
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaCiudad
	 * 
	 * @return  Retorna la variable deshabilitaCiudad
	 */
	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaRegion
	 * 
	 * @return  Retorna la variable deshabilitaRegion
	 */
	public boolean isDeshabilitaRegion() {
		return deshabilitaRegion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaCiudad
	 * 
	 * @param  valor para el atributo deshabilitaCiudad 
	 */
	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaRegion
	 * 
	 * @param  valor para el atributo deshabilitaRegion 
	 */
	public void setDeshabilitaRegion(boolean deshabilitaRegion) {
		this.deshabilitaRegion = deshabilitaRegion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * 
	 * @param  valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
		
		filtroIngresos.setUbicacionLogo(ubicacionLogo);
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoIngresosOdonto
	 * 
	 * @return  Retorna la variable listadoIngresosOdonto
	 */
	public ArrayList<DtoIngresosOdontologicos> getListadoIngresosOdonto() {
		return listadoIngresosOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoIngresosOdonto
	 * 
	 * @param  valor para el atributo listadoIngresosOdonto 
	 */
	public void setListadoIngresosOdonto(
			ArrayList<DtoIngresosOdontologicos> listadoIngresosOdonto) {
		this.listadoIngresosOdonto = listadoIngresosOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaResultadoConsultaIngresos
	 * 
	 * @return  Retorna la variable listaResultadoConsultaIngresos
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> getListaResultadoConsultaIngresos() {
		return listaResultadoConsultaIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaResultadoConsultaIngresos
	 * 
	 * @param  valor para el atributo listaResultadoConsultaIngresos 
	 */
	public void setListaResultadoConsultaIngresos(
			ArrayList<DtoResultadoConsultaIngresosOdonto> listaResultadoConsultaIngresos) {
		this.listaResultadoConsultaIngresos = listaResultadoConsultaIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaProfesionales
	 * 
	 * @return  Retorna la variable listaProfesionales
	 */
	public ArrayList<DtoPersonas> getListaProfesionales() {
		return listaProfesionales;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaProfesionales
	 * 
	 * @param  valor para el atributo listaProfesionales 
	 */
	public void setListaProfesionales(ArrayList<DtoPersonas> listaProfesionales) {
		this.listaProfesionales = listaProfesionales;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoCentroAtencionIngreso
	 * 
	 * @return  Retorna la variable listadoCentroAtencionIngreso
	 */
	public ArrayList<DtoCentrosAtencion> getListadoCentroAtencionIngreso() {
		return listadoCentroAtencionIngreso;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoCentroAtencionIngreso
	 * 
	 * @param  valor para el atributo listadoCentroAtencionIngreso 
	 */
	public void setListadoCentroAtencionIngreso(
			ArrayList<DtoCentrosAtencion> listadoCentroAtencionIngreso) {
		this.listadoCentroAtencionIngreso = listadoCentroAtencionIngreso;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo mostrarSegundoFiltro
	 * 
	 * @return  Retorna la variable mostrarSegundoFiltro
	 */
	public String getMostrarSegundoFiltro() {
		return mostrarSegundoFiltro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo mostrarSegundoFiltro
	 * 
	 * @param  valor para el atributo mostrarSegundoFiltro 
	 */
	public void setMostrarSegundoFiltro(String mostrarSegundoFiltro) {
		this.mostrarSegundoFiltro = mostrarSegundoFiltro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo mostrarTercerFiltro
	 * 
	 * @return  Retorna la variable mostrarTercerFiltro
	 */
	public String getMostrarTercerFiltro() {
		return mostrarTercerFiltro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo mostrarTercerFiltro
	 * 
	 * @param  valor para el atributo mostrarTercerFiltro 
	 */
	public void setMostrarTercerFiltro(String mostrarTercerFiltro) {
		this.mostrarTercerFiltro = mostrarTercerFiltro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoEstadosPresupuesto
	 * 
	 * @return  Retorna la variable listadoEstadosPresupuesto
	 */
	public ArrayList<DtoIntegridadDominio> getListadoEstadosPresupuesto() {
		return listadoEstadosPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoEstadosPresupuesto
	 * 
	 * @param  valor para el atributo listadoEstadosPresupuesto 
	 */
	public void setListadoEstadosPresupuesto(
			ArrayList<DtoIntegridadDominio> listadoEstadosPresupuesto) {
		this.listadoEstadosPresupuesto = listadoEstadosPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoIndicativoContrato
	 * 
	 * @return  Retorna la variable listadoIndicativoContrato
	 */
	public ArrayList<DtoIntegridadDominio> getListadoIndicativoContrato() {
		return listadoIndicativoContrato;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoIndicativoContrato
	 * 
	 * @param  valor para el atributo listadoIndicativoContrato 
	 */
	public void setListadoIndicativoContrato(
			ArrayList<DtoIntegridadDominio> listadoIndicativoContrato) {
		this.listadoIndicativoContrato = listadoIndicativoContrato;
	}

	public void setListadoEspecialidadesOdonto(
			List<Especialidades> listadoEspecialidadesOdonto) {
		this.listadoEspecialidadesOdonto = listadoEspecialidadesOdonto;
	}

	public List<Especialidades> getListadoEspecialidadesOdonto() {
		return listadoEspecialidadesOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoPaquetesOdonto
	 * 
	 * @return  Retorna la variable listadoPaquetesOdonto
	 */
	public ArrayList<PaquetesOdontologicos> getListadoPaquetesOdonto() {
		return listadoPaquetesOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoPaquetesOdonto
	 * 
	 * @param  valor para el atributo listadoPaquetesOdonto 
	 */
	public void setListadoPaquetesOdonto(
			ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto) {
		this.listadoPaquetesOdonto = listadoPaquetesOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCodigoProgramaServicios
	 * 
	 * @return  Retorna la variable listaCodigoProgramaServicios
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaCodigoProgramaServicios
	 * 
	 * @param  valor para el atributo listaCodigoProgramaServicios 
	 */
	public void setListaCodigoProgramaServicios(String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dtoSerProSerPro
	 * 
	 * @return  Retorna la variable dtoSerProSerPro
	 */
	public RecomSerproSerpro getDtoSerProSerPro() {
		
		filtroIngresos.setCodigoPrograma(dtoSerProSerPro.getProgramas().getCodigo());
		filtroIngresos.setNombrePrograma(dtoSerProSerPro.getProgramas().getNombre());
		return dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dtoSerProSerPro
	 * 
	 * @param  valor para el atributo dtoSerProSerPro 
	 */
	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaDetPromociones
	 * 
	 * @return  Retorna la variable listaDetPromociones
	 */
	public ArrayList<DtoDetPromocionOdo> getListaDetPromociones() {
		return listaDetPromociones;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaDetPromociones
	 * 
	 * @param  valor para el atributo listaDetPromociones 
	 */
	public void setListaDetPromociones(
			ArrayList<DtoDetPromocionOdo> listaDetPromociones) {
		this.listaDetPromociones = listaDetPromociones;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dtoRecomenServicioPrograma
	 * 
	 * @return  Retorna la variable dtoRecomenServicioPrograma
	 */
	public RecomendacionesServProg getDtoRecomenServicioPrograma() {
		return dtoRecomenServicioPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dtoRecomenServicioPrograma
	 * 
	 * @param  valor para el atributo dtoRecomenServicioPrograma 
	 */
	public void setDtoRecomenServicioPrograma(
			RecomendacionesServProg dtoRecomenServicioPrograma) {
		this.dtoRecomenServicioPrograma = dtoRecomenServicioPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo utilizaProgramasOdonto
	 * 
	 * @return  Retorna la variable utilizaProgramasOdonto
	 */
	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo utilizaProgramasOdonto
	 * 
	 * @param  valor para el atributo utilizaProgramasOdonto 
	 */
	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}

	
	/**
	 * 
	 * Este m&eacute;todo se encarga de 
	 * @param ayudanteUltimoIndice
	 *
	 * @author Yennifer Guerrero
	 */
	public void setAyudanteUltimoIndice(int ayudanteUltimoIndice) {
		
		this.ayudanteUltimoIndice = ayudanteUltimoIndice;
	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de 
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int getAyudanteUltimoIndice() {
		
		if (listaResultadoConsultaIngresos!=null) {
			this.ayudanteUltimoIndice = listaResultadoConsultaIngresos.size() - 1;
		}
		
		Log4JManager.info(this.ayudanteUltimoIndice);
		
		return ayudanteUltimoIndice;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo mostrarEncabezadoIngresos
	 * 
	 * @return  Retorna la variable mostrarEncabezadoIngresos
	 */
	public String getMostrarEncabezadoIngresos() {
		return mostrarEncabezadoIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo mostrarEncabezadoIngresos
	 * 
	 * @param  valor para el atributo mostrarEncabezadoIngresos 
	 */
	public void setMostrarEncabezadoIngresos(String mostrarEncabezadoIngresos) {
		this.mostrarEncabezadoIngresos = mostrarEncabezadoIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaPrograma
	 * 
	 * @return  Retorna la variable deshabilitaPrograma
	 */
	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaPrograma
	 * 
	 * @param  valor para el atributo deshabilitaPrograma 
	 */
	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo deshabilitaEdadFinal
	 * 
	 * @return  Retorna la variable deshabilitaEdadFinal
	 */
	public boolean isDeshabilitaEdadFinal() {
		return deshabilitaEdadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaEdadFinal
	 * 
	 * @param  valor para el atributo deshabilitaEdadFinal 
	 */
	public void setDeshabilitaEdadFinal(boolean deshabilitaEdadFinal) {
		this.deshabilitaEdadFinal = deshabilitaEdadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tipoSalida
	 * 
	 * @return  Retorna la variable tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo tipoSalida
	 * 
	 * @param  valor para el atributo tipoSalida 
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo enumTipoSalida
	 * 
	 * @return  Retorna la variable enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo enumTipoSalida
	 * 
	 * @param  valor para el atributo enumTipoSalida 
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rutaLogo
	 * 
	 * @param  valor para el atributo rutaLogo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
		
		filtroIngresos.setRutaLogo(rutaLogo);
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreArchivoGenerado
	 * 
	 * @return  Retorna la variable nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreArchivoGenerado
	 * 
	 * @param  valor para el atributo nombreArchivoGenerado 
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaSexoPaciente
	 * 
	 * @return  Retorna la variable listaSexoPaciente
	 */
	public ArrayList<DtoCheckBox> getListaSexoPaciente() {
		return listaSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaSexoPaciente
	 * 
	 * @param  valor para el atributo listaSexoPaciente 
	 */
	public void setListaSexoPaciente(ArrayList<DtoCheckBox> listaSexoPaciente) {
		this.listaSexoPaciente = listaSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaActual
	 * 
	 * @return  Retorna la variable fechaActual
	 */
	public String getFechaActual() {
		return fechaActual;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaActual
	 * 
	 * @param  valor para el atributo fechaActual 
	 */
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

	public String getUtilizaServiciosOdonto() {
		return utilizaServiciosOdonto;
	}

	public void setUtilizaServiciosOdonto(String utilizaServiciosOdonto) {
		this.utilizaServiciosOdonto = utilizaServiciosOdonto;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  deshabilitaPaquete
	 *
	 * @return retorna la variable deshabilitaPaquete
	 */
	public boolean isDeshabilitaPaquete() {
		return deshabilitaPaquete;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo deshabilitaPaquete
	 * @param deshabilitaPaquete es el valor para el atributo deshabilitaPaquete 
	 */
	public void setDeshabilitaPaquete(boolean deshabilitaPaquete) {
		this.deshabilitaPaquete = deshabilitaPaquete;
	}
	
}
