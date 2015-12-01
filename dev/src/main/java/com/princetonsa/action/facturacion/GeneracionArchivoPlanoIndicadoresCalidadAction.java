package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.facturacion.GeneracionArchivoPlanoIndicadoresCalidadForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.GeneracionArchivoPlanoIndicadoresCalidad;


/**
 * Clase para el manejo del workflow de GeneracionArchivoPlanoIndicadoresCalidadAction
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */
public class GeneracionArchivoPlanoIndicadoresCalidadAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(GeneracionArchivoPlanoIndicadoresCalidadAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof GeneracionArchivoPlanoIndicadoresCalidadForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			GeneracionArchivoPlanoIndicadoresCalidadForm forma = (GeneracionArchivoPlanoIndicadoresCalidadForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO GENERACION ARCHIVO PLANO INDICADORES CALIDAD ---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario);    			
    			}	
    			else
    				if(estado.equals("generar"))
    				{
    					return this.accionGenerar(forma, con, mapping, usuario);
    				}
    		}
    	}catch (Exception e) {
    		Log4JManager.error(e);
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    	return null;
            						}
    
    
    
    /**
     * 
     * @param mapa
     * @param m
     * @param forma
     * @return
     */
    private boolean buscarInconsistencias(Connection con, HashMap mapa, HashMap m, GeneracionArchivoPlanoIndicadoresCalidadForm forma)
    {
    	String in="";
    	boolean inc=true;
    	
    	if(!mapa.containsKey("digito_verificacion_0") || mapa.get("digito_verificacion_0").toString().equals("") || mapa.get("digito_verificacion_0").toString().equals(""+ConstantesBD.codigoNuncaValido))
    	{
    		//generarLogInconsistencia("Todos los Indicadores "," Institucion digito de Verificacion ", " Es Requerido",m, forma,mapa);
    		in+="Todos los Indicadores          "+"Institucion digito de Verificacion		"+"Es Requerido \n";
    		inc=false;
    	}
    	if(!mapa.containsKey("cod_min_salud_0") || mapa.get("cod_min_salud_0").toString().equals(""))
    	{
    		//generarLogInconsistencia("Todos los Indicadores "," Codigo Entidad ", " Es Requerido",m, forma,mapa);
    		in+="Todos los Indicadores          "+"Codigo Entidad          		"+"Es Requerido \n";
    		inc=false;
    	}
    	if(mapa.containsKey("nit_0") && mapa.get("nit_0").toString().length()>16)
    	{
    		//generarLogInconsistencia("Todos los Indicadores "," NIT ", " El tamaño es superior al permitido (16) ",m, forma,mapa);
    		in+="Todos los Indicadores          "+"NIT          					"+"El tamaño es superior al permitido (16) \n";
    		inc=false;
    	}
    	if(forma.getFiltro().containsKey("ano") && forma.getFiltro().get("ano").toString().length()>4)
    	{
    		//generarLogInconsistencia("Todos los Indicadores "," Año ", " El tamaño es superior al permitido (4) ",m, forma,mapa);
    		in+="Todos los Indicadores          "+"Año         					 "+"El tamaño es superior al permitido (4) \n";
    		inc=false;
    	}
    	if(mapa.containsKey("cod_min_salud_0") && mapa.get("cod_min_salud_0").toString().length()>20)
    	{
    		//generarLogInconsistencia("Todos los Indicadores "," Código Entidad ", " El tamaño es superior al permitido (20) ",m, forma,mapa);
    		in+="Todos los Indicadores          "+"Código Entidad          		"+"El tamaño es superior al permitido (20) \n";
    		inc=false;
    	}


    	
    	String [] indicadores={
    							ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaGeneral,
    							ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaInterna,
    							ConstantesBD.acronimoOportunidadAsignacioncitasConsultaGinecobstetricia,
    							ConstantesBD.acronimoOportunidadAsignacioncitasConsultaPediatria,
    							ConstantesBD.acronimoOportunidadAsignacioncitasConsultaCirugiaGeneral,
    							ConstantesBD.acronimoOportunidadAtencionServicioImagenologia,
    							ConstantesBD.acronimoOportunidadAtencionConsultaOdontologiaGeneral,
    							ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada,
    							ConstantesBD.acronimoTasaInfeccionIntrahospitalaria,
    							};
    	
    	for(int i=0;i<indicadores.length;i++)
    	{
    	
    		if (!UtilidadesFacturacion.indicadorParametrizado(con, indicadores[i]))
    		{
        		in+=indicadores[i]+"          "+"Parametrizacion          		"+"El indicador no se encuentra parametrizado \n";
        		inc=false;
    		}
    		
    	}
    	
    	if (inc==false)
    	{
    		generarLogInconsistencia(in,"", "",m, forma,mapa);
    	}
    	
    	
    	forma.setInconsis(in);
    	
    	logger.info("<<<<<< INCONSISTENCIAS >>>>\n\n"+forma.getInconsis());
		
    	
    	return inc;
    }
    
    
    /**
     * 
     * @param indicador
     * @param campo
     * @param tipo
     * @param filtro
     * @param forma
     * @param mapa
     */
    private void generarLogInconsistencia(String indicador, String campo, String tipo, HashMap filtro, GeneracionArchivoPlanoIndicadoresCalidadForm forma, HashMap mapa)
    {
    	
    	StringBuffer plano= new StringBuffer(); 
    	plano.append("ARCHIVO INCONSISTENCIAS \n");
    	plano.append("Fecha:"+UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())+" \n");
    	plano.append("Hora:"+UtilidadFecha.getHoraActual()+" \n");
    	plano.append(indicador);
    	
    	String nombre = "Incon"+mapa.get("nit_0")+""+filtro.get("periodoi")+forma.getFiltro().get("ano")+forma.getFiltro().get("numArch")
		, exTxt = ".txt", exZip=".zip";

		String pathb = filtro.get("ruta").toString();
		String sCadenaSinBlancos="";
		
		for (int x=0; x < pathb.length(); x++) {
			  if (pathb.charAt(x) != ' ')
			    sCadenaSinBlancos += pathb.charAt(x);
			}
		
		if (sCadenaSinBlancos.charAt(sCadenaSinBlancos.length()-1)!='/')
		{
			sCadenaSinBlancos+="/";
		}
		
		pathb=sCadenaSinBlancos;
		
		String path = ValoresPorDefecto.getReportPath()+"facturacion/"+pathb;
		
		boolean archivo = util.TxtFile.generarTxt(plano, nombre, path, exTxt);
		
		forma.setIncon(ConstantesBD.codigoNuncaValido);
		
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+exZip+" "+path+nombre+exTxt) != ConstantesBD.codigoNuncaValido)
		{
    		forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+"facturacion/"+pathb+nombre+exZip);
    		forma.setMensaje(""+ConstantesBD.acronimoSi);
	    	}
		else
		{
			forma.setPathArchivoTxt(path+nombre+exTxt);
			forma.setMensaje(""+ConstantesBD.acronimoNo);
	    }
		
		
    }
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionGenerar(GeneracionArchivoPlanoIndicadoresCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		
		// Consultas necesarias para la generacion del archivo plano, numerador y denominador
		GeneracionArchivoPlanoIndicadoresCalidad gapic= new GeneracionArchivoPlanoIndicadoresCalidad();
		HashMap mapaind1=new HashMap();
		HashMap mapaind2=new HashMap();
		HashMap mapaind3=new HashMap();
		HashMap mapaind4=new HashMap();
		HashMap mapaind5=new HashMap();
		HashMap mapaind6=new HashMap();
		HashMap mapaind7=new HashMap();
		HashMap mapaind8=new HashMap();
		HashMap mapaind9=new HashMap();
		HashMap mapaind10=new HashMap();
		HashMap mapaind11=new HashMap();
		String periodo="";
		String datop="";
		StringBuffer cadenaPlano= new StringBuffer();
		
		logger.info("\n\n mapa filtro del jsp>>"+forma.getFiltro());
		HashMap m=new HashMap();
		datop= forma.getFiltro().get("periodo").toString();
		
		if(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
		{
			logger.info("<<< NO MANEJA MULTIEMPRESA >>>");
			//llamo funcion datos institucion
			m=gapic.consultarDatosInstitucion(con, forma.getFiltro());
			logger.info("DAtos institucion >>>>"+m);
		}
		else{
			logger.info("<<< MANEJA MULTIEMPRESA >>>");
			//cargo datos del mapa filtros traidos desde la utilidad que llena el select 
			m=gapic.consultarDatosEmpresaInstitucion(con,forma.getFiltro());
			logger.info("DAtos empresa institucion >>>>"+m);
		}
		
		if(forma.getFiltro().get("periodo").toString().equals("41"))
			periodo="'"+forma.getFiltro().get("ano").toString()+"-01-01' and '"+forma.getFiltro().get("ano").toString()+"-06-30'";
		else
			if (forma.getFiltro().get("periodo").toString().equals("43"))
			periodo="'"+forma.getFiltro().get("ano").toString()+"-07-01' and '"+forma.getFiltro().get("ano").toString()+"-12-31'";
	
		
		forma.getFiltro().put("periodo", periodo);
		m.put("periodo", datop);
		forma.getFiltro().put("periodoi", datop);
		
		
		
		
		if (buscarInconsistencias(con,m, forma.getFiltro(),forma))
		{
		forma.setIncon(0);
// 		Consultar Si existe el archivo para sacar alert de que se va aa sobrescribir 
			String nombre = m.get("nit_0").toString()+""+m.get("periodo")+forma.getFiltro().get("ano")+forma.getFiltro().get("numArch");
			
			String pathb = forma.getFiltro().get("ruta").toString();
			
			String sCadenaSinBlancos="";
			
			for (int x=0; x < pathb.length(); x++) {
				  if (pathb.charAt(x) != ' ')
				    sCadenaSinBlancos += pathb.charAt(x);
				}
			
			if (sCadenaSinBlancos.charAt(sCadenaSinBlancos.length()-1)!='/')
			{
				sCadenaSinBlancos+="/";
			}
			
			pathb=sCadenaSinBlancos;
			
			if((UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"facturacion/"+pathb, nombre+".zip") 
					|| UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+"facturacion/"+pathb, nombre+".txt"))
						&& forma.getAlerta()!=1)
				{
					logger.info("entro a sacar la alerta");
					forma.setFiltro("periodo", datop);
					forma.setAlerta(2);
					return mapping.findForward("empezar");
				}
			
			forma.setAlerta(0);
			
//		Consulta OPORTUNIDAD ASIGNACION DE CITAS EN LA CONSULTA XXXXXX		
		mapaind1=gapic.consultarOportunidadAsignacionCitasConsulta(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado >>>"+mapaind1);

		
		int b=0;
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaGeneral))
			{
				b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
									m.get("digito_verificacion_0").toString()+","+
									datop+","+
									forma.getFiltro().get("ano").toString()+","+
									m.get("tipoins_0")+","+
									m.get("cod_min_salud_0").toString()+","+
									ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaGeneral+","+
									mapaind1.get("numerador_"+j).toString()+","+
									mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}
		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaGeneral+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaInterna))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaInterna+","+
						mapaind1.get("numerador_"+j).toString()+","+
						mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}

		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAsignacioncitasConsultaMedicaInterna+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAsignacioncitasConsultaGinecobstetricia))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoOportunidadAsignacioncitasConsultaGinecobstetricia+","+
						mapaind1.get("numerador_"+j).toString()+","+
						mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}

		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAsignacioncitasConsultaGinecobstetricia+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAsignacioncitasConsultaPediatria))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoOportunidadAsignacioncitasConsultaPediatria+","+
						mapaind1.get("numerador_"+j).toString()+","+
						mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}

		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAsignacioncitasConsultaPediatria+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAsignacioncitasConsultaCirugiaGeneral))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoOportunidadAsignacioncitasConsultaCirugiaGeneral+","+
						mapaind1.get("numerador_"+j).toString()+","+
						mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}

		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAsignacioncitasConsultaCirugiaGeneral+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
