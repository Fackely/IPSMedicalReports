package com.servinte.axioma.generadorReporte.comun;

public interface IConstantesReporte {
	
	/***************************************************************************/
	/**						FORMATOS DE REPORTES   						****/										
	/***************************************************************************/
	public static final int formatoA=1;
	public static final int formatoPlanoA=2;
	public static final int formatoB=3;
	public static final int formatoPlanoB=4;
	public static final int formatoC=5;
	public static final int formatoPlanoC=6;
	
	/***************************************************************************/
	/**						PARAMETROS DE REPORTES   						****/										
	/***************************************************************************/
	public static final String fecha="fecha";
	public static final String convenio="convenio";
	public static final String contrato="contrato";
	public static final String nombreInstitucion="nombreInstitucion";
	public static final String nitInstitucion="nitInstitucion";
	public static final String tipoConsulta="tipoConsulta";
	public static final String rutaLogo="rutaLogo";
	public static final String ubicacionLogo="ubicacionLogo";
	public static final String usuarioProceso="usuarioProceso";
	public static final String actividadEconomica="actividadEconomica";
	public static final String direccion="direccion";
	public static final String centroAtencion="centroAtencion";
	public static final String telefono="telefono";
	public static final String institucionlabel="InstitucionLabel";
	public static final String loginUsuarioProceso="loginUsuarioProceso";
	public static final String valorMensualContrato="valorMensualContrato";
	public static final String porcentajeGastoMensual="porcentajeGastoMensual";
	public static final String valorGastoMensual="valorGastoMensual";
	public static final String fechaProcesa="fechaProcesa";
	
	
	/***************************************************************************/
	/**	  PARAMETROS DE REPORTE TOTAL SERV ART VALORIZADO POR CONVENIO   	****/										
	/***************************************************************************/
	//public static final String totalConvenio = "Total Convenio";
	public static final String servicio = "Servicio";
	public static final String articulo = "Articulo";
	public static final String medInsumo = "Med/Ins";
	public static final String grupoServicio = "Grupo Servicio";
	public static final String claseInv = "Clase Inventario";
	public static final String nivelAten = "Nivel Atención";
	public static final String ordCapSub = "Total Servicios Artículos Valorizados por Convenio";
	
	/***************************************************************************/
	/**	  PARAMETROS DE REPORTE HISTORIA CLINICA						  	****/										
	/***************************************************************************/
	public static final String nombrepaciente="nombrepaciente";
	public static final String fechaNacimiento="fechaNacimiento";
	public static final String estadoCivil="estadoCivil";
	public static final String residencia="residencia";
	public static final String fechahoraingreso="fechahoraingreso";
	public static final String fechahoraEgreso="fechahoraEgreso";
	public static final String acompanantePaciente="acompanantePaciente";
	public static final String responsablePaciente="responsablePaciente";
	public static final String convenioPaciente="convenioPaciente";
	public static final String tipoNumeroID="tipoNumeroID";
	public static final String edad="edad";
	public static final String ocupacion="ocupacion";
	public static final String telefonoPaciente="telefonoPaciente";
	public static final String viaIngreso="viaIngreso";
	public static final String viaEgreso="viaEgreso";
	public static final String telParentescoUno="telParentescoUno";
	public static final String telParentescoDos="telParentescoDos";
	public static final String nombreParentescoUno="nombreParentescoUno";
	public static final String nombreparentescoDos="nombreparentescoDos";
	public static final String tipoAfiliado="tipoAfiliado";
	public static final String sexo="sexo";
	public static final String regimen="regimen";
	
	/***************************************************************************/
	/**	  PARAMETROS DE REPORTE NOTAS PACIENTES						  	****/										
	/***************************************************************************/
	
	public static final String notasNaturaleza="Notas Naturaleza";
	public static final String centroAtencionOrigen="centroAtencionOrigen";
	public static final String centroAtencionDuenio="centroAtencionDuenio";
	public static final String direccionCentroAtencionOrigen="direccionCentroAtencionOrigen";
	public static final String usuarioElaboraNota="usuarioElaboraNota";
	public static final String institucionMultiempresa="esMultiempresa";
	public static final String controlAbonoPacientePorIngreso="controlAbonoPacientePorIngreso";
	public static final String manejoEspecialInstOdonto="manejoEspecialInstOdonto";
	public static final String reporteRango="esReporteRango";
	
	
}
