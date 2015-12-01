/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * @author armando
 *
 */
@SuppressWarnings("serial")
public class RerporteVentaTarjetasForm extends ValidatorForm 
{
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String estado;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Dto para almacenar los par&aacute;metros de b&uacute;squeda 
	 * de la secci&oacute;n de Ingresos en los reportes de ingresos odontol&oacute;gicos.
	 */
	private DtoFiltroReporteIngresosTarjetasCliente filtroIngresos;
	
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
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	/**
	 * Atributo que indica si el campo de regi&oacute;n se encuentra deshabilitado.
	 */
	private boolean deshabilitaRegion;
	
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
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
	private String tipoSalida;
	
	/**
	 * Lista los usuarios existentes en el sistema.
	 */
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoCheckBox> listaSexoComprador;
	
	/**
	 * Atributo que almacena el listado resultado de la consulta.
	 */
	private ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado;
	
	/**
	 * Atributo que almacena el acr&oacute;nimo de la ubicaci&oacute;n del
	 * logo seg&uacute;n est&eacute; definido en el par&aacute;metro general.
	 */
	private String ubicacionLogo;
	
	/**
	 * Fecha en la cual es generado el reporte.
	 */
	private String fechaActual;
	
	
	/**
	 * Este m&eacute;todo se encarga de inicializar todos los valores de la forma.
	 * @author Yennifer Guerrero
	 */
	public void reset(){
		
		this.estado 								= "";
		this.path 									= "";
		this.listaPaises 							= new ArrayList<Paises>();
		this.listaCiudades 							= new ArrayList<Ciudades>();
		this.listaRegiones 							= new ArrayList<RegionesCobertura>();
		this.listaCentrosAtencion 					= new ArrayList<DtoCentrosAtencion>();
		this.listaEmpresaInstitucion 				= new ArrayList<EmpresasInstitucion>();
		this.filtroIngresos 						= new DtoFiltroReporteIngresosTarjetasCliente();
		this.esMultiempresa							= "";	
		this.listaSexoComprador						= new ArrayList<DtoCheckBox>();
		this.deshabilitaCiudad						= false;
		this.deshabilitaRegion						= false;
		this.tipoSalida								= null;
		this.enumTipoSalida							= null;
		this.nombreArchivoGenerado 					= null;
		this.usuarios 								= new ArrayList<DtoUsuarioPersona>();
		this.filtroIngresos.setSexoComprador(ConstantesBD.codigoNuncaValido +"");
	}
	
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
			