//		Consulta Proporcion Cancelacion Cirugia Programada
		mapaind2=gapic.consultaProporcionCancelacionCirugiaProgramada(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 2  >>>"+mapaind2);
		
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoProporcionCancelacionCirugiaProgramada+","+
				mapaind2.get("numerador").toString()+","+
				mapaind2.get("denominador").toString()+"\n");
		
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);		
		

//		Consulta Oportunidad en la atencion consulta de urgencias
		mapaind3=gapic.consultaOportunidadAtencionConsultaUrgencias(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 3 >>>"+mapaind3);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoOportunidadAtencionConsultaUrgencias+","+
				mapaind3.get("numerador").toString()+","+
				mapaind3.get("denominador").toString()+"\n");
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
			
		
//		Consulta Oportunidad Atencion Servicios Imagenologia		
		mapaind4=gapic.consultaOportunidadAtencionServiciosImagenologia(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 4 >>>"+mapaind4);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoOportunidadAtencionServicioImagenologia+","+
				mapaind4.get("numerador").toString()+","+
				mapaind4.get("denominador").toString()+"\n");
			logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
	
	
		
		for (int j=0;j<Integer.parseInt(mapaind1.get("numRegistros").toString());j++)
		{
			if (mapaind1.get("especialidad_"+j).toString().equals(""+ConstantesBD.acronimoOportunidadAtencionConsultaOdontologiaGeneral))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoOportunidadAtencionConsultaOdontologiaGeneral+","+
						mapaind1.get("numerador_"+j).toString()+","+
						mapaind1.get("denominador_"+j).toString()+"\n");
			}
		}

		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoOportunidadAtencionConsultaOdontologiaGeneral+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
