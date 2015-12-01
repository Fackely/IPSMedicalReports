package com.princetonsa.actionform.parametrizacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.Listado;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.mundo.UsuarioBasico;


public class CentrosAtencionForm extends ActionForm 
{
	private ArrayList<DtoRegionesCobertura> arrayRegiones= new  ArrayList<DtoRegionesCobertura>();
	private ArrayList<DtoCategoriaAtencion> arrayCategorias= new  ArrayList<DtoCategoriaAtencion>();
	
	private String telefono;
	
	private String pais;
	
	private InfoDatosStr ciudad;
	
	private String ciudadFormato;
	
	private String departamento;
	
	private DtoRegionesCobertura regionCobertura;		
	
	private DtoCategoriaAtencion categoriaAtencion;
	
	private String estado;
	
	private int consecutivo;
	
	private String codigo;
	
	private String descripcion;
	
	private String activo;
	
	private String codUpgd;
	
	private String codigoInstSirc;
	
	private String mensaje;
	
	private String columna="descripcion";
	
	private Collection centrosAtencion;
	
	private HashMap institucionesSIRC;
	
	private String estadoAnterior;
	
	private String logModificacion;
	
	private String nomUpgd;
	
	private String nomInstitucionesSIRC;
	
	private String ultimaColumnaOrdenada="";
	
	private String ordenar=ConstantesBD.tipoOrdenamientoAscendente;
	//HashMap para el combo de Codigos UPGD
	private HashMap cmbUpgd = new HashMap();
	
	private HashMap cmb_inst_sirc = new HashMap();
	
	private int numCmbUpgd;
	
	private int num_inst_sirc;
	
	private String empresaInstitucion;
	
	private HashMap empresasInstitucionMap= new HashMap();
	
	
	/**
	 * HashMap que contiene las entidades subcontratadas activas
	 * asociadas a Centros de Costo identificados como Subalmacén 
	 */
	private HashMap entidadesSubcontratadas= new HashMap();
	
	/**
	 * Atributo qeu representa el codigo pk de la entidad subcontratada
	 */
	private String codigoEntidadSubcontratada;
	
	private String direccion="";
	
	
	private boolean  esConsulta;
	
	
	
	//<< 	ANEXO  959 >>
	private BigDecimal codigoConsecutivo;
	private String resolucion="";
	private String prefFactura="";
	private BigDecimal rangoInicialFactura;
	private DtoConsecutivoCentroAtencion dtoConsecutivo = new DtoConsecutivoCentroAtencion();
	private BigDecimal rangoFinalFactura;
	private int postConsecutivo;
	private int tamanoListaHistorico;
	private boolean cargarConsecutivosCentro;
	private String tipoConsecutivo;
	
	private int postHistoricio;
	private String tmpNombreBDConsecutivo;
	private BigDecimal consecutivoModificado;
	
	
	private int anioSistema;
	private int anioTmpDto;
	
	
	private String abrirFacturacion;
	private String abrirTesoria;
	private String abrirConsecutivos;
	
	
	
	private String codigoConsecutivoCentro;
	
	
	
	// si Existen consecutivos con anios no puede insertar nuevos consecutivos con el año en blanco
	private boolean idSePuedeModificar; 
	private boolean existeAnioRegistrado;
	
	
	/**
	 * PARAMETROS PARA CARGAR CONSECUTIVOS EN INTERFAZ
	 */
	private String manejaConsecutivoFactura;
	private String manejaConsecutivoTesoreria;
	
	
	
	
	private String idConsecutivoAnual;
	
	
	// PRUEBAS PARA BORRAR
	private boolean existeConsecutivoAnual;
	private boolean aplicaConsecutivoAnaul;
	

	/**
	 * LISTA PARA CARGAR LOS CONSECUTIVOS DE CENTRO DE ATENCION POR FACTURACION
	 */
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturacion = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	/**
	 * LISTA PARA CARGAR LOS CONSECUTIVOS DE CENTRO DE ATENCION POR TESORERIA
	 */
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroTesoreria = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	/**
	 * LISTA PARA CARGAR LOS HISTORICIOS
	 */
	private ArrayList<DtoConsecutivoCentroAtencion> listaHistoricos = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	
	//Agregado Anexo 959 
	private String codigoInterfaz="";
	
	private String piePaginaPresupuestoOdon="";
	
	
	/**
	 * Atributos para la parametrización del consecutivo de la
	 * factura Varia.
	 */
	private String resolucionFacturaVaria;
	private String prefFacturaVaria;
	private BigDecimal rangoInicialFacturaVaria;
	private BigDecimal rangoFinalFacturaVaria;
	private String abrirFacturaVaria;
	