			Date fechaIni = filtroIngresos.getFechaInicial();
			Date fechaFin = filtroIngresos.getFechaFinal();
			String consecutivoIni = filtroIngresos.getConsecutivoInicial();
			String consecutivoFin = filtroIngresos.getConsecutivoFinal();
			
			
			if (fechaIni == null && fechaFin == null) {
				
				if (UtilidadTexto.isEmpty(consecutivoIni) && UtilidadTexto.isEmpty(consecutivoFin)) {
					errores.add("Rangos de Fechas o Consecutivos de Factura Requeridos.", new ActionMessage(
					 "errores.fechasOConsecutivosRequeridos"));
					
				}else if (UtilidadTexto.isEmpty(consecutivoIni) && !UtilidadTexto.isEmpty(consecutivoFin)) {
					errores.add("Consecutivo inicial Factura es Requeridos.", new ActionMessage(
					 "errores.consecutivoInicialRequerido"));
					
				}else if (!UtilidadTexto.isEmpty(consecutivoIni) && UtilidadTexto.isEmpty(consecutivoFin)) {
					errores.add("Consecutivo Final Factura es Requeridos.", new ActionMessage(
					 "errores.consecutivoFinalRequerido"));
				}else if (Integer.parseInt(consecutivoFin)< Integer.parseInt(consecutivoIni)) {
					errores.add("CONSECUTIVO FINAL MENOR QUE CONSECUTIVO INICIAL.", new ActionMessage(
							 "errors.consecutivoFinalMenorQueConsecutivoInicial"," Inicial "+consecutivoIni," Final "+consecutivoFin));
				}
				
				
			}else if (UtilidadTexto.isEmpty(consecutivoIni) && UtilidadTexto.isEmpty(consecutivoFin)) {
				
				if (fechaIni == null && fechaFin !=  null) {
					errores.add("La fecha Inicial es requerida", 
							new ActionMessage("errors.required", "El campo Fecha Inicial"));
				}else if (fechaIni != null && fechaFin ==  null)  {
					errores.add("La fecha Final es requerida", 
							new ActionMessage("errors.required", "El campo Fecha Final"));
				}
			}
			
			
			if(filtroIngresos.getFechaInicial() != null){
				
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
			
			if(filtroIngresos.getFechaFinal() != null){
				
				String fechaFinString = UtilidadFecha.conversionFormatoFechaAAp(
						filtroIngresos.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFinString, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFinString," Actual "+fechaActual));
				}
				if((filtroIngresos.getFechaInicial()!=null) && 
						(!(filtroIngresos.getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							filtroIngresos.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFinString,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFinString," Inicial "+fechaInicial));
					}
				}
			}
			
			if (filtroIngresos.getEdadInicial()!= null) {
				
				if (filtroIngresos.getEdadFinal()== null) {
					
					errores.add("EL RANGO DE EDAD FINAL ES REQUERIDO.", new ActionMessage(
					 "errors.rangoEdadFinalCompradorRequerido"));
					
				}
				else {
					
					if (filtroIngresos.getEdadInicial() > filtroIngresos.getEdadFinal()){
						
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
								 "errors.edadInicialMayorIgualEdadFinal"," Inicial "
								 +filtroIngresos.getEdadInicial()," Final "
								 +filtroIngresos.getEdadFinal()));
					}
				}
				
				if (filtroIngresos.getEdadInicial()  <= 0) {
					
					errores.add("Rango Edad Inicial Comprador debe ser mayor a cero.", new ActionMessage(
					 "errors.rangoEdadInicialMenorIgualCero"));
				}
				
			}
			
			if (UtilidadTexto.isEmpty(filtroIngresos.getCodigoPaisResidencia())
					|| filtroIngresos.getCodigoPaisResidencia().trim().equals("-1")) {
				
				errores.add("PAÍS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
			}

			if(!UtilidadTexto.isEmpty(filtroIngresos.getConsecutivoInicial()))
			{
				try{
					Integer.parseInt(filtroIngresos.getConsecutivoInicial());
				}
				catch (NumberFormatException e) {
					errores.add("El campo Consecutivo inicial", new ActionMessage(
					 "errors.integer", "Consecutivo Inicial"));
				}
			}
			if(!UtilidadTexto.isEmpty(filtroIngresos.getConsecutivoFinal()))
			{
				try{
					Integer.parseInt(filtroIngresos.getConsecutivoFinal());
				}
				catch (NumberFormatException e) {
					errores.add("El campo Consecutivo final", new ActionMessage(
					 "errors.integer", "Consecutivo Final"));
				}
			}
		}
		
		return errores;
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
	public DtoFiltroReporteIngresosTarjetasCliente getFiltroIngresos() {
		return filtroIngresos;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo filtroIngresos
	 * 
	 * @param  valor para el atributo filtroIngresos 
	 */
	public void setFiltroIngresos(
			DtoFiltroReporteIngresosTarjetasCliente filtroIngresos) {
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
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaCiudad
	 * 
	 * @param  valor para el atributo deshabilitaCiudad 
	 */
	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
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
	 * del atributo deshabilitaRegion
	 * 
	 * @param  valor para el atributo deshabilitaRegion 
	 */
	public void setDeshabilitaRegion(boolean deshabilitaRegion) {
		this.deshabilitaRegion = deshabilitaRegion;
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
	 *  del atributo usuarios
	 * 
	 * @return  Retorna la variable usuarios
	 */
	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo usuarios
	 * 
	 * @param  valor para el atributo usuarios 
	 */
	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoResultado
	 * 
	 * @return  Retorna la variable listadoResultado
	 */
	public ArrayList<DtoResultadoReporteVentaTarjetas> getListadoResultado() {
		return listadoResultado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoResultado
	 * 
	 * @param  valor para el atributo listadoResultado 
	 */
	public void setListadoResultado(
			ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado) {
		this.listadoResultado = listadoResultado;
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
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaSexoComprador
	 * 
	 * @return  Retorna la variable listaSexoComprador
	 */
	public ArrayList<DtoCheckBox> getListaSexoComprador() {
		return listaSexoComprador;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaSexoComprador
	 * 
	 * @param  valor para el atributo listaSexoComprador 
	 */
	public void setListaSexoComprador(ArrayList<DtoCheckBox> listaSexoComprador) {
		this.listaSexoComprador = listaSexoComprador;
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

}