//		ojo que en este mapa viene el numerador - denominador de todos los indicadores de consultas (citas) 
		

//		Consulta oportunidad realizacion de cirugia programada		
		mapaind5=gapic.consultaCirugiaProgramada(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 5 >>>"+mapaind5);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoOportunidadRealizacionCirugiaProgramada+","+
				mapaind5.get("numerador").toString()+","+
				mapaind5.get("denominador").toString()+"\n");
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
		
		
		
//		Consulta Tasa Reingreso Pacientes Hospitalizados		
		mapaind6=gapic.consultaTasaReingresoPacientesHospitalizados(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado NUEVO >>>"+mapaind6);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoTasaReingresoPacientesHospitalizados+","+
				mapaind6.get("numerador").toString()+","+
				mapaind6.get("denominador").toString()+"\n");
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
		
		
		
		
//		Consulta Proporcion Pacientes Hipertencion Arterial Controlada		
		mapaind7=gapic.consultaProporcionPacientesHipertencionArterialControlada(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 6 >>>"+mapaind7);
	
		for (int j=0;j<Integer.parseInt(mapaind7.get("numRegistros").toString());j++)
		{
			if (mapaind7.get("acronimo_"+j).toString().equals(""+ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada))
			{
				b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+","+
						mapaind7.get("numerador").toString()+","+
						mapaind7.get("denominador_"+j).toString()+"\n");
			}
		}
		if(b==0)
		{
			cadenaPlano.append( m.get("nit_0").toString()+","+
					m.get("digito_verificacion_0").toString()+","+
					datop+","+
					forma.getFiltro().get("ano").toString()+","+
					m.get("tipoins_0")+","+
					m.get("cod_min_salud_0").toString()+","+
					ConstantesBD.acronimoProporcionPacientesHipertensionArterialControlada+","+
					"000,"+
					"000\n");
		}
		b=0;
		
		
		//ojo que en este mapa viene el numerador de tasa de infeccion intrahospitalaria 
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
//		Consulta Tasa Mortalidad Intrahospitalaria Despues Dos Dias		
		mapaind8=gapic.consultaTasaMortalidadIntrahospitalariaDespuesDosDias(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 7 >>>"+mapaind8);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoTasaMortalidadIntrahospitalaria+","+
				mapaind8.get("numerador").toString()+","+
				mapaind8.get("denominador").toString()+"\n");
		//ojo denominador es el mismo para tasas de infeccion intrahospitalaria

		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
			
