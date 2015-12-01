package com.princetonsa.pdf;

public interface IConstantesReporteAgrupadoFacturacion {

	//Se definen los nombres de las KEY del mapa de los parametros del reporte 
	public static final String tamanoReporte="tamanoReporte";
	public static final String mediaCarta="mediaCarta";
	public static final String carta="Carta";
	public static final String tipoPieDePagina="tipoPieDePagina";
	public static final String institucionBasica="institucionBasica";
	public static final String numRegistros="numRegistros";
	public static final String codigoFactura="codigoFactura_";
	public static final String nombrePaciente="nombrePaciente";
	public static final String identificacionPaciente="identificacionPaciente";
	public static final String noIngreso="noIngreso";
	public static final String direccion="direccion";
	public static final String telefonoPaciente="telefonoPaciente";
	public static final String fechaIngresoPaciente="fechaIngresoPaciente";
	public static final String fechaEgreso="fechaEgreso";
	public static final String valorEnletras=" VALOR EN LETRAS: ";
	public static final String totalCargos=" Total Cargos";
	public static final String cuotaModeradora=" Cuota Moderadora";
	public static final String totalEmpresa=" Total Empresa";
	public static final String valorEnLetras="valorEnLetras";
	public static final String totalCargo="totalCargo";
	public static final String valorBrutoPaciente="valorBrutoPaciente";
	public static final String valorConvenio="valorConvenio";
	public static final String descripcion="descripcion";
	public static final String detalleProcedimiento="detalleProcedimiento";
	public static final String valorTotal="valorTotal";
	public static final String usarDecimales="usarDecimales";
	public static final String falseCadena="false";
	public static final String original="original";
	public static final String piefactura="piefactura";

	//linea en el reporte para hacer la firma de paciente y medico 
	public static final String lineaFirma="___________________________";
	public static final String firmaFacturadorParentesisAbierto="FIRMA FACTURADOR (";
	public static final String parentesisCerrado=") ";

	//titulo de firma de reporte
	public static final String firmapaciente="FIRMA DEL PACIENTE";

	//tirulo de original y copia 
	public static final String originalMayuscula="Original";
	public static final String copia="Copia";

	//nombres de campos 
	public static final String tituloDescripcion="DESCRIPCIÓN";
	public static final String tituloDetalleProcedimiento="DETALLE PROCEDIMIENTO";
	public static final String titulovalorTotal="Vr. TOTAL";

	//ubicacion de logo de los reportes
	public static final String ubicacionLogo="ubicacionLogoReportes";

	//tipos de montos de pago 
	public static final String nombreTipoMontoPagoCuotaModeradora="Cuota Moderadora";
	public static final String nombreTipoMontoPagoCopago="Copago";

}
