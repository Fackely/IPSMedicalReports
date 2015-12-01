package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.facturacion.ConsultaTarifasArticulosForm;
import com.princetonsa.actionform.facturacion.ConsultaTarifasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsultaTarifasDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.servinte.axioma.fwk.exception.IPSException;

public class ConsultaTarifas
{
	/*
	 * Logger de la clase ConsultaTarifasAction
	 */
	public static Logger logger = Logger.getLogger(ConsultaTarifas.class);
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaTarifasDao objetoDao;
	
	
	private HashMap consultaTarifasMap;
	
	private String tiposTarifarios;
	
	private String esquemasTarifarios;
	
	
	//******** Busqueda Servicios
	
	private String codigo;
	
	private String descripcion;
	
	private String especialidad;
	
	private String tipoServicio;
	
	private String grupo;
	
	
	//******** Busqueda Articulos
	
	private String codigoArticulo;
	
	private String descripcionArticulo;
	
	private String clase;
	
	private String grupoArticulo;
	
	private String subgrupo;
	
	private String naturaleza;
	
	
	
//************** Consulta Detalle Servicios
	
	private String fechaAsignacion;
	private String tipoLiquidacion;
	private String valorTarifa;
	private String usuarioAsigna;
	private String tipoCambios;
	private String esquemaTarifario;
	private String tipoRedondeo;
	private String servicio;
	private String liquidarAsocios;
	
	private HashMap consultaServiciosMap;
	
	
	//************* Consulta Detalle Articulo
	
	private String fecha;
	private String tipoTarifa;
	private String porcentaje;
	private String valor;
	private String usuario;
	private String cambio;
	private String esquema;
	private String articulo;
	private String redondeo;
	