/********NO SE HACE PORQUE SE OBTIENEN LOS RESULTADOS DE OTRAS CONSULTAS **********		
//		Consulta Tasa Infeccion Intrahospitalaria		
		mapaind9=gapic.consultaTasaInfeccionIntrahospitalaria(con, forma.getFiltro());
**********************************************************************************/

	for (int j=0;j<Integer.parseInt(mapaind7.get("numRegistros").toString());j++)
		{
			if (mapaind7.get("acronimo_"+j).toString().equals(""+ConstantesBD.acronimoTasaInfeccionIntrahospitalaria))
			{b++;
				cadenaPlano.append( m.get("nit_0").toString()+","+
						m.get("digito_verificacion_0").toString()+","+
						datop+","+
						forma.getFiltro().get("ano").toString()+","+
						m.get("tipoins_0")+","+
						m.get("cod_min_salud_0").toString()+","+
						ConstantesBD.acronimoTasaInfeccionIntrahospitalaria+","+
						mapaind7.get("denominador_"+j).toString()+","+
						mapaind8.get("denominador").toString()+"\n");
			}
		} 
	
	if(b==0)
	{
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoTasaInfeccionIntrahospitalaria+","+
				"000,"+
				"000\n");
	}
	b=0;
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
//		Consulta Proporcion Vigilancia Eventos Adversos		
		mapaind10=gapic.consultaProporcionVigilanciaEventosAdversos(con, forma.getFiltro());
		logger.info("\n\n Mapa Resultado 8 >>>"+mapaind10);
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoProporcionVigilanciaEventosAdversos+","+
				mapaind10.get("numerador").toString()+","+
				mapaind10.get("denominador").toString()+"\n");

		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