	/**
	 * Atributo que indica si se maneja consecutivo
	 * de Factura varia por centro de Atención.
	 * 
	 */
	private String manejaConsecutivoFacturaVariaPorCentoAtencion;
	
	/**
	 * Consecutivos definidos en el centro de Atención para las facturas Varias
	 */
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturasVarias;
	
	/**
	 * Atributo que determina si se esta trabajando con el consecutivo
	 * de Facturas
	 *  
	 */
	private boolean consecutivoFactura;
	
	/**
	 * Atributo que determina si se esta trabajando con el consecutivo
	 * de Facturas Varias
	 * 
	 */
	private boolean consecutivoFacturaVaria;
	

	/**
	 * Atributo que indica si se maneja consecutivo
	 * Notas Débito Paciente por Centro de Atención del módulo de Tesorería
	 * 
	 */
	private String manejaConsecutivoNotasPacientesCentroAtencion;
	
	private String manejoEspecialInstitOdonto;
	
	public void clean()
	{
		
		this.arrayRegiones= new ArrayList<DtoRegionesCobertura>();
		this.arrayCategorias = new ArrayList<DtoCategoriaAtencion>();
		this.consecutivo=-1;
		this.codigo="";
		this.descripcion="";
		this.codUpgd = "";
		this.codigoInstSirc = "";
		this.activo="";
		this.estado="empezar";
		this.estadoAnterior="";
		this.mensaje="";
		this.centrosAtencion=new ArrayList();		
		this.logModificacion="";
		this.telefono = "";
		this.pais="";
		this.ciudad= new InfoDatosStr();
		this.departamento="";
		this.regionCobertura=new DtoRegionesCobertura();
		this.categoriaAtencion=new DtoCategoriaAtencion();
		this.nomUpgd = "";		
		this.institucionesSIRC = new HashMap();
		this.cmbUpgd = new HashMap();
		this.cmb_inst_sirc = new HashMap();
		this.cmb_inst_sirc.put("numRegistros",0);
		this.numCmbUpgd = 0;		
		this.num_inst_sirc = 0;
		
		this.empresasInstitucionMap= new HashMap();
		this.empresasInstitucionMap.put("numRegistros", "0");
		this.empresaInstitucion="";
		this.ultimaColumnaOrdenada="";
		this.ordenar=ConstantesBD.tipoOrdenamientoAscendente;
		this.direccion="";
		this.ciudadFormato = "";
		
		this.codigoInterfaz="";
		this.piePaginaPresupuestoOdon="";
		this.dtoConsecutivo=new DtoConsecutivoCentroAtencion();
		
		//<<<<<<<<<<<<<<<<<<<959>>>>>>>>>>>>>>>>>>>>>>>>><<
		this.resolucion="";
		this.prefFactura="";
		this.rangoInicialFactura=null;
		this.rangoFinalFactura=null; 
		this.listaConsecutivosCentroFacturacion = new ArrayList<DtoConsecutivoCentroAtencion>();
		this.listaConsecutivosCentroTesoreria= new ArrayList<DtoConsecutivoCentroAtencion>();
		this.codigoConsecutivo=BigDecimal.ZERO;
		this.listaHistoricos = new ArrayList<DtoConsecutivoCentroAtencion>();
		this.postConsecutivo= ConstantesBD.codigoNuncaValido;
		this.dtoConsecutivo = new DtoConsecutivoCentroAtencion();
		this.tamanoListaHistorico= ConstantesBD.codigoNuncaValido;
		this.cargarConsecutivosCentro=Boolean.TRUE;
		this.tipoConsecutivo="";
		this.postHistoricio=ConstantesBD.codigoNuncaValido;
		this.tmpNombreBDConsecutivo= "";
		this.anioSistema=ConstantesBD.codigoNuncaValido;
		this.anioTmpDto=ConstantesBD.codigoNuncaValido;
		
		
		this.abrirFacturacion=ConstantesBD.acronimoNo;
		this.abrirTesoria=ConstantesBD.acronimoNo;
		this.abrirConsecutivos=ConstantesBD.acronimoNo;
		this.codigoConsecutivoCentro="";
		this.esConsulta=Boolean.FALSE;
		this.setIdSePuedeModificar(Boolean.FALSE);
		this.existeAnioRegistrado=Boolean.FALSE;
		this.existeConsecutivoAnual= Boolean.FALSE;
		this.aplicaConsecutivoAnaul=Boolean.FALSE;
		
		this.abrirFacturaVaria = ConstantesBD.acronimoNo;
		this.prefFacturaVaria = "";
		this.resolucionFacturaVaria = "";
		this.rangoInicialFacturaVaria = null;
		this.rangoFinalFacturaVaria = null;

		this.setListaConsecutivosCentroFacturasVarias(new ArrayList<DtoConsecutivoCentroAtencion>());
		
		this.consecutivoFactura = false;
		
		this.consecutivoFacturaVaria = false;
		
		this.consecutivoModificado=null;
		
		this.entidadesSubcontratadas = new HashMap();
		this.codigoEntidadSubcontratada = "";
	}
	
	
	/**
	 * MANEJO DE PARAMETROS
	 */
	public void reset2(){
		
		this.manejaConsecutivoFactura=ConstantesBD.acronimoNo;
		this.manejaConsecutivoTesoreria=ConstantesBD.acronimoNo;
		this.manejaConsecutivoFacturaVariaPorCentoAtencion = ConstantesBD.acronimoNo;
		this.manejaConsecutivoNotasPacientesCentroAtencion = ConstantesBD.acronimoNo;
		this.setManejoEspecialInstitOdonto(ConstantesBD.acronimoNo);
	}
	
    
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        
        if(estado.equals("guardarNuevo"))
        {
            if( !UtilidadCadena.noEsVacio(this.getCodigo()) )
                errores.add("El código es requerido", new ActionMessage("errors.required", "El código"));
            
            if( !UtilidadCadena.noEsVacio(this.getDescripcion()) )
                errores.add("La descripción es requerida", new ActionMessage("errors.required", "La descripción"));
            
            if( !UtilidadCadena.noEsVacio(this.getPais()) )
                errores.add("El pais es requerido", new ActionMessage("errors.required", "El pais"));
            
            
            if( !UtilidadCadena.noEsVacio(this.getCiudad().getCodigo()))
                errores.add("La ciudad es requerida", new ActionMessage("errors.required", "La ciudad"));
            
            if(this.getManejoEspecialInstitOdonto().equals(ConstantesBD.acronimoSi))
            {	
            	if(this.getRegionCobertura().getCodigo() == ConstantesBD.codigoNuncaValido)
            		errores.add("La region es requerida", new ActionMessage("errors.required", "La region"));
            
	            if(this.getCategoriaAtencion().getCodigo() == ConstantesBD.codigoNuncaValido)
	            	errores.add("La categoria de atencion es requerida", new ActionMessage("errors.required", "La categoria de atencion"));
            }
            
            //si manejo multiempresa entonces debe ser requerida la empresa_insticion
            if( UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt())))
            {
            	if(UtilidadTexto.isEmpty(this.empresaInstitucion))
            		errores.add("La Empresa-Institución es requerida", new ActionMessage("errors.required", "La Empresa-Institución"));
            }
            
           
            /*
             * ACCION VALIDAR RANGOS DIAN
             */
            if(this.manejaConsecutivoFactura.equals(ConstantesBD.acronimoSi)){
            	 accionValidarRangos(errores, 0);
            }
            
            if(this.manejaConsecutivoFacturaVariaPorCentoAtencion.equals(ConstantesBD.acronimoSi)){
            	 accionValidarRangos(errores, 1);
            }

            if(!errores.isEmpty())
            	this.estado="ingresar";
            
        }            
        else if(estado.equals("guardarCambios"))
        {
            if( !UtilidadCadena.noEsVacio(this.getDescripcion()) )
                errores.add("La descripción es requerida", new ActionMessage("errors.required", "La descripción"));
           
            if( !UtilidadCadena.noEsVacio(this.getPais()) )
                errores.add("El pais es requerido", new ActionMessage("errors.required", "El pais"));
            
            
            if( !UtilidadCadena.noEsVacio(this.getCiudad().getCodigo()))
                errores.add("La ciudad es requerida", new ActionMessage("errors.required", "La ciudad"));
            
            if(this.getManejoEspecialInstitOdonto().equals(ConstantesBD.acronimoSi))
            {
	            if(this.getRegionCobertura().getCodigo() == ConstantesBD.codigoNuncaValido)
	            	errores.add("La region es requerida", new ActionMessage("errors.required", "La region"));
	            
	            if(this.getCategoriaAtencion().getCodigo() == ConstantesBD.codigoNuncaValido)
	            	errores.add("La categoria de atencion es requerida", new ActionMessage("errors.required", "La categoria de atencion"));
            }
            
            //si manejo multiempresa entonces debe ser requerida la empresa_insticion
            if( UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt())))
            {
            	if(UtilidadTexto.isEmpty(this.empresaInstitucion))
            		errores.add("La Empresa-Institución es requerida", new ActionMessage("errors.required", "La Empresa-Institución"));
            }
            
            /*
             * ACCION VALIDAR RANGOS DIAN
             */
            if(this.manejaConsecutivoFactura.equals(ConstantesBD.acronimoSi)){
            	accionValidarRangos(errores, 0);
	        }
	           
	        if(this.manejaConsecutivoFacturaVariaPorCentoAtencion.equals(ConstantesBD.acronimoSi)){
	           	accionValidarRangos(errores, 1);
	        }
 
            if(!errores.isEmpty())
            	this.estado="editar";
        }            

        return errores;
    }

	
	/**
	 * ACCION VALIDAR RANGOS
	 * @param errores
	 */
	private void accionValidarRangos(ActionErrors errores, int tipo) 
	{
		BigDecimal rangoInicialFactura = null;
		BigDecimal rangoFinalFactura = null;
		String mensajeFactura = "";
		 
		if(tipo == 0){
			
			rangoInicialFactura = this.rangoInicialFactura;
			rangoFinalFactura = this.rangoFinalFactura;
			mensajeFactura = "Factura";
		
		}else if (tipo == 1){
			
			rangoInicialFactura = this.rangoInicialFacturaVaria;
			rangoFinalFactura = this.rangoFinalFacturaVaria;
			mensajeFactura = "Factura Varia";
		}
			
		if(rangoInicialFactura!=null)
		{
			if( rangoInicialFactura.doubleValue()<=0)
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Rango Inicial "+mensajeFactura+" debe ser Mayor Que Cero "));
			}
		}

		if(rangoFinalFactura !=null)
		{
			if( rangoFinalFactura.doubleValue()<=0)
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Rango Final "+mensajeFactura+" debe ser Mayor Que Cero "));
			}
		}
		
		if( (rangoInicialFactura!=null) && (rangoFinalFactura !=null) )
		{
			if(rangoInicialFactura.doubleValue()>rangoFinalFactura.doubleValue())
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Rango Final "+mensajeFactura+" debe ser Mayor que el Rango Inicial"));
			}
		}

		if(rangoInicialFactura!=null)
		{
			if(rangoFinalFactura==null)
			{
				errores.add("", new ActionMessage("errors.notEspecific", " Es Requerido el Rango Final en "+mensajeFactura));
			}
		}
		
		if(rangoFinalFactura!=null)
		{
			if(rangoInicialFactura==null)
			{
				errores.add("", new ActionMessage("errors.notEspecific", " Es Requerido el Rango Inicial en "+mensajeFactura));
			}
		}
		
		if( (rangoInicialFactura!=null)  && (rangoFinalFactura!=null) ) 
		{
			if(rangoInicialFactura.doubleValue()==rangoFinalFactura.doubleValue())
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Rango Final "+mensajeFactura+" debe ser Mayor que el Rango Inicial"));
			}
		}
		
	}

	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getLogModificacion()
	{
		return this.logModificacion;
	}
	
	public void setLogModificacion(String logModificacion)
	{
		this.logModificacion=logModificacion;
	}
	
	public String getEstado()
	{
		return this.estado;
	}
	
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	public String getEstadoAnterior()
	{
		return this.estadoAnterior;
	}
	
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior=estadoAnterior;
	}
	
	public int getConsecutivo()
	{
		return this.consecutivo;
	}
	
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo=consecutivo;
	}
	
	public String getCodigo()
	{
		return this.codigo;
	}
	
	public void setCodigo(String codigo)
	{
		this.codigo=codigo;
	}
	
	public String getDescripcion()
	{
		return this.descripcion;
	}
	
	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}
	
	public String getActivo()
	{
		return this.activo;
	}
	
	public void setActivo(String activo)
	{
		this.activo=activo;
	}
	
	public String getMensaje()
	{
		return this.mensaje;
	}
	
	public void setMensaje(String mensaje)
	{
		this.mensaje=mensaje;
	}
	
	public String getColumna()
	{
	    return columna;
	}
	
	public void setColumna(String columna)
	{
	    this.columna = columna;
	}
	
	public Collection getCentrosAtencion()
	{
		return this.centrosAtencion;
	}
	
	public void setCentrosAtencion(Collection centrosAtencion)
	{
		this.centrosAtencion=centrosAtencion;
		this.ordenarCentrosAtencion();
	}
		
	public void ordenarCentrosAtencion()
    {	
		if(UtilidadCadena.noEsVacio(this.columna))   
        {
			if (this.columna.equals(this.ultimaColumnaOrdenada) && this.ordenar.equals(ConstantesBD.tipoOrdenamientoAscendente))
				this.ordenar=ConstantesBD.tipoOrdenamientoDescendente;
			else
				this.ordenar=ConstantesBD.tipoOrdenamientoAscendente;
			
			try 
			{
				this.centrosAtencion =(Collection) Listado.ordenarColumna((ArrayList)this.getCentrosAtencion(), this.getUltimaColumnaOrdenada(), this.getColumna());
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
            this.setUltimaColumnaOrdenada(this.columna);
        }
    }
	
		
	public String getUltimaColumnaOrdenada() {
		return ultimaColumnaOrdenada;
	}

	public void setUltimaColumnaOrdenada(String ultimaColumnaOrdenada) {
		this.ultimaColumnaOrdenada = ultimaColumnaOrdenada;
	}
	
	public String getCodUpgd() {
		return codUpgd;
	}

	public void setCodUpgd(String codUpgd) {
		this.codUpgd = codUpgd;
	}

	public HashMap getCmbUpgd() {
		return cmbUpgd;
	}

	public void setCmbUpgd(HashMap cmbUpgd) {
		this.cmbUpgd = cmbUpgd;
	}

	public Object getCmbUpgd(String key) {
		return cmbUpgd.get(key);
	}

	public void setCmbUpgd(String key,Object obj) {
		this.cmbUpgd.put(key,obj);
	}
	
	public int getNumCmbUpgd() {
		return numCmbUpgd;
	}

	public void setNumCmbUpgd(int numCmbUpgd) {
		this.numCmbUpgd = numCmbUpgd;
	}

	public String getNomUpgd() {
		return nomUpgd;
	}

	public void setNomUpgd(String nomUpgd) {
		this.nomUpgd = nomUpgd;
	}

	/**
	 * @return the cmb_inst_sirc
	 */
	public HashMap getCmb_inst_sirc() {
		return cmb_inst_sirc;
	}

	/**
	 * @param cmb_inst_sirc the cmb_inst_sirc to set
	 */
	public void setCmb_inst_sirc(HashMap cmb_inst_sirc) {
		this.cmb_inst_sirc = cmb_inst_sirc;
	}
	
	/**
	 * @return the institucionesSIRC
	 */
	public Object getCmb_inst_sirc(String key) {
		return cmb_inst_sirc.get(key);
	}

	/**
	 * @param institucionesSIRC the institucionesSIRC to set
	 */
	public void setCmb_inst_sirc(String key, Object value) {
		this.cmb_inst_sirc.put(key, value);
	}



	/**
	 * @return the institucionesSIRC
	 */
	public HashMap getInstitucionesSIRC() {
		return institucionesSIRC;
	}

	/**
	 * @param institucionesSIRC the institucionesSIRC to set
	 */
	public void setInstitucionesSIRC(HashMap institucionesSIRC) {
		this.institucionesSIRC = institucionesSIRC;
	}	
	
	/**
	 * @return the num_inst_sirc
	 */
	public int getNum_inst_sirc() {
		return num_inst_sirc;
	}

	/**
	 * @param num_inst_sirc the num_inst_sirc to set
	 */
	public void setNum_inst_sirc(int num_inst_sirc) {
		this.num_inst_sirc = num_inst_sirc;
	}

	/**
	 * @return the nomInstitucionesSIRC
	 */
	public String getNomInstitucionesSIRC() {
		return nomInstitucionesSIRC;
	}

	/**
	 * @param nomInstitucionesSIRC the nomInstitucionesSIRC to set
	 */
	public void setNomInstitucionesSIRC(String nomInstitucionesSIRC) {
		this.nomInstitucionesSIRC = nomInstitucionesSIRC;
	}

	/**
	 * @return the codigoInstSirc
	 */
	public String getCodigoInstSirc() {
		return codigoInstSirc;
	}

	/**
	 * @param codigoInstSirc the codigoInstSirc to set
	 */
	public void setCodigoInstSirc(String codigoInstSirc) {
		this.codigoInstSirc = codigoInstSirc;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the empresasInstitucionMap
	 */
	public HashMap getEmpresasInstitucionMap() {
		return empresasInstitucionMap;
	}

	/**
	 * @param empresasInstitucionMap the empresasInstitucionMap to set
	 */
	public void setEmpresasInstitucionMap(HashMap empresasInstitucionMap) {
		this.empresasInstitucionMap = empresasInstitucionMap;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the ordenar
	 */
	public String getOrdenar() {
		return ordenar;
	}

	/**
	 * @param ordenar the ordenar to set
	 */
	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		
		 return this.departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	
	/**
	 * @return the arrayRegiones
	 */
	public ArrayList<DtoRegionesCobertura> getArrayRegiones() {
		return arrayRegiones;
	}

	/**
	 * @param arrayRegiones the arrayRegiones to set
	 */
	public void setArrayRegiones(ArrayList<DtoRegionesCobertura> arrayRegiones) {
		this.arrayRegiones = arrayRegiones;
	}

	/**
	 * @return the arrayCategorias
	 */
	public ArrayList<DtoCategoriaAtencion> getArrayCategorias() {
		return arrayCategorias;
	}

	/**
	 * @param arrayCategorias the arrayCategorias to set
	 */
	public void setArrayCategorias(ArrayList<DtoCategoriaAtencion> arrayCategorias) {
		this.arrayCategorias = arrayCategorias;
	}

	/**
	 * @return the regionCobertura
	 */
	public DtoRegionesCobertura getRegionCobertura() {
		return regionCobertura;
	}

	/**
	 * @param regionCobertura the regionCobertura to set
	 */
	public void setRegionCobertura(DtoRegionesCobertura regionCobertura) {
		this.regionCobertura = regionCobertura;
	}

	/**
	 * @return the categoriaAtencion
	 */
	public DtoCategoriaAtencion getCategoriaAtencion() {
		return categoriaAtencion;
	}

	/**
	 * @param categoriaAtencion the categoriaAtencion to set
	 */
	public void setCategoriaAtencion(DtoCategoriaAtencion categoriaAtencion) {
		this.categoriaAtencion = categoriaAtencion;
	}

	/**
	 * @return the ciudad
	 */
	public InfoDatosStr getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(InfoDatosStr ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the ciudadFormato
	 */
	public String getCiudadFormato() {
		 return this.getDepartamento()+""+ConstantesBD.separadorSplit+""+getCiudad().getCodigo();
	}

	/**
	 * @param ciudadFormato the ciudadFormato to set
	 */
	public void setCiudadFormato(String ciudadFormato) {
		this.ciudadFormato = ciudadFormato;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public String getPiePaginaPresupuestoOdon() {
		return piePaginaPresupuestoOdon;
	}

	public void setPiePaginaPresupuestoOdon(String piePaginaPresupuestoOdon) {
		this.piePaginaPresupuestoOdon = piePaginaPresupuestoOdon;
	}

	public void setDtoConsecutivo(DtoConsecutivoCentroAtencion dtoConsecutivo) {
		this.dtoConsecutivo = dtoConsecutivo;
	}

	public DtoConsecutivoCentroAtencion getDtoConsecutivo() {
		return dtoConsecutivo;
	}
	
	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public String getPrefFactura() {
		return prefFactura;
	}

	public void setPrefFactura(String prefFactura) {
		this.prefFactura = prefFactura;
	}

	public BigDecimal getRangoInicialFactura() {
		return rangoInicialFactura;
	}

	public void setRangoInicialFactura(BigDecimal rangoInicialFactura) {
		this.rangoInicialFactura = rangoInicialFactura;
	}

	public void setRangoFinalFactura(BigDecimal rangoFinalFactura) {
		this.rangoFinalFactura = rangoFinalFactura;
	}

	public BigDecimal getRangoFinalFactura() {
		return rangoFinalFactura;
	}

	public void setListaConsecutivosCentroFacturacion(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturacion) {
		this.listaConsecutivosCentroFacturacion = listaConsecutivosCentroFacturacion;
	}

	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroFacturacion() {
		return listaConsecutivosCentroFacturacion;
	}

	public void setListaConsecutivosCentroTesoreria(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroTesoreria) {
		this.listaConsecutivosCentroTesoreria = listaConsecutivosCentroTesoreria;
	}

	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroTesoreria() {
		return listaConsecutivosCentroTesoreria;
	}

	public void setCodigoConsecutivo(BigDecimal codigoConsecutivo) {
		this.codigoConsecutivo = codigoConsecutivo;
	}

	public BigDecimal getCodigoConsecutivo() {
		return codigoConsecutivo;
	}

	public void setListaHistoricos(ArrayList<DtoConsecutivoCentroAtencion> listaHistoricos) {
		this.listaHistoricos = listaHistoricos;
	}

	public ArrayList<DtoConsecutivoCentroAtencion> getListaHistoricos() {
		return listaHistoricos;
	}

	public void setPostConsecutivo(int postConsecutivo) {
		this.postConsecutivo = postConsecutivo;
	}

	public int getPostConsecutivo() {
		return postConsecutivo;
	}

	public void setTamanoListaHistorico(int tamanoListaHistorico) {
		this.tamanoListaHistorico = tamanoListaHistorico;
	}

	public int getTamanoListaHistorico() {
		tamanoListaHistorico= this.getListaHistoricos().size();
		return tamanoListaHistorico;
	}

	public void setCargarConsecutivosCentro(boolean cargarConsecutivosCentro) {
		this.cargarConsecutivosCentro = cargarConsecutivosCentro;
	}

	public boolean isCargarConsecutivosCentro() {
		return cargarConsecutivosCentro;
	}

	public void setTipoConsecutivo(String tipoConsecutivo) {
		this.tipoConsecutivo = tipoConsecutivo;
	}

	public String getTipoConsecutivo() {
		return tipoConsecutivo;
	}

	public void setPostHistoricio(int postHistoricio) {
		this.postHistoricio = postHistoricio;
	}

	public int getPostHistoricio() {
		return postHistoricio;
	}

	public void setTmpNombreBDConsecutivo(String tmpNombreBDConsecutivo) {
		this.tmpNombreBDConsecutivo = tmpNombreBDConsecutivo;
	}

	public String getTmpNombreBDConsecutivo() {
		return tmpNombreBDConsecutivo;
	}

	public int getAnioSistema() {
		return anioSistema;
	}

	public void setAnioSistema(int anioSistema) {
		this.anioSistema = anioSistema;
	}

	public int getAnioTmpDto() {
		return anioTmpDto;
	}

	public void setAnioTmpDto(int anioTmpDto) {
		this.anioTmpDto = anioTmpDto;
	}

	public void setAbrirFacturacion(String abrirFacturacion) {
		this.abrirFacturacion = abrirFacturacion;
	}

	public String getAbrirFacturacion() {
		return abrirFacturacion;
	}

	public void setAbrirTesoria(String abrirTesoria) {
		this.abrirTesoria = abrirTesoria;
	}

	public String getAbrirTesoria() {
		return abrirTesoria;
	}

	public void setCodigoConsecutivoCentro(String codigoConsecutivoCentro) {
		this.codigoConsecutivoCentro = codigoConsecutivoCentro;
	}

	public String getCodigoConsecutivoCentro() {
		return codigoConsecutivoCentro;
	}

	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	public boolean isEsConsulta() {
		return esConsulta;
	}

	public void setIdSePuedeModificar(boolean idSePuedeModificar) {
		this.idSePuedeModificar = idSePuedeModificar;
	}

	public boolean isIdSePuedeModificar() {
		return idSePuedeModificar;
	}

	public void setExisteAnioRegistrado(boolean existeAnioRegistrado) {
		this.existeAnioRegistrado = existeAnioRegistrado;
	}

	public boolean isExisteAnioRegistrado() {
		return existeAnioRegistrado;
	}

	public String getManejaConsecutivoFactura() {
		return manejaConsecutivoFactura;
	}

	public void setManejaConsecutivoFactura(String manejaConsecutivoFactura) {
		this.manejaConsecutivoFactura = manejaConsecutivoFactura;
	}

	public String getManejaConsecutivoTesoreria() {
		return manejaConsecutivoTesoreria;
	}

	public void setManejaConsecutivoTesoreria(String manejaConsecutivoTesoreria) {
		this.manejaConsecutivoTesoreria = manejaConsecutivoTesoreria;
	}

	public void setIdConsecutivoAnual(String idConsecutivoAnual) {
		this.idConsecutivoAnual = idConsecutivoAnual;
	}

	public String getIdConsecutivoAnual() {
		return idConsecutivoAnual;
	}

	public void setExisteConsecutivoAnual(boolean existeConsecutivoAnual) {
		this.existeConsecutivoAnual = existeConsecutivoAnual;
	}

	public boolean isExisteConsecutivoAnual() {
		return existeConsecutivoAnual;
	}

	public void setAplicaConsecutivoAnaul(boolean aplicaConsecutivoAnaul) {
		this.aplicaConsecutivoAnaul = aplicaConsecutivoAnaul;
	}

	public boolean isAplicaConsecutivoAnaul() {
		return aplicaConsecutivoAnaul;
	}

	public String getAbrirConsecutivos() {
		return abrirConsecutivos;
	}

	public void setAbrirConsecutivos(String abrirConsecutivos) {
		this.abrirConsecutivos = abrirConsecutivos;
	}

	/**
	 * @param manejaConsecutivoFacturaVariaPorCentoAtencion the manejaConsecutivoFacturaVariaPorCentoAtencion to set
	 */
	public void setManejaConsecutivoFacturaVariaPorCentoAtencion(
			String manejaConsecutivoFacturaVariaPorCentoAtencion) {
		this.manejaConsecutivoFacturaVariaPorCentoAtencion = manejaConsecutivoFacturaVariaPorCentoAtencion;
	}

	/**
	 * @return the manejaConsecutivoFacturaVariaPorCentoAtencion
	 */
	public String getManejaConsecutivoFacturaVariaPorCentoAtencion() {
		return manejaConsecutivoFacturaVariaPorCentoAtencion;
	}

	/**
	 * @return the abrirFacturaVaria
	 */
	public String getAbrirFacturaVaria() {
		return abrirFacturaVaria;
	}

	/**
	 * @param abrirFacturaVaria the abrirFacturaVaria to set
	 */
	public void setAbrirFacturaVaria(String abrirFacturaVaria) {
		this.abrirFacturaVaria = abrirFacturaVaria;
	}

	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}

	/**
	 * @param resolucionFacturaVaria the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}

	/**
	 * @return the prefFacturaVaria
	 */
	public String getPrefFacturaVaria() {
		return prefFacturaVaria;
	}

	/**
	 * @param prefFacturaVaria the prefFacturaVaria to set
	 */
	public void setPrefFacturaVaria(String prefFacturaVaria) {
		this.prefFacturaVaria = prefFacturaVaria;
	}

	/**
	 * @return the rangoInicialFacturaVaria
	 */
	public BigDecimal getRangoInicialFacturaVaria() {
		return rangoInicialFacturaVaria;
	}

	/**
	 * @param rangoInicialFacturaVaria the rangoInicialFacturaVaria to set
	 */
	public void setRangoInicialFacturaVaria(BigDecimal rangoInicialFacturaVaria) {
		this.rangoInicialFacturaVaria = rangoInicialFacturaVaria;
	}

	/**
	 * @return the rangoFinalFacturaVaria
	 */
	public BigDecimal getRangoFinalFacturaVaria() {
		return rangoFinalFacturaVaria;
	}

	/**
	 * @param rangoFinalFacturaVaria the rangoFinalFacturaVaria to set
	 */
	public void setRangoFinalFacturaVaria(BigDecimal rangoFinalFacturaVaria) {
		this.rangoFinalFacturaVaria = rangoFinalFacturaVaria;
	}


	/**
	 * @param listaConsecutivosCentroFacturasVarias the listaConsecutivosCentroFacturasVarias to set
	 */
	public void setListaConsecutivosCentroFacturasVarias(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturasVarias) {
		this.listaConsecutivosCentroFacturasVarias = listaConsecutivosCentroFacturasVarias;
	}


	/**
	 * @return the listaConsecutivosCentroFacturasVarias
	 */
	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroFacturasVarias() {
		return listaConsecutivosCentroFacturasVarias;
	}


	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(boolean consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}


	/**
	 * @return the consecutivoFactura
	 */
	public boolean isConsecutivoFactura() {
		return consecutivoFactura;
	}


	/**
	 * @param consecutivoFacturaVaria the consecutivoFacturaVaria to set
	 */
	public void setConsecutivoFacturaVaria(boolean consecutivoFacturaVaria) {
		this.consecutivoFacturaVaria = consecutivoFacturaVaria;
	}


	/**
	 * @return the consecutivoFacturaVaria
	 */
	public boolean isConsecutivoFacturaVaria() {
		return consecutivoFacturaVaria;
	}


	/**
	 * Obtiene el valor del atributo consecutivoModificado
	 *
	 * @return Retorna atributo consecutivoModificado
	 */
	public BigDecimal getConsecutivoModificado()
	{
		return consecutivoModificado;
	}


	/**
	 * Establece el valor del atributo consecutivoModificado
	 *
	 * @param valor para el atributo consecutivoModificado
	 */
	public void setConsecutivoModificado(BigDecimal consecutivoModificado)
	{
		this.consecutivoModificado = consecutivoModificado;
	}


	public void setManejoEspecialInstitOdonto(String manejoEspecialInstitOdonto) {
		this.manejoEspecialInstitOdonto = manejoEspecialInstitOdonto;
	}


	public String getManejoEspecialInstitOdonto() {
		return manejoEspecialInstitOdonto;
	}
	/**
	 * @return the entidadesSubcontratadas
	 */
	public HashMap getEntidadesSubcontratadas() {
		return entidadesSubcontratadas;
	}


	/**
	 * @param entidadesSubcontratadas the entidadesSubcontratadas to set
	 */
	public void setEntidadesSubcontratadas(HashMap entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}


	/**
	 * @return the codigoEntidadSubcontratada
	 */
	public String getCodigoEntidadSubcontratada() {
		return codigoEntidadSubcontratada;
	}


	/**
	 * @param codigoEntidadSubcontratada the codigoEntidadSubcontratada to set
	 */
	public void setCodigoEntidadSubcontratada(String codigoEntidadSubcontratada) {
		this.codigoEntidadSubcontratada = codigoEntidadSubcontratada;
	}



		public String getManejaConsecutivoNotasPacientesCentroAtencion() {
		return manejaConsecutivoNotasPacientesCentroAtencion;
	}


	public void setManejaConsecutivoNotasPacientesCentroAtencion(
			String manejaConsecutivoNotasPacientesCentroAtencion) {
		this.manejaConsecutivoNotasPacientesCentroAtencion = manejaConsecutivoNotasPacientesCentroAtencion;
	}


	/**
	 * Método que determina si se debe o no mostrar la sección
	 * de consecutivos de Tesorería
	 * 
	 * @return
	 */
	public boolean isMostrarSeccionConsecutivoTesoreria (){
	
		return (UtilidadTexto.getBoolean(this.getManejaConsecutivoTesoreria()) || 
				UtilidadTexto.getBoolean(this.getManejaConsecutivoNotasPacientesCentroAtencion()));
	}

}