	private HashMap consultaArticulosMap;
	
	
	
	
	
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getConsultaTarifasDao();
			if(objetoDao!=null)
				return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * Constructor de la clase
	 */
	public ConsultaTarifas()
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	
	/**
	 * 
	 *
	 */
	private void reset()
	{
		consultaTarifasMap=new HashMap();
		consultaTarifasMap.put("numRegistros", "0");
		
		consultaServiciosMap=new HashMap();
		consultaServiciosMap.put("numRegistros", "0");
		
		consultaArticulosMap=new HashMap();
		consultaArticulosMap.put("numRegistros", "0");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param buscarEstado
	 * @return
	 * @throws IPSException 
	 */
	public HashMap buscar(Connection con, boolean buscarEstado,int codigoEsquemaTarifario) throws IPSException
	{
		return objetoDao.buscar(con,getCodigo(),getDescripcion(),getEspecialidad(),getTipoServicio(),getGrupo(),codigoEsquemaTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param buscarEstado
	 * @param esquemaTarifario 
	 * @return
	 * @throws IPSException 
	 */
	public HashMap buscarArticulo(Connection con, boolean buscarEstado, int esquemaTarifario) throws IPSException
	{
		return objetoDao.buscarArticulo(con,getCodigoArticulo(),getDescripcionArticulo(),getClase(),getGrupoArticulo() ,getSubgrupo(), getNaturaleza(),esquemaTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario 
	 * @param buscarEstado
	 * @return
	 */
	public HashMap consultaServicio(Connection con, int codigo,int tarifarioOficial, int esquemaTarifario) 
	{
		
		HashMap vo=new HashMap();
		vo.put("codigoServicio", codigo+"");
		vo.put("tarifarioOficial", tarifarioOficial);
		vo.put("esquematarifario",esquemaTarifario);
		return (HashMap)objetoDao.consultaServicio(con,vo).clone();
		
	}
	
	
	/*public void consultaServicio(Connection con, HashMap vo) 
	{
		this.consultaServiciosMap=objetoDao.consultaServicio(con,vo);
	}*/
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */	
	public HashMap consultaArticulo(Connection con, int codigo)
	{
		
		HashMap vo=new HashMap();
		vo.put("codigoArti", codigo+"");
		return (HashMap)objetoDao.consultaArticulo(con,vo).clone();		
	}
	

	/**
	 * 
	 * @return
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * 
	 * @param clase
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @param codigo
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * 
	 * @param codigoArticulo
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getConsultaTarifasMap() {
		return consultaTarifasMap;
	}

	/**
	 * 
	 * @param consultaTarifasMap
	 */
	public void setConsultaTarifasMap(HashMap consultaTarifasMap) {
		this.consultaTarifasMap = consultaTarifasMap;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * 
	 * @param descripcionArticulo
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * 
	 * @param especialidad
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquemasTarifarios() {
		return esquemasTarifarios;
	}

	/**
	 * 
	 * @param esquemasTarifarios
	 */
	public void setEsquemasTarifarios(String esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	/**
	 * 
	 * @return
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * 
	 * @param grupo
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getGrupoArticulo() {
		return grupoArticulo;
	}

	/**
	 * 
	 * @param grupoArticulo
	 */
	public void setGrupoArticulo(String grupoArticulo) {
		this.grupoArticulo = grupoArticulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getNaturaleza() {
		return naturaleza;
	}

	/**
	 * 
	 * @param naturaleza
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	/**
	 * 
	 * @return
	 */
	public String getSubgrupo() {
		return subgrupo;
	}

	/**
	 * 
	 * @param subgrupo
	 */
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * 
	 * @param tipoServicio
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getTiposTarifarios() {
		return tiposTarifarios;
	}

	/**
	 * 
	 * @param tiposTarifarios
	 */
	public void setTiposTarifarios(String tiposTarifarios) {
		this.tiposTarifarios = tiposTarifarios;
	}

	/**
	 * 
	 * @return
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * 
	 * @param articulo
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCambio() {
		return cambio;
	}

	/**
	 * 
	 * @param cambio
	 */
	public void setCambio(String cambio) {
		this.cambio = cambio;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquema() {
		return esquema;
	}

	/**
	 * 
	 * @param esquema
	 */
	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * 
	 * @param esquemaTarifario
	 */
	public void setEsquemaTarifario(String esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * 
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * 
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaAsignacion() {
		return fechaAsignacion;
	}

	/**
	 * 
	 * @param fechaAsignacion
	 */
	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	/**
	 * 
	 * @return
	 */
	public ConsultaTarifasDao getObjetoDao() {
		return objetoDao;
	}

	/**
	 * 
	 * @param objetoDao
	 */
	public void setObjetoDao(ConsultaTarifasDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	/**
	 * 
	 * @return
	 */
	public String getPorcentaje() {
		return porcentaje;
	}

	/**
	 * 
	 * @param porcentaje
	 */
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getRedondeo() {
		return redondeo;
	}

	/**
	 * 
	 * @param redondeo
	 */
	public void setRedondeo(String redondeo) {
		this.redondeo = redondeo;
	}

	/**
	 * 
	 * @return
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * 
	 * @param servicio
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoCambios() {
		return tipoCambios;
	}

	/**
	 * 
	 */
	public void setTipoCambios(String tipoCambios) {
		this.tipoCambios = tipoCambios;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoLiquidacion() {
		return tipoLiquidacion;
	}

	/**
	 * 
	 * @param tipoLiquidacion
	 */
	public void setTipoLiquidacion(String tipoLiquidacion) {
		this.tipoLiquidacion = tipoLiquidacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoRedondeo() {
		return tipoRedondeo;
	}

	/**
	 * 
	 * @param tipoRedondeo
	 */
	public void setTipoRedondeo(String tipoRedondeo) {
		this.tipoRedondeo = tipoRedondeo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}

	/**
	 * 
	 * @param tipoTarifa
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 */
	public String getUsuarioAsigna() {
		return usuarioAsigna;
	}

	/**
	 * 
	 * @param usuarioAsigna
	 */
	public void setUsuarioAsigna(String usuarioAsigna) {
		this.usuarioAsigna = usuarioAsigna;
	}

	/**
	 * 
	 * @return
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * 
	 * @return
	 */
	public String getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * 
	 * @param valorTarifa
	 */
	public void setValorTarifa(String valorTarifa) {
		this.valorTarifa = valorTarifa;
	}


	public HashMap getConsultaArticulosMap() {
		return consultaArticulosMap;
	}


	public void setConsultaArticulosMap(HashMap consultaArticulosMap) {
		this.consultaArticulosMap = consultaArticulosMap;
	}


	public HashMap getConsultaServiciosMap() {
		return consultaServiciosMap;
	}


	public void setConsultaServiciosMap(HashMap consultaServiciosMap) {
		this.consultaServiciosMap = consultaServiciosMap;
	}


	/**
	 * @return the liquidarAsocios
	 */
	public String getLiquidarAsocios() {
		return liquidarAsocios;
	}


	/**
	 * @param liquidarAsocios the liquidarAsocios to set
	 */
	public void setLiquidarAsocios(String liquidarAsocios) {
		this.liquidarAsocios = liquidarAsocios;
	}


	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param esServicio
	 * @return
	 */
	public HashMap<String,Object> consultarEsquemasVigenciasContrato(Connection con, int contrato, boolean esServicio) 
	{
		return objetoDao.consultarEsquemasVigenciasContrato(con,contrato,esServicio);
	}


	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tarifarioOficial
	 * @param esquemaTarifario 
	 * @param grupoServicio 
	 * @return
	 * @throws IPSException 
	 */
	public HashMap consultaServicioTarifaFinal(Connection con, int codigoServicio, int tarifarioOficial,int esquemaTarifario,int convenio,int contrato,int institucion,String fechaVigencia, int grupoServicio) throws IPSException 
	{
		return objetoDao.consultaServicioTarifaFinal(con,codigoServicio,tarifarioOficial,esquemaTarifario,convenio,contrato,institucion,fechaVigencia,grupoServicio);
	}


	/**
	 * 
	 * @param con
	 * @param codigoArti
	 * @param esquemaTarifario
	 * @param convenio
	 * @param contrato
	 * @param institucion
	 * @param fechaVigencia
	 * @param claseInventario 
	 * @return
	 * @throws IPSException 
	 */
	public HashMap consultaArticulosTarifaFinal(Connection con, int codigoArti, int esquemaTarifario, int convenio, int contrato, int institucion, String fechaVigencia, int claseInventario) throws IPSException 
	{
		return objetoDao.consultaArticulosTarifaFinal(con,codigoArti,esquemaTarifario,convenio,contrato,institucion,fechaVigencia,claseInventario);
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	/*
	 * public static ActionForward generar (Connection con, ConsultaTarifasForm forma,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) throws SQLException
	{
		return generarReporte(con, usuario, forma, request, mapping);
		/*
		else
			//mapping.findForward("principal");
			return archivoPlano(connection, forma, usuario, institucion, request, mapping);
			
	}
	 */
	
	
	/**
	 * Metodo encargado de generar reporte en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @throws SQLException 
	 */
	/*
	public static ActionForward generarReporte (Connection con,UsuarioBasico usuario,ConsultaTarifasForm forma, HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		System.gc();
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		String forward="";
		DataSet="consultaCambiosTarifas";
		reporte="ConsultaCambiosTarifas.rptdesign";
		forward="detalle";

		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, con, usuario, forma, request);
		
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		
		comp.obtenerComponentesDataSet(DataSet);
		String newQuery="";
		newQuery = comp.obtenerQueryDataSet().replace("?", forma.getArticulo());
		/*
		 * if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
			//Modificamos el DataSet con las validaciones comunes para todos
			newQuery = comp.obtenerQueryDataSet().replace("?", forma.getArticulo());
		else
			//Se modifica el query
			newQuery=obtenerConsulta(organizarCriterios(con, forma, usuario));
		 *//*
		
		comp.modificarQueryDataSet(newQuery);
		
		logger.info("");
                	    
		logger.info("=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+comp.obtenerQueryDataSet());
    
           
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
    	UtilidadBD.cerrarConexion(con);
        return mapping.findForward(forward);
	
	}
	
	private static String obtenerConsulta(String organizarCriterios) {
		
		return null;
	}

*/
	/**
	 * Metodo encargado de organizar el encabezado 
	 * para el birt
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ConsultaTarifasForm forma, HttpServletRequest request)
	{
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(connection,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Título del reporte
        comp.insertLabelInGridPpalOfHeader(1,1, "CONSULTA CAMBIOS TARIFAS");
        //String criterios = organizarCriterios(connection, forma, usuario); 
        //comp.insertLabelInGridPpalOfHeader(3, 0, criterios);
        //Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	/**
	 * Método que organiza los criterios
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	
	public static String organizarCriterios (Connection connection, ConsultaTarifasArticulosForm forma,UsuarioBasico usuario)
	{
		 String criterios="";
         
	        //esquema tarifario
	        if (UtilidadCadena.noEsVacio(forma.getEsqTar()) && !forma.getEsqTar().equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+="Esquema Tarifario: "+Utilidades.getNombreEsquemaTarifario(connection, Utilidades.convertirAEntero(forma.getEsqTar()));
	    
	        //Codigo Articulo
	        if (UtilidadCadena.noEsVacio(forma.getCodigoArticulo())) 
	        	criterios+="  Codigo Articulo: "+forma.getCodigoArticulo();
	      
	        //Descripcion
	        if (UtilidadCadena.noEsVacio(forma.getDescripcionArticulo()))
	        	criterios+="  Descripcion: "+forma.getDescripcionArticulo();
	        	

	        //clase inventario
	        if (UtilidadCadena.noEsVacio(forma.getClase()) && !forma.getClase().equals("0"))
	        {
	        	HashMap tmp = new HashMap ();
	        	tmp.put("codigo", forma.getClase());
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	criterios+=" Clase Inventario: "+UtilidadInventarios.obtenerClaseInventario(connection, tmp).get(0).get("nombre");
	        }
	        
	        //grupo
	        if (UtilidadCadena.noEsVacio(forma.getGrupo()) && !forma.getGrupo().equals("0"))
	        	criterios+="  Grupo: "+Utilidades.getNombreGrupoServicios(connection, Utilidades.convertirAEntero(forma.getGrupo()));
	        
	        //sub grupo
	        if (UtilidadCadena.noEsVacio(forma.getSubgrupo()) && !forma.getSubgrupo().equals("0"))
	        {
	        	HashMap tmp = new HashMap();
	        	
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	tmp.put("codigo", forma.getSubgrupo());
	        	criterios+="  Sub Grupo: "+Utilidades.obtenerSubGrupo(connection, tmp).get(0).get("nombre");
	        }
	       
	        //naturaleza
	        if (UtilidadCadena.noEsVacio(forma.getNaturaleza()) && !forma.getNaturaleza().equals("selected"))
	        	criterios+="  Naturaleza: "+Utilidades.obtenerNombreNaturalezasArticulo(connection, forma.getNaturaleza());
	        
	    	
	     	//Codigo tipo articulo
	        if (UtilidadCadena.noEsVacio(forma.getTipoCod()) && !forma.getTipoCod().equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+=" Codigo tipo Artiulo: "+ValoresPorDefecto.getIntegridadDominio(forma.getTipoCod());        
	        
	        return criterios;
	}


	
	

}