//		Consulta Tasa Satisfaccion Global		
		mapaind11=gapic.consultaTasaSatisfaccionGlobal(con, forma.getFiltro());		
		logger.info("\n\n Mapa Resultado 9 >>>"+mapaind11);
		
		cadenaPlano.append( m.get("nit_0").toString()+","+
				m.get("digito_verificacion_0").toString()+","+
				datop+","+
				forma.getFiltro().get("ano").toString()+","+
				m.get("tipoins_0")+","+
				m.get("cod_min_salud_0").toString()+","+
				ConstantesBD.acronimoTasaSatisfaccionGlobal+","+
				mapaind11.get("numerador").toString()+","+
				mapaind11.get("denominador").toString()+"\n");
		
		
		logger.info("\n\n\n\n\n CADENA ARCHIVO PLANO >>>>\n\n"+cadenaPlano);
		
		m.put("usuario", usuario.getLoginUsuario());
		generarArchivoPlano(con, cadenaPlano, m, forma.getFiltro(),forma);
		
		forma.setArchivo(forma.getFiltro().get("ruta")+""+forma.getFiltro().get("periodoi"));
		
		//Cargar Archivo indicadores calidad
		return mapping.findForward("verArchivo");
		
		}
		
		
		logger.info("<<<<<< INCONSISTENCIAS >>>>"+forma.getInconsis());
		
		//Cargar Archivo inconsistencia
		return mapping.findForward("verArchivo");
	}

	
	
	/**
	 * Metodo encargado de generar el "Archivo Plano" 
	 * @param con
	 * @param GeneracionArchivoPlanoIndicadoresCalidadForm
	 */
	private void generarArchivoPlano(Connection con, StringBuffer plano, HashMap m, HashMap filtro, GeneracionArchivoPlanoIndicadoresCalidadForm forma)
	{
		
		String nombre = m.get("nit_0").toString()+""+m.get("periodo")+forma.getFiltro().get("ano")+forma.getFiltro().get("numArch")
						, exTxt = ".txt",exZip = ".zip";
	
		String pathb = filtro.get("ruta").toString();
		
		String sCadenaSinBlancos="";
		
		for (int x=0; x < pathb.length(); x++) {
			  if (pathb.charAt(x) != ' ')
			    sCadenaSinBlancos += pathb.charAt(x);
			}
		
		if (sCadenaSinBlancos.charAt(sCadenaSinBlancos.length()-1)!='/')
		{
			sCadenaSinBlancos+="/";
		}
		
		pathb=sCadenaSinBlancos;
		
		
		String path = ValoresPorDefecto.getReportPath()+"facturacion/"+pathb;
	    boolean archivo = util.TxtFile.generarTxt(plano, nombre, path, exTxt);
	    
	    if(archivo)
	    {
	    	// >>> GUARDAR EN LA TABLA LOG_ARCH_IND_CALIDAD <<< 
	    	GeneracionArchivoPlanoIndicadoresCalidad g=new GeneracionArchivoPlanoIndicadoresCalidad();
	    	HashMap mapa=new HashMap();
	    	mapa.put("institucion",m.get("codigo_0") );
	    	mapa.put("usuario", m.get("usuario") );
	    	if (m.get("periodo").toString().equals("41"))
	    		mapa.put("periodo",ConstantesBD.acronimoSi );
	    	else
	    		mapa.put("periodo",ConstantesBD.acronimoNo);
	    	mapa.put("archivo",nombre);
	    	mapa.put("path", pathb);
	    	g.guardarLog(con, mapa);
	    	
			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+exZip+" "+path+nombre+exTxt) != ConstantesBD.codigoNuncaValido)
			{
	    		forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+"facturacion/"+pathb+nombre+exZip);
	    		forma.setMensaje(""+ConstantesBD.acronimoSi);
		    	}
			else
			{
				forma.setPathArchivoTxt(path+nombre+exTxt);
				forma.setMensaje(""+ConstantesBD.acronimoNo);
		    }
	    	
	    
	    }
	    
	}
	
	
	
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(GeneracionArchivoPlanoIndicadoresCalidadForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset();	
		if(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
		{
			logger.info("<<< NO MANEJA MULTIEMPRESA >>>");
			forma.setFiltro("codinstitucion",usuario.getCodigoInstitucionInt()+"");
			forma.setFiltro("nominstitucion",Utilidades.obtenerNombreInstitucion(con,usuario.getCodigoInstitucionInt())+"");
		}
		else{
			logger.info("<<< MANEJA MULTIEMPRESA >>>");
			forma.setInstituciones(Utilidades.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
		}
		
		GeneracionArchivoPlanoIndicadoresCalidad gapic= new GeneracionArchivoPlanoIndicadoresCalidad();
		//consulta para postular el path
		forma.setFiltro("ruta", gapic.consultarUltimoPath(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");		
	}	
	
		
}